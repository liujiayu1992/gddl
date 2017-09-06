package com.zhiren.dc.haiyydlr;

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

public class Haiyydlr extends BasePage implements PageValidateListener {
	
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
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
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
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			//质量表
			String zhil ="delete from zhilb " +
			"where id="+rsl.getString("zhilb_id")+";\n";
			sb.append(zhil.toString());
			//采样表
			String caiy ="delete from caiyb " +
			"where zhilb_id="+rsl.getString("zhilb_id")+";\n";
			sb.append(caiy.toString());
			//质量临时表
			String dd ="delete from zhillsb " +
			"where id="+rsl.getString("zhilb_id")+";\n";
			sb.append(dd.toString());
			//发货表
			String cc ="delete from fahb " +
			"where id="+rsl.getString("id")+";\n";
			sb.append(cc.toString());
			
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("没有需要保存的记录！");
			return;
		}
		String jihkjb_id="0";
		String jihkjsql="select id from jihkjb where mingc ='无'";
		if (con.getHasIt(jihkjsql)){
			jihkjb_id="(select id from jihkjb where mingc ='无')";
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
			
			String fahrq="to_date('"+rsl.getString("fahrq")+"','yyyy-mm-dd')";
			String daohrq=endriq;
			String yunsfsb_id=rsl.getString("yunsfsb_id");
			String kaobrq="to_date('"+rsl.getString("kaobrq")+"','yyyy-mm-dd')";
			String yundh=rsl.getString("yundh");
			String xiemkssj="to_date('"+rsl.getString("xiemkssj")+"','yyyy-mm-dd hh24:mi:ss')";
			String xiemjssj="to_date('"+rsl.getString("xiemjssj")+"','yyyy-mm-dd hh24:mi:ss')";
			String zhuanggkssj="to_date('"+rsl.getString("zhuanggkssj")+"','yyyy-mm-dd hh24:mi:ss')";
			String zhuanggjssj="to_date('"+rsl.getString("zhuanggjssj")+"','yyyy-mm-dd hh24:mi:ss')";
			
			String chec=rsl.getString("chec");
			double maoz=rsl.getDouble("maoz");
			double piz=rsl.getDouble("piz");
			double biaoz=rsl.getDouble("biaoz");
			double jingz=maoz-piz-biaoz;
			double yingd=0;
			double yingk=0;
			double yuns=0;
			if ((maoz-piz-biaoz)<0) {
				if ((maoz-piz-biaoz)>(biaoz*0.012)){
					yuns=(biaoz*0.012);
					yingk=(maoz-piz-biaoz)-(biaoz*0.012);
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
			double sanfsl=0;
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
				
				//zhuanmb
//				//采样编码
//				sb.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id,zidybm) values (");
//				sb.append("getnewid("+diancxxb_id+"),").append(zhillsb_id).append(",");
//				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
//				sb.append("(select id from zhuanmlb where jib=1),'');\n");
//				//制样编码
//				sb.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id,zidybm) values (");
//				sb.append("getnewid("+diancxxb_id+"),").append(zhillsb_id).append(",");
//				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
//				sb.append("(select id from zhuanmlb where jib=2),'');\n");
//				//化验编码
//				sb.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id,zidybm) values (");
//				sb.append("getnewid("+diancxxb_id+"),").append(zhillsb_id).append(",");
//				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
//				sb.append("(select id from zhuanmlb where jib=3),'');\n");
				
//				//插入质量表
				//String zhilbid = MainGlobal.getNewID(diancxxb_id);
//				sb.append("insert into zhilb(id,huaybh,caiyb_id,huaysj)values(\n");
//				sb.append(zhillsb_id);
//				sb.append(",'").append(huaybh).append("',0");
//				sb.append(",sysdate);\n");
//				
				// 插入发货表
				
				String id =MainGlobal.getNewID(diancxxb_id);
				
				sb.append("insert into fahb (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, \n");
				sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, hetb_id,  \n");
				sb.append("zhilb_id, jiesb_id, yunsfsb_id, chec, maoz, piz, jingz, biaoz,  \n");
				sb.append("yingd, yingk, yuns, yunsl, koud, kous, kouz, koum, zongkd,  \n");
				sb.append("sanfsl, ches, tiaozbz, yansbhb_id, lie_id, yuandz_id,  \n");
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
				sb.append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(yunsfsb_id)).append(",");
				sb.append(chec).append(",").append(maoz).append(",").append(piz).append(",");
				sb.append(jingz).append(",").append(biaoz).append(",").append(yingd).append(",");
				sb.append(yingk).append(",").append(yuns).append(",").append(yunsl).append(",");
				sb.append(koud).append(",").append(kous).append(",").append(kouz).append(",");
				sb.append(koum).append(",").append(zongkd).append(",").append(sanfsl).append(",");
				sb.append(ches).append(",").append(tiaozbz).append(",").append(yansbhb_id).append(",");
				sb.append(id).append(",").append(yuandz_id).append(",").append(yuanshdwb_id).append(",");
				sb.append(kuangfzlb_id).append(",").append(liucb_id).append(",").append(liucztb_id).append(",").append(hedbz).append(",'");
				sb.append(beiz).append("',");
				sb.append((getExtGrid().getColumn("luncxxb_id").combo).getBeanId(luncxxb_id)).append(",");
				sb.append(kaobrq).append(",'").append(yundh).append("',").append(xiemkssj).append(",");
				sb.append(xiemjssj).append(",").append(zhuanggkssj).append(",").append(zhuanggjssj).append(");\n");
				
			}else{
				sb.append("update fahb set meikxxb_id=");
				sb.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(meikxxb_id));
				sb.append(",gongysb_id=");
				sb.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(gongysb_id));
				sb.append(",luncxxb_id=");
				sb.append((getExtGrid().getColumn("luncxxb_id").combo).getBeanId(luncxxb_id));
				sb.append(",pinzb_id=");
				sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(pinzb_id));
				sb.append(",faz_id=");
				sb.append((getExtGrid().getColumn("faz_id").combo).getBeanId(faz_id));
				sb.append(",hetb_id=");
				sb.append((getExtGrid().getColumn("hetb_id").combo).getBeanId(hetb_id));
				sb.append(",chec=");
				sb.append(chec);
				sb.append(",maoz=").append(maoz);
				sb.append(",piz=").append(piz);
				sb.append(",jingz=").append(jingz);
				sb.append(",biaoz=").append(biaoz);
				sb.append(",yundh='").append(yundh);
				sb.append("',xiemkssj=").append(xiemkssj);
				sb.append(",xiemjssj=").append(xiemjssj);
				sb.append(",zhuanggkssj=").append(zhuanggkssj);
				sb.append(",zhuanggjssj=").append(zhuanggjssj);
				sb.append(" where fahb.id=").append(rsl.getString("id")).append(";\n");
 				
