package com.zhiren.jt.shous;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.ibm.icu.text.DecimalFormat;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.filejx.FileJx;
import com.zhiren.common.filejx.FilePathRead;
import com.zhiren.jt.shous.CreateData;

public class Shous {

	public void readFileDayData() {
		// 收数处理程序
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSet rs;
		String leix = "HC";
		 ArrayList strbf=new ArrayList();
		 ArrayList strbf2=new ArrayList();
		 CreateData cd = new CreateData();
		 FileJx wjjx=new FileJx();
		 try{ 
			 String shangblj = cd.getFilepath("日报文件上报路径");
//			 String benflj = cd.getFilepath("日报文件备份路径");				 
			 
			 FilePathRead jx=new FilePathRead(leix,shangblj);
			 strbf=jx.getTxtFileList();//得到文件列表
			 int result = -1;
			 
			 for(int i=0;i<strbf.size();i++){
				 strbf2=wjjx.TextJx(strbf.get(i).toString());//一个文件
				 String fileName= strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1);
				 String shangcsj = wjjx.getWenjrq(strbf.get(i).toString());
				 
				 
				 String cksql = "select distinct diancscwjm,to_char(diancscsj,'yyyy-mm-dd HH24:mi:ss') as diancscsj from shouhcrbb rb where rb.diancscwjm='"+fileName+"'";
				 rs = con.getResultSet(cksql);
				 if(rs.next()){
					 if(rs.getString("diancscsj").equals(shangcsj)){//已经接收过数据并且文件没有修改
						 continue;
					 }else{//已经接收数据，但文件做了修改
						 
					 }
				 }else{
				 
					 String value[][] = new String[][]{}; 
					 value = cd.getFileData(strbf2, leix);
					 for(int r=0;r<value.length;r++){
						 if(leix.equals("HC")){
							 int danwjb = 0;//单位级别
							 String gssql = "select d.jib from diancxxb d where d.bianm='"+value[r][1]+"'";
							 rs = con.getResultSet(gssql);
							 if(rs.next()){
								 danwjb = rs.getInt("jib");
							 }
							 if(danwjb==3){
								 long zuorkc = 0;//昨日库存
								 String ssql = "select kuc from shouhcrbb rb where rb.diancxxb_id="+getProperId(getIDiancbmModel(),value[r][1],-1)+" and rb.riq=to_date('"+value[r][0]+"','yyyy-mm-dd')-1";
								 rs = con.getResultSet(ssql);
								 while(rs.next()){
									 zuorkc = rs.getLong("kuc");
								 }
								 String sql = "insert into shouhcrbb(id, riq, diancxxb_id, dangrgm, haoyqkdr, kuc, diancscsj, beiz, dangrfdl, tiaozl, shangbkc, diancscwjm) values (" 
									 		+getTableId("shouhcrbb")+",to_date('"+value[r][0]+"','yyyy-mm-dd'),"+getProperId(getIDiancbmModel(),value[r][1],-1)+","+value[r][3]+","+value[r][7]+","
									 		+(zuorkc+Long.parseLong(value[r][3])-Long.parseLong(value[r][7]))+","
									 		+"to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),'',0,0,"+value[r][9]+",'"+fileName+"')";
								 result = con.getInsert(sql);
								 
								 if(result<0){
									 con.rollBack();
									 System.out.println(fileName+"数据插入失败!"+DateUtil.FormatDateTime(new Date()));
									 break;
								 }
							 }
						 }
					 }
					 con.commit();
				 }
				 /*
				 if(result>=0 || strbf2.size()==0){
					 File fl=new File(strbf.get(i).toString());
					 File f2=new File(benflj+"/"+strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1));
					 if(f2.exists()){
						 f2.delete();
						 fl.renameTo(new File(benflj,strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1)));
						
					 }else{
						 fl.renameTo(new File(benflj,strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1)));
					 }
					 fl.delete();
				 }*/
			 }
		 }catch(Exception e){
			 con.rollBack();
	 		 e.printStackTrace();
		 }finally{
			 con.Close();
		 }
	}
	
	public void readFileMonthData(int intYear,int intMonth) {
		// 收数处理程序
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSet rs;
		ResultSet rec;
		
		StringBuffer sbsql = new StringBuffer();
		String leix[] = {"1","3","4","6","8"};
		 ArrayList strbf=new ArrayList();
		 ArrayList strbf2=new ArrayList();
		 CreateData cd = new CreateData();
		 FileJx wjjx=new FileJx();
		 StringBuffer sbtmp = new StringBuffer();
		 String strMonth="";
		 if(intMonth==0){
			 intYear = intYear-1;
			 intMonth = 12;
		 }
		 if(intMonth<10){
			 strMonth = "0"+intMonth;
		 }else{
			 strMonth = ""+intMonth;
		 }
		 String riq = intYear+"-"+strMonth+"-01";
		 String sql = "";
		 int x=0;	
		 try{ 
			 for(int l=0;l<leix.length;l++){
				 
				 String shangblj = cd.getFilepath("月报文件上报路径");
//				 String beiflj = cd.getFilepath("月报文件备份路径");
				 
				 String strfilename = leix[l]+(""+intYear).substring(2)+strMonth;
				 FilePathRead jx=new FilePathRead(strfilename,shangblj);
				 strbf=jx.getTxtFileList();//得到文件列表
				 
				 for(int i=0;i<strbf.size();i++){
					 strbf2=wjjx.TextJx(strbf.get(i).toString());//一个文件
					 String fileName= strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1);
//					 String riq = "";
//					 if(leix[l].equals("1")||leix[l].equals("3")||leix[l].equals("4")||leix[l].equals("6")||leix[l].equals("8")){
//						 riq = "20"+fileName.substring(1,3)+"-"+fileName.substring(3,5)+"-01";
//					 }
					 String shangcsj = wjjx.getWenjrq(strbf.get(i).toString());
					 String value[][] = new String[][]{}; 
					 value = cd.getFileData(strbf2, leix[l]);
					 
					 if(x==0){
						 if(leix[l].equals("6")){
							 sql="delete from diaor16bb where diancscwjm='"+fileName+"'";
						 }else{
							 sql="delete from diaor0"+leix[l]+"bb where diancscwjm='"+fileName+"'";
						 }
//						 if(leix[l].equals("6")){
//							 sql="delete from diaor16bb where diancxxb_id="+getProperId(getIDiancbmModel(),value[0][0],-1)+" and riq=to_date('"+riq+"','yyyy-MM-dd')";
//						 }else{
//							 sql="delete from diaor0"+leix[l]+"bb where diancxxb_id="+getProperId(getIDiancbmModel(),value[0][0],-1)+" and riq=to_date('"+riq+"','yyyy-MM-dd')";
//						 }
						 con.getDelete(sql);
						 con.commit();
						 x++;
					 }
					 for(int r=0;r<value.length;r++){
						 
					     if(leix[l].equals("6")){
						 
							 long dianc_id = getProperId(getIDiancbmModel(),value[r][0],-1);
							 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
							 long Gongysb_id = getProperId(getIGongysbIdModel(),value[r][2],dianc_id);
							 
							 if(getProperId(getIDiancbmModel(),value[r][0],-1)==-1){
								 continue;
							 }
							 
							 if(Gongysb_id==-1){
								 if(sbtmp.length()==0){
									 sbtmp.append(strdiancmc+"的供应商编码"+value[r][2]);
								 }else{
									 sbtmp.append(","+strdiancmc+"的供应商编码"+value[r][2]);
								 }
							 }else{
								 sql="insert into diaor16bb (id, riq, fenx, diancxxb_id, Gongysb_id, pinz, shangyjc, kuangfgyl, yuns, kuid, farl, huif, shuif, huiff, qitsrl,"
									 +" shijhylhj, shijhylfdy, shijhylgry, shijhylqty, shijhylzcsh, diaocl, panypk, yuemjc,diancscsj,jiessj,jiesr, beiz,diancscwjm) values("
									 +getTableId("diaor16bb")+",to_date('"+riq+"','yyyy-MM-dd'), '本月', "
									 +getProperId(getIDiancbmModel(),value[r][0],-1)+","+getProperId(getIGongysbIdModel(),value[r][2],dianc_id)+", '',"
									 +Float.parseFloat(value[r][3])+", "+Float.parseFloat(value[r][4])+", 0, "+Float.parseFloat(value[r][5])+", "
									 +Float.parseFloat(value[r][6])+", "+Float.parseFloat(value[r][7])+", "+Float.parseFloat(value[r][8])+", "
									 +Float.parseFloat(value[r][9])+", "+Float.parseFloat(value[r][10])+", "+Float.parseFloat(value[r][11])+", "
									 +Float.parseFloat(value[r][12])+", "+Float.parseFloat(value[r][13])+", "+Float.parseFloat(value[r][14])+","
									 +Float.parseFloat(value[r][15])+","+Float.parseFloat(value[r][16])+", "+Float.parseFloat(value[r][17])+","
									 +Float.parseFloat(value[r][18])+",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'自动','','"+fileName+"')";
								 
								 con.getInsert(sql);
								 
								
							 }
						 }else if(leix[l].equals("1")){
							
	//						 String value[] = data.getData(Strsz, leix);
							 if(getProperId(getIDiancbmModel(),value[r][0],-1)==-1){
								 continue;
							 }
							 sql="insert into diaor01bb (id, riq, fenx, diancxxb_id, fadsbrl, meitsg, meithyhj, meithyfd, meithygr, meithyqt, meithysh, meitkc, shiysg, shiyhyhj, shiyhyfd,"
								 +" shiyhygr, shiyhyqt, shiyhysh, shiykc, fadl, gongrl, biaozmhfd, biaozmhgr, tianrmhfd, tianrmhgr, biaozmlfd, biaozmlgr, zonghrl, zonghm, diancscsj,jiessj,jiesr, beiz,diancscwjm)"
								 + " values("
								 +getTableId("diaor01bb")+",to_date('"+riq+"','yyyy-MM-dd'), '本月', "
								 +getProperId(getIDiancbmModel(),value[r][0],-1)+",'"+Float.parseFloat(value[r][1])+"',"
								 +Float.parseFloat(value[r][2])+", "+Float.parseFloat(value[r][3])+", "+Float.parseFloat(value[r][4])+", "
								 +Float.parseFloat(value[r][5])+", "+Float.parseFloat(value[r][6])+", "+Float.parseFloat(value[r][7])+", "
								 +Float.parseFloat(value[r][8])+", "
								 +Float.parseFloat(value[r][9])+", "+Float.parseFloat(value[r][10])+", "+Float.parseFloat(value[r][11])+", "
								 +Float.parseFloat(value[r][12])+", "+Float.parseFloat(value[r][13])+", "+Float.parseFloat(value[r][14])+", "
								 +Float.parseFloat(value[r][15])+","+Float.parseFloat(value[r][16])+","+Float.parseFloat(value[r][17])+", "
								 +Float.parseFloat(value[r][18])+","+Float.parseFloat(value[r][19])+", "+Float.parseFloat(value[r][20])+","
								 +Float.parseFloat(value[r][21])+", "+Float.parseFloat(value[r][22])+","+Float.parseFloat(value[r][23])+", "
								 +Float.parseFloat(value[r][24])+", "+Float.parseFloat(value[r][25])+",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'自动','','"+fileName+"')";
							 
							 con.getInsert(sql);
							 
							
						 }else if(leix[l].equals("3")){
							 
	//						 String value[] = data.getData(Strsz, leix);
							 long dianc_id = getProperId(getIDiancbmModel(),value[r][0],-1);
							 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
							 long Gongysb_id = getProperId(getIGongysbIdModel(),value[r][1],dianc_id);
							 
							 if(getProperId(getIDiancbmModel(),value[r][0],-1)==-1){
								 continue;
							 }
							 if(Gongysb_id==-1){
								 if(sbtmp.length()==0){
									 sbtmp.append(strdiancmc+"的供应商编码"+value[r][1]);
								 }else{
									 sbtmp.append(","+strdiancmc+"的供应商编码"+value[r][1]);
								 }
							 }else{
								 sql="insert into diaor03bb (id, riq, fenx, diancxxb_id, Gongysb_id, jincsl, choucsl, zhanjcm, guoh, jianc, yingdsl, yingdzje,"
									 +" kuid, kuidzje, suopsl, suopje, shuom, diancscsj,jiessj,jiesr, beiz,diancscwjm) values("
									 +getTableId("diaor03bb")+",to_date('"+riq+"','yyyy-MM-dd'), '本月', "
									 +getProperId(getIDiancbmModel(),value[r][0],-1)+","+getProperId(getIGongysbIdModel(),value[r][1],dianc_id)+", "
									 +Float.parseFloat(value[r][2])+", "+Float.parseFloat(value[r][3])+", "
									 +Float.parseFloat(value[r][4])+", "+Float.parseFloat(value[r][5])+", "+Float.parseFloat(value[r][6])+", "
									 +Float.parseFloat(value[r][7])+", "+Float.parseFloat(value[r][8])+", "+Float.parseFloat(value[r][9])+", "
									 +Float.parseFloat(value[r][10])+",0, "+Float.parseFloat(value[r][11])+", '',"
									 +" to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'自动','','"+fileName+"')" ;
									 
								 con.getInsert(sql);
								 
								
							 }
						 }else if(leix[l].equals("4")){
	//						 String value[] = data.getData(Strsz, leix);
							 long dianc_id = getProperId(getIDiancbmModel(),value[r][0],-1);
							 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
							 long Gongysb_id = getProperId(getIGongysbIdModel(),value[r][1],dianc_id);
							 
							 if(getProperId(getIDiancbmModel(),value[r][0],-1)==-1){
								 continue;
							 }
							 if(Gongysb_id==-1){
								 if(sbtmp.length()==0){
									 sbtmp.append(strdiancmc+"的供应商编码"+value[r][1]);
								 }else{
									 sbtmp.append(","+strdiancmc+"的供应商编码"+value[r][1]);
								 }
							 }else{
								 sql="insert into diaor04bb (id, riq, fenx, diancxxb_id, Gongysb_id, pinz, jincsl, yanssl, jianzl, kuangffrl, kuangfdj, kuangfsf, kuangfhf, kuangfhff,"
									 +" kuangflf, changffrl, changfdj, changfsf, changfhf, changfhff, changflf, dengjc, rejc, bufl, danjc, zongje, suopje, relsp, kuikspsl, liusp, liuspsl, diancscsj,jiessj,jiesr, beiz,diancscwjm)"
									 + " values("+getTableId("diaor04bb")+",to_date('"+riq+"','yyyy-MM-dd'), '本月', "
									 +getProperId(getIDiancbmModel(),value[r][0],-1)+","+getProperId(getIGongysbIdModel(),value[r][1],dianc_id)+",'', "
									 +Float.parseFloat(value[r][2])+", "+Float.parseFloat(value[r][3])+", "+Float.parseFloat(value[r][4])+", "
									 +Float.parseFloat(value[r][5])+", "+Float.parseFloat(value[r][6])+", "+Float.parseFloat(value[r][7])+", "
									 +Float.parseFloat(value[r][8])+", "+Float.parseFloat(value[r][9])+", "+Float.parseFloat(value[r][10])+", "
									 +Float.parseFloat(value[r][11])+", "+Float.parseFloat(value[r][12])+", "+Float.parseFloat(value[r][13])+", "
									 +Float.parseFloat(value[r][14])+","+Float.parseFloat(value[r][15])+","+Float.parseFloat(value[r][16])+", "
									 +Float.parseFloat(value[r][17])+","+Float.parseFloat(value[r][18])+", "+Float.parseFloat(value[r][19])+","
									 +Float.parseFloat(value[r][20])+", "+Float.parseFloat(value[r][21])+","
									 +Float.parseFloat(value[r][22])+", 0, 0, 0, 0 "
									 +",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'自动','','"+fileName+"')";
									 
								 con.getInsert(sql);
								 
							 }
						 }else if(leix[l].equals("8")){
	
							 long dianc_id = getProperId(getIDiancbmModel(),value[r][0],-1);
							 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
							 long Gongysb_id = getProperId(getIGongysbIdModel(),value[r][2],dianc_id);
							 
							 if(getProperId(getIDiancbmModel(),value[r][0],-1)==-1){
								 continue;
							 }
							 if(Gongysb_id==-1){
								 if(sbtmp.length()==0){
									 sbtmp.append(strdiancmc+"的供应商编码"+value[r][2]);
								 }else{
									 sbtmp.append(","+strdiancmc+"的供应商编码"+value[r][2]);
								 }
							 }else{
								 String haiysql = "select lx.mingc as leix from diancxxb dc,dianclbb lx where dc.dianclbb_id=lx.id and dc.bianm='"+value[r][0]+"'";
								 rs = con.getResultSet(haiysql);
								 if(rs.next()){
									if(rs.getString("leix").equals("水陆联运")){
										
										value[r][12]=String.valueOf(Float.parseFloat(value[r][8])+Float.parseFloat(value[r][12]));
										value[r][13]=String.valueOf(Float.parseFloat(value[r][9])+Float.parseFloat(value[r][13]));
										value[r][14]=String.valueOf(Float.parseFloat(value[r][10])+Float.parseFloat(value[r][14]));
										value[r][16]=String.valueOf(Float.parseFloat(value[r][7])+Float.parseFloat(value[r][16]));
										
										value[r][7]="0";
										value[r][8]="0";
										value[r][9]="0";
										value[r][10]="0";
										
									}else{
										value[r][5] = new DecimalFormat("0.00").format(Float.parseFloat(value[r][5])*1.13);
										value[r][8]= new DecimalFormat("0.00").format(Float.parseFloat(value[r][8])/0.93);
									}
								 }
								 rs.close();
								 con.closeRs();
								   
								 double daoczhj = 0;
								 String biaomdj = "0";
								 String buhsbmdj = "0";
								 if(Float.parseFloat(value[r][21])!=0){
								 daoczhj = Float.parseFloat(value[r][5])+Float.parseFloat(value[r][7])+Float.parseFloat(value[r][8])
												 +Float.parseFloat(value[r][10])+Float.parseFloat(value[r][11])+Float.parseFloat(value[r][12])
												 +Float.parseFloat(value[r][14])+Float.parseFloat(value[r][15])+Float.parseFloat(value[r][16])
												 +Float.parseFloat(value[r][17])+Float.parseFloat(value[r][18]);
								 
								 biaomdj = new DecimalFormat("0.00").format((Float.parseFloat(value[r][5])+Float.parseFloat(value[r][7])+Float.parseFloat(value[r][8])
												 +Float.parseFloat(value[r][10])+Float.parseFloat(value[r][11])+Float.parseFloat(value[r][12])
												 +Float.parseFloat(value[r][14])+Float.parseFloat(value[r][15])+Float.parseFloat(value[r][16])
												 +Float.parseFloat(value[r][17])+Float.parseFloat(value[r][18]))*29.271/Float.parseFloat(value[r][21]));
								 
								 buhsbmdj = new DecimalFormat("0.00").format((Float.parseFloat(value[r][5])+Float.parseFloat(value[r][7])+Float.parseFloat(value[r][8])
												 +Float.parseFloat(value[r][10])+Float.parseFloat(value[r][11])+Float.parseFloat(value[r][12])
												 +Float.parseFloat(value[r][14])+Float.parseFloat(value[r][15])+Float.parseFloat(value[r][16])
												 +Float.parseFloat(value[r][17])+Float.parseFloat(value[r][18])
												 -Float.parseFloat(value[r][6])-Float.parseFloat(value[r][9])
												 -Float.parseFloat(value[r][13]))*29.271/Float.parseFloat(value[r][21]));
								 }
								 
								 sql="insert into diaor08bb (id, riq, fenx, diancxxb_id, Gongysb_id, meil, daoczhj, kuangj, zengzse, jiaohqyzf, tielyf, tieyse, tielzf, shuillyf,"
									 +" shuiyf, shuiyse, shuiyzf, qiyf, gangzf, daozzf, qitfy, sunhl, sunhzhje, rez, biaomdj, buhsbmdj, meik, diancscsj, qiyse, beiz,jiessj,jiesr,diancscwjm)  values("
									 +getTableId("diaor08bb")+",to_date('"+riq+"','yyyy-MM-dd'), '本月', "
									 +getProperId(getIDiancbmModel(),value[r][0],-1)+","+getProperId(getIGongysbIdModel(),value[r][2],dianc_id)+","
									 +Integer.parseInt(value[r][3])+", "
									 
									 +daoczhj+", "
									 
									 +Float.parseFloat(value[r][5])+", "+Float.parseFloat(value[r][6])+", "+Float.parseFloat(value[r][7])+", "+Float.parseFloat(value[r][8])+", "
									 
									 +Float.parseFloat(value[r][9])+", "
									 
									 +Float.parseFloat(value[r][10])+", "+Float.parseFloat(value[r][11])+", "
									 +Float.parseFloat(value[r][12])+", "+Float.parseFloat(value[r][13])+", "+Float.parseFloat(value[r][14])+", "
									 +Float.parseFloat(value[r][15])+", "+Float.parseFloat(value[r][16])+","+Float.parseFloat(value[r][17])+","
									 +Float.parseFloat(value[r][18])+", "+Float.parseFloat(value[r][19])+","+Float.parseFloat(value[r][20])+", "
									 +Float.parseFloat(value[r][21])+","
									 
									 +Float.parseFloat(biaomdj)+","+Float.parseFloat(buhsbmdj)+","
									
									 +((Float.parseFloat(value[r][5])+Float.parseFloat(value[r][7])+Float.parseFloat(value[r][8])
											 +Float.parseFloat(value[r][10])+Float.parseFloat(value[r][11])+Float.parseFloat(value[r][12])
											 +Float.parseFloat(value[r][14])+Float.parseFloat(value[r][15])+Float.parseFloat(value[r][16])
											 +Float.parseFloat(value[r][17])+Float.parseFloat(value[r][18]))*Integer.parseInt(value[r][3]))
									 +",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'), 0, '',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'自动','"+fileName+"')";
									 
								 con.getInsert(sql);
								 
								
							 }
						 }
					 }
					 if(leix[l].equals("6")){
						 if (fileName.substring(3,5).equals("01")){
								sql="select diaor16bb.*  from diaor16bb where riq=to_date('"+ riq + "','yyyy-mm-dd')  and fenx='本月' ";
							}
							else{
								sql = "select diancxxb_id, Gongysb_id, '累计' as  fenx, sum(kuangfgyl) as kuangfgyl,sum(yuns) as yuns,sum(kuid) as kuid,"
									+ "sum(decode(fenx,'累计',yuemjc,0)) qickc,"
									+ "case when sum(kuangfgyl)=0 then 0 else round(sum(farl*kuangfgyl)/sum(kuangfgyl),2) end as farl,"
									+ "case when sum(kuangfgyl)=0 then 0 else round(sum(huif*kuangfgyl)/sum(kuangfgyl),2) end  huif,"
									+ "case when sum(kuangfgyl)=0 then 0 else round(sum(shuif*kuangfgyl)/sum(kuangfgyl),2) end as shuif,"
									+ "case when sum(kuangfgyl)=0 then 0 else round(sum(huiff*kuangfgyl)/sum(kuangfgyl),2) end  as huiff,"
									+ "sum(qitsrl)  as qitsrl,sum(shijhylfdy+shijhylgry+shijhylqty+shijhylzcsh) as shijhylhj, sum(shijhylfdy) as shijhylfdy,sum(shijhylgry) as shijhylgry,"
									+ "sum(shijhylqty) as shijhylqty,sum(shijhylzcsh) as shijhylzcsh, sum(diaocl) as diaocl,"
									+ "sum( panypk) as panypk,sum(decode(fenx,'本月',yuemjc,0)) as yuemjc from diaor16bb "
									+ "where  "
									+" ((riq=to_date('"+ riq + "','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"
									+ riq + "','yyyy-mm-dd'),-1) and fenx='累计')) group by diancxxb_id, Gongysb_id ";
							}
//						 		if((r+1)==strbf2.size()){
							if(con.getHasIt(sql)){
							 	rec=con.getResultSet(sql);
					 			sbsql.setLength(0);
					 			sbsql.append(" begin ");
						 		while(rec.next()) {
									sbsql.append("insert into diaor16bb (id, riq, fenx, diancxxb_id, Gongysb_id, pinz, shangyjc, kuangfgyl, yuns, kuid, farl, huif, shuif, huiff, qitsrl,"
										+" shijhylhj, shijhylfdy, shijhylgry, shijhylqty, shijhylzcsh, diaocl, panypk, yuemjc,diancscsj, beiz,diancscwjm)"
										+ "values("+getTableId("diaor16bb")
										+ ",to_date('"+ riq + "','yyyy-mm-dd'),"
										+ "'累计',"
										+ rec.getLong("diancxxb_id") + ","
										+ rec.getLong("Gongysb_id") + ",'',0,"
										
										+ rec.getDouble("kuangfgyl") + ","
										+ rec.getLong("yuns") + ","
										+ rec.getLong("kuid") + ","
										+ rec.getDouble("farl") + ","
										+ rec.getDouble("huif") + ","
										+ rec.getDouble("shuif") + ","
										+ rec.getDouble("huiff") + ","
										+ rec.getDouble("qitsrl") + ","
										+ rec.getDouble("shijhylhj") + ","
										+ rec.getDouble("shijhylfdy") + ","
										+ rec.getDouble("shijhylgry") + ","
										+ rec.getDouble("shijhylqty") + ","
										+ rec.getDouble("shijhylzcsh") + ","
										+ rec.getDouble("diaocl") + ","
										+ rec.getDouble("panypk") + ","
										+ rec.getDouble("yuemjc") +",'','','"+fileName+"');\n");
							 	}
						 		sbsql.append(" end; ");
						 		con.getInsert(sbsql.toString());
						 		rec.close();
						 		con.closeRs();
							
						 		sql = "select riq,diancxxb_id,gongysb_id from diaor16bb "
							    	 +"      where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='累计' and gongysb_id not in "
							    	 +"     (select gongysb_id from diaor16bb where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='本月') ";
						 		if(con.getHasIt(sql)){
						 			rec = con.getResultSet(sql);
							 		sbsql.setLength(0);
							 		sbsql.append("begin ");
								    while(rec.next()){
								    	sbsql.append("insert into diaor16bb (id, riq, fenx, diancxxb_id, Gongysb_id, pinz, shangyjc, kuangfgyl, yuns, kuid, farl, huif, shuif, huiff, qitsrl,"
											+" shijhylhj, shijhylfdy, shijhylgry, shijhylqty, shijhylzcsh, diaocl, panypk, yuemjc,diancscsj, beiz,diancscwjm)"
											+ "values("+getTableId("diaor16bb")
											+ ",to_date('"+ riq + "','yyyy-mm-dd'),'本月',"
											+ rec.getLong("diancxxb_id") + ","
											+ rec.getLong("Gongysb_id") + ",'',0,"
											+",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'','','"+fileName+"');\n");
								     }
								    sbsql.append(" end; ");
								    con.getInsert(sbsql.toString());
								    rec.close();	
								    con.closeRs();
						 		}
					 		}
					 }else if(leix[l].equals("1")){
						 if (fileName.substring(3,5).equals("01")){
								sql="select diaor01bb.*  from diaor01bb "  + "where riq=to_date('"+ riq
								+ "','yyyy-mm-dd')  and fenx='本月' ";
						}
						else{
							sql = "select diancxxb_id, '累计' as  fenx, sum(decode(fenx,'本月',fadsbrl,0)) as fadsbrl,sum(meitsg) as meitsg,"
						         + " sum( meithyhj) as meithyhj,sum(meithyfd) as meithyfd,sum(meithygr) as meithygr,sum(meithyqt) as meithyqt,"
						         + "sum(meithysh) as meithysh,sum(decode(fenx,'本月',meitkc,0)) as meitkc,sum(shiysg) as shiysg,sum(shiyhyhj) as shiyhyhj,"
						         + "sum(shiyhyfd) as shiyhyfd,sum(shiyhygr) as shiyhygr, sum(shiyhyqt) as shiyhyqt,sum(shiyhysh) as shiyhysh,"
						         + " sum(decode(fenx,'本月',shiykc,0)) as shiykc,sum(fadl) as fadl,sum(gongrl) as gongrl,"
						         + " decode(sum(fadl),0,0,round(sum(biaozmlfd)/sum(fadl)*100)) as biaozmhfd,"
						         + " decode(sum(gongrl),0,0,round(sum(biaozmlgr)/sum(gongrl)*1000,2)) as biaozmhgr,"
						         + " decode(sum(fadl),0,0,round((sum(shiyhyfd)*2+sum(meithyfd))/sum(fadl)*100)) as tianrmhfd,"
						         + " decode(sum(gongrl),0,0,round((sum(shiyhygr)*2+sum(meithygr))/sum(gongrl)*1000,2)) as tianrmhgr,"
						         + " decode(sum(tianrmhfd),0,0,round((sum(shiyhyfd) * 2 + sum(meithyfd)) / sum(tianrmhfd) * 100)) as tianrmhgd,"
						         + " sum( biaozmlfd) as biaozmlfd,sum(biaozmlgr) as biaozmlgr,"
						         + " decode((sum(shiyhyfd)*2+sum(meithyfd)),0,0,round(sum(biaozmlfd)*7000*0.0041816/(sum(shiyhyfd)*2+sum(meithyfd)),2)) as zonghrl,"
						         + " decode(sum(meithyfd),0,0,round((sum(biaozmlfd)*7000-sum(shiyhyfd)*10000)*0.0041816/sum(meithyfd),2)) as zonghm from diaor01bb "
						         + " where ((riq=to_date('"+ riq+ "','yyyy-mm-dd')  and fenx='本月') "
						         + " or (riq=add_months(to_date('"
								 + riq+ "','yyyy-mm-dd'),-1) and fenx='累计')) group by diancxxb_id";
					     }
					     //保存重新计算的累计数据
//					     if((r+1)==strbf2.size()){
						 if(con.getHasIt(sql)){
					    	 rec = con.getResultSet(sql);
					    	 sbsql.setLength(0);
					    	 sbsql.append("begin ");
						     while(rec.next()) {
						       sbsql.append("insert into diaor01bb (id, riq, fenx, diancxxb_id, fadsbrl, meitsg, meithyhj, meithyfd, meithygr, meithyqt, meithysh, meitkc, shiysg, shiyhyhj,"
						    	   +" shiyhyfd, shiyhygr, shiyhyqt, shiyhysh, shiykc, fadl, gongrl, biaozmhfd, biaozmhgr, tianrmhfd, tianrmhgr, biaozmlfd, biaozmlgr, zonghrl, zonghm, diancscsj, beiz,diancscwjm)"
						           + "values("+getTableId("diaor01bb")
						           + ",to_date('"+riq+ "','yyyy-mm-dd'),"+ "'累计',"
						           + rec.getLong("diancxxb_id") + ","
						           
						           + rec.getDouble("fadsbrl") + ","
						           + rec.getDouble("meitsg") + ","
						           + rec.getDouble("meithyhj") + ","
						           + rec.getDouble("meithyfd") + ","
						           + rec.getDouble("meithygr") + ","
						           + rec.getDouble("meithyqt") + ","
						           + rec.getDouble("meithysh") + ","
						           + rec.getDouble("meitkc") + ","
						           + rec.getDouble("shiysg") + ","
						           + rec.getDouble("shiyhyhj") + ","
						           + rec.getDouble("shiyhyfd") + ","
						           + rec.getDouble("shiyhygr") + ","
						           + rec.getDouble("shiyhyqt") + ","
						           + rec.getDouble("shiyhysh") + ","
						           + rec.getDouble("shiykc") + ","
						           + rec.getDouble("fadl") + ","
						           + rec.getDouble("gongrl") + ","
						           + rec.getDouble("biaozmhfd") + ","
						           + rec.getDouble("biaozmhgr") + ","
						           + rec.getDouble("tianrmhfd") + ","
						           + rec.getDouble("tianrmhgr") + ","
						           + rec.getDouble("biaozmlfd") + ","
						           + rec.getDouble("biaozmlgr") + ","
						           + rec.getDouble("zonghrl") + ","
						           + rec.getDouble("zonghm") 
						           +",'','','"+fileName+"');\n");
					           
						     }
						     sbsql.append(" end; ");
						     con.getInsert(sbsql.toString());
						     rec.close();
						     con.closeRs();
					     }
					 }else if(leix[l].equals("3")){
						 if (fileName.substring(3,5).equals("01")){
								sql="select diaor03bb.*  from diaor03bb "  + "where riq=to_date('"+ riq + "','yyyy-mm-dd')  and fenx='本月'";
						}
						else{
							sql = "select diancxxb_id,Gongysb_id,'累计' as fenx,sum(jincsl) as jincsl,sum(choucsl) as choucsl,"
								 +"decode(sum(jincsl),0,0,round(sum(choucsl)/sum(jincsl)*100,2)) as zhanjcm,"
						          + " decode(sum(jincsl),0,0,round(sum(jincsl*guoh)/sum(jincsl),2)) as guoh,(100-decode(sum(jincsl),0,0,round(sum(jincsl*guoh)/sum(jincsl),2))) as jianc,"
						          +" sum(yingdsl) as yingdsl,sum(yingdzje) as yingdzje,"
						          +"sum(kuid) as kuid,sum(kuidzje) as kuidzje,"
						          + " sum(suopsl) as suopsl,sum(suopje) as suopje"
						         + " from diaor03bb where ((riq=to_date('"+riq
						         + "','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"+ riq
						         + "','yyyy-mm-dd'),-1) and fenx='累计')) "
						         +" group by diancxxb_id,Gongysb_id";
					     }
					     //保存重新计算的累计数据
//					     if((r+1)==strbf2.size()){
						 if(con.getHasIt(sql)){
						 	sbsql.setLength(0);
						 	sbsql.append(" begin \n");
					    	 rec = con.getResultSet(sql);
						     while(rec.next()) {
						       sbsql.append("insert into diaor03bb (id, riq, fenx, diancxxb_id, Gongysb_id, jincsl, choucsl, zhanjcm, guoh,"
						    	   +" jianc, yingdsl, yingdzje, kuid, kuidzje, suopsl, suopje, shuom, diancscsj, beiz,diancscwjm) "
						           + "values("+getTableId("diaor03bb")
						           + ",to_date('"+ riq+ "','yyyy-mm-dd'),'累计',"
						           + rec.getString("diancxxb_id") + ","
						           + rec.getString("Gongysb_id") + ","
						           + rec.getFloat("jincsl") + ","
						           + rec.getFloat("choucsl") + ","
						           + rec.getFloat("zhanjcm") + ","
						           + rec.getFloat("guoh") + ","
						           + rec.getFloat("jianc") + ","
						           + rec.getFloat("yingdsl") + ","
						           + rec.getFloat("yingdzje") + ","
						           + rec.getFloat("kuid") + ","
						           + rec.getFloat("kuidzje") + ","
						           + rec.getFloat("suopsl") + ","
						           + rec.getFloat("suopje") + ",'','','','"+fileName+"');\n");
						     
						     }
						     sbsql.append(" end; ");
						     con.getInsert(sbsql.toString());
						     rec.close();
						     con.closeRs();
						 
						     sql = "select riq,diancxxb_id,gongysb_id from diaor03bb "
						    	 +"      where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='累计' and gongysb_id not in "
						    	 +"     (select gongysb_id from diaor03bb where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='本月') ";
						     if(con.getHasIt(sql)){
						    	 sbsql.setLength(0);
						    	 sbsql.append("begin ");
							     rec = con.getResultSet(sql);
							     while(rec.next()){
							    	 sbsql.append("insert into diaor03bb (id, riq, fenx, diancxxb_id, Gongysb_id, jincsl, choucsl, zhanjcm, guoh,"
								    	   +" jianc, yingdsl, yingdzje, kuid, kuidzje, suopsl, suopje, shuom, diancscsj, beiz,diancscwjm) "
								           + "values("+getTableId("diaor03bb")
								           + ",to_date('"+ riq+ "','yyyy-mm-dd'),'本月',"
								           + rec.getString("diancxxb_id") + ","
								           + rec.getString("Gongysb_id") + ","
										 +",0,0,0,0,0,0,0,0,0,0,0,0,'','','"+fileName+"');");
							     }
							     sbsql.append(" end; ");
							     con.getInsert(sbsql.toString());
							     rec.close();
							     con.closeRs();
						     }
						 }
					 }else if(leix[l].equals("4")){
						 if (fileName.substring(3,5).equals("01")){
							 sql = "select diancxxb_id, Gongysb_id,'累计' as  fenx, sum(jincsl) as jincsl,sum( yanssl) as yanssl,"
						         + "decode(sum(jincsl),0,0,round(sum(yanssl)/sum(jincsl)*100,2)) as jianzl,decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2)) as changffrl, "
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2)) as kuangffrl,dengji(decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2))) as kuangfdj,"
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*kuangfsf)/sum(jincsl),2)) as kuangfsf,decode(sum(jincsl),0,0,round(sum(jincsl*kuangfhf)/sum(jincsl),2)) as kuangfhf,"
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*kuangfhff)/sum(jincsl),2)) as kuangfhff,decode(sum(jincsl),0,0,round(sum(jincsl*kuangflf)/sum(jincsl),2)) as kuangflf,"
						         + "dengji(decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2))) as changfdj,decode(sum(jincsl),0,0,round(sum(jincsl*changfsf)/sum(jincsl),2)) as changfsf,"
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*changfhf)/sum(jincsl),2)) as changfhf, "
						         + " decode(sum(jincsl),0,0,round(sum(jincsl*changfhff)/sum(jincsl),2)) as changfhff,decode(sum(jincsl),0,0,round(sum(jincsl*changflf)/sum(jincsl),2)) as changflf,"
						         + "nvl(dengji(decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2))),0)-nvl(dengji(decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2))),0) as dengjc,"
						         
