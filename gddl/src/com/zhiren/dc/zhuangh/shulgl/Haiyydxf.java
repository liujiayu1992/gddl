package com.zhiren.dc.zhuangh.shulgl;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2012-08-22
 * 描述：在保存时增加煤堆名称重复的检查。
 * 		 在点击保存时，如果发现重复的信息，提示“煤堆名称重复，请重新选择”
 */
/*
* 作者：赵胜男
* 时间：2012-11-09
* 描述：在保存时增加煤堆名称重复的检查。
* 		 在点击保存时，增加煤场和煤堆不匹配的判断，第'+eval(i+1)+'行煤场名称与煤堆名称不匹配，请重新选择！”
*        修正煤堆名称的下拉框
*/
public class Haiyydxf extends BasePage implements PageValidateListener {
	
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
	}

//	 绑定日期
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			setRiqi(DateUtil.FormatDate(new Date()));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}

    //页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void Save1Button(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private void Return(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if("shenh".equals(visit.getStringBuffer20().toString())){
			cycle.activate("Haiyydlrsh");
		}else{
			
			cycle.activate("Haiyydlr_zh");
		}
	}

    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}
	
	//取得最大的采样XUH
	public String creatcaiybm(JDBCcon con,String date,long diancxxb_id) {
		String sql="select To_number(Substr(max(bianm), 9, 2))+1 as xuh from caiyb " +
				"where caiyrq=to_date('"+date+"','yyyy-mm-dd') and zhilb_id="+diancxxb_id+"";
		
		String xuh="01";
		ResultSetList rsl = con.getResultSetList(sql);
		
		while(rsl.next()){
			int xuh1=rsl.getInt("xuh");
			if (xuh1<10){
				xuh="0"+xuh1;
			}
		}
		rsl.close();
		
		return xuh;
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	public void Save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		int count_fah=0;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){

			String cc ="delete from fahmcb " +
			"where id="+rsl.getString("fbid")+";\n";
			sb.append(cc.toString());
			
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("没有需要保存的记录！");
			return;
		}
		
		while (rsl.next()) {
			//fahb
			long diancxxb_id=visit.getDiancxxb_id();
			String gongysb_id=rsl.getString("gongysb_id");
			String zhilb_id=rsl.getString("zhilb_id");
			String meikxxb_id=rsl.getString("meikxxb_id");
			String pinzb_id=rsl.getString("pinzb_id");
			String faz_id=rsl.getString("faz_id");
			String daoz_id=rsl.getString("daoz_id");
			String hetb_id=rsl.getString("hetb_id");
			String luncxxb_id=rsl.getString("luncxxb_id");
			String jihkjb_id="1";//无计划口径
//			String jihkjb_id="0";
			String jihkjsql="select id from jihkjb where mingc ='重点订货'";
			if (con.getHasIt(jihkjsql)){
				jihkjb_id="(select id from jihkjb where mingc ='重点订货')";
			}
			
			if(rsl.getString("jmingc")==null||"".equals(rsl.getString("jmingc"))){
				
			}else{
				
		
				jihkjb_id=String.valueOf((getExtGrid().getColumn("jmingc").combo).getBeanId(rsl.getString("jmingc")));
			}
			
			String fahrq="to_date('"+rsl.getString("fahrq")+"','yyyy-mm-dd')";
			String daohrq=endriq;
			String yunsfsb_id=rsl.getString("yunsfsb_id");
			String kaobrq="to_date('"+rsl.getString("kaobrq")+"','yyyy-mm-dd')";
			String yundh=rsl.getString("yundh");
			
			ResultSetList rs;
			String sql=
				"select max(f.yundh)+1 yund from fahb f where to_char(f.daohrq,'yyyy-mm-dd')='"+getRiqi()+"' and f.yundh like '"+getRiqi().replaceAll("-", "")+"%'";

			if("".equals(yundh)||yundh==null){
				rs=con.getResultSetList(sql);
				while(rs.next()){
					if("".equals(rs.getString("yund"))||rs.getString("yund")==null){
						
						
						yundh=getRiqi().replaceAll("-", "")+"00";
					}else{
						yundh=rs.getString("yund");
					}
				}
			}
			

			
			String chec=rsl.getString("chec");
			double maoz=rsl.getDouble("maoz");
			double piz=rsl.getDouble("piz");
			double biaoz=rsl.getDouble("biaoz");
			double jingz=maoz-piz;
			double yingd=0;
			double yingk=0;
			double yuns=0;
			if ((maoz-piz-biaoz)<0) {
				if (Math.abs((maoz-piz-biaoz))>(biaoz*0.015)){
					yuns=(biaoz*0.015);
					yingk=0-(Math.abs((maoz-piz-biaoz))-(biaoz*0.015));
				}else{
					yuns=maoz-piz-biaoz;
					yingk=0;
				}
			}else{
				yuns=0;
				yingk=0;
				yingd=maoz-piz-biaoz;
			}
			
			double yunsl=0;
			double koud=0;
			double kous=0;
			double kouz=0;
			double koum=0;
			double zongkd=0;
			double sanfsl=rsl.getDouble("sanfsl");
			double ches=0;
			long tiaozbz=0;
			long yansbhb_id=0;
//			long lie_id=0;
			long yuandz_id=0;
			long yuanshdwb_id=0;
			long kuangfzlb_id=0;
			long liucb_id=0;
			long liucztb_id=0;
			long hedbz=0;
			String  meicb_id=rsl.getString("meicb_id");
			String duowmc=rsl.getString("duowmc");
			String beiz="";
			//zhilb 
			String huaybh = rsl.getString("huaybh");
		
			
			if ("0".equals(rsl.getString("id"))) {
				
				
					
				//质量临时表
				String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//				sb.append("insert into zhillsb (id,zhilb_id,shenhzt,huaylbb_id,huaylb)");
//				sb.append("select "+zhillsb_id+","+zhillsb_id+",0,id,mingc from leibb where mingc ='正常样';\n");
//				
				//采样表
				String xuh = creatcaiybm(con,endriq,diancxxb_id);//生成采样编码
				
				String caiyb_id = MainGlobal.getNewID(diancxxb_id);
				sb.append("insert into caiyb (id,zhilb_id,cunywzb_id,bianm,caiyrq,xuh) values ( \n");
				sb.append(caiyb_id).append(",").append(zhillsb_id).append(",0,");
				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
				sb.append("to_date('"+endriq+"','yyyy-mm-dd'),"+xuh+");\n");
				

//				
				// 插入发货煤场表
				String id =MainGlobal.getNewID(diancxxb_id);//发货id
				
				if ("0".equals(rsl.getString("id"))&&!"0".equals(visit.getStringBuffer9().toString())) {
					
					id=visit.getStringBuffer9().toString();
				}
				if(rsl.getString("fbid")==null||"".equals(rsl.getString("fbid"))){
					
					String fbid =MainGlobal.getNewID(diancxxb_id);//fbid
					
					String fahmcb=" insert into fahmcb (id,fahb_id,meicb_id,duowmc,meil) \n" +
							" values("+fbid+","+id+","
							+(getExtGrid().getColumn("meicb_id").combo).getBeanId(meicb_id)+",'"+rsl.getString("duowmc")+"',"+rsl.getInt("fbml")+"); \n";
					
					sb.append(fahmcb);
				}
				
				if(count_fah==0&&"0".equals(visit.getStringBuffer9().toString())){
					
					// 插入发货表
					sb.append("insert into fahb (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, \n");
					sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, hetb_id,  \n");
					sb.append("zhilb_id, jiesb_id, yunsfsb_id, chec, maoz, piz, jingz, biaoz,  \n");
					sb.append("yingd, yingk, yuns, yunsl, koud, kous, kouz, koum, zongkd,  \n");
					sb.append("sanfsl,meicb_id,duowmc, ches, tiaozbz, yansbhb_id, lie_id, yuandz_id,  \n");
					sb.append("yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz,  \n");
					sb.append("beiz,luncxxb_id,kaobrq,YUNDH,XIEMKSSJ,XIEMJSSJ,ZHUANGGKSSJ,ZHUANGGJSSJ) \n");
					sb.append("values (\n");
					sb.append(id).append(",").append(id).append(",").append(visit.getDiancxxb_id()).append(",");
					sb.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(gongysb_id)).append(",");
					sb.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(meikxxb_id)).append(",");
					sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(pinzb_id)).append(",");
					sb.append((getExtGrid().getColumn("faz_id").combo).getBeanId(faz_id)).append(",");
					sb.append((getExtGrid().getColumn("daoz_id").combo).getBeanId(daoz_id)).append(",");
					sb.append(jihkjb_id).append(",");
					sb.append(fahrq);
					sb.append(",to_date('").append(daohrq).append("','yyyy-mm-dd'),");
					sb.append((getExtGrid().getColumn("hetb_id").combo).getBeanId(hetb_id)).append(",");
					sb.append(zhillsb_id).append(",0,");
					sb.append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(yunsfsb_id)).append(",'");
					sb.append(chec).append("',").append(maoz).append(",").append(piz).append(",");
					sb.append(jingz).append(",").append(biaoz).append(",").append(yingd).append(",");
					sb.append(yingk).append(",").append(yuns).append(",").append(yunsl).append(",");
					sb.append(koud).append(",").append(kous).append(",").append(kouz).append(",");
					sb.append(koum).append(",").append(zongkd).append(",").append(sanfsl).append(",").
					append((getExtGrid().getColumn("meicb_id").combo).getBeanId(meicb_id)).append(",'").append(duowmc).append("',");
					sb.append(ches).append(",").append(tiaozbz).append(",").append(yansbhb_id).append(",");
					sb.append(id).append(",").append(yuandz_id).append(",").append(yuanshdwb_id).append(",");
					sb.append(kuangfzlb_id).append(",").append(liucb_id).append(",").append(liucztb_id).append(",").append(hedbz).append(",'");
					sb.append(beiz).append("',");
					sb.append((getExtGrid().getColumn("luncxxb_id").combo).getBeanId(luncxxb_id)).append(",");
					sb.append(kaobrq).append(",'").append(yundh).append("'")
