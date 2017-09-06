package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.zhiren.webservice.shujsc.Shujsc;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：王磊
 * 时间：2009-11-16
 * 描述：设置各种数值的默认值，处理新添加时如果为null保存不上的问题
 */
public abstract class Shouhcrbyb extends BasePage {
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
        int flag = visit.getExtGrid1().Save(getChange(), visit);
        if (flag != -1) {
            setMsg(ErrorMessage.SaveSuccessMessage);
        }
    }

    private boolean _SaveClick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveClick = true;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }
    /*private void CreateData() {
		DelData();
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String LasDate = DateUtil.FormatOracleDate(DateUtil.AddDate(DateUtil.getDate(getRiq()), -1, DateUtil.AddType_intDay));
		long diancxxb_id = visit.getDiancxxb_id();
		if(visit.isFencb() && getChangbValue()!=null) {
			diancxxb_id = getChangbValue().getId();
		}
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("insert into shouhcrbyb(id,diancxxb_id, riq, pinzb_id, shourl, fady, gongry, qity, cuns, panyk, kuc) \n")
		.append("(select getnewid(").append(diancxxb_id).append(") id,").append(diancxxb_id).append(",")
		.append(CurDate).append(", p.id pinzb_id,0 shourl, 0 fady, 0 gongry, 0 qity, 0 cuns, 0 panyk, ")
		.append(" nvl((select kuc from shouhcrbyb where diancxxb_id =").append(diancxxb_id).append(" and riq = ")
		.append(LasDate).append(" and pinzb_id = p.id),0) from pinzb p where leib='油' )");
		con.getInsert(sb.toString());
		con.Close();
	}*/

    /*private boolean _CreateClick = false;

    public void CreateButton(IRequestCycle cycle) {
        _CreateClick = true;
    }*/
    private void DelData() {
        Visit visit = (Visit) getPage().getVisit();
        String CurDate = DateUtil.FormatOracleDate(getRiq());
        long diancxxb_id = visit.getDiancxxb_id();
        if (visit.isFencb() && getChangbValue() != null) {
            diancxxb_id = getChangbValue().getId();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("delete from shouhcrbyb where diancxxb_id=")
                .append(diancxxb_id).append(" and riq=")
                .append(CurDate);
        JDBCcon con = new JDBCcon();
        con.getDelete(sb.toString());
        con.Close();
    }

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }

    private boolean _SubmitButton = false;

    public void SubmitButton(IRequestCycle cycle) {
        _SubmitButton = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveClick) {
            _SaveClick = false;
            Save();
            getSelectData(null);
        }
        if (_Refreshclick) {
            _Refreshclick = false;
            getSelectData(null);
        }

