package com.zhiren.dc.monthReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：王磊
 * 时间：2009-09-02 18：21
 * 描述：修改修约问题
 */
/*
 * 作者：王磊
 * 时间：2009-08-12 11：43
 * 描述：增加数据锁定判断中对电厂ID的区分
 */
/*
 * 作者：王磊
 * 时间：2009-07-30 14：49
 * 描述：增加数量月报取数的日期设置
 * insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * values(getnewid(232),1,232,'月报取数日期差','-2','','月报',1,'')
 */
/*
 * 2009-05-04
 * 王磊
 * 修改判断如果票重=来煤数量 则运损盈亏都按零计算(不包括大唐国际的数量算法)
 */
/*
 * 作者：王磊
 * 日期：2009-06-29
 * 描述：修改月报生成时的小数位休约为内外两层，增加系统参数控制休约位数。
 * 		不增加系统参数的默认为0
 * 		系统信息表参数(两个)
 * 		mingc   = '月报休约小数位内部'
 * 		zhi 	= 0-9的数字
 * 		zhuangt	= 1
 * 		diancxxb_id = 电厂ID
 *
 * 		mingc   = '月报休约小数位外部'
 * 		zhi 	= 0-9的数字
 * 		zhuangt	= 1
 * 		diancxxb_id = 电厂ID
 *
 *
 */

/* 作者：王刚
 * 日期：2009-09-30
 * 描述：修改生成数据时当有无车皮数据时数据生成错误的问题
 * */