//					.append("',").append(xiemkssj).append(",");
//					sb.append(xiemjssj).append(",").append(zhuanggkssj).append(",").append(zhuanggjssj)
					.append(");\n");
					
					String chepb_id =MainGlobal.getNewID(diancxxb_id);
					sb.append("insert into chepb(id,cheph,piaojh,yuanmz,maoz,piz,biaoz,yingd,yingk,yuns,ches,fahb_id,lury)values(\n" );
					sb.append(chepb_id).append(",'").append(luncxxb_id).append("','").append(yundh).append("',");
					sb.append(maoz).append(",").append(maoz).append(",").append(piz).append(",").append(biaoz).append(",").append(yingd).append(",").
					append(yingk).append(",").append(yuns).append(",").append("1").append(",").append(id).append(",'").append(visit.getRenymc()).append("');");		
				}
				count_fah++;
				
				if(rsl.getRows()==count_fah){
					
					visit.SetStringBuffer9(new StringBuffer(id));
					
				}

				
			}else{
				
                   if(rsl.getString("fbid")==null||"".equals(rsl.getString("fbid"))){
   					
   					String fbid =MainGlobal.getNewID(diancxxb_id);//fbid
   					
					String fahmcb=" insert into fahmcb (id,fahb_id,meicb_id,duowmc,meil) \n" +
							" values("+fbid+","+rsl.getString("id")+","
							+(getExtGrid().getColumn("meicb_id").combo).getBeanId(meicb_id)+",'"+rsl.getString("duowmc")+"',"+rsl.getInt("fbml")+"); \n";
					
					sb.append(fahmcb);
				}else{
					
					sb.append("update fahmcb set fahb_id="+rsl.getString("id"))
					.append(",meicb_id="+(getExtGrid().getColumn("meicb_id").combo).getBeanId(meicb_id)).append(",duowmc='"+rsl.getString("duowmc"))
					.append("',meil="+rsl.getString("fbml")).append(" where id="+rsl.getString("fbid")).append(";\n");
				}
				sb.append("update fahb set meikxxb_id=");
				sb.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(meikxxb_id));
				sb.append(",gongysb_id=");
				sb.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(gongysb_id));
				sb.append(",pinzb_id=");
				sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(pinzb_id));
				sb.append(",faz_id=");
				sb.append((getExtGrid().getColumn("faz_id").combo).getBeanId(faz_id));
				sb.append(",hetb_id=");
				sb.append((getExtGrid().getColumn("hetb_id").combo).getBeanId(hetb_id));
				sb.append(",chec='");
				sb.append(chec);
				sb.append("',sanfsl='");//三方数量
				sb.append(sanfsl);
				sb.append("',meicb_id="+(getExtGrid().getColumn("meicb_id").combo).getBeanId(meicb_id));//煤场id
