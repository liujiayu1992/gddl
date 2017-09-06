<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<link rel="stylesheet" type="text/css"></link>
<head>
    <title>燃料管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="edge"/>

    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>

    <style type="text/css">

        .inputTxt {
            height: 18px;
            width: 130px;
            border-top: 1px solid #707070;
            border-right: 1px solid #FFFFFF;
            border-bottom: 1px solid #FFFFFF;
            border-left: 1px solid #707070;
            font-family: "Verdana", "Arial", "Helvetica", "sans-serif";
            font-size: 12px;
            color: #204162;
            text-decoration: none;
            clip: rect(auto auto auto auto);
        }

        .inputPosition {
            clip: rect(auto auto auto auto);
            padding-top: 5px;
            padding-left: 0px;
        }

    </style>
    <link href="css/main_page.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript">
        var welcomeUrl = 'Welcome';
    </script>
    <script language="JavaScript" type="text/JavaScript">
        var lastTitle;
        var lastTab;
        function MM_swapImgRestore() { //v3.0
            var i, x, a = document.MM_sr;
            for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++) x.src = x.oSrc;
        }

        function MM_preloadImages() { //v3.0
            var d = document;
            if (d.images) {
                if (!d.MM_p) d.MM_p = new Array();
                var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                for (i = 0; i < a.length; i++)
                    if (a[i].indexOf("#") != 0) {
                        d.MM_p[j] = new Image;
                        d.MM_p[j++].src = a[i];
                    }
            }
        }

        function MM_findObj(n, d) { //v4.01
            var p, i, x;
            if (!d) d = document;
            if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                d = parent.frames[n.substring(p + 1)].document;
                n = n.substring(0, p);
            }
            if (!(x = d[n]) && d.all) x = d.all[n];
            for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
            for (i = 0; !x && d.layers && i < d.layers.length; i++) x = MM_findObj(n, d.layers[i].document);
            if (!x && d.getElementById) x = d.getElementById(n);
            return x;
        }

        function MM_swapImage() { //v3.0
            var i, j = 0, x, a = MM_swapImage.arguments;
            document.MM_sr = new Array;
            for (i = 0; i < (a.length - 2); i += 3)
                if ((x = MM_findObj(a[i])) != null) {
                    document.MM_sr[j++] = x;
                    if (!x.oSrc) x.oSrc = x.src;
                    x.src = a[i + 2];
                }
        }

    </script>
    <script type="text/javascript"></script>
    <script type="text/javascript"></script>
    <script type="text/javascript"></script>
    <script type="text/javascript"></script>
    <script language="JavaScript" type="text/JavaScript">

        var imgPath = "imgs/main/blue"; </script>
    <script language="JavaScript" type="text/JavaScript">
        var isIE = !!window.ActiveXObject;
        var isIE6 = isIE && !window.XMLHttpRequest;
        var isIE8 = isIE && !!document.documentMode;
        var isIE7 = isIE && !isIE6 && !isIE8;
        //if (isIE){
        //    if (isIE6){
        //        alert("ie6");
        //    }else if (isIE8){
        //        alert("ie8");
        //    }else if (isIE7){
        //        alert("ie7");
        //    }
        //}
        //mainFrame
        var ifm = document.getElementById("mainFrame");
        if (isIE8 || isIE6 || isIE7) {
            ifm.removeAttribute('height');
        } else {

        }
        function MM_reloadPage(init) {  //reloads the window if Nav4 resized
            if (init == true) with (navigator) {
                if ((appName == "Netscape") && (parseInt(appVersion) == 4)) {
                    document.MM_pgW = innerWidth;
                    document.MM_pgH = innerHeight;
                    onresize = MM_reloadPage;
                }
            }
            else if (innerWidth != document.MM_pgW || innerHeight != document.MM_pgH) location.reload();
        }
        MM_reloadPage(true);

        function MM_showHideLayers() { //v6.0
            var i, p, v, obj, args = MM_showHideLayers.arguments;
            MM_ChooseMenuDiv(args[1]);
            for (i = 0; i < (args.length - 2); i += 3) if ((obj = MM_findObj(args[i])) != null) {
                v = args[i + 2];
                if (obj.style) {
                    obj = obj.style;
                    v = (v == 'show') ? 'visible' : (v == 'hide') ? 'hidden' : v;
                }
                obj.visibility = v;
            }
        }

        function MM_ChooseMenuDiv(obj) {
            if (obj == null || obj == "") {
                return;
            }
            document.getElementById("jiantouid").width =
                obj.parentElement.offsetLeft + obj.parentElement.offsetWidth / 2;
            if (lastTitle != null) {
                if (isIE8 || isIE6 || isIE7) {
                    lastDivId = "Menu" + lastTitle.children[0].id;
                } else {
                    lastDivId = "Menu" + lastTitle.childNodes[0].id;
                }

                document.getElementById(lastDivId).style.display = "none";
            }
            var DivId = "Menu" + obj.id;
            document.getElementById(DivId).style.display = "";
            MM_ChangeBackGround(obj);
        }

        function MM_ChangeBackGround(obj) {
            if (lastTitle != null) {

                if (isIE8 || isIE6 || isIE7) {
                    lastPrev = lastTitle.previousSibling;
                    lastNext = lastTitle.nextSibling;
                    lastPrev.children[0].src = getImageSrc("main_bar_line.gif");
                    lastNext.children[0].src = getImageSrc("main_bar_line.gif");
                    lastTitle.background = getImageSrc("main_bar_bg.gif");
                } else {
                    lastPrev = lastTitle.previousElementSibling;
                    lastNext = lastTitle.nextElementSibling;
                    lastPrev.childNodes[0].src = getImageSrc("main_bar_line.gif");
                    lastNext.childNodes[0].src = getImageSrc("main_bar_line.gif");
                    lastTitle.setAttribute('style', 'background-image:url("/gddl/imgs/main/blue/main_bar_bg.gif");');
                }
            }
            lastTitle = obj.parentElement;
            curr = obj.parentElement;
            if (isIE8 || isIE6 || isIE7) {
                prev = obj.parentElement.previousSibling;
                next = obj.parentElement.nextSibling;
                prev.children[0].src = getImageSrc("main_bar_line_on_L.gif");
                next.children[0].src = getImageSrc("main_bar_line_on_R.gif");
                curr.background = getImageSrc("main_bar_line_on_light.gif");
            } else {
                prev = obj.parentElement.previousElementSibling;
                next = obj.parentElement.nextElementSibling;
                prev.childNodes[0].src = getImageSrc("main_bar_line_on_L.gif");
                next.childNodes[0].src = getImageSrc("main_bar_line_on_R.gif");
                curr.setAttribute('style', 'background-image:url("/gddl/imgs/main/blue/main_bar_line_on_light.gif");');
            }


        }

        function MM_openTab(obj) {

            debugger;
            if (isIE8 || isIE6 || isIE7) {
                if (tab.children[0].rows(0).cells.length >= 11) {
                    closeTab(tab.children[0].rows(0).cells(2).children[0].rows(0).cells(2).children[0]);
                }
            } else {
                if (tab.childNodes[0].rows[0].cells.length >= 11) {
                    closeTab(tab.childNodes[0].rows[0].cells[2].childNodes[0].rows[0].cells[2].childNodes[0]);
                }
            }

            var url = '';
            if (obj == null) {
                var objt = document.createElement("a");
                objt.id = 0;
                url = welcomeUrl;
                objt.innerText = "首页";
                obj = objt;
            }
            url = idToUrl[obj.id];
            if (url == null || url == '') {
                return;
            }
            var tab_a = document.getElementById("tab_" + obj.id);
            if (tab_a != null) {
//		对象已经打开
                setTab_Active(tab_a);
                openFrame(tab_a.name);
                MM_showHideLayers('menuLayer', null, 'hide');
                return;
            }
            var TabHtml = "";
            TabHtml += "<td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" background=\"" + getImageSrc("main_TabActive_bg.gif") + "\">"
                + "<tr><td align=\"left\">"
                + "<img src=\"" + getImageSrc("main_TabActive_left.gif") + "\" width=\"16\" height=\"23\"></td>"
                + "<td nowrap class=\"tabLinkText\"><a href=\"#\" id=\"tab_" + obj.id + "\" name=\"" + url + "\" onclick=\"showFrame(this);\">" + obj.innerText + "</a></td>"
                + "<td align=\"right\" ><a href=\"#\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image41','','" + getImageSrc("main_TabActive_close.gif") + "',1)\" onClick=\"closeTab(this)\">"
                + "<img src=\"" + getImageSrc("main_TabActive_closeOn.gif") + "\"  alt=\"关闭标签\" name=\"Image17\" width=\"27\" height=\"23\" border=\"0\"></a></td>"
                + "</tr></table></td><td><img src=\"" + getImageSrc("spacer.gif") + "\" width=\"4\" height=\"1\"></td>"
            var divHtmlBody = "";
            if (tab.innerHTML == null || tab.innerHTML == "") {
                divHtmlBody = TabHtml;
            } else {
                if (isIE8 || isIE6 || isIE7) {
                    divHtmlBody = tab.children[0].rows(0).innerHTML + TabHtml;
                } else {
                    divHtmlBody = tab.childNodes[0].rows[0].innerHTML + TabHtml;
                }

            }
            divHtmlHead = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>";
            divHtmlFoot = "</tr></table>";
            tab.innerHTML = divHtmlHead + divHtmlBody + divHtmlFoot;
            if (lastTab != null) {
                setTab_unActive(lastTab);
            }
            lastTab = document.getElementById("tab_" + obj.id);
            openFrame(url);
            MM_showHideLayers('menuLayer', null, 'hide');
        }

        function MM_openWelcome() {
            var obj = document.createElement("a");
            obj.id = 0;
            obj.url = welcomeUrl;
            obj.innerText = "首页";
            var tab_a = document.getElementById("tab_" + obj.id);
            if (tab_a != null) {
//		对象已经打开
                setTab_Active(tab_a);
                openFrame(tab_a.name);
                MM_showHideLayers('menuLayer', null, 'hide');
                return;
            }
            var TabHtml = "";
            TabHtml += "<td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" background=\"" + getImageSrc("main_TabActive_bg.gif") + "\">"
                + "<tr><td align=\"left\">"
                + "<img src=\"" + getImageSrc("main_TabActive_left.gif") + "\" width=\"16\" height=\"23\"></td>"
                + "<td nowrap class=\"tabLinkText\"><a href=\"#\" id=\"tab_" + obj.id + "\" name=\"" + obj.url + "\" onclick=\"showFrame(this);\">" + obj.innerText + "</a></td>"
                + "<td align=\"right\" ><a href=\"#\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image41','','" + getImageSrc("main_TabActive_close.gif") + "',1)\" >"
                + "<img src=\"" + getImageSrc("main_TabActive_right.gif") + "\"  name=\"Image17\" width=\"27\" height=\"23\" border=\"0\"></a></td>"
                + "</tr></table></td><td><img src=\"" + getImageSrc("spacer.gif") + "\" width=\"4\" height=\"1\"></td>"
            var divHtmlBody = "";
            if (tab.innerHTML == null || tab.innerHTML == "") {
                divHtmlBody = TabHtml;
            } else {
                if (isIE8 || isIE6 || isIE7) {
                    divHtmlBody = tab.children[0].rows(0).innerHTML + TabHtml;
                } else {
                    divHtmlBody = tab.childNodes[0].rows[0].innerHTML + TabHtml;
                }
            }
            divHtmlHead = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>";
            divHtmlFoot = "</tr></table>";
            tab.innerHTML = divHtmlHead + divHtmlBody + divHtmlFoot;
            if (lastTab != null) {
                setTab_unActive(lastTab);
            }
            lastTab = document.getElementById("tab_" + obj.id);
            var url = idToUrl[obj.id];
            openFrame(url);
            MM_showHideLayers('menuLayer', null, 'hide');
        }

        function setTab_Active(obj) {
            if (lastTab != null) {
                setTab_unActive(lastTab);
            }
            if (obj == null) {
                return;
            }
            var main_Tab_right = getImageSrc("main_TabActive_closeOn.gif");
            var closed = "onClick=\"closeTab(this)\"";
            var alt = "alt=\"关闭标签\"";
            if (obj.id == "tab_0") {
                main_Tab_right = getImageSrc("main_TabActive_right.gif");
                closed = "";
                alt = "";
            }
            lastTab = obj;
            oTd = obj.parentElement;
            oTr = oTd.parentElement;
            oTb = oTr.parentElement;

            prev = oTd.previousSibling;
            if (isIE8 || isIE6 || isIE7) {
                oTb.background = getImageSrc("main_TabActive_bg.gif");
                oTd.background = getImageSrc("main_TabActive_bg.gif");
                prev.children[0].src = getImageSrc("main_TabActive_left.gif");
            } else {
                oTb.setAttribute('style', 'background-image:url("/gddl/imgs/main/blue/main_TabActive_bg.gif");');
                oTd.setAttribute('style', 'background-image:url("/gddl/imgs/main/blue/main_TabActive_bg.gif");');
                prev.childNodes[0].src = getImageSrc("main_TabActive_left.gif");
            }

            next = oTd.nextSibling;
            next.innerHTML = "<a href=\"#\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image41','','" + getImageSrc("main_TabActive_close.gif") + "',1)\" " + closed + ">"
                + "<img src=\"" + main_Tab_right + "\"  " + alt + " name=\"Image17\" width=\"27\" height=\"23\" border=\"0\"></a>";
        }

        function setTab_unActive(obj) {
            var main_Tab_right = getImageSrc("main_Tab_close_on.gif");
            var closed = "onClick=\"closeTab(this)\"";
            if (obj.id == "tab_0") {
                main_Tab_right = getImageSrc("main_Tab_close.gif");
                closed = "";
            }
            k = document.getElementById(obj.id);
            oTd = k.parentElement;
            oTr = oTd.parentElement;
            oTb = oTr.parentElement;

            prev = oTd.previousSibling;
            if (isIE8 || isIE6 || isIE7) {
                oTb.background = getImageSrc("main_Tab_bg.gif");
                oTd.background = getImageSrc("main_Tab_bg.gif");
                prev.children[0].src = getImageSrc("main_Tab_left.gif");
            } else {
                oTb.setAttribute('style', 'background-image:url("/gddl/imgs/main/blue/main_Tab_bg.gif");');
                oTd.setAttribute('style', 'background-image:url("/gddl/imgs/main/blue/main_Tab_bg.gif");');
                prev.childNodes[0].src = getImageSrc("main_Tab_left.gif");
            }

            next = oTd.nextSibling;
            next.innerHTML = "<a href=\"#\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image41','','" + getImageSrc("main_TabActive_close.gif") + "',1)\" " + closed + ">"
                + "<img src=\"" + main_Tab_right + "\" name=\"Image17\" width=\"27\" height=\"23\" border=\"0\"></a>";
        }

        function getImageSrc(name) {
            var url = "http://" + document.location.host + document.location.pathname;
            var end = url.indexOf("/app");
            url = url.substring(0, end);
            src = "/gddl/" + imgPath + "/" + name;
            return src
        }

        function openFrame(name) {
            var pageName;
            if (name == null || name == "") {
                pageName = welcomeUrl;
            } else {
                pageName = name;
            }
            var url;
            if (pageName.indexOf("firefox.exe") > -1) {
                url = pageName;
                var objShell = new ActiveXObject("wscript.shell");
                var a = objShell.run(url);
            } else if (pageName.indexOf("tc_http:") > -1) {
                url = pageName.replace('tc_http:', 'http:');
                window.open(url);
            } else if (pageName.indexOf("http:") > -1) {
                url = pageName;
                document.all.mainFrame.src = url;
            } else {
                url = "http://" + document.location.host + document.location.pathname;
                url = url + "?service=page/" + pageName;
                document.all.mainFrame.src = url;
            }
            if (!(isIE8 || isIE6 || isIE7)) {
                document.all.mainFrame.height = document.body.scrollHeight - 96;
            }
        }

        function showFrame(obj) {
            debugger;
            var pageName;
            if (obj == null) {
                pageName = welcomeUrl;
            } else {
                pageName = obj.name;
            }
            setTab_Active(obj);
            openFrame(pageName);
        }

        function closeTab(obj) {
            oTd = obj.parentElement;
            lastoTd = oTd.previousSibling;
            if (isIE8 || isIE6 || isIE7) {
                lastobj = lastoTd.children(0);
            } else {
                lastobj = lastoTd.childNodes[0];
            }
            if (lastobj.name == welcomeUrl) {
                alert("不能关闭首页");
                return;
            }
            oTr = oTd.parentElement;
            oTb = oTr.parentElement;
            oTD = oTb.parentElement.parentElement;
            oTR = oTD.parentElement;
            ci = oTD.cellIndex;
            //alert(lastTab.id +"--------"+ lastobj.id);
            if (lastTab == lastobj) {
                lastTab = null;
                showFrame(document.getElementById("tab_0"));
            }
            oTR.deleteCell(ci);
            oTR.deleteCell(ci);
        }

        var lindex = 1;
        var rindex = 0;
        function moveright(obj) {
            MM_showHideLayers('menuLayer', null, 'hide');
            var tr = obj.parentElement;
            var len = tr.cells.length - 2;
            var rwidth = 0;
            var lwidth = 0;
            if (rindex < len) {
                rwidth = displayTd(tr, rindex, "");
                rindex = rindex + 2;
            } else {
                return;
            }

            lwidth = displayTd(tr, lindex, "none");
            lindex = lindex + 2;
            i = 1;
            while (lwidth + 24 < rwidth && i++ < 10) {
                lwidth = lwidth + displayTd(tr, lindex, "none");
                lindex = lindex + 2;
            }
        }
        function displayTd(tr, index, displayed) {
            tr.cells(index).style.display = displayed;
            tdwidth = tr.cells(index).clientWidth;
            tr.cells(index + 1).style.display = displayed;
            tdwidth = tdwidth + tr.cells(index + 1).clientWidth;
            return tdwidth;
        }
        function moveleft(obj) {
            MM_showHideLayers('menuLayer', null, 'hide');
            var tr = obj.parentElement;
            var rwidth = 0;
            var lwidth = 0;
            if (lindex > 1) {
                lindex = lindex - 2;
                lwidth = displayTd(tr, lindex, "");
            } else {
                return;
            }
            rindex = rindex - 2;
            rwidth = displayTd(tr, rindex, "none");

            i = 1;
            while (lwidth > rwidth + 24 && i++ < 10) {
                rindex = rindex - 2;
                rwidth = displayTd(tr, rindex, "none");

            }
        }
        function initSbar() {
            maxwidth = document.body.clientWidth - 120;
            if (sollbar.clientWidth < maxwidth) {
                sollbar.rows[0].deleteCell(0);
                len = sollbar.rows[0].cells.length;
                sollbar.rows[0].deleteCell(len - 1);
                return;
            }
            var twidth = 0;
            for (i = 0; i < sollbar.rows[0].cells.length - 1; i = i + 2) {
                twidth = twidth + sollbar.rows[0].cells[i].clientWidth;
                twidth = twidth + sollbar.rows[0].cells[i + 1].clientWidth;
                rindex = i;
                if (twidth > maxwidth) {
                    break;
                }
            }
            for (i = rindex; i < sollbar.rows[0].cells.length - 1; i++) {
                sollbar.rows[0].cells[i].style.display = "none";
            }
        }
        //function iFrameHeight() {
        //    var ifm= document.getElementById("mainFrame");
        //    var subWeb = document.frames ? document.frames["mainFrame"].document : ifm.contentDocument;
        //    if(ifm != null && subWeb != null) {
        //        ifm.height = subWeb.body.scrollHeight;
        //    }
        //}
    </script>
    <!--onLoad="MM_preloadImages('imgs/main/search_bt_on.gif','imgs/main/main_TabActive_closeOn.gif')"-->
