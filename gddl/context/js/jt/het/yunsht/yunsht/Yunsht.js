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
body.onload =function(){
	//设置条件限制代码
	
//	objsTiaoj=document.all.item("TIAOJ_ID");
//	if(objsTiaoj!=null){
//		if(typeof(objsTiaoj.length)!='undefined'){
//			for(i=0;i<objsTiaoj.length;i++){
//				if(objsTiaoj[i].value=='大于'||objsTiaoj[i].value=='大于等于'||objsTiaoj[i].value=='等于'){
//					document.all.item('shangx',i).disabled=true;
//				}else if(objsTiaoj[i].value=='小于'||objsTiaoj[i].value=='小于等于'){
//					document.all.item('xiax',i).disabled=true;
//				}else{
//				    document.all.item('xiax',i).disabled=false;
//					document.all.item('shangx',i).disabled=false;
//				}
//			}
//		}else{
//				if(objsTiaoj.value=='大于'||objsTiaoj.value=='大于等于'||objsTiaoj.value=='等于'){
//					document.all.item('shangx').disabled=true;
//				}else if(objsTiaoj.value=='小于'||objsTiaoj.value=='小于等于'){
//					document.all.item('xiax').disabled=true;
//				}else{
//					document.all.item('xiax').disabled=false;
//					document.all.item('shangx').disabled=false;
//				}
//		}
//		
//	}
}
window.onresize=function(){
	//日期
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
	//布局 合同功能区、合同信息区、合同详细信息区以body为基准
	

var bodyHeight;
var bodyWidth ;
var bodyTop;
var tablemainHeight;
var tablemainWidth;

var trconditionHeight;
var trconditionWidth;

var trhetxxHeight;
var trhetxxWidth;
var tableTileHeight=40;
body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop;
tablemainWidth =bodyWidth-5 ;
trconditionHeight =20;
trconditionWidth=tablemainWidth ;
trhetxxHeight=80;
trhetxxWidth=tablemainWidth;
trhetzxxHeight=tablemainHeight-trconditionHeight-trhetxxHeight;
document.getElementById("trcondition").height=trconditionHeight;
document.getElementById("trcondition").width=trconditionWidth;
document.getElementById("hetxx").height=trhetxxHeight;
document.getElementById("hetxx").width=trhetxxWidth;
document.getElementById("tablemain").height=tablemainHeight;
document.getElementById("tablemain").width=tablemainWidth;

//SelectFrmDivg.style.position="relative";
//SelectHeadDivg.style.position="relative";
//SelectDataDivg.style.position="relative";
//SelectFrmDivg.style.height=trhetzxxHeight-70;
//SelectFrmDivg.style.width=tablemainWidth;
//SelectHeadDivg.style.width=tablemainWidth;
//SelectHeadDivg.style.height=tableTileHeight;
//SelectHeadDivg.style.posLeft=0;
//SelectDataDivg.style.width=tablemainWidth;
//SelectDataDivg.style.height=trhetzxxHeight-100-12;
//SelectDataDivg.style.top=tableTileHeight-58;
//oDatag.style.borderRight="gray 1px solid";
//EditHeadTabelg.style.borderRight="gray 1px solid";
//EditHeadTabelg.style.height=tableTileHeight-17;


document.getElementById("wenzTextareay").style.height=trhetzxxHeight-45;
document.getElementById("wenzTextareay").style.width=tablemainWidth;
 j=0;
 
}
//页面调用方法

//标签页
//价格

var oldClickrowg=-1;
var oldinputg;
var objselectColg;
var oldSelectobjg;
var Editrowsg=-1;

