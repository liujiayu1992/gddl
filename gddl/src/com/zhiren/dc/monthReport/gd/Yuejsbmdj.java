package com.zhiren.dc.monthReport.gd;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuejsbmdj extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=this.getExtGrid();
		StringBuffer sql = new StringBuffer("begin \n");
		String strchange=this.getChange();
		String tableName="gd_yuejsbmdj";
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while (mdrsl.next()) {		
			sql.append("update ").append(tableName).append(" set ")
				.append("fenx='").append(mdrsl.getString("FENX"))
				.append("',jiesl=").append(mdrsl.getString("JIESL"))
				.append(",meij=").append(mdrsl.getString("MEIJ"))
				.append(",meijs=").append(mdrsl.getString("MEIJS"))
				.append(",yunj=").append(mdrsl.getString("YUNJ"))
				.append(",yunjs=").append(mdrsl.getString("YUNJS"))
				.append(",daozzf=").append(mdrsl.getString("DAOZZF"))
				.append(",tielzf=").append(mdrsl.getString("TIELZF"))
				.append(",gonglzf=").append(mdrsl.getString("GONGLZF"))
				.append(",guanlfwf=").append(mdrsl.getString("GUANLFWF"))
				.append(",changnzf=").append(mdrsl.getString("CHANGNZF"))
				.append(",qit=").append(mdrsl.getString("QIT"))
				.append(",qnet_ar=").append(mdrsl.getString("QNET_AR"))
				.append(",biaomdj=").append(mdrsl.getString("BIAOMDJ"))
				.append(",buhsbmdj=").append(mdrsl.getString("BUHSBMDJ"))
				.append(",kuangqyf=").append(mdrsl.getString("KUANGQYF"))
			    .append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
		}
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			Countlj(); //计算累计数
		}
		con.Close();
		sql=null;
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
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			Countlj();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
			getSelectData();
		}
	}
	
	public void DelData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "delete from gd_yuejsbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ diancxxb_id
				+ ")";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}
	
	public void CreateData() {

		Visit visit = (Visit) getPage().getVisit();
		String strDate="";
		String strNianf="";
		String strYuef="";
		int intYuef=Integer.parseInt(getYuefValue().getValue());
		strNianf=getNianfValue().getValue();
		strYuef=getYuefValue().getValue();
		if (intYuef<10){
			strYuef="0"+strYuef;
		}
		strDate=strNianf+"-"+strYuef+"-01";
		String diancxxb_id=this.getTreeid();
		String strSql="";
		StringBuffer sb=new StringBuffer("begin \n");
		int xuh=1;
		double daoczhj=0; //到厂综合价
		double biaomdj=0; //标煤单价
		double buhsbmdj=0;	//不含税标煤单位
		
		if(Cleaning_up_History(strDate,"")){
//			已经清空数据
//			只计算本月数据
				JDBCcon con = new JDBCcon();
				strSql ="select danw.*,j.jiessl as jiessl,j.gongfsl,meij,meijs,kuangqyf,tielyf,tielyfs,tielzf,farl, daozzf from\n" +
					"     (select\n" + 
					"           g.fuid as gongysb_id,\n" + 
					"           j.jihkjb_id,\n" + 
					"           getTableId('pinzb','mingc',j.meiz) as pinzb_id,\n" + 
					"           j.yunsfsb_id,\n" + 
					"           sum(round_new(j.jiessl,0)) as jiessl,\n" + 
					"           sum(round_new(getjiesdzb('jiesb',j.id,'"+Locale.jiessl_zhibb+"' ,'gongf'),0)) as gongfsl,\n" + 
					"           decode(sum(j.jiessl),0,0,round_new(sum(j.jiessl*round_new(j.hansmk/j.jiessl,2))/sum(j.jiessl),2)) as meij,\n" + 
					"           decode(sum(j.jiessl),0,0,round_new(sum(j.jiessl*round_new(j.shuik/j.jiessl,2))/sum(j.jiessl),2)) as meijs,\n" + 
					"           decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum(nvl(jy.kuangqzf,0)+nvl(jy.kuangqyf,0))/sum(nvl(jy.jiessl,0)),2)) as kuangqyf,\n" + 
					"           decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum(nvl(jy.guotyf,0))/sum(nvl(jy.jiessl,0)),2)) as tielyf,\n" + 
					"           decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum((nvl(jy.guotyf,0)+nvl(jy.kuangqyf,0))*nvl(jy.shuil,0))/sum(nvl(jy.jiessl,0)),2)) as tielyfs,\n" + 
					"           0 as daozzf,\n" + 
					"           decode(sum(nvl(jy.jiessl,0)),0,0,round_new(sum(nvl(jy.guotzf,0))/ sum(nvl(jy.jiessl,0)),2)) as tielzf,\n" + 
					"           decode(sum(j.jiessl),0,0,round_new(sum(j.jiessl*j.jiesrl)/sum(j.jiessl),2)) as farl\n" + 
					"           from jiesb j,jiesyfb jy,\n" + 
					"                (select id,case when fuid<0 then id else fuid end as fuid\n" + 
					"                from gongysb) g\n" + 
					"           where j.gongysb_id=g.id\n" + 
					"                 and j.id=jy.diancjsmkb_id(+)\n" + 
					"                 and j.liucztb_id=1" +
					"                 and to_char(j.jiesrq,'yyyy')='"+strNianf+"'\n" + 
					"                 and to_char(j.jiesrq, 'mm')='"+strYuef+"'\n" +
					"                 --and to_char(j.ruzrq, 'yyyy')='"+strNianf+"'\n" + 
					"                 --and to_char(j.ruzrq,'mm')='"+strYuef+"'\n" + 
					"                 and j.diancxxb_id="+diancxxb_id+			
					"           group by g.fuid,j.jihkjb_id,j.meiz,j.yunsfsb_id) j,\n" + 
					"           (select distinct gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n" + 
					"                   from (\n" + 
					"                   (select g.fuid as gongysb_id,\n" + 
					"                           j.jihkjb_id,\n" + 
					"                           getTableId('pinzb','mingc',j.meiz) as pinzb_id,\n" + 
					"                           j.yunsfsb_id\n" + 
					"                           from jiesb j,\n" + 
					"                           (select id,case when fuid<=0 then id else fuid end as fuid\n" + 
					"                                   from gongysb) g\n" + 
					"                           where j.gongysb_id=g.id\n" + 
					"                 				and j.diancxxb_id="+diancxxb_id+
					"                               and j.liucztb_id=1\n" +
					"                               and to_char(j.jiesrq,'yyyy')='"+strNianf+"'\n" + 
					"                               and to_char(j.jiesrq, 'mm')='"+strYuef+"'\n" +
					"                           	--and to_char(j.ruzrq, 'yyyy')= '"+strNianf+"'\n" + 
					"                           	--and to_char(j.ruzrq, 'mm')='"+strYuef+"'\n" +
					"                    )\n";
				
				if(intYuef>1){
					
					strSql=strSql+"           union\n" + 
						"           (select kj.gongysb_id,kj.jihkjb_id,\n" + 
						"                   kj.pinzb_id,kj.yunsfsb_id\n" + 
						"                   from gd_yuejsbmdj dj,yuetjkjb kj\n" + 
						"                   where dj.yuetjkjb_id=kj.id\n" + 
						"                         and kj.diancxxb_id="+diancxxb_id+
						"                         and riq=add_months(to_date('"+strDate+"','yyyy-MM-dd'),-1)\n" + 
						"                         and dj.fenx='"+SysConstant.Fenx_Leij+"')\n"; 
				}
				
				strSql=strSql+
						"                         )) danw\n" + 
						"           where danw.gongysb_id=j.gongysb_id(+)\n" + 
						"                 and danw.jihkjb_id=j.jihkjb_id(+)\n" + 
						"                 and danw.pinzb_id=j.pinzb_id(+)\n" + 
						"                 and danw.yunsfsb_id=j.yunsfsb_id(+)\n"+
						"			order by danw.gongysb_id";
		
		
				ResultSetList rs=con.getResultSetList(strSql);
				
				while(rs.next()){
					
					daoczhj=rs.getDouble("meij")+rs.getDouble("kuangqyf")+rs.getDouble("tielyf")+rs.getDouble("daozzf")+rs.getDouble("tielzf");
					biaomdj=CustomMaths.Round_new(daoczhj*29.271/rs.getDouble("farl"),2);
					buhsbmdj=CustomMaths.Round_new((daoczhj-rs.getDouble("meijs")-rs.getDouble("tielyfs"))*29.271/rs.getDouble("farl"),2);
					
					sb.append(InsertYuejsbmdj(diancxxb_id,strDate,xuh,rs.getString("gongysb_id"),rs.getString("jihkjb_id"),
							rs.getString("pinzb_id"),rs.getString("yunsfsb_id"),SysConstant.Fenx_Beny,rs.getDouble("jiessl"),
							rs.getDouble("meij"),rs.getDouble("meijs"),rs.getDouble("kuangqyf"),rs.getDouble("tielyf"),
							rs.getDouble("tielyfs"),rs.getDouble("daozzf"),rs.getDouble("tielzf"),0,0,0,0,rs.getDouble("farl"),biaomdj,buhsbmdj));
					
					xuh++;
				}
				rs.close();
				sb.append("end;");
				if(sb.length()>13){
					
					if(con.getUpdate(sb.toString())>=0){
						this.setMsg("数据生成成功!");
					}else{
						
						this.setMsg("数据生成失败!");
					}
				}
				con.Close();
				sb=null;
		}
	}
	
	private void Countlj(){
		
//		保存时计算累计
		String diancxxb_id=this.getTreeid();
		int intYuef=Integer.parseInt(getYuefValue().getValue());
		String strDate="";
		strDate=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String sql="";
		String str_Id="0";
		int xuh=1;
		
		if(Cleaning_up_History(strDate,"  and js.fenx='累计'\n")){
			JDBCcon con=new JDBCcon();
			StringBuffer sb=new StringBuffer("begin \n");
			
			if(intYuef==1){
//				一月份的报表
				sql="select *\n" +
					"       from gd_yuejsbmdj dj,yuetjkjb kj\n" + 
					"       where dj.yuetjkjb_id=kj.id\n" + 
					"             and kj.diancxxb_id="+diancxxb_id+"\n" + 
					"             and kj.riq=to_date('"+strDate+"','yyyy-MM-dd')\n"+
					" 		order by gongysb_id";
			}else{
				
				sql=
					"select gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id,\n" +
					"       sum(nvl(dj.jiesl,0)) as jiesl,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.meij,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as meij,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.meijs,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as meijs,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.kuangqyf,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as kuangqyf,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.yunj,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as yunj,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.yunjs,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as yunjs,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.daozzf,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as daozzf,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.tielzf,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as tielzf,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.gonglzf,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as gonglzf,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.guanlfwf,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as guanlfwf,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.changnzf,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as changnzf,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.qit,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as qit,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.qnet_ar,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as qnet_ar,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.biaomdj,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as biaomdj,\n" + 
					"       round_new(sum(decode(nvl(dj.jiesl,0),0,0,nvl(dj.jiesl,0)*nvl(dj.buhsbmdj,0)))\n" + 
					"           /sum(decode(nvl(dj.jiesl,0),0,1,nvl(dj.jiesl,0))),2) as buhsbmdj\n" + 
					"\n" + 
					"       from gd_yuejsbmdj dj,yuetjkjb kj\n" + 
					"       where dj.yuetjkjb_id=kj.id\n" + 
					"             and kj.diancxxb_id="+diancxxb_id+"\n" + 
					"             and(\n" + 
					"             (kj.riq=to_date('"+strDate+"','yyyy-MM-dd')\n" + 
					"             and dj.fenx='"+SysConstant.Fenx_Beny+"') or\n" + 
					"             (kj.riq=add_months(to_date('"+strDate+"','yyyy-MM-dd'),-1)\n" + 
					"             and dj.fenx='"+SysConstant.Fenx_Leij+"'))\n" + 
					"\n" + 
					"       group by gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n"+
					"		order by gongysb_id\n";

			}
			
			ResultSetList rs=con.getResultSetList(sql);
			while(rs.next()){
				
				sb.append(InsertYuejsbmdj(diancxxb_id,strDate,xuh,rs.getString("gongysb_id"),rs.getString("jihkjb_id"),
						rs.getString("pinzb_id"),rs.getString("yunsfsb_id"),SysConstant.Fenx_Leij,rs.getDouble("jiesl"),
						rs.getDouble("meij"),rs.getDouble("meijs"),rs.getDouble("kuangqyf"),rs.getDouble("yunj"),
						rs.getDouble("yunjs"),rs.getDouble("daozzf"),rs.getDouble("tielzf"),rs.getDouble("gonglzf"),
						rs.getDouble("guanlfwf"),rs.getDouble("changnzf"),rs.getDouble("qit"),
						rs.getDouble("qnet_ar"),rs.getDouble("biaomdj"),rs.getDouble("buhsbmdj")));
				
				xuh++;
			}
			rs.close();
			sb.append("end;");
			if(sb.length()>13){
				
				if(con.getUpdate(sb.toString())>=0){
					this.setMsg("数据生成成功!");
					
				} else {
					this.setMsg("数据生成失败!");
				}
			}
			sb=null;
			con.Close();
		}
	}
	
	public StringBuffer InsertYuejsbmdj(String diancxxb_id,String riq,int xuh,
			String gongysb_id,String jihkjb_id,String pinzb_id,String yunsfsb_id,
			String fenx,double jiessl,double meij,double meijs,double kuangqyf,
			double yunj,double yunjs,double daozzf,double tielzf,double gonglzf,
			double guanlfwf,double changnzf,double qit,double qnet_ar,double biaomdj,
			double buhsbmdj){
		
		String str_Id="0";
//		先插入yuetjkjb
		StringBuffer sb=new StringBuffer();
		str_Id=MainGlobal.getNewID(Long.parseLong(diancxxb_id));
		sb.append("insert into yuetjkjb\n" +
				"  (id, riq, diancxxb_id, xuh, gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id)\n" + 
				"values\n" + 
				"  ("+str_Id+", to_date('"+riq+"','yyyy-MM-dd'), "+diancxxb_id+", "+xuh+", "+gongysb_id
				+", "+jihkjb_id+", "+pinzb_id+", "+yunsfsb_id+");\n");
		
		sb.append("insert into gd_yuejsbmdj\n" +
				"  (id, fenx, yuetjkjb_id, jiesl, meij, meijs, kuangqyf, yunj, yunjs, " +
				"daozzf, tielzf, gonglzf, guanlfwf, changnzf, qit, qnet_ar, biaomdj, buhsbmdj)\n" + 
				"values (\n" + 
				"getnewid(" + diancxxb_id + "), '"
				+ fenx + "', " 
				+ str_Id + ", "
				+ jiessl + ", "
				+ meij +", "
				+ meijs  +",  "
				+ kuangqyf +", "
				+ yunj +", "
				+ yunjs +", "
				+ daozzf +", "
				+ tielzf +", "
				+ gonglzf +", "
				+ guanlfwf +", "
				+ changnzf +", "
				+ qit +", "
				+ qnet_ar +", "
				+ biaomdj +", "
				+ buhsbmdj +");\n");
		
		return sb;
	}

	/**
	 * @param con
	 * @return   true:已上传状态中 不能修改数据 false:未上传状态中 可以修改数据
	 */
	private boolean getZhangt(JDBCcon con){
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from yuejsbmdj s, yuetjkjb k\n" + 
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
		Visit visit = (Visit) getPage().getVisit();
		String strDate="";
		strDate=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";	
		String diancxxb_id=this.getTreeid();
		String strSql = 
			"SELECT DJ.ID,\n" +
			"       GONGYSB.MINGC AS GONGYSB_ID,\n" + 
			"       JIHKJB.MINGC AS JIHKJB_ID,\n" + 
			"       PINZB.MINGC AS PINZB_ID,\n" + 
			"       YUNSFSB.MINGC AS YUNSFSB_ID,\n" + 
			"       FENX,\n" + 
			"       JIESL,\n" + 
			"       MEIJ,\n" + 
			"       MEIJS,\n" + 
			"       KUANGQYF,\n" + 
			"       YUNJ,\n" + 
			"       YUNJS,\n" + 
			"       DAOZZF,\n" + 
			"       TIELZF,\n" + 
			"       GONGLZF,\n" + 
			"       GUANLFWF,\n" + 
			"       CHANGNZF,\n" + 
			"       QIT,\n" + 
			"       QNET_AR,\n" + 
			"       BIAOMDJ,\n" + 
			"       BUHSBMDJ\n" + 
			"  FROM YUETJKJB TJ, GD_YUEJSBMDJ DJ, GONGYSB, JIHKJB, PINZB, YUNSFSB\n" + 
			" WHERE TJ.ID = DJ.YUETJKJB_ID\n" + 
			"   AND TJ.GONGYSB_ID = GONGYSB.ID\n" + 
			"   AND TJ.JIHKJB_ID = JIHKJB.ID\n" + 
			"   AND TJ.PINZB_ID = PINZB.ID\n" + 
			"   AND TJ.YUNSFSB_ID = YUNSFSB.ID\n" + 
			"   AND DJ.FENX = '" + SysConstant.Fenx_Beny + "'\n" +  
			"   AND DIANCXXB_ID = " + diancxxb_id + "\n" + 
			"   AND RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
			" ORDER BY DJ.ID";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置表名称用于保存
		egu.setTableName("gd_yuejsbmdj");
		// 设置显示列名称
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("gongysb_id").setEditor(null);
		
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);	
		egu.getColumn("jihkjb_id").setEditor(null);
		
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("pinzb_id").setEditor(null);
		
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setEditor(null);
		
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		
		egu.getColumn("jiesl").setHeader("结算量");
		egu.getColumn("jiesl").setWidth(60);
		egu.getColumn("jiesl").setEditor(null);
		
		egu.getColumn("meij").setHeader("煤价");
		egu.getColumn("meij").setWidth(60);	
		egu.getColumn("meij").setEditor(null);
		
		egu.getColumn("meijs").setHeader("煤价税");
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("meijs").setEditor(null);
		
		egu.getColumn("kuangqyf").setHeader("矿区杂费");
		egu.getColumn("kuangqyf").setWidth(80);
		egu.getColumn("kuangqyf").setEditor(null);
		
		egu.getColumn("yunj").setHeader("运价");
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunj").setEditor(null);
		
		egu.getColumn("yunjs").setHeader("运价税");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setEditor(null);
		
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);
		
		egu.getColumn("tielzf").setHeader("铁路杂费");
		egu.getColumn("tielzf").setWidth(80);
		egu.getColumn("tielzf").setEditor(null);
		
		egu.getColumn("gonglzf").setHeader("公路杂费");
		egu.getColumn("gonglzf").setWidth(80);
		
		egu.getColumn("guanlfwf").setHeader("管理服务费");
		egu.getColumn("guanlfwf").setWidth(80);
		
		egu.getColumn("changnzf").setHeader("厂内杂费");
		egu.getColumn("changnzf").setWidth(80);
		
		egu.getColumn("qit").setHeader("其他费用");
		egu.getColumn("qit").setWidth(80);
		
		egu.getColumn("qnet_ar").setHeader("结算热量");
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("qnet_ar").setEditor(null);
		
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("biaomdj").setWidth(60);
		egu.getColumn("biaomdj").setEditor(null);
		
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		egu.getColumn("buhsbmdj").setWidth(100);
		egu.getColumn("buhsbmdj").setEditor(null);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.setDefaultsortable(false);    
		// /设置按钮
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
		
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addToolbarItem(new ToolbarText("-").getScript());
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
						MainGlobal.getExtMessageBox(
								"'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'"
								,true))
						.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if(getZhangt(con)){
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		}else{
			
	//		 生成按钮
			GridButton gbc = new GridButton("生成",
					getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
	//		删除按钮
			GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
	//		 保存按钮
			GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
					egu.getGridColumns(), "SaveButton");
			egu.addTbarBtn(gbs);
		}
