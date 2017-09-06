RenderMenuHtml = '<div id="selectcontent" style="z-index:2;position:absolute;overflow:hidden;visibility:hidden;border:windowframe">'+
	'<iframe id=selframe frameborder=0 height=100%></iframe>'+
	'<div id=formcontent style="position: absolute;top:0;left:0;overflow:auto;border:1px solid #000000;height:100%;width:100%;"></div></div>'+
	'<div id="MultiSelDiv" style="z-index:2;position:absolute;overflow:hidden;visibility:hidden;border:windowframe">'+
	'<iframe id="MultiSelframe" frameborder=0 height=100% ></iframe>'+
	'<div id="MulSelContent" style="position: absolute;overflow:hidden;top:0;left:0;border:1px solid #000000;width:100%;"></div></div>';
document.write(RenderMenuHtml);	

vDiv=selectcontent;
vDivHtml=formcontent;
var isIE = !!window.ActiveXObject;
var isIE6 = isIE && !window.XMLHttpRequest;
var isIE8 = isIE && !!document.documentMode;
var isIE7 = isIE && !isIE6 && !isIE8;
var isIEL=isIE6||isIE7||isIE8;
function RenderSelection(name,readonly,visibility,width,obj){
	var combo=this;
	this.options=new Array();
	this.name=name;
	this.divname=name+'_div';
	this.buttonname=name+'_button';
	this.tablename=name+'_table';
	this.htmltable=name+'_html';
	this.inputname=name+'_input';
	//if (!height) this.height=0; else this.height=height
	//if (!size) size=8;
	if (!readonly || readonly==0) {readonly="" }else {if (readonly==1) readonly="readonly style=cursor:default";this.readonly=1}
  	if(!visibility || visibility==0){visibility="hidden";}else{visibility="visible"}
	RenderFromHtml = '<div id='+this.divname+' style="z-index:2;visibility:'+visibility+';overflow:hidden;position:absolute;">'+
	'<table id='+this.tablename+' cellpadding=0 cellspacing=0 style="border-style:inset;border-width:0.02cm;">'+
	'<tr style="BORDER-TOP: gray 1px solid;"><TD style="border-width:0.03cm; BORDER-TOP: gray 1px solid;BORDER-LEFT: gray 1px solid;">'+
	'<input id='+this.inputname+' type=text '+readonly+' style="border:0px;height: 16;font-family:arial;font-size:12px;background-color:menu;" name='+name+'>'+
	'<td style="BORDER-TOP: gray 1px solid;BORDER-RIGHT: gray 1px solid;BORDER-BOTTOM: gray 1px solid; ">'+
	'<div style="background-color:#ECE9D8;height:17;width:17;">'+
	'<input id='+this.buttonname+' type=button value="6" style="font-family:webdings;font-size:12px;height:16;width: 16; line-height:0.09cm;background-color:buttonface;border:#ffffff 1px solid;position:relative;top:1;left:1;" /></div>'+
	'</td></tr></table></div>';
	if(obj!=""){
	    pobj = obj.parentElement
		pobj.innerHTML= pobj.innerHTML+ RenderFromHtml;		
	}else{
		document.write(RenderFromHtml);
	}
	if (width)eval(document.getElementById(combo.inputname)).style.posWidth = width-20;
	//this.sbutton=eval("document.all."+this.buttonname);
  	if (this.readonly==1) eval(document.getElementById(combo.inputname)).onclick=function(){combo.show()} 
  	eval(document.getElementById(this.buttonname)).onclick=function(){combo.show()}
	
	this.tronclick = 'onclick="document.getElementById(\''+this.inputname+'\').value=this.innerText;'+
					 'vDiv.style.visibility=\'hidden\';'+
					 'document.getElementById(\''+this.inputname+'\').fireEvent(\'onchange\');"';
	this.show=function(){
	    pDiv=eval(combo.divname)
	    pTable=eval(combo.tablename)
	    var vHTML='<table id=htmltable cellspacing="0" cellpadding="2" bgcolor="#ffffff" '+
	    'style="font-family:arial;font-size:12px;cursor:default;background-color:menu;"  width=100%>';
	    for (i=0;i<combo.options.length;i++){
			vHTML+='<tr onmouseover="mo(this)" onmouseout="mu(this)" '+this.tronclick+'><td nowrap>'+combo.options[i]
		}
		vHTML+="</table>";
	    vDiv.style.posLeft  = pDiv.offsetLeft;
		vDiv.style.posTop   = pDiv.offsetTop+pDiv.offsetHeight;
		vDiv.style.posWidth = pDiv.offsetWidth;
		vDivHtml.innerHTML=vHTML;
		if(htmltable.offsetHeight>200){
			vDiv.style.posHeight = 200;
		}else{
			vDiv.style.posHeight = htmltable.offsetHeight+2;
		}
		vDiv.style.visibility="visible"
	}
  
	this.add=function(text){
		combo.options[combo.options.length]=text;
		document.getElementById(this.inputname).value = combo.options[0];
	}
	this.addall=function(Wdrop){
		for(i=0;i<Wdrop.length;i++){
			if(isIEL){
                combo.options[combo.options.length]=Wdrop.options(i).text;
			}else {
                combo.options[combo.options.length]=Wdrop.options[i].text;
			}

		}
		if(isIEL){
            document.getElementById(this.inputname).value = Wdrop.options(Wdrop.selectedIndex).text;
		}else {
            document.getElementById(this.inputname).value = Wdrop.options[Wdrop.selectedIndex].text;
		}

	}
	eval(document.getElementById(combo.inputname)).onchange=function(){
		combo.onchange();
	}
	this.onchange = function(){
		
	}
	
	//this.attachEvent = function(sevent,fun){
	//	eval(this.inputname).attachEvent(sevent,fun);
	//}
	this.setTop = function(top){
		eval(combo.divname).style.posTop = top;
		//alert(eval(combo.divname).style.posTop+"--------"+top);
	}
	this.setLeft = function(left){
		eval(combo.divname).style.posLeft = left;
	}
	this.setHeight = function(height){
		eval(combo.divname).style.posHeight = height;
	}
	this.setWidth = function(width){
		eval(document.getElementById(combo.inputname)).style.posWidth = width-20;
	}
	this.setPosition = function(position){
		eval(combo.inputname).style.position = position;
	}
	//eval(document.getElementById(combo.inputname)).focus();
}

