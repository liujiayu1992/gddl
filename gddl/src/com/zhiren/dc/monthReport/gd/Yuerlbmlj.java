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
//import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：韩超
 * 时间：2014-03-13
 * 描述：入厂标煤价
 */

public class Yuerlbmlj extends BasePage {
	
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
//		String strDate1=getNianfValue().getValue() +"-01-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";
		String sql="select mingc,rlsnbybml,rlbybml, rlbybmltb,rlsnbybmdj,rlbybmdj,rlbybmdjtb,rlsnljbml,rlljbml,rlljbmltb,rlsnljbmdj,\n" +
						"rlljbmdj,rlljbmdjtb,rlbmdjbnjh,bysz,jgyxrlf,jgyx,dlyx\n" + 
						" from ((select xuh,mingc,rlsnbybml,rlbybml, rlbybmltb,rlsnbybmdj,rlbybmdj,rlbybmdjtb,rlsnljbml,rlljbml,rlljbmltb,rlsnljbmdj,\n" + 
						"rlljbmdj,rlljbmdjtb,rlbmdjbnjh,bysz,jgyxrlf,jgyx,dlyx from (select d.xuh,d.mingc  mingc,\n" + 
						"round_new(sum(benysn.rulbml), 0)rlsnbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)rlbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)-round_new(sum(benysn.rulbml), 0) rlbybmltb,\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlsnbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)rlbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)-\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlbybmdjtb,\n" + 
						"round_new(sum(leijsn.rulbml), 0)rlsnljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)rlljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)-round_new(sum(leijsn.rulbml), 0)rlljbmltb,\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlsnljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)rlljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlljbmdjtb,\n" + 
						"round_new(sum(y.rlbmdjbnjh),2)rlbmdjbnjh,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-round_new(sum(y.rlbmdjbnjh),2)bysz,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml))*\n" + 
						"sum(leij.rulbml)/10000,2) jgyxrlf,\n" + 
						"0 jgyx,0 dlyx\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) beny ,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) benysn ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leij ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leijsn,\n" + 
						"    (select * from yuebqtsjb where riq=date'"+strDate+"') y,diancxxb d\n" + 
						"    where d.id=leij.diancxxb_id(+) and d.id=benysn.diancxxb_id(+)\n" + 
						"    and d.id=leijsn.diancxxb_id(+) and d.id=beny.diancxxb_id(+)\n" + 
						"    and d.id=y.diancxxb_id(+) and d.id not in (215,391,476,300) and d.jib>2\n" + 
						"group by rollup(beny.fenx,(d.mingc,d.xuh))\n" + 
						"having not grouping(d.mingc)=1\n" + 
						"order by d.xuh)\n" + 
						"union all\n" + 
						"--英力特\n" + 
						"select * from (select distinct d.xuh,decode(d.mingc,null,'英力特小计',d.mingc)  mingc,\n" + 
						"round_new(sum(benysn.rulbml), 0)rlsnbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)rlbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)-round_new(sum(benysn.rulbml), 0) rlbybmltb,\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlsnbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)rlbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)-\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlbybmdjtb,\n" + 
						"round_new(sum(leijsn.rulbml), 0)rlsnljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)rlljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)-round_new(sum(leijsn.rulbml), 0)rlljbmltb,\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlsnljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)rlljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlljbmdjtb,\n" + 
						"round_new(sum(y.rlbmdjbnjh),2)rlbmdjbnjh,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-round_new(sum(y.rlbmdjbnjh),2)bysz,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml))*\n" + 
						"sum(leij.rulbml)/10000,2) jgyxrlf,\n" + 
						"0 jgyx,0 dlyx\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) beny ,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) benysn ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leij ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leijsn,\n" + 
						"    (select * from yuebqtsjb where riq=date'"+strDate+"') y,diancxxb d\n" + 
						"    where d.id=leij.diancxxb_id(+) and d.id=benysn.diancxxb_id(+)\n" + 
						"    and d.id=leijsn.diancxxb_id(+) and d.id=beny.diancxxb_id(+)\n" + 
						"    and d.id=y.diancxxb_id(+) and d.id in (391,476) and d.jib>2\n" + 
						"group by rollup((beny.fenx,d.mingc,d.xuh))\n" + 
						"order by xuh)\n" + 
						"union all\n" + 
						"--外二\n" + 
						"select distinct d.xuh,decode(d.mingc,null,'外二',d.mingc)  mingc,\n" + 
						"round_new(sum(benysn.rulbml), 0)rlsnbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)rlbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)-round_new(sum(benysn.rulbml), 0) rlbybmltb,\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlsnbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)rlbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)-\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlbybmdjtb,\n" + 
						"round_new(sum(leijsn.rulbml), 0)rlsnljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)rlljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)-round_new(sum(leijsn.rulbml), 0)rlljbmltb,\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlsnljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)rlljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlljbmdjtb,\n" + 
						"round_new(sum(y.rlbmdjbnjh),2)rlbmdjbnjh,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-round_new(sum(y.rlbmdjbnjh),2)bysz,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml))*\n" + 
						"sum(leij.rulbml)/10000,2) jgyxrlf,\n" + 
						"0 jgyx,0 dlyx\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) beny ,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) benysn ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leij ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leijsn,\n" + 
						"    (select * from yuebqtsjb where riq=date'"+strDate+"') y,diancxxb d\n" + 
						"    where d.id=leij.diancxxb_id(+) and d.id=benysn.diancxxb_id(+)\n" + 
						"    and d.id=leijsn.diancxxb_id(+) and d.id=beny.diancxxb_id(+)\n" + 
						"    and d.id=y.diancxxb_id(+) and d.id in (215) and d.jib>2\n" + 
						"group by rollup(beny.fenx,(d.mingc,d.xuh))\n" + 
						"having not grouping(d.mingc)=1\n" + 
						"union all\n" + 
						"--直管口径1不含外二的\n" + 
						"select d.xuh,decode(d.mingc,null,'直管口径1(燃料)',d.mingc)  mingc,\n" + 
						"round_new(sum(benysn.rulbml), 0)rlsnbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)rlbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)-round_new(sum(benysn.rulbml), 0) rlbybmltb,\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlsnbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)rlbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)-\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlbybmdjtb,\n" + 
						
