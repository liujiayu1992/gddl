package com.zhiren.gs.bjdt.chengbgl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caigysb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void Save() {
		String tableName = "NIANCGYSB";
		Visit visit = (Visit) getPage().getVisit();

		JDBCcon con = new JDBCcon();

		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(
				getChange());
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(
				getChange());

		while (mdrsl.next()) {

			if ("0".equals(mdrsl.getString("ID"))) {

				sql.append("insert into ").append(tableName).append(
						"(id,diancxxb_id,gongysb_id,jihkjb_id,dinghl,yujdhl,");
				sql
						.append("caigl,daocrz,chukjg,yunj,zaf,daoczhjhs,daoczhjbhs,buhsbmdj,riq)");
				sql.append("values (getnewid(" + visit.getDiancxxb_id() + "),"
						+ getTreeid());
				sql.append(",").append(
						(getExtGrid().getColumn("gongysb_id").combo)
								.getBeanId(mdrsl.getString("gongysb_id")));
				sql.append(",").append(
						(getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id")))
						.append(",'")
						.append(mdrsl.getString("dinghl"));
				sql.append("','").append(mdrsl.getString("yujdhl")).append(
						"','").append(mdrsl.getString("caigl"));
				sql.append("','").append(mdrsl.getString("daocrz")).append(
						"','").append(mdrsl.getString("chukjg"));
				sql.append("','").append(mdrsl.getString("yunj"));
				sql.append("','").append(mdrsl.getString("zaf")).append("','")
						.append(mdrsl.getString("daoczhjhs"));
				sql.append("','").append(mdrsl.getString("daoczhjbhs")).append(
						"','").append(mdrsl.getString("buhsbmdj"));
				sql.append("',").append(
						"to_date(" + getNianfValue().getValue() + ",'yyyy')")
						.append(");");

			} else {

				sql.append("update ").append(visit.getExtGrid1().tableName)
						.append(" set ");
				sql.append("riq=").append(
						"to_date(" + getNianfValue().getValue() + ",'yyyy')");
				sql.append(",").append("gongysb_id=").append(
						(getExtGrid().getColumn("gongysb_id").combo)
								.getBeanId(mdrsl.getString("gongysb_id")));
				sql.append(",").append("jihkjb_id=").append(
							(getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id")));
				sql.append(",").append("dinghl='").append(
						mdrsl.getString("dinghl")).append("'");
				sql.append(",").append("yujdhl='").append(
						mdrsl.getString("yujdhl")).append("'");
				sql.append(",").append("caigl='").append(
						mdrsl.getString("caigl")).append("'");
				sql.append(",").append("daocrz='").append(
						mdrsl.getString("daocrz")).append("'");
				sql.append(",").append("chukjg='").append(
						mdrsl.getString("chukjg")).append("'");
				sql.append(",").append("yunj='")
						.append(mdrsl.getString("yunj")).append("'");
				sql.append(",").append("zaf='").append(mdrsl.getString("zaf"))
						.append("'");
				sql.append(",").append("daoczhjhs='").append(
						mdrsl.getString("daoczhjhs")).append("'");
				sql.append(",").append("daoczhjbhs='").append(
						mdrsl.getString("daoczhjbhs")).append("'");
				sql.append(",").append("buhsbmdj='").append(
						mdrsl.getString("buhsbmdj")).append("'");
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}

		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	//开始日期取值
	public String getKaisrq() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setKaisrq(String kaisrq) {
		((Visit) this.getPage().getVisit()).setString4(kaisrq);
	}
	
	public String getKaisrq1() {
		return ((Visit) this.getPage().getVisit()).getString9();
	}

	public void setKaisrq1(String kaisrq1) {
		((Visit) this.getPage().getVisit()).setString9(kaisrq1);
	}

	//截止日期

	public String getJiezrq() {
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setJiezrq(String jiezrq) {
		((Visit) this.getPage().getVisit()).setString5(jiezrq);
	}
	
	public String getJiezrq1() {
		return ((Visit) this.getPage().getVisit()).getString10();
	}

	public void setJiezrq1(String jiezrq1) {
		((Visit) this.getPage().getVisit()).setString10(jiezrq1);
	}

	//月份
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString8());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString8();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString8(value);
	}

	public void setRiq1() {
		setKaisrq(this.getKaisrqValue().getValue());
		setYuef(this.getYuefValue().getValue());
	}

	public String getYuefs() {
		int intYuefs = Integer.parseInt(((Visit) getPage().getVisit())
				.getString6());
		if (intYuefs < 10) {
			return "0" + intYuefs;
		} else {
			return ((Visit) getPage().getVisit()).getString6();
		}
	}

	public void setYuefs(String value) {
		((Visit) getPage().getVisit()).setString6(value);
	}

	public void setRiq2() {
		setJiezrq(this.getJiezrqValue().getValue());
		setYuefs(this.getYuefsValue().getValue());
	}

	
	public String getYuef1() {
		int intYuef1 = Integer.parseInt(((Visit) getPage().getVisit())
				.getString11());
		if (intYuef1 < 10) {
			return "0" + intYuef1;
		} else {
			return ((Visit) getPage().getVisit()).getString11();
		}
	}

	public void setYuef1(String value) {
		((Visit) getPage().getVisit()).setString11(value);
	}
	
	public String getYuefs1() {
		int intYuefs1 = Integer.parseInt(((Visit) getPage().getVisit())
				.getString12());
		if (intYuefs1 < 10) {
			return "0" + intYuefs1;
		} else {
			return ((Visit) getPage().getVisit()).getString12();
		}
	}

	public void setYuefs1(String value) {
		((Visit) getPage().getVisit()).setString12(value);
	}
	
	public void setRiq3() {
		setJiezrq1(this.getJiezrq1Value().getValue());
		setYuefs1(this.getYuefs1Value().getValue());
		setKaisrq1(this.getKaisrq1Value().getValue());
		setYuef1(this.getYuef1Value().getValue());
		
	}
	
	public String getGongys() {
		return ((Visit) this.getPage().getVisit()).getString7();
	}

	public void setGongys(String gongys) {
		((Visit) this.getPage().getVisit()).setString7(gongys);
	}

	//	public void setgys() {
	//		setGongys(this.getGongysValue().getId());
	//	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYearData();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
	}

	public void CoypLastYearData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		intyear = intyear - 1;
		//供应商条件

		//
		String strdiancTreeID = "";

		int jib = this.getDiancTreeJib();
		if (jib == 1) {//选集团时刷新出所有的电厂
			strdiancTreeID = "";
		} else if (jib == 2) {//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID = " and d.fuid= " + this.getTreeid();

		} else if (jib == 3) {//选电厂只刷新出该电厂
			strdiancTreeID = " and d.id= " + this.getTreeid();

		}

		String copyData = "select n.*\n"
				+ "  from niancgjhb n, diancxxb d,gongysb g\n"
				+ " where n.diancxxb_id = d.id(+)\n"
				+ "   and n.gongysb_id=g.id(+)\n" + "   " + strdiancTreeID
				+ "\n"

				+ "   and to_char(n.riq, 'yyyy') = '" + intyear + "'";

		//System.out.println("复制去年的数据:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData);
		while (rslcopy.next()) {

			long gongysb_id = rslcopy.getLong("gongysb_id");
			long diancxxb_id = rslcopy.getLong("diancxxb_id");
			long hej = rslcopy.getLong("hej");
			Date riq = rslcopy.getDate("riq");
			int year = DateUtil.getYear(riq);
			int yue = DateUtil.getMonth(riq);
			int day = DateUtil.getDay(riq);

			String strriq = year + 1 + "-" + yue + "-" + day;
			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			con
					.getInsert("insert into niancgjhb(id,gongysb_id,diancxxb_id,hej,riq) values("
							+ _id
							+ ","
							+ gongysb_id
							+ ","
							+ diancxxb_id
							+ ","
							+ hej
							+ ","
							+ "to_date('"
							+ strriq
							+ "','yyyy-mm-dd'))");

		}

		con.Close();

	}

	public void CreateData() {
		//		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		//getShoumlDy();

		String sql = "";
		String jih="";
        String jiag="";
        String jiag_d8="";
        String riq1="";
        String riq2="";
        String yusht="";
        String yunf="";
        String zaf="";
        String qitfy="";
		String gyscount = "select cm.id,cm.jihkjb_id,cm.diancxxb_id,cm.gongysb_id from caigysmxb cm"
				+ " where riq=to_date('" + getNianfValue().getValue()
				+ "' ,'yyyy') ";
		ResultSetList rs = con.getResultSetList(gyscount);

		
		
		StringBuffer sb = new StringBuffer();

		sb.append("delete from niancgysb where riq=to_date("+getNianfValue().getValue()
				+ ",'yyyy')");
		con.getDelete(sb.toString());
		while (rs.next()) {
			sb.delete(0, sb.length());
			jih=rs.getString("jihkjb_id");
			if(jih=="1"||jih.equals("1")){
				
				jiag_d8="round(sum(d8.meil * d8.rez)  /\n"+
				 "decode(sum(d8.meil), 0, 1, sum(d8.meil)),3)jiag,\n";
				jiag="(case when c.jiag>10 then round(c.jiag/(c.farl*0.0041816)*dr.rez,2) else " +
						"round(c.jiag*dr.rez/0.0041816 ,2)end)jiag,\n";
				riq1=" riq>=to_date('"+this.getKaisrq()+"-"+this.getYuef()+"','yyyy-mm')\n";
				riq2="and riq<=to_date('"+this.getJiezrq()+"-"+this.getYuefs()+"','yyyy-mm')and \n";
				yusht="and c.id = cm.yushtb_id\n";
				yunf="   round((sum(d8.tielyf)+sum(d8.shuiyf)+sum(d8.qiyf))/decode((("+getJiezrq()+"-"+getKaisrq()+")*12+"+getYuefs()+"-"+getYuef()+"+1),0,1," 
				      +" (("+getJiezrq()+"-"+getKaisrq()+")*12+"+getYuefs()+"-"+getYuef()+"+1)),2) yunf,";
				zaf="round((sum(d8.tielzf)+sum(d8.shuiyzf)+sum(d8.gangzf)+sum(d8.daozzf))/decode((("+getJiezrq()+"-"+getKaisrq()+")*12+"+getYuefs()+"-"+getYuef()+"+1),0,1," 
					  +" (("+getJiezrq()+"-"+getKaisrq()+")*12+"+getYuefs()+"-"+getYuef()+"+1)),2) zaf,";
				qitfy="round((sum(d8.qitfy))/decode((("+getJiezrq()+"-"+getKaisrq()+")*12+"+getYuefs()+"-"+getYuef()+"+1),0,1," 
				  +" (("+getJiezrq()+"-"+getKaisrq()+")*12+"+getYuefs()+"-"+getYuef()+"+1)),2) qitfy";

			}else if(jih=="2"||jih.equals("2")){
				jiag_d8="round( sum(d8.kuangj)/decode((("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1),0,1," +
						"(("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1)),2) jiag,";
				jiag="dr.jiag,";
				riq1="riq>=to_date('"+this.getKaisrq1()+"-"+this.getYuef1()+"','yyyy-mm')\n";
				riq2="and riq<=to_date('"+this.getJiezrq1()+"-"+this.getYuefs1()+"','yyyy-mm') and\n";
				yusht="";
				yunf="   round((sum(d8.tielyf)+sum(d8.shuiyf)+sum(d8.qiyf))/decode((("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1),0,1," 
			      +" (("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1)),2) yunf,";
			    zaf="round((sum(d8.tielzf)+sum(d8.shuiyzf)+sum(d8.gangzf)+sum(d8.daozzf))/decode((("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1),0,1," 
				  +" (("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1)),2) zaf,";
			    qitfy="round((sum(d8.qitfy))/decode((("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1),0,1," 
			      +" (("+getJiezrq1()+"-"+getKaisrq1()+")*12+"+getYuefs1()+"-"+getYuef1()+"+1)),2) qitfy";

			}
			sb.append("select distinct\n"
							+ "       cm.diancxxb_id,\n"
							+ "       cm.gongysb_id,\n"
							+ "       cm.jihkjb_id,\n"
							+ "       cm.hetl,\n"
							+ "       dr.rez,\n"
							//+ "   round(c.jiag/(c.farl*0.0041816)*dr.jiag,2)jiag,\n"
							+jiag
							+ "   dr.yunf,dr.zaf,dr.qitfy\n"
							+ "\n"
							+ "  from caigysb c, caigysmxb cm, diancxxb d, gongysb g\n"
							+ "  ,(select  round(sum(d8.meil * d8.rez) /\n"
							+ "                     decode(sum(d8.meil), 0, 1, sum(d8.meil)),\n"
							+ "                     3) rez,\n"
//							+ "          round(sum(d8.meil * d8.rez)  /\n"
//							+ "                     decode(sum(d8.meil), 0, 1, sum(d8.meil)),3)jiag,\n"
							+jiag_d8
							+yunf
							+zaf
							+qitfy
//							+ "          (sum(d8.tielyf)+sum(d8.shuiyf)+sum(d8.qiyf))yunf,\n"
//							+"   round((sum(d8.tielyf)+sum(d8.shuiyf)+sum(d8.qiyf))/decode(("+getJiezrq1()+"-"+getKaisrq1()+"*12+"+getYuefs()+"-"+getYuef()+"+1),0,1," 
//							+"   ("+getJiezrq()+"-"+getKaisrq()+"*12+"+getYuefs()+"-"+getYuef()+"+1)),2) yunf,"
//							+ "          (sum(d8.tielzf)+sum(d8.shuiyzf)+sum(d8.gangzf)+sum(d8.daozzf))zaf,\n"
//							+"   round((sum(d8.tielzf)+sum(d8.shuiyzf)+sum(d8.gangzf)+sum(d8.daozzf))/decode(("+getJiezrq1()+"-"+getKaisrq1()+"*12+"+getYuefs()+"-"+getYuef()+"+1),0,1," 
//							+"   ("+getJiezrq()+"-"+getKaisrq()+"*12+"+getYuefs()+"-"+getYuef()+"+1)),2) zaf,"
//							+ "          sum(d8.qitfy)qitfy\n"
//							+"   round((sum(d8.qitfy))/decode(("+getJiezrq1()+"-"+getKaisrq1()+"*12+"+getYuefs()+"-"+getYuef()+"+1),0,1," 
//							+"   ("+getJiezrq()+"-"+getKaisrq()+"*12+"+getYuefs()+"-"+getYuef()+"+1)),2) qitfy"
							+ "          from diaor08bb d8\n"
							+ "          where " 
							+		     riq1
							+            riq2
							+ "          d8.gongysb_id="
							+ rs.getString("gongysb_id") + "\n"
							+ "           and d8.diancxxb_id="
							+ rs.getString("diancxxb_id") + "\n"
							+ "           and d8.fenx='本月'   )dr\n"
							+ "   where\n" + "   d.id = cm.diancxxb_id\n"
							+ "   and g.id = cm.gongysb_id\n"
							+ "  and cm.gongysb_id="
							+ rs.getString("gongysb_id") + "\n"
							//+ "   and c.id = cm.yushtb_id\n"
							+yusht
							+ "   and cm.diancxxb_id="
							+ rs.getString("diancxxb_id"));
			ResultSetList rsl = con.getResultSetList(sb.toString());
			while (rsl.next()) {

				sb.delete(0, sb.length());
				sb.append("insert into NIANCGYSB(id,riq,diancxxb_id,gongysb_id,jihkjb_id,caigl,daocrz,chukjg,yunj,zaf,qitfy) values(\n")
						.append("getnewid(").append(diancxxb_id).append("),")
						.append(
								"to_date(" + getNianfValue().getValue()
										+ ",'yyyy')").append(",").append(
								rsl.getLong("diancxxb_id")).append(",").append(
								rsl.getLong("gongysb_id")).append(",").append(
								rsl.getLong("jihkjb_id")).append(",'").append(
								rsl.getString("hetl")).append("','").append(
								rsl.getString("rez")).append("','").append(
								rsl.getString("jiag")).append("','").append(
								rsl.getString("yunf")).append("','").append(
								rsl.getString("zaf")).append("','").append(
								rsl.getString("qitfy")).append("')");
				con.getInsert(sb.toString());

			}

		}

		//		if(this.getJih().equals("1")){
		//			sql="round(((c.jiag/(c.farl*0.0041816)) *\n" +
		//			"          (select round(sum(d8.meil * d8.rez)  /\n" + 
		//			"          decode(sum(d8.meil), 0, 1, sum(d8.meil)),3)\n" + 
		//			"          from diaor08bb d8\n" + 
		//			"           where riq >= to_date('"+getKaisrq()+"-"+getYuef()+"', 'yyyy-mm')\n" + 
		//			"           and riq <= to_date('"+getJiezrq()+"-"+getYuefs()+"', 'yyyy-mm')\n" + 
		//			"           and d8.gongysb_id= "+MainGlobal.getProperId(getGongysModel(), this.getGongys())+"\n"+
		//			//"           and d8.id = g.id\n" + 
		//			//"           and d8.diancxxb_id = d.id" +
		//			"           and d8.diancxxb_id="+getTreeid()+
		//			"           and d8.fenx='本月')),2) as jiag,";
		//			gys="and c.id = cm.yushtb_id";
		//		}else if(this.getJih().equals("2")){
		//			sql=
		//				"round((select sum(d8.kuangj)/decode(("+getYuefs()+"-"+getYuef()+"+1),0,1,("+getYuefs()+"-"+getYuef()+"+1)) from diaor08bb d8" +
		//				" where riq between to_date('"+getKaisrq()+"-"+getYuef()+"','yyyy-mm')and\n" +
		//				"to_date('"+getJiezrq()+"-"+getYuefs()+"','yyyy-mm')" +
		//				" and d8.gongysb_id= "+MainGlobal.getProperId(getGongysModel(), this.getGongys())+"\n"+
		//				" and d8.diancxxb_id="+getTreeid()+
		//				"and d8.fenx='本月'"+
		//				"),2)as jiag,";
		//			gys="and cm.gongysb_id="+MainGlobal.getProperId(getGongysModel(), this.getGongys());
		//		}
		//				
		//		StringBuffer sb = new StringBuffer();
		//		sb.append(
		//
		//				"select distinct \n" +
		//				"       cm.diancxxb_id,\n" + 
		//				"       cm.gongysb_id,\n" + 
		//				"       cm.jihkjb_id, "+
		//				"       cm.hetl,\n" + 
		//				"       (select round(sum(d8.meil * d8.rez) /\n" + 
		//				"                     decode(sum(d8.meil), 0, 1, sum(d8.meil)),\n" + 
		//				"                     3)\n" + 
		//				"          from diaor08bb d8\n" + 
		//				"         where riq >= to_date('"+getKaisrq()+"-"+getYuef()+"', 'yyyy-mm')\n" + 
		//				"           and riq <= to_date('"+getJiezrq()+"-"+getYuefs()+"', 'yyyy-mm')\n" + 
		//				"           and d8.gongysb_id= "+MainGlobal.getProperId(getGongysModel(), this.getGongys())+"\n"+
		//				"           and d8.diancxxb_id="+getTreeid() + 
		//				"           and d8.fenx='本月')rez, \n"+sql+
		////				"           ((c.jiag / c.farl / 4.1816 * 1000) *\n" + 
		////				"           (select round(sum(d8.meil * d8.rez) * 1000 /\n" + 
		////				"           decode(sum(d8.meil), 0, 1, sum(d8.meil)),3)\n" + 
		////				"           from diaor08bb d8\n" + 
		////				"            where riq >= to_date('2009-4', 'yyyy-mm')\n" + 
		////				"            and riq <= to_date('2009-5', 'yyyy-mm')\n" + 
		////				"            and d8.id = g.id\n" + 
		////				"            and d8.diancxxb_id = d.id)) as jiag,\n" + 
		//				"            (select (sum(d8.tielyf)+sum(d8.shuiyf)+sum(d8.qiyf))\n" + 
		//				"            from diaor08bb d8\n" + 
		//				"            where riq <= to_date('"+getKaisrq()+"-"+getYuef()+"', 'yyyy-mm')\n" + 
		//				"            and riq >= to_date('"+getJiezrq()+"-"+getYuefs()+"', 'yyyy-mm')\n" + 
		//				"            and d8.gongysb_id= "+MainGlobal.getProperId(getGongysModel(), this.getGongys())+"\n"+ 
		//				"             and d8.diancxxb_id="+getTreeid() +"and d8.fenx='本月')yunf,\n" + 
		//				"            (select (sum(d8.tielzf)+sum(d8.shuiyzf)+sum(d8.gangzf)+sum(d8.daozzf))\n" + 
		//				"            from diaor08bb d8\n" + 
		//				"            where riq <= to_date('"+getKaisrq()+"-"+getYuef()+"', 'yyyy-mm')\n" + 
		//				"            and riq >= to_date('"+getJiezrq()+"-"+getYuefs()+"', 'yyyy-mm')\n" + 
		//				"            and d8.gongysb_id= "+MainGlobal.getProperId(getGongysModel(), this.getGongys())+"\n"+ 
		//				"            and d8.diancxxb_id="+getTreeid()+"and d8.fenx='本月')zaf,\n" + 
		//				"            (select sum(d8.qitfy)\n" + 
		//				"            from diaor08bb d8\n" + 
		//				"            where riq <= to_date('"+getKaisrq()+"-"+getYuef()+"', 'yyyy-mm')\n" + 
		//				"            and riq >= to_date('"+getJiezrq()+"-"+getYuefs()+"', 'yyyy-mm')\n" + 
		//				"            and d8.gongysb_id= "+MainGlobal.getProperId(getGongysModel(), this.getGongys())+"\n"+ 
		//				"            and d8.diancxxb_id="+getTreeid() +"and d8.fenx='本月')qitfy\n" + 
		//				"  from caigysb c, caigysmxb cm, diancxxb d, gongysb g\n" + 
		//				"   where \n" + 
		//				"   d.id = cm.diancxxb_id\n" + 
		//				"   and g.id = cm.gongysb_id\n"+
		//				    gys+
		//				"   and cm.diancxxb_id="+getTreeid());
		//		ResultSetList rsl = con.getResultSetList(sb.toString());
		//		while(rsl.next()) {
		//			
		//				sb.delete(0, sb.length());
		//				sb.append("insert into NIANCGYSB(id,riq,diancxxb_id,gongysb_id,jihkjb_id,yujdhl,daocrz,chukjg,yunj,zaf,qitfy) values(\n")
		//				.append("getnewid(").append(diancxxb_id).append("),").append("to_date("+getNianfValue().getValue()+",'yyyy')").append(",")
		//				.append(rsl.getLong("diancxxb_id")).append(",").append(rsl.getLong("gongysb_id")).append(",")
		//				.append(rsl.getLong("jihkjb_id")).append(",'").append(rsl.getString("hetl")).append("','").append(rsl.getString("rez")).append("','")
		//				.append(rsl.getString("jiag")).append("','").append(rsl.getString("yunf")).append("','").append(rsl.getString("zaf")).append("','")
		//				.append(rsl.getString("qitfy"))
		//				.append("')");
		//				con.getInsert(sb.toString());
		//			
		//		}
		//		
		con.commit();
		con.Close();
		setMsg( "数据成功生成!");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gongysdrop = "";
		String kaisrq = "";
		String jiezrq = "";
		String yuef = "";
		String yuefs = "";
		String kaisrq1 = "";
		String jiezrq1 = "";
		String yuef1 = "";
		String yuefs1 = "";

		kaisrq = "\n" + "{items:Kaisrq=new Ext.form.ComboBox({ \n"
				+ "width:70,\n" + "fieldLabel: '合同煤起始年份',\n"
				+ "selectOnFocus:true,\n" 
				+ "transform:'KaisrqDropDown',\n"
				+ "lazyRender:true,\n"
				+ "triggerAction:'all',\n"
				+ "typeAhead:true,\n"
				+ "forceSelection:true,\n"
				+ "editable:false \n" +
				"})}";
		//changb_value = "document.getElementById('TEXT_KAISRQ_VALUE').value=Kaisrq.getRawValue()";
		kaisrq1 = ",\n" + "{items:Kaisrq1=new Ext.form.ComboBox({ \n"
				+ "width:70,\n" + "fieldLabel: '市场煤起始年份',\n"
				+ "selectOnFocus:true,\n"
				+ "transform:'Kaisrq1DropDown',\n"
				+ "lazyRender:true,\n" 
				+ "triggerAction:'all',\n"
				+ "typeAhead:true,\n"
				+ "forceSelection:true,\n"
				+ "editable:false \n" +
				"})}";
		
		jiezrq =
				 " ,\n"
				+ "	\t{items:Jiezrq=new Ext.form.ComboBox({	\n"
				+ "	\twidth:70,	\n"
				+ " \tfieldLabel: '合同煤截止年份',\n"
				+ " \tselectOnFocus:true,	\n"
				+ " \ttransform:'JiezrqDropDown',	\n"
				+ " \tlazyRender:true,	\n"
				+ "	\ttriggerAction:'all',	\n"
				+ " \ttypeAhead:true,	\n"
				+ " \tforceSelection:true,	\n"
				+ " \teditable:false,	\n"
				+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('JiezrqDropDown').selectedIndex=index}}"
				+ "	\t})}";
		//qued = "document.getElementById('TEXT_JIEZRQ_VALUE').value=Jiezrq.getRawValue();";
		jiezrq1 =
			 " ,\n"
			+ "	\t{items:Jiezrq1=new Ext.form.ComboBox({	\n"
			+ "	\twidth:70,	\n"
			+ " \tfieldLabel: '市场煤截止年份',\n"
			+ " \tselectOnFocus:true,	\n"
			+ " \ttransform:'Jiezrq1DropDown',	\n"
			+ " \tlazyRender:true,	\n"
			+ "	\ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false,	\n"
			+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Jiezrq1DropDown').selectedIndex=index}}"
			+ "	\t})}";
		
		
		yuef = " ,	{\n"
				+ "	\titems:Yuef=new Ext.form.ComboBox({	\n"
				+ "	\twidth:50,	\n"
				+ " \tfieldLabel: '合同煤起始月份',\n"
				+ " \tselectOnFocus:true,	\n"
				+ " \ttransform:'YuefDropDown',	\n"
				+ " \tlazyRender:true,	\n"
				+ "	\ttriggerAction:'all',	\n"
				+ " \ttypeAhead:true,	\n"
				+ " \tforceSelection:true,	\n"
				+ " \teditable:false,	\n"
				+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YuefDropDown').selectedIndex=index}}"
				+ "	\t})}";
		
		yuef1 = " ,	{\n"
			+ "	\titems:Yuef1=new Ext.form.ComboBox({	\n"
			+ "	\twidth:50,	\n"
			+ " \tfieldLabel: '市场煤起始月份',\n"
			+ " \tselectOnFocus:true,	\n"
			+ " \ttransform:'Yuef1DropDown',	\n"
			+ " \tlazyRender:true,	\n"
			+ "	\ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false,	\n"
			+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Yuef1DropDown').selectedIndex=index}}"
			+ "	\t})}";

		yuefs = " ,	{\n"
				+ "	\titems:Yuefs=new Ext.form.ComboBox({	\n"
				+ "	\twidth:50,	\n"
				+ " \tfieldLabel: '合同煤截止月份',\n"
				+ " \tselectOnFocus:true,	\n"
				+ " \ttransform:'YuefsDropDown',	\n"
				+ " \tlazyRender:true,	\n"
				+ "	\ttriggerAction:'all',	\n"
				+ " \ttypeAhead:true,	\n"
				+ " \tforceSelection:true,	\n"
				+ " \teditable:false,	\n"
				+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YuefsDropDown').selectedIndex=index}}"
				+ "	\t})}";
		yuefs1 = " ,{\n"
			+ "	\titems:Yuefs1=new Ext.form.ComboBox({	\n"
			+ "	\twidth:50,	\n"
			+ " \tfieldLabel: '市场煤截止月份',\n"
			+ " \tselectOnFocus:true,	\n"
			+ " \ttransform:'Yuefs1DropDown',	\n"
			+ " \tlazyRender:true,	\n"
			+ "	\ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false,	\n"
			+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Yuefs1DropDown').selectedIndex=index}}"
			+ "	\t})}";
