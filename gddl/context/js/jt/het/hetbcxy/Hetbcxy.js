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
objsTiaoj=document.all.item("TIAOJ");
if(objsTiaoj!=null){
		if(typeof(objsTiaoj.length)!='undefined'){
			for(i=0;i<objsTiaoj.length;i++){
				if(objsTiaoj[i].value=='����'||objsTiaoj[i].value=='���ڵ���'||objsTiaoj[i].value=='����'){
					document.all.item('shangx',i).disabled=true;
				}else if(objsTiaoj[i].value=='С��'||objsTiaoj[i].value=='С�ڵ���'){
					document.all.item('xiax',i).disabled=true;
				}else{
					document.all.item('xiax',i).disabled=false;
					document.all.item('shangx',i).disabled=false;
				}
			}
		}else{
			if(objsTiaoj.value=='����'||objsTiaoj.value=='���ڵ���'||objsTiaoj.value=='����'){
				document.all.item('shangx').disabled=true;
			}else if(objsTiaoj.value=='С��'||objsTiaoj.value=='С�ڵ���'){
				document.all.item('xiax').disabled=true;
			}else{
				document.all.item('xiax').disabled=false;
				document.all.item('shangx').disabled=false;
			}
		}
		
	}
	objsTiaoj=document.all.item("tiaojg");
	if(objsTiaoj!=null){
		if(typeof(objsTiaoj.length)!='undefined'){
			for(i=0;i<objsTiaoj.length;i++){
				if(objsTiaoj[i].value=='����'||objsTiaoj[i].value=='���ڵ���'||objsTiaoj[i].value=='����'){
					document.all.item('shangxg',i).disabled=true;
				}else if(objsTiaoj[i].value=='С��'||objsTiaoj[i].value=='С�ڵ���'){
					document.all.item('xiaxg',i).disabled=true;
				}else{
				    document.all.item('xiaxg',i).disabled=false;
					document.all.item('shangxg',i).disabled=false;
				}
			}
		}else{
				if(objsTiaoj.value=='����'||objsTiaoj.value=='���ڵ���'||objsTiaoj.value=='����'){
					document.all.item('shangxg').disabled=true;
				}else if(objsTiaoj.value=='С��'||objsTiaoj.value=='С�ڵ���'){
					document.all.item('xiaxg').disabled=true;
				}else{
					document.all.item('xiaxg').disabled=false;
					document.all.item('shangxg').disabled=false;
				}
		}
		
	}
	objsTiaoj=document.all.item("tiaojj");
	if(objsTiaoj!=null){
		if(typeof(objsTiaoj.length)!='undefined'){
			for(i=0;i<objsTiaoj.length;i++){
				if(objsTiaoj[i].value=='����'||objsTiaoj[i].value=='���ڵ���'||objsTiaoj[i].value=='����'){
					document.all.item('shangxj',i).disabled=true;
				}else if(objsTiaoj[i].value=='С��'||objsTiaoj[i].value=='С�ڵ���'){
					document.all.item('xiaxj',i).disabled=true;
				}else{
				   document.all.item('xiaxj',i).disabled=false;
				   document.all.item('shangxj',i).disabled=false;
				}
			}
		}else{
			if(objsTiaoj.value=='����'||objsTiaoj.value=='���ڵ���'||objsTiaoj.value=='����'){
				document.all.item('shangxj').disabled=true;
			}else if(objsTiaoj.value=='С��'||objsTiaoj.value=='С�ڵ���'){
				document.all.item('xiaxj').disabled=true;
			}else{
				document.all.item('xiaxj').disabled=false;
				document.all.item('shangxj').disabled=false;
			}
		}
	}
}
window.onresize=function(){
	//����
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
	//���� ��ͬ����������ͬ��Ϣ������ͬ��ϸ��Ϣ����bodyΪ��׼
	

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
tablemainWidth =bodyWidth -5;
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
//for(i=0;i<6;i++){
//	document.all.item("div",i).style.width=tablemainWidth;
//	document.all.item("div",i).style.height=trhetzxxHeight-41;
//}
SelectFrmDivs.style.position="relative";
SelectHeadDivs.style.position="relative";
SelectDataDivs.style.position="relative";
SelectFrmDivs.style.height=trhetzxxHeight-70;
SelectFrmDivs.style.width=tablemainWidth;
SelectHeadDivs.style.width=tablemainWidth;
SelectHeadDivs.style.height=tableTileHeight;
SelectHeadDivs.style.posLeft=0;
SelectDataDivs.style.width=tablemainWidth;
SelectDataDivs.style.height=trhetzxxHeight-100-12;
oDatas.style.borderRight="gray 1px solid";
EditHeadTabels.style.borderRight="gray 1px solid";
EditHeadTabels.style.height=tableTileHeight;

SelectFrmDivz.style.position="relative";
SelectHeadDivz.style.position="relative";
SelectDataDivz.style.position="relative";
SelectFrmDivz.style.height=trhetzxxHeight-70;
SelectFrmDivz.style.width=tablemainWidth;
SelectHeadDivz.style.width=tablemainWidth;
SelectHeadDivz.style.height=tableTileHeight;
SelectHeadDivz.style.posLeft=0;
SelectDataDivz.style.width=tablemainWidth;
SelectDataDivz.style.height=trhetzxxHeight-100-12;
oDataz.style.borderRight="gray 1px solid";
EditHeadTabelz.style.borderRight="gray 1px solid";
EditHeadTabelz.style.height=tableTileHeight;

SelectFrmDivg.style.position="relative";
SelectHeadDivg.style.position="relative";
SelectDataDivg.style.position="relative";
SelectFrmDivg.style.height=trhetzxxHeight-70;
SelectFrmDivg.style.width=tablemainWidth;
SelectHeadDivg.style.width=tablemainWidth;
SelectHeadDivg.style.height=tableTileHeight;
SelectHeadDivg.style.posLeft=0;
SelectDataDivg.style.width=tablemainWidth;
SelectDataDivg.style.height=trhetzxxHeight-100-12;
oDatag.style.borderRight="gray 1px solid";
EditHeadTabelg.style.borderRight="gray 1px solid";
EditHeadTabelg.style.height=tableTileHeight;

SelectFrmDivj.style.position="relative";
SelectHeadDivj.style.position="relative";
SelectDataDivj.style.position="relative";
SelectFrmDivj.style.height=trhetzxxHeight-70;
SelectFrmDivj.style.width=tablemainWidth;
SelectHeadDivj.style.width=tablemainWidth;
SelectHeadDivj.style.height=tableTileHeight;
SelectHeadDivj.style.posLeft=0;
SelectDataDivj.style.width=tablemainWidth;
SelectDataDivj.style.height=trhetzxxHeight-100-12;
oDataj.style.borderRight="gray 1px solid";
EditHeadTabelj.style.borderRight="gray 1px solid";
EditHeadTabelj.style.height=tableTileHeight;

document.getElementById("wenzTextareay").style.height=trhetzxxHeight-45;
document.getElementById("wenzTextareay").style.width=tablemainWidth;
 j=0;
 for(i=0;i<oDatas.rows.length;i++){
     document.all.item("Y1s",i).tabIndex=++j;
     document.all.item("Y2s",i).tabIndex=++j;
     document.all.item("Y3s",i).tabIndex=++j;
     document.all.item("Y4s",i).tabIndex=++j;
     document.all.item("Y5s",i).tabIndex=++j;
     document.all.item("Y6s",i).tabIndex=++j;
     document.all.item("Y7s",i).tabIndex=++j;
     document.all.item("Y8s",i).tabIndex=++j;
     document.all.item("Y9s",i).tabIndex=++j;
     document.all.item("Y10s",i).tabIndex=++j;
     document.all.item("Y11s",i).tabIndex=++j;
     document.all.item("Y12s",i).tabIndex=++j;
 }
//document.getElementById("hr").style.width=document.getElementById("gongfTable").style.width;
//
//hetxxHeight=tablemainHeight - trcondition  - 20;
//SelectDataWidth=tablemainWidth ;
////QueryFrameClsssend
///*
//SelectDataHeight=Math.round((tablemainHeight-ConditionHeight)/7 * 4);
//SelectDataWidth=tablemainWidth ;
//EditDataWidth=tablemainWidth ;
//EditDataHeight=tablemainHeight-SelectDataHeight-ConditionHeight -15;
//EditFrmDiv.style.position="relative";
//*/
////*/
//SelectFrmDiv.style.position="relative";
//
//SelectHeadDiv.style.position="relative";
//SelectDataDiv.style.position="relative";
//trcondition.style.height=20;
//tablemain.style.left=0;
//
//tablemain.style.width=bodyWidth -15;
//tablemain.style.height=tablemainHeight;
//
////????????
//SelectData.style.height=SelectDataHeight;
//SelectData.style.width=SelectDataWidth;
//
//SelectFrmDiv.style.height=SelectDataHeight;
//SelectFrmDiv.style.width=SelectDataWidth;
//SelectFrmDiv.style.posTop=0;
//
//SelectHeadDiv.style.posTop=0;
//SelectHeadDiv.style.width=SelectDataWidth-10;
//SelectHeadDiv.style.height=24;
//SelectHeadDiv.style.posLeft=0;
//
//SelectDataDiv.style.posTop=0;
//SelectDataDiv.style.width=SelectDataWidth-7;
//SelectDataDiv.style.height=SelectDataHeight-22;
//SelectDataDiv.style.posLeft=-2;	
}
//ҳ����÷���
//����
//_onscroll classchanges editins dropdowns editclicks editchanges droponchange
var oldClickrow=-1;
var oldinput;
var objselectCol;
var oldSelectobj;
var Editrows=-1;

