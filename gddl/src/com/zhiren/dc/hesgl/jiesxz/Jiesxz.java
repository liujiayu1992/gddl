package com.zhiren.dc.hesgl.jiesxz;
/*
    2009-6-16 改
	zsj
	将visit中的String13 存成 fahb_id，传入下一个页面
*/
/**
 * 徐文理
 * 2012-08-14
 * 描述：国电宣威增加参数控制是否考核全水，减扣数量
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;

import com.zhiren.common.ext.*;
import com.zhiren.dc.hesgl.jiesd.Dcbalance;
import org.apache.log4j.Logger;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.hesgl.jiesd.Balances;
import com.zhiren.dc.hesgl.jiesd.Balances_variable;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.dc.jilgl.Jilcz;

/**
 * @author 张少君
 * 2009-10-16
 * 描述：结算系统中增加《一厂多制单场结算》参数及《主结算厂》 参数。
 * 在结算时根据《一厂多制单场结算》参数判断，列出各分厂所有符合条件的未结算数据进行结算，
 * 结算单中电厂ID存为《主结算厂》参数中设置的电厂ID。
 * @author 张少君
 * 2009-10-19
 * 描述：在该页面增加参数"jsdwid"（结算单位id），值为统一结算时要使用合同的需方id，
 * 例如：要做分公司采购单结算，就要使用分公司的采购合 同，故合同的需方应该是分公司，此时页面配置的参数值应为分公司的id
 * 如果读到该标识，将结算单位标识增加到string15中，
 * 首先判断如果结算单位为分公司
 * 传入下个页面，在保存时判断如果是分公司结算，
 * 那么将结算数据存入"分公司采购表"中，否则存入"电厂采购表"中。
 * @author 张少君
 * 2009-10-23
 * 描述：改善对一厂多制的业务的支持，
 * 1、页面中增加厂别多选combox 并增加 总厂名称。
 * 2、页面中增加主结算厂combox 为最终的结算单位。
 * <p>
 * huochaoyuan
 * 2010-01-16调整合同编号下拉框取数sql，使厂级系统可以取到需方是分公司的运输合同
 * @author 张少君
 * 2011-07-04
 * 描述：针对邯郸电厂及类似结算情况，一个lie_id对应多个pinzb_id，在结算时需要记录页面选择品种，并传到结算界面
 * 1、启用visit.string19 记录品种id
 * 2、将参数传入 Dcbalance和 Jieszbtz 两个功能页面
 * 2、Dcbalance和 Jieszbtz 两个功能做出相应调整
 */
/**
 * @author 张少君
 * 2009-10-19
 * 描述：在该页面增加参数"jsdwid"（结算单位id），值为统一结算时要使用合同的需方id，
 * 		例如：要做分公司采购单结算，就要使用分公司的采购合 同，故合同的需方应该是分公司，此时页面配置的参数值应为分公司的id
 * 			如果读到该标识，将结算单位标识增加到string15中，
 * 			首先判断如果结算单位为分公司
 * 			传入下个页面，在保存时判断如果是分公司结算，
 * 			那么将结算数据存入"分公司采购表"中，否则存入"电厂采购表"中。
 * */
/**
 * @author 张少君
 * 2009-10-23
 * 描述：改善对一厂多制的业务的支持，
 * 		1、页面中增加厂别多选combox 并增加 总厂名称。
 * 		2、页面中增加主结算厂combox 为最终的结算单位。
 * */
/**
 * huochaoyuan
 * 2010-01-16调整合同编号下拉框取数sql，使厂级系统可以取到需方是分公司的运输合同
 */
/**
 * @author 张少君
 * 2011-07-04
 * 描述：针对邯郸电厂及类似结算情况，一个lie_id对应多个pinzb_id，在结算时需要记录页面选择品种，并传到结算界面
 * 		1、启用visit.string19 记录品种id
 * 		2、将参数传入 Dcbalance和 Jieszbtz 两个功能页面
 * 		2、Dcbalance和 Jieszbtz 两个功能做出相应调整
 * */

/**
 * @author liht
 * 2011-12-29
 * 描述：过衡信息和品种共用String19导致结算出错
 * 		1、启用visit.string20 记录品种id
 * 		2、将参数传入 Dcbalance和 Jieszbtz 两个功能页面
 * 		2、Dcbalance和 Jieszbtz 两个功能做出相应调整
 * */
