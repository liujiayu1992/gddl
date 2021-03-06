package com.zhiren.dc.zhuangh.caiygl;


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

public class Caiylr_test extends BasePage implements PageValidateListener {
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
	
	private void Save() {
		 Visit visit = (Visit) this.getPage().getVisit();
		 Save1(getChange(), visit);
			setYundxxValue(null);		//4
			setYundxxModel(null);
	}
	// 这个save方法是要处理第一个grid的添加以及修改和删除的信息，
	private void Save1(String change,Visit visit) {
		
		//逻辑说明，这里涉及到14:00-14:00的概念，在这个期间的耗用信息，产生的入炉采样是同一个值，也就是，在这个时间范围内，
//		第一个添加的，要产生一个采样，其他再产生入炉耗用的（本时间段内的）采样信息是一个，
//		电厂认为这个期间的同一煤场、同一煤种、同一煤堆的采样是没有变化的。
//		关键实现：对于这个时间进行检索，如果有数据那么读取采样id，进行添加数据的控制，
//		没有数据则认为需要往采样表中添加一条数据并且产生关联。
		
		
		String tableName = "rulmzlzb";
		
		JDBCcon con = new JDBCcon();

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(change);
		while (delrsl.next()) {

				StringBuffer sql = new StringBuffer("begin \n");
				
				sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
//				sql.append("delete from yangpdhb where zhilblsb_id=").append(delrsl.getString(0)).append(";\n");
//				String asql =" select id, caiyb_id from yangpdhb where zhilblsb_id="+delrsl.getString(0);
				
//				ResultSetList arsl = con.getResultSetList(asql);
				
//				while(arsl.next()){
					
						//RULCYYGLB
//					sql.append(" delete from RULCYYGLB where rulmzlzb_id="+arsl.getString("id")).append(";\n");
					
					//增加删除采样单
//					sql.append(" delete from caiyb where id="+arsl.getString("caiyb_id")).append(";\n");
//				}
//				sql.append("delete from zhuanmb where zhillsb_id=").append(delrsl.getString(0)).append(";\n");
			String d=" select id from yangpdhb where zhilblsb_id="+delrsl.getString(0);
//			ResultSetList bs = con.getResultSetList(d);
//			while(bs.next()){
//				
//				sql.append("delete from zhiyryglb where yangpdhb_id=").append(bs.getString("id")).append(";\n");
//			}
			sql.append("end;");
			con.getUpdate(sql.toString());
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);
		
		long Diancxxb_id=0;
		//判断分厂别
		if (visit.isFencb()) {
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			Diancxxb_id = visit.getDiancxxb_id();
		}
		while (mdrsl.next()) {
			
			String huayy=mdrsl.getString("CAIYY");
			
			String beiz=mdrsl.getString("beiz");
			String rulrq=mdrsl.getString("shangmkssj");
			
			long bin2=Integer.parseInt(rulrq.substring(0, 10).replaceAll("-", ""));
			Date d=new Date(bin2);
			String riq_hy=String.valueOf(d.getTime());//getChangerq();
			if(Integer.parseInt(rulrq.substring(11, 13))>=13){
				riq_hy=String.valueOf(d.getTime()+1);
			}
			String shangmjssj=mdrsl.getString("shangmjssj");
	
			String zhuangt=mdrsl.getString("zhuangt");
			
			String lury=mdrsl.getString("caiyy");
			long meil=mdrsl.getLong("MEIL");
			String caiysj=mdrsl.getString("shangmkssj");//caiysj
			String caiyml=mdrsl.getString("CAIYML");
			
			String bianm="";
			//此处有问题，应该是 产生编码的语句
			if("".equals(mdrsl.getString("BIANM"))||mdrsl.getString("BIANM")==null){
				
				boolean flag1= false;
				
				long ab=Math.round(Math.random()*99);
				 bianm =caiysj+"R"+ab;
				if(String.valueOf(ab).length()==1){
					
					bianm =caiysj+"R"+"0"+ab;
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

			
				
			}else{
				
				bianm=mdrsl.getString("BIANM");
			}

			
			int flag;
			
			String sqlbm = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
			"where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
			" and c.leibb_id = l.id\n" +
			" and f.mingc = '默认' and f.diancxxb_id =" + Diancxxb_id;

			ResultSetList r = con.getResultSetList(sqlbm);
			String leib ="";
			String zhuanmsz="";
			String leibid="";
			boolean shifzm;
			String bumid="";
			String leix="";
			while(r.next()){
				
//			新建质量临时ID
//			String zhillsb_id = MainGlobal.getNewID(Diancxxb_id);
//			转码类别名称
				leib= r.getString("leib");
//			转码编码
				zhuanmsz = r.getString("zhuanmsz");
//			化验类别ID
				leibid = r.getString("lbid");
//			部门ID
				bumid = r.getString("bmid");
//			是否转码
				shifzm= r.getInt("shifzm") == 1;
				leix=r.getString("leix");
			}

			if ("0".equals(mdrsl.getString("ID"))) {
				long zhilb_id=0;
				zhilb_id=getYundxxValue().getId();
				String sql1 = "select * from fahb where zhilb_id = " + zhilb_id;
				ResultSetList rsl = con.getResultSetList(sql1);
				long meikxxb_id=0;
				long jihkjb_id=0;
				long yunsfsb_id=0;
				long faz_id=0;
				while(rsl.next()){
					meikxxb_id=rsl.getLong("meikxxb_id");
					jihkjb_id=rsl.getLong("jihkjb_id");
					yunsfsb_id=rsl.getLong("yunsfsb_id");
					faz_id=rsl.getLong("faz_id");
				}
				
				String zzb= MainGlobal.getNewID(Diancxxb_id);
				StringBuffer sql=new StringBuffer("begin \n");

				String rul="select id from rulbzb where mingc ='"+mdrsl.getString("BANZB_ID")+"'";
					ResultSetList ruls = con.getResultSetList(rul);
					String ss=" select id from renyxxb where quanc='"+mdrsl.getString("CAIYY")+"'";
					ResultSetList r1 = con.getResultSetList(ss);
					String mingc="";
					while(r1.next()){
						mingc=r1.getString("id");
						
					}

					StringBuffer sql2= new StringBuffer(" begin\n");
					String caiy_id="";
					caiy_id= MainGlobal.getNewID(Diancxxb_id);
					
					while (ruls.next()){
						// 入炉添加
						// 查出最大的耗用编号，然后加1
						String haoyh="";
						int haoy=0;
						String haoy_end="";
						String haoybh_max=
							"select max(r.haoybh) haoybh from rulmzlzb r where r.haoybh like '"+riq_hy+"%'";
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
						
						String spl=//huayy,  ,'"+huayy+"'
						 "insert into rulmzlzb (id,rulmzlb_id,rulrq,fenxrq,LURSJ,meil,DIANCXXB_ID,rulbzb_id,shenhzt,caiyb_id,haoybh," +
						 "shangmkssj,shangmjssj,zhuangt,lury,banzb_id,beiz,haozt) "+//,meicb_id,meizb_id,bianm
						"values(getNewID(" + Diancxxb_id + ")," + zzb + ",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+
						",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss'),"+""+meil+","+visit.getDiancxxb_id()+","//bianm+
						+ruls.getString("id")+",0"+","+caiy_id+",'"+riq_hy+haoy_end+"',"
						+"to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+",to_date('"+shangmjssj+"','YYYY-MM-DD hh24:mi:ss')"
						+",'"+zhuangt+"','"+lury+"',"
//						+(getExtGrid().getColumn("meicb_id").combo).getBeanId(mdrsl.getString("meicb_id"))+","+
//						(getExtGrid().getColumn("meizb_id").combo).getBeanId(mdrsl.getString("meizb_id"))+","
						+(getExtGrid().getColumn("banzb_id").combo).getBeanId(mdrsl.getString("banzb_id"))+",'"+beiz+"',0)";
						sql2.append(spl).append(" ;\n");
						
					}
					sql2.append("end;");
					
					con.getInsert(sql2.toString());
					
					String ssql="select id  from rulmzlzb where rulmzlb_id="+zzb;
					ResultSetList rsls = con.getResultSetList(ssql);
					while(rsls.next()){
						String ssql1="select id from zhuanmlb where mingc='采样编码'";
						String ssql2="select id from zhuanmlb where mingc='制样编码'";
						String ssql3="select id from zhuanmlb where mingc='化验编码'";
						ResultSetList rsls1 = con.getResultSetList(ssql1);
						ResultSetList rsls2 = con.getResultSetList(ssql2);
						ResultSetList rsls3 = con.getResultSetList(ssql3);
						String zhillsb_id=	rsls.getString("id");
						
						while(rsls1.next()){
//							sql.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
//							"values(getNewID(" + Diancxxb_id + "),"+zhillsb_id+",'"+bianm+"',"+rsls1.getString("id")+");\n"
//							);
//							人员关联表
//							sql.append(" insert into RULCYYGLB (RULMZLZB_ID,renyxxb_id) values ("+zhillsb_id+","+mingc+")").append(";\n");
//							System.out.println(" zhuanmb"+sql.toString());
							// 此处判断，找个时间段内的采样表有没有数据，如果有且只有一条，才能读取数据并且赋值给caiyb_id，
//							假如有多条数据，说明有脏数据，如果没有那么才能插入数据。
							// 判断14:00-明天14：00
							
//							String cun_shangm=
//								"select * from rulmzlzb  where shangmkssj <=to_date('"+rulrq+"', 'YYYY-MM-DD hh24:mi:ss')\n" +
//							 	"       and shangmjssj>=to_date('"+rulrq+"', 'YYYY-MM-DD hh24:mi:ss') and id<> "+zhillsb_id;
//
//							ResultSetList sql_shangm = con.getResultSetList(cun_shangm);
//							while(sql_shangm.next()){
//								//取到数据，说明有重复的，那就给出提示信息，
//								
//								setMsg("这个时间已经存在采样信息");
//								
//								return ;
//								
//							}
							
							String sql_14=
								"select c.id,r.caiyb_id\n" +
								"  from caiyb c, rulmzlzb r\n" + 
								" where c.zhilb_id = r.id\n" + 
								"   and r.shangmkssj between\n" + 
								"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss') and\n" + 
								"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss') +1";

							ResultSetList rsls_14 = con.getResultSetList(sql_14);
							
							while (rsls_14.next()){
								//取出采样id
								caiy_id=rsls_14.getString("id");
							}
							
							
//							if(rsls_14.getRows()>0){
								
								//如果有数据，在上一步中已经取了数据，没有数据那么走else来生成采样信息
								
//							}else{
//							
//								String sr=" insert into caiyb (id,zhilb_id,cunywzb_id,bianm,caiyrq,xuh) values" +
//								"("+caiy_id+","+zhillsb_id+",0,'"+bianm+""+"',to_date('"
//								  +rulrq+"','YYYY-MM-DD hh24:mi:ss'),0"+");\n";
//								
//								String sql_y = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id) "+
//								"values(getnewid(" + Diancxxb_id + ")," + caiy_id + "," + zhillsb_id + 
//								",'" + zhuanmsz + "','" + leib + "'," + bumid + "," + leibid + ");\n";
//								sql.append(sql_y);
//								sql.append(sr);
//							}
							//插入到采样表
							//读取采样表id
//							String ql="select id from caiyb where  zhilb_id="+zhillsb_id;
//							ResultSetList rsq = con.getResultSetList(ql);
//							while (rsq.next()){
//								String caiyb_id=rsq.getString("id");
//								
////							写入样品单号表
//								flag = InsYangpdhb(con, Diancxxb_id, 0, caiyb_id, 
//										zhillsb_id, leibid, bumid, zhuanmsz, leib);
////							}
//							}
						}
						
//						 制样信息
						while(rsls2.next()){
							

							
							boolean flag2= false;
							
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

						
							
						
//							sql.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
//									"values(getNewID(" + Diancxxb_id + "),"+zhillsb_id+",'"+bianm+"',"+rsls2.getString("id")+");\n"
//									);
							
						}
						
//						 化验信息
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

						
							
						
//							sql.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
//									"values(getNewID(" + Diancxxb_id + "),"+zhillsb_id+",'"+bianm+"',"+rsls3.getString("id")+");\n"
//									);
							
							
						}
					}

				 sql.append("end;");
				 if(sql.length()>13){
						
						con.getInsert(sql.toString());
					}
			} else {

					tableName = "rulmzlzb";
					String s="select id from renyxxb where quanc= '"+mdrsl.getString("CAIYY")+"'";
					String id="";
					ResultSetList rsq = con.getResultSetList(s);
					while(rsq.next()){
						id=rsq.getString("id");
					}
					// 逻辑概述，修改的时候，也要判断时间，假如 ，上煤开始时间改变为已经存在的范围内，会有提示信息，
//					并且上煤开始时间小于等于上煤结束时间，

					StringBuffer sql2 = new StringBuffer("begin \n");
					sql2.append("update ").append(tableName).append(" set rulrq=to_date('")
					.append(mdrsl.getString("RULRQ")).append("','YYYY-MM-DD hh24:mi:ss')");
					sql2.append(",lury='").append(mdrsl.getString("CAIYY")).append("'");
					sql2.append(",meil=").append(mdrsl.getString("MEIL")).append(",shangmkssj=to_date('")
					.append(mdrsl.getString("RULRQ")).append("','YYYY-MM-DD hh24:mi:ss')").append(",shangmjssj=to_date('")
					.append(mdrsl.getString("shangmjssj")).append("','YYYY-MM-DD hh24:mi:ss')").append(",zhuangt='"+mdrsl.getString("zhuangt"))
					.append("'")
//					.append(",meicb_id="+(getExtGrid().getColumn("meicb_id").combo).getBeanId(mdrsl.getString("meicb_id")))
//					.append(",meizb_id="+(getExtGrid().getColumn("meizb_id").combo).getBeanId(mdrsl.getString("meizb_id")))
					.append(",banzb_id="+(getExtGrid().getColumn("banzb_id").combo).getBeanId(mdrsl.getString("banzb_id"))).append(",beiz='"+mdrsl.getString("beiz"))
					.append("'").append("").append("\n");
					//,bianm=' mdrsl.getString("BIANM")+"'"
					sql2.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
					
//					sql2.append("update zhuanmb set bianm='").append(mdrsl.getString("BIANM")).append("' where zhillsb_id=").append(mdrsl.getString("ID")).append(";\n");
//					sql2.append(" update RULCYYGLB set RENYXXB_ID="+id).append(";\n");
					sql2.append("end;");
					con.getUpdate(sql2.toString());
//				} 
		   }
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
	
	public String bM(){
		String a="";
		
		return a;
	}
	
//	public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
//			long meikxxb_id, long jihkjb_id, long yunsfsb_id, long chezxxb_id, 
//			String bianm,String caiyy,String caiysj,String caiyml,String leib) {
//		String sql ="";
//		ResultSetList rsl = null;
//		String caiyb_id = null;
//		int flag ;
////		判断此质量ID是否存在 如果存在则返回新建编号成功
//		sql = "select * from zhuanmb where zhillsb_id in (select id from zhillsb"+
//		" where zhilb_id in(" + zhilb_id+") and caiysj is null)";
//		rsl = con.getResultSetList(sql);
//		if(rsl.getRows()>0){
//			rsl.close();
//			return 0;
//		}
////		获得采样表ID
//		sql = "select * from caiyb where zhilb_id = " + zhilb_id;
//		rsl = con.getResultSetList(sql);
//		if (rsl.next()) {
//			caiyb_id = rsl.getString("id");
//		}
//		rsl.close();
////		查看供应商对应的采样定义
//		sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhdyb c, fenkcyfsb f, bumb b, leibb l " +
//				"where c.caizhfsb_id = f.caizhfsb_id\n" +
//				" and c.bum_id = b.id\n" +
//				" and c.leibb_id = l.id\n" +
//				" and f.diancxxb_id = " + diancxxb_id + "\n" +
//				" and f.meikxxb_id = " + meikxxb_id + "\n" +
//				" and f.yunsfsb_id = " + yunsfsb_id + "\n" +
//				" and f.chezxxb_id = " + chezxxb_id + "\n" +
//				" and f.jihkjb_id = " + jihkjb_id + "\n" ;
//		rsl = con.getResultSetList(sql);
//		
//		if(rsl.getRows() == 0){
////			如果系统没有设置 则取得系统默认设置
//			sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
//			"where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
//			" and c.leibb_id = l.id\n" +
//			" and f.mingc = '默认' and f.diancxxb_id =" + diancxxb_id;
//			
//			rsl.close();
//			rsl = con.getResultSetList(sql);
//			if(rsl.getRows() == 0){
////				如果默认设置未设置 则返回错误
//				return -1;
//			}
//		}
//		while(rsl.next()){
////			新建质量临时ID
//			String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
////			转码类别名称
////			String leib = rsl.getString("HUAYLB");//rsl.getString("leib");
////			转码编码
//			String zhuanmsz = rsl.getString("zhuanmsz");
////			化验类别ID
//			String leibid = rsl.getString("lbid");
////			部门ID
//			String bumid = rsl.getString("bmid");
////			是否转码
//			boolean shifzm = rsl.getInt("shifzm") == 1;
//			String huaylb=rsl.getString("HUAYLB");
////			插入到采样表(可能不需要)
//			String sr=" insert into caiyb (id,zhilb_id,cunywzb_id,bianm,caiyrq,xuh) values" +
//					"(getnewid("+diancxxb_id+"),"+zhillsb_id+",0,'"+caiysj.replaceAll("-", "").substring(0,10)+"00"+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),0"+")";
////			con.getInsert(sr);
////			写入样品单号表
//			flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id, 
//							zhillsb_id, leibid, bumid, zhuanmsz, leib);
//			if(flag == -1){
////				如果出错则返回错误
//				return -1;
//			}
////			写入质量临时表
//			InsZhillsb(con, diancxxb_id, zhilb_id, 
//					zhillsb_id, leibid, bumid, leib,caiyy,caiysj,caiyml);//,leix
//			if(flag == -1){
////				如果出错则返回错误
//				return -1;
//			}
//			if(bianm != null){
////				如果 手录编码==采样表中编码 则不使用转码设置
////				如果非自动生成编码			
//				zhuanmsz = "";
//				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, false, zhuanmsz,caiysj);
//				if(flag == -1){
////					如果出错则返回错误
//					return -1;
//				}
//			}else{
////				判断如果是自动生成编码
//				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, shifzm, zhuanmsz,caiysj);
//				if(flag == -1){
////					如果出错则返回错误
//					return -1;
//				}
//			}
//		}
//		rsl.close();
//		return 0;
//	}
	
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
			Save();
			getSelectData();
		}
		if (_Save1Chick) {
			_Save1Chick = false;
			Save1();
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

		String tableName = "rulmzlzmxb";
		JDBCcon con = new JDBCcon(); 

        StringBuffer sb_e=new StringBuffer();
        sb_e.append("begin \n");
        
		ResultSetList delrsl = visit.getExtGrid2()
				.getDeleteResultSet(getChange());
		while (delrsl.next()) {
			//删除
			sb_e.append(" delete from ").append(tableName).append(" where id="+delrsl.getString(0)).append(";\n");
			
			
		}
		sb_e.append("end;\n");
		 if(sb_e.length()>13){
				
				con.getInsert(sb_e.toString());
			}
		 sb_e.setLength(0);
		ResultSetList mdrsl = visit.getExtGrid2().getModifyResultSet(getChange());
		
		long Diancxxb_id=0;
		
		if (visit.isFencb()) {
			
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			
			Diancxxb_id = visit.getDiancxxb_id();
		}
		//初始化煤量
		int meil=0;
		int meil_hz=0;
		
		String sqlbm = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
		"where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
		" and c.leibb_id = l.id\n" +
		" and f.mingc = '默认' and f.diancxxb_id =" + Diancxxb_id;
		
		ResultSetList r = con.getResultSetList(sqlbm);
		String leib ="";
		String zhuanmsz="";
		String leibid="";
		boolean shifzm;
		String bumid="";
		String leix="";
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
			shifzm= r.getInt("shifzm") == 1;
			leix=r.getString("leix");
		}
		while (mdrsl.next()) {
			sb_e.append("begin \n");
				meil=mdrsl.getInt("MEIL");
//				meil_hz+=meil;
				String zzb=MainGlobal.getNewID(Diancxxb_id);
				long bin2=Integer.parseInt(getChangerq().substring(0, 10).replaceAll("-", ""));
				Date d=new Date(bin2);
				String caiysj=String.valueOf(d.getTime());//getChangerq();
				if(Integer.parseInt(getChangerq().substring(11, 13))>=13){
					caiysj=String.valueOf(d.getTime()+1);
				}
				String rulrq=getChangerq();
				String meiz=mdrsl.getString("MEIZMC");
				String meic=mdrsl.getString("MEICMC");
				String meid=mdrsl.getString("MEIDMC");
				String yanggh=mdrsl.getString("YANGGH");
				String sd=" select * from rulmzlzmxb where id="+getChangeid();
				String sds=" select * from rulmzlzb where id="+getChangeid();
				ResultSetList rsd = con.getResultSetList(sds);
				String rulid="0";
				String rulbzb_id="0";
				while(rsd.next()){
					rulid=rsd.getString("rulmzlb_id");
				}
			if ("0".equals(mdrsl.getString("ID"))) {
				//新增加的信息
				sb_e.append(" insert into ").append(tableName)//MEIZBH,MEICBH,
				.append(" ( id,RULMZLZB_ID,MIAOS,MEIZMC,MEIL,MEICMC,MEIDMC,JIZLH,JIZLMS,MEICH,MEICMS,CAIYJBH,CAIYJMS,BEIZ,rulrq,diancxxb_id,shenhzt,rulmzlb_id,rulbzb_id,shangmkssj,yanggh,zhilb_id)")
				.append(" values (" + zzb + ",").append(getChangeid()+",'").append(mdrsl.getString("MIAOS")+"','")
//				.append(mdrsl.getString("MEIZBH")+"','")
				.append(mdrsl.getString("MEIZMC")+"',")
//				.append(mdrsl.getString("MEICBH")+"',")
				.append(meil+",'").append(mdrsl.getString("MEICMC")+"','").append(mdrsl.getString("MEIDMC")+"','")
				.append(mdrsl.getString("JIZLH")+"','").append(mdrsl.getString("JIZLMS")+"','").append(mdrsl.getString("MEICH")+"','")
				.append(mdrsl.getString("MEICMS")+"','").append(mdrsl.getString("CAIYJBH")+"','").append(mdrsl.getString("CAIYJMS")+"','")
				.append(mdrsl.getString("BEIZ")+"'"+ ",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss'),"+Diancxxb_id+",0 ,"+rulid+","+rulbzb_id+",to_date('"+getChangerq()+"','YYYY-MM-DD hh24:mi:ss')"+",'"+yanggh+"',"+zzb+");\n")
				;
				
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
					
//					bianm=mdrsl.getString("BIANM");
				}

				String ssql="select id  from rulmzlzmxb where id="+zzb;
				ResultSetList rsls = con.getResultSetList(ssql);
					String ssql1="select id from zhuanmlb where mingc='采样编码'";
					String ssql2="select id from zhuanmlb where mingc='制样编码'";
					String ssql3="select id from zhuanmlb where mingc='化验编码'";
					ResultSetList rsls1 = con.getResultSetList(ssql1);
					ResultSetList rsls2 = con.getResultSetList(ssql2);
					ResultSetList rsls3 = con.getResultSetList(ssql3);
					String zhillsb_id=	getChangeid();//入炉煤质量子表
					String caiy_id="";
					String span="";
					caiy_id= MainGlobal.getNewID(Diancxxb_id);
					if(Integer.parseInt(rulrq.substring(11, 13))>=13){
						span="       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss') and\n" + 
						"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')+1  and " +
						" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
					}else{
						span="       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')-1 and\n" + 
						"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')  and " +
						" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
					}
						String sql_14=
							"select distinct c.zhilb_id,rb.caiyb_id\n" +
							"  from caiyb c, rulmzlzmxb rb \n" + 
							" where c.zhilb_id = rb.zhilb_id \n" + 
							"   and rb.rulrq  between\n" + span;
//							"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')-1 and\n" + 
//							"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')  and " +
//									" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";

						ResultSetList rsls_14 = con.getResultSetList(sql_14);
						 boolean flag=false;
						 String zhilb_id=zzb;
						while (rsls_14.next()){
							//取出采样id
							caiy_id=rsls_14.getString("caiyb_id");
							zhilb_id=rsls_14.getString("zhilb_id");
							flag=true;
						}
						
						
						if(flag){
							sb_e.append(" update rulmzlzmxb set zhilb_id="+zhilb_id+",caiyb_id="+caiy_id+" where id="+zzb).append(";\n");
							//如果有数据，在上一步中已经取了数据，没有数据那么走else来生成采样信息
							
						}else{
							while(rsls1.next()){
								sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
										"values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls1.getString("id")+""+");\n"
								);
//						人员关联表//入炉煤质量子明细
								sb_e.append(" insert into RULCYYGLB (RULMZLZB_ID,renyxxb_id) values ("+zzb+",'"+"')").append(";\n");
//						System.out.println(" zhuanmb"+sql.toString());
								// 此处判断，找个时间段内的采样表有没有数据，如果有且只有一条，才能读取数据并且赋值给caiyb_id，
//						假如有多条数据，说明有脏数据，如果没有那么才能插入数据。
								// 判断14:00-明天14：00

//							
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
							sb_e.append(" update rulmzlzmxb set caiyb_id="+caiy_id+" where id="+zzb).append(";\n");
						//插入到采样表
						//读取采样表id
//						String ql="select id from caiyb where  zhilb_id="+zhillsb_id;
//						ResultSetList rsq = con.getResultSetList(ql);
//						while (rsq.next()){
//							String caiyb_id=rsq.getString("id");
//							
////						写入样品单号表
//							flag = InsYangpdhb(con, Diancxxb_id, 0, caiyb_id, 
//									zhillsb_id, leibid, bumid, zhuanmsz, leib);
////						}
//						}
					
					
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

						sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
								"values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls2.getString("id")+""+");\n"
								);
						
					
//						sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
//								"values(getNewID(" + Diancxxb_id + "),"+zhillsb_id+",'"+bianm+"',"+rsls2.getString("id")+");\n"
//								);
						
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
					
//						sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
//								"values(getNewID(" + Diancxxb_id + "),"+zhillsb_id+",'"+bianm+"',"+rsls3.getString("id")+");\n"
//								);
//						
						
					}
						}
