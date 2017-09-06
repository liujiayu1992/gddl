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

Sethej();
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

tablemainHeight=bodyHeight-bodyTop -15;
tablemainWidth =bodyWidth -15;
ConditionHeight =20;
ConditionWidth=tablemainWidth ;
//QueryFrameClsssBegin
SelectDataHeight=tablemainHeight - ConditionHeight+6;
SelectDataWidth=tablemainWidth+15 ;
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

SelectHeadDiv.style.position="relative";
SelectDataDiv.style.position="relative";
trcondition.style.height=20;
tablemain.style.left=0;

tablemain.style.width=bodyWidth -15;
tablemain.style.height=tablemainHeight;

//????????
SelectData.style.height=SelectDataHeight;
SelectData.style.width=SelectDataWidth;

SelectFrmDiv.style.height=SelectDataHeight;
SelectFrmDiv.style.width=SelectDataWidth;
SelectFrmDiv.style.posTop=0;

SelectHeadDiv.style.posTop=0;
SelectHeadDiv.style.width=SelectDataWidth-10;
SelectHeadDiv.style.height=67;
SelectHeadDiv.style.posLeft=0;

SelectDataDiv.style.posTop=0;
SelectDataDiv.style.width=SelectDataWidth-7;
SelectDataDiv.style.height=SelectDataHeight-78;
SelectDataDiv.style.posLeft=-2;

if (SelectDataDiv.scrollHeight > SelectDataDiv.clientHeight){
	SelectHeadDiv.style.width=SelectDataWidth-25;
}


//??????????????????????????
SelectHeadDiv.scrollLeft=SelectDataDiv.scrollLeft;

	var headtableCols ;
	//alert(tittleHead.length);
	headtableCols =12;
	if (oData.rows.length > 0){
		oData.style.borderRight="gray 1px solid";
		EditHeadTabel.style.borderRight="gray 1px solid";
		for (i =0 ;i< headtableCols;i++){
			tittleHead(i).style.posWidth=oData.cells(i).offsetWidth -4;
			//document.all.item("tittleHead" + i).style.posTop=3;
			//document.all.item("tittleHead" + i).style.posWidth=oData.cells(i).offsetWidth -4;
			EditHeadTabel.cells(i).style.width =oData.cells(i).offsetWidth +1 ;
		}

		//????????Button??????????
		//ShowButtons(1);

	}else{
		//????????Button??????????
		//ShowButtons(0);
	}
	createTabIndex();
}

var oldClickrow=-1;
var oldinput;
var objselectCol;
var oldSelectobj;
var Editrows=-1;


//QueryFrameClsssBegin
function droponchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.options(oldSelectobj.selectedIndex).text;
		if (_int==1){
			//Form0.submit();
		}
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
			_str1.style.posHeight=_str1.parentElement.offsetHeight ; 
		}else{
			_str1.style.posTop=_str1.parentElement.offsetTop  -1;
			_str1.style.posHeight=_str1.parentElement.offsetHeight +1 ; 
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
	if(_str1.rowIndex!=eval(rows.length)-1){
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
		hej();
	}
}

