package com.zhiren.dc.jilgl.shenh;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-01 11��20
 * �������޸�ˢ��ʱδ��������Ӧ��ú�������Ҳ������ʾ
 */
/*
 * ����:tzf
 * ʱ��:2009-7-15
 * �޸�����:��·ȡ����� ҳ��  ȡ����ҳ��ʾ.
 */
/*
 * �޸���:tzf
 * ʱ��:2009-06-19
 * ����:���ӻ����У���ʾ�������  ��ú���� �ͳ����ĺϼ�
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-24 11:57
 * �������޸�����ѡ����ˢ��ʱ���Ӧ���������
 */
/*
 * ����:����
 * ʱ�䣺2009-11-03
 * �������޸������������ϸ�л�ʱ�������͸ı������
 */
/*
 * ����:ͯ�Ҹ�
 * ʱ�䣺2010-01-25
 * �������޸���������˷���������ɵ�bug
 */
/*
 * ���ߣ�yinjm
 * ʱ�䣺2010-06-07
 * ����������Ƥ���ӵ���˼�ȡ����˹���
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-01-22
 * ������ʹ�ò����ļ�MainGlobal.getXitxx_item("����", "���������޸ĺ˶Ա�ʶ", "0", SysConstant.HEDBZ_YJJ+"")
 * 		�Ժ���ȡ�����ʱ�ĺ˶Ա�ʶ�������á�Ĭ��Ϊ�Ѽ��
 */
