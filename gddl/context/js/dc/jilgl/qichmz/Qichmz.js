var iTimerID;

// var cal;
// var calenDate;
// function GetDataString(_date){
// var n ;
// var syear;
// var smonth;
// var sday;
// var str;
// str=_date;
// n=str.indexOf("-");
// if(n>0){
// syear=str.substring(0,n);
// str=str.substring(n+1);
// n=str.indexOf("-")
// smonth=str.substring(0,n);
// sday=str.substring(n+1);
// return(smonth +"/" + sday+"/" +syear) ;
// }
// return _date;
// }
// function changedate(obj) {
// calenDate=obj;
// if (obj.value!=""){
// cal.setSelectedDate(GetDataString(obj.value));
// cal.toggle(obj);
// }
// }
// body.onclick =function(){
// if(calenDate!=null){
// if (window.event.srcElement!=calenDate){
// var bf;
// bf =true;
// var oTmp = window.event.srcElement;
// do
// {
// //alert("oTmp.tagName:" + oTmp.id);
// if(oTmp.id=="DateSelectDiv"){
// bf =false;
// break;
// }
// oTmp = oTmp.offsetParent;
// }
// while (oTmp.tagName != "BODY");
// if (bf){
// calenDate=null;
// cal.hide();
// }
// }else{
// cal.show(calenDate);
// }
// }
// }
function JudgeButtonState() {
	TotalNumber = eval(document.getElementById("TotalPageNumber").value);
	var object = document.getElementById("pageno");
	if (TotalNumber <= 1) {
		document.getElementById("ToFirstPageButton").disabled = true;
		document.getElementById("ToPreviousPageButton").disabled = true;
		document.getElementById("ToNextPageButton").disabled = true;
		document.getElementById("ToLastPageButton").disabled = true;
		document.getElementById("GoPageButton").disabled = true;
		object.value = "";
		return;
	}
	CurrentNumber = eval(document.getElementById("CurrentPageNumber").value);
	object.innerText = CurrentNumber.toString() + "/" + TotalNumber.toString();
	object.style.fontWeight = "bolder";
	if (CurrentNumber == TotalNumber) {
		document.getElementById("ToNextPageButton").disabled = true;
		document.getElementById("ToLastPageButton").disabled = true;
		return;
	}
	if (CurrentNumber == 1) {
		document.getElementById("ToFirstPageButton").disabled = true;
		document.getElementById("ToPreviousPageButton").disabled = true;
		return;
	}
}

function GopageValidate() {
	GoPageNumber = eval(document.getElementById("GoPageNumber").value);
	TotalNumber = eval(document.getElementById("TotalPageNumber").value);
	if (GoPageNumber > TotalNumber) {
		alert("��ֵ��������Χ");
		document.getElementById("GoPageNumber").value = 1;
		document.all.item("wait").style.display = "none";
		return false;
	}
	return true;
}

