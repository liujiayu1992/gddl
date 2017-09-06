package com.zhiren.dc.rulgl.meihyb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.common.ext.form.SelectCombo;
import com.zhiren.dc.diaoygl.AutoCreateShouhcrb;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterFac_dt;


public class Meihypp extends BasePage implements PageValidateListener {
//	客户端的消息框
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
//	页面初始化(每次刷新都执行)
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
	//描述
	public String _miaos;

	public String getMiaos() {
		return _miaos;
	}

	public void setMiaos(String miaos) {
		_miaos = miaos;
	}
//	 日期控件
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {

			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {
		

		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
	
	

		// 保存数据
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult+ "Yundlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if (rsl.getRows() == 0) {
			return;
		}
		
		sb.append("begin\n");
		
		while (rsl.next()) {
			
			if(rsl.getString("shenhzt").equals("3")){
				setMsg("入炉化验已审核,请先取消审核,再保存！");
				return;
			}
			
			if (!"".equals(rsl.getString("huaybh"))) {
				 long rulmzlbtmp_id=(getExtGrid().getColumn("huaybh").combo).getBeanId(rsl.getString("huaybh"));
				sb.append("update  rulmzlb  set qnet_ar=").append("(select max(qnet_ar) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("aar=").append("(select max(aar) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("ad=").append("(select max(ad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("vdaf=").append("(select max(vdaf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("mt=").append("(select max(mt) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("stad=").append("(select max(stad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("aad=").append("(select max(aad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("mad=").append("(select max(mad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qbad=").append("(select max(qbad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("had=").append("(select max(had) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("vad=").append("(select max(vad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("fcad=").append("(select max(fcad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("std=").append("(select max(std) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qgrad=").append("(select max(qgrad) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("hdaf=").append("(select max(hdaf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("sdaf=").append("(select max(sdaf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("var=").append("(select max(var) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("huayy=").append("(select max(huayy) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("lury=").append("'"+visit.getRenymc()+"'").append(",\n");
				sb.append("shenhzt=").append("1").append(",\n");
				sb.append("bianm=").append("'"+rsl.getString("huaybh")+"'").append(",\n");
				sb.append("har=").append("(select max(har) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qgrd=").append("(select max(qgrd) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append(",\n");
				sb.append("qgrad_daf=").append("(select max(qgrad_daf) from rulmzlbtmp where id="+rulmzlbtmp_id+")").append("\n");
				sb.append("where id=").append(""+rsl.getString("rulmzlb_id")).append(";\n");
				sb.append("update rulmzlbtmp set ispip=1 where id="+rulmzlbtmp_id+";\n");
				sb.append("update meihyb set rulmzlbtmp_id="+rulmzlbtmp_id+" where id="+rsl.getString("id")+";\n");
					
				
			} 
		}
		sb.append("end;");

		if(con.getInsert(sb.toString())>=0){
			
			setMsg("保存成功");
		}else{
		setMsg("保存失败");
	    }
		
		con.Close();

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		
	
	
		getSelectData();
	}
	

	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rulrq = DateUtil.FormatOracleDate(this.getRiqi());	//入炉日期 
		
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			if(visit.isFencb()){
				diancxxb_id = " and (m.diancxxb_id="+this.getTreeid()+" or fuid="+this.getTreeid()+") ";
			}else{
				diancxxb_id = "and (d.id = " + this.getTreeid() + ")\n";
			}
		}
		
		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,decode(t.ispip,1,'已匹配','未匹配') as ispip,"
			    + "  m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,r.shenhzt,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,t.huaybh\n"
				+ "  from meihyb m, diancxxb d, rulmzlb r, rulbzb rb, jizfzb j ,rulmzlbtmp t\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulmzlb_id = r.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+"    and m.rulmzlbtmp_id=t.id(+)"
				+ "   and m.rulrq = "+ rulrq + "\n"
				+""+diancxxb_id+"\n"
		        +"  order by m.rulrq,rb.xuh,j.xuh";
		//}
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("耗用日期");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("ispip").setHeader("是否匹配");
		egu.getColumn("ispip").setEditor(null);
		
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("入炉班组");
		egu.getColumn("jizfzb_id").setHeader("入炉机组");
		egu.getColumn("fadhy").setHeader("发电耗用(t)");
		egu.getColumn("fadhy").setEditor(null);
		egu.getColumn("gongrhy").setHeader("供热耗用(t)");
		egu.getColumn("gongrhy").setEditor(null);
		egu.getColumn("qity").setHeader("其它用(t)");
		egu.getColumn("qity").setEditor(null);
		egu.getColumn("feiscy").setHeader("非生产用(t)");
		egu.getColumn("feiscy").setEditor(null);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setWidth(180);
		egu.getColumn("shenhzt").setHeader("入炉化验审核状态");
		egu.getColumn("shenhzt").setWidth(50);
		egu.getColumn("shenhzt").setEditor(null);
		egu.getColumn("shenhzt").setHidden(true);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		// *****************************************设置默认值****************************
		

		// 设置下拉框入炉班组
		
			egu.getColumn("rulbzb_id").setEditor(null);
			egu.getColumn("jizfzb_id").setEditor(null);
		

		
		ComboBox huaybh = new ComboBox();
		egu.getColumn("huaybh").setEditor(huaybh);
		huaybh.setEditable(true);
		String huaybhSql = "select id, huaybh from rulmzlbtmp  where rulrq>=date'"+this.getRiqi()+"'-3 and  rulrq<=date'"+this.getRiqi()+"'+3  order by huaybh";
		egu.getColumn("huaybh").setComboEditor(egu.gridId,new IDropDownModel(huaybhSql));
		egu.getColumn("huaybh").setReturnId(true);
		

		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// ************************************************************
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		
		//保存按钮
	
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		
		
		

		
	
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
			visit.setString3("");
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			setJizfzbModel(null);
			setJizfzbModels();
			setRulbzbModel(null);
			setRulbzbModels();
			setRiqi(null);
			setMsg("");
		}

		getSelectData();
	}

	


	public IPropertySelectionModel getRulbzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setRulbzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setRulbzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setRulbzbModels() {
		String sql = "select r.id,r.mingc from rulbzb r";
		setRulbzbModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getJizfzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setJizfzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setJizfzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setJizfzbModels() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")";
		setJizfzbModel(new IDropDownModel(sql));
	}
	
//	 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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