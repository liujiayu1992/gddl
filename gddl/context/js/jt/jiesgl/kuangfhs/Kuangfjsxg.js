var cal;
var calenDate;
function GetDataString(_date){
	var n ;
	var syear;
	var smonth;
	var sday;
	var str;
	str=_date;
	n=str.indexOf("-");
	if(n>0){
		syear=str.substring(0,n);
		str=str.substring(n+1);
		n=str.indexOf("-")
		smonth=str.substring(0,n);
		sday=str.substring(n+1);
		return(smonth +"/" + sday+"/" +syear) ;
	}
	return _date;
}
function changedate(obj) {
	calenDate=obj;
	if (obj.value!=""){
		cal.setSelectedDate(GetDataString(obj.value));
		cal.toggle(obj);
	}
}
body.onclick =function(){
	if(calenDate!=null){
		if (window.event.srcElement!=calenDate){
			var bf;
			bf =true;
			var oTmp = window.event.srcElement;  
			do 
			   {
			   		//alert("oTmp.tagName:" + oTmp.id);
			   		if(oTmp.id=="DateSelectDiv"){
			   			bf =false;
			   			break;
			   		}		   		
			      oTmp = oTmp.offsetParent;
			   } 
			while (oTmp.tagName != "BODY");
		    if (bf){
		    	calenDate=null;
				cal.hide();
			}
		}else{
			cal.show(calenDate);
		}
	}
}

window.onresize=function(){

var bodyHeight;
var bodyWidth ;
var bodyTop;
var tablemainHeight;
var tablemainWidth;
var SelectDataHeight;
var SelectDataWidth;
var EditDataHeight;
var EditDataWidth;

var ConditionHeight;
var ConditionWidth;

	cal = new Calendar();
	cal.setMonthNames(new Array("\u4E00\u6708", "\u4E8C\u6708", "\u4E09\u6708", "\u56DB\u6708", "\u4E94\u6708", "\u516D\u6708", "\u4E03\u6708", "\u516B\u6708", "\u4E5D\u6708", "\u5341\u6708", "\u5341\u4E00\u6708", "\u5341\u4E8C\u6708"));
	cal.setShortMonthNames(new Array("\u4E00\u6708", "\u4E8C\u6708", "\u4E09\u6708", "\u56DB\u6708", "\u4E94\u6708", "\u516D\u6708", "\u4E03\u6708", "\u516B\u6708", "\u4E5D\u6708", "\u5341\u6708", "\u5341\u4E00\u6708", "\u5341\u4E8C\u6708"));
	cal.setWeekDayNames(new Array("\u65E5", "\u4E00", "\u4E8C", "\u4E09", "\u56DB", "\u4E94", "\u516D"));
	cal.setShortWeekDayNames(new Array("\u65E5", "\u4E00", "\u4E8C", "\u4E09", "\u56DB", "\u4E94", "\u516D"));
	cal.setFormat("yyyy-MM-dd");
	cal.setFirstDayOfWeek(0);
	cal.setMinimalDaysInFirstWeek(1);
	cal.setIncludeWeek(false);
	cal.create();
	cal.onchange = function() {
		if(calenDate.value!=cal.formatDate()){
			calenDate.value  = cal.formatDate();
//			calenDate.onchange();
		}
	}


body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop;
tablemainWidth =bodyWidth -15;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight  - 20;
SelectDataWidth=tablemainWidth ;
//QueryFrameClsssend
/*
SelectDataHeight=Math.round((tablemainHeight-ConditionHeight)/7 * 4);
SelectDataWidth=tablemainWidth ;
EditDataWidth=tablemainWidth ;
EditDataHeight=tablemainHeight-SelectDataHeight-ConditionHeight -15;
EditFrmDiv.style.position="relative";
*/
//*/
SelectFrmDiv.style.position="relative";

//SelectHeadDiv.style.position="relative";
trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

//????????
SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight+15;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;

/*
SelectHeadDiv.style.posTop=0;
SelectHeadDiv.style.width=SelectDataWidth-10;
SelectHeadDiv.style.height=24;
SelectHeadDiv.style.posLeft=0;

SelectDataDiv.style.posTop=0;
SelectDataDiv.style.width=SelectDataWidth-7;
SelectDataDiv.style.height=SelectDataHeight-60;
SelectDataDiv.style.posLeft=-2;

if (SelectDataDiv.scrollHeight > SelectDataDiv.clientHeight){
	SelectHeadDiv.style.width=SelectDataWidth-25;
}


EditData.style.height=EditDataHeight;
EditData.style.width=EditDataWidth;

EditFrmDiv.style.posTop=0;
EditFrmDiv.style.height=EditDataHeight;
EditFrmDiv.style.width=EditDataWidth;

var intHeadWidth;
intHeadWidth =0;

for(i=0;i< editSelectData.cells.length;i+=2){
	if (intHeadWidth <  (editSelectData.cells(i).innerText).length ){
		intHeadWidth = (editSelectData.cells(i).innerText).length ;
	}
}
var editColWidth;
intHeadWidth = intHeadWidth *13;
var cols;
cols =Math.round(editSelectData.rows(0).cells.length/2);
editColWidth =Math.round((editSelectData.parentElement.offsetWidth-(cols*intHeadWidth) -20)/cols);
cols =1;
	for(i=0;i< editSelectData.cells.length;i++){
		if (cols ==1){		
			editSelectData.cells(i).style.width= intHeadWidth ;
			cols =cols  +1;
		}else{
			if (editColWidth > 40){
				editSelectData.cells(i).style.width= editColWidth ;
			}
			cols  =1;
		}
	}
*/
//??????????????????????????
      /*
//SelectHeadDiv.scrollLeft=SelectDataDiv.scrollLeft;

	var headtableCols ;
	//alert(tittleHead.length);
	headtableCols =EditHeadTabel.cells.length ;
	if (oData.rows.length > 0){
		oData.style.borderRight="gray 1px solid";
		EditHeadTabel.style.borderRight="gray 1px solid";
		for (i =0 ;i< headtableCols;i++){
		
//??????????????????????????????		
//			document.all.item("tittleHead" + i).style.posTop=3;
//			document.all.item("tittleHead" + i).style.posWidth=oData.cells(i).offsetWidth -4;
//??????????????????????????
			tittleHead(i).style.posTop=3;
			tittleHead(i).style.posWidth=oData.cells(i).offsetWidth -4;
////
			EditHeadTabel.cells(i).style.width =oData.cells(i).offsetWidth +1 ;
		}
		//????????Button??????????
		ShowButtons(1);

	}else{
		//????????Button??????????
		ShowButtons(0);
	}
*/
}