window.onresize = function() {
	JudgeButtonState();
	var bodyHeight;
	var bodyWidth;
	var bodyTop;
	var tablemainHeight;
	var tablemainWidth;
	var SelectDataHeight;
	var SelectDataWidth;
	var EditDataHeight;
	var EditDataWidth;
	var ConditionHeight;
	var ConditionWidth;
	// cal = new Calendar();
	// cal.setMonthNames(new Array("\u4E00\u6708", "\u4E8C\u6708",
	// "\u4E09\u6708", "\u56DB\u6708", "\u4E94\u6708", "\u516D\u6708",
	// "\u4E03\u6708", "\u516B\u6708", "\u4E5D\u6708", "\u5341\u6708",
	// "\u5341\u4E00\u6708", "\u5341\u4E8C\u6708"));
	// cal.setShortMonthNames(new Array("\u4E00\u6708", "\u4E8C\u6708",
	// "\u4E09\u6708", "\u56DB\u6708", "\u4E94\u6708", "\u516D\u6708",
	// "\u4E03\u6708", "\u516B\u6708", "\u4E5D\u6708", "\u5341\u6708",
	// "\u5341\u4E00\u6708", "\u5341\u4E8C\u6708"));
	// cal.setWeekDayNames(new Array("\u65E5", "\u4E00", "\u4E8C", "\u4E09",
	// "\u56DB", "\u4E94", "\u516D"));
	// cal.setShortWeekDayNames(new Array("\u65E5", "\u4E00", "\u4E8C",
	// "\u4E09", "\u56DB", "\u4E94", "\u516D"));
	// cal.setFormat("yyyy-MM-dd");
	// cal.setFirstDayOfWeek(0);
	// cal.setMinimalDaysInFirstWeek(1);
	// cal.setIncludeWeek(false);
	// cal.create();
	// cal.onchange = function() {
	// calenDate.value = cal.formatDate();
	// calenDate.onchange();
	// }
	body.style.overflow = "hidden";
	bodyHeight = body.clientHeight;
	bodyWidth = body.clientWidth;
	bodyTop = body.clientTop;

	tablemainHeight = bodyHeight - bodyTop - 65;
	tablemainWidth = bodyWidth - 15;
	// ConditionHeight =20;
	// ConditionWidth=tablemainWidth ;
	SelectDataHeight_M = 40;
	SelectDataHeight_P = 380;
	// SelectDataHeight=tablemainHeight - ConditionHeight
	// -SelectDataHeight_P-SelectDataHeight_M;
	SelectDataWidth = tablemainWidth;

	SelectFrmDiv_M.style.position = "relative";
	SelectHeadDiv_M.style.position = "relative";
	document.all.item("SelectDataDiv_M").style.position = "relative";

	// tablemain.style.left=0;
	// tablemain.style.width=bodyWidth -15;
	// tablemain.style.height=tablemainHeight;

	document.all.item("Selectdata_M").style.height = SelectDataHeight_M;
	document.all.item("Selectdata_M").style.width = SelectDataWidth;

	SelectFrmDiv_M.style.height = SelectDataHeight_M;
	SelectFrmDiv_M.style.width = SelectDataWidth;
	SelectFrmDiv_M.style.posTop = 0;

	SelectHeadDiv_M.style.posTop = 0;
	SelectHeadDiv_M.style.width = SelectDataWidth - 12;
	SelectHeadDiv_M.style.height = 24;
	SelectHeadDiv_M.style.posLeft = 0;

	document.all.item("SelectDataDiv_M").style.posTop = 0;
	document.all.item("SelectDataDiv_M").style.width = SelectDataWidth - 7;
	document.all.item("SelectDataDiv_M").style.height = SelectDataHeight_M;
	document.all.item("SelectDataDiv_M").style.posLeft = -2;

	if (document.all.item("SelectDataDiv_M").scrollHeight > document.all
			.item("SelectDataDiv_M").clientHeight) {
		SelectHeadDiv_M.style.width = SelectDataWidth - 25;
	}
	SelectHeadDiv_M.scrollLeft = document.all.item("SelectDataDiv_M").scrollLeft;

	oData_M.style.borderRight = "gray 1px solid";
	EditHeadTabel_M.style.borderRight = "gray 1px solid";

	var headtableCols;
	headtableCols = EditHeadTabel_M.cells.length;
	if (oData_M.rows.length > 0) {
		oData_M.style.borderRight = "gray 1px solid";
		EditHeadTabel_M.style.borderRight = "gray 1px solid";
		for (i = 0; i < headtableCols; i++) {
			tittleHead(i).style.posTop = 3;
			tittleHead(i).style.posWidth = oData_M.cells(i).offsetWidth - 4;
			EditHeadTabel_M.cells(i).style.width = oData_M.cells(i).offsetWidth
					+ 1;
		}
		ShowButtons(1);
	} else {
		ShowButtons(0);
	}

	SelectFrmDiv_P.style.position = "relative";
	SelectHeadDiv_P.style.position = "relative";
	document.all.item("SelectDataDiv_P").style.position = "relative";

	document.all.item("Selectdata_P").style.height = SelectDataHeight_P;
	document.all.item("Selectdata_P").style.width = SelectDataWidth;

	SelectFrmDiv_P.style.height = SelectDataHeight_P;
	SelectFrmDiv_P.style.width = SelectDataWidth;
	SelectFrmDiv_P.style.posTop = 0;

	SelectHeadDiv_P.style.posTop = 0;
	SelectHeadDiv_P.style.width = SelectDataWidth - 12;
	SelectHeadDiv_P.style.height = 30;
	SelectHeadDiv_P.style.posLeft = 0;

	document.all.item("SelectDataDiv_P").style.posTop = 0;
	document.all.item("SelectDataDiv_P").style.width = SelectDataWidth - 7;
	document.all.item("SelectDataDiv_P").style.height = SelectDataHeight_P;
	document.all.item("SelectDataDiv_P").style.posLeft = -2;
	if (document.all.item("SelectDataDiv_P").scrollHeight > document.all
			.item("SelectDataDiv_P").clientHeight) {
		SelectHeadDiv_P.style.width = SelectDataWidth - 25;
	}
	SelectHeadDiv_P.scrollLeft = document.all.item("SelectDataDiv_P").scrollLeft;

	oData_P.style.borderRight = "gray 1px solid";
	EditHeadTabel_P.style.borderRight = "gray 1px solid";

	// ��ʼ����̬���ɼ�����Ϊ��
	document.getElementById("dongtzl").value = 0.0;
	document.getElementById("DTfont").style.color = "red";
	document.getElementById("caijzl").value = 0.0;
	// ��ʼ��Ƥ������Ϊ-1
	document.all.item("EditTableRow").value = -1;

}
var oldClickrow = -1;
var oldClickrow_M = -1;
var oldinput;
var objselectCol;
var oldSelectobj;
var Editrows = -1;
var kecj = false;
body.onunload = function() {
	MSComm1.InputLen = 0;
	if (MSComm1.PortOpen) {
		MSComm1.PortOpen = false;
	}
}
body.onload = function() {
	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var filePath = "C:\\WINDOWS\\�������������";
	if (fso.FileExists(filePath)) {
		var f = fso.OpenTextFile(filePath, 1, false);
		var line = 0;
		while (!f.AtEndOfStream) {
			switch (line++) {
				case 0 :
					commport = f.ReadLine();
					break;
				case 1 :
					BaudRate = f.ReadLine();
					break;
				case 2 :
					shujw = f.ReadLine();
					break;
				case 3 :
					tingzw = f.ReadLine();
					break;
				case 4 :
					jiojy = f.ReadLine();
					break;
				case 5 :
					jilhh = f.ReadLine();
					break;
				default :
					break;
			}
			if (line > 10)
				break;
		}
		// alert(commport+" "+BaudRate+" "+shujw+" "+tingzw+" "+jiojy);
	}
	MSComm1.BaudRate = BaudRate;
	MSComm1.DataBits = shujw;
	// //////////////////////
	MSComm1.CommPort = commport;
	document.getElementById("CommPort").value = commport;
	document.getElementById("BaudRate").value = BaudRate;
	document.getElementById("DataBits").value = shujw;
	document.getElementById("StopBits").value = tingzw;
	document.getElementById("ParitySetting").value = jiojy;
	document.getElementById("Jilhh").value = jilhh;
	MSComm1.Settings = "" + BaudRate + "," + jiojy + "," + shujw + "," + tingzw
			+ "";
	// alert(MSComm1.Settings);
	MSComm1.InputLen = 0;
	// alert(MSComm1.PortOpen);
	if (!MSComm1.PortOpen) {
		MSComm1.PortOpen = true;
		document.getElementById("DTfont").style.color = "yellow";
	}
	iTimerID = setTimeout('getData();', 2000);
	/* setTimeout('whenClick();',500); */
	document.all.item("wait").style.display = "none";

}
// //////////////////////////////////////////////????
function getData() {
	// ��ȡ�������ݣ�Ҫ�����ݳ��ȴ���7
	if (MSComm1.InBufferCount > 7) {
		var Instring = MSComm1.Input;
		var zhongl = 0;
		var w = 0;
		var i = 1;
		var Stemp = '';
		var str = Instring;
		BuildLog(".txt","���յ����ַ�����"+str);
		if (Instring.indexOf("+") > -1) {
			var D = Instring.indexOf("+");
			str = Instring.substring(D + 1, D + 7);
			zhongl = parseInt(str, 10);// Stemp ;
		} else if (Instring.indexOf(")") == 0) {
			str = Instring.substring(4, 10);
			zhongl = parseInt(str, 10);
		} else if (Instring.indexOf("!") == 0 || Instring.indexOf(" !") == 0
				|| Instring.indexOf("!") == 0) {
			// ƽ�׳���" !"�ı�ͷ��2009-03-10 ����ʵ�ṩ
			str = Instring.substring(4, 10);
			zhongl = parseInt(str, 10);
		} else {
			if (Instring.indexOf("=") > -1) {
				var D = Instring.indexOf("=");
				if (D == 0 || Instring.length == 9) {
					str = Instring.substring(D + 1, D + 9);
					for (var i = 8; i >= 0; i--) {
						Stemp = Stemp + str.substr(i, 1);
					}
					zhongl = Stemp * 1000;
					
				} else {
					// ��ͷ�糧��ͷ���ȶ���Ҫ���������
					// setTimeout('getData();',500);
					// return;
					BuildLog(".txt","***********************************************************************************");
				}
			} else if (Instring.indexOf("") > -1) {
				var D = Instring.indexOf("");
				str = Instring.substring(D + 3, D + 14);
				zhongl = parseInt(str, 10);
			}
		}
		if (isNaN(zhongl)) {
			zhongl = 0.0;
		}
		document.all.item("dongtzl").value = eval(Number(zhongl) / 1000);
		var zl = eval(document.all.item("dongtzl").value);
		var zhonglybc = eval(document.all.item("zhonglybc").value);// ǰһ�α��������
		if (Math.abs(eval(zhonglybc - zl)) > zhonglc) {
			kecj = true;
		}
		var Lastzhongl = eval(document.all.item("Lastzl").value);
		if (Lastzhongl == null)
			Lastzhongl = 0;
		if (Lastzhongl == 'undefined')
			Lastzhongl = 0;
		// if (zl > 0) {
		//
		// var str = "";
		// str = str + "��̬����:" + zl + " ";
		// str = str + "�ѱ�������:" + zhonglybc + " ";
		// str = str + "�Ƿ�ɲɼ�:" + kecj + " ";
		// str = str + "��̬����:" + zl + " ";
		// str = str + "Lastzhongl:" + Lastzhongl + " ";
		// // BuildLog("�����б�.txt",str);
		// }

		// if (zl > 0 && kecj) {
		// if (Lastzhongl == zl) {
		// document.getElementById("DTfont").style.color = '#00FF00';//���ݿɲɼ�״̬
		// document.all.item("CaijButton").disabled = false;
		// } else {
		// document.all.item("CaijButton").disabled = true;
		// document.getElementById("DTfont").style.color = 'yellow';
		// document.all.item("Lastzl").value = zl;
		// }
		// } else {
		// document.all.item("CaijButton").disabled = true;
		// document.getElementById("DTfont").style.color = 'yellow';
		// }
		if (zl > 0 && kecj && Lastzhongl == zl) {
			document.getElementById("DTfont").style.color = '#00FF00';// ���ݿɲɼ�״̬
			document.all.item("CaijButton").disabled = false;
		} else {
			document.all.item("CaijButton").disabled = true;
			document.getElementById("DTfont").style.color = 'yellow';
			document.all.item("Lastzl").value = zl;
		}
	}
	iTimerID = setTimeout('getData();', 500);
}
function caij() {
	// ��ݼ��뱣�水ťͬ�� �Ƿ����
	if (document.all.item("CaijButton").disabled == true)
		return;
	if (isTest == 2) {
		document.all.item("caijzl").value = eval(Round(Math.random() * 10, 2))
				+ 20;
		isTest = 1;
	} else

	if (isTest == 1) {
		document.all.item("caijzl").value = eval(Round(Math.random() * 10, 2))
				+ 60;
		isTest = 2;
	} else
		document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
	document.all.item("MAOZ_M", 0).value = eval(document.all.item("caijzl").value);
	return false;
}
function ShowMessOrPara(button) {
	var Mess = document.getElementById("Message");
	var Para = document.getElementById("Param");
	if (button.value == "��ʾ����") {
		button.value = "��ʾ��Ϣ";
		Mess.style.display = "none";
		Para.style.display = "";
	} else {
		button.value = "��ʾ����";
		Mess.style.display = "";
		Para.style.display = "none";
	}
}
function CheckSelect() {
	checked = document.all.item("SELECTED_M").checked;
	var Meikdq = document.getElementById("MEIKDQMC_M");
	var Meikxx = document.getElementById("MEIKDWMC_M");
	var Chengydw = document.getElementById("CHENGYDW_M");
	var Pinz = document.getElementById("PINZ_M");
	var Jihkj = document.getElementById("JIHKJ_M");
	var Fahl = document.getElementById("FAHL_M");
	if (checked) {
		// alert();
		Meikdq.parentElement.style.display = "none";
		EditHeadTabel_M.cells(2).style.display = "none";
		Meikxx.parentElement.style.display = "none";
		EditHeadTabel_M.cells(3).style.display = "none";
		Pinz.parentElement.style.display = "none";
		EditHeadTabel_M.cells(4).style.display = "none";
		Chengydw.parentElement.style.display = "none";
		EditHeadTabel_M.cells(5).style.display = "none";
		Jihkj.parentElement.style.display = "none";
		EditHeadTabel_M.cells(6).style.display = "none";
		Fahl.parentElement.style.display = "none";
		EditHeadTabel_M.cells(7).style.display = "none";
		EditHeadTabel_M.style.width = 1010;
		oData_M.style.width = 1010;
		Meikdq.value = "";
		Meikxx.value = "";
		Pinz.value = "";
		Jihkj.value = "";
		Fahl.value = "0.0";
	} else {
		Meikdq.parentElement.style.display = "";
		EditHeadTabel_M.cells(2).style.display = "";
		Meikxx.parentElement.style.display = "";
		EditHeadTabel_M.cells(3).style.display = "";
		Pinz.parentElement.style.display = "";
		EditHeadTabel_M.cells(4).style.display = "";
		Chengydw.parentElement.style.display = "";
		EditHeadTabel_M.cells(5).style.display = "";
		Jihkj.parentElement.style.display = "";
		EditHeadTabel_M.cells(6).style.display = "";
		Fahl.parentElement.style.display = "";
		EditHeadTabel_M.cells(7).style.display = "";
		EditHeadTabel_M.style.width = 1490;
		oData_M.style.width = 1490;
		if (oData_P.rows.length > 0) {
			i = oData_P.rows.length - 1;
			meikdqmc = document.all.item("MEIKDQMC_P", i).value;
			meikdwmc = document.all.item("MEIKDWMC_P", i).value;
			chengydw = document.all.item("CHENGYDW_P", i).value;
			pinz = document.all.item("PINZ_P", i).value;
			jihkj = document.all.item("JIHKJ_P", i).value;
			var autoCopy = document.getElementById("AUTOCOPY").checked;// �ж��Ƿ����Զ���ֵ
			fahl = "0.0";
			if (autoCopy) {
				fahl = document.all.item("FAHL_P", i).value;
			}
			Meikdq.value = meikdqmc;
			Meikxx.value = meikdwmc;
			Chengydw.value = chengydw;
			Pinz.value = pinz;
			Jihkj.value = jihkj;
			Fahl.value = fahl;
		}
	}
}

