var clickFlag = 1;//标识用户单击了复选框
var cal;
var calenDate;
var selectCol;
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
	    var str = obj.value;
		cal.setSelectedDate(GetDataString(str));
		cal.toggle(calenDate);
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
	//if (oldSelectobj1!=null){oldSelectobj1.style.display="none";}
}

var windowOper;
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
		if(calenDate.value  != cal.formatDate()){
			calenDate.value  = cal.formatDate();
			calenDate.onchange();
		}
	}


	body.style.overflow="hidden";
	bodyHeight =body.clientHeight;
	bodyWidth =body.clientWidth;
	bodyTop=body.clientTop;
}

function getValue(val){
	var _return = "";
	var _obj2 = document.getElementById("FEIYXM")
	for (i=0;i<_obj2.options.length;i++){
		if (val==_obj2.options(i).value){
			_return = _obj2.options(i).text;
			break;
		}
	}
	return _return;	
}

function setFontColor(){
	var _len=0;
	if(oData.rows.length<=0){
		return;
	}
	_len = oData.rows.length;//document.all.item("SEL").length;
	if(_len==1){
		var colorStyle = "red";
		
		if(document.all.item("COLORFLAG").value==1){
			colorStyle ="orange";
		}
		if(document.all.item("COLORFLAG").value==2){
			colorStyle = "green";
		}
		var strVariable = "";
		var obj;
		obj = document.all.item("SHIFSH");
		obj.style.color = colorStyle;
		obj = document.all.item("FAHDW");
		obj.style.color = colorStyle;
		obj = document.all.item("MEIKDW");
		obj.style.color = colorStyle;
		obj = document.all.item("FAHRQ");
		obj.style.color = colorStyle;
		obj = document.all.item("FAZ");
		obj.style.color = colorStyle;
		obj = document.all.item("CHES");
		obj.style.color = colorStyle;
		obj = document.all.item("BIAOZ");
		obj.style.color = colorStyle;
		obj = document.all.item("JIHKJ");
		obj.style.color = colorStyle;
		obj = document.all.item("DAOZ");
		obj.style.color = colorStyle;
		return;
	}
	for(var i = 0;i<_len;i++){
		
		var colorStyle = "red";
		
		if(document.all.item("COLORFLAG",i).value==1){
			colorStyle ="orange";
		}
		if(document.all.item("COLORFLAG",i).value==2){
			colorStyle = "green";
		}
		var strVariable = "";
		var obj;
		obj = document.all.item("SHIFSH",i);
		obj.style.color = colorStyle;
		obj = document.all.item("FAHDW",i);
		obj.style.color = colorStyle;
		obj = document.all.item("MEIKDW",i);
		obj.style.color = colorStyle;
		obj = document.all.item("FAHRQ",i);
		obj.style.color = colorStyle;
		obj = document.all.item("FAZ",i);
		obj.style.color = colorStyle;
		obj = document.all.item("CHES",i);
		obj.style.color = colorStyle;
		obj = document.all.item("BIAOZ",i);
		obj.style.color = colorStyle;
		obj = document.all.item("JIHKJ",i);
		obj.style.color = colorStyle;
		obj = document.all.item("DAOZ",i);
		obj.style.color = colorStyle;
				
	}
	
	if(oDatawei.rows.length<=0){
		return;
	}
	_len = oDatawei.rows.length;
	
	for(var i = 0;i<_len;i++){
		
		if(document.all.item("WeiFARL",i).value==0){
			var colorStyle = "red";
			var strVariable = "";
			var obj;
			obj = document.all.item("WeiXUH",i);
			obj.style.color = colorStyle;
			obj = document.all.item("WeiCHEPH",i);
			obj.style.color = colorStyle;
			obj = document.all.item("DAOZCPH",i);
			obj.style.color = colorStyle;
			obj = document.all.item("WeiBIAOZ",i);
			obj.style.color = colorStyle;
			obj = document.all.item("WeiCHEB",i);
			obj.style.color = colorStyle;
			obj = document.all.item("WeiFARL",i);
			obj.style.color = colorStyle;
		}
	}
	
}

