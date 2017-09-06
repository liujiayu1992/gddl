package com.zhiren.shanxdted.meigzxbb;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Yunfjsb extends BasePage implements PageValidateListener {
	
	public boolean getRaw() {
		return true;
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
	
	public String getPrintTable(){
		return getZhiltz();
	}

	private String getZhiltz() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select\n" +
				"      rownum, riq, mmingc, ymingc, ches, biaoz, jingz, shijsh, \n" +
				"	   round(decode(round(biaoz*yunsl,2),0,0,(biaoz-jingz)/round(biaoz*yunsl,2)),2), biaoz*yunsl,\n" + 
				"      round(biaoz*yunsl,2)-round(biaoz/jingz,2), --损差\n" + 
				"      decode(sign(round(biaoz*yunsl,2)-round(biaoz/jingz,2)),-1,round((round(biaoz*yunsl,2)-round(biaoz/jingz,2)*hansdj/1.17),2),0),\n" + 
				"      yunja,(hansdj/1.17+yunja*0.93)*7000/qnet_ar, xiax-qnet_ar,hansdj, yunja,xiax,qnet_ar,jiesrl,round(jingz*yunja,2)\n" + 
				" from (\n" + 
				"select min(to_char(f.daohrq,'yyyy-mm-dd'))||'~'||max(to_char(f.daohrq,'mm-dd')) riq, y.mingc ymingc, m.mingc mmingc,\n" + 
				"       sum(f.ches) ches, sum(f.biaoz) biaoz,sum(f.jingz) jingz,sum(f.biaoz)-sum(jingz) shijsh,\n" + 
				"       m.yunsl yunsl, jm.hansdj, yjg.yunja, yjg.xiax, round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2) qnet_ar, jm.jiesrl\n" + 
				"  from fahb     f,\n" + 
				"       jiesyfb  jy,\n" + 
				"       hetys    hy,\n" + 
				"       yunsdwb  y,\n" + 
				"       meikxxb  m,\n" + 
				"       jiesb    jm,\n" + 
				"       hetysjgb yjg,\n" + 
				"       zhilb z\n" + 
				" where f.hetys_id = jy.hetys_id(+)\n" + 
				"   and f.hetys_id = hy.id(+)\n" + 
				"   and hy.yunsdwb_id = y.id(+)\n" + 
				"   and f.meikxxb_id = m.id\n" + 
				"   and f.jiesb_id = jm.id\n" + 
				"   and jy.id = yjg.hetys_id(+)\n" + 
				"   and f.zhilb_id = z.id\n" + 
				"   and f.diancxxb_id in(" + this.getTreeid_dc() + ")\n" + 
				"   and jy.jiesrq>=to_date('" + getRiqi() + "','yyyy-mm-dd')\n" + 
				"   and jy.jiesrq<=to_date('" + getRiq2() + "','yyyy-mm-dd')\n" + 
				"group by jy.id, y.mingc, m.mingc, m.yunsl, jm.hansdj, yjg.yunja, xiax,jm.jiesrl\n" + 
				")");
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][23];

		ArrHeader[0] = new String[] {
				"序号","时间","运输队","矿名","车数",
				"矿发","实收","实际损耗","运损率","合理损耗",
				"损差","赔款金额","实际运价","标煤单价","热值差",
				"结算热值差","煤价","合同运价","合同热值","实际热值",
				"结算热值","实付金额","备注"};
		int[] ArrWidth = new int[23];

		ArrWidth = new int[] { 50, 60,90, 90, 60, 60, 60, 60, 60, 60, 60, 60, 60,
				60, 60, 60, 60, 60, 60, 60, 60, 60, 60};

		rt.setTitle("运 费 结 算 表", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
//		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setRiqi(null);
			this.setRiq2(null);
			
			getSelectData();
		}
		getSelectData();

	}
	
	// 绑定日期
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
		}

	}

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
		}
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			getZhiltz();
			_RefurbishChick = false;
		}

	}

    
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
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
		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid_dc(),null,true);

				setDCTree(etu);
				TextField tf = new TextField();
				tf.setId("diancTree_text");
				tf.setWidth(100);
				String[] str=getTreeid_dc().split(",");
				tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
				ToolbarButton toolb2 = new ToolbarButton(null, null,
						"function(){diancTree_window.show();}");
				toolb2.setIcon("ext/resources/images/list-items.gif");
				toolb2.setCls("x-btn-icon");
				toolb2.setMinWidth(20);
				
				tb1.addText(new ToolbarText("单位:"));
				tb1.addField(tf);
				tb1.addItem(toolb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);

		setToolbar(tb1);

	}
	
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
		String sql = "select id,mingc from diancxxb";
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
		if(((Visit) getPage().getVisit()).getString3()!=null && !((Visit) getPage().getVisit()).getString3().equals(treeid)){
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	public ExtTreeUtil getDCTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setDCTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getDCTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getDCTree().getWindowTreeHtml(this);
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

//	页面登陆验证
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