package com.zhiren.dc.jilgl.gongl.shujdr;


import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;


import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.jilgl.Jilcz;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-15 16��31
 * �������޸�һ���������������ƥ�䲻����������
 */
public class Shujpp extends BasePage implements PageValidateListener {
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


	
	
	
	public void Save() 
	{
		//--------------------------------
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		
		StringBuffer sql = new StringBuffer("begin \n");
//		ResultSetList rsl1 = con.getResultSetList("select rownum , a.* from (select gongysmc,meikdwmc,pinz,faz,daoz,jihkj,yunsfs " +
//				"from chepbtmp where fahb_id =0 " + Jilcz.filterDcid(visit, "") +
//				"group by gongysmc,meikdwmc,pinz,faz,daoz,jihkj,yunsfs) a");
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(rsl.next()){
			//ͨ��rownum�ж�������
			int row = rsl.getInt("rownum");
			//ͨ��getExtGrid().griddata[]���������
			String oldData[] = getExtGrid().griddata[row-1];
			String oldgongsymc = oldData[1];
			String oldmeikdwmc = oldData[2];
			String oldpinz = oldData[3];
			String oldfaz = oldData[4];
			String olddaoz = oldData[5];
			String oldjihkj = oldData[6];
			String oldyunsfs = oldData[7];
			sql.append("update chepbtmp set gongysmc='"
					 + rsl.getString("gongysmc")+"',meikdwmc ='"
					 + rsl.getString("meikdwmc")+"',pinz='" 
					 + rsl.getString("pinz")+ "',faz='"
					 + rsl.getString("faz")+"',daoz='"
					 + rsl.getString("daoz")+"',jihkj='"
					 + rsl.getString("jihkj")+"',yunsfs='"
					 + rsl.getString("yunsfs")+"' " +
					 "  where gongysmc = '" + oldgongsymc + 
					 "' and meikdwmc='" + oldmeikdwmc +
					 "' and pinz='" + oldpinz +
					 "' and faz='" + oldfaz + 
					 "' and daoz='" + olddaoz +
					 "' and jihkj='" + oldjihkj + 
					 "' and yunsfs='" + oldyunsfs +
					 "' "  + Jilcz.filterDcid(visit, "") +
					 "  and fahb_id =0 ;\n");

		}
		 

		sql.append("end;");
		con.getUpdate(sql.toString());


		
		con.Close();
		setMsg("����ɹ�!");
	
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
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList rsl = con.getResultSetList("select rownum,a.* from (select gongysmc,meikdwmc,pinz,faz,daoz,jihkj,yunsfs " +
				"from chepbtmp where fahb_id =0 " + Jilcz.filterDcid(visit, "") +
				" group by gongysmc,meikdwmc,pinz,faz,daoz,jihkj,yunsfs) a");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("chepbtmp");
		egu.getColumn("rownum").setHidden(true);
		egu.getColumn("gongysmc").setHeader("��Ӧ��");
		egu.getColumn("meikdwmc").setHeader("ú������");
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("daoz").setHeader("��վ");
		egu.getColumn("jihkj").setHeader("�ƻ��ھ�");
		egu.getColumn("yunsfs").setHeader("���䷽ʽ");
		
		egu.getColumn("gongysmc").setWidth(140);
		egu.getColumn("meikdwmc").setWidth(140);
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("faz").setWidth(80);
		egu.getColumn("daoz").setWidth(80);
		
		
		ComboBox gys = new ComboBox();
		gys.setEditable(true);
		egu.getColumn("gongysmc").setEditor(gys);
		egu.getColumn("gongysmc").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from gongysb where leix=1 and zhuangt=1"));
		
		ComboBox mk = new ComboBox();
		mk.setEditable(true);
		egu.getColumn("meikdwmc").setEditor(mk);
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from meikxxb"));
		
		ComboBox pz = new ComboBox();
		pz.setEditable(true);
		egu.getColumn("pinz").setEditor(pz);
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_Pinz_mei));
		
		ComboBox fz = new ComboBox();
		fz.setEditable(true);
		egu.getColumn("faz").setEditor(fz);
		egu.getColumn("faz").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from chezxxb"));
		
		ComboBox dz = new ComboBox();
		dz.setEditable(true);
		egu.getColumn("daoz").setEditor(dz);
		egu.getColumn("daoz").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from chezxxb"));
		
		ComboBox jhkj = new ComboBox();
		jhkj.setEditable(true);
		egu.getColumn("jihkj").setEditor(jhkj);
		egu.getColumn("jihkj").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_Kouj));
		
		ComboBox ysfs = new ComboBox();
		ysfs.setEditable(true);
		egu.getColumn("yunsfs").setEditor(ysfs);
		egu.getColumn("yunsfs").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from yunsfsb"));

		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