var oldClickrow=-1;
var oldinput;
var objselectCol;
var oldSelectobj;
var Editrows=-1;

/*
function droponchange(obj1,obj2,_int){
	document.all.item(obj1,Editrows).value =obj2.options(obj2.selectedIndex).text;
	if (_int==1){
		updatarows(obj1.name);
	}
}
function dropdown(_obj1,_obj2){
	objselectCol =document.all.item(_obj1,Editrows);
	oldSelectobj=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (document.all.item(_obj1,Editrows).value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			break;
		}
	}	
}
function editin(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (_str2=="onblur"){
		_str1.style.border="gray 1px solid";
		_str1.className="editinputout";
		return;
	}

	if (oldinput!=null){
		oldinput.style.border="gray 1px solid";
		oldinput.className="editinputout";
	}
	if(!_str1.readOnly){
		_str1.style.border="#B1BAD0 1px solid";
		_str1.className="editinputon";
	}
	oldinput =_str1;	
}
*/
//QueryFrameClsssBegin
function droponchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.options(oldSelectobj.selectedIndex).text;
		if (_int==1){
			//updatarows(objselectCol.name);
			Form0.submit();
		}
	}
	oldSelectobj.style.display="none";
}
function dropdown(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft+2;
	_obj2.style.posTop=_obj1.parentElement.offsetTop+50;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth+1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	
}
function dropdown2(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft+2;
	_obj2.style.posTop=_obj1.parentElement.offsetTop+4;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth-260;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editin(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinput!=null){
		oldinput.style.border="#ffffff 0px solid";
		oldinput.style.posWidth=oldinput.parentElement.offsetWidth -2  ;
		oldinput.style.backgroundColor="";
		oldinput.style.position="";
		oldinput.style.left="";
		oldinput.style.height="";
		oldinput.style.top="";
		oldinput.style.padding =2;
	}
	if(!_str1.readOnly){
		_str1.style.backgroundColor="#ffffff";
		_str1.style.border="#FC0303 1px solid";
		_str1.style.posLeft =_str1.parentElement.offsetLeft+2;
		if (_str1.parentElement.offsetTop == 0 ){
			_str1.style.posTop=_str1.parentElement.offsetTop ;
			_str1.style.posHeight=_str1.parentElement.offsetHeight-2 ; 
		}else{
			_str1.style.posTop=_str1.parentElement.offsetTop  -1;
			_str1.style.posHeight=_str1.parentElement.offsetHeight -1 ; 
		}
		_str1.style.posWidth=_str1.parentElement.offsetWidth +1  ;
		_str1.style.padding =3;
		_str1.style.position="absolute";
		if (_str2=="onclick"){
			if (oldSelectobj!=null){oldSelectobj.style.display="none";}
		}
	}
	oldinput =_str1;	
}
//QueryFrameClsssend
function classchange(_str1,_str2){

var rows=oData.rows ;

	if(_str2=="onclick"){
		if (oldClickrow !=-1){
			oData.rows(oldClickrow).className="edittableTrOut";
		}
		oldClickrow=_str1.rowIndex;
		oData.rows(oldClickrow).className="edittableTrClick";
	}

	if(_str2=="onmouseout"){
		if (oldClickrow!=_str1.rowIndex){
			_str1.className="edittableTrOut";
		}else{
			_str1.className="edittableTrClick";
		}
	}
	
	if(_str2=="onmouseover"){
		
		if (oldClickrow!=_str1.rowIndex){
			_str1.className="edittableTrOn";
		}else{
			_str1.className="edittableTrClick";
		}
		
	}
	document.all.item("EditTableRow").value=oldClickrow;
}

