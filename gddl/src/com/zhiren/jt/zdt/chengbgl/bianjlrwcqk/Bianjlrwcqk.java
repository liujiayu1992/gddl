package com.zhiren.jt.zdt.chengbgl.bianjlrwcqk;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
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
public class Bianjlrwcqk  extends BasePage implements PageValidateListener{
	
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
	//	����ĳ���ú̿��¯ǰ��������
	private long Rulqqtfy;
	public void setRulqqtfy(long Rulqqtfy){
		Rulqqtfy=Rulqqtfy;
	}
	public long getRulqqtfy(){
		return SysConstant.Rulqqtfy;
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

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
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
			//this.getSelectData();
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
			setTiaojlxValue(null);
			getITiaojlxModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
		
		}
		
		getToolBars() ;
		this.Refurbish();
	}
	
	private String RT_HET="Yuedmjgmxreport";//�¶�ú�۸���ϸ
	private String mstrReportName="Yuedmjgmxreport";
	
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
		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
//			notHuiz=" having not grouping(f.mingc)=1 ";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
//				notHuiz=" having not  grouping(dc.mingc)=1";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
		
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		//��������
		String fenx="";
		String tiaoj="";
			if(getBaoblxValue().getValue().equals("����")){
				 fenx="����";
			}else if(getBaoblxValue().getValue().equals("�ۼ�")){
				 fenx="�ۼ�";
			}
			if(getTiaojlxValue().getValue().equals("ȫ��")){
				tiaoj="";
			}else if(getTiaojlxValue().getValue().equals("�߼�����Ϊ��")){
				tiaoj=" and gkjg.fadbmdjyg<0 ";
			}else if(getTiaojlxValue().getValue().equals("�����ܶ�Ϊ��")){
				tiaoj=" and gkjg.rucbmdjyg<0 ";
			}
			
		strSQL ="select  decode(grouping(gs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,'&nbsp;&nbsp;'||gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc\n" +
			"        ,sum(l.fadl) as fadl\n" + 
			"        ,sum(l.shoudl) as shoudl\n" + 
			"        ,decode(sum(l.gongdl),0,0,round(sum(l.gongdl*l.gongdlbzmh)/sum(l.gongdl),2)) as gongdlbzmh\n" + 
			"        ,decode(sum(l.laimsl),0,0,round(sum(l.laimsl*l.buhsbmdj)/sum(l.laimsl),2)) as rucbmdj\n" + 
			"        ,decode(sum(l.fadhbzml+l.gongrhbzml),0,0,round(sum((l.fadhbzml+l.gongrhbzml)*l.fadzhbmj)/sum(l.fadhbzml+l.gongrhbzml),2)) as fadzhbmdj\n" + 
			"        ,decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddwbdcb)/sum(l.shoudl),2)) as shouddwbdcb\n" + 
			"        ,decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddwrlcb)/sum(l.shoudl),2)) as shouddwrlcb\n" + 
			"        ,decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddwgdcb)/sum(l.shoudl),2)) as shouddwgdcb\n" + 
			"        ,decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddj)/sum(l.shoudl),2)) as shouddj\n" + 
			"        ,decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.bianjlr)/sum(l.shoudl),2)) as bianjlr\n" + 
			"        ,sum(lirze) as lirze\n" + 
			"        ,round(decode(decode(sum(l.gongdl),0,0,round(sum(l.gongdl*l.gongdlbzmh)/sum(l.gongdl),2)),0,0,\n" + 
			"           ((decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddj)/sum(l.shoudl),2))\n" + 
			"           -(decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddwbdcb)/sum(l.shoudl),2))\n" + 
			"              -decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddwrlcb)/sum(l.shoudl),2))))\n" + 
			"              /decode(sum(l.gongdl),0,0,round(sum(l.gongdl*l.gongdlbzmh)/sum(l.gongdl),2))*1000)),2)  as fadbmdjyg\n" + 
			"       ,(round(decode(decode(sum(l.gongdl),0,0,round(sum(l.gongdl*l.gongdlbzmh)/sum(l.gongdl),2)),0,0,\n" + 
			"       ((decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddj)/sum(l.shoudl),2))\n" + 
			"       -(decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddwbdcb)/sum(l.shoudl),2))\n" + 
			"          -decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.shouddwrlcb)/sum(l.shoudl),2))))\n" + 
			"          /decode(sum(l.gongdl),0,0,round(sum(l.gongdl*l.gongdlbzmh)/sum(l.gongdl),2))*1000)),2)-"+getRulqqtfy()+")  as rucbmdjyg\n" + 
			"       ,decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.caiwfy)/sum(l.shoudl),2)) as caiwfy\n" + 
			"       ,decode(sum(l.shoudl),0,0,round(sum(l.shoudl*l.guanlfy),2)) as guanlfy\n" + 
			"       ,decode(sum(l.fadhbzml),0,0,round(sum(l.fadhbzml*l.jihgdmh)/sum(l.fadhbzml),2)) as jihgdmh\n" + 
			"from\n" + 
			"( select gkjg.diancxxb_id as id\n" + 
			"       ,gkjg.shoudl\n" + 
			"       ,yzb.fadl\n" + 
