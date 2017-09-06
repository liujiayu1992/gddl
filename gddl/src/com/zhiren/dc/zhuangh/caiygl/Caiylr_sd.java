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

public class Caiylr_sd extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		",'" + zhuanmsz + "','" + "正常样" + "'," + bmid + "," + leibid + ")";
		return con.getInsert(sql);
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		long Diancxxb_id=0;
		if (visit.isFencb()) {
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo).getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			Diancxxb_id = visit.getDiancxxb_id();
		}
		String msg="";
		while (mdrsl.next()) {
			long fahb_id=(getExtGrid().getColumn("YUNDXX").combo).getBeanId(mdrsl.getString("YUNDXX"));
			String bianm=(getExtGrid().getColumn("BIANM").combo).getBeanStrId(mdrsl.getString("BIANM"));
			if ("0".equals(mdrsl.getString("ID"))) {
//				添加且采样编码为新增采样时，按正常采样生成方式进行生成。
				if(bianm.equals("-1")){
					bianm="";
					msg=save_old(con,fahb_id,mdrsl.getString("CAIYSJ"), bianm,Diancxxb_id,mdrsl.getString("CAIYY"),mdrsl.getString("CAIYML"),mdrsl.getString("HUAYLB"));
				}else{
//					添加且编码为已存在采样编码时，将对应发货信息的zhilb_id设置为已存编码对应的标识。
//					通过采样编码对应的ZHILLSB.ID获得对应的ZHILLSB.ZHILB_ID信息
					String cy_zhilb=getZhilb_id("zhillsb",bianm);
//					通过发货表ID获得对应的FAHB.ZHILB_ID信息
					String fh_zhilb=getZhilb_id("fahb",fahb_id+"");
					if(cy_zhilb.equals(fh_zhilb)){
						msg="信息以匹配，无需重复匹配";
					}else{
						String sql="update fahb set zhilb_id="+cy_zhilb+" where id="+fahb_id;
						int flag=0;
						flag=con.getUpdate(sql);
						if(flag<0){
							msg="匹配更新保存失败";
						}else{
							msg="匹配更新保存成功";
						}
					}
				}
			} else {
//				更新且采样编码为新增采样
				if(bianm.equals("-1")){
//					查询原有采样编码对应的发货信息条数是否大于1
//					通过采样编码对应的ZHILLSB.ID获得对应的发货信息个数
					if(getCountFah(bianm)>1){
//						如果大于1，按照原采样生成方式进行生成采样信息。
						bianm="";
						msg=save_old(con,fahb_id,mdrsl.getString("CAIYSJ"), bianm,Diancxxb_id,mdrsl.getString("CAIYY"),mdrsl.getString("CAIYML"),mdrsl.getString("HUAYLB"));
					}else{
						msg="原有发货已有采样信息不用重新生成";
					}
				}else{
//					更新且编码为已存编码
//					如果采样编码下拉框中的内容未发生变化则只更新zhillsb中的采样人员信息，并返回
//					通过采样编码对应的ZHILLSB.ID获得对应的ZHILLSB.ZHILB_ID信息
					String cy_zhilb=getZhilb_id("zhillsb",bianm);
//					通过发货表ID获得对应的FAHB.ZHILB_ID信息
					String fh_zhilb=getZhilb_id("fahb",fahb_id+"");
					if(cy_zhilb.equals(fh_zhilb)){
						StringBuffer sql2 = new StringBuffer("begin \n");
						sql2.append("update zhillsb set caiysj=to_date('").append(mdrsl.getString("CAIYSJ")).append("','YYYY-MM-DD hh24:mi:ss')");
						sql2.append(",caiyy='").append(mdrsl.getString("CAIYY")).append("',caiyml=").append(mdrsl.getInt("CAIYML")).append(",huaylb='").append("正常样").append("'").append(",shenhzt=0").append("\n");//
						sql2.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
						sql2.append("end;");
						int flag=0;
						flag=con.getUpdate(sql2.toString());
						if(flag<0){
							msg="更新保存失败";
						}else{
							msg="更新保存成功";
						}
					}else{
//						如果采样编码下拉框中的内容发生变化，查询原有采样编码对应的发货信息条数是否大于1
//						通过采样编码对应的ZHILLSB.ID获得对应的发货信息个数
						if(getCountFah(bianm)>1){
//							如果大于1，将对应发货信息的zhilb_id设置为已存采样编码对应的质量标识
							String sql="update fahb set zhilb_id="+cy_zhilb+" where id="+fahb_id;
							int flag=0;
							flag=con.getUpdate(sql);
							if(flag<0){
								msg="更新保存失败";
							}else{
								msg="更新保存成功";
							}
						}else{
//							如果等于1，将对应发货信息的zhilb_id对应的采样信息删除并将对应发货信息的zhilb_id设置为已存采样编码对应的质量标识
							StringBuffer sql = new StringBuffer("begin \n");
							sql.append("delete from zhillsb where id ="+mdrsl.getString("ID")+";\n");
							sql.append("delete from yangpdhb where zhilblsb_id=").append(mdrsl.getString("ID")).append(";\n");
							sql.append("delete from zhuanmb where zhillsb_id=").append(mdrsl.getString("ID")).append(";\n");
							sql.append("update fahb set zhilb_id="+cy_zhilb+" where id="+fahb_id+";\n");
							sql.append("end;");
							int flag=0;
							flag=con.getUpdate(sql.toString());
							if(flag<0){
								msg="更新保存失败";
							}else{
								msg="更新保存成功";
							}
						}
					}
				}
		   }
		}
		setMsg(msg);
		con.Close();
		mdrsl.close();
	}
	
