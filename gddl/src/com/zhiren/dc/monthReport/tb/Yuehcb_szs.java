package com.zhiren.dc.monthReport.tb;

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
 * 作者：lip
 * 日期：2011-12-20
 * 描述：修改select语句，每月首次做月报时，把上月累计值带入界面参与计算
 */
/*
 * 作者：夏峥
 * 日期：2011-12-20
 * 描述：取消全部界面自动刷新功能，用户需手动点击刷新按钮才可刷新数据
 */
/*
 * 作者：夏峥
 * 日期：2011-12-20
 * 描述：取消计算按钮且总计行和期初库存不能被编辑。
 */
/*
 * 作者：夏峥
 * 日期：2011-12-22
 * 描述：重写保存按钮
 */
/*
 * 作者：夏峥
 * 日期：2011-12-27
 * 描述：调整界面中的计算方法（库存累计和本月应保持一致）
 */
/*
 * 作者：夏峥
 * 日期：2012-01-10
 * 描述：耗存界面中保存时如果数据不符合规则，提示相差数量信息
 * 		修改前台界面中库存计算方法，自动计算所有行数据的库存信息
 * 		调整耗存界面计算公式，只计算本月数据信息。
 * 		
 */
/*
 * 作者：LIP
 * 日期：2012-03-14
 * 描述：因新需求数据存储内容有变，同名称出现两次，改变SQL关联条件
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
 *				可编辑列入最小值限制,最小值大于0.
 */
