package com.zhiren.dc.gdxw.laimtj;

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

public class Laimtj extends BasePage {

	
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
		dfb.Binding("BRIQ", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
//	    
		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox Kouj = new ComboBox();
		Kouj.setTransform("KOUD");
		Kouj.setWidth(150);
		Kouj.setListeners("select:function(){document.forms[0].submit();}");
		Kouj.setEditable(true);
		tb1.addField(Kouj);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		
//	    
		tb1.addText(new ToolbarText("��ӡѡ��"));
		ComboBox dayin = new ComboBox();
		dayin.setTransform("MEIK");
		dayin.setWidth(100);
		dayin.setListeners("select:function(){document.forms[0].submit();}");
		dayin.setEditable(true);
		tb1.addField(dayin);
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
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
		if(this.getKoudValue().getValue().equals("ͳ��Ƭ��")){
			return GetPianq();
		}else if(this.getKoudValue().getValue().equals("ͳ�ƾ���")){
			return getJingk();
		}else if(this.getKoudValue().getValue().equals("ͳ��ú��")){
			return getMeikdq();
		}else if(this.getKoudValue().getValue().equals("ͳ��ú��,����")){
			return getMeikdq_meik();
		}else if(this.getKoudValue().getValue().equals("ͳ��Ƭ��,����")){
			return GetPianq_meik();
		}else if(this.getKoudValue().getValue().equals("ͳ��Ƭ��,ú��")){
			return GetPianq_meikdq();
		}else if(this.getKoudValue().getValue().equals("ͳ��Ƭ��(δ���)")){
			return getPianq_weish();
		}else{
			return "�޴˱���";
		}
			
		
	}
	
	
	
