
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
		calenDate.value  = cal.formatDate();
		calenDate.onchange();
	}


body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop -15;
tablemainWidth =bodyWidth -15;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight  - 20;
SelectDataWidth=tablemainWidth ;
//QueryFrameClsssend
//////////????
SelectDataHeight=Math.round((tablemainHeight-ConditionHeight)/7 * 4-90);
SelectDataWidth=tablemainWidth ;
EditDataWidth=tablemainWidth ;
EditDataHeight=tablemainHeight-SelectDataHeight-ConditionHeight -15;
//EditFrmDiv.style.position="relative";

//////////??????
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
SelectHeadDiv.style.height=24;
SelectHeadDiv.style.posLeft=0;

SelectDataDiv.style.posTop=0;
SelectDataDiv.style.width=SelectDataWidth-7;
SelectDataDiv.style.height=SelectDataHeight-22;
SelectDataDiv.style.posLeft=-2;

if (SelectDataDiv.scrollHeight > SelectDataDiv.clientHeight){
	SelectHeadDiv.style.width=SelectDataWidth-25;
}


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
	for(i=0;i<3;i++){
	   document.all.item("div",i).style.width=EditDataWidth;
	   document.all.item("div",i).style.height=EditDataHeight-25;
    }
    setPageButtonEnabled();
}

var oldClickrow=-1;
var oldinput;
var objselectCol;
var oldSelectobj;
var Editrows=-1;

/////////////////////////////????
function ChangeSelectRow(obj1){//????
	if (Editrows!=-1){
		document.all.item(((obj1.id).substring(0,(obj1.id).indexOf("edit"))),Editrows).value=obj1.value;
		//colCount();
	}
}
function droponchange(obj1,obj2,_int){
	document.all.item(obj1,Editrows).value =obj2.options(obj2.selectedIndex).text;
	if (_int==1){
		updatarows(obj1.name);
	}
}
//????
function SelectRow(obj1){ 
    Editrows =obj1.rowIndex;//???
    //document.all.item("XUHedit").value=document.all.item("XUH",Editrows).value;
	document.all.item("BIANMedit").value=document.all.item("BIANM",Editrows).value;
	document.all.item("MINGCedit").value=document.all.item("MINGC",Editrows).value;
	document.all.item("QUANCedit").value=document.all.item("QUANC",Editrows).value;
//	document.all.item("PINYedit").value=document.all.item("PINY",Editrows).value;
	document.all.item("SHENGFB_IDedit").value=document.all.item("SHENGFB_ID",Editrows).value;
	document.all.item("FUIDedit").value=document.all.item("FUID",Editrows).value;
	document.all.item("DIZedit").value=document.all.item("DIZ",Editrows).value;
	document.all.item("YOUZBMedit").value=document.all.item("YOUZBM",Editrows).value;
	document.all.item("ZONGJedit").value=document.all.item("ZONGJ",Editrows).value;
	document.all.item("RANLCDHedit").value=document.all.item("RANLCDH",Editrows).value;
	document.all.item("ZHUANGJRLedit").value=document.all.item("ZHUANGJRL",Editrows).value;
	document.all.item("ZUIDKCedit").value=document.all.item("ZUIDKC",Editrows).value;
	document.all.item("ZHENGCCBedit").value=document.all.item("ZHENGCCB",Editrows).value;
	document.all.item("XIANFHKCedit").value=document.all.item("XIANFHKC",Editrows).value;
	document.all.item("RIJHMedit").value=	document.all.item("RIJHM",Editrows).value;
	document.all.item("JINGJCMSXedit").value=document.all.item("JINGJCMSX",Editrows).value;
	document.all.item("JINGJCMXXedit").value=document.all.item("JINGJCMXX",Editrows).value;
	document.all.item("DONGCMZBedit").value=document.all.item("DONGCMZB",Editrows).value;
	document.all.item("FADDBRedit").value=document.all.item("FADDBR",Editrows).value;
	document.all.item("WEITDLRedit").value=document.all.item("WEITDLR",Editrows).value;
	document.all.item("KAIHYHedit").value=document.all.item("KAIHYH",Editrows).value;
	document.all.item("ZHANGHedit").value=document.all.item("ZHANGH",Editrows).value;
//	document.all.item("DIANHedit").value=	document.all.item("DIANH",Editrows).value;
	document.all.item("SHUIHedit").value=	document.all.item("SHUIH",Editrows).value;
	document.all.item("JIEXFSedit").value=document.all.item("JIEXFS",Editrows).value;
	document.all.item("JIEXXedit").value=	document.all.item("JIEXX",Editrows).value;
	document.all.item("JIEXNLedit").value=document.all.item("JIEXNL",Editrows).value;
	document.all.item("CAIYFSedit").value=document.all.item("CAIYFS",Editrows).value;
	document.all.item("JILFSedit").value=	document.all.item("JILFS",Editrows).value;
	document.all.item("RANLFZRedit").value=document.all.item("RANLFZR",Editrows).value;
	document.all.item("LIANXDZedit").value=document.all.item("LIANXDZ",Editrows).value;
	document.all.item("JINGJCMLedit").value=document.all.item("JINGJCML",Editrows).value;
//	document.all.item("JIBedit").value=document.all.item("JIB",Editrows).value;
	document.all.item("BEIZedit").value=document.all.item("BEIZ",Editrows).value;
//	document.all.item("DIANCLBB_IDedit").value=document.all.item("DIANCLBB_ID",Editrows).value;
	document.all.item("DAOGedit").value=document.all.item("DAOG",Editrows).value;
	document.all.item("DAOZedit").value=document.all.item("DAOZ",Editrows).value;
    SetDisabled('table1',false);
	SetDisabled('table2',false);
	SetDisabled('table3',false);
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
//????
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

//QueryFrameClsssBegin
/*????
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
   // colCount();
}
*/
//QueryFrameClsssend
///???
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
//??
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
////?????????
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


//?????

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
     FormatInput("XUH",0);
     FormatInput("ZHUANGJRL",0);
     FormatInput("ZUIDKC",0);
     FormatInput("ZHENGCCB",0);
     FormatInput("XIANFHKC",0);
     FormatInput("RIJHM",0);
     FormatInput("jiexnl",0);
     
}

