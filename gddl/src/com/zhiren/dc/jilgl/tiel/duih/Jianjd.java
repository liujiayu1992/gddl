package com.zhiren.dc.jilgl.tiel.duih;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
import com.zhiren.report.Document;
import com.zhiren.report.Paragraph;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ����:tzf
 * ʱ��:2009-5-4
 * �޸����ݣ�ҳ�濪ʼ����ʱ��visit�е�ĳЩ ����ֵ û����գ����� �� ������ﵥ ��ҳ�� ���� �𳵼�ﵥ
 		  ʱ��ҳ����س���.
 */


/*
 * ����:���ش�
 * ʱ��:2009-12-5
 * �޸����ݣ�������ͷ�糧�����󣬺ⵥ��Ű��յ����ν�������.
 *  �������ϵͳ����
    INSERT INTO xitxxb VALUES(
		       getnewid(diancxxb_id),
		       1,diancxxb_id,'','��','�ⵥ��Ű��յ�������','����',1,'ʹ��'
		       )
 * ���ڴ�ӡ��ʱ�򷢻����ڻ��У��ӿ������ڡ�
 * ����ʱ���������յ������С�
 */
/*
 * ����:���ܱ�
 * ʱ��:2010-3-25 20:10:35
 * �޸����ݣ��������ǵ糧�����󣬺ⵥ��Ű��յ����ν�������.�糧����¼���ȷ�������,����
 *          ��order by xuh���������desc
 * 
 * 
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-21 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Jianjd extends BasePage {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}

	private int paperStyle;// ����ֽ������

	private void paperStyle() {
		// �������ܣ�����ֽ�������趨ֽ�ſ��
		// �����߼����ȴ�ϵͳ��Ϣ���ж�ȡֽ�����͵�ֵ��zhi��Ȼ��ֵ��paperStlye
		// ��������
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='ֽ������' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
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
//	�����Ƿ�仯
	private boolean riqchange = false;
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq != null){
			if(!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
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
	
//	�ⵥ������
	public IDropDownBean getHengdValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getHengdModel().getOptionCount()>0) {
				setHengdValue((IDropDownBean)getHengdModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setHengdValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getHengdModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setHengdModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setHengdModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
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
	
	public void setHengdModels() {
//		Visit visit = (Visit)this.getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
//		sb.append("select zhi from xitxxb where mingc='���ⵥ��ӡʱ�䷶Χ' and leib='����' and diancxxb_id="+visit.getDiancxxb_id());
//		ResultSetList rsl = con.getResultSetList(sb.toString());
//		String tians = "30";
//		if(rsl.next()) {
//			tians = rsl.getString("zhi");
//		}
//		sb.delete(0, sb.length());
		String diancid = "" ;
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id ="+ getTreeid_dc();
			}
		}
		sb.append("select g.id,to_char(guohsj,'hh24:mi:ss') guohsj from guohb g,chepb c,fahb f,vwdianc d \n");
		sb.append("where c.guohb_id=g.id and c.fahb_id=f.id and f.diancxxb_id=d.id\n and to_char(guohsj,'yyyy-mm-dd') = '");
		sb.append(getRiq()).append("'");
		sb.append(diancid);
		sb.append(" order by guohsj desc");
		
		setHengdModel(new IDropDownModel(sb.toString()));
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
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		if(visit.isFencb()) {
//			tb1.addText(new ToolbarText("����:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
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
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("���ʱ��:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		ComboBox hengdcb = new ComboBox();
		hengdcb.setTransform("HengdSelect");
		hengdcb.setWidth(130);
		hengdcb.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
		tb1.addField(hengdcb);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	/*
	 * ���ñ�ü
	 */
	private void setTableHeader(Paragraph ph, String date, String unit) {
		//ph.addText("<pre>");
		ph.addText("<table align=center width='685'��border='0' cellspacing='0' cellpadding='0'>");
		ph.addText("<tr height=50><td colspan=3></td></tr>");
		ph.addText("<tr><td align='center' colspan=3><font style='font-size:20pt'>");
		ph.addText("<B>�������ؼ�¼</B></font></td></tr>");
		ph.addText("<tr height=10><td colspan=3></td></tr>");
		ph.addText("<tr><td align='left'><font size=2 >�Ʊ�λ:"
						+ ((Visit)this.getPage().getVisit()).getDiancqc()
						+ "</font></td><td align='center'><font size=2 >"
						+ date
						+"</font></td><td align='right'><font size=2 >");
		ph.addText(unit);
		ph.addText("</font></td></tr></table>");
	}
	
	/*
	 * ���ñ�β
	 */
	private void setTableFooter(Paragraph ph,String jianjy) {
		ph.addText("<table valign=top align=center width='685'��border='0' cellspacing='0' cellpadding='0'>");
		ph.addText("<tr><td align='left' width=40%><font size=2>��ӡ���ڣ�"
						+ DateUtil.FormatDate(new Date()));
		ph.addText("</font></td><td align='left' width=30%><font size=2>");
		ph.addText("���ˣ�</font></td><td align='left' width=30%><font size=2>");
		ph.addText("���Ա��"+jianjy+"</font></td></tr></table>");
	}
	
	private boolean xuh() {
		boolean editor = false;
		Visit visit = (Visit) getPage().getVisit();
		editor = "��".equals(MainGlobal.getXitxx_item("����","�ⵥ��Ű��յ�������", 
				"" + visit.getDiancxxb_id(),"��"));
		return editor;
	}
	
	
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		Document _Document = new Document();
		long hengdid = -2;
		if(getHengdValue()!=null) {
			hengdid = getHengdValue().getId();
		}
		//����ʱ��
		String time = (hengdid > 0) ? getRiq()+" "+getHengdValue().getValue()
				: "1990-01-01 23:00:00";
		//���ü��Ա
		sb.append("select distinct zhongcjjy from chepb where guohb_id = ")
			.append(hengdid);
		ResultSetList rsl = con.getResultSetList(sb.toString()); 
		String lury = ""; 
		if (rsl.next()) {
			lury = rsl.getString(0); 
		}
		// ������ϸ��SQL
		if(xuh()){
			sb.setLength(0);
			sb.append("select rownum xuh,fahdw,meikdw,cheph,pinz,biaoz,maoz,piz,jingz,zongkd,yuns,yingd,kuid,fahrq,ches from " +
					"(select rownum xuh,g.mingc fahdw, m.mingc meikdw, c.cheph,\n");
			sb.append("p.mingc pinz, c.biaoz, c.maoz, c.piz, c.maoz-c.piz-c.zongkd jingz, c.zongkd, \n");
			sb.append("c.yuns, c.yingd, c.yingd-c.yingk kuid, to_char(f.fahrq,'yyyy-mm-dd') fahrq, c.ches\n");
			sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p\n");
			sb.append("where f.id = c.fahb_id and f.gongysb_id = g.id\n");
			sb.append("and f.meikxxb_id = m.id and f.pinzb_id = p.id\n");
			sb.append("and c.guohb_id =").append(hengdid).append(" \n");
			sb.append("and f.yunsfsb_id=").append(SysConstant.YUNSFS_HUOY);
			sb.append(" order by c.xuh desc)");
		}else{
		sb.setLength(0);
		sb.append("select  c.xuh,g.mingc fahdw, m.mingc meikdw, c.cheph,\n");
		sb.append("p.mingc pinz, c.biaoz, c.maoz, c.piz, c.maoz-c.piz-c.zongkd jingz, c.zongkd, \n");
		sb.append("c.yuns, c.yingd, c.yingd-c.yingk kuid, to_char(f.fahrq,'yyyy-mm-dd') fahrq, c.ches\n");
		sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p\n");
		sb.append("where f.id = c.fahb_id and f.gongysb_id = g.id\n");
		sb.append("and f.meikxxb_id = m.id and f.pinzb_id = p.id\n");
		sb.append("and c.guohb_id =").append(hengdid).append(" \n");
		sb.append("and f.yunsfsb_id=").append(SysConstant.YUNSFS_HUOY);
		sb.append(" order by c.xuh");}
		ResultSet rs = con.getResultSet(sb,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		int EndPageRow = 0;
		Report rt = new Report();
		try {
			if (rs.next()) {
				Table tb = new Table(rs, 1, 0, 2);
				String[][] ArrHeader = new String[1][15];
				ArrHeader[0] = new String[] { Locale.xuh_chepb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.cheph_chepb, Locale.pinzb_id_fahb,
						Locale.biaoz_chepb, Locale.maoz_chepb, Locale.piz_chepb, Locale.jingz_fahb, Locale.zongkd_fahb, Locale.yuns_fahb, Locale.yingd_chepb, Locale.kuid_chepb,
						Locale.fahrq_fahb, Locale.ches_chepb };
				int[] ArrWidth = new int[15];
				ArrWidth = new int[] { 32, 90, 140, 45, 40, 35, 40, 32, 40, 40, 32, 32, 32, 70, 30 };
				rt.getArrWidth(ArrWidth, this.paperStyle);
				tb.setWidth(ArrWidth);
				tb.setHeaderData(ArrHeader);
				tb.setPageRows(rt.PAPER_ROWS);
				tb.merge(1,2,tb.getRows(),3,false);
				tb.setColFormat(7,"0.000");
				tb.setColFormat(9,"0.000");
				tb.setColFormat(10,"0.000");
				tb.setColFormat(11,"0.000");
				tb.setColFormat(12,"0.000");
				tb.setColAlign(1, Table.ALIGN_CENTER);

				tb.setColAlign(5, Table.ALIGN_CENTER);
//				�����Ҷ���
				tb.setColAlign(6, Table.ALIGN_RIGHT);
				tb.setColAlign(7, Table.ALIGN_RIGHT);
				tb.setColAlign(8, Table.ALIGN_RIGHT);
				tb.setColAlign(9, Table.ALIGN_RIGHT);
				tb.setColAlign(10, Table.ALIGN_RIGHT);
				tb.setColAlign(11, Table.ALIGN_RIGHT);
				tb.setColAlign(12, Table.ALIGN_RIGHT);
				tb.setColAlign(13, Table.ALIGN_RIGHT);
				tb.setColAlign(15, Table.ALIGN_RIGHT);
				//���ñ�������
				tb.setRowCells(1, Table.PER_FONTSIZE, 10);
				tb.setCells(2, 2, tb.getRows(), 3, Table.PER_ALIGN,
						Table.ALIGN_CENTER);
				tb.setCells(2, 2, tb.getRows(), 3, Table.PER_VALIGN,
						Table.VALIGN_TOP);
				tb.setRowHeight(21);
				EndPageRow = (tb.getRows()-1)%40+3;
				setAllPages(tb.getPages());
				for(int iPage=1; iPage <= tb.getPages(); iPage++){
					String display = "";
					String feny = "";
					if(iPage != 1){
						display = "style='display:none'";
						feny = "<span style=\"display:none\"><p style='page-break-after: always'>&nbsp;</p></span>";
					}
					Paragraph ph = new Paragraph();
					ph.addText(feny+"<span id='reportpage"+iPage+"' "+display+" >");
					
					setTableHeader(ph,"����ʱ�䣺"+time,"��λ���֡�����ǧ��/ʱ");
					ph.addText("<center>"+tb.getHtml(iPage));
					if(iPage == tb.getPages()){
						if(EndPageRow != 0 && 41-EndPageRow>0){
							Table total = getTotalTable(true,41-EndPageRow);
							if(total!=null){
								ph.addText("<table><tr height=10><td></td></tr></table>");
								total.setPageRows(41-EndPageRow);
								ph.addText(total.getHtml(1));
							}
						}else{
							EndPageRow = 0;
						}
					}
					ph.addText("</center>");
					setTableFooter(ph,lury);
					ph.addText("</span>");
					_Document.addParagraph(ph);
				}
				
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		Table total = null;
		if(EndPageRow == 0){
			total = getTotalTable(false,0);
		}else{
			total = getTotalTable(false,41-EndPageRow);
		}
		if(total != null){
			total.setPageRows(rt.PAPER_ROWS);
			for(int iPage=1; iPage <= total.getPages(); iPage++){
				Paragraph ph = new Paragraph();
				ph.addText("<span style=\"display:none\"><p style='page-break-after: always'>&nbsp;</p></span><span id='reportpage"+(getAllPages()+iPage)+"' style='display:none'>");
				setTableHeader(ph,"����ʱ�䣺"+time,"��λ���֡�����ǧ��/ʱ");
				ph.addText("<center>"+total.getHtml(iPage)+"</center>");
				setTableFooter(ph,lury);
				ph.addText("</span>");
				_Document.addParagraph(ph);
			}
		}
		con.Close();
		_Document.setTableWidth(0);
		if(_Document.paragraphs.size() > 0) {
			setCurrentPage(1);
			setAllPages(_Document.paragraphs.size());
		}
		return _Document.getHtml();// ph;
	}
	
	private Table getTotalTable(boolean less,int rownum){
		JDBCcon con = new JDBCcon();
		long hengdid = -1;
		if(getHengdValue()!=null) {
			hengdid = getHengdValue().getId();
		}
		Table tb1 = null;
		String strrow ="";
		if(less){
			strrow = " where r < "+rownum;
		}else{
			strrow = " where r >="+rownum;
		}
		ResultSet hz;
		StringBuffer sb = new StringBuffer();
		sb.append("select bianm,fahdw,meikdw,pinz,ches,biaoz,jingz,zongkd,yuns,yingd,kuid,yingk,faz from \n");
		sb.append("(select rownum r, s.* from (select decode(y.bianm,null,'�ϼ�',y.bianm) bianm, g.mingc fahdw, m.mingc meikdw, p.mingc pinz, \n");
		sb.append("sum(f.ches) ches, sum(f.biaoz) biaoz, sum(f.jingz) jingz, sum(zongkd) zongkd, \n");
		sb.append("sum(f.yuns) yuns, sum(f.yingd) yingd, sum(f.yingd-f.yingk) kuid, \n");
		sb.append("sum(f.yingk) yingk, c.mingc faz \n");
		sb.append("from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, caiyb y, \n");
		sb.append("(select distinct fahb_id from chepb where  guohb_id = ");
		sb.append(hengdid).append(") cp \n");
		sb.append("where f.id = cp.fahb_id and f.gongysb_id = g.id and f.pinzb_id = p.id \n");
		sb.append("and f.meikxxb_id = m.id and f.zhilb_id = y.zhilb_id and f.faz_id = c.id \n");
		sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
		sb.append("group by rollup(y.bianm,g.mingc,m.mingc,p.mingc,c.mingc) \n");
		sb.append("having (GROUPING(c.mingc) = 0 OR GROUPING(y.bianm) =1 )) s) \n");
		sb.append(strrow);
		
		hz = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		try {
			if(hz.next()){
				tb1 = new Table(hz, 1, 0, 1);
				String ArrHeaderHZ[][] = new String[1][13];
				ArrHeaderHZ[0] = new String[] { Locale.caiybm_caiyb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb, Locale.ches_fahb,
						Locale.biaoz_fahb, Locale.jingz_fahb, Locale.zongkd_fahb, Locale.yuns_fahb, Locale.yingd_fahb, Locale.kuid_fahb, Locale.yingk_fahb, Locale.faz_id_fahb };
				int[] ArrWidthHz = new int[13];
				ArrWidthHz = new int[] { 55, 95, 140, 40, 40, 40, 50, 40, 40, 40, 40, 50,
						55 };
				tb1.setWidth(ArrWidthHz);
				tb1.setHeaderData(ArrHeaderHZ);
				tb1.setColFormat(7,"0.000");
				tb1.setColFormat(9,"0.000");
				tb1.setColFormat(10,"0.000");
				tb1.setColFormat(11,"0.000");
				tb1.setColAlign(1, Table.ALIGN_CENTER);
				tb1.setColAlign(2, Table.ALIGN_CENTER);
				tb1.setColAlign(3, Table.ALIGN_CENTER);
				tb1.setColAlign(4, Table.ALIGN_CENTER);
				tb1.setColAlign(5, Table.ALIGN_RIGHT);
				tb1.setColAlign(6, Table.ALIGN_RIGHT);
				tb1.setColAlign(7, Table.ALIGN_RIGHT);
				tb1.setColAlign(8, Table.ALIGN_RIGHT);
				tb1.setColAlign(9, Table.ALIGN_RIGHT);
				tb1.setColAlign(10, Table.ALIGN_RIGHT);
				tb1.setColAlign(11, Table.ALIGN_RIGHT);
				tb1.setColAlign(11, Table.ALIGN_RIGHT);
				tb1.setColAlign(13, Table.ALIGN_CENTER);
//				���ñ�������
				tb1.setRowCells(1, Table.PER_FONTSIZE, 10);
				tb1.setRowHeight(21);
				tb1.ShowZero = false;
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return tb1;
	}
//	������ʹ�õķ���
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
		((DateField)getToolbar().getItem("guohrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		paperStyle();//��ʼ�����ڱ����A4��ʽֵ��zhi��
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString3(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			visit.setboolean1(false);
			visit.setString1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			setHengdValue(null);
			setHengdModel(null);
			getSelectData();
		}
		if(riqchange){
			riqchange = false;
			setHengdValue(null);
			setHengdModel(null);
			setTbmsg(null);
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
//			setHengdModels();
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
	
}