public class Shujsh extends BasePage implements PageValidateListener {
//	��˽������
	public static final String ALL_SH = "ALL_SH";
	public static final String ALL_QXSH = "ALL_QXSH";
	public static final String HY_SH = "HY_SH";
	public static final String HY_QXSH = "HY_QXSH";
	public static final String QY_SH = "QY_SH";
	public static final String QY_QXSH = "QY_QXSH";
	public static final String PDC_SH = "PDC_SH"; // Ƥ�������
	public static final String PDC_QXSH = "PDC_QXSH"; // Ƥ����ȡ�����
	public static final String HAIY_SH = "HAIY_SH";//���˲���
	public static final String HAIY_QXSH = "HAIY_QXSH";//���˲���
//	�����û���ʾ
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
	public String getLeix() {
		return ((Visit)this.getPage().getVisit()).getString10();
	}
	public void setLeix(String leix) {
		((Visit)this.getPage().getVisit()).setString10(leix);
	}
	public void setFahids(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
//	��������������
	public IDropDownBean getFahrqValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getFahrqModel().getOptionCount()>0) {
				setFahrqValue((IDropDownBean)getFahrqModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setFahrqValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getFahrqModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setFahrqModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setFahrqModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setFahrqModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql_yunsfs = "";
		if(ALL_SH.equals(getLeix())) {
			
		}else
			if(HY_SH.equals(getLeix())) {
				sql_yunsfs = " and yunsfsb_id = "+SysConstant.YUNSFS_HUOY;
			}else if(QY_SH.equals(getLeix())) {
				sql_yunsfs = " and yunsfsb_id = "+SysConstant.YUNSFS_QIY;
			}else if(PDC_SH.equals(getLeix())) {
				sql_yunsfs = " and yunsfsb_id = "+SysConstant.YUNSFS_Pidc;
			}else
				if(HAIY_SH.equals(getLeix())||HAIY_QXSH.equals(getLeix())) {
					sql_yunsfs = " and yunsfsb_id = "+SysConstant.YUNSFS_HaiY;//���ú��˲���
				}
		String sql_dcid = Jilcz.filterDcid(visit, "");
		String sql="";
        if(HAIY_SH.equals(getLeix())||HAIY_QXSH.equals(getLeix())){
        	int a=1; 
			if(HAIY_SH.equals(getLeix())){
				a=0;
			}
			sql="select rownum xuh,daohrq from (select distinct to_char(daohrq,'yyyy-mm-dd') daohrq from fahb where liucztb_id="+a
//			+" and  hedbz= "+SysConstant.HEDBZ_LR
			+sql_yunsfs+sql_dcid
			+" order by daohrq) ";
		}else{
		switch ((int) this.getWeizSelectValue().getId()) {
		case 1:sql = "select rownum xuh,fahrq from (select distinct to_char(fahrq,'yyyy-mm-dd') fahrq from fahb where hedbz = "+SysConstant.HEDBZ_YJJ+sql_yunsfs+sql_dcid+" order by fahrq) ";break;
		case 2:sql = "select rownum xuh,daohrq from (select distinct to_char(daohrq,'yyyy-mm-dd') daohrq from fahb where hedbz = "+SysConstant.HEDBZ_YJJ+sql_yunsfs+sql_dcid+" order by daohrq) ";break;
		case 3:
			if (PDC_SH.equals(getLeix())) {
				sql="select rownum xuh,qingcsj from(select distinct to_char(chepb.qingcsj,'yyyy-mm-dd') qingcsj from chepb,(select distinct id fahb_id from fahb where hedbz = "+SysConstant.HEDBZ_YJJ+sql_yunsfs+sql_dcid+" ) wf where chepb.fahb_id =wf.fahb_id) ";
			} else {
				sql="select rownum xuh,zhongcsj from(select distinct to_char(chepb.zhongcsj,'yyyy-mm-dd') zhongcsj from chepb,(select distinct id fahb_id from fahb where hedbz = "+SysConstant.HEDBZ_YJJ+sql_yunsfs+sql_dcid+" ) wf where chepb.fahb_id =wf.fahb_id) ";
			}
			break;
	
		}
		}
	 
		setFahrqModel(new IDropDownModel(sql));
	}
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
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
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
private boolean _RiqClick=false;
public void RiqButton(IRequestCycle cycle){
	_RiqClick=true;
}
	private boolean _RefurbishClick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	private boolean _ShenhClick = false;
	public void ShenhButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}

//    private boolean _HuitClick = false;
//    public void HuitButton(IRequestCycle cycle) {
//        _HuitClick = true;
//    }
	private void Shenh() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		List fhlist = new ArrayList();
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("����", "������㷽��", String.valueOf(v.getDiancxxb_id()), "����"));
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Shujsh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
		}
		int flag = 0;
		int ches = 0;
		double biaoz = 0;
		while(rsl.next()) {
			long meikxxb_id=0;
			String meiksql="select meikxxb_id from fahb where id="+rsl.getString("id");
			ResultSetList mkrs = con.getResultSetList(meiksql);
			if(mkrs.next()){
				meikxxb_id=mkrs.getLong("meikxxb_id");
			}
				if(HY_SH.equals(getLeix())) {
					int flg = Jilcz.Hetkzjm(con, v.getDiancxxb_id(),meikxxb_id, rsl.getString("fahrq"),SysConstant.YUNSFS_HUOY);
					if (flg == -1) {
						con.rollBack();
						con.Close();
//						WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
						setMsg("��ú��λû�ж�Ӧ�ĺ�ͬ���������ͬ���������!");
						return;
					}
				}else if(QY_SH.equals(getLeix())) {
					int flg = Jilcz.Hetkzjm(con, v.getDiancxxb_id(),meikxxb_id, rsl.getString("daohrq"),SysConstant.YUNSFS_QIY);
					if (flg == -1) {
						con.rollBack();
						con.Close();
//						WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
						setMsg("��ú��λû�ж�Ӧ�ĺ�ͬ���������ͬ���������!");
						return;
					}
				}else if(PDC_SH.equals(getLeix())) {
					int flg = Jilcz.Hetkzjm(con, v.getDiancxxb_id(),meikxxb_id, rsl.getString("daohrq"),SysConstant.YUNSFS_Pidc);
					if (flg == -1) {
						con.rollBack();
						con.Close();
//						WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
						setMsg("��ú��λû�ж�Ӧ�ĺ�ͬ���������ͬ���������!");
						return;
					}
				}else
					if(HAIY_SH.equals(getLeix())) {//���Ӻ���
						int flg = Jilcz.Hetkzjm(con, v.getDiancxxb_id(),meikxxb_id, rsl.getString("kaobrq"),SysConstant.YUNSFS_HaiY);
//						System.out.println("flg"+flg);
						if (flg == -1) {
							con.rollBack();
							con.Close();
//							WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
							setMsg("��ú��λû�ж�Ӧ�ĺ�ͬ���������ͬ���������!");
//							System.out.println("��ú��λû�ж�Ӧ�ĺ�ͬ���������ͬ���������!");
							return;
						}
					}
			
			
				if (HAIY_SH.equals(getLeix())) {

			} else {
				if (rsl.getInt("mxhedbz") == 0) {
					continue;
				}
			}
			String fahbid = rsl.getString("id");
			Jilcz.addFahid(fhlist, fahbid);
			ches += rsl.getInt("ches");
			biaoz += rsl.getDouble("biaoz");
			long diancxxb_id = v.getDiancxxb_id();
			if(v.isFencb()){
				diancxxb_id = getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"));
			}
			String daohrq = "";
			if(HAIY_SH.equals(getLeix())){
				daohrq = rsl.getString("kaobrq");
			}else{
				daohrq=rsl.getString("daohrq");
			}
			String sql = "select * from xitxxb where mingc = '����ȷ������' and zhi = '���' and leib = '����' and zhuangt = 1 and diancxxb_id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
			if(con.getHasIt(sql)){
				daohrq = DateUtil.FormatDate(new Date());
			}
			if(HAIY_SH.equals(getLeix())){
				sb.append("update fahb set liucztb_id =1,hedbz = ").append("1");
				sb.append(",daohrq = ").append(DateUtil.FormatOracleDate(daohrq));
				
				sb.append(" where id = ").append(fahbid);
				flag = con.getUpdate(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+"SQL:"+sb);
					setMsg(ErrorMessage.ShujshH002);
					return;
				}
			}else{
			if(rsl.getInt("mxhedbz") == rsl.getInt("hedbz")){
				sb.append("update chepb set hedbz = ").append(SysConstant.HEDBZ_YSH).append(" where fahb_id = ").append(fahbid);
				flag = con.getUpdate(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+"SQL:"+sb);
					setMsg(ErrorMessage.ShujshH001);
					return;
				}
				sb.delete(0, sb.length());
				sb.append("update fahb set liucztb_id =1,hedbz = ").append(SysConstant.HEDBZ_YSH);
	//			�ж��Ƿ�����㵽��
				sb.append(",daohrq = ").append(DateUtil.FormatOracleDate(daohrq));
				
				sb.append(" where id = ").append(fahbid);
				flag = con.getUpdate(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+"SQL:"+sb);
					setMsg(ErrorMessage.ShujshH002);
					return;
				}
			}else{
				
				Jilcz.addFahid(fhlist, fahbid);
				String newfhid = Jilcz.CopyFahb(con,fahbid,v.getDiancxxb_id());
				Jilcz.addFahid(fhlist, newfhid);
				sql = "update chepb set fahb_id=" + newfhid +",hedbz= "+
				SysConstant.HEDBZ_YSH+" where fahb_id=" + fahbid + 
				" and hedbz =" + SysConstant.HEDBZ_YJJ;
				flag = con.getUpdate(sql);
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail);
					setMsg(ErrorMessage.ShujshH001);
					return;
				}
				flag = Jilcz.updateFahb(con, newfhid, null);
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail);
					setMsg(ErrorMessage.ShujshH001);
					return;
				}
//				����ǰ����μ������������
				if(!isDancYuns) {
					flag = Jilcz.CountFahbYuns(con, newfhid);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail);
						setMsg(ErrorMessage.ShujshH002);
						return;
					}
				}
				sb.delete(0, sb.length());
				sb.append("update fahb set liucztb_id =1,hedbz = ").append(SysConstant.HEDBZ_YSH);
