
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
			calenDate.onchange();
		}
	}
//	cal.onchange = function() {
//		calenDate.value  = cal.formatDate();
//		calenDate.onchange();
//	}


body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop -25;
tablemainWidth =bodyWidth -15;

ConditionHeight =20;

ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
////////////////////////////////////////////////////////////////////////////


SelectDataHeight=tablemainHeight - ConditionHeight - 20;
///////////////////////////////////////////////////////////////////////////////
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
//SelectFrmDiv.style.position="relative";
//
//SelectHeadDiv.style.position="relative";
//SelectDataDiv.style.position="relative";

SelectFrmDiv1.style.position="relative";

SelectHeadDiv1.style.position="relative";
SelectDataDiv1.style.position="relative";
EditHeadTabel1.style.position="relative";

oData1.style.position="relative";



//trcondition.style.height=20;
//tablemain.style.left=0;
//
//tablemain.style.width=bodyWidth -15;
//tablemain.style.height=tablemainHeight;

////////////////////////////////////// ????????????
//SelectData.style.height=SelectDataHeight;
//SelectData.style.width=SelectDataWidth;
//
//SelectFrmDiv.style.height=SelectDataHeight;
//SelectFrmDiv.style.width=SelectDataWidth;
//SelectFrmDiv.style.posTop=0;
//
//SelectHeadDiv.style.posTop=0;
//SelectHeadDiv.style.width=SelectDataWidth-10;
//SelectHeadDiv.style.height=24;
//SelectHeadDiv.style.posLeft=0;
//
//SelectDataDiv.style.posTop=0;
//SelectDataDiv.style.width=SelectDataWidth-7;
//SelectDataDiv.style.height=SelectDataHeight-22;
//SelectDataDiv.style.posLeft=-2;

//if (SelectDataDiv.scrollHeight > SelectDataDiv.clientHeight){
//	SelectHeadDiv.style.width=SelectDataWidth-25;
//}
////////////////////////////////////????/*

SelectData1.style.height=SelectDataHeight;
SelectData1.style.width=SelectDataWidth;

SelectFrmDiv1.style.height=SelectDataHeight;
SelectFrmDiv1.style.width=SelectDataWidth;
SelectFrmDiv1.style.posTop=0;

SelectHeadDiv1.style.posTop=0;
SelectHeadDiv1.style.width=SelectDataWidth-10;
SelectHeadDiv1.style.height=24;
SelectHeadDiv1.style.posLeft=0;

SelectDataDiv1.style.posTop=0;
SelectDataDiv1.style.width=SelectDataWidth-7;
SelectDataDiv1.style.height=SelectDataHeight-22;
SelectDataDiv1.style.posLeft=-3;

if (SelectDataDiv1.scrollHeight > SelectDataDiv1.clientHeight){
	SelectHeadDiv1.style.width=SelectDataWidth-25;
}


