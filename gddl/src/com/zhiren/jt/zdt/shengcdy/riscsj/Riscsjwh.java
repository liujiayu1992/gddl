package com.zhiren.jt.zdt.shengcdy.riscsj;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-10-31
 * 描述：修改刷新时一厂多制选择总厂刷新分厂数据
 */
/*
 * 作者:tzf
 * 时间:2009-10-27
 * 修改内容:导入按钮方法中采用zbbh作为判断条件
 */
/*
 * 作者:tzf
 * 时间:2009-10-26
 * 修改内容:导入按钮中因为数据库字符编码的不一样，现调整jhgl_rlsj表中用ct_zbbh去做判断。
 */
/*
 * 作者:tzf
 * 时间:2009-10-20
 * 修改内容:增加导入按钮
 */
/*
 * 作者:zsj
 * 时间:2010-04-02
 * 修改内容:针对马头电厂，改变的接口表结构
 * 把发电量和供热量也要导入我们的系统,目前发电量已经能导入,供热量还没有导入进来.
 */


/*作者:wzb
 *日期:2010-6-2 
 *修改内容:导入的时候取不到供热量的值,发现是sql错误,修改行是272行
 * 把and ct_zbbh like'%dl%' 改成 and (ct_zbbh like '%dl%'  or ct_zbbh like '%dwgrl%')
 * 
 */

/*
 * 作者:夏峥
 * 日期:2012-3-12
 * 修改内容:注销保存时自动生成日报的操作。
 */
/*
 * 作者:夏峥
 * 日期:2012-11-29
 * 修改内容:新增耗煤量列并根据参数判断是否显示，默认为不显示
 * 	MainGlobal.getXitxx_item("日生产数据", "是否隐藏耗煤量信息", "0", "是")
 */
/*
 * 作者:夏峥
 * 日期:2013-03-25
 * 修改内容:将发电量，供电量和上网电量的单位变更为万千瓦时，并调整保存方法。
 * 			初始化电厂树
 */
/*
 * 作者：夏峥
 * 时间：2013-07-31
 * 描述：增加系统功能配置功能
 */
public class Riscsjwh extends BasePage implements PageValidateListener {
//	界面用户提示
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

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		StringBuffer sb=new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag=0;
		String id = "";
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		if(rssb==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahxg.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rssb.next()){
			id=rssb.getString("id");
			//删除时新增日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Riscsj,
					"riscsjb",id+"");
			String sql="delete from riscsjb where id="+id;
			flag=con.getDelete(sql);
			if(flag==-1){
				con.rollBack();
				con.Close();
				
			}
			con.commit();
		}
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Riscsjwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			con.Close();
			return;
		}
		
		sb.append("begin ");