public class Yueslb extends BasePage implements PageValidateListener {
    // 界面用户提示
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
    }

    protected void initialize() {
        super.initialize();
        setMsg("");
    }

    // 页面变化记录
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    private void Save() {
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
        while (rsl.next()) {
            String sql =

                    "select\n" + "ructzl, yingdzje, kuidzje, suopsl, suopje\n"
                            + "from yueslb\n" + "where yuetjkjb_id = "
                            + rsl.getString("yuetjkjb_id") + " and yueslb.fenx = '"
                            + SysConstant.Fenx_Beny + "'";
            ResultSetList rs = con.getResultSetList(sql);
            if (rs.next()) {
                double ructzl = rsl.getDouble("ructzl")
                        - rs.getDouble("ructzl");
                double yingdzje = rsl.getDouble("yingdzje")
                        - rs.getDouble("yingdzje");
                double kuidzje = rsl.getDouble("kuidzje")
                        - rs.getDouble("kuidzje");
                double suopsl = rsl.getDouble("suopsl")
                        - rs.getDouble("suopsl");
                double suopje = rsl.getDouble("suopje")
                        - rs.getDouble("suopje");
                sql = "update yueslb set ructzl = ructzl + " + ructzl + ","
                        + "yingdzje = yingdzje + " + yingdzje + ","
                        + "kuidzje = kuidzje + " + kuidzje + ","
                        + "suopsl = suopsl + " + suopsl + ","
                        + "suopje = suopje + " + suopje
                        + " where yuetjkjb_id =" + rsl.getString("yuetjkjb_id")
                        + " and fenx = '" + SysConstant.Fenx_Leij + "'";
                int flag = con.getUpdate(sql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                            + ErrorMessage.UpdateYueslbFailed + "\nSQL:" + sql);
                    setMsg(ErrorMessage.UpdateYueslbFailed);
                    con.rollBack();
                    con.Close();
                    return;
                }
                sql = "update yueslb set ructzl=" + rsl.getDouble("ructzl")
                        + "," + "yingdzje=" + rsl.getDouble("yingdzje") + ","
                        + "kuidzje=" + rsl.getDouble("kuidzje") + ","
                        + "suopsl=" + rsl.getDouble("suopsl") + "," + "suopje="
                        + rsl.getDouble("suopje") + " where yuetjkjb_id ="
                        + rsl.getString("yuetjkjb_id") + " and fenx = '"
                        + SysConstant.Fenx_Beny + "'";
                flag = con.getUpdate(sql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                            + ErrorMessage.UpdateYueslbFailed + "\nSQL:" + sql);
                    setMsg(ErrorMessage.UpdateYueslbFailed);
                    con.rollBack();
                    con.Close();
                    return;
                }
            }
            rs.close();
        }
        rsl.close();
        con.commit();
        con.Close();
        setMsg(ErrorMessage.SaveSuccessMessage);
    }

    private boolean _SaveClick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveClick = true;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }

    private boolean _CreateClick = false;

    public void CreateButton(IRequestCycle cycle) {
        _CreateClick = true;
    }

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveClick) {
            _SaveClick = false;
            Save();
        }
        if (_Refreshclick) {
            _Refreshclick = false;
            setRiq();
        }

        if (_CreateClick) {
            _CreateClick = false;
            CreateData();
        }
        if (_DelClick) {
            _DelClick = false;
            DelData();
        }
        getSelectData();
    }

    public void DelData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String CurrZnDate = getNianf() + "年" + getYuef() + "月";
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
                + getYuef() + "-01");
        String strSql = "delete from yueslb where yuetjkjb_id in (select id from yuetjkjb where riq="
                + CurrODate
                + " and diancxxb_id="
                + visit.getDiancxxb_id()
                + ")";
        int flag = con.getDelete(strSql);
        if (flag == -1) {
            WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
                    + strSql);
            setMsg("删除过程中发生错误！");
        } else {
            setMsg(CurrZnDate + "的数据被成功删除！");
        }
        con.Close();
    }

    public String getInsertYueslbSql(long id, long yuetjkjb_id, String fenx,
                                     double jingz, double biaoz, double yingd, double kuid, double yuns,
                                     double koud, double kous, double kouz, double koum, double zongkd,
                                     double sanfsl, double jianjsl, double ructzl, double laimsl,
                                     double yingdzje, double kuidzje, double suopsl, double suopje) {
        String sql = "insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,"
                + "yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl,yingdzje,"
                + "kuidzje,suopsl,suopje) values("
                + id
                + ","
                + yuetjkjb_id
                + ",'"
                + fenx
                + "',"
                + jingz
                + ","
                + biaoz
                + ","
                + yingd
                + ","
                + kuid
                + ","
                + yuns
                + ","
                + koud
                + ","
                + kous
                + ","
                + kouz
                + ","
                + koum
                + ","
                + zongkd
                + ","
                + sanfsl
                + ","
                + jianjsl
                + ","
                + ructzl
                + ","
                + laimsl
                + ","
                + yingdzje
                + ","
                + kuidzje
                + "," + suopsl + "," + suopje + ")";
        return sql;
    }

    public void CreateData() {
        long scale_in = 0;
        long scale_out = 0;
        String diancxxb_id = getTreeid();
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        String CurrZnDate = getNianf() + "年" + getYuef() + "月";
        Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
        String CurrODate = DateUtil.FormatOracleDate(cd);
        int intYuef = Integer.parseInt(getYuef());
        String LastODate = DateUtil.FormatOracleDate(DateUtil.AddDate(cd, -1,
                DateUtil.AddType_intMonth));
        String sql = "select zhi from xitxxb where leib = '月报' and mingc ='月报休约小数位内部' and zhuangt = 1 and diancxxb_id="
                + diancxxb_id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            scale_in = rsl.getLong("zhi");
        }
        rsl.close();
        sql = "select zhi from xitxxb where leib = '月报' and mingc ='月报休约小数位外部' and zhuangt = 1 and diancxxb_id="
                + diancxxb_id;
        rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            scale_out = rsl.getLong("zhi");
        }
        rsl.close();
        String date_c = MainGlobal.getXitxx_item("月报", "月报取数日期差", diancxxb_id,
                "0");
        int flag;
        long lngId = 0;
        sql = "insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id)\n"
                + "(select getnewid(t.diancxxb_id) id,"
                + CurrODate
                + " riq,\n"
                + "t.diancxxb_id,rownum xuh,\n"
                + "t.dqid as gongysb_id,t.pinzb_id,t.jihkjb_id,t.yunsfsb_id from\n"
                + "(select f.diancxxb_id,g.dqid,f.pinzb_id,f.jihkjb_id,f.yunsfsb_id\n"
                + " from fahb f, VWGONGYSDQ g\n"
                + "where f.daohrq >= "
                + CurrODate
                + "\n"
                + "and f.daohrq < add_months("
                + CurrODate
                + ",1)\n"
                + "and f.meikxxb_id = g.mk_id and f.diancxxb_id = "
                + diancxxb_id
                + "\n"
                + "union\n"
                + "select f.diancxxb_id,g.dqid,f.pinzb_id,f.jihkjb_id,f.yunsfsb_id\n"
                + " from jiesb j, fahb f, VWGONGYSDQ g\n"
                + "where f.jiesb_id = j.id and f.meikxxb_id = g.mk_id\n"
                + "and j.ruzrq >="
                + CurrODate
                + "\n"
                + "and j.ruzrq <add_months("
                + CurrODate
                + ",1)\n"
                + "and f.diancxxb_id = "
                + diancxxb_id
                + "\n"
                + "union\n"
                + "select y.diancxxb_id,y.gongysb_id,y.pinzb_id,y.jihkjb_id,y.yunsfsb_id\n"
                + "from yuetjkjb y where y.riq = add_months("
                + CurrODate
                + ",-1)\n"
                + "and y.diancxxb_id = "
                + diancxxb_id
                + "\n"
                + "minus\n"
                + "select y.diancxxb_id,y.gongysb_id,y.pinzb_id,y.jihkjb_id,y.yunsfsb_id\n"
                + "from yuetjkjb y where y.riq ="
                + CurrODate
                + "\n"
                + "and y.diancxxb_id = " + diancxxb_id + ") t)";

        flag = con.getInsert(sql);
        if (flag == -1) {
            WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                    + ErrorMessage.InsertYuetjkjFailed + "\nSQL:" + sql);
            setMsg(ErrorMessage.InsertYuetjkjFailed);
            con.rollBack();
            con.Close();
            return;
        }
        sql = "delete from yueslb where yuetjkjb_id in "
                + "(select id from yuetjkjb where riq = " + CurrODate
                + " and diancxxb_id = " + diancxxb_id + ")";
        flag = con.getDelete(sql);
        if (flag == -1) {
            WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                    + ErrorMessage.DeleteYueslbFailed + "\nSQL:" + sql);
            setMsg(ErrorMessage.DeleteYueslbFailed);
            con.rollBack();
            con.Close();
            return;
        }
        sql = "select * from yuetjkjb where riq = " + CurrODate
                + " and diancxxb_id = " + diancxxb_id;
        ResultSetList rs = con.getResultSetList(sql);
        if (rs == null) {
            WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                    + ErrorMessage.NullResult + "引发错误SQL:" + sql);
            setMsg(ErrorMessage.NullResult);
            con.rollBack();
            con.Close();
            return;
        }
        while (rs.next()) {
            lngId = rs.getLong("id");
            long gongysb_id = rs.getLong("gongysb_id");
            long jihkjb_id = rs.getLong("jihkjb_id");
            long pinzb_id = rs.getLong("pinzb_id");
            long yunsfsb_id = rs.getLong("yunsfsb_id");
            double jingz = 0.0, jingz_lj = 0.0;
            double biaoz = 0.0, biaoz_lj = 0.0;
            double yingd = 0.0, yingd_lj = 0.0;
            double kuid = 0.0, kuid_lj = 0.0;
            double yuns = 0.0, yuns_lj = 0.0;
            double koud = 0.0, koud_lj = 0.0;
            double kous = 0.0, kous_lj = 0.0;
            double kouz = 0.0, kouz_lj = 0.0;
            double koum = 0.0, koum_lj = 0.0;
            double zongkd = 0.0, zongkd_lj = 0.0;
            double sanfsl = 0.0, sanfsl_lj = 0.0;
            double jianjsl = 0.0, jianjsl_lj = 0.0;
            double laimsl = 0.0, laimsl_lj = 0.0;
            double yingdsp = 0.0, yingdsp_lj = 0.0;
            double kuidsp = 0.0, kuidsp_lj = 0.0;
            double suopsl = 0.0, suopsl_lj = 0.0;
            double zhejje = 0.0, zhejje_lj = 0.0;
            String type = MainGlobal.getXitxx_item("结算", "结算单所属单位", String
                    .valueOf(diancxxb_id), "ZGDT");
            if (type.equals("JZRD")) {
                sql = "select\n"
                        + "nvl(max(g.xuh), 0) as xuh,\n"
                        + "sum(round(f.laimsl,0)) as jingz,\n"
                        + "sum(round(f.biaoz,0)) as biaoz,\n"
                        + "sum(round(f.yingd,0)) as yingd,\n"
                        + "sum(round(f.biaoz,0)-round(f.laimsl,0)+round(f.yingd,0)-round(f.yuns,0)) as kuid,\n"
                        + "sum(round(f.yuns,0)) as yuns,\n"
                        + "sum(round(f.koud,0)) as koud,\n"
                        + "sum(round(f.kous,0)) as kous,\n"
                        + "sum(round(f.kouz,0)) as kouz,\n"
                        + "sum(round(f.koum,0)) as koum,\n"
                        + "sum(round(f.zongkd,0)) as zongkd,\n"
                        + "sum(round(f.sanfsl,0)) as sanfsl,\n"
                        + "sum(round(c.guohsl,0)) as jianjsl,\n"
                        + "sum(round(f.laimsl,0)) as laimsl,\n"
                        + "round(sum(zonghj*f.jingz)/sum(f.jingz),2)*sum(round(f.yingd,0))  yingdsp,\n"
                        + "round(sum(zonghj*f.jingz)/sum(f.jingz),2)*(sum(round(f.biaoz,0)-round(f.laimsl,0)+round(f.yingd,0)-round(f.yuns,0)))  kuidsp\n"
                        + "from "
                        + "(select g.fahb_id ,(g.meij+g.meis+g.yunf+g.yunfs+g.zaf+g.fazzf+g.ditf)zonghj\n"
                        + "from guslsb g\n" + "where id in(\n"
                        + "select max(guslsb.id)\n" + "from guslsb\n"
                        + "group by guslsb.fahb_id\n" + "))"
                        + " r, fahb f, vwgongys g,\n" + "\n"
                        + "(select fahb_id, sum(maoz - piz) as guohsl\n"
                        + "from chepb\n"
                        + "where jianjfs = '过衡' group by fahb_id ) c\n"
                        + "where f.gongysb_id = g.id\n" + " and g.dqid = "
                        + gongysb_id + " and f.jihkjb_id =" + jihkjb_id
                        + "and f.pinzb_id = " + pinzb_id
                        + " and f.yunsfsb_id =" + yunsfsb_id
                        + " and f.daohrq >= " + CurrODate
                        + "and f.daohrq < add_months(" + CurrODate + ",1)\n"
                        + "and f.diancxxb_id = " + diancxxb_id + "\n"
                        + "and f.id = r.fahb_id(+) and f.id = c.fahb_id(+)";// 增加左连接
            } else {
                String RName = "round_new"; // 修约函数名称
                sql = "select\n"
                        + "nvl(max(g.xuh), 0) as xuh,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.laimsl,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as jingz,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.biaoz,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as biaoz,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(decode(f.biaoz,f.laimsl,0,f.yingd),"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as yingd,\n"
                        + ""
                        + RName
                        + "(sum(decode(f.biaoz,f.laimsl,0,"
                        + RName
                        + "(f.biaoz,"
                        + scale_in
                        + ")"
                        + "-"
                        + RName
                        + "(f.laimsl,"
                        + scale_in
                        + ")+"
                        + RName
                        + "(f.yingd,"
                        + scale_in
                        + ")-"
                        + RName
                        + "(f.yuns,"
                        + scale_in
                        + "))),"
                        + scale_out
                        + ") as kuid,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(decode(f.biaoz,f.laimsl,0,f.yuns),"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as yuns,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.koud,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as koud,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.kous,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as kous,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.kouz,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as kouz,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.koum,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as koum,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.zongkd,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as zongkd,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.sanfsl,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as sanfsl,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(c.guohsl,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as jianjsl,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(f.laimsl,"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ") as laimsl,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(((nvl(r.meij,0) + nvl(r.meijs,0)) * f.yingd),"
                        + scale_in
                        + ")),"
                        + scale_out
                        + ")  yingdsp,\n"
                        + ""
                        + RName
                        + "(sum("
                        + RName
                        + "(((nvl(r.meij,0) + nvl(r.meijs,0) + nvl(r.yunj,0) + nvl(r.yunjs,0))\n"
                        + "* (f.yingd-f.yingk))," + scale_in + ")),"
                        + scale_out + ")  kuidsp\n"
                        + "from ruccb r, fahb f, vwgongys g,\n" + "\n"
                        + "(select fahb_id, sum(maoz - piz) as guohsl\n"
                        + "from chepb\n"
                        + "where jianjfs = '过衡' group by fahb_id ) c\n"
                        + "where f.gongysb_id = g.id\n" + " and g.dqid = "
                        + gongysb_id + " and f.jihkjb_id =" + jihkjb_id
                        + "and f.pinzb_id = " + pinzb_id
                        + " and f.yunsfsb_id =" + yunsfsb_id
                        + " and f.daohrq >= " + CurrODate + "-" + date_c
                        + " \n" + "and f.daohrq < add_months(" + CurrODate
                        + ",1)-" + date_c + " \n" + "and f.diancxxb_id = "
                        + diancxxb_id + "\n"
                        + "and f.ruccbb_id = r.id(+) and f.id = c.fahb_id(+)";// 增加左连接
            }
            ResultSetList datars = con.getResultSetList(sql);
            if (datars.next()) {
                jingz = datars.getDouble("jingz");
                jingz_lj = datars.getDouble("jingz");
                biaoz = datars.getDouble("biaoz");
                biaoz_lj = datars.getDouble("biaoz");
                yingd = datars.getDouble("yingd");
                yingd_lj = datars.getDouble("yingd");
                kuid = datars.getDouble("kuid");
                kuid_lj = datars.getDouble("kuid");
                yuns = datars.getDouble("yuns");
                yuns_lj = datars.getDouble("yuns");
                koud = datars.getDouble("koud");
                koud_lj = datars.getDouble("koud");
                kous = datars.getDouble("kous");
                kous_lj = datars.getDouble("kous");
                kouz = datars.getDouble("kouz");
                kouz_lj = datars.getDouble("kouz");
                koum = datars.getDouble("koum");
                koum_lj = datars.getDouble("koum");
                zongkd = datars.getDouble("zongkd");
                zongkd_lj = datars.getDouble("zongkd");
                sanfsl = datars.getDouble("sanfsl");
                sanfsl_lj = datars.getDouble("sanfsl");
                jianjsl = datars.getDouble("jianjsl");
                jianjsl_lj = datars.getDouble("jianjsl");
                laimsl = datars.getDouble("laimsl");
                laimsl_lj = datars.getDouble("laimsl");
                yingdsp = datars.getDouble("yingdsp");
                yingdsp_lj = datars.getDouble("yingdsp");
                kuidsp = datars.getDouble("kuidsp");
                kuidsp_lj = datars.getDouble("kuidsp");
            }
            datars.close();
            sql = "select distinct\n"
                    + "nvl(sum(abs(s.yingk)),0) suopsl,nvl(sum(abs(s.zhejje)),0) zhejje\n"
                    + "from jiesb j, jieszbsjb s, zhibb z, fahb f, vwgongys g\n"
                    + "where j.id = s.jiesdid(+) and s.zhibb_id = z.id(+)\n"
                    + "and f.jiesb_id = j.id and f.gongysb_id = g.id\n"
                    + "and f.diancxxb_id =" + diancxxb_id + "\n"
                    + "and g.dqid = " + gongysb_id + " and f.jihkjb_id = "
                    + jihkjb_id + "\n" + "and f.yunsfsb_id = " + yunsfsb_id
                    + " and f.pinzb_id = " + pinzb_id + "\n"
                    + "and j.ruzrq >= " + CurrODate + "-" + date_c + "\n"
                    + "and j.ruzrq < add_months(" + CurrODate + ",1) -"
                    + date_c + "\n" + "and z.bianm = '结算数量' and s.yingk < 0";
            datars = con.getResultSetList(sql);
            if (datars.next()) {
                suopsl = rs.getDouble("suopsl");
                suopsl_lj = rs.getDouble("suopsl");
                zhejje = rs.getDouble("zhejje");
                zhejje_lj = rs.getDouble("zhejje");
            }
            datars.close();
            sql = "select sl.* from yuetjkjb tj,yueslb sl  where sl.yuetjkjb_id=tj.id and riq="
                    + LastODate
                    + " and diancxxb_id="
                    + diancxxb_id
                    + "\n"
                    + " and gongysb_id="
                    + gongysb_id
                    + " and pinzb_id="
                    + pinzb_id
                    + " and jihkjb_id="
                    + jihkjb_id
                    + " and yunsfsb_id=" + yunsfsb_id + " and fenx='累计'";
            datars = con.getResultSetList(sql);
            if (datars == null) {
                WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                        + ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
                setMsg(ErrorMessage.NullResult);
                con.rollBack();
                con.Close();
                return;
            }
            if (datars.next() && intYuef != 1) {
                jingz_lj = jingz_lj + datars.getDouble("jingz");
                biaoz_lj = biaoz_lj + datars.getDouble("biaoz");
                yingd_lj = yingd_lj + datars.getDouble("yingd");
                kuid_lj = kuid_lj + datars.getDouble("kuid");
                yuns_lj = yuns_lj + datars.getDouble("yuns");
                koud_lj = koud_lj + datars.getDouble("koud");
                kous_lj = kous_lj + datars.getDouble("kous");
                kouz_lj = kouz_lj + datars.getDouble("kouz");
                koum_lj = koum_lj + datars.getDouble("koum");
                zongkd_lj = zongkd_lj + datars.getDouble("zongkd");
                sanfsl_lj = sanfsl_lj + datars.getDouble("sanfsl");
                jianjsl_lj = jianjsl_lj + datars.getDouble("jianjl");
                laimsl_lj = laimsl_lj + datars.getDouble("laimsl");
                yingdsp_lj = yingdsp_lj + datars.getDouble("YINGDZJE");// yingdsp
                kuidsp_lj = kuidsp_lj + datars.getDouble("KUIDZJE");// kuidsp
                suopsl_lj = suopsl_lj + rs.getDouble("SUOPSL");// suopsl
                zhejje_lj = zhejje_lj + rs.getDouble("SUOPJE");// zhejje
            }

            sql = getInsertYueslbSql(Long.parseLong(MainGlobal.getNewID(Long
                            .parseLong(diancxxb_id))), lngId, SysConstant.Fenx_Beny,
                    jingz, biaoz, yingd, kuid, yuns, koud, kous, kouz, koum,
                    zongkd, sanfsl, jianjsl, 0.0, laimsl, yingdsp, kuidsp,
                    suopsl, zhejje);
            flag = con.getInsert(sql);
            if (flag == -1) {
                WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                        + ErrorMessage.InsertYueslbFailed + "\nSQL:" + sql);
                setMsg(ErrorMessage.InsertYueslbFailed);
                con.rollBack();
                con.Close();
                return;
            }
            sql = getInsertYueslbSql(Long.parseLong(MainGlobal.getNewID(Long
                            .parseLong(diancxxb_id))), lngId, SysConstant.Fenx_Leij,
                    jingz_lj, biaoz_lj, yingd_lj, kuid_lj, yuns_lj, koud_lj,
                    kous_lj, kouz_lj, koum_lj, zongkd_lj, sanfsl_lj,
                    jianjsl_lj, 0.0, laimsl_lj, yingdsp_lj, kuidsp_lj,
                    suopsl_lj, zhejje_lj);
            flag = con.getInsert(sql);
            if (flag == -1) {
                WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                        + ErrorMessage.InsertYueslbFailed + "\nSQL:" + sql);
                setMsg(ErrorMessage.InsertYueslbFailed);
                con.rollBack();
                con.Close();
                return;
            }
        }
        rs.close();
        if (intYuef != 1) {// 取上个月有这个月没有的数据，本月值为0，累计值等与上个月累计
            sql = "select * from (select distinct y.id,y.gongysb_id,y.jihkjb_id,y.pinzb_id,\n"
                    + "y.yunsfsb_id,nvl(s.yuetjkjb_id,-1) yuetjkj\n"
                    + "from yuetjkjb y, yueslb s\n"
                    + "where y.id = s.yuetjkjb_id(+)\n"
                    + "and s.fenx(+) = '"
                    + SysConstant.Fenx_Beny
                    + "'\n"
                    + "and y.riq = "
                    + CurrODate
                    + "\n"
                    + "and y.diancxxb_id = "
                    + diancxxb_id
                    + ")\n" + "where yuetjkj = -1";
            ResultSetList rssylj = con.getResultSetList(sql);
            if (rssylj == null) {
                WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                        + ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
                setMsg(ErrorMessage.NullResult);
                con.rollBack();
                con.Close();
                return;
            }
            while (rssylj.next()) {
                sql = "select y.id yid,s.* from yuetjkjb y,yueslb s where y.id = s.yuetjkjb_id(+) "
                        + " and s.fenx(+) = '"
                        + SysConstant.Fenx_Leij
                        + "'"
                        + " and y.riq="
                        + LastODate
                        + " and y.diancxxb_id="
                        + diancxxb_id
                        + "\n"
                        + " and y.gongysb_id="
                        + rssylj.getLong("gongysb_id")
                        + " and y.pinzb_id="
                        + rssylj.getLong("pinzb_id")
                        + " and y.jihkjb_id="
                        + rssylj.getLong("jihkjb_id")
                        + " and y.yunsfsb_id="
                        + rssylj.getLong("yunsfsb_id");
                ResultSetList recby = con.getResultSetList(sql);
                if (recby == null) {
                    WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                            + ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
                    setMsg(ErrorMessage.NullResult);
                    con.rollBack();
                    con.Close();
                    return;
                }
                if (recby.next()) {
                    sql = getInsertYueslbSql(Long.parseLong(MainGlobal
                                    .getNewID(Long.parseLong(diancxxb_id))), rssylj
                                    .getLong("yid"), SysConstant.Fenx_Beny, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, 0, 0, 0.0, 0, 0, 0, 0, 0);
                    flag = con.getInsert(sql);
                    if (flag == -1) {
                        WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                                + ErrorMessage.InsertYueslbFailed + "\nSQL:"
                                + sql);
                        setMsg(ErrorMessage.InsertYueslbFailed);
                        con.rollBack();
                        con.Close();
                        return;
                    }
                    sql = getInsertYueslbSql(Long.parseLong(MainGlobal
                                    .getNewID(Long.parseLong(diancxxb_id))), rssylj
                                    .getLong("yid"), SysConstant.Fenx_Leij, recby
                                    .getDouble("jingz"), recby.getDouble("biaoz"),
                            recby.getDouble("yingd"), recby.getDouble("kuid"),
                            recby.getDouble("yuns"), recby.getDouble("koud"),
                            recby.getDouble("kous"), recby.getDouble("kouz"),
                            recby.getDouble("koum"), recby.getDouble("zongkd"),
                            recby.getDouble("sanfsl"), recby
                                    .getDouble("jianjl"), 0.0, recby
                                    .getDouble("laimsl"), recby
                                    .getDouble("yingdsp"), recby
                                    .getDouble("kuidsp"), recby
                                    .getDouble("suopsl"), recby
                                    .getDouble("zhejje"));
                    flag = con.getInsert(sql);
                    if (flag == -1) {
                        WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                                + ErrorMessage.InsertYueslbFailed + "\nSQL:"
                                + sql);
                        setMsg(ErrorMessage.InsertYueslbFailed);
                        con.rollBack();
                        con.Close();
                        return;
                    }
                }
            }
        }
        con.commit();
        con.Close();
        setMsg(CurrZnDate + "的数据成功生成！");
    }

    public void getSelectData() {
        String diancxxb_id = getTreeid();
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
                + getYuef() + "-01");
        String sql = "select s.id, t.id yuetjkjb_id, g.mingc gongysb_id, j.mingc jihkjb_id, p.mingc pinzb_id,\n"
                + "y.mingc yunsfsb_id, s.jingz, s.biaoz, s.yingd, s.kuid, s.yuns, s.zongkd,\n"
                + "s.jianjl, s.ructzl, s.yingdzje, s.kuidzje, s.suopsl, s.suopje\n"
                + "from yueslb s, yuetjkjb t, vwgongys g, pinzb p, yunsfsb y, jihkjb j\n"
                + "where t.id = s.yuetjkjb_id and t.gongysb_id = g.id\n"
                + "and t.pinzb_id = p.id and t.jihkjb_id = j.id\n"
                + "and t.yunsfsb_id = y.id and t.diancxxb_id = "
                + diancxxb_id
                + "\n"
                + "and t.riq = "
                + CurrODate
                + " and s.fenx ='"
                + SysConstant.Fenx_Beny + "'";
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl == null) {
            WriteLog.writeErrorLog(this.getClass().getName() + "\n"
                    + ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
            setMsg(ErrorMessage.NullResult);
            return;
        }
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        // //设置表名称用于保存
        egu.setTableName("yueslb");
        // /设置显示列名称
        egu.setWidth("bodyWidth");
        // egu.setHeight("bodyHeight");
        egu.addPaging(0);
        egu.setGridType(ExtGridUtil.Gridstyle_Edit);
        // egu.getColumn("xuh").setHeader("序号");
        // egu.getColumn("xuh").setWidth(50);
        egu.getColumn("yuetjkjb_id").setHidden(true);
        egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
        egu.getColumn("gongysb_id").setWidth(120);
        egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
        egu.getColumn("jihkjb_id").setWidth(60);
        egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
        egu.getColumn("pinzb_id").setWidth(40);
        egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
        egu.getColumn("yunsfsb_id").setWidth(60);
        egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
        egu.getColumn("jingz").setWidth(60);
        egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
        egu.getColumn("biaoz").setWidth(60);
        egu.getColumn("yingd").setHeader(Locale.yingd_fahb);
        egu.getColumn("yingd").setWidth(60);
        egu.getColumn("kuid").setHeader(Locale.kuid_fahb);
        egu.getColumn("kuid").setWidth(60);
        egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
        egu.getColumn("yuns").setWidth(60);
        egu.getColumn("zongkd").setHeader(Locale.zongkd_fahb);
        egu.getColumn("zongkd").setWidth(60);
        egu.getColumn("jianjl").setHeader(Locale.MRtp_jianjl);
        egu.getColumn("jianjl").setWidth(60);
        egu.getColumn("ructzl").setHeader(Locale.MRtp_ructzl);
        egu.getColumn("ructzl").setWidth(90);
        egu.getColumn("yingdzje").setHeader(Locale.MRtp_yingdzje);
        egu.getColumn("yingdzje").setWidth(90);
        egu.getColumn("kuidzje").setHeader(Locale.MRtp_kuidzje);
        egu.getColumn("kuidzje").setWidth(90);
        egu.getColumn("suopsl").setHeader(Locale.MRtp_suopsl);
        egu.getColumn("suopsl").setWidth(80);
        egu.getColumn("suopje").setHeader(Locale.MRtp_suopje);
        egu.getColumn("suopje").setWidth(80);

        egu.getColumn("gongysb_id").setEditor(null);
        egu.getColumn("jihkjb_id").setEditor(null);
        egu.getColumn("pinzb_id").setEditor(null);
        egu.getColumn("yunsfsb_id").setEditor(null);
        egu.getColumn("jingz").setEditor(null);
        egu.getColumn("biaoz").setEditor(null);
        egu.getColumn("yingd").setEditor(null);
        egu.getColumn("kuid").setEditor(null);
        egu.getColumn("yuns").setEditor(null);
        egu.getColumn("zongkd").setEditor(null);
        egu.getColumn("jianjl").setEditor(null);

        String Sql = "select x.zhi from xitxxb x where x.leib='月报' and x.danw='数量' and x.beiz='使用'";
        ResultSetList rs = con.getResultSetList(Sql);
        if (rs != null) {
            while (rs.next()) {
                String zhi = rs.getString("zhi");
                if (egu.getColByHeader(zhi) != null) {
                    egu.getColByHeader(zhi).hidden = true;
                }
            }
        }
        egu.setDefaultsortable(false);

        egu.getColumn("gongysb_id").update = false;
        egu.getColumn("jihkjb_id").update = false;
        egu.getColumn("pinzb_id").update = false;
        egu.getColumn("yunsfsb_id").update = false;

		/*
		 * StringBuffer sb = new StringBuffer(); sb
		 * .append("gridDiv_grid.on('afteredit',function(e){ \n")
		 * .append("if(e.field=='RUCTZL'){ \n") .append( "\t chv =
		 * eval(e.value||0) - eval(e.originalValue||0); \n") .append("\t recs =
		 * new Array();\n") .append("\t recs[0] = gridDiv_ds.getAt(0);\n")
		 * .append("\t recs[1] = gridDiv_ds.getAt(1);\n") .append("\t recs[2] =
		 * gridDiv_ds.getAt(e.row+1);\n") .append("for(m = 0;m <
		 * recs.length;m++){") .append( "\t recs[m].set('RUCTZL',
		 * eval(recs[m].get('RUCTZL')||0) + chv);") .append("}//end for
		 * \n").append( "}//end if \n});//end afteredit\n ");
		 *
		 * sb.append("gridDiv_grid.on('beforeedit',function(e){");
		 * sb.append("if(e.record.get('FENX')=='累计'){e.cancel=true;}");//
		 * 当"累计"时,这一行不允许编辑 sb.append("});");
		 *
		 * sb.append("gridDiv_grid.on('beforeedit',function(e){");
		 * sb.append("if(e.record.get('GONGYSB_ID')=='总计'){e.cancel=true;}");//
		 * 当电厂列的值是"合计"时,这一行不允许编辑 sb.append(" if(e.field=='GONGYSB_ID'){
		 * e.cancel=true;}");// 电厂列不允许编辑 sb.append("});");
		 *  // 设定合计列不保存 sb .append("function
		 * gridDiv_save(record){if(record.get('gongysb_id')=='总计') return
		 * 'continue';}");
		 *
		 * egu.addOtherScript(sb.toString());
		 */
        // /是否返回下拉框的值或ID
        // egu.getColumn("").setReturnId(true);
        // egu.getColumn("").setReturnId(true);
        // /设置按钮
        egu.addTbarText("年份");
        ComboBox comb1 = new ComboBox();
        comb1.setWidth(60);
        comb1.setTransform("NianfDropDown");
        comb1.setId("NianfDropDown");// 和自动刷新绑定
        comb1.setLazyRender(true);// 动态绑定
        comb1.setEditable(true);
        egu.addToolbarItem(comb1.getScript());

        egu.addTbarText("月份");
        ComboBox comb2 = new ComboBox();
        comb2.setWidth(50);
        comb2.setTransform("YuefDropDown");
        comb2.setId("YuefDropDown");// 和自动刷新绑定
        comb2.setLazyRender(true);// 动态绑定
        comb2.setEditable(true);
        egu.addToolbarItem(comb2.getScript());

        egu.addTbarText("-");// 设置分隔符
        // 设置树
        egu.addTbarText("单位:");
        ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
                .getVisit()).getDiancxxb_id(), getTreeid());
        setTree(etu);
        egu.addTbarTreeBtn("diancTree");
        egu.addTbarText("-");// 设置分隔符

        // 判断数据是否被锁定
        boolean isLocked = isLocked(con);
        // 刷新按钮
        StringBuffer rsb = new StringBuffer();
        rsb
                .append("function (){")
                .append(
                        MainGlobal
                                .getExtMessageBox(
                                        "'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",
                                        true)).append(
                "document.getElementById('RefreshButton').click();}");
        GridButton gbr = new GridButton("刷新", rsb.toString());
        gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
        egu.addTbarBtn(gbr);
        // 生成按钮
        GridButton gbc = new GridButton("生成",
                getBtnHandlerScript("CreateButton"));
        if (isLocked) {
            gbc
                    .setHandler("function (){"
                            + MainGlobal.getExtMessageBox(
                            ErrorMessage.DataLocked_Yueslb, false)
                            + "return;}");
        }
        gbc.setIcon(SysConstant.Btn_Icon_Create);
        egu.addTbarBtn(gbc);
        // 删除按钮
        GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
        if (isLocked) {
            gbd
                    .setHandler("function (){"
                            + MainGlobal.getExtMessageBox(
                            ErrorMessage.DataLocked_Yueslb, false)
                            + "return;}");
        }
        gbd.setIcon(SysConstant.Btn_Icon_Delete);
        egu.addTbarBtn(gbd);
        // 保存按钮
        GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
                egu.getGridColumns(), "SaveButton");
        if (isLocked) {
            gbs
                    .setHandler("function (){"
                            + MainGlobal.getExtMessageBox(
                            ErrorMessage.DataLocked_Yueslb, false)
                            + "return;}");
        }
        egu.addTbarBtn(gbs);
        // 打印按钮
        GridButton gbp = new GridButton("打印", "function (){"
                + MainGlobal.getOpenWinScript("MonthReport&lx=yueslb") + "}");
        gbp.setIcon(SysConstant.Btn_Icon_Print);
        egu.addTbarBtn(gbp);

        setExtGrid(egu);
        con.Close();
    }

    public String getBtnHandlerScript(String btnName) {
        // 按钮的script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = getNianf() + "年" + getYuef() + "月";
        btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
        } else {
            btnsb.append("是否删除").append(cnDate).append("的数据？");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){").append(
                "document.getElementById('").append(btnName).append(
                "').click()").append("}; // end if \n").append("});}");
        return btnsb.toString();
    }

    public ExtGridUtil getExtGrid() {
        return ((Visit) this.getPage().getVisit()).getExtGrid1();
    }

    public void setExtGrid(ExtGridUtil extgrid) {
        ((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
    }

    public String getGridScript() {
        if (getExtGrid() == null) {
            return "";
        }
        return getExtGrid().getGridScript();
    }

    public String getGridHtml() {
        if (getExtGrid() == null) {
            return "";
        }
        return getExtGrid().getHtml();
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

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            setRiq();
            setTreeid(String.valueOf(visit.getDiancxxb_id()));
            getSelectData();
        }
    }

    public boolean isLocked(JDBCcon con) {
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
                + getYuef() + "-01");
        return con.getHasIt("select * from yueshchjb where riq=" + CurrODate
                + " and diancxxb_id =" + getTreeid());
    }

    public String getNianf() {
        return ((Visit) getPage().getVisit()).getString1();
    }

    public void setNianf(String value) {
        ((Visit) getPage().getVisit()).setString1(value);
    }

    public String getYuef() {
        int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
                .getString2());
        if (intYuef < 10) {
            return "0" + intYuef;
        } else {
            return ((Visit) getPage().getVisit()).getString2();
        }
    }

    public void setYuef(String value) {
        ((Visit) getPage().getVisit()).setString2(value);
    }

    public void setRiq() {
        setNianf(getNianfValue().getValue());
        setYuef(getYuefValue().getValue());
    }

    // 年份下拉框
    private static IPropertySelectionModel _NianfModel;

    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }

    private IDropDownBean _NianfValue;

    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }

    public void setNianfValue(IDropDownBean Value) {
        if (_NianfValue != Value) {
            _NianfValue = Value;
        }
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

    // 月份下拉框
    private static IPropertySelectionModel _YuefModel;

    public IPropertySelectionModel getYuefModel() {
        if (_YuefModel == null) {
            getYuefModels();
        }
        return _YuefModel;
    }

    private IDropDownBean _YuefValue;

    public IDropDownBean getYuefValue() {
        if (_YuefValue == null) {
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _yuef = 12;
            } else {
                _yuef = _yuef - 1;
            }
            for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
                Object obj = getYuefModel().getOption(i);
                if (_yuef == ((IDropDownBean) obj).getId()) {
                    _YuefValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _YuefValue;
    }

    public void setYuefValue(IDropDownBean Value) {
        if (_YuefValue != Value) {
            _YuefValue = Value;
        }
    }

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }

    public String getTreeid() {
        String treeid = ((Visit) getPage().getVisit()).getString3();
        if (treeid == null || treeid.equals("")) {
            ((Visit) getPage().getVisit()).setString3(String
                    .valueOf(((Visit) this.getPage().getVisit())
                            .getDiancxxb_id()));
        }
        return ((Visit) getPage().getVisit()).getString3();
    }

    public void setTreeid(String treeid) {
        ((Visit) getPage().getVisit()).setString3(treeid);
    }

    public ExtTreeUtil getTree() {
        return ((Visit) this.getPage().getVisit()).getExtTree1();
    }

    public void setTree(ExtTreeUtil etu) {
        ((Visit) this.getPage().getVisit()).setExtTree1(etu);
    }

    public String getTreeHtml() {
        return getTree().getWindowTreeHtml(this);
    }

    public String getTreeScript() {
        return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
    }

    public String getTreedcScript() {
        return getTree().getWindowTreeScript();
    }
}
