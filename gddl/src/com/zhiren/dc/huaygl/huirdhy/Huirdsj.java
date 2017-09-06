package com.zhiren.dc.huaygl.huirdhy;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huirdsj extends BasePage implements PageValidateListener {
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		// ��Ӧ������
		long gongysID = this.getMeikdqmcValue().getId();
		// ��Ŀ��������
		long xiangmmcID = this.getYuansxmmcValue().getId();
		
		ResultSetList rsl = con
				.getResultSetList(
						"select ysf.id,\n" +
						"       ysf.diancxxb_id,\n" + 
						"       mk.mingc as meikxxb_id,\n" + 
						"       mz.mingc as meizb_id,\n" + 
						"       ysf.riq,\n" + 
						"       ysx.mingc as fenxxmb_id,\n" + 
						"       ysf.zhi,\n" + 
						"       ysf.zhuangt\n" + 
						"  from huirdfxb ysf,\n" + 
						"       diancxxb dc,\n" + 
						"       (select mk.id as id, mk.mingc as mingc\n" + 
						"          from meikxxb mk, gongysmkglb gm, gongysb gy\n" + 
						"         where gm.meikxxb_id(+) = mk.id\n" + 
						"           and gm.gongysb_id = gy.id(+)\n" + 
						"           and gy.id = " + gongysID + ") mk,\n" + 
						"       meizb mz,\n" + 
						"       yunsfsb ys,\n" + 
						"       (select * from fenxxmb where id = " + xiangmmcID + " and leib = '���۵�') ysx\n" + 
						" where ysf.diancxxb_id = dc.id\n" + 
						"   and ysf.meikxxb_id = mk.id\n" + 
						"   and ysf.meizb_id = mz.id\n" + 
						"   and ysf.fenxxmb_id = ysx.id\n" + 
						"   and to_char(ysf.riq, 'yyyy') = '" + intyear + 
						" 'order by ysf.id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("huirdfxb");
		// /������ʾ������
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("meikxxb_id").setHeader("ú������");
		egu.getColumn("fenxxmb_id").setHeader("��Ŀ����");
		egu.getColumn("meizb_id").setHeader("ú��");
		egu.getColumn("zhi").setHeader("ֵ");
		egu.getColumn("riq").setHeader("��������");

		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setDefaultValue("1");
		egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		egu.getColumn("diancxxb_id").setEditor(null);
		// //�����п��
		// egu.getColumn("caizhdyb_id").setWidth(200);
		// //���õ�ǰ���Ƿ�༭
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// /��̬������
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu
				.getColumn("meikxxb_id")
				.setComboEditor(
						egu.gridId,
						new IDropDownModel(
								"select mk.id as id,mk.mingc as mingc from meikxxb mk,gongysmkglb gm,gongysb gy "
										+ "where gm.meikxxb_id(+)=mk.id and gm.gongysb_id=gy.id(+) and gy.id="
										+ gongysID));
		egu.getColumn("fenxxmb_id").setEditor(new ComboBox());
		egu.getColumn("fenxxmb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc  from fenxxmb"));
		// /����������Ĭ��ֵ
		egu.getColumn("fenxxmb_id").setDefaultValue(getYuansxmmcValue().getValue());
		
		egu.getColumn("meizb_id").setEditor(new ComboBox());
		egu.getColumn("meizb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meizb"));

		// /�Ƿ񷵻��������ֵ��ID
// egu.getColumn("meikxxb_id").setReturnId(true);
// egu.getColumn("meizb_id").setReturnId(true);
	
		// ********************������************************************************
		// ��ҳ���ϵ��������
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");

		egu.addTbarText("��Ӧ��:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("MeikmcDropDown");
		comb2.setId("gongys");
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(130);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");

		egu.addTbarText("��Ŀ����:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("YuansxmmcDropDown");
		comb3.setId("yuansxm");
		comb3.setLazyRender(true);// ��̬��
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());
// ѭ���趨�еĿ��,���趨С��λ��

		((NumberField)egu.getColumn("zhi").editor).setDecimalPrecision(3);
		
	
		// �趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});gongys.on('select',function(){document.forms[0].submit();});yuansxm.on('select',function(){document.forms[0].submit();});");
		// /���ð�ť
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
// egu.addToolbarItem("{"+new
// GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/Yuansfxreport&lx='+NIANF.getValue()+'&lx='+gongys.getValue()+'&lx='+yuansxm.getValue();" +
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
			this.setNianfValue(null);
			this.getNianfModels();
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			setYuansxmmcValue(null);
			getIYuansxmmcModels();

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
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// ��Ŀ����
	public boolean _Yuansxmmcchange = false;

	private IDropDownBean _YuansxmmcValue;

	public IDropDownBean getYuansxmmcValue() {
		if (_YuansxmmcValue == null) {
			_YuansxmmcValue = (IDropDownBean) getIYuansxmmcModels()
					.getOption(0);
		}
		return _YuansxmmcValue;
	}

	public void setYuansxmmcValue(IDropDownBean Value) {
		long id = -2;
		if (_YuansxmmcValue != null) {
			id = _YuansxmmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Yuansxmmcchange = true;
			} else {
				_Yuansxmmcchange = false;
			}
		}
		_YuansxmmcValue = Value;
	}

	private IPropertySelectionModel _IYuansxmmcModel;

	public void setIYuansxmmcModel(IPropertySelectionModel value) {
		_IYuansxmmcModel = value;
	}

	public IPropertySelectionModel getIYuansxmmcModel() {
		if (_IYuansxmmcModel == null) {
			getIYuansxmmcModels();
		}
		return _IYuansxmmcModel;
	}

	public IPropertySelectionModel getIYuansxmmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc  from fenxxmb order by mingc";
			_IYuansxmmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IYuansxmmcModel;
	}

	// �������
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
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

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
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
}