//		if (_CreateClick) {
//			_CreateClick = false;
//			CreateData();
//			getSelectData(null);
//		}
        if (_DelClick) {
            _DelClick = false;
            DelData();
        }
        if (_SubmitButton) {
            _SubmitButton = false;
            submit();
        }
    }

    private void submit() {
        JDBCcon con = new JDBCcon();
        try {
//            System.out.println("submit");

            String riq = this.getRiq();
            String sql = "select id from shouhcrbyb where riq=date'" + riq + "'";
            ResultSet rs = con.getResultSet(sql);
            String id;
            //添加接口任务
            Shujsc sc = new Shujsc();
            while (rs.next()) {
                id = rs.getString("id");
                sc.addjiekrw(id, "shouhcrbyb", "0", getTreeid(), riq);
            }
            sc.setEndpointAddress("http://localhost/zdt/InterCom_dt.jws");
            sc.request("shouhcrbyb");//执行接口任务
            sql = "update shouhcrbyb set zhuangt=1 where riq=date'" + riq+"'";
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

    public void getSelectData(ResultSetList rsl) {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        try {
            // ----------电厂树--------------
            String str = "";

            if (visit.isJTUser()) {
                str = "";
            } else {
                if (visit.isGSUser()) {
                    str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
                            + getTreeid() + ")";
                } else {
                    str = "and dc.id = " + getTreeid() + "";
                }
            }
            int treejib = this.getDiancTreeJib();
            if (treejib == 1) {// 选集团时刷新出所有的电厂
                str = "";
            } else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
                str = "and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + " or dc.shangjgsid= " + getTreeid()
                        + ")";

            } else if (treejib == 3) {// 选电厂只刷新出该电厂
                str = "and dc.id = " + getTreeid() + "";

            }

            String CurDate = DateUtil.FormatOracleDate(getRiq());
            long diancxxb_id = visit.getDiancxxb_id();
            if (visit.isFencb() && getChangbValue() != null) {
                diancxxb_id = getChangbValue().getId();
            }
            StringBuffer sb = new StringBuffer();
            if (rsl == null) {
                sb.append("select decode(ts,0,yb.id,0) as id,").append(CurDate).append(" as riq,decode(ts,0,dc.mingc,dc.mingc) as diancxxb_id, \n")
                        .append("decode(ts,0,p.mingc,p.mingc) as pinzb_id,decode(ts,0,yb.shourl,0) as shourl, \n")
                        .append("decode(ts,0,yb.fady,0) as fady,decode(ts,0,yb.gongry,0) as gongry, \n")
                        .append("decode(ts,0,yb.qity,0) as qity,decode(ts,0,yb.cuns,0) as cuns,decode(ts,0,yb.panyk,0) as panyk, \n")
                        .append("nvl(yb.kuc, 0) as kuc from  \n")
                        .append("(select id,fuid,shangjgsid,mingc,getYoushcZrDate(id,").append(CurDate).append(") as rq , \n")
                        .append("getYoushcZrDate(id,").append(CurDate).append(")-").append(CurDate).append(" as ts \n")
                        .append("from diancxxb where jib=3) dc,shouhcrbyb yb,pinzb p  \n")
                        .append("where dc.rq=yb.riq and p.id=yb.pinzb_id \n")
                        .append("and dc.id=yb.diancxxb_id \n")
                        // .append("and riq="+ CurDate +" \n")
                        .append(str);
//		System.out.println(sb);
                rsl = con.getResultSetList(sb.toString());

            }
            if (rsl == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
                setMsg(ErrorMessage.NullResult);
                return;
            }
            ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
            egu.setTableName("shouhcrbyb");
            egu.setWidth("bodyWidth");
            egu.addPaging(25);
            egu.setGridType(ExtGridUtil.Gridstyle_Edit);
            egu.getColumn("id").setHidden(true);
            egu.getColumn("id").setEditor(null);
//		 *****************************************设置默认值****************************
            // 电厂下拉框
            int treejib2 = this.getDiancTreeJib();

            if (treejib2 == 1) {// 选集团时刷新出所有的电厂
                egu.getColumn("diancxxb_id").setEditor(new ComboBox());
                egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(
                        "select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
                egu.getColumn("diancxxb_id").setReturnId(true);
            } else if (treejib2 == 2) {// 选分公司的时候刷新出分公司下所有的电厂
                egu.getColumn("diancxxb_id").setEditor(new ComboBox());
                egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(
                        "select id,mingc from diancxxb where fuid=" + getTreeid() + " or shangjgsid=" + getTreeid() + "order by mingc"));
                egu.getColumn("diancxxb_id").setReturnId(true);
            } else if (treejib2 == 3) {// 选电厂只刷新出该电厂
                egu.getColumn("diancxxb_id").setEditor(new ComboBox());
                egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(
                        "select id,mingc from diancxxb where id=" + getTreeid() + " order by mingc"));

                ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id=" + getTreeid() + " order by mingc");
                String mingc = "";
                if (r.next()) {
                    mingc = r.getString("mingc");
                }
                egu.getColumn("diancxxb_id").setDefaultValue(mingc);

            }

//		对数据进行小数修约
            String xyzhi = "";
            String xysql = " select zhi from xitxxb where mingc='油收耗存数据保留位数' and   diancxxb_id=" + visit.getDiancxxb_id();
            ResultSetList xyrsl = con.getResultSetList(xysql);
            while (xyrsl.next()) {
                xyzhi = xyrsl.getString("zhi");
            }
            xyrsl.close();

            int zhi = 0;
            if (xyzhi.equals("")) {
                zhi = 2;
            } else {
                zhi = Integer.parseInt(xyzhi);
            }


            egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
            egu.getColumn("diancxxb_id").setWidth(100);
            egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
            egu.getColumn("pinzb_id").setWidth(60);
            egu.getColumn("pinzb_id").setDefaultValue("柴油");
            egu.getColumn("riq").setHeader("日期");
            egu.getColumn("riq").setHidden(true);
            egu.getColumn("riq").setDefaultValue(getRiq());
            egu.getColumn("shourl").setHeader(Locale.shourl);
            egu.getColumn("shourl").setWidth(60);
            egu.getColumn("shourl").setDefaultValue("0");
            ((NumberField) egu.getColumn("shourl").editor).setDecimalPrecision(zhi);
            egu.getColumn("fady").setHeader(Locale.fady);
            egu.getColumn("fady").setWidth(60);
            egu.getColumn("fady").setDefaultValue("0");
            ((NumberField) egu.getColumn("fady").editor).setDecimalPrecision(zhi);
            egu.getColumn("gongry").setHeader(Locale.gongry);
            egu.getColumn("gongry").setWidth(60);
            egu.getColumn("gongry").setDefaultValue("0");
            ((NumberField) egu.getColumn("gongry").editor).setDecimalPrecision(zhi);
            egu.getColumn("qity").setHeader(Locale.qity);
            egu.getColumn("qity").setWidth(60);
            egu.getColumn("qity").setDefaultValue("0");
            ((NumberField) egu.getColumn("qity").editor).setDecimalPrecision(zhi);
            egu.getColumn("cuns").setHeader(Locale.cuns);
            egu.getColumn("cuns").setWidth(60);
            egu.getColumn("cuns").setDefaultValue("0");
            ((NumberField) egu.getColumn("cuns").editor).setDecimalPrecision(zhi);
            egu.getColumn("panyk").setHeader(Locale.panyk);
            egu.getColumn("panyk").setWidth(60);
            egu.getColumn("panyk").setDefaultValue("0");
            ((NumberField) egu.getColumn("panyk").editor).setDecimalPrecision(zhi);
            egu.getColumn("kuc").setHeader(Locale.kuc);
            egu.getColumn("kuc").setWidth(60);
            egu.getColumn("kuc").setDefaultValue("0");
            ((NumberField) egu.getColumn("kuc").editor).setDecimalPrecision(zhi);
            egu.getColumn("kuc").setEditor(null);

            ComboBox c4 = new ComboBox();
            egu.getColumn("pinzb_id").setEditor(c4);
            c4.setEditable(true);
            String pinzSql = "select id,mingc from pinzb where leib='油'";
            egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));

            egu.addTbarText("日期:");
            DateField df = new DateField();
            df.Binding("RIQ", "");
            df.setValue(getRiq());
            egu.addToolbarItem(df.getScript());
            egu.addTbarText("-");//设置分隔符
            //设置树

            egu.addTbarText("单位:");
            ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                    ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
                    .getVisit()).getDiancxxb_id(), getTreeid());
            setTree(etu);
            egu.addTbarTreeBtn("diancTree");
            egu.addTbarText("-");// 设置分隔符
		/*if(visit.isFencb()) {
			egu.addTbarText("厂别:");
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			egu.addToolbarItem(changbcb.getScript());
		}*/