function FormatInput(){
	var i=0;
	var objLength;
	//if (document.all.item("XUH")!=null){
	//	objLength=document.all.item("XUH").length;
		if (objLength>=1){
			for (i =0;i< objLength;i++){
				//document.all.item("XUH",i).value=Format(document.all.item("XUH",i).value,0);
			}
		}
//	}
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



function ShowButtons(_type){
//_type??1??????????????0??????????????
if(_type==1){
     document.all.item('DeleteButton').style.display="";
     document.all.item('SaveButton').style.display="";
     //document.all.item('CreateButton').style.display="none";
}else{
     document.all.item('DeleteButton').style.display="none";
     document.all.item('SaveButton').style.display="none";
     //document.all.item('CreateButton').style.display="";
}}
body.onload =function(){
     FormatInput();
}
        

function editclick(_obj1,_obj2){
	if(_obj1.name.substring(_obj1.name.indexOf('$')+1)!=6){
		if (oldSelectobj!=null){
			oldSelectobj.style.display="none";
		}
		_obj2.tabIndex=_obj1.tabIndex;
		
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
		_obj2.select();
	}
}
function editchange(_int){
	if (objselectCol !=null){
		objselectCol.value =oldSelectobj.value;
		if (_int==1){
			updatarows(objselectCol.name);
		}
	}
}

function show(){
	document.all.item("EditInput").select();
}

function Pingb(){
	if(event.keyCode==13){
		event.keyCode=0;
     	event.returnValue=false;	
     }else if(event.keyCode==8){
			if(event.srcElement.tagName!="INPUT"){
				event.keyCode=0;
		     	event.returnValue=false;
			}else if(event.srcElement.type!= "text"){
				event.keyCode=0;
		     	event.returnValue=false;				
			}else if(event.srcElement.readOnly){
				event.keyCode=0;
		     	event.returnValue=false;
			}		     
     }
	
}
function checkbeiz(){
	if(objselectCol.id=='BEIZ'){
		return false;
	}else{
		return true;
	}
}

function hej(){
	var size = oData.rows.length;
	var Rucslhj = 0;
	var Rucrlhj = 0;
	var Rucsfhj = 0;
	var Rulslhj = 0;
	var Rulrlhj = 0;
	var Rulsfhj = 0;
	for (var i=0;i<size-1;i++){
		//入厂煤
		var Rucsl=document.all.item("Rucsl",i).value;
		var Rucrl=document.all.item("Rucrl",i).value;
		var Rucsf=document.all.item("Rucsf",i).value;
		//入炉煤
		var Rulsl=document.all.item("Rulsl",i).value;
		var Rulrl=document.all.item("Rulrl",i).value;
		var Rulsf=document.all.item("Rulsf",i).value;
		
		document.all.item("Rezctzq",i).value=Round(eval(Rucrl)-eval(Rulrl),2);
		document.all.item("Rezctzqdk",i).value=Round(eval(Round(eval(Rucrl)-eval(Rulrl),2))*1000/4.1816,0);
		document.all.item("Rezctzh",i).value=Round(eval(Rucrl)-eval(Rulrl)*(100-eval(Rucsf))/(100-eval(Rulsf)),2);
		var Rezctzh=Round(eval(Rucrl)-eval(Rulrl)*(100-eval(Rucsf))/(100-eval(Rulsf)),2)
		document.all.item("Rezctzhdk",i).value=Round(eval(Rezctzh)*1000/4.1816,0);
		
		Rucslhj = eval(Rucslhj)+eval(Rucsl);
		Rucrlhj = eval(Rucrlhj) + eval(Rucrl)*eval(Rucsl);
		Rucsfhj = eval(Rucsfhj) + eval(Rucsf)*eval(Rucsl);
		Rulslhj = eval(Rulslhj)+eval(Rulsl);
		Rulrlhj = eval(Rulrlhj) + eval(Rulrl)*eval(Rulsl);
		Rulsfhj = eval(Rulsfhj) + eval(Rulsf)*eval(Rulsl);
		
	}
	document.all.item("Rucsl",size-1).value = Rucslhj;
	if(Rucslhj!=0){
		document.all.item("Rucrl",size-1).value = Round(eval(Rucrlhj)/eval(Rucslhj),2);
		document.all.item("Rucsf",size-1).value = Round(eval(Rucsfhj)/eval(Rucslhj),2);
	}else{
		document.all.item("Rucrl",size-1).value = 0;
		document.all.item("Rucsf",size-1).value = 0;
	}
	document.all.item("Rulsl",size-1).value = Rulslhj;
	if(Rulslhj!=0){
		document.all.item("Rulrl",size-1).value = Round(eval(Rulrlhj)/eval(Rulslhj),2);
		document.all.item("Rulsf",size-1).value = Round(eval(Rulsfhj)/eval(Rulslhj),2);
	}else{
		document.all.item("Rulrl",size-1).value = 0;
		document.all.item("Rulsf",size-1).value = 0;
	}
	document.all.item("Rezctzq",i).value=Round(eval(Round(eval(Rucrlhj)/eval(Rucslhj),2))-eval(Round(eval(Rulrlhj)/eval(Rulslhj),2)),2);
	document.all.item("Rezctzqdk",i).value=Round(eval(Round(eval(Round(eval(Rucrlhj)/eval(Rucslhj),2))-eval(Round(eval(Rulrlhj)/eval(Rulslhj),2)),2))*1000/4.1816,0);
	document.all.item("Rezctzh",i).value=Round(eval(Round(eval(Rucrlhj)/eval(Rucslhj),2))-eval(Round(eval(Rulrlhj)/eval(Rulslhj),2))*(100-eval(Round(eval(Rucsfhj)/eval(Rucslhj),2)))/(100-eval(Round(eval(Rulsfhj)/eval(Rulslhj),2))),2);
	var Rezctzh=Round(eval(Round(eval(Rucrlhj)/eval(Rucslhj),2))-eval(Round(eval(Rulrlhj)/eval(Rulslhj),2))*(100-eval(Round(eval(Rucsfhj)/eval(Rucslhj),2)))/(100-eval(Round(eval(Rulsfhj)/eval(Rulslhj),2))),2)
	document.all.item("Rezctzhdk",i).value=Round(eval(Rezctzh)*1000/4.1816,0);
}


function myMatchNum(obj1){
	
	var str;
	var id1 = objselectCol.id;;
	str=/^0\.[0-9]{1,2}|[1-9][0-9]{0,9}\.[0-9]{1,2}|0|[1-9][0-9]{0,9}$/;
	
	var Numr, Numre;
	var Nums = obj1.value;
	if(Nums.substring(0,1)=='.'){			//&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;'.'&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;'0.'
		Nums= 0 + Nums;	
	}
	Numre = str;  							// &#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;
	Numr = Nums.match(Numre);               // &#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533; s &#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;
	if(Nums != Numr){
		alert("请输入数字");
		obj1.select();
		
		if (Editrows!=-1){
			var UpperID;
			var Index;
			UpperID = (obj1.id).toUpperCase( ); 
			Index = (UpperID.indexOf("_EDIT"));
			if(Index > 0 ){
				obj1.value=document.all.item(((obj1.id).substring(0,Index)),Editrows).value;
			}else{
				Index = (UpperID.indexOf("EDIT"));
				obj1.value=document.all.item(((obj1.id).substring(0,Index)),Editrows).value;
			}
		}
		obj1.focus();						// &#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;
		return false;
	}else{									// &#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;						
		obj1.value = Nums;						// &#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;
		return true;
	}
	
}

function Sethej(){
	var size = oData.rows.length;
	for(var i=0;i<size;i++){
		if(document.all.item('riq',i).value=='合计'){
			oData.rows(i).className="edittableTrClick";
		}
	}
}

function createTabIndex(){
	j=0;
    for(i=0;i<oData.rows.length;i++){
         document.all.item("Rucsl",i).tabIndex=++j;
         document.all.item("Rucrl",i).tabIndex=++j;
         document.all.item("Rucsf",i).tabIndex=++j;
         document.all.item("Rulsl",i).tabIndex=++j;
         document.all.item("Rulrl",i).tabIndex=++j;
         document.all.item("Rulsf",i).tabIndex=++j;
         document.all.item("Beiz",i).tabIndex=++j;
    }

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
        case (13)    :
            tmp = sel.duplicate();
            tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top);
            tmp.setEndPoint("endToEnd", sel);

            for (var i=0; tmp.text.match(/^[\t]+/g) && i<tmp.text.match(/^[\t]+/g)[0].length; i++){    tabs += "\t";}
            sel.text = "\r\n"+tabs;
            sel.select();
            break;
        default        :
            event.returnValue = true;
            break;
    }
}

