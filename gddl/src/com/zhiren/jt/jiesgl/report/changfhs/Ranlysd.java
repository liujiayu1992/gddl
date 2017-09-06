package com.zhiren.jt.jiesgl.report.changfhs;

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

public class Ranlysd extends BasePage {
	/*private static final int RPTTYPE_TZ_ALL = 1;
	private static final int RPTTYPE_TZ_HUOY = 2;
	private static final int RPTTYPE_TZ_QIY = 3;
	*/
	/**
	 * ����:wzb
	 * ʱ��:2009-6-23 
	 * ����:��ɽ�糧ȼ�����յ�
	 */
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb�ж�Ӧ�Ĺؼ���
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
    


	public StringBuffer getBaseSql() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gongys = "";
		String diancid = "" ;
		
		//2009-5-8 22:47:09 ��ʱû�д���һ������
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id ="+ getTreeid_dc();
			}
			
		}
		con.Close();
		
		
		
		//�������
		long jiesbm=this.getChephValue().getId();
		
		
		 
		
		
		
		
		StringBuffer sb = new StringBuffer();
		String sql1 = "";
	
		sql1 = 
			"select decode(grouping(a.meik),1,'�ϼ�',a.meik) as meik,decode(grouping(a.meik),1,'�ϼ�',a.meik) as meik,decode(grouping(a.meik),1,'�ϼ�',a.meik) as meik," +
			"max(a.faz) as faz,max(a.jij) as jij,sum(a.kous) as kous,\n" +
			"round(sum(a.stad*a.jiessl)/sum(a.jiessl),2) as stad,round(sum(a.mt*a.jiessl)/sum(a.jiessl),2) as mt,\n" + 
			"round(sum(a.mad*a.jiessl)/sum(a.jiessl),2) as mad,round(sum(a.vdaf*a.jiessl)/sum(a.jiessl),2) as vdaf,\n" + 
			"round(sum(a.aad*a.jiessl)/sum(a.jiessl),2) as aad,round(sum(a.qnetar*a.jiessl)/sum(a.jiessl),2) as qnetar,sum(a.ches) as ches,\n" + 
			"sum(a.piaoz) as piaoz,sum(a.jiessl) as jiessl,round(sum(a.danj*a.jiessl)/sum(a.jiessl),2) as danj,\n" + 
			"sum(a.jine) as jine\n" + 
			"from\n" + 
			"      (select pc.xuh,max(m.mingc) as meik,(select mingc from chezxxb where id=pc.faz_id)  as faz,max(js.hetj) as jij,\n" + 
			"      max(pc.kous) as kous,max(pc.stad) as stad,max(pc.mt) as mt,max(pc.mad) as mad,\n" + 
			"      max(pc.vdaf) as vdaf, max(pc.aad) as aad, max(pc.qnetar) as qnetar,\n" + 
			"      max(pc.ches) as ches, max(pc.gongfsl) as piaoz,max(pc.jiessl) as jiessl,\n" + 
			"      max(pc.jiesdj) as danj,max(pc.jiashj) as jine\n" + 
			"      from danpcjsmxb pc ,jiesb js,meikxxb m\n" + 
			"      where pc.jiesdid=js.id and js.meikxxb_id=m.id\n" + 
			"      and js.id="+jiesbm+" and pc.leib=1 group by  (pc.xuh,pc.faz_id))  a\n" + 
			"group by rollup (a.xuh,a.meik)\n" + 
			"having not (grouping(a.meik)+grouping(a.xuh)=1 )\n" + 
			"order by a.xuh";


		sb.append(sql1);
		
		
		
		return sb;
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

		tb1.addText(new ToolbarText("��������:"));
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
		tb1.addText(new ToolbarText("��ͬ��λ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(150);
		//CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
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
		
		//�������
		tb1.addText(new ToolbarText("������:"));
		ComboBox CB = new ComboBox();
		CB.setTransform("CHEPH");
		CB.setWidth(150);
		CB.setListeners("select:function(){document.Form0.submit();}");
		CB.setEditable(true);
		tb1.addField(CB);
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
		//leix.setEditable(true);
		tb1.addField(leix);
		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
		if (getJieslbValue().getValue().equals("���յ�")){
			return getYansd();
		}else if(getJieslbValue().getValue().equals("�ܸ���")){
			return getJufd();
		}else if(getJieslbValue().getValue().equals("��Ʊ��")){
			return getKaipd();
		}else if(getJieslbValue().getValue().equals("���ⵥ")){
			return getGuohd();
		}else{
			return "�޴˱���";
		}
	}
	
	
	public String getGuohd(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		StringBuffer talbe=new StringBuffer();	//�������
		ResultSetList rs=null;
		String faz="";
		String kuangb="";
		String hetbh="";
		String famsj="";
		int p=0;

			sbsql.append(
					"select min(c.mingc) as faz,min(m.mingc) as kuangb,\n" +
					"min(h.hetbh) as heth,to_char(min(f.fahrq),'yyyy-MM-dd') as famsj\n" + 
					"from fahb f,meikxxb m ,chezxxb c,hetb h,jiesb j\n" + 
					"where f.meikxxb_id=m.id(+)\n" + 
					"and f.faz_id=c.id(+)\n" + 
					"and f.hetb_id=h.id(+)\n" + 
					"and f.jiesb_id="+this.getChephValue().getId()+""
			);
			rs=con.getResultSetList(sbsql.toString());
			if(rs.next()){
				
				faz=rs.getString("faz");
				kuangb=rs.getString("kuangb");
				hetbh=rs.getString("heth");
				famsj=rs.getString("famsj");
			}
		

			
			sbsql.setLength(0);
			sbsql.append(
					"select decode(c.zhongcsj,null,'�ϼ�',to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss')) as zhongcsj,\n" +
					"decode(c.cheph,null,count(c.cheph),c.cheph) as cheph,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz) as jingz,\n" + 
					"sum(c.biaoz) as biaoz,sum(c.koud+c.kous+c.kouz+c.koum) as kous,sum(c.maoz-c.piz-c.koud-c.kous-c.kouz-c.koum) as jisml\n" + 
					" from fahb f,chepb c\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.jiesb_id="+this.getChephValue().getId()+"\n" + 
					"group by rollup (c.zhongcsj,c.cheph)\n" + 
					"having not (grouping(c.cheph)=1 and grouping(c.zhongcsj)=0)\n" + 
					"order by c.zhongcsj,min(c.xuh)"

			);
			rs=con.getResultSetList(sbsql.toString());
			
			Report rt = new Report(); //������
			String ArrHeader[][]=new String[1][8];
//			1120
			ArrHeader[0]=new String[] {"��úʱ��","����","ë��","Ƥ��","����","Ʊ��","�۶ֿ�ˮ","����ú��"};
			int ArrWidth[]=new int[] {125,70,70,70,70,70,70,70};
			// ����
			rt.setTitle("��ú���ⵥ",ArrWidth);
			rt.setDefaultTitleLeft("��վ:"+faz+"<br>��ú��λ:"+kuangb+"",2);
			rt.setDefaultTitleRight("��ͬ���:"+hetbh+"<br>��úʱ��:"+famsj, 2);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setWidth(ArrWidth);
//			rt.body.setPageRows(20);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����

			rt.body.ShowZero = true;
			
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
//			�����
			talbe.append(rt.getAllPagesHtml(p));
			p++;
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			
	     	return rt.getAllPagesHtml();// ph;
	
	}
	
	public String getYansd(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
//		�������
		long jiesbm1=this.getChephValue().getId();
		
		String huiz="select h.hetbh,h.gongfdwmc,j.jiesrq, j.ches ,j.jiessl,j.hansdj,j.hansmk,0 as shenf,yf.guotyf,yf.kuangqyf ,yf.kuidjfyf as kuidyf,yf.kuidjfzf as juf,\n" +
			"(j.hansmk+nvl(guotyf,0)+nvl(kuangqyf,0)-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)) as yingfje\n" + 
			"from jiesb j,jiesyfb yf,hetb h\n" + 
			" where  j.bianm=yf.bianm(+) and j.hetb_id=h.id(+)\n" + 
			" and j.id="+jiesbm1+"";
		String ches="";
		String jiessl="";
		String danj="";
		String meik="";
		String shenf="";
		String yunf="";
		String yunzf="";
		String kuidyf="";
		String juf="";
		String zongje="";
		String hetdw="";
		String heth="";
		String jiesrq="";
		ResultSetList HZ = con.getResultSetList(huiz);
		while(HZ.next()){
			hetdw=HZ.getString("gongfdwmc");
			heth=HZ.getString("hetbh");
			jiesrq=HZ.getDateString("jiesrq");
			ches=String.valueOf(HZ.getLong("ches"));
			jiessl=String.valueOf(HZ.getDouble("jiessl"));
			danj=String.valueOf(HZ.getDouble("hansdj"));
			meik=String.valueOf(HZ.getDouble("hansmk"));
			shenf=String.valueOf(HZ.getDouble("shenf"));
			yunf=String.valueOf(HZ.getDouble("guotyf"));
			yunzf=String.valueOf(HZ.getDouble("kuangqyf"));
			kuidyf=String.valueOf(HZ.getDouble("kuidyf"));
			juf=String.valueOf(HZ.getDouble("juf"));
			zongje=String.valueOf(HZ.getDouble("yingfje"));
			
		}
		
		ResultSetList rs = null;
		//String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;



        	rs = rstmp;
        	
        	String ArrHeader[][]=new String[4][17];
        	ArrHeader[0]=new String[] {"����","����ú��","����","ú��","ʲ��","ʲ��","�˷�","�˷�","�˷�","�������ӷ�","�������ӷ�","�۳�/�����˷�","�۳�/�����˷�","�ܸ����","�ܸ����","Ӧ���ܽ��","Ӧ���ܽ��"};
        	ArrHeader[1]=new String[] {ches,   jiessl,danj,meik,shenf,shenf,yunf,yunf,yunf,yunzf,yunzf,kuidyf,kuidyf,juf,juf,zongje,zongje};
        	ArrHeader[2]=new String[] {"��ú��λ","��ú��λ","��ú��λ","��վ","����","��ˮ","ú��","ú��","ú��","ú��","ú��","ú��","����","Ʊ��","������","����","���"};
        	ArrHeader[3]=new String[] {"��ú��λ","��ú��λ","��ú��λ","��վ","����","��ˮ","Stad","Mt","Mad","Vdaf","Aad","Qnetar","����","Ʊ��","������","����","���"};
			
        	
    		 ArrWidth = new int[] {50, 65, 50, 65, 45, 45, 40, 40, 40 ,40, 40, 45, 40, 45, 50, 55, 65};
    
    		rt.setTitle(getRptTitle(), ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		
    		//strFormat = new String[] { "", "", "", "", "0.00", "0.00", "0.00","0.00", "0.00"};
    		rt.setTitle(getRptTitle(), ArrWidth);
    		
		

//
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "��ͬ��λ��" + hetdw,Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4, "��������:"+jiesrq,Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 4, "��ͬ��:"+heth,Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 4, "���:"+this.getChephValue().getValue(), Table.ALIGN_RIGHT);

//
		rt.setBody(new Table(rs, 4, 0, 3));
//		����16��
		int iLastRow=rt.body.getRows();
		int iNewlastRow=16;
		if  (iLastRow<16){
			rt.body.AddTableRow(16-iLastRow);
			//�ϼ��Ƶ����
			for (int i=1 ;i<=rt.body.getCols(); i++){
				rt.body.setCellValue(iNewlastRow-1, i, rt.body.getCellValue(iLastRow,i));
				rt.body.setCellValue(iLastRow, i, "");
			}
			rt.body.setCellValue(iNewlastRow, 1, "��ע");
			rt.body.setCellValue(iNewlastRow-1, 4, "");//ȥ���ϼ��еķ�վ
			rt.body.merge(16, 2, 16, 17);
			
		}
		//�趨�ж���
		for(int a=1;a<=rt.body.getCols();a++){
			rt.body.setColAlign(a, Table.ALIGN_CENTER);
		}
	
		
		
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		//rt.body.mergeFixedCols();
		//rt.body.mergeFixedRow();
		//�趨��ͷ�ϲ�
		rt.body.merge(1, 5, 1, 6);
		rt.body.merge(1, 7, 1, 9);
		rt.body.merge(1, 10, 1, 11);
		rt.body.merge(1, 12, 1, 13);
		rt.body.merge(1, 14, 1, 15);
		rt.body.merge(1, 16, 1, 17);
		
		rt.body.merge(2, 5, 2, 6);
		rt.body.merge(2, 7, 2, 9);
		rt.body.merge(2, 10, 2, 11);
		rt.body.merge(2, 12, 2, 13);
		rt.body.merge(2, 14, 2, 15);
		rt.body.merge(2, 16, 2, 17);
		
		rt.body.merge(3, 1, 4, 3);
		rt.body.merge(3, 4, 4, 4);
		rt.body.merge(3, 5, 4, 5);
		rt.body.merge(3, 6, 4, 6);
		rt.body.merge(3, 7, 3, 12);
		rt.body.merge(3, 13, 4, 13);
		rt.body.merge(3, 14, 4, 14);
		rt.body.merge(3, 15, 4, 15);
		rt.body.merge(3, 16, 4, 16);
		rt.body.merge(3, 17, 4, 17);
		
		//�趨ǰ���дӵ�5�п�ʼ�ϲ�
		for (int i=5;i<=rt.body.getRows()-1;i++){
			rt.body.merge(i, 1, i, 3);
			
		}
		
		
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(2, 3, "���ܣ�",Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "���ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "����Ա��", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;

		rt.body.ShowZero = true;
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		
     	return rt.getAllPagesHtml();// ph;
	}
	
	
	
	public String getJufd(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		long jiesbm=this.getChephValue().getId();
		String sqlJufd ="select j.jiesrq,j.bianm,g.quanc,j.ches,j.jiessl,(j.hansmk+nvl(yf.guotyf,0))  as tuosje,\n" +
			" (yf.kuidjfyf+yf.kuidjfzf) as jufje,(j.hansmk+nvl(yf.guotyf,0)-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)) as chengfje,j.beiz\n" + 
			" from jiesb j,gongysb g ,jiesyfb yf\n" + 
			" where j.gongysb_id=g.id(+)\n" + 
			" and j.bianm=yf.bianm(+)\n" + 
			" and j.id="+jiesbm;

		ResultSetList rs_Jufd = con.getResultSetList(sqlJufd);
		String jiesrq = "";
		String bianm = "";
		String gongys = "";
		String ches = "";
		String jiessl = "";
		String tuosje = "";
		String jufje="";
		String chengfje="";
		String beiz="";

		while(rs_Jufd.next()){
			 jiesrq = rs_Jufd.getDateString("jiesrq");
			 bianm = rs_Jufd.getString("bianm");
			 gongys = rs_Jufd.getString("quanc");
			 ches = String.valueOf(rs_Jufd.getLong("ches"));
			 jiessl = String.valueOf(rs_Jufd.getDouble("jiessl"));
			 tuosje = String.valueOf(rs_Jufd.getDouble("tuosje"));
			 jufje=String.valueOf(rs_Jufd.getDouble("jufje"));
			 chengfje=String.valueOf(rs_Jufd.getDouble("chengfje"));
			 beiz=rs_Jufd.getString("beiz");
		}
		
		
		
		

		String[][] CAIY=new String[][]{
			{"��ͬ��λ",gongys,gongys,gongys},
			{"����",ches,"ú��",jiessl},
			{"���ս��",tuosje,"�ܸ����",jufje},
			{"","","�и����",chengfje},
			{"�ܸ�����","�ܸ�����","�ܸ�����","�ܸ�����"},
			{beiz,beiz,beiz,beiz}};
		rs_Jufd.close();
		
		String[][] ArrHeader = new String[6][4];
		int i=0;
		for(int j=0;j<CAIY.length;j++){
			ArrHeader[i++]=CAIY[j];
		}
		int[] ArrWidth = new int[] { 150,150,150,150 };
		
		Table bt=new Table(6,4);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][4];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		
		rt.setTitle(v.getDiancqc()+"�ܸ�������", ArrWidth);
		rt.setDefaultTitle(1,2,"��������:"+jiesrq,Table.ALIGN_LEFT);
		rt.setDefaultTitle(3,2,"���:"+bianm,Table.ALIGN_RIGHT);
		
		//�ϲ�
		rt.body.merge(1, 2, 1, 4);
		rt.body.merge(4, 1, 4, 2);
		rt.body.merge(5, 1, 5, 4);
		rt.body.merge(6, 1, 6, 4);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setRowHeight(6, 60);
		
		
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(3, 2, "���£�", Table.ALIGN_LEFT);

		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		rt.body.ShowZero = true;
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public String getKaipd(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		long jiesbm_kp=this.getChephValue().getId();
		
		/*��Ϊ�ڽ��㵥ҳ��,����ܸ��˷Ѻ;ܸ��ӷ�,ҳ���в�����js����,û��Ӱ��ú��,˰��,��˰�ϼƵ�ֵ.�����ڸ��糧
		 ����Ʊ��ʱ��Ҫ�ٳ��ܸ��˷Ѻ;ܸ��ӷ�,���¼���˰��,����˰ú��,����˰����,��˰�ϼƵ�*/
		String sql_Kaip="select j.jiesrq,j.bianm,g.quanc,'' as yingsmc,\n" +
			"j.jiessl,\n" + 
			"round_new(((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))- round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2))/j.jiessl,6) buhsdj,\n" + 
			"((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))- round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2))  as buhsmk,\n" + 
			"j.shuil,\n" + 
			"round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2) as shuik,\n" + 
			"(nvl(yf.kuangqyf,0)+nvl(yf.kuangqzf,0)) as huopzf,\n" + 
			"nvl(yf.guotyf,0) as guotyf,\n" + 
			"(j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)) as jiashj\n" + 
			" from jiesb j,gongysb g ,jiesyfb yf\n" + 
			" where j.gongysb_id=g.id(+)\n" + 
			" and j.bianm=yf.bianm(+)\n" + 
			" and j.id="+jiesbm_kp;


		ResultSetList rs_Kp = con.getResultSetList(sql_Kaip);
		String jiesrq = "";
		String bianm = "";
		String gongys = "";
		String yingsmc = "";
		String jiessl = "";
		String buhsdj = "";
		String buhsmk="";
		String shuil="";
		String shuik="";
		String huopzf="";
		String yunf="";
		String jiashj="";

		while(rs_Kp.next()){
			 jiesrq = rs_Kp.getDateString("jiesrq");
			 bianm = rs_Kp.getString("bianm");
			 gongys = rs_Kp.getString("quanc");
			 yingsmc = rs_Kp.getString("yingsmc");
			 jiessl = String.valueOf(rs_Kp.getDouble("jiessl"));
			 buhsdj = String.valueOf(rs_Kp.getDouble("buhsdj"));
			 buhsmk=String.valueOf(rs_Kp.getDouble("buhsmk"));
			 shuil=String.valueOf(rs_Kp.getDouble("shuil"));
			 shuik=String.valueOf(rs_Kp.getDouble("shuik"));
			 huopzf=String.valueOf(rs_Kp.getDouble("huopzf"));
			 yunf=String.valueOf(rs_Kp.getDouble("guotyf"));
			 jiashj=String.valueOf(rs_Kp.getDouble("jiashj"));
		}
		
		
		
		

		String[][] KAIPD=new String[][]{
			{"���㵥��","���㵥��","��ͬ��λ","��ͬ��λ","��ͬ��λ","��ͬ��λ","��������"},
			{bianm,bianm,gongys,gongys,gongys,gongys,jiesrq},
			{"Ӧ˰����","��λ","����","����","���","˰��","˰��"},
			{yingsmc,"��",jiessl,buhsdj,buhsmk,shuil,shuik},
			{"��Ʊ�ӷ�",huopzf,huopzf,"�˷�",yunf,"��˰�ϼ�",jiashj}};
		rs_Kp.close();
		
		String[][] ArrHeader = new String[5][7];
		int i=0;
		for(int j=0;j<KAIPD.length;j++){
			ArrHeader[i++]=KAIPD[j];
		}
		int[] ArrWidth = new int[] { 80,80,80,80,90,80,90 };
		
		Table bt=new Table(5,7);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][7];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		
		rt.setTitle(v.getDiancqc()+"��Ʊ֪ͨ��", ArrWidth);
//		rt.setDefaultTitle(1,2,"��������:"+getBRiq(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(3,2,"���:000009-09",Table.ALIGN_RIGHT);
		
		//�ϲ�
		rt.body.merge(1, 1, 1, 2);
		rt.body.merge(1, 3, 1, 6);
		rt.body.merge(2, 1, 2, 2);
		rt.body.merge(2, 3, 2, 6);
		rt.body.merge(5, 2, 5, 3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setRowHeight(30);
		

		
		
		rt.createFooter(1, ArrWidth);
		//rt.setDefautlFooter(3, 2, "���£�", Table.ALIGN_LEFT);

		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		rt.body.ShowZero = true;
		con.Close();
		return rt.getAllPagesHtml();
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
			this.getChephModels();
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
	
	
//	 ����
	public IDropDownBean getChephValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getChephModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setChephValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setChephModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getChephModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getChephModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getChephModels() {
		
		String jiesrq ="";
		Date addRiq=DateUtil.AddDate(DateUtil.getDate(this.getERiq()), 1, DateUtil.AddType_intDay);
		jiesrq = " and  j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq())
		+" and  j.jiesrq<"+DateUtil.FormatOracleDate(addRiq);
		
		String gongys_tj="";
		
		long gongys=this.getGongysValue().getId();
		if(gongys!=-1){
			gongys_tj=" and j.gongysb_id="+gongys;
		}
		String sql = 
			"select j.id,j.bianm\n" +
			"from jiesb j ,gongysb g \n" + 
			"where j.gongysb_id=g.id\n" + 
			""+jiesrq+""+
			""+gongys_tj+""+
			" order by j.bianm";

		
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql,"������"));
		return;
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
		list.add(new IDropDownBean(1, "���յ�"));
		list.add(new IDropDownBean(2, "�ܸ���"));
		list.add(new IDropDownBean(3, "��Ʊ��"));
		list.add(new IDropDownBean(4, "���ⵥ"));
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
		
		String sql_gongys = "select g.id,g.mingc from gongysb g order by g.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,""));
		return;
	}
	
}
