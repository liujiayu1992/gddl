package com.zhiren.shanxdted.hesgl.yujs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yujs extends BasePage implements PageValidateListener {
	
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

//	绑定日期
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString3();
	}
	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString3(riq);
	}
	
	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString4(riq);
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = new ResultSetList();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		try{
			String sql = "";
			rsl = getExtGrid().getModifyResultSet(getChange());
			String jiesb_id = MainGlobal.getNewID(Long.parseLong(getTreeid()));
//			String sql1 = "";
			
			String str_fahksrq = "";
			String str_fahjzrq = "";
			int ches = 0;
			double jingz = 0;
			int yunsfsb_id=0;
			long meikxxb_id=0;
			String meikqc="";
			while(rsl.next()){
				con.getUpdate("update fahb set jiesb_id="+jiesb_id+" where id=" + rsl.getString("id"));
				if(rsl.getRow()==0){
					str_fahksrq = rsl.getString("fahrq");
				}else{
					str_fahjzrq = rsl.getString("fahrq");
				}
				
				ches += rsl.getInt("ches");
				jingz+= rsl.getDouble("jingz");
				yunsfsb_id=rsl.getInt("yunsfsb_id");
				meikxxb_id=rsl.getLong("meikxxb_id");
				meikqc=rsl.getString("meikqc");
				
				
			}
			String str_jisbh = Jiesdcz.getJiesbh(getTreeid(), "");//bianm
			String str_gysmc = getGongysMc(con,getGongysTree_id());//gongysmc
			String str_dcmc  = getDCMC(con,getTreeid());
			String str_shoukdw = str_gysmc;
			double jiessl = Double.parseDouble(gettheKey());//jissl
			double hansdj = getHetdj(con,getHthValue().getStrId());
			double hansmk = CustomMaths.round(jiessl * hansdj,2);
			double buhsmk = CustomMaths.round(hansmk/1.17,2);
			double meikje = buhsmk;
			double shuik = CustomMaths.round((hansmk-buhsmk),2);
			double shuil = 0.17;
			double buhsdj = CustomMaths.round((buhsmk/jiessl),2);
			double hetj = hansdj;
			
			
			sql = 
				"insert into jiesb(id,diancxxb_id,bianm,gongysb_id,gongysmc,faz,yunsfsb_id,\n" +
				"fahksrq,fahjzrq,meiz,daibch,yuanshr,xianshr,yansksrq,yansjzrq,\n" +
				"shoukdw,ches,jiessl,guohl,hansdj,hansmk,buhsmk,meikje,shuik,shuil,buhsdj,\n" +
				"jieslx,jiesrq,hetb_id,liucztb_id,liucgzid,ranlbmjbr,ranlbmjbrq,jihkjb_id,\n" +
				"meikxxb_id,hetj,meikdwmc,is_yujsd,weicdje,jieslx_dt)\n" +
				"values(\n" +
				jiesb_id+","+getTreeid()+",'"+str_jisbh+"',"+getGongysTree_id()+",'"+str_gysmc+"','汽',"+yunsfsb_id+",\n" +//bianm,gongysmc
				"to_date('"+str_fahksrq+"','yyyy-mm-dd'),to_date('"+str_fahjzrq+"','yyyy-mm-dd'),'沫煤','~','"+str_dcmc+"','"+str_dcmc+"',to_date('"+str_fahksrq+"','yyyy-mm-dd'),to_date('"+str_fahjzrq+"','yyyy-mm-dd')," + //fahksrq,fahjzrq,daibch,yuanshr,xianshr,
				"'"+str_shoukdw+"',"+ches+","+jiessl+","+jingz+","+hansdj+","+hansmk+","+buhsmk+","+meikje+","+shuik+","+shuil+","+buhsdj+",\n" +//shoukdw,ches,jiessl,guohl,hansdj,hansmk,buhsmk,meikje,shuik,shuil,buhsdj
				"2,to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd'),"+getHthValue().getStrId()+",0,0,'"+visit.getRenymc()+"',to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd'),2,\n" +
				""+meikxxb_id+","+hetj+",'"+meikqc+"',1,"+hansmk+",1\n" +//hetj,meikdwmc
				")\n";
			con.getInsert(sql);
			sql = 
				"insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,\n" +
				"yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
				"values(\n" +
				"getnewid("+getTreeid()+"),"+jiesb_id+",1,0,"+jiessl+","+jiessl+","+jiessl+",\n" +
				"0,"+hetj+",0,1,0\n" +
				")\n";
			con.getInsert(sql);
			con.commit();
			setMsg("预结算成功,结算单编码：" + str_jisbh + "！");
		}catch(Exception e){
			con.rollBack();
			setMsg("预结算失败！");
		}finally{
			rsl.close();
			con.Close();
		}
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		getSelectData();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
//		Visit visit = (Visit) this.getPage().getVisit();
		String sql_dc = "and 1=2\n";
		if("300".equals(getTreeid())){
			sql_dc = "and f.diancxxb_id in(301,302,303,304)\n";
		}else if("301".equals(getTreeid())){
			sql_dc = "and f.diancxxb_id in(301)\n";
		}else if("302".equals(getTreeid())){
			sql_dc = "and f.diancxxb_id in(302,303,304)\n";
		}
		
		String sql_gys = "and (f.gongysb_id in("+getGongysmlttree_id()+") or f.meikxxb_id in("+getGongysmlttree_id()+"))";
		if("".equals(getGongysmlttree_id())){
			sql_gys = "";
		}
		
		String sql = 
			"select f.id, d.mingc dmc, g.mingc gmc, m.mingc mmc,f.fahrq,f.daohrq, f.jingz, f.biaoz, f.ches,\n" +
			"f.yunsfsb_id,m.id as meikxxb_id,m.quanc as meikqc\n" +
			"from fahb f ,diancxxb d ,gongysb g ,meikxxb m \n" +
			"where f.fahrq>=to_date('"+this.getBeginRiq()+"','yyyy-mm-dd')\n" +
			"  and f.fahrq<=to_date('"+this.getEndRiq()+"','yyyy-mm-dd')\n" +
			"  and f.jiesb_id=0\n" +
			"  and f.diancxxb_id=d.id\n" +
			"  and f.gongysb_id=g.id\n" +
			"  and f.meikxxb_id=m.id\n" +
			"  " + sql_gys +
			"  " + sql_dc + 
			"  order by g.mingc,m.mingc,f.fahrq";
		 
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
//		设置grid为可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		设置为grid数据不分页
		egu.addPaging(0);
