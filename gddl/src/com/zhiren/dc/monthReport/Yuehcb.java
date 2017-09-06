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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-18
 * �������޸���һ���汾�в�����д������ɼ��㲻��ȷ�����⡣
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-16
 * ����: �±���ú�������µļ��㷽�� 		
 * ���Ӳ�����insert into xitxxb values(getnewid(229),1,2291,'�±�������ú������','Ʊ��+ӯ��-����-����','','�±�',1,'')
 * 		һ������diancxxb_id��ֳ�ID
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-03 11��12
 * ���������ӵ糧���Ĵ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-06-20
 * ���������ӵ糧���ĳ�ʼ������
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-20
 * ���������¿���������ɲ���
 * 		ȡ��ȫ�������Զ�ˢ�¹��ܣ��û����ֶ����ˢ�°�ť�ſ�ˢ������
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-20
 * ������ȡ�����㰴ť���ܼ��к��ڳ���治�ܱ��༭��
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-22
 * ��������ú�����ݲ��ɱ��༭
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-27
 * ����������������ʾ���е�������ʾ��ʽ
 * 		 �������ɷ���������ۼƺͱ���Ӧ����һ�£�
 * 		 ���������еļ��㷽��
 */
/*
 * ���ߣ����
 * ���ڣ�2012-01-10
 * �������Ĵ�����б���ʱ������ݲ����Ϲ�����ʾ���������Ϣ
 * 		�޸�ǰ̨�����п����㷽�����Զ��������������ݵĿ����Ϣ
 * 		�����Ĵ������㹫ʽ��ֻ���㱾��������Ϣ��
 * 		
 */
/*
 * ���ߣ����
 * ���ڣ�2012-02-01
 * �������Ĵ�����ڱ���ʱ�Զ�������ڳ����Ϳ�����������Ϣ���ۼ�ֵ��
 * 		
 */
/*
 * ���ߣ����
 * ���ڣ�2012-03-16
 * �������Ĵ�����ڵ������������±�����ģ��ͬһ��
 */
/*
 * ���ߣ���ʤ��
 * ���ڣ�2013-01-11
 * �������������治�ɱ༭����ʾ��ʽ��
 *				�ɱ༭������Сֵ����,��Сֵ����0.
 */
