package com.zhiren.gangkjy.jiexdy.xiemgl;

import java.sql.ResultSet;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

 
public class Xiemxxcx extends BasePage {

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
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		
		String strDiancID = "";
		if(visit.isJTUser()){
			strDiancID = "";
		}else if(visit.isGSUser()){
			strDiancID = " and (dc.id="+visit.getDiancxxb_id()+" or dc.fuid="+visit.getDiancxxb_id()+")";
		}else{
			strDiancID = " and dc.id="+visit.getDiancxxb_id();
		}
		
		String sqlRenwbb ="select k.fhdw fhdw,k.pinz pinz,k.daohrq daohrq,k.faz faz,k.chec chec,k.ches ches,k.biaoz biaoz\n" +
			 "       ,k.maoz maoz,k.piz piz,k.fahrq fahrq,k.guohsj guohsj  from\n" + 
			 "(select grouping(g.mingc) as g1, grouping(p.mingc) as p1,grouping(f.daohrq) d1,\n" + 
			 "       decode(grouping(g.mingc),1,'总计',g.mingc) as fhdw,\n" + 
			 "       decode(grouping(f.daohrq)-grouping(g.mingc),1,'小计',p.mingc) pinz,\n" + 
			 "        to_char(f.daohrq,'yyyy-mm-dd') daohrq,\n" + 
			 "        c.mingc faz,f.chec chec, sum(f.ches) ches,\n" + 
			 "        sum(round_new(f.biaoz,0)) biaoz ,\n" + 
			 "        sum(round_new(f.maoz,0)) maoz,\n" + 
			 "        sum(round_new(f.piz,0)) piz,\n" + 
			 "        to_char(f.fahrq,'yyyy-mm-dd') fahrq," +
			 "		  to_char(f.guohsj,'yyyy-mm-dd HH24:mi:ss') guohsj\n" + 
			 "        from vwpinz p,fahb f,vwfahr g,vwchez c,diancxxb dc\n" + 
			 "        where f.gongysb_id=g.id and f.pinzb_id=p.id and f.faz_id=c.id\n" + 
			 "             and f.diancxxb_id=dc.id and \n" + 
			 "             f.daohrq >= to_date('"+this.getBeginRiq()+"','yyyy-mm-dd')\n" + 
			 "             and f.daohrq < to_date('"+this.getEndRiq()+"','yyyy-mm-dd')+1 and f.yewlxb_id=1 \n" + 
			 strDiancID+
			 "             group by rollup(g.mingc,p.mingc,f.daohrq,c.mingc,f.chec,f.fahrq,f.guohsj)\n" + 
			 "             having grouping(p.mingc)=1 or grouping(f.guohsj)=0" +
			 "		 order by g1 desc,g.mingc,p1 desc,p.mingc,d1 desc,f.daohrq) k";
		
		ResultSet rs = con.getResultSet(sqlRenwbb);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
//		/********************设置表头********************/
		String ArryHeader[][]=new String[1][11];
		ArryHeader[0]=new String[]{"发货人","品种","到货日期","发站","车次","车数","票重(吨)","毛重(吨)","皮重","发货日期","过衡日期"};
		//设置表宽
		int ArryWidth[]=new int[11];
		ArryWidth=new int[]{100,40,70,60,45,45,45,45,45,80,120};
		rt.setTitle("卸煤信息查询", ArryWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 4, "制表单位: "+visit.getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 7, "到货日期："+getBeginRiq() + " 至 " + getEndRiq(), Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		
		String[] strFormat = new String[]{"","","","","","","","","","",""};
		
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArryWidth);
		rt.body.setHeaderData(ArryHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.setColFormat(strFormat);
		
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArryWidth);

		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
//		rt.body.setRowHeight(43);

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
			getPrintTable();
		}
	}

	
	public void getSelectData(){
	    Toolbar tb1 = new Toolbar("tbdiv");
	    tb1.addText(new ToolbarText("到货日期:"));
	    DateField renwkssj = new DateField();
		renwkssj.setValue(this.getBeginRiq());
		renwkssj.setId("BeginRiq");
		renwkssj.Binding("RIQ", "");
		tb1.addField(renwkssj);
		
		tb1.addText(new ToolbarText("至:"));
		DateField renwjssj = new DateField();
		renwjssj.setValue(this.getEndRiq());
		renwjssj.setId("EndRiq");
		renwjssj.Binding("RIQ1","");// 与html页中的id绑定,并自动刷新
		tb1.addField(renwjssj);
			
		
//     设置按钮
		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
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
		((DateField) getToolbar().getItem("BeginRiq")).setValue(getBeginRiq());
		((DateField) getToolbar().getItem("EndRiq")).setValue(getEndRiq());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString2("");
			getSelectData();
		}

	}
	

	// 绑定日期
	 public String getBeginRiq() {
			if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
				
				((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString1();
		}
	   public void setBeginRiq(String riq) {
				((Visit) this.getPage().getVisit()).setString1(riq);
		}
	   
	   public String getEndRiq() {
			if (((Visit) this.getPage().getVisit()).getString2() == null || ((Visit) this.getPage().getVisit()).getString2().equals("")) {
				
				((Visit) this.getPage().getVisit()).setString2(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString2();
		}
	  public void setEndRiq(String riq) {
				((Visit) this.getPage().getVisit()).setString2(riq);
		}
	  
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
			
}