//		String riq = "";
		while(rsl.next()) {
			id=rsl.getString("id");
			if(id.equals("0")){
//				riq = rsl.getString("riq");
				sb.append("insert into riscsjb (id,diancxxb_id,riq,jizb_id,HAOML,fadl,gongdl,shangwdl,gongrl,fadfhl)");
				sb.append("values (getnewid(").append(getTreeid()).append("),").append(getTreeid()).append(",").append(DateUtil.FormatOracleDate(rsl.getString("riq"))).append(",'");
				sb.append((getExtGrid().getColumn("jizb_id").combo).getBeanId(rsl.getString("jizb_id"))).append("',").append(rsl.getDouble("HAOML")).append(",").append(rsl.getDouble("fadl")).append("*10000,");
				sb.append(rsl.getDouble("gongdl")).append("*10000,").append(rsl.getDouble("shangwdl")).append("*10000,").append(rsl.getDouble("gongrl")).append(",").append(rsl.getDouble("fadfhl")).append(");") ;
//				con.getInsert(sb.toString());
			}else{
				//更改时新增日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Riscsj,
						"riscsjb",id+"");
				sb.append("update riscsjb set jizb_id=").append((getExtGrid().getColumn("jizb_id").combo).getBeanId(rsl.getString("jizb_id"))).append(",fadl=");
				sb.append(rsl.getDouble("fadl")).append("*10000, HAOML= "+rsl.getDouble("HAOML")+", gongrl=").append(rsl.getDouble("gongrl")).append(",gongdl=").append(rsl.getDouble("gongdl")).append("*10000,shangwdl=").append(rsl.getDouble("shangwdl")).append("*10000,fadfhl=");
				sb.append(rsl.getDouble("fadfhl")).append(" where id=").append(id).append(";");
//				con.getUpdate(sb.toString());
			}
		}
		sb.append("end;");
		flag = con.getInsert(sb.toString());
		if(flag==-1){
			con.rollBack();
			con.Close();
			
		}
		if (flag!=-1){//保存成功
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
//		AutoCreateShouhcrb.Create(con,visit.getDiancxxb_id(),DateUtil.getDate(riq));
		con.commit();
		con.Close();
		//setMsg("保存成功");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	private boolean shousButton = false;
	public void shousButton(IRequestCycle cycle) {
		shousButton = true;
	}
	
	private boolean _DaorChick;
	
	public void DaorButton(IRequestCycle cycle){
		_DaorChick=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(shousButton){
			shousButton = false;
			shous();
			getSelectData();
		}
		if(_DaorChick){
			_DaorChick=false;
			Daor();
			getSelectData();
		}
	}
	
	private String getFenc(JDBCcon con){
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		String s="";
		while(rsl.next()){
			s+=rsl.getString("id")+",";
		}
		if(s.equals("")){
			s=this.getTreeid()+",";
		}
		
		return s.substring(0,s.lastIndexOf(","));
	}
	
	private void Daor(){
	    
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		ResultSetList rsl2=null;
		String dcidStr=getFenc(con);
		if(visit.getDiancxxb_id()==232){	//马头电厂
			String insertSql=" delete from riscsjb where riq="+DateUtil.FormatOracleDate(this.getRiqi())+" and diancxxb_id in ("+dcidStr+") ;";
			String sql=" select * from xitxxb where mingc='日生产数据导入数据表' and leib='调运'  and zhuangt=1";
			rsl=con.getResultSetList(sql);
			
			String tableName="jhgl_rlsj";
			if(rsl.next()){
				tableName=rsl.getString("zhi");
			}
			
			if(tableName!=null){//针对 马头
				
				sql=" select j.id, nvl('#','')||REGEXP_SUBSTR(j.jizbh,'[0-9]+') mingc,j.diancxxb_id from jizb j where j.diancxxb_id in ("+dcidStr+")  order by xuh asc ";
				rsl=con.getResultSetList(sql);
				
				while(rsl.next()){
					
					String mingc=rsl.getString("mingc");
					String jizid=rsl.getString("id");
					String dc_id=rsl.getString("diancxxb_id");
//					字段定义：dt_rq（日期）、ct_zbbh（指标）、am_brsz（指标值）
					String sql2=" select dt_rq,ct_zbbh,ct_zbmc,am_brsz,am_bysz from " +tableName+" "+
					"  where dt_rq="+DateUtil.FormatOracleDate(this.getRiqi())+" and nvl('#','')||REGEXP_SUBSTR(ct_zbbh,'[0-9]+')='"+mingc+"' " +
					//" and ct_zbbh like'%dl%' "; 作者:王总兵,更改原因,因为不能够取到供热量的值
					"and (ct_zbbh like '%dl%'  or ct_zbbh like '%dwgrl%')";
					
					rsl2=con.getResultSetList(sql2);
					
					String fadh="";//发电量
					String gongdh="";//供电量
					String fadcydh="";//发电厂用电量
					String gongrh="";
					boolean flag=false;
					while(rsl2.next()){
						
						String zbbm=rsl2.getString("ct_zbbh");
						if(zbbm.toLowerCase().indexOf("fdl")!=-1){
							
							fadh=String.valueOf(CustomMaths.Round_new(rsl2.getDouble("am_brsz")*10000, 0));
						}
						if(zbbm.toLowerCase().indexOf("gdl")!=-1){
							
							gongdh=String.valueOf(CustomMaths.Round_new(rsl2.getDouble("am_brsz")*10000, 0));
						}
						if(zbbm.toLowerCase().indexOf("fdcydl")!=-1){
							fadcydh=rsl2.getString("am_brsz");
						}
						if(zbbm.toLowerCase().indexOf("grl")!=-1 
								|| zbbm.toLowerCase().indexOf("dwgrl")!=-1){
							gongrh=rsl2.getString("am_brsz");
						}
						
						flag=true;
					}
					
					if(flag){//该机组有数据项，需要插入到meihyb中
						
						if(fadh==null || fadh.equals("")) fadh="0";
						if(gongrh==null || gongrh.equals("")) gongrh="0";
						if(fadcydh==null || fadcydh.equals("")) fadcydh="0";
						if(gongdh==null || gongdh.equals("")) gongdh="0";
						
						if(gongdh.equals("0")){//供电量为0   用发电量-发电厂用电量
							gongdh=(Double.parseDouble(fadh)-Double.parseDouble(fadcydh))+"";
						}
						
						insertSql+=" insert into riscsjb (id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl) values \n" +
							"  (getnewid("+getTreeid()+"),"+dc_id+","+DateUtil.FormatOracleDate(this.getRiqi())+"," +
							jizid+","+fadh+","+gongdh+","+gongrh+",0"+",0"+");\n" ;
						
					}
				}
				
				insertSql=" begin \n"+insertSql+" end ;";
				
				con.getUpdate(insertSql);
			}
		}else if(visit.getDiancxxb_id()==229){//保定热电厂生产数据接口
			JDBCcon sqlserver =null;
			String insertSql=" delete from riscsjb where riq="+DateUtil.FormatOracleDate(this.getRiqi())+" and diancxxb_id in ("+this.getTreeid()+") ;";
			String tableName="scrbb";
			String sql="";
			if(tableName!=null){//
				sql=" select j.id, j.jizbh,j.diancxxb_id from jizb j where j.diancxxb_id in ("+this.getTreeid()+")  order by xuh asc ";
				rsl=con.getResultSetList(sql);
				
				while(rsl.next()){
					String mingc=rsl.getString("jizbh");
					String jizid=rsl.getString("id");
					String dc_id=rsl.getString("diancxxb_id");
					if(mingc.equals("华源机组")){		
						sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
								"jdbc:odbc:hymis","scrb","rbgl");
						String sql2=" select rq,fdldr,swdldr,swdldr,round(grldr,0) grldr from " +tableName+" "+
						"  where rq="+DateUtil.FormatOracleDate(this.getRiqi());
						
						rsl2=sqlserver.getResultSetList(sql2);
						
						String fadh="";//发电量
						String gongdh="";//供电量
						String shangwdl="";//上网电量
						String gongrh="";
						boolean flag=false;
						while(rsl2.next()){
								fadh=rsl2.getString("fdldr");
								gongdh=rsl2.getString("swdldr");
								shangwdl=rsl2.getString("swdldr");
								gongrh=rsl2.getString("grldr");
						
							flag=true;
						}
						sqlserver.Close();
						if(flag){//该机组有数据项，需要插入到meihyb中
							
							if(fadh==null || fadh.equals("")) fadh="0";
							if(gongrh==null || gongrh.equals("")) gongrh="0";
							if(shangwdl==null || shangwdl.equals("")) shangwdl="0";
							if(gongdh==null || gongdh.equals("")) gongdh="0";
							
							if(gongdh.equals("0")){//供电量为0   供电量=上网电量
								gongdh=Double.parseDouble(shangwdl)+"";
							}
							
							insertSql+=" insert into riscsjb (id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl) values \n" +
							"  (getnewid("+getTreeid()+"),"+dc_id+","+DateUtil.FormatOracleDate(this.getRiqi())+"," +
							jizid+","+CustomMaths.round(Double.parseDouble(fadh), 0)+","+CustomMaths.round(Double.parseDouble(gongdh),0)+","+gongrh+","+CustomMaths.round((Double.parseDouble(fadh)/(250000*24))*100,2)+","+shangwdl+");\n" ;
							
						}
						
					}else if(mingc.equals("二厂机组")){
						
						sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
								"jdbc:odbc:ecmis","xcjhtj","rbgl");
						String sql2=" select rq,fdldr,swdldr,swdldr,round(grldr,0) grldr from "+tableName+" "+
						"  where rq="+DateUtil.FormatOracleDate(this.getRiqi());
						
						rsl2=sqlserver.getResultSetList(sql2);
						
						String fadh="";//发电量
						String gongdh="";//供电量
						String shangwdl="";//上网电量
						String gongrh="";
						boolean flag=false;
						while(rsl2.next()){
								fadh=rsl2.getString("fdldr");
								gongdh=rsl2.getString("swdldr");
								shangwdl=rsl2.getString("swdldr");
								gongrh=rsl2.getString("grldr");
						
							flag=true;
						}
						
						if(flag){//该机组有数据项，需要插入到meihyb中
							
							if(fadh==null || fadh.equals("")) fadh="0";
							if(gongrh==null || gongrh.equals("")) gongrh="0";
							if(shangwdl==null || shangwdl.equals("")) shangwdl="0";
							if(gongdh==null || gongdh.equals("")) gongdh="0";
							
							if(gongdh.equals("0")){//供电量为0   供电量=上网电量
								gongdh=Double.parseDouble(shangwdl)+"";
							}
							
							insertSql+=" insert into riscsjb (id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl) values \n" +
							"  (getnewid("+getTreeid()+"),"+dc_id+","+DateUtil.FormatOracleDate(this.getRiqi())+"," +
							jizid+","+CustomMaths.round(Double.parseDouble(fadh), 0)+","+CustomMaths.round(Double.parseDouble(gongdh),0)+","+gongrh+","+CustomMaths.round((Double.parseDouble(fadh)/(400000*24))*100,2)+","+shangwdl+");\n" ;
							
						}
					}
					
					
				}
				
				insertSql=" begin \n"+insertSql+" end ;";
				
				con.getUpdate(insertSql);
			}
		}
		
		if(rsl!=null){
			
			rsl.close();
		}
		if(rsl2!=null){
			
			rsl2.close();
		}
		con.Close();
	}
	private void shous(){
		JDBCcon con=new JDBCcon();
//		Visit visit = (Visit) getPage().getVisit();
		String sql="select *\n" +
		"from riscsjb\n" + 
		"where to_char(riscsjb.riq,'yyyy-mm-dd')='"+getRiqi()+"'";
		ResultSet rs=con.getResultSet(sql);
		try{
			if(rs.next()){//如果入炉耗用存在
				setMsg("发电信息已经存在，请删除后再操作！");
			}else{
				sql="insert into riscsjb(id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl)(\n" + 
				"select getnewid("+getTreeid()+")id,"+getTreeid()+" diancxxb_id,to_date('"+getRiqi()+"','yyyy-mm-dd')riq,\n" + 
				"decode(jizh,'#1',(select id from jizb where jizbh='#1'),(select id from jizb where jizbh='#2'))jizbh,\n" + 
				"nvl(fadl,0)*1000,nvl(gongdl,0)*1000,0,nvl(fuhl,0),nvl(shangwdl,0)*1000\n" + 
				"from(\n" + 
				"select '#1'jizh,sum(fadl1)fadl,sum(shangwdl)shangwdl,sum(gongdl)gongdl,sum(fuhl)fuhl\n" + 
				"from(\n" + 
				"    select plan_xh,plan_data fadl1,0 shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10001' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,plan_data shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10006' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,plan_data gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10007' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,0 gongdl,plan_data fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10028' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				" )\n" + 
				" union\n" + 
				" select '#2'jizh,sum(fadl1),sum(shangwdl),sum(gongdl),sum(fuhl)\n" + 
				"from(\n" + 
				"    select plan_xh,plan_data fadl1,0 shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20001' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,plan_data shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20006' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,plan_data gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20007' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,0 gongdl,plan_data fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20028' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				" )))";
				con.getInsert(sql); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon(); 
		String riqTiaoj=this.getRiqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		Visit visit = ((Visit) getPage().getVisit());
