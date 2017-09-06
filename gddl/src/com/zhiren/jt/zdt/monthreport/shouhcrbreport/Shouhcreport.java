/*
 * ʱ�䣺2008-08-22
 * ���ߣ��»���
 * ���������������ļ��㷽�������仯,��������=���/�պ�ú��
 * �������û�к�ú��������к������ݵ�10���ƽ��ֵ
 * Լ��������е�Ͷ�����޸�
 */
/*
 * ʱ�䣺2009-07-21
 * ���ߣ�����
 * �������޸ĵ����ú��Ϣ�ձ�sql,�Էֹ�˾�鿴ʱҳ���й�����λ����
 */


/*ʱ��:2009-8-5 18:02:39
 * ����:���ܱ�
 * ����:���ӿ��Աȱ�
 * 
 */
package com.zhiren.jt.zdt.monthreport.shouhcrbreport;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.tools.FtpCreatTxt;


import org.apache.tapestry.contrib.palette.SortMode;


public class Shouhcreport  extends BasePage implements PageValidateListener{
	
	private String ZhongNbm="000007";
	private String ZhongNyh="000007";
	private String ZhongNmm="000007";
	private String ZhongNip="210.77.176.26";
	private String Baohys;
	
	
	private boolean returnMsg=false;
	private boolean hasSaveMsg=false;
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
    
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
		
		if(diancmc.equals("���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���")){
	          return "���ƹ���ȼ�Ϲ���";
		}else{
			return diancmc;
		}
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
	
	

	//��������v
	private Date _EndriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (_EndriqValue==null){
			_EndriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _EndriqValue;
	}

	public void setEndriqDate(Date _value) {
		if (FormatDate(_EndriqValue).equals(FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			_EndriqValue = _value;
			_EndriqChange=true;
		}
	}

	
	
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ShangcChick = false;

	public void ShangcButton(IRequestCycle cycle) {
		_ShangcChick = true;
	}

	private boolean _ShangbChick = false;

	public void ShangbButton(IRequestCycle cycle) {
		_ShangbChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		returnMsg=false;
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if(_ShangcChick){
			_ShangcChick=false;
		}
		if (_ShangbChick){
			_ShangbChick=false;
			ShangbTXTFile();
			returnMsg=true;
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
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
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
		}else if(getBaoblxValue().getValue().equals("�����ձ�")){
			return getShangbzn();
		}else if(getBaoblxValue().getValue().equals("�������ձ���")){
			return getJingjkcrb();
		}else if(getBaoblxValue().getValue().equals("��ú�����ձ���")){
			return getDianmdlrb();
		}else if(getBaoblxValue().getValue().equals("�պĴ��ܱ���")){
			return getShouhzrb();
		}else if(getBaoblxValue().getValue().equals("�����ú��Ϣ�ձ�")){
			return getDiancjhdmxxrb();
		}else if(getBaoblxValue().getValue().equals("���˺�ú��Ϣ��")){
			return getDiaoyhmxx();
		}else if(getBaoblxValue().getValue().equals("���Աȱ�")){
			return getKucdbb();
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

		strSQL="select decode(grouping(sf.quanc)+grouping(dc.mingc),2,'�ܼ�',1,'&nbsp;&nbsp;'||sf.quanc||'�ϼ�','&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,\n"
			+ "  sum(nvl(vw.zongrl,0)/10) as zongzjrl,jizgcinfo(dc.id) as jizjg,\n"
			+ "         sum(shc.dangrgm) as gongml,sum(shc.haoyqkdr) as haoml,\n"
			//+ "         sum(shc.kuc) as kucl, round(decode(sum(nvl(vw.zongrl,0)),0,0,(sum(shc.kuc)/(sum(nvl(vw.zongrl,0)/10)*24*0.85*5))),1) as keyts,\n"
			+ "	sum(shc.kuc) as kucl,decode(grouping(dc.id),1,decode(sum(shc.rijhm),0,0,null,0,round(sum(shc.kuc)/sum(shc.rijhm),0)),round(keyts_rb(dc.id,"+riq+"),0)) as keyts,\n"
			+ "   decode(grouping(dc.id),0,max(kuc_rb),'') as beiz,sum(shc.quemtjts) as quemtjts,sum(shc.quemtjrl) as quemtjrl\n"
			+ "   from (select nvl(keyts_rijhm(h.diancxxb_id,h.haoyqkdr,h.riq),0) as rijhm,h.*,case when jingjkc_rb(h.diancxxb_id) >kuc_rb (h.diancxxb_id,h.riq) then '���ھ�����' end as kuc_rb from shouhcrbb h where h.riq ="+riq+"  )shc,\n"
			+ "        vwjizxx vw,shengfb sf,dianckjpxb px,diancxxb dc\n"
			+ "   where vw.diancxxb_id(+)=dc.id\n"
			+ "         and dc.shengfb_id=sf.id(+)\n"
			+ "         and dc.id=shc.diancxxb_id(+)\n"
			+ "         and px.diancxxb_id=dc.id\n"
			+ "     and px.kouj='��ú�����ձ���' \n"
//			-------------------------------------------
			+ "		and shc.diancxxb_id(+)=dc.id \n"
//			-------------------------------------------
			+ "         group by rollup(sf.quanc,(dc.id,dc.mingc,shc.riq,shc.beiz))\n"
			+ "   order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc";

		ArrHeader=new String[2][10];
		ArrHeader[0]=new String[] {"�糧����","��װ��<br>(��ǧ��)","����ṹ<br>(̨*��ǧ��)","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","10��ǧ�߼�����ȼú�糧��ú���","ȱúͣ��","ȱúͣ��"};
		ArrHeader[1]=new String[] {"�糧����","��װ��<br>(��ǧ��)","����ṹ<br>(̨*��ǧ��)","��ú��<br>(��)","��ú��<br>(��)","�����<br>(��)","����<br>����","��ú����<br>����ˮƽ��ע","̨","����<br>(��ǧ��)"};

		ArrWidth=new int[] {150,55,100,60,60,60,40,120,40,60};

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 1));
		rt.setTitle(titlename+"<br>(����"+riq1+")", ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, FormatDate(getBeginriqDate()), Table.ALIGN_CENTER);
		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(10,2, "�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
		 
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
		strGongsID=" and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";

		//��������
		titlename=titlename+"";
		strSQL=
			"select decode(grouping(sf.quanc)+grouping(dc.mingc),2,'�ܼ�',1,'&nbsp;&nbsp;'||sf.quanc||'С��',dc.mingc) as diancmc,\n" +
			"             decode(grouping(dc.mingc),0,'&nbsp;&nbsp;'||max(d.fgsmc)) as diancgs,sum(nvl(vw.zongrl,0)/10) as zongrl,JizgcInfo(dc.id) as jizgc,sum(shc.fadl) as fadl,\n" + 
			"             sum(shc.dangrgm) as gml,sum(shc.haoyqkdr) as hml,sum(shc.kuc) as kuc,shc.beiz,\n" + 
			//"              Round(sum(shc.kuc)/(sum(round(nvl(vw.zongrl,0)/10,2))*24*0.85*5),1) as tians,\n" +
			"	decode(grouping(dc.id),1,decode(sum(shc.rijhm),0,0,null,0,round(sum(shc.kuc)/sum(shc.rijhm*0.85),0)),round(keyts_rb(dc.id,"+riq+"),0)) as keyts,\n"+
			"              sum(shc.quemtjts) as quemtjts,sum(shc.quemtjrl) as quemtjrl\n" + 
			"      from (select nvl(keyts_rijhm(h.diancxxb_id,h.haoyqkdr,h.riq),0) as rijhm,h.* from shouhcrbb h\n" + 
			"                 where h.riq="+riq+")shc," +
			"			(select dc.id, dc.mingc as mingc,dc.shengfb_id as shengfb_id,dc.rijhm, dl.mingc as leix, dc.zhengccb,dc.xuh \n" + 
			" 			  from diancxxb dc, dianclbb dl\n" + 
			"			  where dc.dianclbb_id = dl.id(+)"+strGongsID+" )dc,shengfb sf,vwjizxx vw,dianckjpxb px,vwdianc d\n" + 
			"      where shc.diancxxb_id(+)=dc.id and dc.shengfb_id=sf.id and dc.id=d.id\n" + 
			"            and vw.diancxxb_id=dc.id\n" + 
			"            and px.diancxxb_id=dc.id and px.kouj='��ú�����ձ���'\n" + 
			"       group by rollup(sf.quanc,(dc.id,dc.mingc,shc.beiz))\n" + 
			"       order by grouping(sf.quanc)desc ,max(sf.xuh),grouping(dc.mingc)desc,max(dc.xuh)";

		ArrHeader=new String[2][12];
		ArrHeader[0]=new String[] {"�糧����","�糧����","װ������<br>(��ǧ��)","���鹹��<br>(̨*��ǧ��)","������<br>(��ǧ��ʱ)","��ú��<br>(��)","��ú��<br>(��)","��ú���<br>(��)","���ھ�����<br>Ӧ��ע","����<br>����","ȱúͣ��","ȱúͣ��"};
		ArrHeader[1]=new String[] {"�糧����","�糧����","װ������<br>(��ǧ��)","���鹹��<br>(̨*��ǧ��)","������<br>(��ǧ��ʱ)","��ú��<br>(��)","��ú��<br>(��)","��ú���<br>(��)","���ھ�����<br>Ӧ��ע","����<br>����","̨","����<br>(��ǧ��)"};

		ArrWidth=new int[] {150,100,60,80,60,60,60,80,80,40,40,60};

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 2));

