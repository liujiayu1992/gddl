package com.zhiren.dc.diaoygl.Kucmjg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rikcjgljcx extends BasePage {

//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
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


	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		setMsg(null);
		return getKucrb();
	}
	
//	不要删除此方法
	private String zhi ="";
	public String getZhi() {
		if(this.zhi.equals("")){
			setZhi();
		}
			return zhi;
	}
	public void setZhi() {
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select zhi from xitxxb where mingc = '库存日报隐藏电厂名称' and diancxxb_id ="+v.getDiancxxb_id());
		if(rsl.next()){
			this.zhi="  "+rsl.getString("zhi");
		}else{
			this.zhi=" -1 ";
		}
		rsl.close();
		con.Close();
	}
//	
	
	private String getKucrb() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String beginRiq=this.getBeginriqDate();
		
		String strSQL = 

			"--界面开始横向计算\n" +
			"SELECT --ROWNUM XUH,\n" + 
			"       DIANCXXB_ID DWMC,\n" + 
			"       MEIZMC,\n" + 
			"       LAIMSL,\n" + 
			"       ROUND(LAIMRZ,0)LAIMRZ,\n" + 
			"       ROUND(LAIMRLF/10000,2)LAIMRLF,\n" + 
			"       ROUND(DECODE (LAIMSL*LAIMRZ,0,0,LAIMRLF/(LAIMSL*LAIMRZ/7000)),2)LAIMBMDJ,\n" + 
			"       HAOML,\n" + 
			"       ROUND(HAOMRZ,0)HAOMRZ,\n" + 
			"       ROUND(HAOMRLF/10000,2)HAOMRLF,\n" + 
			"       ROUND(DECODE (HAOML*HAOMRZ,0,0,HAOMRLF/(HAOML*HAOMRZ/7000)),2)HAOMBMDJ,\n" + 
			"       ROUND(YKKC/10000,0)YKKC,\n" + 
			"       ROUND(KUCSL/10000,2)KUCSL,\n" + 
			"       ROUND(KUCRL,0)KUCRL,\n" + 
			"       ROUND(DECODE(KUCRL, 0, 0, KUCYMDJ * 7000 / KUCRL),2) KUCBMDJ,\n" + 
			"       ROUND(KUCYMDJ,2)KUCYMDJ,\n" + 
			"       ROUND(DECODE(KUCRL, 0, 0, KUCYMDJ / KUCRL),3) KUCDJMK,\n" + 
			"       ROUND(KUCSL * KUCYMDJ/10000,0) KUCFY,\n" + 
			"       ROUND((KUCSL/10000 - YKKC/10000),2) CZ,\n" + 
			"       ROUND(XZLMYC/10000,2)XZLMYC--,\n" + 
			"       --库存调整量：当日库存-(昨日库存+当日来煤-当日耗用)\n" + 
			"       --ROUND((KUCSL - (ZUORKC + LAIMSL - HAOML)),2) KUCTZL\n" + 
			"  FROM (SELECT DECODE(GROUPING(DC.MINGC),1,'合计',DC.MINGC) DIANCXXB_ID,\n" + 
			"       DECODE(GROUPING(DC.MINGC)+GROUPING(MZ.MEIZMC),1,'小计', MZ.MEIZMC)MEIZMC,\n" + 
			"       DECODE(GROUPING(MZ.MEIZMC)+GROUPING(DC.MINGC), 0, 0, 1,MAX(ZHI),NVL((SELECT SUM(ZHI)\n" + 
			"             FROM DCYKKCMB\n" + 
			"            WHERE RIQ = TRUNC(DATE '"+beginRiq+"', 'mm')\n" + 
			"              AND DIANCXXB_ID IN ("+getTreeid()+")\n" + 
			"              AND DIANCXXB_ID NOT IN (  "+this.getZhi()+")),\n" + 
			"           0)) YKKC,\n" + 
			"       SUM(NVL(ZRKC.KUCSL, 0)) ZUORKC,\n" + 
			"       SUM(ZR.LAIMSL) LAIMSL,\n" + 
			"       DECODE(SUM(ZR.LAIMSL), 0, 0, SUM(ZR.LAIMRZ * ZR.LAIMSL) / SUM(ZR.LAIMSL)) LAIMRZ,\n" + 
			"       SUM(ZR.LAIMRLF) LAIMRLF,\n" + 
			"       SUM(ZR.HAOML) HAOML,\n" + 
			"       DECODE(SUM(ZR.HAOML), 0, 0, SUM(ZR.HAOMRZ * ZR.HAOML) / SUM(ZR.HAOML)) HAOMRZ,\n" + 
			"       SUM(ZR.HAOMRLF) HAOMRLF,\n" + 
			"       SUM(R.KUCSL) KUCSL,\n" + 
			"       DECODE(SUM(R.KUCSL), 0, 0, SUM(R.KUCRL * R.KUCSL) / SUM(R.KUCSL)) KUCRL,\n" + 
			"       DECODE(SUM(R.KUCSL), 0, 0, SUM(R.KUCYMDJ * R.KUCSL) / SUM(R.KUCSL)) KUCYMDJ,\n" + 
			"       SUM(R.XZLMYC) XZLMYC\n" + 
			"  FROM (SELECT DISTINCT MZ.ID, MZ.DIANCXXB_ID, MZ.MEIZMC\n" + 
			"          FROM DCMZB MZ, KUCMRBB K\n" + 
			"         WHERE K.DCMZB_ID = MZ.ID\n" + 
			"           AND K.RIQ BETWEEN TRUNC(DATE '"+beginRiq+"', 'mm') AND DATE'"+beginRiq+"') MZ,\n" + 
			"     (SELECT DIANCXXB_ID,DCMZB_ID,KUCSL,KUCRL,KUCYMDJ,XZLMYC FROM  KUCMRBB R WHERE R.RIQ = DATE '"+beginRiq+"')R,\n" + 
			"     (SELECT DIANCXXB_ID,DCMZB_ID,\n" + 
			"             SUM(LAIMSL) LAIMSL,\n" + 
			"             DECODE(SUM(LAIMSL), 0, 0, SUM(LAIMRZ * LAIMSL) / SUM(LAIMSL)) LAIMRZ,\n" + 
			"             SUM(LAIMRLF) LAIMRLF,\n" + 
			"             SUM(HAOML) HAOML,\n" + 
			"             DECODE(SUM(HAOML), 0, 0, SUM(HAOMRZ * HAOML) / SUM(HAOML)) HAOMRZ,\n" + 
			"             SUM(HAOMRLF) HAOMRLF\n" + 
			"             FROM  KUCMRBB R\n" + 
			"             WHERE RIQ BETWEEN TRUNC(DATE '"+beginRiq+"', 'mm') AND DATE'"+beginRiq+"'\n" + 
			"       GROUP BY DIANCXXB_ID, DCMZB_ID\n" + 
			"       )ZR,\n" + 
			"       (SELECT DIANCXXB_ID, DCMZB_ID, SUM(KUCSL) KUCSL\n" + 
			"          FROM KUCMRBB\n" + 
			"         WHERE RIQ = DATE '"+beginRiq+"' - 1\n" + 
			"         GROUP BY DIANCXXB_ID, DCMZB_ID) ZRKC,\n" + 
			"       (SELECT DIANCXXB_ID, ZHI\n" + 
			"          FROM DCYKKCMB\n" + 
			"         WHERE RIQ = TRUNC(DATE '"+beginRiq+"', 'mm')) YK,\n" + 
			"       DIANCXXB DC\n" + 
			" WHERE MZ.ID = R.DCMZB_ID(+)\n" + 
			"   AND MZ.ID = ZRKC.DCMZB_ID(+)\n" + 
			"   AND MZ.ID = ZR.DCMZB_ID(+)\n" + 
			"   AND DC.ID = MZ.DIANCXXB_ID\n" + 
			"   AND MZ.DIANCXXB_ID = ZR.DIANCXXB_ID(+)\n" + 
			"   AND MZ.DIANCXXB_ID = ZRKC.DIANCXXB_ID(+)\n" + 
			"   AND MZ.DIANCXXB_ID = R.DIANCXXB_ID(+)\n" + 
			"   AND MZ.DIANCXXB_ID = YK.DIANCXXB_ID(+)\n" + 
			"   AND DC.ID IN ("+getTreeid()+")\n" + 
			"   AND DC.ID NOT IN (  "+this.getZhi()+")\n" + 
			" GROUP BY ROLLUP((DC.XUH, DC.MINGC), MZ.MEIZMC)\n" + 
			" ORDER BY GROUPING(DC.XUH) DESC,DC.XUH,GROUPING(MZ.MEIZMC) DESC,MZ.MEIZMC\n" + 
			" )SR";

		ResultSetList rs = con.getResultSetList(strSQL);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[3][19];
		ArrHeader[0]=new String[] {"单位","煤种","当月累计来煤","当月累计来煤","当月累计来煤","当月累计来煤","当月累计耗煤","当月累计耗煤","当月累计耗煤","当月累计耗煤","库存","库存","库存","库存","库存","库存","库存","库存","下周来<br>煤预测"};
		ArrHeader[1]=new String[] {"单位","煤种","数量","热值","燃料费","标煤单价","数量","热量","燃料费","标煤单价","控制目标","数量","热值","标煤单价","原煤单价","每卡单价","库存费用","库存与控制目标差","下周来<br>煤预测"};
		ArrHeader[2]=new String[] {"单位","煤种","吨","Kcal/Kg","万元","元/吨","吨","Kcal/Kg","万元","元/吨","万吨","万吨 ","Kcal/Kg","元/吨"," 元/吨","元/Kcal","万元","万吨 ","万吨"};
		int ArrWidth[]=new int[] {80,80,70,60,60,60,70,60,60,60,60,60,60,60,60,60,70,60,60};
		//rs.beforefirst();
		rt.setBody(new Table(rs, 3, 0, 2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.setTitle(getDiancmc(String.valueOf(visit.getDiancxxb_id()))+"来耗存量质价累计表",ArrWidth);
		rt.setDefaultTitle(1, 6, "填报单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 7, riq, Table.ALIGN_CENTER);
		rt.body.mergeFixedRowCol();
		rt.body.mergeFixedCols();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		//rt.body.setPageRows(21);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private String DataChk(String beginRiq){
		String SQL=
			"SELECT DC.MINGC, DC.XUH\n" +
			"FROM DIANCXXB DC\n" + 
			"WHERE DC.ID IN ("+getTreeid()+")\n" + 
			"AND DC.ID NOT IN ("+this.getZhi()+")\n" + 
			"MINUS\n" + 
			"SELECT DISTINCT DC.MINGC, DC.XUH\n" + 
			"FROM KUCMRBB R, DIANCXXB DC\n" + 
			"WHERE R.DIANCXXB_ID = DC.ID\n" + 
			"AND R.RIQ = DATE '"+beginRiq+"'\n" + 
			"AND DC.ID IN ("+getTreeid()+")\n" + 
			"AND DC.ID NOT IN ("+this.getZhi()+")\n" + 
			"ORDER BY xuh";
		JDBCcon con=new JDBCcon();
		ResultSetList rs = con.getResultSetList(SQL);
		String InvalidDay="";
		int i=1;
		while(rs.next()){
			if(i%5==0 ){
				InvalidDay += rs.getString("MINGC")+",<br>";
			}else{
				InvalidDay += rs.getString("MINGC")+", ";
			}
			i++;
		}
		rs.close();
		con.Close();
		
		return InvalidDay;

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
			+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
//	日期设置开始
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	
	
//	日期设置结束
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("-"));

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
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		String errorMsg=DataChk(this.getBeginriqDate());
		if(errorMsg.length()>1){
			setMsg(errorMsg+"信息未上报，请注意");
		}
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setExtTree1(null);
			visit.setString4(null);
			setTreeid(null);
			setZhi();
//			如果登录用户为国电电力，那么单位下拉框默认为全部单位。
			if(visit.getDiancxxb_id()==112){
				initDiancTree();
			}
		}
		getToolbars();
		blnIsBegin = true;
	}
	

	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
//	电厂名称
//	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

//	private boolean _DiancmcChange=false;
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

//	得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}
//	 得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(String diancId) {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		//long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;

	}
	public String getDiancmc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "组合电厂";
		}else{
			return getDiancmc(str[0]);
		}
	}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
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