package com.zhiren.dc.monthReport.gd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：李琛基
 * 时间：2010-08-25
 * 描述：
 *   根据国电需求  增加月度燃料管理指标情况 月报报表
 */
/*
 * 作者：songy
 * 时间：2011-4-18
 * 描述：
 *   修改报表sql,入炉热值计算不对,报表分组和排序不对
 */
/*
 * 作者：夏峥
 * 时间：2011-05-27
 * 描述：根据国电公司需求修改多选电厂树的默认初始值（为电厂级别大于2的所有单位）。
 */
/*
 * 作者：夏峥
 * 时间：2011-07-04
 * 描述：修正入炉综合标煤单价的计算公式,根据yuezbdyb中的入炉综合标煤单价公式得出
 */
/*
 * 作者：夏峥
 * 时间：2012-01-12
 * 描述：修正月指标情况表，耗用量和库存从耗存合计中取得
 */
/*
 * 作者：夏峥
 * 时间：2012-02-01
 * 描述：修正月指标情况表，进厂煤量以及来煤量中数量信息应包括运损
 */
/*
 * 作者：夏峥
 * 时间：2012-02-02
 * 描述：修正月指标情况表，按电厂序号排列
 */

/*
 * 作者：夏峥
 * 时间：2012-02-12
 * 描述: 报表中当访问用户为国电电力时只显示以上报的数据（其状态为1或3的数据）。
 * 		 修正入厂标煤单价的计算公式（入厂总成本除以入厂折标煤量）
 */
/*
 * 作者：夏峥
 * 时间：2012-03-02
 * 描述: 修正由于关联错误导致报表的来煤数量成倍增长的问题
 */
/*
 * 作者：夏峥
 * 时间：2012-03-05
 * 描述: 更正耗用量，入炉标煤量，入炉标煤单价的取值和计算公式
 * 		 更正入炉标煤量的取值公式
 */
/*
 * 作者：夏峥
 * 时间：2012-03-08
 * 描述: 调整结算相关的修约方式，内层取数不修约
 */
/*
 * 作者：赵胜男
 * 时间：2012-04-05
 * 描述: 修正入厂标煤量的sql
 */
/*
 * 作者：夏峥
 * 时间：2012-09-04
 * 描述: 修正入炉综合标煤单价加权时的加权量应使用（入炉煤折标煤量+入炉气折标煤量+入炉油折标煤量）
 * 		   将大开电厂的数据合并为大开热电
 */
/*
 * 作者：夏峥
 * 时间：2012-09-05
 * 描述: 增加入厂标煤单价和入厂不含税标煤单价字段
 */
/*
 * 作者：夏峥
 * 时间：2012-10-17
 * 描述: 不含税标煤单价的计算公式改为
 * 		round(DECODE(qnet_ar,0,0,(meij/1.17+yunj*0.93+zaf-zafs)* 29.271/qnet_ar),2) buhsbmdj
 */
/*
 * 作者：赵胜男
 * 时间：2012-12-14
 * 描述: 新增入厂入炉标煤单价差一列
 */
/*
 * 作者：夏峥
 * 时间：2012-12-18
 * 描述: 新增库存天然煤热值,库存天然煤价(含税),库存标煤单价(含税)列
 */
/*
 * 作者：夏峥
 * 时间：2013-01-05
 * 描述: 调整入厂标煤单价(不含税)列的计算公式
 */
/*
 * 作者：夏峥
 * 时间：2013-01-06
 * 描述: 调整月末库存，库存天然煤热值，库存天然煤价(不含税),库存标煤单价(不含税)列不显示累计信息。
 * 		耗油量信息取整
 */
/*
 * 作者：夏峥
 * 时间：2013-03-07
 * 描述: 取消大开电厂的手工合并
 */
/*
 * 作者：赵胜男
 * 时间：2013-6-26
 * 描述: 调整报表表头顺序
 */
public class Yuerlzbqkb extends BasePage {
	
	
	private String _msg;
	
	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	public boolean getRaw() {
		return true;
	}
	
