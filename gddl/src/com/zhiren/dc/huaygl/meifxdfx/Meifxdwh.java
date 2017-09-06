package com.zhiren.dc.huaygl.meifxdfx;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*����: ���ܱ�
 *����:2010-4-25 15:22:03
 *����:���ı��淽���е�biaozzΪ�ַ�������,ͬʱ���ݿ�Ҳ�������޸���.
 * 
 */
/**
 * @author yinjm
 * ������ú��ϸ��ά��
 */

public class Meifxdwh extends BasePage implements PageValidateListener {
	
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
	
	private String Fenxrq; // �����������
	
	public String getFenxrq() {
		return Fenxrq;
	}

	public void setFenxrq(String fenxrq) {
		Fenxrq = fenxrq;
	}
	
	private String rowNumIndex; // ������ѡ��ļ�¼��id�������䴫���¸�ҳ��

	public String getRowNumIndex() {
		return rowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		this.rowNumIndex = rowNumIndex;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	"����ָ��"��ť
	private boolean _ShezzbClick = false;
	
	public void ShezzbButton(IRequestCycle cycle) {
		_ShezzbClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_ShezzbClick) {
			_ShezzbClick = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString10(getRowNumIndex()); // ����ѡ��ļ�¼��id�����¸�ҳ��
			cycle.activate("Meifxdxxwh"); // ��ת��ú��ϸ����ϸά��ҳ��
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from meifxdfxb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into meifxdfxb(id, diancxxb_id, fenxrq, quyrq, biaozz, beiz")
				.append(") values(getnewid(").append(getTreeid()).append("), ").append(getTreeid()).append(", to_date('")
				.append(getFenxrq()).append("', 'yyyy-mm-dd'), to_date('").append(mdrsl.getString("quyrq")).append("', 'yyyy-mm-dd'), '")
				.append(mdrsl.getString("biaozz")).append("', '").append(mdrsl.getString("beiz")).append("');\n");
			} else {
				sbsql.append("update meifxdfxb set ")
				.append(" quyrq = to_date('").append(mdrsl.getString("quyrq")).append("', 'yyyy-mm-dd'), ")
				.append("biaozz = '").append(mdrsl.getString("biaozz"))
				.append("', beiz = '").append(mdrsl.getString("beiz")).append("'")
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql = 
			"select fx.id, fx.quyrq, fx.biaozz, fx.beiz\n" +
			"  from meifxdfxb fx\n" + 
			" where fx.diancxxb_id = "+ getTreeid() +"\n" + 
			"   and fx.fenxrq = to_date('"+ getFenxrq() +"', 'yyyy-mm-dd') order by fx.quyrq";
		
		ResultSetList rsl =  con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("quyrq").setHeader("ȡ������");
		egu.getColumn("biaozz").setHeader("��׼ֵ");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(200);
		
		egu.addTbarText("�糧��");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("�������ڣ�");
		DateField df = new DateField();
		df.setValue(getFenxrq());
		df.setId("riq");
		df.Binding("Fenxrq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		String handler = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelected()==null){\n" + 
			"      Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ�м�¼��');\n" + 
			"      return;\n" + 
			"    }\n" + 
			"    var rec=gridDiv_sm.getSelected();\n" +
			"    document.all.RowNumIndex.value=rec.get('ID');\n" +
			"    document.getElementById('ShezzbButton').click();\n" + 
			"}";
		egu.addTbarBtn(new GridButton("����ָ��", handler, SysConstant.Btn_Icon_SelSubmit));
		
		egu.addOtherScript("gridDiv_sm.singleSelect = true;\n");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * �����ҳ����ȡ����ֵΪ�ջ��ǿմ�����ô�����ݿⱣ���ֶε�Ĭ��ֵ
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			setFenxrq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}