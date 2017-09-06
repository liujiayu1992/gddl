package com.zhiren.dc.jilgl.shujdr;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;

import javax.xml.rpc.ServiceException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.DateUtil;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.webservice.QichsjppInterface;
/**
 * @author Rock
 * @since 2009-12-02
 * @discrption 新版数据导入集中处理了所有参数。希望他是最终版
 * 需注意的问题  增加运输方式区分、电厂树、数据合计行、多选导入、
 * 自动导入的按钮显示、导入数据的删除（已导入数据判断状态）
 */
/*
 * 作者：夏峥
 * 时间：2012-10-26
 * 描述：供应商增加类型的判断
 */
/*
 * 作者：夏峥
 * 时间：2012-12-04
 * 描述：增加数据导入失败时错误信息的提示。
 */
/*
 * 作者：夏峥
 * 时间：2014-1-6
 * 描述：增加数据导入删除按钮取消的判断，并且调整bug，导入是汇总行的判断。
 */
/*
 * 作者：赵胜男
 * 时间：2014-1-14
 * 描述：增加煤场字段的取值。
 */
public class DataImport extends BasePage implements PageValidateListener {

	private static final String customKey = "DataImport";
	private static final String QY = "QY";// 汽运
	private static final String HY = "HY";// 火运
	private static final String DRYD = "DRYD";//导入运单
	
	private IDropDownModel iddm_ysfs = new IDropDownModel(SysConstant.SQL_yunsfs);
	private IDropDownModel iddm_ysdw = new IDropDownModel("select id,mingc from yunsdwb");
	private IDropDownModel iddm_yshdw = new IDropDownModel("select id,mingc from vwyuanshdw");
	private IDropDownModel iddm_xcfs = new IDropDownModel(SysConstant.SQL_xiecfs);
	private IDropDownModel iddm_cz = new IDropDownModel("select id, mingc from chezxxb");
	private IDropDownModel iddm_gys = new IDropDownModel("select id, mingc from gongysb where leix=1");
	private IDropDownModel iddm_mk = new IDropDownModel("select id, mingc from meikxxb");
	private IDropDownModel iddm_pz = new IDropDownModel(SysConstant.SQL_Pinz_mei);
	private IDropDownModel iddm_kj = new IDropDownModel(SysConstant.SQL_Kouj);
	
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public String getRiq(){
		return ((Visit) this.getPage().getVisit()).getString5();
	}
	public void setRiq(String rq){
		((Visit) this.getPage().getVisit()).setString5(rq);
	}
	
	public String getMod() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	public void setMod(String mod) {
		((Visit) this.getPage().getVisit()).setString3(mod);
	}
	public String getLeix() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}
	public void setLeix(String leix) {
		((Visit) this.getPage().getVisit()).setString4(leix);
	}
	public String getFahtmpId(){
		return ((Visit) this.getPage().getVisit()).getString1();
	}
	public void setFahtmpId(String ids){
		((Visit) this.getPage().getVisit()).setString1(ids);
	}
