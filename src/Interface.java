import javafx.application.Application;
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
	
	@Override
	public void start(Stage primaryStage){
		Interpreter.init();
		
		stage=primaryStage;
		root=new Group();
		scene=new Scene(root,640,750);
		stage.setScene(scene);
		stage.setTitle("BF interpreter/debugger");
		stage.sizeToScene();
		stage.setResizable(false);
		
		Rectangle rect=new Rectangle();
		root.getChildren().add(rect);
		rect.setX(0);
		rect.setY(0);
		rect.setWidth(640);
		rect.setHeight(320);
		
		pointer=new Rectangle();
		root.getChildren().add(pointer);
		pointer.setX(0);
		pointer.setY(0);
		pointer.setWidth(40);
		pointer.setHeight(20);
		pointer.setFill(Color.DARKRED);
		
		for(int i=0;i<256;i++){
			memory[i]=new Label("000");
			root.getChildren().add(memory[i]);
			memory[i].setLayoutX(40*(i%16)+8);
			memory[i].setLayoutY(20*(i/16)+3);
			memory[i].setFont(new Font("Courier New",12));
			memory[i].setTextFill(Color.LIGHTGRAY);
		}
		
		stage.show();
	}
	
	public static void main(String[]args){launch(args);}
}