//				sb.append(meicb_id);
				sb.append(",duowmc='");//煤堆名称
				sb.append(duowmc);
				
				sb.append("',maoz=").append(maoz);
				sb.append(",piz=").append(piz);
				sb.append(",jingz=").append(jingz);
				sb.append(",biaoz=").append(biaoz);
				sb.append(",yundh='").append(yundh).append("'");

				sb.append(" where fahb.id=").append(rsl.getString("id")).append(";\n");
 				
				sb.append("update chepb set cheph='");
				sb.append(luncxxb_id);
				sb.append("',piaojh='");
				sb.append(yundh);
				sb.append("',maoz=");
				sb.append(maoz);
				sb.append(",yuanmz=");
				sb.append(maoz);
				sb.append(",piz=");
				sb.append(piz);
				sb.append(",biaoz=");
				sb.append(biaoz);
				sb.append(",yingk=");
				sb.append(yingk);
				sb.append(",yingd=").append(yingd);
				sb.append(",yuns=").append(yuns).append(";\n");
			
			}
			
			
		}
		sb.append("end;");
		if(sb.length()>13){
			
		int flag = con.getInsert(sb.toString());
		
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
		
		con.commit();
		con.Close();
		}
	}

	public void getSelectData() {
		Visit visit = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String beginriq=visit.getStringBuffer3().toString();
		String endriq=visit.getString10();
		String fbid="";
		if("".equals(visit.getStringBuffer9().toString())||visit.getStringBuffer9().toString()==null){
		fbid="0";	
		}else{
			fbid=visit.getStringBuffer9().toString();
		}
		StringBuffer sbsql = new StringBuffer("");
		
		sbsql.append("select fahb.id,fb.id as fbid,fb.meil fbml,meicb.mingc as meicb_id,fb.duowmc,fahb.yundh,luncxxb.mingc as luncxxb_id, \n");
		sbsql.append("       gongysb.mingc as gongysb_id,\n");
//		sbsql.append("       meikxxb_id, \n");
		sbsql.append("       meikxxb.mingc as meikxxb_id, \n");
		sbsql.append("       yunsfsb.mingc as yunsfsb_id, \n");
		sbsql.append("       fahb.chec, \n");//zhilb.huaybh,
		sbsql.append("       hetb.hetbh hetb_id, \n");
		//sbsql.append("       fahb.faz_id, \n");
		sbsql.append("       chezxxb.mingc as faz_id, \n");
		sbsql.append(" DECODE ((SELECT cz.quanc\n ");
		sbsql.append(" FROM chezxxb cz, diancdzb dz\n");
		sbsql.append(" WHERE cz.ID = dz.chezxxb_id\n");
		sbsql.append(" AND dz.leib = '港口' AND dz.diancxxb_id = fahb.diancxxb_id),NULL, '请选择',\n");
		sbsql.append(" (SELECT cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id \n");
		sbsql.append(" AND dz.leib = '港口' AND dz.diancxxb_id = fahb.diancxxb_id)) AS daoz_id,\n");
		//sbsql.append("   	 fahb.pinzb_id, \n");
		sbsql.append("       pinzb.mingc as pinzb_id, \n");
		sbsql.append("       luncxxb.dunw, \n");
		sbsql.append("       fahb.maoz,fahb.piz,fahb.biaoz,fahb.sanfsl,\n");//fahb.jingz,
		//sbsql.append("       fahb.maoz, \n");
		//sbsql.append("   to_char(daohrq,'YYYY-MM-DD hh24:mi:ss')daohrq, \n");
		sbsql.append("  fahrq, \n");// 
		sbsql.append("   kaobrq kaobrq, \n");
		sbsql.append("   to_char(xiemkssj,'YYYY-MM-DD hh24:mi:ss')xiemkssj, \n");
		sbsql.append("   to_char(xiemjssj,'YYYY-MM-DD hh24:mi:ss')xiemjssj, \n");
		sbsql.append("   to_char(zhuanggkssj,'YYYY-MM-DD hh24:mi:ss')zhuanggkssj, \n");
		sbsql.append("   to_char(zhuanggjssj,'YYYY-MM-DD hh24:mi:ss')zhuanggjssj \n");
//		sbsql.append("   luncxxb.mingc as huocmc, \n");
		sbsql.append("  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb,meicb,fahmcb fb \n");
		sbsql.append(" where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+) \n");
		sbsql.append("   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+) \n");
		sbsql.append("   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)  \n");
		sbsql.append("   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)  \n");
		sbsql.append("   and yunsfsb.mingc='海运' and fahb.jiesb_id=0 \n").append(" and fahb.id="+fbid+"\n");
		sbsql.append(" and fb.meicb_id=meicb.id(+) \n").append("and fahb.id=fb.fahb_id \n");
		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+beginriq+"'  \n");
		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+endriq+"'  \n");
		sbsql.append("order by fahb.daohrq \n");
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		int count=0;
		int to=0;
		if(rsl.getRows()>0){
			
			count=rsl.getRows();
		}
		while(rsl.next()){
			to+=rsl.getInt("fbml");
		}
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("yundh").setHeader("运单号");
		egu.getColumn("yundh").setWidth(70);
		egu.getColumn("yundh").setEditor(null);
		egu.getColumn("fbid").setHeader("fbid");
		egu.getColumn("fbid").setWidth(70);
		egu.getColumn("fbid").setHidden(true);
		egu.getColumn("luncxxb_id").setHeader("货船名称");
		egu.getColumn("luncxxb_id").setEditor(null);
		egu.getColumn("luncxxb_id").setWidth(70);
		egu.getColumn("fbml").setHeader("煤堆量");
		egu.getColumn("fbml").setWidth(70);
		egu.getColumn("gongysb_id").setHeader("供应商名称");
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("chec").setHeader("船次");
		egu.getColumn("chec").setWidth(40);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("faz_id").setHeader("装船港");
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("daoz_id").setHeader("到船港");
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("pinzb_id").setHeader("煤种");
		egu.getColumn("pinzb_id").setWidth(60);