function editclick(_obj1, _obj2) {
	if (oldSelectobj != null) {
		oldSelectobj.style.display = "none";
	}
	var tiaozl = 2;
	_obj2.tabIndex = _obj1.tabIndex;
	_obj2.style.borderRight = "#FC0303 1px solid";
	_obj2.style.borderLeft = "#FC0303 1px solid";
	_obj2.style.borderTop = "#FC0303 1px solid";
	_obj2.style.borderBottom = "#FC0303 1px solid";
	_obj2.style.posLeft = _obj1.parentElement.offsetLeft + tiaozl;
	_obj2.style.posTop = _obj1.parentElement.offsetTop;
	_obj2.style.posWidth = _obj1.parentElement.offsetWidth;
	_obj2.style.posHeight = _obj1.parentElement.offsetHeight;
	_obj2.style.position = "absolute";
	_obj2.style.display = "";
	objselectCol = _obj1;
	oldSelectobj = _obj2;
	_obj2.value = _obj1.value;
	_obj2.select();
}

function classchange_M(_str1, _str2) {
	var rows = oData_M.rows;
	if (_str2 == "onclick") {
		if (oldClickrow_M != -1) {
			oData_M.rows(oldClickrow_M).className = "edittableTrOut";
		}
		oldClickrow_M = _str1.rowIndex;
		oData_M.rows(oldClickrow_M).className = "edittableTrClick";
	}

	if (_str2 == "onmouseout") {
		if (oldClickrow_M != _str1.rowIndex) {
			_str1.className = "edittableTrOut";
		} else {
			_str1.className = "edittableTrClick";
		}
	}

	if (_str2 == "onmouseover") {

		if (oldClickrow_M != _str1.rowIndex) {
			_str1.className = "edittableTrOn";
		} else {
			_str1.className = "edittableTrClick";
		}

	}
}

