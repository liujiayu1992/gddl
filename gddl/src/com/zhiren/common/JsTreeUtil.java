package com.zhiren.common;

public class JsTreeUtil {

	public static final int TreeType_ImgTree = 0;
	public static final int TreeType_ImgCheckTree = 1;
	public static final int TreeType_Resource = 2;
	private String StrTreeCode = "";
	private int jib = -1;
	private String TreeType = "";
	
	public JsTreeUtil(int treeType) {
		switch(treeType) {
			case TreeType_ImgTree:
				break;
			case TreeType_ImgCheckTree:
				TreeType = "Check";
				break;
			case TreeType_Resource:
				TreeType = "Resource";
				break;
			default : break;
		}
	}
	public String getTree() {
		return StrTreeCode;
	}
	
	public void CreateRoot(String RootName) {
		if(TreeType.equals("Resource")) {
			StrTreeCode = TreeType+"Tree = new Array();\n ";
			jib = 0;
			return;
		}
		String check = "";
		if(TreeType.equals("Check")) {
			check = "\"checked\",";
		}
		StrTreeCode = TreeType+"Tree = new Array();\n "
		+TreeType+"Root = new Array(-1,0,\""+RootName+"\","+check+"0,'#');\n"
		+TreeType+"Tree[0]= "+TreeType+"Root;\n";
		jib = 1;
	}
	
	public void addNode(ResultSetList rsl) {
		int cols = rsl.getColumnCount();
		String treeNode = TreeType+"TreeNode" + jib + " = new Array();\n";
		while(rsl.next()) {
			treeNode += TreeType+"TreeNode" + jib +"[" + rsl.getRow() + "] =  new Array(";
			for(int i = 0; i < cols ; i++) {
				treeNode += "\""+rsl.getString(i)+"\"";
				if(i+1 == cols) {
					treeNode +=");";
				}else {
					treeNode += ",";
				}
			}
		}
		treeNode += TreeType+"Tree[" + jib + "]= "+TreeType+"TreeNode" + jib +";\n";
		StrTreeCode += treeNode;
		jib++;
	}
	
	public static String getOprateScript(String Operate,boolean isDelete,long DelId){
		if(Operate == null || "".equals(Operate)){
			return "";
		}
		String arrNote[] = Operate.split(";");
		StringBuffer array = new StringBuffer();
		for(int i=0; i < arrNote.length ; i++){
			if("".equals(arrNote[i])){
				continue;
			}
			if(isDelete){
				if(arrNote[i].indexOf(""+DelId)!=-1){
					continue;
				}
			}
			String str[] = arrNote[i].split(",");
			if(str[0].equals("O")){
				array.append("OpenOrClose(document.getElementById(\"");
				array.append(str[1]);
				array.append("\"));");
			}else{
				array.append("ClickNode(document.getElementById(\"");
				array.append(str[1]);
				array.append("\").nextSibling);");
			}
		}
		return array.toString();
	}
}
