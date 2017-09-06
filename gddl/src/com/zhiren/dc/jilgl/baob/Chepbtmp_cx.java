package com.zhiren.dc.jilgl.baob;

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
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Chepbtmp_cx extends BasePage {
	
	/**
	 * ����:wzb
	 * ʱ��:2009-11-23 10:38:59
	 * ����:chepbtmp��ú��ѯ����
	 */
	
	/**
	 * ����:���
	 * ʱ��:2013-06-25
	 * ����:����getMeikmx2()����;
	 */
//	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb�ж�Ӧ�Ĺؼ���
//	�����û���ʾ
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
//	��ȡ������
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=visit.getDiancqc()+"ȼ�����յ�";
		
		return sb;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		
		dfb.Binding("BRiq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERiq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("ú��λ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(120);
		CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
	
//		��������
		tb1.addText(new ToolbarText("ͳ�Ʒ�ʽ:"));
		tb1.addText(new ToolbarText("-"));
		ComboBox leix = new ComboBox();
		leix.setTransform("JIESLB");
		leix.setWidth(90);
		leix.setListeners("select:function(){document.Form0.submit();}");
		//leix.setEditable(true);
		tb1.addField(leix);
		

		if(MainGlobal.getXitxx_item("����", "������Ϣ��ѯ�Ƿ���ʾ����������", "0", "��").equals("��")){
//			��������
			tb1.addText(new ToolbarText("����:"));
			tb1.addText(new ToolbarText("-"));
			ComboBox hengh = new ComboBox();
			hengh.setTransform("HENGH");
			hengh.setWidth(90);
			hengh.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(hengh);
		}
		
//		�糧Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		//�����һ�����ƾ���ʾ�糧��,������ǾͲ���ʾ�糧
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("�糧:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		if (getJieslbValue().getValue().equals("����")){
			return getMeikbbhz();
		}else if(getJieslbValue().getValue().equals("��ϸ")){
			if(MainGlobal.getXitxx_item("����", "������Ϣ��ѯ�Ƿ���ʾ˾������", "0", "��").equals("��")){
				return getMeikmx();
			}else{
				return getMeikmx2();
			}
		}else{
			return "�޴˱���";
		}
	}
	
	public String getMeikbbhz(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
//		StringBuffer talbe=new StringBuffer();	//�������
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String meik=this.getGongysValue().getValue();
		String meiktiaoj="";
		if(meik.equals("ȫ��")){
			meiktiaoj="";
		}else{
			meiktiaoj=" and c.meikdwmc= '"+meik+"'";
		}
		
	
		long hengh=this.getHenghValue().getId();
		String henghTiaoj="";
		if(hengh==0){
			henghTiaoj="";
		}else if(hengh==1){
			if(v.getDiancxxb_id()==264){//264�����Ƿ����id,���Ƿ�������Ž�"Aϵͳ"
				henghTiaoj=" and c.qingchh='Aϵͳ'";
			}else{
				henghTiaoj=" and c.qingchh='C'";
			}
			
		}else if(hengh==2){
			if(v.getDiancxxb_id()==264){
				henghTiaoj=" and c.qingchh='Bϵͳ'";
			}else{
				henghTiaoj=" and c.qingchh='A'";
			}
		
		}else {
			henghTiaoj="";
		}
		
		
		Report rt = new Report(); //������
				sbsql.setLength(0);
					sbsql.append(
							" select  decode(c.gongysmc,null,'�ܼ�',c.gongysmc) as gonghdwmc,\n"+
							"decode(grouping(c.meikdwmc)+grouping(c.gongysmc),2,'',1,'С��',c.meikdwmc) as meikdwmc,\n" +
							"count(c.id) as ches,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
							"sum(c.koud) as koud,to_char(min(c.daohrq),'yyyy-mm-dd') as kaisrq,to_char(max(c.daohrq),'yyyy-mm-dd') as jiezrq\n" + 
							"from chepbtmp c\n" + 
							"where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
							" "+meiktiaoj+"\n"+
							" "+henghTiaoj+"\n"+
							" and c.yunsfs='��·'\n"+
							"group by rollup (c.gongysmc,c.meikdwmc)\n" + 
							"order by c.gongysmc,c.meikdwmc"

					);
					rs=con.getResultSetList(sbsql.toString());
					
					String ArrHeader[][]=new String[1][9];
		
					ArrHeader[0]=new String[] {"��Ӧ��","ú��","����","ë��","Ƥ��","����","�۶�","��ʼ����","��������"};
					int ArrWidth[]=new int[] {120,120,45,75,75,75,75,80,80};
					  strFormat = new String[] {"", "", 
			    				"", "0.00", "0.00", "0.00", "0.00", "","" };
					// ����
					rt.setTitle(v.getDiancmc()+"����ú��ú����_(��ú��λ����)",ArrWidth);
					rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 2));
					rt.body.setColFormat(strFormat);
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(30);
					
					rt.body.setHeaderData(ArrHeader);// ��ͷ����
					rt.body.mergeFixedRow();
					rt.body.mergeFixedCols();
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			
			rs.close();
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(25);
			
	     	return rt.getAllPagesHtml();// ph;
	}
	
	
	
	public String getMeikmx(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String meik=this.getGongysValue().getValue();
		String meiktiaoj="";
		if(meik.equals("ȫ��")){
			meiktiaoj="";
		}else{
			meiktiaoj=" and c.meikdwmc= '"+meik+"'";
		}
		
		long hengh=this.getHenghValue().getId();
		String henghTiaoj="";
		if(hengh==0){
			henghTiaoj="";
		}else if(hengh==1){
			if(v.getDiancxxb_id()==264){//264�����Ƿ����id,���Ƿ�������Ž�"Aϵͳ"
				henghTiaoj=" and c.qingchh='Aϵͳ'";
			}else{
				henghTiaoj=" and c.qingchh='C'";
			}
			
		}else if(hengh==2){
			if(v.getDiancxxb_id()==264){
				henghTiaoj=" and c.qingchh='Bϵͳ'";
			}else{
				henghTiaoj=" and c.qingchh='A'";
			}
		
		}else {
			henghTiaoj="";
		}
		
		Report rt = new Report(); //������
				sbsql.setLength(0);
					sbsql.append(
							"select decode(c.meikdwmc,null,'�ܼ�:'||count(c.id)||'��',c.meikdwmc) as meikdwmc,\n" +
							"decode(grouping(c.cheph)+grouping(c.meikdwmc) ,1,'С��:'||count(c.id)||'��',c.cheph) as cheph,\n" + 
							"sum(c.maoz) as maoz,sum(c.piz) as piz,\n" + 
							"sum(c.maoz-c.piz-c.koud) as jingz,sum(c.koud) as koud,\n" + 
							"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj\n"+
							"from chepbtmp c\n" + 
							"where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
							""+meiktiaoj+"\n"+
							""+henghTiaoj+"\n"+
							" and c.yunsfs='��·'\n"+
							"group by rollup (c.meikdwmc,c.zhongcsj,c.cheph,c.qingcsj)\n" + 
							"having not (grouping(c.cheph)+grouping(c.zhongcsj)+grouping(c.qingcsj)=2 or grouping(c.zhongcsj)+grouping(c.qingcsj)=1)\n" + 
							"order by c.meikdwmc,zhongcsj"
					);
					rs=con.getResultSetList(sbsql.toString());
					
					String ArrHeader[][]=new String[1][8];
		//			1120
					ArrHeader[0]=new String[] {"��ú��λ","����","ë��","Ƥ��","����","�۶�","�س�ʱ��","�ճ�ʱ��"};
					int ArrWidth[]=new int[] {120,65,60,60,60,60,120,120};
					  strFormat = new String[] { "","",	"0.00", "0.00", "0.00", "0.00", "","" };
					// ����
					rt.setTitle(v.getDiancmc()+"����ú��ú����_(�����ϸ)",ArrWidth);
					rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(7, 2, "��λ:��", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 0));
					rt.body.setColFormat(strFormat);
