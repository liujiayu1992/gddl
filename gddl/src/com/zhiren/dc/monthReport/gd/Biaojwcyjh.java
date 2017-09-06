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

public class Biaojwcyjh extends BasePage {

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
		return getToolbar().getRenderScript() + getOtherScript("diancTree");
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

	public String getTianzdwQuanc() {
		String[] str = getTreeid().split(",");
		if (str.length > 1) {
			return "组合电厂";
		} else {
			return getTianzdwQuanc(Long.parseLong(str[0]));
		}
	}

	public long getDiancxxbId() {
		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	// 得到单位全称
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

	// 增加电厂多选树的级联
	public String getOtherScript(String treeid) {
		String str = " var "
				+ treeid
				+ "_history=\"\";\n"
				+ treeid
				+ "_treePanel.on(\"checkchange\",function(node,checked){\n"
				+ "    if(checked){\n"
				+ "      addNode(node);\n"
				+ "    }else{\n"
				+ "      subNode(node);\n"
				+ "    }\n"
				+ "    node.expand();\n"
				+ "    node.attributes.checked = checked;\n"
				+ "    node.eachChild(function(child) {\n"
				+ "      if(child.attributes.checked != checked){\n"
				+ "        if(checked){\n"
				+ "          addNode(child);\n"
				+ "        }else{\n"
				+ "          subNode(child);\n"
				+ "        }\n"
				+ "        child.ui.toggleCheck(checked);\n"
				+ "              child.attributes.checked = checked;\n"
				+ "              child.fireEvent('checkchange', child, checked);\n"
				+ "      }\n" + "    });\n" + "  }," + treeid
				+ "_treePanel);\n" + "  function addNode(node){\n"
				+ "    var history = '+,'+node.id+\";\";\n"
				+ "    writesrcipt(node,history);\n" + "  }\n" + "\n"
				+ "  function subNode(node){\n"
				+ "    var history = '-,'+node.id+\";\";\n"
				+ "    writesrcipt(node,history);\n" + "  }\n"
				+ "function writesrcipt(node,history){\n" + "\t\tif(" + treeid
				+ "_history==\"\"){\n" + "\t\t\t" + treeid
				+ "_history = history;\n" + "\t\t}else{\n" + "\t\t\tvar his = "
				+ treeid + "_history.split(\";\");\n"
				+ "\t\t\tvar reset = false;\n"
				+ "\t\t\tfor(i=0;i<his.length;i++){\n"
				+ "\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n"
				+ "\t\t\t\t\this[i] = \"\";\n" + "\t\t\t\t\treset = true;\n"
				+ "\t\t\t\t\tbreak;\n" + "\t\t\t\t}\n" + "\t\t\t}\n"
				+ "\t\tif(reset){\n" + "\t\t\t  " + treeid
				+ "_history = his.join(\";\");\n" + "    }else{\n" + "      	 "
				+ treeid + "_history += history;\n" + "    }\n" + "  }\n"
				+ "\n" + "}";
		return str;
	}

	// 分厂别
	public boolean isFencb() {
		return ((Visit) this.getPage().getVisit()).isFencb();
	}

	// 初始化多选电厂树中的默认值
	private void initDiancTree() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "SELECT ID\n" + "  FROM DIANCXXB\n" + " WHERE JIB > 2\n"
				+ " START WITH ID = " + visit.getDiancxxb_id() + "\n"
				+ "CONNECT BY FUID = PRIOR ID";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		String TreeID = "";
		while (rsl.next()) {
			TreeID += rsl.getString("ID") + ",";
		}
		if (TreeID.length() > 1) {
			TreeID = TreeID.substring(0, TreeID.length() - 1);
			setTreeid(TreeID);
		} else {
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
						.getVisit()).getDiancxxb_id(), getTreeid(), null, true);
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str = getTreeid().split(",");
		if (str.length > 1) {
			tf.setValue("组合电厂");
		} else {
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
					.parseLong(str[0])));
		}
		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		//
		// tb1.addText(new ToolbarText("单位:"));
		// tb1.addField(tf);
		// tb1.addItem(tb2);
		// tb1.addText(new ToolbarText("-"));

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
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + " 年 "
				+ getYuefValue().getValue() + " 月";
		String sql = 
				"select rownum,decode(q.diancxxb_id,1,'小计',2,'英力特小计',3,'直管口径1(燃料)',d.mingc),q.rulbmdj1,q.rulbmdj2,q.rulbmdj3,q.rulbmdj4,q.rulbmdj5,q.rulbmdj6,\n" +
						"q.rulbmdj7,q.rulbmdj8,q.rulbmdj9,q.rulbmdj10,q.rulbmdj11,q.rulbmdj12,q.rulbmdjlj,q.rlbmdjbyjh,q.rlbmdjjcsbxyjh,q.rlbmdjbnjh\n" + 
						" from (SELECT rownum,decode(diancxxb_id,null,'1',diancxxb_id)diancxxb_id,rulbmdj1,rulbmdj2,rulbmdj3,rulbmdj4,rulbmdj5,rulbmdj6,\n" +
						"rulbmdj7,rulbmdj8,rulbmdj9,rulbmdj10,rulbmdj11,rulbmdj12,rulbmdjlj,rlbmdjbyjh,rlbmdjjcsbxyjh,rlbmdjbnjh\n" + 
						"from(select yi.diancxxb_id,yi.fenx,\n" + 
						"round_new(decode(sum(yi.rulbml),0,0,sum(yi.bmdj*yi.rulbml)/sum(yi.rulbml)),2)rulbmdj1,\n" + 
						"round_new(decode(sum(er.rulbml),0,0,sum(er.bmdj*er.rulbml)/sum(er.rulbml)),2)rulbmdj2,\n" + 
						"round_new(decode(sum(sa.rulbml),0,0,sum(sa.bmdj*sa.rulbml)/sum(sa.rulbml)),2)rulbmdj3,\n" + 
						"round_new(decode(sum(si.rulbml),0,0,sum(si.bmdj*si.rulbml)/sum(si.rulbml)),2)rulbmdj4,\n" + 
						"round_new(decode(sum(wu.rulbml),0,0,sum(wu.bmdj*wu.rulbml)/sum(wu.rulbml)),2)rulbmdj5,\n" + 
						"round_new(decode(sum(li.rulbml),0,0,sum(li.bmdj*li.rulbml)/sum(li.rulbml)),2)rulbmdj6,\n" + 
						"round_new(decode(sum(qi.rulbml),0,0,sum(qi.bmdj*qi.rulbml)/sum(qi.rulbml)),2)rulbmdj7,\n" + 
						"round_new(decode(sum(ba.rulbml),0,0,sum(ba.bmdj*ba.rulbml)/sum(ba.rulbml)),2)rulbmdj8,\n" + 
						"round_new(decode(sum(ju.rulbml),0,0,sum(ju.bmdj*ju.rulbml)/sum(ju.rulbml)),2)rulbmdj9,\n" + 
						"round_new(decode(sum(sh.rulbml),0,0,sum(sh.bmdj*sh.rulbml)/sum(sh.rulbml)),2)rulbmdj10,\n" + 
						"round_new(decode(sum(sy.rulbml),0,0,sum(sy.bmdj*sy.rulbml)/sum(sy.rulbml)),2)rulbmdj11,\n" + 
						"round_new(decode(sum(se.rulbml),0,0,sum(se.bmdj*se.rulbml)/sum(se.rulbml)),2)rulbmdj12,\n" + 
						"round_new(decode(sum(lj.rulbml),0,0,sum(lj.bmdj*lj.rulbml)/sum(lj.rulbml)),2)rulbmdjlj,\n" + 
						"sum(nvl(y.rlbmdjbyjh,0))rlbmdjbyjh,sum(nvl(y.rlbmdjjcsbxyjh,0))rlbmdjjcsbxyjh,\n" + 
						"sum(nvl(n.rlbmdjbnjh,0))rlbmdjbnjh\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) yi,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) er,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sa,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) si,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) wu,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) li,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) qi,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) ba,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) ju,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sh,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sy,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) se,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ >= date '"+getNianfValue().getValue()+"-01-01' and Z.RIQ <= date '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) lj,\n" + 
						"    (select diancxxb_id,rlbmdjbyjh,rlbmdjjcsbxyjh\n" + 
						"     from yuebqtsjb where riq=date'"+strDate+"') y,\n" + 
						"    (select diancxxb_id,max(rlbmdjbnjh)rlbmdjbnjh\n" + 
						"     from yuebqtsjb where riq>=date'"+getNianfValue().getValue()+"-01-01' and riq<=date'"+getNianfValue().getValue()+"-12-01' group by diancxxb_id) n\n" + 
						"    where yi.diancxxb_id=er.diancxxb_id and yi.diancxxb_id=sa.diancxxb_id\n" + 
						"    and yi.diancxxb_id=si.diancxxb_id and yi.diancxxb_id=wu.diancxxb_id\n" + 
						"    and yi.diancxxb_id=li.diancxxb_id and yi.diancxxb_id=qi.diancxxb_id\n" + 
						"    and yi.diancxxb_id=ba.diancxxb_id and yi.diancxxb_id=ju.diancxxb_id\n" + 
						"    and yi.diancxxb_id=sh.diancxxb_id and yi.diancxxb_id=sy.diancxxb_id\n" + 
						"    and yi.diancxxb_id=se.diancxxb_id and yi.diancxxb_id=y.diancxxb_id(+)\n" + 
						"    and yi.diancxxb_id=n.diancxxb_id(+) and yi.diancxxb_id not in(391,476)\n" + 
						"    and yi.diancxxb_id=lj.diancxxb_id\n" + 
						"group by rollup((yi.diancxxb_id,yi.fenx)))\n" + 
						"union all\n" + 
						"SELECT rownum,decode(diancxxb_id,null,'2',diancxxb_id)diancxxb_id,rulbmdj1,rulbmdj2,rulbmdj3,rulbmdj4,rulbmdj5,rulbmdj6,\n" + 
						"rulbmdj7,rulbmdj8,rulbmdj9,rulbmdj10,rulbmdj11,rulbmdj12,rulbmdjlj,rlbmdjbyjh,rlbmdjjcsbxyjh,rlbmdjbnjh\n" + 
						"from(select yi.diancxxb_id,yi.fenx,\n" + 
						"round_new(decode(sum(yi.rulbml),0,0,sum(yi.bmdj*yi.rulbml)/sum(yi.rulbml)),2)rulbmdj1,\n" + 
						"round_new(decode(sum(er.rulbml),0,0,sum(er.bmdj*er.rulbml)/sum(er.rulbml)),2)rulbmdj2,\n" + 
						"round_new(decode(sum(sa.rulbml),0,0,sum(sa.bmdj*sa.rulbml)/sum(sa.rulbml)),2)rulbmdj3,\n" + 
						"round_new(decode(sum(si.rulbml),0,0,sum(si.bmdj*si.rulbml)/sum(si.rulbml)),2)rulbmdj4,\n" + 
						"round_new(decode(sum(wu.rulbml),0,0,sum(wu.bmdj*wu.rulbml)/sum(wu.rulbml)),2)rulbmdj5,\n" + 
						"round_new(decode(sum(li.rulbml),0,0,sum(li.bmdj*li.rulbml)/sum(li.rulbml)),2)rulbmdj6,\n" + 
						"round_new(decode(sum(qi.rulbml),0,0,sum(qi.bmdj*qi.rulbml)/sum(qi.rulbml)),2)rulbmdj7,\n" + 
						"round_new(decode(sum(ba.rulbml),0,0,sum(ba.bmdj*ba.rulbml)/sum(ba.rulbml)),2)rulbmdj8,\n" + 
						"round_new(decode(sum(ju.rulbml),0,0,sum(ju.bmdj*ju.rulbml)/sum(ju.rulbml)),2)rulbmdj9,\n" + 
						"round_new(decode(sum(sh.rulbml),0,0,sum(sh.bmdj*sh.rulbml)/sum(sh.rulbml)),2)rulbmdj10,\n" + 
						"round_new(decode(sum(sy.rulbml),0,0,sum(sy.bmdj*sy.rulbml)/sum(sy.rulbml)),2)rulbmdj11,\n" + 
						"round_new(decode(sum(se.rulbml),0,0,sum(se.bmdj*se.rulbml)/sum(se.rulbml)),2)rulbmdj12,\n" + 
						"round_new(decode(sum(lj.rulbml),0,0,sum(lj.bmdj*lj.rulbml)/sum(lj.rulbml)),2)rulbmdjlj,\n" + 
						"sum(nvl(y.rlbmdjbyjh,0))rlbmdjbyjh,sum(nvl(y.rlbmdjjcsbxyjh,0))rlbmdjjcsbxyjh,\n" + 
						"sum(nvl(n.rlbmdjbnjh,0))rlbmdjbnjh\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) yi,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) er,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sa,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) si,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) wu,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) li,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) qi,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) ba,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) ju,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sh,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sy,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) se,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ >= date '"+getNianfValue().getValue()+"-01-01' and Z.RIQ <= date '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) lj,\n" + 
						"    (select diancxxb_id,rlbmdjbyjh,rlbmdjjcsbxyjh\n" + 
						"     from yuebqtsjb where riq=date'"+strDate+"') y,\n" + 
						"    (select diancxxb_id,max(rlbmdjbnjh)rlbmdjbnjh\n" + 
						"     from yuebqtsjb where riq>=date'"+getNianfValue().getValue()+"-01-01' and riq<=date'"+getNianfValue().getValue()+"-12-01' group by diancxxb_id) n\n" + 
						"    where yi.diancxxb_id=er.diancxxb_id and yi.diancxxb_id=sa.diancxxb_id\n" + 
						"    and yi.diancxxb_id=si.diancxxb_id and yi.diancxxb_id=wu.diancxxb_id\n" + 
						"    and yi.diancxxb_id=li.diancxxb_id and yi.diancxxb_id=qi.diancxxb_id\n" + 
						"    and yi.diancxxb_id=ba.diancxxb_id and yi.diancxxb_id=ju.diancxxb_id\n" + 
						"    and yi.diancxxb_id=sh.diancxxb_id and yi.diancxxb_id=sy.diancxxb_id\n" + 
						"    and yi.diancxxb_id=se.diancxxb_id and yi.diancxxb_id=y.diancxxb_id(+)\n" + 
						"    and yi.diancxxb_id=n.diancxxb_id(+) and yi.diancxxb_id in(391,476)\n" + 
						"    and yi.diancxxb_id=lj.diancxxb_id\n" + 
						"group by rollup((yi.diancxxb_id,yi.fenx)))\n" + 
						"union all\n" + 
						"SELECT rownum,decode(diancxxb_id,null,'3',diancxxb_id)diancxxb_id,rulbmdj1,rulbmdj2,rulbmdj3,rulbmdj4,rulbmdj5,rulbmdj6,\n" + 
						"rulbmdj7,rulbmdj8,rulbmdj9,rulbmdj10,rulbmdj11,rulbmdj12,rulbmdjlj,rlbmdjbyjh,rlbmdjjcsbxyjh,rlbmdjbnjh\n" + 
						"from(select yi.diancxxb_id,yi.fenx,\n" + 
						"round_new(decode(sum(yi.rulbml),0,0,sum(yi.bmdj*yi.rulbml)/sum(yi.rulbml)),2)rulbmdj1,\n" + 
						"round_new(decode(sum(er.rulbml),0,0,sum(er.bmdj*er.rulbml)/sum(er.rulbml)),2)rulbmdj2,\n" + 
						"round_new(decode(sum(sa.rulbml),0,0,sum(sa.bmdj*sa.rulbml)/sum(sa.rulbml)),2)rulbmdj3,\n" + 
						"round_new(decode(sum(si.rulbml),0,0,sum(si.bmdj*si.rulbml)/sum(si.rulbml)),2)rulbmdj4,\n" + 
						"round_new(decode(sum(wu.rulbml),0,0,sum(wu.bmdj*wu.rulbml)/sum(wu.rulbml)),2)rulbmdj5,\n" + 
						"round_new(decode(sum(li.rulbml),0,0,sum(li.bmdj*li.rulbml)/sum(li.rulbml)),2)rulbmdj6,\n" + 
						"round_new(decode(sum(qi.rulbml),0,0,sum(qi.bmdj*qi.rulbml)/sum(qi.rulbml)),2)rulbmdj7,\n" + 
						"round_new(decode(sum(ba.rulbml),0,0,sum(ba.bmdj*ba.rulbml)/sum(ba.rulbml)),2)rulbmdj8,\n" + 
						"round_new(decode(sum(ju.rulbml),0,0,sum(ju.bmdj*ju.rulbml)/sum(ju.rulbml)),2)rulbmdj9,\n" + 
						"round_new(decode(sum(sh.rulbml),0,0,sum(sh.bmdj*sh.rulbml)/sum(sh.rulbml)),2)rulbmdj10,\n" + 
						"round_new(decode(sum(sy.rulbml),0,0,sum(sy.bmdj*sy.rulbml)/sum(sy.rulbml)),2)rulbmdj11,\n" + 
						"round_new(decode(sum(se.rulbml),0,0,sum(se.bmdj*se.rulbml)/sum(se.rulbml)),2)rulbmdj12,\n" + 
						"round_new(decode(sum(lj.rulbml),0,0,sum(lj.bmdj*lj.rulbml)/sum(lj.rulbml)),2)rulbmdjlj,\n" + 
						"sum(nvl(y.rlbmdjbyjh,0))rlbmdjbyjh,sum(nvl(y.rlbmdjjcsbxyjh,0))rlbmdjjcsbxyjh,\n" + 
						"sum(nvl(n.rlbmdjbnjh,0))rlbmdjbnjh\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-1-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) yi,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-2-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) er,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-3-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sa,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-4-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) si,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-5-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) wu,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-6-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) li,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-7-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) qi,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-8-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) ba,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-9-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) ju,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-10-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sh,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-11-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) sy,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = date '"+getNianfValue().getValue()+"-12-01'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) se,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))\n" + 
						"          /SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ >= date '"+getNianfValue().getValue()+"-01-01' and Z.RIQ <= date '"+strDate+"'\n" + 
						" AND (z.zhuangt=1 OR z.zhuangt=3) and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) lj,\n" + 
						"    (select diancxxb_id,rlbmdjbyjh,rlbmdjjcsbxyjh\n" + 
						"     from yuebqtsjb where riq=date'"+strDate+"') y,\n" + 
						"    (select diancxxb_id,max(rlbmdjbnjh)rlbmdjbnjh\n" + 
						"     from yuebqtsjb where riq>=date'"+getNianfValue().getValue()+"-01-01' and riq<=date'"+getNianfValue().getValue()+"-12-01' group by diancxxb_id) n\n" + 
						"    where yi.diancxxb_id=er.diancxxb_id and yi.diancxxb_id=sa.diancxxb_id\n" + 
						"    and yi.diancxxb_id=si.diancxxb_id and yi.diancxxb_id=wu.diancxxb_id\n" + 
						"    and yi.diancxxb_id=li.diancxxb_id and yi.diancxxb_id=qi.diancxxb_id\n" + 
						"    and yi.diancxxb_id=ba.diancxxb_id and yi.diancxxb_id=ju.diancxxb_id\n" + 
						"    and yi.diancxxb_id=sh.diancxxb_id and yi.diancxxb_id=sy.diancxxb_id\n" + 
						"    and yi.diancxxb_id=se.diancxxb_id and yi.diancxxb_id=y.diancxxb_id(+)\n" + 
						"    and yi.diancxxb_id=n.diancxxb_id(+) and yi.diancxxb_id not in(215)\n" + 
						"    and yi.diancxxb_id=lj.diancxxb_id\n" + 
						"group by rollup((yi.diancxxb_id,yi.fenx))\n" + 
						"having not grouping(yi.diancxxb_id)=0))q,diancxxb d\n"+
						"where d.id(+)=q.diancxxb_id";

		ResultSetList rs = cn.getResultSetList(sql);
