package com.zhiren.jt.zdt.chengbgl.rucmcb;

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
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Rucmcbreport extends BasePage {
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
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
		
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(_value);
	}

	// �·�
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean3()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean3(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel3(_value);
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
	
	private String REPORT_Rucmcbreport="Rucmcbreport";
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
		if(! isBegin){
			return "";
		}
		isBegin = false;
		if(mstrReportName.equals(REPORT_Rucmcbreport)){
			if(getBaoblxValue().getValue().equals("��ú�������̱�")){
				return getBiaomdjqpb();
			}else{
				return getRucmcbreport();
			}
		}else{
			return "�޴˱���";
		}
	}

	public String getRucmcbreport(){
		
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
		JDBCcon cn = new JDBCcon();

		String str = "";
		String sets = "";
		String fchaving = "";
		String fkhaving = "";
		int treejib = this.getDiancTreeJib();
		if(isJitUserShow()){//�����û�
			
			if (treejib == 1) {// ѡ����
				str = "";
				sets = "sj.fenx,(kj.mingc,sj.fenx),";
				fchaving = " having not grouping(gy.mingc)=0 ";
				fkhaving = " having not grouping(dc.mingc)=0 ";
				
			} else if (treejib == 2) {// ѡ�ֹ�˾
				fchaving = "";
				fkhaving = "";
				str = " and (dc.id="+getTreeid()+" or dc.fuid = "+ getTreeid() + ") ";
				sets = "";
			} else if (treejib == 3) {// ѡ�糧
				fchaving = " having not grouping(dc.mingc)=1 ";
				fkhaving = "";
				str = " and dc.id = " + getTreeid() + " ";
				sets = "";
			}
		}else if(isGongsUser()){//�ֹ�˾�û�
			fchaving = "";
			fkhaving = "";
			str = " and (dc.id="+getTreeid()+" or dc.fuid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
			sets = "";
			if (treejib == 3) {// ѡ�糧
				str = " and dc.id = " + getTreeid() + " ";
				fchaving = " having not grouping(dc.mingc)=1 ";
				sets = "";
			}
		}else if(isDiancUser()){//�糧�û�
			fchaving = " having not grouping(dc.mingc)=1 ";
			fkhaving = "";
			str = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
			sets = "";
		}else{
			fchaving = "";
			fkhaving = "";
			str = "";
			sets = "";
		}
		
		if(getBaoblxValue().getValue().equals("�ֳ���˰")){
			String sql = "select --case when grouping(dc.mingc)=0 then dc.mingc else \n"
					+ "          --case when grouping(gs.mingc)=0 then gs.mingc else '�ܼ�' end end as �糧, \n"
					
					+ "          case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||gy.mingc else \n"
					+ "          case when grouping(kj.mingc)=0 then '&nbsp;&nbsp;'||kj.mingc else \n"
					+ "          case when grouping(dc.mingc)=0 then dc.mingc else \n"
					+ "          case when grouping(gs.mingc)=0 then gs.mingc else '�ܼ�' end end  end end ��ú��λ, \n"
					+ "       sj.fenx as ����,sum(sj.jingz) as �볧����, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meijs*sj.jingz)/sum(jingz),2)) as ��ֵ˰, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.tielyfse*sj.jingz)/sum(jingz),2)) as ��·�˷�˰��, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qiyse*sj.jingz)/sum(jingz),2)) as ����˰��, \n"
					+ "		  decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�,	\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.haiyse*sj.jingz)/sum(jingz),2)) as ����˰��, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����, \n"
					
					+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
					+ "			  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ��˰��ú����  "
					
					+ "  from  yuetjkjb tj,diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj, \n"
					+ "(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl, \n"
					+ "        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit as zonghj, \n"
					+ "        a.meij,a.meijs,a.jiaohqzf,a.tielyf,a.tielyfse,a.daoztlzf,a.qiyf,a.qiyzf,a.qiyse,a.haiyf,a.haiyse,a.gangzf,a.qit \n"
					+ "   from  \n"
					+ "(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf, \n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf, \n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf, \n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf, \n"
					+ "        dj.qit	\n"
					+ "   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys \n"
					+ "  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id \n"
					+ "    and dj.fenx=sl.fenx and dj.fenx=zl.fenx ) a ) sj \n"
					+ "  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id and dc.fuid=gs.id \n"
					+ "	   	   "+str+" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')  \n"
					+ "  group by grouping sets ("+sets+"(gs.mingc,sj.fenx),(gs.mingc,kj.mingc,sj.fenx), \n"
					+ "        (dc.mingc,sj.fenx),(dc.mingc,kj.mingc,sj.fenx),(dc.mingc,kj.mingc,gy.mingc,sj.fenx)) \n"
					+ "	"+fchaving+" \n"	
					+ "order by	\n"
					+ "    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj.mingc),1,4,3),0) desc, \n"
					+ "    min(gs.xuh), gs.mingc,min(dc.xuh),grouping(kj.mingc) desc,min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),sj.fenx";
	
	
			 String ArrHeader[][]=new String[1][19];
			 ArrHeader[0]=new String[] {"��λ","����<br>�ۼ�","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
			 int ArrWidth[]=new int[] {150,40,65,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50};
	
	
			ResultSet rs = cn.getResultSet(sql);
			
			rt.setTitle("ú̿�볧�ɱ�ͳ��(�ֳ�)",ArrWidth);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 3, titdate, Table.ALIGN_CENTER);
			rt.setDefaultTitle(18,2,"��λ:�� Ԫ/��",Table.ALIGN_RIGHT);
			rt.setBody(new Table(rs,1,0,2));
			rt.body.setHeaderData(ArrHeader);
			rt.body.setWidth(ArrWidth);
			
			rt.body.setRowHeight(1, 50);
			rt.body.setPageRows(24);
			rt.body.ShowZero = false;
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			if(rt.body.getRows()>1){
				rt.body.setColAlign(2, Table.ALIGN_CENTER);
				if(rt.body.getCellValue(2, 1).equals("�ܼ�")){
					rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
				}
			}
	//		ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,4,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			 rt.setDefautlFooter(8,3,"���:",Table.ALIGN_CENTER);

			 if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
			 			
				 rt.setDefautlFooter(18,2, "�Ʊ�:",Table.ALIGN_RIGHT);
			 		}else{
			 			
			 			rt.setDefautlFooter(18,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
			 		}
//			 rt.setDefautlFooter(18,2,"�Ʊ�:",Table.ALIGN_RIGHT);
			 
		}else if(getBaoblxValue().getValue().equals("�ֳ�����˰")){
			
			String sql = "select --case when grouping(dc.mingc)=0 then dc.mingc else \n"
					+ "          --case when grouping(gs.mingc)=0 then gs.mingc else '�ܼ�' end end as �糧, \n"
					+ "          case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||gy.mingc else \n"
					+ "          case when grouping(kj.mingc)=0 then '&nbsp;&nbsp;'||kj.mingc else \n"
					+ "          case when grouping(dc.mingc)=0 then dc.mingc else \n"
					+ "          case when grouping(gs.mingc)=0 then gs.mingc else '�ܼ�' end end  end end ��ú��λ, \n"
					+ "       sj.fenx as ����,sum(sj.jingz) as �볧����, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n"
					+ "		  decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�,	\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�, \n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����, \n"
					
					+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
					+ "			  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ����˰��ú����  "
					
					+ "  from  yuetjkjb tj,diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj, \n"
					+ "(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl, \n"
					+ "        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit-a.meijs-tielyfse-a.qiyse-a.haiyse as zonghj, \n"
					+ "        a.meij-a.meijs as meij,a.jiaohqzf,a.tielyf-a.tielyfse as tielyf," 
					+ "		   a.daoztlzf,a.qiyf-a.qiyse as qiyf,a.qiyzf,a.haiyf-a.haiyse as haiyf,a.gangzf,a.qit \n"
					+ "   from  \n"
					+ "(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf, \n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf, \n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf, \n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf, \n"
					+ "        dj.qit	\n"
					+ "   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys \n"
					+ "  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id \n"
					+ "    and dj.fenx=sl.fenx and dj.fenx=zl.fenx ) a ) sj \n"
					+ "  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id and dc.fuid=gs.id \n"
					+ "	   	   "+str+" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')  \n"
					+ "  group by grouping sets ("+sets+"(gs.mingc,sj.fenx),(gs.mingc,kj.mingc,sj.fenx), \n"
					+ "        (dc.mingc,sj.fenx),(dc.mingc,kj.mingc,sj.fenx),(dc.mingc,kj.mingc,gy.mingc,sj.fenx)) \n"
					+ "	"+fchaving+" \n"	
					+ "order by	\n"
					+ "    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj.mingc),1,4,3),0) desc, \n"
					+ "    min(gs.xuh), gs.mingc,min(dc.xuh),grouping(kj.mingc) desc,min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),sj.fenx";


			 String ArrHeader[][]=new String[1][15];
			 ArrHeader[0]=new String[] {"��λ","����<br>�ۼ�","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
			 int ArrWidth[]=new int[] {120,40,65,50,50,50,50,50,50,50,50,50,50,50,50};
	
	
			ResultSet rs = cn.getResultSet(sql);
			
			rt.setTitle("ú̿�볧�ɱ�ͳ��(�ֳ�)",ArrWidth);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 3, titdate, Table.ALIGN_CENTER);
			rt.setDefaultTitle(14,2,"��λ:�� Ԫ/��",Table.ALIGN_RIGHT);
			rt.setBody(new Table(rs,1,0,2));
			rt.body.setHeaderData(ArrHeader);
			rt.body.setWidth(ArrWidth);
			
			rt.body.setRowHeight(1, 50);
			rt.body.setPageRows(24);
			rt.body.ShowZero = false;
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			if(rt.body.getRows()>1){
				rt.body.setColAlign(2, Table.ALIGN_CENTER);
				if(rt.body.getCellValue(2, 1).equals("�ܼ�")){
					rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
				}
			}			
	//		ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,4,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			 rt.setDefautlFooter(6,3,"���:",Table.ALIGN_CENTER);

			 if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
			 			
				 rt.setDefautlFooter(14,2, "�Ʊ�:",Table.ALIGN_RIGHT);
			 		}else{
			 			
			 			rt.setDefautlFooter(14,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
			 		}
//			 rt.setDefautlFooter(14,2,"�Ʊ�:",Table.ALIGN_RIGHT);
			 
		}else if(getBaoblxValue().getValue().equals("�ֿ�˰")){
			
			String sql = "select --case when grouping(gy.mingc)=0 then gy.mingc else\n"
					+ "       --case when grouping(kj.mingc)=0 then kj.mingc else '�ܼ�' end end as ��ú��λ,\n"
					+ "       case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc else\n"
					+ "       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;'||gy.mingc else\n"
					+ "       case when grouping(kj.mingc)=0 then kj.mingc else '�ܼ�' end end  end ��ú��λ,\n"
					+ "       sj.fenx as ����,\n"
					+ "       sum(sj.jingz) as �볧����,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meijs*sj.jingz)/sum(jingz),2)) as ��ֵ˰,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.tielyfse*sj.jingz)/sum(jingz),2)) as ��·�˷�˰��,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qiyse*sj.jingz)/sum(jingz),2)) as ����˰��,\n"
					+ "		  decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�,	\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.haiyse*sj.jingz)/sum(jingz),2)) as ����˰��,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����,\n"
					
					+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
					+ "			  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ��˰��ú����  "
					
					+ "  from  yuetjkjb tj,diancxxb dc,gongysb gy,jihkjb kj,\n"
					+ "(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl,\n"
					+ "        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit as zonghj,\n"
					+ "        a.meij,a.meijs,a.jiaohqzf,a.tielyf,a.tielyfse,a.daoztlzf,a.qiyf,a.qiyse,a.qiyzf,a.haiyf,a.haiyse,a.gangzf,a.qit \n"
					+ "   from\n"
					+ "(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf,\n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf,\n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf,\n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf,\n"
					+ "        dj.qit\n"
					+ "   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys\n"
					+ "  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id\n"
					+ "    and dj.fenx=sl.fenx and dj.fenx=zl.fenx ) a\n"
					+ "    ) sj\n"
					+ "  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id\n"
					+ "    "+str+" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n"
					+ "  group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gy.mingc,sj.fenx),(gy.mingc,dc.mingc,sj.fenx))\n"
					+ "	"+fkhaving+" \n"	
					+ "order by\n"
					+ "    decode(grouping(kj.mingc)+grouping(gy.mingc)+grouping(dc.mingc),3,3,0) desc,\n"
					+ "    min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),grouping(dc.mingc) desc,min(dc.xuh),sj.fenx";



			 String ArrHeader[][]=new String[1][19];
			 ArrHeader[0]=new String[] {"��λ","����<br>�ۼ�","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
			 int ArrWidth[]=new int[] {120,40,65,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45};
	
	
			ResultSet rs = cn.getResultSet(sql);
			
			rt.setTitle("ú̿�볧�ɱ�ͳ��(�ֿ�)",ArrWidth);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 3, titdate, Table.ALIGN_CENTER);
			rt.setDefaultTitle(18,2,"��λ:�� Ԫ/��",Table.ALIGN_RIGHT);
			rt.setBody(new Table(rs,1,0,2));
			rt.body.setHeaderData(ArrHeader);
			rt.body.setWidth(ArrWidth);
			
			rt.body.setRowHeight(1, 50);
			rt.body.setPageRows(36);
			rt.body.ShowZero = false;
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			
			if(rt.body.getRows()>1){
				rt.body.setColAlign(2, Table.ALIGN_CENTER);
				if(rt.body.getCellValue(2, 1).equals("�ܼ�")){
					rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
				}
			}
	//		ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,3,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			 rt.setDefautlFooter(8,3,"���:",Table.ALIGN_CENTER);

			 if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
			 			
				 rt.setDefautlFooter(18,2, "�Ʊ�:",Table.ALIGN_RIGHT);
			 		}else{
			 			
			 			rt.setDefautlFooter(18,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
			 		}
//			 rt.setDefautlFooter(18,2,"�Ʊ�:",Table.ALIGN_RIGHT);
			 
		}else if(getBaoblxValue().getValue().equals("�ֿ󲻺�˰")){
			
			String sql = "select --case when grouping(gy.mingc)=0 then gy.mingc else\n"
					+ "       --case when grouping(kj.mingc)=0 then kj.mingc else '�ܼ�' end end as ��ú��λ,\n"
					+ "       case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc else\n"
					+ "       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;'||gy.mingc else\n"
					+ "       case when grouping(kj.mingc)=0 then kj.mingc else '�ܼ�' end end  end ��ú��λ,\n"
					+ "       sj.fenx as ����,\n"
					+ "       sum(sj.jingz) as �볧����,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�,\n"
					+ "		  decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�,	\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�,\n"
					+ "       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����,\n"
					
					+ " 	  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0,"
					+ "			  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ����˰��ú����  "
					
					+ "  from  yuetjkjb tj,diancxxb dc,gongysb gy,jihkjb kj,\n"
					+ "(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl,\n"
					+ "        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit-a.meijs-tielyfse-a.qiyse-a.haiyse as zonghj, \n"
					+ "        a.meij-a.meijs as meij,a.jiaohqzf,a.tielyf-a.tielyfse as tielyf," 
					+ "		   a.daoztlzf,a.qiyf-a.qiyse as qiyf,a.qiyzf,a.haiyf-a.haiyse as haiyf,a.gangzf,a.qit \n"
					+ "   from\n"
					+ "(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf,\n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf,\n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf,\n"
					+ "        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf,\n"
					+ "        dj.qit\n"
					+ "   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys\n"
					+ "  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id\n"
					+ "    and dj.fenx=sl.fenx and dj.fenx=zl.fenx ) a\n"
					+ "    ) sj\n"
					+ "  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id\n"
					+ "    "+str+" and tj.riq=to_date('"+strdate+"','yyyy-mm-dd')\n"
					+ "  group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gy.mingc,sj.fenx),(gy.mingc,dc.mingc,sj.fenx))\n"
					+ "	"+fkhaving+" \n"	
					+ "order by\n"
					+ "    decode(grouping(kj.mingc)+grouping(gy.mingc)+grouping(dc.mingc),3,3,0) desc,\n"
					+ "    min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),grouping(dc.mingc) desc,min(dc.xuh),sj.fenx";

			 String ArrHeader[][]=new String[1][15];
			 ArrHeader[0]=new String[] {"��λ","����<br>�ۼ�","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
	
			 int ArrWidth[]=new int[] {120,40,65,45,45,45,45,45,45,45,45,45,45,45,45};
	
			ResultSet rs = cn.getResultSet(sql);
			
			rt.setTitle("ú̿�볧�ɱ�ͳ��(�ֿ�)",ArrWidth);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 3, titdate, Table.ALIGN_CENTER);
			rt.setDefaultTitle(14,2,"��λ:�� Ԫ/��",Table.ALIGN_RIGHT);
			rt.setBody(new Table(rs,1,0,2));
			rt.body.setHeaderData(ArrHeader);
			rt.body.setWidth(ArrWidth);
			
			rt.body.setRowHeight(1, 50);
			rt.body.setPageRows(24);
			rt.body.ShowZero = false;
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			
			if(rt.body.getRows()>1){
				rt.body.setColAlign(2, Table.ALIGN_CENTER);
				if(rt.body.getCellValue(2, 1).equals("�ܼ�")){
					rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
				}
			}
	//		ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,3,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			 rt.setDefautlFooter(6,3,"���:",Table.ALIGN_CENTER);

			 if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
			 			
				 rt.setDefautlFooter(14,2, "�Ʊ�:",Table.ALIGN_RIGHT);
			 		}else{
			 			
			 			rt.setDefautlFooter(14,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
			 		}
//			 rt.setDefautlFooter(14,2,"�Ʊ�:",Table.ALIGN_RIGHT);
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	public String getBiaomdjqpb(){
		
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChessboardTable cd=new  ChessboardTable();
		
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
		
		String str = "";
		int treejib = this.getDiancTreeJib();
		if(isJitUserShow()){//�����û�
			if (treejib == 1) {// ѡ����
				str = "";
			} else if (treejib == 2) {// ѡ�ֹ�˾
				str = " and (dc.id="+getTreeid()+" or dc.fuid = "+ getTreeid() + ") ";
			} else if (treejib == 3) {// ѡ�糧
				str = " and dc.id = " + getTreeid() + " ";
			}
		}else if(isGongsUser()){//�ֹ�˾�û�
			str = " and (dc.id="+getTreeid()+" or dc.fuid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
			if (treejib == 3) {// ѡ�糧
				str = " and dc.id = " + getTreeid() + " ";
			}
		}else if(isDiancUser()){//�糧�û�
			str = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
		}else{
			str = "";
		}
		
		int[] ArrWidth;
		ArrWidth =new int[] {120,60,80,80,80,80,80,80};

		StringBuffer strRow = new StringBuffer();
		 
		strRow.append("select decode(grouping(gy.mingc),1,'С��',gy.mingc) as ��Ӧ�� \n");
		strRow.append("  from yuercbmdj dj,yuetjkjb tj,gongysb gy,diancxxb dc \n");
		strRow.append("where dj.yuetjkjb_id=tj.id and tj.gongysb_id=gy.id and tj.diancxxb_id=dc.id \n");
		strRow.append("  and tj.riq=to_date('"+strdate+"','yyyy-mm-dd') "+str+" \n");
		strRow.append(" group by rollup (gy.mingc)  \n");
		strRow.append(" order by grouping(gy.mingc) desc,min(gy.xuh) \n");

		StringBuffer strCol = new StringBuffer();
		 
		strCol.append("select decode(grouping(dc.mingc),1,'С��',dc.mingc) as �糧 from yuercbmdj dj,yuetjkjb tj,diancxxb dc \n");
		strCol.append(" where dj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd') "+str+" \n");
		strCol.append(" group by rollup (dc.mingc) order by grouping(dc.mingc) desc,min(dc.xuh) \n");
	
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select grouping(gy.mingc) as rowjb,grouping(dc.mingc) as coljb, \n");
		sbsql.append("       decode(grouping(gy.mingc),0,gy.mingc,'С��') as ��Ӧ��,decode(grouping(dc.mingc),0,dc.mingc,'С��') as �糧, \n");
		sbsql.append("       max(benybmdj) as ����, max(leijbmdj) as �ۼ� \n");
		sbsql.append("  from \n");
		sbsql.append("      (select tj.gongysb_id,tj.diancxxb_id,        \n");
		sbsql.append("              decode(sj.fenx,'����', \n");
		sbsql.append("         		    decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
		sbsql.append("                     round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))),0) as benybmdj, \n");
		sbsql.append("              decode(sj.fenx,'�ۼ�', \n");
		sbsql.append("        		    decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
		sbsql.append("                     round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))),0) as leijbmdj \n");
		sbsql.append("        from  yuetjkjb tj, \n");
		sbsql.append("             (select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar as farl,dj.meij+dj.jiaohqzf+dj.yunj+dj.daozzf+dj.qit as zonghj \n");
		sbsql.append("                from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,diancxxb dc \n");
		sbsql.append("               where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id "+str+" \n");
		sbsql.append("                 and dj.fenx=sl.fenx and dj.fenx=zl.fenx ) sj   \n");
		sbsql.append("       where sj.yuetjkjb_id=tj.id and tj.riq=to_date('"+strdate+"','yyyy-mm-dd') group by (sj.fenx,tj.gongysb_id,tj.diancxxb_id) ) h, \n");
		sbsql.append("       diancxxb dc,gongysb gy \n");
		sbsql.append(" where h.diancxxb_id=dc.id and h.gongysb_id=gy.id "+str+" \n");
		sbsql.append(" group by cube (gy.mingc,dc.mingc) \n");
		sbsql.append(" order by grouping(gy.mingc) desc,min(gy.xuh),grouping(dc.mingc) desc,min(dc.xuh) \n");
		
		cd.setRowNames("��Ӧ��");
		cd.setColNames("�糧");
		cd.setDataNames("����,�ۼ�");
		cd.setDataOnRow(true);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(),strCol.toString(),sbsql.toString());
		ArrWidth =new int[cd.DataTable.getCols()];
		for (int i=2;i<ArrWidth.length;i++){
			ArrWidth[i]=80;
		}
		if(cd.DataTable.getCols()>=2){
			ArrWidth[0]=150;
			ArrWidth[1]=40;
		}
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero=false;
		rt.setTitle(getBiaotmc()+"<br>ú̿�볧��ú����ͳ��(���̱�)", ArrWidth);
		rt.title.setRowHeight(2, 50);
//			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3,titdate,Table.ALIGN_RIGHT);
		rt.body.setPageRows(22);
		for (int i=2;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		} 
		if(rt.body.getRows()>0 && rt.body.getCols()>0){
			rt.body.setCellValue(1, 1, "��λ");
		}
		rt.body.ShowZero=false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
//		rt.setDefautlFooter(7,1,"��λ:Ԫ/��",Table.ALIGN_RIGHT);
		cd.DataTable.setColAlign(2, Table.ALIGN_CENTER );
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	
//	�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				 biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return biaotmc;
		
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
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			this.setTreeid(null);
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean2(null);
					visit.setProSelectionModel2(null);
					visit.setDropDownBean3(null);
					visit.setProSelectionModel3(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					visit.setDropDownBean10(null);
					visit.setProSelectionModel10(null);
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
		
		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BAOBLX");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
//		ToolbarButton tbLink = new ToolbarButton(null,"��ӡ","function(){}");
		
		tb1.addItem(tb);
//		tb1.addItem(tbLink);
		setToolbar(tb1);
	}
	
//	 ͳ�ƿھ�
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getIBaoblxModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean10() != null) {
			id = ((Visit)getPage().getVisit()).getDropDownBean10().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getIBaoblxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getIBaoblxModels() {
		
		List List = new ArrayList();
		List.add(new IDropDownBean(0,"�ֳ���˰"));
		List.add(new IDropDownBean(1,"�ֳ�����˰"));
		List.add(new IDropDownBean(2,"�ֿ�˰"));
		List.add(new IDropDownBean(3,"�ֿ󲻺�˰"));
		List.add(new IDropDownBean(4,"��ú�������̱�"));
		
		((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel(List));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
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
	
	
}