//		egu.getColumn("huaybh").setHeader("运输合同编号");
//		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("hetb_id").setHeader("采购合同编号");
		egu.getColumn("hetb_id").setWidth(120);
		egu.getColumn("hetb_id").setHidden(true);
		egu.getColumn("dunw").setHeader("停靠泊位");
		egu.getColumn("dunw").setWidth(70);
		egu.getColumn("dunw").setEditor(null);
		egu.getColumn("dunw").setDefaultValue(visit.getString14());
		egu.getColumn("dunw").setHidden(true);
//		egu.getColumn("dunw").setDefaultValue(visit.getString14());///
		egu.getColumn("maoz").setHeader("卸货量(吨)");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setHidden(true);
		egu.getColumn("piz").setHeader("皮重(吨)");
		egu.getColumn("piz").setHidden(true);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setDefaultValue("0");
		
		egu.getColumn("sanfsl").setHeader("水尺量(吨)");
//		egu.getColumn("sanfsl").setEditor(null);
		egu.getColumn("sanfsl").setWidth(70);
		egu.getColumn("sanfsl").setHidden(true);
		egu.getColumn("meicb_id").setHeader("煤场名称");//煤场id
		egu.getColumn("meicb_id").setWidth(70);//煤场id
		egu.getColumn("meicb_id").setReturnId(true);
		egu.getColumn("duowmc").setHeader("煤堆名称");//煤堆名称
		egu.getColumn("duowmc").setWidth(70);//煤堆名称
		
		egu.getColumn("biaoz").setHeader("运单量(吨)");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").setHidden(true);
		egu.getColumn("fahrq").setHeader("承运日期");
		egu.getColumn("fahrq").setWidth(90);
		egu.getColumn("fahrq").setHidden(true);
		egu.getColumn("kaobrq").setHeader("靠泊日期");
		egu.getColumn("kaobrq").setWidth(90);
		egu.getColumn("kaobrq").setHidden(true);
		egu.getColumn("xiemkssj").setHeader("卸煤开始时间");
		egu.getColumn("xiemkssj").setWidth(120);
		egu.getColumn("xiemkssj").setEditor(null);
		egu.getColumn("xiemkssj").setHidden(true);
		
		egu.getColumn("xiemjssj").setHeader("卸煤结束时间");
		egu.getColumn("xiemjssj").setWidth(120);
		egu.getColumn("xiemjssj").setEditor(null);
		egu.getColumn("xiemjssj").setHidden(true);
		
		egu.getColumn("zhuanggkssj").setHeader("装港开始时间");
		egu.getColumn("zhuanggkssj").setWidth(120);
		egu.getColumn("zhuanggkssj").setEditor(null);
		egu.getColumn("zhuanggkssj").setHidden(true);
		
		egu.getColumn("zhuanggjssj").setHeader("装港结束时间");
		egu.getColumn("zhuanggjssj").setWidth(120);
		egu.getColumn("zhuanggjssj").setEditor(null);
		egu.getColumn("zhuanggjssj").setHidden(true);
		
		egu.getColumn("gongysb_id").setDefaultValue(visit.getString1());
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("sanfsl").setDefaultValue(String.valueOf(visit.getDouble4()));
		egu.getColumn("sanfsl").setEditor(null);
		
