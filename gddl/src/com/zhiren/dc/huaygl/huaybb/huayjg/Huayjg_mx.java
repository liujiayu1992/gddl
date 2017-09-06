package com.zhiren.dc.huaygl.huaybb.huayjg;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Huayjg_mx extends BasePage {

 
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
	 * 
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

	public String getBaseSql(String Zhiybh) {
		Visit visit = (Visit) getPage().getVisit();
		
		
		
		StringBuffer sbsql = new StringBuffer();

		
			


				sbsql.append("select TO_CHAR(l.huaysj, 'yyyy-mm-dd') AS huaysj,\n");
				sbsql.append("       TO_CHAR(z.zhiybm) AS zhiybh,\n");
				sbsql.append("       TO_CHAR(h.huaybm) AS huaybh,\n");
				sbsql.append("       l.mt,\n");
				sbsql.append("       l.mad,\n");
				sbsql.append("       l.vdaf,\n");
				sbsql.append("       l.aad,\n");
				sbsql.append("       l.stad,\n");
				sbsql.append("       l.qbad,\n");
				sbsql.append("       l.qgrad,\n");
				sbsql.append("       l.qnet_ar,\n");
				sbsql.append("       huayy\n");
				sbsql.append("  from zhillsb l,\n");
				sbsql.append("       (select bianm as zhiybm, zhillsb_id\n");
				sbsql.append("          from zhuanmb\n");
				sbsql.append("         where zhillsb_id in\n");
				sbsql.append("               (select zm.zhillsb_id as id\n");
				sbsql.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sbsql.append("                 where zm.zhuanmlb_id = lb.id\n");
				sbsql.append("                   and lb.jib = 2\n");
				sbsql.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sbsql.append("                   and f.zhilb_id = z.zhilb_id\n");
				sbsql.append("                   and z.id = zm.zhillsb_id)\n");
				sbsql.append("           and zhuanmlb_id =\n");
				sbsql.append("               (select id from zhuanmlb where mingc = '制样编码')) z,\n");
				sbsql.append("       (select bianm as huaybm, zhillsb_id\n");
				sbsql.append("          from zhuanmb\n");
				sbsql.append("         where zhillsb_id in\n");
				sbsql.append("               (select zm.zhillsb_id as id\n");
				sbsql.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sbsql.append("                 where zm.zhuanmlb_id = lb.id\n");
				sbsql.append("                   and lb.jib = 3\n");
				sbsql.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sbsql.append("                   and f.zhilb_id = z.zhilb_id\n");
				sbsql.append("                   and z.id = zm.zhillsb_id)\n");
				sbsql.append("           and zhuanmlb_id =\n");
				sbsql.append("               (select id from zhuanmlb where mingc = '化验编码')) h\n");
				sbsql.append(" where l.id = z.zhillsb_id\n");
				sbsql.append("   and l.id = h.zhillsb_id\n");
				sbsql.append("   AND z.zhiybm in ("+Zhiybh+")\n");
				sbsql.append("    order by l.huaysj,zhiybh");

		
		
		return sbsql.toString();
	}
	


	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getString1() == null || "".equals(visit.getString1())) {
			return "参数不正确";
		}
		ResultSetList rsl = con.getResultSetList(getBaseSql(visit.getString1()));
		if (rsl == null) {
			setMsg("数据获取失败！请检查您的网络状况！错误代码 XXCX-001");
			return "";
		}
		
	
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;



        	rs = rsl;
        	 ArrHeader = new String[][] {{"化验日期","制样编号","化验编号","Mt(%)","Mad(%)","Vdaf(%)","Aad(%)","Satd(%)","Qb,ad","Qg,rad","Qnet,ar","化验员"} };
    
    		 ArrWidth = new int[] {90, 70,70,45,45,45,45,45,50,50,50,100};
    		rt.setTitle("化验结果单", ArrWidth);

    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		
    		strFormat = new String[] { "", "","", "","", "","", "","", "","",""};
    		
		

//
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
	

		rt.setBody(new Table(rs, 1, 0, 3));
		for(int i=1; i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		
		

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(100);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();

		
		
		
		

		rt.createFooter(1,ArrWidth);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		/*rt.title.fontSize=11;
		
		rt.body.fontSize=11;
		
		rt.footer.fontSize=11;*/

		rt.body.ShowZero = true;
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		
     	return rt.getAllPagesHtml();// ph;
		
	}
	public void setTitle(Table title,String strTitle, int iStartCol, int iEndCol,
			int iRow, int iAlign) {
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
				"function(){document.getElementById('RefurbishButton').click();}");
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