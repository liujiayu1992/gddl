package com.zhiren.gs.bjdt.pandreport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pandzmm_bjdt extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	 ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	//�̵���������
	public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pandb p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ " and p.zhuangt=0"+ " order by p.id desc";
//		    v.setProSelectionModel10(new IDropDownModel(sql,"��ѡ��"));
		    v.setProSelectionModel10(new IDropDownModel(sql));
		}
	    return v.getProSelectionModel10();
	}
	public void setPandValue(IDropDownBean value) {
		((Visit)getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getPandModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		String pandbm = "";
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pandb where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() 
					+ " and zhuangt=0 order by id desc";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				pandbm = rsl.getString("bianm");
			}
			return pandbm;
		}
		return getPandValue().getValue();
	}
	public long getPandbID() {
		if (getPandValue() == null) {
			return -1;
		}
		return getPandValue().getId();
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CreateChick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	private boolean _DeleteChick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} else if (_CreateChick) {
			_CreateChick = false;
			createData();
		} else if (_DeleteChick) {
			_DeleteChick = false;
			delete();
			getSelectData();
		}
	}
	public String getPandRiq(JDBCcon con, String pandBm) throws SQLException  {
		Date riq = null;
		String sRiq = "";
		String sql = "select riq from pandb where bianm='" + pandBm + "'";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			riq = rs.getDate("riq");
			sRiq = DateUtil.FormatDate(riq);
		}
		rs.close();
		return sRiq;
	}
	
	//���ұ����Ӧ�ĵ糧Id
	public String getdiancId() throws SQLException  {
		JDBCcon con = new JDBCcon();
		String diancId ="";
		String sql = "select diancxxb_id from pandb where bianm =('" + getPandbm() + "')";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			diancId = rs.getString("diancxxb_id");
		}
		rs.close();
		return diancId;
	}
	
	public double getShangYKC(JDBCcon con, String riq, Visit visit) throws SQLException {
		double shangykc = 0.0;
		
		//�ж��Ƿ��Ǵ��ƹ����̵㱨��
		boolean isDtgj =false;
		String pSql ="";
		pSql="select zhi from xitxxb where mingc='���ƹ����̵㱨��' and zhuangt =1";
		ResultSet prsl=con.getResultSet(pSql);
		if(prsl.next()){
			String pZhi=prsl.getString("zhi");
			if(pZhi.equals("����ú")){
				isDtgj=true;
			}else{
				isDtgj=false;
			}
		}
				
		int day=0;
		day = DateUtil.getDay(DateUtil.getDate(riq));
		String pandDate = "";
		//�ж��̵�ʱ�䣬������ϰ�����ú������ȡ���£������15��֮����ú������ȡ�Ա��¡�
		if(day <15){
			pandDate = DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(riq),-1,DateUtil.AddType_intMonth));
		}else{
			pandDate = riq;
		}
		
		if(isDtgj){
			String sql = "select Round_new(sum(yuemjc),0) as kuc from diaor16bb where riq=first_day(add_months(to_date('" + pandDate + "','yyyy-mm-dd'),-1))"
			+ " and fenx='����' and diancxxb_id=" +getdiancId();
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				shangykc = rs.getDouble("kuc");
			} else {
				riq = String.valueOf((Integer.parseInt(riq.substring(5,7)) - 1));
				setMsg(riq + "�¿��Ϊ0��");
			}
			rs.close();
			return shangykc;
		}else{
			String sql = "select Round_new(kuc,0) from yueshchjb where riq=first_day(add_months(to_date('" + riq + "','yyyy-mm-dd'),-1))"
					+ " and fenx='����' and diancxxb_id=" + visit.getDiancxxb_id();
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				shangykc = rs.getDouble("kuc");
			} else {
				riq = String.valueOf((Integer.parseInt(riq.substring(5,7)) - 1));
				setMsg(riq + "�¿��Ϊ0��");
			}
			rs.close();
			return shangykc;
		}
	}
	public double[] getShouHC(JDBCcon con, String riq, Visit visit) throws SQLException {
		double shouhc[] = null;
		
//		�ж��Ƿ��Ǵ��ƹ����̵㱨��
		boolean isDtgj =false;
		String pSql ="";
		pSql="select zhi from xitxxb where mingc='���ƹ����̵㱨��' and zhuangt =1";
		ResultSet prsl=con.getResultSet(pSql);
		if(prsl.next()){
			String pZhi=prsl.getString("zhi");
			if(pZhi.equals("����ú")){
				isDtgj=true;
			}else{
				isDtgj=false;
			}
		}
		
		int day=0;
		day = DateUtil.getDay(DateUtil.getDate(riq));
		String pandDate = "";
		//�ж��̵�ʱ�䣬������ϰ�����ú������ȡ���£������15��֮����ú������ȡ�Ա��¡�
		if(day <15){
			pandDate = DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(riq),-1,DateUtil.AddType_intMonth));
		}else{
			pandDate = riq;
		}
				
		if(isDtgj){
			String sql="select decode(Round_new(sum(dr.kuangfgyl),0),null,0,Round_new(sum(dr.kuangfgyl),0)) + case Round_new(sum(dr.qitsrl),0) when null then 0 else Round_new(sum(dr.qitsrl),0) end as jinm,\n"
					  +"       Round_new(sum(dr.shijhylfdy), 0) as fady,\n"
					  +"       Round_new(sum(dr.shijhylgry), 0) as gongry,\n"
					  +"       decode(Round_new(sum(dr.shijhylqty),0),null,0,Round_new(sum(dr.shijhylqty),0))+decode(Round_new(sum(dr.diaocl),0),null,0,Round_new(sum(dr.diaocl),0)) as qity,\n"
					  +"       Round_new(sum(dr.shijhylzcsh),0) as cuns,\n"
					  +"       Round_new(sum(dr.yuns),0) as yuns,\n"
					  +"       sum(0) as shuifctz\n"
					  +" from diaor16bb dr where riq between first_day(to_date('" + pandDate + "','yyyy-mm-dd'))\n"
				+ "and to_date('" + pandDate + "','yyyy-mm-dd') and fenx = '����' and diancxxb_id=" + getdiancId();
			ResultSet rs = con.getResultSet(sql);
			shouhc = new double[7];
			if (rs.next()) {
//			 0:���½�ú  1:������  2:������  3:������  4:����  5:����  6:ˮ�ֵ���
				shouhc[0] = rs.getDouble("jinm");
				shouhc[1] = rs.getDouble("fady");
				shouhc[2] = rs.getDouble("gongry");
				shouhc[3] = rs.getDouble("qity");
				shouhc[4] = rs.getDouble("cuns");
				shouhc[5] = rs.getDouble("yuns");
				shouhc[6] = rs.getDouble("shuifctz");
			} else {
				for (int i = 0; i < shouhc.length; i++) {
					shouhc[i] = 0.0;
				}
			}
			rs.close();
			return shouhc;
		}else{		
			String sql = "select Round_new(sum(dangrgm),0) as jinm,Round_new(sum(fady),0) as fady,Round_new(sum(gongry),0)as gongry,\n"
					+ "Round_new(sum(qity),0) as qity,Round_new(sum(cuns),0) as cuns,Round_new(sum(yuns),0) as yuns,Round_new(sum(shuifctz),0) as shuifctz\n"
					+ "from shouhcrbb where riq between first_day(to_date('" + riq + "','yyyy-mm-dd'))\n"
					+ "and to_date('" + riq + "','yyyy-mm-dd') and diancxxb_id=" + visit.getDiancxxb_id();
			ResultSet rs = con.getResultSet(sql);
			shouhc = new double[7];
			if (rs.next()) {
//			 0:���½�ú  1:������  2:������  3:������  4:����  5:����  6:ˮ�ֵ���
				shouhc[0] = rs.getDouble("jinm");
				shouhc[1] = rs.getDouble("fady");
				shouhc[2] = rs.getDouble("gongry");
				shouhc[3] = rs.getDouble("qity");
				shouhc[4] = rs.getDouble("cuns");
				shouhc[5] = rs.getDouble("yuns");
				shouhc[6] = rs.getDouble("shuifctz");
			} else {
				for (int i = 0; i < shouhc.length; i++) {
					shouhc[i] = 0.0;
				}
			}
			rs.close();
			return shouhc;
		}
	}
	
	public double getRijcml(JDBCcon con, String riq, Visit visit) throws SQLException {
		double rijcm = 0.0;
		int day=0;
		
		String sql ="";
		day=DateUtil.getDay(DateUtil.getDate(riq));
		//˵��,�����ú������15��֮ǰ,����ƽ����ú�������ھ����ϸ��µ�ȫ��,�����ú������15��֮��,����ƽ����ú��
		//�����ھ����³�����ú����
		if(day>15){
			 sql =
				"select round_new(sum(s.kuc)/count(*),2) as rijkc\n" +
				"from shouhcrbb  s where s.riq>=first_day(to_date('"+riq+"','yyyy-mm-dd'))\n" + 
				" and s.riq<=to_date('"+riq+"','yyyy-mm-dd')\n" + 
				" and s.diancxxb_id="+ visit.getDiancxxb_id();
		}else{
			 sql =
					"select round_new(sum(s.kuc)/count(*),2) as rijkc\n" +
					"from shouhcrbb  s where s.riq>=add_months(first_day(to_date('"+riq+"','yyyy-mm-dd')),-1)\n" + 
					" and s.riq<=add_months(last_day(to_date('"+riq+"','yyyy-mm-dd')),-1)\n" + 
					" and s.diancxxb_id="+ visit.getDiancxxb_id();
		}
	
		ResultSet rs = con.getResultSet(sql);
		
		if (rs.next()) {

			rijcm= rs.getDouble("rijkc");
		
		}
		rs.close();
		return rijcm;
	}
	public double getShiJKC(JDBCcon con, String bianm) throws SQLException {
		double cunml = 0.0;
		double qitcm = 0.0;
		String sql = "select Round_new(sum(cunml),0) as cunml from pandtjb,pandb where pandb_id=pandb.id and bianm='" + bianm + "'";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			cunml = rs.getDouble("cunml");
		}
		rs.close();
		sql = "select Round_new(sum(cunml),0) as qitcm from pandwzcmb,pandb where pandb_id=pandb.id and bianm='" + bianm + "' group by pandb_id";
		rs = con.getResultSet(sql);
		if (rs.next()) {
			qitcm = rs.getDouble("qitcm");
		}
		rs.close();
		return cunml + qitcm;
	}
	public void createData() {	
		String sql = "";
		String riq = "";
		String pandbm = getPandbm();
		long pandid = getPandbID();
		double shangykc = 0.0;
		double zhangmkc = 0.0;
		double shijkc = 0.0;
		double panyk = 0.0;
		double[] shouhc = null;
		double rijcml=0.0;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		try {
//			��ú����
			riq = getPandRiq(con, pandbm);
			if (riq.equals("") || riq == null) {
				setMsg("�̵�����Ϊ�գ�");
				return;
			}
//			���¿��
			shangykc = getShangYKC(con, riq, visit);

//			�պô��������
			shouhc = getShouHC(con, riq, visit);
//			ʵ�ʿ��
			shijkc = getShiJKC(con, pandbm);
//			������
//			 0:���½�ú  1:������  2:������  3:������  4:����  5:����  6:ˮ�ֵ���
			zhangmkc = shangykc + shouhc[0] - shouhc[1] - shouhc[2]- shouhc[3]- shouhc[4]- shouhc[5] + shouhc[6];
//			��ӯ��
			panyk = shijkc - zhangmkc;
//          �վ���ú��
			rijcml=getRijcml(con, riq,visit);
//			ɾ�� pandzmm ����
			sql = "delete from pandzmm where pandb_id=" + pandid;
			con.getDelete(sql);
//			���� pandzmm ���� 
			sql = "insert into pandzmm(id,pandb_id,benyjm,fadh,gongrh,feiscy,qity,diaocl,cuns,yuns,shuifc,zhangmkc,shijkc,panyk,pidcqyhml,rucmpjsf,rulmpjsf,meidpjsf,ripjcml,meiccmpjzl,pandhsm,pandhhm) values ("
				+ "getnewid(" + visit.getDiancxxb_id() + "),"
				+ pandid + ","
				+ shouhc[0] + ","
				+ shouhc[1] + ","
				+ shouhc[2] + ","
				+ "0,"
				+ shouhc[3] + ","
				+ "0,"
				+ shouhc[4] + ","
				+ shouhc[5] + ","
				+ shouhc[6] + ","
				+ zhangmkc + ","
				+ shijkc + ","
				+ panyk  + ","
				+"0.0,0.0,0.0,0.0,"+rijcml+",0.0,0,0"
				+ ")";
			con.getInsert(sql);
			con.commit();
		} catch(Exception e) {
			con.rollBack();
			setMsg("��������ʧ�ܣ�");
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	public void delete() {
		String sSql = "";
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			String id = rsl.getString("id");
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Zhangmm,
					"pandzmm",id);
		}
		sSql = "delete from pandzmm  where pandb_id=" + getPandbID();
		flag = con.getDelete(sSql);
		if (flag == -1) {
			setMsg("ɾ������ʧ�ܣ�");
		}
		con.Close();
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		con.setAutoCommit(false);
		ResultSetList rsl = null;
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandzmm.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//�����޸Ĳ���ʱ�����־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Zhangmm,
					"pandzmm",id+"");
			sSql = "update pandzmm set "
				+ " pandb_id=" + getPandbID() + ","					
				+ " benyjm=" + rsl.getDouble("benyjm") + ","
				+ " fadh=" + rsl.getDouble("fadh") + ","
				+ " gongrh=" + rsl.getDouble("gongrh") + ","
				+ " feiscy=" + rsl.getDouble("feiscy") + ","
				+ " qity=" + rsl.getDouble("qity") + ","
				+ " diaocl=" + rsl.getDouble("diaocl") + ","
				+ " cuns=" + rsl.getDouble("cuns") + ","
				+ " yuns=" + rsl.getDouble("yuns") + ","
				+ " shuifc=" + rsl.getDouble("shuifc") + ","
				+ " zhangmkc=" + rsl.getDouble("zhangmkc") + ","
				+ " shijkc=" + rsl.getDouble("shijkc") + ","
				+ " panyk=" + rsl.getDouble("panyk")+ ","
				
				+ " pidcqyhml=" + rsl.getDouble("pidcqyhml")+ ","
				+ " rucmpjsf=" + rsl.getDouble("rucmpjsf")+ ","
				+ " rulmpjsf=" + rsl.getDouble("rulmpjsf")+ ","
				+ " meidpjsf=" + rsl.getDouble("meidpjsf")+ ","
				+ " ripjcml=" + rsl.getDouble("ripjcml")+ ","
				+ " meiccmpjzl=" + rsl.getDouble("meiccmpjzl")+ ","
				+ " pandhsm=" + rsl.getDouble("pandhsm")+ ","
				+ " pandhhm=" + rsl.getDouble("pandhhm")
				+ " where id=" + id;