//			"       ,yzb.gongdl\n" + 
//			"       ,yzb.gongdlbzmh\n" + 
//			"       ,yzb.fadhbzml\n" + 
//			"       ,yzb.fadhyzbzml\n" + 
//			"       ,yzb.gongrhbzml\n" + 
//			-------------------------------�޸ĵ�����ʽ��yuezbb�е��ֶ�------------------------------------	
			"		,yzb.gongdl+yzb.fadzhcgdl+yzb.gongrcgdl as gongdl\n" +
			"       ,yzb.gongdbzmh as gongdlbzmh\n" + 
			"       ,yzb.fadmzbml as fadhbzml\n" + 
			"       ,yzb.fadyzbzml as fadhyzbzml\n" + 
			"       ,yzb.gongrmzbml as gongrhbzml" +
//		    ---------------------------------------------------------------------------------------------
			"       ,rcmb.laimsl\n" + 
			"       ,rcmb.buhsbmdj\n" + 
			"       ,gkjg.fadzhbmj\n" + 
			"       ,gkjg.shouddwbdcb\n" + 
			"       ,gkjg.shouddwrlcb\n" + 
			"       ,gkjg.shouddwgdcb\n" + 
			"       ,gkjg.shouddj\n" + 
			"       ,(gkjg.shouddj-gkjg.shouddwbdcb) as bianjlr\n" + 
			"       ,gkjg.lirze\n" + 
			"       ,gkjg.fadbmdjyg\n" + 
			"       ,gkjg.rucbmdjyg\n" + 
			"       ,gkjg.caiwfy\n" + 
			"       ,gkjg.guanlfy\n" + 
			"       ,gkjg.jihgdmh\n" + 
			"from bianjlrb gkjg,diancxxb dc,yuezbb yzb\n" + 
			"     ,(select tj.diancxxb_id as diancxxbid,sl.laimsl,rcm.buhsbmdj from yuercbmdj rcm,yuetjkjb tj,yueslb sl,diancxxb dc\n" + 
			"      where sl.yuetjkjb_id=tj.id and rcm.yuetjkjb_id=tj.id and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
			"            and rcm.fenx='"+fenx+"' and sl.fenx='"+fenx+"' and tj.diancxxb_id=dc.id "+strGongsID+"\n" + 
			"       )rcmb\n" + 
			"where gkjg.diancxxb_id=dc.id  and gkjg.diancxxb_id=rcmb.diancxxbid and yzb.diancxxb_id=rcmb.diancxxbid\n" + 
			"      and gkjg.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and yzb.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
			"        "+strGongsID+" and yzb.fenx='"+fenx+"' and gkjg.fenx='"+fenx+"' "+tiaoj+"\n" + 
			"      )l,diancxxb dc,diancxxb gs\n" + 
			"      where l.id=dc.id and gs.id=dc.fuid\n" + 
			"      group by rollup(gs.mingc,dc.mingc)\n" + 
			"      order by grouping(gs.mingc)desc,grouping(dc.mingc) desc";

			 ArrHeader =new String[1][17];
			 ArrHeader[0]=new String[] {"��λ����","����������ǧ��ʱ��","�۵�������ǧ��ʱ��","ʵ����ɹ���ú�ģ���/ǧ��ʱ��","�볧��ú���ۣ�����˰��Ԫ/�֣�","�����ۺϱ�ú���ۣ�����˰��Ԫ/�֣�","�۵絥λ�䶯�ɱ���Ԫ/ǧǧ��ʱ��","���У��۵絥λȼ�ϳɱ���Ԫ/ǧǧ��ʱ��","�۵絥λ�̶��ɱ���Ԫ/ǧǧ��ʱ��","�۵絥�ۣ�����˰��Ԫ/ǧǧ��ʱ��","�߼�����Ԫ/ǧǧ��ʱ��","�����ܶ��Ԫ��","���ڱ߼�����Ϊ0ʱ�ķ����ۺϱ�ú����Ԥ����Ԫ/�֣�","���ڱ߼�����Ϊ0ʱ���볧��ú����Ԥ����Ԫ/�֣�","�������","�������","�깩��ú�ļƻ�ֵ"};

			 ArrWidth =new int[] {150,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
//			 arrFormat= new String []{"","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00"};
				
			 iFixedRows=1;
			 iCol=10;
			// System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			Table tb = new Table(rs, 1, 0, 1);
			rt.setBody(tb);
//			���Ź�˾�����ҵ2008��6��ú�ۡ��۵絥λȼ�ϳɱ����߼����������ϸ��										
			
			rt.setTitle( getBiaotmc()+intyear+"��"+intMonth+"��ú�ۡ��۵絥λȼ�ϳɱ����߼����������ϸ��", ArrWidth);
			rt.setDefaultTitle(1, 3, "���λ:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(7, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			if(rt.body.getRows()>1){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
//			rt.body.setColFormat(arrFormat);
			//ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(7,2,"���:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,3,"�Ʊ�:",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(rt.footer.getCols()-2,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
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
		fahdwList.add(new IDropDownBean(0,"����"));
		fahdwList.add(new IDropDownBean(1,"�ۼ�"));
		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
//	��������
	public boolean _Tiaojlxchange = false;
	private IDropDownBean _TiaojlxValue;

	public IDropDownBean getTiaojlxValue() {
		if(_TiaojlxValue==null){
			_TiaojlxValue=(IDropDownBean)getITiaojlxModels().getOption(0);
		}
		return _TiaojlxValue;
	}
	public void setTiaojlxValue(IDropDownBean Value) {
		long id = -2;
		if (_TiaojlxValue != null) {
			id = _TiaojlxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Tiaojlxchange = true;
			} else {
				_Tiaojlxchange = false;
			}
		}
		_TiaojlxValue = Value;
	}

	private IPropertySelectionModel _ITiaojlxModel;

	public void setITiaojlxModel(IPropertySelectionModel value) {
		_ITiaojlxModel = value;
	}

	public IPropertySelectionModel getITiaojlxModel() {
		if (_ITiaojlxModel == null) {
			getITiaojlxModels();
		}
		return _ITiaojlxModel;
	}
	public IPropertySelectionModel getITiaojlxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List tiaojList = new ArrayList();
		tiaojList.add(new IDropDownBean(0,"ȫ��"));
		tiaojList.add(new IDropDownBean(1,"�߼�����Ϊ��"));
		tiaojList.add(new IDropDownBean(2,"�����ܶ�Ϊ��"));
		_ITiaojlxModel = new IDropDownModel(tiaojList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _ITiaojlxModel;
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
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
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
		
		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ѯ����:"));
		ComboBox tj = new ComboBox();
		tj.setTransform("TiaojlxDropDown");
		tj.setWidth(120);
		tj.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(tj);
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