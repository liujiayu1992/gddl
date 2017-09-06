package com.zhiren.dc.gdxw.meiccm;

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

public class Meicrz extends BasePage {

	/*����:���ܱ�
	 *����:2010-7-11 14:08:39
	 *����:�糧7��ú��ÿ���볡ú����ֵԤ��
	 * 
	 * 
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

    

	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ú����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.forms[0].submit();}");
		//dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		//dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.forms[0].submit();}");
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("��ú�ص�:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(100);
		
		CB_GONGYS.setListeners("select:function(){document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
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
		
		
		
		

		
		//�����һ�����ƾ���ʾ�糧��,������ǾͲ���ʾ�糧
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("�糧:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
//		������ϸ
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		ComboBox leix = new ComboBox();
		leix.setTransform("JIESLB");
		leix.setWidth(120);
		leix.setListeners("select:function(){document.forms[0].submit();}");
		tb1.addField(leix);
		
//		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
//		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.addItem(rbtn);
//		
		tb1.setWidth("bodyWidth");
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
	
			
			if (getJieslbValue().getValue().equals("����")){
				return getHuiz();
			}else if(getJieslbValue().getValue().equals("��ϸ")){
				return getYansd();
			}else if(getJieslbValue().getValue().equals("���ջ���δ����б�")){
				return getHuayWeish();
			}else{
				return "�޴˱���";
			}
		
	}
	
	
	//���ջ���δ����б�
	public String getHuayWeish(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
	
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //������
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		String tiaoj="";
		long meik=this.getGongysValue().getId();
		if(meik==-1){
			tiaoj="";
		}else{
			tiaoj=" and c.meicb_id="+this.getGongysValue().getId()+"";
		}
		
		
		
		

			sbsql.append(

					"select decode(mc.mingc,null,'ȫ���ϼ�',mc.mingc) as meicmc,\n" +
					"decode(c.meikdwmc,null,decode(mc.mingc,null,null,'С��'),c.meikdwmc) as meikdwmc,\n" + 
					"sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.qnet_ar*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as qnet_ar,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.qnet_ar*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud)*1000/4.1816,0))  as qnet_dak,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.mt*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),1))  as mt,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.mad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as mad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.aad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as aad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.vad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as vad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.stad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as stad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.had*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as had\n" + 
					"from chepbtmp c,zhilb zl,meicb mc\n" + 
					"where c.zhilb_id=zl.id(+)\n" + 
					"and c.meicb_id=mc.id\n" + 
					"and c.qingcsj is not null\n" + 
					"and c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					""+tiaoj+"\n" + 
					" and zl.qnet_ar is null \n"+
					"group by rollup (mc.mingc,c.meikdwmc)\n" + 
					"order by grouping(mc.mingc),max(mc.xuh),c.meikdwmc");

			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][11];
//				1120
				ArrHeader[0]=new String[] {"��úλ��","ú��","����","��ֵ<br>(�׽�)","��ֵ<br>(��)","ȫˮ","��ˮ","�ҷ�","�ӷ���","���","��"};
				int ArrWidth[]=new int[] {120,120,90,60,60,60,60,60,60,60,60};
				  strFormat = new String[] { "", "","0.0","0.00","","0.00","0.00","0.00","0.00","0.00" ,"0.00"};
				// ����
				rt.setTitle("��úλ����ֵ��ѯ",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais+" �� "+jies, Table.ALIGN_LEFT);
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(5000);
				
			
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("")||xiaoj.equals("С��"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				
				
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
	
	public String getYansd(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//�������
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //������
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		String tiaoj="";
		long meik=this.getGongysValue().getId();
		if(meik==-1){
			tiaoj="";
		}else{
			tiaoj=" and c.meicb_id="+this.getGongysValue().getId()+"";
		}
		
		
		
		

			sbsql.append(

					"select decode(mc.mingc,null,'ȫ���ϼ�',mc.mingc) as meicmc,\n" +
					"decode(c.meikdwmc,null,decode(mc.mingc,null,null,'С��'),c.meikdwmc) as meikdwmc,\n" + 
					"sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.qnet_ar*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as qnet_ar,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.qnet_ar*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud)*1000/4.1816,0))  as qnet_dak,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.mt*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),1))  as mt,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.mad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as mad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.aad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as aad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.vad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as vad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.stad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as stad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.had*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as had\n" + 
					"from chepbtmp c,zhilb zl,meicb mc\n" + 
					"where c.zhilb_id=zl.id(+)\n" + 
					"and c.meicb_id=mc.id\n" + 
					"and c.qingcsj is not null\n" + 
					"and c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					""+tiaoj+"\n" + 
					" and zl.qnet_ar is not null\n"+
					"group by rollup (mc.mingc,c.meikdwmc)\n" + 
					"order by grouping(mc.mingc),mc.mingc desc,max(mc.xuh),c.meikdwmc");

			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][11];
//				1120
				ArrHeader[0]=new String[] {"��úλ��","ú��","����","��ֵ<br>(�׽�)","��ֵ<br>(��)","ȫˮ","��ˮ","�ҷ�","�ӷ���","���","��"};
				int ArrWidth[]=new int[] {120,120,90,60,60,60,60,60,60,60,60};
				  strFormat = new String[] { "", "","0.0","0.00","","0.00","0.00","0.00","0.00","0.00" ,"0.00"};
				// ����
				rt.setTitle("��úλ����ֵ��ѯ",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais+" �� "+jies, Table.ALIGN_LEFT);
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(5000);
				
			
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("")||xiaoj.equals("С��"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				
				
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	
	

	public String getHuiz(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//�������
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //������
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		String tiaoj="";
		long meik=this.getGongysValue().getId();
		if(meik==-1){
			tiaoj="";
		}else{
			tiaoj=" and c.meicb_id="+this.getGongysValue().getId()+"";
		}
		
		
		
		

			sbsql.append(

					"select decode(mc.mingc,null,'ȫ���ϼ�',mc.mingc) as meicmc,\n" +
					"sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.qnet_ar*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as qnet_ar,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.qnet_ar*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud)*1000/4.1816,0))  as qnet_dak,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.mt*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),1))  as mt,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.mad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as mad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.aad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as aad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.vad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as vad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.stad*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as stad,\n" + 
					"decode(sum(c.maoz-c.piz-c.koud),0,0,round_new(sum(zl.had*(c.maoz-c.piz-c.koud))/sum(c.maoz-c.piz-c.koud),2))  as had\n" + 
					"from chepbtmp c,zhilb zl,meicb mc\n" + 
					"where c.zhilb_id=zl.id(+)\n" + 
					"and c.meicb_id=mc.id\n" + 
					"and c.qingcsj is not null\n" + 
					"and c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					""+tiaoj+"\n" + 
					" and zl.qnet_ar is not null\n"+
					"group by rollup (mc.mingc)\n" + 
					"order by grouping(mc.mingc),max(mc.xuh)");

			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][10];
//				1120
				ArrHeader[0]=new String[] {"��úλ��","����","��ֵ<br>(�׽�)","��ֵ<br>(��)","ȫˮ","��ˮ","�ҷ�","�ӷ���","���","��"};
				int ArrWidth[]=new int[] {120,90,60,60,60,60,60,60,60,60};
				  strFormat = new String[] { "", "0.0","0.00","","0.00","0.00","0.00","0.00","0.00" ,"0.00"};
				// ����
				rt.setTitle("��úλ����ֵ��ѯ",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais+" �� "+jies, Table.ALIGN_LEFT);
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(5000);
				
			
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals("")||xiaoj.equals("ȫ���ϼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				
				rt.body.setFontSize(12);
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(30);
				con.Close();
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
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay)));
			setERiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay)));
			
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			

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
	


	
//	 ����/��ϸ
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
		list.add(new IDropDownBean(3, "���ջ���δ����б�"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
	

	
	
	
	
//	 ��úλ��������
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
		
		String sql_gongys = "select id,mingc from meicb order by xuh";
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
}
