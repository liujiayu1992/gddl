package com.zhiren.jt.dtsx;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.File;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.tools.FtpUpload;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.tools.FtpCreatTxt;


import org.apache.tapestry.contrib.palette.SortMode;

/**
 * @author huochaoyuan
 * �����ֹ�˾�ձ�������
 * (�ձ��ۺϲ�ѯ���պĴ汨���պĴ汨���ⱨ������ú�����ձ����ⱨ��)
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class Shouhcreport_tz  extends BasePage implements PageValidateListener{
//	�ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
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
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.quanc as mingc from diancxxb dc where dc.id="
			+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}
	private String userName="";

	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}

	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}


	//��ʼ����v
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

	private boolean _ShangcChick = false;

	public void ShangcButton(IRequestCycle cycle) {
		_ShangcChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if(_ShangcChick){
			_ShangcChick=false;
		}
	}


	private void Refurbish() {
		//Ϊ"ˢ��" ��ť��Ӵ������
		isBegin=true;
//		getSelectData();
		getShouhcrb();
	}

//	******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());

			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			isBegin=true;
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setTreeid(null);
//			this.getShouhcrb();
//			this.getSelectData();

		}
		if (_BeginriqChange){
			_BeginriqChange=false;
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
//		this.getShouhcrb();
		setUserName(visit.getRenymc());
		getToolBars();
		Refurbish();
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (getBaoblxValue().getValue().equals("�պĴ��ձ���")){
			return getShouhcrb();
		}else if(getBaoblxValue().getValue().equals("���˺�ú��Ϣ��")){
			return getDiaoyhmxx();
		}else if(getBaoblxValue().getValue().equals("�������ձ���")){
			return getJingjkcrb();
		}else if(getBaoblxValue().getValue().equals("��ú�����ձ���(�ⱨ)")){
			return getDianmdlrb();
		}else if(getBaoblxValue().getValue().equals("�պĴ��ܱ���")){
			return getShouhzrb();
		}else if(getBaoblxValue().getValue().equals("�����ú��Ϣ�ձ�")){
			return getDiancjhdmxxrb();
		}else if(getBaoblxValue().getValue().equals("�պĴ��ձ���(�ⱨ)")){
			return getShouhcrb_wb();
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
	//������Ϣ�ձ�

	private String getDiancjhdmxxrb(){
		int jib=this.getDiancTreeJib();
		if (jib==1){
			return getDiancjhdmxxrb_B3();
		}else if(jib==2){
			return getDiancjhdmxxrb_B2();
		}
		return "";
	}
	private String getDiancjhdmxxrb_B3(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);

		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="���缯�ŵ�ú��Ϣ�ձ�";

		//��������
		titlename=titlename+"";

		strSQL="select decode(grouping(sf.quanc)+grouping(dc.mingc),2,'ȫ���ܼ�',1,sf.quanc||'�ϼ�',dc.mingc) as diancmc,\n"
			+ "  sum(nvl(vw.zongrl,0)/10) as zongzjrl,jizgcinfo(dc.id) as jizjg,\n"
			+ "         sum(shc.dangrgm) as gongml,sum(shc.haoyqkdr) as haoml,\n"
			+ "         sum(shc.kuc) as kucl, round(decode(sum(nvl(vw.zongrl,0)),0,0,(sum(shc.kuc)/(sum(nvl(vw.zongrl,0)/10)*24*0.85*5))),1) as keyts,\n"
			+ "   decode(grouping(dc.id),0,max(kuc_rb),'') as beiz,sum(shc.quemtjts) as quemtjts,sum(shc.quemtjrl) as quemtjrl\n"
			+ "   from (select h.*,case when jingjkc_rb(h.diancxxb_id) >kuc_rb (h.diancxxb_id,h.riq) then '���ھ�����' end as kuc_rb from Shouhcrbb h where h.riq ="+riq+"  )shc,\n"
			+ "        vwjizxx vw,shengfb sf,dianckjpxb px,diancxxb dc\n"
			+ "   where vw.diancxxb_id(+)=dc.id\n"
			+ "         and dc.shengfb_id=sf.id(+)\n"
			+ "         and dc.id=shc.diancxxb_id(+)\n"
			+ "         and px.diancxxb_id=dc.id\n"
			+ "     and px.kouj='��ú�����ձ���'\n"
			+ "         group by rollup(sf.quanc,(dc.id,dc.mingc,shc.riq,shc.beiz))\n"
			+ "   order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc";

		ArrHeader=new String[2][10];
		ArrHeader[0]=new String[] {"�糧����","��װ��<br>(��ǧ��)","����ṹ<br>(̨*��ǧ��)","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","ȱúͣ��","ȱúͣ��"};
		ArrHeader[1]=new String[] {"�糧����","��װ��<br>(��ǧ��)","����ṹ<br>(̨*��ǧ��)","��ú��<br>(��)","��ú��<br>(��)","�����<br>(��)","����<br>����","��ú����<br>����ˮƽ��ע","̨","����<br>(��ǧ��)"};

		ArrWidth=new int[] {200,55,100,60,60,60,40,120,40,60};

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 1));
		rt.setTitle(titlename+"<br>(����"+riq1+")", ArrWidth);
		rt.setDefaultTitle(1, 2, "���Ƶ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 3, "��λ:��ǧ�ߡ��֡�̨����", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		//ҳ�� 

		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiancjhdmxxrb_B2(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�����ú��Ϣ�ձ���";
		String strGongsID = "";
		strGongsID=" and dc.fuid= " +this.getTreeid();

		//��������
		titlename=titlename+"";
		strSQL=
			"select decode(grouping(sf.quanc)+grouping(dc.mingc),2,'�ܼ�',1,sf.quanc||'С��',dc.mingc) as diancmc,\n" +
			"             decode(1,1,'�����������繫˾') as diancgs,sum(nvl(vw.zongrl,0)/10) as zongrl,JizgcInfo(dc.id) as jizgc,sum(shc.fadl) as fadl,\n" + 
			"             sum(shc.dangrgm) as gml,sum(shc.haoyqkdr) as hml,sum(shc.kuc) as kuc,shc.beiz,\n" + 
			"              Round(sum(shc.kuc)/(sum(round(nvl(vw.zongrl,0)/10,2))*24*0.85*5),1) as tians,\n" + 
			"              sum(shc.quemtjts) as quemtjts,sum(shc.quemtjrl) as quemtjrl\n" + 
			"      from (select * from Shouhcrbb h\n" + 
			"                 where h.riq="+riq+")shc," +
			"			(select dc.id, dc.mingc as mingc,dc.shengfb_id as shengfb_id, dl.mingc as leix, dc.zhengccb,dc.xuh \n" + 
			" 			  from diancxxb dc, dianclbb dl\n" + 
			"			  where dc.dianclbb_id = dl.id(+)"+strGongsID+" )dc,shengfb sf,vwjizxx vw,dianckjpxb px\n" + 
			"      where shc.diancxxb_id(+)=dc.id and dc.shengfb_id=sf.id\n" + 
			"            and vw.diancxxb_id=dc.id\n" + 
			"            and px.diancxxb_id=dc.id and px.kouj='��ú�����ձ���'\n" + 
			"       group by rollup(sf.quanc,(dc.id,dc.mingc,shc.beiz))\n" + 
			"       order by grouping(sf.quanc)desc ,max(sf.xuh),grouping(dc.mingc)desc,max(dc.xuh)";

		ArrHeader=new String[2][12];
		ArrHeader[0]=new String[] {"�糧����","�糧����","װ������<br>(��ǧ��)","���鹹��<br>(̨*��ǧ��)","������<br>(��ǧ��ʱ)","��ú��<br>(��)","��ú��<br>(��)","��ú���<br>(��)","���ھ�����<br>Ӧ��ע","����<br>����","ȱúͣ��","ȱúͣ��"};
		ArrHeader[1]=new String[] {"�糧����","�糧����","װ������<br>(��ǧ��)","���鹹��<br>(̨*��ǧ��)","������<br>(��ǧ��ʱ)","��ú��<br>(��)","��ú��<br>(��)","��ú���<br>(��)","���ھ�����<br>Ӧ��ע","����<br>����","̨","����<br>(��ǧ��)"};

		ArrWidth=new int[] {200,110,60,80,60,60,60,80,80,40,40,60};

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle(titlename+"<br>(����"+riq1+")", ArrWidth);
		rt.setDefaultTitle(1, 2, "���Ƶ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		//rt.setDefaultTitle(3, 3, riq1, Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 4, "��λ:��ǧ�ߡ��֡�̨����", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		Visit visit=(Visit) getPage().getVisit();
		rt.setDefautlFooter(1, 2, "�Ʊ�ʱ�䣺"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "����:",Table.ALIGN_LEFT);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				rt.setDefautlFooter(9, 4, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
				rt.setDefautlFooter(9, 4, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(9,4,"�Ʊ�",Table.ALIGN_LEFT);
		//ҳ�� 

		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	//������
	private String getJingjkcrb(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�������ձ���";
		String strGongsID = "";
		String danwmc="";//��������
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";//��������
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";//��������
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
//		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
		/*strSQL=
			"select decode(grouping(sj.danwmc),1,'"+danwmc+"',sj.danwmc) as danwmc,\n" +
			"       sum(sj.zhuangjrl/10) as zhuangjrl,sum(sj.gongmqk) as gongmqk,sum(sj.haoyqk) as haoyqk,\n" + 
			"       sum(sj.kuc) as kuc,sum(sj.jingjkc) as jingjkc,sum(diyjjxml) as diyjjxml,\n" + 
			"       sum(kuckyts) as kuckyts,sum(rizdhml) as rizdhml,\n" + 
			"       round(sum(sj.kuckyts)*max(cs.fuhl),1) as fuhkyts,round(sum(sj.rizdhml)*max(cs.fuhl),0) as fuhhml from\n" + 
			"(select dc.mingc as danwmc,\n" + 
			"       sum(nvl(jz.jizurl,0)) as zhuangjrl,sum(nvl(hc.dangrgm,0)) as gongmqk,sum(nvl(hc.haoyqkdr,0)) as haoyqk,\n" + 
			"       sum(nvl(hc.kuc,0)) as kuc,sum(nvl(dc.jingjcml,0)) as jingjkc,sum(nvl(hc.kuc,0))-sum(nvl(dc.jingjcml,0)) as diyjjxml,\n" + 
			"       decode(sum(nvl(dc.rijhm,0)),0,0,round(sum(hc.kuc)/sum(dc.rijhm),1)) as kuckyts,sum(nvl(dc.rijhm,0)) as rizdhml\n" + 
			"  from diancxxb dc,jizb jz,shouhcrbb hc\n" + 
			" where jz.diancxxb_id=dc.id and hc.diancxxb_id=dc.id "+strGongsID+" \n" + 
			"   and hc.riq="+riq+" \n" + 
			" group by dc.mingc  ) sj,\n" + 
			" (select max(kyts) as keyts,max(fuhl) as fuhl from\n" + 
			"         (select distinct decode(xt.mingc,'�������������',xt.zhi,'') as kyts,\n" + 
			"         decode(xt.mingc,'�����渺����',xt.zhi,'') as fuhl from xitxxb xt)) cs\n" + 
			"where sj.kuckyts<cs.keyts\n" + 
			"group by rollup(sj.danwmc) order by grouping(sj.danwmc) desc,sj.danwmc";
		*/
		
		
		 strSQL=
			"select decode(grouping(sj.danwmc),1,'"+danwmc+"',sj.danwmc) as danwmc,\n" +
			"  sum(sj.zhuangjrl/10) as zhuangjrl,sum(sj.gongmqk) as gongmqk,sum(sj.haoyqk) as haoyqk,sum(sj.kuc) as kuc,\n" + 
			"  decode(grouping(sj.danwmc),1,sum(decode(shanghdl+yangsp,1,0,shjj)),max(shjj)) as jingjkc,\n" + 
			"  decode(grouping(sj.danwmc),1,sum(decode(shanghdl+yangsp,1,0,shkc-shjj)),max(shkc-shjj)) as diyjjkcl,\n" + 
			"  decode(grouping(sj.danwmc),1,round(sum(sj.kuc)/sum(rizdhml),1),round(keyts_rb(max(sj.id),"+riq+"),1))  as keyts,\n" + 
			"  sum(rizdhml) as rizdhml,\n" + 
			"  decode(grouping(sj.danwmc),1,round(sum(sj.kuc)/sum(round(rizdhml*fuhl,0)),1),round(keyts_rb(max(sj.id),"+riq+")/max(fuhl),1))  as keyts_fh,\n" + 
			"  sum(round(sj.rizdhml*cs.fuhl,0)) as fuhhml\n" + 
			"from\n" + 
			"(select max(dc.mingc) as danwmc,dc.id,isShangHdl(dc.id) as shanghdl,isYansspdc(dc.id) as yangsp,\n" + 
			"       sum(nvl(jz.jizurl,0)) as zhuangjrl,sum(nvl(hc.dangrgm,0)) as gongmqk,sum(nvl(hc.haoyqkdr,0)) as haoyqk,\n" + 
			"       sum(nvl(hc.kuc,0)) as kuc,sum(nvl(dc.jingjcml,0)) as jingjkc,sum(nvl(hc.kuc,0))-sum(nvl(dc.jingjcml,0)) as diyjjxml,\n" + 
			"       decode(sum(nvl(dc.rijhm,0)),0,0,round(sum(hc.kuc)/sum(dc.rijhm),1)) as kuckyts,sum(nvl(dc.rijhm,0)) as rizdhml,\n" + 
			"       jingjkc_rb(dc.id) as shjj,kuc_rb(dc.id,"+riq+" )   as shkc\n" + 
			"  from diancxxb dc,\n" + 
			"       (select diancxxb_id ,sum(jizurl) as jizurl from jizb group by diancxxb_id ) jz,\n" + 
			"       Shouhcrbb hc\n" + 
			" where jz.diancxxb_id=dc.id and hc.diancxxb_id=dc.id "+strGongsID+"\n" + 
			"   and hc.riq="+riq+"\n" + 
			" group by dc.id  ) sj,\n" + 
			" (select max(kyts) as keyts,max(fuhl) as fuhl from\n" + 
			"         (select distinct decode(xt.mingc,'�������������',xt.zhi,'') as kyts,\n" + 
			"         decode(xt.mingc,'�����渺����',xt.zhi,'') as fuhl from xitxxb xt)) cs\n" + 
			"where shjj>shkc\n" + 
			"group by rollup(sj.danwmc) order by grouping(sj.danwmc) desc,sj.danwmc";


		//System.out.println(strSQL);
		ArrHeader = new String[2][11];
		ArrHeader[0] = new String[] {"��λ����","��װ������","��ú���(��)","�������(��)","���ú̿<br>(��)","��澯��<br>��(��)","���ھ�����ú��(��)","Ŀǰ����������(��)","������ú��(��)","��"+getFuhl()+"������<br>�������","��"+getFuhl()+"������<br>�պ�ú��"};
		ArrHeader[1] = new String[] {"��λ����","��ǧ��ʱ"," ���� ","����","���ú̿<br>(��)","��澯��<br>��(��)","���ھ�����ú��(��)","Ŀǰ����������(��)","������ú��(��)","��"+getFuhl()+"������<br>�������","��"+getFuhl()+"������<br>�պ�ú��"};

		ArrWidth = new int[] {200,70,80,80,60,60,80,80,80,80,80};
		ResultSet rs = cn.getResultSet(strSQL);
		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle("�������ձ���", ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, FormatDate(getBeginriqDate()), Table.ALIGN_LEFT);
