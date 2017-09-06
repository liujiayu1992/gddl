
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
		n=str.indexOf("-");
		smonth=str.substring(0,n);
		sday=str.substring(n+1);
		return(smonth +"/" + sday+"/" +syear);
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
			   		if(oTmp.tagName != "BODY"){  		
			      		oTmp = oTmp.offsetParent;
			      	}
			   } 
			while (oTmp.tagName != "BODY");
		    if (bf){
		    	calenDate=null;
				cal.hide();
			}
		}else{
			cal.hide();
			cal.show(calenDate);
		}
	}
}


body.onkeydown=function(){
	sel =event.srcElement;
	//if(event.keyCode == 13){
	//	event.keyCode = 169;
	//}
	
	switch (event.keyCode)
    {
        case (9)    :
       		break;
        case(13):
        	//alert(sel.id);
        	if(sel.id=="body"){
        		//alert("111");
        		//event.keyCode = 9;
				event.returnValue = false;
        	}else{
        		if(sel.id==""){
	        		sel.id = "body";
        		}
        		event.returnValue = false;
        		//event.keyCode = 9;
	        	//alert("222");
			}
       		break;
        default :
            event.returnValue = true;
            break;
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

	cal = new TimeCalendar();
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
	

var ConditionHeight;
var ConditionWidth;


body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop -15;
tablemainWidth =bodyWidth -15;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight  -6;
SelectDataWidth=tablemainWidth +12;
//QueryFrameClsssend

SelectFrmDiv.style.position="relative";

SelectHeadDiv.style.position="relative";
SelectDataDiv.style.position="relative";
trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

//????????
SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;

SelectHeadDiv.style.posTop=0;
SelectHeadDiv.style.width=SelectDataWidth-10;
SelectHeadDiv.style.height=30;
SelectHeadDiv.style.posLeft=0;

SelectDataDiv.style.posTop=0;
SelectDataDiv.style.width=SelectDataWidth-7;
SelectDataDiv.style.height=SelectDataHeight-22;
SelectDataDiv.style.posLeft=-2;

if (SelectDataDiv.scrollHeight > SelectDataDiv.clientHeight){
	SelectHeadDiv.style.width=SelectDataWidth-25;
}

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
SelectHeadDiv.scrollLeft=SelectDataDiv.scrollLeft;

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

	if(document.all.item("EditTableRow").value!=-1){
		if(oData.rows.length == 0){
			return;
		}
		var rowedit = eval(document.all.item("EditTableRow").value);
		//document.all.item("CHEPH",rowedit).focus();
	}
	
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
	obj2.selectedIndex = 0;
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
		ConXiugbz(objselectCol);
		oldSelectobj.selectedIndex =0 ;
		oldSelectobj.style.display="none";
	}
	
}
function dropdown(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}
	_obj2.style.position="absolute";
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft+2;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
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
		Xiugbz(i);
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