function dropdown(_obj1, _obj2) {
	if (oldSelectobj != null) {
		oldSelectobj.style.display = "none";
	}
	_obj2.style.position = "absolute";
	_obj2.style.borderRight = "#FC0303 1px solid";
	_obj2.style.borderLeft = "#FC0303 1px solid";
	_obj2.style.borderTop = "#FC0303 1px solid";
	_obj2.style.borderBottom = "#FC0303 1px solid";
	_obj2.style.posLeft = _obj1.parentElement.offsetLeft + 2;
	_obj2.style.posTop = _obj1.parentElement.offsetTop - 1;
	_obj2.style.posWidth = _obj1.parentElement.offsetWidth + 1;
	_obj2.style.posHeight = _obj1.parentElement.offsetHeight;
	_obj2.style.display = "";
	objselectCol = _obj1;
	oldSelectobj = _obj2;
	_obj2.selectedIndex = 0;
	for (i = 0; i < _obj2.options.length; i++) {
		if (_obj1.value.toString() == _obj2.options(i).text) {
			_obj2.selectedIndex = i;
			break;
		}
	}
	var scrollobj = document.getElementById("SelectdataDiv_M");
	scrollobj.scrollLeft += 1;
	scrollobj.onscroll();
	scrollobj.scrollLeft -= 1;
	scrollobj.onscroll();
}

