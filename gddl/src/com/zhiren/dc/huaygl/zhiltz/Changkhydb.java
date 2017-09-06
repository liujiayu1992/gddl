package com.zhiren.dc.huaygl.zhiltz;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
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

public class Changkhydb extends BasePage {
	
	
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

	// ***************设置消息框******************//
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


	// private String leix = "";



	public String getPrintTable() {
		setMsg(null);
		
		return getChangkhydb();

	}
//	设置制表人默认当前用户
	private String getZhibr(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String zhibr="";
		String zhi="否";
		String sql="select zhi from xitxxb where mingc='月报管理制表人是否默认当前用户' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSet rs=con.getResultSet(sql);
		try{
		  while(rs.next()){
			  zhi=rs.getString("zhi");
		  }
		}catch(Exception e){
			System.out.println(e);
		}
		if(zhi.equals("是")){
			zhibr=visit.getRenymc();
		}	
		return zhibr;
	}
	

	// 判断电厂Tree中所选电厂时候还有子电厂
	private boolean hasDianc(String id) {
		JDBCcon con = new JDBCcon();
		boolean mingc = false;
		String sql = "select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = true;
		}
		rsl.close();
		return mingc;
	}


	private String getChangkhydb() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String meik = "";
		String s = "";
        String pinz="";
		if (!this.hasDianc(this.getTreeid_dc())) {
			s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// 增加
																		// 厂别处理条件;
		}
		
		if(getMeikValue().getId()!=-1){
			meik = " and f.meikxxb_id="+getMeikValue().getId();
		}
		
		if(getPinzValue().getId()!=-1){
			pinz = " and f.pinzb_id="+getPinzValue().getId();
		}
		
		String YunsfsSql = "";
		if (getYunsfsValue().getId() != -1) {
			YunsfsSql = "           and f.yunsfsb_id = "
					+ getYunsfsValue().getId() + "\n";
		}
		
		


		StringBuffer buffer = new StringBuffer();

		buffer
				.append(
						"select\n" +
						"       mkdw,\n" + 
						"       daohrq,\n" + 
						"       pz,\n" + 
						"       laimsl,\n" + 
						"       yansl,\n" + 
						"       ysl,\n" + 
						"       mt_k,\n" + 
						"       aad_k,\n" + 
						"       vdaf_k,\n" + 
						"       std_k,\n" + 
						"       farl_k ,\n" + 
						"       mt,\n" + 
						"       mad,\n" + 
						"       aad,\n" + 
						"       ad,\n" + 
						"       vad,\n" + 
						"       vdaf,\n" + 
						"       std,\n" + 
						"       qgrad,\n" + 
						"       farl\n" + 
						
						
						"  from (select decode(grouping(m.mingc),\n" + 
						"                      1,\n" + 
						"                      '合计',\n" + 
						"                      m.mingc) mkdw,\n" + 
						"               decode(grouping(m.mingc) +\n" + 
						"                      grouping(f.daohrq),\n" + 
						"                      1,\n" + 
						"                      '小计',\n" + 
						"                      to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n" + 
						"               p.mingc pz,\n" + 
						"               sum(round_new(f.laimsl,0)) laimsl,\n" + 
						"                sum(round_new(f.laimsl,0))yansl,\n" + 
						"                    '100%' ysl,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"               0,\n" + 
						"               0,\n" + 
						"               round_new(sum(round_new(kz.mt,1)*round_new(f.laimsl,0))/sum(round_new(f.laimsl,0)),1))as mt_k,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"               0,\n" + 
						"               0,\n" + 
						"               round_new(sum(round_new(kz.aad,1)*round_new(f.laimsl,0))/sum(round_new(f.laimsl,0)),1))as aad_k,\n" + 
						"                decode(sum(round_new(f.laimsl,0)),\n" + 
						"               0,\n" + 
						"               0,\n" + 
						"               round_new(sum(round_new(kz.vdaf,1)*round_new(f.laimsl,0))/sum(round_new(f.laimsl,0)),1))as vdaf_k,\n" + 
						"                decode(sum(round_new(f.laimsl,0)),\n" + 
						"               0,\n" + 
						"               0,\n" + 
						"               round_new(sum(round_new(kz.std,1)*round_new(f.laimsl,0))/sum(round_new(f.laimsl,0)),1))as std_k,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"               0,\n" + 
						"               0,\n" + 
						"               round_new(sum(round_new(kz.qnet_ar,1)*round_new(f.laimsl,0))/sum(round_new(f.laimsl,0)),1))as farl_k,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(round_new(z.mt,1) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 1)) as mt,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(z.mad * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as mad,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(z.aad * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as aad,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(z.ad * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as ad,\n" + 
						"                            decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(z.vad * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as vad,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(z.vdaf * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as vdaf,\n" + 
						"                       decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(round_new(z.qnet_ar,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as farl,\n" + 
						"               decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(z.std * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as std,\n" + 
						"              decode(sum(round_new(f.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(z.qgrad * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as qgrad,\n" + 
						"\n" + 
						"               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n" + 
						"          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z,kuangfzlb kz\n" + 
						"         where\n" + 
					//	"   f.diancxxb_id="+getTreeid_dc()+"\n" + 
						"           f.meikxxb_id = m.id\n" + 
						"           and f.pinzb_id = p.id\n" + 
						"           and f.faz_id = c.id\n" + 
						"           and f.zhilb_id = z.id\n" + 
						"           and f.kuangfzlb_id=kz.id\n" + 
						            s+""+meik+""+pinz+""+YunsfsSql+
						            "and f.daohrq >= to_date('"
									+ getRiqi()
									+ "', 'yyyy-mm-dd')\n"
									+ "           and f.daohrq <= to_date('"
									+ getRiq2()
									+ "', 'yyyy-mm-dd')\n"+
						"         group by rollup( m.mingc, f.daohrq, p.mingc, c.mingc)\n" + 
						"        having grouping(f.daohrq) = 1 or grouping(c.mingc) = 0\n" + 
						"         order by mkdw, daohrq )");

		//System.out.println(buffer.toString());
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


		 String ArrHeader[][]=new String[2][19];
		 ArrHeader[0]=new String[] {"煤矿单位名称","来煤日期","品种","来煤数量","验收数量","验收率","矿方化验","矿方化验","矿方化验","矿方化验","矿方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验"};
		 ArrHeader[1]=new String[] {"煤矿单位名称","来煤日期","品种","来煤数量","验收数量","验收率","Mt","Aad","Vdaf","St,d","Qnet,ar(MJ/kg)","Mt","Mad","Aad","Ad","Vad","Vdaf","St,d","Qgr,ad(MJ/kg)","Qnet,ar(MJ/kg)"};

		int[] ArrWidth = new int[20];

		ArrWidth = new int[] {90,80,40,50,50,45,45,45,45,45,60,45,45,45,45,45,45,45,60,60};

		rt.setTitle("厂 矿 化 验 对 比 表", ArrWidth);
		rt.title.setRowHeight(3, 40);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(3, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "卸煤日期:" + getRiqi() + "至" + getRiq2(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		

//
//		String[] strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
//				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
//				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		//rt.body.mergeFixedRow();
	//	rt.body.setColFormat(strFormat);
		//合并单元格
		rt.body.mergeCell(1,1,2,1);
		rt.body.mergeCell(1,2,2,2);
		rt.body.mergeCell(1,3,2,3);
		rt.body.mergeCell(1,4,2,4);
		rt.body.mergeCell(1,5,2,5);
		rt.body.mergeCell(1,6,2,6);
		rt.body.mergeCell(1,7,1,11);
		rt.body.mergeCell(1,12,1,20);
		for (int i = 1; i <= 20; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 4, "制表:"+getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}
	
	
	// 运输方式下拉框
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		String sql = "select id,mingc from yunsfsb";
		YunsfsModel = new IDropDownModel(sql, "全部");
		return YunsfsModel;
	}


//	煤矿下拉框
	public IDropDownBean getMeikValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getMeikModel().getOptionCount()>0) {
				setMeikValue((IDropDownBean)getMeikModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setMeikValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getMeikModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setMeikModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setMeikModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setMeikModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,mingc from meikxxb order by id");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"全部"));
		setMeikModel(new IDropDownModel(list,sb));
	}
	
//	品种下拉框
	public IDropDownBean getPinzValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getPinzModel().getOptionCount()>0) {
				setPinzValue((IDropDownBean)getPinzModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setPinzValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getPinzModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setPinzModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setPinzModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setPinzModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,mingc from pinzb order by id");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"全部"));
		setPinzModel(new IDropDownModel(list,sb));
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			setMeikValue(null);
			setMeikModel(null);
			setYunsfsValue(null);
			this.getYunsfsModels();
			this.setPinzValue(null);
			this.setPinzModels();
			getSelectData();
		}

		

		
		getSelectData();

	}
	
	


	// 绑定日期
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

	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {

			Refurbish();
			_RefurbishClick = false;
		}

	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getChangkhydb();
	}

	// -------------------------电厂Tree-----------------------------------------------------------------
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
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="
				+ visit.getDiancxxb_id() + " \n";
		sql += " union \n";
		sql += "  select d.id,d.mingc from diancxxb d where d.fuid="
				+ visit.getDiancxxb_id() + " \n";
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

	DefaultTree dc;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}

	// -------------------------电厂Tree END----------

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();

		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null,
				getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel())
				.getBeanValue(Long.parseLong(getTreeid_dc() == null
						|| "".equals(getTreeid_dc()) ? "-1" : getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("riq2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("煤矿:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("MeikSelect");
		meik.setWidth(80);
		meik.setListeners("select:function(own,rec,index){Ext.getDom('MeikSelect').selectedIndex=index}");
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox yuns = new ComboBox();
		yuns.setTransform("YUNSFSSelect");
		yuns.setEditable(true);
		yuns.setWidth(100);
		yuns.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuns);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("品种:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("PinzSelect");
		pinz.setWidth(80);
		pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
		tb1.addField(pinz);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

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

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
}