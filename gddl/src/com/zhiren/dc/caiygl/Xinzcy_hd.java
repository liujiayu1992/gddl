package com.zhiren.dc.caiygl;
/*
 * 作者：王磊
 * 时间：2009-09-10 11：29
 * 描述：如果为一厂多制选择总厂的时候列出所有分厂的采样信息。
 */
/*
 * 作者：王磊
 * 时间：2009-07-31 16：58
 * 描述：增加根据制样编码查询的功能，需增加参数设置
 * 		insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * 		values(getnewid(diancxxb_id),1,diancxxb_id,'新增样按制样编码查询','是','','采制化',1,'使用')
 */
/**
 * @author 刘雨
 * @version 2009-05-15
 */
/*
 * 作者：王磊
 * 时间：2009-05-26 11:31
 * 描述：修改在刷新数据的时候不关联质量表，取消重复做复查样的判断
 */
/*
 * 作者：王磊
 * 时间：2009-05-26 13:57
 * 描述：修改刷新数据时数据重复的问题，在生成重复样时将已二审的数据回退
 */
/*
 * 作者：夏峥
 * 时间：2013-06-06
 * 描述：复制Xinzcy并在原有基础上进行调整，不显示已完成审核的化验数据
 */
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xinzcy_hd extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
	}

	// 保存数据list
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	// 记录页面选中行的内容
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

	boolean riqichange1 = false;

	boolean riqichange2 = false;

	private String riqi1; // 页面起始日期日期选择

	private String riqi2; // 页面结束日期选择

	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}

	public void setRiqi1(String riqi1) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi1)) {
			this.riqi1 = riqi1;
			riqichange1 = true;
		}
	}

	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(new Date());
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi1 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
			riqichange2 = true;
		}
	}

	private boolean _ShedsqlChick = false;

	public void ShedsqlButton(IRequestCycle cycle) {
		_ShedsqlChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_ShedsqlChick) {
			_ShedsqlChick = false;
			Update(cycle);
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) this.getPage().getVisit();
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi1());
		String caiyrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql="";
		String dcsql = " and d.id = " + getTreeid();;
		if(v.isFencb()){
			if(v.getDiancxxb_id() == Long.parseLong(getTreeid())){
				dcsql = " and d.fuid = " + getTreeid();
			}
		}

		sql ="select c.id,\n" +
			"       c.zhilb_id as zhilb_id,\n" +
			"		f.diancxxb_id diancxxb_id,\n" +
			"       c.bianm as jincph,\n" + 
			"       getFahxx4zl(c.zhilb_id) as fahxx,\n" + 
			"       getCaiybh4zl(c.zhilb_id) as caiybh,\n" + 
			"       getHuaybh4zl(c.zhilb_id) as huaybh\n" + 
			"from caiyb c,(select distinct f.diancxxb_id,f.zhilb_id from fahb f,diancxxb d\n" + 
			"where  f.diancxxb_id = d.id and f.daohrq>="+caiyrqb +"\n" + 
			"      and f.daohrq<="+caiyrqe +"\n" +
			"	   " +dcsql+ 
			"	   minus select distinct f.diancxxb_id,f.zhilb_id from fahb f,diancxxb d, zhilb z" +
			"	   where f.zhilb_id=z.id and f.diancxxb_id = d.id and f.daohrq>="+caiyrqb +"\n" + 
			"      and f.daohrq<="+caiyrqe +"\n" +
			"	   " +dcsql+ 
					") f\n" +
			"where f.zhilb_id = c.zhilb_id order by c.caiyrq,c.bianm\n";
	
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caiyb");
		// /设置显示列名称
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("zhilb_id").setHeader("ZHILB_ID");
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("jincph").setHeader("进厂批号");
		egu.getColumn("fahxx").setHeader("发货信息");
		egu.getColumn("fahxx").setWidth(450);
		egu.getColumn("caiybh").setHeader("采样编号");
		egu.getColumn("caiybh").setWidth(100);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setWidth(100);
		// 工具
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi1());
		df.setReadOnly(true);
		df.Binding("RIQI1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riqi1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setValue(this.getRiqi2());
		df1.setReadOnly(true);
		df1.Binding("RIQI2", "forms[0]");
		df1.setId("riqi2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// 设置分隔符

		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// 只能选中单行
		
//		 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
//		 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		
		// 设置运输方式下拉框
		egu.addTbarText("新增样类别:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(80);
		comb1.setTransform("Leib");
		comb1.setListeners("select:function(own,rec,index){ Ext.getDom('Leib').selectedIndex=index;}");
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");

		GridButton gb = new GridButton(
				"生成",
				"function(){"
						+ "if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){"
						+ "Ext.MessageBox.alert('提示信息','请选中一条采样记录');"
						+ "return;}"
						+ "grid1_rcd = gridDiv_sm.getSelections()[0];"
						+ "grid1_history = grid1_rcd.get('ID')+'&'+grid1_rcd.get('DIANCXXB_ID')+'&'+grid1_rcd.get('ZHILB_ID');"
						+ "var Cobj = document.getElementById('CHANGE');"
						+ "Cobj.value = grid1_history;"
						+ "document.getElementById('ShedsqlButton').click();"
						+ "}");
		gb.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gb);
		setExtGrid(egu);
		con.Close();
	}

	//新增样类别
	public IDropDownBean getLeibValue() {
		IDropDownBean vi = ((Visit) this.getPage().getVisit()).getDropDownBean4();
		if (vi == null) {
			if (getLeibModel().getOptionCount() > 0) {
				setLeibValue((IDropDownBean) getLeibModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setLeibValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getLeibModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setLeibModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setLeibModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setLeibModels() {
		String sb;
		sb = " select l.id,l.mingc from leibb l where beiz = '新增'\n";
		setLeibModel(new IDropDownModel(sb));
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
		JDBCcon con = new JDBCcon();
		String caiyb_id = "";
		long diancxxb_id = 0;
		long zhilb_id = 0;
		String bumb_id = "";
		String zhuanmsz = "";
		String leibb_id = getLeibValue().getId()+"";
		String leib = getLeibValue().getValue();
		String xinx = getChange();
		String[] a = xinx.split("&");
		caiyb_id = a[0];
		diancxxb_id =  Long.parseLong(a[1]);
		zhilb_id =  Long.parseLong(a[2]);
		String cysj = "";
		String beiz = "";
		String sqlnew  = "select to_char(y.caiysj,'yyyy-mm-dd') caiysj,y.beiz from yangpdhb y ,zhillsb z where  y.zhilblsb_id = z.id and z.zhilb_id=" + zhilb_id;
		ResultSetList rsl_yp = con.getResultSetList(sqlnew);
		while(rsl_yp.next()){
			cysj = rsl_yp.getString("CAIYSJ");
			beiz = rsl_yp.getString("BEIZ");
		}
		String sql = "select * from leibb where id = " + leibb_id + "\n";
		ResultSetList rsl_zm = con.getResultSetList(sql);
		while(rsl_zm.next()){
			zhuanmsz = rsl_zm.getString("ZHUANMSZ");
		}
		rsl_zm.close();
		//得到部门
		String sSql = "select * from bumb order by zhuhybm desc,mingc";
		ResultSetList rsl = con.getResultSetList(sSql);
		if(rsl.next()){
			bumb_id = rsl.getString("ID");
		}
		rsl.close();
//		添加编码
		int flag = Caiycl.AddBianhC(con, zhilb_id, diancxxb_id,
				caiyb_id, leibb_id, bumb_id, leib,zhuanmsz,cysj,beiz);
	    if(flag>=0){
	    	
	    	setMsg("新增采样成功！");
	    }else{
	    	setMsg("生成过程出现错误！生成未成功！");
	    }
		con.Close();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setLong1(0);
			setLeibValue(null);
			setLeibModel(null);
		}
		getSelectData();
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
