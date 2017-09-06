package com.zhiren.jt.jiesgl.changfhs;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ����:tzf
 * ʱ��:2010-03-11
 * �޸�����:��������  ��������    ���κ� ������ ����
 */
public class Yunfkhjsd  extends BasePage implements PageValidateListener{

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
	
	private String getDate(){
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
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

	public void submit(IRequestCycle cycle) {

	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1("");	//��¼��һҳ�洫�����ı��������
			
			if(cycle.getRequestContext().getParameters("lx")!=null){
				
				visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			}
		}
//		�ڶ�����תҳ����
		if(cycle.getRequestContext().getParameters("lx")!=null){
			
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
		}
		
		this.getSelectData();
		isBegin = true;
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;
	
	private String getSelectData(){
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn=new JDBCcon();
		String jiesbh = "";
		jiesbh = visit.getString1();
		String sql_jieslx="select jieslx,hetb_id from jiesb where bianm='"+jiesbh+"'";
		ResultSetList rsl_jieslx=cn.getResultSetList(sql_jieslx);
		rsl_jieslx.next();
		if(!rsl_jieslx.getString("jieslx").equals("1")){//��jiesb.jieslx!=1,������Ʊ����
			setMsg("�ý��㵥û���˷ѽ��㸽��");
			return "";
		}
		String sql_hetgl="select * from meikyfhtglb where hetb_id="+rsl_jieslx.getString("hetb_id");
		ResultSetList rsl_hetgl=cn.getResultSetList(sql_hetgl);
		if(!rsl_hetgl.next()){//��meikyfhtglb�������ݣ�˵���ý��㵥û���˷ѽ��㸽��
			setMsg("�ý��㵥û���˷ѽ��㸽��");
			return "";
		}
		
		_CurrentPage=1;
		_AllPages=1;
		
		
		String yunsdw="";//���䵥λ
        String zhidrq="";//�Ƶ�����
		String hetbh = "";//��ͬ���
		String no = "";//���㵥���
		String gonghdw="";//������λ
		String jieszq = "";//��������
		double kuangfsl =0;//������
		double shissl=0;//ʵ������
		double kuissl=0;//��������
		double yunxks =0;//�������
		double kaohks =0;//���˿���
		double hetyj =0;//��ͬ�˷ѵ���
		double hetmj =0;//��ͬú��
		double yunfsp = 0;//�˷�����
		double meiksp = 0;//ú������
		double suopzj = 0;//�����ܼ�
		double shijyfdj =0;//ʵ���˷ѵ���
		double yingfyf = 0;//Ӧ���˷�
		double shifyf =0;//ʵ���˷�
		String hetb_id="";//hetb_idú���ͬId
		String hetys_id="";//hetys_id �����ͬid
		String jiesb_id="";
		
		String table = "";
		String table_mk = "";
		String table_yf = "";
		String tiaoj = "";
		String sql_jsb = "";
		/*table = visit.getString1().substring(0,visit.getString1().indexOf(";"));
		table_mk = table.substring(0,table.indexOf(","));
		table_yf = table.substring(table.indexOf(",")+1);*/
		
		
		//���䵥λ
		String sql_yunsdw=
			"select quanc\n" +
			"  from yunsdwb\n" + 
			" where id in (select distinct yunsdwb_id\n" + 
			"                from chepb\n" + 
			"               where fahb_id in\n" + 
			"                     (select id\n" + 
			"                        from fahb\n" + 
			"                       where jiesb_id =\n" + 
			"                             (select id\n" + 
			"                                from jiesb\n" + 
			"                               where bianm = '"+jiesbh+"')))";

		ResultSetList rsl_yunsdw = cn.getResultSetList(sql_yunsdw);
		if(rsl_yunsdw.next()){
			yunsdw=rsl_yunsdw.getString("quanc");
		}
		
		
		//������Ϣ
		String sql_jies="select * from jiesb where bianm='"+jiesbh+"'";
		ResultSetList rsl_jies = cn.getResultSetList(sql_jies);
		if(rsl_jies.next()){
			jiesb_id=rsl_jies.getString("id");
			zhidrq=rsl_jies.getDateString("jiesrq");
			jieszq=rsl_jies.getDateString("fahksrq")+"<br>��<br>"+rsl_jies.getDateString("fahjzrq");
			//kuangfsl=rsl_jies.getDouble("jiessl")+rsl_jies.getDouble("koud");//Ʊ��
			shissl=rsl_jies.getDouble("guohl");
			hetb_id=rsl_jies.getString("hetb_id");
			gonghdw=rsl_jies.getString("gongysmc");
			
		}
        //������
		String sql_kuangfsl="select sum(biaoz) kuangfsl from fahb where jiesb_id="+jiesb_id;
		ResultSetList rsl_kuangfsl = cn.getResultSetList(sql_kuangfsl);
		if(rsl_kuangfsl.next()){
			kuangfsl=rsl_kuangfsl.getDouble("kuangfsl");
		}
		
		//��ͬú��
		String sql_hetjg="select jij from hetjgb where hetb_id="+hetb_id;
		ResultSetList rsl_hetjg = cn.getResultSetList(sql_hetjg);
		if(rsl_hetjg.next()){
			hetmj=rsl_hetjg.getDouble("jij");
		}
		
		String sql_het="select * from meikyfhtglb where hetb_id="+hetb_id;
		ResultSetList rsl_het = cn.getResultSetList(sql_het);
		if(rsl_het.next()){
			hetys_id=rsl_het.getString("hetys_id");
		}
		//�����ͬ���
		String sql_yunsht="select * from hetys where id="+hetys_id;
		ResultSetList rsl_yunsht = cn.getResultSetList(sql_yunsht);
		if(rsl_yunsht.next()){
			//hetys_id=rsl_yunsht.getString("hetys_id");
			hetbh=rsl_yunsht.getString("hetbh");
		}
		//�˷ѵ���
		String sql_hetysjg="select * from hetysjgb where hetys_id="+hetys_id;
		ResultSetList rsl_hetysjg = cn.getResultSetList(sql_hetysjg);
		if(rsl_hetysjg.next()){
			hetyj=rsl_hetysjg.getDouble("yunja");
		}
		//ʵ�������˷�
		String sql_shifyf=
			"select a.zhi\n" +
			"  from (select *\n" + 
			"          from feiyb\n" + 
			"         where feiyb_id =\n" + 
			"               (select feiyb_id\n" + 
			"                  from yunfdjb\n" + 
			"                 where id =\n" + 
			"                       (select distinct yunfdjb_id\n" + 
			"                          from danjcpb\n" + 
			"                         where yunfjsb_id =\n" + 
			"                               (select id\n" + 
			"                                  from jiesyfb\n" + 
			"                                 where bianm = '"+jiesbh+"')))) a,\n" + 
			"       (select feiyxm.id feiyxmb_id, feiymcb.mingc\n" + 
			"          from (select id, feiymcb_id from feiyxmb) feiyxm, feiymcb\n" + 
			"         where feiyxm.feiymcb_id = feiymcb.id) b\n" + 
			" where a.feiyxmb_id = b.feiyxmb_id\n" + 
			"   and b.mingc = '�����˷�'";
		ResultSetList rsl_shifyf = cn.getResultSetList(sql_shifyf);
		if(rsl_shifyf.next()){
			shifyf=rsl_shifyf.getDouble("zhi");
		}

		
		kuissl=CustomMaths.Round_New(kuangfsl-shissl,2);
		yunxks=CustomMaths.Round_New(kuangfsl*0.005,4);
		kaohks=CustomMaths.Round_New(kuissl-yunxks,4);
		yunfsp=CustomMaths.Round_New(hetyj*(kuissl-yunxks),2);
		meiksp=CustomMaths.Round_New(hetmj*kaohks,2);
		suopzj=CustomMaths.Round_New(yunfsp+meiksp,2);
		shijyfdj=CustomMaths.Round_New(hetyj-suopzj/kuangfsl,6);
		yingfyf=CustomMaths.Round_New(shijyfdj*kuangfsl,2);
		
		rsl_yunsdw.close();
		rsl_jies.close();
		rsl_kuangfsl.close();
		rsl_hetjg.close();
		rsl_het.close();
		rsl_yunsht.close();
		rsl_hetysjg.close();
		rsl_shifyf.close();
		
		String ArrHeader[][]=new String[5][15];
		ArrHeader[0]=new String[] {"������λ:"+gonghdw,"","","","","������ϸ","","","","","","","","",""};
		ArrHeader[1]=new String[] {"���","��������","������<br>(��)","ʵ������<br>(��)","��������<br>(��)","�������<br>(��)","���˿���<br>(��)","��ͬ��<br>�ѵ���<br>(��)","��ͬú<br>̿����<br>(��)",
				                   "�˷�����<br>(Ԫ)","ú������<br>(Ԫ)","�����ܼ�<br>(Ԫ)","ʵ���˷ѵ���<br>(Ԫ)","Ӧ���˷�<br>(Ԫ)","ʵ���˷�<br>(Ԫ)"};
		ArrHeader[2]=new String[] {"1",jieszq,kuangfsl+"",shissl+"",kuissl+"",yunxks+"",kaohks+"",hetyj+"",hetmj+"",yunfsp+"",meiksp+"",suopzj+"",shijyfdj+"",yingfyf+"",shifyf+""};
		ArrHeader[3]=new String[] {"�ϼ�","�ϼ�",kuangfsl+"",shissl+"",kuissl+"",yunxks+"",kaohks+"",hetyj+"",hetmj+"",yunfsp+"",meiksp+"",suopzj+"",shijyfdj+"",yingfyf+"",shifyf+""};
		ArrHeader[4]=new String[] {"��ע:","","","","","","","","","","","","","",""};
		
		int ArrWidth[]=new int[] {40,80,60,60,60,60,60,60,60,60,60,60,60,60,60};
//		 ����
		Report rt=new Report();
		rt.setTitle("���������Ȫ�������޹�˾ȼ���˷ѽ��㵥",ArrWidth);
		rt.setDefaultTitle(1,9,"���˵�λ:"+yunsdw+"<br>���㲿��:���������Ȫ�������޹�˾�ƻ���Ӫ��",Table.ALIGN_LEFT);
		rt.setDefaultTitle(10,5,"�Ƶ�����:"+zhidrq+"<br>��ͬ���:"+hetbh,Table.ALIGN_LEFT);
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		
		rt.body.mergeCell(1,1,1,5);
		rt.body.mergeCell(1,6,1,15);
		rt.body.mergeCell(4,1,4,2);
		rt.body.mergeCell(5,1,5,15);
		
		rt.body.setRowHeight(30);

		rt.body.setPageRows(18);
		//rt.body.setHeaderData(ArrHeader);// ��ͷ����
		for(int i=1;i<=4;i++){
		  for(int j=1;j<=15;j++){
			rt.body.setColAlign(j, Table.ALIGN_CENTER);
		  }
		}
        
		
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 5, "�ƻ���Ӫ�����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13,2, "�Ʊ�:", Table.ALIGN_LEFT);
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
	
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

}