//				}
				
				}else{
					//原则是先把原有删除，然后再进行插入
					//修改的信息
					//放在另一个事务里处理
					StringBuffer sb_l=new StringBuffer("begin \n");
					sb_l.append(" delete from rulmzlzmxb where id="+mdrsl.getString("ID")+";\n");
					//删除必要的表的信息
					
//					sb_l.append(" delete from zhuanmb where zhillsb_id="+mdrsl.getString("ID")+";\n");
//					
//					sb_l.append(" delete from RULCYYGLB where RULMZLZB_ID="+mdrsl.getString("ID")+";\n");
//					
//					sb_l.append(" delete from yangpdhb where zhilblsb_id="+mdrsl.getString("ID")+";\n");
					
//					sb_l.append(" delete from caiyb where zhilb_id="+mdrsl.getString("ID")+";\n");
					sb_l.append("end;\n");
					
					//读取原有记录信息
					String a=" select * from rulmzlzmxb where id<>"+mdrsl.getString("ID")+" and caiyb_id in ( select caiyb_id from rulmzlzmxb where id="+mdrsl.getString("ID")+")";
					ResultSetList rsl_a = con.getResultSetList(a);
					
					if(sb_l.length()>13){
						
						con.getUpdate(sb_l.toString());
						
					}
					

					//新增加的信息
					sb_e.append(" insert into ").append(tableName)//MEIZBH,MEICBH,
					.append(" ( id,RULMZLZB_ID,MIAOS,MEIZMC,MEIL,MEICMC,MEIDMC,JIZLH,JIZLMS,MEICH,MEICMS,CAIYJBH,CAIYJMS,BEIZ,rulrq,diancxxb_id,shenhzt,rulmzlb_id,rulbzb_id,shangmkssj,yanggh,zhilb_id)")
					.append(" values (" + zzb + ",").append(getChangeid()+",'").append(mdrsl.getString("MIAOS")+"','")
//					.append(mdrsl.getString("MEIZBH")+"','")
					.append(mdrsl.getString("MEIZMC")+"',")
//					.append(mdrsl.getString("MEICBH")+"',")
					.append(meil+",'").append(mdrsl.getString("MEICMC")+"','").append(mdrsl.getString("MEIDMC")+"','")
					.append(mdrsl.getString("JIZLH")+"','").append(mdrsl.getString("JIZLMS")+"','").append(mdrsl.getString("MEICH")+"','")
					.append(mdrsl.getString("MEICMS")+"','").append(mdrsl.getString("CAIYJBH")+"','").append(mdrsl.getString("CAIYJMS")+"','")
					.append(mdrsl.getString("BEIZ")+"'"+ ",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss'),"+Diancxxb_id+",0 ,"+rulid+","+rulbzb_id+",to_date('"+getChangerq()+"','YYYY-MM-DD hh24:mi:ss')"+",'"+yanggh+"',"+zzb+");\n")
					;
					
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
						
//						bianm=mdrsl.getString("BIANM");
					}

					String ssql="select id  from rulmzlzmxb where id="+zzb;
					ResultSetList rsls = con.getResultSetList(ssql);
