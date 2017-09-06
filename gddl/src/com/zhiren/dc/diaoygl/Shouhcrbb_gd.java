package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.zhiren.webservice.InterFac_dt;
import com.zhiren.webservice.shujsc.Shujsc;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：李琛基
 * 时间：2010-8-19
 * 描述：根据国电要求，修改收耗存日报，增加三个字段：不可调煤量、厂外存煤、可调库存
 * 其中，不可调煤量取的是上一天的数值，可调库存=库存-不可调煤量。
 * 
 */
/*
 * 修改人：李琛基
 * 修改时间：2010-11-1
 * 描述：
 * 		添加生成数据时的约束：如果上一天电厂里没有录入数据，那么当天的数据也不能录入
 * 
 */
/*
 * 作者：夏峥
 * 修改时间：2011-11-23
 * 适用范围：国电电力及其下属单位
 * 描述：用户点击生成按钮时同时生成收耗存日报和分矿日估价信息
 *		  日收耗存录入界面，在每次刷新界面时查询所选日期的所在月1日至所选日期为止是否存在不完整的数据（即耗用为0的数据），
 *			如果存在则提示最早一次存在不完整数据的日期中存在不完整数据，并提示用户需对该日的信息进行补录。
 *		日收耗存录入界面，在用户点击保存按钮时弹出“耗用库存是否正确”的对话框并要求用户确认，如果用户不确认则返回到当前界面且不保存。
 *		日收耗存录入界面在每月的1日可以调整上月月末的水分差、存损、调整量、盘盈亏信息，其他时间该信息不可用，
 *			但可由系统参数‘收耗存日报库存调整信息可编辑’对其进行解锁操作。
 */
/*
 * 作者：夏峥
 * 时间：2011-11-29
 * 使用范围：国电电力及其下属单位
 * 描述：新增收耗存日报实时更新库存功能（即更改某一日库存后滚动计算之后的库存信息）
 * 		在新增时更新当日的来煤量-耗用信息滚动新增至日期后的库存中
 */	
/*
 * 作者：夏峥
 * 时间：2012-02-07
 * 使用范围：国电电力及其下属单位
 * 描述：变更为每月1日至4日可通过修改调整量等信息调整库存信息
 */	
/*
 * 作者：夏峥
 * 时间：2012-02-29
 * 使用范围：国电电力及其下属单位
 * 描述：当所选单位有分子单位且系统配置‘分厂别总厂显示生成按钮’的值为否时，
 * 		 系统将隐藏生成，删除，保存按钮。
 */	
/*
 * 作者：夏峥
 * 时间：2012-08-10
 * 适用范围：国电电力及其下属电厂（不包括庄河和邯郸）
 * 描述：增加厂外煤场进煤量列，厂外煤场进煤量将影响库存。
 */
/*
 * 作者：夏峥
 * 时间：2012-10-29
 * 适用范围：国电电力及其下属电厂
 * 描述：修正可调库存计算未计算的BUG
 */
/*
 * 作者：赵胜男
 * 时间：2013-03-04
 * 适用范围：国电电力公司
 * 描述：根据需求新增供热量一列，将所有列给出单位，并修改getselectDAte()里的存值方式
 *      
 */	
/*
 * 作者:夏峥
 * 日期:2013-03-25
 * 修改内容:将供热量的单位变更为吉焦。
 */

