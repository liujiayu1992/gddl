package com.zhiren.gs.bjdt.monthreport;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.ibm.icu.text.DecimalFormat;
import com.zhiren.common.DateUtil;
import com.zhiren.common.filejx.FileJx;
import com.zhiren.common.filejx.FilePathRead;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.gs.bjdt.monthreport.CreateData;

public class CreateData {

	public String Create(long diancID,String leix,String filePath,String nianf,String yuef,String jiesry) {

		 JDBCcon con=new JDBCcon();
		 con.setAutoCommit(false);
		 String strMsg = "";
		 
		 String strLeix = leix;
		 String strFilePath = filePath;
		 String strNianf = nianf;
		 String strYuef = yuef;
		 String strRiq = strNianf +"-"+ strYuef +"-01";
		 String strJiesry = jiesry;
		 String strFileName = "";
		 
		 String Strsz = "";
		 ArrayList strbf=new ArrayList();
		 ArrayList strbf2=new ArrayList();
		 CreateDataFormat cdf = new CreateDataFormat();
		 String sql = "";
		 try{
			 if(diancID!=-1){
				 String diancbm = "";
				 String bmsql = "select bianm from diancxxb where id="+diancID;
				 ResultSet rsbm = con.getResultSet(bmsql);
				 if(rsbm.next()){
					 diancbm = rsbm.getString("bianm");
				 }
				 rsbm.close();
				 strLeix=strLeix+diancbm.substring(0,1)+diancbm.substring(3)+"M";
			 }
			 FileJx wjjx=new FileJx();
			 FilePathRead jx=new FilePathRead(strLeix,strFilePath,true);
			 strbf=jx.getTxtFileList();//得到文件列表
			 int x=0;	
			 
			 StringBuffer sb = new StringBuffer();
			 StringBuffer sbtmp = new StringBuffer();
			 sb.append("");
			 for(int j=0;j<strbf.size();j++){
				 sbtmp.setLength(0);
				 strbf2=wjjx.TextJx(strbf.get(j).toString());//一个文件
				 String shangcsj = wjjx.getWenjrq(strbf.get(j).toString());
				 strFileName = strbf.get(j).toString().substring(strbf.get(j).toString().lastIndexOf("\\")+1);
				 for(int i=0;i<strbf2.size();i++){
					 
					 Strsz=strbf2.get(i).toString();
					 String value[] = new String[]{};
					 if(Strsz.equals("")){//空行数据不处理
						 continue;
					 }else{
						 value = cdf.getData(Strsz, strLeix);
					 }
					 if(x==0){
						 if(strLeix.substring(0, 1).equals("6")){
							 sql="delete from diaor16bb where diancxxb_id="+getProperId(getIDiancbmModel(),value[0],-1)+" and riq=to_date('"+strRiq+"','yyyy-MM-dd')";
						 }else if(leix.substring(0, 1).equals("0")){
							 sql="delete from diaor08bb_new where diancxxb_id="+getProperId(getIDiancbmModel(),value[0],-1)+" and riq=to_date('"+strRiq+"','yyyy-MM-dd')";
						 }else{
							 sql="delete from diaor0"+strLeix.substring(0, 1)+"bb where diancxxb_id="+getProperId(getIDiancbmModel(),value[0],-1)+" and riq=to_date('"+strRiq+"','yyyy-MM-dd')";
						 }
						 con.getDelete(sql);
						 con.commit();
						 x++;
					 }
					 if(strLeix.substring(0, 1).equals("6")){
						 
						 long dianc_id = getProperId(getIDiancbmModel(),value[0],-1);
						 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
						 long gongysb_id = getProperId(getIMeikdqIdModel(),value[2],dianc_id);
						 if(gongysb_id==-1){
							 if(sbtmp.length()==0){
								 sbtmp.append(strdiancmc+"的煤矿编码"+value[2]);
							 }else{
								 sbtmp.append(","+strdiancmc+"的煤矿编码"+value[2]);
							 }
						 }else{
							 sql="insert into diaor16bb (id, riq, fenx, diancxxb_id, gongysb_id, pinz, shangyjc, kuangfgyl, yuns, kuid, farl, huif, shuif, huiff, qitsrl,"
								 +" shijhylhj, shijhylfdy, shijhylgry, shijhylqty, shijhylzcsh, diaocl, panypk, yuemjc,diancscsj, beiz,jiessj, jiesr, diancscwjm) values("
								 +"getnewid("+getProperId(getIDiancbmModel(),value[0],-1)+"),to_date('"+strRiq+"','yyyy-MM-dd'), '本月', "
								 +getProperId(getIDiancbmModel(),value[0],-1)+","+getProperId(getIMeikdqIdModel(),value[2],dianc_id)+", '',"
								 +Float.parseFloat(value[3])+", "+Float.parseFloat(value[4])+", 0, "+Float.parseFloat(value[5])+", "
								 +Float.parseFloat(value[6])+", "+Float.parseFloat(value[7])+", "+Float.parseFloat(value[8])+", "
								 +Float.parseFloat(value[9])+", "+Float.parseFloat(value[10])+", "+Float.parseFloat(value[11])+", "
								 +Float.parseFloat(value[12])+", "+Float.parseFloat(value[13])+", "+Float.parseFloat(value[14])+","
								 +Float.parseFloat(value[15])+","+Float.parseFloat(value[16])+", "+Float.parseFloat(value[17])+","
								 +Float.parseFloat(value[18])+",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),'',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
							 
							 con.getInsert(sql);
							 
							 if (strYuef.equals("1")){
									sql="select diaor16bb.*  from diaor16bb "  + "where riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月' and DIANCXXB_ID = " + getProperId(getIDiancbmModel(),value[0],-1);
								}
								else{
									sql = "select diancxxb_id, gongysb_id, '累计' as  fenx, sum(kuangfgyl) as kuangfgyl,sum(yuns) as yuns,sum(kuid) as kuid,"
										+ "sum(decode(fenx,'累计',yuemjc,0)) qickc,"
										+ "case when sum(kuangfgyl)=0 then 0 else round(sum(farl*kuangfgyl)/sum(kuangfgyl),2) end as farl,"
										+ "case when sum(kuangfgyl)=0 then 0 else round(sum(huif*kuangfgyl)/sum(kuangfgyl),2) end  huif,"
										+ "case when sum(kuangfgyl)=0 then 0 else round(sum(shuif*kuangfgyl)/sum(kuangfgyl),2) end as shuif,"
										+ "case when sum(kuangfgyl)=0 then 0 else round(sum(huiff*kuangfgyl)/sum(kuangfgyl),2) end  as huiff,"
										+ "sum(qitsrl)  as qitsrl,sum(shijhylfdy+shijhylgry+shijhylqty+shijhylzcsh) as shijhylhj, sum(shijhylfdy) as shijhylfdy,sum(shijhylgry) as shijhylgry,"
										+ "sum(shijhylqty) as shijhylqty,sum(shijhylzcsh) as shijhylzcsh, sum(diaocl) as diaocl,"
										+ "sum( panypk) as panypk,sum(decode(fenx,'本月',yuemjc,0)) as yuemjc from diaor16bb "
										+ "where  DIANCXXB_ID = " + getProperId(getIDiancbmModel(),value[0],-1) +" and ((riq=to_date("
										+ "'"+strRiq+"','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date("
										+ "'"+strRiq+"','yyyy-mm-dd'),-1) and fenx='累计')) group by diancxxb_id, gongysb_id ";
								}
						 		if((i+1)==strbf2.size()){
						 			ResultSetList rslj=con.getResultSetList(sql);
							 		for(int r=0;rslj.next();r++) {
										sql ="insert into diaor16bb (id, riq, fenx, diancxxb_id, gongysb_id, pinz, shangyjc, kuangfgyl, yuns, kuid, farl, huif, shuif, huiff, qitsrl,"
											+" shijhylhj, shijhylfdy, shijhylgry, shijhylqty, shijhylzcsh, diaocl, panypk, yuemjc,diancscsj, beiz, jiessj, jiesr, diancscwjm)"
											+ "values(getnewid("+rslj.getLong("diancxxb_id")+")"
											+ ",to_date('"+strRiq+"','yyyy-mm-dd'),"
											+ "'累计',"
											+ rslj.getLong("diancxxb_id") + ","
											+ rslj.getLong("gongysb_id") + ",'',0,"
											
											+ rslj.getDouble("kuangfgyl") + ","
											+ rslj.getLong("yuns") + ","
											+ rslj.getLong("kuid") + ","
											+ rslj.getDouble("farl") + ","
											+ rslj.getDouble("huif") + ","
											+ rslj.getDouble("shuif") + ","
											+ rslj.getDouble("huiff") + ","
											+ rslj.getDouble("qitsrl") + ","
											+ rslj.getDouble("shijhylhj") + ","
											+ rslj.getDouble("shijhylfdy") + ","
											+ rslj.getDouble("shijhylgry") + ","
											+ rslj.getDouble("shijhylqty") + ","
											+ rslj.getDouble("shijhylzcsh") + ","
											+ rslj.getDouble("diaocl") + ","
											+ rslj.getDouble("panypk") + ","
											+ rslj.getDouble("yuemjc") +",'','',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
										con.getInsert(sql);
										
								 	}
						 		rslj.close();	
						 		}
						 }
					 }else if(strLeix.substring(0, 1).equals("1")){
						
//						 String value[] = data.getData(Strsz, strLeix);
						 
						 sql="insert into diaor01bb (id, riq, fenx, diancxxb_id, fadsbrl, meitsg, meithyhj, meithyfd, meithygr, meithyqt, meithysh, meitkc, shiysg, shiyhyhj, shiyhyfd,"
							 +" shiyhygr, shiyhyqt, shiyhysh, shiykc, fadl, gongrl, biaozmhfd, biaozmhgr, tianrmhfd, tianrmhgr, biaozmlfd, biaozmlgr, zonghrl, zonghm, diancscsj, beiz,jiessj,jiesr,diancscwjm)"
							 + " values(getnewid("+getProperId(getIDiancbmModel(),value[0],-1)+")"
							 +",to_date('"+strRiq+"','yyyy-MM-dd'), '本月', "
							 +getProperId(getIDiancbmModel(),value[0],-1)+",'"+Float.parseFloat(value[1])+"',"
							 +Float.parseFloat(value[2])+", "+Float.parseFloat(value[3])+", "+Float.parseFloat(value[4])+", "
							 +Float.parseFloat(value[5])+", "+Float.parseFloat(value[6])+", "+Float.parseFloat(value[7])+", "
							 +Float.parseFloat(value[8])+", "
							 +Float.parseFloat(value[9])+", "+Float.parseFloat(value[10])+", "+Float.parseFloat(value[11])+", "
							 +Float.parseFloat(value[12])+", "+Float.parseFloat(value[13])+", "+Float.parseFloat(value[14])+", "
							 +Float.parseFloat(value[15])+","+Float.parseFloat(value[16])+","+Float.parseFloat(value[17])+", "
							 +Float.parseFloat(value[18])+","+Float.parseFloat(value[19])+", "+Float.parseFloat(value[20])+","
							 +Float.parseFloat(value[21])+", "+Float.parseFloat(value[22])+","+Float.parseFloat(value[23])+", "
							 +Float.parseFloat(value[24])+", "+Float.parseFloat(value[25])+",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),'',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
						 
						 con.getInsert(sql);
						 
						 if (strYuef.equals("1")){
								sql="select diaor01bb.*  from diaor01bb "  + "where riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月' and DIANCXXB_ID = " + getProperId(getIDiancbmModel(),value[0],-1);
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
						         + " decode((sum(shiyhyfd)+sum(shiyhygr))*2+(sum(meithyfd)+sum(meithygr)),0,0,round((sum(biaozmlfd)+sum(biaozmlgr))*7000*0.0041816/((sum(shiyhyfd)+sum(shiyhygr))*2+(sum(meithyfd)+sum(meithygr))),2)) as zonghrl,"
						         + " decode((sum(meithyfd)+sum(meithygr)),0,0,round(((sum(biaozmlfd)+sum(biaozmlgr))*7000-(sum(shiyhyfd)+sum(shiyhygr))*10000)*0.0041816/(sum(meithyfd)+sum(meithygr)),2)) as zonghm from diaor01bb "
						         + " where ((riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月') "
						         + " or (riq=add_months(to_date("
								 + "'"+strRiq+"','yyyy-mm-dd'),-1) and fenx='累计')) and diancxxb_id="
								 + getProperId(getIDiancbmModel(),value[0],-1)+" group by diancxxb_id";
					     }
					     //保存重新计算的累计数据
					     if((i+1)==strbf2.size()){
					    	 ResultSetList rec = con.getResultSetList(sql);
						     for(int r=0;rec.next();r++) {
						       sql ="insert into diaor01bb (id, riq, fenx, diancxxb_id, fadsbrl, meitsg, meithyhj, meithyfd, meithygr, meithyqt, meithysh, meitkc, shiysg, shiyhyhj,"
						    	   +" shiyhyfd, shiyhygr, shiyhyqt, shiyhysh, shiykc, fadl, gongrl, biaozmhfd, biaozmhgr, tianrmhfd, tianrmhgr, biaozmlfd, biaozmlgr, zonghrl, zonghm, diancscsj, beiz,jiessj,jiesr,diancscwjm)"
						           + "values(getnewid("+rec.getLong("diancxxb_id")+")"
						           + ",to_date('"+strRiq+"','yyyy-mm-dd'),"+ "'累计',"
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
						           +",'','',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
					           con.getInsert(sql);
					           
						     }
						     rec.close();
					     }
					 }else if(strLeix.substring(0, 1).equals("3")){
						 
//						 String value[] = data.getData(Strsz, strLeix);
						 long dianc_id = getProperId(getIDiancbmModel(),value[0],-1);
						 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
						 long gongysb_id = getProperId(getIMeikdqIdModel(),value[1],dianc_id);
						 if(gongysb_id==-1){
							 if(sbtmp.length()==0){
								 sbtmp.append(strdiancmc+"的煤矿编码"+value[1]);
							 }else{
								 sbtmp.append(","+strdiancmc+"的煤矿编码"+value[1]);
							 }
						 }else{
							 sql="insert into diaor03bb (id, riq, fenx, diancxxb_id, gongysb_id, jincsl, choucsl, zhanjcm, guoh, jianc, yingdsl, yingdzje,"
								 +" kuid, kuidzje, suopsl, suopje, shuom, diancscsj, beiz,jiessj, jiesr, diancscwjm) values("
								 +"getnewid("+getProperId(getIDiancbmModel(),value[0],-1)+"),to_date('"+strRiq+"','yyyy-MM-dd'), '本月', "
								 +getProperId(getIDiancbmModel(),value[0],-1)+","+getProperId(getIMeikdqIdModel(),value[1],dianc_id)+", "
								 +Float.parseFloat(value[2])+", "+Float.parseFloat(value[3])+", "
								 +Float.parseFloat(value[4])+", "+Float.parseFloat(value[5])+", "+Float.parseFloat(value[6])+", "
								 +Float.parseFloat(value[7])+", "+Float.parseFloat(value[8])+", "+Float.parseFloat(value[9])+", "
								 +Float.parseFloat(value[10])+",0, "+Float.parseFloat(value[11])+", '',"
								 +" to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),'',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')" ;
								 
							 con.getInsert(sql);
							 
							 if (strYuef.equals("1")){
									sql="select diaor03bb.*  from diaor03bb "  + "where riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月' and DIANCXXB_ID = " + getProperId(getIDiancbmModel(),value[0],-1);
							}
							else{
								sql = "select diancxxb_id,gongysb_id,'累计' as fenx,sum(jincsl) as jincsl,sum(choucsl) as choucsl,"
									 +"decode(sum(jincsl),0,0,round(sum(choucsl)/sum(jincsl)*100,2)) as zhanjcm,"
							          + " decode(sum(jincsl),0,0,round(sum(jincsl*guoh)/sum(jincsl),2)) as guoh,(100-decode(sum(jincsl),0,0,round(sum(jincsl*guoh)/sum(jincsl),2))) as jianc,"
							          +" sum(yingdsl) as yingdsl,sum(yingdzje) as yingdzje,"
							          +"sum(kuid) as kuid,sum(kuidzje) as kuidzje,"
							          + " sum(suopsl) as suopsl,sum(suopje) as suopje"
							         + " from diaor03bb where ((riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"+strRiq+"'"
							         + ",'yyyy-mm-dd'),-1) and fenx='累计')) and diancxxb_id="+getProperId(getIDiancbmModel(),value[0],-1)
							         +" group by diancxxb_id,gongysb_id";
						     }
						     //保存重新计算的累计数据
						     if((i+1)==strbf2.size()){
						    	 ResultSetList rec = con.getResultSetList(sql);
							     for(int r=0;rec.next();r++) {
							       sql ="insert into diaor03bb (id, riq, fenx, diancxxb_id, gongysb_id, jincsl, choucsl, zhanjcm, guoh,"
							    	   +" jianc, yingdsl, yingdzje, kuid, kuidzje, suopsl, suopje, shuom, diancscsj, beiz,jiessj, jiesr, diancscwjm) "
							           + "values(getnewid("+rec.getLong("diancxxb_id")+")"
							           + ",to_date('"+strRiq+"','yyyy-mm-dd'),'累计',"
							           + rec.getString("diancxxb_id") + ","
							           + rec.getString("gongysb_id") + ","
							           + rec.getDouble("jincsl") + ","
							           + rec.getDouble("choucsl") + ","
							           + rec.getDouble("zhanjcm") + ","
							           + rec.getDouble("guoh") + ","
							           + rec.getDouble("jianc") + ","
							           + rec.getDouble("yingdsl") + ","
							           + rec.getDouble("yingdzje") + ","
							           + rec.getDouble("kuid") + ","
							           + rec.getDouble("kuidzje") + ","
							           + rec.getDouble("suopsl") + ","
							           + rec.getDouble("suopje") + ",'','','',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
							     con.getInsert(sql);
							     
							     }
							     rec.close();
						     }
						 }
					 }else if(strLeix.substring(0, 1).equals("4")){
						 
//						 String value[] = data.getData(Strsz, strLeix);
						 long dianc_id = getProperId(getIDiancbmModel(),value[0],-1);
						 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
						 long gongysb_id = getProperId(getIMeikdqIdModel(),value[1],dianc_id);
						 if(gongysb_id==-1){
							 if(sbtmp.length()==0){
								 sbtmp.append(strdiancmc+"的煤矿编码"+value[1]);
							 }else{
								 sbtmp.append(","+strdiancmc+"的煤矿编码"+value[1]);
							 }
						 }else{
							 sql="insert into diaor04bb (id, riq, fenx, diancxxb_id, gongysb_id, pinz, jincsl, yanssl, jianzl, kuangffrl, kuangfdj, kuangfsf, kuangfhf, kuangfhff,"
								 +" kuangflf, changffrl, changfdj, changfsf, changfhf, changfhff, changflf, dengjc, rejc, bufl, danjc, zongje, suopje, relsp, kuikspsl, liusp, liuspsl, diancscsj, beiz,jiessj,jiesr,diancscwjm)"
								 + " values(getnewid("+getProperId(getIDiancbmModel(),value[0],-1)+"),to_date('"+strRiq+"','yyyy-MM-dd'), '本月', "
								 +getProperId(getIDiancbmModel(),value[0],-1)+","+getProperId(getIMeikdqIdModel(),value[1],dianc_id)+",'', "
								 +Float.parseFloat(value[2])+", "+Float.parseFloat(value[3])+", "+Float.parseFloat(value[4])+", "
								 +Float.parseFloat(value[5])+", "+Float.parseFloat(value[6])+", "+Float.parseFloat(value[7])+", "
								 +Float.parseFloat(value[8])+", "+Float.parseFloat(value[9])+", "+Float.parseFloat(value[10])+", "
								 +Float.parseFloat(value[11])+", "+Float.parseFloat(value[12])+", "+Float.parseFloat(value[13])+", "
								 +Float.parseFloat(value[14])+","+Float.parseFloat(value[15])+","+Float.parseFloat(value[16])+", "
								 +Float.parseFloat(value[17])+","+Float.parseFloat(value[18])+", "+Float.parseFloat(value[19])+","
								 +Float.parseFloat(value[20])+", "+Float.parseFloat(value[21])+","
								 +Double.parseDouble(value[22])+", 0, 0, 0, 0 "
								 +",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),'',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
								 
							 con.getInsert(sql);
							 
							 if (strYuef.equals("1")){
								 sql = "select diancxxb_id, gongysb_id,'累计' as  fenx, sum(jincsl) as jincsl,sum( yanssl) as yanssl,"
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
							         + " from diaor04bb where riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月'and diancxxb_id="
							         +getProperId(getIDiancbmModel(),value[0],-1)+" group by diancxxb_id,gongysb_id";
							}
							else{
								sql = "select diancxxb_id, gongysb_id,'累计' as  fenx, sum(jincsl) as jincsl,sum( yanssl) as yanssl,"
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
							         + " from diaor04bb where ((riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"
							         + strRiq+"','yyyy-mm-dd'),-1) and fenx='累计')) and diancxxb_id="+getProperId(getIDiancbmModel(),value[0],-1)+" group by diancxxb_id,gongysb_id";
						     }
						     //保存重新计算的累计数据
						     if((i+1)==strbf2.size()){
						    	 ResultSetList rec = con.getResultSetList(sql);
							     for(int r=0;rec.next();r++) {
							       sql ="insert into diaor04bb (id, riq, fenx, diancxxb_id, gongysb_id, pinz, jincsl, yanssl, jianzl, kuangffrl, kuangfdj, kuangfsf, kuangfhf, kuangfhff, kuangflf,"
							    	   +" changffrl, changfdj, changfsf, changfhf, changfhff, changflf, dengjc, rejc, bufl, danjc, zongje, suopje, relsp, kuikspsl, liusp, liuspsl,diancscsj, beiz,jiessj,jiesr,diancscwjm) "
							           + "values(getnewid("+rec.getString("diancxxb_id")+")"
							           + ",to_date('"+strRiq+"','yyyy-mm-dd'),'累计',"
							           + rec.getString("diancxxb_id") + ","
							           + rec.getString("gongysb_id") + ",'',"
							           
							           + rec.getLong("jincsl") + ","
							           + rec.getLong("yanssl") + ","
							           + rec.getDouble("jianzl") + ","
							           + rec.getDouble("kuangffrl") + ","
							           + rec.getDouble("kuangfdj") + ","
							           + rec.getDouble("kuangfsf") + ","
							           + rec.getDouble("kuangfhf") + ","
							           + rec.getDouble("kuangfhff") + ","
							           + rec.getDouble("kuangflf") + ","
							           + rec.getDouble("changffrl") + ","
							           + rec.getDouble("changfdj") + ","
							           + rec.getDouble("changfsf") + ","
							           + rec.getDouble("changfhf") + ","
							           + rec.getDouble("changfhff") + ","
							           + rec.getDouble("changflf") + ","
							           + rec.getDouble("dengjc") + ","
							           + rec.getDouble("rejc") + ","
							           + rec.getDouble("bufl") + ","
							           + rec.getDouble("danjc") + ","
							           + rec.getDouble("zongje") + ","
							           + rec.getDouble("suopje") + ","
							           + rec.getDouble("relsp") 
							           + ",0,"
							           + rec.getDouble("liusp") 
							           + ",0,'','',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
		
						           con.getInsert(sql);
						           
							     }
							     rec.close();
						     }
						 }
					 }else if(strLeix.substring(0, 1).equals("8")){

						 long dianc_id = getProperId(getIDiancbmModel(),value[0],-1);
						 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
						 long gongysb_id = getProperId(getIMeikdqIdModel(),value[2],dianc_id);
						 if(gongysb_id==-1){
							 if(sbtmp.length()==0){
								 sbtmp.append(strdiancmc+"的煤矿编码"+value[2]);
							 }else{
								 sbtmp.append(","+strdiancmc+"的煤矿编码"+value[2]);
							 }
						 }else{
							 String haiysql = "select lx.mingc as leix from diancxxb dc,dianclbb lx where dc.dianclbb_id=lx.id and dc.bianm='"+value[0]+"'";
							 ResultSetList rs = con.getResultSetList(haiysql);
							 if(rs.next()){
								if(rs.getString("leix").equals("水陆联运")){
									
									value[12]=String.valueOf(Float.parseFloat(value[8])+Float.parseFloat(value[12]));
									value[13]=String.valueOf(Float.parseFloat(value[9])+Float.parseFloat(value[13]));
									value[14]=String.valueOf(Float.parseFloat(value[10])+Float.parseFloat(value[14]));
									value[16]=String.valueOf(Float.parseFloat(value[7])+Float.parseFloat(value[16]));
									
									value[7]="0";
									value[8]="0";
									value[9]="0";
									value[10]="0";
									
								}else{
									value[5] = new DecimalFormat("0.00").format(Float.parseFloat(value[5])+Float.parseFloat(value[6]));
									value[8]= new DecimalFormat("0.00").format(Float.parseFloat(value[8])+Float.parseFloat(value[9]));
								}
							 }
							 double daoczhj = 0;
							 String biaomdj = "0";
							 String buhsbmdj = "0";
							 if(Float.parseFloat(value[21])!=0){
							 daoczhj = Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
											 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
											 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
											 +Float.parseFloat(value[17])+Float.parseFloat(value[18]);
							 
							 biaomdj = new DecimalFormat("0.00").format((Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
											 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
											 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
											 +Float.parseFloat(value[17])+Float.parseFloat(value[18]))*29.271/Float.parseFloat(value[21]));
							 
							 buhsbmdj = new DecimalFormat("0.00").format((Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
											 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
											 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
											 +Float.parseFloat(value[17])+Float.parseFloat(value[18])
											 -Float.parseFloat(value[6])-Float.parseFloat(value[9])
											 -Float.parseFloat(value[13]))*29.271/Float.parseFloat(value[21]));
							 }
							 
							 sql="insert into diaor08bb (id, riq, fenx, diancxxb_id, gongysb_id, meil, daoczhj, kuangj, zengzse, jiaohqyzf, tielyf, tieyse, tielzf, shuillyf,"
								 +" shuiyf, shuiyse, shuiyzf, qiyf, gangzf, daozzf, qitfy, sunhl, sunhzhje, rez, biaomdj, buhsbmdj, meik, diancscsj, qiyse, beiz,jiessj, jiesr,diancscwjm)  values("
								 +"getnewid("+getProperId(getIDiancbmModel(),value[0],-1)+"),to_date('"+strRiq+"','yyyy-MM-dd'), '本月', "
								 +getProperId(getIDiancbmModel(),value[0],-1)+","+getProperId(getIMeikdqIdModel(),value[2],dianc_id)+","
								 +Integer.parseInt(value[3])+", "
								 
								 +daoczhj+", "
								 
								 +Float.parseFloat(value[5])+", "+Float.parseFloat(value[6])+", "+Float.parseFloat(value[7])+", "+Float.parseFloat(value[8])+", "
								 
								 +Float.parseFloat(value[9])+", "
								 
								 +Float.parseFloat(value[10])+", "+Float.parseFloat(value[11])+", "
								 +Float.parseFloat(value[12])+", "+Float.parseFloat(value[13])+", "+Float.parseFloat(value[14])+", "
								 +Float.parseFloat(value[15])+", "+Float.parseFloat(value[16])+","+Float.parseFloat(value[17])+","
								 +Float.parseFloat(value[18])+", "+Float.parseFloat(value[19])+","+Float.parseFloat(value[20])+", "
								 +Float.parseFloat(value[21])+","
								 
								 +Float.parseFloat(biaomdj)+","+Float.parseFloat(buhsbmdj)+","
								
								 +((Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
										 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
										 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
										 +Float.parseFloat(value[17])+Float.parseFloat(value[18]))*Integer.parseInt(value[3]))
								 +",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'), 0, '',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
								 
							 con.getInsert(sql);
							 
							 if (strYuef.equals("1")){
								 sql="select diaor08bb.*  from diaor08bb "  + "where riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月' and DIANCXXB_ID = " + getProperId(getIDiancbmModel(),value[0],-1);
							}
							else{
								sql = "select a.*,kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy as daoczhj,"
							       +"round(decode(rez,0,0,(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy)*29.271/rez),2) as biaomdj,"
							       +"round(decode(rez,0,0,(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy-zengzse-tieyse-shuiyse)*29.271/rez),2) as buhsbmdj,"
							       +"(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy)*meil as meik" 
								   +" from ("
								       +"select diancxxb_id,gongysb_id,'累计' as fenx,sum(meil) as meil," 
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
							          + " from diaor08bb  where ((riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"+strRiq+"','yyyy-mm-dd'),-1) " 
							          + " and fenx='累计')) and diancxxb_id="+getProperId(getIDiancbmModel(),value[0],-1)+"  group by diancxxb_id,gongysb_id) a ";
						     }
						     //保存重新计算的累计数据
						     if((i+1)==strbf2.size()){
						    	 ResultSetList rec = con.getResultSetList(sql);
							     for(int r=0;rec.next();r++) {
							       sql ="insert into diaor08bb (id, riq, fenx, diancxxb_id, gongysb_id, meil, daoczhj, kuangj, zengzse, jiaohqyzf, tielyf, tieyse,"
							    	   +" tielzf, shuillyf, shuiyf, shuiyse, shuiyzf, qiyf, gangzf, daozzf, qitfy, sunhl, sunhzhje, rez, biaomdj, buhsbmdj, meik, diancscsj, qiyse, beiz,jiessj, jiesr,diancscwjm) "
							           + "values(getnewid("+rec.getString("diancxxb_id")+")"
							           + ",to_date('"+strRiq+"','yyyy-mm-dd'),'累计',"
							           + rec.getString("diancxxb_id") + ","
							           + rec.getString("gongysb_id") + ","
							           + rec.getDouble("meil") + ","
							           + rec.getDouble("daoczhj") + ","
							           + rec.getDouble("kuangj") + ","
							           + rec.getDouble("zengzse") + ","
							           + rec.getDouble("jiaohqyzf") + ","
							           + rec.getDouble("tielyf") + ","
							           + rec.getDouble("tieyse") + ","
							           + rec.getDouble("tielzf") + ","
							           + rec.getDouble("shuillyf") + ","
							           + rec.getDouble("shuiyf") + ","
							           + rec.getDouble("shuiyse") + ","
							           + rec.getDouble("shuiyzf") + ","
							           + rec.getDouble("qiyf") + ","
							           + rec.getDouble("gangzf") + ","
							           + rec.getDouble("daozzf") + ","
							           + rec.getDouble("qitfy") + ","
							           + rec.getDouble("sunhl") + ","
							           + rec.getDouble("sunhzhje") + ","
							           + rec.getDouble("rez") + ","
							           + rec.getDouble("biaomdj") + ","
							           + rec.getDouble("buhsbmdj") +"," 
							           + rec.getDouble("meik")+",'',"
							           + rec.getDouble("qiyse")
							           +",'',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
		
						           con.getInsert(sql);
							     }
							     rec.close();
						     }
						 }
					 } else if(leix.substring(0, 1).equals("0")){//08表NEW

						 long dianc_id = getProperId(getIDiancbmModel(),value[0],-1);
						 String strdiancmc = getProperValue(getIDiancModel(),dianc_id);
						 long gongysb_id = getProperId(getIMeikdqIdModel(),value[2],dianc_id);
						 if(gongysb_id==-1){
							 if(sbtmp.length()==0){
								 sbtmp.append(strdiancmc+"的煤矿编码"+value[2]);
							 }else{
								 sbtmp.append(","+strdiancmc+"的煤矿编码"+value[2]);
							 }
						 }else{
							 String haiysql = "select lx.mingc as leix from diancxxb dc,dianclbb lx where dc.dianclbb_id=lx.id and dc.bianm='"+value[0]+"'";
							 ResultSetList rs = con.getResultSetList(haiysql);
							 if(rs.next()){
								if(rs.getString("leix").equals("水陆联运")){
									
									value[12]=String.valueOf(Float.parseFloat(value[8])+Float.parseFloat(value[12]));
									value[13]=String.valueOf(Float.parseFloat(value[9])+Float.parseFloat(value[13]));
									value[14]=String.valueOf(Float.parseFloat(value[10])+Float.parseFloat(value[14]));
									value[16]=String.valueOf(Float.parseFloat(value[7])+Float.parseFloat(value[16]));
									
									value[7]="0";
									value[8]="0";
									value[9]="0";
									value[10]="0";
									value[12]=String.valueOf(Float.parseFloat(value[12])+Float.parseFloat(value[13]));
									
								}else{
//									value[5] = new DecimalFormat("0.00").format(Float.parseFloat(value[5])+Float.parseFloat(value[6]));
									value[8]= new DecimalFormat("0.00").format(Float.parseFloat(value[8])+Float.parseFloat(value[9]));
								}
							 }
							 value[5] = new DecimalFormat("0.00").format(Float.parseFloat(value[5])+Float.parseFloat(value[6]));
							 
							 double daoczhj = 0;
							 String biaomdj = "0";
							 String buhsbmdj = "0";
							 if(Float.parseFloat(value[21])!=0){
							 daoczhj = Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
											 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
											 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
											 +Float.parseFloat(value[17])+Float.parseFloat(value[18]);
							 
							 biaomdj = new DecimalFormat("0.00").format((Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
											 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
											 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
											 +Float.parseFloat(value[17])+Float.parseFloat(value[18]))*29.271/Float.parseFloat(value[21]));
							 
							 buhsbmdj = new DecimalFormat("0.00").format((Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
											 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
											 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
											 +Float.parseFloat(value[17])+Float.parseFloat(value[18])
											 -Float.parseFloat(value[6])-Float.parseFloat(value[9])
											 -Float.parseFloat(value[13]))*29.271/Float.parseFloat(value[21]));
							 }
							 
							 sql="insert into diaor08bb_new (id, riq, fenx, diancxxb_id, gongysb_id, meil, daoczhj, kuangj, zengzse, jiaohqyzf, tielyf, tieyse, tielzf, shuillyf,"
								 +" shuiyf, shuiyse, shuiyzf, qiyf, gangzf, daozzf, qitfy, sunhl, sunhzhje, rez, biaomdj, buhsbmdj, meik, diancscsj, qiyse, beiz,jiessj, jiesr,diancscwjm)  values("
								 +"getnewid("+getProperId(getIDiancbmModel(),value[0],-1)+"),to_date('"+strRiq+"','yyyy-MM-dd'), '本月', "
								 +getProperId(getIDiancbmModel(),value[0],-1)+","+getProperId(getIMeikdqIdModel(),value[2],dianc_id)+","
								 +Integer.parseInt(value[3])+", "
								 
								 +daoczhj+", "
								 
								 +Float.parseFloat(value[5])+", "+Float.parseFloat(value[6])+", "+Float.parseFloat(value[7])+", "+Float.parseFloat(value[8])+", "
								 
								 +Float.parseFloat(value[9])+", "
								 
								 +Float.parseFloat(value[10])+", "+Float.parseFloat(value[11])+", "
								 +Float.parseFloat(value[12])+", "+Float.parseFloat(value[13])+", "+Float.parseFloat(value[14])+", "
								 +Float.parseFloat(value[15])+", "+Float.parseFloat(value[16])+","+Float.parseFloat(value[17])+","
								 +Float.parseFloat(value[18])+", "+Float.parseFloat(value[19])+","+Float.parseFloat(value[20])+", "
								 +Float.parseFloat(value[21])+","
								 
								 +Float.parseFloat(biaomdj)+","+Float.parseFloat(buhsbmdj)+","
								
								 +((Float.parseFloat(value[5])+Float.parseFloat(value[7])+Float.parseFloat(value[8])
										 +Float.parseFloat(value[10])+Float.parseFloat(value[11])+Float.parseFloat(value[12])
										 +Float.parseFloat(value[14])+Float.parseFloat(value[15])+Float.parseFloat(value[16])
										 +Float.parseFloat(value[17])+Float.parseFloat(value[18]))*Integer.parseInt(value[3]))
								 +",to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'), 0, '',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
								 
							 con.getInsert(sql);
							 
							 if (strYuef.equals("1")){
								 sql="select diaor08bb_new.*  from diaor08bb_new "  + "where riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月' and DIANCXXB_ID = " + getProperId(getIDiancbmModel(),value[0],-1);
							}
							else{
								sql = "select a.*,kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy as daoczhj,"
							       +"round(decode(rez,0,0,(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy)*29.271/rez),2) as biaomdj,"
							       +"round(decode(rez,0,0,(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy-zengzse-tieyse-shuiyse)*29.271/rez),2) as buhsbmdj,"
							       +"(kuangj+jiaohqyzf+tielyf+tielzf+shuillyf+shuiyf+shuiyzf+qiyf+gangzf+daozzf+qitfy)*meil as meik" 
								   +" from ("
								       +"select diancxxb_id,gongysb_id,'累计' as fenx,sum(meil) as meil," 
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
							          + " from diaor08bb_new  where ((riq=to_date('"+strRiq+"','yyyy-mm-dd')  and fenx='本月') or (riq=add_months(to_date('"+strRiq+"','yyyy-mm-dd'),-1) " 
							          + " and fenx='累计')) and diancxxb_id="+getProperId(getIDiancbmModel(),value[0],-1)+"  group by diancxxb_id,gongysb_id) a ";
						     }
						     //保存重新计算的累计数据
						     if((i+1)==strbf2.size()){
						    	 ResultSetList rec = con.getResultSetList(sql);
							     for(int r=0;rec.next();r++) {
							       sql ="insert into diaor08bb_new (id, riq, fenx, diancxxb_id, gongysb_id, meil, daoczhj, kuangj, zengzse, jiaohqyzf, tielyf, tieyse,"
							    	   +" tielzf, shuillyf, shuiyf, shuiyse, shuiyzf, qiyf, gangzf, daozzf, qitfy, sunhl, sunhzhje, rez, biaomdj, buhsbmdj, meik, diancscsj, qiyse, beiz,jiessj, jiesr,diancscwjm) "
							           + "values(getnewid("+rec.getString("diancxxb_id")+")"
							           + ",to_date('"+strRiq+"','yyyy-mm-dd'),'累计',"
							           + rec.getString("diancxxb_id") + ","
							           + rec.getString("gongysb_id") + ","
							           + rec.getDouble("meil") + ","
							           + rec.getDouble("daoczhj") + ","
							           + rec.getDouble("kuangj") + ","
							           + rec.getDouble("zengzse") + ","
							           + rec.getDouble("jiaohqyzf") + ","
							           + rec.getDouble("tielyf") + ","
							           + rec.getDouble("tieyse") + ","
							           + rec.getDouble("tielzf") + ","
							           + rec.getDouble("shuillyf") + ","
							           + rec.getDouble("shuiyf") + ","
							           + rec.getDouble("shuiyse") + ","
							           + rec.getDouble("shuiyzf") + ","
							           + rec.getDouble("qiyf") + ","
							           + rec.getDouble("gangzf") + ","
							           + rec.getDouble("daozzf") + ","
							           + rec.getDouble("qitfy") + ","
							           + rec.getDouble("sunhl") + ","
							           + rec.getDouble("sunhzhje") + ","
							           + rec.getDouble("rez") + ","
							           + rec.getDouble("biaomdj") + ","
							           + rec.getDouble("buhsbmdj") +"," 
							           + rec.getDouble("meik")+",'',"
							           + rec.getDouble("qiyse")
							           +",'',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+strJiesry+"','"+strFileName+"')";
		
						           con.getInsert(sql);
							     }
							     rec.close();
						     }
						 }
					 }
				 }
				 x=0;
				 if(sbtmp.length()==0){
		 			 con.commit();
		 		 }else{
		 			 con.rollBack();
		 			 sb.append(sb.toString()+","+sbtmp.toString());
		 		 }
//				 sb.append(sb.toString()+","+sbtmp.toString());
			 }   
			 if(sb.length()==0){
				 strMsg = "取数完成!";
//				 getDiancmcModels();
			 }else{
				 strMsg = sb.toString()+"不存在.该电厂取数失败！";
//				 getDiancmcModels();
			 }
		 }catch(Exception e){
		 		e.printStackTrace();
		 }finally{
			 con.Close();
		 }
		 return strMsg;
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
		
		sql = "select d.id,d.mingc from diancxxb d where d.jib=3 order by d.mingc desc";
//		System.out.println(sql);
		
		_IDiancModel = new IDropDownModel(sql);
		return _IDiancModel;
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
		
		sql = "select d.id,d.bianm from diancxxb d where d.jib=3 order by d.mingc";
		
		_IDiancbmModel = new IDropDownModel(sql);
		return _IDiancbmModel;
	}
	
//煤矿地区_id	
	private IPropertySelectionModel _IMeikdqIdModel;
	public IPropertySelectionModel getIMeikdqIdModel() {
		if (_IMeikdqIdModel == null) {
			getIMeikdqIdModels();
		}
		return _IMeikdqIdModel;
	}
	
