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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.dc.chengbgl.Chengbjs;
//import com.zhiren.dc.diaoygl.AutoCreateShouhcrb;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2013-04-07
 * ������Ӧ�糧Ҫ��������biaoz��Ϊjingz
 */
public class Shujsh_szs extends BasePage implements PageValidateListener {
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
	public IDropDownBean getDaohrqValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getDaohrqModel().getOptionCount()>0) {
				setDaohrqValue((IDropDownBean)getDaohrqModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setDaohrqValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getDaohrqModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setDaohrqModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setDaohrqModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setDaohrqModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql_yunsfs = "";
		sql_yunsfs = " and yunsfsb_id = "+SysConstant.YUNSFS_QIY;
		String sql_dcid = Jilcz.filterDcid(visit, "");
		String sql= "select rownum xuh,daohrq " +
				"from (select distinct to_char(daohrq,'yyyy-mm-dd') daohrq " +
				"from fahb where hedbz = "+SysConstant.HEDBZ_YJJ+sql_yunsfs+sql_dcid+" and diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ") and to_char(daohrq,'yyyy')>2010 order by daohrq) ";
	 
		setDaohrqModel(new IDropDownModel(sql));
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
		if(!treeid.equals(((Visit) getPage().getVisit()).getString2()) || ((Visit) getPage().getVisit()).getString2()==null){
			((Visit) getPage().getVisit()).setString2(treeid);
			setDaohrqValue(null);
			setDaohrqModel(null);
			setDaohrqModels();
		}
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
	private boolean _SaveClick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	private boolean _QuxshClick = false;
	public void QuxshButton(IRequestCycle cycle) {
		_QuxshClick = true;
	}
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
		}
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
			setDaohrqModel(null);
			setDaohrqValue(null);
		}
		if (_QuxshClick) {
			_QuxshClick = false;
			Quxsh();
			setDaohrqModel(null);
			setDaohrqValue(null);
		}
		if(_RiqClick){
			_RiqClick=false;
			setDaohrqModel(null);
			setDaohrqValue(null);
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}
	
	private void save(){
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		JDBCcon con = new JDBCcon();
//		StringBuffer sql = new StringBuffer();
		StringBuffer chep_sql = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Shujsh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
		}
		while(rsl.next()){
			if(!rsl.getString("id").equals("-1")){
				double jingz = rsl.getDouble("sanfsl");
//				sql.append("update fahb set jingz= " + jingz + ",sanfsl="+ jingz +" where id=" + rsl.getString("id") + ";\n");
				ResultSetList rs = con.getResultSetList("select count(*) x from chepb where fahb_id=" + rsl.getString("id") + "");
				rs.next();
				int count = rs.getInt("x");
				int zhi = (int)Math.floor(jingz/count);
				rs = con.getResultSetList("select id from chepb where fahb_id=" + rsl.getString("id"));
				while(rs.next()){
					if(rs.getRow()+1==rs.getRows()){
//						chep_sql.append("update chepb set sanfsl="+CustomMaths.Round_new((jingz-zhi*(count-1)),3)+",maoz="+CustomMaths.Round_new((jingz-zhi*(count-1)),3)+"+20,piz=20 where id="+ rs.getString("id") +";");
						chep_sql.append("update chepb set sanfsl="+CustomMaths.Round_new((jingz-zhi*(count-1)),3)+" where id="+ rs.getString("id") +";");
//						break;
					}else{
//						chep_sql.append("update chepb set sanfsl="+zhi+",maoz="+zhi+"+20,piz=20 where id="+ rs.getString("id") +";");
						chep_sql.append("update chepb set sanfsl="+zhi+" where id="+ rs.getString("id") +";");
					}
				}
			}
		}
		con.setAutoCommit(false);
		int flag = con.getUpdate("begin\n" + chep_sql + "end;\n");
		if(flag==-1){
			setMsg("�޸Ľ���ú��ʧ�ܣ�");
		}else{
//			flag = con.getUpdate("begin\n" + sql + "end;\n");
//			if(flag==-1){
//				con.rollBack();
//				setMsg("�޸�ʧ�ܣ�");
//			}else{
				con.commit();
				setMsg("����ú���޸ĳɹ���");
//			}
		}
		con.Close();
	}
	
	private void Shenh() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		JDBCcon chep_con = new JDBCcon();
		con.setAutoCommit(false);
		List fhlist = new ArrayList();
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("����", "������㷽��", String.valueOf(v.getDiancxxb_id()), "����"));
		StringBuffer sb = new StringBuffer();
