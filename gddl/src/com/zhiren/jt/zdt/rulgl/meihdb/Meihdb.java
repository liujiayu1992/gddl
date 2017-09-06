package com.zhiren.jt.zdt.rulgl.meihdb;

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
/* 
* ʱ�䣺2009-05-4
* ���ߣ� ll
* �޸����ݣ�1���޸Ĳ�ѯsql��yuezbb�����ֶε�����,����yuezbb���µĹ�ʽ��ȡ�µ��ֶ�����
* 		   
*/ 
/* 
* ʱ�䣺2009-05-20
* ���ߣ� ll
* �޸����ݣ�1���޸Ĳ�ѯsql�й�������ʽ��
* 		   
*/ 
/* 
* ʱ�䣺2009-07-20
* ���ߣ� ll
* �޸����ݣ�1��������¯���ݲ���ʾ���⣬�޸ķ���������ǧ��ʱ��λ���㡣
* 		   
*/ 
public class Meihdb  extends BasePage implements PageValidateListener{
	
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
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}


	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
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
		Visit visit = (Visit) getPage().getVisit();
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon con = new JDBCcon();
		
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

		String danwmc="";//��������
		int jib=this.getDiancTreeJib();
		
		danwmc=getTreeDiancmc(this.getTreeid());
	
		String diancCondition=
			"where d.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+")" ; 
		//�����ͷ����
		Report rt = new Report();
		String titlename="��ú�±�";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		StringBuffer sbsql = new StringBuffer();
		//��������
		 
				sbsql.append("from  \n");
				sbsql.append("(select d.id,d.xuh,d.mingc,d.fuid,vwfenxyue.fenx from diancxxb d,vwfenxyue ").append(diancCondition).append(") fx, \n");
				sbsql.append("(select diancxxb_id,decode(1,1,'����') as fenx,round(sum(fadl/10000),2) as fadl,round(sum(gongdl/10000),2) as gongdl, \n");
				sbsql.append("sum(gongrl) as gongrl from riscsjb where riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n");
				sbsql.append("and riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by diancxxb_id \n");
				sbsql.append("union select diancxxb_id,decode(1,1,'�ۼ�') as fenx,round(sum(fadl/10000),2) as fadl,round(sum(gongdl/10000),2) as gongdl, \n");
				sbsql.append("sum(gongrl) as gongrl from riscsjb where riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') \n");
				sbsql.append("and riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by diancxxb_id) rsj, \n");
				sbsql.append("(select diancxxb_id,decode(1,1,'����') as fenx,sum(fady) as fadym,sum(gongry) as gongrym \n");
				sbsql.append("from shouhcrbb where riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n");
				sbsql.append("and riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by diancxxb_id \n");
				sbsql.append("union select diancxxb_id,decode(1,1,'�ۼ�') as fenx,sum(fady) as fadym,sum(gongry) as gongrym \n");
				sbsql.append("from shouhcrbb where riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') \n");
				sbsql.append("and riq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by diancxxb_id) msj, \n");
				sbsql.append("(select rl.diancxxb_id,decode(1,1,'����') as fenx,decode(sum(mh.fadhy+mh.gongrhy), \n");
				sbsql.append("0,0,round((sum(mh.fadhy*rl.qnet_ar)+sum(mh.gongrhy*rl.qnet_ar))/sum(mh.fadhy+mh.gongrhy),2)) as farl  \n");
				sbsql.append("from rulmzlb rl,meihyb mh where mh.rulmzlb_id=rl.id and mh.rulrq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n");
				sbsql.append("and mh.rulrq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by rl.diancxxb_id \n");
				sbsql.append("union select rl.diancxxb_id,decode(1,1,'�ۼ�') as fenx,decode(sum(mh.fadhy+mh.gongrhy), \n");
				sbsql.append("0,0,round((sum(mh.fadhy*rl.qnet_ar)+sum(mh.gongrhy*rl.qnet_ar))/sum(mh.fadhy+mh.gongrhy),2)) as farl  \n");
				sbsql.append("from rulmzlb rl,meihyb mh where mh.rulmzlb_id=rl.id and mh.rulrq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') \n");
				sbsql.append("and mh.rulrq<=last_day(to_date('"+intyear+"-"+intMonth+"-25','yyyy-mm-dd')) group by rl.diancxxb_id) mrez, \n");
