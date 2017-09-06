package com.zhiren.dc.jilgl.shujdr;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.DateUtil;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.webservice.QichsjppInterface;

/*作者:王总兵
 *日期:2010-3-5 14:25:16
 *描述 1.汇总页面中的供货单位下拉框增加过滤,不显示煤矿地区
 *
 *
 */

/*
 * 作者：王总兵
 * 时间：2009-12-15
 * 描述：1、点删除按钮,弹出的对话框中的下列框内容页面没有初始化,我在
 *         beginresponse里给其初始化
 *
 */
/*
 * 作者：张乐
 * 时间：2009-11-20
 * 描述：1、取消分页设置
 * 		2、一厂多制条件下，增加厂别的下拉框，按照选择厂别进行数据刷新
 * 		3、增加合计行显示
 */
/*
 * 作者：王磊
 * 时间：2009-10-20
 * 描述：修改piaojh字段插入时按varchar2类型插入 sql中加''
 */
/*
 * 作者：王磊
 * 时间：2009-10-16
 * 描述：修改保存时JDBC连接未释放的问题。
 */
/*
 * 作者：王总兵
 * 时间：2009-09-27 10：04
 * 描述：增加刷新和保存方法时宣威电厂的参数设置
 */
/*
 * 作者：王磊
 * 时间：2009-08-26 10：04
 * 描述：增加导入时卸车方式的处理
 */
/*
 * 作者：王磊
 * 时间：2009-08-22 17：23
 * 描述：增加净重（maoz - piz - koud - kous - kouz）字段，默认不显示；
 * 如需显示请调整Xitgnsz 关键字为Shujdrhz
 */
/*
 * 修改人:tzf
 * 时间:2009-06-16
 * 内容:汽车导入增加删除逻辑功能
 */
/*
 * 2009-02-20
 * 王磊
 * 修改分厂别时数据过滤出现错误的问题
 */
/*
 * 2009-05-16
 * 王磊
 * 增加未匹配数据提示,增加系统默认日期的配置
 */
/*
 * 2009-05-17
 * 王磊
 * 增加页面初始化时fahbtmp_id 的设置防止存储车皮临时表的错误
 */
/*
 * 作者：王磊
 * 时间：2009-05-27
 * 描述：增加处理原毛重、原皮重的信息，增加参数标识为导入运单
 */
/*
 * 作者：王磊
 * 时间：2009-06-13 11：04
 * 描述：修改车皮TMP表chepbtmp向车皮临时表(cheplsb)中写值时车皮临时表ID等于车皮TMP表ID
 */
/*
 * 作者：王磊
 * 时间：2009-06-16 15：34
 * 描述：修改导入数据时电厂ID 从tmp表中取得
 */
/*
 * 作者：张森涛
 * 时间：2010-01-19
 * 描述：增加煤矿是否有合同进煤控制
 */
/*
 * 作者：刘雨
 * 时间：2010-08-26
 * 描述：增加导入时供应商、煤矿与电厂匹配自动保存电厂的控制
 */

/*
 * 作者：陈环红
 * 时间：2017-08-26
 * 描述：入界面加载时自动更新没有匹配的供应商，煤矿没有匹配时，提示用户煤矿没有匹配。
 */
public class Shujdrhz extends BasePage implements PageValidateListener {

    private static final String customKey = "Shujdrhz";
    public static final String QY = "QY";// 汽运

    public static final String HY = "HY";// 火运

    public static final String DRYD = "DRYD";//导入运单

    public IDropDownBean getFahrqValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
            if (getFahrqModel().getOptionCount() > 0) {
                setFahrqValue((IDropDownBean) getFahrqModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean1();
    }

    public void setFahrqValue(IDropDownBean value) {
        ((Visit) this.getPage().getVisit()).setDropDownBean1(value);
    }

    public IPropertySelectionModel getFahrqModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
            setFahrqModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
    }

