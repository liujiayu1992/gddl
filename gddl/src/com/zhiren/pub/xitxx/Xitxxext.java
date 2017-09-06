package com.zhiren.pub.xitxx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xitxxext extends BasePage implements PageValidateListener {
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
		ResultSetList rsl = con
				.getResultSetList(
						"select id,\n" +
						"        xuh,\n" + 
						"        diancxxb_id,\n" + 
						"        mingc,\n" + 
						"        zhi,\n" + 
						"        danw,\n" + 
						"        leib,\n" + 
						"        decode(zhuangt, 1, '�ɱ༭', '���ɱ༭') as zhuangt,\n" + 
						"        beiz\n" + 
						"   from xitxxb\n" +
						"   where diancxxb_id ="+visit.getDiancxxb_id()+" and zhuangt = 1\n"+
						"  order by leib, xuh, mingc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("xitxxb");
		egu.getColumn("xuh").setHeader("���");
		
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("zhi").setHeader("ֵ");
		egu.getColumn("danw").setHeader("��λ");
		egu.getColumn("leib").setHeader("���");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("diancxxb_id").setHeader("����");
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(
						"select id,mingc from diancxxb where id=" +visit.getDiancxxb_id() + " or fuid="+visit.getDiancxxb_id()));
		if(!visit.isFencb()){
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		egu.getColumn("leib").setEditor(new ComboBox());
		egu
				.getColumn("leib")
				.setComboEditor(
						egu.gridId,
						new IDropDownModel(
								"select *\n" +
								"  from (select 1 as id, decode(1, 1, '����') as mingc\n" + 
								"          from dual\n" + 
								"        union\n" + 
								"        select 2 as id, decode(1, 1, '����') as mingc\n" + 
								"          from dual\n" + 
								"        union\n" + 
								"        select 3 as id, decode(1, 1, '����') as mingc" +
								"          from dual\n"+
								"        union\n" + 
								"        select 4 as id, decode(1, 1, '����') as mingc" +
								"          from dual\n"+
								"        union\n" + 
								"        select 5 as id, decode(1, 1, '��¯') as mingc" +
								"          from dual\n"+
								"        union\n" + 
								"        select 6 as id, decode(1, 1, '�̵�') as mingc" +
								"          from dual)"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu
				.getColumn("zhuangt")
				.setComboEditor(
						egu.gridId,
						new IDropDownModel(

								"select *\n" +
								"  from (select 0 as id, decode(1, 1, '���ɱ༭') as mingc\n" + 
								"          from dual\n" + 
								"        union\n" + 
								"        select 1 as id, decode(1, 1, '�ɱ༭') as mingc from dual)"));
		egu.getColumn("zhuangt").setDefaultValue("�ɱ༭");
		egu.getColumn("leib").setDefaultValue("");
		egu.getColumn("leib").returnId=false;
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Xitxxreport&lx=rezc';" +
   	    " window.open(url,'newWin');";
	egu.addToolbarItem("{"+new GridButton("��ӡ","function (){"+str+"}",SysConstant.Btn_Icon_Print).getScript()+"}");
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
