package com.zhiren.dc.jilgl.jicxx;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2013-03-04
 * 适用范围：国电电力及其下属单位
 * 描述：使用新函数getNewYSDWId获取运输单位的ID
 */
public class Yunsdw extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
	}
	
	/**
	 * 保存数据时分别判断名称和全称是否与数据库中的数据重复，
     * 重复不允许保存，允许保存时将全称保存到bianm字段中，将全称作为运输单位的编码。
	 * @author yinjm
	 */
	public void save() {
		
		JDBCcon con = new JDBCcon();
		String message = "";	// 用于保存重复的数据提示信息
		StringBuffer sqlsb = new StringBuffer("begin\n"); 
		ResultSetList mdrsl = ((Visit)this.getPage().getVisit()).getExtGrid1().getModifyResultSet(getChange());
		ResultSetList rsl = new ResultSetList();
		
		while(mdrsl.next()) {
//			在数据库中查询是否有与要保存的名称、全称相同的记录。
			String sql = "select y.mingc, y.quanc from yunsdwb y where (y.mingc = '"+ mdrsl.getString("mingc") +"' or y.quanc = '"
				+ mdrsl.getString("quanc") +"') and y.id <> " + mdrsl.getString("id") + " and y.diancxxb_id = " + getTreeid();
			rsl = con.getResultSetList(sql);
			
			if (rsl.getRows() > 0) {
				while(rsl.next()) {
					if (rsl.getString("mingc").equals(mdrsl.getString("mingc"))) {
						message += "运输单位名称 \""+ mdrsl.getString("mingc") +"\" 已经存在，请重新输入！<br>";
					}  
					if (rsl.getString("quanc").equals(mdrsl.getString("quanc"))) {
						message += "运输单位全称 \""+ mdrsl.getString("quanc") +"\" 已经存在，请重新输入！<br>";
					}
				}
				continue;
			}
			
			if (mdrsl.getString("id").equals("0")) {
				
				sqlsb.append("insert into yunsdwb(id, diancxxb_id, mingc, beiz, quanc, danwdz, youzbm, shuih, faddbr, " +
					"weitdlr, kaihyh, zhangh, dianh, chuanz, bianm) values(getNewYSDWId("+ getTreeid() +"), ").append(getTreeid())
					.append(", '").append(mdrsl.getString("mingc")).append("', '").append(mdrsl.getString("beiz")).append("', '")
					.append(mdrsl.getString("quanc")).append("', '").append(mdrsl.getString("danwdz")).append("', '")
					.append(mdrsl.getString("youzbm")).append("', '").append(mdrsl.getString("shuih")).append("', '")
					.append(mdrsl.getString("faddbr")).append("', '").append(mdrsl.getString("weitdlr")).append("', '")
					.append(mdrsl.getString("kaihyh")).append("', '").append(mdrsl.getString("zhangh")).append("', '")
					.append(mdrsl.getString("dianh")).append("', '").append(mdrsl.getString("chuanz")).append("', '")
					.append(mdrsl.getString("quanc")).append("'); \n");
			} else {
				sqlsb.append("update yunsdwb set mingc = '").append(mdrsl.getString("mingc"))
					.append("', beiz = '").append(mdrsl.getString("beiz")).append("', quanc = '").append(mdrsl.getString("quanc"))
					.append("', danwdz = '").append(mdrsl.getString("danwdz")).append("', youzbm = '").append(mdrsl.getString("youzbm"))
					.append("', shuih = '").append(mdrsl.getString("shuih")).append("', faddbr = '").append(mdrsl.getString("faddbr"))
					.append("', weitdlr = '").append(mdrsl.getString("weitdlr")).append("', kaihyh = '").append(mdrsl.getString("kaihyh"))
					.append("', zhangh = '").append(mdrsl.getString("zhangh")).append("', dianh = '").append(mdrsl.getString("dianh"))
					.append("', chuanz = '").append(mdrsl.getString("chuanz")).append("', bianm = '").append(mdrsl.getString("quanc"))
					.append("' where id = ").append(mdrsl.getString("id")).append("; \n");
			}
		}
		sqlsb.append("end;");
		if (!message.equals("")) {
			setMsg(message);
		}
		if (!sqlsb.toString().equals("begin\nend;")) {
			con.getUpdate(sqlsb.toString());
		}
		rsl.close();
		mdrsl.close();
		con.Close();
	}

	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql="select id,diancxxb_id,bianm,mingc,quanc,danwdz,youzbm,shuih,faddbr,weitdlr,kaihyh,zhangh,dianh,chuanz,beiz \n"
			+ " from yunsdwb where diancxxb_id = " +getTreeid()+ "\n"
			+ " order by mingc \n";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设置是否可以编辑
		egu.setTableName("yunsdwb");
//		if(visit.isFencb()) {
//			ComboBox dc= new ComboBox();
//			egu.getColumn("diancxxb_id").setEditor(dc);
//			dc.setEditable(true);
//			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
//			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
//			egu.getColumn("diancxxb_id").returnId=true;
//			egu.getColumn("diancxxb_id").setWidth(70);
//		}else {
//			egu.getColumn("diancxxb_id").setHidden(true);
//			egu.getColumn("diancxxb_id").editor = null;
//			egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
//		}
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(""+getTreeid());
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setHidden(true);
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("danwdz").setHeader("单位地址");
		egu.getColumn("youzbm").setHeader("邮政编码");
		egu.getColumn("shuih").setHeader("税号");
		egu.getColumn("faddbr").setHeader("法定代表人");
		egu.getColumn("weitdlr").setHeader("委托代理人");
		egu.getColumn("kaihyh").setHeader("开户银行");
		egu.getColumn("zhangh").setHeader("账号");
		egu.getColumn("dianh").setHeader("电话");
		egu.getColumn("chuanz").setHeader("传真");
		egu.getColumn("beiz").setHeader("备注");
		
		//设置Grid行数
		egu.addPaging(25);
		
		egu.getColumn("bianm").setWidth(100);
		egu.getColumn("quanc").setWidth(130);
		egu.getColumn("beiz").setWidth(150);
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
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		String str = 
			"var url = 'http://'+document.location.host+document.location.pathname;\n" +
			"var end = url.indexOf(';');\n" + 
			"url = url.substring(0,end);\n" + 
			"url = url + '?service=page/YunsdwReport&lx="+ getTreeid() +"';\n" + 
			"window.open(url,'newWin');";
		egu.addToolbarItem("{" + new GridButton("打印", "function (){" + str + "}", SysConstant.Btn_Icon_Print).getScript() + "}");
		
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
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
		if (treeid==null||treeid.equals("")) {

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
			setTreeid(null);
		}
		init();
	}
	private void init() {
		getSelectData();
	}
}
