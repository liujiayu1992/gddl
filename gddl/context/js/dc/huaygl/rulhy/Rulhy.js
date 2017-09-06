var cal;
var calenDate;
var oldDate ;
window.onresize=function(){

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
		//calenDate.value  = cal.formatDate();
//		if(calenDate.value != cal.formatDate()){
//		var dd=calenDate.value;
		oldDate = calenDate.value;
//		calenDate.value  = cal.formatDate();
//			if(!valiDate()){
//				calenDate.value  = dd;
//				
//			}else{
//				calenDate.onchange();
//			}
//			
//		}
	if(calenDate.value == cal.formatDate()){
		return;
	}
		calenDate.value  = cal.formatDate();
		calenDate.onchange();
	}

var bodyHeight;
var bodyWidth ;
var bodyTop;
var tablemainHeight;
var tablemainWidth;

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


divdatatable.style.position="relative";
divdatatable1.style.position="relative";
divdatatable2.style.position="relative";
trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

divdatatable.style.height=(tablemainHeight-ConditionHeight-20);
divdatatable.style.width=tablemainWidth-20;

divdatatable.style.posTop=0;
divdatatable1.style.posTop=0;
divdatatable2.style.posTop=0;

//divdatatable1.style.height=Math.round((tablemainHeight-ConditionHeight)/3*2)/2-10+500;
divdatatable1.style.height=(tablemainHeight-ConditionHeight)-35;
divdatatable1.style.width=107;

//divdatatable2.style.height=Math.round((tablemainHeight-ConditionHeight)/3*2)/2-10+500;
divdatatable2.style.height=(tablemainHeight-ConditionHeight)-35;
divdatatable2.style.width=tablemainWidth-125;



if(demonstration == 0){
	 	document.all.item("HH",0).style.display="none";
	 	document.all.item("SS",0).style.display="none";
	 	document.all.item("SZ",0).style.display="none";
	 	document.all.item("HZ",0).style.display="none";
	 	document.all.item("ml",0).style.borderTop="gray 1px solid";
	 	document.all.item("fenx",0).style.borderBottom="0px solid";

	}

	SetEditMeiyzl();

}

function PressAnyKey(){
}

function HandleOnfocusEvent(obj,eventName,objEdit){
	var objEditable ;
	objEditable = document.getElementById("EditMeiyzl");
	var val;
	val = objEditable.value;
	if(val=="y"){
		editin(obj,eventName);
		editclick(obj,objEdit);
	}
}
function HandleOnclickEvent(obj,eventName,objEdit){
	var objEditable ;
	objEditable = document.getElementById("EditMeiyzl");
	var val;
	val = objEditable.value;
	if(val=="y"){
		editin(obj,eventName);
		editclick(obj,objEdit);
		insert();
	}else{
		//editin(obj,eventName);
	}
	
}
function HandleOnchangeEvent(){
	var objEditable ;
	objEditable = document.getElementById("EditMeiyzl");
	var val;
	val = objEditable.value;
	if(val=="y"){
		colsCount();
	}else{

	}
}
function SetEditMeiyzl(){
	var obj ;
	obj = document.getElementById("EditMeiyzl");
	var val;
	val = obj.value;
	var isReadOnly = true;

	if(val=="y"){
		obj = document.getElementById("MEIYZL");
		obj.readOnly = isReadOnly;
		obj.style.textAlign="center";
		obj.parentElement.className="";
		obj = document.getElementById("MEIYZL$0");
		obj.readOnly = isReadOnly;
		obj.style.textAlign="center";
		obj.parentElement.className="";
		obj = document.getElementById("MEIYZL$1");
		obj.readOnly = isReadOnly; 
		obj.style.textAlign="center";
		obj.parentElement.className="";
		obj = document.getElementById("MEIYZL$2");
		obj.readOnly = isReadOnly; 
		obj.style.textAlign="center";
		obj.parentElement.className="";
		obj = document.getElementById("MEIYZL$3");
		obj.readOnly = isReadOnly; 
		obj.style.textAlign="center";
		obj.parentElement.className="";
		obj = document.getElementById("MEIYZL$4");
		obj.readOnly = isReadOnly; 
		obj.style.textAlign="center";
		obj.parentElement.className="";
		obj = document.getElementById("MEIYZL$5");
		obj.readOnly = isReadOnly; 
		obj.style.textAlign="center";
		obj.parentElement.className="";
		obj = document.getElementById("MEIYZL$6");
		obj.readOnly = isReadOnly; 
		obj.style.textAlign="center";
		obj.parentElement.className="";
		return;
	}
}

function setColor(){
	obj = document.getElementById("shenhzt");
	if(obj.value=="1"){
		obj = document.getElementById("tittleHeadRiq");
		obj.className="text-position1";
	}else{
		obj = document.getElementById("tittleHeadRiq");
		obj.className="text-position";
	}
}

function showSubmitBtn(){
	var obj = document.getElementById("ShujztDropDownid");
	if(obj.options(obj.selectedIndex).text=="未提交"){
		obj = document.getElementById("SubmitButton");
		obj.style.display = "";
		return ;
	}
	
	obj = document.getElementById("SubmitButton");
	obj.style.display = "none";
}
body.clientHeight

