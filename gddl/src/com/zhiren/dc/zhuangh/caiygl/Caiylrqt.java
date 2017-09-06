package com.zhiren.dc.zhuangh.caiygl;


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
 * 描述：隐藏huirdsf字段。
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
public class Caiylrqt extends BasePage implements PageValidateListener {
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
	private String Changerq;

	public String getChangerq() {
		return Changerq;
	}
	
	public void setChangerq(String changerq) {
		Changerq = changerq;
	}
	
	// 页面变化记录
	private String Changed;

	public String getChanged() {
		return Changed;
	}

	public void setChanged(String changed) {
		Changed = changed;
	}


	public static int InsYangpdhb(JDBCcon con, long diancxxb_id, long zhilb_id, String caiyb_id, 
			String zhillsb_id, String leibid, String bmid, String zhuanmsz, String leib){
		String sql = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id) "+
		"values(getnewid(" + diancxxb_id + ")," + caiyb_id + "," + zhillsb_id + 
		",'" + zhuanmsz + "','" + leib + "'," + bmid + "," + leibid + ")";
		return con.getInsert(sql);
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
	
	public String bM(){
		String a="";
		
		return a;
	}
	
	public static int InsZhillsb(JDBCcon con, long diancxxb_id, long zhilb_id, 
			String zhillsb_id, String leibid, String bmid, String leib,String caiyy,String caiysj,String caiyml){//,String leix
		String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,caiyy,caiysj,caiyml) "+//,leix
		"values(" + zhillsb_id + "," + zhilb_id + ",0,'" + "正常样" + "'," + leibid + "," + bmid + ",'"+caiyy+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),"+caiyml+")";//","","+huaylb+
		return con.getInsert(sql);
	}
	
	public static int InsZhuanmb(JDBCcon con,long zhuanmlb_id, long diancxxb_id, 
			String zhillsb_id, String bianm,String caiysj){
		
		String sql = "insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
		"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
		"'," + zhuanmlb_id + ");\n";
		
		String sql_z = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id +" and mingc='制样编码'";
		ResultSetList rsl =  con.getResultSetList(sql_z);
		StringBuffer sb_z=new StringBuffer("begin \n");
		 String czh=
	        	"select *\n" +
	        	"  from xitxxb\n" + 
	        	" where mingc = '判断是否自动三级编码'\n" + 
	        	"   and diancxxb_id = 300\n" + 
	        	"   and zhi = '否'\n" + 
	        	"   and leib = '采制化'\n" + 
	        	"   and zhuangt = 1\n" + 
	        	"   and beiz = '使用'";
		ResultSetList rsl_czh =  con.getResultSetList(czh);
		if(rsl_czh.getRows()>0){//大于0 那么是手动填写
			
		}else{
			//自动填写

		while(rsl.next()){
			boolean flag= false;
			
			long ab=Math.round(Math.random()*99);
			 bianm =caiysj+"F"+ab;
			if(String.valueOf(ab).length()==1){
				
				bianm =caiysj+"F"+"0"+ab;
			}else{
				
				bianm =caiysj+"F"+ab;
			}
			
			String try_bian=" select * from zhuanmb where bianm = '"+bianm+"'";
			ResultSetList rsl_bian = con.getResultSetList(try_bian);
		
			while(rsl_bian.next()){
				
				if(bianm==rsl_bian.getString("bianm")||bianm.equals(rsl_bian.getString("bianm"))){
					flag=true;
				}
			}
			while(flag){
				
				long aa=Math.round(Math.random()*99);
				
				if(String.valueOf(aa).length()==1){
					
					bianm =caiysj+"F"+"0"+aa;;
				}else{
					
					bianm =caiysj+"F"+aa;
				}
			flag=panDflag(bianm,con);
			
			}
			sb_z.append("insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
					"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
					"'," + rsl.getString("id") + ");\n");
		}
		
		String sql_h = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id +" and mingc='化验编码'";
		ResultSetList rsl_h =  con.getResultSetList(sql_h);
		//产生化验编码，随机产生后两位号码，但是不能重复，所以利用while来进行循环，根据电厂业务同一个船煤是不会有太多的样
        while(rsl_h.next()){
            boolean flag= false;
			
			long ab=Math.round(Math.random()*99);
			 bianm =caiysj+"J"+ab;
			if(String.valueOf(ab).length()==1){
				
				bianm =caiysj+"J"+"0"+ab;
			}else{
				
				bianm =caiysj+"J"+ab;
			}
			
			String try_bian=" select * from zhuanmb where bianm = '"+bianm+"'";
			ResultSetList rsl_bian = con.getResultSetList(try_bian);
		
			while(rsl_bian.next()){
				//根据第一次产生的来和数据库已经存在的编码判断，并用flag记录
				if(bianm==rsl_bian.getString("bianm")||bianm.equals(rsl_bian.getString("bianm"))){
					flag=true;
				}
			}
			while(flag){
				//产生随机数
				long aa=Math.round(Math.random()*99);
				
				if(String.valueOf(aa).length()==1){
					//产生编码,位数不够
					bianm =caiysj+"J"+"0"+aa;;
				}else{
					//产生编码，位数为两位
					bianm =caiysj+"J"+aa;
				}
				// 调用方法返回是否需要重新生成新的编码，
			flag=panDflag(bianm,con);
			
			}
			//插入到转码表
			sb_z.append("insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
					"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
					"'," + rsl_h.getString("id") + ");\n");
			
		}
		}
		
        sb_z.append(sql);
        
        sb_z.append("end;");
		return con.getInsert(sb_z.toString());
	}
	
	public static int Zhuanmcz(JDBCcon con, long diancxxb_id, String zhillsb_id,
			String bianm, boolean shifzm, String zhuanmqz,String caiysj){
		
		String sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id +" and mingc='采样编码'";
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
			flag = InsZhuanmb(con,rsl.getLong("id"),diancxxb_id,zhillsb_id,bianm,caiysj);
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
	
	private boolean _Save1Chick = false;

	public void Save1Button(IRequestCycle cycle) {
		_Save1Chick = true;
	}
	
	private boolean _Save2Chick = false;

	public void Save2Button(IRequestCycle cycle) {
		_Save2Chick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save1();
			getSelectData();
		}
		if (_Save1Chick) {
			_Save1Chick = false;
//			Save1();
			getSelectData();
		}
		if (_Save2Chick) {
			_Save2Chick = false;
			Save2();
			setYundxxValue(null);		//4
			setYundxxModel(null);
			getSelectData();
		}
		if(_RefurbishChick){
		   _RefurbishChick = false;
			getSelectData();
		}
	}
	private void Save2() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql="update rulmzlzb set haozt=1 where id="+getChangeid();
		con.getUpdate(sql);
		con.Close();
	}
	private void Save1() {
		// 保存第二组信息，会有第一组的id，
		
		Visit visit = (Visit) getPage().getVisit();
		String tableName = "QITYMXB";
		JDBCcon con = new JDBCcon(); 

        StringBuffer sb_e=new StringBuffer();
        sb_e.append("begin \n");
		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(getChange());
		while (delrsl.next()) {
			//删除
			sb_e.append(" delete from ").append(tableName).append(" where id="+delrsl.getString(0)).append(";\n");
			sb_e.append(" delete from ").append("zhillsb").append(" where id="+delrsl.getString(0)).append(";\n");
			sb_e.append(" delete from ").append("yangpdhb").append(" where zhilblsb_id="+delrsl.getString(0)).append(";\n");
			sb_e.append(" delete from ").append("caiyb").append(" where zhilb_id="+delrsl.getString(0)).append(";\n");
			sb_e.append(" delete from ").append("RULCYYGLB").append(" where RULMZLZB_ID="+delrsl.getString(0)).append(";\n");
			sb_e.append(" delete from ").append("zhuanmb").append(" where zhillsb_id="+delrsl.getString(0)).append(";\n");
		}
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		long Diancxxb_id=0;
		
		if (visit.isFencb()) {
			
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			
			Diancxxb_id = visit.getDiancxxb_id();
		}
		//初始化煤量
		int meil=0;
//		int meil_hz=0;
		
		String sqlbm = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
		"where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
		" and c.leibb_id = l.id\n" +
		" and f.mingc = '默认' and f.diancxxb_id =" + Diancxxb_id;
		
		ResultSetList r = con.getResultSetList(sqlbm);
		String leib ="";
		String zhuanmsz="";
		String leibid="";
//		boolean shifzm;
		String bumid="";
//		String leix="";
		while(r.next()){
			
//				新建质量临时ID
//				String zhillsb_id = MainGlobal.getNewID(Diancxxb_id);
//				转码类别名称
			leib= r.getString("leib");
//				转码编码
			zhuanmsz = r.getString("zhuanmsz");
//				化验类别ID
			leibid = r.getString("lbid");
//				部门ID
			bumid = r.getString("bmid");
//				是否转码
//			shifzm= r.getInt("shifzm") == 1;
//			leix=r.getString("leix");
		}
		while (mdrsl.next()) {
			
				meil=mdrsl.getInt("MEIL");
//				meil_hz+=meil;
				String zzb=MainGlobal.getNewID(Diancxxb_id);
				long bin2=Integer.parseInt(mdrsl.getString("caiysj").substring(0, 10).replaceAll("-", ""));
				Date d=new Date(bin2);
				String caiysj=String.valueOf(d.getTime());//getChangerq();

				String shijd=MainGlobal.getXitxx_item("数量", "入厂入炉节点时间", "0", "0");
//				if(Integer.parseInt(mdrsl.getString("caiysj").substring(11, 13))<Integer.parseInt(shijd)){
//					String sql_date="select to_date('"+caiysj+"','yyyy-mm-dd')-1 as caiysj from dual";
//					ResultSetList rs_date = con.getResultSetList(sql_date);
//					while(rs_date.next()){
//						caiysj = rs_date.getDateString("caiysj").replaceAll("-", "");
//					}
//				}
				
				String meiz=mdrsl.getString("MEIZMC");
				String meic=mdrsl.getString("MEICMC");
				String meid=mdrsl.getString("MEIDMC");
				String rulid="0";

				String pin=mdrsl.getString("caiysj");
				String rulrq=mdrsl.getString("songysj");//da.toLocaleString();

				String songysj=mdrsl.getString("songysj");

			if ("0".equals(mdrsl.getString("ID"))) {
				// 查出最大的耗用编号，然后加1
				String haoyh="";
				int haoy=0;
				String haoy_end="";
				String haoybh_max=
					"select max(r.haoybh) haoybh from QITYMXB r where r.haoybh like '"+rulrq.substring(0, 10).replaceAll("-", "")+"%'";
				ResultSetList ruls_max = con.getResultSetList(haoybh_max);
				while (ruls_max.next()){
					if("".equals(ruls_max.getString("haoybh"))||ruls_max.getString("haoybh")==null){
						haoyh="00";
					}else{
						
						haoyh=ruls_max.getString("haoybh").substring(8, 10);
					}
				}
				if(ruls_max.getRows()>0){//查出数据
					haoy=Integer.parseInt(haoyh)+1;
					if(String.valueOf(haoy).length()<=1){
						
						haoy_end="0"+haoy;	
					}else{
						haoy_end=""+haoy;
					}
					
				}else{//没有数据
					haoy_end="00";
					
				}
				String gongysb_id=mdrsl.getString("gongysb_id");
				//新增加的信息
				sb_e.append(" insert into ").append(tableName)//MEIZBH,MEICBH,
				.append(" ( id,RULMZLZB_ID,MEIZMC,MEIL,MEICMC,MEIDMC,BEIZ,laiylx,haoybh,songyr,songysj,diancxxb_id,shenhzt,rulmzlb_id,caiysj,gongysb_id,huirdsf)")
				.append(" values (" + zzb + ",").append(zzb+",'")
//				.append(mdrsl.getString("MIAOS")+"','")
//				.append(mdrsl.getString("MEIZBH")+"','")
				.append(mdrsl.getString("MEIZMC")+"',")
//				.append(mdrsl.getString("MEICBH")+"',")
				.append(meil+",'").append(mdrsl.getString("MEICMC")+"','").append(mdrsl.getString("MEIDMC")+"','")
				.append(mdrsl.getString("BEIZ")+"','"+mdrsl.getString("laiylx")+"','"+rulrq.substring(0, 10).replaceAll("-", "")
						+haoy_end+"','"+mdrsl.getString("songyr")+ "',to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss'),"
						+Diancxxb_id+",0 ,"+rulid
						+",to_date('"+pin+"','YYYY-MM-DD hh24:mi:ss')"+","+(getExtGrid().getColumn("gongysb_id").combo).getBeanId(gongysb_id)+
						",'"+mdrsl.getString("HUIRDSF")+"'"+");\n");
				
				String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,caiyy,caiysj,caiyml) "+//,leix
				"values(" + zzb + "," + 0 + ",0,'" + "其他样" + "'," + leibid + "," + bumid + ",'"+mdrsl.getString("caiyy")+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),"+meil+");\n";//","","+huaylb+
			
				sb_e.append(sql);
				
				String bianm="";
				
				//此处有问题，应该是 产生编码的语句
				if("".equals(bianm)||bianm==null){
					
					boolean flag1= false;
					
					long ab=Math.round(Math.random()*99);
					 bianm =caiysj+"R"+ab;
					if(String.valueOf(ab).length()==1){
						
						bianm =caiysj+"R"+"0"+ab;//.substring(0, 10).replaceAll("-", "")
					}else{
						
						bianm =caiysj+"R"+ab;
					}
					
					String try_bian=" select * from zhuanmb where bianm = '"+bianm+"'";
					ResultSetList rsl_bian = con.getResultSetList(try_bian);
				
					while(rsl_bian.next()){
						
						if(bianm==rsl_bian.getString("bianm")||bianm.equals(rsl_bian.getString("bianm"))){
							flag1=true;
						}
					}
					while(flag1){
						
						long aa=Math.round(Math.random()*99);
						
						if(String.valueOf(aa).length()==1){
							
							bianm =caiysj+"R"+"0"+aa;;
						}else{
							
							bianm =caiysj+"R"+aa;
						}
					flag1=panDflag(bianm,con);
					
					}
				}
					else{
					
					bianm=mdrsl.getString("BIANM");
				}

					String ssql1="select id from zhuanmlb where mingc='采样编码'";
					String ssql2="select id from zhuanmlb where mingc='制样编码'";
					String ssql3="select id from zhuanmlb where mingc='化验编码'";
					ResultSetList rsls1 = con.getResultSetList(ssql1);
					ResultSetList rsls2 = con.getResultSetList(ssql2);
					ResultSetList rsls3 = con.getResultSetList(ssql3);
