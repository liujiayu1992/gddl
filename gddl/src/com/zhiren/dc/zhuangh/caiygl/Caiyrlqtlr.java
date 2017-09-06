package com.zhiren.dc.zhuangh.caiygl;

/*
 * 作者：夏峥
 * 时间：2014-04-1
 * 描述：调整庄河入厂入炉日期判断，调整后时间节点以早9点至次日早9点为一个统计日期。
 * 		即2日入炉煤量为1日早9点至2日早9点的入炉煤量
 */

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caiyrlqtlr extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public static int InsYangpdhb(JDBCcon con, long diancxxb_id, long zhilb_id, String caiyb_id, 
			String zhillsb_id, String leibid, String bmid, String zhuanmsz, String leib){
		String sql = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id) "+
		"values(getnewid(" + diancxxb_id + ")," + caiyb_id + "," + zhillsb_id + 
		",'" + zhuanmsz + "','" + leib + "'," + bmid + "," + leibid + ")";
		return con.getInsert(sql);
	}
	
	private void Save() {
		 Visit visit = (Visit) this.getPage().getVisit();
		 Save1(getChange(), visit);
	}
	private void Save1(String change,Visit visit) {
		String tableName = "rulmzlzmxb";
		
		JDBCcon con = new JDBCcon();

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(change);
		while (delrsl.next()) {
			if(this.getWeizSelectValue().getValue().equals("其它样")){
				tableName = "zhillsb";
			}
			if(this.getWeizSelectValue().getValue().equals("入炉")){
				tableName = "rulmzlzmxb";
			}
			StringBuffer sql = new StringBuffer("begin \n");
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
				sql.append("delete from yangpdhb where zhilblsb_id=").append(delrsl.getString(0)).append(";\n");
				String asql =" select id, caiyb_id from yangpdhb where zhilblsb_id="+delrsl.getString(0);
				ResultSetList arsl = con.getResultSetList(asql);
				while(arsl.next()){
					if(delrsl.getString("LEIX").equals("入炉")){
						//RULCYYGLB
						sql.append(" delete from RULCYYGLB where rulmzlzb_id="+arsl.getString("zhilb_id")).append(";\n");
					}
					
					//增加删除采样单
					sql.append(" delete from caiyb where id="+arsl.getString("caiyb_id")).append(";\n");
				}
				sql.append("delete from zhuanmb where zhillsb_id=").append(delrsl.getString(0)).append(";\n");
			String d=" select id from yangpdhb where zhilblsb_id="+delrsl.getString(0);
			ResultSetList bs = con.getResultSetList(d);
			while(bs.next()){
				
				sql.append("delete from zhiyryglb where yangpdhb_id=").append(bs.getString("id")).append(";\n");
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
		}
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);
		long Diancxxb_id=0;
		if (visit.isFencb()) {
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			Diancxxb_id = visit.getDiancxxb_id();
		}
		while (mdrsl.next()) {
			String huayy=mdrsl.getString("CAIYY");
			
			String rulrq=mdrsl.getString("RULRQ");
			String meil=mdrsl.getString("MEIL");
//			String caiysj=mdrsl.getString("RULRQ");//mdrsl.getString("CAIYSJ");
//			String caiyml=mdrsl.getString("CAIYML");
			String bianm=mdrsl.getString("BIANM");
//			String meiz=mdrsl.getString("MEIZMC");
//			String meic=mdrsl.getString("MEICMC");
//			String meid=mdrsl.getString("MEIDMC");
//			int flag;
//			String sqlbm = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
//			"where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
//			" and c.leibb_id = l.id\n" +
//			" and f.mingc = '默认' and f.diancxxb_id =" + Diancxxb_id;

//			ResultSetList r = con.getResultSetList(sqlbm);
//			String leib ="";
//			String zhuanmsz="";
//			String leibid="";
//			boolean shifzm;
//			String bumid="";
//			String leix="";
//			while(r.next()){
				
//			新建质量临时ID
//			String zhillsb_id = MainGlobal.getNewID(Diancxxb_id);
//			转码类别名称
//				leib= r.getString("leib");
//			转码编码
//				zhuanmsz = r.getString("zhuanmsz");
//			化验类别ID
//				leibid = r.getString("lbid");
//			部门ID
//				bumid = r.getString("bmid");
//			是否转码
//				shifzm= r.getInt("shifzm") == 1;
//				leix=r.getString("leix");
//			}

			if ("0".equals(mdrsl.getString("ID"))) {
//				long zhilb_id=0;
//				zhilb_id=getYundxxValue().getId();
//				String sql1 = "select * from fahb where zhilb_id = " + zhilb_id;
//				ResultSetList rsl = con.getResultSetList(sql1);
//				long meikxxb_id=0;
//				long jihkjb_id=0;
//				long yunsfsb_id=0;
//				long faz_id=0;
//				while(rsl.next()){
//					meikxxb_id=rsl.getLong("meikxxb_id");
//					jihkjb_id=rsl.getLong("jihkjb_id");
//					yunsfsb_id=rsl.getLong("yunsfsb_id");
//					faz_id=rsl.getLong("faz_id");
//				}
				String zzb= MainGlobal.getNewID(Diancxxb_id);
				String mxid= MainGlobal.getNewID(Diancxxb_id);
				StringBuffer sql=new StringBuffer("begin \n");

					String rul="select id from rulbzb where mingc ='"+this.getFahrqValue().getValue()+"'";
					ResultSetList ruls = con.getResultSetList(rul);
//					String ss=" select id from renyxxb where quanc='"+mdrsl.getString("CAIYY")+"'";
//					ResultSetList r1 = con.getResultSetList(ss);
//					String mingc="0";
//					while(r1.next()){
//						mingc=r1.getString("id");
//						
//					}

					StringBuffer sql2= new StringBuffer(" begin\n");

						// 入炉添加
						//rzb.meidb_id,rzb.meicb_id,rzb.meizb_id
						String spl=
						 "insert into rulmzlzmxb (id,rulmzlb_id,huayy,rulrq,fenxrq,LURSJ,bianm,meil,DIANCXXB_ID,rulbzb_id,shenhzt,meidmc,meicmc,meizmc) "+
						"values(" + mxid + "," + zzb + ",'"+huayy+"',to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+
						",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss'),'"+bianm+"',"+meil+","+visit.getDiancxxb_id()+","+ruls.getString("id")+",0,'"
						+mdrsl.getString("meidb_id")+"','"
						+mdrsl.getString("meicb_id")+"','"
						+mdrsl.getString("meizb_id")+"')";
						sql2.append(spl).append(" ;\n");
						
//					}(getExtGrid().getColumn("meidb_id").combo).getBeanId(mdrsl.getString("meidb_id"))
					sql2.append("end;");
					

//					String ssql="select id  from rulmzlzmxb where id="+mxid+" and rulmzlb_id="+zzb;
					
//					ResultSetList rsls = con.getResultSetList(ssql);


				 sql.append("end;");
				 if(sql.length()>13){
						
						con.getInsert(sql.toString());
					}
			} else {
//				String mxid=mdrsl.getString("zhilb_id");
				StringBuffer sql2 = new StringBuffer("begin \n");

//				String ssql="select id  from rulmzlzmxb where id="+mxid;
				
//				ResultSetList rsls = con.getResultSetList(ssql);
//				while(rsls.next()){
//					String ssql1="select id from zhuanmlb where mingc='采样编码'";
//					String ssql2="select id from zhuanmlb where mingc='制样编码'";
//					String ssql3="select id from zhuanmlb where mingc='化验编码'";
//					ResultSetList rsls1 = con.getResultSetList(ssql1);
//					ResultSetList rsls2 = con.getResultSetList(ssql2);
//					ResultSetList rsls3 = con.getResultSetList(ssql3);
//					String zhillsb_id=	getChangeid();//入炉煤质量子表
//					String caiy_id="";
//					String zhillsb_id=	rsls.getString("id");
//					caiy_id= MainGlobal.getNewID(Diancxxb_id);

					tableName = "rulmzlzmxb";
//					String s="select id from renyxxb where quanc= '"+mdrsl.getString("CAIYY")+"'";
//					String id="";
//					ResultSetList rsq = con.getResultSetList(s);
//					while(rsq.next()){
//						id=rsq.getString("id");
//					}
//					sql2.append(" insert into RULCYYGLB (RULMZLZB_ID,renyxxb_id) values ("+zhillsb_id+",'"+mdrsl.getString("CAIYY")+"')").append(";\n");
					
					sql2.append("update ").append(tableName).append(" set rulrq=to_date('").append(mdrsl.getString("RULRQ")).append("','YYYY-MM-DD hh24:mi:ss')");
//					sql2.append(",huayy='").append(mdrsl.getString("CAIYY")).append("'");
					//rzb.meicb_id,rzb.meidb_id,rzb.meizb_id
//					+(getExtGrid().getColumn("meidb_id").combo).getBeanId(mdrsl.getString("meidb_id"))+","
//					+(getExtGrid().getColumn("meicb_id").combo).getBeanId(mdrsl.getString("meicb_id"))+","
//					+(getExtGrid().getColumn("meizb_id").combo).getBeanId(mdrsl.getString("meizb_id"))+")";
					sql2.append(",meil=").append(mdrsl.getString("MEIL")).append(",bianm='")
					.append(mdrsl.getString("BIANM")+"'").append(",meicmc='"
							+mdrsl.getString("meicb_id")+"',meidmc='")
							.append(mdrsl.getString("meidb_id")+"',meizmc='")
							.append(mdrsl.getString("meizb_id")).append("'")
					.append("\n");
					sql2.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
//					sql2.append("update zhuanmb set bianm='").append(mdrsl.getString("BIANM")).append("' where zhillsb_id=").append(mdrsl.getString("ID")).append(";\n");
					sql2.append(" update RULCYYGLB set RENYXXB_ID='"+mdrsl.getString("CAIYY")+"' where RULMZLZB_ID="+mdrsl.getString("zhilb_id")).append(";\n");
					sql2.append("end;");
					con.getUpdate(sql2.toString());
				} 
//		   }
//		}
			}
		
		con.Close();
	}
	
	public static boolean panDflag(String bm,JDBCcon con){
		
		String try_bian=" select * from zhuanmb where bianm ='"+bm+"'";
		ResultSetList rsl_bian = con.getResultSetList(try_bian);
		
		boolean fg=false;
		if(rsl_bian.getRows()>0){
			
			fg=true;
		}
		
		return fg;
	}
	
	public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
			long meikxxb_id, long jihkjb_id, long yunsfsb_id, long chezxxb_id, 
			String bianm,String caiyy,String caiysj,String caiyml) {
		String sql ="";
		ResultSetList rsl = null;
		String caiyb_id = null;
		int flag ;
//		判断此质量ID是否存在 如果存在则返回新建编号成功
		sql = "select * from zhuanmb where zhillsb_id in (select id from zhillsb"+
		" where zhilb_id in(" + zhilb_id+") and caiysj is null)";
		rsl = con.getResultSetList(sql);
		if(rsl.getRows()>0){
			rsl.close();
			return 0;
		}
//		获得采样表ID
		sql = "select * from caiyb where zhilb_id = " + zhilb_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			caiyb_id = rsl.getString("id");
		}
		rsl.close();
