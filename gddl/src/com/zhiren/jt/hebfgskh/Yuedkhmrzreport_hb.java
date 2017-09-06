package com.zhiren.jt.hebfgskh;

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

import org.apache.tapestry.contrib.palette.SortMode;


public class Yuedkhmrzreport_hb  extends BasePage implements PageValidateListener{
	
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}
//	 �ж��Ƿ��ǹ�˾�û�
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}
//	 �ж��Ƿ��ǵ糧�û�
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	//��ʼ����
	private Date _BeginriqValue = new Date();
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
		} else {
			_BeginriqValue = _value;
		}
	}
	// ��Ϣ��
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
	
	//��ť�¼�����ˢ�¡�
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
	
	//ҳ���ʼ��ˢ���¼�
	private void Refurbish() {
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		getSelectData();
	}
	
	/**
	 * ҳ�濪ʼʱ��ʼ������
	 */
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
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
            //	begin��������г�ʼ������
			visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
//			visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
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
	
	/*
	 * �Զ��屨��
	 * @RT_HET=yunsjhcx
	 */
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
	
	/**
	 * ��ѯ����
	 * @author chenzt
	 * @return
	 */
	private String getSelectData(){
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		//�ж��û�����ϵͳ�����ݵĳ�ʼ״̬
		String zhuangt="";
		if(visit.getRenyjb()==3){
			zhuangt="";
		}else if(visit.getRenyjb()==2){
			zhuangt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
		}else if(visit.getRenyjb()==1){
			zhuangt=" and sl.zhuangt=2";
		}
		String yue="";
		if(getYuefValue().toString().length()==1){
			yue="0"+getYuefValue();
		}
		//�õ�ʱ��������ֵ
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
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] =null;
		String titlename="������ϸ��ֵ��";
		//��������
		
		//���cg
//		
		
		String dianwmc="";
		String groupby="";
		String orderby="";
		String havingnot="";
		//'<font color=red >'||jif||'</font>'select decode(sign(1),-1,'-',1,'+') from dual;
		//SELECT CASE SIGN(jif - 0) WHEN 1   THEN  'Is Positive'  WHEN -1   THEN   'Is Negative'   ELSE 'Is Zero'    END FROM    DUAL; 
	
		String ss1=


			"select gmingc,\n" +
			"fl ,'<font color=green > <strong>'||quannh||'</strong></font>' quannh,rezhi,jiz,chaz,beiz,\n" + 
			"decode(sign(jif),-1,'<font color=red >'||Formatxiaosws(jif,2)||'</font>',1,'<font color=blue >'\n" + 
			"||Formatxiaosws(jif,2)||'</font>',0,'0')  jif \n" + 
			"\n" 
		;

