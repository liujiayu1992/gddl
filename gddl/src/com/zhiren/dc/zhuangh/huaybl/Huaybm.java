package com.zhiren.dc.zhuangh.huaybl;


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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2012-09-01
 * 描述：增加‘灰熔点检查’列，已确认编码的数据置灰并不可被编辑
 * 		zhillsb中新增HUIRDSF字段并以文字形式保存，默认值为‘否’
 */
/*
 * 作者：夏峥
 * 时间：2012-10-17
 * 描述：增加‘化验类别’列
 */
/*
 * 作者：夏峥
 * 时间：2014-03-10
 * 描述：调整庄河入厂入炉日期判断，调整后时间节点以早9点至次日早9点为一个统计日期。
 * 		调整程序存在的BUG，清除无用注释。
 */
/*
 * 作者：夏峥
 * 时间：2014-04-1
 * 描述：调整庄河入厂入炉日期判断，调整后时间节点以早9点至次日早9点为一个统计日期。
 * 		即2日入炉煤量为1日早9点至2日早9点的入炉煤量
 */
public class Huaybm extends BasePage implements PageValidateListener {
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
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}
	public void setChangeid(String changeid) {
		Changeid = changeid;
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
		JDBCcon con = new JDBCcon();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);

		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
				
			} else {
				String ss=" select id from renyxxb where quanc='"+mdrsl.getString("MINGC")+"'";
				ResultSetList r = con.getResultSetList(ss);
//				String mingc="";
				String zhuanmlb_id="";
				String sa=" select id from zhuanmlb where jib=3 ";
//				while(r.next()){
//					mingc=r.getString("id");
//					
//				}
				r=con.getResultSetList(sa);
				while(r.next()){
					zhuanmlb_id=r.getString("id");
				}
				StringBuffer sql2 = new StringBuffer("begin \n");
				
				if(mdrsl.getString("quf").equals("1")){
					sql2.append(" update zhillsb set querry='"+visit.getRenymc()+"', quersj=to_date('")
					.append(mdrsl.getString("quersj")).append("','YYYY-MM-DD hh24:mi:ss'), HUIRDSF='"+mdrsl.getString("HUIRDSF")+"' where id="+getChangeid()).append(";\n") ;
				}else if(mdrsl.getString("quf").equals("2")){
					sql2.append("update rulmzlzmxb set querry='"+visit.getRenymc()+"', quersj=to_date('")
					.append(mdrsl.getString("quersj")).append("','YYYY-MM-DD hh24:mi:ss') where zhuanmbzllsb_id="+mdrsl.getString("zhilblsb_id")).append(";\n");
				}else{
					sql2.append("update QITYMXB set querry='"+visit.getRenymc()+"', quersj=to_date('")
					.append(mdrsl.getString("quersj")).append("','YYYY-MM-DD hh24:mi:ss') where id="+getChangeid()).append(";\n");
				}
				if(visit.getboolean4()){//true 那么手动填写
					
					sql2.append(" insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id) values (getnewid("+visit.getDiancxxb_id()+")," +
							""+mdrsl.getString("ZHILBLSB_ID")+",'"+mdrsl.getString("BBIANM")+"',"+zhuanmlb_id+")").append(";\n");
				}else{
					//自动其实页面没有变化，也不会发生保存事件
				}
				sql2.append("end;");
				if(sql2.length()>13){
					
					con.getUpdate(sql2.toString());
				}
		   }
		}
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
//			写入样品单号表
			flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id, 
							zhillsb_id, leibid, bumid, zhuanmsz, leib);
			if(flag == -1){
//				如果出错则返回错误
				return -1;
			}
//			写入质量临时表
			InsZhillsb(con, diancxxb_id, zhilb_id, 
					zhillsb_id, leibid, bumid, leib,caiyy,caiysj,caiyml);
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
			String zhillsb_id, String leibid, String bmid, String leib,String caiyy,String caiysj,String caiyml){
		String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,caiyy,caiysj,caiyml) "+
		"values(" + zhillsb_id + "," + zhilb_id + ",0,'" + leib + "'," + leibid + "," + bmid + ",'"+caiyy+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),"+caiyml+")";
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		String shij="select beiz from rulbzb where id= "+this.getFahrqValue();
		String beiz="";
