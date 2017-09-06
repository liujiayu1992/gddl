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


body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop -10;
tablemainWidth =bodyWidth;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight  - 45;
SelectDataWidth=tablemainWidth ;

SelectFrmDiv.style.position="relative";

trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight+10;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;

	
}

var oldClickrow=-1;
var oldinput;
var objselectCol;
var oldSelectobj;
var Editrows=-1;

function droponchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.options(oldSelectobj.selectedIndex).text;
		if (_int==1){
			updatarows(objselectCol.name);
		}
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


function print(objid){
	var obj = document.getElementById(objid);
	var printStr = "<title>??</title>";
	printStr += "<style media=\"print\">.noPrint {display:none;}<\/style>";
	printStr += "<style>body{background:#FFFFFF;font-size:12px;}<\/style>";
	printStr += "<style>\n";
	printStr += ".tdLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
   	printStr += ".tdRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
	printStr += ".tdNormal{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\\n";
	printStr += ".tdNoneLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNoneRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNone{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += "<\/style>\n";
	   		
	printStr += obj.innerHTML;
	var a = window.open("about:blank",null,"width=700,height=500,toolbar=yes,left=0,top=0");
	var abody = a.document.open();
	abody.write(printStr);
	abody.close();
	a.print();
	a.close();
	printStr = null;
	a = null;
}

function exportExcel(objid){ 
	var obj = document.getElementById(objid);
	window.clipboardData.setData("Text",obj.innerHTML);
	var oXL = new ActiveXObject("Excel.Application"); 
	oXL.Application.Visible=true;
	var oWB = oXL.Workbooks.Add(); 
	var oSheet = oWB.Worksheets(1);
	try{	
		oSheet.Paste();
 	}catch(e){
 		alert("????");
 	}
} 

function navigatorClick(strAction){ 
	var intCurrentPage=1;
	var intAllPages=1;
	var intOldPage=1;
	var intGoPage=1;
		
	intOldPage=eval(document.all.item("CurrentPage").value);
	intAllPages=eval(document.all.item("AllPages").value);
	
	if (strAction=='go'){
		try{
			intGoPage=eval(document.all.item("GoPage").value);
		}catch(e){
			alert("?????!");
			return;
		}	
	
		if ((intGoPage>=1) && (intGoPage<=intAllPages)){
			if (intGoPage==intOldPage){
				return;
			}else{
				intCurrentPage=intGoPage;
			}
		}else{
			alert("??????!");
			return;
		}
	}else if (strAction=='first'){
		intCurentPage=1;
	}else if (strAction=='previous'){
		if  (intOldPage>1){
			intCurrentPage=intOldPage-1;
		}
	}else if (strAction=='next'){
		if (intOldPage<intAllPages){
			intCurrentPage=intOldPage+1;
		}else{
			intCurrentPage=intOldPage;
		}
	}else if (strAction=='last'){
		intCurrentPage=intAllPages;
	}

	if (intOldPage!=intCurrentPage){
		document.all.item("CurrentPage").value=intCurrentPage;
		setNavigatorButton();
		document.all.item("reportpage"+intCurrentPage).style.display="";
		document.all.item("reportpage"+intOldPage).style.display="none";
	}
} 
		
//function setNavigatorButton(){
//	var intCurrentPage=0;
//	var intAllPages=0;
//	intCurrentPage=eval(document.all.item("CurrentPage").value);
//	intAllPages=eval(document.all.item("AllPages").value);
//	
//	if (intCurrentPage<=1){
//		document.all.item("firstPage").disabled=true;
//		document.all.item("previousPage").disabled=true;
//	}else{
//		document.all.item("firstPage").disabled=false;
//		document.all.item("previousPage").disabled=false;
//	}
//	
//	if (intCurrentPage==intAllPages){
//		document.all.item("nextPage").disabled=true;
//		document.all.item("lastPage").disabled=true;
//	}else{
//		document.all.item("NextPage").disabled=false;
//		document.all.item("LastPage").disabled=false;
//	}
//	
//	document.all.item("pageNumber").innerText=intCurrentPage;
//	document.all.item("totalPage").innerText=intAllPages;
//	
//	if (intAllPages==1){
//		document.all.item("goButton").disabled=true;
//	}
//	
//}