package com.zhiren.jt.jiesgl.changfhs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


/**
 * @author ly
 *         2009-09-07
 *         单批次
 */

/*
 * 修改人：    王伟
 * 修改时间： 2009-10-31
 * 修改内容： 添加判断不同集团报表显示的不同的表头名称，而不是固定的中国大唐
 */
/*
 * 作者:tzf
 * 时间:2010-03-09
 * 修改内容:增加 发货日期  到货日期    批次号
 */
/*
 * 作者:tzf
 * 时间:2010-03-11
 * 修改内容:发货日期  到货日期    批次号 调整成 区间
 */
/*
 * 作者:lil
 * 时间:2014-05-20
 * 修改内容:修改青铝电厂单批次明细报表表头内容，改为“燃料结算单批次明细单”
 */
public class Danpcmxd extends BasePage implements PageValidateListener {

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

    private String getDate() {
        return DateUtil.Formatdate("yyyy-MM-dd", new Date());
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

    public void submit(IRequestCycle cycle) {

    }

    //******************页面初始设置********************//
    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

        Visit visit = (Visit) getPage().getVisit();

        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            visit.setString1("");    //记录上一页面传过来的表名，编号

            if (cycle.getRequestContext().getParameters("lx") != null) {

                visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
            }
        }
//		第二次跳转页面用
        if (cycle.getRequestContext().getParameters("lx") != null) {

            visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
        }

        this.getSelectData();
        isBegin = true;
    }

    public String getPrintTable() {
        if (!isBegin) {
            return "";
        }
        isBegin = false;

        return getSelectData();

    }

    private boolean isBegin = false;

    private String getSelectData_no_pc(JDBCcon cn) {//没有批次

        _CurrentPage = 1;
        _AllPages = 1;
        Visit visit = (Visit) this.getPage().getVisit();
//		JDBCcon cn = new JDBCcon();
        Report rt = new Report();

        String hetdw = "";
        String jiesrq = "";
        String het = "";
        String bianh = "";

        String ches = "";
        String jiesml = "";
        String danj = "";
        String meik = "";
        String shif = "0.00";
        String yunf = "";
        String qityzf = "0";
        String chaod = "";
        String jufje = "";
        String yingfzje = "";

        String fahrq = "";

        String table = "";
        String table_mk = "";
        String table_yf = "";
        String jiesbh = "";
        String tiaoj = "";
        String sql_jsb = "";
        table = visit.getString1().substring(0, visit.getString1().indexOf(";"));
        table_mk = table.substring(0, table.indexOf(","));
        table_yf = table.substring(table.indexOf(",") + 1);

        jiesbh = visit.getString1().substring(visit.getString1().indexOf(";") + 1);

        if (table_mk.equals("jiesb")) {
            tiaoj = "diancjsmkb_id";
        } else if (table_mk.equals("diancjsmkb")) {
            tiaoj = "diancjsmkb_id";
        } else if (table_mk.equals("kuangfjsmkb")) {
            tiaoj = "kuangfjsmkb_id";
        }

        boolean Flag = false;
//		如果为true，则为煤款结算
        Flag = isMeikjsd(table_mk, table_yf, jiesbh);

        if (Flag) {
//			煤款结算
            sql_jsb =
                    "select m.quanc as hetdw,\n" +
                            "           to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq,\n" +
                            "           h.hetbh as het,\n" +
                            "           j.bianm as bianh,\n" +
                            "           j.ches,\n" +
                            "           j.jiessl as jiesml,\n" +
                            "           j.hansdj as danj,\n" +
                            "           j.hansmk as meik,\n" +
                            "           nvl(jy.hansyf,0) as yunf,\n" +
                            "           j.chaokdl,\n" +
                            "           j.kuidjfyf+j.kuidjfzf as jufje,\n" +
                            "           j.hansmk+nvl(jy.hansyf,0) as yingfzje,\n" +
                            "           decode(to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM'),\n" +
                            "                                     to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM'),\n" +
                            "                                     decode(to_char(j.fahksrq,'dd'),to_char(j.fahjzrq,'dd'),to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日发货',to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'-'||to_char(j.fahjzrq,'dd')||'日发货'),\n" +
                            "                                     to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日-'||to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM')||'月'||to_char(j.fahjzrq,'dd')||'日发货') as fahrq\n" +
                            "\n" +
                            "from " + table_mk + " j,meikxxb m,hetb h," + table_yf + " jy\n" +
                            "where j.meikxxb_id = m.id\n" +
                            "      and j.hetb_id = h.id\n" +
                            "      and j.id = jy." + tiaoj + "(+)\n" +
                            "      and j.bianm = '" + jiesbh + "'\n";
        } else {

            sql_jsb =
                    "select m.quanc as hetdw,\n" +
                            "           to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq,\n" +
                            "           h.hetbh as het,\n" +
                            "           j.bianm as bianh,\n" +
                            "           j.ches,\n" +
                            "           j.jiessl as jiesml,\n" +
                            "           j.hansdj as danj,\n" +
                            "           0 as meik,\n" +
                            "           nvl(j.hansyf,0) as yunf,\n" +
                            "           j.chaokdl,\n" +
                            "           j.kuidjfyf+j.kuidjfzf as jufje,\n" +
                            "           nvl(j.hansyf,0) as yingfzje,\n" +
                            "           decode(to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM'),\n" +
                            "                                     to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM'),\n" +
                            "                                     decode(to_char(j.fahksrq,'dd'),to_char(j.fahjzrq,'dd'),to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日发货',to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'-'||to_char(j.fahjzrq,'dd')||'日发货'),\n" +
                            "                                     to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日-'||to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM')||'月'||to_char(j.fahjzrq,'dd')||'日发货') as fahrq\n" +
                            "\n" +
                            "from meikxxb m,hetys h," + table_yf + " j\n" +
                            "where j.meikxxb_id = m.id\n" +
                            "      and j.hetb_id = h.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n";
        }

        ResultSetList rsl = cn.getResultSetList(sql_jsb);
        if (rsl.next()) {
            hetdw = rsl.getString("hetdw");
            jiesrq = rsl.getString("jiesrq");
            het = rsl.getString("het");
            bianh = rsl.getString("bianh");

            ches = rsl.getString("ches");
            jiesml = rsl.getString("jiesml");
            danj = rsl.getString("danj");
            meik = rsl.getString("meik");
            yunf = rsl.getString("yunf");
            chaod = rsl.getString("chaokdl");
            jufje = rsl.getString("jufje");
            yingfzje = rsl.getString("yingfzje");

            fahrq = rsl.getString("fahrq");

        }

        StringBuffer strSQL = new StringBuffer();
        String sql = "";

        if (Flag) {
//			煤款

            sql =
                    "select meikdw as mkdw,meikdw,faz,faz,jij,kous,stad,mt,mad,vdaf,aad,qnetar,ches,huops,yanss,danj,jine from\n" +
                            "((select decode(grouping(meikdw),1,'合计',meikdw) as meikdw,\n" +
                            "           '合计' as faz,\n" +
//				"           sum(jij) as jij,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(jij*jiessl)/sum(jiessl),5)) as jij,\n" +
                            "           sum(kous) as kous,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(stad*jiessl)/sum(jiessl),2)) as stad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mt*jiessl)/sum(jiessl),1)) as mt,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mad*jiessl)/sum(jiessl),2)) as mad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(vdaf*jiessl)/sum(jiessl),2)) as vdaf,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(aad*jiessl)/sum(jiessl),2)) as aad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(qnetar*jiessl)/sum(jiessl),2)) as qnetar,\n" +
                            "           sum(ches) as ches,\n" +
                            "           sum(huops) as huops,\n" +
                            "           sum(yanss) as yanss,\n" +
//				"           sum(danj) as danj,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(danj*jiessl)/sum(jiessl),4)) as danj,\n" +
                            "           sum(jine) as jine\n" +
                            "from\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vdaf) as vdaf,\n" +
                            "           max(d.aad) as aad,\n" +
                            "           max(d.qnetar) as qnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine,\n" +
                            "           max(d.jiessl) as jiessl\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_mk + " j\n" +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh)\n" +
                            "group by rollup(meikdw)\n" +
                            "having not (grouping(meikdw))=0)\n" +
                            "union\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vdaf) as vdaf,\n" +
                            "           max(d.aad) as aad,\n" +
                            "           max(d.qnetar) as qnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_mk + " j\n" +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh))\n" +
                            "order by decode(mkdw,'合计',2,1),meikdw";

        } else {
//			运费
            sql =
                    "select meikdw as mkdw,meikdw,faz,faz,jij,kous,stad,mt,mad,vdaf,aad,qnetar,ches,huops,yanss,danj,jine from\n" +
                            "((select decode(grouping(meikdw),1,'合计',meikdw) as meikdw,\n" +
                            "           '合计' as faz,\n" +
//				"           sum(jij) as jij,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(jij*jiessl)/sum(jiessl),5)) as jij,\n" +
                            "           sum(kous) as kous,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(stad*jiessl)/sum(jiessl),2)) as stad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mt*jiessl)/sum(jiessl),1)) as mt,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mad*jiessl)/sum(jiessl),2)) as mad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(vdaf*jiessl)/sum(jiessl),2)) as vdaf,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(aad*jiessl)/sum(jiessl),2)) as aad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(qnetar*jiessl)/sum(jiessl),2)) as qnetar,\n" +
                            "           sum(ches) as ches,\n" +
                            "           sum(huops) as huops,\n" +
                            "           sum(yanss) as yanss,\n" +
//				"           sum(danj) as danj,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(danj*jiessl)/sum(jiessl),4)) as danj,\n" +
                            "           sum(jine) as jine\n" +
                            "from\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vdaf) as vdaf,\n" +
                            "           max(d.aad) as aad,\n" +
                            "           max(d.qnetar) as qnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine,\n" +
                            "           max(d.jiessl) as jiessl\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_yf + " j\n" +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh)\n" +
                            "group by rollup(meikdw)\n" +
                            "having not (grouping(meikdw))=0)\n" +
                            "union\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vdaf) as vdaf,\n" +
                            "           max(d.aad) as aad,\n" +
                            "           max(d.qnetar) as qnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_yf + " j\n" +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh))\n" +
                            "order by decode(mkdw,'合计',2,1),meikdw";
        }

        strSQL.append(sql);

        String ArrHeader[][] = new String[4][17];
        ArrHeader[0] = new String[]{"车数", "结算煤量", "单价", "煤款", "什费", "什费", "运费", "运费", "运费", "其它运杂费", "其它运杂费", "扣超\\亏吨运费", "扣超\\亏吨运费", "拒付金额", "拒付金额", "应付总金额", "应付总金额"};
        ArrHeader[1] = new String[]{ches, jiesml, danj, meik, shif, shif, yunf, yunf, yunf, qityzf, qityzf, chaod, chaod, jufje, jufje, yingfzje, yingfzje};
        ArrHeader[2] = new String[]{"煤矿单位", "煤矿单位", "发站", "发站", "基价", "扣水", "煤质", "煤质", "煤质", "煤质", "煤质", "煤质", "车数", "货票数", "验收数", "单价", "金额"};
        ArrHeader[3] = new String[]{"煤矿单位", "煤矿单位", "发站", "发站", "基价", "扣水", "Stad", "Mt", "Mad", "Vdaf", "Aad", "Qnetar", "车数", "货票数", "验收数", "单价", "金额"};

        int ArrWidth[] = new int[]{60, 60, 45, 80, 40, 40, 40, 40, 40, 40, 40, 50, 30, 60, 60, 40, 80};


        ResultSet rs = cn.getResultSet(strSQL.toString());

        // 数据
        Table tb = new Table(rs, 4, 0, 0);
        rt.setBody(tb);
//		Visit visit = (Visit) getPage().getVisit();
        String type = MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(visit.getDiancxxb_id()), "ZGDT");
        if (type.equals("GD")) {
//			rt.setTitle("国电电力发展股份有限公司燃料结算单批次明细单", ArrWidth);
            rt.setTitle("燃料结算单批次明细单", ArrWidth);
        } else {
            rt.setTitle("大唐公司燃料结算单批次明细单", ArrWidth);
        }
