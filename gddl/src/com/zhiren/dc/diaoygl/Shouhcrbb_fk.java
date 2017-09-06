package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Shouhcrbb_fk extends BasePage {
//		界面用户提示
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
//		绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//		页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	public void Save1(String strchange, Visit visit) {
		String tableName = "fenkshcrbb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();

			sql2.append("getnewid(").append(getTreeid()).append(")");
			sql2.append(",to_date('").append(getRiq()).append("','yyyy-mm-dd')");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id,riq");

				for (int i = 1; i < mdrsl.getColumnCount(); i++) {

					sql.append(",").append(mdrsl.getColumnNames()[i]);
					if(mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")){
						sql2.append(",").append(getTreeid());
					}else if (mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")) {
						sql2.append(",").append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl.getString("meikxxb_id")));
					}else if (mdrsl.getColumnNames()[i].equals("JIHKJB_ID")) {
						sql2.append(",").append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl
												.getString("jihkjb_id")));
					}else if (mdrsl.getColumnNames()[i].equals("PINZB_ID")) {
						sql2.append(",").append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl
												.getString("pinzb_id")));
					}else if (mdrsl.getColumnNames()[i].equals("YUNSFSB_ID")) {
						sql2.append(",").append(
								(getExtGrid().getColumn(
										mdrsl.getColumnNames()[i]).combo)
										.getBeanId(mdrsl
												.getString("yunsfsb_id")));
					}else {
						sql2.append(",").append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i)));
					}
				}

				sql.append(") values(").append(sql2).append(");\n");
			}else if("".equals(mdrsl.getString("ID"))){
				
			}else{	
				String insert = "select zhi from xitxxb where mingc ='收耗存日报是否显示添加按钮' and zhuangt = 1 and diancxxb_id = "+
			 	visit.getDiancxxb_id();
				ResultSetList insertRS = con.getResultSetList(insert);
				if(insertRS.next()){
					if(insertRS.getString("zhi").equals("是")){
						sql.append("update fenkshcrbb set meikxxb_id="+(getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"))+" \n"+
								",jihkjb_id=" +(getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id"))+" \n"+
								",pinzb_id=" +(getExtGrid().getColumn("pinzb_id").combo).getBeanId(mdrsl.getString("pinzb_id"))+" \n"+
								",yunsfsb_id=" +(getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(mdrsl.getString("yunsfsb_id"))+" \n"+
								",quanyjh=" +mdrsl.getString("quanyjh")+" \n"+
								",shangrkc=" +mdrsl.getString("shangrkc")+" \n"+
								",dangrlm=" +mdrsl.getString("dangrlm")+" \n"+
								",laimlj=" +mdrsl.getString("laimlj")+" \n"+
								",fady=" +mdrsl.getString("fady")+" \n"+
								",gongry=" +mdrsl.getString("gongry")+" \n"+
								",qity=" +mdrsl.getString("qity")+" \n"+
								",cuns=" +mdrsl.getString("cuns")+" \n"+
								",panyk=" +mdrsl.getString("panyk")+" \n"+
								",tiaozl=" +mdrsl.getString("tiaozl")+" \n"+
								",haomlj=" +mdrsl.getString("haomlj")+" \n"+
								",benrjc=" +mdrsl.getString("benrjc")+" \n"+
								",beiz='" +mdrsl.getString("beiz")+"' where id = " +mdrsl.getString("id")+
								";\n");
					}else{
						sql.append("update fenkshcrbb set quanyjh=" +mdrsl.getString("quanyjh")+" \n"+
								",shangrkc=" +mdrsl.getString("shangrkc")+" \n"+
								",dangrlm=" +mdrsl.getString("dangrlm")+" \n"+
								",laimlj=" +mdrsl.getString("laimlj")+" \n"+
								",fady=" +mdrsl.getString("fady")+" \n"+
								",gongry=" +mdrsl.getString("gongry")+" \n"+
								",qity=" +mdrsl.getString("qity")+" \n"+
								",cuns=" +mdrsl.getString("cuns")+" \n"+
								",panyk=" +mdrsl.getString("panyk")+" \n"+
								",tiaozl=" +mdrsl.getString("tiaozl")+" \n"+
								",haomlj=" +mdrsl.getString("haomlj")+" \n"+
								",benrjc=" +mdrsl.getString("benrjc")+" \n"+
								",beiz='" +mdrsl.getString("beiz")+"' where id = " +mdrsl.getString("id")+
								";\n");
					}
				}else{
					sql.append("update fenkshcrbb set quanyjh=" +mdrsl.getString("quanyjh")+" \n"+
								",shangrkc=" +mdrsl.getString("shangrkc")+" \n"+
								",dangrlm=" +mdrsl.getString("dangrlm")+" \n"+
								",laimlj=" +mdrsl.getString("laimlj")+" \n"+
								",fady=" +mdrsl.getString("fady")+" \n"+
								",gongry=" +mdrsl.getString("gongry")+" \n"+
								",qity=" +mdrsl.getString("qity")+" \n"+
								",cuns=" +mdrsl.getString("cuns")+" \n"+
								",panyk=" +mdrsl.getString("panyk")+" \n"+
								",tiaozl=" +mdrsl.getString("tiaozl")+" \n"+
								",haomlj=" +mdrsl.getString("haomlj")+" \n"+
								",benrjc=" +mdrsl.getString("benrjc")+" \n"+
								",beiz='" +mdrsl.getString("beiz")+"' where id = " +mdrsl.getString("id")+
								";\n");
				}

			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
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
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
		} else {
			return value;
		}
	}
	private String beforeData(String tablename,String columnname,String diancxxb_id,String meikxxb_id,String yunsfsb_id,String pinzb_id,String jihkjb_id,String date){
		String beforeDataSql="select decode(sum("+columnname+"),null,0,sum("+columnname+")) as haomlj \n"+
           "from "+tablename+" \n"+
          "where diancxxb_id = "+diancxxb_id+" \n"+
            "and meikxxb_id = "+meikxxb_id+" \n"+
            "and riq=to_date('"+date+"','yyyy-mm-dd') \n"+
            "and yunsfsb_id= "+yunsfsb_id+" \n"+
            "and jihkjb_id= "+jihkjb_id+" \n"+
            "and pinzb_id= "+pinzb_id;
		return beforeDataSql;
	}
	private void CreateData() {
		DelData();
		String beforeDate = DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiq()),-1,DateUtil.AddType_intDay));
		String diancxxb_id = "";
		StringBuffer insertSQL= new StringBuffer();
		String updateSQL= "";
		if(getTreeid()!=null) {
			diancxxb_id = getTreeid();
		}
		JDBCcon con = new JDBCcon();
		String f_meik="select meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id from fahb where daohrq=to_date('"+getRiq()+"','yyyy-mm-dd') and diancxxb_id="+diancxxb_id+" group by meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id";
		String fk_meik="select meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id from fenkshcrbb where riq=to_date('"+beforeDate+"','yyyy-mm-dd') and diancxxb_id="+diancxxb_id;
		String fs_meik="select meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id from fenkshcrbb where meikxxb_id not in (select meikxxb_id from fahb where daohrq=to_date('"+getRiq()+"','yyyy-mm-dd') and diancxxb_id="+diancxxb_id+" group by meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id) and riq=to_date('"+beforeDate+"','yyyy-mm-dd') and diancxxb_id="+diancxxb_id;
		String scSQL=fk_meik+" \n"+ "union \n"+ f_meik;
		ResultSetList f_meikRS=con.getResultSetList(f_meik);
		ResultSetList fs_meikRS=con.getResultSetList(fs_meik);
		ResultSetList meikRS=con.getResultSetList(scSQL);
		
		ArrayList list=new ArrayList();
		insertSQL.append("begin \n");
		while(meikRS.next()){
			insertSQL.append("insert into fenkshcrbb(ID, DIANCXXB_ID, MEIKXXB_ID, JIHKJB_ID, PINZB_ID, YUNSFSB_ID, RIQ, QUANYJH, SHANGRKC, DANGRLM, LAIMLJ, HAOMLJ, BENRJC, BEIZ) " +
					"values(getnewid("+diancxxb_id+"),"+diancxxb_id+","+meikRS.getString("meikxxb_id")+","+meikRS.getString("jihkjb_id")+","+meikRS.getString("pinzb_id")+","+meikRS.getString("yunsfsb_id")+",to_date('"+getRiq()+"','yyyy-mm-dd'),0,0,0,0,0,0,''); \n");
		}
		while(f_meikRS.next()){			
			updateSQL="select "+diancxxb_id+" as diancxxb_id,"+f_meikRS.getString("meikxxb_id")+" as meikxxb_id,"+f_meikRS.getString("jihkjb_id")+" as jihkjb_id,"+f_meikRS.getString("pinzb_id")+" as pinzb_id,"+f_meikRS.getString("yunsfsb_id")+" as yunsfsb_id,to_date('"+getRiq()+"','yyyy-mm-dd') as riq, \n" +
					"       round_new(sum(("+beforeData("fenkshcrbb","benrjc",diancxxb_id,f_meikRS.getString("meikxxb_id"),f_meikRS.getString("yunsfsb_id"),f_meikRS.getString("pinzb_id"),f_meikRS.getString("jihkjb_id"),beforeDate)+")),0) as shangrkc, \n"+
					"		round_new(sum(fh.laimsl),0) as dangrlm, \n"+
					"		round_new(sum(("+beforeData("fenkshcrbb","laimlj",diancxxb_id,f_meikRS.getString("meikxxb_id"),f_meikRS.getString("yunsfsb_id"),f_meikRS.getString("pinzb_id"),f_meikRS.getString("jihkjb_id"),beforeDate)+")+ fh.laimsl),0) as laimlj, \n"+
					"		round_new(sum(("+beforeData("fenkshcrbb","haomlj",diancxxb_id,f_meikRS.getString("meikxxb_id"),f_meikRS.getString("yunsfsb_id"),f_meikRS.getString("pinzb_id"),f_meikRS.getString("jihkjb_id"),beforeDate)+")),0) as haomlj, \n"+
					"		round_new(sum(("+beforeData("fenkshcrbb","benrjc",diancxxb_id,f_meikRS.getString("meikxxb_id"),f_meikRS.getString("yunsfsb_id"),f_meikRS.getString("pinzb_id"),f_meikRS.getString("jihkjb_id"),beforeDate)+")+ fh.laimsl + 0),0) as benrjc \n"+
					"from fahb fh \n"+
					" where daohrq = to_date('"+getRiq()+"', 'yyyy-mm-dd') \n"+
					"	and fh.diancxxb_id = "+diancxxb_id+" \n"+
					"	and fh.meikxxb_id = "+f_meikRS.getString("meikxxb_id")+" \n"+
					"	and fh.yunsfsb_id="+f_meikRS.getString("yunsfsb_id")+" \n"+
					"	and fh.pinzb_id="+f_meikRS.getString("pinzb_id")+" \n"+
					"	and fh.jihkjb_id="+f_meikRS.getString("jihkjb_id"); 
			ResultSetList updateRS =con.getResultSetList(updateSQL.toString());
			list.add(updateRS);
		}
		while(fs_meikRS.next()){
			updateSQL="select "+diancxxb_id+" as diancxxb_id,"+fs_meikRS.getString("meikxxb_id")+" as meikxxb_id,"+fs_meikRS.getString("jihkjb_id")+" as jihkjb_id,"+fs_meikRS.getString("pinzb_id")+" as pinzb_id,"+fs_meikRS.getString("yunsfsb_id")+" as yunsfsb_id,to_date('"+getRiq()+"','yyyy-mm-dd') as riq, \n" +
			"       round_new(sum(benrjc),0) as shangrkc, \n"+
			"		0 as dangrlm, \n"+
			"		round_new(sum(laimlj),0) as laimlj, \n"+
			"		round_new(sum(haomlj),0) as haomlj, \n"+
			"		round_new(sum(benrjc),0) as benrjc  \n"+
			"from fenkshcrbb fh \n"+
			" where riq = to_date('"+beforeDate+"', 'yyyy-mm-dd') \n"+
			"	and fh.diancxxb_id = "+diancxxb_id+" \n"+
			"	and fh.meikxxb_id = "+fs_meikRS.getString("meikxxb_id")+" \n"+
			"	and fh.yunsfsb_id="+fs_meikRS.getString("yunsfsb_id")+" \n"+
			"	and fh.pinzb_id="+fs_meikRS.getString("pinzb_id")+" \n"+
			"	and fh.jihkjb_id="+fs_meikRS.getString("jihkjb_id"); 
			ResultSetList updateRS =con.getResultSetList(updateSQL.toString());
			list.add(updateRS);
		}
		insertSQL.append("end;");
		if(meikRS.getRows()>0){
			con.getInsert(insertSQL.toString());
			UpdateData(list);
		}
		con.Close();
		
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = "";
		if(getTreeid()!=null) {
			diancxxb_id = getTreeid();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("delete from fenkshcrbb where diancxxb_id=")
		.append(diancxxb_id).append(" and riq=")
		.append(CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		con.Close();
	}
	private void UpdateData(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		String beforeDate = DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiq()),-1,DateUtil.AddType_intDay));
		sb.append("begin \n");
		if(list.size()<=0){
			JDBCcon con = new JDBCcon();
			String sql="select * from fenkshcrbb where riqriq=to_date('"+beforeDate+"','yyyy-mm-dd')";
			ResultSetList sqlRs=con.getResultSetList(sql);
			while(sqlRs.next()){
				sb.append("update fenkshcrbb set shangrkc=" +sqlRs.getString("benrjc")+
						",benrjc=" +sqlRs.getString("benrjc")+
						" where diancxxb_id=" +sqlRs.getString("diancxxb_id")+
						" and meikxxb_id=" +sqlRs.getString("meikxxb_id")+
						" and jihkjb_id=" +sqlRs.getString("jihkjb_id")+
						" and pinzb_id=" +sqlRs.getString("pinzb_id")+
						" and yunsfsb_id=" +sqlRs.getString("yunsfsb_id")+
						" and riq=to_date('"+getRiq()+"','yyyy-mm-dd'); \n");
			}
		}else{
			for(int i=0;i<list.size();i++){	
				ResultSetList updateRS=(ResultSetList)list.get(i);
				System.out.println(updateRS.getRows());
				while(updateRS.next()){				
					sb.append("update fenkshcrbb set shangrkc=" +updateRS.getString("shangrkc")+
							",dangrlm=" +updateRS.getString("dangrlm")+
							",laimlj=" +updateRS.getString("laimlj")+
							",haomlj=" +updateRS.getString("haomlj")+
							",benrjc=" +updateRS.getString("benrjc")+
							" where diancxxb_id=" +updateRS.getString("diancxxb_id")+
							" and meikxxb_id=" +updateRS.getString("meikxxb_id")+
							" and jihkjb_id=" +updateRS.getString("jihkjb_id")+
							" and pinzb_id=" +updateRS.getString("pinzb_id")+
							" and yunsfsb_id=" +updateRS.getString("yunsfsb_id")+
							" and riq=to_date('"+DateUtil.FormatDate(updateRS.getDate("riq"))+"','yyyy-mm-dd'); \n");
				}
				
			}
		}
		sb.append("end;");
		JDBCcon con = new JDBCcon();
		con.getUpdate(sb.toString());
		con.Close();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		if (_SaveClick) {
			_SaveClick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			String insert = "select zhi from xitxxb where mingc ='收耗存日报是否显示添加按钮' and zhuangt = 1 and diancxxb_id = "+
		 	visit.getDiancxxb_id();
			ResultSetList insertRS = con.getResultSetList(insert);
			if(insertRS.next()){
				if(insertRS.getString("zhi").equals("否")){
					DelData();
					getSelectData();
				}
			}

		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String beforeDate = DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiq()),-1,DateUtil.AddType_intDay));
		String diancxxb_id = "";
		if(getTreeid()!=null) {
			diancxxb_id = getTreeid();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select (select max(id) + 1 from fenkshcrbb) as id,"+diancxxb_id+" as diancxxb_id,'合计' as meikxxb_id, '' as jihkjb_id,'' as pinzb_id,'' as yunsfsb_id,0 as quanyjh, " +
				"(select kuc from shouhcrbb where riq = to_date('"+beforeDate+"', 'yyyy-mm-dd') and diancxxb_id = "+diancxxb_id+") as shangrkc, " +
				"dangrgm as dangrlm, " +
				"(select decode(sum(laimlj),null,0,sum(laimlj)) from fenkshcrbb where diancxxb_id = "+diancxxb_id+" and riq= to_date('"+beforeDate+"', 'yyyy-mm-dd'))+dangrgm as laimlj, " +
				"fady,gongry,qity,cuns,panyk,shuifctz as tiaozl, " +
				"(select decode(sum(haomlj),null,0,sum(haomlj)) from fenkshcrbb where diancxxb_id = "+diancxxb_id+" and riq= to_date('"+beforeDate+"', 'yyyy-mm-dd'))+fady+gongry+qity as haomlj, " +
				"kuc as benrjc,'' as beiz " +
				"from shouhcrbb where riq = "+CurDate+" and diancxxb_id = "+diancxxb_id+" \n");
		sb.append("union \n");
		sb.append("select f.id,diancxxb_id,m.mingc as meikxxb_id,j.mingc as jihkjb_id,p.mingc as pinzb_id,y.mingc as yunsfsb_id,quanyjh,shangrkc,dangrlm,laimlj,fady,gongry,qity,cuns,panyk,tiaozl,haomlj,benrjc,f.beiz " +
				"from fenkshcrbb f,meikxxb m,jihkjb j,pinzb p,yunsfsb y " +
				"where f.meikxxb_id=m.id and f.jihkjb_id=j.id and f.pinzb_id=p.id and f.yunsfsb_id=y.id " +
				"and f.riq="+CurDate+" and f.diancxxb_id="+diancxxb_id);
		sb.append(" order by id desc");
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fenkshcrbb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("单位名称");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("quanyjh").setHeader("全月计划");
		egu.getColumn("quanyjh").setWidth(60);

		egu.getColumn("shangrkc").setHeader("上日库存");
		egu.getColumn("shangrkc").setWidth(60);

		egu.getColumn("dangrlm").setHeader("当日来煤");
		egu.getColumn("dangrlm").setWidth(60);
		
		egu.getColumn("laimlj").setHeader("来煤累计");
		egu.getColumn("laimlj").setWidth(60);	
		
		egu.getColumn("fady").setHeader("发电用");
		egu.getColumn("fady").setWidth(60);
		egu.getColumn("gongry").setHeader("供热用");
		egu.getColumn("gongry").setWidth(60);
		egu.getColumn("qity").setHeader("其它用");
		egu.getColumn("qity").setWidth(60);
		egu.getColumn("cuns").setHeader("存损");
		egu.getColumn("cuns").setWidth(60);
		egu.getColumn("panyk").setHeader("盘盈亏");
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("tiaozl").setHeader("调整量");
		egu.getColumn("tiaozl").setWidth(60);
		
		egu.getColumn("haomlj").setHeader("耗煤累计");
		egu.getColumn("haomlj").setWidth(60);
		
		egu.getColumn("benrjc").setHeader("本日结存");
		egu.getColumn("benrjc").setWidth(60);
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(80);
		
		String insert = "select zhi from xitxxb where mingc ='收耗存日报是否显示添加按钮' and zhuangt = 1 and diancxxb_id = "+
	 	visit.getDiancxxb_id();
		ResultSetList insertRS = con.getResultSetList(insert);
		if(insertRS.next()){
			if(insertRS.getString("zhi").equals("是")){
				insertRS.beforefirst();
//				   煤矿单位下拉框
				ComboBox gongys = new ComboBox();
				egu.getColumn("meikxxb_id").setEditor(gongys);
				String gongysSql = "select id,mingc from meikxxb order by xuh";
				egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
						new IDropDownModel(gongysSql));
//				   计划口径下拉框
				ComboBox jihkj = new ComboBox();
				egu.getColumn("jihkjb_id").setEditor(jihkj);
				String jihkjSql = "select id,mingc from jihkjb order by xuh";
				egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
						new IDropDownModel(jihkjSql));
