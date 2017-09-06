package com.zhiren.dc.monthReport;

//import java.sql.ResultSet;
//import java.sql.SQLException;
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
//import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
//import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Chengbwh extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}

	public int getDataColumnCount() {
		int count = 0;
		for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
			if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
				count++;
			}
		}
		return count;
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else {
			return value;
		}
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
		getSelectData();
	} 
	
	public void Save1(String strchange,Visit visit) {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=getExtGrid();
		
		String mlongid="";
		try{
			
//			sql��sql2   Ϊ����feiyxmb���
//			sql3��sql4	Ϊ����feiyglb���
//			sql5��sql6	Ϊ����feiyxmmkglb���
			
			StringBuffer sql = new StringBuffer("begin \n");
			StringBuffer sql_hc = new StringBuffer();
			
			String tableName_sl="yueslb";
			String tableName_hc="yuehcb";
			
			String CurrODate = DateUtil.FormatOracleDate(getNianfValue()+"-"+getYuefValue()+"-01");//����
			
			String gongysb_id="0";
			String jihkjb_id="0";
			String pinzb_id="0";
			String yunsfsb_id="0";
			long yuejhkjb_id=0;
			int flag;
			
			ResultSetList delrsl = egu.getDeleteResultSet(strchange);
			while(delrsl.next()) {
				sql.append("delete from ").append(tableName_sl).append(" where id =").append(delrsl.getString(0)).append(";\n");
				sql_hc.append("delete from ").append(tableName_hc).append(" where id =").append(delrsl.getString(0)).append(";\n");
			}
			
			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while(mdrsl.next()) {
				StringBuffer sql_slnr = new StringBuffer();
				StringBuffer sql_hcnr = new StringBuffer();
				
				if("0".equals(mdrsl.getString("ID"))) {//�����ֶ�
					
					mlongid=MainGlobal.getNewID(visit.getDiancxxb_id());//����ID
					
					sql.append("insert into ").append(tableName_sl).append("(id");
					sql_slnr.append(mlongid);
					sql_hc.append("insert into ").append(tableName_hc).append("(id");
					sql_hcnr.append(mlongid);
					for(int i=1;i<mdrsl.getColumnCount();i++) {

						if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
							gongysb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if (mdrsl.getColumnNames()[i].equals("JIHKJB_ID")){
							jihkjb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if (mdrsl.getColumnNames()[i].equals("PINZB_ID")){
							pinzb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if (mdrsl.getColumnNames()[i].equals("YUNSFSB_ID")){
							yunsfsb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if(mdrsl.getColumnNames()[i].equals("FENX")){
							//���������ֶ�
							sql.append(",").append(mdrsl.getColumnNames()[i]);//��ͷ
							
							sql_slnr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));//����
						
							sql_hc.append(",").append(mdrsl.getColumnNames()[i]);//��ͷ
							
							sql_hcnr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));//����
						
						}else if(mdrsl.getColumnNames()[i].equals("FADY")||mdrsl.getColumnNames()[i].equals("GONGRY")
								||mdrsl.getColumnNames()[i].equals("QITH")||mdrsl.getColumnNames()[i].equals("SUNH")
								||mdrsl.getColumnNames()[i].equals("DIAOCL")||mdrsl.getColumnNames()[i].equals("PANYK")
								||mdrsl.getColumnNames()[i].equals("KUC")){
							//�Ĵ�������ֶ�
							sql_hc.append(",").append(mdrsl.getColumnNames()[i]);//��ͷ
							
							sql_hcnr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));//����
						}else{
							//���ݱ������ֶ�
							sql.append(",").append(mdrsl.getColumnNames()[i]);//��ͷ
							
							sql_slnr.append(",").append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));//����
						}

					}
					
					//����YUEJHKJB_ID
					String strSql="select * from yuetjkjb where riq="+CurrODate+" and diancxxb_id="+visit.getDiancxxb_id()
								+" and gongysb_id="+gongysb_id+" and jihkjb_id="+jihkjb_id+" and pinzb_id="+pinzb_id+" and yunsfsb_id="+yunsfsb_id+"";
					ResultSetList rs=con.getResultSetList(strSql);
					if (rs == null) {
						WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
						setMsg(ErrorMessage.NullResult);
						con.rollBack();
						con.Close();
						return;
					}
					if (rs.next()){//������Ӧ��YUEJHKJB_ID
						yuejhkjb_id=rs.getLong("id");
					}else{//û�����ID���½�ID
						yuejhkjb_id=Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
						strSql = "insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
							+yuejhkjb_id+","+CurrODate+","+visit.getDiancxxb_id()+",0,"+gongysb_id
							+","+pinzb_id+","+jihkjb_id+","+yunsfsb_id+")";
						flag = con.getInsert(strSql);
						if(flag == -1) {
							WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
							setMsg("���ɹ��̳��ִ�����ͳ�ƿھ�δ����ɹ���");
							con.rollBack();
							con.Close();
							return;
						}
					}
					
					sql.append(",yuetjkjb_id) values(").append(sql_slnr).append(","+yuejhkjb_id+");\n");
					sql_hc.append(",yuetjkjb_id) values(").append(sql_hcnr).append(","+yuejhkjb_id+");\n");
					
				}else {//�޸�����
					
					sql.append("update ").append(tableName_sl).append(" set ");
					sql_hc.append("update ").append(tableName_hc).append(" set ");
					
					for(int i=1;i<mdrsl.getColumnCount();i++) {
						
						if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
							gongysb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if (mdrsl.getColumnNames()[i].equals("JIHKJB_ID")){
							jihkjb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if (mdrsl.getColumnNames()[i].equals("PINZB_ID")){
							pinzb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if (mdrsl.getColumnNames()[i].equals("YUNSFSB_ID")){
							yunsfsb_id=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i));
						}else if(mdrsl.getColumnNames()[i].equals("FENX")){
							//���������ֶ�
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");//��ͷ
							
							sql.append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");//ֵ
							
							sql_hc.append(mdrsl.getColumnNames()[i]).append(" = ");//��ͷ
							
							sql_hc.append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");//ֵ
							
						}else if(mdrsl.getColumnNames()[i].equals("FADY")||mdrsl.getColumnNames()[i].equals("GONGRY")
								||mdrsl.getColumnNames()[i].equals("QITH")||mdrsl.getColumnNames()[i].equals("SUNH")
								||mdrsl.getColumnNames()[i].equals("DIAOCL")||mdrsl.getColumnNames()[i].equals("PANYK")
								||mdrsl.getColumnNames()[i].equals("KUC")){
							//�Ĵ�������ֶ�
							sql_hc.append(mdrsl.getColumnNames()[i]).append(" = ");//��ͷ
							
							sql_hc.append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");//ֵ
							
						}else{
							//���ݱ������ֶ�
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");//��ͷ
							
							sql.append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");//ֵ
							
						}
					}
					
//					����YUEJHKJB_ID
					String strSql="select * from yuetjkjb where riq="+CurrODate+" and diancxxb_id="+visit.getDiancxxb_id()
								+" and gongysb_id="+gongysb_id+" and jihkjb_id="+jihkjb_id+" and pinzb_id="+pinzb_id+" and yunsfsb_id="+yunsfsb_id+"";
					ResultSetList rs=con.getResultSetList(strSql);
					if (rs == null) {
						WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
						setMsg(ErrorMessage.NullResult);
						con.rollBack();
						con.Close();
						return;
					}
					if (rs.next()){//������Ӧ��YUEJHKJB_ID
						yuejhkjb_id=rs.getLong("id");
					}else{//û�����ID���½�ID
						yuejhkjb_id=Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
						strSql = "insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
							+yuejhkjb_id+","+CurrODate+","+visit.getDiancxxb_id()+",0,"+gongysb_id
							+","+pinzb_id+","+jihkjb_id+","+yunsfsb_id+")";
						flag = con.getInsert(strSql);
						if(flag == -1) {
							WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
							setMsg("���ɹ��̳��ִ�����ͳ�ƿھ�δ����ɹ���");
							con.rollBack();
							con.Close();
							return;
						}
					}
					
					sql.deleteCharAt(sql.length()-1);
					sql_hc.deleteCharAt(sql_hc.length()-1);
					
					sql.append(" ,yuetjkjb_id="+yuejhkjb_id+" where id =").append(mdrsl.getString("ID")).append(";\n");
					sql_hc.append(" ,yuetjkjb_id="+yuejhkjb_id+" where id =").append(mdrsl.getString("ID")).append(";\n");
				}
			}
			sql.append(sql_hc);
			sql.append("end;");
			con.getUpdate(sql.toString());
			Save_hj(CurrODate);//����ϼƱ�
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}

	public void Save_hj(String CurrODate) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrZnDate = getNianfValue()+"��"+getYuefValue()+"��";
		String strSql="";
		int flag = 0;

		