//				�ж��Ƿ�����㵽��
				sb.append(",daohrq = ").append(DateUtil.FormatOracleDate(daohrq));
				sb.append(" where id = ").append(newfhid);
				flag = con.getUpdate(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+"SQL:"+sb);
					setMsg(ErrorMessage.ShujshH002);
					return;
				}
				flag = Jilcz.updateFahb(con, fahbid, null);
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail);
					setMsg(ErrorMessage.ShujshH001);
					return;
				}
			}
			}
//			AutoCreateShouhcrb.Create(con, diancxxb_id, DateUtil.getDate(daohrq));
			sb.delete(0, sb.length());
		}
		con.commit();
		con.Close();
		Chengbjs.CountChengb(v.getDiancxxb_id(), fhlist);
		if(HAIY_SH.equals(getLeix())){
			
			setMsg("��˳ɹ���������������� "+biaoz+" �֡�");
		}else{
			setMsg("��˳ɹ���������� "+ ches + " ������Ʊ�� "+biaoz+" �֡�");
		}
	}
	
	private boolean _QuxshClick = false;
	public void QuxshButton(IRequestCycle cycle) {
		_QuxshClick = true;
	}
	
	private void Quxsh() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Shujsh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
		}
		int flag = 0;
		int ches = 0;
		double biaoz = 0;
		boolean hasfail = false;
		if(HAIY_QXSH.equals(getLeix())){
			while(rsl.next()) {
				if(rsl.getInt("hedbz")>SysConstant.HEDBZ_YSH) {
					hasfail = true;
					continue;
				}
				if(rsl.getString("xiemkssj").equals("")||rsl.getString("xiemkssj")==null){
					
					setMsg("жú��ʼʱ��û����д�������");
					return;
				}
				if(rsl.getString("xiemjssj").equals("")||rsl.getString("xiemjssj")==null){
					
					setMsg("жú����ʱ��û����д�������");
					return;
				}

				String fahbid = rsl.getString("id");
				ches += rsl.getInt("ches");
				biaoz += rsl.getDouble("biaoz");
//				sb.append("update chepb set hedbz = ").append(SysConstant.HEDBZ_YJJ).append(" where fahb_id = ").append(fahbid);
//				flag = con.getUpdate(sb.toString());
//				if(flag == -1) {
//					con.rollBack();
//					con.Close();
//					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
//							+"SQL:"+sb);
//					setMsg(ErrorMessage.Shujsh001);
//					return;
//				}
				sb.delete(0, sb.length());
				String shzt=MainGlobal.getXitxx_item("����", "���������޸ĺ˶Ա�ʶ", "0", SysConstant.HEDBZ_YJJ+"");
				sb.append("update fahb set liucztb_id =0,hedbz = ").append(shzt).append(" where id = ").append(fahbid);
				flag = con.getUpdate(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+"SQL:"+sb);
					setMsg(ErrorMessage.Shujsh002);
					return;
				}
				sb.delete(0, sb.length());
			}
			
		}else{
			
			while(rsl.next()) {
				if(rsl.getInt("hedbz")>SysConstant.HEDBZ_YSH) {
					hasfail = true;
					continue;
				}
				String fahbid = rsl.getString("id");
				ches += rsl.getInt("ches");
				biaoz += rsl.getDouble("biaoz");
				sb.append("update chepb set hedbz = ").append(SysConstant.HEDBZ_YJJ).append(" where fahb_id = ").append(fahbid);
				flag = con.getUpdate(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+"SQL:"+sb);
					setMsg(ErrorMessage.Shujsh001);
					return;
				}
				sb.delete(0, sb.length());
				sb.append("update fahb set liucztb_id =0,hedbz = ").append(SysConstant.HEDBZ_YJJ).append(" where id = ").append(fahbid);
				flag = con.getUpdate(sb.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
							+"SQL:"+sb);
					setMsg(ErrorMessage.Shujsh002);
					return;
				}
				sb.delete(0, sb.length());
			}
		}
		con.commit();
		con.Close();
		if(HAIY_QXSH.equals(getLeix())){
			
			if(hasfail) {
				setMsg("��������δ�ɹ�ȡ����ˣ����ɹ�ȡ���������� "+biaoz+" �֡�");
			}else {
				setMsg("ȡ����˳ɹ�����ȡ���������� "+biaoz+" �֡�");
			}
		}else{
			
			if(hasfail) {
				setMsg("��������δ�ɹ�ȡ����ˣ����ɹ�ȡ���� "+ ches + " ������Ʊ�� "+biaoz+" �֡�");
			}else {
				setMsg("ȡ����˳ɹ�����ȡ���� "+ ches + " ������Ʊ�� "+biaoz+" �֡�");
			}
		}
		
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ���������в鿴��");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer fahids = new StringBuffer();
		while(rsl.next()) {
			fahids.append(rsl.getString("id")).append(",");
		}
		if(fahids.length()>1) {
			fahids.deleteCharAt(fahids.length()-1);
		}
		setFahids(fahids.toString());
		cycle.activate("Shujshcp");
	}
//    public void huit(IRequestCycle cycle){
//        if(getChange()==null || "".equals(getChange())) {
//            setMsg("��ѡ��һ���������л��ˣ�");
//            return;
//        }
//        ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
//        String fahids ="";
//        while(rsl.next()) {
//            fahids+=rsl.getString("id")+",";
//        }
//        fahids=fahids.substring(0,fahids.length()-1);
//       String sql="begin delete from chepb where fahb_id in("+fahids+"); " +
//               "update  chepbtmp set fahb_id =0,fahbtmp_id=0 where chec in (select chec from fahb where id in ("+fahids+") );"+
//               "delete from fahb where id in ("+fahids+");end;";
//       JDBCcon con =new JDBCcon();
//       int flag=con.getUpdate(sql);
//       con.commit();
//       con.Close();
//        if(flag!=-1){
//            setMsg("���˳ɹ���");
//        }else{
//            setMsg("����ʧ��");
//        }
//    }
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
		}
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
			setFahrqModel(null);
			setFahrqValue(null);
		}
		if (_QuxshClick) {
			_QuxshClick = false;
			Quxsh();
			setFahrqModel(null);
			setFahrqValue(null);
		}
		if(_RiqClick){
			_RiqClick=false;
			setFahrqModel(null);
			setFahrqValue(null);
		}