</head>
<body bgcolor="#B3C8DD" leftmargin="0" topmargin="0" id="mainbody">
<script src="/gddl/js/public/Main.js" type="text/javascript"></script>
<table width="100%" border="0" cellpadding="0" cellspacing="0" background="imgs/main/blue/main_top_bg.gif">
    <tr>
        <td width="10%"><img src="imgs/main/blue/gddc/new_main_top_logo.gif" width="624" height="63"></td>
        <td width="90%" align="right"><img usemap="#Map" src="imgs/main/blue/main_top_tools_3.gif" width="297"
                                           height="63" border="0"></a></td>
    </tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0" background="imgs/main/blue/main_bar_bg.gif">
    <tr>
        <td width="1%"><img src="imgs/main/blue/main_bar_bg.gif" width="9" height="33"></td>
        <td width="74%">
            <table id="sollbar" border="0" cellpadding="0" cellspacing="0"
                   background="imgs/main/blue/main_bar_line_on.gif">
                <tr>
                    <td background="imgs/main/blue/main_bar_bg.gif" style=" cursor:hand;color:white"
                        onclick="moveleft(this)">&lt;&lt;</td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="101"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">计划</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="102"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">合同</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="103"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">数量</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="104"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">采样</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="105"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">入厂化验</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="106"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">结算</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="107"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">入炉化验</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="108"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">调运</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="109"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">盘点</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="111"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">月报管理</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="10014037"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">日报管理</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="113"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">综合查询</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="114"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">文件管理</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap background="imgs/main/blue/main_bar_bg.gif" class="barLinkText"><a href="#" id="116"
                                                                                                  onmouseenter="MM_showHideLayers('menuLayer',this,'show')"
                                                                                                  onClick="MM_showHideLayers('menuLayer',this,'show')">系统管理</a>
                    </td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td nowrap><img src="imgs/main/blue/main_bar_line.gif" width="24" height="33"></td>
                    <td background="imgs/main/blue/main_bar_bg.gif" style=" cursor:hand;color:white"
                        onclick="moveright(this)">&gt;&gt;</td>
                </tr>
            </table>
        </td>
        <td width="25%" align="right"><a onclick="window.open('http://zr.zhiren.net')" href="#"
                                         onMouseOut="MM_swapImgRestore()"
                                         onMouseOver="MM_swapImage('Image42','','imgs/main/blue/PowerBy_style03_on.gif',1)"><img
                src="imgs/main/blue/PowerBy_style03.gif" name="Image42" width="77" height="33" border="0"></a></td>
    </tr>