function JihkjChange(obj) {
	var n;
	var row;
	var str = obj.name;
	n = str.indexOf("$");
	if (n != -1) {
		row = eval(str.substring(n + 1)) + 1;
	} else {
		row = 0;
	}
	for (mkindex = 0; mkindex < mkkj.length; mkindex++) {
		if (obj.value == mkkj[mkindex][0]) {
			document.all.item("JIHKJ_M", row).value = mkkj[mkindex][1];
			break;
		}
	}
}

function editin(_str1, _str2) {
	if (oldinput != null) {
		oldinput.style.border = "#ffffff 0px solid";
		oldinput.style.posWidth = oldinput.parentElement.offsetWidth - 2;
		oldinput.style.backgroundColor = "";
		oldinput.style.position = "";
		oldinput.style.left = "";
		oldinput.style.height = "";
		oldinput.style.top = "";
		oldinput.style.padding = 2;
	}
	if (!_str1.readOnly) {
		_str1.select();
		_str1.style.backgroundColor = "#ffffff";
		_str1.style.border = "#FC0303 1px solid";
		_str1.style.posLeft = _str1.parentElement.offsetLeft + 2;
		if (_str1.parentElement.offsetTop == 0) {
			_str1.style.posTop = _str1.parentElement.offsetTop;
			_str1.style.posHeight = _str1.parentElement.offsetHeight - 2;
		} else {
			_str1.style.posTop = _str1.parentElement.offsetTop - 1;
			_str1.style.posHeight = _str1.parentElement.offsetHeight - 1;
		}
		_str1.style.posWidth = _str1.parentElement.offsetWidth + 1;
		_str1.style.padding = 3;
		_str1.style.position = "absolute";
		if (_str2 == "onclick") {
			if (oldSelectobj != null) {
				oldSelectobj.style.display = "none";
			}
		}
	}
	oldinput = _str1;
	_str1.parentElement.parentElement.click();
}

function _onscroll(_obj1, _obj2) {
	if ((_obj1.scrollWidth - _obj1.clientWidth) > 0) {
		if ((_obj1.scrollWidth - _obj1.clientWidth) == _obj1.scrollLeft) {
			_obj2.style.posLeft = 0;
			if (_obj1.scrollHeight > _obj1.clientHeight) {
				_obj2.scrollLeft = _obj1.scrollLeft
						+ (_obj1.scrollWidth - _obj2.scrollWidth);
			} else {
				_obj2.scrollLeft = _obj1.scrollLeft;
			}
		} else {
			_obj2.style.posLeft = 0;
			_obj2.scrollLeft = _obj1.scrollLeft;
		}
	}
}

