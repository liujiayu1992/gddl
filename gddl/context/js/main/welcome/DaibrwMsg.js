
var isIE = !!window.ActiveXObject;
var isIE6 = isIE && !window.XMLHttpRequest;
var isIE8 = isIE && !!document.documentMode;
var isIE7 = isIE && !isIE6 && !isIE8;

var PopWidth = 360; //��Ϣ����
var PopHeight = 280; //��Ϣ��߶�
var PopBorder = 25; //����Ļ��Ե�ľ���
var PopShow = 5000; //��Ϣ�����ʾʱ��
var PopTop =0;
var showtime, hidetime;

var oPopup ;
// if(isIE6||isIE7||isIE7){
    oPopup=window.createPopup();
// }else {
//     oPopup=window.open();
// }




//��Ϣ����ʾ����
function popshow()
{    
    //����������ʾ��Ϣ��ȫ��ʱ����С��Ϣ��߶�
    var tmpHeight = PopTop < PopHeight ? PopTop : PopHeight;
    
    //��Ϣ��λ��screen.width����Ļ��ȡ�screen.height����Ļ�߶�
    oPopup.show(screen.width - (PopWidth + PopBorder), screen.height - PopTop, PopWidth, tmpHeight);
   

    if (PopTop < (PopHeight + PopBorder))
    {
        PopTop = PopTop + 10; //��Ϣ��λ�õ���
      //  showtime = setTimeout("popshow();",100);
    } 
    else
    {
     //   setTimeout("pophide();", PopShow); //׼��������Ϣ��
    }

    showtime = setTimeout("popshow();",100);
}

//��Ϣ�����س���
function pophide()
{
    if (showtime) 
    {
        clearTimeout(showtime); //�����ʾʱ����
    }

    var tmpHeight = PopTop < PopHeight ? PopTop : PopHeight;

    oPopup.show(screen.width - (PopWidth + PopBorder), screen.height - PopTop, PopWidth, tmpHeight);

    if (PopTop > 0)
    {
        PopTop = PopTop - 10;
        hidetime = setTimeout("pophide();",100);
    } 
    else
    {
        clearTimeout(hidetime);
        oPopup.hide();          //��ȫ������Ϣ��
    }
}

function openNewPage(dcid){

var url = "http://"+document.location.host+document.location.pathname;
var end = url.indexOf(";");
url = url.substring(0,end);
url = url + "?service=page/Renwcx&lx="+dcid;        
window.open(url,"newWin");
		

}

function popmsg(tabrTitle,conTitle,msgstr,handler)
{

		

 var str = "<DIV style='BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX: 99999; LEFT: 0px; BORDER-LEFT: #a6b4cf 1px solid; WIDTH: " + PopWidth + "px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: " + PopHeight + "px; BACKGROUND-COLOR: #c9d3f3'>" 
        str += "<TABLE style='BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid' cellSpacing=0 cellPadding=0 width='100%' bgColor=#cfdef4 border=0>" 
        str += "<TR>" 
        str += "<TD style='FONT-SIZE: 12px;COLOR: #0f2c8c' width=30 height=24></TD>" 
        str += "<TD style='PADDING-LEFT: 4px; FONT-WEIGHT: normal; FONT-SIZE: 12px; COLOR: #1f336b; PADDING-TOP: 4px' vAlign=center width='100%'>" + tabrTitle+ "</TD>" 
        str += "<TD style='PADDING-RIGHT: 2px; PADDING-TOP: 2px' vAlign=center align=right width=19>" 
        str += "<SPAN  title=�ر� style='FONT-WEIGHT: bold; FONT-SIZE: 12px; CURSOR: hand; COLOR: red; MARGIN-RIGHT: 4px' id='btSysClose' >��</SPAN></TD>" 
        str += "</TR>" 
        str += "<TR>" 
        str += "<TD style='PADDING-RIGHT: 1px;PADDING-BOTTOM: 1px' colSpan=3 height=" + (PopHeight-28) + ">" 
        str += "<DIV style='BORDER-RIGHT: #b9c9ef 1px solid; PADDING-RIGHT: 8px; BORDER-TOP: #728eb8 1px solid; PADDING-LEFT: 8px; FONT-SIZE: 12px; PADDING-BOTTOM: 8px; BORDER-LEFT: #728eb8 1px solid; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 8px; BORDER-BOTTOM: #b9c9ef 1px solid; HEIGHT: 100%'>" + conTitle + "<BR><BR>" 
        str += "<DIV style='WORD-BREAK: break-all' align=left> "+ msgstr + " </DIV>" 
        str += "</DIV>" 
        str += "</TD>" 
        str += "</TR>" 
        str += "</TABLE>" 
        str += "</DIV>" 






    oPopup.document.body.innerHTML = str;

    //��Ϣ�����ʽ

    popshow();
    //oPopup.document.body.onclick = pophide; //������Ϣ��ʱ��ʼ����
    oPopup.document.getElementById("btSysClose").onclick = pophide;
//    eval(handler);
}

