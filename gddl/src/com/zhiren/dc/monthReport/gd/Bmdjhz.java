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

public class Bmdjhz extends BasePage {
	
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

//		tb1.addText(new ToolbarText("月份:"));
//		ComboBox yuef = new ComboBox();
//		yuef.setTransform("YuefDropDown");
//		yuef.setWidth(60);
//		tb1.addField(yuef);
//		tb1.addText(new ToolbarText("-"));
		
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
//
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);	
//		tb1.addText(new ToolbarText("-"));

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
//		String diancxxb_id = getTreeid();		
//		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		String sql=
				"select rownum,mingc,nvl(snljbmdj,0),nvl(yiybmdj,0),nvl(erybmdj,0),nvl(sanybmdj,0),nvl(siybmdj,0),nvl(wuybmdj,0),\n" +
						"nvl(liuybmdj,0),nvl(qiybmdj,0),nvl(baybmdj,0),nvl(jiuybmdj,0),nvl(shiybmdj,0),nvl(syybmdj,0),nvl(seybmdj,0),nvl(tbbmdj,0)\n" + 
						"from (select jc.mingc,jc.snljbmdj,jc.yiybmdj,jc.erybmdj,jc.sanybmdj,jc.siybmdj,jc.wuybmdj,\n" + 
						"jc.liuybmdj,jc.qiybmdj,jc.baybmdj,jc.jiuybmdj,jc.shiybmdj,jc.syybmdj,jc.seybmdj,jc.tbbmdj\n" + 
						"from (select decode(dc.mingc,null,'   小计',dc.mingc)mingc,\n" + 
						"round(decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) snljbmdj,\n" + 
						"round(decode(sum(yi.rlml),0,0,sum(yi.rldj*yi.rlml)/sum(yi.rlml)),2) yiybmdj,\n" + 
						"round(decode(sum(er.rlml),0,0,sum(er.rldj*er.rlml)/sum(er.rlml)),2) erybmdj,\n" + 
						"round(decode(sum(san.rlml),0,0,sum(san.rldj*san.rlml)/sum(san.rlml)),2) sanybmdj,\n" + 
						"round(decode(sum(si.rlml),0,0,sum(si.rldj*si.rlml)/sum(si.rlml)),2) siybmdj,\n" + 
						"round(decode(sum(wu.rlml),0,0,sum(wu.rldj*wu.rlml)/sum(wu.rlml)),2) wuybmdj,\n" + 
						"round(decode(sum(liu.rlml),0,0,sum(liu.rldj*liu.rlml)/sum(liu.rlml)),2) liuybmdj,\n" + 
						"round(decode(sum(qi.rlml),0,0,sum(qi.rldj*qi.rlml)/sum(qi.rlml)),2) qiybmdj,\n" + 
						"round(decode(sum(ba.rlml),0,0,sum(ba.rldj*ba.rlml)/sum(ba.rlml)),2) baybmdj,\n" + 
						"round(decode(sum(jiu.rlml),0,0,sum(jiu.rldj*jiu.rlml)/sum(jiu.rlml)),2) jiuybmdj,\n" + 
						"round(decode(sum(shi.rlml),0,0,sum(shi.rldj*shi.rlml)/sum(shi.rlml)),2) shiybmdj,\n" + 
						"round(decode(sum(sy.rlml),0,0,sum(sy.rldj*sy.rlml)/sum(sy.rlml)),2) syybmdj,\n" + 
						"round(decode(sum(se.rlml),0,0,sum(se.rldj*se.rlml)/sum(se.rlml)),2) seybmdj,\n" + 
						"round(decode(sum(bnlj.rlml),0,0,sum(bnlj.rldj*bnlj.rlml)/sum(bnlj.rlml))\n" + 
						"-decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) tbbmdj\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2 and dc.id not in(391,476)\n" + 
						" group by rollup ((dc.mingc,dc.xuh)) order by dc.xuh)jc\n" + 
						" union all\n" + 
						" select decode(dc.mingc,null,'   英力特小计',dc.mingc)mingc,\n" + 
						" round(decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) snljbmdj,\n" + 
						"round(decode(sum(yi.rlml),0,0,sum(yi.rldj*yi.rlml)/sum(yi.rlml)),2) yiybmdj,\n" + 
						"round(decode(sum(er.rlml),0,0,sum(er.rldj*er.rlml)/sum(er.rlml)),2) erybmdj,\n" + 
						"round(decode(sum(san.rlml),0,0,sum(san.rldj*san.rlml)/sum(san.rlml)),2) sanybmdj,\n" + 
						"round(decode(sum(si.rlml),0,0,sum(si.rldj*si.rlml)/sum(si.rlml)),2) siybmdj,\n" + 
						"round(decode(sum(wu.rlml),0,0,sum(wu.rldj*wu.rlml)/sum(wu.rlml)),2) wuybmdj,\n" + 
						"round(decode(sum(liu.rlml),0,0,sum(liu.rldj*liu.rlml)/sum(liu.rlml)),2) liuybmdj,\n" + 
						"round(decode(sum(qi.rlml),0,0,sum(qi.rldj*qi.rlml)/sum(qi.rlml)),2) qiybmdj,\n" + 
						"round(decode(sum(ba.rlml),0,0,sum(ba.rldj*ba.rlml)/sum(ba.rlml)),2) baybmdj,\n" + 
						"round(decode(sum(jiu.rlml),0,0,sum(jiu.rldj*jiu.rlml)/sum(jiu.rlml)),2) jiuybmdj,\n" + 
						"round(decode(sum(shi.rlml),0,0,sum(shi.rldj*shi.rlml)/sum(shi.rlml)),2) shiybmdj,\n" + 
						"round(decode(sum(sy.rlml),0,0,sum(sy.rldj*sy.rlml)/sum(sy.rlml)),2) syybmdj,\n" + 
						"round(decode(sum(se.rlml),0,0,sum(se.rldj*se.rlml)/sum(se.rlml)),2) seybmdj,\n" + 
						"round(decode(sum(bnlj.rlml),0,0,sum(bnlj.rldj*bnlj.rlml)/sum(bnlj.rlml))\n" + 
						"-decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) tbbmdj\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2 and dc.id in(391,476)\n" + 
						" group by rollup (dc.mingc)\n" + 
						" union all\n" + 
						" select decode(dc.mingc,null,'外二',dc.mingc)mingc,\n" + 
						" round(decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) snljbmdj,\n" + 
						"round(decode(sum(yi.rlml),0,0,sum(yi.rldj*yi.rlml)/sum(yi.rlml)),2) yiybmdj,\n" + 
						"round(decode(sum(er.rlml),0,0,sum(er.rldj*er.rlml)/sum(er.rlml)),2) erybmdj,\n" + 
						"round(decode(sum(san.rlml),0,0,sum(san.rldj*san.rlml)/sum(san.rlml)),2) sanybmdj,\n" + 
						"round(decode(sum(si.rlml),0,0,sum(si.rldj*si.rlml)/sum(si.rlml)),2) siybmdj,\n" + 
						"round(decode(sum(wu.rlml),0,0,sum(wu.rldj*wu.rlml)/sum(wu.rlml)),2) wuybmdj,\n" + 
						"round(decode(sum(liu.rlml),0,0,sum(liu.rldj*liu.rlml)/sum(liu.rlml)),2) liuybmdj,\n" + 
						"round(decode(sum(qi.rlml),0,0,sum(qi.rldj*qi.rlml)/sum(qi.rlml)),2) qiybmdj,\n" + 
						"round(decode(sum(ba.rlml),0,0,sum(ba.rldj*ba.rlml)/sum(ba.rlml)),2) baybmdj,\n" + 
						"round(decode(sum(jiu.rlml),0,0,sum(jiu.rldj*jiu.rlml)/sum(jiu.rlml)),2) jiuybmdj,\n" + 
						"round(decode(sum(shi.rlml),0,0,sum(shi.rldj*shi.rlml)/sum(shi.rlml)),2) shiybmdj,\n" + 
						"round(decode(sum(sy.rlml),0,0,sum(sy.rldj*sy.rlml)/sum(sy.rlml)),2) syybmdj,\n" + 
						"round(decode(sum(se.rlml),0,0,sum(se.rldj*se.rlml)/sum(se.rlml)),2) seybmdj,\n" + 
						"round(decode(sum(bnlj.rlml),0,0,sum(bnlj.rldj*bnlj.rlml)/sum(bnlj.rlml))\n" + 
						"-decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) tbbmdj\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2 and dc.id in(215)\n" + 
						" group by rollup (dc.mingc) having not grouping(dc.mingc)=1\n" + 
						" union all\n" + 
						" select decode(dc.mingc,null,'直管口径1(燃料)',dc.mingc)mingc,\n" + 
						" round(decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) snljbmdj,\n" + 
						"round(decode(sum(yi.rlml),0,0,sum(yi.rldj*yi.rlml)/sum(yi.rlml)),2) yiybmdj,\n" + 
						"round(decode(sum(er.rlml),0,0,sum(er.rldj*er.rlml)/sum(er.rlml)),2) erybmdj,\n" + 
						"round(decode(sum(san.rlml),0,0,sum(san.rldj*san.rlml)/sum(san.rlml)),2) sanybmdj,\n" + 
						"round(decode(sum(si.rlml),0,0,sum(si.rldj*si.rlml)/sum(si.rlml)),2) siybmdj,\n" + 
						"round(decode(sum(wu.rlml),0,0,sum(wu.rldj*wu.rlml)/sum(wu.rlml)),2) wuybmdj,\n" + 
						"round(decode(sum(liu.rlml),0,0,sum(liu.rldj*liu.rlml)/sum(liu.rlml)),2) liuybmdj,\n" + 
						"round(decode(sum(qi.rlml),0,0,sum(qi.rldj*qi.rlml)/sum(qi.rlml)),2) qiybmdj,\n" + 
						"round(decode(sum(ba.rlml),0,0,sum(ba.rldj*ba.rlml)/sum(ba.rlml)),2) baybmdj,\n" + 
						"round(decode(sum(jiu.rlml),0,0,sum(jiu.rldj*jiu.rlml)/sum(jiu.rlml)),2) jiuybmdj,\n" + 
						"round(decode(sum(shi.rlml),0,0,sum(shi.rldj*shi.rlml)/sum(shi.rlml)),2) shiybmdj,\n" + 
						"round(decode(sum(sy.rlml),0,0,sum(sy.rldj*sy.rlml)/sum(sy.rlml)),2) syybmdj,\n" + 
						"round(decode(sum(se.rlml),0,0,sum(se.rldj*se.rlml)/sum(se.rlml)),2) seybmdj,\n" + 
						"round(decode(sum(bnlj.rlml),0,0,sum(bnlj.rldj*bnlj.rlml)/sum(bnlj.rlml))\n" + 
						"-decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) tbbmdj\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2 and dc.id not in(215)\n" + 
						" group by rollup (dc.mingc) having not grouping(dc.mingc)=0\n" + 
						" union all\n" + 
						" select decode(dc.mingc,null,'直管口径2(计划)',dc.mingc)mingc,\n" + 
						" round(decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) snljbmdj,\n" + 
						"round(decode(sum(yi.rlml),0,0,sum(yi.rldj*yi.rlml)/sum(yi.rlml)),2) yiybmdj,\n" + 
						"round(decode(sum(er.rlml),0,0,sum(er.rldj*er.rlml)/sum(er.rlml)),2) erybmdj,\n" + 
						"round(decode(sum(san.rlml),0,0,sum(san.rldj*san.rlml)/sum(san.rlml)),2) sanybmdj,\n" + 
						"round(decode(sum(si.rlml),0,0,sum(si.rldj*si.rlml)/sum(si.rlml)),2) siybmdj,\n" + 
						"round(decode(sum(wu.rlml),0,0,sum(wu.rldj*wu.rlml)/sum(wu.rlml)),2) wuybmdj,\n" + 
						"round(decode(sum(liu.rlml),0,0,sum(liu.rldj*liu.rlml)/sum(liu.rlml)),2) liuybmdj,\n" + 
						"round(decode(sum(qi.rlml),0,0,sum(qi.rldj*qi.rlml)/sum(qi.rlml)),2) qiybmdj,\n" + 
						"round(decode(sum(ba.rlml),0,0,sum(ba.rldj*ba.rlml)/sum(ba.rlml)),2) baybmdj,\n" + 
						"round(decode(sum(jiu.rlml),0,0,sum(jiu.rldj*jiu.rlml)/sum(jiu.rlml)),2) jiuybmdj,\n" + 
						"round(decode(sum(shi.rlml),0,0,sum(shi.rldj*shi.rlml)/sum(shi.rlml)),2) shiybmdj,\n" + 
						"round(decode(sum(sy.rlml),0,0,sum(sy.rldj*sy.rlml)/sum(sy.rlml)),2) syybmdj,\n" + 
						"round(decode(sum(se.rlml),0,0,sum(se.rldj*se.rlml)/sum(se.rlml)),2) seybmdj,\n" + 
						"round(decode(sum(bnlj.rlml),0,0,sum(bnlj.rldj*bnlj.rlml)/sum(bnlj.rlml))\n" + 
						"-decode(sum(snlj.rlml),0,0,sum(snlj.rldj*snlj.rlml)/sum(snlj.rlml)),2) tbbmdj\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2 and dc.id not in(215,391)\n" + 
						" group by rollup (dc.mingc) having not grouping(dc.mingc)=0\n" + 
						" union all\n" + 
						"select decode(dc.mingc,null,'  小计',dc.mingc)mingc,\n" + 
						"round(decode(sum(snlj.rlbml),0,0,sum(snlj.rlzhbmdj*snlj.rlbml)/sum(snlj.rlbml)),2) snljbmdj,\n" + 
						"round(decode(sum(yi.rlbml),0,0,sum(yi.rlzhbmdj*yi.rlbml)/sum(yi.rlbml)),2) yiybmdj,\n" + 
						"round(decode(sum(er.rlbml),0,0,sum(er.rlzhbmdj*er.rlbml)/sum(er.rlbml)),2) erybmdj,\n" + 
						"round(decode(sum(san.rlbml),0,0,sum(san.rlzhbmdj*san.rlbml)/sum(san.rlbml)),2) sanybmdj,\n" + 
						"round(decode(sum(si.rlbml),0,0,sum(si.rlzhbmdj*si.rlbml)/sum(si.rlbml)),2) siybmdj,\n" + 
						"round(decode(sum(wu.rlbml),0,0,sum(wu.rlzhbmdj*wu.rlbml)/sum(wu.rlbml)),2) wuybmdj,\n" + 
						"round(decode(sum(liu.rlbml),0,0,sum(liu.rlzhbmdj*liu.rlbml)/sum(liu.rlbml)),2) liuybmdj,\n" + 
						"round(decode(sum(qi.rlbml),0,0,sum(qi.rlzhbmdj*qi.rlbml)/sum(qi.rlbml)),2) qiybmdj,\n" + 
						"round(decode(sum(ba.rlbml),0,0,sum(ba.rlzhbmdj*ba.rlbml)/sum(ba.rlbml)),2) baybmdj,\n" + 
						"round(decode(sum(jiu.rlbml),0,0,sum(jiu.rlzhbmdj*jiu.rlbml)/sum(jiu.rlbml)),2) jiuybmdj,\n" + 
						"round(decode(sum(shi.rlbml),0,0,sum(shi.rlzhbmdj*shi.rlbml)/sum(shi.rlbml)),2) shiybmdj,\n" + 
						"round(decode(sum(sy.rlbml),0,0,sum(sy.rlzhbmdj*sy.rlbml)/sum(sy.rlbml)),2) syybmdj,\n" + 
						"round(decode(sum(se.rlbml),0,0,sum(se.rlzhbmdj*se.rlbml)/sum(se.rlbml)),2) seybmdj,\n" + 
						"round(decode(sum(bnlj.rlbml),0,0,sum(bnlj.rlzhbmdj*bnlj.rlbml)/sum(bnlj.rlbml)),2)\n" + 
						"-round(decode(sum(snlj.rlbml),0,0,sum(snlj.rlzhbmdj*snlj.rlbml)/sum(snlj.rlbml)),2) tbbmdj from\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq<=add_months(date'"+getNianfValue().getValue()+"-12-1',-12) and riq>=add_months(date'"+getNianfValue().getValue()+"-1-1',-12)) snlj,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-1-1') yi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-2-1') er,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-3-1') san,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-4-1') si,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-5-1') wu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-6-1') liu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-7-1') qi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-8-1') ba,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-9-1') jiu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-10-1') shi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-11-1') sy,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-12-1') se,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq>=date'"+getNianfValue().getValue()+"-1-1' and riq<=date'"+getNianfValue().getValue()+"-12-1') bnlj,\n" + 
						"yuebdwb dc\n" + 
						"where dc.diancxxb_id=snlj.diancxxb_id(+) and dc.diancxxb_id=yi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=er.diancxxb_id(+) and dc.diancxxb_id=san.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=si.diancxxb_id(+) and dc.diancxxb_id=wu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=liu.diancxxb_id(+) and dc.diancxxb_id=qi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=ba.diancxxb_id(+) and dc.diancxxb_id=jiu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=shi.diancxxb_id(+) and dc.diancxxb_id=sy.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=se.diancxxb_id(+) and dc.diancxxb_id=bnlj.diancxxb_id(+)\n" + 
						"and dc.fuid>0\n" + 
						"group by rollup (dc.fuid,(dc.mingc))\n" + 
						"having not grouping(dc.fuid)=1\n" + 
						"union all\n" + 
						"select decode(z.mingc,null,'全口径',z.mingc)mingc,\n" + 
						"round(decode(sum(z.snljrlbml),0,0,sum(z.snljbmdj*z.snljrlbml)/sum(z.snljrlbml)),2) snljbmdj,\n" + 
						"round(decode(sum(z.yiyrlbml),0,0,sum(z.yiybmdj*z.yiyrlbml)/sum(z.yiyrlbml)),2) yiybmdj,\n" + 
						"round(decode(sum(z.eryrlbml),0,0,sum(z.erybmdj*z.eryrlbml)/sum(z.eryrlbml)),2) erybmdj,\n" + 
						"round(decode(sum(z.sanyrlbml),0,0,sum(z.sanybmdj*z.sanyrlbml)/sum(z.sanyrlbml)),2) sanybmdj,\n" + 
						"round(decode(sum(z.siyrlbml),0,0,sum(z.siybmdj*z.siyrlbml)/sum(z.siyrlbml)),2) siybmdj,\n" + 
						"round(decode(sum(z.wuyrlbml),0,0,sum(z.wuybmdj*z.wuyrlbml)/sum(z.wuyrlbml)),2) wuybmdj,\n" + 
						"round(decode(sum(z.liuyrlbml),0,0,sum(z.liuybmdj*z.liuyrlbml)/sum(z.liuyrlbml)),2) liuybmdj,\n" + 
						"round(decode(sum(z.qiyrlbml),0,0,sum(z.qiybmdj*z.qiyrlbml)/sum(z.qiyrlbml)),2) qiybmdj,\n" + 
						"round(decode(sum(z.bayrlbml),0,0,sum(z.baybmdj*z.bayrlbml)/sum(z.bayrlbml)),2) baybmdj,\n" + 
						"round(decode(sum(z.jiuyrlbml),0,0,sum(z.jiuybmdj*z.jiuyrlbml)/sum(z.jiuyrlbml)),2) jiuybmdj,\n" + 
						"round(decode(sum(z.shiyrlbml),0,0,sum(z.shiybmdj*z.shiyrlbml)/sum(z.shiyrlbml)),2) shiybmdj,\n" + 
						"round(decode(sum(z.syyrlbml),0,0,sum(z.syybmdj*z.syyrlbml)/sum(z.syyrlbml)),2) syybmdj,\n" + 
						"round(decode(sum(z.seyrlbml),0,0,sum(z.seybmdj*z.seyrlbml)/sum(z.seyrlbml)),2) seybmdj,\n" + 
						"round(decode(sum(z.bnljrlbml),0,0,sum(z.bnljbmdj*z.bnljrlbml)/sum(z.bnljrlbml)),2)\n" + 
						"-round(decode(sum(z.snljrlbml),0,0,sum(z.snljbmdj*z.snljrlbml)/sum(z.snljrlbml)),2) tbbmdj\n" + 
						"from (select dc.mingc,snlj.rlzhbmdj snljbmdj,yi.rlzhbmdj yiybmdj,er.rlzhbmdj erybmdj,\n" + 
						"san.rlzhbmdj sanybmdj,si.rlzhbmdj siybmdj,wu.rlzhbmdj wuybmdj,liu.rlzhbmdj liuybmdj,\n" + 
						"qi.rlzhbmdj qiybmdj,ba.rlzhbmdj baybmdj,jiu.rlzhbmdj jiuybmdj,shi.rlzhbmdj shiybmdj,\n" + 
						"sy.rlzhbmdj syybmdj,se.rlzhbmdj seybmdj,bnlj.rlzhbmdj bnljbmdj,\n" + 
						"decode(snlj.diancxxb_id,215,snlj.rlbml*0.4,snlj.rlbml) snljrlbml,\n" + 
						"decode(yi.diancxxb_id,215,yi.rlbml*0.4,yi.rlbml) yiyrlbml,\n" + 
						"decode(er.diancxxb_id,215,er.rlbml*0.4,er.rlbml) eryrlbml,\n" + 
						"decode(san.diancxxb_id,215,san.rlbml*0.4,san.rlbml) sanyrlbml,\n" + 
						"decode(si.diancxxb_id,215,si.rlbml*0.4,si.rlbml) siyrlbml,\n" + 
						"decode(wu.diancxxb_id,215,wu.rlbml*0.4,wu.rlbml) wuyrlbml,\n" + 
						"decode(liu.diancxxb_id,215,liu.rlbml*0.4,liu.rlbml) liuyrlbml,\n" + 
						"decode(qi.diancxxb_id,215,qi.rlbml*0.4,qi.rlbml) qiyrlbml,\n" + 
						"decode(ba.diancxxb_id,215,ba.rlbml*0.4,ba.rlbml) bayrlbml,\n" + 
						"decode(jiu.diancxxb_id,215,jiu.rlbml*0.4,jiu.rlbml) jiuyrlbml,\n" + 
						"decode(shi.diancxxb_id,215,shi.rlbml*0.4,shi.rlbml) shiyrlbml,\n" + 
						"decode(sy.diancxxb_id,215,sy.rlbml*0.4,sy.rlbml) syyrlbml,\n" + 
						"decode(se.diancxxb_id,215,se.rlbml*0.4,se.rlbml) seyrlbml,\n" + 
						"decode(bnlj.diancxxb_id,215,bnlj.rlbml*0.4,bnlj.rlbml) bnljrlbml from\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq<=add_months(date'"+getNianfValue().getValue()+"-12-1',-12) and riq>=add_months(date'"+getNianfValue().getValue()+"-1-1',-12)) snlj,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-1-1') yi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-2-1') er,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-3-1') san,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-4-1') si,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-5-1') wu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-6-1') liu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-7-1') qi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-8-1') ba,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-9-1') jiu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-10-1') shi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-11-1') sy,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-12-1') se,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq>=date'"+getNianfValue().getValue()+"-1-1' and riq<=date'"+getNianfValue().getValue()+"-12-1') bnlj,\n" + 
						"yuebdwb dc\n" + 
						"where dc.diancxxb_id=snlj.diancxxb_id(+) and dc.diancxxb_id=yi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=er.diancxxb_id(+) and dc.diancxxb_id=san.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=si.diancxxb_id(+) and dc.diancxxb_id=wu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=liu.diancxxb_id(+) and dc.diancxxb_id=qi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=ba.diancxxb_id(+) and dc.diancxxb_id=jiu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=shi.diancxxb_id(+) and dc.diancxxb_id=sy.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=se.diancxxb_id(+) and dc.diancxxb_id=bnlj.diancxxb_id(+)\n" + 
						"and dc.fuid>0\n" + 
						"union all\n" + 
						"select* from (select dc.mingc,snlj.rldj snljbmdj,yi.rldj yiybmdj,er.rldj erybmdj,\n" + 
						"san.rldj sanybmdj,si.rldj siybmdj,wu.rldj wuybmdj,liu.rldj liuybmdj,\n" + 
						"qi.rldj qiybmdj,ba.rldj baybmdj,jiu.rldj jiuybmdj,shi.rldj shiybmdj,\n" + 
						"sy.rldj syybmdj,se.rldj seybmdj,bnlj.rldj bnljbmdj,\n" + 
						"decode(snlj.diancxxb_id,215,snlj.rlbml*0.4,snlj.rlbml) snljrlbml,\n" + 
						"decode(yi.diancxxb_id,215,yi.rlbml*0.4,yi.rlbml) yiyrlbml,\n" + 
						"decode(er.diancxxb_id,215,er.rlbml*0.4,er.rlbml) eryrlbml,\n" + 
						"decode(san.diancxxb_id,215,san.rlbml*0.4,san.rlbml) sanyrlbml,\n" + 
						"decode(si.diancxxb_id,215,si.rlbml*0.4,si.rlbml) siyrlbml,\n" + 
						"decode(wu.diancxxb_id,215,wu.rlbml*0.4,wu.rlbml) wuyrlbml,\n" + 
						"decode(liu.diancxxb_id,215,liu.rlbml*0.4,liu.rlbml) liuyrlbml,\n" + 
						"decode(qi.diancxxb_id,215,qi.rlbml*0.4,qi.rlbml) qiyrlbml,\n" + 
						"decode(ba.diancxxb_id,215,ba.rlbml*0.4,ba.rlbml) bayrlbml,\n" + 
						"decode(jiu.diancxxb_id,215,jiu.rlbml*0.4,jiu.rlbml) jiuyrlbml,\n" + 
						"decode(shi.diancxxb_id,215,shi.rlbml*0.4,shi.rlbml) shiyrlbml,\n" + 
						"decode(sy.diancxxb_id,215,sy.rlbml*0.4,sy.rlbml) syyrlbml,\n" + 
						"decode(se.diancxxb_id,215,se.rlbml*0.4,se.rlbml) seyrlbml,\n" + 
						"decode(bnlj.diancxxb_id,215,bnlj.rlbml*0.4,bnlj.rlbml) bnljrlbml\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlbml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2))z\n" + 
						" group by rollup (z.mingc)\n" + 
						" having not grouping(z.mingc)=0\n" + 
						" union all\n" + 
						" select decode(z.mingc,null,'资产口径1(燃料)',z.mingc)mingc,\n" + 
						"round(decode(sum(z.snljrlbml),0,0,sum(z.snljbmdj*z.snljrlbml)/sum(z.snljrlbml)),2) snljbmdj,\n" + 
						"round(decode(sum(z.yiyrlbml),0,0,sum(z.yiybmdj*z.yiyrlbml)/sum(z.yiyrlbml)),2) yiybmdj,\n" + 
						"round(decode(sum(z.eryrlbml),0,0,sum(z.erybmdj*z.eryrlbml)/sum(z.eryrlbml)),2) erybmdj,\n" + 
						"round(decode(sum(z.sanyrlbml),0,0,sum(z.sanybmdj*z.sanyrlbml)/sum(z.sanyrlbml)),2) sanybmdj,\n" + 
						"round(decode(sum(z.siyrlbml),0,0,sum(z.siybmdj*z.siyrlbml)/sum(z.siyrlbml)),2) siybmdj,\n" + 
						"round(decode(sum(z.wuyrlbml),0,0,sum(z.wuybmdj*z.wuyrlbml)/sum(z.wuyrlbml)),2) wuybmdj,\n" + 
						"round(decode(sum(z.liuyrlbml),0,0,sum(z.liuybmdj*z.liuyrlbml)/sum(z.liuyrlbml)),2) liuybmdj,\n" + 
						"round(decode(sum(z.qiyrlbml),0,0,sum(z.qiybmdj*z.qiyrlbml)/sum(z.qiyrlbml)),2) qiybmdj,\n" + 
						"round(decode(sum(z.bayrlbml),0,0,sum(z.baybmdj*z.bayrlbml)/sum(z.bayrlbml)),2) baybmdj,\n" + 
						"round(decode(sum(z.jiuyrlbml),0,0,sum(z.jiuybmdj*z.jiuyrlbml)/sum(z.jiuyrlbml)),2) jiuybmdj,\n" + 
						"round(decode(sum(z.shiyrlbml),0,0,sum(z.shiybmdj*z.shiyrlbml)/sum(z.shiyrlbml)),2) shiybmdj,\n" + 
						"round(decode(sum(z.syyrlbml),0,0,sum(z.syybmdj*z.syyrlbml)/sum(z.syyrlbml)),2) syybmdj,\n" + 
						"round(decode(sum(z.seyrlbml),0,0,sum(z.seybmdj*z.seyrlbml)/sum(z.seyrlbml)),2) seybmdj,\n" + 
						"round(decode(sum(z.bnljrlbml),0,0,sum(z.bnljbmdj*z.bnljrlbml)/sum(z.bnljrlbml)),2)\n" + 
						"-round(decode(sum(z.snljrlbml),0,0,sum(z.snljbmdj*z.snljrlbml)/sum(z.snljrlbml)),2) tbbmdj\n" + 
						"from (select dc.mingc,snlj.rlzhbmdj snljbmdj,yi.rlzhbmdj yiybmdj,er.rlzhbmdj erybmdj,\n" + 
						"san.rlzhbmdj sanybmdj,si.rlzhbmdj siybmdj,wu.rlzhbmdj wuybmdj,liu.rlzhbmdj liuybmdj,\n" + 
						"qi.rlzhbmdj qiybmdj,ba.rlzhbmdj baybmdj,jiu.rlzhbmdj jiuybmdj,shi.rlzhbmdj shiybmdj,\n" + 
						"sy.rlzhbmdj syybmdj,se.rlzhbmdj seybmdj,bnlj.rlzhbmdj bnljbmdj,snlj.rlbml snljrlbml,\n" + 
						"yi.rlbml yiyrlbml,er.rlbml eryrlbml,san.rlbml sanyrlbml,si.rlbml siyrlbml,wu.rlbml wuyrlbml,\n" + 
						"liu.rlbml liuyrlbml,qi.rlbml qiyrlbml,ba.rlbml bayrlbml,jiu.rlbml jiuyrlbml,shi.rlbml shiyrlbml,\n" + 
						"sy.rlbml syyrlbml,se.rlbml seyrlbml,bnlj.rlbml bnljrlbml from\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq<=add_months(date'"+getNianfValue().getValue()+"-12-1',-12) and riq>=add_months(date'"+getNianfValue().getValue()+"-1-1',-12)) snlj,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-1-1') yi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-2-1') er,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-3-1') san,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-4-1') si,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-5-1') wu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-6-1') liu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-7-1') qi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-8-1') ba,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-9-1') jiu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-10-1') shi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-11-1') sy,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-12-1') se,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq>=date'"+getNianfValue().getValue()+"-1-1' and riq<=date'"+getNianfValue().getValue()+"-12-1') bnlj,\n" + 
						"yuebdwb dc\n" + 
						"where dc.diancxxb_id=snlj.diancxxb_id(+) and dc.diancxxb_id=yi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=er.diancxxb_id(+) and dc.diancxxb_id=san.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=si.diancxxb_id(+) and dc.diancxxb_id=wu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=liu.diancxxb_id(+) and dc.diancxxb_id=qi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=ba.diancxxb_id(+) and dc.diancxxb_id=jiu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=shi.diancxxb_id(+) and dc.diancxxb_id=sy.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=se.diancxxb_id(+) and dc.diancxxb_id=bnlj.diancxxb_id(+)\n" + 
						"and dc.fuid>0\n" + 
						"union all\n" + 
						"select* from (select dc.mingc,snlj.rldj snljbmdj,yi.rldj yiybmdj,er.rldj erybmdj,\n" + 
						"san.rldj sanybmdj,si.rldj siybmdj,wu.rldj wuybmdj,liu.rldj liuybmdj,\n" + 
						"qi.rldj qiybmdj,ba.rldj baybmdj,jiu.rldj jiuybmdj,shi.rldj shiybmdj,\n" + 
						"sy.rldj syybmdj,se.rldj seybmdj,bnlj.rldj bnljbmdj,snlj.rlml snljrlbml,\n" + 
						"yi.rlml yiyrlbml,er.rlml eryrlbml,san.rlml sanyrlbml,si.rlml sirlbml,wu.rlml wuyrlbml,\n" + 
						"liu.rlml liuyrlbml,qi.rlml qiyrlbml,ba.rlml bayrlbml,jiu.rlml jiuyrlbml,shi.rlml shiyrlbml,\n" + 
						"sy.rlml syyrlbml,se.rlml seyrlbml,bnlj.rlml bnljrlbml\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2 and dc.id not in(215)))z\n" + 
						" group by rollup (z.mingc)\n" + 
						" having not grouping(z.mingc)=0\n" + 
						" union all\n" + 
						" select decode(z.mingc,null,'资产口径2(计划)',z.mingc)mingc,\n" + 
						"round(decode(sum(z.snljrlbml),0,0,sum(z.snljbmdj*z.snljrlbml)/sum(z.snljrlbml)),2) snljbmdj,\n" + 
						"round(decode(sum(z.yiyrlbml),0,0,sum(z.yiybmdj*z.yiyrlbml)/sum(z.yiyrlbml)),2) yiybmdj,\n" + 
						"round(decode(sum(z.eryrlbml),0,0,sum(z.erybmdj*z.eryrlbml)/sum(z.eryrlbml)),2) erybmdj,\n" + 
						"round(decode(sum(z.sanyrlbml),0,0,sum(z.sanybmdj*z.sanyrlbml)/sum(z.sanyrlbml)),2) sanybmdj,\n" + 
						"round(decode(sum(z.siyrlbml),0,0,sum(z.siybmdj*z.siyrlbml)/sum(z.siyrlbml)),2) siybmdj,\n" + 
						"round(decode(sum(z.wuyrlbml),0,0,sum(z.wuybmdj*z.wuyrlbml)/sum(z.wuyrlbml)),2) wuybmdj,\n" + 
						"round(decode(sum(z.liuyrlbml),0,0,sum(z.liuybmdj*z.liuyrlbml)/sum(z.liuyrlbml)),2) liuybmdj,\n" + 
						"round(decode(sum(z.qiyrlbml),0,0,sum(z.qiybmdj*z.qiyrlbml)/sum(z.qiyrlbml)),2) qiybmdj,\n" + 
						"round(decode(sum(z.bayrlbml),0,0,sum(z.baybmdj*z.bayrlbml)/sum(z.bayrlbml)),2) baybmdj,\n" + 
						"round(decode(sum(z.jiuyrlbml),0,0,sum(z.jiuybmdj*z.jiuyrlbml)/sum(z.jiuyrlbml)),2) jiuybmdj,\n" + 
						"round(decode(sum(z.shiyrlbml),0,0,sum(z.shiybmdj*z.shiyrlbml)/sum(z.shiyrlbml)),2) shiybmdj,\n" + 
						"round(decode(sum(z.syyrlbml),0,0,sum(z.syybmdj*z.syyrlbml)/sum(z.syyrlbml)),2) syybmdj,\n" + 
						"round(decode(sum(z.seyrlbml),0,0,sum(z.seybmdj*z.seyrlbml)/sum(z.seyrlbml)),2) seybmdj,\n" + 
						"round(decode(sum(z.bnljrlbml),0,0,sum(z.bnljbmdj*z.bnljrlbml)/sum(z.bnljrlbml)),2)\n" + 
						"-round(decode(sum(z.snljrlbml),0,0,sum(z.snljbmdj*z.snljrlbml)/sum(z.snljrlbml)),2) tbbmdj\n" + 
						"from (select dc.mingc,snlj.rlzhbmdj snljbmdj,yi.rlzhbmdj yiybmdj,er.rlzhbmdj erybmdj,\n" + 
						"san.rlzhbmdj sanybmdj,si.rlzhbmdj siybmdj,wu.rlzhbmdj wuybmdj,liu.rlzhbmdj liuybmdj,\n" + 
						"qi.rlzhbmdj qiybmdj,ba.rlzhbmdj baybmdj,jiu.rlzhbmdj jiuybmdj,shi.rlzhbmdj shiybmdj,\n" + 
						"sy.rlzhbmdj syybmdj,se.rlzhbmdj seybmdj,bnlj.rlzhbmdj bnljbmdj,snlj.rlbml snljrlbml,\n" + 
						"yi.rlbml yiyrlbml,er.rlbml eryrlbml,san.rlbml sanyrlbml,si.rlbml siyrlbml,wu.rlbml wuyrlbml,\n" + 
						"liu.rlbml liuyrlbml,qi.rlbml qiyrlbml,ba.rlbml bayrlbml,jiu.rlbml jiuyrlbml,shi.rlbml shiyrlbml,\n" + 
						"sy.rlbml syyrlbml,se.rlbml seyrlbml,bnlj.rlbml bnljrlbml from\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq<=add_months(date'"+getNianfValue().getValue()+"-12-1',-12) and riq>=add_months(date'"+getNianfValue().getValue()+"-1-1',-12)) snlj,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-1-1') yi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-2-1') er,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-3-1') san,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-4-1') si,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-5-1') wu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-6-1') liu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-7-1') qi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-8-1') ba,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-9-1') jiu,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-10-1') shi,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-11-1') sy,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq=date'"+getNianfValue().getValue()+"-12-1') se,\n" + 
						"(select diancxxb_id,rlzhbmdj,rlbml from ZICKJSJB where riq>=date'"+getNianfValue().getValue()+"-1-1' and riq<=date'"+getNianfValue().getValue()+"-12-1') bnlj,\n" + 
						"yuebdwb dc\n" + 
						"where dc.diancxxb_id=snlj.diancxxb_id(+) and dc.diancxxb_id=yi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=er.diancxxb_id(+) and dc.diancxxb_id=san.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=si.diancxxb_id(+) and dc.diancxxb_id=wu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=liu.diancxxb_id(+) and dc.diancxxb_id=qi.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=ba.diancxxb_id(+) and dc.diancxxb_id=jiu.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=shi.diancxxb_id(+) and dc.diancxxb_id=sy.diancxxb_id(+)\n" + 
						"and dc.diancxxb_id=se.diancxxb_id(+) and dc.diancxxb_id=bnlj.diancxxb_id(+)\n" + 
						"and dc.fuid>0\n" + 
						"union all\n" + 
						"select* from (select dc.mingc,snlj.rldj snljbmdj,yi.rldj yiybmdj,er.rldj erybmdj,\n" + 
						"san.rldj sanybmdj,si.rldj siybmdj,wu.rldj wuybmdj,liu.rldj liuybmdj,\n" + 
						"qi.rldj qiybmdj,ba.rldj baybmdj,jiu.rldj jiuybmdj,shi.rldj shiybmdj,\n" + 
						"sy.rldj syybmdj,se.rldj seybmdj,bnlj.rldj bnljbmdj,snlj.rlml snljrlbml,\n" + 
						"yi.rlml yiyrlbml,er.rlml eryrlbml,san.rlml sanyrlbml,si.rlml sirlbml,wu.rlml wuyrlbml,\n" + 
						"liu.rlml liuyrlbml,qi.rlml qiyrlbml,ba.rlml bayrlbml,jiu.rlml jiuyrlbml,shi.rlml shiyrlbml,\n" + 
						"sy.rlml syyrlbml,se.rlml seyrlbml,bnlj.rlml bnljrlbml\n" + 
						"from (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')yi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')er,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')san,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')si,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')wu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')liu,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')qi,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')ba,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')jiu,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')shi,\n" + 
						"(SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')sy,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '本月')se,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '2012-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')snlj,\n" + 
						" (SELECT Zb.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"       (Zb.RULMZBZML + Zb.RULYZBZML + Zb.RULQZBZML)rlml,\n" + 
						"       Zb.RULZHBMDJ rldj\n" + 
						"  FROM YUEZBB Zb\n" + 
						" WHERE Zb.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						"   AND (zb.zhuangt = 1 OR zb.zhuangt = 3)\n" + 
						"   and zb.fenx = '累计')bnlj,\n" + 
						" diancxxb dc\n" + 
						" where dc.id=yi.diancxxb_id(+) and dc.id=er.diancxxb_id(+) and\n" + 
						" dc.id=san.diancxxb_id(+) and dc.id=si.diancxxb_id(+) and\n" + 
						" dc.id=wu.diancxxb_id(+) and dc.id=liu.diancxxb_id(+) and\n" + 
						" dc.id=qi.diancxxb_id(+) and dc.id=ba.diancxxb_id(+) and\n" + 
						" dc.id=jiu.diancxxb_id(+) and dc.id=shi.diancxxb_id(+) and\n" + 
						" dc.id=sy.diancxxb_id(+) and dc.id=se.diancxxb_id(+) and\n" + 
						" dc.id=snlj.diancxxb_id(+) and dc.id=bnlj.diancxxb_id(+)\n" + 
						" and dc.jib>2 and dc.id not in(215,391)))z\n" + 
						" group by rollup (z.mingc)\n" + 
						" having not grouping(z.mingc)=0)";
		ResultSetList rs=cn.getResultSetList(sql.toString());
		Report rt=new Report();
		 String ArrHeader[][]=new String[2][17];
		 int nian=Integer.parseInt(getNianfValue().getValue());
		 ArrHeader[0]=new String[] {"序号","单位",(nian-1)+"年",getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",
				 getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",
				 getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",getNianfValue().getValue()+"年",
				 getNianfValue().getValue()+"年",getNianfValue().getValue()+"年"};
		 ArrHeader[1]=new String[] {"序号","单位",(nian-1)+"年","1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月","累计","同比"};

		 int ArrWidth[]=new int[] {40,120,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60};

		 rt.setTitle(getNianfValue().getValue()+"标煤价、热值差汇总", ArrWidth);
		 rt.setDefaultTitle(2,4,"单位："+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(14, 3, "单位：元/吨", Table.ALIGN_RIGHT);
		//设置页面
		rt.setBody(new Table(rs,2,0,0));
		rt.body.setWidth(ArrWidth);

//		rt.body.setPageRows(99);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeCol(1);
		rt.body.mergeFixedRowCol();
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