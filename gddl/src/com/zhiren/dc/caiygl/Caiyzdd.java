package com.zhiren.dc.caiygl;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Caiyzdd extends BasePage {
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
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	// 页面变化记录
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

	// 衡单下拉框
	public IDropDownBean getBianhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianhModel().getOptionCount() > 0) {
				setBianhValue((IDropDownBean) getBianhModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianhValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
	}

	public void setBianhModels() {
		// Visit visit = (Visit)this.getPage().getVisit();
		// JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		// sb.append("select zhi from xitxxb where mingc='过衡单打印时间范围' and
		// leib='数量' and diancxxb_id="+visit.getDiancxxb_id());
		// ResultSetList rsl = con.getResultSetList(sb.toString());
		// String tians = "30";
		// if(rsl.next()) {
		// tians = rsl.getString("zhi");
		// }
		// sb.delete(0, sb.length());
		sb.append("select z.zhillsb_id as id,z.bianm \n");
		sb.append(" from  zhuanmb z, caiyb c,zhillsb zl,yangpdhb y,fahb f \n");
		sb.append("where z.zhuanmlb_id in\n");
		sb.append("(select id from zhuanmlb where jib=2)\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and zl.id=z.zhillsb_id\n");
		sb.append("and c.zhilb_id=f.zhilb_id\n");
		sb.append("and c.caiyrq >=sysdate -7\n");
		setBianhModel(new IDropDownModel(sb.toString()));
	}

	// 刷新衡单列表
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("采样编号:"));
		ComboBox bianhcb = new ComboBox();
		bianhcb.setTransform("BianhSelect");
		bianhcb.setWidth(130);
		bianhcb
				.setListeners("select:function(own,rec,index){Ext.getDom('BianhSelect').selectedIndex=index}");
		tb1.addField(bianhcb);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		// Document _Document = new Document();
		// String bianh = "";
		long bianhid = -1;
		if (getBianhValue() != null) {
			// bianh = getBianhValue().getValue();
			bianhid = getBianhValue().getId();
		}
		// int EndPageRow = 0;
		sb.setLength(0);
		sb
				.append("select count(cp.id) as ches,z.bianm ,y.caiyfs,'' as daih,'0.2mm' as lid,18 as caiyds, y.leib, c.caiyrq\n");
		sb
				.append("from yangpdhb y, zhuanmb z, zhuanmlb zl, caiyb c, fahb f, chepb cp\n");
		sb.append(" where y.zhilblsb_id =" + bianhid + "\n");
		sb.append("   and zl.id = z.zhuanmlb_id\n");
		sb.append(" and y.zhilblsb_id = z.zhillsb_id\n");
		sb.append("  and zl.jib = 2\n");
		sb.append("  and y.caiyb_id = c.id\n");
		sb.append(" and cp.fahb_id = f.id\n");
		sb.append(" and c.zhilb_id = f.zhilb_id\n");
		sb.append(" group by z.bianm,y.caiyfs,y.leib,c.caiyrq\n");
		sb.append("union\n");
		sb
				.append("select count(cp.id) as ches,z.bianm ,y.caiyfs,'' as daih,'3mm' as lid,18 as caiyds, y.leib, c.caiyrq\n");
		sb
				.append("from yangpdhb y, zhuanmb z, zhuanmlb zl, caiyb c, fahb f, chepb cp\n");
		sb.append(" where y.zhilblsb_id =" + bianhid + "\n");
		sb.append("   and zl.id = z.zhuanmlb_id\n");
		sb.append(" and y.zhilblsb_id = z.zhillsb_id\n");
		sb.append("  and zl.jib = 2\n");
		sb.append("  and y.caiyb_id = c.id\n");
		sb.append(" and cp.fahb_id = f.id\n");
		sb.append(" and c.zhilb_id = f.zhilb_id\n");
		sb.append(" group by z.bianm,y.caiyfs,y.leib,c.caiyrq\n");
		sb.append("union\n");
		sb
				.append("select count(cp.id) as ches,z.bianm ,y.caiyfs,'' as daih,'6mm' as lid,18 as caiyds, y.leib, c.caiyrq\n");
		sb
				.append("from yangpdhb y, zhuanmb z, zhuanmlb zl, caiyb c, fahb f, chepb cp\n");
		sb.append(" where y.zhilblsb_id =" + bianhid + "\n");
		sb.append("   and zl.id = z.zhuanmlb_id\n");
		sb.append(" and y.zhilblsb_id = z.zhillsb_id\n");
		sb.append("  and zl.jib = 2\n");
		sb.append("  and y.caiyb_id = c.id\n");
		sb.append(" and cp.fahb_id = f.id\n");
		sb.append(" and c.zhilb_id = f.zhilb_id\n");
		sb.append(" group by z.bianm,y.caiyfs,y.leib,c.caiyrq\n");
		ResultSet rs = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();

		String[][] dataList = null;
		int rowNum = 0;
		try {
			int i = 0;
			rs.last();
			rowNum = rs.getRow() * 3;
			if (rowNum <= 0) {
				return null;
			}
			dataList = new String[rowNum][6];
			rs.beforeFirst();
			while (rs.next()) {
				dataList[i++] = new String[] { "采样编号", rs.getString("BIANM"),
						"车数", rs.getString("CHES"), "样品粒度", rs.getString("LID") };
				dataList[i++] = new String[] { "样品袋号",
						"   " + "              " + rs.getString("LEIB"),
						"采样点数", rs.getString("CAIYDS"), "采样日期",
						FormatDate(rs.getDate("CAIYRQ")) };
				dataList[i++] = new String[] { "----", "----", "----", "----",
						"----", "----" };
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		int[] ArrWidth = new int[] { 90, 140, 90, 90, 90, 120 };

		rt.setBody(new Table(rowNum, 6));
		rt.body.setWidth(ArrWidth);

		for (int i = 0; i < dataList.length; i++) {
			for (int j = 0; j < 6; j++) {
				rt.body.setCellValue(i + 1, j + 1, dataList[i][j]);
			}
		}
		rt.body.setPageRows(42);

		rt.body.setBorder(0, 0, 2, 2);

		for (int i = 1; i <= 6; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.body.setColCells(1, Table.PER_BORDER_LEFT, 2);
		rt.body.setColCells(6, Table.PER_BORDER_RIGHT, 2);

		for (int i = 1; i <= dataList.length; i++) {
			if (i % 3 == 0) {
				rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 2);
				rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
				rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(i, Table.PER_BORDER_TOP, 1);
			}
		}

		con.Close();
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();

		rt.body.setRowHeight(23);

		return rt.getAllPagesHtml();
	}

	// private Table getTotalTable(boolean less,int rownum){
	// JDBCcon con = new JDBCcon();
	// long hengdid = -1;
	// if(getBianhValue()!=null) {
	// hengdid = getBianhValue().getId();
	// }
	// Table tb1 = null;
	// String strrow ="";
	// if(less){
	// strrow = " where r < "+rownum;
	// }else{
	// strrow = " where r >="+rownum;
	// }
	// ResultSet hz;
	// StringBuffer sb = new StringBuffer();
	// sb.append("select bianm,'','',pinz,ches,biaoz,yingd,kuid from \n");
	// sb.append("(select rownum r, s.* from (select
	// decode(y.bianm,null,'合计',y.bianm) bianm, g.mingc fahdw, m.mingc meikdw,
	// p.mingc pinz, \n");
	// sb.append("sum(f.ches) ches, sum(f.biaoz) biaoz, sum(f.jingz) jingz,
	// sum(zongkd) zongkd, \n");
	// sb.append("sum(f.yuns) yuns, sum(f.yingd) yingd, sum(f.yingd-f.yingk)
	// kuid, \n");
	// sb.append("sum(f.yingk) yingk, c.mingc faz \n");
	// sb.append("from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, caiyb
	// y, \n");
	// sb.append("(select distinct fahb_id from chepb where guohb_id = ");
	// sb.append(hengdid).append(") cp \n");
	// sb.append("where f.id = cp.fahb_id and f.gongysb_id = g.id and f.pinzb_id
	// = p.id \n");
	// sb.append("and f.meikxxb_id = m.id and f.zhilb_id = y.zhilb_id and
	// f.faz_id = c.id \n");
	// sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
	// sb.append("group by rollup(y.bianm,g.mingc,m.mingc,p.mingc,c.mingc) \n");
	// sb.append("having (GROUPING(c.mingc) = 0 OR GROUPING(y.bianm) =1 )) s)
	// \n");
	// sb.append(strrow);
	//		
	// hz = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
	// ResultSet.CONCUR_READ_ONLY);
	// try {
	// if(hz.next()){
	// tb1 = new Table(hz, 1, 0, 1);
	// String ArrHeaderHZ[][] = new String[1][8];
	// ArrHeaderHZ[0] = new String[] { Locale.caiybm_caiyb,
	// Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb,
	// Locale.ches_fahb,
	// Locale.biaoz_fahb, Locale.yingd_fahb, Locale.kuid_fahb };
	// int[] ArrWidthHz = new int[8];
	// ArrWidthHz = new int[] { 50, 80, 150, 60,60,60,60,
	// 60 };
	// tb1.setWidth(ArrWidthHz);
	// tb1.setHeaderData(ArrHeaderHZ);
	// tb1.setColFormat(6,"0.000");
	// tb1.setColFormat(7,"0.000");
	// tb1.setColFormat(8,"0.000");
	// tb1.setColAlign(1, Table.ALIGN_CENTER);
	// tb1.setColAlign(2, Table.ALIGN_CENTER);
	// tb1.setColAlign(3, Table.ALIGN_CENTER);
	// tb1.setColAlign(4, Table.ALIGN_CENTER);
	// tb1.setColAlign(5, Table.ALIGN_RIGHT);
	// tb1.setColAlign(6, Table.ALIGN_RIGHT);
	// tb1.setColAlign(7, Table.ALIGN_RIGHT);
	// tb1.setColAlign(8, Table.ALIGN_RIGHT);
	// // 设置标题字体
	// tb1.setRowCells(1, Table.PER_FONTSIZE, 10);
	// tb1.setRowHeight(21);
	// tb1.ShowZero = false;
	// }
	// } catch (SQLException e) {
	// // TODO 自动生成 catch 块
	// e.printStackTrace();
	// }
	// return tb1;
	// }
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		// ((DateField)getToolbar().getItem("lursj")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}

	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setString1(null);
			setBianhValue(null);
			setBianhModel(null);
			getSelectData();
		}
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 日期格式化
	private String FormatDate(Date _date) {
		if (_date == null) {
			// return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// 页面登陆验证
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