//		String beiz1="";
//		String beiz2="";
		ResultSetList rslq1 = con.getResultSetList(shij);
		while(rslq1.next()){
			beiz=rslq1.getString("beiz");
		}
		if("".equals(beiz)){
			beiz="00-23"; 
//			beiz1=beiz.substring(0,2)+":00:00";
//			beiz2=beiz.substring(3,5)+":59:59";
		}else{ 
//			if("24".equals(beiz.substring(3,5))){
////				beiz1=beiz.substring(0,2)+":00:00";
////				beiz2="23:59:59";
//			}else{
////				beiz1=beiz.substring(0,2)+":00:00";
////				beiz2=beiz.substring(3,5)+":00:00";
//			}
			
		}
		String shijd=MainGlobal.getXitxx_item("数量", "入厂入炉节点时间", "0", "0");
		sb1.append("select a.id,a.zhilblsb_id,DECODE(1,1,'入厂')leib,a.bianm,bb.bbianm,bb.querry,decode(bb.quersj,'',to_char(sysdate,'YYYY-MM-DD hh24:mi:ss'),to_char(bb.quersj,'YYYY-MM-DD hh24:mi:ss')) quersj,'1' as quf,a.huirdsf  from (\n"
						+ "select distinct yb.id, yb.zhilblsb_id,zmb.bianm, r.quanc as mingc, to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm,zh.jib,zb.huirdsf\n"
						+ "  from zhillsb zb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh, zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = zb.id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
						+ "   and zh.jib=2\n"
//						+ "  -- and zb.shenhzt=0\n"
						+ "   and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and zb.caiysj between to_date('"+this.getRiq1()+" "+"00:00:00"+"','YYYY-MM-DD hh24:mi:ss') " +
						"and to_date('"+this.getRiq1()+" "+"23:59:59"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "    ) a,\n"
						+ "\n"
						+ "\n"
						+ "   ( select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm,zb.querry,zb.quersj, r.quanc as mingc, to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm,zh.jib,zb.huirdsf\n"
						+ "  from zhillsb zb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh, zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = zb.id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
						+ "   and zh.jib=3\n"
//						+ "  -- and zb.shenhzt=0\n"
						+ "   and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and zb.caiysj between to_date('"+this.getRiq1()+" "+"00:00:00"+"','YYYY-MM-DD hh24:mi:ss') " +
						"and to_date('"+this.getRiq1()+" "+"23:59:59"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "    ) bb\n"
						+ "    where a.id=bb.id(+)\n"
						+ "\n"
						+ "\n"
						+ "union\n"
						+ "\n"
						+ "select  c.id,c.zhilblsb_id,DECODE(1,1,'入炉')leib, c.bianm,d.bbianm,max(d.querry) querry,decode(min(d.quersj),'',to_char(sysdate,'YYYY-MM-DD hh24:mi:ss'),to_char(min(d.quersj),'YYYY-MM-DD hh24:mi:ss')) quersj,'2' as quf,'否' huirdsf  from (\n"
						+ "select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm, r.quanc as mingc , to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm\n"
						+ "  from rulmzlzmxb rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = rb.zhuanmbzllsb_id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "  -- and rb.shenhzt=0\n"
						+ "   and  zh.jib=2\n"
						+ "     and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and rb.rulrq between to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')-1 " +
						"and to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "   ) c,\n"
						+ "   (\n"
						+ "   select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm,rb.querry,rb.quersj, r.quanc as mingc, to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm\n"
						+ "  from rulmzlzmxb rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = rb.zhuanmbzllsb_id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "   --and rb.shenhzt=0\n"
						+ "   and  zh.jib=3\n"
						+ "     and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and rb.rulrq between to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')-1\n " +
						"and to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "   ) d\n" + "   where c.id=d.id group by (c.id,c.zhilblsb_id,c.bianm,d.bianm)\n" 
						+ "\n"

						+ "");
		
		// 如果还没有，显示上一个
		// 如果都没有，都不显示，

		ResultSetList rsl = con.getResultSetList(sb1.toString());