//		查看供应商对应的采样定义
		sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhdyb c, fenkcyfsb f, bumb b, leibb l " +
				"where c.caizhfsb_id = f.caizhfsb_id\n" +
				" and c.bum_id = b.id\n" +
				" and c.leibb_id = l.id\n" +
				" and f.diancxxb_id = " + diancxxb_id + "\n" +
				" and f.meikxxb_id = " + meikxxb_id + "\n" +
				" and f.yunsfsb_id = " + yunsfsb_id + "\n" +
				" and f.chezxxb_id = " + chezxxb_id + "\n" +
				" and f.jihkjb_id = " + jihkjb_id + "\n" ;
		rsl = con.getResultSetList(sql);
		
		if(rsl.getRows() == 0){
//			如果系统没有设置 则取得系统默认设置
			sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
			"where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
			" and c.leibb_id = l.id\n" +
			" and f.mingc = '默认' and f.diancxxb_id =" + diancxxb_id;
			
			rsl.close();
			rsl = con.getResultSetList(sql);
			if(rsl.getRows() == 0){
//				如果默认设置未设置 则返回错误
				return -1;
			}
		}
		while(rsl.next()){
//			新建质量临时ID
			String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//			转码类别名称
			String leib = rsl.getString("leib");
//			转码编码
			String zhuanmsz = rsl.getString("zhuanmsz");
//			化验类别ID
			String leibid = rsl.getString("lbid");
//			部门ID
			String bumid = rsl.getString("bmid");
//			是否转码
			boolean shifzm = rsl.getInt("shifzm") == 1;
			String leix=rsl.getString("leix");
//			写入样品单号表
			flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id, 
							zhillsb_id, leibid, bumid, zhuanmsz, leib);
			if(flag == -1){
//				如果出错则返回错误
				return -1;
			}
