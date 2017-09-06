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
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-11-06
 * 描述：月报收煤量增加新的计算方法 
 * 		增加参数：insert into xitxxb values(getnewid(229),1,2291,'月报耗用收煤量定义','票重+盈吨-亏吨-运损','','月报',1,'')
 * 		一厂多制diancxxb_id需分厂ID
 */
/*
 * 作者：王磊
 * 时间：2009-07-30 14：49
 * 描述：增加数量月报取数的日期设置 
 * insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * values(getnewid(232),1,232,'月报取数日期差','-2','','月报',1,'')
 */
/*
 * 修改人：licj
 * 修改时间：2010-10-29
 * 描述：水分差调整也计入库存计算，生成数据时水分差调整会自动计算，yueshchjb新加JITCS(计提储损)、RUNXCS(允许储损) 计提储损计入库存计算
 * 		允许储损=月度日均库存煤量*0.5%（从shouhcrbb里取数）
 * 		计提储损 从月度盘点相关表中取数
 * 
 */
/*
 * 作者：夏峥
 * 时间：2011-06-20
 * 描述：增加电厂树的初始化操作
 */
/*
 * 作者：夏峥
 * 日期：2011-12-20
 * 描述：取消全部界面自动刷新功能，用户需手动点击刷新按钮才可刷新数据
 */
/*
 * 作者：夏峥
 * 日期：2012-03-16
 * 描述：取消界面特殊配置，库存不可编辑
 * 		 如果已有耗存信息，则只显示刷新按钮
 * 		调整界面风格，与月报其他模块同一
 */