String ss=
	"\n" +
	"SELECT gmingc,mmingc,pmingc,fl,yingk,mt,mad,stad,vad,aad,qnet_ar,rez,jiz,beiz," +
	"decode(sign(jif),-1,'<font color=red >'||Formatxiaosws(jif,2)||'</font>',1,'<font color=blue >'\n" + 
	"||Formatxiaosws(jif,2)||'</font>',0,'0')  jif \n" + " FROM (\n" + 
	"(\n" + 
	"\n" + 
	"select  fdl.gmingc, '�����ۺ���ֵ(����ú�ࡢ��ú��)' mmingc,fdl.pmingc ,\n" + 
	"\n" + 
	"round_new(sum(fdl.jingz),0) as fl,round_new(sum(fdl.yingk),2) yingk,\n" + 
	"       decode(sum(ah), 0, 0, round_new(sum(fdl.mt * ah) / sum(ah), 1)) as mt,\n" + 
	"        decode(sum(ah), 0, 0, round_new(sum(fdl.mad *ah) / sum(ah), 2)) as mad,\n" + 
	"         decode(sum(ah), 0, 0, round_new(sum(fdl.stad * ah) / sum(ah), 2)) as stad,\n" + 
	"          decode(sum(ah), 0, 0, round_new(sum(fdl.vad * ah) / sum(ah), 2)) as vad,\n" + 
	"           decode(sum(ah), 0, 0, round_new(sum(fdl.aad * ah) / sum(ah), 2)) as aad,\n" + 
	"              decode(sum(ah), 0, 0, round_new(sum(fdl.qnet_ar * ah) / sum(ah), 3)) as qnet_ar,\n" + 
	"                 nvl(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),0) as rez\n" + 
	"                 ,getjiz(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),'KHXM_ZHRZ','ԭú') jiz\n" + 
	"                 ,getbiaoz(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),'KHXM_ZHRZ','ԭú') beiz,\n" + 
	"           nvl(round_new(round_new(sum(ah)/3300,0)*nvl(getbiaoz(round_new(sum(fdl.rez * ah) / sum(ah), 2),'KHXM_ZHRZ','ԭú')*\n" + 
	"           getchaocjz(abs(getchaz(round_new(sum(rez * ah) / sum(ah), 2),'KHXM_ZHRZ','ԭú')),round_new(sum(rez * ah) / sum(ah),2),'KHXM_ZHRZ')\n" + 
	"\n" + 
	"            ,0),3),0) jif,2 as xuh\n" + 
	" from\n" + 
	"(select f.id,g.mingc gmingc,m.mingc mmingc,p.mingc pmingc,f.laimsl,f.jingz,f.yingk,z.mt,z.mad,z.stad,z.vad,z.aad\n" + 
	",z.qnet_ar,round_new(nvl(z.qnet_ar,0) * 1000 / 4.1816, 0) rez\n" + 
	"\n" + 
	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
	"and f.pinzb_id=p.id\n" + 
	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
	")fdl,\n" + 
	"(\n" + 
	"select f.id, g.mingc,m.mingc,p.mingc, sum(nvl(f.jingz, 0)) as ah\n" + 
	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
	"and f.pinzb_id=p.id\n" + 
	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
	"group by f.id,g.mingc,m.mingc,p.mingc\n" + 
	") mh\n" + 
	"where fdl.id=mh.id and (fdl.pmingc= 'ԭú'or fdl.pmingc= '��ú')\n" + 
	"\n" + 
	"group by rollup(fdl.gmingc, fdl.mmingc,fdl.pmingc )\n" + 
	"having (grouping(fdl.gmingc)+grouping(fdl.mmingc)+grouping(fdl.pmingc)) =3\n" + 
	"\n" + 
	"\n" + 
	")\n" + 
	"union\n" + 
	"(\n" + 
	"\n" + 
	"select  fdl.gmingc, decode(fdl.mmingc,'','�����ۺ���ֵ',fdl.mmingc) mmingc,fdl.pmingc ,\n" + 
	"\n" + 
	"round_new(sum(fdl.jingz),0) as fl,round_new(sum(fdl.yingk),2) yingk,\n" + 
	"       decode(sum(ah), 0, 0, round_new(sum(fdl.mt * ah) / sum(ah), 1)) as mt,\n" + 
	"        decode(sum(ah), 0, 0, round_new(sum(fdl.mad *ah) / sum(ah), 2)) as mad,\n" + 
	"         decode(sum(ah), 0, 0, round_new(sum(fdl.stad * ah) / sum(ah), 2)) as stad,\n" + 
	"          decode(sum(ah), 0, 0, round_new(sum(fdl.vad * ah) / sum(ah), 2)) as vad,\n" + 
	"           decode(sum(ah), 0, 0, round_new(sum(fdl.aad * ah) / sum(ah), 2)) as aad,\n" + 
	"              decode(sum(ah), 0, 0, round_new(sum(fdl.qnet_ar * ah) / sum(ah), 3)) as qnet_ar,\n" + 
	"                 nvl(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),0) as rez\n" + 
	"                 ,\n" + 
	"            decode(fdl.mmingc,'','0', getjiz(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),'KHXM_FKRZ',fdl.pmingc))     jiz\n" + 
	"                 ,getbiaoz(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),'KHXM_FKRZ',fdl.pmingc) beiz,\n" + 
	"           nvl(round_new(round_new(sum(ah)/3300,6)*nvl(getbiaoz(round_new(sum(fdl.rez * ah) / sum(ah), 6),'KHXM_FKRZ',fdl.pmingc)*\n" + 
	"           getchaocjz(abs(getchaz(round_new(sum(rez * ah) / sum(ah), 6),'KHXM_FKRZ',fdl.pmingc)),round_new(sum(rez * ah) / sum(ah),6),'KHXM_FKRZ')\n" + 
	"\n" + 
	"            ,0),3),0) jif,1 as xuh\n" + 
	" from\n" + 
	"(select f.id,g.mingc gmingc,m.mingc mmingc,p.mingc pmingc,f.laimsl,f.jingz,f.yingk,z.mt,z.mad,z.stad,z.vad,z.aad\n" + 
	",z.qnet_ar,round_new(nvl(z.qnet_ar,0) * 1000 / 4.1816, 0) rez\n" + 
	"\n" + 
	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
	"and f.pinzb_id=p.id\n" + 
	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
	")fdl,\n" + 
	"(\n" + 
	"select f.id, g.mingc,m.mingc,p.mingc, sum(nvl(f.jingz, 0)) as ah\n" + 
	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
	"and f.pinzb_id=p.id\n" + 
	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
	"group by f.id,g.mingc,m.mingc,p.mingc\n" + 
	") mh\n" + 
	"where fdl.id=mh.id\n" + 
	"\n" + 
	"group by rollup(fdl.gmingc, fdl.mmingc,fdl.pmingc )\n" + 
	"having (grouping(fdl.gmingc)+grouping(fdl.mmingc)+grouping(fdl.pmingc)) =3 or\n" + 
	"(grouping(fdl.gmingc)+grouping(fdl.mmingc)+grouping(fdl.pmingc))=0 or\n" + 
	"(grouping(fdl.gmingc)+grouping(fdl.mmingc)+grouping(fdl.pmingc))=0\n" + 
	"\n" + 
	"\n" + 
	")\n" + 
	"\n" + 
	"UNION\n" + 
	"(\n" + 
	"\n" + 
	"select  '' gmingc, '' mmingc,'�ۺϵ÷֣�' pmingc ,\n" + 
	"\n" + 
	"0 as fl,0 AS  yingk,\n" + 
	"       0 as mt,\n" + 
	"        0 as mad,\n" + 
	"         0 as stad,\n" + 
	"          0 as vad,\n" + 
	"           0 as aad,\n" + 
	"              0 as qnet_ar,\n" + 
	"                 0 as rez\n" + 
	"                 ,'0' jiz\n" + 
	"                 ,0 beiz,\n" + 