function ChangeSelectRow(obj1,_int){
	if (obj1!=null){
	//	alert(obj1.value);
	//	document.all.item(((obj1.id).substring(0,(obj1.id).indexOf("edit"))),Editrows).value=obj1.value;
		if(_int==1){
			updatarows(obj1.name);
		}
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
	//_obj2.focus();
	_obj2.select();
}

function editchange(_int){
	if (objselectCol !=null){
	//alert();
		if(objselectCol.id=='CHEPH'){
			Trim(oldSelectobj);
			if(MatchStr(oldSelectobj,20)){
				
			}else{
				oldSelectobj.select();
				return;
			}
		}else
			if(objselectCol.id=='BIAOZ'){
				Trim(oldSelectobj);
				if(MatchNum(oldSelectobj,/^[0-9]{0,5}\.[0-9]{0,3}$|^[0-9]{0,5}$/)){
					
				}else{
					oldSelectobj.select();
					return;
				}
			}else
				if(objselectCol.id=='DAOZCH'){
					Trim(oldSelectobj);
					if(MatchStr(oldSelectobj,20)){
						
					}else{
						oldSelectobj.select();
						return;
					}
				}else
					if(objselectCol.id=='CHEC'){
						Trim(oldSelectobj);
						if(MatchStr(oldSelectobj,20)){
							
						}else{
							oldSelectobj.select();
							return;
						}
					}
		
		objselectCol.value =oldSelectobj.value;
		xiugrow = eval(document.all.item("EditTableRow").value);
		Xiugbz(xiugrow);
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
     //FormatInput("BIAOZ",0);
}

function Xiugbz(changerow){
	if(changerow!=-1){
		document.all.item("XIUGBZ",changerow).value = "1";
	}
}

function ConXiugbz(obj){
	var n;
	var row;
	var str = obj.name;
	n=str.indexOf("$");
	if(n!=-1){
		row =eval(str.substring(n+1))+1;
	}else{
		row =0;
	}
	if(row!=-1){
		Xiugbz(row);
	}
}

function CopyLastRow(lastrow){
	nextrow = eval(lastrow)+1;
	document.all.item("FAHDWB_ID",nextrow).value = document.all.item("FAHDWB_ID",lastrow).value;
	document.all.item("MEIKXXB_ID",nextrow).value = document.all.item("MEIKXXB_ID",lastrow).value;
	document.all.item("FAZ_ID",nextrow).value = document.all.item("FAZ_ID",lastrow).value;
	document.all.item("RANLPZB_ID",nextrow).value = document.all.item("RANLPZB_ID",lastrow).value;
	document.all.item("FAHRQ",nextrow).value = document.all.item("FAHRQ",lastrow).value;
	document.all.item("JIHKJB_ID",nextrow).value = document.all.item("JIHKJB_ID",lastrow).value;
	document.all.item("CHANGBB_ID",nextrow).value = document.all.item("CHANGBB_ID",lastrow).value;
	document.all.item("CHEB",nextrow).value = document.all.item("CHEB",lastrow).value;
	document.all.item("YUNSFS",nextrow).value = document.all.item("YUNSFS",lastrow).value;
}


function chepenter(){
//alert(event.keyCode);
	if(event.keyCode != 13 && event.keyCode != 9){
		var n;
		var row;
		var str = objselectCol.name;
		n=str.indexOf("$");
		if(n!=-1){
			row =eval(str.substring(n+1))+1;
		}else{
			row =0;
		}
		if(objselectCol.id=='CHEPH'){
			oldv = oldSelectobj.value;
			if(oldv.length >= chehws){
				//alert(oldv.substring(0,chehws));
				oldSelectobj.value = oldv.substring(0,chehws);
				objselectCol.value = oldSelectobj.value;
				//event.keyCode = 9;
				document.all.item("BIAOZ",row).focus();
			}
		}else
			if(objselectCol.id=='BIAOZ'){
				Trim(oldSelectobj);
				if(MatchNum(oldSelectobj,/^[0-9]{0,5}\.[0-9]{0,3}$|^[0-9]{0,5}$/)){
					
				}else{
					oldSelectobj.select();
					return;
				}
				oldv = oldSelectobj.value;
				if(oldv.length >= biaozws){
					//alert();
					nextrow = row+1;
					var tmpvalue = oldv.substring(0,biaozws);
					if(biaozws == 3){
						tmpvalue = tmpvalue/10;
					}
					oldSelectobj.value = tmpvalue;
					objselectCol.value = oldSelectobj.value;
					if(oData.rows.length > nextrow ){
						CopyLastRow(row);
						document.all.item("CHEPH",nextrow).focus();
					}else{
						alert("已到达数据末尾！");
					}
				}
			}
			Xiugbz(row);
	}else{
		//alert("dd");
	}
}

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
function insert(){
	Objsel =document.selection.createRange();
	Objsel.expand("character");
	Objsel.select();
}

function selectAll(){
	if(oData.rows.length == 0){
		//alert();
			return;
	}else{
		//alert(oData.rows.length);
		for(row=0;row<oData.rows.length;row++){
			document.all.item("FLAG",row).checked = document.all.item("AllCheck").checked;
		}
	}
}