//					�趨С���еı���ɫ������
					for (int i=2;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 7);
						if((xiaoj.equals(""))){
							//rt.body.setCellValue(i, 10, "");
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
					}
					
					
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(30);
					rt.body.setHeaderData(ArrHeader);// ��ͷ����
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			rs.close();
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			
	     	return rt.getAllPagesHtml();// ph;
	}
	
	public String getMeikmx2(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String meik=this.getGongysValue().getValue();
		String meiktiaoj="";
		if(meik.equals("ȫ��")){
			meiktiaoj="";
		}else{
			meiktiaoj=" and c.meikdwmc= '"+meik+"'";
		}
		
		long hengh=this.getHenghValue().getId();
		String henghTiaoj="";
		if(hengh==0){
			henghTiaoj="";
		}else if(hengh==1){
			if(v.getDiancxxb_id()==264){//264�����Ƿ����id,���Ƿ�������Ž�"Aϵͳ"
				henghTiaoj=" and c.qingchh='Aϵͳ'";
			}else{
				henghTiaoj=" and c.qingchh='C'";
			}
			
		}else if(hengh==2){
			if(v.getDiancxxb_id()==264){
				henghTiaoj=" and c.qingchh='Bϵͳ'";
			}else{
				henghTiaoj=" and c.qingchh='A'";
			}
		
		}else {
			henghTiaoj="";
		}
		
		Report rt = new Report(); //������
			sbsql.setLength(0);
			sbsql.append(
							"select decode(c.meikdwmc,null,'�ܼ�:'||count(c.id)||'��',c.meikdwmc) as meikdwmc,\n" +
							"decode(grouping(c.cheph)+grouping(c.meikdwmc) ,1,'С��:'||count(c.id)||'��',c.cheph) as cheph,\n" + 
							"sum(c.maoz) as maoz,sum(c.piz) as piz,\n" + 
							"sum(c.maoz-c.piz-c.koud) as jingz,sum(c.koud) as koud,\n" + 
							"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,c.sijxm\n"+
							"from chepbtmp c\n" + 
							"where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
							""+meiktiaoj+"\n"+
							""+henghTiaoj+"\n"+
							" and c.yunsfs='��·'\n"+
							"group by rollup (c.meikdwmc,(c.zhongcsj,c.cheph,c.qingcsj,c.sijxm))\n" + 
							"order by c.meikdwmc,qingcsj"
					);
			rs=con.getResultSetList(sbsql.toString());
			
			String ArrHeader[][]=new String[1][9];
//			1120
			ArrHeader[0]=new String[] {"��ú��λ","����","ë��","Ƥ��","����","�۶�","�س�ʱ��","�ճ�ʱ��","˾������"};
			int ArrWidth[]=new int[] {120,65,60,60,60,60,120,120,100};
			  strFormat = new String[] { "","",	"0.00", "0.00", "0.00", "0.00", "","","" };
			// ����
			rt.setTitle(v.getDiancmc()+"����ú��ú����_(�����ϸ)",ArrWidth);
			rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 2, "��λ:��", Table.ALIGN_RIGHT);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