public class Jiesxz extends BasePage implements PageValidateListener {
    private static Logger logger = org.apache.log4j.LogManager.getLogger(Jiesxz.class);
    private String msg = "";
    private static String sql = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
    }

    protected void initialize() {
        msg = "";
    }

    private String scjsl = "";

    public String getScjsl() {

        return scjsl;
    }

    public void setScjsl(String value) {

        scjsl = value;
    }

    // 绑定日期
    public String getRiq1() {
        if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {

            ((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
        }
        return ((Visit) this.getPage().getVisit()).getString5();
    }

    public void setRiq1(String riq1) {

        if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {

            ((Visit) this.getPage().getVisit()).setString5(riq1);
            ((Visit) this.getPage().getVisit()).setboolean1(true);
        }
    }


    public String getRiq2() {
        if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {

            ((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
        }
        return ((Visit) this.getPage().getVisit()).getString6();
    }

    public void setRiq2(String riq2) {

        if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {

            ((Visit) this.getPage().getVisit()).setString6(riq2);
            ((Visit) this.getPage().getVisit()).setboolean1(true);
        }

    }

    //	日期类型选择
    public String getRbvalue() {
        return ((Visit) this.getPage().getVisit()).getString1();
    }

    public void setRbvalue(String rbvalue) {
        ((Visit) this.getPage().getVisit()).setString1(rbvalue);
    }

//	长别下拉框取值(多选)

    public String getChangb() {

        if (((Visit) this.getPage().getVisit()).getString3().equals("")) {

            ((Visit) this.getPage().getVisit()).setString3(
                    ((IDropDownBean) getFencbModel().getOption(0)).getValue());
        }

        return ((Visit) this.getPage().getVisit()).getString3();
    }

    public void setChangb(String changb) {
        ((Visit) this.getPage().getVisit()).setString3(changb);
    }

//	主结算单位

    public String getMainjsdw() {

        if (((Visit) this.getPage().getVisit()).getString16().equals("")) {

            ((Visit) this.getPage().getVisit()).setString16(
                    ((IDropDownBean) getFencbModel().getOption(0)).getValue());
        }

        return ((Visit) this.getPage().getVisit()).getString16();
    }

    public void setMainjsdw(String maindw) {
        ((Visit) this.getPage().getVisit()).setString16(maindw);
    }

    //	运输单位下拉框
    public String getYunsdw() {
        return ((Visit) this.getPage().getVisit()).getString10();
    }

    public void setYunsdw(String yunsdw) {
        ((Visit) this.getPage().getVisit()).setString10(this.getYunsdwValue().getValue());
    }

    //	品种下拉框
    private String _pinz = "";

    public String getPinz() {
        return _pinz;
    }

    public void setPinz(String pinz) {
        _pinz = pinz;
    }

//	验收编号下拉框取值

    public String getYansbh() {
        return ((Visit) this.getPage().getVisit()).getString4();
    }

    public void setYansbh(String yansbh) {
        ((Visit) this.getPage().getVisit()).setString4(yansbh);
    }

    //	预结算单编码
    public String getYujsdbm() {
        return ((Visit) this.getPage().getVisit()).getString20();
    }

    public void setYujsdbm(String yujsdbm) {
        ((Visit) this.getPage().getVisit()).setString20(yujsdbm);
    }

    //结算扣吨量取值
    public String getJieskdl() {
        return ((Visit) this.getPage().getVisit()).getString7();
    }

    public void setJieskdl(String jieskdl) {
        ((Visit) this.getPage().getVisit()).setString7(jieskdl);
    }

    //结算扣款金额取值
    public String getJieskkje() {
        return ((Visit) this.getPage().getVisit()).getString18();
    }

    public void setJieskkje(String jieskkje) {
        ((Visit) this.getPage().getVisit()).setString18(jieskkje);
    }

    //	Qnet,ar上限
    private String _Qu = "";

    public String getQu() {
        return _Qu;
    }

    public void setQu(String qu) {

        _Qu = qu;
    }

    //	Qnet,ar下限
    private String _Qd = "";

    public String getQd() {
        return _Qd;
    }

    public void setQd(String qd) {
        _Qd = qd;
    }

    //	Std上限
    private String _Stdu = "";

    public String getStdu() {
        return _Stdu;
    }

    public void setStdu(String su) {
        _Stdu = su;
    }

    //	Std下限
    private String _Stdd = "";

    public String getStdd() {
        return _Stdd;
    }

    public void setStdd(String sd) {
        _Stdd = sd;
    }

    //	Star上限
    private String _Staru = "";

    public String getStaru() {
        return _Staru;
    }

    public void setStaru(String su) {
        _Staru = su;
    }

    //	Star下限
    private String _Stard = "";

    public String getStard() {
        return _Stard;
    }

    public void setStard(String sd) {
        _Stard = sd;
    }

    //货票核对状态
    public String getHuopztvalue() {
        return ((Visit) this.getPage().getVisit()).getString9();
    }

    public void setHuopztvalue(String huopztvalue) {
        ((Visit) this.getPage().getVisit()).setString9(huopztvalue);
    }

    // 页面变化记录
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    private boolean _RefurbishChick = false;

    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
        if(this.getYunsdwValue().getId()==-1){
            this.getYunsdwModels();
        }
    }

    private void Refurbish() {
        //为  "刷新"  按钮添加处理程序
//    	getSelectData();
        ((Visit) getPage().getVisit()).setboolean4(true);    //点击“刷新”按钮
    }

    //
    private boolean _JiesChick = false;

    public void JiesButton(IRequestCycle cycle) {
        _JiesChick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }

        if (_JiesChick) {
            _JiesChick = false;
             GotoJiesd(cycle);


        }
    }

    private void gotoYunfjsd(IRequestCycle cycle) {
        cycle.activate("Yunfjsd");
    }

    private void GotoJiesd(IRequestCycle cycle) {
        try {
            ExtGridUtil egu = this.getExtGrid();
            ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
            String mstr_lieid = "";
            String mstrchaij_lieid = "";
            String mstr_jieszbtz = "否";
            while (mdrsl.next()) {
                if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                        || getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                        || getJieslxValue().getId() == Locale.dityf_feiylbb_id
                        || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id
                        ) {//两票结算、国铁运费、地铁
                    if (mdrsl.getString("HUOPHDZT").equals("已完成")) {
                        mstr_lieid += mdrsl.getString("ID") + ",";
                    } else if (!mdrsl.getString("YIHD_CHES").equals("0")) {
//        			要拆分的列
                        if (getHuopztvalue().equals("weiwc")) {
                            mstrchaij_lieid = mdrsl.getString("ID");
                            setChaifl(mstrchaij_lieid);
                            mstr_lieid += ((Visit) getPage().getVisit()).getString8() + ",";
                        } else {
                            this.setMsg("还未核对货票，不能进行结算");
                            return;
                        }
                    } else {
//                        if (getHuopztvalue().equals("weiwc")) {
                            mstr_lieid += mdrsl.getString("ID") + ",";
//                        } else {
//                            this.setMsg("还未核对货票，不能进行结算");
//                            return;
//                        }
                    }
                } else {
                    mstr_lieid += mdrsl.getString("ID") + ",";
                }
            }
//    	从所选的树中得到第一个节点id
            String gongysb_id = getGongysmlttree_id();

            if (gongysb_id.equals("")) {

                gongysb_id = "0";
            }

            if (gongysb_id.indexOf(",") > -1) {

                gongysb_id = gongysb_id.substring(0, gongysb_id.indexOf(","));
            }

//    	电厂Id赋值

//    	增加对一厂多制“一厂多制单厂结算多厂”的支持，将系统设置中的“一厂多制主结算厂ID”当成结算单位id
//    		传入后面结算计算类


            if (((Visit) getPage().getVisit()).isFencb()) {
//
                ((Visit) getPage().getVisit()).setLong1(MainGlobal.getProperId(this.getFencbModel(), this.getMainjsdw()));
            } else {

                ((Visit) getPage().getVisit()).setLong1(((Visit) getPage().getVisit()).getDiancxxb_id());
            }

//    	发货的Lie_Id
            ((Visit) getPage().getVisit()).setString1(mstr_lieid.substring(0, mstr_lieid.lastIndexOf(",")));

//    	发货Id
            ((Visit) getPage().getVisit()).setString13(Jiesdcz.getFahb_id_FromLie_id(sql, mstr_lieid.substring(0, mstr_lieid.lastIndexOf(","))));

//    	wangzb,大同使用,setString21也记录发货Id,防止在结算单保存时getString13得不到fahb_id时,可以用getString21();
            ((Visit) getPage().getVisit()).setString21(Jiesdcz.getFahb_id_FromLie_id(sql, mstr_lieid.substring(0, mstr_lieid.lastIndexOf(","))));

//    	结算类型（两票、煤款、运费...）
            ((Visit) getPage().getVisit()).setLong2(this.getJieslxValue().getId());

            //发货单位id
            ((Visit) getPage().getVisit()).setLong3(Long.parseLong(MainGlobal.getLeaf_ParentNodeId(getTree(), gongysb_id)));

            //结算时的扣吨量（该部分数量已记入库存量）
            if (((Visit) getPage().getVisit()).getString7().trim().equals("")) {

                ((Visit) getPage().getVisit()).setString7("0");
            }

            if (((Visit) getPage().getVisit()).getString18().trim().equals("")) {

                ((Visit) getPage().getVisit()).setString18("0");
            }

//    	结算时的上次结算量（为了累计起来算数量折价用）
            if (this.getScjsl().trim().equals("")) {

                ((Visit) getPage().getVisit()).setString12("0");
            } else {

                ((Visit) getPage().getVisit()).setString12(this.getScjsl());
            }

//    	记录下结算单位，该单位可能是分公司id，也可能是电厂id，专为处理统一结算时使用
            if (((Visit) getPage().getVisit()).getString15().equals("")) {

                if (((Visit) getPage().getVisit()).isFencb()) {

                    ((Visit) getPage().getVisit()).setString15(String.valueOf(MainGlobal.getProperId(this.getFencbModel(), this.getMainjsdw())));

                } else {

                    ((Visit) getPage().getVisit()).setString15(String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
                }
            }

//    	记录页面选择的燃料品种ID
            if (!"".equals(this.getPinz())) {

                try {
                    ((Visit) getPage().getVisit()).setString19(String.valueOf(MainGlobal.getTableId("pinzb", "mingc", this.getPinz())));
                } catch (Exception e) {
                    // TODO 自动生成 catch 块
                    ((Visit) getPage().getVisit()).setString19("0");
                    e.printStackTrace();
                }
            }
//    	记录预结算单的结算编号
            if (!this.getYujsdbm().equals("")) {
                ((Visit) getPage().getVisit()).setString20(this.getYujsdbm());
            }
//    	更新选中发货表的hetb_id为getHthValue().getId()
            if (setFahb_HetbId(((Visit) getPage().getVisit()).getString1(), this.getHthValue().getId())) {

//    		将Hetb_id存入Long8
                ((Visit) getPage().getVisit()).setLong8(this.getHthValue().getId());

//    		为防止gongysb_id=0取结算设置方案时出错，特加入gongysb_id重新赋值的语句
                ((Visit) getPage().getVisit()).setLong3(Jiesdcz.getGongysb_id(((Visit) getPage().getVisit()).getString1(),
                        ((Visit) getPage().getVisit()).getLong1(), ((Visit) getPage().getVisit()).getLong3(),
                        ((Visit) getPage().getVisit()).getLong8(), Double.parseDouble(((Visit) getPage().getVisit()).getString7())));

                mstr_jieszbtz = Jiesdcz.getJiessz_item(((Visit) getPage().getVisit()).getLong1(),
                        ((Visit) getPage().getVisit()).getLong3(), this.getHthValue().getId(), Locale.jieszbtz_jies, mstr_jieszbtz);

//    		煤款所有结算单都走指标调整
                String flag_mstr = MainGlobal.getXitxx_item("结算", Locale.jieszbtz_jies, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
                if ("是".equals(flag_mstr)) {
                    String sql = "select leib from feiylbb f where f.id=" + getJieslxValue().getId();
                    JDBCcon con = new JDBCcon();
                    ResultSetList rsl1 = con.getResultSetList(sql);
                    if (rsl1.next()) {
                        if (rsl1.getInt("leib") == 0) {
                            mstr_jieszbtz = flag_mstr;
                        }
                    }
                    con.Close();
                }

                ((Visit) getPage().getVisit()).setString2(mstr_jieszbtz);

//    		运输单位表_id
                ((Visit) getPage().getVisit()).setLong9(this.getYunsdwValue().getId());

//    		判断是否需要指标调整
                if (mstr_jieszbtz.equals("否")) {
                    if (((Visit) getPage().getVisit()).getString17().trim().equals("tb")) {
//	    			如果结算方式为填报，则跳转到 DCBalance_tb
                        cycle.activate("DCBalance_tb");
                    } else {
                        if (MainGlobal.getXitxx_item("数量", "是否显示过衡系统下拉框", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
//	    				说明是山西阳城电厂的运费结算问题，需要将选择的过衡系统(A系统、B系统)保存起来。
                            ((Visit) getPage().getVisit()).setString19(getGuohxtValue().getValue());
                        }
                        cycle.activate("DCBalance");
                    }
                } else {
                    if (((Visit) getPage().getVisit()).getString17().trim().equals("tb")) {
//	    			如果结算方式为填报，则跳转到 DCBalance_tb
                        cycle.activate("DCBalance_tb");
                    }
//	    		需要指标调整
//	    		1、系统信息中不使用验收编号
                    ((Visit) getPage().getVisit()).setString4("");
//	    		2、系统信息中使用验收编号
                    ((Visit) getPage().getVisit()).setString4((this.getYansbh().equals("") ? "" : this.getYansbh()));
                    String s = MainGlobal.getXitxx_item("结算", "结算大同专用", ((Visit) getPage().getVisit()).getDiancxxb_id(), "");
                    if ("是".equals(s)) {
                        JDBCcon con = new JDBCcon();
                        con.setAutoCommit(false);
                        try {
                            String sql0 = "select getnewid(300) as yansbhb_id from dual";
                            //得到一个yansbhb的id
                            ResultSetList rsl0 = con.getResultSetList(sql0);
                            rsl0.next();
                            //插入验收编号表
                            String sql = "insert into yansbhb (id,bianm,lie_id) values(" + rsl0.getLong("yansbhb_id") + "," + MainGlobal.getYansbh() + ",'" + ((Visit) getPage().getVisit()).getString1() + "')";

                            con.getInsert(sql);
                            //	    		//更新fahb的yansbhb_id
                            String sql1 = "update fahb f set f.yansbhb_id=" + rsl0.getLong("yansbhb_id") + " where lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")";
                            con.getUpdate(sql1);
                            //插入jieszbsjb
                            mstr_jieszbtz = MainGlobal.getXitxx_item("结算", "结算数量组成方式", ((Visit) getPage().getVisit()).getDiancxxb_id(), "maoz-piz-koud");

                            String sql2 = "select sum(" + mstr_jieszbtz + ") from chepb c where c.fahb_id in(select id from fahb f where f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + "))";
                            String sql3 = "select round(sum((" + mstr_jieszbtz + ")*qnet_ar)/sum(" + mstr_jieszbtz + "),2) " +
                                    "from chepb c,(select f.id fid,z.* from fahb f,zhilb z " +
                                    "				where  f.zhilb_id=z.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")) g " +
                                    "where c.fahb_id=g.fid";

                            String sql4 = "select round(sum((" + mstr_jieszbtz + ")*stad)/sum(" + mstr_jieszbtz + "),2) " +
                                    "from chepb c,(select f.id fid,z.* from fahb f,zhilb z " +
                                    "				where  f.zhilb_id=z.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")) g " +
                                    "where c.fahb_id=g.fid";

                            String sql5 = "select round(sum((" + mstr_jieszbtz + ")*mt)/sum(" + mstr_jieszbtz + "),2) " +
                                    "from chepb c,(select f.id fid,z.* from fahb f,zhilb z " +
                                    "				where  f.zhilb_id=z.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")) g " +
                                    "where c.fahb_id=g.fid";

                            String sql6 = "select sum(c.biaoz) from fahb f,chepb c where c.fahb_id=f.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")";

                            String sql7 = "begin \n" +
                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,1,(" + sql6 + "),(" + sql2 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n" +

                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,2,0,(" + sql3 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n" +

                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,13,0,(" + sql4 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n" +

                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,6,0,(" + sql5 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n end;";
                            con.getInsert(sql7);
                            con.commit();
                        } catch (Exception e) {
                            con.rollBack();
                        } finally {
                            con.Close();
                        }

                    }


                    cycle.activate("Jieszbtz");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setChaifl(String mstrchaij_lieid) {
        // TODO 自动生成方法存根
//		拆分列，找到要拆分的列
        JDBCcon con = new JDBCcon();
        try {

            long lngFahb_id = 0;
            String sql = "select id from fahb f where f.lie_id=" + mstrchaij_lieid;
            ResultSetList rsl = con.getResultSetList(sql);
            while (rsl.next()) {

                lngFahb_id = rsl.getLong("id");
                Chaif(lngFahb_id);
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    private void Chaif(long lngFahb_id) {
//		实际拆分发货表
        JDBCcon con = new JDBCcon();
        try {
            String strNew_FahbId = "";
            StringBuffer sqlbf = new StringBuffer("begin 		\n");

//			1、首先备份要拆分的发货,生成新的发货
            strNew_FahbId = Jilcz.CopyFahb(con, String.valueOf(lngFahb_id), ((Visit) getPage().getVisit()).getDiancxxb_id());
            sqlbf.append(" update fahb set lie_id=" + strNew_FahbId + " where id=" + strNew_FahbId + "; 	\n");


//			2、更新已核对的chepb的fahb_id为新发货表id
            sqlbf.append(" update chepb set fahb_id=" + strNew_FahbId + " where id in (select cp.id from chepb cp,fahb f,danjcpb dj 	\n");
            sqlbf.append(" 		where f.id=cp.fahb_id and dj.chepb_id=cp.id and f.id=" + lngFahb_id + ");	\n");
            sqlbf.append("end;	");

            con.getUpdate(sqlbf.toString());

//			3、更新新发货表
            Jilcz.updateFahb(con, strNew_FahbId);

//			4、更新老发货表
            Jilcz.updateFahb(con, String.valueOf(lngFahb_id));

            if (((Visit) getPage().getVisit()).getString8().equals("")) {

                ((Visit) getPage().getVisit()).setString8(strNew_FahbId);
            } else {

                ((Visit) getPage().getVisit()).setString8("," + strNew_FahbId);
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    private boolean setFahb_HetbId(String FahbSelLieId, long HetbId) {
        // TODO 自动生成方法存根
//		更新选中发货表的hetb_id为getHthValue().getId()
        boolean flag = true;
        if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || getJieslxValue().getId() == Locale.meikjs_feiylbb_id
                ) {
            JDBCcon con = new JDBCcon();
            flag = false;
            String sql = "update fahb set hetb_id=" + HetbId + " where lie_id in (" + FahbSelLieId + ")";
            if (con.getUpdate(sql) >= 0) {

                flag = true;
            }
            con.Close();
        }

        return flag;
    }

    public void getSelectData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
//		 ********************工具栏************************************************
        String biaoq = "发货日期";
        String riq = "fahrq";
        String danx = "";
        String danx1 = "";
        String yihd = "";
        String weihd = "";
        String fencb = "";
        String yansbh = "";
        String huophdzt = "";
        String yunsdw = "";
        String pinz = "";
        String yujsdbm = "";
        String shangcjsl = "";    //上次结算量
        String tiaoj_returnvalue_changb = "";
        String tiaoj_returnvalue_yansbh = "";
        String tiaoj_returnvalue_huophdzt = "";
        String tiaoj_returnvalue_yunsdw = "";
        String tiaoj_returnvalue_pinz = "";
        String tiaoj_returnvalue_yujsdbm = "";
        String tiaoj_returnvalue_shangcjsl = "";
        String where_rownum = "";
        String jieskdl = " ,{  \n"
                + "	\titems:jieskdl=new Ext.form.Field({	\n"
                + "	\twidth:100,	\n"
                + " \tfieldLabel: '结算扣吨量',\n"
                + " \tselectOnFocus:true,	\n"
                + " \tvalue:document.getElementById('Jieskdl').value,	\n"
                + " \ttransform:'Jieskdl',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Jieskdl').selectedIndex=index}}"
                + "	\t})}";

        String zhi = "否";
        if (getJieslxValue().getValue().equals(Locale.guotyf_feiylbb) || getJieslxValue().getValue().equals(Locale.dityf_feiylbb)) {
            zhi = MainGlobal.getXitxx_item("数量", "是否显示过衡系统下拉框", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
        }
//
//        String Qnet =
//        	",{ \n" +
//        	" \t\titems: [{\n" +
//        	" \t\t\txtype:'textfield',\n" +
//        	" \t\t\tfieldLabel:'Qnet,ar(mj/kg)',\n" +
//        	"\t\t\twidth:0\n" +
//        	"\t\t } ]}";
//

        //region EXT
        String jieskkje = " ,{  \n"
                + "	\titems:jieskkje=new Ext.form.Field({	\n"
                + "	\twidth:100,	\n"
                + " \tfieldLabel: '结算扣款金额',\n"
                + " \tselectOnFocus:true,	\n"
                + " \tvalue:document.getElementById('Jieskkje').value,	\n"
                + " \ttransform:'Jieskkje',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Jieskkje').selectedIndex=index}}"
                + "	\t})}";

        String Qd =
                ",{\n" +
                        "\t\t\titems:qd=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Qnet,ar(mj/kg)下限',\n" +
                        " \t\t\tvalue:document.getElementById('Qd').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Qd',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tqd.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Qu =
                ",{\n" +
                        "\t\t\titems:qu=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Qnet,ar(mj/kg)上限',\n" +
                        " \t\t\tvalue:document.getElementById('Qu').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Qu',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tqu.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Stard =
                ",{\n" +
                        "\t\t\titems:stard=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Star(%)下限',\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\tvalue:document.getElementById('Stard').value,	\n" +
                        " \t\t\ttransform:'Stard',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstard.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Staru =
                ",{\n" +
                        "\t\t\titems:staru=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Star(%)上限',\n" +
                        " \t\t\tvalue:document.getElementById('Staru').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Staru',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstaru.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";
        String Stdd =
                ",{\n" +
                        "\t\t\titems:stdd=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Std(%)下限',\n" +
                        " \t\t\tvalue:document.getElementById('Stdd').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Stdd',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstdd.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Stdu =
                ",{\n" +
                        "\t\t\titems:stdu=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Std(%)上限',\n" +
                        " \t\t\tvalue:document.getElementById('Stdu').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Stdu',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstdu.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";
        //endregion


        if (getRbvalue().equals("fahrq") || this.getRbvalue().equals("")) {


            biaoq = "发货日期";
            riq = "fahrq";
            danx = "checked:true ,   \n";
        } else if (getRbvalue().equals("daohrq")) {
            biaoq = "到货日期";
            riq = "daohrq";
            danx1 = "checked:true ,   \n";

        }
//        if(visit.isFencb()==true){


        //大同特殊处理，当选择主结算单位为国电大同二期或国电大同三期时，厂别选择电大同二期和国电大同三期
        StringBuffer listeners = new StringBuffer();
        String dtsqlString = "SELECT ID FROM xitxxb WHERE mingc = '大同结算条件特殊处理' AND zhi = '是'";
        ResultSetList rslxitxx = con.getResultSetList(dtsqlString);
        if (rslxitxx.next()) {
            listeners.append("   ,listeners:{                                               \n");
            listeners.append("     'select':function(own,rec,index){                        \n");
            listeners.append("        if (own.getValue()== '301'){                          \n");
            listeners.append("            Changb.setValue('国电大同一期');                  \n");
            listeners.append("        } else {                                              \n");
            listeners.append("           Changb.setValue('国电大同二期,国电大同三期');      \n");
            listeners.append("        }                                                     \n");
            listeners.append("    }                                                         \n");
            listeners.append("   }                                                          \n");
        }
        fencb =
                ",{items:Changb=new Ext.zr.select.Selectcombo({\n" +
                        "   multiSelect:true,\n" +
                        "   width:100,\n" +
                        "   fieldLabel: '厂别',\n" +
                        "   transform:'FencbDropDown',\n" +
                        "   lazyRender:true,\n" +
                        "   triggerAction:'all',\n" +
                        "   typeAhead:true,\n" +
                        "   forceSelection:true \n" +
                        "})},\n" +
                        "{items:Mainjsdw=new Ext.form.ComboBox({\n" +
                        "\n" +
                        "   width:100,\n" +
                        "   fieldLabel: '主结算单位',\n" +
                        "   selectOnFocus:true,\n" +
                        "   transform:'MainjsdwDropDown',\n" +
                        "   lazyRender:true,\n" +
                        "   triggerAction:'all',\n" +
                        "   typeAhead:true,\n" +
                        "   forceSelection:true,\n" +
                        "   editable:false\n" +
                        listeners.toString() +
                        "})}";


        tiaoj_returnvalue_changb = "	document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue();	\n"
                + "	document.getElementById('TEXT_MAINJSDW_VALUE').value=Mainjsdw.getRawValue();	\n";


        shangcjsl = ",{\n" +
                "      items:scjsl=new Ext.form.Field({\n" +
                "      width:100,\n" +
                "      fieldLabel: '上次结算量(吨)',\n" +
                "      selectOnFocus:true,\n" +
                "	   value:document.getElementById('Scjsl').value, \n" +
                "      transform:'Scjsl',\n" +
                "      lazyRender:true,\n" +
                "      triggerAction:'all',\n" +
                "      typeAhead:true,\n" +
                "      forceSelection:true,\n" +
                " \t\t\teditable:false,\n" +
                "\t\t\tlisteners:{'change':function(own,rec,index){\n" +
                "\t\t        \t\tvar obj1 = own.getRawValue();\n" +
                "\t\t      \t\t\tvar num = /^[0-9]{0,7}\\.[0-9]{0,4}$|^[0-9]{0,7}$/;\n" +
                "\t\t\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                "\t\t\t\t\t\t\tscjsl.setRawValue(0);\n" +
                "\t\t\t\t\t\t}\n" +
                "      \t\t\t\t}\n" +
                "\t\t\t}\n" +
                "})}";

        tiaoj_returnvalue_shangcjsl = " document.getElementById('Scjsl').value=scjsl.getRawValue();	\n";

//       }else{
//        	fencb="";
//        	tiaoj_returnvalue_changb="";
//        }
//    	if(MainGlobal.getXitxx_item("结算", Locale.shiyysbh_jies, String.valueOf(visit.isFencb()?MainGlobal.getProperId(getFencbModel(), this.getChangb()):visit.getDiancxxb_id()), "否").equals("是")){
        tiaoj_returnvalue_yansbh = "document.getElementById('TEXT_YANSBH_VALUE').value=yansbh.getRawValue();";
        yansbh = "	,{\n"
                + " \titems:yansbh=new Ext.form.ComboBox({	\n"
                + "	\twidth:100,	\n"
                + " \tfieldLabel: '验收编号',\n"
                + " \tselectOnFocus:true,	\n"
                + " \ttransform:'YansbhDropDown',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YansbhDropDown').selectedIndex=index}}"
                + "	\t})}";
//			}else{
//				yansbh="";
//			}
//         if(this.getJieslxValue().getId()==Locale.liangpjs_feiylbb_id
//        		 ||this.getJieslxValue().getId()==Locale.guotyf_feiylbb_id){

//        	 yunsdw=",{		\n"
//        		 + " xtype:'textfield',	\n"
//        		 + " fieldLabel:'运输单位',	\n"
//        		 + " width:0	\n"
//        		 + "}	\n";

        yunsdw = "	/*,{\n"
                + "items:YunsdwCb=new Ext.form.ComboBox({	\n"
                + "	width:100,	\n"
                + " fieldLabel: '运输单位',\n"
                + "	selectOnFocus:true,		\n"
                + "	transform:'YunsdwDropDown',		\n"
                + "	lazyRender:true,		\n"
                + " triggerAction:'all',	\n"
                + " typeAhead:true,		\n"
                + "	forceSelection:true,	\n"
                + "	editable:false		\n"
                + "	})}*/";

        tiaoj_returnvalue_yunsdw = "document.getElementById('TEXT_YUNSDW_VALUE').value=YunsdwCb.getRawValue();";

//        	 pinz=",{		\n"
//        		 + " xtype:'textfield',	\n"
//        		 + " fieldLabel:'品种',	\n"
//        		 + " width:0	\n"
//        		 + "}	\n";

        pinz = "	,{\n"
                + "items:PinzCb=new Ext.form.ComboBox({	\n"
                + "	width:100,	\n"
                + " fieldLabel: '品种',\n"
                + "	selectOnFocus:true,		\n"
                + "	transform:'PinzDropDown',		\n"
                + "	lazyRender:true,		\n"
                + " triggerAction:'all',	\n"
                + " typeAhead:true,		\n"
                + "	forceSelection:true,	\n"
                + "	editable:false		\n"
                + "	})}";

        tiaoj_returnvalue_pinz = "document.getElementById('TEXT_PINZ_VALUE').value=PinzCb.getRawValue();";

        tiaoj_returnvalue_yujsdbm = "document.getElementById('TEXT_YUJSDBM_VALUE').value=yujsdbm.getRawValue();";
        yujsdbm = "	,{\n"
                + " \titems:yujsdbm=new Ext.form.ComboBox({	\n"
                + "	\twidth:100,	\n"
                + "	\tlistWidth:220,	\n"
                + " \tfieldLabel: '预结算单编码',\n"
                + " \tselectOnFocus:true,	\n"
                + " \ttransform:'YujsdbmDropDown',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YujsdbmDropDown').selectedIndex=index}}"
                + "	\t})}";

        if (visit.getDiancxxb_id() == 323) {
            if (getHuopztvalue().equals("yiwc")) {
                yihd = "checked:true ,   \n";
            } else if (getHuopztvalue().equals("weiwc") || this.getHuopztvalue().equals("")) {
                weihd = "checked:true ,   \n";
            }
        } else {
            if (getHuopztvalue().equals("yiwc") || this.getHuopztvalue().equals("")) {
                yihd = "checked:true ,   \n";
            } else if (getHuopztvalue().equals("weiwc")) {
                weihd = "checked:true ,   \n";
            }
        }
        huophdzt = " ,{items: [{  \n"
                + " \txtype:'textfield',\n"
                + " \tfieldLabel:'货票核对状态',\n"
                + "	\twidth:0	\n"
                + " },	\n"
                + " { \n"
                + "     xtype:'radio', \n"
                + "		boxLabel:'已核对', \n"
                + "     anchor:'95%', \n"
                + yihd
                + "     Value:'yiwc', \n"
                + "		name:'r2',\n"
                + "		listeners:{ \n"
//				+ "				'focus':function(r,c){\n"
//				+ "					document.getElementById('huopztvalue').value=r.Value;\n"
//				+ "				},\n"
                + "				'check':function(r,c){ \n"
                + "					var val='weiwc';if(c) val='yiwc';"
                + "					document.getElementById('huopztvalue').value=val;\n"
                + "					\tif(document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value==''){	\n"
                + " 				\t	document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;} \n"
                + "					  }\n"
                + "		} \n"

                + "	},\n"
                + "	{  \n"
                + "     xtype:'radio', \n"
                + "		boxLabel:'未核对',\n"
                + "		Value:'weiwc', \n"
                + "     anchor:'95%',	\n"
                + weihd
                + "		name:'r2'"
                + ",\n"
                + "		listeners:{ \n"
//				+ "				'focus':function(r,c){ \n"
//				+ "					document.getElementById('huopztvalue').value=r.Value;\n"
//				+ "				}, \n"
                +
                "'check':function(r,c){\n" +
                "            var val='yiwc';if(c) val='weiwc';\n" +
                "            document.getElementById('huopztvalue').value=val;\n" +
                "              if(document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value==''){\n" +
                "                 document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;\n" +
                "               }\n" +
                "        }"
                + "		}	\n"
                + "}]}";

        tiaoj_returnvalue_huophdzt = "document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;";
//         }else{
//        	 huophdzt="";
//         }


        String Strtmpfunction =
                "var form =new Ext.FormPanel({\n" +
                        "\tlabelAlign:'left',buttonAlign:'right',bodyStyle:'padding:5px;',\n" +
                        "    frame:true,labelWidth:115,monitorValid:true,\n" +
                        "    items:[{\n" +
                        "      layout:'column',border:false,labelSeparator:':',\n" +
                        "        defaults:{layout: 'form',border:false,columnWidth:.5},\n" +
                        "        items:[\n" +
                        "              {items: [{\n" +
                        "              xtype:'textfield',\n" +
                        "              fieldLabel:'日期选择',\n" +
                        "              width:0\n" +
                        "            },{xtype:'radio',\n" +
                        "                    boxLabel:'发货日期',\n" +
                        "               anchor:'95%',\n" +
                        danx +
                        "               Value:'fahrq',\n" +
                        "            name:'test',\n" +
                        "            listeners:{\n" +
                        "                  'focus':function(r,c){\n" +
                        "                  document.getElementById('rbvalue').value=r.Value;\n" +
                        "                },\n" +
                        "                'check':function(r,c){\n" +
                        "                    document.getElementById('rbvalue').value=r.Value;\n" +
                        "                    if(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){\n" +
                        "                     document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;}\n" +
                        "                }\n" +
                        "            }\n" +
                        "          },{\n" +
                        "            xtype:'radio',\n" +
                        "            boxLabel:'到货日期',\n" +
                        "            Value:'daohrq',\n" +
                        "               anchor:'95%',\n" +
                        danx1 +
                        "            name:'test',\n" +
                        "            listeners:{\n" +
                        "                'focus':function(r,c){\n" +
                        "                document.getElementById('rbvalue').value=r.Value;\n" +
                        "            },\n" +
                        "            'check':function(r,c){\n" +
                        "                document.getElementById('rbvalue').value=r.Value;\n" +
                        "                if(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){\n" +
                        "                 document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;}\n" +
                        "            }\n" +
                        "          }\n" +
                        "        }]}\n" +
                        yansbh +
                        yunsdw +
                        pinz +
                        huophdzt +
                        jieskdl +
                        jieskkje +
//	    	Qnet +
                        Qd +
                        Qu +
                        Stdd +
                        Stard +
                        Stdu +
                        Staru +
                        fencb +
                        shangcjsl +
                        yujsdbm +
                        "\n" +
                        "        ]//items\n" +
                        "      }]//items\n" +
                        "});//FormPanel\n"


                        + " win = new Ext.Window({\n"
                        + " el:'hello-win',\n"
                        + "layout:'fit',\n"
                        + "width:500,\n"
                        + "height:420,\n"
                        + "closeAction:'hide',\n"
                        + "plain: true,\n"
                        + "title:'条件',\n"
                        + "modal:true,"
                        + "items: [form],\n"

                        + "buttons: [{\n"
                        + "   text:'确定',\n"
                        + "   handler:function(){  \n"
                        + "  		win.hide();\n"
                        + tiaoj_returnvalue_changb + "\n"
                        + tiaoj_returnvalue_yansbh + "\n"
                        + tiaoj_returnvalue_yujsdbm + "\n"
                        + "      	document.getElementById('Jieskdl').value=jieskdl.getRawValue();\n"
                        + "      	document.getElementById('Jieskkje').value=jieskkje.getRawValue();\n"
                        + "      	document.getElementById('Qd').value=qd.getRawValue();\n"
                        + "      	document.getElementById('Qu').value=qu.getRawValue();\n"
                        + "      	document.getElementById('Stdd').value=stdd.getRawValue();\n"
                        + "      	document.getElementById('Stdu').value=stdu.getRawValue();\n"
                        + "      	document.getElementById('Stard').value=stard.getRawValue();\n"
                        + "      	document.getElementById('Staru').value=staru.getRawValue();\n"
                        + tiaoj_returnvalue_shangcjsl + "\n"
                        + tiaoj_returnvalue_huophdzt + "\n"
                        + tiaoj_returnvalue_yunsdw + "\n"
                        + tiaoj_returnvalue_pinz + "\n"
                        + tiaoj_returnvalue_yujsdbm + "\n"
                        + "			document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
                        + " 		document.getElementById('RefurbishButton').click(); \n"
                        + "  	}   \n"
                        + "},{\n"
                        + "   	text: '取消',\n"
                        + "   	handler: function(){\n"
                        + "		win.hide();\n"
                        + "		document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_MAINJSDW_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_YANSBH_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_YUNSDW_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_PINZ_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_YUJSDBM_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value='';	\n"
                        + "		document.getElementById('Jieskdl').value='';  \n"
                        + "		document.getElementById('Jieskkje').value='';  \n"
                        + "		document.getElementById('Qd').value='';  \n"
                        + "		document.getElementById('Qu').value='';  \n"
                        + "		document.getElementById('Stdd').value='';  \n"
                        + "		document.getElementById('Stdu').value='';  \n"
                        + "		document.getElementById('Stard').value='';  \n"
                        + "		document.getElementById('Staru').value='';  \n"
                        + "		document.getElementById('Scjsl').value='';  \n"
                        + "   }\n"
                        + "}]\n"

                        + " });win.show();win.hide();";


//      读取结算设置表中结算设置参数
//      	1、结算数量
//        	2、结算加权数量
//        	3、结算显示指标
//        	4、结算数量保留小数位
//        	5、结算数量取整方式
//        	6、Mt保留小数位
//        	7、Mad保留小数位
//        	8、Aar保留小数位
//        	9、Aad保留小数位
//        	10、Adb保留小数位
//        	11、Vad保留小数位
//        	12、Vdaf保留小数位
//        	13、Stad保留小数位
//        	14、Std保留小数位
//        	15、Had保留小数位
//        	16、Qnetar保留小数位
//        	17、Qbad保留小数位
//        	18、Qgrad保留小数位
//        	19、结算指标调整

        String jies_Jssl = "f.biaoz+f.yingk";
        String jies_Jqsl = "jingz";
        String jies_Jsxszb = "Mt,Qnetark,Std";
        String jies_Jsslblxs = "0";
        String jies_Jieslqzfs = "sum(round())";
        String jies_Mtblxs = "1";
        String jies_Madblxs = "2";
        String jies_Aarblxs = "2";
        String jies_Aadblxs = "2";
        String jies_Adblxs = "2";
        String jies_Vadblxs = "2";
        String jies_Vdafblxs = "2";
        String jies_Stadblxs = "2";
        String jies_Starblxs = "2";
        String jies_Stdblxs = "2";
        String jies_Hadblxs = "2";
        String jies_Qnetarblxs = "2";
        String jies_Qbadblxs = "2";
        String jies_Qgradblxs = "2";

        long feiylbb_id = 0;
        String strDiancxxb_id = "0";

        if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id) {
//				费用类别转换，如果是两票结算或是国铁结算那么统统转换为国铁运费。
            feiylbb_id = Locale.guotyf_feiylbb_id;
        } else {

            feiylbb_id = getJieslxValue().getId();
        }

        String YunsfsWhere = "";
        if (!((Visit) getPage().getVisit()).getString11().equals("")) {

            if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_tiel)) {
//	    		   铁路
                YunsfsWhere = " and f.yunsfsb_id=1 ";
            } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_gongl)) {
//	    		   公路
                YunsfsWhere = " and f.yunsfsb_id=2 ";
            } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_haiy)) {
//	    		   海运
                YunsfsWhere = " and f.yunsfsb_id=3 ";
            } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_pidc)) {
//	    		   皮带秤
                YunsfsWhere = " and f.yunsfsb_id=4 ";
            }
        }

        if (((Visit) getPage().getVisit()).getboolean4()) {
//	    	   点击“刷新”按钮
            where_rownum = "";
            ((Visit) getPage().getVisit()).setboolean4(false);
        } else {

            where_rownum = "	and rownum=0 \n";
        }
        String sql_tmp = "";
        if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//	    	   倒装运费结算
            //region 倒装运费结算
            sql =

                    " select f.lie_id as id,decode(g.mingc,null,'合计',g.mingc) as fahdw,m.mingc as meikdw,		\n" +
                            " 		decode(min(to_char(f.fahrq,'yyyy-MM-dd')),max(to_char(f.fahrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.fahrq,'yyyy-MM-dd'))) as fahrq ," +
                            " 		decode(min(to_char(f.daohrq,'yyyy-MM-dd')),max(to_char(f.daohrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.daohrq,'yyyy-MM-dd'))) as daohrq ," +
                            "       c.mingc as faz,decode(g.mingc,null,'',count(cp.id)) as ches,decode(g.mingc,null,'',sum(cp.biaoz)) as biaoz,	\n" +
                            "		decode(g.mingc,null,'',sum(cp.yingk)) as yingk," +
                            "		decode(g.mingc,null,'',sum(cp.yuns)) as yuns,	\n" +
                            "       decode(g.mingc,null,'',sum(" + Locale.jing_jiesxz + ")) as jingz,decode(g.mingc,null,'',h.hetbh) as hetbh,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qnetar,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + ")*1000/4.1816,0))) as Qnetark,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz)," + jies_Stdblxs + "))) as Std,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz)," + jies_Stadblxs + "))) as Stad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.star)/sum(f.jingz)," + jies_Starblxs + "))) as Star,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz)," + jies_Vdafblxs + "))) as Vdaf,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz)," + jies_Mtblxs + "))) as Mt,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mad)/sum(f.jingz)," + jies_Madblxs + "))) as Mad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz)," + jies_Aarblxs + "))) as Aar,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aad)/sum(f.jingz)," + jies_Aadblxs + "))) as Aad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.ad)/sum(f.jingz)," + jies_Adblxs + "))) as Ad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz)," + jies_Vadblxs + "))) as Vad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.had)/sum(f.jingz)," + jies_Hadblxs + "))) as Had,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qbad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qbad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qgrad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qgrad\n" +
                            "       from fahb f,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,daozcpb cp,yansbhb ys \n" + this.getSupplyTable(riq) + " \n" +
                            "       where f.gongysb_id=g.id and f.meikxxb_id=m.id 						\n" +
                            "       and f.hetb_id=h.id(+) and f.faz_id=c.id 							\n" +
                            "		and f.id=cp.fahb_id 												\n" +
                            "       and f.liucztb_id=1													\n" +
                            "		and f.yansbhb_id = ys.id(+)											\n" +
                            where_rownum +