//			写入质量临时表
			InsZhillsb(con, diancxxb_id, zhilb_id, 
					zhillsb_id, leibid, bumid, leib,caiyy,caiysj,caiyml,leix);
			if(flag == -1){
//				如果出错则返回错误
				return -1;
			}
			if(bianm != null){
//				如果 手录编码==采样表中编码 则不使用转码设置
//				如果非自动生成编码			
				zhuanmsz = "";
				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, false, zhuanmsz);
				if(flag == -1){
//					如果出错则返回错误
					return -1;
				}
			}else{
//				判断如果是自动生成编码
				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, shifzm, zhuanmsz);
				if(flag == -1){
//					如果出错则返回错误
					return -1;
				}
			}
		}
		rsl.close();
		return 0;
	}
	
	public static int InsZhillsb(JDBCcon con, long diancxxb_id, long zhilb_id, 
			String zhillsb_id, String leibid, String bmid, String leib,String caiyy,String caiysj,String caiyml,String leix){
		String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,caiyy,caiysj,caiyml,leix) "+
		"values(" + zhillsb_id + "," + zhilb_id + ",0,'" + leib + "'," + leibid + "," + bmid + ",'"+caiyy+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),"+caiyml+","+leix+")";
		return con.getInsert(sql);
	}
	
	public static int InsZhuanmb(JDBCcon con,long zhuanmlb_id, long diancxxb_id, 
			String zhillsb_id, String bianm){
		String sql = "insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
			"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
			"'," + zhuanmlb_id + ")";
		return con.getInsert(sql);
	}
	
	public static int Zhuanmcz(JDBCcon con, long diancxxb_id, String zhillsb_id,
			String bianm, boolean shifzm, String zhuanmqz){
		
		String sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id;
		ResultSetList rsl =  con.getResultSetList(sql);
		long zhuanm = MainGlobal.getSequenceNextVal(con,SysConstant.BiascCodeSequenceName);
		int flag;
		while(rsl.next()){
			if(shifzm){
				zhuanm = zhuanm*2 + 729;
				bianm = zhuanmqz + zhuanm;
			}else{
				bianm = zhuanmqz + bianm;
			}
			flag = InsZhuanmb(con,rsl.getLong("id"),diancxxb_id,zhillsb_id,bianm);
			if(flag == -1){
				return -1;
			}
		}
		rsl.close();
		return 0;
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
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

//	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
//		_InsertChick = true;
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_RefurbishChick){
		   _RefurbishChick = false;
			getSelectData();
		}
	}
	 
	public void getSelectData() {
		JDBCcon con = new JDBCcon();

		String shijd=MainGlobal.getXitxx_item("数量", "入厂入炉节点时间", "0", "0");
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct rzb.zhuanmbzllsb_id zhilb_id,to_char(min(rzb.rulrq),'YYYY-MM-DD hh24:mi:ss') " +
				"as rulrq, b.bianm, glb.RENYXXB_ID as caiyy, rzb.meicmc meicb_id,rzb.meidmc meidb_id,rzb.meizmc meizb_id,'入炉' as leix \n"
				+ "  from rulmzlzmxb rzb ,zhuanmb b,zhuanmlb l, RULCYYGLB glb  \n"
				+ " where  (rzb.rulrq between  to_date('"+this.getRiq1()+" "+shijd+":00:00"+"', 'YYYY-MM-DD hh24:mi:ss')-1\n"
				+ " and to_date('"+this.getRiq1()+" "+shijd+":00:00"+"', 'YYYY-MM-DD hh24:mi:ss'))\n" +
						" and b.zhillsb_id=rzb.zhuanmbzllsb_id and b.zhuanmlb_id=l.id and l.jib=1  and rzb.zhuanmbzllsb_id= glb.RULMZLZB_ID\n" +
						" group by (rzb.zhuanmbzllsb_id, b.bianm,glb.RENYXXB_ID, rzb.meicmc,rzb.meidmc,rzb.meizmc ) ");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) { 
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setTableName("fahb");
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置每页显示行数
		egu.addPaging(25);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").setWidth(60);
		egu.getColumn("zhilb_id").editor = null;
		if(this.getWeizSelectValue().getValue().equals("入炉")){
			
			egu.getColumn("rulrq").setHeader("采样时间");//Center
			egu.getColumn("rulrq").setWidth(120);
			
			
			egu.getColumn("bianm").setCenterHeader("采样编码");
			egu.getColumn("bianm").setWidth(90);
			egu.getColumn("bianm").setEditor(null);
			
			egu.getColumn("caiyy").setCenterHeader("采样人员");
			egu.getColumn("caiyy").setWidth(150);
			egu.getColumn("caiyy").setEditor(null);

			egu.getColumn("meidb_id").setCenterHeader("煤堆名称");
			egu.getColumn("meidb_id").setWidth(70);
			egu.getColumn("meidb_id").setEditor(null);
			egu.getColumn("meicb_id").setCenterHeader("煤场名称");
			egu.getColumn("meicb_id").setWidth(70);
			egu.getColumn("meicb_id").setEditor(null);
			egu.getColumn("meizb_id").setCenterHeader("煤种名称");
			egu.getColumn("meizb_id").setWidth(70);
			egu.getColumn("meizb_id").setEditor(null);
			
			DatetimeField datetime = new DatetimeField();
			egu.getColumn("rulrq").setEditor(datetime);
			egu.getColumn("rulrq").setEditor(null);
			egu.getColumn("rulrq").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		}
		if(this.getWeizSelectValue().getValue().equals("其它样")){
			
			egu.getColumn("zhilb_id").setHidden(true);
			egu.getColumn("zhilb_id").setWidth(60);
			egu.getColumn("zhilb_id").editor = null;
			egu.getColumn("zhilb_id").setDefaultValue(""+getYundxxValue().getId());
			egu.getColumn("caiysj").setHeader("采样时间");//Center
			egu.getColumn("caiysj").setWidth(120);
			
			egu.getColumn("bianm").setCenterHeader("采样编码");
			egu.getColumn("bianm").setWidth(90);
			egu.getColumn("caiyy").setCenterHeader("采样人员");
			egu.getColumn("caiyy").setWidth(150);
			egu.getColumn("caiyy").setEditor(null);
			egu.getColumn("caiyml").setCenterHeader("采样煤量");
			egu.getColumn("caiyml").setWidth(70);
			egu.getColumn("caiyml").setHidden(true);
			
			DatetimeField datetime = new DatetimeField();
			egu.getColumn("caiysj").setEditor(datetime);
			egu.getColumn("caiysj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		}
		egu.getColumn("leix").setCenterHeader("采样类型");
		egu.getColumn("leix").setWidth(90);
		egu.getColumn("leix").setEditor(null);
		egu.getColumn("leix").setHidden(true);
		
		if(this.getWeizSelectValue().getValue().equals("其它样")){
			
			egu.addTbarText("采样时间");
		}
		if(this.getWeizSelectValue().getValue().equals("入炉")){
			
			egu.addTbarText("入炉采样时间");
		}
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
//		egu.addTbarText("至：");
//		DateField df1 = new DateField();
//		df1.setReadOnly(true);
//		df1.setValue(this.getRiq2());
//		df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
//		df1.setId("riq2");
//		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");// 设置分隔符

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");

		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '采样人员',\n" + 
			//"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
			"    \t\t\t}else{\n" + 
			"    \t\t\t\te.cancel = true;\n" + 
			"    \t\t\t}\n" + 
			"    \t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\taddNode(node);\n" + 
			"\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\tsubNode(node);\n" + 
			"\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\tnode.expand();\n" + 
			"\t\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\t\t\taddNode(child);\n" + 
			"\t\t\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\t\t\tsubNode(child);\n" + 
			"\t\t\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t\t}\n" + 
			"\t\t\t\t\t});\n" + 
			"\t\t\t\t}\n" + 
			"    \t},\n" + 
			"\t   \ttbar:[{text:'确定',handler:function(){\n" + 
			"        var cs = navtree.getChecked();\n" + 
			"        rec = gridDiv_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('CAIYY',tmp2);\n" + 
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			" win = new Ext.Window({\n" + 
			" title: '采样人员',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
				+ "row = irow; \n"
				+ "if('CAIYY' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"if(!win){"+treepanel+Strtmpfunction+"}"
				+"win.show(this);}});\n");

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		con.Close();
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		导航栏树的查询SQL
		String sql=
			"select 0 id,'根' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select r.id, r.quanc  as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from diancxxb d,renyxxb r\n" + 
			" where\n" + 
			"r.diancxxb_id=d.id\n" + 
			"and r.bum='采样' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
//		System.out.println(sql);
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

//	按照什么时间查条件选择框
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			JDBCcon con=new JDBCcon();
			ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()+" and zhuangt=1 and mingc='海运煤日期查询默认值'");
			String zhi="";
			while(rsl.next()){
				zhi=rsl.getString("zhi");
			}
			if(zhi.equals("离泊日期")){
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(2));
			}else if(zhi.equals("到泊日期")){
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(1));
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

			((Visit) getPage().getVisit()).setboolean3(true);
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
//		Visit v=(Visit)this.getPage().getVisit();	
		list.add(new IDropDownBean(1, "入炉"));
