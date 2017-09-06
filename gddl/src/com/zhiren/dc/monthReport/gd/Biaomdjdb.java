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

public class Biaomdjdb extends BasePage {
	
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
				"SELECT ROWNUM-1 xh,MINGC,DYRC,DYRL,DYCZ,LJRC,LJRL,LJCZ,\n" +
						"ZAF,RANY,RZC,LJCZ-ZAF-RANY-RZC HSFF,KCDJ\n" + 
						"FROM\n" + 
						"(SELECT --dc.id,dc.mingc,rc.*,rl.*\n" + 
						" DC.MINGC,\n" + 
						" RC.BYBD DYRC,\n" + 
						" RL.BYBMDJ DYRL,\n" + 
						" RL.BYBMDJ - RC.BYBD DYCZ,\n" + 
						" RC.LJBD LJRC,\n" + 
						" RL.LJBMDJ LJRL,\n" + 
						" RL.LJBMDJ - RC.LJBD LJCZ,\n" + 
						" DECODE(RL.LJBML, 0, 0, ROUND(ZF.JINE / RL.LJBML, 2)) ZAF,\n" + 
						" DECODE(RL.LJBML, 0, 0, ROUND(RL.LJTRYL / RL.LJBML, 2)) RANY,\n" + 
						" DECODE(RC.LJRZ,0,0,ROUND((RC.LJRZ - RL.LJFRL) *(RL.LJBMDJ -DECODE(RL.LJBML, 0, 0, ROUND(ZF.JINE / RL.LJBML, 2)) -DECODE(RL.LJBML, 0, 0, ROUND(RL.LJTRYL / RL.LJBML, 2))) /RC.LJRZ,2)) RZC,\n" + 
						" DECODE(RL.BYKCRZ, 0, 0, ROUND(RL.BYKCDJ * 29.271 / RL.BYKCRZ, 2)) KCDJ\n" + 
						" FROM (SELECT SR.DC_ID,\n" + 
						"       max( decode(sr.fenx,'本月',SR.JIESL))bysl,\n" + 
						"       max( decode(sr.fenx,'累计',SR.JIESL))ljsl,\n" + 
						"       max( decode(sr.fenx,'本月',SR.QNET_AR))byrz,\n" + 
						"       max( decode(sr.fenx,'累计',SR.QNET_AR))ljrz,\n" + 
						"       max( decode(sr.fenx,'本月',ROUND(decode(SR.QNET_AR,0,0,(SR.MEIJ/1.17 + SR.YUNJ-SR.YUNJS + SR.ZAF-SR.ZAFS) * 29.271 / SR.QNET_AR),2)))bybd,\n" + 
						"       max( decode(sr.fenx,'累计',ROUND(decode(SR.QNET_AR,0,0,(SR.MEIJ/1.17 + SR.YUNJ-SR.YUNJS + SR.ZAF-SR.ZAFS) * 29.271 / SR.QNET_AR),2)))ljbd\n" + 
						"  FROM (SELECT DECODE(D.ID,NULL,-1,D.ID)DC_ID,\n" + 
						"               DECODE(D.XUH,NULL,0,D.XUH) XUH,\n" + 
						"               Y.FENX,\n" + 
						"               ROUND (SUM(JIESL),0) JIESL,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM((Y.MEIJ) * JIESL) / SUM(JIESL)),2) MEIJ,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJ * JIESL) / SUM(JIESL)),2) YUNJ,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM((Y.ZAF+Y.DAOZZF+Y.QIT+Y.KUANGQYF) * JIESL) / SUM(JIESL)),2) ZAF,\n" + 
						"               ROUND_NEW(DECODE(SUM(JIESL), 0, 0, SUM(Y.QNET_AR * JIESL) / SUM(JIESL)),2) QNET_AR,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM(Y.ZAFS * JIESL) / SUM(JIESL)),2) ZAFS,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM(Y.YUNJS * JIESL) / SUM(JIESL)),2) YUNJS,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM(Y.MEIJS * JIESL) / SUM(JIESL)),2) MEIJS,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM(Y.QIT * JIESL) / SUM(JIESL)),2) QIT,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM(Y.DAOZZF * JIESL) / SUM(JIESL)),2) DAOZZF,\n" + 
						"               round(DECODE(SUM(JIESL), 0, 0, SUM(Y.KUANGQYF * JIESL) / SUM(JIESL)),2) KUANGQYF\n" + 
						"          FROM YUEJSBMDJ Y,JIHKJB,DIANCXXB D,\n" + 
						"          (SELECT T.ID,\n" + 
						"               T.GONGYSB_ID,\n" + 
						"               V.SMC,\n" + 
						"               V.DQMC,\n" + 
						"               G.MINGC,\n" + 
						"               DECODE(T.JIHKJB_ID, 1, '重点订货', 3, '重点订货', J.MINGC) TJKJ,\n" + 
						"               T.JIHKJB_ID,\n" + 
						"               T.RIQ,\n" + 
						"               T.DIANCXXB_ID\n" + 
						"          FROM YUETJKJB T, GONGYSB G, JIHKJB J, (SELECT DISTINCT ID,DQMC,SMC FROM VWGONGYSDQ) V\n" + 
						"         WHERE T.GONGYSB_ID = G.ID\n" + 
						"           AND V.ID = G.ID\n" + 
						"           AND T.JIHKJB_ID = J.ID\n" + 
						"           AND T.DIANCXXB_ID NOT IN (215, 300, 100, 112)\n" + 
						"           AND T.RIQ = TO_DATE('"+strDate+"', 'yyyy-mm-dd')) T\n" + 
						"WHERE Y.YUETJKJB_ID = T.ID\n" + 
						"   AND (Y.ZHUANGT=1 OR Y.ZHUANGT=3)\n" + 
						"   AND T.JIHKJB_ID = JIHKJB.ID\n" + 
						"   AND D.ID=T.DIANCXXB_ID(+)\n" + 
						" GROUP BY ROLLUP(Y.FENX, D.id,D.XUH)\n" + 
						"HAVING NOT(GROUPING(Y.FENX) = 1 OR GROUPING(D.ID)+GROUPING(D.XUH)=1)) SR\n" + 
						"GROUP BY SR.DC_ID)rc,(\n" + 
						"SELECT SR.DC_ID,\n" + 
						"max( decode(sr.fenx,'本月',SR.FRL))byfrl,\n" + 
						"max( decode(sr.fenx,'累计',SR.FRL))ljfrl,\n" + 
						"max( decode(sr.fenx,'本月',SR.BML))bybml,\n" + 
						"max( decode(sr.fenx,'累计',SR.BML))ljbml,\n" + 
						"max( decode(sr.fenx,'本月',SR.BMDJ))bybmdj,\n" + 
						"max( decode(sr.fenx,'累计',SR.BMDJ))ljbmdj,\n" + 
						"max( decode(sr.fenx,'本月',SR.TRYL))bytryl,\n" + 
						"max( decode(sr.fenx,'累计',SR.TRYL))ljtryl,\n" + 
						"max( decode(sr.fenx,'本月',SR.KCRZ))bykcrz,\n" + 
						"max( decode(sr.fenx,'累计',SR.KCRZ))ljkcrz,\n" + 
						"max( decode(sr.fenx,'本月',SR.KCDJ))bykcdj,\n" + 
						"max( decode(sr.fenx,'累计',SR.KCDJ))ljkcdj,\n" + 
						"max( decode(sr.fenx,'本月',SR.KUC))bykc,\n" + 
						"max( decode(sr.fenx,'累计',SR.KUC))ljkc\n" + 
						"FROM (SELECT DECODE(GROUPING(Z.DIANCXXB_ID), 1, -1, Z.DIANCXXB_ID) DC_ID,\n" + 
						"       Z.FENX,\n" + 
						"        ROUND_NEW(DECODE(SUM(Z.FADGRYTRML),0,0,SUM(Z.FADGRYTRML * Z.RULTRMPJFRL) /SUM(Z.FADGRYTRML)),2) FRL,\n" + 
						"        SUM(Z.RULMZBZML + Z.RULYZBZML + Z.RULQZBZML) BML,\n" + 
						"        ROUND(DECODE(SUM(Z.RULMZBZML + Z.RULYZBZML + Z.RULQZBZML),0,0,\n" + 
						"              SUM((Z.RULMZBZML + Z.RULYZBZML + Z.RULQZBZML) *Z.RULZHBMDJ) /SUM(Z.RULMZBZML + Z.RULYZBZML + Z.RULQZBZML)),2) BMDJ,\n" + 
						"        SUM(Z.FADYCB) TRYL,\n" + 
						"        ROUND_NEW(DECODE(SUM(H.KUC),0,0,SUM(H.KUC * Z.KUCTRMRZ) / SUM(H.KUC)),2) KCRZ,\n" + 
						"        ROUND(DECODE(SUM(H.KUC), 0, 0, SUM(H.KUC * Z.KUCTRMJ) / SUM(H.KUC)),2) KCDJ,\n" + 
						"        SUM(H.KUC) KUC\n" + 
						"  FROM YUEZBB Z, YUESHCHJB H\n" + 
						" WHERE Z.DIANCXXB_ID = H.DIANCXXB_ID(+)\n" + 
						"   AND Z.FENX = H.FENX(+)\n" + 
						"   AND Z.RIQ = H.RIQ(+)\n" + 
						"   AND Z.RIQ = DATE '"+strDate+"'\n" + 
						"   AND Z.DIANCXXB_ID NOT IN (215, 300, 100, 112)\n" + 
						" GROUP BY ROLLUP(Z.FENX, Z.DIANCXXB_ID)\n" + 
						"HAVING NOT GROUPING(Z.FENX) = 1)SR\n" + 
						"GROUP BY SR.DC_ID\n" + 
						")rl,\n" + 
						"(SELECT DECODE(GROUPING(DIANCXXB_ID), 1, -1, DIANCXXB_ID) DC_ID,\n" + 
						"       SUM(JINE) JINE\n" + 
						"  FROM ZAFB\n" + 
						" WHERE DIANCXXB_ID NOT IN (215, 300, 100, 112)\n" + 
						"    AND RIQ BETWEEN TRUNC(DATE '"+strDate+"', 'yyyy') AND DATE '2013-07-01'\n" + 
						" GROUP BY ROLLUP((DIANCXXB_ID)))zf,\n" + 
						"(SELECT ID, MINGC, XUH FROM DIANCXXB WHERE ID NOT IN (215, 300, 100, 112) UNION ALL SELECT -1,nvl('直管口径1(燃料)','')mc, 0 FROM dual )dc\n" + 
						"WHERE dc.id=rl.DC_ID(+)\n" + 
						"AND dc.id=rc.DC_ID(+)\n" + 
						"AND dc.id=zf.dc_id(+)\n" + 
						"ORDER BY dc.xuh)";

		ResultSetList rs=cn.getResultSetList(sql.toString());
		Report rt=new Report();
		//定义表头数据
		 String ArrHeader[][]=new String[2][13];
		 ArrHeader[0]=new String[] {"序号","单位","当月","当月","当月","累计","累计","累计","入厂入炉累计<br>差值分解","入厂入炉累计<br>差值分解","入厂入炉累计<br>差值分解","入厂入炉累计<br>差值分解","库存"};
		 ArrHeader[1]=new String[] {"序号","单位","入厂","入炉","差值","入厂","入炉","差值","杂费","燃油","热值差","核算方法","库存"};

		 int ArrWidth[]=new int[] {60,120,80,80,80,80,80,80,100,100,100,100,80};

		 rt.setTitle(strMonth+"标煤单价完成情况对比", ArrWidth);
		 rt.setDefaultTitle(1,3,"单位："+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(10, 3, "单位：元/吨", Table.ALIGN_RIGHT);
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