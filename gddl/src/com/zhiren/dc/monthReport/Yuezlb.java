package com.zhiren.dc.monthReport;

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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
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
/*
 * 作者：夏峥
 * 日期：2012-11-05
 * 描述：修正由于MainGlobal中getnewid方法引发的程序错误
 */
/*
 * 作者：夏峥
 * 日期：2012-03-16
 * 描述：修改显示和保存方法使其具有生成和修改功能。
 * 		 界面中显示汇总数据
 * 		 通过系统配置”月报质量自动计算累计“取得月报是否计算累计值的判定。
 */
/*
 * 作者：夏峥
 * 日期：2011-12-20
 * 描述：取消全部界面自动刷新功能，用户需手动点击刷新按钮才可刷新数据
 */
/*
 * 作者：夏峥
 * 时间：2011-08-02
 * 适用范围：国电电力
 * 描述：保存时，取净重作为来煤的依据和加权方式
 * 		生成时，取发货表中的净重作为本月数量依据，
 * 		生成时，当取累计值时，使用数量表中的净重作为数量依据
 */

/*
 * 作者：夏峥
 * 时间：2011-07-22
 * 适用范围：国电电力
 * 描述：修正生成方法，如果矿方化验值为空，则值为0
 */
/*
 * 作者：夏峥
 * 时间：2011-06-22
 * 适用范围：国电电力
 * 描述：增加矿方空干基灰分列及其对应的保存方法
 */
/*
 * 作者:tzf
 * 时间:2010-03-12
 * 修改内容: 增加 折价金额 各指标(水分 灰分等)。
 */
/*
 * 作者:tzf
 * 时间:2010-03-09
 * 修改内容: 调整 索赔数量  不累计的问题。
 */
/*
 * 作者：王磊
 * 时间：2009-12-09
 * 描述：修改大唐国际月质量表生成时SQL关联错误
 */
/*
 * 作者：王磊
 * 时间：2009-11-09
 * 描述：增加月报生成系统参数的判断 默认为ZGDT
 * mingc = '月报单位'
 * zhi = 'DTGJ'
 * diancxxb_id = 电厂ID
 * leib = '月报'
 */
/*
 * 作者：王磊
 * 时间：2009-10-19
 * 描述：修改供应商煤矿新关联关系
 */
/*
 * 作者：王磊
 * 时间：2009-08-12 11：42
 * 描述：修改月质量表删除时未判断电厂ID 的问题
 */
/*
 * 作者：王磊
 * 时间：2009-07-30 14：49
 * 描述：增加数量月报取数的日期设置 
 * insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * values(getnewid(232),1,232,'月报取数日期差','-2','','月报',1,'')
 */
/*
 * 作者：王磊
 * 时间：2009-06-03 10:07
 * 描述：判断是否有索赔的方法 由 盈亏<0  改为 折价金额<0
 * 		11:29
 * 		修改硫索赔数量取自结算表结算数量字段
 * 		13:28
 * 		修改同一张结算单关联多个发货时造成数据重复累加的问题
 */
/*
 * 作者：赵胜男
 * 日期：2013-01-11
 * 描述：调整界面不可编辑列显示方式；
 *				化验值信息加入最小值限制,最小值大于0.
 */
/*
 * 作者：夏峥
 * 日期：2013-04-03
 * 描述：处理关于Mt显示的BUG
 */
