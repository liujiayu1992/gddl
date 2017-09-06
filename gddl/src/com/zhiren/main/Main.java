package com.zhiren.main;
/*
 * ʱ�䣺2008-09-10
 * ���ߣ�chh
 * �ؼ��֣�Mian,��ҳ
 * ����������������˵���ʾ��ѡ�
 */

/*
 * �޸�ʱ�䣺2009-08-07
 * �޸��ˣ�  ww
 * �޸����ݣ�����C/S��������Դ���ڵ㣬����ʾ�ڲ˵�����
 */

/*
 * �޸�ʱ�䣺2009-10-16
 * �޸��ˣ�  ww
 * �޸����ݣ�����Դ���ļ�λ��Ϊ��qx���Ĺ��ˣ�
 * 			����������Դ�����wenjwzΪ��qx������Դ������ʾ�ڳ���Ĳ˵�����
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-04-22
 * ��������URL��#userid�滻Ϊ�û�ID
 */

/*
 * ���ߣ�chh
 * ʱ�䣺2013-09-26
 * ��������������������Ż�����ҳȨ�ޣ�ҳ�����ʱ�����Ż���ҳ��
 * ������1����Դ����������Ϊ���Ż�������Դ��λ��Ϊ�Ż��ķ��ʵ�ַ��
 * ������help��js�߼������ӵ���ҳ��ĵ�js����,�����û��ж��Ƿ��С��Ż�������ԴȨ�ޣ�����еõ�����Դ�ĵ�ַ�������ɵ�����ҳ���js��
 * �ڼ���Ȩ�޲˵�ʱ�����Ż�Ȩ�޲����ء�
 */

/*
 * ���ߣ�chh
 * ʱ�䣺2013-10-26
 * ��������ϵͳ�����������ֱ�ӵ�����ҳ���Ż���ҳ��Ҫ��IE9���ϰ汾��Firefox���������С�
 * �������ڵ�������֮ǰ��鵱ǰ��IE�汾�������9����ֱ�ӵ�����ҳ��
 * ���������ͨ������wscript.shell��������FireFox.exe �����Ż�ҳ�档
 * ע��˹�����Ҫ��IE�н�ȼ�Ϸ�������������վ��ģ������Զ��弶�����趨Ϊ�ͣ���������ACTIVEX�ؼ��Ͳ����
 */

import org.apache.tapestry.IPage;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.validate.Login;

public abstract class Main extends BasePage implements PageValidateListener {

    private String _MenuDiv = "";

    private String idToUrlStr = "";

    public String getBgColor() {
        String bgColor = "#6A6A6A";
        if("blue".equals(MainGlobal.getStyleColor())) {
            bgColor = "#2A5E8E";
        }
        return bgColor;
    }

    public String getBarHtml() {
        StringBuffer strBar = new StringBuffer();
        Visit visit = (Visit)this.getPage().getVisit();
        long renyid = visit.getRenyID();
        JDBCcon con = new JDBCcon();
        //int width = 0;
        ResultSetList rsl = con.getResultSetList(
                "select distinct z.fuid,z.id,z.mingc,z.wenjwz,z.xuh from ziyxxb z,zuqxb zq,renyzqxb r\n" +
                        "where r.renyxxb_id = "+renyid+" and r.zuxxb_id = zq.zuxxb_id and zq.ziyxxb_id = z.id\n"+
                        "and z.jib=1 order by z.xuh");
        strBar.append("<table  id=\"sollbar\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" background=\""+getImgSrc("main_bar_line_on.gif")+"\"><tr>");
        strBar.append("<td background=\""+getImgSrc("main_bar_bg.gif")+"\" style=\" cursor:hand;color:white\" onclick=\"moveleft(this)\">&lt;&lt;</td>");
        while(rsl.next()) {
            //width = width + 24 + rsl.getString(2).length()*12;   width=\""+width+"\"
            strBar.append("<td nowrap><img src=\""+getImgSrc("main_bar_line.gif")+"\" width=\"24\" height=\"33\"></td>");
            strBar.append("<td nowrap background=\""+getImgSrc("main_bar_bg.gif")+"\" class=\"barLinkText\">");
            strBar.append("<a href=\"#\" id=\""+rsl.getLong(1)+"\" onmouseenter=\"MM_showHideLayers('menuLayer',this,'show')\"  onClick=\"MM_showHideLayers('menuLayer',this,'show')\">"+rsl.getString(2)+"</a></td>");
            addMenuDiv(con,rsl.getLong(1));
        }

        strBar.append("<td nowrap><img src=\""+getImgSrc("main_bar_line.gif")+"\" width=\"24\" height=\"33\"></td>");
//        strBar.append("<td nowrap background=\""+getImgSrc("main_bar_bg.gif")+"\" class=\"barLinkText\">");
//        strBar.append("<a onclick=\"window.open('caozsm.html')\" href=\"#\" >����˵��</a></td>");




        strBar.append("<td nowrap><img src=\""+getImgSrc("main_bar_line.gif")+"\" width=\"24\" height=\"33\"></td>");
        strBar.append("<td background=\""+getImgSrc("main_bar_bg.gif")+"\" style=\" cursor:hand;color:white\" onclick=\"moveright(this)\">&gt;&gt;</td>");
        strBar.append("</tr></table>");
        rsl.close();
        con.Close();
        return strBar.toString();
    }