//		gongysdrop = " ,{	\n"
//				+ "	\titems:Gongys=new Ext.form.ComboBox({	\n"
//				+ "	\twidth:110,	\n"
//				+ " \tfieldLabel: '供应商',\n"
//				+ " \tselectOnFocus:true,	\n"
//				+ " \ttransform:'GongysDropDown',	\n"
//				+ " \tlazyRender:true,	\n"
//				+ "	\ttriggerAction:'all',	\n"
//				+ " \ttypeAhead:true,	\n"
//				+ " \tforceSelection:true,	\n"
//				+ " \teditable:true,	\n"
//				+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('GongysDropDown').selectedIndex=index}}"
//				+ "	\t})}";

		String Strtmpfunction = "var form = new Ext.form.FormPanel({ "+
			"\tlabelAlign:'left',buttonAlign:'right',bodyStyle:'padding:5px;',\n" + 
	    	"frame:true,labelWidth:115,monitorValid:true,\n" 
				+"    items:[{\n"  
				+" layout:'column',border:false,labelSeparator:':',\n"  
		    	+" defaults:{layout: 'form',border:false,columnWidth:.5},\n" 
				+ "items: [ \n"
				+ kaisrq
				+"\n"
				+kaisrq1
				+"\n"
				+ yuef
				+"\n"
				+yuef1
				+ "\n"
				+ jiezrq
				+"\n"
				+jiezrq1
				+"\n"
				+ yuefs
				+"\n"
				+yuefs1
				+ "\n"
				+ "]\n"
				+" }]\n"
				+ " });\n"

				+ " win = new Ext.Window({\n"
				+ " el:'hello-win',\n"
				+ "layout:'fit',\n"
				+ "width:500,\n"
				+ "height:300,\n"
				+ "closeAction:'hide',\n"
				+ " plain: true,\n"
				+ "title:'条件',\n"
				+ "items: [form],\n"

				+ "buttons: [{\n"
				+ "    text:'确定',\n"
				+ "   handler:function(){  \n"
				+ "  		win.hide();\n"
				+"document.getElementById('TEXT_KAISRQ_VALUE').value=Kaisrq.getRawValue();"
				+ "\n"
				+ "document.getElementById('TEXT_JIEZRQ_VALUE').value=Jiezrq.getRawValue();"
				+ "\n"
				//+ "document.getElementById('TEXT_GONGYS_VALUE').value=Gongys.getRawValue();\n"
				+ "     document.getElementById('TEXT_YUEF_VALUE').value=Yuef.getRawValue();	\n"
				+ "     document.getElementById('TEXT_YUEFS_VALUE').value=Yuefs.getRawValue();	\n"
				+ "     document.getElementById('TEXT_KAISRQ1_VALUE').value=Kaisrq1.getRawValue();"
				+"      document.getElementById('TEXT_JIEZRQ1_VALUE').value=Jiezrq1.getRawValue();"
				+ "     document.getElementById('TEXT_YUEF1_VALUE').value=Yuef1.getRawValue();	\n"
				+ "     document.getElementById('TEXT_YUEFS1_VALUE').value=Yuefs1.getRawValue();	\n"
				+ " 	document.getElementById('CreateButton').click(); \n"
				+ "  	}   \n"
				+ "},{\n"
				+ "   text: '取消',\n"
				+ "   handler: function(){\n"
				+ "       win.hide();\n"
				+ "		document.getElementById('TEXT_KAISRQ_VALUE').value='';	\n"
				+ "		document.getElementById('TEXT_JIEZRQ_VALUE').value='';	\n"
				+ "     document.getElementById('TEXT_YUEF_VALUE').value='';	\n"
				+ "     document.getElementById('TEXT_YUEFS_VALUE').value='';	\n"
				+ "		document.getElementById('TEXT_KAISRQ1_VALUE').value='';	\n"
				+ "		document.getElementById('TEXT_JIEZRQ1_VALUE').value='';	\n"
				+ "     document.getElementById('TEXT_YUEF1_VALUE').value='';	\n"
				+ "     document.getElementById('TEXT_YUEFS1_VALUE').value='';	\n"
				//+ "     document.getElementById('TEXT_GONGYS_VALUE').value='';	\n"

				+ "   }\n" + "}]\n"

				+ " });";
		String jihkj = "";
		if (getJihkjValue().getValue() == "全部"
				|| getJihkjValue().getValue().equals("全部")) {
			jihkj = "";
		} else {
			jihkj = "and n.jihkjb_id=" + getJihkjValue().getId();
		}

		// 工具栏的年份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		String strdiancTreeID = "";

		int jib = this.getDiancTreeJib();
		if (jib == 1) {//选集团时刷新出所有的电厂
			strdiancTreeID = "";
		} else if (jib == 2) {//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID = "";

		} else if (jib == 3) {//选电厂只刷新出该电厂
			strdiancTreeID = " and dc.id= " + this.getTreeid();

		}
		String chaxun = "select n.id,dc.mingc as diancxxb_id,g.mingc as gongysb_id,j.mingc jihkjb_id,n.dinghl,n.yujdhl,"
				+ "n.caigl,round((n.daocrz),2) daocrz,n.chukjg,n.yunj,n.zaf,n.qitfy,n.daoczhjhs,n.daoczhjbhs,n.buhsbmdj,to_char(n.riq,'yyyy') riq"
				+ " from NIANCGYSB n,diancxxb dc,gongysb g,jihkjb j where n.diancxxb_id=dc.id and "
				+ "n.gongysb_id=g.id and j.id=n.jihkjb_id "
				+ strdiancTreeID
				+ " "
				+ jihkj
				+ " "
				+ "and n.riq=to_date("
				+ getNianfValue().getValue()
				+ ", 'yyyy')" + "order by dc.mingc,g.mingc";

		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("NIANCGYSB");
		egu.setWidth("bodyWidth");
		//egu.getColumn("jihkjb_id").setHidden(true);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(60);
		ComboBox jih = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(jih);
		String jihSql = "select id ,mingc  from jihkjb   order by id ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihSql));
		
		
		egu.getColumn("jihkjb_id").editor.allowBlank = true;
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		ComboBox gongys = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(gongys);
		String gongysSql = "select id ,mingc  from gongysb where fuid=0  order by xuh ";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gongysSql));
		egu.getColumn("gongysb_id").editor.allowBlank = true;
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("dinghl").setHeader("订货煤量");
		egu.getColumn("dinghl").setWidth(60);
		egu.getColumn("dinghl").setHidden(true);
		
		egu.getColumn("yujdhl").setHeader("预计到货量");
		egu.getColumn("yujdhl").setWidth(60);
		egu.getColumn("yujdhl").setHidden(true);
		
		egu.getColumn("caigl").setHeader("采购量(万吨)");
		egu.getColumn("caigl").setWidth(90);
		
		egu.getColumn("daocrz").setHeader("到厂热值(MJ/Kg)");
		egu.getColumn("daocrz").setWidth(95);
		egu.getColumn("daocrz").setDefaultValue("0.00");
		((NumberField)egu.getColumn("daocrz").editor).setDecimalPrecision(2);
		
		egu.getColumn("chukjg").setHeader("出矿价格");
		egu.getColumn("chukjg").setWidth(60);
		egu.getColumn("yunj").setHeader("运费");
		egu.getColumn("yunj").setWidth(60);

		egu.getColumn("zaf").setHeader("杂费");
		egu.getColumn("zaf").setWidth(60);
		egu.getColumn("qitfy").setHeader("其他费用");
		egu.getColumn("qitfy").setWidth(60);
		
		egu.getColumn("daoczhjhs").setHeader("到厂综价(含税)");
		egu.getColumn("daoczhjhs").setWidth(90);

		egu.getColumn("daoczhjbhs").setHeader("到厂综价(不含税)");
		egu.getColumn("daoczhjbhs").setWidth(100);
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		egu.getColumn("buhsbmdj").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页

		// *****************************************设置默认值****************************

		ResultSetList r = con
				.getResultSetList("select id,mingc from diancxxb where id="
						+ getTreeid() + " order by mingc");
		String mingc = "";
		if (r.next()) {
			mingc = r.getString("mingc");
		}
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("计划口径");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(150);
		comb2.setTransform("JihkjDropDown");
		comb2.setId("Jihkj");
		comb2.setLazyRender(true);
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		// 设置树

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		// 设定工具栏下拉框自动刷新
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu
				.addOtherScript("Jihkj.on('select',function(){document.forms[0].submit();});");
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu
				.addToolbarItem("{"
						+ new GridButton(
								"生成",
								"function(){ if(win){ win.show(this);"
										+ "}"
										//	+ " win.show(this);	\n" 
										+ " \tif(document.getElementById('TEXT_KAISRQ_VALUE').value!=''){	\n"
										+ "		\tKaisrq.setRawValue(document.getElementById('TEXT_KAISRQ_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_JIEZRQ_VALUE').value!=''){	\n"
										+ "		\tJiezrq.setRawValue(document.getElementById('TEXT_JIEZRQ_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_YUEF_VALUE').value!=''){	\n"
										+ "		\tYuef.setRawValue(document.getElementById('TEXT_YUEF_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_YUEFS_VALUE').value!=''){	\n"
										+ "		\tYuefs.setRawValue(document.getElementById('TEXT_YUEFS_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_KAISRQ1_VALUE').value!=''){	\n"
										+ "		\tKaisrq1.setRawValue(document.getElementById('TEXT_KAISRQ1_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_JIEZRQ1_VALUE').value!=''){	\n"
										+ "		\tJiezrq1.setRawValue(document.getElementById('TEXT_JIEZRQ1_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_YUEF1_VALUE').value!=''){	\n"
										+ "		\tYuef1.setRawValue(document.getElementById('TEXT_YUEF1_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_YUEFS1_VALUE').value!=''){	\n"
										+ "		\tYuefs1.setRawValue(document.getElementById('TEXT_YUEFS1_VALUE').value);	\n"
//										+ " \tif(document.getElementById('TEXT_GONGYS_VALUE').value!=''){	\n"
//										+ "		\tGongys.setRawValue(document.getElementById('TEXT_GONGYS_VALUE').value);	\n"
										+ "	\t}	\n" + "}").getScript() + "}");
				GridButton gbct = new GridButton("计算"," function(){ " +


						"var rows=gridDiv_ds.getTotalCount();"+
						"for (var i=0;i<rows;i++){"+
							"var rec1=gridDiv_ds.getAt(i);"+

							"var DAOCRZ=rec1.get('DAOCRZ');"+
							
							"var CHUKJG=rec1.get('CHUKJG');"+
							
							"var YUNJ=rec1.get('YUNJ');"+
							
							"var ZAF=rec1.get('ZAF');"+
							
							"var QITFY=rec1.get('QITFY');"+
							

							//"var DAOCZHJBHS = rec1.get('DAOCZHJBHS');"+

							"rec1.set('DAOCZHJHS',Round(eval(CHUKJG||0)+eval(YUNJ||0)+eval(ZAF||0)+eval(QITFY||0),2));"+
							
							"rec1.set('DAOCZHJBHS',Round(eval(CHUKJG||0)/1.17+eval(YUNJ||0)*0.93+eval(ZAF||0)+eval(QITFY||0),2));"+

							"if(DAOCRZ==''){rec1.set('BUHSBMDJ',0)}else if(DAOCRZ==0){rec1.set('BUHSBMDJ',0)}else" +
							"{rec1.set('BUHSBMDJ',Round(eval(rec1.get('DAOCZHJBHS')||0)*29.271/eval(DAOCRZ||0),2));}}"+

						"}"
		
		       );
				gbct.setIcon(SysConstant.Btn_Icon_Count);
				egu.addTbarBtn(gbct);
		egu.addTbarText("-");

		//---------------页面js的计算开始------------------------------------------
		//		StringBuffer sb = new StringBuffer();
		//		sb.append("gridDiv_grid.on('afteredit',function(e){");
		//			sb.append("e.record.set('DAOCJ',Round(parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2));\n"
		//					+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(eval(Round((parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))/1.17+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))*(1-0.07)+eval(e.record.get('ZAF')||0))*29.271/parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2)||0),2));}\n"
		//					+ "\n");
		//			sb.append("if(!(e.field == 'DIANCXXB_ID'||e.field == 'GONGYSB_ID'||e.field == 'JIHKJB_ID'||e.field == 'FAZ_ID'||e.field == 'DAOZ_ID')){e.record.set('QUANN',parseFloat(e.record.get('Y1')==''?0:e.record.get('Y1'))+parseFloat(e.record.get('Y2')==''?0:e.record.get('Y2'))+parseFloat(e.record.get('Y3')==''?0:e.record.get('Y3'))+parseFloat(e.record.get('Y4')==''?0:e.record.get('Y4'))" +
		//					" +parseFloat(e.record.get('Y5')==''?0:e.record.get('Y5'))+parseFloat(e.record.get('Y6')==''?0:e.record.get('Y6'))+parseFloat(e.record.get('Y7')==''?0:e.record.get('Y7'))+parseFloat(e.record.get('Y8')==''?0:e.record.get('Y8'))+parseFloat(e.record.get('Y9')==''?0:e.record.get('Y9'))" +
		//					"  +parseFloat(e.record.get('Y10')==''?0:e.record.get('Y10'))+parseFloat(e.record.get('Y11')==''?0:e.record.get('Y11'))+parseFloat(e.record.get('Y12')==''?0:e.record.get('Y12')) )};");
		//			
		//		sb.append("});");
		//		
		//		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