public class Yueshchjb extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
	private static final int shouml_shouml = 0;
	private static final int shouml_sunh = 1;
	private static final int shouml_tiaozl = 2;
	private static final int shouml_yingd = 3;
	private static final int shouml_kuid = 4;
	private static final int shouml_biaoz = 5;
	private static final int shouml_laimsl = 6;
	private static final int meihl_fady = 0;
	private static final int meihl_gongry = 1;
	private static final int meihl_qith = 2;
	private static final int qickc_kc = 0;
	private static final int huiz_qickc = 0;
	private static final int huiz_shouml = 1;
	private static final int huiz_fady = 2;
	private static final int huiz_gongry = 3;
	private static final int huiz_qith = 4;
	private static final int huiz_sunh = 5;
	private static final int huiz_diaocl = 6;
	private static final int huiz_panyk = 7;
	private static final int huiz_kuc  = 8;
	private static final int huiz_shuifctz=9;
	private static final int huiz_runxcs=10;
	private static final int huiz_jitcs=11;
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
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
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
		String CurrZnDate="";
		String CurrODate = "";
		
		if(visit.getString10().equals(strParam)){
			CurrZnDate = sj[0] + "年" + sj[1] + "月";
			CurrODate=DateUtil.FormatOracleDate(sj[0] + "-" + sj[1]+ "-01");
		}else{
			CurrZnDate = getNianf() + "年" + getYuef() + "月";
			CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		}
		
		String strSql = "delete from yueshchjb where riq="+CurrODate
		+" and diancxxb_id = "+getTreeid();
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("删除过程出现错误！");
		}else {
			setMsg(CurrZnDate+"的数据被成功删除！");
		}
		con.Close();
	}
	
	public double[] getShouml(JDBCcon con, String diancxxb_id, String CurrODate) {
		double shouml[] = null;//收煤量
		String strSql="select sum(jingz) as shouml,sum(biaoz) biaoz,sum(laimsl) laimsl,\n" +
				      "       sum(yuns) yuns,sum(ructzl) ructzl,sum(yingd) yingd, sum(kuid) kuid\n" +
				      "  from yueslb ys,yuetjkjb yj\n" +
				      " where ys.yuetjkjb_id=yj.id\n" +
				      "   and yj.riq="+CurrODate+"\n" +
				      "   and yj.diancxxb_id in("+diancxxb_id+")\n" + 
				      "   and fenx ='本月' group by fenx";
		ResultSetList rs = con.getResultSetList(strSql);
		if (rs == null) {//判断是否连接失败
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return shouml;
		}
		if(rs.next()){
			shouml = new double[7];
			shouml[shouml_shouml] = rs.getDouble("shouml");//净重
			shouml[shouml_sunh] = rs.getDouble("yuns");//损耗
			shouml[shouml_tiaozl] = rs.getDouble("ructzl");
			shouml[shouml_yingd] = rs.getDouble("yingd");
			shouml[shouml_kuid] = rs.getDouble("kuid");
			shouml[shouml_biaoz] = rs.getDouble("biaoz");
			shouml[shouml_laimsl] = rs.getDouble("laimsl");
		}else{
			setMsg("收煤量获取失败，请确认数量月报已经生成！");
			return shouml;
		}
		return shouml;
	}
	public double[] getShuifc(JDBCcon con, String diancxxb_id, String Riq1,String Riq2){
		double shuifc[] = new double[2];
		String strSql=
			"select dianc.mingc, rucsf.mt rucsf, rulsf.mt rulsf\n" +
			"  from (select f.diancxxb_id,\n" + 
			"               round_new(decode(sum(f.jingz),0,0,sum(z.mt * f.jingz) / sum(f.jingz)),3) mt\n" + 
			"          from fahb f, zhilb z\n" + 
			"         where f.zhilb_id = z.id\n" + 
			"           and f.daohrq >= "+Riq1+"\n" + 
			"           and f.daohrq <= "+Riq2+"\n" + 
			"         group by f.diancxxb_id) rucsf,\n" + 
			"       (select m.diancxxb_id,\n" + 
			"               round_new(decode(sum(m.fadhy + m.gongrhy + m.qity + m.feiscy),0,0,\n" + 
			"                                sum(r.mt *(m.fadhy + m.gongrhy + m.qity + m.feiscy)) /\n" + 
			"                                sum(m.fadhy + m.gongrhy + m.qity + m.feiscy)),3) mt\n" + 
			"          from meihyb m, rulmzlb r\n" + 
			"         where m.rulmzlb_id = r.id\n" + 
			"           and m.rulrq >= "+Riq1+"\n" + 
			"           and m.rulrq <= "+Riq2+"\n" + 
			"         group by m.diancxxb_id) rulsf,\n" + 
			"       vwdianc dianc\n" + 
			" where dianc.id = rucsf.diancxxb_id\n" + 
			"   and dianc.id = rulsf.diancxxb_id\n" + 
			"   and dianc.id in("+diancxxb_id+")";
		ResultSetList rs=con.getResultSetList(strSql);
		if(!con.getHasIt(strSql)){
			shuifc[0]=0.0;
			shuifc[1]=0.0;
			return shuifc;
		}
		if (rs.next()) {
			shuifc[0]=rs.getDouble("rucsf");
			shuifc[1]=rs.getDouble("rulsf");
		}
		return shuifc;
	}
	
	public double[] getMeihl(JDBCcon con, String diancxxb_id, String Riq1,String Riq2, String date_c) {
		double meihl[] = null;
		String strSql="select sum(mh.fadhy) as fadhy,sum(mh.gongrhy) as gongrhy,sum(mh.qity) as qity,sum(feiscy) feiscy\n" +
		" from meihyb mh where mh.rulrq >="+Riq1+"-"+date_c+" and mh.rulrq <="+Riq2+"-"+date_c+"\n" + 
		" and mh.diancxxb_id in("+diancxxb_id+") group by mh.rulrq";
		if(!con.getHasIt(strSql)){
			setMsg("缺少煤耗数据！");
			meihl = new double[3];
			meihl[meihl_fady] = 0.0;
			meihl[meihl_gongry] = 0.0;
			meihl[meihl_qith] = 0.0;
			return meihl;
		}
		strSql = "select nvl(sum(mh.fadhy),0) as fadhy,nvl(sum(mh.gongrhy),0) as gongrhy,nvl(sum(mh.qity + mh.feiscy),0) as qity\n" +
		" from meihyb mh where mh.rulrq >="+Riq1+"-"+date_c+" and mh.rulrq <="+Riq2+"-"+date_c+"\n" + 
		" and mh.diancxxb_id in("+diancxxb_id+ ")";
		ResultSetList rs=con.getResultSetList(strSql);
		if (rs == null) {//判断是否连接失败
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return meihl;
		}
		if(rs.next()) {
			meihl = new double[3];
			meihl[meihl_fady] = rs.getDouble("fadhy");//发热用
			meihl[meihl_gongry] = rs.getDouble("gongrhy");//供热用
			meihl[meihl_qith] = rs.getDouble("qity");//其它用
		}else {
//			不可能发生的错误
			setMsg("未知错误！");
			return meihl;
		}
		return meihl;
	}
	
	public double[] getQickc(JDBCcon con, String diancxxb_id, String CurrODate) {
		double qickc[] = null;
		String strSql=" select kuc from yueshchjb where riq=Add_Months("+CurrODate+",-1) and fenx ='本月' and diancxxb_id in("+diancxxb_id+")";//查询上个月的库存数
		ResultSetList rs=con.getResultSetList(strSql);
		if (rs == null) {//判断是否连接失败
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return qickc;
		}
		qickc = new double[1];
		if (rs.next()) {//但到上月的库存数据，本月/累计
			qickc[qickc_kc] = rs.getDouble("kuc");
		}else {
			qickc[qickc_kc] = 0.0;
		}
		return qickc;
	}
	
	public double[] getLeij(JDBCcon con,String diancxxb_id) {
//		Visit visit = (Visit) getPage().getVisit();
		String strDate[] = this.getRiq2().split("-");
		String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		double leij[] =null;
		String strSql=" select qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc,shuifctz,jitcs,runxcs from yueshchjb where riq=Add_Months("+CurrODate+",-1) and fenx ='累计' and diancxxb_id="+diancxxb_id;//查询上个月的库存数
		ResultSetList rs=con.getResultSetList(strSql);
		if (rs == null) {//判断是否连接失败
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return leij;
		}
		leij = new double[12];
		if(rs.next()) {
			leij[huiz_qickc] = rs.getDouble("qickc");
			leij[huiz_shouml] = rs.getDouble("shouml");
			leij[huiz_fady] = rs.getDouble("fady");
			leij[huiz_gongry] = rs.getDouble("gongry");
			leij[huiz_qith] = rs.getDouble("qith");
			leij[huiz_sunh] = rs.getDouble("sunh");
			leij[huiz_diaocl] = rs.getDouble("diaocl");
			leij[huiz_panyk] = rs.getDouble("panyk");
			leij[huiz_kuc] = rs.getDouble("kuc");
			leij[huiz_shuifctz] = rs.getDouble("shuifctz");
			leij[huiz_runxcs]=rs.getDouble("runxcs");
			leij[huiz_jitcs]=rs.getDouble("jitcs");
			
		}else {
			for(int i =0; i < leij.length ; i++) {
				leij[i] = 0.0;
			}
		}
		return leij;
	}
