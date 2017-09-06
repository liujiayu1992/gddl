package com.zhiren.dc.monthReport.gd;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import sun.jdbc.odbc.JdbcOdbc;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public abstract class Shoufc_xb extends BasePage {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
	}
	
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}
	
	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
	
//	 ���������
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
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

	// �·�������
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} 
			else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
	

private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String selectSql = "";
		
		
		selectSql += 
			"select decode(shoufcyllbb_id,0,'���ú��',1,'�ݹ�ú��',2,'�ݹ����ú��') as shoufcyllbb_id,\n" +
			"       qickc_shul,\n" + 
			"       qickc_danj,\n" + 
			"       qickc_jine,\n" + 
			"       benyrk_shul,\n" + 
			"       benyrk_danj,\n" + 
			"       benyrk_yunfdj,\n" + 
			"       benyrk_jine,\n" + 
			"       benyrk_yunfje,\n" + 
			"       benyhy_shul,\n" + 
			"       benyhy_danj,\n" + 
			"       benyhy_yunfdj,\n" + 
			"       benyhy_jine,\n" + 
			"       benyhy_yunfje,\n" + 
			"       shuifctzl_shul,\n" + 
			"       shuifctzl_jine,\n" + 
			"       pandks_shul,\n" + 
			"       pandks_jine,\n" + 
			"       yuemyekc_shul,\n" + 
			"       yuemyekc_danj,\n" + 
			"       yuemyekc_jine\n" + 
			"  from shoufctbb\n" + 
			"where to_char(riq,'yyyy-mm')='"+getNianf()+"-"+getYuef()+"'\n" +
			"union all\n" + 
			"select '�ܼ�' as shoufcyllbb_id,\n" + 
			"       sum(qickc_shul),\n" + 
			"       round(decode(sum(qickc_shul),0,0,sum(qickc_jine)/sum(qickc_shul)),2) as qickc_danj,\n" + 
			"       sum(qickc_jine),\n" + 
			"       sum(benyrk_shul),\n" + 
			"       round(decode(sum(benyrk_shul),0,0,sum(benyrk_jine)/sum(benyrk_shul)),2) as benyrk_danj ,\n" + 
			"       round(decode(sum(benyrk_shul),0,0,sum(benyrk_yunfje)/sum(benyrk_shul)),2) as benyrk_yunfdj ,\n" + 
			"       sum(benyrk_jine),\n" + 
			"       sum(benyrk_yunfje),\n" + 
			"       sum(benyhy_shul),\n" + 
			"       round(decode(sum(benyhy_shul),0,0,sum(benyhy_jine)/sum(benyhy_shul)),2) as benyhy_danj ,\n" + 
			"       round(decode(sum(benyhy_shul),0,0,sum(benyhy_yunfje)/sum(benyhy_shul)),2) as benyhy_yunfdj ,\n" + 
			"       sum(benyhy_jine),\n" + 
			"       sum(benyhy_yunfje),\n" + 
			"       sum(shuifctzl_shul),\n" + 
			"       sum(shuifctzl_jine),\n" + 
			"       sum(pandks_shul),\n" + 
			"       sum(pandks_jine),\n" + 
			"       sum(yuemyekc_shul),\n" + 
			"       round(decode(sum(yuemyekc_shul),0,0,sum(yuemyekc_jine)/sum(yuemyekc_shul)),2) as yuemyekc_danj ,\n" + 
			"       sum(yuemyekc_jine)\n" + 
			"  from shoufctbb\n" + 
			"where to_char(riq,'yyyy-mm')='"+getNianf()+"-"+getYuef()+"'"+"order by shoufcyllbb_id";


		String[][] ArrHeader = new String[2][21];
		
		ArrHeader[0] = new String[]{"����","�ڳ����","�ڳ����","�ڳ����","�������","�������","�������","�������","�������","���º���","���º���","���º���","���º���","���º���","ˮ�ֲ������","ˮ�ֲ������","�̵����","�̵����","��ĩ�����","��ĩ�����","��ĩ�����"};
		ArrHeader[1] = new String[]{"����","�������֣�","����","���","�������֣�","ú���","�˷ѵ���","ú����","�˷ѽ��","�������֣�","ú���","�˷ѵ���","ú����","�˷ѽ��","�������֣�","���","�������֣�","���","�������֣�","����","���"};

		int[] ArrWidth = new int[] {80,70,70,80,70,70,70,70,80,70,70,70,70,80,70,80,70,80,70,70,80};
		
		ResultSetList rslData =  con.getResultSetList(selectSql);
		
		rt.setTitle(getNianfValue()+"��"+getYuefValue()+"��ú�ա������汨��", ArrWidth);
		rt.setBody(new Table(rslData, 2, 0, 4));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);

		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		for(int i = 4; i< rt.body.getRows() ; i++){
			rt.body.merge(i, 4, i, 5);
		}
		rt.body.ShowZero = true;
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(20, Table.ALIGN_RIGHT);
		rt.body.setColAlign(21, Table.ALIGN_RIGHT);
		
		rt.setDefaultTitle(1, 4, "�����ţ�"+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);

//		ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2, 1, "���ܾ���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 1, "���񸴺�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "�ƻ�������:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 1, "ȼ�ϲ�����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(19, 1, "ͳ��Ա:", Table.ALIGN_LEFT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();

		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
	
		
		ToolbarButton tbrtn = new ToolbarButton(null, "��ѯ", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tbrtn);
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
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
		}
		setRiq();
		getSelectData();
	}
}