</table>
<table width="100%" border="0" cellpadding="8" cellspacing="0" background="imgs/main/blue/main_center_bg.gif"
       class="center-bg">
    <tr>
        <td valign="top">
            <div id="tab"></div>
            <table id="mainWindowTb" width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#B7B7B7">
                <tr>
                    <td valign="top" bgcolor="#FFFFFF">
                        <iframe id="mainFrame" width="100%" marginwidth="0" height="100%" marginheight="0"
                                scrolling="no" frameborder="0"></iframe>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="right"><img src="imgs/main/blue/spacer.gif" width="1" height="1"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<div id="menuLayer" class="menuLayer01">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="8px"><img src="imgs/main/blue/spacer.gif" width="8" height="1"></td>
            <td background="imgs/main/blue/main_menu_jiantou_bg.gif"><img src="imgs/main/blue/spacer.gif" id="jiantouid"
                                                                          width="20" height="1"></td>
            <td width="100%" background="imgs/main/blue/main_menu_jiantou_bg.gif"><img
                    src="imgs/main/blue/main_menu_jiantou.gif" width="11" height="8"></td>
            <td width="8px"><img src="imgs/main/blue/spacer.gif" width="8" height="1"></td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="8px"><img src="imgs/main/blue/spacer.gif" width="8" height="1"></td>
            <td width="1px" bgcolor="#B7B7B7"><img src="imgs/main/blue/spacer.gif" width="1" height="1"></td>
            <td width="100%">
                <table width="100%" border="0" cellpadding="12" cellspacing="0"
                       background="imgs/main/blue/main_menu_bg.gif" bgcolor="#2A5E8E" class="center-bg">
                    <tr>
                        <td valign="top" background="imgs/main/blue/main_menu_tu.gif" class="noRepeatBG">
                            <div id=Menu101 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">计划录入</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1010103"
                                                                            url="Niandjh_caig"
                                                                            onclick="MM_openTab(this);">年度采购计划</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10020370637"
                                                                            url="Niandjh_zaf"
                                                                            onclick="MM_openTab(this);">年度燃料杂费计划</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10022176033"
                                                                            url="Niandjh_zhib"
                                                                            onclick="MM_openTab(this);">年计划相关指标预测</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10022176034"
                                                                            url="Yuedjh_caig"
                                                                            onclick="MM_openTab(this);">月度采购计划</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10022176035"
                                                                            url="Yuedjh_zhib"
                                                                            onclick="MM_openTab(this);">月计划相关指标预测</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">计划查询</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1010303"
                                                                            url="Niandcgjhcx"
                                                                            onclick="MM_openTab(this);">年度采购计划查询</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1010305"
                                                                            url="Niandrlzhbmdjcs"
                                                                            onclick="MM_openTab(this);">年度指标计划查询</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10020370703"
                                                                            url="Niandjhcx" onclick="MM_openTab(this);">年度计划提交</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10022176108"
                                                                            url="Niandjhcx&lx=return"
                                                                            onclick="MM_openTab(this);">年度计划回退</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10022176109"
                                                                            url="YuedjhReport"
                                                                            onclick="MM_openTab(this);">月度计划提交</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10022176110"
                                                                            url="YuedjhReport&lx=return"
                                                                            onclick="MM_openTab(this);">月度计划回退</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10024416233"
                                                                            url="Yuedjhcx" onclick="MM_openTab(this);">月度计划查询</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10022176112"
                                                                            url="Jihzbdy" onclick="MM_openTab(this);">计划指标定义</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu102 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">合同维护</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020101"
                                                                            url="Mobgl"
                                                                            onclick="MM_openTab(this);">煤款模板</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10012234"
                                                                            url="Yunshtmb" onclick="MM_openTab(this);">运输模板</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020102"
                                                                            url="Diancgmht" onclick="MM_openTab(this);">煤款合同</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020103"
                                                                            url="Yunsht" onclick="MM_openTab(this);">运输合同</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020104"
                                                                            url="Hetbcxy" onclick="MM_openTab(this);">补充合同</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1006700"
                                                                            url="Hetgl"
                                                                            onclick="MM_openTab(this);">合同关联</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">审核</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020201"
                                                                            url="Hetsh"
                                                                            onclick="MM_openTab(this);">合同审核</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">查询打印</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020301"
                                                                            url="Hetcx"
                                                                            onclick="MM_openTab(this);">合同查询</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020302"
                                                                            url="Hetdy"
                                                                            onclick="MM_openTab(this);">合同打印</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020303"
                                                                            url="Hetltj" onclick="MM_openTab(this);">合同量统计</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1020304"
                                                                            url="Hetdxl" onclick="MM_openTab(this);">合同兑现率</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu103 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030102"
                                                                            url="Shulxtsz" onclick="MM_openTab(this);">数量系统设置</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030105"
                                                                            url="Yunsl" onclick="MM_openTab(this);">运损率设置</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1008545"
                                                                            url="Xiecfs" onclick="MM_openTab(this);">卸车方式</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1008588"
                                                                            url="Qichjcsjpp"
                                                                            onclick="MM_openTab(this);">数据自动导入设置</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1008589"
                                                                            url="Qichysdwpp"
                                                                            onclick="MM_openTab(this);">运输单位匹配</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10013592"
                                                                            url="Qiccht" onclick="MM_openTab(this);">车牌头设置</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">数据处理</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030103"
                                                                            url="Fahxg"
                                                                            onclick="MM_openTab(this);">发货修改</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1007651"
                                                                            url="ShulxgIndex"
                                                                            onclick="MM_openTab(this);">数量信息修改</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1008596"
                                                                            url="Shujdrhz&lx=QY"
                                                                            onclick="MM_openTab(this);">数据导入</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node508155052"
                                                                            url="DataImport&lx=QY"
                                                                            onclick="MM_openTab(this);">数据导入(多选)</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030313"
                                                                            url="ShujblQ" onclick="MM_openTab(this);">公路数据补录</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030314"
                                                                            url="Shujsh&lx=QY_SH"
                                                                            onclick="MM_openTab(this);">公路数据审核</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030315"
                                                                            url="Shujsh&lx=QY_QXSH"
                                                                            onclick="MM_openTab(this);">公路取消审核</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10085407"
                                                                            url="Shujdrxg" onclick="MM_openTab(this);">数据导入修改</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10057160"
                                                                            url="Shujpp" onclick="MM_openTab(this);">数据匹配</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">查询&报表</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030303"
                                                                            url="Xinxcx" onclick="MM_openTab(this);">汽车运单查询</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030311"
                                                                            url="Qicjjcx&lx=PRINT_BAOER"
                                                                            onclick="MM_openTab(this);">汽车检斤单(默认)</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030316"
                                                                            url="Jilrb&lx=3"
                                                                            onclick="MM_openTab(this);">过衡日报(汽车)</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030317"
                                                                            url="Jilrb&lx=6"
                                                                            onclick="MM_openTab(this);">收煤日报(汽车)</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030318"
                                                                            url="Jiltz&lx=3&lr=jiltzpz"
                                                                            onclick="MM_openTab(this);">数量台帐(汽车)</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030504"
                                                                            url="Zonghcx_sl"
                                                                            onclick="MM_openTab(this);">综合查询</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1008613"
                                                                            url="JianjdIndex&lx=3"
                                                                            onclick="MM_openTab(this);">格式检斤单(汽车)</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100144838"
                                                                            url="Yunsdwcx" onclick="MM_openTab(this);">运输数量总查询</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu104 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040101"
                                                                            url="Bum" onclick="MM_openTab(this);">部门</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040102"
                                                                            url="Leib"
                                                                            onclick="MM_openTab(this);">样品类别</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040103"
                                                                            url="Caizhfs" onclick="MM_openTab(this);">采制化方式</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10017450"
                                                                            url="Fenkcyfs" onclick="MM_openTab(this);">分矿采样方式</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">采样</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040201"
                                                                            url="Caiyxg" onclick="MM_openTab(this);">进厂批号修改</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040202"
                                                                            url="Caiywh" onclick="MM_openTab(this);">采样信息维护</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040203"
                                                                            url="Caiybmd&lx=Tiel"
                                                                            onclick="MM_openTab(this);">火车采样单</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10012144"
                                                                            url="Caiybmd&lx=Gongl"
                                                                            onclick="MM_openTab(this);">汽车采样单</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040204"
                                                                            url="Zhuanmd&lx=Caiybmzzybm"
                                                                            onclick="MM_openTab(this);">采/制样转码查询</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1006897"
                                                                            url="Caizhbmxg" onclick="MM_openTab(this);">采制化编码修改</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10011604"
                                                                            url="Zhuanmd&lx=Zhuanmd_13"
                                                                            onclick="MM_openTab(this);">采/化编码查询</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">制样</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1040301"
                                                                            url="Zhuanmd&lx=all_fah_daohrq_yunsdw"
                                                                            onclick="MM_openTab(this);">解码查询</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu105 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050101"
                                                                            url="Yuansxm" onclick="MM_openTab(this);">元素分析项目</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050102"
                                                                            url="Yuansfx" onclick="MM_openTab(this);">元素分析</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">化验</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050202"
                                                                            url="Huaylr" onclick="MM_openTab(this);">化验值录入</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">审核</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050401"
                                                                            url="Huayyjsh" onclick="MM_openTab(this);">一级审核</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050402"
                                                                            url="Ruchyejsh_duotsh"
                                                                            onclick="MM_openTab(this);">二级审核</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10084077"
                                                                            url="Ersht" onclick="MM_openTab(this);">二级审核回退</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">报表</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050501"
                                                                            url="Zhuanmd&lx=Zhiybmzhybm"
                                                                            onclick="MM_openTab(this);">编码查询</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1003431"
                                                                            url="Zhuanmd&lx=all_fah_daohrq_yunsdw"
                                                                            onclick="MM_openTab(this);">转码单汇总</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050503"
                                                                            url="Huaybgd&lx=Huaybgd"
                                                                            onclick="MM_openTab(this);">化验报告单</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050504"
                                                                            url="Meizjyrb&lx=Meizjyrb_zhilb_bm"
                                                                            onclick="MM_openTab(this);">化验日报</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1050505"
                                                                            url="Huayyb" onclick="MM_openTab(this);">化验月报</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1005056"
                                                                            url="Zhiltz" onclick="MM_openTab(this);">化验台帐</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu106 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060101"
                                                                            url="Feiymc" onclick="MM_openTab(this);">费用名称</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060102"
                                                                            url="Feiyxm" onclick="MM_openTab(this);">费用项目</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060103"
                                                                            url="Jiesszbmb" onclick="MM_openTab(this);">结算设置</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060104"
                                                                            url="Jiesszfab" onclick="MM_openTab(this);">结算设置方案</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060105"
                                                                            url="Shoukdw" onclick="MM_openTab(this);">收款单位</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1005115"
                                                                            url="Kuangfhy" onclick="MM_openTab(this);">矿方化验</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">货票</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060201"
                                                                            url="Chephhd" onclick="MM_openTab(this);">车号核对</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060202"
                                                                            url="Huophd" onclick="MM_openTab(this);">货票核对</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060203"
                                                                            url="Huopfytj" onclick="MM_openTab(this);">费用统计</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">煤款结算</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060301"
                                                                            url="Jiesxz" onclick="MM_openTab(this);">煤款结算</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060302"
                                                                            url="Jiesdxg" onclick="MM_openTab(this);">结算修改</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1060303"
                                                                            url="Balancebill&lx=jiesb,jiesyfb"
                                                                            onclick="MM_openTab(this);">结算单</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1005297"
                                                                            url="Jiestz" onclick="MM_openTab(this);">结算台账</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100172926"
                                                                            url="Yunfjstjtz"
                                                                            onclick="MM_openTab(this);">运费结算台账</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">结算审核</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1005117"
                                                                            url="Jieslcext" onclick="MM_openTab(this);">审核</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu107 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础维护</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10068002"
                                                                            url="Jizfzbext" onclick="MM_openTab(this);">机组维护</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10068001"
                                                                            url="Rulbzbext" onclick="MM_openTab(this);">班组维护</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">入炉化验</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1070202"
                                                                            url="Meihybext" onclick="MM_openTab(this);">煤耗用</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100111981"
                                                                            url="Rulhysh" onclick="MM_openTab(this);">入炉化验审核</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1070201"
                                                                            url="Rulmzlbext"
                                                                            onclick="MM_openTab(this);">入炉化验</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node508137834"
                                                                            url="Rulshht&lx=Rulshht"
                                                                            onclick="MM_openTab(this);">入炉化验回退</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">报表</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1070404"
                                                                            url="Rulhyd" onclick="MM_openTab(this);">化验报告单</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1070405"
                                                                            url="Rulbb&lx=Rulbb"
                                                                            onclick="MM_openTab(this);">检质报表</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1070406"
                                                                            url="Rulbb&lx=Rultz"
                                                                            onclick="MM_openTab(this);">检质台帐</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu108 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1080101"
                                                                            url="Shebxx" onclick="MM_openTab(this);">接卸设备</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281877"
                                                                            url="Ridyjh" onclick="MM_openTab(this);">日调运计划</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281878"
                                                                            url="Ridyjhcx" onclick="MM_openTab(this);">日调运计划查询</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">预报</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1080201"
                                                                            url="Tielyb" onclick="MM_openTab(this);">铁路预报</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1080202"
                                                                            url="Zhuangctb" onclick="MM_openTab(this);">矿方预报</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">信息录入</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1080301"
                                                                            url="Riscsjwh" onclick="MM_openTab(this);">日生产数据</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">报表</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100121541"
                                                                            url="Riscsjcx" onclick="MM_openTab(this);">日生产数据查询</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">耗用管理</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10011548066"
                                                                            url="Fahhy"
                                                                            onclick="MM_openTab(this);">发货耗用</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10011548067"
                                                                            url="FahhyReport"
                                                                            onclick="MM_openTab(this);">耗用查询</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu109 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">基础设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090101"
                                                                            url="Meiccl" onclick="MM_openTab(this);">煤仓信息设置</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100149019"
                                                                            url="Pandcmwz" onclick="MM_openTab(this);">其他存煤位置</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100149020"
                                                                            url="Meic" onclick="MM_openTab(this);">煤场信息维护</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">盘点填报</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090301"
                                                                            url="Pand_GD" onclick="MM_openTab(this);">盘点填报</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090302"
                                                                            url="PandRy" onclick="MM_openTab(this);">盘点人员</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090303"
                                                                            url="Meicmdcl" onclick="MM_openTab(this);">密度测量</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090304"
                                                                            url="Meicdxcs" onclick="MM_openTab(this);">体积测量</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090306"
                                                                            url="Pandgdcm" onclick="MM_openTab(this);">煤仓存煤</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090307"
                                                                            url="Pandgdqtcm"
                                                                            onclick="MM_openTab(this);">盘点其他存煤</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100149021"
                                                                            url="Panqhbh" onclick="MM_openTab(this);">盘点前后变化</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100149022"
                                                                            url="Pandsx" onclick="MM_openTab(this);">盘点事项说明</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100149023"
                                                                            url="Pandzm" onclick="MM_openTab(this);">盘点账面情况</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100174365"
                                                                            url="Pandzm" onclick="MM_openTab(this);">盘点账面</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10021405068"
                                                                            url="Pand_GDJT" onclick="MM_openTab(this);">盘点录入</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">盘点报表</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090401"
                                                                            url="Pandcx" onclick="MM_openTab(this);">盘点报告</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10021405069"
                                                                            url="PandGDCX" onclick="MM_openTab(this);">盘点查询</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10024468340"
                                                                            url="Pand_GDJT&lx=return"
                                                                            onclick="MM_openTab(this);">盘点回退</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu111 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">中电投cpi 月报</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281763"
                                                                            url="Yuemtgyqkbreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理05表</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281766"
                                                                            url="Yuemthcqkbreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理06表</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281765"
                                                                            url="Yuesyhcqkbreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理07表</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281767"
                                                                            url="Biaomhyqkbreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理08表</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281768"
                                                                            url="Yuemyzlqkbreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理09表</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281769"
                                                                            url="Rucmdj_newreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理10表</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281770"
                                                                            url="yuefdbmdjqkbreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理11表</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node20390209"
                                                                            url="Ranlcbfybreport"
                                                                            onclick="MM_openTab(this);">cpi燃料管理12表</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1007141870"
                                                                            url="Niandhtzxqkreport"
                                                                            onclick="MM_openTab(this);">年度合同执行情况月报</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">月报填报</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10057052"
                                                                            url="Yueslbtb" onclick="MM_openTab(this);">数量填报</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10057053"
                                                                            url="Yuezlbtb" onclick="MM_openTab(this);">质量填报</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281711"
                                                                            url="Yueshchjbtb"
                                                                            onclick="MM_openTab(this);">月煤耗存</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281717"
                                                                            url="Yueshcybtb"
                                                                            onclick="MM_openTab(this);">月油耗存</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281724"
                                                                            url="Yuedmjgmxb"
                                                                            onclick="MM_openTab(this);">入厂煤成本填报</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281741"
                                                                            url="Rucycbtb" onclick="MM_openTab(this);">入厂油成本填报</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281755"
                                                                            url="Changnscsjwh_new"
                                                                            onclick="MM_openTab(this);">财务、生产数据填报</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10025281759"
                                                                            url="Niandhtzxqk"
                                                                            onclick="MM_openTab(this);">年度合同执行情况填报</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">月报上报</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1006287631"
                                                                            url="Yuebshsb" onclick="MM_openTab(this);">月报审核</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu10014037 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">数据维护</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100172922"
                                                                            url="Shouhcrbb_gd"
                                                                            onclick="MM_openTab(this);">收耗存日报维护</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100172923"
                                                                            url="Shouhcfkb" onclick="MM_openTab(this);">日估价维护</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1100101"
                                                                            url="Fahhtwh" onclick="MM_openTab(this);">发货合同维护</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1100102"
                                                                            url="Fahhtpp" onclick="MM_openTab(this);">发货合同匹配</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1004212629"
                                                                            url="Kucmjgds" onclick="MM_openTab(this);">库存煤结构维护</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">数据查询</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10014049"
                                                                            url="Rishctj_GD"
                                                                            onclick="MM_openTab(this);">煤收耗存日报</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10012689"
                                                                            url="Rucrlrzc_dc"
                                                                            onclick="MM_openTab(this);">入厂入炉热值差</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu113 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">台帐</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1130101"
                                                                            url="Jiltz&lx=1"
                                                                            onclick="MM_openTab(this);">数量台帐</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1130102"
                                                                            url="Huayyb" onclick="MM_openTab(this);">质量台帐</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">综合查询</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1130201"
                                                                            url="Zonghcx_sl"
                                                                            onclick="MM_openTab(this);">数量</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1130202"
                                                                            url="Zonghcx_zl"
                                                                            onclick="MM_openTab(this);">质量</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu114 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">公共信息</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1140101"
                                                                            url="Wenjfb" onclick="MM_openTab(this);">文档管理</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1140102"
                                                                            url="WenjAll" onclick="MM_openTab(this);">文档查询</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText"></table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id=Menu116 name="MenuDiv" style="display:none;">
                                <table border="0" cellpadding="6" cellspacing="0" class="menuLinkText">
                                    <tr class="menuLinkText">
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">资源权限管理</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160201"
                                                                            url="RManage" onclick="MM_openTab(this);">资源管理</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160202"
                                                                            url="Zuxxext" onclick="MM_openTab(this);">权限管理</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">人员信息</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160301"
                                                                            url="Renyxxfj" onclick="MM_openTab(this);">人员维护</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160302"
                                                                            url="Renyjbxx" onclick="MM_openTab(this);">修改个人信息</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">系统设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160401"
                                                                            url="Dianc" onclick="MM_openTab(this);">修改电厂信息</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1090102"
                                                                            url="Jiz"
                                                                            onclick="MM_openTab(this);">机组信息</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160402"
                                                                            url="Xitxxext" onclick="MM_openTab(this);">参数维护</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160403"
                                                                            url="Shuzhlfw" onclick="MM_openTab(this);">数值范围</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160410"
                                                                            url="Chez"
                                                                            onclick="MM_openTab(this);">车站信息</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160409"
                                                                            url="Luj"
                                                                            onclick="MM_openTab(this);">路局信息</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160412"
                                                                            url="Liclx"
                                                                            onclick="MM_openTab(this);">里程类型</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160413"
                                                                            url="Lic"
                                                                            onclick="MM_openTab(this);">里程维护</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100543"
                                                                            url="Gongysxg" onclick="MM_openTab(this);">供应商</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100544"
                                                                            url="Meikxx_gd" onclick="MM_openTab(this);">煤矿</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100546"
                                                                            url="Gongysgl" onclick="MM_openTab(this);">供应商煤矿关联</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160303"
                                                                            url="Kuaijtdsz" onclick="MM_openTab(this);">快捷菜单设置</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1030104"
                                                                            url="Shouhdwb" onclick="MM_openTab(this);">收货单位</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1006740"
                                                                            url="Yunsdw" onclick="MM_openTab(this);">运输单位</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160419"
                                                                            url="Jiexx"
                                                                            onclick="MM_openTab(this);">接卸线</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160404"
                                                                            url="Meic"
                                                                            onclick="MM_openTab(this);">煤场</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160420"
                                                                            url="Youg"
                                                                            onclick="MM_openTab(this);">油罐</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160414"
                                                                            url="Meiz"
                                                                            onclick="MM_openTab(this);">煤种</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160415"
                                                                            url="Pinz"
                                                                            onclick="MM_openTab(this);">品种</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160407"
                                                                            url="Shengf"
                                                                            onclick="MM_openTab(this);">省份</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160417"
                                                                            url="Wangjxx"
                                                                            onclick="MM_openTab(this);">网局</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160418"
                                                                            url="Gongs1" onclick="MM_openTab(this);">基础公式</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160405"
                                                                            url="Jihkj"
                                                                            onclick="MM_openTab(this);">计划口径</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160406"
                                                                            url="Yunsfs" onclick="MM_openTab(this);">运输方式</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td valign=top>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160408"
                                                                            url="Dianczyx" onclick="MM_openTab(this);">电厂专用线</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1008248"
                                                                            url="SQL"
                                                                            onclick="MM_openTab(this);">数据库操作</a></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10084174"
                                                                            url="Baobpz" onclick="MM_openTab(this);">报表配置</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node10084176"
                                                                            url="Xitgnpz" onclick="MM_openTab(this);">系统功能配置</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node100146326"
                                                                            url="Xiangmfl" onclick="MM_openTab(this);">项目分类</a>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                        <td nowrap>
                                            <fieldset>
                                                <legend class="menuLinkTitle">流程设置</legend>
                                                <table width="90%" border="0" cellspacing="4" cellpadding="0"
                                                       class="menuLinkText">
                                                    <tr>
                                                        <td>
                                                            <table width="90%" border="0" cellspacing="4"
                                                                   cellpadding="0" class="menuLinkText">
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160501"
                                                                            url="Renyjs" onclick="MM_openTab(this);">人员角色</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap><img
                                                                            src="imgs/main/blue/title_img.gif"><a
                                                                            href="#" title='单击打开' id="Node1160502"
                                                                            url="Gongzlgl" onclick="MM_openTab(this);">工作流程</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <table onMouseOut="MM_showHideLayers('menuLayer',null,'hide')" width="100%" border="0"
                                   cellspacing="0" cellpadding="0">
                                <tr>
                                    <td align="right" title='单击关闭菜单'><img src="imgs/main/blue/main_menu_closeBT.gif"
                                                                          width="17" height="16"
                                                                          onClick="MM_showHideLayers('menuLayer',null,'hide')">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="1px" bgcolor="#B7B7B7"><img src="imgs/main/blue/spacer.gif" width="1" height="1"></td>
            <td width="3" valign="top" bgcolor="#555555">
                <table width="100%" height="4" border="0" cellpadding="0" cellspacing="0" bgcolor="#174570">
                    <tr>
                        <td><img src="imgs/main/blue/spacer.gif" width="3" height="4"></td>
                    </tr>
                </table>
            </td>
            <td width="5" valign="top"><img src="imgs/main/blue/spacer.gif" width="5" height="1"></td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="8"><img src="imgs/main/blue/spacer.gif" width="8" height="1"></td>
            <td bgcolor="#B7B7B7"><img src="imgs/main/blue/spacer.gif" width="1" height="1"></td>
            <td width="3" bgcolor="#555555"><img src="imgs/main/blue/spacer.gif" width="3" height="1"></td>
            <td width="5"><img src="imgs/main/blue/spacer.gif" width="5" height="1"></td>
        </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="8"><img src="imgs/main/blue/spacer.gif" width="8" height="1"></td>
            <td width="6"><img src="imgs/main/blue/spacer.gif" width="4" height="4"></td>
            <td width="100%" bgcolor="#555555"><img src="imgs/main/blue/spacer.gif" width="1" height="1"></td>
            <td width="3" bgcolor="#555555"><img src="imgs/main/blue/spacer.gif" width="3" height="1"></td>
            <td width="5"><img src="imgs/main/blue/spacer.gif" width="5" height="1"></td>
        </tr>
    </table>
