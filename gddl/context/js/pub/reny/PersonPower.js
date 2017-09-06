window.onresize=function(){
	editMainDiv.style.height=bodyHeight-150;
	editMainDiv.style.width=250;
	editMainDiv.style.posTop=0;
	
	editBodyTable.style.borderRight="gray 1px solid";
}

function ReturnClick(){
	self.close();
}

function InfoChange(srcElement){
	oTr = srcElement.parentElement.parentElement;
	SelectClick(oTr);
}

function SelectClick(oTr){
	srcElement = oTr.cells(0).childNodes(0);
	if(!srcElement.checked){
		srcElement.checked = true;
	}
}
function CheckClick(srcElement){
	if(srcElement.checked){
		
	}else{
		if(confirm("确认不保存当前行所作更改?")){
//			srcElement.checked = false;
		}else{
			srcElement.checked = true;
		}
	}
}
function changeColor(oTr,color){
	oTr.style.color = color;
}
function SavePowerClick(){
	if(PowerChangeValue == ""){
//		提示:没有发生改动无需保存
//		return false;
	}
	document.getElementById("POWERCHANGE").value= PowerChangeValue;
	return true;
}