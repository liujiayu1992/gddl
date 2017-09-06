package com.zhiren.pub.qichjcsjpp;

import java.sql.ResultSet;
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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 2009-05-15
 * ����
 * ȥ��ע���з�GB2312�ַ����ַ�
 */
public class Qichjcsjpp extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
	}
 
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //Ϊ  "ˢ��"  ��ť��Ӵ������
    	getSelectData();
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			if(Savepd()){
				Save();
			}else{
				setMsg("����ʱ�����ظ��ļ�¼������ʧ�ܣ�");
			}
			getSelectData();
		}
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
	}
	
//	���������ж�:�Ƿ���һ�µ�����
	public boolean Savepd() {
		JDBCcon con = new JDBCcon();
        StringBuffer sb = new StringBuffer();
        boolean sv = true;
        String sql = "";
        int i = 0;
        String[][] rec = null;
        try {
        	sql = 
    			"select q.id,q.diancxxb_id,q.gongysmc,q.meikdwmc,q.pinz,\n" +
    			"       g.mingc as gongysb_id,\n" + 
    			"       m.mingc as meikxxb_id,\n" + 
    			"       p.mingc as pinzb_id,\n" + 
    			"       j.mingc as jihkjb_id\n" + 
    			"from qichjcsjppb q,gongysb g,pinzb p,meikxxb m,jihkjb j\n" + 
    			"where q.gongysb_id = g.id\n" + 
    			"      and q.meikxxb_id = m.id\n" + 
    			"      and q.pinzb_id = p.id\n" + 
    			"      and q.jihkjb_id = j.id\n" + 
    			"order by q.id";
        	sb.append(sql);
            ResultSet rs=con.getResultSet(sb.toString());
            ResultSetList rsl=this.getExtGrid().getModifyResultSet(this.getChange());
            while(rsl.next()){
            	while(rs.next()){
            		if(rsl.getString("DIANCXXB_ID").equals(rs.getString("DIANCXXB_ID"))&&rsl.getString("GONGYSMC").equals(rs.getString("GONGYSMC"))&&rsl.getString("MEIKDWMC").equals(rs.getString("MEIKDWMC"))&&rsl.getString("PINZ").equals(rs.getString("PINZ"))&&rsl.getString("GONGYSB_ID").equals(rs.getString("GONGYSB_ID"))&&rsl.getString("MEIKXXB_ID").equals(rs.getString("MEIKXXB_ID"))&&rsl.getString("PINZB_ID").equals(rs.getString("PINZB_ID"))&&rsl.getString("JIHKJB_ID").equals(rs.getString("JIHKJB_ID"))){
            			sv = false;
            		}
            	}
            }
            rs.close();
        }catch (Exception e) {
        	e.printStackTrace();
        }         
		con.Close();
        return sv;
	}
	
//	����������ȡֵ

	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String diancxxbid = "";
		if(((Visit) getPage().getVisit()).isFencb()){
			diancxxbid = ""+MainGlobal.getProperId(getFencbModel(),this.getChangb());
		}else{
			diancxxbid = ""+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		String sql = 
			"select q.id,q.diancxxb_id,q.gongysmc,q.meikdwmc,q.pinz,\n" +
			"       g.mingc as gongysb_id,\n" + 
			"       m.mingc as meikxxb_id,\n" + 
			"       p.mingc as pinzb_id,\n" + 
			"       j.mingc as jihkjb_id,\n" + 
			"       decode(q.zhuangt,1,'����','������') as zhuangt\n" + 
			"from qichjcsjppb q,gongysb g,pinzb p,meikxxb m,jihkjb j\n" + 
			"where q.gongysb_id = g.id\n" + 
			"      and q.meikxxb_id = m.id\n" + 
			"      and q.pinzb_id = p.id\n" + 
			"      and q.jihkjb_id = j.id\n" + 
			"	   and q.diancxxb_id="+diancxxbid+"\n" + 
			"order by q.id";
			
		
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight - 150");
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("qichjcsjppb");		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("id").setWidth(70);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("diancxxb_id").setDefaultValue(diancxxbid);
		egu.getColumn("gongysmc").setHeader("��Ӧ��");
		egu.getColumn("gongysmc").setWidth(95);
		egu.getColumn("gongysmc").setFixed(true);
		egu.getColumn("meikdwmc").setHeader("ú��λ");
		egu.getColumn("meikdwmc").setWidth(95);
		egu.getColumn("meikdwmc").setFixed(true);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setWidth(95);
		egu.getColumn("pinz").setFixed(true);
//		egu.getColumn("yunsdw").setHeader("���䵥λ");
//		egu.getColumn("yunsdw").setWidth(70);		
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setFixed(true);
		egu.getColumn("meikxxb_id").setHeader("ú��λ");
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setFixed(true);
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("pinzb_id").setWidth(75);
		egu.getColumn("pinzb_id").setFixed(true);
//		egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
//		egu.getColumn("yunsdwb_id").setWidth(70);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(75);
		egu.getColumn("jihkjb_id").setFixed(true);
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("zhuangt").setWidth(75);
		egu.getColumn("zhuangt").setFixed(true);

//		 ���ù�Ӧ��������
		ComboBox c1 = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c1);
		c1.setEditable(true);
		String gysSql = "select id,mingc from gongysb where leix=1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gysSql));