//					String ss=" select id from renyxxb where quanc='"+mdrsl.getString("CAIYY")+"'";
//					ResultSetList r1 = con.getResultSetList(ss);
//					String mingc="";
//					while(r1.next()){
//						mingc=r1.getString("id");
//						
//					}
//					while(rsls.next()){
						String ssql1="select id from zhuanmlb where mingc='采样编码'";
						String ssql2="select id from zhuanmlb where mingc='制样编码'";
						String ssql3="select id from zhuanmlb where mingc='化验编码'";
						ResultSetList rsls1 = con.getResultSetList(ssql1);
						ResultSetList rsls2 = con.getResultSetList(ssql2);
						ResultSetList rsls3 = con.getResultSetList(ssql3);
						String zhillsb_id=	getChangeid();//入炉煤质量子表
						String caiy_id="";
						String span="";
						caiy_id= MainGlobal.getNewID(Diancxxb_id);
						if(Integer.parseInt(rulrq.substring(11, 13))>=13){
							span="       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss') and\n" + 
							"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')+1  and " +
							" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
						}else{
							span="       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')-1 and\n" + 
							"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')  and " +
							" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
						}
							String sql_14=
								"select c.id,rb.caiyb_id,rb.zhilb_id\n" +
								"  from caiyb c, rulmzlzmxb rb \n" + 
								" where c.id = rb.caiyb_id \n" + 
								"   and rb.rulrq  between\n" + span;