    public String getMainHtml() {
        _MenuDiv = "";
        String titleLogo = getImgPath()+"/"+MainGlobal.getLogoPath()+"/gjdt_nx_nyly_lh.jpg";
//		if(((Visit)this.getPage().getVisit()).isJTUser()) {
//			titleLogo = getImgSrc("main_top_logo_jt.gif");
//		}else
//			if(((Visit)this.getPage().getVisit()).isGSUser()) {
//				titleLogo = getImgSrc("main_top_logo_fgs.gif");
//			}else
//				if(((Visit)this.getPage().getVisit()).isDCUser()) {
//					titleLogo = getImgSrc("main_top_logo_dc.gif");
//				}

        StringBuffer strHtml = new StringBuffer();
//		��������LOGO                  http://localhost:8086/zgdt/app/help/helpdoc.htm
        strHtml.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" background=\""+getImgSrc("main_top_bg.gif")+"\">");
        strHtml.append("<tr>");
        strHtml.append("<td width=\"10%\"><img src=\""+titleLogo+"\" width=\"624\" height=\"63\"></td>");
        strHtml.append("<td width=\"90%\" align=\"right\">");

        strHtml.append("<img usemap=\"#Map\" src=\""+getImgSrc("main_top_tools_3.gif")+"\"  width=\"297\" height=\"63\" border=\"0\">");
        strHtml.append("</a>");
        strHtml.append("</td>");
        strHtml.append("</tr>");
        strHtml.append("</table>");
//		������
        strHtml.append("<table  width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" background=\""+getImgSrc("main_bar_bg.gif")+"\"  >");
        strHtml.append("<tr>");
        strHtml.append("<td width=\"1%\"><img src=\""+getImgSrc("main_bar_bg.gif")+"\" width=\"9\" height=\"33\"></td>");
        strHtml.append("<td width=\"74%\">");
        strHtml.append(getBarHtml());
        strHtml.append("</td>");
        strHtml.append("<td width=\"25%\" align=\"right\">");
        strHtml.append("<a onclick=\"window.open('http://zr.zhiren.net')\" href=\"#\" onMouseOut=\"MM_swapImgRestore()\" onMouseOver=\"MM_swapImage('Image42','','"+getImgSrc("PowerBy_style03_on.gif")+"',1)\">");
        strHtml.append("<img src=\""+getImgSrc("PowerBy_style03.gif")+"\" name=\"Image42\" width=\"77\" height=\"33\" border=\"0\">");
        strHtml.append("</a>");
        strHtml.append("</td>");
        strHtml.append("</tr>");
        strHtml.append("</table>");
//		tab��ҳβ
        strHtml.append("<table  width=\"100%\" border=\"0\" cellpadding=\"8\" cellspacing=\"0\" background=\""+getImgSrc("main_center_bg.gif")+"\" class=\"center-bg\">");
        strHtml.append("<tr>");
        strHtml.append("<td valign=\"top\">");
//		strHtml.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
//		strHtml.append("<tr>");
//		tab��
        strHtml.append("<div id=\"tab\"></div>");
//		strHtml.append("</tr>");
//		strHtml.append("</table>");
//		iframe��
        strHtml.append("<table id=\"mainWindowTb\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#B7B7B7\">");
        strHtml.append("<tr>");
//		height=\"567\"
        strHtml.append("<td valign=\"top\" bgcolor=\"#FFFFFF\">");
        strHtml.append("<iframe id=\"mainFrame\"  width=\"100%\"  marginwidth=\"0\" height=\"100%\" marginheight=\"0\" scrolling=\"no\" frameborder=\"0\"></iframe>");
        strHtml.append("</td>");
        strHtml.append("</tr>");
        strHtml.append("</table>");
//		ҳβ
        strHtml.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        strHtml.append("<tr>");
        strHtml.append("<td align=\"right\">");
        strHtml.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"1\" height=\"1\">");
        strHtml.append("</td>");
        strHtml.append("</tr>");
        strHtml.append("</table>");
