import java.io.File;
import java.util.Scanner;
import java.util.Stack;
public class PtBF {
	public static String code;
	public static void main(String[] args)throws Exception{
		code=new Scanner(new File("BF.txt")).useDelimiter("\\Z").next().replace("\t","");
		String expr[]=code.replace("\r","").split("\n"),out="",e[],extra="";
		Stack<String>end=new Stack<>();
		int i;
		for(String exp:expr){
			e=exp.split(" ");
			if(e[0].equals("for")){
				if(!extra.contains(e[1]))extra+=e[1];
				out+=e[1]+"+"+e[2]+" a+0 b+ b[ b[ b- b] ";
				end.push(e[1]+"+ "+e[1]+"[ "+e[1]+"- a+ b+ "+e[1]+"] a[ a- "+e[1]+"+ a] b-"+e[3]+" b] "+e[1]+"[ "+e[1]+"- "+e[1]+"] ");
			}else if(e[0].equals("if")){
				i=0;
				while(!e[i].equals("then")){
					out+=e[i+1]+"[ "+e[i+1]+"- c+ d+ "+e[i+1]+"] d[ d- "+e[i+1]+"+ d] ";
					try{
						out+="d+"+String.valueOf(Integer.parseInt(e[i+3]))+" ";
					}catch(Exception n){
						out+=e[i+3]+"[ "+e[i+3]+"- d+ h+ "+e[i+3]+"] h[ h- "+e[i+3]+"+ h] ";
					}
					if(e[i].equals("and")){
						if(e[i+2].equals("<"))out+="c[ c- d- f- d[ f+ d[ d- g+ d] d] g[ g- d+ g] c] f+ d[ d- d] ";
						else if(e[i+2].equals(">"))out+="f- c[ f+ c[ c- g+ c] c] g[ g- c+ g] d[ d- c- f- c[ f+ c[ c- g+ c] c] g[ g- c+ g] d] f+ c[ c- c] ";
						else if(e[i+2].equals("="))out+="d[ d- c- d] c[ f- c[ c- c] c] f+ ";
						else if(e[i+2].equals("!"))out+="d[ d- c- d] c[ f+ c[ c- c] c] ";
						else if(e[i+2].equals("<="))out+="f+ c[ f- c[ c- g+ c] c] g[ g- c+ g] d[ d- c- f+ c[ f- c[ c- g+ c] c] g[ g- c+ g] d] c[ c- c] ";
						else if(e[i+2].equals(">="))out+="c[ c- d- f+ d[ f- d[ d- g+ d] d] g[ g- d+ g] c] d[ d- d] ";
						out+="e[ e- c+ e] c[ f[ e+ d+ f- f] d[ f+ d- d] c- c] f[ f- f] ";
					}else{
						if(e[i+2].equals("<"))out+="c[ c- d- e- d[ e+ d[ d- f+ d] d] f[ f- d+ f] c] e+ d[ d- d] ";
						else if(e[i+2].equals(">"))out+="e- c[ e+ c[ c- f+ c] c] f[ f- c+ f] d[ d- c- e- c[ e+ c[ c- f+ c] c] f[ f- c+ f] d] e+ c[ c- c] ";
						else if(e[i+2].equals("="))out+="d[ d- c- d] c[ e- c[ c- c] c] e+ ";
						else if(e[i+2].equals("!"))out+="d[ d- c- d] c[ e+ c[ c- c] c] ";
						else if(e[i+2].equals("<="))out+="e+ c[ e- c[ c- f+ c] c] f[ f- c+ f] d[ d- c- e+ c[ e- c[ c- f+ c] c] f[ f- c+ f] d] c[ c- c] ";
						else if(e[i+2].equals(">="))out+="c[ c- d- e+ d[ e- d[ d- f+ d] d] f[ f- d+ f] c] d[ d- d] ";
					}
					i+=4;
				}
				out+="f+ e[ f- e[ e- e] ";
				end.push("e] f[ f- f] ");
			}else if(e[0].equals("else")){
				end.pop();
				out+="e] f[ f- ";
				end.push("f] ");
			}else if(e[0].equals("out")){
				if(e.length==2){
					try{
						out+="c+"+String.valueOf(Integer.parseInt(e[1]))+" c. c[ c- c] ";
					}catch(Exception n){
						out+=e[1]+". ";
					}
				}else if(!e[2].equals("*")){
					try{
						if(Integer.parseInt(e[3])<20)out+=e[1]+e[2]+e[3]+" "+e[1]+"."+((e.length==4)?"":e[5])+" "+e[1]+((e[2].equals("+"))?"-":"+")+e[3]+" ";
						else out+=e[1]+"[ "+e[1]+"- c+ d+ "+e[1]+"] d[ d- "+e[1]+"+ d] c"+e[2]+e[3]+" c."+((e.length==4)?"":e[5])+" c[ c- c] ";
					}catch(Exception n){
						out+=e[1]+"[ "+e[1]+"- c+ d+ "+e[1]+"] d[ d- "+e[1]+"+ d] "+e[3]+"[ "+e[3]+"- d+ c"+e[2]+" "+e[3]+"] d[ d- "+e[3]+"+ d] c."+((e.length==4)?"":e[5])+" c[ c- c] ";
					}
				}else{
					try{
						out+="c+"+String.valueOf(Integer.parseInt(e[1]))+" c."+e[3]+" c[ c- c] ";
					}catch(Exception n){
						out+=e[1]+"."+e[3]+" ";
					}
				}
			}else if(e[0].equals("end")){
				out+=end.pop();
			}else if(e[0].equals("dump")){
				out+="# ";
			}else{
				if(e.length==1&&!extra.contains(e[0])){
					extra+=e[0];
				}else if(!extra.contains(e[0])){
					extra+=e[0];
					try{
						out+=e[0]+"+"+String.valueOf(Integer.parseInt(e[2]))+" ";
					}catch(Exception n){
						if(e[2].equals("inp"))out+=e[0]+", ";
						else out+=e[2]+"[ "+e[2]+"- a+ "+e[0]+"+ "+e[2]+"] a[ a- "+e[2]+"+ a] ";;
					}
				}else if(e[1].equals("=")){
					try{
						out+=e[0]+"[ "+e[0]+"- "+e[0]+"] "+e[0]+"+"+String.valueOf(Integer.parseInt(e[2]))+" ";
					}catch(Exception n){
						if(e[2].equals("inp"))out+=e[0]+", ";
						else out+=e[0]+"[ "+e[0]+"- "+e[0]+"] "+e[2]+"[ "+e[2]+"- a+ "+e[0]+"+ "+e[2]+"] a[ a- "+e[2]+"+ a] ";
					}
				}else{
					try{
						out+=e[0]+e[1]+String.valueOf(Integer.parseInt(e[2]))+" ";
					}catch(Exception n){
						if(e[2].equals("inp"))out+="a, a[ a- "+e[0]+e[1]+" a] ";
						else out+=e[2]+"[ "+e[2]+"- a+ "+e[0]+e[1]+" "+e[2]+"] a[ a- "+e[2]+"+ a] ";
					}
				}
			}
		}
		expr=out.split(" ");
		out="";
		String varpos="abcdefgh"+extra;
		int p=varpos.indexOf(expr[0].charAt(0));
		//p=0;
		for(String exp:expr){
			if(exp.equals("#")){
				out+="#";
			}else{
				if(varpos.indexOf(exp.charAt(0))>p)for(int x=0;x<varpos.indexOf(exp.charAt(0))-p;x++)out+=">";
				if(varpos.indexOf(exp.charAt(0))<p)for(int x=0;x<p-varpos.indexOf(exp.charAt(0));x++)out+="<";
				p=varpos.indexOf(exp.charAt(0));
				if(exp.length()==2)out+=exp.charAt(1);
				else for(int x=0;x<Integer.valueOf(exp.substring(2));x++)out+=exp.charAt(1);
			}
		}
		while(out.contains("<>")||out.contains("><")||out.contains("+-")||out.contains("-+")){
			out=out.replace("<>","");
			out=out.replace("><","");
			out=out.replace("+-","");
			out=out.replace("-+","");
		}
		System.out.print(out);
	}
}
/*
<SYNTAX>

for var start(inc) end(exc)
//code
end

if cond1 then
/code
end

if cond1 or cond2 then
/code
end

if cond1 and cond2 then
/code
end

cond : var1 <,>,=,!,<=,>= var2/const

out var/const (+ const)

increment: var + const

</SYNTAX>
*/