//								"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')-1 and\n" + 
//								"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss')  and " +
//										" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";

							ResultSetList rsls_14 = con.getResultSetList(sql_14);
							String zhilb_id=zzb;
							 boolean flag=false;
							while (rsls_14.next()){
								//取出采样id
								caiy_id=rsls_14.getString("id");
								zhilb_id=rsls_14.getString("zhilb_id");
								flag=true;
							}
							
							
							if(flag){
								sb_e.append(" update rulmzlzmxb set zhilb_id="+zhilb_id+" caiyb_id="+caiy_id+" where id="+zzb).append(";\n");
								//如果有数据，在上一步中已经取了数据，没有数据那么走else来生成采样信息
								
							}else{
								while(rsls1.next()){
									sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
											"values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls1.getString("id")+""+");\n"
									);
//							人员关联表//入炉煤质量子明细
									sb_e.append(" insert into RULCYYGLB (RULMZLZB_ID,renyxxb_id) values ("+zzb+",'"+0+"')").append(";\n");
//							System.out.println(" zhuanmb"+sql.toString());
									// 此处判断，找个时间段内的采样表有没有数据，如果有且只有一条，才能读取数据并且赋值给caiyb_id，
//							假如有多条数据，说明有脏数据，如果没有那么才能插入数据。
									// 判断14:00-明天14：00
									
