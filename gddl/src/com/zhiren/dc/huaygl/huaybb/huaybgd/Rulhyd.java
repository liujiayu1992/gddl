package com.zhiren.dc.huaygl.huaybb.huaybgd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
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
/*
 * 作者:tzf
 * 时间:2009-07-02
 * 修改内容:对于  有分厂时 ，要显示 电厂下拉框  页面加载出错，多个ext组建 对应同一个 html节点，
 * 且  一厂多制时 ，sql查询时   只是根据登录人员的电厂id进行查数，并没有根据下拉框选择的
 * 电厂进行查询，现已调整.
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者:夏峥
 * 时间:2013-07-12
 * 修改内容: 使用系统参数配置化验报告单所使用的发热量保留小数位。
 */
/*
 * 作者:夏峥
 * 时间:2013-11-22
 * 修改内容: 使用系统参数配置化验报告单显示审核状态为何值的数据。
 */

public class Rulhyd extends BasePage {
	public boolean getRaw() {
		return true;
	}

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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

	public String getPrintTable() {
		return getHuaybgd();
	}

	// 燃料采购部指标完成情况日报
	private String getHuaybgd() {
		Visit visit = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String diancxxb_id=visit.getDiancxxb_id()+"";
		
		if(visit.isFencb()){
			diancxxb_id=getTreeid();
		}
		
		String shCondition=MainGlobal.getXitxx_item("入炉", "入炉化验报告单审核状态", diancxxb_id, "");
		
		if(!shCondition.equals("")){
			shCondition="and r.shenhzt in("+shCondition+") \n";
		}
		
		int farldec=visit.getFarldec();
		StringBuffer sqlRulhyd = new StringBuffer();
		sqlRulhyd
				.append("select TO_CHAR(r.lursj, 'YYYY-MM-DD') AS lursj,\n"
						+ "       TO_CHAR(r.rulrq, 'YYYY-MM-DD') AS rulrq,\n"
						+ "       round_new(r.mt, 1) as MT,\n"
						+ "       round_new(r.mad, 2) as MAD,\n"
						+ "       round_new(r.aad, 2) as AAD,\n"
						+ "       round_new(r.aar, 2) as AAR,\n"
						+ "       round_new(r.ad, 2) as AD,\n"
						+ "       round_new(r.vad, 2) as VAD,\n"
						+ "       round_new(r.vdaf, 2) as VDAF,\n"
						+ "       round_new(r.stad, 2) as STAD,\n"
						+ "       round_new(r.std, 2) as STD,\n"
						+ "       ROUND_NEW((100 - r.MT) * r.STAD / (100 - r.MAD), 2) AS STAR,\n"
						+ "       round_new(r.had, 2) as HAD,\n"
						+ "       round_new(r.har, 2) as HAR,\n"
						+ "       round_new(r.qbad, 3) * 1000 as QBAD,\n"
						+ "       round_new(r.qgrd, "+farldec+") * 1000 as QGRD,\n"
						+ "       round_new(r.qgrad, "+farldec+") * 1000 as QGRAD,\n"
						+ "       round_new(r.qgrad_daf, "+farldec+") * 1000 as GANZWHJGWRZ,\n"
						+ "       round_new(r.qnet_ar, "+farldec+") * 1000 as QNETAR,\n"
						+ "       round_new(round_new(r.qnet_ar, "+farldec+") * 7000 / 29.271, 0) as FRL,\n"
						+ "       r.lury,\n"
						+ "       '' as shenhry,\n"
						+ "       round_new(m.fadhy + m.gongrhy+m.feiscy, 2) as meihy,\n"
						+ "       to_char(r.fenxrq,'yyyy-mm-dd') as fenxrq,\n" + "       rb.mingc banzmc,\n"
						+ "       j.mingc jizmc\n"
						+ "  from rulmzlb r, meihyb m,rulbzb rb,jizfzb j\n"
						+ " where r.rulrq = "
						+ DateUtil.FormatOracleDate(getRiq()) + "\n"
//						+ "   and r.diancxxb_id=" + visit.getDiancxxb_id()
						+ "   and r.diancxxb_id=" + diancxxb_id
						+ "   and m.rulrq = r.rulrq\n"
						+ "   and m.rulbzb_id = r.rulbzb_id\n"
						+ "   and m.jizfzb_id = r.jizfzb_id\n"
                        + "   and r.rulbzb_id=rb.id\n"
                        + shCondition
						+ "   and r.jizfzb_id=j.id" + "   and r.rulbzb_id="
						+ getBianmValue().getId() + "   and r.jizfzb_id="
						+ getChangbValue().getId());
		
//		System.out.println(sqlRulhyd);
		ResultSet rs = con.getResultSet(sqlRulhyd);
		String shangjshry = "";
		String lury = "";
		String[][] ArrHeader = new String[20][6];
		try {
			if (rs.next()) {
				lury = rs.getString("lury");
//				StringBuffer buffer = new StringBuffer();
				// String cheph = rs.getString("CHEPH");
				// String[] list = cheph.split(",");
				// for (int i = 1; i <= list.length; i++) {
				// if (i % 11 == 0) {
				// buffer.append(list[i - 1] + ",<br>");
				// } else {
				// buffer.append(list[i - 1] + ",");
				// }
				// }
				// cheph = buffer.toString().substring(0, buffer.length() - 1);
				String num = rs.getString("FRL");
				ArrHeader[0] = new String[] { "入炉日期",
						"" + rs.getString("rulrq") + "", "录入时间",
						"" + rs.getString("lursj") + "", "分析日期",
						"" + rs.getString("fenxrq") + "" };

				ArrHeader[1] = new String[] { "煤耗用",
						"" + rs.getString("meihy") + "", "班组信息",
						"" + rs.getString("banzmc") + "", "机组信息",
						"" + rs.getString("jizmc") + "" };
				ArrHeader[2] = new String[] { "录入人员",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "" };
				ArrHeader[3] = new String[] { "全水分Mt(%)", "全水分Mt(%)",
						"全水分Mt(%)", "" + rs.getDouble("MT") + "", "备注", "备注" };
				ArrHeader[4] = new String[] { "空气干燥基水分Mad(%)", "空气干燥基水分Mad(%)",
						"空气干燥基水分Mad(%)", "" + rs.getDouble("MAD") + "", "", "" };
				ArrHeader[5] = new String[] { "空气干燥基灰分Aad(%)", "空气干燥基灰分Aad(%)",
						"空气干燥基灰分Aad(%)", "" + rs.getDouble("AAD") + "", "", "" };
				ArrHeader[6] = new String[] { "收到基灰分Aar(%)", "收到基灰分Aar(%)",
						"收到基灰分Aar(%)", "" + rs.getDouble("AAR") + "", "", "" };
				ArrHeader[7] = new String[] { "干燥基灰分Ad(%)", "干燥基灰分Ad(%)",
						"干燥基灰分Ad(%)", "" + rs.getDouble("AD") + "", "0", "0" };
				ArrHeader[8] = new String[] { "空气干燥基挥发分Vad(%)",
						"空气干燥基挥发分Vad(%)", "空气干燥基挥发分Vad(%)",
						"" + rs.getDouble("VAD") + "", "", "" };
				ArrHeader[9] = new String[] { "干燥无灰基挥发分Vdaf(%)",
						"干燥无灰基挥发分Vdaf(%)", "干燥无灰基挥发分Vdaf(%)",
						"" + rs.getDouble("VDAF") + "", "", "" };
				ArrHeader[10] = new String[] { "空气干燥基全硫St,ad(%)",
						"空气干燥基全硫St,ad(%)", "空气干燥基全硫St,ad(%)",
						"" + rs.getDouble("STAD") + "", "", "" };
				ArrHeader[11] = new String[] { "干燥基全硫St,d(%)", "干燥基全硫St,d(%)",
						"干燥基全硫St,d(%)", "" + rs.getDouble("STD") + "", "", "" };
				ArrHeader[12] = new String[] { "收到基全硫St,ar(%)",
						"收到基全硫St,ar(%)", "收到基全硫St,ar(%)",
						"" + rs.getDouble("STAR") + "", "", "" };
				ArrHeader[13] = new String[] { "空气干燥基氢Had(%)", "空气干燥基氢Had(%)",
						"空气干燥基氢Had(%)", "" + rs.getDouble("HAD") + "", "", "" };
				ArrHeader[14] = new String[] { "收到基氢Har(%)", "收到基氢Har(%)",
						"收到基氢Har(%)", "" + rs.getDouble("HAR") + "", "", "" };
				ArrHeader[15] = new String[] { "空气干燥基弹筒热值Qb,ad(J/g)",
						"空气干燥基弹筒热值Qb,ad(J/g)", "空气干燥基弹筒热值Qb,ad(J/g)",
						"" + rs.getDouble("QBAD") + "", "", "" };
				ArrHeader[16] = new String[] { "干燥基高位热值Qgr,d(J/g)",
						"干燥基高位热值Qgr,d(J/g)", "干燥基高位热值Qgr,d(J/g)",
						"" + rs.getDouble("QGRD") + "", "", "" };
				ArrHeader[17] = new String[] { "空气干燥基高位热值Qgr,ad(J/g)",
						"空气干燥基高位热值Qgr,ad(J/g)", "空气干燥基高位热值Qgr,ad(J/g)",
						"" + rs.getDouble("QGRAD") + "", "", "" };
				ArrHeader[18] = new String[] { "干燥无灰基高位热值Qgr,daf(J/g)",
						"干燥无灰基高位热值Qgr,daf(J/g)", "干燥无灰基高位热值Qgr,daf(J/g)",
						"" + rs.getDouble("GANZWHJGWRZ") + "", "", "" };
				ArrHeader[19] = new String[] { "收到基低位热值Qnet,ar(J/g)",
						"收到基低位热值Qnet,ar(J/g)", "收到基低位热值Qnet,ar(J/g)",
						"" + rs.getDouble("QNETAR") + "",
						"" + num + "" + "(千卡/千克)", "" + num + "" + "(千卡/千克)" };
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 100, 95, 95, 155, 95, 95 };

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("入  炉  煤  质  报  告", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:" + str, -1);
		rt.setDefautlFooter(3, 1, "负责人：", -1);
		rt.setDefautlFooter(4, 1, "审核：" + shangjshry, -1);
		rt.setDefautlFooter(5, 2, "化验员：" + lury, Table.ALIGN_RIGHT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(20, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		for (int i = 1; i < 20; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
		for (int i = 1; i <= 20; i++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCellValue(4, 4, rt.body.format(rt.body.getCellValue(4, 4),
				"0.0"));
		for (int i = 5; i < 16; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0.00"));
		}
		for (int i = 16; i < 21; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0"));
		}
		// rt.body.setCellValue(i, j, strValue);

		rt.body.setCellFontSize(4, 0, 9);
		rt.body.setCells(2, 1, 20, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		// rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.merge(2, 1, 20, 3);
		rt.body.merge(2, 5, 20, 6);
		rt.body.merge(3, 2, 3, 6);
		// rt.body.merge(3, 4, 3, 6);
		rt.body.ShowZero = false;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// 刷新衡单列表
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
        
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+((Visit)getPage().getVisit()).getDiancxxb_id(),"",null,getTreeid());
		((Visit)getPage().getVisit()).setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
//		if (visit.isFencb()) {
//			tb1.addText(new ToolbarText("厂别:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("DCSelect");
//			changbcb.setWidth(130);
//			changbcb
//					.setListeners("select:function(own,rec,index){Ext.getDom('DCSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
		tb1.addText(new ToolbarText("入炉日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("rulrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("班组信息:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("机组信息:"));
		ComboBox jiz = new ComboBox();
		jiz.setTransform("ChangbSelect");
		jiz.setWidth(130);
		jiz.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		tb1.addField(jiz);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		((DateField) getToolbar().getItem("rulrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			setTreeid(null);
			this.setDCModel(null);
			this.setDCValue(null);
            setDiancmcValue(null);
		    setDiancmcModel(null);
		    getSelectData();
		}
		
		//begin方法里进行初始化设置
		visit.setString1(null);
			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {
				visit.setString1(pagewith);
			}
		//	visit.setString1(null);保存传递的非默认纸张的样式
			
//		if (riqchange) {
//			riqchange = false;
//			setBianmValue(null);
//			setBianmModel(null);
//		}
//		
		if(visit.isFencb()){
			if(DCflage){
				DCflage=false;
				setChangbValue(null);
				setChangbModel(null);
				setBianmValue(null);
				setBianmModel(null);
			}
		}
	}
	
	// 添加电厂树
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 电厂名称
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel4(_value);
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	// 日期是否变化
//	private boolean riqchange = false;

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
//		if (this.riq != null) {
//			if (!this.riq.equals(riq))
//				riqchange = true;
//		}
		this.riq = riq;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// 班组
	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		String diancxxb_id = visit.getDiancxxb_id() + "";
		if (visit.isFencb()) {
			diancxxb_id = getTreeid();
		}
		sb.append("select bz.id, bz.mingc " +
				  "  from rulbzb bz, diancxxb dc " +
				  " where bz.diancxxb_id = dc.id " +
				  "   and (dc.id = " + diancxxb_id + " or dc.fuid = " + diancxxb_id + ")");
		setBianmModel(new IDropDownModel(sb.toString(), "请选择"));
	}

	// 机组下拉框
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		String diancxxb_id = visit.getDiancxxb_id() + "";
		if (visit.isFencb()) {
			diancxxb_id = getTreeid();
		}
		
		sb.append("select j.id,j.mingc from jizfzb j,diancxxb d where d.id=j.diancxxb_id  and (d.id="
						+ diancxxb_id + " or d.fuid=" + diancxxb_id + ")");

		setChangbModel(new IDropDownModel(sb.toString(), "请选择"));
	}

	// 分厂别
	private boolean DCflage = false;

	public IDropDownBean getDCValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getDCModel().getOptionCount() > 0) {
				setDCValue((IDropDownBean) getDCModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setDCValue(IDropDownBean value) {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() != null) {
			if (!((Visit) this.getPage().getVisit()).getDropDownBean3().equals(
					value)) {
				DCflage = true;
			}
		}
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getDCModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setDCModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setDCModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void setDCModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();

		sb.append("select id,mingc from diancxxb where fuid="
				+ visit.getDiancxxb_id());

		setDCModel(new IDropDownModel(sb.toString(), "请选择"));
	}
}