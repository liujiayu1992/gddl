package com.zhiren.dc.huaygl.kuangfhy;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kuangfhyb extends BasePage implements PageValidateListener {

	public void ExtGridUtil() {
	}

	public String OratypeOfExt(String oratype) {
		if ("NUMBER".equals(oratype)) {
			return "float";
		}
		if ("DATE".equals(oratype)) {
			return "date";
		}
		// if(oratype.equals(anObject)) {

		// }
		return "string";
	}

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

	// private boolean _RefurbishChick = false;
	// public void RefurbishButton(IRequestCycle cycle) {
	// _RefurbishChick = true;
	// }
	// private void Refurbish() {
	// //Ϊ "ˢ��" ��ť��Ӵ������
	// getSelectData();
	// }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		// if(_RefurbishChick){
		// _RefurbishChick = false;
		// Refurbish();
		// }
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();

		// ��Ӧ������
		long gongysID = this.getMeikdqmcValue().getId();

		long meikID = this.getMeikValue().getId();

		ResultSetList rsl = con
				.getResultSetList("select distinct k.id,g.mingc as gongysb_id,m.mingc as meikxxb_id, "
						+ " k.qnet_ar as qnet_ar,k.aar as aar,k.ad as ad,k.vdaf as vdaf,  "
						+ "k.mt as mt,k.stad as stad,k.aad as aad,k.mad as mad,k.qbad as qbad,  "
						+ "k.had,k.vad,k.fcad,k.std,k.qbrad,k.hdaf,k.qgrad_daf,k.sdaf,  "
						+ "k.var as var,k.t1 as t1,k.t2 as t2,k.t3 as t3,k.t4 as t4,  "
						+ "m.leib as leib,k.lury as lury"
						+ " from kuangfzlb k,gongysb g,meikxxb m,zhilb z,fahb f"
						+ " where k.gongysb_id=g.id   and g.id = f.gongysb_id  and  k.meikxxb_id=m.id "
						+ " and m.id = " + meikID + "  and g.id = " + gongysID);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("kuangfzlb");
		egu.setWidth(1000);
		// /������ʾ������
		// egu.clicksToEdit;
		// egu.defaultsortable
		// egu.frame
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setHeader("ú������");
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("qnet_ar").setHeader("QNET_AR(Mj/kg)");
		egu.getColumn("qnet_ar").setWidth(100);
		egu.getColumn("aar").setHeader("AAR(%)");
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("ad").setHeader("AD(%)");
		egu.getColumn("ad").setWidth(60);
		egu.getColumn("vdaf").setHeader("VDAF(%)");
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("mt").setHeader("MT(%)");
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("stad").setHeader("STAD(%)");
		egu.getColumn("stad").setWidth(60);
		egu.getColumn("aad").setHeader("AAD(%)");
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("mad").setHeader("MAD(%)");
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("qbad").setHeader("QBAD(Mj/kg)");
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setHeader("HAD(%)");
		egu.getColumn("had").setWidth(60);
		egu.getColumn("vad").setHeader("VAD(%)");
		egu.getColumn("vad").setWidth(60);
		egu.getColumn("fcad").setHeader("FCAD(%)");
		egu.getColumn("fcad").setWidth(60);
		egu.getColumn("std").setHeader("STD(%)");
		egu.getColumn("std").setWidth(60);
		egu.getColumn("qbrad").setHeader("QBRAD(Mj/kg)");
		egu.getColumn("qbrad").setWidth(80);
		egu.getColumn("hdaf").setHeader("HDAF(%)");
		egu.getColumn("hdaf").setWidth(60);
		egu.getColumn("qgrad_daf").setHeader("QGRAD_DAF(Mj/kg)");
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setHeader("SDAF(%)");
		egu.getColumn("sdaf").setWidth(60);
		egu.getColumn("var").setHeader("VAR(%)");
		egu.getColumn("var").setWidth(60);
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t1").setWidth(60);
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t2").setWidth(60);
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t3").setWidth(60);
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("t4").setWidth(60);
		egu.getColumn("leib").setHeader("���");
		egu.getColumn("leib").setWidth(60);
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setWidth(60);

		// egu.getColumn("zhuangt").setDefaultValue("1");
		// egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		// egu.getColumn("diancxxb_id").setEditor(null);
		// //�����п��
		// egu.getColumn("caizhdyb_id").setWidth(200);
		// //���õ�ǰ���Ƿ�༭
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// /��̬������
		// egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		// egu
		// .getColumn("meikxxb_id")
		// .setComboEditor(
		// egu.gridId,
		// new IDropDownModel(
		// "select mk.id as id,mk.mingc as mingc from meikxxb mk,gongysmkglb
		// gm,gongysb gy "
		// + "where gm.meikxxb_id(+)=mk.id and gm.gongysb_id=gy.id(+) and
		// gy.id="
		// + gongysID));

		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from gongysb"));
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from meikxxb"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId,
				new IDropDownModel("select id,leib from meikxxb"));
		// egu.getColumn("lury").setEditor(new ComboBox());
		// egu.getColumn("lury").setComboEditor(egu.gridId,
		// new IDropDownModel("select bumb_id,"))
		//				
		// /����������Ĭ��ֵ
		// egu.getColumn("yuansxmb_id").setDefaultValue(getYuansxmmcValue().getValue());
		//		
		// egu.getColumn("meizb_id").setEditor(new ComboBox());
		// egu.getColumn("meizb_id").setComboEditor(egu.gridId,
		// new IDropDownModel("select id, mingc from meizb"));

		// /�Ƿ񷵻��������ֵ��ID
		// egu.getColumn("meikxxb_id").setReturnId(true);
		// egu.getColumn("yunsfsb_id").setReturnId(true);
		// egu.getColumn("yuansxmb_id").setReturnId(true);
		// egu.getColumn("meizb_id").setReturnId(true);

		// ********************������************************************************

		// egu.addTbarText("��������:");
		// DateField df = new DateField();
		// df.setValue(this.getRiqi());
		// df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��

		// egu.addToolbarItem(df.getScript());
		// egu.addTbarText("-");
		egu.addTbarText("��Ӧ��:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("MeikmcDropDown");
		comb1.setId("gongys");
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(130);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		egu.addTbarText("ú��:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("MeikDropDown");
		comb2.setId("meik");
		comb2.setLazyRender(true);
		comb2.setWidth(130);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");

		// egu.addTbarText("��Ŀ����:");
		// ComboBox comb3 = new ComboBox();
		// comb3.setTransform("YuansxmmcDropDown");
		// comb3.setId("yuansxm");
		// comb3.setLazyRender(true);// ��̬��
		// comb3.setWidth(130);
		// egu.addToolbarItem(comb3.getScript());
		// ѭ���趨�еĿ��,���趨С��λ��

		// �趨�������������Զ�ˢ��
		egu
				.addOtherScript("gongys.on('select',function(){document.forms[0].submit();});meik.on('select',function(){document.forms[0].submit();});");

		// ((NumberField)egu.getColumn("zhi").editor).setDecimalPrecision(3);
		// ������

		// egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		// egu.addToolbarItem("{"+new
		// GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		// egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		// egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/Kuangfhybreport&lx='+gongys.getValue().getId()+'&lx='+meik.getValue().getId();"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + str + "}").getScript()
				+ "}");
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
		}
		getSelectData();
	}

	// ��Ӧ��/���
	public boolean _meikdqmcchange = false;

	public boolean _meikchange = false;

	private IDropDownBean _MeikdqmcValue;

	private IDropDownBean _MeikValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public IDropDownBean getMeikValue() {
		if (_MeikValue == null) {
			_MeikValue = (IDropDownBean) getIMeikModels().getOption(0);
		}
		return _MeikValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	public void setMeikValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikValue != null) {
			id = _MeikValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikchange = true;
			} else {
				_meikchange = false;
			}
		}
		_MeikValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	private IPropertySelectionModel _IMeikModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public void setIMeikModel(IPropertySelectionModel value) {
		_IMeikModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikModel() {
		if (_IMeikModel == null) {
			getIMeikModels();
		}
		return _IMeikModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from gongysb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from meikxxb order by mingc";
			_IMeikModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikModel;
	}

}
