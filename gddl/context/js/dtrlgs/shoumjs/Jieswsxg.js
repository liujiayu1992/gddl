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
	}
document.all.item("shezhi").style.display="none";
body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop;
tablemainWidth =bodyWidth;
ConditionHeight =15;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight  - 20;
//SelectDataWidth=tablemainWidth ;
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

trcondition.style.height=15;
tablemain.style.left=0;

tablemain.style.width=bodyWidth;
tablemain.style.height=tablemainHeight;

//选择框架
//SelectData.style.height=SelectDataHeight;
//SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight+20;
//SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;
	
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
		
		if(objselectCol==document.all.item("SHOUKDW")){
			var ZH=document.all.item("ZhanghSelect");
   			var KH=document.all.item("KaihyhSelect");
   			var SK=document.all.item("SHOUKDWSelect");
			ZH.selectedIndex=SK.selectedIndex;
			KH.selectedIndex=SK.selectedIndex;
  			document.all.item("KAIHYH").value=KH.options(KH.selectedIndex).text;
  			document.all.item("ZHANGH").value=ZH.options(ZH.selectedIndex).text;
		}
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
	_obj2.style.posTop=_obj1.parentElement.offsetTop+49;
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
	_obj2.style.posLeft =_obj1.offsetLeft+85;
	_obj2.style.posTop=_obj1.offsetTop+28;
	_obj2.style.posWidth=_obj1.offsetWidth+1;
	_obj2.style.posHeight=_obj1.offsetHeight ; 
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
//	cbofun_TianzdwSelect();
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
}
//
function Count(){
	if(eval(document.all.item("GONGFL").value)>0){
    	document.all.item("GONGFSL").value=eval(document.all.item("GONGFL").value);
    	document.all.item("YANSSL").value=eval(document.all.item("GONGFL").value);
    	document.all.item("JIESSL").value=eval(document.all.item("GONGFL").value);
    	document.all.item("JIESSLJS").value=document.all.item("GONGFL").value;
    }
    colsCount();
}
//用jiessljs改变jiessl
function CountJiessl(){
	
	if(eval(document.all.item("JIESSLJS").value)>0){
	    
	   	document.all.item("JIESSL").value=eval(document.all.item("JIESSLJS").value);
	}
	colsCount();
}
//用yanssl改变结算数量
function CountYanssl(){

	if(eval(document.all.item("YANSSL").value)>0){
	    
	   	document.all.item("JIESSL").value=Math.round(eval(document.all.item("YANSSL").value)*100)/100;
	   	document.all.item("JIESSLJS").value=document.all.item("JIESSL").value;
	   	colsCount();
	}
}
//计算特殊盈亏
function CountYingk(){
	//热量
		
	document.all.item("RELZJJE").value=-Math.round(eval(document.all.item("JIESSL").value)*eval(document.all.item("RELZJBZ").value)*100)/100;
}
//初始化结算项
function Count3(){
	

	if(eval(document.all.item("YANSRL").value)>0&&eval(document.all.item("JIESRL").value)==0){
	    
	   	document.all.item("JIESRL").value=Math.round(eval(document.all.item("YANSRL").value)*100)/100;
	}
	
	if(eval(document.all.item("YANSLF").value)>0&&eval(document.all.item("JIESLF").value)==0){
	    
	   	document.all.item("JIESLF").value=Math.round(eval(document.all.item("YANSLF").value)*100)/100;
	}
	
	if(eval(document.all.item("YANSHFF").value)>0&&eval(document.all.item("JIESHFF").value)==0){
	    
	   	document.all.item("JIESHFF").value=Math.round(eval(document.all.item("YANSHFF").value)*100)/100;
	}
	
	if(eval(document.all.item("YANSHF").value)>0&&eval(document.all.item("JIESHF").value)==0){
	    
	   	document.all.item("JIESHF").value=Math.round(eval(document.all.item("YANSHF").value)*100)/100;
	}
	
	if(eval(document.all.item("YANSSF").value)>0&&eval(document.all.item("JIESSF").value)==0){
	    
	   	document.all.item("JIESSF").value=Math.round(eval(document.all.item("YANSSF").value)*100)/100;
	}
	
	if(eval(document.all.item("YANSSF").value)>0&&eval(document.all.item("JIESSF").value)==0){
	    
	   	document.all.item("JIESSF").value=Math.round(eval(document.all.item("YANSSF").value)*100)/100;
	}
    colsCount();
}
//计算价格
function Count2(){
	
	document.all.item("JIASJE").value=Math.round((eval(document.all.item("JIAKHJ").value)+eval(document.all.item("JIAKSK").value))*100)/100;;
	document.all.item("YUNZFHJ").value=Math.round((eval(document.all.item("YUNFSK").value)+eval(document.all.item("BUHSYF").value))*100)/100;
	document.all.item("JIAKJE").value=Math.round((eval(document.all.item("JIAKHJ").value)-eval(document.all.item("BUKYQJK").value))*100)/100;
	document.all.item("HEJ").value=Math.round((eval(document.all.item("YUNZFHJ").value)+eval(document.all.item("JIASJE").value))*100)/100;
    document.all.item("MEIKHJDX").value=tormb(eval(document.all.item("JIASJE").value));
    document.all.item("YUNZFHJDX").value=tormb(eval(document.all.item("YUNZFHJ").value));
	document.all.item("DAXHJ").value=tormb(eval(document.all.item("HEJ").value));
}
//计算普通盈亏
function colsCount(){
	
	if(eval(document.getElementById("JIAKJE").value)>0){
		
		document.all.item("JIASJE").value=Math.round((eval(document.all.item("HANSDJ").value)*eval(document.all.item("JIESSL").value)+eval(document.all.item("BUKYQJK").value))*100)/100;
		document.all.item("JIAKHJ").value=Math.round(eval(document.all.item("JIASJE").value)/(1+eval(document.all.item("JIAKSL").value))*100)/100;
		document.all.item("JIAKSK").value =Math.round((eval(document.all.item("JIASJE").value)-eval(document.all.item("JIAKHJ").value))*100)/100;
		document.all.item("JIAKJE").value =Math.round((eval(document.all.item("JIAKHJ").value)-Math.round(eval(document.all.item("BUKYQJK").value)/(1+eval(document.all.item("JIAKSL").value))*100)/100)*100)/100;
		document.all.item("BUHSDJ").value=Math.round(eval(document.all.item("JIAKJE").value)/eval(document.all.item("JIESSL").value)*10000000)/10000000;
		document.all.item("SHULZJBZ").value=eval(document.all.item("HANSDJ").value);
	}
	    	
   // if(eval(document.all.item("TIELYF").value)>0){
    	
	   	document.all.item("YUNZFHJ").value =Math.round((eval(document.all.item("TIELYF").value)+eval(document.all.item("TIELZF").value)+eval(document.all.item("KUANGQYF").value)+eval(document.all.item("KUANGQZF").value)+eval(document.all.item("BUKYQYZF").value))*100)/100;
	    document.all.item("YUNFSK").value =Math.round((Math.round((eval(document.all.item("YUNZFHJ").value)-eval(document.all.item("TIELZF").value)-eval(document.all.item("KUANGQYF").value)-eval(document.all.item("KUANGQZF").value))*eval(document.all.item("YUNFSL").value)*100)/100+eval(document.all.item("KUANGQSK").value))*100)/100;
	    document.all.item("BUHSYF").value=Math.round((eval(document.all.item("YUNZFHJ").value)-eval(document.all.item("YUNFSK").value))*100)/100;
	//}
    
    document.all.item("HEJ").value=Math.round((eval(document.all.item("YUNZFHJ").value)+eval(document.all.item("JIASJE").value))*100)/100;
    document.all.item("MEIKHJDX").value=tormb(eval(document.all.item("JIASJE").value));
    document.all.item("YUNZFHJDX").value=tormb(eval(document.all.item("YUNZFHJ").value));
	document.all.item("DAXHJ").value=tormb(eval(document.all.item("HEJ").value));
}



