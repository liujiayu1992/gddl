package com.zhiren.dc.hesgl.yufkcx;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-21 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Yufkcx extends BasePage {
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	// ***************������Ϣ��******************//
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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// �õ���λȫ��
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}

	private String RT_DR16 = "Yufkcx";

	private String RT_DR03 = "Yufktjb";


	private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);


		if (mstrReportName.equals(RT_DR16)) {

			return getYufkb();
		} else if (mstrReportName.equals(RT_DR03)) {

			return getYufktjb();
		} else {
			return "";
		}
	}

	private String getYufkb() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String ruzzt = "";
		if (getRuzztValue().getId() == 0) {
			ruzzt = "and ruzrq is null";
		} else {
			ruzzt = "and ruzrq is not null";
		}
		if(getRuzztValue().getId() == 0){
			if(getGongysValue().getId()==-1){
				sbsql.append("select to_char(y.riq,'yyyy-mm-dd'),decode(y.bianh,null,null,getHtmlAlert('"
						+ MainGlobal.getHomeContext(this)
						+ "','YufkReport','lx',y.id,y.bianh)) as bianh,g.mingc,y.jine,y.yue,s.quanc,y.kaihyh,y.zhangh,to_char(y.ruzrq,'yyyy-mm-dd'),y.beiz from yufkb y,gongysb g,shoukdw s\n");
		        sbsql.append("where y.gongysb_id=g.id and y.shoukdwb_id=s.id and  "
						+ "y.riq>=to_date('"
						+ getRiqi()
						+ "','yyyy-mm-dd')and y.riq<=to_date('"
						+ getRiq2() + "','yyyy-mm-dd')" + ruzzt);
			}else{
				sbsql.append("select to_char(y.riq,'yyyy-mm-dd'),decode(y.bianh,null,null,getHtmlAlert('"
						+ MainGlobal.getHomeContext(this)
						+ "','YufkReport','lx',y.id,y.bianh)) as bianh,g.mingc,y.jine,y.yue,s.quanc,y.kaihyh,y.zhangh,to_char(y.ruzrq,'yyyy-mm-dd'),y.beiz from yufkb y,gongysb g,shoukdw s\n");
		        sbsql.append("where y.gongysb_id=g.id and y.shoukdwb_id=s.id and  y.gongysb_id="
						+ getGongysValue().getId()
						+ "and y.riq>=to_date('"
						+ getRiqi()
						+ "','yyyy-mm-dd')and y.riq<=to_date('"
						+ getRiq2() + "','yyyy-mm-dd')" + ruzzt);
			}
		}else{
			if(getGongysValue().getId()==-1){
				sbsql.append("select to_char(y.riq,'yyyy-mm-dd'),decode(y.bianh,null,null,getHtmlAlert('"
						+ MainGlobal.getHomeContext(this)
						+ "','Yufklsb','lx',y.id,y.bianh)) as bianh,g.mingc,y.jine,y.yue,s.quanc,y.kaihyh,y.zhangh,to_char(y.ruzrq,'yyyy-mm-dd'),y.beiz from yufkb y,gongysb g,shoukdw s\n");
		        sbsql.append("where y.gongysb_id=g.id and y.shoukdwb_id=s.id and  "
						+ "y.riq>=to_date('"
						+ getRiqi()
						+ "','yyyy-mm-dd')and y.riq<=to_date('"
						+ getRiq2() + "','yyyy-mm-dd')" + ruzzt);
			}else{
				sbsql.append("select to_char(y.riq,'yyyy-mm-dd'),decode(y.bianh,null,null,getHtmlAlert('"
						+ MainGlobal.getHomeContext(this)
						+ "','Yufklsb','lx',y.id,y.bianh)) as bianh,g.mingc,y.jine,y.yue,s.quanc,y.kaihyh,y.zhangh,to_char(y.ruzrq,'yyyy-mm-dd'),y.beiz from yufkb y,gongysb g,shoukdw s\n");
		        sbsql.append("where y.gongysb_id=g.id and y.shoukdwb_id=s.id and  y.gongysb_id="
						+ getGongysValue().getId()
						+ "and y.riq>=to_date('"
						+ getRiqi()
						+ "','yyyy-mm-dd')and y.riq<=to_date('"
						+ getRiq2() + "','yyyy-mm-dd')" + ruzzt);
			}
			
		}