public class Yuehcb_szs extends BasePage implements PageValidateListener {
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
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");
		while (rsl.next()) {
			if(-1!=rsl.getDouble("YUETJKJB_ID")){
				if ("".equals(rsl.getString("id"))) {
					sql.append(
							"insert into yuehcb(id,fenx,yuetjkjb_id,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,jitcs,kuc)\n" +
							"values(\n" + 
							rsl.getString("yid")+"\n," +
							"'" + rsl.getString("fenx")+"'\n," +
							rsl.getString("yuetjkjb_id")+"\n," +
							rsl.getDouble("qickc")+"\n," +
							rsl.getDouble("shouml")+"\n," +
							rsl.getDouble("fady")+"\n," +
							rsl.getDouble("gongry")+"\n," +
							rsl.getDouble("qith")+"\n," +
							rsl.getDouble("sunh")+"\n," +
							rsl.getDouble("diaocl")+"\n," +
							rsl.getDouble("panyk")+"\n," +
							rsl.getDouble("shuifctz")+"\n," +
							rsl.getDouble("jitcs")+"\n," +
							rsl.getDouble("kuc")+"\n" +
							");\n"
					);
				} else {
					sql.append(
							"update yuehcb set \n" + 
							"qickc = " + rsl.getDouble("qickc") + ",\n" + 
							"shouml = " + rsl.getDouble("shouml") + ",\n" + 
							"fady = " + rsl.getDouble("fady") + ",\n" + 
							"gongry = " + rsl.getDouble("gongry") + ",\n" + 
							"qith = " + rsl.getDouble("qith") + ",\n" + 
							"sunh = " + rsl.getDouble("sunh") + ",\n" + 
							"diaocl = " + rsl.getDouble("diaocl") + ",\n" + 
							"panyk = " + rsl.getDouble("panyk") + ",\n" + 
							"shuifctz = " + rsl.getDouble("shuifctz") + ",\n" + 
							"jitcs = " + rsl.getDouble("jitcs") + ",\n" + 
							"kuc = " + rsl.getDouble("kuc") + "\n" + 
							"where id=" + rsl.getString("id") + ";\n"
					);
				}
			}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if(flag!=-1){
			rsl.beforefirst();
			sql.setLength(0);
			while(rsl.next()){
				if(-1!=rsl.getDouble("YUETJKJB_ID")){
					if("累计".equals(rsl.getString("fenx")) && getIsSelectLike()){
						
						String tmpsql = "";
						double bq_qickc = 0;
						double bq_kc = 0;
						{
							tmpsql = "select h.qickc,h.kuc from yuehcb h,yuetjkjb y\n" +
									 " where h.fenx='本月'\n" + 
									 "   and h.yuetjkjb_id=y.id\n" +
									 "   and to_char(y.riq,'yyyy')='"+getNianf()+"'\n" + 
									 "	 and y.riq=to_date('"+ getNianf() + "-" + getYuef() + "-01" + "','yyyy-mm-dd')\n" +
									 "	 and y.id=" + rsl.getString("yuetjkjb_id");
							ResultSetList tmprsl = con.getResultSetList(tmpsql);
							tmprsl.next();
							bq_qickc = tmprsl.getDouble("qickc");
							bq_kc = tmprsl.getDouble("kuc");
						}
						
						String sq = 
							"select sum(qickc) qickc,sum(shouml) shouml,sum(fady) fady,sum(gongry) gongry,sum(qith) qith,\n" +
							"          sum(sunh) sunh,sum(diaocl) diaocl,sum(panyk) panyk,sum(shuifctz) shuifctz,sum(jitcs) jitcs,0 kuc\n" + 
							"  from yuehcb h,yuetjkjb yt,(select gongysb_id,jihkjb_iD,pinzb_id,yunsfsb_id from yuetjkjb where id= " + rsl.getString("YUETJKJB_ID") + ")yt2\n" + 
							" where h.yuetjkjb_id=yt.id\n" + 
							"	and yt.gongysb_id=yt2.gongysb_id\n" +
							"	and yt.jihkjb_id=yt2.jihkjb_id\n" +
							"	and yt.pinzb_id=yt2.pinzb_id\n" +
							"	and yt.yunsfsb_id=yt2.yunsfsb_id\n" +
							"   and yt.riq>=to_date('"+ getNianf() + "-01-01" + "','yyyy-mm-dd')\n" + 
							"   and yt.riq<=to_date('"+ getNianf() + "-" + getYuef() + "-01" + "','yyyy-mm-dd')\n" + 
							"   and h.fenx='" + SysConstant.Fenx_Beny + "'\n" +
							"   and yt.diancxxb_id=" + getTreeid();
						ResultSetList rs = con.getResultSetList(sq);
						rs.next();
						sql.append(
								"update yuehcb set \n" + 
								"qickc = " + bq_qickc + ",\n" + 
								"shouml = " + rs.getDouble("shouml") + ",\n" + 
								"fady = " + rs.getDouble("fady") + ",\n" + 
								"gongry = " + rs.getDouble("gongry") + ",\n" + 
								"qith = " + rs.getDouble("qith") + ",\n" + 
								"sunh = " + rs.getDouble("sunh") + ",\n" + 
								"diaocl = " + rs.getDouble("diaocl") + ",\n" + 
								"panyk = " + rs.getDouble("panyk") + ",\n" + 
								"shuifctz = " + rs.getDouble("shuifctz") + ",\n" + 
								"jitcs = " + rs.getDouble("jitcs") + ",\n" + 
								"kuc = " + bq_kc + "\n" + 
								"where id=" + rsl.getString("yid") + ";\n"
						);
					}
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
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
		}
		
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		setRiq();
	   getSelectData();
	}
	

	public void DelData() {
			String diancxxb_id = getTreeid();
			JDBCcon con = new JDBCcon();
			String CurrZnDate=getNianf()+"年"+getYuef()+"月";
			String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
			String strSql=
				"delete from yuehcb where yuetjkjb_id in (select id from yuetjkjb where riq="
				+CurrODate+" and diancxxb_id="+diancxxb_id+")";
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
	private boolean getZhangt(JDBCcon con){
		String CurrODate =  DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from yuehcb s, yuetjkjb k\n" + 
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
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql="";
		strSql = "select id from yueshchjb where riq = "+CurrODate+  "and diancxxb_id="+diancxxb_id;
		boolean isLocked = !con.getHasIt(strSql);

		strSql = 
			"select * from (select id,null yid,-1 yuetjkjb_id,'总计' as gmingc,'-' jmingc,'-' pmingc,'-' ymingc, fenx,\n" +
			"       qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,jitcs,kuc\n" + 
			"       from yueshchjb\n" + 
			"where riq ="+CurrODate+" and diancxxb_id = " + diancxxb_id + " order by fenx)\n" + 
			" union all\n" + 
			" select * from(\n" + 
			" select y.id,decode(y.id,null,getnewid(" + diancxxb_id + "),y.id) yid,s.yuetjkjb_id,s.gmingc,s.jmingc,s.pmingc,s.ymingc,s.fenx,\n" + 
			"           decode(y.qickc,null,s.kuc,0,s.kuc,y.qickc) qickc,s.shouml,\n" + 

			"           decode(y.fady,null,decode(s.fenx,'本月',0,s.fady),y.fady) fady,\n" + 
			"           decode(y.gongry,null,decode(s.fenx,'本月',0,s.gongry),y.gongry) gongry,\n" + 
			"           decode(y.qith,null,decode(s.fenx,'本月',0,s.qith),y.qith) qith,\n" + 
			"           decode(y.sunh,null,decode(s.fenx,'本月',0,s.sunh),y.sunh) sunh,\n" + 
			"           decode(y.diaocl,null,decode(s.fenx,'本月',0,s.diaocl),y.diaocl) diaocl,\n" + 
			"           decode(y.panyk,null,decode(s.fenx,'本月',0,s.panyk),y.panyk) panyk,\n" + 
			"           decode(y.shuifctz,null,decode(s.fenx,'本月',0,s.shuifctz),y.shuifctz) shuifctz,\n" + 
			"           decode(y.jitcs,null,decode(s.fenx,'本月',0,s.jitcs),y.jitcs) jitcs,\n" + 
			"           decode(y.kuc,null,decode(s.fenx,'本月',0,s.kuc),y.kuc) kuc" +
			"  from (\n" + 
			"      SELECT g.id,j.id,p.id,yfs.id, x.fenx,y.id yuetjkjb_id,g.mingc gmingc,j.mingc jmingc,p.mingc pmingc,yfs.mingc ymingc,\n" +
			"		 nvl(k.kuc,0)kuc,nvl(ysl.jingz,0) shouml,nvl(k1.fady,0)fady,\n" +
			"		 nvl(k1.gongry,0)gongry,nvl(k1.qith,0)qith,nvl(k1.sunh,0)sunh,\n" +
			"			nvl(k1.diaocl,0)diaocl,nvl(k1.panyk,0)panyk,\n" +
			"			nvl(k1.shuifctz,0)shuifctz,nvl(k1.jitcs,0)jitcs" + 
			"        from yuetjkjb y,yueslb ysl\n" + 
			"             ,(select decode(0,0,'本月') fenx from dual\n" + 
			"              union\n" + 
			"              select decode(0,0,'累计') fenx from dual) x,\n" + 
			"             gongysb g,\n" + 
			"             jihkjb j,\n" + 
			"             pinzb p,\n" + 
			"             yunsfsb yfs,\n" + 
			"             (\n" + 
			"             select h.fenx,kuc,y.gongysb_id,y.jihkjb_id,y.pinzb_id,y.yunsfsb_id\n" + 
			"               from yuehcb h, yuetjkjb y\n" + 
			"              where h.yuetjkjb_id = y.id\n" + 
			"                and h.fenx = '本月'\n" + 
			"                and y.diancxxb_id=" + diancxxb_id + "\n" + 
			"                and y.riq=add_months("+CurrODate+", -1)\n" + 
			//"                and to_char(y.riq,'yyyy')="+getNianf()+"\n" + 
			"             )k,\n" + 
			"             (\n" + 
			"             select h.fenx,kuc,y.gongysb_id,y.jihkjb_id,y.pinzb_id,y.yunsfsb_id,h.fady,h.gongry,h.qith,h.sunh,h.diaocl,h.panyk,h.shuifctz,h.jitcs\n" + 
			"               from yuehcb h, yuetjkjb y\n" + 
			"              where h.yuetjkjb_id = y.id\n" + 
			"                and h.fenx = '累计'\n" + 
			"                and y.diancxxb_id=" + diancxxb_id + "\n" + 
			"                and y.riq=add_months("+CurrODate+", -1)\n" + 
			//"                and to_char(y.riq,'yyyy')="+getNianf()+"\n" + 
			"             )k1\n" + 
			"       where y.riq = "+CurrODate+"\n" + 
			"         and y.diancxxb_id=" + diancxxb_id + "\n" + 
			"         and y.gongysb_id=g.id\n" + 
			"         and y.jihkjb_id=j.id\n" + 
			"         and y.pinzb_id=p.id\n" + 
			"         and y.yunsfsb_id=yfs.id\n" + 
			"         and y.id=ysl.yuetjkjb_id\n" + 
			"         and x.fenx=ysl.fenx\n" + 
			"\n" + 
			"         and y.gongysb_id=k.gongysb_id(+)\n" + 
			"         and y.jihkjb_id=k.jihkjb_id(+)\n" + 
			"         and y.pinzb_id=k.pinzb_id(+)\n" + 
			"         and y.yunsfsb_id=k.yunsfsb_id(+)\n" + 
			"\n" + 
			"         and y.gongysb_id=k1.gongysb_id(+)\n" + 
			"         and y.jihkjb_id=k1.jihkjb_id(+)\n" + 
			"         and y.pinzb_id=k1.pinzb_id(+)\n" + 
			"         and y.yunsfsb_id=k1.yunsfsb_id(+)\n" + 
			"      ) s,\n" + 
			"      yuehcb y\n" + 
			"      where y.yuetjkjb_id(+)=s.yuetjkjb_id\n" + 
			"        and y.fenx(+)=s.fenx\n" + 
			"      order by s.yuetjkjb_id,s.fenx\n" + 
			"      )\n" + 
			"";

  		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		// //设置表名称用于保存
		egu.setTableName("yuehcb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("yid").setHeader("yid");
		egu.getColumn("yid").setWidth(120);
		egu.getColumn("yid").setEditor(null);
		egu.getColumn("yid").setHidden(true);
		egu.getColumn("gmingc").setHeader("供货单位");
		egu.getColumn("gmingc").setWidth(120);
		egu.getColumn("GMINGC").setEditor(null);
		egu.getColumn("GMINGC").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jmingc").setHeader("计划口径");
		egu.getColumn("jmingc").setWidth(60);
		egu.getColumn("jmingc").setEditor(null);
		egu.getColumn("jmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pmingc").setHeader("品种");
		egu.getColumn("pmingc").setWidth(45);
		egu.getColumn("pmingc").setEditor(null);
		egu.getColumn("pmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("ymingc").setHeader("运输方式");
		egu.getColumn("ymingc").setWidth(60);
		egu.getColumn("ymingc").setEditor(null);
		egu.getColumn("ymingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("qickc").setHeader("期初库存");
		egu.getColumn("qickc").setWidth(70);
//		egu.getColumn("qickc").setEditor(null);
		
		egu.getColumn("shouml").setHeader("收煤量");
		egu.getColumn("shouml").setWidth(70);
		((NumberField)egu.getColumn("shouml").editor).setDecimalPrecision(0);
		egu.getColumn("shouml").setEditor(null);
		egu.getColumn("fady").setHeader("发电耗");
		egu.getColumn("fady").setWidth(70);
		egu.getColumn("fady").editor.setMinValue("0");
		
		egu.getColumn("gongry").setHeader("供热耗");
		egu.getColumn("gongry").setWidth(70);
		egu.getColumn("gongry").editor.setMinValue("0");
		
		egu.getColumn("qith").setHeader("其它耗");
		egu.getColumn("qith").setWidth(70);
		egu.getColumn("qith").editor.setMinValue("0");
		
		egu.getColumn("sunh").setHeader("实际储损");
		egu.getColumn("sunh").setWidth(60);
		egu.getColumn("sunh").editor.setMinValue("0");
		
		egu.getColumn("diaocl").setHeader("调出量");
		egu.getColumn("diaocl").setWidth(60);	
		egu.getColumn("panyk").setHeader("盘盈亏");
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("shuifctz").setHeader("水分差调整");
		egu.getColumn("shuifctz").setWidth(60);
		egu.getColumn("jitcs").setHeader("计提储损");
		egu.getColumn("jitcs").setWidth(60);
		egu.getColumn("jitcs").setHidden(true);
		egu.getColumn("kuc").setHeader("库存");
		egu.getColumn("kuc").setWidth(60);
		egu.getColumn("kuc").setEditor(null);
		String Sql="select zhi from xitxxb x where x.leib='月报' and x.danw='耗存' and beiz='使用'";
		ResultSetList rs = con.getResultSetList(Sql);
		
		while (rs.next()){
			String zhi = rs.getString("zhi");
			if(egu.getColByHeader(zhi)!=null){
				egu.getColByHeader(zhi).hidden=true;
			}
		}
		
		egu.setDefaultsortable(false);  
		// /设置按钮
		StringBuffer sb = new StringBuffer();	
		sb.append("\ngridDiv_grid.on('afteredit',function(e){");
		sb.append("CountAllKuc(gridDiv_ds);});\n");
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('YUETJKJB_ID')=='-1'){e.cancel=true;}");//当统计口径的值是"-1"时,这一行不允许编辑
		sb.append(" if(e.field=='QICKC'){ e.cancel=true;}");//期初库存不允许编辑
		sb.append("});");
		
		 //设定合计列不保存
//		sb.append("function gridDiv_save(record){if(record.get('gongysb_id')=='总计') return 'continue';}");
		
		egu.addOtherScript(sb.toString());
		
		egu.addTbarText("年份");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
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
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if(getZhangt(con)){
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		}else{
			
//			保存按钮
			String save_script=
				"function(){\n"+
				" var gridDivsave_history = '';var Mrcd = gridDiv_ds.getRange();\n"+
				"if(validateHy(gridDiv_ds)){return;};\n"+
				"for(i = 0; i< Mrcd.length; i++){\n"+
				"if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n"+
				"gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n"+
				"+ '<YID update=\"true\">' + Mrcd[i].get('YID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YID>'\n"+
				"+ '<YUETJKJB_ID update=\"true\">' + Mrcd[i].get('YUETJKJB_ID')+ '</YUETJKJB_ID>'\n"+
				"+ '<GMINGC update=\"true\">' + Mrcd[i].get('GMINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</GMINGC>'\n"+
				"+ '<JMINGC update=\"true\">' + Mrcd[i].get('JMINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JMINGC>'\n"+
				"+ '<PMINGC update=\"true\">' + Mrcd[i].get('PMINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PMINGC>'\n"+
				"+ '<YMINGC update=\"true\">' + Mrcd[i].get('YMINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YMINGC>'\n"+
				"+ '<FENX update=\"true\">' + Mrcd[i].get('FENX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FENX>'\n"+
				"+ '<QICKC update=\"true\">' + Mrcd[i].get('QICKC')+ '</QICKC>'\n"+
				"+ '<SHOUML update=\"true\">' + Mrcd[i].get('SHOUML')+ '</SHOUML>'\n"+
				"+ '<FADY update=\"true\">' + Mrcd[i].get('FADY')+ '</FADY>'\n"+
				"+ '<GONGRY update=\"true\">' + Mrcd[i].get('GONGRY')+ '</GONGRY>'\n"+
				"+ '<QITH update=\"true\">' + Mrcd[i].get('QITH')+ '</QITH>'\n"+
				"+ '<SUNH update=\"true\">' + Mrcd[i].get('SUNH')+ '</SUNH>'\n"+
				"+ '<DIAOCL update=\"true\">' + Mrcd[i].get('DIAOCL')+ '</DIAOCL>'\n"+
				"+ '<PANYK update=\"true\">' + Mrcd[i].get('PANYK')+ '</PANYK>'\n"+
				"+ '<SHUIFCTZ update=\"true\">' + Mrcd[i].get('SHUIFCTZ')+ '</SHUIFCTZ>'\n"+
				"+ '<JITCS update=\"true\">' + Mrcd[i].get('JITCS')+ '</JITCS>'\n"+
				"+ '<KUC update=\"true\">' + Mrcd[i].get('KUC')+ '</KUC>'\n"+
				" + '</result>' ; }\n"+
				"if(gridDiv_history=='' && gridDivsave_history==''){\n"+ 
				"Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n"+
				"}else{\n"+
				"var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';document.getElementById('SaveButton').click();Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"+
				"}\n"+
				"}";
//			删除
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setDisabled(isLocked);
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
			//保存
			GridButton gbs = new GridButton("保存",save_script) ;
			gbs.setIcon(SysConstant.Btn_Icon_Save);
			gbs.setDisabled(isLocked);
			egu.addTbarBtn(gbs);
			
			Checkbox cb=new Checkbox();
			cb.setId("SelectLike");
			cb.setListeners("check:function(own,checked){if(checked){document.all.SelectLike.value='true'}else{document.all.SelectLike.value='false'}}");
			egu.addToolbarItem(cb.getScript());
			egu.addTbarText("是否自动计算累计值");
		}

		
		sb.setLength(0);
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='SUNH'){\n" + 
				"    var sunh=0;\n" + 
				"    var i = e.row;\n" + 
				"    sunh = gridDiv_ds.getAt(i).get('SUNH');\n" + 
				"	 gridDiv_ds.getAt(i).set('JITCS',sunh);" +
				"  }\n" + 
				"});"
		);
		setExtGrid(egu);
		con.Close();
	}
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf()+"年"+getYuef()+"月";
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
			//getShoumlDy();
			setRiq();
			setTreeid(null);
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
