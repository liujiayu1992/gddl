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
var SelectDataHeight;
var SelectDataWidth;
var ConditionHeight;

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
	calenDate.value  = cal.formatDate();
	calenDate.onchange();
}

body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;
ConditionHeight=20;
//tablemainHeight=bodyHeight-bodyTop -15-30;
//tablemainWidth =bodyWidth -15-6;
//ConditionHeight =20;
//ConditionWidth=tablemainWidth ;
////QueryFrameClsssBegin
SelectDataHeight=bodyHeight - ConditionHeight  - 20-10-80;
SelectDataWidth=bodyWidth-10;
////QueryFrameClsssend
///*
//SelectDataHeight=Math.round((tablemainHeight-ConditionHeight)/7 * 4);
//SelectDataWidth=tablemainWidth ;
//EditDataWidth=tablemainWidth ;
//EditDataHeight=tablemainHeight-SelectDataHeight-ConditionHeight -15;
//EditFrmDiv.style.position="relative";
//*/
////*/

SelectFrmDiv.style.position="relative";
SelectHeadDiv.style.position="relative";
SelectDataDiv.style.position="relative";
//trcondition.style.height=20;
//tablemain.style.left=0;
//tablemain.style.width=bodyWidth -15;
//tablemain.style.height=tablemainHeight;
//
////选择框架
SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;
SelectFrmDiv.style.posLeft=3;
SelectHeadDiv.style.posTop=0;
SelectHeadDiv.style.width=SelectDataWidth-10;
SelectHeadDiv.style.height=24;
SelectHeadDiv.style.posLeft=0;

SelectDataDiv.style.posTop=0;
SelectDataDiv.style.width=SelectDataWidth-7;
SelectDataDiv.style.height=SelectDataHeight-22;

//
//if (SelectDataDiv.scrollHeight > SelectDataDiv.clientHeight){
//	SelectHeadDiv.style.width=SelectDataWidth-25;
//}
//
///*
//EditData.style.height=EditDataHeight;
//EditData.style.width=EditDataWidth;
//
//EditFrmDiv.style.posTop=0;
//EditFrmDiv.style.height=EditDataHeight;
//EditFrmDiv.style.width=EditDataWidth;
//
//var intHeadWidth;
//intHeadWidth =0;
//
//for(i=0;i< editSelectData.cells.length;i+=2){
//	if (intHeadWidth <  (editSelectData.cells(i).innerText).length ){
//		intHeadWidth = (editSelectData.cells(i).innerText).length ;
//	}
//}
//var editColWidth;
//intHeadWidth = intHeadWidth *13;
//var cols;
//cols =Math.round(editSelectData.rows(0).cells.length/2);
//editColWidth =Math.round((editSelectData.parentElement.offsetWidth-(cols*intHeadWidth) -20)/cols);
//cols =1;
//	for(i=0;i< editSelectData.cells.length;i++){
//		if (cols ==1){		
//			editSelectData.cells(i).style.width= intHeadWidth ;
//			cols =cols  +1;
//		}else{
//			if (editColWidth > 40){
//				editSelectData.cells(i).style.width= editColWidth ;
//			}
//			cols  =1;
//		}
//	}
//*/
////标题框与数据框的滚动条同步
//SelectHeadDiv.scrollLeft=SelectDataDiv.scrollLeft;
oData.style.borderRight="gray 1px solid";
EditHeadTabel.style.borderRight="gray 1px solid";
//for(i=0;i<6;i++){
//	document.all.item("div",i).style.width=tablemainWidth+6;
//	document.all.item("div",i).style.height=tablemainHeight;
//}
////tijiao
SelectFrmDivT.style.position="relative";
SelectHeadDivT.style.position="relative";
SelectDataDivT.style.position="relative";
SelectFrmDivT.style.height=SelectDataHeight;
SelectFrmDivT.style.width=SelectDataWidth;
SelectFrmDivT.style.posTop=0;
SelectFrmDivT.style.posLeft=3;
SelectHeadDivT.style.posTop=0;
//SelectHeadDivT.style.width=SelectDataWidth-10;
SelectHeadDivT.style.height=24;

SelectDataDivT.style.posTop=0;
SelectDataDivT.style.width=SelectDataWidth;
SelectDataDivT.style.height=SelectDataHeight;
oDataT.style.borderRight="gray 1px solid";
EditHeadTabelT.style.borderRight="gray 1px solid";

