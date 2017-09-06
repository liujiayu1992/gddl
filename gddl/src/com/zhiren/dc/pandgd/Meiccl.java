package com.zhiren.dc.pandgd;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meiccl extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	//设置盘点器皿容积的值
	private double zhi = -1;
	public double getZhi() {
		if(this.zhi == -1){
			setZhi();
		}
			return zhi;
	}
	public void setZhi() {
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select zhi from xitxxb where mingc = '盘点器皿容积' and diancxxb_id ="+v.getDiancxxb_id());
		if(rsl.next()){
			this.zhi=rsl.getDouble("zhi");
		}else{
			setMsg("请先设置盘点器皿容积的值");
		}
		
		rsl.close();
		con.Close();
	}
	
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	//盘点编号下拉框
	/*public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pand_gd p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ "order by p.id desc";
		    v.setProSelectionModel10(new IDropDownModel(sql,"请选择"));
		}
	    return v.getProSelectionModel10();
	}
	public void setPandValue(IDropDownBean value) {
		((Visit)getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getPandModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		String pandbm = "";
		//判断页面是否有盘点码，如果没有的话，从数据库中读取
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pand_gd where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + " and zhuangt=0 order by id desc";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				pandbm = rsl.getString("bianm");
			}
			return pandbm;
		}
		return getPandValue().getValue();
	}
	public long getPandbID() {
		if (getPandValue() == null) {
			return -1;
		}
		return getPandValue().getId();
	}*/
	
	
	//设置测量方法下拉框
//	private IPropertySelectionModel _celModel;
//	public void setCelModel(IPropertySelectionModel  value){
//		
//		_celModel = value;
//	}
//	public IPropertySelectionModel getCelModel(){
//		if(_celModel == null){
//			List list = new ArrayList();
//		
//			list.add(new IDropDownBean(1,"模拟"));
//			list.add(new IDropDownBean(2,"沉桶"));
//			_celModel = new IDropDownModel(list);
//		}
//		return _celModel;
//	}
//	
//	public void setCelValue(IDropDownBean value) {
//		Visit visit = (Visit) getPage().getVisit();
//		visit.setDropDownBean6(value);
//		
//	}
//	public IDropDownBean getCelValue() {
//		Visit visit = (Visit) getPage().getVisit();
//		if(visit.getDropDownBean6()== null){
//			setCelValue((IDropDownBean)getCelModel().getOption(0));
//		}
//		return visit.getDropDownBean6();
//	}
	//刷新按钮
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
//	private boolean _AddChick = false;
//	public void AddButton(IRequestCycle cycle) {
//		_AddChick = true;
//	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} 
//		else if (_AddChick) {
//			getSelectData();
//		}
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		//String celffid = getCelValue().getValue();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "meicclb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//进行删除操作是添加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meicmd,
					"meicclb",String.valueOf(id));
			sSql = "delete from meicclb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "meicclb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
			while (rsl.next()) {
				id = rsl.getLong("id");
				if (id == 0) {
					sSql = "insert into meicclb(id, diancxxb_id, jizb_id, item_id) values(getNewId(" + this.getTreeid() + "),"
						+ getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id")) + ","
						+ getExtGrid().getColumn("jizbh").combo.getBeanId(rsl.getString("jizbh")) + ","
//						+ getItembID(con,rsl.getString("mingc"))		
						+ getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc"))
						+ " )";
					flag = con.getInsert(sSql);
					if (flag == -1) {
						setMsg("保存失败!");
						con.rollBack();
						con.Close();
						return;
					}
				} else {
				//进行修改操作时添加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meicmd,
						"meicdjmdcdb",id+"");
					sSql = "update meicclb set " +
							"diancxxb_id="+getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"))	+","				
						+ " jizb_id=" + getExtGrid().getColumn("jizbh").combo.getBeanId(rsl.getString("jizbh")) + ","
						+ " item_id=" + getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc"))
						+ "  where id=" + id;
					flag = con.getUpdate(sSql);
					if (flag == -1) {
						setMsg("保存失败");
						con.rollBack();
						con.Close();
						return;
						}
					}
				}
			
		}
	public String getMeicbID(JDBCcon con, String meicMC) {
		String yougbID = "";
		String sql = "select id from jizb where jizbh='" + meicMC + "' and jizb.diancxxb_id=" + getTreeid();
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			yougbID = rs.getString("id");
		}
		return yougbID;
	}
	public String getItembID(JDBCcon con,String itemMC){
		String yongbID="";
		String sql="select id from item where mingc='"+itemMC+"'";
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			yongbID=rs.getString("id");
		}
		return yongbID;
	}
	public void getSelectData() {
		String sSql = "";
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
	//	String celffid = getCelValue().getValue();//得到页面选择的测量方法，如果是模拟法就执行IF中的语句，如果是沉桶法就执行ELSE中的语句
		boolean cel = false;
		String jizsql = "select id ,jizbh from jizb where diancxxb_id="+getTreeid();
		String diancId=this.getTreeid();
//		修正查询时的密度
		String where ="";
			sSql = 
				"select p.id,d.mingc as diancxxb_id, b.jizbh, m.mingc\n" +
				"  from jizb b, item m, meicclb p,vwdianc d\n" + 
				" where p.jizb_id = b.id\n" + 
				"   and p.item_id = m.id\n" +
				" and p.diancxxb_id=d.id\n"+
				"and p.diancxxb_id in (select id from vwdianc where id="+diancId+" or fuid="+diancId+") \n"+
				"order by p.id";
			
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = getEgu(cel,rsl);
		//设置电厂下拉框
		int treejib = this.getDiancTreeJib();
		IDropDownModel dc;
		if (treejib == 1) {
			dc =  new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc");
		} else if (treejib == 2) {
			dc =  new IDropDownModel("select id,mingc from diancxxb where fuid=" + getTreeid() + " order by mingc");
			jizsql= "select id ,jizbh from jizb where diancxxb_id in (select id from vwdianc where fuid="+getTreeid()+")";
		} else {
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			if(visit.isFencb()){
				dc = new IDropDownModel("select id,mingc from diancxxb where id = "+getTreeid()+" or fuid="+getTreeid()+" order by mingc");
				jizsql= "select id ,jizbh from jizb where diancxxb_id in (select id from vwdianc where id="+getTreeid()+" or fuid="+getTreeid()+")";
			}else{
				dc = new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");
				jizsql= "select id ,jizbh from jizb where diancxxb_id ="+getTreeid();
			}
		}
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dc);
		egu.getColumn("diancxxb_id").setReturnId(true);
		egu.getColumn("diancxxb_id").setDefaultValue(dc.getBeanValue(getTreeid()));