//		rt.setTitle("大唐公司燃料结算单批次明细单", ArrWidth);
        rt.setDefaultTitle(1, 4, "合同单位：" + hetdw, Table.ALIGN_LEFT);
        rt.setDefaultTitle(5, 4, "结算日期：" + jiesrq, Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 5, "合同：" + het, Table.ALIGN_LEFT);
        rt.setDefaultTitle(14, 4, "编号：" + bianh, Table.ALIGN_LEFT);

        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(18);
        rt.body.setHeaderData(ArrHeader);// 表头数据
        rt.body.setRowHeight(10);

        rt.body.AddTableRow(1);//表格最下面添加一行
//		合并单元格
        rt.body.mergeCell(1, 5, 1, 6);
        rt.body.mergeCell(2, 5, 2, 6);
        rt.body.mergeCell(1, 7, 1, 9);
        rt.body.mergeCell(2, 7, 2, 9);
        rt.body.mergeCell(1, 10, 1, 11);
        rt.body.mergeCell(2, 10, 2, 11);
        rt.body.mergeCell(1, 12, 1, 13);
        rt.body.mergeCell(2, 12, 2, 13);
        rt.body.mergeCell(1, 14, 1, 15);
        rt.body.mergeCell(2, 14, 2, 15);
        rt.body.mergeCell(1, 16, 1, 17);
        rt.body.mergeCell(2, 16, 2, 17);

        rt.body.mergeCell(3, 1, 4, 2);
        rt.body.mergeCell(3, 3, 4, 4);
        rt.body.mergeCell(3, 5, 4, 5);
        rt.body.mergeCell(3, 6, 4, 6);
        rt.body.mergeCell(3, 7, 3, 12);
        rt.body.mergeCell(3, 13, 4, 13);
        rt.body.mergeCell(3, 14, 4, 14);
        rt.body.mergeCell(3, 15, 4, 15);
        rt.body.mergeCell(3, 16, 4, 16);
        rt.body.mergeCell(3, 17, 4, 17);

        int row = rt.body.getRows();
        for (int i = 5; i < row - 1; i++) {
            rt.body.mergeCell(i, 1, i, 2);
            rt.body.mergeCell(i, 3, i, 4);
        }
        rt.body.mergeCell(row - 1, 1, row - 1, 4);
        rt.body.mergeCell(row, 2, row, 17);