function droponchange(_int) {
	if (objselectCol != null) {
		if (oldSelectobj.id == 'PANDMC_Select'
				&& oldSelectobj.selectedIndex != 0) {
			if (!document.all.item("SELECTED_M").checked) {
				document.all.item("SELECTED_M").checked = true;
				CheckSelect();
			}
		}
		objselectCol.value = oldSelectobj.options(oldSelectobj.selectedIndex).text;
		if (_int == 1) {
			updatarows(objselectCol.name);
		}
	}
}

function alertMessage(msg) {
	// ��ʾ�쳣
	alert(msg);
	document.all.item("wait").style.display = "none";
	iTimerID = setTimeout('getData();', 500);
	return false;
}

function saveMaozClick() {
	clearTimeout(iTimerID);// ��ͣ��ʱ��������com���ڶ�ȡ����
	checked = document.all.item("SELECTED_M").checked;
	cheph = document.all.item("CHEPH_M").value;
	meikdqmc = document.all.item("MEIKDQMC_M").value;
	meikdwmc = document.all.item("MEIKDWMC_M").value;
	pinz = document.all.item("PINZ_M").value;
	jihkj = document.all.item("JIHKJ_M").value;
	fahl = document.all.item("FAHL_M").value;
	koud = document.all.item("KOUD_M").value;
	pandmc = document.all.item("PANDMC_M").value;
	meic = document.all.item("MEIC_M").value;
	meigy = document.all.item("MEIGY_M").value;
	chengydw = document.all.item("CHENGYDW_M").value;
	changbb = document.all.item("CHANGB_M").value;
	shouhr = document.all.item("SHOUHR_M").value;
	beiz = document.all.item("BEIZ_M").value;
	maoz = document.all.item("MAOZ_M").value;

	if (maoz == 0) {
		return alertMessage("��ɼ�ë�أ�");
	}
	if (cheph == null || cheph == "") {
		return alertMessage("����д��Ƥ�ţ�");
	}
	if (checked) {
		if (pandmc == null || pandmc == "" || pandmc == "��ѡ��") {
			return alertMessage("�̵�ʱ����д�̵�ú����");
		}
	} else {
		if (pandmc != null && pandmc != "" && pandmc != "��ѡ��") {
			return alertMessage("����ó�Ϊ�̵������뽫ǰ��ĸ�ѡ��ѡ�У�\n�����̵�ú����Ϊ'��ѡ��'��");
		}
		if (meikdqmc == null || meikdqmc == "") {
			return alertMessage("����д������λ��");
		}
		if (meikdwmc == null || meikdwmc == "") {
			return alertMessage("����дú��");
		}
		if (pinz == null || pinz == "" || pinz == "��ѡ��") {
			return alertMessage("����дƷ�֣�");
		}
		if (jihkj == null || jihkj == "" || jihkj == "��ѡ��") {
			return alertMessage("����д�ƻ��ھ���");
		}
	}
	if (meic == null || meic == "" || meic == "��ѡ��") {
		return alertMessage("����дú����");
	}
	if (meigy == null || meigy == "" || meigy == "��ѡ��") {
		return alertMessage("����д�ʼ�Ա��");
	}
	if (chengydw == null || chengydw == "" || chengydw == "��ѡ��") {
		return alertMessage("����д���˵�λ��");
	}
	if (changbb == null || changbb == "" || changbb == "��ѡ��") {
		return alertMessage("����д����");
	}
	if (shouhr == null || shouhr == "" || shouhr == "��ѡ��") {
		return alertMessage("����д�ջ��ˣ�");
	}
	if (isNaN(koud) || parseFloat(koud) < 0) {
		// Ҫ��۶�������������
		return alertMessage("�۶���ֵ������");
	}

	document.all.item("zhonglybc").value = maoz;
	var show = document.getElementById("ShowDialog").value
	var blnC = true;


	if (show == "y") {
		blnC = ShowDialog();
	}
	if (blnC) {
		var scale = document.getElementById("SCALE").value;
		maoz = maoz - koud;// ë��=ë��-�۶֣����۶�����ֱ��������ë����
		if (scale != -1) {
			maoz = Round_New(maoz, scale);
		} else {
			maoz = Round_New(maoz, 1);
		}
		document.all.item("MAOZ_M").value = maoz;
		document.all.item("caijzl").value = 0;
	} else {
		document.all.item("wait").style.display = "none";
	}
	iTimerID = setTimeout('getData();', 500);
	
	return blnC;
}