//					String zhillsb_id=	getChangeid();//入炉煤质量子表
					String caiy_id="";
					String span="";
					caiy_id= MainGlobal.getNewID(Diancxxb_id);
					
					
					if(Integer.parseInt(mdrsl.getString("caiysj").substring(11, 13))<Integer.parseInt(shijd)){
						span="       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss') and\n" + 
						"       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')+1 and " +
						" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
					}else{
						span="       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')-1 and\n" + 
						"       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')   and " +
						" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
					}
						String sql_14=
							"select c.id,rb.caiyb_id\n" +
							"  from caiyb c, QITYMXB rb \n" + 
							" where c.id = rb.caiyb_id \n" + 
							"   and rb.rulrq  between\n" + span;

						ResultSetList rsls_14 = con.getResultSetList(sql_14);
						 boolean flag=false;
						while (rsls_14.next()){
							//取出采样id
							caiy_id=rsls_14.getString("id");
							flag=true;
						}
						
						
						if(flag){
							sb_e.append(" update QITYMXB set bianm='"+bianm+"' ,caiyb_id="+caiy_id+" where id="+zzb).append(";\n");
							//如果有数据，在上一步中已经取了数据，没有数据那么走else来生成采样信息
							
						}else{
							while(rsls1.next()){
								sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
										"values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls1.getString("id")+""+");\n"
								);