//	"          SUM( round_new(round_new(sum(ah)/3300,0)*nvl(getbiaoz(round_new(sum(fdl.rez * ah) / sum(ah), 2),'KHXM_FKRZ','ԭú')*\n" + 
//	"           getchaocjz(abs(getchaz(round_new(sum(rez * ah) / sum(ah), 2),'KHXM_FKRZ','ԭú')),round_new(sum(rez * ah) / sum(ah),2),'KHXM_FKRZ')\n" + 
	" sum(nvl(jif,0)) jif"+
	"\n" + 
	"           ,3 as xuh\n" + 
	" from\n" + 

	"( select  fdl.gmingc, decode(fdl.mmingc,'','�����ۺ���ֵ',fdl.mmingc) mmingc,fdl.pmingc ,\n" +
	"\n" + 
	"round_new(sum(fdl.jingz),0) as fl,round_new(sum(fdl.yingk),2) yingk,\n" + 
	"       decode(sum(ah), 0, 0, round_new(sum(fdl.mt * ah) / sum(ah), 1)) as mt,\n" + 
	"        decode(sum(ah), 0, 0, round_new(sum(fdl.mad *ah) / sum(ah), 2)) as mad,\n" + 
	"         decode(sum(ah), 0, 0, round_new(sum(fdl.stad * ah) / sum(ah), 2)) as stad,\n" + 
	"          decode(sum(ah), 0, 0, round_new(sum(fdl.vad * ah) / sum(ah), 2)) as vad,\n" + 
	"           decode(sum(ah), 0, 0, round_new(sum(fdl.aad * ah) / sum(ah), 2)) as aad,\n" + 
	"              decode(sum(ah), 0, 0, round_new(sum(fdl.qnet_ar * ah) / sum(ah), 3)) as qnet_ar,\n" + 
	"                 nvl(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),0) as rez\n" + 
	"                 ,\n" + 
	"            decode(fdl.mmingc,'','0', getjiz(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),'KHXM_FKRZ',fdl.pmingc))     jiz\n" + 
	"                 ,getbiaoz(round_new(decode(sum(ah), 0, 0, round_new(sum(fdl.rez * ah) / sum(ah), 2)),0),'KHXM_FKRZ',fdl.pmingc) beiz,\n" + 
	"           nvl(round_new(round_new(sum(ah)/3300,5)*nvl(getbiaoz(round_new(sum(fdl.rez * ah) / sum(ah), 5),'KHXM_FKRZ',fdl.pmingc)*\n" + 
	"           getchaocjz(abs(getchaz(round_new(sum(rez * ah) / sum(ah), 5),'KHXM_FKRZ',fdl.pmingc)),round_new(sum(rez * ah) / sum(ah),5),'KHXM_FKRZ')\n" + 
	"\n" + 
	"            ,0),3),0) jif,1 as xuh\n" + 
	" from \n" + 
	"(select f.id,g.mingc gmingc,m.mingc mmingc,p.mingc pmingc,f.laimsl,f.jingz,f.yingk,z.mt,z.mad,z.stad,z.vad,z.aad\n" + 
	",z.qnet_ar,round_new(nvl(z.qnet_ar,0) * 1000 / 4.1816, 0) rez\n" + 
	"\n" + 
	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
	"and f.pinzb_id=p.id\n" + 
	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
	")fdl,\n" + 
	"(\n" + 
	"select f.id, g.mingc,m.mingc,p.mingc, sum(nvl(f.jingz, 0)) as ah\n" + 
	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
	"and f.pinzb_id=p.id\n" + 
	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
	"group by f.id,g.mingc,m.mingc,p.mingc\n" + 
	") mh\n" + 
	"where fdl.id=mh.id\n" + 
	"\n" + 
	"group by rollup(fdl.gmingc, fdl.mmingc,fdl.pmingc )\n" + 
	"having (grouping(fdl.gmingc)+grouping(fdl.mmingc)+grouping(fdl.pmingc)) =3 or\n" + 
	"(grouping(fdl.gmingc)+grouping(fdl.mmingc)+grouping(fdl.pmingc))=0 or\n" + 
	"(grouping(fdl.gmingc)+grouping(fdl.mmingc)+grouping(fdl.pmingc))=0"+

