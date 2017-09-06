package com.zhiren.jt.zdt.monthreport.changncwscsjwh;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Changnscsjwh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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


	private void Save() {
		
		
		JDBCcon con = new JDBCcon();
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
 
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		
		//
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange().replaceAll("&nbsp;", ""));//把getChange中的&nbsp;替换成"",否则ext不能识别
		
		String zidm1 = "";
		String zidm2 = "";
		StringBuffer sb = new StringBuffer();
		sb.append("update yuezbb set ");
		while (rsl.next()) {
			switch(rsl.getInt("LANC")){
				case 1:
					zidm1 = "haoytrm";
					zidm2 = "rulbzml";
					break;
				case 2:
					zidm1="rulmrz";
					zidm2="";
					break;
				case 3:
					zidm1="fadhml";
					zidm2="fadhbzml";
					break;
				case 4:
					zidm1="gongrhml";
					zidm2="gongrhbzml";
					break;
				
				case 5:
					zidm1="haoytry";
					zidm2="rulbzyl";
					break;
				case 6:
					zidm1="rulyrz";
					zidm2="";
					break;
				case 7:
					zidm1="fadhy";
					zidm2="fadhyzbzml";
					break;
				case 8:
					zidm1="gongrhy";
					zidm2="gongrhyzbzml";
					break;
				case 9:
					zidm1="meihl";
					zidm2="";
					break;
				case 10:
					zidm1="fadl";
					zidm2="fadbzmh";
					break;
				case 11:
					zidm1="gongdl";
					zidm2="gongdlbzmh";
					break;
				case 12:
					zidm1="gongrl";
					zidm2="gongrbzmh";
					break;
				case 13:
					zidm1="shangwdl";
					zidm2="";
					break;
//		--------------------------------------------------
				case 14:
					zidm1="shoudl";
					zidm2="";
					break;
//		--------------------------------------------------
                 case 15:
					zidm1="ranlcb_bhs";
					zidm2="";
					break;
                 case 16:
 					zidm1="fadmcb";
 					zidm2="fadtrmdj";
 					break;	
                 case 17:
  					zidm1="gongrmcb";
  					zidm2="gongrtrmdj";
  					break;
                 case 18:
  					zidm1="fadycb";
  					zidm2="fadtrydj";
  					break;
                 case 19:
  					zidm1="gongrycb";
  					zidm2="gongrtrydj";
  					break;
                 case 20:
  					zidm1="ranlcnfy";
  					zidm2="";
  					break;
//  	----------------------------------------------------
                 case 21:
   					zidm1="";
   					zidm2="";
   					break;
                 case 22:
   					zidm1="shouddwbdcb";
   					zidm2="";
   					break;
                 case 23:
   					zidm1="shouddwrlcb";
   					zidm2="";
   					break;
                 case 24:
   					zidm1="shouddwgdcb";
   					zidm2="";
   					break;
                 case 25:
   					zidm1="shouddj";
   					zidm2="";
   					break;
                 case 26:
   					zidm1="lirze";
   					zidm2="";
   					break;		
//  	----------------------------------------------------				
                 case 27:
  					zidm1="";
  					zidm2="";
  					break;
                 case 28:
  					zidm1="benqrcmgjsl";
  					zidm2="";
  					break;
                 case 29:
  					zidm1="benqrcmgjzje_bhs";
  					zidm2="";
  					break;
                 case 30:
                	zidm1="benqrcmgjdj_bhs";
      				zidm2="";
      				break; 
				default: break;
			}
			if(!zidm1.equals("")){
				sb.append(zidm1).append("=").append(rsl.getDouble("zhi1")).append(",");
			}
			
			if(!zidm2.equals("")){//当zidm2为空的时候不执行下面的语句
			sb.append(zidm2).append("=").append(rsl.getDouble("zhi2")).append(",");
			}
		}
		long diancxxb_id=Long.parseLong(this.getTreeid());
		sb.deleteCharAt(sb.length()-1);
		sb.append(" where ").append("riq=to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd')").append(" and");
		sb.append(" diancxxb_id="+diancxxb_id+" and fenx='本月'");
		con.getUpdate(sb.toString());
		
		//插入累计值
		String riq="'"+ intyear + "-"+ StrMonth + "-01'";
		int i=0;
		for(i=0;i<=12-intMonth;i++){
			String sqlljcx  = "select y.diancxxb_id,sum(y.haoytrm) as haoytrm,\n"
					+ "       decode(sum(y.haoytrm),0,0,sum(y.haoytrm*y.rulmrz)/sum(y.haoytrm)) as rulmrz,\n"
					+ "       sum(y.fadhml) as fadhml,sum(y.gongrhml) as gongrhml,sum(y.haoytry) as haoytry,\n"
					+ "       decode(sum(y.haoytry),0,0,sum(y.haoytry*y.rulyrz)/sum(y.haoytry)) as rulyrz,\n"
					+ "       sum(y.fadhy) as fadhy,sum(y.gongrhy) as gongrhy,\n"
					+ "       sum(y.fadl) as fadl,sum(y.gongdl) as gongdl,sum(y.gongrl) as gongrl,sum(y.shangwdl) as shangwdl,\n"
					+ "       sum(y.ranlcb_bhs) as ranlcb_bhs,sum(y.fadmcb) as fadmcb,sum(y.gongrmcb) as gongrmcb,\n"
					+ "       sum(y.fadycb) as fadycb,sum(y.gongrycb) as gongrycb,sum(y.ranlcnfy) as ranlcnfy,\n"
					+ "       sum(y.benqrcmgjsl) as benqrcmgjsl,sum(y.benqrcmgjzje_bhs) as benqrcmgjzje_bhs,\n"
					+ "       decode(sum(y.benqrcmgjsl),0,0,sum(y.benqrcmgjsl*y.benqrcmgjdj_bhs)/sum(y.benqrcmgjsl)) as benqrcmgjdj_bhs,\n"
					+ "       sum(y.rulbzml) as rulbzml,sum(y.fadhbzml) as fadhbzml,sum(y.gongrhbzml) as gongrhbzml,\n"
					+ "       sum(y.rulbzyl) as rulbzyl, sum(y.fadhyzbzml) as fadhyzbzml,sum(y.gongrhyzbzml) as gongrhyzbzml,\n"
					+ "       decode(sum(y.fadl),0,0, Round((sum(y.fadhbzml)/sum(y.fadl)*100),0)) as fadbzmh,\n"
					+ "       decode(sum(y.gongdl),0,0,Round((sum(y.fadhbzml)/sum(y.gongdl)*100),0)) as gongdlbzmh,\n"
					+ "       decode(sum(y.gongrl),0,0,Round((sum(y.gongrhbzml)/sum(y.gongrl)*1000),2)) as gongrbzmh,\n"
					+ "       decode(sum(y.fadhml),0,0, Round((sum(y.fadmcb)/sum(y.fadhml)),2)) as fadtrmdj,\n"
					+ "       decode(sum(y.gongrhml),0,0,Round(sum(y.gongrmcb)/sum(y.gongrhml),2)) as gongrtrmdj,\n"
					+ "       decode(sum(y.fadhy),0,0,Round(sum(y.fadycb)/sum(y.fadhy),2)) as fadtrydj,\n"
					+ "       decode(sum(y.gongrhy),0,0,Round(sum(y.gongrycb)/sum(y.gongrhy),2)) as gongrtrydj,\n"
					+"		  sum(y.shoudl) as shoudl,\n"
					+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddwbdcb)/sum(y.shoudl)) as shouddwbdcb,\n"
					+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddwrlcb)/sum(y.shoudl)) as shouddwrlcb,\n"
					+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddwgdcb)/sum(y.shoudl)) as shouddwgdcb,\n"
					+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddj)/sum(y.shoudl)) as shouddj,\n"
					+ "		  sum(y.lirze) as lirze \n"
					+ "from yuezbb y\n"
					+ " where y.diancxxb_id = "+diancxxb_id+"\n"
					+ "   and y.riq >= getYearFirstDate(to_date('"+intyear+"-"+(intMonth+i)+"-01', 'yyyy-mm-dd'))\n"
					+ "   and y.riq <= to_date('"+intyear+"-"+(intMonth+i)+"-01', 'yyyy-mm-dd')\n"
					+ "   and y.fenx='本月'\n" 
					+ " group by (y.diancxxb_id)";
			
			 ResultSetList rsllj = con.getResultSetList(sqlljcx); 
			 StringBuffer sqllj = new StringBuffer("begin \n");
			
			 while (rsllj.next()) {
				
				 sqllj.append("delete from yuezbb where diancxxb_id ="+diancxxb_id+" " +
				 	 " and riq=to_date('"+ intyear + "-"+ (intMonth+i) + "-01','yyyy-mm-dd') " +
				 		" and fenx='累计'").append(";\n");
				 
				 
				 long yuezbb_id= 0;
				 yuezbb_id = Long.parseLong(MainGlobal.getNewID(((Visit)getPage().getVisit()).getDiancxxb_id()));
				 sqllj.append("insert into yuezbb(id,diancxxb_id,riq,fenx,gongdl,haoytry,rulyrz,fadhy,gongrhy,fadl," +
							"gongrl,ranlcb_bhs,fadmcb,gongrmcb,fadycb,gongrycb,ranlcnfy,benqrcmgjsl,benqrcmgjzje_bhs,benqrcmgjdj_bhs" +
							",rulmrz,fadhml,gongrhml,rulbzml,fadhbzml,gongrhbzml,rulbzyl,fadhyzbzml,gongrhyzbzml,fadbzmh,gongdlbzmh," +
							"gongrbzmh,fadtrmdj,gongrtrmdj,fadtrydj,gongrtrydj,haoytrm,shangwdl,shoudl,shouddwbdcb,shouddwrlcb,shouddwgdcb,shouddj,lirze) values(\n"
				 + yuezbb_id
				 +","+diancxxb_id+""
				 +",to_date('"+ intyear + "-"+ (intMonth+i) + "-01','yyyy-mm-dd'),"
				 +"'累计',"+""
				 +""+rsllj.getDouble("gongdl")+","+""
				 +""+rsllj.getDouble("haoytry")+","+""
				 +""+rsllj.getDouble("rulyrz")+","+""
				 +""+rsllj.getDouble("fadhy")+""+""
				 +","+rsllj.getDouble("gongrhy")
				 +","+rsllj.getDouble("fadl")
				 
				 +","+rsllj.getDouble("gongrl")
				 +","+rsllj.getDouble("ranlcb_bhs")
				  +","+rsllj.getDouble("fadmcb")
				 +","+rsllj.getDouble("gongrmcb")
				 +","+rsllj.getDouble("fadycb")
				 +",Round("+rsllj.getDouble("gongrycb")+",2)"
				 +","+rsllj.getDouble("ranlcnfy")
				 +","+rsllj.getDouble("benqrcmgjsl")
				 +","+rsllj.getDouble("benqrcmgjzje_bhs")
				 +","+rsllj.getDouble("benqrcmgjdj_bhs")
				
				 +","+rsllj.getDouble("rulmrz")
				 +","+rsllj.getDouble("fadhml")
				 +","+rsllj.getDouble("gongrhml")
				 +","+rsllj.getDouble("rulbzml")
				 +","+rsllj.getDouble("fadhbzml")
				 +","+rsllj.getDouble("gongrhbzml")
				 +","+rsllj.getDouble("rulbzyl")
				 +","+rsllj.getDouble("fadhyzbzml")
				 +","+rsllj.getDouble("gongrhyzbzml")
				 +","+rsllj.getDouble("fadbzmh")
				 +","+rsllj.getDouble("gongdlbzmh")
				 
				 +","+rsllj.getDouble("gongrbzmh")
				 +","+rsllj.getDouble("fadtrmdj")
				 +","+rsllj.getDouble("gongrtrmdj")
				 +","+rsllj.getDouble("fadtrydj")
				 +","+rsllj.getDouble("gongrtrydj")
				 +","+rsllj.getDouble("haoytrm")
				 +","+rsllj.getDouble("shangwdl")
				 +","+rsllj.getDouble("shoudl")
				 +","+rsllj.getDouble("shouddwbdcb")
				 +","+rsllj.getDouble("shouddwrlcb")
				 +","+rsllj.getDouble("shouddwgdcb")
				 +","+rsllj.getDouble("shouddj")
				 +","+rsllj.getDouble("lirze")
				 +");\n");
						
			 }
			 sqllj.append("end;");
			 
			 con.getInsert(sqllj.toString());

		}	
		//---------------------
		con.Close();
		setMsg("保存成功!");
	}
	
	public ResultSetList LeijSelect(long diancxxb_id,String riq) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sqllj  = "select y.diancxxb_id,sum(y.haoytrm) as haoytrm,\n"
				+ "       decode(sum(y.haoytrm),0,0,sum(y.haoytrm*y.rulmrz)/sum(y.haoytrm)) as rulmrz,\n"
				+ "       sum(y.fadhml) as fadhml,sum(y.gongrhml) as gongrhml,sum(y.haoytry) as haoytry,\n"
				+ "       decode(sum(y.haoytry),0,0,sum(y.haoytry*y.rulyrz)/sum(y.haoytry)) as rulyrz,\n"
				+ "       sum(y.fadhy) as fadhy,sum(y.gongrhy) as gongrhy,\n"
				+ "       sum(y.fadl) as fadl,sum(y.gongdl) as gongdl,sum(y.gongrl) as gongrl,sum(y.shangwdl) as shangwdl,\n"
				+ "       sum(y.ranlcb_bhs) as ranlcb_bhs,sum(y.fadmcb) as fadmcb,sum(y.gongrmcb) as gongrmcb,\n"
				+ "       sum(y.fadycb) as fadycb,sum(y.gongrycb) as gongrycb,sum(y.ranlcnfy) as ranlcnfy,\n"
				+ "       sum(y.benqrcmgjsl) as benqrcmgjsl,sum(y.benqrcmgjzje_bhs) as benqrcmgjzje_bhs,\n"
				+ "       decode(sum(y.benqrcmgjsl),0,0,sum(y.benqrcmgjsl*y.benqrcmgjdj_bhs)/sum(y.benqrcmgjsl)) as benqrcmgjdj_bhs,\n"
				+ "       sum(y.rulbzml) as rulbzml,sum(y.fadhbzml) as fadhbzml,sum(y.gongrhbzml) as gongrhbzml,\n"
				+ "       sum(y.rulbzyl) as rulbzyl, sum(y.fadhyzbzml) as fadhyzbzml,sum(y.gongrhyzbzml) as gongrhyzbzml,\n"
				+ "       decode(sum(y.fadl),0,0, Round((sum(y.fadhbzml)/sum(y.fadl)*100),0)) as fadbzmh,\n"
				+ "       decode(sum(y.gongdl),0,0,Round((sum(y.fadhbzml)/sum(y.gongdl)*100),0)) as gongdlbzmh,\n"
				+ "       decode(sum(y.gongrl),0,0,Round((sum(y.gongrhbzml)/sum(y.gongrl)*1000),2)) as gongrbzmh,\n"
				+ "       decode(sum(y.fadhml),0,0, Round((sum(y.fadmcb)/sum(y.fadhml)),2)) as fadtrmdj,\n"
				+ "       decode(sum(y.gongrhml),0,0,Round(sum(y.gongrmcb)/sum(y.gongrhml),2)) as gongrtrmdj,\n"
				+ "       decode(sum(y.fadhy),0,0,Round(sum(y.fadycb)/sum(y.fadhy),2)) as fadtrydj,\n"
				+ "       decode(sum(y.gongrhy),0,0,Round(sum(y.gongrycb)/sum(y.gongrhy),2)) as gongrtrydj,\n"
				+"		  sum(y.shoudl) as shoudl,\n"
				+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddwbdcb)/sum(y.shoudl)) as shouddwbdcb,\n"
				+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddwrlcb)/sum(y.shoudl)) as shouddwrlcb,\n"
				+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddwgdcb)/sum(y.shoudl)) as shouddwgdcb,\n"
				+ "       decode(sum(y.shoudl),0,0,sum(y.shoudl*y.shouddj)/sum(y.shoudl)) as shouddj,\n"
				+ "		  sum(y.lirze) as lirze \n"
				+ "from yuezbb y\n"
				+ " where y.diancxxb_id = "+diancxxb_id+"\n"
				+ "   and y.riq >= getYearFirstDate(to_date("+riq+", 'yyyy-mm-dd'))\n"
				+ "   and y.riq <= to_date("+riq+", 'yyyy-mm-dd')\n"
				+ "   and y.fenx='本月'\n" 
				+ " group by (y.diancxxb_id)";

		ResultSetList rsllj = con.getResultSetList(sqllj); 
		con.Close();
		return rsllj;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _InsertButton = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertButton = true;
	}
	private boolean _DeleteButton = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteButton = true;
	}
	
	private boolean _ShenhClick = false;
	
	public void ShenhButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}
	private boolean _ReturnClick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
		if(_InsertButton){
			_InsertButton=false;
			Insert();
			getSelectData();
		}
		if(_DeleteButton){
			_DeleteButton = false;
			Delete();
			getSelectData();
		}
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
			getSelectData();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			Return();
			getSelectData();
		}

	}
	public void Shenh(){				//	审核按钮
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//--------------------------------
		String CurrODate = DateUtil.FormatOracleDate(intyear+"-"+StrMonth+"-01");
		String chaxzt="";
		String zhuangt="";
		Visit visit = (Visit) getPage().getVisit();

		// ----------电厂树--------------
		String str = "";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// 选集团时刷新出所有的电厂
				str = "";
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				str = "and dc.id = " + getTreeid() + "";
			}
		//查询数据时的状态
		if(visit.getRenyjb()==3){

		}else if(visit.getRenyjb()==2){
			chaxzt=" and y.zhuangt=1";
		}else if(visit.getRenyjb()==1){
			chaxzt=" and y.zhuangt=2";
		}
		//点击审核改变的状态
		if(visit.getRenyjb()==3){
			zhuangt="zhuangt=1"; 
		}else if(visit.getRenyjb()==2){
			zhuangt=" zhuangt=2";
		}
		
		JDBCcon con = new JDBCcon();
		String sqlcx=	"select y.* \n" + 
		" from yuezbb y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id "+str+"\n" + 
		"       and y.fenx='本月' and y.riq="+CurrODate+chaxzt+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");		
		while (rsl.next()) {
	
			sql.append("update yuezbb set "+zhuangt	 + " where id=" + rsl.getLong("id")+";\n");
			

			String tongjkj_id = "select distinct y.id as id from yuezbb y,diancxxb dc" +
								" where  y.fenx='累计' and y.diancxxb_id=dc.id "+str+" and y.riq="+CurrODate;
			ResultSet rs1 = con.getResultSet(tongjkj_id);
		
			StringBuffer sqllj =new StringBuffer();
			try {
				while (rs1.next()) {
					 sqllj.append("begin \n");
					 sqllj.append("update yuezbb set "+zhuangt+ " where id=" + rs1.getLong("id")+";\n");
						 sqllj.append("end;");
				 }
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			 con.getUpdate(sqllj.toString());
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}

	public void Return(){
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//--------------------------------
		String CurrODate = DateUtil.FormatOracleDate(intyear+"-"+StrMonth+"-01");
		String chaxzt="";
		String zhuangt="";
		Visit visit = (Visit) getPage().getVisit();
		// ----------电厂树--------------
		String str = "";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// 选集团时刷新出所有的电厂
				str = "";
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				str = "and dc.id = " + getTreeid() + "";
			}
		//查询数据时的状态
		if(visit.getRenyjb()==3){

		}else if(visit.getRenyjb()==2){
			chaxzt=" and y.zhuangt=1";
		}else if(visit.getRenyjb()==1){
			chaxzt=" and y.zhuangt=2";
		}
		//点击审核改变的状态
		if(visit.getRenyjb()==1){
			zhuangt="zhuangt=1";
		}else if(visit.getRenyjb()==2){
			zhuangt=" zhuangt=0";
		}

		JDBCcon con = new JDBCcon();
		String sqlcx=	"select y.* \n" + 
		" from yuezbb y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id "+str+"\n" + 
		"       and y.fenx='本月' and y.riq="+CurrODate+chaxzt+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("begin \n");		
		while (rsl.next()) {
			result++;
			if(visit.getRenyjb()==1){
				if(rsl.getLong("zhuangt")==2){
					sql.append("update yuezbb set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
				
					String tongjkj_id = "select distinct y.id as id from yuezbb y,diancxxb dc" +
					" where  y.fenx='累计' and y.diancxxb_id=dc.id "+str+" and y.riq="+CurrODate;
					
					ResultSet rs1 = con.getResultSet(tongjkj_id);
				
					StringBuffer sqllj =new StringBuffer();
						 try {
							while (rs1.next()) {
								 sqllj.append("begin \n");
								 sqllj.append("update yuezbb set "+zhuangt + " where id=" + rs1.getLong("id")+";\n");
									 sqllj.append("end;");
							 }
						} catch (SQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						}
					 con.getUpdate(sqllj.toString());
					
				}
			}else if(visit.getRenyjb()==2){
				if(rsl.getLong("zhuangt")==1){
					sql.append("update yuezbb set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
				
					String tongjkj_id = "select distinct y.id as id from yuezbb y,diancxxb dc" +
					" where  y.fenx='累计' and y.diancxxb_id=dc.id "+str+" and y.riq="+CurrODate;
					
					ResultSet rs1 = con.getResultSet(tongjkj_id);
				
					StringBuffer sqllj =new StringBuffer();
						 try {
							while (rs1.next()) {
								 sqllj.append("begin \n");
								 sqllj.append("update yuezbb set "+zhuangt + " where id=" + rs1.getLong("id")+";\n");
								 sqllj.append("end;");
							 }
						 } catch (SQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						 }
						 con.getUpdate(sqllj.toString());
				}
			}
		}	
		sql.append("end;");
		if(result>0){
			con.getUpdate(sql.toString());
		}
//		con.getUpdate(sql.toString());
		
		con.Close();
	}
	public void Delete(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//删除本月和累计的数据
		long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		String DeleteStr="delete yuezbb y where y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
				"and y.diancxxb_id="+diancxxb_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	public void Insert(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		
		long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		String isHaveZhi="select * from yuezbb y where y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
				"and y.diancxxb_id="+diancxxb_id+" and y.fenx='本月'";
		ResultSetList rssy=con.getResultSetList(isHaveZhi);
		//插入时判断是否有值
		if(rssy.next()){
			con.Close();
			return;
		}else{
			con.getInsert("insert into yuezbb(id,diancxxb_id,riq,fenx,gongdl,danwrlcb,haoytry,rulyrz,fadhy,gongrhy,meihl,fadl," +
				"gongrl,ranlcb_bhs,fadmcb,gongrmcb,fadycb,gongrycb,ranlcnfy,gujqk,benqrcmgjsl,benqrcmgjzje_bhs,benqrcmgjdj_bhs" +
				",rulmrz,fadhml,gongrhml,rulbzml,fadhbzml,gongrhbzml,rulbzyl,fadhyzbzml,gongrhyzbzml,fadbzmh,gongdlbzmh," +
				"gongrbzmh,fadtrmdj,gongrtrmdj,fadtrydj,gongrtrydj,haoytrm,shangwdl,shoudl,shouddwbdcb,shouddwrlcb,shouddwgdcb,shouddj,lirze) values(\n"
				+id+","+diancxxb_id+",to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd'),'本月',0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0,0,0,0)");
		}
		
		con.Close();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhuangt="";
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (y.zhuangt=1 or y.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and y.zhuangt=2";
			}
		}		
		//工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		//-----------------------------------
		String str = "";
		
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		
		}
//		---------------------------------------------------------------------	
		boolean isReture = false ;
		if(visit.getRenyjb()==1){
			if(treejib==1){
				isReture=true;
			}
		}else if(visit.getRenyjb()==2){
			if(treejib==2){
				isReture=true;
			}
		}

//		if(isReture) {
//			setMsg("如果有需要回退信息时，请选择所要回退的公司或电厂名称！");
//		}
//	----------------------------------------------------------------------	
	
		String chaxun  = "select tj.lanc,tj.mingc1,tj.danw,tj.zhi1,tj.mingc2,tj.danw2,tj.zhi2,tj.zhuangt\n"
				+ "from (\n"
				+ "select 1 as lanc,'耗用天然煤' as mingc1,'吨' as danw,y.haoytrm as zhi1 ,'入炉煤折标准煤量'as mingc2,'吨' as danw2,y.rulbzml as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 2 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入炉煤热值' as mingc1,'Mj/kg' as danw,y.rulmrz as zhi1,''as mingc2,''as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 3 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电耗煤量' as mingc1,'吨' as danw,y.fadhml as zhi1,'发电耗标准煤量'as mingc2,'吨' as danw2, y.fadhbzml as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 4 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热耗煤量' as mingc1,'吨' as danw,y.gongrhml as zhi1,'供热耗标准煤量'as mingc2,'吨' as danw2, y.gongrhbzml as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 5 as lanc,'耗用天然油' as mingc1,'吨' as danw,y.haoytry as zhi1,'入炉油折标准煤量'as mingc2,'吨' as danw2, y.rulbzyl as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 6 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入炉油热值' as mingc1,'Mj/kg' as danw,y.rulyrz as zhi1,''as mingc2,''as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 7 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电耗油' as mingc1,'吨' as danw,y.fadhy as zhi1,'发电耗油折标准煤量'as mingc2,'吨' as danw2, y.fadhyzbzml as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 8 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热耗油' as mingc1,'吨' as danw,y.gongrhy as zhi1,'供热耗油折标准煤量'as mingc2,'吨' as danw2, y.gongrhyzbzml as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 9 as lanc,'煤耗率' as mingc1,'' as danw,null as zhi1,''as mingc2,''as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 10 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电量' as mingc1,'万千瓦时' as danw,y.fadl as zhi1,'发电标准煤耗'as mingc2,'克/千瓦时' as danw2, y.fadbzmh as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 11 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供电量' as mingc1,'万千瓦时' as danw,y.gongdl as zhi1,'供电量标准煤耗'as mingc2,'克/千瓦时' as danw2, y.gongdlbzmh as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 12 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热量' as mingc1,'吉焦' as danw,y.gongrl as zhi1,'供热标准煤耗'as mingc2,'千克/吉焦' as danw2, y.gongrbzmh as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+"select 13 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上网电量' as mingc1,'万千瓦时' as danw,y.shangwdl as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n" 
				+"  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
//				-------------------------------------------------------------------------------------------------------------------
				+ "union\n"
				+"select 14 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电量' as mingc1,'万千瓦时' as danw,y.shoudl as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n" 
				+"  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
//				-------------------------------------------------------------------------------------------------------------------
				+ "union\n"
				+ "select 15 as lanc,'燃料成本(不含税)' as mingc1,'元' as danw,y.ranlcb_bhs as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 16 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电煤成本' as mingc1,'元' as danw,y.fadmcb as zhi1,'发电天然煤单价'as mingc2,'元/吨' as danw2, y.fadtrmdj as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 17 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热煤成本' as mingc1,'元' as danw,y.gongrmcb as zhi1,'供热天然煤单价'as mingc2,'元/吨' as danw2, y.gongrtrmdj as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 18 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电油成本' as mingc1,'元' as danw,y.fadycb as zhi1,'发电天然油单价'as mingc2,'元/吨' as danw2, y.fadtrydj as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 19 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热油成本' as mingc1,'元' as danw,y.gongrycb as zhi1,'供热天然油单价'as mingc2,'元/吨' as danw2, y.gongrtrydj as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 20 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;燃料厂内费用' as mingc1,'元' as danw,y.ranlcnfy as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
//				------------------------------------------------------------------------------------------------------------------
				+ "union\n"
				+ "select 21 as lanc,'售电情况' as mingc1,'' as danw,null as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 22 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电单位变动成本' as mingc1,'元/千千瓦时' as danw,y.shouddwbdcb as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 23 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电单位燃料成本' as mingc1,'元/千千瓦时' as danw,y.shouddwrlcb as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 24 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电单位固定成本' as mingc1,'元/千千瓦时' as danw,y.shouddwgdcb as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 25 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电单价' as mingc1,'元/千千瓦时' as danw,y.shouddj as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 26 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;利润总额' as mingc1,'万元' as danw,y.lirze as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
//				------------------------------------------------------------------------------------------------------------------
				+ "union\n"
				+ "select 27 as lanc,'估价情况' as mingc1,'' as danw,null as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 28 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本期入厂煤估价数量' as mingc1,'吨' as danw,y.benqrcmgjsl as zhi1,''as mingc2, '' as danw2,null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ "union\n"
				+ "select 29 as lanc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本期入厂煤估价总金额(不含税)' as mingc1,'元' as danw,y.benqrcmgjzje_bhs as zhi1,''as mingc2,'' as danw2, null as zhi2,y.zhuangt\n"
				+ "  from yuezbb y where y.fenx='本月' and y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and y.diancxxb_id="+this.getTreeid()+zhuangt+"\n"
				+ ") tj order by tj.lanc";

		  
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
	boolean yincan=false;
	while(rsl.next()){
		if(visit.getRenyjb()==3){
			if(rsl.getLong("zhuangt")==0){
				yincan = false;
			}else{
				yincan = true;
			}
		}else if(visit.getRenyjb()==2||visit.getRenyjb()==1){
				yincan = true;
		}
	}
	rsl.beforefirst();
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("yuezbb");
   	
	egu.getColumn("lanc").setCenterHeader("栏次");
	egu.getColumn("mingc1").setCenterHeader("项目");

	egu.getColumn("danw").setCenterHeader("单位");
	egu.getColumn("zhi1").setCenterHeader("完成值");
	egu.getColumn("mingc2").setCenterHeader("折算项目");
	egu.getColumn("danw2").setCenterHeader("折算单位");
	egu.getColumn("zhi2").setCenterHeader("值");
	egu.getColumn("lanc").setEditor(null);
	egu.getColumn("mingc1").setEditor(null);
	egu.getColumn("danw").setEditor(null);
	egu.getColumn("mingc2").setEditor(null);
	//egu.getColumn("zhi2").setEditor(null);//设置最后一列是否可以编辑
	egu.getColumn("danw2").setEditor(null);
	egu.getColumn("zhuangt").setCenterHeader("状态");
	egu.getColumn("zhuangt").setHidden(true);
	egu.getColumn("zhuangt").setEditor(null);
//	设定不可编辑列的颜色
	egu.getColumn("lanc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("mingc1").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
	egu.getColumn("danw").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	//设定单元格的底色
	egu.getColumn("zhi1").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex==0||rowIndex==4||rowIndex==8||rowIndex==14||rowIndex==20||rowIndex==26){metadata.css='tdTextext3';} return value;}");
	egu.getColumn("mingc2").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("danw2").setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
	egu.getColumn("zhi2").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
	egu.setDefaultsortable(false);//设定页面不自动排序
	
	//设定列初始宽度
	egu.getColumn("lanc").setWidth(70);
	egu.getColumn("lanc").setAlign("right");
	egu.getColumn("mingc1").setWidth(200);
	egu.getColumn("danw").setWidth(80);
	egu.getColumn("danw2").setWidth(60);
	egu.getColumn("zhi1").setWidth(80);
	egu.getColumn("mingc2").setWidth(150);
	egu.getColumn("zhi2").setWidth(80);
	
//	设定列的小数位
	//((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(100);//设置分页
	egu.setWidth(1000);//设置页面的宽度,当超过这个宽度时显示滚动条
	
	
	

	
	
	//*************************下拉框*****************************************88
	
	//********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("月份:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");//设置分隔符
		//设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		
		 
		
		//设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NIANF').value+'年'+Ext.getDom('YUEF').value+'月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		if(visit.getRenyjb()==3){
			if(yincan ==false){
		//		添加按钮
				StringBuffer ins = new StringBuffer();
				ins.append("function (){")
				.append("document.getElementById('InsertButton').click();}");
				GridButton gbIns = new GridButton("添加",ins.toString());
				gbIns.setIcon(SysConstant.Btn_Icon_Insert);
				egu.addTbarBtn(gbIns);
		//		删除按钮
		
				String ss="Ext.MessageBox.confirm('警告', '你确定要删除本月所有的数据吗？', function(btn) { if(btn=='yes'){document.getElementById('DeleteButton').click();}})";
				StringBuffer del = new StringBuffer();
				del.append("function (){")
				.append(""+ss+"}");
				GridButton gbDel = new GridButton("删除",del.toString());
				gbDel.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbDel);
				
				//保存按钮
				
				egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",MainGlobal.getExtMessageShow("正在保存数据,请稍后...", "保存中...", 200));
			}
		}
