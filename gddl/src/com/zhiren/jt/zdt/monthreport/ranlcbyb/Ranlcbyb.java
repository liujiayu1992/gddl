package com.zhiren.jt.zdt.monthreport.ranlcbyb;

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

public class Ranlcbyb extends BasePage implements PageValidateListener {
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
		
		String zidm = "";
		String beiz="";
		StringBuffer sb = new StringBuffer();
		sb.append("update ranlcb set ");
		while (rsl.next()) {
			switch(rsl.getInt("LANC")){
				case 1:
					zidm = "fadgrrlf";
					break;
				case 2:
					zidm="fadgrrlfrm";
					break;
				case 3:
					zidm="fadgrrlfry";
					break;
				case 4:
					zidm="fadgrrlfrq";
					break;
				case 5:
					zidm="fadrlf";
					break;
				case 6:
					zidm="fadrlfrm";
					break;
				case 7:
					zidm="fadrlfry";
					break;
				case 8:
					zidm="fadrlfrq";
					break;
				case 9:
					zidm="gongrrlf";
					break;
				case 10:
					zidm="gongrrlfrm";
					break;
				case 11:
					zidm="gongrrlfry";
					break;
				case 12:
					zidm="gongrrlfrq";
					break;
				case 13:
					zidm="gongrydftrlf";
					break;
				case 14:
					zidm="gongrydrlfrm";
					break;
				case 15:
					zidm="gongrydrlfry";
					break;
                 case 16:
					zidm="gongrydrlfrq";
					break;
                 case 17:
 					zidm="fadgrtrml";
 					break;	
                 case 18:
  					zidm="fadtrml";
  					break;
                 case 19:
  					zidm="gongrtrml";
  					break;
                 case 20:
  					zidm="fadgrtryl";
  					break;
                 case 21:
  					zidm="fadtryl";
  					break;
                 case 22:
   					zidm="gongrtryl";
   					break;
                 case 23:
   					zidm="fadgrtrql";
   					break;
                 case 24:
   					zidm="fadtrql";
   					break;
                 case 25:
   					zidm="gongrtrql";
   					break;
                 case 26:
   					zidm="fadgrtrmdj";
   					break;
                 case 27:
   					zidm="fadgrtrydj";
   					break;		
                 case 28:
  					zidm="fadgrtrqdj";
  					break;
                 case 29:
  					zidm="fadtrmdj";
  					break;
                 case 30:
  					zidm="fadtrydj";
  					break;
                 case 31:
                	zidm="fadtrqdj";
      				break; 
                 case 32:
                 	zidm="gongrtrmdj";
       				break; 
                 case 33:
                 	zidm="gongrtrydj";
       				break; 
                 case 34:
                 	zidm="gongrtrqdj";
       				break; 
                 case 35:
                 	zidm="fadgrzbml";
       				break; 
                 case 36:
                 	zidm="fadgrmzbml";
       				break; 
                 case 37:
                 	zidm="fadgryzbml";
       				break; 
                 case 38:
                 	zidm="fadgrqzbml";
       				break; 
                 case 39:
                 	zidm="fadbml";
       				break; 
                 case 40:
                 	zidm="fadmzbml";
       				break; 
                 case 41:
                 	zidm="fadyzbml";
       				break; 
                 case 42:
                 	zidm="fadqzbml";
       				break; 
                 case 43:
                 	zidm="gongrbml";
       				break; 
                 case 44:
                 	zidm="gongrmzbml";
       				break; 
                 case 45:
                 	zidm="gongryzbml";
       				break; 
                 case 46:
                 	zidm="gongrqzbml";
       				break; 
                 case 47:
                 	zidm="rulzhbmj";
       				break; 
                 case 48:
                 	zidm="rulmbmdj";
       				break; 
                 case 49:
                 	zidm="rulybmdj";
       				break; 
                 case 50:
                 	zidm="rulqbmdj";
       				break; 
                 case 51:
                 	zidm="fadbmdj";
       				break; 
                 case 52:
                 	zidm="fadmzbmdj";
       				break; 
                 case 53:
                 	zidm="fadyzbmdj";
       				break; 
                 case 54:
                 	zidm="fadqzbmdj";
       				break; 
                 case 55:
                 	zidm="gongrbmdj";
       				break; 
                 case 56:
                 	zidm="gongrmzbmdj";
       				break; 
                 case 57:
                 	zidm="gongryzbmdj";
       				break; 
                 case 58:
                 	zidm="gongrqzbmdj";
       				break; 
                 case 59:
                 	zidm="fadmrl";
       				break; 
                 case 60:
                 	zidm="fadyrl";
       				break; 
                 case 61:
                 	zidm="fadqrl";
       				break; 
                 case 62:
                 	zidm="gongrmrl";
       				break; 
                 case 63:
                 	zidm="gongryrl";
       				break; 
                 case 64:
                 	zidm="gongrqrl";
       				break; 
                 case 65:
                 	zidm="rulmrl";
       				break; 
                 case 66:
                 	zidm="rulyrl";
       				break; 
                 case 67:
                 	zidm="rulqrl";
       				break;
                 
				default: break;
			}
			if(!zidm.equals("")){
				sb.append(zidm).append("=").append(rsl.getDouble("zhi")).append(",");
			}
		}
		long diancxxb_id=Long.parseLong(this.getTreeid());
		sb.deleteCharAt(sb.length()-1);
		sb.append(" where ").append("riq=to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd')").append(" and");
		sb.append(" diancxxb_id="+diancxxb_id+" and fenx='本月'");
		con.getUpdate(sb.toString());
		
		//插入累计值
		String riq=intyear + "-"+ StrMonth + "-01";
		ResultSetList rsllj = LeijSelect(diancxxb_id,riq);
		 StringBuffer sqllj = new StringBuffer("begin \n");
		
