package com.zhiren.jingjfx;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 类名：入炉标煤单价分析维护
 */

public class Rulbmdjfxwh extends BasePage implements PageValidateListener {
	
	private boolean happenWrong; // 判断保存时是否有错误数据，true为是，false为否
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	将有错误的数据拼成可以生成Record的字符串，保存到wrongDataSource变量中。
	private String wrongDataSource; 
	
	public String getWrongDataSource() {
		return wrongDataSource;
	}

	public void setWrongDataSource(String wrongDataSource) {
		this.wrongDataSource = wrongDataSource;
	}
	
//	年份下拉框_开始
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	年份下拉框_结束
	
//	月份下拉框_开始
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	月份下拉框_结束
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		List arraylist = new ArrayList(); // 用来保存有错误的数据
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from jingjfxrlbmdjb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		String errorMsg = "";
		while(mdrsl.next()) {
			
			StringBuffer validate_sql = new StringBuffer();
			validate_sql.append("select id from jingjfxrlbmdjb bmdj where bmdj.riq = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd')")
				.append(" and bmdj.diancxxb_id = ").append((getExtGrid().getColumn("mingc").combo).getBeanId(mdrsl.getString("mingc")));
			
			if("0".equals(mdrsl.getString("id"))) {
				if (JDBCcon.getRow(con.getResultSet(validate_sql.toString())) != 0) {
					errorMsg += "单位名称 " + mdrsl.getString("mingc") + " 重复不能添加！<br>";
					arraylist.add(mdrsl.getResultSetlist().get(mdrsl.getRow())); // 将有错误的数据放到一个list中
				} else {
					sbsql.append("insert into jingjfxrlbmdjb(id, diancxxb_id, riq, yusdj, shijwcdj, leijwcdj) values(getnewid(")
						.append((getExtGrid().getColumn("mingc").combo).getBeanId(mdrsl.getString("mingc")))
						.append("), ").append((getExtGrid().getColumn("mingc").combo).getBeanId(mdrsl.getString("mingc")))
						.append(", to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), ")
						.append(getSqlValue(mdrsl.getString("yusdj"))).append(", ").append(getSqlValue(mdrsl.getString("shijwcdj"))).append(", ")
						.append(getSqlValue(mdrsl.getString("leijwcdj"))).append(");\n");
				}
			} else {
				if (JDBCcon.getRow(con.getResultSet(validate_sql.toString())) != 0) {
					errorMsg += "单位名称 " + mdrsl.getString("mingc") + " 重复不能更新！<br>";
					arraylist.add(mdrsl.getResultSetlist().get(mdrsl.getRow())); // 将有错误的数据放到一个list中
				} else {
					sbsql.append("update jingjfxrlbmdjb set diancxxb_id = ").append((getExtGrid().getColumn("mingc").combo).getBeanId(mdrsl.getString("mingc")))
						.append(", yusdj = ").append(getSqlValue(mdrsl.getString("yusdj"))).append(", shijwcdj = ").append(getSqlValue(mdrsl.getString("shijwcdj")))
						.append(", leijwcdj = ").append(getSqlValue(mdrsl.getString("leijwcdj"))).append(" where id = ").append(mdrsl.getString("id")).append(");\n");
				}
			}
		}
		sbsql.append("end;");
		
		if (errorMsg != "") {
//			将有错误的数据拼成可以生成Record的字符串，保存到wrongDataSource变量中。
			String str2 = "";
			StringBuffer strsb = new StringBuffer();
			for(int i = 0; i < arraylist.size(); i ++) {
				String str = "";
				for (int j = 0; j < mdrsl.getColumnCount(); j ++) {
					str += mdrsl.getColumnNames()[j]+":'"+((String[])arraylist.get(i))[j]+"',";
				}
				str2 = "{"+str.substring(0, str.lastIndexOf(","))+"}";
				strsb.append(str2).append("&");
			}
			setWrongDataSource(strsb.substring(0, strsb.lastIndexOf("&")));
			happenWrong = true; // 保存时页面有错误数据。
			setMsg(errorMsg);
		}
		if (!sbsql.toString().equals("begin\nend;")) {
			con.getUpdate(sbsql.toString());
		}
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql = 
			"select bmdj.id, dc.mingc, bmdj.yusdj, bmdj.shijwcdj, bmdj.leijwcdj\n" +
			"  from jingjfxrlbmdjb bmdj, diancxxb dc\n" + 
			" where bmdj.diancxxb_id = dc.id\n" + 
			"   and bmdj.riq = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd')\n" + 
			"   and (dc.id = "+ getTreeid() +" or dc.fuid = "+ getTreeid() +")\n" + 
			"order by dc.jib, dc.mingc";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("mingc").setHeader("单位名称");
		egu.getColumn("yusdj").setHeader("集团预算");
		egu.getColumn("shijwcdj").setHeader("实际完成");
		egu.getColumn("leijwcdj").setHeader("累计完成");
		
		String dianc_sql = "select dc.id, dc.mingc, dc.fuid, dc.jib from diancxxb dc where dc.jib = 2 or dc.jib = 3 order by dc.jib, dc.mingc";
		if (visit.getRenyjb() == 3) {
			dianc_sql = "select dc.id, dc.mingc from diancxxb dc where dc.jib = 3 order by dc.fuid, dc.mingc";
		}
		egu.getColumn("mingc").setEditor(new ComboBox());
		egu.getColumn("mingc").setComboEditor(egu.gridId, 
			new IDropDownModel(dianc_sql));
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("年份：");
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(70);
		nf_comb.setTransform("Nianf");
		nf_comb.setId("Nianf");
		nf_comb.setLazyRender(true);
		nf_comb.setEditable(true);
		egu.addToolbarItem(nf_comb.getScript());
		egu.addOtherScript("Nianf.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("月份：");
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(70);
		yf_comb.setTransform("Yuef");
		yf_comb.setId("Yuef");
		yf_comb.setLazyRender(true);
		yf_comb.setEditable(true);
		egu.addToolbarItem(yf_comb.getScript());
		egu.addOtherScript("Yuef.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
//		将保存时发现有错误的数据显示在页面上
		if (happenWrong) {
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getWrongDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("var p=new gridDiv_plant("+ recs[i] +");\n gridDiv_ds.insert("+ i +",p);\n");
				sb.append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
			happenWrong = false;
		}
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
//	电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	
	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
//	电厂树_结束
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // 月份下拉框
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}
}