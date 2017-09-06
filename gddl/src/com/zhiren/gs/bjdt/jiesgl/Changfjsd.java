package com.zhiren.gs.bjdt.jiesgl;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.asset.ExternalAsset;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
//import com.zhiren.rmis.report.diancpmt.DiancpmtBean;

public class Changfjsd extends BasePage{
	
	public Changfjsd(){
		
	}
	
	public String getChangfjsd(String where,int iPageIndex,Visit visit){
		
		JDBCcon cn = new JDBCcon();
		ResultSet yfrs=null;
		Report rt=new Report();
		  try{
			 String sql=""; 
			 int intjiesdscfs = -1;
			 String strjiesrq="";
			 String strfahdw="";
			 String strfaz="";
			 String strdiabch="";
			 long 	lgdiancxxb_id=0;
			 String strbianh="";
			 String strfahksrq="";
			 String strfahjzrq="";
			 String strfahrq = "";
			 String strdiqdm = "";
			 String stryuanshr = "";
			 String strshoukdw = "";
			 String strkaihyh = "";
			 String strkaisysrq="";
			 String strjiezysrq="";
			 String stryansrq = "";
			 String strhuowmc = "";
			 String strxianshr = "";
			 String stryinhzh = "";
			 String strfahsl = "";
			 String strches = "";
			 String stryansbh = "";
			 String strfapbh = "";
			 String strduifdd = "";
			 String strfukfs = "";
			 String strshijfk = "";
			 String strgongfbz_rl = "";
			 String strkuangfys_rl = "";
			 String strxiancsl_rl = "";
			 String strzhejbz_rl = "";
			 String strzhehje_rl = "";
			 String strgongfbz_lf = "";
			 String strkuangfys_lf = "";
			 String strxiancsl_lf = "";
			 String strzhejbz_lf = "";
			 String strzhehje_lf = "";
			 String strgongfsl = "";
			 String strxianssl = "";
			 String strkuidsl = "";
			 String strzhehje = "";
			 String strjiessl = "";
			 String strdanj = "";
			 String strjine = "";
			 String strbukouqjk = "";
			 String strjiakhj = "";
			 String strshuil_mk = "";
			 String strshuik_mk = "";
			 String strjialhj = "";
			 String strtielyf = "";
			 String strzaf = "";
			 String strbukouqzf = "";
			 String strjiskc = "";
			 String strbuhsyf = "";
			 String strshuil_ys = "";
			 String strshuik_ys = "";
			 String stryunzshj = "";
			 String strhej_dx = "";
			 String strhej_xx = "";
			 String strbeiz = "";
			 String strguohzl = "";
			 String strgongfbz_hff="";
			 String strkuangfys_hff="";
			 String strxiancsl_hff="";
			 String strgongfbz_hf="";
			 String strkuangfys_hf="";
			 String strxiancsl_hf="";
			 String strzhejbz_hf="";
			 String strzhejbz_hff="";
			 String strzhehje_hff="";
			 String strgongfbz_sf="";
			 String strkuangfys_sf="";
			 String strxiancsl_sf="";
			 String strzhejbz_sf="";
			 String strzhehje_sf="";
			 String strzhehje_hf="";
			 String strranlbmjbr=" ";
			 String strranlbmjbrq="";
			 String strchangcwjbr=" ";
			 String strchangcwjbrq="";
			 String strzhijzxjbr=" ";
			 String strzhijzxjbrq="";
			 String strlingdqz=" ";
			 String strlingdqzrq="";
			 String strzonghcwjbr=" ";
			 String strzonghcwjbrq="";
			 String strmeikhjdx="";
			 String stryunzfhjdx="";
			 String strkuangfjsdbh = "";
			 
			 String strzhiljdcldqz = "";
			 String strzhiljdcldqzrq = "";
			 String liucztb_id="";
			 double danjc = 0;
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 sql="select djs.hetbh hetbh, nvl(rel.changf,0) changffrl,nvl(quanl.changf,0) changflf,nvl(hf.changf,0)  changfhf ,0 danjc,\n"+
                  "djs.diancxxb_id diancxxb_id,djs.bianm bianh,djs.jiesrq jiesrq,djs.jieslx jieslx,djs.id id,djs.gongysmc fahdw,\n"+
                  "djs.fahksrq fahrq,djs.fahjzrq jiezfhrq,djs.faz faz,djs.daibch daibch,djs.yuanshr yuanshr,djs.shoukdw shoukdw,\n"+
                  "djs.kaihyh kaihyh,djs.yansksrq yansrq,djs.yansjzrq jiezysrq,djs.meiz meiz,djs.xianshr xianshr,djs.zhangh zhangh,\n"+
                  "nvl(sl.gongf,0) gongfsl,djs.ches ches,djs.yansbh yansbh,djs.fapbh fapbh,djs.duifdd duifdd,djs.fukfs fukfs,go.bianm diqbm,\n"+
                  "djs.hansdj SHULZJBZ,\n"+
                  "nvl(rel.hetbz,0) GONGFRL,nvl(rel.jies,0) YANSRL,nvl(rel.jies,0)-to_number(substr(rel.hetbz,0,4))  YINGKRL,nvl(rel.zhejbz,0) RELZJBZ,nvl(rel.zhejje,0) RELZJJE,\n"+
                   "nvl(quanl.hetbz,0) LIUBZ,nvl(quanl.jies,0) LIUF,nvl(quanl.jies,0)-nvl(quanl.hetbz,0)  LIUYK,nvl(quanl.zhejbz,0) LIUYXDJ,nvl(quanl.zhejje,0) LIUYXJE,\n"+
                   "nvl(hff.gongf,0) GONGFHF,nvl(hff.jies,0) HUIFF,nvl(hff.yingk,0)  YINGKHF,nvl(hff.zhejbz,0) HUIFFYXDJ,nvl(hff.zhejje,0) HUIFFYXJE,\n"+
                   "nvl(shuif.gongf,0) GONGFSF,nvl(shuif.jies,0) SHUIF,nvl(shuif.yingk,0)  YINGKSF,nvl(shuif.zhejbz,0) SHUIFZJBZ,nvl(shuif.zhejje,0) SHUIFZJJE,\n"+
                   "nvl(sl.gongf,0) gongfsl,nvl(sl.jies,0)  YANSSL,nvl(sl.yingk,0) YINGK,nvl(sl.zhejje,0) SHULZJJE,\n"+
                   "djs.jiessl jiessl , djs.buhsdj danj,djs.buhsmk jiakje,djs.bukmk bukyqjk,djs.shuik JIAKHJ,djs.shuil jiaksl,\n"+
                   "djs.meikje jiaksk,djs.shuik+djs.meikje JIASJE,djs.guohl guohl,djs.beiz beiz,djs.ranlbmjbr ranlbmjbr,djs.ranlbmjbrq ranlbmjbrq,\n"+
                   "djs.ranlbmjbrq zhijzxjbrq,djs.liucgzid liucgzid,djs.ranlbmjbr,djs.ranlbmjbrq,djs.liucztb_id\n"+
                   "from diancjsmkb  djs\n"+
                   "left join\n"+
                   "--����\n"+
                   "(select * from  jieszbsjb ji  left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='�յ�����λ��ֵ') rel\n"+
                   "on(djs.id=rel.jiesdid)\n"+
                   "--ȫ��\n"+
                   " left join\n"+
                   "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='�����ȫ��') quanl\n"+
                   "on (djs.id=quanl.jiesdid)\n"+
                   "--�ҷ�\n"+
                   "left join\n"+ 
                   "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='������ҷ�') hf\n"+
                   "on (hf.jiesdid=djs.id)\n"+
                    "--����\n"+
                    "left join\n"+
                    "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='����' ) sl\n"+
                    "on(sl.jiesdid=djs.id)\n"+
                    "--�ӷ���\n"+
                    "left join\n"+
                    "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='�����޻һ��ӷ���') hff\n"+
                    "on(hff.jiesdid=djs.id)\n"+
                    "--ˮ��\n"+
                    "left join\n"+
                    "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='ȫˮ��' ) shuif\n"+
                    "on(hff.jiesdid=djs.id)\n"+
                    "left join\n"+
                    "gongysb go\n"+
                    "on(djs.gongysb_id=go.id)\n"+
                    " where djs.bianm='"+where+"'\n"; 
			 ResultSet rs = cn.getResultSet(sql);
			
			 int intLeix=2;
			 long intDiancjsmkId=0;
			 long strkuangfjsmkb_id = -1;
			 boolean blnHasMeik =false;
			 
			 
			 String strhetbh = "";
			 String strchangffrl = "";
			 String strchangflf = "";
			 String strchangfhf = "";
			 
			 if(rs.next()){
				 
				 strhetbh = rs.getString("hetbh");//��ͬ���
				 strchangffrl = rs.getString("changffrl");//��������
				 strchangflf = rs.getString("changflf");//�������
				 strchangfhf = rs.getString("changfhf"); //�����ҷ�
				 
				 danjc = rs.getDouble("danjc");//�۲�
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				// strkuangfjsmkb_id = rs.getLong("kuangfjsmkb_id");
				 
				 strbianh=rs.getString("bianh");//���
				 strjiesrq=FormatDate(rs.getDate("jiesrq"));
				 intLeix=rs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
				 intDiancjsmkId =rs.getInt("id");//ú��id
				 strfahdw=rs.getString("fahdw");
				 
				 strfahksrq=rs.getString("fahrq");
				 strfahjzrq=rs.getString("jiezfhrq");
				 if(strfahksrq.equals(strfahjzrq)){
					 strfahrq = FormatDate(rs.getDate("fahrq"));//��������
				 }else{
					 strfahrq=FormatDate(rs.getDate("fahrq"))+" �� "+FormatDate(rs.getDate("jiezfhrq"));
				 }
//				 strfahrq = rs.getString("fahrq");//��������
				 strfaz=rs.getString("faz");
				 strdiabch=rs.getString("daibch");
				 stryuanshr = rs.getString("yuanshr");//ԭ�ջ���
				 strshoukdw = rs.getString("shoukdw");//�տλ
				 strkaihyh = rs.getString("kaihyh");//��������
				 strkaisysrq=rs.getString("yansrq");
				 strjiezysrq=rs.getString("jiezysrq");
				 if(strkaisysrq.equals(strjiezysrq)){
					 stryansrq=FormatDate(rs.getDate("yansrq"));
				 }else{
					 stryansrq=FormatDate(rs.getDate("yansrq"))+" �� "+FormatDate(rs.getDate("jiezysrq"));
				 }
//				 stryansrq = rs.getString("yansrq");//��������
				 strhuowmc = rs.getString("MEIZ");//��������
				 strxianshr = rs.getString("xianshr");//���ջ���
				 stryinhzh = rs.getString("zhangh");//�ʺ�
				 strfahsl =rs.getString("gongfsl");//��������������������������������������������
				 strches = rs.getString("ches");//����
				 stryansbh = rs.getString("yansbh");//���ձ��
				 strfapbh = rs.getString("fapbh");//��Ʊ���
				 strduifdd = rs.getString("duifdd");//�Ҹ��ص�
				 strfukfs =rs.getString("fukfs") ;//���ʽ
				 strdiqdm=rs.getString("diqbm");
				 strshijfk =rs.getString("SHULZJBZ");//��˰���ۣ���������������������������������
				 strgongfbz_rl = rs.getString("GONGFRL");//��������
				 strkuangfys_rl = rs.getString("YANSRL");//��������
				 strxiancsl_rl=rs.getString("YINGKRL");//�����������
				 strzhejbz_rl =rs.getString("RELZJBZ");//�ۼ۱�׼����
				 strzhehje_rl = rs.getString("RELZJJE");//�ۺϽ������

				 strgongfbz_lf = rs.getString("LIUBZ");//������׼���
				 strkuangfys_lf = rs.getString("LIUF");//�������
				 strxiancsl_lf = rs.getString("LIUYK");//����������
				 strzhejbz_lf = rs.getString("LIUYXDJ");//�ۼ۱�׼���
				 strzhehje_lf = rs.getString("LIUYXJE");//�ۺϽ�����

				 strgongfbz_hff = rs.getString("GONGFHF");//������׼�ӷ���
				 strkuangfys_hff = rs.getString("HUIFF");//���ջӷ���
				 strxiancsl_hff = rs.getString("YINGKHF");//��������ӷ���
				 strzhejbz_hff = rs.getString("HUIFFYXDJ");//�ۼ۱�׼�ӷ���
				 strzhehje_hff = rs.getString("HUIFFYXJE");//�ۺϽ��ӷ���


				 strgongfbz_sf = rs.getString("GONGFSF");//������׼ˮ��
				 strkuangfys_sf = rs.getString("SHUIF");//����ˮ��
				 strxiancsl_sf = rs.getString("YINGKSF");//�������ˮ��
				 strzhejbz_sf = rs.getString("SHUIFZJBZ");//�ۼ۱�׼ˮ��
				 strzhehje_sf = rs.getString("SHUIFZJJE");//�ۺϽ��ˮ��

				 //*****************��������*****************//
				 strgongfsl = rs.getString("gongfsl");//��������
				 strxianssl =rs.getString("YANSSL");//��������
				 strkuidsl =rs.getString("YINGK");//��������
				 strzhehje = rs.getString("SHULZJJE");//�ۺϽ��

				 //********************����*****************//
				 strjiessl = rs.getString("jiessl");//��������
				 strdanj = rs.getString("danj");//����
				 strjine = rs.getString("JIAKJE");//���
				 strbukouqjk = rs.getString("BUKYQJK");//��(��)��ǰ�ۿ�
				 strjiakhj = rs.getString("JIAKHJ");//�ۿ�ϼ�
				 
				 strshuil_mk = rs.getString("JIAKSL");//˰��(ú��)
				 strshuik_mk = rs.getString("JIAKSK");//˰��(ú��)
				 
				 strjialhj = rs.getString("JIASJE");//��˰�ϼ�
				 liucztb_id=rs.getString("liucztb_id");
				 
				 strguohzl =rs.getString("GUOHL");//��������
				 strbeiz = nvlStr(rs.getString("beiz"));//��ע
				
				 dblMeik= Double.parseDouble(strjialhj);
				 blnHasMeik=true;
				 
//				 intjiesdscfs = rs.getInt("jiesdscfs");
//				********************��Ա����*****************//
				 strranlbmjbr=rs.getString("ranlbmjbr");//����¼����
				 strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));//¼��ʱ��
				 if(rs.getDate("ranlbmjbrq").getYear()+1900<2009){//Ӳ�Թ涨09��ǰ˰��Ϊ0.13��֮���Ϊ0.17
					 strshuil_mk="0.13";
				 }else{
					 strshuil_mk="0.17";
				 }
				 /*//				 strchangcwjbr=rs.getString("changcwjbr");
				 strchangcwjbrq=FormatDate(rs.getDate("changcwjbrq"));
//				 strzhijzxjbr=rs.getString("zhijzxjbr");
				 strzhijzxjbr=rs.getString("gongsshr");
				 if(strzhijzxjbr==null){
					 strzhijzxjbr = " ";
				 }
				 strzhijzxjbrq=FormatDate(rs.getDate("zhijzxjbrq"));
				 
				 strzhiljdcldqz=rs.getString("zhiljdcldqz");
				 if(strzhiljdcldqz==null){
					 strzhiljdcldqz = "";
				 }
				 strzhiljdcldqzrq = FormatDate(rs.getDate("zhiljdcldqzrq"));
				 
				 strlingdqz=rs.getString("lingdqz");
				 if(strlingdqz==null){
					 strlingdqz = " ";
				 }
				 strlingdqzrq=FormatDate(rs.getDate("lingdqzrq"));
				 strzonghcwjbr=rs.getString("zonghcwjbr");
				 if(strzonghcwjbr==null){
					 strzonghcwjbr = " ";
				 }
				 strzonghcwjbrq=FormatDate(rs.getDate("zonghcwjbrq"));*/
				 
				 long liucgzid = rs.getLong("liucgzid");//���̸������
				 //����ÿһ���Ĳ���Ա
				 String kfsql="select caozy,shij from liucgzb where  liucgzid ="+liucgzid+" order by id"; 
					 kfsql="select *  from liucgzb where id in(\n"+
					   " select max(gz.id) gz_id from liucgzb gz,liucdzb dz,liucztb zt1,liucztb zt2 ,liucztb zt3\n"+
					   " where gz.liucdzb_id=dz.id  and gz.liucgzid="+liucgzid+" and dz.liucztqqid=zt1.id and dz.liuczthjid=zt2.id and zt1.xuh<zt2.xuh \n"+
					   " and zt2.xuh<=zt3.xuh \n"+
					   "    and zt3.id="+liucztb_id+"\n"+
					   "   group by gz.liucdzb_id\n"+
					  "  ) order by shij";
					    
//				System.out.println(kfsql);
				 ResultSet kfrs = cn.getResultSet(kfsql);
				 List liuChengeXunXu =new ArrayList();
				 while(kfrs.next()){
					/* 
					 if(kfrs.getRow()==1){
						 strchangcwjbr=kfrs.getString("caozy");
						 strchangcwjbrq=this.FormatDate(kfrs.getDate("shij"));
					 }*/
					 if(kfrs.getRow()==1){//��1��˾��˼�
						 strzhijzxjbr=kfrs.getString("caozy");
						 strzhijzxjbrq=this.FormatDate(kfrs.getDate("shij"));
					 }
					 if(kfrs.getRow()==2){//��2��˾��˼�
						 strzhiljdcldqz=kfrs.getString("caozy");
						 strzhiljdcldqzrq=this.FormatDate(kfrs.getDate("shij"));
					 }
					 if(kfrs.getRow()==3){//��3��˾��˼�
						 strlingdqz=kfrs.getString("caozy");
						 strlingdqzrq=this.FormatDate(kfrs.getDate("shij"));
					 }
					 if(kfrs.getRow()==4){//��4��˾��˼�
						 strzonghcwjbr=kfrs.getString("caozy");
						 strzonghcwjbrq=this.FormatDate(kfrs.getDate("shij")); 
					 }	 
				 }
				 kfrs.close();
				 
//	����������diancjsyf��ȡ������			 
				 if(intjiesdscfs==1){
					 if ((blnHasMeik)&&(intLeix==0)){
						 sql="select * from diancjsyf where diancjsmkb_id="+intDiancjsmkId;
					 }else{
						 sql="select * from diancjsyf where bianh='"+where+"'";
					 }
				 	 yfrs = cn.getResultSet(sql);
				  }
				}else{
					sql="select * from diancjsyf where bianh='"+where+"'";
					yfrs = cn.getResultSet(sql);
				}
			 