//	删除
	private void delete(){
		JDBCcon con = new JDBCcon();
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		String msg="";
		while (delrsl.next()) {
				StringBuffer sql = new StringBuffer("begin \n");
				sql.append("delete from zhillsb where id ="+delrsl.getString("ID")+";\n");
				sql.append("delete from yangpdhb where zhilblsb_id=").append(delrsl.getString("ID")).append(";\n");
				sql.append("delete from zhuanmb where zhillsb_id=").append(delrsl.getString("ID")).append(";\n");
				sql.append("end;");
				int flag=0;
				flag=con.getUpdate(sql.toString());
				if(flag<0){
					msg="删除失败";
				}else{
					msg="删除成功";
				}
		}
		setMsg(msg);
		delrsl.close();
		con.Close();
	}
	
//	拆分
	private void Chaif(){
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		String msg="";
		while (delrsl.next()) {
			long fahb_id=(getExtGrid().getColumn("YUNDXX").combo).getBeanId(delrsl.getString("YUNDXX"));
			String bianm=(getExtGrid().getColumn("BIANM").combo).getBeanStrId(delrsl.getString("BIANM"));
//			通过采样编码对应的ZHILLSB.ID获得对应的发货信息个数
			if(getCountFah(bianm)>1){
				StringBuffer sql = new StringBuffer("begin \n");
				sql.append("update fahb set zhilb_id="+MainGlobal.getNewID(visit.getDiancxxb_id())+" where id="+fahb_id+";\n");
				sql.append("end;");
				int flag=0;
				flag=con.getUpdate(sql.toString());
				if(flag<0){
					msg="拆分失败";
				}else{
					msg="拆分成功";
				}
			}else{
				msg="该发货未合并，无需拆分";
			}
		}
		setMsg(msg);
		delrsl.close();
		con.Close();
	}
	
//	通过采样编码对应的ZHILLSB.ID获得对应的ZHILLSB.ZHILB_ID信息
	private String getZhilb_id(String tablename,String ID){
		String zhilb_id="";
		JDBCcon con = new JDBCcon();
		String sql="select zhilb_id from "+tablename+" where id="+ID;
		ResultSetList rsl=con.getResultSetList(sql);
		while(rsl.next()){
			zhilb_id=rsl.getString("zhilb_id");
		}
		rsl.close();
		con.Close();
		return zhilb_id;
	}
	