//				sb.append("update zhilb set zhilb.huaybh='");
//				sb.append(huaybh).append("where zhilb.id=(");
//				sb.append("select zhilb_id from fahb where fahb.id=");
//				sb.append(rsl.getString("id")).append(");\n");
//				
//				sb.append("update chepb set cheph='");
//				sb.append(huaybh).append("' where chepb.fahb_id=");
//				sb.append(rsl.getString("id")).append(";\n");
				
			}
			
		}
		sb.append("end;");
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

	public void getSelectData() {
		Visit visit = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
		
		String beginriq=getRiqi();
		String endriq=getRiq2();
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select fahb.id,fahb.yundh,luncxxb.mingc as luncxxb_id, \n");
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
		sbsql.append("       fahb.maoz,fahb.piz,fahb.biaoz,\n");//fahb.jingz,
		//sbsql.append("       fahb.maoz, \n");
		//sbsql.append("   to_char(daohrq,'YYYY-MM-DD hh24:mi:ss')daohrq, \n");
		sbsql.append("   fahrq, \n");// 
		sbsql.append("   kaobrq, \n");
		sbsql.append("   to_char(xiemkssj,'YYYY-MM-DD hh24:mi:ss')xiemkssj, \n");
		sbsql.append("   to_char(xiemjssj,'YYYY-MM-DD hh24:mi:ss')xiemjssj, \n");
		sbsql.append("   to_char(zhuanggkssj,'YYYY-MM-DD hh24:mi:ss')zhuanggkssj, \n");
		sbsql.append("   to_char(zhuanggjssj,'YYYY-MM-DD hh24:mi:ss')zhuanggjssj \n");
//		sbsql.append("   luncxxb.mingc as huocmc, \n");
		sbsql.append("  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb \n");
		sbsql.append(" where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+) \n");
		sbsql.append("   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+) \n");
		sbsql.append("   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)  \n");
		sbsql.append("   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)  \n");
		sbsql.append("   and yunsfsb.mingc='海运' and fahb.jiesb_id=0 \n");
		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+beginriq+"'  \n");
		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+endriq+"'  \n");
		sbsql.append("order by fahb.daohrq \n");
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("yundh").setHeader("运单号");
		egu.getColumn("yundh").setWidth(70);
		egu.getColumn("luncxxb_id").setHeader("货船名称");
		egu.getColumn("luncxxb_id").setWidth(70);
		egu.getColumn("gongysb_id").setHeader("供应商名称");
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("chec").setHeader("船次");
		egu.getColumn("chec").setWidth(40);
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
		egu.getColumn("dunw").setHeader("停靠泊位");
		egu.getColumn("dunw").setWidth(70);
		egu.getColumn("maoz").setHeader("卸货量(吨)");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setHeader("皮重(吨)");
		egu.getColumn("piz").setHidden(true);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setDefaultValue("0");