//						人员关联表//入炉煤质量子明细
								sb_e.append(" insert into RULCYYGLB (RULMZLZB_ID,renyxxb_id) values ("+zzb+",'"+0+"')").append(";\n");

								// 此处判断，找个时间段内的采样表有没有数据，如果有且只有一条，才能读取数据并且赋值给caiyb_id，
//						假如有多条数据，说明有脏数据，如果没有那么才能插入数据。
								// 判断14:00-明天14：00
							}
						
							String sr=" insert into caiyb (id,zhilb_id,cunywzb_id,bianm,caiyrq,xuh) values" +
							"("+caiy_id+","+zzb+",0,'"+bianm+""+"',to_date('"
							  +rulrq+"','YYYY-MM-DD hh24:mi:ss'),0"+");\n";
							
							String sql_y = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id) "+
							"values(getnewid(" + Diancxxb_id + ")," + caiy_id + "," + zzb + 
							",'" + zhuanmsz + "','" + leib + "'," + bumid + "," + leibid + ");\n";
							sb_e.append(sql_y);
							sb_e.append(sr);
//						}
							sb_e.append(" update QITYMXB set bianm='"+bianm+"' ,caiyb_id="+caiy_id+" where id="+zzb).append(";\n");
						//插入到采样表
						//读取采样表id