//        System.out.println(sbsql.toString());
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] { "����", "���", "������λ", "Ԥ�����", "���", "�տλ",
				"��������", "�ʺ�", "��������", "��ע" };

		int ArrWidth[] = new int[] { 100, 60, 100, 60, 60, 100, 100, 60, 100,
				100 };
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("Ԥ�����ѯ��", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "���:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getYufktjb() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String ruzzt = "";
		if (getRuzztValue().getId() == 0) {
			ruzzt = "and ruzrq is null";
		} else {
			ruzzt = "and ruzrq is not null";
		}
		if(getGongysValue().getId()==-1){
			sbsql
			.append("select decode(g.mingc,null,'�ܼ�',g.mingc)as gongys,decode(y.bianh,null,\n"
					+ "decode(g.mingc,null,'','С��'),y.bianh)as bianh,\n"
					+ "to_char(y.riq,'yyyy-mm-dd'),s.quanc,y.kaihyh,y.zhangh,sum(y.jine) as jine,sum(y.yue) as yue,to_char(y.ruzrq,'yyyy-mm-dd'),y.beiz\n"
					+ "from yufkb y,gongysb g,shoukdw s where g.id= y.gongysb_id\n"
					+ "and s.id=y.shoukdwb_id\n"
					+ ruzzt
					+ "\n"
					+ "and y.riq>=to_date('"
					+ getRiqi()
					+ "','yyyy-mm-dd')"
					+ "and y.riq<=to_date('"
					+ getRiq2()
					+ "','yyyy-mm-dd')"
					+ "group by rollup (g.mingc,(y.bianh,y.riq,s.quanc,y.kaihyh,y.zhangh,y.ruzrq,y.beiz))\n"
					+ "order by grouping(g.mingc) desc,g.mingc,grouping(y.bianh) desc,y.bianh");
		}else{
			sbsql
			.append("select decode(g.mingc,null,'�ܼ�',g.mingc)as gongys,decode(y.bianh,null,\n"
					+ "decode(g.mingc,null,'','С��'),y.bianh)as bianh,\n"
					+ "to_char(y.riq,'yyyy-mm-dd'),s.quanc,y.kaihyh,y.zhangh,sum(y.jine) as jine,sum(y.yue) as yue,to_char(y.ruzrq,'yyyy-mm-dd'),y.beiz\n"
					+ "from yufkb y,gongysb g,shoukdw s where g.id= y.gongysb_id\n"
					+ "and s.id=y.shoukdwb_id\n"
					+ ruzzt
					+ "\n"
					+ "and y.gongysb_id="
					+ getGongysValue().getId()
					+ "\n"
					+ "and y.riq>=to_date('"
					+ getRiqi()
					+ "','yyyy-mm-dd')"
					+ "and y.riq<=to_date('"
					+ getRiq2()
					+ "','yyyy-mm-dd')"
					+ "group by rollup (g.mingc,(y.bianh,y.riq,s.quanc,y.kaihyh,y.zhangh,y.ruzrq,y.beiz))\n"
					+ "order by grouping(g.mingc) desc,g.mingc,grouping(y.bianh) desc,y.bianh");
		}

		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] { "��Ӧ��", "���", "����", "�տλ", "��������", "�ʺ�",
				"���(Ԫ)", "���(Ԫ)", "��������", "��ע" };

        int ArrWidth[] = new int[] { 100, 60, 100, 100, 100, 60, 60, 60, 100,
				150 };
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("Ԥ����ͳ�Ʊ�", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(8, 1, "���:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(10, 1, "�Ʊ�:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}

	}

	// ����
	private void Save() {
		Visit visit = new Visit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
//			sql = "update yufkb y set ruzrq=to_date('" + getRiq()
//					+ "','yyyy-mm-dd'),ruzry=" + visit.getRenymc()
//					+ " where  y.gongysb_id=" + getGongysValue().getId();
			
			sql="update yufkb set ruzrq=sysdate,ruzry='"+visit.getRenymc()+"' where ruzrq is null";

			con.getUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			getSelectData();
			
			
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel3(null);
				visit.setDropDownBean3(null);
				getGongysModels();
				setGongysValue(null);
				getGongysModel();
				getRuzztModels();
				setRuzztValue(null);
				getRuzztModel();
				setRiqi(null);
				setRiq2(null);
				visit.setboolean3(false);
			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
			
		}
        
		if(mstrReportName.equals("Yufkcx")||mstrReportName.equals("Yufktjb")){
			
		}else{
			mstrReportName="Yufkcx";
		}
		
		
		//begin��������г�ʼ������
		visit.setString4(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString4(pagewith);
			}
		//	visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			
			
		// mstrReportName="diaor04bb";
		getSelectData();

	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	// ����״̬
	public boolean _Ruzztchange = false;

	public IDropDownBean getRuzztValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getRuzztModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setRuzztValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getRuzztModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getRuzztModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setRuzztModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getRuzztModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			List.add(new IDropDownBean(0, "δ����"));
			List.add(new IDropDownBean(1, "������"));

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// ������λ

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {

		String sql = "select gys.id,gys.mingc from gongysdcglb glb,diancxxb dc,gongysb gys\n"
				+ "where glb.diancxxb_id=dc.id and glb.gongysb_id=gys.id and gys.leix=1 and zhuangt=1 and dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ " order by gys.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ʼ����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��������:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("������λ:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("GongysDropDown");
		meik.setEditable(true);
		meik.setWidth(100);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);

		tb1.addText(new ToolbarText("����״̬:"));
		ComboBox gh = new ComboBox();
		gh.setTransform("RuzztDropDown");
		gh.setEditable(true);
		gh.setWidth(80);
		gh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gh);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		if (getRuzztValue().getId() == 0) {
			ToolbarButton tb2 = new ToolbarButton(null, "����",
					"function(){ document.Form0.SaveButton.click();}");
			tb2.setId("savebt");
			tb1.addItem(tb2);
		}

		setToolbar(tb1);

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

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}
}