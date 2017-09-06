  mouseover=true   
  tableover=true
  tiaojianover=true
  var pleft
  var ptop
  var Scrolltop 
  var Scrollleft 
  function   coordinates(){   
	if (document.all.item("TdName")==null && document.all.item("tiaojian")==null){
	  	return
    }else{
    	if(document.all.item("tiaojian")!=null && document.all.item("TdName")!=null){
    		if(!tiaojian  || !TdName){
    			return
    		}else{
    			if (event.srcElement.id=="tiaojian" || event.srcElement.id=="TdName"){  
			  	  mouseover=true  
			  	  if(event.srcElement.id=="tiaojian"){
				  	  tiaojianover=true
					  pleft=document.getElementById('shezhi').style.pixelLeft   
					  ptop=document.getElementById('shezhi').style.pixelTop 
			  	  }else{
			  	  	  tableover=true
				  	  Scrolltop = document.all.item("SelectFrmDiv").scrollTop;
					  Scrollleft = document.all.item("SelectFrmDiv").scrollLeft;
			  	  }
				  //Scrolltop = document.all.item("SelectFrmDiv").scrollTop;
				  //Scrollleft = document.all.item("SelectFrmDiv").scrollLeft;
				  
				  xcoor=event.clientX   
				  ycoor=event.clientY   
				  document.onmousemove=moveImage 
	    		}
    		}
    		
		}else if(document.all.item("tiaojian")==null && document.all.item("TdName")!=null){
    		if (event.srcElement.id=="TdName"){  
	  		  mouseover=true  
	  	  
	  	  	  tableover=true
		  	  Scrolltop = document.all.item("SelectFrmDiv").scrollTop;
			  Scrollleft = document.all.item("SelectFrmDiv").scrollLeft;
			  xcoor=event.clientX   
			  ycoor=event.clientY   
			  document.onmousemove=moveImage   
		    }
    	}else if(document.all.item("tiaojian")!=null && document.all.item("TdName")==null){
    		if (event.srcElement.id=="tiaojian"){  
	  		  mouseover=true  
	  	  
	  	  	  tiaojianover=true
			  pleft=document.getElementById('shezhi').style.pixelLeft   
			  ptop=document.getElementById('shezhi').style.pixelTop 
			  xcoor=event.clientX   
			  ycoor=event.clientY   
			  document.onmousemove=moveImage   
		    }
    	}
    }	
 
  }   
     
  function   moveImage() {   
	  if   (mouseover&&event.button==1){   
	  		//alert("tiaojianover="+tiaojianover);
	  		//alert("tableover="+tableover);
		  if(tiaojianover==true){
		  	document.getElementById('shezhi').style.pixelLeft=pleft+event.clientX-xcoor   
		    document.getElementById('shezhi').style.pixelTop=ptop+event.clientY-ycoor 
		  }
		  if(tableover==true){
			  document.all.item("SelectFrmDiv").scrollLeft=Scrollleft-event.clientX+xcoor
			  document.all.item("SelectFrmDiv").scrollTop=Scrolltop-event.clientY+ycoor
		  }  
		  
	  	return   false   
	  }   
  }   
    
  function   mouseup(){   
  	mouseover=false  
  	tableover=false 
  	tiaojianover=false
  }   
	  document.onmousedown=coordinates   
	  document.onmouseup=mouseup 

function Shezhi(){//设置第一次显示页面的状态
	if(document.all.item("Zhuangt").value==1){
		document.all.item("shezhi").style.display="";
		SetDisabled('tablemain',true);  	
	}else if(document.all.item("Zhuangt").value==2 || document.all.item("Zhuangt").value==0){
		document.all.item("shezhi").style.display="none";
		SetDisabled('tablemain',false);  	
	}
}


function print_tool(objid){//得到report页的id
	var obj = parent.document.getElementById(objid);
	var printStr = "<title>??</title>";
	printStr += "<style media=\"print\">.noPrint {display:none;}<\/style>";
	printStr += "<style>body{background:#FFFFFF;font-size:12px;}<\/style>";
	printStr += "<style>\n";
	printStr += ".tdLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
   	printStr += ".tdRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
	printStr += ".tdNormal{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\\n";
	printStr += ".tdNoneLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNoneRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNone{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += "<\/style>\n";
	   		
	printStr += obj.innerHTML;
	var a = window.open("about:blank",null,"width=700,height=500,toolbar=yes,left=0,top=0");
	var abody = a.document.open();
	abody.write(printStr);
	abody.close();
	a.print();
	a.close();
	printStr = null;
	a = null;
}