function mo(obj){
obj.style.backgroundColor="highlight";
obj.style.color="#ffffff";
}

function mu(obj){
obj.style.backgroundColor="menu";
obj.style.color="#000000";
}
function showSelection(activeobj,newobjname,selection){
		activeobj.style.position="relative";//"absolute";"relative"
		var top = activeobj.offsetTop;
		var left = activeobj.offsetLeft;
		var height = activeobj.offsetHeight;
		var width = activeobj.offsetWidth;
	if(document.getElementById(newobjname)!=null){
		document.getElementById(newobjname+'_div').style.visibility='visible';//"visible";"hidden"
	}else{
		newname=new  RenderSelection(newobjname,0,1,0,activeobj);
		newname.addall(selection);
	}	
		newname.setTop(top);
		newname.setLeft(left);
		newname.setHeight(height);
		newname.setWidth(width);
		newname.onchange = function(){
			SelectionChange();
		}
}
function SelectionChange(){
}


function MultiSel(){
	var MultiObj=this;
	this.options=new Array();
	this.size =10;
	this.srcElement = null;
	
	this.show=function(){
		buttonClick="";
		if(this.srcElement != null){
			buttonClick = 'onclick="MulSelClick(\''+this.srcElement+'\')"';
		}
		var vHTML='<table width=100% height=100% cellspacing="0" cellpadding="0" bgcolor="#ffffff"'+ 
		'style="font-family:arial;font-size:12px;cursor:default;background-color:menu;">'+
		'<tr><td><select id="MultiSelect" Multiple size="'+this.size+'" width="100%">';
	    for (i=0;i<MultiObj.options.length;i++){
			vHTML+='<option>'+MultiObj.options[i] + '</option>'
		}
		vHTML+='</select></td></tr><tr><td><button '+buttonClick+' style="width=100%;height:20px;">Ñ¡      Ôñ</button></td></tr></table>';
		MulSelContent.innerHTML=vHTML;
		//MultiSelDiv.style.visibility= "";
	}
	
	this.add=function(text){
		MultiObj.options[MultiObj.options.length]=text;
		//document.getElementById(this.inputname).value = combo.options[0];
	}
	this.addall=function(Wdrop){
		for(i=0;i<Wdrop.length;i++){
			if(isIEL){
                MultiObj.options[MultiObj.options.length]=Wdrop.options(i).text;
			}else {
                MultiObj.options[MultiObj.options.length]=Wdrop.options[i].text;
            }

		}
		//document.getElementById(this.inputname).value = Wdrop.options(Wdrop.selectedIndex).text;
	}
	this.setWidth = function(width){
		eval(MulSelContent).style.posWidth = width;
	}
	this.setSize = function(size){
		this.size = size;
	}
	this.setSrcElement = function(em){
		this.srcElement = em.name;
		//alert(em.name);
	}
}
function PointSite(iX, iY,ipHeight)
{
	this.x = iX;
	this.y = iY;
	this.h = ipHeight;
}

