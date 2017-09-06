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
 * 作者：韩超
 * 时间：2014-03-13
 * 描述：入厂标煤价
 */

public class Yuercbmj extends BasePage {
	
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
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
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
		String sql="select zong.mingc,zong.dj,zong.tongb,zong.huanb,zong.lj,zong.ljtb\n" +
			" from(select -1 xuh,'直管口径1(燃料)' mingc,sum(z1.bybuhsbmdj)dj,(sum(z1.snbybuhsbmdj)-sum(z1.bybuhsbmdj))tongb,\n" +
			"       (sum(z1.sybuhsbmdj)-sum(z1.bybuhsbmdj))huanb,sum(z1.ljbuhsbmdj)lj,(sum(z1.ljbuhsbmdj)-sum(z1.snljbuhsbmdj))ljtb\n" + 
			"       from(select beny.riq,beny.fenx,ROUND(decode(beny.QNET_AR,0,0,(beny.MEIJ/1.17 + beny.YUNJ-beny.YUNJS + beny.ZAF-beny.ZAFS) * 29.271 / beny.QNET_AR),2) BYBUHSBMDJ,\n" + 
			"      ROUND(decode(leij.QNET_AR,0,0,(leij.MEIJ/1.17 + leij.YUNJ-leij.YUNJS + leij.ZAF-leij.ZAFS) * 29.271 / leij.QNET_AR),2) LJBUHSBMDJ,\n" + 
			"      ROUND(decode(shangnby.QNET_AR,0,0,(shangnby.MEIJ/1.17 + shangnby.YUNJ-shangnby.YUNJS + shangnby.ZAF-shangnby.ZAFS) * 29.271 / shangnby.QNET_AR),2) SNBYBUHSBMDJ,\n" + 
			"      ROUND(decode(shangnlj.QNET_AR,0,0,(shangnlj.MEIJ/1.17 + shangnlj.YUNJ-shangnlj.YUNJS + shangnlj.ZAF-shangnlj.ZAFS) * 29.271 / shangnlj.QNET_AR),2) SNLJBUHSBMDJ,\n" + 
			"      ROUND(decode(shangy.QNET_AR,0,0,(shangy.MEIJ/1.17 + shangy.YUNJ-shangy.YUNJS + shangy.ZAF-shangy.ZAFS) * 29.271 / shangy.QNET_AR),2) SYBUHSBMDJ\n" + 
			"      from (select k.riq,y.fenx,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"             round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                          SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                          SUM(JIESL)), 2) ZAF,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"        from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"       where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = to_date('"+ strDate+ "','yyyy-mm-dd')\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215)\n" + 
			"         and k.diancxxb_id = dc.id\n" + 
			"      group by k.riq,y.fenx\n" + 
			"    order by riq,fenx)beny ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='累计')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = to_date('"+ strDate+ "','yyyy-mm-dd')\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by  k.riq,y.fenx\n" + 
			"    order by riq,fenx)leij ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-12)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by k.riq,y.fenx\n" + 
			"    order by riq,fenx)shangnby ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='累计')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-12)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by k.riq,y.fenx\n" + 
			"    order by riq,fenx\n" + 
			"    )shangnlj ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-1)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by k.riq,y.fenx\n" + 
			"    order by riq,fenx)shangy)z1\n" + 
			"    union all\n" + 
			"    select 0 xuh,'直管口径2(计划)' mingc,sum(z1.bybuhsbmdj)dj,(sum(z1.snbybuhsbmdj)-sum(z1.bybuhsbmdj))tongb,\n" + 
			"       (sum(z1.sybuhsbmdj)-sum(z1.bybuhsbmdj))huanb,sum(z1.ljbuhsbmdj)lj,(sum(z1.ljbuhsbmdj)-sum(z1.snljbuhsbmdj))ljtb\n" + 
			"       from(select beny.riq,beny.fenx,ROUND(decode(beny.QNET_AR,0,0,(beny.MEIJ/1.17 + beny.YUNJ-beny.YUNJS + beny.ZAF-beny.ZAFS) * 29.271 / beny.QNET_AR),2) BYBUHSBMDJ,\n" + 
			"      ROUND(decode(leij.QNET_AR,0,0,(leij.MEIJ/1.17 + leij.YUNJ-leij.YUNJS + leij.ZAF-leij.ZAFS) * 29.271 / leij.QNET_AR),2) LJBUHSBMDJ,\n" + 
			"      ROUND(decode(shangnby.QNET_AR,0,0,(shangnby.MEIJ/1.17 + shangnby.YUNJ-shangnby.YUNJS + shangnby.ZAF-shangnby.ZAFS) * 29.271 / shangnby.QNET_AR),2) SNBYBUHSBMDJ,\n" + 
			"      ROUND(decode(shangnlj.QNET_AR,0,0,(shangnlj.MEIJ/1.17 + shangnlj.YUNJ-shangnlj.YUNJS + shangnlj.ZAF-shangnlj.ZAFS) * 29.271 / shangnlj.QNET_AR),2) SNLJBUHSBMDJ,\n" + 
			"      ROUND(decode(shangy.QNET_AR,0,0,(shangy.MEIJ/1.17 + shangy.YUNJ-shangy.YUNJS + shangy.ZAF-shangy.ZAFS) * 29.271 / shangy.QNET_AR),2) SYBUHSBMDJ\n" + 
			"      from (select k.riq,y.fenx,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"             round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                          SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                          SUM(JIESL)), 2) ZAF,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"        from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"       where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = to_date('"+ strDate+ "','yyyy-mm-dd')\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215,391)\n" + 
			"         and k.diancxxb_id = dc.id\n" + 
			"      group by k.riq,y.fenx\n" + 
			"    order by riq,fenx)beny ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='累计')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = to_date('"+ strDate+ "','yyyy-mm-dd')\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215,391)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by  k.riq,y.fenx\n" + 
			"    order by riq,fenx)leij ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-12)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215,391)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by k.riq,y.fenx\n" + 
			"    order by riq,fenx)shangnby ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='累计')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-12)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215,391)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by k.riq,y.fenx\n" + 
			"    order by riq,fenx\n" + 
			"    )shangnlj ,\n" + 
			"    (select  k.riq,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-1)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+") and k.diancxxb_id not in(215,391)\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by k.riq,y.fenx\n" + 
			"    order by riq,fenx)shangy)z1\n" + 
			"	 union all\n" + 
			"    select z.xuh,z.mingc,z.bybuhsbmdj,(z.snbybuhsbmdj-z.bybuhsbmdj)tongb,\n" + 
			"(z.sybuhsbmdj-z.bybuhsbmdj)huanb,z.ljbuhsbmdj,(z.ljbuhsbmdj-z.snljbuhsbmdj)ljtb\n" + 
			" from (select beny.xuh,beny.mingc,\n" + 
			"      ROUND(decode(beny.QNET_AR,0,0,(beny.MEIJ/1.17 + beny.YUNJ-beny.YUNJS + beny.ZAF-beny.ZAFS) * 29.271 / beny.QNET_AR),2) BYBUHSBMDJ,\n" + 
			"      ROUND(decode(leij.QNET_AR,0,0,(leij.MEIJ/1.17 + leij.YUNJ-leij.YUNJS + leij.ZAF-leij.ZAFS) * 29.271 / leij.QNET_AR),2) LJBUHSBMDJ,\n" + 
			"      ROUND(decode(shangnby.QNET_AR,0,0,(shangnby.MEIJ/1.17 + shangnby.YUNJ-shangnby.YUNJS + shangnby.ZAF-shangnby.ZAFS) * 29.271 / shangnby.QNET_AR),2) SNBYBUHSBMDJ,\n" + 
			"      ROUND(decode(shangnlj.QNET_AR,0,0,(shangnlj.MEIJ/1.17 + shangnlj.YUNJ-shangnlj.YUNJS + shangnlj.ZAF-shangnlj.ZAFS) * 29.271 / shangnlj.QNET_AR),2) SNLJBUHSBMDJ,\n" + 
			"      ROUND(decode(shangy.QNET_AR,0,0,(shangy.MEIJ/1.17 + shangy.YUNJ-shangy.YUNJS + shangy.ZAF-shangy.ZAFS) * 29.271 / shangy.QNET_AR),2) SYBUHSBMDJ\n" + 
			"      from (select dc.xuh,dc.mingc,k.riq,k.diancxxb_id,y.fenx,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"             round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                          SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                          SUM(JIESL)), 2) ZAF,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"             round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"        from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"       where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = to_date('"+ strDate+ "','yyyy-mm-dd')\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+")\n" + 
			"         and k.diancxxb_id = dc.id\n" + 
			"      group by   dc.xuh,dc.mingc, k.riq,k.diancxxb_id,y.fenx\n" + 
			"    order by dc.xuh,dc.mingc,riq,fenx)beny ,\n" + 
			"    (select dc.xuh,dc.mingc,k.riq,k.diancxxb_id,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='累计')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = to_date('"+ strDate+ "','yyyy-mm-dd')\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+")\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by   dc.xuh,dc.mingc, k.riq,k.diancxxb_id,y.fenx\n" + 
			"    order by dc.xuh,dc.mingc,riq,fenx)leij ,\n" + 
			"    (select dc.xuh,dc.mingc,k.riq,k.diancxxb_id,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-12)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+")\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by   dc.xuh,dc.mingc, k.riq,k.diancxxb_id,y.fenx\n" + 
			"    order by dc.xuh,dc.mingc,riq,fenx)shangnby ,\n" + 
			"    (select dc.xuh,dc.mingc,k.riq,k.diancxxb_id,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='累计')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-12)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+")\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by   dc.xuh,dc.mingc, k.riq,k.diancxxb_id,y.fenx\n" + 
			"    order by dc.xuh,dc.mingc,riq,fenx\n" + 
			"    )shangnlj ,\n" + 
			"    (select dc.xuh,dc.mingc,k.riq,k.diancxxb_id,y.fenx,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)), 2) YUNJ,\n" + 
			"           round(DECODE(SUM(JIESL), 0,0,\n" + 
			"                        SUM((Y.ZAF + Y.DAOZZF + Y.QIT + Y.KUANGQYF) * JIESL) /\n" + 
			"                        SUM(JIESL)), 2) ZAF,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)), 2) QNET_AR,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)), 2) ZAFS,\n" + 
			"           round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)), 2) YUNJS\n" + 
			"      from (select * from YUEJSBMDJ where (ZHUANGT=1 OR ZHUANGT=3) and fenx='本月')y, yuetjkjb k,diancxxb dc\n" + 
			"     where y.yuetjkjb_id = k.id\n" + 
			"         and k.riq = add_months(to_date('"+ strDate+ "','yyyy-mm-dd'),-1)\n" +
			"		  and k.diancxxb_id in ("+diancxxb_id+")\n" + 
			"       and k.diancxxb_id = dc.id\n" + 
			"    group by   dc.xuh,dc.mingc, k.riq,k.diancxxb_id,y.fenx\n" + 
			"    order by dc.xuh,dc.mingc,riq,fenx)shangy\n" + 
			"    where beny.diancxxb_id=leij.diancxxb_id and beny.diancxxb_id=shangnby.diancxxb_id\n" + 
			"    and beny.diancxxb_id=shangnlj.diancxxb_id and beny.diancxxb_id=shangy.diancxxb_id)z)zong\n" + 
			"    order by zong.xuh,zong.mingc desc";


		ResultSetList rs=cn.getResultSetList(sql.toString());
//		System.out.println(sql);
		Report rt=new Report();
		//定义表头数据
		 String ArrHeader[][]=new String[2][6];
		 ArrHeader[0]=new String[] {"单 位","当月(元/吨)","当月(元/吨)","当月(元/吨)","累计(元/吨)","累计(元/吨)"};
		 ArrHeader[1]=new String[] {"单 位","2013年","同比","环比","2013年","同比"};

		 int ArrWidth[]=new int[] {120,120,120,120,120,120};


		 rt.setTitle(strMonth+"入厂标煤单价情况（不含税）", ArrWidth);
		 rt.setDefaultTitle(1,3,"单位："+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(4, 3, "单位：元/吨", Table.ALIGN_RIGHT);
		//设置页面
		
//		数据
		rt.setBody(new Table(rs,2,0,0));
		rt.body.setWidth(ArrWidth);

//		rt.body.setPageRows(99);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeCol(1);
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);

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
	
	// 页面初始设置
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
}