			 if(intjiesdscfs==1){
			 	 if ((blnHasMeik)&&(intLeix==0)){
					 sql="select * from diancjsyf where diancjsmkb_id="+intDiancjsmkId;
				 }else{
					 sql="select * from diancjsyf where bianh='"+where+"'";
				 }
				 rs = cn.getResultSet(sql);
			 }
			 
			 if(yfrs!=null){
				 if (yfrs.next()){
					 strtielyf =yfrs.getString("TIELYF");//��·�˷�
					 strzaf = yfrs.getString("ZAF");//�ӷ�
					 strdiqdm=yfrs.getString("diqbm");
					 strbukouqzf = yfrs.getString("BUKYQYZF");//��(��)��ǰ���ӷ�
					 strjiskc = yfrs.getString("JISKC");//��˰�۳�
					 strbuhsyf =yfrs.getString("BUHSYF");//����˰�˷�
					 strshuil_ys = yfrs.getString("YUNFSL");//˰��(�˷�)
					 strshuik_ys = yfrs.getString("YUNFSK");//˰��(�˷�)
					 stryunzshj = yfrs.getString("YUNZFHJ");//���ӷѺϼ�
	//				 strbeiz =strbeiz + nvlStr(rs.getString("beiz"));//��ע
					 dblYunf=yfrs.getDouble("YUNZFHJ");
	//				 double tmpshijfk=Double.parseDouble(strshijfk);
	//				 double tmpyunfsl=Double.parseDouble(strshuil_ys);
	//				 strdanj = String.valueOf((double)Math.round((tmpshijfk-(tmpshijfk*tmpyunfsl))*100)/100);//����
					 
					 if(intLeix==2){
						 strshijfk =yfrs.getString("yunfhsdj");
						 lgdiancxxb_id=yfrs.getLong("diancxxb_id");
						 strbianh=yfrs.getString("bianh");
						 strjiesrq=FormatDate(yfrs.getDate("jiesrq"));
						 intLeix=yfrs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
						 intDiancjsmkId =yfrs.getInt("id");//ú��id
						 strfahdw=yfrs.getString("fahdw");
						 strfahksrq=yfrs.getString("fahrq");
						 strfahjzrq=yfrs.getString("jiezfhrq");
						 if(strfahksrq.equals(strfahjzrq)){
							 strfahrq = FormatDate(yfrs.getDate("fahrq"));//��������
						 }else{
							 strfahrq=FormatDate(yfrs.getDate("fahrq"))+" �� "+FormatDate(yfrs.getDate("jiezfhrq"));
						 }
	//					 strfahrq = yfrs.getString("fahrq");//��������
						 strfaz=yfrs.getString("faz");
						 strdiabch=yfrs.getString("daibch");
						 stryuanshr = yfrs.getString("yuanshr");//ԭ�ջ���
						 strshoukdw = yfrs.getString("shoukdw");//�տλ
						 strkaihyh = yfrs.getString("kaihyh");//��������
						 strkaisysrq=yfrs.getString("yansrq");
						 strjiezysrq=yfrs.getString("yansjzrq");
						 if(strkaisysrq.equals(strjiezysrq)){
							 stryansrq=FormatDate(yfrs.getDate("yansrq"));
						 }else{
							 stryansrq=FormatDate(yfrs.getDate("yansrq"))+" �� "+FormatDate(yfrs.getDate("yansjzrq"));
						 }
	//					 stryansrq = rs.getString("yansrq");//��������
						 strhuowmc = yfrs.getString("MEIZ");//��������
						 strxianshr = yfrs.getString("xianshr");//���ջ���
						 stryinhzh = yfrs.getString("zhangh");//�ʺ�
						 strfahsl =yfrs.getString("gongfsl");//��������
						 strches = yfrs.getString("ches");//����
						 stryansbh = yfrs.getString("yansbh");//���ձ��
						 strfapbh = yfrs.getString("fapbh");//��Ʊ���
						 strduifdd = yfrs.getString("duifdd");//�Ҹ��ص�
						 strfukfs = yfrs.getString("fukfs") ;//���ʽ
						 strdiqdm=yfrs.getString("diqbm");
						 strjiessl = yfrs.getString("gongfsl");
						 strjine = "0";//���
						 strbukouqjk = "0";//��(��)��ǰ�ۿ�
						 strjiakhj = "0";//�ۿ�ϼ�
						 strshuil_mk = "0";//˰��(ú��)
						 strshuik_mk = "0";//˰��(ú��)
						 strjialhj = "0";//��˰�ϼ�
						 strguohzl =yfrs.getString("GUOHL");//��������
						 strbeiz = nvlStr(yfrs.getString("beiz"));//��ע
						 dblMeik= Double.parseDouble(strjialhj);
						 blnHasMeik=true;
						 
						 strranlbmjbr=yfrs.getString("ranlbmjbr");
						 strranlbmjbrq=FormatDate(yfrs.getDate("ranlbmjbrq"));
						 strchangcwjbrq=FormatDate(yfrs.getDate("changcwjbrq"));
						 strzhijzxjbrq=FormatDate(yfrs.getDate("zhijzxjbrq"));
						 strlingdqzrq=FormatDate(yfrs.getDate("lingdqzrq"));
						 strzonghcwjbrq=FormatDate(yfrs.getDate("zonghcwjbrq"));
						 
					 }
				 }
			 }
			 