public class Yuezlb extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		int flag ;
		StringBuffer sql = new StringBuffer();
		
		String str_v = MainGlobal.getXitxx_item("月报", "调燃03表中挥发份类型", getTreeid(), "VDAF");
		String str_value_vdaf="";
		String str_value_vad="";

		while (rsl.next()){
			if("VDAF".equals(str_v)){
				str_value_vdaf = rsl.getDouble("VDAF") + "";
				str_value_vad= "round_new(" +rsl.getDouble("vdaf")/100*(100-rsl.getDouble("mad")-rsl.getDouble("aad"))+    ",2)"  ;
			}else{
				str_value_vdaf = "round_new(" + rsl.getDouble("VAD")*100/(100-rsl.getDouble("AAD")-rsl.getDouble("MAD")) + ",2)";
				str_value_vad = rsl.getDouble("vad")+"";
			}
			
			if(MainGlobal.getXitxx_item("月报", "月报质量自动计算累计", getTreeid(), "是").equals("是")){
				if(rsl.getString("FENX").equals("累计")){
					continue;
				}
//				保存本月数据
				sql.delete(0, sql.length());
				sql.append("update yuezlb set yuetjkjb_id=" + rsl.getString("yuetjkjb_id") +
						",QNET_AR=" + rsl.getDouble("QNET_AR") + 
						",MT=" + rsl.getDouble("MT") + 
						",MAD=" + rsl.getDouble("MAD") +
						",AAR=" + rsl.getDouble("AAR") + 
						",AAD=" + rsl.getDouble("AAD") + 
						",AD=" + rsl.getDouble("AD") + 
						",VDAF=" + str_value_vdaf + 
						",vad=" + str_value_vad + 
						",STD="	+ rsl.getDouble("STD") + 
						",HAD="	+ rsl.getDouble("HAD") + 
						",FCAD=" + rsl.getDouble("FCAD") + 
						",QNET_AR_KF="+ rsl.getDouble("QNET_AR_KF") + 
						",MT_KF="+ rsl.getDouble("MT_KF") + 
						",MAD_KF="+ rsl.getDouble("MAD_KF") + 
						",AAR_KF="+ rsl.getDouble("AAR_KF") + 
						",AAD_KF="+ rsl.getDouble("AAD_KF") + 
						",AD_KF="+ rsl.getDouble("AD_KF") + 
						",VDAF_KF="	+ rsl.getDouble("VDAF_KF") + 
						",STD_KF="+ rsl.getDouble("STD_KF") + 
						",HAD_KF="+ rsl.getDouble("HAD_KF") + 
						",FCAD_KF="	+ rsl.getDouble("FCAD_KF") + 
						",zhijbfml="+ rsl.getDouble("zhijbfml") + 
						",zhijbfje="+ rsl.getDouble("zhijbfje") + 
						",zhijbfje_m="+ rsl.getDouble("zhijbfje_m") + 
						",zhijbfje_a="+ rsl.getDouble("zhijbfje_a") + 
						",zhijbfje_v="	+ rsl.getDouble("zhijbfje_v") + 
						",zhijbfje_q="+ rsl.getDouble("zhijbfje_q") + 
						",zhijbfje_s="+ rsl.getDouble("zhijbfje_s") + 
						",zhijbfje_t="	+ rsl.getDouble("zhijbfje_t") + 
						",suopje="+ rsl.getDouble("suopje") + 
						",lsuopsl="+ rsl.getDouble("lsuopsl") + 
						",lsuopje="+ rsl.getDouble("lsuopje") + 
						" where id=" + rsl.getLong("id") + "\n");
					flag = con.getUpdate(sql.toString());
					if(flag == -1){
						WriteLog.writeErrorLog(this.getClass().getName() + "\n"
								+ ErrorMessage.UpdateYuezlbFailed + "\nSQL:" + sql.toString());
						setMsg(ErrorMessage.UpdateYuezlbFailed);
						con.rollBack();
						con.Close();
						return;
					}
					
				//保存累计数据
				Visit visit = (Visit) getPage().getVisit();
				String sj[] = this.getRiq2().split("-");
				String BeginDate = "";
				String EndDate = "";
				if(visit.getString10().equals(strParam)){
					EndDate=DateUtil.FormatOracleDate(sj[0] + "-" + sj[1]+ "-01");
					BeginDate=DateUtil.FormatOracleDate(sj[0] + "-01-01");
				}else{
					EndDate = DateUtil.FormatOracleDate(getNianf() + "-"+ getYuef() + "-01");
					BeginDate=DateUtil.FormatOracleDate(getNianf() + "-01-01");
				}
				
				String sq = "select\n" +
				"round_new(decode(sum(jingz),0,0,sum(jingz*qnet_ar)/sum(jingz)),2) qnet_ar,\n" +
				"round_new(decode(sum(jingz),0,0,sum(jingz*mt)/sum(jingz)),2) mt,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*mad)/sum(jingz)),2) mad,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*aar)/sum(jingz)),2) aar,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*aad)/sum(jingz)),2) aad,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*ad)/sum(jingz)),2) ad,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*vdaf)/sum(jingz)),2) vdaf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*vad)/sum(jingz)),2) vad,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*std)/sum(jingz)),2) std,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*had)/sum(jingz)),2) had,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*fcad)/sum(jingz)),2) fcad,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*qnet_ar_kf)/sum(jingz)),2) qnet_ar_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*mt_kf)/sum(jingz)),2) mt_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*mad_kf)/sum(jingz)),2) mad_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*aar_kf)/sum(jingz)),2) aar_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*aad_kf)/sum(jingz)),2) aad_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*ad_kf)/sum(jingz)),2) ad_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*vdaf_kf)/sum(jingz)),2) vdaf_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*std_kf)/sum(jingz)),2) std_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*had_kf)/sum(jingz)),2) had_kf,\n" + 
				"round_new(decode(sum(jingz),0,0,sum(jingz*fcad_kf)/sum(jingz)),2) fcad_kf,\n" + 
				"sum(zhijbfml)zhijbfml,sum(zhijbfje)zhijbfje,\n" + 
				"sum(zhijbfje_m)zhijbfje_m,sum(zhijbfje_a)zhijbfje_a,sum(zhijbfje_v)zhijbfje_v," +
				"sum(zhijbfje_q)zhijbfje_q,sum(zhijbfje_s)zhijbfje_s,sum(zhijbfje_t)zhijbfje_t,\n" + 
				"sum(y.suopje)suopje,sum(y.lsuopsl)lsuopsl,sum(y.lsuopje)lsuopje\n" + 
				"  from yuezlb y,yueslb s,yuetjkjb yt,(select gongysb_id,jihkjb_iD,pinzb_id,yunsfsb_id from yuetjkjb where id= " + rsl.getString("YUETJKJB_ID") + ")yt2\n" + 
				" where y.yuetjkjb_id=yt.id\n" + 
				"	and y.yuetjkjb_id=s.yuetjkjb_id\n" +
				"	and yt.gongysb_id=yt2.gongysb_id\n" +
				"	and yt.jihkjb_id=yt2.jihkjb_id\n" +
				"	and yt.pinzb_id=yt2.pinzb_id\n" +
				"	and yt.yunsfsb_id=yt2.yunsfsb_id\n" +
				"   and yt.riq>="+BeginDate+"\n" + 
				"   and yt.riq<="+EndDate+"\n" + 
				"   and y.fenx='" + SysConstant.Fenx_Beny + "'\n" +
				"   and s.fenx='" + SysConstant.Fenx_Beny + "'\n" + 
				"   and yt.diancxxb_id=" + getTreeid();
			ResultSetList rs = con.getResultSetList(sq);
				if(rs.next()){
					sql.delete(0, sql.length());
					sql.append("update yuezlb set " +
							"QNET_AR=" + rs.getDouble("QNET_AR") + 
							",MT=" + rs.getDouble("MT") + 
							",MAD=" + rs.getDouble("MAD") +
							",AAR=" + rs.getDouble("AAR") + 
							",AAD=" + rs.getDouble("AAD") + 
							",AD=" + rs.getDouble("AD") + 
							",VDAF=" + rs.getDouble("VDAF") + 
							",vad=" + rs.getDouble("vad") + 
							",STD="	+ rs.getDouble("STD") + 
							",HAD="	+ rs.getDouble("HAD") + 
							",FCAD=" + rs.getDouble("FCAD") + 
							",QNET_AR_KF="+ rs.getDouble("QNET_AR_KF") + 
							",MT_KF="+ rs.getDouble("MT_KF") + 
							",MAD_KF="+ rs.getDouble("MAD_KF") + 
							",AAR_KF="+ rs.getDouble("AAR_KF") + 
							",AAD_KF="+ rs.getDouble("AAD_KF") + 
							",AD_KF="+ rs.getDouble("AD_KF") + 
							",VDAF_KF="	+ rs.getDouble("VDAF_KF") + 
							",STD_KF="+ rs.getDouble("STD_KF") + 
							",HAD_KF="+ rs.getDouble("HAD_KF") + 
							",FCAD_KF="	+ rs.getDouble("FCAD_KF") + 
							",zhijbfml="+ rs.getDouble("zhijbfml") + 
							",zhijbfje="+ rs.getDouble("zhijbfje") + 
							",zhijbfje_m="+ rs.getDouble("zhijbfje_m") + 
							",zhijbfje_a="+ rs.getDouble("zhijbfje_a") + 
							",zhijbfje_v="	+ rs.getDouble("zhijbfje_v") + 
							",zhijbfje_q="+ rs.getDouble("zhijbfje_q") + 
							",zhijbfje_s="+ rs.getDouble("zhijbfje_s") + 
							",zhijbfje_t="	+ rs.getDouble("zhijbfje_t") + 
							",suopje="+ rs.getDouble("suopje") + 
							",lsuopsl="+ rs.getDouble("lsuopsl") + 
							",lsuopje="+ rs.getDouble("lsuopje") + 
							" where yuetjkjb_id =" + rsl.getString("YUETJKJB_ID")+
							" and fenx = '" + SysConstant.Fenx_Leij + "'");
					flag = con.getUpdate(sql.toString());
					if (flag == -1) {
						WriteLog.writeErrorLog(this.getClass().getName() + "\n"
								+ ErrorMessage.UpdateYuezlbFailed + "\nSQL:" + sql.toString());
						setMsg(ErrorMessage.UpdateYuezlbFailed);
						con.rollBack();
						con.Close();
						return;
					}
				}
				rs.close();
			}else{
				sql.delete(0, sql.length());
				sql.append("update yuezlb set yuetjkjb_id=" + rsl.getString("yuetjkjb_id") +
						",QNET_AR=" + rsl.getDouble("QNET_AR") + 
						",MT=" + rsl.getDouble("MT") + 
						",MAD=" + rsl.getDouble("MAD") +
						",AAR=" + rsl.getDouble("AAR") + 
						",AAD=" + rsl.getDouble("AAD") + 
						",AD=" + rsl.getDouble("AD") + 
						",VDAF=" + str_value_vdaf + 
						",vad=" + str_value_vad + 
						",STD="	+ rsl.getDouble("STD") + 
						",HAD="	+ rsl.getDouble("HAD") + 
						",FCAD=" + rsl.getDouble("FCAD") + 
						",QNET_AR_KF="+ rsl.getDouble("QNET_AR_KF") + 
						",MT_KF="+ rsl.getDouble("MT_KF") + 
						",MAD_KF="+ rsl.getDouble("MAD_KF") + 
						",AAR_KF="+ rsl.getDouble("AAR_KF") + 
						",AAD_KF="+ rsl.getDouble("AAD_KF") + 
						",AD_KF="+ rsl.getDouble("AD_KF") + 
						",VDAF_KF="	+ rsl.getDouble("VDAF_KF") + 
						",STD_KF="+ rsl.getDouble("STD_KF") + 
						",HAD_KF="+ rsl.getDouble("HAD_KF") + 
						",FCAD_KF="	+ rsl.getDouble("FCAD_KF") + 
						",zhijbfml="+ rsl.getDouble("zhijbfml") + 
						",zhijbfje="+ rsl.getDouble("zhijbfje") + 
						",zhijbfje_m="+ rsl.getDouble("zhijbfje_m") + 
						",zhijbfje_a="+ rsl.getDouble("zhijbfje_a") + 
						",zhijbfje_v="	+ rsl.getDouble("zhijbfje_v") + 
						",zhijbfje_q="+ rsl.getDouble("zhijbfje_q") + 
						",zhijbfje_s="+ rsl.getDouble("zhijbfje_s") + 
						",zhijbfje_t="	+ rsl.getDouble("zhijbfje_t") + 
						",suopje="+ rsl.getDouble("suopje") + 
						",lsuopsl="+ rsl.getDouble("lsuopsl") + 
						",lsuopje="+ rsl.getDouble("lsuopje") + 
						" where id=" + rsl.getLong("id") + "\n");
				
					flag = con.getUpdate(sql.toString());
					if(flag == -1){
						WriteLog.writeErrorLog(this.getClass().getName() + "\n"
								+ ErrorMessage.UpdateYuezlbFailed + "\nSQL:" + sql.toString());
						setMsg(ErrorMessage.UpdateYuezlbFailed);
						con.rollBack();
						con.Close();
						return;
					}
			}
		}
		rsl.close();
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
	}
	public void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sj[] = this.getRiq2().split("-");
		String CurrZnDate = "";
		String CurrODate = "";
		if(visit.getString10().equals(strParam)){
			CurrZnDate = sj[0] + "年" + sj[1] + "月";
			CurrODate=DateUtil.FormatOracleDate(sj[0] + "-" + sj[1]+ "-01");
		}else{
			CurrZnDate = getNianf() + "年" + getYuef() + "月";
			CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"+ getYuef() + "-01");
		}
		String strSql=
			"delete from yuezlb where yuetjkjb_id in (select id from yuetjkjb where riq="
			+CurrODate+" and diancxxb_id="+getTreeid()+")";
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("删除过程中发生错误！");
		}else {
			setMsg(CurrZnDate+"的数据被成功删除！");
		}
		con.Close();
	}
	public String InsertYuezlbSql(String diancxxb_id, long yuetjkjb_id, String fenx, double Qnetar,
			double Qbad, double Mt, double Aar, double Vdaf, double Std, double Had,
			double Mad, double Aad, double Ad, double Vad, double Var, double Stad,
			double Sdaf, double Hdaf, double Qgrad, double Qgrad_daf, double Fcad,
			double kQnetar, double kQbad, double kMt, double kAar, double kVdaf, 
			double kStd, double kHad, double kMad, double kAad, double kAd, double kVad, 
			double kVar, double kStad, double kSdaf, double kHdaf, double kQgrad, 
			double kQgrad_daf, double kFcad, double zhijbfml, double zhijbfje, 
			double suopje, double liuspsl, double liuspje,double ZHIJBFJE_M,double ZHIJBFJE_A,
			double ZHIJBFJE_V,double ZHIJBFJE_Q,double ZHIJBFJE_S,double ZHIJBFJE_T
			){
		String sql = "insert into yuezlb (id,yuetjkjb_id,fenx,qnet_ar,qbad,mt,aar," +
				"vdaf,std,had,mad,aad,ad,vad,var,stad,sdaf,hdaf,qbrad,qgrad_daf," +
				"fcad,qnet_ar_kf,qbad_kf,mt_kf,aar_kf,vdaf_kf,std_kf,had_kf,mad_kf," +
				"aad_kf,ad_kf,vad_kf,var_kf,stad_kf,sdaf_kf,hdaf_kf,qbrad_kf," +
				"qgrad_daf_kf,fcad_kf,zhijbfml,zhijbfje,suopje,lsuopsl,lsuopje,ZHIJBFJE_M,ZHIJBFJE_A,ZHIJBFJE_V,ZHIJBFJE_Q,ZHIJBFJE_S,ZHIJBFJE_T) " +
				"values(getnewid("+diancxxb_id+")"
				+ "," + yuetjkjb_id + ",'" + fenx + "'," + Qnetar + "," + Qbad +
				"," + Mt + "," + Aar + "," + Vdaf + "," + Std + "," + Had + "," +
				Mad + "," + Aad + "," + Ad + "," + Vad + "," + Var + "," + Stad +
				"," + Sdaf + "," + Hdaf + "," + Qgrad + "," + Qgrad_daf + "," +
				Fcad + "," + kQnetar + "," + kQbad + "," + kMt + "," + kAar + "," + 
				kVdaf + "," + kStd + "," + kHad + "," + kMad + "," + kAad + "," + 
				kAd + "," + kVad + "," + kVar + "," + kStad + "," + kSdaf + "," + 
				kHdaf + "," + kQgrad + "," + kQgrad_daf + "," + kFcad + "," + 
				zhijbfml + "," + zhijbfje + "," + suopje + "," + liuspsl + "," + 
				liuspje +","+ZHIJBFJE_M+","+ZHIJBFJE_A+","+ZHIJBFJE_V+","+ZHIJBFJE_Q+","+ZHIJBFJE_S+","+ZHIJBFJE_T+ ")";
		return sql;
	}
	public void CreateData() {
		Visit visit = (Visit) getPage().getVisit();
		
		String diancxxb_id = getTreeid();//读取设置、初始化统计口径用
		String sjdiancxxb_id = "";       //查询数据用
		
//		大同：生成月报时，二期、三期、洗煤厂的数据统计到二期。
		sjdiancxxb_id = MainGlobal.getXitxx_item("月报", "月报数据多厂合并", diancxxb_id, diancxxb_id);

		
		String sj [] = this.getRiq2().split("-");
		String CurrZnDate = "";
		Date cd = null;
		int intYuef = 0;Integer.parseInt(getYuef());
		if(visit.getString10().equals(strParam)){
			CurrZnDate = sj[0] + "年" + sj[1] + "月" ;
		    cd = DateUtil.getDate(sj[0] + "-"
					+ sj[1] + "-01");
		    intYuef=Integer.parseInt(sj[1]);
		}else{
			  CurrZnDate=getNianf()+"年"+getYuef()+"月";
			  cd = DateUtil.getDate(getNianf() + "-"
						+ getYuef() + "-01");
			  intYuef = Integer.parseInt(getYuef());
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);

		String CurrODate = DateUtil.FormatOracleDate(cd);
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		int flag;
		long lngId=0;
		String date_c = MainGlobal.getXitxx_item("月报", "月报取数日期差", diancxxb_id, "0");
		String sql = "delete from yuezlb where yuetjkjb_id in "
			+ "(select id from yuetjkjb where riq = " + CurrODate
			+ " and diancxxb_id = " + diancxxb_id + ")";
		flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.DeleteYuezlbFailed + "\nSQL:" + sql);
			setMsg(ErrorMessage.DeleteYuezlbFailed);
			con.rollBack();
			con.Close();
			return;
		}
		
		sql = "select * from yuetjkjb where riq = " + CurrODate + 
		" and diancxxb_id = " + diancxxb_id;
		ResultSetList rs = con.getResultSetList(sql);
		if (rs == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			con.rollBack();
			con.Close();
			return;
		}
		
		String Zhijbf = MainGlobal.getXitxx_item("月报", "计算质价不符", String.valueOf(diancxxb_id), "否");
		
		while(rs.next()){
			lngId = rs.getLong("id");
			long gongysb_id = rs.getLong("gongysb_id");
			long jihkjb_id = rs.getLong("jihkjb_id");
			long pinzb_id = rs.getLong("pinzb_id");
			long yunsfsb_id = rs.getLong("yunsfsb_id");
			double suopje=0.0,liuspsl=0.0,liuspje=0.0,zhijbfml=0.0,zhijbfje=0.0,
			qnet_ar = 0.0,aar = 0.0,ad = 0.0,vdaf = 0.0,mt = 0.0,stad = 0.0,
			aad = 0.0,mad = 0.0,qbad = 0.0,had = 0.0,vad = 0.0,fcad = 0.0,std = 0.0,
			qgrad = 0.0,hdaf = 0.0,qgrad_daf = 0.0,sdaf = 0.0,var = 0.0,kqnet_ar = 0.0,
			kaar = 0.0,kad = 0.0,kvdaf = 0.0,kmt = 0.0,kstad = 0.0,kaad = 0.0,
			kmad = 0.0,kqbad = 0.0,khad = 0.0,kvad = 0.0,kfcad = 0.0,kstd = 0.0,
			kqgrad = 0.0,khdaf = 0.0,kqgrad_daf = 0.0,ksdaf = 0.0,kvar = 0.0,
			laimslby=0.0,laimsllj;
			String type=MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(diancxxb_id), "ZGDT");
			String model = MainGlobal.getXitxx_item("月报", "月报单位", String
					.valueOf(diancxxb_id), "ZGDT");
			
			 if(type.equals("JZRD")||model.equals("DTGJ")){
				 sql = " select\n" + 
				 "a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id,sum(a.laimsl) laimsl,\n" + 
				 "sum(zhijbfml) zhijbfml,sum(zhijbfje) zhijbfje,decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qnet_ar)/sum(a.laimsl),2)) as qnet_ar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aar)/sum(a.laimsl),2)) as aar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.ad)/sum(a.laimsl),2)) as ad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vdaf)/sum(a.laimsl),2)) as vdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mt)/sum(a.laimsl),1)) as mt,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.stad)/sum(a.laimsl),2)) as stad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aad)/sum(a.laimsl),2)) as aad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mad)/sum(a.laimsl),2)) as mad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qbad)/sum(a.laimsl),2)) as qbad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.had)/sum(a.laimsl),2)) as had,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vad)/sum(a.laimsl),2)) as vad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.fcad)/sum(a.laimsl),2)) as fcad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.std)/sum(a.laimsl),2)) as std,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad)/sum(a.laimsl),2)) as qgrad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.hdaf)/sum(a.laimsl),2)) as hdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad_daf)/sum(a.laimsl),2)) as qgrad_daf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.sdaf)/sum(a.laimsl),2)) as sdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.var)/sum(a.laimsl),2)) as var,\n" + 
				 "\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqnet_ar)/sum(a.laimsl),2)) as kqnet_ar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kaar)/sum(a.laimsl),2)) as kaar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kad)/sum(a.laimsl),2)) as kad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kvdaf)/sum(a.laimsl),2)) as kvdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kmt)/sum(a.laimsl),1)) as kmt,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kstad)/sum(a.laimsl),2)) as kstad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kaad)/sum(a.laimsl),2)) as kaad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kmad)/sum(a.laimsl),2)) as kmad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqbad)/sum(a.laimsl),2)) as kqbad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.khad)/sum(a.laimsl),2)) as khad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kvad)/sum(a.laimsl),2)) as kvad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kfcad)/sum(a.laimsl),2)) as kfcad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kstd)/sum(a.laimsl),2)) as kstd,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqgrad)/sum(a.laimsl),2)) as kqgrad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.khdaf)/sum(a.laimsl),2)) as khdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqgrad_daf)/sum(a.laimsl),2)) as kqgrad_daf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.ksdaf)/sum(a.laimsl),2)) as ksdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kvar)/sum(a.laimsl),2)) as kvar\n" + 
				 "from (\n" + 
				 "select g.meikdq_id dqid, f.jihkjb_id, f.pinzb_id, f.yunsfsb_id,\n" + 
				 "  f.laimsl,z.*,k.qnet_ar kqnet_ar,0 kaar,0 kad,\n" + 
				 "  0 kvdaf, 0 kmt, 0 kstad, 0 kaad,\n" + 
				 "  0 kmad, 0 kqbad, 0 khad, 0 kvad,\n" + 
				 "  0 kfcad, k.std kstd, 0 kqgrad, 0 khdaf,\n" + 
				 "  0 kqgrad_daf, 0 ksdaf, 0 kvar,\n" + 
				 "getzhijbf(f.id,1) zhijbfml,\n" + 
				 "getzhijbf(f.id,2)*getzhijbf(f.id,1) zhijbfje\n" + 
				 "from fahb f, zhilb z, (\n" + 
				 "select hetb_id,sum(xiax)qnet_ar,sum(std)std\n" + 
				 "from(\n" + 
				 "select hetb_id,decode(danwb.bianm,'千卡千克',round_new(xiax*0.0041816,3),xiax)xiax ,0 std\n" + 
				 "from hetzlb,danwb\n" + 
				 "where hetzlb.danwb_id=danwb.id and hetzlb.zhibb_id=2\n" + 
				 "union\n" + 
				 "select hetb_id,0 ,shangx\n" + 
				 "from hetzlb\n" + 
				 "where  hetzlb.zhibb_id=3)\n" + 
				 "group by hetb_id\n" + 
				 ") k, meikxxb g\n" + 
				 "where f.zhilb_id = z.id\n" + 
				 "and f.hetb_id = k.hetb_id(+) and f.meikxxb_id = g.id\n" + 
				 "and f.diancxxb_id = " + diancxxb_id + " and g.meikdq_id =" + gongysb_id +
					" and f.jihkjb_id = " + jihkjb_id + " and f.pinzb_id =" + pinzb_id +
					" and f.daohrq >=to_date('"+this.getRiq1()+"','yyyy-MM-dd') and f.daohrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')" +
					" and f.yunsfsb_id =" + yunsfsb_id +
					") a\n" + 
					"group by a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id\n" + 
					"";
			 }else{
				 sql = 
						"select\n" +
						"a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id,sum(a.laimsl) laimsl,\n" + 
						"sum(zhijbfml) zhijbfml,sum(zhijbfje) zhijbfje," + 
						"sum(relzj_zj) relzj_zj,sum(liuzj_zj) liuzj_zj,sum(huifzj_zj) huifzj_zj,\n"+
						"sum(shuifzj_zj) shuifzj_zj,sum(huiffzj_zj) huiffzj_zj,sum(huirdzj_zj) huirdzj_zj,\n"+
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qnet_ar)/sum(a.laimsl),2)) as qnet_ar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aar)/sum(a.laimsl),2)) as aar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.ad)/sum(a.laimsl),2)) as ad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vdaf)/sum(a.laimsl),2)) as vdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mt)/sum(a.laimsl),1)) as mt,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.stad)/sum(a.laimsl),2)) as stad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aad)/sum(a.laimsl),2)) as aad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mad)/sum(a.laimsl),2)) as mad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qbad)/sum(a.laimsl),2)) as qbad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.had)/sum(a.laimsl),2)) as had,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vad)/sum(a.laimsl),2)) as vad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.fcad)/sum(a.laimsl),2)) as fcad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.std)/sum(a.laimsl),2)) as std,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad)/sum(a.laimsl),2)) as qgrad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.hdaf)/sum(a.laimsl),2)) as hdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad_daf)/sum(a.laimsl),2)) as qgrad_daf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.sdaf)/sum(a.laimsl),2)) as sdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.var)/sum(a.laimsl),2)) as var,\n" + 
//						如果矿方化验值为空，则值为0
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqnet_ar,null,0,a.kqnet_ar))/sum(a.laimsl),2)) as kqnet_ar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kaar,null,0,a.kaar))/sum(a.laimsl),2)) as kaar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kad,null,0,a.kad))/sum(a.laimsl),2)) as kad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvdaf,null,0,a.kvdaf))/sum(a.laimsl),2)) as kvdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kmt,null,0,a.kmt))/sum(a.laimsl),1)) as kmt,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kstad,null,0,a.kstad))/sum(a.laimsl),2)) as kstad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kaad,null,0,a.kaad))/sum(a.laimsl),2)) as kaad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kmad,null,0,a.kmad))/sum(a.laimsl),2)) as kmad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqbad,null,0,a.kqbad))/sum(a.laimsl),2)) as kqbad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.khad,null,0,a.khad))/sum(a.laimsl),2)) as khad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvad,null,0,a.kvad))/sum(a.laimsl),2)) as kvad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kfcad,null,0,a.kfcad))/sum(a.laimsl),2)) as kfcad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kstd,null,0,a.kstd))/sum(a.laimsl),2)) as kstd,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqgrad,null,0,a.kqgrad))/sum(a.laimsl),2)) as kqgrad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.khdaf,null,0,a.khdaf))/sum(a.laimsl),2)) as khdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqgrad_daf,null,0,a.kqgrad_daf))/sum(a.laimsl),2)) as kqgrad_daf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.ksdaf,null,0,a.ksdaf))/sum(a.laimsl),2)) as ksdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvar,null,0,a.kvar))/sum(a.laimsl),2)) as kvar\n" + 
//						取发货表中的净重作为本月数量依据
						"from (select g.meikdq_id dqid, f.jihkjb_id, f.pinzb_id, f.yunsfsb_id,\n" + 
						"  f.jingz laimsl,z.*,k.qnet_ar kqnet_ar, k.aar kaar, k.ad kad,\n" + 
						"  k.vdaf kvdaf, k.mt kmt, k.stad kstad, k.aad kaad,\n" + 
						"  k.mad kmad, k.qbad kqbad, k.had khad, k.vad kvad,\n" + 
						"  k.fcad kfcad, k.std kstd, k.qgrad kqgrad, k.hdaf khdaf,\n" + 
						"  k.qgrad_daf kqgrad_daf, k.sdaf ksdaf, k.var kvar,\n"; 
						
						if(Zhijbf.equals("否")){
							sql+= "  0 as	zhijbfml,	\n"	+
								"  0 as	zhijbfje, 	\n" +
								"  0 as relzj_zj	\n";
						}else if(Zhijbf.equals("是")){
							
							sql+="  nvl(case when (getgusxx(f.id,'hetzk') < 0) then f.biaoz end,0) zhijbfml,\n" + 
								"  abs(case when getgusxx(f.id,'hetzk') < 0 then getgusxx(f.id,'hetzk') * f.biaoz end) zhijbfje	\n" + 
								", abs(case when getgusxx(f.id,'hetzk') < 0 then getgusxx(f.id,'hetzk') * f.biaoz end) relzj_zj	\n";
						}
						
						sql+=",0 liuzj_zj\n"+
							",0 huifzj_zj\n"+
							",0 shuifzj_zj\n"+
							",0 huiffzj_zj\n"+
							",0 huirdzj_zj\n"+
							
							"from fahb f, zhilb z,kuangfzlb k, meikxxb g\n" + 
							"where f.zhilb_id = z.id \n" + 
							"and f.kuangfzlb_id = k.id(+) and f.meikxxb_id = g.id\n" + 
							"and f.diancxxb_id in( " + sjdiancxxb_id + ") and g.meikdq_id =" + gongysb_id +
							" and f.jihkjb_id = " + jihkjb_id + " and f.pinzb_id =" + pinzb_id +
							" and f.daohrq >=to_date('"+this.getRiq1()+"','yyyy-MM-dd')-" + date_c + "\n" +
							" and f.daohrq <=to_date('"+this.getRiq2()+"','yyyy-MM-dd')-" + date_c + "\n" +
							" and f.yunsfsb_id =" + yunsfsb_id +
							") a\n" + 
							"group by a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id\n" + 
							"";
			 }
			ResultSetList datars = con.getResultSetList(sql);
			
			double relzj_zj=0.0;double liuzj_zj=0.0;double huifzj_zj=0.0;
			double shuifzj_zj=0.0;double huiffzj_zj=0.0;double huirdzj_zj=0.0;
			
			if(datars.next()){
				zhijbfml = datars.getDouble("zhijbfml"); zhijbfje = datars.getDouble("zhijbfje");
				qnet_ar = datars.getDouble("qnet_ar"); kqnet_ar = datars.getDouble("kqnet_ar");
				aar = datars.getDouble("aar"); kaar = datars.getDouble("kaar");
				ad = datars.getDouble("ad"); kad = datars.getDouble("kad");
				vdaf = datars.getDouble("vdaf"); kvdaf = datars.getDouble("kvdaf");
				mt = datars.getDouble("mt"); kmt = datars.getDouble("kmt");
				stad = datars.getDouble("stad"); kstad = datars.getDouble("kstad");
				aad = datars.getDouble("aad"); kaad = datars.getDouble("kaad");
				mad = datars.getDouble("mad"); kmad = datars.getDouble("kmad");
				qbad = datars.getDouble("qbad"); kqbad = datars.getDouble("kqbad");
				had = datars.getDouble("had"); khad = datars.getDouble("khad");
				vad = datars.getDouble("vad"); kvad = datars.getDouble("kvad");
				fcad = datars.getDouble("fcad"); kfcad = datars.getDouble("kfcad");
				std = datars.getDouble("std"); kstd = datars.getDouble("kstd");
				qgrad = datars.getDouble("qgrad"); kqgrad = datars.getDouble("kqgrad");
				hdaf = datars.getDouble("hdaf"); khdaf = datars.getDouble("khdaf");
				qgrad_daf = datars.getDouble("qgrad_daf"); kqgrad_daf = datars.getDouble("kqgrad_daf");
				sdaf = datars.getDouble("sdaf"); ksdaf = datars.getDouble("ksdaf");
				var = datars.getDouble("var"); kvar = datars.getDouble("kvar");
				laimslby = datars.getDouble("laimsl");
				relzj_zj=datars.getDouble("relzj_zj");
				liuzj_zj=datars.getDouble("liuzj_zj");
				huifzj_zj=datars.getDouble("huifzj_zj");
				shuifzj_zj=datars.getDouble("shuifzj_zj");
				huiffzj_zj=datars.getDouble("huiffzj_zj");
				huirdzj_zj=datars.getDouble("huirdzj_zj");
				
			}
			datars.close();
			sql =
				"select nvl(sum(relzjje + quanszjje + huifzjje + huiffzjje),0) zhejje,\n" +
				"round(nvl(sum(liuspsl),0),0) liuspsl,nvl(sum(liuspje),0) liuspje\n" + 
				"from (select j.id,\n" + 
				"nvl(sum(case when z.bianm = 'Qnetar' and s.zhejje <0 then abs(s.zhejje) end),0) relzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Mt' and s.zhejje <0 then abs(s.zhejje) end),0) quanszjje,\n" + 
				"nvl(sum(case when z.bianm = 'Aar' and s.zhejje <0 then abs(s.zhejje) end),0) huifzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Vdaf' and s.zhejje <0 then abs(s.zhejje) end),0) huiffzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Std' and s.zhejje <0 then abs(j.jiessl) end),0) liuspsl,\n" + 
				"nvl(sum(case when z.bianm = 'Std' and s.zhejje <0 then abs(s.zhejje) end),0) liuspje\n" + 
				"from jiesb j, jieszbsjb s, zhibb z\n" + 
				"where j.id = s.jiesdid and s.zhibb_id = z.id\n" + 
				"and j.ruzrq >=to_date('"+this.getRiq1()+"','yyyy-MM-dd')-"+date_c+"\n" + 
				"and j.ruzrq <=to_date('"+this.getRiq2()+"','yyyy-MM-dd')-"+date_c+"\n" + 
				"group by j.id) js, (select distinct f.jiesb_id from fahb f, meikxxb g\n" + 
				"where f.meikxxb_id = g.id\n" + 
				" and f.diancxxb_id in(" + sjdiancxxb_id + ")\n" +
				" and g.meikdq_id = " + gongysb_id +
				" and f.jihkjb_id = " + jihkjb_id +
				" and f.yunsfsb_id = " + yunsfsb_id +
				" and f.pinzb_id = " + pinzb_id +
				" ) fh where fh.jiesb_id = js.id";
			datars = con.getResultSetList(sql);
			if(datars.next()){
				suopje = datars.getDouble("zhejje");
				liuspsl = datars.getDouble("liuspsl");
				liuspje = datars.getDouble("liuspje");
			}
			datars.close();
			sql = InsertYuezlbSql(diancxxb_id, lngId, SysConstant.Fenx_Beny,qnet_ar,qbad,mt,aar,vdaf,std,had,mad,
			aad,ad,vad,var,stad,sdaf,hdaf,qgrad,qgrad_daf,fcad,kqnet_ar,kqbad,kmt,
			kaar,kvdaf,kstd,khad,kmad,kaad,kad,kvad,kvar,kstad,ksdaf,khdaf,kqgrad,
			kqgrad_daf,kfcad,zhijbfml,zhijbfje,suopje,liuspsl,liuspje,shuifzj_zj,huifzj_zj,
			huiffzj_zj,relzj_zj,liuzj_zj,huirdzj_zj);
			flag = con.getInsert(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.InsertYuezlbFailed + "\nSQL:" + sql);
				setMsg(ErrorMessage.InsertYuezlbFailed);
				con.rollBack();
				con.Close();
				return;
			}
