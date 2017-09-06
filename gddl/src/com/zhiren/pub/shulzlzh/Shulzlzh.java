package com.zhiren.pub.shulzlzh;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shulzlzh extends BasePage implements PageValidateListener {
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
	
	
	private boolean _CheckChick = false;

	public void CheckButton(IRequestCycle cycle) {
		_CheckChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}	
		if(_CheckChick)	{
			_CheckChick= false;
			create();
		}
			
//		getSelectData();
	}
private void create(){
	JDBCcon con = new JDBCcon();
	//����������ݺ��·�������
	long intyear;
	if (getNianfValue() == null) {
		intyear = DateUtil.getYear(new Date());
	} else {
		intyear = getNianfValue().getId();
	}
	long intMonth;
	if (getYuefValue() == null) {
		intMonth = DateUtil.getMonth(new Date());
	} else {
		intMonth = getYuefValue().getId();
	}
	//���·���1��ʱ����ʾ01,
	String StrMonth="";
	if(intMonth<10){
		
		StrMonth="0"+intMonth;
	}else{
		StrMonth=""+intMonth;
	}
	//delete from shulzlzhb where riq='200701'
	
	con.getDelete("delete from shulzlzhb where to_char(riq,'yyyy-mm')='"+ intyear + "-" + StrMonth+"'");
	
	String  insert ="insert into shulzlzhb(id,riq,gongysb_id,yunsfsb_id,pinzb_id,jihkjb_id,biaoz,jingz,yingd,kuid,yuns,koud,farl,huiff,liuf,quansf,shuif,huif)"
		    + "(select f.id as id,f.daohrq as riq,g.id as gongysb_id,y.id as yunsfsb_id,p.id as pinzb_id,\n" 
			+"j.id as jihkjb_id,f.biaoz as biaoz, f.jingz as jingz,f.yingd as yingd,\n" 
			+"f.yingk as kuid, f.yuns as yuns, f.koud as koud,\n"
			+"z.qnet_ar as farl,z.vad as huiff,z.std as liuf,z.mt as quansf,z.mad as shuif,z.aar as huif \n"
			+"from gongysb g,yunsfsb y,jihkjb j,fahb f,zhilb z,pinzb p \n"				
			+ "where f.gongysb_id=g.id(+) and f.jihkjb_id = j.id(+) and f.yunsfsb_id= y.id(+) \n" 
			+"and f.zhilb_id=z.id(+)  and f.pinzb_id=p.id and to_char(f.daohrq,'yyyy-mm') ='" 
			+ intyear + "-" + StrMonth+"')";
	
     con.getInsert(insert);

		con.Close();
}
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		//����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//���·�С��10��ʱ������ǰ��ӡ�0��
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		String chaxun;
		
		chaxun ="select distinct s.id as id,s.riq as riq,g.mingc as gongysb_id,y.mingc as yunsfsb_id, \n" 
			+"p.mingc as pinzb_id,j.mingc as jihkjb_id,s.biaoz,s.jingz,s.yingd,s.kuid,s.yuns,s.koud,s.farl,\n" 
			+"s.huiff,s.liuf,s.quansf,s.shuif,s.huif \n" 
			+"from shulzlzhb s, gongysb g,yunsfsb y,jihkjb j,fahb f,zhilb z,pinzb p \n" 
			+"where s.gongysb_id=g.id(+) and s.yunsfsb_id=y.id(+) and s.pinzb_id=p.id(+) and s.jihkjb_id=j.id(+) \n" 
			+"and to_char(riq,'yyyy-mm') ='"+ intyear + "-" + StrMonth+"' order by id";
	//System.out.println(chaxun);	
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("shulzlzhb");
   	
	egu.getColumn("riq").setHeader("����");
	egu.getColumn("gongysb_id").setHeader("��Ӧ��");
	egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
	egu.getColumn("pinzb_id").setHeader("Ʒ��");
	egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
	egu.getColumn("biaoz").setHeader("Ʊ��");
	egu.getColumn("biaoz").setDefaultValue("0");
	egu.getColumn("jingz").setHeader("����");
	egu.getColumn("jingz").setDefaultValue("0");
	egu.getColumn("yingd").setHeader("Ӯ��");
	egu.getColumn("yingd").setDefaultValue("0");
	egu.getColumn("kuid").setHeader("����");
	egu.getColumn("kuid").setDefaultValue("0");
	egu.getColumn("yuns").setHeader("����");
	egu.getColumn("yuns").setDefaultValue("0");
	egu.getColumn("koud").setHeader("�۶�");
	egu.getColumn("koud").setDefaultValue("0");
	egu.getColumn("farl").setHeader("������");
	egu.getColumn("farl").setDefaultValue("0");
	egu.getColumn("huiff").setHeader("�ӷ���");
	egu.getColumn("huiff").setDefaultValue("0");
	egu.getColumn("liuf").setHeader("���");
	egu.getColumn("liuf").setDefaultValue("0");
	egu.getColumn("quansf").setHeader("ȫˮ��");
	egu.getColumn("quansf").setDefaultValue("0");
	egu.getColumn("shuif").setHeader("ˮ��");
	egu.getColumn("shuif").setDefaultValue("0");
	egu.getColumn("huif").setHeader("�ҷ�");
	egu.getColumn("huif").setDefaultValue("0");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	
	
	//�趨�г�ʼ���
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(100);
	egu.getColumn("yunsfsb_id").setWidth(80);
	egu.getColumn("pinzb_id").setWidth(60);
	egu.getColumn("jihkjb_id").setWidth(60);
	egu.getColumn("biaoz").setWidth(60);
	egu.getColumn("jingz").setWidth(60);
	egu.getColumn("yingd").setWidth(60);
	egu.getColumn("kuid").setWidth(60);
	egu.getColumn("yuns").setWidth(60);
	egu.getColumn("koud").setWidth(60);
	egu.getColumn("farl").setWidth(60);
	egu.getColumn("huiff").setWidth(60);
	egu.getColumn("liuf").setWidth(60);
	egu.getColumn("quansf").setWidth(60);
	egu.getColumn("shuif").setWidth(60);
	egu.getColumn("huif").setWidth(60);
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(100);//���÷�ҳ
	egu.setWidth(1000);//����ҳ��Ŀ��,������������ʱ��ʾ������
	
	
	
	//*****************************************����Ĭ��ֵ****************************
	
	//�������ڵ�Ĭ��ֵ,
	egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	
	//*************************������*****************************************88
	//���ù�Ӧ�̵�������
	egu.getColumn("gongysb_id").setEditor(new ComboBox());
	
	String GongysSql="select id,mingc from gongysb order by id";
	egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
	//�������䷽ʽ������
	ComboBox yunsfs=new ComboBox();
	egu.getColumn("yunsfsb_id").setEditor(yunsfs);
	yunsfs.setEditable(true);
	String yunsfsSql="select id ,mingc from yunsfsb order by id";;
	egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel(yunsfsSql));
	//���üƻ��ھ�������
	ComboBox jihkj=new ComboBox();
	egu.getColumn("jihkjb_id").setEditor(jihkj);
	jihkj.setEditable(true);
	String jihkjSql="select id,mingc from jihkjb order by id";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
	
//	����Ʒ��������
	ComboBox pinz=new ComboBox();
	egu.getColumn("pinzb_id").setEditor(pinz);
	pinz.setEditable(true);
	String pinzSql="select id,mingc from pinzb order by id";
	egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));

	
	//********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("�·�:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");//���÷ָ���
		
		
		//�趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("����","function(){document.all.item('CheckButton').click()}").getScript()+"}");		
		
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/Shulzlzhreport&lx='+NIANF.getValue()+'&lx='+YUEF.getValue();" +
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
			this.setYuefValue(null);
			this.getYuefModels();
			
			
		}
		getSelectData();
	}
//	 ���
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

	// �·�
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

}