//		StringBuffer sb = new StringBuffer();
//		sb
//				.append("gridDiv_grid.on('afteredit',function(e){"
//						+ "e.record.set('DAOCZHJHS',Round(eval(e.record.get('CHUKJG')||0)+eval(e.record.get('YUNJ')||0)+eval(e.record.get('ZAF')||0)+eval(e.record.get('QITFY')||0),2));"
//						+
//
//						"e.record.set('DAOCZHJBHS',Round(eval(e.record.get('CHUKJG')||0)/1.17+eval(e.record.get('YUNJ')||0)*0.93+eval(e.record.get('ZAF')||0)+eval(e.record.get('QITFY')||0),2));"
//						+
//
//						"e.record.set('BUHSBMDJ',Round(eval(e.record.get('DAOCZHJBHS')||0)*29271/eval(e.record.get('DAOCRZ')||0),2));"
//						+ "});");
//		egu.addOtherScript(sb.toString());
		egu.addOtherScript(Strtmpfunction);

		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setKaisrqValue(null);
			this.getKaisrqModels();
			this.setKaisrq1Value(null);
			this.getKaisrq1Models();
			this.setYuef(null);
			this.setYuefs(null);
			this.setYuefValue(null);
			this.setYuefsValue(null);
			this.getYuefModels();
			this.getYuefsModels();
			this.setKaisrq1(null);
			this.setYuef1Value(null);
			this.setYuefs1Value(null);
			this.getYuef1Models();
			this.getYuefs1Models();
			this.setJiezrq(null);
			this.getJiezrqModels();
			this.setJiezrq1(null);
			this.setJiezrqValue(null);
			this.setJiezrq1Value(null);
			this.getJiezrq1Models();
			this.setTreeid(null);