//							String cun_shangm=
//								"select * from rulmzlzmxb  where shangmkssj <=to_date('"+rulrq+"', 'YYYY-MM-DD hh24:mi:ss')\n" +
//							 	"       and shangmjssj>=to_date('"+rulrq+"', 'YYYY-MM-DD hh24:mi:ss') and id<> "+zhillsb_id;
	//
//							ResultSetList sql_shangm = con.getResultSetList(cun_shangm);
//							while(sql_shangm.next()){
//								//取到数据，说明有重复的，那就给出提示信息，
//								
//								setMsg("这个时间已经存在采样信息");
//								
//								return ;
//								
//							}
									
									//判断同一煤场同一煤堆同一煤种
//							String sql_12=
//								"select c.id,rb.caiyb_id\n" +
//								"  from caiyb c, rulmzlzb r,rulmzlzmxb rb \n" + 
//								" where c.zhilb_id = r.id and r.id=rb.rulmzlzb_id\n" + 
//								"   and r.shangmkssj between\n" + 
//								"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss') and\n" + 
//								"       to_date('"+rulrq.substring(0, 10)+" 13:00:00', 'YYYY-MM-DD hh24:mi:ss') +1";
	//
//							ResultSetList rsls_12 = con.getResultSetList(sql_12);
//							if(){
//								
								}
							
								String sr=" insert into caiyb (id,zhilb_id,cunywzb_id,bianm,caiyrq,xuh) values" +
								"("+caiy_id+","+zzb+",0,'"+bianm+""+"',to_date('"
								  +rulrq+"','YYYY-MM-DD hh24:mi:ss'),0"+");\n";
								
								String sql_y = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id) "+
								"values(getnewid(" + Diancxxb_id + ")," + caiy_id + "," + zzb + 
								",'" + zhuanmsz + "','" + leib + "'," + bumid + "," + leibid + ");\n";
								sb_e.append(sql_y);
								sb_e.append(sr);
//							}
								sb_e.append(" update rulmzlzmxb set caiyb_id="+caiy_id+" where id="+zzb).append(";\n");
							//插入到采样表
							//读取采样表id
//							String ql="select id from caiyb where  zhilb_id="+zhillsb_id;
//							ResultSetList rsq = con.getResultSetList(ql);
//							while (rsq.next()){
//								String caiyb_id=rsq.getString("id");
//								
////							写入样品单号表
//								flag = InsYangpdhb(con, Diancxxb_id, 0, caiyb_id, 
//										zhillsb_id, leibid, bumid, zhuanmsz, leib);
////							}
//							}
						
						
//						 制样信息
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

							sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
									"values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls2.getString("id")+""+");\n"
									);
							
						
//							sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
//									"values(getNewID(" + Diancxxb_id + "),"+zhillsb_id+",'"+bianm+"',"+rsls2.getString("id")+");\n"
//									);
							
						}
						
//						 化验信息
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
						
//							sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
//									"values(getNewID(" + Diancxxb_id + "),"+zhillsb_id+",'"+bianm+"',"+rsls3.getString("id")+");\n"
//									);
//							
							
						}
							}
//					}
					

							while(rsl_a.next()){
								
								// 查找采样表
								sb_e.append(" update rulmzlzmxb set caiyb_id="+caiy_id+" where id="+rsl_a.getString("id")+";\n");
							}

				}
			sb_e.append("end;\n");
			System.out.println("sb_e.toString()\n"+sb_e.toString());
			if(sb_e.length()>13){
				
				con.getInsert(sb_e.toString());
				
			}
			sb_e.setLength(0);
			} 
		
		

		

		
	
		
		 ResultSetList rsl = con.getResultSetList(" select meil from rulmzlzmxb where rulmzlzb_id="+getChangeid());
		 while(rsl.next()){
			 meil_hz+=rsl.getInt("meil");
		 }
		 sb_e.setLength(0);
		 sb_e.append(" update rulmzlzb set meil="+meil_hz+" where id="+getChangeid()).append("");
		 if(sb_e.length()>13){
				
				con.getUpdate(sb_e.toString());
			}
	}
	
	
		
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
        String ssql=
        	"select rb.id,\n" +
        	"       to_char(rb.rulrq,'YYYY-MM-DD hh24:mi:ss') as rulrq,\n" + 
        	"       rb.haoybh,\n" + 
        	"       rb.meil,--b.bianm,\n" + 
        	"       to_char(rb.shangmkssj,'YYYY-MM-DD hh24:mi:ss') as shangmkssj,\n" + 
        	"      to_char(rb.shangmjssj,'YYYY-MM-DD hh24:mi:ss') as  shangmjssj,\n" + 
        	"       rb.zhuangt,\n" + 
        	"       rb.lury as caiyy,\n" + 
//        	"       mc.mingc meicb_id,\n" + 
//        	"       mz.mingc meizb_id,\n" + 
        	"       bz.mingc banzb_id,\n" + 
        	"       rb.beiz \n" + 
        	"  from rulmzlzb rb, pinzb mz,meicb mc,rulbzb bz  \n"+//RULCYYGLB glb,renyxxb ren,,zhuanmb b, zhuanmlb l,
        	"  where rb.haoybh ='"+getYundxxValue().getValue()+"'"+
