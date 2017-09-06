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
var rzbHeight=100;
var rzbWidth;

body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop;
tablemainWidth =bodyWidth -15;
ConditionWidth=tablemainWidth ;
rzbWidth=tablemainWidth;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - rzbHeight;
SelectDataWidth=tablemainWidth ;

SelectFrmDiv.style.position="relative";

tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;
rzb.style.height=rzbHeight;
rzb.style.width=rzbWidth;
rizdiv.style.height=rzbHeight;
rizdiv.style.width=rzbWidth;
rizdiv.style.posTop=0;

SelectFrmDiv.style.height=SelectDataHeight;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;
}