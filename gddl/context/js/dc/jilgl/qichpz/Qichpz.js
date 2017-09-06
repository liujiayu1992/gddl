var eventsrcid;// 避免在下拉框中选择数据项目时，皮重数据计算错误
var fangx;// 读取串口数据的方向，默认从左至右，参数可以通过参数配置
var datalength;// 默认读取串口字符串的，参数可以通过参数配置

var cal;
var calenDate;
function GetDataString(_date) {
    var n;
    var syear;
    var smonth;
    var sday;
    var str;
    str = _date;
    n = str.indexOf("-");
    if (n > 0) {
        syear = str.substring(0, n);
        str = str.substring(n + 1);
        n = str.indexOf("-")
        smonth = str.substring(0, n);
        sday = str.substring(n + 1);
        return (smonth + "/" + sday + "/" + syear);
    }
    return _date;
}
function changedate(obj) {
    calenDate = obj;
    if (obj.value != "") {
        cal.setSelectedDate(GetDataString(obj.value));
        cal.toggle(obj);
    }
}
body.onclick = function() {
    if (calenDate != null) {
        if (window.event.srcElement != calenDate) {
            var bf;
            bf = true;
            var oTmp = window.event.srcElement;
            do {
                // alert("oTmp.tagName:" + oTmp.id);
                if (oTmp.id == "DateSelectDiv") {
                    bf = false;
                    break;
                }
                oTmp = oTmp.offsetParent;
            } while (oTmp.tagName != "BODY");
            if (bf) {
                calenDate = null;
                cal.hide();
            }
        } else {
            cal.show(calenDate);
        }
    }
}
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
        alert("数值超出合理范围");
        document.getElementById("GoPageNumber").value = 1;
        document.all.item("wait").style.display = "none";
        return false;
    }
    return true;
}