//	"(" +
//	"select f.id,g.mingc gmingc,m.mingc mmingc,p.mingc pmingc,f.laimsl,f.jingz,f.yingk,z.mt,z.mad,z.stad,z.vad,z.aad\n" + 
//	",z.qnet_ar,round_new(nvl(z.qnet_ar,0) * 1000 / 4.1816, 0) rez\n" + 
//	"\n" + 
//	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
//	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
//	"and f.pinzb_id=p.id\n" + 
//	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
//	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
//	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
//	")fdl,\n" + 
//	"(\n" + 
//	"select f.id, g.mingc,m.mingc,p.mingc, sum(nvl(f.jingz, 0)) as ah\n" + 
//	" from fahb f,meikxxb m,pinzb p,gongysb g,zhilb z\n" + 
//	"where f.gongysb_id=g.id  and f.meikxxb_id=m.id and f.zhilb_id=z.id\n" + 
//	"and f.pinzb_id=p.id\n" + 
//	"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
//	"and to_char(f.daohrq,'yyyy-mm-dd') >='"+getBRiq()+"'\n" + 
//	"and to_char(f.daohrq,'yyyy-mm-dd') <='"+getERiq()+"'\n" + 
//	"group by f.id,g.mingc,m.mingc,p.mingc\n" + 
//	") mh\n" + 
//	"where fdl.id=mh.id --and fdl.pmingc= 'ԭú'or fdl.pmingc= '��ú'\n" + 
//	"\n" + 
//	"group by rollup(fdl.gmingc,mmingc,fdl.pmingc )\n" + 
//	"having (grouping(fdl.gmingc)+grouping(mmingc)+grouping(fdl.pmingc)) =3\n" + 
//	"\n" + 
	"\n" + 
	")\n" + 
	"))\n" + 
	"ORDER BY XUH,gmingc";


		ArrHeader=new String[2][15];
		ArrHeader[0]=new String[]{"��Ӧ��","ú��","ú��","����","ӯ��","ȫˮ<br>(Mt)","��ˮ<br>(Mad)","���<br>(Stad)","�ӷ���<br>(Vad)"
				,"�ҷ�<br>(Aad)","��ֵ<br>(Qnet_ar)","��ֵ<br>(Qnet_ar)","��ֵ<br>(Qnet_ar)","��ֵ<br>(Qnet_ar)","��ֵ<br>(Qnet_ar)"};
	    ArrHeader[1]=new String[] {"��Ӧ��","ú��","ú��","��","��","%","%","%","%","%","(Mj/Kg)","(Kcal/Kg)","���˻�׼","�۷ֱ�׼<br>(��/��)","�Ʒ�"};
		ArrWidth =new int[] {110,80,65,55,55,55,55,55,55,55,55,55,55,55,55};
		ResultSet rs = cn.getResultSet(ss);
