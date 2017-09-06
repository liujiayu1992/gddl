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
 * ���ߣ�lip
 * ���ڣ�2011-12-20
 * �������޸�select��䣬ÿ���״����±�ʱ���������ۼ�ֵ�������������
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-20
 * ������ȡ��ȫ�������Զ�ˢ�¹��ܣ��û����ֶ����ˢ�°�ť�ſ�ˢ������
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-20
 * ������ȡ�����㰴ť���ܼ��к��ڳ���治�ܱ��༭��
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-22
 * ��������д���水ť
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-27
 * ���������������еļ��㷽��������ۼƺͱ���Ӧ����һ�£�
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
 * ���ߣ�LIP
 * ���ڣ�2012-03-14
 * �����������������ݴ洢�����б䣬ͬ���Ƴ������Σ��ı�SQL��������
 */
/*
 * ���ߣ���ʤ��
 * ���ڣ�2012-03-16
 * ����������ɾ������
 */
/*
 * ���ߣ���ʤ��
 * ���ڣ�2013-01-11
 * �������������治�ɱ༭����ʾ��ʽ��
 *				�ɱ༭������Сֵ����,��Сֵ����0.
 */
public class Yuehcb_szs extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
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

	// ҳ��仯��¼
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
					if("�ۼ�".equals(rsl.getString("fenx")) && getIsSelectLike()){
						
						String tmpsql = "";
						double bq_qickc = 0;
						double bq_kc = 0;
						{
							tmpsql = "select h.qickc,h.kuc from yuehcb h,yuetjkjb y\n" +
									 " where h.fenx='����'\n" + 
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
					setMsg("����ɹ�!");
				}else{
					setMsg("����ɹ�,�ۼ�ֵ����ʧ��!");
				}
			}else{
				setMsg("����ɹ�!");
			}
		}else{
			setMsg("����ʧ��");
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
			String CurrZnDate=getNianf()+"��"+getYuef()+"��";
			String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
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

	/**
	 * @param con
	 * @return   true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
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
			"select * from (select id,null yid,-1 yuetjkjb_id,'�ܼ�' as gmingc,'-' jmingc,'-' pmingc,'-' ymingc, fenx,\n" +
			"       qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,jitcs,kuc\n" + 
			"       from yueshchjb\n" + 
			"where riq ="+CurrODate+" and diancxxb_id = " + diancxxb_id + " order by fenx)\n" + 
			" union all\n" + 
			" select * from(\n" + 
			" select y.id,decode(y.id,null,getnewid(" + diancxxb_id + "),y.id) yid,s.yuetjkjb_id,s.gmingc,s.jmingc,s.pmingc,s.ymingc,s.fenx,\n" + 
			"           decode(y.qickc,null,s.kuc,0,s.kuc,y.qickc) qickc,s.shouml,\n" + 

			"           decode(y.fady,null,decode(s.fenx,'����',0,s.fady),y.fady) fady,\n" + 
			"           decode(y.gongry,null,decode(s.fenx,'����',0,s.gongry),y.gongry) gongry,\n" + 
			"           decode(y.qith,null,decode(s.fenx,'����',0,s.qith),y.qith) qith,\n" + 
			"           decode(y.sunh,null,decode(s.fenx,'����',0,s.sunh),y.sunh) sunh,\n" + 
			"           decode(y.diaocl,null,decode(s.fenx,'����',0,s.diaocl),y.diaocl) diaocl,\n" + 
			"           decode(y.panyk,null,decode(s.fenx,'����',0,s.panyk),y.panyk) panyk,\n" + 
			"           decode(y.shuifctz,null,decode(s.fenx,'����',0,s.shuifctz),y.shuifctz) shuifctz,\n" + 
			"           decode(y.jitcs,null,decode(s.fenx,'����',0,s.jitcs),y.jitcs) jitcs,\n" + 
			"           decode(y.kuc,null,decode(s.fenx,'����',0,s.kuc),y.kuc) kuc" +
			"  from (\n" + 
			"      SELECT g.id,j.id,p.id,yfs.id, x.fenx,y.id yuetjkjb_id,g.mingc gmingc,j.mingc jmingc,p.mingc pmingc,yfs.mingc ymingc,\n" +
			"		 nvl(k.kuc,0)kuc,nvl(ysl.jingz,0) shouml,nvl(k1.fady,0)fady,\n" +
			"		 nvl(k1.gongry,0)gongry,nvl(k1.qith,0)qith,nvl(k1.sunh,0)sunh,\n" +
			"			nvl(k1.diaocl,0)diaocl,nvl(k1.panyk,0)panyk,\n" +
			"			nvl(k1.shuifctz,0)shuifctz,nvl(k1.jitcs,0)jitcs" + 
			"        from yuetjkjb y,yueslb ysl\n" + 
			"             ,(select decode(0,0,'����') fenx from dual\n" + 
			"              union\n" + 
			"              select decode(0,0,'�ۼ�') fenx from dual) x,\n" + 
			"             gongysb g,\n" + 
			"             jihkjb j,\n" + 
			"             pinzb p,\n" + 
			"             yunsfsb yfs,\n" + 
			"             (\n" + 
			"             select h.fenx,kuc,y.gongysb_id,y.jihkjb_id,y.pinzb_id,y.yunsfsb_id\n" + 
			"               from yuehcb h, yuetjkjb y\n" + 
			"              where h.yuetjkjb_id = y.id\n" + 
			"                and h.fenx = '����'\n" + 
			"                and y.diancxxb_id=" + diancxxb_id + "\n" + 
			"                and y.riq=add_months("+CurrODate+", -1)\n" + 
			//"                and to_char(y.riq,'yyyy')="+getNianf()+"\n" + 
			"             )k,\n" + 
			"             (\n" + 
			"             select h.fenx,kuc,y.gongysb_id,y.jihkjb_id,y.pinzb_id,y.yunsfsb_id,h.fady,h.gongry,h.qith,h.sunh,h.diaocl,h.panyk,h.shuifctz,h.jitcs\n" + 
			"               from yuehcb h, yuetjkjb y\n" + 
			"              where h.yuetjkjb_id = y.id\n" + 
			"                and h.fenx = '�ۼ�'\n" + 
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
		// //���ñ��������ڱ���
		egu.setTableName("yuehcb");
		// /������ʾ������
		egu.setWidth("bodyWidth");
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("yid").setHeader("yid");
		egu.getColumn("yid").setWidth(120);
		egu.getColumn("yid").setEditor(null);
		egu.getColumn("yid").setHidden(true);
		egu.getColumn("gmingc").setHeader("������λ");
		egu.getColumn("gmingc").setWidth(120);
		egu.getColumn("GMINGC").setEditor(null);
		egu.getColumn("GMINGC").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jmingc").setHeader("�ƻ��ھ�");
		egu.getColumn("jmingc").setWidth(60);
		egu.getColumn("jmingc").setEditor(null);
		egu.getColumn("jmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pmingc").setHeader("Ʒ��");
		egu.getColumn("pmingc").setWidth(45);
		egu.getColumn("pmingc").setEditor(null);
		egu.getColumn("pmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("ymingc").setHeader("���䷽ʽ");
		egu.getColumn("ymingc").setWidth(60);
		egu.getColumn("ymingc").setEditor(null);
		egu.getColumn("ymingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("qickc").setHeader("�ڳ����");
		egu.getColumn("qickc").setWidth(70);
//		egu.getColumn("qickc").setEditor(null);
		
		egu.getColumn("shouml").setHeader("��ú��");
		egu.getColumn("shouml").setWidth(70);
		((NumberField)egu.getColumn("shouml").editor).setDecimalPrecision(0);
		egu.getColumn("shouml").setEditor(null);
		egu.getColumn("fady").setHeader("�����");
		egu.getColumn("fady").setWidth(70);
		egu.getColumn("fady").editor.setMinValue("0");
		
		egu.getColumn("gongry").setHeader("���Ⱥ�");
		egu.getColumn("gongry").setWidth(70);
		egu.getColumn("gongry").editor.setMinValue("0");
		
		egu.getColumn("qith").setHeader("������");
		egu.getColumn("qith").setWidth(70);
		egu.getColumn("qith").editor.setMinValue("0");
		
		egu.getColumn("sunh").setHeader("ʵ�ʴ���");
		egu.getColumn("sunh").setWidth(60);
		egu.getColumn("sunh").editor.setMinValue("0");
		
		egu.getColumn("diaocl").setHeader("������");
		egu.getColumn("diaocl").setWidth(60);	
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
		String Sql="select zhi from xitxxb x where x.leib='�±�' and x.danw='�Ĵ�' and beiz='ʹ��'";
		ResultSetList rs = con.getResultSetList(Sql);
		
		while (rs.next()){
			String zhi = rs.getString("zhi");
			if(egu.getColByHeader(zhi)!=null){
				egu.getColByHeader(zhi).hidden=true;
			}
		}
		
		egu.setDefaultsortable(false);  
		// /���ð�ť
		StringBuffer sb = new StringBuffer();	
		sb.append("\ngridDiv_grid.on('afteredit',function(e){");
		sb.append("CountAllKuc(gridDiv_ds);});\n");
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('YUETJKJB_ID')=='-1'){e.cancel=true;}");//��ͳ�ƿھ���ֵ��"-1"ʱ,��һ�в�����༭
		sb.append(" if(e.field=='QICKC'){ e.cancel=true;}");//�ڳ���治����༭
		sb.append("});");
		
		 //�趨�ϼ��в�����
//		sb.append("function gridDiv_save(record){if(record.get('gongysb_id')=='�ܼ�') return 'continue';}");
		
		egu.addOtherScript(sb.toString());
		
		egu.addTbarText("���");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		egu.addToolbarItem(comb2.getScript());
//		egu.addOtherScript("YuefDropDown.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
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
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con)){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
			
//			���水ť
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
				"Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');\n"+
				"}else{\n"+
				"var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';document.getElementById('SaveButton').click();Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"+
				"}\n"+
				"}";
//			ɾ��
			GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
			gbd.setDisabled(isLocked);
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
			//����
			GridButton gbs = new GridButton("����",save_script) ;
			gbs.setIcon(SysConstant.Btn_Icon_Save);
			gbs.setDisabled(isLocked);
			egu.addTbarBtn(gbs);
			
			Checkbox cb=new Checkbox();
			cb.setId("SelectLike");
			cb.setListeners("check:function(own,checked){if(checked){document.all.SelectLike.value='true'}else{document.all.SelectLike.value='false'}}");
			egu.addToolbarItem(cb.getScript());
			egu.addTbarText("�Ƿ��Զ������ۼ�ֵ");
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
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf()+"��"+getYuef()+"��";
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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
