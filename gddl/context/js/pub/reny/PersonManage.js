window.onresize=function(){
	editMainDiv.style.height=bodyHeight-100;
	editMainDiv.style.width=bodyWidth/2-10;
	editMainDiv.style.posTop=0;
	
	treeMainDiv.style.height=bodyHeight-100;
	treeMainDiv.style.width=bodyWidth/2-10;
	treeMainDiv.style.posTop=0;
	
	editBodyTable.style.borderRight="gray 1px solid";
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