	// 报表初始设置
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
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}
	
	// 年份下拉框
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
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
	public String getTianzdwQuanc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "组合电厂";
		}else{
			return getTianzdwQuanc(Long.parseLong(str[0]));
		}
	}
	
	public long getDiancxxbId(){	
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	//得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	
	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
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
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
	
//	分厂别
	public boolean isFencb() {
		return ((Visit) this.getPage().getVisit()).isFencb();
	}
	
	// 页面初始设置
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
	
	private void init(){
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel1(null);
		visit.setDropDownBean1(null);
		visit.setProSelectionModel2(null);
		visit.setDropDownBean2(null);
		visit.setDefaultTree(null);
		setDiancmcModel(null);
		initDiancTree();
		getSelectData();
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
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		tb1.addField(yuef);
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
		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);	
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "查询",
			"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	// submit
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		this.getSelectData();
	}

	// 报表主体
	public String getPrintTable() {	
		return getYuezbqkb();
	}
	
	private String getYuezbqkb() {
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = getTreeid();		
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		StringBuffer sql=new StringBuffer();
		StringBuffer where_sql=new StringBuffer();
//		int jib=this.getDiancTreeJib();
		
		/*if (jib==1) {//选集团时刷新出所有的电厂
			where_sql.append("where fuid="+this.getTreeid()+"\n");
		}else if(jib==2) {//选分公司的时候刷新出分公司下所有的电厂
			try{
				where_sql.append("where fuid="+this.getTreeid()+"\n");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}

		}else{//选择电厂
			where_sql.append("where id="+this.getTreeid()+"\n");
		}*/

		String tiaoj1="";
		String tiaoj2="";
		String tiaoj3="";
		String tiaoj4="";
		String tiaoj5="";
		Visit visit = (Visit) getPage().getVisit();
		if(visit.getDiancxxb_id()==112)
		{
			tiaoj1=" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3) \n ";
			tiaoj2=" AND (HC.ZHUANGT=1 OR HC.ZHUANGT=3)\n ";
			tiaoj3=" AND (y.zhuangt=1 OR y.zhuangt=3) \n";
			tiaoj4=" AND (z.zhuangt=1 OR z.zhuangt=3)\n ";
			tiaoj5=" AND (js.zhuangt=1 OR js.zhuangt=3) \n";
		}
		
		where_sql.append("where id in("+diancxxb_id+")\n");
		
//		综合价，入厂含税标煤单价和入厂不含税标煤单价采用横向计算的方式取得
		sql.append("SELECT mingc,fenx,laiml,haoyl,kuc,youhyl,rez,rulrz,rez-rulrz rezc,\n");
		sql.append("jiesl,qnet_ar,meij,yunj,zaf,meij+yunj+zaf zonghdj,\n");
		sql.append("round(DECODE(qnet_ar,0,0,(meij+yunj+zaf)* 29.271/qnet_ar),2) biaomdj,\n");
		sql.append("round(DECODE(qnet_ar,0,0,(meij/1.17+yunj-yunjs+zaf-zafs)* 29.271/qnet_ar),2) buhsbmdj,rulbml,rulbmdj, \n");
		sql.append("(round(DECODE(qnet_ar,0,0,(meij/1.17+yunj-yunjs+zaf-zafs)* 29.271/qnet_ar),2) -rulbmdj) chaz,kuctrmj,kuctrmrz,\n");
		sql.append("round(DECODE(kuctrmrz,0,0,kuctrmj* 29.271/kuctrmrz),2) kucbmdj\n");
		sql.append("from(select decode(grouping(dianc.mingc)+grouping(dianc.fuid), 2, '总计',1 ,'小计',dianc.mingc)  mingc,\n");
		sql.append("dianc.fenx,\n"); 
		sql.append("round(sum(shul.laiml), 0) laiml,\n"); 
		sql.append("round(sum(shul.haoyl), 0) haoyl,\n"); 
		sql.append("round(sum(shul.kuc), 0) kuc,\n"); 
		sql.append("round(sum(haoy.haoyl), 0) youhyl,\n"); 
		sql.append("round_new(decode(sum(shul.jincml),0,0,sum(shul.rez * shul.jincml) / sum(shul.jincml)),2) rez,\n"); 
		sql.append("round_new(decode(sum(zhibb.fadgrytrml),0,0,sum(zhibb.rultrmpjfrl*decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml)) /sum(decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml))),2) rulrz,\n"); 
		sql.append("--round_new(nvl(decode(sum(shul.jincml),0,0,sum(shul.rez * shul.jincml) /sum(shul.jincml)),0),2) -\n"); 
		sql.append("--round_new(decode(sum(zhibb.fadgrytrml),0,0,sum(zhibb.rultrmpjfrl*decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml)) /sum(decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml))),2) rezc,\n"); 
		sql.append("round_new(sum(jiesbm.jiesl), 0) jiesl,\n"); 
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.qnet_ar * jiesbm.jiesl) /sum(jiesbm.jiesl)),2) qnet_ar,\n"); 
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.meij * jiesbm.jiesl) / sum(jiesbm.jiesl)),2) meij,\n"); 
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.yunj * jiesbm.jiesl) / sum(jiesbm.jiesl)),2) yunj,\n"); 
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.yunjs * jiesbm.jiesl) / sum(jiesbm.jiesl)),2) yunjs,\n"); 
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.zaf * jiesbm.jiesl) / sum(jiesbm.jiesl)),2) zaf,\n"); 
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.zafs * jiesbm.jiesl) / sum(jiesbm.jiesl)),2) zafs,\n"); 
		sql.append("--round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.zonghdj * jiesbm.jiesl) /sum(jiesbm.jiesl)),2) zonghdj,\n"); 
		sql.append("--round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.biaomdj * jiesbm.jiesl) /sum(jiesbm.jiesl)),2) biaomdj,\n"); 
		sql.append("--round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.buhsbmdj * jiesbm.jiesl) /sum(jiesbm.jiesl)),2) buhsbmdj,\n"); 
		sql.append("round_new(sum(decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml)), 0)rulbml,\n"); 
		sql.append("round_new(decode(sum(zhibb.rulbml),0,0,sum(zhibb.bmdj*decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml)) /sum(decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml))),2)rulbmdj, \n"); 
		sql.append("round_new(decode(sum(shul.kuc),0,0,sum(zhibb.kuctrmj * shul.kuc) / sum(shul.kuc)),2) kuctrmj,\n"); 
		sql.append("round_new(decode(sum(shul.kuc),0,0,sum(zhibb.kuctrmrz * shul.kuc) / sum(shul.kuc)),2) kuctrmrz\n"); 
		sql.append("from \n"); 
		sql.append("--建立表头\n"); 
		sql.append("(SELECT ID,\n"); 
		sql.append("      MINGC MINGC,\n"); 
		sql.append("      MIN(XUH) XUH,\n"); 
		sql.append("      MAX(FUID) FUID,\n"); 
		sql.append("      NVL('本月', '') FENX\n"); 
		sql.append(" FROM DIANCXXB\n"); 
		sql.append(where_sql); 
		sql.append(" GROUP BY ID,\n"); 
		sql.append("          MINGC\n"); 
		sql.append("UNION\n"); 
		sql.append("SELECT ID,\n"); 
		sql.append("       MINGC,\n"); 
		sql.append("       MIN(XUH) XUH,\n"); 
		sql.append("       MAX(FUID) FUID,\n"); 
		sql.append("       NVL('累计', '') FENX\n"); 
		sql.append("  FROM DIANCXXB\n"); 
		sql.append(where_sql); 
		sql.append(" GROUP BY ID,\n"); 
		sql.append("          MINGC\n"); 
		sql.append("UNION\n"); 
		sql.append("SELECT ID,\n"); 
		sql.append("       MINGC,\n"); 
		sql.append("       MIN(XUH) XUH,\n"); 
		sql.append("       MAX(FUID) FUID,\n"); 
		sql.append("       NVL('同期累计', '') FENX\n"); 
		sql.append("  FROM DIANCXXB\n"); 
		sql.append(where_sql); 
		sql.append(" GROUP BY ID,\n"); 
		sql.append("          MINGC\n"); 
		sql.append(") dianc,\n"); 
		
