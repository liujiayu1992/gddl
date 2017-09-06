package com.zhiren.dc.jilgl.baob;

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


public class Caiydlb_cx extends BasePage {


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

	public String getBaseSql(String Caiybh) {
		Visit visit = (Visit) getPage().getVisit();
		//JDBCcon con = new JDBCcon();
		//StringBuffer sql = new StringBuffer();
		
		
		StringBuffer sbsql = new StringBuffer();
				/*sbsql.append("select max(zm.bianm) as bianm,\n");
				sbsql.append("       max(g.mingc) as gonghdw,\n");
				//sbsql.append("       max(m.mingc) as kuangb,\n");
				sbsql.append("       max(cz.mingc) as faz,\n");
				sbsql.append("       max(to_char(cy.caiyrq,'yyyy-mm-dd')) as zhiyrq,\n");
				sbsql.append("       max(h.hetbh) as heth,\n");
				sbsql.append("       min(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss')) as zhongcsj,\n");
				sbsql.append("       count(c.id) as ches,\n");
				sbsql.append("       GetCaiyCheph(max(f.zhilb_id)) as cheh\n");
				sbsql.append(" from fahb f ,chepb c,gongysb g,meikxxb m,chezxxb cz,hetb h,zhillsb zl,caiyb cy,zhuanmb zm\n");
				sbsql.append(" where f.id=c.fahb_id\n"); 
				sbsql.append(" and f.gongysb_id=g.id\n");
				sbsql.append(" and f.meikxxb_id=m.id\n");
				sbsql.append(" and f.faz_id=cz.id\n");
				sbsql.append(" and f.hetb_id=h.id(+)\n");
				sbsql.append(" and f.zhilb_id=zl.zhilb_id\n");
				sbsql.append(" and f.zhilb_id=cy.zhilb_id\n");
				sbsql.append(" and zm.zhillsb_id=zl.id\n");
				sbsql.append(" and zm.zhuanmlb_id=(select id from zhuanmlb where jib=1)\n");
				sbsql.append(" and zm.bianm in ("+Caiybh+")\n");
				sbsql.append(" group by (f.zhilb_id)\n"); 
				sbsql.append(" order by max(zm.bianm)");
*/
		
			/*	sbsql.append("select distinct\n");
				sbsql.append("       c.caiybm as bianm,\n");
				sbsql.append("       (select mingc from meikxxb where id = s.meikxxb_id) as kuangb,\n");
				sbsql.append("         (select mingc from chezxxb where id = (select faz_id from fahb where id = s.id)) as faz,\n");
				sbsql.append("         to_char(cy.caiyrq,'yyyy-mm-dd') as zhiyrq,  (select s.huaylb from zhillsb s where id = s.zid) as huaylb,\n");
				sbsql.append("         (select h.hetbh from hetb h where h.id=s.hetb_id) as heth,\n");
				sbsql.append("         (select max(to_char(c.zhongcsj,'yyyy-hh-dd hh24:mi:ss')) from chepb c where c.fahb_id=s.id) as zhongcsj,\n");
				sbsql.append("\n");
				sbsql.append("        (select ches from fahb where id = s.id) as ches,\n");
				sbsql.append("       GetCaiyCheph(s.zhilb_id) as cheh\n");
				sbsql.append("  from (select bianm as caiybm, zhillsb_id\n");
				sbsql.append("          from zhuanmb\n");
				sbsql.append("         where zhillsb_id in\n");
				sbsql.append("               (select zm.zhillsb_id as id\n");
				sbsql.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sbsql.append("                 where zm.zhuanmlb_id = lb.id\n");
				sbsql.append("                   and lb.jib = 1\n");
				sbsql.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sbsql.append("                   and f.zhilb_id = z.zhilb_id\n");
				sbsql.append("                   and z.id = zm.zhillsb_id)\n");
				sbsql.append("           and zhuanmlb_id =\n");
				sbsql.append("               (select id from zhuanmlb where mingc = '采样编码')) c,\n");
				sbsql.append("       (select distinct f.id,f.pinzb_id,\n");
				sbsql.append("                        f.diancxxb_id,\n");
				sbsql.append("                        f.gongysb_id,\n");
				sbsql.append("                        f.meikxxb_id,\n");
				sbsql.append("                        f.hetb_id,\n");
				sbsql.append("                        z.id as zid,\n");
				sbsql.append("                        f.zhilb_id\n");
				sbsql.append("          from zhillsb z, fahb f, chepb c,pinzb p\n");
				sbsql.append("         where f.zhilb_id = z.zhilb_id\n");
				sbsql.append("            and f.pinzb_id=p.id\n");
				sbsql.append("           and c.fahb_id = f.id\n");
				sbsql.append("             ) s,caiyb cy\n");
				sbsql.append(" where\n");
				sbsql.append("    c.zhillsb_id = s.zid\n");
				sbsql.append(" and s.zhilb_id=cy.zhilb_id\n");
				sbsql.append(" and c.caiybm in ("+Caiybh+")\n");
				sbsql.append("   order by kuangb,faz,ches,caiybm");*/
		
		
		
				sbsql.append("select distinct\n");
				sbsql.append("       c.caiybm as bianm,\n");
				sbsql.append("       (select mingc from meikxxb where id = s.meikxxb_id) as kuangb,\n");
				sbsql.append("         (select mingc from chezxxb where id = s.faz_id) as faz,\n");
				sbsql.append("         to_char(cy.caiyrq,'yyyy-mm-dd') as zhiyrq,  (select s.huaylb from zhillsb s where id = s.zid) as huaylb,\n");
				sbsql.append("         (select h.hetbh from hetb h where h.id=s.hetb_id) as heth,\n");
				sbsql.append("        to_char(s.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),s.ches,\n");
				sbsql.append("       GetCaiyCheph(s.zhilb_id) as cheh\n");
				sbsql.append("  from (select bianm as caiybm, zhillsb_id\n");
				sbsql.append("          from zhuanmb\n");
				sbsql.append("         where zhillsb_id in\n");
				sbsql.append("               (select zm.zhillsb_id as id\n");
				sbsql.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sbsql.append("                 where zm.zhuanmlb_id = lb.id\n");
				sbsql.append("                   and lb.jib = 1\n");
				sbsql.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sbsql.append("                   and f.zhilb_id = z.zhilb_id\n");
				sbsql.append("                   and z.id = zm.zhillsb_id)\n");
				sbsql.append("           and zhuanmlb_id =\n");
				sbsql.append("               (select id from zhuanmlb where mingc = '采样编码')) c,\n");
				sbsql.append("             (select ls.id as zid,ls.zhilb_id,ww.gongys_id,ww.meikxxb_id,ww.hetb_id,ww.pinzb_id,ww.ches,ww.faz_id,\n");
				sbsql.append("ww.fahrq,ww.zhongcsj\n");
				sbsql.append("from\n");
				sbsql.append("(select f.zhilb_id,max(f.gongysb_id) as gongys_id,max(f.meikxxb_id) as meikxxb_id,max(f.hetb_id) as hetb_id,\n");
				sbsql.append("max(f.pinzb_id) as pinzb_id,count(c.id) as ches,max(f.faz_id) as faz_id,min(f.fahrq) as fahrq,min(c.zhongcsj) as zhongcsj\n");
				sbsql.append("from fahb f,chepb c\n");
				sbsql.append("where f.id=c.fahb_id\n");
				sbsql.append(" group by (f.zhilb_id)) ww,zhillsb ls\n");
				sbsql.append(" where ww.zhilb_id=ls.zhilb_id) s, caiyb cy\n");
				sbsql.append(" where c.zhillsb_id = s.zid\n");
				sbsql.append(" and s.zhilb_id=cy.zhilb_id\n");
				sbsql.append(" and c.caiybm in ("+Caiybh+")\n");
				sbsql.append("   order by kuangb,faz,ches,caiybm");


		
		
		
		return sbsql.toString();
	}
	