	//ͳ�ƾ���
	public String getJingk(){
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
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		

			sbsql.append(

					"select decode(mk.mingc,null,'�ܼ�',mk.mingc) as meikdwmc,\n" +
					"decode(sum(aa.ches),null,'0',sum(aa.ches)) as ches1,\n" + 
					"decode(sum(aa.jingz),null,'0.0',sum(aa.jingz)) as jingz1,\n" + 
					"decode(sum(bb.ches),null,'0',sum(bb.ches)) as ches2,\n" + 
					"decode(sum(bb.jingz),null,'0.0',sum(bb.jingz)) as jingz2,\n" + 
					"decode(sum(cc.ches),null,'0',sum(cc.ches)) as ches3,\n" + 
					"decode(sum(cc.jingz),null,'0.0',sum(cc.jingz)) as jingz3\n" + 
					"from\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"group by  (f.meikxxb_id)) aa,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=first_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=last_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) bb,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=getyearfirstdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=getyearlastdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) cc,meikxxb mk,meikdqglb gl,meikdqb_xw dq,meikpqb pq\n" + 
					"\n" + 
					"where cc.meikxxb_id=mk.id\n" + 
					"and cc.meikxxb_id=bb.meikxxb_id(+)\n" + 
					"and cc.meikxxb_id=aa.meikxxb_id(+)\n" + 
					"and mk.id=gl.meikxxb_id\n" + 
					"and gl.meikdqb_xw_id=dq.id\n" + 
					"and dq.meikpqb_id=pq.id\n"+
					""+mk_str+""	+
					"group by rollup (mk.mingc)"

);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[2][7];
//				1120
				ArrHeader[0]=new String[] {"����","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�"};
				ArrHeader[1]=new String[] {"����","����","����","����","����","����","����"};
				int ArrWidth[]=new int[] {130,60,80,60,80,70,100};
				  strFormat = new String[] { "", "","0.0", "","0.0","" , "0.0"};
				// ����
				rt.setTitle("�볧ú����ͳ��̨��",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 2, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(30);
				
			
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals("�ܼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	//ͳ��ú��
	public String getMeikdq(){
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
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}

			sbsql.append(

					"select decode(dq.meikdqmc,null,'�ܼ�',dq.meikdqmc) as meikdwmc,\n" +
					"decode(sum(aa.ches),null,'0',sum(aa.ches)) as ches1,\n" + 
					"decode(sum(aa.jingz),null,'0.0',sum(aa.jingz)) as jingz1,\n" + 
					"decode(sum(bb.ches),null,'0',sum(bb.ches)) as ches2,\n" + 
					"decode(sum(bb.jingz),null,'0.0',sum(bb.jingz)) as jingz2,\n" + 
					"decode(sum(cc.ches),null,'0',sum(cc.ches)) as ches3,\n" + 
					"decode(sum(cc.jingz),null,'0.0',sum(cc.jingz)) as jingz3\n" + 
					"from\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"group by  (f.meikxxb_id)) aa,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=first_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=last_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) bb,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=getyearfirstdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=getyearlastdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) cc,meikxxb mk,meikdqglb gl,meikdqb_xw dq,meikpqb pq\n" + 
					"\n" + 
					"where cc.meikxxb_id=mk.id\n" + 
					"and cc.meikxxb_id=bb.meikxxb_id(+)\n" + 
					"and cc.meikxxb_id=aa.meikxxb_id(+)\n" + 
					"and mk.id=gl.meikxxb_id\n" + 
					"and gl.meikdqb_xw_id=dq.id\n" + 
					"and dq.meikpqb_id=pq.id\n"+
					""+mk_str+""	+
					"group by rollup (dq.meikdqmc)"

);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[2][7];
//				1120
				ArrHeader[0]=new String[] {"����","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�"};
				ArrHeader[1]=new String[] {"����","����","����","����","����","����","����"};
				int ArrWidth[]=new int[] {130,60,80,60,80,70,100};
				  strFormat = new String[] { "", "","0.0", "","0.0","" , "0.0"};
				// ����
				rt.setTitle("�볧ú����ͳ��̨��",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 2, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(30);
				
			
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals("�ܼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	
	//ú�����,ú��
	public String getMeikdq_meik(){
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
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}

			sbsql.append(

					"select decode(dq.meikdqmc,null,'�ܼ�',dq.meikdqmc) meikdq,\n" +
					"decode(mk.mingc,null,decode(dq.meikdqmc,null,null,'�ϼ�'),mk.mingc) as meik,\n"+
					"decode(sum(aa.ches),null,'0',sum(aa.ches)) as ches1,\n" + 
					"decode(sum(aa.jingz),null,'0.0',sum(aa.jingz)) as jingz1,\n" + 
					"decode(sum(bb.ches),null,'0',sum(bb.ches)) as ches2,\n" + 
					"decode(sum(bb.jingz),null,'0.0',sum(bb.jingz)) as jingz2,\n" + 
					"decode(sum(cc.ches),null,'0',sum(cc.ches)) as ches3,\n" + 
					"decode(sum(cc.jingz),null,'0.0',sum(cc.jingz)) as jingz3\n" + 
					"from\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"group by  (f.meikxxb_id)) aa,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=first_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=last_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) bb,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=getyearfirstdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=getyearlastdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) cc,meikxxb mk,meikdqb_xw dq,meikdqglb gl,meikpqb pq\n" + 
					"\n" + 
					"where cc.meikxxb_id=mk.id\n" + 
					"and cc.meikxxb_id=bb.meikxxb_id(+)\n" + 
					"and cc.meikxxb_id=aa.meikxxb_id(+)\n" + 
					"and mk.id=gl.meikxxb_id\n"+
					"and gl.meikdqb_xw_id=dq.id\n"+
					"and dq.meikpqb_id=pq.id\n"+
					""+mk_str+""	+
					"group by rollup (dq.meikdqmc,mk.mingc)"

);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[2][8];
//				1120
				ArrHeader[0]=new String[] {"ú�����","����","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�"};
				ArrHeader[1]=new String[] {"ú�����","����","����","����","����","����","����","����"};
				int ArrWidth[]=new int[] {130,130,60,80,60,80,70,100};
				  strFormat = new String[] { "","", "","0.0", "","0.0","" , "0.0"};
				// ����
				rt.setTitle("�볧ú����ͳ��̨��",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 2, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(30);
				
			
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("�ϼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}

	public String GetPianq(){

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
		
		
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		
		


			sbsql.append(


					"select decode(pq.mingc,null,'�ܼ�',pq.mingc) as meikdwmc,\n" +
					"decode(sum(aa.ches),null,'0',sum(aa.ches)) as ches1,\n" + 
					"decode(sum(aa.jingz),null,'0.0',sum(aa.jingz)) as jingz1,\n" + 
					"decode(sum(bb.ches),null,'0',sum(bb.ches)) as ches2,\n" + 
					"decode(sum(bb.jingz),null,'0.0',sum(bb.jingz)) as jingz2,\n" + 
					"decode(sum(cc.ches),null,'0',sum(cc.ches)) as ches3,\n" + 
					"decode(sum(cc.jingz),null,'0.0',sum(cc.jingz)) as jingz3\n" + 
					"from\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"group by  (f.meikxxb_id)) aa,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=first_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=last_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) bb,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=getyearfirstdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=getyearlastdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) cc,meikxxb mk,meikdqglb gl,meikdqb_xw dq,meikpqb pq\n" + 
					"\n" + 
					"where cc.meikxxb_id=mk.id\n" + 
					"and cc.meikxxb_id=bb.meikxxb_id(+)\n" + 
					"and cc.meikxxb_id=aa.meikxxb_id(+)\n" + 
					"and mk.id=gl.meikxxb_id\n" + 
					"and gl.meikdqb_xw_id=dq.id\n" + 
					"and dq.meikpqb_id=pq.id\n"+
					""+mk_str+""	+
					"group by rollup (pq.mingc)"


);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[2][7];
//				1120
				ArrHeader[0]=new String[] {"����","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�"};
				ArrHeader[1]=new String[] {"����","����","����","����","����","����","����"};
				int ArrWidth[]=new int[] {130,60,80,60,80,70,100};
				  strFormat = new String[] { "", "","0.0", "","0.0","" , "0.0"};
				// ����
				rt.setTitle("�볧ú����ͳ��̨��",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 2, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(30);
				
			
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals("�ܼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
	//ú��Ƭ��,ú��
	public String GetPianq_meik(){

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
		
		
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		
		


			sbsql.append(


					"select decode(pq.mingc,null,'�ܼ�',pq.mingc) as meikdwmc,\n" +
					"decode(mk.mingc,null,decode(pq.mingc,null,null,'�ϼ�'),mk.mingc) as meik,\n"+
					"decode(sum(aa.ches),null,'0',sum(aa.ches)) as ches1,\n" + 
					"decode(sum(aa.jingz),null,'0.0',sum(aa.jingz)) as jingz1,\n" + 
					"decode(sum(bb.ches),null,'0',sum(bb.ches)) as ches2,\n" + 
					"decode(sum(bb.jingz),null,'0.0',sum(bb.jingz)) as jingz2,\n" + 
					"decode(sum(cc.ches),null,'0',sum(cc.ches)) as ches3,\n" + 
					"decode(sum(cc.jingz),null,'0.0',sum(cc.jingz)) as jingz3\n" + 
					"from\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"group by  (f.meikxxb_id)) aa,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=first_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=last_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) bb,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=getyearfirstdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=getyearlastdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) cc,meikxxb mk,meikdqglb gl,meikdqb_xw dq,meikpqb pq\n" + 
					"\n" + 
					"where cc.meikxxb_id=mk.id\n" + 
					"and cc.meikxxb_id=bb.meikxxb_id(+)\n" + 
					"and cc.meikxxb_id=aa.meikxxb_id(+)\n" + 
					"and mk.id=gl.meikxxb_id\n" + 
					"and gl.meikdqb_xw_id=dq.id\n" + 
					"and dq.meikpqb_id=pq.id\n"+
					""+mk_str+""	+
					"group by rollup (pq.mingc,mk.mingc)"


);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[2][8];
//				1120
				ArrHeader[0]=new String[] {"Ƭ������","ú������","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�"};
				ArrHeader[1]=new String[] {"Ƭ������","ú������","����","����","����","����","����","����"};
				int ArrWidth[]=new int[] {130,130,60,80,60,80,70,100};
				  strFormat = new String[] {"", "", "","0.0", "","0.0","" , "0.0"};
				// ����
				rt.setTitle("�볧ú����ͳ��̨��",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 2, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(30);
				
			
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("�ϼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
//	ú��Ƭ��,ú�����
	public String GetPianq_meikdq(){

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
		
		
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		
		


			sbsql.append(


					"select decode(pq.mingc,null,'�ܼ�',pq.mingc) as meikdwmc,\n" +
					"decode(dq.meikdqmc,null,decode(pq.mingc,null,null,'�ϼ�'),dq.meikdqmc) as meik,\n"+
					"decode(sum(aa.ches),null,'0',sum(aa.ches)) as ches1,\n" + 
					"decode(sum(aa.jingz),null,'0.0',sum(aa.jingz)) as jingz1,\n" + 
					"decode(sum(bb.ches),null,'0',sum(bb.ches)) as ches2,\n" + 
					"decode(sum(bb.jingz),null,'0.0',sum(bb.jingz)) as jingz2,\n" + 
					"decode(sum(cc.ches),null,'0',sum(cc.ches)) as ches3,\n" + 
					"decode(sum(cc.jingz),null,'0.0',sum(cc.jingz)) as jingz3\n" + 
					"from\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"group by  (f.meikxxb_id)) aa,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=first_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=last_day(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) bb,\n" + 
					"\n" + 
					"(select f.meikxxb_id ,\n" + 
					" count(c.id) as ches,sum(c.maoz-c.piz-c.koud) as jingz\n" + 
					" from chepb c,fahb f\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.daohrq>=getyearfirstdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"and f.daohrq<=getyearlastdate(to_date('"+kais+"','yyyy-mm-dd'))\n" + 
					"group by  (f.meikxxb_id)) cc,meikxxb mk,meikdqglb gl,meikdqb_xw dq,meikpqb pq\n" + 
					"\n" + 
					"where cc.meikxxb_id=mk.id\n" + 
					"and cc.meikxxb_id=bb.meikxxb_id(+)\n" + 
					"and cc.meikxxb_id=aa.meikxxb_id(+)\n" + 
					"and mk.id=gl.meikxxb_id\n" + 
					"and gl.meikdqb_xw_id=dq.id\n" + 
					"and dq.meikpqb_id=pq.id\n"+
					""+mk_str+""	+
					"group by rollup (pq.mingc,dq.meikdqmc)"


);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[2][8];
//				1120
				ArrHeader[0]=new String[] {"Ƭ������","ú�����","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�","�����ۼ�"};
				ArrHeader[1]=new String[] {"Ƭ������","ú�����","����","����","����","����","����","����"};
				int ArrWidth[]=new int[] {130,130,60,80,60,80,70,100};
				  strFormat = new String[] {"", "", "","0.0", "","0.0","" , "0.0"};
				// ����
				rt.setTitle("�볧ú����ͳ��̨��",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 2, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(30);
				
			
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("�ϼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
	
	
	
	//ͳ��Ƭ��(δ���)
	public String getPianq_weish(){
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
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}

			sbsql.append(


					"select decode(pq.mingc,null,'�ܼ�',pq.mingc) as pianq,\n" +
					"decode(dq.meikdqmc,null,decode(pq.mingc,null,null,'�ϼ�'),dq.meikdqmc) as dqmc,\n" + 
					"count(*) as ches,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.koud) as koud,\n" + 
					"sum(c.maoz-c.piz-c.koud) as jing\n" + 
					"from chepbtmp c ,meikxxb mk,meikdqglb gl,meikdqb_xw dq,meikpqb pq\n" + 
					"where c.daohrq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and c.meikdwmc=mk.mingc\n" + 
					"and mk.id=gl.meikxxb_id\n" + 
					"and dq.id=gl.meikdqb_xw_id\n" + 
					"and dq.meikpqb_id=pq.id\n" + 
					""+mk_str+"\n"+
					"group by rollup (pq.mingc,dq.meikdqmc)\n" + 
					"order by pq.mingc,dq.meikdqmc"

);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][7];
//				1120
				ArrHeader[0]=new String[] {"Ƭ������","ú������","����","ë��","Ƥ��","�۶�","����"};

				int ArrWidth[]=new int[] {80,80,80,80,80,80,80};
				  strFormat = new String[] { "", "","", "","","" , ""};
				// ����
				rt.setTitle("�볧ú����ͳ��̨��",ArrWidth);
				rt.setDefaultTitle(1, 3, "��ú����:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(30);
				
			
				
//				�趨С���еı���ɫ������
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("�ϼ�"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// ��ͷ����
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
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
			
			setBRiq( DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);

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
		}
		
//		}else{
//			this.setMeikValue(null);
//			this.setMeikModel(null);
//		}
		getSelectData();
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
	

	
	
	

	
	
	
	

	
	
	
//	 ú��������
	public IDropDownBean getMeikValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getMeikModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setMeikValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setMeikModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getMeikModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getMeikModels() {
		
		
		
		
		String sql="select id,mingc from fenzb";
		
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql,"ȫ��"));
		return;
	}
	
	
//	 ͳ�ƿھ�״̬������
	public IDropDownBean getKoudValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getKoudModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setKoudValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setKoudModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getKoudModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getKoudModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getKoudModels() {
		
		List list=new ArrayList();
		//list.add(new IDropDownBean("1","ȫ��"));
		list.add(new IDropDownBean("1","ͳ��Ƭ��"));
		list.add(new IDropDownBean("2","ͳ��Ƭ��(δ���)"));
		list.add(new IDropDownBean("3","ͳ��ú��"));
		list.add(new IDropDownBean("4","ͳ�ƾ���"));
		list.add(new IDropDownBean("5","ͳ��ú��,����"));
		list.add(new IDropDownBean("6","ͳ��Ƭ��,����"));
		list.add(new IDropDownBean("7","ͳ��Ƭ��,ú��"));
		
		
		
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	
}
