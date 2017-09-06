package com.zhiren.pub.hetgl;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Hetgl extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
    //ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private boolean _GuanlChick = false;
    public void GuanlButton(IRequestCycle cycle) {
    	_GuanlChick = true;
    }
    private void Refurbish() {
            //Ϊ  "ˢ��"  ��ť��Ӵ������
            getSelectData();
    } 

	public void submit(IRequestCycle cycle) {
		
		if(_RefurbishChick){
			_RefurbishChick = false;
			Refurbish();
		}
		if(_GuanlChick){
			_GuanlChick = false;
			GotoShezfa(cycle);
		}
		
//		cycle.activate("12321");
	}
	
	private void GotoShezfa(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		((Visit) getPage().getVisit()).setString10(this.getParameters());
		((Visit) getPage().getVisit()).setString11(String.valueOf(this.getGuanllxValue().getId()));
		cycle.activate("Guanl");
	}
	
	private String Parameters;//��¼��ĿID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		//���ڿؼ�
//		String riqTiaoj=this.getRiqi();
//		if(riqTiaoj==null||riqTiaoj.equals("")){
//			riqTiaoj=DateUtil.FormatDate(new Date());	
//		}
		
		//������������
		String guanllxValue = this.getGuanllxValue().getValue();
		String dat = DateUtil.FormatDate(new Date());
		ResultSetList rsl = null;
		ExtGridUtil egu = null;
		if(guanllxValue.equals("ú���ͬ�����˷Ѻ�ͬ")){
			rsl = con
			.getResultSetList("select h.id,h.hetbh,g.mingc as gongysmc,h.qisrq,h.guoqrq from hetb h,gongysb g "
					+ " where gongysb_id = g.id "
					+ " and h.qisrq <= to_date('"+dat+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+dat+"','yyyy-mm-dd')"
					+ " order by h.id");
			
			egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("hetgl");
			
			//������ʾ������
			egu.getColumn("id").setHidden(true);
			egu.getColumn("hetbh").setHeader("��ͬ���");
			egu.getColumn("gongysmc").setHeader("��Ӧ������");
			egu.getColumn("qisrq").setHeader("��ʼ����");
			egu.getColumn("guoqrq").setHeader("��ֹ����");		
			 //�����п��
			 egu.getColumn("gongysmc").setWidth(200);

		}else{
			rsl = con
			.getResultSetList("select h.id,h.hetbh,y.mingc as chengydwmc,h.qisrq,h.guoqrq from hetys h,yunsdwb y " 
					+ " where h.yunsdwb_id = y.id"
					+ " and h.qisrq <= to_date('"+dat+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+dat+"','yyyy-mm-dd')"
					+ " order by h.id");
			egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("hetgl");
			
			//������ʾ������
			egu.getColumn("id").setHidden(true);
			egu.getColumn("hetbh").setHeader("��ͬ���");
			egu.getColumn("chengydwmc").setHeader("���˵�λ����");
			egu.getColumn("qisrq").setHeader("��ʼ����");
			egu.getColumn("guoqrq").setHeader("��ֹ����");	
			 //�����п��
			 egu.getColumn("chengydwmc").setWidth(200);
		}
		
//		//���õ�ǰgrid�Ƿ�ɱ༭
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		
//********************������************************************************
		// �ܸ�����
		egu.addTbarText("��������:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("GuanllxDropDown");
		comb1.setId("guanllx");
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(200);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
//		egu.getColumn("guanllx").setDefaultValue("ú���ͬ�����˷Ѻ�ͬ");

//		 /���ð�ť
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarItem("{"+new GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
	        +"  if(rec!=null){\n"
	        +"  	gridDiv_history = rec.get('ID');\n"
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"  }else{\n"
	        +"  	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ŀ!'); \n"
	        +"  	return;"
	        +"  }"
	        +" document.getElementById('GuanlButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("����","function(){"+str2+"}").getScript()+"}");
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
			visit.setString10("");	//����ͬid	
			visit.setString11("");	//����������id
			
			if(((Visit) this.getPage().getVisit()).getString2()==null||
					!((Visit) this.getPage().getVisit()).getString2().equals("GUANL")) {
				
				this.setGuanllxValue(null);
				setGuanllxModel(null);
	        }
//			
		}
		getSelectData();
	}

	// ��������
	public boolean _guanllxchange = false;

	private IDropDownBean _GuanllxValue;

	public IDropDownBean getGuanllxValue() {
		if (_GuanllxValue == null) {
			_GuanllxValue = (IDropDownBean) getGuanllxModel().getOption(0);
		}
		return _GuanllxValue;
	}

	public void setGuanllxValue(IDropDownBean Value) {
		long id = -2;
		if (_GuanllxValue != null) {
			id = _GuanllxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_guanllxchange = true;
			} else {
				_guanllxchange = false;
			}
		}
		_GuanllxValue = Value;
	}
	
	public void setGuanllxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGuanllxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGuanllxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getGuanllxModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "ú���ͬ�����˷Ѻ�ͬ"));
		list.add(new IDropDownBean(0, "�˷Ѻ�ͬ����ú���ͬ"));
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(list));

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
}