//	获得允许储损
	private double getRunxcs(JDBCcon con,String diancxxb_id,String Riq1,String Riq2){
		String sql=
			"select decode(count(id), 0, 0, sum(kuc) / count(id)) pingjkc\n" +
			"  from shouhcrbb\n" + 
			" where diancxxb_id in("+diancxxb_id+")\n" + 
			"   and riq >= "+Riq1+"\n" + 
			"   and riq <="+Riq2+"";
		ResultSetList rs=con.getResultSetList(sql);
		double runxcs=0.0;
		if(rs.next()){
			runxcs=rs.getDouble("pingjkc")*0.5/100;
		}
		return runxcs;
	}
//	//获得当月盘点填报信息
//	private ResultSetList getPandGD(JDBCcon con,String Riq1,String Riq2){
//		String sql=
//			"select id, kaisrq, jiesrq\n" +
//			"  from pand_gd\n" + 
//			" where id = (select max(id)\n" + 
//			"               from pand_gd\n" + 
//			"              where jiesrq>="+Riq1+" and jiesrq< ="+Riq2+" \n" + 
//			"                and diancxxb_id = "+getTreeid()+")";
//
//		ResultSetList rs=con.getResultSetList(sql);
//		if (rs==null) {
//			return null;
//		}
//		return rs;
//	}
	public void CreateData() {
		
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = String.valueOf(getTreeid());
		
		String sjdiancxxb_id = "";       //查询数据用
		
//		大同：生成月报时，二期、三期、洗煤厂的数据统计到二期。
		sjdiancxxb_id = MainGlobal.getXitxx_item("月报", "月报数据多厂合并", diancxxb_id, diancxxb_id);
		
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = "";//strDate[0]+"年"+strDate[1]+"月";
		String CurrODate = "";//DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		String Riq1="";//DateUtil.FormatOracleDate(this.getRiq1());
		String Riq2="";//DateUtil.FormatOracleDate(this.getRiq2());
		int intYuef=0;
		
		if(visit.getString10().equals(strParam)){
			CurrODate=DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
			Riq1=DateUtil.FormatOracleDate(this.getRiq1());
			Riq2=DateUtil.FormatOracleDate(this.getRiq2());
			CurrZnDate = strDate[0] + "年" + strDate[1] + "月";
			intYuef=Integer.parseInt(strDate[1]);
		}else{
			CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
			Riq1=CurrODate;
			Riq2=DateUtil.Formatdate("yyyy-MM-dd",DateUtil.getLastDayOfMonth(DateUtil.getDate(getNianf()+ "-"+ getYuef() + "-01")));
			CurrZnDate = getNianf() + "年" + getYuef() + "月";
			intYuef=Integer.parseInt(getYuef());
		}
		
		String date_c = MainGlobal.getXitxx_item("月报", "月报取数日期差", diancxxb_id, "0");
		String strSql="";
		int flag = 0;
//		本月数组
		double beny[] = new double[12];
//		当月盘盈亏
		double panyk = 0.0;//盘盈亏
//		当月库存
		double kuc = 0.0;
//		取得当月收煤量  [0] 收煤量 [1] 损耗 [2] 调整
		double shouml[] = getShouml(con,sjdiancxxb_id,CurrODate);
		if(shouml == null) {
			return;
		}
//		取得当月耗煤量 [0] 发电用  [1] 供热用 [2] 其它用 = 其它用 + 非生产用
		double meihl[] = getMeihl(con,sjdiancxxb_id,Riq1,Riq2,date_c);
		if(meihl == null) {
			return;
		}
		//取得当月入厂入炉水分 [0]入厂平均水分 [1]入炉平均水分
		double shuifc[]=getShuifc(con, sjdiancxxb_id, Riq1,Riq2);
		//当月水分差调整
		double shuifctzl=shuifc[0]==100?0:(meihl[0]+meihl[1]+meihl[2])*(1-(100-shuifc[1])/(100-shuifc[0]));
//		取得期初库存 [0] 当月期初库存 
		double qickc[] = getQickc(con,sjdiancxxb_id,CurrODate);
		if(qickc == null) {
			return;
		}
//		计算当月库存
		kuc+=shuifctzl;
		kuc += qickc[qickc_kc];
		/*
		 * 运损不计入库存 如想计入运损则自定义来煤量
		 * 修改时间：2008-12-03
		 * 修改人：王磊
		 * 原句 kuc += (shouml[shouml_shouml] + shouml[shouml_sunh] - shouml[shouml_tiaozl]);
		 * 新增收煤量算法shouml(jingz) - yuns + yingd - kuid 
		 */
		String sql = "select zhi from xitxxb where mingc ='月报耗用收煤量定义' and zhuangt = 1 and leib = '月报' and diancxxb_id =" + diancxxb_id;
		double shoumlv = 0.0;
		ResultSetList sm = con.getResultSetList(sql);
		if(sm.next()){
			if("票重+盈吨-亏吨-运损".equals(sm.getString("zhi"))){
				shoumlv = CustomMaths.round(shouml[shouml_biaoz] + shouml[shouml_yingd] - shouml[shouml_sunh] - shouml[shouml_kuid],0);
			}else{
				shoumlv = CustomMaths.round(shouml[shouml_shouml],0);
			}
		}else{
			shoumlv = CustomMaths.round(shouml[shouml_shouml],0);
		}
		sm.close();
		kuc += shoumlv - shouml[shouml_tiaozl]; 
		kuc = kuc -  meihl[meihl_fady] - meihl[meihl_gongry] - meihl[meihl_qith];
		kuc += panyk;
		/*
		 * 新增字段 计提储损、允许储损
		 */
		double runxcs=getRunxcs(con,sjdiancxxb_id,Riq1,Riq2);
		double jitcs=0;
		
		//kuc=kuc-jitcs;    //把计提储损计入库存
//		本月数组赋值 [0] 期初库存 [1] 收煤量 [2] 发电用 [3] 供热用 [4] 其它耗 [5] 损耗 [6] 调出量 [7] 盘盈亏 [8] 库存 [9]水分差调整 [10]允许储损 [11]计提储损
		beny[huiz_qickc] = qickc[0]; 
		beny[huiz_shouml] = shoumlv; 
		beny[huiz_fady] = meihl[0];
		beny[huiz_gongry] = meihl[1]; 
		beny[huiz_qith] = meihl[2]; 
		//损耗改为手工填报 不从 shouml[shouml_sunh] 中取值
		beny[huiz_sunh] =0;
		beny[huiz_diaocl] = shouml[shouml_tiaozl]; 
		beny[huiz_panyk] = panyk; 
		beny[huiz_kuc] = kuc;
		beny[huiz_shuifctz] = shuifctzl;
		beny[huiz_runxcs]=runxcs;
		beny[huiz_jitcs]=jitcs;
//		取得上月累计数
		double leij[] = getLeij(con,diancxxb_id);
		if(leij == null) {
			return;
		}
//		新建本月的累计数
		double benylj[] = new double[12];
//		计算本月累计数
		for(int i = 1; i < leij.length ; i++) {
			benylj[i] = leij[i] + beny[i];
		}
		benylj[huiz_kuc] = beny[huiz_kuc];
		
//		删除本月数
		strSql = "delete from yueshchjb where riq="+CurrODate
		+" and diancxxb_id = "+diancxxb_id;
		flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("生成过程出现错误！月收耗存合计删除失败！");
			con.rollBack();
			con.Close();
			return;
		}