function allPrint_local(objid){
	var obj = document.getElementById(objid);
	var printStr = "<title>??</title>";
	printStr += "<style media=\"print\">.noPrint {display:none;}<\/style>";
	printStr += "<style>body{background:#FFFFFF;font-size:12px;}<\/style>";
	printStr += "<style>\n";
	printStr += ".tdLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
   	printStr += ".tdRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
	printStr += ".tdNormal{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\\n";
	printStr += ".tdNoneLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNoneRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNone{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += "<\/style>\n";
   	
   	if(eval(document.getElementById("AllPages").value)>1){
   		spans = obj.getElementsByTagName("span");
	   	for(i=0;i<spans.length;i++){
	   		r = spans[i];
	   		r.style.display = "";
	   	}
		printStr += obj.innerHTML;
		CurrentPage = eval(document.getElementById("CurrentPage").value);
	   	for(i=0;i<spans.length;i++){
	   		r = spans[i];
	   		if(i+1==CurrentPage){
	   			r.style.display = "";
	   		}else{
	   			r.style.display = "none";
	   		}
	   	}
   	}else{
   		printStr += obj.innerHTML;
   	}
//   	printStr += "<script>\n";
//	printStr +=	"window.onload=function() {if(document.readyState=='complete'){window.print();window.close();}}\n";
//	printStr += "<\/script>\n";
	var a = window.open("about:blank",null,"width=700,height=500,toolbar=yes,left=0,top=0");
//	var abody = a.document.open();
//	abody.write(printStr);
//	abody.close();
//	if (a.attachEvent) {
//		a.attachEvent("onload", function() {a.print();});
//	} else if (a.addEventListener) {
//		a.addEventListener("onload", function() {a.print();});
//	}
//	a.print();
	a.document.body.innerHTML = printStr;
	(function(){if(!a.document.body) return setTimeout(arguments.callee,1);a.print();})();
	a.close();
	printStr = null;
	a = null;
	}

function exportExcel_tool(objid){ 
	var obj = parent.document.getElementById(objid);
	window.clipboardData.setData("Text",obj.innerHTML);
	var oXL = new ActiveXObject("Excel.Application"); 
	oXL.Application.Visible=true;
	var oWB = oXL.Workbooks.Add(); 
	var oSheet = oWB.Worksheets(1);
	try{	
		oSheet.Paste();
 	}catch(e){
 		alert("导出失败");
 	}
} 

function print_local(objid){
	var obj = document.getElementById(objid);
	var printStr = "<title>??</title>";
	printStr += "<style media=\"print\">.noPrint {display:none;}<\/style>";
	printStr += "<style>body{background:#FFFFFF;font-size:12px;}<\/style>";
	printStr += "<style>\n";
	printStr += ".tdLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
   	printStr += ".tdRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
	printStr += ".tdNormal{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\\n";
	printStr += ".tdNoneLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNoneRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNone{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += "<\/style>\n";
	printStr += "<body>\n";		
	printStr += obj.innerHTML;
	printStr += "<\/body>\n";
	//printStr += "<script>\n";
	//printStr +=	"window.onload=function() {window.print();window.close();}\n";
	//printStr += "<\/script>\n";
	var a = window.open("about:blank",null,"width=700,height=500,toolbar=yes,left=0,top=0");
//	var abody = a.document.open();
//	abody.write(printStr);
//	abody.close();
//	if (a.attachEvent) {
//		a.attachEvent("load", function(a) {a.print();});
//	} else if (a.addEventListener) {
//		a.addEventListener("load", function(a) {a.print();});
//	}
//	a.print();
//	a.close();
	a.document.body.innerHTML = printStr;
	(function(){if(!a.document.body) return setTimeout(arguments.callee,1);a.print();})();
	a.close();
	printStr = null;
	a = null;
}


function exportExcel(objid){ 
	var obj = document.getElementById(objid);
	window.clipboardData.setData("Text",obj.innerHTML);
	var oXL = new ActiveXObject("Excel.Application"); 
	oXL.Application.Visible=true;
	var oWB = oXL.Workbooks.Add(); 
	var oSheet = oWB.Worksheets(1);
	try{	
		oSheet.Paste();
 	}catch(e){
 		alert("导出失败");
 	}
} 