			 Money money=new Money();
			 //����ϼ�
			 strhej_xx=format(dblYunf+dblMeik,"0.00");
			 strmeikhjdx=money.NumToRMBStr(dblMeik);
			 stryunzfhjdx=money.NumToRMBStr(dblYunf);
			 strhej_dx=money.NumToRMBStr(dblYunf+dblMeik);
			 
			 rs.close();
			 cn.Close();
			 
			 int ArrWidth[]=new int[] {128,78,76,76,76,76,136,76,80,76,76};
			 
			 String ArrHeader[][]=new String[22][11];
			 ArrHeader[0]=new String[] {"������λ:"+strfahdw,"","","��վ:",strfaz,"������:",strdiabch,"�տλ:",strshoukdw,"",""};
			 ArrHeader[1]=new String[] {"��������:"+strfahrq,"","","��������:",strdiqdm,"ԭ�ջ���:",stryuanshr,"��������:",strkaihyh,"",""};
			 ArrHeader[2]=new String[] {"��������:"+stryansrq,"","","��������:",strhuowmc,"���ջ���:",strxianshr,"�����ʺ�:",stryinhzh,"",""};
			 ArrHeader[3]=new String[] {"��������:",strfahsl,"����:"+strches,"���ձ��:",stryansbh,"��Ʊ���:",strfapbh,"��ͬ���:",strhetbh,"",""};
			 ArrHeader[4]=new String[] {"��������","��������","��������","��������","��������","��������","��������","��������","","",""};
			 ArrHeader[5]=new String[] {"��˰��:"+strshijfk,"������׼","��������","��������","�������","�ۼ۱�׼","�ۺϽ��","��������","��������","ӯ��������","�ۺϽ��"};
			 ArrHeader[6]=new String[] {"����Qnet,ar(KCAL/KG)",strgongfbz_rl,strchangffrl,strkuangfys_rl,strxiancsl_rl,strzhejbz_rl,strzhehje_rl,"(��)","(��)","(��)","(Ԫ)"};
			 ArrHeader[7]=new String[] {"���St,ad(%)",strgongfbz_lf,strchangflf,strkuangfys_lf,strxiancsl_lf,strzhejbz_lf,strzhehje_lf,strgongfsl,strxianssl,strkuidsl,strzhehje};
			 ArrHeader[8]=new String[] {"�ӷ���vdaf(%)","","","","","","","","","",""};
			 ArrHeader[9]=new String[] {"�ҷ�aad",strgongfbz_hf,strchangfhf,strkuangfys_hf,strxiancsl_hf,strzhejbz_hf,strzhehje_hf,"","","",""};
			 ArrHeader[10]=new String[] {"ˮ��Mt(%)","","","","","","","","","",""};
			 ArrHeader[11]=new String[] {"��������","����","���","","��(��)��ǰ�ۿ�","��(��)��ǰ�ۿ�","�ۿ�ϼ�","˰��","˰��","��˰�ϼ�","��˰�ϼ�"};
			 ArrHeader[12]=new String[] {strjiessl,strdanj,strjine,"",strbukouqjk," ",formatq(strjiakhj),strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
			 ArrHeader[13]=new String[] {"ú��ϼ�(��д):",strmeikhjdx,"","","","","","","","",""};
			 ArrHeader[14]=new String[] {"�˷�","�ӷ�","��(��)��ǰ���ӷ�","","��˰�۳�","��˰�۳�","����˰�˷�","˰��","˰��","���ӷѺϼ�","���ӷѺϼ�"};
			 ArrHeader[15]=new String[] {strtielyf,strzaf,strbukouqzf,"",strjiskc,"",formatq(strbuhsyf),strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
			 ArrHeader[16]=new String[] {"���ӷѺϼ�(��д):",stryunzfhjdx,"","","","","","","","",""};
			 ArrHeader[17]=new String[] {"�ϼ�(��д):",strhej_dx,"","","","","","","�ϼ�(Сд):",strhej_xx,""};
			 ArrHeader[18]=new String[] {"��ע:",strbeiz,"","","","","","","��������:",strguohzl,""};
			 ArrHeader[19]=new String[] {"�糧ȼ�ϲ���:(����)","�糧������:(����)","","�����ල��:(ǩ��)","","�����ල���쵼:(ǩ��)","","�쵼����:(ǩ��)","","�ۺϲ���:(ǩ��)",""};
			 ArrHeader[20]=new String[] {"������:"+strranlbmjbr,"������:"+strchangcwjbr,"","������:"+strzhijzxjbr,"","������:"+strzhiljdcldqz,"",""+strlingdqz,"","������:"+strzonghcwjbr,""};
			 ArrHeader[21]=new String[] {""+strranlbmjbrq+"",""+strchangcwjbrq+"","",""+strzhijzxjbrq+"","",""+strzhiljdcldqzrq+"","",""+strlingdqzrq+"","",""+strzonghcwjbrq+"",""};
			 
//			 ����ҳTitle
			 rt.setTitle("ȼ�ϲɹ�����֪ͨ��",ArrWidth);
//			 Visit visit =(Visit)this.getPage().getVisit();
			 String tianbdw=getTianzdw(lgdiancxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
			 rt.setDefaultTitleLeft("���Ƶ�λ��"+tianbdw,3);
			 rt.setDefaultTitle(4,4,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������ڣ�"+strjiesrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(9,3,"���:"+strbianh+"  "+strkuangfjsdbh,Table.ALIGN_RIGHT);
			 rt.setBody(new Table(ArrHeader,0,0,0));
//			 rt.body.setHeaderData(ArrHeader);
			 rt.body.setRowHeight(26);
			 rt.body.setWidth(ArrWidth);			 
			 rt.body.mergeCell(1,1,1,3);
			 rt.body.mergeCell(1,9,1,11);
			 rt.body.mergeCell(2,1,2,3);
			 rt.body.mergeCell(2,9,2,11);
			 rt.body.mergeCell(3,1,3,3);
			 rt.body.mergeCell(3,9,3,11);
			 rt.body.mergeCell(4,9,4,11);
			 rt.body.mergeCell(5,1,5,7);
			 rt.body.mergeCell(5,8,5,11);
			 rt.body.mergeCell(6,5,6,5);
			 rt.body.mergeCell(6,6,6,6);
			 rt.body.mergeCell(6,10,6,10);
			 rt.body.mergeCell(7,5,7,5);
			 rt.body.mergeCell(7,6,7,6);
			 rt.body.mergeCell(7,10,7,10);
			 rt.body.mergeCell(8,5,8,5);
			 rt.body.mergeCell(8,6,8,6);
			 rt.body.mergeCell(8,10,8,10);
			 rt.body.mergeCell(12,1,12,1);
			 rt.body.mergeCell(12,2,12,2);
			 rt.body.mergeCell(12,3,12,4);
			 rt.body.mergeCell(12,5,12,6);
			 rt.body.mergeCell(12,10,12,11);
			 rt.body.mergeCell(13,1,13,1);
			 rt.body.mergeCell(13,3,13,4);
			 rt.body.mergeCell(13,5,13,6);
			 rt.body.mergeCell(13,10,13,11);
			 rt.body.mergeCell(14,2,14,11);
			 rt.body.mergeCell(15,1,15,1);
			 rt.body.mergeCell(15,2,15,2);
			 rt.body.mergeCell(15,3,15,4);
			 rt.body.mergeCell(15,5,15,6);
			 rt.body.mergeCell(15,10,15,11);
			 rt.body.mergeCell(16,1,16,1);
			 rt.body.mergeCell(16,2,16,2);
			 rt.body.mergeCell(16,3,16,4);
			 rt.body.mergeCell(16,5,16,6);
			 rt.body.mergeCell(16,10,16,11);
			 rt.body.mergeCell(17,2,17,11);
			 rt.body.mergeCell(18,2,18,8);
			 rt.body.mergeCell(18,10,18,11);
			 rt.body.mergeCell(19,2,19,8);
			 rt.body.mergeCell(19,10,19,11);
			 
			 rt.body.mergeCell(20,2,20,3);
			 rt.body.mergeCell(20,4,20,5);
			 rt.body.mergeCell(20,6,20,7);
			 rt.body.mergeCell(20,8,20,9);
			 rt.body.mergeCell(20,10,20,11);
			 rt.body.mergeCell(21,2,21,3);
			 rt.body.mergeCell(21,4,21,5);
			 rt.body.mergeCell(21,6,21,7);
			 rt.body.mergeCell(21,8,21,9);
			 rt.body.mergeCell(21,10,21,11);
			 rt.body.mergeCell(22,2,22,3);
			 rt.body.mergeCell(22,4,22,5);
			 rt.body.mergeCell(22,6,22,7);
			 rt.body.mergeCell(22,8,22,9);
			 rt.body.mergeCell(22,10,22,11);
			 
			 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,4,4,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,6,4,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,8,4,8,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.setCells(5,1,19,11,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(21,1,21,11,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(1,1,4,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(1,4,4,4,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(1,6,4,6,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(1,8,4,8,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 
			 
			 rt.body.setCells(17,2,17,11,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(14,2,14,11,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(18,2,18,8,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(20,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(20,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(21,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(22,Table.PER_ALIGN,Table.ALIGN_RIGHT);
			 rt.body.setRowCells(1,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(2,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(3,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(4,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 if(strlingdqz!=null){
				 if(strlingdqz.equals("�׸���")){
					 rt.body.setCellImage(21, 8, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/baijlqz.gif");
				 }else if(strlingdqz.equals("��־��")){
					 rt.body.setCellImage(21, 8, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/zzg.gif");
				 }
			 }
			 
				// ����ҳ��
				_CurrentPage = iPageIndex;
//				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				// System.out.println(rt.getAllPagesHtml());
				return rt.getAllPagesHtml(iPageIndex);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return rt.getAllPagesHtml(iPageIndex);
	}
	
	public String getTianzdw(long diancxxb_id) {
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select quanc from diancxxb where id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("quanc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy�� MM�� dd��", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
	}
	
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//��ǧλ�ָ���
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
	
//	 ���Ƶ�λ
//	private IDropDownBean _TianzdwValue;
//
//	private boolean _Tianzdwchange = false;
//
//	public IDropDownBean getTianzdwValue() {
//		if(_TianzdwValue==null){
//			_TianzdwValue=(IDropDownBean)getITianzdwModel().getOption(0);
//		}
//		return _TianzdwValue;
//	}
//
//	public void setTianzdwValue(IDropDownBean Value) {
//		if (_TianzdwValue != Value) {
//
//			_Tianzdwchange = true;
//		}
//		_TianzdwValue = Value;
//	}

//	private static IPropertySelectionModel _ITianzdwModel;
//
//	public void setITianzdwModel(IPropertySelectionModel value) {
//		_ITianzdwModel = value;
//	}
//
//	public IPropertySelectionModel getITianzdwModel() {
//		if (_ITianzdwModel == null) {
//			getITianzdwModels();
//		}
//		return _ITianzdwModel;
//	}
//
//	public IPropertySelectionModel getITianzdwModels() {
//		String sql = "select id,quanc from "
//					+" (select id,quanc,rownum+2 as xuh from diancxxb "
//					+" union select id,quanc,rownum as xuh from gongsxxb) "
//					+" order by xuh";
//		_ITianzdwModel = new IDropDownSelectionModel(sql);
//		return _ITianzdwModel;
//	}

	
}