//					 制样信息
					while(rsls2.next()){
						boolean flag2= false;
						
						long ab1=Math.round(Math.random()*99);
						 bianm =caiysj+"F"+ab1;
						if(String.valueOf(ab1).length()==1){
							
							bianm =caiysj+"F"+"0"+ab1;
						}else{
							
							bianm =caiysj+"F"+ab1;
						}
						
						String try_bian1=" select * from zhuanmb where bianm = '"+bianm+"'";
						ResultSetList rsl_bian1 = con.getResultSetList(try_bian1);
					
						while(rsl_bian1.next()){
							
							if(bianm==rsl_bian1.getString("bianm")||bianm.equals(rsl_bian1.getString("bianm"))){
								flag2=true;
							}
						}
						while(flag2){
							
							long aa=Math.round(Math.random()*99);
							
							if(String.valueOf(aa).length()==1){
								
								bianm =caiysj+"F"+"0"+aa;;
							}else{
								
								bianm =caiysj+"F"+aa;
							}
						flag2=panDflag(bianm,con);
						
						}

						sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id) values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls2.getString("id")+""+");\n");	
					}
					
//					 化验信息
					while(rsls3.next()){
						
						boolean flag3= false;
						
						long ab=Math.round(Math.random()*99);
						 bianm =caiysj+"J"+ab;
						if(String.valueOf(ab).length()==1){
							
							bianm =caiysj+"J"+"0"+ab;
						}else{
							
							bianm =caiysj+"J"+ab;
						}
						
						String try_bian=" select * from zhuanmb where bianm = '"+bianm+"'";
						ResultSetList rsl_bian = con.getResultSetList(try_bian);
					
						while(rsl_bian.next()){
							
							if(bianm==rsl_bian.getString("bianm")||bianm.equals(rsl_bian.getString("bianm"))){
								flag3=true;
							}
						}
						while(flag3){
							
							long aa=Math.round(Math.random()*99);
							
							if(String.valueOf(aa).length()==1){
								
								bianm =caiysj+"J"+"0"+aa;;
							}else{
								
								bianm =caiysj+"J"+aa;
							}
						flag3=panDflag(bianm,con);
						
						}

					
						sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
								"values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls3.getString("id")+""+");\n"
								);
					}
						}
