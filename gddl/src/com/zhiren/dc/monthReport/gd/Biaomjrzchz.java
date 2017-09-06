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

public class Biaomjrzchz extends BasePage {
	
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
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		String sql=
				"SELECT rownum,decode(diac.mingc,null,'直管口径1(燃料)',diac.mingc),nvl(jh.rlbmdjxyjh,0),nvl(shuj.rulbmdjby,0),nvl(benyjh.rlbmdjxyjh,0) rlbmdjbyjh,\n" +
						"nvl(shuj.rulbmdjby-benyjh.rlbmdjxyjh,0) bybmdjbjhzj,nvl(shuj.rulbmdjby-shuj.rulbmdjsy,0) bmdjhbzj,nvl(shuj.rulbmdjby-shuj.rulbmdjtq,0) bmdjtbzj,\n" + 
						"nvl(jh.rlbmdjbnjh,0),nvl(shuj.rulbmdjlj,0),nvl(shuj.rulbmdjlj-jh.rlbmdjbnjh,0) ljbmdjbjhzj,nvl(shuj.rulbmdjlj-shuj.rulbmdjtqlj,0) ljbmdjtbzj,\n" + 
						"nvl(jh.rzcxyjh,0),nvl(shuj.rezby-shuj.rulrzby,0) rezcby,nvl(jh.rzcbyjh,0),nvl(shuj.rezby-shuj.rulrzby-jh.rzcbyjh,0) rzcbybjhzj,\n" + 
						"nvl((shuj.rezby-shuj.rulrzby)-(shuj.rezsy-shuj.rulrzsy),0) rzchbzj,nvl((shuj.rezby-shuj.rulrzby)-(shuj.reztq-shuj.rulrztq),0) rzctbzj,\n" + 
						"nvl(jh.rzcbnjh,0),nvl(shuj.rezlj-shuj.rulrzlj,0) rezclj,nvl(shuj.rezlj-shuj.rulrzlj-jh.rzcbnjh,0) rzcbjhzj,\n" + 
						"nvl((shuj.rezlj-shuj.rulrzlj)-(shuj.reztqlj-shuj.rulrztqlj),0) rzctbzj\n" + 
						"from(select dc.id diancxxb_id,shulby.fenx,\n" + 
						"round_new(decode(sum(shulby.jincml),0,0,sum(shulby.rez * shulby.jincml) / sum(shulby.jincml)),2) rezby,\n" + 
						"round_new(decode(sum(zhibbby.fadgrytrml),0,0,sum(zhibbby.rultrmpjfrl*zhibbby.fadgrytrml)/sum(zhibbby.fadgrytrml)),2) rulrzby,\n" + 
						"round_new(decode(sum(zhibbby.rulbml),0,0,sum(zhibbby.bmdj*zhibbby.rulbml)/sum(zhibbby.rulbml)),2)rulbmdjby,\n" + 
						"round_new(decode(sum(shulsy.jincml),0,0,sum(shulsy.rez * shulsy.jincml) / sum(shulsy.jincml)),2) rezsy,\n" + 
						"round_new(decode(sum(zhibbsy.fadgrytrml),0,0,sum(zhibbsy.rultrmpjfrl*zhibbsy.fadgrytrml)/sum(zhibbsy.fadgrytrml)),2) rulrzsy,\n" + 
						"round_new(decode(sum(zhibbsy.rulbml),0,0,sum(zhibbsy.bmdj*zhibbsy.rulbml)/sum(zhibbsy.rulbml)),2)rulbmdjsy,\n" + 
						"round_new(decode(sum(shultq.jincml),0,0,sum(shultq.rez * shultq.jincml) / sum(shultq.jincml)),2) reztq,\n" + 
						"round_new(decode(sum(zhibbtq.fadgrytrml),0,0,sum(zhibbtq.rultrmpjfrl*zhibbtq.fadgrytrml)/sum(zhibbtq.fadgrytrml)),2) rulrztq,\n" + 
						"round_new(decode(sum(zhibbtq.rulbml),0,0,sum(zhibbtq.bmdj*zhibbtq.rulbml)/sum(zhibbtq.rulbml)),2)rulbmdjtq,\n" + 
						"round_new(decode(sum(shullj.jincml),0,0,sum(shullj.rez * shullj.jincml) / sum(shullj.jincml)),2) rezlj,\n" + 
						"round_new(decode(sum(zhibblj.fadgrytrml),0,0,sum(zhibblj.rultrmpjfrl*zhibblj.fadgrytrml)/sum(zhibblj.fadgrytrml)),2) rulrzlj,\n" + 
						"round_new(decode(sum(zhibblj.rulbml),0,0,sum(zhibblj.bmdj*zhibblj.rulbml)/sum(zhibblj.rulbml)),2)rulbmdjlj,\n" + 
						"round_new(decode(sum(shultqlj.jincml),0,0,sum(shultqlj.rez * shultqlj.jincml) / sum(shultqlj.jincml)),2) reztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.fadgrytrml),0,0,sum(zhibbtqlj.rultrmpjfrl*zhibbtqlj.fadgrytrml)/sum(zhibbtqlj.fadgrytrml)),2) rulrztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.rulbml),0,0,sum(zhibbtqlj.bmdj*zhibbtqlj.rulbml)/sum(zhibbtqlj.rulbml)),2)rulbmdjtqlj\n" + 
						"from\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
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
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulby,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbby,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-1))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulsy,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-1)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbsy,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultq,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtq,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
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
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shullj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = DATE '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibblj,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultqlj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtqlj,\n" + 
						"    diancxxb dc\n" + 
						"where shulby.diancxxb_id=zhibbby.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shulsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shullj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibblj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=dc.id and dc.id not in (215)\n" + 
						"group by rollup( shulby.fenx,(dc.id)) having not grouping(shulby.fenx)=0)shuj,\n" + 
						"(select diancxxb_id,rlbmdjxyjh,rlbmdjbnjh,rzcbyjh,rzcxyjh,rzcbnjh from yuebqtsjb where riq=DATE '"+strDate+"') jh,\n" + 
						"(select diancxxb_id,rlbmdjxyjh from yuebqtsjb where riq=add_months(DATE '"+strDate+"',-1)) benyjh,diancxxb diac\n" + 
						"where shuj.diancxxb_id=jh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=benyjh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=diac.id(+)\n" + 
						"union all\n" + 
						"SELECT rownum,decode(diac.mingc,null,'直管口径2(计划)',diac.mingc),nvl(jh.rlbmdjxyjh,0),nvl(shuj.rulbmdjby,0),nvl(benyjh.rlbmdjxyjh,0) rlbmdjbyjh,\n" + 
						"nvl(shuj.rulbmdjby-benyjh.rlbmdjxyjh,0) bybmdjbjhzj,nvl(shuj.rulbmdjby-shuj.rulbmdjsy,0) bmdjhbzj,nvl(shuj.rulbmdjby-shuj.rulbmdjtq,0) bmdjtbzj,\n" + 
						"nvl(jh.rlbmdjbnjh,0),nvl(shuj.rulbmdjlj,0),nvl(shuj.rulbmdjlj-jh.rlbmdjbnjh,0) ljbmdjbjhzj,nvl(shuj.rulbmdjlj-shuj.rulbmdjtqlj,0) ljbmdjtbzj,\n" + 
						"nvl(jh.rzcxyjh,0),nvl(shuj.rezby-shuj.rulrzby,0) rezcby,nvl(jh.rzcbyjh,0),nvl(shuj.rezby-shuj.rulrzby-jh.rzcbyjh,0) rzcbybjhzj,\n" + 
						"nvl((shuj.rezby-shuj.rulrzby)-(shuj.rezsy-shuj.rulrzsy),0) rzchbzj,nvl((shuj.rezby-shuj.rulrzby)-(shuj.reztq-shuj.rulrztq),0) rzctbzj,\n" + 
						"nvl(jh.rzcbnjh,0),nvl(shuj.rezlj-shuj.rulrzlj,0) rezclj,nvl(shuj.rezlj-shuj.rulrzlj-jh.rzcbnjh,0) rzcbjhzj,\n" + 
						"nvl((shuj.rezlj-shuj.rulrzlj)-(shuj.reztqlj-shuj.rulrztqlj),0) rzctbzj\n" + 
						"from(select dc.id diancxxb_id,shulby.fenx,\n" + 
						"round_new(decode(sum(shulby.jincml),0,0,sum(shulby.rez * shulby.jincml) / sum(shulby.jincml)),2) rezby,\n" + 
						"round_new(decode(sum(zhibbby.fadgrytrml),0,0,sum(zhibbby.rultrmpjfrl*zhibbby.fadgrytrml)/sum(zhibbby.fadgrytrml)),2) rulrzby,\n" + 
						"round_new(decode(sum(zhibbby.rulbml),0,0,sum(zhibbby.bmdj*zhibbby.rulbml)/sum(zhibbby.rulbml)),2)rulbmdjby,\n" + 
						"round_new(decode(sum(shulsy.jincml),0,0,sum(shulsy.rez * shulsy.jincml) / sum(shulsy.jincml)),2) rezsy,\n" + 
						"round_new(decode(sum(zhibbsy.fadgrytrml),0,0,sum(zhibbsy.rultrmpjfrl*zhibbsy.fadgrytrml)/sum(zhibbsy.fadgrytrml)),2) rulrzsy,\n" + 
						"round_new(decode(sum(zhibbsy.rulbml),0,0,sum(zhibbsy.bmdj*zhibbsy.rulbml)/sum(zhibbsy.rulbml)),2)rulbmdjsy,\n" + 
						"round_new(decode(sum(shultq.jincml),0,0,sum(shultq.rez * shultq.jincml) / sum(shultq.jincml)),2) reztq,\n" + 
						"round_new(decode(sum(zhibbtq.fadgrytrml),0,0,sum(zhibbtq.rultrmpjfrl*zhibbtq.fadgrytrml)/sum(zhibbtq.fadgrytrml)),2) rulrztq,\n" + 
						"round_new(decode(sum(zhibbtq.rulbml),0,0,sum(zhibbtq.bmdj*zhibbtq.rulbml)/sum(zhibbtq.rulbml)),2)rulbmdjtq,\n" + 
						"round_new(decode(sum(shullj.jincml),0,0,sum(shullj.rez * shullj.jincml) / sum(shullj.jincml)),2) rezlj,\n" + 
						"round_new(decode(sum(zhibblj.fadgrytrml),0,0,sum(zhibblj.rultrmpjfrl*zhibblj.fadgrytrml)/sum(zhibblj.fadgrytrml)),2) rulrzlj,\n" + 
						"round_new(decode(sum(zhibblj.rulbml),0,0,sum(zhibblj.bmdj*zhibblj.rulbml)/sum(zhibblj.rulbml)),2)rulbmdjlj,\n" + 
						"round_new(decode(sum(shultqlj.jincml),0,0,sum(shultqlj.rez * shultqlj.jincml) / sum(shultqlj.jincml)),2) reztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.fadgrytrml),0,0,sum(zhibbtqlj.rultrmpjfrl*zhibbtqlj.fadgrytrml)/sum(zhibbtqlj.fadgrytrml)),2) rulrztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.rulbml),0,0,sum(zhibbtqlj.bmdj*zhibbtqlj.rulbml)/sum(zhibbtqlj.rulbml)),2)rulbmdjtqlj\n" + 
						"from\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
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
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulby,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = DATE '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbby,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-1))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulsy,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-1)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbsy,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ =add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultq,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtq,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ =DATE '"+strDate+"')kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shullj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = DATE '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibblj,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ =add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultqlj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtqlj,\n" + 
						"    diancxxb dc\n" + 
						"where shulby.diancxxb_id=zhibbby.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shulsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shullj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibblj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=dc.id and dc.id not in (215,391)\n" + 
						"group by rollup( shulby.fenx,(dc.id)) having not grouping(shulby.fenx)=0)shuj,\n" + 
						"(select diancxxb_id,rlbmdjxyjh,rlbmdjbnjh,rzcbyjh,rzcxyjh,rzcbnjh from yuebqtsjb where riq=DATE '"+strDate+"') jh,\n" + 
						"(select diancxxb_id,rlbmdjxyjh from yuebqtsjb where riq=add_months(DATE '"+strDate+"',-1)) benyjh,diancxxb diac\n" + 
						"where shuj.diancxxb_id=jh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=benyjh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=diac.id(+)\n" + 
						"union all\n" + 
						"SELECT rownum,diac.mingc,nvl(jh.rlbmdjxyjh,0),nvl(shuj.rulbmdjby,0),nvl(benyjh.rlbmdjxyjh,0) rlbmdjbyjh,\n" + 
						"nvl(shuj.rulbmdjby-benyjh.rlbmdjxyjh,0) bybmdjbjhzj,nvl(shuj.rulbmdjby-shuj.rulbmdjsy,0) bmdjhbzj,nvl(shuj.rulbmdjby-shuj.rulbmdjtq,0) bmdjtbzj,\n" + 
						"nvl(jh.rlbmdjbnjh,0),nvl(shuj.rulbmdjlj,0),nvl(shuj.rulbmdjlj-jh.rlbmdjbnjh,0) ljbmdjbjhzj,nvl(shuj.rulbmdjlj-shuj.rulbmdjtqlj,0) ljbmdjtbzj,\n" + 
						"nvl(jh.rzcxyjh,0),nvl(shuj.rezby-shuj.rulrzby,0) rezcby,nvl(jh.rzcbyjh,0),nvl(shuj.rezby-shuj.rulrzby-jh.rzcbyjh,0) rzcbybjhzj,\n" + 
						"nvl((shuj.rezby-shuj.rulrzby)-(shuj.rezsy-shuj.rulrzsy),0) rzchbzj,nvl((shuj.rezby-shuj.rulrzby)-(shuj.reztq-shuj.rulrztq),0) rzctbzj,\n" + 
						"nvl(jh.rzcbnjh,0),nvl(shuj.rezlj-shuj.rulrzlj,0) rezclj,nvl(shuj.rezlj-shuj.rulrzlj-jh.rzcbnjh,0) rzcbjhzj,\n" + 
						"nvl((shuj.rezlj-shuj.rulrzlj)-(shuj.reztqlj-shuj.rulrztqlj),0) rzctbzj\n" + 
						"from(select dc.id diancxxb_id,shulby.fenx,\n" + 
						"round_new(decode(sum(shulby.jincml),0,0,sum(shulby.rez * shulby.jincml) / sum(shulby.jincml)),2) rezby,\n" + 
						"round_new(decode(sum(zhibbby.fadgrytrml),0,0,sum(zhibbby.rultrmpjfrl*zhibbby.fadgrytrml)/sum(zhibbby.fadgrytrml)),2) rulrzby,\n" + 
						"round_new(decode(sum(zhibbby.rulbml),0,0,sum(zhibbby.bmdj*zhibbby.rulbml)/sum(zhibbby.rulbml)),2)rulbmdjby,\n" + 
						"round_new(decode(sum(shulsy.jincml),0,0,sum(shulsy.rez * shulsy.jincml) / sum(shulsy.jincml)),2) rezsy,\n" + 
						"round_new(decode(sum(zhibbsy.fadgrytrml),0,0,sum(zhibbsy.rultrmpjfrl*zhibbsy.fadgrytrml)/sum(zhibbsy.fadgrytrml)),2) rulrzsy,\n" + 
						"round_new(decode(sum(zhibbsy.rulbml),0,0,sum(zhibbsy.bmdj*zhibbsy.rulbml)/sum(zhibbsy.rulbml)),2)rulbmdjsy,\n" + 
						"round_new(decode(sum(shultq.jincml),0,0,sum(shultq.rez * shultq.jincml) / sum(shultq.jincml)),2) reztq,\n" + 
						"round_new(decode(sum(zhibbtq.fadgrytrml),0,0,sum(zhibbtq.rultrmpjfrl*zhibbtq.fadgrytrml)/sum(zhibbtq.fadgrytrml)),2) rulrztq,\n" + 
						"round_new(decode(sum(zhibbtq.rulbml),0,0,sum(zhibbtq.bmdj*zhibbtq.rulbml)/sum(zhibbtq.rulbml)),2)rulbmdjtq,\n" + 
						"round_new(decode(sum(shullj.jincml),0,0,sum(shullj.rez * shullj.jincml) / sum(shullj.jincml)),2) rezlj,\n" + 
						"round_new(decode(sum(zhibblj.fadgrytrml),0,0,sum(zhibblj.rultrmpjfrl*zhibblj.fadgrytrml)/sum(zhibblj.fadgrytrml)),2) rulrzlj,\n" + 
						"round_new(decode(sum(zhibblj.rulbml),0,0,sum(zhibblj.bmdj*zhibblj.rulbml)/sum(zhibblj.rulbml)),2)rulbmdjlj,\n" + 
						"round_new(decode(sum(shultqlj.jincml),0,0,sum(shultqlj.rez * shultqlj.jincml) / sum(shultqlj.jincml)),2) reztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.fadgrytrml),0,0,sum(zhibbtqlj.rultrmpjfrl*zhibbtqlj.fadgrytrml)/sum(zhibbtqlj.fadgrytrml)),2) rulrztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.rulbml),0,0,sum(zhibbtqlj.bmdj*zhibbtqlj.rulbml)/sum(zhibbtqlj.rulbml)),2)rulbmdjtqlj\n" + 
						"from\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ =DATE '"+strDate+"')kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulby,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = DATE '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbby,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-1))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulsy,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ =add_months(DATE '"+strDate+"',-1)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbsy,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultq,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtq,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
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
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shullj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ =DATE '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibblj,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ =add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultqlj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtqlj,\n" + 
						"    diancxxb dc\n" + 
						"where shulby.diancxxb_id=zhibbby.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shulsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shullj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibblj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=dc.id and dc.id not in (215,476,391)\n" + 
						"group by rollup( shulby.fenx,(dc.id)) having not grouping(dc.id)=1)shuj,\n" + 
						"(select diancxxb_id,rlbmdjxyjh,rlbmdjbnjh,rzcbyjh,rzcxyjh,rzcbnjh from yuebqtsjb where riq=DATE '"+strDate+"') jh,\n" + 
						"(select diancxxb_id,rlbmdjxyjh from yuebqtsjb where riq=add_months(DATE '"+strDate+"',-1)) benyjh,diancxxb diac\n" + 
						"where shuj.diancxxb_id=jh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=benyjh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=diac.id(+)\n" + 
						"union all\n" + 
						"SELECT rownum,decode(diac.mingc,null,'英力特小计',diac.mingc),nvl(jh.rlbmdjxyjh,0),nvl(shuj.rulbmdjby,0),nvl(benyjh.rlbmdjxyjh,0) rlbmdjbyjh,\n" + 
						"nvl(shuj.rulbmdjby-benyjh.rlbmdjxyjh,0) bybmdjbjhzj,nvl(shuj.rulbmdjby-shuj.rulbmdjsy,0) bmdjhbzj,nvl(shuj.rulbmdjby-shuj.rulbmdjtq,0) bmdjtbzj,\n" + 
						"nvl(jh.rlbmdjbnjh,0),nvl(shuj.rulbmdjlj,0),nvl(shuj.rulbmdjlj-jh.rlbmdjbnjh,0) ljbmdjbjhzj,nvl(shuj.rulbmdjlj-shuj.rulbmdjtqlj,0) ljbmdjtbzj,\n" + 
						"nvl(jh.rzcxyjh,0),nvl(shuj.rezby-shuj.rulrzby,0) rezcby,nvl(jh.rzcbyjh,0),nvl(shuj.rezby-shuj.rulrzby-jh.rzcbyjh,0) rzcbybjhzj,\n" + 
						"nvl((shuj.rezby-shuj.rulrzby)-(shuj.rezsy-shuj.rulrzsy),0) rzchbzj,nvl((shuj.rezby-shuj.rulrzby)-(shuj.reztq-shuj.rulrztq),0) rzctbzj,\n" + 
						"nvl(jh.rzcbnjh,0),nvl(shuj.rezlj-shuj.rulrzlj,0) rezclj,nvl(shuj.rezlj-shuj.rulrzlj-jh.rzcbnjh,0) rzcbjhzj,\n" + 
						"nvl((shuj.rezlj-shuj.rulrzlj)-(shuj.reztqlj-shuj.rulrztqlj),0) rzctbzj\n" + 
						"from(select dc.id diancxxb_id,shulby.fenx,\n" + 
						"round_new(decode(sum(shulby.jincml),0,0,sum(shulby.rez * shulby.jincml) / sum(shulby.jincml)),2) rezby,\n" + 
						"round_new(decode(sum(zhibbby.fadgrytrml),0,0,sum(zhibbby.rultrmpjfrl*zhibbby.fadgrytrml)/sum(zhibbby.fadgrytrml)),2) rulrzby,\n" + 
						"round_new(decode(sum(zhibbby.rulbml),0,0,sum(zhibbby.bmdj*zhibbby.rulbml)/sum(zhibbby.rulbml)),2)rulbmdjby,\n" + 
						"round_new(decode(sum(shulsy.jincml),0,0,sum(shulsy.rez * shulsy.jincml) / sum(shulsy.jincml)),2) rezsy,\n" + 
						"round_new(decode(sum(zhibbsy.fadgrytrml),0,0,sum(zhibbsy.rultrmpjfrl*zhibbsy.fadgrytrml)/sum(zhibbsy.fadgrytrml)),2) rulrzsy,\n" + 
						"round_new(decode(sum(zhibbsy.rulbml),0,0,sum(zhibbsy.bmdj*zhibbsy.rulbml)/sum(zhibbsy.rulbml)),2)rulbmdjsy,\n" + 
						"round_new(decode(sum(shultq.jincml),0,0,sum(shultq.rez * shultq.jincml) / sum(shultq.jincml)),2) reztq,\n" + 
						"round_new(decode(sum(zhibbtq.fadgrytrml),0,0,sum(zhibbtq.rultrmpjfrl*zhibbtq.fadgrytrml)/sum(zhibbtq.fadgrytrml)),2) rulrztq,\n" + 
						"round_new(decode(sum(zhibbtq.rulbml),0,0,sum(zhibbtq.bmdj*zhibbtq.rulbml)/sum(zhibbtq.rulbml)),2)rulbmdjtq,\n" + 
						"round_new(decode(sum(shullj.jincml),0,0,sum(shullj.rez * shullj.jincml) / sum(shullj.jincml)),2) rezlj,\n" + 
						"round_new(decode(sum(zhibblj.fadgrytrml),0,0,sum(zhibblj.rultrmpjfrl*zhibblj.fadgrytrml)/sum(zhibblj.fadgrytrml)),2) rulrzlj,\n" + 
						"round_new(decode(sum(zhibblj.rulbml),0,0,sum(zhibblj.bmdj*zhibblj.rulbml)/sum(zhibblj.rulbml)),2)rulbmdjlj,\n" + 
						"round_new(decode(sum(shultqlj.jincml),0,0,sum(shultqlj.rez * shultqlj.jincml) / sum(shultqlj.jincml)),2) reztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.fadgrytrml),0,0,sum(zhibbtqlj.rultrmpjfrl*zhibbtqlj.fadgrytrml)/sum(zhibbtqlj.fadgrytrml)),2) rulrztqlj,\n" + 
						"round_new(decode(sum(zhibbtqlj.rulbml),0,0,sum(zhibbtqlj.bmdj*zhibbtqlj.rulbml)/sum(zhibbtqlj.rulbml)),2)rulbmdjtqlj\n" + 
						"from\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
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
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulby,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = DATE '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbby,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-1))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shulsy,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ =add_months(DATE '"+strDate+"',-1)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbsy,\n" + 
						"    (SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='本月'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultq,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtq,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
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
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shullj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = DATE '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibblj,\n" + 
						"(SELECT KJ.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"               sl.fenx fenx,\n" + 
						"               sum((sl.jingz+SL.YUNS)) jincml,\n" + 
						"               ROUND_NEW(decode(sum(sl.jingz+SL.YUNS),0,0,sum(zl.qnet_ar *(sl.jingz+SL.YUNS))\n" + 
						"               /sum((sl.jingz+SL.YUNS))),2) rez\n" + 
						"          FROM YUESLB SL,YUEZLB ZL,\n" + 
						"               (SELECT ID,\n" + 
						"                       DIANCXXB_ID,\n" + 
						"                       Y.GONGYSB_ID,\n" + 
						"                       Y.JIHKJB_ID,\n" + 
						"                       Y.PINZB_ID\n" + 
						"                  FROM YUETJKJB Y\n" + 
						"                 WHERE RIQ = add_months(DATE '"+strDate+"',-12))kj\n" + 
						"           WHERE KJ.ID = SL.YUETJKJB_ID\n" + 
						"           AND KJ.ID=ZL.YUETJKJB_ID AND SL.FENX = ZL.FENX\n" + 
						" AND (SL.ZHUANGT=1 or SL.ZHUANGT=3) AND (ZL.zhuangt=1 OR Zl.zhuangt=3)\n" + 
						" and sl.fenx='累计'\n" + 
						"            GROUP BY KJ.DIANCXXB_ID, sl.FENX) shultqlj,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t       Z.FENX,\n" + 
						"\t\t      SUM(Z.FADGRYTRML) FADGRYTRML,\n" + 
						"\t\t      ROUND(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.RULTRMPJFRL*Z.FADGRYTRML)/SUM(Z.FADGRYTRML)),6) RULTRMPJFRL,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(DATE '"+strDate+"',-12)\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						" and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) zhibbtqlj,\n" + 
						"    diancxxb dc\n" + 
						"where shulby.diancxxb_id=zhibbby.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shulsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbsy.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtq.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shullj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibblj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=shultqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=zhibbtqlj.diancxxb_id(+)\n" + 
						"and shulby.diancxxb_id=dc.id and dc.id in (476,391)\n" + 
						"group by rollup( shulby.fenx,(dc.id)) having not grouping(shulby.fenx)=1)shuj,\n" + 
						"(select diancxxb_id,rlbmdjxyjh,rlbmdjbnjh,rzcbyjh,rzcxyjh,rzcbnjh from yuebqtsjb where riq=DATE '"+strDate+"') jh,\n" + 
						"(select diancxxb_id,rlbmdjxyjh from yuebqtsjb where riq=add_months(DATE '"+strDate+"',-1)) benyjh,diancxxb diac\n" + 
						"where shuj.diancxxb_id=jh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=benyjh.diancxxb_id(+)\n" + 
						"and shuj.diancxxb_id=diac.id(+)";

		ResultSetList rs=cn.getResultSetList(sql.toString());
		Report rt=new Report();
		 String ArrHeader[][]=new String[3][22];
		 ArrHeader[0]=new String[] {"序号","单位","入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)",
				 "入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)","入炉综合标煤单价(元/吨)","入厂、入炉煤热值差（MJ/kg）","入厂、入炉煤热值差（MJ/kg）",
				 "入厂、入炉煤热值差（MJ/kg）","入厂、入炉煤热值差（MJ/kg）","入厂、入炉煤热值差（MJ/kg）","入厂、入炉煤热值差（MJ/kg）","入厂、入炉煤热值差（MJ/kg）","入厂、入炉煤热值差（MJ/kg）",
				 "入厂、入炉煤热值差（MJ/kg）","入厂、入炉煤热值差（MJ/kg）"};
		 ArrHeader[1]=new String[] {"序号","单位","月度计划完成情况","月度计划完成情况","月度计划完成情况","月度计划完成情况","月度计划完成情况","月度计划完成情况","年度计划完成情况","年度计划完成情况",
				 "年度计划完成情况","年度计划完成情况","月度计划完成情况","月度计划完成情况","月度计划完成情况","月度计划完成情况","月度计划完成情况","月度计划完成情况","年度计划完成情况","年度计划完成情况",
				 "年度计划完成情况","年度计划完成情况"};
		 ArrHeader[2]=new String[] {"序号","单位","下月<br>计划","本月<br>完成","本月<br>计划","比计划 <br>增减","环比<br>增减","同比<br>增减","本年<br>计划","累计<br>完成",
				 "比计划 <br>增减","同比<br>增减","下月<br>计划","本月<br>完成","本月<br>计划","比计划<br>增减","环比<br>增减","同比<br>增减","本年<br>计划","累计<br>完成","比计划<br>增减","同比<br>增减"};

		 int ArrWidth[]=new int[] {40,120,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60};


		 rt.setTitle(strMonth+"标煤价、热值差汇总", ArrWidth);
		 rt.setDefaultTitle(1,3,"单位："+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(19, 3, "单位：元/吨", Table.ALIGN_RIGHT);
		//设置页面
		
//		数据
		rt.setBody(new Table(rs,3,0,0));
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