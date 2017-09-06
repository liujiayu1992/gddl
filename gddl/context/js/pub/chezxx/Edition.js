window.onresize=function(){
	editMainDiv.style.height=bodyHeight-100;
	editMainDiv.style.width=bodyWidth-20;
	editMainDiv.style.posTop=0;
	if(xianszt == false){
		ShowButtons(0);
	}else{
		ShowButtons(1);
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
function dropdown(_obj1,_obj2){
	if (oldSelectobj!=null){
		oldSelectobj.style.display="none";
		_obj2.selectedIndex=-1;
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
//	if (_str2=="onfocus"){
//		_str1.select();
//		return;
//	}
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
function ShowButtons(_type){
//_type为1时表示有数据为0时表述没有数据
	if(_type==1){
	     document.all.item('DeleteButton').style.display="";
	}else{
	     document.all.item('DeleteButton').style.display="none";
	}
}