////		计算按钮
//		GridButton gbct = new GridButton("计算","function(){computAll(gridDiv_ds);}") ;
//		gbct.setIcon(SysConstant.Btn_Icon_Count);
//		egu.addTbarBtn(gbct);
		
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
			"var end = url.indexOf(';');"+
	         "url = url.substring(0,end);"+
	         "url = url + '?service=page/' + 'GdMonthReport&lx=yuejsbmdj';" +
	         " window.open(url,'newWin');";
		GridButton gbp = new GridButton("打印","function (){"+str+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
		StringBuffer sb = new StringBuffer();
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"\n" + 
				"  if(e.field=='DAOZZF'||e.field=='QIT'||e.field=='GONGLZF'||e.field=='GUANLFWF'||e.field=='CHANGNZF'){\n" + 
				"\n" + 
				"    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ'))+eval(gridDiv_ds.getAt(i).get('KUANGQYF'))+eval(gridDiv_ds.getAt(i).get('YUNJ'))+eval(gridDiv_ds.getAt(i).get('DAOZZF'))+eval(gridDiv_ds.getAt(i).get('TIELZF'))+eval(gridDiv_ds.getAt(i).get('GONGLZF'))+eval(gridDiv_ds.getAt(i).get('GUANLFWF'))+eval(gridDiv_ds.getAt(i).get('CHANGNZF'))+eval(gridDiv_ds.getAt(i).get('QIT'));\n" + 
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),2);\n" + 
				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS'))-eval(gridDiv_ds.getAt(i).get('YUNJS')))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')),2);\n" + 
				"\n" + 
				"    gridDiv_ds.getAt(i).set('BIAOMDJ',biaomdj);\n" + 
				"    gridDiv_ds.getAt(i).set('BUHSBMDJ',buhsbmdj);\n" + 
				"  }\n" + 
				"});"
		);
		