function BuildLog(filename, str) {
	// ���˷����е�ע�ʹ򿪣�������c�̸�Ŀ¼�����ļ������ڼ��������ë���в�������ֵ ���磺
	// 15:1:36:��̬����:3.7 �ѱ�������:39.24 �Ƿ�ɲɼ�:true ��̬����:3.7
	var fso, file;
	var time = new Date();
	var c = ":";
	var s = "";
	time = new Date();
	s += time.getHours() + c;
	s += time.getMinutes() + c;
	s += time.getSeconds() + c;
	s += time.getMilliseconds();
	filename = "c:\\windows\\" + time.getMonth() + "��" + time.getDay() + "��"
			+ filename;
	fso = new ActiveXObject("Scripting.FileSystemObject");
	if (fso.FileExists(filename)) {
		file = fso.OpenTextFile(filename, 8, true);
	} else {
		file = fso.CreateTextFile(filename, true);
	}

	file.WriteLine(s + str);
	file.WriteLine("end");
	file.close();
	fso = null;

}

function ShowDialog() {
	// alert("showdialog function");
	var scale = document.getElementById("SCALE").value;
	var ruleRounding = document.getElementById("RULEFORROUNDING").value;
	var myObject = new Object();
	myObject.MEIKDQMC = meikdqmc;
	myObject.MEIKDWMC = meikdwmc;
	myObject.CHENGYDW = chengydw;
	myObject.JIHKJ = jihkj;
	myObject.CHEPH = cheph;
	if (scale != -1) {
		myObject.MAOZ = Round_New(maoz, scale);
		if (ruleRounding == "UpOrDown") {

		} else if (ruleRounding == "Up") {
			if (maoz > myObject.MAOZ) {
				myObject.MAOZ = eval(Number(myObject.MAOZ)
						+ Number(1.0 / Math.pow(10, scale)));
			}
		} else if (ruleRounding == "Down") {
			if (maoz < myObject.MAOZ) {
				myObject.MAOZ = Number(myObject.MAOZ)
						- Number(1.0 / Math.pow(10, scale));
			}
		}
		myObject.MAOZ = Format(myObject.MAOZ, scale);
	} else {
		myObject.MAOZ = maoz;
	}

	myObject.MEIC = meic;
	myObject.PIZ = 0.0;
	myObject.JINGZ = Round_New(Number(myObject.MAOZ) - Number(koud), 3);// ���ë��
	myObject.FAHL = fahl;
	myObject.KOUD = koud;
	myObject.JINGZNAME = "���ë�أ�";
	// �жϼ�����ţ�����ӡ�ţ�
	myObject.JILHH = "�Ҳ�����ӡ�ţ�"
	var len = arrBianh.length;
	for (var m = 0; m < len; m++) {
		if (arrBianh[m] == jilhh) {
			myObject.JILHH = arrBianh[m + 1];
			break;
		}
		m++;
	}
	var getval = window.showModalDialog(targetPath, myObject,
			"dialogWidth:400px;dialogHeight:500px;status:no;help:yes");
	if (getval != null && getval == "y") {
		document.all.item("MAOZ_M").value = Number(myObject.MAOZ);
		maoz = document.all.item("MAOZ_M").value;// ������ע��return���
		return true;
	} else {
		maoz = document.all.item("MAOZ_M").value;// ������ע��return���
		return false;
	}
}
// function savePizClick() {
// if (oData_P.rows.length <= 0) {
// alert("Ƥ�ؼ�¼Ϊ��");
// document.all.item("wait").style.display = "none";
// return false;
// }
// piz = document.all.item("PIZ_P").value;
// if (piz == 0) {
// alert("��ɼ�Ƥ�أ�");
// document.all.item("wait").style.display = "none";
// return false;
// }
// }

function classchange_P(_str1, _str2) {
	var rows = oData_P.rows;
	if (_str2 == "onclick") {
		if (oldClickrow != -1) {
			oData_P.rows(oldClickrow).className = "edittableTrOut";
		}
		oldClickrow = _str1.rowIndex;
		oData_P.rows(oldClickrow).className = "edittableTrClick";
	}
	if (_str2 == "onmouseout") {
		if (oldClickrow != _str1.rowIndex) {
			_str1.className = "edittableTrOut";
		} else {
			_str1.className = "edittableTrClick";
		}
	}
	if (_str2 == "onmouseover") {

		if (oldClickrow != _str1.rowIndex) {
			_str1.className = "edittableTrOn";
		} else {
			_str1.className = "edittableTrClick";
		}

	}
	/*
	 * // ���´�������ɸ�ѡ��ѡ�����ʹ�� // 1��ȡ����һ��ѡ�и�ѡ������� var lastrow =
	 * document.all.item("EditTableRow").value; //
	 * 2������һ��ѡ�еĸ�ѡ��ȡ��ѡ���������ѡ��Ĳ���ͬһ����ѡ�� if(lastrow != -1 && lastrow !=
	 * oldClickrow){ document.all.item("SELECTED_P",lastrow).checked = false; } //
	 * 3���жϵ����ڸ�ѡ���ϻ����� // (1)��������ڸ�ѡ���� if(window.event.srcElement ==
	 * document.all.item("SELECTED_P",oldClickrow)){ // a)�жϸ�ѡ������ѡ����δѡ�� //
	 * δѡ��ʱ��������Ϊ-1 if(document.all.item("SELECTED_P",oldClickrow).checked ==
	 * false){ oldClickrow = -1; // ѡ��ʱ���ɼ���������д��"Ƥ��"���� }else{
	 * document.all.item("PIZ_P",oldClickrow).value =
	 * eval(document.all.item("caijzl").value); } // (2)������������� }else{ checked =
	 * document.all.item("SELECTED_P",oldClickrow).checked;//�õ����и�ѡ��ǰ״̬ //
	 * b)�жϴ�ʱ��ѡ��״̬ // �����ǰΪ��ѡ���򽫸�ѡ��ȡ������������Ϊ-1 if(checked){
	 * document.all.item("SELECTED_P",oldClickrow).checked = false; oldClickrow =
	 * -1; // �����ǰΪδѡ���򽫸�ѡ��ѡ�񲢽��ɼ�����д��"Ƥ��"���� }else{
	 * document.all.item("SELECTED_P",oldClickrow).checked = true;
	 * document.all.item("PIZ_P",oldClickrow).value =
	 * eval(document.all.item("caijzl").value); } } // 4����д����
	 * document.all.item("EditTableRow").value=oldClickrow;
	 */
}