    public void setFahrqModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
    }

    public void setFahrqModels() {
        Visit visit = (Visit) this.getPage().getVisit();
        String sql_yunsfs = "";

        if (HY.equals(getLeix())) {
            sql_yunsfs = " and yunsfsb_id = " + SysConstant.YUNSFS_HUOY;
        } else if (HY.equals(getLeix())) {
            sql_yunsfs = " and yunsfsb_id = " + SysConstant.YUNSFS_QIY;
        }
        String sql = "select rownum xuh,fahrq from (select distinct to_char(fahrq,'yyyy-mm-dd') fahrq from fahb where diancxxb_id = "
                + visit.getDiancxxb_id()
                + " and hedbz = "
                + SysConstant.HEDBZ_YJJ + sql_yunsfs + " order by fahrq) ";
        setFahrqModel(new IDropDownModel(sql));
    }

    public String getMod() {
        return ((Visit) this.getPage().getVisit()).getString3();
    }

    public void setMod(String mod) {
        ((Visit) this.getPage().getVisit()).setString3(mod);
    }

    public String getLeix() {
        return ((Visit) this.getPage().getVisit()).getString2();
    }

    public void setLeix(String leix) {
        ((Visit) this.getPage().getVisit()).setString2(leix);
    }

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

    // 保存数据list
    public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }

    // 保存页面Bean
    // private GongsBean editValue;
    // public GongsBean getEditValue() {
    // return editValue;
    // }
    // public void setEditValue(GongsBean editValue) {
    // this.editValue = editValue;
    // }
    // 页面变化记录
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    private String Changeid;

    public String getChangeid() {
        return Changeid;
    }

    public void setChangeid(String changeid) {
        Changeid = changeid;
    }

    private String Parameters;// 记录ID

    public String getParameters() {

        return Parameters;
    }

    public void setParameters(String value) {

        Parameters = value;
    }


    private void CheckGongys() {
        String riqTiaoj = this.getRiqi();
        JDBCcon cn = new JDBCcon();
        String strMkMsg = "";
        String strGonsyMsg = "";
        String sql = "select * from (\n" +
                "    select cp.meikdwmc,nvl(cp.meikxxb_Id,0) as meikxxb_id,mkgys.gongysmc ,cp.gongysmc as gongys\n" +
                "    from (select distinct meikdwmc,gongysmc,\n" +
                "                 ( select max(id) from meikxxb where cp.meikdwmc=mingc or cp.meikdwmc =quanc) as meikxxb_Id\n" +
                "           from chepbtmp  cp\n" +
                "                where  daohrq=date'" + riqTiaoj + "') cp,\n" +
                "           (select max(gy.mingc) as gongysmc,mk.mingc,meikxxb_id\n" +
                "           from gongysmkglb gl,gongysb gy,meikxxb mk\n" +
                "                where gl.gongysb_id=gy.id\n" +
                "                and mk.id=gl.meikxxb_id\n" +
                "                group by gl.meikxxb_id ,mk.mingc) mkgys\n" +
                "    where cp.meikxxb_Id=mkgys.meikxxb_id(+)\n" +
                ")\n" +
                "where  meikxxb_id=0 or gongysmc is null  or gongysmc<>gongys";

        ResultSetList rs = cn.getResultSetList(sql);
        while (rs.next()) {
            //系统中没有对应的煤矿
            if (rs.getString("meikxxb_Id").equals("0")) {
                if (strMkMsg.length() == 0) {
                    strMkMsg = rs.getString("meikdwmc");
                } else {
                    strMkMsg = strMkMsg + "，" + rs.getString("meikdwmc");
                }
            } else {
                //系统供应商与chepttmp中的供应商不一致
                if (!rs.getString("gongysmc").equals(rs.getString("gongys"))) {
                    //系统中没有此供应商
                    if (rs.getString("gongysmc").equals("")) {
                        if (strGonsyMsg.length() == 0) {
                            strGonsyMsg = rs.getString("meikdwmc");
                        } else {
                            strGonsyMsg = strGonsyMsg + "，" + rs.getString("meikdwmc");
                        }
                    } else {
                        //系统中有此供应商，但名称不一致，需要按煤矿和日期条件更新此煤矿的供应商
                        cn.getUpdate(" update chepbtmp set gongysmc='" + rs.getString("gongysmc") + "' " +
                                " where meikdwmc='" + rs.getString("meikdwmc") + "'" +
                                " and daohrq=date'" + riqTiaoj + "'");
                    }
                }
            }
        }
        cn.Close();

        if (strMkMsg.length() > 0) {
            setMsg("请检查煤矿单位[" + strMkMsg + "]是否在系统中配置|");
        }

        if (strGonsyMsg.length() > 0) {
            setMsg(getMsg() + "请检煤矿[" + strGonsyMsg + "]是否在系统中关联供应商!");
        }
    }

    private String Save() {
        Visit visit = (Visit) this.getPage().getVisit();
        JDBCcon con = new JDBCcon();
        //判断当天是否已经存在这批
        String ssql = "select id from fahb where (GONGYSB_ID,MEIKXXB_ID,PINZB_ID,CHEC) in (\n" +
                "  select\n" +
                "    DISTINCT\n" +
                "    (select max(id) from gongysb where mingc=c.gongysmc or QUANC=c.GONGYSMC),\n" +
                "    (SELECT max(id) from meikxxb where mingc=c.meikdwmc or QUANC=c.MEIKDWMC),\n" +
                "    (select max(id) from pinzb where mingc =c.pinz),\n" +
                "    c.chec from chepbtmp c where fahbtmp_id=" + Changeid + "\n" +
                ")";
        ResultSet srs = con.getResultSet(ssql);
        try {
            if (srs.next()) {
                String fahb_id = srs.getString("id");
                ssql = "BEGIN\n" +
                        "insert into chepb (id,xuh,piaojh, cheph, yuanmz, yuanpz, maoz, piz, biaoz, koud, kous, kouz, zongkd, sanfsl,\n" +
                        "                   ches, jianjfs, guohb_id, fahb_id, chebb_id, yuanmkdw, yunsdwb_id, qingcsj, qingchh, qingcjjy,\n" +
                        "                   zhongcsj, zhongchh, zhongcjjy, meicb_id, daozch, lursj, lury, beiz , hedbz,xiecfsb_id,bulsj,\n" +
                        "                   zhuangcdw_item_id,meigy,YINGD,yingk,yuns)\n" +
                        "  select GETNEWID(197),CHEC, PIAOJH,\n" +
                        "    cheph, yuanmz, yuanpz, maoz, piz, biaoz, koud, kous, kouz,koud+kous+kouz,sanfsl, ches, jianjfs, guohb_id, " + fahb_id + ", chebb_id, yuanmkdw, (select max(id) from yunsdwb where mingc=yunsdw or quanc=yunsdw),\n" +
                        "    qingcsj, qingchh, qingcjjy, zhongcsj, zhongchh, zhongcjjy, meicb_id, daozch,sysdate,lury, beiz, 2,2,0,\n" +
                        "    0,meigy ,\n" +
                        "    YINGD,yingk,yuns from CHEPBTMP where fahbtmp_id=" + Changeid + ";\n" +
                        "\n" +
                        "UPDATE fahb set ( maoz,piz,jingz,biaoz,yingd,yingk,yuns,\n" +
                        "                  koud,kous,kouz,zongkd,ches,sanfsl,hedbz,laimsl,\n" +
                        "                  laimzl,laimkc)=(\n" +
                        "     select sum(maoz),sum(piz),sum(maoz-piz) ,sum(biaoz),sum(yingd),sum(yingk),sum(yuns),sum(koud),sum(kous),\n" +
                        "     sum(kouz),sum(zongkd),count(id),sum(SANFSL),2,sum(maoz-piz-kouz),sum(maoz-piz-kouz),sum(maoz-piz-kouz)\n" +
                        "     from chepb where FAHB_ID=" + fahb_id + "\n" +
                        ") where id=  " + fahb_id + ";" +
                        "update chepbtmp set fahb_id=1 where fahbtmp_id="+Changeid+";end;";
                int flag = con.getUpdate(ssql);
                if (flag != -1) {
                    setMsg("导入成功！");
                    con.commit();
                } else {
                    con.rollBack();
                    setMsg("导入失败！");
                }
                return this.msg;
            }
            srs.close();
            StringBuffer sb = new StringBuffer();
            String sqlSave = "select c.id, c.diancxxb_id, c.gongysmc gongysb_id, c.meikdwmc meikxxb_id, c.faz faz_id,\n"
                    + "c.pinz pinzb_id, c.jihkj jihkjb_id, zhilb_id,piaojh, c.fahrq, c.daohrq, c.jianjfs,c.chebb_id,\n"
                    + "c.chec, '' as bianm, to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') guohsj,c.qingchh,c.qingcjjy,\n"
                    + " to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') zhongcsj, c.zhongchh, c.zhongcjjy,c.caiyrq, c.cheph, c.maoz, c.piz, c.biaoz,\n"
                    + "c.koud, c.kous, c.kouz, c.yuanmz, c.yuanpz, c.sanfsl, c.ches,c.daoz daoz_id, c.daoz yuandz_id,c.YUNSFS,c.yuanshdw,\n"
                    + "c.meikdwmc yuanmkdw, c.yunsdw, c.daozch, c.xiecfs, c.beiz ,c.fahbtmp_id,c.meigy from chepbtmp c where fahbtmp_id = "
                    + Changeid + " order by c.zhongcsj";
            ResultSetList rsl = con.getResultSetList(sqlSave);
            if (rsl == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "ShujblQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
                setMsg(ErrorMessage.NullResult);
                return this.msg;
            }
            IDropDownModel YUNSFSB = new IDropDownModel("select * from YUNSFSB");
            IDropDownModel yunsdwb = new IDropDownModel("select id,mingc from yunsdwb");
            IDropDownModel yuanshdw = new IDropDownModel("select id,mingc from vwyuanshdw");
            IDropDownModel Xiecfs = new IDropDownModel(SysConstant.SQL_xiecfs);
            // 插入车皮临时表
            sb.append("begin\n");
            while (rsl.next()) {
                long gongysb_id = -1;
                //----------------------------------根据名称匹配供应商和煤矿信息-----------------------------------------------
                String gongysmc = rsl.getString("gongysb_id");
                String sql = " select max(id) id from gongysb where mingc='" + gongysmc + "' or " + "quanc='" + gongysmc + "' and leix=1";
                ResultSetList gRsl = con.getResultSetList(sql);
                if (gRsl.next()) {
                    gongysb_id = gRsl.getLong("id");
                } else {
                    setMsg("该煤矿单位没有对应的供应商，请输入供应商后重新录入!");
                    return this.msg;
                }
                gRsl.close();
                long meikxxb_id = -1;
                String meikmc = rsl.getString("meikxxb_id");
                sql = " select max(id) id from meikxxb where mingc='" + meikmc + "' or " + "quanc='" + meikmc + "'";
                ResultSetList mRsl = con.getResultSetList(sql);
                if (mRsl.next()) {
                    meikxxb_id = mRsl.getLong("id");
                } else {
                    setMsg("该煤矿单位没有对应的煤矿，请输入煤矿后重新录入!");
                    return this.msg;
                }
                mRsl.close();
                long yunsdwb_id=-1;
                String yunsdw = rsl.getString("yunsdw");
                sql = " select max(id) id from yunsdwb where mingc='" + yunsdw + "' or " + "quanc='" + yunsdw + "'";
                ResultSetList yRsl = con.getResultSetList(sql);
                if (yRsl.next()) {
                    yunsdwb_id = yRsl.getLong("id");
                } else {
                    setMsg("该运输单位没有对应的运输单位，请输入运输单位后重新录入!");
                    return this.msg;
                }
                yRsl.close();
                //----------------------------------------------------------------------------------------------------------
                int flg = Jilcz.Hetkzjm(con, visit.getDiancxxb_id(),
                        meikxxb_id, DateUtil.FormatDate(rsl.getDate("daohrq")), SysConstant.YUNSFS_QIY);
                if (flg == -1) {
                    con.rollBack();
                    con.Close();
                    setMsg("该煤矿单位没有对应的合同，请输入合同后重新录入!");
                    return this.msg;
                }

                if (gongysb_id == -1 || gongysb_id == 0) {
                    setMsg("部分数据供应商【" + gongysmc + "】未匹配，请匹配后再试!");
                    rsl.close();
                    con.Close();
                    return this.msg;
                }

                if (meikxxb_id == -1 || meikxxb_id == 0) {
                    setMsg("部分数据煤矿【" + meikmc + "】未匹配，请匹配后再试!");
                    rsl.close();
                    con.Close();
                    return this.msg;
                }
                long pinzb_id = (getExtGrid().getColumn("pinzb_id").combo)
                        .getBeanId(rsl.getString("pinzb_id"));

                if (pinzb_id == -1) {
                    setMsg("部分品种数据未匹配，请匹配后再试!");
                    rsl.close();
                    con.Close();
                    return this.msg;
                }
                long faz_id = (getExtGrid().getColumn("faz_id").combo)
                        .getBeanId(rsl.getString("faz_id"));
                if (faz_id == -1) {
                    setMsg("部分数据未匹配，请匹配后再试!");
                    rsl.close();
                    con.Close();
                    return this.msg;
                }
                long daoz_id = (getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl
                        .getString("daoz_id"));
                if (daoz_id == -1) {
                    setMsg("部分数据未匹配，请匹配后再试!");
                    rsl.close();
                    con.Close();
                    return this.msg;
                }
                long yunsfs_id = YUNSFSB.getBeanId(rsl.getString("YUNSFS"));
                if (yunsfs_id == -1) {
                    setMsg("部分数据未匹配，请匹配后再试!");
                    rsl.close();
                    con.Close();
                    return this.msg;
                }
                long jihkj_id = (getExtGrid().getColumn("jihkjb_id").combo)
                        .getBeanId(rsl.getString("jihkjb_id"));
                if (jihkj_id == -1) {
                    setMsg("计划口径部分数据未匹配，请匹配后再试!");
                    rsl.close();
                    con.Close();
                    return this.msg;
                }
                long xiecfs_id = Xiecfs.getBeanId(rsl.getString("xiecfs"));
                //保存前在jincjhb表中自动查找供应商、煤矿与电厂匹配
                long diancxxb_id = rsl.getLong("diancxxb_id");
                String sql_pp = "select j.diancxxb_id\n" +
                        "from jincjhb j\n" +
                        "where j.gongysb_id = '" + gongysb_id + "'\n" +
                        "  and j.meikxxb_id = '" + meikxxb_id + "'\n";
                ResultSetList rsl_pp = con.getResultSetList(sql_pp);
                if (rsl_pp.next()) {
                    diancxxb_id = rsl_pp.getLong("diancxxb_id");
                }
                sb.append("insert into cheplsb\n");
                sb.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id,zhilb_id,piaojh, " +
                        "fahrq, daohrq, caiybh, yunsfsb_id, chec,cheph, maoz, piz, biaoz, koud, kous, kouz, yuanmz, yuanpz, " +
                        "sanfsl, ches,jianjfs, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, zhongcjjy, daozch, lury, " +
                        "beiz,qingcsj,qingchh,QINGCJJY,zhongcsj,zhongchh,yunsdwb_id,caiyrq,xiecfsb_id,meigy)\n");
                sb.append("values (GETNEWID(197),").append(diancxxb_id);
                sb.append(",").append(gongysb_id);
                sb.append(",").append(meikxxb_id);
                sb.append(",").append(pinzb_id);
                sb.append(",").append(faz_id);
                sb.append(",").append(daoz_id);
                sb.append(",").append(jihkj_id);
                sb.append(",").append(rsl.getString("zhilb_id"));
                sb.append(",'").append(rsl.getString("piaojh"));
                sb.append("',to_date('").append(DateUtil.FormatDate(rsl.getDate("fahrq"))).append("','yyyy-mm-dd')");
                sb.append(",to_date('").append(DateUtil.FormatDate(rsl.getDate("daohrq"))).append("','yyyy-mm-dd')");
                sb.append(",'").append(rsl.getString("bianm")).append("'");
                sb.append(",").append(yunsfs_id).append(",'").append(rsl.getString("chec"));
                sb.append("','").append(rsl.getString("cheph"));
                sb.append("',").append(rsl.getDouble("maoz"));
                sb.append(",").append(rsl.getDouble("piz"));
                sb.append(",").append(rsl.getDouble("biaoz"));
                sb.append(",").append(rsl.getDouble("koud"));
                sb.append(",").append(rsl.getDouble("kous"));
                sb.append(",").append(rsl.getDouble("kouz"));
                sb.append(",").append(rsl.getDouble("yuanmz"));
                sb.append(",").append(rsl.getDouble("yuanpz"));
                sb.append(",").append(rsl.getDouble("sanfsl"));
                sb.append(",").append(rsl.getDouble("ches"));
                sb.append(",'").append(rsl.getString("jianjfs"));
                sb.append("',").append(rsl.getString("chebb_id"));
                sb.append(",").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("yuandz_id")));
                sb.append(",").append(yuanshdw.getBeanId(rsl.getString("yuanshdw")));
                sb.append(",'").append(rsl.getString("yuanmkdw"));
                sb.append("','").append(rsl.getString("zhongcjjy"));
                sb.append("','").append(rsl.getString("daozch"));
                sb.append("','").append(visit.getRenymc());
                sb.append("','").append(rsl.getString("beiz")).append("'");
                sb.append(",").append(DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
                sb.append(",'").append(rsl.getString("qingchh")).append("'");
                sb.append(",'").append(rsl.getString("qingcjjy")).append("'");
                sb.append(",").append(DateUtil.FormatOracleDateTime(rsl.getString("zhongcsj")));
                sb.append(",'").append(rsl.getString("zhongchh")).append("'");
                sb.append(",").append(yunsdwb_id);
                sb.append(",to_date('").append(DateUtil.FormatDate(rsl.getDate("caiyrq"))).append("','yyyy-mm-dd hh24:mi:ss'),").append(xiecfs_id).append(",'").append(rsl.getString("meigy")).append("');\n");
            }
            sb.append("end;");