//		 设定合计列不保存
//		sb.append("function gridDiv_save(record){if(record.get('gongysb_id')=='总计') return 'continue';}");
		
		egu.addOtherScript(sb.toString());

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianfValue() + "年" + getYuefValue() + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			setRiq();
			getSelectData();
		}

	}
	
	private boolean Cleaning_up_History(String Date,String Condition){
//		当用户点击“生成”按钮时，要求先删除两个表的数据，才能重新计算
//		当用户点击“保存”按钮时，要求先将累计数据删除，再从新计算累计值
		JDBCcon con = new JDBCcon();
		JDBCcon con2 = new JDBCcon();
		String sql="";
		boolean Falg=false;
		con.setAutoCommit(false);
		String diancxxb_id=this.getTreeid();
		
		sql=
				"delete from gd_yuejsbmdj where id in (\n" +
				"    select js.id\n" + 
				"       from gd_yuejsbmdj js,yuetjkjb kj\n" + 
				"       where js.yuetjkjb_id=kj.id\n" + 
				"			  and kj.diancxxb_id=" +diancxxb_id+"\n"+
				"             and kj.riq=to_date('"+Date+"','yyyy-MM-dd')\n" +Condition+ 
				")";
		if(con.getDelete(sql)>=0){
			
			sql=
				"delete from yuetjkjb where id in (\n" +
				"    select kj.id\n" + 
				"       from gd_yuejsbmdj js,yuetjkjb kj\n" + 
				"       where js.yuetjkjb_id=kj.id\n" + 
				"			  and kj.diancxxb_id=" +diancxxb_id+"\n"+
				"             and kj.riq=to_date('"+Date+"','yyyy-MM-dd')\n" +Condition+
				")";
			
			if(con2.getDelete(sql)>=0){
				
				con.commit();
				Falg=true;
			}else{
				
				con.rollBack();
			}
		}
		con2.Close();
		con.Close();
		return Falg;
	}
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
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
}
