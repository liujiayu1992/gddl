
package com.zhiren.jt.zdt.zonghcx.yuedpdqkhzb;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

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


import org.apache.tapestry.contrib.palette.SortMode;


public class Yuedpdqkhzbreport  extends BasePage implements PageValidateListener{
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean returnMsg=false;

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


/*	//��ʼ����v
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
	}*/
	

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _DanwmcszChick = false;

	public void DanwmcszButton(IRequestCycle cycle) {
		_DanwmcszChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		returnMsg=false;
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_DanwmcszChick) {
			_DanwmcszChick = false;
			Danwmcsz(cycle);
		}
	}


	private void Refurbish() {
		//Ϊ"ˢ��" ��ť��Ӵ������
		isBegin=true;
//		getSelectData();
		getPandcmqkb();
	}
//	 �糧�������ƫ��ֵ����
	private void Danwmcsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString7(getChange());
		cycle.activate("Bizpgzsz");
	}

//	******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());

//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			isBegin=true;
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setTreeid(null);
			visit.setString1(null);
			visit.setString3(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
		}
		/*if (_BeginriqChange){
			_BeginriqChange=false;
			Refurbish();
		}*/
		
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
		setUserName(visit.getRenymc());
		getToolBars();
		Refurbish();
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (getBaoblxValue().getValue().equals("�̵��ú�����")){
			return getPandcmqkb();
		}else if(getBaoblxValue().getValue().equals("�̵���������")){
			return getPandcyqkb();
		}else if(getBaoblxValue().getValue().equals("�ܶ�ƫ���")){
			return getMidpcb();
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

	//�̵��ú�����
	private String getPandcmqkb(){
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

		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�̵��ú�����";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		String having ="";
		int jib=this.getDiancTreeJib();
	
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			danwmc="�ܼ�";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			having=" having  grouping(dc.fgsmc)=0\n";
			danwmc=getTreeDiancmc(this.getTreeid())+"�ϼ�";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			
			having=" having  grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";

			

			strSQL=" select   decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,\n" +
				"     sm.meiz,sum(sm.tij) as tij,\n" + 
				"     round(decode(sum(sm.tij),0,0,sum(cunml_mc)/sum(sm.tij)),5) as shicbz,\n" + 
				"     sum(sm.cunml) as qitcm,sum(sm.pandhlhbhtz) as pandhlhbhtz,sum(sm.shijkc) as shijpcml,\n" + 
				"     sum(sm.zhangmcm_ycl) as zhangmcm_ycl,sum(sm.cunsl) as cunsl,sum(sm.zhangmkc) as zhangmcm_wcl,\n" + 
				"     sum(sm.shuifctz) as shuifctz,sum(sm.cunm_c) as cunm_c,sum(sm.cunsl_kj) as cunsl_kj,\n" + 
				"     sum(sm.panyl) as panyl,sum(sm.pankl) as pankl,\n" + 
				"     sm.Pandff,sm.CELFF,sm.xiuzmd,to_char(sm.riq,'yyyy-mm-dd'),sm.beiz\n" + 
				" from\n" + 
				" (\n" + 
				"    select distinct dc.id,dc.mingc,'' as meiz,nvl(tj.tij,0) as tij,nvl(tj.cunml_mc,0) as cunml_mc,nvl(qtcml.cunml,0) as cunml,\n" + 
				"        getpandlhtz(pd.diancxxb_id,pd.riq) as pandhlhbhtz,nvl(pm.shijkc,0) as shijkc,\n" + 
				"        nvl(pm.zhangmkc-pm.cuns,0) as zhangmcm_ycl,nvl(pm.cuns,0) as cunsl,\n" + 
				"        nvl(pm.zhangmkc,0) as zhangmkc,nvl(pm.shuifc,0) as shuifctz,\n" + 
				"        (nvl(pm.shijkc,0)-nvl(pm.zhangmkc,0)) as cunm_c,\n" + 
				"        getPandkjwcs(pd.diancxxb_id,pd.riq) as cunsl_kj,\n" + 
				"        CASE WHEN nvl(pm.shijkc,0)-nvl(pm.zhangmkc,0) <= 0 THEN 0 ELSE nvl(pm.panyk,0) END as panyl,\n" + 
				"        CASE WHEN nvl(pm.shijkc,0)-nvl(pm.zhangmkc,0) >= 0 THEN 0 ELSE nvl(pm.panyk,0) END as pankl,\n" + 
				"        tj.Pandff ,mdb.CELFF,'' as xiuzmd,pd.riq ,pd.beiz\n" + 
				"     from pandzmm pm,pandmdb mdb,pandb pd,vwdianc dc\n" + 
				"     ,(select pandtjb.pandb_id as id,sum(pandtjb.tij) as tij,sum(pandtjb.cunml) as cunml_mc,\n" + 
				"              max(pandtjb.Pandff) as Pandff from pandtjb group by (pandtjb.pandb_id))tj\n" + 
				"     ,(select wzcm.pandb_id as id,sum(wzcm.cunml)as cunml from pandcmwz cmwz,pandwzcmb wzcm\n" + 
				"              where cmwz.id=wzcm.pandcmwz_id group by (wzcm.pandb_id))qtcml\n" + 
				"    where pm.pandb_id =pd.id and tj.id(+)=pd.id and pd.diancxxb_id=dc.id and mdb.pandb_id(+)=pd.id\n" + 
				"          and qtcml.id(+)=pd.id  and pd.riq=getpanddate(pd.diancxxb_id,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) "+strGongsID+"\n" + 
				") sm, vwdianc dc\n" + 
				"where dc.id=sm.id(+) "+strGongsID+"\n" + 
				"group by rollup(dc.fgsmc,(dc.mingc,sm.meiz,sm.Pandff,sm.CELFF,sm.xiuzmd,sm.riq,sm.beiz))\n" + 
				having+"\n"+
				"order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
				"grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc";
		
		 ArrHeader =new String[1][20];
		 ArrHeader[0]=new String[] {"��λ����","ú��","�̵�����������ף�","ʵ����أ���/�����ף�","ú����ú�ޡ�ú���������س���ú��","�̵�����ı仯����","ʵ���̴�ú�����֣�","�̵�ʱ����������������ú","������","�̵�ʱδ��������������ú","�̵�ʱ�����úˮ�ֲ������","ʵ���̴�ú����δ��������������ú����ֵ","���пɼ�Ϊ������","��ӯ��","�̿���","����̵㷽��","�ܶȲⶨ����","�Ƿ���ˮ�ֲ�ֵ�����ܶ�","�̵�ʱ��","�̵�˵��"};

		 ArrWidth =new int[] {120,40,70,60,60,55,70,55,60,70,70,50,50,50,50,40,40,40,80,80};

		
		
		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, 1));

		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 2, "�Ʊ�ʱ��"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
		rt.setDefaultTitle(17, 3, "��λ:�֡���/������", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		if(jib==1){
			if(rt.body.getRows()>1){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
		}
		
		//ҳ�� 
 
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,2,"���:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(17,2,"�Ʊ�:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 

		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}

	private String getMidpcb(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		/*String riq=OraDate(_BeginriqValue);//��ǰ����
		String riq1=FormatDate(_BeginriqValue);*/
		
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
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�ܶ�ƫ���";
//		int iFixedRows=0;//�̶��к�
//		int iCol=0;//����
		String danwmc="";//��������
		String strGongsID = "";
		String having ="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";

		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			having=" having  grouping(dc.fgsmc)=0\n";

		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			having=" having  grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());

		//��������
		titlename=titlename+"";
		
			strSQL=
			" select   decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,\n" +
			"     sm.meiz,sum(sm.tij) as tij,\n" + 
			"     round(decode(sum(sm.tij),0,0,sum(sm.cunml)/sum(sm.tij)),5) as shicbz,\n" + 
			"     js.zhi as jiasbzpgz,\n" + 
			"     (sum(sm.tij)*js.zhi) as xuzkcl\n" + 
			" from\n" + 
			" (\n" + 
			" select dc.id as id,'' as meiz,tj.tij as tij,tj.cunml as cunml\n" + 
			"               from pandtjb tj,pandb pd,vwdianc dc\n" + 
			"               where tj.pandb_id=pd.id and pd.riq=getpanddate(pd.diancxxb_id,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))\n" + 
			"               and pd.diancxxb_id=dc.id "+strGongsID+"\n" + 
			") sm,\n" + 
			"(select j.diancxxb_id as id,nvl(zhi,0) as zhi from jiasbzpgzb j,vwdianc dc where j.diancxxb_id=dc.id "+strGongsID+")js,\n" + 
			"vwdianc dc\n" + 
			"where dc.id=sm.id(+)  and dc.id=js.id(+) "+strGongsID+"\n" + 
			"group by rollup(dc.fgsmc,(dc.mingc,sm.meiz,js.zhi))\n" + 
			having+"\n"+
			"order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
			"grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc";


		 ArrHeader =new String[1][6];
		 ArrHeader[0]=new String[] {"��λ����","ú��","�̵�����������ף�","ʵ����أ���/�����ף�","�������ƫ��ֵ����/�����ף�","�������ú�����֣�"};

		 ArrWidth =new int[] {150,50,70,70,70,70};

		
//		iFixedRows=1;
//		iCol=14;

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, 2));
		

		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, intyear+"��"+intMonth+"��", Table.ALIGN_CENTER);
