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
 *����:2010-5-26 13:47:51
 *����:ú̿������Ʊ���ձ�
 */


public class Meitxsphsb extends BasePage implements PageValidateListener {
	
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

//	��Ӧ��������_��ʼ
	public IDropDownBean getGongysValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean5() == null) {
			if (getGongysModel().getOptionCount() > 0) {
				setGongysValue((IDropDownBean) getGongysModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean5();
	}

	public void setGongysValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean5(LeibValue);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			getGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(value);
	}

	public void getGongysModels() {
		String sql = "select id, mingc from gongysb where leix=1 order by mingc";
		setGongysModel(new IDropDownModel(sql, "��ѡ��"));
	}
//	��Ӧ��������_����
	
	
	
//	ú��������_��ʼ
	public IDropDownBean getMeikValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getMeikModel().getOptionCount() > 0) {
				setMeikValue((IDropDownBean) getMeikModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setMeikValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LeibValue);
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			getMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setMeikModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getMeikModels() {
		String sql = "select id, mingc from meikxxb  order by mingc";
		setMeikModel(new IDropDownModel(sql, "��ѡ��"));
	}
//	ú��������_����
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
		String tiaoj="";
		String nianf=this.getNianfValue().getValue();
		String yuef=this.getYuefValue().getValue();
		String yuef1="";
		if(Long.parseLong(yuef)<10){
			 yuef1="0"+yuef;
		}
		long gongys=this.getGongysValue().getId();
		long meik=this.getMeikValue().getId();
		if(gongys==-1){
			//Ĭ������²�������ú��ׯ������,��ú��ׯ��id��2641506
			tiaoj=" and m.meikxxb_id!=2641506";
		}else{
			tiaoj=" and m.gongysb_id="+gongys+"";
		}
		
		if(meik==-1){
			//
		}else{
			tiaoj=" and m.meikxxb_id="+meik;
		}
		
		
		String sql = 


			"select decode(g.mingc,null,'�ܼ�',g.mingc) as gongys,\n" +
			"decode(grouping(g.mingc)+grouping(mk.mingc),1,'С��',mk.mingc) as meik,\n" + 
			"'"+yuef+"'||'�·�' as yuef,count(m.id) as zhis,\n" + 
			"sum(m.shul) as shul,min(m.bianh)||' �� '||max(m.bianh) as bianh\n" + 
			"from meitxspjb m,gongysb g ,meikxxb mk\n" + 
			"where m.gongysb_id=g.id\n" + 
			"and m.meikxxb_id=mk.id\n" + 
			"and to_char(m.riq,'yyyy-mm')='"+nianf+"-"+yuef1+"'\n" + 
			""+tiaoj+"\n"+
			"group by rollup (g.mingc,mk.mingc)\n" + 
			"order by g.mingc,mk.mingc";


		ResultSetList rslData = con.getResultSetList(sql);
		
		
		String[][] ArrHeader = new String[1][6];
		ArrHeader[0] = new String[]{ "��ú��λ", "ú��", "ʱ��", "֧��", "����", "���"};
		int[] ArrWidth = new int[] { 150, 150, 80, 80, 90,200};
		
		
		rt.setBody(new Table(rslData, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		for (int i=1 ;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
//		�趨С���еı���ɫ������
		for (int i=1;i<=rt.body.getRows();i++){
			String xiaoj=rt.body.getCellValue(i, 2);
			if(xiaoj.equals("С��")||xiaoj.equals("")){
				for (int j=0;j<rt.body.getCols()+1;j++){
					//rt.body.getCell(i, j).backColor="silver";
					rt.body.getCell(i, j).fontBold=true;
				}
			}
		}
		if(meik==2641506){//2641506�ǽ�ú��ׯ
			rt.setTitle("ɽ��ʡú̿����Ʊ����·������"+nianf+"��"+yuef+"�»��ջ��ܱ�<br>��         д         Ʊ", ArrWidth);
			
		}else{
			rt.setTitle("ɽ��ʡú̿����Ʊ����·������"+nianf+"��"+yuef+"�»��ջ��ܱ�<br>��         ��         Ʊ", ArrWidth);
			
		}
		
		
		rt.setDefaultTitle(1, 2, "���λ��(����)", Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2, ""+DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_CENTER);
		rt.setDefaultTitle(5, 2, "��λ����", Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ˣ��� �� ��", Table.ALIGN_CENTER);
		rt.setDefautlFooter(4, 2, "�����ˣ�", Table.ALIGN_CENTER);
		
		rt.body.setPageRows(23);
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
		
		tbr.addText(new ToolbarText("��Ӧ�̵�λ��"));
		ComboBox gysdw = new ComboBox();
		gysdw.setTransform("Gongys");
		gysdw.setWidth(120);
		gysdw.setListWidth(150);
		gysdw.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		gysdw.setLazyRender(true);
		tbr.addField(gysdw);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("ú��λ��"));
		ComboBox mk = new ComboBox();
		mk.setTransform("Meik");
		mk.setWidth(120);
		mk.setListWidth(150);
		mk.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		mk.setLazyRender(true);
		tbr.addField(mk);
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
			
			visit.setProSelectionModel5(null); //��Ӧ��������
			visit.setDropDownBean5(null);
			visit.setProSelectionModel4(null); //ú��������
			visit.setDropDownBean4(null);
		}
		getSelectData();
	}
}