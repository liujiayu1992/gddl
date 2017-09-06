package com.zhiren.dc.huaygl.huaybb.huayyb;
/*
 * 作者:tzf
 * 时间;2009-4-16
 * 内容:增加 电厂 树形 ，按此进行查询信息
 */
/*
 * 作者:夏峥
 * 时间:2009-05-12
 * 修改内容：修改查询语句qgrd*1000并设置无小数位，修正干基高位热的单位显示
 */
/*
* 作者：ww
* 时间：2009-12-26
* 描述：1、添加选择报表统计时间是按到货日期还是按发货日期，默认为到货日期,只在getMeizjyrb_zhilb() 方法中添加
* 	   配置参数：
* 		insert into xitxxb values(
			getnewid(257),
			1,
			257,                 --diancxxbID
			'计量报表统计日期',
			'fahrq',             --fahrq,daohrq
			'',
			'数量',
			1,
			'使用'
		)
* 		2、 修改热值（卡）为加权后计算
*/

/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

/*
 * 作者：卞昕
 * 时间：2010-11-8
 * 描述：将对化验月报报表增加运输单位一栏.(此功能针对电厂运输单位和煤矿分开用)
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;


import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Column;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Huayyb extends BasePage {

    public Date getMonthFirstday(Date dat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return cal.getTime();
    }

    boolean riqchange = false;

    private String riq;

    public String getRiq() {
        if (riq == null || riq.equals("")) {
            riq = DateUtil.FormatDate(new Date());
        }
        return riq;
    }

    public void setRiq(String riq) {

        if (this.riq != null && !this.riq.equals(riq)) {
            this.riq = riq;
            riqchange = true;
        }

    }

    boolean afterchange = false;

    private String after;

    public String getAfter() {
        if (after == null || after.equals("")) {
            after = DateUtil.FormatDate(new Date());
        }
        return after;
    }

    public void setAfter(String after) {

        if (this.after != null && !this.after.equals(after)) {
            this.after = after;
            afterchange = true;
        }

    }

    private String _msg;

    public void setMsg(String _value) {
        _msg = _value;
    }

    public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }

    public boolean getRaw() {
        return true;
    }

    private int _CurrentPage = -1;

    public int getCurrentPage() {
        return _CurrentPage;
    }

    public void setCurrentPage(int _value) {
        _CurrentPage = _value;
    }

    private int _AllPages = -1;

    public int getAllPages() {
        return _AllPages;
    }

    public void setAllPages(int _value) {
        _AllPages = _value;
    }

    public static final String REPORT_NAME_RUCMZJYYB = "Rucmzjyyb";// 入厂煤质检验报表

    public static final String REPORT_NAME_RUCMZJYYB_A = "Rucmzjyyb_A";// 入厂煤质检验报表

    public static final String REPORT_NAME_RUCMZJYYB_B = "Rucmzjyyb_B";// 入厂煤质检验报表

    public static final String REPORT_NAME_RUCMZJYYB_C = "Rucmzjyyb_C";// 入厂煤质检验报表+运输单位

    private String mstrReportName = "";

    private boolean blnIsBegin = false;

    public String getPrintTable() {
        setMsg(null);
        if (!blnIsBegin) {
            return "";
        }
        blnIsBegin = false;
        if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB)) {
            return getRucmzjyyb();
        } else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_C)) {
            return getRucmzjyyb_C();
        }else{
            return getRucmzjyyb();
        }

//		 return getRucmzjyyb_B();
//		 } else {
//		 return "无此报表";
//		 }
    }


    //	设置制表人默认当前用户
    private String getZhibr(){
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con=new JDBCcon();
        String zhibr="";
        String zhi="否";
        String sql="select zhi from xitxxb where mingc='月报管理制表人是否默认当前用户' and diancxxb_id="+visit.getDiancxxb_id();
        ResultSet rs=con.getResultSet(sql);
        try{
            while(rs.next()){
                zhi=rs.getString("zhi");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        if(zhi.equals("是")){
            zhibr=visit.getRenymc();
        }
        return zhibr;
    }

    //  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean hasDianc(String id){
        JDBCcon con=new JDBCcon();
        boolean mingc= false;
        String sql="select mingc from diancxxb where fuid = " + id;
        ResultSetList rsl=con.getResultSetList(sql);
        if(rsl.next()){
            mingc=true;
        }
        rsl.close();
        return mingc;
    }

    private String getTongjRq(JDBCcon con) {
        Visit visit = (Visit) getPage().getVisit();
        if (con == null){
            con = new JDBCcon();
        }
        String tongjrq=" select * from xitxxb where mingc='计量报表统计日期'  and zhuangt=1 and leib='数量' and diancxxb_id="+visit.getDiancxxb_id();
        ResultSetList rsl=con.getResultSetList(tongjrq);

        String strTongjrq="daohrq";

        if(rsl.next()){
            strTongjrq=rsl.getString("zhi");
        }

        rsl.close();
        con.Close();
        return strTongjrq;
    }

    /*
     * 将检质数量由jingz改为发货表laimsl字段 并对弹筒热值、发热量（MJ）、发热量（Kcal）进行修约
     * 修改时间：2008-12-04
     * 修改人：王磊
     */
    private String getRucmzjyyb() {
        Visit v = (Visit) getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();
        String strTongjRq = getTongjRq(con);
        String s="";

        if(!this.hasDianc(this.getTreeid_dc())){
            s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
        }
        String yunSql = "";
        long yunsfsId = getYunsfsValue().getId();
        if(yunsfsId == 0){     //判断是否为0，为0时，运输方式为全部，SQL语句中则没有对运输方式的判断
            yunSql = " \n";
        }else{                //如果不为0，运输方式为yunsfsID的值，SQL中加入条件判断语句
            yunSql = "and f.yunsfsb_id = "+yunsfsId +" \n";
        }
        // String Radix = getRadixValue().getValue();
        // int x = (int) getCumStyleValue().getId();

        // String cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
        // + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
        // + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
        // switch (x) {
        // case 1:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix + ")";
        // break;
        // case 2:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
        // + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
        // + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
        // break;
        // case 3:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix + ")";
        // break;
        // case 4:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.KOUD,0))," + Radix + ")";
        // break;
        // }
        StringBuffer buffer = new StringBuffer();

        buffer.append("select fhdw,\n"
                        + "       mkdw,\n"
                        + "       pz,\n"
                        + "       fz,\n"
                        + "       jingz,\n"
                        + "       ches,\n"
                        + "       mt,\n"
                        + "       mad,\n"
                        + "       aad,\n"
                        + "       ad,\n"
                        + "       aar,\n"
                        + "       vad,\n"
                        + "       vdaf,\n"
                        + "       qbad*1000 as qbad,\n"
                        + "       farl*1000 as farl,\n"
//						+ "       qbar,\n"   2009-12-26 ww 修改热值为加权后计算
                        + "       round_new(farl*1000/4.1816,0) as qbar,\n"
                        + "       sdaf,stad,\n"
                        + "       std,\n"
                        + "       star,\n"
                        + "       hdaf,\n"
                        + "       had,\n"

                        + "       fcad,\n"
                        + "       qgrd*1000 as qgrd\n"
                        + "  from (select decode(grouping(g.mingc), 1, '总计', g.mingc) as fhdw,\n"
                        + "               decode(grouping(g.mingc) + grouping(m.mingc),\n"
                        + "                      1,\n"
                        + "                      '合计',\n"
                        + "                      m.mingc) mkdw,\n"
                        + "               decode(grouping(g.mingc)+grouping(m.mingc)+grouping(p.mingc),1,'小计',p.mingc) pz,\n"
                        + "               c.mingc fz,\n"
                        + "               sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n"
                        + "               sum(f.biaoz) biaoz,\n"
                        + "               sum(f.yuns) yuns,\n"
                        + "               sum(f.yingk) yingk,\n"
                        + "               sum(f.zongkd) zongkd,\n"
                        + "               sum(f.ches) ches,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as mad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.ad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as ad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.aar * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aar,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.vad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) /\n"
                        + "                                          sum(round_new(f.laimsl,"+v.getShuldec()+"))\n"
                        + "                                           * 1000 / 4.1816,\n"
                        + "                                0)) as qbar,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.sdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as stad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.std * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as std,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum((round_new(z.stad*(100-z.mt)/(100-z.mad),2)) * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as star,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.hdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as hdaf,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as had,\n"

                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.fcad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as fcad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.qgrad*100/(100-z.mad) * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as qgrd\n"
                        //+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
                        //+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
                        + "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
                        + "         where f.gongysb_id = g.id\n"

                        +s


                        + "           and f.meikxxb_id = m.id\n"
                        + "           and f.pinzb_id = p.id\n"
                        + "           and f.faz_id = c.id\n"
                        + "           and f.zhilb_id = z.id\n"
                        + "           and f." + strTongjRq + " >= to_date('"
                        + getRiq()
                        + "', 'yyyy-mm-dd')\n"
                        + "           and f." + strTongjRq + " <= to_date('"
                        + getAfter()
                        + "', 'yyyy-mm-dd')\n"
                        + yunSql
                        + "\n"
                        + "         group by rollup(g.mingc, m.mingc,(p.mingc, c.mingc))\n"
                        + "         order by decode(grouping(g.mingc),1,0,999),max(g.xuh),g.mingc," +
                        "		decode(grouping(m.mingc),1,0,999),max(m.xuh),m.mingc," +
                        "	decode(grouping(p.mingc),1,0,999),p.mingc,decode(grouping(c.mingc),1,0,999))");
//		--------------------------- 新加的方法 ------------------------------------------------------
        ResultSetList rstmp = con.getResultSetList(buffer.toString());
        ResultSetList rs = null;
        String[][] ArrHeader=null;
        String[] strFormat=null;
        int[] ArrWidth=null;
        int aw=0;
        String [] Zidm=null;
        StringBuffer sb=new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='Rucmzjyyb' order by xuh");
        ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
            ArrWidth=new int[rsl.getRows()];
            strFormat=new String[rsl.getRows()];
            String biaot=rsl.getString(0,1);
            String [] Arrbt=biaot.split("!@");
            ArrHeader =new String [Arrbt.length][rsl.getRows()];
            Zidm=new String[rsl.getRows()];
            rs = new ResultSetList();
            while(rsl.next()){
                Zidm[rsl.getRow()]=rsl.getString("zidm");
                ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
                strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
                String[] title=rsl.getString("biaot").split("!@");
                for(int i=0;i<title.length;i++){
                    ArrHeader[i][rsl.getRow()]=title[i];
                }
            }
            rs.setColumnNames(Zidm);
            while(rstmp.next()){
                rs.getResultSetlist().add(rstmp.getArrString(Zidm));
            }
            rstmp.close();
            rsl.close();
            rsl=con.getResultSetList("select biaot from baobpzb where guanjz='Rucmzjyyb'");
            String Htitle="";
            while(rsl.next()){
                Htitle=rsl.getString("biaot");
            }
            aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
            rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制

            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        }else{
            rs = rstmp;
//        	-----------------------------  新加的方法 -------------------------------------------------------
            ArrHeader = new String[1][24];

            ArrHeader[0] = new String[] { "发货单位", "煤矿单位", "品种", "发站",
                    "检质数<br>量(吨)", "车数", "全水<br>分<br>(%)Mt",
                    "空气<br>干燥<br>基水<br>分<br>(%)Mad",
                    "空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
                    "收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
                    "干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
                    "收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
                    "收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
                    "干燥<br>无灰<br>基硫<br>(%)<br>Sdaf", "空气<br>干燥<br>基硫<br>(%)<br>Stad","干燥<br>基全<br>硫(%)<br>St,d","收到<br>基全<br>硫(%)<br>St,ar",
                    "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf","空气<br>干燥<br>基氢<br>(%)<br>Had",

                    "固定<br>碳<br>(%)<br>Fcad",
                    "干基<br>高位<br>热<br>(J/g)<br>Qgrd"};
//    		int[] 
            ArrWidth = new int[24];

            ArrWidth = new int[] { 90, 180, 50, 50, 40, 50, 40, 40, 40, 40, 40, 40,
                    40, 40, 40, 40, 40, 40, 40,40 ,40, 40, 40,40};
            aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
            rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
            rt.setTitle("煤  质  检  验  月  报", ArrWidth);

            strFormat = new String[] { "", "", "", "", "0.00", "", "0.0",
                    "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
                    "0.00", "0.00", "0.00","0.00","0.00" ,"0.00","0.00","" };


        }


        //System.out.println(buffer);