		 while (rsllj.next()) {
			
			 sqllj.append("delete from ranlcb where diancxxb_id ="+diancxxb_id+" " +
			 	 " and riq=to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd') " +
			 		" and fenx='累计'").append(";\n");
			 
			 
			 long ranlcb_id= 0;
			 ranlcb_id = Long.parseLong(MainGlobal.getNewID(((Visit)getPage().getVisit()).getDiancxxb_id()));
			 sqllj.append("insert into ranlcb(id,riq,fenx,diancxxb_id,yuebjzb_id,fadgrrlf,fadgrrlfrm,fadgrrlfry,fadgrrlfrq,fadrlf,\n"
					
					 + "fadrlfrm,fadrlfry,fadrlfrq,gongrrlf,gongrrlfrm,gongrrlfry,gongrrlfrq,gongrydftrlf,gongrydrlfrm,gongrydrlfry,gongrydrlfrq,\n"
					
					 + "fadgrtrml,fadtrml,gongrtrml,fadgrtryl,fadtryl,gongrtryl,fadgrtrql,fadtrql,gongrtrql,fadgrtrmdj,fadgrtrydj,\n"
					
					 + "fadgrtrqdj,fadtrmdj,fadtrydj,fadtrqdj,gongrtrmdj,gongrtrydj,gongrtrqdj,fadgrzbml,fadgrmzbml,fadgryzbml,\n"
					
					 + "fadgrqzbml,fadbml,fadmzbml,fadyzbml,fadqzbml,gongrbml,gongrmzbml,gongryzbml,gongrqzbml,rulzhbmj,rulmbmdj,\n"
					
					 + "rulybmdj,rulqbmdj,fadbmdj,fadmzbmdj,fadyzbmdj,fadqzbmdj,gongrbmdj,gongrmzbmdj,gongryzbmdj,gongrqzbmdj,\n"
					
					 + "fadmrl,fadyrl,fadqrl,gongrmrl,gongryrl,gongrqrl,rulmrl,rulyrl,rulqrl) values(\n"
					
			 + ranlcb_id+",to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd'),'累计',"+diancxxb_id+",0"
			 +","+rsllj.getDouble("fadgrrlf")+","+rsllj.getDouble("fadgrrlfrm")+","+rsllj.getDouble("fadgrrlfry")
			 +","+rsllj.getDouble("fadgrrlfrq")+","+rsllj.getDouble("fadrlf")+","+rsllj.getDouble("fadrlfrm")
			 +","+rsllj.getDouble("fadrlfry")+","+rsllj.getDouble("fadrlfrq")+","+rsllj.getDouble("gongrrlf")
			 +","+rsllj.getDouble("gongrrlfrm")+","+rsllj.getDouble("gongrrlfry")+","+rsllj.getDouble("gongrrlfrq")
			 +","+rsllj.getDouble("gongrydftrlf")+","+rsllj.getDouble("gongrydrlfrm")+","+rsllj.getDouble("gongrydrlfry")+","+rsllj.getDouble("gongrydrlfrq")
			 +","+rsllj.getDouble("fadgrtrml")+","+rsllj.getDouble("fadtrml")+","+rsllj.getDouble("gongrtrml")
			 +","+rsllj.getDouble("fadgrtryl")+","+rsllj.getDouble("fadtryl")+","+rsllj.getDouble("gongrtryl")
			 +","+rsllj.getDouble("fadgrtrql")+","+rsllj.getDouble("fadtrql")+","+rsllj.getDouble("gongrtrql")
			 +","+rsllj.getDouble("fadgrtrmdj")+","+rsllj.getDouble("fadgrtrydj")+","+rsllj.getDouble("fadgrtrqdj")
			 +","+rsllj.getDouble("fadtrmdj")+","+rsllj.getDouble("fadtrydj")+","+rsllj.getDouble("fadtrqdj")
			 +","+rsllj.getDouble("gongrtrmdj")+","+rsllj.getDouble("gongrtrydj")+","+rsllj.getDouble("gongrtrqdj")
			 +","+rsllj.getDouble("fadgrzbml")+","+rsllj.getDouble("fadgrmzbml")+","+rsllj.getDouble("fadgryzbml")
			 +","+rsllj.getDouble("fadgrqzbml")+","+rsllj.getDouble("fadbml")+","+rsllj.getDouble("fadmzbml")
			 +","+rsllj.getDouble("fadyzbml")+","+rsllj.getDouble("fadqzbml")+","+rsllj.getDouble("gongrbml")
			 +","+rsllj.getDouble("gongrmzbml")+","+rsllj.getDouble("gongryzbml")+","+rsllj.getDouble("gongrqzbml")
			 +","+rsllj.getDouble("rulzhbmj")+","+rsllj.getDouble("rulmbmdj")+","+rsllj.getDouble("rulybmdj")
			 +","+rsllj.getDouble("rulqbmdj")+","+rsllj.getDouble("fadbmdj")+","+rsllj.getDouble("fadmzbmdj")
			 +","+rsllj.getDouble("fadyzbmdj")+","+rsllj.getDouble("fadqzbmdj")+","+rsllj.getDouble("gongrbmdj")
			 +","+rsllj.getDouble("gongrmzbmdj")+","+rsllj.getDouble("gongryzbmdj")+","+rsllj.getDouble("gongrqzbmdj")
			 +","+rsllj.getDouble("fadmrl")+","+rsllj.getDouble("fadyrl")+","+rsllj.getDouble("fadqrl")
			 +","+rsllj.getDouble("gongrmrl")+","+rsllj.getDouble("gongryrl")+","+rsllj.getDouble("gongrqrl")
			 +","+rsllj.getDouble("rulmrl")+","+rsllj.getDouble("rulyrl")+","+rsllj.getDouble("rulqrl")
			 +");\n");
					
		 }
		 sqllj.append("end;");
		 
		 con.getInsert(sqllj.toString());
		
		
		