//				sbsql.append("(select dc.id,y.fenx,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl, \n");
//				sbsql.append("sum(fadhml) as fadhml,sum(gongrhml) as gongrhml,sum(fadhml+gongrhml) as tianrml, \n");
//				sbsql.append("decode(sum(fadhml+gongrhml),0,0,round((sum(fadhml*rulmrz)+sum(gongrhml*rulmrz))/sum(fadhml+gongrhml),2)) as rulmrz, \n");
//				sbsql.append("sum(fadhbzml) as fadhbzml,sum(gongrhbzml) as gongrhbzml \n");
//				--------------------------------�޸ĵ�����ʽ��yuezbb�е��ֶ�---------------------------------------------------
				sbsql.append("(select dc.id,y.fenx,sum(fadl) as fadl,sum(y.fadl-y.gongdl) as gongdl,sum(gongrl) as gongrl,  \n");
				sbsql.append("sum(fadytrml) as fadhml,sum(gongrytrml) as gongrhml,sum(fadytrml+gongrytrml) as tianrml,  \n");
				sbsql.append("decode(sum(fadytrml+gongrytrml),0,0,round((sum(fadytrml*rultrmpjfrl/1000)+sum(gongrytrml*rultrmpjfrl/1000))/sum(fadytrml+gongrytrml),2)) as rulmrz,   \n");
				sbsql.append("sum(fadmzbml+fadyzbzml+fadqzbzml) as fadhbzml,sum(gongrmzbml+gongryzbzml+gongrqzbzml) as gongrhbzml  \n");