//		String[][] 
        rt.title.setRowHeight(2, 40);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 24);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "卸煤日期:" + getRiq() + "至" + getAfter(),
                Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        rt.setBody(new Table(rs, 1, 0, 3));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 24; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 3, "打印日期:"
                        + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(14, 4, "制表:"+getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // 设置页数
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"煤质检验月报","Rucmzjyyb");

        return rt.getAllPagesHtml();

    }

    private String getRucmzjyyb_C() {
        Visit v = (Visit) getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();
        String strTongjRq = getTongjRq(con);
        String s="";

        if(!this.hasDianc(this.getTreeid_dc())){
            s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
        }
        String yunSql = "";
        long yunsfsId = getYunsfsValue().getId();
        if(yunsfsId == 0){     //判断是否为0，为0时，运输方式为全部，SQL语句中则没有对运输方式的判断
            yunSql = " \n";
        }else{                //如果不为0，运输方式为yunsfsID的值，SQL中加入条件判断语句
            yunSql = "and f.yunsfsb_id = "+yunsfsId +" \n";
        }
        StringBuffer buffer = new StringBuffer();

        buffer
                .append("select fhdw,\n"
                        + "       mkdw,\n"
                        + "       ysdw,\n"
                        + "       pz,\n"
                        + "       fz,\n"
                        + "       jingz,\n"
                        + "       ches,\n"
                        + "       mt,\n"
                        + "       mad,\n"
                        + "       aad,\n"
                        + "       ad,\n"
                        + "       aar,\n"
                        + "       vad,\n"
                        + "       vdaf,\n"
                        + "       qbad*1000 as qbad,\n"
                        + "       farl*1000 as farl,\n"
                        + "       round_new(farl*1000/4.1816,0) as qbar,\n"
                        + "       sdaf,stad,\n"
                        + "       std,\n"
                        + "       star,\n"
                        + "       hdaf,\n"
                        + "       had,\n"

                        + "       fcad,\n"
                        + "       qgrd*1000 as qgrd\n"
                        + "  from (select decode(grouping(g.mingc), 1, '总计', g.mingc) as fhdw,\n"
                        + "               decode(grouping(g.mingc) + grouping(m.mingc),1,'合计',m.mingc) mkdw,\n"
                        + "               --decode(grouping(g.mingc) + grouping(m.mingc) + grouping(y.mingc),1,'小计',y.mingc) ysdw,\n"
                        + "               decode(grouping(g.mingc)+grouping(m.mingc)+grouping(p.mingc),1,'小计',p.mingc) pz,\n"
                        + "               c.mingc fz,\n"
                        + "               sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n"
                        + "               sum(f.biaoz) biaoz,\n"
                        + "               sum(f.yuns) yuns,\n"
                        + "               sum(f.yingk) yingk,\n"
                        + "               sum(f.zongkd) zongkd,\n"
                        + "               sum(f.ches) ches,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as mad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.ad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as ad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.aar * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aar,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.vad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) /\n"
                        + "                                          sum(round_new(f.laimsl,"+v.getShuldec()+"))\n"
                        + "                                           * 1000 / 4.1816,\n"
                        + "                                0)) as qbar,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.sdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as stad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.std * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as std,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum((round_new(z.stad*(100-z.mt)/(100-z.mad),2)) * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as star,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.hdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as hdaf,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as had,\n"

                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.fcad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as fcad,\n"
                        + "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
                        + "                      0,\n"
                        + "                      0,\n"
                        + "                      round_new(sum(z.qgrd * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as qgrd\n"
                        //+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
                        //+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
                        + "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z ,yunsdwb y, \n"
                        + "  		(select distinct c.fahb_id, c.yunsdwb_id from chepb c) cp \n"
                        + "         where f.gongysb_id = g.id\n"

                        +s


                        + "           and f.meikxxb_id = m.id  and cp.yunsdwb_id = y.id and cp.fahb_id = f.id\n"
                        + "           and f.pinzb_id = p.id\n"
                        + "           and f.faz_id = c.id\n"
                        + "           and f.zhilb_id = z.id\n"
                        + "           and f." + strTongjRq + " >= to_date('"
                        + getRiq()
                        + "', 'yyyy-mm-dd')\n"
                        + "           and f." + strTongjRq + " <= to_date('"
                        + getAfter()
                        + "', 'yyyy-mm-dd')\n"
                        + yunSql
                        + "\n"
                        + "         group by rollup(g.mingc, m.mingc,y.mingc,p.mingc, c.mingc)\n"
                        + " order by decode(grouping(g.mingc), 1, 0, 999),\n"
                        + "	max(g.xuh),\n"
                        + "	g.mingc,\n"
                        + "	decode(grouping(m.mingc), 1, 0, 999),\n"
                        + "	max(m.xuh),\n"
                        + "	m.mingc,\n"
                        + "	decode(grouping(y.mingc),1,0,999),\n"
                        + "	y.mingc,\n"
                        + "	decode(grouping(p.mingc), 1, 0, 999),\n"
                        + "	p.mingc,\n"
                        + "	decode(grouping(c.mingc), 1, 0, 999))");
//		--------------------------- 新加的方法 ------------------------------------------------------
        ResultSetList rstmp = con.getResultSetList(buffer.toString());
        ResultSetList rs = null;
        String[][] ArrHeader=null;
        String[] strFormat=null;
        int[] ArrWidth=null;
        int aw=0;
        String [] Zidm=null;
        StringBuffer sb=new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='Rucmzjyyb' order by xuh");
        ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
            ArrWidth=new int[rsl.getRows()];
            strFormat=new String[rsl.getRows()];
            String biaot=rsl.getString(0,1);
            String [] Arrbt=biaot.split("!@");
            ArrHeader =new String [Arrbt.length][rsl.getRows()];
            Zidm=new String[rsl.getRows()];
            rs = new ResultSetList();
            while(rsl.next()){
                Zidm[rsl.getRow()]=rsl.getString("zidm");
                ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
                strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
                String[] title=rsl.getString("biaot").split("!@");
                for(int i=0;i<title.length;i++){
                    ArrHeader[i][rsl.getRow()]=title[i];
                }
            }
            rs.setColumnNames(Zidm);
            while(rstmp.next()){
                rs.getResultSetlist().add(rstmp.getArrString(Zidm));
            }
            rstmp.close();
            rsl.close();
            rsl=con.getResultSetList("select biaot from baobpzb where guanjz='Rucmzjyyb'");
            String Htitle="";
            while(rsl.next()){
                Htitle=rsl.getString("biaot");
            }
            aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
            rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制

            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        }else{
            rs = rstmp;
//        	-----------------------------  新加的方法 -------------------------------------------------------
            ArrHeader = new String[1][25];

            ArrHeader[0] = new String[] { "发货单位", "煤矿单位",/*"运输单位",*/ "品种", "发站",
                    "检质数<br>量(吨)", "车数", "全水<br>分<br>(%)Mt",
                    "空气<br>干燥<br>基水<br>分<br>(%)Mad",
                    "空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
                    "收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
                    "干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
                    "收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
                    "收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
                    "干燥<br>无灰<br>基硫<br>(%)<br>Sdaf", "空气<br>干燥<br>基硫<br>(%)<br>Stad","干燥<br>基全<br>硫(%)<br>St,d","收到<br>基全<br>硫(%)<br>St,ar",
                    "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf","空气<br>干燥<br>基氢<br>(%)<br>Had",

                    "固定<br>碳<br>(%)<br>Fcad",
                    "干基<br>高位<br>热<br>(J/g)<br>Qgrd"};
//    		int[] 
            ArrWidth = new int[25];

            ArrWidth = new int[] { 90, 180,/*80 ,*/50, 50, 40, 50, 40, 40, 40, 40, 40, 40,
                    40, 40, 40, 40, 40, 40, 40,40 ,40, 40, 40,40};
            aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
            rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
            rt.setTitle("煤  质  检  验  月  报", ArrWidth);

            strFormat = new String[] { "", "", /*"",*/"", "", "0.00", "", "0.0",
                    "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
                    "0.00", "0.00", "0.00","0.00","0.00" ,"0.00","0.00","" };


        }


        //System.out.println(buffer);