//		来煤，耗煤，库存
		sql.append("(SELECT DC.ID DIANCXXB_ID,\n" ); 
		sql.append("       DC.FENX,\n" );  
		sql.append("       NVL(LM.LAIML, 0) LAIML,\n");
		sql.append("       NVL(HC.HAOYL, 0) HAOYL,\n");
		sql.append("       NVL(HC.KUC, 0) KUC, NVL(LM.JINCML, 0) JINCML, NVL(LM.REZ, 0) REZ\n");
		sql.append("FROM (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" +
				"               sl.fenx fenx,\n" + 
				"               SUM(DECODE(KJ.DIANCXXB_ID, 215, (SL.JINGZ+SL.YUNS) * 0.4, (SL.JINGZ+SL.YUNS))) LAIML,\n" + 
				"               sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) jincml,\n" + 
				"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) /sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS)))),2) rez\n" + 
				"          FROM YUESLB SL,YUEZLB ZL,\n" + 
				"               (SELECT ID,\n" + 
				"                       DIANCXXB_ID,\n" + 
				"                       Y.GONGYSB_ID,\n" + 
				"                       Y.JIHKJB_ID,\n" + 
				"                       Y.PINZB_ID\n" + 
				"                  FROM YUETJKJB Y\n" + 
				"                 WHERE RIQ = DATE '"+strDate+"')kj\n" + 
				"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
				"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
				tiaoj1 + 
				"           GROUP BY KJ.DIANCXXB_ID, sl.FENX\n" + 
				"           UNION\n" + 
				"             SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
				"               NVL('同期累计', '') FENX,\n" + 
				"               SUM(DECODE(KJ.DIANCXXB_ID, 215, (SL.JINGZ+SL.YUNS) * 0.4, (SL.JINGZ+SL.YUNS))) LAIML,\n" + 
				"               sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) jincml,\n" + 
				"               decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS))) /sum(decode(diancxxb_id, 215, (sl.jingz+SL.YUNS) * 0.4, (sl.jingz+SL.YUNS)))) rez\n" + 
				"               FROM YUESLB SL, YUEZLB ZL,\n" + 
				"                (SELECT ID,\n" + 
				"                       DIANCXXB_ID,\n" + 
				"                       NVL('累计', '') AS FENX,\n" + 
				"                       Y.GONGYSB_ID,\n" + 
				"                       Y.JIHKJB_ID,\n" + 
				"                       Y.PINZB_ID\n" + 
				"                  FROM YUETJKJB Y\n" + 
				"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)) KJ\n" + 
				"         WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
				"           AND KJ.ID=ZL.YUETJKJB_ID\n" + 
				"           AND KJ.FENX = SL.FENX AND KJ.FENX = ZL.FENX\n" + 
				"         GROUP BY KJ.DIANCXXB_ID) LM,\n");
		sql.append("       (SELECT HC.DIANCXXB_ID DIANCXXB_ID,\n");
		sql.append("               NVL('同期累计', '') FENX,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID,\n");
		sql.append("                          215,\n");
		sql.append("                          (HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH) * 0.4,\n");
		sql.append("                          HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH)) HAOYL,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID, 215, HC.KUC * 0.4, HC.KUC)) KUC\n");
		sql.append("          FROM YUESHCHJB HC\n");
		sql.append("         WHERE HC.RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n");
		sql.append("           AND HC.FENX = '累计'\n");
		sql.append("         GROUP BY HC.DIANCXXB_ID\n");
		sql.append("        UNION\n");
		sql.append("        SELECT HC.DIANCXXB_ID DIANCXXB_ID,\n");
		sql.append("               HC.FENX FENX,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID,\n");
		sql.append("                          215,\n");
		sql.append("                          (HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH) * 0.4,\n");
		sql.append("                          HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH)) HAOYL,\n");
		sql.append("               DECODE(HC.FENX,'累计',0,SUM(DECODE(HC.DIANCXXB_ID, 215, HC.KUC * 0.4, HC.KUC))) KUC\n");
		sql.append("          FROM YUESHCHJB HC\n");
		sql.append("         WHERE HC.RIQ = DATE '"+strDate+"' "+tiaoj2+"\n");
		sql.append("         GROUP BY HC.DIANCXXB_ID, HC.FENX) HC,\n");
		sql.append("       (SELECT DISTINCT DC.ID ID, FX.FENX\n");
		sql.append("          FROM DIANCXXB DC,\n");
		sql.append("               (SELECT NVL('本月', '') FENX\n");
		sql.append("                  FROM DUAL\n");
		sql.append("                UNION\n");
		sql.append("                SELECT NVL('累计', '') FENX\n");
		sql.append("                  FROM DUAL\n");
		sql.append("                UNION\n");
		sql.append("                SELECT NVL('同期累计', '') FENX FROM DUAL) FX) DC\n");
		sql.append(" WHERE DC.ID = HC.DIANCXXB_ID(+)\n");
		sql.append("   AND DC.ID = LM.DIANCXXB_ID(+)\n");
		sql.append("   AND DC.FENX = HC.FENX(+)\n");
		sql.append("   AND DC.FENX = LM.FENX(+) ) shul,");

		sql.append("\n"); 
		sql.append("(select  y.DIANCXXB_ID diancxxb_id,\n"); 
		sql.append("y.fenx fenx,\n"); 
		sql.append("sum(decode(diancxxb_id,215,(y.fadyy + y.gongry + y.qithy + y.sunh) * 0.4,y.fadyy + y.gongry + y.qithy + y.sunh)) haoyl\n"); 
		sql.append("from yueshcyb y\n"); 
		sql.append("where y.riq = date'"+strDate+"' "+tiaoj3+"\n"); 
		sql.append("group by  y.DIANCXXB_ID , y.fenx\n"); 
		sql.append("union\n"); 
		sql.append("select  y.DIANCXXB_ID diancxxb_id,\n"); 
		sql.append("NVL('同期累计', '') fenx,\n"); 
		sql.append("sum(decode(diancxxb_id,215,(y.fadyy + y.gongry + y.qithy + y.sunh) * 0.4,y.fadyy + y.gongry + y.qithy + y.sunh)) haoyl\n"); 
		sql.append("from yueshcyb y\n"); 
		sql.append("where y.riq = add_months(date'"+strDate+"',-12)\n"); 
		sql.append("and y.fenx = '累计'\n"); 
		sql.append("group by  y.DIANCXXB_ID ) haoy,\n"); 
		sql.append("\n"); 
		