//		rt.body.mergeFixedRowCol();

        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_CENTER);
        rt.body.setColAlign(8, Table.ALIGN_CENTER);
        rt.body.setColAlign(9, Table.ALIGN_CENTER);
        rt.body.setColAlign(10, Table.ALIGN_CENTER);
        rt.body.setColAlign(11, Table.ALIGN_CENTER);
        rt.body.setColAlign(12, Table.ALIGN_CENTER);
        rt.body.setColAlign(13, Table.ALIGN_CENTER);
        rt.body.setColAlign(14, Table.ALIGN_CENTER);
        rt.body.setColAlign(15, Table.ALIGN_CENTER);
        rt.body.setColAlign(16, Table.ALIGN_CENTER);
        rt.body.setColAlign(17, Table.ALIGN_CENTER);

        rt.body.setCellValue(row, 1, "备注");
        rt.body.setCellValue(row, 2, fahrq);

        rt.createFooter(1, ArrWidth);
        if (MainGlobal.getXitxx_item("结算", "东胜结算明细样式", "0", "否").equals("是")) {
            rt.setDefautlFooter(1, 4, "供应商复核：", Table.ALIGN_LEFT);
        } else {
            rt.setDefautlFooter(1, 2, "部门负责人：", Table.ALIGN_LEFT);
            rt.setDefautlFooter(7, 2, "审核：", Table.ALIGN_LEFT);
            rt.setDefautlFooter(15, 2, "结算员：", Table.ALIGN_LEFT);
        }

        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        cn.Close();
        return rt.getAllPagesHtml();

    }

    //酒泉特殊批次明细单
    private String getSelectData_no_pc_jq(JDBCcon cn) {

        _CurrentPage = 1;
        _AllPages = 1;
        Visit visit = (Visit) this.getPage().getVisit();
//		JDBCcon cn = new JDBCcon();
        Report rt = new Report();

        String hetdw = "";
        String jiesrq = "";
        String het = "";
        String bianh = "";

        String ches = "";
        String jiesml = "";
        String danj = "";
        String meik = "";
        String shif = "0.00";
        String yunf = "";
        String qityzf = "0";
        String chaod = "";
        String jufje = "";
        String yingfzje = "";

        String fahrq = "";

        String table = "";
        String table_mk = "";
        String table_yf = "";
        String jiesbh = "";
        String tiaoj = "";
        String sql_jsb = "";
        table = visit.getString1().substring(0, visit.getString1().indexOf(";"));
        table_mk = table.substring(0, table.indexOf(","));
        table_yf = table.substring(table.indexOf(",") + 1);

        jiesbh = visit.getString1().substring(visit.getString1().indexOf(";") + 1);

        if (table_mk.equals("jiesb")) {
            tiaoj = "diancjsmkb_id";
        } else if (table_mk.equals("diancjsmkb")) {
            tiaoj = "diancjsmkb_id";
        } else if (table_mk.equals("kuangfjsmkb")) {
            tiaoj = "kuangfjsmkb_id";
        }

        boolean Flag = false;
//		如果为true，则为煤款结算
        Flag = isMeikjsd(table_mk, table_yf, jiesbh);

        if (Flag) {
//			煤款结算
            sql_jsb =
                    "select m.quanc as hetdw,\n" +
                            "           to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq,\n" +
                            "           h.hetbh as het,\n" +
                            "           j.bianm as bianh,\n" +
                            "           j.ches,\n" +
                            "           j.jiessl as jiesml,\n" +
                            "           j.hansdj as danj,\n" +
                            "           j.hansmk as meik,\n" +
                            "           nvl(jy.hansyf,0) as yunf,\n" +
                            "           j.chaokdl,\n" +
                            "           j.kuidjfyf+j.kuidjfzf as jufje,\n" +
                            "           j.hansmk+nvl(jy.hansyf,0) as yingfzje,\n" +
                            "           decode(to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM'),\n" +
                            "                                     to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM'),\n" +
                            "                                     decode(to_char(j.fahksrq,'dd'),to_char(j.fahjzrq,'dd'),to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日发货',to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'-'||to_char(j.fahjzrq,'dd')||'日发货'),\n" +
                            "                                     to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日-'||to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM')||'月'||to_char(j.fahjzrq,'dd')||'日发货') as fahrq\n" +
                            "\n" +
                            "from " + table_mk + " j,meikxxb m,hetb h," + table_yf + " jy\n" +
                            "where j.meikxxb_id = m.id\n" +
                            "      and j.hetb_id = h.id\n" +
                            "      and j.id = jy." + tiaoj + "(+)\n" +
                            "      and j.bianm = '" + jiesbh + "'\n";
        } else {

            sql_jsb =
                    "select m.quanc as hetdw,\n" +
                            "           to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq,\n" +
                            "           h.hetbh as het,\n" +
                            "           j.bianm as bianh,\n" +
                            "           j.ches,\n" +
                            "           j.jiessl as jiesml,\n" +
                            "           j.hansdj as danj,\n" +
                            "           0 as meik,\n" +
                            "           nvl(j.hansyf,0) as yunf,\n" +
                            "           j.chaokdl,\n" +
                            "           j.kuidjfyf+j.kuidjfzf as jufje,\n" +
                            "           nvl(j.hansyf,0) as yingfzje,\n" +
                            "           decode(to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM'),\n" +
                            "                                     to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM'),\n" +
                            "                                     decode(to_char(j.fahksrq,'dd'),to_char(j.fahjzrq,'dd'),to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日发货',to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'-'||to_char(j.fahjzrq,'dd')||'日发货'),\n" +
                            "                                     to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日-'||to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM')||'月'||to_char(j.fahjzrq,'dd')||'日发货') as fahrq\n" +
                            "\n" +
                            "from meikxxb m,hetys h," + table_yf + " j\n" +
                            "where j.meikxxb_id = m.id\n" +
                            "      and j.hetb_id = h.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n";
        }

        ResultSetList rsl = cn.getResultSetList(sql_jsb);
        if (rsl.next()) {
            hetdw = rsl.getString("hetdw");
            jiesrq = rsl.getString("jiesrq");
            het = rsl.getString("het");
            bianh = rsl.getString("bianh");

            ches = rsl.getString("ches");
            jiesml = rsl.getString("jiesml");
            danj = rsl.getString("danj");
            meik = rsl.getString("meik");
            yunf = rsl.getString("yunf");
            chaod = rsl.getString("chaokdl");
            jufje = rsl.getString("jufje");
            yingfzje = rsl.getString("yingfzje");

            fahrq = rsl.getString("fahrq");

        }

        StringBuffer strSQL = new StringBuffer();
        String sql = "";

        if (Flag) {
//			煤款

            sql =
                    "select meikdw as mkdw,meikdw,faz,faz,jij,kous,stad,mt,mad,vad,aad,qnetar,ches,huops,yanss,danj,jine from\n" +
                            "((select decode(grouping(meikdw),1,'合计',meikdw) as meikdw,\n" +
                            "           '合计' as faz,\n" +
//				"           sum(jij) as jij,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(jij*jiessl)/sum(jiessl),5)) as jij,\n" +
                            "           sum(kous) as kous,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(stad*jiessl)/sum(jiessl),2)) as stad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mt*jiessl)/sum(jiessl),1)) as mt,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mad*jiessl)/sum(jiessl),2)) as mad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(vad*jiessl)/sum(jiessl),2)) as vad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(aad*jiessl)/sum(jiessl),2)) as aad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(round_new(qnetar * 1000 / 4.1816,0) * jiessl) / sum(jiessl),0)) as qnetar,\n" +
                            "           sum(ches) as ches,\n" +
                            "           sum(huops) as huops,\n" +
                            "           sum(yanss) as yanss,\n" +
//				"           sum(danj) as danj,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(danj*jiessl)/sum(jiessl),4)) as danj,\n" +
                            "           sum(jine) as jine\n" +
                            "from\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vad) as vad,\n" +
                            "           max(d.aad) as aad,\n" +
                            "           max(d.qnetar) as qnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine,\n" +
                            "           max(d.jiessl) as jiessl\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_mk + " j\n" +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh)\n" +
                            "group by rollup(meikdw)\n" +
                            "having not (grouping(meikdw))=0)\n" +
                            "union\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vad) as vad,\n" +
                            "           max(d.aad) as aad,\n" +
                            "            max(round_new(d.qnetar*1000/4.1816,0)) as qnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_mk + " j\n"

                            +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh))\n" +
                            "order by decode(mkdw,'合计',2,1),meikdw";

        } else {
//			运费
            sql =
                    "select meikdw as mkdw,meikdw,faz,faz,jij,kous,stad,mt,mad,vad,aad,qnetar,ches,huops,yanss,danj,jine from\n" +
                            "((select decode(grouping(meikdw),1,'合计',meikdw) as meikdw,\n" +
                            "           '合计' as faz,\n" +
//				"           sum(jij) as jij,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(jij*jiessl)/sum(jiessl),5)) as jij,\n" +
                            "           sum(kous) as kous,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(stad*jiessl)/sum(jiessl),2)) as stad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mt*jiessl)/sum(jiessl),1)) as mt,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(mad*jiessl)/sum(jiessl),2)) as mad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(vad*jiessl)/sum(jiessl),2)) as vad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(aad*jiessl)/sum(jiessl),2)) as aad,\n" +
                            "           decode(sum(jiessl),0,0,round_new(sum(round_new(qnetar * 1000 / 4.1816,0) * jiessl) / sum(jiessl),0)) as qnetar,\n" +
                            "           sum(ches) as ches,\n" +
                            "           sum(huops) as huops,\n" +
                            "           sum(yanss) as yanss,\n" +
//				"           sum(danj) as danj,\n" + 
                            "			decode(sum(jiessl),0,0,round_new(sum(danj*jiessl)/sum(jiessl),4)) as danj,\n" +
                            "           sum(jine) as jine\n" +
                            "from\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vad) as vad,\n" +
                            "           max(d.aad) as aad,\n" +
                            "           max(d.qnetar) as qnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine,\n" +
                            "           max(d.jiessl) as jiessl\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_yf + " j\n" +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh)\n" +
                            "group by rollup(meikdw)\n" +
                            "having not (grouping(meikdw))=0)\n" +
                            "union\n" +
                            "(select max(m.quanc) as meikdw,\n" +
                            "           max(c.mingc) as faz,\n" +
                            "           max(d.hetj) as jij,\n" +
                            "           max(d.kous) as kous,\n" +
                            "           max(d.stad) as stad,\n" +
                            "           max(d.mt) as mt,\n" +
                            "           max(d.mad) as mad,\n" +
                            "           max(d.vad) as vad,\n" +
                            "           max(d.aad) as aad,\n" +
                            "            max(round_new(d.qnetar*1000/4.1816,0)) asqnetar,\n" +
                            "           max(d.ches) as ches,\n" +
                            "           max(d.gongfsl) as huops,\n" +
                            "           max(d.yanssl) as yanss,\n" +
                            "           max(d.jiesdj) as danj,\n" +
                            "           max(d.zongje) as jine\n" +
                            "from danpcjsmxb d,meikxxb m,chezxxb c," + table_yf + " j\n" +
                            "where d.meikxxb_id = m.id\n" +
                            "      and d.faz_id = c.id\n" +
                            "	   and d.jiesdid = j.id\n" +
                            "      and j.bianm = '" + jiesbh + "'\n" +
                            "group by d.xuh))\n" +
                            "order by decode(mkdw,'合计',2,1),meikdw";
        }

        strSQL.append(sql);

        String ArrHeader[][] = new String[4][17];
        ArrHeader[0] = new String[]{"车数", "结算煤量", "单价", "煤款", "什费", "什费", "运费", "运费", "运费", "其它运杂费", "其它运杂费", "扣超\\亏吨运费", "扣超\\亏吨运费", "拒付金额", "拒付金额", "应付总金额", "应付总金额"};
        ArrHeader[1] = new String[]{ches, jiesml, danj, meik, shif, shif, yunf, yunf, yunf, qityzf, qityzf, chaod, chaod, jufje, jufje, yingfzje, yingfzje};
        ArrHeader[2] = new String[]{"煤矿单位", "煤矿单位", "发站", "发站", "基价", "扣水", "煤质", "煤质", "煤质", "煤质", "煤质", "煤质", "车数", "货票数", "验收数", "单价", "金额"};
        ArrHeader[3] = new String[]{"煤矿单位", "煤矿单位", "发站", "发站", "基价", "扣水", "Stad", "Mt", "Mad", "Vad", "Aad", "Qnetar", "车数", "货票数", "验收数", "单价", "金额"};

        int ArrWidth[] = new int[]{60, 60, 45, 80, 40, 40, 40, 40, 40, 40, 40, 50, 30, 60, 60, 40, 80};


        ResultSet rs = cn.getResultSet(strSQL.toString());

        // 数据
        Table tb = new Table(rs, 4, 0, 0);
        rt.setBody(tb);