//		 设置煤场下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("jizbh").setEditor(c1);
		c1.setEditable(true);
		egu.getColumn("jizbh").setComboEditor(egu.gridId,new IDropDownModel(jizsql));
//		设置机组下拉框
		ComboBox c2=new ComboBox();
		egu.getColumn("mingc").setEditor(c2);
		c2.setEditable(true);
		String meic="select id,bianm || mingc from item where itemsortid=(select itemsortid from itemsort where bianm='MEIC') order by xuh";
		egu.getColumn("mingc").setComboEditor(egu.gridId, new IDropDownModel(meic));
//		设置工具栏盘点编码下拉框
//		egu.addTbarText("盘点编码：");
//		ComboBox cobPand = new ComboBox();
//		cobPand.setWidth(100);
//		cobPand.setTransform("PandDropDown");
//		cobPand.setId("PandDropDown");
//		cobPand.setLazyRender(true);
//		egu.addToolbarItem(cobPand.getScript());
//		egu.addTbarText("-");
//		设置测量方法下拉框
//		egu.addTbarText("测量方法");
//		ComboBox cobCel = new ComboBox();
//		cobCel.setWidth(100);
//		cobCel.setTransform("CelDropDown");
//		cobCel.setId("CelDropDown");
//		cobCel.setLazyRender(true);
//		egu.addToolbarItem(cobCel.getScript());
//		egu.addTbarText("-");
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);


		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		/*String moni ="gridDiv_grid.on('afteredit',function(e){\n" +
		"var record = gridDiv_ds.getAt(e.row);\n" + 
		"var jingz = eval(record.get('JINGZ')||0);\n" + 
		"var rongj = eval(record.get('RONGJ')||0);\n" + 
		"if(e.field=='JINGZ'||e.field=='RONGJ'){\n" + 
		//"var cedjj = eval(record.get('CEDJJ')||0);\n" + 
		//"var zhi = record.get('ZHI');\n" + 
		"var mid = Round_new((jingz/rongj)*100,2);\n" + 
		"mid=mid.toFixed(5);\n" + 
		"record.set('MID',mid);\n" + 
		"}\n" + 
		"});";
	
	   egu.addOtherScript(moni);*/

		/*String scriptc = "\n  var celIndex = CelDropDown.getValue();\n"+
					"CelDropDown.on('select',function(o,record,index){ \n" +
					"if(celIndex!=CelDropDown.getValue()){document.forms[0].submit();}}); \n" ;
		egu.addOtherScript(scriptc);*/
		/*String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);*/
		setExtGrid(egu);
		con.Close();
	}
	public ExtGridUtil getEgu(boolean x,ResultSetList rsl){
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);

			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.setWidth(Locale.Grid_DefaultWidth);
			egu.setHeight("bodyHeight");
			egu.getColumn("id").setHidden(true);
			egu.getColumn("diancxxb_id").setHeader("电厂");
			egu.getColumn("jizbh").setHeader("机组");
			egu.getColumn("mingc").setHeader("煤仓");
		
		return egu;
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		//在进入页面之前先判断，如果在系统信息表中没有该厂的盘点器皿容积参数，提示用户设置
//		JDBCcon con = new JDBCcon();
//		ResultSetList rsl = con.getResultSetList("select * from xitxxb where mingc ='盘点器皿容积' and diancxxb_id ="+visit.getDiancxxb_id());
//		if(!rsl.next()){
//			setMsg("请先输入盘点器皿容积参数");
//			rsl.close();
//			con.Close();
//			//return;
//		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			/*setPandModel(null);
			setPandValue(null);*/
		//	setCelModel(null);
		//	setCelValue(null);
			setZhi();
			setTreeid("");
		}
		init();
//		rsl.close();
//		con.Close();
	}
	private void init() {
		getSelectData();
//		_AddChick = false;
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

}