//			取累计值时，使用数量表中的净重作为数量依据
			sql = "select s.jingz laimsl,zl.* from yuetjkjb tj,yuezlb zl,yueslb s  " +
					"where s.yuetjkjb_id=tj.id and s.fenx = zl.fenx " +
					" and zl.yuetjkjb_id=tj.id and tj.riq="
				+ LastODate
				+ " and tj.diancxxb_id="
				+ diancxxb_id
				+ "\n"
				+ " and tj.gongysb_id="
				+ gongysb_id
				+ " and tj.pinzb_id="
				+ pinzb_id
				+ " and tj.jihkjb_id="
				+ jihkjb_id
				+ " and tj.yunsfsb_id="
				+ yunsfsb_id
				+ " and zl.fenx='累计'";
			datars = con.getResultSetList(sql);
			if (datars == null) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
				setMsg(ErrorMessage.NullResult);
				con.rollBack();
				con.Close();
				return;
			}
			if(datars.next() && intYuef != 1){
				zhijbfml = zhijbfml + datars.getDouble("zhijbfml"); 
				zhijbfje = zhijbfje + datars.getDouble("zhijbfje");
				
				shuifzj_zj = shuifzj_zj + datars.getDouble("ZHIJBFJE_M");
				huifzj_zj = huifzj_zj + datars.getDouble("ZHIJBFJE_A");
				huiffzj_zj = huiffzj_zj + datars.getDouble("ZHIJBFJE_V");
				relzj_zj = relzj_zj + datars.getDouble("ZHIJBFJE_Q");
				liuzj_zj = liuzj_zj + datars.getDouble("ZHIJBFJE_S");
				huirdzj_zj = huirdzj_zj + datars.getDouble("ZHIJBFJE_T");
				
				
				
				laimsllj = datars.getDouble("laimsl");
				if(laimslby + laimsllj != 0){
					qnet_ar = CustomMaths.Round_new(CustomMaths.div(qnet_ar*laimslby 
							+ datars.getDouble("qnet_ar")*laimsllj,laimslby+laimsllj), 2); 
					kqnet_ar = CustomMaths.Round_new(CustomMaths.div(kqnet_ar*laimslby 
							+ datars.getDouble("qnet_ar_kf")*laimsllj,laimslby+laimsllj), 2); 
					aar = CustomMaths.Round_new(CustomMaths.div(aar*laimslby 
							+ datars.getDouble("aar")*laimsllj,laimslby+laimsllj), 2); 
					kaar = CustomMaths.Round_new(CustomMaths.div(kaar*laimslby 
							+ datars.getDouble("aar_kf")*laimsllj,laimslby+laimsllj), 2); 
					ad = CustomMaths.Round_new(CustomMaths.div(ad*laimslby 
							+ datars.getDouble("ad")*laimsllj,laimslby+laimsllj), 2); 
					kad = CustomMaths.Round_new(CustomMaths.div(kad*laimslby 
							+ datars.getDouble("ad_kf")*laimsllj,laimslby+laimsllj), 2); 
					vdaf = CustomMaths.Round_new(CustomMaths.div(vdaf*laimslby 
							+ datars.getDouble("vdaf")*laimsllj,laimslby+laimsllj), 2); 
/**
*huochaoyuan
*2009-10-11 修改取数错误的bug，原来为"vad_kf",改为"vdaf_kf"
*/
						
					kvdaf = CustomMaths.Round_new(CustomMaths.div(kvdaf*laimslby 
							+ datars.getDouble("vdaf_kf")*laimsllj,laimslby+laimsllj), 2); 
//end					
					mt = CustomMaths.Round_new(CustomMaths.div(mt*laimslby 
							+ datars.getDouble("mt")*laimsllj,laimslby+laimsllj), 1); 
					kmt = CustomMaths.Round_new(CustomMaths.div(kmt*laimslby 
							+ datars.getDouble("mt_kf")*laimsllj,laimslby+laimsllj), 1); 
					stad = CustomMaths.Round_new(CustomMaths.div(stad*laimslby 
							+ datars.getDouble("stad")*laimsllj,laimslby+laimsllj), 2); 
					kstad = CustomMaths.Round_new(CustomMaths.div(kstad*laimslby 
							+ datars.getDouble("stad_kf")*laimsllj,laimslby+laimsllj), 2); 
					aad = CustomMaths.Round_new(CustomMaths.div(aad*laimslby 
							+ datars.getDouble("aad")*laimsllj,laimslby+laimsllj), 2); 
					kaad = CustomMaths.Round_new(CustomMaths.div(kaad*laimslby 
							+ datars.getDouble("aad_kf")*laimsllj,laimslby+laimsllj), 2); 
					mad = CustomMaths.Round_new(CustomMaths.div(mad*laimslby 
							+ datars.getDouble("mad")*laimsllj,laimslby+laimsllj), 2); 
					kmad = CustomMaths.Round_new(CustomMaths.div(kmad*laimslby 
							+ datars.getDouble("mad_kf")*laimsllj,laimslby+laimsllj), 2); 
					qbad = CustomMaths.Round_new(CustomMaths.div(qbad*laimslby 
							+ datars.getDouble("qbad")*laimsllj,laimslby+laimsllj), 2); 
					kqbad = CustomMaths.Round_new(CustomMaths.div(kqbad*laimslby 
							+ datars.getDouble("qbad_kf")*laimsllj,laimslby+laimsllj), 2); 
					had = CustomMaths.Round_new(CustomMaths.div(had*laimslby 
							+ datars.getDouble("had")*laimsllj,laimslby+laimsllj), 2); 
					khad = CustomMaths.Round_new(CustomMaths.div(khad*laimslby 
							+ datars.getDouble("had_kf")*laimsllj,laimslby+laimsllj), 2); 
					vad = CustomMaths.Round_new(CustomMaths.div(vad*laimslby 
							+ datars.getDouble("vad")*laimsllj,laimslby+laimsllj), 2); 
					kvad = CustomMaths.Round_new(CustomMaths.div(kvad*laimslby 
							+ datars.getDouble("vad_kf")*laimsllj,laimslby+laimsllj), 2); 
					fcad = CustomMaths.Round_new(CustomMaths.div(fcad*laimslby 
							+ datars.getDouble("fcad")*laimsllj,laimslby+laimsllj), 2);
					kfcad = CustomMaths.Round_new(CustomMaths.div(kfcad*laimslby 
							+ datars.getDouble("fcad_kf")*laimsllj,laimslby+laimsllj), 2);
					std = CustomMaths.Round_new(CustomMaths.div(std*laimslby 
							+ datars.getDouble("std")*laimsllj,laimslby+laimsllj), 2);
					kstd = CustomMaths.Round_new(CustomMaths.div(kstd*laimslby 
							+ datars.getDouble("std_kf")*laimsllj,laimslby+laimsllj), 2);
					qgrad = CustomMaths.Round_new(CustomMaths.div(qgrad*laimslby 
							+ datars.getDouble("qgrad")*laimsllj,laimslby+laimsllj), 2);
					kqgrad = CustomMaths.Round_new(CustomMaths.div(kqgrad*laimslby 
							+ datars.getDouble("qgrad_kf")*laimsllj,laimslby+laimsllj), 2);
					hdaf = CustomMaths.Round_new(CustomMaths.div(hdaf*laimslby 
							+ datars.getDouble("hdaf")*laimsllj,laimslby+laimsllj), 2);
					khdaf = CustomMaths.Round_new(CustomMaths.div(khdaf*laimslby 
							+ datars.getDouble("hdaf_kf")*laimsllj,laimslby+laimsllj), 2);
					qgrad_daf = CustomMaths.Round_new(CustomMaths.div(qgrad_daf*laimslby 
							+ datars.getDouble("qgrad_daf")*laimsllj,laimslby+laimsllj), 2);
					kqgrad_daf = CustomMaths.Round_new(CustomMaths.div(kqgrad_daf*laimslby 
							+ datars.getDouble("qgrad_daf_kf")*laimsllj,laimslby+laimsllj), 2);
					sdaf = CustomMaths.Round_new(CustomMaths.div(sdaf*laimslby 
							+ datars.getDouble("sdaf")*laimsllj,laimslby+laimsllj), 2);
					ksdaf = CustomMaths.Round_new(CustomMaths.div(ksdaf*laimslby 
							+ datars.getDouble("sdaf_kf")*laimsllj,laimslby+laimsllj), 2);
					var = CustomMaths.Round_new(CustomMaths.div(var*laimslby 
							+ datars.getDouble("var")*laimsllj,laimslby+laimsllj), 2);
					kvar = CustomMaths.Round_new(CustomMaths.div(kvar*laimslby 
							+ datars.getDouble("var_kf")*laimsllj,laimslby+laimsllj), 2);
					
					
						suopje = suopje + datars.getDouble("suopje");//suppje
					
					
					liuspsl = liuspsl + datars.getDouble("lsuopsl");
					liuspje = liuspje + datars.getDouble("lsuopje");
				}
			}
			sql = InsertYuezlbSql(diancxxb_id, lngId, SysConstant.Fenx_Leij,qnet_ar,qbad,mt,aar,vdaf,std,had,mad,
			aad,ad,vad,var,stad,sdaf,hdaf,qgrad,qgrad_daf,fcad,kqnet_ar,kqbad,kmt,
			kaar,kvdaf,kstd,khad,kmad,kaad,kad,kvad,kvar,kstad,ksdaf,khdaf,kqgrad,
			kqgrad_daf,kfcad,zhijbfml,zhijbfje,suopje,liuspsl,liuspje,shuifzj_zj,huifzj_zj,
			huiffzj_zj,relzj_zj,liuzj_zj,huirdzj_zj);
			flag = con.getInsert(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.InsertYuezlbFailed + "\nSQL:" + sql);
				setMsg(ErrorMessage.InsertYuezlbFailed);
				con.rollBack();
				con.Close();
				return;
			}
		}
		rs.close();
		
		if (intYuef!=1){//取上个月有这个月没有的数据，本月值为0，累计值等与上个月累计
			sql = "select * from (select distinct y.id,y.gongysb_id,y.jihkjb_id,y.pinzb_id,\n"
				+ "y.yunsfsb_id,nvl(z.yuetjkjb_id,-1) yuetjkj\n"
				+ "from yuetjkjb y, yuezlb z\n"
				+ "where y.id = z.yuetjkjb_id(+)\n"
				+ "and z.fenx(+) = '"
				+ SysConstant.Fenx_Beny
				+ "'\n"
				+ "and y.riq = "
				+ CurrODate
				+ "\n"
				+ "and y.diancxxb_id = "
				+ diancxxb_id
				+ ")\n" + "where yuetjkj = -1";
			
			
//			sql="select sl.*,tj.* from yuezlb sl,yuetjkjb tj \n"
//				+ " where sl.yuetjkjb_id=tj.id and riq="+LastODate+" \n"
//				+ " and fenx='累计' and diancxxb_id="+visit.getDiancxxb_id();
			ResultSetList rssylj=con.getResultSetList(sql);
			if(rssylj == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
				setMsg(ErrorMessage.NullResult);
				con.rollBack();
				con.Close();
				return;
			}
			
			while (rssylj.next()){
				sql = "select y.id yid,z.* from yuetjkjb y,yuezlb z where y.id = z.yuetjkjb_id(+) "
					+ " and z.fenx(+) = '"
					+ SysConstant.Fenx_Leij
					+ "'"
					+ " and y.riq="
					+ LastODate
					+ " and y.diancxxb_id="
					+ diancxxb_id
					+ "\n"
					+ " and y.gongysb_id="
					+ rssylj.getLong("gongysb_id")
					+ " and y.pinzb_id="
					+ rssylj.getLong("pinzb_id")
					+ " and y.jihkjb_id="
					+ rssylj.getLong("jihkjb_id")
					+ " and y.yunsfsb_id="
					+ rssylj.getLong("yunsfsb_id");
				ResultSetList recby = con.getResultSetList(sql);
				if (recby == null) {
					WriteLog.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
					setMsg(ErrorMessage.NullResult);
					con.rollBack();
					con.Close();
					return;
				}
				if (recby.next()) {
					sql = InsertYuezlbSql(diancxxb_id, lngId, SysConstant.Fenx_Beny,0,0,0,0,0,0,0,0,
					0,0,0,0,0,0,0,0,0,0,0,0,0,
					0,0,0,0,0,0,0,0,0,0,0,0,0,
					0,0,0,0,0,0,0,0,0,0,0,0,0);
					flag = con.getInsert(sql);
					if (flag == -1) {
						WriteLog.writeErrorLog(this.getClass().getName() + "\n"
								+ ErrorMessage.InsertYuezlbFailed + "\nSQL:" + sql);
						setMsg(ErrorMessage.InsertYuezlbFailed);
						con.rollBack();
						con.Close();
						return;
					}
					
				
					sql = InsertYuezlbSql(diancxxb_id, lngId, SysConstant.Fenx_Leij,recby.getDouble("qnet_ar"),
					recby.getDouble("qbad"),recby.getDouble("mt"),recby.getDouble("aar"),
					recby.getDouble("vdaf"),recby.getDouble("std"),recby.getDouble("had"),
					recby.getDouble("mad"),recby.getDouble("aad"),recby.getDouble("ad"),
					recby.getDouble("vad"),recby.getDouble("var"),recby.getDouble("stad"),
					recby.getDouble("sdaf"),recby.getDouble("hdaf"),recby.getDouble("qgrad"),
					recby.getDouble("qgrad_daf"),recby.getDouble("fcad"),recby.getDouble("kqnet_ar"),
					recby.getDouble("kqbad"),recby.getDouble("kmt"),recby.getDouble("kaar"),
					recby.getDouble("kvdaf"),recby.getDouble("kstd"),recby.getDouble("khad"),
					recby.getDouble("kmad"),recby.getDouble("kaad"),recby.getDouble("kad"),
					recby.getDouble("kvad"),recby.getDouble("kvar"),recby.getDouble("kstad"),
					recby.getDouble("ksdaf"),recby.getDouble("khdaf"),recby.getDouble("kqgrad"),
					recby.getDouble("kqgrad_daf"),recby.getDouble("kfcad"),recby.getDouble("zhijbfml"),
					recby.getDouble("zhijbfje"),recby.getDouble("suopje"),recby.getDouble("lsuopsl"),
					recby.getDouble("lsuopje"),
					recby.getDouble("ZHIJBFJE_M"),recby.getDouble("ZHIJBFJE_A"),recby.getDouble("ZHIJBFJE_V"),
					recby.getDouble("ZHIJBFJE_Q"),recby.getDouble("ZHIJBFJE_S"),recby.getDouble("ZHIJBFJE_T"));
					flag = con.getInsert(sql);
					if (flag == -1) {
						WriteLog.writeErrorLog(this.getClass().getName() + "\n"
								+ ErrorMessage.InsertYuezlbFailed + "\nSQL:"
								+ sql);
						setMsg(ErrorMessage.InsertYuezlbFailed);
						con.rollBack();
						con.Close();
						return;
					}
				}
				recby.close();
			}
			rssylj.close();
		}
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"的数据成功生成！");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String sj[] = this.getRiq2().split("-");
		String CurrODate ="";
		if(visit.getString10().equals(strParam)){
			CurrODate = DateUtil.FormatOracleDate(sj[0] + "-" + sj[1] + "-01");
		}else{
		    CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		}
		String diancxxb_id = getTreeid();

		String sql = "SELECT * FROM\n" +
		"(\n" + 
		"select -2 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
		"'' as yunsfsb_id,'本月合计' AS fenx,sum(sl.jingz) jingz,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar)/SUM(sl.jingz)),2) AS qnet_ar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad)/SUM(sl.jingz)),2) AS qbad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt)/SUM(sl.jingz)),1) AS mt,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad)/SUM(sl.jingz)),2) AS mad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar)/SUM(sl.jingz)),2) AS aar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad)/SUM(sl.jingz)),2) AS aad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*ad)/SUM(sl.jingz)),2) AS ad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf)/SUM(sl.jingz)),2) AS vdaf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std)/SUM(sl.jingz)),2) AS std,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had)/SUM(sl.jingz)),2) AS had,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad)/SUM(sl.jingz)),2) AS fcad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar_kf)/SUM(sl.jingz)),2) AS qnet_ar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad_kf)/SUM(sl.jingz)),2) AS qbad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt_kf)/SUM(sl.jingz)),1) AS mt_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad_kf)/SUM(sl.jingz)),2) AS mad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar_kf)/SUM(sl.jingz)),2) AS aar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad_kf)/SUM(sl.jingz)),2) AS aad_kf,\n"+
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf_kf)/SUM(sl.jingz)),2) AS vdaf_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std_kf)/SUM(sl.jingz)),2) AS std_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had_kf)/SUM(sl.jingz)),2) AS had_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad_kf)/SUM(sl.jingz)),2) AS fcad_kf,\n" + 
		"SUM(zhijbfml) AS zhijbfml,\n" +
		"SUM(zhijbfje) AS zhijbfje, \n" +
		"SUM(zhijbfje_m) AS zhijbfje_m,\n" +
		"SUM(zhijbfje_a) AS zhijbfje_a,\n" + 
		"SUM(zhijbfje_v) AS zhijbfje_v,\n" +
		"SUM(zhijbfje_q) AS zhijbfje_q,\n" +
		"SUM(zhijbfje_s) AS zhijbfje_s,\n" + 
		"SUM(zhijbfje_t) AS zhijbfje_t,\n" +
		"SUM(t.suopje) AS suopje, sum(t.lsuopsl) AS lsuopsl, sum(t.lsuopje) lsuopje\n" + 
		"from yuetjkjb tj,yuezlb t,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n" + 
		"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
		"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND tj.id = sl.yuetjkjb_id AND sl.fenx = t.fenx\n" + 
		"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+" AND t.fenx='本月'\n" + 
		"UNION\n" + 
		" select -1 id,0 yuetjkjb_id,'' as gongysb_id,'' as jihkjb_id,'' as pinzb_id,\n" + 
		"'' as yunsfsb_id,'累计合计' AS fenx,sum(sl.jingz) jingz,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar)/SUM(sl.jingz)),2) AS qnet_ar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad)/SUM(sl.jingz)),2) AS qbad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt)/SUM(sl.jingz)),1) AS mt,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad)/SUM(sl.jingz)),2) AS mad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar)/SUM(sl.jingz)),2) AS aar,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad)/SUM(sl.jingz)),2) AS aad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*ad)/SUM(sl.jingz)),2) AS ad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf)/SUM(sl.jingz)),2) AS vdaf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std)/SUM(sl.jingz)),2) AS std,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had)/SUM(sl.jingz)),2) AS had,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad)/SUM(sl.jingz)),2) AS fcad,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qnet_ar_kf)/SUM(sl.jingz)),2) AS qnet_ar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*qbad_kf)/SUM(sl.jingz)),2) AS qbad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mt_kf)/SUM(sl.jingz)),1) AS mt_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*mad_kf)/SUM(sl.jingz)),2) AS mad_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aar_kf)/SUM(sl.jingz)),2) AS aar_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*aad_kf)/SUM(sl.jingz)),2) AS aad_kf,\n"+
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*vdaf_kf)/SUM(sl.jingz)),2) AS vdaf_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*std_kf)/SUM(sl.jingz)),2) AS std_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*had_kf)/SUM(sl.jingz)),2) AS had_kf,\n" + 
		"round(decode(sum(sl.jingz),0,0,sum(sl.jingz*fcad_kf)/SUM(sl.jingz)),2) AS fcad_kf,\n" + 
		"SUM(zhijbfml) AS zhijbfml,\n" +
		"SUM(zhijbfje) AS zhijbfje,\n" +
		"SUM(zhijbfje_m) AS zhijbfje_m,\n" +
		"SUM(zhijbfje_a) AS zhijbfje_a,\n" + 
		"SUM(zhijbfje_v) AS zhijbfje_v,\n" +
		"SUM(zhijbfje_q) AS zhijbfje_q,\n" +
		"SUM(zhijbfje_s) AS zhijbfje_s,\n" + 
		"SUM(zhijbfje_t) AS zhijbfje_t,\n" +
		"SUM(t.suopje) AS suopje, sum(t.lsuopsl) AS lsuopsl, sum(t.lsuopje) lsuopje\n" + 
		"from yuetjkjb tj,yuezlb t,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n" + 
		"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
		"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND tj.id = sl.yuetjkjb_id AND sl.fenx = t.fenx\n" + 
		"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+" AND t.fenx='累计'\n" + 
		"UNION\n" + 
		"select t.id,tj.id yuetjkjb_id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n" + 
		"yunsfsb.mingc as yunsfsb_id,t.fenx,sl.jingz,qnet_ar, qbad, mt, mad, aar, aad, ad,\n" + 
		"vdaf, std, had, fcad, qnet_ar_kf, qbad_kf, mt_kf, mad_kf,\n" + 
		"aar_kf,aad_kf, vdaf_kf, std_kf, had_kf, fcad_kf, zhijbfml,zhijbfje,zhijbfje_m,\n" + 
		"zhijbfje_a,zhijbfje_v,zhijbfje_q,zhijbfje_s,zhijbfje_t,t.suopje, lsuopsl, lsuopje\n" + 
		"from yuetjkjb tj,yuezlb t,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n" + 
		"where tj.id=t.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
		"and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id AND tj.id = sl.yuetjkjb_id AND sl.fenx = t.fenx\n" + 
		"and tj.diancxxb_id="+diancxxb_id+" and tj.riq="+CurrODate+"\n" + 
		") ORDER BY jihkjb_id DESC,gongysb_id,pinzb_id,id,yuetjkjb_id,fenx";
		
		JDBCcon con = new JDBCcon();