	public String getCaiyry(String Caiybh ){
		
		
		StringBuffer sb = new StringBuffer();

				sb.append( "select distinct r.quanc as caizr\n");
				sb.append("from fahb f,meikxxb m,hetb h,chepb c,gongysb g,caiyb cy,zhillsb z,zhuanmb zm,yangpdhb y,caiyryglb cgl,renyxxb r\n");
				sb.append("where f.meikxxb_id = m.id\n");
				sb.append("      and f.hetb_id = h.id(+)\n");
				sb.append("      and c.fahb_id = f.id\n");
				sb.append("      and f.gongysb_id = g.id\n");
				sb.append("      and f.zhilb_id = z.zhilb_id\n");
				sb.append("      and zm.zhillsb_id = z.id\n");
				sb.append("      and zm.zhuanmlb_id = 100661\n");
				sb.append("      and zm.bianm in ("+Caiybh+")\n");
				sb.append("      and cy.zhilb_id = f.zhilb_id\n");
				sb.append("      and y.caiyb_id = cy.id\n");
				sb.append("      and cgl.yangpdhb_id = y.id\n");
				sb.append("      and cgl.renyxxb_id = r.id");

		
		return sb.toString();
		
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
		
		ResultSetList rs1 = con.getResultSetList(getCaiyry(visit.getString1()));
		String caizr = "";
		while(rs1.next()){
			caizr += "、";
			caizr += rs1.getString("CAIZR");
			
		}
		if(!(caizr.equals(null)||caizr.equals(""))){
			caizr = caizr.substring(1);
		}else{
			caizr = " ";
		}
		
		
		
		
		
		
		
		
		
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;



        	rs = rsl;
        	 ArrHeader = new String[][] {{"制样单号","供煤单位","发站","制样日期","化验类别","合同编号","过衡时间","车数","车号"} };
    
    		 ArrWidth = new int[] {75, 100, 60, 80, 70,80, 80, 40, 200 };
    
    		rt.setTitle("燃料采制单列表", ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		
    		strFormat = new String[] { "", "", "", "", "", "","", ""};
    		rt.setTitle("燃料采制单列表", ArrWidth);

		

//
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);

		rt.setBody(new Table(rs, 1, 0, 3));
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_LEFT);
		

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		
		
		
		
//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "交样日期：" + DateUtil.Formatdate("yyyy-MM-dd HH:mm:ss", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 4, "人员:"+caizr, Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.fontSize=13;
		
		rt.body.fontSize=13;
		rt.body.cols[4].fontSize=10;
		rt.body.cols[5].fontSize=10;
		rt.body.cols[6].fontSize=10;
		rt.body.cols[7].fontSize=10;
		rt.footer.fontSize=13;
//		rt.footer.setRowHeight(1, 1);
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