//			" and b.zhillsb_id=rb.id and b.zhuanmlb_id=l.id and l.jib=1  \n" +//and rb.id= glb.RULMZLZB_ID
			" and rb.meicb_id=mc.id(+) and rb.meizb_id=mz.id(+) and rb.banzb_id=bz.id and rb.haozt=0";//and glb.RENYXXB_ID=ren.id 
		ResultSetList rsl1 = con.getResultSetList(ssql);
		String id="";
        while(rsl1.next()){
        	id=rsl1.getString("id");
        }
        //记录记录集的条数
        int jil=0;
        if(rsl1.getRows()>0){
        	jil=rsl1.getRows();
        }
        //查出数据后进行记录集重置
        rsl1.beforefirst();
        

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
		
		egu.getColumn("rulrq").setHidden(true);
		egu.getColumn("rulrq").setWidth(60);
		egu.getColumn("rulrq").editor = null;
		
		egu.getColumn("haoybh").setHeader("耗用编号");//Center
		egu.getColumn("haoybh").setWidth(70);
		egu.getColumn("haoybh").editor = null;
		egu.getColumn("meil").setHeader("煤量");//Center
		egu.getColumn("meil").setWidth(70);
		egu.getColumn("meil").setEditor(null);
		
//		egu.getColumn("bianm").setHeader("编码");//Center
//		egu.getColumn("bianm").setWidth(70);
		if(visit.getboolean4()){//手动
			
		}else{//自动
			
//			egu.getColumn("bianm").setHidden(true);
//			egu.getColumn("bianm").editor = null;
			
		}
		
		
		egu.getColumn("shangmkssj").setHeader("上煤开始时间");//Center
		egu.getColumn("shangmkssj").setWidth(130);
		egu.getColumn("shangmjssj").setHeader("上煤结束时间");//Center
		egu.getColumn("shangmjssj").setWidth(130);
		egu.getColumn("zhuangt").setHeader("状态");//Center
		egu.getColumn("zhuangt").setWidth(70);
		
		egu.getColumn("caiyy").setHeader("登记员");//Center
		egu.getColumn("caiyy").setWidth(70);
		egu.getColumn("caiyy").setDefaultValue(visit.getRenymc());
		egu.getColumn("caiyy").setEditor(null);
//		egu.getColumn("meicb_id").setHeader("煤场");//Center
//		egu.getColumn("meicb_id").setWidth(70);
//		egu.getColumn("meizb_id").setHeader("煤种");//Center
//		egu.getColumn("meizb_id").setWidth(70);
		egu.getColumn("banzb_id").setHeader("班组");//Center
		egu.getColumn("banzb_id").setWidth(70);
		
		egu.getColumn("beiz").setHeader("备注");//Center
		egu.getColumn("beiz").setWidth(70);
		

		DatetimeField datetime = new DatetimeField();
		egu.getColumn("shangmjssj").setEditor(datetime);
		egu.getColumn("shangmjssj").setDefaultValue("");
		egu.getColumn("shangmjssj").editor.setAllowBlank(true);
		DatetimeField datet = new DatetimeField();
		egu.getColumn("shangmkssj").setEditor(datet);
		egu.getColumn("shangmkssj").setDefaultValue("");
		
//		班组id
		ComboBox c9 = new ComboBox();
		egu.getColumn("banzb_id").setEditor(c9);
		c9.setEditable(true);
		String MeikbSql="select id,mingc from rulbzb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
		egu.getColumn("banzb_id").setComboEditor(egu.gridId, new IDropDownModel(MeikbSql));
		egu.getColumn("banzb_id").setReturnId(true);
		
		
//		煤场id
//		ComboBox c8 = new ComboBox();
//		egu.getColumn("meicb_id").setEditor(c8);
//		c8.setEditable(true);
//		String MeicbSql="select id,mingc from meicb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
//		egu.getColumn("meicb_id").setComboEditor(egu.gridId, new IDropDownModel(MeicbSql));
//		egu.getColumn("meicb_id").setReturnId(true);
		
//		煤种id
//		ComboBox c7 = new ComboBox();
//		egu.getColumn("meizb_id").setEditor(c7);
//		c7.setEditable(true);
//		String MeizbSql="select id,mingc from pinzb  "+"  order by mingc";
//		egu.getColumn("meizb_id").setComboEditor(egu.gridId, new IDropDownModel(MeizbSql));
//		egu.getColumn("meizb_id").setReturnId(true);
		
		egu.addTbarText("耗用日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());
		

		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("耗用编号:");
		ComboBox comb = new ComboBox();
		comb.setTransform("YundxxDropDown");
		comb.setId("Yundxx");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(100);
		comb.setListWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Yundxx.on('select',function(){document.forms[0].submit();});");


		

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		String codt_in=" var Mrcd1 = gridDiv_ds.getModifiedRecords();"
			
			+"if(Mrcd1.length==1){Ext.MessageBox.alert('提示信息','一个时间段只能有一个耗用信息！'); return;}";
		
		String codt=" var Mrcd1 = gridDiv_ds.getModifiedRecords();"
			
			+"if(Mrcd1.length>1){Ext.MessageBox.alert('提示信息','一个时间段只能有一个耗用信息！'); return;}";
		
		if(jil==0){
			egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"",codt_in);//
		}else{
			
		}

		

		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",codt);
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		
		
		
		//构造第二个grid
		if(id==null||id.equals("")){
			id="-1";
		}
		String  q=
			"select xb.id ,\n" +
			"       xb.rulmzlzb_id rulmzlzb_id,\n" + 
			"       xb.miaos miaos,\n" + 
//			"       xb.meizbh,\n" + 
			"       xb.meizmc meizmc,\n" + 
//			"       xb.meicbh,\n" + 
			"       xb.meicmc meicmc,\n" + 
			"       xb.meidmc meidmc,\n" + 
			"       xb.meil,\n" + 
			"       xb.jizlh,\n" + 
			"       xb.jizlms,\n" + 
			"       xb.meich,\n" + 
			"       xb.meicms,\n" + 
			"       xb.caiyjbh,\n" + 
			"       xb.caiyjms,xb.yanggh, \n" + 
			"       xb.beiz \n" + 
			"  from rulmzlzmxb xb,rulmzlzb r  where xb.rulmzlzb_id=r.id and r.haoybh='"+getYundxxValue().getValue()+"' ";
		

		ResultSetList rsl2 = con.getResultSetList(q);
		ExtGridUtil egu1 = new ExtGridUtil("gridDiv1", rsl2);
		// 设置GRID是否可以编辑
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu1.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置每页显示行数
		egu1.addPaging(25);
		egu1.getColumn("id").setHidden(true);
		egu1.getColumn("id").setWidth(60);
		egu1.getColumn("rulmzlzb_id").setHeader("rulmzlzb_id");//Center
		egu1.getColumn("rulmzlzb_id").setHidden(true);
		egu1.getColumn("rulmzlzb_id").setWidth(60);
		egu1.getColumn("miaos").setHeader("描述");//Center
		egu1.getColumn("miaos").setWidth(120);
		egu1.getColumn("miaos").setHidden(true);
//		egu1.getColumn("meizbh").setHeader("煤种编号");//Center
//		egu1.getColumn("meizbh").setWidth(60);
		egu1.getColumn("meizmc").setHeader("煤种名称");//Center
		egu1.getColumn("meizmc").setWidth(60);
