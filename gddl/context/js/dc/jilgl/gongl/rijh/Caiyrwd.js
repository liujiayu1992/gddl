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

body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop -15;
tablemainWidth =bodyWidth ;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight-30;
SelectDataWidth=tablemainWidth ;

SelectFrmDiv.style.position="relative";

trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;
}

function leixSelect(obj){
//	if(obj.options(obj.selectedIndex).value==1){//分厂
//		document.getElementById("DanwSelect").style.display=''; 
//		document.getElementById("danw").style.display='';
//		document.getElementById("GongysDropDown").style.display='none'; 
//		document.getElementById("gongys").style.display='none';  
//	}else {//分矿
//		document.getElementById("DanwSelect").style.display='none'; 
//		document.getElementById("danw").style.display='none'; 
//		document.getElementById("GongysDropDown").style.display=''; 
//		document.getElementById("gongys").style.display=''; 
//	}
}

setBegin =function(){
//	obj=document.getElementById("LeixSelect");
//	if(document.getElementById("LeixSelect").options(obj.selectedIndex).value==1){//分厂
//		document.getElementById("DanwSelect").style.display=''; 
//		document.getElementById("danw").style.display='';
//		document.getElementById("GongysDropDown").style.display='none'; 
//		document.getElementById("gongys").style.display='none';  
//	}else {//分矿
//		document.getElementById("DanwSelect").style.display='none'; 
//		document.getElementById("danw").style.display='none'; 
//		document.getElementById("GongysDropDown").style.display=''; 
//		document.getElementById("gongys").style.display=''; 
//	}
}