//		String riq = DateUtil.FormatDate(new Date());
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select r.id,r.diancxxb_id,r.riq,j.jizbh as jizb_id,r.HAOML,round(r.fadl/10000,4) fadl,round(r.gongdl/10000,4) gongdl,round(r.shangwdl/10000,4) shangwdl,r.gongrl,r.fadfhl \n");
		sbsql.append("from riscsjb r,jizb j,diancxxb d \n");
		sbsql.append("where r.jizb_id=j.id and riq=to_date('"+riqTiaoj+"','yyyy-mm-dd') \n");
		sbsql.append("and r.diancxxb_id=d.id and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+") order by id");
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sbsql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,"Riscsjwh");
		egu.setTableName("riscsjb");
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("diancxxb_id").setWidth(80);
		egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancxxb_id()+"");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("riq").setDefaultValue(riqTiaoj);
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("jizb_id").setHeader("机组编号");
		egu.getColumn("jizb_id").setWidth(100);
		egu.getColumn("HAOML").setHeader("耗煤量(吨)");
		egu.getColumn("HAOML").setWidth(100);
		egu.getColumn("HAOML").setDefaultValue("0");
		if(MainGlobal.getXitxx_item("日生产数据", "是否隐藏耗煤量信息", "0", "是").equals("是")){
			egu.getColumn("HAOML").setHidden(true);
		}
		egu.getColumn("fadl").setHeader("发电量<br>(万千瓦时)");
		egu.getColumn("fadl").setWidth(80);
		((NumberField)egu.getColumn("fadl").editor).setDecimalPrecision(4);
		egu.getColumn("gongdl").setHeader("供电量<br>(万千瓦时)");
		egu.getColumn("gongdl").setWidth(80);
		((NumberField)egu.getColumn("gongdl").editor).setDecimalPrecision(4);
		egu.getColumn("shangwdl").setHeader("上网电量<br>(万千瓦时)");
		egu.getColumn("shangwdl").setWidth(80);
		((NumberField)egu.getColumn("shangwdl").editor).setDecimalPrecision(4);
		egu.getColumn("gongrl").setHeader("供热量<br>(吉焦)");
		egu.getColumn("gongrl").setWidth(80);
		egu.getColumn("fadfhl").setHeader("发电<br>负荷率(%)");
		egu.getColumn("fadfhl").setWidth(100);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