//					�趨С���еı���ɫ������
			for (int i=2;i<=rt.body.getRows();i++){
				String xiaoj=rt.body.getCellValue(i, 7);
				if((xiaoj.equals(""))){
					//rt.body.setCellValue(i, 10, "");
					for (int j=0;j<rt.body.getCols()+1;j++){
						rt.body.getCell(i, j).backColor="silver";
						rt.body.getCell(i, j).fontBold=true;
					}
				}
			}
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(30);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
			rt.body.ShowZero = true;
			rs.close();
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			
	     	return rt.getAllPagesHtml();// ph;
	}
	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

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
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			//visit.setInt1(Integer.parseInt(reportType));
			visit.setString15(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			setChangbValue(null);
			setChangbModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
		}
		getSelectData();
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
			getSelectData();
		}
	}
//	ҳ���½��֤
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
	
//	 �������(���յ�,�ܸ���,��Ʊ��)
	public IDropDownBean getJieslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJieslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJieslbValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJieslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJieslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJieslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJieslbModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "����"));
		list.add(new IDropDownBean(2, "��ϸ"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
//	 �º�ɺ�
	public IDropDownBean getHenghValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getHenghModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setHenghValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setHenghModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getHenghModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getHenghModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getHenghModels() {
		List list = new ArrayList();
		Visit visit = (Visit) this.getPage().getVisit();
		if(visit.getDiancxxb_id()==264){//���Ƿ��������AϵͳBϵͳ
			list.add(new IDropDownBean(0, "ȫ��"));
			list.add(new IDropDownBean(1, "Aϵͳ"));
			list.add(new IDropDownBean(2, "Bϵͳ"));
		}else{//̫ԭ����������º�ɺ�
			list.add(new IDropDownBean(0, "ȫ��"));
			list.add(new IDropDownBean(1, "�º�"));
			list.add(new IDropDownBean(2, "�ɺ�"));
		}
		
		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(list));
		return;
	}
	
//	 ��Ӧ��������
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getGongysModels() {
		String sql_gongys = 
			"select rownum as xuh, a.meikdwmc from (\n" +
			"select distinct meikdwmc from chepbtmp order by meikdwmc ) a";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
	
}