						"round_new(sum(leijsn.rulbml), 0)rlsnljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)rlljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)-round_new(sum(leijsn.rulbml), 0)rlljbmltb,\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlsnljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)rlljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlljbmdjtb,\n" + 
						"round_new(sum(y.rlbmdjbnjh),2)rlbmdjbnjh,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-round_new(sum(y.rlbmdjbnjh),2)bysz,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml))*\n" + 
						"sum(leij.rulbml)/10000,2) jgyxrlf,\n" + 
						"0 jgyx,0 dlyx\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) beny ,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) benysn ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leij ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leijsn,\n" + 
						"    (select * from yuebqtsjb where riq=date'"+strDate+"') y,diancxxb d\n" + 
						"    where d.id=leij.diancxxb_id(+) and d.id=benysn.diancxxb_id(+)\n" + 
						"    and d.id=leijsn.diancxxb_id(+) and d.id=beny.diancxxb_id(+)\n" + 
						"    and d.id=y.diancxxb_id(+) and d.id not in (215,300) and d.jib>2\n" + 
						"group by rollup(beny.fenx,(d.mingc,d.xuh))\n" + 
						"having not grouping(beny.fenx)=0\n" + 
						"union all\n" + 
						"--直管口径2不含外二和英化热电\n" + 
						"select d.xuh,decode(d.mingc,null,'直管口径2(计划)',d.mingc)  mingc,\n" + 
						"round_new(sum(benysn.rulbml), 0)rlsnbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)rlbybml,\n" + 
						"round_new(sum(beny.rulbml), 0)- round_new(sum(benysn.rulbml), 0)rlbybmltb,\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlsnbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)rlbybmdj,\n" + 
						"round_new(decode(sum(beny.rulbml),0,0,sum(beny.bmdj*beny.rulbml) /sum(beny.rulbml)),2)-\n" + 
						"round_new(decode(sum(benysn.rulbml),0,0,sum(benysn.bmdj*benysn.rulbml) /sum(benysn.rulbml)),2)rlbybmdjtb,\n" + 
						"round_new(sum(leijsn.rulbml), 0)rlsnljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)rlljbml,\n" + 
						"round_new(sum(leij.rulbml), 0)-round_new(sum(leijsn.rulbml), 0)rlljbmltb,\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlsnljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)rlljbmdj,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-\n" + 
						"round_new(decode(sum(leijsn.rulbml),0,0,sum(leijsn.bmdj*leijsn.rulbml) /sum(leijsn.rulbml)),2)rlljbmdjtb,\n" + 
						"round_new(sum(y.rlbmdjbnjh),2)rlbmdjbnjh,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml)),2)-round_new(sum(y.rlbmdjbnjh),2)bysz,\n" + 
						"round_new(decode(sum(leij.rulbml),0,0,sum(leij.bmdj*leij.rulbml) /sum(leij.rulbml))*\n" + 
						"sum(leij.rulbml)/10000,2) jgyxrlf,\n" + 
						"0 jgyx,0 dlyx\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) beny ,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) benysn ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leij ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leijsn,\n" + 
						"    (select * from yuebqtsjb where riq=date'"+strDate+"') y,diancxxb d\n" + 
						"    where d.id=leij.diancxxb_id(+) and d.id=benysn.diancxxb_id(+)\n" + 
						"    and d.id=leijsn.diancxxb_id(+) and d.id=beny.diancxxb_id(+)\n" + 
						"    and d.id=y.diancxxb_id(+) and d.id not in (215,391,300) and d.jib>2\n" + 
						"group by rollup(beny.fenx,(d.mingc,d.xuh))\n" + 
						"having not grouping(beny.fenx)=0)\n" + 
						"union all\n" + 
						"--累计是自动算的\n" + 
						"select d.fuid xuh,decode(d.fuid+grouping(d.mingc),611,'江苏小计',621,'新疆小计',d.mingc)mingc,\n" + 
						"  nvl(sum(zsn.rlbml),0) rlsnbybml,nvl(sum(z.rlbml),0) rlbybml,nvl(sum(z.rlbml-zsn.rlbml),0) rlbybmltb,\n" + 
						"  nvl(sum(zsn.rlzhbmdj),0) rlsnbybmdj,nvl(sum(z.rlzhbmdj),0) rlbybmdj,nvl(sum(z.rlzhbmdj-zsn.rlzhbmdj),0) rlbybmdjtb,\n" + 
						"  nvl(sum(zsnlj.rlbml),0) rlsnljbml,nvl(sum(zlj.rlbml),0) rlljbml,nvl(sum(zlj.rlbml-zsnlj.rlbml),0) rlljbmltb,\n" + 
						"  nvl(sum(zsnlj.rlzhbmdj),0) rlsnljbmdj,nvl(sum(zlj.rlzhbmdj),0) rlljbmdj,nvl(sum(z.rlzhbmdj-zsnlj.rlzhbmdj),0) rlljbmdjtb,\n" + 
						"  nvl(sum(z.rlbmdjbnjh),0)rlbmdjbnjh,nvl(sum(zlj.rlzhbmdj-z.rlbmdjbnjh),0) bysz,\n" + 
						"  nvl(round_new((sum(zsnlj.rlzhbmdj-z.rlzhbmdj))*sum(z.rlbml)/10000,2),0) jgyxrlf,0 jgyx,0 dlyx from\n" + 
						"  (select diancxxb_id,rlbml,rlzhbmdj,rlbmdjbnjh from zickjsjb where riq=to_date('"+strDate+"','yyyy-mm-dd')) z,\n" + 
						"  (select diancxxb_id,rlbml,rlzhbmdj from zickjsjb where riq=add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)) zsn,\n" + 
						"  (select diancxxb_id,sum(rlbml) rlbml,sum(rlzhbmdj) rlzhbmdj from zickjsjb\n" + 
						"  where riq>=to_date('"+getNianfValue().getValue()+"-01-01','yyyy-mm-dd') and riq<=to_date('"+strDate+"','yyyy-mm-dd') group by diancxxb_id) zlj  ,\n" + 
						"  (select diancxxb_id,sum(rlbml) rlbml,sum(rlzhbmdj) rlzhbmdj from zickjsjb\n" + 
						"  where riq>=add_months(to_date('"+getNianfValue().getValue()+"-01-01','yyyy-mm-dd'),-12) and riq<=add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12) group by diancxxb_id) zsnlj,\n" + 
						"  yuebdwb d\n" + 
						"  where d.diancxxb_id=zsn.diancxxb_id(+) and d.diancxxb_id=z.diancxxb_id(+)\n" + 
						"  and d.diancxxb_id=zsnlj.diancxxb_id(+)  and d.diancxxb_id=zlj.diancxxb_id(+)\n" + 
						"  and d.fuid>0\n" + 
						"  group by rollup (d.fuid,(d.mingc))\n" + 
						"  having not grouping(d.fuid)=1\n" + 
						"  union all\n" + 
						"select xuh,decode(mingc,null,'资产口径1(燃料)',mingc)mingc,round(sum(rlsnbybml),2)rlsnbybml,\n" +
						"round(sum(rlbybml),2)rlbybml,round(sum(rlbybml-rlsnbybml),2)rlbybmltb,\n" + 
						"round(decode(sum(rlsnbybml),0,0,sum(rlsnbybmdj*rlsnbybml)/sum(rlsnbybml)),2)rlsnbybmdj,\n" + 
						"round(decode(sum(rlbybml),0,0,sum(rlbybmdj*rlbybml)/sum(rlbybml)),2)rlbybmdj,\n" + 
						"round(decode(sum(rlbybml),0,0,sum(rlbybmdj*rlbybml)/sum(rlbybml))-\n" + 
						"decode(sum(rlsnbybml),0,0,sum(rlsnbybmdj*rlsnbybml)/sum(rlsnbybml)),2)rlbybmdjtb,\n" + 
						"round(sum(rlsnljbml),2)rlsnljbml,round(sum(rlljbml),2)rlljbml,round(sum(rlljbml-rlsnljbml),2)rlljbmltb,\n" + 
						"round_new(decode(sum(rlsnljbml),0,0,sum(rlsnljbmdj*rlsnljbml) /sum(rlsnljbml)),2)rlsnljbmdj,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml)),2)rlljbmdj,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml)),2)-\n" + 
						"round_new(decode(sum(rlsnljbml),0,0,sum(rlsnljbmdj*rlsnljbml) /sum(rlsnljbml)),2)rlljbmdjtb,\n" + 
						"round_new(sum(rlbmdjbnjh),2)rlbmdjbnjh,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml)),2)-round_new(sum(rlbmdjbnjh),2)bysz,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml))*sum(rlljbml)/10000,2)jgyxrlf,\n" + 
						"sum(jgyx)jgyx,sum(dlyx)dlyx\n" + 
						" from(select xuh,mingc,rlsnbybml,rlbybml,rlsnbybmdj,rlbybmdj,\n" + 
						"rlsnljbml,rlljbml,rlsnljbmdj,rlljbmdj,rlbmdjbnjh,jgyx,dlyx\n" + 
						" from (select d.xuh,d.mingc,benysn.rulbml rlsnbybml,beny.rulbml rlbybml,\n" + 
						"benysn.bmdj rlsnbybmdj,beny.bmdj rlbybmdj,leijsn.rulbml rlsnljbml,leij.rulbml rlljbml,\n" + 
						"leijsn.bmdj rlsnljbmdj,leij.bmdj rlljbmdj,y.rlbmdjbnjh rlbmdjbnjh,0 jgyx,0 dlyx\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) beny ,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) benysn ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leij ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leijsn,\n" + 
						"    (select * from yuebqtsjb where riq=date'"+strDate+"') y,diancxxb d\n" + 
						"    where d.id=leij.diancxxb_id(+) and d.id=benysn.diancxxb_id(+)\n" + 
						"    and d.id=leijsn.diancxxb_id(+) and d.id=beny.diancxxb_id(+)\n" + 
						"    and d.id=y.diancxxb_id(+) and d.id not in (215,300) and d.jib>2\n" + 
						"order by d.xuh)\n" + 
						"union all\n" + 
						"select d.fuid xuh,d.mingc mingc,\n" + 
						"  nvl(zsn.rlbml,0) rlsnbybml,nvl(z.rlbml,0) rlbybml,\n" + 
						"  nvl(zsn.rlzhbmdj,0) rlsnbybmdj,nvl(z.rlzhbmdj,0) rlbybmdj,\n" + 
						"  nvl(zsnlj.rlbml,0) rlsnljbml,nvl(zlj.rlbml,0) rlljbml,\n" + 
						"  nvl(zsnlj.rlzhbmdj,0) rlsnljbmdj,nvl(zlj.rlzhbmdj,0) rlljbmdj,\n" + 
						"  nvl(z.rlbmdjbnjh,0)rlbmdjbnjh,0 jgyx,0 dlyx from\n" + 
						"  (select diancxxb_id,rlbml,rlzhbmdj,rlbmdjbnjh from zickjsjb where riq=to_date('"+strDate+"','yyyy-mm-dd')) z,\n" + 
						"  (select diancxxb_id,rlbml,rlzhbmdj from zickjsjb where riq=add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)) zsn,\n" + 
						"  (select diancxxb_id,sum(rlbml) rlbml,sum(rlzhbmdj) rlzhbmdj from zickjsjb\n" + 
						"  where riq>=to_date('"+getNianfValue().getValue()+"-01-01','yyyy-mm-dd') and riq<=to_date('"+strDate+"','yyyy-mm-dd') group by diancxxb_id) zlj  ,\n" + 
						"  (select diancxxb_id,sum(rlbml) rlbml,sum(rlzhbmdj) rlzhbmdj from zickjsjb\n" + 
						"  where riq>=add_months(to_date('"+getNianfValue().getValue()+"-01-01','yyyy-mm-dd'),-12) and riq<=add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12) group by diancxxb_id) zsnlj,\n" + 
						"  yuebdwb d\n" + 
						"  where d.diancxxb_id=zsn.diancxxb_id(+) and d.diancxxb_id=z.diancxxb_id(+) and d.diancxxb_id=zsnlj.diancxxb_id(+)\n" + 
						"  and zlj.diancxxb_id(+)=d.diancxxb_id and d.fuid>0)zckj1\n" + 
						"  group by rollup((zckj1.mingc,zckj1.xuh)) having not grouping(mingc)=0"+
						"union all\n" + 
						"select xuh,decode(mingc,null,'资产口径2(计划)',mingc)mingc,round(sum(rlsnbybml),2)rlsnbybml,\n" +
						"round(sum(rlbybml),2)rlbybml,round(sum(rlbybml-rlsnbybml),2)rlbybmltb,\n" + 
						"round(decode(sum(rlsnbybml),0,0,sum(rlsnbybmdj*rlsnbybml)/sum(rlsnbybml)),2)rlsnbybmdj,\n" + 
						"round(decode(sum(rlbybml),0,0,sum(rlbybmdj*rlbybml)/sum(rlbybml)),2)rlbybmdj,\n" + 
						"round(decode(sum(rlbybml),0,0,sum(rlbybmdj*rlbybml)/sum(rlbybml))-\n" + 
						"decode(sum(rlsnbybml),0,0,sum(rlsnbybmdj*rlsnbybml)/sum(rlsnbybml)),2)rlbybmdjtb,\n" + 
						"round(sum(rlsnljbml),2)rlsnljbml,round(sum(rlljbml),2)rlljbml,round(sum(rlljbml-rlsnljbml),2)rlljbmltb,\n" + 
						"round_new(decode(sum(rlsnljbml),0,0,sum(rlsnljbmdj*rlsnljbml) /sum(rlsnljbml)),2)rlsnljbmdj,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml)),2)rlljbmdj,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml)),2)-\n" + 
						"round_new(decode(sum(rlsnljbml),0,0,sum(rlsnljbmdj*rlsnljbml) /sum(rlsnljbml)),2)rlljbmdjtb,\n" + 
						"round_new(sum(rlbmdjbnjh),2)rlbmdjbnjh,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml)),2)-round_new(sum(rlbmdjbnjh),2)bysz,\n" + 
						"round_new(decode(sum(rlljbml),0,0,sum(rlljbmdj*rlljbml) /sum(rlljbml))*sum(rlljbml)/10000,2)jgyxrlf,\n" + 
						"sum(jgyx)jgyx,sum(dlyx)dlyx\n" + 
						" from(select xuh,mingc,rlsnbybml,rlbybml,rlsnbybmdj,rlbybmdj,\n" + 
						"rlsnljbml,rlljbml,rlsnljbmdj,rlljbmdj,rlbmdjbnjh,jgyx,dlyx\n" + 
						" from (select d.xuh,d.mingc,benysn.rulbml rlsnbybml,beny.rulbml rlbybml,\n" + 
						"benysn.bmdj rlsnbybmdj,beny.bmdj rlbybmdj,leijsn.rulbml rlsnljbml,leij.rulbml rlljbml,\n" + 
						"leijsn.bmdj rlsnljbmdj,leij.bmdj rlljbmdj,y.rlbmdjbnjh rlbmdjbnjh,0 jgyx,0 dlyx\n" + 
						"from\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) beny ,\n" + 
						"(SELECT  Z.DIANCXXB_ID DIANCXXB_ID,Z.FENX,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='本月'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) benysn ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = to_date('"+strDate+"','yyyy-mm-dd')\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leij ,\n" + 
						"    (SELECT  Z.DIANCXXB_ID DIANCXXB_ID,\n" + 
						"\t\t      SUM((Z.RULMZBZML+z.rulyzbzml+z.rulqzbzml))RULBML,\n" + 
						"\t\t      ROUND(DECODE(SUM((Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),0,0,\n" + 
						"          SUM(Z.RULZHBMDJ*(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML))/SUM(Z.RULMZBZML+Z.RULYZBZML+Z.RULQZBZML)),2) BMDJ\n" + 
						"\t\t  FROM YUEZBB Z\n" + 
						"\t\t WHERE Z.RIQ = add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)\n" + 
						"     AND (z.zhuangt=1 OR z.zhuangt=3)\n" + 
						"     and z.fenx='累计'\n" + 
						" \t\t group by  z.DIANCXXB_ID ,Z.FENX\n" + 
						"\t\t) leijsn,\n" + 
						"    (select * from yuebqtsjb where riq=date'"+strDate+"') y,diancxxb d\n" + 
						"    where d.id=leij.diancxxb_id(+) and d.id=benysn.diancxxb_id(+)\n" + 
						"    and d.id=leijsn.diancxxb_id(+) and d.id=beny.diancxxb_id(+)\n" + 
						"    and d.id=y.diancxxb_id(+) and d.id not in (215,391,300) and d.jib>2\n" + 
						"order by d.xuh)\n" + 
						"union all\n" + 
						"select d.fuid xuh,d.mingc mingc,\n" + 
						"  nvl(zsn.rlbml,0) rlsnbybml,nvl(z.rlbml,0) rlbybml,\n" + 
						"  nvl(zsn.rlzhbmdj,0) rlsnbybmdj,nvl(z.rlzhbmdj,0) rlbybmdj,\n" + 
						"  nvl(zsnlj.rlbml,0) rlsnljbml,nvl(zlj.rlbml,0) rlljbml,\n" + 
						"  nvl(zsnlj.rlzhbmdj,0) rlsnljbmdj,nvl(zlj.rlzhbmdj,0) rlljbmdj,\n" + 
						"  nvl(z.rlbmdjbnjh,0)rlbmdjbnjh,0 jgyx,0 dlyx from\n" + 
						"  (select diancxxb_id,rlbml,rlzhbmdj,rlbmdjbnjh from zickjsjb where riq=to_date('"+strDate+"','yyyy-mm-dd')) z,\n" + 
						"  (select diancxxb_id,rlbml,rlzhbmdj from zickjsjb where riq=add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12)) zsn,\n" + 
						"  (select diancxxb_id,sum(rlbml) rlbml,sum(rlzhbmdj) rlzhbmdj from zickjsjb\n" + 
						"  where riq>=to_date('"+getNianfValue().getValue()+"-01-01','yyyy-mm-dd') and riq<=to_date('"+strDate+"','yyyy-mm-dd') group by diancxxb_id) zlj  ,\n" + 
						"  (select diancxxb_id,sum(rlbml) rlbml,sum(rlzhbmdj) rlzhbmdj from zickjsjb\n" + 
						"  where riq>=add_months(to_date('"+getNianfValue().getValue()+"-01-01','yyyy-mm-dd'),-12) and riq<=add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12) group by diancxxb_id) zsnlj,\n" + 
						"  yuebdwb d\n" + 
						"  where d.diancxxb_id=zsn.diancxxb_id(+) and d.diancxxb_id=z.diancxxb_id(+) and d.diancxxb_id=zsnlj.diancxxb_id(+)\n" + 
						"  and zlj.diancxxb_id(+)=d.diancxxb_id and d.fuid>0)zckj1\n" + 
						"  group by rollup((zckj1.mingc,zckj1.xuh)) having not grouping(mingc)=0)";

