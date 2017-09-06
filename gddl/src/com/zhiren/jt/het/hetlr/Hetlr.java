package com.zhiren.jt.het.hetlr;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Hetlr extends BasePage implements PageValidateListener {
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO Auto-generated method stub
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
	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(),visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		JDBCcon con = new JDBCcon();
		try{
		String tableName="hetxxb_dtgj";
		boolean isDel =false;
		
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			isDel=true;
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);

		
		
		while(mdrsl.next()) {
			StringBuffer bsql=new StringBuffer();
			bsql.append("select * from  hetxxb_dtgj ");
			
			StringBuffer sql2 = new StringBuffer();
			
			if("0".equals(mdrsl.getString("ID"))) {
				if(getChangeHetbh(mdrsl.getString("HETBH"),bsql)){
					
					this.setMsg("编号"+mdrsl.getString("HETBH")+"已存在!");
					return;
				}
				
            	sql.append("insert into ").append(tableName).append("(id");
            	sql2.append("getnewid("+visit.getDiancxxb_id()+")");
				
				for(int i=1;i<mdrsl.getColumnCount();i++) {
			
					sql.append(",").append(mdrsl.getColumnNames()[i]);					
					sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));}

				sql.append(") values(").append(sql2).append(");\n");
				
			}else {
//				if(isDel==false) {
//					 bsql=new StringBuffer();
//						bsql.append("select * from  hetxxb_dtgj ");
//					if(getChangeHetbh(mdrsl.getString("HETBH"),bsql)){
//						this.setMsg("编号"+mdrsl.getString("HETBH")+"已存在!");
//						return;
//					}
//				}
				sql.append("update ").append(tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");					
					sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");

				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
			
		}
		sql.append("end;");
//		System.out.println(sql.toString());
		con.getUpdate(sql.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		}
	}
	//页面编号是否已存在
	private boolean getChangeHetbh(String changehetbh,StringBuffer sql){
		boolean forchange=false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		
		sql.append(" where hetbh='"+changehetbh+"'");
		ps=con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();			
			while(rs.next()){
				if(rs!=null){
					forchange=true;			
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        
		return forchange;
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strRiq = getRiqi();
		/*   oracle入炉日期   */
		String sql = "select h.id,diancxxb_id,g.mingc as gongysb_id,qiandrq,gonghksrq,gonghjsrq,hetbh,hetsl,decode(h.SHIFYKJJ,1,'是',0,'否') SHIFYKJJ,hetjg,decode(shifhyf,0,'否',1,'是') as shifhyf,\n" +
				"yunf,rezbz,liubz,rezjfbz,liujfbz,decode(jilfs,0,'厂方',1,'矿方',2,'协商') as jilfs, \n" +
				"decode(zhilfs,0,'厂方',1,'矿方',2,'协商') zhilfs, y.mingc yunsfsb_id \n" +
				"from hetxxb_dtgj h,gongysb g, yunsfsb y \n" +
				"where h.gongysb_id=g.id and h.yunsfsb_id=y.id and h.diancxxb_id="+getTreeid()+"\n " +
				"and qiandrq>=to_date('"+getNianfValue()+"-01-01','yyyy-mm-dd') and qiandrq<=to_date('"+getNianfValue()+"-12-31','yyyy-mm-dd')";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("hetxxb_dtgj");
		egu.setWidth("bodyWidth");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		/*	设置列名	*/
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("qiandrq").setHeader("签订日期");
		egu.getColumn("gonghksrq").setHeader("供货开始日期");
		egu.getColumn("gonghjsrq").setHeader("供货结束日期");
		egu.getColumn("hetbh").setHeader("合同编号");
		egu.getColumn("hetsl").setHeader("煤量（万吨）");
	    egu.getColumn("SHIFYKJJ").setHeader("是否以卡计价");
		egu.getColumn("hetjg").setHeader("价格");
		egu.getColumn("shifhyf").setHeader("是否含运费");
		egu.getColumn("yunf").setHeader("运费");
		egu.getColumn("rezbz").setHeader("Qnet,ar标准(大卡)");
		egu.getColumn("liubz").setHeader("St,d标准");
		egu.getColumn("rezjfbz").setHeader("Qnet,ar奖罚");
		egu.getColumn("liujfbz").setHeader("St,d奖罚");
		egu.getColumn("jilfs").setHeader("计量方式");
		egu.getColumn("zhilfs").setHeader("质量方式");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		/*	设置列不可编辑	 */
		egu.getColumn("diancxxb_id").editor = null;
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("shifhyf").setEditor(null);
		egu.getColumn("jilfs").setEditor(null);
		/*  设置列默认值  */
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("qiandrq").setDefaultValue(strRiq);
		egu.getColumn("gonghksrq").setDefaultValue(strRiq);
		egu.getColumn("gonghjsrq").setDefaultValue(strRiq);
		/*  设置列显示  */
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);		
		/*  设置列宽  */
		egu.getColumn("id").setWidth(85);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("qiandrq").setWidth(85);
		egu.getColumn("gonghksrq").setWidth(85);
		egu.getColumn("diancxxb_id").setWidth(85);
		egu.getColumn("gonghjsrq").setWidth(100);
		egu.getColumn("hetbh").setWidth(60);
		egu.getColumn("hetsl").setWidth(60);
		egu.getColumn("hetjg").setWidth(60);
		egu.getColumn("shifhyf").setWidth(80);
		egu.getColumn("yunf").setWidth(60);
		egu.getColumn("rezbz").setWidth(80);
		egu.getColumn("liubz").setWidth(60);
		egu.getColumn("rezjfbz").setWidth(300);
		egu.getColumn("liujfbz").setWidth(120);
		egu.getColumn("jilfs").setWidth(80);
		egu.getColumn("zhilfs").setWidth(80);
		egu.getColumn("yunsfsb_id").setWidth(80);
		/*  设置编辑下拉框  */
		//设置供应商
		ComboBox hetxxb_gys = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(hetxxb_gys);
		hetxxb_gys.setEditable(true);
		String hetxxb_gysSql = "select g.id,g.mingc from diancxxb d,diancgysglb gl,gongysb g  where d.id=gl.diancxxb_id and gl.gongysb_id=g.id and d.id="+getTreeid()+" group by (g.id,g.mingc)  order by g.mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(hetxxb_gysSql));
		//设置是否含运费
		List l1 = new ArrayList();
		l1.add(new IDropDownBean(0, "否"));
		l1.add(new IDropDownBean(1, "是"));
		ComboBox c1 = new ComboBox();
		egu.getColumn("shifhyf").setEditor(c1);
		c1.setEditable(true);
		egu.getColumn("shifhyf").setComboEditor(egu.gridId,
				new IDropDownModel(l1));
		egu.getColumn("shifhyf").setDefaultValue("否");	
		//设置计量方式
		List l2 = new ArrayList();
		l2.add(new IDropDownBean(0, "厂方"));
		l2.add(new IDropDownBean(1, "矿方"));
		l2.add(new IDropDownBean(1, "协商"));
		ComboBox c2 = new ComboBox();
		egu.getColumn("jilfs").setEditor(c2);
		c2.setEditable(true);
		egu.getColumn("jilfs").setComboEditor(egu.gridId,
				new IDropDownModel(l2));
		egu.getColumn("jilfs").setDefaultValue("协商");
//		是否按卡计算
	        List	shifakjs=new ArrayList();
	        shifakjs.add(new IDropDownBean(1,"是"));
	        shifakjs.add(new IDropDownBean(0,"否"));
	        ComboBox shifakjsCombox=new ComboBox();
	        egu.getColumn("SHIFYKJJ").setEditor(shifakjsCombox);
	        egu.getColumn("SHIFYKJJ").setComboEditor(egu.gridId, new IDropDownModel(shifakjs));
	        egu.getColumn("SHIFYKJJ").setDefaultValue("否");
	        
//		质量方式
		ComboBox zhil = new ComboBox();
		egu.getColumn("zhilfs").setEditor(zhil);
		c2.setEditable(true);
		egu.getColumn("zhilfs").setComboEditor(egu.gridId,
				new IDropDownModel(l2));
		egu.getColumn("zhilfs").setDefaultValue("协商");
//		运输方式
		ComboBox yunsfs = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(yunsfs);
		yunsfs.setEditable(true);
		String yunsfsSql = "select id,mingc from yunsfsb order by id";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsfsSql));
		/*  设置工具栏  */
		egu.addTbarText("签订年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(100);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton","var Mr = gridDiv_grid.getStore().getRange();  \n" +
                "for(i = 0; i< Mr.length; i++){  \n"+
                "	for(j=i+1; j< Mr.length; j++){  \n"+
                "		if(Mr[i].get('HETBH')==Mr[j].get('HETBH')){  \n"+
                "       	Ext.MessageBox.alert('提示信息','合同编'+Mr[i].get('HETBH')+'号重复!');  \n"+
                "         	return false;"+			
                  "       }  \n"+        
                "   }  \n"+  
                "}" +
                "for(k=0;k<Mr.length;k++){if(Mr[k].get('SHIFYKJJ')=='否'&&Mr[k].get('HETJG')==''){Ext.MessageBox.alert('提示信息','第'+(k+1)+'行请填写合同价格');return false; }}");
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){rec=e.record;if(e.field=='HETBH'&&rec.get('ID')>0){e.cancel=true;}})");
		setExtGrid(egu);
		con.Close();
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
			visit.setActivePageName(getPageName().toString());
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			this.setNianfValue(null);
			this.getNianfModels();
		}

		getSelectData();

	}

	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	
	private IDropDownBean getIDropDownBean(IPropertySelectionModel model,
			long id) {
		int OprionCount;
		OprionCount = model.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) model.getOption(i)).getId() == id) {
				return (IDropDownBean) model.getOption(i);
			}
		}
		return null;
	}
//	年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
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
		for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
//		 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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


	boolean treechange = false;

	private String treeid;

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