//        if (_HuitClick) {
//            _HuitClick = false;
//            huit(cycle);
//        }
		initGrid();
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}

	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb1 = new StringBuffer();
		boolean isshenh = true;
		String zhongcsj="";
		String chepb="";
		if(ALL_SH.equals(getLeix())) {
			
		}else if(HY_SH.equals(getLeix())) {
			sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
		}else if(QY_SH.equals(getLeix())) {
			sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY);
		}else if(PDC_SH.equals(getLeix())) {
			sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_Pidc);
		}else if(ALL_QXSH.equals(getLeix())) {
			isshenh = false;
		}else if(HY_QXSH.equals(getLeix())) {
			isshenh = false;
			sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
		}else if(QY_QXSH.equals(getLeix())) {
			isshenh = false;
			sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY);
		}else if(PDC_QXSH.equals(getLeix())) {
			isshenh = false;
			sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_Pidc);
		}else if(HAIY_QXSH.equals(getLeix())) {
			isshenh = false;
//			sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_Pidc);
		}
		StringBuffer sbsql = new StringBuffer("");
		if(isshenh) {
			
         if(HAIY_SH.equals(getLeix())){
				
     		
     		sbsql.append("select fahb.id,fahb.yundh,luncxxb.mingc as luncxxb_id, \n");
     		sbsql.append("       gongysb.mingc as gongysb_id,\n");
//     		sbsql.append("       meikxxb_id, \n");
     		sbsql.append("       meikxxb.mingc as meikxxb_id, \n");
     		sbsql.append("       yunsfsb.mingc as yunsfsb_id, \n");
     		sbsql.append("       fahb.chec, \n");//zhilb.huaybh,
     		sbsql.append("       hetb.hetbh hetb_id, \n");
     		//sbsql.append("       fahb.faz_id, \n");
     		sbsql.append("       chezxxb.mingc as faz_id, \n");
     		sbsql.append(" DECODE ((SELECT cz.quanc\n ");
     		sbsql.append(" FROM chezxxb cz, diancdzb dz\n");
     		sbsql.append(" WHERE cz.ID = dz.chezxxb_id\n");
     		sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id),NULL, '��ѡ��',\n");
     		sbsql.append(" (SELECT cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id \n");
     		sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id)) AS daoz_id,\n");
     		//sbsql.append("   	 fahb.pinzb_id, \n");
     		sbsql.append("       pinzb.mingc as pinzb_id, \n");
     		sbsql.append("       luncxxb.dunw,meicb.mingc as meicb_id,fahb.duowmc, \n");
     		sbsql.append("       fahb.maoz,fahb.piz,fahb.biaoz,fahb.sanfsl,\n");//fahb.jingz,
     		//sbsql.append("       fahb.maoz, \n");
     		//sbsql.append("   to_char(daohrq,'YYYY-MM-DD hh24:mi:ss')daohrq, \n");
     		sbsql.append("   fahrq, \n");// 
     		sbsql.append("   kaobrq, \n");
     		sbsql.append("   to_char(xiemkssj,'YYYY-MM-DD hh24:mi:ss')xiemkssj, \n");
     		sbsql.append("   to_char(xiemjssj,'YYYY-MM-DD hh24:mi:ss')xiemjssj, \n");
     		sbsql.append("   to_char(zhuanggkssj,'YYYY-MM-DD hh24:mi:ss')zhuanggkssj, \n");
     		sbsql.append("   to_char(zhuanggjssj,'YYYY-MM-DD hh24:mi:ss')zhuanggjssj \n");
//     		sbsql.append("   luncxxb.mingc as huocmc, \n");
     		sbsql.append("  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb,meicb \n");
     		sbsql.append(" where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+) \n");
     		sbsql.append("   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+) \n");
     		sbsql.append("   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+) and fahb.liucztb_id=0 \n");
     		sbsql.append("   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)  \n");
     		sbsql.append("   and yunsfsb.mingc='����' and fahb.jiesb_id=0 \n");
     		sbsql.append(" and fahb.meicb_id=meicb.id(+) \n");
//     		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+beginriq+"'  \n");
//     		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+endriq+"'  \n");
     		
     		if((int) this.getWeizSelectValue().getId()==1){
     			sbsql.append(" and fahb.fahrq = ").append(DateUtil.FormatOracleDate(getFahrqValue().getValue()));
     		}else{
     			sbsql.append(" and fahb.daohrq = ").append(DateUtil.FormatOracleDate(getFahrqValue().getValue()));
     		}
     		sbsql.append("order by fahb.daohrq \n");
