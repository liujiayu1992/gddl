package com.zhiren.dc.zhuangh.caiygl;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;



public class Ruchycx extends BasePage {
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
//	��ʼ����v
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_BeginriqChange=true;
		}
	}
	
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndriqChange=true;
		}
	}
	
	public boolean getRaw() {
		return true;
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
	
	private String REPORT_JIZYXQK="Report";
	private String REPORT_QUEMTJBG="quemtjbg";
	private String mstrReportName="";
	
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
		if(diancmc.equals("���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���")){
	          return "���ƹ���ȼ�Ϲ���";
		}else{
			return diancmc;
		}
		
	}
	
	private boolean isBegin=false;
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_JIZYXQK)){
			return getJizyxqkreport();
		}else{
			return getJizyxqkreport();
		}
	}

	public String getJizyxqkreport(){
		Visit visit = (Visit) getPage().getVisit();
		_CurrentPage=1;
		_AllPages=1;
		Date dat=new Date();//getBeginriqDate();//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = " and dc.jib=3 ";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + "";
		}
		
		//ȡ��ҳ���ѯ��ʽ
		String zhuangt=this.getBaoblxValue().getStrId();
		
		//��Ź���Ĳ���sql���
		String biaoz="=";
		
		//�������Ĺ���
		if(zhuangt.equals("0")){
			biaoz=" and "+
			"rb.shangmkssj >=\n" +
			"to_date(to_char(sysdate-1,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
			"and rb.shangmjssj< to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss') \n"+
			" and rb.shangmjssj is not null";


		}else if(zhuangt.equals("1")){
			biaoz="and "+
				"rb.shangmkssj >=\n" +
				"to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
				"and rb.shangmjssj< sysdate  \n"+
			" and rb.shangmjssj is not null"
;
		}else if(zhuangt.equals("2")){
			biaoz=
		" and rb.shangmjssj is  null";
		}else if(zhuangt.equals("3")){
			biaoz=" and rb.shangmkssj > to_date(to_char(sysdate-1,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" +
			"and rb.shangmkssj< to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss') \n"+		
			" and rb.shangmjssj is  null";
		}else if(zhuangt.equals("4")){
			biaoz="";
		}
		
		biaoz="and "+
		"rb.shangmkssj >=\n" +
		"to_date(to_char(sysdate,'yyyy-mm-dd')||' '||'14:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
		"and rb.shangmjssj< sysdate  \n"+
	" and rb.shangmjssj is not null";
		String c_sql=

			"select zm.bianm,mz.mingc\n" +
			",round_new(r.mt,1),r.mad,r.aad,\n" + 
			"r.vad,r.stad,r.had,r.fcad,r.aar,r.vdaf, r.ad,round_new((r.vad/(100-r.mad))*100,2) as vd ," +
			"round_new(r.qbad,2),round_new(r.qgrd,2),round_new(r.qgrad,2)," +
			"round_new(r.qnet_ar,2), round_new(round_new(r.qnet_ar,2) * 1000 / 4.1816, 0),\n" + 
			"r.huayy,to_char(r.huaysj,'yyyy-mm-dd')\n" + 
			"from zhillsb r,pinzb mz	,fahb f,zhuanmb zm,zhuanmlb l\n" + 
			" where r.id=" +visit.getString8()+
			" and r.zhilb_id=f.zhilb_id and  zm.zhuanmlb_id=l.id and l.jib=3 and r.id=zm.zhillsb_id"+
			" and\n" + 
			" f.pinzb_id=mz.id";
		String c_sq=

			"select zm.bianm,mz.mingc\n" +
			",round_new(r.mt,1),r.mad,r.aad,\n" + 
			"r.vad,r.stad,r.had,r.fcad,r.aar,r.vdaf, r.ad,round_new((r.vad/(100-r.mad))*100,2) as vd ," +
			"round_new(r.qbad,2),round_new(r.qgrd,2),round_new(r.qgrad,2)," +
			"round_new(r.qnet_ar,2), round_new(round_new(r.qnet_ar,2) * 1000 / 4.1816, 0),\n" + 
			"r.huayy,to_char(r.huaysj,'yyyy-mm-dd'),r.shenhry,r.shenhryej\n" + 
			"from zhillsb r,pinzb mz	,fahb f,zhuanmb zm,zhuanmlb l\n" + 
			" where r.id=" +visit.getString8()+
			" and r.zhilb_id=f.zhilb_id and  zm.zhuanmlb_id=l.id and l.jib=3 and r.id=zm.zhillsb_id"+
			" and\n" + 
			" f.pinzb_id=mz.id";