//		StringBuffer chep_sql = new StringBuffer();
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
			int flg = Jilcz.Hetkzjm(con, v.getDiancxxb_id(),meikxxb_id, rsl.getString("daohrq"),SysConstant.YUNSFS_QIY);
			if (flg == -1) {
				con.rollBack();
				con.Close();
				setMsg("��ú��λû�ж�Ӧ�ĺ�ͬ���������ͬ���������!");
				return;
			}
			
			if(rsl.getInt("mxhedbz")==0){
				continue;
			}
			String fahbid = rsl.getString("id");
			Jilcz.addFahid(fhlist, fahbid);
			ches += rsl.getInt("ches");
			biaoz += rsl.getDouble("biaoz");
			long diancxxb_id = v.getDiancxxb_id();
			if(v.isFencb()){
				diancxxb_id = getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"));
			}
			String daohrq = rsl.getString("daohrq");
			String sql = "select * from xitxxb where mingc = '����ȷ������' and zhi = '���' and leib = '����' and zhuangt = 1 and diancxxb_id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
			if(con.getHasIt(sql)){
				daohrq = DateUtil.FormatDate(new Date());
			}
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
//				chep_sql.append("update chepb set sanfsl=maoz-piz where fahb_id=" + rsl.getString("id") + ";\n");
//				chep_sql.append("update fahb set sanfsl="+ rsl.getString("jingz") +" where id=" + rsl.getString("id") + ";\n");
//				chep_con.getUpdate("begin\n" +chep_sql+"\n end;");
//				chep_sql.setLength(0);
				
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
			
//			AutoCreateShouhcrb.Create(con, diancxxb_id, DateUtil.getDate(daohrq));
			sb.delete(0, sb.length());
		}
		
		chep_con.Close();
		
		con.commit();
		con.Close();
		Chengbjs.CountChengb(v.getDiancxxb_id(), fhlist);