SelectFrmDivL.style.position="relative";
SelectHeadDivL.style.position="relative";
SelectDataDivL.style.position="relative";
SelectFrmDivL.style.height=SelectDataHeight;
SelectFrmDivL.style.width=SelectDataWidth;
SelectFrmDivL.style.posTop=0;
SelectHeadDivL.style.posTop=0;
SelectHeadDivL.style.width=SelectDataWidth;
SelectHeadDivL.style.height=24;

SelectDataDivL.style.posTop=0;
SelectDataDivL.style.width=SelectDataWidth;
SelectDataDivL.style.height=SelectDataHeight;
oDataL.style.borderRight="gray 1px solid";
EditHeadTabelL.style.borderRight="gray 1px solid";
//
//tijiao
SelectFrmDivH.style.position="relative";
SelectHeadDivH.style.position="relative";
SelectDataDivH.style.position="relative";
SelectFrmDivH.style.height=SelectDataHeight;
SelectFrmDivH.style.width=SelectDataWidth;
SelectFrmDivH.style.posTop=0;
SelectFrmDivH.style.posLeft=3;
SelectHeadDivH.style.posTop=0;
SelectHeadDivH.style.width=SelectDataWidth
SelectHeadDivH.style.height=24;

SelectDataDivH.style.posTop=0;
SelectDataDivH.style.width=SelectDataWidth;
SelectDataDivH.style.height=SelectDataHeight;

oDataH.style.borderRight="gray 1px solid";
EditHeadTabelH.style.borderRight="gray 1px solid";
//流程类别
SelectFrmDivM.style.position="relative";
SelectHeadDivM.style.position="relative";
SelectDataDivM.style.position="relative";
SelectFrmDivM.style.height=SelectDataHeight;
SelectFrmDivM.style.width=SelectDataWidth;
SelectFrmDivT.style.posTop=0;
SelectFrmDivM.style.posLeft=3;
SelectHeadDivM.style.posTop=0;
SelectHeadDivM.style.width=SelectDataWidth-10;
SelectHeadDivM.style.height=24;

SelectDataDivM.style.posTop=0;
SelectDataDivM.style.width=SelectDataWidth-7;
SelectDataDivM.style.height=SelectDataHeight-22;
oDataM.style.borderRight="gray 1px solid";
EditHeadTabelM.style.borderRight="gray 1px solid";
//动作
SelectFrmDivD.style.position="relative";
SelectHeadDivD.style.position="relative";
SelectDataDivD.style.position="relative";
SelectFrmDivD.style.height=SelectDataHeight;
SelectFrmDivD.style.width=SelectDataWidth;
SelectFrmDivT.style.posTop=0;
SelectFrmDivD.style.posLeft=3;
SelectHeadDivD.style.posTop=0;
SelectHeadDivD.style.width=SelectDataWidth-10;
SelectHeadDivD.style.height=24;

SelectDataDivD.style.posTop=0;
SelectDataDivD.style.width=SelectDataWidth-7;
SelectDataDivD.style.height=SelectDataHeight-22;
oDataD.style.borderRight="gray 1px solid";
EditHeadTabelD.style.borderRight="gray 1px solid";
SelectFrmDiv.style.border=0;
SelectFrmDivM.style.border=0;
SelectFrmDivH.style.border=0;
SelectFrmDivT.style.border=0;
SelectFrmDivD.style.border=0;
SelectFrmDivL.style.border=0;
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
			updatarows(objselectCol.name);
		}
	}
}
function dropdown(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
		 oldSelectobj.selectedIndex=0;
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
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
		_str1.style.posLeft =_str1.parentElement.offsetLeft;
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
	if (oldSelectobj!=null){oldSelectobj.style.display="none";}
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
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.value;
		if (_int==1){
			updatarows(objselectCol.name);
		}
	}
   // colCount();
}

function colsCount(){
    if (oldClickrow!=-1){
    ;
}
}