//		list.add(new IDropDownBean(2, "其它样"));
//		list.add(new IDropDownBean(3, "离泊日期"));
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

//	 绑定日期
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {
		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	
	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {
		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	
	// 时间段
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
		String sql="";
		sql="select id,mingc from rulbzb where diancxxb_id="+visit.getDiancxxb_id();
		setFahrqModel(new IDropDownModel(sql));
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

	public boolean _Yundxxchange = false;

	public IDropDownBean getYundxxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getYundxxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYundxxValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setYundxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYundxxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYundxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getYundxxModels() {
		String sql="";
		switch ((int) this.getWeizSelectValue().getId()) {
		case 1:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.kaobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.kaobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// 靠泊日期
		case 2:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.daobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// 到泊日期
		case 3:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daohrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.daohrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// 离泊日期
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			visit.setString5(null);
			visit.setString6(null);
			setFahrqValue(null);
			setFahrqModel(null);
			setYundxxModel(null);	
			getYundxxModels();		
			setTbmsg(null);
			initNavigation();
			visit.setString1(null);
		}
		if(visit.getboolean1()){
			visit.setboolean1(false);
//			visit.setString9("");
			visit.setList1(null);
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			visit.setString1(null);
		}
		if(visit.getboolean2()){
			visit.setboolean2(false);
		}
		if(visit.getboolean3()){
		   visit.setboolean3(false);
		   setYundxxValue(null);		//4
		   setYundxxModel(null);
		   visit.setString1(null);
		}
		initNavigation();
		getSelectData();
	}
}
