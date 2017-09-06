package com.zhiren.jt.zdt.chengbgl.rucycb;
/* 
* ʱ�䣺2009-08-29
* ���ߣ� ll
* �޸����ݣ�1��Ʒ����ǰ�Ʋ��ϲ������¡��ۼƶ������ݵĲ���ʾ��
* 				������λ�ܼƹ���,��λ�����Ͻ�.
* 		   
*/ 
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */


public class Rucycbreport extends BasePage {
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	 //	ҳ���ж�����
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
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
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
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
		  int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj)
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
	
	public boolean getRaw() {
		return true;
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}
	
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	private String REPORT_RUCYCBREPORT="rucycbreport";
	private String mstrReportName="";
	
	//�õ���½��Ա�����糧��ֹ�˾������
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_RUCYCBREPORT)){
			return getRucycbreport();
		}else{
			return "�޴˱���";
		}
	}

	public String getRucycbreport(){
		
		_CurrentPage=1;
		_AllPages=1;
		
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
		String strdate = intyear+"-"+StrMonth+"-01";
		String titdate = intyear+"��"+StrMonth+"��";
		Report rt=new Report();
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();

		String str = "";
		String guolzj="";
		String youzt="";
		if(visit.getRenyjb()==3){
			
			youzt="";
		}else if(visit.getRenyjb()==2){
			youzt=" and (rcy.zhuangt=1 or rcy.zhuangt=2)";
		}else if(visit.getRenyjb()==1){
			
			youzt=" and rcy.zhuangt=2";
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and (dc.fuid="+getTreeid()+" or dc.shangjgsid = "+ getTreeid() + ") ";
			guolzj=" and grouping(fx.fgsmc)=0\n";//			ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + " ";
			guolzj=" and grouping(fx.diancmc)=0\n";//ѡ�糧ֻˢ�³��õ糧
		}

		String sql=
//			"select decode(grouping(gs.mingc)+grouping(dc.mingc),2,decode(gs.mingc,null,'�ܼ�'),1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc) as danwmc,\n" +
//			"       cb.fenx,\n" + 
//			"       decode(grouping(gs.mingc)+grouping(dc.mingc)+grouping(p.mingc),3,'Ʒ��С��',\n" + 
//			"       	2,decode(gs.mingc,null,p.mingc,'Ʒ��С��'),\n" + 
//			"       	1,decode(dc.mingc,null,p.mingc,'Ʒ��С��'),p.mingc) as pingz,\n" + 
//			"       sum(cb.shul) as shul,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(youfrl,0)*nvl(shul,0))/sum(nvl(shul,0)),3)) as youfrl,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(hanszhj,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as hanszhj,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(buhszhj,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as buhszhj,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(youj,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as youj,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(yunf,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as yunf,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(yunzf,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as yunzf,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(qitfy,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as qitfy,\n" + 
//			"       decode(sum(nvl(shul,0)),0,0,decode(sum(nvl(shul,0)*nvl(youfrl,0)),0,0,\n" + 
//			"          round(sum(nvl(zhebmdj,0)*(nvl(shul,0)*nvl(youfrl,0)/29.271))/(sum(nvl(shul,0)*nvl(youfrl,0))/29.271),2))) as zhebmdj\n" + 
//			"      from rucycbb cb,diancxxb dc,diancxxb gs,pinzb p\n" + 
//			" where cb.diancxxb_id=dc.id and dc.fuid=gs.id "+str+" and cb.riq=to_date('"+strdate+"','yyyy-mm-dd') and cb.pinzb_id=p.id\n" + 
//			" group by cube (cb.fenx,p.mingc,gs.mingc,dc.mingc)\n" + 
//			" having not (grouping(cb.fenx)=1 or (grouping(gs.mingc)=1 and grouping(dc.mingc)<>1))\n" + 
//			" order by grouping(gs.mingc) desc,grouping(dc.mingc) desc,dc.mingc desc,p.mingc desc ,cb.fenx";

		"select decode(grouping(fx.fgsmc)+grouping(fx.diancmc),2,decode(fx.fgsmc,null,'�ܼ�'),1,'&nbsp;&nbsp;'||fx.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||fx.diancmc) as danwmc,\n" +
		
		"      decode(grouping(fx.mingc),1,decode(fx.mingc,null,'С��'),fx.mingc) as pinz,\n" + 
		"       fx.fenx,\n" + 
		"       sum(cb.shul) as shul,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(youfrl,0)*nvl(shul,0))/sum(nvl(shul,0)),3)) as youfrl,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(hanszhj,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as hanszhj,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(buhszhj,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as buhszhj,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(youj,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as youj,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(yunf,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as yunf,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(yunzf,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as yunzf,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,round(sum(nvl(qitfy,0)*nvl(shul,0))/sum(nvl(shul,0)),2)) as qitfy,\n" + 
		"       decode(sum(nvl(shul,0)),0,0,decode(sum(nvl(shul,0)*nvl(youfrl,0)),0,0,\n" + 
		"          round(sum(nvl(zhebmdj,0)*(nvl(shul,0)*nvl(youfrl,0)/29.271))/(sum(nvl(shul,0)*nvl(youfrl,0))/29.271),2))) as zhebmdj\n" + 
		"      from\n" + 
		"      ( select distinct dc.fgsmc,dc.fgsxh ,dc.id as id,dc.mingc as diancmc,dc.xuh, pz.id as pinzb_id, pz.mingc as mingc,fx.xuh as xuh_fx,fx.fenx\n" + 
		"      from vwdianc dc, rucycbb rcy,pinzb pz,(select 1 as xuh,decode(1,1,'����','') as fenx from dual union select 2 as xuh,decode(1,1,'�ۼ�','') as fenx from dual)fx\n" + 
		"      where pz.leib='��' and rcy.riq=to_date('"+strdate+"','yyyy-mm-dd') and rcy.diancxxb_id=dc.id and rcy.pinzb_id=pz.id "+str+" )fx,\n" + 
		"      (select decode(1,1,'����') as fenx,rcy.diancxxb_id,rcy.pinzb_id,rcy.shul,rcy.youfrl,rcy.hanszhj,rcy.buhszhj,rcy.youj,rcy.yunf,rcy.yunzf,rcy.qitfy,rcy.zhebmdj\n" + 
		"       from rucycbb rcy,vwdianc dc\n" + 
		"       where rcy.riq=to_date('"+strdate+"','yyyy-mm-dd') and rcy.diancxxb_id=dc.id and rcy.fenx='����'  "+youzt+str+"\n" + 
		"       union\n" + 
		"       select decode(1,1,'�ۼ�') as fenx,rcy.diancxxb_id,rcy.pinzb_id,rcy.shul,rcy.youfrl,rcy.hanszhj,rcy.buhszhj,rcy.youj,rcy.yunf,rcy.yunzf,rcy.qitfy,rcy.zhebmdj\n" + 
		"       from rucycbb rcy,vwdianc dc\n" + 
		"       where rcy.riq=to_date('"+strdate+"','yyyy-mm-dd') and rcy.diancxxb_id=dc.id and rcy.fenx='�ۼ�' "+youzt+str+"\n" + 
		"        )cb\n" + 
		" where fx.id=cb.diancxxb_id(+) and fx.pinzb_id=cb.pinzb_id(+) and fx.fenx=cb.fenx(+)\n" + 
		" group by cube (fx.fenx,fx.fgsmc,(fx.diancmc,fx.xuh),fx.mingc)\n" + 
		" having not (grouping(fx.fenx)=1 or (grouping(fx.fgsmc)=1 and grouping(fx.diancmc)<>1)) "+guolzj+"\n" + 
		" order by grouping(fx.fgsmc) desc,max(fx.fgsxh),fx.fgsmc,grouping(fx.diancmc) desc,fx.xuh,fx.diancmc ,\n" + 
		"       grouping(fx.mingc) desc ,fx.mingc,fx.fenx\n" + 
		"";

		
		 String ArrHeader[][]=new String[2][12];
		 ArrHeader[0]=new String[] {"�糧","Ʒ��","����","����","�ͷ�����","�ۺϼ�","�ۺϼ�","�ͼ�","�˷�","���ӷ�","��������","�۱�ú����"};
		 ArrHeader[1]=new String[] {"�糧","Ʒ��","����","����","�ͷ�����","��˰","����˰","�ͼ�","�˷�","���ӷ�","��������","�۱�ú����"};

		 int ArrWidth[]=new int[] {120,65,40,80,65,55,55,55,55,55,55,65};

		ResultSet rs = cn.getResultSet(sql);
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("���볧�ɱ�ͳ��",ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 3,  titdate, Table.ALIGN_CENTER);
		rt.setDefaultTitle(11, 2,"��λ:��  �׽�  Ԫ/��",Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		if(rt.body.getRows()>2){
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getCellValue(3, 1).equals("�ܼ�")){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
		}
//		ҳ�� 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(5,3,"���:",Table.ALIGN_CENTER);

		 if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
		 			
			 rt.setDefautlFooter(11,2, "�Ʊ�:",Table.ALIGN_RIGHT);
		 		}else{
		 			
		 			rt.setDefautlFooter(11,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
		 		}
//		 rt.setDefautlFooter(11,2,"�Ʊ�:",Table.ALIGN_RIGHT);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
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

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			this.setTreeid(null);
			//begin��������г�ʼ������
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					this.setTreeid(null);
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }
		getToolBars();
		isBegin=true;
	}