//		ResultSetList rsl1;
		
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
        String czh=
        	"select *\n" +
        	"  from xitxxb\n" + 
        	" where mingc = '判断是否自动三级编码'\n" + 
        	"   and diancxxb_id = 300\n" + 
        	"   and zhi = '否'\n" + 
        	"   and leib = '采制化'\n" + 
        	"   and zhuangt = 1\n" + 
        	"   and beiz = '使用'";
        ResultSetList rsl_czh = con.getResultSetList(czh);
        if(rsl_czh.getRows()>0){
        	visit.setboolean4(true);
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
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setWidth(60);
		egu.getColumn("id").editor = null;
		egu.getColumn("zhilblsb_id").setHidden(true);
		egu.getColumn("zhilblsb_id").setWidth(60);
		egu.getColumn("zhilblsb_id").editor = null;
		
		egu.getColumn("leib").setCenterHeader("化验类别");
		egu.getColumn("leib").setWidth(60);
		egu.getColumn("leib").setEditor(null);
		
		egu.getColumn("bbianm").setCenterHeader("化验编码");
		egu.getColumn("bbianm").setWidth(120);
		if(visit.getboolean4()){
			//如果存在true，那么是手动
		}else{
			//自动
			egu.getColumn("bbianm").setEditor(null);
		}
//		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("querry").setCenterHeader("确认人员");
		egu.getColumn("querry").setWidth(150);
		egu.getColumn("querry").setEditor(null);
		egu.getColumn("quersj").setCenterHeader("确认时间");
		egu.getColumn("quersj").setWidth(150);
		egu.getColumn("quf").setCenterHeader("区分");
		egu.getColumn("quf").setWidth(150);
		egu.getColumn("quf").setEditor(null);
		egu.getColumn("quf").setHidden(true);
		
		
		egu.getColumn("bianm").setCenterHeader("制样编码");
		egu.getColumn("bianm").setWidth(120);
		egu.getColumn("bianm").setEditor(null);
		
		// 设置发货日期和到货日期的默认值
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("quersj").setEditor(datetime);
		egu.getColumn("quersj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		
		egu.getColumn("huirdsf").setHeader("灰熔点检查");//Center
		egu.getColumn("huirdsf").setWidth(90);
		egu.getColumn("huirdsf").setDefaultValue("否");
		ComboBox c1 = new ComboBox();
		egu.getColumn("huirdsf").setEditor(c1);
		c1.setEditable(true);
		String MeicbSql1="select  1 as id, decode(0,0,'是') as mingc from dual union select 2 as id,decode(0,0,'否') as mingc from dual ";
		egu.getColumn("huirdsf").setComboEditor(egu.gridId, new IDropDownModel(MeicbSql1));
		
		
		egu.addTbarText("制样时间");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");

		GridButton gb = new GridButton("编码确认", "function(){ "
				+
				" var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ZHILBLSB_ID');"//zhilblsb_id
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;  if(id==0||id=='0'){Ext.MessageBox.alert('提示信息','请先保存第一组添加的记录'); return ; }}"
				+ " if(rec == null){"
				+ "  Ext.MessageBox.alert('提示信息','请选择一条信息记录'); return ;"
				+ " }"+
				" var gridDivsave_history = '';\n" +
				" var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n" + //gridDiv_ds.getModifiedRecords();
				"for(i = 0; i< Mrcd.length; i++){\n" + 
				"if(typeof(gridDiv_save)=='function'){\n" + 
				" var revalue = gridDiv_save(Mrcd[i]);\n" + 
				"  if(revalue=='return'){return;}else if(revalue=='continue'){continue;}\n" + 
				"  }\n" + 
				"if(Mrcd.length>0 && Mrcd[i].get('QUERSJ') == ''){\n" + 
				"Ext.MessageBox.alert('提示信息','字段 <center>确认时间</center> 不能为空');return;\n" + 
				"}\n" + 
				"gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
				"+ '<ZHILBLSB_ID update=\"true\">' + Mrcd[i].get('ZHILBLSB_ID')+ '</ZHILBLSB_ID>'\n" + 
				"+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'\n" + 
				"+ '<BBIANM update=\"true\">' + Mrcd[i].get('BBIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BBIANM>'\n" + 
				"+ '<QUERRY update=\"true\">' + Mrcd[i].get('QUERRY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUERRY>'\n" + 
				"+ '<QUERSJ update=\"true\">' + Mrcd[i].get('QUERSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUERSJ>'\n" + 
				"+ '<HUIRDSF update=\"true\">' + Mrcd[i].get('HUIRDSF').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HUIRDSF>'\n" + 
				"+ '<QUF update=\"true\">' + Mrcd[i].get('QUF').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUF>'\n" + 
				" + '</result>' ;\n" + 
				"  }"+

				"    if(gridDiv_history=='' && gridDivsave_history=='' ){\n" + //&& rec == null
				"    Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n" + 
				"\t\t\t}else{\n" + 
				"\t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
				"\t\t\t\tCobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" + 
				"\t\t\t\tdocument.getElementById('SaveButton').click();\n" + 
				"\t\t\t\tExt.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"\t\t\t}\n" + 
				"\t}"

				+ " ");
		egu.addTbarBtn(gb);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
//		设置不可编辑的内容
		String Scrpit="gridDiv_grid.on('beforeedit',function(e){\n"+
		"if(e.field=='HUIRDSF'&& e.record.get('QUERRY').length>0){e.cancel=true;}\n"+
		"if(e.field=='HUIRDSF'&& e.record.get('QUF')!='1'){e.cancel=true;}\n"+
		"});\n";
//		设置颜色
		String changeColor=" var girdcount=0;\n"+
			" gridDiv_ds.each(function(r){\n"+
			"     if(r.get('QUERRY').length>0){\n"+
			"         gridDiv_grid.getView().getCell(girdcount, 8).style.backgroundColor='#E3E3E3';\n"+
			"     }\n"+
			"     girdcount=girdcount+1;\n"+
			" });\n";
		egu.addOtherScript(Scrpit+changeColor);

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
			"and r.bum='制样' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
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
		list.add(new IDropDownBean(1, "靠泊日期"));
		list.add(new IDropDownBean(2, "到泊日期"));
		list.add(new IDropDownBean(3, "离泊日期"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
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
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
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

//	 时间段
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
//		String sql_yunsfs = "";
		String sql="";
//		if(HAIY_SH.equals(getLeix())){
			
		sql="select id,mingc from rulbzb where diancxxb_id="+visit.getDiancxxb_id();
 
		setFahrqModel(new IDropDownModel(sql));
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
			setYundxxModel(null);	
			getYundxxModels();	
			setFahrqValue(null);
			setFahrqModel(null);
			getFahrqModel();
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
		visit.setboolean4(false);
		initNavigation();
		getSelectData();
	}
}