//		Visit visit = (Visit) getPage().getVisit();
        String type = MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(visit.getDiancxxb_id()), "ZGDT");
        if (type.equals("GD")) {
//			rt.setTitle("国电电力发展股份有限公司燃料结算单批次明细单", ArrWidth);
            rt.setTitle("燃料结算单批次明细单", ArrWidth);
        } else {
            rt.setTitle("大唐公司燃料结算单批次明细单", ArrWidth);
        }
//		rt.setTitle("大唐公司燃料结算单批次明细单", ArrWidth);
        rt.setDefaultTitle(1, 4, "合同单位：" + hetdw, Table.ALIGN_LEFT);
        rt.setDefaultTitle(5, 4, "结算日期：" + jiesrq, Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 5, "合同：" + het, Table.ALIGN_LEFT);
        rt.setDefaultTitle(14, 4, "编号：" + bianh, Table.ALIGN_LEFT);

        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(18);
        rt.body.setHeaderData(ArrHeader);// 表头数据
        rt.body.setRowHeight(10);

        rt.body.AddTableRow(1);//表格最下面添加一行
//		合并单元格
        rt.body.mergeCell(1, 5, 1, 6);
        rt.body.mergeCell(2, 5, 2, 6);
        rt.body.mergeCell(1, 7, 1, 9);
        rt.body.mergeCell(2, 7, 2, 9);
        rt.body.mergeCell(1, 10, 1, 11);
        rt.body.mergeCell(2, 10, 2, 11);
        rt.body.mergeCell(1, 12, 1, 13);
        rt.body.mergeCell(2, 12, 2, 13);
        rt.body.mergeCell(1, 14, 1, 15);
        rt.body.mergeCell(2, 14, 2, 15);
        rt.body.mergeCell(1, 16, 1, 17);
        rt.body.mergeCell(2, 16, 2, 17);

        rt.body.mergeCell(3, 1, 4, 2);
        rt.body.mergeCell(3, 3, 4, 4);
        rt.body.mergeCell(3, 5, 4, 5);
        rt.body.mergeCell(3, 6, 4, 6);
        rt.body.mergeCell(3, 7, 3, 12);
        rt.body.mergeCell(3, 13, 4, 13);
        rt.body.mergeCell(3, 14, 4, 14);
        rt.body.mergeCell(3, 15, 4, 15);
        rt.body.mergeCell(3, 16, 4, 16);
        rt.body.mergeCell(3, 17, 4, 17);

        int row = rt.body.getRows();
        for (int i = 5; i < row - 1; i++) {
            rt.body.mergeCell(i, 1, i, 2);
            rt.body.mergeCell(i, 3, i, 4);
        }
        rt.body.mergeCell(row - 1, 1, row - 1, 4);
        rt.body.mergeCell(row, 2, row, 17);