//	设置电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
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
//	设置电厂树_结束

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql;
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl.getRows()>0){
			String ids = "";
			while(rsl.next()){
				if( !rsl.getString("id").equals("-1")){
					ids += "," + rsl.getString("id");
				}
			}
			setFahtmpId(ids.substring(1));
		}else{
			setMsg("请选择数据");
			return;
		}
		sql = "select c.id, c.diancxxb_id, c.gongysmc gongysb_id, c.meikdwmc meikxxb_id, c.faz faz_id,\n"
			+ "c.pinz pinzb_id, c.jihkj jihkjb_id, zhilb_id,piaojh, c.fahrq, c.daohrq, c.jianjfs,c.chebb_id,\n"
			+ "c.chec, '' as bianm, to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') guohsj,c.qingchh,c.qingcjjy,\n" 
			+ " to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') zhongcsj, c.zhongchh, c.zhongcjjy,c.caiyrq, c.cheph, c.maoz, c.piz, c.biaoz,\n"
			+ "c.koud, c.kous, c.kouz, c.yuanmz, c.yuanpz, c.sanfsl, c.daoz daoz_id, c.daoz yuandz_id,c.YUNSFS,c.yuanshdw,\n"
			+ "c.meikdwmc yuanmkdw, c.yunsdw, c.daozch, c.xiecfs, c.beiz ,c.fahbtmp_id,MEICB_ID from chepbtmp c where fahbtmp_id in ("
			+ getFahtmpId()+") and to_char(qingcsj,'yyyy-mm-dd')='" + getRiq() + "' order by c.zhongcsj,c.id";
		rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ this.getClass().getName()  +"保存方法出错。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		sb.append("begin\n");
		while(rsl.next()){
			long gongysb_id = iddm_gys.getBeanId(rsl.getString("gongysb_id"));
			long meikxxb_id = iddm_mk.getBeanId(rsl.getString("meikxxb_id"));
			long pinzb_id = iddm_pz.getBeanId(rsl.getString("pinzb_id"));
			long faz_id = iddm_cz.getBeanId(rsl.getString("faz_id"));
			long daoz_id = iddm_cz.getBeanId(rsl.getString("daoz_id"));
			long yunsfs_id = iddm_ysfs.getBeanId(rsl.getString("YUNSFS"));
			long jihkj_id = iddm_kj.getBeanId(rsl.getString("jihkjb_id"));
			long xiecfs_id = iddm_xcfs.getBeanId(rsl.getString("xiecfs"));
			long yunsdw_id = iddm_ysdw.getBeanId(rsl.getString("yunsdw"));
			long yuanshdw_id = iddm_yshdw.getBeanId(rsl.getString("yuanshdw"));
			long yuandz_id = iddm_cz.getBeanId(rsl.getString("yuandz_id"));
			String  MEICB_ID = rsl.getString("MEICB_ID");
			if(gongysb_id==-1 ){
				setMsg("供应商未匹配，请匹配后再试!");
				rsl.close();
				con.Close();
				return ;
			}
			if(meikxxb_id==-1 ){
				setMsg("煤矿未匹配，请匹配后再试!");
				rsl.close();
				con.Close();
				return ;
			}
			
			if(pinzb_id==-1 ){
				setMsg("品种未匹配，请匹配后再试!");
				rsl.close();
				con.Close();
				return ;
			}
			
			if(faz_id ==-1||daoz_id ==-1 || yunsfs_id==-1 ||jihkj_id ==-1 || xiecfs_id ==-1){
				setMsg("其他数据未匹配，请匹配后再试!");
				rsl.close();
				con.Close();
				return ;
			}
			sb.append("insert into cheplsb\n");
			sb
					.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id,zhilb_id,piaojh, fahrq, daohrq, caiybh, yunsfsb_id, chec,cheph, maoz, piz, biaoz, koud, kous, kouz, yuanmz, yuanpz, sanfsl, jianjfs, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, zhongcjjy, daozch, lury, beiz,qingcsj,qingchh,QINGCJJY,zhongcsj,zhongchh,yunsdwb_id,caiyrq,xiecfsb_id,MEICB_ID)\n");
			sb.append("values (").append(rsl.getString("id"))
					.append(",").append(rsl.getString("diancxxb_id"));
			sb.append(",").append(gongysb_id);
			sb.append(",").append(meikxxb_id);
			sb.append(",").append(pinzb_id);
			sb.append(",").append(faz_id);
			sb.append(",").append(daoz_id);
			sb.append(",").append(jihkj_id);
			sb.append(",").append(rsl.getString("zhilb_id"));
			sb.append(",'").append(rsl.getString("piaojh"));
			sb.append("',to_date('").append(
					DateUtil.FormatDate(rsl.getDate("fahrq"))).append(
					"','yyyy-mm-dd')");
			sb.append(",to_date('").append(
					DateUtil.FormatDate(rsl.getDate("daohrq"))).append(
					"','yyyy-mm-dd')");
			sb.append(",'").append(rsl.getString("bianm")).append("'");
			sb.append(",").append(yunsfs_id)
					.append(",'").append(rsl.getString("chec"));
			sb.append("','").append(rsl.getString("cheph"));
			sb.append("',").append(rsl.getDouble("maoz"));
			sb.append(",").append(rsl.getDouble("piz"));
			sb.append(",").append(rsl.getDouble("biaoz"));
			sb.append(",").append(rsl.getDouble("koud"));
			sb.append(",").append(rsl.getDouble("kous"));
			sb.append(",").append(rsl.getDouble("kouz"));
			sb.append(",").append(rsl.getDouble("yuanmz"));
			sb.append(",").append(rsl.getDouble("yuanpz"));
			sb.append(",").append(rsl.getDouble("sanfsl"));
			sb.append(",'").append(rsl.getString("jianjfs"));
			sb.append("',").append(rsl.getString("chebb_id"));
			sb.append(",").append(yuandz_id);
			sb.append(",")
					.append(yuanshdw_id);
			sb.append(",'").append(rsl.getString("yuanmkdw"));
			sb.append("','").append(rsl.getString("zhongcjjy"));
			sb.append("','").append(rsl.getString("daozch"));
			sb.append("','").append(visit.getRenymc());
			sb.append("','").append(rsl.getString("beiz")).append("'");
			sb.append(",").append(
					DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
			sb.append(",'").append(rsl.getString("qingchh")).append("'");
			sb.append(",'").append(rsl.getString("qingcjjy")).append("'");
			sb.append(",").append(
					DateUtil.FormatOracleDateTime(rsl.getString("zhongcsj")));
			sb.append(",'").append(rsl.getString("zhongchh")).append("'");
			sb.append(",").append(yunsdw_id);
			sb.append(",to_date('").append(DateUtil.FormatDate(rsl.getDate("caiyrq"))).append(
					"','yyyy-mm-dd hh24:mi:ss'),").append(xiecfs_id).append(",").append(MEICB_ID).append(");\n");
		}
		sb.append("end;");
		rsl.close();
		int yunsfs = SysConstant.YUNSFS_HUOY;
		if (HY.equals(getLeix())) {
			
		} else if (QY.equals(getLeix())) {
			yunsfs = SysConstant.YUNSFS_QIY;
		}
		int Hedbz = SysConstant.HEDBZ_YJJ;
		if(DRYD.equalsIgnoreCase(getMod())){
			Hedbz = SysConstant.HEDBZ_TJ;
		}
		setMsg(Jilcz.SaveJilData(sb.toString(), visit.getDiancxxb_id(), yunsfs, 
				Hedbz, null, this.getClass().getName(), Jilcz.SaveMode_DR, getFahtmpId()));
		con.Close();
		
	}
	private void Update(IRequestCycle cycle) {
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl.getRows()>0){
			String ids = "";
			while(rsl.next()){
				ids += "," + rsl.getString("id");
			}
			setFahtmpId(ids.substring(1));
			cycle.activate("DataImportDetails");
		}else{
			setMsg("请选择数据");
		}
		rsl.close();
	}

	private void delete(){
		JDBCcon con = new JDBCcon();
		String sql ;
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			sql = "delete from chepbtmp where fahbtmp_id =" + rsl.getString("id");
			con.getDelete(sql);
		}
		rsl.close();
	}
	
	private void AutoMath() throws MalformedURLException, ServiceException,
			RemoteException {
		// TODO 自动生成方法存根
		// 手动调用自动匹配导入方法
		String ReMsg = "";
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		ReMsg = QichsjppInterface.Qicsjpp_sd(
				((Visit) this.getPage().getVisit()).getDiancxxb_id(), riqTiaoj);// 匹配操作
		setMsg(ReMsg);
	}

	private boolean _RefurbishChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ChakChick = false;

	public void ChakButton(IRequestCycle cycle) {
		_ChakChick = true;
	}

	private boolean _AutoMathChick = false; // 自动匹配按钮

	public void AutoMathButton(IRequestCycle cycle) {
		_AutoMathChick = true;
	}

	private boolean _DeleteChick=false;
	public void DeleteButton(IRequestCycle cycle){
		_DeleteChick=true;
	}
	
	public void submit(IRequestCycle cycle) throws MalformedURLException,
			RemoteException, ServiceException {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}

		if (_AutoMathChick) {
			_AutoMathChick = false;
			AutoMath();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			delete();
		}
		getSelectData();
		if (_ChakChick) {
			_ChakChick = false;
			Update(cycle);
		}
	}

	private String getSQLCondtion_ysfs(){
		String strSQLConditions_ysfs = "";
		if (HY.equals(getLeix())) {
			strSQLConditions_ysfs = " and yunsfs = '" + iddm_ysfs.getBeanValue(SysConstant.YUNSFS_HUOY) + "'";
		} else if (QY.equals(getLeix())) {
			strSQLConditions_ysfs = " and yunsfs = '" + iddm_ysfs.getBeanValue(SysConstant.YUNSFS_QIY) + "'";
		}
		return strSQLConditions_ysfs;
	}
	
	private String getSQLCondtion_dc(){
		String strSQLConditions_dc = " and diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")";
		return strSQLConditions_dc;
	}
	
	private String getSQLCondtion_sfdr(){
//		现在为固定查询未导入数据可根据需要调整 (预留调整后需更改程序)
		String strSQLConditions_sfdr = " and fahb_id=0";
		return strSQLConditions_sfdr;
	}
	
	private String getSQLCondtion_dhrq(){
		String strOraDate ;
		if(getRiq() != null && !"".equals(getRiq())){
			strOraDate = DateUtil.FormatOracleDate(getRiq());
		}else{
			strOraDate = DateUtil.FormatOracleDate(new Date());
		}
		String strSQLConditions_dhrq = " daohrq = " + strOraDate;
		return strSQLConditions_dhrq;
	}
	
	private void updateTmpId(JDBCcon con,String sql){
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("begin\n");
			while (rsl.next()) {
				long fahbtmpid = rsl.getLong("ID");
				if(fahbtmpid == -1){
					fahbtmpid = Long.parseLong(MainGlobal.getNewID(rsl.getLong("diancxxb_id")));
				}
				rsl.setString("ID", String.valueOf(fahbtmpid));
				if (rsl.getString("caiybh").trim().equals("")
						|| rsl.getString("caiybh") == null) {
					sb.append("update chepbtmp set fahbtmp_id=" + fahbtmpid
							+ " where gongysmc ='"
							+ rsl.getString("gongysb_id"));
					sb.append("' and meikdwmc='" + rsl.getString("meikxxb_id")
							+ "' and faz = '" + rsl.getString("faz_id")
							+ "' and daoz='" + rsl.getString("daoz_id")
							+ "' and pinz='" + rsl.getString("pinzb_id"));
					sb.append("' and fahrq=to_date('"
							+ DateUtil.FormatDate(rsl.getDate("fahrq"))
							+ "','yyyy-mm-dd') and diancxxb_id = " + rsl.getString("diancxxb_id")
							+ " and daohrq=to_date('"
							+ DateUtil.FormatDate(rsl.getDate("daohrq"))
							+ "','yyyy-mm-dd') and chec='"
							+ rsl.getString("chec") + "' and caiybh is null and fahbtmp_id=-1;\n");
				} else {
					sb.append("update chepbtmp set fahbtmp_id=" + fahbtmpid
							+ " where gongysmc ='"
							+ rsl.getString("gongysb_id"));
					sb.append("' and meikdwmc='" + rsl.getString("meikxxb_id")
							+ "' and faz = '" + rsl.getString("faz_id")
							+ "' and daoz='" + rsl.getString("daoz_id")
							+ "' and pinz='" + rsl.getString("pinzb_id"));
					sb.append("' and fahrq=to_date('"
							+ DateUtil.FormatDate(rsl.getDate("fahrq"))
							+ "','yyyy-mm-dd') and diancxxb_id = " + rsl.getString("diancxxb_id")
							+ " and daohrq=to_date('"
							+ DateUtil.FormatDate(rsl.getDate("daohrq"))
							+ "','yyyy-mm-dd') and chec='"
							+ rsl.getString("chec") + "' and caiybh='" 
							+ rsl.getString("caiybh") + "'"
//							+ " --and fahbtmp_id=-1 " 未导入时，合并样
							+ " ;\n");
				}

			}
			sb.append("end;\n");
			con.getUpdate(sb.toString());
		}
		rsl.close();
	}

	public void getSelectData() {
		String sql;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl;
		boolean isShowTotal = false;	//是否显示汇总行
		/*  设置是否显示汇总行参数  */
		sql = "select zhi from xitxxb where mingc = '数据导入显示汇总行' and zhi='是' and zhuangt =1 and beiz = '使用' and leib ='数量' and diancxxb_id=" + visit.getDiancxxb_id();
		isShowTotal = con.getHasIt(sql);
		
		boolean isShowAutoImportButton = false;	//是否显示自动导入按钮
		/*  设置是否显示自动导入按钮  */
		sql = "select zhi from xitxxb where mingc = '数据导入显示自动导入按钮' and zhi='是' and zhuangt =1 and beiz = '使用' and leib ='数量' and diancxxb_id=" + visit.getDiancxxb_id();
		isShowAutoImportButton = con.getHasIt(sql);
		
		boolean isUpdateTmpId = true;	//是否更新FAHTMPID
		/*  设置是否更新FAHTMPID  */
		sql = "select zhi from xitxxb where mingc = '数据导入是否更新FAHTMPID' and zhi='否' and zhuangt =1 and beiz = '使用' and leib ='数量' and diancxxb_id=" + visit.getDiancxxb_id();
		isUpdateTmpId = !con.getHasIt(sql);
		
		String strSQLCondition_ysfs = getSQLCondtion_ysfs();	//查询SQL运输方式条件
		String strSQLCondition_dc = getSQLCondtion_dc(); 		//查询SQL电厂条件
		String strSQLCondition_sfdr = getSQLCondtion_sfdr();	//查询SQL是否导入条件
		String strSQLCondition_dhrq = getSQLCondtion_dhrq();	//查询SQL到货日期条件
		
		/*  更新发货TMPID  */
		if(isUpdateTmpId){
			sql = "select distinct diancxxb_id,gongysmc as gongysb_id,meikdwmc as meikxxb_id,faz as faz_id," +
				"daoz as daoz_id,pinz as pinzb_id,jihkj as jihkjb_id,fahrq,daohrq,caiybh,chec," +
				"nvl(max(FAHBTMP_ID),-1) as ID from chepbtmp where " + strSQLCondition_dhrq +
				strSQLCondition_ysfs + strSQLCondition_dc + strSQLCondition_sfdr + 
				" group by diancxxb_id,gongysmc, meikdwmc,faz,daoz,pinz,jihkj,fahrq,daohrq,caiybh,chec";
			updateTmpId(con,sql);
		}
		
		sql = "select diancxxb_id as dcid, gongysmc as gys, meikdwmc as mkdw, faz, daoz, pinz, jihkj,\n" +
		"fahrq, daohrq, caiybh, chec, nvl(FAHBTMP_ID, -1) as ID, count(id) as ches, sum(maoz) as maoz,\n" + 
		"sum(piz) as piz, sum(koud) as koud, sum(biaoz) as biaoz, sum(maoz - piz - koud - kous - kouz) jingz\n" +
		"from chepbtmp where " + strSQLCondition_dhrq + strSQLCondition_ysfs + strSQLCondition_dc + 
		strSQLCondition_sfdr + " group by rollup(diancxxb_id,gongysmc,meikdwmc,faz,daoz,pinz,jihkj,fahrq,\n" +
		"daohrq,caiybh,chec,FAHBTMP_ID) having grouping(fahbtmp_id) = 0 " + 
		(isShowTotal?" or grouping(diancxxb_id) = 1":"");
		
		rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, customKey);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		设置多选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		egu.setTableName("chepbtmp");
		egu.getColumn("dcid").setHeader("电厂");
		egu.getColumn("dcid").setHidden(true);
		egu.getColumn("gys").setHeader("供货单位");
		egu.getColumn("gys").setWidth(80);
		egu.getColumn("gys").setEditor(null);
		egu.getColumn("mkdw").setHeader("煤矿");
		egu.getColumn("mkdw").setWidth(80);
		egu.getColumn("mkdw").setEditor(null);
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("daoz").setHeader("到站");
		egu.getColumn("daoz").setWidth(60);
		egu.getColumn("daoz").setEditor(null);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("jihkj").setHeader("计划口径");
		egu.getColumn("jihkj").setWidth(60);
		egu.getColumn("jihkj").setEditor(null);
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("caiybh").setHeader("采样编号");
		egu.getColumn("caiybh").setWidth(60);
		egu.getColumn("caiybh").setEditor(null);
		egu.getColumn("chec").setHeader("车次");
		egu.getColumn("chec").setWidth(50);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setWidth(50);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("koud").setHeader("扣吨");
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("koud").setEditor(null);
		egu.getColumn("biaoz").setHeader("标重");
		egu.getColumn("biaoz").setWidth(50);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(50);
		egu.getColumn("jingz").setEditor(null);
	
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		GridButton gbr = new GridButton("刷新", 
				"function (){document.getElementById('RefreshButton').click();}", 
				SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		GridButton bc = new GridButton("导入", GridButton.ButtonType_Sel, egu.gridId, egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(bc);
		
		GridButton sc = new GridButton("删除", GridButton.ButtonType_Sel, egu.gridId, egu.getGridColumns(), "DeleteButton");
		if(MainGlobal.getXitxx_item("数量", "数据导入是否显示删除按钮", "0", "是").equals("是")){
			egu.addTbarBtn(sc);
		}
		
		GridButton gb = new GridButton("修改明细", GridButton.ButtonType_Sel, egu.gridId, egu.getGridColumns(), "ChakButton");
		egu.addTbarBtn(gb);
		
		if(isShowAutoImportButton){
			GridButton TBb = new GridButton("自动匹配并导入", "function(){ \n"
					+ " var rec=gridDiv_ds.getRange();	\n"
					+ " if(rec.length==0){ \n"
					+ "		Ext.MessageBox.alert('提示信息','没有未导入的汽车数据');	\n"
					+ " }else{	\n"
					+ " 		document.getElementById('AutoMathButton').click();}	\n}");
			egu.addTbarBtn(TBb);
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	// 页面判定方法
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
		String lx = cycle.getRequestContext().getParameter("lx");
		if ( lx != null && !"".equals(lx)) {
			setLeix(lx);
		}
		String mod = cycle.getRequestContext().getParameter("mod");
		if ( mod != null && !"".equals(mod)) {
			setMod(mod);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if(!visit.getActivePageName().toString().equals("DataImportDetails")){
				JDBCcon con = new JDBCcon ();
				String sql = "select zhi from xitxxb where mingc ='数据导入默认日期' " +
						"and zhuangt=1 and leib='数量' and beiz='使用' and diancxxb_id ="+
						visit.getDiancxxb_id();
				int riqpy = -1 ;
				ResultSetList rsl = con.getResultSetList(sql);
				while(rsl.next()){
					riqpy = rsl.getInt("zhi");
				}
				setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), riqpy,
						DateUtil.AddType_intDay)));
				setTreeid(visit.getDiancxxb_id()+"");
				rsl.close();
				con.Close();
			}
			visit.setActivePageName(this.getPageName().toString());
			if(getLeix()==null || "".equals(getLeix())){
				setLeix("HY");
			}
			if(getMod()==null || "".equals(getMod())){
				setMod("");
			}
			getSelectData();
		}
	}
}