</div>
<map name="Map">
    <area shape="rect" coords="177,7,210,53"
          onclick="if(confirm('页面将跳转至登陆界面，确认？')==true){window.parent.location.href='http://'+document.location.host+document.location.pathname+'?service=page/Login';}"
          href="#">
    <area shape="rect" coords="140,7,173,53"
          onclick="window.open('http://'+document.location.host+document.location.pathname+'?service=page/helpdoc' );"
          href="#">
    <area shape="rect" coords="214,7,245,53" onclick="if(confirm('系统将退出，确认？')==true){window.close();}" href="#">
</map>
</body>
<script type="text/javascript">
    var idToUrl = new Object();
    idToUrl['Node101'] = '#';
    idToUrl['Node10101'] = '#';
    idToUrl['Node10201'] = '#';
    idToUrl['Node10301'] = '#';
    idToUrl['Node10401'] = '#';
    idToUrl['Node10501'] = '#';
    idToUrl['Node10601'] = '#';
    idToUrl['Node10801'] = '#';
    idToUrl['Node10901'] = '#';
    idToUrl['Node11301'] = '#';
    idToUrl['Node11401'] = '#';
    idToUrl['Node11601'] = 'Login';
    idToUrl['Node1005117'] = 'Jieslcext';
    idToUrl['Node1020101'] = 'Mobgl';
    idToUrl['Node1020201'] = 'Hetsh';
    idToUrl['Node1020301'] = 'Hetcx';
    idToUrl['Node1030102'] = 'Shulxtsz';
    idToUrl['Node1030103'] = 'Fahxg';
    idToUrl['Node1040101'] = 'Bum';
    idToUrl['Node1040201'] = 'Caiyxg';
    idToUrl['Node1040301'] = 'Zhuanmd&lx=all_fah_daohrq_yunsdw';
    idToUrl['Node1050101'] = 'Yuansxm';
    idToUrl['Node1050401'] = 'Huayyjsh';
    idToUrl['Node1050501'] = 'Zhuanmd&lx=Zhiybmzhybm';
    idToUrl['Node1060101'] = 'Feiymc';
    idToUrl['Node1060201'] = 'Chephhd';
    idToUrl['Node1060301'] = 'Jiesxz';
    idToUrl['Node1070202'] = 'Meihybext';
    idToUrl['Node1080101'] = 'Shebxx';
    idToUrl['Node1080201'] = 'Tielyb';
    idToUrl['Node1080301'] = 'Riscsjwh';
    idToUrl['Node1090101'] = 'Meiccl';
    idToUrl['Node1090301'] = 'Pand_GD';
    idToUrl['Node1090401'] = 'Pandcx';
    idToUrl['Node1130101'] = 'Jiltz&lx=1';
    idToUrl['Node1130201'] = 'Zonghcx_sl';
    idToUrl['Node1140101'] = 'Wenjfb';
    idToUrl['Node1160201'] = 'RManage';
    idToUrl['Node1160301'] = 'Renyxxfj';
    idToUrl['Node1160401'] = 'Dianc';
    idToUrl['Node1160501'] = 'Renyjs';
    idToUrl['Node10068002'] = 'Jizfzbext';
    idToUrl['Node100172922'] = 'Shouhcrbb_gd';
    idToUrl['Node10011548066'] = 'Fahhy';
    idToUrl['Node10022176112'] = 'Jihzbdy';
    idToUrl['Node102'] = '#';
    idToUrl['Node10202'] = '#';
    idToUrl['Node10402'] = '#';
    idToUrl['Node10502'] = '#';
    idToUrl['Node10602'] = '#';
    idToUrl['Node10802'] = '#';
    idToUrl['Node11302'] = '#';
    idToUrl['Node11602'] = '#';
    idToUrl['Node1003431'] = 'Zhuanmd&lx=all_fah_daohrq_yunsdw';
    idToUrl['Node1020302'] = 'Hetdy';
    idToUrl['Node1030105'] = 'Yunsl';
    idToUrl['Node1030303'] = 'Xinxcx';
    idToUrl['Node1040102'] = 'Leib';
    idToUrl['Node1040202'] = 'Caiywh';
    idToUrl['Node1050102'] = 'Yuansfx';
    idToUrl['Node1050202'] = 'Huaylr';
    idToUrl['Node1050402'] = 'Ruchyejsh_duotsh';
    idToUrl['Node1060102'] = 'Feiyxm';
    idToUrl['Node1060202'] = 'Huophd';
    idToUrl['Node1060302'] = 'Jiesdxg';
    idToUrl['Node1080202'] = 'Zhuangctb';
    idToUrl['Node1090102'] = 'Jiz';
    idToUrl['Node1090302'] = 'PandRy';
    idToUrl['Node1130102'] = 'Huayyb';
    idToUrl['Node1130202'] = 'Zonghcx_zl';
    idToUrl['Node1140102'] = 'WenjAll';
    idToUrl['Node1160202'] = 'Zuxxext';
    idToUrl['Node1160302'] = 'Renyjbxx';
    idToUrl['Node1160502'] = 'Gongzlgl';
    idToUrl['Node10012234'] = 'Yunshtmb';
    idToUrl['Node10014044'] = '数据维护';
    idToUrl['Node10014049'] = 'Rishctj_GD';
    idToUrl['Node10068001'] = 'Rulbzbext';
    idToUrl['Node100111981'] = 'Rulhysh';
    idToUrl['Node100130455'] = '#';
    idToUrl['Node100149019'] = 'Pandcmwz';
    idToUrl['Node100172923'] = 'Shouhcfkb';
    idToUrl['Node10011548067'] = 'FahhyReport';
    idToUrl['Node10021405069'] = 'PandGDCX';
    idToUrl['Node10025281877'] = 'Ridyjh';
    idToUrl['Node103'] = '#';
    idToUrl['Node10103'] = '#';
    idToUrl['Node10203'] = '#';
    idToUrl['Node10403'] = '#';
    idToUrl['Node10603'] = '#';
    idToUrl['Node10702'] = '';
    idToUrl['Node10803'] = '#';
    idToUrl['Node10903'] = '#';
    idToUrl['Node11603'] = '#';
    idToUrl['Node11604'] = '#';
    idToUrl['Node1007651'] = 'ShulxgIndex';
    idToUrl['Node1010303'] = 'Niandcgjhcx';
    idToUrl['Node1020102'] = 'Diancgmht';
    idToUrl['Node1020303'] = 'Hetltj';
    idToUrl['Node1040103'] = 'Caizhfs';
    idToUrl['Node1040203'] = 'Caiybmd&lx=Tiel';
    idToUrl['Node1060103'] = 'Jiesszbmb';
    idToUrl['Node1060203'] = 'Huopfytj';
    idToUrl['Node1060303'] = 'Balancebill&lx=jiesb,jiesyfb';
    idToUrl['Node1070201'] = 'Rulmzlbext';
    idToUrl['Node1090303'] = 'Meicmdcl';
    idToUrl['Node1100101'] = 'Fahhtwh';
    idToUrl['Node1160402'] = 'Xitxxext';
    idToUrl['Node10014046'] = 'project';
    idToUrl['Node10084077'] = 'Ersht';
    idToUrl['Node100149020'] = 'Meic';
    idToUrl['Node10024468340'] = 'Pand_GDJT&lx=return';
    idToUrl['Node10025281763'] = 'Yuemtgyqkbreport';
    idToUrl['Node10025281878'] = 'Ridyjhcx';
    idToUrl['Node104'] = '#';
    idToUrl['Node10504'] = '#';
    idToUrl['Node10904'] = '#';
    idToUrl['Node11103'] = '';
    idToUrl['Node11605'] = '#';
    idToUrl['Node1010103'] = 'Niandjh_caig';
    idToUrl['Node1020103'] = 'Yunsht';
    idToUrl['Node1020304'] = 'Hetdxl';
    idToUrl['Node1050503'] = 'Huaybgd&lx=Huaybgd';
    idToUrl['Node1060104'] = 'Jiesszfab';
    idToUrl['Node1070404'] = 'Rulhyd';
    idToUrl['Node1090304'] = 'Meicdxcs';
    idToUrl['Node1100102'] = 'Fahhtpp';
    idToUrl['Node1160403'] = 'Shuzhlfw';
    idToUrl['Node10012144'] = 'Caiybmd&lx=Gongl';
    idToUrl['Node10012689'] = 'Rucrlrzc_dc';
    idToUrl['Node10017450'] = 'Fenkcyfs';
    idToUrl['Node10057052'] = 'Yueslbtb';
    idToUrl['Node508137834'] = 'Rulshht&lx=Rulshht';
    idToUrl['Node10022176111'] = '#';
    idToUrl['Node10025281766'] = 'Yuemthcqkbreport';
    idToUrl['Node105'] = '#';
    idToUrl['Node10505'] = '#';
    idToUrl['Node10704'] = '#';
    idToUrl['Node10805'] = '#';
    idToUrl['Node1008545'] = 'Xiecfs';
    idToUrl['Node1010305'] = 'Niandrlzhbmdjcs';
    idToUrl['Node1020104'] = 'Hetbcxy';
    idToUrl['Node1030311'] = 'Qicjjcx&lx=PRINT_BAOER';
    idToUrl['Node1040204'] = 'Zhuanmd&lx=Caiybmzzybm';
    idToUrl['Node1050504'] = 'Meizjyrb&lx=Meizjyrb_zhilb_bm';
    idToUrl['Node1060105'] = 'Shoukdw';
    idToUrl['Node1070405'] = 'Rulbb&lx=Rulbb';
    idToUrl['Node1160409'] = 'Luj';
    idToUrl['Node1160410'] = 'Chez';
    idToUrl['Node10057053'] = 'Yuezlbtb';
    idToUrl['Node10086430'] = '#';
    idToUrl['Node100121541'] = 'Riscsjcx';
    idToUrl['Node1004212629'] = 'Kucmjgds';
    idToUrl['Node10020370637'] = 'Niandjh_zaf';
    idToUrl['Node10025281765'] = 'Yuesyhcqkbreport';
    idToUrl['Node106'] = '#';
    idToUrl['Node1005115'] = 'Kuangfhy';
    idToUrl['Node1005297'] = 'Jiestz';
    idToUrl['Node1006700'] = 'Hetgl';
    idToUrl['Node1006897'] = 'Caizhbmxg';
    idToUrl['Node1008596'] = 'Shujdrhz&lx=QY';
    idToUrl['Node1050505'] = 'Huayyb';
    idToUrl['Node1070406'] = 'Rulbb&lx=Rultz';
    idToUrl['Node1090306'] = 'Pandgdcm';
    idToUrl['Node100299916'] = '#';
    idToUrl['Node10011548056'] = 'program';
    idToUrl['Node10020370703'] = 'Niandjhcx';
    idToUrl['Node10022176033'] = 'Niandjh_zhib';
    idToUrl['Node10025281767'] = 'Biaomhyqkbreport';
    idToUrl['Node107'] = '#';
    idToUrl['Node1005116'] = '#';
    idToUrl['Node1008588'] = 'Qichjcsjpp';
    idToUrl['Node1090307'] = 'Pandgdqtcm';
    idToUrl['Node1160412'] = 'Liclx';
    idToUrl['Node10011604'] = 'Zhuanmd&lx=Zhuanmd_13';
    idToUrl['Node100172926'] = 'Yunfjstjtz';
    idToUrl['Node10022176034'] = 'Yuedjh_caig';
    idToUrl['Node10022176108'] = 'Niandjhcx&lx=return';
    idToUrl['Node10025281768'] = 'Yuemyzlqkbreport';
    idToUrl['Node108'] = '#';
    idToUrl['Node1008589'] = 'Qichysdwpp';
    idToUrl['Node1008592'] = '#';
    idToUrl['Node1160413'] = 'Lic';
    idToUrl['Node100149021'] = 'Panqhbh';
    idToUrl['Node10022176035'] = 'Yuedjh_zhib';
    idToUrl['Node10022176109'] = 'YuedjhReport';
    idToUrl['Node10025281769'] = 'Rucmdj_newreport';
    idToUrl['Node109'] = '#';
    idToUrl['Node10305'] = '#';
    idToUrl['Node100543'] = 'Gongysxg';
    idToUrl['Node1005056'] = 'Zhiltz';
    idToUrl['Node1030316'] = 'Jilrb&lx=3';
    idToUrl['Node10013592'] = 'Qiccht';
    idToUrl['Node100149022'] = 'Pandsx';
    idToUrl['Node508155052'] = 'DataImport&lx=QY';
    idToUrl['Node10022176110'] = 'YuedjhReport&lx=return';
    idToUrl['Node10025281711'] = 'Yueshchjbtb';
    idToUrl['Node10025281770'] = 'yuefdbmdjqkbreport';
    idToUrl['Node100544'] = 'Meikxx_gd';
    idToUrl['Node1030317'] = 'Jilrb&lx=6';
    idToUrl['Node20390209'] = 'Ranlcbfybreport';
    idToUrl['Node100149023'] = 'Pandzm';
    idToUrl['Node10024416233'] = 'Yuedjhcx';
    idToUrl['Node10025281717'] = 'Yueshcybtb';
    idToUrl['Node111'] = '#';
    idToUrl['Node100546'] = 'Gongysgl';
    idToUrl['Node1030313'] = 'ShujblQ';
    idToUrl['Node1030318'] = 'Jiltz&lx=3&lr=jiltzpz';
    idToUrl['Node100174365'] = 'Pandzm';
    idToUrl['Node10025281724'] = 'Yuedmjgmxb';
    idToUrl['Node1160303'] = 'Kuaijtdsz';
    idToUrl['Node10014037'] = 'project';
    idToUrl['Node10021405068'] = 'Pand_GDJT';
    idToUrl['Node10025281741'] = 'Rucycbtb';
    idToUrl['Node1030104'] = 'Shouhdwb';
    idToUrl['Node1030314'] = 'Shujsh&lx=QY_SH';
    idToUrl['Node10025281755'] = 'Changnscsjwh_new';
    idToUrl['Node113'] = '#';
    idToUrl['Node1006740'] = 'Yunsdw';
    idToUrl['Node1030315'] = 'Shujsh&lx=QY_QXSH';
    idToUrl['Node10025281759'] = 'Niandhtzxqk';
    idToUrl['Node114'] = '#';
    idToUrl['Node1030504'] = 'Zonghcx_sl';
    idToUrl['Node1160419'] = 'Jiexx';
    idToUrl['Node1006287631'] = 'Yuebshsb';
    idToUrl['Node1160404'] = 'Meic';
    idToUrl['Node116'] = '#';
    idToUrl['Node1160420'] = 'Youg';
    idToUrl['Node1160414'] = 'Meiz';
    idToUrl['Node10085407'] = 'Shujdrxg';
    idToUrl['Node1160415'] = 'Pinz';
    idToUrl['Node10057160'] = 'Shujpp';
    idToUrl['Node1160407'] = 'Shengf';
    idToUrl['Node1008613'] = 'JianjdIndex&lx=3';
    idToUrl['Node1160417'] = 'Wangjxx';
    idToUrl['Node1160418'] = 'Gongs1';
    idToUrl['Node1007141870'] = 'Niandhtzxqkreport';
    idToUrl['Node1160405'] = 'Jihkj';
    idToUrl['Node100144838'] = 'Yunsdwcx';
    idToUrl['Node1160406'] = 'Yunsfs';
    idToUrl['Node1160408'] = 'Dianczyx';
    idToUrl['Node1008248'] = 'SQL';
    idToUrl['Node10084174'] = 'Baobpz';
    idToUrl['Node10084176'] = 'Xitgnpz';
    idToUrl['Node100146326'] = 'Xiangmfl';

</script>
<script type="text/javascript">
    MM_showHideLayers('menuLayer', null, 'hide');
    MM_openWelcome();
    initSbar();
    document.getElementById('mainWindowTb').height = bodyHeight - 136;
    //function iFrameHeight() {
    //    var ifm= document.getElementById("mainFrame");
    //    var subWeb = document.frames ? document.frames["mainFrame"].document : ifm.contentDocument;
    //    if(ifm != null && subWeb != null) {
    //        ifm.height = subWeb.body.scrollHeight;
    //    }
    //}
</script>
</html>
