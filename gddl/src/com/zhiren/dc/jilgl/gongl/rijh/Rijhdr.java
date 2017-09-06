package com.zhiren.dc.jilgl.gongl.rijh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.DateUtil;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;

public class Rijhdr extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	public String getRiq() {
		if(((Visit)this.getPage().getVisit()).getString2()== null){
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1,
					DateUtil.AddType_intDay)));
		}
		return ((Visit)this.getPage().getVisit()).getString2();
	}

	public void setRiq(String riqi) {
		((Visit)this.getPage().getVisit()).setString2(riqi);
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void save(Visit v,JDBCcon con,String changeid,int intysfs){
		StringBuffer sb = new StringBuffer(
				"insert into cheplsb\n" +
				"(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id,\n" + 
				"jihkjb_id, fahrq, daohrq, caiybh, yunsfsb_id, chec,cheph, maoz, piz,\n" + 
				"biaoz, koud, kous, kouz, sanfsl, jianjfs, chebb_id, yuandz_id,\n" + 
				"yuanshdwb_id,  yuanmkdw, zhongcjjy, daozch, lury, beiz,qingcsj,zhongcsj,\n" + 
				"yunsdwb_id,caiyrq,zhilb_id)\n" + 
				"(select getnewid("+v.getDiancxxb_id()+"),"+v.getDiancxxb_id()+",q.gongys_id,q.meikxxb_id,q.pinz_id\n" + 
				",(select id from chezxxb where mingc =c.faz) faz_id\n" + 
				",(select id from chezxxb where mingc =c.daoz) daoz_id\n" + 
				",q.kouj_id,c.fahrq,c.daohrq,c.caiybh, "+intysfs+",q.chec,c.cheph,c.maoz\n" + 
				",c.piz,c.biaoz, c.koud,c.kous,c.kouz,c.sanfsl,c.jianjfs,c.chebb_id\n" + 
				",(select nvl(max(id),0) from chezxxb where mingc =c.yuandz) yuandz_id\n" + 
				",(select nvl(max(id),"+v.getDiancxxb_id()+") from vwyuanshdw where mingc = c.yuanshdw) yuanshdw_id\n" + 
				",c.yuanmkdw,c.zhongcjjy,c.daozch,c.lury,c.beiz,c.qingcsj\n" + 
				",c.zhongcsj,q.yunsdwb_id,c.caiyrq,q.zhilb_id from chepbtmp c,qicrjhb q\n" + 
				"where c.qicrjhb_id = q.id and c.qicrjhb_id = "+changeid+" )");
			int flag = con.getInsert(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog
						.writeErrorLog(ErrorMessage.InsertDatabaseFail + "sb:" + sb);
				setMsg(ErrorMessage.Jhdr001);
				return;
			}
			if(MainGlobal.getXitxx_item("数量", "日计划生成采样", String.valueOf(v.getDiancxxb_id()), "否").equals("否")){
				flag = Jilcz.Updatezlid(con, v.getDiancxxb_id(),
						intysfs, null);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Jhdr002);
					setMsg(ErrorMessage.Jhdr002);
					return;
				}
			}
			flag = Jilcz.INSorUpfahb(con, v.getDiancxxb_id());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr003);
				setMsg(ErrorMessage.Jhdr003);
				return;
			}
			flag = Jilcz.InsChepb(con, v.getDiancxxb_id(),null, 9);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr004);
				setMsg(ErrorMessage.Jhdr004);
				return;
			}
			sb.delete(0, sb.length());
			sb.append("update chepb set Qicrjhb_id = "+changeid+" where id in (")
			.append("select c.id from chepb c,fahb f where f.id = c.fahb_id and c.hedbz=9 and f.diancxxb_id="+v.getDiancxxb_id()+")");
			flag = con.getUpdate(sb.toString());
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr008);
				setMsg(ErrorMessage.Jhdr008);
				return;
			}
			
	}
	private void Save() {
		if(getChange() == null || "".equals(getChange())){
			setMsg("请选择一条计划进行导入操作!");
			return;
		}
		
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		sb.append("select zhuangt from qicrjhb where zhuangt = 1 and id =" + getChange());
		if(con.getHasIt(sb.toString())){
			setMsg("该数据已经导入,不能重复导入!");
			return;
		}
		save(v,con,getChange(),SysConstant.YUNSFS_QIY);
		int flag = 0;
		sb.delete(0, sb.length());
		sb.append("select c.* from chepb c,fahb f where f.id = c.fahb_id and c.hedbz=9 and f.diancxxb_id="+v.getDiancxxb_id());
		ResultSetList rsl = con.getResultSetList(sb.toString());
		List fhlist = new ArrayList();
		while (rsl.next()) {
			flag = Jilcz.CountChepbYuns(con, rsl.getString("id"),
					SysConstant.HEDBZ_YJJ);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr005);
				setMsg(ErrorMessage.Jhdr005);
				return;
			}
			Jilcz.addFahid(fhlist, rsl.getString("fahb_id"));
		}
		rsl.close();
		for (int i = 0; i < fhlist.size(); i++) {
			flag = Jilcz.updateFahb(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr006);
				setMsg(ErrorMessage.Jhdr006);
				return;
			}
			flag = Jilcz.updateLieid(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr007);
				setMsg(ErrorMessage.Jhdr007);
				return;
			}
		}
		
		sb.delete(0, sb.length());
		sb.append("begin\n")
		.append("update chepbtmp set fahb_id = 1 where fahb_id = 0 and diancxxb_id=")
		.append(v.getDiancxxb_id()).append("and qicrjhb_id = ").append(getChange()).append(";\n")
		.append("update qicrjhb set zhuangt = 1 where id =").append(getChange()).append(";\n end;");
		flag = con.getUpdate(sb.toString());
		if(flag == -1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Jhdr009);
			setMsg(ErrorMessage.Jhdr009);
			return;
		}
		con.commit();
		con.Close();
		Chengbjs.CountChengb(v.getDiancxxb_id(), fhlist);
		setMsg("保存成功");
	}
	
	private void Delete(){
		if(getChange() == null || "".equals(getChange())){
			setMsg("请选择一条计划进行撤销操作!");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		sb.append("select zhuangt from qicrjhb where zhuangt = 0 and id =" + getChange());
		if(con.getHasIt(sb.toString())){
			setMsg("该数据未导入,不能进行撤销操作!");
			return;
		}
		sb.delete(0, sb.length());
		sb.append("select hedbz from fahb where id in (select distinct fahb_id from chepb where qicrjhb_id = "+getChange()+ ")");
		ResultSetList rs = con.getResultSetList(sb.toString());
		while(rs.next()){
			if(SysConstant.HEDBZ_YSH == rs.getInt("hedbz")){
				setMsg("该数据已审核,不能进行撤销操作!");
				rs.close();
				return;
			}
		}
		rs.close();
		sb.delete(0, sb.length());
		sb.append("select distinct fahb_id from chepb where qicrjhb_id = "+getChange());
		rs = con.getResultSetList(sb.toString());
		List fhlist = new ArrayList();
		while(rs.next()){
			Jilcz.addFahid(fhlist, rs.getString("fahb_id"));
		}
		rs.close();
		sb.delete(0, sb.length());
		sb.append("delete from chepb where qicrjhb_id = "+getChange());
		int flag = con.getDelete(sb.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Jhdr010);
			setMsg(ErrorMessage.Jhdr010);
			return;
		}
		for (int i = 0; i < fhlist.size(); i++) {
			flag = Jilcz.updateFahb(con, (String) fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jhdr011);
				setMsg(ErrorMessage.Jhdr011);
				return;
			}
		}
		sb.delete(0, sb.length());
		sb.append("update qicrjhb set zhuangt = 0 where id = "+getChange());
		flag = con.getUpdate(sb.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Jhdr012);
			setMsg(ErrorMessage.Jhdr012);
			return;
		}
		con.commit();
		setMsg("撤消成功");
	}

	private boolean _RefurbishChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _ChakChick = false;

	public void ChakButton(IRequestCycle cycle) {
		_ChakChick = true;
	}

	public void submit(IRequestCycle cycle) {
		setMsg("");
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ChakChick) {
			_ChakChick = false;
			Update(cycle);
		}
	}

	public void getSelectData() {
		String rq = DateUtil.FormatOracleDate(getRiq());
		if (rq == null || "".equals(rq)) {
			rq = DateUtil.FormatOracleDate(new Date());
		}
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 

			"select j.id,decode(j.zhuangt,1,'已导入','未导入') zhuangt,g.mingc gongys\n" +
			",m.mingc meikdw,p.mingc pinz,y.mingc yunsdw,jh.jhcs,wc.wccs,j.jihl,wc.wcl\n" + 
			"from qicrjhb j,gongysb g,meikxxb m,pinzb p, yunsdwb y,\n" + 
			"(select j.id,sum(jc.yunmcs)jhcs from qicrjhb j,qicrjhcpb jc\n" + 
			"where j.id = jc.qicrjhb_id\n" + 
			"group by j.id) jh,\n" + 
			"(select j.id,count(c.id) wccs, nvl(sum(c.maoz-c.piz),0) wcl from qicrjhb j,chepbtmp c\n" + 
			"where j.id = c.qicrjhb_id\n" + 
			"group by j.id) wc\n" + 
			"where j.id = jh.id and j.id = wc.id\n" + 
			"and j.gongys_id = g.id and j.meikxxb_id = m.id\n" + 
			"and j.pinz_id = p.id and j.yunsdwb_id = y.id\n" + 
			"and j.riq = "+rq+" and j.diancxxb_id = "+v.getDiancxxb_id();
		
		ResultSetList rs = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);
