var PowerChangeValue = "";
var operateNotes = "";

function CheckOpenOrClose(currentObjTd){
	imgName = currentObjTd.childNodes[0].name;
	operateNotes += "O,"+currentObjTd.id+";";
	if(imgName.indexOf("plus") != -1){
		CheckopenChild(currentObjTd);
	}else
		if(imgName.indexOf("minus") != -1){	
			CheckcloseChild(currentObjTd);
		}
//		else
//			if(imgName.indexOf("blank") != -1){
//				CheckboxClick(currentObjTd.nextSibling);
//			}
	setTdFont(currentObjTd);
}

function CheckopenChild(currentObjTd){
	var currentObjTr = currentObjTd.parentElement;
	var currentTable = currentObjTr.parentElement;
	var ShowText = currentObjTr.cells[1].innerText.replace(/(^\s*)|(\s*$)/g, "");
	var ArrIndex = eval(currentObjTr.cells[2].innerText.replace(/(^\s*)|(\s*$)/g, ""))+1;
	var CurNodeID = currentObjTr.cells[3].innerText.replace(/(^\s*)|(\s*$)/g, "");
	var InsertIndex = currentObjTr.rowIndex+1;
	if(ArrIndex >= CheckTree.length){
		currentObjTd.innerHTML = getImgPathHtml("minus.gif") + getImgPathHtml("folderOpen.gif");
		return;
	}
	for(i=0;i<CheckTree[ArrIndex].length;i++){
		if(CheckTree[ArrIndex][i][0]==CurNodeID){
			var newTr = currentTable.insertRow(InsertIndex++);
			newTr.insertCell(0);
			var newTd = newTr.insertCell(1);
			newTd.innerHTML = CheckShowNode(ArrIndex,i);
			Td2 = newTr.insertCell(2);
			Td2.innerHTML = ArrIndex;
			Td2.style.display = "none";
			Td3 = newTr.insertCell(3); 
			Td3.innerHTML = "Child";
			Td3.style.display = "none";
		}
	}
	currentObjTd.innerHTML = getImgPathHtml("minus.gif") + getImgPathHtml("folderOpen.gif");
}
function CheckcloseChild(currentObjTd){
	var currentObjTr = currentObjTd.parentElement;
	var currentTable = currentObjTr.parentElement;
	var ArrIndex = eval(currentObjTr.cells[2].innerText.replace(/(^\s*)|(\s*$)/g, "")+1);
	for(i=currentObjTr.rowIndex+1;i<currentTable.rows.length;i++){
		NodeId= currentTable.rows[i].cells[3].innerText.replace(/(^\s*)|(\s*$)/g, "");
		if(NodeId=="Child"){
			currentTable.removeChild(currentTable.rows[i--]);
		}
	}
	currentObjTd.innerHTML = getImgPathHtml("plus.gif")+ getImgPathHtml("folderClosed.gif");
}

function setTdFont(ObjTd){
	ObjTd.style.fontSize = "14px";
}

function CheckShowRoot(DivID){
	var Checked = "";
	Checked = CheckTree[0][3];
//	if(CheckTree[0][3]){
//		Checked = "checked";
//	}
	RootHtml = "<table><tr><td style='font-size: 14px;' onmouseover = 'this.style.cursor=\"hand\"'"+
	 			" onclick='CheckOpenOrClose(this)'>"+getImgPathHtml("plus.gif")+getImgPathHtml("folderClosed.gif")+"</td><td>"+"<input disabled type='checkbox' onclick='CheckboxClick(this)' "+Checked+"/>"+CheckTree[0][2]
	 			+"</td><td style='display:none'>0</td><td style='display:none'>"+CheckTree[0][1]+"</td></tr></table>";
	document.getElementById(DivID).innerHTML = RootHtml;
	//ShowPlatform.innerHTML =RootHtml ; 
	//alert(RootHtml );
}

function CheckShowNode(ArrIndex,NodeRow){
	var ImgName = "blank.gif";
	var fileImgName = "leaf.gif"
	var Checked = "";
	Checked = CheckTree[ArrIndex][NodeRow][3];
//	if(CheckTree[ArrIndex][NodeRow][3]){
//		Checked = "checked";
//	}
	if(hasChild(ArrIndex,NodeRow)){
		ImgName = "plus.gif";
		fileImgName = "folderClosed.gif";
	}
	NodeHtml = "<table><tr><td style='font-size: 14px;' onmouseover = 'this.style.cursor=\"hand\"'"
	+" onclick='CheckOpenOrClose(this)'>"+ getImgPathHtml(ImgName) + getImgPathHtml(fileImgName)
	+"</td><td><input onclick='CheckboxClick(this)' type='checkbox' "
	+Checked +"/>"+CheckTree[ArrIndex][NodeRow][2]
	+"</td><td style='display:none'>"+ArrIndex+"</td><td style='display:none'>"
	+CheckTree[ArrIndex][NodeRow][1]+"</td></tr></table>";	
	return NodeHtml;
}

function hasChild(ArrIndex,NodeRow){
	var hasChild = false;
	var ArrNextIndex = eval(ArrIndex+1)
	if(ArrNextIndex >= CheckTree.length){
		return false;
	}
	ID = CheckTree[ArrIndex][NodeRow][1];
	for(iChild = 0; iChild < CheckTree[ArrNextIndex].length ; iChild ++){
		if(ID == CheckTree[ArrNextIndex][iChild ][0]){
			return true;
		}
	}
	return false ;
}

function getImgPathHtml(ImgName){
	var url = "http://"+document.location.host+document.location.pathname;
	var end = url.indexOf("/app");
	url = url.substring(0,end);
	//url = url.replace("/app","");
	//alert(url);
	var ImgPath = "<img border=\"0\" name=\""+ImgName+"\" src=\""+url+"/imgs/tree/"+ImgName+"\" />";
	return ImgPath;
}

function CheckboxClick(box){
	var powerChange;
	var FindChange;
	var currentObjTd = box.parentElement;
	var currentObjTr = currentObjTd.parentElement;
	var currentTable = currentObjTr.parentElement;
	var ArrIndex = eval(currentObjTr.cells[2].innerText.replace(/(^\s*)|(\s*$)/g, ""));
	var CurNodeID = currentObjTr.cells[3].innerText.replace(/(^\s*)|(\s*$)/g, "");
	for(i=0;i < CheckTree[ArrIndex].length;i++){
		if(CheckTree[ArrIndex][i][1]==CurNodeID){
			if(box.checked){
				CheckTree[ArrIndex][i][3] = "checked";
			}else{
				CheckTree[ArrIndex][i][3] = "";
			}
        	if(box.checked){
        		powerChange = "+"+CheckTree[ArrIndex][i][1];
        		FindChange = "-"+CheckTree[ArrIndex][i][1];
        	}else{
        		powerChange = "-"+CheckTree[ArrIndex][i][1];
        		FindChange = "+"+CheckTree[ArrIndex][i][1];
        	}
        	var found = true;
        	var Powervalue = PowerChangeValue.split(",");
        	for(pi =0;pi<Powervalue.length;pi++){
        		if(Powervalue[pi] == FindChange){
        			Powervalue[pi] = powerChange;
        			found = false;
        		}
        	}
        	if(found){
        		PowerChangeValue += powerChange+",";
        	}else{
        		PowerChangeValue = Powervalue.join(",");
        	}
		}
	}
}
