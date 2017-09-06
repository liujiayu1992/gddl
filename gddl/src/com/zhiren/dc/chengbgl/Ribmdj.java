package com.zhiren.dc.chengbgl;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ribmdj extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		String sql="";
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(rsl.next()){
			sql="update ribmdjb set yunj="+rsl.getDouble("yunj")
				+",yunjs="+rsl.getDouble("yunjs")
				+",daozzf="+rsl.getDouble("daozzf")
				+",zaf="+rsl.getDouble("zaf")
				+",qit="+rsl.getDouble("qit")
				+" where fahb_id in(select id from fahb where gongysb_id="+rsl.getLong("gongysb_id")
				+ " and jihkjb_id="+rsl.getLong("jihkjb_id")
				+ " and pinzb_id="+rsl.getLong("pinzb_id")
				+" and yunsfsb_id="+rsl.getLong("yunsfsb_id")
				+" and daohrq=to_date('"+getRiq()+"','yyyy-mm-dd')"
				+")";
			con.getUpdate(sql);
			sql="update ribmdjb set biaomdj=decode(qnet_ar,0,0,round((meij+meijs+yunj+yunjs+daozzf+zaf+qit)*29.271/qnet_ar,2))"
				+ ",buhsbmdj=decode(qnet_ar,0,0,round((meij+yunj+daozzf+zaf+qit)*29.271/qnet_ar,2))"
				+" where fahb_id in(select id from fahb where gongysb_id="+rsl.getLong("gongysb_id")
				+ " and jihkjb_id="+rsl.getLong("jihkjb_id")
				+ " and pinzb_id="+rsl.getLong("pinzb_id")
				+" and yunsfsb_id="+rsl.getLong("yunsfsb_id")
				+" and daohrq=to_date('"+getRiq()+"','yyyy-mm-dd')"
				+")";
			con.getUpdate(sql);
		}
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
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
		if (_SaveClick) {
			_SaveClick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
			getSelectData();
		}
	}
	public void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strSql="";
		ResultSetList rs=con.getResultSetList(strSql);
		while(rs.next()){
			con.getDelete("delete from ribmdjb  where fahb_id in(select id from fahb where daohrq=to_date('"+getRiq()+"','yyyy-mm-dd'))");
		}
		
	}
	
	public double getQnetar(long gongys,long jihkj,long pinz,long yunsfs,int tians){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String strSql="select decode(sum(jingz),0,0,round(sum(qnet_ar*jingz)/sum(jingz),2)) as qnet_ar from fahb,zhilb where fahb.gongysb_id="+gongys
			+" and jihkjb_id="+jihkj
			+" and pinzb_id="+pinz
			+" and yunsfsb_id="+yunsfs
			+" and daohrq>=to_date('"+getRiq()+"','yyyy-mm-dd')-"+tians
			+" and daohrq<=to_date('"+getRiq()+"','yyyy-mm-dd')"
			+" and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(strSql);
		if (rs.next()){
			return rs.getDouble("qnet_ar");
		}else{
			return 0;
		}
		
	}
	public void CreateData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strDate=this.getRiq();
		String strSql="";
		String sql="";
		double dblHetj=0;
		double dblRelzj=0;
		double dblLiufzj=0;
		double dblHuifzj=0;
		double dblHuiffzj=0;
		double dblShuifzj=0;
		double dblMeij=0;
		double dblMeijs=0;
		double dblYunj=0;
		double dblYunjs=0;
		double dblDaozzf=0;
		double dblZaf=0;
		double dblQit=0;
		double dblQnetar=0;
		
		strSql="select fahb.id as fahbid,fahb.gongysb_id,fahb.jihkjb_id,fahb.pinzb_id,fahb.yunsfsb_id,nvl(qnet_ar,0) as qnet_ar,fahb.ches,jiesb_id,fahb.hetb_id,nvl(hansdj,0) as hansdj,\n"
			+ " decode(nvl(jiessl,0),0,0,round(nvl(buhsmk,0)/nvl(jiessl,0),2)) as meij,decode(nvl(jiessl,0),0,0,round(nvl(shuik,0)/nvl(jiessl,0),2)) as meijs\n"  
			+ " from (select id,diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id,ches,zhilb_id,jiesb_id,hetb_id from fahb where daohrq=to_date('"+getRiq()+"','yyyy-mm-dd')\n" 
			+ " union select fahb.id,fahb.diancxxb_id,fahb.gongysb_id,fahb.jihkjb_id,fahb.pinzb_id,fahb.yunsfsb_id,fahb.ches,zhilb_id,fahb.jiesb_id,fahb.hetb_id from fahb,ribmdjb\n"
			+ " where fahb.id=ribmdjb.fahb_id and ribmdjb.meikjsbz=0 and yunfjsbz<>1)fahb,\n"
			+ " jiesb,zhilb where fahb.jiesb_id=jiesb.id(+) and fahb.zhilb_id=zhilb.id(+) and fahb.diancxxb_id="+visit.getDiancxxb_id();
		
		ResultSetList rs = con.getResultSetList(strSql);
		while (rs.next()){
			dblMeij=rs.getDouble("meij");
			dblMeijs=rs.getDouble("meijs");
			
			if (rs.getDouble("qnet_ar")==0){
				//取最近一次结算热值
				strSql="select nvl(max(jiesb_id),0) as jiesb_id from fahb where gongysb_id="+rs.getLong("gongysb_id")
					+" and jihkjb_id="+rs.getLong("jihkjb_id")
					+" and pinzb_id="+rs.getLong("pinzb_id")
					+ " and yunsfsb_id="+rs.getLong("yunsfsb_id");
				ResultSetList rec = con.getResultSetList(strSql);
				if (rec.next()){
					strSql="select nvl(jieszbsjb.jies,0) as jies from jieszbsjb,zhibb where jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='Qnetar' and  jieszbsjb.jiesdid="+rec.getLong("jiesb_id");
					ResultSetList recJsrl = con.getResultSetList(strSql);
					if (recJsrl.next()){
						if (recJsrl.getDouble("jies")!=0){
							dblQnetar=recJsrl.getDouble("jies");
						}else{//取一段时间的热值。天数通过参数设定。
							dblQnetar=getQnetar(rs.getLong("gongysb_id"),rs.getLong("jihkjb_id"),rs.getLong("pinzb_id"),rs.getLong("yunsfsb_id"),30);
						}
					}
				}
			}else{
				dblQnetar=rs.getDouble("qnet_ar");
			}
			
			int intMeikjsbz=0;//煤款结算标志 1全部结算，0未结算
			if (rs.getLong("jiesb_id")==0){//未结算，按合同估算。
				//
				
				
			}else{//已经结算				
				strSql="select jieszbsjb.zhejbz from jieszbsjb,zhibb where jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='Qnetar' and  jieszbsjb.jiesdid="+rs.getLong("jiesb_id");
				ResultSetList rsZhej=con.getResultSetList(strSql);
				if (rsZhej.next()){
					dblRelzj=rsZhej.getDouble("zhejbz");
				}
				strSql="select jieszbsjb.zhejbz from jieszbsjb,zhibb where jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='Std' and  jieszbsjb.jiesdid="+rs.getLong("jiesb_id");
				rsZhej=con.getResultSetList(strSql);
				if (rsZhej.next()){
					dblLiufzj=rsZhej.getDouble("zhejbz");
				}
				strSql="select jieszbsjb.zhejbz from jieszbsjb,zhibb where jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='Ad' and  jieszbsjb.jiesdid="+rs.getLong("jiesb_id");
				rsZhej=con.getResultSetList(strSql);
				if (rsZhej.next()){
					dblHuifzj=rsZhej.getDouble("zhejbz");
				}
				strSql="select jieszbsjb.zhejbz from jieszbsjb,zhibb where jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='Vdaf' and  jieszbsjb.jiesdid="+rs.getLong("jiesb_id");
				rsZhej=con.getResultSetList(strSql);
				if (rsZhej.next()){
					dblHuiffzj=rsZhej.getDouble("zhejbz");
				}
				strSql="select jieszbsjb.zhejbz from jieszbsjb,zhibb where jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='Mt' and  jieszbsjb.jiesdid="+rs.getLong("jiesb_id");
				rsZhej=con.getResultSetList(strSql);
				if (rsZhej.next()){
					dblShuifzj=rsZhej.getDouble("zhejbz");
				}
				dblHetj=rs.getDouble("hansdj")-dblRelzj-dblLiufzj-dblHuifzj-dblHuiffzj-dblShuifzj;
				intMeikjsbz=1;
			}
			//运费
			int intYunfjsbz=0;//运费结算标志 1全部结算，0未结算，2部分车皮结算
			strSql="select c.fahb_id,y.id as jiesyfb_id,decode(sum(jiessl),0,0,round(sum(buhsyf)/sum(jiessl),2)) as yunj,\n"
				+ " decode(sum(jiessl),0,0,round(sum(shuik)/sum(jiessl),2)) as yunjs,count(*) as cheps \n"
				+ " from danjcpb dj,chepb c,jiesyfb y\n"
				+ " where dj.chepb_id=c.id and dj.yunfjsb_id=y.id and c.fahb_id="+rs.getLong("fahbid")
				+ " group by c.fahb_id,y.id";
			ResultSetList rsYunf = con.getResultSetList(strSql);
			if (rsYunf.next()){
				if(rs.getLong("ches")==rsYunf.getLong("cheps")){
					intYunfjsbz=1;
				}else{
					intYunfjsbz=2;
				}
				dblYunj=rsYunf.getDouble("yunj");
				dblYunjs=rsYunf.getDouble("yunjs");
			}else{
				//取合同中运价
				strSql="select nvl(avg(yunj),0) as yunj from hetjgb where hetb_id="+rs.getLong("hetb_id")+" and yunj<>0";
				ResultSetList rsYunj = con.getResultSetList(strSql);
				if(rsYunj.next()){
					dblYunj=(double)Math.round(rsYunj.getDouble("yunj")*0.93*100)/100;
					dblYunjs=(double)Math.round(rsYunj.getDouble("yunj")*0.07*100)/100;
				}
			}
			
			//杂费
			strSql="select c.fahb_id,decode(sum(dj.biaoz),0,0,round(sum(zongje)/sum(dj.biaoz),2))" 
				+ " as zaf \n"
				+ " from yunfdjb dj,danjcpb cp,feiylbb lb,chepb c\n"
				+ " where dj.id=cp.yunfdjb_id and cp.chepb_id=c.id and\n" 
				+ " dj.feiylbb_id=lb.id and c.fahb_id="+rs.getLong("fahbid");
			String strSqlDz=strSql+" and lb.leib =3 group by c.fahb_id";
			ResultSetList rsDaozzf = con.getResultSetList(strSqlDz);
			if (rsDaozzf.next()){
				dblDaozzf=rsDaozzf.getDouble("zaf");
			}
			String strSqlzf=strSql+" and lb.leib in(2,4,5) group by c.fahb_id";
			ResultSetList rsZaf = con.getResultSetList(strSqlzf);
			if (rsZaf.next()){
				dblZaf=rsZaf.getDouble("zaf");
			}
			String strSqlqt=strSql+" and lb.leib=6 group by c.fahb_id";
			ResultSetList rsQit = con.getResultSetList(strSqlqt);
			if (rsQit.next()){
				dblQit=rsQit.getDouble("zaf");
			}
			
			double dblBuhsbmdj=0;
			double dblBiaomdj=0;
			if(dblQnetar!=0){
				dblBuhsbmdj=(double)Math.round((dblMeij+dblYunj+dblDaozzf+dblZaf+dblQit)*29.271/dblQnetar*100)/100;
				dblBiaomdj=(double)Math.round((dblMeij+dblMeijs+dblYunj+dblYunjs+dblDaozzf+dblZaf+dblQit)*29.271/dblQnetar*100)/100;
			}
			
			strSql="select * from ribmdjb where fahb_id="+rs.getLong("fahbid");
			ResultSetList rsFah = con.getResultSetList(strSql);
			if(rsFah.next()){
				if (rsFah.getInt("meikjsbz")==0){
					sql="update ribmdjb set hetj="+dblHetj+",relzj="+dblRelzj+",liufzj="+dblLiufzj+",huifzj="+dblHuifzj+",huiffzj="
						+dblHuiffzj+",shuifzj="+dblShuifzj+",meij="+dblMeij+",meijs="+dblMeijs+",meikjsbz="+intMeikjsbz+" where fahb_id="+rs.getLong("fahbid");
					con.getUpdate(sql);
				}
				if (rsFah.getInt("yunfjsbz")!=1){
					sql="update ribmdjb set yunj="+dblYunj+",yunjs="+dblYunjs+",daozzf="+dblDaozzf+",zaf="+dblZaf+",qit="+dblQit+",yunfjsbz="+intYunfjsbz+" where fahb_id="+rs.getLong("fahbid");
					con.getUpdate(sql);
				}
				sql="update ribmdjb set qnet_ar="+dblQnetar+",biaomdj="+dblBiaomdj+",buhsbmdj="+dblBuhsbmdj+" where fahb_id="+rs.getLong("fahbid");
				con.getUpdate(sql);
			}else{
				sql="insert into ribmdjb(id,fahb_id,hetj,relzj,liufzj,huifzj,huiffzj,shuifzj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,qnet_ar,biaomdj,buhsbmdj,meikjsbz,yunfjsbz) values(\n"
					+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
					+","+rs.getLong("fahbid")
					+","+dblHetj
					+","+dblRelzj
					+","+dblLiufzj
					+","+dblHuifzj
					+","+dblHuiffzj
					+","+dblShuifzj
					+","+dblMeij
					+","+dblMeijs
					+","+dblYunj
					+","+dblYunjs
					+","+dblDaozzf
					+","+dblZaf
					+","+dblQit
					+","+dblQnetar
					+","+dblBiaomdj
					+","+dblBuhsbmdj
					+","+intMeikjsbz
					+","+intYunfjsbz
					+")";
				con.getInsert(sql);
			}
			
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strDate="";
		strDate=this.getRiq();
		if(strDate==null||strDate.equals("")){
			strDate=DateUtil.FormatDate(new Date());
		}
		String strSql="";
		strSql="select gongysb.mingc as gongys,gongysb.id as gongysb_id,jihkjb.mingc as jihkj,jihkjb.id as jihkjb_id,pinzb.mingc as pinz,pinzb.id as  pinzb_id,yunsfsb.mingc as yunsfs,yunsfsb.id as yunsfsb_id,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*hetj)/sum(jingz),2)) as hetj,decode(sum(jingz),0,0,round(sum(jingz*relzj)/sum(jingz),2)) as relzj,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*liufzj)/sum(jingz),2)) as liufzj,decode(sum(jingz),0,0,round(sum(jingz*huifzj)/sum(jingz),2)) as huifzj,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*huiffzj)/sum(jingz),2)) as huiffzj,decode(sum(jingz),0,0,round(sum(jingz*shuifzj)/sum(jingz),2)) as shuifzj,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*meij)/sum(jingz),2)) as meij,decode(sum(jingz),0,0,round(sum(jingz*meijs)/sum(jingz),2)) as meijs,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*yunj)/sum(jingz),2)) as yunj,decode(sum(jingz),0,0,round(sum(jingz*yunjs)/sum(jingz),2)) as yunjs,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*daozzf)/sum(jingz),2)) as daozzf,decode(sum(jingz),0,0,round(sum(jingz*zaf)/sum(jingz),2)) as zaf,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*qit)/sum(jingz),2)) as qit,decode(sum(jingz),0,0,round(sum(jingz*qnet_ar)/sum(jingz),2)) as qnet_ar,\n"
			+ " decode(sum(jingz),0,0,round(sum(jingz*biaomdj)/sum(jingz),2)) as biaomdj,decode(sum(jingz),0,0,round(sum(jingz*buhsbmdj)/sum(jingz),2)) as buhsbmdj \n"
			+ " from ribmdjb tj,fahb sl,gongysb,jihkjb,pinzb,yunsfsb\n"
			+ " where tj.fahb_id=sl.id and sl.gongysb_id=gongysb.id and sl.jihkjb_id=jihkjb.id \n"
			+ " and sl.pinzb_id=pinzb.id and sl.yunsfsb_id=yunsfsb.id and sl.diancxxb_id="+visit.getDiancxxb_id()+" and sl.daohrq=to_date('"+strDate+"','yyyy-mm-dd')"
			+ " group by gongysb.mingc,gongysb.id,jihkjb.mingc,jihkjb.id,pinzb.mingc,pinzb.id,yunsfsb.mingc,yunsfsb.id";
		JDBCcon con = new JDBCcon();
				
		ResultSetList rsl = con.getResultSetList(strSql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("ribmdjb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		egu.setHeight("bodyHeight");
		
		//egu.getColumn("xuh").setHeader("序号");
		//egu.getColumn("xuh").setWidth(50);
		egu.getColumn("gongys").setHeader("供应商");
		egu.getColumn("gongys").setWidth(120);
		egu.getColumn("jihkj").setHeader("计划口径");
		egu.getColumn("jihkj").setWidth(80);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("yunsfs").setHeader("运输方式");
		egu.getColumn("yunsfs").setWidth(80);
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("hetj").setHeader("合同价");
		egu.getColumn("hetj").setWidth(60);
		egu.getColumn("relzj").setHeader("热量折价");
		egu.getColumn("relzj").setWidth(60);
		egu.getColumn("liufzj").setHeader("硫分折价");
		egu.getColumn("liufzj").setWidth(60);
		egu.getColumn("huifzj").setHeader("灰分折价");
		egu.getColumn("huifzj").setWidth(60);
		egu.getColumn("huiffzj").setHeader("挥发份折价");
		egu.getColumn("huiffzj").setWidth(80);
		egu.getColumn("shuifzj").setHeader("水分折价");
		egu.getColumn("shuifzj").setWidth(60);
		egu.getColumn("meij").setHeader("不含税煤价");
		egu.getColumn("meij").setWidth(80);
		egu.getColumn("meijs").setHeader("煤价税");
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("yunj").setHeader("不含税运价");
		egu.getColumn("yunj").setWidth(80);
		egu.getColumn("yunjs").setHeader("运价税");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);
		egu.getColumn("zaf").setHeader("杂费");
		egu.getColumn("zaf").setWidth(60);
		egu.getColumn("qit").setHeader("其他杂费");
		egu.getColumn("qit").setWidth(60);
		egu.getColumn("qnet_ar").setHeader("热值");
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("biaomdj").setHeader("含税标煤单价");
		egu.getColumn("biaomdj").setWidth(90);
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		egu.getColumn("buhsbmdj").setWidth(90);
		//NumberField nf = new NumberField();
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("hetj").setEditor(null);
		egu.getColumn("relzj").setEditor(null);
		egu.getColumn("liufzj").setEditor(null);
		egu.getColumn("huifzj").setEditor(null);
		egu.getColumn("huiffzj").setEditor(null);
		egu.getColumn("shuifzj").setEditor(null);
		egu.getColumn("meij").setEditor(null);
		egu.getColumn("meijs").setEditor(null);
		egu.getColumn("biaomdj").setEditor(null);
		egu.getColumn("buhsbmdj").setEditor(null);
		egu.getColumn("qnet_ar").setEditor(null);
		egu.setDefaultsortable(false);             
		egu.getColumn("gongysb_id").hidden=true;
		egu.getColumn("jihkjb_id").hidden=true;
		egu.getColumn("pinzb_id").hidden=true;
		egu.getColumn("yunsfsb_id").hidden=true;
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
		sb.append("rec = gridDiv_ds.getAt(e.row);").append("if(e.field=='YUNJ'){").append("if(rec.get('QNET_AR')==0){rec.set('BIAOMDJ',0); }else{rec.set('BIAOMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('MEIJS')||0)+eval(rec.get('YUNJ')||0)+eval(rec.get('YUNJS')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='YUNJS'){").append("if(rec.get('QNET_AR')==0){rec.set('BIAOMDJ',0); }else{rec.set('BIAOMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('MEIJS')||0)+eval(rec.get('YUNJ')||0)+eval(rec.get('YUNJS')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='DAOZZF'){").append("if(rec.get('QNET_AR')==0){rec.set('BIAOMDJ',0); }else{rec.set('BIAOMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('MEIJS')||0)+eval(rec.get('YUNJ')||0)+eval(rec.get('YUNJS')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='ZAF'){").append("if(rec.get('QNET_AR')==0){rec.set('BIAOMDJ',0); }else{rec.set('BIAOMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('MEIJS')||0)+eval(rec.get('YUNJ')||0)+eval(rec.get('YUNJS')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='QIT'){").append("if(rec.get('QNET_AR')==0){rec.set('BIAOMDJ',0); }else{rec.set('BIAOMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('MEIJS')||0)+eval(rec.get('YUNJ')||0)+eval(rec.get('YUNJS')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='YUNJ'){").append("if(rec.get('QNET_AR')==0){rec.set('BUHSBMDJ',0); }else{rec.set('BUHSBMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('YUNJ')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='YUNJS'){").append("if(rec.get('QNET_AR')==0){rec.set('BUHSBMDJ',0); }else{rec.set('BUHSBMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('YUNJ')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='DAOZZF'){").append("if(rec.get('QNET_AR')==0){rec.set('BUHSBMDJ',0); }else{rec.set('BUHSBMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('YUNJ')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='ZAF'){").append("if(rec.get('QNET_AR')==0){rec.set('BUHSBMDJ',0); }else{rec.set('BUHSBMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('YUNJ')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("if(e.field=='QIT'){").append("if(rec.get('QNET_AR')==0){rec.set('BUHSBMDJ',0); }else{rec.set('BUHSBMDJ', Round((eval(rec.get('MEIJ')||0) +eval(rec.get('YUNJ')||0)+eval(rec.get('DAOZZF')||0)+eval(rec.get('ZAF')||0)+eval(rec.get('QIT')||0))*29.271/eval(rec.get('QNET_AR')||0),2));}").append("}");
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		//df.setId("RIQ");
		egu.addToolbarItem(df.getScript());
		
		egu.addToolbarItem("{"+new GridButton("刷新","function (){document.getElementById('RefreshButton').click()}").getScript()+"}");
		egu.addToolbarItem("{"+new GridButton("生成","function (){document.getElementById('CreateButton').click()}").getScript()+"}");
		egu.addToolbarItem("{"+new GridButton("删除","function (){document.getElementById('DelButton').click()}").getScript()+"}");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        		"var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
                 "url = url + '?service=page/' + 'Chengbgl&lx=ribmdj';" +
                 " window.open(url,'newWin');";
		egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");

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
			this.setRiq(null);
			getSelectData();
			
		}
	}
	private String riq;

	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq!=null &&!this.riq.equals(riq)){
			this.riq = riq;
		}
	}
	 
}