window.onresize = function() {
    JudgeButtonState();
    for (i = 0; i < oData_P.rows.length; i++) {
        var beiz = document.all.item("BEIZ_P", i).value;
        if (beiz > 0) {
            document.all.item("CHEPH_P", i).style.color = "#FF0000";
            document.all.item("SELECTED_P", i).disabled = true;
        } else if (beiz < 0) {
            document.all.item("CHEPH_P", i).style.color = "#0000FF";
        }
    }
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
    /*
     * cal = new Calendar(); cal.setMonthNames(new Array("\u4E00\u6708",
     * "\u4E8C\u6708", "\u4E09\u6708", "\u56DB\u6708", "\u4E94\u6708",
     * "\u516D\u6708", "\u4E03\u6708", "\u516B\u6708", "\u4E5D\u6708",
     * "\u5341\u6708", "\u5341\u4E00\u6708", "\u5341\u4E8C\u6708"));
     * cal.setShortMonthNames(new Array("\u4E00\u6708", "\u4E8C\u6708",
     * "\u4E09\u6708", "\u56DB\u6708", "\u4E94\u6708", "\u516D\u6708",
     * "\u4E03\u6708", "\u516B\u6708", "\u4E5D\u6708", "\u5341\u6708",
     * "\u5341\u4E00\u6708", "\u5341\u4E8C\u6708")); cal.setWeekDayNames(new
     * Array("\u65E5", "\u4E00", "\u4E8C", "\u4E09", "\u56DB", "\u4E94",
     * "\u516D")); cal.setShortWeekDayNames(new Array("\u65E5", "\u4E00",
     * "\u4E8C", "\u4E09", "\u56DB", "\u4E94", "\u516D"));
     * cal.setFormat("yyyy-MM-dd"); cal.setFirstDayOfWeek(0);
     * cal.setMinimalDaysInFirstWeek(1); cal.setIncludeWeek(false);
     * cal.create(); cal.onchange = function() { calenDate.value =
     * cal.formatDate(); calenDate.onchange(); }
     */
    body.style.overflow = "hidden";
    bodyHeight = body.clientHeight;
    bodyWidth = body.clientWidth;
    bodyTop = body.clientTop;

    tablemainHeight = bodyHeight - bodyTop - 65;
    tablemainWidth = bodyWidth - 15;
    // ConditionHeight =20;
    // ConditionWidth=tablemainWidth ;
    SelectDataHeight_P = 460;
    // SelectDataHeight=tablemainHeight - ConditionHeight
    // -SelectDataHeight_P-SelectDataHeight_M;
    SelectDataWidth = tablemainWidth;

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

    // 初始化动态及采集重量为零
    document.getElementById("dongtzl").value = 0.0;
    document.getElementById("DTfont").style.color = "red";
    document.getElementById("caijzl").value = 0.0;
    // 初始化皮重索引为-1
    document.all.item("EditTableRow").value = -1;
}
var oldClickrow = -1;
var oldClickrow_M = -1;
var oldinput;
var objselectCol;
var oldSelectobj;
var Editrows = -1;
var v_zhonglxs = 1000.0;// 重量转换系数，公斤→吨
body.onunload = function() {
    MSComm1.InputLen = 0;
    if (MSComm1.PortOpen) {
        MSComm1.PortOpen = false;
    }
}
body.onload = function() {
    fangx = "→";
    datalength = 7;
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    // alert(fso.GetFolder("c:\\"));
    var filePath = "C:\\WINDOWS\\汽车衡参数设置";
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
                case 6 :
                    fangx = f.ReadLine();
                    break;
                case 7 :
                    datalength = f.ReadLine();
                    break;
                case 8 :
                    v_zhonglxs = f.ReadLine();
                    break;// 重量折算系数
                default :
                    break;
            }
            if (line > 10)
                break;
        }
        f.close();
        // alert(commport+" "+BaudRate+" "+shujw+" "+tingzw+" "+jiojy);
    }
    fso = null;
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
    setTimeout(getData, 1000);
    /* setTimeout('whenClick();',500); */
    document.all.item("wait").style.display = "none";
}
// //////////////////////////////////////////////????
var kecj = false;
function getData() {
    var kecj1 = false;
    var kecj2 = false;
    var Instring = MSComm1.Input;
    BuildLog(".txt", "接收到原始字符串：" + Instring);
    if (Instring.length > 7) {
        var zhongl = 0;
        var w = 0;
        var i = 1;
        var Stemp = '';
        var isEnabled = false;
        var start = 0;
        var end = 0;
        if (Instring.indexOf("+") > -1) {
            var D = Instring.indexOf("+");
            // Instring = Instring.substring( D + 1, D+datalength);
            start = parseInt(D) + parseInt(1);
            end = parseInt(D) + parseInt(datalength);
            isEnabled = true;
        }
        // if(Instring.indexOf(")")==0){
        // Instring = Instring.substring(4, 10);
        // isEnabled = true;
        // }
        if (Instring.indexOf(")") == 0 || Instring.indexOf("!") == 0
            || Instring.indexOf(" !") == 0 || Instring.indexOf("!") == 0) {
            // 平圩出现" !"的表头，2009-03-10 王儒实提供
            Instring = Instring.substring(4, 10);
            isEnabled = true;
        }
        if (Instring.indexOf("=") > -1) {
            var D = Instring.indexOf("=");
            start = parseInt(D) + parseInt(1);
            end = parseInt(D) + parseInt(datalength);
            // if(D==0||Instring.length==9){
            // Instring = Instring.substring( D + 1, D+datalength);
            // for(var i = 8;i>= 0;i--){
            // Stemp = Stemp + Instring.substr(i,1);
            // }
            // zhongl=Stemp*1000;
            // }
            isEnabled = true;
        }
        // if (Instring.indexOf("")>-1) {
        // var D=Instring.indexOf("");
        // // Instring = Instring.substring( D + 3, D+datalength);
        // start = parseInt(D)+parseInt(3);
        // end = parseInt(D)+parseInt(datalength);
        // isEnabled = true;
        // }
        if (isEnabled) {
            Instring = Instring.substring(start, end);
            if (fangx == "←") {
                // 从右至左读取数据
                Instring = Reverse(Instring);
            }
            BuildLog(".txt", "截取后的字符串：" + Instring);
            zhongl = parseInt(Instring, 10);// 转换为十进制整数
            BuildLog(".txt", "实际计量结果：" + zhongl);
        }

        if (isNaN(zhongl)) {
            zhongl = 0.0;
        }
        if (document.all.item("zhonglybc").value == ""
            || document.all.item("zhonglybc").value == "undefined") {
            document.all.item("zhonglybc").value = "0";
        }
        if (isNaN(v_zhonglxs)) {
            v_zhonglxs = 0.0;
        }
        if (v_zhonglxs == null || v_zhonglxs == 'undefined'
            || v_zhonglxs == 0.0) {
            v_zhonglxs = 1000.0;
        }

        document.all.item("dongtzl").value = eval(Number(zhongl) / v_zhonglxs);

        var zl = Format(eval(document.all.item("dongtzl").value), 3);
        var zhonglybc = eval(document.all.item("zhonglybc").value);
        if (Math.abs(Format(eval(zhonglybc - zl), 0)) > zhonglc) {
            kecj = true;
        }
        BuildLog(".txt", "超差判断：zl=" + zl + ",zhonglybc=" + zhonglybc
            + ",zhonglc=" + zhonglc + ","
            + Math.abs(Format(eval(zhonglybc - zl), 0)) + ","
            + (Math.abs(Format(eval(zhonglybc - zl), 0)) > zhonglc));
        var Lastzl = Format(eval(document.all.item("Lastzl").value), 3);
        if (Lastzl == null)
            Lastzl = 0;
        if (Lastzl == 'undefined')
            Lastzl = 0;
        if (zl > 0) {// 动态重量必须是正数
            kecj1 = true;
        }
        if (Math.abs(parseFloat(eval(Lastzl - zl))) < 0.02) {// 上一次动态重量与当前动态重量误差在0.02之内，认为数据达到了稳态
            kecj2 = true;
        }
//		BuildLog(".txt", "条件判断：" + (zl > 0) + "|" + kecj + "|"
//						+ (Lastzhongl == zl));

        BuildLog(".txt", "条件判断：" + "[符合重量差" + kecj + "]|[采集重量非负" + kecj1
            + "]|[称重稳态" + kecj2 + "]");
        if (kecj && kecj1 && kecj2) {
            // if(Lastzl == zl){
            document.getElementById("DTfont").style.color = '#00FF00';
            document.all.item("CaijButton").disabled = false;
            // }else{
            // document.all.item("CaijButton").disabled = true;
            // document.getElementById("DTfont").style.color = 'yellow';
            // document.all.item("Lastzl").value = zl;
            // }
        } else {
            document.all.item("CaijButton").disabled = true;
            document.getElementById("DTfont").style.color = 'yellow';
            document.all.item("Lastzl").value = zl;
        }
    }
    setTimeout(getData, 1000);
}
function caij() {
    if (document.all.item("CaijButton").disabled == true)
        return;
    if (isTest == 2) {
        document.all.item("caijzl").value = eval(Round(Math.random() * 10, 2));
        isTest = 1;
    } else if (isTest == 1) {
        document.all.item("caijzl").value = eval(Round(Math.random() * 10, 2))
            + 60;
        isTest = 2;
    } else
        document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
    row = document.all.item("EditTableRow").value;
    if (row != -1) {
        var kd = document.all.item("KOUD_P", row).value;
        var kz = document.all.item("KOUZ_P", row).value;
        var scale = document.getElementById("SCALE").value;
        var ruleRounding = document.getElementById("RULEFORROUNDING").value;
        var pz = eval(document.all.item("caijzl").value);
        if (scale != -1) {
            document.all.item("PIZ_P", row).value = pz;
            var _pz = Round_New(pz, scale);
            if (ruleRounding == "UpOrDown") {

            } else if (ruleRounding == "Up") {
                if (pz > _pz) {
                    pz = eval(Number(_pz) + Number(1.0 / Math.pow(10, scale)));
                }
            } else if (ruleRounding == "Down") {
                if (pz < _pz) {
                    pz = Number(_pz) - Number(1.0 / Math.pow(10, scale));
                }
            }
            pz = Number(pz) + Number(kd) + Number(kz);
            document.all.item("PIZ_P", row).value = Format(pz, scale);
        } else {
            pz = Number(pz) + Number(kd) + Number(kz);
            document.all.item("PIZ_P", row).value = pz;
        }

    }
}
function ShowMessOrPara(button) {
    var Mess = document.getElementById("Message");
    var Para = document.getElementById("Param");
    if (button.value == "显示参数") {
        button.value = "显示信息";
        Mess.style.display = "none";
        Para.style.display = "";
    } else {
        button.value = "显示参数";
        Mess.style.display = "";
        Para.style.display = "none";
    }
}
function CheckSelect() {
    checked = document.all.item("SELECTED_M").checked;
    var Meikdq = document.getElementById("MEIKDQMC_M");
    var Meikxx = document.getElementById("MEIKDWMC_M");
    var Pinz = document.getElementById("PINZ_M");
    var Jihkj = document.getElementById("JIHKJ_M");
    var Fahl = document.getElementById("FAHL_M");
    if (checked) {
        Meikdq.parentElement.style.display = "none";
        EditHeadTabel_M.cells(2).style.display = "none";
        Meikxx.parentElement.style.display = "none";
        EditHeadTabel_M.cells(3).style.display = "none";
        Pinz.parentElement.style.display = "none";
        EditHeadTabel_M.cells(4).style.display = "none";
        Jihkj.parentElement.style.display = "none";
        EditHeadTabel_M.cells(5).style.display = "none";
        Fahl.parentElement.style.display = "none";
        EditHeadTabel_M.cells(6).style.display = "none";
        EditHeadTabel_M.style.width = 990;
        oData_M.style.width = 990;
        Meikdq.value = "";
        Meikxx.value = "";
        Pinz.value = "";
        Jihkj.value = "";
        Fahl.value = "";
    } else {
        Meikdq.parentElement.style.display = "";
        EditHeadTabel_M.cells(2).style.display = "";
        Meikxx.parentElement.style.display = "";
        EditHeadTabel_M.cells(3).style.display = "";
        Pinz.parentElement.style.display = "";
        EditHeadTabel_M.cells(4).style.display = "";
        Jihkj.parentElement.style.display = "";
        EditHeadTabel_M.cells(5).style.display = "";
        Fahl.parentElement.style.display = "";
        EditHeadTabel_M.cells(6).style.display = "";
        EditHeadTabel_M.style.width = 1490;
        oData_M.style.width = 1490;
        if (oData_P.rows.length > 0) {
            i = oData_P.rows.length - 1;
            meikdqmc = document.all.item("MEIKDQMC_P", i).value;
            meikdwmc = document.all.item("MEIKDWMC_P", i).value;
            pinz = document.all.item("PINZ_P", i).value;
            jihkj = document.all.item("JIHKJ_P", i).value;
            fahl = document.all.item("FAHL_P", i).value;
            Meikdq.value = meikdqmc;
            Meikxx.value = meikdwmc;
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
    // if(_obj2.id=='EditInput'){
    // tiaozl=2;
    // }
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
    // document.all.item("EditTableRow2").value=oldClickrow_M;
}

function dropdown(_obj1, _obj2) {
    if (oldSelectobj != null) {
        oldSelectobj.style.display = "none";
    }
    // _obj2.style.display="none";
    // _obj2.style.posLeft = 0;
    _obj2.style.position = "absolute";
    _obj2.style.borderRight = "#FC0303 1px solid";
    _obj2.style.borderLeft = "#FC0303 1px solid";
    _obj2.style.borderTop = "#FC0303 1px solid";
    _obj2.style.borderBottom = "#FC0303 1px solid";
    _obj2.style.posLeft = _obj1.parentElement.offsetLeft + 2;
    // alert(_obj2.style.posLeft);
    _obj2.style.posTop = _obj1.parentElement.offsetTop - 1;
    // alert(_obj2.style.posTop);
    _obj2.style.posWidth = _obj1.parentElement.offsetWidth + 1;
    // alert(_obj2.style.posWidth);
    _obj2.style.posHeight = _obj1.parentElement.offsetHeight;
    // alert(_obj2.style.posHeight);
    _obj2.style.display = "";
    objselectCol = _obj1;
    oldSelectobj = _obj2;
    _obj2.selectedIndex = 0;
    for (i = 0; i < _obj2.options.length; i++) {
        if (_obj1.value.toString() == _obj2.options(i).text) {
            _obj2.selectedIndex = i;
            // _obj2.value=i;
            break;
        }
    }
    /*
     * var scrollobj = document.getElementById("SelectdataDiv_M");
     * scrollobj.scrollLeft += 1; scrollobj.onscroll(); scrollobj.scrollLeft -=
     * 1; scrollobj.onscroll();
     */
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
    // alert(mkkj.length);
    for (mkindex = 0; mkindex < mkkj.length; mkindex++) {
        if (obj.value == mkkj[mkindex][0]) {
            document.all.item("JIHKJ_M", row).value = mkkj[mkindex][1];
        }
    }
}

function editin(_str1, _str2) {
    // if (_str2=="onfocus"){
    // _str1.select();
    // / return;
    // }
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

    eventsrcid = _str1.id;// 控制下拉框

    _str1.parentElement.parentElement.click();
}

function _onscroll(_obj1, _obj2) {
    // if (oldSelectobj!=null){oldSelectobj.style.display="none";}
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

// function saveMaozClick(){
// checked = document.all.item("SELECTED_M").checked;
// cheph = document.all.item("CHEPH_M").value;
// meikdqmc = document.all.item("MEIKDQMC_M").value;
// meikdwmc = document.all.item("MEIKDWMC_M").value;
// pinz = document.all.item("PINZ_M").value;
// jihkj = document.all.item("JIHKJ_M").value;
// fahl = document.all.item("FAHL_M").value;
// koud = document.all.item("KOUD_M").value;
// pandmc = document.all.item("PANDMC_M").value;
// meic = document.all.item("MEIC_M").value;
// meigy = document.all.item("MEIGY_M").value;
// chengydw = document.all.item("CHENGYDW_M").value;
// changbb = document.all.item("CHANGB_M").value;
// shouhr = document.all.item("SHOUHR_M").value;
// beiz = document.all.item("BEIZ_M").value;
// maoz = document.all.item("MAOZ_M").value;
// if(maoz == 0){
// alert("请采集毛重！");
// return false;
// }
// if(cheph == null || cheph ==""){
// alert("请填写车皮号！");
// return false;
// }
// if(checked){
// if(pandmc == null || pandmc =="" || pandmc =="请选择"){
// alert("盘点时请填写盘点煤场！");
// return false;
// }
// }else{
// if(pandmc != null && pandmc !="" && pandmc !="请选择"){
// alert("如果该车为盘点数据请将前面的复选框选中！\n否则将盘点煤场改为'请选择'！");
// return false;
// }
// if(meikdqmc == null || meikdqmc ==""){
// alert("请填写发货单位！");
// return false;
// }
// if(meikdwmc == null || meikdwmc ==""){
// alert("请填写煤矿！");
// return false;
// }
// if(pinz == null || pinz =="" || pinz =="请选择"){
// alert("请填写品种！");
// return false;
// }
// if(jihkj == null || jihkj =="" || jihkj =="请选择"){
// alert("请填写计划口径！");
// return false;
// }
// }
// if(meic == null || meic =="" || meic =="请选择"){
// alert("请填写煤场！");
// return false;
// }
// if(meigy == null || meigy =="" || meigy =="请选择"){
// alert("请填写质检员！");
// return false;
// }
// if(chengydw == null || chengydw =="" || chengydw =="请选择"){
// alert("请填写承运单位！");
// return false;
// }
// if(changbb == null || changbb =="" || changbb =="请选择"){
// alert("请填写厂别！");
// return false;
// }
// if(shouhr == null || shouhr =="" || shouhr =="请选择"){
// alert("请填写收货人！");
// return false;
// }
// return true;
// }
function savePizClick() {
    if (oData_P.rows.length <= 0) {
        alert("皮重记录为空");
        document.all.item("wait").style.display = "none";
        return false;
    }
    row = document.all.item("EditTableRow").value;
    if (row == -1) {
        alert("请选择一条记录!");
        document.all.item("wait").style.display = "none";
        return false;
    }

    piz = document.all.item("PIZ_P", row).value;
    if (piz == 0) {
        alert("请采集皮重！");
        document.all.item("wait").style.display = "none";
        return false;
    }
    document.getElementById("zhonglybc").value = piz;
    var biaozpz = document.all.item("BIAOZPZ", row).value;
    if (document.getElementById("ShowDialog").value == "y"
        && parseFloat(biaozpz) != 0.0) {
        // 判断皮重是否超差(biaozpz-0.3<=piz<=biaozpz+0.5)

        var min = Round(eval(parseFloat(biaozpz) - parseFloat(0.3)), 3);
        var max = Round(eval(parseFloat(biaozpz) + parseFloat(0.5)), 3);
        if (!(min <= piz && piz <= max)) {
            alert("皮重超出合理范围。");
            document.all.item("wait").style.display = "none";
            return false;
        }
    }

    // 判断用户在“无存档”的情况下，是否可以保存数据

    var isSave = document.all.item("PIAO_P", row).value;

    if (!(isSave == "是" || isSave == "&#26159;")) {
        if (!confirm("选择了无存档票据，确实要这样做吗？")) {
            document.all.item("wait").style.display = "none";
            return false;
        }
    }

    fahl = document.all.item("FAHL_P", row).value;

    koud = document.all.item("KOUD_P", row).value;

    kouz = document.all.item("KOUZ_P", row).value;

    beiz = document.all.item("BEIZ_P", row).value;
    // alert("beiz="+beiz);
    var blnContinue = true;
    if (beiz > 0) {
        blnContinue = confirm("该车属于过日车，是否检皮？");
        if (!blnContinue) {
            document.all.item("wait").style.display = "none";
            return false;
        }
    } else if (beiz < 0) {
        alert("该车检皮时间发生错误！请检查!");
        document.all.item("wait").style.display = "none";
        return false;
    }
    document.all.item("zhonglybc").value = piz;

    auto = document.getElementById("AutoPiz").checked;

    if (auto) {
        return true;
    }
    var b = true;
    if (document.getElementById("ShowBaocts").value == "y") {
        b = blnContinue && ShowBaocts();
        if (!b) {
            document.all.item("wait").style.display = "none";
        }

    }
    return b && true;
}

function ShowBaocts() {
    var scale = document.getElementById("SCALE").value;
    var ruleRounding = document.getElementById("RULEFORROUNDING").value;
    var row = document.all.item("EditTableRow").value;
    var myObject = new Object();
    myObject.MEIKDQMC = document.all.item("MEIKDQMC_P", row).value;
    myObject.MEIKDWMC = document.all.item("MEIKDWMC_P", row).value;
    myObject.CHENGYDW = document.all.item("CHENGYDW_P", row).value;
    myObject.JIHKJ = document.all.item("JIHKJ_P", row).value;
    myObject.CHEPH = document.all.item("CHEPH_P", row).value;
    myObject.MAOZ = document.all.item("MAOZ_P", row).value;
    myObject.MEIC = document.all.item("MEIC_P", row).value;
    if (scale != -1) {
        piz = (piz == null ? 0 : piz);
        myObject.PIZ = Round_New(piz, scale);
        if (ruleRounding == "UpOrDown") {

        } else if (ruleRounding == "Up") {
            if (piz > myObject.PIZ) {
                myObject.PIZ = eval(Number(myObject.PIZ)
                    + Number(1.0 / Math.pow(10, scale)));
            }
        } else if (ruleRounding == "Down") {
            if (piz < myObject.PIZ) {
                myObject.PIZ = Number(myObject.PIZ)
                    - Number(1.0 / Math.pow(10, scale));
            }
        }
        myObject.PIZ = Format(myObject.PIZ, scale);

    } else {
        myObject.PIZ = (piz == null ? 0 : piz);
    }

    myObject.JINGZ = Round_New(myObject.MAOZ - myObject.PIZ, scale);

    myObject.FAHL = fahl;
    myObject.KOUD = Round_New(Number(koud) + Number(kouz), 3);
    myObject.JINGZNAME = "净重：";
    // 判断计量衡号（即钢印号）
    myObject.JILHH = "找不到钢印号！"
    var len = arrBianh.length;
    for (var m = 0; m < len; m++) {
        if (arrBianh[m] == jilhh) {
            myObject.JILHH = arrBianh[m + 1];
            break;
        }
        m++;
    }
    var getval = window.showModalDialog(targetPath, myObject,
        "dialogWidth:400px;dialogHeight:520px;status:no;help:yes");
    if (getval != null && getval == "y") {
        document.all.item("PIZ_P").value = myObject.PIZ;
        return true;

    } else {
        piz = document.all.item("PIZ_P").value;
        return false;
    }

}
function Ok() {
    window.returnValue = "y";
    window.close();
}
function BuildLog(filename, str) {
    // 将此方法中的注释打开，可以在c盘根目录生成文件，用于检测汽车衡毛重中产生的数值 例如：
    // 15:1:36:动态重量:3.7 已保存重量:39.24 是否可采集:true 动态重量:3.7
    var fso, file;
    var time = new Date();
    var c = ":";
    var s = "";

    s += time.getHours() + c;
    s += time.getMinutes() + c;
    s += time.getSeconds() + c;
    s += time.getMilliseconds();
    filename = "c:\\mscomm\\" + (time.getMonth() + 1) + "月" + time.getDate()
        + "日" + filename;
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
function Cancel() {
    window.returnValue = "n";
    window.close();
}

function classchange_P(_str1, _str2) {
    var beiz;
    var blnC;
    var row;
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
    if (eventsrcid == "PIAO_P" || eventsrcid == "MEIC_P"
        || eventsrcid == "PANDMC_P") {
        return;
    }
    eventsrcid = "";
    document.all.item("PIZ_P", oldClickrow).value = "0.0";
    var kd = document.all.item("KOUD_P", oldClickrow).value;
    var kz = document.all.item("KOUZ_P", oldClickrow).value;
    // 以下代码是完成复选框选择操作使用
    // 1、取得上一次选中复选框的索引
    var lastrow = document.all.item("EditTableRow").value;
    // 2、将上一次选中的复选框取消选择（如果两次选择的不是同一个复选框）
    if (lastrow != -1 && lastrow != oldClickrow) {
        document.all.item("SELECTED_P", lastrow).checked = false;
        beiz = document.all.item("BEIZ_P", lastrow).value;
        if (beiz > 0) {
            document.all.item("CHEPH_P", lastrow).style.color = "#FF0000";
            document.all.item("SELECTED_P", lastrow).disabled = true;
        } else {
            document.all.item("CHEPH_P", lastrow).style.color = "#0000FF";
            document.all.item("SELECTED_P", lastrow).disabled = false;
        }
        document.all.item("KOUD_P", lastrow).value = "0.0";
        document.all.item("KOUZ_P", lastrow).value = "0.0";
        document.all.item("PIZ_P", lastrow).value = "0.0";
        document.all.item("EditInput").style.display = "none";
    }
    // 3、判断单击在复选框上或行上
    // (1)如果单击在复选框上
    if (window.event.srcElement == document.all.item("SELECTED_P", oldClickrow)) {
        // a)判断复选框是已选择还是未选择
        // 未选择时将索引置为-1
        if (document.all.item("SELECTED_P", oldClickrow).checked == false) {
            beiz = document.all.item("BEIZ_P", oldClickrow).value;
            blnC = document.all.item("SELECTED_P", oldClickrow).value;
            document.all.item("KOUD_P", oldClickrow).value = "0.0";
            document.all.item("KOUZ_P", lastrow).value = "0.0";
            document.all.item("PIZ_P", oldClickrow).value = "0.0";
            document.all.item("EditInput").style.display = "none";
            row = oldClickrow;
            oldClickrow = -1;
            // 选择时将采集到的重量写入"皮重"框内
        } else {
            beiz = document.all.item("BEIZ_P", oldClickrow).value;
            blnC = document.all.item("SELECTED_P", oldClickrow).checked;
            // document.all.item("PIZ_P",oldClickrow).value =
            // eval(document.all.item("caijzl").value);
            var scale = document.getElementById("SCALE").value;
            var ruleRounding = document.getElementById("RULEFORROUNDING").value;
            var pz = eval(document.all.item("caijzl").value);
            if (scale != -1) {
                document.all.item("PIZ_P", oldClickrow).value = pz;
                var _pz = Round_New(pz, scale);
                if (ruleRounding == "UpOrDown") {

                } else if (ruleRounding == "Up") {
                    if (pz > _pz) {
                        pz = eval(Number(_pz)
                            + Number(1.0 / Math.pow(10, scale)));
                    }
                } else if (ruleRounding == "Down") {
                    if (pz < _pz) {
                        pz = Number(_pz) - Number(1.0 / Math.pow(10, scale));
                    }
                }
                pz = Number(pz) + Number(kd) + Number(kz);
                document.all.item("PIZ_P", oldClickrow).value = Format(pz,
                    scale);
            } else {
                pz = Number(pz) + Number(kd) + Number(kz);
                document.all.item("PIZ_P", oldClickrow).value = pz;
            }
            row = oldClickrow;
        }
        // (2)如果单击在行上
    } else {
        checked = document.all.item("SELECTED_P", oldClickrow).checked;// 得到本行复选框当前状态
        // b)判断此时复选框状态
        // 如果当前为已选择则将复选框取消并将索引置为-1
        if (checked) {
            beiz = document.all.item("BEIZ_P", oldClickrow).value;
            blnC = document.all.item("SELECTED_P", oldClickrow).checked;
            document.all.item("SELECTED_P", oldClickrow).checked = false;
            document.all.item("KOUD_P", oldClickrow).value = "0.0";
            document.all.item("KOUZ_P", lastrow).value = "0.0";
            document.all.item("PIZ_P", oldClickrow).value = "0.0";
            document.all.item("EditInput").style.display = "none";
            row = oldClickrow;
            oldClickrow = -1;
            // 如果当前为未选择则将复选框选择并将采集重量写入"皮重"框内
        } else {
            beiz = document.all.item("BEIZ_P", oldClickrow).value;
            blnC = document.all.item("SELECTED_P", oldClickrow).checked;
            document.all.item("SELECTED_P", oldClickrow).checked = true;
            document.all.item("SELECTED_P", oldClickrow).disabled = false;
            // document.all.item("PIZ_P",oldClickrow).value =
            // eval(document.all.item("caijzl").value);
            var scale = document.getElementById("SCALE").value;
            var ruleRounding = document.getElementById("RULEFORROUNDING").value;
            var pz = eval(document.all.item("caijzl").value);
            if (scale != -1) {
                document.all.item("PIZ_P", oldClickrow).value = pz;
                var _pz = Round_New(pz, scale);
                if (ruleRounding == "UpOrDown") {

                } else if (ruleRounding == "Up") {
                    if (pz > _pz) {
                        pz = eval(Number(_pz)
                            + Number(1.0 / Math.pow(10, scale)));
                    }
                } else if (ruleRounding == "Down") {
                    if (pz < _pz) {
                        pz = Number(_pz) - Number(1.0 / Math.pow(10, scale));
                    }
                }
                pz = Number(pz) + Number(kd) + Number(kz);
                document.all.item("PIZ_P", oldClickrow).value = Format(pz,
                    scale);
            } else {
                pz = Number(pz) + Number(kd) + Number(kz);
                document.all.item("PIZ_P", oldClickrow).value = pz;
            }
            row = oldClickrow;
        }
    }
    if (beiz > 0 && blnC) {
        document.all.item("CHEPH_P", row).style.color = "#FF0000";
        document.all.item("SELECTED_P", row).disabled = true;
    } else {
        document.all.item("CHEPH_P", row).style.color = "#0000FF";
        document.all.item("SELECTED_P", row).disabled = false;
    }
    // 4、重写索引
    document.all.item("EditTableRow").value = oldClickrow;
}

function find() {
    if (event.keyCode == 13) {
        var pizsize = document.all.item("oData_P").rows.length;
        var findvalue = document.all.item("findvalue").value;
        i = 0;
        for (; i < pizsize; i++) {
            if (document.all.item("CHEPH_P", i).value == findvalue.replace(
                    /\s/g, "")) {
                var lastrow = document.all.item("EditTableRow").value;
                if (lastrow != -1 && lastrow != i) {
                    document.all.item("SELECTED_P", lastrow).checked = false;
                }
                document.all.item("SELECTED_P", i).checked = true;
                document.all.item("PIZ_P", i).value = eval(document.all
                    .item("caijzl").value);
                document.all.item("EditTableRow").value = i;
                return;
            }
        }
        if (i == pizsize) {
            alert("输入的车号不存在");
        }
    }
}

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
function Reverse(val) {
    var originalvalue = "";
    for (var i = val.length - 1; i >= 0; i--) {
        originalvalue = originalvalue + val.substr(i, 1) + "";
    }
    return originalvalue;
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
        var id = objselectCol.id;
        if (id == "KOUD_P") {
            var r = document.all.item("EditTableRow").value;
            var scale = document.getElementById("SCALE").value;
            var kouz = document.all.item("KOUZ_P", r).value;
            var old_piz = document.all.item("PIZ_P", r).value
                - objselectCol.value - kouz;
            var new_piz = eval(Number(old_piz) + Number(oldSelectobj.value)
                + Number(kouz));
            document.all.item("PIZ_P", r).value = Format(new_piz, scale);
        }

        if (id == "KOUZ_P") {
            var r = document.all.item("EditTableRow").value;
            var scale = document.getElementById("SCALE").value;
            var koud = document.all.item("KOUD_P", r).value;
            var old_piz = document.all.item("PIZ_P", r).value
                - objselectCol.value - koud;
            var new_piz = eval(Number(old_piz) + Number(oldSelectobj.value)
                + Number(koud));
            document.all.item("PIZ_P", r).value = Format(new_piz, scale);
        }

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

function Xiugbz(changerow) {
    if (changerow != -1) {
        document.all.item("XIUGBZ", changerow).value = "1";
    }
}

function ConXiugbz(obj) {
    var n;
    var row;
    var str = obj.name;
    n = str.indexOf("$");
    if (n != -1) {
        row = eval(str.substring(n + 1)) + 1;
    } else {
        row = 0;
    }
    if (row != -1) {
        document.all.item("XIUGBZ", row).value = "1";
    }
}
function printq() {
    if (document.getElementById('fenl').value == 0) {
        var docObj = document.getElementById('printjjd');
        if (docObj) {
            var pwin = window.open("", "print", "height=400,width=900,top=200");
            pwin.document.write(docObj.innerHTML);
            pwin.print();
            pwin.location.reload();
            pwin.close();
        }
    } else {
        var docObj = document.getElementById('printjjd');
        if (docObj) {
            var pwin = window.open("", "print", "height=400,width=900,top=200");
            document.all.item("lian").innerText = '第 一 联';
            pwin.document.write(docObj.innerHTML);
            document.all.item("lian").innerText = '第 二 联';
            pwin.document.write(docObj.innerHTML);
            document.all.item("lian").innerText = '第 三 联';
            pwin.document.write(docObj.innerHTML);
            pwin.print();
            pwin.location.reload();
            pwin.close();
        }
    }
}
document.onkeydown = function() {
    // 如果按下的是退格键
    if (event.keyCode == 8) {
        // alert("ok");
        event.returnValue = false;
    }
}