function updatarows(str){
	var rowscount,n;
	var row;
	n=str.indexOf("$");
	if(n!=-1){
		strn =str.substring(0,n);
		row =eval(str.substring(n+1))+1;
	}else{
		strn =str;
		row =0;
	}
	rowscount=document.all.item(strn).length;
	for (i=row+1;i<rowscount;i++){
		document.all.item(strn,i).value=document.all.item(strn,row).value;
	}
}

function _onscroll(_obj1,_obj2){
	//if (oldSelectobj!=null){oldSelectobj.style.display="none";}
	if ((_obj1.scrollWidth-_obj1.clientWidth) > 0) {
		if ((_obj1.scrollWidth -_obj1.clientWidth) ==_obj1.scrollLeft) {
			_obj2.style.posLeft=0;
			if (_obj1.scrollHeight > _obj1.clientHeight){
				_obj2.scrollLeft=_obj1.scrollLeft + (_obj1.scrollWidth -_obj2.scrollWidth) ;
			}else{
				_obj2.scrollLeft=_obj1.scrollLeft;
			}
		}else{
			_obj2.style.posLeft=0;
			_obj2.scrollLeft=_obj1.scrollLeft;
		}
	}
}

//InstrSelectEdit

function ChangeSelectRow(obj1){
	if (Editrows!=-1){
		document.all.item(((obj1.id).substring(0,(obj1.id).indexOf("edit"))),Editrows).value=obj1.value;
		//colCount();
	}
}


function FormatInput(obj,decimalLength){
	var i=0;
	var objLength;
	if (document.all.item(obj)!=null){
		objLength=document.all.item(obj).length;
		if (objLength>1){
			for (i =0;i< objLength;i++){
				document.all.item(obj,i).value=Format(document.all.item(obj,i).value,decimalLength);
			}
		}else if (objLength=1){
			document.all.item(obj).value=Format(document.all.item(obj).value,decimalLength);
		}
	}
}	
function Round(a_Num , a_Bit)
{
  return(Format(Math.round(a_Num * Math.pow (10 , a_Bit)) / Math.pow(10 , a_Bit),a_Bit))  ;
} 

function Format(value, decimalLength){
	value = Math.round(value * Math.pow(10, decimalLength));


	value = value / Math.pow(10, decimalLength);

	var v = value.toString().split(".");
	if (v[0]==""){
		v[0]="0";
	}

	if (v.length > 1) {
		var len = v[1].length;
		if (len > decimalLength) {
			v[1] = v[1].substring(0, decimalLength);
		}
		else {
			 for (var i=v[1].length; i<decimalLength; i++) {
				v[1] += "0";
			}
		}
	}
	else {
		v[1] = "";
		for (var i=v[1].length; i<decimalLength; i++) {
			v[1] += "0";
		}
	}

	if (decimalLength > 0) {
		return v[0] + "." + v[1];
	}
	else {
		return v[0];
	}	
}