//            System.out.println(sb);
            int yunsfs = SysConstant.YUNSFS_HUOY;
            if (QY.equals(getLeix())) {
                yunsfs = SysConstant.YUNSFS_QIY;
            }
            int Hedbz = SysConstant.HEDBZ_YJJ;
            if (DRYD.equalsIgnoreCase(getMod())) {
                Hedbz = SysConstant.HEDBZ_TJ;
            }
            setMsg(Jilcz.SaveJilData(sb.toString(), visit.getDiancxxb_id(), yunsfs, Hedbz, null, this.getClass().getName(), Jilcz.SaveMode_DR, Changeid));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return "";
    }

    private boolean _RefurbishChick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }

    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _ChakChick = false;

    public void ChakButton(IRequestCycle cycle) {
        _ChakChick = true;
    }

    private boolean _AutoMathChick = false; // 自动匹配按钮

    public void AutoMathButton(IRequestCycle cycle) {
        _AutoMathChick = true;
    }

    private boolean _DeleteChick = false;

    public void DeleteButton(IRequestCycle cycle) {
        _DeleteChick = true;
    }

    public void submit(IRequestCycle cycle) throws Exception {
        try {
            if (_SaveChick) {
                _SaveChick = false;
                String msg=Save();
                getSelectData();
//                this.setMsg(msg);
            }
            if (_RefurbishChick) {
                _RefurbishChick = false;
                getSelectData();
            }
            if (_ChakChick) {
                _ChakChick = false;
                Update(cycle);
            }
            if (_AutoMathChick) {
                _AutoMathChick = false;
                AutoMath();
                getSelectData();
            }
            if (_DeleteChick) {
                _DeleteChick = false;
                delete();
                getSelectData();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    private void delete() {

        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        int ds_id = Integer.valueOf(MainGlobal.getProperId_String(this.getIDsModel(), this.getDsmc())).intValue();

        String fahb_id = "";
        if (ds_id < 0) {
            fahb_id = "";//全部
        }
        if (ds_id == 0) {//未导入的
            fahb_id = " and fahb_id=0";
        }
        if (ds_id > 0) {//已经导入
            fahb_id = " and fahb_id!=0";
        }


        String riqTiaoj = this.getRiqi();

        String sql = " delete from chepbtmp where daohrq= to_date('" + riqTiaoj
                + "','yyyy-mm-dd') " + Jilcz.filterDcid(visit, "") + fahb_id + "  and fahbtmp_id=" + Changeid;

        //System.out.println(sql);
        int flag = con.getDelete(sql);

        con.Close();

        if (flag >= 0) {
            this.setMsg("数据操作成功!");
        } else {
            this.setMsg("数据操作失败!");
        }
    }

    private void AutoMath() throws MalformedURLException, ServiceException,
            RemoteException {
        // TODO 自动生成方法存根
        // 手动调用自动匹配导入方法
        String ReMsg = "";
        String riqTiaoj = this.getRiqi();
        if (riqTiaoj == null || riqTiaoj.equals("")) {
            riqTiaoj = DateUtil.FormatDate(new Date());
        }
        ReMsg = QichsjppInterface.Qicsjpp_sd(
                ((Visit) this.getPage().getVisit()).getDiancxxb_id(), riqTiaoj);// 匹配操作
        setMsg(ReMsg);
    }

    public void getSelectData() {
//        setMsg("");
        CheckGongys();

        // i++;
        // System.out.println(i);
        String yunsfs = "select id,mingc from yunsfsb";
        String riqTiaoj = this.getRiqi();
        String changb = "";
        if (riqTiaoj == null || riqTiaoj.equals("")) {
            riqTiaoj = DateUtil.FormatDate(new Date());
        }
        StringBuffer sb1 = new StringBuffer();
        if (HY.equals(getLeix())) {
            sb1.append(" and yunsfs = '").append(
                    (new IDropDownModel(yunsfs))
                            .getBeanValue(SysConstant.YUNSFS_HUOY)).append("'");
        } else if (QY.equals(getLeix())) {
            sb1.append(" and yunsfs = '").append(
                    (new IDropDownModel(yunsfs))
                            .getBeanValue(SysConstant.YUNSFS_QIY)).append("'");
        }


        Visit visit = (Visit) getPage().getVisit();
        visit.setString1(getChange());

        if (getChangbValue().getId() != visit.getDiancxxb_id()) {
            changb = " and diancxxb_id=" + getChangbValue().getId();
        } else {

        }

        JDBCcon con = new JDBCcon();
        String sql = "";
        //sum(maoz) as maoz,sum(piz) as piz,sum(koud) as koud,
        //region Description
        if (visit.getDiancmc().equals("宣威发电")) {
            //region sql=...
            sql = "select diancxxb_id,gongysmc as gongysb_id,meikdwmc as meikxxb_id,faz as faz_id,daoz as daoz_id,pinz as pinzb_id,jihkj as jihkjb_id,fahrq,daohrq,caiybh,chec,nvl(max(FAHBTMP_ID),-1) as ID,\n"
                    + "count(id) as ches,sum(maoz) as maoz,sum(piz) as piz,sum(koud) as koud,sum(biaoz) as biaoz,sum(maoz - piz - koud - kous - kouz) jingz\n"
                    + "from chepbtmp where daohrq = to_date('" + riqTiaoj + "','yyyy-mm-dd') "
                    + Jilcz.filterDcid(visit, "")
                    + " and fahb_id = 0 "
                    + sb1.toString()
                    + " \n"
                    + "  group by diancxxb_id,gongysmc, meikdwmc,faz,daoz,pinz,jihkj,fahrq,daohrq,caiybh,chec,FAHBTMP_ID \n"
                    + "  union\n"
                    + "  select -1 diancxxb_id,'合计' as gongysb_id,'' as meikxxb_id,'' as faz_id,'' as daoz_id,'' as pinzb_id,'' as jihkjb_id,to_date('','yyyy-mm-dd') fahrq,to_date('','yyyy-mm-dd') daohrq,'' caiybh,'' chec,0 as ID,\n"
                    + "  hj.ches as ches,hj.maoz as maoz,hj.piz as piz,hj.koud as koud,hj.biaoz as biaoz,hj.jingz as  jingz\n"
                    + "  from\n"
                    + "  (select '' diancxxb_id,count(id) as ches,sum(maoz) as maoz,sum(piz) as piz,sum(koud) as koud,sum(biaoz) as biaoz,sum(maoz - piz - koud - kous - kouz) jingz\n"
                    + "  from chepbtmp where daohrq = to_date('" + riqTiaoj + "','yyyy-mm-dd') "
                    + Jilcz.filterDcid(visit, "")
                    + "  and fahb_id = 0  "
                    + sb1.toString()
                    + "  ) hj where hj.maoz>0\n"
                    + "  order by diancxxb_id desc";
            //endregion

        } else {
            sql = "select diancxxb_id,gongysmc as gongysb_id,meikdwmc as meikxxb_id,faz as faz_id,daoz as daoz_id,pinz as pinzb_id,jihkj as jihkjb_id,fahrq,daohrq,caiybh,chec,nvl(max(FAHBTMP_ID),-1) as ID,\n"
                    + "count(id) as ches,sum(maoz) as maoz,sum(piz) as piz,sum(koud) as koud,sum(biaoz) as biaoz,sum(maoz - piz - koud - kous - kouz) jingz\n"
                    + "from chepbtmp where daohrq = to_date('" + riqTiaoj + "','yyyy-mm-dd') "
                    + Jilcz.filterDcid(visit, "")
                    + changb
                    + " and fahb_id = 0 "
                    + sb1.toString()
                    + " \n"
                    + "group by diancxxb_id,gongysmc, meikdwmc,faz,daoz,pinz,jihkj,fahrq,daohrq,caiybh,chec\n"
                    + "union\n"
                    + "select -1 diancxxb_id,'合计' as gongysb_id,'' as meikxxb_id,'' as faz_id,'' as daoz_id,'' as pinzb_id,'' as jihkjb_id,to_date('','yyyy-mm-dd') fahrq,to_date('','yyyy-mm-dd') daohrq,'' caiybh,'' chec,0 as ID,\n"
                    + "hj.ches as ches,hj.maoz as maoz,hj.piz as piz,hj.koud as koud,hj.biaoz as biaoz,hj.jingz as  jingz\n"
                    + "from \n"
                    + "(select '' diancxxb_id,count(id) as ches,sum(maoz) as maoz,sum(piz) as piz,sum(koud) as koud,sum(biaoz) as biaoz,sum(maoz - piz - koud - kous - kouz) jingz\n"
                    + "from chepbtmp where daohrq = to_date('" + riqTiaoj + "','yyyy-mm-dd') "
                    + Jilcz.filterDcid(visit, "")
                    + changb
                    + " and fahb_id = 0 "
                    + sb1.toString()
                    + "--group by diancxxb_id\n) hj where hj.maoz>0 ";

        }
        //endregion
        ResultSetList rsl = con.getResultSetList(sql);

        if (visit.getDiancmc().equals("宣威发电")) {

        } else {

//			 setFahbtmp_id(rsl);
            if (rsl.getRows() > 0) {
                StringBuffer sb = new StringBuffer();

                sb.append("begin\n");
                while (rsl.next()) {
                    if (!rsl.getString("gongysb_id").equals("合计")) {
                        long fahbtmpid = rsl.getLong("ID");
                        if (fahbtmpid == -1 || fahbtmpid == 0) {
                            fahbtmpid = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
                        }
                        rsl.setString("ID", String.valueOf(fahbtmpid));
                        if (rsl.getString("caiybh").trim().equals("")
                                || rsl.getString("caiybh") == null) {
                            sb.append("update chepbtmp set fahbtmp_id=" + fahbtmpid
                                    + " where gongysmc ='" + rsl.getString("gongysb_id"));
                            sb.append("' and meikdwmc='" + rsl.getString("meikxxb_id")
                                    + "' and faz = '" + rsl.getString("faz_id")
                                    + "' and daoz='" + rsl.getString("daoz_id")
                                    + "' and pinz='" + rsl.getString("pinzb_id"));
                            sb.append("' and fahrq=to_date('" + DateUtil.FormatDate(rsl.getDate("fahrq")) + "','yyyy-mm-dd') " + Jilcz.filterDcid(visit, "")
                                    + " and daohrq=to_date('" + DateUtil.FormatDate(rsl.getDate("daohrq")) + "','yyyy-mm-dd') and chec='"
                                    + rsl.getString("chec") + "' and caiybh is null and fahbtmp_id in(-1,0);\n");
                        } else {
                            sb.append("update chepbtmp set fahbtmp_id=" + fahbtmpid
                                    + " where gongysmc ='"
                                    + rsl.getString("gongysb_id"));
                            sb.append("' and meikdwmc='" + rsl.getString("meikxxb_id")
                                    + "' and faz = '" + rsl.getString("faz_id")
                                    + "' and daoz='" + rsl.getString("daoz_id")
                                    + "' and pinz='" + rsl.getString("pinzb_id"));
                            sb.append("' and fahrq=to_date('"
                                    + DateUtil.FormatDate(rsl.getDate("fahrq"))
                                    + "','yyyy-mm-dd') " + Jilcz.filterDcid(visit, "")
                                    + " and daohrq=to_date('"
                                    + DateUtil.FormatDate(rsl.getDate("daohrq"))
                                    + "','yyyy-mm-dd') and chec='"
                                    + rsl.getString("chec") + "' and caiybh='"
                                    + rsl.getString("caiybh")
                                    + "' and fahbtmp_id in(-1,0);\n");
                        }

                    }
                }
                sb.append("end;\n");
                con.getUpdate(sb.toString());
                rsl.beforefirst();
            }

        }


        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, customKey);
        egu.setWidth(Locale.Grid_DefaultWidth);

        egu.setTableName("chepbtmp");
        egu.getColumn("diancxxb_id").setHeader("电厂");
        egu.getColumn("diancxxb_id").setHidden(true);
        egu.getColumn("gongysb_id").setHeader("供货单位");
        egu.getColumn("meikxxb_id").setHeader("煤矿");
        egu.getColumn("faz_id").setHeader("发站");
        egu.getColumn("daoz_id").setHeader("到站");
        egu.getColumn("pinzb_id").setHeader("品种");
        egu.getColumn("jihkjb_id").setHeader("计划口径");
        egu.getColumn("fahrq").setHeader("发货日期");
        egu.getColumn("daohrq").setHeader("到货日期");
        egu.getColumn("daohrq").setEditor(null);
        egu.getColumn("caiybh").setHeader("采样编号");
        egu.getColumn("chec").setHeader("车次");
        //egu.getColumn("yuns").setHeader("运损");
        //egu.getColumn("yingk").setHeader("盈亏");
        egu.getColumn("ches").setHeader("车数");
        egu.getColumn("ches").setEditor(null);
        egu.getColumn("maoz").setHeader("毛重");
        egu.getColumn("maoz").setEditor(null);
        egu.getColumn("piz").setHeader("皮重");
        egu.getColumn("piz").setEditor(null);
        egu.getColumn("koud").setHeader("扣吨");
        egu.getColumn("koud").setEditor(null);
        egu.getColumn("biaoz").setHeader("标重");
        egu.getColumn("biaoz").setEditor(null);
        egu.getColumn("jingz").setHeader("净重");
        egu.getColumn("jingz").setWidth(80);
        //egu.getColumn("jingz").setHidden(true);
        egu.getColumn("jingz").setEditor(null);
        egu.getColumn("ID").setHeader("分组ID");
        egu.getColumn("ID").setHidden(true);

        egu.addTbarText("到货日期:");
        DateField df = new DateField();
        df.setValue(this.getRiqi());
        df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
        egu.addToolbarItem(df.getScript());

        // 设置供应商下拉框
        ComboBox c8 = new ComboBox();
        egu.getColumn("gongysb_id").setEditor(c8);
        c8.setEditable(true);
        String gyssb = "select id,mingc from gongysb  where leix=1 order by mingc";
        egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gyssb));
        // 设置煤矿单位下拉框
        ComboBox c9 = new ComboBox();
        egu.getColumn("meikxxb_id").setEditor(c9);
        c9.setEditable(true);
        c9.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
        String mksb = "select id,mingc from meikxxb order by mingc";
        egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(mksb));
        // 设置发站下拉框
        ComboBox c0 = new ComboBox();
        egu.getColumn("faz_id").setEditor(c0);
        c0.setEditable(true);
        String Fazsb = "select id,mingc from chezxxb order by mingc";
        egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(Fazsb));

        ComboBox c1 = new ComboBox();
        egu.getColumn("daoz_id").setEditor(c1);
        c1.setEditable(true);
        String Daozsb = "select id,mingc from chezxxb order by mingc";
        egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(Daozsb));

        // 设置品种下拉框
        ComboBox c2 = new ComboBox();
        egu.getColumn("pinzb_id").setEditor(c2);
        c2.setEditable(true);
        String pinzsb = SysConstant.SQL_Pinz_mei;
        egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzsb));
        // 设置口径下拉框
        ComboBox c3 = new ComboBox();
        egu.getColumn("jihkjb_id").setEditor(c3);
        c3.setEditable(true);
        String jihkjsb = SysConstant.SQL_Kouj;
        egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjsb));

        egu.setGridType(ExtGridUtil.Gridstyle_Edit);
        egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// 只能单选中行

        egu.addPaging(0);

        //
        if (visit.isFencb()) {

            egu.addTbarText("厂别:");
            ComboBox CBchangb = new ComboBox();
            CBchangb.setTransform("ChangbDropDown");
            CBchangb.setId("Changb");
            CBchangb.setLazyRender(true);// 动态绑定
            CBchangb.setWidth(100);
            egu.addToolbarItem(CBchangb.getScript());
            egu.addOtherScript("Changb.on('select',function(){document.getElementById('RefreshButton').click();});");
        }

        StringBuffer rsb = new StringBuffer();
        rsb.append("function (){").append(
                "document.getElementById('RefreshButton').click();}");
        GridButton gbr = new GridButton("刷新", rsb.toString());
        gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
        egu.addTbarBtn(gbr);
        GridButton bc = new GridButton("导入", "function(){ "
                + " var rec = gridDiv_sm.getSelected(); "
                + " if(rec != null){var id = rec.get('ID');"
                + " var Cobjid = document.getElementById('CHANGEID');"
                + " Cobjid.value = id;"
                + " var sbtn=document.getElementById('SaveButton');sbtn.click();sbtn.setAttribute('disabled', true);"+
                MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+"}}");
        bc.setIcon(SysConstant.Btn_Icon_Save);
        egu.addTbarBtn(bc);

        egu.addTbarText("-");

        GridButton gb = new GridButton("查看", "function(){ "
                + " var rec = gridDiv_sm.getSelected(); "
                + " if(rec != null){var id = rec.get('ID');"
                + " var Cobjid = document.getElementById('CHANGEID');"
                + " Cobjid.value = id;"
                + " document.getElementById('ChakButton').click();}}");
        gb.setIcon(SysConstant.Btn_Icon_Search);
        egu.addTbarBtn(gb);

        egu.addTbarText("-");

        if (visit.getDiancmc().equals("宣威发电")) {
            //宣威发电不显示"自动匹配导入"和删除按钮,同时把一些不需要的字段隐藏


        } else {
//			GridButton TBb = new GridButton("全部导入", "function(){ \n"
//					+ " var rec=gridDiv_ds.getRange();	\n"
//					+ " if(rec.length==0){ \n"
//					+ "		Ext.MessageBox.alert('提示信息','没有未导入的汽车数据');	\n"
//					+ " }else{	\n"
//					+ " 		document.getElementById('AutoMathButton').click();}	\n}");
//			TBb.setIcon(SysConstant.Btn_Icon_Save);
//			egu.addTbarBtn(TBb);

            if (QY.equals(getLeix())) {//汽运  增加删除功能

                egu.addTbarText("-");

                String gb3_fs = "function(){  \n"
                        + " var rec = gridDiv_sm.getSelected(); "
                        + " if(rec != null){var id = rec.get('ID');"
                        + " var Cobjid = document.getElementById('CHANGEID');"
                        + " Cobjid.value = id;}"

                        + " if(!win){	\n"
                        + "	\tvar form = new Ext.form.FormPanel({	\n"
                        + " \tbaseCls: 'x-plain',	\n"
                        + " \tlabelAlign:'right',	\n"
                        + " \tdefaultType: 'textfield',	\n"
                        + " \titems: [{		\n"
                        + " \txtype:'fieldset',	\n"
                        + " \ttitle:'请选择要删除的数据源',	\n"
                        + " \tautoHeight:false,	\n"
                        + " \theight:220,	\n"
                        + " \titems:[	\n"
                        + " \tlcmccb=new Ext.form.ComboBox({	\n"
                        + " \twidth:150,	\n"
                        + " \tid:'lcmccb',	\n"
                        + " \tselectOnFocus:true,	\n"
                        + "	\ttransform:'DsDropDown',	\n"
                        + " \tlazyRender:true,	\n"
                        + " \tfieldLabel:'数据源',		\n"
                        + " \ttriggerAction:'all',	\n"
                        + " \ttypeAhead:true,	\n"
                        + " \tforceSelection:true,	\n"
                        + " \teditable:false	\n"
                        + " \t})	\n"


                        + " \t]		\n"
                        + " \t}]	\n"
                        + " \t});	\n"
                        + " \twin = new Ext.Window({	\n"
                        + " \tel:'hello-win',	\n"
                        + " \tlayout:'fit',	\n"
                        + " \twidth:500,	\n"
                        + " \theight:300,	\n"
                        + " \tcloseAction:'hide',	\n"
                        + " \tplain: true,	\n"
                        + " \ttitle:'数据删除',	\n"
                        + " \titems: [form],	\n"
                        + " \tbuttons: [{	\n"
                        + " \ttext:'确定',	\n"
                        + " \thandler:function(){	\n"
                        + " \twin.hide();	\n"
                        + " \tif(lcmccb.getRawValue()=='请选择'){		\n"
                        + "	\t	alert('请选择数据源！');		\n"
                        + " \t}else{"
                        + " \t\t document.getElementById('DS_VALUE').value=lcmccb.getRawValue();	\n"
                        + " \t\t document.all.item('DeleteButton').click();	\n"
//					+" Ext.Msg.progress('提示信息','请等待','数据加载中……');\n"
//					+" Ext.Msg.show({title: 'Please wait',msg: '正在加载数据...',progressText: '数据加载中...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'数据加载中……','正在加载数据');},i*100);"
                        + " \t}	\n"
                        + " \t}	\n"
                        + " \t},{	\n"
                        + " \ttext: '取消',	\n"
                        + " \thandler: function(){	\n"
                        + " \twin.hide();	\n"
                        + " \tdocument.getElementById('DS_VALUE').value='';	\n"
                        + " \t}		\n"
                        + " \t}]	\n"
                        + " \t});}	\n"
                        + " \twin.show(this);	\n"

                        + " \tif(document.getElementById('DS_VALUE').value!=''){	\n"
                        //+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"
                        + " \t}	\n"
                        + " \t}";
                if (shujdrCanDel()) {
                    GridButton del = new GridButton("删除", gb3_fs);
                    del.setIcon(SysConstant.Btn_Icon_Delete);
                    egu.addTbarBtn(del);
                }
            }
        }

        setExtGrid(egu);
        con.Close();
    }


    //	数据源mingc
    private String _ds;

    public void setDsmc(String _value) {
        _ds = _value;
    }

    public String getDsmc() {
        if (_ds == null) {
            _ds = "";
        }
        return _ds;
    }


    //	数据源 DropDownBean8