//		 ����ú��λ������
		ComboBox c2 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c2);
		c2.setEditable(true);
		String meikSql = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(meikSql));		
//		 ����Ʒ��������
		ComboBox c3 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c3);
		c3.setEditable(true);
		String pzSql = "select id,mingc from pinzb order by mingc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pzSql));
////		 �������䵥λ������
//		ComboBox c4 = new ComboBox();
//		egu.getColumn("yunsdwb_id").setEditor(c4);
//		c4.setEditable(true);
//		String ysdwSql = "select id,mingc from yunsdwb order by mingc";
//		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
//				new IDropDownModel(ysdwSql));
//		 ���üƻ��ھ�������
		ComboBox c5 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c5);
		c5.setEditable(true);
		String jihkjSql = "select id,mingc from jihkjb order by mingc";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
//		 ����״̬������
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "����"));
		ls.add(new IDropDownBean(0, "������"));
		ComboBox c6 = new ComboBox();
		egu.getColumn("zhuangt").setEditor(c6);
		c6.setEditable(true);
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(ls));
		egu.getColumn("zhuangt").setDefaultValue("������");
		egu.getColumn("zhuangt").returnId = true;
		
		egu.addToolbarItem("{"+new GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		if(((Visit) getPage().getVisit()).isFencb()){
			
			egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(FencbDropDown.getRawValue()=='��ѡ��'){Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��糧����'); return;}");
		}else{
			
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		}
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		//�߼����ж�ҳ�����Ƿ����ظ��ļ�¼
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton",
				"var flag=true;\n"
				+ " var rec=gridDiv_ds.getRange();\n"
				+ "for(var i=0;i<rec.length;i++){\n"
				+ "    for(var j=i+1;j<rec.length;j++){\n"
				+ "        if(rec[i].get('DIANCXXB_ID')==rec[j].get('DIANCXXB_ID')&&rec[i].get('GONGYSMC')==rec[j].get('GONGYSMC')&&rec[i].get('MEIKDWMC')==rec[j].get('MEIKDWMC')&&rec[i].get('PINZ')==rec[j].get('PINZ')&&rec[i].get('GONGYSB_ID')==rec[j].get('GONGYSB_ID')&&rec[i].get('MEIKXXB_ID')==rec[j].get('MEIKXXB_ID')&&rec[i].get('PINZB_ID')==rec[j].get('PINZB_ID')&&rec[i].get('JIHKJB_ID')==rec[j].get('JIHKJB_ID')){\n"
				+ "            Ext.MessageBox.alert('��ʾ��Ϣ','�����ظ����棡');\n"
				+ "            flag=false;\n"
				+ "            break;\n"
				+ "        }\n"
				+ "    }\n"
				+ "}\n"
				+ "if(!flag){	\n"
				+ "		return;	\n"
				+ "}	\n");
				
		
		if(((Visit) getPage().getVisit()).isFencb()){
			egu.addTbarText("-");
			egu.addTbarText("����:");
			ComboBox comb5 = new ComboBox();
			comb5.setTransform("FencbDropDown");
			comb5.setId("FencbDropDown");
			comb5.setEditable(false);
			comb5.setLazyRender(true);// ��̬��
			comb5.setWidth(135);
			comb5.setReadOnly(true);
			egu.addToolbarItem(comb5.getScript());
		}
		
		
		StringBuffer sb = new StringBuffer();
		String Headers = 

			"		 [\n" +
			"         {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n" + 
			"         {header:'ID', align:'center',rowspan:2},\n" + 
			"         {header:'DIANCXXB_ID', align:'center',rowspan:2},\n" + 
			"         {header:'������', colspan:3},\n" + 
			"         {header:'ϵͳ��Ϣ', colspan:5}\n" + 
			"        ],\n" + 
			"        [\n" + 
			"	      {header:'<table><tr><td width=80 align=center style=border:0>��Ӧ��</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>ú��λ</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>Ʒ��</td></tr></table>', align:'center'},\n" + 
			"	      {header:'<table><tr><td width=70 align=center style=border:0>��Ӧ��</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=70 align=center style=border:0>ú��λ</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>Ʒ��</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>�ƻ��ھ�</td></tr></table>'},\n" + 
			"         {header:'<table><tr><td width=55 align=center style=border:0>״̬</td></tr></table>'}\n" + 
			"        ]";

		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString3("");	//��
			visit.setList1(null);
			setFencbValue(null);	//5
			setFencbModel(null);
			getFencbModels();		//5
			getSelectData();
		}
	}
	
//	����
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql,"��ѡ��"));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
}