//	通过采样编码对应的ZHILLSB.ID获得对应的发货信息个数
	private int getCountFah(String ID){
		int Number=0;
		JDBCcon con = new JDBCcon();
		String sql=
			"SELECT COUNT(ID) FAHNUMBER\n" +
			"  FROM FAHB\n" + 
			" WHERE ZHILB_ID IN\n" + 
			"       (SELECT ZHILB_ID FROM ZHILLSB WHERE ID ="+ID+" )";
		ResultSetList rsl=con.getResultSetList(sql);
		while(rsl.next()){
			Number=rsl.getInt("FAHNUMBER");
		}
		rsl.close();
		con.Close();
		return Number;
	}
	
//	原始的保存方法
	private String save_old(JDBCcon con,long fahb_id,String CAIYSJ, String BIANM,long Diancxxb_id,String CAIYY,String CAIYML,String HUAYLB){
		Visit visit = (Visit) this.getPage().getVisit();
		String sql1 = "select * from fahb where id = " + fahb_id;
		if(!con.getHasIt(sql1)){
			con.Close();
			return "未找到对应发货信息";
		}
		
		long bin2=Integer.parseInt(CAIYSJ.substring(0, 10).replaceAll("-", ""));
		Date d=new Date(bin2);
		String caiysj=String.valueOf(d.getTime());//getChangerq();
		if(Integer.parseInt(CAIYSJ.substring(11, 13))>=13){
			String sql_date="select to_date('"+caiysj+"','yyyy-mm-dd')+0 as caiysj from dual";
			ResultSetList rs_date = con.getResultSetList(sql_date);
			while(rs_date.next()){
				caiysj = rs_date.getDateString("caiysj").replaceAll("-", "");
			}
			rs_date.close();
		}
		ResultSetList rsl = con.getResultSetList(sql1);
		long meikxxb_id=0;
		long jihkjb_id=0;
		long yunsfsb_id=0;
		long faz_id=0;
		long zhilb_id=0;
		while(rsl.next()){
			meikxxb_id=rsl.getLong("meikxxb_id");
			jihkjb_id=rsl.getLong("jihkjb_id");
			yunsfsb_id=rsl.getLong("yunsfsb_id");
			faz_id=rsl.getLong("faz_id");
			zhilb_id=rsl.getLong("zhilb_id");
		}
		rsl.close();
		//产生编码信息--原来产生方式mdrsl.getString("BIANM")
		boolean flag= false;
		
		long ab=Math.round(Math.random()*99);
		String bianm =caiysj+"R"+ab;//mdrsl.getString("CAIYSJ").substring(0, 10).replaceAll("-", "")
		if(visit.getboolean4()&&(!"".equals(BIANM)||BIANM!=null)){//true 是手动
			bianm= BIANM;
		}else{
			// 产生自动采样码
			if(String.valueOf(ab).length()==1){
				bianm =caiysj+"R"+"0"+ab;
			}else{
				bianm =caiysj+"R"+ab;
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
					bianm =caiysj+"R"+"0"+aa;;
				}else{
					bianm =caiysj+"R"+aa;
				}
				flag=panDflag(bianm,con);
			}
			//结束自动产生
		}
		CreatBianh(con,zhilb_id,Diancxxb_id,meikxxb_id,jihkjb_id,yunsfsb_id,faz_id,bianm, CAIYY , CAIYSJ , CAIYML ,HUAYLB);
		con.Close();
		return "生成完成";
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
	
	public  int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
			long meikxxb_id, long jihkjb_id, long yunsfsb_id, long chezxxb_id, 
			String bianm,String caiyy,String caiysj,String caiyml,String leib) {
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
//			新建质量临时ID zhilb_id
			String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//			转码编码
			String zhuanmsz = rsl.getString("zhuanmsz");
//			化验类别ID
			String leibid = rsl.getString("lbid");
//			部门ID
			String bumid = rsl.getString("bmid");
//			是否转码
			boolean shifzm = rsl.getInt("shifzm") == 1;
//			String huaylb=rsl.getString("HUAYLB");
//			插入到采样表(可能不需要)
			String cai_id=MainGlobal.getNewID(diancxxb_id);
			if(caiyb_id==null||"".equals(caiyb_id)){
				caiyb_id=cai_id;
			}
			String yundh=rsl.getString("yundh");
			ResultSetList rs;
			String bianm_c="";
			String sql_s="select max(f.bianh)+1 yund from caiyb f where to_char(f.caiyrq,'yyyy-mm-dd')='"+getRiq1()+"' and f.bianm like '"+getRiq1().replaceAll("-", "")+"%'";
			if("".equals(yundh)||yundh==null){
				rs=con.getResultSetList(sql_s);
				while(rs.next()){
					if("".equals(rs.getString("yund"))||rs.getString("yund")==null){
						bianm_c=getRiq1().replaceAll("-", "")+"00";
					}else{
						bianm_c=rs.getString("yund");
					}
				}
			}
			String sr=" insert into caiyb (id,zhilb_id,cunywzb_id,bianm,caiyrq,xuh,bianh) values" +//zhilb_id
					"("+cai_id+","+zhillsb_id+",0,'"+bianm_c+"',to_date('"+caiysj.substring(0, 10)+"','YYYY-MM-DD'),0,'"+bianm_c+"')";// hh24:mi:ss
			con.getInsert(sr);
//			写入样品单号表
			flag = InsYangpdhb(con, diancxxb_id, zhilb_id, cai_id, 
							zhillsb_id, leibid, bumid, zhuanmsz, leib);
			if(flag == -1){
//				如果出错则返回错误
				return -1;
			}
//			写入质量临时表
			InsZhillsb(con, diancxxb_id, zhilb_id, 
					zhillsb_id, leibid, bumid, leib,caiyy,caiysj,caiyml);//,leix
			if(flag == -1){
//				如果出错则返回错误
				return -1;
			}
			if(bianm != null){
//				如果 手录编码==采样表中编码 则不使用转码设置
//				如果非自动生成编码			
				zhuanmsz = "";
				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, false, zhuanmsz,caiysj);
				if(flag == -1){
//					如果出错则返回错误
					return -1;
				}
			}else{
//				判断如果是自动生成编码
				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, shifzm, zhuanmsz,caiysj);
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
			String zhillsb_id, String leibid, String bmid, String leib,String caiyy,String caiysj,String caiyml){//,String leix
		
	if(caiyml==null||"".equals(caiyml)){
		caiyml="0";
	}
		String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,caiyy,caiysj,caiyml,yuanzhilb_id) "+//,leix
		"values(" + zhillsb_id + "," + zhilb_id + ",0,'" + "正常样" + "'," + leibid + "," + bmid + ",'"+caiyy+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),"+caiyml+","+zhilb_id+")";//","","+huaylb+
		return con.getInsert(sql);
	}
	
	public static int InsZhuanmb(JDBCcon con,long zhuanmlb_id, long diancxxb_id, 
		String zhillsb_id, String bianm,String caiysj){
		String sql = "insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
		"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
		"'," + zhuanmlb_id + ");\n";
		
		long bin2=Integer.parseInt(caiysj.substring(0, 10).replaceAll("-", ""));
		Date d=new Date(bin2);
		String caiys=String.valueOf(d.getTime());//getChangerq();
		if(Integer.parseInt(caiysj.substring(11, 13))>=13){
			String sql_date="select to_date('"+caiys+"','yyyy-mm-dd')+0 as caiysj from dual";
			ResultSetList rs_date = con.getResultSetList(sql_date);
			while(rs_date.next()){
				caiys = rs_date.getDateString("caiysj").replaceAll("-", "");
			}
		}
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
			 bianm =caiys+"F"+ab;
			if(String.valueOf(ab).length()==1){
				bianm =caiys+"F"+"0"+ab;//caiysj.substring(0, 10).replaceAll("-", "")
			}else{
				bianm =caiys+"F"+ab;
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
					bianm =caiys+"F"+"0"+aa;;
				}else{
					bianm =caiys+"F"+aa;
				}
			flag=panDflag(bianm,con);
			}
			sb_z.append("insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
					"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
					"'," + rsl.getString("id") + ");\n");
		}
		
		String sql_h = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id +" and mingc='化验编码'";
		ResultSetList rsl_h =  con.getResultSetList(sql_h);
        while(rsl_h.next()){
            boolean flag= false;
			
			long ab=Math.round(Math.random()*99);
			 bianm =caiys+"J"+ab;
			if(String.valueOf(ab).length()==1){
				bianm =caiys+"J"+"0"+ab;
			}else{
				bianm =caiys+"J"+ab;
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
					bianm =caiys+"J"+"0"+aa;;
				}else{
					bianm =caiys+"J"+aa;
				}
			flag=panDflag(bianm,con);
			}
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
	
	private boolean _DelChick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelChick = true;
	}
	
	private boolean _ChaifChick = false;

	public void ChaifButton(IRequestCycle cycle) {
		_ChaifChick = true;
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
		}else if(_DelChick){
			_DelChick=false;
			delete();
			getSelectData();
		}else if(_RefurbishChick){
		   _RefurbishChick = false;
			getSelectData();
		}else if(_ChaifChick){
			_ChaifChick=false;
			Chaif();
			getSelectData();
		}
	}
	
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();

