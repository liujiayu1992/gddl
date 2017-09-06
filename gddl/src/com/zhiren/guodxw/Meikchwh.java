package com.zhiren.guodxw;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author ������
 * 2009-08-21
 * ������Meikchwh(ú�󳵺�ά��)
 */

public class Meikchwh extends BasePage {
	
	boolean isClick = false; // �ж��Ƿ�������ѯ���š���ť��falseΪ��trueΪ�ǡ�
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	private String Cheph1;
//	
//	public String getCheph1() {
//		return Cheph1;
//	}
//
//	public void setCheph1(String cheph1) {
//		Cheph1 = cheph1;
//	}
	
//	ú������������_��ʼ
	public IDropDownBean getMeikValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getMeikModel().getOptionCount()>0) {
				setMeikValue((IDropDownBean)getMeikModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setMeikValue(IDropDownBean gongysValue) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(gongysValue);
	}
	
	public IPropertySelectionModel getMeikModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setMeikModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setMeikModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setMeikModels() {
		String str = "select 0 id, 'ȫ��' mingc from dual union select id, mingc " +
			"from meikxxb order by id";
		setMeikModel(new IDropDownModel(str));
	}
//	ú������������_����
	
//	�糧��_��ʼ
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
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
//	�糧��_����
	
	private boolean _SearchChephChick = false;

	public void SearchChephButton(IRequestCycle cycle) {
		_SearchChephChick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save();
		}
		if (_SearchChephChick) {
			_SearchChephChick = false;
			searchCheph();
		}
	}
	
//	��ѯ��Ƥ��
	private void searchCheph() {
		
		JDBCcon con = new JDBCcon();
		String str = "select meikxxb_id from qicmkb where cheph = '"+ this.getChange() +"'";
		ResultSetList rsl = con.getResultSetList(str);
		
		if (rsl.next()) {
			setMeikValue((IDropDownBean)getMeikModel().translateValue(rsl.getString("meikxxb_id")));
			isClick = true; // ����ѯ���š���ť�ѱ����
			getSelectData();
		} else {
			this.setMsg("���� ��" + this.getChange() + "�� û�б�ú��ʹ�ã�");
		}
		
	}
	
	private void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from qicmkb where id = ").append(delrsl.getString("id")).append("; \n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			String searchCheph = "select mkxx.mingc mkmc, qcmk.meikxxb_id, qcmk.cheph " +
				"from qicmkb qcmk, meikxxb mkxx where qcmk.meikxxb_id = mkxx.id and cheph = '"+ mdrsl.getString("cheph") +"'";
			ResultSetList ChephRsl = con.getResultSetList(searchCheph);
			if (ChephRsl.next()) {
				this.setMsg("���� ��"+ ChephRsl.getString("cheph") +"�� �ѱ� ��"+ ChephRsl.getString("mkmc") +"�� ʹ��!");
				continue;
			} else {
				String piz = mdrsl.getString("piz");
				if (piz.equals("") || piz == null) {
					piz = "default";
				}
				if ("0".equals(mdrsl.getString("id"))) {
					sbsql.append("insert into qicmkb(id, meikxxb_id, diancxxb_id, cheph, piz, lury, lursj, zhuangt, beiz) values(")
					.append("getnewid("+ getTreeid() +"), ").append((getExtGrid().getColumn("mkmc").combo).getBeanId(mdrsl.getString("mkmc")))
					.append(", ").append(getTreeid()).append(", '").append(mdrsl.getString("cheph"))
					.append("', ").append(piz).append(", '").append(mdrsl.getString("lury")).append("', ")
					.append("to_date('").append(mdrsl.getString("lursj")).append("', 'yyyy-MM-dd, hh24:mi:ss')").append(", ")
					.append("default, '").append(mdrsl.getString("beiz")).append("'); \n");
				} else {
					sbsql.append("update qicmkb set meikxxb_id = ").append((getExtGrid().getColumn("mkmc").combo).getBeanId(mdrsl.getString("mkmc")))
					.append(", cheph = '").append(mdrsl.getString("cheph")).append("', piz = ").append(piz).append(", lury = '")
					.append(mdrsl.getString("lury")).append("', beiz = '").append(mdrsl.getString("beiz")).append("' where id = ")
					.append(mdrsl.getString("id")).append("; \n");
				}
			}
		}
		sbsql.append("end;");