function ShowButtons(_type){
//_type为1时表示有数据为0时表述没有数据
if(_type==1){
}else{
}}
body.onload =function(){
     FormatInput("SHENHLX",0);
}
////////////流程命

	var objselectColM;
	var oldSelectobjM;
	var oldinputM;
	var oldClickrowM=-1;
	function classchangeM(_str1,_str2){
		var rows=oDataM.rows ;
		if(_str2=="onclick"){
			if (oldClickrowM !=-1){
				oDataM.rows(oldClickrowM).className="edittableTrOut";
			}
			oldClickrowM=_str1.rowIndex;
			oDataM.rows(oldClickrowM).className="edittableTrClick";
		}
		if(_str2=="onmouseout"){
			if (oldClickrowM!=_str1.rowIndex){
				_str1.className="edittableTrOut";
			}else{
				_str1.className="edittableTrClick";
			}
		}
		if(_str2=="onmouseover"){
			if (oldClickrowM!=_str1.rowIndex){
				_str1.className="edittableTrOn";
			}else{
				_str1.className="edittableTrClick";
			}
			
		}
		document.all.item("EditTableRowM").value=oldClickrowM;
	}
		function droponchangeM(_int){
	if (objselectColM !=null){
		objselectColM.value =oldSelectobjM.options(oldSelectobjM.selectedIndex).text;
		if (_int==1){
			updatarows(objselectColM.name);
		}
	}
}
function dropdownM(_obj1,_obj2){
	if (oldSelectobjM!=null){
		oldSelectobjM.style.display="none";
		 oldSelectobjM.selectedIndex=0;
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColM =_obj1;
	oldSelectobjM=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editinM(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputM!=null){
		oldinputM.style.border="#ffffff 0px solid";
		oldinputM.style.posWidth=oldinputM.parentElement.offsetWidth -2  ;
		oldinputM.style.backgroundColor="";
		oldinputM.style.position="";
		oldinputM.style.left="";
		oldinputM.style.height="";
		oldinputM.style.top="";
		oldinputM.style.padding =2;
	}
	if(!_str1.readOnly){
		_str1.style.backgroundColor="#ffffff";
		_str1.style.border="#FC0303 1px solid";
		_str1.style.posLeft =_str1.parentElement.offsetLeft;
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
			if (oldSelectobjM!=null){oldSelectobjM.style.display="none";}
		}
	}
	oldinputM =_str1;	
}
function editclickM(_obj1,_obj2){
	if (oldSelectobjM!=null){
		oldSelectobjM.style.display="none";
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColM =_obj1;
	oldSelectobjM=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangeM(_int){
	if (objselectColM !=null){
		objselectColM.value =oldSelectobjM.value;
		if (_int==1){
			
		}
	}
   // colCount();
}
////////////////////////////////////////////////////////
	var oldClickrowT=-1;
	function classchangeT(_str1,_str2){
		var rows=oDataT.rows ;
		if(_str2=="onclick"){
			if (oldClickrowT !=-1){
				oDataT.rows(oldClickrowT).className="edittableTrOut";
			}
			oldClickrowT=_str1.rowIndex;
			oDataT.rows(oldClickrowT).className="edittableTrClick";
		}
		if(_str2=="onmouseout"){
			if (oldClickrowT!=_str1.rowIndex){
				_str1.className="edittableTrOut";
			}else{
				_str1.className="edittableTrClick";
			}
		}
		if(_str2=="onmouseover"){
			if (oldClickrowT!=_str1.rowIndex){
				_str1.className="edittableTrOn";
			}else{
				_str1.className="edittableTrClick";
			}
		}
		document.all.item("EditTableRowT").value=oldClickrowT;
	}
	var objselectColT;
	var oldSelectobjT;
	var oldinputT;
	function droponchangeT(_int){
	if (objselectColT !=null){
		objselectColT.value =oldSelectobjT.options(oldSelectobjT.selectedIndex).text;
		if (_int==1){
			updatarows(objselectColT.name);
		}
	}
}
function dropdownT(_obj1,_obj2){
	if (oldSelectobjT!=null){
		oldSelectobjT.style.display="none";
		 oldSelectobjT.selectedIndex=0;
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColT =_obj1;
	oldSelectobjT=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editinT(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputT!=null){
		oldinputT.style.border="#ffffff 0px solid";
		oldinputT.style.posWidth=oldinputT.parentElement.offsetWidth -2  ;
		oldinputT.style.backgroundColor="";
		oldinputT.style.position="";
		oldinputT.style.left="";
		oldinputT.style.height="";
		oldinputT.style.top="";
		oldinputT.style.padding =2;
	}
	if(!_str1.readOnly){
		_str1.style.backgroundColor="#ffffff";
		_str1.style.border="#FC0303 1px solid";
		_str1.style.posLeft =_str1.parentElement.offsetLeft;
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
			if (oldSelectobjT!=null){oldSelectobjT.style.display="none";}
		}
	}
	oldinputT =_str1;	
}
function editclickT(_obj1,_obj2){
	if (oldSelectobjT!=null){
		oldSelectobjT.style.display="none";
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColT =_obj1;
	oldSelectobjT=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangeT(_int){
	if (objselectColT !=null){
		objselectColT.value =oldSelectobjT.value;
		if (_int==1){
			
		}
	}
   // colCount();
}

////////////////////////////////////////////////////////
	var oldClickrowL=-1;
	function classchangeL(_str1,_str2){
		var rows=oDataL.rows ;
		if(_str2=="onclick"){
			if (oldClickrowL !=-1){
				oDataL.rows(oldClickrowL).className="edittableTrOut";
			}
			oldClickrowL=_str1.rowIndex;
			oDataL.rows(oldClickrowL).className="edittableTrClick";
		}
		if(_str2=="onmouseout"){
			if (oldClickrowL!=_str1.rowIndex){
				_str1.className="edittableTrOut";
			}else{
				_str1.className="edittableTrClick";
			}
		}
		if(_str2=="onmouseover"){
			if (oldClickrowL!=_str1.rowIndex){
				_str1.className="edittableTrOn";
			}else{
				_str1.className="edittableTrClick";
			}
		}
		document.all.item("EditTableRowL").value=oldClickrowL;
	}
	var objselectColL;
	var oldSelectobjL;
	var oldinputL;
	function droponchangeL(_int){
	if (objselectColL !=null){
		objselectColL.value =oldSelectobjL.options(oldSelectobjL.selectedIndex).text;
		if (_int==1){
			updatarows(objselectColL.name);
		}
	}
}
function dropdownL(_obj1,_obj2){
	if (oldSelectobjL!=null){
		oldSelectobjL.style.display="none";
		 oldSelectobjL.selectedIndex=0;
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColL =_obj1;
	oldSelectobjL=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editinL(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputL!=null){
		oldinputL.style.border="#ffffff 0px solid";
		oldinputL.style.posWidth=oldinputL.parentElement.offsetWidth -2  ;
		oldinputL.style.backgroundColor="";
		oldinputL.style.position="";
		oldinputL.style.left="";
		oldinputL.style.height="";
		oldinputL.style.top="";
		oldinputL.style.padding =2;
	}
	if(!_str1.readOnly){
		_str1.style.backgroundColor="#ffffff";
		_str1.style.border="#FC0303 1px solid";
		_str1.style.posLeft =_str1.parentElement.offsetLeft;
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
			if (oldSelectobjL!=null){oldSelectobjL.style.display="none";}
		}
	}
	oldinputL =_str1;	
}
function editclickL(_obj1,_obj2){
	if (oldSelectobjL!=null){
		oldSelectobjL.style.display="none";
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColL =_obj1;
	oldSelectobjL=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangeL(_int){
	if (objselectColL !=null){
		objselectColL.value =oldSelectobjL.value;
		if (_int==1){
			
		}
	}
   // colCount();
}
	///
	var objselectColH;
	var oldSelectobjH;
	var oldinputH;
	var oldClickrowH=-1;
	function classchangeH(_str1,_str2){
		var rows=oDataH.rows ;
		if(_str2=="onclick"){
			if (oldClickrowH !=-1){
				oDataH.rows(oldClickrowH).className="edittableTrOut";
			}
			oldClickrowH=_str1.rowIndex;
			oDataH.rows(oldClickrowH).className="edittableTrClick";
		}
		if(_str2=="onmouseout"){
			if (oldClickrowH!=_str1.rowIndex){
				_str1.className="edittableTrOut";
			}else{
				_str1.className="edittableTrClick";
			}
		}
		if(_str2=="onmouseover"){
			if (oldClickrowH!=_str1.rowIndex){
				_str1.className="edittableTrOn";
			}else{
				_str1.className="edittableTrClick";
			}
			
		}
		document.all.item("EditTableRowH").value=oldClickrowH;
	}
		function droponchangeH(_int){
	if (objselectColH !=null){
		objselectColH.value =oldSelectobjH.options(oldSelectobjH.selectedIndex).text;
		if (_int==1){
			updatarows(objselectColH.name);
		}
	}
}
function dropdownH(_obj1,_obj2){
	if (oldSelectobjH!=null){
		oldSelectobjH.style.display="none";
		 oldSelectobjH.selectedIndex=0;
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColH =_obj1;
	oldSelectobjH=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editinH(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputH!=null){
		oldinputH.style.border="#ffffff 0px solid";
		oldinputH.style.posWidth=oldinputH.parentElement.offsetWidth -2  ;
		oldinputH.style.backgroundColor="";
		oldinputH.style.position="";
		oldinputH.style.left="";
		oldinputH.style.height="";
		oldinputH.style.top="";
		oldinputH.style.padding =2;
	}
	if(!_str1.readOnly){
		_str1.style.backgroundColor="#ffffff";
		_str1.style.border="#FC0303 1px solid";
		_str1.style.posLeft =_str1.parentElement.offsetLeft;
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
			if (oldSelectobjH!=null){oldSelectobjH.style.display="none";}
		}
	}
	oldinputH =_str1;	
}
function editclickH(_obj1,_obj2){
	if (oldSelectobjH!=null){
		oldSelectobjH.style.display="none";
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColH =_obj1;
	oldSelectobjH=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangeH(_int){
	if (objselectColH !=null){
		objselectColH.value =oldSelectobjH.value;
		if (_int==1){
			
		}
	}
   // colCount();
}
//动作

	var objselectColD;
	var oldSelectobjD;
	var oldinputD;
	var oldClickrowD=-1;
	function classchangeD(_str1,_str2){
		var rows=oDataD.rows ;
		if(_str2=="onclick"){
			if (oldClickrowD !=-1){
				oDataD.rows(oldClickrowD).className="edittableTrOut";
			}
			oldClickrowD=_str1.rowIndex;
			oDataD.rows(oldClickrowD).className="edittableTrClick";
		}
		if(_str2=="onmouseout"){
			if (oldClickrowD!=_str1.rowIndex){
				_str1.className="edittableTrOut";
			}else{
				_str1.className="edittableTrClick";
			}
		}
		if(_str2=="onmouseover"){
			if (oldClickrowD!=_str1.rowIndex){
				_str1.className="edittableTrOn";
			}else{
				_str1.className="edittableTrClick";
			}
			
		}
		document.all.item("EditTableRowD").value=oldClickrowD;
	}
		function droponchangeD(_int){
	if (objselectColD !=null){
		objselectColD.value =oldSelectobjD.options(oldSelectobjD.selectedIndex).text;
		if (_int==1){
			updatarows(objselectColD.name);
		}
	}
}
function dropdownD(_obj1,_obj2){
	if (oldSelectobjD!=null){
		oldSelectobjD.style.display="none";
		 oldSelectobjD.selectedIndex=0;
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColD =_obj1;
	oldSelectobjD=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editinD(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputD!=null){
		oldinputD.style.border="#ffffff 0px solid";
		oldinputD.style.posWidth=oldinputD.parentElement.offsetWidth -2  ;
		oldinputD.style.backgroundColor="";
		oldinputD.style.position="";
		oldinputD.style.left="";
		oldinputD.style.height="";
		oldinputD.style.top="";
		oldinputD.style.padding =2;
	}
	if(!_str1.readOnly){
		_str1.style.backgroundColor="#ffffff";
		_str1.style.border="#FC0303 1px solid";
		_str1.style.posLeft =_str1.parentElement.offsetLeft;
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
			if (oldSelectobjD!=null){oldSelectobjD.style.display="none";}
		}
	}
	oldinputD =_str1;	
}
function editclickD(_obj1,_obj2){
	if (oldSelectobjD!=null){
		oldSelectobjD.style.display="none";
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColD =_obj1;
	oldSelectobjD=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangeD(_int){
	if (objselectColD !=null){
		objselectColD.value =oldSelectobjD.value;
		if (_int==1){
			
		}
	}
   // colCount();
}
//////////////////////////////////////////////////////