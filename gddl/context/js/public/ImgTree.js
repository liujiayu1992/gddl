var operateNotes = "";
var LastNode;
function OpenOrClose(currentObjTd){
	imgName = currentObjTd.childNodes[0].name;
	operateNotes += "O,"+currentObjTd.id+";";
	if(imgName.indexOf("plus") != -1){
		openChild(currentObjTd);
	}else
		if(imgName.indexOf("minus") != -1){	
			closeChild(currentObjTd);
		}else
			if(imgName.indexOf("blank") != -1){
				ClickNode(currentObjTd.nextSibling);
			}
}

function openChild(currentObjTd){
	var currentObjTr = currentObjTd.parentElement;
	var currentTable = currentObjTr.parentElement;
	var ShowText = currentObjTr.cells[1].innerText.replace(/(^\s*)|(\s*$)/g, "");
	var ArrIndex = eval(currentObjTr.cells[2].innerText.replace(/(^\s*)|(\s*$)/g, ""))+1;
	var CurNodeID = currentObjTr.cells[3].innerText.replace(/(^\s*)|(\s*$)/g, "");
	var InsertIndex = currentObjTr.rowIndex+1;
	if(ArrIndex >= Tree.length){
		//currentObjTd.innerText = "-";
		currentObjTd.innerHTML = getImgPathHtml("minus.gif") + getImgPathHtml("folderOpen.gif");
		return;
	}
	for(i=0;i<Tree[ArrIndex].length;i++){
		if(Tree[ArrIndex][i][0]==CurNodeID){
			var newTr = currentTable.insertRow(InsertIndex++);
			var newTd0 = newTr.insertCell(0);
			newTd0.className = "ImageTd";
			var newTd = newTr.insertCell(1);
			newTd.innerHTML = ShowNode(ArrIndex,i);
			var Td2 = newTr.insertCell(2);
			Td2.innerHTML = ArrIndex;
			Td2.style.display = "none";
			Td3 = newTr.insertCell(3); 
			Td3.innerHTML = "Child";
			Td3.style.display = "none";
		}
	}
	currentObjTd.innerHTML = getImgPathHtml("minus.gif") + getImgPathHtml("folderOpen.gif");
	//currentObjTd.innerText = "-";
}
function closeChild(currentObjTd){
	var currentObjTr = currentObjTd.parentElement;
	var currentTable = currentObjTr.parentElement;
	var ArrIndex = eval(currentObjTr.cells[2].innerText.replace(/(^\s*)|(\s*$)/g, "")+1);
	for(i=currentObjTr.rowIndex+1;i<currentTable.rows.length;i++){
		NodeId= currentTable.rows[i].cells[3].innerText.replace(/(^\s*)|(\s*$)/g, "");
		if(NodeId=="Child"){
			currentTable.removeChild(currentTable.rows[i--]);
		}
	}
	currentObjTd.innerHTML = getImgPathHtml("plus.gif") + getImgPathHtml("folderClosed.gif");
	//currentObjTd.innerText = "+";
}

function ShowRoot(DivID){
	RootHtml = "<table class= Table cellPadding= 0 cellSpacing=0><tr>"
				+ "<td class= ImageTd  onmouseover = 'this.style.cursor=\"hand\"'"+
	 			" onclick='OpenOrClose(this)' id=root>"+getImgPathHtml("plus.gif")
	 			+getImgPathHtml("folderClosed.gif")
	 			+"</td><td onclick='ClickNode(this)'>"+Tree[0][2]
	 			+"</td><td style='display:none'>0</td><td style='display:none'>"+Tree[0][1]+"</td></tr></table>";
	document.getElementById(DivID).innerHTML = RootHtml;
	//ShowPlatform.innerHTML =RootHtml ; 
	//alert(Tree.length);
}

function ShowNode(ArrIndex,NodeRow){
	var ImgName = "blank.gif";
	var fileImgName = "leaf.gif"
	if(hasChild(ArrIndex,NodeRow)){
		ImgName = "plus.gif";
		fileImgName = "folderClosed.gif";
	}
	NodeHtml = "<table class= Table cellPadding= 0 cellSpacing=0><tr>"
	+"<td  class= ImageTd onmouseover = 'this.style.cursor=\"hand\"'"+
	" onclick='OpenOrClose(this)' id="+Tree[ArrIndex][NodeRow][1]+">"
	+getImgPathHtml(ImgName) + getImgPathHtml(fileImgName)
	+"</td><td onclick='ClickNode(this)'>"+Tree[ArrIndex][NodeRow][2]
	+"</td><td style='display:none'>"+ArrIndex+"</td><td style='display:none'>"
	+Tree[ArrIndex][NodeRow][1]+"</td></tr></table>";	
	return NodeHtml;
}

function hasChild(ArrIndex,NodeRow){
	var hasChild = false;
	var ArrNextIndex = eval(ArrIndex+1)
	if(ArrNextIndex >= Tree.length){
		return false;
	}
	ID = Tree[ArrIndex][NodeRow][1];
	for(iChild = 0; iChild < Tree[ArrNextIndex].length ; iChild ++){
		if(ID == Tree[ArrNextIndex][iChild ][0]){
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

function ClickNode(Node){
	if(LastNode!=null){
		LastNode.style.color="black";
	}
	LastNode = Node;
	operateNotes += "C,"+Node.previousSibling.id+";";
	Node.style.color = "red";
	NodeChange();
}

function NodeChange(){
	
}