//				   品种下拉框
				ComboBox pinz = new ComboBox();
				egu.getColumn("pinzb_id").setEditor(pinz);
				String pinzSql = "select id,mingc from pinzb order by xuh";
				egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
						new IDropDownModel(pinzSql));
//				   运输方式下拉框
				ComboBox yunsfs = new ComboBox();
				egu.getColumn("yunsfsb_id").setEditor(yunsfs);
				String yunsfsSql = "select id,mingc from yunsfsb";
				egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
						new IDropDownModel(yunsfsSql));	
			}else{
				if(!MainGlobal.getXitxx_item("收耗存日报", "收耗存日报来煤可编辑", "0", "否").equals("是")){
					egu.getColumn("shangrkc").setEditor(null);
					egu.getColumn("dangrlm").setEditor(null);
					egu.getColumn("laimlj").setEditor(null);
					egu.getColumn("haomlj").setEditor(null);
					egu.getColumn("benrjc").setEditor(null);
				}
			}
		}else{
			if(!MainGlobal.getXitxx_item("收耗存日报", "收耗存日报来煤可编辑", "0", "否").equals("是")){
				egu.getColumn("shangrkc").setEditor(null);
				egu.getColumn("dangrlm").setEditor(null);
				egu.getColumn("laimlj").setEditor(null);
				egu.getColumn("haomlj").setEditor(null);
				egu.getColumn("benrjc").setEditor(null);
			}
		}
		
//			工具栏	
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
//          电厂tree
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("电厂:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
//			刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('RIQ').value+'的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
	 	visit.getDiancxxb_id();
		if(insertRS.next()){
			if(insertRS.getString("zhi").equals("是")){
				insertRS.beforefirst();
//	        添加按钮		
				egu.addToolbarButton(GridButton.ButtonType_Insert, "InsertButton");				
			}		
		}

//			生成按钮
		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		if(insertRS.next()){
			if(insertRS.getString("zhi").equals("是")){
				insertRS.beforefirst();
//	        删除按钮	
				egu.addToolbarButton(GridButton.ButtonType_Delete, "DelButton");				
			}else{
//				删除按钮
				GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);			
			}		
		}else{
//			删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);			
		}

//			保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save_condition,"gridDiv",egu.getGridColumns(),"SaveButton","if(validateHy(gridDiv_ds)){return;};\n");
		egu.addTbarBtn(gbs);
		
//			grid 计算方法
		egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);");
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//			按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
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
//电厂tree
	boolean treechange = false;

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
		return getTree().getWindowTreeScript();
	}
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			visit.isFencb();
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			
		}
		getSelectData();
	}
}

