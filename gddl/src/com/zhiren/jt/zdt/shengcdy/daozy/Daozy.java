package com.zhiren.jt.zdt.shengcdy.daozy;



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
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Daozy extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	���ڿؼ�
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else {
			return value;
		}
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
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
	}

	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strd="";
		String jib="select jib from diancxxb where id="+visit.getDiancxxb_id();
		ResultSet rs1 = con.getResultSet(jib);
		long diancjb=0;
		try {
			if(rs1.next()){
				diancjb=rs1.getLong("jib");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		if(diancjb==1){
			strd="";
		}else if(diancjb==2){
			strd="where dc.fuid="+visit.getDiancxxb_id();
		}else if(diancjb==3){
			strd="where dc.id="+visit.getDiancxxb_id();
		}
		
		
		ResultSetList rsl = con
				.getResultSetList(" select t.id,dc.mingc as diancxxb_id, t.riq as riq,cz.mingc as chezxxb_id,t.banjh,t.ches,t.beiz" +
						"			 from tielyb t,diancxxb dc,chezxxb cz where t.diancxxb_id=dc.id and t.chezxxb_id=cz.id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("tielyb");
		// /������ʾ������
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select dc.id, dc.mingc from diancxxb dc "+strd));
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		egu.getColumn("chezxxb_id").setHeader("��վ����");
		egu.getColumn("chezxxb_id").setEditor(new ComboBox());
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("banjh").setHeader("��ƻ�");
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("beiz").setHeader("��ע");	
//		// //�����п��
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("chezxxb_id").setWidth(90);
		egu.getColumn("banjh").setWidth(90);
		egu.getColumn("ches").setWidth(90);
		egu.getColumn("beiz").setWidth(90);
		
//		// //���õ�ǰ���Ƿ�༭
//		egu.getColumn("piny").setEditor(null);
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// /���ð�ť
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Bumreport&lx=rezc';" +
   	    " window.open(url,'newWin');";
	egu.addToolbarItem("{"+new GridButton("��ӡ","function (){"+str+"}").getScript()+"}");
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
			visit.setList1(null);
			getSelectData();
		}
	}
}
