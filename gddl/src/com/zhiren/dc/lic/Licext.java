package com.zhiren.dc.lic;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-4-21
 * 修改内容:增加电厂树，支持一厂多制
 */
/* 修改内容：1、添加save()方法，当修改电厂级别是3的电厂数据时，该电厂的下属电厂的数据也同时跟着更改。
 *    2、判断如果煤矿下拉框选择为空那么向数据库插入默认值。
 *    3、给保存按钮添加Js，校验值(ZHI)字段不能为空。
 * 修改时间：2009-09-14
 * 修改人：尹佳明
 */
/*
	 * 作者：赵胜男
	 * 时间：2013-03-29
	 * 适用范围：庄河电厂
	 * 描述：调整sql 适用于庄河港口取发站到站信息
	 */
public class Licext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
	
	private void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet((getChange()));
		while (delrsl.next()) {
			// 删除操作
			String sqlFencId = "select id from diancxxb where fuid = "+ delrsl.getString("diancxxb_id");
			ResultSetList rslData = con.getResultSetList(sqlFencId);
			
			if (rslData.next()) {
				String sql = 
					"select l.id\n" +
					"from licb l, (select * from licb where id = "+ delrsl.getString("id") +") f\n" + 
					"where l.faz_id = f.faz_id\n" + 
					"  and l.daoz_id = f.daoz_id\n" + 
					"  and l.liclxb_id = f.liclxb_id\n" + 
					"  and nvl(l.meikxxb_id, 0) = nvl(f.meikxxb_id, 0)";
				ResultSetList rslFencId = con.getResultSetList(sql);
				while (rslFencId.next()) {
					sbSql.append("delete from licb where id = " + rslFencId.getString("id") + "; \n");
				}
			} else {
				sbSql.append("delete from licb where id = " + delrsl.getString("id") + "; \n");
			}
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			String strSql = "select id from diancxxb where fuid = "+ getTreeid();
			ResultSetList rsl = con.getResultSetList(strSql); 
			
//			如果煤矿下拉框选择为空那么向数据库插入默认值
			String meikxxb_id;
			if (0 == (getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"))) {
				meikxxb_id = "default";
			} else {
				meikxxb_id = String.valueOf((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")));
			}
			
			if ("0".equals(mdrsl.getString("id"))) {
				// 插入操作
				while (rsl.next()) {
					sbSql.append("insert into licb(id, diancxxb_id, faz_id, daoz_id, liclxb_id, zhi, meikxxb_id, beiz) values (")
					.append("getnewid("+ rsl.getString("id") +")").append(", ").append(rsl.getString("id")).append(", ")
					.append((getExtGrid().getColumn("faz_id").combo).getBeanId(mdrsl.getString("faz_id"))).append(", ")
					.append((getExtGrid().getColumn("daoz_id").combo).getBeanId(mdrsl.getString("daoz_id"))).append(", ")
					.append((getExtGrid().getColumn("liclxb_id").combo).getBeanId(mdrsl.getString("liclxb_id"))).append(", ")
					.append(mdrsl.getString("zhi")).append(", ")
					.append(meikxxb_id).append(", '")
					.append(mdrsl.getString("beiz")).append("'); \n");
				}
				sbSql.append("insert into licb(id, diancxxb_id, faz_id, daoz_id, liclxb_id, zhi, meikxxb_id, beiz) values (")
				.append("getnewid("+ getTreeid() +")").append(", ").append(getTreeid()).append(", ")
				.append((getExtGrid().getColumn("faz_id").combo).getBeanId(mdrsl.getString("faz_id"))).append(", ")
				.append((getExtGrid().getColumn("daoz_id").combo).getBeanId(mdrsl.getString("daoz_id"))).append(", ")
				.append((getExtGrid().getColumn("liclxb_id").combo).getBeanId(mdrsl.getString("liclxb_id"))).append(", ")
				.append(mdrsl.getString("zhi")).append(", ")
				.append(meikxxb_id).append(", '")
				.append(mdrsl.getString("beiz")).append("'); \n");
				
			} else {
				// 更新操作
				String sqlFencId = "select id from diancxxb where fuid = "+ mdrsl.getString("diancxxb_id");
				ResultSetList rslData = con.getResultSetList(sqlFencId);
				if (rslData.next()) {
					String sql = 
						"select l.id\n" +
						"from licb l, (select * from licb where id = "+ mdrsl.getString("id") +") f\n" + 
						"where l.faz_id = f.faz_id\n" + 
						"  and l.daoz_id = f.daoz_id\n" + 
						"  and l.liclxb_id = f.liclxb_id\n" + 
						"  and nvl(l.meikxxb_id, 0) = nvl(f.meikxxb_id, 0)";
					ResultSetList rslFencId = con.getResultSetList(sql);

					while (rslFencId.next()) {
						sbSql.append("update licb set faz_id = ")
						.append((getExtGrid().getColumn("faz_id").combo).getBeanId(mdrsl.getString("faz_id"))).append(", ")
						.append("daoz_id = ")
						.append((getExtGrid().getColumn("daoz_id").combo).getBeanId(mdrsl.getString("daoz_id"))).append(", ")
						.append("liclxb_id = ")
						.append((getExtGrid().getColumn("liclxb_id").combo).getBeanId(mdrsl.getString("liclxb_id"))).append(", ")
						.append("meikxxb_id = ")
						.append(meikxxb_id).append(", ")
						.append("zhi = ").append(mdrsl.getString("zhi")).append(", ").append("beiz = '").append(mdrsl.getString("beiz"))
						.append("' where id = ").append(rslFencId.getString("id")).append("; \n");
					}
				} else {
					sbSql.append("update licb set faz_id = ")
					.append((getExtGrid().getColumn("faz_id").combo).getBeanId(mdrsl.getString("faz_id"))).append(", ")
					.append("daoz_id = ")
					.append((getExtGrid().getColumn("daoz_id").combo).getBeanId(mdrsl.getString("daoz_id"))).append(", ")
					.append("liclxb_id = ")
					.append((getExtGrid().getColumn("liclxb_id").combo).getBeanId(mdrsl.getString("liclxb_id"))).append(", ")
					.append("meikxxb_id = ")
					.append(meikxxb_id).append(", ")
					.append("zhi = ").append(mdrsl.getString("zhi")).append(", ").append("beiz = '").append(mdrsl.getString("beiz"))
					.append("' where id = ").append(mdrsl.getString("id")).append("; \n");
				}
			}
		}
		sbSql.append("end;");
//		System.out.println("Licext save() sbSql: \n" + sbSql);
		con.getUpdate(sbSql.toString());
		mdrsl.close();
		con.Close();
	}
	 
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
		if(_Refreshclick){
			_Refreshclick=false;
			this.getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "SELECT   l.ID, l.diancxxb_id, z.mingc AS faz_id, dz.mingc AS daoz_id,\n"
			+ "         x.mingc AS liclxb_id, l.zhi, m.mingc as meikxxb_id,l.beiz\n"
			+ "    FROM licb l, chezxxb z, chezxxb dz, liclxb x, diancxxb d, meikxxb m\n"
			+ "   WHERE l.faz_id = z.ID\n"
			+ "     AND l.daoz_id = dz.ID\n"
			+ "     AND x.ID = l.liclxb_id\n"
			+ "     AND l.diancxxb_id = d.ID\n"
			+ "		and m.id(+)=l.meikxxb_id \n"
			+ "     AND d.ID = "
//			+ visit.getDiancxxb_id()
			+ this.getTreeid()
			+ "\n"
			+ "ORDER BY z.xuh, z.mingc, m.mingc,x.xuh,x.mingc";
		
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("licb");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("liclxb_id").setHeader("类型");
		egu.getColumn("zhi").setHeader("值(km)");
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("diancxxb_id").setHidden(true);
//		egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		egu.getColumn("diancxxb_id").setDefaultValue(""+this.getTreeid());
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
//		String sql = "select id from diancxxb where id="
//				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
//		ResultSet rs = con.getResultSet(sql);
//		String diancid = "";
//		try {
//			while (rs.next()) {
//				diancid = rs.getString("id");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		egu.getColumn("diancxxb_id").setDefaultValue(diancid);
		egu.getColumn("daoz_id").setEditor(new ComboBox());
		egu.getColumn("daoz_id").setComboEditor(
				egu.gridId,
				new IDropDownModel("select distinct c.id, c.mingc as chezmc "
						+ " from chezxxb c " 
//						"where " + " c.leib = '港口'"
						+ " order by c.mingc"));
		egu.getColumn("faz_id").setEditor(new ComboBox());
		egu.getColumn("faz_id").setComboEditor(
				egu.gridId,
				new IDropDownModel("select distinct c.id, c.mingc as chezmc "
						+ " from chezxxb c " 
//						"where " + " c.leib = '港口'"
						+ " order by c.mingc"));
		egu.getColumn("liclxb_id").setEditor(new ComboBox());
		egu.getColumn("liclxb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id, mingc from liclxb order by xuh, mingc"));
//		煤矿信息
		String meik = 
			"select id,mingc from(\n" +
			"select 0 as id, '' as mingc from meikxxb\n" + 
			"union\n" + 
			"select id, mingc from meikxxb order by mingc\n" + 
			")\n" + 
			"order by decode(mingc, '', 1, 2),mingc";

		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(meik));
		egu.getColumn("meikxxb_id").editor.setAllowBlank(true);

		//设置树
		egu.addTbarText("电厂:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){");
		
		rsb.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		String condition = "var Mrcd_bo = gridDiv_ds.getModifiedRecords();" +
		"for(i = 0; i < Mrcd_bo.length; i ++){" +
		"	if(Mrcd_bo[i].get('ZHI') != '0' && Mrcd_bo[i].get('ZHI') == '') {Ext.MessageBox.alert('提示信息','字段 值(km) 不能为空');return;}" +
		"}\n";
		
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton", condition);
//		GridButton gdb = new GridButton(GridButton.ButtonType_Save_condition, egu.gridId, egu.getGridColumns(), "SaveButton", condition);
//		egu.addTbarBtn(gdb);
		
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "alert(url);"
				+ "alert(document.location.host);"
				+ "alert(document.location.pathname);"
				+ "var end = url.indexOf(';');"
				+ "alert(end);"
				+ "alert(url);"
				+ "url = url.substring(0,end);"
				+ "alert(url);"
				+ "url = url + '?service=page/' + 'Licreport&lx=rezc';"
				+ "alert(url);"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("打印", "function (){" + str + "}").getScript()
				+ "}");

		setExtGrid(egu);
		con.Close();
	}
//-----电厂tree
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
		return getTree().getWindowTreeScript();
	}
	
	//--------------------------------
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
			visit.setList1(null);
			this.setTreeid(null);
			
		}
		
		getSelectData();
	}
}