public abstract class Shouhcrbb_gd extends BasePage {
    //	界面用户提示
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
    }

    //	绑定日期
    private String riq;

    public String getRiq() {
        return riq;
    }

    public void setRiq(String riq) {
        this.riq = riq;
    }

    //	页面变化记录
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    //	厂别下拉框
    public IDropDownBean getChangbValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
            if (getChangbModel().getOptionCount() > 0) {
                setChangbValue((IDropDownBean) getChangbModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean2();
    }

    public void setChangbValue(IDropDownBean value) {
        ((Visit) this.getPage().getVisit()).setDropDownBean2(value);
    }

    public IPropertySelectionModel getChangbModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
            setChangbModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
    }

    public void setChangbModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
    }

    public void setChangbModels() {
        Visit visit = (Visit) this.getPage().getVisit();
        StringBuffer sb = new StringBuffer();
        if (visit.isFencb()) {
            sb.append("select id,mingc from diancxxb where fuid=" + visit.getDiancxxb_id());
        } else {
            sb.append("select id,mingc from diancxxb where id=" + visit.getDiancxxb_id());
        }
        setChangbModel(new IDropDownModel(sb.toString()));
    }

    private void Save() {
        Visit visit = (Visit) this.getPage().getVisit();
        JDBCcon con = new JDBCcon();
        ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
        StringBuffer sql = new StringBuffer("begin \n");
        while (mdrsl.next()) {
            String mokmc = "";
            if (!(mokmc == null) && !mokmc.equals("")) {
                String id = mdrsl.getString("id");
                //更改时增加日志
                MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit.getRenymc(), SysConstant.RizOpType_UP, mokmc, "Shouhcrbb", id);
            }
            sql.append("update Shouhcrbb set ");
            for (int i = 1; i < mdrsl.getColumnCount(); i++) {
                if(!mdrsl.getColumnNames()[i].equals("KUC")){
                    sql.append(mdrsl.getColumnNames()[i]).append(" = ");
                    sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i))).append(",\n");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" kuc=(select kuc from shouhcrbb where riq=date'"+this.getRiq()+"'-1) where id =").append(mdrsl.getString("ID")).append(";\n");
            sql.append("update shouhcrbb set kuc=kuc+dangrgm+tiaozl-fady-gongry-qity-cuns+shuifctz \n where id="+mdrsl.getString("ID")+";");