//		egu.getColumn("jingz").setHeader("净重(吨)");
//		egu.getColumn("jingz").setEditor(null);
//		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("biaoz").setHeader("运单量(吨)");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("fahrq").setHeader("承运日期");
		egu.getColumn("fahrq").setWidth(90);
		egu.getColumn("kaobrq").setHeader("靠泊日期");
		egu.getColumn("kaobrq").setWidth(90);
		egu.getColumn("xiemkssj").setHeader("卸煤开始时间");
		egu.getColumn("xiemkssj").setWidth(120);
		egu.getColumn("xiemjssj").setHeader("卸煤结束时间");
		egu.getColumn("xiemjssj").setWidth(120);
		egu.getColumn("zhuanggkssj").setHeader("装港开始时间");
		egu.getColumn("zhuanggkssj").setWidth(120);
		egu.getColumn("zhuanggjssj").setHeader("装港结束时间");
		egu.getColumn("zhuanggjssj").setWidth(120);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		egu.getColumn("fahrq").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("kaobrq").setDefaultValue(DateUtil.FormatDate(new Date()));
		
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("xiemkssj").setEditor(datetime);
		egu.getColumn("xiemkssj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		
		egu.getColumn("xiemjssj").setEditor(datetime);
		egu.getColumn("xiemjssj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		
		egu.getColumn("zhuanggkssj").setEditor(datetime);
		egu.getColumn("zhuanggkssj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		
		egu.getColumn("zhuanggjssj").setEditor(datetime);
		egu.getColumn("zhuanggjssj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql="select id,mingc from gongysb   where leix =1  order by mingc";
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
		String hetbid = "select id,hetbh from hetb where diancxxb_id="+visit.getDiancxxb_id()+" order by hetbh";
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
				new IDropDownModel(hetbid));
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("luncxxb_id").setEditor(c1);
		c1.setEditable(true);
		String luncid = "select id,mingc from luncxxb order by mingc";
		egu.getColumn("luncxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(luncid));
		
		ComboBox c2 = new ComboBox();
		egu.getColumn("faz_id").setEditor(c2);
		c2.setEditable(true);
		String faz = "select id,mingc from chezxxb where leib='港口' order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(faz));
		
		ComboBox c5 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c5);
		c5.setEditable(true);
		String daoz = "SELECT cz.id,cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id AND dz.leib = '港口'";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daoz));
		
		ComboBox c3 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c3);
		c3.setEditable(true);
		//c3.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		
//		 设置品种下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c4);
		c4.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		
//		到货日期查询
		egu.addTbarText("离泊日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
		
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
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
			visit.setString1("");	//传合同id	
//			setRiqi(DateUtil.FormatDate(new Date()));
//			setRiq2(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
	}
}
