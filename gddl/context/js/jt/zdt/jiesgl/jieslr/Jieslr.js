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
var oldvalue;
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
	_obj2.style.posTop=_obj1.parentElement.offsetTop+55;
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

function Deletcheck(){
	
	if(document.all.item("JIESBH").value!=""){
		var msg="确认要删除此结算单吗？";
		if(confirm(msg)){
			
			return true;
		}else{
			
			return false;
		}
		
	}else{
		alert("请先选择结算单！");
		return false;
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
//保存小数点后两位
function FormatNumber(srcStr,nAfterDot){ 
　　var srcStr,nAfterDot; 
　　var resultStr,nTen; 
　　srcStr = ""+srcStr+""; 
　　strLen = srcStr.length; 
　　dotPos = srcStr.indexOf(".",0); 
　　if (dotPos == -1){ 
　　　　//resultStr = srcStr+"."; 
　　　　//for (i=0;i<nAfterDot;i++){ 
　　　　//　　resultStr = resultStr+"0"; 
　　　　//} 
　　　　return srcStr; 
　　} 
　　else{ 
　　　　if ((strLen - dotPos - 1) >= nAfterDot){ 
　　　　　　nAfter = dotPos + nAfterDot + 1; 
　　　　　　nTen =1; 
　　　　　　for(j=0;j<nAfterDot;j++){ 
　　　　　　　　nTen = nTen*10; 
　　　　　　} 
　　　　　　resultStr = Math.round(parseFloat(srcStr)*nTen)/nTen; 
　　　　　　return resultStr; 
　　　　} 
　　　　else{ 
　　　　　　resultStr = srcStr; 
　　　　　　for (i=0;i<(nAfterDot - strLen + dotPos + 1);i++){ 
　　　　　　　　resultStr = resultStr+"0"; 
　　　　　　} 
　　　　　　return resultStr; 
　　　　} 
　　} 
}
//**********************运算公式*********************************
function Count(_int){
	if(eval(document.all.item("SHIJJSL").value)!=0||eval(document.all.item("JINGZ").value)!=0){
		document.all.item("CHAYL").value=FormatNumber((eval(document.all.item("SHIJJSL").value)-eval(document.all.item("JINGZ").value)),0);
	}
	if(eval(document.all.item("YINGFYF").value)!=0||eval(document.all.item("YINGKYF").value)!=0){
		document.all.item("HEJ").value=FormatNumber((eval(document.all.item("YINGFYF").value)-eval(document.all.item("YINGKYF").value)),2);
	}
	if(eval(document.all.item("YINGFYF_B").value)!=0||eval(document.all.item("YINGKYF_B").value)!=0){
		document.all.item("HEJ_B").value=FormatNumber((eval(document.all.item("YINGFYF_B").value)-eval(document.all.item("YINGKYF_B").value)),2);
	}
	if(eval(document.all.item("MEIJ").value)!=0&&eval(document.all.item("SHIJJSL").value)!=0||eval(document.all.item("JIAKMK").value)!=0){
		document.all.item("HANSDJ").value=FormatNumber((eval(document.all.item("MEIJ").value)*eval(document.all.item("SHIJJSL").value)
		                             +eval(document.all.item("JIAKMK").value))/eval(document.all.item("SHIJJSL").value),2);
	}
	if(eval(document.all.item("SHUIL").value)!=0&&eval(document.all.item("MEIJ").value)!=0&&eval(document.all.item("SHIJJSL").value)!=0&&eval(document.all.item("JIAKMK").value)!=0){
//		document.all.item("BUHSDJ").value=FormatNumber((eval(document.all.item("HANSDJ").value)/(eval(document.all.item("SHUIL").value)+1)),7);
		document.all.item("BUHSDJ").value=FormatNumber(
			(((eval(document.all.item("MEIJ").value)*eval(document.all.item("SHIJJSL").value)+eval(document.all.item("JIAKMK").value))/(eval(document.all.item("SHUIL").value)+1))
			 /eval(document.all.item("SHIJJSL").value)),7);
	}
	if(eval(document.all.item("BUHSDJ").value)!=0||eval(document.all.item("SHIJJSL").value)!=0){
		document.all.item("BUHSMK").value=FormatNumber((eval(document.all.item("BUHSDJ").value)*eval(document.all.item("SHIJJSL").value)),2);
	}
	if(eval(document.all.item("BUHSMK").value)!=0||document.all.item("SHUIL").value!=""){
		document.all.item("SHUIJ").value=FormatNumber((eval(document.all.item("BUHSMK").value)*eval(document.all.item("SHUIL").value)),2)
	}
	if(eval(document.all.item("BUHSMK").value)!=0||eval(document.all.item("SHUIJ").value)!=0){
//		document.all.item("JIASHJ").value=FormatNumber((eval(document.all.item("BUHSMK").value)+eval(document.all.item("SHUIJ").value)),2);
		document.all.item("JIASHJ").value=eval(FormatNumber(
		(eval(document.all.item("MEIJ").value)*eval(document.all.item("SHIJJSL").value))
		,2)||0)+eval(document.all.item("JIAKMK").value||0);
	}
	
	if(_int=='3'){
		if(eval(document.all.item("HEJ").value)!=0||eval(document.all.item("YUNFSL").value)!=0||eval(document.all.item("HANSYF_K").value)!=0||eval(document.all.item("HANSYF_Q").value)!=0){
		document.all.item("YUNFSJ").value=FormatNumber((eval(document.all.item("HEJ").value)*eval(document.all.item("YUNFSL").value)
									 +eval(document.all.item("HANSYF_K").value)*eval(document.all.item("YUNFSL").value)
									 +eval(document.all.item("HANSYF_Q").value)*eval(document.all.item("YUNFSL").value)),2);
		}
	}
	if(eval(document.all.item("HEJ").value)!=0||eval(document.all.item("HEJ_B").value)!=0||eval(document.all.item("HANSYF_Q").value)!=0||eval(document.all.item("SHUIJ_K").value)!=0||eval(document.all.item("HANSYF_Q").value)!=0||eval(document.all.item("QITYZF").value)!=0){
			document.all.item("YUNFHJ").value=FormatNumber((eval(document.all.item("HEJ").value)+eval(document.all.item("HEJ_B").value)+eval(document.all.item("HANSYF_K").value)
										+eval(document.all.item("SHUIJ_K").value)+eval(document.all.item("HANSYF_Q").value)+eval(document.all.item("QITYZF").value)),2);
	}
	if(_int=='2'){
	
		if(eval(document.all.item("YUNFHJ").value)!=0||eval(document.all.item("YUNFSJ").value)!=0){
			document.all.item("YUNF").value=FormatNumber((eval(document.all.item("YUNFHJ").value)-eval(document.all.item("YUNFSJ").value)),2);
		}
	}
	if(eval(document.all.item("YUNFHJ").value)!=0||eval(document.all.item("JIASHJ").value)!=0){
		document.all.item("SHIFZJEXX").value=FormatNumber((eval(document.all.item("YUNFHJ").value)+eval(document.all.item("JIASHJ").value)),2);
		document.all.item("SHIFZJEDX").value=tormb(eval(document.all.item("SHIFZJEXX").value));
	}
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



function Pingb(){
	if(event.keyCode==8 && event.srcElement.type!='text'){
		event.keyCode==0;
		event.returnValue = false;
	}
}

function MatchNum(obj1,str){
	var Numr, Numre;
	var Nums = obj1.value;
	
	//alert("Nums="+Nums);
	Nums = Nums.replace(/(^\s*)|(\s*$)/g, "");
	
	if(Nums==''){
		obj1.value = oldvalue;
	}
	if(Nums.substring(0,1)=='.'){			//将首字符为'.'的数值替换为'0.'
		Nums= 0 + Nums;	
	}
	Numre = str;  							// 创建正则表达式对象。
	Numr = Nums.match(Numre);               // 在字符串 s 中查找匹配。
	if(Nums!= Numr){						// 如果不等
		alert("数值超出合理范围！");
		obj1.value = oldvalue;
		obj1.focus();						// 输入框取得焦点
		return false;
	}else{									// 否则判断是否超过上下限						
		obj1.value = Nums;						// 如果没超过上下限，将改正的数值返回到原输入框
		oldvalue = Nums;
		return true;
	}
}