function droponchangeg(_int){
	if (objselectColg !=null){
		objselectColg.value =oldSelectobjg.options(oldSelectobjg.selectedIndex).text;

//		if(oldSelectobjg.id=='TiaojSelect'){
//			if(objselectColg.value=='小于'||objselectColg.value=='小于等于'){
//				document.all.item('xiax',oldClickrowg).disabled=true;
//				document.all.item('xiax',oldClickrowg).value=0;
//				document.all.item('shangx',oldClickrowg).disabled=false;
//
//			}else if(objselectColg.value=='大于'||objselectColg.value=='大于等于'||objselectColg.value=='等于'){
//				document.all.item('shangx',oldClickrowg).disabled=true;
//				document.all.item('shangx',oldClickrowg).value=0;
//				document.all.item('xiax',oldClickrowg).disabled=false;
//
//			}else{
//					document.all.item('xiax',oldClickrowg).disabled=false;
//					document.all.item('shangx',oldClickrowg).disabled=false;
//			}	
//		}
//		if(oldSelectobjg.id=='ZhibSelect'){
//			document.all.item("ZhibdwSelect").length=0;
//			zhibb_id=oldSelectobjg.options(oldSelectobjg.selectedIndex).value;
//			var j=0;
//			for(i=0;i<zhib_danw.length;i++){
//				if(zhibb_id==zhib_danw[i][0]){
//					document.all.item("ZhibdwSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
//				}
//			}
//			if(document.all.item("ZhibdwSelect").options(0)!=null){
//		   		document.all.item("ZHIBDW_ID",oldClickrowg).value=document.all.item("ZhibdwSelect").options(0).text;
//			}else{
//				document.all.item("ZHIBDW_ID",oldClickrowg).value="";
//			}
//		}	
		
	}
}

function dropdowng(_obj1,_obj2){
	
	if(_obj2.id=='ZhibdwSelect'){
		document.all.item("ZhibdwSelect").length=0;
		zhibb_mc=document.all.item("ZHIBB_ID",_obj1.parentElement.parentElement.rowIndex).value;
		var j=0;
		for(i=0;i<zhib_danw.length;i++){
			if(zhibb_mc==zhib_danw[i][3]){
				document.all.item("ZhibdwSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
			}
		}
	}
	
	if (oldSelectobjg!=null){
		oldSelectobjg.style.display="none";
		 oldSelectobjg.selectedIndex=0;
	}
	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop -1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColg =_obj1;
	oldSelectobjg=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editing(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputg!=null){
		oldinputg.style.border="#ffffff 0px solid";
		oldinputg.style.posWidth=oldinputg.parentElement.offsetWidth -2  ;
		oldinputg.style.backgroundColor="";
		oldinputg.style.position="";
		oldinputg.style.left="";
		oldinputg.style.height="";
		oldinputg.style.top="";
		oldinputg.style.padding =2;
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
			if (oldSelectobjg!=null){oldSelectobjg.style.display="none";}
		}
	}
	oldinputg =_str1;	
}

function classchangeg(_str1,_str2){

var rows=oDatag.rows ;

	if(_str2=="onclick"){
		if (oldClickrowg !=-1){
			oDatag.rows(oldClickrowg).className="edittableTrOut";
		}
		oldClickrowg=_str1.rowIndex;
		oDatag.rows(oldClickrowg).className="edittableTrClick";
	}

	if(_str2=="onmouseout"){
		if (oldClickrowg!=_str1.rowIndex){
			_str1.className="edittableTrOut";
		}else{
			_str1.className="edittableTrClick";
		}
	}
	
	if(_str2=="onmouseover"){
		
		if (oldClickrowg!=_str1.rowIndex){
			_str1.className="edittableTrOn";
		}else{
			_str1.className="edittableTrClick";
		}
		
	}
	document.all.item("EditTableRowg").value=oldClickrowg;
}