//		System.out.println(strSql);
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		// //设置表名称用于保存
		egu.setTableName("yuezlb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		//egu.getColumn("xuh").setHeader("序号");
		//egu.getColumn("xuh").setWidth(50);
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("gongysb_id").setCenterHeader("供货单位");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").update=false;
		
		egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").update=false;
		
		egu.getColumn("pinzb_id").setCenterHeader("品种");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("pinzb_id").update=false;
		
		egu.getColumn("yunsfsb_id").setCenterHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").update=false;
		
		egu.getColumn("fenx").setCenterHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		
		egu.getColumn("jingz").setCenterHeader("数量");
		egu.getColumn("jingz").setWidth(80);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setHidden(true);
		
		//*******************厂方**********************//

		egu.getColumn("qnet_ar").setCenterHeader("收到基低位热(Qnet,ar(Mj/kg))",6,2);
		egu.getColumn("qnet_ar").setWidth(90);
		egu.getColumn("qnet_ar").editor.setMinValue("0");
		
		egu.getColumn("qbad").setCenterHeader("弹筒热(Qbad(Mj/kg))",3,2);
		egu.getColumn("qbad").setWidth(60);
		egu.getColumn("qbad").editor.setMinValue("0");
		
		egu.getColumn("mt").setCenterHeader("全水(Mt)",2,2);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mt").editor.setMinValue("0");
		
		egu.getColumn("mad").setCenterHeader("空干基水(Mad)",4,2);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("mad").editor.setMinValue("0");
		
		egu.getColumn("aar").setCenterHeader("收到基灰分(Aar)",5,2);
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("aar").editor.setMinValue("0");
		
		egu.getColumn("aad").setCenterHeader("空干基灰分(Aad)",5,2);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("aad").editor.setMinValue("0");
		
		egu.getColumn("ad").setCenterHeader("干基灰分(Ad)",4,2);
		egu.getColumn("ad").setWidth(60);
		egu.getColumn("ad").editor.setMinValue("0");
		
		egu.getColumn("vdaf").setCenterHeader("干燥无灰基挥发分(Vdaf)",8,2);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("vdaf").editor.setMinValue("0");
		
		egu.getColumn("std").setCenterHeader("干基硫(Std)",3,2);
		egu.getColumn("std").setWidth(60);
		egu.getColumn("std").editor.setMinValue("0");
		
		egu.getColumn("had").setCenterHeader("空干基氢(Had)",4,2);
		egu.getColumn("had").setWidth(60);
		egu.getColumn("had").editor.setMinValue("0");
		
		egu.getColumn("fcad").setCenterHeader("固定碳(Fcad)",3,2);
		egu.getColumn("fcad").setWidth(60);
		egu.getColumn("fcad").editor.setMinValue("0");
		
		//*******************矿方**********************//
		egu.getColumn("qnet_ar_KF").setCenterHeader("矿方低位热(Qnet,ar(Mj/kg))",5,2);
		egu.getColumn("qnet_ar_KF").setWidth(90);
		egu.getColumn("qnet_ar_KF").editor.setMinValue("0");
		
		egu.getColumn("qbad_KF").setCenterHeader("矿方弹筒热(Qbad)",5,2);
		egu.getColumn("qbad_KF").setWidth(60);
		egu.getColumn("qbad_KF").editor.setMinValue("0");
		
		egu.getColumn("mt_KF").setCenterHeader("矿方全水(Mt)",4,2);
		egu.getColumn("mt_KF").setWidth(60);
		egu.getColumn("mt_KF").editor.setMinValue("0");
		
		egu.getColumn("mad_KF").setCenterHeader("矿方空干基水(Mad)",6,2);
		egu.getColumn("mad_KF").setWidth(60);
		egu.getColumn("mad_KF").editor.setMinValue("0");
		
		egu.getColumn("aar_KF").setCenterHeader("矿方收到基灰分(Aar)",7,2);
		egu.getColumn("aar_KF").setWidth(60);
		egu.getColumn("aar_KF").editor.setMinValue("0");
		
		egu.getColumn("aad_KF").setCenterHeader("矿方空干基灰分(Aad)",7,2);
		egu.getColumn("aad_KF").setWidth(60);
		egu.getColumn("aad_KF").editor.setMinValue("0");
		
		egu.getColumn("vdaf_KF").setCenterHeader("矿方挥发分(Vdaf)",5,2);
		egu.getColumn("vdaf_KF").setWidth(60);
		egu.getColumn("vdaf_KF").editor.setMinValue("0");
		
		egu.getColumn("std_KF").setCenterHeader("矿方干基硫(Std)",5,2);
		egu.getColumn("std_KF").setWidth(60);
		egu.getColumn("std_KF").editor.setMinValue("0");
		
		egu.getColumn("had_KF").setCenterHeader("矿方空干基氢(Had)",6,2);
		egu.getColumn("had_KF").setWidth(60);
		egu.getColumn("had_KF").editor.setMinValue("0");
		
		egu.getColumn("fcad_KF").setCenterHeader("矿方固定碳(Fcad)",5,2);
		egu.getColumn("fcad_KF").setWidth(60);
		egu.getColumn("fcad_KF").editor.setMinValue("0");
		
		egu.getColumn("zhijbfml").setCenterHeader("质价不符煤量(吨)",4,2);
		egu.getColumn("zhijbfml").setWidth(60);
		egu.getColumn("zhijbfje").setCenterHeader("质价不符金额(元)",4,2);
		egu.getColumn("zhijbfje").setWidth(60);
		egu.getColumn("suopje").setCenterHeader("索赔金额(元)",2,2);
		egu.getColumn("suopje").setWidth(60);
		egu.getColumn("lsuopsl").setCenterHeader("硫索赔数量(吨)",3,2);
		egu.getColumn("lsuopsl").setWidth(60);
		egu.getColumn("lsuopje").setCenterHeader("硫索赔金额(元)",3,2);
		egu.getColumn("lsuopje").setWidth(60);
		
		egu.getColumn("ZHIJBFJE_M").setCenterHeader("质价不符水分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_M").setWidth(60);
		egu.getColumn("ZHIJBFJE_A").setCenterHeader("质价不符灰分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_A").setWidth(60);
		egu.getColumn("ZHIJBFJE_V").setCenterHeader("质价不符挥发分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_V").setWidth(60);
		egu.getColumn("ZHIJBFJE_Q").setCenterHeader("质价不符热值(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_Q").setWidth(60);
		egu.getColumn("ZHIJBFJE_S").setCenterHeader("质价不符硫分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_S").setWidth(60);
		egu.getColumn("ZHIJBFJE_T").setCenterHeader("质价不符灰熔点(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_T").setWidth(60);
		
		
//		设定不可编辑列的颜色
		egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jihkjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pinzb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunsfsb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("zhijbfje").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

//		隐藏列设置
		egu.getColumn("qbad").setHidden(true);
		egu.getColumn("qbad_kf").setHidden(true);
		String Sql="select zhi from xitxxb x where x.leib='月报' and x.danw='质量' and beiz='使用'";
		ResultSetList rs = con.getResultSetList(Sql);
		if(rs != null) {
			while (rs.next()){
				String zhi = rs.getString("zhi");
				if(egu.getColByHeader(zhi)!=null){
					egu.getColByHeader(zhi).hidden=true;
				}
			}
		}
		egu.setDefaultsortable(false);   
		
		// /设置按钮
		if(visit.getString10().equals(strParam)){
			egu.addTbarText("时间:");
	    	DateField df = new DateField();
	    	df.setValue(this.getRiq1());
	    	df.Binding("RIQ1", "");
	    	df.setId("riq1");
	    	egu.addToolbarItem(df.getScript());
		
	    	egu.addTbarText("-");
	    	egu.addTbarText("至:");
	    	DateField df1 = new DateField();
	    	df1.setValue(this.getRiq2());
	    	df1.Binding("RIQ2", "");
	    	df1.setId("riq2");
	    	egu.addToolbarItem(df1.getScript());
			
		}else{
			egu.addTbarText("年份");
			ComboBox comb1=new ComboBox();
			comb1.setWidth(50);
			comb1.setTransform("NianfDropDown");
			comb1.setId("NianfDropDown");//和自动刷新绑定
			comb1.setLazyRender(true);//动态绑定
			comb1.setEditable(true);
			egu.addToolbarItem(comb1.getScript());
			
			egu.addTbarText("月份");
			ComboBox comb2=new ComboBox();
			comb2.setWidth(50);
			comb2.setTransform("YuefDropDown");
			comb2.setId("YuefDropDown");//和自动刷新绑定
			comb2.setLazyRender(true);//动态绑定
			comb2.setEditable(true);
			egu.addToolbarItem(comb2.getScript());
		}
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新"+sj[0]+"年"+sj[1]+"月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if(getZhangt(con)){
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		}else{
	//		生成按钮
			GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
	//		删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
	//		保存按钮
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		}
//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+MainGlobal.getOpenWinScript("MonthReport&lx=yuezlb")+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		String condition="";
		if(MainGlobal.getXitxx_item("月报", "月报质量自动计算累计", getTreeid(), "是").equals("是")){
			condition="if(e.record.get('FENX')=='累计'){ e.cancel=true;}\n";
		}
//		质价不符 不允许编辑
		egu.addOtherScript(" gridDiv_grid.addListener('beforeedit',function(e){" +
				"if(e.field=='ZHIJBFJE' || e.record.get('ID')=='-1' || e.record.get('ID')=='-2'){ e.cancel=true;}\n"+
				condition+
				"\n} );\n");
		egu.addOtherScript(" gridDiv_grid.addListener('afteredit',function(e){" +
				"if(e.field=='ZHIJBFJE_M' || e.field=='ZHIJBFJE_A' ||e.field=='ZHIJBFJE_V' ||" +
				"e.field=='ZHIJBFJE_Q' ||e.field=='ZHIJBFJE_S' ||e.field=='ZHIJBFJE_T' )" +
				"{ var _val=parseFloat(e.record.get('ZHIJBFJE_M'))+parseFloat(e.record.get('ZHIJBFJE_A'))+parseFloat(e.record.get('ZHIJBFJE_V'))" +
				" +parseFloat(e.record.get('ZHIJBFJE_Q'))+parseFloat(e.record.get('ZHIJBFJE_S'))+parseFloat(e.record.get('ZHIJBFJE_T'));\n" +
				" e.record.set('ZHIJBFJE',_val);}\n" +
				"\n} );\n"); 
		
		setExtGrid(egu);
		con.Close();
	}
	/**
	 * @param con
	 * @return   true:已上传状态中 不能修改数据 false:未上传状态中 可以修改数据
	 */
	private boolean getZhangt(JDBCcon con){
		Visit visit=(Visit)getPage().getVisit();
		String CurrODate = "";
		String sj[] = this.getRiq2().split("-");
		if(visit.getString10().equals(strParam)){
			CurrODate=DateUtil.FormatOracleDate(sj[0] + "-" + sj[1]+ "-01");
		}else{
			CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		}
		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from yuezlb s, yuetjkjb k\n" + 
			" where s.yuetjkjb_id = k.id\n" + 
			"   and k.diancxxb_id = "+getTreeid()+"\n" + 
			"   and k.riq = "+CurrODate;
		ResultSetList rs=con.getResultSetList(sql);
		boolean zt=true;
		if(con.getHasIt(sql)){
			while(rs.next()){
				if(rs.getInt("zhuangt")==0||rs.getInt("zhuangt")==2){
					zt=false;
				}
			}
		}else{
			zt=false;
		}
		return zt;
	}
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		Visit visit = (Visit) getPage().getVisit();
		String cnDate = "";
		String sj [] = this.getRiq2().split("-");
		if(visit.getString10().equals(strParam)){
			cnDate = sj[0] + "年" + sj[1] + "月"; 
		}else{
		    cnDate = getNianf()+"年"+getYuef()+"月";
		}
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖")
			.append(cnDate).append("的已存数据，是否继续？");
		}else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
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
			String strDate = cycle.getRequestContext().getParameter("lx");
			if(strDate!=null){
				visit.setString10(strDate);
			}else{
				visit.setString10("");
			}
			visit.setActivePageName(getPageName().toString());
			visit.setString11("");
			visit.setString9("");
			setRiq();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
		}
		getSelectData();
	}
	
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString2());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString2();
		}
	}
	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
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
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
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
    	if  (_YuefValue!=Value){
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

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
//	设置日期时间框
	public String getRiq1(){
	       if(((Visit) this.getPage().getVisit()).getString11()==null||((Visit) this.getPage().getVisit()).getString11().equals("")){
				
				((Visit) this.getPage().getVisit()).setString11(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString11();
		}
		public void setRiq1(String riq1){
	         if(((Visit) this.getPage().getVisit()).getString11()!=null &&!((Visit) this.getPage().getVisit()).getString11().equals(riq1)){
				
				((Visit) this.getPage().getVisit()).setString11(riq1);
			}
		}
		
		public String getRiq2(){
	         if(((Visit) this.getPage().getVisit()).getString9()==null||((Visit) this.getPage().getVisit()).getString9().equals("")){
				
				((Visit) this.getPage().getVisit()).setString9(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString9();
		}
		public void setRiq2(String riq2){
	        if(((Visit) this.getPage().getVisit()).getString9()!=null &&!((Visit) this.getPage().getVisit()).getString9().equals(riq2)){
				
				((Visit) this.getPage().getVisit()).setString9(riq2);
			}
		}
}