//		ɾ��������
		strSql = "delete from yueshchjb where riq="+CurrODate
		+" and diancxxb_id = "+visit.getDiancxxb_id();
		flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("���ɹ��̳��ִ������պĴ�ϼ�ɾ��ʧ�ܣ�");
			con.rollBack();
			con.Close();
			return;
		}
		//�óɺĴ��Ļ�������
		strSql="select fenx,sum(yh.qickc) as qickc,sum(yh.shouml) as shouml,\n" +
			"sum(yh.fady) as fady,sum(yh.gongry) as gongry,sum(yh.qith) as qith,\n" + 
			"sum(yh.sunh) as sunh,sum(yh.diaocl) as diaocl,sum(yh.panyk) as panyk,sum(yh.kuc) as kuc\n" + 
			"from yuehcb yh,yuetjkjb yj\n" + 
			"where yh.yuetjkjb_id=yj.id and yj.diancxxb_id="+visit.getDiancxxb_id()+"\n" + 
			"and yj.riq="+CurrODate+"\n" + 
			"group by fenx";
		
		ResultSetList rsl = con.getResultSetList(strSql);
		
		long id=0;
		long diancxxb_id=0;
		String riq="";
		String fenx="";
		double qickc=0;
		double shouml=0;
		double fady=0;
		double gongry=0;
		double qith=0;
		double sunh=0;
		double diaocl=0;
		double panyk=0;
		double kuc=0;
		
		while (rsl.next()){
			id=Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
			diancxxb_id=visit.getDiancxxb_id();
			riq=CurrODate;
			fenx=rsl.getString("fenx");
			qickc=rsl.getDouble("qickc");
			shouml=rsl.getDouble("shouml");
			fady=rsl.getDouble("fady");
			gongry=rsl.getDouble("gongry");
			qith=rsl.getDouble("qith");
			sunh=rsl.getDouble("sunh");
			diaocl=rsl.getDouble("diaocl");
			panyk=rsl.getDouble("panyk");
			kuc=rsl.getDouble("kuc");
			
			strSql = "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc) values("
				+ id
				+ "," + diancxxb_id
				+","+riq+",'"+fenx+"',"+qickc+","+shouml+","
				+ fady +","+gongry+","+qith+","+sunh+","
				+ diaocl+","+panyk+","+kuc+")";
			flag = con.getInsert(strSql);
			if(flag == -1) {
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
				setMsg("���ɹ��̳��ִ������պĴ�ϼ�δ����ɹ���");
				con.rollBack();
				con.Close();
				return;
			}
		}
		
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"�����ݱ���ɹ���");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if(_DeleteChick){
			_DeleteChick= false;
			delete();
		}
	}
	
	public void delete(){
		JDBCcon con = new JDBCcon();
		String id =getChangeid();//�õ���ѡ�е�ID
		String sql="";
		sql=" delete from yueslb where id ="+id;
		con.getUpdate(sql);
		sql="delete from yuehcb where id ="+id;
		con.getUpdate(sql);
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianfValue()+"-"+getYuefValue()+"-01");

		String Strsql="select sl.id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n" +
		"       yunsfsb.mingc as yunsfsb_id,sl.fenx,sl.jingz,sl.biaoz,sl.yingd,sl.kuid,sl.yuns,sl.koud,sl.kous,\n" + 
		"       sl.kouz,sl.koum,sl.zongkd,sl.sanfsl,sl.jianjl,sl.ructzl,yh.fady,yh.gongry,yh.qith,yh.sunh,yh.diaocl,\n" + 
		"       yh.panyk,yh.kuc\n" + 
		" from yuetjkjb tj,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb,yuehcb yh\n" + 
		" where tj.id=sl.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n" + 
		"      and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and sl.id=yh.id and diancxxb_id="+visit.getDiancxxb_id()+"\n" + 
		"      and riq="+CurrODate+" order by sl.id";
		
		ResultSetList rsl = con.getResultSetList(Strsql);
						
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yueslb");
//		egu.setWidth(1000);
		egu.setWidth("bodyWidth");
		
		//����
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("jingz").setDefaultValue("0");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setDefaultValue("0");
		egu.getColumn("yingd").setHeader(Locale.yingd_fahb);
		egu.getColumn("yingd").setWidth(60);
		egu.getColumn("yingd").setDefaultValue("0");
		egu.getColumn("kuid").setHeader(Locale.kuid_fahb);
		egu.getColumn("kuid").setWidth(60);
		egu.getColumn("kuid").setDefaultValue("0");
		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("yuns").setDefaultValue("0");
		egu.getColumn("koud").setHeader(Locale.koud_fahb);
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("koud").setDefaultValue("0");
		egu.getColumn("kous").setHeader(Locale.kous_fahb);
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kouz").setHeader(Locale.kouz_fahb);
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("koum").setHeader(Locale.koum_fahb);
		egu.getColumn("koum").setWidth(60);
		egu.getColumn("koum").setDefaultValue("0");
		egu.getColumn("zongkd").setHeader(Locale.zongkd_fahb);
		egu.getColumn("zongkd").setWidth(60);
		egu.getColumn("zongkd").setDefaultValue("0");
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_fahb);
		egu.getColumn("sanfsl").setWidth(60);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("jianjl").setHeader(Locale.MRtp_jianjl);
		egu.getColumn("jianjl").setWidth(60);
		egu.getColumn("jianjl").setDefaultValue("0");
		egu.getColumn("ructzl").setHeader(Locale.MRtp_ructzl);
		egu.getColumn("ructzl").setWidth(90);
		egu.getColumn("ructzl").setDefaultValue("0");
		
		//�Ĵ�
		egu.getColumn("fady").setHeader("�����");
		egu.getColumn("fady").setWidth(80);
		egu.getColumn("fady").setDefaultValue("0");
		egu.getColumn("gongry").setHeader("���Ⱥ�");
		egu.getColumn("gongry").setWidth(80);
		egu.getColumn("gongry").setDefaultValue("0");
		egu.getColumn("qith").setHeader("������");
		egu.getColumn("qith").setWidth(80);
		egu.getColumn("qith").setDefaultValue("0");
		egu.getColumn("sunh").setHeader("���");
		egu.getColumn("sunh").setWidth(60);
		egu.getColumn("sunh").setDefaultValue("0");
		egu.getColumn("diaocl").setHeader("������");
		egu.getColumn("diaocl").setWidth(60);	
		egu.getColumn("diaocl").setDefaultValue("0");
		egu.getColumn("panyk").setHeader("��ӯ��");
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("panyk").setDefaultValue("0");
		egu.getColumn("kuc").setHeader("���");
		egu.getColumn("kuc").setWidth(60);
		egu.getColumn("kuc").setDefaultValue("0");
		egu.getColumn("fenx").editor = null;
		
