package com.zhiren.dc.jilgl.gongl.shouc;

/*
 * 2009-03-17
 * Yanghj
 * 修改内容：在收车的时候，如果同一批次的车皮有检斤和未检斤的，则将这一批次进行拆分，
 *          检斤过的车皮正常收车，没检斤的车皮新增一个发货。
 */
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

import com.zhiren.common.DateUtil;
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
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 修改人:tzf
 * 时间:2010-03-05
 * 修改内容:收车时，对于新增的发货的lie_id也要进行更新，不能与原来的发货的一样。
 */
/*
 * 修改人:tzf
 * 时间:2009-06-17
 * 修改内容:收车时，对fahb对应的车皮表增加时间限制，以页面上选的时间段内的fahb数据进行收车
 */
/*
 * 2009-05-14
 * 王磊
 * 修改计算成本的发货list未全部加载的问题
 */
public class Shouc extends BasePage implements PageValidateListener {
	// 界面用户提示
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
		// setTbmsg(null);
	}

	// private String tbmsg;
	// public String getTbmsg() {
	// return tbmsg;
	// }
	// public void setTbmsg(String tbmsg) {
	// this.tbmsg = tbmsg;
	// }
	// 绑定开始日期
	private String startRiq;

	public String getStartRiq() {
		return startRiq;
	}

	public void setStartRiq(String riq) {
		this.startRiq = riq;
	}

	// 绑定结束日期
	private String endRiq;

	public String getEndRiq() {
		return endRiq;
	}

	public void setEndRiq(String riq) {
		this.endRiq = riq;
	}

	public void setSRiq(String sRiq) {
		((Visit) getPage().getVisit()).setString1(sRiq);
	}

	public void setERiq(String eRiq) {
		((Visit) getPage().getVisit()).setString2(eRiq);
	}

	public String getSRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public String getERiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public String getQRiq(String ss) {
		if (ss == null || ss.equals("")) {
			String riq = getSRiq();
			return riq.substring(0, 4) + "年" + riq.substring(5, 7) + "月"
					+ riq.substring(8, 10) + "日";
		} else {
			return ss.substring(0, 4) + "年" + ss.substring(5, 7) + "月"
					+ ss.substring(8, 10) + "日";
		}
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// 收车方法 :同一批次的要进行拆分，检斤过的车皮收车，没检斤的新增发货。
	// begin -----------------
	private void shouc() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);//取消自动更新。
		ResultSetList rsl;
		String sShoucRiq = getSRiq();
		String eShoucRiq = getERiq();
		String shoucrq = DateUtil.FormatOracleDate(sShoucRiq);
		String sql = "select * from xitxxb where mingc = '收车日期设置' and zhuangt=1 and leib = '数量' and zhi = '结束日期'";
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			shoucrq = DateUtil.FormatOracleDate(eShoucRiq);
		}
		rsl.close();// 在rsl循环之后马上在后面写关闭。

		int flag;
		List fhlist = new ArrayList();
		long diancxxb_id = visit.getDiancxxb_id();
		String id = "";
//		没收车的id中，有piz<>0的条件，意思是一批次都没来就不用新增加发货。
//		sql = "select id from fahb where daohrq = to_date('2050-12-31','yyyy-mm-dd') and piz <> 0 and yunsfsb_id =" + SysConstant.YUNSFS_QIY;
		
		//没有对fahb的日期做限制  应该和 页面上的时间一致
		
		sql = "select f.id from fahb f where f.daohrq = to_date('2050-12-31','yyyy-mm-dd') and f.piz <> 0 and f.yunsfsb_id =" + SysConstant.YUNSFS_QIY
			+" and exists(select c.fahb_id from chepb c where c.fahb_id=f.id   and c.qingcsj >="+DateUtil.FormatOracleDate(this.getStartRiq())+" and  c.qingcsj<"+DateUtil.FormatOracleDate(this.getEndRiq())+"+1 )";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			id = rsl.getString("id");
			Jilcz.addFahid(fhlist, id);
			sql = "select * from chepb where fahb_id=" + id + " and piz=0";//一个fahb的id对应多个chepb的id
			if(con.getHasIt(sql)){  //如果数据库里面含有这个记录。
//				复制一个新的发货
				String newfahid = Jilcz.CopyFahb(con, id, diancxxb_id);//这个方法返回的是id，或者null。
				if (newfahid == null) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("复制fahb失败");
					setMsg("复制fahb失败");
					return;
				}
				Jilcz.addFahid(fhlist, newfahid);
//				更新未检斤车皮的fahb_id
				sql = "update chepb set fahb_id =" + newfahid + " where fahb_id=" + id +" and piz=0";
				flag = con.getUpdate(sql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
							+ sql);
					setMsg("更新未检斤车皮的fahb_id失败");
					return;
				}
//				计算新发货的毛皮重等信息
				flag = Jilcz.updateFahb(con, newfahid);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("计算fahb失败");
					setMsg("计算fahb失败");
					return;
				}
				//清空新生成的发货的lei_id
				flag=this.UpdateLieId(con, newfahid);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("lie_id更新失败");
					setMsg("lie_id更新失败");
					return;
				}
				