//		指标
		sql.append("(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n");
		sql.append("		       Z.FENX,\n");
		sql.append("		      SUM(Z.FADGRYTRML) FADGRYTRML,\n");
		sql.append("		      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n");
		sql.append("		      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n");
		sql.append("		      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ,\n");
		sql.append("		      DECODE(Z.FENX,'累计',0,max(z.kuctrmrz)) kuctrmrz,\n");
		sql.append("		      DECODE(Z.FENX,'累计',0,max(z.kuctrmj)) kuctrmj\n");
		sql.append("		  FROM YUEZBB Z\n");
		sql.append("		 WHERE Z.RIQ = date '"+strDate+"' \n");
		sql.append(tiaoj4);
		sql.append("		 group by  z.DIANCXXB_ID ,Z.FENX\n");
		sql.append("		UNION\n");
		sql.append("		SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n");
		sql.append("		       NVL('同期累计', '') FENX,\n");
		sql.append("		      SUM(Z.FADGRYTRML) FADGRYTRML,\n");
		sql.append("		      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n");
		sql.append("		      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n");
		sql.append("		      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULZHBMDJ*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),2) BMDJ,\n");
		sql.append("		      max(z.kuctrmrz)kuctrmrz,\n");
		sql.append("		      max(z.kuctrmj)kuctrmj\n");
		sql.append("		  FROM YUEZBB Z\n");
		sql.append("		 WHERE Z.RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n");
		sql.append(tiaoj4);
		sql.append("		   AND Z.FENX = '累计'\n");
		sql.append("		   group by  z.DIANCXXB_ID \n");
		sql.append("		) zhibb,\n");
		sql.append("\n"); 
		