//		System.out.print(sql);
		Report rt = new Report();
		// 定义表头数据
		String ArrHeader[][] = new String[3][18];
		ArrHeader[0] = new String[] { "序号", "单位", "", "", "", "", "", "", "",
				"", "", "", "", "", "", strDate+"", strDate+"", getNianfValue().getValue()+"年度计划" };
		ArrHeader[1] = new String[] { "序号", "单位", "1月", "2月", "3月", "4月", "5月",
				"6月", "7月", "8月", "9月", "10月", "11月", "12月", "累计", "公司下达计划",
				"基层上报", getNianfValue().getValue()+"年度计划" };
		ArrHeader[2] = new String[] { "序号", "单位", "1月", "2月", "3月", "4月", "5月",
				"6月", "7月", "8月", "9月", "10月", "11月", "12月", "累计", "公司下达计划",
				"基层上报", getNianfValue().getValue()+"年度计划" };

		int ArrWidth[] = new int[] { 40, 120, 60, 60, 60, 60, 60, 60, 60, 60,
				60, 60, 60, 60, 60, 80, 80, 80 };

		rt.setTitle(strMonth + "标煤单价完成情况对比", ArrWidth);
		rt.setDefaultTitle(1, 3, "单位：" + this.getTianzdwQuanc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(16, 3, "单位：元/吨", Table.ALIGN_RIGHT);
		// 设置页面

		// 数据
		rt.setBody(new Table(rs, 3, 0, 0));
		rt.body.setWidth(ArrWidth);

		// rt.body.setPageRows(99);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeCol(1);
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);

		rt.getPages();
		rt.body.ShowZero = true;// reportShowZero();
		_CurrentPage = 1;
		_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}

	private void init() {
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