package com.zhiren.dc.huaygl.huaybb.baob;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ߣ�zl
 * ʱ�䣺2011-11-09
 * ���������������ֳ��ֿ�������ѯ
 */

public class Zhilbb_hd extends BasePage {

	private static final String REPORTNAME_ZHILBB="Zhilbb_hd"; 
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	Ʒ��������
	public IDropDownBean getPinzValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getPinzModel().getOptionCount()>0) {
				setPinzValue((IDropDownBean)getPinzModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setPinzValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getPinzModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setPinzModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setPinzModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	public void setPinzModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"ȫ��"));
		setPinzModel(new IDropDownModel(list,SysConstant.SQL_Pinz_mei));
	}
//	���䷽ʽ������
	public IDropDownBean getYunsfsValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getYunsfsModel().getOptionCount()>0) {
				setYunsfsValue((IDropDownBean)getYunsfsModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setYunsfsValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getYunsfsModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setYunsfsModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setYunsfsModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	public void setYunsfsModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"ȫ��"));
		setYunsfsModel(new IDropDownModel(list,SysConstant.SQL_yunsfs));
	}
//  ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����   
    private String getDcMingc(String id){ 
    	if(id == null || "".equals(id)){
    		return "";
    	}
		JDBCcon con=new JDBCcon();
		String mingc="";
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
//  ���ѡ������ڵ�Ķ�Ӧ�Ĺ�Ӧ������   
    private String[] getGys(String id){ 
    	String[] gys={"ȫ��","-1"};
    	if(id==null || "".equals(id)){
    		return gys;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			gys[0]=rsl.getString("mingc");
			gys[1]=rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean isParentDc(String id){ 
    	boolean isParent= false;
    	if(id == null || "".equals(id)){
    		return isParent;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc from diancxxb where fuid = " + id;
		if(con.getHasIt(sql)){
			isParent = true;
		}
		con.Close();
		return isParent;
	}
//	ȡ�����ڲ���SQL
    private String getDateParam(){
//		��������
		String rqsql = "";
		if(getBRiq() == null || "".equals(getBRiq())){
			rqsql = "and fahb.daohrq >= "+DateUtil.FormatOracleDate(new Date())+"\n";
		}else{
			rqsql = "and fahb.daohrq >= "+DateUtil.FormatOracleDate(getBRiq())+"\n";
		}
		if(getERiq() == null || "".equals(getERiq())){
			rqsql += "and fahb.daohrq < "+DateUtil.FormatOracleDate(new Date())+"+1\n";
		}else{
			rqsql += "and fahb.daohrq < "+DateUtil.FormatOracleDate(getERiq())+"+1\n";
		}
		return rqsql;
    }
//  ȡ�ù�Ӧ�̲���SQL
    private String getGysParam(){
//		��Ӧ��ú������
		String gyssql = "";
		if("1".equals(getGys(getTreeid())[1])){
			gyssql = "and fahb.gongysb_id = " + getTreeid() + "\n";
		}else if("0".equals(getGys(getTreeid())[1])){
			gyssql = "and fahb.meikxxb_id = " + getTreeid() + "\n";
		}
		return gyssql;
    }
//  ȡ��Ʒ�ֲ���SQL
    private String getPinzParam(){
//		Ʒ��sql
		String pzsql = "";
		if(getPinzValue() != null && getPinzValue().getId() != -1){
			pzsql = "and fahb.pinzb_id = " + getPinzValue().getId() + "\n";
		}
		return pzsql;
    }
    
//  ȡ�����䷽ʽ����SQL
    private String getYunsfsParam(){
		String yunsfssql = "";
		if(getYunsfsValue() != null && getYunsfsValue().getId() != -1){
			yunsfssql = "and fahb.yunsfsb_id = " + getYunsfsValue().getId() + "\n";
		}
		return yunsfssql;
    }

//	�����Ʊ���Ĭ�ϵ�ǰ�û�
	private String getZhibr(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String zhibr="";
		String zhi="��";
		String sql="select zhi from xitxxb where mingc='�±������Ʊ����Ƿ�Ĭ�ϵ�ǰ�û�' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSet rs=con.getResultSet(sql);
		try{
		  while(rs.next()){
			  zhi=rs.getString("zhi");
		  }
		}catch(Exception e){
			System.out.println(e);
		}
		if(zhi.equals("��")){
			zhibr=visit.getRenymc();
		}	
		return zhibr;
	}
//  ȡ�õ糧����SQL
    private String getDcParam(){
//		�糧sql
		String dcsql = "";
    	if(isParentDc(getTreeid_dc())){
    		dcsql = "and diancxxb.fuid = " + getTreeid_dc() + "\n";
    	}else{
    		dcsql = "and fahb.diancxxb_id = " + getTreeid_dc() + "\n";
    	}
		return dcsql;
    }

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
	
		return getZhilreport();
	}

	private String getZhilreport() {
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		Visit visit = (Visit) getPage().getVisit();
		String sql=
			"select\n" +
			"       zhilb.huaybh,\n" + 
			"       gongysb.mingc as meikdq,\n" + 
			"       meikxxb.mingc as miekdw,\n" + 
			"       chezxxb.mingc as faz,\n" + 
			"       pinzb.mingc as pinz,\n" + 
			"       fahb.ches,\n" + 
			"       round_new(fahb.laimsl,1) as laimsl,\n" + 
			"       round_new(round_new(zhilb.qnet_ar,3) / 4.1816 * 1000, 0) as farl1,\n" + 
			"       round_new(round_new(zhilb.qnet_ar,3) * 1000, 0) as farl2,\n" + 
			"       round_new(zhilb.mt, 1) as mt,\n" + 
			"       round_new(zhilb.mad, 2) as mad,\n" + 
			"       round_new(zhilb.aar, 2) as aar,\n" + 
			"       round_new(zhilb.aad, 2) as aad,\n" + 
			"       round_new(round_new(zhilb.qbad,3) * 1000, 0) as qbad,\n" + 
			"       round_new(zhilb.ad, 2) as ad,\n" + 
			"       round_new(zhilb.vdaf, 2) as vdaf,\n" + 
			"       round_new(zhilb.vad, 2) as vad,\n" + 
			"       round_new(zhilb.sdaf, 2) as sdaf,\n" + 
			"       round_new(zhilb.std, 2) as std,\n" + 
			"       round_new(zhilb.hdaf, 2) as hdaf,\n" + 
			"       to_char(zhilb.huaysj,'yyyy-mm-dd') as huaysj,\n" + 
			"       zhilb.huayy\n" + 
			"  from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb,diancxxb \n" + 
			" where fahb.zhilb_id = zhilb.id\n" + 
			"   and fahb.gongysb_id = gongysb.id\n" + 
			"   and fahb.meikxxb_id = meikxxb.id\n" + 
			"   and fahb.diancxxb_id = diancxxb.id\n" +
			"   and fahb.faz_id = chezxxb.id\n" + 
			"   and fahb.pinzb_id = pinzb.id\n" + getDateParam() + getGysParam() + getPinzParam() + getDcParam() + getYunsfsParam() +
//			" and ( fahb.diancxxb_id = 308 or fahb.diancxxb_id = 306 or fahb.diancxxb_id = 307 or fahb.diancxxb_id = 309 )\n" + 
//			"  and daohrq >= to_date('2011-09-29','yyyy-mm-dd') and daohrq < to_date('2011-11-09','yyyy-mm-dd') + 1 and  gongysb_id = 2018204 and meikxxb_id = 20111699\n" + 
			" union\n" + 
			"select\n" + 
			"       '�ϼ�' huaybh,\n" + 
			"       '' as meikdq,\n" + 
			"       '' as miekdw,\n" + 
			"       '' as faz,\n" + 
			"       '' as pinz,\n" + 
			"      sum(fahb.ches) as ches,\n" + 
			"       sum(round_new(fahb.laimsl,1)) as laimsl,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new((sum(round_new(zhilb.qnet_ar,3) * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1))) /4.1816 * 1000,0)) as farl1,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(round_new(zhilb.qnet_ar,3) * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)) * 1000,0)) as farl2,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(round_new(zhilb.mt,1) * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)),1)) as mt,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.mad * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as mad,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.aar * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as aar,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.aad * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as aad,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(round_new(zhilb.qbad,3) * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)) * 1000,0)) as qbad,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.ad * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)),2)) as ad,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.vdaf * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as vdaf,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.vad * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as vad,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.sdaf * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as sdaf,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.std * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as std,\n" + 
			"decode(sum(round_new(fahb.laimsl,1)),0,0,round_new(sum(zhilb.hdaf * round_new(fahb.laimsl,1)) / sum(round_new(fahb.laimsl,1)), 2)) as hdaf,\n" + 
			"        '' as huaysj,\n" + 
			"       '' as huayy\n" + 
			"  from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb,diancxxb \n" + 
			" where fahb.zhilb_id = zhilb.id\n" + 
			"   and fahb.gongysb_id = gongysb.id\n" + 
			"   and fahb.meikxxb_id = meikxxb.id\n" +
			"   and fahb.diancxxb_id = diancxxb.id\n" + 
			"   and fahb.faz_id = chezxxb.id\n" + 
			"   and fahb.pinzb_id = pinzb.id\n" + getDateParam() + getGysParam() + getPinzParam() + getDcParam() + getYunsfsParam() +
//			" and ( fahb.diancxxb_id = 308 or fahb.diancxxb_id = 306 or fahb.diancxxb_id = 307 or fahb.diancxxb_id = 309 )\n" + 
//			"  and daohrq >= to_date('2011-09-29','yyyy-mm-dd') and daohrq < to_date('2011-11-09','yyyy-mm-dd') + 1 and  gongysb_id = 2018204 and meikxxb_id = 20111699\n" + 
			" order by huaysj";
//
//			"select\n" +
//			"      decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc )as diancmc,\n" + 
//			"      decode(grouping(dc.mingc)+grouping(gs.mingc),1,'�ϼ�',2,'',gs.mingc) as gonghdw,\n" + 
//			"      decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(mk.mingc),1,'�ϼ�',2,'',3,'',mk.mingc) as meikdw,\n" + 
//			"      decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(mk.mingc)+grouping(to_char(f.daohrq,'yyyy-mm-dd')),1,'�ϼ�',3,'',4,'',to_char(f.daohrq,'yyyy-mm-dd')) as daohrq,\n" + 
//			"      pz.mingc as pinz,cz.mingc as faz,f.chec as chec,\n" + 
//			"      sum(f.ches) as ches,\n" + 
//			"      sum(f.jingz ) as jingz,\n" + 
//			"      sum(f.biaoz) as biaoz,\n" + 
//			"    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(mk.mingc)+grouping(to_char(f.daohrq,'yyyy-mm-dd'))+grouping(zl.id),2,'С��',3,'�ϼ�',4,'',5,'',GetHuaybmFromZhilId(zl.id))as bianm,\n" + 
//			"     to_char(zl.huaysj,'yyyy-mm-dd') as huaysj,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.qnet_ar*f.jingz)/sum(f.jingz),2)) as qnet_ar,\n" + 
//			"	  round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*qnet_ar/0.0041816)/sum(f.biaoz)),0) qnet_ar_k,\n" +
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.mt*f.jingz)/sum(f.jingz),2)) as mt,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl. mad*f.jingz)/sum(f.jingz),2))  as mad,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.aad*f.jingz)/sum(f.jingz),2))as aad,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.vad*f.jingz)/sum(f.jingz),2)) as vad,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.vdaf*f.jingz)/sum(f.jingz),2))as vdaf,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.stad*f.jingz)/sum(f.jingz),2))as stad,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.std*f.jingz)/sum(f.jingz),2))as std,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.had*f.jingz)/sum(f.jingz),2)) as had,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.qbad*f.jingz)/sum(f.jingz),2)) as qbad,\n" + 
//			"     decode(sum(f.jingz),0,0,round_new(sum(zl.qgrad*f.jingz)/sum(f.jingz),2)) as qgrad,\n" + 
//			"     gethuayy(zl.id) as huayy,\n" + 
//			"decode(grouping(zl.id),1,null,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"', 'Chehxxz', 'id',max(zl.id), '������Ϣ')) as chehxx,\n" + 
//			"decode(grouping(zl.id),1,null,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"', 'Huayd', 'zhilb_id',max(f.zhilb_id), '���鵥')) as huayd,\n" +
//			"decode(grouping(zl.id),1,null,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"', 'Duibcx', 'bianm',max(getHuaybh4zl(f.zhilb_id)), '�ԱȲ�ѯ')) as duibcx\n"+
//			" from  fahb f,zhilb zl ,diancxxb dc ,meikxxb mk,gongysb gs,pinzb pz,chezxxb cz\n" + 
//			"    where  f.zhilb_id =zl.id\n" + 
//			"      and f.gongysb_id=gs.id and f.meikxxb_id=mk.id\n" + 
//			"      and f.pinzb_id=pz.id and f.faz_id=cz.id\n" + 
//			"      and f.diancxxb_id=dc.id\n" + 
//			getDateParam() + getGysParam() + getPinzParam() + getDcParam() + getYunsfsParam() +
//			"  group by rollup (dc.mingc ,gs.mingc,mk.mingc,to_char(f.daohrq ,'yyyy-mm-dd') ,pz.mingc ,cz.mingc ,f.chec, zl.id,to_char(zl.huaysj,'yyyy-mm-dd'))\n" + 
//			"  having not(grouping(to_char(f.daohrq,'yyyy-mm-dd')) ||grouping(to_char(zl.huaysj,'yyyy-mm-dd'))=1)\n" + 
//			"  order by grouping(dc.mingc)desc ,dc.mingc ,grouping(gs.mingc)desc,gs.mingc ,grouping(mk.mingc)desc,mk.mingc,grouping(to_char(f.daohrq ,'yyyy-mm-dd'))desc ,to_char(f.daohrq ,'yyyy-mm-dd'),grouping(pz.mingc)desc,pz.mingc,\n" + 
//			"  grouping(cz.mingc)desc,cz.mingc,grouping(f.chec),f.chec,grouping(zl.id)desc,zl.id,grouping(to_char(zl.huaysj,'yyyy-mm-dd'))desc,to_char(zl.huaysj,'yyyy-mm-dd')";
		
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
//		System.out.println(sql);
		String[][] ArrHeader = new String[1][28];
		String[] strFormat=null;
		int[] ArrWidth=null;
		int aw=0;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+(REPORTNAME_ZHILBB)+"' order by xuh");
		ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+(REPORTNAME_ZHILBB)+"'");
        	String Htitle="ú  ��  ��  ��  ̨  ��" ;
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
    		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	ArrHeader=new String[1][22];
        	ArrHeader[0]=new String[] {"������","ú�����","ú��λ","��վ","Ʒ��","����","��������(��)",
        			"�յ�����λ��ֵ(Kcal<br>/kg)","�յ�����λ��ֵ(j/g)","ȫ<br>ˮ<br>��<br>(%)<br>Mt","���������ˮ��(%)Mad",
        			"�յ����ҷ�(%)Aar","����������ҷ�(%)Aad","��Ͳ<br>��ֵ<br>(j/g)<br>Qb,ad","������ҷ�(%)Ad",
        			"�����޻һ��ӷ���(%)Vdaf","����������ӷ���(%)Vad","�����޻һ���(%)Sdaf","�����ȫ��(%)S,td",
        			"�����޻һ���(%)Hdaf","����<br>����","������Ա"};
        	
        	ArrWidth=new int[] {54,100,100,100,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,65,54};
        	rt.setTitle("�����ۺϲ�ѯ��ϸ", ArrWidth);
	
        }
		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 5, "��ѯ����:" + getBRiq() + "��" + getERiq(),Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
		rt.body.setColAlign(20, Table.ALIGN_RIGHT);
		rt.body.setColAlign(21, Table.ALIGN_RIGHT);
		rt.body.setColAlign(22, Table.ALIGN_RIGHT);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		
		