// function find() {
// if (event.keyCode == 13) {
// var pizsize = document.all.item("oData_P").rows.length;
// var findvalue = document.all.item("findvalue").value;
// i = 0;
// for (; i < pizsize; i++) {
// if (document.all.item("CHEPH_P", i).value == findvalue.replace(
// /\s/g, "")) {
// var lastrow = document.all.item("EditTableRow").value;
// if (lastrow != -1 && lastrow != i) {
// document.all.item("SELECTED_P", lastrow).checked = false;
// }
// document.all.item("SELECTED_P", i).checked = true;
// document.all.item("PIZ_P", i).value = eval(document.all
// .item("caijzl").value);
// document.all.item("EditTableRow").value = i;
// return;
// }
// }
// if (i == pizsize) {
// alert("����ĳ��Ų�����");
// }
// }
// }

// END

// QueryFrameClsssend

function updatarows(str) {
	var rowscount, n;
	var row;
	n = str.indexOf("$");
	if (n != -1) {
		strn = str.substring(0, n);
		row = eval(str.substring(n + 1)) + 1;
	} else {
		strn = str;
		row = 0;
	}
	rowscount = document.all.item(strn).length;
	for (i = row + 1; i < rowscount; i++) {
		document.all.item(strn, i).value = document.all.item(strn, row).value;
	}
}

// InstrSelectEdit

function ChangeSelectRow(obj1) {
	if (Editrows != -1) {
		document.all.item(((obj1.id).substring(0, (obj1.id).indexOf("edit"))),
				Editrows).value = obj1.value;
		// colCount();
	}
}

function FormatInput(obj, decimalLength) {
	var i = 0;
	var objLength;
	if (document.all.item(obj) != null) {
		objLength = document.all.item(obj).length;
		if (objLength > 1) {
			for (i = 0; i < objLength; i++) {
				document.all.item(obj, i).value = Format(document.all.item(obj,
								i).value, decimalLength);
			}
		} else if (objLength = 1) {
			document.all.item(obj).value = Format(document.all.item(obj).value,
					decimalLength);
		}
	}
}
function Round(a_Num, a_Bit) {
	return (Format(Math.round(a_Num * Math.pow(10, a_Bit))
					/ Math.pow(10, a_Bit), a_Bit));
}

function Format(value, decimalLength) {
	value = Math.round(value * Math.pow(10, decimalLength));

	value = value / Math.pow(10, decimalLength);

	var v = value.toString().split(".");
	if (v[0] == "") {
		v[0] = "0";
	}

	if (v.length > 1) {
		var len = v[1].length;
		if (len > decimalLength) {
			v[1] = v[1].substring(0, decimalLength);
		} else {
			for (var i = v[1].length; i < decimalLength; i++) {
				v[1] += "0";
			}
		}
	} else {
		v[1] = "";
		for (var i = v[1].length; i < decimalLength; i++) {
			v[1] += "0";
		}
	}

	if (decimalLength > 0) {
		return v[0] + "." + v[1];
	} else {
		return v[0];
	}
}

function editchange(_int) {
	if (objselectCol != null) {
		objselectCol.value = oldSelectobj.value;
		if (_int == 1) {
			updatarows(objselectCol.name);
		}
	}
	// colCount();
}

function ShowButtons(_type) {

	if (_type == 1) {
	} else {
	}
}

// function Xiugbz(changerow) {
// if (changerow != -1) {
// document.all.item("XIUGBZ", changerow).value = "1";
// }
// }

// function ConXiugbz(obj) {
// var n;
// b var row;
// var str = obj.name;
// n = str.indexOf("$");
// if (n != -1) {
// row = eval(str.substring(n + 1)) + 1;
// } else {
// row = 0;
// }
// if (row != -1) {
// document.all.item("XIUGBZ", row).value = "1";
// }
// }
document.onkeydown = function() {
	// ������µ����˸��
	if (event.keyCode == 8) {
		// alert("ok");
		event.returnValue = false;
	}
}