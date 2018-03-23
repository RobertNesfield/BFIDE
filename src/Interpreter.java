public class Interpreter{
	private static byte[]bytes=new byte[30000];
	private static int pointer;
	private static String out,inp;
	private static boolean stopped=true,pause=false;
	
	public static void init(){
		for(int i=0;i<30000;i++)bytes[i]=0;
		pointer=0;
		out="";
	}
	
	public static void run(String code,String input,boolean fast){
		if(brace(code)){
			inp=input;
			stopped=false;
			pause=false;
			if(fast)execF(code);else exec(code);
			stopped=true;
		}else out="ERROR: Unmatched Braces";
	}
	
	private static void execF(String code){
		for(int i=0;i<code.length();i++){
			if(stopped)break;
			switch(code.charAt(i)){
				case'+':
					bytes[pointer]++;
					break;
				case'-':
					bytes[pointer]--;
					break;
				case'>':
					pointer++;
					if(pointer==30000)pointer=0;
					break;
				case'<':
					pointer--;
					if(pointer==-1)pointer=29999;
					break;
				case'.':
					out+=(bytes[pointer]==9||bytes[pointer]==10)?(char)(bytes[pointer]&0xFF):String.valueOf((char)(bytes[pointer]&0xFF)).replaceAll("\\p{Cntrl}","");
					break;
				case',':
					if(inp.length()==0)bytes[pointer]=0;
					else{
						bytes[pointer]=(byte)inp.charAt(0);
						inp=inp.substring(1);
					}
					break;
				case'[':
					int toFind=1,j=i;
					
					while(toFind!=0){
						j++;
						switch(code.charAt(j)){
							case'[':
								toFind++;
								break;
							case']':
								toFind--;
						}
					}
					
					while(!stopped&&bytes[pointer]!=0)execF(code.substring(i+1,j));
					i=j;
			}
		}
	}
	
	private static void exec(String code){
		for(int i=0;i<code.length();i++){
			while(pause&&!stopped)System.out.print("");
			if(stopped)break;
			switch(code.charAt(i)){
				case'+':
					bytes[pointer]++;
					break;
				case'-':
					bytes[pointer]--;
					break;
				case'>':
					pointer++;
					if(pointer==30000)pointer=0;
					break;
				case'<':
					pointer--;
					if(pointer==-1)pointer=29999;
					break;
				case'.':
					out+=(bytes[pointer]==9||bytes[pointer]==10)?(char)(bytes[pointer]&0xFF):String.valueOf((char)(bytes[pointer]&0xFF)).replaceAll("\\p{Cntrl}","");
					break;
				case',':
					if(inp.length()==0)bytes[pointer]=0;
					else{
						bytes[pointer]=(byte)inp.charAt(0);
						inp=inp.substring(1);
					}
					break;
				case'[':
					int toFind=1,j=i;
					
					while(toFind!=0){
						j++;
						switch(code.charAt(j)){
							case'[':
								toFind++;
								break;
							case']':
								toFind--;
						}
					}
					
					while(!stopped&&bytes[pointer]!=0){
						pause=true;
						exec(code.substring(i+1,j));
					}
					i=j;
			}
			pause=true;
		}
	}
	
	private static boolean brace(String code){
		int toFind=0;
		
		for(char chr:code.toCharArray()){
			switch(chr){
				case'[':
					toFind++;
					break;
				case']':
					toFind--;
					if(toFind<0)return false;
			}
		}
		
		return toFind==0;
	}
	
	public static void step(){pause=false;}
	public static void stop(){stopped=true;}
	public static boolean running(){return !stopped;}
	public static String output(){return out;}
	public static int[]memory(){return new int[]{bytes[pointer]&0xFF,pointer};}
}