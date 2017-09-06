package com.zhiren.report;

public class Paragraph {
	private String fontName="ËÎÌו";
	private int fontSize=9;
	private boolean fontBold=false;
	private int indent=0;
	private String indentString="";
	private String align="left";
	private StringBuffer html=new StringBuffer();
	public static final String HTML_SPACE="&nbsp;&nbsp;";
	public static final String ALIGN_LEFT="left";
	public static final String ALIGN_CENTER="center";
	public static final String ALIGN_RIGTH="right";
	
	public Paragraph(String Text,String fontName,int fontSize ,boolean fontBold){
		this.fontName=fontName;
		this.fontSize=fontSize;
		this.fontBold=fontBold;
		addText(Text);
	}
	
	public Paragraph(String Text){
		addText(Text);
	}
	
	public Paragraph(){
		
	}
	
	public void setAlign(String align){
		this.align=align;
	}
	
	public void setFont(String fontName,int fontSize ,boolean fontBold){
		this.fontName=fontName;
		this.fontSize=fontSize;
		this.fontBold=fontBold;
	}
	
	public void setIndent(int indent){
		this.indent=indent;
		indentString="";
		if (indent>0 ){
			for (int i=1;i<=indent;i++){
				indentString=indentString+HTML_SPACE;
			}
		}
	}
	
	private String getIndent(int indent){
		if (indent>0 ){
			String strIndentString="";
			for (int i=1;i<=indent;i++){
				strIndentString=strIndentString+HTML_SPACE;
			}
			return strIndentString;
		}
		return "";
	}
	public void setFontSize(int fontSize){
		this.fontSize=fontSize;
	}
	
	public void setFontName(String fontName){
		this.fontName=fontName;
	}
	
	public void setFontBold(boolean fontBold){
		this.fontBold=fontBold;
	}
	
	private String getStyle(){
		return " style='font-name:"+fontName+";font-size:" +fontSize+"pt'";
	}
	
	private String getAlign(String align){
		if (align!=ALIGN_LEFT){
			return "";
		}
		return " align="+align;
	}
	
	public void newPage(){
		html.append("<span ><br clear='all' style='page-break-before:always'></span>");
	}
	
	public void newLine(){
		html.append("<p "+getStyle()+">&nbsp;</p>");
	}
	
	private String getBold(){
		if (fontBold){
			return "<b>";
		}else{
			return "";
		}
	}
	
	private String getBoldEnd(){
		if (fontBold){
			return "</b>";
		}else{
			return "";
		}
	}
	public void newLine(int rows){
		if (rows<1){
			return;
		}
		for (int i=1 ;i<rows;i++){
			html.append("<p "+getStyle()+">&nbsp;</p>");
		}
	}
		
	public void newLine(String text){
		if (align!=ALIGN_LEFT){
			html.append("<p align="+align+getStyle()+">").append(getBold()).append(indentString).append(text).append(getBoldEnd());
			html.append("</p>");
		}else{
			html.append("<p "+getStyle()+">").append(getBold()).append(indentString).append(text).append(getBoldEnd());
			html.append("</p>");
		}
	}

	public void newLine(int indent,String text){
		if (align!=ALIGN_LEFT){
			html.append("<p align="+align+getStyle()+">").append(getBold()).append(getIndent(indent)).append(text).append(getBoldEnd());
			html.append("</p>");
		}else{
			html.append("<p "+getStyle()+">").append(getBold()).append(getIndent(indent)).append(text).append(getBoldEnd());
			html.append("</p>");
		}
	}
	
	public void newLine(String text,String align){
		String style="align='"+align+"' style='font-name:"+fontName+";font-size:" +fontSize+"pt'";
		html.append("<p "+style+">").append(getBold()).append(indentString).append(text).append(getBoldEnd());
		html.append("</p>");
	}
	
	public void newLine(String text,String _fontName,int _fontSize ,boolean fontBold,String align){
		if (fontBold){
			html.append("<p "+getAlign(align)+"sytle='font-family:"+_fontName+";font-size:" +_fontSize+"pt'>").append(text);
			html.append("</p>");
		}else{
			html.append("<p "+getAlign(align)+" sytle='font-family:"+_fontName+";font-size:" +_fontSize+"pt'><b>").append(text).append("</b>");
			html.append("</p>");
		}
	}
	
	public void addText(String text){
		html.append(text);
	} 
	
	
	public void addText(String text,String fontName,int fontSize ,boolean fontBold){
		if (fontBold){
			html.append("<span sytle='font-family:"+fontName+";font-size:" +fontSize+"pt'>").append(text).append("</span>");
		}else{
			html.append("<span sytle='font-family:"+fontName+";font-size:" +fontSize+"pt'><b>").append(text).append("</b></span>");
		}
	}
	
	public void addTable(String title,Table tb){
		newLine(title);
		html.append(title).append(tb.getHtml());
	}
	
	public void addTable(Table tb){
		html.append(tb.getHtml());
	}
	
	public void addCenterTable(String title,Table tb){
		newLine(title);
		html.append("<center>").append(title).append(tb.getHtml()).append("</center>");
	}
	
	public StringBuffer getHtml(){
		return html;
	}
	
}