//			this.setGongysValue(null);
//			this.getGongysModels();
			this.setJihkjValue(null);
			this.getJihkjModels();
			setTbmsg(null);
			setRiq1();
			setRiq2();
			setRiq3();
			//setgys();
			getSelectData();
		}
		getSelectData();
	}

	//	 年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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

	//开始日期
	private static IPropertySelectionModel _KaisrqModel;

	public IPropertySelectionModel getKaisrqModel() {
		if (_KaisrqModel == null) {
			getKaisrqModels();
		}
		return _KaisrqModel;
	}

	private IDropDownBean _KaisrqValue;

	public IDropDownBean getKaisrqValue() {
		if (_KaisrqValue == null) {
			for (int i = 0; i < _KaisrqModel.getOptionCount(); i++) {
				Object obj = _KaisrqModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_KaisrqValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _KaisrqValue;
	}

	public boolean kaisrqchanged;

	public void setKaisrqValue(IDropDownBean Value) {
		if (_KaisrqValue != Value) {
			kaisrqchanged = true;
		}
		_KaisrqValue = Value;
	}

	public IPropertySelectionModel getKaisrqModels() {
		List listKaisrq = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listKaisrq.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_KaisrqModel = new IDropDownModel(listKaisrq);
		return _KaisrqModel;
	}

	public void setKaisrqModel(IPropertySelectionModel _value) {
		_KaisrqModel = _value;
	}

	//截止日期
	private static IPropertySelectionModel _JiezrqModel;

	public IPropertySelectionModel getJiezrqModel() {
		if (_JiezrqModel == null) {
			getJiezrqModels();
		}
		return _JiezrqModel;
	}

	private IDropDownBean _JiezrqValue;

	public IDropDownBean getJiezrqValue() {
		if (_JiezrqValue == null) {
			for (int i = 0; i < _JiezrqModel.getOptionCount(); i++) {
				Object obj = _JiezrqModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_JiezrqValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _JiezrqValue;
	}

	public boolean jiezrqchanged;

	public void setJiezrqValue(IDropDownBean Value) {
		if (_JiezrqValue != Value) {
			jiezrqchanged = true;
		}
		_JiezrqValue = Value;
	}

	public IPropertySelectionModel getJiezrqModels() {
		List listJiezrq = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listJiezrq.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_JiezrqModel = new IDropDownModel(listJiezrq);
		return _JiezrqModel;
	}

	public void setJiezrqModel(IPropertySelectionModel _value) {
		_JiezrqModel = _value;
	}
	
	//市场煤开始年份
	private static IPropertySelectionModel _Kaisrq1Model;

	public IPropertySelectionModel getKaisrq1Model() {
		if (_Kaisrq1Model == null) {
			getKaisrq1Models();
		}
		return _Kaisrq1Model;
	}

	private IDropDownBean _Kaisrq1Value;

	public IDropDownBean getKaisrq1Value() {
		if (_Kaisrq1Value == null) {
			for (int i = 0; i < _Kaisrq1Model.getOptionCount(); i++) {
				Object obj = _Kaisrq1Model.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_Kaisrq1Value = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _Kaisrq1Value;
	}

	public boolean kaisrq1changed;

	public void setKaisrq1Value(IDropDownBean Value) {
		if (_Kaisrq1Value != Value) {
			kaisrq1changed = true;
		}
		_Kaisrq1Value = Value;
	}

	public IPropertySelectionModel getKaisrq1Models() {
		List listKaisrq1 = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listKaisrq1.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_Kaisrq1Model = new IDropDownModel(listKaisrq1);
		return _Kaisrq1Model;
	}

	public void setKaisrq1Model(IPropertySelectionModel _value) {
		_Kaisrq1Model = _value;
	}


	//市场煤截止日期
	private static IPropertySelectionModel _Jiezrq1Model;

	public IPropertySelectionModel getJiezrq1Model() {
		if (_Jiezrq1Model == null) {
			getJiezrq1Models();
		}
		return _Jiezrq1Model;
	}

	private IDropDownBean _Jiezrq1Value;

	public IDropDownBean getJiezrq1Value() {
		if (_Jiezrq1Value == null) {
			for (int i = 0; i < _Jiezrq1Model.getOptionCount(); i++) {
				Object obj = _Jiezrq1Model.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_Jiezrq1Value = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _Jiezrq1Value;
	}

	public boolean jiezrq1changed;

	public void setJiezrq1Value(IDropDownBean Value) {
		if (_Jiezrq1Value != Value) {
			jiezrq1changed = true;
		}
		_Jiezrq1Value = Value;
	}

	public IPropertySelectionModel getJiezrq1Models() {
		List listJiezrq1 = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listJiezrq1.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_Jiezrq1Model = new IDropDownModel(listJiezrq1);
		return _Jiezrq1Model;
	}

	public void setJiezrq1Model(IPropertySelectionModel _value) {
		_Jiezrq1Model = _value;
	}
	
	
	// 起始月份下拉框
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

	//截止月份
	private static IPropertySelectionModel _YuefsModel;

	public IPropertySelectionModel getYuefsModel() {
		if (_YuefsModel == null) {
			getYuefsModels();
		}
		return _YuefsModel;
	}

	private IDropDownBean _YuefsValue;

	public IDropDownBean getYuefsValue() {
		if (_YuefsValue == null) {
			int _yuefs = DateUtil.getMonth(new Date());
			if (_yuefs == 1) {
				_yuefs = 12;
			} else {
				_yuefs = _yuefs - 1;
			}
			for (int i = 0; i < getYuefsModel().getOptionCount(); i++) {
				Object obj = getYuefsModel().getOption(i);
				if (_yuefs == ((IDropDownBean) obj).getId()) {
					_YuefsValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefsValue;
	}

	public void setYuefsValue(IDropDownBean Value) {
		if (_YuefsValue != Value) {
			_YuefsValue = Value;
		}
	}

	public IPropertySelectionModel getYuefsModels() {
		List listYuefs = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuefs.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefsModel = new IDropDownModel(listYuefs);
		return _YuefsModel;
	}

	public void setYuefsModel(IPropertySelectionModel _value) {
		_YuefsModel = _value;
	}
	
	// 市场起始月份下拉框
	private static IPropertySelectionModel _Yuef1Model;

	public IPropertySelectionModel getYuef1Model() {
		if (_Yuef1Model == null) {
			getYuef1Models();
		}
		return _Yuef1Model;
	}

	private IDropDownBean _Yuef1Value;

	public IDropDownBean getYuef1Value() {
		if (_Yuef1Value == null) {
			int _yuef1 = DateUtil.getMonth(new Date());
			if (_yuef1 == 1) {
				_yuef1 = 12;
			} else {
				_yuef1 = _yuef1 - 1;
			}
			for (int i = 0; i < getYuef1Model().getOptionCount(); i++) {
				Object obj = getYuef1Model().getOption(i);
				if (_yuef1 == ((IDropDownBean) obj).getId()) {
					_Yuef1Value = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _Yuef1Value;
	}

	public void setYuef1Value(IDropDownBean Value) {
		if (_Yuef1Value != Value) {
			_Yuef1Value = Value;
		}
	}

	public IPropertySelectionModel getYuef1Models() {
		List listYuef1 = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef1.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_Yuef1Model = new IDropDownModel(listYuef1);
		return _Yuef1Model;
	}

	public void setYuef1Model(IPropertySelectionModel _value) {
		_Yuef1Model = _value;
	}

//	市场截止月份
	private static IPropertySelectionModel _Yuefs1Model;

	public IPropertySelectionModel getYuefs1Model() {
		if (_Yuefs1Model == null) {
			getYuefs1Models();
		}
		return _Yuefs1Model;
	}

	private IDropDownBean _Yuefs1Value;

	public IDropDownBean getYuefs1Value() {
		if (_Yuefs1Value == null) {
			int _yuefs1 = DateUtil.getMonth(new Date());
			if (_yuefs1 == 1) {
				_yuefs1 = 12;
			} else {
				_yuefs1 = _yuefs1 - 1;
			}
			for (int i = 0; i < getYuefs1Model().getOptionCount(); i++) {
				Object obj = getYuefs1Model().getOption(i);
				if (_yuefs1== ((IDropDownBean) obj).getId()) {
					_Yuefs1Value = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _Yuefs1Value;
	}

	public void setYuefs1Value(IDropDownBean Value) {
		if (_Yuefs1Value != Value) {
			_Yuefs1Value = Value;
		}
	}

	public IPropertySelectionModel getYuefs1Models() {
		List listYuefs1 = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuefs1.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_Yuefs1Model = new IDropDownModel(listYuefs1);
		return _Yuefs1Model;
	}

	public void setYuefs1Model(IPropertySelectionModel _value) {
		_Yuefs1Model = _value;
	}
	
	
	//计划口径
	public boolean _Jihkjchange = false;

	public IDropDownBean getJihkjValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJihkjModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJihkjValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getJihkjModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getJihkjModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setJihkjModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJihkjModels() {

		String sql = "select id,mingc from jihkjb ";

		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	//	供应商
//	public boolean _Gongyschange = false;
//
//	public IDropDownBean getGongysValue() {
//
//		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
//			((Visit) getPage().getVisit())
//					.setDropDownBean6((IDropDownBean) getGongysModel()
//							.getOption(0));
//		}
//		return ((Visit) getPage().getVisit()).getDropDownBean6();
//	}
//
//	public void setGongysValue(IDropDownBean Value) {
//
//		if (Value != ((Visit) getPage().getVisit()).getDropDownBean6()) {
//
//			((Visit) getPage().getVisit()).setboolean4(true);
//
//		}
//		((Visit) getPage().getVisit()).setDropDownBean6(Value);
//	}
//
//	public IPropertySelectionModel getGongysModel() {
//
//		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
//
//			getGongysModels();
//		}
//		return ((Visit) getPage().getVisit()).getProSelectionModel6();
//	}
//
//	public void setGongysModel(IPropertySelectionModel value) {
//
//		((Visit) getPage().getVisit()).setProSelectionModel6(value);
//	}
//
//	public IPropertySelectionModel getGongysModels() {
//
//		String sql = "select id,mingc from gongysb where fuid=0 order by xuh ";
//
//		((Visit) getPage().getVisit())
//				.setProSelectionModel6(new IDropDownModel(sql));
//		return ((Visit) getPage().getVisit()).getProSelectionModel6();
//	}

//	//判断当前生成数据的供应商的计划口径
//	public String getJih() {
//		String jih = "";
//		JDBCcon cn = new JDBCcon();
//		String sql_jihkj = "select id,jihkjb_id from caigysmxb where diancxxb_id= "
//				+ getTreeid()
//				+ " and gongysb_id="
//				+ MainGlobal.getProperId(getGongysModel(), this.getGongys());
//		ResultSet rs = cn.getResultSet(sql_jihkj);
//		try {
//			while (rs.next()) {
//				jih = rs.getString("jihkjb_id");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		} finally {
//			cn.Close();
//		}
//
//		return jih;
//
//	}

	//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	//得到电厂的默认到站
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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

	//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

}