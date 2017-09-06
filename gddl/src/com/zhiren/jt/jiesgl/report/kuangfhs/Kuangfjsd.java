package com.zhiren.jt.jiesgl.report.kuangfhs;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Kuangfjsd {

	public Kuangfjsd(){
		
	}
	
	public String getKuangfjsd(String where,int iPageIndex){
		
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		  try{
			 String sql=""; 
			 String strjiesrq="";
			 String strfahdw="";
			 String strfaz="";
			 String strdiabch="";
			 long 	lgdiancxxb_id=0;
			 long 	lggongsxxb_id=0;
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
			 String strjiessl_rl="";
			 String strxiancsl_rl = "";
			 String strzhejbz_rl = "";
			 String strzhehje_rl = "";
			 
			 String strhetbz_lf="";
			 String strgongfbz_lf = "";
			 String strkuangfys_lf = "";
			 String strjiessl_lf="";
			 String strxiancsl_lf = "";
			 String strzhejbz_lf = "";
			 String strzhehje_lf = "";
				 
			 String strhetbz_hff=""; 	 
			 String strgongfbz_hff="";
			 String strkuangfys_hff="";
			 String strjiessl_hff="";
			 String strxiancsl_hff="";
			 String strzhejbz_hff="";
			 String strzhehje_hff=""; 
			 
			 String strhetbz_hf="";
			 String strgongfbz_hf="";
			 String strkuangfys_hf="";
			 String strjiessl_hf="";
			 String strxiancsl_hf="";
			 String strzhejbz_hf="";
			 String strzhehje_hf="";
				 
			 String strhetbz_sf="";
			 String strgongfbz_sf="";
			 String strkuangfys_sf="";
			 String strjiessl_sf="";
			 String strxiancsl_sf="";
			 String strzhejbz_sf="";
			 String strzhehje_sf="";
			 
				 ;
			 String strhetsl = "";
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
			 
			 
			 String strzhibr=" ";
			 String strzhibrq="";
			 String strlingdqz=" ";
			 String strlingdqzrq="";
			 String strzonghcwjbr=" ";
			 String strzonghcwjbrq="";
			 String strmeikhjdx="";
			 String stryunzfhjdx="";
			 
			 long diancjsmkb_id=-1;
			 String strdiancjsbh="";
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 sql="select * from kuangfjsmkb where bianm='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=2;
			 long intDiancjsmkId=0;
			 
			 boolean blnHasMeik =false;
			 
			 if(rs.next()){
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				 diancjsmkb_id=rs.getLong("diancjsmkb_id");
				 strbianh=rs.getString("bianm");
				 strjiesrq=FormatDate(rs.getDate("jiesrq"));
				 intLeix=rs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
				 intDiancjsmkId =rs.getLong("id");//ú��id
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
				 
				 
				 sql = "select jieszbsjb.*,zhibb.mingc from jieszbsjb,kuangfjsmkb,zhibb "
						+ " where jieszbsjb.jiesdid=kuangfjsmkb.id and zhibb.id=jieszbsjb.zhibb_id"
						+ " and kuangfjsmkb.bianm='"
						+ where
						+ "' and jieszbsjb.zhuangt=1 ";
				 ResultSet rec = cn.getResultSet(sql);
				 while(rec.next()){
					 
					 if(rec.getString("mingc").equals("����")){
						 
						 //*****************��������*****************//
						 
						 strhetsl=rec.getString("hetbz");
						 strgongfsl = rec.getString("gongf");//��������
						 strfahsl=strgongfsl;
						 strxianssl =rec.getString("changf");//��������
						 strkuidsl =String.valueOf(-rec.getDouble("yingk"));//��������
						 strshijfk =rec.getString("zhejbz");//�����ۼ۱�׼
						 strzhehje = rec.getString("zhejje");//�ۺϽ��
					 
					 }else if(rec.getString("mingc").equals("�յ�����λ��ֵ")){
						 
//						*****************��������*****************//
						 strhetbz_rl=rec.getString("hetbz");
						 strgongfbz_rl = rec.getString("GONGF");//��������
						 strkuangfys_rl = rec.getString("changf");//��������
						 strjiessl_rl=rec.getString("jies");
						 strxiancsl_rl=rec.getString("yingk");//�����������
						 strzhejbz_rl =rec.getString("zhejbz");//�ۼ۱�׼����
						 strzhehje_rl = rec.getString("zhejje");//�ۺϽ������
					 
					 }else if(rec.getString("mingc").equals("�����ȫ��")){
						 
						 strhetbz_lf=rec.getString("hetbz");
						 strgongfbz_lf = rec.getString("GONGF");//������׼���
						 strkuangfys_lf = rec.getString("changf");//�������
						 strjiessl_lf=rec.getString("jies");
						 strxiancsl_lf = rec.getString("yingk");//����������
						 strzhejbz_lf = rec.getString("zhejbz");//�ۼ۱�׼���
						 strzhehje_lf = rec.getString("zhejje");//�ۺϽ�����
					 
					 }else if(rec.getString("mingc").equals("�����޻һ��ӷ���")){
						 
						 strhetbz_hff=rec.getString("hetbz");//��ͬ��׼
						 strgongfbz_hff = rec.getString("GONGF");//������׼�ӷ���
						 strkuangfys_hff = rec.getString("changf");//���ջӷ���
						 strjiessl_hff=rec.getString("jies");
						 strxiancsl_hff = rec.getString("yingk");//��������ӷ���
						 strzhejbz_hff = rec.getString("zhejbz");//�ۼ۱�׼�ӷ���
						 strzhehje_hff = rec.getString("zhejje");//�ۺϽ��ӷ���
						 
					 }else if(rec.getString("mingc").equals("������ҷ�")){
						 
						 strhetbz_hf=rec.getString("hetbz");
						 strgongfbz_hf = rec.getString("GONGF");//������׼����
						 strkuangfys_hf =rec.getString("changf");//���շ���
						 strjiessl_hf=rec.getString("jies");
						 strxiancsl_hf = rec.getString("yingk");//�����������
						 strzhejbz_hf =rec.getString("zhejbz");//�ۼ۱�׼����
						 strzhehje_hf =rec.getString("zhejje");//�ۺϽ���
						 
					 }else if(rec.getString("mingc").equals("ȫˮ��")){
						 
						 strhetbz_sf=rec.getString("hetbz");
						 strgongfbz_sf = rec.getString("GONGF");//������׼ˮ��
						 strkuangfys_sf = rec.getString("changf");//����ˮ��
						 strjiessl_sf=rec.getString("jies");
						 strxiancsl_sf = rec.getString("YINGK");//�������ˮ��
						 strzhejbz_sf = rec.getString("zhejbz");//�ۼ۱�׼ˮ��
						 strzhehje_sf = rec.getString("zhejje");//�ۺϽ��ˮ��
					 }
				 }
				 
				 rec.close();

				 //********************����*****************//
				 strjiessl = rs.getString("jiessl");//��������
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
			 }
				 
			 	if ((blnHasMeik)&&(intLeix==1)){
					 
					 sql="select * from kuangfjsyfb where bianm='"+where+"'";
					 
					 rs=cn.getResultSet(sql);
					 if (rs.next()){
						 
						 strtielyf =rs.getString("GUOTYF");//��·�˷�
						 strzaf = rs.getString("dityf");//�ӷ�
						 strbukouqzf = rs.getString("bukyf");//��(��)��ǰ���ӷ�
						 strjiskc = rs.getString("JISKC");//��˰�۳�
						 strbuhsyf =rs.getString("buhsyf");//����˰�˷�
						 strshuil_ys = rs.getString("shuil");//˰��(�˷�)
						 strshuik_ys = rs.getString("shuik");//˰��(�˷�)
						 stryunzshj = rs.getString("hansyf");//���ӷѺϼ�
						 dblYunf=rs.getDouble("hansyf");
					 }
				 }else if(intLeix!=0){
					 
					 sql="select * from kuangfjsyfb where bianm='"+where+"'";
					 
					 rs=cn.getResultSet(sql);
					 if(rs.next()){
						 
						 lgdiancxxb_id=rs.getLong("diancxxb_id");
						 strbianh=rs.getString("bianm");
						 strjiesrq=FormatDate(rs.getDate("jiesrq"));
						 intLeix=rs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
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
						 strhuowmc = rs.getString("MEIZ");//��������
						 strxianshr = rs.getString("xianshr");//���ջ���
						 stryinhzh = rs.getString("zhangh");//�ʺ�
						 strfahsl =getGongfsl(rs.getLong("diancjsb_id"));//��������������������������������������������
						 strches = rs.getString("ches");//����
						 stryansbh = rs.getString("yansbh");//���ձ��
						 strfapbh = rs.getString("fapbh");//��Ʊ���
						 strduifdd = rs.getString("duifdd");//�Ҹ��ص�
						 strfukfs = rs.getString("fukfs") ;//���ʽ
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
	
						 strjiessl = rs.getString("gongfsl");
						 strjine = "0";//���
						 strbukouqjk = "0";//��(��)��ǰ�ۿ�
						 strjiakhj = "0";//�ۿ�ϼ�
						 strshuil_mk = "0";//˰��(ú��)
						 strshuik_mk = "0";//˰��(ú��)
						 strjialhj = "0";//��˰�ϼ�
						 strguohzl =rs.getString("GUOHL");//��������
						 strbeiz = nvlStr(rs.getString("beiz"));//��ע
						 dblMeik= Double.parseDouble(strjialhj);
				 }
			 }
			 rs.close();
			 
			 String dcsql="select bianm from diancjsmkb where id="+diancjsmkb_id; 
			 ResultSet dcrs = cn.getResultSet(dcsql);
			 if(dcrs.next()){
				 strdiancjsbh=dcrs.getString("bianm");
				 strdiancjsbh="("+strdiancjsbh+")";
			 }
			 dcrs.close();
			 Money money=new Money();
			 //����ϼ�
			 strhej_xx=format(dblYunf+dblMeik,"0.00");
			 strmeikhjdx=money.NumToRMBStr(dblMeik);
			 stryunzfhjdx=money.NumToRMBStr(dblYunf);
			 strhej_dx=money.NumToRMBStr(dblYunf+dblMeik);
			 cn.Close();
//			 A4	110*790
//			 now 901
//			 ���ƻӷ��֡��ҷ֡�ˮ��������ʾ
			 boolean hff_bn=false;
			 boolean hf_bn=false;
			 boolean sf_bn=false;
			 
			 int hff_row=9;
			 int hf_row=10;
			 int sf_row=11;
			 
			 if(strjiessl_hff.equals("")||strjiessl_hff.equals("0")){
				 hff_bn=true;
			 }
			 if(strjiessl_hf.equals("")||strjiessl_hf.equals("0")){
				 hf_bn=true;
			 }
			 if(strjiessl_sf.equals("")||strjiessl_sf.equals("0")){
				 sf_bn=true;
			 }
//			 
			 int ArrWidth[]=new int[] {110,85,65,65,65,65,75,70,76,85,70,70};
			 String ArrHeader[][]=new String[22][12];
			 ArrHeader[0]=new String[] {"������λ:"+strfahdw,"","","��վ:",strfaz,"������:",strdiabch,"","�տλ:",strshoukdw,"",""};
			 ArrHeader[1]=new String[] {"��������:"+strfahrq,"","","��������:",strdiqdm,"ԭ�ջ���:",stryuanshr,"","��������:",strkaihyh,"",""};
			 ArrHeader[2]=new String[] {"��������:"+stryansrq+"-"+stryansrq,"","","��������:",strhuowmc,"���ջ���:",strxianshr,"","�����ʺ�:",stryinhzh,"",""};
			 ArrHeader[3]=new String[] {"��������:",strfahsl,"����:"+strches,"���ձ��:",stryansbh,"��Ʊ���:",strfapbh,"","�Ҹ��ص�:",strduifdd,"���ʽ:",strfukfs};
			 ArrHeader[4]=new String[] {"��������","��������","��������","��������","��������","��������","��������","��������","��������","","",""};
			 ArrHeader[5]=new String[] {"��˰��:"+strshijfk+"(Ԫ)","��ͬ��׼","������׼","��������","�����׼","�������","�ۼ۱�׼","�ۺϽ��","��������","��������","��������","�ۺϽ��"};
			 ArrHeader[6]=new String[] {"����(KCAL/KG)",strhetbz_rl,strgongfbz_rl,strkuangfys_rl,strjiessl_rl,strxiancsl_rl,strzhejbz_rl,strzhehje_rl,"(��)","(��)","(��)","(Ԫ)"};
			 ArrHeader[7]=new String[] {"���(%)",strhetbz_lf,strgongfbz_lf,strkuangfys_lf,strjiessl_lf,strxiancsl_lf,strzhejbz_lf,strzhehje_lf,strgongfsl,strxianssl,strkuidsl,strzhehje};
			 ArrHeader[8]=new String[] {"�ӷ���(%)",strhetbz_hff,strgongfbz_hff,strkuangfys_hff,strjiessl_hff,strxiancsl_hff,strzhejbz_hff,strzhehje_hff,"","","",""};
			 ArrHeader[9]=new String[] {"�ҷ�(%)",strhetbz_hf,strgongfbz_hf,strkuangfys_hf,strjiessl_hf,strxiancsl_hf,strzhejbz_hf,strzhehje_hf,"","","",""};
			 ArrHeader[10]=new String[] {"ˮ��(%)",strhetbz_sf,strgongfbz_sf,strkuangfys_sf,strjiessl_sf,strxiancsl_sf,strzhejbz_sf,strzhehje_sf,"","","",""};
			 ArrHeader[11]=new String[] {"��������","����","���","","��(��)��ǰ�ۿ�","��(��)��ǰ�ۿ�","�ۿ�ϼ�","","˰��","˰��","��˰�ϼ�","��˰�ϼ�"};
			 ArrHeader[12]=new String[] {strjiessl,strdanj,strjine,"",strbukouqjk," ",formatq(strjiakhj),"",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
			 ArrHeader[13]=new String[] {"ú��ϼ�(��д):",strmeikhjdx,"","","","","","","","","",""};
			 ArrHeader[14]=new String[] {"��·�˷�","�ӷ�","��(��)��ǰ���ӷ�","","��˰�۳�","��˰�۳�","����˰�˷�","","˰��","˰��","���ӷѺϼ�","���ӷѺϼ�"};
			 ArrHeader[15]=new String[] {strtielyf,strzaf,strbukouqzf,"",strjiskc,"",formatq(strbuhsyf),"",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
			 ArrHeader[16]=new String[] {"���ӷѺϼ�(��д):",stryunzfhjdx,"","","","","","","","","",""};
			 ArrHeader[17]=new String[] {"�ϼ�(��д):",strhej_dx,"","","","","","","�ϼ�(Сд):",strhej_xx,"",""};
			 ArrHeader[18]=new String[] {"��ע:",strbeiz,"","","","","","","��������:",strguohzl,"",""};
			 ArrHeader[19]=new String[] {"�Ʊ���:(ǩ��)","","","�쵼���:(ǩ��)","","","","�ۺϲ���:(ǩ��)","","","",""};
			 ArrHeader[20]=new String[] {"������:"+strzhibr,"","",""+strlingdqz,"","","","������:"+strzonghcwjbr,"","","",""};
			 ArrHeader[21]=new String[] {strzhibrq,"","",strlingdqzrq,"","","",strzonghcwjbrq,"","","",""};
			 
//			 ����ҳTitle
			 
			 rt.setTitle("ȼ�ϲɹ���ⵥ",ArrWidth);
			 String tianbdw=getTianzdw(lgdiancxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
			 rt.setDefaultTitleLeft("���Ƶ�λ��"+tianbdw,3);
			 rt.setDefaultTitle(4,5,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������ڣ�"+strjiesrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(9,3,"���:"+strbianh+"  "+strdiancjsbh,Table.ALIGN_CENTER);
			 rt.setBody(new Table(ArrHeader,0,0,0));
			 rt.body.setRowHeight(24);
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
			 rt.body.mergeCell(19,10,19,12);//��ע
			 
			 rt.body.mergeCell(20,1,20,3);
			 rt.body.mergeCell(20,4,20,7);
			 rt.body.mergeCell(20,8,20,12);
			 
			 rt.body.mergeCell(21,1,21,3);
			 rt.body.mergeCell(21,4,21,7);
			 rt.body.mergeCell(21,8,21,12);
			 rt.body.mergeCell(22,1,22,3);
			 rt.body.mergeCell(22,4,22,7);
			 rt.body.mergeCell(22,8,22,12);
			 
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
			 
			 if(strlingdqz!=null){
				 if(strlingdqz.equals("�׸���")){
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.2.222:8086/ftp/lingdqz/baijlqz.gif");
				 }else if(strlingdqz.equals("��־��")){
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.2.222:8086/ftp/lingdqz/zzg.gif");
				 }
			 }
				// ����ҳ��
				_CurrentPage = iPageIndex;
//				_AllPages = rt.body.getPages();
				
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				return rt.getAllPagesHtml(iPageIndex);
			}catch(Exception e){
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
			
			String sql=" select gongf from jieszbsjb,diancjsmkb,zhibb "
			        + " where diancjsmkb.diancjsb_id= "+jiesbid+""
			        + " and diancjsmkb.id=jieszbsjb.jiesdid" 
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
	
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy�� MM�� dd��", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	��ʽ��
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
	
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
	}

	
}
