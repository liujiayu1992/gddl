package com.zhiren.dc.huaygl.huaybb.huaybgd;

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


public class Huaybgdlb_hs_mx extends BasePage {


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

	private String getCaiySql(){
		/*String riq=this.getBRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}*/
		Visit visit = (Visit) getPage().getVisit();
		String	sSql =  "select decode(y.caiyrq,null,' ',TO_CHAR(Y.CAIYRQ, 'YYYY-MM-DD')) AS CAIYRQ,\n"
				+ "       decode(ls.lury,null,' ',ls.lury) as lurry,\n"
				+ "       TO_CHAR(ls.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,\n"
				+ "       round_new(ls.mt,2) as MT,round_new(ls.mad,2) as MAD,\n"
				+ "       round_new(ls.vdaf,2) as VDAF,round_new(ls.stad,2) as STAD,\n"
				+ "       round_new(ls.aad,2) as AAD,\n"
				+ "       round_new(ls.qbad,2) as QBAD,\n"
				+ "       round_new(ls.qgrad,2) as QGRAD,\n"
				+ "       round_new(ls.qnet_ar,2) as QNETAR,\n"
				+ "       decode(ls.huayy,null,' ',ls.huayy) as huayy,ls.shenhry as shenhry,ls.shenhryej as shenhry2,a.kaissj,a.jiessj,\n"
				//+ "       '"+this.getBianhValue().getValue()+"' as bianh,a.meikdwmc,a.chez,a.pinz,a.cheph,a.ches,a.meil,ls.beiz,a.heth from zhilb z,caiyb y,zhillsb ls,\n"
				+ "       "+visit.getString11()+" as bianh,a.meikdwmc,a.chez,a.pinz,a.cheph,a.ches,a.meil,ls.beiz,a.heth from zhilb z,caiyb y,zhillsb ls,\n"
				+ "    (select distinct m.mingc as meikdwmc,\n"
				+ "    cz.mingc as chez,\n"
				+ "    p.mingc as pinz,\n"
				+ "     f.zhilb_id as zhilb_id,min(c.zhongcsj) as kaissj,max(c.qingcsj) as jiessj,\n"
				+ "    round_new(sum(c.maoz-c.piz-c.koud), 2) as meil,max(h.hetbh) as heth,\n"
				+ "    count(c.id) AS CHES,\n"
				+ "    GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH\n"
				+ "    from fahb f, zhilb z,meikxxb m,chezxxb cz,pinzb p,hetb h,chepb c,zhillsb ls\n"
				+ "    where z.id = f.zhilb_id\n"
				+ "     and f.pinzb_id=p.id\n"
				+ "    and f.faz_id=cz.id\n"
				+ "     and f.hetb_id=h.id(+)\n"
				+ "    and f.meikxxb_id=m.id\n"
				+ "    and f.id=c.fahb_id\n"
				+ "    and z.id=ls.zhilb_id\n"
				+ "    and ls.id ="+visit.getString1()+"\n"
				+ "    group by m.mingc,cz.mingc,p.mingc,f.zhilb_id) a\n"
				+ "where z.id=a.zhilb_id\n"
				+ "and z.caiyb_id=y.id\n"
				+ "and y.zhilb_id=z.id\n"
				+ "and z.id=ls.zhilb_id\n"
				+ "and ls.id ="+visit.getString1()+"\n";
				



		return sSql;
	}
	private String getCaizrSql(){
		
		Visit visit = (Visit) getPage().getVisit();
		String	sSql =  
			"select distinct r.quanc as caizr,yp.beiz from yangpdhb yp,zhillsb ls,caiyryglb gl,renyxxb r\n" +
			"    where yp.zhilblsb_id=ls.id\n" + 
			"    and gl.yangpdhb_id=yp.id\n" + 
			"    and gl.renyxxb_id=r.id\n" + 
			"    and ls.id="+visit.getString1()+"";



		return sSql;
	}
	

