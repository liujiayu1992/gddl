package com.zhiren.report;
import java.util.*;


public class Document {
	public List paragraphs=new ArrayList();
	private int left=50;
	private int right=50;
	private int top=50;
	private int bottom=50;
	private int width=700;
	private String align="center";
	private int border = 0;
	
	public void setMarginLeft(int left){
		this.left=left;
	}
	
	public void setMarginRgith(int right){
		this.right=right;
	}
	
	public void setMarginTop(int top){
		this.top=top;
	}
	
	public void setMarginBottom(int bottom){
		this.bottom=bottom;
	}
	
	public void setTableWidth(int width){
		this.width = width;
	}
	
	public void setBorder(int border){
		this.border = border;
	}
	
	public void setAlign(String align){
		this.align = align;
	}
	
	public String getHtml(){
		StringBuffer sb=new StringBuffer();
		if(width==0){
			if (paragraphs.size()>0) {
				for (int i=0 ;i<paragraphs.size();i++){
					sb.append(((Paragraph)paragraphs.get(i)).getHtml());
				}
			}
		}else{
			sb.append(" <table width="+width+" border="+border+" align= "+align+">");
			if (top!=0){
				sb.append("<tr height="+top+" style='font-size:1pt'><td colspan=3>&nbsp;</td></tr>");
			}
			sb.append("<tr>");
			if(left!=0){
				sb.append("<td width="+left+" style='font-size:1pt'>&nbsp;</td>");
			}
			sb.append("<td wdith="+(width-left-right)+">");
			if (paragraphs.size()>0) {
				for (int i=0 ;i<paragraphs.size();i++){
					sb.append(((Paragraph)paragraphs.get(i)).getHtml());
				}
			}
			sb.append("</td>");
			if(right!=0){
				sb.append("<td width="+right+" style='font-size:1pt'>&nbsp;</td>");
			}
			sb.append("</tr>");
			if (bottom!=0){
				sb.append("<tr height='"+bottom+"' style='font-size:1pt'><td colspan=3>&nbsp;</td></tr>");
			}
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	public void addParagraph(Paragraph ph){
		paragraphs.add(ph);
	}
}