//		���sbsql������Ϊ"begin \nend;"��ô��ִ�����ݿ������
		if (sbsql.length() != 11) {
			con.getUpdate(sbsql.toString());
		}
		mdrsl.close();
		con.Close();
	}
	
	private void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		
		String meikName = getMeikValue().getValue();
		if (meikName.equals("ȫ��")) {
			meikName = "";
		} else {
			meikName = " and qcmk.meikxxb_id = " + getMeikValue().getId();
		}
		
		String str = "select qcmk.id, mkxx.mingc mkmc, qcmk.cheph, qcmk.piz, qcmk.lury, to_char(qcmk.lursj, 'yyyy-MM-dd hh24:mi:ss') lursj," +
			" qcmk.beiz from qicmkb qcmk, meikxxb mkxx\n" + 
			"where qcmk.meikxxb_id = mkxx.id and qcmk.diancxxb_id = "+ getTreeid() + meikName + 
			"\norder by qcmk.meikxxb_id, qcmk.lursj";
		ResultSetList rsl = con.getResultSetList(str);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("mkmc").setHeader("ú������");
		ComboBox mkmc = new ComboBox();
		mkmc.setListWidth(120);
		mkmc.setEditable(true);
		egu.getColumn("mkmc").setEditor(mkmc);
		String mkmcSql = "select id, mingc from meikxxb order by mingc";
		egu.getColumn("mkmc").setComboEditor(egu.gridId, new IDropDownModel(mkmcSql));
		egu.getColumn("mkmc").setReturnId(true);
		if (!getMeikValue().getValue().equals("ȫ��")) {
			egu.getColumn("mkmc").setDefaultValue(getMeikValue().getValue());
		}
		
		egu.getColumn("cheph").setHeader("��Ƥ��");
		egu.getColumn("piz").setHeader("����");
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setDefaultValue(((Visit)getPage().getVisit()).getRenymc());
		egu.getColumn("lursj").setHeader("¼��ʱ��");
		egu.getColumn("lursj").setWidth(120);
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(200);
		
		egu.addTbarText("�糧���ƣ�");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("ú�����ƣ�");
		ComboBox comb = new ComboBox();
		comb.setWidth(100);
		comb.setListWidth(130);
		comb.setTransform("Meik");
		comb.setId("Meik");
		comb.setLazyRender(true);
		comb.setEditable(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Meik.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("��Ƥ�ţ�");
		TextField tfCheph = new TextField();
		tfCheph.setId("tf_cheph");
		tfCheph.setWidth(90);
		egu.addToolbarItem(tfCheph.getScript());
		egu.addTbarText("-");
		
		String handler = "function(){													\n" +
				"	if(tf_cheph.getValue() != ''){										\n" +
				"		document.getElementById('CHANGE').value = tf_cheph.getValue();	\n" +
				" 		document.getElementById('SearchChephButton').click();			\n" +
				"	}else{																\n" +
				"		Ext.MessageBox.alert('��ʾ��Ϣ','�����복Ƥ�ţ�');				\n" +
				"	}																	\n" +
				"}																		\n";
		
		/*
		 * �������ѯ���š���ť����������ݿ��в�ѯ�����ţ����������Javascript����Javascript��������
		 * ��Ҫ��ѯ�ĳ�����һ���ϡ�
		 */
		if (isClick) {
			egu.addOtherScript(
				"var i = -1;															\n" +
				"	i = gridDiv_ds.findBy(function(rec) {								\n" +
				"	if (rec.get('CHEPH') == document.getElementById('CHANGE').value) {	\n" +
				"		i = rec.row;													\n" +
				"		return true;													\n" +
				"	}																	\n" +
				"});																	\n" +
				"if (i>= 0) {															\n" +
				"	gridDiv_sm.selectRow(i);											\n" +
				"}																		\n"
			);
		}
		
		egu.addTbarBtn(new GridButton("��ѯ����", handler, SysConstant.Btn_Icon_Search));
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		setExtGrid(egu);
		rsl.close();
		con.Close();
		
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
		}
		getSelectData();
	}

}
