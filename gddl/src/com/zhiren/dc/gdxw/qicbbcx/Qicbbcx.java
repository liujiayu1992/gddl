package com.zhiren.dc.gdxw.qicbbcx;

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
import com.zhiren.common.Locale;
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
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Qicbbcx extends BasePage {
	
	/**
	 * ����:wzb
	 * ʱ��:2009-9-16 14:34:10
	 * ����:��������������ú��ѯ����
	 */
	
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

    
//  ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
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
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("ú��λ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(120);
		//CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
	
		
//		��Ӧ��������
		tb1.addText(new ToolbarText("���䵥λ:"));
		ComboBox CB_yunsdw = new ComboBox();
		CB_yunsdw.setTransform("YUNSDW");
		CB_yunsdw.setWidth(100);
		CB_yunsdw.setEditable(true);
		tb1.addField(CB_yunsdw);
		tb1.addText(new ToolbarText("-"));
		
		
		
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
		
		
		
		
//		//����
		tb1.addText(new ToolbarText("����:"));
		ComboBox CB_cheh = new ComboBox();
		CB_cheh.setTransform("CHEH");
		CB_cheh.setWidth(100);
		
		CB_cheh.setEditable(true);
		tb1.addField(CB_cheh);
		tb1.addText(new ToolbarText("-"));

		
		//�����һ�����ƾ���ʾ�糧��,������ǾͲ���ʾ�糧
		if(hasDianc(String.valueOf(visit.getDiancxxb_id()))){
			tb1.addText(new ToolbarText("�糧:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
//		��������
		tb1.addText(new ToolbarText("-"));
		ComboBox leix = new ComboBox();
		leix.setTransform("JIESLB");
		leix.setWidth(90);
		leix.setListeners("select:function(){document.Form0.submit();}");
	
		tb1.addField(leix);
		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
		if (getJieslbValue().getValue().equals("����")){
			return getMeikbbhz();
		}else if(getJieslbValue().getValue().equals("��ϸ")){
			return getMeikmx();
		}else{
			return "�޴˱���";
		}
	}
	
	
	
	
	public String getMeikbbhz(){


		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		StringBuffer talbe=new StringBuffer();	//�������
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
		
		long meik=this.getGongysValue().getId();
		String meiktiaoj="";
		if(meik==-1){
			meiktiaoj="";
		}else{
			meiktiaoj=" and f.meikxxb_id= "+meik;
		}
		
		long yunsdw=this.getYunsdwValue().getId();
		String yunsdwtj="";
		if(yunsdw==-1){
			
		}else{
			yunsdwtj=" and c.yunsdwb_id="+yunsdw;
		}
		
		
		
		String cheph=this.getChehValue().getValue();
		Report rt = new Report(); //������
		if(cheph.equals("ȫ��")){//ú�����
			
			if(yunsdw==-1){//�ж����䵥λ�Ƿ�ѡ��,���û��ѡ��,��ú�����ҳ��,���ѡ�������䵥λ��ѯҳ��
				sbsql.setLength(0);
					sbsql.append(
							"select m.mingc,decode(grouping(m.mingc),1,'�ܼ�',max(y.mingc)) as yunsdw,\n" +
							"count(c.id) as ches,sum(c.maoz) as maoz,sum(c.piz) as piz,\n" + 
							"sum(c.koud) as koud,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
							" from chepb c ,fahb f ,meikxxb m,yunsdwb y\n" + 
							" where c.fahb_id=f.id\n" + 
							" and c.chebb_id=3\n" + 
							" and f.meikxxb_id=m.id\n" + 
							" and c.yunsdwb_id=y.id(+)\n" + 
							" and c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							" and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
							" "+meiktiaoj+"\n"+
							" group by rollup (m.mingc)\n" + 
							" order by m.mingc"
					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[1][7];
		
					ArrHeader[0]=new String[] {"��ú���","���䵥λ","����","ë��","Ƥ��","�۶�","����"};
					int ArrWidth[]=new int[] {150,100,55,80,80,55,80};
					  strFormat = new String[] { "", "","",
			    				"0.0", "0.0", "0.0", "0.0" };
					// ����
					rt.setTitle(v.getDiancmc()+"����ú��ú����_(��ú������)",ArrWidth);
					rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(5, 2, "��λ:��", Table.ALIGN_CENTER);
					rt.setBody(new Table(rs, 1, 0, 0));
					rt.body.setColFormat(strFormat);
					rt.body.setWidth(ArrWidth);
					rt.body.setFontSize(12);
					rt.body.setPageRows(200);
					rt.body.setRowHeight(30);
					rt.body.setHeaderData(ArrHeader);// ��ͷ����
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			}else{//���䵥λ���ܲ�ѯ
				sbsql.setLength(0);
				sbsql.append(

						"select decode(grouping(y.mingc),1,'�ܼ�',y.mingc) as yunsdw,\n" +
						"decode(grouping(c.cheph)+grouping(m.mingc),1,m.mingc||'С��',m.mingc) as meikmc,\n" + 
						"c.cheph, count(c.id) as ches,sum(c.maoz) as maoz,\n" + 
						"sum(c.piz) as piz,sum(c.koud) as koud,sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
						"to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as fubsj,\n" + 
						"decode(grouping(c.qingcsj),1,'',max(c.qingcjjy))  as jianjy\n" + 
						"from chepb c,fahb f,yunsdwb y,meikxxb m\n" + 
						"where c.fahb_id=f.id\n" + 
						"and c.yunsdwb_id=y.id\n" + 
						"and f.meikxxb_id=m.id\n" + 
						"and c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
						"and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
						" "+yunsdwtj+"\n" + 
						""+meiktiaoj+"\n"+
						"group by rollup(y.mingc,m.mingc,c.cheph,c.qingcsj)\n" + 
						"having not grouping(c.qingcsj)+grouping(m.mingc)+grouping(c.cheph)=1\n" + 
						"order by y.mingc,m.mingc,c.cheph,c.qingcsj"

				);
				rs=con.getResultSetList(sbsql.toString());
				
				
				String ArrHeader[][]=new String[1][10];
	
				ArrHeader[0]=new String[] {"���䵥λ","��ú���","��Ƥ��","����","ë��","Ƥ��","�۶�","����","����ʱ��","����Ա"};
				int ArrWidth[]=new int[] {120,120,75,45,60,60,60,60,120,60};
				  strFormat = new String[] { "", "","",
		    				"", "0.0", "0.0", "0.0", "0.0", "","" };
				// ����
				rt.setTitle(v.getDiancmc()+"����ú��ú����_(���䵥λ����)",ArrWidth);
				rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
				rt.setDefaultTitle(9, 2, "��λ:��", Table.ALIGN_CENTER);
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setWidth(ArrWidth);
				rt.body.setFontSize(12);
				rt.body.setPageRows(30);
				rt.body.setRowHeight(30);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				rt.body.ShowZero = true;
				
				
				
			}
			
		}else{//���Ż���
			
				sbsql.setLength(0);
				sbsql.append(
						"select max(c.cheph) as cheph,max(y.mingc) as yunsdw,max(m.mingc) as meik,sum(c.maoz) as maoz,\n" +
						"sum(c.piz) as piz,sum(c.koud) as koud,\n" + 
						"sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
						"to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as fubsj,max(c.qingcjjy) as fuby\n" + 
						" from chepb c ,fahb f ,meikxxb m,yunsdwb y\n" + 
						" where c.fahb_id=f.id\n" + 
						" and c.chebb_id=3\n" + 
						" and f.meikxxb_id=m.id\n" + 
						" and c.yunsdwb_id=y.id(+)\n" + 
						" and c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
						" and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
						" "+meiktiaoj+"\n"+
						" and c.cheph='"+cheph+"'\n" + 
						" group by rollup (c.qingcsj)\n" + 
						" order by c.qingcsj"
	
				);
				rs=con.getResultSetList(sbsql.toString());
				
				
				String ArrHeader[][]=new String[1][9];
	//			1120
				ArrHeader[0]=new String[] {"����","���䵥λ","��ú���","ë��","Ƥ��","�۶�","����","����ʱ��","����Ա"};
				int ArrWidth[]=new int[] {80,120,120,50,50,50,50,130,60};
				  strFormat = new String[] { "", "","",
		    				"0.0", "0.0", "0.0", "0.0",  "","" };
				// ����
				rt.setTitle(v.getDiancmc()+"����ú��ú����_(���Ż���)",ArrWidth);
				rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
				rt.setDefaultTitle(5, 2, "��λ:��", Table.ALIGN_CENTER);
				rt.setBody(new Table(rs, 1, 0, 0));
				
				int iLastRow=rt.body.getRows();
				int yangges=iLastRow-2;
				if(iLastRow>1){
					rt.body.setCellValue(iLastRow, 1, "");
					rt.body.setCellValue(iLastRow, 2, "�ϼ�:"+yangges+"��");
					rt.body.setCellValue(iLastRow, 3, "");
					rt.body.setCellValue(iLastRow, 9, "");
					
					
				}
				rt.body.setColFormat(strFormat);
				rt.body.setWidth(ArrWidth);
				rt.body.setFontSize(12);
				rt.body.setPageRows(200);
				rt.body.setRowHeight(30);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				rt.body.ShowZero = true;
			
			}
		
		
		
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
		//	rt.body.setRowHeight(30);
			
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
		
		long meik=this.getGongysValue().getId();
		String meiktiaoj="";
		if(meik==-1){
			meiktiaoj="";
		}else{
			meiktiaoj=" and f.meikxxb_id= "+meik;
		}
		
		
		long yunsdw=this.getYunsdwValue().getId();
		String yunsdwtj="";
		if(yunsdw==-1){
			
		}else{
			yunsdwtj=" and c.yunsdwb_id="+yunsdw;
		}
		
		String cheph=this.getChehValue().getValue();
		Report rt = new Report(); //������
	
			
		if(yunsdw==-1){
				sbsql.setLength(0);
					sbsql.append(
							"select decode(grouping(m.mingc),1,'�ܼ�',m.mingc) as meik,\n" +
							"decode(grouping(y.mingc)+grouping(m.mingc),2,'',1,'С��',y.mingc) as yunsdw ,\n" + 
							"decode(grouping(c.cheph),1,count(c.id)||'��',c.cheph) as cheph,\n" + 
							"sum(c.maoz) as maoz,sum(c.piz) as piz,\n" + 
							"sum(c.koud) as koud,sum(c.maoz-c.piz-c.koud) as jingz,max(c.meigy) as meigy,\n" + 
							"to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,max(c.qingcjjy) as qingcjjy\n" + 
							"from chepb c,yunsdwb y,fahb f,meikxxb m\n" + 
							"where c.yunsdwb_id=y.id(+)\n" + 
							"and c.fahb_id=f.id\n" + 
							"and f.meikxxb_id=m.id\n" + 
							"and c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
							" "+meiktiaoj+"\n"+
							"group by rollup (m.mingc,c.qingcsj,y.mingc,c.cheph)\n" + 
							"having not grouping(c.cheph)+grouping(c.qingcsj)=1\n" + 
							"order by m.mingc,c.qingcsj"

					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[1][10];
		//			1120
					ArrHeader[0]=new String[] {"��ú���","���䵥λ","����","ë��","Ƥ��","�۶�","����","��ú��","����ʱ��","����Ա"};
					int ArrWidth[]=new int[] {130,100,95,75,75,75,75,75,130,85};
					  strFormat = new String[] { "", "","",
			    				"0.0", "0.0", "0.0", "0.0", "", "","" };
					// ����
					rt.setTitle(v.getDiancmc()+"����ú��ú����_(��ú�����ϸ)",ArrWidth);
					rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(5, 2, "��λ:��", Table.ALIGN_CENTER);
					rt.setBody(new Table(rs, 1, 0, 0));
					rt.body.setColFormat(strFormat);
//					�趨С���еı���ɫ������
					for (int i=2;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 9);
						if((xiaoj.equals(""))){
							rt.body.setCellValue(i, 8, "");
							rt.body.setCellValue(i, 10, "");
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
					}
					
					rt.body.setFontSize(13);
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(30);
					rt.body.setHeaderData(ArrHeader);// ��ͷ����
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 5, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			
		}else{
			
			sbsql.setLength(0);
			sbsql.append(

					"select decode(grouping(y.mingc),1,'�ܼ�',y.mingc) as yunsdw,\n" +
					"decode(grouping(c.cheph)+grouping(m.mingc),1,m.mingc||'С��',m.mingc) as meikmc,\n" + 
					"c.cheph, count(c.id) as ches,sum(c.maoz) as maoz,\n" + 
					"sum(c.piz) as piz,sum(c.koud) as koud,sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
					"to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as fubsj,\n" + 
					"decode(grouping(c.qingcsj),1,'',max(c.qingcjjy))  as jianjy\n" + 
					"from chepb c,fahb f,yunsdwb y,meikxxb m\n" + 
					"where c.fahb_id=f.id\n" + 
					"and c.yunsdwb_id=y.id\n" + 
					"and f.meikxxb_id=m.id\n" + 
					"and c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
					" "+yunsdwtj+"\n" + 
					""+meiktiaoj+"\n"+
					"group by rollup(y.mingc,m.mingc,c.cheph,c.qingcsj)\n" + 
					"having not grouping(c.qingcsj)+grouping(m.mingc)+grouping(c.cheph)=1\n" + 
					"order by y.mingc,m.mingc,c.cheph,c.qingcsj"

			);
			rs=con.getResultSetList(sbsql.toString());
			
			
			String ArrHeader[][]=new String[1][10];

			ArrHeader[0]=new String[] {"���䵥λ","��ú���","��Ƥ��","����","ë��","Ƥ��","�۶�","����","����ʱ��","����Ա"};
			int ArrWidth[]=new int[] {100,100,55,45,45,45,45,45,120,50};
			  strFormat = new String[] { "", "","",
	    				"", "0.0", "0.0", "0.0", "0.0", "","" };
			// ����
			rt.setTitle(v.getDiancmc()+"����ú��ú����_(���䵥λ����)",ArrWidth);
			rt.setDefaultTitle(1, 3, "����ʱ��:"+this.getBRiq()+"��"+this.getERiq(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 2, "��λ:��", Table.ALIGN_CENTER);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
			

//			�趨С���еı���ɫ������
			for (int i=2;i<=rt.body.getRows();i++){
				String xiaoj=rt.body.getCellValue(i, 9);
				if((xiaoj.equals(""))){
					
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
			
			
			
		}
		
		
		
		
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
			visit.setDropDownBean9(null);
			visit.setProSelectionModel9(null);
			setChangbValue(null);
			setChangbModel(null);
//			visit.setDefaultTree(null);
//			setDiancmcModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
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
		
		String sql_gongys = "select id,piny || '-' ||mingc from meikxxb g order by g.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
	

//	 ���䵥λ������
	public IDropDownBean getYunsdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getYunsdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setYunsdwValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setYunsdwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getYunsdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getYunsdwModels() {
		
		String sql_yunsdw = "select y.id,y.mingc from yunsdwb y order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel8(new IDropDownModel(sql_yunsdw,"ȫ��"));
		return;
	}
	
	
	

//	 ����������
	public IDropDownBean getChehValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getChehModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setChehValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean9(Value);
	}

	public void setChehModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getChehModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getChehModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void getChehModels() {
		
		String cheh = 
			"select rownum as xuh,cheph from (\n" +
			"select  distinct c.cheph from chepb c)";

		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(cheh,"ȫ��"));
		return;
	}
}