function editclick(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft+2;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
	
}
function editchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.value;
		if (_int==1){
			updatarows(objselectCol.name);
		}
	}
   //colsCount();
}
function Count(){
	
	document.all.item("JIASJE").value =Math.round((eval(document.all.item("JIAKHJ").value)+eval(document.all.item("JIAKSK").value))*100)/100;
	document.all.item("JIAKJE").value= Math.round((eval(document.all.item("JIAKHJ").value)-eval(document.all.item("BUKYQJK").value))*100)/100;
	document.all.item("HEJ").value=Math.round((eval(document.all.item("YUNZFHJ").value)+eval(document.all.item("JIASJE").value))*100)/100;
	document.all.item("DAXHJ").value=tormb(eval(document.all.item("HEJ").value));
}

function colsCount(){

	if(eval(document.all.item("GONGFRL").value)>0&&eval(document.all.item("YANSRL").value)>0){
	   	document.all.item("YINGKRL").value=Math.round(eval(document.all.item("YANSRL").value)-eval(document.all.item("GONGFRL").value));
	   	document.all.item("RELZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("RELZJBZ").value)*100)/100;
	}
	if((document.all.item("GONGFRL").value>0||document.all.item("GONGFRL").value.length>4)&&eval(document.all.item("YANSRL").value)>0){
	    	
	    	if(document.all.item("GONGFRL").value.length==4){
	    	
	    		document.all.item("YINGKRL").value=Math.round(eval(document.all.item("YANSRL").value)-eval(document.all.item("GONGFRL").value));
	    	
		    	if(eval(document.all.item("YANSRL").value)<eval(document.all.item("GONGFRL").value)){
		    		document.all.item("RELZJJE").value=-Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("RELZJBZ").value)*100)/100;
		    	}else{
					document.all.item("RELZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("RELZJBZ").value)*100)/100;
		    	}
		    	if(document.all.item("YINGKRL").value==0){
		    		document.all.item("RELZJJE").value=0;
		    	}
	    	
	    	}else{
	    		
	    		var gongfrlxx=eval(document.all.item("GONGFRL").value.substring(0,4));
	    		var gongfrlsx=eval(document.all.item("GONGFRL").value.substring(5));
	    		
	    		if(eval(document.all.item("YANSRL").value)-gongfrlsx>=0){
	    		
	    			document.all.item("YINGKRL").value=Math.round(eval(document.all.item("YANSRL").value)-gongfrlsx);
	    		}else{
	    		
	    			document.all.item("YINGKRL").value=Math.round(eval(document.all.item("YANSRL").value)-gongfrlxx);
	    		}
		    			    			
		    	if(document.all.item("YINGKRL").value>0){

		    		document.all.item("RELZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("RELZJBZ").value)*100)/100;
		    
				}else{
					
					document.all.item("RELZJJE").value=-Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("RELZJBZ").value)*100)/100;
				}
						    
		    	if(document.all.item("YINGKRL").value==0){
		    		document.all.item("RELZJJE").value=0;
		    	}

	    	}
	    		
	    }
	    
	    if(eval(document.all.item("GONGFHFF").value)>0&&eval(document.all.item("YANSHFF").value)>0){
	    	document.all.item("YINGKHFF").value=Math.round((eval(document.all.item("YANSHFF").value)-eval(document.all.item("GONGFHFF").value))*100)/100;
//	    	document.all.item("HUIFFZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("HUIFFZJBZ").value)*100)/100;
			if(eval(document.all.item("YANSHFF").value)<eval(document.all.item("GONGFHFF").value)){
	    		document.all.item("HUIFFZJJE").value=-Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("HUIFFZJBZ").value)*100)/100;
	    	}else{
				document.all.item("HUIFFZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("HUIFFZJBZ").value)*100)/100;
	    	}
	    }
	    
	    if(eval(document.all.item("GONGFHF").value)>0&&eval(document.all.item("YANSHF").value)>0){
	    	document.all.item("YINGKHF").value=Math.round((eval(document.all.item("YANSHF").value)-eval(document.all.item("GONGFHF").value))*100)/100;
	    	document.all.item("HUIFZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("HUIFZJBZ").value)*100)/100;
	    }
	    
	    if(eval(document.all.item("GONGFSF").value)>0&&eval(document.all.item("YANSSF").value)>0){
	    	document.all.item("YINGKSF").value=Math.round((eval(document.all.item("YANSSF").value)-eval(document.all.item("GONGFSF").value))*100)/100;
	    	document.all.item("SHUIFZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("SHUIFZJBZ").value)*100)/100;
	    }
	    
	    if(eval(document.all.item("GONGFLF").value)>0&&eval(document.all.item("YANSL").value)>0){
	    	document.all.item("YINGKLF").value=Math.round((eval(document.all.item("YANSL").value)-eval(document.all.item("GONGFLF").value))*100)/100;
	    	if(document.all.item("YINGKLF").value>0){
	    	
	    		document.all.item("LIUZJJE").value=-Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("LIUZJBZ").value)*100)/100;
	    	}else{
	    		
	    		document.all.item("LIUZJJE").value=Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("LIUZJBZ").value)*100)/100;
	    	}
	    	
	    }
	    if (eval(document.all.item("JIESSL").value)>0){
		    document.all.item("JIASJE").value=Math.round((Math.round(eval(document.all.item("HANSDJ").value)*eval(document.all.item("JIESSL").value)*100)/100+Math.round(eval(document.all.item("BUKYQJK").value)*eval(document.all.item("JIAKSL").value)*100)/100)*100)/100;
			document.all.item("JIAKHJ").value=Math.round(eval(document.all.item("JIASJE").value)/eval(document.all.item("JIAKSL").value)*100)/100;
			document.all.item("JIAKJE").value =Math.round((eval(document.all.item("JIAKHJ").value)-eval(document.all.item("BUKYQJK").value))*100)/100;
			document.all.item("JIAKSK").value =Math.round((eval(document.all.item("JIASJE").value)-eval(document.all.item("JIAKHJ").value))*100)/100;
			document.all.item("BUHSDJ").value=Math.round(eval(document.all.item("JIAKJE").value)/eval(document.all.item("JIESSL").value)*10000000)/10000000;
			document.all.item("SHULZJBZ").value=eval(document.all.item("HANSDJ").value);
	    }
	    
		if(eval(document.all.item("TIELYF").value)>0){
	    	document.all.item("YUNZFHJ").value =Math.round((eval(document.all.item("TIELYF").value)+eval(document.all.item("ZAF").value)+eval(document.all.item("BUKYQYZF").value))*100)/100;
		    document.all.item("YUNFSK").value =Math.round((eval(document.all.item("YUNZFHJ").value)-eval(document.all.item("JISKC").value))*eval(document.all.item("YUNFSL").value)*100)/100;
		    document.all.item("BUHSYF").value=Math.round((eval(document.all.item("YUNZFHJ").value)-eval(document.all.item("JISKC").value)-eval(document.all.item("YUNFSK").value))*100)/100;
		}
		
		document.all.item("HEJ").value=Math.round((eval(document.all.item("YUNZFHJ").value)+eval(document.all.item("JIASJE").value))*100)/100;
		document.all.item("DAXHJ").value=tormb(eval(document.all.item("HEJ").value));
}


