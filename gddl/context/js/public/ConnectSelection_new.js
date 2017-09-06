var TrRowHeght,trobj;
	for(i =0; i<drop.length;i++ ){
		RenderMenuHtml = '<div id="selectcontent'+i+'" style="z-index:1;position:absolute;overflow:hidden;visibility:hidden;border:windowframe">'+
		'<iframe id=selframe'+i+' frameborder='+i+' height=100%></iframe>'+
		'<div id=formcontent'+i+' style="position: absolute;top:0;left:0;overflow:auto;border:1px solid #000000;height:100%;width:100%;"></div></div>';
		document.write(RenderMenuHtml);	
	}
/*
RenderMenuHtml = '<div id="selectcontent1" style="z-index:1;position:absolute;overflow:hidden;visibility:hidden;border:windowframe">'+
	'<iframe id=selframe1 frameborder=0 height=100%></iframe>'+
	'<div id=formcontent1 style="position: absolute;top:0;left:0;overflow:auto;border:1px solid #000000;height:100%;width:100%;"></div></div>';
document.write(RenderMenuHtml);	
RenderMenuHtml = '<div id="selectcontent2" style="z-index:1;position:absolute;overflow:hidden;visibility:hidden;border:windowframe">'+
	'<iframe id=selframe2 frameborder=0 height=100%></iframe>'+
	'<div id=formcontent2 style="position: absolute;top:0;left:0;overflow:auto;border:1px solid #000000;height:100%;width:100%;"></div></div>';
document.write(RenderMenuHtml);	*/

	function show(index,fuid,inputname){
	    var vHTML='<table id=htmltable'+index+' cellspacing="0" cellpadding="2" bgcolor="#ffffff" '+
	    'style="font-family:arial;font-size:14px;cursor:default;background-color:lightblue"  width=100%>';
	    //+'<tr><td><input onclick="alert(this.id)" id=re type=text /></td></tr>';//inactivecaption , menu ,#D4DAE8;
	    var allobj = drop[index];
	    var inputobj = getObject(inputname);
	    var inputValue = inputobj.value;
	    var tableobj = allobj[1];
	    var trHTML = '';
	    nextindex = index+1;
	    for(m=0;m<tableobj.length;m++){
		    if(tableobj[m][0]==fuid || fuid ==0){
	    		if(drop.length != nextindex){
			    	trHTML += '<tr height=20px onmousedown="md('+index+','+m+',\''+inputname+'\')" onmouseover="mo(this);show('+nextindex +','+tableobj[m][1]+',\''+inputname+'\')" onmouseout="mu(this)" ><td nowrap>'+tableobj[m][2];
			    }else{
			    	trHTML += '<tr height=20px onmousedown="md('+index+','+m+',\''+inputname+'\')" onmouseover="mo(this);" onmouseout="mu(this)" ><td nowrap>'+tableobj[m][2];
			    }
			}
		}
		if(trHTML==''){
			return;
		}
		vHTML+= trHTML + "</table>";
//		alert(vHTML);
		inputobj.style.position="relative";//"absolute";"relative"
		popdiv = document.all.item('selectcontent'+index);
		popcontent = document.all.item('formcontent'+index);
		popcontent.innerHTML=vHTML;
		poptable = document.all.item('htmltable'+index);
		var parObject = inputobj;
		var offsetnum = 0;
		var offsetLnum = 0;
		while(parObject.tagName != "BODY"){
			offsetnum = offsetnum+ parObject.offsetTop - parObject.scrollTop;
			offsetLnum = offsetLnum + parObject.offsetLeft - parObject.scrollLeft;
			parObject = parObject.offsetParent;
		}
		if(fuid==0){
			popdiv.style.posLeft  = offsetLnum;//inputobj.offsetLeft+
			popdiv.style.posTop   = inputobj.offsetHeight+offsetnum+2;
			popdiv.style.posWidth = 180;//poptable.offsetWidth; //inputobj.offsetWidth;
		}else{
			parentindex = eval(index-1);
			parentpopdiv = document.all.item('selectcontent'+parentindex);
			parentpopform = document.all.item('formcontent'+parentindex);
			parentpoptable = document.all.item('htmltable'+parentindex);
			popdiv.style.posLeft = parentpopdiv.style.posLeft + parentpopdiv.style.posWidth-1;
			if(TrRowHeght != null){
				popdiv.style.posTop = parentpopdiv.style.posTop + TrRowHeght +1 - parentpopform.scrollTop;
				//TrRowHeght =0;
			}else{
				//alert();
				popdiv.style.posTop = parentpopdiv.style.posTop;
			}
			//alert(popdiv.style.posTop);
			popdiv.style.posWidth = 180;//poptable.offsetWidth;//parentpopdiv.style.posWidth;
		}
		if(poptable.offsetHeight>240){
			if(poptable.scrollWidth > popdiv.style.posWidth){
				popdiv.style.posHeight = 260;
			}else{
				popdiv.style.posHeight = 240;
			}
		}else{
			if(poptable.scrollWidth > popdiv.style.posWidth){
				popdiv.style.posHeight = poptable.offsetHeight+20;
			}else{
				popdiv.style.posHeight = poptable.offsetHeight+2;
			}
		}
		
		
		if(TrRowHeght + inputobj.offsetHeight +offsetnum + popdiv.style.posHeight+20 > body.clientHeight){
			if(popdiv.style.posTop - popdiv.style.posHeight >0){
				if(fuid==0){
					popdiv.style.posTop = popdiv.style.posTop - popdiv.style.posHeight - inputobj.offsetHeight -5;
				}else{
					if(poptable.scrollWidth > popdiv.style.posWidth){
						popdiv.style.posTop = popdiv.style.posTop - popdiv.style.posHeight + inputobj.offsetHeight +25;
					}else{
						popdiv.style.posTop = popdiv.style.posTop - popdiv.style.posHeight + inputobj.offsetHeight +5;
					}
					
				}
				
			}else{
				//alert("意外2");
			}
		}else{
			//alert(body.clientHeight);
		}
		TrRowHeght = 0;
		popdiv.style.visibility="visible"
	}
	
	function showfilter(index,fuid,inputname){
	    var vHTML='<table id=htmltable'+index+' cellspacing="0" cellpadding="2" bgcolor="#ffffff" '+
	    'style="font-family:arial;font-size:14px;cursor:default;background-color:lightblue"  width=100%>';
	    //+'<tr><td><input onclick="alert(this.id)" id=re type=text /></td></tr>';//inactivecaption , menu ,#D4DAE8;
	    var allobj = drop[index];
	    var inputobj = getObject(inputname);
	    var inputValue = inputobj.value;
	    var tableobj = allobj[1];
	    var trHTML = '';
	    nextindex = index+1;
	    for(m=0;m<tableobj.length;m++){
		    if(tableobj[m][0]==fuid || fuid ==0){
		    	if(inputValue=="" || tableobj[m][3].indexOf(inputValue)!=-1){
		    		if(drop.length != nextindex){
				    	trHTML+='<tr height=20px onmousedown="md('+index+','+m+',\''+inputname+'\')" onmouseover="mo(this);show('+nextindex +','+tableobj[m][1]+',\''+inputname+'\')" onmouseout="mu(this)" ><td nowrap>'+tableobj[m][2];
				    }else{
				    	trHTML+='<tr height=20px onmousedown="md('+index+','+m+',\''+inputname+'\')" onmouseover="mo(this);" onmouseout="mu(this)" ><td nowrap>'+tableobj[m][2];
				    }
		    	}
			}
		}
		if(trHTML==''){
			return;
		}
		vHTML+= trHTML + "</table>";
//		alert(vHTML);
		inputobj.style.position="relative";//"absolute";"relative"
		popdiv = document.all.item('selectcontent'+index);
		popcontent = document.all.item('formcontent'+index);
		popcontent.innerHTML=vHTML;
		poptable = document.all.item('htmltable'+index);
		var parObject = inputobj;
		var offsetnum = 0;
		var offsetLnum = 0;
		while(parObject.tagName != "BODY"){
			offsetnum = offsetnum+ parObject.offsetTop - parObject.scrollTop;
			offsetLnum = offsetLnum + parObject.offsetLeft - parObject.scrollLeft;
			parObject = parObject.offsetParent;
		}
		if(fuid==0){
			popdiv.style.posLeft  = offsetLnum;//inputobj.offsetLeft+
			popdiv.style.posTop   = inputobj.offsetHeight+offsetnum+2;
			popdiv.style.posWidth = 180;//poptable.offsetWidth; //inputobj.offsetWidth;
		}else{
			parentindex = eval(index-1);
			parentpopdiv = document.all.item('selectcontent'+parentindex);
			parentpopform = document.all.item('formcontent'+parentindex);
			parentpoptable = document.all.item('htmltable'+parentindex);
			popdiv.style.posLeft = parentpopdiv.style.posLeft + parentpopdiv.style.posWidth-1;
			if(TrRowHeght != null){
				popdiv.style.posTop = parentpopdiv.style.posTop + TrRowHeght +1 - parentpopform.scrollTop;
				//TrRowHeght =0;
			}else{
				//alert();
				popdiv.style.posTop = parentpopdiv.style.posTop;
			}
			//alert(popdiv.style.posTop);
			popdiv.style.posWidth = 180;//poptable.offsetWidth;//parentpopdiv.style.posWidth;
		}
		if(poptable.offsetHeight>240){
			if(poptable.scrollWidth > popdiv.style.posWidth){
				popdiv.style.posHeight = 260;
			}else{
				popdiv.style.posHeight = 240;
			}
		}else{
			if(poptable.scrollWidth > popdiv.style.posWidth){
				popdiv.style.posHeight = poptable.offsetHeight+20;
			}else{
				popdiv.style.posHeight = poptable.offsetHeight+2;
			}
		}
		
		
		if(TrRowHeght + inputobj.offsetHeight +offsetnum + popdiv.style.posHeight+20 > body.clientHeight){
			if(popdiv.style.posTop - popdiv.style.posHeight >0){
				if(fuid==0){
					popdiv.style.posTop = popdiv.style.posTop - popdiv.style.posHeight - inputobj.offsetHeight -5;
				}else{
					if(poptable.scrollWidth > popdiv.style.posWidth){
						popdiv.style.posTop = popdiv.style.posTop - popdiv.style.posHeight + inputobj.offsetHeight +25;
					}else{
						popdiv.style.posTop = popdiv.style.posTop - popdiv.style.posHeight + inputobj.offsetHeight +5;
					}
					
				}
				
			}else{
				//alert("意外2");
			}
		}else{
			//alert(body.clientHeight);
		}
		TrRowHeght = 0;
		popdiv.style.visibility="visible"
	}
	
	function onfilter(index,inputObj){
		if(event.keyCode == 13 ||event.keyCode == 9){
			event.returnValue=false;
			showfilter(index,0,inputObj.name);
		}else{
    		event.returnValue=true;
    		showfilter(index,0,inputObj.name);
    	}
	}
	
	function getObject(str){
		var row,n;
		n=str.indexOf("$");
		if(n!=-1){
			strn =str.substring(0,n);
			row =eval(str.substring(n+1))+1;
		}else{
			strn =str;
			row =0;
		}
		return document.all.item(strn,row);
	}
	function getObjectRow(str){
		var row,n;
		n=str.indexOf("$");
		if(n!=-1){
			strn =str.substring(0,n);
			row =eval(str.substring(n+1))+1;
		}else{
			strn =str;
			row =0;
		}
		return row;
	}
	
	function md(index,tabrow,inputname){
		var id = tabrow;
		var row = getObjectRow(inputname);
		for (ifor=index;ifor>=0;ifor--){
			var allobj = drop[ifor];
	    	var inputobj = document.all.item(allobj[0],row);
	    	var tableobj = allobj[1];
	    	var parentid = tableobj[id][0];
	    	var value = tableobj[id][2];
	    	inputobj.value = value;
	    	
	    	if(ifor>0){
	    		var pallobj = drop[ifor-1];
	    		var ptableobj = pallobj[1];
	    		
	    		for(j=0;j<ptableobj.length;j++){
	    			if(ptableobj[j][1] == parentid){
	    				id = j;
	    				break;
	    			}
	    		}
	    	}
	    	inputobj.fireEvent("onchange");
		}
		for(ifor=index+1;ifor<drop.length;ifor++){
			var allobj = drop[ifor];
	    	var inputobj = document.all.item(allobj[0],row);
			inputobj.value = "";
		}
		for(i=0;i<drop.length;i++){
			popdiv = document.all.item('selectcontent'+i);
			popdiv.style.visibility = "hidden";
		}
		/*
		selectcontent0.style.visibility = "hidden";
		selectcontent1.style.visibility = "hidden";
		selectcontent2.style.visibility = "hidden";*/
	}
	function mo(obj){
		TrRowHeght = obj.rowIndex * obj.clientHeight;
		obj.style.backgroundColor="highlight";
		obj.style.color="#ffffff";
	}

	function mu(obj){
		obj.style.backgroundColor="lightblue";//buttonhighlight  ,lightblue
		obj.style.color="#000000";
	}
	
	document.onmousedown=function(){
	divHidden = true;
	for(i=0;i<drop.length;i++){
		if(event.srcElement.id == 'formcontent'+i){
			divHidden = false;
			break;
		}
	}
	for(i=0;i<drop.length;i++){
		popdiv = document.all.item('selectcontent'+i);
		if(popdiv.style.visibility=="visible"){
			mx=event.x + document.body.scrollLeft;
			my=event.y + document.body.scrollTop;
		
			x1=popdiv.offsetLeft;
			y1=popdiv.offsetTop;
		
			x2=popdiv.offsetLeft+selectcontent2.offsetWidth;
			y2=popdiv.offsetTop+selectcontent2.offsetHeight;
		
			if(divHidden){
				if (mx<x1 || my<y1 || x2<mx || y2<my) { 
					popdiv.style.visibility='hidden';
				}
			}
		}
	}
	/*
	if(event.srcElement.id == 'formcontent0' || event.srcElement.id == 'formcontent1'
	|| event.srcElement.id == 'formcontent2'){
		divHidden = false;
	}
		if(selectcontent2.style.visibility=="visible"){
			mx=event.x + document.body.scrollLeft;
			my=event.y + document.body.scrollTop;
		
			x1=selectcontent2.offsetLeft;
			y1=selectcontent2.offsetTop;
		
			x2=selectcontent2.offsetLeft+selectcontent2.offsetWidth;
			y2=selectcontent2.offsetTop+selectcontent2.offsetHeight;
		
			if(divHidden){
				if (mx<x1 || my<y1 || x2<mx || y2<my) { 
					selectcontent2.style.visibility='hidden';
				}
			}
		}
		if(selectcontent1.style.visibility=="visible"){
			mx=event.x + document.body.scrollLeft;
			my=event.y + document.body.scrollTop;
		
			x1=selectcontent1.offsetLeft;
			y1=selectcontent1.offsetTop;
		
			x2=selectcontent1.offsetLeft+selectcontent1.offsetWidth;
			y2=selectcontent1.offsetTop+selectcontent1.offsetHeight;
		
			if(divHidden){
				if (mx<x1 || my<y1 || x2<mx || y2<my) { 
					selectcontent1.style.visibility='hidden';
				}
			}
		}
		if(selectcontent0.style.visibility=="visible"){
			mx=event.x + document.body.scrollLeft;
			my=event.y + document.body.scrollTop;
		
			x1=selectcontent0.offsetLeft;
			y1=selectcontent0.offsetTop;
		
			x2=selectcontent0.offsetLeft+selectcontent0.offsetWidth;
			y2=selectcontent0.offsetTop+selectcontent0.offsetHeight;
			if(divHidden){
				if (mx<x1 || my<y1 || x2<mx || y2<my) { 
					selectcontent0.style.visibility='hidden';
				}
			}
		}*/
	}