//  数据源 ProSelectionModel8
    public IDropDownBean getDsValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
            ((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getIDsModel().getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean8();
    }

    public void setDsValue(IDropDownBean value) {
        if (((Visit) getPage().getVisit()).getDropDownBean8() != value) {
            ((Visit) getPage().getVisit()).setDropDownBean8(value);
        }
    }

    public void setIDsModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel8(value);
    }

    public IPropertySelectionModel getIDsModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
            getIDsModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }

    public IPropertySelectionModel getIDsModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean("-1", "全部"));
        list.add(new IDropDownBean("1", "已导入"));
        list.add(new IDropDownBean("0", "未导入"));
        ((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(list));
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }
//   数据源 end


    public ExtGridUtil getExtGrid() {
        return ((Visit) this.getPage().getVisit()).getExtGrid1();
    }

    public void setExtGrid(ExtGridUtil extgrid) {
        ((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
    }

    public String getGridScript() {
        return getExtGrid().getGridScript();
    }

    public String getGridHtml() {
        return getExtGrid().getHtml();
    }

    // 页面判定方法
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

    private void Update(IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        visit.setLong1(Long.parseLong(getChangeid()));
        //宣威需要查看时绑定日期
        visit.setString15(this.getRiqi());
        cycle.activate("Shujdr");
    }

    boolean riqichange = false;

    private String riqi;

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        if (this.riqi != null && !this.riqi.equals(riqi)) {
            riqichange = true;
        }
        this.riqi = riqi;
    }


    // 厂别
    public IDropDownBean getChangbValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean4((IDropDownBean) getIChangbModels()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setChangbValue(IDropDownBean Value) {
        if (((Visit) getPage().getVisit()).getDropDownBean4() != Value) {
            ((Visit) getPage().getVisit()).setboolean2(true);
            ((Visit) getPage().getVisit()).setDropDownBean4(Value);
        }
    }

    public void setIChangbModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getIChangbModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getIChangbModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public IPropertySelectionModel getIChangbModels() {
        String sql = "";
        if (((Visit) getPage().getVisit()).isFencb()) {
            sql = "select id, mingc from diancxxb d where d.fuid = " + ((Visit) getPage().getVisit()).getDiancxxb_id()
                    + "union select id, mingc from diancxxb where id = " + ((Visit) getPage().getVisit()).getDiancxxb_id()
                    + " order by id";
        } else {
            sql = "select id, mingc from diancxxb d where d.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
        }

        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql));
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    // 厂别end


    private boolean shujdrCanDel() {
        boolean canDel = false;
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) getPage().getVisit();
        String sql = "select zhi from xitxxb where mingc ='数据导入允许删除' " +
                "and zhuangt=1 and leib='数量' " +
                "and beiz='使用' " +
                "and diancxxb_id =" + visit.getDiancxxb_id();
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            if (rsl.getString("zhi").equals("是")) {
                canDel = true;
            }
        }
        con.Close();
        return canDel;
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        String lx = cycle.getRequestContext().getParameter("lx");
        if (lx != null && !"".equals(lx)) {
            setLeix(lx);
        }
        String mod = cycle.getRequestContext().getParameter("mod");
        if (mod != null && !"".equals(mod)) {
            setMod(mod);
        }
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {

            if (!visit.getActivePageName().toString().equals("Shujdr")) {
                JDBCcon con = new JDBCcon();
                String sql = "select zhi from xitxxb where mingc ='数据导入默认日期' " +
                        "and zhuangt=1 and leib='数量' and beiz='使用' and diancxxb_id =" +
                        visit.getDiancxxb_id();
                int riqpy = -1;
                ResultSetList rsl = con.getResultSetList(sql);
                while (rsl.next()) {
                    riqpy = rsl.getInt("zhi");
                }
                setRiqi(DateUtil.FormatDate(DateUtil.AddDate(new Date(), riqpy,
                        DateUtil.AddType_intDay)));
                rsl.close();
                con.Close();
            }
            setChangbValue(null); // DropDownBean4
            setIChangbModel(null); // ProSelectionModel4
            visit.setDropDownBean8(null);
            visit.setProSelectionModel8(null);
            visit.setActivePageName(this.getPageName().toString());
            visit.setLong1(0);
            if (getLeix() == null || "".equals(getLeix())) {
                setLeix("HY");
            }
            if (getMod() == null || "".equals(getMod())) {
                setMod("");
            }

            getSelectData();
        }

    }
}