//	     			运输方式条件
                            YunsfsWhere +
                            this.getWhere(riq) +
                            this.getSupplyWhere() +
                            "       group by rollup(f.lie_id,g.mingc,m.mingc,h.hetbh,c.mingc)	\n" +
                            "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                            "       order by g.mingc,fahrq";
            //endregion


        } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_haiy)) {
//	    	   如果运输方式是海运,需要调整页面显示的值，增加 船名、航次 ，将发站改为港口 去掉车数

            //region Description
            sql = " select f.lie_id as id,decode(g.mingc,null,'合计',g.mingc) as fahdw,m.mingc as meikdw,lcxx.mingc as chuanm,f.chec as hangc,		\n" +
                    " 		decode(min(to_char(f.fahrq,'yyyy-MM-dd')),max(to_char(f.fahrq,'yyyy-MM-dd')),	\n" +
                    "       min(to_char(f.fahrq,'yyyy-MM-dd')),\n" +
                    "       min(to_char(f.fahrq,'yyyy-MM-dd'))	\n" +
                    "       ||'--'||max(to_char(f.fahrq,'yyyy-MM-dd'))) as fahrq ," +
                    " 		decode(min(to_char(f.daohrq,'yyyy-MM-dd')),max(to_char(f.daohrq,'yyyy-MM-dd')),	\n" +
                    "       min(to_char(f.daohrq,'yyyy-MM-dd')),\n" +
                    "       min(to_char(f.daohrq,'yyyy-MM-dd'))	\n" +
                    "       ||'--'||max(to_char(f.daohrq,'yyyy-MM-dd'))) as daohrq ," +
                    "       c.mingc as faz,decode(g.mingc,null,'',count(cp.id)) as ches,decode(g.mingc,null,'',sum(f.biaoz)) as biaoz,	\n" + this.getSupplyCol() + "decode(g.mingc,null,'',sum(cp.yingk)) as yingk," +
                    "		decode(g.mingc,null,'',sum(cp.yuns)) as yuns,	\n" +
                    "       decode(g.mingc,null,'',sum(f.jingz)) as jingz,decode(g.mingc,null,'',h.hetbh) as hetbh,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qnetar,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + ")*1000/4.1816,0))) as Qnetark,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz)," + jies_Stdblxs + "))) as Std,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz)," + jies_Stadblxs + "))) as Stad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.star)/sum(f.jingz)," + jies_Starblxs + "))) as Star,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz)," + jies_Vdafblxs + "))) as Vdaf,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz)," + jies_Mtblxs + "))) as Mt,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mad)/sum(f.jingz)," + jies_Madblxs + "))) as Mad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz)," + jies_Aarblxs + "))) as Aar,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aad)/sum(f.jingz)," + jies_Aadblxs + "))) as Aad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.ad)/sum(f.jingz)," + jies_Adblxs + "))) as Ad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz)," + jies_Vadblxs + "))) as Vad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.had)/sum(f.jingz)," + jies_Hadblxs + "))) as Had,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qbad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qbad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qgrad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qgrad\n" +
                    "       from fahb f,luncxxb lcxx,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,chepb cp,yansbhb ys \n" + this.getSupplyTable(riq) + " \n" +
                    "       where f.gongysb_id=g.id and f.meikxxb_id=m.id 						\n" +
                    "       and f.hetb_id=h.id(+) and f.faz_id=c.id 							\n" +
                    "		and f.id=cp.fahb_id 												\n" +
                    "       and f.liucztb_id=1													\n" +
                    "		and f.yansbhb_id = ys.id(+)											\n" +
                    "		and f.luncxxb_id = lcxx.id(+)										\n" +
                    where_rownum +
                    //     			运输方式条件
                    YunsfsWhere +
                    this.getWhere(riq) +
                    this.getSupplyWhere() +
                    "       group by rollup(f.lie_id,g.mingc,m.mingc,lcxx.mingc,f.chec,h.hetbh,c.mingc)	\n" +
                    "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                    "       order by g.mingc,fahrq";
            //endregion

        } else {
//---------------
            String guohxt = "";
            if (getJieslxValue().getValue().equals(Locale.guotyf_feiylbb) || getJieslxValue().getValue().equals(Locale.dityf_feiylbb)) {
                if (zhi.equals("是")) {
                    guohxt = "	and cp.zhongchh = '" + getGuohxtValue().getValue() + "'\n";
                }
            }

            //如果是国电宣威电厂，加权净重=净重-水分调整量
	    	 /*  String fahbtmp="(select * from fahb) f";
	    	   if(MainGlobal.getXitxx_item("结算", "国电宣威结算量是否进行水分考核调整", String.valueOf(visit.getDiancxxb_id()), "否").equals("是")){
	    		   fahbtmp="(select id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id,\n" +
  					"jihkjb_id, fahrq, daohrq, hetb_id, zhilb_id, jiesb_id,  yunsfsb_id, chec, maoz, piz,\n" +
  					" jingz - gethetsfkhl(id) jingz, biaoz, yingd, yingk, yuns, yunsl, koud,  kous, kouz, koum, zongkd, sanfsl,\n" +
  					" ches, tiaozbz, yansbhb_id, lie_id, yuandz_id,  yuanshdwb_id, kuangfzlb_id, liucb_id,\n" +
  					" liucztb_id, hedbz, beiz, ruccbb_id, leiid,  ditjsbz, ditjsb_id, lieid, laimsl, laimzl,\n" +
  					"  laimkc, jiancl, yundh, xiemkssj, xiemjssj, zhuanggkssj, zhuanggjssj, chex from fahb) f";
	    	   }*/
//	    	 String jieslx=getJieslxValue().getValue();
//	    	 System.out.println(jieslx);
            sql_tmp =" select f.lie_id as id," ;
                            if(getJieslxValue().getValue().equals("地铁运费")){
                                sql_tmp+=   "decode(y.mingc,null,'合计',y.mingc) as yunsdw," ;
                            }else{
                                sql_tmp+= "decode(g.mingc,null,'合计',g.mingc) as fahdw," ;
                            }


            sql_tmp+= "m.mingc as meikdw,	p.mingc pinz,	\n" +
                            " 		decode(min(to_char(f.fahrq,'yyyy-MM-dd')),max(to_char(f.fahrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.fahrq,'yyyy-MM-dd'))) as fahrq ," +
                            " 		decode(min(to_char(f.daohrq,'yyyy-MM-dd')),max(to_char(f.daohrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.daohrq,'yyyy-MM-dd'))) as daohrq ," +
                            "       c.mingc as faz," +
                                    "decode(m.mingc,null,'',count(cp.id)) as ches," +
                                    "decode(m.mingc,null,'',sum(cp.biaoz)) as biaoz,	\n" + this.getSupplyCol() +
                                    "decode(m.mingc,null,'',sum(cp.yingk)) as yingk," +
                            "		decode(m.mingc,null,'',sum(cp.yuns)) as yuns,	\n" +
                            "       decode(m.mingc,null,'',sum(" + Locale.jing_jiesxz + ")) as jingz,decode(m.mingc,null,'',h.hetbh) as hetbh,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qnetar,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + ")*1000/4.1816,0))) as Qnetark,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz)," + jies_Stdblxs + "))) as Std,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz)," + jies_Stadblxs + "))) as Stad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.star)/sum(f.jingz)," + jies_Starblxs + "))) as Star,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz)," + jies_Vdafblxs + "))) as Vdaf,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz)," + jies_Mtblxs + "))) as Mt,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mad)/sum(f.jingz)," + jies_Madblxs + "))) as Mad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz)," + jies_Aarblxs + "))) as Aar,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aad)/sum(f.jingz)," + jies_Aadblxs + "))) as Aad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.ad)/sum(f.jingz)," + jies_Adblxs + "))) as Ad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz)," + jies_Vadblxs + "))) as Vad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.had)/sum(f.jingz)," + jies_Hadblxs + "))) as Had,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qbad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qbad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qgrad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qgrad\n" +
                            "       from fahb f,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,chepb cp,yansbhb ys,pinzb p,yunsdwb y\n" + this.getSupplyTable(riq) + " \n" +
                            "       where f.gongysb_id=g.id and f.meikxxb_id=m.id 	 and cp.yunsdwb_id=y.id(+)					\n" +
                            "       and f.hetb_id=h.id(+) and f.faz_id=c.id 							\n" +
                            "		and f.id=cp.fahb_id 												\n" +
                            "       and f.liucztb_id=1													\n" +
                            "		and f.yansbhb_id = ys.id(+)	and f.pinzb_id=p.id										\n" +
                            where_rownum +
