

window.onresize=function(){



	var tName = "MainTable";
	if(xianszt == false){
		SetDisabled(tName,false);
		//ShowButtons(1);
	}else{
		SetDisabled(tName,true);
		//ShowButtons(0);

}
}

function ShowButtons(_type){
//_type为1时表示有数据为0时表述没有数据
if(_type==1){
	document.all.item('SaveButton').style.display="";
}else{
	document.all.item('SaveButton').style.display="none";
}}
body.onload =function(){
}

//function getTab(flag) {
//  var tags=e("tags/li");
//  var contents=e("tagContent/div");
//  for(var i=0;i<tags.length;i++)
//    if(tags[i].className){
//      tags[i].className="";
//      contents[i].className="tagContent";
//      break;
//      }
//  tags[flag].className="selectTag";
//  contents[flag].className+=" selectTag";
//  function e(a){
//    a=a.split("/");
//    a[1]=a[1].toUpperCase();
//    var t=document.getElementById(a[0]).childNodes;
//    var ret=[];
//    for(var i=0;i<t.length;i++)
//      if(t[i].tagName==a[1])
//        ret.push(t[i]);
//    return ret;
//    }
//}

function SetDisabled(tName,enable){
	var obj;
	obj = document.getElementById(tName);
	var aReturn;
	var l ;
	var i ;
	aReturn =obj.getElementsByTagName("INPUT");
	
	l=aReturn.length;
	for (i=0;i<l;i++){
		var v;
		v = aReturn[i].disabled = enable;
	}
     
//	aReturn =obj.getElementsByTagName("SELECT");
//	l=aReturn.length;
//	for (i=0;i<l;i++){
//		var v;
//		v = aReturn[i].disabled = enable;
//	}
//
//	aReturn =obj.getElementsByTagName("TEXTAREA");
//	l=aReturn.length;
//	for (i=0;i<l;i++){
//		var v;
//		v = aReturn[i].disabled = enable;
//	}
}




function SetXianszt(strLEIB,obj){
	//alert(strLEIB)
	if(strLEIB=='大供货单位'){
		document.all.item('FUIDSelectTitle').style.display = "none";
		document.all.item('FUIDSelect').style.display = "none";
		obj.hide();
	}else{
		document.all.item('FUIDSelectTitle').style.display = "";
		document.all.item('FUIDSelect').style.display = "";
		obj.show();
	}
}