//		刷新按钮
            StringBuffer rsb = new StringBuffer();
            rsb.append("function (){")
                    .append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('RIQ').value+'的数据,请稍候！'", true))
                    .append("document.getElementById('RefreshButton').click();}");
            GridButton gbr = new GridButton("刷新", rsb.toString());
            gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
            egu.addTbarBtn(gbr);
            String sql = "select zhuangt from shouhcrbyb where riq=date'" + this.getRiq() + "'";
            ResultSet rs = con.getResultSet(sql);
            boolean z = false;
            if (rs.next()) {
                z = rs.getInt("zhuangt") != 0;
            }
//		添加按钮
            egu.addToolbarButton(GridButton.ButtonType_Insert, null,z);
//		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
//		gbc.setIcon(SysConstant.Btn_Icon_Create);
//		egu.addTbarBtn(gbc);
//		删除按钮
            egu.addToolbarButton(GridButton.ButtonType_Delete, null,z);

//		保存按钮,SaveAll保存后及时没有更改也能存进数据库。
            GridButton gbs = new GridButton(GridButton.ButtonType_SaveAll, "gridDiv", egu.getGridColumns(), "SaveButton");
            gbs.setDisabled(z);
            egu.addTbarBtn(gbs);
            GridButton tj = new GridButton("提交", getBtnHandlerScript("SubmitButton"));
            tj.setIcon(SysConstant.Btn_Icon_SelSubmit);
            tj.setDisabled(z);
            egu.addTbarBtn(tj);
//		grid 计算方法
            egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);");

            setExtGrid(egu);
        }catch (Exception e){
            e.printStackTrace();
            con.rollBack();
        }finally {
            con.Close();
        }
    }

    public String getBtnHandlerScript(String btnName) {
//		按钮的script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = "'+Ext.getDom('RIQ').value+'";
        btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("新生成数据将覆盖")
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
            this.setTreeid(null);
            setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));

        }
        getSelectData(null);
    }

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
}