//	     			运输方式条件
                            YunsfsWhere +
                            this.getWhere(riq) +
                            this.getSupplyWhere() + guohxt ;


            logger.info("运输单位id:"+this.getYunsdwValue());
            if(getJieslxValue().getValue().equals("地铁运费")){
                long yunsdw_id=this.getYunsdwValue().getId();
                if(yunsdw_id!=-1){
                    sql_tmp+=" and cp.yunsdwb_id="+yunsdw_id+"\n";
                }

                sql_tmp+=  " group by rollup(f.lie_id,y.mingc,m.mingc,p.mingc,h.hetbh,c.mingc) \n"+
                "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                        "       order by y.mingc,m.mingc,fahrq";

            }else {
                sql_tmp+= "       group by rollup(f.lie_id,g.mingc,m.mingc,p.mingc,h.hetbh,c.mingc)	\n" +
                "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                        "       order by g.mingc,fahrq";

            }

            String flag = MainGlobal.getXitxx_item("结算", "西部单批次结算,处理超出部分,根据chec排序用", String.valueOf(visit.getDiancxxb_id()), "否");
            if ("是".equals(flag)) {
                sql = "select sj.* from fahb f,(" + sql_tmp + ") sj where f.lie_id(+)=sj.id order by decode(substr(f.chec||'_0',2,9),'0','_999999999',substr(f.chec||'_0',2,9))";
            } else {
                sql = sql_tmp;
            }
        }


        ResultSetList rsl = con.getResultSetList(sql);
        String flag = MainGlobal.getXitxx_item("结算", "西部单批次结算,处理超出部分,根据chec排序用", String.valueOf(visit.getDiancxxb_id()), "否");
        if ("是".equals(flag)) {
            sql = sql_tmp;
        }
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        egu.setTableName("fahb");
        egu.setDefaultsortable(false);
        egu.setWidth(Locale.Grid_DefaultWidth + "-5");// 设置页面的宽度,当超过这个宽度时显示滚动条
        egu.getColumn("id").setHidden(true);
        if(getJieslxValue().getValue().equals("地铁运费")){
            egu.getColumn("yunsdw").setHeader("运输单位");
        }else {
            egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
        }
        egu.getColumn("meikdw").setHeader(Locale.meikxxb_id_fahb);
        egu.getColumn("ches").setHeader("车数");
        egu.getColumn("hetbh").setHeader("合同号");
        egu.getColumn("pinz").setHeader("煤种");
        egu.getColumn("fahrq").setHeader("发货日期");
        egu.getColumn("daohrq").setHeader("到货日期");
        egu.getColumn("faz").setHeader("发站");
        egu.getColumn("biaoz").setHeader("票重");
        egu.getColumn("yingk").setHeader("盈亏");
        egu.getColumn("yuns").setHeader("运损");
        egu.getColumn("jingz").setHeader("净重");

        egu.getColumn("Std").setHeader("Std(%)");
        egu.getColumn("Mt").setHeader("Mt(%)");
        egu.getColumn("Mad").setHeader("Mad(%)");
        egu.getColumn("Aar").setHeader("Aar(%)");
        egu.getColumn("Aad").setHeader("Aad(%)");
        egu.getColumn("Ad").setHeader("Ad(%)");
        egu.getColumn("Vad").setHeader("Vad(%)");
        egu.getColumn("Vdaf").setHeader("Vdaf(%)");
        egu.getColumn("Stad").setHeader("Stad(%)");
        egu.getColumn("Star").setHeader("Star(%)");
        egu.getColumn("Had").setHeader("Had(%)");
        egu.getColumn("Qnetar").setHeader("Qnetar(mj/kg)");
        egu.getColumn("Qnetark").setHeader("Qnetar(kcal/kg)");
        egu.getColumn("Qbad").setHeader("Qbad(mj/kg)");
        egu.getColumn("Qgrad").setHeader("Qgrad(mj/kg)");

        egu.getColumn("Std").setHidden(true);
        egu.getColumn("Mt").setHidden(true);
        egu.getColumn("Mad").setHidden(true);
        egu.getColumn("Aar").setHidden(true);
        egu.getColumn("Aad").setHidden(true);
        egu.getColumn("Ad").setHidden(true);
        egu.getColumn("Vad").setHidden(true);
        egu.getColumn("Vdaf").setHidden(true);
        egu.getColumn("Star").setHidden(true);
        egu.getColumn("Stad").setHidden(true);
        egu.getColumn("Had").setHidden(true);
        egu.getColumn("Qnetar").setHidden(true);
        egu.getColumn("Qnetark").setHidden(true);
        egu.getColumn("Qbad").setHidden(true);
        egu.getColumn("Qgrad").setHidden(true);

        if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_haiy)) {
//			如果运输方式是海运，
            egu.getColumn("chuanm").setHeader("船名");
            egu.getColumn("hangc").setHeader("航次");
            egu.getColumn("faz").setHeader("港口");
            egu.getColumn("ches").setHidden(true);
        }