//		审核按钮
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==2 || visit.getRenyjb()==3){
				GridButton gsh = new GridButton("审核","function (){document.getElementById('ShenhButton').click();}");
				gsh.setIcon(SysConstant.Btn_Icon_Search);
				egu.addTbarBtn(gsh);
			}
		}
		
//		回退按钮
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==2 || visit.getRenyjb()==1){
				GridButton ght = new GridButton("回退","function (){document.getElementById('ReturnButton').click();}");
				ght.setDisabled(isReture);
				ght.setIcon(SysConstant.Btn_Icon_Return);
				egu.addTbarBtn(ght);
			}
		} 
			
		egu.addTbarText("->");
		
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		//设定某一行不能编辑
		sb.append("if(e.record.get('LANC')=='1'||e.record.get('LANC')=='5'||e.record.get('LANC')=='9'||e.record.get('LANC')=='15'||e.record.get('LANC')=='21'||e.record.get('LANC')=='27'||e.record.get('LANC')=='30'){e.cancel=true;}");//
		
		sb.append("});");
		
		
	
		sb.append("gridDiv_grid.on('afteredit',function(e){");
		
			//计算耗用天然煤
			sb.append("if(e.row==1 || e.row==2||e.row==3){")
					.append("rec = gridDiv_ds.getAt(0);")
					.append("rulmzr=gridDiv_ds.getAt(1);")
					.append("recfadhm= gridDiv_ds.getAt(2);")
					.append("recgongrhm= gridDiv_ds.getAt(3);")
					.append("if(e.column==4){")        //值为第四列时发生计算,当位第七列时执行else语句
						.append("recfadhm.set('ZHI1', Round(eval(recfadhm.get('ZHI1')||0),2) );")
						.append("recgongrhm.set('ZHI1', Round(eval(recgongrhm.get('ZHI1')||0),2) );")
						.append("rec.set('ZHI1', Round(eval(recfadhm.get('ZHI1')||0) + eval(recgongrhm.get('ZHI1')||0),2));")
						.append("recfadhm.set('ZHI2', Round((eval(recfadhm.get('ZHI1')||0) * eval(rulmzr.get('ZHI1')||0)/29.271),2));")
						.append("recgongrhm.set('ZHI2', Round((eval(recgongrhm.get('ZHI1')||0) * eval(rulmzr.get('ZHI1')||0)/29.271),2));")
						.append("rec.set('ZHI2', Round(eval(recfadhm.get('ZHI2')||0) + eval(recgongrhm.get('ZHI2')||0),2));")
					.append("}else{")
						.append("rec.set('ZHI2', Round(eval(recfadhm.get('ZHI2')||0) + eval(recgongrhm.get('ZHI2')||0),2));")
					.append("}")
			.append("}");
			
			//计算耗用天然油
			sb.append("if(e.row==5||e.row==6||e.row==7){")
			.append("haoytry = gridDiv_ds.getAt(4);")
			.append("rulyrz=gridDiv_ds.getAt(5);")
			.append("fadhy=gridDiv_ds.getAt(6);")
			.append("gongrhy=gridDiv_ds.getAt(7);")
				.append("if(e.column==4){")        //值为第四列时发生计算,当位第七列时执行else语句
					.append("fadhy.set('ZHI1',Round(eval(fadhy.get('ZHI1')||0),2));")
					.append("gongrhy.set('ZHI1',Round(eval(gongrhy.get('ZHI1')||0),2));")
					.append("haoytry .set('ZHI1', Round(eval(fadhy.get('ZHI1')||0) + eval(gongrhy.get('ZHI1')||0),2));")
					.append("fadhy.set('ZHI2', Round((eval(fadhy.get('ZHI1')||0) * eval(rulyrz.get('ZHI1')||0)/29.271),2));")
					.append("gongrhy.set('ZHI2', Round((eval(gongrhy.get('ZHI1')||0) * eval(rulyrz.get('ZHI1')||0)/29.271),2));")
					.append("haoytry .set('ZHI2',  Round(eval(fadhy.get('ZHI2')||0) + eval(gongrhy.get('ZHI2')||0),2));")
				.append("}else{")
					.append("haoytry .set('ZHI2',  Round(eval(fadhy.get('ZHI2')||0) + eval(gongrhy.get('ZHI2')||0),2));")
				.append("}")
			.append("}");
			//计算煤耗率
			sb.append("if(e.row==9||e.row==10||e.row==11){")
			.append("if(e.column==4){")  
			.append("fadl=gridDiv_ds.getAt(9);")
			.append("gongdl=gridDiv_ds.getAt(10);")
			.append("gongrl=gridDiv_ds.getAt(11);")
			.append("recfadhm= gridDiv_ds.getAt(2);")
			.append("recfadhy= gridDiv_ds.getAt(6);")
			.append("recgongrhm= gridDiv_ds.getAt(3);")
			.append("recgongrhy= gridDiv_ds.getAt(7);")
			.append("if(e.row==9){").append("fadl .set('ZHI2', Round(((eval(recfadhm.get('ZHI2')||0)+eval(recfadhy.get('ZHI2')||0)) / eval(fadl.get('ZHI1')||0)*100),1));}")
			.append("if(e.row==10){").append("gongdl .set('ZHI2', Round(((eval(recfadhm.get('ZHI2')||0)+eval(recfadhy.get('ZHI2')||0)) / eval(gongdl.get('ZHI1')||0)*100),1));}")
			.append("if(e.row==11){").append("gongrl .set('ZHI2', Round(((eval(recgongrhm.get('ZHI2')||0)+eval(recgongrhy.get('ZHI2')||0)) / eval(gongrl .get('ZHI1')||0)*1000),2));}")
			.append("}")
			.append("}");
			
			//计算燃料成本
			sb.append("if(e.row==15||e.row==16||e.row==17||e.row==18||e.row==19){").append("recfadhm= gridDiv_ds.getAt(2);")
			.append("if(e.column==4){")  
			.append("recgongrhm= gridDiv_ds.getAt(3);")
			.append("fadhy=gridDiv_ds.getAt(6);")
			.append("gongrhy=gridDiv_ds.getAt(7);")
			.append("ranlcb=gridDiv_ds.getAt(14);")
			.append("fadmcb=gridDiv_ds.getAt(15);")
			.append("gongrmcb=gridDiv_ds.getAt(16);")
			.append("fadycb=gridDiv_ds.getAt(17);")
			.append("gongrycb=gridDiv_ds.getAt(18);")
			
			.append("ranlcnfy=gridDiv_ds.getAt(19);")
			.append("fadmcb.set('ZHI1',Round(eval(fadmcb.get('ZHI1')||0),2));")
			.append("gongrmcb.set('ZHI1',Round(eval(gongrmcb.get('ZHI1')||0),2));")
			.append("fadycb.set('ZHI1',Round(eval(fadycb.get('ZHI1')||0),2));")
			.append("gongrycb.set('ZHI1',Round(eval(gongrycb.get('ZHI1')||0),2));")
			.append("ranlcnfy.set('ZHI1',Round(eval(ranlcnfy.get('ZHI1')||0),2));")
			
			.append("ranlcb.set('ZHI1', Round(eval(fadmcb.get('ZHI1')||0) + eval(gongrmcb.get('ZHI1')||0)+eval(fadycb.get('ZHI1')||0)+eval(gongrycb.get('ZHI1')||0),2));")
			.append("if(e.row==15){fadmcb.set('ZHI2', Round((eval(fadmcb.get('ZHI1')||0) /eval(recfadhm.get('ZHI1')||0)),2));}")
			.append("if(e.row==16){gongrmcb.set('ZHI2', Round((eval(gongrmcb.get('ZHI1')||0) /eval(recgongrhm.get('ZHI1')||0)),2));}")
			.append("if(e.row==17){fadycb.set('ZHI2', Round((eval(fadycb.get('ZHI1')||0) /eval(fadhy.get('ZHI1')||0)),2));}")
			.append("if(e.row==18){gongrycb.set('ZHI2', Round((eval(gongrycb.get('ZHI1')||0) /eval(gongrhy.get('ZHI1')||0)),2));}")
			.append("}")
			.append("}");
			
			//计算入厂煤估价单价
			sb.append(" if(e.row==27||e.row==28){")
			.append("rucmgjsl=gridDiv_ds.getAt(27);")
			.append("rucmgjzje=gridDiv_ds.getAt(28);")
			.append("rucmgjdj=gridDiv_ds.getAt(29);")
			.append("rucmgjsl.set('ZHI1',Round(eval(rucmgjsl.get('ZHI1')||0),2));")
			.append("rucmgjzje.set('ZHI1',Round(eval(rucmgjzje.get('ZHI1')||0),2));")
			.append("rucmgjdj.set('ZHI1', Round((eval(rucmgjzje.get('ZHI1')||0) /eval(rucmgjsl.get('ZHI1')||0)),2));")
			.append("}");
			
		sb.append("});");
		
	
		
		
      
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		
		
		
		
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
			this.setTreeid(null);
			this.setYuefValue(null);
			this.getYuefModels();
			visit.setShifsh(true);
			setTbmsg(null);
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
		if (_NianfValue == null||_NianfValue.equals("")) {
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

	// 月份
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	


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
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
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
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
}