//				}
				
				}else{
					//原则是先把原有删除，然后再进行插入
					//修改的信息
					//放在另一个事务里处理
					 sb_e.append(" update zhillsb set caiysj=to_date('"+ pin+
							 "','yyyy-mm-dd hh24:mi:ss'), caiyy='"+ mdrsl.getString("caiyy")+
							 "', caiyml="+mdrsl.getString("meil")+" where id="+mdrsl.getString("ID")).append(";\n");


					 sb_e.append(" update QITYMXB set caiysj=to_date('"+pin+"','yyyy-mm-dd hh24:mi:ss'), songysj=to_date('"+ songysj+"','yyyy-mm-dd hh24:mi:ss'),songyr='"+ mdrsl.getString("songyr")+"', meizmc='"+mdrsl.getString("meizmc")+"',meicmc='"+
							 mdrsl.getString("meicmc")+"',meidmc='"+mdrsl.getString("meidmc")+"', meil="+mdrsl.getString("meil")+",beiz='"+mdrsl.getString("BEIZ")+"' where id="+mdrsl.getString("ID")).append(";\n");

				}
		}
		
		sb_e.append("end;\n");
		if(sb_e.length()>13){
			
			con.getUpdate(sb_e.toString());
		}

	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
        String ssql=
        	"select rb.id,rb.laiylx,rb.BIANM,rb.haoybh,g.mingc as gongysb_id,rb.MEIZMC,rb.MEICMC,rb.MEIDMC,rb.meil ,rb.songyr,to_char(rb.songysj,'yyyy-mm-dd hh24:mi:ss') songysj\n" +
        	"       ,\n" + 
        	"       \n" + 
        	"       \n" + 
        	"      \n" + 
        	"      \n" + 
        	"     \n" + 

        	"  rb.huirdsf ,  \n" + 
        	"      to_char(rb.caiysj,'yyyy-mm-dd hh24:mi:ss') caiysj,z.caiyy, rb.beiz \n" + 
        	"  from QITYMXB rb ,zhillsb z,gongysb g \n"+//RULCYYGLB glb,renyxxb ren,,zhuanmb b, zhuanmlb l,
        	"  where "+//rb.haoybh ='"+getYundxxValue().getValue()+"'and 
			" rb.haozt=0 and rb.gongysb_id=g.id(+) and rb.rulmzlzb_id=z.id and  to_char(songysj,'yyyy-mm-dd') >='"+getRiq1()+"' and to_char(songysj,'yyyy-mm-dd')< ='"+getRiq2()+"'";//and glb.RENYXXB_ID=ren.id 
		ResultSetList rsl1 = con.getResultSetList(ssql);
