package com.zhiren.dtrlgs.fkgl;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
//import java.util.Calendar;
import java.util.Calendar;
import java.util.Date;
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
//import com.zhiren.liuc.Liuc;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import org.apache.tapestry.contrib.palette.SortMode;

public class FuktzdcxS extends BasePage implements PageValidateListener{
	
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
	

//	��ʽ��
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//��ǧλ�ָ���
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
	
//*****************************************�������ÿ�ʼ******************************************//	
//****************��������*******************//
	//��ʼ����
	private String _BeginriqValue ="";
	
	public String getBeginriqDate() {
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(String _value) {
		if(!this._BeginriqValue.equals(_value)){
			this._BeginriqValue = _value;
			flag=true;
		}
	}
	//��������
	private String _EndriqValue ="";
	
	public String getEndriqDate() {
		return _EndriqValue;
	}
	
	public void setEndriqDate(String _value) {
		if(!this._EndriqValue.equals(_value)){
			this._EndriqValue = _value;
			flag=true;
		}
	}
	
//***************������Ϣ��******************//
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
//******************��ť����****************//
	//���ذ�ť
	private boolean _FindChick = false;

	public void FindButton(IRequestCycle cycle) {
		_FindChick = true;
	}
	public void submit(IRequestCycle cycle) {
		
		if (_FindChick) {
			_FindChick = false;
	    }
	}
//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBeginriqDate(DateUtil.FormatDate(new Date()));
			setEndriqDate(DateUtil.FormatDate(new Date()));
			setBianhValue(null);
			setBianhModel(null);
		}
		getToolBars();
		if(flag){
			setBianhValue(null);
			setBianhModel(null);
			flag=false;
		}
//		getSelectData();
	}