	public String getPrintTable() {
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		String sql = this.getCaiySql();
		ResultSetList rs = con.getResultSetList(sql);
		String caiyrq="";
		String huayrq="";
		String Mt="";
		String Mad="";
		String Vdaf="";
		String Stad="";
		String Aad="";
		String Qbad="";
		String Qgrad="";
		String Qnetar="";
		String huayy="";
		String shenhry="";
		String huaybh="";
		String meik="";
		String faz="";
		String cheph1="";
		String ches="";
		String meil="";
		String heth="";
		String kaissj="";
		String jiessj="";
		String shenhry1="";
		String shenhry2="";
		

		while(rs.next()){
			caiyrq = rs.getString("CAIYRQ");
			huayrq = rs.getString("HUAYRQ");
			Mt =String.valueOf( rs.getDouble("MT"));
			Mad=String.valueOf(rs.getDouble("MAD"));
			Vdaf=String.valueOf(rs.getDouble("VDAF"));
			Stad=String.valueOf(rs.getDouble("STAD"));
			 Aad=String.valueOf(rs.getDouble("AAD"));
			 Qbad=String.valueOf(rs.getDouble("QBAD"));
			 Qgrad=String.valueOf(rs.getDouble("QGRAD"));
			 Qnetar=String.valueOf(rs.getDouble("QNETAR"));
			 huayy=rs.getString("huayy");
			 shenhry=rs.getString("shenhry");;
			 huaybh=rs.getString("bianh");
			 meik=rs.getString("meikdwmc");
			 faz=rs.getString("chez");
			 cheph1=rs.getString("cheph");
			 ches=String.valueOf(rs.getLong("ches"));
			 meil=String.valueOf(rs.getDouble("meil"));
			 heth=rs.getString("heth");
			 kaissj=rs.getDateTimeString("kaissj");
			 jiessj=rs.getDateTimeString("jiessj");
			 shenhry1=rs.getString("shenhry");
			 shenhry2=rs.getString("shenhry2");
			 
		}
		if(cheph1!=null||cheph1.equals("")){
			StringBuffer buffer = new StringBuffer();
			String[] list = cheph1.split(",");				
			for (int i = 1; i <= list.length; i++) {
				if (i % 8 == 0) {
					buffer.append(list[i - 1] + ",<br>");
				} else {
					buffer.append(list[i - 1] + ",");
				}
			}
			cheph1 = buffer.toString().substring(0, buffer.length() - 1);	
			
		}
		if(jiessj.equals("1970-01-01 08:00:00")||jiessj==null){
			jiessj=kaissj;
		}
		
		
		String sql_czr = this.getCaizrSql();
		rs = con.getResultSetList(sql_czr);
		String caizr = "";
		String beiz="";
		while(rs.next()){
			caizr += "、";
			caizr += rs.getString("CAIZR");
			beiz=rs.getString("beiz");
		}
		if(!(caizr.equals(null)||caizr.equals(""))){
			caizr = caizr.substring(1);
		}else{
			caizr = " ";
		}
		
		
		
		
		

		String[][] CAIY=new String[][]{
			{"煤样编号","化验日期","化验日期","采制员","采制员","采制员","化验员","化验员","化验员"},
			{huaybh,huayrq,huayrq,caizr,caizr,caizr,huayy,huayy,huayy,},
			{"发站","发站","发站","采样日期","采样日期","采样日期","车数","煤量","煤量"},
			{faz,faz,faz,caiyrq,caiyrq,caiyrq,ches,meil,meil},
			{"合同编号",heth,heth,"矿别:",meik,meik,meik,meik,meik},
			{"过衡开始",kaissj,kaissj,kaissj,"过衡结束时间","过衡结束时间",jiessj,jiessj,jiessj},
			{"分析项目","水份(%)","水份(%)","挥发(%)<br>Vdaf","灰分(%)<br>Aad","硫分(%)<br>Stad","热值(MJ/Kg)","热值(MJ/Kg)","热值(MJ/Kg)"},
			{"分析项目","Mt","Mad","挥发(%)<br>Vdaf","灰分(%)<br>Aad","硫分(%)<br>Stad","Qb,ad","Qgr,ad","Qnet,ar"},
			{"分析项目",Mt,Mad,Vdaf,Aad,Stad,Qbad,Qgrad,Qnetar},
			{"车号列表",cheph1,cheph1,cheph1,cheph1,cheph1,cheph1,cheph1,cheph1},
			{"备注","","","","","","","",""}
		};
		rs.close();
		
		String[][] ArrHeader = new String[11][9];
		int i=0;
		for(int j=0;j<CAIY.length;j++){
			ArrHeader[i++]=CAIY[j];
		}
		int[] ArrWidth = new int[] { 90,65,65,65,65,65,65,65,65};
		
		Table bt=new Table(11,9);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][9];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		rt.setTitle("燃料煤样化验单", ArrWidth);
		
		
		//合并
		rt.body.merge(1, 2, 1, 3);
		rt.body.merge(1, 4, 1, 6);
		rt.body.merge(1, 7, 1, 9);
		
		rt.body.merge(2, 2, 2, 3);
		rt.body.merge(2, 4, 2, 6);
		rt.body.merge(2, 7, 2, 9);
		
		rt.body.merge(3, 1, 3, 3);
		rt.body.merge(3, 4, 3, 6);
		rt.body.merge(3, 8, 3, 9);
		
		rt.body.merge(4, 1, 4, 3);
		rt.body.merge(4, 4, 4, 6);
		rt.body.merge(4, 8, 4, 9);
		
		rt.body.merge(5, 2, 5, 3);
		rt.body.merge(5, 5, 5, 9);
		
		rt.body.merge(6, 2, 6, 4);
		rt.body.merge(6, 5, 6, 6);
		rt.body.merge(6, 7, 6, 9);
		
		rt.body.merge(7, 2, 7, 3);
		rt.body.merge(7, 7, 7, 9);
		
		rt.body.merge(7, 1, 9, 1);
		rt.body.merge(7, 4, 8, 4);
		rt.body.merge(7, 5, 8, 5);
		rt.body.merge(7, 6, 8, 6);
		
		rt.body.merge(10, 2, 10, 9);
		
		rt.body.merge(11, 2, 11, 9);
		
		rt.body.setRowHeight(10, 75);
		
		
		
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(50);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		

		
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(5, 2, "一审:"+shenhry1, Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 3, "二审:"+shenhry2,Table.ALIGN_LEFT);
		rt.body.setCellAlign(5, 5, Table.ALIGN_LEFT);
		//rt.body.setCellVAlign(10, 2, Table.VALIGN_TOP);
		rt.body.setCells(10, 2, 10, 9, Table.PER_ALIGN, Table.ALIGN_LEFT);
		
		rt.body.setCellAlign(11, 2, Table.ALIGN_LEFT);
		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();
		/*JDBCcon con = new JDBCcon();
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
        	 ArrHeader = new String[][] {{"制样单号","供煤单位","发站","制样日期","合同编号","过衡时间","车数","车号"} };
    
    		 ArrWidth = new int[] {75, 100, 60, 80, 80, 80, 40, 200 };
    
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
		rt.body.setColAlign(8, Table.ALIGN_LEFT);
		

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
		rt.setDefautlFooter(6, 3, "人员:"+caizr, Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.fontSize=13;
		
		rt.body.fontSize=13;
		rt.body.cols[4].fontSize=10;
		rt.body.cols[5].fontSize=10;
		rt.body.cols[6].fontSize=10;
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
*/		
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