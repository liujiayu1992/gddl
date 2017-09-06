window.onresize=function(){
	editMainDiv.style.height=bodyHeight-100;
	editMainDiv.style.width=bodyWidth-50;
	editMainDiv.style.posTop=0;
	
	editBodyTable.style.borderRight="gray 1px solid";
}
var LastCheckElement;
function CheckClick(srcElement){
	if(srcElement.checked){
		if(LastCheckElement != null && LastCheckElement != srcElement){
			LastCheckElement.checked = false;
		}
		LastCheckElement = srcElement;
	}
}
function editTab()
{
    var code, sel, tmp, r
    var tabs="";
    event.returnValue = false;
    sel =event.srcElement.document.selection.createRange();
    r = event.srcElement.createTextRange();
    switch (event.keyCode)
    {
        case (8)    :
            if (!(sel.getClientRects().length > 1))
            {
                event.returnValue = true;
                return;
            }
            code = sel.text;
            tmp = sel.duplicate();
            tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top);
            sel.setEndPoint("startToStart", tmp);
            sel.text = sel.text.replace(/^\t/gm, "");
            code = code.replace(/^\t/gm, "").replace(/\r\n/g, "\r");
            r.findText(code);
            r.select();
            break;
        case (9)    :
            if (sel.getClientRects().length > 1)
            {
                code = sel.text;
                tmp = sel.duplicate();
                tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top);
                sel.setEndPoint("startToStart", tmp);
                sel.text = "\t"+sel.text.replace(/\r\n/g, "\r\t");
                code = code.replace(/\r\n/g, "\r\t");
                r.findText(code);
                r.select();
            }
            else
            {
                sel.text = "\t";
                sel.select();
            }
            break
        case (13)    :
            tmp = sel.duplicate();
            tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top);
            tmp.setEndPoint("endToEnd", sel);

            for (var i=0; tmp.text.match(/^[\t]+/g) && i<tmp.text.match(/^[\t]+/g)[0].length; i++){    tabs += "\t";}
            sel.text = "\r\n"+tabs;
            sel.select();
            break;
        default        :
            event.returnValue = true;
            break;
    }
}