//		egu.getColumn("fahrq").setDefaultValue(visit.getString9());
		egu.getColumn("fahrq").setEditor(null);
//		egu.getColumn("kaobrq").setDefaultValue(visit.getString12());
		egu.getColumn("kaobrq").setEditor(null);
		egu.getColumn("pinzb_id").setDefaultValue(visit.getString4());
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("hetb_id").setDefaultValue(visit.getString7());
		egu.getColumn("hetb_id").setEditor(null);

		if(count>=0){
			
			egu.getColumn("yundh").setDefaultValue(visit.getString13());
			
			egu.getColumn("luncxxb_id").setDefaultValue(visit.getString8());
			
			egu.getColumn("gongysb_id").setDefaultValue(visit.getString1());
			egu.getColumn("meikxxb_id").setDefaultValue(visit.getString3());
			
			egu.getColumn("yunsfsb_id").setDefaultValue(visit.getString11());
			egu.getColumn("chec").setDefaultValue(visit.getStringBuffer1().toString());
			
			egu.getColumn("faz_id").setDefaultValue(visit.getString5());
			egu.getColumn("daoz_id").setDefaultValue(visit.getString6());
			
			egu.getColumn("pinzb_id").setDefaultValue(visit.getString4());
			egu.getColumn("hetb_id").setDefaultValue(visit.getString7());
			
			
			egu.getColumn("maoz").setDefaultValue(String.valueOf(visit.getDouble1()));
			
			egu.getColumn("piz").setDefaultValue(String.valueOf(visit.getDouble2()));
			
			egu.getColumn("biaoz").setDefaultValue(String.valueOf(visit.getDouble3()));


		}

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		DateField datet = new DateField();
		egu.getColumn("fahrq").setDefaultValue(visit.getString9());//DateUtil.FormatDate(new Date())
		egu.getColumn("kaobrq").setEditor(datet);
		egu.getColumn("kaobrq").setDefaultValue(visit.getString12());//DateUtil.FormatDate(new Date())
		
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("xiemkssj").setEditor(datetime);
		egu.getColumn("xiemkssj").setDefaultValue(visit.getStringBuffer10().toString());//DateUtil.FormatDateTime(new Date())
		
		egu.getColumn("xiemjssj").setEditor(datetime);
		egu.getColumn("xiemjssj").setDefaultValue(visit.getString15());//DateUtil.FormatDateTime(new Date())
		
		egu.getColumn("zhuanggkssj").setEditor(datetime);
		egu.getColumn("zhuanggkssj").setDefaultValue(visit.getString16());//DateUtil.FormatDateTime(new Date())
		
		egu.getColumn("zhuanggjssj").setEditor(datetime);
		egu.getColumn("zhuanggjssj").setDefaultValue(visit.getString17());//DateUtil.FormatDateTime(new Date())
		
		egu.getColumn("xiemjssj").editor.allowBlank=true;
		egu.getColumn("xiemkssj").editor.allowBlank=true;
		egu.getColumn("zhuanggjssj").editor.allowBlank=true;
		egu.getColumn("zhuanggkssj").editor.allowBlank=true;
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue(visit.getString1());
		cb_gongysb.setEditable(true);
		String GongysSql="select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
		
		ComboBox c7 = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(c7);
		c7.setEditable(true);
		String yunsfsid = "select id,mingc from yunsfsb where mingc ='海运' order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsid));
		egu.getColumn("yunsfsb_id").setDefaultValue("海运");
		
		egu.getColumn("yunsfsb_id").setHidden(true);
		
		ComboBox c8 = new ComboBox();
		egu.getColumn("hetb_id").setEditor(c8);
		c8.setEditable(true);
		c8.allowBlank=true;
		String hetbid = "select id,hetbh from hetb where diancxxb_id="+visit.getDiancxxb_id()+" order by hetbh";
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
				new IDropDownModel(hetbid));
//		煤场id
		ComboBox c9 = new ComboBox();
		egu.getColumn("meicb_id").setEditor(c9);