//				sb1.append(" and (f.mxhedbz = ").append(SysConstant.HEDBZ_LR)
//				.append(" or f.hedbz=").append(SysConstant.HEDBZ_LR).append(")");
     		
			}else{
				
			sb1.append(" and (f.mxhedbz = ").append(SysConstant.HEDBZ_YJJ)
			.append(" or f.hedbz=").append(SysConstant.HEDBZ_YJJ).append(")");
			}
			if(getFahrqValue()!=null) {
				
				switch ((int) this.getWeizSelectValue().getId()) {
				case 1:
					sb1.append(" and f.fahrq = ").append(
							DateUtil.FormatOracleDate(getFahrqValue()
									.getValue())).append("\n");
					break;// ��������
				case 2:
					sb1.append(" and f.daohrq = ").append(
							DateUtil.FormatOracleDate(getFahrqValue()
									.getValue())).append("\n");
					break;// ��������
				case 3:
					if (PDC_SH.equals(getLeix())) {
						sb1.append("\n");
						zhongcsj = "and f.id=che.fahb_id and qingcsj >="+ DateUtil.FormatOracleDate(getFahrqValue()
								.getValue())+" and qingcsj < "+DateUtil.FormatOracleDate(getFahrqValue()
										.getValue())+" + 1\n";
					} else {
						sb1.append("\n");
						zhongcsj = "and f.id=che.fahb_id and zhongcsj >="+ DateUtil.FormatOracleDate(getFahrqValue()
								.getValue())+" and zhongcsj < "+DateUtil.FormatOracleDate(getFahrqValue()
										.getValue())+" + 1\n";
					}
					chepb=",chepb che";
					break;// ��������
				default:
					sb1.append(" and f.fahrq = ").append(
							DateUtil.FormatOracleDate(getFahrqValue()
									.getValue())).append("\n");
				}
//				sb1.append(" and f.fahrq = ").append(DateUtil.FormatOracleDate(getFahrqValue().getValue())).append("\n");
			}
		}else {
			if(HAIY_QXSH.equals(getLeix())){
			sbsql.append("select fahb.id,fahb.yundh,luncxxb.mingc as luncxxb_id, \n");
     		sbsql.append("       gongysb.mingc as gongysb_id,\n");
//     		sbsql.append("       meikxxb_id, \n");
     		sbsql.append("       meikxxb.mingc as meikxxb_id, \n");
     		sbsql.append("       yunsfsb.mingc as yunsfsb_id, \n");
     		sbsql.append("       fahb.chec, \n");//zhilb.huaybh,
     		sbsql.append("       hetb.hetbh hetb_id, \n");
     		//sbsql.append("       fahb.faz_id, \n");
     		sbsql.append("       chezxxb.mingc as faz_id, \n");
     		sbsql.append(" DECODE ((SELECT cz.quanc\n ");
     		sbsql.append(" FROM chezxxb cz, diancdzb dz\n");
     		sbsql.append(" WHERE cz.ID = dz.chezxxb_id\n");
     		sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id),NULL, '��ѡ��',\n");
     		sbsql.append(" (SELECT cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id \n");
     		sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id)) AS daoz_id,\n");
     		//sbsql.append("   	 fahb.pinzb_id, \n");
     		sbsql.append("       pinzb.mingc as pinzb_id, \n");
     		sbsql.append("       luncxxb.dunw,meicb.mingc as meicb_id,fahb.duowmc, \n");
     		sbsql.append("       fahb.maoz,fahb.piz,fahb.biaoz,fahb.sanfsl,\n");//fahb.jingz,
     		//sbsql.append("       fahb.maoz, \n");
     		//sbsql.append("   to_char(daohrq,'YYYY-MM-DD hh24:mi:ss')daohrq, \n");
     		sbsql.append("   fahrq, \n");// 
     		sbsql.append("   kaobrq, \n");
     		sbsql.append("   to_char(xiemkssj,'YYYY-MM-DD hh24:mi:ss')xiemkssj, \n");
     		sbsql.append("   to_char(xiemjssj,'YYYY-MM-DD hh24:mi:ss')xiemjssj--, \n");
     		sbsql.append("   --to_char(zhuanggkssj,'YYYY-MM-DD hh24:mi:ss')zhuanggkssj, \n");
     		sbsql.append("  -- to_char(zhuanggjssj,'YYYY-MM-DD hh24:mi:ss')zhuanggjssj \n");
//     		sbsql.append("   luncxxb.mingc as huocmc, \n");
     		sbsql.append("  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb,meicb \n");
     		sbsql.append(" where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+) \n");
     		sbsql.append("   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+) \n");
     		sbsql.append("   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+) and fahb.liucztb_id=1 \n");
     		sbsql.append("   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)  \n");
     		sbsql.append("   and yunsfsb.mingc='����' and fahb.jiesb_id=0 \n");
     		sbsql.append(" and fahb.meicb_id=meicb.id(+) \n");
     		if((int) this.getWeizSelectValue().getId()==1){
     			sbsql.append(" and fahb.fahrq = ").append(DateUtil.FormatOracleDate(getRiq()));//getFahrqValue().getValue()
     		}else{
     			sbsql.append(" and fahb.daohrq = ").append(DateUtil.FormatOracleDate(getRiq()));//getFahrqValue().getValue()
     		}
     		sbsql.append("order by fahb.daohrq \n");
			}
			switch ((int) this.getWeizSelectValue().getId()) {
			case 1:sb1.append(" and f.fahrq = ").append(DateUtil.FormatOracleDate(getRiq())); break;// ��������
			case 2:sb1.append(" and f.daohrq = ").append(DateUtil.FormatOracleDate(getRiq())); break;// ��������
			case 3:
				zhongcsj="and f.id=che.fahb_id and zhongcsj >="+ DateUtil.FormatOracleDate(getRiq())+" and zhongcsj < "+DateUtil.FormatOracleDate(getRiq())+" + 1\n";
				chepb=",chepb che";
				break;// ��������
			default: ;
			}