function droponchanges(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.options(oldSelectobj.selectedIndex).text;
		if (_int==1){
			updatarows(objselectCol.name);
		}
	}
}
function dropdowns(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
		 oldSelectobj.selectedIndex=0;
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
function editins(_str1,_str2)
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
	_str1.parentElement.parentElement.click();
}

function classchanges(_str1,_str2){

var rows=oDatas.rows ;

	if(_str2=="onclick"){
		if (oldClickrow !=-1){
			oDatas.rows(oldClickrow).className="edittableTrOut";
		}
		oldClickrow=_str1.rowIndex;
		oDatas.rows(oldClickrow).className="edittableTrClick";
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
	document.all.item("EditTableRows").value=oldClickrow;
}


function _onscrolls(_obj1,_obj2){
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

function editclicks(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}
	_obj2.tabIndex=_obj1.tabIndex ;
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
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchanges(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.value;
	}
	colsCount();
}

//����

var oldClickrowz=-1;
var oldinputz;
var objselectColz;
var oldSelectobjz;
var Editrowsz=-1;

function droponchangez(_int){
	if (objselectColz !=null){
		objselectColz.value =oldSelectobjz.options(oldSelectobjz.selectedIndex).text;
		if(oldSelectobjz.id=='TIAOJSelect'){
			if(objselectColz.value=='С��'||objselectColz.value=='С�ڵ���'){
				document.all.item('xiax',oldClickrowz).disabled=true;
				document.all.item('xiax',oldClickrowz).value=0;
				document.all.item('shangx',oldClickrowz).disabled=false;
			}else if(objselectColz.value=='����'||objselectColz.value=='���ڵ���'||objselectColz.value=='����'){
				document.all.item('shangx',oldClickrowz).disabled=true;
				document.all.item('shangx',oldClickrowz).value=0;
				document.all.item('xiax',oldClickrowz).disabled=false;
			}else{
					document.all.item('xiax',oldClickrowz).disabled=false;
					document.all.item('shangx',oldClickrowz).disabled=false;
			}	
		}
		if(oldSelectobjz.id=='ZHIBSelect'){
			document.all.item("DANWSelect").length=0;
			zhibb_id=oldSelectobjz.options(oldSelectobjz.selectedIndex).value;
			var j=0;
			for(i=0;i<zhib_danw.length;i++){
				if(zhibb_id==zhib_danw[i][0]){
					document.all.item("DANWSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
				}
			}
			//����ֵ
			if(document.all.item("DANWSelect").options(0)!=null){
		   		document.all.item("DANW",oldClickrowz).value=document.all.item("DANWSelect").options(0).text;
			}else{
				document.all.item("DANW",oldClickrowz).value="";
			}
		}	
	}
}
function dropdownz(_obj1,_obj2){
	if(_obj2.id=='DANWSelect'){
		document.all.item("DANWSelect").length=0;
		zhibb_mc=document.all.item("ZHIB",_obj1.parentElement.parentElement.rowIndex).value;
		var j=0;
		for(i=0;i<zhib_danw.length;i++){
			if(zhibb_mc==zhib_danw[i][3]){
				document.all.item("DANWSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
			}
		}
	}
	if (oldSelectobjz!=null){
		oldSelectobjz.style.display="none";
		 oldSelectobjz.selectedIndex=0;
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
	objselectColz =_obj1;
	oldSelectobjz=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editinz(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputz!=null){
		oldinputz.style.border="#ffffff 0px solid";
		oldinputz.style.posWidth=oldinputz.parentElement.offsetWidth -2  ;
		oldinputz.style.backgroundColor="";
		oldinputz.style.position="";
		oldinputz.style.left="";
		oldinputz.style.height="";
		oldinputz.style.top="";
		oldinputz.style.padding =2;
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
			if (oldSelectobjz!=null){oldSelectobjz.style.display="none";}
		}
	}
	oldinputz =_str1;	
}

function classchangez(_str1,_str2){

var rows=oDataz.rows ;

	if(_str2=="onclick"){
		if (oldClickrowz !=-1){
			oDataz.rows(oldClickrowz).className="edittableTrOut";
		}
		oldClickrowz=_str1.rowIndex;
		oDataz.rows(oldClickrowz).className="edittableTrClick";
	}

	if(_str2=="onmouseout"){
		if (oldClickrowz!=_str1.rowIndex){
			_str1.className="edittableTrOut";
		}else{
			_str1.className="edittableTrClick";
		}
	}
	
	if(_str2=="onmouseover"){
		
		if (oldClickrowz!=_str1.rowIndex){
			_str1.className="edittableTrOn";
		}else{
			_str1.className="edittableTrClick";
		}
		
	}
	document.all.item("EditTableRowz").value=oldClickrowz;
}


function _onscrollz(_obj1,_obj2){
	if (oldSelectobjz!=null){oldSelectobjz.style.display="none";}
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

function editclickz(_obj1,_obj2){
	if (oldSelectobjz!=null){
		oldSelectobjz.style.display="none";
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
	objselectColz =_obj1;
	oldSelectobjz=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangez(_int){
	if (objselectColz !=null){
		objselectColz.value =oldSelectobjz.value;
	}

}
//��ǩҳ
//�۸�

var oldClickrowg=-1;
var oldinputg;
var objselectColg;
var oldSelectobjg;
var Editrowsg=-1;

function droponchangeg(_int){
	if (objselectColg !=null){
		objselectColg.value =oldSelectobjg.options(oldSelectobjg.selectedIndex).text;
		if(oldSelectobjg.id=='tiaojgSelect'){
			if(objselectColg.value=='С��'||objselectColg.value=='С�ڵ���'){
				document.all.item('xiaxg',oldClickrowg).disabled=true;
				document.all.item('xiaxg',oldClickrowg).value=0;
				document.all.item('shangxg',oldClickrowg).disabled=false;

			}else if(objselectColg.value=='����'||objselectColg.value=='���ڵ���'||objselectColg.value=='����'){
				document.all.item('shangxg',oldClickrowg).disabled=true;
				document.all.item('shangxg',oldClickrowg).value=0;
				document.all.item('xiaxg',oldClickrowg).disabled=false;

			}else{
					document.all.item('xiaxg',oldClickrowg).disabled=false;
					document.all.item('shangxg',oldClickrowg).disabled=false;
			}	
		}
		if(oldSelectobjg.id=='xiangmmgSelect'){
			document.all.item("zhibdwgSelect").length=0;
			zhibb_id=oldSelectobjg.options(oldSelectobjg.selectedIndex).value;
			var j=0;
			for(i=0;i<zhib_danw.length;i++){
				if(zhibb_id==zhib_danw[i][0]){
					document.all.item("zhibdwgSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
				}
			}
			if(document.all.item("zhibdwgSelect").options(0)!=null){
		   		document.all.item("zhibdwg",oldClickrowg).value=document.all.item("zhibdwgSelect").options(0).text;
			}else{
				document.all.item("zhibdwg",oldClickrowg).value="";
			}
		}		
	}
}
function dropdowng(_obj1,_obj2){
	if(_obj2.id=='zhibdwgSelect'){
		document.all.item("zhibdwgSelect").length=0;
		zhibb_mc=document.all.item("xiangmmg",_obj1.parentElement.parentElement.rowIndex).value;
		var j=0;
		for(i=0;i<zhib_danw.length;i++){
			if(zhibb_mc==zhib_danw[i][3]){
				document.all.item("zhibdwgSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
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
//����

var oldClickrowj=-1;
var oldinputj;
var objselectColj;
var oldSelectobjj;
var Editrowsj=-1;


function droponchangej(_int){
	if (objselectColj !=null){
		objselectColj.value =oldSelectobjj.options(oldSelectobjj.selectedIndex).text;
		if(oldSelectobjj.id=='tiaojjSelect'){
			if(objselectColj.value=='С��'||objselectColj.value=='С�ڵ���'){
				document.all.item('xiaxj',oldClickrowj).disabled=true;
				document.all.item('xiaxj',oldClickrowj).value=0;
				
				document.all.item('shangxj',oldClickrowj).disabled=false;

			}else if(objselectColj.value=='����'||objselectColj.value=='���ڵ���'||objselectColj.value=='����'){
				document.all.item('shangxj',oldClickrowj).disabled=true;
				document.all.item('shangxj',oldClickrowj).value=0;
				document.all.item('xiaxj',oldClickrowj).disabled=false;

			}else{
					document.all.item('xiaxj',oldClickrowj).disabled=false;
					document.all.item('shangxj',oldClickrowj).disabled=false;
			}	
		}
		if(oldinputj.id=='canzxm'){//ѡ����Ŀʱ����
			document.all.item("zhibdwSelect").length=0;
			zhibb_id=oldSelectobjj.options(oldSelectobjj.selectedIndex).value;
			var j=0;
			for(i=0;i<zhib_danw.length;i++){
				if(zhibb_id==zhib_danw[i][0]){
					document.all.item("zhibdwSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
				}
			}
			if(document.all.item("zhibdwSelect").options(0)!=null){
		   		document.all.item("canzxmdw",oldClickrowj).value=document.all.item("zhibdwSelect").options(0).text;
		   	
			}else{
				document.all.item("canzxmdw",oldClickrowj).value="";
			
			}
		}else{//��Ŀ
			if(oldSelectobjj.id=='zhilxmjSelect'){
				document.all.item("zhibdwSelect").length=0;
				zhibb_id=oldSelectobjj.options(oldSelectobjj.selectedIndex).value;
				var j=0;
				for(i=0;i<zhib_danw.length;i++){
					if(zhibb_id==zhib_danw[i][0]){
						document.all.item("zhibdwSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
					}
				}
				if(document.all.item("zhibdwSelect").options(0)!=null){
			   		document.all.item("zhibdw",oldClickrowj).value=document.all.item("zhibdwSelect").options(0).text;
			   		document.all.item("jisdw",oldClickrowj).value=document.all.item("zhibdwSelect").options(0).text;
				}else{
					document.all.item("zhibdw",oldClickrowj).value="";
					document.all.item("jisdw",oldClickrowj).value="";
				}
			}	
		}
	
	}
}
function dropdownj(_obj1,_obj2){
	if(oldinputj.id=='canzxmdw'){//������Ŀdanw
		if(_obj2.id=='zhibdwSelect'){
			document.all.item("zhibdwSelect").length=0;
			zhibb_mc=document.all.item("canzxm",_obj1.parentElement.parentElement.rowIndex).value;
			var j=0;
			for(i=0;i<zhib_danw.length;i++){
				if(zhibb_mc==zhib_danw[i][3]){
					document.all.item("zhibdwSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
				}
			}
		}
	 } else{
	 	if(_obj2.id=='zhibdwSelect'){
			document.all.item("zhibdwSelect").length=0;
			zhibb_mc=document.all.item("zhilxmj",_obj1.parentElement.parentElement.rowIndex).value;
			var j=0;
			for(i=0;i<zhib_danw.length;i++){
				if(zhibb_mc==zhib_danw[i][3]){
					document.all.item("zhibdwSelect").options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]);
				}
			}
		 }
	}
	if (oldSelectobjj!=null){
		oldSelectobjj.style.display="none";
		 oldSelectobjj.selectedIndex=0;
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
	objselectColj =_obj1;
	oldSelectobjj=_obj2;
	for (i=0;i<_obj2.options.length;i++){
		if (_obj1.value.toString()==_obj2.options(i).text){
			_obj2.selectedIndex=i;
			//_obj2.value=i;
			break;
		}
	}	

}
function editinj(_str1,_str2)
{	
	if (_str2=="onfocus"){
		_str1.select();
		return;
	}
	if (oldinputj!=null){
		oldinputj.style.border="#ffffff 0px solid";
		oldinputj.style.posWidth=oldinputj.parentElement.offsetWidth -2  ;
		oldinputj.style.backgroundColor="";
		oldinputj.style.position="";
		oldinputj.style.left="";
		oldinputj.style.height="";
		oldinputj.style.top="";
		oldinputj.style.padding =2;
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
			if (oldSelectobjj!=null){oldSelectobjj.style.display="none";}
		}
	}
	oldinputj =_str1;	
}

function classchangej(_str1,_str2){

var rows=oDataj.rows ;

	if(_str2=="onclick"){
		if (oldClickrowj !=-1){
			oDataj.rows(oldClickrowj).className="edittableTrOut";
		}
		oldClickrowj=_str1.rowIndex;
		oDataj.rows(oldClickrowj).className="edittableTrClick";
	}

	if(_str2=="onmouseout"){
		if (oldClickrowj!=_str1.rowIndex){
			_str1.className="edittableTrOut";
		}else{
			_str1.className="edittableTrClick";
		}
	}
	
	if(_str2=="onmouseover"){
		
		if (oldClickrowj!=_str1.rowIndex){
			_str1.className="edittableTrOn";
		}else{
			_str1.className="edittableTrClick";
		}
		
	}
	document.all.item("EditTableRowj").value=oldClickrowj;
}


function _onscrollj(_obj1,_obj2){
	if (oldSelectobjj!=null){oldSelectobjj.style.display="none";}
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

function editclickj(_obj1,_obj2){
	if (oldSelectobjj!=null){
		oldSelectobjj.style.display="none";
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
	objselectColj =_obj1;
	oldSelectobjj=_obj2;
	_obj2.value=_obj1.value;
	_obj2.select();
}
function editchangej(_int){
	if (objselectColj !=null){
		objselectColj.value =oldSelectobjj.value;
	}

}	 
function _Modulebar(imgRow){
	document.all.item("TabbarSelect").value=imgRow;//���ֱ�ǩ����
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
			OutLineID(i).src=context + "/imgs/tab/img_formtab_middle_off.gif";//ͼ��
		}
		TDid(i).style.color="#888888";//�ı�
		TDid(i).background=context + "/imgs/tab/img_formtab_back_off.gif";
		selectTab(i).style.display="none";//ҳ
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
//��֤�����
	function colsCount(){
		var indexh=document.all.item("EditTableRows").value;
		y1=parseFloat(document.all.item("Y1s",indexh).value);
		y2=parseFloat(document.all.item("Y2s",indexh).value);
		y3=parseFloat(document.all.item("Y3s",indexh).value);
		y4=parseFloat(document.all.item("Y4s",indexh).value);
		y5=parseFloat(document.all.item("Y5s",indexh).value);
		y6=parseFloat(document.all.item("Y6s",indexh).value);
		y7=parseFloat(document.all.item("Y7s",indexh).value);
		y8=parseFloat(document.all.item("Y8s",indexh).value);
		y9=parseFloat(document.all.item("Y9s",indexh).value);
		y10=parseFloat(document.all.item("Y10s",indexh).value);
		y11=parseFloat(document.all.item("Y11s",indexh).value);
		y12=parseFloat(document.all.item("Y12s",indexh).value);
		document.all.item("HEJs",indexh).value=y1+y2+y3+y4+y5+y6+y7+y8+y9+y10+y11+y12;
	}
	
	function saveCheck(){
	if(document.all.item("hetbh").value==''){
		alert("��ͬ��Ų���Ϊ��!");
			return false;								
	}
	if(document.all.item("qiandrq").value==''){
		alert("��ͬǩ�����ڲ���Ϊ��!");		
		return false;							
	}
	if(cbo_mobmcSelect.getValue()==-1||cbo_mobmcSelect.getValue()==''){		
		alert("��ͬģ�����Ʋ���Ϊ��!");		
		return false;			
	}
//					if(document.all.item("mobmc").value==''){
//						alert("��ͬģ�����Ʋ���Ϊ��!");		
//						return false;							
//					}
	if(document.all.item("jihkjSelect").selectedIndex==0){
		alert("��ѡ��ƻ��ھ�!");
			return false;							
	}
	if(document.all.item("zhuhtSelect").selectedIndex==0){
		alert("��ѡ������ͬ!");
			return false;							
	}	
	
//					if(document.all.item("oDatas").rows.length==0){
//						alert("��ͬ��������Ϊ��!");		
//						return false;							
//					}
	if(cbo_hetSelect.getValue()==-1){//������½���ͬ��������ģ��
		if(cbo_mobmcSelect.getValue()==-1){
			alert("��ѡ���ͬģ��!");
			return false;			
		}	
	}
	//if(document.all.item("jiesff").selectedIndex==0){
//		alert("��ѡ����㷽��!");
//			return false;							
//	}
//	if(document.all.item("jiesfs").selectedIndex==0){
//		alert("��ѡ����㷽ʽ!");
//			return false;							
//	}
//	if(document.all.item("jijfs").selectedIndex==0){
//		alert("��ѡ�����Ƽ۷�ʽ!");
//			return false;							
//	}
	if(document.all.item("shengxsj").value==''){
		alert("��ͬ��Ч���ڲ���Ϊ��!");		
		return false;							
	}
	if(document.all.item("guoqsj").value==''){
		alert("��ͬ����ʱ�䲻��Ϊ��!");		
		return false;							
	}	
	if(document.all.item("gongfdwmc").value==''){
		alert("��ͬ��������Ϊ��!");		
		return false;							
	}
	if(document.all.item("gongfSelect").selectedIndex==0){
		alert("��ͬ��������Ϊ��!");
			return false;							
	}
	if(document.all.item("shijgfSelect").selectedIndex==0){
		alert("��ͬ�����˲���Ϊ��!");
			return false;							
	}
	if(document.all.item("xufdwmc").value==''){
		alert("��ͬ�跽����Ϊ��!");		
		return false;							
	}
	//����
	for(i=0;i<oDatas.rows.length;i++){
		if(document.all.item("HEJs",i).value==0){
		alert("<������Ϣ>��ǩҳ�е�<"+(i+1)+">��<�ϼ�>����Ϊ�㣡");
		return false;
		}
		if(document.all.item("RANLPZB_ID",i).value==''){
		alert("<������Ϣ>��ǩҳ�е�<"+(i+1)+">��<ú̿Ʒ��>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("yunsfs",i).value==''){
		alert("<������Ϣ>��ǩҳ�е�<"+(i+1)+">��<���䷽ʽ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("faz",i).value==''){
		alert("<������Ϣ>��ǩҳ�е�<"+(i+1)+">��<��վ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("daoz",i).value==''){
		alert("<������Ϣ>��ǩҳ�е�<"+(i+1)+">��<��վ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("shouhr",i).value==''){
		alert("<������Ϣ>��ǩҳ�е�<"+(i+1)+">��<�ջ���>����Ϊ�գ�");
		return false;
		}
		
    }
	//����
	for(i=0;i<oDataz.rows.length;i++){
		if(document.all.item("ZHIB",i).value==''){
		alert("<����Ҫ��>��ǩҳ�е�<"+(i+1)+">��<��Ŀ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("TIAOJ",i).value==''){
		alert("<����Ҫ��>��ǩҳ�е�<"+(i+1)+">��<����>����Ϊ�գ�");
		return false;
		}
//						if(document.all.item("ZHI",i).value==0){
//						alert("<����Ҫ��>��ǩҳ�е�<"+(i+1)+">��<ֵ>����Ϊ�㣡");
//						return false;
//						}
		if(document.all.item("DANW",i).value==''){
		alert("<����Ҫ��>��ǩҳ�е�<"+(i+1)+">��<��λ>����Ϊ�գ�");
		return false;
		}

    }
	//�����۸��׼
	for(i=0;i<oDatag.rows.length;i++){
		if(document.all.item("xiangmmg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<��Ŀ��>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("tiaojg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<����>����Ϊ�գ�");
		return false;
		}
		
		if(document.all.item("zhibdwg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<��λ>����Ϊ�գ�");
		return false;
		}
		
		if(document.all.item("jiagdwg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<�۸�λ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("jiesfsg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<���㷽ʽ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("jijfsg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<�Ƽ۷�ʽ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("jiaqfsg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<��Ȩ��ʽ>����Ϊ�գ�");
		return false;
		}
		if(document.all.item("yunsfsg",i).value==''){
		alert("<�۸�>��ǩҳ�е�<"+(i+1)+">��<���䷽ʽ>����Ϊ�գ�");
		return false;
		}
    }
	//���ۼ�
	if(document.all.item("oDataj").value!=1){
		for(i=0;i<oDataj.rows.length;i++){
			if(document.all.item("zhilxmj",i).value==''){
			alert("<���ۿ�>��ǩҳ�е�<"+(i+1)+">��<��Ŀ>����Ϊ�գ�");
			return false;
			}
			if(document.all.item("tiaojj",i).value==''){
			alert("<���ۿ�>��ǩҳ�е�<"+(i+1)+">��<����>����Ϊ�գ�");
			return false;
			}
			
			if(document.all.item("zhibdw",i).value==''){
			alert("<���ۿ�>��ǩҳ�е�<"+(i+1)+">��<��λ>����Ϊ�գ�");
			return false;
			}
			if(document.all.item("jis",i).value==''){
			alert("<���ۿ�>��ǩҳ�е�<"+(i+1)+">��<����>����Ϊ�գ�");
			return false;
			}
			if(document.all.item("jisdw",i).value==''){
			alert("<���ۿ�>��ǩҳ�е�<"+(i+1)+">��<������λ>����Ϊ�գ�");
			return false;
			}
//			if(document.all.item("jiaqfsj",i).value==''){
//			alert("<���ۿ�>��ǩҳ�е�<"+(i+1)+">��<��Ȩ��ʽ>����Ϊ�գ�");
//			return false;
//			}
			if(document.all.item("yunsfsj",i).value==''){
			alert("<���ۿ�>��ǩҳ�е�<"+(i+1)+">��<���䷽ʽ>����Ϊ�գ�");
			return false;
			}
			
		}		
	}
	return true;
}
 function chekRepear(){
	if(!submited){
		submited=true;
		return true;
	}else{
		return false;
	}
 }	