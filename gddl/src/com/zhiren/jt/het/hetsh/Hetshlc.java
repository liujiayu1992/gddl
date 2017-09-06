package com.zhiren.jt.het.hetsh;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Liucdzcl;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.menu.Menu;
import com.zhiren.common.ext.menu.TextItem;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Hetshlc extends BasePage implements PageValidateListener {
	private String msg = "";

	List List_TableName = null; // Ҫ���в����ı�

	List List_TableId = null;

	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	protected void initialize() {
		msg = "";
		MenuId = "";
		OpenWindow = "";
	}
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String ShenHeYJChange;
	
	public String getShenHeYJChange() {
		return ShenHeYJChange;
	}

	public void setShenHeYJChange(String shenHeYJChange) {
		ShenHeYJChange = shenHeYJChange;
	}
	
	private String Histry_opinion;
	
	public String getHistry_opinion() {
		return Histry_opinion;
	}

	public void setHistry_opinion(String histry_opinion) {
		Histry_opinion = histry_opinion;
	}
	
	private String My_opinion;
	
	public String getMy_opinion() {
		return My_opinion;
	}

	public void setMy_opinion(String my_opinion) {
		My_opinion = my_opinion;
	}

	private String MenuId;
	
	public String getMenuId() {
		return MenuId;
	}

	public void setMenuId(String menuId) {
		MenuId = menuId;
	}
	
	private String OpenWindow;
	
	public String getOpenWindow() {
		return OpenWindow;
	}

	public void setOpenWindow(String openWindow) {
		OpenWindow = openWindow;
		
	}

	private boolean DzclButton = false;

	public void DzclButton(IRequestCycle cycle) {
		DzclButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (DzclButton) {
			DzclButton = false;
			this.DzclButtonCF();
		}
	}
	
	private void DzclButtonCF(){
		// ��¼id
		Liucdzcl.idStrSource = this.Change;
		// ����TalbeNameList��TableIdList �Ա�����ʹ��
		this.Dongzcl();
		// ���ú���Ķ�������������Ҫ������ҳ��
		this.OpenWindow = Liucdzcl.Dongzcl(this.MenuId, List_TableName, List_TableId);
		// �ѱ����ͱ�id��list�������ַ���
		// ���磺hetb,3001;
		String TableNameIdStr = Liucdzcl.TableNameIdStr(List_TableName, List_TableId);
		
		((Visit) getPage().getVisit()).setLiucclsb(new StringBuffer(
				TableNameIdStr + "+" + this.Histry_opinion));
	}
	
	public void Tij() {
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		
		if (!(this.getChange().equals("") || this.getChange() == null)) {
			String change[] = this.getChange().split(";");
			for (int i = 0; i < change.length; i++) {
				if (change[i] == null || "".equals(change[i])) {
					continue;
				}
				String record[] = change[i].split(",");
				Liuc.tij("hetb", Long.parseLong(record[0]), renyxxb_id, record[10]);
			}
		}
	}
	
	public void getSelectData() {
		
		String sql = "";
		JDBCcon con = new JDBCcon();
		String leib = "��ͬ";
		int rsl_rows = 0;
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		String where = getTreeid();
		
		if (getWeizSelectValue().getId() == 1) {// �Լ�������
			sql = "select * " + 
				  "  from (select dm.id, dm.hetbh, dm.gongfdwmc, dm.xufdwmc, dm.qiandrq," + 
				  "           	  dm.liucztb_id, leibztb.mingc as zhuangt, sum(sl.hetl) hetl, lz.liucb_id," + 
				  "               nvl('�鿴', '') as link, nvl('', '') as yij, nvl('', '') as histryyj" + 
				  "          from hetb dm, hetslb sl, liucztb lz, leibztb, hetb_mb mb" + 
				  "         where dm.id = sl.hetb_id(+)" +
				  "			  and dm.liucztb_id = lz.id " + 
				  "			  and lz.leibztb_id = leibztb.id " + 
				  "			  and dm.hetb_mb_id = mb.id " + 
				  "			  and mb.liucb_id = " + getLiucSelectValue().getId() + 
				  "			  and dm.diancxxb_id in (" + where + ") " + 
				  "			  and dm.id in (" + Liuc.getWodrws("hetb", renyxxb_id, leib) + ")" + 
				  "			group by dm.id, dm.hetbh, dm.gongfdwmc, dm.xufdwmc, dm.qiandrq, dm.liucztb_id, leibztb.mingc, lz.liucb_id)";
		} else {
			sql = "select * " + 
				  "  from (select dm.id, dm.hetbh, dm.gongfdwmc, dm.xufdwmc, dm.qiandrq," + 
				  "         	  dm.liucztb_id, leibztb.mingc as zhuangt, sum(sl.hetl) hetl, lz.liucb_id," + 
				  "         	  nvl('�鿴', '') as link, nvl('', '') as yij, nvl('', '') as histryyj" + 
				  "          from hetb dm, hetslb sl, liucztb lz, leibztb, hetb_mb mb" + 
				  "         where dm.id = sl.hetb_id(+)" +
				  "			  and dm.liucztb_id = lz.id " + 
				  "			  and lz.leibztb_id = leibztb.id " + 
				  "			  and dm.hetb_mb_id = mb.id " + 
				  "			  and mb.liucb_id = " + getLiucSelectValue().getId() + 
				  "			  and dm.diancxxb_id in (" + where + ") " + 
				  "			  and dm.id in (" + Liuc.getLiuczs("hetb", renyxxb_id, leib) + ")" + 
				  "			group by dm.id, dm.hetbh, dm.gongfdwmc, dm.xufdwmc, dm.qiandrq, dm.liucztb_id, leibztb.mingc, lz.liucb_id)";
		}
		
		ResultSetList rsl = con.getResultSetList(sql);
		rsl_rows = rsl.getRows();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);//�趨��¼����Ӧ�ı�
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("gongfdwmc").setHeader("������λ����");
		egu.getColumn("gongfdwmc").setWidth(200);
		egu.getColumn("xufdwmc").setHeader("�跽��λ����");
		egu.getColumn("xufdwmc").setWidth(200);
		egu.getColumn("qiandrq").setHeader("ǩ������");
		egu.getColumn("liucztb_id").setHidden(true);
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("hetl").setHeader("��ͬ��");
		egu.getColumn("liucb_id").setHidden(true);
		egu.getColumn("link").setHeader("");
		egu.getColumn("yij").setHidden(true);
		egu.getColumn("histryyj").setHidden(true);
		
		String str = " var url = 'http://' + document.location.host+document.location.pathname;" + 
					 " var end = url.indexOf(';');" + 
					 " url = url.substring(0, end);" + 
					 " url = url + '?service=page/' + 'Shenhrz&hetb_id=' + record.data['ID'];";
		
		egu.getColumn("link").setRenderer("function(value, p, record) {" + str + 
			" return \"<a href=# onclick=window.open('\"+url+\"','newWin')>�鿴</a>\"}");
		
		List tmp = new ArrayList();
		
		for (int i = 0; i < rsl_rows; i++) {
			String strtmp = "";
			tmp = Liuc.getRiz(Long.parseLong(egu.getDataValue(i, 0)));
			for (int j = 0; j < tmp.size(); j++) {
				strtmp += ((Yijbean) tmp.get(j)).getXitts() + "\\n" + 
						  (((Yijbean) tmp.get(j)).getYij() == null ? "" : ((Yijbean) tmp.get(j)).getYij()) + "\\n";
			}
			egu.setDataValue(i, 11, "��ͬ��� " + egu.getDataValue(i, 1) + "��\\n " + strtmp);
		}
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(18);		
		
		egu.addTbarText("�糧���ƣ�");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
				((Visit)getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("ʹ�����̣�");
		ComboBox LiucSelect = new ComboBox();
		LiucSelect.setId("Liuc");
		LiucSelect.setWidth(80);
		LiucSelect.setLazyRender(true);
		LiucSelect.setTransform("LiucSelectx");
		egu.addToolbarItem(LiucSelect.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("����״̬��");
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(80);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
		egu.addToolbarItem(WeizSelect.getScript());
		egu.addTbarText("-");
		
		egu.addOtherScript("Liuc.on('select', function() {document.forms[0].submit();});");
		egu.addOtherScript("Weizx.on('select', function() {document.forms[0].submit();});");
		egu.addOtherScript("gridDiv_sm.on('rowselect', function(own, row, record) {document.all.item('EditTableRow').value = row;});");
		
		if (((Visit) getPage().getVisit()).getboolean4()) {
			Menu MuWdqx = new Menu();
			
			sql = "select distinct\n" + 
				  "       liucdzb.id,\n" + 
				  "       decode(liucdzb.mingc, '�ύ', '��<' || leibztb.mingc || '>' || liucdzb.mingc || '<' || GetLiuc_Hjzt(liucdzb.id) || '>',\n" +  
				  "             '����', '��<' || leibztb.mingc || '>' || liucdzb.mingc || '<' || GetLiuc_Hjzt(liucdzb.id) || '>') as mingc,\n" + 
				  "       'onMenuItemClick' as dongz\n" + 
				  "  from liucdzjsb, liucdzb, liucztb, liuclbb, leibztb, liucb\n" + 
				  " where liucdzjsb.liucdzb_id = liucdzb.id\n" + 
				  "   and liucdzb.liucztqqid = liucztb.id\n" + 
				  "   and liucztb.leibztb_id = leibztb.id\n" + 
				  "   and leibztb.liuclbb_id = liuclbb.id\n" + 
				  "	  and liucztb.liucb_id = liucb.id\n" + 
				  "	  and liucb.id = " + getLiucSelectValue().getId() + 
				  "   and liuclbb.mingc = '��ͬ'\n" + 
				  "   and liucdzjsb.liucjsb_id in (select liucjsb_id\n" + 
				  "								     from renyjsb\n" + 
				  "                                 where renyxxb_id = " + renyxxb_id + ")";
			List List_Wdqx = new ArrayList();
			rsl = con.getResultSetList(sql);
			while (rsl.next()) {
				List_Wdqx.add(new TextItem(rsl.getString("id"), rsl.getString("mingc"), rsl.getString("dongz")));
			}
			
			MuWdqx.setItems(List_Wdqx);
			egu.addToolbarItem("{text:'�ҵ�Ȩ��', menu:" + MuWdqx.getScript() + "}");
			
			egu.addOtherScript("\nfunction onMenuItemClick(item) {\n");
			egu.addOtherScript("	  var rc = gridDiv_grid.getSelectionModel().getSelections();\n");
			egu.addOtherScript("	  var value = '';\n");
			egu.addOtherScript("	  var strmyp = '';\n");
			egu.addOtherScript("	  document.all.Histry_opinion.value = '';\n");
			egu.addOtherScript("	  document.all.My_opinion.value = '';\n");
			egu.addOtherScript("	  document.all.ShenHeYJChange.value = '';\n");
			egu.addOtherScript("	  if (rc.length > 0) {\n");
			egu.addOtherScript("	      for (var i = 0; i < rc.length; i++) {\n");
			egu.addOtherScript("			  value += rc[i].get('ID') + ';';\n");
			egu.addOtherScript("              document.all.ShenHeYJChange.value += rc[i].get('ID') + ',' + rc[i].get('HETBH') + ',' + rc[i].get('GONGFDWMC') + ',' + rc[i].get('XUFDWMC') + ',' + rc[i].get('QIANDRQ') + ',' + rc[i].get('LIUCZTB_ID') + ',' + rc[i].get('ZHUANGT') + ',' + rc[i].get('HETL') + ',' + rc[i].get('LIUCB_ID') + ',' + rc[i].get('LINK') + ',' + rc[i].get('YIJ') + ',' + rc[i].get('HISTRYYJ') + ';';\n");
			egu.addOtherScript(" 	  		  if (strmyp.substring(rc[i].get('YIJ')) > -1) {\n");
			egu.addOtherScript(" 		  	      if (strmyp == '') {\n");
			egu.addOtherScript("					  strmyp = rc[i].get('YIJ');\n");
			egu.addOtherScript("				  } else {\n");
			egu.addOtherScript("					  strmyp += ',' + rc[i].get('YIJ');\n");
			egu.addOtherScript("				  }\n");
			egu.addOtherScript("			  }\n");
			egu.addOtherScript(" 		      var strtmp = rc[i].get('HISTRYYJ');\n");
			egu.addOtherScript(" 		      document.all.Histry_opinion.value += strtmp + '\\n';\n");
			egu.addOtherScript("          }");
			egu.addOtherScript("        document.all.My_opinion.value = strmyp;\n");
			egu.addOtherScript("	    document.getElementById('CHANGE').value = value;\n");
			egu.addOtherScript("	    document.getElementById('MenuId').value = item.id;\n");
			egu.addOtherScript("	    document.getElementById('DzclButton').click();\n");
			egu.addOtherScript("	  } else {\n");
			egu.addOtherScript("		  Ext.MessageBox.alert('��ʾ��Ϣ', '��ѡ��Ҫ�����ļ�¼!');\n");
			egu.addOtherScript("	  }\n");
			egu.addOtherScript("}\n");
		}
		
		if (!((Visit) getPage().getVisit()).getboolean4()) {
			egu.addToolbarItem("{" + new GridButton("��ʷ���", "function() {\n" + 
					" var rc = gridDiv_grid.getSelectionModel().getSelections();\n" + 
					" var val = '';\n" + 
					" if (rc.length > 0) {\n" + 
					"     for (var i = 0; i < rc.length; i++) {\n" + 
					"	      val += rc[i].get('HISTRYYJ') + '\\n';\n" + 
					"     }\n" + 
					"     document.all.tab.value = val;\n" + 
					"     window_panel.setVisible(true);\n" + 
					" } else {\n" + 
					"     Ext.MessageBox.alert('��ʾ��Ϣ', '��ѡ��Ҫ�����ļ�¼��');\n" + 
					" }\n" + 
					"}\n").getScript() + "}");
		}
		
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
	
	// λ��
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ҵ�����"));
		list.add(new IDropDownBean(2, "������"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}
	
	// ʹ������
	public IDropDownBean getLiucSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLiucSelectModel()
							.getOption(0));
		}
		
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLiucSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {
			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public void setLiucSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLiucSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getLiucSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getLiucSelectModels() {
		String sql = "select liucb.id, liucb.mingc " +
				     "  from liucb, liuclbb" +
				     " where liucb.liuclbb_id = liuclbb.id" +
				     "   and liuclbb.mingc = '��ͬ'";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
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
			visit.setInt1(-1);
			// 2
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			getWeizSelectModel();
			// 2
			setLiucSelectValue(null);
			setLiucSelectModel(null);
			getLiucSelectModel();
			visit.setboolean1(false);// λ��
			visit.setboolean2(false);// ʹ������
			visit.setboolean3(false);// ��λ
			visit.setboolean4(true);// �ҵ�����������
		}
		
		if (((Visit) getPage().getVisit()).getboolean1()
				|| ((Visit) getPage().getVisit()).getboolean2()
				|| ((Visit) getPage().getVisit()).getboolean3()) {// �����ͬλ�øı�
			// 1, λ��2, ����3, ��λ
			if (((Visit) getPage().getVisit()).getboolean1() == true) {
				if (getWeizSelectValue().getId() == 1) {
					visit.setboolean4(true);
				} else {
					visit.setboolean4(false);
				}
			}
			
			visit.setboolean1(false);
			visit.setboolean2(false);
			visit.setboolean3(false);
		}
		
		getSelectData();
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
	
	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}
	
	public void setEditTableRow(int value){
		
		((Visit) this.getPage().getVisit()).setInt1(value);
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
	
	public String getWunScript(){
		return "for (var i = 0; i < rec.length; i++) {\n" + 
			   "    rec[i].set('YIJ', document.getElementById('My_opinion').value);\n" + 
			   "}";
	}
	
	public void Shuaxin(IRequestCycle cycle) {
		this.OpenWindow = "";
		this.getSelectData();
	}
	
	private void Dongzcl() {
		// TODO �Զ����ɷ������
		// �߼����ú�������˵�����¼���Ĳ���������˵���ش��������ݣ�
		// 1��ѡ��ĺ�ͬid����ϵ������ҳ��Ĳ���
		// 2��liucdzb.id�����̶�����id���������id������֪��liucdz���õĶ�����ʲô
		// ��ģ��Ҫ����"Liucdzcl"�ķ���������TableName,TableID,liucdzb.id
		
		if (!this.getChange().equals("")) {
			List_TableName = new ArrayList(); // Ҫ���в����ı�
			List_TableId = new ArrayList(); // Ҫ���в������id
			String tmp[] = getChange().split(";"); // ����ѡ��ͬ��¼��id����洢
			String hetbId = ""; // ��ͬ��id

			for (int i = 0; i < tmp.length; i++) {
				hetbId = tmp[i].toString();
				// �õ���ͬid
				
				// ��jiesb����List
				List_TableName.add("hetb");
				// ��jiesb_id����List
				List_TableId.add(hetbId);
			}
		}
	}	
}