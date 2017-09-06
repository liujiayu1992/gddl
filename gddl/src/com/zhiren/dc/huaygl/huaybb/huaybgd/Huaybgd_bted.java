package com.zhiren.dc.huaygl.huaybb.huaybgd;

/*
 * 修改人：ww
 * 修改时间： 2009-10-25
 * 修改内容： 修改化验报告单格式添加制样时间、制样人
 */
/*
 * 修改人：ww
 * 修改时间： 2009-11-18
 * 修改内容： 化验报告单改为二级审核后显示
 */
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Huaybgd_bted extends BasePage {

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
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		boolean isChes = MainGlobal.getXitxx_item("化验", "化验报告单显示车数", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"否").equals("是");
		
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,GetCaiyry(y.id) as caiyry,to_char(y.zhiysj,'yyyy-mm-dd') as zhiyrq,getzhiyry(y.id) as zhiyry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.fcad, 2) as FCAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,2) as QBAD,round_new(z.qgrd,2) as QGRD,round_new(z.qgrad,2) as QGRAD,\n");
		sqlHuaybgd
				.append("round_new(z.qgrad_daf,2) as GANZWHJGWRZ,round_new(z.qnet_ar,2) as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,\n");
		sqlHuaybgd
				.append("'"
						+ getBianmValue().getValue()
						+ "' as bianh,a.meikdwmc,a.chez,a.pinz,a.cheph,a.ches,a.meil,z.beiz ,z.huayy, nvl(z.shenhry,' ') as shenhryyj ,nvl(z.shenhryej,' ') as shenhryej from zhillsb z,yangpdhb y,\n");
		sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
		sqlHuaybgd.append("cz.mingc as chez,\n");
		sqlHuaybgd.append("p.mingc as pinz,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd
				.append("round_new(sum(f.maoz) - sum(f.piz) -sum(f.zongkd), 2) as meil,\n");
		sqlHuaybgd.append("sum(f.ches) AS CHES, \n");			
		sqlHuaybgd.append("GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH \n");
		sqlHuaybgd
				.append("from fahb f, zhillsb z,meikxxb m,chezxxb cz,pinzb p\n");
		sqlHuaybgd.append("where z.zhilb_id = f.zhilb_id\n");
		sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
		sqlHuaybgd.append("and f.faz_id=cz.id\n");
		sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId()).append("\n");
		sqlHuaybgd.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id) a \n");
		sqlHuaybgd.append("where z.zhilb_id=a.zhilb_id\n");
		sqlHuaybgd.append("and y.zhilblsb_id=z.id\n");
		sqlHuaybgd.append("and z.shenhzt>=7");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
		ResultSet rs = con.getResultSet(sqlHuaybgd);
		String lury = "";
		String yisry = "";
		String ersry = "";
		String[][] ArrHeader = new String[14][6];
		try {
			if (rs.next()) {
				lury = rs.getString("HUAYY");
				yisry = rs.getString("shenhryyj");
				ersry = rs.getString("shenhryej");
				//是否显示车数
				StringBuffer buffer = new StringBuffer();
				String cheph = "";
				if (!isChes) {
					cheph = rs.getString("CHEPH");
					String[] list = cheph.split(",");				
					for (int i = 1; i <= list.length; i++) {
						if (i % 11 == 0) {
							buffer.append(list[i - 1] + ",<br>");
						} else {
							buffer.append(list[i - 1] + ",");
						}
					}
					cheph = buffer.toString().substring(0, buffer.length() - 1);				
				}
				String num = rs.getString("FRL");
				ArrHeader[0] = new String[] { "化验编号", rs.getString("BIANH"), "矿别", rs.getString("MEIKDWMC"), "煤量(t)",  rs.getString("MEIL")};
				
				ArrHeader[1] = new String[] { "化验日期", rs.getString("HUAYRQ"), rs.getString("CHEZ"), rs.getString("PINZ"),  "车数", rs.getString("ches")};
				
				ArrHeader[2] = new String[] { "采样日期", rs.getString("CAIYRQ"), "采样人员", rs.getString("caiyry"), rs.getString("caiyry"), rs.getString("caiyry")};
				
				ArrHeader[3] = new String[] { "制样日期", rs.getString("zhiyrq"), "制样人员", rs.getString("zhiyry"), rs.getString("zhiyry"), rs.getString("zhiyry")};

				ArrHeader[4] = new String[] { "全水分Mt(%)", "全水分Mt(%)", rs.getString("MT"), "空气干燥基氢Had(%)", "空气干燥基氢Had(%)", rs.getString("HAD")};
				
				ArrHeader[5] = new String[] { "空气干燥基水分Mad(%)", "空气干燥基水分Mad(%)", rs.getString("MAD"), "空气干燥基固定碳FCad(%)", "空气干燥基固定碳FCad(%)", (100-rs.getDouble("MT")-rs.getDouble("AAD")-rs.getDouble("VAD")) + ""};
				
				ArrHeader[6] = new String[] { "空气干燥基灰分Aad(%)", "空气干燥基灰分Aad(%)", rs.getString("AAD"), 
						"收到基固定碳FCar(%)", "收到基固定碳FCar(%)", (100-rs.getDouble("MT")-rs.getDouble("AAD")-rs.getDouble("VAD"))*(100-rs.getDouble("MT"))/(100-rs.getDouble("MAD")) + ""};
				
				ArrHeader[7] = new String[] { "收到基灰分Aar(%)", "收到基灰分Aar(%)", "" + rs.getDouble("AAR") + "",
						"弹筒热值Qb,ad(MJ/kg)","弹筒热值Qb,ad(MJ/kg)","" + rs.getDouble("QBAD") + ""};
				
				ArrHeader[8] = new String[] { "干燥基灰分Ad(%)", "干燥基灰分Ad(%)", "" + rs.getDouble("AD") + "",
						"空气干燥基高位热值Qgr,ad(MJ/kg)", "空气干燥基高位热值Qgr,ad(MJ/kg)","" + rs.getDouble("QGRAD") + ""};
				
				ArrHeader[9] = new String[] { "空气干燥基挥发分Vad(%)", "空气干燥基挥发分Vad(%)","" + rs.getDouble("VAD") + "", 
						"干燥基高位热值Qgr,d(MJ/kg)", "干燥基高位热值Qgr,d(MJ/kg)","" + rs.getDouble("QGRD")};
				
				ArrHeader[10] = new String[] { "干燥无灰基挥发分Vdaf(%)", "干燥无灰基挥发分Vdaf(%)","" + rs.getDouble("VDAF") + "",
						"干燥无灰基高位热值Qgr,daf(MJ/kg)", "干燥无灰基高位热值Qgr,daf(MJ/kg)","" + rs.getDouble("GANZWHJGWRZ") + ""};
				
				ArrHeader[11] = new String[] {"空气干燥基全硫St,ad(%)", "空气干燥基全硫St,ad(%)","" + rs.getDouble("STAD") + "",
						"收到基低位热值Qnet,ar(MJ/kg)", "收到基低位热值Qnet,ar(MJ/kg)","" + rs.getDouble("QNETAR") + ""};
				
				ArrHeader[12] = new String[] { "干燥基全硫St,d(%)", "干燥基全硫St,d(%)", "" + rs.getDouble("STD") + "", 
						"收到基低位热值Qnet,ar(kcal/kg)", "收到基低位热值Qnet,ar(kcal/kg)","" + num + ""};
				
				ArrHeader[13] = new String[] {"备注","","","","",""};
				
				if (!rs.getString("CHEZ").equals("汽")) {
					cheph = rs.getString("CHEPH");
					String[] list = cheph.split(",");				
					for (int i = 1; i <= list.length; i++) {
						if (i % 8 == 0) {
							buffer.append(list[i - 1] + "<br>&nbsp;&nbsp;");
						} else {
							if (i==1) {
								buffer.append("&nbsp;&nbsp;" + list[i - 1] + ",");
							} else {
								buffer.append(list[i - 1] + ",");
							}
							
						}
					}
					cheph = buffer.toString().substring(0, buffer.length() - 1);				
				}
				if (cheph!=null && !"".equals(cheph)) {	
					    ArrHeader[13][1] = cheph;
						ArrHeader[13][2] = cheph;
						ArrHeader[13][3] = cheph;
						ArrHeader[13][4] = cheph;
						ArrHeader[13][5] = cheph;						
				}
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 120, 105, 105, 165, 105, 105 };

		rt.setTitle("煤  质  检  验  报  告", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		rt.footer.setRowHeight(12, 100);
		rt.footer.setCellValue(1, 1, "打印日期:" + str);
		rt.footer.setCellAlign(1, 1, -1);
		rt.footer.mergeCell(1, 1, 1, 2);
//		rt.setDefautlFooter(1, 2, "打印日期:" + str, -1);
//		rt.setDefautlFooter(1, 2, "负责人：", -1);
//		rt.setDefautlFooter(3, 2, "审核：",-1 );
//		rt.setDefautlFooter(5, 2, "化验员：",-1);
		rt.setDefautlFooter(1, 2, "二审人员："+ersry , -1);
		rt.setDefautlFooter(3, 2, "一审人员："+yisry,-1 );
		rt.setDefautlFooter(5, 2, "化验员："+lury,-1);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 12);
		rt.setBody(new Table(14, 6));
		rt.body.setFontSize(12);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		for (int i = 1; i < 14; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}

		rt.body.setCellValue(5, 3, rt.body.format(rt.body.getCellValue(5, 3), "0.0"));
		rt.body.setCellValue(6, 6, rt.body.format(rt.body.getCellValue(6, 6), "0.00"));
		rt.body.setCellValue(7, 6, rt.body.format(rt.body.getCellValue(7, 6), "0.00"));

		for (int i = 5; i <= 13; i++) {
			rt.body.setCellValue(i, 6, rt.body.format(rt.body
					.getCellValue(i, 6), "0.00"));
			if (i==5) {
			//全水
				rt.body.setCellValue(i, 3, rt.body.format(rt.body
						.getCellValue(i, 3), "0.0"));
			} else {
				rt.body.setCellValue(i, 3, rt.body.format(rt.body
						.getCellValue(i, 3), "0.00"));
			}
			//热量
			if (i==13) {
				rt.body.setCellValue(i, 6, rt.body.format(rt.body
						.getCellValue(i, 6), "0"));
			}
		}
		
		rt.body.setCells(2, 1, 13, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCellAlign(14, 1, Table.ALIGN_CENTER);
		rt.body.merge(5, 1, 13, 2);
		rt.body.merge(5, 4, 13, 5);
		rt.body.merge(3,4,3,6);
		rt.body.merge(4,4,4,6);
		rt.body.merge(14, 2, 14, 6);
		
		rt.body.ShowZero = false;

		rt.body.setFontSize(12);
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(43);
		rt.body.setRowHeight(14, 43*3);
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if (visit.isFencb()) {
			tb1.addText(new ToolbarText("厂别:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("化验日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("huayrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("化验编码:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
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
		((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
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
			getSelectData();
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}
	}

	// 日期是否变化
	private boolean riqchange = false;

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
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
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select l.id,z.bianm from zhuanmb z,zhillsb l,caiyb c\n")
				.append(
						"where z.zhillsb_id= l.id and c.zhilb_id = l.zhilb_id\n")
				.append("and l.huaysj = ")
				.append(DateUtil.FormatOracleDate(getRiq()))
				.append("\n")
				.append("and l.shenhzt>=7 \n")
				.append("and z.zhuanmlb_id = \n")
				.append(
						"(select id from zhuanmlb where jib = (select nvl(max(jib),0) from zhuanmlb))");
		setBianmModel(new IDropDownModel(sb.toString(), "请选择"));
	}

	// 厂别下拉框
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
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
}