//		rt.setDefaultTitle(10, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(9,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getFuhl(){
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String strfuhl = "";
		String sql = "select zhi from xitxxb where mingc='�����渺����'";
		try{
			rs = con.getResultSet(sql);
			if(rs.next()){
				strfuhl = Math.round(rs.getDouble("zhi")*100)+"%";
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return strfuhl;
	}

//	��ú�����ձ�
	private String getDianmdlrb(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�������ձ���";
		String strGongsID = "";
		String danwmc="";//��������
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			danwmc=getTreeDiancmc(this.getTreeid());
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}

//		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
		strSQL="select decode(grouping(dc.mingc)+grouping(sf.quanc)+grouping(dq.quanc),3,'"+danwmc+"',2,'&nbsp;&nbsp;'||dq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n" +
		"       sum(round(nvl(jz.zongrl,0)/10,2)) as zongjzrl,sum(jz.tais) as tais,JizgcInfo(dc.id) as jizgc,sum(zr.zuorfdl) as zuorfdl,\n" + 
		"       sum(zr.zuorhm) as zuorhm,sum(zrlm.lm) as zuorlm,sum(lj.leijhm) as leijhm,sum(ljlm.lm) as leijlm,\n" + 
		"       sum(zr.zuorkc) as zuorkc,\n" + 
		"       decode(grouping(dc.id),1,decode(sum(dc.rijhm),0,0,null,0,round(sum(zr.zuorkc)/sum(dc.rijhm),0)),round(keyts_rb(dc.id,"+riq+"),0)) as keyts,\n" + 
		"       sum(zr.quemtjts) as quemtjts,sum(zr.quemtjrl) as quemtjrl\n" + 
		"  from diancxxb dc,shengfb sf,shengfdqb dq,vwjizxx jz,dianckjpxb px,\n" + 
		"(select rb.diancxxb_id,nvl(rb.fadl,0) as zuorfdl,nvl(rb.haoyqkdr,0) as zuorhm,\n" + 
		"        nvl(rb.shangbkc,0) as zuorkc,nvl(rb.quemtjts,0) as quemtjts,nvl(rb.quemtjrl,0) as quemtjrl\n" + 
		"   from Shouhcrbb rb where rb.riq="+riq+" ) zr,\n" + 
		"(select rb.diancxxb_id,sum(rb.haoyqkdr) as leijhm from Shouhcrbb rb\n" + 
		" where rb.riq>=to_date('2008-01-01','yyyy-mm-dd') and rb.riq<="+riq+"   \n" + 
		" group by rb.diancxxb_id ) lj,\n" + 
		"(select diancxxb_id,sum(nvl(biaoz,0)) as lm from fahb where daohrq="+riq+"\n" +
		"         group by diancxxb_id\n" + 
		"         )zrlm,\n"+
		"(select diancxxb_id,sum(nvl(biaoz,0)) as lm from fahb where daohrq<="+riq+"\n" +
		"         and to_char(daohrq,'yyyy-mm-dd')>='2008-01-01'\n" + 
		"         group by diancxxb_id\n" + 
		"         )ljlm\n"+
		" where zr.diancxxb_id(+)=dc.id and ljlm.diancxxb_id(+)=dc.id\n " +
		"and zrlm.diancxxb_id=dc.id(+) and lj.diancxxb_id(+)=dc.id and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id\n" + 
		"   and jz.diancxxb_id=dc.id "+strGongsID+"and px.diancxxb_id=dc.id and px.kouj='��ú�����ձ���' and px.shujsbzt=1\n" + 
		" group by rollup(dq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
		" order by grouping(dq.quanc) desc,max(dq.xuh),dq.quanc,grouping(sf.quanc) desc, max(sf.xuh),sf.quanc,grouping(dc.mingc) desc,max(px.xuh)\n" + 
		"";
		//System.out.println(strSQL);
		ArrHeader = new String[2][13];
		ArrHeader[0]=new String[] {"�糧����","��װ��<br>����","̨��","���鹹��","����<br>������","����<br>��ú","����<br>��ú","�ۼ�<br>��ú","�ۼ�<br>��ú","����<br>��ú","����<br>����","ȱúͣ��","ȱúͣ��"};
		ArrHeader[1]=new String[] {"�糧����","��ǧ��","̨","̨*��ǧ��","���"," �� ","��"," �� ","��"," �� ","��","̨","����"};

		ArrWidth = new int[] {170,40,30,80,45,50,50,55,55,55,30,25,35};

		ResultSet rs = cn.getResultSet(strSQL);
		// ����
		rt.setBody(new Table(rs,2, 2, 1));

		rt.setTitle(getDiancName()+"��ú�����ձ���("+FormatDate(getBeginriqDate())+")", ArrWidth);
		rt.setDefaultTitle(1,  2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4,  FormatDate(DateUtil.AddDate(getBeginriqDate(), 1, DateUtil.AddType_intDay)), Table.ALIGN_CENTER);
		rt.setDefaultTitle(10,4,"��λ:�֡�ǧ��ʱ��̨",Table.ALIGN_RIGHT);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.setColCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER );

		rt.body.ShowZero = true;

		int tais3 = 0;
		int tais7 = 0;
		double rongl3 = 0;
		double rongl7 = 0;
		String beiz = "";
		String tjsql = 
			"select sum(case when keyts<=3 then tais else 0 end) as tais3,\n" +
			"sum(case when keyts<=3 then zongjzrl else 0 end) as zongjzrl3,\n" + 
			"sum(case when keyts<=7 then tais else 0 end) as tais7,\n" + 
			"sum(case when keyts<=7 then zongjzrl else 0 end) as zongjzrl7\n" + 
			"from \n" + 
			"(select dc.id,sum(jz.tais) as tais,sum(round(nvl(jz.zongrl,0)/10,2)) as zongjzrl,\n" + 
			"      sum(round(keyts_rb(dc.id,"+riq+"),0)) as keyts\n" + 
			"  from diancxxb dc,dianckjpxb px,vwjizxx jz,\n" + 
			"(select rb.diancxxb_id,nvl(rb.shangbkc,0) as zuorkc\n" + 
			"   from Shouhcrbb rb where rb.riq="+riq+" ) zr\n" + 
			" where zr.diancxxb_id=dc.id and px.diancxxb_id=dc.id "+strGongsID+" \n" + 
			"       and jz.diancxxb_id=dc.id and px.kouj='��ú�����ձ���' and px.shujsbzt=1 group by dc.id )";

		String bzsql = "select bz.beiz from shouhcrbbzb bz,diancxxb dc "
			+ " where bz.diancxxb_id=dc.id and bz.riq="+riq+" "+strGongsID+"";
		try{
			ResultSet res = cn.getResultSet(tjsql);
			ResultSet bzrs = cn.getResultSet(bzsql);
			if(res.next()){
				tais3 = res.getInt("tais3");
				tais7 = res.getInt("tais7");
				rongl3 = res.getDouble("zongjzrl3");
				rongl7 = res.getDouble("zongjzrl7");
			}
			if(bzrs.next()){
				beiz = bzrs.getString("beiz");
			}
			res.close();
			bzrs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		rt.body.merge(rt.body.getRows()-1, 2, rt.body.getRows()-1, 3);
		rt.body.merge(rt.body.getRows()-1, 4, rt.body.getRows()-1, 5);
		rt.body.merge(rt.body.getRows()-1, 6, rt.body.getRows()-1, 8);
		rt.body.merge(rt.body.getRows()-1, 9, rt.body.getRows()-1, 10);
		rt.body.merge(rt.body.getRows()-1, 11, rt.body.getRows()-1, 13);

		rt.body.merge(rt.body.getRows(), 2, rt.body.getRows(), 13);

		rt.body.setCellValue(rt.body.getRows()-1, 1, "��ú��������<=7");
		rt.body.setCellValue(rt.body.getRows()-1, 2, tais7+"̨");
		rt.body.setCellValue(rt.body.getRows()-1, 4, rongl7+"��ǧ��");

		rt.body.setCellValue(rt.body.getRows()-1, 6, "��ú��������<=3");
		rt.body.setCellValue(rt.body.getRows()-1, 9, tais3+"̨");
		rt.body.setCellValue(rt.body.getRows()-1, 11, rongl3+"��ǧ��");

		rt.body.setCellValue(rt.body.getRows(), 1, "��ע��");
		rt.body.setCellValue(rt.body.getRows(), 2, beiz);

		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		Visit visit=(Visit) getPage().getVisit();
		rt.setDefautlFooter(1, 2, "�Ʊ�ʱ�䣺"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 3, "���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				rt.setDefautlFooter(11, 3, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
				rt.setDefautlFooter(11, 3, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(11,3, "�Ʊ�:",Table.ALIGN_RIGHT);
		//ҳ�� 
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(11,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		return rt.getAllPagesHtml();
	}

	//�պĴ��ձ���
	private String getShouhcrb(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�պĴ��ձ�";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL="  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'"+danwmc+"',\n" +
			"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
			"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
			"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
			"  from (select * from Shouhcrbb h\n" + 
			"        where h.riq = to_date("+riq+") ) shc,\n" + 
			"  (select dc.id, dc.mingc as mingc,dc.shengfb_id as shengfb_id \n" + 
			"    from diancxxb dc, diancxxb df \n" +
			"	  where dc.jib=3 \n" +
			"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
			"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
			"from Shouhcrbb h\n" + 
			"  where h.riq >= First_day("+riq+")\n" + 
			"    and h.riq <= "+riq+"\n" + 
			"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
			"where  dc.id = lj.diancxxb_id(+) and dc.id=shc.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
			"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
			"       and dckj.kouj='�պĴ��ձ�' and dc.id=shc.diancxxb_id(+)\n" + 
			"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
			"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dckj.xuh)";


		}else{
			strSQL=
				"select decode(dc.mingc,null,'�ϼ�',dc.mingc) as dc,\n" +
				"       sum(shc.dangrgm) as dangrgm,\n" + 
				"       sum(lj.dangrgm) as ljdangrgm,\n" + 
				"       sum(shc.haoyqkdr) as haoyqkdr,\n" + 
				"       sum(lj.haoyqkdr) as ljhaoyqkdr,\n" + 
				"       sum(shc.kuc) as kuc\n" + 
				"  from (select *\n" + 
				"          from Shouhcrbb h, diancxxb dc\n" + 
				"         where h.riq = "+riq+"\n" + 
				"           and h.diancxxb_id = dc.id\n" + 
				"           "+strGongsID+") shc,\n" + 
				"       (select h.diancxxb_id,\n" + 
				"               sum(h.dangrgm) as dangrgm,\n" + 
				"               sum(h.haoyqkdr) as haoyqkdr\n" + 
				"          from Shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"         group by h.diancxxb_id) lj,\n" + 
				"       --dianckjpxb dckj,\n diancxxb dc\n" + 
				" where shc.diancxxb_id = lj.diancxxb_id(+)\n" + 
				" and shc.diancxxb_id=dc.id\n" + 
				"  -- and dckj.diancxxb_id = dc.id\n" + 
				"   --and dckj.kouj = '�պĴ��ձ�'\n" + 
				" group by rollup(dc.mingc)";
/**
 * huochaoyuan 2008-12-17
 * �ϱ�sql��Ϊdianckjpxb�������ݵ��²�������dianckjpxb���ò��������Ƚ���ע����
 */			

		}
		ArrHeader=new String[2][6];
		ArrHeader[0]=new String[] {"��λ����","��ú���(��)","��ú���(��)","�������(��)","�������(��)","���ú̿(��)"};
		ArrHeader[1]=new String[] {"��λ����","����","�ۼ�","����","�ۼ�","���ú̿"};

		ArrWidth=new int[] {240,90,90,90,90,90};
		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,  riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(5, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
//		ҳ�� 
		rt.createDefautlFooter(ArrWidth);
		Visit visit=(Visit) getPage().getVisit();
		rt.setDefautlFooter(1, 2, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				rt.setDefautlFooter(6, 1, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
				rt.setDefautlFooter(6, 1, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(6, 1, "�Ʊ�",Table.ALIGN_RIGHT);
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getShouhcrb_wb(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�պĴ��ձ�";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL="  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'"+danwmc+"',\n" +
			"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
			"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
			"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
			"  from (select * from Shouhcrbb h\n" + 
			"        where h.riq = to_date("+riq+") ) shc,\n" + 
			"  (select dc.id, dc.mingc as mingc,dc.shengfb_id as shengfb_id \n" + 
			"    from diancxxb dc, diancxxb df \n" +
			"	  where dc.jib=3 \n" +
			"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
			"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
			"from Shouhcrbb h\n" + 
			"  where h.riq >= First_day("+riq+")\n" + 
			"    and h.riq <= "+riq+"\n" + 
			"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
			"where  dc.id = lj.diancxxb_id(+) and dc.id=shc.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
			"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
			"       and dckj.kouj='�պĴ��ձ�' and dc.id=shc.diancxxb_id(+)\n" + 
			"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
			"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dckj.xuh)";


		}else{
			strSQL=
			"select decode(dc.mingc, null, '�ϼ�', dc.mingc) as dc,\n" +
			"       sum(dr.lm) as dangrgm,\n" + 
			"       sum(ljlm.lm) as ljdangrgm,\n" + 
			"       sum(shc.haoyqkdr) as haoyqkdr,\n" + 
			"       sum(ljhy.haoyqkdr) as ljhaoyqkdr,\n" + 
			"       sum(shc.kuc) as kuc\n" + 
			"  from (select h.diancxxb_id, h.haoyqkdr, h.kuc\n" + 
			"          from Shouhcrbb h\n" + 
			"         where h.riq = "+riq+") shc,\n" + 
			"       (select h.diancxxb_id, sum(h.haoyqkdr) as haoyqkdr\n" + 
			"          from Shouhcrbb h\n" + 
			"         where h.riq >= First_day("+riq+")\n" + 
			"           and h.riq <= "+riq+"\n" + 
			"         group by h.diancxxb_id) ljhy,\n" + 
			"       (select f.diancxxb_id, nvl(sum(f.biaoz), 0) as lm\n" + 
			"          from fahb f, diancxxb dc\n" + 
			"         where f.daohrq ="+riq+"\n" + 
			"           and f.diancxxb_id = dc.id(+)\n" + 
			strGongsID+
			"         group by f.diancxxb_id) dr,\n" + 
			"       (select f.diancxxb_id, nvl(sum(f.biaoz), 0) as lm\n" + 
			"          from fahb f\n" + 
			"         where f.daohrq >= First_day("+riq+")\n" + 
			"           and f.daohrq <= "+riq+"\n" + 
			"         group by f.diancxxb_id) ljlm,\n" + 
			"       dianckjpxb dckj,\n" + 
			"       diancxxb dc\n" + 
			" where dr.diancxxb_id = shc.diancxxb_id(+)\n" + 
			"   and dr.diancxxb_id = ljlm.diancxxb_id(+)\n" + 
			"   and dr.diancxxb_id = ljhy.diancxxb_id(+)\n" + 
			"   and dr.diancxxb_id = dc.id\n" + 
			"   and dckj.diancxxb_id = dc.id\n" + 
			"   and dckj.kouj = '�պĴ��ձ�'\n" + 
			" group by rollup(dc.mingc)";


		}
		ArrHeader=new String[2][6];
		ArrHeader[0]=new String[] {"��λ����","��ú���(��)","��ú���(��)","�������(��)","�������(��)","���ú̿(��)"};
		ArrHeader[1]=new String[] {"��λ����","����","�ۼ�","����","�ۼ�","���ú̿"};

		ArrWidth=new int[] {240,90,90,90,90,90};
		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,  riq1, Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		Visit visit=(Visit) getPage().getVisit();
		rt.setDefautlFooter(1, 2, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "���:",Table.ALIGN_LEFT);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				rt.setDefautlFooter(5, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
				rt.setDefautlFooter(5, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(5, 2, "�Ʊ�",Table.ALIGN_RIGHT);
		//ҳ�� 

		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getDiaoyhmxx(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="���˺�ú��Ϣ��";
//		int iFixedRows=0;//�̶��к�
//		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
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
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL=
				"select dc.bianm as �糧����,dc.mingc as �糧����,to_char(shcb.riq,'yyyy-mm-dd') as �ձ�����,shcb.dangrgm as ���չ�ú,lj.dangrgm as ���ۼƹ�ú,\n" +
				"                shcb.haoyqkdr as ���պ�ú, lj.haoyqkdr as ���ۼƺ�ú,shcb.tiaozl as ��������, shcb.kuc as ���տ��\n" + 
				"     from (select * from Shouhcrbb h\n" + 
				"        where h.riq = "+riq+" )shcb,\n" + 
				"     (select dc.id, dc.mingc as mingc,dc.bianm as bianm, df.mingc as leix, dc.zhengccb \n"+
				"	  from diancxxb dc, diancxxb df \n" +
				"	  where dc.jib=3 \n" +
				"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
				"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"       from Shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"      group by h.diancxxb_id)lj\n" + 
				"      where shcb.diancxxb_id=dc.id and dc.id = lj.diancxxb_id\n" + 
				"      order by dc.bianm";
		}else{
			strSQL=
				"select dc.bianm as �糧����,dc.mingc as �糧����,to_char(shcb.riq,'yyyy-mm-dd') as �ձ�����,shcb.dangrgm as ���չ�ú,lj.dangrgm as ���ۼƹ�ú,\n" +
				"                shcb.haoyqkdr as ���պ�ú, lj.haoyqkdr as ���ۼƺ�ú,shcb.tiaozl as ��������, shcb.kuc as ���տ��\n" + 
				"     from (select * from Shouhcrbb h\n" + 
				"        where h.riq = to_date("+riq+") )shcb,\n" + 
				"     (select dc.id, dc.mingc as mingc,dc.bianm as bianm, dl.mingc as leix, dc.zhengccb\n" + 
				"          from diancxxb dc, dianclbb dl\n" + 
				"         where dc.dianclbb_id = dl.id(+) "+strGongsID+")dc,\n" + 
				"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"       from Shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"       group by h.diancxxb_id)lj\n" + 
				"      where shcb.diancxxb_id=dc.id and dc.id = lj.diancxxb_id\n" + 
				"      order by dc.bianm";

		}
		ArrHeader =new String[1][9];
		ArrHeader[0]=new String[] {"�糧����","�糧����","�ձ�����","���չ�ú<br>(��)","�ۼƹ�ú<br>(��)","���պ�ú<br>(��)","�ۼƺ�ú<br>(��)","��������<br>(��)","���տ��<br>(��)"};
		ArrWidth=new int[] {60,200,110,65,65,65,65,65,65};
//		iFixedRows=1;
//		iCol=14;

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, 2));

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, riq1, Table.ALIGN_CENTER);
//		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 

		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}

	private String getSelectData(){

		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�Ĵ��ձ�";
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
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL=
				"select decode(grouping(dc.leix) + grouping(dc.mingc),\n" +
				"              2,\n" + 
				"              '"+danwmc+"',\n" + 
				"              1,\n" + 
				"              '��' || dc.leix || 'С��',\n" + 
				"              max(dc.mingc)) as diancmc,\n" + 
				"       sum(dh.hej * 10000) as ���¼ƻ�,\n" + 
				"       sum(round(dh.hej * 10000 /\n" + 
				"                 daycount("+riq+"),\n" + 
				"                 0)) as �վ��ƻ�,\n" + 
				"       sum(h.dangrgm) as ���չ�ú,\n" + 
				"       sum(lj.dangrgm) as �ۼƹ�ú,\n" + 
				"       sum((round(dh.hej * 10000 /\n" + 
				"                  daycount("+riq+"),\n" + 
				"                  0) * to_char("+riq+", 'dd'))) as �ۼ�Ӧ��,\n" + 
				"       sum(((round(dh.hej / daycount("+riq+"),\n" + 
				"                   0) *\n" + 
				"           to_char("+riq+", 'dd')) - h.dangrgm)) as ��Ƿ,\n" + 
				"       sum(round(lj.dangrgm / (dh.hej * 10000), 2)) as ������,\n" + 
				"       sum(h.dangrgm) as ����,\n" + 
				"       sum(lj.dangrgm) as �ۼ�,\n" + 
				"       sum(h.haoyqkdr) as ���պ���,\n" + 
				"       sum(lj.haoyqkdr) as �ۼƺ���,\n" + 
				"       sum(h.kuc) as ���,  sum(h.tiaozl) as ������,\n" + 
				"       decode(grouping(dc.mingc),\n" + 
				"              1,\n" + 
				"              '',\n" + 
				"              sum(decode(hm.meizrjhm, 0, 0, round(h.kuc / hm.meizrjhm)))) as ��������,\n" + 
				"       sum(dc.zhengccb) as ����������\n" + 
				"  from (select *\n" + 
				"          from Shouhcrbb h\n" + 
				"         where h.riq = "+riq+") h,\n" + 
				"       (select h.diancxxb_id, sum(hs.hetl) as hej\n" + 
				"          from hetb h, hetslb hs\n" + 
				"         where hs.hetb_id = h.id\n" + 
				"           and h.liucztb_id = 1\n" + 
				"           and hs.riq = First_day("+riq+")\n" + 
				"         group by h.diancxxb_id) dh,\n" + 
				"       (select h.diancxxb_id,\n" + 
				"               sum(h.dangrgm) as dangrgm,\n" + 
				"               sum(h.haoyqkdr) as haoyqkdr\n" + 
				"          from Shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"         group by h.diancxxb_id) lj,\n" + 
				"       (  select dc.id, dc.mingc as mingc, df.mingc as leix, dc.zhengccb\n" + 
				"           from diancxxb dc, diancxxb df\n" + 
				"          where dc.jib=3\n" + 
				"          and dc.fuid = df.id(+) "+strGongsID+") dc,\n" + 
				"       (select hc.diancxxb_id,\n" + 
				"               nvl(round(sum(hc.haoyqkdr) / 7), 0) as meizrjhm\n" + 
				"          from Shouhcrbb hc\n" + 
				"         where hc.riq >= "+riq+" - 7\n" + 
				"           and hc.riq <= "+riq+" - 1\n" + 
				"         group by (hc.diancxxb_id)) hm,\n" + 
				"       dianckjpxb px\n" + 
				" where h.diancxxb_id(+) = dc.id\n" + 
				"   and dc.id = dh.diancxxb_id(+)\n" + 
				"   and dc.id = lj.diancxxb_id(+)\n" + 
				"   and hm.diancxxb_id(+) = dc.id\n" + 
				"   and px.diancxxb_id = dc.id\n" + 
				"   and px.kouj = '����ȼ���ձ�'\n" + 
				" group by rollup(dc.leix, (dc.mingc, px.xuh))\n" + 
				" order by grouping(dc.leix) desc,\n" + 
				"          dc.leix desc,\n" + 
				"          grouping(dc.mingc) desc,\n" + 
				"          px.xuh";


		}else{
			strSQL=
				"\n" +
				"select decode(grouping(dc.leix) + grouping(dc.mingc),\n" + 
				"              2,\n" + 
				"              '"+danwmc+"',\n" + 
				"              1,\n" + 
				"              '��' || dc.leix || 'С��',\n" + 
				"              max(dc.mingc)) as diancmc,\n" + 
				"       sum(dh.hej * 10000) as ���¼ƻ�,\n" + 
				"       sum(round(dh.hej * 10000 /\n" + 
				"                 daycount("+riq+"),\n" + 
				"                 0)) as �վ��ƻ�,\n" + 
				"       sum(h.dangrgm) as ���չ�ú,\n" + 
				"       sum(lj.dangrgm) as �ۼƹ�ú,\n" + 
				"       sum((round(dh.hej * 10000 /\n" + 
				"                  daycount("+riq+"),\n" + 
				"                  0) * to_char("+riq+", 'dd'))) as �ۼ�Ӧ��,\n" + 
				"       sum(((round(dh.hej / daycount("+riq+"),\n" + 
				"                   0) *\n" + 
				"           to_char("+riq+", 'dd')) - h.dangrgm)) as ��Ƿ,\n" + 
				"       sum(round(lj.dangrgm / (dh.hej * 10000), 2)) as ������,\n" + 
				"       sum(h.dangrgm) as ����,\n" + 
				"       sum(lj.dangrgm) as �ۼ�,\n" + 
				"       sum(h.haoyqkdr) as ���պ���,\n" + 
				"       sum(lj.haoyqkdr) as �ۼƺ���,\n" + 
				"       sum(h.kuc) as ���,  sum(h.tiaozl) as ������,\n" + 
				"       decode(grouping(dc.mingc),\n" + 
				"              1,\n" + 
				"              '',\n" + 
				"              sum(decode(hm.meizrjhm, 0, 0, round(h.kuc / hm.meizrjhm)))) as ��������,\n" + 
				"       sum(dc.zhengccb) as ����������\n" + 
				"  from (select *\n" + 
				"          from Shouhcrbb h\n" + 
				"         where h.riq = "+riq+") h,\n" + 
				"       (select h.diancxxb_id, sum(hs.hetl) as hej\n" + 
				"          from hetb h, hetslb hs\n" + 
				"         where hs.hetb_id = h.id\n" + 
				"           and h.liucztb_id = 1\n" + 
				"           and hs.riq = First_day("+riq+")\n" + 
				"         group by h.diancxxb_id) dh,\n" + 
				"       (select h.diancxxb_id,\n" + 
				"               sum(h.dangrgm) as dangrgm,\n" + 
				"               sum(h.haoyqkdr) as haoyqkdr\n" + 
				"          from Shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"         group by h.diancxxb_id) lj,\n" + 
				"       (select dc.id, dc.mingc as mingc, dl.mingc as leix, dc.zhengccb\n" + 
				"          from diancxxb dc, dianclbb dl\n" + 
				"         where dc.dianclbb_id = dl.id(+) "+strGongsID+") dc,\n" + 
				"       (select hc.diancxxb_id,\n" + 
				"               nvl(round(sum(hc.haoyqkdr) / 7), 0) as meizrjhm\n" + 
				"          from Shouhcrbb hc\n" + 
				"         where hc.riq >= "+riq+" - 7\n" + 
				"           and hc.riq <= "+riq+" - 1\n" + 
				"         group by (hc.diancxxb_id)) hm,\n" + 
				"       dianckjpxb px\n" + 
				" where h.diancxxb_id(+) = dc.id\n" + 
				"   and dc.id = dh.diancxxb_id(+)\n" + 
				"   and dc.id = lj.diancxxb_id(+)\n" + 
				"   and hm.diancxxb_id(+) = dc.id\n" + 
				"   and px.diancxxb_id = dc.id\n" + 
				"   and px.kouj = '����ȼ���ձ�'\n" + 
				" group by rollup(dc.leix, (dc.mingc, px.xuh))\n" + 
				" order by grouping(dc.leix) desc,\n" + 
				"          dc.leix desc,\n" + 
				"          grouping(dc.mingc) desc,\n" + 
				"          px.xuh";

		}

		//System.out.println(strSQL);
		ArrHeader =new String[3][16];
		ArrHeader[0]=new String[] {"��λ����","��ͬ�ƻ�ִ�����","��ͬ�ƻ�ִ�����","��ͬ�ƻ�ִ�����","��ͬ�ƻ�ִ�����","��ͬ�ƻ�ִ�����","��ͬ�ƻ�ִ�����","��ͬ�ƻ�ִ�����","��ú����","��ú����","�������","�������","���","������","��������","����������"};
		ArrHeader[1]=new String[] {"��λ����","���¼ƻ�","�վ��ƻ�","���չ�ú","�ۼƹ�ú","�ۼ�Ӧ��","��Ƿ + -","������ %","����","�ۼ�","����","�ۼ�","���","������","��������","����������"};
		ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
		ArrWidth=new int[] {130,60,60,60,60,60,60,60,50,60,50,60,60,50,50,60};
		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,3, 0, iFixedRows));

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 3, riq1, Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 
		/*
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
		 * rt.setDefautlFooter(4,1,"�Ʊ�:",Table.ALIGN_LEFT);
		 * rt.setDefautlFooter(6,1,"���:",Table.ALIGN_LEFT);
		 * rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 */
		//����ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(14,3,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