//		从系统信息表中动态设置附加的显示指标
        if (((Visit) getPage().getVisit()).isFencb()) {

            strDiancxxb_id = String.valueOf(MainGlobal.getProperId(this.getFencbModel(), this.getMainjsdw()));
        } else {
            strDiancxxb_id = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
        }

        String Xitxxvalue = "";
        String Xitxx[] = null;
        Xitxxvalue = MainGlobal.getXitxx_item("结算", Locale.jiesxzfjxszb_xitxx,
                strDiancxxb_id, "");

        Xitxx = Xitxxvalue.split(",");

        for (int i = 0; i < Xitxx.length; i++) {

            if (egu.getColumn(Xitxx[i]) != null) {

                egu.getColumn(Xitxx[i]).setHidden(false);
            }
        }
//		从系统信息表中动态设置附加的显示指标_end


//		通过jiesszb中结算指标显示字段项目配置，配置结算选择的显示指标
        String xszb[] = jies_Jsxszb.split(",");

        for (int i = 0; i < xszb.length; i++) {

            egu.getColumn(xszb[i]).setHidden(false);
        }

//		通过结算类型设置字段的显示与否

        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id) {
//			两票结算

//			设置显示字段
            egu.getColumn("yihd_ches").setHeader("已核对车数");
            egu.getColumn("yihd_biaoz").setHeader("已核对票重");
            egu.getColumn("Huophdzt").setHeader("货票核对状态");
            egu.getColumn("Huophdzt").setHidden(true);
            egu.getColumn("Huophdzt").setRenderer("renderHdzt");

//			设置字段宽度
            egu.getColumn("yihd_ches").setWidth(70);
            egu.getColumn("yihd_biaoz").setWidth(70);
            egu.getColumn("Huophdzt").setWidth(80);
        }

        // 设定列初始宽度
//		egu.getColumn("id").setWidth(80);
        if(getJieslxValue().getValue().equals("地铁运费")){
            egu.getColumn("yunsdw").setWidth(100);
        }else {
            egu.getColumn("fahdw").setWidth(100);
        }


        egu.getColumn("meikdw").setWidth(130);
        egu.getColumn("pinz").setWidth(80);
        egu.getColumn("ches").setWidth(50);
        egu.getColumn("hetbh").setWidth(70);
        egu.getColumn("fahrq").setWidth(130);
        egu.getColumn("daohrq").setWidth(130);
        egu.getColumn("faz").setWidth(60);
        egu.getColumn("biaoz").setWidth(60);
        egu.getColumn("yingk").setWidth(50);
        egu.getColumn("yuns").setWidth(50);
        egu.getColumn("jingz").setWidth(60);
        egu.getColumn("Std").setWidth(55);
        egu.getColumn("Star").setWidth(55);
        egu.getColumn("Mt").setWidth(55);
        egu.getColumn("Mad").setWidth(60);
        egu.getColumn("Aar").setWidth(60);
        egu.getColumn("Aad").setWidth(60);
        egu.getColumn("Ad").setWidth(60);
        egu.getColumn("Vad").setWidth(60);
        egu.getColumn("Vdaf").setWidth(60);
        egu.getColumn("Stad").setWidth(60);
        egu.getColumn("Had").setWidth(60);
        egu.getColumn("Qnetar").setWidth(80);
        egu.getColumn("Qnetark").setWidth(80);
        egu.getColumn("Qbad").setWidth(80);
        egu.getColumn("Qgrad").setWidth(100);

        egu.setGridType(ExtGridUtil.Gridstyle_Read);// 设定grid可以编辑
        egu.addPaging(0);// 设置分页

        egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
        egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

        egu.addTbarText(biaoq);
        DateField df = new DateField();
        df.setReadOnly(true);
        df.setValue(this.getRiq1());
        df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
        df.setId("RIQ1");

        egu.addToolbarItem(df.getScript());

        egu.addTbarText("至:");
        DateField df1 = new DateField();
        df1.setReadOnly(true);
        df1.setValue(this.getRiq2());
        df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
        df1.setId("RIQ2");
        egu.addToolbarItem(df1.getScript());

        egu.addTbarText("-");// 设置分隔符

        // 设置树
        String condition = " and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') ";

        ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
                ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
                .getVisit()).getDiancxxb_id(), getTreeid(), condition, true);

        String changbAndJiesdwValue = "";
        String RefurbishScript = "document.getElementById('RefurbishButton').click();";
        if (MainGlobal.getXitxx_item("结算", "是否为山东分公司采购结算单", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {

            egu.addTbarText("厂别:");
            ComboBox combcb = new ComboBox();
            combcb.setTransform("DiancDropDown");
            combcb.setId("dianc");
            combcb.setEditable(false);
            combcb.setLazyRender(true);
            combcb.setWidth(80);
            egu.addToolbarItem(combcb.getScript());
            egu.addOtherScript("dianc.on('select',function(){document.forms[0].submit();});");
            egu.addTbarText("-");

            changbAndJiesdwValue = "Mainjsdw.setRawValue(dianc.getRawValue()); Changb.setRawValue(dianc.getRawValue());";
            RefurbishScript = "document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue(); document.getElementById('RefurbishButton').click();";

            etu = new ExtTreeUtil("gongysTree",
                    ExtTreeUtil.treeWindowCheck_gongys, getFencbValue().getId(), getTreeid(), condition, true);
        }

        egu.addTbarText(Locale.gongysb_id_fahb);
        setTree(etu);
        egu.addTbarTreeBtn("gongysTree");


        egu.addTbarText("-");// 设置分隔符




        egu.addTbarText("运输单位:");
        ComboBox ys = new ComboBox();
        ys.setTransform("YunsdwDropDown");
        ys.setEditable(true);
        ys.setWidth(200);
        //ys.setListeners("select:function(){document.Form0.submit();}");
        egu.addToolbarItem(ys.getScript());

        egu.addTbarText("-");// 设置分隔符


        //验收编号根据不同用户可以隐藏
        //
        egu.addTbarText("结算类型:");
        ComboBox comb4 = new ComboBox();
        comb4.setTransform("JieslxDropDown");
        comb4.setId("Jieslx");
        comb4.setEditable(false);
        comb4.setLazyRender(true);// 动态绑定
        comb4.setWidth(80);
        comb4.setReadOnly(true);
        egu.addToolbarItem(comb4.getScript());
        //egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");

        egu.addTbarText("-");
        egu.addTbarText("合同号:");
        ComboBox comb5 = new ComboBox();
        comb5.setTransform("HthDropDown");
        comb5.setId("Heth");
        comb5.setEditable(false);
        comb5.setLazyRender(true);// 动态绑定
        comb5.setWidth(100);
        comb5.setListWidth(350);
        comb5.setReadOnly(true);
        egu.addToolbarItem(comb5.getScript());


        if (getJieslxValue().getValue().equals(Locale.guotyf_feiylbb) || getJieslxValue().getValue().equals(Locale.dityf_feiylbb)) {
            if (zhi.equals("是")) {
                egu.addTbarText("-");
                egu.addTbarText("系统：");
                ComboBox xit_comb = new ComboBox();
                xit_comb.setWidth(80);
                xit_comb.setTransform("Guohxt");
                xit_comb.setId("Guohxt");
                xit_comb.setLazyRender(true);
                xit_comb.setEditable(false);
                egu.addToolbarItem(xit_comb.getScript());
            }
        }

        // 设定工具栏下拉框自动刷新
//		egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
        egu.addTbarText("-");// 设置分隔符
        //egu.addToolbarButton(GridButton.ButtonType_ShowDIV, null);
        egu.addToolbarItem("{" + new GridButton("刷新", "function(){" + RefurbishScript + "}").getScript() + "}");
        egu.addTbarText("-");
        egu.addToolbarItem("{" + new GridButton("条件", "function(){ if(win){ win.show(this);" + "}"
//				+ " win.show(this);	\n"
                + " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n"
                + "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_MAINJSDW_VALUE').value!=''){	\n"
                + "		\tMainjsdw.setRawValue(document.getElementById('TEXT_MAINJSDW_VALUE').value);	\n"
                + "	\t}	\n" + changbAndJiesdwValue
                + " \tif(document.getElementById('TEXT_YANSBH_VALUE').value!=''){	\n"
                + "		\tyansbh.setRawValue(document.getElementById('TEXT_YANSBH_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_YUNSDW_VALUE').value!=''){	\n"
                + "		\tYunsdwCb.setRawValue(document.getElementById('TEXT_YUNSDW_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_PINZ_VALUE').value!=''){	\n"
                + "		\tPinzCb.setRawValue(document.getElementById('TEXT_PINZ_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_YUJSDBM_VALUE').value!=''){	\n"
                + "		\tyujsdbm.setRawValue(document.getElementById('TEXT_YUJSDBM_VALUE').value);	\n"
                + "	\t}	\n"
                + "}").getScript() + "}");
        egu.addTbarText("-");

        if (((Visit) getPage().getVisit()).getString17().equals("tb")) {
//        	如果结算方式为填报，取消结算时对合同的判断

            egu.addToolbarButton("结算", GridButton.ButtonType_SubmitSel, "JiesButton");

        } else {

            String buttonName = "结算";
            if (MainGlobal.getXitxx_item("结算", "是否为山东分公司采购结算单", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
                buttonName = "生成";
            }
            egu.addToolbarButton(buttonName, GridButton.ButtonType_SubmitSel_condition, "JiesButton", "if(Heth.getRawValue()=='请选择'&&Jieslx.getRawValue()!='地铁运费'&&Jieslx.getRawValue()!='国铁运费'){	\n	"
                    + " Ext.MessageBox.alert('提示信息','请选择合同！');	\n"
                    + " return ;	\n"
                    + " }");
        }


        egu.addOtherScript("function gridDiv_save(rec){	\n" +
                "	\tif(rec.get('FAHDW')=='合计'){	\n" +
                "	\treturn 'continue';	\n" +
                "	\t}}");

        // ---------------页面js的计算开始------------------------------------------
	/*	StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if (e.row==7){e.cancel=true;}");//设定第8列不可编辑,索引是从0开始的
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");// 设定供应商列不可编辑
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");// 设定电厂信息列不可编辑
		sb.append("});");

		egu.addOtherScript(sb.toString());*/

//      得到配置的加权量，默认为净重
        String jiaq = "";
        String value = MainGlobal.getXitxx_item("结算", "结算选择加权值", visit.getDiancxxb_id() + "", "净重");
        if (value.equals("票重")) {
            jiaq = "eval(rec[i].get('BIAOZ'))";
        } else if (value.equals("票重+盈亏")) {
            jiaq = "eval(rec[i].get('BIAOZ'))+eval(rec[i].get('YINGK'))";
        } else if (value.equals("净重+运损")) {
            jiaq = "eval(rec[i].get('JINGZ'))+eval(rec[i].get('YUNS'))";
        } else {
            jiaq = "eval(rec[i].get('JINGZ'))";
        }

        toAllUpperOrLowerCase("");

        egu.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
                + " \tvar ches=0,biaoz=0,yihd_ches=0,yihd_biaoz=0,yingk=0,yuns=0,jingz=0,std=0, mt=0,mad=0,aar=0,aad=0,ad=0,vad=0,vdaf=0,stad=0,had=0,qnet_ar=0,qbad=0,qgrad_daf=0;	\n"
//				设置加权变量
                + "var jiaqz = 0;qnetar_jq=0;qnetark_jq = 0;std_jq=0;mt_jq=0;qnetar = 0;qnetark = 0;std1=0;mt1=0;\n"

                + "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"
                + " for(var i=0;i<rec.length;i++){ \n"
                + " \tif(''!=rec[i].get('ID')){ \n"
                + "		\tches+=eval(rec[i].get('CHES'));	\n"
                + "		\tbiaoz+=eval(rec[i].get('BIAOZ'));	\n"
                + "		\tyihd_ches+=eval(rec[i].get('YIHD_CHES'));	\n"
                + "		\tyihd_biaoz+=eval(rec[i].get('YIHD_BIAOZ'));	\n"
                + " 	\tyingk+=eval(rec[i].get('YINGK')); \n"
                + " 	\tyuns+=eval(rec[i].get('YUNS'));	\n"
                + " 	\tjingz+=eval(rec[i].get('JINGZ'));	\n"
                + " 	\tstd=eval(rec[i].get('STD'));	\n"
                + "		\tmt=eval(rec[i].get('MT'));"
                + " 	\tmad=eval(rec[i].get('MAD')); \n"
                + " 	\taar=eval(rec[i].get('AAR'));	\n"
                + " 	\taad=eval(rec[i].get('AAD'));	\n"
                + " 	\tad=eval(rec[i].get('AD'));	\n"
                + "		\tvad=eval(rec[i].get('VAD'));"
                + " 	\tvdaf=eval(rec[i].get('VDAF')); \n"
                + " 	\tstad=eval(rec[i].get('STAD'));	\n"
                + " 	\thad=eval(rec[i].get('HAD'));	\n"
                + " 	\tqnet_ar=eval(rec[i].get('QNET_AR'));	\n"
                + "		\tqbad=eval(rec[i].get('QBAD'));"
                + " 	\tqgrad_daf=eval(rec[i].get('QGRAD_DAF')); \n"

                //计算加权变量值
                + " 	jiaqz += (" + jiaq + "); \n"
                + " 	qnetar_jq += (eval(rec[i].get('QNETAR'))*(" + jiaq + ")); \n"
//				+ " 	qnetark_jq += (eval(rec[i].get('QNETARK'))*("+jiaq+")); \n"
                + " 	std_jq += (eval(rec[i].get('STD'))*(" + jiaq + ")); \n"
                + " 	mt_jq += (eval(rec[i].get('MT'))*(" + jiaq + ")); \n"


                + " \t}	\n"
                + " }	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_CHES',yihd_ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_BIAOZ',yihd_biaoz);	\n"

                //如果加权值为0，合计为0
                + " if(jiaqz!=0){	\n"
                + " 	qnetar = eval(qnetar_jq/jiaqz).toFixed(2);	\n"
//				+ " 	qnetark = eval(qnetark_jq/jiaqz);	\n"
                + " 	std1 = eval(std_jq/jiaqz);	\n"
                + " 	mt1 = eval(mt_jq/jiaqz);	\n"
                + " } 	\n"
                + "	qnetark = qnetar*1000/4.1816;\n"
                //加权后赋值
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETAR',qnetar);	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETARK',qnetark.toFixed(0) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STD',std1.toFixed(2) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MT',mt1.toFixed(2) );	\n"

                + " });		\n");


//		当选择全选后重新计算选中项的合计值
        egu.addOtherScript("gridDiv_grid.on('click',function(){		\n"
                + "		reCountToolbarNum(this);			\n"
                + "});		\n");


//		重新计算选中项的合计值
        egu.addOtherScript(" function reCountToolbarNum(obj){	\n "
                + " \tvar rec;	\n"
                + " \tvar ches=0,biaoz=0,yihd_ches=0,yihd_biaoz=0,yingk=0,yuns=0,jingz=0;	\n"
                //设置加权变量
                + "var jiaqz = 0;qnetar_jq=0;qnetark_jq = 0;std_jq=0;mt_jq=0;qnetar = 0;qnetark = 0;std1=0;mt1=0;\n"
                + " \trec = obj.getSelectionModel().getSelections();				\n"
                + " \tfor(var i=0;i<rec.length;i++){								\n"
                + " 	\tif(0!=rec[i].get('ID')){									\n"
                + " 		\tches+=eval(rec[i].get('CHES'));						\n"
                + " 		\tbiaoz+=eval(rec[i].get('BIAOZ'));						\n"
                + " 		\tyingk+=eval(rec[i].get('YINGK'));						\n"
                + " 		\tyuns+=eval(rec[i].get('YUNS'));						\n"
                + " 		\tjingz+=eval(rec[i].get('JINGZ'));						\n"
                + " 		\tyihd_ches+=eval(rec[i].get('YIHD_CHES'));				\n"
                + " 		\tyihd_biaoz+=eval(rec[i].get('YIHD_BIAOZ'));			\n"
                //计算加权变量值
                + " 	jiaqz += (" + jiaq + "); \n"
                + " 	qnetar_jq += (eval(rec[i].get('QNETAR'))*(" + jiaq + ")); \n"
//				+ " 	qnetark_jq += (eval(rec[i].get('QNETARK'))*("+jiaq+")); \n"
                + " 	std_jq += (eval(rec[i].get('STD'))*(" + jiaq + ")); \n"
                + " 	mt_jq += (eval(rec[i].get('MT'))*(" + jiaq + ")); \n"
                + " 	\t}															\n"
                + " \t}																\n"
                + " if(gridDiv_ds.getCount()>0){									\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_CHES',yihd_ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_BIAOZ',yihd_biaoz);	\n"
                //如果加权值为0，合计为0
                + " if(jiaqz!=0){	\n"
                + " 	qnetar = eval(qnetar_jq/jiaqz).toFixed(2);	\n"
//				+ " 	qnetark = eval(qnetark_jq/jiaqz);	\n"
                + " 	std1 = eval(std_jq/jiaqz);	\n"
                + " 	mt1 = eval(mt_jq/jiaqz);	\n"
                + " } 	\n"
                + "	qnetark = qnetar*1000/4.1816;\n"
                //加权后赋值
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETAR',qnetar);	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETARK',qnetark.toFixed(0) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STD',std1.toFixed(2) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MT',mt1.toFixed(2) );	\n"
                + "	}\n"
                + " \t} \n");