//		生成当月数
		strSql = "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc,shuifctz,jitcs,runxcs) values("
			+ Long.parseLong(MainGlobal.getNewID(Long.parseLong(diancxxb_id)))
			+ "," + diancxxb_id
			+","+CurrODate+",'本月',"+beny[huiz_qickc]+","+beny[huiz_shouml]+","
			+ beny[huiz_fady] +","+beny[huiz_gongry]+","+beny[huiz_qith]+","+beny[huiz_sunh]+","
			+ beny[huiz_diaocl]+","+beny[huiz_panyk]+","+beny[huiz_kuc]+","+beny[huiz_shuifctz]+","+beny[huiz_jitcs]+","+beny[huiz_runxcs]+")";
		flag = con.getInsert(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
			setMsg("生成过程出现错误！月收耗存合计未插入成功！");
			con.rollBack();
			con.Close();
			return;
		}
//		生成累计数
		if(intYuef == 1) {
//			如果是一月累计==本月
			benylj[huiz_qickc] = beny[huiz_qickc];
			strSql = "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc,shuifctz,jitcs,runxcs) values("
				+ Long.parseLong(MainGlobal.getNewID(Long.parseLong(diancxxb_id)))
				+ "," + diancxxb_id
				+","+CurrODate+",'累计',"+beny[huiz_qickc]+","+beny[huiz_shouml]+","
				+ beny[huiz_fady] +","+beny[huiz_gongry]+","+beny[huiz_qith]+","+beny[huiz_sunh]+","
				+ beny[huiz_diaocl]+","+beny[huiz_panyk]+","+beny[huiz_kuc]+","+beny[huiz_shuifctz]+","+beny[huiz_jitcs]+","+beny[huiz_runxcs]+")";
		}else {
			benylj[huiz_qickc] = beny[huiz_qickc];
			strSql = "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc,shuifctz,jitcs,runxcs) values("
				+ Long.parseLong(MainGlobal.getNewID(Long.parseLong(diancxxb_id)))
				+ "," + diancxxb_id
				+","+CurrODate+",'累计',"+benylj[huiz_qickc]+","+benylj[huiz_shouml]+","
				+ benylj[huiz_fady] +","+benylj[huiz_gongry]+","+benylj[huiz_qith]+","+benylj[huiz_sunh]+","
				+ benylj[huiz_diaocl]+","+benylj[huiz_panyk]+","+benylj[huiz_kuc]+","+benylj[huiz_shuifctz]+","+benylj[huiz_jitcs]+","+benylj[huiz_runxcs]+")";
		}
		flag = con.getInsert(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
			setMsg("生成过程出现错误！月收耗存合计未插入成功！");
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"的数据成功生成！");
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
			"  from yueshchjb s\n" + 
			" where  s.diancxxb_id = "+getTreeid()+"\n" + 
			"   and s.riq = "+CurrODate;
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
		JDBCcon con = new JDBCcon();
		
		String diancxxb_id = getTreeid();
		String strDate[] = this.getRiq2().split("-");
		String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		String strSql = "select * from yueslb s, yuetjkjb k\n" +
				        "where s.yuetjkjb_id = k.id\n" +
				        "  and k.riq = "+ CurrODate + "\n" +
				        "  and k.diancxxb_id="+diancxxb_id;
		boolean isNotReady = !con.getHasIt(strSql);
		if(isNotReady) {
			setMsg("请在使用本模块之前，首先完成月数量数据的计算！");
		}
		strSql="select id,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,jitcs,kuc,runxcs\n" +
			   "  from yueshchjb\n" +
			   " where riq="+CurrODate+" and diancxxb_id="+diancxxb_id+" order by fenx";
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		// //设置表名称用于保存
		egu.setTableName("yueshchjb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("qickc").setHeader("期初库存");
		egu.getColumn("qickc").setWidth(60);
		egu.getColumn("qickc").setEditor(null);
		egu.getColumn("qickc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("shouml").setHeader("净重");
		egu.getColumn("shouml").setWidth(60);
		egu.getColumn("shouml").setEditor(null);
		egu.getColumn("shouml").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("fady").setHeader("发电用");
		egu.getColumn("fady").setWidth(60);
		egu.getColumn("gongry").setHeader("供热用");
		egu.getColumn("gongry").setWidth(60);
		egu.getColumn("qith").setHeader("其它用");
		egu.getColumn("qith").setWidth(60);
		egu.getColumn("sunh").setHeader("实际储损");
		egu.getColumn("sunh").setWidth(60);
		egu.getColumn("diaocl").setHeader("调出量");
		egu.getColumn("diaocl").setWidth(60);
		egu.getColumn("panyk").setHeader("盘盈亏");
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("panyk").setHidden(true);
		egu.getColumn("kuc").setHeader("库存");
		egu.getColumn("kuc").setWidth(60);
		egu.getColumn("kuc").setEditor(null);
		egu.getColumn("kuc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("shuifctz").setHeader("水分差调整");
		egu.getColumn("shuifctz").setWidth(80);
		egu.getColumn("runxcs").setHeader("允许储损");
		egu.getColumn("runxcs").setWidth(60);
		egu.getColumn("jitcs").setHeader("计提储损");
		egu.getColumn("jitcs").setWidth(60);
		egu.getColumn("jitcs").setHidden(true);
		
		egu.setDefaultsortable(false);  
		// /设置按钮
		StringBuffer sb = new StringBuffer();
		sb.append("function countChange(field,cha){ \n");
		sb.append("reclj = gridDiv_ds.getAt(1);\n");
		sb.append("oValue = eval(reclj.get(field)||0);\n");
		sb.append("value = eval((oValue + cha)||0);\n");
		sb.append("reclj.set(field,value);\n");
		sb.append("for(i =0;i<2;i++){\n");
		sb.append("rec = gridDiv_ds.getAt(i);rec.set('JITCS',eval(rec.get('SUNH')||0));\n");
		sb.append("okuc = eval(rec.get('KUC')||0);\n");
		sb.append("kuc = okuc;\n");
		sb.append("if(field == 'PANYK'||field=='SHUIFCTZ'){\n");
		sb.append("kuc = eval((okuc + cha)||0);\n");
		sb.append("}else{if(field=='RUNXCS'||field=='JITCS'){}else{\n");
		sb.append("kuc = eval((okuc - cha)||0);\n");
		sb.append("}}rec.set('KUC',kuc);\n");
		sb.append("}}\n");
		sb.append("gridDiv_grid.on('afteredit',function(e){\n");
		sb.append("cha = eval(e.value||0) - eval(e.originalValue||0);\n");
		sb.append("countChange(e.field,cha);\n");
		sb.append("});\n");		
		sb.append("gridDiv_grid.on('beforeedit',function(e){\n");
		sb.append("if(e.record.get('FENX')=='累计'){e.cancel=true;}\n");//当"累计"时,这一行不允许编辑
		sb.append("});\n");
		
		egu.addOtherScript(sb.toString());
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
	    	egu.addTbarText("-");
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
		egu.addTbarText("-");// 设置分隔符
		}
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
//		判断数据是否被锁定
		boolean isLocked = isLocked(con);
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){");
		if(visit.getString10().equals(strParam)){
			rsb.append(MainGlobal.getExtMessageBox("'正在刷新"+strDate[0]+"年"+strDate[1]+"月的数据,请稍候！'",true));
		}else{
			rsb.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",true));
		}
		rsb.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if(getZhangt(con)){
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		}else{
			if(isLocked){
				setMsg("耗存数据已经生成，<br>请先删除相应数据后再操作！");
			}else{
//				生成按钮
				GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
				gbc.setDisabled(isNotReady);
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
//				删除按钮
				GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
				gbd.setDisabled(isNotReady);
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);
//				保存按钮
				GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
				gbs.setDisabled(isNotReady);
				egu.addTbarBtn(gbs);
			}
		}
//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+MainGlobal.getOpenWinScript("MonthReport&lx=yuehcb")+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		Visit visit =(Visit)getPage().getVisit();
		StringBuffer btnsb = new StringBuffer();
		String cnDate ="";// getNianf()+"年"+getYuef()+"月";
		String sj[] = this.getRiq2().split("-");
		if(visit.getString10().equals(strParam)){
			cnDate=sj[0]+"年"+sj[1]+"月";
		}else{
			cnDate=getNianf()+"年"+getYuef()+"月";
		}
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
		String strDate = cycle.getRequestContext().getParameter("lx");
		if(strDate!=null){
			visit.setString10(strDate);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString11("");
			visit.setString9("");
//			初始化电厂树
			setTreeid(null);
			setRiq();
		}
		getSelectData();
	}
	
	public boolean isLocked(JDBCcon con) {//判断数据是否已锁定_保存
		String CurrODate ="";
		String sj[] = this.getRiq2().split("-");
		Visit visit=(Visit)getPage().getVisit();
		if(visit.getString10().equals(strParam)){
			CurrODate = DateUtil.FormatOracleDate(sj[0]+"-"+sj[1]+"-"+"01");
		}else{
			CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-"+"01");
		}
		return con.getHasIt("select yj.id from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
				" yj.riq="+CurrODate+"\n" + 
				" and yj.diancxxb_id="+this.getTreeid());
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