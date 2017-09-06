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
 * ����:tzf
 * ʱ��:2009-4-21
 * �޸�����:���ӵ糧����֧��һ������
 */
/* �޸����ݣ�1�����save()���������޸ĵ糧������3�ĵ糧����ʱ���õ糧�������糧������Ҳͬʱ���Ÿ��ġ�
 *    2���ж����ú��������ѡ��Ϊ����ô�����ݿ����Ĭ��ֵ��
 *    3�������水ť���Js��У��ֵ(ZHI)�ֶβ���Ϊ�ա�
 * �޸�ʱ�䣺2009-09-14
 * �޸��ˣ�������
 */
/*
	 * ���ߣ���ʤ��
	 * ʱ�䣺2013-03-29
	 * ���÷�Χ��ׯ�ӵ糧
	 * ����������sql ������ׯ�Ӹۿ�ȡ��վ��վ��Ϣ
	 */
public class Licext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// ҳ��仯��¼
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
			// ɾ������
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
			
//			���ú��������ѡ��Ϊ����ô�����ݿ����Ĭ��ֵ
			String meikxxb_id;
			if (0 == (getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"))) {
				meikxxb_id = "default";
			} else {
				meikxxb_id = String.valueOf((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")));
			}
			
			if ("0".equals(mdrsl.getString("id"))) {
				// �������
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
				// ���²���
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
		egu.getColumn("faz_id").setHeader("��վ");
		egu.getColumn("daoz_id").setHeader("��վ");
		egu.getColumn("liclxb_id").setHeader("����");
		egu.getColumn("zhi").setHeader("ֵ(km)");
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("beiz").setHeader("��ע");
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
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
//		egu.getColumn("diancxxb_id").setDefaultValue(diancid);
		egu.getColumn("daoz_id").setEditor(new ComboBox());
		egu.getColumn("daoz_id").setComboEditor(
				egu.gridId,
				new IDropDownModel("select distinct c.id, c.mingc as chezmc "
						+ " from chezxxb c " 
//						"where " + " c.leib = '�ۿ�'"
						+ " order by c.mingc"));
		egu.getColumn("faz_id").setEditor(new ComboBox());
		egu.getColumn("faz_id").setComboEditor(
				egu.gridId,
				new IDropDownModel("select distinct c.id, c.mingc as chezmc "
						+ " from chezxxb c " 
//						"where " + " c.leib = '�ۿ�'"
						+ " order by c.mingc"));
		egu.getColumn("liclxb_id").setEditor(new ComboBox());
		egu.getColumn("liclxb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id, mingc from liclxb order by xuh, mingc"));
//		ú����Ϣ
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

		//������
		egu.addTbarText("�糧:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){");
		
		rsb.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		String condition = "var Mrcd_bo = gridDiv_ds.getModifiedRecords();" +
		"for(i = 0; i < Mrcd_bo.length; i ++){" +
		"	if(Mrcd_bo[i].get('ZHI') != '0' && Mrcd_bo[i].get('ZHI') == '') {Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ֵ(km) ����Ϊ��');return;}" +
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
				+ new GridButton("��ӡ", "function (){" + str + "}").getScript()
				+ "}");

		setExtGrid(egu);
		con.Close();
	}
//-----�糧tree
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			
		}
		
		getSelectData();
	}
}