	public IPropertySelectionModel getIMeikdqIdModels() {
		
		String sql="";
		
		sql = "select id,bianm from gongysb gy where gy.fuid=0 order by mingc";
		
		_IMeikdqIdModel = new IDropDownModel(sql);
		return _IMeikdqIdModel;
	}
//	
	//地区名称
	public boolean _diqumcchange = false;
	private IDropDownBean _DiqumcValue;

	public IDropDownBean getDiqumcValue() {
		if(_DiqumcValue==null){
			_DiqumcValue=(IDropDownBean)getIDiqumcModels().getOption(0);
		}
		return _DiqumcValue;
	}

	public void setDiqumcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiqumcValue != null) {
			id = _DiqumcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diqumcchange = true;
			} else {
				_diqumcchange = false;
			}
		}
		_DiqumcValue = Value;
	}

	private IPropertySelectionModel _IDiqumcModel;

	public void setIDiqumcModel(IPropertySelectionModel value) {
		_IDiqumcModel = value;
	}

	public IPropertySelectionModel getIDiqumcModel() {
		if (_IDiqumcModel == null) {
			getIDiqumcModels();
		}
		return _IDiqumcModel;
	}

	public IPropertySelectionModel getIDiqumcModels() {
		
		String sql="";
		
		sql = "select bianm,mingc from gongysb gy where gy.fuid=0 order by mingc";
//		System.out.println(sql);
		
		_IDiqumcModel = new IDropDownModel(sql);
		return _IDiqumcModel;
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
	
}
