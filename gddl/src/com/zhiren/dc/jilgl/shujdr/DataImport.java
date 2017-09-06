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
 * @discrption �°����ݵ��뼯�д��������в�����ϣ���������հ�
 * ��ע�������  �������䷽ʽ���֡��糧�������ݺϼ��С���ѡ���롢
 * �Զ�����İ�ť��ʾ���������ݵ�ɾ�����ѵ��������ж�״̬��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-26
 * ��������Ӧ���������͵��ж�
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-12-04
 * �������������ݵ���ʧ��ʱ������Ϣ����ʾ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-1-6
 * �������������ݵ���ɾ����ťȡ�����жϣ����ҵ���bug�������ǻ����е��жϡ�
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2014-1-14
 * ����������ú���ֶε�ȡֵ��
 */
public class DataImport extends BasePage implements PageValidateListener {

	private static final String customKey = "DataImport";
	private static final String QY = "QY";// ����
	private static final String HY = "HY";// ����
	private static final String DRYD = "DRYD";//�����˵�
	
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
//	���õ糧��_��ʼ
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
//	���õ糧��_����

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
			setMsg("��ѡ������");
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
							+ this.getClass().getName()  +"���淽������");
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
				setMsg("��Ӧ��δƥ�䣬��ƥ�������!");
				rsl.close();
				con.Close();
				return ;
			}
			if(meikxxb_id==-1 ){
				setMsg("ú��δƥ�䣬��ƥ�������!");
				rsl.close();
				con.Close();
				return ;
			}
			
			if(pinzb_id==-1 ){
				setMsg("Ʒ��δƥ�䣬��ƥ�������!");
				rsl.close();
				con.Close();
				return ;
			}
			
			if(faz_id ==-1||daoz_id ==-1 || yunsfs_id==-1 ||jihkj_id ==-1 || xiecfs_id ==-1){
				setMsg("��������δƥ�䣬��ƥ�������!");
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
			setMsg("��ѡ������");
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
		// TODO �Զ����ɷ������
		// �ֶ������Զ�ƥ�䵼�뷽��
		String ReMsg = "";
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		ReMsg = QichsjppInterface.Qicsjpp_sd(
				((Visit) this.getPage().getVisit()).getDiancxxb_id(), riqTiaoj);// ƥ�����
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

	private boolean _AutoMathChick = false; // �Զ�ƥ�䰴ť

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
//		����Ϊ�̶���ѯδ�������ݿɸ�����Ҫ���� (Ԥ������������ĳ���)
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
//							+ " --and fahbtmp_id=-1 " δ����ʱ���ϲ���
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
		boolean isShowTotal = false;	//�Ƿ���ʾ������
		/*  �����Ƿ���ʾ�����в���  */
		sql = "select zhi from xitxxb where mingc = '���ݵ�����ʾ������' and zhi='��' and zhuangt =1 and beiz = 'ʹ��' and leib ='����' and diancxxb_id=" + visit.getDiancxxb_id();
		isShowTotal = con.getHasIt(sql);
		
		boolean isShowAutoImportButton = false;	//�Ƿ���ʾ�Զ����밴ť
		/*  �����Ƿ���ʾ�Զ����밴ť  */
		sql = "select zhi from xitxxb where mingc = '���ݵ�����ʾ�Զ����밴ť' and zhi='��' and zhuangt =1 and beiz = 'ʹ��' and leib ='����' and diancxxb_id=" + visit.getDiancxxb_id();
		isShowAutoImportButton = con.getHasIt(sql);
		
		boolean isUpdateTmpId = true;	//�Ƿ����FAHTMPID
		/*  �����Ƿ����FAHTMPID  */
		sql = "select zhi from xitxxb where mingc = '���ݵ����Ƿ����FAHTMPID' and zhi='��' and zhuangt =1 and beiz = 'ʹ��' and leib ='����' and diancxxb_id=" + visit.getDiancxxb_id();
		isUpdateTmpId = !con.getHasIt(sql);
		
		String strSQLCondition_ysfs = getSQLCondtion_ysfs();	//��ѯSQL���䷽ʽ����
		String strSQLCondition_dc = getSQLCondtion_dc(); 		//��ѯSQL�糧����
		String strSQLCondition_sfdr = getSQLCondtion_sfdr();	//��ѯSQL�Ƿ�������
		String strSQLCondition_dhrq = getSQLCondtion_dhrq();	//��ѯSQL������������
		
		/*  ���·���TMPID  */
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
//		���ö�ѡ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		egu.setTableName("chepbtmp");
		egu.getColumn("dcid").setHeader("�糧");
		egu.getColumn("dcid").setHidden(true);
		egu.getColumn("gys").setHeader("������λ");
		egu.getColumn("gys").setWidth(80);
		egu.getColumn("gys").setEditor(null);
		egu.getColumn("mkdw").setHeader("ú��");
		egu.getColumn("mkdw").setWidth(80);
		egu.getColumn("mkdw").setEditor(null);
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("daoz").setHeader("��վ");
		egu.getColumn("daoz").setWidth(60);
		egu.getColumn("daoz").setEditor(null);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("jihkj").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkj").setWidth(60);
		egu.getColumn("jihkj").setEditor(null);
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("caiybh").setHeader("�������");
		egu.getColumn("caiybh").setWidth(60);
		egu.getColumn("caiybh").setEditor(null);
		egu.getColumn("chec").setHeader("����");
		egu.getColumn("chec").setWidth(50);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(50);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("koud").setEditor(null);
		egu.getColumn("biaoz").setHeader("����");
		egu.getColumn("biaoz").setWidth(50);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setWidth(50);
		egu.getColumn("jingz").setEditor(null);
	
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("�糧��");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		GridButton gbr = new GridButton("ˢ��", 
				"function (){document.getElementById('RefreshButton').click();}", 
				SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		GridButton bc = new GridButton("����", GridButton.ButtonType_Sel, egu.gridId, egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(bc);
		
		GridButton sc = new GridButton("ɾ��", GridButton.ButtonType_Sel, egu.gridId, egu.getGridColumns(), "DeleteButton");
		if(MainGlobal.getXitxx_item("����", "���ݵ����Ƿ���ʾɾ����ť", "0", "��").equals("��")){
			egu.addTbarBtn(sc);
		}
		
		GridButton gb = new GridButton("�޸���ϸ", GridButton.ButtonType_Sel, egu.gridId, egu.getGridColumns(), "ChakButton");
		egu.addTbarBtn(gb);
		
		if(isShowAutoImportButton){
			GridButton TBb = new GridButton("�Զ�ƥ�䲢����", "function(){ \n"
					+ " var rec=gridDiv_ds.getRange();	\n"
					+ " if(rec.length==0){ \n"
					+ "		Ext.MessageBox.alert('��ʾ��Ϣ','û��δ�������������');	\n"
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

	// ҳ���ж�����
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
				String sql = "select zhi from xitxxb where mingc ='���ݵ���Ĭ������' " +
						"and zhuangt=1 and leib='����' and beiz='ʹ��' and diancxxb_id ="+
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