//	************************end********************
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		  try{
			 String sql=""; 
			 long id=-1;
			 String strzhibrq="";//�Ʊ�����
			 String strfukdbh="";
			 String strfukdlx="";
			 String strfapje="";
			 double dbshijfk=0;
			 
			 String strhexyfk="";
			 String strkouyf="";
			 String strkouhkf="";
			 String strshijfkdx="";
			 String strfapbhs="";
			 String strshoukdw="";
			 String strgongysbm="";
			 String strdiz="";
			 String strkaihyh="";
			 String strzhangh="";
			 String strxiangmmc="";
			 String strxiangmbh="";
			 String strcaiwtxxx="";
			 String strcaiwtxbz="";
			 String strtianzdw="";
			 
			 String strshijfk="";
			 
			 sql = "select fk.*,gy.quanc,gy.bianm,gy.danwdz diz,gy.kaihyh,gy.zhangh "
				 + "  from fuktzb fk,gongysb gy where fk.gongysb_id=gy.id and fukdbh='"+getBianhValue().getValue()+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 if(rs.next()){
				 id=rs.getLong("id");
				 strzhibrq=FormatDate(rs.getDate("riq"));
				 
				 strfukdbh=rs.getString("fukdbh");
				 strfukdlx=rs.getString("fukdlx");
				 
				 strfapje=new DecimalFormat("0.00").format(rs.getDouble("fapje"));
				 dbshijfk = rs.getDouble("shijfk");
				 strhexyfk=new DecimalFormat("0.00").format(rs.getDouble("hexyfk"));
				 strkouyf=new DecimalFormat("0.00").format(rs.getDouble("kouyf"));
				 strkouhkf=new DecimalFormat("0.00").format(rs.getDouble("kouhkf"));
				
				 strshijfkdx=getDXMoney(dbshijfk);
				 strfapbhs=rs.getString("fapbhs");
				 if(strfapbhs!=""&&strfapbhs!=null){
				 String[] fabh2=strfapbhs.split(",");
				 strfapbhs="";
				  int j=0;
				 for(int i=0;i<fabh2.length;i++){
					 j++;
					 strfapbhs+=fabh2[i]+",";
					 if(j%10==0){
						 
						 strfapbhs+="<br>";
					 }
							 
				 }
				 strfapbhs= strfapbhs.substring(0,strfapbhs.lastIndexOf(",")-1);
				 }
				 strshoukdw=rs.getString("quanc");
				 
				 strgongysbm=rs.getString("bianm");
				 strdiz=rs.getString("diz");
				 strkaihyh=rs.getString("kaihyh");
				 strzhangh=rs.getString("zhangh");
				 strxiangmmc=rs.getString("xiangmmc");
				 strxiangmbh=rs.getString("xiangmbh");
				 strcaiwtxxx=rs.getString("caiwtxxx");
				 strcaiwtxbz=nvlStr(rs.getString("caiwtxbz"));
				 strtianzdw=nvlStr(rs.getString("tianzdw"));
				 
//				********************��Ա����*****************	//
			 }
			 String shenhxx[] = {"","","","","",""};
//			 �������״̬
			 String sql_tr = "select fk.id,lz.mingc as zhuangt,fk.liucztb_id,fk.liucgzid from fuktzb fk,liucztb zt,leibztb lz where fk.liucztb_id=zt.id and zt.leibztb_id=lz.id and fukdbh='"+this.getBianhValue().getValue()+"'";
				long liucgzid=0;
				long liucztb_id=0;
			ResultSet	rs_liuczt = cn.getResultSet(sql_tr);
				while(rs_liuczt.next()){
					liucgzid=rs_liuczt.getLong("liucztb_id");
					liucztb_id=rs_liuczt.getLong("liucgzid");
				}
				String kfsql="select *  from liucgzb where id in(\n"+
					   " select max(gz.id) gz_id from liucgzb gz,liucdzb dz,liucztb zt1,liucztb zt2 ,liucztb zt3\n"+
					   " where gz.liucdzb_id=dz.id  and gz.liucgzid="+liucztb_id+" and dz.liucztqqid=zt1.id and dz.liuczthjid=zt2.id and zt1.xuh<zt2.xuh \n"+
					   " and zt2.xuh<=zt3.xuh \n"+
					   "    and zt3.id="+liucgzid+"\n"+
//					   " and gz.liucgzid="+liucgzid+"\n"+
					   "   group by gz.liucdzb_id\n"+
					  "  ) order by shij";
			 rs = cn.getResultSet(kfsql);
			 for(int i=0;rs.next();i++){
				 shenhxx[i] = rs.getString("caozy");
			 }
			 rs.close();
			 
			 strshijfk = new DecimalFormat("0.00").format(dbshijfk);
			 String strmoney = new DecimalFormat("0").format(dbshijfk*100);
			 String money[] = {"","","","","","","","","","",""};
			 for(int i=0;i<strmoney.length();i++){
				 if(i<10){
					 money[10-i] = strmoney.substring(strmoney.length()-(i+1), strmoney.length()-i);
				 }else{
					 money[0] = strmoney.substring(0, strmoney.length()-i);
				 }
			 }
			 String strshenhr = "";
			 String biaot="";
			 if(strfukdlx.equals("Ԥ����")){
				 strshenhr = "������";
				 biaot="Ԥ �� �� ͨ ֪ ��";
			 }else{
				 strshenhr = "�����";
				 biaot="�� �� ͨ ֪ ��";
			 }
			 String ArrHeader[][]=new String[14][21];
			 ArrHeader[0]=new String[] {"��������","",strfukdlx,"","","","","","","","","","","","","","","","","",""};
			 ArrHeader[1]=new String[] {"��Ʊ���","","���˷�","","�ۻؿշ�","","����Ԥ����","","ʵ�ʸ���","","ʵ�ʸ�����","","","","","","","","","",""};
			 ArrHeader[2]=new String[] {"","","","","","","","","","","��","ǧ","��","ʮ","��","ǧ","��","ʮ","Ԫ","��","��"};
			 ArrHeader[3]=new String[] {formatq(strfapje),"",formatq(strkouyf),"",formatq(strkouhkf),"",formatq(strhexyfk),"",formatq(strshijfk),"",money[0],money[1],money[2],money[3],money[4],money[5],money[6],money[7],money[8],money[9],money[10]};
			 ArrHeader[4]=new String[] {"��д","",strshijfkdx,"","","","","","","","","","","","","","","","","",""};
			 ArrHeader[5]=new String[] {"��Ʊ���","",strfapbhs,"","","","","","","","","","","","","","","","","",""};
			 ArrHeader[6]=new String[] {"�տλ��Ϣ","","����","",strshoukdw,"","","","","","","","","","","","","","","",""};
			 ArrHeader[7]=new String[] {"","","����","",strgongysbm,"","","","","��ַ","",strdiz,"","","","","","","","",""};
			 ArrHeader[8]=new String[] {"","","��������","",strkaihyh,"","","","","�����ʺ�","",strzhangh,"","","","","","","","",""};
			 ArrHeader[9]=new String[] {"��Ŀ��Ϣ","","��Ŀ����","",strxiangmmc,"","","","","��Ŀ���","",strxiangmbh,"","","","","","","","",""};
			 if(strfukdlx.equals("Ԥ����")){
				 ArrHeader[10]=new String[] {"��λ����",shenhxx[3],"�������",shenhxx[2],"���","","����","","��������",shenhxx[1],"������",shenhxx[0],"","","",strshenhr,"","","","",""};
			 }else if(dbshijfk==0){
				 ArrHeader[10]=new String[] {"��λ����",shenhxx[3],"�������",shenhxx[2],"���","","����","","��������",shenhxx[1],"������",shenhxx[0],"","","",strshenhr,"","","","",""};
			 }else{
				 ArrHeader[10]=new String[] {"��λ����",shenhxx[3],"�������",shenhxx[2],"���","","����","","��������",shenhxx[1],"������",shenhxx[0],"","","",strshenhr,"","","","",""};
			 }
			 ArrHeader[11]=new String[] {"���������в�����д","","","","","","","","","","","","","","","","","","","",""};
			 ArrHeader[12]=new String[] {"�����ռ�������/Ӧ������ƾ֤��","","","","","","","","��ע","","","","","","","","","","","",""};
			 ArrHeader[13]=new String[] {strcaiwtxxx,"","","","","","","",strcaiwtxbz,"","","","","","","","","","","",""};

			 int ArrWidth[]=new int[] {23,110,23,110,23,110,23,110,23,110,23,23,23,23,23,23,23,23,23,23,23};

//			 String logoPath = MainGlobal.getHomeContext(this)+"/img/report/logo.gif";
//			 ����ҳTitle
			 
			 rt.setTitle(biaot,ArrWidth);
//			 rt.title.setCellImage(2,3,35,32,logoPath);
			 rt.setDefaultTitleLeft("���Ƶ�λ��"+strtianzdw,5);
			 rt.setDefaultTitle(7,3,"�Ʊ����ڣ�"+strzhibrq,Table.ALIGN_LEFT);
			 rt.setDefaultTitle(16,6,"���:"+strfukdbh,Table.ALIGN_RIGHT);
			 rt.setBody(new Table(ArrHeader,0,0,0));
			 rt.body.setRowHeight(25);
			 rt.body.setRowHeight(11,60);
			 rt.body.setRowHeight(14, 80);
			 rt.body.setWidth(ArrWidth);	
			 rt.body.ShowZero=true;
//			 �ϲ���Ԫ��
			 rt.body.mergeCell(1,1,1,2);
			 rt.body.mergeCell(1,3,1,21);
			 rt.body.mergeCell(2,1,3,2);
			 rt.body.mergeCell(2,3,3,4);
			 rt.body.mergeCell(2,5,3,6);
			 rt.body.mergeCell(2,7,3,8);
			 rt.body.mergeCell(2,9,3,10);
			 rt.body.mergeCell(2,11,2,21);
			 rt.body.mergeCell(4,1,4,2);
			 rt.body.mergeCell(4,3,4,4);
			 rt.body.mergeCell(4,5,4,6);
			 rt.body.mergeCell(4,7,4,8);
			 rt.body.mergeCell(4,9,4,10);
			 rt.body.mergeCell(5,1,5,2);
			 rt.body.mergeCell(5,3,5,21);
			 rt.body.mergeCell(6,1,6,2);
			 rt.body.mergeCell(6,3,6,21);
			 rt.body.mergeCell(7,1,9,2);
			 rt.body.mergeCell(7,3,7,4);
			 rt.body.mergeCell(7,5,7,21);
			 rt.body.mergeCell(8,3,8,4);
			 rt.body.mergeCell(8,5,8,9);
			 rt.body.mergeCell(8,10,8,11);
			 rt.body.mergeCell(8,12,8,21);
			 rt.body.mergeCell(9,3,9,4);
			 rt.body.mergeCell(9,5,9,9);
			 rt.body.mergeCell(9,10,9,11);
			 rt.body.mergeCell(9,12,9,21);
			 rt.body.mergeCell(10,1,10,2);
			 rt.body.mergeCell(10,3,10,4);
			 rt.body.mergeCell(10,5,10,9);
			 rt.body.mergeCell(10,10,10,11);
			 rt.body.mergeCell(10,12,10,21);
			 rt.body.mergeCell(11,12,11,15);
			 rt.body.mergeCell(11,17,11,21);
			 rt.body.mergeCell(12,1,12,21);
			 rt.body.mergeCell(13,1,13,8);
			 rt.body.mergeCell(13,9,13,21);
			 rt.body.mergeCell(14,1,14,8);
			 rt.body.mergeCell(14,9,14,21);
			 
			 rt.body.setCells(1,1,3,21,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(1,3,1,21,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(4,1,4,21,Table.PER_ALIGN,Table.ALIGN_RIGHT);
			 rt.body.setCells(5, 1, 12, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(7, 3, 13, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(8, 10, 10, 11, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(13, 1, 13, 21, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 5, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 7, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 9, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 11, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 16, Table.ALIGN_CENTER);
			 
				// ����ҳ��
				_CurrentPage = 0;
//				_AllPages = rt.body.getPages();
				
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				return rt.getAllPagesHtml(0);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return rt.getAllPagesHtml(0);
	}
//	**********ToolBar**************
	private boolean flag = false;
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");		

		tb1.addText(new ToolbarText("����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBeginriqDate());
		dfb.Binding("BRIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getEndriqDate());
		dfe.Binding("ERIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("xiemrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("����"));
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(100);
		comb1.setTransform("BIANH");
		comb1.setEditable(true);
		comb1.setLazyRender(true);// ��̬��

//		comb1.setReadOnly(true);
		comb1.setId("BIANH");
		comb1.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(comb1);
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
//	���
	private IDropDownBean BianhValue;

	public IDropDownBean getBianhValue() {
		if (BianhValue == null) {
			BianhValue = (IDropDownBean) getBianhModel().getOption(0);
		}
		return BianhValue;
	}

	public void setBianhValue(IDropDownBean Value) {
		if (!(BianhValue == Value)) {
			BianhValue = Value;
		}
	}

	private IPropertySelectionModel BianhModel;

	public void setBianhModel(IPropertySelectionModel value) {
		BianhModel = value;
	}

	public IPropertySelectionModel getBianhModel() {
		if (BianhModel == null) {
			getBianhModels();
		}
		return BianhModel;
	}

	public IPropertySelectionModel getBianhModels() {
		String sql="select id,fukdbh from fuktzb where riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and riq<to_date('"+getEndriqDate()+"','yyyy-mm-dd')+1";
		BianhModel = new IDropDownModel(sql,"��ѡ��");
		return BianhModel;
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
//***************************�����ʼ����***************************//
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


	public String getPrintTable() {
		if (!getBianhValue().getStrId().equals("-1")) {
			return getSelectData();
		} else {
			return "�޴˱���";
		}
	}
	
//******************************����*******************************//
	
//	public String getcontext() {
//		/*return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).getContextPath() + "';";*/
//		return MainGlobal.getHomeContext(this);
//	}

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
//			return MainGlobal.Formatdate("yyyy�� MM�� dd��", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
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