		rt.setTitle(titlename+"<br>(����"+riq1+")", ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 3, riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 4, "��λ:��ǧ�ߡ���", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		//ҳ�� 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(5, 3, "���:",Table.ALIGN_CENTER);
		 rt.setDefautlFooter(11,2,"�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);

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
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
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
		
		
		 strSQL="select a.danwmc,a.zhuangjrl,a.gongmqk,a.haoyqk,a.kuc,a.jingjkc,a.diyjjkcl, "+
		 	"  decode(a.jingjkc,0,0,round(abs(a.diyjjkcl)*100/a.jingjkc,2)) as diyjjkcbfl,"+
		 	"  a.keyts,a.rizdhml,a.keyts_fh,a.fuhhml"+
		 	"  from ("+
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
			"       sum(nvl(keyts_rijhm(dc.id,hc.haoyqkdr,hc.riq),0)) as rizdhml,\n" + 
			"       jingjkc_rb(dc.id) as shjj,kuc_rb(dc.id,"+riq+" )   as shkc\n" + 
			"  from diancxxb dc,\n" + 
			"       (select diancxxb_id ,sum(jizurl) as jizurl from jizb group by diancxxb_id ) jz,\n" + 
			"       shouhcrbb hc\n" + 
			" where jz.diancxxb_id=dc.id and hc.diancxxb_id(+)=dc.id "+strGongsID+"\n" + 
			"   and hc.riq="+riq+"\n" + 
			" group by dc.id  ) sj,\n" + 
			" (select max(kyts) as keyts,max(fuhl) as fuhl from\n" + 
			"         (select distinct decode(xt.mingc,'�������������',xt.zhi,'') as kyts,\n" + 
			"         decode(xt.mingc,'�����渺����',xt.zhi,'') as fuhl from xitxxb xt)) cs\n" + 
			"where shjj>shkc\n" + 
			"group by rollup(sj.danwmc) order by grouping(sj.danwmc) desc,sj.danwmc) a";


		//System.out.println(strSQL);
		ArrHeader = new String[2][12];
		ArrHeader[0] = new String[] {"��λ����","��װ������","��ú���(��)","�������(��)","���ú̿<br>(��)","��澯��<br>��(��)","���ھ�����<br>ú��(��)","���ھ���<br>������(%)","Ŀǰ���<br>��������(��)","������ú��(��)","��"+getFuhl()+"������<br>�������","��"+getFuhl()+"������<br>�պ�ú��"};
		ArrHeader[1] = new String[] {"��λ����","��ǧ��ʱ"," ���� ","����","���ú̿<br>(��)","��澯��<br>��(��)","���ھ�����<br>ú��(��)","���ھ���<br>������(%)","Ŀǰ���<br>��������(��)","������ú��(��)","��"+getFuhl()+"������<br>�������","��"+getFuhl()+"������<br>�պ�ú��"};

		ArrWidth = new int[] {120,55,70,70,60,55,60,60,55,60,60,60};
	
		ResultSet rs = cn.getResultSet(strSQL);
		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle("�������ձ���", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 3,  FormatDate(getBeginriqDate()), Table.ALIGN_CENTER);
		rt.setDefaultTitle(11, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9,"0.0");
		rt.body.setColFormat(11,"0.0");
		rt.body.ShowZero = true;
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(5, 3, "���:",Table.ALIGN_CENTER);
		 rt.setDefautlFooter(11, 2,"�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
		 

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
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
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
		"       sum(zr.zuorhm) as zuorhm,sum(zr.zuorlm) as zuorlm,sum(lj.leijhm) as leijhm,sum(lj.leijlm) as leijlm,\n" + 
		"       sum(zr.zuorkc) as zuorkc,\n" + 
		"       decode(grouping(dc.id),1,decode(sum(zr.rijhm),0,0,null,0,round(sum(zr.zuorkc)/sum(zr.rijhm),0)),round(keyts_rb(dc.id,"+riq+"),0)) as keyts,\n" + 
		"       sum(zr.quemtjts) as quemtjts,sum(zr.quemtjrl) as quemtjrl\n" + 
		"  from diancxxb dc,shengfb sf,shengfdqb dq,vwjizxx jz,dianckjpxb px,\n" + 
		"(select nvl(keyts_rijhm(rb.diancxxb_id,rb.haoyqkdr,rb.riq),0) as rijhm,rb.diancxxb_id,nvl(rb.fadl,0) as zuorfdl,nvl(rb.haoyqkdr,0) as zuorhm,nvl(rb.dangrgm,0) as zuorlm,\n" + 
		"        nvl(rb.kuc,0) as zuorkc,nvl(rb.quemtjts,0) as quemtjts,nvl(rb.quemtjrl,0) as quemtjrl\n" + 
		"   from shouhcrbb rb where rb.riq="+riq+" ) zr,\n" + 
		"(select rb.diancxxb_id,sum(rb.dangrgm) as leijlm,sum(rb.haoyqkdr) as leijhm from shouhcrbb rb\n" + 
		" where rb.riq>=First_day("+riq+") and rb.riq<="+riq+"   \n" + 
		" group by rb.diancxxb_id ) lj\n" + 
		" where zr.diancxxb_id(+)=dc.id and lj.diancxxb_id(+)=dc.id and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id\n" + 
		"   and jz.diancxxb_id(+)=dc.id "+strGongsID+"and px.diancxxb_id=dc.id and px.kouj='��ú�����ձ���' \n" + 
		" group by rollup(dq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
		" order by grouping(dq.quanc) desc,max(dq.xuh),dq.quanc,grouping(sf.quanc) desc, max(sf.xuh),sf.quanc,grouping(dc.mingc) desc,max(px.xuh)\n" + 
		"";
		//System.out.println(strSQL);
		ArrHeader = new String[2][13];
		ArrHeader[0]=new String[] {"�糧����","��װ��<br>����","̨��","���鹹��","����<br>������","����<br>��ú","����<br>��ú","�ۼ�<br>��ú","�ۼ�<br>��ú","����<br>��ú","����<br>����","ȱúͣ��","ȱúͣ��"};
		ArrHeader[1]=new String[] {"�糧����","��ǧ��","̨","̨*��ǧ��","���"," �� ","��"," �� ","��"," �� ","��","̨","����"};

		ArrWidth = new int[] {150,40,30,80,45,50,50,55,55,55,30,25,35};

		ResultSet rs = cn.getResultSet(strSQL);
		// ����
		rt.setBody(new Table(rs,2, 2, 1));

		rt.setTitle(getDiancName()+"��ú�����ձ���("+FormatDate(getBeginriqDate())+")", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4, FormatDate(DateUtil.AddDate(getBeginriqDate(), 1, DateUtil.AddType_intDay)), Table.ALIGN_CENTER);
		rt.setDefaultTitle(11, 3, "��λ:�֡�����ʱ",Table.ALIGN_RIGHT);
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
			"(select rb.diancxxb_id,nvl(rb.kuc,0) as zuorkc\n" + 
			"   from shouhcrbb rb where rb.riq="+riq+" ) zr\n" + 
			" where zr.diancxxb_id=dc.id and px.diancxxb_id=dc.id "+strGongsID+" \n" + 
			"       and jz.diancxxb_id=dc.id and px.kouj='��ú�����ձ���'  group by dc.id )";

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
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
     	rt.setDefautlFooter(1, 3,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
     	rt.setDefautlFooter(5, 3, "���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(11,3, "�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
		
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
			danwmc="�ܼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";

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
			strSQL=
				"  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(gs.mingc)+grouping(dc.mingc),4,'"+danwmc+"',\n" +
				"  3,'&nbsp;&nbsp;'||sfdq.quanc,2,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,1,\n" + 
				"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
				"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
				"  from (select * from shouhcrbb h\n" + 
				"        where h.riq = to_date( "+riq+") ) shc,\n" + 
				"  (select dc.id,dc.fuid,dc.xuh, dc.mingc as mingc,dc.shengfb_id as shengfb_id\n" + 
				"    from diancxxb dc, diancxxb df\n" + 
				"    where dc.jib=3\n" + 
				"    and dc.fuid = df.id(+)"+strGongsID+" )dc,\n" + 
				"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"from shouhcrbb h\n" + 
				"  where h.riq >= First_day( "+riq+")\n" + 
				"    and h.riq <=  "+riq+"\n" + 
				"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq,diancxxb gs\n" + 
				"where shc.diancxxb_id(+)=dc.id and dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
				"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
				"       and dckj.kouj='�պĴ��ձ�' and dc.id=shc.diancxxb_id(+) and gs.id=dc.fuid\n" + 
				"group by rollup(sfdq.quanc,sf.quanc,gs.mingc,(dc.id,dc.mingc))\n" + 
				"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(gs.mingc) desc,max(gs.xuh),grouping(dc.mingc) desc,max(dc.xuh),max(dckj.xuh)";

			/*strSQL="  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'"+danwmc+"',\n" +
			"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
			"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
			"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
			"  from (select * from shouhcrbb h\n" + 
			"        where h.riq = to_date("+riq+") ) shc,\n" + 
			"  (select dc.id, dc.mingc as mingc,dc.shengfb_id as shengfb_id \n" + 
			"    from diancxxb dc, diancxxb df \n" +
			"	  where dc.jib=3 \n" +
			"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
			"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
			"from shouhcrbb h\n" + 
			"  where h.riq >= First_day("+riq+")\n" + 
			"    and h.riq <= "+riq+"\n" + 
			"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
			"where shc.diancxxb_id=dc.id and dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
			"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
			"       and dckj.kouj='�պĴ��ձ�' and dc.id=shc.diancxxb_id(+)\n" + 
			"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
			"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dckj.xuh)";*/


		}else if(jib==2){
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			
			try{
				ResultSet rl = cn.getResultSet(ranlgs);
				if(rl!=null){
					strSQL=
						"  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(gs.mingc)+grouping(dc.mingc),4,'"+danwmc+"',\n" +
						"  3,'&nbsp;&nbsp;'||sfdq.quanc,2,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,1,\n" + 
						"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
						"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
						"  from (select * from shouhcrbb h\n" + 
						"        where h.riq = to_date( "+riq+") ) shc,\n" + 
						"  (select dc.id,dc.fuid,dc.xuh, dc.mingc as mingc,dc.shengfb_id as shengfb_id\n" + 
						"    from diancxxb dc, diancxxb df\n" + 
						"    where dc.jib=3\n" + 
						"    and dc.fuid = df.id(+)"+strGongsID+" )dc,\n" + 
						"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
						"from shouhcrbb h\n" + 
						"  where h.riq >= First_day( "+riq+")\n" + 
						"    and h.riq <=  "+riq+"\n" + 
						"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq,diancxxb gs\n" + 
						"where shc.diancxxb_id(+)=dc.id and dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
						"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
						"       and dckj.kouj='�պĴ��ձ�' and dc.id=shc.diancxxb_id(+) and gs.id=dc.fuid\n" + 
						"group by rollup(sfdq.quanc,sf.quanc,gs.mingc,(dc.id,dc.mingc))\n" + 
						"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(gs.mingc) desc,max(gs.xuh),grouping(dc.mingc) desc,max(dc.xuh),max(dckj.xuh)";
				}else{
					strSQL=
						"  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'"+danwmc+"',\n" +
						"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
						"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
						"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
						"  from (select * from shouhcrbb h\n" + 
						"        where h.riq = to_date("+riq+") ) shc,\n" + 
						"  (select dc.id, dc.mingc as mingc,dc.xuh,dc.shengfb_id as shengfb_id, dl.mingc as leix, dc.zhengccb\n" + 
						"   from diancxxb dc, dianclbb dl\n" + 
						"  where dc.dianclbb_id = dl.id(+)"+strGongsID+" \n" + 
						"  )dc,\n" + 
						"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
						"from shouhcrbb h\n" + 
						"  where h.riq >= First_day("+riq+")\n" + 
						"    and h.riq <= "+riq+"\n" + 
						"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
						"where shc.diancxxb_id(+)=dc.id and dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
						"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
						"       and dckj.kouj='�պĴ��ձ�'\n" + 
						"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
						"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dc.xuh),max(dckj.xuh)";
				}				
				rl.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
				
		}else{
			strSQL=
				"  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'"+danwmc+"',\n" +
				"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
				"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
				"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
				"  from (select * from shouhcrbb h\n" + 
				"        where h.riq = to_date("+riq+") ) shc,\n" + 
				"  (select dc.id, dc.mingc as mingc,dc.xuh,dc.shengfb_id as shengfb_id, dl.mingc as leix, dc.zhengccb\n" + 
				"   from diancxxb dc, dianclbb dl\n" + 
				"  where dc.dianclbb_id = dl.id(+)"+strGongsID+" \n" + 
				"  )dc,\n" + 
				"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"from shouhcrbb h\n" + 
				"  where h.riq >= First_day("+riq+")\n" + 
				"    and h.riq <= "+riq+"\n" + 
				"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
				"where shc.diancxxb_id(+)=dc.id and dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
				"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
				"       and dckj.kouj='�պĴ��ձ�'\n" + 
				"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
				"having not grouping(sfdq.quanc) =1 \n" +
				"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dc.xuh),max(dckj.xuh)";

		}
		ArrHeader=new String[2][6];
		ArrHeader[0]=new String[] {"��λ����","��ú���(��)","��ú���(��)","�������(��)","�������(��)","���ú̿(��)"};
		ArrHeader[1]=new String[] {"��λ����","����","�ۼ�","����","�ۼ�","���ú̿(��)"};

		ArrWidth=new int[] {150,90,90,90,90,90};
		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2,   riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(5, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		if(jib==1){
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
		}
		
		//ҳ�� 
 
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�ʱ��"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3,2,"���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(5,2,"�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 

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
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";


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
				"     from (select * from shouhcrbb h\n" + 
				"        where h.riq = "+riq+" )shcb,\n" + 
				"     (select dc.id, dc.mingc as mingc,dc.bianm as bianm, df.mingc as leix, dc.zhengccb \n"+
				"	  from diancxxb dc, diancxxb df \n" +
				"	  where dc.jib=3 \n" +
				"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
				"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"       from shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+" \n" + 
				"      group by h.diancxxb_id)lj\n" + 
				"      where shcb.diancxxb_id(+)=dc.id and dc.id = lj.diancxxb_id\n" + 
				"      order by dc.bianm";
		}else{
			strSQL=
				"select dc.bianm as �糧����,dc.mingc as �糧����,to_char(shcb.riq,'yyyy-mm-dd') as �ձ�����,shcb.dangrgm as ���չ�ú,lj.dangrgm as ���ۼƹ�ú,\n" +
				"                shcb.haoyqkdr as ���պ�ú, lj.haoyqkdr as ���ۼƺ�ú,shcb.tiaozl as ��������, shcb.kuc as ���տ��\n" + 
				"     from (select * from shouhcrbb h\n" + 
				"        where h.riq = to_date("+riq+") )shcb,\n" + 
				"     (select dc.id, dc.mingc as mingc,dc.bianm as bianm, dl.mingc as leix, dc.zhengccb\n" + 
				"          from diancxxb dc, dianclbb dl\n" + 
				"         where dc.dianclbb_id = dl.id(+) "+strGongsID+")dc,\n" + 
				"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"       from shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"       group by h.diancxxb_id)lj\n" + 
				"      where shcb.diancxxb_id(+)=dc.id and dc.id = lj.diancxxb_id\n" + 
				"      order by dc.bianm";

		}
		ArrHeader =new String[1][9];
		ArrHeader[0]=new String[] {"�糧����","�糧����","�ձ�����","���չ�ú<br>(��)","�ۼƹ�ú<br>(��)","���պ�ú<br>(��)","�ۼƺ�ú<br>(��)","��������<br>(��)","���տ��<br>(��)"};
		ArrWidth=new int[] {60,120,110,60,60,60,60,60,60};
//		iFixedRows=1;
//		iCol=14;

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, 2));
		int rows=rt.body.getRows();
		int cols=rt.body.getCols();
		
		for (int i=2;i<rows;i++){
			String dangrgm=rt.body.getCellValue(i, 4);
			String dangrhm=rt.body.getCellValue(i, 6);
			
			if((dangrgm.equals("0") || dangrgm.equals("")) && (dangrhm.equals("0") || dangrhm.equals(""))){
				for (int j=0;j<cols+1;j++){
					rt.body.getCell(i, j).backColor="red";
				}
			}
		}
		

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3,  riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
	    rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 3, "���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 2, "�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
		 
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getShangbzn(){//�ϱ�����
	
		
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
		String titlename="�����ձ�";
//		int iFixedRows=0;//�̶��к�
//		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";

		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";


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
				"select dc.bianm as �糧����,decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as �糧����,to_char(shcb.riq,'yyyy-mm-dd') as �ձ�����,sum(shcb.dangrgm) as ���չ�ú,sum(lj.dangrgm) as ���ۼƹ�ú,\n" +
				"                sum(shcb.haoyqkdr) as ���պ�ú, sum(lj.haoyqkdr) as ���ۼƺ�ú,sum(shcb.tiaozl) as ��������, sum(shcb.kuc) as ���տ��\n" + 
//				"     from (select * from shouhcrbb h\n" + 
//				"        where h.riq = "+riq+" and h.diancxxb_id in (select diancxxb_id from dianckjpxb where kouj='�պĴ��ձ�' and shujsbzt=1) )shcb,\n" + 
				"     from (select dx.*,sh.dangrgm,sh.haoyqkdr,sh.tiaozl,sh.kuc,sh.riq from \n" +
				"     (select * from shouhcrbb h where h.riq="+riq+" ) sh,\n" +
				"     (select diancxxb_id from dianckjpxb where kouj = '�պĴ��ձ�' and shujsbzt = 1 ) dx \n" +
				"     where sh.diancxxb_id(+)=dx.diancxxb_id) shcb, \n" +
				
				"     (select dc.id, dc.mingc as mingc,dc.bianm as bianm, df.mingc as leix, dc.zhengccb \n"+
				"	  from diancxxb dc, diancxxb df \n" +
				"	  where dc.jib=3 \n" +
				"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
//				"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
//				"       from shouhcrbb h\n" + 
//				"         where h.riq >= First_day("+riq+")\n" + 
//				"           and h.riq <= "+riq+" and h.diancxxb_id in (select diancxxb_id from dianckjpxb where kouj='�պĴ��ձ�' and shujsbzt=1)\n" + 
//				"      group by h.diancxxb_id)lj\n" + 
				"      (select dx.diancxxb_id,sh.dangrgm,sh.haoyqkdr from \n" +
				"      (select h.diancxxb_id,sum(h.dangrgm) as dangrgm,sum(h.haoyqkdr) as haoyqkdr \n" +
				"      from shouhcrbb h where h.riq >= First_day("+riq+") \n" +
				"      and h.riq <= "+riq+" \n" +
				"      group by h.diancxxb_id) sh, \n" +
				"      (select diancxxb_id from dianckjpxb where kouj = '�պĴ��ձ�' and shujsbzt = 1) dx \n" +
				"      where sh.diancxxb_id(+)=dx.diancxxb_id ) lj \n" +
				
				"      where shcb.diancxxb_id=dc.id and dc.id = lj.diancxxb_id group by rollup (dc.bianm,dc.mingc,shcb.riq) having not (grouping(shcb.riq)=1 and grouping(dc.bianm) =0)\n" + 
				"      order by dc.bianm desc";
		}else{
			strSQL=
				"select dc.bianm as �糧����,decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as �糧����,to_char(shcb.riq,'yyyy-mm-dd') as �ձ�����,sum(shcb.dangrgm) as ���չ�ú,sum(lj.dangrgm) as ���ۼƹ�ú,\n" +
				"                sum(shcb.haoyqkdr) as ���պ�ú, sum(lj.haoyqkdr) as ���ۼƺ�ú,sum(shcb.tiaozl) as ��������, sum(shcb.kuc) as ���տ��\n" + 
//				"     from (select * from shouhcrbb h\n" + 
//				"        where h.riq = to_date("+riq+") and h.diancxxb_id in (select diancxxb_id from dianckjpxb where kouj='�պĴ��ձ�' and shujsbzt=1) )shcb,\n" +
				"     from (select dx.*,sh.dangrgm,sh.haoyqkdr,sh.tiaozl,sh.kuc,sh.riq from \n" +
				"     (select * from shouhcrbb h where h.riq="+riq+" ) sh,\n" +
				"     (select diancxxb_id from dianckjpxb where kouj = '�պĴ��ձ�' and shujsbzt = 1 ) dx \n" +
				"     where sh.diancxxb_id(+)=dx.diancxxb_id) shcb, \n" +
				
				"     (select dc.id, dc.mingc as mingc,dc.bianm as bianm, dl.mingc as leix, dc.zhengccb\n" + 
				"          from diancxxb dc, dianclbb dl\n" + 
				"         where dc.dianclbb_id = dl.id(+) "+strGongsID+")dc,\n" + 
//				"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
//				"       from shouhcrbb h\n" + 
//				"         where h.riq >= First_day("+riq+")\n" + 
//				"           and h.riq <= "+riq+" and h.diancxxb_id in (select diancxxb_id from dianckjpxb where kouj='�պĴ��ձ�' and shujsbzt=1)\n" + 
//				"       group by h.diancxxb_id)lj\n" + 
				"      (select dx.diancxxb_id,sh.dangrgm,sh.haoyqkdr from \n" +
				"      (select h.diancxxb_id,sum(h.dangrgm) as dangrgm,sum(h.haoyqkdr) as haoyqkdr \n" +
				"      from shouhcrbb h where h.riq >= First_day("+riq+") \n" +
				"      and h.riq <= "+riq+" \n" +
				"      group by h.diancxxb_id) sh, \n" +
				"      (select diancxxb_id from dianckjpxb where kouj = '�պĴ��ձ�' and shujsbzt = 1) dx \n" +
				"      where sh.diancxxb_id(+)=dx.diancxxb_id ) lj \n" +
				
				"      where shcb.diancxxb_id=dc.id and dc.id = lj.diancxxb_id group by rollup (dc.bianm,dc.mingc,shcb.riq) having not (grouping(shcb.riq)=1 and grouping(dc.bianm) =0)\n" + 
				"      order by dc.bianm desc";

		}
		ArrHeader =new String[1][9];
		ArrHeader[0]=new String[] {"�糧����","�糧����","�ձ�����","���չ�ú<br>(��)","�ۼƹ�ú<br>(��)","���պ�ú<br>(��)","�ۼƺ�ú<br>(��)","��������<br>(��)","���տ��<br>(��)"};
		ArrWidth=new int[] {60,120,90,60,60,60,60,60,60};
//		iFixedRows=1;
//		iCol=14;

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, 2));
		
		int rows=rt.body.getRows();
		int cols=rt.body.getCols();
		
		for (int i=3;i<rows+1;i++){
			String dangrgm=rt.body.getCellValue(i, 4);
//			������������int���Ϊdouble
			double dangrhm=0;
			if(rt.body.getCellValue(i, 6).equals("")){
				dangrhm=0;
			}else{
				dangrhm=Double.parseDouble(rt.body.getCellValue(i, 6));
			}
			
			if((dangrhm<200)){//(dangrgm.equals("0") || dangrgm.equals("")) && dangrhm.equals("0") || dangrhm.equals("")
				for (int j=0;j<cols+1;j++){
					rt.body.getCell(i, j).backColor="red";
				}
			}
		}

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3,  riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 2,"�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
		 

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
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";


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
				"          from shouhcrbb h\n" + 
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
				"          from shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"         group by h.diancxxb_id) lj,\n" + 
				"       (  select dc.id, dc.mingc as mingc, df.mingc as leix, dc.zhengccb\n" + 
				"           from diancxxb dc, diancxxb df\n" + 
				"          where dc.jib=3\n" + 
				"          and dc.fuid = df.id(+) "+strGongsID+") dc,\n" + 
				"       (select hc.diancxxb_id,\n" + 
				"               nvl(round(sum(hc.haoyqkdr) / 7), 0) as meizrjhm\n" + 
				"          from shouhcrbb hc\n" + 
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
				"          from shouhcrbb h\n" + 
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
				"          from shouhcrbb h\n" + 
				"         where h.riq >= First_day("+riq+")\n" + 
				"           and h.riq <= "+riq+"\n" + 
				"         group by h.diancxxb_id) lj,\n" + 
				"       (select dc.id, dc.mingc as mingc, dl.mingc as leix, dc.zhengccb\n" + 
				"          from diancxxb dc, dianclbb dl\n" + 
				"         where dc.dianclbb_id = dl.id(+) "+strGongsID+") dc,\n" + 
				"       (select hc.diancxxb_id,\n" + 
				"               nvl(round(sum(hc.haoyqkdr) / 7), 0) as meizrjhm\n" + 
				"          from shouhcrbb hc\n" + 
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
		ArrWidth=new int[] {150,60,60,60,60,60,60,60,50,60,50,60,60,50,50,60};
		iFixedRows=1;
		iCol=14;


		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,3, 0, iFixedRows));

		rt.setTitle(riq1+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 3,  riq1, Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(36);
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
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";

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
			"  from (select * from shouhcrbb h\n" + 
			"        where h.riq = to_date("+riq+") ) shc,\n" + 
			"  (select dc.id, dc.mingc as mingc,dc.xuh,dc.shengfb_id as shengfb_id \n" + 
			"    from diancxxb dc, diancxxb df \n" +
			"	  where dc.jib=3 \n" +
			"	  and dc.fuid = df.id(+)"+strGongsID+")dc,\n" + 
			"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
			"from shouhcrbb h\n" + 
			"  where h.riq >= getMondayDate("+riq+")\n" + 
			"    and h.riq <= "+riq+"\n" + 
			"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
			"where  dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
			"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
			"       and dckj.kouj='�պĴ��ձ�' and dc.id=shc.diancxxb_id(+)\n" + 
			"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
			"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dc.xuh),max(dckj.xuh)";


		}else{
			strSQL=

				"  select  decode(grouping(sfdq.quanc)+grouping(sf.quanc)+grouping(dc.mingc),3,'���Ź�˾�ϼ�',\n" +
				"  2,'&nbsp;&nbsp;'||sfdq.quanc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||sf.quanc,\n" + 
				"  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as diancmc,sum(shc.dangrgm) as dangrgm,\n" + 
				"  sum(lj.dangrgm) as ljdangrgm,sum(shc.haoyqkdr) as haoyqkdr,sum(lj.haoyqkdr) as ljhaoyqkdr,sum(shc.kuc) as kuc\n" + 
				"  from (select * from shouhcrbb h\n" + 
				"        where h.riq = to_date("+riq+") ) shc,\n" + 
				"  (select dc.id, dc.mingc as mingc,dc.xuh,dc.shengfb_id as shengfb_id, dl.mingc as leix, dc.zhengccb\n" + 
				"   from diancxxb dc, dianclbb dl\n" + 
				"  where dc.dianclbb_id = dl.id(+)"+strGongsID+" \n" + 
				"  )dc,\n" + 
				"  (select h.diancxxb_id,sum(h.dangrgm) as dangrgm, sum(h.haoyqkdr) as haoyqkdr\n" + 
				"from shouhcrbb h\n" + 
				"  where h.riq >= getMondayDate("+riq+")\n" + 
				"    and h.riq <= "+riq+"\n" + 
				"group by h.diancxxb_id)lj ,dianckjpxb dckj,shengfb sf,shengfdqb sfdq\n" + 
				"where  dc.id = lj.diancxxb_id(+) and dckj.diancxxb_id=dc.id\n" + 
				"       and dc.shengfb_id=sf.id and sf.shengfdqb_id= sfdq.id\n" + 
				"       and dckj.kouj='�պĴ��ձ�' \n" + 
//				--------------------------------------------------------------------
				"		and dc.id=shc.diancxxb_id(+) \n"+
//				--------------------------------------------------------------------
				"group by rollup(sfdq.quanc,sf.quanc,(dc.id,dc.mingc))\n" + 
				"order by grouping(sfdq.quanc) desc,max(sfdq.xuh),grouping(sf.quanc) desc,max(sf.xuh),grouping(dc.mingc) desc,max(dc.xuh),max(dckj.xuh)";

		}
		ArrHeader=new String[2][6];
		ArrHeader[0]=new String[] {"��λ����","��ú���(��)","��ú���(��)","�������(��)","�������(��)","���ú̿(��)"};
		ArrHeader[1]=new String[] {"��λ����","����","�ۼ�","����","�ۼ�","���ú̿(��)"};

		ArrWidth=new int[] {150,90,90,90,90,90};
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

		if(jib==1){
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
		}
		
		
		//ҳ�� 
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,2,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		  rt.setDefautlFooter(3,2,"���:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,2,"�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
	//    rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 

		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}
	private String getKucdbb(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq1=DateUtil.FormatDate(getBeginriqDate());
		String riq3=DateUtil.FormatDate(DateUtil.AddDate(getBeginriqDate(), -1, DateUtil.AddType_intDay));
		String riq2=DateUtil.FormatDate(getEndriqDate());
		int jib=getDiancTreeJib();
		String diancCondition=
			"and diancxxb_id in (select id from(\n" + 
			" 	select id from diancxxb\n" + 
			" 	start with id="+getTreeid()+"\n" + 
			" 	connect by fuid=prior id )\n" + 
			" union\n" + 
			" select id  from diancxxb\n" + 
			" 	where id="+getTreeid()+") " ; 
		
		if(jib==3){
			sbsql.append("select dc.mingc as danw, \n");
			sbsql.append("round(sum(dr.dangrgm),0) as dangrgm,round(sum(dr.haoyqkdr),0) as dangrhy,round(sum(qr.kuc),0) as qianrkc,round(sum(dangr.kuc),0) as dangrkc, \n");
			sbsql.append("round(sum(nvl(dangr.kuc,0)-nvl(qr.kuc,0)),0) as kucc,case when round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2)>=1000 then 1000 else round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2) end as zengjbl \n");
			sbsql.append("from diancxxb dc,vwfengs gs, \n");
			sbsql.append("(select diancxxb_id,sum(dangrgm) as dangrgm,sum(haoyqkdr) as haoyqkdr \n");
			sbsql.append("from shouhcrbb where riq>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
			sbsql.append("and riq<=to_date('"+riq2+"','yyyy-mm-dd') ").append(diancCondition).append(" group by diancxxb_id) dr, \n");
			sbsql.append("(select diancxxb_id,kuc from shouhcrbb where riq=to_date('"+riq2+"','yyyy-mm-dd')").append(diancCondition).append(" ) dangr, \n");
			sbsql.append("(select diancxxb_id,dangrgm,haoyqkdr,kuc from shouhcrbb  \n");
			sbsql.append("where riq=to_date('"+riq1+"','yyyy-mm-dd')-1 ").append(diancCondition).append(") qr \n");
			sbsql.append("where dc.fuid=gs.id and dr.diancxxb_id=dc.id and dr.diancxxb_id=qr.diancxxb_id(+)  and dr.diancxxb_id=dangr.diancxxb_id(+)  \n");
			sbsql.append("group by rollup(gs.mingc,dc.mingc) \n");
			sbsql.append("having not(grouping(dc.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh),dc.mingc \n");
		}else if(jib==2){
			diancCondition=
				"and diancxxb_id in (select id from(\n" + 
				" 	select id from diancxxb\n" + 
				" 	start with (fuid="+getTreeid()+" or shangjgsid="+getTreeid()+")\n" + 
				" 	connect by fuid=prior id )\n" + 
				" union\n" + 
				" select id  from diancxxb\n" + 
				" 	where (fuid="+getTreeid()+" or shangjgsid="+getTreeid()+")) " ; 
			

			String sql = "select decode( grouping(dc.mingc),1,'�ܼ�',dc.mingc) as danw,\n" +
			"       round(sum(dr.dangrgm), 0) as dangrgm,\n" + 
			"       round(sum(dr.haoyqkdr), 0) as dangrhy,\n" + 
			"       round(sum(qr.kuc), 0) as qianrkc,\n" + 
			"       round(sum(dangr.kuc), 0) as dangrkc,\n" + 
			"       round(sum(nvl(dangr.kuc, 0) - nvl(qr.kuc, 0)), 0) as kucc,\n" + 
			"       case\n" + 
			"         when round(decode(sum(qr.kuc), 0,1000,\n" + 
			"                           sum(dangr.kuc - qr.kuc) / sum(abs(qr.kuc))) * 100,\n" + 
			"                    2) >= 1000 then\n" + 
			"          1000\n" + 
			"         else\n" + 
			"          round(decode(sum(qr.kuc),\n" + 
			"                       0,\n" + 
			"                       1000,\n" + 
			"                       sum(dangr.kuc - qr.kuc) / sum(abs(qr.kuc))) * 100,\n" + 
			"                2)\n" + 
			"       end as zengjbl\n" + 
			"\n" + 
			"  from diancxxb dc,\n" + 
			"\n" + 
			"       (select diancxxb_id,\n" + 
			"               sum(dangrgm) as dangrgm,\n" + 
			"               sum(haoyqkdr) as haoyqkdr\n" + 
			"          from shouhcrbb\n" + 
			"         where riq >= to_date('" + riq1 + "', 'yyyy-mm-dd')\n" + 
			"           and riq <= to_date('" + riq2 + "', 'yyyy-mm-dd')\n" + 
			diancCondition+ 
			"         group by diancxxb_id) dr,\n" + 
			"\n" + 
			"       (select diancxxb_id, kuc\n" + 
			"          from shouhcrbb\n" + 
			"         where riq = to_date('" + riq2 + "', 'yyyy-mm-dd')\n" + 
			diancCondition +
			"		) dangr,\n" + 
			"\n" + 
			"       (select diancxxb_id, dangrgm, haoyqkdr, kuc\n" + 
			"          from shouhcrbb\n" + 
			"         where riq = to_date('" + riq1 + "', 'yyyy-mm-dd') - 1\n" + 
			diancCondition +
			") qr\n" + 
			"\n" + 
			" where dr.diancxxb_id = dc.id\n" + 
			"     and dr.diancxxb_id = qr.diancxxb_id(+)\n" + 
			"     and dr.diancxxb_id = dangr.diancxxb_id(+)\n" + 
			"\n" + 
			" group by rollup(dc.mingc)\n" + 
			" order by  grouping(dc.mingc) desc,\n" + 
			"          min(dc.xuh),\n" + 
			"          dc.mingc";
			
			sbsql.append(sql);

			

		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,'&nbsp&nbsp'||gs.mingc,'&nbsp&nbsp&nbsp&nbsp'||dc.mingc) as danw, \n");
			sbsql.append("round(sum(dr.dangrgm),0) as dangrgm,round(sum(dr.haoyqkdr),0) as dangrhy,round(sum(qr.kuc),0) as qianrkc,round(sum(dangr.kuc),0) as dangrkc, \n");
			sbsql.append("round(sum(nvl(dangr.kuc,0)-nvl(qr.kuc,0)),0) as kucc,case when round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2)>=1000 then 1000 else round(decode(sum(qr.kuc),0,1000,sum(dangr.kuc-qr.kuc)/sum(abs(qr.kuc)))*100,2) end as zengjbl \n");
			sbsql.append("from diancxxb dc,vwfengs gs, \n");
			sbsql.append("(select diancxxb_id,sum(dangrgm) as dangrgm,sum(haoyqkdr) as haoyqkdr \n");
			sbsql.append("from shouhcrbb where riq>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
			sbsql.append("and riq<=to_date('"+riq2+"','yyyy-mm-dd') ").append(diancCondition).append(" group by diancxxb_id) dr, \n");
			sbsql.append("(select diancxxb_id,kuc from shouhcrbb where riq=to_date('"+riq2+"','yyyy-mm-dd')").append(diancCondition).append(" ) dangr, \n");
			sbsql.append("(select diancxxb_id,dangrgm,haoyqkdr,kuc from shouhcrbb  \n");
			sbsql.append("where riq=to_date('"+riq1+"','yyyy-mm-dd')-1 ").append(diancCondition).append(") qr \n");
			sbsql.append("where dc.fuid=gs.id and dr.diancxxb_id=dc.id and dr.diancxxb_id=qr.diancxxb_id(+)  and dr.diancxxb_id=dangr.diancxxb_id(+)  \n");
			sbsql.append("group by rollup(gs.mingc,dc.mingc) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh),dc.mingc \n");
		}
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[2][7];
		 ArrHeader[0]=new String[] {"��λ","��ú��","������","���","���","����","��������(%)"};
		 ArrHeader[1]=new String[] {"��λ","��ú��","������",riq3,riq2,"����","��������(%)"};

		 int ArrWidth[]=new int[] {150,80,80,80,80,80,80};


		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setPageRows(36);
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
//		rt.body.setCellValue(2, 4, riq1);
//		rt.body.setCellValue(2, 5, riq2);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//�ϲ���
		rt.body.mergeFixedCols();//�Ͳ���
		rt.setTitle("���Ա������", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		String diancmc=((Visit) getPage().getVisit()).getDiancqc();
		
			if(diancmc.equals("���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���")){
				diancmc= "���ƹ���ȼ�Ϲ���";
			}
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +diancmc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3,  FormatDate(DateUtil.getDate(riq1))+"-"+FormatDate(DateUtil.getDate(riq2)),Table.ALIGN_CENTER);
		rt.setDefaultTitle(6, 2, "��λ:��" ,Table.ALIGN_RIGHT);
		
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 2, "�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(), Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
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
		fahdwList.add(new IDropDownBean(0,"�պĴ��ձ���"));
		fahdwList.add(new IDropDownBean(1,"�����ձ�"));
		fahdwList.add(new IDropDownBean(2,"�������ձ���"));
		fahdwList.add(new IDropDownBean(3,"��ú�����ձ���"));
		fahdwList.add(new IDropDownBean(4,"�պĴ��ܱ���"));
		fahdwList.add(new IDropDownBean(5,"�����ú��Ϣ�ձ�"));
		fahdwList.add(new IDropDownBean(6,"���˺�ú��Ϣ��"));
		fahdwList.add(new IDropDownBean(7,"���Աȱ�"));

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
		
		if(getBaoblxValue().getValue().equals("���Աȱ�")){
			DateField df1 = new DateField();
			df1.setValue(DateUtil.FormatDate(this.getEndriqDate()));
			df1.Binding("Endriq","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
			df1.setWidth(100);
			tb1.addField(df1);
			tb1.addText(new ToolbarText("-"));
		}

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
		if (getBaoblxValue()!=null && getBaoblxValue().getId()!=0){
			if(getBaoblxValue().getValue().equals("�����ձ�")){
				ToolbarButton tb3 = new ToolbarButton(null,"�ϱ�����","function(){document.getElementById('ShangbButton').click();}");
				tb1.addItem(tb3);
			}
			
		}
		
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

	
	//**************************�ϱ�����*********************************//
	private void getSettings(){
		JDBCcon cn = new JDBCcon();
		try {
			ResultSet rs = cn.getResultSet("select zhi from xitxxb where mingc='���ܱ���'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNbm=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp�û�'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNyh=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp����'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNmm=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp������ip'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNip=rs.getString("zhi");
				};
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.closeRs();
			cn.Close();
		}
	}
	
	private void getUploadFtp(String tableName){
		
        if(tableName == ""){
            setMsg("û�����ݿ��ϱ���");
        }else{
            try{
                getSettings();
                FtpUpload fu = new FtpUpload();
                String ip =ZhongNip;
                String username = ZhongNyh;
                String password = ZhongNmm;
                String filepath = "";
                fu.connectServer(ip, username, password, filepath);
                String filename = "C://Ribsc//" + tableName + ".txt";
                if(fu.upload(filename, tableName + ".txt" + "\n").equals("�ϴ��ɹ�")){
                	this.ShangbznUpdate();//�ϱ������պĴ�״̬����
                	setMsg("�ϱ��ɹ�!");
                }else{
                	setMsg("�ϴ�ʧ��!");
                }
                fu.closeConnect();
              
            }catch(Exception e){
            	setMsg("�ϴ�ʧ��!");
                e.printStackTrace();
            }
           
        }
    }

    private void ShangbTXTFile(){
    	//�ϴ��ӿ�Ԥ��,��ʱû��������
        String UploadFilename;
        if(!(new File("c://Ribsc")).isDirectory()){
            (new File("c://Ribsc")).mkdir();
        }
        JDBCcon con = new JDBCcon();
        FtpCreatTxt ct = new FtpCreatTxt();
        UploadFilename = "";
        StringBuffer fileline = new StringBuffer();
        String filedata = "";
        String date = "";
        String diancbm = "";
        double hetjh = 0;
        double dangrgm = 0;
        double leijgm = 0;
        double dangr = 0;
        double leij = 0;
        double dangrhy = 0;
        double leijhy = 0;
        double kuc = 0;
        String riq=DateUtil.FormatDate(_BeginriqValue);
        try{
	        String gsbm =ZhongNbm;
	        String FileName = "HC" + gsbm.substring(0, 1) + gsbm.substring(3) + riq.substring(8, 10);
	        ct.CreatTxt("c://Ribsc/" + FileName + ".txt");
                        
           StringBuffer sbsql = new StringBuffer();
           sbsql.append("select  to_char("+OraDate(_BeginriqValue)+",'yyyymmdd') as riq,dc.bianm as diancbm,0 as hetjh, \n");
           sbsql.append("      sum(nvl(dr.dangrgm,0)) as dangrgm,sum(nvl(lj.dangrgm,0)) as leijgm, \n");
           sbsql.append("      sum(nvl(dr.dangrgm,0)) as dangr,sum(nvl(lj.dangrgm,0)) as leij, \n");
           sbsql.append("      sum(nvl(dr.haoyqkdr,0)) as dangrhy,sum(nvl(lj.haoyqkdr,0)) as leijhy,sum(dr.kuc) as kuc \n");
           sbsql.append("from (select h.diancxxb_id,h.dangrgm as dangrgm,h.haoyqkdr as haoyqkdr,h.kuc  \n");
           sbsql.append("      from shouhcrbb h \n");
           sbsql.append("      where h.riq ="+OraDate(_BeginriqValue)+") dr, \n");
           sbsql.append("     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm,sum(h.haoyqkdr) as haoyqkdr \n");
           sbsql.append("       from shouhcrbb h \n");
           sbsql.append("       where h.riq >= First_day("+OraDate(_BeginriqValue)+") \n");
           sbsql.append("         and h.riq <= "+OraDate(_BeginriqValue)+" \n");
           sbsql.append("         group by h.diancxxb_id) lj,diancxxb dc,(select * from dianckjpxb kj where kouj='�պĴ��ձ�' and shujsbzt=1) kj \n");
           sbsql.append("  where dc.id=kj.diancxxb_id \n");
           sbsql.append("        and dc.id = lj.diancxxb_id(+) \n");
           sbsql.append("        and dc.id = dr.diancxxb_id(+) \n");
           sbsql.append("        group by (dc.bianm) \n");
           sbsql.append("        order by grouping(dc.bianm) desc \n");
           
            ResultSet rsdata = con.getResultSet(sbsql.toString());
            
            while(rsdata.next()){
                filedata = "";
                fileline.setLength(0);
                date = rsdata.getString("riq");
                diancbm = rsdata.getString("diancbm");
                hetjh = rsdata.getDouble("hetjh");
                dangrgm = rsdata.getDouble("dangrgm");
                leijgm = rsdata.getDouble("leijgm");
                dangr = rsdata.getDouble("dangr");
                leij = rsdata.getDouble("leij");
                dangrhy = rsdata.getDouble("dangrhy");
                leijhy = rsdata.getDouble("leijhy");
                kuc = rsdata.getDouble("kuc");
                fileline.append(getStr(8, date));
                fileline.append(getStr(6, diancbm));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(hetjh))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrgm))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leijgm))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangr))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leij))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrhy))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leijhy))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(kuc))));
                filedata = fileline.toString();
				ct.aLine(filedata);//д��������
            }
            rsdata.close();
            UploadFilename = FileName;
            ct.finish();
            
         
            
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            con.Close();
        }
        getUploadFtp(UploadFilename);
    }
	
    private String getStr(int weis,String str){
		StringBuffer Str_zf = new StringBuffer();
		if(str==null || str.equals("")){
			for (int i=0;i<weis;i++){
				Str_zf.append(" ");
			}
		}else{
			char[] Str=str.toCharArray();
			int Str_lenght=Str.length;
			
			for (int j=0;j<Str_lenght;j++){
				String Strs=""+Str[j];
				Str_zf.append(Strs);
			}
			int cha=0;
			if (Str_lenght!=weis){
				cha=weis-Str_lenght;
				for (int i=0;i<cha;i++){
					Str_zf.append(" ");
				}
			}
		}
		return Str_zf.toString();
	}
	
	private String getNum(int weis,int xiaos,String Number){//�õ�λ����������
		StringBuffer Str_zf = new StringBuffer();
		String str="";
		str=Number;
		if(str.equals("") ){
			for (int j=0;j<weis-xiaos-2;j++){
				String Strs="";
				Str_zf.append(Strs);
			}
			Str_zf.append(0.);
			for (int j=0;j<xiaos;j++){
				Str_zf.append(0);
			}
		}else{
			int zhengsw=0; 
			if(xiaos!=0){//��С��λ��
				String[] c=str.split("\\.");
				String strs1=c[0];//����λ
				char[] Str1=strs1.toCharArray();//����λ
				String Strs2=c[1];//С��λ
				char[] Str2=Strs2.toCharArray();//С��λ
				//¼������λ
				zhengsw=weis-xiaos-1;
				if (Str1.length!=zhengsw){
					int cha=zhengsw-Str1.length;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str1.length;j++){
					String Strs=""+Str1[j];
					Str_zf.append(Strs);
				}
				//¼��С��λ
				Str_zf.append(".");
				if(Str2.length!=xiaos){
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
					for (int j=0;j<xiaos-Str2.length;j++){
						Str_zf.append(0);
					}
				}else{
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
				}
			}else{//����С��λ
				char[] Str=str.toCharArray();
				int Str_lenght=Str.length;
				int cha=0;
				if (Str_lenght!=weis){//���ո�
					cha=weis-Str_lenght;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str_lenght;j++){//¼������
					String Strs=""+Str[j];
					Str_zf.append(Strs);
				}
			}
		}
		return Str_zf.toString();
	}
	
	
	public void ShangbznUpdate(){//�ϱ������Ժ�ı�shouhcrbb ��״̬Ϊ1,������糧�޸�
		  JDBCcon con = new JDBCcon();
		  String riq=DateUtil.FormatDate(_BeginriqValue);
		  String sql="update shouhcrbb s set s.zhuangt=1\n" +
			  "where s.riq=to_date('"+riq+"','yyyy-mm-dd')\n" + 
			  "and s.diancxxb_id in (select  kj.diancxxb_id from dianckjpxb kj where kj.kouj='�պĴ��ձ�' and kj.shujsbzt=1)";
		
		  int isUpdateTrue = con.getUpdate(sql);
		  if(isUpdateTrue==0){
			  System.out.println("�ϴ������ձ������պĴ��ձ����״̬û�гɹ�!");
		  }
		  con.Close();
	}
}