//		rt.setDefaultTitle(4, 2, "��λ:������,��", Table.ALIGN_RIGHT);

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

	//�̵���������
	private String getPandcyqkb(){
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

		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="�̵���������";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		
		String strGongsID = "";
		String having ="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid= " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			having=" having  grouping(dc.fgsmc)=0\n";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			having=" having  grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
		titlename=titlename+"";
		
			
			strSQL=
				" select   decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�����ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,\n" +
				"     sm.pinz,sum(sm.shicbz) as shicbz,sum(sm.shijcy) as shijcy,sum(sm.zhangmkc_ycl) as zhangmkc_ycl,sum(sm.cuncl) as cuncl,\n" + 
				"     sum(sm.zhangmkc_wcl) as zhangmkc_wcl,sum(sm.shijcy-sm.zhangmkc_wcl) as zhangmcyl_c,sum(sm.panyl) as panyl,sum(sm.pankl) as pankl,max(sm.shuom) as shuom\n" + 
				" from\n" + 
				" (   select dc.id,pd.riq,'' as pinz,0 as shicbz,nvl(pdy.shijkc,0) as shijcy,0 as zhangmkc_ycl,0 as cuncl,nvl(pdy.zhangmkc,0) as zhangmkc_wcl\n" + 
				"           ,CASE WHEN nvl(pdy.shijkc,0)-nvl(pdy.zhangmkc,0) <= 0 THEN 0 ELSE nvl(pdy.panyk,0) END as panyl\n" + 
				"           ,CASE WHEN nvl(pdy.shijkc,0)-nvl(pdy.zhangmkc,0) >= 0 THEN 0 ELSE nvl(pdy.panyk,0) END as pankl\n" + 
				"           ,pd.beiz as shuom\n" + 
				"     from pandzmy pdy,pandb pd,vwdianc dc,\n" + 
				"     (select distinct pd.diancxxb_id as id,getpanddate(pd.diancxxb_id,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) as riq\n" + 
				"             from pandb pd,vwdianc dc where pd.diancxxb_id =dc.id "+strGongsID+" )pdrq\n" + 
				"     where pdy.pandb_id=pd.id and pd.diancxxb_id=dc.id  and pd.diancxxb_id=pdrq.id\n" + 
				"     and pd.riq=pdrq.riq "+strGongsID+"\n" + 
				") sm, vwdianc dc\n" + 
				"where dc.id=sm.id(+) "+strGongsID+"\n" + 
				"group by rollup(dc.fgsmc,(dc.mingc,sm.pinz))\n" + 
				having+"\n"+
				"order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
				"grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc";		
		
		 ArrHeader =new String[1][11];
		 ArrHeader[0]=new String[] {"��λ����","����","ʵ����أ���/�����ף�","ʵ���̴��������֣�","�̵�ʱ������������������","������","�̵�ʱδ��������������ú","ʵ���̴�������δ��������������������ֵ","��ӯ��","�̿���","˵��"};

		 ArrWidth =new int[] {150,60,60,70,70,60,70,60,70,70,100};

		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs,1, 0, 1));

		rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 2, "��λ:�֡���/������", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;

		if(jib==1){
			if(rt.body.getRows()>1){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
		}
		//ҳ�� 
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,2,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		  rt.setDefautlFooter(5,2,"���:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(9,2,"�Ʊ�:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 

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
		fahdwList.add(new IDropDownBean(0,"�̵��ú�����"));
		fahdwList.add(new IDropDownBean(1,"�̵���������"));
		fahdwList.add(new IDropDownBean(2,"�ܶ�ƫ���"));

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

		/*tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));*/
		
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

		tb1.addText(new ToolbarText("�����ѯ:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setId("Tongjkj");
		cb.setWidth(120);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
//		 �糧����ƫ��ֵ����

		
		ToolbarButton tb3 = new ToolbarButton(null,"�糧����ƫ��ֵ����","function(){document.getElementById('DanwmcszButton').click();}");
		tb1.addItem(tb3);
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
			int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
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
			int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
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

	
}