//				判断是否单车计算运损
				boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("数量", "运损计算方法", String.valueOf(diancxxb_id), "单车"));
				if(isDancYuns){
					sql = "select id from chepb where fahb_id=" + id + " and piz<>0";
					ResultSetList rscp = con.getResultSetList(sql);
					while(rscp.next()){
						flag = Jilcz.CountChepbYuns(con, rscp.getString("id"), SysConstant.HEDBZ_YJJ);
						if (flag == -1) {
							con.rollBack();
							con.Close();
							WriteLog.writeErrorLog("计算chepb运损失败");
							setMsg("计算chepb运损失败");
							return;
						}
					}
					rscp.close();
				}
				flag = Jilcz.updateFahb(con, id);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog("更新fahb失败");
					setMsg("更新fahb失败");
					return;
				}
//				如果不是单车运输，则计算批次运损
				if(!isDancYuns){
					flag = Jilcz.CountFahbYuns(con, id);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog("计算fahb运损失败");
						setMsg("计算fahb运损失败");
						return;
					}
				}
				
			
			}//if(con.getHasIt(sql))
			
//			将检斤后的fahb的id的daohrq更改为shoucrq
			sql = "update fahb set daohrq = " + shoucrq + " where id=" + id;
			con.getUpdate(sql);

			
		}//while(rsl.next())
		
		con.commit();
		con.Close();
		if (rsl.getRows() != 0) {
			setMsg(getQRiq(sShoucRiq) + "收车过程成功!");
		}else {
			setMsg(getQRiq(sShoucRiq) + "没有收车数据!");
		}
		rsl.close();
		

//		setMsg(ErrorMessage.SaveSuccessMessage);
		
//		计算成本
		Chengbjs.CountChengb(diancxxb_id, fhlist);
		
	}//shouc()



	// 收车方法 ---------------------------------------------------------------- 
	//  end------------------------

	private int UpdateLieId(JDBCcon con,String fahb_id){
		
		String sql=" select * from xitxxb where mingc='收车新发货lie_id更改' and zhi='是' and leib='数量' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(!rsl.next()){//没有配置 不需要更改
			return 1;
		}
		
		sql=" update fahb set lie_id=getnewid(diancxxb_id) where id="+fahb_id;
		int flag=con.getUpdate(sql);
		return flag;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _ShoucChick = false;

	public void ShoucButton(IRequestCycle cycle) {
		_ShoucChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			setSRiq(getStartRiq());
			setERiq(getEndRiq());
			getSelectData();
		} else if (_ShoucChick) {
			_ShoucChick = false;
			shouc();
			getSelectData();
		}
	}

	// 页面数据显示的方法。
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		// Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select sum(f.maoz) maoz,sum(f.piz) piz,sum(f.biaoz) biaoz,sum(jingz) jingz,sum(f.koud) koud,\n")
				.append(
						" sum(f.kous) kous,sum(f.kouz) kouz,sum(f.ches) ches,sum(c.yjcs) yjcs\n")
				.append(" from fahb f,\n")
				.append(
						" (select fahb_id,count(id) yjcs from chepb where maoz<>0 and piz <>0\n")
				.append(" and qingcsj >=").append(
						DateUtil.FormatOracleDate(getStartRiq())).append("\n")
				.append(" and qingcsj <").append(
						DateUtil.FormatOracleDate(getEndRiq())).append("+1")
				.append("\n").append(" group by fahb_id) c\n").append(
						" where f.id = c.fahb_id\n").append(
						" and f.daohrq = to_date('2050-12-31','yyyy-mm-dd')\n")
				.append(" group by daohrq\n");
//		System.out.println(sb);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
		egu.getColumn("piz").setHeader(Locale.piz_fahb);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("koud").setHeader(Locale.koud_fahb);
		egu.getColumn("kous").setHeader(Locale.kous_fahb);
		egu.getColumn("kouz").setHeader(Locale.kouz_fahb);
		egu.getColumn("ches").setHeader(Locale.ches_fahb);
		egu.getColumn("yjcs").setHeader("已检车数");

		egu.addTbarText("开始时间:");
		DateField dStart = new DateField();
		dStart.Binding("STARTRIQ", "");
		dStart.setValue(getStartRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("&nbsp&nbsp");
		egu.addTbarText("结束时间:");
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ", "");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);

		StringBuffer sShoucHandler = new StringBuffer();
		sShoucHandler.append(
				"function(){Ext.MessageBox.confirm('提示信息','是否进行"
						+ getQRiq(getSRiq()) + "收车操作？',").append(
				"function(btn){if (btn == 'yes') {").append(
				"document.getElementById('ShoucButton').click();").append("}}")
				.append(")}");

		GridButton gShouc = new GridButton("收车", sShoucHandler.toString());
		egu.addTbarBtn(gShouc);

		// egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		setExtGrid(egu);
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
		// if(getTbmsg()!=null) {
		// getExtGrid().addToolbarItem("'->'");
		// getExtGrid().addToolbarItem("'<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>'");
		// }
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
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
			init();
		}

	}

	private void init() {
		setStartRiq(DateUtil.FormatDate(new Date()));
		setEndRiq(DateUtil.FormatDate(new Date()));
		setSRiq(getStartRiq());
		setERiq(getEndRiq());
		getSelectData();
	}
}