//		rt.body.mergeFixedRowCol();

        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_CENTER);
        rt.body.setColAlign(8, Table.ALIGN_CENTER);
        rt.body.setColAlign(9, Table.ALIGN_CENTER);
        rt.body.setColAlign(10, Table.ALIGN_CENTER);
        rt.body.setColAlign(11, Table.ALIGN_CENTER);
        rt.body.setColAlign(12, Table.ALIGN_CENTER);
        rt.body.setColAlign(13, Table.ALIGN_CENTER);
        rt.body.setColAlign(14, Table.ALIGN_CENTER);
        rt.body.setColAlign(15, Table.ALIGN_CENTER);
        rt.body.setColAlign(16, Table.ALIGN_CENTER);
        rt.body.setColAlign(17, Table.ALIGN_CENTER);

        rt.body.setCellValue(row, 1, "备注");
        rt.body.setCellValue(row, 2, fahrq);

        rt.createFooter(1, ArrWidth);
        if (MainGlobal.getXitxx_item("结算", "东胜结算明细样式", "0", "否").equals("是")) {
            rt.setDefautlFooter(1, 4, "供应商复核：", Table.ALIGN_LEFT);
        } else {
            rt.setDefautlFooter(1, 2, "部门负责人：", Table.ALIGN_LEFT);
            rt.setDefautlFooter(7, 2, "审核：", Table.ALIGN_LEFT);
            rt.setDefautlFooter(15, 2, "结算员：", Table.ALIGN_LEFT);
        }

        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        cn.Close();
        return rt.getAllPagesHtml();

    }

    private boolean isMeikjsd(String table_mk, String table_yf, String jiesbh) {
        // TODO 自动生成方法存根

//		函数逻辑：判断所传的jiesbh 是否为煤款结算，如果是返回true，如果不是返回false
        JDBCcon con = new JDBCcon();
        boolean Flag = false;
        String sql = "select * from " + table_mk + " where bianm='" + jiesbh + "'";
        ResultSetList rsl = con.getResultSetList(sql);

        if (rsl.getRows() > 0) {
//			属于煤款结算
            Flag = true;
        }
        rsl.close();
        con.Close();

        return Flag;
    }

    private String getSelectData_has_pc(JDBCcon cn) {// 显示 日期和批次


        _CurrentPage = 1;
        _AllPages = 1;
        Visit visit = (Visit) this.getPage().getVisit();
//		JDBCcon cn = new JDBCcon();
        Report rt = new Report();

        String hetdw = "";
        String jiesrq = "";
        String het = "";
        String bianh = "";

        String ches = "";
        String jiesml = "";
        String danj = "";
        String meik = "";
        String shif = "0.00";
        String yunf = "";
        String qityzf = "0";
        String chaod = "";
        String jufje = "";
        String yingfzje = "";

        String fahrq = "";

        String table = "";
        String table_mk = "";
        String table_yf = "";
        String jiesbh = "";
        String tiaoj = "";

        table = visit.getString1().substring(0, visit.getString1().indexOf(";"));
        table_mk = table.substring(0, table.indexOf(","));
        table_yf = table.substring(table.indexOf(",") + 1);

        jiesbh = visit.getString1().substring(visit.getString1().indexOf(";") + 1);

        if (table_mk.equals("jiesb")) {
            tiaoj = "diancjsmkb_id";
        } else if (table_mk.equals("diancjsmkb")) {
            tiaoj = "diancjsmkb_id";
        } else if (table_mk.equals("kuangfjsmkb")) {
            tiaoj = "kuangfjsmkb_id";
        }
        String sql_jsb =
                "select m.quanc as hetdw,\n" +
                        "           to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq,\n" +
                        "           h.hetbh as het,\n" +
                        "           j.bianm as bianh,\n" +
                        "           j.ches,\n" +
                        "           j.jiessl as jiesml,\n" +
                        "           j.hansdj as danj,\n" +
                        "           j.hansmk as meik,\n" +
                        "           nvl(jy.hansyf,0) as yunf,\n" +
                        "           j.chaokdl,\n" +
                        "           j.kuidjfyf+j.kuidjfzf as jufje,\n" +
                        "           j.hansmk+nvl(jy.hansyf,0) as yingfzje,\n" +
                        "           decode(to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM'),\n" +
                        "                                     to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM'),\n" +
                        "                                     decode(to_char(j.fahksrq,'dd'),to_char(j.fahjzrq,'dd'),to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日发货',to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'-'||to_char(j.fahjzrq,'dd')||'日发货'),\n" +
                        "                                     to_char(j.fahksrq,'yyyy')||'年'||to_char(j.fahksrq,'MM')||'月'||to_char(j.fahksrq,'dd')||'日-'||to_char(j.fahjzrq,'yyyy')||'年'||to_char(j.fahjzrq,'MM')||'月'||to_char(j.fahjzrq,'dd')||'日发货') as fahrq\n" +
                        "\n" +
                        "from " + table_mk + " j,meikxxb m,hetb h," + table_yf + " jy\n" +
                        "where j.meikxxb_id = m.id\n" +
                        "      and j.hetb_id = h.id\n" +
                        "      and j.id = jy." + tiaoj + "(+)\n" +
                        "      and j.bianm = '" + jiesbh + "'\n";

        ResultSetList rsl = cn.getResultSetList(sql_jsb);
        if (rsl.next()) {
            hetdw = rsl.getString("hetdw");
            jiesrq = rsl.getString("jiesrq");
            het = rsl.getString("het");
            bianh = rsl.getString("bianh");

            ches = rsl.getString("ches");
            jiesml = rsl.getString("jiesml");
            danj = rsl.getString("danj");
            meik = rsl.getString("meik");
            yunf = rsl.getString("yunf");
            chaod = rsl.getString("chaokdl");
            jufje = rsl.getString("jufje");
            yingfzje = rsl.getString("yingfzje");

            fahrq = rsl.getString("fahrq");

        }

        StringBuffer strSQL = new StringBuffer();
        String sql =
                "select k.meikdw as mkdw,k.meikdw,k.faz,\n" +
                        " ( select case when min(f.fahrq) = max(f.fahrq)	\n" +
                        "		then to_char(min(f.fahrq), 'yyyy-MM-dd')	\n" +
                        "		else to_char(min(f.fahrq), 'yyyy-MM-dd') || '至<br>' ||	\n" +
                        "   	to_char(max(f.fahrq), 'yyyy-MM-dd')	\n" +
                        "   	end case  from fahb f where f.lie_id in (select *  from table( split_del(k.lie_id,',')))) fahrq,\n" +
                        "  ( select case when min(f.daohrq) = max(f.daohrq)	\n" +
                        "       then to_char(min(f.daohrq), 'yyyy-MM-dd')	\n" +
                        "       else to_char(min(f.daohrq), 'yyyy-MM-dd') || '至<br>' ||	\n" +
                        "       to_char(max(f.daohrq), 'yyyy-MM-dd')		\n" +
                        "       end case from fahb f where f.lie_id in (select *  from table( split_del(k.lie_id,',')))) daohrq,\n" +
                        "  ( select case when min(cy.bianm) = max(cy.bianm)	\n" +
                        "       then min(cy.bianm)	\n" +
                        "       else min(cy.bianm) || '至<br>' || max(cy.bianm)	\n" +
                        "       end case from fahb f,caiyb cy where f.zhilb_id=cy.zhilb_id and f.lie_id in (  select *  from table( split_del(k.lie_id,',') )    )  \n" +
                        //	"    and f.fahrq=(select  min(f.fahrq) from fahb f,caiyb cy where f.zhilb_id=cy.zhilb_id and f.lie_id in (  select *  from table( split_del(k.lie_id,',') )   ) )  \n" +
                        //	"  and rownum=1 \n" +
                        ") pich,\n" +
                        "k.jij,k.kous,k.stad,k.mt,k.mad,k.vdaf,k.aad,k.qnetar,k.ches,k.huops,k.yanss,k.qnetar_zj,k.qnetar_zje,k.std_zj,k.std_zje,k.danj,k.jine from\n" +
                        "((select decode(grouping(meikdw),1,'合计',meikdw) as meikdw,\n" +
                        "           '合计' as faz,\n" +
//			"           sum(jij) as jij,\n" + 
                        "			decode(sum(jiessl),0,0,round_new(sum(jij*jiessl)/sum(jiessl),5)) as jij,\n" +
                        "           sum(kous) as kous,\n" +
                        "           decode(sum(jiessl),0,0,round_new(sum(stad*jiessl)/sum(jiessl),2)) as stad,\n" +
                        "           decode(sum(jiessl),0,0,round_new(sum(mt*jiessl)/sum(jiessl),1)) as mt,\n" +
                        "           decode(sum(jiessl),0,0,round_new(sum(mad*jiessl)/sum(jiessl),2)) as mad,\n" +
                        "           decode(sum(jiessl),0,0,round_new(sum(vdaf*jiessl)/sum(jiessl),2)) as vdaf,\n" +
                        "           decode(sum(jiessl),0,0,round_new(sum(aad*jiessl)/sum(jiessl),2)) as aad,\n" +
                        "           decode(sum(jiessl),0,0,round_new(sum(qnetar*jiessl)/sum(jiessl),2)) as qnetar,\n" +
                        "           sum(ches) as ches,\n" +
                        "           sum(huops) as huops,\n" +
                        "           sum(yanss) as yanss,\n" +
                        "			decode(sum(jiessl),0,0,round_new(sum(getdanpcjsmx('Qnetar','zhejbz',jiesb_id,xuh) * jiessl) / sum(jiessl), 4)) as qnetar_zj,\n" +
                        "           sum(getdanpcjsmx('Qnetar','zhejje',jiesb_id,xuh)) as qnetar_zje,\n" +
                        "           decode(sum(jiessl),0,0,round_new(sum(getdanpcjsmx('Std','zhejbz',jiesb_id,xuh) * jiessl) / sum(jiessl), 4)) as std_zj,\n" +
                        "           sum(getdanpcjsmx('Std','zhejje',jiesb_id,xuh)) as std_zje,\n" +
//			"           sum(danj) as danj,\n" + 
                        "			decode(sum(jiessl),0,0,round_new(sum(danj*jiessl)/sum(jiessl),4)) as danj,\n" +
                        "           sum(jine) as jine,\n" +
                        "			'-1' lie_id \n" +
                        "from\n" +
                        "(select 	d.xuh,\n" +
                        "           max(j.id) as jiesb_id," +
                        "			max(m.quanc) as meikdw,\n" +
                        "           max(c.mingc) as faz,\n" +
                        "           max(d.hetj) as jij,\n" +
                        "           max(d.kous) as kous,\n" +
                        "           max(d.stad) as stad,\n" +
                        "           max(d.mt) as mt,\n" +
                        "           max(d.mad) as mad,\n" +
                        "           max(d.vdaf) as vdaf,\n" +
                        "           max(d.aad) as aad,\n" +
                        "           max(d.qnetar) as qnetar,\n" +
                        "           max(d.ches) as ches,\n" +
                        "           max(d.gongfsl) as huops,\n" +
                        "           max(d.yanssl) as yanss,\n" +
                        "           max(d.jiesdj) as danj,\n" +
                        "           max(d.zongje) as jine,\n" +
                        "           max(d.jiessl) as jiessl\n" +
                        "from danpcjsmxb d,meikxxb m,chezxxb c," + table_mk + " j\n" +
                        "where d.meikxxb_id = m.id\n" +
                        "      and d.faz_id = c.id\n" +
                        "	   and d.jiesdid = j.id\n" +
                        "      and j.bianm = '" + jiesbh + "'\n" +
                        "group by d.xuh)\n" +
                        "group by rollup(meikdw)\n" +
                        "having not (grouping(meikdw))=0)\n" +
                        "union\n" +
                        "(select max(m.quanc) as meikdw,\n" +
                        "           max(c.mingc) as faz,\n" +
                        "           max(d.hetj) as jij,\n" +
                        "           max(d.kous) as kous,\n" +
                        "           max(d.stad) as stad,\n" +
                        "           max(d.mt) as mt,\n" +
                        "           max(d.mad) as mad,\n" +
                        "           max(d.vdaf) as vdaf,\n" +
                        "           max(d.aad) as aad,\n" +
                        "           max(d.qnetar) as qnetar,\n" +
                        "           max(d.ches) as ches,\n" +
                        "           max(d.gongfsl) as huops,\n" +
                        "           max(d.yanssl) as yanss,\n" +
                        "			getdanpcjsmx('Qnetar','zhejbz',max(j.id),d.xuh) as qnetar_zj,\n" +
                        "           getdanpcjsmx('Qnetar','zhejje',max(j.id),d.xuh) as qnetar_zje,\n" +
                        "           getdanpcjsmx('Std','zhejbz',max(j.id),d.xuh) as std_zj,\n" +
                        "           getdanpcjsmx('Std','zhejje',max(j.id),d.xuh) as std_zje," +
                        "           max(d.jiesdj) as danj,\n" +
                        "           max(d.zongje) as jine,\n" +
                        "		    max( to_char(d.lie_id) ) lie_id \n" +
                        "from danpcjsmxb d,meikxxb m,chezxxb c," + table_mk + " j\n" +
                        "where d.meikxxb_id = m.id\n" +
                        "      and d.faz_id = c.id\n" +
                        "	   and d.jiesdid = j.id\n" +
                        "      and j.bianm = '" + jiesbh + "'\n" +
                        "group by d.xuh)) k \n" +
                        "order by decode(mkdw,'合计',2,1) asc ,\n" +
                        " (select min(f.fahrq) from fahb f where f.lie_id in (select *  from table( split_del(k.lie_id,',') ) )   ) asc,\n" +
                        "  ( select cy.bianm from fahb f ,caiyb cy where f.zhilb_id=cy.zhilb_id and f.lie_id in ( select *  from table( split_del(k.lie_id,',') )  )    \n " +
                        "     and f.fahrq=(select  min(f.fahrq) from fahb f,caiyb cy where f.zhilb_id=cy.zhilb_id and f.lie_id in ( select *  from table( split_del(k.lie_id,',') )  )) \n" +
                        "  and rownum=1 \n" +
                        ") asc \n";


        strSQL.append(sql);

        String ArrHeader[][] = new String[4][23];
        ArrHeader[0] = new String[]{"车数", "结算煤量", "单价", "煤款", "煤款", "煤款", "什费", "什费", "运费", "运费", "运费", "其它运杂费", "其它运杂费", "扣超\\亏吨运费", "扣超\\亏吨运费", "拒付金额", "拒付金额", "应付总金额", "应付总金额", "应付总金额", "应付总金额", "应付总金额", "应付总金额"};
        ArrHeader[1] = new String[]{ches, jiesml, danj, meik, meik, meik, shif, shif, yunf, yunf, yunf, qityzf, qityzf, chaod, chaod, jufje, jufje, yingfzje, yingfzje, yingfzje, yingfzje, yingfzje, yingfzje};
        ArrHeader[2] = new String[]{"煤矿单位", "煤矿单位", "发站", "发货日期", "到货日期", "批次号", "基价", "扣水", "煤质", "煤质", "煤质", "煤质", "煤质", "煤质", "车数", "货票数", "验收数", "Qnet,ar<br>折价", "Qnet,ar<br>折金额", "Std<br>折价", "Std<br>折金额", "单价", "金额"};
        ArrHeader[3] = new String[]{"煤矿单位", "煤矿单位", "发站", "发货日期", "到货日期", "批次号", "基价", "扣水", "Stad", "Mt", "Mad", "Vdaf", "Aad", "Qnetar", "车数", "货票数", "验收数", "Qnet,ar<br>折价", "Qnet,ar<br>折金额", "Std<br>折价", "Std<br>折金额", "单价", "金额"};

        int ArrWidth[] = new int[]{60, 60, 50, 80, 80, 90, 40, 40, 40, 40, 40, 40, 40, 50, 50, 60, 60, 60, 60, 60, 60, 40, 80};


        ResultSet rs = cn.getResultSet(strSQL.toString());

        // 数据
        Table tb = new Table(rs, 4, 0, 0);
        rt.setBody(tb);
//		Visit visit = (Visit) getPage().getVisit();
        String type = MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(visit.getDiancxxb_id()), "ZGDT");
        if (type.equals("GD")) {
//			rt.setTitle("国电电力发展股份有限公司燃料结算单批次明细单", ArrWidth);
            rt.setTitle("燃料结算单批次明细单", ArrWidth);
        } else {
            rt.setTitle("大唐公司燃料结算单批次明细单", ArrWidth);
        }