//		egu1.getColumn("meicbh").setHeader("煤场编号");//Center
//		egu1.getColumn("meicbh").setWidth(60);
		egu1.getColumn("meicmc").setHeader("煤场名称");//Center
		egu1.getColumn("meicmc").setWidth(120);
		
		egu1.getColumn("meidmc").setHeader("煤堆名称");//Center
		egu1.getColumn("meidmc").setWidth(120);
		egu1.getColumn("meil").setHeader("煤量");//Center
		egu1.getColumn("meil").setWidth(120);
		
		egu1.getColumn("jizlh").setHeader("机组炉号");//Center
		egu1.getColumn("jizlh").setWidth(120);
		egu1.getColumn("jizlms").setHeader("机组炉描述");//Center
		egu1.getColumn("jizlms").setWidth(120);
		egu1.getColumn("jizlms").setHidden(true);
		
//		egu1.getColumn("a").setHeader("a");//Center
//		egu1.getColumn("a").setWidth(120);
//		egu1.getColumn("a").setHidden(true);
//				egu1.getColumn("b").setHeader("b");//Center
//		egu1.getColumn("b").setWidth(120);
//		egu1.getColumn("b").setHidden(true);
//				egu1.getColumn("c").setHeader("c");//Center
//		egu1.getColumn("c").setWidth(120);
//		egu1.getColumn("c").setHidden(true);
//				egu1.getColumn("d").setHeader("d");//Center
//		egu1.getColumn("d").setWidth(120);
//		egu1.getColumn("d").setHidden(true);
		
		egu1.getColumn("meich").setHeader("煤仓号");//Centeryanggh
		egu1.getColumn("meich").setWidth(160);
		egu1.getColumn("meich").setEditor(null);
		egu1.getColumn("yanggh").setHeader("样罐号");//Centeryanggh
		egu1.getColumn("yanggh").setWidth(130);
		egu1.getColumn("yanggh").setEditor(null);
		egu1.getColumn("meicms").setHeader("煤仓描述");//Center
		egu1.getColumn("meicms").setWidth(120);
		egu1.getColumn("meicms").setHidden(true);
		
		egu1.getColumn("caiyjbh").setHeader("采样机编号");//Center
		egu1.getColumn("caiyjbh").setWidth(120);
		egu1.getColumn("caiyjms").setHeader("采样机描述");//Center
		egu1.getColumn("caiyjms").setWidth(120);
		egu1.getColumn("caiyjms").setHidden(true);
		egu1.getColumn("beiz").setHeader("备注");//Center
		egu1.getColumn("beiz").setWidth(120);

//		煤仓id
		ComboBox c6 = new ComboBox();
		egu1.getColumn("caiyjbh").setEditor(c6);
		c6.setEditable(true);
		String MeikbSql6="select id,  mingc  from caiyjb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by id";
		egu1.getColumn("caiyjbh").setComboEditor(egu1.gridId, new IDropDownModel(MeikbSql6));
		egu1.getColumn("caiyjbh").setReturnId(true);
//		egu1.addOtherScript("Yundxx.on('select',function(){document.forms[0].submit();});");
//		煤仓id
//		ComboBox c5 = new ComboBox();
//		egu1.getColumn("meich").setEditor(c5);
//		c5.setEditable(true);
//		String MeikbSql5="select id,  mingc  from meicangb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by id";
//		egu1.getColumn("meich").setComboEditor(egu1.gridId, new IDropDownModel(MeikbSql5));
//		egu1.getColumn("meich").setReturnId(true);
		
//		机组id
		ComboBox c1 = new ComboBox();
		egu1.getColumn("jizlh").setEditor(c1);
		c1.setEditable(true);
		String MeikbSql1="select id, jizbh as mingc  from jizb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by id";
		egu1.getColumn("jizlh").setComboEditor(egu1.gridId, new IDropDownModel(MeikbSql1));
		egu1.getColumn("jizlh").setReturnId(true);
		
		
//		煤场id
		ComboBox c2 = new ComboBox();
		egu1.getColumn("meicmc").setEditor(c2);
		c2.setEditable(true);
		String MeicbSql2="select id,mingc from meicb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
		egu1.getColumn("meicmc").setComboEditor(egu1.gridId, new IDropDownModel(MeicbSql2));
		egu1.getColumn("meicmc").setReturnId(true);
		
//		煤场id
		ComboBox c3 = new ComboBox();
		egu1.getColumn("meizmc").setEditor(c3);
		c3.setEditable(true);
		String MeizbSql3="select id,mingc from pinzb  "+"  order by mingc";
		egu1.getColumn("meizmc").setComboEditor(egu1.gridId, new IDropDownModel(MeizbSql3));
		egu1.getColumn("meizmc").setReturnId(true);
		
//		煤堆id
		ComboBox c4 = new ComboBox();
		egu1.getColumn("meidmc").setEditor(c4);
		c4.setEditable(true);
		String MeikbSql4="select id,  mingc  from duow WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
		egu1.getColumn("meidmc").setComboEditor(egu1.gridId, new IDropDownModel(MeikbSql4));
		egu1.getColumn("meidmc").setReturnId(true);
		
		
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
			"        rec = gridDiv1_grid.getSelectionModel().getSelected();\n"+
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
			"        rec.set('MEICH',tmp2);\n" + //meich
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			" win = new Ext.Window({\n" + 
			" title: '煤仓信息',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		
		
		String treepanel1 =
			"var navtreei = new Ext.tree.TreePanel({\n" +
			//"\t    title: '采样人员',\n" + 
			//"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTreei0,\n" + 
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
			"        var cs = navtreei.getChecked();\n" + 
			"        rec = gridDiv1_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win1.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('YANGGH',tmp2);\n" + //yanggh
			"        win1.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction1 = 

			" win1 = new Ext.Window({\n" + 
			" title: '样罐信息',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtreei]\n" + 
			" });\n";
		egu1.addOtherScript("gridDiv1_grid.on('cellclick',function(own,irow,icol, e){ "
				+ "row = irow; \n"
				+ "if('MEICH' == gridDiv1_grid.getColumnModel().getDataIndex(icol)){"
				+"if(!win){"+treepanel+Strtmpfunction+"}"
				+"win.show(this);}" 
				
				+ "if('YANGGH' == gridDiv1_grid.getColumnModel().getDataIndex(icol)){"
				+"if(!win1){"+treepanel1+Strtmpfunction1+"}"
				+"win1.show(this);}" +
						"});\n");
		egu1.addToolbarButton(GridButton.ButtonType_Delete, null);
		String condition = //gridDiv_grid.getSelectionModel().getSelections();
			""			+"  var Mrcdd = gridDiv1_ds.getModifiedRecords(); " +
//					"if(Mrcdd.length>1){ Ext.MessageBox.alert('提示信息','请选择保存一条信息'); return ; }"+

//			"if(Mrcdd.length>1){ Ext.MessageBox.alert('提示信息','请选择保存一条信息'); return ; }\n" +
			"  for(j = 0; j< Mrcdd.length; j++){\n" + 
			"  var a = Mrcdd[j].get('MEICMC');\n" + 
			"\n" + 
			"  var b =  Mrcdd[j].get('MEIDMC');\n" + 
			"\n" + 
			"  var c=b.substring(2,3);  var d =a.substring(0,1);"+
			"    if(d!=c){\n" + 
			"    Ext.MessageBox.alert('提示信息','煤场和煤堆不对应'); return ;\n" + 
			"    }\n" + 
			"  }"