//		根据日期框的不同选取不同的日期字段
		String riq="KAOBRQ";
		switch ((int) this.getWeizSelectValue().getId()) {
		case 1:
			riq="KAOBRQ";
			break;// 靠泊日期
		case 2:
			riq="DAOBRQ";
			break;// 到泊日期
		case 3:
			riq="DAOHRQ";
			break;// 离泊日期
		}
		
		sb.append(	"SELECT Z.ID,\n" +
					"       F.ZHILB_ID,\n" + 
					"       LC.MINGC || '_' || C.MINGC || '_' || P.MINGC || '_' || F.BIAOZ AS YUNDXX,\n" + 
					"       TO_CHAR(Z.CAIYSJ, 'YYYY-MM-DD hh24:mi:ss') AS CAIYSJ,\n" + 
					"       B.BIANM,\n" + 
					"       Z.CAIYY,\n" + 
					"       Z.CAIYML\n" + 
					"  FROM ZHILLSB  Z,\n" + 
					"       FAHB     F,\n" + 
					"       ZHUANMB  B,\n" + 
					"       ZHUANMLB L,\n" + 
					"       LUNCXXB  LC,\n" + 
					"       PINZB    P,\n" + 
					"       CHEZXXB  C\n" + 
					" WHERE F.LUNCXXB_ID = LC.ID\n" + 
					"   AND F.PINZB_ID = P.ID\n" + 
					"   AND F.LIUCZTB_ID = 1\n" + 
					"   AND F.FAZ_ID = C.ID(+)\n" + 
					"   AND Z.ZHILB_ID = F.ZHILB_ID\n" + 
					"   AND B.ZHILLSB_ID = Z.ID\n" + 
					"   AND B.ZHUANMLB_ID = L.ID\n" + 
					"   AND L.JIB = 1\n" + 
					"   AND Z.HUAYLB = '正常样'\n" + 
					"   AND F."+riq+" = TO_DATE('"+this.getRiq1()+"', 'yyyy-MM-dd')");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置每页显示行数
		egu.addPaging(25);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setWidth(100);
		egu.getColumn("id").editor = null;
		
		egu.getColumn("ZHILB_ID").setHidden(true);
		egu.getColumn("ZHILB_ID").setWidth(100);
		egu.getColumn("ZHILB_ID").editor = null;
		egu.getColumn("ZHILB_ID").setDefaultValue("0");
		
		
		egu.getColumn("YUNDXX").setHeader("运单信息");
		egu.getColumn("YUNDXX").setWidth(250);
		ComboBox cb_fahxx = new ComboBox();
		egu.getColumn("YUNDXX").setEditor(cb_fahxx);
		egu.getColumn("YUNDXX").setDefaultValue("");
		cb_fahxx.setEditable(true);
		String cb_fahxxSql="SELECT F.ID,\n" +
			"       L.MINGC || '_' || C.MINGC || '_' || P.MINGC || '_' || F.BIAOZ AS YUNDXX\n" + 
			"  FROM FAHB F, LUNCXXB L, PINZB P, CHEZXXB C\n" + 
			" WHERE F.LUNCXXB_ID = L.ID\n" + 
			"   AND F.PINZB_ID = P.ID\n" + 
			"   AND F.LIUCZTB_ID = 1\n" + 
			"   AND F.FAZ_ID = C.ID(+)"+
			"	AND F."+riq+" = TO_DATE('"+this.getRiq1()+"', 'yyyy-MM-dd')";

		egu.getColumn("YUNDXX").setReturnId(true);
		egu.getColumn("YUNDXX").setComboEditor(egu.gridId, new IDropDownModel(cb_fahxxSql));
		egu.getColumn("YUNDXX").setDefaultValue("请选择");
		egu.getColumn("YUNDXX").editor.setAllowBlank(false);		
		
		egu.getColumn("caiysj").setHeader("采样时间");//Center
		egu.getColumn("caiysj").setWidth(120);
		// 设置采样时间的默认值
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("caiysj").setEditor(datetime);
		egu.getColumn("caiysj").setDefaultValue(DateUtil.FormatDateTime(new Date()));	
		
		egu.getColumn("BIANM").setCenterHeader("采样编码");
		egu.getColumn("BIANM").setWidth(90);
		ComboBox cb_cybm = new ComboBox();
		egu.getColumn("BIANM").setEditor(cb_cybm);
		egu.getColumn("BIANM").setDefaultValue("");
		cb_cybm.setEditable(true);
		String cb_cybmSql=
			"SELECT Z.ID, B.BIANM\n" +
			"  FROM ZHILLSB Z, ZHUANMB B, ZHUANMLB L\n" + 
			" WHERE Z.ZHILB_ID IN\n" + 
			"       (SELECT ZHILB_ID\n" + 
			"          FROM FAHB F\n" + 
			"         WHERE F.LIUCZTB_ID = 1\n" + 
			"           AND F."+riq+" = TO_DATE('"+this.getRiq1()+"', 'yyyy-MM-dd'))\n" + 
			"   AND B.ZHILLSB_ID = Z.ID\n" + 
			"   AND B.ZHUANMLB_ID = L.ID\n" + 
			"   AND L.JIB = 1\n" + 
			"   AND Z.HUAYLB = '正常样'";
		egu.getColumn("BIANM").setReturnId(true);
		egu.getColumn("BIANM").setComboEditor(egu.gridId, new IDropDownModel(cb_cybmSql,"新增编码"));
		egu.getColumn("BIANM").setDefaultValue("新增编码");
		egu.getColumn("BIANM").editor.setAllowBlank(false);

		egu.getColumn("caiyy").setCenterHeader("采样人员");
		egu.getColumn("caiyy").setWidth(150);
		egu.getColumn("caiyy").setEditor(null);

		egu.getColumn("caiyml").setCenterHeader("采样煤量");
		egu.getColumn("caiyml").setWidth(90);
		egu.getColumn("caiyml").setHidden(true);

		ComboBox comb1 = new ComboBox();
		comb1.setTransform("WeizSelectx");
		comb1.setId("WeizSelectx");
		comb1.setWidth(100);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("WeizSelectx.on('select',function(){document.forms[0].submit();});");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		String ss="var CAIYSJ= '"+DateUtil.FormatDateTime(new Date())+"';" +
				"if(CAIYSJ>='"+DateUtil.FormatDateTime(new Date()).substring(0, 10)+" 13:00:00'&&CAIYSJ<='"+DateUtil.FormatDateTime(new Date()).substring(0, 10)+" 20:00:00') {Ext.MessageBox.alert('提示信息','请注意采样时间已经超过下午一点');}";
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"",ss);
		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '采样人员',\n" + 
			//"\t    regio n: 'east',\n " + 
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
		
		String click_Scrpit="gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
			+ "row = irow; \n"
			+ "if('CAIYY' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
			+"if(!win){"+treepanel+Strtmpfunction+"}"
			+"win.show(this);}});\n";
		
		String before_Script="gridDiv_grid.on('beforeedit',function(e){if(e.field=='YUNDXX'&& e.record.get('ZHILB_ID')!=0 ){e.cancel=true;} });\n ";
		egu.addOtherScript(click_Scrpit+before_Script);
		
