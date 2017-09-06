package com.zhiren.dtrlgs.fkgl;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
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
			 
			 String strzhiljdcldqz = "";
			 String strzhiljdcldqzrq = "";
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 sql="select * from kuangfjsmkb where bianh='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=2;
			 long intDiancjsmkId=0;
			 
			 boolean blnHasMeik =false;
			 
			 if(rs.next()){
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				 lggongsxxb_id=rs.getLong("gongsxxb_id");
				 
				 diancjsmkb_id=rs.getLong("diancjsmkb");
				 
				 strbianh=rs.getString("bianh");
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
				 strjiezysrq=rs.getString("yansjzrq");
				 if(strkaisysrq.equals(strjiezysrq)){
					 stryansrq=FormatDate(rs.getDate("yansrq"));
				 }else{
					 stryansrq=FormatDate(rs.getDate("yansrq"))+" �� "+FormatDate(rs.getDate("yansjzrq"));
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
				 strshijfk =rs.getString("SHULZJBZ");//ʵ�ʸ����������������������������������
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

				 strgongfbz_hf = rs.getString("GONGFHF");//������׼����
				 strkuangfys_hf =rs.getString("HUIF");//���շ���
				 strxiancsl_hf = rs.getString("YINGKHF");//�����������
				 strzhejbz_hf =rs.getString("HUIFYXDJ");//�ۼ۱�׼����
				 strzhehje_hf =rs.getString("HUIFYXJE");//�ۺϽ���

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
				 strguohzl =rs.getString("GUOHL");//��������
				 strbeiz = nvlStr(rs.getString("beiz"));//��ע
				 dblMeik= Double.parseDouble(strjialhj);
				 blnHasMeik=true;
				 
//				********************��Ա����*****************//
				 strzhibr=rs.getString("zhibr");
				 if(strzhibr==null){
					 strzhibr = " ";
				 }
				 strzhibrq=FormatDate(rs.getDate("zhibrq"));
				 strlingdqz=rs.getString("lingdqz");
				 if(strlingdqz==null){
					 strlingdqz = " ";
				 }
				 
				 strzhiljdcldqz=rs.getString("zhiljdcldqz");
				 if(strzhiljdcldqz==null){
					 strzhiljdcldqz = "";
				 }
				 strzhiljdcldqzrq = FormatDate(rs.getDate("zhiljdcldqzrq"));
				 
				 strlingdqzrq=FormatDate(rs.getDate("lingdqzrq"));
				 strzonghcwjbr=rs.getString("zonghcwjbr");
				 if( strzonghcwjbr==null){
					 strzonghcwjbr = " ";
				 }
				 strzonghcwjbrq=FormatDate(rs.getDate("zonghcwjbrq"));
				 
			 }
			 rs.close();
			 
			 if (blnHasMeik){
				 if(intLeix==0){
					 sql="select * from kuangfjsyf where KUANGFJSMKB_ID="+intDiancjsmkId;
				 }else{
					 sql="select * from kuangfjsyf where bianh=''";
				 }
			 }else{
				 sql="select * from kuangfjsyf where bianh='"+where+"'";
			 }
			 
			 rs = cn.getResultSet(sql);
			 if (rs.next()){
				 strtielyf =rs.getString("TIELYF");//��·�˷�
				 strzaf = rs.getString("ZAF");//�ӷ�
				 strdiqdm=rs.getString("diqbm");
				 strbukouqzf = rs.getString("BUKYQYZF");//��(��)��ǰ���ӷ�
				 strjiskc = rs.getString("JISKC");//��˰�۳�
				 strbuhsyf =rs.getString("BUHSYF");//����˰�˷�
				 strshuil_ys = rs.getString("YUNFSL");//˰��(�˷�)
				 strshuik_ys = rs.getString("YUNFSK");//˰��(�˷�)
				 stryunzshj = rs.getString("YUNZFHJ");//���ӷѺϼ�
				 strbeiz =strbeiz + nvlStr(rs.getString("beiz"));//��ע
				 dblYunf=rs.getDouble("YUNZFHJ");
				 strshijfk =rs.getString("YUNFHSDJ");
				 double tmpshijfk=Double.parseDouble(strshijfk);
				 double tmpyunfsl=Double.parseDouble(strshuil_ys);
//				 strdanj = String.valueOf((double)Math.round((tmpshijfk-(tmpshijfk*tmpyunfsl))*100)/100);//����
				 
				 if(intLeix==2){
					 
					 lgdiancxxb_id=rs.getLong("diancxxb_id");
					 lggongsxxb_id=rs.getLong("gongsxxb_id");
					 strbianh=rs.getString("bianh");
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
//					 strfahrq = rs.getString("fahrq");//��������
					 strfaz=rs.getString("faz");
					 strdiabch=rs.getString("daibch");
					 stryuanshr = rs.getString("yuanshr");//ԭ�ջ���
					 strshoukdw = rs.getString("shoukdw");//�տλ
					 strkaihyh = rs.getString("kaihyh");//��������
					 strkaisysrq=rs.getString("yansrq");
					 strjiezysrq=rs.getString("yansjzrq");
					 if(strkaisysrq.equals(strjiezysrq)){
						 stryansrq=FormatDate(rs.getDate("yansrq"));
					 }else{
						 stryansrq=FormatDate(rs.getDate("yansrq"))+" �� "+FormatDate(rs.getDate("yansjzrq"));
					 }
//					 stryansrq = rs.getString("yansrq");//��������
					 strhuowmc = rs.getString("MEIZ");//��������
					 strxianshr = rs.getString("xianshr");//���ջ���
					 stryinhzh = rs.getString("zhangh");//�ʺ�
					 strfahsl =rs.getString("gongfsl");//��������������������������������������������
					 strches = rs.getString("ches");//����
					 stryansbh = rs.getString("yansbh");//���ձ��
					 strfapbh = rs.getString("fapbh");//��Ʊ���
					 strduifdd = rs.getString("duifdd");//�Ҹ��ص�
					 strfukfs = rs.getString("fukfs") ;//���ʽ
					 strdiqdm=rs.getString("diqbm");
//					 strshijfk =rs.getString("SHULZJBZ");//ʵ�ʸ����������������������������������
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
					 strjiessl = rs.getString("gongfsl");//��������
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
					 
//					 strzhibr=rs.getString("zhibr");
					 strzhibrq=FormatDate(rs.getDate("zhibrq"));
//					 strlingdqz=rs.getString("lingdqz");
					 strlingdqzrq=FormatDate(rs.getDate("lingdqzrq"));
//					 strzonghcwjbr=rs.getString("zonghcwjbr");
					 strzonghcwjbrq=FormatDate(rs.getDate("zonghcwjbrq"));
					 
				 }
				 
			 }
			 
			 
			 String dcsql="select bianh from diancjsmkb where id="+diancjsmkb_id; 
			 ResultSet dcrs = cn.getResultSet(dcsql);
			 if(dcrs.next()){
				 strdiancjsbh=dcrs.getString("bianh");
				 strdiancjsbh="("+strdiancjsbh+")";
			 }
			 dcrs.close();
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
			 ArrHeader[3]=new String[] {"��������:",strfahsl,"����:"+strches,"���ձ��:",stryansbh,"��Ʊ���:",strfapbh,"�Ҹ��ص�:",strduifdd,"���ʽ��",strfukfs};
			 ArrHeader[4]=new String[] {"��������","��������","��������","��������","��������","��������","��������","��������","","",""};
			 ArrHeader[5]=new String[] {"��˰��:"+strshijfk,"","������׼","��������","�������","�ۼ۱�׼","�ۺϽ��","��������","��������","ӯ��������","�ۺϽ��"};
			 ArrHeader[6]=new String[] {"����(KCAL/KG)","",strgongfbz_rl,strkuangfys_rl,strxiancsl_rl,strzhejbz_rl,strzhehje_rl,"(��)","(��)","(��)","(Ԫ)"};
			 ArrHeader[7]=new String[] {"���(%)","",strgongfbz_lf,strkuangfys_lf,strxiancsl_lf,strzhejbz_lf,strzhehje_lf,strgongfsl,strxianssl,strkuidsl,strzhehje};
			 ArrHeader[8]=new String[] {"�ӷ���(%)","","","","","","","","","",""};
			 ArrHeader[9]=new String[] {"�ҷ�","",strgongfbz_hf,strkuangfys_hf,strxiancsl_hf,strzhejbz_hf,strzhehje_hf,"","","",""};
			 ArrHeader[10]=new String[] {"ˮ��(%)","","","","","","","","","",""};
			 ArrHeader[11]=new String[] {"��������","����","���","","��(��)��ǰ�ۿ�","��(��)��ǰ�ۿ�","�ۿ�ϼ�","˰��","˰��","��˰�ϼ�","��˰�ϼ�"};
			 ArrHeader[12]=new String[] {strjiessl,strdanj,strjine,"",strbukouqjk," ",formatq(strjiakhj),strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
			 ArrHeader[13]=new String[] {"ú��ϼ�(��д):",strmeikhjdx,"","","","","","","","",""};
			 ArrHeader[14]=new String[] {"�˷�","�ӷ�","��(��)��ǰ���ӷ�","","��˰�۳�","��˰�۳�","����˰�˷�","˰��","˰��","���ӷѺϼ�","���ӷѺϼ�"};
			 ArrHeader[15]=new String[] {strtielyf,strzaf,strbukouqzf,"",strjiskc,"",formatq(strbuhsyf),strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
			 ArrHeader[16]=new String[] {"���ӷѺϼ�(��д):",stryunzfhjdx,"","","","","","","","",""};
			 ArrHeader[17]=new String[] {"�ϼ�(��д):",strhej_dx,"","","","","","","�ϼ�(Сд):",strhej_xx,""};
			 ArrHeader[18]=new String[] {"��ע:",strbeiz,"","","","","","","��������:",strguohzl,""};
			 ArrHeader[19]=new String[] {"�Ʊ���:(ǩ��)","","�����ۺϴ��쵼���:(ǩ��)","","","�쵼���:(ǩ��)","","","�ۺϲ���:(ǩ��)","",""};
			 ArrHeader[20]=new String[] {"������:"+strzhibr,"",""+strzhiljdcldqz+"","","",""+strlingdqz,"","","������:"+strzonghcwjbr,"",""};
			 ArrHeader[21]=new String[] {strzhibrq,"",strzhiljdcldqzrq,"","",strlingdqzrq,"","",strzonghcwjbrq,"",""};
			 
//			 ����ҳTitle
			 
			 rt.setTitle("ȼ�ϲɹ���ⵥ",ArrWidth);
//			 rt2.setTitle("ȼ�ϲɹ����ⵥ",ArrWidth);
			 String tianbdw=getProperValue(getITianzdwModel(),lggongsxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
//			 String tianbdw=getTianzdw(lggongsxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
			 rt.setDefaultTitleLeft("���Ƶ�λ��"+tianbdw,2);
			 rt.setDefaultTitle(4,4,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������ڣ�"+strjiesrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(9,3,"���:"+strbianh+"  "+strdiancjsbh,Table.ALIGN_CENTER);
			 Table table = new Table(rs, 1, 0, 0);
			 
			 rt.setBody(table);//new Table(ArrHeader,0,true,Table.ALIGN_CENTER));
			 rt.body.setRowHeight(26);
			 rt.body.setWidth(ArrWidth);			 
			 rt.body.mergeCell(1,1,1,3);
			 rt.body.mergeCell(1,9,1,11);
			 rt.body.mergeCell(2,1,2,3);
			 rt.body.mergeCell(2,9,2,11);
			 rt.body.mergeCell(3,1,3,3);
			 rt.body.mergeCell(3,9,3,11);
			 rt.body.mergeCell(5,1,5,7);
			 rt.body.mergeCell(5,8,5,11);
			 rt.body.mergeCell(6,1,6,2);
			 rt.body.mergeCell(7,1,7,2);
			 rt.body.mergeCell(8,1,8,2);
			 rt.body.mergeCell(9,1,9,2);
			 rt.body.mergeCell(10,1,10,2);
			 rt.body.mergeCell(11,1,11,2);
			 rt.body.mergeCell(12,3,12,4);
			 rt.body.mergeCell(12,5,12,6);
			 rt.body.mergeCell(12,10,12,11);
			 rt.body.mergeCell(13,3,13,4);
			 rt.body.mergeCell(13,5,13,6);
			 rt.body.mergeCell(13,10,13,11);
			 rt.body.mergeCell(14,2,14,11);
			 rt.body.mergeCell(15,3,15,4);
			 rt.body.mergeCell(15,5,15,6);
			 rt.body.mergeCell(15,10,15,11);
			 rt.body.mergeCell(16,3,16,4);
			 rt.body.mergeCell(16,5,16,6);
			 rt.body.mergeCell(16,10,16,11);
			 rt.body.mergeCell(17,2,17,11);
			 rt.body.mergeCell(18,2,18,8);
			 rt.body.mergeCell(18,10,18,11);
			 rt.body.mergeCell(19,2,19,8);
			 rt.body.mergeCell(19,10,19,11);
			 
//			 rt.body.mergeCell(20,1,20,3);
//			 rt.body.mergeCell(20,4,20,7);
//			 rt.body.mergeCell(20,8,20,11);
//			 rt.body.mergeCell(21,1,21,3);
//			 rt.body.mergeCell(21,4,21,7);
//			 rt.body.mergeCell(21,8,21,11);
//			 rt.body.mergeCell(22,1,22,3);
//			 rt.body.mergeCell(22,4,22,7);
//			 rt.body.mergeCell(22,8,22,11);
			 
			 rt.body.mergeCell(20,1,20,2);
			 rt.body.mergeCell(20,3,20,5);
			 rt.body.mergeCell(20,6,20,8);
			 rt.body.mergeCell(20,9,20,11);
			 
			 rt.body.mergeCell(21,1,21,2);
			 rt.body.mergeCell(21,3,21,5);
			 rt.body.mergeCell(21,6,21,8);
			 rt.body.mergeCell(21,9,21,11);
			 
			 rt.body.mergeCell(22,1,22,2);
			 rt.body.mergeCell(22,3,22,5);
			 rt.body.mergeCell(22,6,22,8);
			 rt.body.mergeCell(22,9,22,11);
			 
			 
//			 rt.body.setCellBorderRight(5,1,0);
			 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,4,4,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,6,4,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,8,4,8,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.setCells(6,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
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
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/baijlqz.gif");
				 }else if(strlingdqz.equals("��־��")){
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/zzg.gif");
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

//	public String getTianzdw(long diancxxb_id) {
//		String Tianzdw="";
//		JDBCcon con=new JDBCcon();
//		try{
//			String sql="select quanc from gongsxxb where id="+diancxxb_id;
//			ResultSet rs=con.getResultSet(sql);
//			if(rs.next()){
//				
//				Tianzdw=rs.getString("quanc");
//			}
//			rs.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			con.Close();
//		}
//		return Tianzdw;
//	}
	
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

//	 ���Ƶ�λ
	private IDropDownBean _TianzdwValue;

	private boolean _Tianzdwchange = false;

	public IDropDownBean getTianzdwValue() {
		if(_TianzdwValue==null){
			_TianzdwValue=(IDropDownBean)getITianzdwModel().getOption(0);
		}
		return _TianzdwValue;
	}

	public void setTianzdwValue(IDropDownBean Value) {
		if (_TianzdwValue != Value) {

			_Tianzdwchange = true;
		}
		_TianzdwValue = Value;
	}

	private static IPropertySelectionModel _ITianzdwModel;

	public void setITianzdwModel(IPropertySelectionModel value) {
		_ITianzdwModel = value;
	}

	public IPropertySelectionModel getITianzdwModel() {
		if (_ITianzdwModel == null) {
			getITianzdwModels();
		}
		return _ITianzdwModel;
	}

	public IPropertySelectionModel getITianzdwModels() {
		String sql = "select id,quanc from "
					+" (select id,quanc,rownum+2 as xuh from diancxxb "
					+" union select id,quanc,rownum as xuh from gongsxxb) "
					+" order by xuh";
		_ITianzdwModel = new IDropDownModel(sql);
		return _ITianzdwModel;
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
