var bodyHeight = document.body.offsetHeight<500?eval(window.screen.height - window.screenTop-30):document.body.offsetHeight;
//alert(bodyHeight);
var bodyWidth = document.body.offsetWidth;

function Showwait(){
	return true;
}
function blurfocus(){
	return true;
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
var error=0;
function onDefMenuItemClick(btn){
	var t = document.getElementById("MenuText");
	if(t == null) {
		error += 1;
	}
	if(error == 1){
		Ext.MessageBox.alert('��ʾһ��','�װ��ģ�ȱ��MenuText����');
		return;
	}else if(error > 1){
		Ext.MessageBox.alert('��ʾһ��','�װ��ģ�������һֱ����ȥ������ȱ��MenuText����');
		return;
	}
	t.value = btn.text;
	var b = document.getElementById("MenuButton");
	if(b == null){
		Ext.MessageBox.alert('��ʾһ��',"ȱ��MenuButton����");
		error = 3;
		return;
	}
	b.click();
}

function DECODE(){
	var len = arguments.length
	var base = arguments[0];
	for(i = 1;i<len-1;i=i+2){
		if(base == arguments[i]){
			return arguments[i+1];
		}
	}
	return arguments[len-1];
}

function Round_new(value, _bit) {
	// �����������˫�ķ�����Լ��
	// ����
	// 1.���������ֵĵ�һλ����5���1����24.236--->24.24,С��5����������23.234--->23.23.
	// 2.���������ֵĵ�һλ����5����5��������ֲ���ȫΪ0ʱ���1����23.2251--->23.23
	// 3.���������ֵĵ�һλ����5����5���������ȫ��Ϊ0ʱ����5ǰ��һλΪ���������1��˫����23.235--->23.24;
	// ��5ǰ��Ϊż��������ȥ����23.225--->23.22
	var value1;// ���������ֵĵ�һλ����5����5ǰ�������
	value1 = Math.floor(value * Math.pow(10, _bit))
			- Math.floor(value * Math.pow(10, _bit - 1)) * 10;
	var dbla = 0;
	dbla =  Math.round(value * Math.pow(10, _bit) * 10000000) / 10000000;
	if ((dbla - Math.floor(value * Math.pow(10, _bit))) >= 0.5
			&& (dbla - Math.floor(value * Math.pow(10, _bit))) < 0.6) {
		if ((dbla - Math.floor(value * Math.pow(10, _bit))) == 0.5) {
			if (value1 == 0 || value1 == 2 || value1 == 4 || value1 == 6
					|| value1 == 8) {
				return Math.floor(value * Math.pow(10, _bit))
						/ Math.pow(10, _bit);
			} else {
				return (Math.floor(value * Math.pow(10, _bit)) + 1)
						/ Math.pow(10, _bit);
			}
		} else {
			return Math.round(value * Math.pow(10, _bit))
					/ Math.pow(10, _bit);
		}
	} else {
		return Math.round(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
	}
}

function Round(value, _bit) {
	// ��������ķ�����Լ��
		return Math.round(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
}