//	 �ֹ�˾������
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"�е�Ͷ"));
	}
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		
//		tb1.addText(new ToolbarText("Ʒ��:"));
//		ComboBox tbpinz = new ComboBox();
//		tbpinz.setTransform("PinzDropDown");
//		tbpinz.setWidth(60);
//		tb1.addField(tbpinz);
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
//		ToolbarButton tbLink = new ToolbarButton(null,"��ӡ","function(){}");
		
		tb1.addItem(tb);
//		tb1.addItem(tbLink);
		setToolbar(tb1);
	}
	
	
//	�糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql="";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		
		_IDiancmcModel = new IDropDownModel(sql);
	}
	
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
//  Day������
	private boolean _Day = false;
	private IDropDownBean _DayValue;
	private IPropertySelectionModel _DayModel;

	public IDropDownBean getDayValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean) getDayModel().getOption(14));
    	}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	public void setDayValue(IDropDownBean Value) {
//		if(((Visit)getPage().getVisit()).getDropDownBean1()!=null){
			if(((Visit)getPage().getVisit()).getDropDownBean1().getId()!=Value.getId()){
				_Day=true;
			}
//		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setDayModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
    }
    public IPropertySelectionModel getDayModel() {
        if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
            getDayModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }
    public void getDayModels() {
        List listDay = new ArrayList();
//        listDay.add(new IDropDownBean(-1, "��ѡ��"));
    	for (int i = 1; i < 32; i++) {
            listDay.add(new IDropDownBean(i, String.valueOf(i)+"��"));
        }
    	((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listDay));
    }

	
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
	
//	Ʒ��
	public IDropDownBean getPinzDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getPinzDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setPinzDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setPinzDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getPinzDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getPinzDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getPinzDropDownModels() {
		String sql = "select id,mingc\n" + "from pinzb where leib='��'\n";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql));//, "ȫ��"
		return;
	}
	
}