//						         + "decode(sum(jincsl),0,0,round(sum(jincsl*aad)/sum(jincsl),2)) as aad,sum(shuifsp) as shuifzje,sum(huifsp) as huifzje,sum(huiffsp) as huiffzje,"
						         
						         + " nvl(decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2)),0)-nvl(decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2)),0) as rejc,sum(relsp) as relsp,"
						         + "sum(liusp) as liusp,decode(sum(yanssl),0,0,round(sum(yanssl*bufl)/sum(yanssl),2)) as bufl,"
						         + "decode(sum(yanssl*bufl),0,0,round(sum(zongje)/sum(yanssl*bufl/100),2)) as danjc,sum(zongje) as zongje,sum(suopje) as suopje"
						         + " from diaor04bb where riq=to_date('"+riq+ "','yyyy-mm-dd')  and fenx='本月'"
						         +" group by diancxxb_id,Gongysb_id";
						}
						else{
							sql = "select diancxxb_id, Gongysb_id,'累计' as  fenx, sum(jincsl) as jincsl,sum( yanssl) as yanssl,"
						         + "decode(sum(jincsl),0,0,round(sum(yanssl)/sum(jincsl)*100,2)) as jianzl,decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2)) as changffrl, "
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2)) as kuangffrl,dengji(decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2))) as kuangfdj,"
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*kuangfsf)/sum(jincsl),2)) as kuangfsf,decode(sum(jincsl),0,0,round(sum(jincsl*kuangfhf)/sum(jincsl),2)) as kuangfhf,"
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*kuangfhff)/sum(jincsl),2)) as kuangfhff,decode(sum(jincsl),0,0,round(sum(jincsl*kuangflf)/sum(jincsl),2)) as kuangflf,"
						         + "dengji(decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2))) as changfdj,decode(sum(jincsl),0,0,round(sum(jincsl*changfsf)/sum(jincsl),2)) as changfsf,"
						         + "decode(sum(jincsl),0,0,round(sum(jincsl*changfhf)/sum(jincsl),2)) as changfhf, "
						         + " decode(sum(jincsl),0,0,round(sum(jincsl*changfhff)/sum(jincsl),2)) as changfhff,decode(sum(jincsl),0,0,round(sum(jincsl*changflf)/sum(jincsl),2)) as changflf,"
						         + "nvl(dengji(decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2))),0)-nvl(dengji(decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2))),0) as dengjc,"
						         