public class Yuehcb extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// ҳ��仯��¼
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
		if(MainGlobal.getXitxx_item("�±�", "�±��Ĵ��Զ������ۼ�", getTreeid(), "��").equals("��")){
			String diancxxb_id = getTreeid();
			JDBCcon con = new JDBCcon();
			String strDate[] = this.getRiq2().split("-");
			String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
			
			if(visit.getString10().equals(strParam)){
				CurrODate=DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]+ "-01");
			}else{
				CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"+ getYuef() + "-01");
			}
			String sql="SELECT SUM(SHOUML) SHOUML,\n" +
				"       SUM(FADY) FADY,\n" + 
				"       SUM(GONGRY) GONGRY,\n" + 
				"       SUM(QITH) QITH,\n" + 
				"       SUM(SUNH) SUNH,\n" + 
				"       SUM(DIAOCL) DIAOCL,\n" + 
				"       SUM(PANYK) PANYK,\n" + 
				"       SUM(SHUIFCTZ) SHUIFCTZ,\n" + 
				"       SUM(JITCS) JITCS,\n" + 
				"       YT2.ID ID\n" + 
				"  FROM YUEHCB H,\n" + 
				"       YUETJKJB YT,\n" + 
				"       (SELECT ID, GONGYSB_ID, JIHKJB_ID, PINZB_ID, YUNSFSB_ID\n" + 
				"          FROM YUETJKJB\n" + 
				"         WHERE RIQ = "+CurrODate+"\n" + 
				"           AND DIANCXXB_ID = "+diancxxb_id+") YT2\n" + 
				" WHERE H.YUETJKJB_ID = YT.ID\n" + 
				"   AND H.FENX = '" + SysConstant.Fenx_Beny + "'\n" + 
				"   AND YT2.GONGYSB_ID = YT.GONGYSB_ID\n" + 
				"   AND YT2.JIHKJB_ID = YT.JIHKJB_ID\n" + 
				"   AND YT2.PINZB_ID = YT.PINZB_ID\n" + 
				"   AND YT2.YUNSFSB_ID = YT.YUNSFSB_ID\n" + 
				"   AND YT.DIANCXXB_ID = "+diancxxb_id+"\n" + 
				"   AND YT.RIQ BETWEEN trunc("+CurrODate+",'yyyy') AND "+CurrODate+"\n" + 
				" GROUP BY YT2.ID";
			ResultSetList rs=con.getResultSetList(sql);
			StringBuffer sql_Upd = new StringBuffer();
			sql_Upd.append("begin \n");
			while(rs.next()){
				sql_Upd.append("update yuehcb set \n" + 
				"shouml = " + rs.getDouble("shouml") + ",\n" + 
				"fady = " + rs.getDouble("fady") + ",\n" + 
				"gongry = " + rs.getDouble("gongry") + ",\n" + 
				"qith = " + rs.getDouble("qith") + ",\n" + 
				"sunh = " + rs.getDouble("sunh") + ",\n" + 
				"diaocl = " + rs.getDouble("diaocl") + ",\n" + 
				"panyk = " + rs.getDouble("panyk") + ",\n" + 
				"shuifctz = " + rs.getDouble("shuifctz") + ",\n" + 
				"jitcs = " + rs.getDouble("jitcs") + "\n" + 
				"where YUETJKJB_ID=" + rs.getString("id") + " and fenx='" + SysConstant.Fenx_Leij + "';\n");
			}
			sql_Upd.append("end;");
			if(sql_Upd.length()>15){
				int flag=0;
				flag=con.getUpdate(sql_Upd.toString());
				if(flag!=-1){
					setMsg("����ɹ�!");
				}else{
					setMsg("�ۼ�ֵ����ʧ��!");
				}
			}
			return;
		}
		setMsg("����ɹ�!");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
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
		if (_SaveChick) {
			_SaveChick = false;
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
		getSelectData();
	}
	public void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate=strDate[0]+"��"+strDate[1]+"��";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		
		if(visit.getString10().equals(strParam)){
			CurrZnDate = strDate[0] + "��" + strDate[1] + "��";
			CurrODate=DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]+ "-01");
		}else{
			CurrZnDate = getNianf() + "��" + getYuef() + "��";
			CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		}
		
		String strSql=
			"delete from yuehcb where yuetjkjb_id in (select id from yuetjkjb where riq="
			+CurrODate+" and diancxxb_id="+diancxxb_id+")";
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("ɾ�������з�������");
		}else {
			setMsg(CurrZnDate+"�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}
	private String mingc = "jingz";

	public void CreateData() {
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String strDate[] =this.getRiq2().split("-");
		String CurrZnDate="";//strDate[0]+"��"+strDate[1]+"��";
		String CurrODate ="";// DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		int intYuef=0;//Integer.parseInt(strDate[1]);
		
		if(visit.getString10().equals(strParam)){
			CurrZnDate = strDate[0] + "��" + strDate[1] + "��";
			CurrODate=DateUtil.FormatOracleDate(strDate[0] + "-" + strDate[1]+ "-01");
			intYuef=Integer.parseInt(strDate[1]);
		}else{
			CurrZnDate = getNianf() + "��" + getYuef() + "��";
			CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
			intYuef=Integer.parseInt(getYuef());
		}
		
		//getShoumlDy();
		String strshouml = mingc;
		String sql = "select zhi from xitxxb where mingc ='�±�������ú������' and zhuangt = 1 and leib = '�±�' and diancxxb_id =" + diancxxb_id;
		ResultSetList sm = con.getResultSetList(sql);
		if(sm.next()){
			if("Ʊ��+ӯ��-����-����".equals(sm.getString("zhi"))){
				strshouml = "biaoz + yingd - kuid - yuns";
			}
		}
		sm.close();
		DelData();
		StringBuffer sb = new StringBuffer();
		sb.append("select nvl(y.id,0) id,oy.* from yuetjkjb y, \n")
		.append("(select distinct diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id from yuehcb h,yuetjkjb t \n")
		.append("where h.yuetjkjb_id = t.id and h.kuc <>0 and t.diancxxb_id =").append(diancxxb_id).append(" \n")
		.append("and t.riq = add_months(").append(CurrODate).append(",-1)) oy\n")
		.append("where y.diancxxb_id = oy.diancxxb_id and y.gongysb_id = oy.gongysb_id\n")
		.append("and y.jihkjb_id = oy.jihkjb_id and y.pinzb_id = oy.pinzb_id\n")
		.append("and y.yunsfsb_id = oy.yunsfsb_id and y.riq>=").append(DateUtil.FormatOracleDate(this.getRiq1())).append("and y.riq<=").append(DateUtil.FormatOracleDate(this.getRiq2()));
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			if(rsl.getLong("id") == 0) {
				sb.delete(0, sb.length());
				sb.append("insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n")
				.append("getnewid(").append(diancxxb_id).append("),").append(CurrODate).append(",")
				.append(diancxxb_id).append(",0,").append(rsl.getLong("gongysb_id")).append(",")
				.append(rsl.getLong("pinzb_id")).append(",").append(rsl.getLong("jihkjb_id")).append(",").append(rsl.getLong("yunsfsb_id")).append(")");
				con.getInsert(sb.toString());
			}
		}
		sb.delete(0, sb.length());
		sb.append("insert into yuehcb(id,fenx,yuetjkjb_id,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,jitcs,kuc)")
		.append("(select getnewid(").append(diancxxb_id).append("),k.fenx,k.yuetjkjb_id,k.qickc,k.shouml,k.fady,k.gongry,k.qith,k.sunh,k.diaocl,k.panyk,k.shuifctz,k.jitcs,k.kuc from ")
		.append("(select ")
		.append(" rownum,hz.id yuetjkjb_id,hz.fenx,");
		if(intYuef == 1) {
			sb.append("nvl(h.kuc,0) qickc,nvl(")
			.append(strshouml).append(",0) shouml,\n")
			.append("0 fady,0 gongry,0 qith,0 sunh,0 diaocl,0 panyk,0 shuifctz,0 jitcs,0 kuc\n");
		}else {
			sb.append("nvl(h.kuc,0) qickc,");
			sb.append("nvl("+strshouml).append(",0) shouml,\n");
			sb.append("nvl(decode(hz.fenx,'����',0,h.fady),0) fady, nvl(decode(hz.fenx,'����',0,h.gongry),0) gongry,\n");
			sb.append("nvl(decode(hz.fenx,'����',0,h.qith),0) qith, nvl(decode(hz.fenx,'����',0,h.sunh),0) sunh,\n");
			sb.append("nvl(decode(hz.fenx,'����',0,h.diaocl),0) diaocl, nvl(decode(hz.fenx,'����',0,h.panyk),0) panyk,\n");
			sb.append("nvl(decode(hz.fenx,'����',0,h.shuifctz),0) shuifctz,nvl(decode(hz.fenx,'����',0,h.jitcs),0) jitcs,\n");
			sb.append("nvl(decode(hz.fenx,'����',h.kuc,h.qickc),0) +nvl("+strshouml+",0) -nvl(decode(hz.fenx,'����',0,h.fady),0) - nvl(decode(hz.fenx,'����',0,h.gongry),0)-nvl(decode(hz.fenx,'����',0,h.qith),0) -nvl(decode(hz.fenx,'����',0,h.diaocl),0) + nvl(decode(hz.fenx,'����',0,h.panyk),0)+nvl(decode(hz.fenx,'����',0,h.shuifctz),0)-nvl(decode(hz.fenx,'����',0,h.sunh),0) kuc\n");
		}
		sb.append("from (select * from yuetjkjb,(select '����' fenx from dual union select '�ۼ�' from dual) where diancxxb_id =").append(diancxxb_id)
		.append("     and riq = ").append(CurrODate).append(" order by id,fenx) hz,yueslb s,").append(" (select y.yuetjkjb_id_new,h.* from ")
		.append("(select nvl(y.id,0) yuetjkjb_id_new,oy.yuetjkjb_id from yuetjkjb y, \n")
		.append("(select distinct t.id yuetjkjb_id,diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id from yuehcb h,yuetjkjb t \n")
		.append("where h.yuetjkjb_id = t.id  and t.diancxxb_id =").append(diancxxb_id).append(" \n")//and h.kuc <>0
		.append("and t.riq = add_months(").append(CurrODate).append(",-1)) oy \n").append("where y.diancxxb_id = oy.diancxxb_id and y.gongysb_id = oy.gongysb_id \n")
		.append("and y.jihkjb_id = oy.jihkjb_id and y.pinzb_id = oy.pinzb_id \n").append("and y.yunsfsb_id = oy.yunsfsb_id and y.riq=")
		.append(CurrODate).append(") y, yuehcb h \n").append("where h.yuetjkjb_id = y.yuetjkjb_id ) h\n")
		.append("where hz.id = s.yuetjkjb_id(+) and hz.id = h.yuetjkjb_id_new(+)")
		.append("and hz.fenx = s.fenx(+) and hz.fenx = h.fenx(+) order by hz.id,hz.fenx) k) \n");

		int flag = con.getUpdate(sb.toString());
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sb.toString());
			setMsg("���ɹ��̳��ִ����ºĴ�δ����ɹ���");
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"�����ݳɹ����ɣ�");
	}
	
	/**
	 * @param con
	 * @return   true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
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
		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = getTreeid();
		String strDate [] = this.getRiq2().split("-");
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		String strSql="";
		strSql = "select id from yueshchjb where riq = "+CurrODate+ "and diancxxb_id ="+this.getTreeid();
		boolean isLocked = !con.getHasIt(strSql);
		if(isLocked) {
			setMsg("����ʹ�ñ�ģ��֮ǰ��������ɱ��ºĴ�ϼ����ݵļ��㣡");
		}
			strSql = "select * from (select -1 id,'-' as gongysb_id,'-' jihkjb_id,'-' pinzb_id,'-' yunsfsb_id,fenx||'�ϼ�' fenx,\n"+
			"       qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,jitcs,kuc\n"+
			"       from yueshchjb\n"+
			"where riq ="+CurrODate+" and diancxxb_id in( "+diancxxb_id+") order by fenx)\n"+
			" union all select * from\n"+
			"(select yh.id ,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n"+
			"		yunsfsb.mingc as yunsfsb_id,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,jitcs,kuc\n"+
			"		from yuetjkjb tj, yuehcb yh, gongysb, jihkjb, pinzb, yunsfsb\n"+
			"where tj.id = yh.yuetjkjb_id and tj.gongysb_id = gongysb.id and\n"+
			"      tj.jihkjb_id = jihkjb.id and tj.pinzb_id = pinzb.id and\n"+
			"      tj.yunsfsb_id = yunsfsb.id  and tj.diancxxb_id in("+diancxxb_id+")\n"+
			"	   and riq="+CurrODate+" order by jihkjb_id DESC,gongysb_id,pinzb_id,id,yuetjkjb_id,fenx) s";
		
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		// //���ñ��������ڱ���
		egu.setTableName("yuehcb");
		// /������ʾ������
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		egu.getColumn("gongysb_id").setHeader("������λ");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("gongysb_id").setUpdate(false);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("jihkjb_id").setUpdate(false);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("pinzb_id").setWidth(45);
		egu.getColumn("pinzb_id").setUpdate(false);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("pinzb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setUpdate(false);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setUpdate(false);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("qickc").setHeader("�ڳ����");
		egu.getColumn("qickc").setWidth(70);
		egu.getColumn("qickc").setEditor(null);
		egu.getColumn("qickc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("shouml").setHeader("��ú��");
		egu.getColumn("shouml").setWidth(70);
		egu.getColumn("shouml").setEditor(null);
		egu.getColumn("shouml").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("fady").setHeader("�����");
		egu.getColumn("fady").setWidth(70);
		egu.getColumn("fady").editor.setMinValue("0");
		
		egu.getColumn("gongry").setHeader("���Ⱥ�");
		egu.getColumn("gongry").setWidth(70);
		egu.getColumn("gongry").editor.setMinValue("0");
		
		egu.getColumn("qith").setHeader("������");
		egu.getColumn("qith").setWidth(70);
		egu.getColumn("qith").editor.setMinValue("0");
		
		egu.getColumn("sunh").setHeader("�������");
		egu.getColumn("sunh").setWidth(60);
		egu.getColumn("sunh").editor.setMinValue("0");
		
		egu.getColumn("diaocl").setHeader("������");
		egu.getColumn("diaocl").setWidth(60);	
		egu.getColumn("diaocl").editor.setMinValue("0");
		
		egu.getColumn("panyk").setHeader("��ӯ��");
		egu.getColumn("panyk").setWidth(60);
		
		egu.getColumn("shuifctz").setHeader("ˮ�ֲ����");
		egu.getColumn("shuifctz").setWidth(60);
		
		
		egu.getColumn("jitcs").setHeader("���ᴢ��");
		egu.getColumn("jitcs").setWidth(60);
		egu.getColumn("jitcs").setHidden(true);
		
		egu.getColumn("kuc").setHeader("���");
		egu.getColumn("kuc").setWidth(60);
		egu.getColumn("kuc").setEditor(null);
		egu.getColumn("kuc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		egu.setDefaultsortable(false);  
		// /���ð�ť
		StringBuffer sb = new StringBuffer();	

		String condition="";
		if(MainGlobal.getXitxx_item("�±�", "�±��Ĵ��Զ������ۼ�", getTreeid(), "��").equals("��")){
			condition="if(e.record.get('FENX')=='�ۼ�'){ e.cancel=true;}\n";
		}
		
		sb.append("\ngridDiv_grid.on('afteredit',function(e){");
		sb.append("CountAllKuc(gridDiv_ds);});\n");

		sb.append("gridDiv_grid.on('beforeedit',function(e){\n");
		sb.append("if(e.record.get('ID')=='-1'){e.cancel=true;}\n");//�ϼ��в�����༭
		sb.append(condition);
		sb.append("});\n");
		
//		 �趨�ϼ��в�����
		sb.append("function gridDiv_save(record){if(record.get('ID')=='-1') return 'continue';}");
		
		egu.addOtherScript(sb.toString());
		
		if(visit.getString10().equals(strParam)){
	    	egu.addTbarText("ʱ��:");
	    	DateField df = new DateField();
	    	df.setValue(this.getRiq1());
	    	df.Binding("RIQ1", "");
	    	df.setId("riq1");
	    	egu.addToolbarItem(df.getScript());
		
	    	egu.addTbarText("-");
	    	egu.addTbarText("��:");
	    	DateField df1 = new DateField();
	    	df1.setValue(this.getRiq2());
	    	df1.Binding("RIQ2", "");
	    	df1.setId("riq2");
	    	egu.addToolbarItem(df1.getScript());
	    	egu.addTbarText("-");
		}else{
		egu.addTbarText("���");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("�·�");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// ���÷ָ���
		}
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){");
		if(visit.getString10().equals(strParam)){
			rsb.append(MainGlobal.getExtMessageBox("'����ˢ��"+strDate[0]+"��"+strDate[1]+"�µ�����,���Ժ�'",true));
		}else{
			rsb.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",true));
		}
		rsb.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con)){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
//			���ɰ�ť
			GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
			gbc.setDisabled(isLocked);
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
//			ɾ����ť
			GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
			gbd.setDisabled(isLocked);
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
//			���水ť
			GridButton gbs;
			gbs = new GridButton(GridButton.ButtonType_Save_condition,"gridDiv",egu.getGridColumns(),"SaveButton","if(validateHy(gridDiv_ds)){return;};\n");	
			gbs.setDisabled(isLocked);
			egu.addTbarBtn(gbs);
		}
//		��ӡ��ť
		GridButton gbp = new GridButton("��ӡ","function (){"+MainGlobal.getOpenWinScript("MonthReport&lx=yuehcb")+"}");
		gbp.setDisabled(isLocked);
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String strDate [] = this.getRiq2().split("-");
		String cnDate = strDate[0]+"��"+strDate[1]+"��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����")
			.append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			//getShoumlDy();
			visit.setString11("");
			visit.setString9("");
			setTreeid(null);
			setRiq();
			getSelectData();
		}
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
	
	 // ���������
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

	// �·�������
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