//		����ҳ��
		rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 4, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 4, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(visit.getDiancxxb_id(),sql,rt,"�����ۺϲ�ѯ��ϸ",""+REPORTNAME_ZHILBB);
		return  rt.getAllPagesHtml();// ph;

	}

	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ѯ����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBRiq());
		df.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getERiq());
		df1.Binding("ERiq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
//		�糧��
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
//		��Ӧ����
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
//		Ʒ��ѡ��
		tb1.addText(new ToolbarText("Ʒ��:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("PinzSelect");
		pinz.setWidth(80);
		pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
		tb1.addField(pinz);
		tb1.addText(new ToolbarText("-"));
		//���䷽ʽ
		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox yunsfs = new ComboBox();
		yunsfs.setTransform("YunsfsSelect");
		yunsfs.setWidth(80);
		yunsfs.setListeners("select:function(own,rec,index){Ext.getDom('YunsfsSelect').selectedIndex=index}");
		tb1.addField(yunsfs);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		tb.setIcon(SysConstant.Btn_Icon_Print);
		setToolbar(tb1);

	}

	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}
	
//	�糧����
	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
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
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
//	������ʹ�õķ���
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
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

	

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setPinzValue(null);
			setPinzModel(null);
			setYunsfsValue(null);
			setYunsfsModel(null);
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
		}
		blnIsBegin = true;
		getSelectData();

	}

}