//		egu.getColumn("meicb_id").setDefaultValue("");
		c9.setEditable(true);
		String MeikbSql="select id,mingc from meicb order by mingc";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId, new IDropDownModel(MeikbSql));
		egu.getColumn("meicb_id").setReturnId(true);
		//煤堆名称
		ComboBox c10 = new ComboBox();
		egu.getColumn("duowmc").setEditor(c10);
		egu.getColumn("duowmc").setDefaultValue("");
		c10.setEditable(true);
		String DuowmcSql=
				"SELECT ID, MINGC\n" +
						"  FROM DUOW\n" + 
						" WHERE MINGC IN\n" + 
						"       (SELECT MINGC\n" + 
						"          FROM DUOW\n" + 
						"        MINUS\n" + 
						"        SELECT DUOWMC\n" + 
						"          FROM FAHMCB\n" + 
						"        UNION (SELECT FB.DUOWMC\n" + 
						"                 FROM FAHB,\n" + 
						"                      ZHILB,\n" + 
						"                      LUNCXXB,\n" + 
						"                      CHEZXXB,\n" + 
						"                      GONGYSB,\n" + 
						"                      MEIKXXB,\n" + 
						"                      PINZB,\n" + 
						"                      HETB,\n" + 
						"                      HETYS,\n" + 
						"                      YUNSFSB,\n" + 
						"                      MEICB,\n" + 
						"                      FAHMCB FB\n" + 
						"                WHERE FAHB.LUNCXXB_ID = LUNCXXB.ID(+)\n" + 
						"                  AND FAHB.ZHILB_ID = ZHILB.ID(+)\n" + 
						"                  AND FAHB.FAZ_ID = CHEZXXB.ID(+)\n" + 
						"                  AND FAHB.MEIKXXB_ID = MEIKXXB.ID(+)\n" + 
						"                  AND FAHB.PINZB_ID = PINZB.ID(+)\n" + 
						"                  AND FAHB.HETB_ID = HETB.ID(+)\n" + 
						"                  AND FAHB.YUNSFSB_ID = YUNSFSB.ID(+)\n" + 
						"                  AND FAHB.GONGYSB_ID = GONGYSB.ID(+)\n" + 
						"                  AND YUNSFSB.MINGC = '海运'\n" + 
						"                  AND FAHB.JIESB_ID = 0\n" + 
						"                  AND FAHB.ID = "+fbid+"\n" + 
						"                  AND FB.MEICB_ID = MEICB.ID(+)\n" + 
						"                  AND FAHB.ID = FB.FAHB_ID\n" + 
						"                  AND TO_CHAR(FAHB.DAOHRQ, 'YYYY-MM-DD') >='"+beginriq+"' \n" + 
						"                  AND TO_CHAR(FAHB.DAOHRQ, 'YYYY-MM-DD') <= '"+endriq+"' \n" + 
						"\n" + 
						"               ))";


		egu.getColumn("duowmc").setComboEditor(egu.gridId, new IDropDownModel(DuowmcSql));
		
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("luncxxb_id").setEditor(c1);
		c1.setEditable(false);
		String luncid = "select id,mingc from luncxxb order by mingc";
		egu.getColumn("luncxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(luncid));
		
		ComboBox c2 = new ComboBox();
		egu.getColumn("faz_id").setEditor(c2);
		c2.setEditable(false);
		String faz = "select id,mingc from chezxxb where leib='港口' order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(faz));
		
		ComboBox c5 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c5);
		egu.getColumn("daoz_id").setDefaultValue("庄河港");
		c5.setEditable(false);
		String daoz = "SELECT cz.id,cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id AND dz.leib = '港口'";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daoz));
		
		ComboBox c3 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c3);
		c3.setEditable(false);
		//c3.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		
//		 设置品种下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c4);
		c4.setEditable(false);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		

		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		String sql_cond=
			"select mingc\n" +
			"  from xitxxb\n" + 
			" where diancxxb_id = 202\n" + 
			"   and zhi = '是'\n" + 
			"   and leib = '运算' and zhuangt = 1\n" + 
			"   and beiz = '使用'";

		ResultSetList rsl_cond = con.getResultSetList(sql_cond);
		String pan_cond="";
		double total=0;
		while(rsl_cond.next()){
			pan_cond=rsl_cond.getString("mingc");
			
		}
		if(pan_cond.equals("运单量")||pan_cond=="运单量"){
			
			total=visit.getDouble3();//visit.setDouble3
		}else if(pan_cond.equals("水尺量")||pan_cond=="水尺量"){
			total=visit.getDouble4();
		}else{//卸货量
			total=visit.getDouble1();
		}
		String condition= 
//			"var rsl = gridDiv_grid.getSelectionModel().getSelections();\n" +
			" var  total=0"+";" + //to
//			"var Mrcd = gridDiv_ds.getModifiedRecords();\n" +
//			"if(Mrcd.length>0){ for (i = 0; i < rsl.length; i ++) {\n" + 
//			"    if (rsl[i].get('MEIL') == '' || rsl[i].get('MEIL') == null) {\n" +
//			"        Ext.MessageBox.alert('提示信息','煤场煤堆的煤量总和要小于等于发货的数量');\n" + 
//			"    }\n" +
//			"    if (rsl[i].get('KAOBRQ') == '' || rsl[i].get('KAOBRQ') == null) {\n" +
//			"        Ext.MessageBox.alert('提示信息','靠泊日期不能为空');\n" + 
//			"    }\n" +
//			"    if (rsl[i].get('XIEMKSSJ') == '' || rsl[i].get('XIEMKSSJ') == null) {\n" +
//			"        Ext.MessageBox.alert('提示信息','卸煤开始时间不能为空');\n" + 
//			"    }\n" +
//			"    if (rsl[i].get('XIEMJSSJ') == '' || rsl[i].get('XIEMJSSJ') == null) {\n" +
//			"        Ext.MessageBox.alert('提示信息','卸煤结束时间不能为空');\n" + 
//			"    }\n" +
//			"    if (rsl[i].get('ZHUANGGKSSJ') == '' || rsl[i].get('ZHUANGGKSSJ') == null) {\n" +
//			"        Ext.MessageBox.alert('提示信息','装港开始时间不能为空');\n" + 
//			"    }\n" +
//			"    if (rsl[i].get('ZHUANGGJSSJ') == '' || rsl[i].get('ZHUANGGJSSJ') == null) {\n" +
//			"        Ext.MessageBox.alert('提示信息','装港结束时间不能为空');\n" + 
//			"    }\n" +
			