//			"select r.huaybh,mz.mingc \n" +
//			",r.mt,r.mad,r.aad,\n" + 
//			"r.vad,r.stad,r.had,r.fcad,r.aar,r.vdaf, r.ad,round_new((r.vad/(100-r.mad))*100,2) as vd ,r.qbad,r.qgrd,r.qgrad," +
//			"r.qnet_ar, round_new(r.qnet_ar * 1000 / 4.1816, 0),\n" + 
//			"r.huayy,to_char(r.huaysj,'yyyy-mm-dd'),zb.shenhry,zb.shenhryej \n" + 
//			"from zhilb r,pinzb mz	,fahb f,zhillsb zb\n" + //,caiyb z
//			" where id=" +visit.getString8()+
////			"r.huaysj>=to_date('"+getRiq()+"'||' '||'13:00:00','yyyy-mm-dd hh24:mi:ss')-1" +
////			" and r.huaysj<to_date('"+getRiq()+"'||' '||'13:00:00','yyyy-mm-dd hh24:mi:ss')"+"\n"+
//			" \n" + 
//			" and \n" + 
//			" "+
//			" r.id= zb.zhilb_id and "+
//			" r.id=f.zhilb_id and\n" + 
//			" f.pinzb_id=mz.id";

		
		String ArrHeader[][]=new String[1][19];//19
		ArrHeader[0]=new String[] {"������","ú��","Mt(%)","Mad(%)","Aad(%)","Vad(%)",
				"St,ad(%)","Had(%)","Fc,ad(%)","Aar(%)","Vdaf(%)","Ad(%)","Vd(%)"
				,"Qb,ad(MJ/kg)","Qgr,d(MJ/kg)","Qgr,ad(MJ/kg)",
				"Qnet,ar(MJ/kg)","Qnet,ar(Kcal/kg)",
				"����Ա","��������"};

		int ArrWidth[]=new int[] {70,60,80,45,45,45,45,45,45,45,45,
				45,45,45,45,45,
				45,45,
				120,80};
		
		//������¼������
		ResultSet rs = cn.getResultSet(c_sql);
		int s=0;
		
			
			try {
				if(rs.getRow()>0){
					s=rs.getRow();
				}
			} catch (SQLException e1) {
				// TODO �Զ����� catch ��
				e1.printStackTrace();
			}

		StringBuffer footer=new StringBuffer("");
		try {
			String []a=new String[15]  ;
			String []ab=new String[15]  ;
			int i=0;
			int count=0;//��¼���и���
			while(rs.next()){
				//�жϵ�һ���Ƿ�Ϊ��
				if(!"".equals(rs.getString("huayy"))&&rs.getString("huayy")!=null){
					
					//ȡ����Ա��������������
//					a[i]=rs.getString("huayy");
					//ÿ��ȡ�������Ա
					ab=rs.getString("huayy").split(",");
				//�ж��Ƿ�����ͬ
				boolean flag=false;
				//ȡ�ö�ά����ĳ���
//				System.out.println(rs.getString("huayy"));
				for(int b=0;b<a.length;b++){
//					ȡ��ĳ����ά�����һά����
//					for(int m=0;m<a[i].length;m++){
//						
//						
//					}
					//���鲻���ڼ�¼��������
					
					//�ж��Ƿ��С�����
					if(rs.getString("huayy").length()>rs.getString("huayy").replaceAll(",", "").length()){
						//�ж���
						//��ô�����зֽ�
						flag=true;
						
					}else{//û�ж���
						
						
//						if((!rs.getString("huayy").equals(a[b]))&&!rs.getString("huayy").equals("")){
							flag=true;
//							System.out.println(flag);
//						}
					}
				}

				//��һ��Ҫȫ���������ڶ����Ժ�����в��ظ�����ô�ͼ������������flag�����жϵ����Ƿ���ͬ
				if(i==0||flag){
					//��¼����
					if(i==0){
						
//						a=rs.getString("huayy").split(",");
						for(int q=0;q<rs.getString("huayy").split(",").length;q++){
							a[q]=rs.getString("huayy").split(",")[q];
//							System.out.println("a["+q+"]"+a[q]);
						}
						count=rs.getString("huayy").split(",").length;
					}else{
						
						String[] bt= rs.getString("huayy").split(",");
						//�ж��ظ�
						for(int m=0;m<bt.length;m++){//����
							boolean pan=false;
							int e=0;
							for(int n=0;n<count;n++){//�����
								//��ʾbu��ͬ�Ҳ���""
								if(!bt[m].equals(a[n])&&!"".equals(a[n]) && !"".equals(bt[m])){
//									System.out.println("a[n]+bt[m]"+a[n]+bt[m]);
									e++;//��¼��ͬ��Ŀ
								}
								if(e==count){
									
									pan=true;
								}
							}
							if(pan){
								
								a[count]=bt[m];
//								System.out.println("a{}"+a[count]);
								count++;
							}
						}
//						System.out.println(i);
					}
					i++;
					
					
				 }
				}
			}
			
			for(int w=0;w<a.length;w++){
				String sql_d=" select mingc from renyxxb where quanc='"+a[w]+"'";
//				System.out.println("a["+w+"]"+a[w]);
				ResultSet rs_d =cn.getResultSet(sql_d);
				while(rs_d.next()){
					
					//�ж�������Դ�Ƿ����
					if(isNetFileAvailable(MainGlobal.getHomeContext(getPage())+"/imgs/"+rs_d.getString("mingc")+".gif"+"")){
						
//								System.out.println("you");
						footer.append("<image   src='"+MainGlobal.getHomeContext(getPage())+"/imgs/"+rs_d.getString("mingc")+".gif"+"'"
								+"  width=\"100\" height=\"80\" align=\"middle\"  />"		
						);
					}else{
						
						System.out.println("meiyou");
					}
				}
			} 
//			footer.append("</tr></table>");
			rs.beforeFirst();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		
		rt.setTitle("�볧ú���鱨�浥",ArrWidth);
		//footer
		ResultSet rs_zh=cn.getResultSet(c_sq);
		StringBuffer sb_zhu=new StringBuffer("");
		StringBuffer sb_z=new StringBuffer("");

		String shenhry="";
		String shenhryej="";
		try {
			while (rs_zh.next()){
				shenhry=rs_zh.getString("shenhry");
				shenhryej=rs_zh.getString("shenhryej");
			}
			

			 //�ж�������Դ�Ƿ����

			//һ�����
				String sql_zhu=" select mingc from renyxxb where quanc='"+shenhry+"'";
				ResultSet rs_zhu =cn.getResultSet(sql_zhu);
				while(rs_zhu.next()){
					
					if(isNetFileAvailable(MainGlobal.getHomeContext(getPage())+"/imgs/"+rs_zhu.getString("mingc")+".gif"+"")){
						
//						System.out.println("you");
						sb_zhu.append("<image   src='"+MainGlobal.getHomeContext(getPage())+"/imgs/"+rs_zhu.getString("mingc")+".gif"+"'"
								+"  width=\"100\" height=\"80\" align=\"middle\"  />"		
						);
					}else{
						
						System.out.println("meiyou");
					}
				}
				//�������
				String sql_z=" select mingc from renyxxb where quanc='"+shenhryej+"'";
				ResultSet rs_z =cn.getResultSet(sql_z);
				while(rs_z.next()){
					
					if(isNetFileAvailable(MainGlobal.getHomeContext(getPage())+"/imgs/"+rs_z.getString("mingc")+".gif"+"")){
						
//						System.out.println("you");
						sb_z.append("<image   src='"+MainGlobal.getHomeContext(getPage())+"/imgs/"+rs_z.getString("mingc")+".gif"+"'"
								+"  width=\"100\" height=\"80\" align=\"middle\"  />"		
						);
					}else{
						
						System.out.println("meiyou");
					}
				}

		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		rt.setDefaultTitle(1, 7, "�Ʊ�λ:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 2, "", Table.ALIGN_RIGHT);
		rt.setDefaultTitle(13, 6, " ",Table.ALIGN_RIGHT);
		
		String ArrFooter[][]=new String[1][20];
		String ArrFooter1[][]=new String[1][20];
		ArrFooter[0]=new String[] {
				"<br><br>ע��Mt(%):ȫˮ�� Mad(%):���������ˮ�� Aad(%):����������ҷ� Vad(%):����������ӷ��� St,ad(%):�����������ֵ Had(%):�����������ֵ <br>      ;Fc,ad(%):����������̶�̼ Aar(%):�յ����ҷ� Vdaf(%):�����޻һ��ӷ��� Ad(%):������ҷ� Vd(%):������ӷ��� Qb,ad(MJ/kg):�����������Ͱ������ <br> Qgr,d(MJ/kg):�������λ��ֵ Qgr,ad(MJ/kg):�����������λ��ֵ  &nbsp;&nbsp;&nbsp; Qnet,ar(MJ/kg):�յ�����λ��ֵ Qnet,ar(Kcal/kg):�յ�����λ��ֵ<br><br>",//
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"" ,
				""  ,"" ,"" 
				
		};
		Report rt_footer=new Report();
		Report rt_footer1=new Report();
		ArrFooter1[0]=new String[]{"���ܣ�"+sb_z.toString(),"","","","","","��ˣ�"+sb_zhu.toString(),"","","","","���飺"+footer.toString(),"","","","","","","",""};
		
		
		
		rt.setBody(new Table(rs,1,0,0));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
	
		
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		

			 rt_footer.setBody(new Table(ArrFooter, 0, 0, 0));
			 rt_footer.body.setWidth(ArrWidth);
			 rt_footer.body.mergeCell(1, 1, 1, 20);

			 rt_footer.body.setBorder(1, 1, 0, 0);
			 rt_footer.getHtml();
			 rt_footer1.setBody(new Table(ArrFooter1, 0, 0, 0));
			 rt_footer1.body.setWidth(ArrWidth);
			 rt_footer1.body.mergeCell(1, 1, 1, 6);
			 rt_footer1.body.mergeCell(1, 7, 1, 11);
			 rt_footer1.body.mergeCell(1, 12, 1, 20);
			 rt_footer1.body.setBorder(1, 1, 1, 1);
			 rt_footer1.getHtml();
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml()+rt_footer.getAllPagesHtml()+rt_footer1.getAllPagesHtml();
	}

//	
	//
	public static boolean isNetFileAvailable(String netFileUrl)
	{
	   InputStream   netFileInputStream =null;
	    try{
	     URL   url   =   new   URL(netFileUrl);   
	     URLConnection   urlConn   =   url.openConnection();   
	     netFileInputStream   =   urlConn.getInputStream(); 
	    }catch (IOException e)
	    {
	     return false;
	    }
	     if(null!=netFileInputStream)
	     {
	      return true;
	     }else
	     {
	     return false;
	     }
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private void Return(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();

			
			cycle.activate("Huaylrxg_zh");
		
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			setRiq(DateUtil.FormatDate(new Date()));
			this.setTreeid(null);
			
			//begin��������г�ʼ������
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					this.setTreeid(null);
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }

		if(_Baoblxchange){
			_Baoblxchange=false;
			getJizyxqkreport();
		}
		getToolBars();
		isBegin=true;
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
		setDiancxxModel(new IDropDownModel(sql,"�е�Ͷ"));
	}
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
		
//		tb1.addText(new ToolbarText("����:"));
//		DateField df = new DateField();
//		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
//		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df.setWidth(100);
//		//df.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(df);
//		
//		tb1.addText(new ToolbarText("-"));
		
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
		
//		tb1.addText(new ToolbarText("����ѡ��:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		cb.setId("Baoblx");
//		cb.setWidth(120);
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("��������:"));
//		DateField df = new DateField();
//		df.setValue(getRiq());
//		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df.setId("rulrq");
//		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		ToolbarButton rbtn = new ToolbarButton(null, "����",
				"function(){document.getElementById('ReturnButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
//		ToolbarButton tbLink = new ToolbarButton("ReturnButton","����","function(){document.getElementById('ReturnButton').click();}");
		tb1.addItem(tb);
//		tb1.addItem(tbLink);
		setToolbar(tb1);
	}
	
	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
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
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		
		_IDiancmcModel = new IDropDownModel(sql);
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
	