function getPointSite(aTag)
{
   var oTmp = aTag;
   var point = new PointSite(0,0,0);
   //var divWidth=0;
   //var divtf=false;
   do 
   {
		if (oTmp.tagName =="DIV" && oTmp.style.overflow=="auto"){
			point.y =point.y - oTmp.scrollTop;
			point.x =point.x - oTmp.scrollLeft;
	//		divWidth=oTmp.offsetWidth;
	//		divtf =true;
			//oData.offsetWidth
		}
      point.x += oTmp.offsetLeft;
      point.y += oTmp.offsetTop;
      oTmp = oTmp.offsetParent;
   } 
   while (oTmp.tagName != "BODY");
   point.h =oTmp.offsetHeight;
   //if(divtf){
	//   if (eval(point.x + 189)>divWidth){
	//	   point.x=eval(divWidth -189);
	//   }
  // }
   return point;

}
function showMultiSel(srcElementobj,msname,srcDropObj){
	
	msdPoint = getPointSite(srcElementobj);
	msdHeight = Math.round(msdPoint.h / 3);
	mssize = srcDropObj.options.length;
	msheight = mssize * 20;
	msdWidth = srcElementobj.offsetWidth;
	
	MultiSelDiv.style.posLeft  = msdPoint.x;
	MultiSelDiv.style.posTop   = msdPoint.y + srcElementobj.offsetHeight;
	
	if(msheight>msdHeight-20){
		mssize = Math.round((msdHeight-20)/20);
		MultiSelDiv.style.posHeight = msdHeight;
	}else{
		MultiSelDiv.style.posHeight = eval(msheight+24);//msheight+20;
	}
	if(document.getElementById(msname)!=null){
		MultiSelDiv.style.visibility="hidden";
	}else{
		msname = new MultiSel();
	}
	msname.setSize(mssize);
	msname.addall(srcDropObj);
	//alert(srcElementobj.id);
	msname.setSrcElement(srcElementobj); 
	msname.show();
	if(document.getElementById("MultiSelect").offsetWidth>msdWidth){
		msdWidth = document.getElementById("MultiSelect").offsetWidth;
	}else{
		//alert(document.getElementById("MultiSelect").offsetWidth);
		document.getElementById("MultiSelect").style.posWidth = msdWidth;
	}
	MultiSelDiv.style.posWidth = msdWidth;
	MultiSelDiv.style.visibility="visible";
}
function MulSelClick(srcElementName){
//	 alert('56789');
	
	MulO = document.getElementById("MultiSelect");
	mulsize = MulO.options.length;
	var mulValue ="";
	for(mulindex= 0;mulindex < mulsize;mulindex++){
		if(isIEL){
            if(MulO.options(mulindex).selected == true){
                mulValue = mulValue + MulO.options(mulindex).text + ',';
            }
		}else {
            if(MulO.options[mulindex].selected == true){
                mulValue = mulValue + MulO.options[mulindex].text + ',';
            }
        }

	}
	if(mulValue.length == mulValue.lastIndexOf(',')+1){
		mulValue = mulValue.substring(0,mulValue.lastIndexOf(','));
	}
	//alert(srcElementName.id);
	srcElement = name2obj(srcElementName);
	//alert(srcElement);
	srcElement.value = mulValue;
	if(isIEL){
        srcElement.fireEvent("onchange");
	}else {
        srcElement.onchange;
	}

	MultiSelDiv.style.visibility = "hidden";
}	


document.onmousedown=function(){
	if (vDiv.style.visibility=="visible"){
		mx=event.x + document.body.scrollLeft;
		my=event.clientY + document.body.scrollTop;
		
		x1=vDiv.offsetLeft;
		y1=vDiv.offsetTop;
		
		x2=vDiv.offsetLeft+vDiv.offsetWidth;
		y2=vDiv.offsetTop+vDiv.offsetHeight;
		
		if (mx<x1 || my<y1 || x2<mx || y2<my){ 
			vDiv.style.visibility='hidden';
		}
	}
	if (MultiSelDiv.style.visibility=="visible"){
		mx=event.x + document.body.scrollLeft;
		my=event.clientY + document.body.scrollTop;
		//alert(event.clientY + "----" +document.body.scrollTop );
		x1=MultiSelDiv.offsetLeft;
		y1=MultiSelDiv.offsetTop;
		
		x2=MultiSelDiv.offsetLeft+MultiSelDiv.offsetWidth;
		y2=MultiSelDiv.offsetTop+MultiSelDiv.offsetHeight;
		//alert("X:"+mx+",Y:"+my+"---x1:"+x1+",x2:"+x2+"|y1:"+y1+"y2:"+y2);
		if (mx<x1 || my<y1 || x2<mx || y2<my){ 
		//alert(1);
			MultiSelDiv.style.visibility='hidden';
		}
	}
}