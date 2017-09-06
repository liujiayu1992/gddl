package com.zhiren.jt.leibbreport;

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


public class Leibbreport  extends BasePage implements PageValidateListener{
	
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
		getLeibfxb();
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
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
			this.getLeibfxb();
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}

		if(_fengschange){
			
			_fengschange=false;
			Refurbish();
		}
		getToolBars() ;
		Refurbish();
	}
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if(getBaoblxValue().getValue().equals("�۱ȷ�����")){
			return getLeibfxb();
		}else if(getBaoblxValue().getValue().equals("���ȷ�����")){
			return getHuanbfxb();
		}else{
			return "�޴˱���";
		}
	}
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	//�۱�			
	private String getLeibfxb(){

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
		long year=intyear-1;
//		String riq=OraDate(_BeginriqValue);//��ǰ����
//		String riq=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="ͬ�ڶԱ�(�۱�)";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����

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


		//��������
		titlename=titlename+"";
		String str1SQL="select decode(grouping(fgs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,fgs.mingc,dc.mingc) as danwmc,\n" +
				"decode(1,1,fx.fenx,'') as fenx,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',sum(tq.meitsg),'"+intyear+"-1^"+intyear+"-"+intMonth+"',sum(bq.meitsg),'��ֵ',sum(bq.meitsg)-sum(tq.meitsg),'%',decode(sum(tq.meitsg),0,0,round((sum(bq.meitsg)-sum(tq.meitsg))/sum(tq.meitsg)*100,2))) as meitsg,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',sum(tq.meithy),'"+intyear+"-1^"+intyear+"-"+intMonth+"',sum(bq.meithy),'��ֵ',sum(bq.meithy)-sum(tq.meithy),'%',decode(sum(tq.meithy),0,0,round((sum(bq.meithy)-sum(tq.meithy))/sum(tq.meithy)*100,2))) as meithy,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',sum(tq.meitkc),'"+intyear+"-1^"+intyear+"-"+intMonth+"',sum(bq.meitkc),'��ֵ',sum(bq.meitkc)-sum(tq.meitkc),'%',decode(sum(tq.meitkc),0,0,round((sum(bq.meitkc)-sum(tq.meitkc))/sum(tq.meitkc)*100,2))) as meitkc,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',sum(tq.meitfd),'"+intyear+"-1^"+intyear+"-"+intMonth+"',sum(bq.meitfd),'��ֵ',sum(bq.meitfd)-sum(tq.meitfd),'%',decode(sum(tq.meitfd),0,0,round((sum(bq.meitfd)-sum(tq.meitfd))/sum(tq.meitfd)*100,2))) as meitfd,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),'"+intyear+"-1^"+intyear+"-"+intMonth+"',fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))," +
				"								 '��ֵ',fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
				"							     '%',decode(fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),0,0,round((fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))/fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))*100,2))) as rulrl,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3)),'"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))," +
				"									 '��ֵ',decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))," +
				"									 '%',decode(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3)),0,0,round((decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3)))" +
				"												 /decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))*100,2))) as rucrl,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
				"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))," +
								"				'��ֵ',(decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd)))-(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))," +
								"				'%',decode(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),0,0," +
								"					round(((decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd)))-(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))))/" +
								"					(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))*100,2))) as rucrzc,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))," +
						"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)))/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))*100,2))) as jiesrz,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
						"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
								"				 '��ֵ',(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))-(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),0,0,round(((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))-(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))))/" +
								"				     (decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))*100,2))) as jiesrzc,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2))," +
						"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.meil),0,0,round(sum(bq.meil*tq.meij)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*tq.meij)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*tq.meij)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2)))/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2))*100,2))) as meij,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))," +
						"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2)))/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*100,2))) as daocj,\n" + 
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))," +
				"								 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0, round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))*29.271/decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))," +
	"												'��ֵ',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))*29.271/\n" +
	"                                       			decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
	"                                      				 round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),"+
								"				 '%',decode(decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),0,0,\n" +
								"                     round((decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))*29.271/\n" + 
								"                                         decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
								"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)))/\n" + 
								"                                       decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
								"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/\n" + 
								"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))*100,2))) as biaomdj,"+
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))," +
						"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0, round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.meidjbhs)/sum(bq.meil),2))*29.271/decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))," +
						"						 '��ֵ',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,\n" +
						"                        round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.meidjbhs)/sum(bq.meil),2))*29.271/\n" + 
						"                                         decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),"+
						"						 '%',decode(decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" +
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),0,0,\n" + 
						"                                      round((decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.meidjbhs)/sum(bq.meil),2))*29.271/\n" + 
						"                                         decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)))/\n" + 
						"                                       decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))*100,2))) as buhsbmdj,"+
				" decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2))," +
						"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.yunzf)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.yunzf)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.yunzf)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2)))/\n" +
								"      					 decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2))*100,2))) as yunzf,"+
	
				"decode(fx.fenx,'"+year+"-1^"+year+"-"+intMonth+"',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2))," +
						"						 '"+intyear+"-1^"+intyear+"-"+intMonth+"',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.qitfy)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.qitfy)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.qitfy)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2)))/\n" +
								"       				decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2))*100,2))) as qitfy"+
				"          from\n";
		String str2SQL="select '"+intyear+"-1^"+intyear+"-"+intMonth+"' as fenx,dc.id,max(dc.mingc),max(dr01.meitsg) as meitsg,max(dr01.meithyhj) as meithy,max(dr01.meitkc) as meitkc,max(dr01.fadl) as meitfd,\n" +
				"sum(dr01.biaozmlfd) as biaozmlfd, sum(dr01.shiyhyfd) as shiyhyfd, sum(dr01.meithyfd) as meithyfd,sum(dr04.jincsl) as jincsl,\n" + 
				"sum(dr08.meil) as meil,sum(dr08.daoczhj) as daoczhj,	\n"+
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.kuangj+dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf+dr08.daozzf+dr08.qitfy	\n"+
				"             +dr08.shuiyf+dr08.shuiyzf+dr08.qiyf+dr08.gangzf-dr08.zengzse-dr08.qiyse-dr08.shuiyse-dr08.tieyse))/sum(dr08.meil),2)) as meidjbhs,\n" +
				"fun_cunrlfrl(sum(dr01.biaozmlfd), sum(dr01.shiyhyfd), sum(dr01.meithyfd)) as rulrl,\n" + 
				"decode(sum(dr04.jincsl),0,0,round(sum(dr04.jincsl*dr04.changffrl)/sum(dr04.jincsl),3)) as rucrl,\n" + 
				"(decode(sum(dr04.jincsl),0,0,round(sum(dr04.jincsl*dr04.changffrl)/sum(dr04.jincsl),3)) -\n" + 
				"             fun_cunrlfrl(sum(dr01.biaozmlfd), sum(dr01.shiyhyfd), sum(dr01.meithyfd))) as rucrzc,\n" + 
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)) as jiesrz,\n" + 
				"(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)) -\n" + 
				"             fun_cunrlfrl(sum(dr01.biaozmlfd), sum(dr01.shiyhyfd), sum(dr01.meithyfd))) as jiesrzc,\n" + 
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.kuangj)/sum(dr08.meil),2)) as meij,\n" + 
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.daoczhj)/sum(dr08.meil),2)) as daocj,\n" + 
				"/*   decode(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),0,0,\n" +
				"             round(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.kuangj+dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf+dr08.daozzf+dr08.qitfy\n" + 
				"             +dr08.shuiyf+dr08.shuiyzf+dr08.qiyf+dr08.gangzf))/sum(dr08.meil),2))*29.271/\n" + 
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),3)) as biaomdj,*/\n" + 
				"decode(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),0,0,\n" + 
				"             round(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.daoczhj)/sum(dr08.meil),2))*29.271/\n" + 
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),3))\n" + 
				"             as biaomdj,"+
	
				"decode(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),0,0,\n" +
				"             round(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.kuangj+dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf+dr08.daozzf+dr08.qitfy\n" + 
				"             +dr08.shuiyf+dr08.shuiyzf+dr08.qiyf+dr08.gangzf-dr08.zengzse-dr08.qiyse-dr08.shuiyse-dr08.tieyse))/sum(dr08.meil),2)) *29.271/\n" + 
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),3))\n" + 
				"             as buhsbmdj,"+
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf\n" +
				"             +dr08.shuiyf+dr08.shuiyzf+dr08.gangzf+dr08.qiyf\n" + 
				"             +dr08.daozzf))/sum(dr08.meil),2)) as yunzf,"+
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.qitfy)/sum(dr08.meil),2)) as qitfy\n" + 
				"from diaor01bb dr01,diaor04bb dr04,diaor08bb dr08,diancxxb dc\n" ;
		String str3SQL="diancxxb dc,vwfengs fgs\n" + 
				"where dc.fuid=fgs.id and dc.id=fx.diancxxb_id\n" + 
				"          and fx.diancxxb_id=bq.id(+)\n" + 
				"          and fx.diancxxb_id=tq.id(+)\n" + 
				"group by rollup(fx.fenx,fgs.mingc,dc.mingc)\n" + 
				"having not grouping(fx.fenx)=1\n" + 
				"order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,\n" + 
				"grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";;
			if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL= 
					str1SQL+"( select diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"                           (select distinct diancxxb_id from diaor08bb\n" + 
				"                                   where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				"                           ) dcid,(select '"+year+"-1^"+year+"-"+intMonth+"' as fenx,1 as xuh  from dual union select '"+intyear+"-1^"+intyear+"-"+intMonth+"'  as fenx,2 as xhu from dual\n" + 
				"                                   union select '��ֵ'  as fenx,3 as xhu from dual union select '%'  as fenx,4 as xhu from dual ) fx,diancxxb dc\n" + 
				"                           where dc.id=dcid.diancxxb_id "+strGongsID+" ) fx,\n" + 
				"                ( "+str2SQL+
				"                    where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
				"                          and dr01.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"                          and dr04.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.fenx='�ۼ�'\n" + 
				"                          and dr01.fenx='�ۼ�' and dr04.fenx='�ۼ�' "+strGongsID+"\n" + 
				"                    group by (dc.id))bq ,\n" + 
				"                ( "+str2SQL+
				"                        where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
				"                              and dr01.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dr08.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"                              and dr04.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dr08.fenx='�ۼ�'\n" + 
				"                              and dr01.fenx='�ۼ�' and dr04.fenx='�ۼ�' "+strGongsID+"\n" + 
				"                        group by (dc.id))tq,\n"
					+ str3SQL;
		}else{
		strSQL=
				str1SQL+"( select diancxxb_id,fx.fenx,fx.xuh from\n" + 
			"                           (select distinct diancxxb_id from diaor08bb\n" + 
			"                                   where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
			"                           ) dcid,(select '"+year+"-1^"+year+"-"+intMonth+"' as fenx,1 as xuh  from dual union select '"+intyear+"-1^"+intyear+"-"+intMonth+"'  as fenx,2 as xhu from dual\n" + 
			"                                   union select '��ֵ'  as fenx,3 as xhu from dual union select '%'  as fenx,4 as xhu from dual ) fx,diancxxb dc\n" + 
			"                           where dc.id=dcid.diancxxb_id "+strGongsID+" ) fx,\n" + 
			"                ( "+str2SQL+
			"                    where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
			"                          and dr01.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
			"                          and dr04.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.fenx='�ۼ�'\n" + 
			"                          and dr01.fenx='�ۼ�' and dr04.fenx='�ۼ�' "+strGongsID+"\n" + 
			"                    group by (dc.id))bq ,\n" + 
			"                ( "+str2SQL+
			"                        where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
			"                              and dr01.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dr08.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
			"                              and dr04.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and dr08.fenx='�ۼ�'\n" + 
			"                              and dr01.fenx='�ۼ�' and dr04.fenx='�ۼ�' "+strGongsID+"\n" + 
			"                        group by (dc.id))tq,\n" 
				+ str3SQL;
		}
		 
		//System.out.println(strSQL);
		 ArrHeader =new String[1][17];
		 ArrHeader[0]=new String[] {"��λ����","�Ա�����","ú̿ʵ��","ú̿����","���","������","��¯��ֵ","�볧��ֵ","�볧��ֵ��","������ֵ","������ֵ��","ú��","������","��ú����","˰ǰ��ú����","���ӷ�","��������"};

		 ArrWidth =new int[] {150,120,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80};


		iFixedRows=1;
		//iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 2,"�������"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,2,"��׼:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(6,3,"�Ʊ�:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(12,2,"���:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//����ҳ��
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	//����				
	private String getHuanbfxb(){

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
		long month=0;
		if(intMonth==1){
			month=1;
		}else{
			month=intMonth-1;
		}
		
//		String riq=OraDate(_BeginriqValue);//��ǰ����
//		String riq=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="ͬ�ڶԱ�(����)";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����

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
		//��������
		titlename=titlename+"";
		String str1SQL="select decode(grouping(fgs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,fgs.mingc,dc.mingc) as danwmc,\n" +
				"decode(1,1,fx.fenx,'') as fenx,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',sum(tq.meitsg),'"+intyear+"��"+intMonth+"��',sum(bq.meitsg),'��ֵ',sum(bq.meitsg)-sum(tq.meitsg),'%',decode(sum(tq.meitsg),0,0,round((sum(bq.meitsg)-sum(tq.meitsg))/sum(tq.meitsg)*100,2))) as meitsg,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',sum(tq.meithy),'"+intyear+"��"+intMonth+"��',sum(bq.meithy),'��ֵ',sum(bq.meithy)-sum(tq.meithy),'%',decode(sum(tq.meithy),0,0,round((sum(bq.meithy)-sum(tq.meithy))/sum(tq.meithy)*100,2))) as meithy,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',sum(tq.meitkc),'"+intyear+"��"+intMonth+"��',sum(bq.meitkc),'��ֵ',sum(bq.meitkc)-sum(tq.meitkc),'%',decode(sum(tq.meitkc),0,0,round((sum(bq.meitkc)-sum(tq.meitkc))/sum(tq.meitkc)*100,2))) as meitkc,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',sum(tq.meitfd),'"+intyear+"��"+intMonth+"��',sum(bq.meitfd),'��ֵ',sum(bq.meitfd)-sum(tq.meitfd),'%',decode(sum(tq.meitfd),0,0,round((sum(bq.meitfd)-sum(tq.meitfd))/sum(tq.meitfd)*100,2))) as meitfd,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),'"+intyear+"��"+intMonth+"��',fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))," +
				"								 '��ֵ',fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
				"							     '%',decode(fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),0,0,round((fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))/fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))*100,2))) as rulrl,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3)),'"+intyear+"��"+intMonth+"��',decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))," +
				"									 '��ֵ',decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))," +
				"									 '%',decode(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3)),0,0,round((decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3)))" +
				"												 /decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))*100,2))) as rucrl,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
				"						 '"+intyear+"��"+intMonth+"��',decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd))," +
								"				'��ֵ',(decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd)))-(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))," +
								"				'%',decode(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),0,0," +
								"					round(((decode(sum(bq.jincsl),0,0,round(sum(bq.jincsl*bq.rucrl)/sum(bq.jincsl),3))-fun_cunrlfrl(sum(bq.biaozmlfd), sum(bq.shiyhyfd), sum(bq.meithyfd)))-(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))))/" +
								"					(decode(sum(tq.jincsl),0,0,round(sum(tq.jincsl*tq.rucrl)/sum(tq.jincsl),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))*100,2))) as rucrzc,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))," +
						"						 '"+intyear+"��"+intMonth+"��',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)))/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))*100,2))) as jiesrz,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
						"						 '"+intyear+"��"+intMonth+"��',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))," +
								"				 '��ֵ',(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))-(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)),0,0,round(((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))-(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd))))/" +
								"				     (decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3))-fun_cunrlfrl(sum(tq.biaozmlfd), sum(tq.shiyhyfd), sum(tq.meithyfd)))*100,2))) as jiesrzc,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2))," +
						"						 '"+intyear+"��"+intMonth+"��',decode(sum(bq.meil),0,0,round(sum(bq.meil*tq.meij)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*tq.meij)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*tq.meij)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2)))/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meij)/sum(tq.meil),2))*100,2))) as meij,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))," +
						"						 '"+intyear+"��"+intMonth+"��',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2)))/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*100,2))) as daocj,\n" + 
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))," +
				"								 '"+intyear+"��"+intMonth+"��',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0, round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))*29.271/decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))," +
				"											'��ֵ',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))*29.271/\n" +
				"                                				decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
				"                               				 round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),"+
								"				 '%',decode(decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),0,0,\n" +
								"                                      round((decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.daocj)/sum(bq.meil),2))*29.271/\n" + 
								"                                         decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
								"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)))/\n" + 
								"                                       decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
								"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.daocj)/sum(tq.meil),2))*29.271/\n" + 
								"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))*100,2))) as biaomdj,"+
				" decode(fx.fenx,'"+intyear+"��"+month+"��',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))," +
						"						 '"+intyear+"��"+intMonth+"��',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0, round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.meidjbhs)/sum(bq.meil),2))*29.271/decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))," +
						"						 '��ֵ',decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,\n" +
						"                                         round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.meidjbhs)/sum(bq.meil),2))*29.271/\n" + 
						"                                         decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),"+
						"						 '%',decode(decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" +
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)),0,0,\n" + 
						"                                      round((decode(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.meidjbhs)/sum(bq.meil),2))*29.271/\n" + 
						"                                         decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.jiesrz)/sum(bq.meil),3)),3))-decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3)))/\n" + 
						"                                       decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),0,0,\n" + 
						"                                         round(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.meidjbhs)/sum(tq.meil),2))*29.271/\n" + 
						"                                         decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.jiesrz)/sum(tq.meil),3)),3))*100,2))) as buhsbmdj,"+
		
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2))," +
						"						 '"+intyear+"��"+intMonth+"��',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.yunzf)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.yunzf)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.yunzf)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2)))/\n" +
								"      					 decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.yunzf)/sum(tq.meil),2))*100,2))) as yunzf,"+
				"decode(fx.fenx,'"+intyear+"��"+month+"��',decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2))," +
						"						 '"+intyear+"��"+intMonth+"��',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.qitfy)/sum(bq.meil),2))," +
								"				 '��ֵ',decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.qitfy)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2))," +
								"				 '%',decode(decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2)),0,0,round((decode(sum(bq.meil),0,0,round(sum(bq.meil*bq.qitfy)/sum(bq.meil),2))-decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2)))/\n" +
								"       				decode(sum(tq.meil),0,0,round(sum(tq.meil*tq.qitfy)/sum(tq.meil),2))*100,2))) as qitfy"+
				"          from\n" ;
		String str2SQL="select '"+intyear+"��"+intMonth+"��' as fenx,dc.id,max(dc.mingc),max(dr01.meitsg) as meitsg,max(dr01.meithyhj) as meithy,max(dr01.meitkc) as meitkc,max(dr01.fadl) as meitfd,\n" +
				"sum(dr01.biaozmlfd) as biaozmlfd, sum(dr01.shiyhyfd) as shiyhyfd, sum(dr01.meithyfd) as meithyfd,sum(dr04.jincsl) as jincsl,\n" + 
				"sum(dr08.meil) as meil,sum(dr08.daoczhj) as daoczhj,	\n"+
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.kuangj+dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf+dr08.daozzf+dr08.qitfy	\n"+
				"                           		+dr08.shuiyf+dr08.shuiyzf+dr08.qiyf+dr08.gangzf-dr08.zengzse-dr08.qiyse-dr08.shuiyse-dr08.tieyse))/sum(dr08.meil),2)) as meidjbhs,\n" +
				"                           fun_cunrlfrl(sum(dr01.biaozmlfd), sum(dr01.shiyhyfd), sum(dr01.meithyfd)) as rulrl,\n" +
				
				"decode(sum(dr04.jincsl),0,0,round(sum(dr04.jincsl*dr04.changffrl)/sum(dr04.jincsl),3)) as rucrl,\n" + 
				"(decode(sum(dr04.jincsl),0,0,round(sum(dr04.jincsl*dr04.changffrl)/sum(dr04.jincsl),3)) -\n" + 
				"       fun_cunrlfrl(sum(dr01.biaozmlfd), sum(dr01.shiyhyfd), sum(dr01.meithyfd))) as rucrzc,\n" + 
				"decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)) as jiesrz,\n" + 
				"(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)) -\n" + 
				"       fun_cunrlfrl(sum(dr01.biaozmlfd), sum(dr01.shiyhyfd), sum(dr01.meithyfd))) as jiesrzc,\n" + 
				" decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.kuangj)/sum(dr08.meil),2)) as meij,\n" + 
				" decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.daoczhj)/sum(dr08.meil),2)) as daocj,\n" + 
				"/*   decode(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),0,0,\n" +
				"                        		 round(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.kuangj+dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf+dr08.daozzf+dr08.qitfy\n" + 
				"     +dr08.shuiyf+dr08.shuiyzf+dr08.qiyf+dr08.gangzf))/sum(dr08.meil),2))*29.271/\n" + 
				"     decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),3))\n" + 
				"     as biaomdj,*/\n" + 
				"decode(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),0,0,\n" + 
				"      round(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.daoczhj)/sum(dr08.meil),2))*29.271/\n" + 
				"      decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),3))\n" + 
				"       as biaomdj,"+
				"decode(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),0,0,\n" +
				"      round(decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.kuangj+dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf+dr08.daozzf+dr08.qitfy\n" + 
				"      +dr08.shuiyf+dr08.shuiyzf+dr08.qiyf+dr08.gangzf-dr08.zengzse-dr08.qiyse-dr08.shuiyse-dr08.tieyse))/sum(dr08.meil),2)) *29.271/\n" + 
				"      decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.rez)/sum(dr08.meil),3)),3))\n" + 
				"      as buhsbmdj,"+
				" decode(sum(dr08.meil),0,0,round(sum(dr08.meil*(dr08.jiaohqyzf+dr08.tielyf+dr08.tielzf\n" +
				"       +dr08.shuiyf+dr08.shuiyzf+dr08.gangzf+dr08.qiyf\n" + 
				"		+dr08.daozzf))/sum(dr08.meil),2)) as yunzf,"+
				" decode(sum(dr08.meil),0,0,round(sum(dr08.meil*dr08.qitfy)/sum(dr08.meil),2)) as qitfy\n" + 
				"                    from diaor01bb dr01,diaor04bb dr04,diaor08bb dr08,diancxxb dc\n" ;
		String str3SQL="diancxxb dc,vwfengs fgs\n" + 
				"where dc.fuid=fgs.id and dc.id=fx.diancxxb_id\n" + 
				"      and fx.diancxxb_id=bq.id(+)\n" + 
				"      and fx.diancxxb_id=tq.id(+)\n" + 
				"group by rollup(fx.fenx,fgs.mingc,dc.mingc)\n" + 
				"having not grouping(fx.fenx)=1\n" + 
				"order by grouping(fgs.mingc) desc,max(fgs.xuh),fgs.mingc,\n" + 
				"grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL=
				str1SQL+"( select diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"                           (select distinct diancxxb_id from diaor08bb\n" + 
				"                                   where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1))\n" + 
				"                           ) dcid,(select '"+intyear+"��"+month+"��' as fenx,1 as xuh  from dual union select '"+intyear+"��"+intMonth+"��'  as fenx,2 as xhu from dual\n" + 
				"                                   union select '��ֵ'  as fenx,3 as xhu from dual union select '%'  as fenx,4 as xhu from dual ) fx,diancxxb dc\n" + 
				"                           where dc.id=dcid.diancxxb_id "+strGongsID+" ) fx,\n" + 
				"                ( "+str2SQL+ 
				"                    where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
				"                          and dr01.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"                          and dr04.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.fenx='����'\n" + 
				"                          and dr01.fenx='����' and dr04.fenx='����' "+strGongsID+"\n" + 
				"                    group by (dc.id))bq ,\n" + 
				"                ( "+str2SQL+ 
				"                        where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
				"                              and dr01.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dr08.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1)\n" + 
				"                              and dr04.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dr08.fenx='����'\n" + 
				"                              and dr01.fenx='����' and dr04.fenx='����' "+strGongsID+"\n" + 
				"                        group by (dc.id))tq,\n" 
				+str3SQL; 


		}else{
			strSQL=
				str1SQL+"( select diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"                           (select distinct diancxxb_id from diaor08bb\n" + 
				"                                   where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1))\n" + 
				"                           ) dcid,(select '"+intyear+"��"+month+"��' as fenx,1 as xuh  from dual union select '"+intyear+"��"+intMonth+"��'  as fenx,2 as xhu from dual\n" + 
				"                                   union select '��ֵ'  as fenx,3 as xhu from dual union select '%'  as fenx,4 as xhu from dual ) fx,diancxxb dc\n" + 
				"                           where dc.id=dcid.diancxxb_id "+strGongsID+" ) fx,\n" + 
				"                ( "+str2SQL+ 
				"                    where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
				"                          and dr01.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"                          and dr04.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and dr08.fenx='����'\n" + 
				"                          and dr01.fenx='����' and dr04.fenx='����' "+strGongsID+"\n" + 
				"                    group by (dc.id))bq ,\n" + 
				"                ( "+str2SQL+ 
				"                        where dr01.diancxxb_id=dc.id and dr04.diancxxb_id=dc.id and dr08.diancxxb_id=dc.id\n" + 
				"                              and dr01.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dr08.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1)\n" + 
				"                              and dr04.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-1) and dr08.fenx='����'\n" + 
				"                              and dr01.fenx='����' and dr04.fenx='����' "+strGongsID+"\n" + 
				"                        group by (dc.id))tq,\n" 
				+str3SQL; 
		}
	
//		System.out.println(strSQL);
		 ArrHeader =new String[1][17];
		 ArrHeader[0]=new String[] {"��λ����","�Ա�����","ú̿ʵ��","ú̿����","���","������","��¯��ֵ","�볧��ֵ","�볧��ֵ��","������ֵ","������ֵ��","ú��","������","��ú����","˰ǰ��ú����","���ӷ�","��������"};

		 ArrWidth =new int[] {150,120,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80};


		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, iFixedRows));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 2,intyear+"-"+month+"��"+intyear+"-"+intMonth, Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,1,"��׼:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(6,3,"�Ʊ�:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(12,3,"���:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
		//����ҳ��
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(14,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}	

	//****************************************************
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
	
//*************************************************************
//	�󱨱�����
	public boolean _Baoblxchange = false;
//	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getIBaoblxModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean1() != null) {
			id = (((Visit)getPage().getVisit()).getDropDownBean1()).getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

//	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIBaoblxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIBaoblxModels() {
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"�۱ȷ�����"));
		fahdwList.add(new IDropDownBean(1,"���ȷ�����"));

		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(fahdwList));

		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
	
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
		

		tb1.addText(new ToolbarText("�����ѯ:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setId("Tongjkj");
		cb.setWidth(200);
		tb1.addField(cb);
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