//
        strHtml.append("</td>");
        strHtml.append("</tr>");
        strHtml.append("</table>");
        strHtml.append(getMenuLayerDiv());
        if("black".equals(MainGlobal.getStyleColor())) {
            strHtml.append("<map name=\"Map\"><area shape=\"rect\" coords=\"165,7,193,53\" onclick=\"window.open('http://'+document.location.host+document.location.pathname+'?service=page/helpdoc' href=\"#\"><area shape=\"rect\" coords=\"202,7,229,54\" onclick='window.close();' href=\"#\"></map>");
        }else {
            strHtml.append("<map name=\"Map\">" +
                    "<area shape=\"rect\" coords=\"177,7,210,53\" " +
                    "onclick=\"if(confirm('ҳ�潫��ת����½���棬ȷ�ϣ�')==true){window.parent.location.href='http://'+document.location.host+document.location.pathname+'?service=page/Login';}\" href=\"#\">" +
                    "<area shape=\"rect\" coords=\"140,7,173,53\" " +
                    "onclick=\"window.open('http://'+document.location.host+document.location.pathname+'?service=page/helpdoc' );\" href=\"#\">" +
                    "<area shape=\"rect\" coords=\"214,7,245,53\" onclick=\"if(confirm('ϵͳ���˳���ȷ�ϣ�')==true){window.close();}\" href=\"#\"></map>");
        }
        return strHtml.toString();
    }

    public int getMenuRows(JDBCcon con,long fuid) {
        Visit visit = (Visit)this.getPage().getVisit();
        int MaxCols = Integer.parseInt(MainGlobal.getXitsz("ϵͳ�Ӳ˵��������", ""+visit.getDiancxxb_id(), "9"));
        int rows = 1;
        ResultSetList rs = con.getResultSetList(
                "select nvl(count(z.id),0) s from ziyxxb z,zuqxb zq,renyzqxb r\n" +
                        "where r.renyxxb_id = "+visit.getRenyID()+" and r.zuxxb_id = zq.zuxxb_id and zq.ziyxxb_id = z.id\n"+
                        "and z.fuid in (select id from ziyxxb where fuid="+fuid+")  group by z.fuid");
        if(rs.getRows() >9) {
            return 5;
        }
        while(rows++<20) {
            int cols = 0;
            while(rs.next()) {
                cols = cols + (int)Math.ceil(rs.getDouble(0)/rows);
                if(cols > MaxCols) {
                    break;
                }
            }
            if(cols<=MaxCols) {
                break;
            }
            rs.beforefirst();
        }
        rs.close();
//		rows = Math.max(2, (int)Math.ceil(rs.getRows()/8));
        return rows;
    }

    public void addMenuDiv(JDBCcon con,long fuid) {
        Visit visit = (Visit)this.getPage().getVisit();
        int rows = getMenuRows(con,fuid);
        ResultSetList rslnode = null;
//		JDBCcon con = new JDBCcon();
        ResultSetList rsll = con.getResultSetList("select distinct z.id,z.mingc,z.wenjwz,z.xuh from ziyxxb z,zuqxb zq,renyzqxb r\n" +
                "where r.renyxxb_id = "+visit.getRenyID()+" and r.zuxxb_id = zq.zuxxb_id and zq.ziyxxb_id = z.id\n"+
                "and z.fuid = "+fuid+" order by z.xuh");
        _MenuDiv += "<div id=Menu"+fuid+" name=\"MenuDiv\" style=\"display:none;\" >";
        _MenuDiv += "<table border=\"0\" cellpadding=\"6\" cellspacing=\"0\"  class=\"menuLinkText\"><tr class=\"menuLinkText\">";
        String strTableHeader = "<table width=\"90%\" border=\"0\" cellspacing=\"4\" cellpadding=\"0\" class=\"menuLinkText\">";
        String strTableFooter = "</table>";
        StringBuffer sb = new StringBuffer();
        while(rsll.next()) {
            if("Login".equals(rsll.getString(2))) {
                continue;
            }

			/*
			 * �޸�ʱ�䣺2009-08-07
			 * �޸��ˣ�  ww
			 * �޸����ݣ�����C/S��������Դ���ڵ㣬����ʾ�ڲ˵�����
			 */
            if ("qich".equals(rsll.getString("wenjwz"))) {
                continue;
            }

            if ("qx".equals(rsll.getString("wenjwz"))) {
                continue;
            }

            if ("�Ż�".equals(rsll.getString("mingc"))) {
                continue;
            }
            sb.append("<td nowrap><fieldset><legend class=\"menuLinkTitle\">").append(rsll.getString(1))
                    .append("</legend>").append(strTableHeader).append("<tr><td>").append(strTableHeader);
            rslnode = con.getResultSetList(
                    "select distinct z.id,z.mingc,z.wenjwz,z.xuh from ziyxxb z,zuqxb zq,renyzqxb r\n" +
                            "where r.renyxxb_id = "+visit.getRenyID()+" and r.zuxxb_id = zq.zuxxb_id and zq.ziyxxb_id = z.id\n"+
                            "and z.fuid = "+rsll.getLong(0)+" order by z.xuh");
            int i=0;

            while(rslnode.next()) {
                sb.append("<tr><td nowrap>").append("<img src=\"").append(getImgSrc("title_img.gif")).append("\" ><a href=\"#\"  title='������' id=\"Node")
                        .append(rslnode.getLong(0)).append("\" url=\"").append(rslnode.getString(2).replaceAll("#userid", visit.getRenyID()+"")).append("\"  onclick=\"MM_openTab(this);\">").append(rslnode.getString(1))
                        .append("</a></td></tr>");
                if((rslnode.getRow()+1)%rows==0) {
                    sb.append(strTableFooter).append("</td><td valign=top>").append(strTableHeader);
                }
//                System.out.println(rslnode.getString(2));
            }

            if(rslnode.getRows()<rows) {
                i = rows - rslnode.getRows();
                for(int j=0;j<i;j++) {
                    sb.append("<tr><td nowrap>&nbsp;</td></tr>");
                }
            }
            sb.append(strTableFooter).append("</td></tr></table></td>");
        }
        rsll.close();
        _MenuDiv += sb;
        _MenuDiv += "</tr>";
        _MenuDiv += "</table>";
        _MenuDiv += "</div>";
    }

    public String getMenuLayerDiv() {
        StringBuffer strMenu = new StringBuffer();
        strMenu.append("<div id=\"menuLayer\" class=\"menuLayer01\">");
//        strMenu.append("<iframe height=0>");
//        strMenu.append("</iframe>");
//		�����˵���ͷ
        strMenu.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        strMenu.append("<tr>");
        strMenu.append("<td width=\"8px\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"8\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td background=\""+getImgSrc("main_menu_jiantou_bg.gif")+"\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" id=\"jiantouid\" width=\"20\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"100%\" background=\""+getImgSrc("main_menu_jiantou_bg.gif")+"\">");
        strMenu.append("<img src=\""+getImgSrc("main_menu_jiantou.gif")+"\" width=\"11\" height=\"8\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"8px\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"8\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("</tr>");
        strMenu.append("</table>");
//
        strMenu.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        strMenu.append("<tr>");
        strMenu.append("<td width=\"8px\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"8\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"1px\" bgcolor=\"#B7B7B7\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"1\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"100%\">");
        strMenu.append("<table width=\"100%\" border=\"0\" cellpadding=\"12\" cellspacing=\"0\" background=\""+getImgSrc("main_menu_bg.gif")+"\" bgcolor=\""+getBgColor()+"\" class=\"center-bg\">");
        strMenu.append("<tr>");
        strMenu.append("<td valign=\"top\" background=\""+getImgSrc("main_menu_tu.gif")+"\" class=\"noRepeatBG\">");
//		�������˵������Ӳ˵�
        strMenu.append(_MenuDiv);
//
        strMenu.append("<table onMouseOut=\"MM_showHideLayers('menuLayer',null,'hide')\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        strMenu.append("<tr>");
        strMenu.append("<td align=\"right\"  title='�����رղ˵�'>");
        strMenu.append("<img src=\""+getImgSrc("main_menu_closeBT.gif")+"\" width=\"17\" height=\"16\" onClick=\"MM_showHideLayers('menuLayer',null,'hide')\">");
        strMenu.append("</td>");
        strMenu.append("</tr>");
        strMenu.append("</table>");
        strMenu.append("</td>");
        strMenu.append("</tr>");
        strMenu.append("</table>");
        strMenu.append("</td>");
        strMenu.append("<td width=\"1px\" bgcolor=\"#B7B7B7\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"1\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"3\" valign=\"top\" bgcolor=\"#555555\">");
        strMenu.append("<table width=\"100%\" height=\"4\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#174570\">");
        strMenu.append("<tr>");
        strMenu.append("<td>");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"3\" height=\"4\">");
        strMenu.append("</td>");
        strMenu.append("</tr>");
        strMenu.append("</table>");
        strMenu.append("</td>");
        strMenu.append("<td width=\"5\" valign=\"top\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"5\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("</tr>");
        strMenu.append("</table>");
        strMenu.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        strMenu.append("<tr>");
        strMenu.append("<td width=\"8\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"8\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td bgcolor=\"#B7B7B7\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"1\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"3\" bgcolor=\"#555555\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"3\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"5\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"5\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("</tr>");
        strMenu.append("</table>");
        strMenu.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        strMenu.append("<tr>");
        strMenu.append("<td width=\"8\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"8\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"6\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"4\" height=\"4\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"100%\" bgcolor=\"#555555\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"1\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"3\" bgcolor=\"#555555\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"3\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("<td width=\"5\">");
        strMenu.append("<img src=\""+getImgSrc("spacer.gif")+"\" width=\"5\" height=\"1\">");
        strMenu.append("</td>");
        strMenu.append("</tr>");
        strMenu.append("</table>");
        strMenu.append("</div>");
        return strMenu.toString();
    }

    public String getImgPath() {
        String StyleColor = "blue";
        if(!"".equals(MainGlobal.getStyleColor())) {
            StyleColor = MainGlobal.getStyleColor();
        }
        String imgpath = "imgs/main/"+StyleColor;
        return imgpath;
    }

    public String getImgSrc(String imgName) {
        return getImgPath()+"/"+imgName;
    }

    public String getScriptImgPath() {
        return "var imgPath = \""+getImgPath()+"\";";
    }

    //�ж��Ƿ����Ż���Ȩ�ޣ�����з����Ż�ҳ��ĵ�ַ
    public String getPortalUrl(){
        String Url="";
        JDBCcon cn=new JDBCcon();
        ResultSetList rs=cn.getResultSetList("select distinct z.id,z.mingc,z.wenjwz\n" +
                "from ziyxxb z,zuqxb zq,renyzqxb r\n" +
                "where r.renyxxb_id ="+((Visit)this.getPage().getVisit()).getRenyID()+"\n" +
                "and r.zuxxb_id = zq.zuxxb_id\n" +
                "and zq.ziyxxb_id = z.id\n" +
                "and z.mingc='�Ż�'");
        if (rs.next()){
            Url= rs.getString("wenjwz");
//            System.out.println(Url);
        }
        cn.Close();
        return Url;
    }

    public String getHelp() {
        String help="";

//			"Ext.onReady(function(){\n" +
//			"\n" +
//			"    var button = Ext.get('help');\n" +
//			"\n" +
//			"    button.on('click', function(){\n" +
//			"\n" +
//			"        var win = new Ext.Window({\n" +
//			"\n" +
//			"            title: '����',\n" +
//			"            closable:true,\n" +
//			"            width:700,\n" +
//			"            height:350,\n" +
//			"            //plain:true,\n" +
//			"            //layout: 'border',\n" +
//			"            //html: 'adsfad'\n" +
//			"            html: '<iframe width=\"690\" height=\"310\" src=\"http://localhost:8086/zgdt/help/helpdoc.htm\"></iframe>'\n" +
//			"\n" +
//			"        });\n" +
//			"\n" +
//			"        win.show(this);\n" +
//			"    });\n" +
//			"});";
        // �����Ȩ�ޣ����д������Ż���js chh 2013-09-18
        String url=getPortalUrl();
        if (url.length()>6){ //������Ҫhttp:/��һ���򵥵��ж�
            help=help+"	var browser=navigator.appName;	\n" ;
            help=help+"	var b_version=navigator.appVersion; \n" ;
            help=help+"	var version=b_version.split(\";\"); \n" ;
            help=help+"	var trim_Version=version[1].replace(/[ ]/g,\"\"); \n" ;
            help=help+"	if(browser==\"Microsoft Internet Explorer\" && parseFloat(trim_Version.substring(4))>8){	\n" ;
            help=help+"	window.open('"+url+"');	\n";
            help=help+"	}else{	\n" ;
            help=help+"		try{ var objShell = new ActiveXObject(\"wscript.shell\");	\n";
            help=help+"		 var a= objShell.run(\"firefox.exe "+url+"\");} catch(err){}\n";
            help=help+"	}\n" ;
        }
        return help;
    }
    public void pageValidate(PageEvent arg0) {
        String PageName = arg0.getPage().getPageName();
        String ValPageName = Login.ValidateLogin(arg0.getPage());
        if (!PageName.equals(ValPageName)) {
            ValPageName = Login.ValidateAdmin(arg0.getPage());
            if (!PageName.equals(ValPageName)) {
                IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
                throw new PageRedirectException(ipage);
            }
        }
    }



    /**
     * @���ܣ� ��ȡid ���� url ��js ���� ����
     * @return
     */
    public String getIdToUrl(){
        Visit visit = (Visit)this.getPage().getVisit();
        long renyid = visit.getRenyID();
        JDBCcon con = new JDBCcon();
        StringBuffer sql = new StringBuffer();
        sql.append("  \n");
        sql.append(" select distinct z.fuid,z.id,z.mingc,z.wenjwz,z.xuh from ziyxxb z,zuqxb zq,renyzqxb r\n");
        sql.append(" where r.renyxxb_id = "+renyid+" and r.zuxxb_id = zq.zuxxb_id and zq.ziyxxb_id = z.id\n");
        sql.append(" order by z.xuh\n");
        ResultSetList rsl = con.getResultSetList(sql.toString());
        //�����˵�id�� url�Ķ����ֵ�Ե�js
        StringBuffer idToUrl = new StringBuffer();
        idToUrl.append("var idToUrl = new Object(); \n");
        while (rsl.next()){
            idToUrl.append("idToUrl['Node" + rsl.getLong("ID") + "'] = '" + rsl.getString("WENJWZ").replaceAll("#userid", visit.getRenyID()+"") + "'; \n");

        }
        return idToUrl.toString();
    }



    /**
     * @���ܣ� ��ȡ��ҳ���ؽ����url
     *
    into xitxxb
    (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)
    values
    (getnewid(122), 1, 122, '��ҳ����', 'www.baidu.com', '', '��ҳ', '1', '');
     * @return
     */
    public String getWelcomeUrl(){
        Visit visit = (Visit)this.getPage().getVisit();
        long renyid = visit.getRenyID();
        JDBCcon con = new JDBCcon();
        StringBuffer sql = new StringBuffer();
        sql.append("     \n");
        sql.append("   select xt.*  \n");
        sql.append("   from xitxxb xt  \n");
        sql.append("   where xt.diancxxb_id = " + visit.getDiancxxb_id() + "  \n");
        sql.append("   and xt.mingc = '��ҳ����'  \n");
        ResultSetList rsl = con.getResultSetList(sql.toString());
        StringBuffer  sqlToUrl = new StringBuffer();
        while (rsl.next()){
            sqlToUrl = new StringBuffer();
            sqlToUrl.append("   \n");
            sqlToUrl.append("    welcomeUrl = '"+ rsl.getString("ZHI") + "';   \n");
        }
        return sqlToUrl.toString();
    }



}