//		egu.getColumn("gongysb_id").update=false;
//		egu.getColumn("jihkjb_id").update=false;
//		egu.getColumn("pinzb_id").update=false;
//		egu.getColumn("yunsfsb_id").update=false;
		
		
		// egu.getColumn("diancxxb_id").setHidden(true);
		// egu.getColumn("diancxxb_id").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		// egu.getColumn("toucrq").setEditor(new DateField());
		//��λ����������
		//�糧������
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		//leix=0����ú�����
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from gongysb where (fuid=0 or fuid=-1) and leix=0 order by xuh "));
		egu.getColumn("gongysb_id").setReturnId(true);

		//�ƻ��ھ�
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from jihkjb"));
		egu.getColumn("jihkjb_id").setReturnId(true);

		//Ʒ��
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from pinzb"));
		egu.getColumn("pinzb_id").setReturnId(true);
		
		//���䷽ʽ 
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from yunsfsb "));
		egu.getColumn("yunsfsb_id").setReturnId(true);		
		
		//����
		List fenxlist = new ArrayList();
		fenxlist.add(new IDropDownBean(0, "����"));
		fenxlist.add(new IDropDownBean(1, "�ۼ�"));
		egu.getColumn("fenx").setEditor(new ComboBox());
		egu.getColumn("fenx").setComboEditor(egu.gridId, new IDropDownModel(fenxlist));
		egu.getColumn("fenx").setReturnId(false);
		egu.getColumn("fenx").setDefaultValue("����");
		
		egu.addTbarText("���:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("�·�:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		
//		 �趨�������������Զ�ˢ��
		egu.addOtherScript("NianfDropDown.on('select',function(){document.forms[0].submit();});YuefDropDown.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-"); 
//		egu.getColumn("diancxxb_id").setDefaultValue(
//				"" +  visit.getDiancxxb_id());
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		/*String s = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Jizreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + s + "}").getScript()
				+ "}");*/
//		ɾ����ť
		GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
//		gbd.setDisabled(isNotReady);
//		if(isLocked) {
			gbd.setHandler("function (){" +
						" var grid1_history =\"\";" +
						" if(gridDiv_sm.getSelected()==null){ " +
						"	Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"��ѡ��һ�����ݣ�\");  return; } " +
						" grid1_rcd = gridDiv_sm.getSelected();" +
//						" if(grid1_rcd.get(\"ID\") == \"0\"){ " +
//						"	Ext.MessageBox.alert(\"��ʾ��Ϣ\",\"������Ȩ��֮ǰ���ȱ���!\"); return; }" +
						" grid1_history = grid1_rcd.get(\"ID\");" +
						" var Cobj = document.getElementById(\"CHANGEID\");" +
						" Cobj.value = grid1_history; " +
						" document.getElementById(\"DeleteButton\").click();}");
//		}
//		if(isLocked!=0) {
//			if(isLocked==1){//���ñ����ݴ���
//				gbd.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueshchjb_shouml,false)+"return;}");
//			}else{//�¸���������
//				gbd.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueshchjb_nextmonthe,false)+"return;}");
//			}
//		}
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianfValue()+"��"+getYuefValue()+"��";
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
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();
//			getSelectData();
		}
		getSelectData();
	}

	

//	 ���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
}