//		 设置机组下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("jizb_id").setEditor(c1);
		c1.setEditable(true);
		String Sql = "select id, jizbh from jizb where diancxxb_id in (select id from diancxxb d where (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+"))";
		egu.getColumn("jizb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql));
		
//		 设置树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		//egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		if(MainGlobal.getXitxx_item("耗用电量", "耗用电量取数据", "0", "否").equals("是")){
//			egu.addToolbarButton(GridButton.."收取数据", "shouqsj");
			egu.addToolbarItem("{"+new GridButton("收取数据","function(){document.getElementById('shousButton').click();}").getScript()+"}");
		}
		
//		添加导入按钮
		String sql=" select * from xitxxb where mingc='日生产数据添加导入按钮' and leib='调运' and zhuangt=1 and zhi='是' ";
		ResultSetList rs1=con.getResultSetList(sql);
		if(rs1.next()){
			GridButton daor=new GridButton("导入","function(){ Ext.MessageBox.confirm('提示信息','确定要覆盖掉该时间的数据吗?',function(btn){if(btn=='yes'){document.all.DaorButton.click();} });}");
			egu.addTbarBtn(daor);
		}
		rs1.close();
		
		
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
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

//	树
	private String treeid;
	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
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

	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTbmsg(null);
//			treeid = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			setTreeid(null);
		}
		getSelectData();
	}
}