//						         + "decode(sum(jincsl),0,0,round(sum(jincsl*aad)/sum(jincsl),2)) as aad,sum(shuifsp) as shuifzje,sum(huifsp) as huifzje,sum(huiffsp) as huiffzje,"
						         
						         + " nvl(decode(sum(jincsl),0,0,round(sum(jincsl*changffrl)/sum(jincsl),2)),0)-nvl(decode(sum(jincsl),0,0,round(sum(jincsl*kuangffrl)/sum(jincsl),2)),0) as rejc,"
						         + "sum(relsp) as relsp,sum(liusp) as liusp,decode(sum(yanssl),0,0,round(sum(yanssl*bufl)/sum(yanssl),2)) as bufl,"
						         + "decode(sum(yanssl*bufl),0,0,round(sum(zongje)/sum(yanssl*bufl/100),2)) as danjc,sum(zongje) as zongje,sum(suopje) as suopje"
						         + " from diaor04bb where ((riq=to_date('"+riq+ "','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"+riq
						         + "','yyyy-mm-dd'),-1) and fenx='累计'))  group by diancxxb_id,Gongysb_id";
					     }
					     //保存重新计算的累计数据
//					     if((r+1)==strbf2.size()){
						 if(con.getHasIt(sql)){
							 sbsql.setLength(0);
							 sbsql.append("begin ");
					    	 rec = con.getResultSet(sql);
						     while(rec.next()) {
						       sbsql.append("insert into diaor04bb (id, riq, fenx, diancxxb_id, Gongysb_id, pinz, jincsl, yanssl, jianzl, kuangffrl, kuangfdj, kuangfsf, kuangfhf, kuangfhff, kuangflf,"
						    	   +" changffrl, changfdj, changfsf, changfhf, changfhff, changflf, dengjc, rejc, bufl, danjc, zongje, suopje, relsp, kuikspsl, liusp, liuspsl, diancscsj, beiz,diancscwjm) "
						           + "values("+getTableId("diaor04bb")
						           + ",to_date('"+riq+ "','yyyy-mm-dd'),'累计',"
						           + rec.getString("diancxxb_id") + ","
						           + rec.getString("Gongysb_id") + ",'',"
						           
						           + rec.getLong("jincsl") + ","
						           + rec.getLong("yanssl") + ","
						           + rec.getFloat("jianzl") + ","
						           + rec.getFloat("kuangffrl") + ","
						           + rec.getFloat("kuangfdj") + ","
						           + rec.getFloat("kuangfsf") + ","
						           + rec.getFloat("kuangfhf") + ","
						           + rec.getFloat("kuangfhff") + ","
						           + rec.getFloat("kuangflf") + ","
						           + rec.getFloat("changffrl") + ","
						           + rec.getFloat("changfdj") + ","
						           + rec.getFloat("changfsf") + ","
						           + rec.getFloat("changfhf") + ","
						           + rec.getFloat("changfhff") + ","
						           + rec.getFloat("changflf") + ","
						           + rec.getFloat("dengjc") + ","
						           + rec.getFloat("rejc") + ","
						           + rec.getFloat("bufl") + ","
						           + rec.getFloat("danjc") + ","
						           + rec.getFloat("zongje") + ","
						           + rec.getFloat("suopje") + ","
						           + rec.getFloat("relsp") 
						           + ",0,"
						           + rec.getFloat("liusp") 
						           + ",0,'','','"+fileName+"');\n");
	
						     }
						     sbsql.append(" end; ");
						     con.getInsert(sbsql.toString());
						     rec.close();
						     con.closeRs();
						     sql = "select riq,diancxxb_id,gongysb_id from diaor04bb "
						    	 +"      where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='累计' and gongysb_id not in "
						    	 +"     (select gongysb_id from diaor04bb where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='本月') ";
						     if(con.getHasIt(sql)){
						    	 sbsql.setLength(0);
						    	 sbsql.append("begin ");
							     rec = con.getResultSet(sql);
							     while(rec.next()){
							    	 sbsql.append("insert into diaor04bb (id, riq, fenx, diancxxb_id, Gongysb_id, pinz, jincsl, yanssl, jianzl, kuangffrl, kuangfdj, kuangfsf, kuangfhf, kuangfhff,"
										 + " kuangflf, changffrl, changfdj, changfsf, changfhf, changfhff, changflf, dengjc, rejc, bufl, danjc, zongje, suopje, relsp, kuikspsl, liusp, liuspsl, diancscsj,jiessj,jiesr, beiz,diancscwjm)"
										 + " values("+getTableId("diaor04bb")+",to_date('"+riq+"','yyyy-MM-dd'), '本月', "
										 +rec.getLong("diancxxb_id")
										 + ","
										 +rec.getLong("gongysb_id")
										 +",'',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'','','','','"+fileName+"');\n");
							     }
							     sbsql.append(" end; ");
							     con.getInsert(sbsql.toString());
							     rec.close();
							     con.closeRs();
						     }
					     }
					 }else if(leix[l].equals("8")){
						 if (fileName.substring(3,5).equals("01")){
							 sql="select diaor08bb.*  from diaor08bb "  + "where riq=to_date('"+riq+ "','yyyy-mm-dd')  and fenx='本月' ";
						}
						else{
							sql = "select a.*,kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy as daoczhj,"
						       +"round(decode(rez,0,0,(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy)*29.271/rez),2) as biaomdj,"
						       +"round(decode(rez,0,0,(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy-zengzse-tieyse-shuiyse)*29.271/rez),2) as buhsbmdj,"
						       +"(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy)*meil as meik" 
							   +" from ("
							       +"select diancxxb_id,Gongysb_id,'累计' as fenx,sum(meil) as meil," 
//								  +"decode(sum(meil),0,0,round(sum(meil*daoczhj)/sum(meil),2)) as daoczhj,"
						           + " decode(sum(meil),0,0,round(sum(meil*kuangj)/sum(meil),2)) as kuangj,decode(sum(meil),0,0,round(sum(meil*zengzse)/sum(meil),2)) as zengzse,"
						           + " decode(sum(meil),0,0,round(sum(meil*jiaohqyzf)/sum(meil),2)) as jiaohqyzf,decode(sum(meil),0,0,round(sum(meil*tielyf)/sum(meil),2)) as tielyf,"
						           + " decode(sum(meil),0,0,round(sum(meil*tieyse)/sum(meil),2)) as tieyse,decode(sum(meil),0,0,round(sum(meil*tielzf)/sum(meil),2)) as tielzf,"
						           + " decode(sum(meil),0,0,round(sum(meil*shuillyf)/sum(meil),2)) as shuillyf,decode(sum(meil),0,0,round(sum(meil*shuiyf)/sum(meil),2)) as shuiyf,"
						           + " decode(sum(meil),0,0,round(sum(meil*shuiyse)/sum(meil),2)) as shuiyse,decode(sum(meil),0,0,round(sum(meil*shuiyzf)/sum(meil),2)) as shuiyzf,"
						           + " decode(sum(meil),0,0,round(sum(meil*qiyf)/sum(meil),2)) as qiyf,decode(sum(meil),0,0,round(sum(meil*gangzf)/sum(meil),2)) as gangzf,"
						           + " decode(sum(meil),0,0,round(sum(meil*daozzf)/sum(meil),2)) as daozzf,decode(sum(meil),0,0,round(sum(meil*qitfy)/sum(meil),2)) as qitfy,"
						           + " decode(sum(meil),0,0,round(sum(meil*sunhl)/sum(meil),2)) as sunhl,decode(sum(meil),0,0,round(sum(meil*sunhzhje)/sum(meil),2)) as sunhzhje,"
						           + " decode(sum(meil),0,0,round(sum(meil*rez)/sum(meil),2)) as rez,"
//						          +"decode(sum(meil),0,0,round(sum(meil*biaomdj)/sum(meil),2)) as biaomdj,"
//						          + " (decode(sum(meil),0,0,round(sum(meil*biaomdj)/sum(meil),2))-decode(sum(meil),0,0,round(sum(meil*zengzse)/sum(meil),2))" 
//						          +		"-decode(sum(meil),0,0,round(sum(meil*tieyse)/sum(meil),2))-decode(sum(meil),0,0,round(sum(meil*shuiyse)/sum(meil),2))) as buhsbmdj, "
//						          + " (sum(meil)*decode(sum(meil),0,0,round(sum(meil*daoczhj)/sum(meil),2))) as meik,"
						          + "decode(sum(meil),0,0,round(sum(meil*qiyse)/sum(meil),2)) as qiyse "
						          + " from diaor08bb  where ((riq=to_date('"+riq+ "','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"+ riq+ "','yyyy-mm-dd'),-1) " 
						          + " and fenx='累计')) group by diancxxb_id,Gongysb_id) a ";
					     }
					     //保存重新计算的累计数据
//					     if((r+1)==strbf2.size()){
						if(con.getHasIt(sql)){
							sbsql.setLength(0);
							sbsql.append("begin ");
					    	 rec = con.getResultSet(sql);
						     while(rec.next()) {
						       sbsql.append("insert into diaor08bb (id, riq, fenx, diancxxb_id, Gongysb_id, meil, daoczhj, kuangj, zengzse, jiaohqyzf, tielyf, tieyse,"
						    	   +" tielzf, shuillyf, shuiyf, shuiyse, shuiyzf, qiyf, gangzf, daozzf, qitfy, sunhl, sunhzhje, rez, biaomdj, buhsbmdj, meik, diancscsj, qiyse, beiz,diancscwjm) "
						           + "values("+getTableId("diaor08bb")
						           + ",to_date('"+riq+ "','yyyy-mm-dd'),'累计',"
						           + rec.getString("diancxxb_id") + ","
						           + rec.getString("Gongysb_id") + ","
						           + rec.getFloat("meil") + ","
						           + rec.getFloat("daoczhj") + ","
						           + rec.getFloat("kuangj") + ","
						           + rec.getFloat("zengzse") + ","
						           + rec.getFloat("jiaohqyzf") + ","
						           + rec.getFloat("tielyf") + ","
						           + rec.getFloat("tieyse") + ","
						           + rec.getFloat("tielzf") + ","
						           + rec.getFloat("shuillyf") + ","
						           + rec.getFloat("shuiyf") + ","
						           + rec.getFloat("shuiyse") + ","
						           + rec.getFloat("shuiyzf") + ","
						           + rec.getFloat("qiyf") + ","
						           + rec.getFloat("gangzf") + ","
						           + rec.getFloat("daozzf") + ","
						           + rec.getFloat("qitfy") + ","
						           + rec.getFloat("sunhl") + ","
						           + rec.getFloat("sunhzhje") + ","
						           + rec.getFloat("rez") + ","
						           + rec.getFloat("biaomdj") + ","
						           + rec.getFloat("buhsbmdj") +"," 
						           + rec.getFloat("meik")+",'',"
						           + rec.getFloat("qiyse")
						           +",'','"+fileName+"');\n");
					           
						     }
						     sbsql.append(" end; ");
						     con.getInsert(sbsql.toString());
						     rec.close();
						     con.closeRs();
						     sql = "select riq,diancxxb_id,gongysb_id from diaor08bb "
						    	 +"      where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='累计' and gongysb_id not in "
						    	 +"     (select gongysb_id from diaor08bb where riq=to_date('"+riq+"','yyyy-mm-dd') and fenx='本月') ";
						     if(con.getHasIt(sql)){
						    	 sbsql.setLength(0);
						    	 sbsql.append("begin ");
							     rec = con.getResultSet(sql);
							     while(rec.next()){
							    	 sbsql.append("insert into diaor08bb (id, riq, fenx, diancxxb_id, Gongysb_id, meil, daoczhj, kuangj, zengzse, jiaohqyzf, tielyf, tieyse,"
								    	   +" tielzf, shuillyf, shuiyf, shuiyse, shuiyzf, qiyf, gangzf, daozzf, qitfy, sunhl, sunhzhje, rez, biaomdj, buhsbmdj, meik, diancscsj, qiyse, beiz,diancscwjm) "
								           + "values("+getTableId("diaor08bb")
								           + ",to_date('"+riq+ "','yyyy-mm-dd'),'本月',"
								           + rec.getString("diancxxb_id") + ","
								           + rec.getString("Gongysb_id") + ","
										 +",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'',0,'','"+fileName+"');\n");
							     }
							     sbsql.append(" end; ");
							     con.getInsert(sbsql.toString());
							     rec.close();
							     con.closeRs();
						     }
					     }
					 }
					 
					 x=0;
					 if(sbtmp.length()==0){
			 			 con.commit();
			 			System.out.println(fileName+"取数完成！");
			 		 }else{
			 			 con.rollBack();
			 			 System.out.println(fileName+"中"+sbtmp.toString()+"不存在.该电厂取数失败！");
			 		 }
					 /*
					 if(result>=0 || strbf2.size()==0){
						 File fl=new File(strbf.get(i).toString());
						 File f2=new File(benflj+"/"+strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1));
						 if(f2.exists()){
							 f2.delete();
							 fl.renameTo(new File(benflj,strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1)));
							
						 }else{
							 fl.renameTo(new File(benflj,strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1)));
						 }
						 fl.delete();
					 }*/
				 }
			 }
		 }catch(Exception e){
			 con.rollBack();
	 		 e.printStackTrace();
		 }finally{
			 con.Close();
		 }
	}
	
	private long getTableId(String table){
		JDBCcon con = new JDBCcon();
		long tableid = -1;
        try {
            String sql = "select xl_" + table + "_id.nextval as id from dual";
            ResultSet rs = con.getResultSet(sql); 
            if(rs.next()){
            	tableid = rs.getLong("id");
            }
            
            rs.close();
            con.closeRs();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            con.Close();
        }
        return tableid;
	}
	
	private long getProperId(IPropertySelectionModel _selectModel, String value,long diancID) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		
		if(value.equals("111311")&&diancID==211){
			value="111306";
		}
		
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}
//	电厂编码
	public boolean _diancbmchange = false;
	private IDropDownBean _DiancbmValue;

	public IDropDownBean getDiancbmValue() {
		if(_DiancbmValue==null){
			_DiancbmValue=(IDropDownBean)getIDiancbmModels().getOption(0);
		}
		return _DiancbmValue;
	}

	public void setDiancbmValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancbmValue != null) {
			id = _DiancbmValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancbmchange = true;
			} else {
				_diancbmchange = false;
			}
		}
		_DiancbmValue = Value;
	}

	private IPropertySelectionModel _IDiancbmModel;

	public void setIDiancbmModel(IPropertySelectionModel value) {
		_IDiancbmModel = value;
	}

	public IPropertySelectionModel getIDiancbmModel() {
		if (_IDiancbmModel == null) {
			getIDiancbmModels();
		}
		return _IDiancbmModel;
	}

	public IPropertySelectionModel getIDiancbmModels() {
		
		String sql="";
		
		sql = "select d.id,d.bianm from diancxxb d order by d.mingc";
		
		_IDiancbmModel = new IDropDownModel(sql);
		return _IDiancbmModel;
	}
