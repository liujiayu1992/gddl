package com.zhiren.jt.diaoygl.bucysjhcx;

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
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;


public class Bucysjhcx  extends BasePage implements PageValidateListener{
	
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
	
	//��ʼ����
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
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
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			setBaoblxValue(null);
			getIBaoblxModels();
			this.setTreeid(null);
			this.getFengsModels();
			isBegin=true;
			
			
			this.getSelectData();
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}
		if(_Baoblxchange){
			_Baoblxchange=false;
			Refurbish();
		}
		if(_fengschange){
			
			_fengschange=false;
			Refurbish();
		}
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	
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

	private boolean isBegin=false;
	private String getSelectData(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		
	
		
		

		String strGongsID = "";
		String danwmc="";//��������
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());
		
		
		
		
		
		
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="��������ƻ�";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		//��������
			if(getBaoblxValue().getValue().equals("�ֳ�����")){
				titlename=titlename+"(�ֳ�����)";
				if(jib==1){//�ֳ����ܵĵ糧���Ǽ���ʱ,���շֹ�˾����
					strSQL="select\n" +
					"decode(grouping(dc.leix)+grouping(dc.jianc)+grouping(ys.id),\n" + 
					"   3,'"+danwmc+"',2,'��'||dc.leix||'С��',1,dc.jianc||'С��',max(dc.jianc)) as diancmc,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.faz_id=cz.id))) as fazmc,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.daoz_id=cz.id))) as daozmc,\n" + 
					" decode(grouping(ys.id),1,'',max((select mz.mingc from meizb mz where ys.pinm=mz.id))) as pinm,\n" + 
					" sum(ys.pic) as pic,sum(ys.pid),\n" + 
					" decode(grouping(ys.id),1,'',max(ys.pizjhh)) as pizjhh,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.huanzg=cz.id and cz.leib='�ۿ�'))) as huanzg,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.zongdg=cz.id and cz.leib='�ۿ�'))) as zongdg,\n" + 
					" decode(grouping(ys.id),1,'',max(ys.zibccc)) as zibccc,\n" + 
					" decode(grouping(ys.id),1,'',max(ys.shunh)) as shunh,\n" + 
					" decode(grouping(ys.id),1,'',max((select lj.mingc from lujxxb lj where ys.tielj=lj.id))) as tielj\n" + 
					"from yunsjhb ys,gongysb mk,\n" + 
					"(select d.id,d.fuid,d.mingc as jianc,df.mingc as leix   from diancxxb d ,diancxxb df where  d.jib=3 and d.fuid=df.id(+)) dc\n" + 
					"where ys.diancxxb_id=dc.id and ys.gongysb_id=mk.id and ys.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID+" and ys.leix=0\n" + 
					"group by rollup (dc.leix,dc.jianc,ys.id)\n" + 
					"order by grouping(dc.leix) desc,dc.leix desc,grouping(dc.jianc) desc,dc.jianc,grouping(ys.id) desc,ys.id";
			
				}else{
					strSQL="select\n" +
						"decode(grouping(dc.leix)+grouping(dc.jianc)+grouping(ys.id),\n" + 
						"   3,'"+danwmc+"',2,'��'||dc.leix||'С��',1,dc.jianc||'С��',max(dc.jianc)) as diancmc,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.faz_id=cz.id))) as fazmc,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.daoz_id=cz.id))) as daozmc,\n" + 
						" decode(grouping(ys.id),1,'',max((select mz.mingc from meizb mz where ys.pinm=mz.id))) as pinm,\n" + 
						" sum(ys.pic) as pic,sum(ys.pid),\n" + 
						" decode(grouping(ys.id),1,'',max(ys.pizjhh)) as pizjhh,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.huanzg=cz.id and cz.leib='�ۿ�'))) as huanzg,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.zongdg=cz.id and cz.leib='�ۿ�'))) as zongdg,\n" + 
						" decode(grouping(ys.id),1,'',max(ys.zibccc)) as zibccc,\n" + 
						" decode(grouping(ys.id),1,'',max(ys.shunh)) as shunh,\n" + 
						" decode(grouping(ys.id),1,'',max((select lj.mingc from lujxxb lj where ys.tielj=lj.id))) as tielj\n" + 
						"from yunsjhb ys,gongysb mk,\n" + 
						"(select dc.id,dc.mingc as jianc,dc.fuid,dl.mingc as leix from diancxxb dc,dianclbb dl where dc.dianclbb_id=dl.id) dc\n" + 
						"where ys.diancxxb_id=dc.id and ys.gongysb_id=mk.id and ys.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID+" and ys.leix=0\n" + 
						"group by rollup (dc.leix,dc.jianc,ys.id)\n" + 
						"order by grouping(dc.leix) desc,dc.leix desc,grouping(dc.jianc) desc,dc.jianc,grouping(ys.id) desc,ys.id";
				}
			
//				ֱ���ֳ��ֿ����	
				 ArrHeader=new String[1][12];
				 ArrHeader[0]=new String[] {"������λ����","����","��վ","Ʒ��","����","����","��׼�ƻ���","��װ��","�յ���","�Ա�������","˳��","·��"};
				 ArrWidth=new int[] {175,65,65,65,65,65,65,75,65,90,70,65};
				 iFixedRows=1;
				 iCol=10;
				 
			}else if (getBaoblxValue().getValue().equals("�ֿ����")){
				
				
				
				titlename=titlename+"(�ֿ����)";
				strSQL=
					

					"select\n" +
					" decode(grouping(mk.mingc),1,'�ܼ�',mk.mingc) as meikmc,\n" + 
					" decode(grouping(mk.mingc),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.faz_id=cz.id))) as fazmc,\n" + 
					" decode(grouping(mk.mingc),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.daoz_id=cz.id))) as daozmc,\n" + 
					" decode(grouping(mk.mingc),1,'',max((select mz.mingc as meizhong_mc from meizb mz where ys.pinm=mz.id))) as pinm,\n" + 
					" sum(ys.pic) as pic,sum(ys.pid),\n" + 
					" decode(grouping(mk.mingc),1,'',max(ys.pizjhh)) as pizjhh,\n" + 
					" decode(grouping(mk.mingc),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.huanzg=cz.id and cz.leib='�ۿ�'))) as huanzg,\n" + 
					" decode(grouping(mk.mingc),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.zongdg=cz.id and cz.leib='�ۿ�'))) as zongdg,\n" + 
					" decode(grouping(mk.mingc),1,'',max(ys.zibccc)) as zibccc,\n" + 
					" decode(grouping(mk.mingc),1,'',max(ys.shunh)) as shunh,\n" + 
					" decode(grouping(mk.mingc),1,'',max((select lj.mingc from lujxxb lj where ys.tielj=lj.id))) as tielj\n" + 
					"from yunsjhb ys,gongysb mk,\n" + 
					"(select dc.id,dc.fuid,dc.mingc as jianc,dl.mingc as leix from diancxxb dc,dianclbb dl where dc.dianclbb_id=dl.id) dc\n" + 
					"where ys.diancxxb_id=dc.id and ys.gongysb_id=mk.id and ys.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  "+strGongsID +" and ys.leix=0\n" + 
					"group by rollup (mk.mingc)\n" + 
					"order by grouping(mk.mingc) desc,mk.mingc";

			
//				ֱ���ֿ����	
				 ArrHeader=new String[1][12];
				 ArrHeader[0]=new String[] {"�ջ���λ����","����","��վ","Ʒ��","����","����","��׼�ƻ���","��װ��","�յ���","�Ա�������","˳��","·��"};
				 ArrWidth=new int[] {175,65,65,65,65,65,65,75,65,90,70,65};
				 iFixedRows=1;
				 iCol=10;
					
			}else if(getBaoblxValue().getValue().equals("�ֳ��ֿ����")){//�ֳ��ֿ����
				
			
				titlename=titlename+"(�ֳ��ֿ����)";
//				ֱ���ֳ��ֿ����
				if(jib==1){//�ֳ��ֿ�������Ǽ���ʱ,���շֹ�˾����
					strSQL =" select\n" +
					"decode(grouping(dc.leix)+grouping(dc.jianc)+grouping(ys.id),\n" + 
					"   3,'"+danwmc+"',2,'��'||dc.leix||'С��',1,dc.jianc||'С��',max(dc.jianc)) as diancmc,\n" + 
					" decode(grouping(ys.id),1,'',max(mk.mingc)) as meikdqmc,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.faz_id=cz.id))) as fazmc,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.daoz_id=cz.id))) as daozmc,\n" + 
					" decode(grouping(ys.id),1,'',max((select mz.mingc from meizb mz where ys.pinm=mz.id))) as pinm,\n" + 
					" sum(ys.pic) as pic,sum(ys.pid),\n" + 
					" decode(grouping(ys.id),1,'',max(ys.pizjhh)) as pizjhh,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.huanzg=cz.id and cz.leib='�ۿ�'))) as huanzg,\n" + 
					" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.zongdg=cz.id and cz.leib='�ۿ�'))) as zongdg,\n" + 
					" decode(grouping(ys.id),1,'',max(ys.zibccc)) as zibccc,\n" + 
					" decode(grouping(ys.id),1,'',max(ys.shunh)) as shunh,\n" + 
					" decode(grouping(ys.id),1,'',max((select lj.mingc from lujxxb lj where ys.tielj=lj.id))) as tielj\n" + 
					"from yunsjhb ys,gongysb mk,\n" + 
					"(select d.id,d.fuid,d.mingc as jianc,df.mingc as leix   from diancxxb d ,diancxxb df where  d.jib=3 and d.fuid=df.id(+)) dc\n" + 
					"where ys.diancxxb_id=dc.id and ys.gongysb_id=mk.id and ys.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID +" and ys.leix=0\n" + 
					"group by rollup (dc.leix,dc.jianc,ys.id)\n" + 
					"order by grouping(dc.leix) desc,dc.leix desc,grouping(dc.jianc) desc,dc.jianc,grouping(ys.id) desc,ys.id";

				}else{//�ֳ��ֿ����Ƿֹ�˾,�糧ʱ,���յ糧���ͻ���.
					strSQL =" select\n" +
						"decode(grouping(dc.leix)+grouping(dc.jianc)+grouping(ys.id),\n" + 
						"   3,'"+danwmc+"',2,'��'||dc.leix||'С��',1,dc.jianc||'С��',max(dc.jianc)) as diancmc,\n" + 
						" decode(grouping(ys.id),1,'',max(mk.mingc)) as meikdqmc,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.faz_id=cz.id))) as fazmc,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.daoz_id=cz.id))) as daozmc,\n" + 
						" decode(grouping(ys.id),1,'',max((select mz.mingc from meizb mz where ys.pinm=mz.id))) as pinm,\n" + 
						" sum(ys.pic) as pic,sum(ys.pid),\n" + 
						" decode(grouping(ys.id),1,'',max(ys.pizjhh)) as pizjhh,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.huanzg=cz.id and cz.leib='�ۿ�'))) as huanzg,\n" + 
						" decode(grouping(ys.id),1,'',max((select cz.mingc from chezxxb cz where ys.zongdg=cz.id and cz.leib='�ۿ�'))) as zongdg,\n" + 
						" decode(grouping(ys.id),1,'',max(ys.zibccc)) as zibccc,\n" + 
						" decode(grouping(ys.id),1,'',max(ys.shunh)) as shunh,\n" + 
						" decode(grouping(ys.id),1,'',max((select lj.mingc from lujxxb lj where ys.tielj=lj.id))) as tielj\n" + 
						"from yunsjhb ys,gongysb mk,\n" + 
						"(select dc.id,dc.fuid,dc.mingc as jianc,dl.mingc as leix from diancxxb dc,dianclbb dl where dc.dianclbb_id=dl.id) dc\n" + 
						"where ys.diancxxb_id=dc.id and ys.gongysb_id=mk.id and ys.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+strGongsID +" and ys.leix=0\n" + 
						"group by rollup (dc.leix,dc.jianc,ys.id)\n" + 
						"order by grouping(dc.leix) desc,dc.leix desc,grouping(dc.jianc) desc,dc.jianc,grouping(ys.id) desc,ys.id";

				}
//				ֱ���ֳ��ֿ����	
				 ArrHeader=new String[1][13];
				 ArrHeader[0]=new String[] {"������λ����","�ջ���λ����","����","��վ","Ʒ��","����","����","��׼�ƻ���","��װ��","�յ���","�Ա�������","˳��","·��"};
				 ArrWidth=new int[] {120,120,65,65,60,65,65,65,75,65,80,70,60};
				 iFixedRows=2;
				 iCol=11;
				
			}else if (getBaoblxValue().getValue().equals("�ֿ�ֳ�����")){
				titlename=titlename+"(�ֿ�ֳ�����)";
//				�ֿ�ֳ�����
				strSQL =
				

					"select\n" +
					" decode(grouping(mk.mingc),1,'�ܼ�',mk.mingc) as meikmc,\n" + 
					"\tdecode(grouping(mk.mingc)+grouping(dc.jianc),2,'"+danwmc+"',1,'�ϼ�',dc.jianc) as diancmc,\n" + 
					" decode(grouping(dc.jianc),1,'',max((select cz.mingc from chezxxb cz where ys.faz_id=cz.id))) as fazmc,\n" + 
					" decode(grouping(dc.jianc),1,'',max((select cz.mingc from chezxxb cz where ys.daoz_id=cz.id))) as daozmc,\n" + 
					" decode(grouping(dc.jianc),1,'',max((select mz.mingc from meizb mz where ys.pinm=mz.id))) as pinm,\n" + 
					" sum(ys.pic) as pic,sum(ys.pid),\n" + 
					" decode(grouping(dc.jianc),1,'',max(ys.pizjhh)) as pizjhh,\n" + 
					" decode(grouping(dc.jianc),1,'',max((select cz.mingc from chezxxb cz where ys.huanzg=cz.id and cz.leib='�ۿ�'))) as huanzg,\n" + 
					" decode(grouping(dc.jianc),1,'',max((select cz.mingc from chezxxb cz where ys.zongdg=cz.id and cz.leib='�ۿ�'))) as zongdg,\n" + 
					" decode(grouping(dc.jianc),1,'',max(ys.zibccc)) as zibccc,\n" + 
					" decode(grouping(dc.jianc),1,'',max(ys.shunh)) as shunh,\n" + 
					" decode(grouping(dc.jianc),1,'',max((select lj.mingc from lujxxb lj where ys.tielj=lj.id))) as tielj\n" + 
					"from yunsjhb ys,gongysb mk,\n" + 
					"(select dc.id,dc.fuid,dc.mingc as jianc,dl.mingc as leix from diancxxb dc,dianclbb dl where dc.dianclbb_id=dl.id) dc\n" + 
					"where ys.diancxxb_id=dc.id and ys.gongysb_id=mk.id and ys.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+ strGongsID +" and ys.leix=0\n" + 
					"group by rollup (mk.mingc,dc.jianc)\n" + 
					"order by grouping(mk.mingc) desc,mk.mingc,grouping(dc.jianc) desc,dc.jianc desc";

				
//				ֱ���ֳ��ֿ����	
				 ArrHeader=new String[1][13];
				 ArrHeader[0]=new String[] {"�ջ���λ����","������λ����","����","��վ","Ʒ��","����","����","��׼�ƻ���","��װ��","�յ���","�Ա�������","˳��","·��"};
				 ArrWidth=new int[] {120,120,65,65,65,65,65,65,75,65,80,70,60};
				 iFixedRows=2;
				 iCol=11;
			} else if(getBaoblxValue().getValue().equals("�ֵ����ֳ�����")){
				
			
				
				
				titlename=titlename+"(�ֵ����ֳ�����)";
				strSQL = " select\n"
					+ " decode(grouping(dc.leix)+grouping(dc.jianc)+grouping(ys.id),\n"
					+ "  3,'"+danwmc +"',2,'��'||dc.leix||'С��',1,dc.jianc||'С��',max(dc.jianc)) as diancmc,\n"
					+ " decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.faz_id=cz.id))) as fazmc,\n"
					+ " decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.daoz_id=cz.id))) as daozmc,\n"
					+ " decode(grouping(ys.id),1,'',max((select mz.mingc from meizb mz where ys.pinm=mz.id))) as pinm,\n"
					+ " sum(ys.pic) as pic,sum(ys.pid) as pid,\n"
					+ " decode(grouping(ys.id),1,'',max(ys.pizjhh)) as pizjhh,\n"
					+ " decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.huanzg=cz.id and cz.leib='�ۿ�'))) as huanzg,\n"
					+ " decode(grouping(ys.id),1,'',max((select cz.mingc as jianc from chezxxb cz where ys.zongdg=cz.id and cz.leib='�ۿ�'))) as zongdg,\n"
					+ " decode(grouping(ys.id),1,'',max(ys.zibccc)) as zibccc,\n"
					+ " decode(grouping(ys.id),1,'',max(ys.shunh)) as shunh,\n"
					+ " decode(grouping(ys.id),1,'',max((select lj.mingc from lujxxb lj where ys.tielj=lj.id))) as tielj\n"
					+ " from yunsjhb ys,gongysb mk,\n"
					+ " (select d.id,d.mingc as jianc,d.fuid,dc.mingc  as leix  from diancxxb d, diancxxb dc where d.fuid=dc.id  and dc.fuid!=0 order by dc.mingc ) dc\n"
					+ " where ys.diancxxb_id=dc.id and ys.gongysb_id=mk.id and ys.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  "+ strGongsID +"  and ys.leix=0\n"
					+ " group by rollup (dc.leix,dc.jianc,ys.id)\n"
					+ " order by grouping(dc.leix) desc,dc.leix desc,grouping(dc.jianc) desc,dc.jianc,grouping(ys.id) desc,ys.id";

					
						
			

				 ArrHeader=new String[1][12];
				 ArrHeader[0]=new String[] {"������λ����","����","��վ","Ʒ��","����","����","��׼�ƻ���","��װ��","�յ���","�Ա�������","˳��","·��"};
				 ArrWidth=new int[] {175,65,65,65,65,65,65,75,65,90,70,65};
				 iFixedRows=1;
				 iCol=10;
				 
			}
			
//			System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// ����
			rt.setBody(new Table(rs,1, 0, iFixedRows));
			
			rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(iCol, 3, "�Ʊ�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = true;
			//ҳ�� 
			/*
			 * rt.createDefautlFooter(ArrWidth);
			 * rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
			 * rt.setDefautlFooter(4,1,"�Ʊ�:",Table.ALIGN_LEFT);
			 * rt.setDefautlFooter(6,1,"���:",Table.ALIGN_LEFT);
			 * rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 */
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(10,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
			 
			//����ҳ��
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
//			System.out.println(rt.getAllPagesHtml());
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
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		cn.Close();	
		return diancmc;
		
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
		fahdwList.add(new IDropDownBean(1,"�ֿ����"));
		fahdwList.add(new IDropDownBean(2,"�ֳ��ֿ����"));
		fahdwList.add(new IDropDownBean(3,"�ֿ�ֳ�����"));
		fahdwList.add(new IDropDownBean(4,"�ֵ����ֳ�����"));

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
		setDiancxxModel(new IDropDownModel(sql,"�й����Ƽ���"));
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
		
		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setWidth(120);
		//cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
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

	
	
	
	
}