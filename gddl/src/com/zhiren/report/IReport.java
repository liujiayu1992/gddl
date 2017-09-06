package com.zhiren.report;

import java.util.ArrayList;

public class IReport extends Report {
	private ArrayList tabels=null;
	public ArrayList getTabels(){
		return this.tabels;
	}
	public void setTabels(ArrayList tabels){
		this.tabels=tabels;
	}
 public String getAllPagesHtml(){
	   StringBuffer sb=new StringBuffer();
	   sb.append("<center>");	
	 if(tabels!=null){
			int c=0;
			for(int i=0;i<tabels.size();i++){
				super.body=(Table)this.tabels.get(i);
			    int cc=((Table)tabels.get(i)).getPages();
			    c+=cc;
			    for(int j=1;j<=cc;j++){
			    	sb.append(getPageHtml(j, cc, c-cc));
			    }
			}
		}
		sb.append("</center>");
		return  sb.toString();
 } 
}