var oldClickrow=-1;
var oldinput;
var objselectCol;
var oldSelectobj;

function droponchange(_int){
	if(objselectCol!=null){
		objselectCol.value =oldSelectobj.options(oldSelectobj.selectedIndex).text;
		if (_int==1){
			updatarows(objselectCol.name);
		}
		oldSelectobj.selectedIndex=0;
		oldSelectobj.style.display="none";
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
	_obj2.style.posLeft =_obj1.offsetLeft+118;
	
	_obj2.style.posTop=_obj1.offsetTop +2;
	_obj2.style.posWidth=_obj1.offsetWidth +1;
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

//function editin(_str1,_str2)
//{	
//	oldinput =_str1;	
//}

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
		_str1.className="edittableTrOn";
	}

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
function editZ(){
 	if(event.keyCode==13){
 		document.all.item("DANTRL").select();
 		return false;
 		
 	}
}
function editA(){
 	if(event.keyCode==13){
 		document.all.item("QIMBH",0).select();
 		return false;
 		
 	}
}

function valiDate(){
	if(document.all.item("FENXRQ").value<document.all.item("riqDateSelect").value){
		alert("分析日期不能早于入炉日期!");
		return false;
	}else{
		return true;
	}
}


function colsCount(){
	    var l;
	    if(huayzws == 7){
	    	l = 10000;
	    }else{
	    	if(huayzws==6){
	    		l=10000;
	    	}else if(huayzws==5){
	    		l=1000;
	   	 }
	    }
	    var objEditable ;
		objEditable = document.getElementById("EditMeiyzl");
		var val;
		val = objEditable.value;
	
	    for(i=0;i<2;i++){
	    	if ((document.all.item("QIMZL",i).value).length<huayzws) {
	    		var v =eval(document.all.item("QIMZL",i).value)*Math.pow(10,(huayzws-(document.all.item("QIMZL",i).value).length));
	    		document.all.item("QIMZL",i).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMZL",i).value).length>huayzws){
	    		
	    		}else{
		    		if (document.all.item("QIMZL",i).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMZL",i).value=Format(eval(document.all.item("QIMZL",i).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	
	    	// --------------------
	    	if(val=="y"){
	    		if ((document.all.item("MEIYZL",i).value).length<huayzws) {
		    		var v =eval(document.all.item("MEIYZL",i).value)*Math.pow(10,(huayzws - (document.all.item("MEIYZL",i).value).length));
		    		document.all.item("MEIYZL",i).value=Format(eval(v/l),huayxsws);
			    	// if(i==0){
			    		// alert("document.all.item(MEIYZL,i).value = " +
						// document.all.item("MEIYZL",i).value);
			    	// }
		    	}else{
		    		if((document.all.item("MEIYZL",i).value).length>huayzws){
		    		
		    		}else{
			    		if (document.all.item("MEIYZL",i).value.toString().split(".").length==2){
			    				
			    		}else{
			    			/*
							 * if(i==0){
							 * alert("document.all.item(MEIYZL,i).value = " +
							 * document.all.item("MEIYZL",i).value); alert("l = " +
							 * l + ";huayxsws = " + huayxsws); alert("eval " +
							 * eval(document.all.item("MEIYZL",i).value));
							 * alert("eval1 " +
							 * eval(document.all.item("MEIYZL",i).value/1.0));//怪！这样写就正确了
							 * alert("number " +
							 * Number(document.all.item("MEIYZL",i).value));
							 * alert("eval String " +
							 * eval(document.all.item("MEIYZL",i).value.toString()));
							 * alert("before format " +
							 * eval(document.all.item("MEIYZL",i).value)/l);
							 * alert("after format " +
							 * Format(eval(document.all.item("MEIYZL",i).value)/l,huayxsws)); }
							 */
			    			document.all.item("MEIYZL",i).value=Format(eval(document.all.item("MEIYZL",i).value/l),huayxsws);
			    		}
			    	}
		    	}
		    	
		    	document.all.item("QIMMYZL",i).value=  Format(Round_new((eval(document.all.item("MEIYZL",i).value) + eval(document.all.item("QIMZL",i).value)),4),huayxsws);
	    	}
	    	// --------------------
	    	
	    	if ((document.all.item("QIMMYZL",i).value).length<huayzws && document.all.item("QIMMYZL",i).value.toString().split(".").length==1) {
	    		var v=eval(document.all.item("QIMMYZL",i).value)*Math.pow(10,(huayzws-(document.all.item("QIMMYZL",i).value).length));
	    		document.all.item("QIMMYZL",i).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMMYZL",i).value).length>huayzws){
	    			
	    		}else{
		    		if (document.all.item("QIMMYZL",i).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMMYZL",i).value=Format(eval(document.all.item("QIMMYZL",i).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	
	    	if ((document.all.item("HONGHZL1",i).value).length<huayzws) {
	    		var v=eval(document.all.item("HONGHZL1",i).value)*Math.pow(10,(huayzws-(document.all.item("HONGHZL1",i).value).length));
	    		document.all.item("HONGHZL1",i).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("HONGHZL1",i).value).length>huayzws){
	    		
		    	}else{
				    if (document.all.item("HONGHZL1",i).value.toString().split(".").length==2){
			    				
			    	}else{
			    		document.all.item("HONGHZL1",i).value=Format(eval(document.all.item("HONGHZL1",i).value/l),huayxsws);
			    	}
			    }
	    	}
	    	if(val!="y"){
	    		document.all.item("MEIYZL",i).value=Round_new((eval(document.all.item("QIMMYZL",i).value)-eval(document.all.item("QIMZL",i).value)),4);
	    	}
	    	
	    	
    		shiqzl=eval(document.all.item("MEIYZL",i).value)-(eval(document.all.item("HONGHZL1",i).value)-eval(document.all.item("QIMZL",i).value));
    		if(zt==0){
	    		if(document.all.item("MEIYZL",i).value==0){
	    			document.all.item("BAIFL",i).value=0;
	    		}else{
	    			document.all.item("BAIFL",i).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",i).value))*100),2);
	    			// alert(document.all.item("BAIFL",i).value);
		    	}
	    	}else{
	    		if(document.all.item("HONGHZL1",i).value!=0){
	    			document.all.item("BAIFL",i).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",i).value))*100),2);
	    		}	
	    	}
	    }
	    
	    	// Aad
			if ((document.all.item("QIMZL",2).value).length<huayzws) {
	    		document.all.item("QIMZL",2).value=eval(document.all.item("QIMZL",2).value)*Math.pow(10,(huayzws-(document.all.item("QIMZL",2).value).length));
	    		document.all.item("QIMZL",2).value=Format(eval(document.all.item("QIMZL",2).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMZL",2).value).length>huayzws){
	    		
	    		}else{
		    		if (document.all.item("QIMZL",2).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMZL",2).value=Format(eval(document.all.item("QIMZL",2).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	// --------------------
	    	if(val=="y"){
	    		if ((document.all.item("MEIYZL",2).value).length<huayzws) {
		    		document.all.item("MEIYZL",2).value=eval(document.all.item("MEIYZL",2).value)*Math.pow(10,(huayzws-(document.all.item("MEIYZL",2).value).length));
		    		document.all.item("MEIYZL",2).value=Format(eval(document.all.item("MEIYZL",2).value/l),huayxsws);
		    	}else{
		    		if((document.all.item("MEIYZL",2).value).length>huayzws){
		    		
		    		}else{
			    		if (document.all.item("MEIYZL",2).value.toString().split(".").length==2){
			    				
			    		}else{
			    			document.all.item("MEIYZL",2).value=Format(eval(document.all.item("MEIYZL",2).value/l),huayxsws);
			    		}
			    	}
		    	}
		    	document.all.item("QIMMYZL",2).value=Format(Round_new((eval(document.all.item("MEIYZL",2).value) + eval(document.all.item("QIMZL",2).value)),4),huayxsws);
	    	}
	    	// --------------------
	    	
	    	
	    	if ((document.all.item("QIMMYZL",2).value).length<huayzws) {
	    		var v =eval(document.all.item("QIMMYZL",2).value)*Math.pow(10,(huayzws-(document.all.item("QIMMYZL",2).value).length));
	    		document.all.item("QIMMYZL",2).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMMYZL",2).value).length>huayzws){
	    			
	    		}else{
		    		if (document.all.item("QIMMYZL",2).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMMYZL",2).value=Format(eval(document.all.item("QIMMYZL",2).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	
	    	if ((document.all.item("HONGHZL1",2).value).length<huayzws) {
	    		document.all.item("HONGHZL1",2).value=eval(document.all.item("HONGHZL1",2).value)*Math.pow(10,(huayzws-(document.all.item("HONGHZL1",2).value).length));
	    		document.all.item("HONGHZL1",2).value=Format(eval(document.all.item("HONGHZL1",2).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("HONGHZL1",2).value).length>huayzws){
	    		
	    		}else{
		    		if (document.all.item("HONGHZL1",2).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("HONGHZL1",2).value=Format(eval(document.all.item("HONGHZL1",2).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	
	    	if(val!="y"){
	    		document.all.item("MEIYZL",2).value=Round_new((eval(document.all.item("QIMMYZL",2).value)-eval(document.all.item("QIMZL",2).value)),4);
	    	}
    		shiqzl=(eval(document.all.item("HONGHZL1",2).value)-eval(document.all.item("QIMZL",2).value));
    		if(zt==0){
	    		if(document.all.item("MEIYZL",2).value==0){
	    			document.all.item("BAIFL",2).value=0;
	    		}else{
	    			document.all.item("BAIFL",2).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",2).value))*100),2);
	    			// alert(document.all.item("BAIFL",2).value);
		    	}
    		}else{
    			if(document.all.item("HONGHZL1",2).value!=0){
    				document.all.item("BAIFL",2).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",2).value))*100),2);
    			}
    			}
    		
	    	// Vad
	    	if ((document.all.item("QIMZL",3).value).length<huayzws) {
	    		document.all.item("QIMZL",3).value=eval(document.all.item("QIMZL",3).value)*Math.pow(10,(huayzws-(document.all.item("QIMZL",3).value).length));
	    		document.all.item("QIMZL",3).value=Format(eval(document.all.item("QIMZL",3).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMZL",3).value).length>huayzws){
	    		
	    		}else{
		    		if (document.all.item("QIMZL",3).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMZL",3).value=Format(eval(document.all.item("QIMZL",3).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	// --------------------
	    	if(val=="y"){
	    		if ((document.all.item("MEIYZL",3).value).length<huayzws) {
		    		document.all.item("MEIYZL",3).value=eval(document.all.item("MEIYZL",3).value)*Math.pow(10,(huayzws-(document.all.item("MEIYZL",3).value).length));
		    		document.all.item("MEIYZL",3).value=Format(eval(document.all.item("MEIYZL",3).value/l),huayxsws);
		    	}else{
		    		if((document.all.item("MEIYZL",3).value).length>huayzws){
		    		
		    		}else{
			    		if (document.all.item("MEIYZL",3).value.toString().split(".").length==2){
			    				
			    		}else{
			    			document.all.item("MEIYZL",3).value=Format(eval(document.all.item("MEIYZL",3).value/l),huayxsws);
			    		}
			    	}
		    	}
		    	document.all.item("QIMMYZL",3).value=Format(Round_new((eval(document.all.item("MEIYZL",3).value) + eval(document.all.item("QIMZL",3).value)),4),huayxsws);
	    	}
	    	// --------------------
	    	
	    	if ((document.all.item("QIMMYZL",3).value).length<huayzws) {
	    		var v=eval(document.all.item("QIMMYZL",3).value)*Math.pow(10,(huayzws-(document.all.item("QIMMYZL",3).value).length));
	    		document.all.item("QIMMYZL",3).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMMYZL",3).value).length>huayzws){
	    			
	    		}else{
		    		if (document.all.item("QIMMYZL",3).value.toString().split(".").length==2){
		    				
		    		}else{
		    				document.all.item("QIMMYZL",3).value=Format(eval(document.all.item("QIMMYZL",3).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	
	    	if ((document.all.item("HONGHZL1",3).value).length<huayzws) {
	    		document.all.item("HONGHZL1",3).value=eval(document.all.item("HONGHZL1",3).value)*Math.pow(10,(huayzws-(document.all.item("HONGHZL1",3).value).length));
	    		document.all.item("HONGHZL1",3).value=Format(eval(document.all.item("HONGHZL1",3).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("HONGHZL1",3).value).length>huayzws){
		    	}else{
			    	if (document.all.item("HONGHZL1",3).value.toString().split(".").length==2){
			    				
			    	}else{
			    		document.all.item("HONGHZL1",3).value=Format(eval(document.all.item("HONGHZL1",3).value/l),huayxsws);
			    	}
			   	}
	    	}
	    	if(val!="y"){
	    		document.all.item("MEIYZL",3).value=Format(Round_new((eval(document.all.item("QIMMYZL",3).value)-eval(document.all.item("QIMZL",3).value)),4),huayxsws);
	    	}
    		shiqzl=eval(document.all.item("MEIYZL",3).value)-(eval(document.all.item("HONGHZL1",3).value)-eval(document.all.item("QIMZL",3).value));
    		
    		if(zt==0){
	    		if(document.all.item("MEIYZL",3).value==0){
	    			document.all.item("BAIFL",3).value=0;
	    		}else{
	    			document.all.item("BAIFL",3).value=eval(Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",3).value))*100- eval(document.all.item("BAIFL",1).value)),2));
	    			// alert(document.all.item("BAIFL",3).value);
		    	}
	    	}else{
	    		if(document.all.item("HONGHZL1",3).value!=0){
	    			document.all.item("BAIFL",3).value=eval(Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",3).value))*100- eval(document.all.item("BAIFL",1).value)),2));
	    		}
	    	}
	    	
	    	
	    for(i=4;i<6;i++){
	 		// --------------------
	 		if ((document.all.item("QIMZL",i).value).length<huayzws) {
	    		document.all.item("QIMZL",i).value=eval(document.all.item("QIMZL",i).value)*Math.pow(10,(huayzws-(document.all.item("QIMZL",i).value).length));
	    		document.all.item("QIMZL",i).value=Format(eval(document.all.item("QIMZL",i).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMZL",i).value).length>huayzws){
	    		
	    		}else{
		    		if (document.all.item("QIMZL",i).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMZL",i).value=Format(eval(document.all.item("QIMZL",i).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	if(val=="y"){
	    		if ((document.all.item("MEIYZL",i).value).length<huayzws) {
		    		document.all.item("MEIYZL",i).value=eval(document.all.item("MEIYZL",i).value)*Math.pow(10,(huayzws-(document.all.item("MEIYZL",i).value).length));
		    		document.all.item("MEIYZL",i).value=Format(eval(document.all.item("MEIYZL",i).value/l),huayxsws);
		    	}else{
		    		if((document.all.item("MEIYZL",i).value).length>huayzws){
		    		
		    		}else{
			    		if (document.all.item("MEIYZL",i).value.toString().split(".").length==2){
			    				
			    		}else{
			    			document.all.item("MEIYZL",i).value=Format(eval(document.all.item("MEIYZL",i).value/l),huayxsws);
			    		}
			    	}
		    	}
		    	document.all.item("QIMMYZL",i).value=Format(Round_new((eval(document.all.item("MEIYZL",i).value) + eval(document.all.item("QIMZL",i).value)),4),huayxsws);
	    	}
	    	// --------------------
	    	if ((document.all.item("QIMMYZL",i).value).length<huayzws) {
	    		var v =eval(document.all.item("QIMMYZL",i).value)*Math.pow(10,(huayzws-(document.all.item("QIMMYZL",i).value).length));
	    		document.all.item("QIMMYZL",i).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMMYZL",i).value).length>huayzws){
	    			
	    		}else{
	    			if (document.all.item("QIMMYZL",i).value.toString().split(".").length==2){
		    				
		    		}else{
	    				document.all.item("QIMMYZL",i).value=Format(eval(document.all.item("QIMMYZL",i).value/l),huayxsws);
	    			}
	    		}
	    	}
	    	
	    	if ((document.all.item("HONGHZL1",i).value).length<huayzws) {
	    		document.all.item("HONGHZL1",i).value=eval(document.all.item("HONGHZL1",i).value)*Math.pow(10,(huayzws-(document.all.item("HONGHZL1",i).value).length));
	    		document.all.item("HONGHZL1",i).value=Format(eval(document.all.item("HONGHZL1",i).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("HONGHZL1",i).value).length>huayzws){
		    	}else{
			   		if (document.all.item("HONGHZL1",i).value.toString().split(".").length==2){
			   				
			   		}else{
			   				document.all.item("HONGHZL1",i).value=Format(eval(document.all.item("HONGHZL1",i).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	if(val!="y"){
	    		document.all.item("MEIYZL",i).value=Format(Round_new((eval(document.all.item("QIMMYZL",i).value)-eval(document.all.item("QIMZL",i).value)),4),huayxsws);
	    	}
    		shiqzl=eval(document.all.item("MEIYZL",i).value)-(eval(document.all.item("HONGHZL1",i).value)-eval(document.all.item("QIMZL",i).value));
    		if(zt==0){
	    		if(document.all.item("MEIYZL",i).value==0){
	    			document.all.item("BAIFL",i).value=0;
	    		}else {
	    			document.all.item("BAIFL",i).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",i).value))*100),2);
		    	}
	    	}else{
	    		if(document.all.item("HONGHZL1",i).value!=0){
	    			document.all.item("BAIFL",i).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",i).value))*100),2);
	    		}
	    	}
	}
			// Aad
	    	// --------------------
	    	if ((document.all.item("QIMZL",6).value).length<huayzws) {
	    		document.all.item("QIMZL",6).value=eval(document.all.item("QIMZL",6).value)*Math.pow(10,(huayzws-(document.all.item("QIMZL",6).value).length));
	    		document.all.item("QIMZL",6).value=Format(eval(document.all.item("QIMZL",6).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMZL",6).value).length>huayzws){
	    		
	    		}else{
		    		if (document.all.item("QIMZL",6).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMZL",6).value=Format(eval(document.all.item("QIMZL",6).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	if(val=="y"){
	    		if ((document.all.item("MEIYZL",6).value).length<huayzws) {
		    		document.all.item("MEIYZL",6).value=eval(document.all.item("MEIYZL",6).value)*Math.pow(10,(huayzws-(document.all.item("MEIYZL",6).value).length));
		    		document.all.item("MEIYZL",6).value=Format(eval(document.all.item("MEIYZL",6).value/l),huayxsws);
		    	}else{
		    		if((document.all.item("MEIYZL",6).value).length>huayzws){
		    		
		    		}else{
			    		if (document.all.item("MEIYZL",6).value.toString().split(".").length==2){
			    				
			    		}else{
			    			document.all.item("MEIYZL",6).value=Format(eval(document.all.item("MEIYZL",6).value/l),huayxsws);
			    		}
			    	}
		    	}
		    	document.all.item("QIMMYZL",6).value=Format(Round_new((eval(document.all.item("MEIYZL",6).value) + eval(document.all.item("QIMZL",6).value)),4),huayxsws);
	    	}
	    	// --------------------
	    	if ((document.all.item("QIMMYZL",6).value).length<huayzws) {
	    		var v =eval(document.all.item("QIMMYZL",6).value)*Math.pow(10,(huayzws-(document.all.item("QIMMYZL",6).value).length));
	    		document.all.item("QIMMYZL",6).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMMYZL",6).value).length>huayzws){
	    			
	    		}else{
		    		if (document.all.item("QIMMYZL",6).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMMYZL",6).value=Format(eval(document.all.item("QIMMYZL",6).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	
	    	if ((document.all.item("HONGHZL1",6).value).length<huayzws) {
	    		document.all.item("HONGHZL1",6).value=eval(document.all.item("HONGHZL1",6).value)*Math.pow(10,(huayzws-(document.all.item("HONGHZL1",6).value).length));
	    		document.all.item("HONGHZL1",6).value=Format(eval(document.all.item("HONGHZL1",6).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("HONGHZL1",6).value).length>huayzws){
	    		}else{
			   		if (document.all.item("HONGHZL1",6).value.toString().split(".").length==2){
			   				
		   			}else{
		   				document.all.item("HONGHZL1",6).value=Format(eval(document.all.item("HONGHZL1",6).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	if(val!="y"){
	    		document.all.item("MEIYZL",6).value=Format(Round_new((eval(document.all.item("QIMMYZL",6).value)-eval(document.all.item("QIMZL",6).value)),4),huayxsws);
	    	}
    		shiqzl=(eval(document.all.item("HONGHZL1",6).value)-eval(document.all.item("QIMZL",6).value));
    		if(zt==0){
	    		if(document.all.item("MEIYZL",6).value==0){
	    			document.all.item("BAIFL",6).value=0;
	    		}else{
	    			document.all.item("BAIFL",6).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",6).value))*100),2);
		    	}
	    	}else{
	    		if(document.all.item("HONGHZL1",6).value!=0){
	    			document.all.item("BAIFL",6).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",6).value))*100),2);
	    		}
	    	}
	    	
	    	// Vad
	    	if ((document.all.item("QIMZL",7).value).length<huayzws) {
	    		document.all.item("QIMZL",7).value=eval(document.all.item("QIMZL",7).value)*Math.pow(10,(huayzws-(document.all.item("QIMZL",7).value).length));
	    		document.all.item("QIMZL",7).value=Format(eval(document.all.item("QIMZL",7).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMZL",7).value).length>huayzws){
	    		
	    		}else{
		    		if (document.all.item("QIMZL",7).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMZL",7).value=Format(eval(document.all.item("QIMZL",7).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	// --------------------
	    	if(val=="y"){
	    		if ((document.all.item("MEIYZL",7).value).length<huayzws) {
		    		document.all.item("MEIYZL",7).value=eval(document.all.item("MEIYZL",7).value)*Math.pow(10,(huayzws-(document.all.item("MEIYZL",7).value).length));
		    		document.all.item("MEIYZL",7).value=Format(eval(document.all.item("MEIYZL",7).value/l),huayxsws);
		    	}else{
		    		if((document.all.item("MEIYZL",7).value).length>huayzws){
		    		
		    		}else{
			    		if (document.all.item("MEIYZL",7).value.toString().split(".").length==2){
			    				
			    		}else{
			    			document.all.item("MEIYZL",7).value=Format(eval(document.all.item("MEIYZL",7).value/l),huayxsws);
			    		}
			    	}
		    	}
		    	document.all.item("QIMMYZL",7).value=Format(Round_new((eval(document.all.item("MEIYZL",7).value) + eval(document.all.item("QIMZL",7).value)),4),huayxsws);
	    	}
	    	// --------------------
			if ((document.all.item("QIMMYZL",7).value).length<huayzws) {
	    		var v=eval(document.all.item("QIMMYZL",7).value)*Math.pow(10,(huayzws-(document.all.item("QIMMYZL",7).value).length));
	    		document.all.item("QIMMYZL",7).value=Format(eval(v/l),huayxsws);
	    	}else{
	    		if((document.all.item("QIMMYZL",7).value).length>huayzws){
	    			
	    		}else{
		    		if (document.all.item("QIMMYZL",7).value.toString().split(".").length==2){
		    				
		    		}else{
		    			document.all.item("QIMMYZL",7).value=Format(eval(document.all.item("QIMMYZL",7).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	
	    	if ((document.all.item("HONGHZL1",7).value).length<huayzws) {
	    		document.all.item("HONGHZL1",7).value=eval(document.all.item("HONGHZL1",7).value)*Math.pow(10,(huayzws-(document.all.item("HONGHZL1",7).value).length));
	    		document.all.item("HONGHZL1",7).value=Format(eval(document.all.item("HONGHZL1",7).value/l),huayxsws);
	    	}else{
	    		if((document.all.item("HONGHZL1",7).value).length>huayzws){
	    		}else{
		    		if (document.all.item("HONGHZL1",7).value.toString().split(".").length==2){
		    				
		    		}else{
		    				document.all.item("HONGHZL1",7).value=Format(eval(document.all.item("HONGHZL1",7).value/l),huayxsws);
		    		}
		    	}
	    	}
	    	if(val!="y"){
				document.all.item("MEIYZL",7).value=Round_new((eval(document.all.item("QIMMYZL",7).value)-eval(document.all.item("QIMZL",7).value)),4);
	    	}
    		shiqzl=eval(document.all.item("MEIYZL",7).value)-(eval(document.all.item("HONGHZL1",7).value)-eval(document.all.item("QIMZL",7).value));
    		if(zt==0){
	    		if(document.all.item("MEIYZL",7).value==0){
	    			document.all.item("BAIFL",7).value=0;
	    		}else{
	    			document.all.item("BAIFL",7).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",7).value))*100- eval(document.all.item("BAIFL",5).value)),2);
		    	}
	    	}else{
	    		if(document.all.item("HONGHZL1",7).value!=0){
	    			document.all.item("BAIFL",7).value=Round_new(((eval(shiqzl)/eval(document.all.item("MEIYZL",7).value))*100- eval(document.all.item("BAIFL",5).value)),2);
	    		}
	    	}
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
//?????????????????????????????????????????????????
//??
//1.???????????5??1??24.236--->24.24,??5?????23.234--->23.23.
//2.???????????5??5?????????0???1??23.2251--->23.23
//3.???????????5??5????????0???5??????????1????23.235--->23.24;
//  ?5???????????23.225--->23.22
function Round_new(value,_bit){
	
	var value1;//???????????5??5?????
	var dbla;
	value1=Math.floor(value*Math.pow(10,_bit))-Math.floor(value*Math.pow(10,_bit-1))*10;
	dbla=Math.round(value * Math.pow(10, _bit)*10000000)/10000000;
	if ((dbla-Math.floor(value*Math.pow(10,_bit)))>=0.5 && (dbla-Math.floor(value*Math.pow(10,_bit)))<0.6){
		if ((dbla-Math.floor(value*Math.pow(10,_bit)))==0.5){
			if(value1==0 || value1==2 || value1==4 || value1==6 || value1==8 ){
				return Math.floor(value*Math.pow(10,_bit))/Math.pow(10,_bit);
			}else{
				return (Math.floor(value*Math.pow(10,_bit))+1)/Math.pow(10,_bit);
			}
		}else{
			return Math.round(value * Math.pow (10,_bit))/ Math.pow(10,_bit);
		}
	}else{
		return Math.round(value * Math.pow (10,_bit))/ Math.pow(10,_bit);
	}
	
}
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
			cal.show(calenDate);
		}
	}
}


/*
function editclick(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	//alert(_obj1.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.offsetLeft);
	//alert(_obj1.parentElement.parentElement.parentElement.parentElement.parentElement.offsetLeft);

	//alert(_obj1.offsetLeft);
	_obj2.style.posLeft =divdatatable2.offsetLeft +_obj1.offsetLeft;

	_obj2.style.posTop= divdatatable2.offsetTop +_obj1.parentElement.offsetTop + 3 ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +2;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight +1 ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
	
}
*/

function editclick8(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =divdatatable2.offsetLeft +_obj1.offsetLeft;

	_obj2.style.posTop= _obj1.parentElement.offsetTop - divdatatable2.scrollTop + 8 ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth+1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight; 
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
		if(objselectCol.name=='BAIFLedit$0'){
		document.all.item('BAIFL',3).value=Round_new(100*(document.all.item('QIMMYZL',3).value-document.all.item('HONGHZL1',3).value)/document.all.item('MEIYZL',3).value-document.all.item('BAIFL',1).value,2);
	}
	}
	//colsCount();
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
	if (_str2=="onclick"){
			if (oldSelectobj!=null){oldSelectobj.style.display="none";}
		}
	oldinput =_str1;	
}

function editclick(_obj1,_obj2){
	var objEditable ;
	objEditable = document.getElementById("EditMeiyzl");
	var val;
	val = objEditable.value;
	if(val=="y"){
		if(_obj1.id =="QIMMYZL"){
			return;
		}
	}
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	//alert(_obj1.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.offsetLeft);
	//alert(_obj1.parentElement.parentElement.parentElement.parentElement.parentElement.offsetLeft);

	//alert(_obj1.offsetLeft);
	_obj2.style.posLeft =divdatatable2.offsetLeft +_obj1.offsetLeft;

	_obj2.style.posTop= divdatatable2.offsetTop +_obj1.parentElement.offsetTop +73  ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +2;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight +1 ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}

function editclick1(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =divdatatable2.offsetLeft +_obj1.offsetLeft;

	_obj2.style.posTop= divdatatable2.offsetTop +_obj1.parentElement.offsetTop + 2 ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight +1 ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
	
}

function editclick2(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =divdatatable2.offsetLeft +_obj1.offsetLeft;

	_obj2.style.posTop= divdatatable2.offsetTop +_obj1.parentElement.offsetTop + 2 ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight +1 ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
	
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

function LikeEnter(){
	var objEditable ;
	objEditable = document.getElementById("EditMeiyzl");
	var val;
	val = objEditable.value;
	if(objselectCol.id!='BAIFL'){
	var n;
	var row;
	var str = objselectCol.name;
	n=str.indexOf("$");
	if(n!=-1){
		row =eval(str.substring(n+1))+1;
	}else{
		row =0;
	}
	if(objselectCol.id=='QIMZL'){
		oldv = oldSelectobj.value;
		if((oldv.toString().split(".").length==1 && oldv.length==huayzws)||(oldv.toString().split(".").length==2 && oldv.length==huayzws+1)){
			objselectCol.value = oldSelectobj.value;
			if(val=="y"){
					document.all.item('MEIYZL',row).focus();
				}else{
					document.all.item('QIMMYZL',row).focus();
				}
		}
		if(oldv.length>huayzws){
			if (oldv.toString().split(".").length==2){
				oldSelectobj.value=oldSelectobj.value.toString().substring(0,9);
  				objselectCol.value =oldSelectobj.value.toString().substring(0,9);
  				//alert(objselectCol.value);
			}else{
				oldSelectobj.value=oldSelectobj.value.toString().substring(0,8);
  				objselectCol.value =oldSelectobj.value.toString().substring(0,8);
  				//alert(objselectCol.value);
			}
		}
		
	}else if(objselectCol.id=='QIMMYZL'){
		oldv = oldSelectobj.value;
		if((oldv.toString().split(".").length==1 && oldv.length==huayzws)||(oldv.toString().split(".").length==2 && oldv.length==huayzws+1)){
			objselectCol.value = oldSelectobj.value;
			if(val!="y"){
					document.all.item('HONGHZL1',row).focus();
				}else{
					document.all.item('MEIYZL',row).focus();
				}
		}
		if(oldv.length>huayzws){
			if (oldv.toString().split(".").length==2){
				oldSelectobj.value=oldSelectobj.value.toString().substring(0,9);
  				objselectCol.value =oldSelectobj.value.toString().substring(0,9);
			}else{
				oldSelectobj.value=oldSelectobj.value.toString().substring(0,8);
  				objselectCol.value =oldSelectobj.value.toString().substring(0,8);
			}
		}
	}else  if(objselectCol.id=='HONGHZL1'){
	  	oldv = oldSelectobj.value;
	  	if((oldv.toString().split(".").length==1 && oldv.length==huayzws)||(oldv.toString().split(".").length==2 && oldv.length==huayzws+1)){
	  		nextrow = row+1;
	  		objselectCol.value = oldSelectobj.value;
	  		if(nextrow <9){
		        if (nextrow==4 || nextrow==8){
		           document.all.item('DANTRL',0).focus();
		        }else{
		           document.all.item('QIMZL',nextrow).focus();
		        } 
	  		}else{
	           
	      	}
	     }
	     if(oldv.length>huayzws){
			if (oldv.toString().split(".").length==2){
				oldSelectobj.value=oldSelectobj.value.toString().substring(0,9);
  				objselectCol.value =oldSelectobj.value.toString().substring(0,9);
			}else{
				oldSelectobj.value=oldSelectobj.value.toString().substring(0,8);
  				objselectCol.value =oldSelectobj.value.toString().substring(0,8);
			}
		}
  	}else if(objselectCol.id=='DANTRL'){
  		if(oldSelectobj.value!=0){
	  		oldSelectobj.value=oldSelectobj.value.toString().substring(0,5);
	  		objselectCol.value =oldSelectobj.value.toString().substring(0,5);
  		}
  	}	
  colsCount();
	}
}
function shuax(){
	document.all.item("RefurbishButton").click();
}

function EditInKeyDown(){
	if(event.keyCode==13){
//		event.keyCode=9;
		var row;
		var str = objselectCol.name;
		n=str.indexOf("$");
		if(n!=-1){
			row =eval(str.substring(n+1))+1;
		}else{
			row =0;
		}
		if(objselectCol.id=='BAIFL'){
			if(row>6){
				document.all.item('DANTRL',0).select();
			}else{
				if(row!=3 && row <7){
					document.all.item('BAIFL',row+1).focus();
				}else{
					if(row == 3){
						if(msg()){
							document.all.item('BAIFL',row+1).focus();
						}else{
							document.all.item('DANTRL',0).select();
						}
					}
				}
			}
		}
	}
}

function msg(){
	return confirm("是否继续输入第二组数据?");
}
body.onload =function(){
     FormatInput("DANTRL",0);
     FormatInput("BAIFL",2);
     FormatInput("QIMZL",4);
     FormatInput("QIMMYZL",4);
     FormatInput("HONGHZL1",4);
}
function save(){
	if(document.all.item("FENXXM",0).value==''){
	alert("分析项目不能为空！");
	return false;
	}
	
	return valiDate();

}

function changeRulrq(objFenxrq){
	
	if(objFenxrq==null){
		return;
	}
	var Friq = objFenxrq.value.toString();
	
	var Lriq = addDays(Friq,-1,"-");
	
	var objRulrq = document.getElementById("riqDateSelect");
	objRulrq.value = Lriq;
	Form0.submit();
}


function addDays(currentDate,diff,op){
	
	var arrDate ;
	arrDate = new String(currentDate).split(op.toString());
	
	var year ;
  	year = arrDate[0];
  	
	var month ;
	
	month  = arrDate[1];
	
	var day ;
	
	day = arrDate[2];
	
	var newDate ;
	
	newDate = month + "/" + day + "/" + year ;//mm/dd/yyyy
	
	var MinMilli = 1000 * 60;       // 初始化变量。
	
	var HrMilli = MinMilli * 60;
	
	var DyMilli = HrMilli * 24;
	
	var allMilli = DyMilli * diff;
	
	var dateObj ;
	
	dateObj = new Date(Date.parse(newDate) + allMilli)
	
	var newYear = dateObj.getFullYear();
	
	var newMonth = dateObj.getMonth () + 1;
	
	var newDay = dateObj.getDate();
	
	if(newMonth==12 && newDay == 31){
		var firstDay = (newYear + 1).toString() + "-01-01";
		
		return firstDay;
	}
	
	var mon = newMonth;
	
	if((dateObj.getMonth () + 1)<10){
		mon  = "0" + mon.toString(10); 
	}
	
	var day = newDay;
	
	if(day<10){
		day = "0" + day.toString(); 
	}
	var result;
	result = newYear.toString() + "-" + mon.toString() + "-" + day.toString();
	return result;
}

function insert(){
	Objsel =document.selection.createRange();
	Objsel.expand("character");
	Objsel.select();
}

function submitForm(obj){

	if(valiDate()){
		Form0.submit();
	}else{
		var oldVal ;
		oldVal = new String (oldDate.toString());
		if(obj!=null){
			var newVal;
			newVal =  addDays(oldVal,0,"-")
			obj.value = newVal;
		}
	}
}