//		String id="";
//        while(rsl1.next()){
//        	id=rsl1.getString("id");
//        }
        //记录记录集的条数
//        int jil=0;
//        if(rsl1.getRows()>0){
//        	jil=rsl1.getRows();
//        }
        //查出数据后进行记录集重置
//        rsl1.beforefirst();
		if (rsl1 == null) {
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
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl1);
		
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
		
		egu.getColumn("haoybh").setHeader("登记编号");//Center
		egu.getColumn("haoybh").setWidth(70);
		egu.getColumn("haoybh").editor = null;
		
		egu.getColumn("bianm").setHeader("采样编码");//Center
		egu.getColumn("bianm").setWidth(90);
		egu.getColumn("bianm").editor = null;
		
		egu.getColumn("gongysb_id").setHeader("供应商名称");
		egu.getColumn("gongysb_id").setWidth(100);
		
		egu.getColumn("laiylx").setHeader("来源类型");//Center
		egu.getColumn("laiylx").setWidth(70);
		
		egu.getColumn("MEIZMC").setHeader("煤种");//Center
		egu.getColumn("MEIZMC").setWidth(70);
		egu.getColumn("MEICMC").setHeader("煤场");//Center
		egu.getColumn("MEICMC").setWidth(70);
		
		egu.getColumn("MEIDMC").setHeader("煤堆");//Center
		egu.getColumn("MEIDMC").setWidth(70);

		
		egu.getColumn("songyr").setHeader("送样人");//Center
		egu.getColumn("songyr").setWidth(70);
		egu.getColumn("caiyy").setHeader("登记员");//Center
		egu.getColumn("caiyy").setWidth(70);
		egu.getColumn("songysj").setHeader("送样时间");//Center
		egu.getColumn("songysj").setWidth(120);

		egu.getColumn("caiysj").setHeader("采样时间");//Center
		egu.getColumn("caiysj").setWidth(120);

		
		egu.getColumn("meil").setHeader("煤量");//Center
		egu.getColumn("meil").setWidth(70);

		egu.getColumn("huirdsf").setHeader("灰熔点检查");//Center
		egu.getColumn("huirdsf").setWidth(90);
		egu.getColumn("huirdsf").setDefaultValue("否");
		egu.getColumn("beiz").setHeader("备注");//Center
		egu.getColumn("beiz").setWidth(400);
		egu.getColumn("beiz").setDefaultValue("正常取样");
		egu.getColumn("beiz").editor.allowBlank=false;
		

		DatetimeField datetime = new DatetimeField();
		egu.getColumn("songysj").setEditor(datetime);
		egu.getColumn("songysj").setDefaultValue("");
