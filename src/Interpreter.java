public class Interpreter{
	private static byte[]bytes=new byte[256];
	private static byte pointer;
	private static String out,inp;
	private static boolean stopped=true;
	
	public static void init(){
		for(int i=0;i<256;i++)bytes[i]=0;
		pointer=0;
	}
	
	public static void run(String code,String input){
		if(stopped&&brace(code)){
			for(int i=0;i<256;i++)bytes[i]=0;
			pointer=0;
			out="";
			inp=input;
			
			stopped=false;
			exec(code);
			stopped=true;
		}
	}
	
	private static void exec(String code){
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
					break;
				case'<':
					pointer--;
					break;
				case'.':
					out+=(char)bytes[pointer];
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
					
					while(!stopped&&bytes[pointer]!=0)exec(code.substring(i+1,j));
					i=j;
			}
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
	
	public static void stop(){stopped=true;}
	public static boolean running(){return !stopped;}
	public static String output(){return out;}
	public static byte[] memory(){return bytes.clone();}
	public static int position(){return pointer;}
}