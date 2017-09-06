package com.zhiren.gdjh.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者：赵胜男
 * 时间：2012-11-08
 * 适用范围：国电电力
 * 描述：修正bug
 */
/*
 * 作者：赵胜男
 * 时间：2012-11-13
 * 适用范围：国电电力
 * 描述：处理页面bug
 */
/*
 * 作者：夏峥
 * 时间：2012-11-14
 * 适用范围：国电电力
 * 描述：处理页面bug
 */
/*
 * 作者：夏峥
 * 时间：2012-12-22
 * 适用范围：国电电力
 * 描述：调整界面宽度
 */ 
/*
 * 作者：赵胜男
 * 时间：2012-12-24
 * 描述：变更单位为多选电厂树
 */
/*
 * 作者：夏峥
 * 日期：2014-04-16
 * 描述：调整报表标题
 */
public class Niandrlzhbmdjcs extends BasePage implements PageValidateListener {

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

	private boolean _RefurbishChick = false; 

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	// 报表展示
	public String getPrintTable() {
		return getSelectData();
	}

	private String getSelectData() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String strDate=getNianfValue().getValue() +"-01-01";
		String strZBDate=getNianfValue().getValue() +"-11-01";
		String strSQL ="SELECT DECODE(GROUPING(DC.MINGC), 1, '合计', DC.MINGC) MK,\n" +
						"       NVL(SUM(NRL.FADL), 0) AS FDL,\n" + 
						"       NVL(SUM(NRL.FADBML), 0) AS FDBML,\n" + 
						"       NVL(SUM(NRL.GONGRL), 0) AS GRL,\n" + 
						"       NVL(SUM(NRL.GONGRBML), 0) AS GRBML,\n" + 
						"       NVL(SUM(NRL.BIAOMLHJ), 0) AS BMLHJ,\n" + 
						"       NVL(SUM(NRL.MEIZBML), 0) AS MZBML,\n" + 
						"       ROUND(DECODE(SUM(NRL.MEIZBML),\n" + 
						"                    0,\n" + 
						"                    0,\n" + 
						"                    SUM(NRL.MEIZBMDJ * NRL.MEIZBML) / SUM(NRL.MEIZBML)),\n" + 
						"             2) AS MZBMDJ,\n" + 
						"       NVL(SUM(NRL.RANYL), 0) AS RYL,\n" + 
						"       ROUND(DECODE(SUM(NRL.RANYL),\n" + 
						"                    0,\n" + 
						"                    0,\n" + 
						"                    SUM(NRL.RANYL * NRL.RANYDJ) / SUM(NRL.RANYL)),\n" + 
						"             2) AS RYDJ,\n" + 
						"       NVL(SUM(NRL.YOUZBML), 0) AS YZBML,\n" + 
						"       ROUND(DECODE(SUM(NRL.YOUZBML),\n" + 
						"                    0,\n" + 
						"                    0,\n" + 
						"                    SUM(NRL.YOUZBML * NRL.YOUZBMDJ) / SUM(NRL.YOUZBML)),\n" + 
						"             2) YZBMDJ,\n" + 
						"       NVL(SUM(NRL.QITFY), 0) AS QTFY,\n" + 
						"       ROUND(DECODE(SUM(NRL.BIAOMLHJ),\n" + 
						"                    0,\n" + 
						"                    0,\n" + 
						"                    SUM(NRL.BIAOMLHJ * NRL.RLZHBMDJ) / SUM(NRL.BIAOMLHJ)),\n" + 
						"             2) AS RLZHBMDJ,\n" + 
						"       ROUND(DECODE(SUM(ZHIB.MEIL),\n" + 
						"                    0,\n" + 
						"                    0,\n" + 
						"                    SUM(ZHIB.MEIL * ZHIB.RULZHBMDJ) / SUM(ZHIB.MEIL)),\n" + 
						"             2) AS RLZHBMDJ11,\n" + 
						"\n" + 
						"       (ROUND(DECODE(SUM(NRL.BIAOMLHJ),\n" + 
						"                     0,\n" + 
						"                     0,\n" + 
						"                     SUM(NRL.BIAOMLHJ * NRL.RLZHBMDJ) / SUM(NRL.BIAOMLHJ)),\n" + 
						"              2)) -\n" + 
						"       (ROUND(DECODE(SUM(ZHIB.MEIL),\n" + 
						"                     0,\n" + 
						"                     0,\n" + 
						"                     SUM(ZHIB.MEIL * ZHIB.RULZHBMDJ) / SUM(ZHIB.MEIL)),\n" + 
						"              2)) AS RLZHBMDJ12\n" + 
						"  FROM (SELECT NZ.DIANCXXB_ID,\n" + 
						"               NZ.FADL,\n" + 
						"               NZ.FADBML,\n" + 
						"               NZ.GONGRL,\n" + 
						"               NZ.GONGRBML,\n" + 
						"               NZ.BIAOMLHJ,\n" + 
						"               NZ.MEIZBML,\n" + 
						"               NZ.MEIZBMDJ,\n" + 
						"               NZ.RANYL,\n" + 
						"               NZ.RANYDJ,\n" + 
						"               NZ.YOUZBML,\n" + 
						"               NZ.YOUZBMDJ,\n" + 
						"               NZ.QITFY,\n" + 
						"               NZ.RLZHBMDJ\n" + 
						"          FROM NIANDJH_ZHIB NZ where  nz.riq=DATE'"+strDate+"') NRL,\n" + 
						"       (SELECT ZB.DIANCXXB_ID,\n" + 
						"               ZB.RULZHBMDJ,\n" + 
						"               (ZB.RULMZBZML + ZB.RULYZBZML + ZB.RULQZBZML) MEIL\n" + 
						"          FROM YUEZBB ZB\n" + 
						"         WHERE FENX = '累计'\n" + 
						"           AND RIQ = ADD_MONTHS(DATE '"+strZBDate+"', -12)) ZHIB,\n" + 
						"       DIANCXXB DC\n" + 
						" WHERE NRL.DIANCXXB_ID(+) = DC.ID\n" + 
						"   AND ZHIB.DIANCXXB_ID(+) = DC.ID\n" + 
						"   AND DC.JIB = 3\n" + 
						"   AND (DC.ID in( "+this.getTreeid()+") )\n" + 
						" GROUP BY ROLLUP(DC.MINGC, DC.XUH)\n" + 
						"HAVING NOT GROUPING(DC.MINGC) + GROUPING(DC.XUH) = 1\n" + 
						" ORDER BY GROUPING(DC.MINGC) DESC, MK, GROUPING(DC.XUH) DESC";


		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 4, 0, 3);