//			sb1.append(" and f.daohrq = ").append(DateUtil.FormatOracleDate(getRiq()))
			sb1.append("\nand f.hedbz >=").append(SysConstant.HEDBZ_YSH);
		}
		sb1.append(Jilcz.filterDcid(visit, "f"));
		
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		StringBuffer hj=new StringBuffer();
		
		String huiz=" select * from xitxxb where mingc='���������ʾ����' and zhi='��' and zhuangt=1 and leib='����' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rshj=con.getResultSetList(huiz);
		
		boolean flag=false;
		String laimsl="";//��ú�����ֶ��Ƿ���ʾ
		if(rshj.next()){
			laimsl=" f.laimsl laimsl,";
			flag=true;
		}
		
		ExtGridUtil egu = new ExtGridUtil();
		ResultSetList rsl = new ResultSetList();
		
		
		if (getLeix().equals(PDC_SH)) {
//			Ƥ�������
			
			String sql = 
				"select f.id,\n" +
				"       f.fahrq,\n" + 
				"       f.daohrq,\n" + 
				"       f.maoz,\n" + 
				"       f.biaoz,\n" + 
				"       f.yingd - f.yingk kuid,\n" + 
				"       g.mingc gys,\n" + 
				"       m.mingc mk,\n" + 
				"       p.mingc pz,\n" + 
				"       j.mingc kj,\n" +
				"       f.mxhedbz\n" + 
				"  from (select fh.*,\n" + 
				"               (select nvl(max(hedbz), 0) hedbz\n" + 
				"                  from chepb c\n" + 
				"                 where c.fahb_id = fh.id) mxhedbz\n" + 
				"          from fahb fh) f, gongysb g, meikxxb m, pinzb p, jihkjb j\n" + chepb +
				" where f.gongysb_id = g.id\n" + 
				"   and f.meikxxb_id = m.id\n" + 
				"   and f.pinzb_id = p.id\n" + 
				"   and f.jihkjb_id = j.id\n" + sb1 + zhongcsj;
			
			rsl = con.getResultSetList(sql.toString());
			
			if(rsl == null){
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			egu = new ExtGridUtil("gridDiv", rsl);
			
			egu.setWidth(Locale.Grid_DefaultWidth);
			egu.addPaging(25);
			egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
			egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
			
			egu.getColumn("gys").setHeader(Locale.gongysb_id_fahb);
			egu.getColumn("gys").setWidth(120);
			egu.getColumn("mk").setHeader(Locale.meikxxb_id_fahb);
			egu.getColumn("mk").setWidth(150);
			egu.getColumn("pz").setHeader(Locale.pinzb_id_fahb);
			egu.getColumn("pz").setWidth(70);
			egu.getColumn("fahrq").setHeader(Locale.Pidc_fahrq);
			egu.getColumn("fahrq").setWidth(80);
			egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
			egu.getColumn("daohrq").setWidth(80);
			egu.getColumn("kj").setHeader(Locale.jihkjb_id_fahb);
			egu.getColumn("kj").setWidth(70);
			egu.getColumn("maoz").setHeader(Locale.Pidc_maoz);
			egu.getColumn("maoz").setWidth(70);
			egu.getColumn("biaoz").setHeader(Locale.Pidc_biaoz);
			egu.getColumn("biaoz").setWidth(70);
			egu.getColumn("kuid").setHeader(Locale.kuid_chepb);
			egu.getColumn("kuid").setWidth(70);
			egu.getColumn("mxhedbz").setHidden(true);
			egu.getColumn("mxhedbz").editor=null;
			
		} else {
			
//			���ˡ��������
			
			sb.append("select distinct f.id,");
			if(visit.isFencb()){
				sb.append("  d.mingc diancxxb_id,");
			}
			sb.append(" g.mingc gongysb_id, m.mingc meikxxb_id, \n")
			.append("p.mingc pinzb_id,"+laimsl+" f.fahrq, f.daohrq, y.mingc yunsfsb_id, \n")
			.append("(select fc.mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n")
			.append("(select dc.mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id, \n")
			.append("f.mxhedbz,decode(f.mxhedbz,f.hedbz,'��ȫ','δ��ȫ') jjzt,\n")
			.append("f.jingz, f.biaoz, f.sanfsl, f.zongkd, f.ches, f.chec, j.mingc jihkjb_id,f.hedbz \n")
			.append("from (select fh.*,(select nvl(max(hedbz),0) hedbz from chepb c where c.fahb_id = fh.id) mxhedbz from fahb fh ) f, gongysb g, meikxxb m, pinzb p, jihkjb j,yunsfsb y, diancxxb d"+chepb+"\n")
			.append("where f.gongysb_id = g.id(+) and f.meikxxb_id = m.id(+) and f.yunsfsb_id=y.id \n")
			.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id and f.diancxxb_id = d.id\n");
			if(visit.isFencb()){
				sb.append("and diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n");
			}
			sb.append(sb1+"\n");
			sb.append(zhongcsj);
			
			if(flag){//ϵͳ���Ѿ������� ��Ҫ��ʾ������
				
				

				hj.append("select -1 id,");
				if(visit.isFencb()){
					hj.append("  '' diancxxb_id,");
				}
				hj.append(" '�ϼ�' gongysb_id, '' meikxxb_id, \n")
				.append("'' pinzb_id,sum(z.laimsl) laimsl,null fahrq, null daohrq, '' yunsfsb_id, \n")
				.append("'' faz_id, \n")
				.append("'' daoz_id, \n")
				.append("null mxhedbz,'' jjzt,\n")
				.append("sum(jingz) jingz, sum(biaoz) biaoz, sum(sanfsl) sanfsl, sum(zongkd) zongkd, sum(z.ches) ches , '' chec, '' jihkjb_id, null hedbz \n")
				.append("from ("+sb.toString()+") z");
				
				
				sb.append("\n union \n");
				
				sb.append(hj.toString());
				
				sb=new StringBuffer(" select *   from ("+sb.toString()+") where laimsl is not null  order by daohrq");
			}
			if(HAIY_SH.equals(getLeix())||HAIY_QXSH.equals(getLeix())){
				
				if(sbsql.toString().length()>13){
					
					rsl = con.getResultSetList(sbsql.toString());
				}
			}else{
			rsl = con.getResultSetList(sb.toString());
			}
			if(rsl == null){
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			egu = new ExtGridUtil("gridDiv", rsl);
			//����ҳ����
			egu.setWidth(Locale.Grid_DefaultWidth);
//			egu.setHeight("bodyHeight");
			egu.addPaging(0);
			egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
			//���ö�ѡ��
			egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
			//����ÿҳ��ʾ����
			if(HY_QXSH.equals(getLeix())){// ��·ȡ�����   ȡ����ҳ��ʾ
				egu.addPaging(-1);
			}else{
				egu.addPaging(25);
			}
			if(HAIY_SH.equals(getLeix())||HAIY_QXSH.equals(getLeix())){
				
			}else{
				
			egu.getColumn("id").setHidden(true);
			egu.getColumn("id").editor=null;
			egu.getColumn("hedbz").setHidden(true);
			egu.getColumn("hedbz").editor=null;
			egu.getColumn("mxhedbz").setHidden(true);
			egu.getColumn("mxhedbz").editor=null;
			}
			if(visit.isFencb()) {
				ComboBox dc= new ComboBox();
				egu.getColumn("diancxxb_id").setEditor(dc);
				dc.setEditable(true);
				String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
				egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
				egu.getColumn("diancxxb_id").setWidth(70);
//				egu.getColumn("diancxxb_id").setEditor(null);
			}
			if(HAIY_SH.equals(getLeix())||HAIY_QXSH.equals(getLeix())){
				//����ҳ����
				egu.setWidth(Locale.Grid_DefaultWidth);
				//����GRID�Ƿ���Ա༭
				
				egu.getColumn("id").setHeader("ID");
				egu.getColumn("id").setWidth(70);
				egu.getColumn("id").setHidden(true);
				egu.getColumn("id").setEditor(null);
				egu.getColumn("yundh").setHeader("�˵���");
				egu.getColumn("yundh").setWidth(70);
				egu.getColumn("yundh").setEditor(null);
				egu.getColumn("luncxxb_id").setHeader("��������");
				egu.getColumn("luncxxb_id").setWidth(70);
				egu.getColumn("luncxxb_id").setEditor(null);
				egu.getColumn("gongysb_id").setHeader("��Ӧ������");
				egu.getColumn("gongysb_id").setWidth(100);
				egu.getColumn("gongysb_id").setEditor(null);
				egu.getColumn("meikxxb_id").setHeader("ú������");
				egu.getColumn("meikxxb_id").setWidth(120);
				egu.getColumn("meikxxb_id").setEditor(null);
				egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
				egu.getColumn("yunsfsb_id").setWidth(60);
				egu.getColumn("yunsfsb_id").setEditor(null);
				egu.getColumn("chec").setHeader("����");
				egu.getColumn("chec").setWidth(40);
				egu.getColumn("chec").setEditor(null);
				egu.getColumn("faz_id").setHeader("װ����");
				egu.getColumn("faz_id").setWidth(60);
				egu.getColumn("faz_id").setEditor(null);
				egu.getColumn("daoz_id").setHeader("������");
				egu.getColumn("daoz_id").setEditor(null);
				egu.getColumn("daoz_id").setWidth(60);
			
				egu.getColumn("pinzb_id").setHeader("ú��");
				egu.getColumn("pinzb_id").setWidth(60);
				egu.getColumn("pinzb_id").setEditor(null);
//				egu.getColumn("huaybh").setHeader("�����ͬ���");
//				egu.getColumn("huaybh").setWidth(80);
				egu.getColumn("hetb_id").setHeader("�ɹ���ͬ���");
				egu.getColumn("hetb_id").setWidth(120);
				egu.getColumn("hetb_id").setEditor(null);
				egu.getColumn("dunw").setHeader("ͣ����λ");
				egu.getColumn("dunw").setWidth(70);
				egu.getColumn("dunw").setHidden(true);
				egu.getColumn("dunw").setEditor(null);
				egu.getColumn("maoz").setHeader("ж����(��)");
				egu.getColumn("maoz").setWidth(60);
				egu.getColumn("maoz").setEditor(null);
				egu.getColumn("piz").setHeader("Ƥ��(��)");
				egu.getColumn("piz").setHidden(true);
				egu.getColumn("piz").setWidth(60);
				egu.getColumn("piz").setDefaultValue("0");
				egu.getColumn("piz").setEditor(null);
				
				egu.getColumn("sanfsl").setHeader("ˮ����(��)");
//				egu.getColumn("sanfsl").setEditor(null);
				egu.getColumn("sanfsl").setWidth(70);
				egu.getColumn("sanfsl").setEditor(null);
				egu.getColumn("meicb_id").setHeader("ú������");//ú��id
				egu.getColumn("meicb_id").setWidth(70);//ú��id
				egu.getColumn("meicb_id").setReturnId(true);
				egu.getColumn("meicb_id").setEditor(null);
				egu.getColumn("meicb_id").setHidden(true);
				egu.getColumn("duowmc").setHeader("ú������");//ú������
				egu.getColumn("duowmc").setWidth(70);//ú������
				egu.getColumn("duowmc").setEditor(null);
				egu.getColumn("duowmc").setHidden(true);
				
				egu.getColumn("biaoz").setHeader("�˵���(��)");
				egu.getColumn("biaoz").setWidth(70);
				egu.getColumn("biaoz").setEditor(null);
				egu.getColumn("fahrq").setHeader("��������");
				egu.getColumn("fahrq").setWidth(90);
				egu.getColumn("fahrq").setEditor(null);
				egu.getColumn("kaobrq").setHeader("��������");
				egu.getColumn("kaobrq").setWidth(90);
				egu.getColumn("kaobrq").setEditor(null);
				egu.getColumn("xiemkssj").setHeader("жú��ʼʱ��");
				egu.getColumn("xiemkssj").setWidth(120);
				egu.getColumn("xiemkssj").setEditor(null);
				egu.getColumn("xiemjssj").setHeader("жú����ʱ��");
				egu.getColumn("xiemjssj").setWidth(120);
				egu.getColumn("xiemjssj").setEditor(null);

			}else{
			egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
			egu.getColumn("gongysb_id").setWidth(90);
			egu.getColumn("gongysb_id").setEditor(null);
			egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
			egu.getColumn("meikxxb_id").setWidth(90);
			egu.getColumn("meikxxb_id").setEditor(null);
			egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
			egu.getColumn("pinzb_id").setWidth(50);
			egu.getColumn("pinzb_id").setEditor(null);
			egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
			egu.getColumn("fahrq").setWidth(77);
			egu.getColumn("fahrq").setEditor(null);
			egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
			egu.getColumn("daohrq").setWidth(77);
			egu.getColumn("daohrq").setEditor(null);
			egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
			egu.getColumn("yunsfsb_id").setWidth(70);
			egu.getColumn("yunsfsb_id").setEditor(null);
			egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
			egu.getColumn("faz_id").setWidth(65);
			egu.getColumn("faz_id").setEditor(null);
			egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
			egu.getColumn("daoz_id").setWidth(65);
			egu.getColumn("daoz_id").setEditor(null);
			egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
			egu.getColumn("jingz").setWidth(60);
			egu.getColumn("jingz").setEditor(null);
			egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
			egu.getColumn("biaoz").setWidth(60);
			egu.getColumn("biaoz").setEditor(null);
			egu.getColumn("sanfsl").setHeader(Locale.sanfsl_fahb);
			egu.getColumn("sanfsl").setWidth(60);
			egu.getColumn("sanfsl").setEditor(null);
			egu.getColumn("zongkd").setHeader(Locale.zongkd_chepb);
			egu.getColumn("zongkd").setWidth(60);
			egu.getColumn("zongkd").setEditor(null);
			egu.getColumn("ches").setHeader(Locale.ches_fahb);
			egu.getColumn("ches").setWidth(60);
			egu.getColumn("ches").setEditor(null);
			egu.getColumn("chec").setHeader(Locale.chec_fahb);
			egu.getColumn("chec").setWidth(60);
			egu.getColumn("chec").setEditor(null);
			egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
			egu.getColumn("jihkjb_id").setWidth(65);
			egu.getColumn("jihkjb_id").setEditor(null);
			egu.getColumn("jjzt").setHeader("���״̬");
			egu.getColumn("jjzt").setWidth(80);
			egu.getColumn("jjzt").setEditor(null);
			if(flag){
				egu.getColumn("laimsl").setHeader("��ú����");
				egu.getColumn("laimsl").setEditor(null);
			}
			}
		}
		
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();"+MainGlobal.getExtMessageShow("��ȴ�","������",200)+"}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		
		egu.addTbarText("�糧��");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
//		ѡ�����ڵ�����
		ComboBox rqfs = new ComboBox();
		rqfs.setTransform("WeizSelectx");
		rqfs.setWidth(130);
		rqfs.setId("WeizSelectx");
		rqfs.setListeners("select:function(own,rec,index){Ext.getDom('WeizSelectx').selectedIndex=index;document.getElementById('RiqButton').click();"+MainGlobal.getExtMessageShow("��ȴ�","������",200)+"}");
		egu.addToolbarItem(rqfs.getScript());

		if(isshenh) {
			egu.addTbarText("��");
			ComboBox fhrq = new ComboBox();
			fhrq.setTransform("FahrqSelect");
			fhrq.setWidth(130);
			fhrq.setId("FahrqSelect");
			fhrq.setListeners("select:function(own,rec,index){Ext.getDom('FahrqSelect').selectedIndex=index}");
			egu.addToolbarItem(fhrq.getScript());
			
			egu.addTbarBtn(refurbish);
			
			egu.addToolbarButton("���",GridButton.ButtonType_SubmitSel, "ShenhButton");


//            egu.addToolbarButton("����",GridB  nutton.ButtonType_Delete_confirm, "HuitButton");
//            egu.addToolbarButton("����",GridButton.ButtonType_Sel, "HuitButton", null, SysConstant.Btn_Icon_Delete);
			
		}else {
			
			if(HAIY_QXSH.equals(getLeix())){
				
				egu.addTbarText(":");
				DateField df = new DateField();
				df.setValue(getRiq());
				df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
				df.setId("guohrq");
				egu.addToolbarItem(df.getScript());
				

				
				egu.addTbarBtn(refurbish);
				egu.addToolbarButton("ȡ�����",GridButton.ButtonType_SubmitSel, "QuxshButton");
			}else{
			
			egu.addTbarText(":");
			DateField df = new DateField();
			df.setValue(getRiq());
			df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
			df.setId("guohrq");
			egu.addToolbarItem(df.getScript());
			
			egu.addTbarBtn(refurbish);
			
			egu.addToolbarButton("ȡ�����",GridButton.ButtonType_SubmitSel, "QuxshButton");
			}
		}
		if(HAIY_SH.equals(getLeix())||HAIY_QXSH.equals(getLeix())){
			
		}else{
		egu.addToolbarButton("�鿴",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Show);
		}
		
		if(flag){//���ü������ԺϼƵ�  ��       ���ܱ�ѡ�У�����  ���   ����    �鿴ʱ   �п��� ����
			
			egu.addOtherScript("  gridDiv_sm.addListener('beforerowselect',function(model,rowIndex,keepExisting,r){keepExisting=true; if(r.get('ID')=='-1'){return false; }     });");
		}
		setExtGrid(egu);
		con.Close();
		rsl.close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
   //����ʲôʱ�������ѡ���
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			JDBCcon con=new JDBCcon();
			ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()+" and zhuangt=1 and mingc='�Ƿ�Ĭ��������˺������޸�ҳ��Ϊ����ʱ��'");
			String zhi="";
			while(rsl.next()){
				zhi=rsl.getString("zhi");
			}
			if(zhi.equals("��")){
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(2));
			}else{
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		list.add(new IDropDownBean(1, "���������ڲ�"));
		list.add(new IDropDownBean(2, "���������ڲ�"));
		list.add(new IDropDownBean(3, "���������ڲ�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
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
		String lx = cycle.getRequestContext().getParameter("lx");
		if(lx != null) {
			setRiq(DateUtil.FormatDate(new Date()));
			setLeix(lx);
			setTreeid(visit.getDiancxxb_id()+"");
			init();
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			if(getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			setTreeid(visit.getDiancxxb_id()+"");
			if(lx == null && !"Shujshcp".equals(visit.getActivePageName().toString())) {
				setLeix(ALL_SH);
			}
			if("Shujshcp".equals(visit.getActivePageName().toString())){
				initGrid();
			}else{
				init();
			}
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setFahrqModel(null);
		setFahrqValue(null);
		setExtGrid(null);
		setFahids(null);
		setFahrqModels();
		getFahrqValue();
		setWeizSelectValue(null);
		setWeizSelectModel(null);
		initGrid();
	}
}