//	电厂名称
	public boolean _diancchange = false;
	private IDropDownBean _DiancValue;

	public IDropDownBean getDiancValue() {
		if(_DiancValue==null){
			_DiancValue=(IDropDownBean)getIDiancModels().getOption(0);
		}
		return _DiancValue;
	}

	public void setDiancValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancValue != null) {
			id = _DiancValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancchange = true;
			} else {
				_diancchange = false;
			}
		}
		_DiancValue = Value;
	}

	private IPropertySelectionModel _IDiancModel;

	public void setIDiancModel(IPropertySelectionModel value) {
		_IDiancModel = value;
	}

	public IPropertySelectionModel getIDiancModel() {
		if (_IDiancModel == null) {
			getIDiancModels();
		}
		return _IDiancModel;
	}

	public IPropertySelectionModel getIDiancModels() {
		
		String sql="";
		
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
//		System.out.println(sql);
		
		_IDiancModel = new IDropDownModel(sql);
		return _IDiancModel;
	}
	
//	供应商地区_id	
	private IPropertySelectionModel _IGongysbIdModel;
	public IPropertySelectionModel getIGongysbIdModel() {
		if (_IGongysbIdModel == null) {
			getIGongysbIdModels();
		}
		return _IGongysbIdModel;
	}
	
	public IPropertySelectionModel getIGongysbIdModels() {
		
		String sql="";
		
		sql = "select id,bianm from gongysb order by mingc";
		
		_IGongysbIdModel = new IDropDownModel(sql);
		return _IGongysbIdModel;
	}
	
}
