package com.zhiren.dc.zhuangh.caiygl;

/*
 * ���ߣ����
 * ʱ�䣺2012-03-01
 * ʹ�÷�Χ���������ׯ�ӵ糧
 * �������޸Ľ��棬����ϸ��Ϣ�ѱ��棬�û������޸�ú�����ƣ�ú�����ƣ�ú��������Ϣ
 * 		�޸ı��淽�������û��޸���ϸú��ʱ���������Ӧ�ı�����Ϣ��
 */	
/*
 * ���ߣ����
 * ʱ�䣺2014-03-10
 * ����������ׯ���볧��¯�����жϣ�������ʱ��ڵ�����9����������9��Ϊһ��ͳ�����ڡ�
 * 		����������ڵ�BUG���������ע�͡�
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-04-1
 * ����������ׯ���볧��¯�����жϣ�������ʱ��ڵ�����9����������9��Ϊһ��ͳ�����ڡ�
 * 		��2����¯ú��Ϊ1����9����2����9�����¯ú��
 */
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

public class Caiyrllr_zh extends BasePage implements PageValidateListener {
	// �����û���ʾ
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
	private String Changerq;

	public String getChangerq() {
		return Changerq;
	}
	
	public void setChangerq(String changerq) {
		Changerq = changerq;
	}
	
	
	// ҳ��仯��¼
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
	// ���save������Ҫ�����һ��grid������Լ��޸ĺ�ɾ������Ϣ��
	private void Save1(String change,Visit visit) {
		
		//�߼�˵���������漰��14:00-14:00�ĸ��������ڼ�ĺ�����Ϣ����������¯������ͬһ��ֵ��Ҳ���ǣ������ʱ�䷶Χ�ڣ�
//		��һ����ӵģ�Ҫ����һ�������������ٲ�����¯���õģ���ʱ����ڵģ�������Ϣ��һ����
//		�糧��Ϊ����ڼ��ͬһú����ͬһú�֡�ͬһú�ѵĲ�����û�б仯�ġ�
//		�ؼ�ʵ�֣��������ʱ����м����������������ô��ȡ����id������������ݵĿ��ƣ�
//		û����������Ϊ��Ҫ�������������һ�����ݲ��Ҳ���������
		
		
		String tableName = "rulmzlzb";
		
		JDBCcon con = new JDBCcon();

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(change);
		while (delrsl.next()) {
			StringBuffer sql = new StringBuffer("begin \n");
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
			sql.append(" delete from rulmzlzmxb where RULMZLZB_ID="+delrsl.getString(0)).append(";\n");
//			String d=" select id from yangpdhb where zhilblsb_id="+delrsl.getString(0);
			sql.append("end;");
			con.getUpdate(sql.toString());
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);
		
		long Diancxxb_id=0;
		//�жϷֳ���
		if (visit.isFencb()) {
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			Diancxxb_id = visit.getDiancxxb_id();
		}
		while (mdrsl.next()) {
			
			String beiz=mdrsl.getString("beiz");
			String rulrq=mdrsl.getString("shangmkssj");
			String ruld=mdrsl.getString("shangmjssj");
			long bin3=Integer.parseInt(ruld.substring(0, 10).replaceAll("-", ""));
			Date da=new Date(bin3);
			String riq_h=String.valueOf(da.getTime());
			String shijd=MainGlobal.getXitxx_item("����", "�볧��¯�ڵ�ʱ��", "0", "0");
//			if(Integer.parseInt(rulrq.substring(11, 13))<Integer.parseInt(shijd)){
////				riq_hy=String.valueOf(d.getTime()+1);
//			}
//			if(Integer.parseInt(ruld.substring(11, 13))<Integer.parseInt(shijd)){
////				riq_h=String.valueOf(da.getTime()+1);
//			}
			String shangmjssj=mdrsl.getString("shangmjssj");
	
			String zhuangt=mdrsl.getString("zhuangt");
			
			String lury=mdrsl.getString("caiyy");
			long meil=mdrsl.getLong("MEIL");
			String caiysj=mdrsl.getString("shangmkssj");//caiysj
			
			String bianm="";
			//�˴������⣬Ӧ���� ������������
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

			if ("0".equals(mdrsl.getString("ID"))) {
				
				String zzb= MainGlobal.getNewID(Diancxxb_id);
				StringBuffer sql=new StringBuffer("begin \n");

				String rul="select id from rulbzb where mingc ='"+mdrsl.getString("BANZB_ID")+"'";
					ResultSetList ruls = con.getResultSetList(rul);

					StringBuffer sql2= new StringBuffer(" begin\n");
					String caiy_id="";
					caiy_id= MainGlobal.getNewID(Diancxxb_id);
					
					while (ruls.next()){
						// ��¯���
						// ������ĺ��ñ�ţ�Ȼ���1
						String haoyh="";
						int haoy=0;
						String haoy_end="";
						String haoybh_max=
							"select max(r.haoybh) haoybh from rulmzlzb r where r.haoybh like '"+riq_h+"%'";//riq_hy
						ResultSetList ruls_max = con.getResultSetList(haoybh_max);
						while (ruls_max.next()){
							if("".equals(ruls_max.getString("haoybh"))||ruls_max.getString("haoybh")==null){
								haoyh="00";
							}else{
								
								haoyh=ruls_max.getString("haoybh").substring(8, 10);
							}
						}
						if(ruls_max.getRows()>0){//�������
							haoy=Integer.parseInt(haoyh)+1;
							if(String.valueOf(haoy).length()<=1){
								
								haoy_end="0"+haoy;	
							}else{
								haoy_end=""+haoy;
							}
							
						}else{//û������
							haoy_end="00";
							
						}
						
						String spl=//huayy,  ,'"+huayy+"'
						 "insert into rulmzlzb (id,rulmzlb_id,rulrq,fenxrq,LURSJ,meil,DIANCXXB_ID,rulbzb_id,shenhzt,caiyb_id,haoybh," +
						 "shangmkssj,shangmjssj,zhuangt,lury,banzb_id,bancb_id,beiz,haozt) "+//,meicb_id,meizb_id,bianm
						"values(getNewID(" + Diancxxb_id + ")," + zzb + ",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+
						",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss'),"+""+meil+","+visit.getDiancxxb_id()+","//bianm+
						+ruls.getString("id")+",0"+","+caiy_id+",'"+riq_h+haoy_end+"',"//riq_hy
						+"to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss')"+",to_date('"+shangmjssj+"','YYYY-MM-DD hh24:mi:ss')"
						+",'"+zhuangt+"','"+lury+"',"
//						+(getExtGrid().getColumn("meicb_id").combo).getBeanId(mdrsl.getString("meicb_id"))+","+
//						(getExtGrid().getColumn("meizb_id").combo).getBeanId(mdrsl.getString("meizb_id"))+","
						+(getExtGrid().getColumn("banzb_id").combo).getBeanId(mdrsl.getString("banzb_id"))+","
						+(getExtGrid().getColumn("bancb_id").combo).getBeanId(mdrsl.getString("bancb_id"))+",'"
						+beiz+"',0)";
						sql2.append(spl).append(" ;\n");
						
					}
					sql2.append("end;");
					
					con.getInsert(sql2.toString());
					
					String ssql="select id  from rulmzlzb where rulmzlb_id="+zzb;
					ResultSetList rsls = con.getResultSetList(ssql);
					while(rsls.next()){
						String ssql1="select id from zhuanmlb where mingc='��������'";
						String ssql2="select id from zhuanmlb where mingc='��������'";
						String ssql3="select id from zhuanmlb where mingc='�������'";
						ResultSetList rsls1 = con.getResultSetList(ssql1);
						ResultSetList rsls2 = con.getResultSetList(ssql2);
						ResultSetList rsls3 = con.getResultSetList(ssql3);
//						String zhillsb_id=	rsls.getString("id");
						while(rsls1.next()){
							// �˴��жϣ��Ҹ�ʱ����ڵĲ�������û�����ݣ��������ֻ��һ�������ܶ�ȡ���ݲ��Ҹ�ֵ��caiyb_id��
//							�����ж������ݣ�˵���������ݣ����û����ô���ܲ������ݡ�
							// �ж�14:00-����14��00

							String sql_14=
								"select c.id,r.caiyb_id\n" +
								"  from caiyb c, rulmzlzb r\n" + 
								" where c.zhilb_id = r.id\n" + 
								"   and r.shangmkssj between\n" + 
								"       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss') and\n" + 
								"       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')+1";

							ResultSetList rsls_14 = con.getResultSetList(sql_14);
							
							while (rsls_14.next()){
								//ȡ������id
								caiy_id=rsls_14.getString("id");
							}
						}
						
//						 ������Ϣ
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
						
//						 ������Ϣ
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
						}
					}

				 sql.append("end;");
				 if(sql.length()>13){
						
						con.getInsert(sql.toString());
					}
			} else {
					tableName = "rulmzlzb";

					// �߼��������޸ĵ�ʱ��ҲҪ�ж�ʱ�䣬���� ����ú��ʼʱ��ı�Ϊ�Ѿ����ڵķ�Χ�ڣ�������ʾ��Ϣ��
//					������ú��ʼʱ��С�ڵ�����ú����ʱ�䣬

//					 ������ĺ��ñ�ţ�Ȼ���1
					String haoyh="";
					int haoy=0;
					String haoy_end="";
					String haoybh_max=
						"select max(r.haoybh) haoybh from rulmzlzb r where r.haoybh like '"+riq_h+"%'";//riq_hy
					ResultSetList ruls_max = con.getResultSetList(haoybh_max);
					while (ruls_max.next()){
						if("".equals(ruls_max.getString("haoybh"))||ruls_max.getString("haoybh")==null){
							haoyh="00";
						}else{
							
							haoyh=ruls_max.getString("haoybh").substring(8, 10);
						}
					}
					if(ruls_max.getRows()>0){//�������
						haoy=Integer.parseInt(haoyh);
						if(String.valueOf(haoy).length()<=1){
							
							haoy_end="0"+haoy;	
						}else{
							haoy_end=""+haoy;
						}
						
					}else{//û������
						haoy_end="00";
						
					}
					
					StringBuffer sql2 = new StringBuffer("begin \n");
					sql2.append("update ").append(tableName).append(" set rulrq=to_date('")
					.append(mdrsl.getString("RULRQ")).append("','YYYY-MM-DD hh24:mi:ss')");
					sql2.append(",lury='").append(mdrsl.getString("CAIYY")).append("'");
					sql2.append(",meil=").append(mdrsl.getString("MEIL")).append(",shangmkssj=to_date('")
					.append(mdrsl.getString("RULRQ")).append("','YYYY-MM-DD hh24:mi:ss')").append(",shangmjssj=to_date('")
					.append(mdrsl.getString("shangmjssj")).append("','YYYY-MM-DD hh24:mi:ss')").append(",zhuangt='"+mdrsl.getString("zhuangt"))
					.append("',haoybh='"+riq_h+haoy_end+"'")//riq_h+haoy_end

					.append(",banzb_id="+(getExtGrid().getColumn("banzb_id").combo).getBeanId(mdrsl.getString("banzb_id")))
					.append(",bancb_id="+(getExtGrid().getColumn("bancb_id").combo).getBeanId(mdrsl.getString("bancb_id")))
					.append(",beiz='"+mdrsl.getString("beiz"))
					.append("'").append("").append("\n");
					sql2.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
					sql2.append("end;");
					con.getUpdate(sql2.toString());

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
	public static int InsZhillsb(JDBCcon con, long diancxxb_id, long zhilb_id, 
		String zhillsb_id, String leibid, String bmid, String leib,String caiyy,String caiysj,String caiyml){//,String leix
		String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,caiyy,caiysj,caiyml) "+//,leix
		"values(" + zhillsb_id + "," + zhilb_id + ",0,'" + "������" + "'," + leibid + "," + bmid + ",'"+caiyy+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),"+caiyml+")";//","","+huaylb+
		return con.getInsert(sql);
	}
	
	public static int InsZhuanmb(JDBCcon con,long zhuanmlb_id, long diancxxb_id, 
		String zhillsb_id, String bianm,String caiysj){
		
		String sql = "insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
		"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
		"'," + zhuanmlb_id + ");\n";
		
		String sql_z = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id +" and mingc='��������'";
		ResultSetList rsl =  con.getResultSetList(sql_z);
		StringBuffer sb_z=new StringBuffer("begin \n");
		 String czh=
	        	"select *\n" +
	        	"  from xitxxb\n" + 
	        	" where mingc = '�ж��Ƿ��Զ���������'\n" + 
	        	"   and diancxxb_id = 300\n" + 
	        	"   and zhi = '��'\n" + 
	        	"   and leib = '���ƻ�'\n" + 
	        	"   and zhuangt = 1\n" + 
	        	"   and beiz = 'ʹ��'";
		ResultSetList rsl_czh =  con.getResultSetList(czh);
		if(rsl_czh.getRows()>0){//����0 ��ô���ֶ���д
			
		}else{
			//�Զ���д

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
		
		String sql_h = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id +" and mingc='�������'";
		ResultSetList rsl_h =  con.getResultSetList(sql_h);
		//����������룬�����������λ���룬���ǲ����ظ�����������while������ѭ�������ݵ糧ҵ��ͬһ����ú�ǲ�����̫�����
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
				//���ݵ�һ�β������������ݿ��Ѿ����ڵı����жϣ�����flag��¼
				if(bianm==rsl_bian.getString("bianm")||bianm.equals(rsl_bian.getString("bianm"))){
					flag=true;
				}
			}
			while(flag){
				//���������
				long aa=Math.round(Math.random()*99);
				
				if(String.valueOf(aa).length()==1){
					//��������,λ������
					bianm =caiysj+"J"+"0"+aa;;
				}else{
					//�������룬λ��Ϊ��λ
					bianm =caiysj+"J"+aa;
				}
				// ���÷��������Ƿ���Ҫ���������µı��룬
			flag=panDflag(bianm,con);
			
			}
			//���뵽ת���
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
		
		String sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id +" and mingc='��������'";
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
		// ����ڶ�����Ϣ�����е�һ���id��
		
		Visit visit = (Visit) getPage().getVisit();

		String tableName = "rulmzlzmxb";
		JDBCcon con = new JDBCcon(); 

        StringBuffer sb_e=new StringBuffer();
        sb_e.append("begin \n");
        
		ResultSetList delrsl = visit.getExtGrid2()
				.getDeleteResultSet(getChange());
		while (delrsl.next()) {
//			ɾ��
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
		//��ʼ��ú��
		int meil=0;
//		int meil_hz=0;
		
		String sqlbm = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
		"where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
		" and c.leibb_id = l.id\n" +
		" and f.mingc = 'Ĭ��' and f.diancxxb_id =" + Diancxxb_id;
		
		ResultSetList r = con.getResultSetList(sqlbm);
		String leib ="";
		String zhuanmsz="";
		String leibid="";
//		boolean shifzm;
		String bumid="";
//		String leix="";
		while(r.next()){
			
//				�½�������ʱID
//				String zhillsb_id = MainGlobal.getNewID(Diancxxb_id);
//				ת���������
			leib= r.getString("leib");
//				ת�����
			zhuanmsz = r.getString("zhuanmsz");
//				�������ID
			leibid = r.getString("lbid");
//				����ID
			bumid = r.getString("bmid");
//				�Ƿ�ת��
//			shifzm= r.getInt("shifzm") == 1;
//			leix=r.getString("leix");
		}
		while (mdrsl.next()) {
			sb_e.append("begin \n");
				meil=mdrsl.getInt("MEIL");
//				meil_hz+=meil;
				String zzb=MainGlobal.getNewID(Diancxxb_id);
				String shijd=MainGlobal.getXitxx_item("����", "�볧��¯�ڵ�ʱ��", "0", "0");
				long bin2=Integer.parseInt(getChangerq().substring(0, 10).replaceAll("-", ""));
				Date d=new Date(bin2);
				String caiysj=String.valueOf(d.getTime());//getChangerq();
				if(Integer.parseInt(getChangerq().substring(11, 13))>=Integer.parseInt(shijd)){
					String sql_date="select to_date('"+caiysj+"','yyyy-mm-dd')+1 as caiysj from dual";
					ResultSetList rs_date = con.getResultSetList(sql_date);
					while(rs_date.next()){
						caiysj = rs_date.getDateString("caiysj").replaceAll("-", "");
					}
				}
				String rulrq=getChangerq();
				String meiz=mdrsl.getString("MEIZMC");
				String meic=mdrsl.getString("MEICMC");
				String meid=mdrsl.getString("MEIDMC");
				String yanggh=mdrsl.getString("YANGGH");
//				String sd=" select * from rulmzlzmxb where id="+getChangeid();
				String sds=" select * from rulmzlzb where id="+getChangeid();
				ResultSetList rsd = con.getResultSetList(sds);
				String rulid="0";
				String rulbzb_id="0";
				while(rsd.next()){
					rulid=rsd.getString("rulmzlb_id");
				}
			if ("0".equals(mdrsl.getString("ID"))) {
				//�����ӵ���Ϣ
				sb_e.append(" insert into ").append(tableName)//MEIZBH,MEICBH,
				.append(" ( id,RULMZLZB_ID,MIAOS,MEIZMC,MEIL,MEICMC,MEIDMC,JIZLH,JIZLMS,MEICH,MEICMS,CAIYJBH,CAIYJMS,BEIZ,rulrq,diancxxb_id,shenhzt,rulmzlb_id,rulbzb_id,shangmkssj,yanggh,zhuanmbzllsb_id)")
				.append(" values (" + zzb + ",").append(getChangeid()+",'").append(mdrsl.getString("MIAOS")+"','")
				.append(mdrsl.getString("MEIZMC")+"',")
				.append(meil+",'").append(mdrsl.getString("MEICMC")+"','").append(mdrsl.getString("MEIDMC")+"','")
				.append(mdrsl.getString("JIZLH")+"','").append(mdrsl.getString("JIZLMS")+"','").append(mdrsl.getString("MEICH")+"','")
				.append(mdrsl.getString("MEICMS")+"','").append(mdrsl.getString("CAIYJBH")+"','").append(mdrsl.getString("CAIYJMS")+"','")
				.append(mdrsl.getString("BEIZ")+"'"+ ",to_date('"+rulrq+"','YYYY-MM-DD hh24:mi:ss'),"+Diancxxb_id+",0 ,"+rulid+","+rulbzb_id+",to_date('"+getChangerq()+"','YYYY-MM-DD hh24:mi:ss')"+",'"+yanggh+"',"+zzb+");\n");
				
				String bianm="";
				
//				������ϵͳ�Զ�����
//				if("".equals(bianm)||bianm==null){
				boolean flag1= true;
				while(flag1){
					long aa=Math.round(Math.random()*99);
					if(String.valueOf(aa).length()==1){
						bianm =caiysj+"R"+"0"+aa;;
					}else{
						bianm =caiysj+"R"+aa;
					}
					flag1=panDflag(bianm,con);
				}
				
//				String ssql="select id  from rulmzlzmxb where id="+zzb;
//				ResultSetList rsls = con.getResultSetList(ssql);
					String ssql1="select id from zhuanmlb where mingc='��������'";
					String ssql2="select id from zhuanmlb where mingc='��������'";
					String ssql3="select id from zhuanmlb where mingc='�������'";
					ResultSetList rsls1 = con.getResultSetList(ssql1);
					ResultSetList rsls2 = con.getResultSetList(ssql2);
					ResultSetList rsls3 = con.getResultSetList(ssql3);
//					String zhillsb_id=	getChangeid();//��¯ú�����ӱ�
					String caiy_id="";
					String span="";
					caiy_id= MainGlobal.getNewID(Diancxxb_id);
					
					if(Integer.parseInt(rulrq.substring(11, 13))>=Integer.parseInt(shijd)){
						span="       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')  and\n" + 
						"       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')+1  and " +
						" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
					}else{
						span="       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')-1 and\n" + 
						"       to_date('"+rulrq.substring(0, 10)+" "+shijd+":00:00', 'YYYY-MM-DD hh24:mi:ss')  and " +
						" rb.meizmc='"+meiz+"' and rb.meicmc='"+meic+"' and rb.meidmc='"+meid+"'";
					}
						String sql_14=
							"select distinct c.zhilb_id,rb.caiyb_id\n" +
							"  from caiyb c, rulmzlzmxb rb \n" + 
							" where c.zhilb_id = rb.zhuanmbzllsb_id \n" + 
							"   and rb.rulrq  between\n" + span;

						ResultSetList rsls_14 = con.getResultSetList(sql_14);
						 boolean flag=false;
						 String zhilb_id=zzb;
						while (rsls_14.next()){
//							ȡ������id
							caiy_id=rsls_14.getString("caiyb_id");
							zhilb_id=rsls_14.getString("zhilb_id");
							flag=true;
						}
						if(flag){
//							��������ݣ�����һ�����Ѿ�ȡ�����ݣ���ô��ʹ����ͬ������Ϣ
							sb_e.append(" update rulmzlzmxb set zhuanmbzllsb_id="+zhilb_id+",caiyb_id="+caiy_id+" where id="+zzb).append(";\n");
						}else{
//							û��������ô��ͨ��ϵͳ�����µĲ�����Ϣ
							while(rsls1.next()){
								sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)" +
										"values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls1.getString("id")+""+");\n"
								);
//								��Ա������
								sb_e.append(" insert into RULCYYGLB (RULMZLZB_ID,renyxxb_id) values ("+zzb+",'"+"')").append(";\n");
//								System.out.println(" zhuanmb"+sql.toString());
//								 �˴��жϣ��Ҹ�ʱ����ڵĲ�������û�����ݣ��������ֻ��һ�������ܶ�ȡ���ݲ��Ҹ�ֵ��caiyb_id��
//								�����ж������ݣ�˵���������ݣ����û����ô���ܲ������ݡ�
//								 �ж�14:00-����14��00
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
					
//					 ������Ϣ
					while(rsls2.next()){
						boolean flag2= true;
						while(flag2){
							long aa=Math.round(Math.random()*99);
							if(String.valueOf(aa).length()==1){
								bianm =caiysj+"F"+"0"+aa;;
							}else{
								bianm =caiysj+"F"+aa;
							}
						flag2=panDflag(bianm,con);
						}
						sb_e.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)");
						sb_e.append("values(getNewID(" + Diancxxb_id + "),"+zzb+",'"+bianm+"',"+rsls2.getString("id")+""+");\n");
					}
					
//					 ������Ϣ
					while(rsls3.next()){
					boolean flag3= true;
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
						
				}else{
					
					sb_e.append("update rulmzlzmxb ")
					.append(" set JIZLH=' "+mdrsl.getString("JIZLH")+"',")
					.append(" JIZLMS='"+mdrsl.getString("JIZLMS")+"',")
					.append(" MEICH='"+mdrsl.getString("MEICH")+"',")
					.append(" MEICMS='"+mdrsl.getString("MEICMS")+"',")
					.append(" CAIYJBH='"+mdrsl.getString("CAIYJBH")+"',")
					.append(" CAIYJMS='"+mdrsl.getString("CAIYJMS")+"',")
					.append(" YANGGH='"+mdrsl.getString("YANGGH")+"',")
					.append(" BEIZ='"+mdrsl.getString("BEIZ")+"',")
					.append(" MEIL='"+mdrsl.getString("MEIL")+"',")
					.append(" MIAOS='"+mdrsl.getString("MIAOS")+"'")
					.append(" where id="+mdrsl.getString("ID")+";\n");
				}
			sb_e.append("end;\n");
			if(sb_e.length()>13){
				con.getInsert(sb_e.toString());
			}
			sb_e.setLength(0);
		}
		
//		ȫ��������ɺ󣬸���ú����Ϣ
		sb_e.setLength(0);
		sb_e.append(" update rulmzlzb set meil=(select sum(meil) meil from rulmzlzmxb " +
				"where rulmzlzb_id="+getChangeid()+") where id="+getChangeid());
		con.getUpdate(sb_e.toString());
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
        	"       bc.mingc bancb_id,\n" + 
        	"       rb.beiz \n" + 
        	"  from rulmzlzb rb, pinzb mz,meicb mc,rulbzb bz,rulbcb bc  \n"+//RULCYYGLB glb,renyxxb ren,,zhuanmb b, zhuanmlb l,
        	"  where rb.haoybh ='"+getYundxxValue().getValue()+"'"+
//			" and b.zhillsb_id=rb.id and b.zhuanmlb_id=l.id and l.jib=1  \n" +//and rb.id= glb.RULMZLZB_ID
			" and rb.meicb_id=mc.id(+) and rb.meizb_id=mz.id(+) and rb.banzb_id=bz.id and rb.bancb_id=bc.id(+) and rb.haozt=0";//and glb.RENYXXB_ID=ren.id 
		ResultSetList rsl1 = con.getResultSetList(ssql);
		String id="";
        while(rsl1.next()){
        	id=rsl1.getString("id");
        }
        //��¼��¼��������
        int jil=0;
        if(rsl1.getRows()>0){
        	jil=rsl1.getRows();
        }
        //������ݺ���м�¼������
        rsl1.beforefirst();
        

		if (rsl1 == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
        String czh=
        	"select *\n" +
        	"  from xitxxb\n" + 
        	" where mingc = '�ж��Ƿ��Զ���������'\n" + 
        	"   and diancxxb_id = 300\n" + 
        	"   and zhi = '��'\n" + 
        	"   and leib = '���ƻ�'\n" + 
        	"   and zhuangt = 1\n" + 
        	"   and beiz = 'ʹ��'";
        ResultSetList rsl_czh = con.getResultSetList(czh);
        if(rsl_czh.getRows()>0){
        	visit.setboolean4(true);
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
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl1);
		
		
		if(rsl2.getRows()>0){

			egu.setGridType(ExtGridUtil.Gridstyle_Read);
//			rsl2.beforefirst();
		}else{
			
			// ����GRID�Ƿ���Ա༭
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDiv1", rsl2);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// ����ÿҳ��ʾ����
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setWidth(60);
		egu.getColumn("id").editor = null;
		
		egu.getColumn("rulrq").setHidden(true);
		egu.getColumn("rulrq").setWidth(60);
		egu.getColumn("rulrq").editor = null;
		
		egu.getColumn("haoybh").setHeader("���ñ��");//Center
		egu.getColumn("haoybh").setWidth(70);
		egu.getColumn("haoybh").editor = null;
		egu.getColumn("meil").setHeader("ú��");//Center
		egu.getColumn("meil").setWidth(70);
		egu.getColumn("meil").setEditor(null);
		
//		egu.getColumn("bianm").setHeader("����");//Center
//		egu.getColumn("bianm").setWidth(70);
//		if(visit.getboolean4()){//�ֶ�
//		}else{//�Զ�
//			egu.getColumn("bianm").setHidden(true);
//			egu.getColumn("bianm").editor = null;
//		}
		
		egu.getColumn("shangmkssj").setHeader("��ú��ʼʱ��");//Center
		egu.getColumn("shangmkssj").setWidth(130);
		egu.getColumn("shangmjssj").setHeader("��ú����ʱ��");//Center
		egu.getColumn("shangmjssj").setWidth(130);
		egu.getColumn("zhuangt").setHeader("״̬");//Center
		egu.getColumn("zhuangt").setWidth(70);
		
		egu.getColumn("caiyy").setHeader("�Ǽ�Ա");//Center
		egu.getColumn("caiyy").setWidth(70);
		egu.getColumn("caiyy").setDefaultValue(visit.getRenymc());
		egu.getColumn("caiyy").setEditor(null);
//		egu.getColumn("meicb_id").setHeader("ú��");//Center
//		egu.getColumn("meicb_id").setWidth(70);
//		egu.getColumn("meizb_id").setHeader("ú��");//Center
//		egu.getColumn("meizb_id").setWidth(70);
		egu.getColumn("banzb_id").setHeader("����");//Center
		egu.getColumn("banzb_id").setWidth(70);
		
		egu.getColumn("bancb_id").setHeader("���");//Center
		egu.getColumn("bancb_id").setWidth(70);
		
		egu.getColumn("beiz").setHeader("��ע");//Center
		egu.getColumn("beiz").setWidth(70);
		

		DatetimeField datetime = new DatetimeField();
		egu.getColumn("shangmjssj").setEditor(datetime);
		egu.getColumn("shangmjssj").setDefaultValue("");
//		egu.getColumn("shangmjssj").editor.setAllowBlank(true);
		DatetimeField datet = new DatetimeField();
		egu.getColumn("shangmkssj").setEditor(datet);
		egu.getColumn("shangmkssj").setDefaultValue("");
		
//		����id
		ComboBox c9 = new ComboBox();
		egu.getColumn("banzb_id").setEditor(c9);
		c9.setEditable(true);
		String MeikbSql="select id,mingc from rulbzb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
		egu.getColumn("banzb_id").setComboEditor(egu.gridId, new IDropDownModel(MeikbSql));
		egu.getColumn("banzb_id").setReturnId(true);
		
//		����id
		ComboBox c10 = new ComboBox();
		egu.getColumn("bancb_id").setEditor(c10);
		c10.setEditable(true);
		String MeikbSql1="select id,mingc from rulbcb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
		egu.getColumn("bancb_id").setComboEditor(egu.gridId, new IDropDownModel(MeikbSql1));
		egu.getColumn("bancb_id").setReturnId(true);
		
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("���ñ��:");
		ComboBox comb = new ComboBox();
		comb.setTransform("YundxxDropDown");
		comb.setId("Yundxx");
		comb.setEditable(false);
		comb.setLazyRender(true);// ��̬��
		comb.setWidth(100);
		comb.setListWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Yundxx.on('select',function(){document.forms[0].submit();});");

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		String codt_in=" var Mrcd1 = gridDiv_ds.getModifiedRecords();"
			
			+"if(Mrcd1.length==1){Ext.MessageBox.alert('��ʾ��Ϣ','һ��ʱ���ֻ����һ��������Ϣ��'); return;}";
		
		String codt=" var Mrcd1 = gridDiv_ds.getModifiedRecords();"
			
			+"if(Mrcd1.length>1){Ext.MessageBox.alert('��ʾ��Ϣ','һ��ʱ���ֻ����һ��������Ϣ��'); return;}";
		
		if(jil==0){
			egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"",codt_in);//
		}else{
			
		}

		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",codt);
		egu.setDefaultsortable(false);
		
		//����ڶ���grid
		if(id==null||id.equals("")){
			id="-1";
		}

		setExtGrid(egu);
		// ����GRID�Ƿ���Ա༭
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu1.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// ����ÿҳ��ʾ����
		egu1.addPaging(25);
		egu1.getColumn("id").setHidden(true);
		egu1.getColumn("id").setWidth(60);
		egu1.getColumn("rulmzlzb_id").setHeader("rulmzlzb_id");//Center
		egu1.getColumn("rulmzlzb_id").setHidden(true);
		egu1.getColumn("rulmzlzb_id").setWidth(60);
		egu1.getColumn("miaos").setHeader("����");//Center
		egu1.getColumn("miaos").setWidth(120);
		egu1.getColumn("miaos").setHidden(true);
//		egu1.getColumn("meizbh").setHeader("ú�ֱ��");//Center
//		egu1.getColumn("meizbh").setWidth(60);
		egu1.getColumn("meizmc").setHeader("ú������");//Center
		egu1.getColumn("meizmc").setWidth(60);
//		egu1.getColumn("meicbh").setHeader("ú�����");//Center
//		egu1.getColumn("meicbh").setWidth(60);
		egu1.getColumn("meicmc").setHeader("ú������");//Center
		egu1.getColumn("meicmc").setWidth(120);
		
		egu1.getColumn("meidmc").setHeader("ú������");//Center
		egu1.getColumn("meidmc").setWidth(120);
		egu1.getColumn("meil").setHeader("ú��");//Center
		egu1.getColumn("meil").setWidth(120);
		
		egu1.getColumn("jizlh").setHeader("����¯��");//Center
		egu1.getColumn("jizlh").setWidth(120);
		egu1.getColumn("jizlms").setHeader("����¯����");//Center
		egu1.getColumn("jizlms").setWidth(120);
		egu1.getColumn("jizlms").setHidden(true);
		
		
		egu1.getColumn("meich").setHeader("ú�ֺ�");//Centeryanggh
		egu1.getColumn("meich").setWidth(160);
		egu1.getColumn("meich").setEditor(null);
		egu1.getColumn("yanggh").setHeader("���޺�");//Centeryanggh
		egu1.getColumn("yanggh").setWidth(130);
		egu1.getColumn("yanggh").setEditor(null);
		egu1.getColumn("meicms").setHeader("ú������");//Center
		egu1.getColumn("meicms").setWidth(120);
		egu1.getColumn("meicms").setHidden(true);
		
		egu1.getColumn("caiyjbh").setHeader("���������");//Center
		egu1.getColumn("caiyjbh").setWidth(120);
		egu1.getColumn("caiyjms").setHeader("����������");//Center
		egu1.getColumn("caiyjms").setWidth(120);
		egu1.getColumn("caiyjms").setHidden(true);
		egu1.getColumn("beiz").setHeader("��ע");//Center
		egu1.getColumn("beiz").setWidth(120);

//		ú��id
		ComboBox c6 = new ComboBox();
		egu1.getColumn("caiyjbh").setEditor(c6);
		c6.setEditable(true);
		String MeikbSql6="select id,  mingc  from caiyjb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by id";
		egu1.getColumn("caiyjbh").setComboEditor(egu1.gridId, new IDropDownModel(MeikbSql6));
		egu1.getColumn("caiyjbh").setReturnId(true);

		
//		����id
		ComboBox c1 = new ComboBox();
		egu1.getColumn("jizlh").setEditor(c1);
		c1.setEditable(true);
		String MeikbSql7="select id, jizbh as mingc  from jizb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by id";
		egu1.getColumn("jizlh").setComboEditor(egu1.gridId, new IDropDownModel(MeikbSql7));
		egu1.getColumn("jizlh").setReturnId(true);
		
		
//		ú��id
		ComboBox c2 = new ComboBox();
		egu1.getColumn("meicmc").setEditor(c2);
		c2.setEditable(true);
		String MeicbSql2="select id,mingc from meicb WHERE diancxxb_id="+visit.getDiancxxb_id()+"  order by mingc";
		egu1.getColumn("meicmc").setComboEditor(egu1.gridId, new IDropDownModel(MeicbSql2));
		egu1.getColumn("meicmc").setReturnId(true);
		
//		ú��id
		ComboBox c3 = new ComboBox();
		egu1.getColumn("meizmc").setEditor(c3);
		c3.setEditable(true);
		String MeizbSql3="select id,mingc from pinzb  "+"  order by mingc";
		egu1.getColumn("meizmc").setComboEditor(egu1.gridId, new IDropDownModel(MeizbSql3));
		egu1.getColumn("meizmc").setReturnId(true);
		
//		ú��id
		ComboBox c4 = new ComboBox();
		egu1.getColumn("meidmc").setEditor(c4);
		c4.setEditable(true);
		String MeikbSql4="select id,  mingc  from duow WHERE diancxxb_id="+visit.getDiancxxb_id()+" and zhuangt=1  order by mingc";
		egu1.getColumn("meidmc").setComboEditor(egu1.gridId, new IDropDownModel(MeikbSql4));
		egu1.getColumn("meidmc").setReturnId(true);
		
		
		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '������Ա',\n" + 
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
			"\t   \ttbar:[{text:'ȷ��',handler:function(){\n" + 
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
			" title: 'ú����Ϣ',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		
		
		String treepanel1 =
			"var navtreei = new Ext.tree.TreePanel({\n" +
			//"\t    title: '������Ա',\n" + 
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
			"\t   \ttbar:[{text:'ȷ��',handler:function(){\n" + 
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
			" title: '������Ϣ',\n " + 
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
//					"if(Mrcdd.length>1){ Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�񱣴�һ����Ϣ'); return ; }"+

//			"if(Mrcdd.length>1){ Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�񱣴�һ����Ϣ'); return ; }\n" +
			"  for(j = 0; j< Mrcdd.length; j++){\n" + 
			"  var a = Mrcdd[j].get('MEICMC');\n" + 
			"\n" + 
			"  var b =  Mrcdd[j].get('MEIDMC');\n" + 
			"\n" + 
			"  var c=b.substring(2,3);  var d =a.substring(0,1);"+
			"    if(d!=c){\n" + 
			"    Ext.MessageBox.alert('��ʾ��Ϣ','ú����ú�Ѳ���Ӧ'); return ;\n" + 
			"    }\n" + 
			"  }"


			+ " var rec = gridDiv_sm.getSelected(); "
			+ " if(rec != null){var id = rec.get('ID');var rq = rec.get('SHANGMKSSJ');"//shangmkssj
			+ " var Cobjid = document.getElementById('CHANGEID');var Cobjrq = document.getElementById('CHANGERQ');"
			+ " Cobjid.value = id; Cobjrq.value = rq; if(id==0||id=='0'){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ����һ����ӵļ�¼'); return ; }}"
			+ " if(rec == null){"
			+ "  Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����һ����Ϣ��¼'); return ;"
			+ " }"+
			"";
		String condition1 = ""

			+ " var rec = gridDiv_sm.getSelected(); "
			+ " if(rec != null){var id = rec.get('ID');var rq = rec.get('SHANGMKSSJ');"//shangmkssj
			+ " var Cobjid = document.getElementById('CHANGEID');var Cobjrq = document.getElementById('CHANGERQ');"
			+ " Cobjid.value = id; Cobjrq.value = rq; if(id==0||id=='0'){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ����һ����ӵļ�¼'); return ; }}"
			+ " if(rec == null){"
			+ "  Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����һ����Ϣ��¼'); return ;"
			+ " }"+
			"";
		egu1.addToolbarButton(GridButton.ButtonType_Insert_condition, null,condition1);//,condition1
		egu1.addToolbarButton(GridButton.ButtonType_Save_condition, "Save1Button",condition);
		GridButton gb = new GridButton("��¯ȷ��", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;document.getElementById('Save2Button').click();" +
						"}else{Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼');return; }"
				+

				"\t}"

				+ " "); 
		
		StringBuffer sbjs = new StringBuffer();
//		  �趨ĳЩֵ���ܱ༭
		sbjs.append("gridDiv1_grid.on('beforeedit',function(e){");
		sbjs.append("if(e.record.get('ID')!='0'&&(e.field=='MEIZMC'||e.field=='MEICMC'||e.field=='MEIDMC')){e.cancel=true;}");
		sbjs.append("});");
		egu1.addOtherScript(sbjs.toString());
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
		
//		���������Ĳ�ѯSQL
		String sql=
			"select 0 id,'��' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
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
		
//		���������Ĳ�ѯSQL
		String sql=
			"select 0 id,'��' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
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


//	����ʲôʱ�������ѡ���
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			
			((Visit) getPage().getVisit())
			.setDropDownBean2((IDropDownBean) getWeizSelectModel()
					.getOption(0));
			JDBCcon con=new JDBCcon();
			ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()+" and zhuangt=1 and mingc='����ú���ڲ�ѯĬ��ֵ'");
			String zhi="";
			while(rsl.next()){
				zhi=rsl.getString("zhi");
			}
			if(zhi.equals("�벴����")){
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(2));
			}else if(zhi.equals("��������")){
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
//		List list = new ArrayList();
//		Visit v=(Visit)this.getPage().getVisit();	
//		list.add(new IDropDownBean(1, "��������"));
//		list.add(new IDropDownBean(2, "��������"));
//		list.add(new IDropDownBean(3, "�벴����"));
		String sql=" select to_number(haoybh) id,haoybh mingc from rulmzlzb where rulrq = to_date('"
			+getRiq1()+"','yyyy-mm-dd')";//��ѯ���ñ�ţ�������ñ��������¯ú������
		
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"��ѡ��"));
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

//	 ������
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
//			break;// ��������
//		case 2:
//			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
//    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
//    		"  and f.daobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
//			break;// ��������
//		case 3:
//			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
//    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daohrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
//    		"  and f.daohrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
//			break;// �벴����
//		}
		String sql=" select  id,haoybh mingc from rulmzlzb where haozt=0 and shangmjssj>=to_date('"+getRiq1()+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and" +
				" shangmjssj<=to_date('"+getRiq1()+" 23:59:59','yyyy-mm-dd hh24:mi:ss') " 
			;// to_char(rulrq-1,'yyyy-mm-dd hh24:mi:ss') >='
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
	private void isCouDate(){
		if(!DateUtil.Formatdate("yyyy-MM-dd",new Date()).equals(getRiq1())){
			setMsg("ע�⣺��ǵ�������");
		}
	}
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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
		isCouDate();
	}
}
