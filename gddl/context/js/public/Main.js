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
		Ext.MessageBox.alert('提示一下','亲爱的！缺少MenuText对象');
		return;
	}else if(error > 1){
		Ext.MessageBox.alert('提示一下','亲爱的！就算你一直点下去。还是缺少MenuText对象啊');
		return;
	}
	t.value = btn.text;
	var b = document.getElementById("MenuButton");
	if(b == null){
		Ext.MessageBox.alert('提示一下',"缺少MenuButton对象");
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
	// 四舍六入五成双的法则修约，
	// 即：
	// 1.拟舍弃数字的第一位大于5则进1，如24.236--->24.24,小于5则舍弃，如23.234--->23.23.
	// 2.拟舍弃数字的第一位等于5，且5后面的数字并非全为0时则进1，如23.2251--->23.23
	// 3.拟舍弃数字的第一位等于5，且5后面的数字全部为0时，若5前面一位为奇数，则进1成双，如23.235--->23.24;
	// 若5前面为偶数，则舍去，如23.225--->23.22
	var value1;// 拟舍弃数字的第一位等于5，且5前面的数字
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
	// 四舍五入的法则修约，
		return Math.round(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
}