//		多选树的赋值
        egu.addOtherScript(
                "gongysTree_treePanel.on(\"checkchange\",function(node,checked){\n" +
                        "    if(checked){\n" +
                        "      addNode(node);\n" +
                        "    }else{\n" +
                        "      subNode(node);\n" +
                        "    }\n" +
                        "    node.expand();\n" +
                        "    node.attributes.checked = checked;\n" +
                        "    node.eachChild(function(child) {\n" +
                        "      if(child.attributes.checked != checked){\n" +
                        "        if(checked){\n" +
                        "          addNode(child);\n" +
                        "        }else{\n" +
                        "          subNode(child);\n" +
                        "        }\n" +
                        "        child.ui.toggleCheck(checked);\n" +
                        "              child.attributes.checked = checked;\n" +
                        "              child.fireEvent('checkchange', child, checked);\n" +
                        "      }\n" +
                        "    });\n" +
                        "  },gongysTree_treePanel);\n" +
                        "  function addNode(node){\n" +
                        "    var history = '+,'+node.id+\";\";\n" +
                        "    writesrcipt(node,history);\n" +
                        "  }\n" +
                        "\n" +
                        "  function subNode(node){\n" +
                        "    var history = '-,'+node.id+\";\";\n" +
                        "    writesrcipt(node,history);\n" +
                        "  }\n" +
                        "function writesrcipt(node,history){\n" +
                        "\t\tif(gongysTree_history==\"\"){\n" +
                        "\t\t\tgongysTree_history = history;\n" +
                        " 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                        "\t\t}else{\n" +
                        "\t\t\tvar his = gongysTree_history.split(\";\");\n" +
                        "\t\t\tvar reset = false;\n" +
                        "\t\t\tfor(i=0;i<his.length;i++){\n" +
                        "\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" +
                        "\t\t\t\t\this[i] = \"\";\n" +
                        "\t\t\t\t\treset = true;\n" +
                        "\t\t\t\t\tbreak;\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\tif(reset){\n" +
                        "\t\t\t  gongysTree_history = his.join(\";\");\n" +
                        "      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                        "    }else{\n" +
                        "      	 gongysTree_history += history;\n" +
                        "        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                        "    }\n" +
                        "  }\n" +
                        "\n" +
                        "}");

        // ---------------页面js计算结束--------------------------
        egu.addOtherScript(Strtmpfunction);
        setExtGrid(egu);
        con.Close();
    }

    //大小写转换
    public static String toAllUpperOrLowerCase(String fireString) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < fireString.length(); i++) {
            char c = fireString.charAt(i);
            if (Character.isUpperCase(c)) {
                buffer.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                buffer.append(Character.toUpperCase(c));
            }
        }
        return buffer.toString();
    }

    private String getSupplyTable(String DateType) {
//		为了处理除煤款结算外增列的情况
        String supplytable = "";
        long feiylbb_id = 0;
        String where = "";
//      使用验收编号
        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id        //        	两票结算
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id    //        	国铁运费
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id    //        	地铁运费
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id    //        	海运运费
                ) {

            if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id || getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                    || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id) {

                feiylbb_id = Locale.guotyf_feiylbb_id;
            } else {

                feiylbb_id = getJieslxValue().getId();
            }

            supplytable = ",(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl				\n"
                    + "       						where c.id=yfzl.chepb_id																			\n"
                    + "       						and yfzl.feiylbb_id=" + feiylbb_id + ") yfzl,									\n"

                    + " (select distinct cp.id,decode(yfzl.jifzl,null,nvl(cp.biaoz,0),yfzl.jifzl) as yihd_biaoz 			\n"
                    + " 	from chepb cp,danjcpb dj,yunfdjb yf,(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl				\n"
                    + "       where c.id=yfzl.chepb_id																			\n"
                    + "       and yfzl.feiylbb_id=" + feiylbb_id + ") yfzl,fahb f,meikxxb m,gongysb g,yansbhb ys,zhilb z 		\n"
                    + " 	where cp.id=dj.chepb_id and f.id=cp.fahb_id 															\n"
                    + "     and f.meikxxb_id=m.id and f.gongysb_id=g.id	and dj.yunfdjb_id=yf.id and yf.feiylbb_id=" + feiylbb_id
                    + " 	and cp.id=yfzl.chepb_id(+) " + this.getWhere(DateType)
                    + " ) yihd,\n" +

                    "	(select distinct cp.id,dj.yunfjsb_id\n" +
                    "                  from chepb cp,yunfdjb yd,danjcpb dj,fahb f,zhilb z,yansbhb ys,\n" +
                    "						gongysb g,meikxxb m\n" +
                    "                  where yd.id=dj.yunfdjb_id\n" +
                    "                        and dj.chepb_id=cp.id\n" +
                    "                        and f.id=cp.fahb_id\n" +
                    "						 and f.gongysb_id=g.id\n" +
                    "						 and f.meikxxb_id=m.id\n" +
                    "                        and yd.feiylbb_id=" + feiylbb_id + "\n" + this.getWhere(DateType) +
                    "     ) djcp";

        }

        return supplytable;
    }

    private String getSupplyWhere() {

        String supplywhere = "";

        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id            //两票结算
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id        //国铁运费
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id        //地铁运费
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id        //海运运费
                ) {

            supplywhere = "	    and cp.id=yihd.id(+)\n" +
                    "       and cp.id=djcp.id(+)\n" +
                    "      -- and (djcp.yunfjsb_id is null or djcp.yunfjsb_id=0) \n" +
                    "		and cp.id=yfzl.chepb_id(+) \n ";
        }

        return supplywhere;
    }

    private String getSupplyCol() {
//		运费结算的增列
        String supplycol = "";

        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id
                ) {
//			两票结算
            supplycol = " decode(m.mingc,null,'',count(yihd.id)) as yihd_ches,decode(m.mingc,null,'',sum(nvl(yihd.yihd_biaoz,0))) as yihd_biaoz,	\n" +
                    " decode(m.mingc,null,'',case when (count(cp.id)-count(yihd.id))=0 then '已完成' else '未完成' end) as Huophdzt,	\n";
        }

        return supplycol;
    }

    private String getWhere(String DateType) {

        String where = "";

        //供应商
//		2008-12-19新修改，如果用户没对树做操作，则列出全部的发货信息
        if (!this.getYansbh().equals("请选择") && !this.getYansbh().equals("")) {

//			有验收编号
            where = " and f.yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh());

            if (((Visit) getPage().getVisit()).isFencb()) {
//        		一厂多制单厂结算多厂判断

                String dcid = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());

                where += " and f.diancxxb_id in (" + dcid + ")";

//            	where+=" and f.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
            } else {

                where += " and f.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
            }

            if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id) {    //两票结算

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ")	\n";
            } else if (getJieslxValue().getId() == Locale.meikjs_feiylbb_id) {    //煤款结算

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ")	\n";
            } else if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {

                where += " and cp.jiesb_id=0	\n";
            } else {

                where += " and ((dj.yunfjsb_id=0 or yd.feiylbb_id<>" + getJieslxValue().getId() + " or yd.feiylbb_id is null) or dj.yunfdjb_id is null)	\n";
            }

        } else {

//        	没有验收编号
            String gongysb_id = this.getGongysmlttree_id();

            if (!(gongysb_id == null || gongysb_id.equals(""))) {
                where += " and (m.id in (" + gongysb_id + ") or g.id in (" + gongysb_id + "))	\n";
            }

            if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id) {    //两票结算

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ") and f.zhilb_id=z.id and z.liucztb_id=1 	\n";
            } else if (getJieslxValue().getId() == Locale.meikjs_feiylbb_id) {    //煤款结算

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ") and f.zhilb_id=z.id and z.liucztb_id=1	\n";

            } else if (getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                    || getJieslxValue().getId() == Locale.dityf_feiylbb_id
                    || getJieslxValue().getId() == Locale.daozyf_feiylbb_id
                    || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id
                    ) {        //运费结算
//        		where+=" and (dj.yunfjsb_id=0 or dj.yunfdjb_id is null) and f.zhilb_id=z.id(+) ";
                where += " and f.zhilb_id=z.id and z.liucztb_id=1 \n";
            }

            if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//        		倒装运费
                where += " and cp.jiesb_id=0 \n";
            }

            if (MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw()) > -1) {

                if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//        			倒装运费
                    where += " and cp.yunsdw='" + getYunsdw() + "'";
                } else {

                    where += " and cp.yunsdwb_id=" + MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw());
                }
            }

            //品种
            if (MainGlobal.getProperId(getPinzModel(), this.getPinz()) > -1) {
                where += " and f.pinzb_id = " + MainGlobal.getProperId(getPinzModel(), this.getPinz()) + "\n";
            }

            //低位热量
//        	下限
            if (!this.getQd().equals("") && !this.getQd().equals("0")) {
                where += " and z.qnet_ar>=" + this.getQd() + "\n";
            }
//        	上限
            if (!this.getQu().equals("") && !this.getQu().equals("0")) {
                where += " and z.qnet_ar<=" + this.getQu() + "\n";
            }
            //Std
//        	下限
            if (!this.getStdd().equals("") && !this.getStdd().equals("0")) {
                where += " and z.std>=" + this.getStdd() + "\n";
            }
//        	上限
            if (!this.getStdu().equals("") && !this.getStdu().equals("0")) {
                where += " and z.std<=" + this.getStdu() + "\n";
            }
            //Star
//        	下限
            if (!this.getStard().equals("") && !this.getStard().equals("0")) {
                where += " and z.star>=" + this.getStard() + "\n";
            }
//        	上限
            if (!this.getStaru().equals("") && !this.getStaru().equals("0")) {
                where += " and z.star<=" + this.getStaru() + "\n";
            }


            if (((Visit) getPage().getVisit()).isFencb()) {

//            	一厂多制单厂结算多厂判断
                String yicdzcl = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());

                where += " and f.diancxxb_id in (" + yicdzcl + ")";

//            	where+=" and f.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
            } else {

                where += " and f.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
            }

//          日期判断

            where += " and f." + DateType + " >= to_date('" + getRiq1() + "', 'yyyy-mm-dd')				\n" +
                    "	and f." + DateType + " <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')				\n";


