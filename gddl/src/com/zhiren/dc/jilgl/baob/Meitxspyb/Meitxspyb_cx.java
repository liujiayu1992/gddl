package com.zhiren.dc.jilgl.baob.Meitxspyb;

import java.util.Date;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*����:���ܱ�
 *����:2010-4-25 15:40:27
 *����:ú̿���±���ѯ
 */


public class Meitxspyb_cx extends BasePage implements PageValidateListener {
	
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
	
//	���������_��ʼ
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	���������_����
	
//	�·�������_��ʼ
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	�·�������_����
	
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
		
		String sql = 

			"select m.fadl,m.gouml,m.leijgml,m.haoml,m.yueckc,m.yuemkc,m.meih,m.beiz,\n" +
			" m.fuzr,m.tianbr\n"+
			"from meitxspybb m\n" + 
			"where m.riq=to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd')";

		ResultSetList rslData = con.getResultSetList(sql);
		double fadl=0.0d;
		double gouml=0.0d;
		double leijgml=0.0d;
		long haoml=0;
		long yueckc=0;
		long yuemkc=0;
		long meih=0;
		String beiz="";
		String tianbr="";
		String fuzr="";
		while(rslData.next()){
			fadl=rslData.getDouble("fadl");
			gouml=rslData.getDouble("gouml");
			leijgml=rslData.getDouble("leijgml");
			haoml=rslData.getLong("haoml");
			yueckc=rslData.getLong("yueckc");
			yuemkc=rslData.getLong("yuemkc");
			meih=rslData.getLong("meih");
			beiz=rslData.getString("beiz");
			tianbr=rslData.getString("tianbr");
			fuzr=rslData.getString("fuzr");
		}
		
		String[][] ArrHeader = new String[7][8];
		ArrHeader[0] = new String[]{"���²�Ʒ����", "���¹�ú��", "�ۼƹ�ú��", "���º�ú��", "�³����", "��ĩ���", 
			"��Ʒת��ϵ��", "��ע"};
		ArrHeader[1] = new String[]{fadl+"�ڶ�", gouml+"",leijgml+"",haoml+"",yueckc+"",yuemkc+"",meih+"",beiz};
		ArrHeader[2] = new String[]{"", "","","","","","",""};
		ArrHeader[3] = new String[]{"", "","","","","","",""};
		ArrHeader[4] = new String[]{"", "","","","","","",""};
		ArrHeader[5] = new String[]{"", "","","","","","",""};
		ArrHeader[6] = new String[]{"���˵����1���ն���ҵҪ������ʵ�ʹ�ú����ʵ��д��<br> 2.�˱���5��ǰ��ͬú̿����Ʊ���ձ���ͻ�����һ���������أ��С�����ú̿���������", "","","","","","",""};
		int[] ArrWidth = new int[] {100, 100, 110, 100, 100, 100,100,100};
		
		Table tb = new Table(7, 8);
		

		
		rt.setTitle("�������ն���ú��ҵ�±���", ArrWidth);
		
		
		
		tb.mergeCell(7, 1, 7, 8);
		
		tb.setRowHeight(40);
		tb.setFontSize(12);
		tb.setWidth(ArrWidth);
		tb.setHeaderData(ArrHeader);
		rt.setBody(tb);
		rt.setDefaultTitle(1, 2, "��λ��(����)", Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, "���ڣ�"+getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��", Table.ALIGN_CENTER);
		rt.setDefaultTitle(7, 2, "��λ����", Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "�����ˣ�"+fuzr, Table.ALIGN_CENTER);
		rt.setDefautlFooter(4, 2, "��ˣ�"+tianbr, Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 2, DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_LEFT);
		
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
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("��ݣ�"));
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(60);
		nf_comb.setListWidth(60);
		nf_comb.setTransform("Nianf");
		nf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf_comb.setLazyRender(true);
		tbr.addField(nf_comb);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�·ݣ�"));
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(60);
		yf_comb.setListWidth(60);
		yf_comb.setTransform("Yuef");
		yf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		yf_comb.setLazyRender(true);
		tbr.addField(yf_comb);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "��ѯ", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
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
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel2(null); // ���������
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // �·�������
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}
}