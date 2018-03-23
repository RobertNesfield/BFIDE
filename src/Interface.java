import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.*;

public class Interface extends Application{
	private Stage stage;
	private Scene scene;
	private Group root;
	private Label[]memory=new Label[256];
	private Rectangle pointer;
	private TextArea code,input,output;
	private Button run,runF,stop,clear;
	private ComboBox<Integer>speed;
	private boolean fast=true;
	
	@Override
	public void start(Stage primaryStage){
		Interpreter.init();
		
		stage=primaryStage;
		root=new Group();
		scene=new Scene(root,960,750);
		stage.setScene(scene);
		stage.setTitle("BF interpreter/debugger");
		stage.sizeToScene();
		stage.setResizable(false);
		stage.setOnCloseRequest(e->System.exit(0));
		
		Rectangle rect=new Rectangle();
		root.getChildren().add(rect);
		rect.setX(0);
		rect.setY(0);
		rect.setWidth(960);
		rect.setHeight(160);
		
		pointer=new Rectangle();
		root.getChildren().add(pointer);
		pointer.setX(0);
		pointer.setY(0);
		pointer.setWidth(30);
		pointer.setHeight(20);
		pointer.setFill(Color.DARKRED);
		
		for(int i=0;i<256;i++){
			memory[i]=new Label("000");
			root.getChildren().add(memory[i]);
			memory[i].setLayoutX(30*(i%32)+4);
			memory[i].setLayoutY(20*(i/32)+3);
			memory[i].setFont(new Font("Courier New",12));
			memory[i].setTextFill(Color.LIGHTGRAY);
		}
		
		code=new TextArea();
		root.getChildren().add(code);
		code.setPrefWidth(470);
		code.setPrefHeight(440);
		code.setLayoutX(5);
		code.setLayoutY(205);
		code.setWrapText(true);
		code.setFont(new Font("Courier New",10));
		
		output=new TextArea();
		root.getChildren().add(output);
		output.setPrefWidth(470);
		output.setPrefHeight(440);
		output.setLayoutX(485);
		output.setLayoutY(205);
		output.setFont(new Font("Courier New",10));
		output.setEditable(false);
		
		input=new TextArea();
		root.getChildren().add(input);
		input.setPrefWidth(950);
		input.setPrefHeight(90);
		input.setLayoutX(5);
		input.setLayoutY(655);
		input.setWrapText(true);
		input.setFont(new Font("Courier New",10));
		
		runF=new Button("Run");
		root.getChildren().add(runF);
		runF.setPrefWidth(100);
		runF.setPrefHeight(25);
		runF.setLayoutX(5);
		runF.setLayoutY(170);
		runF.setOnAction(e->{
			if(!Interpreter.running()){
				fast=true;
				
				Interpreter.init();
				Platform.runLater(()->{
					for(int i=0;i<256;i++)memory[i].setText("000");
					pointer.setX(0);
					pointer.setY(0);
				});
				
				new Thread(()->Interpreter.run(code.getText(),input.getText(),true)).start();
			}
		});
		
		speed=new ComboBox<>(FXCollections.observableArrayList(1,5,10,50,100,500,1000));
		root.getChildren().add(speed);
		speed.setPrefWidth(80);
		speed.setPrefHeight(25);
		speed.setLayoutX(310);
		speed.setLayoutY(170);
		speed.getSelectionModel().selectLast();
		speed.getSelectionModel().selectPrevious();
		speed.getSelectionModel().selectPrevious();
		
		Label ips=new Label("Instructions per second");
		root.getChildren().add(ips);
		ips.setLayoutX(393);
		ips.setLayoutY(175);
		
		run=new Button("Debug at");
		root.getChildren().add(run);
		run.setPrefWidth(100);
		run.setPrefHeight(25);
		run.setLayoutX(205);
		run.setLayoutY(170);
		run.setOnAction(e->{
			if(!Interpreter.running()){
				fast=false;
				
				Interpreter.init();
				Platform.runLater(()->{
					for(int i=0;i<256;i++)memory[i].setText("000");
					pointer.setX(0);
					pointer.setY(0);
				});
				
				new Thread(()->Interpreter.run(code.getText(),input.getText(),false)).start();
			}
		});
		
		stop=new Button("Terminate");
		root.getChildren().add(stop);
		stop.setPrefWidth(100);
		stop.setPrefHeight(25);
		stop.setLayoutX(650);
		stop.setLayoutY(170);
		stop.setOnAction(e->Interpreter.stop());
		
		clear=new Button("Reset");
		root.getChildren().add(clear);
		clear.setPrefWidth(100);
		clear.setPrefHeight(25);
		clear.setLayoutX(855);
		clear.setLayoutY(170);
		clear.setOnAction(e->{
			Interpreter.stop();
			Interpreter.init();
			Platform.runLater(()->{
				for(int i=0;i<256;i++)memory[i].setText("000");
				pointer.setX(0);
				pointer.setY(0);
				code.setText("");
				input.setText("");
				output.setText("");
			});
		});
		
		stage.show();
		
		new Thread(()->{
			while(true){
				Platform.runLater(()->{
					if(!fast){
						final int[]mem=Interpreter.memory();
						final String buffer=(mem[0]>99)?"":(mem[0]>9)?"0":"00";
						if(mem[1]<256){
							memory[mem[1]].setText(buffer+String.valueOf(mem[0]));
							pointer.setX(30*(mem[1]%32));
							pointer.setY(20*(mem[1]/32));
						}else{
							pointer.setX(0);
							pointer.setY(-20);
						}
					}
					if(!output.getText().equals(Interpreter.output()))try{output.appendText(Interpreter.output().substring(output.getText().length()));}catch(Exception e){output.setText(Interpreter.output());}
				});
				if(!fast){
					Interpreter.step();
					try{Thread.sleep(1000/speed.getValue());}catch(Exception e){}
				}else try{Thread.sleep(1);}catch(Exception e){}
			}
		}).start();
		
		new Thread(()->Interpreter.run("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.","",fast)).start();
	}
	
	public static void main(String[]args){launch(args);}
}