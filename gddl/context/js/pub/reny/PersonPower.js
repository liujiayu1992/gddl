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
		if(confirm("ȷ�ϲ����浱ǰ����������?")){
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
//		��ʾ:û�з����Ķ����豣��
//		return false;
	}
	document.getElementById("POWERCHANGE").value= PowerChangeValue;
	return true;
}