//				--------------------------------------------------------------------------------------------------------------			
				sbsql.append("from vwyuezbb y ,diancxxb dc \n");
				sbsql.append("where riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and y.diancxxb_id=dc.id \n");
				sbsql.append("group by dc.id,y.fenx order by dc.id,fenx) sb,vwfengs gs \n");
				String tjsql=sbsql.toString();
				sbsql.setLength(0);
				
			if(jib==3){//��Ϊ����ʱ,���շֹ�˾����
				 
				sbsql.append("select fx.mingc as danw, \n");
				sbsql.append("fx.fenx,sum(sb.fadl) as sbfdl,sum(sb.gongdl) as sbgdl,sum(sb.gongrl) as sbgrl, \n");
				sbsql.append("sum(sb.tianrml) as sbtianrml, \n");
				sbsql.append("decode(sum(sb.fadhml+sb.gongrhml),0,0,round((sum(sb.fadhml*sb.rulmrz)+sum(sb.gongrhml*sb.rulmrz))/sum(sb.fadhml+sb.gongrhml),2)) as sbhmrz, \n");
				sbsql.append("decode(sum(sb.fadl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.fadl)*10000),2)) as sbfadmh, \n");
				sbsql.append("decode(sum(sb.gongrl),0,0,round(sum(sb.gongrhbzml)*1000/sum(sb.gongrl),2)) as sbgongrmh, \n");
				sbsql.append("sum(rsj.fadl) as fadl,sum(rsj.gongdl) as gongdl,sum(rsj.gongrl) as gongrl, \n");
				sbsql.append("sum(msj.fadym+msj.gongrym) as tianrml, \n");
				sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
				sbsql.append("decode(sum(rsj.fadl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.fadl)*10000),2)) as fadmh, \n");
				sbsql.append("decode(sum(rsj.gongrl),0,0,round(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000/sum(rsj.gongrl),2)) as gongrmh, \n");
				sbsql.append("(sum(sb.tianrml)-sum(msj.fadym+msj.gongrym)) as meilcz, \n");
				sbsql.append("(decode(sum(sb.fadhml+sb.gongrhml),0,0,round((sum(sb.fadhml*sb.rulmrz)+sum(sb.gongrhml*sb.rulmrz))/sum(sb.fadhml+sb.gongrhml),2))-decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))) as rezc, \n");
				sbsql.append("(decode(sum(sb.fadl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.fadl)*10000),2))-decode(sum(rsj.fadl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.fadl)*10000),2))) as fadmhc, \n");
				sbsql.append("(decode(sum(sb.gongdl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.gongdl)*10000),1))-decode(sum(rsj.gongdl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.gongdl)*1000),2))) as gongdmhc, \n");
				sbsql.append("(decode(sum(sb.gongrl),0,0,round(sum(sb.gongrhbzml)*1000/sum(sb.gongrl),2))-decode(sum(rsj.gongrl),0,0,round(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)/sum(rsj.gongrl),2))) as gongrmhc \n");
				sbsql.append(tjsql);
				sbsql.append("where fx.fuid=gs.id and  fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
				sbsql.append("and fx.id=msj.diancxxb_id(+) and  fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
				sbsql.append("and fx.fenx=mrez.fenx(+) and sb.id=fx.id(+) and sb.fenx=fx.fenx(+) \n");
				sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
				sbsql.append("having not(grouping(fx.mingc)=1) \n");
				sbsql.append("order by grouping(gs.mingc) desc,max(gs.xuh),gs.mingc, \n");
				sbsql.append("grouping(fx.mingc) desc,max(fx.xuh),fx.mingc,fx.fenx \n");
			}else if(jib==2){
				sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),1,gs.mingc,fx.mingc) as danw, \n");
				sbsql.append("fx.fenx,sum(sb.fadl) as sbfdl,sum(sb.gongdl) as sbgdl,sum(sb.gongrl) as sbgrl, \n");
				sbsql.append("sum(sb.tianrml) as sbtianrml, \n");
				sbsql.append("decode(sum(sb.fadhml+sb.gongrhml),0,0,round((sum(sb.fadhml*sb.rulmrz)+sum(sb.gongrhml*sb.rulmrz))/sum(sb.fadhml+sb.gongrhml),2)) as sbhmrz, \n");
				sbsql.append("decode(sum(sb.fadl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.fadl)*10000),2)) as sbfadmh, \n");
				sbsql.append("decode(sum(sb.gongrl),0,0,round(sum(sb.gongrhbzml)*1000/sum(sb.gongrl),2)) as sbgongrmh, \n");
				sbsql.append("sum(rsj.fadl) as fadl,sum(rsj.gongdl) as gongdl,sum(rsj.gongrl) as gongrl, \n");
				sbsql.append("sum(msj.fadym+msj.gongrym) as tianrml, \n");
				sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
				sbsql.append("decode(sum(rsj.fadl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.fadl)*10000),2)) as fadmh, \n");
				sbsql.append("decode(sum(rsj.gongrl),0,0,round(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000/sum(rsj.gongrl),2)) as gongrmh, \n");
				sbsql.append("(sum(sb.tianrml)-sum(msj.fadym+msj.gongrym)) as meilcz, \n");
				sbsql.append("(decode(sum(sb.fadhml+sb.gongrhml),0,0,round((sum(sb.fadhml*sb.rulmrz)+sum(sb.gongrhml*sb.rulmrz))/sum(sb.fadhml+sb.gongrhml),2))-decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))) as rezc, \n");
				sbsql.append("(decode(sum(sb.fadl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.fadl)*10000),2))-decode(sum(rsj.fadl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.fadl)*10000),2))) as fadmhc, \n");
				sbsql.append("(decode(sum(sb.gongdl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.gongdl)*10000),1))-decode(sum(rsj.gongdl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.gongdl)*1000),2))) as gongdmhc, \n");
				sbsql.append("(decode(sum(sb.gongrl),0,0,round(sum(sb.gongrhbzml)*1000/sum(sb.gongrl),2))-decode(sum(rsj.gongrl),0,0,round(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)/sum(rsj.gongrl),2))) as gongrmhc \n");
				sbsql.append(tjsql);
				sbsql.append("where fx.fuid=gs.id and  fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
				sbsql.append("and fx.id=msj.diancxxb_id(+) and  fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
				sbsql.append("and fx.fenx=mrez.fenx(+) and sb.id=fx.id(+) and sb.fenx=fx.fenx(+) \n");
				sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
				sbsql.append("having not(grouping(gs.mingc)=1) \n");
				sbsql.append("order by grouping(gs.mingc) desc,max(gs.xuh),gs.mingc, \n");
				sbsql.append("grouping(fx.mingc) desc,max(fx.xuh),fx.mingc,fx.fenx \n");
			}else if(jib==1){//��Ϊ�ֹ�˾�͵糧ʱ,���յ糧���ͻ���
				sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
				sbsql.append("fx.fenx,sum(sb.fadl) as sbfdl,sum(sb.gongdl) as sbgdl,sum(sb.gongrl) as sbgrl, \n");
				sbsql.append("sum(sb.tianrml) as sbtianrml, \n");
				sbsql.append("decode(sum(sb.fadhml+sb.gongrhml),0,0,round((sum(sb.fadhml*sb.rulmrz)+sum(sb.gongrhml*sb.rulmrz))/sum(sb.fadhml+sb.gongrhml),2)) as sbhmrz, \n");
				sbsql.append("decode(sum(sb.fadl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.fadl)*10000),2)) as sbfadmh, \n");
				sbsql.append("decode(sum(sb.gongrl),0,0,round(sum(sb.gongrhbzml)*1000/sum(sb.gongrl),2)) as sbgongrmh, \n");
				sbsql.append("sum(rsj.fadl) as fadl,sum(rsj.gongdl) as gongdl,sum(rsj.gongrl) as gongrl, \n");
				sbsql.append("sum(msj.fadym+msj.gongrym) as tianrml, \n");
				sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
				sbsql.append("decode(sum(rsj.fadl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.fadl)*10000),2)) as fadmh, \n");
				sbsql.append("decode(sum(rsj.gongrl),0,0,round(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000/sum(rsj.gongrl),2)) as gongrmh, \n");
				sbsql.append("(sum(sb.tianrml)-sum(msj.fadym+msj.gongrym)) as meilcz, \n");
				sbsql.append("(decode(sum(sb.fadhml+sb.gongrhml),0,0,round((sum(sb.fadhml*sb.rulmrz)+sum(sb.gongrhml*sb.rulmrz))/sum(sb.fadhml+sb.gongrhml),2))-decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))) as rezc, \n");
				sbsql.append("(decode(sum(sb.fadl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.fadl)*10000),2))-decode(sum(rsj.fadl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.fadl)*10000),2))) as fadmhc, \n");
				sbsql.append("(decode(sum(sb.gongdl),0,0,round(sum(sb.fadhbzml)*1000000/(sum(sb.gongdl)*10000),1))-decode(sum(rsj.gongdl),0,0,round(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)*1000000/(sum(rsj.gongdl)*1000),2))) as gongdmhc, \n");
				sbsql.append("(decode(sum(sb.gongrl),0,0,round(sum(sb.gongrhbzml)*1000/sum(sb.gongrl),2))-decode(sum(rsj.gongrl),0,0,round(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)/sum(rsj.gongrl),2))) as gongrmhc \n");
				sbsql.append(tjsql);
				sbsql.append("where fx.fuid=gs.id and  fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
				sbsql.append("and fx.id=msj.diancxxb_id(+) and  fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
				sbsql.append("and fx.fenx=mrez.fenx(+) and sb.id=fx.id(+) and sb.fenx=fx.fenx(+) \n");
				sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
				sbsql.append("having not(grouping(fx.fenx)=1) \n");
				sbsql.append("order by grouping(gs.mingc) desc,max(gs.xuh),gs.mingc, \n");
				sbsql.append("grouping(fx.mingc) desc,max(fx.xuh),fx.mingc,fx.fenx \n");
			}
//			 System.out.println(sbsql.toString());
			 ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			 String ArrHeader[][]=new String[3][21];
			 ArrHeader[0]=new String[] {"��λ","��λ","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","��ֵ","��ֵ","��ֵ","��ֵ","��ֵ"};
			 ArrHeader[1]=new String[] {"��λ","��λ","������","������","������","����ȼú��","��ֵ","ú��","ú��","������","������","������","����ȼú��","��ֵ","ú��","ú��","����ȼú��","��ֵ","ú��","ú��","ú��"};
			 ArrHeader[2]=new String[] {"��λ","��λ","������","������","������","����ȼú��","��ֵ","����","����","������","������","������","����ȼú��","��ֵ","����","����","����ȼú��","��ֵ","����","����","����"};

			 int ArrWidth[]=new int[] {140,45,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55};

				 iFixedRows=1;
				 iCol=10;
			
			 
			// ����
//			rt.setBody(new Table(rs,2, 0, 2));
			Table bt=new Table(rs,3,0,2);
			rt.setBody(bt);
			//�ڶ��о���
			bt.setColAlign(2,Table.ALIGN_CENTER);
			rt.setTitle(getBiaotmc()+intyear+"��"+intMonth+"��"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(9, 3, intyear+"��"+intMonth+"��", Table.ALIGN_CENTER);
			rt.setDefaultTitle(19, 3 ,"��λ:�֡�ǧ��ʱ", Table.ALIGN_RIGHT);
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			//�����С���һ�о���
			if(rt.body.getRows()>3){
				rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			}
			//ҳ�� 
			  rt.createDefautlFooter(ArrWidth);
			/*  rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(4,1,"�Ʊ�:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(6,1,"���:",Table.ALIGN_LEFT);*/
			//  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
//			����ҳ��
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			 rt.setDefautlFooter(11,3,"���:",Table.ALIGN_CENTER);
			 if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				 rt.setDefautlFooter(19,3, "�Ʊ�:",Table.ALIGN_RIGHT);
					}else{
						
						rt.setDefautlFooter(19,3, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

			(),Table.ALIGN_RIGHT);
					}
//			 rt.setDefautlFooter(19,3,"�Ʊ�:",Table.ALIGN_RIGHT);
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			con.Close();
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
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
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