//		结算标煤单价
		sql.append( "(select  KJ.DIANCXXB_ID diancxxb_id,js.fenx fenx,\n" +
					"sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) jiesl,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,sum(js.qnet_ar *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl))) qnet_ar,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.meij) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) meij,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.yunj) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) yunj,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.yunjs) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) yunjs,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.ZAF+js.DAOZZF+js.QIT+js.KUANGQYF) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) zaf,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.ZAFS) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) zafs,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,sum((js.meij+ js.yunj+ js.zaf +js.daozzf + js.qit+js.KUANGQYF) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl))) zonghdj,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.biaomdj) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) biaomdj,\n" +
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.buhsbmdj) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) buhsbmdj\n" +
					"from yuejsbmdj js,\n" + 
					"(select id,\n" + 
					"diancxxb_id\n" + 
					"from yuetjkjb\n" + 
					"where riq = date '"+strDate+"')kj\n" + 
					"where kj.id = js.yuetjkjb_id(+)\n" + 
					tiaoj5+ 
					"group by  KJ.DIANCXXB_ID , js.fenx\n" + 
					"UNION\n" + 
					"select  KJ.DIANCXXB_ID diancxxb_id,\n" + 
					"NVL('同期累计', '') fenx,\n" + 
					"sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) jiesl,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,sum(js.qnet_ar *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl))) qnet_ar,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.meij) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) meij,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.yunj) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) yunj,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.yunjs) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) yunjs,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.ZAF+js.DAOZZF+js.QIT+js.KUANGQYF) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) zaf,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.ZAFS) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) zafs,\n" +
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,sum((js.meij+ js.yunj+ js.zaf +js.daozzf + js.qit+js.KUANGQYF) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl))) zonghdj,\n" + 
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.biaomdj) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) biaomdj,\n" +
					"decode(sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)),0,0,(sum((js.buhsbmdj) *decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)) /sum(decode(diancxxb_id, 215, js.jiesl * 0.4, js.jiesl)))) buhsbmdj\n" +
					"FROM yuejsbmdj js,(select id, diancxxb_id, nvl('累计', '') as fenx\n" + 
					"from yuetjkjb\n" + 
					"where riq = add_months(date'"+strDate+"',-12)) kj\n" + 
					"where kj.id = js.yuetjkjb_id(+)\n" + 
					"and kj.fenx = js.fenx(+)\n" + 
					"group by KJ.DIANCXXB_ID) jiesbm\n"); 
		sql.append("\n"); 
		sql.append("where dianc.id = shul.diancxxb_id(+)\n"); 
		sql.append("and dianc.id = haoy.diancxxb_id(+)\n"); 
		sql.append("and dianc.fenx = shul.fenx(+)\n"); 
		sql.append("and dianc.fenx = haoy.fenx(+)\n"); 
		sql.append("and dianc.id = zhibb.diancxxb_id(+)\n"); 
		sql.append("and dianc.fenx = zhibb.fenx(+)\n"); 
		sql.append("and dianc.id = jiesbm.diancxxb_id(+)\n"); 
		sql.append("and dianc.fenx = jiesbm.fenx(+)\n"); 
		sql.append("and dianc.id not in (300,112,303)\n"); 
		sql.append("group by rollup( dianc.fenx,dianc.fuid,(dianc.mingc, dianc.xuh))\n"); 
		sql.append("having not grouping(dianc.fenx) + grouping(dianc.mingc) + grouping(dianc.fuid) = 3\n"); 
		sql.append("order by grouping(dianc.fuid)desc,dianc.fuid,grouping(dianc.mingc) desc,dianc.xuh, dianc.mingc,grouping( dianc.fenx) desc , dianc.fenx) ");