function _onscrollg(_obj1,_obj2){
	if (oldSelectobjg!=null){oldSelectobjg.style.display="none";}
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

function editclickg(_obj1,_obj2){
	if (oldSelectobjg!=null){
		oldSelectobjg.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft;
	_obj2.style.posTop=_obj1.parentElement.offsetTop ;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight ; 
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectColg =_obj1;
	oldSelectobjg=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangeg(_int){
	if (objselectColg !=null){
		objselectColg.value =oldSelectobjg.value;
	}

}	

function _Modulebar(imgRow){
	document.all.item("TabbarSelect").value=imgRow;//保持标签作用
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
			OutLineID(i).src=context + "/imgs/tab/img_formtab_middle_off.gif";//图象
		}
		TDid(i).style.color="#888888";//文本
		TDid(i).background=context + "/imgs/tab/img_formtab_back_off.gif";
		selectTab(i).style.display="none";//页
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
/*
function saveCheck(){
	if(document.all.item("hetbh").value==''){
		alert("合同编号不能为空!");
			return false;								
	}
	if(document.all.item("qiandrq").value==''){
		alert("合同签定日期不能为空!");		
		return false;							
	}
	if(cbo_hetSelect.getValue()==-1){//如果是新建合同必须套用模板
		if(cbo_mobmcSelect.getValue()==-1){
			alert("请选择合同模板!");
			return false;			
		}	
	}

	if(document.all.item("shengxsj").value==''){
		alert("合同生效日期不能为空!");		
		return false;							
	}
	if(document.all.item("guoqsj").value==''){
		alert("合同过期时间不能为空!");		
		return false;							
	}	
	if(document.all.item("gongfdwmc").value==''){
		alert("合同供方不能为空!");		
		return false;							
	}
	if(document.all.item("gongfSelect").selectedIndex==0){
		alert("合同供方不能为空!");
			return false;							
	}
//	if(document.all.item("shijgfSelect").selectedIndex==0){
//		alert("合同发货人不能为空!");
//			return false;							
//	}
	if(document.all.item("xufdwmc").value==''){
		alert("合同需方不能为空!");		
		return false;							
	}
	
	//基本价格标准
	for(i=0;i<oDatag.rows.length;i++){
		
		
		if(document.all.item("ZHIBB_ID",i).value==''){
			alert("<价格>标签页中第<"+(i+1)+">行<指标>不能为空！");
			return false;
		}else if (document.all.item("ZHIBB_ID",i).value=='运距'){
			if(document.all.item("Meikxx",i).value==''){
				alert("<价格>标签页中第<"+(i+1)+">行<煤矿单位>不能为空！");
				return false;
			}
		}
		if(document.all.item("ZHIBDW_ID",i).value==''){
			alert("<价格>标签页中第<"+(i+1)+">行<指标单位>不能为空！");
			return false;
		}
//		if(document.all.item("TIAOJ_ID",i).value==''){
//			alert("<价格>标签页中第<"+(i+1)+">行<条件>不能为空！");
//			return false;
//		}
		if(document.all.item("Yunjia",i).value==''){
			alert("<价格>标签页中第<"+(i+1)+">行<运价>不能为空！");
			return false;
		}
		
		if(document.all.item("Yunjiadw",i).value==''){
			alert("<价格>标签页中第<"+(i+1)+">行<运价单位>不能为空！");
			return false;
		}
    }

	return true;
}	
* */
 function chekRepear(){
	if(!submited){
		submited=true;
		return true;
	}else{
		return false;
	}
 }
 
 function Pipfz(Type){
	
	var objselect;
	var quanc="";
	if(Type=="chengyf"){
		
		objselect=document.getElementById("gongfSelect");
		quanc=objselect.options(objselect.selectedIndex).text;
		
		if(Chengydw.length==0){
			
			alert("请添加承运单位！");
			return;
		}else{
			
			for(var i=0;i<Chengydw.length;i++){
			
				if(quanc==Chengydw[i][10]){
					
					document.getElementById("gongfdwmc").value=Chengydw[i][0];
					document.getElementById("gongfdwdz").value=Chengydw[i][1];
					document.getElementById("gongffddbr").value=Chengydw[i][4];
					document.getElementById("gongfwtdlr").value=Chengydw[i][5];
					document.getElementById("gongfdn").value=Chengydw[i][8];
					document.getElementById("gongfcz").value=Chengydw[i][9];
					document.getElementById("gongfkhyh").value=Chengydw[i][6];
					document.getElementById("gongfzh").value=Chengydw[i][7];
					document.getElementById("gongfyzbm").value=Chengydw[i][2];
					document.getElementById("Gshuih").value=Chengydw[i][3];
				}
			}
		}
	}else if (Type=="xuf"){
		
		objselect=document.getElementById("xufSelect");
		quanc=objselect.options(objselect.selectedIndex).text;
		
		if(xuf.length==0){
			
			alert("请添加托运方！");
			return;
		}else{
			
			for(var i=0;i<xuf.length;i++){
			
				if(quanc==xuf[i][10]){
					
					document.getElementById("xufdwmc").value=xuf[i][0];
					document.getElementById("xufdwdz").value=xuf[i][1];
					document.getElementById("xuffddbr").value=xuf[i][4];
					document.getElementById("xufwtdlr").value=xuf[i][5];
					document.getElementById("xufdn").value=xuf[i][8];
					document.getElementById("xufcz").value=xuf[i][9];
					document.getElementById("xufkhyh").value=xuf[i][6];
					document.getElementById("xuffzh").value=xuf[i][7];
					document.getElementById("xufyzbm").value=xuf[i][2];
					document.getElementById("shuih").value=xuf[i][3];
				}
			}
		}
	}
 }