//		setMsg("��˳ɹ���������� "+ ches + " ������Ʊ�� "+biaoz+" �֡�");
		setMsg("��˳ɹ���");
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
//		int ches = 0;
//		double biaoz = 0;
		boolean hasfail = false;
		while(rsl.next()) {
			if(rsl.getInt("hedbz")>SysConstant.HEDBZ_YSH) {
				hasfail = true;
				continue;
			}
			String fahbid = rsl.getString("id");
//			ches += rsl.getInt("ches");
//			biaoz += rsl.getDouble("biaoz");
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
		con.commit();
		con.Close();
		if(hasfail) {
//			setMsg("��������δ�ɹ�ȡ����ˣ����ɹ�ȡ���� "+ ches + " ������Ʊ�� "+biaoz+" �֡�");
			setMsg("��������δ�ɹ�ȡ����ˣ�");
		}else {
//			setMsg("ȡ����˳ɹ�����ȡ���� "+ ches + " ������Ʊ�� "+biaoz+" �֡�");
			setMsg("ȡ����˳ɹ���");
		}
		
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
	
	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" and f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY + "\n");
		sb1.append(" and (f.mxhedbz = ").append(SysConstant.HEDBZ_YJJ)
		.append(" or f.hedbz=").append(SysConstant.HEDBZ_YJJ).append(")");
		sb1.append(" and f.daohrq = ").append(DateUtil.FormatOracleDate(getDaohrqValue()
								.getValue())).append("\n"); 
		sb1.append(Jilcz.filterDcid(visit, "f"));
		
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		
		ExtGridUtil egu = new ExtGridUtil();
		ResultSetList rsl = new ResultSetList();
		
		sb.append(
			"select\n" +
			"   decode(id,null,-1,id) id\n" + 
			"  ,decode(grouping(diancxxb_id)+grouping(meikxxb_id),2,'�ܼ�',1,diancxxb_id || 'С��',diancxxb_id) diancxxb_id\n" + 
			"  ,decode(grouping(id)+grouping(diancxxb_id)+grouping(meikxxb_id),1,meikxxb_id || '_С��',meikxxb_id) meikxxb_id\n" + 
			"  ,daohrq\n" +
			"  ,sum(jingz) biaoz\n" + 
			"  ,sum(sanfsl) sanfsl\n" + 
			"  ,round(decode(sum(sanfsl),0,sum(biaoz*qnet_ar_mj)/sum(biaoz),sum(sanfsl*qnet_ar_mj)/sum(sanfsl)),3) qnet_ar_mj\n" + 
			"  ,round(decode(sum(sanfsl),0,sum(biaoz*vad)/sum(biaoz),sum(sanfsl*vad)/sum(sanfsl)),2) vad\n" + 
			"  ,round(decode(sum(sanfsl),0,sum(biaoz*qnet_ar)/sum(biaoz),sum(sanfsl*qnet_ar)/sum(sanfsl)),0) qnet_ar\n" + 
			"  ,round(decode(sum(sanfsl),0,sum(biaoz*mt)/sum(biaoz),sum(sanfsl*mt)/sum(sanfsl)),2) mt\n" + 
			"  ,round(decode(sum(sanfsl),0,sum(biaoz*mad)/sum(biaoz),sum(sanfsl*mad)/sum(sanfsl)),2) mad\n" + 			
			"  ,round(decode(sum(sanfsl),0,sum(biaoz*std)/sum(biaoz),sum(sanfsl*std)/sum(sanfsl)),2) std\n" + 
			"  ,round(decode(sum(sanfsl),0,sum(biaoz*aar)/sum(biaoz),sum(sanfsl*aar)/sum(sanfsl)),2) aar\n" + 
			"  ,mxhedbz,ches\n" +
			"from (\n" + 
			"select distinct f.id,  d.mingc diancxxb_id, m.mingc meikxxb_id,mxhedbz,ches,\n" + 
			"f.daohrq, f.jingz,f.sanfsl, f.biaoz,z.qnet_ar qnet_ar_mj,round(z.qnet_ar/4.1816*1000,0) qnet_ar,z.mt,z.mad,z.vad,z.std,z.aar\n" + 
			"from (select fh.*,(select nvl(max(hedbz),0) hedbz from chepb c where c.fahb_id = fh.id) mxhedbz from fahb fh ) f,\n" + 
			"     gongysb g, meikxxb m, pinzb p, jihkjb j,yunsfsb y, diancxxb d,zhillsb z\n" + 
			"where f.gongysb_id = g.id(+) and f.meikxxb_id = m.id(+) and f.yunsfsb_id=y.id\n" + 
			"  and f.pinzb_id = p.id and f.jihkjb_id = j.id and f.diancxxb_id = d.id\n");
		if(visit.isFencb()){
			sb.append("and diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n");
		}
		sb.append(
			sb1 + 
			"  and f.zhilb_id=z.zhilb_id\n" + 
			"  )\n" + 
			"  group by rollup(diancxxb_id,meikxxb_id,(daohrq,id,mxhedbz,ches))");

		rsl = con.getResultSetList(sb.toString());
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
		egu.addPaging(-1);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(50);
			egu.getColumn("diancxxb_id").setEditor(null);
		}
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(180);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(77);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("biaoz").setHeader("ԭʼú��");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("sanfsl").setHeader("����ú��");
		egu.getColumn("sanfsl").setWidth(70);
		((NumberField)egu.getColumn("sanfsl").editor).setDecimalPrecision(2);
		egu.getColumn("sanfsl").editor.setAllowBlank(false);

		egu.getColumn("qnet_ar_mj").setHeader("�յ�����λ<br>������Mj/kg");
		egu.getColumn("qnet_ar_mj").setWidth(70);
		egu.getColumn("qnet_ar_mj").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("�յ�����λ<br>��ֵKcal/kg");
		egu.getColumn("qnet_ar").setWidth(70);
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("mt").setHeader("ȫˮ");
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("mad").setHeader("�ոɻ�ˮ");
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("vad").setHeader("�ոɻ�<br>�ӷ���");
		egu.getColumn("vad").setWidth(60);
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("std").setHeader("�ɻ���<br>(ȫ��)");
		egu.getColumn("std").setWidth(60);
		egu.getColumn("std").setEditor(null);
		egu.getColumn("aar").setHeader("�յ�����");
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("mxhedbz").setHidden(true);
		egu.getColumn("mxhedbz").editor=null;
		egu.getColumn("ches").setHidden(true);
		egu.getColumn("ches").editor=null;
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();"+MainGlobal.getExtMessageShow("��ȴ�","������",200)+"}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		
		egu.addTbarText("�糧��");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("�������ڣ�");
		ComboBox fhrq = new ComboBox();
		fhrq.setTransform("DaohrqSelect");
		fhrq.setWidth(130);
		fhrq.setId("DaohrqSelect");
		fhrq.setLazyRender(true);// ��̬��
		egu.addToolbarItem(fhrq.getScript());
		egu.addOtherScript("DaohrqSelect.on('select',function(){document.forms[0].submit();});");
	
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarButton("���",GridButton.ButtonType_SubmitSel, "ShenhButton");
			
//		egu.addToolbarButton("�鿴",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Show);
		
		//���ü������ԺϼƵ�  ��       ���ܱ�ѡ�У�����  ���   ����    �鿴ʱ   �п��� ����
		egu.addOtherScript("  gridDiv_sm.addListener(" +
								"'beforerowselect',function(" +
									"model,rowIndex,keepExisting,r){" +
									"	keepExisting=true; " +
									"	if(r.get('ID')=='-1'){" +
									"		return false; " +
									"	}     " +
									"});");
		
		String script = 
			"gridDiv_grid.on('beforeedit',function(e){\n" +
			"  if(e.field=='JINGZ'){\n" + 
			"    if(e.record.get('ID')==-1){e.cancel=true;}\n" + 
			"  }\n" + 
			"});";
		egu.addOtherScript(script);
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
		setLeix("HY_SH");
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setString2("");
			init();
			visit.setActivePageName(getPageName().toString());
		}
		
		initGrid();
		
	} 
	
	private void init() {
		setExtGrid(null);
		setFahids(null);
		setDaohrqValue(null);
		setDaohrqModel(null);
		setDaohrqModels();
	}
}