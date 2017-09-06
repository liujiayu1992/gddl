package com.zhiren.dc.monthReport.tb;
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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:SONGY
 * 时间:2011-5-30
 * 修改内容: 同时保存vdaf和vad的值，通过公式转换。在xitxxb中通过参数判断页面输出哪个，注意xitxxb中设置的值要求大写
 */

/*
 * 作者：夏峥
 * 时间：2011-06-22
 * 适用范围：国电电力
 * 描述：取消使用diancrz字段
 * 		修正对化学分析元素AAR,AAD,AAR_KF,AAD_KF的使用
 */
/*
 * 作者：夏峥
 * 日期：2011-12-20
 * 描述：取消全部界面自动刷新功能，用户需手动点击刷新按钮才可刷新数据
 */
/*
 * 作者：夏峥
 * 日期：2012-01-10
 * 描述：质量填报界面累计值不能自动计算不符折价信息
 */
/*
 * 作者：LIP
 * 日期：2012-03-14
 * 描述：因新需求数据存储内容有变，同名称出现两次，改变SQL关联条件
 */
/*
 * 作者：LIP
 * 日期：2012-03-14
 * 描述：当月无数量，不能填化验值
 */
/*
 * 作者：赵胜男
 * 日期：2012-03-16
 * 描述：增加删除功能
 */
/*
 * 作者：赵胜男
 * 日期：2013-01-11
 * 描述：调整界面不可编辑列显示方式；
 *				化验值信息加入最小值限制,最小值大于0.
 */