//		rt.setTitle("大唐公司燃料结算单批次明细单", ArrWidth);
        rt.setDefaultTitle(1, 4, "合同单位：" + hetdw, Table.ALIGN_LEFT);
        rt.setDefaultTitle(6, 5, "结算日期：" + jiesrq, Table.ALIGN_LEFT);
        rt.setDefaultTitle(11, 4, "合同：" + het, Table.ALIGN_LEFT);
        rt.setDefaultTitle(16, 4, "编号：" + bianh, Table.ALIGN_LEFT);

        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(18);
        rt.body.setHeaderData(ArrHeader);// 表头数据
        rt.body.setRowHeight(10);

        rt.body.AddTableRow(1);//表格最下面添加一行
//		合并单元格
        rt.body.mergeCell(1, 4, 1, 6);//
        rt.body.mergeCell(1, 7, 1, 8);
        rt.body.mergeCell(2, 4, 2, 6);//
        rt.body.mergeCell(2, 7, 2, 8);
        rt.body.mergeCell(1, 9, 1, 11);
        rt.body.mergeCell(2, 9, 2, 11);
        rt.body.mergeCell(1, 12, 1, 13);
        rt.body.mergeCell(2, 12, 2, 13);
        rt.body.mergeCell(1, 14, 1, 15);
        rt.body.mergeCell(2, 14, 2, 15);
        rt.body.mergeCell(1, 16, 1, 17);
        rt.body.mergeCell(2, 16, 2, 17);
        rt.body.mergeCell(1, 18, 1, 23);
        rt.body.mergeCell(2, 18, 2, 23);

        rt.body.mergeCell(3, 1, 4, 2);
        rt.body.mergeCell(3, 3, 4, 3);
        rt.body.mergeCell(3, 4, 4, 4);
        rt.body.mergeCell(3, 5, 4, 5);//
        rt.body.mergeCell(3, 6, 4, 6);//
        rt.body.mergeCell(3, 7, 4, 7);//
        rt.body.mergeCell(3, 8, 4, 8);
        rt.body.mergeCell(3, 9, 3, 14);
        rt.body.mergeCell(3, 15, 4, 15);
        rt.body.mergeCell(3, 16, 4, 16);
        rt.body.mergeCell(3, 17, 4, 17);
        rt.body.mergeCell(3, 18, 4, 18);
        rt.body.mergeCell(3, 19, 4, 19);
        rt.body.mergeCell(3, 20, 4, 20);
        rt.body.mergeCell(3, 21, 4, 21);
        rt.body.mergeCell(3, 22, 4, 22);
        rt.body.mergeCell(3, 23, 4, 23);

        int row = rt.body.getRows();
        for (int i = 5; i < row - 1; i++) {
            rt.body.mergeCell(i, 1, i, 2);
//			rt.body.mergeCell(i, 3, i, 4);
        }
        rt.body.mergeCell(row - 1, 1, row - 1, 6);
        rt.body.mergeCell(row, 2, row, 19);