//		删除按钮
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){var record=gridDiv_grid.getSelectionModel().getSelected();");
		btnsb.append("if(record.get('ID')==0){Ext.MessageBox.alert('提示信息','未保存记录不能删除');return;}");
		btnsb.append("if(record==null){Ext.MessageBox.alert('提示信息','请先选择一条记录');return;}");
		btnsb.append("var Mrcd = gridDiv_ds.getModifiedRecords();\n");
		btnsb.append("for(i = 0; i< Mrcd.length; i++){\n");
		btnsb.append("if(gridDiv_ds.indexOf(record)==gridDiv_ds.indexOf(Mrcd[i])){Ext.MessageBox.alert('提示信息','所选记录已变更，不能删除'); return;}\n");
		btnsb.append("}\n");
		btnsb.append("Ext.MessageBox.confirm('提示信息','是否删除所选的数据？',function(btn){if(btn == 'yes'){");
		btnsb.append("gridDiv_history += '<result>' + '<sign>D</sign>' + '<ID update=\"true\">' + record.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'");
		btnsb.append("+ '<ZHILB_ID update=\"true\">' + record.get('ZHILB_ID')+ '</ZHILB_ID>'");
		btnsb.append("+ '<YUNDXX update=\"true\">' + record.get('YUNDXX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNDXX>'");
		btnsb.append("+ '<CAIYSJ update=\"true\">' + record.get('CAIYSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYSJ>'");
		btnsb.append("+ '<BIANM update=\"true\">' + record.get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'");
		btnsb.append("+ '<CAIYY update=\"true\">' + record.get('CAIYY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYY>'");
		btnsb.append("+ '<CAIYML update=\"true\">' + record.get('CAIYML')+ '</CAIYML>'");
		btnsb.append(" + '</result>' ;");
		btnsb.append("var Cobj = document.getElementById('CHANGE');");
		btnsb.append("Cobj.value = '<result>'+gridDiv_history+'</result>';");
		btnsb.append("document.getElementById('DelButton').click();");
		btnsb.append("Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});");
		btnsb.append("};});");
		btnsb.append("}");
		
		GridButton gbd = new GridButton("删除",btnsb.toString());
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		
		String tj_Script=
			"function(){\n"+
			"var gridDivsave_history = '';\n"+
			"var record=gridDiv_grid.getSelectionModel().getSelected();\n"+
			"if(record==null){Ext.MessageBox.alert('提示信息','请先选择一条记录');return;}\n"+
			"var Mrcd = gridDiv_ds.getModifiedRecords();\n"+
			"for(i = 0; i< Mrcd.length; i++){\n"+
			"if(gridDiv_ds.indexOf(record)==gridDiv_ds.indexOf(Mrcd[i])){\n"+
			"if(typeof(gridDiv_save)=='function'){\n"+
			" var revalue = gridDiv_save(Mrcd[i]);\n"+
			" if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n"+
			"if(Mrcd[i].get('YUNDXX') == ''){Ext.MessageBox.alert('提示信息','字段 运单信息 不能为空');return;\n"+
			"}if(Mrcd[i].get('CAIYSJ') == ''){Ext.MessageBox.alert('提示信息','字段 采样时间 不能为空');return;\n"+
			"}if(Mrcd[i].get('BIANM') == ''){Ext.MessageBox.alert('提示信息','字段 <center>采样编码</center> 不能为空');return;\n"+
			"}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n"+
			"+ '<ZHILB_ID update=\"true\">' + Mrcd[i].get('ZHILB_ID')+ '</ZHILB_ID>'\n"+
			"+ '<YUNDXX update=\"true\">' + Mrcd[i].get('YUNDXX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNDXX>'\n"+
			"+ '<CAIYSJ update=\"true\">' + Mrcd[i].get('CAIYSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYSJ>'\n"+
			"+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'\n"+
			"+ '<CAIYY update=\"true\">' + Mrcd[i].get('CAIYY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYY>'\n"+
			"+ '<CAIYML update=\"true\">' + Mrcd[i].get('CAIYML')+ '</CAIYML>'\n"+
			" + '</result>' ;} }\n"+
			"if(gridDiv_history=='' && gridDivsave_history==''){\n"+ 
			"Ext.MessageBox.alert('提示信息','所选行没有进行改动无需保存');\n"+
			"}else{\n"+
			"var Cobj = document.getElementById('CHANGE');\n"+
			"Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n"+
			"document.getElementById('SaveButton').click();\n"+
			"Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"+
			"}\n"+
			"}\n";

		GridButton dbd = new GridButton("保存",tj_Script);
		dbd.setIcon(SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(dbd);
		
//		增加拆分按钮
		StringBuffer btncf = new StringBuffer();
		btncf.append("function (){var record=gridDiv_grid.getSelectionModel().getSelected();");
		btncf.append("if(record.get('ID')==0){Ext.MessageBox.alert('提示信息','未保存记录不能拆分');return;}");
		btncf.append("if(record==null){Ext.MessageBox.alert('提示信息','请先选择一条记录');return;}");
		btncf.append("var Mrcd = gridDiv_ds.getModifiedRecords();\n");
		btncf.append("for(i = 0; i< Mrcd.length; i++){\n");
		btncf.append("if(gridDiv_ds.indexOf(record)==gridDiv_ds.indexOf(Mrcd[i])){Ext.MessageBox.alert('提示信息','所选记录已变更，不能拆分'); return;}\n");
		btncf.append("}\n");
		btncf.append("Ext.MessageBox.confirm('提示信息','是否拆分所选的数据？',function(btn){if(btn == 'yes'){");
		btncf.append("gridDiv_history += '<result>' + '<sign>D</sign>' + '<ID update=\"true\">' + record.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'");
		btncf.append("+ '<ZHILB_ID update=\"true\">' + record.get('ZHILB_ID')+ '</ZHILB_ID>'");
		btncf.append("+ '<YUNDXX update=\"true\">' + record.get('YUNDXX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNDXX>'");
		btncf.append("+ '<CAIYSJ update=\"true\">' + record.get('CAIYSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYSJ>'");
		btncf.append("+ '<BIANM update=\"true\">' + record.get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'");
		btncf.append("+ '<CAIYY update=\"true\">' + record.get('CAIYY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYY>'");
		btncf.append("+ '<CAIYML update=\"true\">' + record.get('CAIYML')+ '</CAIYML>'");
		btncf.append(" + '</result>' ;");
		btncf.append("var Cobj = document.getElementById('CHANGE');");
		btncf.append("Cobj.value = '<result>'+gridDiv_history+'</result>';");
		btncf.append("document.getElementById('ChaifButton').click();");
		btncf.append("Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});");
		btncf.append("};});");
		btncf.append("}");
		
		GridButton gbcf = new GridButton("拆分",btncf.toString());
		gbcf.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbcf);
		
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
			"and r.bum='采样' and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

//	按照什么时间查条件选择框
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getWeizSelectModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {
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
		list.add(new IDropDownBean(1, "靠泊日期"));
		list.add(new IDropDownBean(2, "到泊日期"));
		list.add(new IDropDownBean(3, "离泊日期"));
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
	public  String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {
			((Visit) this.getPage().getVisit()).setString5(riq1);
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
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			setRiq1(null);
			setTbmsg(null);
			initNavigation();
			visit.setString1(null);
		}

		visit.setboolean4(false);
		initNavigation();
		getSelectData();
	}
}