//		String[][] 
        rt.title.setRowHeight(2, 40);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "卸煤日期:" + getRiq() + "至" + getAfter(),
                Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        rt.setBody(new Table(rs, 1, 0, 3));
        rt.body.ShowZero=true;
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 25; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 3, "打印日期:"
                        + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(14, 4, "制表:"+getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // 设置页数
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"煤质检验月报","Rucmzjyyb");

        return rt.getAllPagesHtml();

    }

    // 部门A入厂煤质检验报表
    private String getRucmzjyyb_A() {
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s="";

        if(!this.hasDianc(this.getTreeid_dc())){
            s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
        }


        // String Radix = getRadixValue().getValue();
        // int x = (int) getCumStyleValue().getId();
        // String cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
        // + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
        // + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
        // switch (x) {
        // case 1:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix + ")";
        // break;
        // case 2:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
        // + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
        // + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
        // break;
        // case 3:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix + ")";
        // break;
        // case 4:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.KOUD,0))," + Radix + ")";
        // break;
        // }

        StringBuffer buffer = new StringBuffer();

        buffer.append("SELECT MEIKDQMC,\n");
        buffer.append("       MEIKDWMC,\n");
        buffer.append("       FAZ,\n");
        buffer.append("       PINZ,\n");
        buffer.append("       CHES,\n");
        buffer.append("       JINGZ,\n");
        buffer.append("       MT,\n");
        buffer.append("       MAD,\n");
        buffer.append("       AAD,\n");
        buffer.append("       AD,\n");
        buffer.append("       AAR,\n");
        buffer.append("       VAD,\n");
        buffer.append("       VDAF,\n");
        buffer.append("       QBAD,\n");
        buffer.append("       QBAR,\n");
        buffer.append("       FARL,\n");
        buffer.append("       SDAF,\n");
        buffer.append("       STD,\n");
        buffer.append("       HDAF\n");
        buffer.append("  FROM (SELECT A,\n");
        buffer.append("               B,\n");
        buffer.append("               C,\n");
        buffer.append("               D,\n");
        buffer.append("               MEIKDQMC,\n");
        buffer.append("               MEIKDWMC,\n");
        buffer.append("               FAZ,\n");
        buffer.append("               DECODE(MEIKDWMC,\n");
        buffer.append("                      NULL,\n");
        buffer
                .append("                      DECODE(MEIKDQMC, NULL, '总计', '合计'),\n");
        buffer.append("                      NVL(PINZ, '小计')) AS PINZ,\n");
        buffer.append("               JINGZ,\n");
        buffer.append("               CHES,\n");
        buffer.append("               MT,\n");
        buffer.append("               MAD,\n");
        buffer.append("               AAD,\n");
        buffer.append("               AD,\n");
        buffer.append("               AAR,\n");
        buffer.append("               VAD,\n");
        buffer.append("               VDAF,\n");
        buffer.append("               QBAD * 1000 AS QBAD,\n");
        buffer.append("               QBAR * 1000 AS QBAR,\n");
        buffer
                .append("               ROUND_NEW(QBAR * 1000 / 4.1816, 0) AS FARL,\n");
        buffer.append("               SDAF,\n");
        buffer.append("               STD,\n");
        buffer.append("               HDAF\n");
        buffer.append("          FROM (SELECT GROUPING(F.MEIKDQMC) AS A,\n");
        buffer.append("                       GROUPING(F.MEIKDWMC) AS B,\n");
        buffer.append("                       GROUPING(F.JIANC) AS C,\n");
        buffer.append("                       GROUPING(F.PINZ) AS D,\n");
        buffer.append("                       MAX(F.DQXUH) AS DQXH,\n");
        buffer.append("                       MAX(F.KBXUH) AS KBXH,\n");
        buffer.append("                       F.MEIKDQMC,\n");
        buffer.append("                       F.MEIKDWMC,\n");
        buffer.append("                       F.JIANC AS FAZ,\n");
        buffer.append("                       F.PINZ,\n");
        buffer.append("                       SUM(S.JINGZ) AS JINGZ,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.QUANSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.QUANSF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.QUANSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 1) AS MT,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.KONGQGZJSF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS MAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.KONGQGZJHF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS AAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.GANZJHF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS AD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.SHOUDJHF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS AAR,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.KONGQGZJHFF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS VAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.GANZWHJHFF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS VDAF,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.DANTRL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.DANTRL, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.DANTRL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS QBAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.FARL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.FARL, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.FARL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS QBAR,\n");
        buffer
                .append("                       ROUND_NEW(ROUND_NEW(SUM(Z.GANZJL * S.JINGZ) /\n");
        buffer
                .append("                                           SUM(S.JINGZ),\n");
        buffer
                .append("                                           2) * 100 /\n");
        buffer
                .append("                                 (100 - ROUND_NEW(SUM(Z.GANZJHF * S.JINGZ) /\n");
        buffer
                .append("                                                  SUM(S.JINGZ),\n");
        buffer
                .append("                                                  2)),\n");
        buffer.append("                                 2) AS SDAF,\n");
        buffer.append("\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.GANZJL, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS STD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.HDAF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.HDAF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.HDAF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS HDAF,\n");
        buffer.append("                       SUM(S.CHES) AS CHES\n");
        buffer.append("                  FROM (SELECT F.ID,\n");
        buffer.append("                               F.HEDBZ,\n");
        buffer.append("                               DQ.XUH AS DQXUH,\n");
        buffer.append("                               KB.XUH AS KBXUH,\n");
        buffer.append("                               DQ.MEIKDQMC,\n");
        buffer.append("                               KB.MEIKDWMC,\n");
        buffer.append("                               CZ.JIANC,\n");
        buffer.append("                               PZ.PINZ\n");
        buffer.append("                          FROM FAHB    F,\n");
        buffer.append("                               MEIKDQB DQ,\n");
        buffer.append("                               MEIKXXB KB,\n");
        buffer.append("                               CHEZXXB CZ,\n");
        buffer.append("                               RANLPZB PZ\n");
        buffer.append("                         WHERE F.MEIKXXB_ID = KB.ID\n");

        buffer.append(s);


        buffer.append("                           AND DQ.ID = KB.MEIKDQB_ID\n");
        buffer.append("                           AND F.FAZ_ID = CZ.ID\n");
        buffer.append("                           AND F.RANLPZB_ID = PZ.ID\n");
        buffer.append("                           AND KB.TONGJBZ = 1) F,\n");
        buffer.append("                       VWZHILBTMP Z,\n");
        buffer.append("                       (SELECT C.FAHB_ID,\n");
        buffer
                .append("                               Z.ZHILB_ID AS ZHILBID,\n");
        // buffer.append(" " + cumStyle
        // + " AS JINGZ,\n");
        buffer.append("                               COUNT(C.ID) AS CHES\n");
        buffer
                .append("                          FROM CHEPB C, FAHB F, VWZHILBTMP Z\n");
        buffer.append("                         WHERE C.FAHB_ID = F.ID(+)\n");


        buffer.append(s);


        // 实现报表的兼容性
        buffer
                .append("                           AND F.ZHILB_ID = Z.ZHILB_ID\n");
        buffer.append("                           AND Z.BUM = 'A'\n");
        buffer.append("                           AND C.GUOHSJ >=\n");
        // buffer.append(" TO_DATE('"
        // + DateUtil.FormatDate(getRiq()) + "' || ' ' ||\n");
        buffer
                .append("                                       NVL((SELECT XITXXB.ZHI\n");
        buffer
                .append("                                             FROM XITXXB\n");
        buffer
                .append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
        buffer.append("                                           '0'),\n");
        buffer
                .append("                                       'YYYY-MM-DD HH24')\n");
        buffer.append("                           AND C.GUOHSJ <\n");
        // buffer.append(" TO_DATE('"
        // + DateUtil.FormatDate(custom.addDays(getAfter(), 1))
        // + "' || ' ' ||\n");
        buffer
                .append("                                       NVL((SELECT XITXXB.ZHI\n");
        buffer
                .append("                                             FROM XITXXB\n");
        buffer
                .append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
        buffer.append("                                           '0'),\n");
        buffer
                .append("                                       'YYYY-MM-DD HH24')\n");
        buffer
                .append("                         GROUP BY C.FAHB_ID, Z.ZHILB_ID) S\n");
        buffer.append("                 WHERE F.ID = S.FAHB_ID\n");

        buffer.append(s);


        buffer.append("                   AND Z.ZHILB_ID = S.ZHILBID\n");
        buffer.append("                   AND Z.SHANGJSHZT = 1\n");
        buffer.append("                   AND Z.BUM = 'A'\n");
        buffer.append("                   AND F.HEDBZ = 1\n");
        buffer
                .append("                 GROUP BY ROLLUP(F.MEIKDQMC, F.MEIKDWMC, F.JIANC, F.PINZ)\n");
        buffer
                .append("                HAVING GROUPING(F.JIANC) + GROUPING(F.PINZ) != 1) T)\n");
        buffer.append(" ORDER BY A DESC,\n");
        buffer.append("          MEIKDQMC DESC,\n");
        buffer.append("          B DESC,\n");
        buffer.append("          MEIKDWMC DESC,\n");
        buffer.append("          C DESC,\n");
        buffer.append("          FAZ,\n");
        buffer.append("          D DESC,\n");
        buffer.append("          PINZ DESC");

        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String[][] ArrHeader = new String[1][19];

        ArrHeader[0] = new String[] { "煤矿地区", "煤矿单位", "发站", "品种", "车数",
                "检质数<br>量(吨)", "全水<br>分<br>(%)Mt",
                "空气<br>干燥<br>基水<br>分<br>(%)Mad",
                "空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
                "收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
                "干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
                "收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
                "收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
                "干燥<br>无灰<br>基硫<br>(%)<br>Sdaf", "干燥<br>基全<br>硫(%)<br>St,d",
                "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf" };
        int[] ArrWidth = new int[19];

        ArrWidth = new int[] { 90, 180, 50, 50, 40, 50, 40, 40, 40, 40, 40, 40,
                40, 40, 40, 40, 40, 40, 40 };

        int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
        rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
        rt.setTitle("煤  质  检  验  月  报", ArrWidth);
        rt.title.setRowHeight(2, 40);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "到厂日期:"
                        + DateUtil.FormatDate(new Date(getRiq())) + "至"
                        + DateUtil.FormatDate(new Date(getAfter())),
                Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "", "", "0.00", "", "0.0",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
                "0.00", "0.00", "0.00" };

        rt.setBody(new Table(rs, 1, 0, 3));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 19; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 3, "打印日期:"
                        +  DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(14, 4, "制表:"+getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // 设置页数
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(21);

        return rt.getAllPagesHtml();

    }

    // 部门B入厂煤质检验报表
    private String getRucmzjyyb_B() {
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s="";

        if(!this.hasDianc(this.getTreeid_dc())){
            s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
        }


        // String Radix = getRadixValue().getValue();
        // int x = (int) getCumStyleValue().getId();
        // String cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
        // + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
        // + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
        // switch (x) {
        // case 1:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix + ")";
        // break;
        // case 2:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
        // + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
        // + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
        // break;
        // case 3:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix + ")";
        // break;
        // case 4:
        // cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix
        // + ")-ROUND_NEW(SUM(NVL(C.KOUD,0))," + Radix + ")";
        // break;
        // }
        StringBuffer buffer = new StringBuffer();

        buffer.append("SELECT MEIKDQMC,\n");
        buffer.append("       MEIKDWMC,\n");
        buffer.append("       FAZ,\n");
        buffer.append("       PINZ,\n");
        buffer.append("       CHES,\n");
        buffer.append("       JINGZ,\n");
        buffer.append("       MT,\n");
        buffer.append("       MAD,\n");
        buffer.append("       AAD,\n");
        buffer.append("       AD,\n");
        buffer.append("       AAR,\n");
        buffer.append("       VAD,\n");
        buffer.append("       VDAF,\n");
        buffer.append("       QBAD,\n");
        buffer.append("       QBAR,\n");
        buffer.append("       FARL,\n");
        buffer.append("       SDAF,\n");
        buffer.append("       STD,\n");
        buffer.append("       HDAF\n");
        buffer.append("  FROM (SELECT A,\n");
        buffer.append("               B,\n");
        buffer.append("               C,\n");
        buffer.append("               D,\n");
        buffer.append("               MEIKDQMC,\n");
        buffer.append("               MEIKDWMC,\n");
        buffer.append("               FAZ,\n");
        buffer.append("               DECODE(MEIKDWMC,\n");
        buffer.append("                      NULL,\n");
        buffer
                .append("                      DECODE(MEIKDQMC, NULL, '总计', '合计'),\n");
        buffer.append("                      NVL(PINZ, '小计')) AS PINZ,\n");
        buffer.append("               JINGZ,\n");
        buffer.append("               CHES,\n");
        buffer.append("               MT,\n");
        buffer.append("               MAD,\n");
        buffer.append("               AAD,\n");
        buffer.append("               AD,\n");
        buffer.append("               AAR,\n");
        buffer.append("               VAD,\n");
        buffer.append("               VDAF,\n");
        buffer.append("               QBAD * 1000 AS QBAD,\n");
        buffer.append("               QBAR * 1000 AS QBAR,\n");
        buffer
                .append("               ROUND_NEW(QBAR * 1000 / 4.1816, 0) AS FARL,\n");
        buffer.append("               SDAF,\n");
        buffer.append("               STD,\n");
        buffer.append("               HDAF\n");
        buffer.append("          FROM (SELECT GROUPING(F.MEIKDQMC) AS A,\n");
        buffer.append("                       GROUPING(F.MEIKDWMC) AS B,\n");
        buffer.append("                       GROUPING(F.JIANC) AS C,\n");
        buffer.append("                       GROUPING(F.PINZ) AS D,\n");
        buffer.append("                       MAX(F.DQXUH) AS DQXH,\n");
        buffer.append("                       MAX(F.KBXUH) AS KBXH,\n");
        buffer.append("                       F.MEIKDQMC,\n");
        buffer.append("                       F.MEIKDWMC,\n");
        buffer.append("                       F.JIANC AS FAZ,\n");
        buffer.append("                       F.PINZ,\n");
        buffer.append("                       SUM(S.JINGZ) AS JINGZ,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.QUANSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.QUANSF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.QUANSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 1) AS MT,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.KONGQGZJSF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS MAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.KONGQGZJHF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS AAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.GANZJHF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS AD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.SHOUDJHF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS AAR,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.KONGQGZJHFF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS VAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.GANZWHJHFF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS VDAF,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.DANTRL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.DANTRL, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.DANTRL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS QBAD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.FARL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.FARL, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.FARL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS QBAR,\n");
        buffer
                .append("                       ROUND_NEW(ROUND_NEW(SUM(Z.GANZJL * S.JINGZ) /\n");
        buffer
                .append("                                           SUM(S.JINGZ),\n");
        buffer
                .append("                                           2) * 100 /\n");
        buffer
                .append("                                 (100 - ROUND_NEW(SUM(Z.GANZJHF * S.JINGZ) /\n");
        buffer
                .append("                                                  SUM(S.JINGZ),\n");
        buffer
                .append("                                                  2)),\n");
        buffer.append("                                 2) AS SDAF,\n");
        buffer.append("\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.GANZJL, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS STD,\n");
        buffer
                .append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.HDAF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ)),\n");
        buffer.append("                                        0,\n");
        buffer.append("                                        0,\n");
        buffer
                .append("                                        SUM(NVL(Z.HDAF, 0) * S.JINGZ) /\n");
        buffer
                .append("                                        SUM(DECODE(NVL(Z.HDAF, 0),\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   0,\n");
        buffer
                .append("                                                   S.JINGZ))),\n");
        buffer.append("                                 2) AS HDAF,\n");
        buffer.append("                       SUM(S.CHES) AS CHES\n");
        buffer.append("                  FROM (SELECT F.ID,\n");
        buffer.append("                               F.HEDBZ,\n");
        buffer.append("                               DQ.XUH AS DQXUH,\n");
        buffer.append("                               KB.XUH AS KBXUH,\n");
        buffer.append("                               DQ.MEIKDQMC,\n");
        buffer.append("                               KB.MEIKDWMC,\n");
        buffer.append("                               CZ.JIANC,\n");
        buffer.append("                               PZ.PINZ\n");
        buffer.append("                          FROM FAHB    F,\n");
        buffer.append("                               MEIKDQB DQ,\n");
        buffer.append("                               MEIKXXB KB,\n");
        buffer.append("                               CHEZXXB CZ,\n");
        buffer.append("                               RANLPZB PZ\n");
        buffer.append("                         WHERE F.MEIKXXB_ID = KB.ID\n");

        buffer.append(s);


        buffer.append("                           AND DQ.ID = KB.MEIKDQB_ID\n");
        buffer.append("                           AND F.FAZ_ID = CZ.ID\n");
        buffer.append("                           AND F.RANLPZB_ID = PZ.ID\n");
        buffer.append("                           AND KB.TONGJBZ = 1) F,\n");
        buffer.append("                       VWZHILBTMP Z,\n");
        buffer.append("                       (SELECT C.FAHB_ID,\n");
        buffer
                .append("                               Z.ZHILB_ID AS ZHILBID,\n");
        // buffer.append(" " + cumStyle
        // + " AS JINGZ,\n");
        buffer.append("                               COUNT(C.ID) AS CHES\n");
        buffer
                .append("                          FROM CHEPB C, FAHB F, VWZHILBTMP Z\n");
        buffer.append("                         WHERE C.FAHB_ID = F.ID(+)\n");

        buffer.append(s);


        // 实现报表的兼容性
        buffer
                .append("                           AND F.ZHILB_ID = Z.ZHILB_ID\n");
        buffer.append("                           AND Z.BUM = 'A'\n");
        buffer.append("                           AND C.GUOHSJ >=\n");
        // buffer.append(" TO_DATE('"
        // + DateUtil.FormatDate(getRiq()) + "' || ' ' ||\n");
        buffer
                .append("                                       NVL((SELECT XITXXB.ZHI\n");
        buffer
                .append("                                             FROM XITXXB\n");
        buffer
                .append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
        buffer.append("                                           '0'),\n");
        buffer
                .append("                                       'YYYY-MM-DD HH24')\n");
        buffer.append("                           AND C.GUOHSJ <\n");
        // buffer.append(" TO_DATE('"
        // + DateUtil.FormatDate(custom.addDays(getAfter(), 1))
        // + "' || ' ' ||\n");
        buffer
                .append("                                       NVL((SELECT XITXXB.ZHI\n");
        buffer
                .append("                                             FROM XITXXB\n");
        buffer
                .append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
        buffer.append("                                           '0'),\n");
        buffer
                .append("                                       'YYYY-MM-DD HH24')\n");
        buffer
                .append("                         GROUP BY C.FAHB_ID, Z.ZHILB_ID) S\n");
        buffer.append("                 WHERE F.ID = S.FAHB_ID\n");

        buffer.append(s);


        buffer.append("                   AND Z.ZHILB_ID = S.ZHILBID\n");
        buffer.append("                   AND Z.SHANGJSHZT = 1\n");
        buffer.append("                   AND Z.BUM = 'B'\n");
        buffer.append("                   AND F.HEDBZ = 1\n");
        buffer
                .append("                 GROUP BY ROLLUP(F.MEIKDQMC, F.MEIKDWMC, F.JIANC, F.PINZ)\n");
        buffer
                .append("                HAVING GROUPING(F.JIANC) + GROUPING(F.PINZ) != 1) T)\n");
        buffer.append(" ORDER BY A DESC,\n");
        buffer.append("          MEIKDQMC DESC,\n");
        buffer.append("          B DESC,\n");
        buffer.append("          MEIKDWMC DESC,\n");
        buffer.append("          C DESC,\n");
        buffer.append("          FAZ,\n");
        buffer.append("          D DESC,\n");
        buffer.append("          PINZ DESC");

        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String[][] ArrHeader = new String[1][19];

        ArrHeader[0] = new String[] { "煤矿地区", "煤矿单位", "发站", "品种", "车数",
                "检质数<br>量(吨)", "全水<br>分<br>(%)Mt",
                "空气<br>干燥<br>基水<br>分<br>(%)Mad",
                "空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
                "收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
                "干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
                "收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
                "收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
                "干燥<br>无灰<br>基硫<br>(%)<br>Sdaf", "干燥<br>基全<br>硫(%)<br>St,d",
                "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf" };
        int[] ArrWidth = new int[19];

        ArrWidth = new int[] { 90, 180, 50, 50, 40, 50, 40, 40, 40, 40, 40, 40,
                40, 40, 40, 40, 40, 40, 40 };

        int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
        rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
        rt.setTitle("煤  质  检  验  月  报", ArrWidth);
        rt.title.setRowHeight(2, 40);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "到厂日期:"
                        +  DateUtil.FormatDate(new Date(getRiq())) + "至"
                        + DateUtil.FormatDate(new Date(getAfter())),
                Table.ALIGN_LEFT);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "", "", "0.00", "", "0.0",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
                "0.00", "0.00", "0.00" };

        rt.setBody(new Table(rs, 1, 0, 3));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 19; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 3, "打印日期:"
                        + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(14, 4, "制表:"+getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // 设置页数
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(21);

        return rt.getAllPagesHtml();

    }

    private boolean _QueryClick = false;

    public void QueryButton(IRequestCycle cycle) {
        _QueryClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_QueryClick) {
            _QueryClick = false;
        }
    }


    //	-------------------------电厂Tree-----------------------------------------------------------------
    public IPropertySelectionModel getDiancmcModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getDiancmcModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void setDiancmcModel(IPropertySelectionModel _value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(_value);
    }

    public void getDiancmcModels() {
        Visit visit=(Visit)this.getPage().getVisit();
        String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
        sql+=" union \n";
        sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
        setDiancmcModel(new IDropDownModel(sql));
    }

    public String getTreeid_dc() {
        String treeid = ((Visit) getPage().getVisit()).getString3();
        if (treeid == null || treeid.equals("")) {
            ((Visit) getPage().getVisit()).setString3(String
                    .valueOf(((Visit) this.getPage().getVisit())
                            .getDiancxxb_id()));
        }
        return ((Visit) getPage().getVisit()).getString3();
    }

    public void setTreeid_dc(String treeid) {
        ((Visit) getPage().getVisit()).setString3(treeid);
    }
    DefaultTree dc ;

    public DefaultTree getTree_dc() {
        return dc;
    }

    public void setTree_dc(DefaultTree etu) {
        dc= etu;
    }

    public String getTreeScript1() {
        return getTree_dc().getScript();
    }

