package com.zhiren.jt.het.hetsh;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Hetfuj extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO �Զ����ɷ������
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

//		����ļ�����·��
		String Imagelj=MainGlobal.getXitxx_item("��ͬ", "��ͬ����·��", "0", "D:\\\\zhiren\\\\het");
		Imagelj=Imagelj + "\\\\" + this.getTreeid() + "\\\\";
		Imagelj=Imagelj.replaceAll("\\\\", "\\\\\\\\");
		String Sql="SELECT  H.ID, HETBH, GONGFDWMC, XUFDWMC, QIANDRQ, \n" +
			"     decode(T.BIANM,null,null,'<a href=# onclick=window.open(''"+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETHETFILENAME(H.ID)||''')>�鿴����</a>') FUJMC \n"+
			" FROM HETB H,\n" +
			"      DIANCXXB DC,\n" + 
			"      (SELECT T.BIANM FROM TUPCCB T WHERE T.MOKMC = 'HET') T\n" + 
			"WHERE H.DIANCXXB_ID = DC.ID\n" + 
			"  AND H.ID = T.BIANM(+)\n" + 
			"  AND TRUNC(H.QIANDRQ, 'y') = DATE '"+intyear+"-01-01'\n" + 
			"  AND H.LIUCZTB_ID = 0"+
			"  AND DC.ID = "+this.getTreeid();

		ResultSetList rsl = con.getResultSetList(Sql);
		
		rsl.beforefirst();
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("PAND_GDJT");
		egu.setWidth("bodyWidth");

		egu.setDefaultsortable(false);//����ÿ�б�ͷ����󲻿�������
		egu.getColumn("HETBH").setCenterHeader("��ͬ���");
		egu.getColumn("HETBH").setWidth(150);
		egu.getColumn("HETBH").setEditor(null);
		
		egu.getColumn("GONGFDWMC").setCenterHeader("������λ����");
		egu.getColumn("GONGFDWMC").setWidth(250);
		egu.getColumn("GONGFDWMC").setEditor(null);
		
		egu.getColumn("XUFDWMC").setCenterHeader("�跽��λ����");
		egu.getColumn("XUFDWMC").setWidth(200);
		egu.getColumn("XUFDWMC").setEditor(null);
		
		egu.getColumn("QIANDRQ").setCenterHeader("ǩ������");
		egu.getColumn("QIANDRQ").setWidth(100);
		egu.getColumn("QIANDRQ").setEditor(null);
		
		egu.getColumn("FUJMC").setCenterHeader("����");
		egu.getColumn("FUJMC").setWidth(100);
		egu.getColumn("FUJMC").setEditor(null);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(23);// ���÷�ҳ
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
	
		// ********************������************************************************
		egu.addTbarText("ǩ�����:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		
		String script="gridDiv_grid.on('rowclick',function(e){document.all.item(\"BTNTJ\").disabled=false;}); \n";
		egu.addOtherScript(script);
		
		
		// ������
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		egu.addToolbarButton("ˢ��", GridButton.ButtonType_Refresh, "RefreshButton");
		
//		��Ӹ���
		GridButton gbphoto = new GridButton("��Ӹ���","function (){"+MainGlobal.getOpenWinScript("HetFujUpLoad&id='+gridDiv_grid.getSelectionModel().getSelected().get(\"ID\")+'","480","140")+ "}");
		gbphoto.setIcon(SysConstant.Btn_Icon_Create);
		gbphoto.setId("BTNTJ");
		egu.addTbarBtn(gbphoto);
		
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
		if(visit.getActivePageName().toString().equals("PandFujUpLoad")){
			visit.setActivePageName(getPageName().toString());
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setMsg(null);
		}
		getSelectData();
	}

	// ���
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
		for (i = 2012; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
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
}