//		设置grid宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		复选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		
//		设置grid列信息
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHeader("ID");
		
		
		egu.getColumn("dmc").setHeader("厂别");
		egu.getColumn("dmc").setWidth(100);
		egu.getColumn("dmc").setEditor(null);
		
		
		egu.getColumn("gmc").setHeader("供应商");
		egu.getColumn("gmc").setWidth(150);
		egu.getColumn("gmc").setEditor(null);
		
		
		egu.getColumn("mmc").setHeader("煤矿");
		egu.getColumn("mmc").setWidth(150);
		egu.getColumn("mmc").setEditor(null);
		
		
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(100);
		egu.getColumn("fahrq").setEditor(null);
		
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(100);
		egu.getColumn("daohrq").setEditor(null);
		
		
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(100);
		egu.getColumn("jingz").setEditor(null);
		
		
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("biaoz").setWidth(100);
		egu.getColumn("biaoz").setEditor(null);
		
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(100);
		egu.getColumn("ches").setEditor(null);
		
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setHidden(true);
		egu.getColumn("yunsfsb_id").setEditor(null);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿id");
		egu.getColumn("meikxxb_id").setHidden(true);
		egu.getColumn("meikxxb_id").setEditor(null);
		
		
		egu.getColumn("meikqc").setHeader("煤矿全称");
		egu.getColumn("meikqc").setHidden(true);
		egu.getColumn("meikqc").setEditor(null);
		
		
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
//		增加grid中Toolbar显示日期参数
		egu.addTbarText("发货:");
		DateField dStart = new DateField();
		dStart.Binding("BeginRq","");
		dStart.setValue(getBeginRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText(" 至 ");
		DateField dEnd = new DateField();
		dEnd.setValue(getEndRiq());
		dEnd.Binding("EndRq", "forms[0]");
		egu.addToolbarItem(dEnd.getScript());
		
		egu.addTbarText("-");
		
		String dcid = getTreeid();
		if("301".equals(dcid)){
			dcid = " and fh.diancxxb_id in(301)";
		}else if("302".equals(dcid)){
			dcid = " and fh.diancxxb_id in(302,303,304)";
		}else{
			dcid = "";
		}
		String condition=
			dcid +
			" and fh.fahrq>=to_date('"+this.getBeginRiq()+"','yyyy-MM-dd') " +
			" and fh.fahrq<=to_date('"+this.getEndRiq()+"','yyyy-MM-dd') ";
		ExtTreeUtil etu2 = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getGongysTree_id(),condition,true);
		egu.addTbarText("供应商");
		setTree2(etu2);
		egu.addTbarTreeBtn("gongysTree");
		
		egu.addTbarText("-");
		
		egu.addTbarText("合同:");
		ComboBox comb5 = new ComboBox();
		comb5.setTransform("HthDropDown");
		comb5.setId("Heth");
		comb5.setEditable(false);
		comb5.setLazyRender(true);// 动态绑定
		comb5.setWidth(100);
		comb5.setListWidth(250);
		comb5.setReadOnly(true);
		egu.addToolbarItem(comb5.getScript());
		
		egu.addTbarText("-");
		