//			"    total += eval(rsl[i].get('FBML'),0);\n" + 
//			"}}\n " +
			"   var Mrcd = gridDiv_ds.getRange();\n" + //getModifiedRecords()
			"  for(i = 0; i< Mrcd.length; i++){\n" + 
			"  total += eval(Mrcd[i].get('FBML'),0);"+
//			"    if(typeof(gridDiv_save)=='function'){\n" + 
//			"    var revalue = gridDiv_save(Mrcd[i]);\n" + 
//			"    if(revalue=='return'){\n" + 
//			"    return;\n" + 
//			"    }else if(revalue=='continue'){continue;}\n" + 
//			"    }" +
			"  for(j = i+1; j< Mrcd.length; j++){\n" + 
			"  	if(Mrcd[i].get('DUOWMC')==Mrcd[j].get('DUOWMC')){\n" + 
			"  		Ext.MessageBox.alert('提示信息','煤堆名称重复，请重新选择！');\n" + 
			"  		return;\n" + 
			"  	}\n" + 
			"  }\n" + 
			"   if(Mrcd[i].get('MEICB_ID')!=''&& Mrcd[i].get('DUOWMC')!=''){\n" +
			" 	  	if(Mrcd[i].get('DUOWMC').indexOf(Mrcd[i].get('MEICB_ID').substring(0,1))<0){\n" +
			" 	  		Ext.MessageBox.alert('提示信息','第'+eval(i+1)+'行煤场名称与煤堆名称不匹配，请重新选择！');\n" +
			" 	  		return;\n" +
			" 	  	}\n" +
			" 	  }\n" +
			"}\n" + 
			"if(total > "+total+"){ Ext.MessageBox.alert('提示信息','各个煤场煤堆的煤量总和要小于等于水尺量');return;}\n" + 
			"";
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "Save1Button",condition);//,condition
		
		
//		GridButton gb = new GridButton("保存", "function(){ "
////				+ " var rec = gridDiv_sm.getSelected(); "
////				+ " if(rec != null){var id = rec.get('ID');"
////				+ " var Cobjid = document.getElementById('CHANGEID');"
////				+ " Cobjid.value = id;}"
////				+
////				"   var gridDivsave_history = '';\n" 
//				+
//				"   var rec = gridDiv_sm.getSelected();\n" + 
//				"    if(rec != null){\n" + 
//				"      var id = rec.get('ID');\n" + 
//				"       var Cobjid = document.getElementById('CHANGEID');\n" + 
//				"        Cobjid.value = id;\n" + 
//				"        }\n" + 
//				"   var Mrcd = gridDiv_ds.getModifiedRecords();\n" + 
//				"  for(i = 0; i< Mrcd.length; i++){\n" + 
//				"    if(typeof(gridDiv_save)=='function'){\n" + 
//				"    var revalue = gridDiv_save(Mrcd[i]);\n" + 
//				"    if(revalue=='return'){\n" + 
//				"    return;\n" + 
//				"    }else if(revalue=='continue'){continue;}\n" + 
//				"    }\n" + 
//				"    if(Mrcd[i].get('LUNCXXB_ID') == ''){\n" + 
//				"      Ext.MessageBox.alert('提示信息','字段 货船名称 不能为空');return;\n" + 
//				"    }\n" + 
//				"    if(Mrcd[i].get('GONGYSB_ID') == ''){\n" + 
//				"    Ext.MessageBox.alert('提示信息','字段 供应商名称 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('MEIKXXB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 煤矿名称 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('YUNSFSB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 运输方式 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('CHEC') == ''){Ext.MessageBox.alert('提示信息','字段 船次 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('FAZ_ID') == ''){Ext.MessageBox.alert('提示信息','字段 装船港 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('DAOZ_ID') == ''){Ext.MessageBox.alert('提示信息','字段 到船港 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('PINZB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 煤种 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('MEICB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 煤场名称 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('DUOWMC') == ''){Ext.MessageBox.alert('提示信息','字段 煤堆名称 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('MAOZ') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段卸货量(吨) 低于最小值 -100000000');return;\n" + 
//				"    }if( Mrcd[i].get('MAOZ') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 卸货量(吨) 高于最大值 100000000000');return;\n" + 
//				"    }if(Mrcd[i].get('MAOZ')!=0 && Mrcd[i].get('MAOZ') == ''){Ext.MessageBox.alert('提示信息','字段 卸货量(吨) 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('PIZ') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段皮重(吨) 低于最小值 -100000000');return;\n" + 
//				"    }if( Mrcd[i].get('PIZ') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 皮重(吨) 高于最大值 100000000000');return;\n" + 
//				"    }if(Mrcd[i].get('PIZ')!=0 && Mrcd[i].get('PIZ') == ''){Ext.MessageBox.alert('提示信息','字段 皮重(吨) 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('BIAOZ') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段运单量(吨) 低于最小值 -100000000');return;\n" + 
//				"    }if( Mrcd[i].get('BIAOZ') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 运单量(吨) 高于最大值 100000000000');return;\n" + 
//				"    }if(Mrcd[i].get('BIAOZ')!=0 && Mrcd[i].get('BIAOZ') == ''){Ext.MessageBox.alert('提示信息','字段 运单量(吨) 不能为空');return;\n" + 
//				"    }if(Mrcd[i].get('SANFSL') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段水尺量(吨) 低于最小值 -100000000');return;\n" + 
//				"    }if( Mrcd[i].get('SANFSL') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 水尺量(吨) 高于最大值 100000000000');return;\n" + 
//				"    }if(Mrcd[i].get('SANFSL')!=0 && Mrcd[i].get('SANFSL') == ''){Ext.MessageBox.alert('提示信息','字段 水尺量(吨) 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('FAHRQ') == ''){Ext.MessageBox.alert('提示信息','字段 承运日期 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('KAOBRQ') == ''){Ext.MessageBox.alert('提示信息','字段 靠泊日期 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('XIEMKSSJ') == ''){Ext.MessageBox.alert('提示信息','字段 卸煤开始时间 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('XIEMJSSJ') == ''){Ext.MessageBox.alert('提示信息','字段 卸煤结束时间 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('ZHUANGGKSSJ') == ''){Ext.MessageBox.alert('提示信息','字段 装港开始时间 不能为空');return;\n" + 
////				"    }if(Mrcd[i].get('ZHUANGGJSSJ') == ''){\n" + 
////				"    Ext.MessageBox.alert('提示信息','字段 装港结束时间 不能为空');return;\n" + 
//				"    }\n" + 
//				"    gridDivsave_history += '<result>' + '<sign>U</sign>' +\n" + 
//				"     '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
//				"    + '<YUNDH update=\"true\">' + Mrcd[i].get('YUNDH').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNDH>'\n" + 
//				"    + '<LUNCXXB_ID update=\"true\">' + Mrcd[i].get('LUNCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LUNCXXB_ID>'\n" + 
//				"    + '<GONGYSB_ID update=\"true\">' + Mrcd[i].get('GONGYSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</GONGYSB_ID>'\n" + 
//				"    + '<MEIKXXB_ID update=\"true\">' + Mrcd[i].get('MEIKXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKXXB_ID>'\n" + 
//				"    + '<YUNSFSB_ID update=\"true\">' + Mrcd[i].get('YUNSFSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNSFSB_ID>'\n" + 
//				"    + '<CHEC update=\"true\">' + Mrcd[i].get('CHEC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHEC>'\n" + 
//				"    + '<HETB_ID update=\"true\">' + Mrcd[i].get('HETB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HETB_ID>'\n" + 
//				"    + '<FAZ_ID update=\"true\">' + Mrcd[i].get('FAZ_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FAZ_ID>'\n" + 
//				"    + '<DAOZ_ID update=\"true\">' + Mrcd[i].get('DAOZ_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DAOZ_ID>'\n" + 
//				"    + '<PINZB_ID update=\"true\">' + Mrcd[i].get('PINZB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINZB_ID>'\n" + 
//				"    + '<DUNW update=\"true\">' + Mrcd[i].get('DUNW')+ '</DUNW>'\n" + 
////				"    + '<MEICB_ID update=\"true\">' + Mrcd[i].get('MEICB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEICB_ID>'\n" + 
////				"    + '<DUOWMC update=\"true\">' + Mrcd[i].get('DUOWMC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DUOWMC>'\n" + 
//				"    + '<MAOZ update=\"true\">' + Mrcd[i].get('MAOZ')+ '</MAOZ>'\n" + 
//				"    + '<PIZ update=\"true\">' + Mrcd[i].get('PIZ')+ '</PIZ>'\n" + 
//				"    + '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'\n" + 
//				"    + '<SANFSL update=\"true\">' + Mrcd[i].get('SANFSL')+ '</SANFSL>'\n" + 
////				"    + '<FAHRQ update=\"true\">' + ('object' != typeof(Mrcd[i].get('FAHRQ'))?Mrcd[i].get('FAHRQ'):Mrcd[i].get('FAHRQ').dateFormat('Y-m-d'))+ '</FAHRQ>'\n" + 
////				"    + '<KAOBRQ update=\"true\">' + ('object' != typeof(Mrcd[i].get('KAOBRQ'))?Mrcd[i].get('KAOBRQ'):Mrcd[i].get('KAOBRQ').dateFormat('Y-m-d'))+ '</KAOBRQ>'\n" + 
////				"    + '<XIEMKSSJ update=\"true\">' + Mrcd[i].get('XIEMKSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIEMKSSJ>'\n" + 
////				"    + '<XIEMJSSJ update=\"true\">' + Mrcd[i].get('XIEMJSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIEMJSSJ>'\n" + 
////				"    + '<ZHUANGGKSSJ update=\"true\">' + Mrcd[i].get('ZHUANGGKSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ZHUANGGKSSJ>'\n" + 
////				"    + '<ZHUANGGJSSJ update=\"true\">' + Mrcd[i].get('ZHUANGGJSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ZHUANGGJSSJ>'\n" + 
//				"     + '</result>' ;\n" + 
//				"     }\n" + 
//				"    if(gridDiv_history=='' && gridDivsave_history=='' && rec == null){\n" + 
//				"    Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n" + 
//				"\t\t\t}else{\n" + 
//				"\t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
//				"\t\t\t\tCobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" + 
//				"\t\t\t\tdocument.getElementById('SaveButton').click();\n" + 
//				"\t\t\t\tExt.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
//				"\t\t\t}\n" + 
//				"\t}"
//
//				+ " ");
//		egu.addTbarBtn(gb);
		GridButton bc = new GridButton("返回","function(){ " +
		" document.getElementById('ReturnButton').click();}");
        bc.setIcon(SysConstant.Btn_Icon_Return);
        egu.addTbarBtn(bc);
        egu.setDefaultsortable(false);
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
			visit.setActivePageName(this.getPageName().toString());
//			visit.setString1("");	//传合同id	
			setRiqi(null);
			setRiq2(null);


			getSelectData();
		}
		visit.getStringBuffer20();
	}
}
