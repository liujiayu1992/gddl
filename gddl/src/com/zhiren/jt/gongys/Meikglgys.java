package com.zhiren.jt.gongys;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * ú�������Ӧ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-20
 * ������ʹ��String2��Ϊ����ҳ��Ĳ���
 */
public class Meikglgys extends BasePage implements PageValidateListener {
	
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
	
	private String Meik_id; // ������ϸ�ҳ�洫������ú��id

	public String getMeikid() {
		return Meik_id;
	}

	public void setMeikid(String id) {
		this.Meik_id = id;
	}
	
	private String Meikmc; // ������ϸ�ҳ�洫������ú������
	
	public String getMeikmc() {
		return Meikmc;
	}

	public void setMeikmc(String meikmc) {
		Meikmc = meikmc;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	"����"��ť
	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			Visit visit = (Visit) this.getPage().getVisit();
			if(visit.getString2()!=null && visit.getString2().equals("Meikxx_gd")){
				cycle.activate(visit.getString2());
			}else{
				cycle.activate("Meikxx");
			}
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from gongysmkglb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into gongysmkglb(id, gongysb_id, meikxxb_id) values(getnewid(").append(visit.getDiancxxb_id())
					.append("), ").append((getExtGrid().getColumn("gongysb_id").combo)
						.getBeanId(getPinyMingc(con, "gongysb", mdrsl.getString("gongysb_id")))).append(", ")
					.append(getMeikid()).append(");\n");
			} else {
				sbsql.append("update gongysmkglb set ")
				.append(" gongysb_id = ").append((getExtGrid().getColumn("gongysb_id").combo)
					.getBeanId(getPinyMingc(con, "gongysb", mdrsl.getString("gongysb_id"))))
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
			"select gmgl.id, mk.mingc meikxxb_id, gys.mingc gongysb_id\n" +
			"  from gongysmkglb gmgl, meikxxb mk, gongysdcglb gdgl, gongysb gys\n" + 
			" where gmgl.gongysb_id = gdgl.gongysb_id\n" + 
			"   and gmgl.meikxxb_id = mk.id\n" + 
			"   and gdgl.gongysb_id = gys.id\n" + 
			"   and mk.id = " + getMeikid() + "\n order by mk.mingc";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("meikxxb_id").setHeader("ú��λ");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setDefaultValue(getMeikmc());
		egu.getColumn("meikxxb_id").setWidth(130);
		egu.getColumn("gongysb_id").setHeader("������λ");
		egu.getColumn("gongysb_id").setWidth(150);
		
		String strSql = 
			"select gys.id, gys.mingc as mingc\n" +
			"  from gongysdcglb gdgl, diancxxb dc, gongysb gys\n" + 
			" where gdgl.diancxxb_id = dc.id\n" + 
			"   and gdgl.gongysb_id = gys.id\n" + 
			"   and gdgl.diancxxb_id = "+ ((Visit) getPage().getVisit()).getDiancxxb_id() +"\n" + 
		    "   and gys.id not in (select gongysb_id from gongysmkglb where meikxxb_id = "+ getMeikid() +") and gys.leix=1 and gys.zhuangt=1 order by gys.piny, gys.mingc";
		ComboBox gys_com = new ComboBox();
		gys_com.setEditable(true);
		gys_com.setListeners("beforequery:function(e){" +
                "var combo = e.combo;" +
                "if(!e.forceAll){" +
                "var value = e.query;" +
                "combo.store.filterBy(function(record,id){" +
                "var text = record.get(combo.displayField);" +
                "return (text.indexOf(value)!=-1);" +
                "});" +
                "combo.expand();" +
                "return false; " +
                " } " +
                "}");

		egu.getColumn("gongysb_id").setEditor(gys_com);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(strSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		
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
		egu.addTbarBtn(new GridButton("����","function(){document.all.ReturnButton.click();}",SysConstant.Btn_Icon_Return));
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * @param con
	 * @param tableName 
	 * @param mingc			
	 * @return ����"ƴ��-����"��ʽ���ַ���
	 */
	public String getPinyMingc(JDBCcon con, String tableName, String mingc){
		
		ResultSetList rsl = con.getResultSetList("select piny ||'-'|| mingc as pmingc from "+ tableName +" where mingc ='"+ mingc + "'");
		if(rsl.next()){
			return rsl.getString("pmingc");
		}else{
			return mingc;
		}
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setMeikid(visit.getString9()); // ����ǰ��ҳ�洫������ú��id���浽Meik_id�����С�
			setMeikmc(visit.getString10()); // ����ǰ��ҳ�洫������ú�����Ʊ��浽Meikmc�����С�
		}
		getSelectData();
	}
}