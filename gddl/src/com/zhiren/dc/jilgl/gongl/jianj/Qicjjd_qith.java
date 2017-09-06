package com.zhiren.dc.jilgl.gongl.jianj;

import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.DateUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Qicjjd_qith extends BasePage {
	// private static final String PRINT_MOR = "PRINT_MOR";

	private static final String PRINT_BAOER = "PRINT_BAOER";

	private static final String PRINT_HUXIAN = "PRINT_HUXIAN";

	private static final String PRINT_GUIGUAN = "PRINT_GUIGUAN";
	
	private static final String PRINT_LUBEI = "PRINT_LUBEI";

	private static final String PRINT_HBW = "PRINT_HBW";

	private static final String PRINT_BEIZ = "PRINT_BEIZ"; //老的检斤单样式，没有煤场，只有备注。甘肃用

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

	public boolean getRaw() {
		return true;
	}

	/*
	 * 作者:童忠付 时间:2009-4-13 修改内容:增加一场多制时电厂id的过滤,添加 返回按钮
	 * 
	 */
	private String filterDcid(Visit v) {

		String sqltmp = " (" + v.getDiancxxb_id() + ")";
		if (v.isFencb()) {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList("select id from diancxxb where fuid="
							+ v.getDiancxxb_id());
			sqltmp = "";
			while (rsl.next()) {
				sqltmp += "," + rsl.getString("id");
			}
			sqltmp = "(" + sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}

	// 获取查询语句

	public String getBaseSql(String qithwb_id) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select q.piaojh,\n" +
				"       (select d.mingc from diancxxb d where id = q.diancxxb_id) as shouhdw,\n" + 
				"       (select qt.mingc from qitgysb qt where id = q.qitgys_id) as fahdw,\n" + 
				"       q.cheph,\n" + 
				"       to_char(q.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,\n" + 
				"      to_char(q.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,\n" + 
				"       (select pz.mingc from pinzb pz where id = q.pinzb_id) as pingm,\n" + 
				"       q.yuanmz,\n" + 
				"       q.maoz,\n" + 
				"       q.piz,\n" + 
				"       (q.maoz - q.piz) as jingz,\n" + 
				"       q.beiz,\n" + 
				"       q.zhongcjjy,\n" + 
				"       q.qingcjjy,\n" + 
				"       (select r.mingc from renyxxb r where id = q.renyxxb_id) as shoulr\n" + 
				"  from qithwb q\n" + 
				" where q.id ="+qithwb_id);
		return sql.toString();
	}

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
//		if (visit.getString1() == null || "".equals(visit.getString1())) {
//			return "参数不正确";
//		}
		ResultSetList rsl = con
				.getResultSetList(getBaseSql(visit.getString19()));
		if (rsl == null) {
			setMsg("数据获取失败！请检查您的网络状况！错误代码 XXCX-001");
			return "";
		}
		
		/*以下内容暂时不用，检斤单不符合鲁北电厂要求
		String fahdw = "";
		String shouhdw = "";
		String cheh = "";
		String yundpjh = "";
		String  chengmzsj ="";
		String chengpzsj ="";
		String pinm = "";
		String danw = "吨";
		String yuanfs = "";
		String maoz = "";
		String piz = "";
		String jingz = "";
		String beiz = "";
		String chengmzjjy = "";
		String chengpzjjy = "";
		String shoulr = "";
		String piaojh = "";
		
		while(rsl.next()){
			fahdw = rsl.getString("fahdw");
			shouhdw = rsl.getString("shouhdw");
			cheh = rsl.getString("cheph");
			chengmzsj = rsl.getString("zhongcsj");
			chengpzsj = rsl.getString("qingcsj");
			pinm = rsl.getString("pingm");
			yuanfs = rsl.getString("yuanmz");
			maoz = rsl.getString("maoz");
			piz = rsl.getString("piz");
			jingz = rsl.getString("jingz");
			beiz = rsl.getString("beiz");
			chengmzjjy = rsl.getString("zhongcjjy");
			chengpzjjy = rsl.getString("qingcjjy");
			shoulr = rsl.getString("shoulr");
			piaojh = rsl.getString("piaojh");
			
			
			
		}
//		int fontSize = 9;
//		String SQL = "select zhi from xitxxb where leib = '数量' "
//				+ " and mingc = '汽车检斤单字体字号' and diancxxb_id = "
//				+ visit.getDiancxxb_id();
//		ResultSetList rs = con.getResultSetList(SQL);
//		if (rs.next()) {
//			fontSize = rs.getInt("zhi");
//		}
//		rs.close();
//		if (PRINT_BAOER.equals(visit.getString15())) {
//		 String ArrHeader[][]=new String[6][9];
////		 ArrHeader[0]=new String[] {"检     斤     单","","","","","","","",""};
////		 ArrHeader[1]=new String[] {"大唐鲁北发电有限责任公司           "+chengmzsj+"                No：  "+piaojh+"   ","","","","","","","",""};
//		 ArrHeader[0]=new String[] {"发货单位",""+fahdw+"","","",""+shouhdw+"","","车号",""+cheh+"",""};
//		 ArrHeader[1]=new String[] {"装货地点","","","称毛重时间",""+chengmzsj+"","","称皮重时间",""+chengpzsj+"",""};
//		 ArrHeader[2]=new String[] {"    品       名","","","型号规格","  单位"," 原发数","  毛重","皮重","净重"};
//		 ArrHeader[3]=new String[] {""+pinm+"","","","空","吨",""+yuanfs+"",""+maoz+"",""+piz+"",""+jingz+""};
//		 ArrHeader[4]=new String[] {"备注",""+beiz+"","","","","","","",""};
//		 ArrHeader[5]=new String[] {"称毛重检斤员:"+chengmzjjy+"             称皮重检斤员："+chengpzjjy+"          收料人："+shoulr+"          复核人：空","","","","","","","",""};
//
//		 int ArrWidth[]=new int[] {54,120,54,54,54,54,54,54,54};
//		if(chengmzsj==null||chengmzsj.equals("")||chengpzsj==null||chengpzsj.equals("")){
//			chengmzsj = "";
//			chengpzsj = "";
//		}
		String ArrHeader[][]=new String[5][8];
		ArrHeader[0]=new String[] {"发货单位",""+fahdw+"","收货单位",""+shouhdw+"","","车号",""+cheh+"",""};
		ArrHeader[1]=new String[] {"装货地点","","称毛重时间",""+chengmzsj+"","","称皮重时间",""+chengpzsj+""," "};
		ArrHeader[2]=new String[] {"    品       名","    品       名","型号规格","  单位"," 原发数","  毛重","皮重","净重"};
		ArrHeader[3]=new String[] {""+pinm+"","","","吨",""+yuanfs+"",""+maoz+"",""+piz+"",""+jingz+""};
		ArrHeader[4]=new String[] {"备注",""+beiz+"","","","","","",""};

		int ArrWidth[]=new int[] {58,120,58,58,58,58,58,58};



//		 定义页Title

		 Report rt=new Report();
		 rt.setTitle("检斤单",ArrWidth);
		 rt.setDefaultTitle(1,2,""+shouhdw+"",Table.ALIGN_LEFT);
		 rt.setDefaultTitle(3,3,""+chengmzsj+"",Table.ALIGN_CENTER);
		 rt.setDefaultTitle(6,3,"NO:"+piaojh+"",Table.ALIGN_RIGHT);
		 rt.setBody(new Table(ArrHeader,0,0,0));
//		 合并单元格
		 rt.body.mergeCell(1,4,1,5);
		 rt.body.mergeCell(2,4,2,5);
		 rt.body.mergeCell(3,1,3,2);
		 rt.body.mergeCell(4,1,4,2);
		 rt.body.mergeCell(5,2,5,8);
		 rt.body.mergeCell(1,7,1,8);
		 rt.body.mergeCell(2,7,2,8);
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1, 2,"称毛重检斤员："+chengmzjjy+"",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(3,3,"称皮重检斤员："+chengpzjjy+"",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(6,2," 收料人："+shoulr+"",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(8,1,"复核人:",Table.ALIGN_LEFT);
		return rt.getAllPagesHtml();
//		} 
 * 
 */
		int fontSize=9;
		String zhongcjjy = "";
		String qingcjjy = "";
		Date date = new Date();
		int year = DateUtil.getYear(date);
		int month = DateUtil.getMonth(date);
		int day	= DateUtil.getDay(date);
		String data[][] = new String[6][8];

		if (rsl.next()) {
			data[0][2] = ""+year;
			data[0][3] = ""+month;
			data[0][4] = ""+day;
			data[0][6] = rsl.getString("PIAOJH");
			data[1][1] = rsl.getString("fahdw");
			data[1][3] = "大唐鲁北发电公司";
			data[1][6] = rsl.getString("cheph");
			data[2][3] = rsl.getString("zhongcsj");
			data[2][6] = rsl.getString("qingcsj");
			data[3][0] = rsl.getString("pingm");
			data[3][3] = "吨";
			data[3][4] = rsl.getString("yuanmz");
			data[3][5] = rsl.getString("maoz");
			data[3][6] = rsl.getString("piz");
			data[3][7] = rsl.getString("jingz");
			zhongcjjy = rsl.getString("zhongcjjy");
			qingcjjy= rsl.getString("qingcjjy");
		}
		Report rt = new Report();
		int[] ArrWidth = new int[] { 80 ,130,90,50,80,80,65,65};

		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 2, zhongcjjy, Table.ALIGN_RIGHT);
		rt.setDefautlFooter(3, 2, qingcjjy, Table.ALIGN_RIGHT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;

		rt.setBody(new Table(data, 0, 0, 0));
		
		rt.body.setColWidth(1, 80);
		rt.body.setColWidth(2, 130);
		rt.body.setColWidth(3, 90);
		rt.body.setColWidth(4, 50);
		rt.body.setColWidth(5, 80);
		rt.body.setColWidth(6, 80);
		rt.body.setColWidth(7, 65);
		rt.body.setColWidth(8, 65);

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);

		rt.body.mergeCell(1, 7, 1, 8);
		rt.body.mergeCell(2, 4, 2, 5);
		rt.body.mergeCell(2, 7, 2, 8);
		rt.body.mergeCell(2, 4, 2, 5);
		rt.body.mergeCell(3, 4, 3, 5);
		rt.body.mergeCell(3, 7, 3, 8);
		rt.body.mergeCell(4, 1, 4, 2);
		rt.body.mergeCell(5, 1, 5, 2);
		rt.body.mergeCell(6, 2, 6, 8);
		rt.body.setBorderNone(); 
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_LEFT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_TOP, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_FONTSIZE, 12);
		rt.setPageRows(1);
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(30);
		rt.body.setFontSize(fontSize);
		return rt.getAllPagesHtml();
	}

	public void setTitle(Table title, String strTitle, int iStartCol,
			int iEndCol, int iRow, int iAlign) {
		title.setCellValue(iRow, iStartCol, strTitle);
		title.setCellAlign(iRow, iStartCol, iAlign);
		// title.setCellAlign(2,2,Table.ALIGN_CENTER);
		title.setCellFont(iRow, iStartCol, "", 10, false);
		title.mergeCell(iRow, iStartCol, iRow, iEndCol);
		//		title.merge(iRow, iEndCol + 1, iRow, title.getCols());
	}

	public void getSelectData() {
		// Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		ToolbarButton rbtn = new ToolbarButton(null, "返回",
				"function(){document.getElementById('RefurbishButton').click();window.close();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Return);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
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
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		return getToolbar().getRenderScript();
	}

	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setString10(visit.getActivePageName());
			//visit.setActivePageName(getPageName().toString());
			setTbmsg(null);
		}

		getSelectData();
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
			Visit visit = (Visit) getPage().getVisit();
			cycle.activate(visit.getString10());
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