//			+ "var Mrcd1 = gridDiv1_ds.getRange();\n"//gridDiv1_ds.getModifiedRecords();gridDiv_ds.getModifiedRecords()
//			+ "for(i = 0; i< Mrcd1.length; i++){ var b=0;var b1=0;var b2=0; \n"//meizmc
//			+ "var a= Mrcd1[i].get('MEIZMC');var a1= Mrcd1[i].get('MEICMC'); var a2= Mrcd1[i].get('MEIDMC');" +
//					"for(j = 0; j< Mrcd1.length; j++){" +
//					"if(a==Mrcd1[j].get('MEIZMC')){b=b+1; }" +"if(a1==Mrcd1[j].get('MEICMC')){b1=b1+1; }"+
//					 "if(a2==Mrcd1[j].get('MEIDMC')){b2=b2+1; }"+
//					"} if(b1>1&&b2>1&&b>1){Ext.MessageBox.alert('提示信息','同一煤场同一煤堆同一煤种不能重复'); return ; }"
//			+ "gridDiv_history += '<result>' + '<sign>U</sign>' +" +
//					" '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ " +
//					"'</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+" +
//					" '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + " +
//					"('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+" +
//					" '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' +" +
//					" Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ " +
//					"'<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<"+s.toUpperCase()+" update=\"true\">'" +
//							" + Mrcd[i].get('"+s.toUpperCase()+"')+ '</"+s.toUpperCase()+">'+ '<QBAD update=\"true\">'" +
//									" + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<HUAYY update=\"true)\">' " +
//									"+ Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+" +
//									" '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+" +
//									" '</BEIZ>'+ '<ZHIYRY update=\"true\">' + Mrcd[i].get('ZHIYRY').replace('<','&lt;').replace('>','&gt;')+ '</ZHIYRY>' + '</result>' ; \n"
//			+ "}\n"
			+ " var rec = gridDiv_sm.getSelected(); "
			+ " if(rec != null){var id = rec.get('ID');var rq = rec.get('SHANGMKSSJ');"//shangmkssj
			+ " var Cobjid = document.getElementById('CHANGEID');var Cobjrq = document.getElementById('CHANGERQ');"
			+ " Cobjid.value = id; Cobjrq.value = rq; if(id==0||id=='0'){Ext.MessageBox.alert('提示信息','请先保存第一组添加的记录'); return ; }}"
			+ " if(rec == null){"
			+ "  Ext.MessageBox.alert('提示信息','请选择一条第一组信息记录'); return ;"
			+ " }"+
			"";
		String condition1 = ""
//			""				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
//			+ "for(i = 0; i< Mrcd.length; i++){ var b=0; \n"//meizmc
//			+ "var a= Mrcd[i].get('MEIZMC');var a1= Mrcd[i].get('MEICMC'); var a2= Mrcd[i].get('MEIDMC');" +
//					"for(j = 0; j< Mrcd.length; j++){" +
//					"if(a==Mrcd[j].get('MEIZMC')){b=b+1; }" +"if(a1==Mrcd[j].get('MEICMC')){b=b+1; }"+
//					 "if(a2==Mrcd[j].get('MEIDMC')){b=b+1; }"+
//					"} if(b>=3){Ext.MessageBox.alert('提示信息','同一煤场同一煤堆同一煤种不能重复'); return ; }"
//			+ "gridDiv_history += '<result>' + '<sign>U</sign>' +" +
//					" '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ " +
//					"'</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+" +
//					" '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + " +
//					"('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+" +
//					" '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' +" +
//					" Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ " +
//					"'<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<"+s.toUpperCase()+" update=\"true\">'" +
//							" + Mrcd[i].get('"+s.toUpperCase()+"')+ '</"+s.toUpperCase()+">'+ '<QBAD update=\"true\">'" +
//									" + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<HUAYY update=\"true)\">' " +
//									"+ Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+" +
//									" '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+" +
//									" '</BEIZ>'+ '<ZHIYRY update=\"true\">' + Mrcd[i].get('ZHIYRY').replace('<','&lt;').replace('>','&gt;')+ '</ZHIYRY>' + '</result>' ; \n"
//			+ "}\n"
			+ " var rec = gridDiv_sm.getSelected(); "
			+ " if(rec != null){var id = rec.get('ID');var rq = rec.get('SHANGMKSSJ');"//shangmkssj
			+ " var Cobjid = document.getElementById('CHANGEID');var Cobjrq = document.getElementById('CHANGERQ');"
			+ " Cobjid.value = id; Cobjrq.value = rq; if(id==0||id=='0'){Ext.MessageBox.alert('提示信息','请先保存第一组添加的记录'); return ; }}"
			+ " if(rec == null){"
			+ "  Ext.MessageBox.alert('提示信息','请选择一条第一组信息记录'); return ;"
			+ " }"+
			"";
		egu1.addToolbarButton(GridButton.ButtonType_Insert, null);//,condition1
		egu1.addToolbarButton(GridButton.ButtonType_Save_condition, "Save1Button",condition);
		GridButton gb = new GridButton("入炉确认", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;document.getElementById('Save2Button').click();" +
						"}else{Ext.MessageBox.alert('提示信息','请选择一条记录');return; }"
				+

				"\t}"

				+ " "); 
		
		StringBuffer sbjs = new StringBuffer();

		sbjs.append("gridDiv1_grid.on('beforeedit',function(e){");

		// 设定某一行不能编辑
		sbjs
				.append("if(e.row==1||e.row==2"+"){gridDiv1_ds.getAt(1).get('MEICMC');}");//meicmc

		sbjs.append("});");
//		egu1.addOtherScript(sbjs.toString());
		egu.addTbarBtn(gb);
		egu1.defaultsortable=false;
		setExtGrid1(egu1);
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
	
	
	public String getNavigetion1(){
		return ((Visit)this.getPage().getVisit()).getString4();
	}
	
	public void setNavigetion1(String nav){
		((Visit)this.getPage().getVisit()).setString4(nav);
	}
	
	public void initNavigation1(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion1("");
		
//		导航栏树的查询SQL
		String sql=
			"select 0 id,'根' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select id,mingc as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from yanggb \n" + 
			" where\n" + 
			"diancxxb_id="+visit.getDiancxxb_id()+"\n" + ""
			;
			
		TreeOperation dt = new TreeOperation();
//		System.out.println(sql);
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTreei", node);
		setNavigetion1(treeNodeScript);
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
		
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
//		list.add(new IDropDownBean(1, "靠泊日期"));
//		list.add(new IDropDownBean(2, "到泊日期"));
//		list.add(new IDropDownBean(3, "离泊日期"));
		String sql=" select to_number(haoybh) id,haoybh mingc from rulmzlzb where rulrq = to_date('"
			+getRiq1()+"','yyyy-mm-dd')";//查询耗用编号，这个耗用编号来自入炉煤质量表
		
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql,"请选择"));
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
		
//		String sql="";
		
//		switch ((int) this.getWeizSelectValue().getId()) {
//		case 1:
//			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
//    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.kaobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
//    		"  and f.kaobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
//			break;// 靠泊日期
//		case 2:
//			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
//    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
//    		"  and f.daobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
//			break;// 到泊日期
//		case 3:
//			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
//    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daohrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
//    		"  and f.daohrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
//			break;// 离泊日期
//		}
		String sql=" select  id,haoybh mingc from rulmzlzb where haozt=0 and to_char(rulrq,'yyyy-mm-dd') ='"
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
//			setWeizSelectValue(null);
//			setWeizSelectModel(null);
			setYundxxModel(null);	
			getYundxxModels();		
			setTbmsg(null);
			initNavigation();
			initNavigation1();
			visit.setString1(null);
		}
		if(visit.getboolean1()){
			
			visit.setboolean1(false);
//			visit.setString9("");
			visit.setList1(null);
			setYundxxValue(null);		//4
			setYundxxModel(null);
//			setWeizSelectValue(null);
//			setWeizSelectModel(null);
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
		initNavigation1();
		getSelectData();
	}
}