//		选择gridModel
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// 只能单选中行
//		设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(21);
		
		egu.getColumn("zhuangt").setHeader(Locale.qicrjh_zhuangt);
		egu.getColumn("zhuangt").setWidth(60);
		egu.getColumn("gongys").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongys").setWidth(150);
		egu.getColumn("meikdw").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikdw").setWidth(120);
		egu.getColumn("pinz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdw").setWidth(100);
		egu.getColumn("jhcs").setHeader(Locale.qicrjh_jhcs);
		egu.getColumn("jhcs").setWidth(80);
		egu.getColumn("wccs").setHeader(Locale.qicrjh_wccs);
		egu.getColumn("wccs").setWidth(80);
		egu.getColumn("jihl").setHeader(Locale.qicrjh_jhl);
		egu.getColumn("jihl").setWidth(80);
		egu.getColumn("wcl").setHeader(Locale.qicrjh_wcl);
		egu.getColumn("wcl").setWidth(80);

		egu.addTbarText(Locale.daohrq_id_fahb+":");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		GridButton bc = new GridButton("导入", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGE');"
				+ " Cobjid.value = id;"
				+ " document.getElementById('SaveButton').click();}else{" 
				+ MainGlobal.getExtMessageBox("请选择一条记录",false) + "}}");
		egu.addTbarBtn(bc);
		GridButton cx = new GridButton("撤销", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGE');"
				+ " Cobjid.value = id;"
				+ " document.getElementById('DeleteButton').click();}else{"
				+ MainGlobal.getExtMessageBox("请选择一条记录",false)+"}}");
		egu.addTbarBtn(cx);
		GridButton gb = new GridButton("查看", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGE');"
				+ " Cobjid.value = id;"
				+ " document.getElementById('ChakButton').click();}else{"
			+ MainGlobal.getExtMessageBox("请选择一条记录",false) + "}}");
		egu.addTbarBtn(gb);
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	// 页面判定方法
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

	private void Update(IRequestCycle cycle) {
		if(getChange() == null || "".equals(getChange())){
			setMsg("请选择一条计划查看明细!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString10("Rijhdr");
		visit.setLong1(Integer.parseInt(getChange()));
		cycle.activate("Jihsjdrmx");
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if(!visit.getActivePageName().equalsIgnoreCase("Jihsjdrmx") ){
				setRiq(null);
			}
			visit.setActivePageName(this.getPageName().toString());
			getSelectData();
		}

	}
}