//  Day������
	private boolean _Day = false;
	private IDropDownBean _DayValue;
	private IPropertySelectionModel _DayModel;

	public IDropDownBean getDayValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean) getDayModel().getOption(14));
    	}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	public void setDayValue(IDropDownBean Value) {
//		if(((Visit)getPage().getVisit()).getDropDownBean1()!=null){
			if(((Visit)getPage().getVisit()).getDropDownBean1().getId()!=Value.getId()){
				_Day=true;
			}
//		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setDayModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
    }
    public IPropertySelectionModel getDayModel() {
        if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
            getDayModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }
    public void getDayModels() {
        List listDay = new ArrayList();
//        listDay.add(new IDropDownBean(-1, "��ѡ��"));
    	for (int i = 1; i < 32; i++) {
            listDay.add(new IDropDownBean(i, String.valueOf(i)+"��"));
        }
    	((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listDay));
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
			fahdwList.add(new IDropDownBean(0,"����(����14�㵽����14��)"));
			fahdwList.add(new IDropDownBean(1,"����(����14�㵽����)"));
			fahdwList.add(new IDropDownBean(2,"δ���ȡ��(����)"));
			fahdwList.add(new IDropDownBean(3,"δ���ȡ��(����)"));
			fahdwList.add(new IDropDownBean(4,"����"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
}