//SelectFrmDiv2.style.border=0;
/*
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
				oData1.style.borderRight="gray 1px solid";
		EditHeadTabel1.style.borderRight="gray 1px solid";


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
	}
_obj2.selectedIndex=-1;
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft+2;
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
	//if (_str2=="onfocus"){
//		_str1.select();
///		return;
//	}
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
	_str1.select();
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
	_str1.parentElement.parentElement.click();
		
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
_obj2.tabIndex=_obj1.tabIndex ;
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth ;
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
//_type??1??????????????0??????????????
if(_type==1){
}else{
}}
body.onload =function(){
}
////////////////////////////////////////////////????

var oldClickrow1=-1;
function classchange1(_str1,_str2){

var rows=oData1.rows ;

	if(_str2=="onclick"){
		if (oldClickrow1 !=-1){
			oData1.rows(oldClickrow1).className="edittableTrOut";
		}
		oldClickrow1=_str1.rowIndex;
		oData1.rows(oldClickrow1).className="edittableTrClick";
	}

	if(_str2=="onmouseout"){
		if (oldClickrow1!=_str1.rowIndex){
			_str1.className="edittableTrOut";
		}else{
			_str1.className="edittableTrClick";
		}
	}
	
	if(_str2=="onmouseover"){
		
		if (oldClickrow1!=_str1.rowIndex){
			_str1.className="edittableTrOn";
		}else{
			_str1.className="edittableTrClick";
		}
		
	}
	document.all.item("EditTableRow1").value=oldClickrow1;
}
//////////////////////////////////////////////////????

function JihkjChange(obj){
	var n;
	var row;
	var str = obj.name;
	n=str.indexOf("$");
	if(n!=-1){
		row =eval(str.substring(n+1))+1;
	}else{
		row =0;
	}
	//alert(mkkj.length);
	for(mkindex=0;mkindex<mkkj.length;mkindex++){
		if(obj.value == mkkj[mkindex][0]){
			document.all.item("JIHKJB_ID",row).value = mkkj[mkindex][1];
		}
	}
}
////

function Xiugbz(changerow){
	if(changerow!=-1){
		document.all.item("XIUGBZ",changerow).value = "1";
	}
}

function ConXiugbz(obj){
	var n;b
	var row;
	var str = obj.name;
	n=str.indexOf("$");
	if(n!=-1){
		row =eval(str.substring(n+1))+1;
	}else{
		row =0;
	}
	if(row!=-1){
		document.all.item("XIUGBZ",row).value = "1";
	}
}


function find(){
	var l=document.all.item("oData").rows.length;
	var l1=document.all.item("oData1").rows.length;
	var objvalue=document.all.item("objvalue").value;
	for(var i=0;i<l;i++){
		if(document.all.item("CHEPH",i).value==objvalue.replace(/\s/g, "")){
			document.all.item("trid",i).click();
			if(document.all.item("EditTableRow1").value!=-1){
				document.all.item("trid1",document.all.item("EditTableRow1").value).className='';
			    document.all.item("EditTableRow1").value=-1;
			}
			return;
		}
	}
	if(i==l){//没搜到的话继续搜索
	if(document.all.item("EditTableRow").value!=-1){
	
		document.all.item("trid",document.all.item("EditTableRow").value).className='';
		document.all.item("EditTableRow").value=-1;
	}
	
		for(var j=0;j<l1;j++){
		if(document.all.item("CHEPH1",j).value==objvalue.replace(/\s/g, "")){
			document.all.item("trid1",j).click();
			return;
		}
		}
		if(j==l){//没搜到
		if(document.all.item("EditTableRow1").value!=-1){
			document.all.item("trid1",document.all.item("EditTableRow1").value).className='';
			document.all.item("EditTableRow1").value=-1;
		}
		
			alert("输入的车皮号不存在！");
		}
	}
}

function printq(){
		var num = 0;
        var rowlen = oData1.rows.length;
    	var flag = false;
    	for(i = 0; i<rowlen; i++){
        	if(document.all.item("XUANZ",i).checked == true){
            	flag = true;
            	break;
            }
        }
    	if(flag){
			
	        	var docObj = document.getElementById( 'printjjd' );
				if ( docObj )
				{	var pwin = window.open( "", "print", "height=400,width=900,top=200" ); 
					pwin.document.write( docObj.innerHTML ); 
					pwin.print( );
					pwin.location.reload( );
					pwin.close( );
				}        	
        }else{
            alert("请选择一条将要打印的检斤记录！");
        }
	}

    function Selected(obj){
        var num = 0;
        var rowlen = oData1.rows.length;
    	var i;
    	var flag = true;
        if(obj.checked == true){
	    	for(i = 0; i<rowlen; i++){
	            if(document.all.item("XUANZ",i).checked == true){
	            	num++;
	            }
	            if(num>=2){
	            	alert("请只选择一条将要打印的检斤记录！");
	            	flag = false;
	            	break;
	            }
	        }
            if(flag){
            	document.all.item("SaveButton").click();
            }
        }        
    }