var oldClickrow=-1;
var oldClickrowYi=-1;
var oldinput;
var oldinput1;
var objselectCol;
var objselectCol1;
var oldSelectobj;
var oldSelectobj1;
var Editrows=-1;

//EditFrameClsssBegin
function droponchange(obj1,obj2,_int){
	if(Editrows!=-1){
		document.all.item(obj1,Editrows).value =obj2.options(obj2.selectedIndex).text;
		if (_int==1){
			updatarows(obj1.name);
		}
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
//EditFrameClsssEnd

function dropdown1(_obj1,_obj2){
	if (oldSelectobj1!=null){
		oldSelectobj1.style.display="none";
	}
	_obj2.style.borderRight="0px";
	_obj2.style.borderLeft="0px";
	_obj2.style.borderTop="0px";
	_obj2.style.borderBottom="0px";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft + document.all.item("lefttdhp").clientWidth + 14 ;
	_obj2.style.posTop=_obj1.parentElement.offsetTop  +61;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol1 =_obj1;
	oldSelectobj1=_obj2;
	_obj2.selectedIndex=0;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//alert(_obj1.value.toString());
			//_obj2.value=i;
			break;
		}
	}	
//	判断下拉框是否为“请选择”项,如果是，请清除费别和费用
	if(_obj2.selectedIndex==0){
		
		
	}
}

function dropdownFeibIDChanged(_obj1,_obj2,_obj3,_obj4){
//	_obj1 :费别的编辑框对象
//  _obj2 :下拉框FeiyxmDropDown对象
//	_obj3 :货票金额的编辑框对象（1-18）
//	_obj4 :费别ID
	if (oldSelectobj1!=null){
		oldSelectobj1.style.display="none";
	}
	_obj2.style.borderRight="0px";
	_obj2.style.borderLeft="0px";
	_obj2.style.borderTop="0px";
	_obj2.style.borderBottom="0px";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft + document.all.item("lefttdhp").clientWidth + 14 ;
	_obj2.style.posTop=_obj1.parentElement.offsetTop  +60;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol1 =_obj1;
	oldSelectobj1=_obj2;
	_obj2.selectedIndex=0;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).value){
			_obj2.selectedIndex=i;
			_obj4.value = _obj2.options(i).value;
			break;
		}
	}	
}



function dropdownFeibChanged(_obj1,_obj2,_obj3,_obj4){
//	_obj1 :费别的编辑框对象
//  _obj2 :下拉框FeiyxmDropDown对象
//	_obj3 :货票金额的编辑框对象（1-18）
//	_obj4 :费别ID
	if (oldSelectobj1!=null){
		oldSelectobj1.style.display="none";
	}
	_obj2.style.borderRight="0px";
	_obj2.style.borderLeft="0px";
	_obj2.style.borderTop="0px";
	_obj2.style.borderBottom="0px";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft + document.all.item("lefttdhp").clientWidth + 14 ;
	//alert(_obj1.parentElement.offsetTop);
	_obj2.style.posTop=_obj1.parentElement.offsetTop  +60;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol1 =_obj1;
	oldSelectobj1=_obj2;
	_obj2.selectedIndex=0;
	_obj4.value = 0;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//alert(_obj1.value.toString());
			//_obj2.value=i;
			_obj4.value = _obj2.options(i).value;
			break;
		}
	}	
////	判断下拉框是否为“请选择”项,如果是，请清除费别和费用
//
//alert(_obj2.options(_obj2.selectedIndex).value);
//	if(_obj2.options(_obj2.selectedIndex).text=="请选择"){
//		_obj3.value = "0.0";//
//		_obj1.value = "";//
//		_obj4.value = "-1";
//		Hej();	
////		alert("");	
//	}
}

