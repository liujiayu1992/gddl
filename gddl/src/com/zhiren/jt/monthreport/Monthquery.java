package com.zhiren.jt.monthreport;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
/*
 * ���ߣ�����
 * ʱ�䣺2010-01-21
 * �������±�16-1������м�����������
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Monthquery extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
	
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
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
	
//	***************������Ϣ��******************//
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
	
	private String RT_DR16="diaor16bb";//��ȼ16��
	private String RT_DR03="diaor03bb";//��ȼ03��
	private String RT_DR04="diaor04bb";//��ȼ04��
	private String RT_DR08="diaor08bb";//��ȼ08��
	private String RT_DR02="diaor02bb";//��ȼ02��
	private String RT_DR01="diaor01bb";//��ȼ01��
	
	private String mstrReportName="";
	
	public String getTianzdwQuanc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "��ϵ糧";
		}else{
			return getTianzdwQuanc(Long.parseLong(str[0]));
		}
	}
	
	public long getDiancxxbId(){
		
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	public boolean isJTUser(){
		return ((Visit)getPage().getVisit()).isJTUser();
	}
	//�õ���λȫ��
	public String getTianzdwQuanc(long gongsxxbID){
		String _TianzdwQuanc="";  
		JDBCcon cn = new JDBCcon();
		
		try {
			ResultSet rs=cn.getResultSet(" select quanc from diancxxb where id="+gongsxxbID);
			while (rs.next()){
				_TianzdwQuanc=rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	private boolean blnIsBegin = false;
	private String leix="";
	public String getPrintTable(){
		setMsg(null);
		if(!blnIsBegin){
			return "";
		}
		blnIsBegin=false;
		
		if (mstrReportName.equals(RT_DR16)){

			return getDiaor16();
		}else if(mstrReportName.equals(RT_DR01)){

			return  getDiaor01();
		}else if(mstrReportName.equals(RT_DR03)){

			return  getDiaor03();
		}else if(mstrReportName.equals(RT_DR04)){

			return  getDiaor04();
		}else if(mstrReportName.equals(RT_DR08)){

			return getDiaor08();
		}else if(mstrReportName.equals(RT_DR02)){
			return getDiaor02();
		}else{	
			return "�޴˱���";
		}
	}
	
	private String getDiancCondition(){
		/*JDBCcon cn = new JDBCcon();
		String diancxxb_id=getTreeid();
		String condition ="";
		ResultSet rs=cn.getResultSet("select jib,id,fuid from diancxxb where id=" +diancxxb_id);
		try {
			if (rs.next()){
				if( rs.getLong("jib")==SysConstant.JIB_JT){
					condition="";
				}else if(rs.getLong("jib")==SysConstant.JIB_GS){
					condition=" and dc.fuid=" +diancxxb_id;
				}else {
					condition=" and dc.id=" +diancxxb_id;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		return " and dc.id in ("+getTreeid()+")";
	}
	
	private String getGongysCondition(){
		if (getMeikdqmcValue().getId()==-1){
			return "";
		}else{
			return " and dq.id=" +getMeikdqmcValue().getId();
		}
	}
	
	private String getDiaor01(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		long lngDiancId=getDiancxxbId();//�糧��Ϣ��id
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��";
		
		sbsql.append("select  (decode(grouping(dc.mingc)+grouping(fgs.mingc),2,'�ܼ�',1, fgs.mingc,dc.mingc)) as �糧,   \n");
		sbsql.append("       sum(fadsbrl) as �����豸����, fenx as ���¼��ۼ�, sum(meitsg) as ú̿ʵ��,  \n");
		sbsql.append("       sum(meithyhj) as ú̿���úϼ�, sum(meithyfd) as ú̿���÷���, sum(meithygr) as ú̿���ù���,  \n");
		sbsql.append("       sum(meithyqt) as ú̿��������, sum(meithysh) as ú̿�������, decode(fenx,'�ۼ�','-',sum(meitkc)) as ú̿���,  \n");
		sbsql.append("       sum(shiysg) as ʯ��ʵ��, sum(shiyhyhj) as ʯ�ͺ��úϼ�, sum(shiyhyfd) as ʯ�ͺ��÷���, sum(shiyhygr) as ʯ�ͺ��ù���,  \n");
		sbsql.append("       sum(shiyhyqt) as ʯ�ͺ�������, sum(shiyhysh) as ʯ�ͺ������, decode(fenx,'�ۼ�','-',sum(shiykc)) as ʯ�Ϳ��,  \n");
		sbsql.append("       round(sum(fadl)) as ������,  sum(gongrl) as ������,  \n");
		sbsql.append("       case when sum(fadl) = 0 then 0 else round(sum(biaozmlfd) / sum(fadl) * 100, 0) end as ��׼ú�ķ���,  \n");
		sbsql.append("       case when sum(gongrl) = 0 then 0 else round(sum(biaozmlgr) / sum(gongrl) * 1000, 1) end as ��׼ú�Ĺ���,  \n");
		sbsql.append("       case when sum(fadl) = 0 then 0 else round((nvl(sum(shiyhyfd),0) * 2 + nvl(sum(meithyfd),0)) / sum(fadl) * 100, 0)  end as ��Ȼú�ķ���,  \n");
		sbsql.append("       case when sum(gongrl) = 0 then 0 else round((nvl(sum(shiyhygr),0) * 2 + nvl(sum(meithygr),0)) / sum(gongrl) * 1000, 1) end as ��Ȼú�Ĺ���,  \n");
		sbsql.append("       round(sum(biaozmlfd)) as ��׼ú������, sum(biaozmlgr) as ��׼ú������,  \n");
		sbsql.append("       fun_zonghrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as �ۺ�ȼ��,  \n");
		sbsql.append("       fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) As ��ȼ��  \n");
		sbsql.append("from diaor01bb dr,diancxxb dc,dianclbb lx,(select * from dianckjpxb where kouj='�±�')  px,vwfengs  fgs \n");
		sbsql.append("        where riq=to_date('"+strDate+"','yyyy-mm-dd')");
		sbsql.append("      and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("      and dr.diancxxb_id=dc.id(+)   \n");
		sbsql.append("      and dc.dianclbb_id=lx.id \n").append(getDiancCondition());
		sbsql.append("      and dc.fuid=fgs.id \n");
		sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc)    \n");
		sbsql.append(" having(grouping(dr.fenx)=0)   \n");
		sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh), grouping(dc.mingc) desc, \n");
		sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		//�����ͷ����
		 String ArrHeader[][]=new String[4][27];
		 ArrHeader[0]=new String[] {"��λ����","����<br>�豸<br>����<br>(����)","����<br>��<br>�ۼ�","ú̼","ú̼","ú̼","ú̼","ú̼","ú̼","ú̼","ʯ��","ʯ��","ʯ��","ʯ��","ʯ��","ʯ��","ʯ��","ʵ�����","ʵ�����","ú��","ú��","ú��","ú��","��׼ú��","��׼ú��","������","������"};
		 ArrHeader[1]=new String[] {"��λ����","����<br>�豸<br>����<br>(����)","����<br>��<br>�ۼ�","ʵ��<br>(��)","����","����","����","����","����","���<br>(��)","ʵ��<br>(��)","����","����","����","����","����","���<br>(��)","����<br>(��ǧ��ʱ)","����<br>(����)","��׼ú��","��׼ú��","��Ȼú��","��Ȼú��","����(��)","����(��)","�ۺ�ȼ��<br>(�׽�/ǧ��)","��ȼ��<br>(�׽�/ǧ��)"};
		 ArrHeader[2]=new String[] {"��λ����","����<br>�豸<br>����<br>(����)","����<br>��<br>�ۼ�","ʵ��<br>(��)","С��<br>(��)","����<br>(��)","����<br>(��)","����<br>(��)","���<br>(��)","���<br>(��)","ʵ��<br>(��)","С��<br>(��)","����<br>(��)","����<br>(��)","����<br>(��)","���<br>(��)","���<br>(��)","����<br>(��ǧ��ʱ)","����<br>(����)","����<br>(��/ǧ��ʱ)","����<br>(ǧ��/����)","����<br>(��/ǧ��ʱ)","����<br>(ǧ��/����)","����(��)","����(��)","�ۺ�ȼ��<br>(�׽�/ǧ��)","��ȼ��<br>(�׽�/ǧ��)"};
		 ArrHeader[3]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"};
//		�п�
		 int ArrWidth[]=new int[] {60,40,30,50,50,60,50,50,50,60,45,45,45,45,45,45,40,50,50,40,50,40,50,50,50,50,50};
		
		 
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		 //����ҳ����
		rt.setTitle("������ú̼��Ӧ������������ܱ�("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,5,"�Ʊ�λ:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(13,2,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(24,4,"��λ:�֡����ߡ��������׽�/ǧ��",Table.ALIGN_RIGHT);
	//	rt.setDefaultTitle(rt.title.getCols()-1,2,"��ȼ01��",Table.ALIGN_RIGHT);
		
		//����
		rt.setBody(new Table(rs,4,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.ShowZero=false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.setColAlign(2,Table.ALIGN_RIGHT);
		rt.body.setColFormat(4,"0");
		rt.body.setColFormat(5,"0");
		rt.body.setColFormat(6,"0");
		rt.body.setColFormat(7,"0");
		rt.body.setColFormat(8,"0");
		rt.body.setColFormat(9,"0");
		rt.body.setColFormat(10,"0");
		rt.body.setColFormat(11,"0");
		rt.body.setColFormat(12,"0");
		rt.body.setColFormat(13,"0");
		rt.body.setColFormat(14,"0");
		rt.body.setColFormat(15,"0");
		rt.body.setColFormat(16,"0");
		rt.body.setColFormat(17,"0");
		rt.body.setColFormat(21,"0.0");
		rt.body.setColFormat(23,"0.0");
		rt.body.setColFormat(26,"0.00");
		rt.body.setColFormat(27,"0.00");	
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,3,"�Ʊ�ʱ��:"+strDate,Table.ALIGN_LEFT);
		rt.setDefautlFooter(12,2,"�Ʊ�:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(26,2,"���:",Table.ALIGN_RIGHT);
	//  rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();	
	}
	
	private String getDiaor03(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��";
		String strDanw="";
		String strMeik="";
		boolean blnIsFcFk=false;
		
		sbsql.append("		fenx as ���¼��ۼ�,");
		sbsql.append("       sum(jincsl) as ��������, sum(choucsl) as �������,  \n");
		sbsql.append("       decode(sum(jincsl),0,0,round(sum(choucsl)/sum(jincsl)*100,2)) as ռ����ú , \n");
		sbsql.append("       (case when sum(jincsl)>0 then round(sum(jincsl * guoh) /sum(jincsl),2) else 0 end) as ����,  \n");
		sbsql.append("       100-(case when sum(jincsl)>0 then round(sum(jincsl * guoh) /sum(jincsl),2) else 0 end) as ���,  \n");
		sbsql.append("       sum(yingdsl) as ӯ������,  sum(yingdzje) as ӯ���۽��,  \n");
		sbsql.append("       sum(kuid) as ����,  sum(kuidzje) as �����۽��,  \n");
		sbsql.append("       sum(suopje) as ������, \n");
		sbsql.append("     max(shuom) as ˵��  \n");
		sbsql.append(" from diaor03bb dr,gongysb dq,diancxxb dc,vwfengs  fgs,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='�±�')  px \n");
		sbsql.append("        where dr.riq=to_date('"+strDate+"','yyyy-mm-dd')   \n");
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(getDiancCondition());
		sbsql.append("         and dc.fuid=fgs.id \n").append(getGongysCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+)  \n");
		
		String strSqlBody=sbsql.toString();
		sbsql.setLength(0);
		
		if (getSelzhuangtValue().getValue().equals("�ֿ�")) {
			strDanw="���";
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'�ܼ�',1,sf.quanc||'�ϼ�',dq.mingc)) as ú������,   \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,sf.quanc,dq.mingc)    \n");
			sbsql.append(" having(grouping(dr.fenx)=0)   \n");
			sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
		}else if (getSelzhuangtValue().getValue().equals("�ֳ�")){
			strDanw="��λ";
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(fgs.mingc),2,'�ܼ�',1, fgs.mingc,dc.mingc)) as �糧,   \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc)    \n");
			sbsql.append(" having(grouping(dr.fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc, grouping(dc.mingc) desc, \n");
			sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
		}else if (getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
			strDanw="���";
			strMeik="��λ";
			blnIsFcFk=true;
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'�ܼ�',dq.mingc) as ú��, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'С��',dc.mingc) as �糧, \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,dq.mingc,dc.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx    \n");

		}else if(getSelzhuangtValue().getValue().equals("�ֳ��ֿ�")){
			strDanw="��λ";
			strMeik="���";
			blnIsFcFk=true;
			sbsql.append("select decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),3,'�ܼ�',2,fgs.mingc,dc.mingc) as �糧, \n");
			sbsql.append("decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),2,'�ֹ�˾С��',1,'�糧С��',dq.mingc) as ú��, \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc,dq.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(fenx) desc,fenx    \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//�����ͷ����
		String ArrHeader[][];
		int ArrWidth[];
		
		if (!blnIsFcFk){
			 ArrHeader=new String[4][13];
			 ArrHeader[0]=new String[] {strDanw,"����<br>��<br>�ۼ�","��������<br>(��)","         �����������","         �����������","         �����������","         �����������","ӯ�����","ӯ�����","ӯ�����","ӯ�����","������<br>(Ԫ)","˵��"};
			 ArrHeader[1]=new String[] {strDanw,"����<br>��<br>�ۼ�","��������<br>(��)","      ����","      ����","      ����","      ����","ӯú<br>(��)","ӯú���<br>(Ԫ)","����<br>(��)","���ֽ��<br>(Ԫ)","������<br>(Ԫ)","˵��"};
			 ArrHeader[2]=new String[] {strDanw,"����<br>��<br>�ۼ�","��������<br>(��)","(��)","%","����%","���%","ӯú<br>(��)","ӯú���<br>(Ԫ)","����<br>(��)","���ֽ��<br>(Ԫ)","������<br>(Ԫ)","˵��"};
			 ArrHeader[3]=new String[] {"��","��","1","2","3","4","5","6","7","8","9","10","11"};
			 ArrWidth=new int[] {100,40,60,60,60,60,60,60,80,60,80,80,120};
		}else{
			 ArrHeader=new String[4][14];
			 ArrHeader[0]=new String[] {strDanw,strMeik,"����<br>��<br>�ۼ�","��������<br>(��)","�����������","�����������","�����������","�����������","ӯ�����","ӯ�����","ӯ�����","ӯ�����","������<br>(Ԫ)","˵��"};
			 ArrHeader[1]=new String[] {strDanw,strMeik,"����<br>��<br>�ۼ�","��������<br>(��)","����","����","����","����","ӯú<br>(��)","ӯú���<br>(Ԫ)","����<br>(��)","���ֽ��<br>(Ԫ)","������<br>(Ԫ)","˵��"};
			 ArrHeader[2]=new String[] {strDanw,strMeik,"����<br>��<br>�ۼ�","��������<br>(��)","(��)","%","����%","���%","ӯú<br>(��)","ӯú���<br>(Ԫ)","����<br>(��)","���ֽ��<br>(Ԫ)","������<br>(Ԫ)","˵��"};
			 ArrHeader[3]=new String[] {"��","��","��","1","2","3","4","5","6","7","8","9","10","11"};
			 ArrWidth=new int[] {100,150,35,70,70,40,40,40,55,80,55,80,70,120};
		}
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		
		//����ҳ����
		rt.setTitle("����ú����ӯ���±���("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,5,"�Ʊ�λ:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(7,3,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(12,2,"��λ:��",Table.ALIGN_RIGHT);
	//	rt.setDefaultTitle(rt.title.getCols()-1,2,"��ȼ03��",Table.ALIGN_RIGHT);
		
		//����
		rt.setBody(new Table(rs,4,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.ShowZero=reportShowZero();
		rt.body.mergeFixedRow();
		
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (blnIsFcFk){
				rt.body.mergeFixedCol(2);
			}else{
				rt.body.mergeFixedCol(1);
			}
		}
		
		int iFormatCol=5;
		if(blnIsFcFk){
			iFormatCol=6;
		}
		rt.body.setColFormat(iFormatCol,"0.00");
		rt.body.setColFormat(iFormatCol+1,"0.00");
		rt.body.setColFormat(iFormatCol+2,"0.00");
		rt.body.setColFormat(iFormatCol+4,"0.00");
		rt.body.setColFormat(iFormatCol+6,"0.00");
		rt.body.setColFormat(iFormatCol+7,"0.00");
		rt.body.setColFormat(iFormatCol+8,"0.00");
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+strDate,Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,2,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				rt.setDefautlFooter(12, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
				rt.setDefautlFooter(12, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(12, 2,"�Ʊ�:",Table.ALIGN_RIGHT);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();	
	}
	
	private String getDiaor04(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��";
		String strDanw="";
		String strMeik="";
		boolean blnIsFcFk=false;
		
		sbsql.append(" Ʒ��,���¼��ۼ�,JINCSL as ��������,YANSSL as ��������,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(YANSSL/JINCSL*100,2)) as ������,    \n");
		sbsql.append("    decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2)) as ������,   \n");
		sbsql.append("    dengji(decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2))) as �󷽵ȼ�,  \n");
		sbsql.append("    decode(KUANGFSFsl,0, 0,round(KUANGFSF/KUANGFSFsl,2)) as ��ˮ��,    \n");
		sbsql.append("    decode(KUANGFHFsl,0,0 ,round(KUANGFHF/KUANGFHFsl,2)) as �󷽻ҷ�,    \n");
		sbsql.append("    decode(KUANGFHFFsl,0,0,round(KUANGFHFF/KUANGFHFFsl,2)) as �󷽻ӷ���,    \n");
		sbsql.append("    decode(KUANGFLFsl,0,0,round(KUANGFLF/KUANGFLFsl,2)) as �����,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(FARL/JINCSL,2)) as ����,    \n");
		sbsql.append("    dengji(decode(JINCSL,0,0,round(FARL/JINCSL,2))) as �ȼ�,  \n");
		sbsql.append("    decode(JINCSL,0,0,round(SHUIF/JINCSL,2)) as ˮ��,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(HUIF/JINCSL,2)) as �ҷ�,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(HUIFF/JINCSL,2)) as �ӷ���,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(LIUF/JINCSL,2)) as ���,    \n");
		sbsql.append("    (dengji(decode(JINCSL,0,0,round(FARL/JINCSL,2)))-dengji(decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2))))*2 as �ȼ���,  \n");
		sbsql.append("    decode(JINCSL,0,0,round(FARL/JINCSL,2))-decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2)) as ������,  \n");
		sbsql.append("    decode(JINCSL,0,0,round(BUFL/JINCSL,2)) as ������,    \n");
		sbsql.append("    decode(BUFL,0,0,round(DANJC/BUFL,2)) as ���۲�,  \n");
		sbsql.append("    ZONGJE as �ܽ��,SUOPJE as ������  \n");
		sbsql.append(" from (  \n");
		
		String strSqlBody1=sbsql.toString();
		sbsql.setLength(0);
		sbsql.append("        (decode(grouping(pz.mingc) + grouping(dc.mingc),1,'С��',pz.mingc)) as Ʒ��,fenx as ���¼��ۼ�,  \n");
		sbsql.append("        sum(JINCSL) as JINCSL,sum(YANSSL) as YANSSL ,    \n");
		sbsql.append("        sum(JINCSL * KUANGFFRL) as KUANGFFRL,sum(decode(nvl(KUANGFFRL,0),0,0,JINCSL)) as KUANGFFRLsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFSF)as KUANGFSF,   sum(decode(nvl(KUANGFSF,0),0,0,JINCSL))  as KUANGFSFsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFHF) as KUANGFHF,  sum(decode(nvl(KUANGFHF,0),0,0,JINCSL))  as KUANGFHFsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFHFF) as KUANGFHFF,sum(decode(nvl(KUANGFHFF,0),0,0,JINCSL)) as KUANGFHFFsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFLF) as KUANGFLF,  sum(decode(nvl(KUANGFLF,0),0,0,JINCSL))  as KUANGFLFsl,    \n");
		sbsql.append("        sum(JINCSL * CHANGFFRL) as FARL,    \n");
		sbsql.append("        sum(JINCSL * CHANGFSF)  as SHUIF,    \n");
		sbsql.append("        sum(JINCSL * CHANGFHF)  as HUIF,    \n");
		sbsql.append("        sum(JINCSL * CHANGFHFF) as HUIFF,    \n");
		sbsql.append("        sum(JINCSL * CHANGFLF)  as LIUF,    \n");
		sbsql.append("        sum(JINCSL * BUFL)      as BUFL,    \n");
		sbsql.append("        sum(JINCSL * DANJC*BUFL)     as DANJC,  \n");
		sbsql.append("        sum(ZONGJE) as ZONGJE,sum(SUOPJE) as SUOPJE,  \n");
		sbsql.append("        sum(RELSP) as RELSP,sum(KUIKSPSL) as KUIKSPSL,  \n");
		sbsql.append("        sum(LIUSP) as LIUSP,sum(LIUSPSL)  as LIUSPSL    \n");
		sbsql.append(" from diaor04bb dr,gongysb dq,diancxxb dc,vwfengs  fgs,dianclbb lx,shengfb sf , pinzb pz,(select * from dianckjpxb where kouj='�±�')  px \n");
		sbsql.append("        where dr.riq=to_date('"+strDate+"','yyyy-mm-dd')   \n");
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(getDiancCondition());
		sbsql.append("         and dc.fuid=fgs.id \n").append(getGongysCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+) and dr.pinz = pz.id(+)  \n");
		
		String strSqlBody2=sbsql.toString();;
		sbsql.setLength(0);
		if (getSelzhuangtValue().getValue().equals("�ֿ�")) {
			strDanw="���";
			sbsql.append("select  ú��,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'�ܼ�',1,sf.quanc||'�ϼ�',dq.mingc)) as ú��,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,sf.quanc,dq.mingc,pz.mingc)    \n");
			sbsql.append(" having(grouping(dr.fenx)=0)   \n");
			sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(pz.mingc) desc,pz.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			sbsql.append(")   \n");
		}else if (getSelzhuangtValue().getValue().equals("�ֳ�")){
			strDanw="��λ";
			 
			sbsql.append("select �糧,").append(strSqlBody1);;
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(fgs.mingc),2,'�ܼ�',1, fgs.mingc,dc.mingc)) as �糧,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc,pz.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(pz.mingc) desc,pz.mingc ,grouping(fenx) desc,fenx   \n");
			sbsql.append(")   \n");
		}else if (getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
			strDanw="���";
			strMeik="��λ";
			blnIsFcFk=true;
			
			sbsql.append("select ú��,�糧,").append(strSqlBody1);
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'�ܼ�',dq.mingc) as ú��, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'С��',dc.mingc) as �糧, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,dq.mingc,dc.mingc,pz.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(pz.mingc) desc,pz.mingc ,grouping(fenx) desc,fenx   \n");
			
			sbsql.append(")  \n");
		}else if(getSelzhuangtValue().getValue().equals("�ֳ��ֿ�")){
			strDanw="��λ";
			strMeik="���";
			blnIsFcFk=true;
			sbsql.append("select �糧,ú��,").append(strSqlBody1);;
			sbsql.append("select decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),3,'�ܼ�',2,fgs.mingc,dc.mingc) as �糧, \n");
			sbsql.append("decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),2,'�ֹ�˾С��',1,'�糧С��',dq.mingc) as ú��, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc,dq.mingc,pz.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(pz.mingc) desc,pz.mingc,grouping(fenx) desc,fenx    \n");
			sbsql.append(")  \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//�����ͷ����
		String ArrHeader[][];
		int ArrWidth[];
		
		if (getSelzhuangtValue().getValue().equals("�ֳ�") || getSelzhuangtValue().getValue().equals("�ֿ�")){
			 ArrHeader=new String[3][24];
			 ArrHeader[0]=new String[] {strDanw,"Ʒ��","����<br>��<br>�ۼ�","��������","��������","��������","�󷽻���","�󷽻���","�󷽻���","�󷽻���","�󷽻���","�󷽻���","��������","��������","��������","��������","��������","��������","�ȼ���","��ֵ��","�ʼ۲������","�ʼ۲������","�ʼ۲������","������<br>(Ԫ)"};
			 ArrHeader[1]=new String[] {strDanw,"Ʒ��","����<br>��<br>�ۼ�","����ú��<br>(��)","����ú��<br>(��)","������%","Qnet, ar%","�ȼ�","Mt%","Aar%","Vdaf%","St,d%","Qnet, ar%","�ȼ�","Mt%","Aar%","Vdaf%","St,d%","�ȼ�","Qnet, ar%","%","���۲�<br>(Ԫ/��)","�ܽ��<br>(Ԫ)","������<br>(Ԫ)"};
			 ArrHeader[2]=new String[] {"��","��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};
			 ArrWidth=new int[] {100,40,34,60,60,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,50,85,85};
		}else{
			 ArrHeader=new String[3][25];
			 ArrHeader[0]=new String[] {strDanw,strMeik,"Ʒ��","����<br>��<br>�ۼ�","��������","��������","��������","�󷽻���","�󷽻���","�󷽻���","�󷽻���","�󷽻���","�󷽻���","��������","��������","��������","��������","��������","��������","�ȼ���","��ֵ��","�ʼ۲������","�ʼ۲������","�ʼ۲������","������<br>(Ԫ)"};
			 ArrHeader[1]=new String[] {strDanw,strMeik,"Ʒ��","����<br>��<br>�ۼ�","����ú��<br>(��)","����ú��<br>(��)","������%","Qnet, ar%","�ȼ�","Mt%","Aar%","Vdaf%","St,d%","Qnet, ar%","�ȼ�","Mt%","Aar%","Vdaf%","St,d%","�ȼ�","Qnet, ar%","%","���۲�<br>(Ԫ/��)","�ܽ��<br>(Ԫ)","������<br>(Ԫ)"};
			 ArrHeader[2]=new String[] {"��","��","��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};
			 ArrWidth=new int[] {70,90,40,34,60,60,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,50,85,85};

		}
	
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		//����ҳ����
		rt.setTitle("����ú�������Ƽ�ú����������±�("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,4,"�Ʊ�λ:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(11,3,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(23,2, "��λ:��",Table.ALIGN_RIGHT);
	// rt.setDefaultTitle(rt.title.getCols()-1,2,"��ȼ04��",Table.ALIGN_RIGHT);
		
		//����
		rt.setBody(new Table(rs,3,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.ShowZero=reportShowZero();
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (getSelzhuangtValue().getValue().equals("�ֳ��ֿ�") || getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
				rt.body.mergeFixedCol(2);
			}
			else{
				rt.body.mergeFixedCol(1);
			}
		}
		if(blnIsFcFk){
			rt.body.setColFormat(8,"0.00");
			rt.body.setColFormat(9,"0.0");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.0");
			rt.body.setColFormat(21,"0.00");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(25,"0.00");
			rt.body.setColFormat(24,"0.00");
			
		}else{
			rt.body.setColFormat(7,"0.00");
			rt.body.setColFormat(8,"0.0");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.0");
			rt.body.setColFormat(20,"0.00");
			rt.body.setColFormat(21,"0.00");
			rt.body.setColFormat(23,"0.00");
			rt.body.setColFormat(24,"0.00");
		}

		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+strDate,Table.ALIGN_LEFT);
		rt.setDefautlFooter(11,2,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
			rt.setDefautlFooter(23,2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(23,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(23,2,"�Ʊ�:",Table.ALIGN_RIGHT);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();		
	}
	
	private String getDiaor08(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��";
		String strDanw="";
		String strMeik="";
		boolean blnIsFcFk=false;
		
		sbsql.append("a.���¼��ۼ�,a.ú��, ���+����ǰ���ӷ�+��·�˷�+��·�ӷ�+��վ�ӷ�+��������+ˮ�˷�+ˮ���ӷ�+���˷�+���ӷ� as �����ۺϼ�, \n");
		sbsql.append("        a.���,a.��ֵ˰��,a.��ֵ˰��,a.����ǰ���ӷ�,a.��·�˷�,a.����˰��,a.��·�ӷ�,a.ˮ�˷�,a.ˮ��˰��,a.ˮ���ӷ�,a.���˷�,a.����˰��,a.���ӷ�,a.��վ�ӷ�,a.��������,a.��ֵ, \n");
		sbsql.append("        case when ��ֵ>0 then round((���+����ǰ���ӷ�+��·�˷�+��·�ӷ�+��վ�ӷ�+��������+ˮ�˷�+ˮ���ӷ�+���˷�+���ӷ�)*29.271/��ֵ,2) else 0 end as ��ú����, \n");
		sbsql.append("        case when ��ֵ>0 then round((���+����ǰ���ӷ�+��·�˷�+��·�ӷ�+��վ�ӷ�+��������+ˮ�˷�+ˮ���ӷ�+���˷�+���ӷ�-��ֵ˰��-����˰��-ˮ��˰��-����˰��)*29.271/��ֵ,2) else 0 end as ����˰��ú����, \n");
		sbsql.append("        round((���+����ǰ���ӷ�+��·�˷�+��·�ӷ�+��վ�ӷ�+��������+ˮ�˷�+ˮ���ӷ�+���˷�+���ӷ�)*ú��/10000,2) as ú��  \n");
		sbsql.append("    from ( \n");
		String strSqlBody1=sbsql.toString();
		
		sbsql.setLength(0);
		sbsql.append("        fenx as ���¼��ۼ�,sum(meil) as ú��, \n");
		sbsql.append("        	 case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*kuangj)/sum(meil),2) end as ���,0.13 as ��ֵ˰��, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*zengzse)/sum(meil),2) end as ��ֵ˰��, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*jiaohqyzf)/sum(meil),2) end as ����ǰ���ӷ�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielyf)/sum(meil),2) end as ��·�˷�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tieyse)/sum(meil),2) end as ����˰��, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielzf)/sum(meil),2) end as ��·�ӷ�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyf)/sum(meil),2) end as ˮ�˷�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyse)/sum(meil),2) end as ˮ��˰��, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyzf)/sum(meil),2) end as ˮ���ӷ�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyf)/sum(meil),2) end as ���˷�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyse)/sum(meil),2) end as ����˰��, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*gangzf)/sum(meil),2) end as ���ӷ�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*daozzf)/sum(meil),2) end as ��վ�ӷ�, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qitfy)/sum(meil),2) end as ��������, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*rez)/sum(meil),2) end as ��ֵ \n");
		sbsql.append(" from diaor08bb dr,gongysb dq,diancxxb dc,vwfengs  fgs,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='�±�')  px \n");
		sbsql.append("        where dr.riq=to_date('"+strDate+"','yyyy-mm-dd')   \n");
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(getDiancCondition());
		sbsql.append("         and dc.fuid=fgs.id \n").append(getGongysCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+)  \n");
		
		String strSqlBody2=sbsql.toString();
		sbsql.setLength(0);
		if (getSelzhuangtValue().getValue().equals("�ֿ�")) {
			strDanw="���";
			sbsql.append("select a.ú��,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'�ܼ�',1,sf.quanc||'�ϼ�',dq.mingc)) as ú��,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,sf.quanc,dq.mingc)    \n");
			sbsql.append(" having(grouping(dr.fenx)=0)   \n");
			sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			sbsql.append("       ) a \n");
			
		}else if (getSelzhuangtValue().getValue().equals("�ֳ�")){
			strDanw="��λ";
			 
			sbsql.append("select a.�糧,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(fgs.mingc),2,'�ܼ�',1, fgs.mingc,dc.mingc)) as �糧,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc)    \n");
			sbsql.append(" having(grouping(dr.fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc, grouping(dc.mingc) desc, \n");
			sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			sbsql.append("          ) a \n");
		}else if (getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
			strDanw="���";
			strMeik="��λ";
			blnIsFcFk=true;
			 
			sbsql.append("select a.ú��,a.�糧,").append(strSqlBody1);
			sbsql.append("	select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'�ܼ�',dq.mingc) as ú��, \n");
			sbsql.append("		decode(grouping(dc.mingc)+grouping(dq.mingc),1,'С��',dc.mingc) as �糧, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,dq.mingc,dc.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx    \n");
			sbsql.append("      ) a \n");
		}else if(getSelzhuangtValue().getValue().equals("�ֳ��ֿ�")){
			strDanw="��λ";
			strMeik="���";
			blnIsFcFk=true;
			
			sbsql.append("select a.�糧,a.ú��,").append(strSqlBody1);
			sbsql.append("	select decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),3,'�ܼ�',2,fgs.mingc,dc.mingc) as �糧, \n");
			sbsql.append("		decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),2,'�ֹ�˾С��',1,'�糧С��',dq.mingc) as ú��, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc,dq.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(fenx) desc,fenx    \n");
			sbsql.append("      ) a \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//�����ͷ����
		String ArrHeader[][];
		int ArrWidth[];
		
		if (getSelzhuangtValue().getValue().equals("�ֳ�") || getSelzhuangtValue().getValue().equals("�ֿ�")){
			 ArrHeader = new String[2][23];
			 ArrHeader[0]=new String[] {strDanw,"����<br>��<br>�ۼ�","ú��(��)","����<br>�ۺϼ�<br>(Ԫ/��)","���<br>(Ԫ/��)","��ֵ<br>˰��<br>%","��ֵ˰��<br>(Ԫ/��)","����ǰ<br>���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·˰��<br>(Ԫ/��)","��·�ӷ�<br>(Ԫ/��)","ˮ�˷�<br>(Ԫ/��)","ˮ��˰��<br>(Ԫ/��)","ˮ���ӷ�<br>(Ԫ/��)","���˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��վ�ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��ֵ<br>Mj/kg","��ú����<br>��˰<br>(Ԫ/��)","��ú����<br>����˰<br>(Ԫ/��)","ú��<br>(��Ԫ)"};
			 ArrHeader[1]=new String[] {"��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};

			 ArrWidth=new int[] {100,40,60,50,50,40,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,60};

		}else{
			 ArrHeader=new String[2][24];
			 ArrHeader[0]=new String[] {strDanw,strMeik,"����<br>��<br>�ۼ�","ú��(��)","����<br>�ۺϼ�<br>(Ԫ/��)","���<br>(Ԫ/��)","��ֵ<br>˰��<br>%","��ֵ˰��<br>(Ԫ/��)","����ǰ<br>���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·˰��<br>(Ԫ/��)","��·�ӷ�<br>(Ԫ/��)","ˮ�˷�<br>(Ԫ/��)","ˮ��˰��<br>(Ԫ/��)","ˮ���ӷ�<br>(Ԫ/��)","���˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��վ�ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��ֵ<br>Mj/kg","��ú����<br>��˰<br>(Ԫ/��)","��ú����<br>����˰<br>(Ԫ/��)","ú��<br>(��Ԫ)"};
			 ArrHeader[1]=new String[] {"��","��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};

			 ArrWidth=new int[] {80,100,30,60,50,50,40,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,60};

		}
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		//����ҳ����
		rt.setTitle("��ú�۸����("+getSelzhuangtValue().getValue()+")��",ArrWidth);
		rt.setDefaultTitle(1,4,"�Ʊ�λ:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(11,3,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(22,2, "��λ:��",Table.ALIGN_RIGHT );
	  //rt.setDefaultTitle(rt.title.getCols()-1,2,"��ȼ08��",Table.ALIGN_RIGHT);
		
		
		
		//����
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
//		rt.body.mergeFixedRow();
		rt.body.ShowZero=reportShowZero();
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (getSelzhuangtValue().getValue().equals("�ֳ��ֿ�") || getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
				rt.body.mergeFixedCol(2);
			}
			else{
				rt.body.mergeFixedCol(1);
			}
		}
		if(blnIsFcFk){
			rt.body.setColFormat(5,"0.00");
			rt.body.setColFormat(6,"0.00");
			rt.body.setColFormat(8,"0.00");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.00");
			rt.body.setColFormat(21,"0.000");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(23,"0.00");
			rt.body.setColFormat(24,"0.00");
			
		}else{
			rt.body.setColFormat(4,"0.00");
			rt.body.setColFormat(5,"0.00");
			rt.body.setColFormat(7,"0.00");
			rt.body.setColFormat(8,"0.00");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.000");
			rt.body.setColFormat(21,"0.00");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(23,"0.00");
		}
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+strDate,Table.ALIGN_LEFT);
		rt.setDefautlFooter(11,2,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
			rt.setDefautlFooter(22,2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(22,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(22,2,"�Ʊ�:",Table.ALIGN_RIGHT);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();		
	}
	
	private String getDiaor16(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��";
		String strDanw="";
		String strMeik="";
		String strSqlBody="";
		boolean blnIsFcFk =false;
		
		sbsql.append("        decode(grouping(pz.mingc)+grouping(dc.mingc),1,'С��',pz.mingc) as Ʒ��,fenx as ���¼��ۼ�,   \n");
		sbsql.append("        case when fenx='�ۼ�' then '-' else to_char( sum(shangyjc)) end as ���½��,   \n");
		sbsql.append("        sum(kuangfgyl) as �󷽹�Ӧ��, sum(yuns + kuid) as ����  ,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * farl) /sum(kuangfgyl),2) else 0 end) as ������,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * huif) /sum(kuangfgyl),2) else 0 end) as �ҷ�,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * shuif) /sum(kuangfgyl),2) else 0 end) as ˮ��,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * huiff) /sum(kuangfgyl),2) else 0 end) as �ӷ��� ,   \n");
		sbsql.append("        sum(qitsrl) as ����������,sum(shijhylhj) as �ϼ���,sum(shijhylfdy) as ������,   \n");
		sbsql.append("        sum(shijhylgry) as ������,sum(shijhylqty) as ������,sum(SHIJHYLZCSH) as ����,sum(diaocl) as ������,sum(panypk) as ��ӯ��,   \n");
		sbsql.append("        case when fenx='�ۼ�' then '-' else to_char(sum(yuemjc)) end as ��ĩ���    \n");
		sbsql.append(" from diaor16bb dr,gongysb dq,diancxxb dc,vwfengs fgs,dianclbb lx,shengfb sf , pinzb pz,(select * from dianckjpxb where kouj='�±�')  px \n");
		sbsql.append("        where dr.riq=to_date('"+strDate+"','yyyy-mm-dd')   \n");
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(getGongysCondition());
		sbsql.append("         and dc.fuid=fgs.id \n").append(getDiancCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+) and dr.pinz = pz.id(+)  \n");
		
		strSqlBody=sbsql.toString();
		sbsql.setLength(0);
		
		if (getSelzhuangtValue().getValue().equals("�ֿ�")) {
			strDanw="���";
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'�ܼ�',1,sf.quanc||'�ϼ�',dq.mingc)) as ú������,   \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,sf.quanc,dq.mingc,pz.mingc) \n");
			sbsql.append(" having(grouping(dr.fenx)=0)   \n");
			sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),grouping(dq.mingc) desc,dq.mingc,max(dq.xuh),grouping(dr.fenx) desc, grouping(pz.mingc) desc,pz.mingc,dr.fenx \n");
		}else if (getSelzhuangtValue().getValue().equals("�ֳ�")){
			strDanw="��λ";
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(fgs.mingc),2,'�ܼ�',1, fgs.mingc,dc.mingc)) as �糧,   \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc,pz.mingc)    \n");
			sbsql.append(" having(grouping(dr.fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh), grouping(dc.mingc) desc, \n");
			sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc , grouping(pz.mingc) desc,pz.mingc,dr.fenx \n");
		}else if (getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
			blnIsFcFk=true;
			strDanw="���";
			strMeik="��λ";
			sbsql.append("select decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),3,'�ܼ�',dq.mingc) as ú��, \n");
			sbsql.append("decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),2,'�ϼ�',1,fgs.mingc||'С��',dc.mingc) as �糧, \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,dq.mingc,fgs.mingc,dc.mingc,pz.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),grouping(fgs.mingc) desc,fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc , grouping(pz.mingc) desc,pz.mingc,fenx   \n");
		}else if(getSelzhuangtValue().getValue().equals("�ֳ��ֿ�")){
			strDanw="��λ";
			strMeik="���";
			blnIsFcFk=true;
			sbsql.append("select --dr.fenx,fgs.mingc,dc.mingc,dq.mingc, \n");
			sbsql.append("decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),3,'�ܼ�',2,fgs.mingc,dc.mingc) as �糧, \n");
			sbsql.append("decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),2,'�ֹ�˾С��',1,'�糧С��',dq.mingc) as ú��, \n");
			sbsql.append(strSqlBody);
			sbsql.append(" group by rollup (dr.fenx,fgs.mingc,dc.mingc,dq.mingc,pz.mingc)    \n");
			sbsql.append(" having(grouping(fenx)=0)   \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),grouping(fenx) desc, grouping(pz.mingc) desc,pz.mingc,fenx    \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//�����ͷ����
		String ArrHeader[][];
		int ArrWidth[];
		
		if (getSelzhuangtValue().getValue().equals("�ֳ�") || getSelzhuangtValue().getValue().equals("�ֿ�")){
//			 ArrHeader=new String[4][20];
			ArrHeader=new String[4][19];
			 ArrHeader[0]=new String[] {"Ʒ��","Ʒ��","����<br>��<br>�ۼ�","�³�<br>�����<br>(��)","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","����<br>������<br>(��)","ʵ�ʺ�����","ʵ�ʺ�����","ʵ�ʺ�����","ʵ�ʺ�����","ʵ�ʺ�����","������<br>(��)","��ӯ<br>��<br>�̿�<br>(��)","��ĩ<br>�����<br>(��)"};
			 ArrHeader[1]=new String[] {strDanw,"ú��","����<br>��<br>�ۼ�","�³�<br>�����<br>(��)","�󷽹�Ӧ��","�󷽹�Ӧ��","������<br>Qnet,ar<br>MJ/kg","�ҷ�Ad%","ˮ��Mt%","�ӷ���<br>Vdaf%","����<br>������<br>(��)","�ϼ�<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","��ӯ<br>��<br>�̿�<br>(��)","��ĩ<br>�����<br>(��)"};
			 ArrHeader[2]=new String[] {strDanw,"ú��","����<br>��<br>�ۼ�","�³�<br>�����<br>(��)","�ϼ�<br>(��)","����/����<br>(��)","������<br>Qnet,ar<br>MJ/kg","�ҷ�Ad%","ˮ��Mt%","�ӷ���<br>Vdaf%","����<br>������<br>(��)","�ϼ�<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","��ӯ<br>��<br>�̿�<br>(��)","��ĩ<br>�����<br>(��)"};
			 ArrHeader[3]=new String[] {"��","��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
			 ArrWidth=new int[] {80,40,30,60,60,60,60,40,40,40,60,60,60,60,60,60,60,60,60};
					
		}else{
			 ArrHeader=new String[4][20];
			 ArrHeader[0]=new String[] {"Ʒ��","Ʒ��","Ʒ��","����<br>��<br>�ۼ�","�³�<br>�����<br>(��)","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","ʵ�ʹ�Ӧ���","����<br>������<br>(��)","ʵ�ʺ�����","ʵ�ʺ�����","ʵ�ʺ�����","ʵ�ʺ�����","ʵ�ʺ�����","������<br>(��)","��ӯ<br>��<br>�̿�<br>(��)","��ĩ<br>�����<br>(��)"};
			 ArrHeader[1]=new String[] {strDanw,strMeik,"ú��","����<br>��<br>�ۼ�","�³�<br>�����<br>(��)","�󷽹�Ӧ��","�󷽹�Ӧ��","������<br>Qnet,ar<br>MJ/kg","�ҷ�Ad%","ˮ��Mt%","�ӷ���<br>Vdaf%","����<br>������<br>(��)","�ϼ�<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","��ӯ<br>��<br>�̿�<br>(��)","��ĩ<br>�����<br>(��)"};
			 ArrHeader[2]=new String[] {strDanw,strMeik,"ú��","����<br>��<br>�ۼ�","�³�<br>�����<br>(��)","�ϼ�<br>(��)","����/����<br>(��)","������<br>Qnet,ar<br>MJ/kg","�ҷ�Ad%","ˮ��Mt%","�ӷ���<br>Vdaf%","����<br>������<br>(��)","�ϼ�<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","������<br>(��)","��ӯ<br>��<br>�̿�<br>(��)","��ĩ<br>�����<br>(��)"};
			 ArrHeader[3]=new String[] {"��","��","��","��","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};

			 ArrWidth=new int[] {80,100,45,30,60,60,60,60,40,40,40,60,60,60,60,60,60,60,60,60};
		}
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		
		//����ҳ����
		rt.setTitle("������ú̿��Ӧ�����������±�("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1, 5,"�Ʊ�λ:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(10,2 ,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(18,2 ,"��λ:��",Table.ALIGN_RIGHT);
	  //rt.setDefaultTitle(rt.title.getCols()-1,2,"����16-1��",Table.ALIGN_RIGHT);
		
		
		//����ҳ��
		//rt.setPaper(rt.PAPER_A4);
//		rt.setOrientation(rt.PAPER_Landscape);
//		rt.setSplitPageStyle(rt.PAGE_HEIGHT);
//		rt.setMarginBottom(rt.getMarginBottom()+25);
//		����
		rt.setBody(new Table(rs,4,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.getPages();
		
		//rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();

		
		
		
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (getSelzhuangtValue().getValue().equals("�ֳ��ֿ�") || getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
				rt.body.mergeFixedCol(3);
			}
			else{
				rt.body.mergeFixedCol(2);
			}
		}
		if (blnIsFcFk){
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
		}else{
			rt.body.setColFormat(8,"0.00");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
		}
		
		rt.body.ShowZero=reportShowZero();
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+strDate,Table.ALIGN_LEFT);
		rt.setDefautlFooter(10,2,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
			rt.setDefautlFooter(18,2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(18,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
    //rt.setDefautlFooter(18,2,"�Ʊ�:", Table.ALIGN_RIGHT );
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml(0);
		
		//˵�������ָ����ֽ�Ŵ�С��ҳ������setPaper(rt.PAPER_A4)������
		//Ҫ��getAllPapersHtml��������ֵ��������getAllPagesHtml,���ڵ������кϲ�����ǰִ��getPages()
		//return rt.getAllPapersHtml(0);
	}
	
	private String getDiaor02(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String strCondtion="";
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��";
		String strYear=getNianfValue().getValue() +"-01-01";
		StringBuffer sbsql = new StringBuffer();
		
		if (isJTUser()){
			strCondtion="";
		}
		
		sbsql.append("  sum(nvl(hetby,0)) as �ƻ�����,sum(nvl(hetlj,0)) as �ƻ��ۼ�, \n");
		sbsql.append("      sum(nvl(shisby,0)) as ʵ�յ���, sum(nvl(shislj,0)) as ʵ���ۼ�, \n");
		sbsql.append("      sum(nvl(shisby,0))-sum(nvl(hetby,0)) as ��Ƿ����, \n");
		sbsql.append("      sum(nvl(shislj,0))-sum(nvl(hetlj,0)) as ��Ƿ�ۼ�, \n");
		sbsql.append("      baifb(sum(nvl(shisby,0)),sum(nvl(hetby,0)) ) as �����ʵ���, \n");
		sbsql.append("      baifb(sum(nvl(shislj,0)),sum(nvl(hetlj,0)) ) as �������ۼ� \n");
		sbsql.append("from (select ht.diancxxb_id,decode(dq.fuid,0,dq.id,-1,dq.id,dq.fuid) as gongysb_id \n");
		sbsql.append(" 		from  hetb ht,hetslb sl,gongysb dq,diancxxb dc \n");
		sbsql.append("  	where ht.gongysb_id=dq.id  and ht.liucztb_id=1  \n").append(getGongysCondition());
		sbsql.append("       and ht.id=sl.hetb_id and dc.id=ht.diancxxb_id \n").append(getDiancCondition());
		sbsql.append("       and sl.riq>=to_date('"+strYear+"','yyyy-mm-dd') \n");
		sbsql.append("       and sl.riq<=to_date('"+strDate+"','yyyy-mm-dd')  \n");
		sbsql.append(" union \n");
		sbsql.append(" select dc.id as diancxxb_id,gongysb_id from diaor16bb dr,diancxxb dc,gongysb dq \n");
		sbsql.append("        where kuangfgyl>0 and dq.id=dr.gongysb_id and dc.id=dr.diancxxb_id \n").append(getDiancCondition());
		sbsql.append("        and  riq=to_date('"+strDate+"','yyyy-mm-dd') \n").append(getDiancCondition());
		sbsql.append("        ) a, \n");
		sbsql.append("(select ht.diancxxb_id,decode(dq.fuid,0,dq.id,-1,dq.id,dq.fuid) as gongysb_id, \n");
		sbsql.append(" sum(decode(sl.riq,to_date('"+strDate+"','yyyy-mm-dd'),sl.hetl,0)) as hetby, \n");
		sbsql.append(" sum(sl.hetl) as hetlj \n");
		sbsql.append(" from hetb ht,hetslb sl,gongysb dq,diancxxb dc \n");
		sbsql.append("       where  ht.gongysb_id=dq.id \n");
		sbsql.append("       and ht.liucztb_id=1 and ht.diancxxb_id=dc.id \n").append(getDiancCondition());
		sbsql.append("       and ht.id=sl.hetb_id \n").append(getDiancCondition());
		sbsql.append("       and sl.riq>=to_date('"+strYear+"','yyyy-mm-dd') \n");
		sbsql.append("       and sl.riq<=to_date('"+strDate+"','yyyy-mm-dd') \n");
		sbsql.append("group by ht.diancxxb_id,decode(dq.fuid,0,dq.id,-1,dq.id,dq.fuid)) ht, \n");
		sbsql.append("(select dr.diancxxb_id,gongysb_id, sum(decode(fenx,'����',kuangfgyl)) as shisby,sum(decode(fenx,'�ۼ�',kuangfgyl)) as shislj \n");
		sbsql.append("    from diaor16bb  dr,diancxxb dc,gongysb dq \n");
		sbsql.append("    where riq = to_date('"+strDate+"', 'yyyy-mm-dd') ");
		sbsql.append("    and dc.id=dr.diancxxb_id and dq.id=dr.gongysb_id \n").append(getDiancCondition()).append(getDiancCondition());
		sbsql.append("group by dr.diancxxb_id,gongysb_id) sl, \n");
		sbsql.append("diancxxb dc,gongysb dq,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='�±�')  px,vwfengs  fgs \n");
		sbsql.append("WHERE  a.diancxxb_id=dc.id \n");
		sbsql.append("     and a.gongysb_id=dq.id \n");
		sbsql.append("     and dc.dianclbb_id=lx.id(+) and dc.fuid=fgs.id  \n");
		sbsql.append("     and a.diancxxb_id=ht.diancxxb_id(+) and a.gongysb_id=ht.gongysb_id(+) \n");
		sbsql.append("     and a.diancxxb_id=sl.diancxxb_id(+) and a.gongysb_id=sl.gongysb_id(+) \n");
		sbsql.append("     and dc.id=px.diancxxb_id(+)  and dq.shengfb_id=sf.id(+)  \n");
		
		String strSqlBody=sbsql.toString();
		sbsql.setLength(0);
		if (getSelzhuangtValue().getValue().equals("�ֿ�")) {
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'�ܼ�',1,sf.quanc||'�ϼ�',dq.mingc)) as ú������,   \n");
			sbsql.append(strSqlBody); 
			sbsql.append(" group by rollup (sf.quanc,dq.mingc)    \n");
			sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),grouping(dq.mingc) desc,dq.mingc,max(dq.xuh)  \n");
			
		}else if(getSelzhuangtValue().getValue().equals("�ֳ�")){
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(fgs.mingc),2,'�ܼ�',1, fgs.mingc,dc.mingc)) as �糧,   \n");
			sbsql.append(strSqlBody); 
			sbsql.append(" group by rollup (fgs.mingc,dc.mingc)    \n");
			sbsql.append(" order by grouping(fgs.mingc) desc,max(fgs.xuh), grouping(dc.mingc) desc, \n");
			sbsql.append(" max(px.xuh),dc.mingc \n");
			
		}else if(getSelzhuangtValue().getValue().equals("�ֳ��ֿ�")){
			sbsql.append("select decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),3,'�ܼ�',2,fgs.mingc,dc.mingc) as �糧,  \n");
			sbsql.append("      decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),2,'�ֹ�˾С��',1,'�糧С��',dq.mingc) as ú��,  \n");
			sbsql.append(strSqlBody); 
			sbsql.append("group by rollup (fgs.mingc,dc.mingc,dq.mingc)     \n");
			sbsql.append("order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh) \n");
		
		}else if(getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
			sbsql.append("select decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),3,'�ܼ�',dq.mingc) as ú��, \n");
			sbsql.append("decode(grouping(fgs.mingc)+grouping(dc.mingc)+grouping(dq.mingc),2,'�ϼ�',1,fgs.mingc||'С��',dc.mingc) as �糧, \n");
			sbsql.append(strSqlBody); 
			sbsql.append(" group by rollup (dq.mingc,fgs.mingc,dc.mingc)    \n");
			sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),grouping(fgs.mingc) desc,fgs.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc   \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//�����ͷ����
		String ArrHeader[][]=null;
		int ArrWidth[]=null;
		if(getSelzhuangtValue().getValue().equals("�ֿ�")){
			ArrHeader=new String[2][9];
			ArrHeader[0]=new String[] {"���","�ƻ�(��)","�ƻ�(��)","ʵ��(��)","ʵ��(��)","��Ƿ(��)","��Ƿ(��)","������%","������%"};
			ArrHeader[1]=new String[] {"���","����","�ۼ�","����","�ۼ�","����","�ۼ�","����","�ۼ�"};
			ArrWidth=new int[] {150,80,80,80,80,80,80,80,80};
			rt.setBody(new Table(rs,2,0,1));
		}else if(getSelzhuangtValue().getValue().equals("�ֳ�")){
			ArrHeader=new String[2][9];
			ArrHeader[0]=new String[] {"����","�ƻ�(��)","�ƻ�(��)","ʵ��(��)","ʵ��(��)","��Ƿ(��)","��Ƿ(��)","������%","������%"};
			ArrHeader[1]=new String[] {"����","����","�ۼ�","����","�ۼ�","����","�ۼ�","����","�ۼ�"};
			ArrWidth=new int[] {150,80,80,80,80,80,80,80,80};
			rt.setBody(new Table(rs,2,0,1));
		}else if(getSelzhuangtValue().getValue().equals("�ֳ��ֿ�")){
			ArrHeader=new String[2][10];
			ArrHeader[0]=new String[] {"����","���","�ƻ�(��)","�ƻ�(��)","ʵ��(��)","ʵ��(��)","��Ƿ(��)","��Ƿ(��)","������%","������%"};
			ArrHeader[1]=new String[] {"����","���","����","�ۼ�","����","�ۼ�","����","�ۼ�","����","�ۼ�"};
			ArrWidth=new int[] {150,80,80,80,80,80,80,80,80};
			rt.setBody(new Table(rs,2,0,2));
		}else if(getSelzhuangtValue().getValue().equals("�ֿ�ֳ�")){
			ArrHeader=new String[2][10];
			ArrHeader[0]=new String[] {"���","����","�ƻ�(��)","�ƻ�(��)","ʵ��(��)","ʵ��(��)","��Ƿ(��)","��Ƿ(��)","������%","������%"};
			ArrHeader[1]=new String[] {"���","����","����","�ۼ�","����","�ۼ�","����","�ۼ�","����","�ۼ�"};
			ArrWidth=new int[] {150,80,80,80,80,80,80,80,80};
			rt.setBody(new Table(rs,2,0,2));
		}
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		//����ҳ����
		rt.setTitle("ȼ�Ϲ�Ӧ�±�("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,3,"�Ʊ�λ:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(5,2,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(8,2,"��λ:��",Table.ALIGN_RIGHT);
	//	rt.setDefaultTitle(rt.title.getCols()-1,2,"��ȼ02��",Table.ALIGN_RIGHT);
		
		//����
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero=reportShowZero();
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,3,"�Ʊ�ʱ��:"+strDate,Table.ALIGN_LEFT);
		rt.setDefautlFooter(4,2,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
			rt.setDefautlFooter(8,2 , "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(8,2 , "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(8,2 ,"�Ʊ�:",Table.ALIGN_RIGHT);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private boolean _QueryClick = false;
	
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		if(_CreateChick){
			_CreateChick=false;
			Create();
		}
		if(_DeleteChick){
			_DeleteChick=false;
			Delete();
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);
			
			//begin��������г�ʼ������
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			getDiancmcModels();
			getMeikdqmcModels();
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName =visit.getString1();
        }else{
        	if(visit.getString1().equals("")) {
        		mstrReportName =visit.getString1();
            }
        }
		//mstrReportName="diaor04bb";
		if(mstrReportName.equals("diaor01bb")){
			leix="1";
		}else if(mstrReportName.equals("diaor02bb")){
			leix="2";
		}else if(mstrReportName.equals("diaor03bb")){
			leix="3";
		}else if(mstrReportName.equals("diaor04bb")){
			leix="4";
		}else if(mstrReportName.equals("diaor08bb")){
			leix="8";
		}else if(mstrReportName.equals("diaor16bb")){
			leix="6";
		}
		if(getYuefValue()!=null){
			String yuef = getYuefValue().getValue();
			if(yuef.length()==1){
				leix=leix+"0"+yuef;
			}else{
				leix=leix+yuef;
			}
			if(! leix.substring(0,1).equals("2")){
				//getDiancmcModels(); 
			}
		}
		setUserName(visit.getRenymc());
		setYuebmc(mstrReportName);
		blnIsBegin = true;

		getSelectData();
		
	}
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if (!mstrReportName.equals(this.RT_DR01)){
			tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("SelzhuangtSelect");
			cb.setWidth(100);
			//cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
		tb1.addText(new ToolbarText("-"));
		
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
		/*DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);*/
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		
		setTree(etu);
		
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("��ϵ糧");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		//tb1.addText(new ToolbarText("�糧:"));
		//ComboBox dianc = new ComboBox();
		//dianc.setTransform("DiancmcDropDown");
		//dianc.setWidth(60);
		//dianc.setListeners("select:function(){document.Form0.submit();}");
		//tb1.addField(dianc);
		
		tb1.addText(new ToolbarText("-"));
		if (!mstrReportName.equals(this.RT_DR01)){
			tb1.addText(new ToolbarText("���:"));
			ComboBox meik = new ComboBox();
			meik.setTransform("MeikdqmcDropDown");
			meik.setEditable(true);
			//meik.setWidth(60);
			//meik.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(meik);
			
			tb1.addText(new ToolbarText("-"));
		}
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
		
		
	}
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		/*if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue(treeid);
			}
		}*/
		this.treeid = treeid;
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
//	���ӵ糧��ѡ���ļ���
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
	
	private String m_yuebmc;
	public void setYuebmc(String yuebmc){
		m_yuebmc=yuebmc;
	}
	public String getYuebmc(){
		return m_yuebmc;
	}
	
	private void Create() {
		// Ϊ "ˢ��" ��ť��Ӵ������
	}
	
	private void Delete() {
		JDBCcon con=new JDBCcon();
		String sql="";
		String where="where tongjrq=to_date('"+getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-1','yyyy-MM-dd')";
		if(getDiancmcValue().getId()!=-1){
			where = where+" and diancxxb_id="+getDiancmcValue().getId();
		}
		 if(leix.substring(0, 1).equals("6")){
			 sql="delete from diaor16bb "+where;
		 }else{
			 sql="delete from diaor0"+leix.substring(0, 1)+"bb "+where;
		 }
		 con.getDelete(sql);
		 con.Close();
		 setMsg("ɾ���ɹ�!");
	}

//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel10(_value);
		}
		
//		ú�����
		private boolean _meikdqmc = false;
	    public IPropertySelectionModel getMeikdqmcModel() {
	    	if(((Visit)getPage().getVisit()).getProSelectionModel3() == null){
	    		getMeikdqmcModels();
	    	}
	    	return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }

	    public IDropDownBean getMeikdqmcValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean3() == null){
				((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getMeikdqmcModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean3();
	    }
	    
	    public void setMeikdqmcValue(IDropDownBean Value){
	    	if (Value==null){
	    		((Visit)getPage().getVisit()).setDropDownBean3(Value);
				return;
			}
	    	if (((Visit)getPage().getVisit()).getDropDownBean3().getId()==Value.getId()) {
	    		_meikdqmc = false;
			}else{
				_meikdqmc = true;
			}
	    	((Visit)getPage().getVisit()).setDropDownBean3(Value);
	    }
	    
	    public IPropertySelectionModel getMeikdqmcModels(){
	        long  lngDiancxxbID= ((Visit) getPage().getVisit()).getDiancxxb_id();
	        String sql="";
	        
	        if (((Visit) getPage().getVisit()).isDCUser()){
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,gongysb gys where d.gongysb_id=gys.id and diancxxb_id=" + lngDiancxxbID;
	        }else if(((Visit) getPage().getVisit()).isJTUser()){
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d ,gongysb gys where d.gongysb_id=gys.id  ";
	        }else {
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,diancxxb dc,gongysb gys where d.gongysb_id=gys.id and d.diancxxb_id=dc.id and dc.fuid=" + lngDiancxxbID;	        	
	        }
	        ((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"ȫ��"));
	        return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }
	    
	    public void setMeikdqmcModel(IPropertySelectionModel _value) {
	    	((Visit)getPage().getVisit()).setProSelectionModel3(_value);
	    }
		
	 // ���������
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
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2003; i <= DateUtil.getYear(new Date())+1; i++) {
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
	        } else {
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
    	if  (_YuefValue!=Value){
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
    
private static IPropertySelectionModel _SelzhuangtModel;
	
	public IPropertySelectionModel getSelzhuangtModel() {
		if (_SelzhuangtModel == null) {
			getSelzhuangtModels();
		}
		return _SelzhuangtModel;
	}

	private IDropDownBean _SelzhuangtValue;

	public IDropDownBean getSelzhuangtValue() {
		if(_SelzhuangtValue==null){
			setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
		}
		if(mstrReportName.equals(RT_DR01)){
			setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
		}
		return _SelzhuangtValue;
	}
	
	private boolean _SelzhuangtChange=false;
	public void setSelzhuangtValue(IDropDownBean Value) {
		if (_SelzhuangtValue==Value) {
			_SelzhuangtChange = false;
		}else{
			_SelzhuangtValue = Value;
			_SelzhuangtChange = true;
		}
	}

	public IPropertySelectionModel getSelzhuangtModels() {
		List listSelzhuangt=new ArrayList();
		listSelzhuangt.add(new IDropDownBean(1, "�ֳ�"));
		listSelzhuangt.add(new IDropDownBean(2, "�ֿ�"));
		listSelzhuangt.add(new IDropDownBean(3, "�ֳ��ֿ�"));
		listSelzhuangt.add(new IDropDownBean(4, "�ֿ�ֳ�"));
		
		_SelzhuangtModel = new IDropDownModel(listSelzhuangt);
		return _SelzhuangtModel;
	}

	public void setSelzhuangtModel(IPropertySelectionModel _value) {
		_SelzhuangtModel = _value;
	}
	
//ú�����_id	
	private IPropertySelectionModel _IMeikdqIdModel;
	public IPropertySelectionModel getIMeikdqIdModel() {
		if (_IMeikdqIdModel == null) {
			getIMeikdqIdModels();
		}
		return _IMeikdqIdModel;
	}
	
	public IPropertySelectionModel getIMeikdqIdModels() {
		
		String sql="";
		
		sql = "select id,meikdqbm from meikdqb order by meikdqmc";
		
		_IMeikdqIdModel = new IDropDownModel(sql);
		return _IMeikdqIdModel;
	}
//	
	//��������
	public boolean _diqumcchange = false;
	private IDropDownBean _DiqumcValue;

	public IDropDownBean getDiqumcValue() {
		if(_DiqumcValue==null){
			_DiqumcValue=(IDropDownBean)getIDiqumcModels().getOption(0);
		}
		return _DiqumcValue;
	}

	public void setDiqumcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiqumcValue != null) {
			id = _DiqumcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diqumcchange = true;
			} else {
				_diqumcchange = false;
			}
		}
		_DiqumcValue = Value;
	}

	private IPropertySelectionModel _IDiqumcModel;

	public void setIDiqumcModel(IPropertySelectionModel value) {
		_IDiqumcModel = value;
	}

	public IPropertySelectionModel getIDiqumcModel() {
		if (_IDiqumcModel == null) {
			getIDiqumcModels();
		}
		return _IDiqumcModel;
	}

	public IPropertySelectionModel getIDiqumcModels() {
		String sql="";
		
		sql = "select mk.meikdqbm,mk.meikdqmc from meikdqb mk order by meikdqmc";
//		System.out.println(sql);
		
		_IDiqumcModel = new IDropDownModel(sql);
		return _IDiqumcModel;
	}
	

	
	public String getcontext() {
			return "";
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}
}