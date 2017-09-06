package com.zhiren.jt.zdt.shengcdy.ranlscddkb;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import org.apache.tapestry.contrib.palette.SortMode;

/**
 * ����:sy
 * ʱ��:2009-9-11
 * �޸�����:�޸Ĳ��ܲ���ɶ��ͽ�Ԫ���ݵ�sql����
 *
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-28 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Ranlscddkbreport  extends BasePage implements PageValidateListener{
	
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
	
//	 ��ʼ����
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
		}
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		getSelectData();
	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			setBaoblxValue(null);
			getIBaoblxModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
			isBegin=true;
			this.getSelectData();
		}

		
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="Ranlsckb";
	private String mstrReportName="Ranlsckb";//ȼ�������챨
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "�޴˱���";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private String FormatDate1(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
	private boolean isBegin=false;
	private boolean checkXitszKyts() {
		// TODO �Զ����ɷ������
		// ���ϵͳ�����е�"�ɵ��������˷�"����
		JDBCcon con = new JDBCcon();
		try {
			String zhi = "";

			String sql = "select zhi from xitxxb where mingc='���ú�����������㷽��' and zhuangt=1";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				zhi = rs.getString("zhi");
			}

			if (zhi.trim().equals("���ƹ���")) {

				return true;
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return false;
	}
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		
		String riq=DateUtil.FormatDate(this.getBeginriqDate());
		String riq1=FormatDate1(this.getBeginriqDate());
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		
		int tians=DateUtil.getDay(this.getBeginriqDate());
		
		int jib=this.getDiancTreeJib();
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		//��������
		//˵��:ȼ���������ȿ챨�ļƻ����վ��ƻ���ֵֻ�ж���hetslb��״̬,û���ж�hetb������״̬,����Ժ���Ҫ���Լ���.
		
		StringBuffer grouping_sql = new StringBuffer();
		StringBuffer where_sql = new StringBuffer();
		StringBuffer rollup_sql = new StringBuffer();
		StringBuffer having_sql = new StringBuffer();
		StringBuffer orderby_sql = new StringBuffer();
		
		StringBuffer strSQL = new StringBuffer();
		
		
		if (jib==1) {//ѡ����ʱˢ�³����еĵ糧
			grouping_sql.append(" select decode(grouping(dc.mingc)+grouping(dc.fengs),2,'�ܼ�',1,dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n");
			
			where_sql.append(" ");
			rollup_sql.append(" group by rollup (dc.fengs,dc.mingc) ");
			having_sql.append("");
			orderby_sql.append("  order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc ,max(dc.xuh1) ");
		}else if(jib==2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			
			try{
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//ȼ�Ϲ�˾
					grouping_sql.append(" select decode(grouping(dc.quygs)+grouping(dc.fengs)+grouping(dc.mingc),3,'�ܼ�',\n");
					grouping_sql.append("2,dc.quygs,1,'&nbsp;&nbsp;'||dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n");
					
					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.quygs,dc.fengs,dc.mingc) ");
					having_sql.append(" having not grouping(dc.quygs)=1\n");
					orderby_sql.append(" order by grouping(dc.quygs) desc,dc.quygs,grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,max(dc.xuh1)\n ");
				}else{
					grouping_sql.append(" select decode(grouping(dc.fengs)+grouping(dc.mingc),2,'�ܼ�',\n");
					grouping_sql.append("1,dc.fengs,'&nbsp;&nbsp;'||dc.mingc) as danw,\n");
					
					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.fengs,dc.mingc) ");
					having_sql.append(" having not grouping(dc.fengs)=1\n");
					orderby_sql.append(" order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,max(dc.xuh1)\n ");
				}
				rl.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			
		}else{//ѡ��糧
			grouping_sql.append(" select decode(grouping(dc.mingc),1,'�ܼ�',\n");
			grouping_sql.append("dc.mingc) as danw,\n");
			
			where_sql.append(" and dc.id=").append(this.getTreeid()).append("\n");
			rollup_sql.append(" group by rollup (dc.mingc) \n");
			having_sql.append(" having not grouping(dc.mingc)=1\n");
			orderby_sql.append(" order by grouping(dc.mingc) desc,max(dc.xuh1)\n ");
		}
		
			strSQL.append(grouping_sql.toString());
			strSQL.append("sum(htjh.jih) as jih,sum(htjh.rijjh) as rijh,sum(dr.dangrgm) as dangrgm,\n");
			strSQL.append("sum(lj.leijgm) as leijgm,sum(dr.haoyqkdr) as dangrhy,sum(lj.leijhy) as leijhy,\n");
			strSQL.append("(nvl(sum(lj.leijgm),0)-sum(htjh.rijjh)*"+tians+") as jihljc,sum(dr.kuc) as kuc,\n");
			
			if(checkXitszKyts()){
				strSQL.append("decode(grouping(dc.mingc),1,'',sum(decode(hm.meizrjhm, 0, 0, round(h.kuc / hm.meizrjhm)))) as keyts\n");
			}else{
				strSQL.append("decode(grouping(dc.mingc),1,round(sum(dr.kuc)/sum(dr.rizdhml),1),\n");
				strSQL.append("round(keyts_rb(max(dr.diancxxb_id),to_date('"+riq+"','yyyy-mm-dd')),1))  as keyts\n");
			}
			
			strSQL.append("from\n");
			strSQL.append("(select y.diancxxb_id,sum(y.yuejhcgl) as jih,\n");
			strSQL.append("Round(sum(y.yuejhcgl)/daycount(to_date('"+riq+"', 'yyyy-mm-dd')),0) as rijjh\n");
			strSQL.append("from yuecgjhb y\n");
			strSQL.append("where y.riq=First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n");
			strSQL.append("group by y.diancxxb_id ) htjh,\n");
			strSQL.append("(select s.diancxxb_id, sum(s.dangrgm) as dangrgm, sum(s.haoyqkdr) as haoyqkdr, sum(s.kuc) as kuc,\n");
			strSQL.append("sum(nvl(keyts_rijhm(s.diancxxb_id,s.haoyqkdr,s.riq),0)) as rizdhml\n");
			strSQL.append("from shouhcrbb s\n");
			strSQL.append("where s.riq = to_date('"+riq+"', 'yyyy-mm-dd') group by s.diancxxb_id) dr,\n");
			strSQL.append("( select s.diancxxb_id,\n");
			strSQL.append(" sum(s.dangrgm) as leijgm,\n");
			strSQL.append(" sum(s.haoyqkdr) as leijhy\n");
			strSQL.append(" from shouhcrbb s\n");
			strSQL.append(" where s.riq >= First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n");
			strSQL.append(" and s.riq <= to_date('"+riq+"', 'yyyy-mm-dd')\n");
			strSQL.append(" group by (s.diancxxb_id)) lj,\n");
			
			if(checkXitszKyts()){
			strSQL.append("  ( select d.id,d.xuh as xuh1,d.mingc,d.jingjcml ,d.rijhm,dc.mingc as fengs,sf.id as quygsid,sf.mingc as quygs,d.xuh,dc.id as fuid,d.shangjgsid\n");
			strSQL.append(" from diancxxb d, diancxxb dc ,diancxxb sf\n");
			strSQL.append(" where d.jib = 3\n");
			strSQL.append(" and d.fuid=dc.id  and d.shangjgsid=sf.id(+)) dc,\n");	
			
			strSQL.append(" (select *\n" ); 
			strSQL.append(" from shouhcrbb h\n" );
			strSQL.append(" where h.riq = to_date('"+riq+"', 'yyyy-mm-dd')) h,\n");
			
			strSQL.append(" (select hc.diancxxb_id,\n" );
			strSQL.append(" nvl(round(sum(hc.haoyqkdr) / 7), 0) as meizrjhm\n" );
			strSQL.append(" from shouhcrbb hc\n" ); 
			strSQL.append(" where hc.riq >= to_date('"+riq+"', 'yyyy-mm-dd') - 6\n" );
			strSQL.append(" and hc.riq <= to_date('"+riq+"', 'yyyy-mm-dd')\n");
			strSQL.append(" group by (hc.diancxxb_id)) hm\n");
			}else{
				strSQL.append("  ( select d.id,d.xuh as xuh1,d.mingc,d.jingjcml ,d.rijhm,dc.mingc as fengs,sf.id as quygsid,sf.mingc as quygs,d.xuh,dc.id as fuid,d.shangjgsid\n");
				strSQL.append(" from diancxxb d, diancxxb dc ,diancxxb sf\n");
				strSQL.append(" where d.jib = 3\n");
				strSQL.append(" and d.fuid=dc.id  and d.shangjgsid=sf.id(+)) dc \n");
			}
			
			strSQL.append(" where dc.id=htjh.diancxxb_id(+)\n");
			strSQL.append(" and dc.id=dr.diancxxb_id(+)\n");
			strSQL.append(" and dc.id=lj.diancxxb_id(+) \n");
			if(checkXitszKyts()){
			strSQL.append(" and dc.id = h.diancxxb_id(+)\n");
			strSQL.append(" and dc.id = hm.diancxxb_id(+) \n");
			}
			strSQL.append(where_sql.toString());
			strSQL.append(rollup_sql.toString());
			strSQL.append(having_sql.toString());
			strSQL.append(orderby_sql.toString());
			
		
			// ֱ���ֳ�����
			ArrHeader=new String[2][10];
			ArrHeader[0]=new String[] {"�糧����","�ƻ�","�վ��ƻ�","ʵ�ʹ�ú","ʵ�ʹ�ú","����","����","��ƻ��ۼƲ�","���","���ú��������"};
			ArrHeader[1]=new String[] {"�糧����","�ƻ�","�վ��ƻ�","����","�ۼ�","����","�ۼ�","��ƻ��ۼƲ�","���","���ú��������"};
		 
			ArrWidth=new int[] {140,60,65,65,65,65,65,75,65,80};
			
			//System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL.toString());
			 
			// ����
			Table tb=new Table(rs,2, 0, 1);
			rt.setBody(tb);
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			rt.setTitle("ȼ��(ú̿)�������ȿ챨", ArrWidth);
			rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 4,  riq1, Table.ALIGN_CENTER);
			rt.setDefaultTitle(9, 2, "��λ:��", Table.ALIGN_RIGHT);
			
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(rt.PAPER_ROWS);
//			���ӳ��ȵ�����
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			
			if(jib==1){
				if(rt.body.getRows()>2){
					rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
				}
			}
			
			
			//ҳ�� 
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(5,2,"���:",Table.ALIGN_CENTER);
			  if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
					rt.setDefautlFooter(9, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
					}else{
						
					rt.setDefautlFooter(9, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
					}
		  //  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
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
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			if(diancmc.equals("���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���")){
		          return "���ƹ���ȼ�Ϲ���";
			}else{
				return diancmc;
			}
			
		
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
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}
	
	
//	�������
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
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
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{
//		List fahdwList = new ArrayList();
//		fahdwList.add(new IDropDownBean(-1,"��ѡ��"));
//		
//		String sql="";
//		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
////		System.out.println(sql);
//		ResultSet rs = con.getResultSet(sql);
//		for(int i=0;rs.next();i++){
//			fahdwList.add(new IDropDownBean(i,rs.getString("meikdqmc")));
//		}
		
		String sql="";
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
	}
	
//	�󱨱�����
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"�ֳ�����"));
		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
//	���
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
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * �·�
	 */
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
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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


	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	***************************�����ʼ����***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
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
			rs.close();
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
			rs.close();
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
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		tb1.addField(df);
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
		
		
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
		
		
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

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}
}