//	-------------------------电厂Tree END----------

    public String getTreeScript() {
        return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
    }



    public void getSelectData() {
        Toolbar tb1 = new Toolbar("tbdiv");
        Visit visit = (Visit) this.getPage().getVisit();

        DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
                "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
        setTree_dc(dt1);
        TextField tf1 = new TextField();
        tf1.setId("diancTree_text");
        tf1.setWidth(100);
        tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
                .parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
                        : getTreeid_dc())));

        ToolbarButton tb3 = new ToolbarButton(null, null,
                "function(){diancTree_window.show();}");
        tb3.setIcon("ext/resources/images/list-items.gif");
        tb3.setCls("x-btn-icon");
        tb3.setMinWidth(20);

        tb1.addText(new ToolbarText("电厂:"));
        tb1.addField(tf1);
        tb1.addItem(tb3);

        tb1.addText(new ToolbarText("-"));


        tb1.addText(new ToolbarText("到货日期:"));
        DateField df = new DateField();
        df.setReadOnly(true);
        df.setValue(this.getRiq());
        df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
        df.setId("riq");
        tb1.addField(df);
        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("至:"));
        DateField df1 = new DateField();
        df1.setReadOnly(true);
        df1.setValue(this.getAfter());
        df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
        df1.setId("after");
        tb1.addField(df1);
        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("运输方式:"));
        ComboBox meik = new ComboBox();
        meik.setTransform("YUNSFSSelect");
        meik.setEditable(true);
        meik.setWidth(100);
        //meik.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(meik);

        ToolbarButton tb = new ToolbarButton(null, "刷新",
                "function(){document.Form0.submit();}");
        tb1.addItem(tb);

        setToolbar(tb1);

    }

    public Toolbar getToolbar() {
        return ((Visit) this.getPage().getVisit()).getToolbar();
    }

    public void setToolbar(Toolbar tb1) {
        ((Visit) this.getPage().getVisit()).setToolbar(tb1);
    }

    public String getToolbarScript() {
        return getToolbar().getRenderScript();
    }

    public String getcontext() {
        return "";
        // return "var context='http://"
        // + this.getRequestCycle().getRequestContext().getServerName()
        // + ":"
        // + this.getRequestCycle().getRequestContext().getServerPort()
        // + ((Visit) getPage().getVisit()).get() + "';";
    }

    public SortMode getSort() {
        return SortMode.USER;
    }

    private String _pageLink;

    public String getpageLink() {
        if (!_pageLink.equals("")) {
            return _pageLink;
        } else {
            return "";
        }
    }

    // Page方法
    protected void initialize() {
        _pageLink = "";
    }

    // 运输方式下拉框
    private boolean falg1 = false;

    private IDropDownBean YunsfsValue;

    public IDropDownBean getYunsfsValue() {
        if (YunsfsValue == null) {
            YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
        }
        return YunsfsValue;
    }

    public void setYunsfsValue(IDropDownBean Value) {
        if (!(YunsfsValue == Value)) {
            YunsfsValue = Value;
            falg1 = true;
        }
    }

    private IPropertySelectionModel YunsfsModel;

    public void setYunsfsModel(IPropertySelectionModel value) {
        YunsfsModel = value;
    }

    public IPropertySelectionModel getYunsfsModel() {
        if (YunsfsModel == null) {
            getYunsfsModels();
        }
        return YunsfsModel;
    }

    public IPropertySelectionModel getYunsfsModels() {
        String sql = "select 0 id,'全部' mingc from dual union select id,mingc from yunsfsb";//连接一条假设行，如果ID为0时，运输方式为全部.
        YunsfsModel = new IDropDownModel(sql);
        return YunsfsModel;
    }


    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();

        if (!visit.getActivePageName().equals(this.getPageName().toString())) {
            visit.setActivePageName(getPageName().toString());
            visit.setString1("");

            //begin方法里进行初始化设置
            visit.setString4(null);
            visit.setString3(null);
            String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
            if (pagewith != null) {

                visit.setString4(pagewith);
            }
            //	visit.setString4(null);保存传递的非默认纸张的样式
            getSelectData();
        }
        if (cycle.getRequestContext().getParameters("lx") != null) {
            if (!visit.getString1().equals(
                    cycle.getRequestContext().getParameters("lx")[0])) {

                visit.setProSelectionModel10(null);
                visit.setDropDownBean10(null);
                visit.setProSelectionModel1(null);
                visit.setDropDownBean1(null);
                visit.setProSelectionModel5(null);
                visit.setDropDownBean5(null);
                visit.setString3(null);
                setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
            }
            visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
            mstrReportName = visit.getString1();
        } else {
            visit.setString3(null);
            if (visit.getString1().equals("")) {
                mstrReportName = visit.getString1();
            }
        }
        blnIsBegin = true;
        // mstrReportName="diaor04bb";
        getSelectData();

    }

}