function onLoadSet(){
	
	document.all.item('oldjiakhj').value=document.all.item('JIAKHJ').value;
	document.all.item('oldjiaksk').value=document.all.item('JIAKSK').value;
	
	if(document.all.item('shenhjb').value==1 && document.all.item('shenhzt').value==1){
		document.all.item('TijldButton').disabled=false;
		document.all.item('DeleteButton').disabled=false;
	
	}else if(document.all.item('shenhjb').value==2 && document.all.item('shenhzt').value==0){
		document.all.item('TijldButton').disabled=true;
		document.all.item('DeleteButton').disabled=false;
	
	}else{
		document.all.item('TijldButton').disabled=true;
		document.all.item('DeleteButton').disabled=true;
	}
 
 }
body.onload =function(){
     FormatInput("DIANCXXB_ID",0);
     FormatInput("MEIKXXB_ID",0);
     FormatInput("FAHDWB_ID",0);
     FormatInput("CHEZXXB_ID",0);
     FormatInput("MEIJB_ID",0);
     FormatInput("CHES",0);
     //FormatInput("GONGFRL",0);
     FormatInput("GONGFHF",2);
     FormatInput("HANSDJ",4);
     FormatInput("YANSHF",2);
     FormatInput("YINGKHF",2);
     FormatInput("HUIFZJBZ",2);
     FormatInput("HUIFZJJE",2);
     FormatInput("GONGFHFF",2);
     FormatInput("YANSHFF",2);
     FormatInput("YINGKHFF",2);
     FormatInput("HUIFFZJBZ",2);
     FormatInput("HUIFFZJJE",2);
     FormatInput("GONGFSF",2);
     FormatInput("YANSSF",2);
     FormatInput("YINGKSF",2);
     FormatInput("SHUIFZJBZ",2);
     FormatInput("SHUIFZJJE",2);
     
     FormatInput("YINGKLF",2);
     FormatInput("YANSRL",0);
     FormatInput("GONGFL",2);
     FormatInput("YANSL",2);
     FormatInput("YINGKRL",0);
     FormatInput("RELZJBZ",4);
     FormatInput("RELZJJE",2);
     FormatInput("SHULZJBZ",4);
     FormatInput("SHULZJJE",2);
     FormatInput("LIUZJBZ",3);
     FormatInput("GONGFLF",2);
     FormatInput("LIUZJJE",2);
     FormatInput("HUIRDZJBZ",2);
     FormatInput("HUIRDZJJE",2);
     FormatInput("GONGFHRD",0);
     FormatInput("YANSHRD",0);
     FormatInput("GONGFSL",2);
     FormatInput("YANSSL",2);
     FormatInput("YINGK",0);
     FormatInput("JIESSL",2);
     FormatInput("JINGZ",2);
     FormatInput("BUHSDJ",7);
     FormatInput("JIAKJE",2);
     FormatInput("BUKYQJK",2);
     FormatInput("JIAKHJ",2);
     FormatInput("JIAKSL",2);
     FormatInput("JIAKSK",2);
     FormatInput("JIASJE",2);
     FormatInput("TIELYF",2);
     FormatInput("ZAF",2);
     FormatInput("BUKYQYZF",2);
     FormatInput("JISKC",2);
     FormatInput("BUHSYF",2);
     FormatInput("YUNFSL",2);
     FormatInput("YUNFSK",2);
     FormatInput("YUNZFHJ",2);
     FormatInput("HEJ",2);
     FormatInput("HETJG",2);
     FormatInput("JIESLX",0);
     FormatInput("KUIDJF",2);
     FormatInput("RELSX",0);
     FormatInput("RELXX",0);
     FormatInput("LIUSX",2);
     FormatInput("LIUXX",2);
     
     FormatInput("DANJC",2);
     FormatInput("HETSL",2);
     FormatInput("HETLF",2);
     FormatInput("HETHFF",2);
     FormatInput("HETHF",2);
     FormatInput("HETSF",2);
     FormatInput("JIESSLJS",2);
     FormatInput("JIESRL",0);
     FormatInput("JIESLF",2);
     FormatInput("JIESHFF",2);
     FormatInput("JIESHF",2);
     FormatInput("JIESSF",2);
    /* 
     var _obj=document.all.item("JIESLXDropDown");
	if(_obj.options(_obj.selectedIndex).text=="????????"){
		document.all.item("MKJSDBH").style.display="";
	}
     */
     document.all.item("shezhi").style.display="none";
     document.all.item("JIESSLJS").value=document.all.item("JIESSL").value;
     document.all.item("GONGFL").value=document.all.item("GONGFSL").value;
     document.all.item("huiffzb").style.display="none";
	 document.all.item("huifzb").style.display="none";     
     document.all.item("shuifzb").style.display="none";     
     document.all.item("Imgjts").style.display="none";
     
}
function tormb(currencyDigits) {
	var MAXIMUM_NUMBER = 99999999999.99;
	var CN_ZERO = "零";
	var CN_ONE = "壹";
	var CN_TWO = "贰";
	var CN_THREE = "叁";
	var CN_FOUR = "肆";
	var CN_FIVE = "伍";
	var CN_SIX = "陆";
	var CN_SEVEN = "柒";
	var CN_EIGHT = "捌";
	var CN_NINE = "玖";
	var CN_TEN = "拾";
	var CN_HUNDRED = "佰";
	var CN_THOUSAND = "仟";
	var CN_TEN_THOUSAND = "万";
	var CN_HUNDRED_MILLION = "亿";
	var CN_SYMBOL = "￥";
	var CN_DOLLAR = "元";
	var CN_TEN_CENT = "角";
	var CN_CENT = "分";
	var CN_INTEGER = "";
	var integral; // Represent integral part of digit number.
	var decimal; // Represent decimal part of digit number.
	var outputCharacters; // The output result.
	var parts;
	var digits, radices, bigRadices, decimals;
	var zeroCount;
	var i, p, d;
	var quotient, modulus;
	currencyDigits = currencyDigits.toString();
	if (currencyDigits == "") {
		return "";
	}
	if (currencyDigits.match(/[^,.\d]/) != null) {
		return "";
	}
	if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
		return "";
	}
	currencyDigits = currencyDigits.replace(/,/g, ""); // Remove comma delimiters.
	currencyDigits = currencyDigits.replace(/^0+/, ""); // Trim zeros at the beginning.
	if (Number(currencyDigits) > MAXIMUM_NUMBER) {
		return "";
	}
	parts = currencyDigits.split(".");
	if (parts.length > 1) {
		integral = parts[0];
		decimal = parts[1];
		decimal = decimal.substr(0, 2);
	}else {
		integral = parts[0];
		decimal = "";
	}
	digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
	radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
	bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
	decimals = new Array(CN_TEN_CENT, CN_CENT);
	outputCharacters = "";
	if (Number(integral) > 0) {
		zeroCount = 0;
		for (i = 0; i < integral.length; i++) {
			p = integral.length - i - 1;
			d = integral.substr(i, 1);
			quotient = p / 4;
			modulus = p % 4;
			if (d == "0") {
			    zeroCount++;
			}else {
				if (zeroCount > 0){
					outputCharacters += digits[0];
				}
				zeroCount = 0;
				outputCharacters += digits[Number(d)] + radices[modulus];
			}
		   if (modulus == 0 && zeroCount < 4) {
				outputCharacters += bigRadices[quotient];
			}
		}
  		outputCharacters += CN_DOLLAR;
 	}
	if (decimal != "") {
		for (i = 0; i < decimal.length; i++) {
			d = decimal.substr(i, 1);
			if (d != "0") {
				outputCharacters += digits[Number(d)] + decimals[i];
			}
		}
	}
	if (outputCharacters == "") {
		outputCharacters = CN_ZERO + CN_DOLLAR;
	}
	if (decimal == "") {
		//outputCharacters += CN_INTEGER;
	}
	if(outputCharacters=="零元")
	{
    	outputCharacters="";
	}else{
		outputCharacters = CN_SYMBOL + outputCharacters + CN_INTEGER ;
	}
	return outputCharacters;
}
function check(){
	if(document.all.item("SHOUKDW").value==""||document.all.item("SHOUKDW").value=="请选择"){
		alert("请选择收款单位!");
		return false;
	}else if(document.all.item("KAIHYH").value==""||document.all.item("KAIHYH").value=="请选择"){
		alert("请选择开户银行!");
		return false;
	}else if(document.all.item("ZHANGH").value==""){
		alert("请填写银行帐号!");
		return false;
	}else{
		var str=document.all.item("FAPBH").value;
		var s=str.replace(/[^\x00-\xff]/g,"aa");
		if(s.length>20){
			alert("发票编号过长，请核对！");
			return false;
		}
		/*
		if(checkMKJSDBH()){
		
		}else{
			return false;
		}
		if(checkSuosgs()){
			
		}else{
			return false;
		}
		*/
		if(document.all.item("JINGZ").value<=0||document.all.item("JIESSL").value<=0||document.all.item("HANSDJ").value<=0||document.all.item("GONGFSL").value<=0){
			alert("结算数量,供方数量，过衡重量，含税单价信息小于或等于零，请核对！");
			return false;
		}			
		return true;
	}
}