//            使用验收编号
            if (MainGlobal.getXitxx_item("结算", Locale.shiyysbh_jies, String.valueOf(((Visit) getPage().getVisit()).isFencb() ? MainGlobal.getProperIds(this.getFencbModel(), this.getChangb()) : String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id())), "否").equals("是")) {

                where += " and f.yansbhb_id=ys.id(+) ";

                if (this.getTree() != null && getHthValue().getId() != -1) {

//            		if(Jiesdcz.getJiessz_item(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(this.getFencbModel(),this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id(),
//            				Long.parseLong(MainGlobal.getLeaf_ParentNodeId(this.getTree(), this.getTreeid())),this.getHthValue().getId() ,Locale.jieszbtz_jies, "否").equals("是")){
//
//            			//where+=" and ys.liucztbid=1";
//            		}
                }
            }
        }


        return where;
    }

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
            visit.setboolean1(true);
            if (visit.getActivePageName().toString().equals("Balance")
                    || visit.getActivePageName().toString().equals("Jieszbtz")
                    || visit.getActivePageName().toString().equals("DCBalance_tb")
                    || visit.getActivePageName().toString().equals("DCBalance")
                    ) {
//				如果是从后面的界面跳转回来的，且String15 不为空，应保留Stirng15的值，认为是分公司结算
                if (!visit.getString15().equals("")) {

                    visit.setboolean1(false);
                }
            }

            visit.setActivePageName(getPageName().toString());

            visit.setString1("");    //Rbvalue日期类型选择
            visit.setString2("");    //Treeid
            visit.setString3("");    //厂
            visit.setString4("");    //验收编号
            visit.setString5("");    //riq1
            visit.setString6("");    //riq2
            visit.setString7("");    //结算扣吨量
            visit.setString8("");    //保存拆分列的列id
            visit.setString9("");    //活泼核对状态
            visit.setString10("");    //运输单位
            visit.setString12("");    //上次结算量
            visit.setString13("");    //fahb_id
            visit.setString14("");    //gongysbmlt_id(供应商表)
            visit.setString16("");    //主结算单位
            visit.setString18("");  //结算扣款金额
            visit.setString19("");  //燃料品种ID
            visit.setString20("");  //预结算单编码

            if (visit.getboolean1()) {

                visit.setString15("");    //结算单位（用来保存页面设置的结算单位，可能是 电厂id 或 分公司id），
                //该参数要传到下一个页面，保存时做判断用
                visit.setString17("");    //结算方式（正常、填报）
                visit.setString11("");  //保存页面加载参数(运输方式)
            }
//			点击结算后的页面返回结算设置页面时，运输方式丢失，无法正确构建结算设置页面
            if ("hy".equals(MainGlobal.getXitxx_item("结算", "庄河海运煤结算设置页面bug", String.valueOf(visit.getDiancxxb_id()), ""))) {
                visit.setString11("hy");
            }
            visit.setLong1(0);        //电厂信息表id
            visit.setLong2(0);        //结算类型
            visit.setLong3(0);        //供应商表id
            visit.setLong8(0);        //合同表id
            visit.setLong9(0);        //运输单位表id
            visit.setList1(null);
            setJieslxValue(null);    //3
            setJieslxModel(null);

            setHthValue(null);        //4
            setHthModel(null);

            setYansbhValue(null);    //2
            setYansbhModel(null);

            setFencbValue(null);    //5
            setFencbModel(null);

            setYunsdwValue(null);    //6
            setYunsdwModel(null);    //6

            setYujsdbmValue(null);    //8
            setYujsdbmModel(null);

            setGuohxtValue(null);    //9
            setGuohxtModel(null);    //9

            setPinzValue(null);        //7
            setPinzModel(null);        //7

            visit.setboolean1(true);    //日期change
            visit.setboolean2(false);    //发货单位change、结算类型,运输单位,品种
            ((Visit) getPage().getVisit()).setboolean3(false);    //分厂别
            ((Visit) getPage().getVisit()).setboolean4(false);    //判断是否点击率“刷新按钮”，只有点击“刷新”才显示数据

            getJieslxModels();        //3
            getHthModels();            //4
            getYansbhModels();        //2
            getFencbModels();        //5
            logger.info("===begin getYunsdwValue:"+this.getYunsdwValue().getId());
            if(this.getYunsdwValue().getId()==-1){
                 getYunsdwModels();
            }
      //6
            getPinzModels();        //7
            getYujsdbmModels();        //8

//			((Visit) this.getPage().getVisit()).setString11(""); //保存页面加载参数(运输方式)
            if (cycle.getRequestContext().getParameters("lx") != null) {

                ((Visit) this.getPage().getVisit()).setString11(cycle.getRequestContext().getParameters("lx")[0].trim());
            }
//			统一结算时，得到合同的需方的id（如果是分公司采购单，就应是分公司的id，如果是电厂采购单不要加该参数）
            if (cycle.getRequestContext().getParameters("jsdwid") != null) {

                ((Visit) this.getPage().getVisit()).setString15(cycle.getRequestContext().getParameters("jsdwid")[0].trim());
            }
//			判断结算的结算方式，如果是填报 jsfs=tb,否则认为是结算方式是计算
            if (cycle.getRequestContext().getParameters("jsfs") != null) {

                ((Visit) this.getPage().getVisit()).setString17(cycle.getRequestContext().getParameters("jsfs")[0].trim());
            }

//			设置默认货票核对状态
            setHuopztvalue(MainGlobal.getXitxx_item("结算", "煤款结算设置中的货票核对状态为", String.valueOf(visit.getDiancxxb_id()), "yiwc"));

        }

        if (visit.getboolean1() || visit.getboolean2()) {

            visit.setboolean1(false);
            visit.setboolean2(false);
//			visit.setString9("");
            getHthModels();
            getYansbhModels();
//            getYunsdwModels();
            getPinzModels();
            //this.getPinzModels();
        }
        getYujsdbmModels();
        getSelectData();
    }


    //结算类型
    public boolean _Jieslxchange = false;

    public IDropDownBean getJieslxValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean3((IDropDownBean) getJieslxModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setJieslxValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

            ((Visit) getPage().getVisit()).setboolean2(true);

        }
        ((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }

    public IPropertySelectionModel getJieslxModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

            getJieslxModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void setJieslxModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getJieslxModels() {

        String sql = "select id, mingc from feiylbb where (leib<2 or leib=3) order by id";

        if (MainGlobal.getXitxx_item("结算", "是否为山东分公司采购结算单", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
            sql = "select id, mingc from feiylbb where mingc = '" + Locale.meikjs_feiylbb + "'";
        }

        ((Visit) getPage().getVisit())
                .setProSelectionModel3(new IDropDownModel(sql));
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    //  过衡系统下拉框_开始
    public IDropDownBean getGuohxtValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean9() == null) {
            if (getGuohxtModel().getOptionCount() > 0) {
                setGuohxtValue((IDropDownBean) getGuohxtModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean9();
    }

    public void setGuohxtValue(IDropDownBean LeibValue) {
        ((Visit) this.getPage().getVisit()).setDropDownBean9(LeibValue);
    }

    public IPropertySelectionModel getGuohxtModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel9() == null) {
            getGuohxtModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel9();
    }

    public void setGuohxtModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel9(value);
    }

    public void getGuohxtModels() {
        ArrayList list = new ArrayList();
        list.add(new IDropDownBean(1, "A系统"));
        list.add(new IDropDownBean(2, "B系统"));
        setGuohxtModel(new IDropDownModel(list));
    }
//	过衡下拉框_结束

    public String rb1() {
        if (getRbvalue() == null || getRbvalue().equals("")) {


            return "fh.fahrq";


        } else {
            if (getRbvalue().equals("fahrq")) {
                return "fh.fahrq";
            } else return "fh.daohrq";
        }
    }

    // 验收编号
    public boolean _Yansbhchange = false;

    public IDropDownBean getYansbhValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean2((IDropDownBean) getYansbhModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void setYansbhValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

            ((Visit) getPage().getVisit()).setboolean1(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean2(Value);
    }

    public void setYansbhModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getYansbhModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getYansbhModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public IPropertySelectionModel getYansbhModels() {

        String sql = "";

        if (this.getJieslxValue().getId() == 1 || getJieslxValue().getId() == 2) {
//			两票结算或煤款结算

            sql = "select distinct y.id,y.bianm from yansbhb y, chepb cp, fahb fh where fh.yansbhb_id=y.id and cp.fahb_id=fh.id  "
                    + " and fh.jiesb_id=0 and fh.diancxxb_id in (" + String.valueOf(((Visit) getPage().getVisit()).isFencb() ? MainGlobal.getProperIds(getFencbModel(),
                    this.getChangb()) : String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id())) + ") order by bianm";
        } else {
//			运费结算
            sql = "select distinct y.id,y.bianm from yansbhb y, chepb cp, danjcpb dj,fahb fh where fh.yansbhb_id=y.id and cp.id=dj.chepb_id and cp.fahb_id=fh.id "
                    + " and dj.yunfjsb_id=0 and fh.diancxxb_id in (" + String.valueOf(((Visit) getPage().getVisit()).isFencb() ? MainGlobal.getProperIds(getFencbModel(),
                    this.getChangb()) : String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id())) + ") order by bianm";
        }

        ((Visit) getPage().getVisit())
                .setProSelectionModel2(new IDropDownModel(sql, "请选择"));
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    //	预结算单编码begin
    public IDropDownBean getYujsdbmValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean8((IDropDownBean) getYujsdbmModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean8();
    }

    public void setYujsdbmValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean8()) {

            ((Visit) getPage().getVisit()).setboolean1(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean8(Value);
    }

    public void setYujsdbmModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel8(value);
    }

    public IPropertySelectionModel getYujsdbmModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
            getYujsdbmModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }

    public IPropertySelectionModel getYujsdbmModels() {
        String gys_id = getGongysmlttree_id();

        String mstr_gongys_id = "";
//		选择了供应商
        if ("".equals(gys_id) || gys_id == null) {
            //什么都没有选择,是全部的时候
            mstr_gongys_id = "0";
        } else if (gys_id.indexOf(",") == -1) {
            //如果单独只选择了煤矿,自动把煤矿转换为供应商的id
            mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(), gys_id);
        } else {
            //即选择了煤矿又选择了供应商,截取供应商的id
            mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                    gys_id.substring(0, gys_id.indexOf(",")));
        }
        String sql = "select id,bianm from jiesb where gongysb_id in(" + mstr_gongys_id + ") " +
                " and daibch like '%~%' and is_yujsd=1  and weicdje>0";

        ((Visit) getPage().getVisit())
                .setProSelectionModel8(new IDropDownModel(sql, "请选择"));
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }

    //	预结算单编码end
    //合同号
    public boolean _Hthchange = false;

    public IDropDownBean getHthValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean4((IDropDownBean) getHthModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setHthValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

            ((Visit) getPage().getVisit()).setboolean1(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }

    public void setHthModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getHthModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getHthModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public IPropertySelectionModel getHthModels() {
        JDBCcon con = new JDBCcon();
        List List = new ArrayList();
        try {

//			一、煤矿有没有供应商
//			二、判断系统里有没有合同
//			2008-10-17修改方案，当合同未审核完成时可以进行试结算，但不能保存
//
//			合同和供应商和煤矿的关系（要想找出对应的合同必须满足的条件）：

//			电厂采购：
//			1、煤款合同
//				1)、需方（hetb中的diancxxb_id）为该电厂或 合同数量中的收货人包含该电厂。
//				2)、未选择供应商时，选择的时间段内存在合同发货人供的煤 或 选择的供应商为合同的发货人
//					或 发货中的发货人存在于"hetfhrgysglb"中且表中的gongysb_id能和发货相关联;
//					已选择了供应商时，合同的发货人为所选供应商 或 所选供应商在 hetfhrgysglb 中有合同发货人与之对应。

//			2、运输合同(支持一厂多制，分厂可使用总厂的运输合同)
//				1）、如果没有选择供应商，那么结算时间段内的发货必须有运输合同价格中规定的煤矿；
//						如果指定了供应商，那么供应商煤矿关联中的煤矿信息必须在运输合同价格中得以显示。
//				2）、该电厂必须是运输合同的托运方。

//			分公司采购：
//			1、煤款合同
//				1）、需方（hetb中的diancxxb_id）为该分公司。
//				2）、未选择供应商时，选择的时间段内的发货存在供应商为合同的发货人
//						或 选择的时间段内的发货存在的供应商为合同的需方（分公司）此处用hetb中的需方单位(xufdwmc) 与电厂信息表中的 string15 为id的全称匹配。
//						或 发货中的发货人存在于"hetfhrgysglb"中，且表中的gongysb_id能和发货相关联;
//					已选择供应商时，合同的发货人为所选供应商
//								或 合同的需方为所选的供应商（分公司） 此处用hetb中的需方单位(xufdwmc) 与电厂信息表中的 string15 为id的全称匹配。
//								或 所选供应商在 hetfhrgysglb 中有合同发货人与之对应。

//			2、运输合同(支持一厂多制，分厂可使用总厂的运输合同)
//				1）、如果没有选择供应商，那么结算时间段内的发货必须有运输合同价格中规定的煤矿；
//					如果指定了供应商，那么供应商煤矿关联中的煤矿信息必须在运输合同价格中得以显示。
//				2）、该分公司必须是运输合同的托运方。

            String strHetspzt = "";
            String sql = "";
            String stryunsdw = "";
            String stryansbh = "";//验收编号判断条件
            String riq = "fahrq";
            String gongysb_id = this.getGongysmlttree_id();

            if (getRbvalue().equals("fahrq") || this.getRbvalue().equals("")) {


                riq = "fahrq";
            } else if (getRbvalue().equals("daohrq")) {
                riq = "daohrq";
            }

            List.add(new IDropDownBean(-1, "请选择"));

            if (MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw()) > -1) {
//				选择了运输单位
                if (getJieslxValue().getId() != Locale.daozyf_feiylbb_id) {
//					倒装运费
                    stryunsdw = " and ys.mingc='" + getYunsdw() + "'";
                } else {

                    stryunsdw = " and yunsdwb_id=" + MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw());
                }

            } else {

                stryunsdw = "";
            }

            if (MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) > -1) {

                stryansbh = " and f.yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh());
            }

            if (!Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())) {
//				如果为空，或是就是电厂id，我们认为是厂级的采购单结算

                if (gongysb_id == null || gongysb_id.equals("")) {    //全部

                    if (((Visit) getPage().getVisit()).isFencb()) {

//						分厂别
                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//						分公司采购，分厂别，煤款结算

                            //大同特殊处理，当选择主结算单位为国电大同二期或国电大同三期时，厂别选择电大同二期和国电大同三期
                            StringBuffer hetrqSql = new StringBuffer();
                            String dtsqlString = "SELECT ID FROM xitxxb WHERE mingc = '大同结算条件特殊处理' AND zhi = '是'";
                            ResultSetList rslxitxx = con.getResultSetList(dtsqlString);

                            if (rslxitxx.next()) {
                                hetrqSql.append("|| '(' || to_char(h.qisrq,'yyyy-mm-dd') || '至' || to_char(h.guoqrq,'yyyy-mm-dd') || ')' AS hetbh");
                            }

                            sql = " select distinct h.id,h.hetbh " + hetrqSql.toString() + " from hetb h,hetslb hs where (h.diancxxb_id in ("
                                    + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                    + ") or hs.diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                    + " )) and h.id = hs.hetb_id(+) and h.gongysb_id in ((select distinct gongysb_id from fahb f where ";
                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + " ) union ("
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }


                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + " ))) \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') order by hetbh ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') order by hetbh ";
                            }

                        } else {
//							分厂别运费结算
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }

                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') \n ";
                            }

                            sql += " and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) or meikxxb_id=0) \n"
                                    + stryunsdw + stryansbh + " and (hetys.diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ") or	\n"
                                    + " hetys.diancxxb_id=(select distinct id from diancxxb where id in (select distinct fuid from diancxxb where id in ("
                                    + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) and jib=" + SysConstant.JIB_DC + "))  \n"
                                    + " order by hetbh ";
                        }

                    } else {

                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							单厂 煤款结算
                            sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id = hs.hetb_id(+) and (h.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                    + " or hs.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                    + ") and h.gongysb_id in ((select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }


                            sql += " liucztb_id=1 " + stryansbh + ") \n union ( \n"
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 " + stryansbh + "))) \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') \n";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') \n";
                            }

                            sql += " order by hetbh";

                        } else {
//							单厂 运费结算
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n";
                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }
                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb where \n ";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and\n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id in (" + ((Visit) getPage().getVisit()).getDiancxxb_id() + ")) or meikxxb_id=0) \n"
                                    + stryunsdw + stryansbh + " and (hetys.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                    + " or hetys.diancxxb_id in (select id from diancxxb where id in (select fuid from diancxxb where id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id() + ") and jib=" + SysConstant.JIB_DC + "))	\n"
                                    + " order by hetbh ";
                        }
                    }

                } else {

                    String mstr_gongys_id = "";
//					选择了供应商
                    if (gongysb_id.indexOf(",") == -1) {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(), gongysb_id);
                    } else {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                                gongysb_id.substring(0, gongysb_id.indexOf(",")));
                    }

                    if (mstr_gongys_id.equals("")) {

                        setMsg("该煤矿没有对应的供应商信息！");
                    } else {

                        if (((Visit) getPage().getVisit()).isFencb()) {
//							选择了供应商，分厂别
                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {

                                //大同特殊处理，当选择主结算单位为国电大同二期或国电大同三期时，厂别选择电大同二期和国电大同三期
                                StringBuffer hetrqSql = new StringBuffer();
                                String dtsqlString = "SELECT ID FROM xitxxb WHERE mingc = '大同结算条件特殊处理' AND zhi = '是'";
                                ResultSetList rslxitxx = con.getResultSetList(dtsqlString);

                                if (rslxitxx.next()) {
                                    hetrqSql.append("|| '(' || to_char(h.qisrq,'yyyy-mm-dd') || '至' || to_char(h.guoqrq,'yyyy-mm-dd') || ')' AS hetbh");
                                }

//								两票、煤款
                                sql = "select distinct h.id,h.hetbh " + hetrqSql + " from hetb h,hetslb hs where h.id=hs.hetb_id(+) and (h.diancxxb_id in ("
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                        + ") or hs.diancxxb_id in ( "
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                        + ")) and (gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n" +
                                        "       from hetfhrgysglb h\n" +
                                        "            where h.gongysb_id=" + mstr_gongys_id + "))	\n ";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";

                            } else {
//								选择了供应商，分厂别，运费结算
                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id = 0) and (hetys.diancxxb_id in ("
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                        + ") or hetys.diancxxb_id in (select distinct id from diancxxb where id in (select distinct fuid from diancxxb where id in ("
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) and jib = " + SysConstant.JIB_DC + ")) 	\n";
                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            }

                        } else {
//
                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							选择了供应商，单厂结算	两票、煤款
                                sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id=hs.hetb_id(+) and (h.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                        + " or hs.diancxxb_id = "
                                        + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                        + " ) and (gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n" +
                                        "       from hetfhrgysglb h\n" +
                                        "            where h.gongysb_id=" + mstr_gongys_id + "))	\n ";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            } else {
//								选择了供应商，单厂，运费结算
                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id = 0)	\n"
                                        + " and (hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                        + " or hetys.diancxxb_id in (select id from diancxxb where id in (select fuid from diancxxb where id="
                                        + ((Visit) getPage().getVisit()).getDiancxxb_id() + "))) 	\n";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";
                            }
                        }
                    }
                }
            } else if (Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())) {
//				如果string15（jiesdwid）不为空，且是分公司采购结算

                if (gongysb_id == null || gongysb_id.equals("")) {    //全部

                    if (((Visit) getPage().getVisit()).isFencb()) {

//						分厂别
                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							分公司，未选择供应商，分厂别，煤款结算

                            String caight = "";
                            if (MainGlobal.getXitxx_item("结算", "是否为山东分公司采购结算单", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
                                caight = "\nand h.leib = 2";
                            }

                            sql = " select distinct h.id,h.hetbh from hetb h where h.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getString15() + caight
                                    + " and (h.gongysb_id in ((select distinct gongysb_id from fahb f where ";
                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + ") union ("
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }


                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + " ))) or h.xufdwmc in (select quanc from diancxxb where id=" + ((Visit) getPage().getVisit()).getString15() + "))\n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id="
                                        + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') order by hetbh ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') order by hetbh ";
                            }

                        } else {
//							分公司，未选择供应商，分厂别，运费结算
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }

                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb f where \n ";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') \n ";
                            }

                            sql += " and liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) or meikxxb_id=0) \n"
                                    + stryunsdw + stryansbh + " and hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getString15() + " 	\n"
                                    + " order by hetbh ";
                        }

                    } else {
//						不分厂别
                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							分公司采购，未选择供应商，不分厂别，煤款结算

                            String caight = "";
                            if (MainGlobal.getXitxx_item("结算", "是否为山东分公司采购结算单", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
                                caight = "\nand h.leib = 2";
                            }

                            sql = " select distinct h.id,h.hetbh from hetb h where h.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getString15() + caight
                                    + " and (h.gongysb_id in ((select distinct gongysb_id from fahb f where ";
                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "  \n"
                                    + stryansbh + ") union ("
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "  \n"
                                    + stryansbh + " ))) or h.xufdwmc in (select quanc from diancxxb where id=" + ((Visit) getPage().getVisit()).getString15() + "))\n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id="
                                        + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') order by hetbh ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') order by hetbh ";
                            }

                        } else {
//							分公司采购，未选择供应商，不分厂别，运费结算
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n";
                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }
                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb where \n ";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and\n ";
                            }

                            sql += " liucztb_id=1 and dianxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + ") or meikxxb_id = 0) \n"
                                    + stryunsdw + stryansbh + " and hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getString15() + " 	\n"
                                    + " order by hetbh ";
                        }
                    }

                } else {

                    String mstr_gongys_id = "";
                    if (gongysb_id.indexOf(",") == -1) {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(), gongysb_id);
                    } else {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                                gongysb_id.substring(0, gongysb_id.indexOf(",")));
                    }

                    if (mstr_gongys_id.equals("")) {

                        setMsg("该煤矿没有对应的供应商信息！");
                    } else {

                        if (((Visit) getPage().getVisit()).isFencb()) {

                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//								分公司采购，选择了供应商，一厂多制，煤款结算
                                sql = "select distinct h.id,h.hetbh from hetb h where \n"
                                        + " h.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getString15()
                                        + " and ((gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n"
                                        + "       from hetfhrgysglb h\n"
                                        + "            where h.gongysb_id=" + mstr_gongys_id + "))	\n "
                                        + " or h.xufdwmc=(select quanc from diancxxb where id="
                                        + ((Visit) getPage().getVisit()).getString15() + "))";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

//										sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by hetbh ";
                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";

                            } else {
//								分公司采购，选择了供应商，一厂多制，运费结算
                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id=0) \n"
                                        + " and hetys.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getString15();
                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            }

                        } else {
//
                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//								分公司采购，选择了供应商，单一电厂，煤款结算
                                sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id=hs.hetb_id(+) and h.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getString15()
                                        + " and ((gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n"
                                        + "       from hetfhrgysglb h\n"
                                        + "            where h.gongysb_id=" + mstr_gongys_id + "))	\n "
                                        + " or h.xufdwmc in (select quanc from diancxxb where id="
                                        + ((Visit) getPage().getVisit()).getString15() + "))";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            } else {

                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id=0) 	\n"
                                        + " and hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getString15();

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";
                            }
                        }
                    }
                }
            }

            ResultSetList rsl = con.getResultSetList(sql);
            if (rsl.getRows() == 0 && !gongysb_id.equals("")) {

                if (!((Visit) getPage().getVisit()).getString17().equals("tb")) {

                    setMsg("该供应商在系统里没有对应的合同！");
                }
            }

            while (rsl.next()) {

                List.add(new IDropDownBean(rsl.getLong("id"), rsl.getString("hetbh")));
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        ((Visit) getPage().getVisit())
                .setProSelectionModel4(new IDropDownModel(List));
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }


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
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return diancmc;

    }

    // 得到电厂树下拉框的电厂名称或者分公司,集团的名称d
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
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    //厂别
    public boolean _Fencbchange = false;

    public IDropDownBean getFencbValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean5((IDropDownBean) getFencbModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean5();
    }

    public void setFencbValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

            ((Visit) getPage().getVisit()).setboolean3(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean5(Value);
    }

    public IPropertySelectionModel getFencbModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

            getFencbModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

    public void setFencbModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getFencbModels() {

//			String sql ="select id,mingc from diancxxb d where d.fuid="+
//			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";
        JDBCcon con = new JDBCcon();
        String sql =
                "select id,mingc from diancxxb d where d.fuid="
                        + ((Visit) getPage().getVisit()).getDiancxxb_id() + " \n" +
                        "order by xuh,mingc";
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.getRows() == 0) {

            sql = "select id,mingc from diancxxb where id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
        }
        rsl.close();
        con.Close();
        ((Visit) getPage().getVisit())
                .setProSelectionModel5(new IDropDownModel(sql));
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