function ShowButtons(_type){
//_type为1时表示有数据为0时表述没有数据
if(_type==1){
}else{
}}
window.onload=function(){

     FormatInput("DIANCXXB_ID",0);
     FormatInput("MEIKXXB_ID",0);
     FormatInput("FAHDWB_ID",0);
     FormatInput("CHEZXXB_ID",0);
     FormatInput("MEIJB_ID",0);
     FormatInput("CHES",0);
     FormatInput("HANSDJ",4);
     FormatInput("JINGZ",2);
     FormatInput("SHULZJBZ",4);
     FormatInput("SHULZJJE",2);
     FormatInput("GONGFSL",2);
     FormatInput("YANSSL",2);
     FormatInput("YINGK",2);
     FormatInput("JIESSL",2);
     FormatInput("BUHSDJ",7);
     FormatInput("JIAKJE",2);
     FormatInput("BUKYQJK",2);
     FormatInput("JIAKHJ",2);
//     FormatInput("JIAKSL",2);
     FormatInput("JIAKSK",2);
     FormatInput("JIASJE",2);
     FormatInput("TIELYF",2);
     FormatInput("ZAF",2);
     FormatInput("BUKYQYZF",2);
     FormatInput("JISKC",2);
     FormatInput("BUHSYF",2);
//     FormatInput("YUNFSL",2);
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
     
     FormatInput("JUFYF",2);
     FormatInput("JUFZF",2);
     
     FormatInput("HETSL",2);
     FormatInput("JIESSLJS",2);
	 document.all.item("JIESSLJS").value=document.all.item("JIESSL").value;
     document.all.item("GONGFL").value=document.all.item("GONGFSL").value;
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
		alert("收款单位为空，请核对！");
		return false;
	}else if(document.all.item("DAIBCC").value==""){
		alert("代表车号为空，请核对！");
		return false;
	}else if(document.all.item("JIESBH").value==""){
		alert("结算编号为空，请核对！");
		return false;
	}else if(document.all.item("RANLBMJBR").value==""){
		alert("燃料部门经办人为空，请核对！");
		return false;
	}else if(document.all.item("YUANSHR").value=="" || document.all.item("YUANSHR").value=="请选择"){
		alert("原收货人为空，请核对！");
		return false;
	}else if(document.all.item("PINZ").value==""||document.all.item("PINZ").value=="请选择"){
		alert("品种为空，请核对！");
		return false;
	}else if(document.all.item("Fahdw").value==""||document.all.item("Fahdw").value=="请选择"){
		alert("发货单位为空，请核对！");
		return false;
	}else if(document.all.item("Faz").value==""||document.all.item("Faz").value=="请选择"){
		alert("发站为空，请核对！");
		return false;
	}else if(document.all.item("FAHKSRQ").value==""||document.all.item("FAHJZRQ").value==""){
		alert("发货日期为空，请核对！");
		return false;
	}else if(document.all.item("YANSKSRQ").value==""||document.all.item("YANSJZRQ").value==""){
		alert("验收日期为空，请核对！");
		return false;
	};
	else{
		var str=document.all.item("FAPBH").value;
		var s=str.replace(/[^\x00-\xff]/g,"aa");
		if(s.length>20){
			alert("发票编号过长，请核对！");
			return false;
		}
		if(document.all.item("JINGZ").value<=0||document.all.item("JIESSL").value<=0||document.all.item("GONGFSL").value<=0){
			alert("结算数量,供方数量，过衡重量，含税单价信息小于或等于零，请核对！");
			return false;
		}			
		return true;
	}
}
function checkHetbh(){
	
	if(HTBHD.getRawValue()!="请选择"){
		
		Ext.get("addbt").setVisible(false);
    	Ext.get("quxbt").setVisible(true);
		//document.all.item("AddButton").style.display="none";
		//document.all.item("QuxButton").style.display="";
		SetDisabled('SelectData',false);
		return true;
	}else{
		alert("请选择购销合同！");
		return false;
	}
}

function Pingb(){
	if(event.keyCode==8 && event.srcElement.type!='text'){
		event.keyCode==0;
		event.returnValue = false;
	}
}

function Lcsubmit(){
	
	document.all.item("shezhi").style.display="";
}

function checkSubmit(){
	
	if(cbo_LIUCMCSelect.getRawValue()!="请选择"){
		
		document.all.item("shezhi").style.display="none";
		
		if(check()){
			
			return true;
		}
		
	}else{
		alert("请选择流程名称！");
		return false;
	}
}

function HideTiaoj(){
	
	document.all.item("shezhi").style.display="none";
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

function Deletcheck(){
	
	if(document.getElementById("DAIBCC").value!=""){
		
		var strtmp="确定要删除该结算单吗？";
		if(confirm(strtmp)){
			
			return true;
		}else{
			
			return false;
		}
	}else{
		
		alert("请选择结算单！");
		return false;
	}

}

function checkJiesbh(){
	
	if(JiesbhDrop.getRawValue()!="请选择"){
	
		return true;
	}else{
	
		alert("请选择结算编号！");
		return false;
	}
}