		ResultSetList rs=cn.getResultSetList(sql.toString());
//		System.out.print(sql.toString());
		Report rt=new Report();
		//定义表头数据
		 String ArrHeader[][]=new String[3][19];
		 int n=Integer.parseInt(getNianfValue().getValue());
		 ArrHeader[0]=new String[] {"项目","当月","当月","当月","当月","当月","当月","累计","累计",
				 "累计","累计","累计","累计",getNianfValue().getValue()+"年预算","比预算值(±)(元/吨)","影响因素","影响因素","影响因素",""};
		 ArrHeader[1]=new String[] {"项目","标煤量(吨)","标煤量(吨)","标煤量(吨)","单价(元/吨)","单价(元/吨)",
				 "单价(元/吨)","标煤量(吨)","标煤量(吨)","标煤量(吨)","单价(元/吨)","单价(元/吨)","单价(元/吨)",
				 getNianfValue().getValue()+"年预算","比预算值(±)(元/吨)","价格影响燃料费(万元)","价格影响(元/吨)","电量影响(元/吨)","电量影响(元/吨)"};
		 ArrHeader[2]=new String[] {"项目",n+"年",getNianfValue().getValue()+"年","同比",n+"年",getNianfValue().getValue()+"年","同比",n+"年",getNianfValue().getValue()+"年",
				 "同比",n+"年",getNianfValue().getValue()+"年","同比",getNianfValue().getValue()+"年预算","比预算值(±)(元/吨)","价格影响燃料费(万元)","价格影响(元/吨)",
				 "电量影响(元/吨)","电量影响(元/吨)"};

		 int ArrWidth[]=new int[] {120,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80};

		 rt.setTitle(strMonth+"入炉标煤量价", ArrWidth);
		 rt.setDefaultTitle(1,3,"单位："+this.getTianzdwQuanc(),Table.ALIGN_LEFT);
		 rt.setDefaultTitle(16, 3, "单位：元/吨", Table.ALIGN_RIGHT);
		 
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