		//---------------------
		con.Close();
		setMsg("保存成功!");
	}
	
	public ResultSetList LeijSelect(long diancxxb_id,String riq) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sqllj="";
		if(riq.substring(5,7).equals("01")){
			 sqllj = "select * from ranlcb where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='本月'";
		}else{
			 sqllj  = "select r.diancxxb_id,sum(r.fadrlfrm+r.fadrlfry+r.fadrlfrq+r.gongrrlfrm+r.gongrrlfry+r.gongrrlfrq) as fadgrrlf, \n"
					+ "sum(r.fadrlfrm+r.gongrrlfrm) as fadgrrlfrm,sum(r.fadrlfry+r.gongrrlfry) as fadgrrlfry,  \n"
					+ "sum(r.fadrlfrq+r.gongrrlfrq) as fadgrrlfrq ,sum(r.fadrlfrm+r.fadrlfry+fadrlfrq) as fadrlf,sum(r.fadrlfrm) as fadrlfrm, \n" 
					+ "sum(r.fadrlfry) as fadrlfry,sum(r.fadrlfrq) as fadrlfrq,sum(r.gongrrlfrm+r.gongrrlfry+r.gongrrlfrq) as gongrrlf, \n"
					+ "sum(r.gongrrlfrm) as gongrrlfrm,sum(r.gongrrlfry) as gongrrlfry,sum(r.gongrrlfrq) as gongrrlfrq,  \n"
					+ "sum(r.gongrydrlfrm+r.gongrydrlfry+r.gongrydrlfrq) as gongrydftrlf,sum(r.gongrydrlfrm) as gongrydrlfrm,sum(r.gongrydrlfry) as gongrydrlfry, \n" 
					+ "sum(r.gongrydrlfrq) as gongrydrlfrq,sum(r.fadtrml+r.gongrtrml) as fadgrtrml,sum(r.fadtrml) as fadtrml,  \n"
					+ "sum(r.gongrtrml) as gongrtrml,sum(r.fadtryl+r.gongrtryl) as fadgrtryl,sum(r.fadtryl) as fadtryl,  \n"
					+ "sum(r.gongrtryl) as gongrtryl,sum(r.fadtrql+r.gongrtrql) as fadgrtrql,sum(r.fadtrql) as fadtrql,sum(r.gongrtrql) as gongrtrql,  \n"
					+ "decode(sum(r.fadtrml+r.gongrtrml),0,0,round(sum(r.fadrlfrm+r.gongrrlfrm)/sum(r.fadtrml+r.gongrtrml),2)) as fadgrtrmdj, \n"
					+ "decode(sum(r.fadtryl+r.gongrtryl),0,0,round(sum(r.fadrlfry+r.gongrrlfry)/sum(r.fadtryl+r.gongrtryl),2)) as fadgrtrydj,  \n"
					+ "decode(sum(r.fadtrql+r.gongrtrql),0,0,round(sum(r.fadrlfrq+r.gongrrlfrq)/sum(r.fadtrql+r.gongrtrql),2)) as fadgrtrqdj, \n"
					+ "decode(sum(r.fadtrml),0,0,round(sum(r.fadrlfrm+r.gongrydrlfrm)/sum(r.fadtrml),2)) as fadtrmdj, \n"
					+ "decode(sum(r.fadtryl),0,0,round(sum(r.fadrlfry+r.gongrydrlfry)/sum(r.fadtryl),2)) as fadtrydj, \n"
					+ "decode(sum(r.fadtrql),0,0,round(sum(r.fadrlfrq+r.gongrydrlfrq)/sum(r.fadtrql),2)) as fadtrqdj, \n"
					+ "decode(sum(r.gongrtrml),0,0,round(sum(r.gongrrlfrm-r.gongrydrlfrm)/sum(r.gongrtrml),2)) as gongrtrmdj, \n"
					+ "decode(sum(r.gongrtryl),0,0,round(sum(r.gongrrlfry-r.gongrydrlfry)/sum(r.gongrtryl),2)) as gongrtrydj,  \n"
					+ "decode(sum(r.gongrtrql),0,0,round(sum(r.gongrrlfrq-r.gongrydrlfrq)/sum(r.gongrtrql),2)) as gongrtrqdj, \n"
					+ "round(sum(r.fadtrml*r.fadmrl/29.271+r.fadtryl*r.fadyrl/29.271+r.fadtrql*r.fadqrl/29.271+r.gongrtrml*r.gongrmrl/29.271+r.gongrtryl*r.gongryrl/29.271+r.gongrtrql*r.gongrqrl/29.271),2) as fadgrzbml, \n"
					+ "round(sum(r.fadtrml*r.fadmrl/29.271+r.gongrtrml*r.gongrmrl/29.271),2) as fadgrmzbml,  \n"
					+ "round(sum(r.fadtrql*r.fadqrl/29.271+r.gongrtryl*r.gongryrl/29.271),2) as fadgryzbml, \n"
					+ "round(sum(r.fadtrql*r.fadqrl/29.271+r.gongrtrql*r.gongrqrl/29.271),2) as fadgrqzbml, \n"
					+ "round(sum(r.fadtrml*r.fadmrl/29.271+r.fadtryl*r.fadyrl/29.271+r.fadtrql*r.fadqrl/29.271),2) as fadbml,  \n"
					+ "round(sum(r.fadtrml*r.fadmrl/29.271),2) as fadmzbml,round(sum(r.fadtryl*r.fadyrl/29.271),2) as fadyzbml, \n"
					+ "round(sum(r.fadtrql*r.fadqrl/29.271),2) as fadqzbml, \n"
					+ "round(sum(r.gongrtrml*r.gongrmrl/29.271+r.gongrtryl*r.gongryrl/29.271+r.gongrtrql*r.gongrqrl/29.271),2) as gongrbml, \n"
					+ "round(sum(r.gongrtrml*r.gongrmrl/29.271),2) as gongrmzbml,round(sum(r.gongrtryl*r.gongryrl/29.271),2) as gongryzbml,  \n"
					+ "round(sum(r.gongrtrql*r.gongrqrl/29.271),2) as gongrqzbml, \n"
					+ "decode(sum(r.fadtrml*r.fadmrl/29.271+r.fadtryl*r.fadyrl/29.271+r.fadtrql*r.fadqrl/29.271+r.gongrtrml*r.gongrmrl/29.271+r.gongrtryl*r.gongryrl/29.271+r.gongrtrql*r.gongrqrl/29.271),0,0,round(sum(r.fadrlfrm+r.fadrlfry+r.fadrlfrq+r.gongrrlfrm+r.gongrrlfry+r.gongrrlfrq)/sum(r.fadtrml*r.fadmrl/29.271+r.fadtryl*r.fadyrl/29.271+r.fadtrql*r.fadqrl/29.271+r.gongrtrml*r.gongrmrl/29.271+r.gongrtryl*r.gongryrl/29.271+r.gongrtrql*r.gongrqrl/29.271),2)) as rulzhbmj, \n"
					+ "decode(sum(r.fadtrml*r.fadmrl/29.271+r.gongrtrml*r.gongrmrl/29.271),0,0,round(sum(r.fadrlfrm+r.gongrrlfrm)/sum(r.fadtrml*r.fadmrl/29.271+r.gongrtrml*r.gongrmrl/29.271),2)) as rulmbmdj, \n"
					+ "decode(sum(r.fadtryl*r.fadyrl/29.271+r.gongrtryl*r.gongryrl/29.271),0,0,round(sum(r.fadrlfry+r.gongrrlfry)/sum(r.fadtryl*r.fadyrl/29.271+r.gongrtryl*r.gongryrl/29.271),2)) as rulybmdj, \n"
					+ "decode(sum(r.fadtrql*r.fadqrl/29.271+r.gongrtrql*r.gongrqrl/29.271),0,0,round(sum(r.fadrlfrq+r.gongrrlfrq)/sum(r.fadtrql*r.fadqrl/29.271+r.gongrtrql*r.gongrqrl/29.271),2)) as rulqbmdj, \n"
					+ "decode(sum(r.fadtrml*r.fadmrl/29.271+r.fadtryl*r.fadyrl/29.271+r.fadtrql*r.fadqrl/29.271),0,0,round(sum(r.fadrlfrm+r.fadrlfry+r.fadrlfrq+r.gongrydrlfrm+r.gongrydrlfry+r.gongrydrlfrq)/sum(r.fadtrml*r.fadmrl/29.271+r.fadtryl*r.fadyrl/29.271+r.fadtrql*r.fadqrl/29.271),2)) as fadbmdj,  \n"
					+ "decode(sum(r.fadtrml*r.fadmrl/29.271),0,0,round(sum(r.fadrlfrm+r.gongrydrlfrm)/sum(r.fadtrml*r.fadmrl/29.271),2)) as fadmzbmdj, \n"
					+ "decode(sum(r.fadtryl*r.fadyrl/29.271),0,0,round(sum(r.fadrlfry+r.gongrydrlfry)/sum(r.fadtryl*r.fadyrl/29.271),2)) as fadyzbmdj,  \n"
					+ "decode(sum(r.fadtrql*r.fadqrl/29.271),0,0,round(sum(r.fadrlfrq+r.gongrydrlfrq)/sum(r.fadtrql*r.fadqrl/29.271),2)) as fadqzbmdj,  \n"
					+ "decode(sum(r.gongrtrml*r.gongrmrl/29.271+r.gongrtryl*r.gongryrl/29.271+r.gongrtrql*r.gongrqrl/29.271),0,0,round(sum(r.gongrrlfrm+r.gongrrlfry+r.gongrrlfrq-r.gongrydrlfrm+r.gongrydrlfry+r.gongrydrlfrq)/sum(r.gongrtrml*r.gongrmrl/29.271+r.gongrtryl*r.gongryrl/29.271+r.gongrtrql*r.gongrqrl/29.271),2)) as gongrbmdj, \n"
					+ "decode(sum(r.gongrtrml*r.gongrmrl/29.271),0,0,round(sum(r.gongrrlfrm-r.gongrydrlfrm)/sum(r.gongrtrml*r.gongrmrl/29.271),2)) as gongrmzbmdj, \n"
					+ "decode(sum(r.gongrtryl*r.gongryrl/29.271),0,0,round(sum(r.gongrrlfry-r.gongrydrlfry)/sum(r.gongrtryl*r.gongryrl/29.271),2)) as gongryzbmdj,  \n"
					+ "decode(sum(r.gongrtrql*r.gongrqrl/29.271),0,0,round(sum(r.gongrrlfrq-r.gongrydrlfrq)/sum(r.gongrtrql*r.gongrqrl/29.271),2)) as gongrqzbmdj,  \n"
					+ "decode(sum(r.fadtrml),0,0,round(sum(r.fadmrl*r.fadtrml)/sum(r.fadtrml),2)) as fadmrl,  \n" 
					+ "decode(sum(r.fadtryl),0,0,round(sum(r.fadyrl*r.fadtryl)/sum(r.fadtryl),2)) as fadyrl,  \n"
					+ "decode(sum(r.fadtrql),0,0,round(sum(r.fadqrl*r.fadtrql)/sum(r.fadtrql),2)) as fadqrl,  \n"
					+ "decode(sum(r.gongrtrml),0,0,round(sum(r.gongrmrl*r.gongrtrml)/sum(r.gongrtrml),2)) as gongrmrl,  \n"
					+ "decode(sum(r.gongrtryl),0,0,round(sum(r.gongryrl*r.gongrtryl)/sum(r.gongrtryl),2)) as gongryrl,  \n"
					+ "decode(sum(r.gongrtrql),0,0,round(sum(r.gongrqrl*r.gongrtrql)/sum(r.gongrtrql),2)) as gongrqrl,  \n"
					+ "decode(sum(r.fadtrml+r.gongrtrml),0,0,round(sum((r.fadtrml*r.fadmrl/29.271+r.gongrtrml*r.gongrmrl/29.271)*29.271)/sum(r.fadtrml+r.gongrtrml),2)) as rulmrl,  \n"
					+ "decode(sum(r.fadtryl+r.gongrtryl),0,0,round(sum((r.fadtryl*r.fadyrl/29.271+r.gongrtryl*r.gongryrl/29.271)*29.271)/sum(r.fadtryl+r.gongrtryl),2)) as rulyrl,  \n"
					+ "decode(sum(r.fadtrql+r.gongrtrql),0,0,round(sum((r.fadtrql*r.fadqrl/29.271+r.gongrtrql*r.gongrqrl/29.271)*29.271)/sum(r.fadtrql+r.gongrtrql),2)) as rulqrl  \n"
					+ "from ranlcb r where ((r.riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='本月') or   \n"
					+ "( r.riq=add_months(to_date('"+riq+"','yyyy-mm-dd'),-1) and r.fenx='累计')) and r.diancxxb_id="+visit.getDiancxxb_id()+"  \n"
					+ "group by r.diancxxb_id \n";
		}
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
		String DeleteStr="delete ranlcb r where r.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
				"and r.diancxxb_id="+diancxxb_id+"";
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
		String isHaveZhi="select * from ranlcb r where r.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
				"and r.diancxxb_id="+diancxxb_id+" and r.fenx='本月'";
		ResultSetList rssy=con.getResultSetList(isHaveZhi);
		//插入时判断是否有值
		if(rssy.next()){
			con.Close();
			return;
		}else{
			con.getInsert("insert into ranlcb(id,riq,fenx,diancxxb_id,yuebjzb_id,fadgrrlf,fadgrrlfrm,fadgrrlfry,fadgrrlfrq,fadrlf,\n"
					+ "fadrlfrm,fadrlfry,fadrlfrq,gongrrlf,gongrrlfrm,gongrrlfry,gongrrlfrq,gongrydftrlf,gongrydrlfrm,gongrydrlfry,gongrydrlfrq,\n"
					+ "fadgrtrml,fadtrml,gongrtrml,fadgrtryl,fadtryl,gongrtryl,fadgrtrql,fadtrql,gongrtrql,fadgrtrmdj,fadgrtrydj,\n"
					+ "fadgrtrqdj,fadtrmdj,fadtrydj,fadtrqdj,gongrtrmdj,gongrtrydj,gongrtrqdj,fadgrzbml,fadgrmzbml,fadgryzbml,\n"
					+ "fadgrqzbml,fadbml,fadmzbml,fadyzbml,fadqzbml,gongrbml,gongrmzbml,gongryzbml,gongrqzbml,rulzhbmj,rulmbmdj,\n"
					+ "rulybmdj,rulqbmdj,fadbmdj,fadmzbmdj,fadyzbmdj,fadqzbmdj,gongrbmdj,gongrmzbmdj,gongryzbmdj,gongrqzbmdj,\n"
					+ "fadmrl,fadyrl,fadqrl,gongrmrl,gongryrl,gongrqrl,rulmrl,rulyrl,rulqrl,beiz) values(\n"
				+id+",to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd'),'本月',"+diancxxb_id+",0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,0" +
						",0,0,0,0,0,0,0,0,0,'')");
		}
		
		con.Close();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
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


	String chaxun="select dy.lanc,dy.xuh,dy.biaoz,dy.mingc,dy.danw,dy.zhi,lj.zhi as ljzhi from vwRanlcbdy dy,vwRanlcblj lj where dy.riq=to_date('" 
				 + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and dy.diancxxb_id=lj.diancxxb_id(+) and dy.diancxxb_id="
				 +this.getTreeid()+" and dy.lanc=lj.lanc(+) and dy.riq=lj.riq(+) and dy.xuh=lj.xuh(+) and dy.biaoz=lj.biaoz(+) order by dy.xuh\n";
		  
	//System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
	rsl.beforefirst();
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("ranlcb");
   	
	egu.getColumn("lanc").setCenterHeader("栏次");
	egu.getColumn("xuh").setCenterHeader("序号");
	egu.getColumn("biaoz").setCenterHeader("标志");
	egu.getColumn("mingc").setCenterHeader("项目");
	egu.getColumn("danw").setCenterHeader("单位");
	egu.getColumn("zhi").setCenterHeader("本月数值");
	egu.getColumn("ljzhi").setCenterHeader("年累计值");
	
	egu.getColumn("lanc").setEditor(null);
	egu.getColumn("xuh").setEditor(null);