//	运输单位_begin

    public IDropDownBean getYunsdwValue() {
       logger.info ("getYunsdwValue:"+((Visit) getPage().getVisit()).getDropDownBean6());
        if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean6((IDropDownBean) getYunsdwModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean6();
    }

    public void setYunsdwValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean6()) {

            ((Visit) getPage().getVisit()).setboolean2(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean6(Value);
    }

    public IPropertySelectionModel getYunsdwModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {

            getYunsdwModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }

    public void setYunsdwModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getYunsdwModels() {

        String Diancxxb_id = "0";
        String sql = "";
        if (((Visit) getPage().getVisit()).isFencb()) {

            Diancxxb_id = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());
        } else {

            Diancxxb_id = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
        }

        if (this.getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//			如果是“倒装运费”结算，要从倒装车皮表取值

            sql = "select distinct rownum as id,yunsdw \n" +
                    "		from fahb fh,daozcpb c,zhilb z\n" +
                    "		where fh.id=c.fahb_id \n" +
                    "			and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd')\n" +
                    "			and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd')\n" +
                    "			and fh.diancxxb_id in (" + Diancxxb_id + ")	\n" +
                    "			and fh.liucztb_id=1 and z.liucztb_id=1\n" +
                    "			and fh.zhilb_id = z.id" +
                    "		order by c.yunsdw";

        } else {

            sql = "select distinct y.id,y.mingc\n" +
                    "		from fahb fh,chepb c,zhilb z,yunsdwb y\n" +
                    "		where fh.id=c.fahb_id and c.yunsdwb_id=y.id\n" +
                    "			and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd')\n" +
                    "			and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd')\n" +
                    "			and fh.diancxxb_id in (" + Diancxxb_id + ")	\n" +
                    "			and fh.liucztb_id=1 and z.liucztb_id=1\n" +
                    "			and fh.zhilb_id = z.id" +
                    "		order by y.mingc";
        }

        ((Visit) getPage().getVisit())
                .setProSelectionModel6(new IDropDownModel(sql, "全部"));
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }
//	运输单位_end

//	品种_begin

    public IDropDownBean getPinzValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean7((IDropDownBean) getPinzModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean7();
    }

    public void setPinzValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean7()) {

            ((Visit) getPage().getVisit()).setboolean2(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean7(Value);
    }

    public IPropertySelectionModel getPinzModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

            getPinzModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }

    public void setPinzModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getPinzModels() {

        String Diancxxb_id = "0";
        if (((Visit) getPage().getVisit()).isFencb()) {

            Diancxxb_id = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());
        } else {

            Diancxxb_id = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
        }

        String sql = "select distinct p.id,p.mingc\n" +
                "               from fahb fh,zhilb z,pinzb p\n" +
                "               where fh.pinzb_id = p.id\n" +
                "                     and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd')\n" +
                "                     and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd')\n" +
                "					  and fh.diancxxb_id in (" + Diancxxb_id + ")	\n" +
                "                     and fh.zhilb_id=z.id\n" +
                "                     and fh.liucztb_id=1 and z.liucztb_id=1\n" +
                "               order by p.mingc";

        ((Visit) getPage().getVisit())
                .setProSelectionModel7(new IDropDownModel(sql, "全部"));
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }
//	品种_end

    //	tree_id_begin
    private String treeid;

    /*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
    public String getTreeid() {
        // if(treeid==null||treeid.equals("")){
        // ((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit)
        // this.getPage().getVisit()).getDiancxxb_id()));
        // }
        if (((Visit) getPage().getVisit()).getString2() == null || ((Visit) getPage().getVisit()).getString2().equals("")) {

            ((Visit) getPage().getVisit()).setString2("0");
        }
        return ((Visit) getPage().getVisit()).getString2();
    }

    public void setTreeid(String treeid) {

        if (!((Visit) getPage().getVisit()).getString2().equals(treeid)) {

            ((Visit) getPage().getVisit()).setString2(treeid);
            ((Visit) getPage().getVisit()).setboolean2(true);
        }
    }
//	tree_id_end

//	gongysTree_id_begin

    public String getGongysmlttree_id() {

        String gongys_id = "";
        if (((Visit) getPage().getVisit()).getString14() != null
                && !((Visit) getPage().getVisit()).getString14().equals("")) {

            String change[] = ((Visit) getPage().getVisit()).getString14().split(";");
            if (change.length == 1 &&
                    (change[0].indexOf("+") == -1 && change[0].indexOf("-") == -1)) {

                gongys_id = change[0];
            } else {

                for (int i = 0; i < change.length; i++) {
                    if (change[i] == null || "".equals(change[i])) {
                        continue;
                    }
                    String record[] = change[i].split(",", 2);
                    String sign = record[0];
                    String ziyid = record[1];
                    if ("+".equals(sign)) {
                        gongys_id += ziyid + ",";
                    }
                }

                if (!gongys_id.equals("")) {

                    gongys_id = gongys_id.substring(0, gongys_id.lastIndexOf(","));
                }
            }

        }

        if (gongys_id.indexOf(",") == -1) {

            if (gongys_id.equals("")) {

                this.setTreeid("0");
            } else {

                this.setTreeid(MainGlobal.getLeaf_ParentNodeId(this.getTree(), gongys_id));
            }

        } else {

            this.setTreeid(MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                    gongys_id.substring(0, gongys_id.indexOf(","))));
        }

        return gongys_id;
    }

    public void setGongysmlttree_id(String value) {

        ((Visit) getPage().getVisit()).setString14(value);
    }

//	gongysTree_id_end

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
        return getTree().getWindowTreeScript();
    }

    // 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂

    public String getWunScript() {

        return "";
    }
}