//		tb.setFixedCols(12);
		
		rt.setBody(tb);

		ArrHeader = new String[4][16];
		ArrHeader[0] = (new String[] { "期别", "发电量", "发电标煤量", "供热量", "供热标煤量", "标煤量合计","煤折标煤量", "煤折<br>标煤单价", "燃油量", "燃油单价<br>不含税", "油折<br>标煤量", "油折<br>标煤单价","其他费用","入炉综合标煤单价","备注(入炉综合标煤单价)","备注(入炉综合标煤单价)" });
		ArrHeader[1] = (new String[]  { "期别", "发电量", "发电标煤量", "供热量", "供热标煤量", "标煤量合计","煤折标煤量", "煤折<br>标煤单价", "燃油量", "燃油单价<br>不含税", "油折<br>标煤量", "油折<br>标煤单价","其他费用","入炉综合标煤单价","去年11月累计完成","预测比去年11月累计" });
		ArrHeader[2] = (new String[]  { "期别", "万千瓦时", "吨", "万吉焦", "吨", "吨","吨", "元/吨", "吨", "元/吨", "吨", "元/吨","吨","元/吨","元/吨","元/吨" });
		ArrHeader[3]=new String[] {"甲","1","2","3","4","5","6","7","8", "9","10","11","12","13","14","15"};
		ArrWidth = (new int[] { 120, 60, 75, 60, 75, 75, 75, 60, 60, 60, 60,60, 75, 60, 75,75 });
		rt.setTitle(intyear + "年入炉综合标煤单价测算汇总表", ArrWidth);
		rt.body.setWidth(ArrWidth);

		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);

//	标题行合并单元格
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 3, 1);
		rt.body.merge(1, 2, 2, 2);
		rt.body.merge(1, 3, 2, 3);
		rt.body.merge(1, 4, 2, 4);
		rt.body.merge(1, 5, 2, 5);
		rt.body.merge(1, 6, 2, 6);
		rt.body.merge(1, 7, 2, 7);
		rt.body.merge(1, 8, 2, 8);
		rt.body.merge(1, 9, 2,9);
		rt.body.merge(1, 10, 2, 10);
		rt.body.merge(1, 11, 2,11);
		rt.body.merge(1, 12, 2, 12);
		rt.body.merge(1, 13, 2,13);
		rt.body.merge(1, 14, 2,14);
		rt.getPages();
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2, 2, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 1, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols(), 1, "(第Page/Pages页)",
				Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}

	// 年份
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged = false;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2011; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	// ***************************报表初始设置***************************//
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

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	// 页面判定方法
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
//	初始化多选电厂树中的默认值
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString()
				.equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			// setYuefValue(null);
			getNianfModels();
			// getYuefModels();
//			getIDiancmcModels();
			getDiancmcModel();
			this.setTreeid(null);
			setToolbar(null);
			 initDiancTree();
		}
		getToolBars();
		getSelectData();
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
//		Visit visit = (Visit) getPage().getVisit();
//		double dcid = visit.getDiancxxb_id();

		// 单位
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("组合电厂");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));
		
		// 年份
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
//		刷新
		ComboBox comb1 = new ComboBox();
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		setToolbar(tb1);
		comb1.setId("NIANF");// 和自动刷新绑定
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

//	电厂名称-----------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {

		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript()+getOtherScript("diancTree");
	}
//	增加电厂多选树的级联
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
}