//	egu.getColumn("biaoz").setEditor(null);
	egu.getColumn("mingc").setEditor(null);
	egu.getColumn("danw").setEditor(null);
	egu.getColumn("ljzhi").setEditor(null);
	egu.getColumn("lanc").setHidden(true);
	egu.getColumn("biaoz").setHidden(true);
//	设定不可编辑列的颜色
	egu.getColumn("xuh").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("mingc").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
	egu.getColumn("danw").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	//设定单元格的底色
//	egu.getColumn("zhi").setRenderer("function(value,metadata,rec,rowIndex){if("
//				+ "rowIndex==0||rowIndex==1||rowIndex==2||rowIndex==3||rowIndex==4||rowIndex==8||rowIndex==12||rowIndex==16"
//				+ "||rowIndex==19||rowIndex==22||rowIndex==25||rowIndex==26||rowIndex==27||rowIndex==28||rowIndex==29||rowIndex==30"
//				+ "||rowIndex==31||rowIndex==32||rowIndex==33||rowIndex==34||rowIndex==35||rowIndex==36||rowIndex==37||rowIndex==38"
//				+ "||rowIndex==39||rowIndex==40||rowIndex==41||rowIndex==42||rowIndex==43||rowIndex==44||rowIndex==45||rowIndex==46"
//				+ "||rowIndex==47||rowIndex==48||rowIndex==49||rowIndex==50||rowIndex==51||rowIndex==52||rowIndex==53||rowIndex==54"
//				+ "||rowIndex==55||rowIndex==56||rowIndex==57||rowIndex==64||rowIndex==65||rowIndex==66){metadata.css='tdTextext3';} return value;}");
	egu.getColumn("ljzhi").setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
	egu.setDefaultsortable(false);//设定页面不自动排序
	
	//设定列初始宽度
	egu.getColumn("lanc").setWidth(70);
	egu.getColumn("lanc").setAlign("right");
	egu.getColumn("mingc").setWidth(200);
	egu.getColumn("danw").setWidth(100);
	egu.getColumn("zhi").setWidth(80);
	egu.getColumn("ljzhi").setWidth(80);
	
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
			
		egu.addTbarText("->");
		
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		//设定某一行不能编辑
		
		sb.append("for (m=0;m<gridDiv_ds.getTotalCount();m++){if (gridDiv_ds.getAt(m).get('BIAOZ')=='fadrlfrm'){fadrlfrmnum=m;} ")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadrlfry'){fadrlfrynum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadrlfrq'){fadrlfrqnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrrlfrm'){gongrrlfrmnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrrlfry'){gongrrlfrynum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrrlfrq'){gongrrlfrqnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrydrlfrm'){gongrydftrlfrmnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrydrlfry'){gongrydftrlfrynum=m;}")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrydrlfrq'){gongrydftrlfrqnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadtrml'){fadtrmlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrtrml'){gongrtrmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadtryl'){fadtrylnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrtryl'){gongrtrylnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadtrql'){fadtrqlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrtrql'){gongrtrqlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadmrl'){fadmrlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadyrl'){fadyrlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadqrl'){fadqrlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrmrl'){gongrmrlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongryrl'){gongryrlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrqrl'){gongrqrlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrrlf'){fadgrrlfnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrrlfrm'){fadgrrlfrmnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrrlfry'){fadgrrlfrynum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrrlfrq'){fadgrrlfrqnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadrlf'){fadrlfnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrrlf'){gongrrlfnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrydftrlf'){gongrydftrlfnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrtrml'){fadgrtrmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrtryl'){fadgrtrylnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrtrql'){fadgrtrqlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrtrmdj'){fadgrtrmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrtrydj'){fadgrtrydjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrtrqdj'){fadgrtrqdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadtrmdj'){fadtrmdjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadtrydj'){fadtrydjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadtrqdj'){fadtrqdjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrtrmdj'){gongrtrmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrtrydj'){gongrtrydjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrtrqdj'){gongrtrqdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrzbml'){fadgrzbmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrmzbml'){fadgrmzbmlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgryzbml'){fadgryzbmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadgrqzbml'){fadgrqzbmlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadbml'){fadbmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadmzbml'){fadmzbmlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadyzbml'){fadyzbmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadqzbml'){fadqzbmlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrbml'){gongrbmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrmzbml'){gongrmzbmlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongryzbml'){gongryzbmlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrqzbml'){gongrqzbmlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='rulzhbmj'){rulzhbmjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='rulmbmdj'){rulmbmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='rulybmdj'){rulybmdjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='rulqbmdj'){rulqbmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadbmdj'){fadbmdjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadmzbmdj'){fadmzbmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadyzbmdj'){fadyzbmdjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='fadqzbmdj'){fadqzbmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrbmdj'){gongrbmdjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrmzbmdj'){gongrmzbmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongryzbmdj'){gongryzbmdjnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='gongrqzbmdj'){gongrqzbmdjnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='rulmrl'){rulmrlnum=m;}").append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='rulyrl'){rulyrlnum=m;}\n")
		.append("if(gridDiv_ds.getAt(m).get('BIAOZ')=='rulqrl'){rulqrlnum=m;}}");
		
		sb.append("if(e.record.get('XUH')==fadgrrlfnum+1||e.record.get('XUH')==fadgrrlfrmnum+1||e.record.get('XUH')==fadgrrlfrynum+1 ||e.record.get('XUH')==fadgrrlfrqnum +1||e.record.get('XUH')==fadrlfnum+1"
				+ "||e.record.get('XUH')==gongrrlfnum+1 ||e.record.get('XUH')==gongrydftrlfnum+1||e.record.get('XUH')==fadgrtrmlnum+1||e.record.get('XUH')==fadgrtrylnum+1||e.record.get('XUH')==fadgrtrqlnum+1"
				+ "||e.record.get('XUH')==fadgrtrmdjnum+1||e.record.get('XUH')==fadgrtrydjnum+1||e.record.get('XUH')==fadgrtrqdjnum+1||e.record.get('XUH')==fadtrmdjnum+1"
				+ "||e.record.get('XUH')==fadtrydjnum+1||e.record.get('XUH')==fadtrqdjnum+1||e.record.get('XUH')==gongrtrmdjnum+1||e.record.get('XUH')==gongrtrydjnum+1"
				+ "||e.record.get('XUH')==gongrtrqdjnum+1||e.record.get('XUH')==fadgrzbmlnum+1||e.record.get('XUH')==fadgrmzbmlnum+1||e.record.get('XUH')==fadgryzbmlnum+1"
				+ "||e.record.get('XUH')==fadgrqzbmlnum+1||e.record.get('XUH')==fadbmlnum+1||e.record.get('XUH')==fadmzbmlnum+1||e.record.get('XUH')==fadyzbmlnum+1"
				+ "||e.record.get('XUH')==fadqzbmlnum+1||e.record.get('XUH')==gongrbmlnum+1||e.record.get('XUH')==gongrmzbmlnum+1||e.record.get('XUH')==gongryzbmlnum+1"
				+ "||e.record.get('XUH')==gongrqzbmlnum+1||e.record.get('XUH')==rulzhbmjnum+1||e.record.get('XUH')==rulmbmdjnum+1||e.record.get('XUH')==rulybmdjnum+1"
				+ "||e.record.get('XUH')==rulqbmdjnum+1||e.record.get('XUH')==fadbmdjnum+1||e.record.get('XUH')==fadmzbmdjnum+1||e.record.get('XUH')==fadyzbmdjnum+1"
				+ "||e.record.get('XUH')==fadqzbmdjnum+1||e.record.get('XUH')==gongrbmdjnum+1||e.record.get('XUH')==gongrmzbmdjnum+1||e.record.get('XUH')==gongryzbmdjnum+1"
				+ "||e.record.get('XUH')==gongrqzbmdjnum+1||e.record.get('XUH')==rulmrlnum+1||e.record.get('XUH')==rulyrlnum+1||e.record.get('XUH')==rulqrlnum+1){e.cancel=true;}");//
		
		sb.append("});");
		//if( gridDiv_ds.getAt(m).get('biaoz')=='fadrlfrm'){alert(e.row);}  
		sb.append("gridDiv_grid.on('afteredit',function(e){");
		
		//计算耗用天然煤
		sb.append("if(e.row==fadrlfrmnum||e.row==fadrlfrynum||e.row==fadrlfrqnum||e.row==gongrrlfrmnum||e.row==gongrrlfrynum||e.row==gongrrlfrqnum||e.row==gongrydftrlfrmnum||e.row==gongrydftrlfrynum||e.row==gongrydftrlfrqnum||e.row==fadtrmlnum||e.row==gongrtrmlnum")
		.append("||e.row==fadtrylnum||e.row==gongrtrylnum||e.row==fadtrqlnum||e.row==gongrtrqlnum||e.row==fadmrlnum||e.row==fadyrlnum||e.row==fadqrlnum||e.row==gongrmrlnum||e.row==gongryrlnum||e.row==gongrqrlnum){")
		.append("fadgdrlf = gridDiv_ds.getAt(fadgrrlfnum);").append("fadgrrlfrm = gridDiv_ds.getAt(fadgrrlfrmnum);").append("fadgrrlfry = gridDiv_ds.getAt(fadgrrlfrynum);")
		.append("fadgrrlfrq = gridDiv_ds.getAt(fadgrrlfrqnum);").append("fadrlf = gridDiv_ds.getAt(fadrlfnum);").append("fadrlfrm=gridDiv_ds.getAt(fadrlfrmnum);")
		.append("fadrlfry=gridDiv_ds.getAt(fadrlfrynum);").append("fadrlfrq=gridDiv_ds.getAt(fadrlfrqnum);").append("gongrrlf=gridDiv_ds.getAt(gongrrlfnum);")
		.append("gongrrlfrm=gridDiv_ds.getAt(gongrrlfrmnum);").append("gongrrlfry=gridDiv_ds.getAt(gongrrlfrynum);").append("gongrrlfrq=gridDiv_ds.getAt(gongrrlfrqnum);")
		.append("gongrydftrlf=gridDiv_ds.getAt(gongrydftrlfnum);").append("gongrydftrlfrm=gridDiv_ds.getAt(gongrydftrlfrmnum);").append("gongrydftrlfry=gridDiv_ds.getAt(gongrydftrlfrynum);").append("gongrydftrlfrq=gridDiv_ds.getAt(gongrydftrlfrqnum);")
		.append("fadgrtrml=gridDiv_ds.getAt(fadgrtrmlnum);").append("fadtrml=gridDiv_ds.getAt(fadtrmlnum);").append("gongrtrml=gridDiv_ds.getAt(gongrtrmlnum);")
		.append("fadgrtryl=gridDiv_ds.getAt(fadgrtrylnum);").append("fadtryl=gridDiv_ds.getAt(fadtrylnum);").append("gongrtryl=gridDiv_ds.getAt(gongrtrylnum);")
		.append("fadgrtrql=gridDiv_ds.getAt(fadgrtrqlnum);").append("fadtrql=gridDiv_ds.getAt(fadtrqlnum);").append("gongrtrql=gridDiv_ds.getAt(gongrtrqlnum);")
		.append("fadgrtrmdj=gridDiv_ds.getAt(fadgrtrmdjnum);").append("fadgrtrydj=gridDiv_ds.getAt(fadgrtrydjnum);").append("fadgrtrqdj=gridDiv_ds.getAt(fadgrtrqdjnum);")
		.append("fadtrmdj=gridDiv_ds.getAt(fadtrmdjnum);").append("fadtrydj=gridDiv_ds.getAt(fadtrydjnum);").append("fadtrqdj=gridDiv_ds.getAt(fadtrqdjnum);")
		.append("gongrtrmdj=gridDiv_ds.getAt(gongrtrmdjnum);").append("gongrtrydj=gridDiv_ds.getAt(gongrtrydjnum);").append("gongrtrqdj=gridDiv_ds.getAt(gongrtrqdjnum);")
		.append("fadgrzbml=gridDiv_ds.getAt(fadgrzbmlnum);").append("fadgrmzbml=gridDiv_ds.getAt(fadgrmzbmlnum);").append("fadgryzbml=gridDiv_ds.getAt(fadgryzbmlnum);")
		.append("fadgrqzbml=gridDiv_ds.getAt(fadgrqzbmlnum);").append("fadbml=gridDiv_ds.getAt(fadbmlnum);").append("fadmzbml=gridDiv_ds.getAt(fadmzbmlnum);")
		.append("fadyzbml=gridDiv_ds.getAt(fadyzbmlnum);").append("fadqzbml=gridDiv_ds.getAt(fadqzbmlnum);").append("gongrbml=gridDiv_ds.getAt(gongrbmlnum);")
		.append("gongrmzbml=gridDiv_ds.getAt(gongrmzbmlnum);").append("gongryzbml=gridDiv_ds.getAt(gongryzbmlnum);").append("gongrqzbml=gridDiv_ds.getAt(gongrqzbmlnum);")
		.append("rulzhbmj=gridDiv_ds.getAt(rulzhbmjnum);").append("rulmbmdj=gridDiv_ds.getAt(rulmbmdjnum);").append("rulybmdj=gridDiv_ds.getAt(rulybmdjnum);")
		.append("rulqbmdj=gridDiv_ds.getAt(rulqbmdjnum);").append("fadbmdj=gridDiv_ds.getAt(fadbmdjnum);").append("fadmzbmdj=gridDiv_ds.getAt(fadmzbmdjnum);")
		.append("fadyzbmdj=gridDiv_ds.getAt(fadyzbmdjnum);").append("fadqzbmdj=gridDiv_ds.getAt(fadqzbmdjnum);").append("gongrbmdj=gridDiv_ds.getAt(gongrbmdjnum);")
		.append("gongrmzbmdj=gridDiv_ds.getAt(gongrmzbmdjnum);").append("gongryzbmdj=gridDiv_ds.getAt(gongryzbmdjnum);").append("gongrqzbmdj=gridDiv_ds.getAt(gongrqzbmdjnum);")
		.append("fadmrl=gridDiv_ds.getAt(fadmrlnum);").append("fadyrl=gridDiv_ds.getAt(fadyrlnum);").append("fadqrl=gridDiv_ds.getAt(fadqrlnum);")
		.append("gongrmrl=gridDiv_ds.getAt(gongrmrlnum);").append("gongryrl=gridDiv_ds.getAt(gongryrlnum);").append("gongrqrl=gridDiv_ds.getAt(gongrqrlnum);")
		.append("rulmrl=gridDiv_ds.getAt(rulmrlnum);").append("rulyrl=gridDiv_ds.getAt(rulyrlnum);").append("rulqrl=gridDiv_ds.getAt(rulqrlnum);")
		
		.append("fadrlfrm.set('ZHI',Round(eval(fadrlfrm.get('ZHI')||0),2));").append("fadrlfry.set('ZHI',Round(eval(fadrlfry.get('ZHI')||0),2));").append("fadrlfrq.set('ZHI',Round(eval(fadrlfrq.get('ZHI')||0),2));")
		.append("fadrlf.set('ZHI',Round(eval(fadrlfrm.get('ZHI')||0)+eval(fadrlfry.get('ZHI')||0)+eval(fadrlfrq.get('ZHI')||0),2));")
		.append("gongrrlfrm.set('ZHI',Round(eval(gongrrlfrm.get('ZHI')||0),2));").append("gongrrlfry.set('ZHI',Round(eval(gongrrlfry.get('ZHI')||0),2));").append("gongrrlfrq.set('ZHI',Round(eval(gongrrlfrq.get('ZHI')||0),2));")
		.append("gongrydftrlfrm.set('ZHI',Round(eval(gongrydftrlfrm.get('ZHI')||0),2));").append("gongrydftrlfry.set('ZHI',Round(eval(gongrydftrlfry.get('ZHI')||0),2));").append("gongrydftrlfrq.set('ZHI',Round(eval(gongrydftrlfrq.get('ZHI')||0),2));")
		.append("fadtrml.set('ZHI',Round(eval(fadtrml.get('ZHI')||0),2));").append("gongrtrml.set('ZHI',Round(eval(gongrtrml.get('ZHI')||0),2));")
		.append("fadtryl.set('ZHI',Round(eval(fadtryl.get('ZHI')||0),2));").append("gongrtryl.set('ZHI',Round(eval(gongrtryl.get('ZHI')||0),2));")
		.append("fadtrql.set('ZHI',Round(eval(fadtrql.get('ZHI')||0),2));").append("gongrtrql.set('ZHI',Round(eval(gongrtrql.get('ZHI')||0),2));")
		.append("fadmrl.set('ZHI',Round(eval(fadmrl.get('ZHI')||0),2));").append("fadyrl.set('ZHI',Round(eval(fadyrl.get('ZHI')||0),2));")
		.append("fadqrl.set('ZHI',Round(eval(fadqrl.get('ZHI')||0),2));").append("gongrmrl.set('ZHI',Round(eval(gongrmrl.get('ZHI')||0),2));")
		.append("gongryrl.set('ZHI',Round(eval(gongryrl.get('ZHI')||0),2));").append("gongrqrl.set('ZHI',Round(eval(gongrqrl.get('ZHI')||0),2));")
		.append("gongrrlf.set('ZHI',Round(eval(gongrrlfrm.get('ZHI')||0)+eval(gongrrlfry.get('ZHI')||0)+eval(gongrrlfrq.get('ZHI')||0),2));")
		.append("fadgrrlfrm.set('ZHI',Round(eval(fadrlfrm.get('ZHI')||0)+eval(gongrrlfrm.get('ZHI')||0),2));").append("fadgrrlfry.set('ZHI',Round(eval(fadrlfry.get('ZHI')||0)+eval(gongrrlfry.get('ZHI')||0),2));").append("fadgrrlfrq.set('ZHI',Round(eval(fadrlfrq.get('ZHI')||0)+eval(gongrrlfrq.get('ZHI')||0),2));")
		.append("fadgdrlf.set('ZHI',Round(eval(fadgrrlfrm.get('ZHI')||0)+eval(fadgrrlfry.get('ZHI')||0)+eval(fadgrrlfrq.get('ZHI')||0),2));")
		.append("gongrydftrlf.set('ZHI',Round(eval(gongrydftrlfrm.get('ZHI')||0)+eval(gongrydftrlfry.get('ZHI')||0)+eval(gongrydftrlfrq.get('ZHI')||0),2));")
		.append("fadgrtrml.set('ZHI',Round(eval(fadtrml.get('ZHI')||0)+eval(gongrtrml.get('ZHI')||0),2));")
		.append("fadgrtryl.set('ZHI',Round(eval(fadtryl.get('ZHI')||0)+eval(gongrtryl.get('ZHI')||0),2));")
		.append("fadgrtrql.set('ZHI',Round(eval(fadtrql.get('ZHI')||0)+eval(gongrtrql.get('ZHI')||0),2));")
		.append("if(eval(fadgrtrml.get('ZHI')||0)==0){fadgrtrmdj.set('ZHI',0);}else{fadgrtrmdj.set('ZHI',Round(eval(fadgrrlfrm.get('ZHI')||0)/eval(fadgrtrml.get('ZHI')||0),2));}")
		.append("if(eval(fadgrtryl.get('ZHI')||0)==0){fadgrtrydj.set('ZHI',0);}else{fadgrtrydj.set('ZHI',Round(eval(fadgrrlfry.get('ZHI')||0)/eval(fadgrtryl.get('ZHI')||0),2));}")
		.append("if(eval(fadgrtrql.get('ZHI')||0)==0){fadgrtrqdj.set('ZHI',0);}else{fadgrtrqdj.set('ZHI',Round(eval(fadgrrlfrq.get('ZHI')||0)/eval(fadgrtrql.get('ZHI')||0),2));}")
		.append("if(eval(fadtrml.get('ZHI')||0)==0){fadtrmdj.set('ZHI',0);}else{fadtrmdj.set('ZHI',Round((eval(fadrlfrm.get('ZHI')||0)+eval(gongrydftrlfrm.get('ZHI')||0))/eval(fadtrml.get('ZHI')||0),2));}")
		.append("if(eval(fadtryl.get('ZHI')||0)==0){fadtrydj.set('ZHI',0);}else{fadtrydj.set('ZHI',Round((eval(fadrlfry.get('ZHI')||0)+eval(gongrydftrlfry.get('ZHI')||0))/eval(fadtryl.get('ZHI')||0),2));}")
		.append("if(eval(fadtrql.get('ZHI')||0)==0){fadtrqdj.set('ZHI',0);}else{fadtrqdj.set('ZHI',Round((eval(fadrlfrq.get('ZHI')||0)+eval(gongrydftrlfrq.get('ZHI')||0))/eval(fadtrql.get('ZHI')||0),2));}")
		.append("if(eval(gongrtrml.get('ZHI')||0)==0){gongrtrmdj.set('ZHI',0);}else{gongrtrmdj.set('ZHI',Round((eval(gongrrlfrm.get('ZHI')||0)-eval(gongrydftrlfrm.get('ZHI')||0))/eval(gongrtrml.get('ZHI')||0),2));}")
		.append("if(eval(gongrtryl.get('ZHI')||0)==0){gongrtrydj.set('ZHI',0);}else{gongrtrydj.set('ZHI',Round((eval(gongrrlfry.get('ZHI')||0)-eval(gongrydftrlfry.get('ZHI')||0))/eval(gongrtryl.get('ZHI')||0),2));}")
		.append("if(eval(gongrtrql.get('ZHI')||0)==0){gongrtrqdj.set('ZHI',0);}else{gongrtrqdj.set('ZHI',Round((eval(gongrrlfrq.get('ZHI')||0)-eval(gongrydftrlfrq.get('ZHI')||0))/eval(gongrtrql.get('ZHI')||0),2));}")
		.append("fadmzbml.set('ZHI',Round(eval(fadtrml.get('ZHI')||0)*eval(fadmrl.get('ZHI')||0)/29.271,2));")
		.append("fadyzbml.set('ZHI',Round(eval(fadtryl.get('ZHI')||0)*eval(fadyrl.get('ZHI')||0)/29.271,2));")
		.append("fadqzbml.set('ZHI',Round(eval(fadtrql.get('ZHI')||0)*eval(fadqrl.get('ZHI')||0)/29.271,2));")
		.append("fadbml.set('ZHI',Round(eval(fadmzbml.get('ZHI')||0)+eval(fadyzbml.get('ZHI')||0)+eval(fadqzbml.get('ZHI')||0),2));")
		.append("gongrmzbml.set('ZHI',Round(eval(gongrtrml.get('ZHI')||0)*eval(gongrmrl.get('ZHI')||0)/29.271,2));")
		.append("gongryzbml.set('ZHI',Round(eval(gongrtryl.get('ZHI')||0)*eval(gongryrl.get('ZHI')||0)/29.271,2));")
		.append("gongrqzbml.set('ZHI',Round(eval(gongrtrql.get('ZHI')||0)*eval(gongrqrl.get('ZHI')||0)/29.271,2));")
		.append("gongrbml.set('ZHI',Round(eval(gongrmzbml.get('ZHI')||0)+eval(gongryzbml.get('ZHI')||0)+eval(gongrqzbml.get('ZHI')||0),2));")
		.append("fadgrzbml.set('ZHI',Round(eval(fadbml.get('ZHI')||0)+eval(gongrbml.get('ZHI')||0),2));")
		.append("fadgrmzbml.set('ZHI',Round(eval(fadmzbml.get('ZHI')||0)+eval(gongrmzbml.get('ZHI')||0),2));")
		.append("fadgryzbml.set('ZHI',Round(eval(fadyzbml.get('ZHI')||0)+eval(gongryzbml.get('ZHI')||0),2));")
		.append("fadgrqzbml.set('ZHI',Round(eval(fadqzbml.get('ZHI')||0)+eval(gongrqzbml.get('ZHI')||0),2));")
		.append("if(eval(fadgrzbml.get('ZHI')||0)==0){rulzhbmj.set('ZHI',0);}else{rulzhbmj.set('ZHI',Round(eval(fadgdrlf.get('ZHI')||0)/eval(fadgrzbml.get('ZHI')||0),2));}")
		.append("if(eval(fadgrmzbml.get('ZHI')||0)==0){rulmbmdj.set('ZHI',0);}else{rulmbmdj.set('ZHI',Round(eval(fadgrrlfrm.get('ZHI')||0)/eval(fadgrmzbml.get('ZHI')||0),2));}")
		.append("if(eval(fadgryzbml.get('ZHI')||0)==0){rulybmdj.set('ZHI',0);}else{rulybmdj.set('ZHI',Round(eval(fadgrrlfry.get('ZHI')||0)/eval(fadgryzbml.get('ZHI')||0),2));}")
		.append("if(eval(fadgrqzbml.get('ZHI')||0)==0){rulqbmdj.set('ZHI',0);}else{rulqbmdj.set('ZHI',Round(eval(fadgrrlfrq.get('ZHI')||0)/eval(fadgrqzbml.get('ZHI')||0),2));}")
		.append("if(eval(fadbml.get('ZHI')||0)==0){fadbmdj.set('ZHI',0);}else{fadbmdj.set('ZHI',Round((eval(fadrlf.get('ZHI')||0)+eval(gongrydftrlf.get('ZHI')||0))/eval(fadbml.get('ZHI')||0),2));}")
		.append("if(eval(fadmzbml.get('ZHI')||0)==0){fadmzbmdj.set('ZHI',0);}else{fadmzbmdj.set('ZHI',Round((eval(fadrlfrm.get('ZHI')||0)+eval(gongrydftrlfrm.get('ZHI')||0))/eval(fadmzbml.get('ZHI')||0),2));}")
		.append("if(eval(fadyzbml.get('ZHI')||0)==0){fadyzbmdj.set('ZHI',0);}else{fadyzbmdj.set('ZHI',Round((eval(fadrlfry.get('ZHI')||0)+eval(gongrydftrlfry.get('ZHI')||0))/eval(fadyzbml.get('ZHI')||0),2));}")
		.append("if(eval(fadqzbml.get('ZHI')||0)==0){fadqzbmdj.set('ZHI',0);}else{fadqzbmdj.set('ZHI',Round((eval(fadrlfrq.get('ZHI')||0)+eval(gongrydftrlfrq.get('ZHI')||0))/eval(fadqzbml.get('ZHI')||0),2));}")
		.append("if(eval(gongrbml.get('ZHI')||0)==0){gongrbmdj.set('ZHI',0);}else{gongrbmdj.set('ZHI',Round((eval(gongrrlf.get('ZHI')||0)-eval(gongrydftrlf.get('ZHI')||0))/eval(gongrbml.get('ZHI')||0),2));}")
		.append("if(eval(gongrmzbml.get('ZHI')||0)==0){gongrmzbmdj.set('ZHI',0);}else{gongrmzbmdj.set('ZHI',Round((eval(gongrrlfrm.get('ZHI')||0)-eval(gongrydftrlfrm.get('ZHI')||0))/eval(gongrmzbml.get('ZHI')||0),2));}")
		.append("if(eval(gongryzbml.get('ZHI')||0)==0){gongryzbmdj.set('ZHI',0);}else{gongryzbmdj.set('ZHI',Round((eval(gongrrlfry.get('ZHI')||0)-eval(gongrydftrlfry.get('ZHI')||0))/eval(gongryzbml.get('ZHI')||0),2));}")
		.append("if(eval(gongrqzbml.get('ZHI')||0)==0){gongrqzbmdj.set('ZHI',0);}else{gongrqzbmdj.set('ZHI',Round((eval(gongrrlfrq.get('ZHI')||0)-eval(gongrydftrlfrq.get('ZHI')||0))/eval(gongrqzbml.get('ZHI')||0),2));}")
		.append("if(eval(fadgrtrml.get('ZHI')||0)==0){rulmrl.set('ZHI',0);}else{rulmrl.set('ZHI',Round(eval(fadgrmzbml.get('ZHI')||0)*29.271/eval(fadgrtrml.get('ZHI')||0),2));}")
		.append("if(eval(fadgrtryl.get('ZHI')||0)==0){rulyrl.set('ZHI',0);}else{rulyrl.set('ZHI',Round(eval(fadgryzbml.get('ZHI')||0)*29.271/eval(fadgrtryl.get('ZHI')||0),2));}")
		.append("if(eval(fadgrtrql.get('ZHI')||0)==0){rulqrl.set('ZHI',0);}else{rulqrl.set('ZHI',Round(eval(fadgrqzbml.get('ZHI')||0)*29.271/eval(fadgrtrql.get('ZHI')||0),2));}")
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