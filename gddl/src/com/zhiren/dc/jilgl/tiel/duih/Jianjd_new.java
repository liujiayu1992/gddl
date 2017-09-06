package com.zhiren.dc.jilgl.tiel.duih;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jianjd_new extends BasePage {
	public boolean getRaw() {
		return true;
	}

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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getSelectData();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

//	�����Ƿ�仯
	private boolean riqchange = false;
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq != null) {
			if(!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}
	
//  �жϵ糧Tree����ѡ�糧�Ƿ����ӵ糧   
    private boolean hasDianc(String id) { 
		JDBCcon con = new JDBCcon();
		boolean mingc = false;
		String sql = "select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()) {
			mingc = true;
		}
		rsl.close();
		return mingc;
	}
	
	private boolean xuh() {
		boolean editor = false;
		Visit visit = (Visit) getPage().getVisit();
		editor = "��".equals(MainGlobal.getXitxx_item("����","�ⵥ��Ű��յ�������", 
				"" + visit.getDiancxxb_id(),"��"));
		return editor;
	}
    
	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		StringBuffer sb = new StringBuffer("");
		
		_CurrentPage = 0;
		_AllPages = 0;
		
		// ��1
		long hengdid = -2;
		if(getHengdValue() != null) {
			hengdid = getHengdValue().getId();
		}
		//����ʱ��
		String time = (hengdid > 0) ? getRiq() + " " + getHengdValue().getValue()
				: "1990-01-01 23:00:00";
		
		//���ü��Ա
		sb.append("select distinct zhongcjjy from chepb where guohb_id = ").append(hengdid);
		
		rsl = con.getResultSetList(sb.toString()); 
		String lury = ""; 
		if (rsl.next()) {
			lury = rsl.getString(0); 
		}
		
		String[][] ArrHeader = new String[1][15];
		ArrHeader[0] = new String[] { Locale.xuh_chepb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, 
				Locale.cheph_chepb, Locale.pinzb_id_fahb, Locale.biaoz_chepb, Locale.maoz_chepb, Locale.piz_chepb, 
				Locale.jingz_fahb, Locale.zongkd_fahb, Locale.yuns_fahb, Locale.yingd_chepb, Locale.kuid_chepb,
				Locale.fahrq_fahb, Locale.ches_chepb };
		
		int[] ArrWidth = new int[15];
		ArrWidth = new int[] { 50, 90, 140, 45, 40, 35, 40, 40, 40, 40, 32, 32, 32, 70, 30 };
		
		if(xuh()) {
			sb.setLength(0);
			sb.append("select rownum xuh, fahdw, meikdw, cheph, pinz, biaoz, maoz, piz, jingz, zongkd, yuns, yingd, kuid, fahrq, ches from " +
					"(select rownum xuh, g.mingc fahdw, m.mingc meikdw, c.cheph,\n");
			sb.append("p.mingc pinz, c.biaoz, c.maoz, c.piz, c.maoz-c.piz-c.zongkd jingz, c.zongkd, \n");
			sb.append("c.yuns, c.yingd, c.yingd-c.yingk kuid, to_char(f.fahrq, 'yyyy-mm-dd') fahrq, c.ches\n");
			sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p\n");
			sb.append("where f.id = c.fahb_id and f.gongysb_id = g.id\n");
			sb.append("and f.meikxxb_id = m.id and f.pinzb_id = p.id\n");
			sb.append("and c.guohb_id = ").append(hengdid).append(" \n");
			sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
			sb.append(" order by c.xuh desc)");
		} else {
			sb.setLength(0);
			sb.append("select c.xuh, g.mingc fahdw, m.mingc meikdw, c.cheph,\n");
			sb.append("p.mingc pinz, c.biaoz, c.maoz, c.piz, c.maoz-c.piz-c.zongkd jingz, c.zongkd, \n");
			sb.append("c.yuns, c.yingd, c.yingd-c.yingk kuid, to_char(f.fahrq,'yyyy-mm-dd') fahrq, c.ches\n");
			sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p\n");
			sb.append("where f.id = c.fahb_id and f.gongysb_id = g.id\n");
			sb.append("and f.meikxxb_id = m.id and f.pinzb_id = p.id\n");
			sb.append("and c.guohb_id = ").append(hengdid).append(" \n");
			sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
			sb.append(" order by c.xuh");
		}
		
	    ResultSet rs = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    
	    rsl = con.getResultSetList(sb.toString());
	    if (!rsl.next()) {
	    	return null;
	    }
	    
	    // ��2����
		hengdid = -1;
		if (getHengdValue() != null) {
			hengdid = getHengdValue().getId();
		}
	    
	    ResultSetList hz;
		sb.setLength(0);
		sb.append("select bianm, fahdw, meikdw, pinz, ches, biaoz, jingz, zongkd, yuns, yingd, kuid, yingk, yingk, faz, faz from \n");
		sb.append("(select rownum r, s.* from (select decode(y.bianm, null, '�ϼ�', y.bianm) bianm, g.mingc fahdw, m.mingc meikdw, p.mingc pinz, \n");
		sb.append("sum(f.ches) ches, sum(f.biaoz) biaoz, sum(f.jingz) jingz, sum(zongkd) zongkd, \n");
		sb.append("sum(f.yuns) yuns, sum(f.yingd) yingd, sum(f.yingd-f.yingk) kuid, \n");
		sb.append("sum(f.yingk) yingk, c.mingc faz \n");
		sb.append("from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, caiyb y, \n");
		sb.append("(select distinct fahb_id from chepb where  guohb_id = ");
		sb.append(hengdid).append(") cp \n");
		sb.append("where f.id = cp.fahb_id and f.gongysb_id = g.id and f.pinzb_id = p.id \n");
		sb.append("and f.meikxxb_id = m.id and f.zhilb_id = y.zhilb_id and f.faz_id = c.id \n");
		sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
		sb.append("group by rollup(y.bianm, g.mingc, m.mingc, p.mingc, c.mingc) \n");
		sb.append("having (GROUPING(c.mingc) = 0 OR GROUPING(y.bianm) = 1 )) s) \n");
		
		hz = con.getResultSetList(sb.toString());
	    
		// ��3����
		String str = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ�ٶ�ͳ����", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
		ResultSetList rsldata = null;
		if (str.equals("��")) {
			
			String sql = 
				"select case\n" +
				"         when to_char(temp.jib) = '1' then '1Km����'\n" + 
				"         when to_char(temp.jib) = '2' then '1Km��15Km'\n" + 
				"         when to_char(temp.jib) = '3' then '15Km����'\n" + 
				"         else '' end as fanw, count(temp.ches) ches from (\n" + 
				"select case\n" + 
				"         when cp.ches < 1.0 then 1\n" + 
				"         when cp.ches >= 1.0 and cp.ches <= 15.0 then 2\n" + 
				"         when cp.ches > 15.0 then 3\n" + 
				"         else 0.0 end as jib,\n" + 
				"       cp.ches ches\n" + 
				"  from chepb cp, fahb fh, vwdianc d\n" + 
				" where cp.fahb_id = fh.id\n" + 
				"   and fh.yunsfsb_id = 1\n" + 
				"   and fh.diancxxb_id = d.id\n" + 
				"   and d.fgsid = " + ((Visit) this.getPage().getVisit()).getDiancxxb_id() + "\n" + 
				"   and cp.guohb_id = " + hengdid + " order by jib) temp\n" + 
				"group by temp.jib";
			
			rsldata = con.getResultSetList(sql);
		}
		
		Report rt = new Report();
		Table tb = new Table(rs, 1, hz.getRows() + 4, 3);
		rt.setBody(tb);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.setTitle("�������ؼ�¼", ArrWidth);
		
		rt.setDefaultTitle(1, 3, "�Ʊ�λ��"
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 6, "����ʱ�䣺" + time, Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 4, "��λ���֡�����ǧ��/ʱ", Table.ALIGN_RIGHT);
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"
				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 3, "���ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 2, "���Ա��" + lury, Table.ALIGN_LEFT);
		