//		System.out.println(sql.toString());
		ResultSetList rs=cn.getResultSetList(sql.toString());
		Report rt=new Report();
		 
		//定义表头数据

		 String ArrHeader[][]=new String[2][22];
		 ArrHeader[0]=new String[] {"单位名称","分项","来煤量","耗煤量","月末库存","耗油量",
				 "进厂煤热值","入炉煤热值","热值差","结算量","结算热值",
				 "煤价<br>(含税)","运费<br>(含税)","杂费","入厂原煤综合单价<br>(含税)","入厂标煤单价<br>(含税)","入厂标煤单价<br>(不含税)","入炉标煤量<br>(含油、气)",
				 "入炉综合标煤单价","入厂入炉标煤单价差(不含税)","库存天然煤价<br>(不含税)","库存天然煤热值","库存标煤单价<br>(不含税)"};
		 
		 ArrHeader[1]=new String[] {"甲","乙","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};

		 int ArrWidth[]=new int[] {65,63,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52};

		 rt.setTitle(strMonth+"份月度燃料管理指标情况表", ArrWidth);
		 rt.setDefaultTitle(1,13,"单位："+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(20, 4, "单位：吨、MJ/Kg、元/吨", Table.ALIGN_RIGHT);
		//设置页面
		
//		数据
		rt.setBody(new Table(rs,2,0,0));
		rt.body.setWidth(ArrWidth);

//		rt.body.setPageRows(99);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeCol(1);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);

		rt.getPages();

		rt.body.ShowZero=true;//reportShowZero();
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		
		return rt.getAllPagesHtml();		
	}
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

}