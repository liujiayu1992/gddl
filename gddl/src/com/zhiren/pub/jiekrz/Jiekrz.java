package com.zhiren.pub.jiekrz;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterCom;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Jiekrz extends BasePage {
	private String value;
	
	public String getValue() {
		if(value==null||value.equals("")){
			value="1";
		}
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean getRaw() {
		return true;
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

//	private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '�����еĳ����Ƿ���ʾ'";
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}
//
//	public void setFlag(int _value) {
//		_Flag = _value;
//	}

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getRiz();
		//} else {
		//	return "�޴˱���";
		//}
	}
	// ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getRiz() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		String Strzhixzt="";
		String Strrenwmc="";
		String sql="";
		if(getLeixSelectValue().getId()!=-2){//ȫ��
			Strzhixzt=" and zhixzt="+getLeixSelectValue().getId();
		}
		if(getrenwValue().getId()!=-1){
			Strrenwmc="and  renwms='"+getrenwValue().getValue()+"'";
		}
		if(getValue().equals("1")){
			sql="select mingc,renwms,renwbs,decode(caoz,0,'����','ɾ��')caoz,decode(zhixzt,-1,'ʧ��',1,'�ɹ�',' ')zhixzt,to_char(shij,'YYYY-MM-DD HH24:MI:SS')shij,\n" +
			"cuowdm,cuowxx\n" + 
			"from(\n" + 
			"select d.mingc,renwmcb.renwms,renwbs,caoz,zhixzt,decode(pinl,'��ʱ',shij,riq)shij,cuowdm,cuowxx,pinl\n" + 
			"from jiekjsrzb,diancxxb d,renwmcb\n" + 
			"where jiekjsrzb.diancxxb_id=d.id and jiekjsrzb.renw=renwmcb.renw and jiekjsrzb.id   in(\n" + 
			"select max(id)\n" + 
			"from jiekjsrzb\n" + 
			"where "+
			"jiekjsrzb.diancxxb_id in(\n" +
			"   select id from diancxxb\n" + 
			"   start with id=" + getDiancmcValue().getId()+
			"   connect by fuid=prior id)"+
			"group by diancxxb_id,renw,renwbs,caoz) )\n" + 
			"where  to_char(shij,'YYYY-MM-DD')>='"+getRiq1()+"' and to_char(shij,'YYYY-MM-DD')<='"+getRiq2()+"' "+ Strrenwmc+Strzhixzt+
			"order by mingc,renwms,renwbs";
		}else{
			sql="select d.mingc,renwmcb.renwms,renwbs,decode(caoz,0,'����','ɾ��')caoz,decode(zhixzt,-1,'ʧ��',1,'�ɹ�',' ')zhixzt,to_char(shij,'YYYY-MM-DD HH24:MI:SS')shij,\n" +
			"cuowdm,cuowxx\n" + 
			"from jiekjsrzb,diancxxb d,renwmcb\n" + 
			"where "+
			"jiekjsrzb.diancxxb_id in(\n" +
			"   select id from diancxxb\n" + 
			"   start with id=" + getDiancmcValue().getId()+
			"   connect by fuid=prior id)"+
			" and jiekjsrzb.diancxxb_id=d.id and to_char(shij,'YYYY-MM-DD')>='"+getRiq1()+"'and jiekjsrzb.renw=renwmcb.renw and to_char(shij,'YYYY-MM-DD')<='"+getRiq2()+"'"+Strrenwmc+Strzhixzt;

		}

		ResultSet rs = con.getResultSet(new StringBuffer(sql),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		
		ArrHeader = new String[1][7];
		ArrWidth = new int[] {100,50,50,50,50,100,50,350};//��������", "��ʶ","����","ִ��״̬","ִ��ʱ��","�������","���","������Ϣ"
		ArrHeader[0] = new String[] { "��λ","��������","��ʶ","����","ִ��״̬","����ʱ��","�������","������Ϣ"};
		rt.setBody(new Table(rs, 1, 0, 1));
		//
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("�� �� �� ־ �� ��", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 14, "��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}
	private boolean ShangcButton = false;

	public void ShangcButton(IRequestCycle cycle) {
		ShangcButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
//		if (ShangcButton) {
//			ShangcButton = false;
//			InterCom jiek=new InterCom();
//			if(getrenwValue().getId()==-1){//ȫ��
//				jiek.getRenwall(String.valueOf(getDiancmcValue().getId()));
//			}else{
//				String renwmc="";
//				JDBCcon con=new JDBCcon();
//				ResultSet rs=con.getResultSet("select renw from renwmcb where renwms='"+getrenwValue().getValue()+"'");
//				try {
//					if(rs.next()){
//						renwmc=rs.getString(1);
//					}
//				} catch (SQLException e) {
//					// TODO �Զ����� catch ��
//					e.printStackTrace();
//				}finally{
//					con.Close();
//				}
//				jiek.getRenw(String.valueOf(getDiancmcValue().getId()),renwmc );
//				
//			}
//		}
		
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
			setLeixSelectValue((IDropDownBean)getLeixSelectModel().getOption(1));
		}
		blnIsBegin = true;
		if(chang1){
			getrenwModels();
			chang1=false;
		}
		if(chang2){
			getrenwModels();
			chang2=false;
		}
		if(_DiancmcChange){
			getrenwModels();
			_DiancmcChange=false;
		}

	}
		public IPropertySelectionModel getrenwModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
				getrenwModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel1();
		}
		
		public IDropDownBean getrenwValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getrenwModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean1();
		}
		
		public void setrenwValue(IDropDownBean Value) {
			((Visit)getPage().getVisit()).setDropDownBean1(Value);
		}

		public IPropertySelectionModel getrenwModels() {
			List list=new ArrayList();
			list.add(new IDropDownBean(-1,"ȫ��") );
			String sql = 
				"select rownum,renwms\n" +
				"from(\n" + 
				"select distinct renwms\n" + 
				"from jiekjsrzb,renwmcb\n" + 
				"where jiekjsrzb.renw=renwmcb.renw and " +
				"jiekjsrzb.diancxxb_id in("+

				"select id from diancxxb\n" +
				" start with id="+getDiancmcValue().getId()+
				" connect by fuid=prior id)"+
				" and to_char(jiekjsrzb.shij,'YYYY-MM-DD')>='"+getRiq1()+"' and to_char(jiekjsrzb.shij,'YYYY-MM-DD')<='"+getRiq2()+
				"')";
			 setrenwModel(new IDropDownModel(list,sql)) ;
			 return ((Visit) getPage().getVisit()).getProSelectionModel1();
		}

		public void setrenwModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
		//�糧����
		private IPropertySelectionModel _IDiancModel;
		public IPropertySelectionModel getDiancmcModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
				getDiancmcModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel2();
		}
		
		private boolean _DiancmcChange=false;
		public IDropDownBean getDiancmcValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
				((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getDiancmcModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean2();
		}
		
		public void setDiancmcValue(IDropDownBean Value) {
			if (((Visit)getPage().getVisit()).getDropDownBean2()==Value) {
				_DiancmcChange = false;
			}else{
				_DiancmcChange = true;
			}
			((Visit)getPage().getVisit()).setDropDownBean2(Value);
		}

		public IPropertySelectionModel getDiancmcModels() {
			String sql = 
				"select id,mingc from diancxxb where id in(\n" +
				"   select id from diancxxb\n" + 
				"    start with id=" +((Visit) getPage().getVisit()).getDiancxxb_id()+ 
				"     connect by fuid=prior id\n" + 
				")";

			 setDiancmcModel(new IDropDownModel(sql)) ;
			 return ((Visit) getPage().getVisit()).getProSelectionModel2();
		}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel2(_value);
		}
		private String riq1;
		private boolean chang1,chang2;
		public String getRiq1(){
			if(riq1==null||riq1.equals("")){
				riq1=DateUtil.FormatDate(new Date());
			}
			return riq1;
		}
		public void setRiq1(String value){
			if(!value.equals(riq1)){
				chang1=true;
			}
			riq1=value;
		}
		private String riq2;
		public String getRiq2(){
			if(riq2==null||riq2.equals("")){
				riq2=DateUtil.FormatDate(new Date());
			}
			return riq2;
		}
		public void setRiq2(String value){
			if(!value.equals(riq2)){
				chang2=true;
			}
			riq2=value;
		}
  
		 //����
	    public IDropDownBean getLeixSelectValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean4();
	    }
	    public void setLeixSelectValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
	    }
	    public void setLeixSelectModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
	    }

	    public IPropertySelectionModel getLeixSelectModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
	            getLeixSelectModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel4();
	    }
	    public void getLeixSelectModels() {
	        List list=new ArrayList();
	        list.add(new IDropDownBean(-2,"ȫ��"));
	        list.add(new IDropDownBean(-1,"ʧ��"));
	        list.add(new IDropDownBean(1,"�ɹ�"));
	        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
	        return ;
	    }
   
}