function classchange(_str1,_str2){

var rows=oDatawei.rows ;

	if(_str2=="onclick"){
		if (oldClickrow !=-1){
			oDatawei.rows(oldClickrow).className="edittableTrOut";
		}
		oldClickrow=_str1.rowIndex;
		oDatawei.rows(oldClickrow).className="edittableTrClick";
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
	document.all.item("WeiEditTableRow").value=oldClickrow;
}

function classchangeYi(_str1,_str2){

var rows=oDatayi.rows ;

	if(_str2=="onclick"){
		if (oldClickrowYi !=-1){
			oDatayi.rows(oldClickrowYi).className="edittableTrOut";
		}
		oldClickrowYi=_str1.rowIndex;
		oDatayi.rows(oldClickrowYi).className="edittableTrClick";
	}

	if(_str2=="onmouseout"){
		if (oldClickrowYi!=_str1.rowIndex){
			_str1.className="edittableTrOut";
		}else{
			_str1.className="edittableTrClick";
		}
	}
	
	if(_str2=="onmouseover"){
		
		if (oldClickrowYi!=_str1.rowIndex){
			_str1.className="edittableTrOn";
		}else{
			_str1.className="edittableTrClick";
		}
		
	}
	document.all.item("YiEditTableRow").value=oldClickrowYi;
}

function classchanget(_str1,_str2){

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
	//document.all.item("EditTableRow").value=oldClickrow;
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
	if (oldSelectobj1!=null){oldSelectobj1.style.display="none";}
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
		colCount();
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
	if (objselectCol1 !=null){
		objselectCol1.value =oldSelectobj1.value;
		if (_int==1){
			updatarows(objselectCol1.name);
		}
	}
   // colCount();
}

function SelectRow(obj1){ 
    Editrows =obj1.rowIndex;
    var len = document.all.oDatawei.rows.length ;
//    检测用户设置的货票类型：单票、联票
//	if(document.Form0.radiogroup[0].checked){
//		单票
//		for(var i=0;i<len;i++){
//			if(i!=Editrows){
//				document.all.item("WeiSEL",i).checked = false;
//			}
//		}
//	}
	if(clickFlag!=0){
		return ;
	}
	if(document.Form0.radiogroup[1].checked){
//		联票
//    需要判断checkbox的状态
		if(document.all.item("WeiSEL",Editrows).checked){
			document.all.item("WeiSEL",Editrows).checked = false;
			document.all.item("WeiBIAOZ",Editrows).value = document.all.item("OriginalBIAOZ",Editrows).value;
			document.all.item("WeiCHEB",Editrows).value = document.all.item("OriginalCHEB",Editrows).value;
			document.all.item("WeiBIAOZEdit").value="";
			document.all.item("WeiCHEPHedit").value="";
			return;
		}
		document.all.item("WeiSEL",Editrows).checked = true;
		
		dropdown("WeiCHEB",document.all.item("WeiCHEBSelect"));
		document.all.item("WeiBIAOZEdit").value=document.all.item("WeiBIAOZ",Editrows).value;
    	document.all.item("WeiCHEPHedit").value=document.all.item("WeiCHEPH",Editrows).value;
		return;
	}

//    需要判断checkbox的状态
	if(document.all.item("WeiSEL",Editrows).checked){
		document.all.item("WeiSEL",Editrows).checked = false;
		document.all.item("WeiCHEB",Editrows).value = document.all.item("OriginalCHEB",Editrows).value;
		document.all.item("WeiBIAOZ",Editrows).value = document.all.item("OriginalBIAOZ",Editrows).value;
		document.all.item("WeiBIAOZEdit").value = "";
		document.all.item("WeiCHEPHedit").value = "";
		dropdown("WeiCHEB",document.all.item("WeiCHEBSelect"));
		return;
	}else{
		document.all.item("WeiSEL",Editrows).checked = true;
		document.all.item("WeiBIAOZ",Editrows).value = document.all.item("OriginalBIAOZ",Editrows).value;
		document.all.item("WeiCHEB",Editrows).value = document.all.item("OriginalCHEB",Editrows).value;
		document.all.item("WeiBIAOZEdit").value=document.all.item("WeiBIAOZ",Editrows).value;
    	document.all.item("WeiCHEPHedit").value=document.all.item("WeiCHEPH",Editrows).value;
    	dropdown("WeiCHEB",document.all.item("WeiCHEBSelect"));
		
	}
}
function colCount(){
    if (Editrows!=-1){
    ;
;
    }
}

function ShowButtons(_type){
//_type为1时表示有数据为0时表述没有数据
if(_type==1){
}else{
}}
body.onload =function(){
	 FormatInput("WeiFARL",0);
	 FormatInput("CHES",0);
	 FormatInput("BIAOZ",0);
	 FormatInput("OriginalBIAOZ",0);
	 FormatInput("WeiBIAOZ",0);
	 FormatInput("YiBIAOZ",0);
	 FormatInput("TMPBIAOZ",0);
	 FormatInput("HPHEJ",2);
	 
}
function dblClick(){
	if(document.Form0.radiogroup[0].checked){
		document.Form0.DanpDblButton.click();
		return;
		//return;//双击行时，不进行任何操作
	}else{
		document.Form0.DanLianPButton.click();
		return;
	}
}
function singleCheck(obj){
	clickFlag = 1;
	var Editrows;
	var objTD,objTR;
	objTD = obj.parentElement;
	objTR = objTD.parentElement;
	Editrows = objTR.rowIndex;
	var len = document.all.oDatawei.rows.length ;
//    检测用户设置的货票类型：单票、联票
//	if(document.Form0.radiogroup[0].checked){
////		单票
//		for(var i=0;i<len;i++){
//			if(i!=Editrows){
//				document.all.item("WeiSEL",i).checked = false;
//			}
//		}
//	}
	if(document.Form0.radiogroup[1].checked){
//		联票
		document.all.item("WeiSEL",Editrows).checked = false;
		return;
	}
//    需要判断checkbox的状态
	if(obj.checked){
		document.all.item("WeiBIAOZ",Editrows).value = document.all.item("OriginalBIAOZ",Editrows).value;
		document.all.item("WeiCHEB",Editrows).value = document.all.item("OriginalCHEB",Editrows).value;
		document.all.item("WeiBIAOZEdit").value=document.all.item("WeiBIAOZ",Editrows).value;
    	document.all.item("WeiCHEPHedit").value=document.all.item("WeiCHEPH",Editrows).value;
//    	dropdown("WeiCHEB",document.all.item("WeiCHEBSelect"));
	}else{
		obj.checked = false;
		document.all.item("WeiBIAOZ",Editrows).value = document.all.item("OriginalBIAOZ",Editrows).value;
		document.all.item("WeiCHEB",Editrows).value = document.all.item("OriginalCHEB",Editrows).value;
	    document.all.item("WeiBIAOZEdit").value="";
	    document.all.item("WeiCHEPHedit").value="";
//	    dropdown("WeiCHEB",document.all.item("WeiCHEBSelect"));
	}
}

function AllConfirm(){
//	如果没有数据，则不进行任何操作
	
	var _return = true;
	for(var i=0;i<document.all.item("WeiSEL").length;i++){
		
		_return =  false;
		
		break;
	}
	
	return _return;
}

function ShowChepInfo(){
	var  jiesbh =0;
	var obj;
	obj = document.getElementById("TuoshDropDown");
	jiesbh =  obj.options(obj.selectedIndex).text;
	var newPage = sourcePath + "&tuosbh=" + jiesbh;
	window.open(newPage,"tapmainwinNEW", "toolbar=0,resizable=0,top=70,left=70,width=800,height=600");
}


function hpshow(){
	if(document.all.item("YiEditTableRow").value<0){
		alert("请选择一张货票！");
		return false;
	}
	return true;
}
function DELETE(){
	var deleterow = document.all.item("YiEditTableRow").value;
		if(deleterow>=0){
			if(document.all.item("YiYUNFJSB_ID",deleterow).value <= 0){
				if(document.all.item("YiCHES",deleterow).value > 1){
					return confirm("该货票为联票，您将删除其所有相关信息，是否删除？");
				}
			}else{
				alert("该货票已结算不能删除！");
				return false;
			}
			return true;
		}else{
			alert("请选择一个发货！");
			return false;
		}
}
function droponchange1(){
	if (objselectCol1 != null ){
		partname = "HPFEIB";
		zhipartname = "HPJINE" + selectCol;
        feiybID = "HPFEIYID";
        var feiyIDname;
        feiyIDname = feiybID + selectCol;
        
        var repeatID = 0;        
		for(feibrow=0;feibrow < 19; feibrow++){
			allname = partname+feibrow;
			if(allname != objselectCol1.id){
				if(document.all.item(allname).value == oldSelectobj1.options(oldSelectobj1.selectedIndex).text && oldSelectobj1.options(oldSelectobj1.selectedIndex).text!="请选择"){
					document.all.item(feiyIDname).value = "-1";
                    alert("本货票已包含此费用");		
                	repeatID = -1;
					break;
				}
			}
            var feibname,jinename;
            
            feibname =partname+feibrow;
            jinename ="HPJINE"+feibrow;
            if(document.all.item(allname).value=="请选择"){
            	document.all.item(allname).value="";
            	document.all.item(jinename).value="0.0";
            	document.all.item(feiyIDname).value ="-1";
            }
            
		}
       
       jeobj = document.all.item("JINE");
       //alert(jeobj.options(oldSelectobj1.selectedIndex).id);
       //alert(oldSelectobj1.options(oldSelectobj1.selectedIndex).text)
       //alert(jeobj.options(oldSelectobj1.selectedIndex).text);
		if(repeatID!=-1){
	   		var zhi = 0.0;
	        document.all.item(feiyIDname).value = oldSelectobj1.options(oldSelectobj1.selectedIndex).value;
	        if(oldSelectobj1.options(oldSelectobj1.selectedIndex).text == "保价费"){
	        	zhi = document.all.item("HPBAOJJE").value;
	        	if(zhi != 0){
	        		zhi = Format(zhi/1000,1);
	        	}
	        }else{
	        	zhi = jeobj.options(oldSelectobj1.selectedIndex).text;
	        }
	        document.all.item(zhipartname).value = zhi;
	        document.all.item(zhipartname).fireEvent("onchange");
		}
		oldSelectobj1.style.display="none";
		objselectCol1.style.display="";
		if(feibrow == 19){
			objselectCol1.value =oldSelectobj1.options(oldSelectobj1.selectedIndex).text;
			//document.Form0.DropsubmitButton.click();
		}
	}
}
var flag = 0;
function changeflag(){
	flag = 1;
}
function feiylxdrop(){
	document.Form0.RefurbishButton.click();		
}
function test(){
	document.Form0.RefButton.click();
}
function FDD1change(){
	document.Form0.DropsubmitButton.click();
}
function findsame(obj){
	var flagxx = 1;
	var findrows=oData.rows;
    var findrow =obj.rowIndex;
	if(flag == 0){
		if(document.all.item("SEL",findrow).checked == false){
			document.all.item("SEL",findrow).checked = true;
		}else{
			document.all.item("SEL",findrow).checked = false;
		}	
	}else{
		flag = 0;
	}
	for(i=0;i < findrows.length;i++){
		if(i!= findrow){
			if(document.all.item("SEL",i).checked==true){
				if(document.all.item("MEIKDW",i).value!=document.all.item("MEIKDW",findrow).value ||document.all.item("FAZ",i).value!=document.all.item("FAZ",findrow).value
        		||document.all.item("JIHKJ",i).value!=document.all.item("JIHKJ",findrow).value || document.all.item("FAHDW",i).value!=document.all.item("FAHDW",findrow).value){
					alert('不同的发货不能作为一个货票！');
					flagxx = 0;
					document.all.item("SEL",findrow).checked = false;
				}
				break;
			}
		}
	}
	if(flagxx == 1){
		document.Form0.selectButton.click();
	}	
}
function findrows(){
	for (i=0;i < oDatawei.rows.length;i++){
		if(document.all.item("WeiCHEPH",i).value.toString() == document.all.item("WeiCHEPHedit",0).value.toString()){
			Editrows = i;
			dropdown("WeiCHEB",document.all.item("WeiCHEBSelect"));
   			document.all.item("WeiBIAOZEdit").value=document.all.item("WeiBIAOZ",Editrows).value;
    		document.all.item("WeiCHEPHedit").value=document.all.item("WeiCHEPH",Editrows).value;
    		document.all.item("WeiEditTableRow").value = Editrows;
			if(document.Form0.radiogroup[1].checked){
				document.Form0.DanLianPButton.click();
				document.all.item("WeiCHEPHedit").focus();
			}else{
        		document.all.item("WeiSEL",Editrows).checked=true;
        		for (j=0;j < oDatawei.rows.length;j++){
                    if(Editrows!=j){
                    	document.all.item("WeiSEL",j).checked=false;
                    	}
                    }
        	}
			break;
		}
	}
	if(i==oDatawei.rows.length){
		alert("该车皮号不存在！");
		document.all.item("WeiCHEPHedit").value="";
		document.all.item("WeiBIAOZedit").value=""
		document.all.item("WeiCHEPHedit").focus();
	}
}
				
	function  Modify_click(){
		if(document.Form0.radiogroup[0].checked){
			if(document.all.item("WeiEditTableRow").value == -1){
				alert("请选择一个未审核车皮！");
				return false;
			}else{
				return true;
			}
		}else{
			if(oDatatmp.rows.length == 0 ){
				alert("联票中没有任何车皮信息!");
				return false;
			}else{
				return true;
			}
		}	
		return true;
	}		
	var oldfeiym;
	var flagfeiy = 1;
	function feiymchange(){
		for(Dropindex = 0;Dropindex<document.all.item("FeiyxmDropDown1").options.length ;Dropindex++){
			if(document.all.item("FYFEIYMC").value == document.all.item("FeiyxmDropDown1").options(Dropindex).text){
				alert("费用名称重复");
			document.all.item("FYFEIYMC").value = "";
			document.all.item("FYFEIYMC").focus;
				flagfeiy = 0;
				break;
			}else{
				flagfeiy = 1;
			}
		}	
		if(flagfeiy == 1){
			if(document.all.item("FYGONGS").value == ""){
				document.all.item("FYGONGS").value = document.all.item("FYFEIYMC").value + "=";
				oldfeiym = document.all.item("FYFEIYMC").value;
			}else{
				var Str = document.all.item("FYGONGS").value;
				var change = Str.replace(oldfeiym,document.all.item("FYFEIYMC").value);
				document.all.item("FYGONGS").value = change;
				oldfeiym = document.all.item("FYFEIYMC").value;
			}	
		}		
	}		
		
	function ysblchange(obj1){
		document.all.item("FYGONGS").value = document.all.item("FYGONGS").value + obj1.options(obj1.selectedIndex).text;
	}				
				
	function feiyclick(){
		if(document.all.item("TmpEditTableRow").value == 3){
			if(document.all.item("FYGONGS").value == ""){	
					alert("请输入费用公式");
					return false;
				}else{
					return true;
				}
		}else{
			if(document.all.item("FYFEIYMC").value == ""){
				alert("请输入费用名称");
				return false;
			}else{
				if(document.all.item("FYGONGS").value == ""){	
					alert("请输入费用公式");
					return false;
				}else{
					return true;
				}
			}	
		}	
	}		
	function onclickfeiym(obj,_obj3){
            //alert(obj.value);
        if(obj.value==""){
        	//document.all.item("FEIYMINDEX").value = "请选择";
            _obj3.value = "0.0";//
			Hej();	
            //alert("no value;");
        }else{
			//document.all.item("FEIYMINDEX").value = obj.value;
            }
       selectCol =obj.id.replace("HPFEIB","");   
	}

	function Hej(){
        var sum=0;
        sum=
        parseFloat(document.all.item("HPJINE0").value)+ 
		parseFloat(document.all.item("HPJINE1").value)+
		parseFloat(document.all.item("HPJINE2").value)+
		parseFloat(document.all.item("HPJINE3").value)+
		parseFloat(document.all.item("HPJINE4").value)+
		parseFloat(document.all.item("HPJINE5").value)+
		parseFloat(document.all.item("HPJINE6").value)+
		parseFloat(document.all.item("HPJINE7").value)+
		parseFloat(document.all.item("HPJINE8").value)+
		parseFloat(document.all.item("HPJINE9").value)+
		parseFloat(document.all.item("HPJINE10").value)+
		parseFloat(document.all.item("HPJINE11").value)+
		parseFloat(document.all.item("HPJINE12").value)+
		parseFloat(document.all.item("HPJINE13").value)+
		parseFloat(document.all.item("HPJINE14").value)+
		parseFloat(document.all.item("HPJINE15").value)+
		parseFloat(document.all.item("HPJINE16").value)+
		parseFloat(document.all.item("HPJINE17").value);
        document.all.item("HPHEJ").value = Format(sum,2);    		
	}	
function countbaojf(obj){
	FeibName = "HPFEIB";
	JineName = "HPJINE";
	for(feibrow=0;feibrow < 19; feibrow++){
		allname = FeibName+feibrow;
		//alert(allname);
		if(document.all.item(allname).value == "保价费"){
			Ajinename = JineName + feibrow;
			document.all.item(Ajinename).value = Format(obj.value/1000,1);
			break;
		}
	}
	Hej();
}

function closeOpenWin(){
	//要求用户必须输入费别
	var hasFeiy = false;
	for (var i = 0; i< 19 ;++i){
		var obj ;
		var oName = "HPFEIYID"+i;
		obj = document.getElementById(oName);
		if(obj.value.toString()!="0" && obj.value.toString()!="-1"){
			hasFeiy = true;
			break;
		}
	}
	
	if(windowOper!=null){
		var sValue;
		var bClosed = windowOper.closed;
		if(bClosed){
			var aCookie = document.cookie.split(";");
			for (var i=0; i < aCookie.length; i++)
			{
			var aCrumb = aCookie[i].split("=");
			if ("allowsave" == aCrumb[0])
				sValue = unescape(aCrumb[1]);
			}
			
			if(sValue=="y"){
				document.getElementById("SaveButton").disabled = false;
			}else{
				alert("矿方托收数据没有保存。");
				document.getElementById("SaveButton").disabled = true;
				return false;
			}
			date = new Date();
			document.cookie = "allowsave=" + escape(sValue) + "; expires=" + date.toGMTString();
		}else{
			alert("请关闭其他数据录入窗口。");
			document.getElementById("SaveButton").disabled = true;
			return false;
		}
		
	}else{
	}
	
	if(!hasFeiy){
		alert("请选择费用。");
		return false;
	}
	
	return true;
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
function openInsertWindow(){
	document.getElementById("SaveButton").disabled = false;
	var sFeatures="height=600px,width=960px";
	var newPage = targetPath+ "&operation=refresh&tuosbh="+v_tuosbh+"&kuangftsh="+v_kuangftsh;
	var screenY = screen.height;
	var screenX = screen.width;
	var winTop = (screenY - 600)/2;
	var winLeft =(screenX - 960)/2;
	if(!eval(Number(winTop))>0){
		winTop = 0;
	}
	if(!eval(Number(winLeft))>0){
		winLeft = 0;
	}
	
	document.cookie="allowsave="+escape("n")+";";
	
	window.opener = window.open(newPage,"winNew", "toolbar=0,resizable=0,top="+winTop+"px,left="+winLeft+"px,"+sFeatures+",status=no,scrollbars=yes");
	window.opener.focus();
	windowOper = window.opener;
}	
