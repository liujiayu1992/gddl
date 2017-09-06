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

import com.zhiren.common.CustomMaths;
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
 * 时间：2012-05-15
 * 适用范围：国电电力
 * 描述：调整界面查询内容，以及入厂信息的取值方式
 */

public class Yuerlzbqkb_New extends BasePage {
	
	
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
		
		sql.append("SELECT mingc,fenx,nvl(laiml,0)laiml,nvl(haoyl,0)haoyl,nvl(kuc,0)kuc,nvl(youhyl,0)youhyl,rez,rulrz, NVL(rez,0)-NVL(rulrz,0) rezc,\n" );
		sql.append("NVL(rucmj,0)rucmj,NVL(rucyj,0)rucyj,NVL(ruczf,0)ruczf,(NVL(rucmj,0)+NVL(rucyj,0)+NVL(ruczf,0))ruczhj,NVL(rulbml,0)rulbml,\n");
		sql.append("NVL(rulbmdj,0)rulbmdj,nvl(kuctrmj,0) kuctrmj,nvl(kuctrmrz,0)kuctrmrz,NVL(gusl,0)gusl,NVL(zgj,0)zgj,\n");
		sql.append("(NVL(rucmj,0)+NVL(rucyj,0)+NVL(ruczf,0)-NVL(zgj,0)-NVL(rucmjs,0)-NVL(rucyjs,0)-NVL(ruczfs,0))junjc\n");
		sql.append("FROM (select --GROUPING(dianc.fenx)a,GROUPING(dianc.fuid)b,GROUPING(dianc.mingc)c,GROUPING(dianc.xuh)d,\n");
		sql.append("--dianc.fenx,dianc.fuid,dianc.mingc, dianc.xuh,\n"); 
		sql.append("DECODE(grouping(dianc.fuid),1, '总计',dianc.mingc)  mingc,");
		sql.append("dianc.fenx,\n"); 
		sql.append("round_new(sum(shul.laiml), 0) laiml,\n"); 
		sql.append("round_new(sum(shul.haoyl), 0) haoyl,\n"); 
		sql.append("round_new(sum(shul.kuc), 0) kuc,\n"); 
		sql.append("round_new(sum(haoy.haoyl), 2) youhyl,\n"); 
		sql.append("round_new(decode(sum(shul.jincml),0,0,sum(shul.rez * shul.jincml) / sum(shul.jincml)),2) rez,\n"); 
		sql.append("round_new(decode(sum(zhibb.fadgrytrml),0,0,sum(zhibb.rultrmpjfrl*decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml)) /sum(decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml))),2) rulrz,\n"); 
		sql.append("round_new(nvl(decode(sum(shul.jincml),0,0,sum(shul.rez * shul.jincml) /sum(shul.jincml)),0),2) -\n"); 
		sql.append("round_new(decode(sum(zhibb.fadgrytrml),0,0,sum(zhibb.rultrmpjfrl*decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml)) /sum(decode(dianc.id,215,zhibb.fadgrytrml * 0.4,zhibb.fadgrytrml))),2) rezc,\n"); 
		