//		egu.getColumn("shangmjssj").editor.setAllowBlank(true);
		DatetimeField datet = new DatetimeField();
		egu.getColumn("caiysj").setEditor(datet);
		egu.getColumn("caiysj").setDefaultValue("");
		
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql="select id,mingc from gongysb  where leix =1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("huirdsf").setEditor(c1);
		c1.setEditable(true);
		String MeicbSql1="select  1 as id,'是' as mingc from dual union select 2 as id,'否' as mingc from dual ";
		egu.getColumn("huirdsf").setComboEditor(egu.gridId, new IDropDownModel(MeicbSql1));
		egu.getColumn("huirdsf").setReturnId(true);
		egu.getColumn("huirdsf").setHidden(true);
		
//		煤的来源
		ComboBox c9 = new ComboBox();
		egu.getColumn("laiylx").setEditor(c9);
		c9.setEditable(true);
		String MeicbSql9="select  1 as id,'外来煤' as mingc from dual union select 2 as id,'库存煤' as mingc from dual ";
		egu.getColumn("laiylx").setComboEditor(egu.gridId, new IDropDownModel(MeicbSql9));
		egu.getColumn("laiylx").setReturnId(true);
		
//		煤场id
		ComboBox c8 = new ComboBox();
		egu.getColumn("meicmc").setEditor(c8);
		c8.setEditable(true);
		String MeicbSql="select id,mingc from meicb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
		egu.getColumn("meicmc").setComboEditor(egu.gridId, new IDropDownModel(MeicbSql));
		egu.getColumn("meicmc").setReturnId(true);
		egu.getColumn("meicmc").editor.allowBlank=true;
		
//		煤种id
		ComboBox c7 = new ComboBox();
		egu.getColumn("meizmc").setEditor(c7);
		c7.setEditable(true);
		String MeizbSql="select id,mingc from pinzb  "+"  order by mingc";
		egu.getColumn("meizmc").setComboEditor(egu.gridId, new IDropDownModel(MeizbSql));
		egu.getColumn("meizmc").setReturnId(true);
		egu.getColumn("meizmc").editor.allowBlank=true;
		