//		设置grid按钮
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		
		egu.addTbarText("-");
		egu.addTbarText("预结量：");
		TextField theKey = new TextField();
		theKey.setWidth(60);
		theKey.setId("theKey_text");
		
		egu.addToolbarItem(theKey.getScript());
		egu.addTbarText("-");
		
		egu.addToolbarButton("预结算",GridButton.ButtonType_SubmitSel_condition, "SaveButton",
				"var objkey = document.getElementById('theKey');objkey.value = theKey_text.getValue();\n" + 
				"if(Heth.getRawValue()=='请选择'){\n	" + 
				" Ext.MessageBox.alert('提示信息','请选择合同！');\n" + 
				" return;\n" + 
				" }");
		
//		多选树的赋值
		egu.addOtherScript(
				"gongysTree_treePanel.on(\"checkchange\",function(node,checked){\n" +
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
				"  },gongysTree_treePanel);\n" + 
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
				"\t\tif(gongysTree_history==\"\"){\n" + 
				"\t\t\tgongysTree_history = history;\n" + 
				" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = gongysTree_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  gongysTree_history = his.join(\";\");\n" + 
				"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 gongysTree_history += history;\n" + 
				"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}");
		setExtGrid(egu);
		con.Close();
	}

	
//	dctree  begin
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}

//	public String getTreeid() {
//		if (((Visit) getPage().getVisit()).getString1().equals("")) {
//			((Visit) getPage().getVisit()).setString1(String.valueOf(((Visit) getPage().getVisit())
//					.getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString1();
//	}
//
//	public void setTreeid(String treeid) {
////		if (((Visit) getPage().getVisit()).getString1().equals(treeid)) {
//			((Visit) getPage().getVisit()).setString1(treeid);
////		}
//	}
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			this.treeid = treeid;
		}
	}
//	dctree  end
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
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
			visit.setActivePageName(getPageName().toString());
			setBeginRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
			setExtGrid(null);
			setTreeid("");
			setTree(null);
			visit.setString2("");
			setGongysTree_id("");
			setGongysmlttree_id("");
			setHthValue(null);
			setHthModel(null);
		}
		getHthModels();
		getSelectData();
		
	}
	