//		rt.body.setPageRows(21);
		
		rt.body.merge(1, 2, tb.getRows() - hz.getRows() - 4, 3);
		
		for (int i = 1; i <= 15; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		// ����2��3������
		String ArrHeaderHZ[][] = new String[1][15];
		ArrHeaderHZ[0] = new String[] { Locale.caiybm_caiyb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb, 
				Locale.ches_fahb, Locale.biaoz_fahb, Locale.jingz_fahb, Locale.zongkd_fahb, Locale.yuns_fahb, Locale.yingd_fahb, 
				Locale.kuid_fahb, Locale.yingk_fahb, Locale.yingk_fahb, Locale.faz_id_fahb, Locale.faz_id_fahb };
		
		int iPageHz = rt.body.getRows() - (hz.getRows() + 4) + 2;
		for (int i = 1; i <= 15; i++) {
			rt.body.setCellValue(iPageHz, i, ArrHeaderHZ[0][i - 1]);
		}
		
		for (int i = 1; i <= hz.getRows(); i++) {
			hz.next();
			for (int j = 1; j <= hz.getColumnCount(); j++){
				rt.body.setCellValue(iPageHz + i, j, hz.getString(j - 1));
			}
		}
		
//		21	25
		for (int i = rsl.getRows() + 3; i < tb.getRows() - 1; i++) {
			rt.body.mergeCell(i, 12, i, 13);
			rt.body.mergeCell(i, 14, i, 15);
		}
		
		rt.body.setCellValue(tb.getRows(), 1, "1Km����");
		rt.body.setCellValue(tb.getRows(), 4, "1Km��15Km");
		rt.body.setCellValue(tb.getRows(), 9, "15Km����");
		
		while(rsldata.next()) {
			if(rsldata.getString("fanw").equals("1Km����")) {
				rt.body.setCellValue(tb.getRows(), 3, rsldata.getString("ches"));
			} else if(rsldata.getString("fanw").equals("1Km��15Km")) {
				rt.body.setCellValue(tb.getRows(), 7, rsldata.getString("ches"));
			} else {
				rt.body.setCellValue(tb.getRows(), 12, rsldata.getString("ches"));
			}
		}
		
		rt.body.mergeCell(tb.getRows(), 1, tb.getRows(), 2);
		rt.body.mergeCell(tb.getRows(), 4, tb.getRows(), 6);
		rt.body.mergeCell(tb.getRows(), 7, tb.getRows(), 8);
		rt.body.mergeCell(tb.getRows(), 9, tb.getRows(), 11);
		rt.body.mergeCell(tb.getRows(), 12, tb.getRows(), 15);
		
		rt.body.mergeCell(rsl.getRows() + 2, 1, rsl.getRows() + 2, 15);
		rt.body.mergeCell(tb.getRows() - 1, 1, tb.getRows() - 1, 15);
		
		//���ö��ͷ��λ��
		int[][] intArrPos = new int[2][2];
		//��һ�����
		intArrPos[0][0] = 1;
		intArrPos[0][1] = 1;
		
		//�ڶ������
		intArrPos[1][0] = iPageHz;
		intArrPos[1][1] = iPageHz;
		
		rt.body.setTableTitlePos(intArrPos);
		int iPages = rt.body.getPages();
		
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = iPages;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		rsl.close();
		hz.close();
		rsldata.close();
		con.Close();
		return rt.getAllPagesHtml();
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
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
		hengdcb.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex = index}");
		tb1.addField(hengdcb);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString3(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			setRiq(DateUtil.FormatDate(new Date()));
			setHengdValue(null);
			setHengdModel(null);
		}
		if(riqchange) {
			riqchange = false;
			setHengdValue(null);
			setHengdModel(null);
		}
		getToolbars();
		blnIsBegin = true;
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		((DateField)getToolbar().getItem("guohrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}
	
	// �糧Tree
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
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	�ⵥ������
	public IDropDownBean getHengdValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1() == null) {
			if(getHengdModel().getOptionCount() > 0) {
				setHengdValue((IDropDownBean)getHengdModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	
	public void setHengdValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getHengdModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1() == null) {
			setHengdModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	
	public void setHengdModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setHengdModels() {
		StringBuffer sb = new StringBuffer();
		String diancid = "" ;
		if (getTreeid_dc() != null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if (hasDianc(getTreeid_dc())) {
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id = " + getTreeid_dc();
			}
		}
		sb.append("select g.id, to_char(guohsj,'hh24:mi:ss') guohsj from guohb g, chepb c, fahb f, vwdianc d \n");
		sb.append("where c.guohb_id = g.id and c.fahb_id = f.id and f.diancxxb_id = d.id\n and to_char(guohsj, 'yyyy-mm-dd') = '");
		sb.append(getRiq()).append("'");
		sb.append(diancid);
		sb.append(" order by guohsj desc");
		
		setHengdModel(new IDropDownModel(sb.toString()));
	}
}