////	煤堆id
		ComboBox c4 = new ComboBox();
		egu.getColumn("meidmc").setEditor(c4);
		c4.setEditable(true);
		String MeikbSql4="select id,  mingc  from duow WHERE diancxxb_id="+visit.getDiancxxb_id()+" and zhuangt=1  order by mingc";
		egu.getColumn("meidmc").setComboEditor(egu.gridId, new IDropDownModel(MeikbSql4));
		egu.getColumn("meidmc").setReturnId(true);
		egu.getColumn("meidmc").editor.allowBlank=true;
		
		egu.addTbarText("送样日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至：");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");// 设置分隔符

		

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		String codt_in=" var Mrcd1 = gridDiv_ds.getModifiedRecords();"
			
			+"if(Mrcd1.length==1){Ext.MessageBox.alert('提示信息','一个时间段只能有一个耗用信息！'); return;}";
		
		String codt=
//			" var Mrcd1 = gridDiv_ds.getModifiedRecords();"
//			
//			+"if(Mrcd1.length>1){Ext.MessageBox.alert('提示信息','一个时间段只能有一个耗用信息！'); return;}" +
			""			+"  var Mrcdd = gridDiv_ds.getModifiedRecords(); " +
//					"if(Mrcdd.length>1){ Ext.MessageBox.alert('提示信息','请选择保存一条信息'); return ; }"+

			"if(Mrcdd.length>1){ Ext.MessageBox.alert('提示信息','请选择保存一条信息'); return ; }\n" +
			"  for(j = 0; j< Mrcdd.length; j++){\n" + 
			"  var a = Mrcdd[j].get('MEICMC');\n" + 
			"\n" + 
			"  var b =  Mrcdd[j].get('MEIDMC');\n" + 
			"\n" + 
			"  var c=b.substring(2,3);  var d =a.substring(0,1);"+
			"    if(d!=c){\n" + 
			"    Ext.MessageBox.alert('提示信息','煤场和煤堆不对应'); return ;\n" + 
			"    }\n" + 
			"  }";
		
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"",codt_in);//,codt_in

		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",codt);
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		
		StringBuffer sbjs = new StringBuffer();

		sbjs.append("gridDiv1_grid.on('beforeedit',function(e){");

		// 设定某一行不能编辑
		sbjs.append("if(e.row==1||e.row==2"+"){gridDiv1_ds.getAt(1).get('MEICMC');}");//meicmc

		sbjs.append("});");
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
			"select id,mingc as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from meicangb \n" + 
			" where\n" + 
			"diancxxb_id="+visit.getDiancxxb_id()+"\n" + ""
			;
			
		TreeOperation dt = new TreeOperation();
//		System.out.println(sql);
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

//	按照什么时间查条件选择框
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			
			((Visit) getPage().getVisit())
			.setDropDownBean2((IDropDownBean) getWeizSelectModel()
					.getOption(0));
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
		String sql=" select to_number(haoybh) id,haoybh mingc from rulmzlzb where rulrq = to_date('"
			+getRiq1()+"','yyyy-mm-dd')";//查询耗用编号，这个耗用编号来自入炉煤质量表
		
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"请选择"));
	}
	
	
	public ExtGridUtil getExtGrid1() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid1(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript1() {
		if (getExtGrid1() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid1().addToolbarItem("'->'");
			getExtGrid1().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid1().getGridScript();
	}

	public String getGridHtml1() {
		if (getExtGrid1() == null) {
			return "";
		}
		return getExtGrid1().getHtml();
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
			DatetimeField df = new DatetimeField();
			((Visit) this.getPage().getVisit()).setString6(df.toString());
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
		String sql=" select  id,haoybh mingc from QITYMXB where haozt=0 and to_char(rulrq,'yyyy-mm-dd') ='"
			+getRiq1()+"'";
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
			setYundxxModel(null);	
			getYundxxModels();		
			setTbmsg(null);
			initNavigation();
			visit.setString1(null);
		}
		if(visit.getboolean1()){
			visit.setboolean1(false);
			visit.setList1(null);
			setYundxxValue(null);		//4
			setYundxxModel(null);
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