function Qux(){
		
		document.all.item("RetrunsButton").click();
}
function lcsubmit(int_Value){
	
	if(document.all.item("JIESBH").value!=""){
		document.all.item("shezhi").style.display="";
		if(int_Value==1){
	//		修改合同
	
			document.all.item("HetbhTr").style.display="";
			document.all.item("HetbhTr2").style.display="";
			
			document.all.item("LiucmcTr").style.display="none";
			document.all.item("LiucmcTr2").style.display="none";
			
			document.all.item("Zhuangt").value=1;
			
		}else if(int_Value==2){
	//		提交流程
	
			document.all.item("HetbhTr").style.display="none";
			document.all.item("HetbhTr2").style.display="none";
			
			document.all.item("LiucmcTr").style.display="";
			document.all.item("LiucmcTr2").style.display="";
			document.all.item("Zhuangt").value=2;
		}
	}else{
		
		alert("请选择结算单！");
	}	
}
function checkSubmit(){
	
	if(document.all.item("Zhuangt").value==2){
		
		var _obj=document.all.item("LiucmcDropDown");
		if(_obj.options(_obj.selectedIndex).text!="请选择"){
			
			document.all.item("shezhi").style.display="none";
			
			if(check()){
				
				return true;
			}
			
		}else{
			alert("请选择流程名称！");
			return false;
		}
	}else if(document.all.item("Zhuangt").value==1){
		
		var _obj=document.all.item("HetbhDropDown");
		if(_obj.options(_obj.selectedIndex).text!="请选择"){
			
			document.all.item("shezhi").style.display="none";
			
			if(check()){
				
				return true;
			}
			
		}else{
			alert("请选择合同编号！");
			return false;
		}
	}
}