function navigatorClick(strAction){ 
	var intCurrentPage=1;
	var intAllPages=1;
	var intOldPage=1;
	var intGoPage=1;
		
	intOldPage=eval(document.all.item("CurrentPage").value);
	intAllPages=eval(document.all.item("AllPages").value);
	
	if (strAction=='go'){
		try{
			intGoPage=eval(document.all.item("GoPage").value);
		}catch(e){
			alert("无效的页码!");
			return;
		}	
	
		if ((intGoPage>=1) && (intGoPage<=intAllPages)){
			if (intGoPage==intOldPage){
				return;
			}else{
				intCurrentPage=intGoPage;
			}
		}else{
			alert("页码超出范围!");
			return;
		}
	}else if (strAction=='first'){
		intCurentPage=1;
	}else if (strAction=='previous'){
		if  (intOldPage>1){
			intCurrentPage=intOldPage-1;
		}
	}else if (strAction=='next'){
		if (intOldPage<intAllPages){
			intCurrentPage=intOldPage+1;
		}else{
			intCurrentPage=intOldPage;
		}
	}else if (strAction=='last'){
		intCurrentPage=intAllPages;
	}

	if (intOldPage!=intCurrentPage){
		document.all.item("CurrentPage").value=intCurrentPage;
		setNavigatorButton();
		document.all.item("reportpage"+intCurrentPage).style.display="";
		document.all.item("reportpage"+intOldPage).style.display="none";
	}
} 

function navigatorClick_tool(strAction){ 
	var intCurrentPage=1;
	var intAllPages=1;
	var intOldPage=1;
	var intGoPage=1;
		
	intOldPage=eval(parent.document.all.item("CurrentPage").value);
	intAllPages=eval(parent.document.all.item("AllPages").value);
	if (strAction=='go'){
		try{
			intGoPage=eval(document.all.item("GoPage").value);
		}catch(e){
			alert("无效的页码!");
			return;
		}	
	
		if ((intGoPage>=1) && (intGoPage<=intAllPages)){
			if (intGoPage==intOldPage){
				return;
			}else{
				intCurrentPage=intGoPage;
			}
		}else{
			alert("页码超出范围!");
			return;
		}
	}else if (strAction=='first'){
		intCurentPage=1;
	}else if (strAction=='previous'){
		if  (intOldPage>1){
			intCurrentPage=intOldPage-1;
		}
	}else if (strAction=='next'){
		if (intOldPage<intAllPages){
			intCurrentPage=intOldPage+1;
		}else{
			intCurrentPage=intOldPage;
		}
	}else if (strAction=='last'){
		intCurrentPage=intAllPages;
	}

	if (intOldPage!=intCurrentPage){
		parent.document.all.item("CurrentPage").value=intCurrentPage;
		setNavigatorButton_tool();
		parent.document.all.item("reportpage"+intCurrentPage).style.display="";
		parent.document.all.item("reportpage"+intOldPage).style.display="none";
	}
} 

function setNavigatorButton_tool(){
	var intCurrentPage=0;
	var intAllPages=0;

	intCurrentPage=eval(document.all.item("CurrentPage").value);
	intAllPages=eval(document.all.item("AllPages").value);
	
	if (intCurrentPage<=1){
		//document.all.item("firstButton").disabled=true;//第一页
		//document.all.item("previousButton").disabled=true;//上一页
		document.all.item("FirstPage").style.display="none";	
		document.all.item("FirstPage_disabled").style.display="";	
		document.all.item("PreviousPage").style.display="none";	
		document.all.item("PreviousPage_disabled").style.display="";	
	}else{
		//document.all.item("firstButton").disabled=false;
		//document.all.item("previousButton").disabled=false;
		document.all.item("FirstPage").style.display="";	
		document.all.item("FirstPage_disabled").style.display="none";	
		document.all.item("PreviousPage").style.display="";	
		document.all.item("PreviousPage_disabled").style.display="none";	
	}
	
	if (intCurrentPage==intAllPages){
		//document.all.item("nextButton").disabled=true;//下一页
		//document.all.item("lastButton").disabled=true;//最后一页
		document.all.item("NextPage").style.display="none";	
		document.all.item("NextPage_disabled").style.display="";	
		document.all.item("LastPage").style.display="none";	
		document.all.item("LastPage_disabled").style.display="";	
	}else{
		//document.all.item("nextButton").disabled=false;
		//document.all.item("lastButton").disabled=false;
		document.all.item("NextPage").style.display="";	
		document.all.item("NextPage_disabled").style.display="none";	
		document.all.item("LastPage").style.display="";	
		document.all.item("LastPage_disabled").style.display="none";	 
	}

	document.all.item("pageNumber").innerText=intCurrentPage;
	document.all.item("totalPage").innerText=intAllPages;
	
	if (intAllPages==1){
		document.all.item("goButton").disabled=true;
	}
}	

