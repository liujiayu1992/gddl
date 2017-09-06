function _onscroll(_obj1,_obj2){
	document.all.item("EditInput").style.display="none";
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

function SelectRow(obj1){ 
//	alert("SelectRow"+obj1.rowIndex);
	document.all.item("EditTableRow").value=obj1.rowIndex;
//	alert(document.all.item("EditTableRow").value);
    Editrows =obj1.rowIndex;
}
var oldClickrow=-1;
var oldinput;
var objselectCol;
var oldSelectobj;
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
//    if (document.all.item("SelectStep").value==8){
		document.all.item("EditTableRow").value=oldClickrow;
//	}else {
//		document.all.item("EditTableRow").value=-1;
//	}
              
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



body.style.overflow="hidden";
bodyHeight =body.clientHeight;
bodyWidth =body.clientWidth;
bodyTop=body.clientTop;

tablemainHeight=bodyHeight-bodyTop -50;
tablemainWidth =bodyWidth -15;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
/*
SelectDataHeight=tablemainHeight - ConditionHeight  - 20;
SelectDataWidth=tablemainWidth ;
*/
//EditFrameClsssBegin
SelectDataHeight=tablemainHeight;
SelectDataWidth=tablemainWidth ;
EditDataWidth=tablemainWidth ;
EditDataHeight=tablemainHeight-SelectDataHeight-ConditionHeight -15;
//EditFrmDiv.style.position="relative";
//EditFrameClsssEnd
//*/

SelectFrmDiv.style.position="relative";

SelectHeadDiv.style.position="relative";
SelectDataDiv.style.position="relative";
trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

//Ñ¡Ôñ¿ò¼Ü
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
var intHeadWidth;
intHeadWidth =0;
SelectHeadDiv.scrollLeft=SelectDataDiv.scrollLeft;
	var headtableCols ;
	headtableCols =EditHeadTabel.cells.length ;
	if (oData.rows.length > 0){
		oData.style.borderRight="gray 1px solid";
		EditHeadTabel.style.borderRight="gray 1px solid";
		for (i =0 ;i< headtableCols;i++){
			tittleHead(i).style.posTop=3;
			tittleHead(i).style.posWidth=oData.cells(i).offsetWidth -4;
			EditHeadTabel.cells(i).style.width =oData.cells(i).offsetWidth +1 ;
		}
		for(i=0;i<oData.rows.length;i++){
			oData.rows(i).className="edittableTrOut";
		}
	}

}
function _changeXm(obj,obj1){
	var XiangmmcSelect=obj;
	var insertvalue;
	var radio=0;
	insertvalue=  XiangmmcSelect.options(XiangmmcSelect.selectedIndex).text;
	obj1.focus();
	sel =document.selection.createRange();
	sel.text=insertvalue ;
	sel.select();

}
function _Modulebar(imgRow){
	var context='/cpicrmis';
	document.all.item("TabbarSelect").value=imgRow;
	
	var OutLineID=document.all.item("imgbtn0");
	var TDid =TDBtn0;
	imgCount=OutLineID.length-1;
	if(imgCount==1){
		TDid.style.color="#336699";
		TDid.background=context + "/img/tab/img_formtab_back_on.gif";
		//alert(context);
		OutLineID(0).src="/img/tab/img_formtab_first_lefton.gif";
		OutLineID(1).src=context + "/img/tab/img_formtab_last_righton.gif";
		return;
	}
	for (i=0;i<(imgCount);i++){
		if(i>0){
			OutLineID(i).src=context + "/img/tab/img_formtab_middle_off.gif";
		}
		TDid(i).style.color="#888888";
		TDid(i).background=context + "/img/tab/img_formtab_back_off.gif";
		selectTab(i).style.display="none";
	}
	OutLineID(0).src=context + "/img/tab/img_formtab_first_leftoff.gif";
	OutLineID(imgCount).src=context + "/img/tab/img_formtab_last_rightoff.gif";//??
	//alert(context);
	TDid(imgRow).style.color="#336699";
	TDid(imgRow).background=context + "/img/tab/img_formtab_back_on.gif";
	if(imgRow ==0 ){//??
		OutLineID(0).src=context + "/img/tab/img_formtab_first_lefton.gif";
	}else{
		OutLineID(imgRow).src=context + "/img/tab/img_formtab_middle_righton.gif";
	}
	if((imgRow+1)!=imgCount){//??
		OutLineID(imgRow+1).src=context + "/img/tab/img_formtab_middle_lefton.gif";
	}else{
		OutLineID(imgRow+1).src=context + "/img/tab/img_formtab_last_righton.gif";
	}
	selectTab(imgRow).style.display="";
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

function editchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.value;
	}
}
function droponchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.options(oldSelectobj.selectedIndex).text;
		if (_int==1){
			updatarows(objselectCol.name);
		}
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
	for (i=0;i<rowscount;i++){
		document.all.item(strn,i).value=document.all.item(strn,row).value;
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

	_obj2.style.paddingTop=5;
                
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
function editclick(_obj1,_obj2){
	editTab();
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
	}

	_obj2.style.borderRight="#FC0303 1px solid";
	_obj2.style.borderLeft="#FC0303 1px solid";
	_obj2.style.borderTop="#FC0303 1px solid";
	_obj2.style.borderBottom="#FC0303 1px solid";
	_obj2.style.paddingTop=5;
                
	_obj2.style.posLeft =_obj1.parentElement.offsetLeft+2;
	_obj2.style.posTop=_obj1.parentElement.offsetTop  - 1;
	_obj2.style.posWidth=_obj1.parentElement.offsetWidth +1;
	_obj2.style.posHeight=_obj1.parentElement.offsetHeight +1;
                
	_obj2.style.position="absolute";
	_obj2.style.display="";
	objselectCol =_obj1;
	oldSelectobj=_obj2;
	_obj2.value=_obj1.value;
    _obj2.select();
	
}

function editTab()
{
    var code, sel, tmp, r
    var tabs="";
    event.returnValue = false;
    sel =event.srcElement.document.selection.createRange();
    r = event.srcElement.createTextRange();
    switch (event.keyCode)
    {
        case (8)    :
            if (!(sel.getClientRects().length > 1))
            {
                event.returnValue = true;
                return;
            }
            code = sel.text;
            tmp = sel.duplicate();
            tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top);
            sel.setEndPoint("startToStart", tmp);
            sel.text = sel.text.replace(/^\t/gm, "");
            code = code.replace(/^\t/gm, "").replace(/\r\n/g, "\r");
            r.findText(code);
            r.select();
            break;
        case (9)    :
            if (sel.getClientRects().length > 1)
            {
                code = sel.text;
                tmp = sel.duplicate();
                tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top);
                sel.setEndPoint("startToStart", tmp);
                sel.text = "\t"+sel.text.replace(/\r\n/g, "\r\t");
                code = code.replace(/\r\n/g, "\r\t");
                r.findText(code);
                r.select();
            }
            else
            {
                sel.text = "\t";
                sel.select();
            }
            break
       /* case (13)    :
            tmp = sel.duplicate();
            tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top);
            tmp.setEndPoint("endToEnd", sel);

            for (var i=0; tmp.text.match(/^[\t]+/g) && i<tmp.text.match(/^[\t]+/g)[0].length; i++){    tabs += "\t";}
            sel.text = "\r\n"+tabs;
            sel.select();
            break;*/
        default        :
            event.returnValue = true;
            break;
    }
}