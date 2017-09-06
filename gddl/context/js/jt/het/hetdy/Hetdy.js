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

tablemainHeight=bodyHeight-bodyTop +5;
tablemainWidth =bodyWidth-8 ;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight+10;
SelectDataWidth=tablemainWidth ;

SelectFrmDiv.style.position="relative";

trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=tablemainWidth;
tablemain.style.height=tablemainHeight;

SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;
}