function HideTiaoj(){
	
	document.all.item("shezhi").style.display="none";
}

function show(){
	
	if(document.all.item("shuifzb").style.display==""){
		
		if(document.all.item("huifzb").style.display==""){
			
			document.all.item("huiffzb").style.display="";
			document.all.item("Imgjts").style.display="";
			document.all.item("Imgjtx").style.display="none";
		}else{
			
			document.all.item("huifzb").style.display=""
		}
	}else{
				
		document.all.item("shuifzb").style.display=""
	}	
}
function hide(){
	
	if(document.all.item("shuifzb").style.display=="none"){
		
		if(document.all.item("huifzb").style.display=="none"){
			
			document.all.item("huiffzb").style.display="none";
			document.all.item("Imgjts").style.display="none";
			document.all.item("Imgjtx").style.display="";
		}else{
			
			document.all.item("huifzb").style.display="none"
		}
	}else{
				
		document.all.item("shuifzb").style.display="none"
	}	
}

function ShowslszDIV(_value){
	if(_value=='show'){
		
		if(document.all.item("JIESBH").value){
		
			document.all.item("ShuilszDiv").style.display="";
	    	document.all.item('shuilDIV').value=document.all.item('SHUIL').value;
	    	document.all.item('YfshuilDIV').value=document.all.item('YUNFSL').value;
	    	document.all.item('shuilDIV').select();
			SetDisabled('SelectData',true);	
		}else{
			
			alert("请选择结算单！");
		}
		
	}else if(_value=='qued'){
		
		document.all.item("ShuilszDiv").style.display="none";
    	document.all.item('YUNFSL').value=document.all.item('YfshuilDIV').value;
    	document.all.item('JIAKSL').value=document.all.item('shuilDIV').value;
    	
		document.all.item("JIASJE").value=Math.round((Math.round(eval(document.all.item("HANSDJ").value)*eval(document.all.item("JIESSL").value)*100)/100+Math.round(eval(document.all.item("BUKYQJK").value)*(1+eval(document.all.item("JIAKSL").value))*100)/100)*100)/100;
		document.all.item("JIAKHJ").value=Math.round(eval(document.all.item("JIASJE").value)/(1+eval(document.all.item('JIAKSL').value))*100)/100;
		document.all.item("JIAKJE").value =Math.round((eval(document.all.item("JIAKHJ").value)-eval(document.all.item("BUKYQJK").value))*100)/100;
		document.all.item("JIAKSK").value =Math.round((eval(document.all.item("JIASJE").value)-eval(document.all.item("JIAKHJ").value))*100)/100;
		document.all.item("BUHSDJ").value=Math.round(eval(document.all.item("JIAKJE").value)/eval(document.all.item("JIESSL").value)*10000000)/10000000;
		document.all.item("SHULZJBZ").value=eval(document.all.item("HANSDJ").value);  
		
		document.all.item("YUNZFHJ").value =Math.round((eval(document.all.item("TIELYF").value)+eval(document.all.item("ZAF").value)+eval(document.all.item("BUKYQYZF").value))*100)/100;
		document.all.item("YUNFSK").value =Math.round((eval(document.all.item("YUNZFHJ").value)-eval(document.all.item("JISKC").value))*eval(document.all.item("YUNFSL").value)*100)/100;
		document.all.item("BUHSYF").value=Math.round((eval(document.all.item("YUNZFHJ").value)-eval(document.all.item("JISKC").value)-eval(document.all.item("YUNFSK").value))*100)/100;
		    
		document.all.item("HEJ").value=Math.round((eval(document.all.item("YUNZFHJ").value)+eval(document.all.item("JIASJE").value))*100)/100;
		document.all.item("DAXHJ").value=tormb(eval(document.all.item("HEJ").value));
		    
		SetDisabled('SelectData',false);
	}else{
		
		document.all.item("ShuilszDiv").style.display="none";
		SetDisabled('SelectData',false);
	}
}
function Pingb(){
	if(event.keyCode==13){
		event.keyCode=0;
     	event.returnValue=false;	
     }else if(event.keyCode==8){
			if(event.srcElement.tagName!="INPUT"){
				event.keyCode=0;
		     	event.returnValue=false;
			}else if(event.srcElement.type!= "text"){
				event.keyCode=0;
		     	event.returnValue=false;				
			}else if(event.srcElement.readOnly){
				event.keyCode=0;
		     	event.returnValue=false;
			}		     
     }
	
}