function setPageButtonEnabled(){
	var cPage;
	var tPages ;
	cPage = document.getElementById("CurrentPage").value;
	tPages = document.getElementById("TotalPages").value;
	var object =document.getElementById("pageno");
	if(cPage==0){
		document.getElementById("FirstPageButton").disabled= true;
		document.getElementById("UpPageButton").disabled= true;
		document.getElementById("LastPageButton").disabled= true;
		document.getElementById("DownPageButton").disabled= true;
		document.getElementById("GoPageButton").disabled= true;
		object.value="";
		return;
	}
	document.getElementById("GoPageButton").disabled= false;
	object.innerText = cPage.toString()+"/"+tPages.toString(); 
	object.style.fontWeight = "bolder";
	if(cPage==1){
		document.getElementById("FirstPageButton").disabled= true;
		document.getElementById("UpPageButton").disabled= true;
		if(tPages==1){
			document.getElementById("LastPageButton").disabled= true;
			document.getElementById("DownPageButton").disabled= true;
			document.getElementById("GoPageButton").disabled= true;
		}
		return;
	}
	if(cPage==tPages){
		document.getElementById("LastPageButton").disabled= 

true;
		document.getElementById("DownPageButton").disabled= 

true;
		return;
	}
	document.getElementById("FirstPageButton").disabled= false;
	document.getElementById("LastPageButton").disabled= false;
	document.getElementById("UpPageButton").disabled= false;
	document.getElementById("DownPageButton").disabled= false;
}

function IllegalPage(){
//	要求输入正确的数字
	var gopage ;
	var tPages;
	tPages = document.getElementById("TotalPages").value;
	gopage = document.getElementById("GoPage").value;
//	alert("gopage = " + gopage.toString() + ";tPages = " + tPages.toString());
	if(eval(gopage - 1) >= 0 && eval(tPages - gopage) >=0){
		return true;
	}
	alert("当前页不存在，请重新输入！");
	return false;
}

function ResetSEL(obj){
	var row = obj.parentElement.parentElement.rowIndex;
	for(var i = 0;i<oData.rows.length;i++){
		
		if(i.toString()!=row.toString()){
			document.all.item("SEL",i).checked = false;
		}
	}
	if(!obj.checked){
		oldClickrow = -1;
		
	}else{
		oldClickrow = row;
	}
	document.all.item("EditTableRow").value = oldClickrow;
}

function _Modulebar(imgRow){
	var context='/zgdt';
	document.all.item("TabbarSelect").value=imgRow;
	var OutLineID=document.all.item("imgbtn0");
	var TDid =TDBtn0;
	imgCount=OutLineID.length-1;
	if(imgCount==1){
		TDid.style.color="#336699";
		TDid.background=context + "/imgs/tab/img_formtab_back_on.gif";
		//alert(context);
		OutLineID(0).src="/imgs/tab/img_formtab_first_lefton.gif";
		OutLineID(1).src=context + "/imgs/tab/img_formtab_last_righton.gif";
		return;
	}
	for (i=0;i<(imgCount);i++){
		if(i>0){
			OutLineID(i).src=context + "/imgs/tab/img_formtab_middle_off.gif";
		}
		TDid(i).style.color="#888888";
		TDid(i).background=context + "/imgs/tab/img_formtab_back_off.gif";
		selectTab(i).style.display="none";
	}
	OutLineID(0).src=context + "/imgs/tab/img_formtab_first_leftoff.gif";
	OutLineID(imgCount).src=context + "/imgs/tab/img_formtab_last_rightoff.gif";//??
	//alert(context);
	TDid(imgRow).style.color="#336699";
	TDid(imgRow).background=context + "/imgs/tab/img_formtab_back_on.gif";
	if(imgRow ==0 ){//??
		OutLineID(0).src=context + "/imgs/tab/img_formtab_first_lefton.gif";
	}else{
		OutLineID(imgRow).src=context + "/imgs/tab/img_formtab_middle_righton.gif";
	}
	if((imgRow+1)!=imgCount){//??
		OutLineID(imgRow+1).src=context + "/imgs/tab/img_formtab_middle_lefton.gif";
	}else{
		OutLineID(imgRow+1).src=context + "/imgs/tab/img_formtab_last_righton.gif";
	}
	selectTab(imgRow).style.display="";
}
function setRow1(){
	if(oData.rows.length>0){
		if(document.all.item("EditTableRow").value==-1){
			SetDisabled('table1',true);
			SetDisabled('table2',true);
			SetDisabled('table3',true);
		}else{
			//if(oData.rows.length>document.all.item("EditTableRow").value || document.all.item("EditTableRow").value ==0 )
			//	SelectRowEdit(document.all.item("EditTableRow").value);
			//}
		}
	}
	else{
		SetDisabled('table1',true);
	    SetDisabled('table2',true);
		SetDisabled('table3',true);
	}
}