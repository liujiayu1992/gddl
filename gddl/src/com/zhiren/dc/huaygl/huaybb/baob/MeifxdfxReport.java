package com.zhiren.dc.huaygl.huaybb.baob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*作者:王总兵
 * 日期:2010-4-25 15:10:42
 * 描述:报表标题上面的单位名称由于在打印时还是和标题一样的大字体,所以挪到报表的左上角
 */
/**
 * @author yinjm
 * 类名：煤粉细度分析报表
 */

public class MeifxdfxReport extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	分析日期
	private String fenxrq;
	
	public String getFenxrq() {
		return fenxrq;
	}

	public void setFenxrq(String fenxrq) {
		this.fenxrq = fenxrq;
	}
	
//	炉号下拉框_开始
	public IDropDownBean getLuhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getLuhModel().getOptionCount() > 0) {
				setLuhValue((IDropDownBean) getLuhModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setLuhValue(IDropDownBean LuhValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LuhValue);
	}

	public IPropertySelectionModel getLuhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getLuhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setLuhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getLuhModels() {
		String sql = "select id, jizbh from jizb";
		setLuhModel(new IDropDownModel(sql));
	}
//	炉号下拉框_结束
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String sql = 
		"select m.zhifxtmc as mingc,formatxiaosws(m.r200,2) as r200," +
		"formatxiaosws(m.r90,2) as r90,formatxiaosws(m.r75,2) as r75 " +
		" from meifxdb m,jizb j\n" +
		"where m.jizb_id=j.id\n" + 
		"and m.fenxrq=to_date('"+ getFenxrq() +"', 'yyyy-mm-dd')\n" +
		"and j.id= "+ getLuhValue().getStrId() +" order by m.id\n" ;


		ResultSetList rslData =  con.getResultSetList(sql);
		
		String ArrBody[][] = new String[15][13];
		int[] ArrWidth = new int[] {140, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
		
		
		ArrBody[0] = new String[]{"取样时间及工况","","","","","","","","","","","",""};
		ArrBody[1] = new String[]{"炉号","","","","","","","","","","","",""};
		ArrBody[2] = new String[]{"制粉系统","","","","","","","","","","","",""};
		ArrBody[3] = new String[]{"项目","","","","","","","","","","","",""};
		ArrBody[4] = new String[]{"R<sub>200</sub>(%)","","","","","","","","","","","",""};
		ArrBody[5] = new String[]{"R<sub>90</sub>(%)","","","","","","","","","","","",""};
		ArrBody[6] = new String[]{"R<sub>75</sub>(%)","","","","","","","","","","","",""};
		ArrBody[7] = new String[]{"取样时间及工况","","","","","","","","","","","",""};
		ArrBody[8] = new String[]{"炉号","","","","","","","","","","","",""};
		ArrBody[9] = new String[]{"制粉系统","","","","","","","","","","","",""};
		ArrBody[10] = new String[]{"项目","","","","","","","","","","","",""};
		ArrBody[11] = new String[]{"R<sub>200</sub>(%)","","","","","","","","","","","",""};
		ArrBody[12] = new String[]{"R<sub>90</sub>(%)","","","","","","","","","","","",""};
		ArrBody[13] = new String[]{"R<sub>75</sub>(%)","","","","","","","","","","","",""};
		ArrBody[14] = new String[]{"备注","","","","","","","","","","","",""};
		
		//rt.setTitle("<p class=\"normal\">"+ visit.getDiancqc() +"</p>" + "<br>煤 粉 细 度 分 析 报 表", ArrWidth);
		rt.setTitle("煤 粉 细 度 分 析 报 表", ArrWidth);
		rt.setBody(new Table(ArrBody, 0, 0, 0));
		rt.body.setWidth(ArrWidth);
		
		
		
		rt.body.setCellValue(1, 2, getQuyrq(con));
		rt.body.mergeCell(1, 2, 1, 13);
		rt.body.setCellAlign(1, 1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(1, 2, Table.ALIGN_CENTER);
		
		rt.body.setCellValue(2, 2, getLuhValue().getValue()+"炉");
		rt.body.mergeCell(2, 2, 2, 13);
		rt.body.setCellAlign(2, 1, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(2, 2, Table.ALIGN_CENTER);
		rt.body.setCellBorderbottom(2, 1, 0);
		
		rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		rt.body.setCellBorderbottom(3, 1, 0);
		
		rt.body.setCellAlign(5, 1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(6, 1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(7, 1, Table.ALIGN_CENTER);
		
		if(rslData.getRows()>12){
			rt.body.setCellValue(8, 2, getQuyrq(con));
		}
		
		rt.body.mergeCell(8, 2, 8, 13);
		rt.body.setCellAlign(8, 1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(8, 2, Table.ALIGN_CENTER);
		
		if(rslData.getRows()>12){
			rt.body.setCellValue(9, 2, getLuhValue().getValue()+"炉");
		}
		
		rt.body.mergeCell(9, 2, 9, 13);
		rt.body.setCellAlign(9, 1, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(9, 2, Table.ALIGN_CENTER);
		rt.body.setCellBorderbottom(9, 1, 0);
		
		rt.body.setCellAlign(10, 1, Table.ALIGN_CENTER);
		rt.body.setCellBorderbottom(10, 1, 0);
		
		rt.body.setCellAlign(12, 1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(13, 1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(14, 1, Table.ALIGN_CENTER);
		
		rt.body.setCellAlign(15, 1, Table.ALIGN_CENTER);
		rt.body.mergeCell(15, 2, 15, 13);
		rt.body.setRowHeight(15, 80);
		
		rt.body.setRowHeight(0, 25);
		rt.body.setRowHeight(1, 25);
		rt.body.setRowHeight(2, 25);
		rt.body.setRowHeight(6, 25);
		rt.body.setRowHeight(7, 25);
		rt.body.setRowHeight(8, 25);
		rt.body.setRowHeight(9, 25);
		rt.body.setRowHeight(13, 25);
		rt.body.setRowHeight(14, 25);
		
		
		if (rslData.getRows() > 0) {
			String[][] strData = new String[rslData.getRows()][4];
			
			int temp = 0;
			while(rslData.next()) {
				strData[temp][0] = rslData.getString("mingc");
				strData[temp][1] = rslData.getString("r200");
				strData[temp][2] = rslData.getString("r90");
				strData[temp][3] = rslData.getString("r75");
				temp ++;
			}
			
			int colIndex = 0;
			for (int j = 0; j < strData.length; j ++) {
				if (j < 12) {
					rt.body.setCellValue(3, 2 + j, getZhifxtmc(strData[j][0]));
					rt.body.setCellAlign(3, 2 + j, Table.ALIGN_CENTER);
					rt.body.setCellValue(5, 2 + j, strData[j][1]);
					rt.body.setCellAlign(5, 2 + j, Table.ALIGN_CENTER);
					rt.body.setCellValue(6, 2 + j, strData[j][2]);
					rt.body.setCellAlign(6, 2 + j, Table.ALIGN_CENTER);
					rt.body.setCellValue(7, 2 + j, strData[j][3]);
					rt.body.setCellAlign(7, 2 + j, Table.ALIGN_CENTER);
				} else {
					rt.body.setCellValue(10, 2 + colIndex, getZhifxtmc(strData[j][0]));
					rt.body.setCellAlign(10, 2 + colIndex, Table.ALIGN_CENTER);
					rt.body.setCellValue(12, 2 + colIndex, strData[j][1]);
					rt.body.setCellAlign(12, 2 + colIndex, Table.ALIGN_CENTER);
					rt.body.setCellValue(13, 2 + colIndex, strData[j][2]);
					rt.body.setCellAlign(13, 2 + colIndex, Table.ALIGN_CENTER);
					rt.body.setCellValue(14, 2 + colIndex, strData[j][3]);
					rt.body.setCellAlign(14, 2 + colIndex, Table.ALIGN_CENTER);
					colIndex ++;
				}
			}
			rt.body.setCellValue(15, 2, "<br>&nbsp;&nbsp;&nbsp;&nbsp;"+getBeiz(con));
			rt.body.setCellVAlign(15, 2, 1);
		}
		
		for (int i = 2; i <= 13; i ++) {
			rt.body.mergeCell(3, i, 4, i);
			rt.body.mergeCell(10, i, 11, i);
		}
		rt.body.ShowZero=true;
		rt.setDefaultTitle(1,3,"单位:"+visit.getDiancmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 3, "分析日期："+DateUtil.Formatdate("yyyy年MM月dd日", getDate(getFenxrq())), Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2, 2, "批准：秦晓林", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "审核： 焦瑾辉", Table.ALIGN_CENTER);
		rt.setDefautlFooter(10, 3, "分析：梁建芬", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel())
			.getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null, null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("分析日期："));
		DateField df = new DateField();
		df.setValue(getFenxrq());
		df.setId("Fenxrq");
		df.Binding("Fenxrq", "forms[0]");
//		df.setListeners("change:function(){document.forms[0].submit();}");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("炉号："));
		ComboBox luh_comb = new ComboBox();
		luh_comb.setTransform("Luh");
		luh_comb.setWidth(100);
		luh_comb.setListWidth(120);
		luh_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		luh_comb.setLazyRender(true);
		tbr.addField(luh_comb);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 * String转Date
	 * @param date
	 * @return
	 */
	public static Date getDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date newdate = new Date();
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			newdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newdate;
	}
	
	/**
	 * 返回制粉系统的名称，并在中间加上了换行 
	 * @param mingc
	 * @return
	 */
	public static String getZhifxtmc(String mingc) {
		
		StringBuffer sbstr = new StringBuffer();
		sbstr.append(mingc.substring(0, 2)).append("<br>").append(mingc.substring(2, mingc.length()));
		
		return sbstr.toString();
	}
	
	/**
	 * 获取取样日期
	 * @param con
	 * @return
	 */
	public String getQuyrq(JDBCcon con) {
		String quyrq = "";
		String sql = 
			
		

		"select distinct to_char(m.quyrq, 'yyyy-mm-dd') quyrq\n" +
		"from meifxdb m,jizb j\n" + 
		"where m.jizb_id=j.id\n" + 
		"and m.fenxrq=to_date('"+ getFenxrq() +"', 'yyyy-mm-dd')\n" + 
		" and j.id = "+ getLuhValue().getStrId() +"\n" ;


		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			quyrq = rsl.getString("quyrq");
		}
		rsl.close();
		return quyrq; 
	}
	
	/**
	 * 获取备注信息
	 * @param con
	 * @return
	 */
	public String getBeiz(JDBCcon con) {
		String beiz = "";
		String sql = 

			"select distinct m.beiz \n" +
			"from meifxdb m,jizb j\n" + 
			"where m.jizb_id=j.id\n" + 
			"and m.fenxrq=to_date('"+ getFenxrq() +"', 'yyyy-mm-dd')\n" + 
			" and j.id = "+ getLuhValue().getStrId() +"\n" ;


		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			beiz = rsl.getString("beiz");
		}
		rsl.close();
		return beiz; 
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
	
//	电厂树_开始
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂树_结束

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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel3(null); // 炉号下拉框
			visit.setDropDownBean3(null);
			setFenxrq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}