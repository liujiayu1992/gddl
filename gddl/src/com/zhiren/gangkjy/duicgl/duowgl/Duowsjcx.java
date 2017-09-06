package com.zhiren.gangkjy.duicgl.duowgl;


/**
 * @author 张琦
 */
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Duowsjcx extends BasePage implements PageValidateListener {
	public List gridColumns;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	boolean riqichange = false;
	private String riqi; //页面起始日期日期选择
	
	
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
	
	
	
	private String riqi1; //页面起始日期日期选择
	
	
	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}

	public void setRiqi1(String riqi1) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi1)) {
			this.riqi1 = riqi1;
			riqichange = true;
		}
	}
	





	//刷新按钮
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		 
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {

		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {

		
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
			
			String sql = "select * from (\n" +
					"--明细\n" + 
					"(select to_char(d.riq,'yyyy-MM-dd') as riq,\n" + 
					"       v.mingc as duow,\n" + 
					"       decode(d.leix,1,d.fahr,null) as fahr,\n" + 
					"       decode(d.leix,1,d.chec,null) as chec,\n" + 
					"       decode(d.leix,1,d.pinz,null) as pinz_rk,\n" + 
					"       d.ruksl as ruksl,\n" + 
					"       decode(d.leix,-1,d.fahr,null) as chuanm,\n" + 
					"       decode(d.leix,-1,d.chec,null) as hangc,\n" + 
					"       decode(d.leix,-1,d.pinz,null) as pinz_ck,\n" + 
					"       d.chuksl as chuksl,\n" + 
					"       null as yingk,\n" + 
					"       d.kucl as kuc\n" + 
					"from duowkcb d,vwduow v\n" + 
					"where d.meicb_id = v.id\n" + 
					"      and d.leix in (-1,1)\n" + 
					" 	   and d.riq>="+DateUtil.FormatOracleDate(getRiqi()) +
					" 	   and d.riq<"+DateUtil.FormatOracleDate(getRiqi1()) + "+1 \n" +
					")\n" + 
					"union\n" + 
					"--小计\n" + 
					"(select to_char(d.riq,'yyyy-MM-dd') as riq,\n" + 
					"       decode(grouping(v.mingc),1,'合计',v.mingc) as duow,\n" + 
					"       '小计' as fahr,\n" + 
					"       null as chec,\n" + 
					"       null as pinz_rk,\n" + 
					"       sum(d.ruksl) as ruksl,\n" + 
					"       null as chuanm,\n" + 
					"       null as hangc,\n" + 
					"       null as pinz_ck,\n" + 
					"       sum(d.chuksl) as chuksl,\n" + 
					"       sum(d.panyk) as yingk,\n" + 
					"       sum(d.kucl) as kuc\n" + 
					"from duowkcb d,vwduow v\n" + 
					"where d.meicb_id = v.id\n" + 
					"      and d.leix = 2\n" + 
					" 	   and d.riq>="+DateUtil.FormatOracleDate(getRiqi()) +
					" 	   and d.riq<"+DateUtil.FormatOracleDate(getRiqi1()) + "+1 \n" +
					"group by rollup (d.riq,v.mingc)\n" + 
					"having not (grouping(d.riq)) = 1\n" + 
					")\n" + 
					"union\n" + 
					"--合计\n" + 
					"(select distinct '总计' as riq,\n" + 
					"        v.mingc as duow,\n" + 
					"        null as fahr,\n" + 
					"        null as chec,\n" + 
					"        null as pinz_rk,\n" + 
					"        sum(d.ruksl) as ruksl,\n" + 
					"        null as chuanm,\n" + 
					"        null as hangc,\n" + 
					"        null as pinz_ck,\n" + 
					"        sum(d.chuksl) as chuksl,\n" + 
					"        sum(panyk) as yingk,\n" + 
					"        sum(\n" + 
					"        decode(d.id,(select max(id) from duowkcb where meicb_id = d.meicb_id and d.leix=2\n" + 
					" 	     and riq>="+DateUtil.FormatOracleDate(getRiqi()) +
					" 	     and riq<"+DateUtil.FormatOracleDate(getRiqi1()) + "+1 \n" +
					"        ),d.kucl,0)) as kuc\n" + 
					"from duowkcb d,vwduow v\n" + 
					"where d.meicb_id = v.id\n" + 
					" and d.leix = 2\n" + 
					" 	   and d.riq>="+DateUtil.FormatOracleDate(getRiqi()) +
					" 	   and d.riq<"+DateUtil.FormatOracleDate(getRiqi1()) + "+1 \n" +
					"group by rollup (v.mingc)\n" + 
					")\n" + 
					")\n" + 
					"order by riq, decode(duow,'合计',2,null,3,1), decode(fahr,null,2,'小计',3,-1)";
			


		
				ResultSetList rsl = con.getResultSetList(sql);
				ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

				egu.setWidth(Locale.Grid_DefaultWidth);
				egu.getColumn("riq").setHeader(Local.riq);
				egu.getColumn("riq").setWidth(100);
				egu.getColumn("duow").setHeader(Local.duow);
				egu.getColumn("duow").setWidth(80);
				egu.getColumn("fahr").setHeader(Local.fahr);
				egu.getColumn("fahr").setWidth(80);
				egu.getColumn("chec").setHeader(Local.chec);
				egu.getColumn("chec").setWidth(80);
				egu.getColumn("pinz_rk").setHeader(Local.pinz);
				egu.getColumn("pinz_rk").setWidth(80);
				egu.getColumn("ruksl").setHeader(Local.chuanm_zhuangcb);
				egu.getColumn("ruksl").setWidth(80);
				egu.getColumn("chuanm").setHeader(Local.fahr);
				egu.getColumn("chuanm").setWidth(80);
				egu.getColumn("hangc").setHeader(Local.chuanc_duic);
				egu.getColumn("hangc").setWidth(80);
				egu.getColumn("pinz_ck").setHeader(Local.pinz);
				egu.getColumn("pinz_ck").setWidth(100);
				
				egu.getColumn("chuksl").setHeader(Local.shul_duic);
				egu.getColumn("chuksl").setWidth(70);
				egu.getColumn("yingk").setHeader(Local.yingk);
				egu.getColumn("yingk").setWidth(80);
				egu.getColumn("kuc").setHeader(Local.kuc);
				egu.getColumn("kuc").setWidth(80);
				
				// 工具日期下拉框
				egu.addTbarText("起始时间:");
				DateField df = new DateField();
				df.setValue(this.getRiqi());
				df.setReadOnly(true);
				df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
				df.setId("riqi");
				egu.addToolbarItem(df.getScript());
				egu.addTbarText("|");
				egu.addTbarText("结束时间:");
				DateField df1 = new DateField();
				df1.setValue(this.getRiqi1());
				df1.setReadOnly(true);
				df1.Binding("RIQI1", "forms[0]");// 与html页中的id绑定,并自动刷新
				df1.setId("riqi1");
				egu.addToolbarItem(df1.getScript());

//				查询按钮
				GridButton gbtr = new GridButton("查询","function(){document.getElementById('RefurbishButton').click();}");
				gbtr.setIcon(SysConstant.Btn_Icon_Refurbish);
				egu.addTbarBtn(gbtr);
				egu.setGridType(ExtGridUtil.Gridstyle_Read);
				egu.addPaging(25);
				setExtGrid(egu);
				con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
		}
		getSelectData();
	}

}