//			System.out.println(sSql);
			flag = con.getUpdate(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
			}
		}
		con.commit();
		con.Close();
	}
	public void getSelectData() {
		String sSql = "";
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		boolean isShowQit=false;
		ResultSetList rsl;
		sSql="select zhi from xitxxb x where x.mingc='�̵�����ú�Ƿ���ʾ����ָ��' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		 rsl = con.getResultSetList(sSql);
		 if(rsl.next()){
			 String  qitzb=rsl.getString("zhi");
			 if(qitzb.equals("��")){
				 isShowQit=true;
			 }else{
				 isShowQit=false;
			 }
		 }
		
		String pandriq = null;
		double shangykc = 0;
		try {
			if(getPandbm().equals(null)||getPandbm().equals("")){
				shangykc = 0;
			}else{
				pandriq = this.getPandRiq(con, getPandbm());
				if(!pandriq.equals(null)||!pandriq.equals("")){
					shangykc = this.getShangYKC(con, pandriq, visit);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sSql = "select pandzmm.id,Round_new("+ shangykc +",0) as shangykc,Round_new(benyjm,0) as benyjm,Round_new(fadh,0) as fadh,Round_new(gongrh,0) as gongrh,Round_new(feiscy,0) as feiscy,Round_new(qity,0) as qity,Round_new(diaocl,0) as diaocl,Round_new(cuns,0) as cuns,Round_new(yuns,0) as yuns,Round_new(shuifc,0) as shuifc," 
			+	"Round_new(zhangmkc,0) as zhangmkc,Round_new(shijkc,0) as shijkc,Round_new(panyk,0) as panyk,Round_new(pandhsm,0) as pandhsm,Round_new(pandhhm,0) as pandhhm,Round_new(pidcqyhml,0) as pidcqyhml,Round_new(rucmpjsf,0) as rucmpjsf,Round_new(rulmpjsf,0) as rulmpjsf,Round_new(meidpjsf,0) as meidpjsf,Round_new(ripjcml,0) as ripjcml,Round_new(meiccmpjzl,0) as meiccmpjzl\n" 
			+ " from pandzmm,pandb\n"
			+ " where pandb_id = pandb.id  and pandb.bianm='" + getPandbm() + "'";
		 rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("shangykc").setHeader("���¿��");
		egu.getColumn("shangykc").setWidth(70);
		egu.getColumn("benyjm").setHeader("���½�ú");
		egu.getColumn("benyjm").setWidth(70);
//		egu.getColumn("benyjm").setEditor(null);
		egu.getColumn("fadh").setHeader("�����");
		egu.getColumn("fadh").setWidth(55);
		egu.getColumn("gongrh").setHeader("���Ⱥ�");
		egu.getColumn("gongrh").setWidth(55);
		egu.getColumn("feiscy").setHeader("��������");
		egu.getColumn("feiscy").setWidth(70);
		egu.getColumn("qity").setHeader("������");
		egu.getColumn("qity").setWidth(55);
		egu.getColumn("diaocl").setHeader("������");
		egu.getColumn("diaocl").setWidth(55);
		egu.getColumn("cuns").setHeader("����");
		egu.getColumn("cuns").setWidth(55);
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("yuns").setWidth(55);
		egu.getColumn("shuifc").setHeader("ˮ�ֲ����");
		egu.getColumn("shuifc").setWidth(80);
		egu.getColumn("zhangmkc").setHeader("������");
		egu.getColumn("zhangmkc").setEditor(null);
		egu.getColumn("zhangmkc").setWidth(70);
		egu.getColumn("shijkc").setHeader("ʵ�ʿ��");
		egu.getColumn("shijkc").setEditor(null);
		egu.getColumn("shijkc").setWidth(70);
		egu.getColumn("panyk").setHeader("��ӯ��");
		egu.getColumn("panyk").setEditor(null);
		egu.getColumn("panyk").setWidth(55);
		egu.getColumn("pandhsm").setHeader("�̵����ú");
		egu.getColumn("pandhsm").setWidth(80);
		egu.getColumn("pandhsm").setDefaultValue("0");
		egu.getColumn("pandhhm").setHeader("�̵���ú");
		egu.getColumn("pandhhm").setWidth(80);
		egu.getColumn("pandhhm").setDefaultValue("0");
		egu.getColumn("pidcqyhml").setHeader("Ƥ����ȫ�º�ú��");
		egu.getColumn("pidcqyhml").setWidth(110);
		egu.getColumn("rucmpjsf").setHeader("�볧úƽ��ˮ��");
		egu.getColumn("rulmpjsf").setHeader("��¯úƽ��ˮ��");
		egu.getColumn("meidpjsf").setHeader("ú��ƽ��ˮ��");
		egu.getColumn("ripjcml").setHeader("��ƽ����ú��");
		egu.getColumn("meiccmpjzl").setHeader("ú����úƽ������");
		egu.getColumn("meiccmpjzl").setWidth(110);
		
		if(!isShowQit){
			egu.getColumn("pidcqyhml").setHidden(true);
			egu.getColumn("rucmpjsf").setHidden(true);
			egu.getColumn("rulmpjsf").setHidden(true);
			egu.getColumn("meidpjsf").setHidden(true);
			egu.getColumn("ripjcml").setHidden(true);
			egu.getColumn("meiccmpjzl").setHidden(true);
		}
		
		egu.addTbarText("�̵���룺");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		gbt = new GridButton("����","function(){document.getElementById('CreateButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt);
		gbt = new GridButton("ɾ��",GridButton.ButtonType_SaveAll,"gridDiv",egu.gridColumns,"DeleteButton");
		gbt.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbt);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		StringBuffer script = new StringBuffer();
		script.append("\nvar tmpIndex = PandDropDown.getValue();\n");
		script.append("PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n");
		script.append( "gridDiv_grid.on('afteredit', function(e) {\n") 
				.append("var record = gridDiv_ds.getAt(e.row);\n")
				.append("var changeValue = eval(e.originalValue||0) - (eval(e.value||0)<0 ? 0 : eval(e.value||0));\n")
				.append("if (e.field == 'SHANGYKC') {\n")
				.append("changeValue = -changeValue;\n")
				.append("}\n")
				.append("if (e.field == 'BENYJM') {\n")
				.append("	changeValue = -changeValue;\n")
				.append("}\n")
				.append("if (e.field == 'SHUIFC') {\n")
				.append("	changeValue = -changeValue;\n")
				.append("	}\n")
				.append("var zhangmkc = eval(record.get('ZHANGMKC')||0);\n")
				.append("var shijkc = eval(record.get('SHIJKC')||0);\n")
				.append("var pdhs= eval(record.get('PANDHSM')||0);\n")
				.append("var pdhh= eval(record.get('PANDHHM')||0);\n")
				.append("if(e.field!='PANDHSM' && e.field!='PANDHHM'){\n")
				.append("zhangmkc =Math.round((zhangmkc + changeValue) * Math.pow(10,2)) / Math.pow(10,2);\n")
				.append("}\n")
				.append("var panyk = Math.round((shijkc - zhangmkc+pdhs-pdhh) * Math.pow(10,2)) / Math.pow(10,2);\n")
				.append("if (eval(e.value||0)<0) {\n")
				.append("	record.set(e.field,0);\n")
				.append("}\n")
				.append("record.set('ZHANGMKC',zhangmkc);\n")
				.append("record.set('PANYK',panyk);\n")
				.append("});");
		egu.addOtherScript(script.toString());
		setExtGrid(egu);
		con.Close();
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
		}
		init();
	}
	private void init() {
		getSelectData();
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
}