//	gongysTree_id_begin
	public String getGongysTree_id() {
		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setGongysTree_id(String treeid) {
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			((Visit) getPage().getVisit()).setString2(treeid);
		}
	}
	public String getGongysmlttree_id(){
		String gongys_id="";
		if(((Visit) getPage().getVisit()).getString14()!=null
				&&!((Visit) getPage().getVisit()).getString14().equals("")){
			String change[] = ((Visit) getPage().getVisit()).getString14().split(";");
			if(change.length==1&&
					(change[0].indexOf("+")==-1&&change[0].indexOf("-")==-1)){
				gongys_id=change[0];
			}else{
				for(int i = 0 ;i < change.length ; i++) {
					if(change[i] == null || "".equals(change[i])) {
						continue;
					}
					String record[] = change[i].split(",",2);
					String sign = record[0];
					String ziyid = record[1];
					if("+".equals(sign)) {
						gongys_id+=ziyid+",";
					}
				}
				if(!gongys_id.equals("")){
					gongys_id=gongys_id.substring(0,gongys_id.lastIndexOf(","));
				}
			}
		}
		if(gongys_id.indexOf(",")==-1){
			if(gongys_id.equals("")){
				this.setGongysTree_id("0");
			}else{
				this.setGongysTree_id(MainGlobal.getLeaf_ParentNodeId(this.getTree2(), gongys_id));
			}
		}else{
			this.setGongysTree_id(MainGlobal.getLeaf_ParentNodeId(this.getTree2(), 
					gongys_id.substring(0,gongys_id.indexOf(","))));
		}
		return gongys_id;
	}
	
	public void setGongysmlttree_id(String value){
		((Visit) getPage().getVisit()).setString14(value);
	}
	
	public ExtTreeUtil getTree2() {
		return ((Visit) this.getPage().getVisit()).getExtTree2();
	}

	public void setTree2(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree2(etu);
	}
	
	public String getTreeHtml2() {
		if (getTree() == null){
			return "";
		}else {
			return getTree2().getWindowTreeHtml(this);
		}
	}

	public String getTreeScript2() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree2().getWindowTreeScript();
		}
	}
	public String getGongysMc(JDBCcon con,String id){
		ResultSetList rsl = con.getResultSetList("select quanc from gongysb where id in("+id+")");
		if(rsl.next()){
			return rsl.getString("quanc");
		}else
			return "";
	}
//	gongysTree_id_end
	
//	heth--begin
	public IDropDownBean getHthValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getHthModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setHthValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setHthModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getHthModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getHthModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getHthModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		List.add(new IDropDownBean(-1, "请选择"));
		try {
			String gongysb_id=this.getGongysmlttree_id();
			
			String mstr_gongys_id="";
//			选择了供应商
			if(gongysb_id==null||gongysb_id.equals("")){
				
			}else if(gongysb_id.indexOf(",")==-1){
				
				mstr_gongys_id=MainGlobal.getLeaf_ParentNodeId(this.getTree2(), gongysb_id);
			}else{
				
				mstr_gongys_id=MainGlobal.getLeaf_ParentNodeId(this.getTree2(),gongysb_id.substring(0,gongysb_id.indexOf(",")));
			}
			
			String gys_sql = " gongysb_id in("+mstr_gongys_id+") and ";
			if("".equals(gongysb_id)){
				gys_sql = "";
			}
			String dc_sql = getTreeid();
			if("301".equals(dc_sql)){
				dc_sql = " and diancxxb_id in(301)";
			}else if("302".equals(dc_sql)){
				dc_sql = " and diancxxb_id in(302,303,304)";
			}else{
				dc_sql = "";
			}
			String sql = 
				"select id,hetbh from hetb \n" +
				" where "+gys_sql+"\n" +
				"       qisrq <= to_date('" + getBeginRiq() + "','yyyy-mm-dd')\n" +
				"   and guoqrq>= to_date('" + getEndRiq() + "','yyyy-mm-dd')\n" + 
				dc_sql;
			
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.getRows()==0&&!gongysb_id.equals("")){
					setMsg("该供应商在系统里没有对应的合同！");
			}
			while (rsl.next()) {
				List.add(new IDropDownBean(rsl.getLong("id"), rsl.getString("hetbh")));
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
//	heth--end
	
    private String theKey;
    public String gettheKey() {
    	return theKey;
    }
    public void settheKey(String theKey) {
    	this.theKey = theKey;
    }
	
    public double getHetdj(JDBCcon con,String id){
		ResultSetList rsl = con.getResultSetList("select max(jg.jij) jij from hetb h, hetjgb jg where h.id=jg.hetb_id and h.id=" + id);
		if(rsl.next()){
			return rsl.getDouble("jij");
		}else
			return 0;
	}
    public String getDCMC(JDBCcon con,String id){
		ResultSetList rsl = con.getResultSetList("select quanc from diancxxb where id=" + id);
		if(rsl.next()){
			return rsl.getString("quanc");
		}else
			return "";
	}
}