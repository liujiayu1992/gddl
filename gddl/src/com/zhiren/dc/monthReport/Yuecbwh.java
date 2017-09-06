package com.zhiren.dc.monthReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * 类名：月成本维护
 */

public class Yuecbwh extends BasePage implements PageValidateListener {
	
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
	
//	"生成"按钮
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_CreateClick) {
			_CreateClick = false;
			createData();
		}
	}
	
	public void createData() {
		
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		String sql = 
			"select fh.meikxxb_id, fh.jihkjb_id, sum(fh.laimsl) caigsl,\n" +
			"       round_new(sum(fh.laimsl * ls.rez) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 0) caigfrl,\n" + 
			"       round_new(sum(fh.laimsl * (ls.meij + ls.yunf + ls.zaf + ls.fazzf + ls.ditf)) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) as caigje,\n" + 
			"       round_new(sum(fh.laimsl * ls.meij) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) as kuangj,\n" + 
			"       round_new(sum(fh.laimsl * (ls.fazzf + ls.ditf)) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) yunzf,\n" + 
			"       round_new(sum(fh.laimsl * ls.zaf) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) changnfy,\n" + 
			"       round_new(sum(fh.laimsl * fh.jingz) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) biaoml,\n" + 
			"       round_new(sum(fh.laimsl * round_new((ls.meij + ls.yunf + ls.zaf + ls.fazzf+ls.ditf) * 7000 / ls.rez, 2)) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) biaomdj\n" + 
			"  from guslsb ls, fahb fh\n" + 
			" where ls.id in (select max(ls.id)\n" + 
			"                   from fahb fh, guslsb ls\n" + 
			"                  where to_date(to_char(fh.daohrq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')\n" + 
			"                    and ls.fahb_id = fh.id\n" + 
			"                  group by ls.fahb_id)\n" + 
			"   and ls.fahb_id = fh.id\n" + 
			"group by fh.meikxxb_id, fh.jihkjb_id\n" + 
			"order by fh.meikxxb_id";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			sbsql.append("delete from yuecbrmb rm where to_char(rm.riq, 'yyyy-mm') = '"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"';\n");
			while(rsl.next()) {
				sbsql.append("insert into yuecbrmb(id, diancxxb_id, meikxxb_id, jihkjb_id, riq, xuh, caigsl, caigfrl, caigje, kuangj, yunzf, changnfy, biaoml, biaomdj) values(")
				.append("getnewid(").append(getTreeid()).append("), ").append(getTreeid()).append(", ")
				.append(rsl.getString("meikxxb_id")).append(", ").append(rsl.getString("jihkjb_id")).append(", ")
				.append("to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd')").append(", ")
				.append("(select max(xuh)+1 from yuecbrmb)").append(", ").append(rsl.getString("caigsl")).append(", ")
				.append(rsl.getString("caigfrl")).append(", ").append(rsl.getString("caigje")).append(", ")
				.append(rsl.getString("kuangj")).append(", ").append(rsl.getString("yunzf")).append(", ")
				.append(rsl.getString("changnfy")).append(", ").append(rsl.getString("biaoml")).append(", ")
				.append(rsl.getString("biaomdj")).append(");\n");
			}
			sbsql.append("end;");
			con.getUpdate(sbsql.toString());
		} else {
			setMsg("同期没有数据，无法生成！");
		}
		rsl.close();
		con.Close();
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			if (visit.getString1().equals("ranm")) {
				sbsql.append("delete from yuecbrmb where id = ").append(delrsl.getString("id")).append(";\n");
			} else {
				sbsql.append("delete from yuecbryb where id = ").append(delrsl.getString("id")).append(";\n");
			}
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				if (visit.getString1().equals("ranm")) {
					sbsql.append("insert into yuecbrmb(id, diancxxb_id, meikxxb_id, jihkjb_id, riq, xuh, niancjcsl, niancjcdj, niancjcje, caigsl, yunj, caigfrl, \n" +
							"caigje, kuangj, yunzf, changnfy, biaoml, biaomdj, rulsl, ruldj, rulje, rulzs, rulfrl, rulbml, meizbmdj, youzbmdj, zonghbmdj, rezc,\n" + 
							"qithysl, qithydj, qithyje, qimjysl, qimjydj, qimjyje) values(").append("getnewid(").append(getTreeid()).append("), ").append(getTreeid())
							.append(", ").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")))
							.append(", ").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id")))
							.append(", to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), ").append(mdrsl.getString("xuh"))
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcsl"))).append(", ").append(getSqlValue(mdrsl.getString("niancjcdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcje"))).append(", ").append(getSqlValue(mdrsl.getString("caigsl")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunj"))).append(", ").append(getSqlValue(mdrsl.getString("caigfrl")))
							.append(", ").append(getSqlValue(mdrsl.getString("caigje"))).append(", ").append(getSqlValue(mdrsl.getString("kuangj")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunzf"))).append(", ").append(getSqlValue(mdrsl.getString("changnfy")))
							.append(", ").append(getSqlValue(mdrsl.getString("biaoml"))).append(", ").append(getSqlValue(mdrsl.getString("biaomdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulsl"))).append(", ").append(getSqlValue(mdrsl.getString("ruldj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulje"))).append(", ").append(getSqlValue(mdrsl.getString("rulzs")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulfrl"))).append(", ").append(getSqlValue(mdrsl.getString("rulbml")))
							.append(", ").append(getSqlValue(mdrsl.getString("meizbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("youzbmdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("zonghbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("rezc")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithysl"))).append(", ").append(getSqlValue(mdrsl.getString("qithydj")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithyje"))).append(", ").append(getSqlValue(mdrsl.getString("qimjysl")))
							.append(", ").append(getSqlValue(mdrsl.getString("qimjydj"))).append(", ").append(getSqlValue(mdrsl.getString("qimjyje")))
							.append(");\n");
				} else {
					sbsql.append("insert into yuecbryb(id, diancxxb_id, riq, niancjcsl, niancjcdj, niancjcje, caigsl, yunj, caigfrl, \n" +
							"caigje, kuangj, yunzf, changnfy, biaoml, biaomdj, rulsl, ruldj, rulje, rulzs, rulfrl, rulbml, meizbmdj, youzbmdj, zonghbmdj, rezc,\n" + 
							"qithysl, qithydj, qithyje, qimjysl, qimjydj, qimjyje) values(").append("getnewid(").append(getTreeid()).append("), ").append(getTreeid())
							.append(", to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd') ")
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcsl"))).append(", ").append(getSqlValue(mdrsl.getString("niancjcdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcje"))).append(", ").append(getSqlValue(mdrsl.getString("caigsl")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunj"))).append(", ").append(getSqlValue(mdrsl.getString("caigfrl")))
							.append(", ").append(getSqlValue(mdrsl.getString("caigje"))).append(", ").append(getSqlValue(mdrsl.getString("kuangj")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunzf"))).append(", ").append(getSqlValue(mdrsl.getString("changnfy")))
							.append(", ").append(getSqlValue(mdrsl.getString("biaoml"))).append(", ").append(getSqlValue(mdrsl.getString("biaomdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulsl"))).append(", ").append(getSqlValue(mdrsl.getString("ruldj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulje"))).append(", ").append(getSqlValue(mdrsl.getString("rulzs")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulfrl"))).append(", ").append(getSqlValue(mdrsl.getString("rulbml")))
							.append(", ").append(getSqlValue(mdrsl.getString("meizbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("youzbmdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("zonghbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("rezc")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithysl"))).append(", ").append(getSqlValue(mdrsl.getString("qithydj")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithyje"))).append(", ").append(getSqlValue(mdrsl.getString("qimjysl")))
							.append(", ").append(getSqlValue(mdrsl.getString("qimjydj"))).append(", ").append(getSqlValue(mdrsl.getString("qimjyje")))
							.append(");\n");
				}
			} else {
				if (visit.getString1().equals("ranm")) {
					sbsql.append("update yuecbrmb set")
					.append(" meikxxb_id = ").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"))).append(", ")
					.append(" jihkjb_id = ").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id"))).append(", ")
					.append(" xuh = ").append(getSqlValue(mdrsl.getString("xuh"))).append(", ");
				} else {
					sbsql.append("update yuecbryb set");
				}
				sbsql.append(" niancjcsl = ").append(getSqlValue(mdrsl.getString("niancjcsl"))).append(", ")
				.append(" niancjcdj = ").append(getSqlValue(mdrsl.getString("niancjcdj"))).append(", ")
				.append(" niancjcje = ").append(getSqlValue(mdrsl.getString("niancjcje"))).append(", ")
				.append(" caigsl = ").append(getSqlValue(mdrsl.getString("caigsl"))).append(", ")
				.append(" yunj = ").append(getSqlValue(mdrsl.getString("yunj"))).append(", ")
				.append(" caigfrl = ").append(getSqlValue(mdrsl.getString("caigfrl"))).append(", ")
				.append(" caigje = ").append(getSqlValue(mdrsl.getString("caigje"))).append(", ")
				.append(" kuangj = ").append(getSqlValue(mdrsl.getString("kuangj"))).append(", ")
				.append(" yunzf = ").append(getSqlValue(mdrsl.getString("yunzf"))).append(", ")
				.append(" changnfy = ").append(getSqlValue(mdrsl.getString("changnfy"))).append(", ")
				.append(" biaoml = ").append(getSqlValue(mdrsl.getString("biaoml"))).append(", ")
				.append(" biaomdj = ").append(getSqlValue(mdrsl.getString("biaomdj"))).append(", ")
				.append(" rulsl = ").append(getSqlValue(mdrsl.getString("rulsl"))).append(", ")
				.append(" ruldj = ").append(getSqlValue(mdrsl.getString("ruldj"))).append(", ")
				.append(" rulje = ").append(getSqlValue(mdrsl.getString("rulje"))).append(", ")
				.append(" rulzs = ").append(getSqlValue(mdrsl.getString("rulzs"))).append(", ")
				.append(" rulfrl = ").append(getSqlValue(mdrsl.getString("rulfrl"))).append(", ")
				.append(" rulbml = ").append(getSqlValue(mdrsl.getString("rulbml"))).append(", ")
				.append(" meizbmdj = ").append(getSqlValue(mdrsl.getString("meizbmdj"))).append(", ")
				.append(" youzbmdj = ").append(getSqlValue(mdrsl.getString("youzbmdj"))).append(", ")
				.append(" zonghbmdj = ").append(getSqlValue(mdrsl.getString("zonghbmdj"))).append(", ")
				.append(" rezc = ").append(getSqlValue(mdrsl.getString("rezc"))).append(", ")
				.append(" qithysl = ").append(getSqlValue(mdrsl.getString("qithysl"))).append(", ")
				.append(" qithydj = ").append(getSqlValue(mdrsl.getString("qithydj"))).append(", ")
				.append(" qithyje = ").append(getSqlValue(mdrsl.getString("qithyje"))).append(", ")
				.append(" qimjysl = ").append(getSqlValue(mdrsl.getString("qimjysl"))).append(", ")
				.append(" qimjydj = ").append(getSqlValue(mdrsl.getString("qimjydj"))).append(", ")
				.append(" qimjyje = ").append(getSqlValue(mdrsl.getString("qimjyje")))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		
		String sql = 
			"select ry.id, ry.niancjcsl, ry.niancjcdj, ry.niancjcje, ry.caigsl, ry.yunj,\n" +
			"       ry.caigfrl, ry.caigje, ry.kuangj, ry.yunzf, ry.changnfy, ry.biaoml, ry.biaomdj, ry.rulsl, ry.ruldj,\n" + 
			"       ry.rulje, ry.rulzs, ry.rulfrl, ry.rulbml, ry.meizbmdj, ry.youzbmdj, ry.zonghbmdj, ry.rezc, ry.qithysl,\n" + 
			"       ry.qithydj, ry.qithyje, ry.qimjysl, ry.qimjydj, ry.qimjyje from yuecbryb ry" +
			" where ry.riq = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-1', 'yyyy-mm-dd')";
		
		if (visit.getString1().equals("ranm")) { // 判断是否为燃煤
			sql = 
				"select rm.id, rm.xuh, mk.mingc meikxxb_id, kj.mingc jihkjb_id, rm.niancjcsl, rm.niancjcdj, rm.niancjcje, rm.caigsl, rm.yunj,\n" +
				"       rm.caigfrl, rm.caigje, rm.kuangj, rm.yunzf, rm.changnfy, rm.biaoml, rm.biaomdj, rm.rulsl, rm.ruldj,\n" + 
				"       rm.rulje, rm.rulzs, rm.rulfrl, rm.rulbml, rm.meizbmdj, rm.youzbmdj, rm.zonghbmdj, rm.rezc, rm.qithysl,\n" + 
				"       rm.qithydj, rm.qithyje, rm.qimjysl, rm.qimjydj, rm.qimjyje from yuecbrmb rm, meikxxb mk, jihkjb kj\n" + 
				" where rm.meikxxb_id = mk.id and rm.jihkjb_id = kj.id " +
				"   and rm.riq = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-1', 'yyyy-mm-dd')" +
				"order by rm.xuh";
		}
		
		ResultSetList rsl =  con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setFixed(true);
		if (visit.getString1().equals("ranm")) {
			egu.getColumn("xuh").setHeader("行号");
			egu.getColumn("xuh").setWidth(60);
			egu.getColumn("xuh").setFixed(true);
			egu.getColumn("meikxxb_id").setHeader("煤矿");
			egu.getColumn("meikxxb_id").setFixed(true);
			egu.getColumn("jihkjb_id").setHeader("计划口径");
			egu.getColumn("jihkjb_id").setFixed(true);
		}
		egu.getColumn("niancjcsl").setHeader("数量(吨)");
		egu.getColumn("niancjcsl").setFixed(true);
		egu.getColumn("niancjcdj").setHeader("单价(元/吨)");
		egu.getColumn("niancjcdj").setFixed(true);
		egu.getColumn("niancjcje").setHeader("金额(万元)");
		egu.getColumn("niancjcje").setFixed(true);
		egu.getColumn("caigsl").setHeader("数量(吨)");
		egu.getColumn("caigsl").setFixed(true);
		egu.getColumn("yunj").setHeader("运距(公里)");
		egu.getColumn("yunj").setFixed(true);
		egu.getColumn("caigfrl").setHeader("单位发热量(千焦/千克)");
		egu.getColumn("caigfrl").setFixed(true);
		egu.getColumn("caigje").setHeader("采购金额(万元)");
		egu.getColumn("caigje").setFixed(true);
		egu.getColumn("kuangj").setHeader("矿价");
		egu.getColumn("kuangj").setFixed(true);
		egu.getColumn("yunzf").setHeader("运杂费");
		egu.getColumn("yunzf").setFixed(true);
		egu.getColumn("changnfy").setHeader("厂内费用");
		egu.getColumn("changnfy").setFixed(true);
		egu.getColumn("biaoml").setHeader("标煤量(吨)");
		egu.getColumn("biaoml").setFixed(true);
		egu.getColumn("biaomdj").setHeader("标煤单价(元/吨)");
		egu.getColumn("biaomdj").setFixed(true);
		egu.getColumn("rulsl").setHeader("数量(吨)");
		egu.getColumn("rulsl").setFixed(true);
		egu.getColumn("ruldj").setHeader("单价(元/吨)");
		egu.getColumn("ruldj").setFixed(true);
		egu.getColumn("rulje").setHeader("金额(万元)");
		egu.getColumn("rulje").setFixed(true);
		egu.getColumn("rulzs").setHeader("贮损(吨)");
		egu.getColumn("rulzs").setFixed(true);
		egu.getColumn("rulfrl").setHeader("单位发热量(千焦/千克)");
		egu.getColumn("rulfrl").setFixed(true);
		egu.getColumn("rulbml").setHeader("标煤量(吨)");
		egu.getColumn("rulbml").setFixed(true);
		egu.getColumn("meizbmdj").setHeader("煤折标煤单价(元/吨)");
		egu.getColumn("youzbmdj").setHeader("油折标煤单价(元/吨)");
		egu.getColumn("meizbmdj").setFixed(true);
		egu.getColumn("zonghbmdj").setHeader("综合标煤单价(元/吨)");
		egu.getColumn("zonghbmdj").setFixed(true);
		egu.getColumn("rezc").setHeader("热值差(千焦/千克)");
		egu.getColumn("rezc").setFixed(true);
		egu.getColumn("qithysl").setHeader("数量(吨)");
		egu.getColumn("qithysl").setFixed(true);
		egu.getColumn("qithydj").setHeader("单价(元/吨)");
		egu.getColumn("qithydj").setFixed(true);
		egu.getColumn("qithyje").setHeader("金额(万元)");
		egu.getColumn("qithyje").setFixed(true);
		egu.getColumn("qimjysl").setHeader("数量(吨)");
		egu.getColumn("qimjysl").setFixed(true);
		egu.getColumn("qimjydj").setHeader("单价(元/吨)");
		egu.getColumn("qimjydj").setFixed(true);
		egu.getColumn("qimjyje").setHeader("金额(万元)");
		egu.getColumn("qimjyje").setFixed(true);
		
		if (visit.getString1().equals("ranm")) {
			egu.getColumn("meikxxb_id").setEditor(new ComboBox());
			egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id, mingc from meikxxb order by mingc"));
			
			egu.getColumn("jihkjb_id").setEditor(new ComboBox());
			egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id, mingc from jihkjb order by mingc"));
		}
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("年份：");
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(60);
		nf_comb.setTransform("Nianf");
		nf_comb.setId("Nianf");
		nf_comb.setLazyRender(true);
		nf_comb.setEditable(true);
		egu.addToolbarItem(nf_comb.getScript());
//		egu.addOtherScript("Nianf.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("月份：");
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(60);
		yf_comb.setTransform("Yuef");
		yf_comb.setId("Yuef");
		yf_comb.setLazyRender(true);
		yf_comb.setEditable(true);
		egu.addToolbarItem(yf_comb.getScript());
//		egu.addOtherScript("Yuef.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		
		if (visit.getString1().endsWith("ranm")) {
			String condition = 
				"var xuhMrcd = gridDiv_ds.getModifiedRecords();\n" +
				"for(var i = 0; i< xuhMrcd.length; i++){\n" + 
				"    if(xuhMrcd[i].get('XUH') == '' || xuhMrcd[i].get('XUH') == null){\n" + 
				"        Ext.MessageBox.alert('提示信息','字段 序号 不能为空');\n" + 
				"        return;\n" + 
				"    }\n" + 
				"}";
			egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton", condition);
		} else {
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		
		if (visit.getString1().equals("ranm")) {
			egu.addTbarText("-");
			GridButton createButton = new GridButton("生成", getButtonHandler(con, "CreateButton"), SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(createButton);
		}
		
		String ranm = "";
		if (visit.getString1().equals("ranm")) {
			ranm = 
			"    {header:'<table><tr><td width=40 align=center style=border:0>序号</td></tr></table>',align:'center', rowspan:2},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>煤矿</td></tr></table>',align:'center', rowspan:2},\n" + 
			"    {header:'<table><tr><td width=80 align=center style=border:0>计划口径</td></tr></table>',align:'center', rowspan:2},\n";
		}
		
		String headers = // 多行表头样式
			
			"[\n" +
			"    {header:'<table><tr><td width=5 align=center></td></tr></table>', align:'center', rowspan:2},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>ID</td></tr></table>',align:'center', rowspan:2},\n" + ranm +
			"    {header:'<table><tr><td width=240 align=center style=border:0>年初结存情况</td></tr></table>', align:'center', colspan:3},\n" + 
			"    {header:'<table><tr><td width=600 align=center style=border:0>采购情况(不含税)</td></tr></table>', align:'center', colspan:9},\n" +
			"    {header:'<table><tr><td width=600 align=center style=border:0>入炉情况</td></tr></table>', align:'center', colspan:9},\n" + 
			"    {header:'<table><tr><td width=80 align=center style=border:0>热值差</td></tr></table>', align:'center', rowspan:2},\n" +
			"    {header:'<table><tr><td width=240 align=center style=border:0>其它耗用</td></tr></table>', align:'center', colspan:3},\n" +
			"    {header:'<table><tr><td width=240 align=center style=border:0>期末结余</td></tr></table>', align:'center', colspan:3}\n" + 
			"],\n" + 
			"[\n" + 
			"    {header:'<table><tr><td width=80 align=center style=border:0>数量<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>金额<br>(万元)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>数量<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>运距<br>(公里)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>单位发热量<br>(千焦/千克)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>采购金额<br>(万元)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>矿价</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>运杂费</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>厂内费用</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>标煤量<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>标煤单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>数量<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>金额<br>(万元)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>贮损<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>单位发热量<br>(千焦/千克)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>标煤量<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>煤折标煤单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>油折标煤单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>标煤单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>数量<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>金额<br>(万元)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>数量<br>(吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>单价<br>(元/吨)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>金额<br>(万元)</td></tr></table>', align:'center'}\n" +
			"]";
		
		egu.setHeaders(headers);
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
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
	
	/**
	 * 返回传入日期的上个月份，返回的日期格式为"yyyy-mm"
	 * @param date
	 * @return
	 */
	public static String getLastMonth(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(date == null){
			date = sdf.format(new Date());
		}
		try {
			Date tempdate = sdf.parse(date);
			int month = DateUtil.getMonth(tempdate);
			int year =DateUtil.getYear(tempdate);
			if(month == 1){
				year -= 1;
				month = 12;
			}else{
				month -= 1;
			}
			return year+"-"+month;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 返回"复制同期"按钮的handler。当点击"复制同期"按钮时判断当前日期是否有数据，
	 * 如果有数据那么给出提示是否要覆盖原有数据，如果没有那么不给提示信息，
	 * 直接进行"复制同期"操作。
	 * @return
	 */
	public String getButtonHandler(JDBCcon con, String buttonName) {
		
		String handler = 
			"function (){\n" +
			"    document.getElementById('"+ buttonName +"').click();\n" + 
			"}";
		
		String sql = "select rm.id from yuecbrmb rm where to_date(to_char(rm.riq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')";
		ResultSetList rsl = con.getResultSetList(sql);
		
		if (rsl.next()) {
			handler = 
				"function (){\n" +
				"    Ext.MessageBox.confirm('提示信息','新数据将会覆盖原有数据，是否继续？',\n" + 
				"        function(btn){\n" + 
				"            if(btn == 'yes'){\n" + 
				"                document.getElementById('"+ buttonName +"').click();\n" + 
				"                Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...'," +
				"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"            };\n" + 
				"        }\n" + 
				"    );\n" + 
				"}";
		}
		rsl.close();
		return handler;
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

		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			visit.setString1(reportType);
			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // 月份下拉框
			visit.setDropDownBean3(null);
		}
		
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