//	******************************************************************************
	//�պĴ��ձ���
	private String getShouhzrb(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�պĴ��ܱ�";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="���Ź�˾�ϼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";
		if(jib==1){//ѡ���ŵ�ʱ���շֹ�˾ͳ��,�����İ��յ�������ͳ��
			strSQL="  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'"+danwmc+"',\n" +
			"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
			"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
			"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
			"  from (select * from Shouhcrbb h\n" + 
			"        where h.riq = to_date("+riq+") ) shc,\n" + 
			"  (select dc.id, dc.mingc as mingc,dc.shengfb_id as shengfb_id \n" + 
			"    from diancxxb dc, diancxxb df \n" +
			"	  where dc.jib=3 \n" +
			"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
			"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
			"from Shouhcrbb h\n" + 
			"  where h.riq >= getMondayDate("+riq+")\n" + 
			"    and h.riq <= "+riq+"\n" + 
			"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
			"where  dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
			"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
			"       and dckj.kouj='�պĴ��ձ�'and dckj.shujsbzt=1 and dc.id=shc.diancxxb_id(+)\n" + 
			"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
			"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dckj.xuh)";


		}else{
			strSQL=

				"  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'���Ź�˾�ϼ�',\n" +
				"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
				"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
				"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
				"  from (select * from Shouhcrbb h\n" + 
				"        where h.riq = to_date("+riq+") ) shc,\n" + 
				"  (select dc.id, dc.mingc as mingc,dc.shengfb_id as shengfb_id, dl.mingc as leix, dc.zhengccb\n" + 
				"   from diancxxb dc, dianclbb dl\n" + 
				"  where dc.dianclbb_id = dl.id(+)"+strGongsID+" \n" + 
				"  )dc,\n" + 
				"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"from Shouhcrbb h\n" + 
				"  where h.riq >= getMondayDate("+riq+")\n" + 
				"    and h.riq <= "+riq+"\n" + 
				"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
				"where  dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
				"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
				"       and dckj.kouj='�պĴ��ձ�'and dckj.shujsbzt=1\n" + 
				"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
				"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dckj.xuh)";

		}
		ArrHeader=new String[2][6];
		ArrHeader[0]=new String[] {"��λ����","��ú���(��)","��ú���(��)","�������(��)","�������(��)","���ú̿(��)"};
		ArrHeader[1]=new String[] {"��λ����","����","�ۼ�","����","�ۼ�","���ú̿"};

		ArrWidth=new int[] {240,90,90,90,90,90};
		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, riq1, Table.ALIGN_LEFT);
//		rt.setDefaultTitle(7, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 

		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}
//	******************************************************************************
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
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
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
			sql = "select id,meikdqmc from meikdqb order by meikdqmc";
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
//		fahdwList.add(new IDropDownBean(0,"���˺�ú��Ϣ��"));
		fahdwList.add(new IDropDownBean(0,"�պĴ��ձ���"));
//		fahdwList.add(new IDropDownBean(2,"�������ձ���"));
		fahdwList.add(new IDropDownBean(1,"��ú�����ձ���(�ⱨ)"));
//		fahdwList.add(new IDropDownBean(4,"�պĴ��ܱ���"));
		fahdwList.add(new IDropDownBean(2,"�պĴ��ձ���(�ⱨ)"));

		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(fahdwList));

		return ((Visit)getPage().getVisit()).getProSelectionModel1();
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
//	�ֹ�˾������
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

		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
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

		tb1.addText(new ToolbarText("�����ѯ:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setId("Tongjkj");
		cb.setWidth(120);
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