function setNavigatorButton(){
	var intCurrentPage=0;
	var intAllPages=0;

	intCurrentPage=eval(document.all.item("CurrentPage").value);
	intAllPages=eval(document.all.item("AllPages").value);
	
	if (intCurrentPage<=1){
		//document.all.item("firstButton").disabled=true;//第一页
		//document.all.item("previousButton").disabled=true;//上一页
		document.all.item("FirstPage").style.display="none";	
		document.all.item("FirstPage_disabled").style.display="";	
		document.all.item("PreviousPage").style.display="none";	
		document.all.item("PreviousPage_disabled").style.display="";	
	}else{
		//document.all.item("firstButton").disabled=false;
		//document.all.item("previousButton").disabled=false;
		document.all.item("FirstPage").style.display="";	
		document.all.item("FirstPage_disabled").style.display="none";	
		document.all.item("PreviousPage").style.display="";	
		document.all.item("PreviousPage_disabled").style.display="none";	
	}
	
	if (intCurrentPage==intAllPages){
		//document.all.item("nextButton").disabled=true;//下一页
		//document.all.item("lastButton").disabled=true;//最后一页
		document.all.item("NextPage").style.display="none";	
		document.all.item("NextPage_disabled").style.display="";	
		document.all.item("LastPage").style.display="none";	
		document.all.item("LastPage_disabled").style.display="";	
	}else{
		//document.all.item("nextButton").disabled=false;
		//document.all.item("lastButton").disabled=false;
		document.all.item("NextPage").style.display="";	
		document.all.item("NextPage_disabled").style.display="none";	
		document.all.item("LastPage").style.display="";	
		document.all.item("LastPage_disabled").style.display="none";	
	}
	
	document.all.item("pageNumber").innerText=intCurrentPage;
	document.all.item("totalPage").innerText=intAllPages;
	
	if (intAllPages==1){
		document.all.item("goButton").disabled=true;
	}
}
function classchange(_str1,_str2,obj){
if(typeof(obj) != 'object'){
	return;
}
var rows=obj.rows ;
	if(_str2=="onclick"){
		for(var r=0;r<rows.length;r++){
			if(obj.rows(r).className="edittableTrClick"){
				obj.rows(r).className="edittableTrOut";
			}
		}
//		if (oldClickrow !=-1 && oldClickrow<=rows.length){
//			obj.rows(oldClickrow).className="edittableTrOut";
//		}
		oldClickrow=_str1.rowIndex;
		obj.rows(oldClickrow).className="edittableTrClick";
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
//	document.all.item("EditTableRow").value=oldClickrow;
}
function print2(objid){
	var obj = document.getElementById(objid);
	var printStr = "<title>????</title>";
	printStr += "<style media=\"print\">.noPrint {display:none;}<\/style>";
	printStr += "<style>body{background:#FFFFFF;font-size:12px;}<\/style>";
	printStr += "<style>\n";
	printStr += ".tdLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
   	printStr += ".tdRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\n";
	printStr += ".tdNormal{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);}\\n";
	printStr += ".tdNoneLeft{padding-left:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
  	printStr += ".tdNoneRight{padding-right:2; border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += ".tdNone{border-left:0px solid rgb(0,0,0); border-top:0px solid rgb(0,0,0); border-right:0px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);}\n";
   	printStr += "<\/style>\n";
	
	/*printStr += "<div style=\"border:0px solid gray; padding:2px; POSITION: absolute;z-index:1;BORDER-RIGHT: gray 1px solid; PADDING-RIGHT: 2px; BORDER-TOP: gray 1px solid; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; OVERFLOW: auto; BORDER-LEFT: gray 1px solid; PADDING-TOP: 2px; BORDER-BOTTOM: gray 1px solid; OVERFLOW: auto; width=750;height=550; left:0px; top:0px;\">";  */ 		
	printStr += obj.innerHTML;
	//printStr += "</div>";
	var a = window.open("about:blank",null,"width=750,height=550,toolbar=yes,scrollbars=yes,left=0,top=0");
	var abody = a.document.open();
	abody.write(printStr);
	abody.close();
//	a.print();
//	a.close();
//	printStr = null;
//	a = null;
}