		sql.append("round_new(sum(decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml)), 0)rulbml,\n");
		sql.append("round_new(decode(sum(zhibb.rulbml),0,0,sum(zhibb.bmdj*decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml)) /sum(decode(dianc.id,215,zhibb.rulbml * 0.4,zhibb.rulbml))),2)rulbmdj,\n"); 
		sql.append("decode(grouping(dianc.fuid),1,0,MAX(zhibb.kuctrmj))kuctrmj,\n");
		sql.append("decode(grouping(dianc.fuid),1,0,MAX(zhibb.kuctrmrz))kuctrmrz,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucmj*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucmj,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucyj*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucyj,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.ruczf*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) ruczf,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucmjs*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucmjs,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.rucyjs*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) rucyjs,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.ruczfs*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) ruczfs,\n");
		sql.append("round_new(decode(sum(jiesbm.jiesl),0,0,sum(jiesbm.zgj*jiesbm.jiesl)/sum(jiesbm.jiesl)),2) zgj,\n");
		sql.append("round_new(sum(jiesbm.gusl),0)gusl\n");
//		头信息
		sql.append("from (select id, mingc, xuh, fuid, nvl('本月', '') fenx\n"); 
		sql.append("from diancxxb\n"); 
		sql.append(where_sql); 
		sql.append("union\n"); 
		sql.append("select id, mingc, xuh, fuid, nvl('累计', '') fenx\n"); 
		sql.append("from diancxxb\n"); 
		sql.append(where_sql); 
		sql.append("union\n"); 
		sql.append("select id, mingc, xuh, fuid, nvl('同期累计', '') fenx\n"); 
		sql.append("from diancxxb\n"); 
		sql.append(where_sql+") dianc,\n"); 
		sql.append("\n"); 
//		来煤，耗煤，库存
		sql.append("(SELECT DC.ID DIANCXXB_ID,\n" ); 
		sql.append("       DC.FENX,\n" );  
		sql.append("       NVL(LM.LAIML, 0) LAIML,\n");
		sql.append("       NVL(HC.HAOYL, 0) HAOYL,\n");
		sql.append("       NVL(HC.KUC, 0) KUC, NVL(LM.JINCML, 0) JINCML, NVL(LM.REZ, 0) REZ\n");
		sql.append("FROM (SELECT KJ.DIANCXXB_ID,\n" +
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
				"             SELECT KJ.DIANCXXB_ID,\n" + 
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
		sql.append("       (SELECT HC.DIANCXXB_ID,\n");
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
		sql.append("        SELECT HC.DIANCXXB_ID,\n");
		sql.append("               HC.FENX FENX,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID,\n");
		sql.append("                          215,\n");
		sql.append("                          (HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH) * 0.4,\n");
		sql.append("                          HC.FADY + HC.GONGRY + HC.QITH + HC.SUNH)) HAOYL,\n");
		sql.append("               SUM(DECODE(HC.DIANCXXB_ID, 215, HC.KUC * 0.4, HC.KUC)) KUC\n");
		sql.append("          FROM YUESHCHJB HC\n");
		sql.append("         WHERE HC.RIQ = DATE '"+strDate+"' "+tiaoj2+"\n");
		sql.append("         GROUP BY HC.DIANCXXB_ID, HC.FENX) HC,\n");
		sql.append("       (SELECT DC.ID, FX.FENX\n");
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
//		油耗用
		sql.append("\n"); 
		sql.append("(select y.diancxxb_id,\n"); 
		sql.append("y.fenx fenx,\n"); 
		sql.append("sum(decode(diancxxb_id,215,(y.fadyy + y.gongry + y.qithy + y.sunh) * 0.4,y.fadyy + y.gongry + y.qithy + y.sunh)) haoyl\n"); 
		sql.append("from yueshcyb y\n"); 
		sql.append("where y.riq = date'"+strDate+"' "+tiaoj3+"\n"); 
		sql.append("group by y.diancxxb_id, y.fenx\n"); 
		sql.append("union\n"); 
		sql.append("select y.diancxxb_id,\n"); 
		sql.append("NVL('同期累计', '') fenx,\n"); 
		sql.append("sum(decode(diancxxb_id,215,(y.fadyy + y.gongry + y.qithy + y.sunh) * 0.4,y.fadyy + y.gongry + y.qithy + y.sunh)) haoyl\n"); 
		sql.append("from yueshcyb y\n"); 
		sql.append("where y.riq = add_months(date'"+strDate+"',-12)\n"); 
		sql.append("and y.fenx = '累计'\n"); 
		sql.append("group by y.diancxxb_id, y.fenx) haoy,\n"); 
		sql.append("\n"); 
//		指标公式
		sql.append("(SELECT Z.DIANCXXB_ID,\n" );
		sql.append("       Z.FENX,\n");
		sql.append("       Z.FADGRYTRML FADGRYTRML,\n");
		sql.append("       Z.RULTRMPJFRL RULTRMPJFRL,\n");
		sql.append("      (Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml)RULBML,\n");
		sql.append("       Z.RULZHBMDJ BMDJ,\n");
		sql.append("   		DECODE(Z.FENX,'累计',0,z.kuctrmj) kuctrmj,\n");
		sql.append("   		DECODE(Z.FENX,'累计',0,z.kuctrmrz) kuctrmrz\n");
		sql.append("  FROM YUEZBB Z\n");
		sql.append(" WHERE Z.RIQ = date '"+strDate+"' "+tiaoj4+"\n");
		sql.append("UNION\n");
		sql.append("SELECT Z.DIANCXXB_ID,\n");
		sql.append("       NVL('同期累计', '') FENX,\n");
		sql.append("       Z.FADGRYTRML FADGRYTRML,\n");
		sql.append("       Z.RULTRMPJFRL RULTRMPJFRL,\n");
		sql.append("       (Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml) RULBML,\n");
		sql.append("       Z.RULZHBMDJ BMDJ,\n");
		sql.append("   		0 kuctrmj,\n");
		sql.append("   		0 kuctrmrz\n");
		sql.append("  FROM YUEZBB Z\n");
		sql.append(" WHERE Z.RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n");
		sql.append("   AND Z.FENX = '累计') zhibb,\n");
		sql.append("\n"); 
		
//		结算标煤单价
		sql.append( "(SELECT JS.DIANCXXB_ID,\n");
		sql.append( "       JS.FENX FENX,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.rucsl * 0.4, JS.rucsl)) JIESL,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.gusl * 0.4, JS.gusl)) gusl,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczf*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczf,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczfs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczfs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.gusl*0.4,js.gusl)),0,0,SUM((js.gusmj+js.gusyj+js.guszf-js.gusmjs-js.gusyjs-js.guszfs)*DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))/SUM(DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))) zgj\n");
		sql.append( "  FROM YUERCBMDJ_GYS JS\n");
		sql.append( " WHERE RIQ = DATE '"+strDate+"'\n");
		sql.append(tiaoj5);
		sql.append( " GROUP BY JS.DIANCXXB_ID, JS.FENX\n");
		sql.append( "UNION\n");
		sql.append( "SELECT JS.DIANCXXB_ID,\n");
		sql.append( "      NVL('同期累计', '')  FENX,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.rucsl * 0.4, JS.rucsl)) JIESL,\n");
		sql.append( "       SUM(DECODE(JS.DIANCXXB_ID, 215, JS.gusl * 0.4, JS.gusl)) gusl,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyj*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyj,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczf*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczf,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucmjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucmjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.rucyjs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) rucyjs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.rucsl*0.4,js.rucsl)),0,0,SUM(js.ruczfs*DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))/SUM(DECODE(js.diancxxb_id,215,js.rucsl*0.4,js.rucsl))) ruczfs,\n");
		sql.append( "       DECODE(SUM(DECODE(js.diancxxb_id, 215, js.gusl*0.4,js.gusl)),0,0,SUM((js.gusmj+js.gusyj+js.guszf-js.gusmjs-js.gusyjs-js.guszfs)*DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))/SUM(DECODE(js.diancxxb_id,215,js.gusl*0.4,js.gusl))) zgj\n");
		sql.append( "  FROM YUERCBMDJ_GYS JS\n");
		sql.append( " WHERE RIQ = add_months(date'"+strDate+"',-12)\n");
		sql.append( " AND js.fenx='累计'\n");
		sql.append( " GROUP BY JS.DIANCXXB_ID) jiesbm\n");
		
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
		
		sql.append("group by rollup( dianc.fenx,dianc.fuid,dianc.mingc, dianc.xuh)\n");
		sql.append("HAVING GROUPING(dianc.fenx)+GROUPING(dianc.fuid)=1 OR GROUPING(dianc.fenx)+GROUPING(dianc.xuh)=0\n");
		sql.append("ORDER BY GROUPING(dianc.fuid)DESC,dianc.fuid,dianc.xuh,dianc.mingc,dianc.fenx)sr\n");
		
//		System.out.println(sql.toString());
		
		ResultSetList rs=cn.getResultSetList(sql.toString());
//		如果有结果集那么构造数据
		if(rs.next()){
			rs.beforefirst();
//			初始化两个List
			List arrStrCur =rs.getResultSetlist();
			List arrStrNew = new ArrayList();
//			按3行循环该数组
			for(int i=0;i<arrStrCur.size();i=i+3){
				String[] beny = (String[])arrStrCur.get(i);
				String[] leij = (String[])arrStrCur.get(i+1);
				String[] tongqlj = (String[])arrStrCur.get(i+2);
				String[] leijc = subArr(leij, tongqlj);
				arrStrNew.add(beny);
				arrStrNew.add(leij);
				arrStrNew.add(tongqlj);
				arrStrNew.add(leijc);
			}
			rs.setResultSetlist(arrStrNew);
		}
		Report rt=new Report();
		 
		//定义表头数据
		 String ArrHeader[][]=new String[2][20];
		 ArrHeader[0]=new String[] {"单位名称","分项","来煤量","耗煤量","月末库存","耗油量",
				 "进厂煤<br>热值","入炉煤<br>热值","热值差","煤价<br>(含税)","运价","杂费",
				 "入厂原煤<br>综合单价<br>(含税)","入炉标煤量<br>(含油、气)","入炉综合标煤单价",
				 "库存天然煤价(不含税)","库存煤热值","暂估量","暂估价(含运费不含税)","暂估价与本月进场煤均价差"};
		 
		 ArrHeader[1]=new String[] {"甲","乙","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","19"};

		 int ArrWidth[]=new int[] {65,63,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52};

		 rt.setTitle(strMonth+"份月度燃料管理指标情况表", ArrWidth);
		 rt.setDefaultTitle(1,13,"单位："+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(17, 4, "单位：吨、MJ/Kg、元/吨", Table.ALIGN_RIGHT);
		//设置页面
		
//		数据
		rt.setBody(new Table(rs,2,0,0));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(20);
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
	
//	累计-同期累计，得到新的字符串数组
	private String[] subArr(String[] leij, String[] tongqlj){
		String[] v = new String[leij.length];
		v[0]=leij[0];
		v[1]="累计差";
		for(int i=2;i<15;i++){
			v[i]= CustomMaths.sub(leij[i], tongqlj[i]);
		}
		return v;
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