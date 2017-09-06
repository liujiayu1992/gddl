package com.zhiren.jt.jiesgl.report.dianchs;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Money;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Diancjsd extends BasePage{
	
	public Diancjsd(){
		
	}
	
	public String getDiancjsd(String where,int iPageIndex){
		
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		  try{
			  
			 String sql=""; 
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
			 
			 String strhetbz_rl="";
			 String strgongfbz_rl = "";
			 String strkuangfys_rl = "";
			 String strjiesbz_rl="";
			 String strxiancsl_rl = "";
			 String strzhejbz_rl = "";
			 String strzhehje_rl = "";
			 
			 String strhetbz_lf = "";
			 String strgongfbz_lf = "";
			 String strkuangfys_lf = "";
			 String strjiesbz_lf="";
			 String strxiancsl_lf = "";
			 String strzhejbz_lf = "";
			 String strzhehje_lf = "";
			 
			 String strhetbz_hff="";
			 String strgongfbz_hff="";
			 String strkuangfys_hff="";
			 String strjiesbz_hff="";
			 String strxiancsl_hff="";
			 String strzhejbz_hff="";
			 String strzhehje_hff="";
			 
			 String strhetbz_hf="";
			 String strgongfbz_hf="";
			 String strkuangfys_hf="";
			 String strjiesbz_hf="";
			 String strxiancsl_hf="";
			 String strzhejbz_hf="";
			 String strzhehje_hf="";
			 
			 String strhetbz_sf="";
			 String strgongfbz_sf="";
			 String strkuangfys_sf="";
			 String strjiesbz_sf="";
			 String strxiancsl_sf="";
			 String strzhejbz_sf="";
			 String strzhehje_sf="";
			 
			 
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
			 
			 double danjc = 0;
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 sql="select * from jiesb  where bianm='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=2;
			 long intJiesbId=0;

			 boolean blnHasMeik =false;
			 
			 if(rs.next()){
				 
//				 danjc = rs.getDouble("danjc");
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				 strbianh=rs.getString("bianm");
				 strjiesrq=FormatDate(rs.getDate("jiesrq"));
				 intLeix=rs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
				 intJiesbId =rs.getInt("id");//ú��id
				 strfahdw=rs.getString("gongysmc");
				 strfahksrq=rs.getString("fahksrq");
				 strfahjzrq=rs.getString("fahjzrq");
				 if(strfahksrq.equals(strfahjzrq)){
					 strfahrq = FormatDate(rs.getDate("fahksrq"));//��������
				 }else{
					 strfahrq=FormatDate(rs.getDate("fahksrq"))+" �� "+FormatDate(rs.getDate("fahjzrq"));
				 }
				 strfaz=rs.getString("faz");
				 strdiabch=rs.getString("daibch");
				 stryuanshr = rs.getString("yuanshr");//ԭ�ջ���
				 strshoukdw = rs.getString("shoukdw");//�տλ
				 strkaihyh = rs.getString("kaihyh");//��������
				 strkaisysrq=rs.getString("yansksrq");
				 strjiezysrq=rs.getString("yansjzrq");
				 if(strkaisysrq.equals(strjiezysrq)){
					 stryansrq=FormatDate(rs.getDate("yansksrq"));
				 }else{
					 stryansrq=FormatDate(rs.getDate("yansksrq"))+" �� "+FormatDate(rs.getDate("yansjzrq"));
				 }
				 strhuowmc = rs.getString("MEIZ");//��������
				 strxianshr = rs.getString("xianshr");//���ջ���
				 stryinhzh = rs.getString("zhangh");//�ʺ�
				 strches = rs.getString("ches");//����
				 stryansbh = rs.getString("yansbh");//���ձ��
				 strfapbh = rs.getString("fapbh");//��Ʊ���
				 strduifdd = rs.getString("duifdd");//�Ҹ��ص�
				 strfukfs =rs.getString("fukfs") ;//���ʽ
				 strshijfk =rs.getString("hansdj");//ʵ�ʸ���
				 
				 sql="select jieszbsjb.*,zhibb.mingc from jieszbsjb,jiesb,zhibb "
					 + " where jieszbsjb.jiesdid=jiesb.id and zhibb.id=jieszbsjb.zhibb_id"
			        + " and jiesb.bianm='"+where+"' and jieszbsjb.zhuangt=1";
				 
				 ResultSet rs2=cn.getResultSet(sql);
				 while(rs2.next()){
					 
					 if(rs2.getString("mingc").equals("����")){
						 
						 strfahsl =rs2.getString("GONGF");//��������
						 strgongfsl = strfahsl;//��������
						 strxianssl =rs2.getString("CHANGF");//��������
						 strjiessl=rs2.getString("JIES");
						 strkuidsl =String.valueOf((-rs2.getDouble("YINGK")));//��������
						 strzhehje = rs2.getString("ZHEJJE");//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals("�յ�����λ��ֵ")){
						 
						 strhetbz_rl=rs2.getString("hetbz");
						 strgongfbz_rl = rs2.getString("GONGF");//��������
						 strkuangfys_rl = rs2.getString("CHANGF");//��������
						 strjiesbz_rl=rs2.getString("jies");//��������
						 strxiancsl_rl=rs2.getString("YINGK");//�����������
						 strzhejbz_rl =rs2.getString("ZHEJBZ");//�ۼ۱�׼����
						 strzhehje_rl = rs2.getString("ZHEJJE");//�ۺϽ������
						 
					 }else if(rs2.getString("mingc").equals("�����ȫ��")){
						 
						 strhetbz_lf=rs2.getString("hetbz");
						 strgongfbz_lf = rs2.getString("GONGF");//������׼���
						 strkuangfys_lf = rs2.getString("CHANGF");//�������
						 strjiesbz_lf=rs2.getString("jies");//�������
						 strxiancsl_lf = rs2.getString("YINGK");//����������
						 strzhejbz_lf = rs2.getString("ZHEJBZ");//�ۼ۱�׼���
						 strzhehje_lf = rs2.getString("ZHEJJE");//�ۺϽ�����
						 
					 }else if(rs2.getString("mingc").equals("�����޻һ��ӷ���")){
						 
						 strhetbz_hff=rs2.getString("hetbz");
						 strgongfbz_hff = rs2.getString("GONGF");//������׼�ӷ���
						 strkuangfys_hff = rs2.getString("CHANGF");//���ջӷ���
						 strjiesbz_hff=rs2.getString("jies");//����ӷ���
						 strxiancsl_hff = rs2.getString("YINGK");//��������ӷ���
						 strzhejbz_hff = rs2.getString("ZHEJBZ");//�ۼ۱�׼�ӷ���
						 strzhehje_hff = rs2.getString("ZHEJJE");//�ۺϽ��ӷ���
						 
					 }else if(rs2.getString("mingc").equals("������ҷ�")){
						 
						 strhetbz_hf=rs2.getString("hetbz");
						 strgongfbz_hf = rs2.getString("GONGF");//������׼����
						 strkuangfys_hf =rs2.getString("CHANGF");//���շ���
						 strjiesbz_hf=rs2.getString("jies");//����ҷ�
						 strxiancsl_hf = rs2.getString("YINGK");//�����������
						 strzhejbz_hf =rs2.getString("ZHEJBZ");//�ۼ۱�׼����
						 strzhehje_hf =rs2.getString("ZHEJJE");//�ۺϽ���
						 
					 }else if(rs2.getString("mingc").equals("ȫˮ��")){
						 
						 strhetbz_sf=rs2.getString("hetbz");
						 strgongfbz_sf = rs2.getString("GONGF");//������׼ˮ��
						 strkuangfys_sf = rs2.getString("CHANGF");//����ˮ��
						 strjiesbz_sf=rs2.getString("jies");//����ˮ��
						 strxiancsl_sf = rs2.getString("YINGK");//�������ˮ��
						 strzhejbz_sf = rs2.getString("ZHEJBZ");//�ۼ۱�׼ˮ��
						 strzhehje_sf = rs2.getString("ZHEJJE");//�ۺϽ��ˮ��
					 }
				 }
				 
				 rs2.close();

				 //********************����*****************//
				 strdanj = rs.getString("hansdj");//����
				 strjine = rs.getString("meikje");//���
				 strbukouqjk = rs.getString("bukmk");//��(��)��ǰ�ۿ�
				 strjiakhj = rs.getString("buhsmk");//�ۿ�ϼ�
				 strshuil_mk = rs.getString("shuil");//˰��(ú��)
				 strshuik_mk = rs.getString("shuik");//˰��(ú��)
				 strjialhj = rs.getString("hansmk");//��˰�ϼ�
				 strguohzl =rs.getString("GUOHL");//��������
				 strbeiz = nvlStr(rs.getString("beiz"));//��ע
				 dblMeik= Double.parseDouble(strjialhj);
				 blnHasMeik=true;
				 
//			 0, ú�����;
//			 1, ��Ʊ����;
//			 2, �˷ѽ���
			 }	 
			 if ((blnHasMeik)&&(intLeix==1)){
				 
				 sql="select * from jiesfyb where bianm='"+where+"'";
				 
				 ResultSet rs3=cn.getResultSet(sql);
				 if (rs3.next()){
					 
					 strtielyf =rs3.getString("GUOTYF");//��·�˷�
					 strzaf = rs3.getString("dityf");//�ӷ�
					 strbukouqzf = rs3.getString("bukyf");//��(��)��ǰ���ӷ�
					 strjiskc = rs3.getString("JISKC");//��˰�۳�
					 strbuhsyf =rs3.getString("buhsyf");//����˰�˷�
					 strshuil_ys = rs3.getString("shuil");//˰��(�˷�)
					 strshuik_ys = rs3.getString("shuik");//˰��(�˷�)
					 stryunzshj = rs3.getString("hansyf");//���ӷѺϼ�
					 dblYunf=rs3.getDouble("hansyf");
				 }
				 rs3.close();
			 }else if(intLeix!=0){
					 
				 sql="select * from jiesfyb  where bianm='"+where+"'"; 
				 rs=cn.getResultSet(sql); 
				 	if(rs.next()){
	//					 strshijfk =rs.getString("yunfhsdj");
						 lgdiancxxb_id=rs.getLong("diancxxb_id");
						 strbianh=rs.getString("bianm");
						 strjiesrq=FormatDate(rs.getDate("jiesrq"));
						 intLeix=rs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
	//					 intDiancjsmkId =rs.getInt("id");//ú��id
						 strfahdw=rs.getString("gongysmc");
						 strfahksrq=rs.getString("fahksrq");
						 strfahjzrq=rs.getString("fahjzrq");
						 if(strfahksrq.equals(strfahjzrq)){
							 strfahrq = FormatDate(rs.getDate("fahksrq"));//��������
						 }else{
							 strfahrq=FormatDate(rs.getDate("fahksrq"))+" �� "+FormatDate(rs.getDate("fahjzrq"));
						 }
	//					 strfahrq = rs.getString("fahrq");//��������
						 strfaz=rs.getString("faz");
						 strdiabch=rs.getString("daibch");
						 stryuanshr = rs.getString("yuanshr");//ԭ�ջ���
						 strshoukdw = rs.getString("shoukdw");//�տλ
						 strkaihyh = rs.getString("kaihyh");//��������
						 strkaisysrq=rs.getString("yansksrq");
						 strjiezysrq=rs.getString("yansjzrq");
						 if(strkaisysrq.equals(strjiezysrq)){
							 stryansrq=FormatDate(rs.getDate("yansksrq"));
						 }else{
							 stryansrq=FormatDate(rs.getDate("yansksrq"))+" �� "+FormatDate(rs.getDate("yansjzrq"));
						 }
	//					 stryansrq = rs.getString("yansrq");//��������
						 strhuowmc = rs.getString("MEIZ");//��������
						 strxianshr = rs.getString("xianshr");//���ջ���
						 stryinhzh = rs.getString("zhangh");//�ʺ�
						 strfahsl =getGongfsl(rs.getLong("diancjsb_id"));//��������������������������������������������
						 strches = rs.getString("ches");//����
						 stryansbh = rs.getString("yansbh");//���ձ��
						 strfapbh = rs.getString("fapbh");//��Ʊ���
						 strduifdd = rs.getString("duifdd");//�Ҹ��ص�
						 strfukfs = rs.getString("fukfs") ;//���ʽ
						 strdiqdm=rs.getString("diqbm");
						 strshijfk =" ";//ʵ�ʸ����������������������������������
						 strgongfbz_rl = "";//��������
						 strkuangfys_rl = "";//��������
						 strxiancsl_rl= "";//�����������
						 strzhejbz_rl = "";//�ۼ۱�׼����
						 strzhehje_rl = "";//�ۺϽ������
	
						 strgongfbz_lf = "";//������׼���
						 strkuangfys_lf = "";//�������
						 strxiancsl_lf = "";//����������
						 strzhejbz_lf = "";//�ۼ۱�׼���
						 strzhehje_lf = "";//�ۺϽ�����
	
						 strgongfbz_hff = "";//������׼�ӷ���
						 strkuangfys_hff = "";//���ջӷ���
						 strxiancsl_hff = "";//��������ӷ���
						 strzhejbz_hff = "";//�ۼ۱�׼�ӷ���
						 strzhehje_hff = "";//�ۺϽ��ӷ���
	
						 strgongfbz_hf = "";//������׼����
						 strkuangfys_hf = "";//���շ���
						 strxiancsl_hf = "";//�����������
						 strzhejbz_hf = "";//�ۼ۱�׼����
						 strzhehje_hf = "";//�ۺϽ���
	
						 strgongfbz_sf = "";//������׼ˮ��
						 strkuangfys_sf = "";//����ˮ��
						 strxiancsl_sf = "";//�������ˮ��
						 strzhejbz_sf = "";//�ۼ۱�׼ˮ��
						 strzhehje_sf = "";//�ۺϽ��ˮ��
	
						 strgongfsl = "";//��������
						 strxianssl ="";//��������
						 strkuidsl ="";//��������
						 strzhehje = "";//�ۺϽ��
	
	//					 strjiessl = rs.getString("jiessl");//��������
						 strjiessl = rs.getString("gongfsl");
	//					 strdanj = (double)Math.round(a);//����
						 strjine = "0";//���
						 strbukouqjk = "0";//��(��)��ǰ�ۿ�
						 strjiakhj = "0";//�ۿ�ϼ�
						 strshuil_mk = "0";//˰��(ú��)
						 strshuik_mk = "0";//˰��(ú��)
						 strjialhj = "0";//��˰�ϼ�
						 strguohzl =rs.getString("GUOHL");//��������
						 strbeiz = nvlStr(rs.getString("beiz"));//��ע
						 dblMeik= Double.parseDouble(strjialhj);
						 blnHasMeik=true;
						 
						 strranlbmjbr=rs.getString("ranlbmjbr");
						 strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));
					 
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
//			 ���ƻӷ��֡��ҷ֡�ˮ��������ʾ
			 boolean hff_bn=false;
			 boolean hf_bn=false;
			 boolean sf_bn=false;
			 
			 int hff_row=9;
			 int hf_row=10;
			 int sf_row=11;
			 
			 if(strjiesbz_hff.equals("")||strjiesbz_hff.equals("0")){
				 hff_bn=true;
			 }
			 if(strjiesbz_hf.equals("")||strjiesbz_hf.equals("0")){
				 hf_bn=true;
			 }
			 if(strjiesbz_sf.equals("")||strjiesbz_sf.equals("0")){
				 sf_bn=true;
			 }
//			 
			 int ArrWidth[]=new int[] {110,85,65,65,65,65,75,70,76,85,70,70};
			 
			 String ArrHeader[][]=new String[22][12];
			 ArrHeader[0]=new String[] {"������λ:"+strfahdw,"","","��վ:",strfaz,"������:",strdiabch,"","�տλ:",strshoukdw,"",""};
			 ArrHeader[1]=new String[] {"��������:"+strfahrq,"","","��������:",strdiqdm,"ԭ�ջ���:",stryuanshr,"","��������:",strkaihyh,"",""};
			 ArrHeader[2]=new String[] {"��������:"+stryansrq,"","","��������:",strhuowmc,"���ջ���:",strxianshr,"","�����ʺ�:",stryinhzh,"",""};
			 ArrHeader[3]=new String[] {"��������:",strfahsl,"����:"+strches,"���ձ��:",stryansbh,"��Ʊ���:",strfapbh,"","�Ҹ��ص�:",strduifdd,"���ʽ:",strfukfs};
			 ArrHeader[4]=new String[] {"��������","��������","��������","��������","��������","��������","��������","��������","��������","","",""};
			 ArrHeader[5]=new String[] {"��˰��:"+strshijfk+"(Ԫ)","��ͬ��׼","������׼","��������","�����׼","�������","�ۼ۱�׼","�ۺϽ��","��������","��������","��������","�ۺϽ��"};
			 ArrHeader[6]=new String[] {"����(KCAL/KG)",strhetbz_rl,strgongfbz_rl,strkuangfys_rl,strjiesbz_rl,strxiancsl_rl,strzhejbz_rl,strzhehje_rl,"(��)","(��)","(��)","(Ԫ)"};
			 ArrHeader[7]=new String[] {"���(%)",strhetbz_lf,strgongfbz_lf,strkuangfys_lf,strjiesbz_lf,strxiancsl_lf,strzhejbz_lf,strzhehje_lf,strgongfsl,strxianssl,strkuidsl,strzhehje};
			 ArrHeader[8]=new String[] {"�ӷ���(%)",strhetbz_hff,strgongfbz_hff,strkuangfys_hff,strjiesbz_hff,strxiancsl_hff,strzhejbz_hff,strzhehje_hff,"","","",""};
			 ArrHeader[9]=new String[] {"�ҷ�(%)",strhetbz_hf,strgongfbz_hf,strkuangfys_hf,strjiesbz_hf,strxiancsl_hf,strzhejbz_hf,strzhehje_hf,"","","",""};
			 ArrHeader[10]=new String[] {"ˮ��(%)",strhetbz_sf,strgongfbz_sf,strkuangfys_sf,strjiesbz_sf,strxiancsl_sf,strzhejbz_sf,strzhehje_sf,"","","",""};
			 ArrHeader[11]=new String[] {"��������","����","���","","��(��)��ǰ�ۿ�","��(��)��ǰ�ۿ�","�ۿ�ϼ�","","˰��","˰��","��˰�ϼ�","��˰�ϼ�"};
			 ArrHeader[12]=new String[] {strjiessl,strdanj,strjine,"",strbukouqjk," ",formatq(strjiakhj),"",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
			 ArrHeader[13]=new String[] {"ú��ϼ�(��д):",strmeikhjdx,"","","","","","","","","",""};
			 ArrHeader[14]=new String[] {"��·�˷�","�ӷ�","��(��)��ǰ���ӷ�","","��˰�۳�","��˰�۳�","����˰�˷�","","˰��","˰��","���ӷѺϼ�","���ӷѺϼ�"};
			 ArrHeader[15]=new String[] {strtielyf,strzaf,strbukouqzf,"",strjiskc,"",formatq(strbuhsyf),"",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
			 ArrHeader[16]=new String[] {"���ӷѺϼ�(��д):",stryunzfhjdx,"","","","","","","","","",""};
			 ArrHeader[17]=new String[] {"�ϼ�(��д):",strhej_dx,"","","","","","","�ϼ�(Сд):",strhej_xx,"",""};
			 ArrHeader[18]=new String[] {"��ע:",strbeiz,"","","","","","","��������:",strguohzl,"",""};
			 ArrHeader[19]=new String[] {"�糧ȼ�ϲ���:(����)","","�糧������:(����)","","","�����ල��:(ǩ��)","","�쵼����:(ǩ��)","","�ۺϲ���:(ǩ��)","",""};
			 ArrHeader[20]=new String[] {"������:"+strranlbmjbr,"","������:"+strchangcwjbr,"","","������:"+strzhijzxjbr,"",""+strlingdqz,"","������:"+strzonghcwjbr,"",""};
			 ArrHeader[21]=new String[] {""+strranlbmjbrq+"","",""+strchangcwjbrq+"","","",""+strzhijzxjbrq+"","",""+strlingdqzrq+"","",""+strzonghcwjbrq+"","",""};
			 
//			 ����ҳTitle
//			 Report rt=new Report();
			 rt.setTitle("ȼ�ϲɹ�����ͳһ֪ͨ��",ArrWidth);
			 String tianbdw=getTianzdw(lgdiancxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
			 rt.setDefaultTitleLeft("���Ƶ�λ��"+tianbdw,3);
			 rt.setDefaultTitle(4,4,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������ڣ�"+strjiesrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(9,3,"���:"+strbianh,Table.ALIGN_CENTER);
			 rt.setBody(new Table(ArrHeader,0,0,0));
			 rt.body.setRowHeight(26);
			 rt.body.setWidth(ArrWidth);			 
			 
			 rt.body.mergeCell(1,1,1,3);
			 rt.body.mergeCell(1,7,1,8);
			 rt.body.mergeCell(1,10,1,12);
			 rt.body.setCells(1,4,1,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,6,1,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,9,1,9,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.mergeCell(2,1,2,3);
			 rt.body.mergeCell(2,7,2,8);
			 rt.body.mergeCell(2,10,2,12);
			 rt.body.setCells(2,4,2,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(2,6,2,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(2,9,2,9,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.mergeCell(3,1,3,3);
			 rt.body.mergeCell(3,7,3,8);
			 rt.body.mergeCell(3,10,3,12);
			 rt.body.setCells(3,4,3,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(3,6,3,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(3,9,3,9,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.mergeCell(4,7,4,8);
			 rt.body.setCells(4,4,4,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.mergeCell(5,1,5,8);
			 rt.body.mergeCell(5,9,5,12);
			 
			 rt.body.mergeCell(12,3,12,4);
			 rt.body.mergeCell(12,5,12,6);
			 rt.body.mergeCell(12,7,12,8);
			 rt.body.mergeCell(12,11,12,12);
			 
			 rt.body.mergeCell(13,3,13,4);
			 rt.body.mergeCell(13,5,13,6);
			 rt.body.mergeCell(13,7,13,8);
			 rt.body.mergeCell(13,11,13,12);
			 
			 rt.body.mergeCell(14,2,14,12);//ú��ϼƴ�д
			 rt.body.setCells(14,12,14,12,Table.PER_BORDER_RIGHT,1);
			 
			 rt.body.mergeCell(15,3,15,4);
			 rt.body.mergeCell(15,5,15,6);
			 rt.body.mergeCell(15,7,15,8);
			 rt.body.mergeCell(15,11,15,12);
			 
			 rt.body.mergeCell(16,3,16,4);
			 rt.body.mergeCell(16,5,16,6);
			 rt.body.mergeCell(16,7,16,8);
			 rt.body.mergeCell(16,11,16,12);
			 
			 rt.body.mergeCell(17,2,17,12);//�˷Ѻϼƴ�д
			 
			 rt.body.mergeCell(18,2,18,8);
			 rt.body.mergeCell(18,10,18,12);//�ϼƴ�д
			 
			 rt.body.mergeCell(19,2,19,8);
			 rt.body.mergeCell(19,10,19,12);
			 rt.body.mergeCell(20,1,20,2);
			 rt.body.mergeCell(20,3,20,5);
			 rt.body.mergeCell(20,6,20,7);
			 rt.body.mergeCell(20,8,20,9);
			 rt.body.mergeCell(20,10,20,12);
			 rt.body.mergeCell(21,1,21,2);
			 rt.body.mergeCell(21,3,21,5);
			 rt.body.mergeCell(21,6,21,7);
			 rt.body.mergeCell(21,8,21,9);
			 rt.body.mergeCell(21,10,21,12);
			 rt.body.mergeCell(22,1,22,2);
			 rt.body.mergeCell(22,3,22,5);
			 rt.body.mergeCell(22,6,22,7);
			 rt.body.mergeCell(22,8,22,9);
			 rt.body.mergeCell(22,10,22,12);
			 
			 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.setCells(5, 1, 12, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(6,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(13,1,13,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(15,1,15,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(16,1,16,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setRowCells(20,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(21,Table.PER_BORDER_BOTTOM,0);
			 
//			 ����������
			 if(hff_bn){
				 rt.body.setRowCells(hff_row, Table.PER_USED, false);
				 rt.body.setRowHeight(hff_row,0);
			 }
			 if(hf_bn){
				 
				 rt.body.setRowCells(hf_row, Table.PER_USED, false);
				 rt.body.setRowHeight(hf_row,0);
			 }
			 if(sf_bn){
				 
				 rt.body.setRowCells(sf_row, Table.PER_USED, false);
				 rt.body.setRowHeight(sf_row,0);
			 }
			 
				// ����ҳ��
				_CurrentPage = 1;
//				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				// System.out.println(rt.getAllPagesHtml());
				return rt.getAllPagesHtml(iPageIndex);
			}catch(Exception e) {
		// TODO �Զ����ɷ������
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return rt.getAllPagesHtml(iPageIndex);
	}
	
	private String getGongfsl(long jiesbid) {
		// TODO �Զ����ɷ������
		JDBCcon con=new JDBCcon();
		String gongfsl="";
		try{
			
			String sql=" select gongf from jieszbsjb,jiesb,zhibb "
			        + " where jiesb.diancjs= "+jiesbid+""
			        + " and jiesb.id=jieszbsjb.jiesdid" 
			        + " and jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='����'";
			
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				gongfsl=rs.getString("gongf");
				return gongfsl;
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return null;
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
}
