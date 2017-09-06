package com.zhiren.report;
public class Cell {	
	public Cell(){}
	public Cell(String strValue){
		value=strValue;
	}
	
	public Cell(Cell a){
		value=a.value;
		used=a.used;
		rowSpan=a.rowSpan;
		colSpan=a.colSpan;
		align=a.align;
		border_left=a.border_left ;
		border_right=a.border_right ;
		border_top=a.border_top ;
		border_bottom=a.border_bottom ;
	}
	
	public void setBorderNone(){
		border_left=0;
		border_right=0;
		border_top=0;
		border_bottom=0;		
	}

	public void setBorder(int left,int right,int top ,int bottom){
		border_left=left;
		border_right=right;
		border_top=top;
		border_bottom=bottom;
	}
	
	public void setBorderDefault(){
		border_left=0;
		border_right=1;
		border_top=0;
		border_bottom=1;
	}
	
	public String backColor="";
	public String foreColor="";
	
	public String bg_Color="#F2F2F2";
	
	public String getBg_Color(){
		return bg_Color;
	}
	
	public void setBg_Color(String value){
		bg_Color=value;
	}
	
	public String value="";
	public boolean used=true;
	public int rowSpan=1;
	public int colSpan=1;
	
	public int padding=-1;
	public int align=Table.ALIGN_LEFT;
	public int valign=-1;
	
	public String fontName="";
	public int fontSize=-1;
	public boolean fontBold=false;
	
	public int border=0;
	public int border_left=0;
	public int border_right=1;
	public int border_top=0;
	public int border_bottom=1;
	
	public String border_left_color="";
	public String border_right_color="";
	public String border_top_color="";
	public String border_bottom_color="";
	
	public String border_left_style="";
	public String border_right_style="";
	public String border_top_style="";
	public String border_bottom_style="";

	public String className = "";
	public String funEvent="";
}