//			判断是否同步更新库存 默认同步更新设置为否时不同步更新
            if (MainGlobal.getXitxx_item("收耗存日报", "收耗存日报实时更新库存", "0", "否").equals("是")) {
                String kuc_sql = "select kuc from shouhcrbb where diancxxb_id=" + getTreeid() + " and riq = " + DateUtil.FormatOracleDate(this.getRiq());
                ResultSetList kuc_rsl = con.getResultSetList(kuc_sql);
                if (kuc_rsl.next()) {
                    double kuccha = CustomMaths.sub(mdrsl.getDouble("kuc"), kuc_rsl.getDouble("KUC"));
                    //				更新当前日期以后的所有库存
                    sql.append("update shouhcrbb set ")
                            .append("kuc = kuc + ").append(kuccha)
                            .append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
                            .append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
//					更新当前日期以后的所有可调库存
                    sql.append("update shouhcrbb set ")
                            .append("kedkc = kuc -bukdml ")
                            .append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
                            .append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
                }
                kuc_rsl.close();
            }
        }
        mdrsl.close();
        sql.append("end;");
        int flag = con.getUpdate(sql.toString());
        if (flag == -1) {
            setMsg("收耗存库存信息更新失败");
        }
        con.Close();
    }

    private boolean _SaveClick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveClick = true;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }

    private boolean _SubmitButton = false;

    public void SubmitButton(IRequestCycle cycle) {
        _SubmitButton = true;
    }

    private boolean isParentDc(JDBCcon con) {
        String sql = "select * from diancxxb where fuid = " + getTreeid();
        return con.getHasIt(sql);
    }

    private void CreateData() {
//		Visit visit = (Visit) getPage().getVisit();
        long diancxxb_id = Long.parseLong(getTreeid());
        JDBCcon con = new JDBCcon();
//		生成时自动生成日收耗存和分矿数据
        AutoCreateDaily_Report_gd RP = new AutoCreateDaily_Report_gd();
        String rbb = RP.CreateRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
        String fcb = RP.CreateFCB(con, diancxxb_id, DateUtil.getDate(getRiq()));
        String Smsg = "";
        if (rbb.length() > 0) {
            Smsg += rbb + "<br>";
        }
        if (fcb.length() > 0) {
            Smsg += fcb + "<br>";
        }
        if (Smsg.length() > 0) {
            setMsg(Smsg);
        }
        con.Close();
    }

    private boolean _CreateClick = false;

    public void CreateButton(IRequestCycle cycle) {
        _CreateClick = true;
    }

    private void DelData() {
        String CurDate = DateUtil.FormatOracleDate(getRiq());
        String diancxxb_id = getTreeid();
        StringBuffer sb = new StringBuffer();
        JDBCcon con = new JDBCcon();
        sb.append("begin \n");
//		判断是否同步更新库存 默认同步更新设置为否时不同步更新
        if (MainGlobal.getXitxx_item("收耗存日报", "收耗存日报实时更新库存", "0", "否").equals("是")) {
            String kuc_sql = "select jingz+CHANGWML - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk kucca from shouhcrbb where diancxxb_id=" + getTreeid() + " and riq = " + DateUtil.FormatOracleDate(this.getRiq());
            ResultSetList kuc_rsl = con.getResultSetList(kuc_sql);
            if (kuc_rsl.next()) {
//					更新当前日期以后的所有库存
                sb.append("update shouhcrbb set ")
                        .append("kuc = kuc - ").append(kuc_rsl.getDouble("KUCCA"))
                        .append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
                        .append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
//					更新当前日期以后的所有可调库存
                sb.append("update shouhcrbb set ")
                        .append("kedkc = kuc -bukdml ")
                        .append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
                        .append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
            }
            kuc_rsl.close();
        }
        sb.append("delete from shouhcrbb where diancxxb_id=").append(diancxxb_id).append(" and riq=").append(CurDate).append(";\n");
        sb.append("end;");

        con.getUpdate(sb.toString());
        con.Close();
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
        }

        if (_CreateClick) {
            _CreateClick = false;
            CreateData();
        }
        if (_DelClick) {
            _DelClick = false;
            DelData();
        }
        if (_SubmitButton) {
            _SubmitButton = false;
            submit();
        }
        getSelectData();
    }

    private void submit() {
        JDBCcon con = new JDBCcon();
        try {
//            System.out.println("submit");

            String riq = this.getRiq();
            String sql = "select id from shouhcrbb where riq=date'" + riq + "'";
            ResultSet rs = con.getResultSet(sql);
            String id;
            if (rs.next()) {
                id = rs.getString("id");
            } else {
                this.setMsg("数据不存在!");
                return;
            }
            //添加接口任务
            Shujsc sc = new Shujsc();
            sc.addjiekrw(id, "shouhcrbb", "0", "197", riq);
            sc.setEndpointAddress("http://localhost/zdt/InterCom_dt.jws");
            sc.request("shouhcrbb");//执行接口任务
            sql = "update shouhcrbb set zhaungt=1 where id=" + id;
            int r = con.getUpdate(sql);
            if (r != -1) {
                this.setMsg("提交成功!");
            } else {
                this.setMsg("提交失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            con.rollBack();
        } finally {
            con.Close();
        }
    }

    public void getSelectData() {
        JDBCcon con = new JDBCcon();
        try {
            Visit visit = (Visit) getPage().getVisit();
            String CurDate = DateUtil.FormatOracleDate(getRiq());
            String diancxxb_id = getTreeid();
            StringBuffer sb = new StringBuffer();
            sb.append("select s.id,s.dangrgm,s.yuns,s.jingz,s.fady,s.gongry,/*s.fadl, s.gongrl,*/ s.qity,s.cuns,s.tiaozl, s.panyk,s.shuifctz,s.kuc/*," +
                    "s.jingz as dangrgm, s.biaoz,  s.yingd, s.kuid, s.fady, \n")
                    .append("s.gongry,s.fady+s.gongry+s.qity+s.cuns+s.feiscy as haoyqkdr,s.feiscy, " +
                            " s.tiaozl, s.changwml, s.bukdml,s.kedkc */\n")
                    .append("from shouhcrbb s where diancxxb_id =")
                    .append(diancxxb_id).append(" and riq=").append(CurDate);


            ResultSetList rsl = con.getResultSetList(sb.toString());
            if (rsl == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
                setMsg(ErrorMessage.NullResult);
                return;
            }
            ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
            egu.setTableName("shouhcrbb");
            egu.setWidth("bodyWidth");
            egu.addPaging(0);
            egu.setGridType(ExtGridUtil.Gridstyle_Edit);
            egu.getColumn("id").setHidden(true);
            egu.getColumn("id").setEditor(null);
            egu.getColumn("dangrgm").setHeader("入厂量<br>(吨)");
            egu.getColumn("dangrgm").setWidth(80);
//		egu.getColumn("dangrgm").setHidden(true);
//		egu.getColumn("haoyqkdr").setHidden(true);
            egu.getColumn("yuns").setHeader("运损量<br>(吨)");
            egu.getColumn("yuns").setWidth(60);
            egu.getColumn("jingz").setHeader("净重<br>(吨)");
            egu.getColumn("jingz").setWidth(60);
            egu.getColumn("fady").setHeader("发电用<br>(吨)");
            egu.getColumn("fady").setWidth(60);
            egu.getColumn("gongry").setHeader("供热用<br>(吨)");
            egu.getColumn("gongry").setWidth(60);
            egu.getColumn("qity").setHeader("其它用<br>(吨)");
            egu.getColumn("qity").setWidth(60);
            egu.getColumn("cuns").setHeader("存损<br>(吨)");
            egu.getColumn("cuns").setWidth(60);
            egu.getColumn("tiaozl").setHeader("调整量<br>(吨)");
            egu.getColumn("tiaozl").setWidth(60);
            egu.getColumn("panyk").setHeader("盘盈亏<br>(吨)");
            egu.getColumn("panyk").setWidth(60);
            egu.getColumn("shuifctz").setHeader("水分差调整<br>(吨)");
            egu.getColumn("shuifctz").setWidth(80);
            egu.getColumn("kuc").setHeader("库存<br>(吨)");
            egu.getColumn("kuc").setWidth(80);

//        egu.getColumn("fadl").setHeader("发电量<br>(万千瓦时)");
//		egu.getColumn("fadl").setWidth(80);
//
//		egu.getColumn("gongrl").setHeader("供热量<br>(吉焦)");
//		egu.getColumn("gongrl").setWidth(70);


//		egu.getColumn("biaoz").setHeader("票重<br>(吨)");
//		egu.getColumn("biaoz").setWidth(60);


//		egu.getColumn("yingd").setHeader("盈吨<br>(吨)");
//		egu.getColumn("yingd").setWidth(60);
//
//		egu.getColumn("kuid").setHeader("亏吨<br>(吨)");
//		egu.getColumn("kuid").setWidth(60);
//
//
//
//		egu.getColumn("feiscy").setHeader("非生产用<br>(吨)");
//		egu.getColumn("feiscy").setWidth(60);
//
//
//
//
//		egu.getColumn("bukdml").setHeader("不可调<br>煤量(吨)");
//		egu.getColumn("bukdml").setWidth(60);
//		egu.getColumn("changwml").setHeader("厂外煤场<br>进煤量(吨)");
//		egu.getColumn("changwml").setWidth(80);
//		egu.getColumn("kedkc").setHeader("可调库存<br>(吨)");
//		egu.getColumn("kedkc").setWidth(60);
//		egu.getColumn("kedkc").setHidden(true);
            //egu.getColumn("kedkc").setEditor(null);
//		判断当前系统日期是否为本月1日至4日。如果为本月1日至4日，那么所选的日期是否为上月月末，如果条件都成立则可以查出数据
            String sql = "SELECT 1 FROM (SELECT FIRST_DAY(SYSDATE) FD,\n" +
                    "               TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)), 'yyyy-mm-dd') LD\n" +
                    "          FROM DUAL) SDAY,\n" +
                    "       (SELECT TO_CHAR(SYSDATE, 'yyyy-mm-dd') FD,\n" +
                    "               TO_CHAR(" + CurDate + ", 'yyyy-mm-dd') LD\n" +
                    "          FROM DUAL) UDAY\n" +
                    " WHERE SDAY.LD = UDAY.LD\n" +
                    "	AND SYSDATE BETWEEN SDAY.FD AND SDAY.FD+3";
//		如果不能查询出数据那么存损，调整量，水分差调整和盘盈亏信息将不可显示且不可编辑
//            if (!con.getHasIt(sql) && MainGlobal.getXitxx_item("收耗存日报", "收耗存日报库存调整信息可编辑", "0", "否").equals("否")) {
//                egu.getColumn("cuns").setEditor(null);
//                egu.getColumn("cuns").setHidden(true);
//                egu.getColumn("tiaozl").setEditor(null);
//                egu.getColumn("tiaozl").setHidden(true);
//                egu.getColumn("shuifctz").setEditor(null);
//                egu.getColumn("shuifctz").setHidden(true);
//                egu.getColumn("panyk").setEditor(null);
                egu.getColumn("panyk").setHidden(true);
//            }

//            if (!MainGlobal.getXitxx_item("收耗存日报", "收耗存日报来煤可编辑", "0", "否").equals("是")) {
//			egu.getColumn("biaoz").setEditor(null);
                egu.getColumn("jingz").setEditor(null);
                egu.getColumn("yuns").setEditor(null);
//			egu.getColumn("yingd").setEditor(null);
//			egu.getColumn("kuid").setEditor(null);
                egu.getColumn("kuc").setEditor(null);
//            }
            egu.addTbarText("日期:");
            DateField df = new DateField();
            df.Binding("RIQ", "");
            df.setValue(getRiq());
            egu.addToolbarItem(df.getScript());
            egu.addTbarText("-");// 设置分隔符
            // 设置树
            egu.addTbarText("单位:");
            ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                    ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
                    .getVisit()).getDiancxxb_id(), getTreeid());
            setTree(etu);
            egu.addTbarTreeBtn("diancTree");
//		刷新按钮
            StringBuffer rsb = new StringBuffer();
            rsb.append("function (){")
                    .append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('RIQ').value+'的数据,请稍候！'", true))
                    .append("document.getElementById('RefreshButton').click();}");
            GridButton gbr = new GridButton("刷新", rsb.toString());
            gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
            egu.addTbarBtn(gbr);
//		生成按钮
//            if (visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("收耗存日报", "分厂别总厂显示生成按钮", diancxxb_id, "否").equals("否")) {
//
//            } else {
            sql = "select zhaungt from shouhcrbb where riq=date'" + this.getRiq() + "'";
            ResultSet rs = con.getResultSet(sql);
            boolean z = false;
            if (rs.next()) {
                z = rs.getInt("zhaungt") != 0;
            }
            GridButton gbc = new GridButton("生成", getBtnHandlerScript("CreateButton"));
            gbc.setIcon(SysConstant.Btn_Icon_Create);
            gbc.setDisabled(z);
            egu.addTbarBtn(gbc);
//			删除按钮
            GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
            gbd.setIcon(SysConstant.Btn_Icon_Delete);
            gbd.setDisabled(z);
            egu.addTbarBtn(gbd);
//			重写保存按钮
            String Script = "function (){\n" +
                    "Ext.MessageBox.confirm('提示信息','耗用、库存信息是否正确？',function(btn){if(btn == 'yes'){\n" +
                    "var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();\n" +
                    "for(i = 0; i< Mrcd.length; i++){\n" +
                    "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n" +
                    "if(Mrcd[i].get('DANGRGM') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段DANGRGM 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('DANGRGM') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 DANGRGM 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('DANGRGM')!=0 && Mrcd[i].get('DANGRGM') == ''){Ext.MessageBox.alert('提示信息','字段 DANGRGM 不能为空');return;\n" +
                    "}if(Mrcd[i].get('FADY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段发电用 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('FADY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 发电用 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('FADY')!=0 && Mrcd[i].get('FADY') == ''){Ext.MessageBox.alert('提示信息','字段 发电用 不能为空');return;\n" +
                    "}if(Mrcd[i].get('GONGRY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段供热用 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('GONGRY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 供热用 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('GONGRY')!=0 && Mrcd[i].get('GONGRY') == ''){Ext.MessageBox.alert('提示信息','字段 供热用 不能为空');return;\n" +
                    "}if(Mrcd[i].get('QITY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段其它用 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('QITY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 其它用 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('QITY')!=0 && Mrcd[i].get('QITY') == ''){Ext.MessageBox.alert('提示信息','字段 其它用 不能为空');return;\n" +
                    "}if(Mrcd[i].get('CUNS') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段存损 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('CUNS') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 存损 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('CUNS')!=0 && Mrcd[i].get('CUNS') == ''){Ext.MessageBox.alert('提示信息','字段 存损 不能为空');return;\n" +
                    "}if(Mrcd[i].get('TIAOZL') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段调整量 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('TIAOZL') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 调整量 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('TIAOZL')!=0 && Mrcd[i].get('TIAOZL') == ''){Ext.MessageBox.alert('提示信息','字段 调整量 不能为空');return;\n" +
                    "}if(Mrcd[i].get('SHUIFCTZ') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段水分差调整 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('SHUIFCTZ') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 水分差调整 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('SHUIFCTZ')!=0 && Mrcd[i].get('SHUIFCTZ') == ''){Ext.MessageBox.alert('提示信息','字段 水分差调整 不能为空');return;\n" +
                    "}if(Mrcd[i].get('PANYK') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段盘盈亏 低于最小值 -100000000');return;\n" +
                    "}if( Mrcd[i].get('PANYK') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 盘盈亏 高于最大值 100000000000');return;\n" +
                    "}if(Mrcd[i].get('PANYK')!=0 && Mrcd[i].get('PANYK') == ''){Ext.MessageBox.alert('提示信息','字段 盘盈亏 不能为空');return;\n" +
                    "}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" +
//				"+ '<FADL update=\"true\">' + Mrcd[i].get('FADL')+ '</FADL>'\n"+
//				"+ '<GONGRL update=\"true\">' + Mrcd[i].get('GONGRL')+ '</GONGRL>'\n"+
                    "+ '<JINGZ update=\"true\">' + Mrcd[i].get('JINGZ')+ '</JINGZ>'\n" +
                    "+ '<DANGRGM update=\"true\">' + Mrcd[i].get('DANGRGM')+ '</DANGRGM>'\n" +
//				"+ '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'\n"+
                    "+ '<YUNS update=\"true\">' + Mrcd[i].get('YUNS')+ '</YUNS>'\n" +
//				"+ '<YINGD update=\"true\">' + Mrcd[i].get('YINGD')+ '</YINGD>'\n"+
//				"+ '<KUID update=\"true\">' + Mrcd[i].get('KUID')+ '</KUID>'\n"+
                    "+ '<FADY update=\"true\">' + Mrcd[i].get('FADY')+ '</FADY>'\n" +
                    "+ '<GONGRY update=\"true\">' + Mrcd[i].get('GONGRY')+ '</GONGRY>'\n" +
                    "+ '<QITY update=\"true\">' + Mrcd[i].get('QITY')+ '</QITY>'\n" +
//				"+ '<HAOYQKDR update=\"true\">' + Mrcd[i].get('HAOYQKDR')+ '</HAOYQKDR>'\n"+
//				"+ '<FEISCY update=\"true\">' + Mrcd[i].get('FEISCY')+ '</FEISCY>'\n"+
                    "+ '<CUNS update=\"true\">' + Mrcd[i].get('CUNS')+ '</CUNS>'\n" +
                    "+ '<TIAOZL update=\"true\">' + Mrcd[i].get('TIAOZL')+ '</TIAOZL>'\n" +
                    "+ '<SHUIFCTZ update=\"true\">' + Mrcd[i].get('SHUIFCTZ')+ '</SHUIFCTZ>'\n" +
                    "+ '<PANYK update=\"true\">' + Mrcd[i].get('PANYK')+ '</PANYK>'\n" +
                    "+ '<KUC update=\"true\">' + Mrcd[i].get('KUC')+ '</KUC>'\n" +
//				"+ '<CHANGWML update=\"true\">' + Mrcd[i].get('CHANGWML')+ '</CHANGWML>'\n"+
//				"+ '<BUKDML update=\"true\">' + Mrcd[i].get('BUKDML')+ '</BUKDML>'\n"+
//				"+ '<KEDKC update=\"true\">' + Mrcd[i].get('KEDKC')+ '</KEDKC>'\n"+
                    " + '</result>' ; }\n" +
                    "if(gridDiv_history=='' && gridDivsave_history==''){\n" +
                    "Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n" +
                    "}else{\n" +
                    "var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" +
                    "document.getElementById('SaveButton').click();\n" +
                    "Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" +
                    "}\n" +
                    "};});\n" +
                    "}";
//			
//			GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
//			egu.addTbarBtn(gbs);
            GridButton gbs = new GridButton("保存", Script);
            gbs.setIcon(SysConstant.Btn_Icon_Save);
            gbs.setDisabled(z);
            egu.addTbarBtn(gbs);
            GridButton tj = new GridButton("提交", getBtnHandlerScript("SubmitButton"));
            tj.setIcon(SysConstant.Btn_Icon_SelSubmit);
            tj.setDisabled(z);
            egu.addTbarBtn(tj);

//            }

//		grid 计算方法
            egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);\n");

            AutoCreateDaily_Report_gd DR = new AutoCreateDaily_Report_gd();
            String msg = DR.ChkRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
            if (msg.length() > 0) {
                egu.addOtherScript("Ext.MessageBox.alert('提示信息','" + msg + "日数据不完整！');\n");
            }

            setExtGrid(egu);
        } catch (Exception e) {
            e.printStackTrace();
            con.rollBack();
        } finally {
            con.Close();
        }

    }

    public String getBtnHandlerScript(String btnName) {
//		按钮的script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = "'+Ext.getDom('RIQ').value+'";
        btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("新生成数据将同时覆盖:日收耗存和日估价<br>")
                    .append(cnDate).append("的已存数据，是否继续？");
        } else if (btnName.endsWith("DelButton")) {
            btnsb.append("是否删除").append(cnDate).append("的数据？");
        } else {
            btnsb.append("是否提交").append(cnDate).append("的数据？");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('").append(btnName).append("').click()")
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
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
            setChangbModels();
            getSelectData();
        }
    }

    private String treeid;

    public String getTreeid() {
        if (treeid == null || treeid.equals("")) {
            treeid = String.valueOf(((Visit) getPage().getVisit())
                    .getDiancxxb_id());
        }
        return treeid;
    }

    public void setTreeid(String treeid) {
        this.treeid = treeid;
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
        return getTree().getWindowTreeScript();
    }
}
