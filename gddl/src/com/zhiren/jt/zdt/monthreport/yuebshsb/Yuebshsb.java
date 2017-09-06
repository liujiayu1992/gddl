package com.zhiren.jt.zdt.monthreport.yuebshsb;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import com.zhiren.webservice.InterFac_dt;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

/*
 * 作者：zuoh
 * 时间：2010-11-27
 * 描述：实现页面不自动刷新
 */
/*
 * 时间：2009-03-16
 * 作者： ll
 * 修改内容：
 *     修改页面显示sql,增加zhuangt=1的条件
 */
/*
 * 时间：2009-04-07
 * 作者： ll
 * 修改内容：  页面显示填报名称。
 */
/*
 * 时间：2009-04-13
 * 作者： ll
 * 修改内容：  按人员级别修改显示审核和回退提示框内容。
 */
/*
 * 时间：2009-05-6
 * 作者： ll
 * 修改内容：1、替换表名,把yuezbb_zdt改为yuezbb。
 * 		   2、绑定年份、月份下拉框，选择日期后页面可自动刷新
 * 			3、审核数据状态为2时，分公司回退时增加提示框。提示框内容提示“以审核至集团，不能回退。请向集团申请回退！”
 */
/*
 * 时间：2009-05-18
 * 作者： ll
 * 修改内容：修改年份、月份下拉框，选择日期后页面可自动刷新后，没有刷新数据问题。
 *
 */
/*
 * 时间：2009-05-20
 * 作者： sy
 * 修改内容：由于平圩电厂是一厂两制，平圩一电和平圩二电同在一个厂级系统中填报报表数据。
 * 		   当两个电厂填报同一个月报页面时页面出错。所以在beginResponse()中增加了用户级别为电厂级，
 判断登陆电厂与电厂树是否一致，并重新加载刷新页面。
 *
 */
/*
 * 时间：2009-06-1
 * 作者： ll
 * 修改内容：修改审核和回退功能中，年份和月份下拉框获取不到日期的bug。
 *
 */
/*
 * 时间：2009-06-2
 * 作者： ll
 * 修改内容：当分公司审核下属全部电厂时如果有电厂还未上报数据。则分公司审核后，该电厂数据状态仍为未上报状态。
 *
 */
/*
 * 时间：2010-04-30
 * 作者： ll
 * 修改内容：1、增加年度合同执行情况表的审核、上报、回退功能。
 * 			2、增加getHtmlAllAlert()和getzhuangt()两个函数的参数。
 *
 */

/*
 * 时间：2010-07-16
 * 作者：sy
 * 修改内容：1、修改niandhtzxqkb分月审核
 *
 */
