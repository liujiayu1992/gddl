package com.zhiren.heiljkhh;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhoubwh extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
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
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		return riq;
	}
	
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid==null||treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
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
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CreateClick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			setOriRiq(getRiq());
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} else if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
	}
	public void save() {
		Visit visit = (Visit) this.getPage().getVisit();
		 int flag = -1;
		 flag = visit.getExtGrid1().Save(getChange(), visit);
		 if(flag>=0){
			 setMsg("保存成功！");
		 }else{
			 setMsg("保存失败！");
		 }
	}
	//生成按钮
	public void CreateData() {
//		String diancxxb_id = getTreeid();
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag;
		String Ma = "";
		String Su = "";
//		String isql = "";
		String riq = "";
		StringBuffer isql = new StringBuffer("");
		//得要时间段（一周）
		String sql = 
			"select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" +
			"       TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			Ma = rs.getString("YI");
			Su = rs.getString("RI");
		}
		//清空hlj_zhoub中的原始记录
		sql = 
			"delete from hlj_zhoub h\n" +
			"where h.diancxxb_id = "+getTreeid()+"\n" + 
			"      and h.riq>=to_date('"+Ma+"','yyyy-MM-dd') and h.riq<=to_date('"+Su+"','yyyy-MM-dd')";

		flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.DeleteZhoubFailed + "\nSQL:" + sql);
			setMsg(ErrorMessage.DeleteZhoubFailed);
			con.rollBack();
			con.Close();
			return;
		}
		//得到生成的数据
		
		sql = 
			"select b.diancxxb_id,\n" +
			"       b.riq,\n" + 
			"		nvl(tp.laim+df.laim,0) as laim_hj,\n" +
			"		nvl(tp.laim,0) as laim_tp,\n" +
			"		nvl(df.laim,0) as laim_df,\n" + 
			"		nvl(h.haoy,0) as haoy,\n" + 
			"		nvl(h.kuc,0) as kuc\n" +
			"from\n" + 
			"------------------表头-------------------------------\n" +
			"((select d.id as diancxxb_id,to_char(x.riq,'yyyy-MM-dd') as riq\n" + 
			"from diancxxb d,\n" + 
			"     (select to_date(w.ri,'yyyy-MM-dd')-rownum+1 as riq\n" + 
			"     from\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w,all_objects\n" + 
			"      where rownum<=7 ) x\n" + 
			"where d.id = "+getTreeid()+"\n" + 
			")\n" +
			"union\n" +
			"(select d.id as diancxxb_id,'周计' as riq\n" + 
			"from diancxxb d\n" + 
			"where d.id = "+getTreeid()+")\n" + 
			"union\n" + 
			"(select d.id as diancxxb_id,'月累' as riq\n" + 
			"from diancxxb d\n" + 
			"where d.id = "+getTreeid()+")\n"+
			")b,\n" +
			"-----------------统配----------------------------\n" + 
			"((select f.diancxxb_id,\n" + 
			"       decode(f.daohrq,null,'周计' ,to_char(f.daohrq,'yyyy-MM-dd')) as riq,\n" + 
			"       sum(round_new(f.laimsl,0)) as laim\n" + 
			"from fahb f,meikxxb m,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" + 
			"       TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where f.diancxxb_id = "+getTreeid()+"\n" + 
			"      and f.meikxxb_id = m.id\n" + 
			"      and m.leib = '统配'\n" + 
			"      and f.daohrq>=to_date(w.yi,'yyyy-MM-dd') and f.daohrq<=to_date(w.ri,'yyyy-MM-dd')\n" + 
			"group by rollup(f.diancxxb_id,f.daohrq)\n" + 
			"having not (grouping(f.diancxxb_id)+grouping(f.daohrq)) = 2\n" + 
			")\n" + 
			"union\n" + 
			"(select f.diancxxb_id,\n" + 
			"       decode(f.daohrq,null,'月累',f.daohrq) as riq,\n" + 
			"       sum(round_new(f.laimsl,0)) as laim\n" + 
			"from fahb f,meikxxb m,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where f.diancxxb_id = "+getTreeid()+"\n" + 
			"      and f.meikxxb_id = m.id\n" + 
			"      and m.leib = '统配'\n" + 
			"      and to_char(f.daohrq,'yyyy')||'-'||to_char(f.daohrq,'MM')=to_char(to_date(w.ri,'yyyy-MM-dd'),'yyyy')||'-'||to_char(to_date(w.ri,'yyyy-MM-dd'),'MM')\n" + 
			"group by rollup(f.diancxxb_id,f.daohrq)\n" + 
			"having not (grouping(f.diancxxb_id)-grouping(f.daohrq)) = 0\n" + 
			")\n" + 
			") tp,\n" + 
			"----------------------地方--------------------------\n" + 
			"((select f.diancxxb_id,\n" + 
			"       decode(f.daohrq,null,'周计',to_char(f.daohrq,'yyyy-MM-dd')) as riq,\n" + 
			"       sum(round_new(f.laimsl,0)) as laim\n" + 
			"from fahb f,meikxxb m,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" + 
			"       TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where f.diancxxb_id = "+getTreeid()+"\n" + 
			"      and f.meikxxb_id = m.id\n" + 
			"      and m.leib = '地方'\n" + 
			"      and f.daohrq>=to_date(w.yi,'yyyy-MM-dd') and f.daohrq<=to_date(w.ri,'yyyy-MM-dd')\n" + 
			"group by rollup(f.diancxxb_id,f.daohrq)\n" + 
			"having not (grouping(f.diancxxb_id)+grouping(f.daohrq)) = 2)\n" + 
			"union\n" + 
			"(select f.diancxxb_id,\n" + 
			"       decode(f.daohrq,null,'月累',to_char(f.daohrq,'yyyy-MM-dd')) as riq,\n" + 
			"       sum(round_new(f.laimsl,0)) as laim\n" + 
			"from fahb f,meikxxb m,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where f.diancxxb_id = "+getTreeid()+"\n" + 
			"      and f.meikxxb_id = m.id\n" + 
			"      and m.leib = '地方'\n" + 
			"      and to_char(f.daohrq,'yyyy')||'-'||to_char(f.daohrq,'MM')=to_char(to_date(w.ri,'yyyy-MM-dd'),'yyyy')||'-'||to_char(to_date(w.ri,'yyyy-MM-dd'),'MM')\n" + 
			"group by rollup(f.diancxxb_id,f.daohrq)\n" + 
			"having not (grouping(f.diancxxb_id)-grouping(f.daohrq)) = 0\n" + 
			")\n" + 
			") df,\n" + 
			"--------------------耗用&库存------------------------\n" + 
			"((select s.diancxxb_id,\n" + 
			"       to_char(s.riq,'yyyy-MM-dd') as riq,\n" + 
			"       round_new(s.haoyqkdr,0) haoy,\n" + 
			"       round_new(s.kuc,0) as kuc\n" + 
			"from shouhcrbb s,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" + 
			"       TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where s.diancxxb_id = "+getTreeid()+"\n" + 
			"      and s.riq>=to_date(w.yi,'yyyy-MM-dd') and s.riq<=to_date(w.ri,'yyyy-MM-dd')\n" + 
			")\n" + 
			"union\n" + 
			"(select s.diancxxb_id,\n" + 
			"       decode(s.riq,null,'周计',s.riq) as riq,\n" + 
			"       sum(round_new(s.haoyqkdr,0)) as  haoy,\n" + 
			"       sum(decode(to_char(s.riq,'yyyy-MM-dd'),w.ri,round_new(s.kuc,0),0)) as kuc\n" + 
			"from shouhcrbb s,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" + 
			"       TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where s.diancxxb_id = "+getTreeid()+"\n" + 
			"      and s.riq>=to_date(w.yi,'yyyy-MM-dd') and s.riq<=to_date(w.ri,'yyyy-MM-dd')\n" + 
			"group by rollup (s.diancxxb_id,s.riq)\n" + 
			"having not (grouping(s.diancxxb_id)-grouping(s.riq))=0\n" + 
			")\n" + 
			"union\n" + 
			"(select s.diancxxb_id,\n" + 
			"       '月累' as riq,\n" + 
			"       sum(round_new(s.haoyqkdr,0)) as haoy,\n" + 
			"       0 as kuc\n" + 
			"from shouhcrbb s,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where s.diancxxb_id = "+getTreeid()+"\n" + 
			"      and to_char(s.riq,'yyyy')||'-'||to_char(s.riq,'MM')=to_char(to_date(w.ri,'yyyy-MM-dd'),'yyyy')||'-'||to_char(to_date(w.ri,'yyyy-MM-dd'),'MM')\n" + 
			"group by rollup (s.diancxxb_id)\n" + 
			"having not grouping(s.diancxxb_id) = 1\n" + 
			")\n" + 
			") h\n" + 

			"where b.diancxxb_id = tp.diancxxb_id(+)\n" +
			"      and b.diancxxb_id = df.diancxxb_id(+)\n" + 
			"      and b.diancxxb_id = h.diancxxb_id(+)\n" + 
			"      and b.riq = tp.riq(+)\n" + 
			"      and b.riq = df.riq(+)\n" + 
			"      and b.riq = h.riq(+)\n" +
			"order by b.diancxxb_id,decode(b.riq,'月累',1,'周计',2,3) desc,b.riq\n";

		
		rs = con.getResultSetList(sql);
		isql.append("begin\n");
		while(rs.next()){
			long zhoub_id = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
			if(!(rs.getString("RIQ").equals("月累"))&&!(rs.getString("RIQ").equals("周计"))){
				riq = "to_date('" + rs.getString("RIQ") + "','yyyy-MM-dd'),";
			}
			isql.append("insert into hlj_zhoub values(").append(zhoub_id).append(",\n")
				.append(rs.getString("DIANCXXB_ID")).append(",\n")
				.append(riq).append("\n");
			if(rs.getString("riq").equals("周计")){
				isql.append("'周',\n");
			}else if(rs.getString("riq").equals("月累")){
				isql.append("'月',\n"); 
			}else{
				isql.append("'日',\n");
			}
			isql.append(rs.getString("LAIM_HJ")).append(",\n")
				.append(rs.getString("LAIM_TP")).append(",\n")
				.append("0,\n")
				.append(rs.getString("LAIM_DF")).append(",\n")
				.append("0,\n")
				.append(rs.getString("HAOY")).append(",\n")
				.append(rs.getString("KUC")).append(",\n")
				.append("null);\n");
		}
		isql.append("end;\n");
		flag = con.getInsert(isql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.InsertZhoubFailed + "\nSQL:" + sql);
			setMsg(ErrorMessage.InsertZhoubFailed);
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
		setMsg("数据成功生成！");
	}
	

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";
		}
		String sSql = 
			"select h.id,\n" +
			"       d.quanc as diancxxb_id,\n" + 
			"       decode(h.leix,'周','周计','月','月累',to_char(h.riq,'yyyy-MM-dd')) as riq,\n" + 
			"       h.laimhj,\n" + 
			"       h.laim_tp,\n" + 
			"       h.cbj_tp,\n" + 
			"       h.laim_df,\n" + 
			"       h.cbj_df,\n" + 
			"       h.haoy,\n" + 
			"       h.kuc,\n" + 
			"       h.beiz\n" + 
			"from hlj_zhoub h,diancxxb d,\n" + 
			"     (select TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" + 
			"       TO_CHAR( to_date('" + getRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
			"      from dual) w\n" + 
			"where h.diancxxb_id = d.id\n" + 
			   str + "\n" + 
			"      and h.riq>=to_date(w.yi,'yyyy-MM-dd') and h.riq<=to_date(w.ri,'yyyy-MM-dd')\n" + 
			"order by d.quanc,decode(h.leix,'日',1,'周',2,'月',3,4),h.riq\n";
		
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setTableName("hlj_zhoub");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setHeader("统计日期");
		egu.getColumn("riq").update = false;
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("laimhj").setHeader("来煤合计<br>(吨)");
		egu.getColumn("laimhj").setEditor(null);
		egu.getColumn("laim_tp").setHeader("统配来煤<br>(吨)");
		egu.getColumn("laim_tp").setEditor(null);
		egu.getColumn("cbj_tp").setHeader("车板价格<br>(元/吨)");
		egu.getColumn("laim_df").setHeader("地方自购<br>(吨)");
		egu.getColumn("laim_df").setEditor(null);
		egu.getColumn("cbj_df").setHeader("车板价格<br>(元/吨)");
		egu.getColumn("haoy").setHeader("耗用<br>(吨)");
		egu.getColumn("haoy").setEditor(null);
		egu.getColumn("kuc").setHeader("库存<br>(吨)");
		egu.getColumn("kuc").setEditor(null);
		egu.getColumn("beiz").setHeader("备注");
		if (treejib == 1) {
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.quanc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);

		} else if (treejib == 2) {
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,quanc from diancxxb where fuid=" + getTreeid() + " order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else {
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,quanc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,quanc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("quanc");
			}
//			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		}
		egu.addTbarText("日期:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		
//		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
//		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('RIQ')!='周计'&&e.record.get('RIQ')!='月累'&&e.field!='BEIZ'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		sb.append(" if(e.field!='CBJ_TP'&&e.field!='CBJ_DF'&&e.field!='BEIZ'){ e.cancel=true;}");//电厂列不允许编辑
		sb.append("});");
		
		
//		
//		
//       //设定合计列不保存
//		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='合计') return 'continue';}");
//		 
		egu.addOtherScript(sb.toString());
//		//---------------页面js计算结束--------------------------
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
//		 判断数据是否被锁定
//		boolean isLocked = isLocked(con);
//		 生成按钮
		if (treejib == 3) {// 选电厂只刷新出该电厂
			GridButton gbc = new GridButton("生成",
					getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
//		String script = "";
//		script = "\ngridDiv_grid.on('beforeedit', function(e) {\n" 
//				+ "\tif (e.record.get('ZHUANGT') == '否') {e.cancel = true;}"
//				+ "});";
//		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(getRiq()).append("一周内的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(getRiq()).append("一周的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	public boolean isLocked(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		return con.getHasIt("select * from hlj_zhoub where riq=" + CurrODate);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setTreeid(null);
		}
		init();
	}
	private void init() {
		setOriRiq(getRiq());
		getSelectData();
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
}