//		rt.body.mergeFixedRowCol();

        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_CENTER);
        rt.body.setColAlign(8, Table.ALIGN_CENTER);
        rt.body.setColAlign(9, Table.ALIGN_CENTER);
        rt.body.setColAlign(10, Table.ALIGN_CENTER);
        rt.body.setColAlign(11, Table.ALIGN_CENTER);
        rt.body.setColAlign(12, Table.ALIGN_CENTER);
        rt.body.setColAlign(13, Table.ALIGN_CENTER);
        rt.body.setColAlign(14, Table.ALIGN_CENTER);
        rt.body.setColAlign(15, Table.ALIGN_CENTER);
        rt.body.setColAlign(16, Table.ALIGN_CENTER);
        rt.body.setColAlign(17, Table.ALIGN_CENTER);
        rt.body.setColAlign(18, Table.ALIGN_CENTER);
        rt.body.setColAlign(19, Table.ALIGN_CENTER);
        rt.body.setColAlign(20, Table.ALIGN_CENTER);

        rt.body.setCellValue(row, 1, "备注");
        rt.body.setCellValue(row, 2, fahrq);

        rt.createFooter(1, ArrWidth);

        if (MainGlobal.getXitxx_item("结算", "东胜结算明细样式", "0", "否").equals("是")) {
            rt.setDefautlFooter(1, 4, "供应商复核：", Table.ALIGN_LEFT);
        } else {
            rt.setDefautlFooter(1, 2, "部门负责人：", Table.ALIGN_LEFT);
            rt.setDefautlFooter(7, 2, "审核：", Table.ALIGN_LEFT);
            rt.setDefautlFooter(15, 2, "结算员：", Table.ALIGN_LEFT);
        }

        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        cn.Close();
        return rt.getAllPagesHtml();

    }

    private String getSelectData() {

        JDBCcon con = new JDBCcon();
        boolean Flag = false;
        String sql = " select * from xitxxb where mingc='单批次明细单显示日期和批次' and zhi='是' and leib='结算' and zhuangt=1" +
                " and diancxxb_id=" + ((Visit) this.getPage().getVisit()).getDiancxxb_id();

        ResultSetList rsl = con.getResultSetList(sql);

        if (rsl.getRows() > 0) {//显示批次

            Flag = true;
        }

        rsl.close();
//		if(MainGlobal.getXitxx_item("结算", "临河电厂特殊单批次明细单", "197", "否").equals("是")){
//			return getSelectData_no_pc_jq(con);
//		}
        Visit visit = (Visit) this.getPage().getVisit();
        String jiesbh = visit.getString1().substring(visit.getString1().indexOf(";") + 1);
        ResultSet rs = con.getResultSet("select id from jiesb where bianm='" + jiesbh + "'");
        try {
            if (!rs.next()) {
                return this.getSelectData_yf(con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (Flag) {
            return this.getSelectData_has_pc(con);
        } else {
            return this.getSelectData_no_pc(con);
        }
    }

    public String getTianzdw(long diancxxb_id) {
        String Tianzdw = "";
        JDBCcon con = new JDBCcon();
        try {
            String sql = "select quanc from diancxxb where id=" + diancxxb_id;
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {

                Tianzdw = rs.getString("quanc");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return Tianzdw;
    }

    private String getSelectData_yf(JDBCcon con) {
        // 显示 日期和批次


        _CurrentPage = 1;
        _AllPages = 1;
        Visit visit = (Visit) this.getPage().getVisit();
//		JDBCcon cn = new JDBCcon();
        Report rt = new Report();

        String hetdw = "";
        String jiesrq = "";
        String het = "";
        String bianh = "";

        String ches = "";
        String jiesml = "";
        String danj = "";
        String meik = "";
        String shif = "0.00";
        String yunf = "";
        String qityzf = "0";
        String chaod = "";
        String jufje = "";
        String yingfzje = "";

        String fahrq = "";

        String table = "";
        String table_mk = "";
        String table_yf = "";
        String jiesbh = "";
        String tiaoj = "";

        table = visit.getString1().substring(0, visit.getString1().indexOf(";"));
        table_mk = table.substring(0, table.indexOf(","));
        table_yf = table.substring(table.indexOf(",") + 1);
        jiesbh = visit.getString1().substring(visit.getString1().indexOf(";") + 1);

        String sql = "  select decode(t.kuangb,'小计','小计',rownum) xuh,t.* from (\n" +
                "      select  decode(grouping(m.mingc),1,'小计',m.mingc) kuangb,\n" +
                "      to_char(sum(d.jiessl)) jiessl,\n" +
                "      round(decode(sum(d.jiessl),0,0,sum(d.jiessl*d.jiesdj)/sum(d.jiessl)),2) yunfdj,\n" +
                "      sum(d.zongje) yunfje \n" +
                "      from jiesyfb j inner join danpcjsmxb d on j.id=d.jiesdid\n" +
                "      inner join meikxxb m on d.meikxxb_id=m.id \n" +
                "      where j.bianm='"+jiesbh+"'\n" +
                "      group by rollup (m.mingc) \n" +
                "      ) t\n" +
                "      union all\n" +
                " select '奖励与考核' ,to_char(j.bukyf),'结算总金额',j.hansyf-j.bukyf,0 from jiesyfb j \n" +
                " inner join hetys h on j.hetb_id=h.id where j.bianm='"+jiesbh+"'";

        String tianbdw = getTianzdw(197);//填制单位。（可根据条件来填入单位）
        ResultSet rs = con.getResultSet(sql);
        sql = "select j.shoukdw,h.hetbh,j.beiz from jiesyfb j inner join hetys h on j.hetb_id=h.id where j.bianm='" + jiesbh + "'";
        ResultSet srs = con.getResultSet(sql);
        String jiesdw = "";
        String strHetbh = "";
        String beiz = "";
        String bukyf = "";
        try {
            if (srs.next()) {
                jiesdw = srs.getString("shoukdw");
                strHetbh = srs.getString("hetbh");
                beiz = srs.getString("beiz");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String ArrHeader[][] = new String[4][5];
//		ArrHeader[0]=new String[] {"","","","",""};
		ArrHeader[0]=new String[] {"临河统配煤运费结算单","临河统配煤运费结算单","临河统配煤运费结算单","临河统配煤运费结算单","临河统配煤运费结算单"};
        ArrHeader[1]=new String[] {"","","","",""};
        int ArrWidth[] = new int[]{60, 120, 120, 120, 120};
        ArrHeader[2]=new String[] {"合同编号："+strHetbh,"","","结算单编号："+jiesbh,""};
		ArrHeader[3]=new String[] {"结算单位："+jiesdw,"","","制表日期："+format.format(new Date()),""};
        String ArrH[][] = new String[1][5];
        ArrH[0] = new String[]{"序号", "矿别", "结算数量(吨)", "运费单价(元/吨)", "运费金额(元)"};

        rt.setTitle(new Table(ArrHeader,0,0,0));
        // 数据
        Table tb = new Table(rs, 1, 0, 0);
        rt.setBody(tb);
        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(18);
        rt.body.setHeaderData(ArrH);// 表头数据
        for(int i=1;i<=4;i++){
            for(int j=1;j<=5;j++){
                rt.title.getCell(i,j).setBorderNone();
            }
        }
        rt.title.mergeCell(1,1,1,5);
        rt.title.mergeCell(3,1,3,3);
        rt.title.mergeCell(3,4,3,5);
        rt.title.mergeCell(4,1,4,3);
        rt.title.mergeCell(4,4,4,5);
        rt.title.setBorder(0,0,0,0);
        rt.title.setWidth(new int[]{60, 120, 120, 120, 120});
        rt.title.setCells(1, 1, 1, 5, Table.PER_FONTNAME, "隶书");
        rt.title.setCells(1, 1, 1, 5, Table.PER_FONTSIZE, 18);
        rt.title.setCellAlign(1, 1, Table.ALIGN_CENTER);

        rt.body.setRowHeight(10);
        rt.body.AddTableRow(1);//表格最下面添加一行
        int row = rt.body.getRows();
        rt.body.setCellValue(row, 1, "备注");
        rt.body.setCellValue(row, 2, beiz);
        rt.body.mergeCell(row-2,1,row-2,2);
        rt.body.mergeCell(row,2,row,5);
        rt.createFooter(1, ArrWidth);
        rt.body.setColAlign(1,Table.ALIGN_CENTER);
        rt.body.setColAlign(2,Table.ALIGN_CENTER);
        rt.setDefautlFooter(1, 1, "经理：", Table.ALIGN_LEFT);
        rt.setDefautlFooter(2, 1, "主管经营领导：", Table.ALIGN_LEFT);
        rt.setDefautlFooter(3, 1, "燃料管理部：", Table.ALIGN_LEFT);
        rt.setDefautlFooter(4, 1, "财务审核：", Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 1, "核算：", Table.ALIGN_LEFT);
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
		con.Close();
        return rt.getAllPagesHtml();

    }
//	******************************************************************************


    public SortMode getSort() {
        return SortMode.USER;
    }

    private String _pageLink;

    public boolean getRaw() {
        return true;
    }

    public String getpageLink() {
        if (!_pageLink.equals("")) {
            return _pageLink;
        } else {
            return "";
        }
    }

    // Page方法
    protected void initialize() {
        _msg = "";
        _pageLink = "";
    }


    private String FormatDate(Date _date) {
        if (_date == null) {
            return "";
        }
        return DateUtil.Formatdate("yyyy年MM月dd日", _date);
    }

    //	***************************报表初始设置***************************//
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

    public Date getYesterday(Date dat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public Date getMonthFirstday(Date dat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return cal.getTime();
    }

    protected String getpageLinks() {
        String PageLink = "";
        IRequestCycle cycle = this.getRequestCycle();
        if (cycle.isRewinding())
            return "";
        String _servername = cycle.getRequestContext().getRequest()
                .getServerName();
        String _scheme = cycle.getRequestContext().getRequest().getScheme();
        int _ServerPort = cycle.getRequestContext().getRequest()
                .getServerPort();
        if (_ServerPort != 80) {
            PageLink = _scheme + "://" + _servername + ":" + _ServerPort
                    + this.getEngine().getContextPath();
        } else {
            PageLink = _scheme + "://" + _servername
                    + this.getEngine().getContextPath();
        }
        return PageLink;
    }

}