public class Yuebshsb extends BasePage implements PageValidateListener {
    // 界面用户提示
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
        ;
    }

    protected void initialize() {
        super.initialize();
        setMsg("");
        setTbmsg(null);
    }

    private String tbmsg;

    public String getTbmsg() {
        return tbmsg;
    }

    public void setTbmsg(String tbmsg) {
        this.tbmsg = tbmsg;
    }

    // 页面变化记录
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }

    private boolean _ShangbClick = false;

    public void ShangbButton(IRequestCycle cycle) {
        _ShangbClick = true;
    }

    private boolean _ShenhClick = false;

    public void ShenhButton(IRequestCycle cycle) {
        _ShenhClick = true;
    }

    private boolean _ReturnClick = false;

    public void ReturnButton(IRequestCycle cycle) {
        _ReturnClick = true;
    }

    private boolean _ReturnDiancClick = false;

    public void ReturnDiancButton(IRequestCycle cycle) {
        _ReturnDiancClick = true;
    }

    public void submit(IRequestCycle cycle) {
        // Visit visit = (Visit) this.getPage().getVisit();

        if (_Refreshclick) {
            _Refreshclick = false;
            setRiq();
            getSelectData();
        }
        if (_ShangbClick) {
            _ShangbClick = false;
            Shenh();
            getSelectData();
        }
        if (_ShenhClick) {
            _ShenhClick = false;
            Shenh();
            getSelectData();
        }
        if (_ReturnClick) {
            _ReturnClick = false;
            Return();
            getSelectData();
        }
        // 集团回退至电厂按钮
        if (_ReturnDiancClick) {
            _ReturnDiancClick = false;
            ReturnDianc();
            getSelectData();
        }

    }

    public boolean Shifht() {
        boolean shifht = false;
        JDBCcon con = new JDBCcon();
        String sql = "select * from (select id,mingc from diancxxb where id=157 or fuid=157 ) where id ="
                + this.getTreeid();
        ResultSetList rl = con.getResultSetList(sql);
        try {
            if (rl.getRows() > 0) {

                shifht = true;
            } else {
                shifht = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }

        return shifht;

    }

    public void Tis() { // 提示信息
        Visit visit = (Visit) getPage().getVisit();
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());

        ArrayList aaa = new ArrayList();

        ArrayList bbb = new ArrayList();

        String[] list1 = null;
        String[] list2 = null;
        while (rsl.next()) {
            if (!rsl.getString("GUANLBMCBZ").equals("")
                    && rsl.getString("GUANLBMCBZ") != null) {
                list1 = rsl.getString("GUANLBMCBZ").split("、");
            }
            if (!rsl.getString("baobmc").equals("")
                    && rsl.getString("baobmc") != null) {
                list2 = rsl.getString("baobmc").split("、");
            }

            for (int i = 0; i < list1.length; i++) {
                if (!aaa.contains(list1[i])) {// 过滤ArrayList中的重复项
                    aaa.add(list1[i]);
                }
            }
            for (int k = 0; k < list2.length; k++) {
                if (!bbb.contains(list2[k])) {// 过滤ArrayList中的重复项
                    bbb.add(list2[k]);
                }
            }

        }

        for (int j = 0; j < aaa.size(); j++) {
            for (int z = 0; z < bbb.size(); z++) {
                if (!aaa.get(j).equals(bbb.get(z))) {
                    continue;
                } else {
                    aaa.remove(j);
                    j = 0;
                }
            }
        }

        for (int j = 0; j < aaa.size(); j++) {
            String ccc = "" + aaa.get(j).toString();
            System.out.println(ccc);
        }

    }

    public void Shenh() { // 审核按钮
        // 工具栏的年份和月份下拉框
        long intyear;
        if (getNianfValue() == null) {
            intyear = DateUtil.getYear(new Date());
        } else {
            intyear = getNianfValue().getId();
        }
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        // 当月份是1的时候显示01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
        // --------------------------------
        String CurrODate = DateUtil.FormatOracleDate(intyear + "-" + StrMonth
                + "-01");
        Date date = new Date();
        String lurysj = DateUtil.FormatOracleTime(date);// 录入时间（自动获得系统时间）

        Visit visit = (Visit) getPage().getVisit();

        // ----------电厂树--------------
        String str = "";
        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {// 选集团时刷新出所有的电厂
            str = "";
        } else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
            str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
                    + getTreeid() + " or dc.shangjgsid = " + getTreeid() + ")";
        } else if (treejib == 3) {// 选电厂只刷新出该电厂
            str = "and dc.id = " + getTreeid() + "";
        }

        // 点击审核改变的状态
        String zhuangt = "";
        if (visit.getRenyjb() == 3) {
            zhuangt = "zhuangt=1";
        } else if (visit.getRenyjb() == 2) {
            zhuangt = " zhuangt=2";
        }
        long id = 0;
        String dc_id = "";
        String zhuangt2 = "";
        String shzt_sql = "";
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        int flag = 0;
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());

        StringBuffer sql = new StringBuffer();
        sql.append("begin \n");
        while (rsl.next()) {
            if (rsl.getString("shujbmc").equals("yuejhbb")) { // 判断审核采购计划录入对应月采购计划表（yuecgjhb）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct cg.diancxxb_id as diancxxb_id,cg.zhuangt as zhuangt  from yuecgjhb cg,diancxxb dc\n"
                            + "where cg.riq=" + CurrODate
                            + " and cg.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yuecgjhb set " + zhuangt
                                    + " where id in ("
                                    + "select  cg.id from yuecgjhb cg,diancxxb dc\n"
                                    + "where cg.diancxxb_id=dc.id " + str
                                    + " and cg.riq=" + CurrODate + ");\n");
                            sql.append("update yuexqjhh set " + zhuangt
                                    + " where id in ("
                                    + "select rcy.id from yuexqjhh rcy,diancxxb dc\n"
                                    + "where rcy.diancxxb_id=dc.id " + str
                                    + " and rcy.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核月计划',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yuecgjhb set " + zhuangt
                            + " where id in ("
                            + "select  cg.id from yuecgjhb cg,diancxxb dc\n"
                            + "where cg.diancxxb_id=dc.id " + str
                            + " and cg.riq=" + CurrODate + ");\n");
                    sql.append("update yuexqjhh set " + zhuangt
                            + " where id in ("
                            + "select rcy.id from yuexqjhh rcy,diancxxb dc\n"
                            + "where rcy.diancxxb_id=dc.id " + str
                            + " and rcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核月计划',''" + ");\n");

                }
            }
            if (rsl.getString("shujbmc").equals("rucycbb")) { // 判断审核入厂油成本对应入厂油成本表（rucycbb）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct rcy.diancxxb_id as diancxxb_id,rcy.zhuangt as zhuangt  from rucycbb rcy,diancxxb dc\n"
                            + "where rcy.riq=" + CurrODate
                            + " and rcy.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update rucycbb set " + zhuangt
                                    + " where id in ("
                                    + "select rcy.id from rucycbb rcy,diancxxb dc\n"
                                    + "where rcy.diancxxb_id=dc.id " + str
                                    + " and rcy.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        // 添加事件信息
                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核rucycbb',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update rucycbb set " + zhuangt
                            + " where id in ("
                            + "select rcy.id from rucycbb rcy,diancxxb dc\n"
                            + "where rcy.diancxxb_id=dc.id " + str
                            + " and rcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));
                    // 添加事件信息
                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核rucycbb',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("yuexqjhh")) { // 判断审核月需求计划录入表（yuexqjhh）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct rcy.diancxxb_id as diancxxb_id,rcy.zhuangt as zhuangt  from yuexqjhh rcy,diancxxb dc\n"
                            + "where rcy.riq=" + CurrODate
                            + " and rcy.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yuexqjhh set " + zhuangt
                                    + " where id in ("
                                    + "select rcy.id from yuexqjhh rcy,diancxxb dc\n"
                                    + "where rcy.diancxxb_id=dc.id " + str
                                    + " and rcy.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                        // 添加事件信息
                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核yuexqjhh',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yuexqjhh set " + zhuangt
                            + " where id in ("
                            + "select rcy.id from yuexqjhh rcy,diancxxb dc\n"
                            + "where rcy.diancxxb_id=dc.id " + str
                            + " and rcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));
                    // 添加事件信息
                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核yuexqjhh',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("yueslb")) { // 判断是否审核cpi燃料管理05表对应月数量表（yueslb）审核状态

                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct kj.diancxxb_id as diancxxb_id,sl.zhuangt as zhuangt  from yueslb sl,yuetjkjb kj,diancxxb dc\n"
                            + "where sl.yuetjkjb_id=kj.id and kj.riq="
                            + CurrODate + " and kj.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yueslb set " + zhuangt2
                                    + " where id in ("
                                    + "select  sl.id from yueslb sl,yuetjkjb yt,diancxxb dc\n"
                                    + "where sl.yuetjkjb_id=yt.id and yt.diancxxb_id="
                                    + dc_id + " and yt.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核yueslb',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yueslb set " + zhuangt
                            + " where id in ("
                            + "select  sl.id from yueslb sl,yuetjkjb yt,diancxxb dc\n"
                            + "where sl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核yueslb',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("yuezbb")) { // 判断是否审核cpi燃料管理08表、cpi燃料管理11表、cpi燃料管理12表对应月指标表（yuezbb）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct zb.diancxxb_id as diancxxb_id,zb.zhuangt as zhuangt  from yuezbb zb,diancxxb dc\n"
                            + "where zb.riq=" + CurrODate
                            + " and zb.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yuezbb set " + zhuangt
                                    + " where id in ("
                                    + "select  zb.id from yuezbb zb,diancxxb dc\n"
                                    + "where zb.diancxxb_id=dc.id " + str
                                    + " and zb.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));

                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核yuezbb',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yuezbb set " + zhuangt
                            + " where id in ("
                            + "select  zb.id from yuezbb zb,diancxxb dc\n"
                            + "where zb.diancxxb_id=dc.id " + str
                            + " and zb.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核yuezbb',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("yueshchjb")) { // 判断是否审核cpi燃料管理06表对应月煤耗存表（yueshchjb）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct shc.diancxxb_id as diancxxb_id,shc.zhuangt as zhuangt  from yueshchjb shc,diancxxb dc\n"
                            + "where shc.riq="
                            + CurrODate
                            + " and shc.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yueshchjb set " + zhuangt
                                    + " where id in ("
                                    + "select  shc.id from yueshchjb shc,diancxxb dc\n"
                                    + "where shc.diancxxb_id=dc.id " + str
                                    + " and shc.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核yueshchjb',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yueshchjb set " + zhuangt
                            + " where id in ("
                            + "select  shc.id from yueshchjb shc,diancxxb dc\n"
                            + "where shc.diancxxb_id=dc.id " + str
                            + " and shc.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核yueshchjb',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("yueshcyb")) { // 判断是否审核cpi燃料管理07表对应月油耗存表（yueshcyb）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct shcy.diancxxb_id as diancxxb_id,shcy.zhuangt as zhuangt  from yueshcyb shcy,diancxxb dc\n"
                            + "where shcy.riq=" + CurrODate
                            + " and shcy.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yueshcyb set " + zhuangt
                                    + " where id in ("
                                    + "select  shcy.id from yueshcyb shcy,diancxxb dc\n"
                                    + "where shcy.diancxxb_id=dc.id " + str
                                    + " and shcy.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));

                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核yueshchjb',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yueshcyb set " + zhuangt
                            + " where id in ("
                            + "select  shcy.id from yueshcyb shcy,diancxxb dc\n"
                            + "where shcy.diancxxb_id=dc.id " + str
                            + " and shcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核yueshchjb',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("yuezlb")) { // 判断是否审核cpi燃料管理09表对应月质量表（yuezlb）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct kj.diancxxb_id as diancxxb_id,zl.zhuangt as zhuangt  from yuezlb zl,yuetjkjb kj,diancxxb dc\n"
                            + "where zl.yuetjkjb_id=kj.id and kj.riq="
                            + CurrODate + " and kj.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yuezlb set " + zhuangt
                                    + " where id in ("
                                    + "select  zl.id from yuezlb zl,yuetjkjb yt,diancxxb dc\n"
                                    + "where zl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                                    + str + " and yt.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核yuezlb',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yuezlb set "
                            + zhuangt
                            + " where id in ("
                            + "select  zl.id from yuezlb zl,yuetjkjb yt,diancxxb dc\n"
                            + "where zl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核yuezlb',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("yuercbmdj")) { // 判断是否审核cpi燃料管理10表对应入厂煤成本表（yuercbmdj）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct kj.diancxxb_id as diancxxb_id,cb.zhuangt as zhuangt  from yuercbmdj cb,yuetjkjb kj,diancxxb dc\n"
                            + "where cb.yuetjkjb_id=kj.id and kj.riq="
                            + CurrODate + " and kj.diancxxb_id=dc.id\n" + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update yuercbmdj set "
                                    + zhuangt
                                    + " where id in ("
                                    + "select  cb.id from yuercbmdj cb,yuetjkjb yt,diancxxb dc\n"
                                    + "where cb.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                                    + str + " and yt.riq=" + CurrODate + ");\n");
                        }
                        id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));

                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update yuercbmdj set "
                            + zhuangt
                            + " where id in ("
                            + "select  cb.id from yuercbmdj cb,yuetjkjb yt,diancxxb dc\n"
                            + "where cb.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核',''" + ");\n");
                }
            }
            if (rsl.getString("shujbmc").equals("niandhtzxqkb")) { // 判断是否审核年度合同执行情况表（niandhtzxqkb）审核状态
                JDBCcon sh = new JDBCcon();
                if (visit.getRenyjb() == 2) {
                    shzt_sql = "select distinct n.diancxxb_id as diancxxb_id,n.zhuangt as zhuangt  from niandhtzxqkb n,diancxxb dc\n"
                            + "where to_char(n.riq,'yyyy-mm-dd')='"
                            + intyear
                            + "-"
                            + StrMonth
                            + "-01' and n.diancxxb_id=dc.id\n"
                            + str;
                    ResultSet shzt = sh.getResultSet(shzt_sql);
                    try {
                        while (shzt.next()) {
                            dc_id = shzt.getString("diancxxb_id");
                            if (shzt.getLong("zhuangt") == 0) {
                                zhuangt2 = "zhuangt=0";
                            } else if (shzt.getLong("zhuangt") == 1) {
                                zhuangt2 = "zhuangt=2";
                            } else if (shzt.getLong("zhuangt") == 2) {
                                zhuangt2 = "zhuangt=2";
                            }
                            sql.append("update niandhtzxqkb set "
                                    + zhuangt
                                    + " where id in ("
                                    + "select  n.id from niandhtzxqkb n,diancxxb dc\n"
                                    + "where n.diancxxb_id=dc.id " + str
                                    + " and to_char(n.riq,'yyyy-mm-dd')='"
                                    + intyear + "-" + StrMonth + "-01');\n");
                        }
                        id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));

                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'审核',''" + ");\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        sh.Close();
                    }
                } else {
                    sql.append("update niandhtzxqkb set " + zhuangt
                            + " where id in ("
                            + "select  n.id from niandhtzxqkb n,diancxxb dc\n"
                            + "where n.diancxxb_id=dc.id " + str
                            + " and to_char(n.riq,'yyyy-mm-dd')='" + intyear
                            + "-" + StrMonth + "-01');\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'审核',''" + ");\n");
                }
            }

        }
        sql.append("end;");
        flag = con.getUpdate(sql.toString());
        if (flag == -1) {
            con.rollBack();
            con.Close();
            WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
            setMsg(ErrorMessage.UpdateDatabaseFail);
            return;
        }
        InterFac_dt sender = new InterFac_dt();
        sender.requestall();
        if (flag != -1) {
            if (visit.getRenyjb() == 2) {
                setMsg("已审核至集团");
            }
            if (visit.getRenyjb() == 3) {
                setMsg("已上报至分公司");
            }
        }
        con.commit();
        con.Close();
    }

    private boolean shenhjb_change = false;// 判断审核状态和登录人员的级别。

    public void Return() {

        // 工具栏的年份和月份下拉框
        long intyear;
        if (getNianfValue() == null) {
            intyear = DateUtil.getYear(new Date());
        } else {
            intyear = getNianfValue().getId();
        }
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        // 当月份是1的时候显示01,
        String StrMonth = "";
        if (intMonth < 10) {
            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
        // --------------------------------
        String CurrODate = DateUtil.FormatOracleDate(intyear + "-" + StrMonth
                + "-01");
        Date date = new Date();
        String lurysj = DateUtil.FormatOracleTime(date);// 录入时间（自动获得系统时间）

        Visit visit = (Visit) getPage().getVisit();
        // ----------电厂树--------------
        String str = "";
        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {// 选集团时刷新出所有的电厂
            str = "";
        } else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
            str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
                    + getTreeid() + " or dc.shangjgsid = " + getTreeid() + ")";
        } else if (treejib == 3) {// 选电厂只刷新出该电厂
            str = "and dc.id = " + getTreeid() + "";
        }

        // 点击审核改变的状态
        String zhuangt = "";
        if (visit.getRenyjb() == 1) { // 判断登陆用户级别是集团用户
            zhuangt = "zhuangt=1";
        } else if (visit.getRenyjb() == 2) {// 判断登陆用户级别是分公司用户
            zhuangt = " zhuangt=0";
        }

        JDBCcon con = new JDBCcon();
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        con.setAutoCommit(false);
        int flag = 0;
        StringBuffer sql = new StringBuffer();
        long id = 0;
        long jb = 0;
        String htzt = "";

        /**
         * 张照奎 2015-4-20
         * 修改集团月计划回退方法，当点击回退之后进行判断，如果是在集团填报的数据只修改状态，如果是通过接口传输到集团的则把集团的数据删除调燃
         * ，并把厂里的状态修改为未审核的状态。
         *
         * */
        String UserName = "";
        String PassWord = "";
        String ENDPOINTADDRESS = "";
        String OperationName = ""; // 上传的方法
        String rejectdata = ""; // 数据传输类型 1电厂上传上来的，0是在集团填报的
        String caozrDcid = visit.getDiancxxb_id() + ""; // 操作人员的电厂ID

        sql.append("begin \n");
        while (rsl.next()) {
            System.out.println("1:" + rsl.getString("shujbmc"));
            if (rsl.getString("shujbmc").equals("yuejhbb")) { // 判断回退采购计划录入对应月采购计划表（yuecgjhb）审核状态

                String dcid = "select distinct diancxxb_id from yuecgjhb ,diancxxb dc where riq = " + CurrODate + "  and diancxxb_id = dc.id " + str;
                System.out.println("2:" + dcid);
                ResultSetList rs_dc = con.getResultSetList(dcid);
                while (rs_dc.next()) {
                    String caozID = visit.getDiancxxb_id() + "";
                    String shujb_id = rs_dc.getString("diancxxb_id");

                    if (visit.getRenyjb() == 1 && treejib == 2) { // 集团回退某一个分公司的数据
                        caozID = (getFGSID(shujb_id));
                    } else if (visit.getRenyID() == 1 && treejib == 1) { // 集团回退整个集团下的月计划
                        caozID = (getFGSID(shujb_id));
                    } else if (visit.getRenyjb() == 2 && treejib == 2) { // 分公司回退整个分公司的数据
                        caozID = getFGSID(shujb_id);
                    } else { // 回退单个电厂的数据

                    }
                    int rollbakczt = rollbackZT(shujb_id, visit.getDiancxxb_id() + "", "yuejhbb", "rollback");// 操作之后下一级别状态
                    String sqlUserInfo = "  select * from RptReject r where r.RptOrgId =  " + shujb_id + " \n"
                            + "	 and oporgid = " + caozID + " \n"
                            + "  and r.reportcode = 'yuejhbb'";

                    System.out.println("3:" + sqlUserInfo + " \n;rollbakczt:" + rollbakczt);
                    ResultSetList rs = con.getResultSetList(sqlUserInfo);
                    if (rs.next()) {
                        UserName = rs.getString("username");
                        PassWord = rs.getString("password");
                        ENDPOINTADDRESS = rs.getString("endpointaddress");
                        rejectdata = rs.getString("rejectdata");
                        OperationName = rs.getString("OPERATIONNAME");
                    }
                    if (ENDPOINTADDRESS.equals("") || ENDPOINTADDRESS == null) {
                        setMsg("该电厂没有入口地址，回退失败。");
                        con.Close();
                        shenhjb_change = false;
                        continue;
                    }
                    if (caozID == "157") {
                        rejectdata = "1";
                    }
                    if (visit.getRenyjb() == 1) {
                        rejectdata = "0";
                    }
                    System.out.println("3,4:" + rejectdata);
                    if (rejectdata.equals("0")) {
                        sql.append(" update ( select * from yuecgjhb where riq = " + CurrODate
                                + " and diancxxb_id = " + shujb_id + "  ) set zhuangt = " + rollbakczt + " ; \n");
                        sql.append(" update ( select * from yuexqjhh where riq = " + CurrODate
                                + " and diancxxb_id = " + shujb_id + "  ) set zhuangt = " + rollbakczt + " ; \n");
                        shenhjb_change = false;
                        System.out.println("4:" + sql.toString());
                    } else {
                        Call call;
                        Service service = new Service();
                        String[] sqls = null;
                        if (caozID == "157") {
                            sqls = new String[]{"begin  \n"
                                    + " update ( select * from yuecgjhb where riq = " + CurrODate
                                    + " 	and diancxxb_id in( select id from diancxxb where fuid in ( " + getTreeid() + " )  ) set zhuangt = 0 ;\n"
                                    + " update ( select * from yuexqjhh where riq = " + CurrODate
                                    + " 	and diancxxb_id in( select id from diancxxb where fuid in ( " + getTreeid() + " )  ) set zhuangt = 0 ;\n "
                                    + " end; "};
                        } else {
                            sqls = new String[]{"begin  \n"
                                    + " update yuedjhfb y set y.zhuangt=0 where  y.niand='" + intyear + "'\n"
                                    + "		and y.yued='" + StrMonth
                                    + "' and (select c.id_jit from changbb c where c.id=y.changbb_id)=" + shujb_id + " ;\n"
                                    + " update yuedjhzb y set y.zhuangt=0 where  y.niand='" + intyear
                                    + "'and y.yued='" + StrMonth + "'\n"
                                    + " 	and (select c.id_jit from changbb c where c.id=y.changbb_id)="
                                    + shujb_id + " ;\n " + " end; "};
                        }
                        System.out.println("5:" + sql.toString());
                        try {
                            call = (Call) service.createCall();
                            call.setTargetEndpointAddress(new java.net.URL(
                                    ENDPOINTADDRESS));
                            call.setOperationName(OperationName);
                            call.addParameter("sqls", XMLType.SOAP_ARRAY,
                                    ParameterMode.IN);
                            call.addParameter("isTransaction",
                                    XMLType.SOAP_BOOLEAN, ParameterMode.IN);
                            call.setReturnType(XMLType.SOAP_ARRAY);
                            System.out.println(sqls[0]);
                            String[] resultArry = (String[]) call
                                    .invoke(new Object[]{sqls,
                                            Boolean.valueOf(true)});// 如果执行成功返回true
//System.out.println("Return:"+resultArry[0] +","+resultArry[1]+","+resultArry[2]+","+resultArry[3]);
                            if (!resultArry[0].equals("true")) {
                                shenhjb_change = true;
                                continue;
                            } else {
                                sql.append(" delete from yuecgjhb where riq = "
                                        + CurrODate + " and diancxxb_id = "
                                        + shujb_id + "  ; \n");
                                sql.append(" delete from yuexqjhh where riq = "
                                        + CurrODate + " and diancxxb_id = "
                                        + shujb_id + "  ; \n");
                                shenhjb_change = false;
                            }
                            System.out.println("6:" + sql.toString());
                        } catch (ServiceException e) {
                            shenhjb_change = true;
                            e.printStackTrace();
                            continue;
                        } catch (MalformedURLException e) {
                            shenhjb_change = true;
                            e.printStackTrace();
                            continue;
                        } catch (RemoteException e) {
                            shenhjb_change = true;
                            e.printStackTrace();
                            continue;
                        }

                    }

                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));
                    // 添加事件信息
                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + shujb_id + ",'" + visit.getRenymc()
                            + "'," + lurysj + "," + CurrODate + ","
                            + rsl.getDouble("id") + "," + "'回退', '"
                            + visit.getRenymc() + "回退月计划'" + ");\n");
                    System.out.println("7:" + sql.toString());
                }

            }
            if (rsl.getString("shujbmc").equals("rucycbb")) { // 判断回退入厂油成本对应入厂油成本表（rucycbb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(rcy.zhuangt) as zhuangt  from rucycbb rcy,diancxxb dc\n"
                        + "where rcy.riq= "
                        + CurrODate
                        + " and rcy.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update rucycbb set " + zhuangt
                            + " where id in ("
                            + "select rcy.id from rucycbb rcy,diancxxb dc\n"
                            + "where rcy.diancxxb_id=dc.id " + str
                            + " and rcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));
                    // 添加事件信息
                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update rucycbb set " + zhuangt
                            + " where id in ("
                            + "select rcy.id from rucycbb rcy,diancxxb dc\n"
                            + "where rcy.diancxxb_id=dc.id " + str
                            + " and rcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));
                    // 添加事件信息
                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }

            if (rsl.getString("shujbmc").equals("yueslb")) { // 判断是否回退cpi燃料管理05表对应月数量表（yueslb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(sl.zhuangt) as zhuangt  from yueslb sl,yuetjkjb kj,diancxxb dc\n"
                        + "where sl.yuetjkjb_id=kj.id and kj.riq="
                        + CurrODate
                        + " and kj.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yueslb set " + zhuangt
                            + " where id in ("
                            + "select  sl.id from yueslb sl,yuetjkjb yt,diancxxb dc\n"
                            + "where sl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update yueslb set "
                            + zhuangt
                            + " where id in ("
                            + "select  sl.id from yueslb sl,yuetjkjb yt,diancxxb dc\n"
                            + "where sl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }

            }
            if (rsl.getString("shujbmc").equals("yuezbb")) { // 判断是否回退cpi燃料管理08表、cpi燃料管理11表、cpi燃料管理12表对应月指标表（yuezbb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(zb.zhuangt) as zhuangt  from yuezbb zb,diancxxb dc\n"
                        + "where zb.riq="
                        + CurrODate
                        + " and zb.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yuezbb set " + zhuangt
                            + " where id in ("
                            + "select  zb.id from yuezbb zb,diancxxb dc\n"
                            + "where zb.diancxxb_id=dc.id " + str
                            + " and zb.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update yuezbb set " + zhuangt
                            + " where id in ("
                            + "select  zb.id from yuezbb zb,diancxxb dc\n"
                            + "where zb.diancxxb_id=dc.id " + str
                            + " and zb.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }

            }
            if (rsl.getString("shujbmc").equals("yueshchjb")) { // 判断是否回退cpi燃料管理06表对应月煤耗存表（yueshchjb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(shc.zhuangt) as zhuangt  from yueshchjb shc,diancxxb dc\n"
                        + "where shc.riq="
                        + CurrODate
                        + " and shc.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yueshchjb set " + zhuangt
                            + " where id in ("
                            + "select  shc.id from yueshchjb shc,diancxxb dc\n"
                            + "where shc.diancxxb_id=dc.id " + str
                            + " and shc.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update yueshchjb set " + zhuangt
                            + " where id in ("
                            + "select  shc.id from yueshchjb shc,diancxxb dc\n"
                            + "where shc.diancxxb_id=dc.id " + str
                            + " and shc.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }

            }
            if (rsl.getString("shujbmc").equals("yueshcyb")) { // 判断是否回退cpi燃料管理07表对应月油耗存表（yueshcyb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(shcy.zhuangt) as zhuangt  from yueshcyb shcy,diancxxb dc\n"
                        + "where shcy.riq="
                        + CurrODate
                        + " and shcy.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yueshcyb set "
                            + zhuangt
                            + " where id in ("
                            + "select  shcy.id from yueshcyb shcy,diancxxb dc\n"
                            + "where shcy.diancxxb_id=dc.id " + str
                            + " and shcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update yueshcyb set "
                            + zhuangt
                            + " where id in ("
                            + "select  shcy.id from yueshcyb shcy,diancxxb dc\n"
                            + "where shcy.diancxxb_id=dc.id " + str
                            + " and shcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }
            if (rsl.getString("shujbmc").equals("yuezlb")) { // 判断是否回退cpi燃料管理09表对应月质量表（yuezlb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(zl.zhuangt) as zhuangt  from yuezlb zl,yuetjkjb kj,diancxxb dc\n"
                        + "where zl.yuetjkjb_id=kj.id and kj.riq="
                        + CurrODate
                        + " and kj.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yuezlb set "
                            + zhuangt
                            + " where id in ("
                            + "select  zl.id from yuezlb zl,yuetjkjb yt,diancxxb dc\n"
                            + "where zl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update yuezlb set "
                            + zhuangt
                            + " where id in ("
                            + "select  zl.id from yuezlb zl,yuetjkjb yt,diancxxb dc\n"
                            + "where zl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }
            if (rsl.getString("shujbmc").equals("yuercbmdj")) { // 判断是否回退cpi燃料管理10表对应入厂煤成本表（yuercbmdj）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(cb.zhuangt) as zhuangt  from yuercbmdj cb,yuetjkjb kj,diancxxb dc\n"
                        + "where cb.yuetjkjb_id=kj.id and kj.riq="
                        + CurrODate
                        + " and kj.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yuercbmdj set "
                            + zhuangt
                            + " where id in ("
                            + "select  cb.id from yuercbmdj cb,yuetjkjb yt,diancxxb dc\n"
                            + "where cb.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update yuercbmdj set "
                            + zhuangt
                            + " where id in ("
                            + "select  cb.id from yuercbmdj cb,yuetjkjb yt,diancxxb dc\n"
                            + "where cb.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }
            if (rsl.getString("shujbmc").equals("niandhtzxqkb")) { // 判断是否回退年度合同执行情况表（niandhtzxqkb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(n.zhuangt) as zhuangt  from niandhtzxqkb n,diancxxb dc\n"
                        + "where to_char(n.riq,'yyyy-mm-dd')='"
                        + intyear
                        + "-"
                        + StrMonth + "-01' and n.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update niandhtzxqkb set " + zhuangt
                            + " where id in ("
                            + "select  n.id from niandhtzxqkb n,diancxxb dc\n"
                            + "where n.diancxxb_id=dc.id " + str
                            + " and to_char(n.riq,'yyyy-mm-dd')='" + intyear
                            + "-" + StrMonth + "-01');\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else if (visit.getRenyjb() == 2 && jb == 1) {
                    shenhjb_change = false;
                    sql.append("update niandhtzxqkb set " + zhuangt
                            + " where id in ("
                            + "select  n.id from niandhtzxqkb n,diancxxb dc\n"
                            + "where n.diancxxb_id=dc.id " + str
                            + " and to_char(n.riq,'yyyy-mm-dd')='" + intyear
                            + "-" + StrMonth + "-01');\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }

        }
        sql.append("end;");
        System.out.println("A:" + shenhjb_change + "," + sql.toString());
        if (shenhjb_change == false) {
            if (Shifht() == true) { // 判断是否是上海电力分公司。
                // 如果是远程回退的话，先更新远程回退状态，再更新本地回退状态。
                InterCom_dt dt = new InterCom_dt();
                String[] sqls = new String[]{sql.toString()};
                String[] answer = dt.sqlExe(getTreeid(), sqls, true);
                if (answer[0].equals("true")) {
                    int num = con.getUpdate(sql.toString());
                    if (num == -1) {
                        setMsg("本地数据回退发生异常！");
                    } else {
                        if (visit.getRenyjb() == 1) {
                            setMsg("已回退至分公司");
                        }
                        if (visit.getRenyjb() == 2) {
                            setMsg("已回退至电厂");
                        }
                    }
                } else {
                    setMsg("对" + getIDropDownDiancmc(getTreeid()) + "回退数据发生异常！");
                }
            } else {
                flag = con.getUpdate(sql.toString());
                if (flag == -1) {
                    con.rollBack();
                    con.Close();
                    WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
                    setMsg(ErrorMessage.UpdateDatabaseFail);
                    return;
                }

                if (flag != -1) {

                    if (visit.getRenyjb() == 1) {
                        setMsg("已回退至分公司");
                    }
                    if (visit.getRenyjb() == 2) {
                        setMsg("已回退至电厂");
                    }
                }
            }
        } else {
            if (visit.getRenyjb() == 1 && jb == 0) {
                setMsg("部分电厂未上报数据，请选择具体电厂回退！");
            } else if (visit.getRenyjb() == 2 && jb == 0) {
                setMsg("部分电厂未上报数据，请选择具体电厂回退！");
            } else if (visit.getRenyjb() == 1 && jb == 1) {
                setMsg("部分电厂未审核数据，请选择具体电厂回退！");
            } else if (visit.getRenyjb() == 2 && jb == 1) {
                setMsg("部分电厂未审核数据，请选择具体电厂回退！");
            } else {
                setMsg("以审核至集团，不能回退。请向集团申请回退！");
            }
        }
        con.commit();
        con.Close();
    }

    public int rollbackZT(String caozDcid, String yewDcid, String yewmc,
                          String caoz) {
        int zhuant = 0;
        JDBCcon con = new JDBCcon();
        String sql = "select * from zhuangtpz  where dcid = " + caozDcid
                + " and caozid = " + yewDcid + " and yewmm = '" + yewmc
                + "' and caozlx = '" + caoz + "' ";
        ResultSetList rs = con.getResultSetList(sql);
        if (rs.next()) {
            zhuant = rs.getInt("houzzt");
        }
        return zhuant;
    }

    // 集团直接回退到电厂
    public void ReturnDianc() {

        // 工具栏的年份和月份下拉框
        long intyear;
        if (getNianfValue() == null) {
            intyear = DateUtil.getYear(new Date());
        } else {
            intyear = getNianfValue().getId();
        }
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        // 当月份是1的时候显示01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
        // --------------------------------
        String CurrODate = DateUtil.FormatOracleDate(intyear + "-" + StrMonth
                + "-01");
        Date date = new Date();
        String lurysj = DateUtil.FormatOracleTime(date);// 录入时间（自动获得系统时间）

        Visit visit = (Visit) getPage().getVisit();
        // ----------电厂树--------------
        String str = "";
        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {// 选集团时刷新出所有的电厂
            str = "";
        } else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂

            str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
                    + getTreeid() + " or dc.shangjgsid = " + getTreeid() + ")";
        } else if (treejib == 3) {// 选电厂只刷新出该电厂
            str = "and dc.id = " + getTreeid() + "";
        }

        // 点击回退至电厂直接回退到电厂
        String zhuangt = "";
        if (visit.getRenyjb() == 1) { // 判断登陆用户级别是集团用户
            zhuangt = " zhuangt=0";
        } else {
            zhuangt = " zhuangt=0";
        }

        JDBCcon con = new JDBCcon();
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        con.setAutoCommit(false);
        int flag = 0;
        StringBuffer sql = new StringBuffer();
        // int result = 0;
        long id = 0;
        long jb = 0;
        String htzt = "";

        sql.append("begin \n");
        while (rsl.next()) {
            // result++;
            if (rsl.getString("shujbmc").equals("yuecgjhb")) { // 判断回退采购计划录入对应月采购计划表（yuecgjhb）审核状态
                if (visit.getRenyjb() == 1) {
                    long qf = 0;
                    long jhid = 0;
                    JDBCcon c = new JDBCcon();
                    String sqlqufen = "select y.id,y.qufen from yuecgjhb y where y.riq="
                            + CurrODate + " and y.diancxxb_id=" + getTreeid();
                    ResultSet shzt = c.getResultSet(sqlqufen);
                    try {
                        while (shzt.next()) {
                            qf = shzt.getLong("qufen");
                            jhid = shzt.getLong("id");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        c.Close();
                    }
                    System.out.println("----------" + qf);
                    System.out.println("----------" + jhid);
                    if (qf == 2) {
                        shenhjb_change = false;
                        // 填报的数据删除
                        JDBCcon co = new JDBCcon();
                        String sqld = "delete from yuecgjhb y where y.id="
                                + jhid;
                        flag = co.getDelete(sqld);
                        if (flag == -1) {
                            con.rollBack();
                            con.Close();
                            WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
                            setMsg(ErrorMessage.UpdateDatabaseFail);
                            return;
                        }
                        // 添加事件信息
                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'回退',''" + ");\n");
                    } else if (qf == 1) {
                        shenhjb_change = false;
                        // 上传的数据解锁
                        String ENDPOINTADDRESS = "";
                        String sqlUserInfo = "  select * from jiekzhb r where r.diancxxb_id =  "
                                + getTreeid();
                        ResultSetList rs = con.getResultSetList(sqlUserInfo);
                        if (rs.next()) {
                            ENDPOINTADDRESS = rs.getString("endpointaddress");
                        }
                        Call call;
                        Service service = new Service();
                        String[] sqls = new String[]{"update yuedjhzb y set y.zhuangt=0 where  y.niand='"
                                + intyear
                                + "'and y.yued='"
                                + StrMonth
                                + "' and (select c.diancxxb_id from changbb c where c.id=y.changbb_id)="
                                + getTreeid()};
                        try {

                            call = (Call) service.createCall();// 远程调用者
                            call.setTargetEndpointAddress(new java.net.URL(
                                    ENDPOINTADDRESS));
                            call.setOperationName("sqlExe");
                            call.addParameter("sqls", XMLType.SOAP_ARRAY,
                                    ParameterMode.IN);
                            call.addParameter("isTransaction",
                                    XMLType.SOAP_BOOLEAN, ParameterMode.IN);
                            call.setReturnType(XMLType.SOAP_ARRAY);
                            String[] resultArry = (String[]) call
                                    .invoke(new Object[]{sqls,
                                            Boolean.valueOf(true)});// 如果执行成功返回true
                            if (!resultArry[0].equals("true")) {
                                con.rollBack();
                                continue;
                            }

                        } catch (ServiceException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        // 添加事件信息
                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'回退',''" + ");\n");
                    } else {
                        shenhjb_change = true;
                    }
                }

            }
            if (rsl.getString("shujbmc").equals("rucycbb")) { // 判断回退入厂油成本对应入厂油成本表（rucycbb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(rcy.zhuangt) as zhuangt  from rucycbb rcy,diancxxb dc\n"
                        + "where rcy.riq="
                        + CurrODate
                        + " and rcy.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update rucycbb set " + zhuangt
                            + " where id in ("
                            + "select rcy.id from rucycbb rcy,diancxxb dc\n"
                            + "where rcy.diancxxb_id=dc.id " + str
                            + " and rcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));
                    // 添加事件信息
                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }
            if (rsl.getString("shujbmc").equals("yuexqjhh")) { // 判断回退月需求计划录入表（yuexqjhh）审核状态

                if (visit.getRenyjb() == 1) {
                    long qf = 0;
                    long jhid = 0;
                    JDBCcon c = new JDBCcon();
                    String sqlqufen = "select y.id,y.qufen from yuexqjhh y where y.riq="
                            + CurrODate + " and y.diancxxb_id=" + getTreeid();
                    ResultSet shzt = c.getResultSet(sqlqufen);
                    try {
                        while (shzt.next()) {
                            qf = shzt.getLong("qufen");
                            jhid = shzt.getLong("id");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        c.Close();
                    }
                    System.out.println("----------" + qf);
                    System.out.println("----------" + jhid);
                    if (qf == 2) {
                        shenhjb_change = false;
                        // 填报的数据删除
                        JDBCcon co = new JDBCcon();
                        String sqld = "delete from yuexqjhh y where y.id="
                                + jhid;
                        flag = co.getDelete(sqld);
                        if (flag == -1) {
                            con.rollBack();
                            con.Close();
                            WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
                            setMsg(ErrorMessage.UpdateDatabaseFail);
                            return;
                        }
                        // 添加事件信息
                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'回退',''" + ");\n");
                    } else if (qf == 1) {
                        shenhjb_change = false;
                        // 上传的数据解锁
                        String ENDPOINTADDRESS = "";
                        String sqlUserInfo = "  select * from jiekzhb r where r.diancxxb_id =  "
                                + getTreeid();
                        ResultSetList rs = con.getResultSetList(sqlUserInfo);
                        if (rs.next()) {
                            ENDPOINTADDRESS = rs.getString("endpointaddress");
                        }
                        Call call;
                        Service service = new Service();
                        String[] sqls = new String[]{"update yuedjhzb y set y.zhuangt=0 where  y.niand='"
                                + intyear
                                + "'and y.yued='"
                                + StrMonth
                                + "' and (select c.diancxxb_id from changbb c where c.id=y.changbb_id)="
                                + getTreeid()};
                        try {

                            call = (Call) service.createCall();// 远程调用者
                            call.setTargetEndpointAddress(new java.net.URL(
                                    ENDPOINTADDRESS));
                            call.setOperationName("sqlExe");
                            call.addParameter("sqls", XMLType.SOAP_ARRAY,
                                    ParameterMode.IN);
                            call.addParameter("isTransaction",
                                    XMLType.SOAP_BOOLEAN, ParameterMode.IN);
                            call.setReturnType(XMLType.SOAP_ARRAY);
                            String[] resultArry = (String[]) call
                                    .invoke(new Object[]{sqls,
                                            Boolean.valueOf(true)});// 如果执行成功返回true
                            if (!resultArry[0].equals("true")) {
                                con.rollBack();
                                continue;
                            }

                        } catch (ServiceException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        // 添加事件信息
                        sql.append("insert into yuebshsjb("
                                + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                                + id + "," + visit.getDiancxxb_id() + ",'"
                                + visit.getRenymc() + "'," + lurysj + ","
                                + CurrODate + "," + rsl.getDouble("id") + ","
                                + "'回退',''" + ");\n");
                    } else {
                        shenhjb_change = true;
                    }

                }

            }
            if (rsl.getString("shujbmc").equals("yueslb")) { // 判断是否回退cpi燃料管理05表对应月数量表（yueslb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(sl.zhuangt) as zhuangt  from yueslb sl,yuetjkjb kj,diancxxb dc\n"
                        + "where sl.yuetjkjb_id=kj.id and kj.riq="
                        + CurrODate
                        + " and kj.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yueslb set "
                            + zhuangt
                            + " where id in ("
                            + "select  sl.id from yueslb sl,yuetjkjb yt,diancxxb dc\n"
                            + "where sl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }

            }
            if (rsl.getString("shujbmc").equals("yuezbb")) { // 判断是否回退cpi燃料管理08表、cpi燃料管理11表、cpi燃料管理12表对应月指标表（yuezbb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(zb.zhuangt) as zhuangt  from yuezbb zb,diancxxb dc\n"
                        + "where zb.riq="
                        + CurrODate
                        + " and zb.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yuezbb set " + zhuangt
                            + " where id in ("
                            + "select  zb.id from yuezbb zb,diancxxb dc\n"
                            + "where zb.diancxxb_id=dc.id " + str
                            + " and zb.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }

            }
            if (rsl.getString("shujbmc").equals("yueshchjb")) { // 判断是否回退cpi燃料管理06表对应月煤耗存表（yueshchjb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(shc.zhuangt) as zhuangt  from yueshchjb shc,diancxxb dc\n"
                        + "where shc.riq="
                        + CurrODate
                        + " and shc.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yueshchjb set " + zhuangt
                            + " where id in ("
                            + "select  shc.id from yueshchjb shc,diancxxb dc\n"
                            + "where shc.diancxxb_id=dc.id " + str
                            + " and shc.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }

            }
            if (rsl.getString("shujbmc").equals("yueshcyb")) { // 判断是否回退cpi燃料管理07表对应月油耗存表（yueshcyb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(shcy.zhuangt) as zhuangt  from yueshcyb shcy,diancxxb dc\n"
                        + "where shcy.riq="
                        + CurrODate
                        + " and shcy.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yueshcyb set "
                            + zhuangt
                            + " where id in ("
                            + "select  shcy.id from yueshcyb shcy,diancxxb dc\n"
                            + "where shcy.diancxxb_id=dc.id " + str
                            + " and shcy.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }
            if (rsl.getString("shujbmc").equals("yuezlb")) { // 判断是否回退cpi燃料管理09表对应月质量表（yuezlb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(zl.zhuangt) as zhuangt  from yuezlb zl,yuetjkjb kj,diancxxb dc\n"
                        + "where zl.yuetjkjb_id=kj.id and kj.riq="
                        + CurrODate
                        + " and kj.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yuezlb set "
                            + zhuangt
                            + " where id in ("
                            + "select  zl.id from yuezlb zl,yuetjkjb yt,diancxxb dc\n"
                            + "where zl.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }
            if (rsl.getString("shujbmc").equals("yuercbmdj")) { // 判断是否回退cpi燃料管理10表对应入厂煤成本表（yuercbmdj）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(cb.zhuangt) as zhuangt  from yuercbmdj cb,yuetjkjb kj,diancxxb dc\n"
                        + "where cb.yuetjkjb_id=kj.id and kj.riq="
                        + CurrODate
                        + " and kj.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update yuercbmdj set "
                            + zhuangt
                            + " where id in ("
                            + "select  cb.id from yuercbmdj cb,yuetjkjb yt,diancxxb dc\n"
                            + "where cb.yuetjkjb_id=yt.id and yt.diancxxb_id=dc.id "
                            + str + " and yt.riq=" + CurrODate + ");\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }
            if (rsl.getString("shujbmc").equals("niandhtzxqkb")) { // 判断是否回退年度合同执行情况表（niandhtzxqkb）审核状态
                JDBCcon c = new JDBCcon();
                htzt = "select min(n.zhuangt) as zhuangt  from niandhtzxqkb n,diancxxb dc\n"
                        + "where to_char(n.riq,'yyyy-mm-dd')='"
                        + intyear
                        + "-"
                        + StrMonth + "-01' and n.diancxxb_id=dc.id\n" + str;
                ResultSet shzt = c.getResultSet(htzt);
                try {
                    while (shzt.next()) {
                        jb = shzt.getLong("zhuangt");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    c.Close();
                }
                if (visit.getRenyjb() == 1 && jb == 2) {
                    shenhjb_change = false;
                    sql.append("update niandhtzxqkb set " + zhuangt
                            + " where id in ("
                            + "select  n.id from niandhtzxqkb n,diancxxb dc\n"
                            + "where n.diancxxb_id=dc.id " + str
                            + " and to_char(n.riq,'yyyy-mm-dd')='" + intyear
                            + "-" + StrMonth + "-01');\n");
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuebshsjb("
                            + "id,diancxxb_id,shijr,riq,xiugrq,yuebbdysjb_id,shij,beiz)values("
                            + id + "," + visit.getDiancxxb_id() + ",'"
                            + visit.getRenymc() + "'," + lurysj + ","
                            + CurrODate + "," + rsl.getDouble("id") + ","
                            + "'回退',''" + ");\n");
                } else {
                    shenhjb_change = true;
                }
            }

        }
        sql.append("end;");
        if (shenhjb_change == false) {
            if (Shifht() == true) {
                // 判断是否是上海电力分公司下属电厂。
                // 如果是远程回退的话，先更新远程回退状态，再更新本地回退状态。
                InterCom_dt dt = new InterCom_dt();
                String[] sqls = new String[]{sql.toString()};
                String[] answer = dt.sqlExe(getTreeid(), sqls, true);
                if (answer[0].equals("true")) {
                    int num = con.getUpdate(sql.toString());
                    if (num == -1) {
                        setMsg("本地数据回退发生异常！");
                    } else {
                        if (visit.getRenyjb() == 1) {
                            setMsg("已回退至分公司");
                        }
                        if (visit.getRenyjb() == 2) {
                            setMsg("已回退至电厂");
                        }
                    }
                } else {
                    setMsg("对" + getIDropDownDiancmc(getTreeid()) + "回退数据发生异常！");
                }
            } else {
                flag = con.getUpdate(sql.toString());
                if (flag == -1) {
                    con.rollBack();
                    con.Close();
                    WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
                    setMsg(ErrorMessage.UpdateDatabaseFail);
                    return;
                }

                if (flag != -1) {
                    if (visit.getRenyjb() == 1) {
                        setMsg("已回退至电厂!");
                    }
                }
            }
        } else {
            if (visit.getRenyjb() == 1 && jb == 0) {
                setMsg("部分电厂未上报数据，请选择具体电厂回退！");
            } else if (visit.getRenyjb() == 1 && jb == 1) {
                setMsg("分公司没有审核数据，请选择具体电厂回退！");
            }
        }
        con.commit();
        con.Close();
    }

    public void getSelectData() {
        ResultSetList rsl = null;
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) getPage().getVisit();

        // 工具栏的年份和月份下拉框
        long intyear;
        if (getNianfValue() == null) {
            intyear = DateUtil.getYear(new Date());
        } else {
            intyear = getNianfValue().getId();
        }
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        // 当月份是1的时候显示01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
        String CurrODate = DateUtil.FormatOracleDate(intyear + "-" + intMonth
                + "-01");
        // -----------------------------------
        // ----------电厂树--------------
        String str = "";

        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {// 选集团时刷新出所有的电厂
            str = "";
        } else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
            str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
                    + getTreeid() + ")";
        } else if (treejib == 3) {// 选电厂只刷新出该电厂
            str = "and dc.id = " + getTreeid() + "";
        }
        // ---------------------------------------------------------------------
        boolean isReture = false;
        if (visit.getRenyjb() == 1) {
            if (treejib == 1) {
                isReture = true;
            }
        } else if (visit.getRenyjb() == 2) {
            if (treejib == 2) {
                isReture = true;
            }
        }
        String context = MainGlobal.getHomeContext(this);
        String chaolj = "";
        String Str = "&'||'diancxxb_id='||" + getTreeid() + "||'&'||'riq='||'"
                + intyear + "-" + StrMonth + "-01'||'&'||'year='||'" + intyear
                + "'||'&'||'lx='||";
        if (treejib == 3) {
            chaolj = "get_Zhuangt(" + getTreeid() + "," + CurrODate + ",'"
                    + intyear + "',sjb.shujbmc) as zhuangt \n";
        } else {
            chaolj = "getHtmlAllAlert('" + MainGlobal.getHomeContext(this)
                    + "','YuebshsbReport','" + Str
                    + "sjb.shujbmc||'',get_Zhuangt(" + getTreeid() + ","
                    + CurrODate + ",'" + intyear
                    + "',sjb.shujbmc)) as zhuangt \n";
        }
        if (rsl == null) {
            String strSql = "select distinct sjb.id as id,sjb.xuh as xuh,sjb.baobmc as baobmc,sjb.shujbmc as shujbmc,\n"
                    + "sjb.shujbmcbz as shujbmcbz,sjb.guanlbmc as guanlbmc,sjb.guanlbmcbz as guanlbmcbz,\n"
                    + chaolj
                    + "from yuebbdysjb sjb\n"
                    + "where sjb.zhuangt=1\n" + "order by xuh";

            rsl = con.getResultSetList(strSql);
        }

        rsl.beforefirst();
        boolean showBtn = false;
        if (rsl.next()) {
            rsl.beforefirst();
            showBtn = false;
        } else {
            showBtn = true;
        }
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        // //设置表名称用于保存
        egu.setTableName("yuebshsjb");
        // /设置显示列名称
        egu.setWidth("bodyWidth");
        egu.addPaging(0);
        egu.setGridType(ExtGridUtil.Gridstyle_Edit);

        egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
        // 设置多选框
        egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
        egu.getColumn(1).setAlign("middle");

        egu.getColumn("xuh").setHeader("序号");
        egu.getColumn("xuh").setWidth(50);
        egu.getColumn("xuh").setHidden(true);
        egu.getColumn("baobmc").setHeader("报表名称");
        egu.getColumn("baobmc").setWidth(300);
        egu.getColumn("baobmc").setEditor(null);
        egu.getColumn("shujbmc").setHeader("数据表");
        egu.getColumn("shujbmc").setWidth(80);
        egu.getColumn("shujbmc").setHidden(true);
        egu.getColumn("shujbmcbz").setHeader("填报名称");
        egu.getColumn("shujbmcbz").setWidth(200);
        egu.getColumn("guanlbmc").setHeader("关联表名称");
        egu.getColumn("guanlbmc").setWidth(150);
        egu.getColumn("guanlbmc").setHidden(true);
        egu.getColumn("guanlbmcbz").setHeader("关联填报名称");
        egu.getColumn("guanlbmcbz").setWidth(200);
        egu.getColumn("guanlbmcbz").setHidden(true);
        egu.getColumn("zhuangt").setHeader("状态");
        egu.getColumn("zhuangt").setWidth(100);
        egu.getColumn("zhuangt").setEditor(null);
        egu.getColumn("zhuangt").setUpdate(false);

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
        egu.addTbarText("-");

        // 电厂树
        egu.addTbarText("单位:");
        DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
                "diancTree", "" + visit.getDiancxxb_id(), "", getTreeid(),
                getTreeid());
        visit.setDefaultTree(dt);
        // ExtTreeUtil etu = new
        // ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
        // setTree(etu);
        egu.addTbarTreeBtn("diancTree");

        egu.addTbarText("-");// 设置分隔符
        // 设定工具栏下拉框自动刷新
        // egu.addOtherScript("NianfDropDown.on('select',function(){document.forms[0].submit();});YuefDropDown.on('select',function(){document.forms[0].submit();});");
        egu.addTbarText("-");// 设置分隔符

        // 刷新按钮
        StringBuffer rsb = new StringBuffer();
        rsb.append("function (){")
                .append(MainGlobal
                        .getExtMessageBox(
                                "'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",
                                true))
                .append("document.getElementById('RefreshButton').click();}");
        GridButton gbr = new GridButton("刷新", rsb.toString());
        gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
        egu.addTbarBtn(gbr);

        // 审核按钮

        if (visit.getRenyjb() == 3) {
            egu.addToolbarButton("上报", GridButton.ButtonType_SubmitSel,
                    "ShangbButton", "", SysConstant.Btn_Icon_SelSubmit);
        }

        // 审核按钮

        if (visit.getRenyjb() == 2) {
            egu.addToolbarButton("审核", GridButton.ButtonType_SubmitSel,
                    "ShenhButton", "", SysConstant.Btn_Icon_SelSubmit);
        }

        // 回退按钮

        if (visit.getRenyjb() == 1) {
            egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,
                    "ReturnButton", "", SysConstant.Btn_Icon_Return);
            egu.addToolbarButton("回退至电厂", GridButton.ButtonType_SubmitSel,
                    "ReturnDiancButton", "", SysConstant.Btn_Icon_Return);

        } else if (visit.getRenyjb() == 2) {
            egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel,
                    "ReturnButton", "", SysConstant.Btn_Icon_Return);

        }

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
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('")
                .append(btnName)
                .append("').click()")
                // -------------------------------------------------------------------
                .append(";Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',")
                .append("width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO}); \n")
                // -------------------------------------------------------------------
                .append("}; // end if \n").append("});}");
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
        if (!visit.getActivePageName().toString()
                .equals(this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            visit.setString1(null);
            visit.setString2(null);
            visit.setString3(null);
            setYuefValue(null);
            setNianfValue(null);
            this.getYuefModels();
            this.getNianfModels();
            this.setTreeid(null);
            setRiq();

        }
        if (visit.getRenyjb() == 3) {
            if (!this.getTreeid().equals(visit.getDiancxxb_id() + "")) {
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                visit.setString1(null);
                visit.setString2(null);
                visit.setString3(null);
                setYuefValue(null);
                setNianfValue(null);
                this.getYuefModels();
                this.getNianfModels();
                this.setTreeid(null);
                setRiq();
            }
        }
        getSelectData();
    }

    public String getNianf() {
        return ((Visit) getPage().getVisit()).getString1();
    }

    public void setNianf(String value) {
        ((Visit) getPage().getVisit()).setString1(value);
    }

    public String getYuef() {
        int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
                .getString3());
        if (intYuef < 10) {
            return "0" + intYuef;
        } else {
            return ((Visit) getPage().getVisit()).getString3();
        }
    }

    public void setYuef(String value) {
        ((Visit) getPage().getVisit()).setString3(value);
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

    public boolean nianfchanged;

    public void setNianfValue(IDropDownBean Value) {
        if (_NianfValue != Value) {
            nianfchanged = true;
        }
        _NianfValue = Value;
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

    public boolean Changeyuef = false;

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
        long id = -2;
        if (_YuefValue != null) {
            id = getYuefValue().getId();
        }
        if (Value != null) {
            if (Value.getId() != id) {
                Changeyuef = true;
            } else {
                Changeyuef = false;
            }
        }
        _YuefValue = Value;

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

    // 电厂树

    // 得到登陆用户所在电厂或者分公司的名称
    public String getDiancmc() {
        String diancmc = "";
        JDBCcon cn = new JDBCcon();
        long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
        String sql_diancmc = "select d.quanc from diancxxb d where d.id="
                + diancid;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                diancmc = rs.getString("quanc");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return diancmc;

    }

    // 得到电厂树下拉框的电厂名称或者分公司,集团的名称
    public String getIDropDownDiancmc(String diancmcId) {
        if (diancmcId == null || diancmcId.equals("")) {
            diancmcId = "1";
        }
        String IDropDownDiancmc = "";
        JDBCcon cn = new JDBCcon();

        String sql_diancmc = "select d.mingc from diancxxb d where d.id="
                + diancmcId;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                IDropDownDiancmc = rs.getString("mingc");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    private String treeid;

    public String getTreeid() {
        String treeid = ((Visit) getPage().getVisit()).getString2();
        if (treeid == null || treeid.equals("")) {
            ((Visit) getPage().getVisit()).setString2(String
                    .valueOf(((Visit) this.getPage().getVisit())
                            .getDiancxxb_id()));
        }
        return ((Visit) getPage().getVisit()).getString2();
    }

    public void setTreeid(String treeid) {
        ((Visit) getPage().getVisit()).setString2(treeid);
    }

    public String getTreeScript() {
        return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
    }

    // public ExtTreeUtil getTree() {
    // return ((Visit) this.getPage().getVisit()).getExtTree1();
    // }
    // public void setTree(ExtTreeUtil etu) {
    // ((Visit) this.getPage().getVisit()).setExtTree1(etu);
    // }
    // public String getTreeHtml() {
    // return getTree().getWindowTreeHtml(this);
    // }
    // public String getTreeScript() {
    // return getTree().getWindowTreeScript();
    // }*/

    // 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
    public int getDiancTreeJib() {
        JDBCcon con = new JDBCcon();
        int jib = -1;
        String DiancTreeJib = this.getTreeid();
        // System.out.println("jib:" + DiancTreeJib);
        if (DiancTreeJib == null || DiancTreeJib.equals("")) {
            DiancTreeJib = "0";
        }
        String sqlJib = "select d.jib from diancxxb d where d.id="
                + DiancTreeJib;
        ResultSet rs = con.getResultSet(sqlJib.toString());

        try {
            while (rs.next()) {
                jib = rs.getInt("jib");
            }
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            con.Close();
        }

        return jib;
    }

    public String getFGSID(String dcid) {
        JDBCcon con = new JDBCcon();
        String sql = "select fuid from diancxxb where id = " + dcid;
        ResultSetList rs = con.getResultSetList(sql);
        String fgs_id = "";
        while (rs.next()) {
            fgs_id = rs.getString("fuid");
        }
        return fgs_id;

    }

}