public class Yuezlb_szs extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
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
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		
		String str_v = MainGlobal.getXitxx_item("月报", "调燃03表中挥发份类型", getTreeid(), "VDAF");
		String str_value_vdaf="";
		String str_value_vad="";
		
		sql.delete(0, sql.length());
		sql.append("begin \n");
		while (rsl.next()) {//判断xitxx参数，电厂填报vdaf或vad，保存时作转换后保存。
			if("VDAF".equals(str_v)){
				str_value_vdaf = rsl.getDouble("VDAF") + "";
				str_value_vad= "round_new(" +rsl.getDouble("vdaf")/100*(100-rsl.getDouble("mad")-rsl.getDouble("aad"))+    ",2)"  ;
			}else{
				str_value_vdaf = "round_new(" + rsl.getDouble("VAD")*100/(100-rsl.getDouble("AAD")-rsl.getDouble("MAD")) + ",2)";
				str_value_vad = rsl.getDouble("vad")+"";
			}
			if ("".equals(rsl.getString("YID"))) {
				sql.append("insert into yuezlb("
								+ "id,fenx,yuetjkjb_id,\n"
								+ "qnet_ar,mt,mad,aar,aad,ad,vdaf,vad,std,had,fcad,\n" 
								+ "qnet_ar_kf,mt_kf,mad_kf,aar_kf,aad_kf,vdaf_kf,std_kf,had_kf,fcad_kf,\n"
								+ "zhijbfml,zhijbfje,\n"
								+ "zhijbfje_m,zhijbfje_a,zhijbfje_v,zhijbfje_q,zhijbfje_s,zhijbfje_t,\n"
								+ "suopje,lsuopsl,lsuopje\n"
//								+ "diancrz\n"
								+ ")values(\n"
								+ rsl.getString("ID") + ",'" + rsl.getString("fenx") + "'," + rsl.getString("yuetjkjb_id") + ","
								+ rsl.getDouble("QNET_AR") + ","
								+ rsl.getDouble("MT") + ","
								+ rsl.getDouble("MAD") + ","
								+ rsl.getDouble("AAR") + ","
								+ rsl.getDouble("AAD") + ","
								+ rsl.getDouble("AD") + ","
								+ str_value_vdaf + ","
								+ str_value_vad + ","
//								+ rsl.getDouble("VDAF") + ","
								+ rsl.getDouble("STD") + ","
								+ rsl.getDouble("HAD") + ","
								+ rsl.getDouble("FCAD") + ",\n"
								
								+ rsl.getDouble("QNET_AR_KF") + ","
								+ rsl.getDouble("MT_KF") + ","
								+ rsl.getDouble("MAD_KF") + ","
								+ rsl.getDouble("AAR_KF") + ","
								+ rsl.getDouble("AAD_KF") + ","
								+ rsl.getDouble("VDAF_KF") + ","
								+ rsl.getDouble("STD_KF") + ","
								+ rsl.getDouble("HAD_KF") + ","
								+ rsl.getDouble("FCAD_KF") + ",\n"
								
								+ rsl.getDouble("zhijbfml") + ","
								+ rsl.getDouble("zhijbfje") + ",\n"
								
								+ rsl.getDouble("zhijbfje_m") + ","
								+ rsl.getDouble("zhijbfje_a") + ","
								+ rsl.getDouble("zhijbfje_v") + ","
								+ rsl.getDouble("zhijbfje_q") + ","
								+ rsl.getDouble("zhijbfje_s") + ","
								+ rsl.getDouble("zhijbfje_t") + ",\n"
								
								+ rsl.getDouble("suopje") + ","
								+ rsl.getDouble("lsuopsl") + ","
								+ rsl.getDouble("lsuopje") + "\n"
//								+ rsl.getDouble("diancrz")
								+ ");\n");
				// }
			} else {
				sql.append("update yuezlb set yuetjkjb_id=" + rsl.getString("yuetjkjb_id") +
						
						",QNET_AR=" + rsl.getDouble("QNET_AR") + 
						",MT=" + rsl.getDouble("MT") + 
						",MAD=" + rsl.getDouble("MAD") +
						",AAR=" + rsl.getDouble("AAR") + 
						",AAD=" + rsl.getDouble("AAD") + 
						",AD=" + rsl.getDouble("AD") + 
						",VDAF=" + str_value_vdaf + 
						",vad=" + str_value_vad + 
//						",VDAF=" + rsl.getDouble("VDAF") + 
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
						
//						",diancrz=" + rsl.getDouble("diancrz")+
						" where id=" + rsl.getLong("id") + ";\n");
			}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if(flag!=-1){
			rsl.beforefirst();
			sql.setLength(0);
			while(rsl.next()){
				if("累计".equals(rsl.getString("fenx")) && getIsSelectLike()){
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
						"\n" + 
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
						"\n" + 
						"sum(zhijbfml)zhijbfml,sum(zhijbfje)zhijbfje,\n" + 
						"sum(zhijbfje_m)zhijbfje_m,sum(zhijbfje_a)zhijbfje_a,sum(zhijbfje_v)zhijbfje_v," +
						"sum(zhijbfje_q)zhijbfje_q,sum(zhijbfje_s)zhijbfje_s,sum(zhijbfje_t)zhijbfje_t,\n" + 
						"sum(y.suopje)suopje,sum(y.lsuopsl)lsuopsl,sum(y.lsuopje)lsuopje\n" + 
//						"round_new(decode(sum(jingz),0,0,sum(jingz*diancrz)/sum(jingz)),2) diancrz" +
						
						"  from yuezlb y,yueslb s,yuetjkjb yt,(select gongysb_id,jihkjb_iD,pinzb_id,yunsfsb_id from yuetjkjb where id= " + rsl.getString("YUETJKJB_ID") + ")yt2\n" + 
						" where y.yuetjkjb_id=yt.id\n" + 
						"	and y.yuetjkjb_id=s.yuetjkjb_id\n" +
						"	and yt.gongysb_id=yt2.gongysb_id\n" +
						"	and yt.jihkjb_id=yt2.jihkjb_id\n" +
						"	and yt.pinzb_id=yt2.pinzb_id\n" +
						"	and yt.yunsfsb_id=yt2.yunsfsb_id\n" +
						"   and yt.riq>=to_date('"+ getNianf() + "-01-01" + "','yyyy-mm-dd')\n" + 
						"   and yt.riq<=to_date('"+ getNianf() + "-" + getYuef() + "-01" + "','yyyy-mm-dd')\n" + 
						"   and y.fenx='" + SysConstant.Fenx_Beny + "'\n" +
						"   and s.fenx='" + SysConstant.Fenx_Beny + "'\n" + 
						"   and yt.diancxxb_id=" + getTreeid();
					ResultSetList rs = con.getResultSetList(sq);
					rs.next();
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
							
//							",diancrz=" + rs.getDouble("diancrz")+
							" where id=" + rsl.getLong("id") + ";\n");
				}
			}
			if(sql.length()!=0){
				flag = con.getUpdate("begin\n" + sql.toString() + "\n end;");
				if(flag!=-1){
					setMsg("保存成功!");
				}else{
					setMsg("保存成功,累计值计算失败!");
				}
			}else{
				setMsg("保存成功!");
			}
		}else{
			setMsg("保存失败");
		} 
		rsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

    private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			//getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
		//	getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		
		setRiq();
	}
	
	public void DelData() {
		//String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
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

	
	/**
	 * @param con
	 * @return   true:已上传状态中 不能修改数据 false:未上传状态中 可以修改数据
	 */
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		String cnDate = getNianf() + "年" + getYuef() + "月";
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		btnsb.append("是否删除").append(cnDate).append("的数据？");
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	

	private boolean getZhangt(JDBCcon con){
		String CurrODate =  DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
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

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String str_v = MainGlobal.getXitxx_item("月报", "调燃03表中挥发份类型", getTreeid(), "VDAF");
		String sql_v = "";
		if("VDAF".equals(str_v) ){//判断xitxx参数，选择在页面查询出的挥发分
			sql_v = "y.vdaf";
		}else{
			sql_v = "y.vad  ";
		}
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String sql = 
			"select y.id yid,sl.jingz,decode(y.id,null,getnewid(" + getTreeid() + "),y.id) id,s.gmingc,s.jmingc,s.pmingc,s.yfsmingc,s.fenx,s.yuetjkjb_id,\n" +
			"             y.qnet_ar,y.mt,y.mad,y.aar,y.aad,y.ad," + sql_v + ",y.std,y.had,y.fcad,\n" + 
			"             y.qnet_ar_kf,y.mt_kf,y.mad_kf,y.aar_kf,y.aad_kf,y.ad_kf,y.vdaf_kf,y.std_kf,y.had_kf,y.fcad_kf,\n" + 
			"             y.zhijbfml,y.zhijbfje,\n" + 
			"             y.zhijbfje_m,y.zhijbfje_a,y.zhijbfje_v,y.zhijbfje_q,y.zhijbfje_s,y.zhijbfje_t,\n" + 
			"             y.suopje,y.lsuopsl,y.lsuopje\n" + 
//			"             y.diancrz\n" + 
			"  from (\n" + 
			"      select x.fenx,y.id yuetjkjb_id,g.mingc gmingc,j.mingc jmingc,p.mingc pmingc,yfs.mingc yfsmingc\n" + 
			"        from yuetjkjb y,\n" + 
			"             (select decode(0,0,'"+SysConstant.Fenx_Beny+"') fenx\n" + 
			"                from dual\n" + 
			"              union\n" + 
			"              select decode(0,0,'"+SysConstant.Fenx_Leij+"') fenx from dual) x,\n" + 
			"             gongysb g,\n" + 
			"             jihkjb j,\n" + 
			"             pinzb p,\n" + 
			"             yunsfsb yfs\n" + 
			"       where y.riq = to_date('" + strDate + "','yyyy-mm-dd')\n" + 
			"         and y.diancxxb_id="+getTreeid()+"\n" + 
			"         and y.gongysb_id=g.id\n" + 
			"         and y.jihkjb_id=j.id\n" + 
			"         and y.pinzb_id=p.id\n" + 
			"         and y.yunsfsb_id=yfs.id\n" + 
			"      ) s,yuezlb y,yueslb sl\n" + 
			"      where y.yuetjkjb_id(+)=s.yuetjkjb_id\n" + 
			"		 and sl.yuetjkjb_id(+)=s.yuetjkjb_id\n" +
	        "		 and sl.fenx(+)=s.fenx\n" +
			"        and y.fenx(+)=s.fenx\n" + 
			"      order by jmingc desc, gmingc, pmingc, y.id, s.yuetjkjb_id, s.fenx";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		// //设置表名称用于保存
		egu.setTableName("yuezlb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.getColumn("yuetjkjb_id").setHidden(true);
		
		egu.getColumn("yid").setHeader("yid");
		egu.getColumn("yid").setHidden(true);
		egu.getColumn("yid").setEditor(null);
		
		egu.getColumn("jingz").setHeader("jingz");
		egu.getColumn("jingz").setHidden(true);
		egu.getColumn("jingz").setEditor(null);

		egu.getColumn("qnet_ar").setHeader("低位热<br>(Qnet,ar)(MJ/kg)");
		egu.getColumn("qnet_ar").setWidth(80);
		egu.getColumn("qnet_ar").editor.setMinValue("0");
		
		egu.getColumn("mt").setHeader("全水<br>(Mt)(%)");
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mt").editor.setMinValue("0");
		
		egu.getColumn("mad").setHeader("空干基水<br>(Mad)(%)");
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("mad").editor.setMinValue("0");
		
		egu.getColumn("aar").setHeader("收到基<br>灰分(Aar)(%)");
		egu.getColumn("aar").setWidth(85);
		egu.getColumn("aar").editor.setMinValue("0");
		
		egu.getColumn("aad").setHeader("空干基<br>灰分(Aad)(%)");
		egu.getColumn("aad").setWidth(85);
		egu.getColumn("aad").editor.setMinValue("0");
		
		egu.getColumn("ad").setHeader("干基<br>灰分(Ad)(%)");
		egu.getColumn("ad").setWidth(85);
		egu.getColumn("ad").editor.setMinValue("0");
		egu.getColumn(str_v).setHeader("挥发分<br>("+str_v+")(%)");
		egu.getColumn(str_v).setWidth(65);
		egu.getColumn(str_v).editor.setMinValue("0");
		
		egu.getColumn("std").setHeader("干基硫<br>(St,d)(%)");
		egu.getColumn("std").setWidth(60);
		egu.getColumn("std").editor.setMinValue("0");
		
		egu.getColumn("had").setHeader("空干基氢<br>(Had)(%)");
		egu.getColumn("had").setWidth(60);
		egu.getColumn("had").editor.setMinValue("0");
		
		egu.getColumn("fcad").setHeader("固定碳<br>(Fcad)(%)");
		egu.getColumn("fcad").setWidth(60);
		egu.getColumn("fcad").editor.setMinValue("0");
		
		egu.getColumn("qnet_ar_KF").setHeader("矿方低位热<br>(Qnet,ar)(MJ/Kg)");
		egu.getColumn("qnet_ar_KF").setWidth(85);
		egu.getColumn("qnet_ar_KF").editor.setMinValue("0");
		
		egu.getColumn("mt_KF").setHeader("矿方全水<br>(Mt)(%)");
		egu.getColumn("mt_KF").setWidth(70);
		egu.getColumn("mt_KF").editor.setMinValue("0");
		
		egu.getColumn("mad_KF").setHeader("矿方空干基水<br>(Mad)(%)");
		egu.getColumn("mad_KF").setWidth(60);
		egu.getColumn("mad_KF").editor.setMinValue("0");
		
		egu.getColumn("aar_KF").setHeader("矿方收到基<br>灰分(Aar)(%)");
		egu.getColumn("aar_KF").setWidth(100);
		egu.getColumn("aar_KF").editor.setMinValue("0");
		
		egu.getColumn("aad_KF").setHeader("空干基<br>灰分(Aad)(%)");
		egu.getColumn("aad_KF").setWidth(85);
		egu.getColumn("aad_KF").editor.setMinValue("0");
		
		egu.getColumn("ad_KF").setHeader("干基<br>灰分(Ad)(%)");
		egu.getColumn("ad_KF").setWidth(85);
		egu.getColumn("ad_KF").editor.setMinValue("0");
		
		egu.getColumn("vdaf_KF").setHeader("矿方挥发分<br>(Vdaf)(%)");
		egu.getColumn("vdaf_KF").setWidth(80);
		egu.getColumn("vdaf_KF").editor.setMinValue("0");
		
		egu.getColumn("std_KF").setHeader("矿方干基硫<br>(St,d)(%)");
		egu.getColumn("std_KF").setWidth(80);
		egu.getColumn("std_KF").editor.setMinValue("0");
		
		egu.getColumn("had_KF").setHeader("矿方空干基氢<br>(Had)(%)");
		egu.getColumn("had_KF").setWidth(60);
		egu.getColumn("had_KF").editor.setMinValue("0");
		
		egu.getColumn("fcad_KF").setHeader("矿方固定碳<br>(Fcad)(%)");
		egu.getColumn("fcad_KF").setWidth(60);
		egu.getColumn("fcad_KF").editor.setMinValue("0");
		
		egu.getColumn("gmingc").setHeader("供货单位");
		egu.getColumn("gmingc").setEditor(null);
		egu.getColumn("gmingc").setWidth(80);
		egu.getColumn("gmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jmingc").setHeader("计划口径");
		egu.getColumn("jmingc").setEditor(null);
		egu.getColumn("jmingc").setWidth(70);
		egu.getColumn("jmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("pmingc").setHeader("品种");
		egu.getColumn("pmingc").setEditor(null);
		egu.getColumn("pmingc").setWidth(70);
		egu.getColumn("pmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("yfsmingc").setHeader("运输");
		egu.getColumn("yfsmingc").setEditor(null);
		egu.getColumn("yfsmingc").setWidth(60);
		egu.getColumn("yfsmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("yfsmingc").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
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
		egu.getColumn("ZHIJBFJE_A").setCenterHeader("质价不符灰分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_V").setCenterHeader("质价不符挥发分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_Q").setCenterHeader("质价不符热值(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_S").setCenterHeader("质价不符硫分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_T").setCenterHeader("质价不符灰熔点(元)", 4, 2);

		// 设定列的小数位
//		((NumberField) egu.getColumn("diancrz").editor).setDecimalPrecision(3);
		((NumberField) egu.getColumn("qnet_ar").editor).setDecimalPrecision(3);
		((NumberField) egu.getColumn("aar").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn(str_v).editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("ad").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("mt").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("std").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("qnet_ar_KF").editor)
				.setDecimalPrecision(2);
		((NumberField) egu.getColumn("aar_KF").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("vdaf_KF").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("ad_KF").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("mt_KF").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("std_KF").editor).setDecimalPrecision(2);

		egu.setDefaultsortable(false);
		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("月份");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		egu.addToolbarItem(comb2.getScript());
//		egu.addOtherScript("YuefDropDown.on('select',function(){document.forms[0].submit();});");
		
		
		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
					MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",
					true)).append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		// 删除按钮
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		

//		判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if(getZhangt(con)){
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		}else{
			//删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
			// 保存按钮
			egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton",MainGlobal.getExtMessageShow("正在保存数据,请稍后...", "保存中...", 200));
			Checkbox cb=new Checkbox();
			cb.setId("SelectLike");
			cb.setListeners("check:function(own,checked){if(checked){document.all.SelectLike.value='true'}else{document.all.SelectLike.value='false'}}");
			egu.addToolbarItem(cb.getScript());
			egu.addTbarText("是否自动计算累计值");

		}
		
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
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			visit.setShifsh(true);
			setYuefValue(null);
			setNianfValue(null);
			this.getYuefModels();
			this.getNianfModels();
			setRiq();
			this.setTreeid(null);
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
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString3());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString3();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString3(value);
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

	// 电厂树

	// 得到登陆用户所在电厂或者分公司的名称
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

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
	
	public boolean getIsSelectLike(){
		return ((Visit) this.getPage().getVisit()).getboolean8();
	}
	public String getSelectLike(){
		return ((Visit) this.getPage().getVisit()).getString8();
	}
	public void setSelectLike(String value){
		boolean flag = false;
		if("true".equals(value)){
			flag = true;
		}
		((Visit) this.getPage().getVisit()).setboolean8(flag);
	}


}