//		System.out.println(strSQL.toString());
		// ����
		rt.setBody(new Table(rs,2, 0, 3));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.setTitle(getBRiq().replaceFirst("-", "��").replaceFirst("-", "��").replaceFirst("-", "��")+"��"+getERiq().replaceFirst("-", "��").replaceFirst("-", "��").replaceFirst("-", "��")+""+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "���λ:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 2, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_CENTER);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(17);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		
		rt.body.ShowZero = false;
		rt.body.merge(3, 1, 3, 2);
		for(int i=1;i<9;i++){
			
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
//		rt.body.setTableClass(Table.ALIGN_CENTER);
		rt.body.setCells(3, 1, 3, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		//ҳ�� 
		
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(5,2,"���:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(10,2,"�Ʊ�:",Table.ALIGN_RIGHT);
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
//		System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
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

	/*private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}*/
	
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
	//��������
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
//		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setWidth(120);
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));

//		tb1.addText(new ToolbarText("���:"));
//		ComboBox nianf = new ComboBox();
//		nianf.setTransform("NIANF");
//		nianf.setWidth(60);
//		nianf.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(nianf);
//		tb1.addText(new ToolbarText("-"));
//		
//		tb1.addText(new ToolbarText("�·�:"));
//		ComboBox yuef = new ComboBox();
//		yuef.setTransform("YUEF");
//		yuef.setWidth(60);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(yuef);
//		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ʼ����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" ��������: "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
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
	
//	������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	//�糧��
	private String treeid;
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
//	 ��������
	public boolean _Baoblxchange = false;

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if (_BaoblxValue == null) {
			_BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
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
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "�ֳ�"));
			fahdwList.add(new IDropDownBean(1, "�ֿ�"));
			fahdwList.add(new IDropDownBean(2, "�ֳ��ֿ�"));
			fahdwList.add(new IDropDownBean(3, "�ֿ�ֳ�"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IBaoblxModel;
	}

	
}