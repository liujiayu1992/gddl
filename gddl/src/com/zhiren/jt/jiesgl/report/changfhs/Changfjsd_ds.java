package com.zhiren.jt.jiesgl.report.changfhs;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author ����
 * 2009-03-25
 * ������1�������ȵ�ʹ�ô��ƹ���ͳһ���㵥��
 * 2�������˸�ʽ����������д��ǩ��������������̵�������
 * */
/**
 * @author ����
 * 2009-05-27
 * �������޸���ú�ۼ����в���С��λ����ȷ��BUG
 * */

/**
 * @author ��ΰ
 * 2009-07-05
 * ��������Թ�����㵥��һЩ�޸�
 * 
 * 	  1 meijΪ���ۺ�ļ۸�����ټ����ۼ۵��������˻�
 * 		��ÿ��ָ���
 * 		meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qnetar));
 * 		�ĳ�(������ͬ��)
 * 		meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
 * 
 * 	  2 ������㵥��ӯ���ֶ�Ϊ0ʱ��""ֵ
 * 	  
 *    3 ����˷ѽ�������� strjiesyfbz_sl���ڼ����˷ѵ���ʱ������strjiesyfbz_sl���滻 strjiesyfbz_sl
 *    
 *    4 �ж�������䵥λ�ǹ�·ʱ����ʾ���ӷ�С��
 */
/**
 * @author ���پ�
 * 2009-07-07
 * ��������ϵͳ�����Ӳ������ã�����ʾ����/���֡�
 * */
/**
 * @author ���پ�
 * 2009-07-17
 * ���������й����ƵĽ��㵥�Ϸϳ��ˣ����Ը��ص㣬���ʽ����Ϊ�ˡ���ͬ��š�
 * */

/**
 * @author  ��ΰ
 * 2009-10-31
 * ������  ��Լ����ۿ�С��λ��ʹ�䲻��������С��
 * 			
 */

/**
 * @author  ��ΰ
 * 2010-01-18
 * ������1 ����xitxxb�е����õõ����㵥(����)�н��㲿�ŵ�����
 * 		INSERT INTO xitxxb VALUES(
			getnewid(diancID),         --diancID   �糧ID
			1,
			diancID,                   --diancID   �糧ID
			'���㲿������',
			'�ƻ�Ӫ����',                --�ڽ��㵥���㲿������ʾ������
			'',
			'����',
			1,
			'ʹ��'
			)	
		
 *		2 ��ӻ�ȡVisit�ķ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-03-02
 * ʹ�÷�Χ�����������ʤ
 * ���������糧Ҫ��������㵥��ʾ����
 */
public class Changfjsd_ds extends BasePage{
	
	/**
	 * Visit
	 */
	private Object visit;
	
	public void setVisit(Object visit) {
		this.visit = visit;
	}
	
	public Object getVisit() {
		return visit;
	}
	
	/**
	 * @param where
	 * @param iPageIndex
	 * @param tables
	 * @return
	 */
	public String getChangfjsd(String where,int iPageIndex,String tables){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		  try{
			  
			 String type="";	//��־�Ž��㵥���ͣ�Ŀǰ�й�������"ZGDT",������"GD"����jzrd���Ǵ��ƹ��ʽ��ݹ�˾
			 String table1="";
			 String table2="";
			 
			 if(tables.indexOf(",")>-1){
				 
				 table1=tables.substring(0,tables.lastIndexOf(","));
				 table2=tables.substring(tables.lastIndexOf(",")+1);
			 }else{
				 
				 table1=table2=tables;
			 }
			  
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
			 
			 String strhetbz_sl="";
			 String strgongfbz_sl = "";
			 String strchangfys_sl = "";
			 String strjiesbz_sl="";
			 String strxiancsl_sl = "";
			 String strzhejbz_sl = "";
			 String strzhehje_sl = "";
			 String strjiesyfbz_sl = "0"; //�˷ѽ�������
			 
			 String strhetbz_Shulzb = "";		// ��ͬ����ָ��
			 String strxiancsl_Shulzb = "";		// ����ָ��ӯ��
			 String strzhejbz_Shulzb = "";		// ����ָ���۵���
			 String strzhehje_Shulzb = "";		// ����ָ���۽��
			 
			 String strhetbz_Qnetar="";
			 String strgongfbz_Qnetar = "";
			 String strchangfys_Qnetar = "";
			 String strjiesbz_Qnetar="";
			 String strxiancsl_Qnetar = "";
			 String strzhejbz_Qnetar = "";
			 String strzhehje_Qnetar = "";
			 
			 String strhetbz_Std = "";
			 String strgongfbz_Std = "";
			 String strchangfys_Std = "";
			 String strjiesbz_Std="";
			 String strxiancsl_Std = "";
			 String strzhejbz_Std = "";
			 String strzhehje_Std = "";
			 
			 String strhetbz_Star = "";
			 String strgongfbz_Star = "";
			 String strchangfys_Star = "";
			 String strjiesbz_Star="";
			 String strxiancsl_Star = "";
			 String strzhejbz_Star = "";
			 String strzhehje_Star = "";
			 
			 String strhetbz_Ad="";
			 String strgongfbz_Ad="";
			 String strchangfys_Ad="";
			 String strjiesbz_Ad="";
			 String strxiancsl_Ad="";
			 String strzhejbz_Ad="";
			 String strzhehje_Ad="";
			 
			 String strhetbz_Vdaf="";
			 String strgongfbz_Vdaf="";
			 String strchangfys_Vdaf="";
			 String strjiesbz_Vdaf="";
			 String strxiancsl_Vdaf="";
			 String strzhejbz_Vdaf="";
			 String strzhehje_Vdaf="";
			 
			 String strhetbz_Mt="";
			 String strgongfbz_Mt="";
			 String strchangfys_Mt="";
			 String strjiesbz_Mt="";
			 String strxiancsl_Mt="";
			 String strzhejbz_Mt="";
			 String strzhehje_Mt="";
			 
			 String strhetbz_Qgrad="";
			 String strgongfbz_Qgrad="";
			 String strchangfys_Qgrad="";
			 String strjiesbz_Qgrad="";
			 String strxiancsl_Qgrad="";
			 String strzhejbz_Qgrad="";
			 String strzhehje_Qgrad="";
			 
			 String strhetbz_Qbad="";
			 String strgongfbz_Qbad="";
			 String strchangfys_Qbad="";
			 String strjiesbz_Qbad="";
			 String strxiancsl_Qbad="";
			 String strzhejbz_Qbad="";
			 String strzhehje_Qbad="";
			 
			 String strhetbz_Had="";
			 String strgongfbz_Had="";
			 String strchangfys_Had="";
			 String strjiesbz_Had="";
			 String strxiancsl_Had="";
			 String strzhejbz_Had="";
			 String strzhehje_Had="";
			 
			 String strhetbz_Stad="";
			 String strgongfbz_Stad="";
			 String strchangfys_Stad="";
			 String strjiesbz_Stad="";
			 String strxiancsl_Stad="";
			 String strzhejbz_Stad="";
			 String strzhehje_Stad="";
			 
			 String strhetbz_Mad="";
			 String strgongfbz_Mad="";
			 String strchangfys_Mad="";
			 String strjiesbz_Mad="";
			 String strxiancsl_Mad="";
			 String strzhejbz_Mad="";
			 String strzhehje_Mad="";
			 
			 String strhetbz_Aar="";
			 String strgongfbz_Aar="";
			 String strchangfys_Aar="";
			 String strjiesbz_Aar="";
			 String strxiancsl_Aar="";
			 String strzhejbz_Aar="";
			 String strzhehje_Aar="";
			 
			 String strhetbz_Aad="";
			 String strgongfbz_Aad="";
			 String strchangfys_Aad="";
			 String strjiesbz_Aad="";
			 String strxiancsl_Aad="";
			 String strzhejbz_Aad="";
			 String strzhehje_Aad="";
			 
			 String strhetbz_Vad="";
			 String strgongfbz_Vad="";
			 String strchangfys_Vad="";
			 String strjiesbz_Vad="";
			 String strxiancsl_Vad="";
			 String strzhejbz_Vad="";
			 String strzhehje_Vad="";
			 
			 String strhetbz_T2="";
			 String strgongfbz_T2="";
			 String strchangfys_T2="";
			 String strjiesbz_T2="";
			 String strxiancsl_T2="";
			 String strzhejbz_T2="";
			 String strzhehje_T2="";
			 
			 String strhetbz_Yunju="";
			 String strgongfbz_Yunju="";
			 String strchangfys_Yunju="";
			 String strjiesbz_Yunju="";
			 String strxiancsl_Yunju="";
			 String strzhejbz_Yunju="";
			 String strzhehje_Yunju="";
			 
			 String strdanj = "";
			 String strbuhsdj = "";
			 String strjine = "";
			 String strbukouqjk = "";
			 String strjiakhj = "";
			 String strshuil_mk = "";
			 String strshuik_mk = "";
			 String strjialhj = "";
			 String strtielyf = "";
			 String strtielzf = "";
			 String strkuangqyf = "";
			 String strkuangqzf = "";
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
			 String stryuns = "";
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
			 String strJihkj="";
			 double danjc = 0;
			 String stryunsfs="";	//���䷽ʽ
			 String strChaokdl="";	//��������
			 String strChaokdlx="";	//����������
			 String strHetbh="";	//��ͬ���
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 String strkuidjfyf="";
			 String strkuidjfzf="";
			 String strbukmk = "";
			 sql="select * from "+table1+" where bianm='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=3;
			 long intDiancjsmkId=0;
			 long strkuangfjsmkb_id = -1;
			 boolean blnHasMeik =false;		//�Ƿ���ú��
			 
			 if(rs.next()){
				 
				 strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetb", "hetbh", "id", rs.getString("hetb_id")));
//				 danjc = rs.getDouble("danjc");
				 strbukmk = rs.getString("bukmk");
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
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
				 strJihkj=MainGlobal.getTableCol("jihkjb", "mingc", "id", rs.getString("jihkjb_id"));
				 
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
				 strduifdd = nvlStr(rs.getString("duifdd"));//�Ҹ��ص�
				 strfukfs =nvlStr(rs.getString("fukfs")) ;//���ʽ
				 strshijfk =rs.getString("hansdj");//ʵ�ʸ���
				 strbuhsdj=rs.getString("buhsdj");	//����˰����
				 stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
				 strChaokdl=String.valueOf(Math.abs(rs.getDouble("chaokdl")));	//��/������
				 strChaokdlx=Jiesdcz.nvlStr(rs.getString("chaokdlx"));	//����������
				 
				  sql="select jieszbsjb.*,zhibb.bianm as mingc from jieszbsjb,"+table1+",zhibb "
					 + " where jieszbsjb.jiesdid="+table1+".id and zhibb.id=jieszbsjb.zhibb_id"
			        + " and "+table1+".bianm='"+where+"' and jieszbsjb.zhuangt=1 order by jieszbsjb.id";
				 
				 ResultSet rs2=cn.getResultSet(sql);
				 while(rs2.next()){
					 
					 if(rs2.getString("mingc").equals(Locale.jiessl_zhibb)){
						 
						 strhetbz_sl = rs2.getString("hetbz");		//��ͬ��׼
						 strgongfbz_sl = rs2.getString("gongf");	//��������
						 strfahsl=strgongfbz_sl;
						 strchangfys_sl = rs2.getString("CHANGF");	//��������
						 strjiesbz_sl = rs2.getString("JIES");		//��������
						 strxiancsl_sl = String.valueOf((rs2.getDouble("YINGK")>0?(-rs2.getDouble("YINGK")):0));//��������
						 strzhejbz_sl = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_sl = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Shul_zhibb)){
					 
						 strhetbz_Shulzb = rs2.getString("hetbz");
						 strxiancsl_Shulzb = rs2.getString("YINGK");
						 strzhejbz_Shulzb = rs2.getString("ZHEJBZ");
						 strzhehje_Shulzb = rs2.getString("ZHEJJE");
					 
				 	 }else if(rs2.getString("mingc").equals(Locale.Qnetar_zhibb)){
						 
						 strhetbz_Qnetar = rs2.getString("hetbz");
						 strgongfbz_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("GONGF")));	    //��������
						 strchangfys_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("CHANGF")));	//��������
						 strjiesbz_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("jies")));		//��������
						 strxiancsl_Qnetar = rs2.getString("YINGK"); 		//�����������
						 strzhejbz_Qnetar = rs2.getString("ZHEJBZ");		//�ۼ۱�׼����
						 strzhehje_Qnetar = rs2.getString("ZHEJJE");	//�ۺϽ������
						 
					 }else if(rs2.getString("mingc").equals(Locale.Std_zhibb)){
						 
						 strhetbz_Std=rs2.getString("hetbz");
						 strgongfbz_Std = rs2.getString("GONGF");	//������׼���
						 strchangfys_Std = rs2.getString("CHANGF");	//�������
						 strjiesbz_Std=rs2.getString("jies");		//�������
						 strxiancsl_Std = rs2.getString("YINGK");	//����������
						 strzhejbz_Std = rs2.getString("ZHEJBZ");	//�ۼ۱�׼���
						 strzhehje_Std = rs2.getString("ZHEJJE");	//�ۺϽ�����
						 
					 }else if(rs2.getString("mingc").equals(Locale.Star_zhibb)){
						 
						 strhetbz_Star=rs2.getString("hetbz");
						 strgongfbz_Star = rs2.getString("GONGF");	//������׼���
						 strchangfys_Star = rs2.getString("CHANGF");	//�������
						 strjiesbz_Star = rs2.getString("jies");		//�������
						 strxiancsl_Star = rs2.getString("YINGK");	//����������
						 strzhejbz_Star = rs2.getString("ZHEJBZ");	//�ۼ۱�׼���
						 strzhehje_Star = rs2.getString("ZHEJJE");	//�ۺϽ�����
					 
					 }else if(rs2.getString("mingc").equals(Locale.Ad_zhibb)){
						 
						 strhetbz_Ad=rs2.getString("hetbz");
						 strgongfbz_Ad = rs2.getString("GONGF");//������׼�ӷ���
						 strchangfys_Ad = rs2.getString("CHANGF");//���ջӷ���
						 strjiesbz_Ad=rs2.getString("jies");//����ӷ���
						 strxiancsl_Ad = rs2.getString("YINGK");//��������ӷ���
						 strzhejbz_Ad = rs2.getString("ZHEJBZ");//�ۼ۱�׼�ӷ���
						 strzhehje_Ad = rs2.getString("ZHEJJE");//�ۺϽ��ӷ���
						 
					 }else if(rs2.getString("mingc").equals(Locale.Vdaf_zhibb)){
						 
						 strhetbz_Vdaf=rs2.getString("hetbz");
						 strgongfbz_Vdaf = rs2.getString("GONGF");//������׼����
						 strchangfys_Vdaf =rs2.getString("CHANGF");//���շ���
						 strjiesbz_Vdaf=rs2.getString("jies");//����ҷ�
						 strxiancsl_Vdaf = rs2.getString("YINGK");//�����������
						 strzhejbz_Vdaf =rs2.getString("ZHEJBZ");//�ۼ۱�׼����
						 strzhehje_Vdaf =rs2.getString("ZHEJJE");//�ۺϽ���
						 
					 }else if(rs2.getString("mingc").equals(Locale.Mt_zhibb)){
						 
						 strhetbz_Mt=rs2.getString("hetbz");
						 strgongfbz_Mt = rs2.getString("GONGF");//������׼ˮ��
						 strchangfys_Mt = rs2.getString("CHANGF");//����ˮ��
						 strjiesbz_Mt=rs2.getString("jies");//����ˮ��
						 strxiancsl_Mt = rs2.getString("YINGK");//�������ˮ��
						 strzhejbz_Mt = rs2.getString("ZHEJBZ");//�ۼ۱�׼ˮ��
						 strzhehje_Mt = rs2.getString("ZHEJJE");//�ۺϽ��ˮ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Qgrad_zhibb)){
						 
						 strhetbz_Qgrad=rs2.getString("hetbz");
						 strgongfbz_Qgrad = rs2.getString("GONGF");		//������׼
						 strchangfys_Qgrad = rs2.getString("CHANGF");	//����
						 strjiesbz_Qgrad=rs2.getString("jies");			//����
						 strxiancsl_Qgrad = rs2.getString("YINGK");		//�������
						 strzhejbz_Qgrad = rs2.getString("ZHEJBZ");		//�ۼ۱�׼
						 strzhehje_Qgrad = rs2.getString("ZHEJJE");		//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Qbad_zhibb)){
						 
						 strhetbz_Qbad=rs2.getString("hetbz");
						 strgongfbz_Qbad = rs2.getString("GONGF");		//������׼
						 strchangfys_Qbad = rs2.getString("CHANGF");	//����
						 strjiesbz_Qbad=rs2.getString("jies");			//����
						 strxiancsl_Qbad = rs2.getString("YINGK");		//�������
						 strzhejbz_Qbad = rs2.getString("ZHEJBZ");		//�ۼ۱�׼
						 strzhehje_Qbad = rs2.getString("ZHEJJE");		//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Had_zhibb)){
						 
						 strhetbz_Had=rs2.getString("hetbz");
						 strgongfbz_Had = rs2.getString("GONGF");	//������׼
						 strchangfys_Had = rs2.getString("CHANGF");	//����
						 strjiesbz_Had=rs2.getString("jies");		//����
						 strxiancsl_Had = rs2.getString("YINGK");	//�������
						 strzhejbz_Had = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_Had = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Stad_zhibb)){
						 
						 strhetbz_Stad=rs2.getString("hetbz");
						 strgongfbz_Stad = rs2.getString("GONGF");	//������׼
						 strchangfys_Stad = rs2.getString("CHANGF");	//����
						 strjiesbz_Stad = rs2.getString("jies");		//����
						 strxiancsl_Stad = rs2.getString("YINGK");	//�������
						 strzhejbz_Stad = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_Stad = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Mad_zhibb)){
						 
						 strhetbz_Mad=rs2.getString("hetbz");
						 strgongfbz_Mad = rs2.getString("GONGF");	//������׼
						 strchangfys_Mad = rs2.getString("CHANGF");	//����
						 strjiesbz_Mad = rs2.getString("jies");		//����
						 strxiancsl_Mad = rs2.getString("YINGK");	//�������
						 strzhejbz_Mad = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_Mad = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Aar_zhibb)){
						 
						 strhetbz_Aar=rs2.getString("hetbz");
						 strgongfbz_Aar = rs2.getString("GONGF");	//������׼
						 strchangfys_Aar = rs2.getString("CHANGF");	//����
						 strjiesbz_Aar = rs2.getString("jies");		//����
						 strxiancsl_Aar = rs2.getString("YINGK");	//�������
						 strzhejbz_Aar = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_Aar = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Aad_zhibb)){
						 
						 strhetbz_Aad=rs2.getString("hetbz");
						 strgongfbz_Aad = rs2.getString("GONGF");	//������׼
						 strchangfys_Aad = rs2.getString("CHANGF");	//����
						 strjiesbz_Aad = rs2.getString("jies");		//����
						 strxiancsl_Aad = rs2.getString("YINGK");	//�������
						 strzhejbz_Aad = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_Aad = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Vad_zhibb)){
						 
						 strhetbz_Vad=rs2.getString("hetbz");
						 strgongfbz_Vad = rs2.getString("GONGF");	//������׼
						 strchangfys_Vad = rs2.getString("CHANGF");	//����
						 strjiesbz_Vad = rs2.getString("jies");		//����
						 strxiancsl_Vad = rs2.getString("YINGK");	//�������
						 strzhejbz_Vad = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_Vad = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.T2_zhibb)){
						 
						 strhetbz_T2=rs2.getString("hetbz");
						 strgongfbz_T2 = rs2.getString("GONGF");	//������׼
						 strchangfys_T2 = rs2.getString("CHANGF");	//����
						 strjiesbz_T2 = rs2.getString("jies");		//����
						 strxiancsl_T2 = rs2.getString("YINGK");	//�������
						 strzhejbz_T2 = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
						 strzhehje_T2 = rs2.getString("ZHEJJE");	//�ۺϽ��
						 
					 }else if(rs2.getString("mingc").equals(Locale.Yunju_zhibb)){
						 
						 strhetbz_Yunju=rs2.getString("hetbz");
						 strgongfbz_Yunju = rs2.getString("GONGF");		//������׼
						 strchangfys_Yunju = rs2.getString("CHANGF");	//����
						 strjiesbz_Yunju = rs2.getString("jies");		//����
						 strxiancsl_Yunju = rs2.getString("YINGK");		//�������
						 strzhejbz_Yunju = rs2.getString("ZHEJBZ");		//�ۼ۱�׼
						 strzhehje_Yunju = rs2.getString("ZHEJJE");		//�ۺϽ��
						 
					 }
				 }
				 
				 rs2.close();

				 //********************����*****************//
				 strdanj = rs.getString("hansdj");		//����
				 strjine = rs.getString("meikje");		//���
				 strbukouqjk = rs.getString("bukmk");	//��(��)��ǰ�ۿ�
				 strjiakhj = rs.getString("buhsmk");	//�ۿ�ϼ�
				 strshuil_mk = rs.getString("shuil");	//˰��(ú��)
				 strshuik_mk = rs.getString("shuik");	//˰��(ú��)
				 strjialhj = rs.getString("hansmk");	//��˰�ϼ�
				 strguohzl =rs.getString("GUOHL");		//��������
				 stryuns =rs.getString("jiesslcy");		//����(������������)
				 strbeiz = nvlStr(rs.getString("beiz"));//��ע
				 dblMeik= Double.parseDouble(strjialhj);
				 strranlbmjbr=rs.getString("ranlbmjbr");
				 strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));
				 blnHasMeik=true;
				 
			 }	 
//			1, ��Ʊ����;
//			2, ú�����
//			3, �����˷�
//			4, �����˷�
			 
			 if ((blnHasMeik)&&(intLeix==1)){
				 
				 sql="select * from "+table2+" where bianm='"+where+"'";
				 
				 ResultSet rs3=cn.getResultSet(sql);
				 if (rs3.next()){
					 
					 strtielyf =rs3.getString("GUOTYF");//��·�˷�
					 strtielzf = rs3.getString("guotzf");//�ӷ�
					 strkuangqyf = rs3.getString("kuangqyf");//�����˷�
					 strkuangqzf = rs3.getString("kuangqzf");//�����ӷ�
					 strbukouqzf = rs3.getString("bukyf");//��(��)��ǰ���ӷ�
					 strjiskc = rs3.getString("JISKC");//��˰�۳�
					 strbuhsyf =rs3.getString("buhsyf");//����˰�˷�
					 strshuil_ys = rs3.getString("shuil");//˰��(�˷�)
					 strshuik_ys = rs3.getString("shuik");//˰��(�˷�)
					 stryunzshj = rs3.getString("hansyf");//���ӷѺϼ�
					 dblYunf=rs3.getDouble("hansyf");
					 strkuidjfyf = rs3.getString("kuidjfyf");
					 strkuidjfzf = rs3.getString("kuidjfzf");
					 strjiesyfbz_sl = rs3.getString("jiessl"); //�˷ѽ�������
				 }
				 rs3.close();
			 }else if(intLeix!=2){
					 
				 sql="select * from "+table2+"  where bianm='"+where+"'"; 
				 rs=cn.getResultSet(sql); 
				 	if(rs.next()){
//						 strshijfk =rs.getString("hansdj");		
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
						 
						 strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetys", "hetbh", "id", rs.getString("hetb_id")));
						 strfaz=rs.getString("faz");
						 strdiabch=rs.getString("daibch");
						 stryuanshr = rs.getString("yuanshr");//ԭ�ջ���
						 strshoukdw = rs.getString("shoukdw");//�տλ
						 strkaihyh = rs.getString("kaihyh");//��������
						 strkaisysrq=rs.getString("yansksrq");
						 strjiezysrq=rs.getString("yansjzrq");
						 
						 strgongfbz_sl=rs.getString("gongfsl");
						 strchangfys_sl=rs.getString("yanssl");
						 strjiesbz_sl=rs.getString("jiessl");
						 strxiancsl_sl=String.valueOf(-rs.getDouble("yingk"));
						 
						 
						 if(strkaisysrq.equals(strjiezysrq)){
							 stryansrq=FormatDate(rs.getDate("yansksrq"));
						 }else{
							 stryansrq=FormatDate(rs.getDate("yansksrq"))+" �� "+FormatDate(rs.getDate("yansjzrq"));
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
						 strdiqdm="";
						 strshijfk =rs.getString("hansdj");//ʵ�ʸ����������������������������������
						 stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
						 
//						 2009-3-12 zsj���˷���ʾ
						 strtielyf =rs.getString("GUOTYF");//��·�˷�
						 strtielzf = rs.getString("guotzf");//�ӷ�
						 strkuangqyf = rs.getString("kuangqyf");//�����˷�
						 strkuangqzf = rs.getString("kuangqzf");//�����ӷ�
						 strbukouqzf = rs.getString("bukyf");//��(��)��ǰ���ӷ�
						 strjiskc = rs.getString("JISKC");//��˰�۳�
						 strbuhsyf =rs.getString("buhsyf");//����˰�˷�
						 strshuil_ys = rs.getString("shuil");//˰��(�˷�)
						 strshuik_ys = rs.getString("shuik");//˰��(�˷�)
						 stryunzshj = rs.getString("hansyf");//���ӷѺϼ�
						 dblYunf=rs.getDouble("hansyf");
						 strbuhsdj=String.valueOf(CustomMaths.mul(rs.getDouble("hansdj"),CustomMaths.sub(1,rs.getDouble("shuil"))));
						 strjiesyfbz_sl = rs.getString("jiessl"); //�˷ѽ�������
						 
						 strhetbz_Qnetar = "";	//��ͬ
						 strgongfbz_Qnetar = "";//��������
						 strchangfys_Qnetar = "";//��������
						 strjiesbz_Qnetar = "";//�����׼
						 strxiancsl_Qnetar= "";//�����������
						 strzhejbz_Qnetar = "";//�ۼ۱�׼����
						 strzhehje_Qnetar = "";//�ۺϽ������
						 
						 strhetbz_Std = "";		//��ͬ
						 strgongfbz_Std = "";	//������׼���
						 strchangfys_Std = "";	//�������
						 strjiesbz_Std = "";	//�����׼
						 strxiancsl_Std = "";	//����������
						 strzhejbz_Std = "";	//�ۼ۱�׼���
						 strzhehje_Std = "";	//�ۺϽ�����
						 
						 strhetbz_Ad = "";		//��ͬ
						 strgongfbz_Ad = "";	//������׼�ӷ���
						 strchangfys_Ad = "";	//���ջӷ���
						 strjiesbz_Ad = "";		//�����׼
						 strxiancsl_Ad = "";	//��������ӷ���
						 strzhejbz_Ad = "";		//�ۼ۱�׼�ӷ���
						 strzhehje_Ad = "";		//�ۺϽ��ӷ���
	
						 strhetbz_Vdaf = "";		//������׼����
						 strgongfbz_Vdaf = "";		//������׼����
						 strchangfys_Vdaf = "";		//���շ���
						 strjiesbz_Vdaf = "";		//�����׼
						 strxiancsl_Vdaf = "";		//�����������
						 strzhejbz_Vdaf = "";		//�ۼ۱�׼����
						 strzhehje_Vdaf = "";		//�ۺϽ���
	
						 strhetbz_Mt="";
						 strgongfbz_Mt = "";		//������׼ˮ��
						 strchangfys_Mt = "";		//����ˮ��
						 strjiesbz_Mt = "";			//�����׼
						 strxiancsl_Mt = "";		//�������ˮ��
						 strzhejbz_Mt = "";			//�ۼ۱�׼ˮ��
						 strzhehje_Mt = "";			//�ۺϽ��ˮ��
						 
						 strhetbz_Qgrad="";
						 strgongfbz_Qgrad = "";		//������׼ˮ��
						 strchangfys_Qgrad = "";	//����ˮ��
						 strjiesbz_Qgrad = "";		//�����׼
						 strxiancsl_Qgrad = "";		//�������ˮ��
						 strzhejbz_Qgrad = "";		//�ۼ۱�׼ˮ��
						 strzhehje_Qgrad = "";		//�ۺϽ��ˮ��
						 
						 strhetbz_Qbad="";
						 strgongfbz_Qbad = "";		//������׼ˮ��
						 strchangfys_Qbad = "";		//����ˮ��
						 strjiesbz_Qbad = "";		//�����׼
						 strxiancsl_Qbad = "";		//�������ˮ��
						 strzhejbz_Qbad = "";		//�ۼ۱�׼ˮ��
						 strzhehje_Qbad = "";		//�ۺϽ��ˮ��
	
						 strhetbz_sl = "";			//��ͬ����
//						 strgongfbz_sl = "";		//��������
//						 strchangfys_sl ="";		//��������
//						 strjiesbz_sl = "";			//�����׼
//						 strxiancsl_sl = "";		//�������
						 strzhejbz_sl ="";			//��������
						 strzhehje_sl = "";			//�ۺϽ��
						 
						 
	
//						 strjiessl = rs.getString("jiessl");//��������
//						 strjiesbz_sl = rs.getString("gongfsl");
//						 strdanj = (double)Math.round(a);//����
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
						 
						 strkuidjfyf = rs.getString("kuidjfyf");
						 strkuidjfzf = rs.getString("kuidjfzf");
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
//			 ���Ƶ�λ�ȡ��ӷ��֡��ҷ֡�ˮ��������ʾ
			 
			 boolean Qnetar_bn=false;
			 boolean Yunju_bn=false;	//�˾�
			 boolean Shulzb_bn=false;	//����ָ��
			 boolean Ad_bn=false;
			 boolean Vdaf_bn=false;
			 boolean Mt_bn=false;
			 boolean Qgrad_bn=false;
			 boolean Qbad_bn=false;
			 boolean Had_bn=false;
			 boolean Stad_bn=false;
			 boolean Mad_bn=false;
			 boolean Aar_bn=false;
			 boolean Aad_bn=false;
			 boolean Vad_bn=false;
			 boolean T2_bn=false;
			 boolean Star_bn=false;
			 
			 //����ָ�����ڵ�����
			 int Qnetar_row=7;
			 int Std_row=8;
			 int Shulzb_row=9;
			 int Ad_row=10;
			 int Vdaf_row=11;
			 int Mt_row=12;
			 int Qgrad_row=13;
			 int Qbad_row=14;
			 int Had_row=15;
			 int Stad_row=16;
			 int Mad_row=17;
			 int Aar_row=18;
			 int Aad_row=19;
			 int Vad_row=20;
			 int T2_row=21;
			 int Yunju_row=22;
			 int Star_row=23;
			 
			 //����ָ���ֶβ���ʾ
			 
			 if(strhetbz_Shulzb.equals("")||strhetbz_Shulzb.equals("0")){
				 Shulzb_bn=true;
			 }
			 if(strjiesbz_Qnetar.equals("")||strjiesbz_Qnetar.equals("0")){
				 Qnetar_bn=true;
			 }
			 if(strjiesbz_Yunju.equals("")||strjiesbz_Yunju.equals("0")){
				 Yunju_bn=true;
			 }
			 if(strjiesbz_Ad.equals("")||strjiesbz_Ad.equals("0")){
				 Ad_bn=true;
			 }
			 if(strjiesbz_Vdaf.equals("")||strjiesbz_Vdaf.equals("0")){
				 Vdaf_bn=true;
			 }
			 if(strjiesbz_Mt.equals("")||strjiesbz_Mt.equals("0")){
				 Mt_bn=true;
			 }
			 if(strjiesbz_Qgrad.equals("")||strjiesbz_Qgrad.equals("0")){
				 Qgrad_bn=true;
			 }
			 if(strjiesbz_Qbad.equals("")||strjiesbz_Qbad.equals("0")){
				 Qbad_bn=true;
			 }
			 if(strjiesbz_Had.equals("")||strjiesbz_Had.equals("0")){
				 Had_bn=true;
			 }
			 if(strjiesbz_Stad.equals("")||strjiesbz_Stad.equals("0")){
				 Stad_bn=true;
			 }
			 if(strjiesbz_Mad.equals("")||strjiesbz_Mad.equals("0")){
				 Mad_bn=true;
			 }
			 if(strjiesbz_Aar.equals("")||strjiesbz_Aar.equals("0")){
				 Aar_bn=true;
			 }
			 if(strjiesbz_Aad.equals("")||strjiesbz_Aad.equals("0")){
				 Aad_bn=true;
			 }
			 if(strjiesbz_Vad.equals("")||strjiesbz_Vad.equals("0")){
				 Vad_bn=true;
			 }
			 if(strjiesbz_T2.equals("")||strjiesbz_T2.equals("0")){
				 T2_bn=true;
			 }
			 if(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0")){
				 
				 Star_bn=true;
			 }
			 
			 type=MainGlobal.getXitxx_item("����", "���㵥������λ", String.valueOf(lgdiancxxb_id), "ZGDT");
			 if(type.equals("JZRD")){
//					1, ��Ʊ����;
//					2, ú�����
//					3, �����˷�
//					4, �����˷�
				 String sql1="";
				 String hetbh="";
				 JDBCcon con1= new JDBCcon();
				 ResultSet rs1=null;
				 String tab="jiesb";
				  if(intLeix==3){	 //�˷�ʱ��ͬ��
					   sql1=
						  "select hetys.hetbh\n" +
						  "from jiesyfb,hetys where jiesyfb.hetb_id=hetys.id and jiesyfb.bianm='"+where+"'";
					   rs1=con1.getResultSet(sql1);
					   if(rs1.next()){
						   hetbh=rs1.getString(1);
					   }
					   tab="jiesyfb";
				  }else if(intLeix==4){//
					  hetbh="";
				  }else{//ú��ĺ�ͬ��
					   sql1=
						  "select hetb.hetbh\n" +
						  "from jiesb,hetb where jiesb.hetb_id=hetb.id and jiesb.bianm='"+where+"'";
					   rs1=con1.getResultSet(sql1);
					   if(rs1.next()){
						   hetbh=rs1.getString(1);
					   }
				  }
				  if(rs1!=null){
					  rs1.close();
				  }
				  con1.Close();
//				 ���ƹ���
					/**
					 *  
					����
					ȼ�Ϲ���
					���ܾ���
					�ܾ���
					 */

				  String sql2="select  distinct qianqztmc, caozy\n" +
				  "from (\n" + 
				  "select rownum i,t.*\n" + 
				  "from (\n" + 
				  "select *\n" + 
				  "from liucgzb\n" + 
				  "where liucgzid=(select liucgzid from  "+tab+" where bianm='"+where+"') order by id desc\n" + 
				  ")t)where i>=(\n" + 
				  "select max(i1)\n" + 
				  "from(\n" + 
				  "select  rownum i1,t1.*\n" + 
				  "from(\n" + 
				  "select liucgzb.*\n" + 
				  "from liucgzb\n" + 
				  "where liucgzid=(select liucgzid from  "+tab+" where bianm='"+where+"') order by id desc\n" + 
				  ")t1 )where liucdzbmc='�ύ' and houjztmc =(\n" + 
				  "select houjztmc\n" + 
				  "from(\n" + 
				  "select *\n" + 
				  "from liucgzb\n" + 
				  "where liucgzid=(select liucgzid from  "+tab+" where bianm='"+where+"') order by id desc\n" + 
				  ")t1 where rownum=1\n" + 
				  ") )and  qianqztmc in('ȼ�Ϲ���','����','���ܾ���','�ܾ���')\n" + 
				  "order by decode(qianqztmc,'ȼ�Ϲ���',1,'����',2,'���ܾ���',3,4)\n" ;
				 // "--�鿴��ǰ���㵥������λ�ã��ҵ��ύ������ǰλ�õ������ǰ��";
				 JDBCcon con=new JDBCcon();
				 ResultSet rs2=con.getResultSet(sql2);
				 String qianz_ranlb="";
				 String qianz_caiwb="";
				 String qianz_zhugjl="";
				 String qianz_zongjl="";
				 
				 String yij_ranlb="";
				 String yij_caiwb="";
				 String yij_zhugjl="";
				 String yij_zongjl="";
				 int i=0;
				 while(rs2.next()){ 
					 i++;
					 if(i==1){//ȼ�ϲ�
						 qianz_ranlb="<img name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_ranlb="ͬ��";
					 }else if(i==2){//����
						 qianz_caiwb="<img  name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_caiwb="ͬ��";
					 }else if(i==3){//���ܾ���
						 qianz_zhugjl="<img  name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_zhugjl="ͬ��";
					 }else{//�ܾ���
						 qianz_zongjl="<img name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_zongjl="ͬ��";
					 }
				 }

				 int ArrWidth[]=new int[] {11,80,90,90,90,90,11,80,90,90,92,90,10};
				 String ArrHeader[][]=new String[23][13];
				 ArrHeader[0]=new String[] {"������λ:"+strfahdw,"","","","��վ:"+strfaz,"","������:"+strdiabch,"","","�տλ:"+strshoukdw,"","","��<br>һ<br>��<br>ȼ<br>��<br>��<br>��<br>��<br>��<br>��<br>��<br>��<br>��"};
				 ArrHeader[1]=new String[] {"��������:"+strfahrq,"","","","��������:"+strdiqdm,"","ԭ�ջ���:"+stryuanshr,"","","��������:"+this.nvlStr(strkaihyh),"","",""};
				 ArrHeader[2]=new String[] {"��������:"+stryansrq,"","","","��������:"+strhuowmc,"","���ջ���:"+strxianshr,"","","�����ʺ�:"+this.nvlStr(stryinhzh),"","",""};
				 ArrHeader[3]=new String[] {"��������:"+strfahsl+"��"+strches+"��","","","","���ձ��:"+this.nvlStr(stryansbh),"","��Ʊ���:"+this.nvlStr(strfapbh),"","","��ͬ���:"+hetbh,"","",""};
				 ArrHeader[4]=new String[] {"��������������","","","","","","","","","","","",""};
				 ArrHeader[5]=new String[] {"","","��ͬ��׼","��","����","����","�������","","�ۼ۱�׼","�ۺϽ��","","",""};
				 ArrHeader[6]=new String[] {"����(��)","",strgongfbz_sl,strgongfbz_sl,strchangfys_sl,strjiesbz_sl,strxiancsl_sl,"",strzhejbz_sl,strzhehje_sl,"","",""};
				 ArrHeader[7]=new String[] {"����(kc/kg)","",strhetbz_Qnetar,strgongfbz_Qnetar,strchangfys_Qnetar,strjiesbz_Qnetar,strxiancsl_Qnetar,"",strzhejbz_Qnetar,strzhehje_Qnetar,"","",""};
				 ArrHeader[8]=new String[] {"���(%)","",strhetbz_Std,strgongfbz_Std,strchangfys_Std,strjiesbz_Std,strxiancsl_Std,"",strzhejbz_Std,strzhehje_Std,"","",""};
				 ArrHeader[9]=new String[] {"��˰����","","����","���","��(��)�ۿ�","�ۿ�ϼ�","","","˰��","˰��","��˰�ϼ�","",""};
				 ArrHeader[10]=new String[] {strshijfk,"",strbuhsdj,strjine,strbukouqjk,formatq(strjiakhj),"","",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"",""};
				 ArrHeader[11]=new String[] {"��·�˷�","","�ӷ�","��(��)���ӷ�","��˰�۳�","����˰�˷�","","","˰��","˰��","���ӷѺϼ�","",""};
				 ArrHeader[12]=new String[] {String.valueOf(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)+Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)),"",String.valueOf(Double.parseDouble(strtielzf.equals("")?"0":strtielzf)+Double.parseDouble(strkuangqzf.equals("")?"0":strkuangqzf)),strbukouqzf,String.valueOf(Double.parseDouble(strtielzf.equals("")?"0":strtielzf)+Double.parseDouble(strkuangqzf.equals("")?"0":strkuangqzf)),formatq(strbuhsyf),"","",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"",""};
				 ArrHeader[13]=new String[] {"�ϼ�(��д):"+strhej_dx,"","","","","","","","�ϼ�(Сд):"+strhej_xx,"","","",""};
				 ArrHeader[14]=new String[] {"��ע:"+strbeiz,"","","","","","","","��������",strguohzl,"","",""};
								 
				 ArrHeader[15]=new String[] {"��<br>��<br>��<br>��","��������","ȼ�Ϲ���","����","���ܾ���","�ܾ���","��<br>��<br>��<br>��","������:","�������:","����:","������:","�����:",""};
				 ArrHeader[16]=new String[] {"","�������",yij_ranlb,yij_caiwb,yij_zhugjl,yij_zongjl,"","","","",strranlbmjbr,"",""};
				 ArrHeader[17]=new String[] {"","ǩ��",qianz_ranlb,qianz_caiwb,qianz_zhugjl,qianz_zongjl,"","","","",strjiesrq,"",""};
				 ArrHeader[18]=new String[] {"","ǩ��","","","","","","","","","","",""};
				 ArrHeader[19]=new String[] {"","ǩ��","","","","","","","","","","",""};
					
				 ArrHeader[20]=new String[] {"���������ɲ�����д","","","","","","","","","","","",""};
				 ArrHeader[21]=new String[] {"Ӧ������ƾ֤��","Ӧ������ƾ֤��","Ӧ������ƾ֤��","Ӧ������ƾ֤��","Ӧ������ƾ֤��","Ӧ������ƾ֤��","��ע:","��ע:","��ע:","��ע:","��ע:","��ע:",""};
				 ArrHeader[22]=new String[] {"Ӧ����Ʊƾ֤��","Ӧ����Ʊƾ֤��","Ӧ����Ʊƾ֤��","Ӧ����Ʊƾ֤��","Ӧ����Ʊƾ֤��","Ӧ����Ʊƾ֤��","��ע:","��ע:","��ע:","��ע:","��ע:","��ע:",""};
//				 Report rt=new Report();
				 rt.setTitle(Locale.jiesd_title,ArrWidth);
				 String tianbdw=getTianzdw(lgdiancxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
				 rt.setDefaultTitleLeft("���Ƶ�λ��"+tianbdw,4);
//				 rt.setDefaultTitle(5,3,"�������ڣ�"+strjiesrq,Table.ALIGN_CENTER);
				 rt.setDefaultTitle(9,4,"���:"+strbianh,Table.ALIGN_RIGHT);
			
				
//				 rt.createTitle(2,ArrWidth);
//				 rt.title.setCellValue(1,1,Locale.jiesd_title,13);
//				 rt.title.setCellAlign(1,1,Table.ALIGN_CENTER);
//				 
//				 rt.title.setCellValue(2,1,"���Ƶ�λ��"+tianbdw,8);
//				 rt.title.setCellAlign(2,1,Table.ALIGN_LEFT);
//				 rt.title.setCellValue(2,9,"���:"+strbianh,5);
//				 rt.title.setCellAlign(2,9,Table.ALIGN_RIGHT);
				 rt.setBody(new Table(ArrHeader,0,0,0));
				 rt.body.setWidth(ArrWidth);	
				 
				 rt.body.mergeCell(1,1,1,4);
				 rt.body.mergeCell(1,5,1,6);
				 rt.body.mergeCell(1,7,1,9);
				 rt.body.mergeCell(1,10,1,12);
				 
				 
				 rt.body.mergeCell(2,1,2,4);
				 rt.body.mergeCell(2,5,2,6);
				 rt.body.mergeCell(2,7,2,9);
				 rt.body.mergeCell(2,10,2,12);
				 
				 
				 rt.body.mergeCell(3,1,3,4);
				 rt.body.mergeCell(3,5,3,6);
				 rt.body.mergeCell(3,7,3,9);
				 rt.body.mergeCell(3,10,3,12);
					
//				 rt.body.setCells(3,4,3,4,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(3,6,3,6,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(3,9,3,9,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(4,1,4,4);
				 rt.body.mergeCell(4,5,4,6);
				 rt.body.mergeCell(4,7,4,9);
				 rt.body.mergeCell(4,10,4,12);
				 
//				 rt.body.setCells(4,6,4,8,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(5,1,5,12);
				 
				 rt.body.mergeCell(6,1,6,2);
				 rt.body.mergeCell(6,7,6,8);
				 rt.body.mergeCell(6,10,6,12);
				 
				 rt.body.mergeCell(7,1,7,2);
				 rt.body.mergeCell(7,7,7,8);
				 rt.body.mergeCell(7,10,7,12);
				 
				 rt.body.mergeCell(8,1,8,2);
				 rt.body.mergeCell(8,7,8,8);
				 rt.body.mergeCell(8,10,8,12);
				 
				 rt.body.mergeCell(9,1,9,2);
				 rt.body.mergeCell(9,7,9,8);
				 rt.body.mergeCell(9,10,9,12);
				 
				 rt.body.mergeCell(10,1,10,2);
				 rt.body.mergeCell(10,6,10,8);
				 rt.body.mergeCell(10,11,10,12);
				 
				 rt.body.mergeCell(11,1,11,2);
				 rt.body.mergeCell(11,6,11,8);
				 rt.body.mergeCell(11,11,11,12);
				 
				 rt.body.mergeCell(12,1,12,2);
				 rt.body.mergeCell(12,6,12,8);
				 rt.body.mergeCell(12,11,12,12);
				 
				 rt.body.mergeCell(13,1,13,2);
				 rt.body.mergeCell(13,6,13,8);
				 rt.body.mergeCell(13,11,13,12);
				 
				 rt.body.mergeCell(14,1,14,8);//
				 rt.body.mergeCell(14,9,14,12);
				 
				 rt.body.mergeCell(15,1,15,8);//
				 rt.body.mergeCell(15,10,15,12);
				 
				 rt.body.mergeCell(16,1,20,1);//��������
				 rt.body.mergeCell(18,2,20,2);
				 rt.body.mergeCell(18,3,20,3);
				 rt.body.mergeCell(18,4,20,4);
				 rt.body.mergeCell(18,5,20,5);
				 rt.body.mergeCell(18,6,20,6);
				 rt.body.mergeCell(16,7,20,7);
				 rt.body.mergeCell(16,8,20,8);
				 rt.body.mergeCell(16,9,20,9);
				 rt.body.mergeCell(16,10,20,10);
				 
//				 rt.body.mergeCell(16,11,20,11);//������
				 rt.body.setCells(16,11,19,11,Table.PER_BORDER_BOTTOM,0);
				 rt.body.mergeCell(16,12,20,12);
				 
				 rt.body.mergeCell(21,1,21,12);
				 rt.body.mergeCell(22,1,22,6);
				 rt.body.mergeCell(22,7,23,12);
				 rt.body.mergeCell(23,1,23,6);
				 rt.body.mergeCell(1,13,23,13);
				 rt.body.setCells(5, 1, 12, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.body.setCells(5,1,13,12,Table.PER_ALIGN,Table.ALIGN_CENTER);//����
				 rt.body.setCells(16,1,21,7,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(1,13,1,13,Table.PER_ALIGN,Table.VALIGN_CENTER);
//				 rt.body.setCells(1,13,1,13,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(16,8,16,12,Table.PER_VALIGN,Table.VALIGN_TOP);
				 rt.body.setCells(22,7,22,7,Table.PER_VALIGN,Table.VALIGN_TOP);
				 
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_TOP,0);
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_LEFT,1);
				 rt.body.setCells(1,1,1,12,Table.PER_BORDER_TOP,2);
				 rt.body.setCells(23,1,23,12,Table.PER_BORDER_BOTTOM,2);
				 rt.body.setCells(22,7,22,7,Table.PER_BORDER_BOTTOM,2);
				 rt.body.setBorder(2, 0, 0, 0);
//				 ����ҳ��
				_CurrentPage = 1;
//					_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				// System.out.println(rt.getAllPagesHtml());
				String str="";
				rt.body.setCellValue(1, 13, "��һ��ȼ�ϲ��Ŵ浵��<br>&nbsp&nbsp ������<br>");
				str=rt.getAllPagesHtml(0+iPageIndex*3);
				rt.body.setCellValue(1, 13, "�ڶ���������֧����<br>&nbsp&nbsp ������<br>");
				str+=rt.getAllPagesHtml(1+iPageIndex*3);
				rt.body.setCellValue(1, 13, "������������Ʊ����<br>&nbsp&nbsp ������<br>");
				str+=rt.getAllPagesHtml(2+iPageIndex*3);
				_AllPages=3;
				
//				 rt.title.rows[1].height=1;
//				 rt.title.rows[1].fontSize=1;
				rt.body.setRowHeight(10);
				return str;
			 }else if(type.equals("ZGDT")){
//				 �й�����
				 
				 int ArrWidth[]=new int[] {125,70,65,65,65,65,73,70,86,75,87,59};
				 
				 String ArrHeader[][]=new String[34][12];
				 ArrHeader[0]=new String[] {"������λ:"+strfahdw,"","","��վ:",strfaz,"������:",strdiabch,"","�տλ:",strshoukdw,"",""};
				 ArrHeader[1]=new String[] {"��������:"+strfahrq,"","","��������:",strdiqdm,"ԭ�ջ���:",stryuanshr,"","��������:",strkaihyh,"",""};
				 ArrHeader[2]=new String[] {"��������:"+stryansrq,"","","��������:",strhuowmc,"���ջ���:",strxianshr,"","�����ʺ�:",stryinhzh,"",""};
//				 ArrHeader[3]=new String[] {"��������(��):",strfahsl," ����:"+strches,"���ձ��:",this.nvlStr(stryansbh),"��Ʊ���:",this.nvlStr(strfapbh),"","�Ҹ��ص�:",strduifdd,"���ʽ:",strfukfs};
				 ArrHeader[3]=new String[] {"��������(��):",strfahsl," ����:"+strches,"���ձ��:",this.nvlStr(stryansbh),"��Ʊ���:",this.nvlStr(strfapbh),"","��ͬ���:",strHetbh,"",""};
				 ArrHeader[4]=new String[] {"��������","��������","��������","��������","��������","��������","��������","��������","��������","","",""};
				 ArrHeader[5]=new String[] {"��˰��:"+strshijfk+"(Ԫ)","��ͬ��׼","������׼","��������","�����׼","�������","�ۼ۱�׼","�ۺϽ��","��������","��������","��������","�ۺϽ��"};
				 ArrHeader[6]=new String[] {""+Locale.Qnetar_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qnetar,strgongfbz_Qnetar,strchangfys_Qnetar,strjiesbz_Qnetar,strxiancsl_Qnetar,strzhejbz_Qnetar,strzhehje_Qnetar,"(��)","(��)","(��)","(Ԫ)"};
				 ArrHeader[7]=new String[] {""+Locale.Std_zhibb+"("+Locale.baifb_danw+")",strhetbz_Std,strgongfbz_Std,strchangfys_Std,strjiesbz_Std,strxiancsl_Std,strzhejbz_Std,strzhehje_Std,strgongfbz_sl,strchangfys_sl,strxiancsl_sl,strzhehje_sl};
				 ArrHeader[8]=new String[] {""+Locale.Shul_zhibb+"("+Locale.dun_danw+")",strhetbz_Shulzb,strfahsl,strchangfys_sl,strjiesbz_sl,strxiancsl_Shulzb,strzhejbz_Shulzb,strzhehje_Shulzb,"","","",""};
				 ArrHeader[9]=new String[] {""+Locale.Ad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Ad,strgongfbz_Ad,strchangfys_Ad,strjiesbz_Ad,strxiancsl_Ad,strzhejbz_Ad,strzhehje_Ad,"","","",""};
				 ArrHeader[10]=new String[] {""+Locale.Vdaf_zhibb+"("+Locale.baifb_danw+")",strhetbz_Vdaf,strgongfbz_Vdaf,strchangfys_Vdaf,strjiesbz_Vdaf,strxiancsl_Vdaf,strzhejbz_Vdaf,strzhehje_Vdaf,"","","",""};
				 ArrHeader[11]=new String[] {""+Locale.Mt_zhibb+"("+Locale.baifb_danw+")",strhetbz_Mt,strgongfbz_Mt,strchangfys_Mt,strjiesbz_Mt,strxiancsl_Mt,strzhejbz_Mt,strzhehje_Mt,"","","",""};
				 ArrHeader[12]=new String[] {""+Locale.Qgrad_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qgrad,strgongfbz_Qgrad,strchangfys_Qgrad,strjiesbz_Qgrad,strxiancsl_Qgrad,strzhejbz_Qgrad,strzhehje_Qgrad,"","","",""};
				 ArrHeader[13]=new String[] {""+Locale.Qbad_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qbad,strgongfbz_Qbad,strchangfys_Qbad,strjiesbz_Qbad,strxiancsl_Qbad,strzhejbz_Qbad,strzhehje_Qbad,"","","",""};
				 ArrHeader[14]=new String[] {""+Locale.Had_zhibb+"("+Locale.baifb_danw+")",strhetbz_Had,strgongfbz_Had,strchangfys_Had,strjiesbz_Had,strxiancsl_Had,strzhejbz_Had,strzhehje_Had,"","","",""};
				 ArrHeader[15]=new String[] {""+Locale.Stad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Stad,strgongfbz_Stad,strchangfys_Stad,strjiesbz_Stad,strxiancsl_Stad,strzhejbz_Stad,strzhehje_Stad,"","","",""};
				 ArrHeader[16]=new String[] {""+Locale.Mad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Mad,strgongfbz_Mad,strchangfys_Mad,strjiesbz_Mad,strxiancsl_Mad,strzhejbz_Mad,strzhehje_Mad,"","","",""};
				 ArrHeader[17]=new String[] {""+Locale.Aar_zhibb+"("+Locale.baifb_danw+")",strhetbz_Aar,strgongfbz_Aar,strchangfys_Aar,strjiesbz_Aar,strxiancsl_Aar,strzhejbz_Aar,strzhehje_Aar,"","","",""};
				 ArrHeader[18]=new String[] {""+Locale.Aad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Aad,strgongfbz_Aad,strchangfys_Aad,strjiesbz_Aad,strxiancsl_Aad,strzhejbz_Aad,strzhehje_Aad,"","","",""};
				 ArrHeader[19]=new String[] {""+Locale.Vad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Vad,strgongfbz_Vad,strchangfys_Vad,strjiesbz_Vad,strxiancsl_Vad,strzhejbz_Vad,strzhehje_Vad,"","","",""};
				 ArrHeader[20]=new String[] {""+Locale.T2_zhibb+"("+Locale.shesd_danw+")",strhetbz_T2,strgongfbz_T2,strchangfys_T2,strjiesbz_T2,strxiancsl_T2,strzhejbz_T2,strzhehje_T2,"","","",""};
				 ArrHeader[21]=new String[] {""+Locale.Yunju_zhibb+"("+Locale.yuanmdmgl_daw+")",strhetbz_Yunju,strgongfbz_Yunju,strchangfys_Yunju,strjiesbz_Yunju,strxiancsl_Yunju,strzhejbz_Yunju,strzhehje_Yunju,"","","",""};
				 ArrHeader[22]=new String[] {""+Locale.Star_zhibb+"("+Locale.baifb_danw+")",strhetbz_Star,strgongfbz_Star,strchangfys_Star,strjiesbz_Star,strxiancsl_Star,strzhejbz_Star,strzhehje_Star,"","","",""};
				 
				 ArrHeader[23]=new String[] {"��������","����˰����","���","","��(��)��ǰ�ۿ�","��(��)��ǰ�ۿ�","�ۿ�ϼ�","","˰��","˰��","��˰�ϼ�","��˰�ϼ�"};
				 ArrHeader[24]=new String[] {strjiesbz_sl,strbuhsdj,strjine,"",strbukouqjk," ",formatq(strjiakhj),"",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
				 ArrHeader[25]=new String[] {"ú��ϼ�(��д):",strmeikhjdx,"","","","","","","","","",""};
				 ArrHeader[26]=new String[] {"��·�˷�","��·�ӷ�","�����˷�","�����ӷ�","��(��)��ǰ���ӷ�","��(��)��ǰ���ӷ�","����˰�˷�","","˰��","˰��","���ӷѺϼ�","���ӷѺϼ�"};
				 ArrHeader[27]=new String[] {strtielyf,strtielzf,strkuangqyf,strkuangqzf,strbukouqzf,"",formatq(strbuhsyf),"",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
				 ArrHeader[28]=new String[] {"���ӷѺϼ�(��д):",stryunzfhjdx,"","","","","","","�ܸ��˷�",strkuidjfyf,"�ܸ��ӷ�",strkuidjfzf};
				 
				 if(!strChaokdlx.equals("")){
//					 ���㳬\����
					 ArrHeader[29]=new String[] {"�ϼ�(��д):",strhej_dx,"","","","","","","�ϼ�(Сд):",strhej_xx,"��/������(��)",strChaokdl};
				 }else{
//					 �����㳬\����
					 ArrHeader[29]=new String[] {"�ϼ�(��д):",strhej_dx,"","","","","","","�ϼ�(Сд):",strhej_xx,"",""};
				 }
				 
				 ArrHeader[30]=new String[] {"��ע:",strbeiz,"","","","","","","��������(��):",strguohzl,""+Locale.jiesslcy_title+"",stryuns};
				 ArrHeader[31]=new String[] {"�糧ȼ�ϲ���:(����)","","�糧������:(����)","","","�����ල��:(ǩ��)","","�쵼����:(ǩ��)","","�ۺϲ���:(ǩ��)","",""};
				 ArrHeader[32]=new String[] {"������:"+this.nvlStr(strranlbmjbr),"","������:"+strchangcwjbr,"","","������:"+strzhijzxjbr,"",""+strlingdqz,"","������:"+strzonghcwjbr,"",""};
				 ArrHeader[33]=new String[] {""+strranlbmjbrq+"","",""+strchangcwjbrq+"","","",""+strzhijzxjbrq+"","",""+strlingdqzrq+"","",""+strzonghcwjbrq+"","",""};
				 
//				 ����ҳTitle
//				 Report rt=new Report();
				 rt.setTitle(Locale.jiesd_title,ArrWidth);
				 String tianbdw=getTianzdw(lgdiancxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
				 rt.setDefaultTitleLeft("���Ƶ�λ��"+tianbdw,3);
				 rt.setDefaultTitle(5,3,"�������ڣ�"+strjiesrq,Table.ALIGN_CENTER);
				 rt.setDefaultTitle(9,4,"���:"+strbianh,Table.ALIGN_RIGHT);
				 rt.setBody(new Table(ArrHeader,0,0,0));
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
				 rt.body.mergeCell(4,10,4,12);//��ͬ���
				 rt.body.setCells(4,4,4,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(5,1,5,8);
				 rt.body.mergeCell(5,9,5,12);
				 
				 rt.body.mergeCell(23,3,23,4);
				 rt.body.mergeCell(23,5,23,6);
				 rt.body.mergeCell(23,7,23,8);
				 rt.body.mergeCell(23,11,23,12);
				 
				 rt.body.mergeCell(24,3,24,4);
				 rt.body.mergeCell(24,5,24,6);
				 rt.body.mergeCell(24,7,24,8);
				 rt.body.mergeCell(24,11,24,12);
				 
				 rt.body.setRowCells(25, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
//				 ����ָ����뷽ʽ
				 rt.body.setCells(7, 1, 23, 11, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 rt.body.setCells(7, 2, 23, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(25,3,25,4);
				 rt.body.mergeCell(25,5,25,6);
				 rt.body.mergeCell(25,7,25,8);
				 rt.body.mergeCell(25,11,25,12);
				 
				 rt.body.mergeCell(26,2,26,12);					//ú��ϼƴ�д
				 rt.body.setCells(26,12,26,12,Table.PER_BORDER_RIGHT,1);
				 rt.body.setCells(26,1,26,1,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(27,5,27,6);
				 rt.body.mergeCell(27,7,27,8);
				 rt.body.mergeCell(27,11,27,12);
				 
				 rt.body.mergeCell(28,5,28,6);
				 rt.body.mergeCell(28,7,28,8);
				 rt.body.mergeCell(28,11,28,12);
				 
				 rt.body.mergeCell(29,2,29,8);//�˷Ѻϼƴ�д
				 rt.body.mergeCell(30,2,30,8);
				 
				 if(strChaokdlx.equals("")){
//					 �����㳬����
					 rt.body.mergeCell(30,10,30,12);
				 }
				 
				 rt.body.mergeCell(31,2,31,8);
				 rt.body.mergeCell(32,1,32,2);
				 rt.body.mergeCell(32,3,32,5);
				 rt.body.mergeCell(32,6,32,7);
				 rt.body.mergeCell(32,8,32,9);
				 rt.body.mergeCell(32,10,32,12);
				 
				 
				 rt.body.mergeCell(33,1,33,2);
				 rt.body.mergeCell(33,3,33,5);
				 rt.body.mergeCell(33,6,33,7);
				 rt.body.mergeCell(33,8,33,9);
				 rt.body.mergeCell(33,10,33,12);
				 
				 rt.body.mergeCell(34,1,34,2);
				 rt.body.mergeCell(34,3,34,5);
				 rt.body.mergeCell(34,6,34,7);
				 rt.body.mergeCell(34,8,34,9);
				 rt.body.mergeCell(34,10,34,12);
				 
				 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.setCells(5, 1, 13, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.body.setCells(6,1,12,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(24,1,24,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(27,1,27,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(28,1,28,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(23,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(32,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(33,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(33,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(34,Table.PER_ALIGN,Table.ALIGN_RIGHT);
				 
//				 ����������
				 if(Shulzb_bn){
					 rt.body.setRowCells(Shulzb_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Shulzb_row,0);
					 rt.body.rows[Shulzb_row].hidden=true;
				 }
				 if(Ad_bn){
					 
					 rt.body.setRowCells(Ad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Ad_row,0);
					 rt.body.rows[Ad_row].hidden=true;
				 }
				 if(Vdaf_bn){
					 
					 rt.body.setRowCells(Vdaf_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Vdaf_row,0);
					 rt.body.rows[Vdaf_row].hidden=true;
				 }
				 if(Mt_bn){
					 
					 rt.body.setRowCells(Mt_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Mt_row,0);
					 rt.body.rows[Mt_row].hidden=true;
				 }
				 if(Qgrad_bn){
					 
					 rt.body.setRowCells(Qgrad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Qgrad_row,0);
					 rt.body.rows[Qgrad_row].hidden=true;
				 }
				 if(Qbad_bn){
					 
					 rt.body.setRowCells(Qbad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Qbad_row,0);
					 rt.body.rows[Qbad_row].hidden=true;
				 }
				 if(Had_bn){
					 
					 rt.body.setRowCells(Had_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Had_row,0);
					 rt.body.rows[Had_row].hidden=true;
				 }
				 if(Stad_bn){
					 
					 rt.body.setRowCells(Stad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Stad_row,0);
					 rt.body.rows[Stad_row].hidden=true;
				 }
				 if(Star_bn){
					 
					 rt.body.setRowCells(Star_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Star_row,0);
					 rt.body.rows[Star_row].hidden=true;
				 }
				 if(Mad_bn){
					 
					 rt.body.setRowCells(Mad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Mad_row,0);
					 rt.body.rows[Mad_row].hidden=true;
				 }
				 if(Aar_bn){
					 
					 rt.body.setRowCells(Aar_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Aar_row,0);
					 rt.body.rows[Aar_row].hidden=true;
				 }
				 if(Aad_bn){
					 
					 rt.body.setRowCells(Aad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Aad_row,0);
					 rt.body.rows[Aad_row].hidden=true;
				 }
				 if(Vad_bn){
					 
					 rt.body.setRowCells(Vad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Vad_row,0);
					 rt.body.rows[Vad_row].hidden=true;
				 }
				 if(T2_bn){
					 
					 rt.body.setRowCells(T2_row, Table.PER_USED, false);
					 rt.body.setRowHeight(T2_row,0);
					 rt.body.rows[T2_row].hidden=true;
				 }
				 
				 if(Yunju_bn){
				 
					 rt.body.setRowCells(Yunju_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Yunju_row,0);
					 rt.body.rows[Yunju_row].hidden=true;
				 }
				 
			 }else if(type.equals("GD")){
//				 �������
				 if(lgdiancxxb_id==476){
//					 ��ʱ���������糧
					 
					 String strGonghdc="";//��������
					 String strYunsdw="";//���䵥λ
					 String strHansdj="";//��˰����
					 String strBuhsdj="";//����˰����
					 JDBCcon cn1 = new JDBCcon();
					 String sql1=
	                       "SELECT MAX(YD.MINGC) AS YUNSDW FROM CHEPB CP,YUNSDWB YD\n" +
	                       "       WHERE CP.YUNSDWB_ID = YD.ID\n" + 
	                       "             AND CP.ID IN (\n" + 
	                       "SELECT CHEPB_ID FROM DANJCPB WHERE YUNFJSB_ID = (select id from jiesyfb where bianm ='"+where+"'))";
					 ResultSetList rs1 = cn1.getResultSetList(sql1);
					 while(rs1.next()){
						 strYunsdw = rs1.getString("YUNSDW");
					 }
					 rs1.close();
					 String sql2 = 
						 "select m.mingc as mingc from jiesyfb jy,meikxxb m where jy.meikxxb_id = m.id and jy.bianm='"+where+"'";
					 ResultSetList rs2 = cn1.getResultSetList(sql2);
					 while(rs2.next()){
						 strGonghdc = rs2.getString("mingc");
					 }
					 rs2.close();
					 String sql3=
						 "select  jy.hansdj,(jy.hansdj-round_new(jy.hansdj*jy.shuil,2)) as buhsdj  from jiesyfb jy where jy.bianm='"+where+"'";
					 ResultSetList rs3 = cn1.getResultSetList(sql3);
					 while(rs3.next()){
						 strHansdj = rs3.getString("hansdj");
						 strBuhsdj = rs3.getString("buhsdj");
					 }
					 
					 String tianbdw=getTianzdw(lgdiancxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
					 if (strzhejbz_sl.equals("")){
						 strzhejbz_sl="0";
					 }
					 double meij=Double.parseDouble(strzhejbz_sl);//ú��
					 double mkoukxj=0;//�ۿ�С��
					 boolean bTl_yunsfs=false;
					 boolean bGl_yunsfs=false; 
					 
					 if(!strzhejbz_Qnetar.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qnetar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
	//					 meij=meij+Double.parseDouble(strzhejbz_Qnetar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qnetar));
					 }
					 
					 if(!strzhejbz_Std.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Std));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Std));
	//					 meij+=Double.parseDouble(strzhejbz_Std);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Std));
					 }
					 
					 if(!strzhejbz_Ad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Ad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Ad));
	//					 meij+=Double.parseDouble(strzhejbz_Ad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Ad));
					 }
					 
					 if(!strzhejbz_Vdaf.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vdaf));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vdaf));
	//					 meij+=Double.parseDouble(strzhejbz_Vdaf);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vdaf));
					 }
					 
					 if(!strzhejbz_Mt.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mt));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mt));
	//					 meij+=Double.parseDouble(strzhejbz_Mt);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mt));
					 }
					 
					 if(!strzhejbz_Qgrad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qgrad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qgrad));
	//					 meij+=Double.parseDouble(strzhejbz_Qgrad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qgrad));
					 }
					 
					 if(!strzhejbz_Qbad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qbad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qbad));
	//					 meij+=Double.parseDouble(strzhejbz_Qbad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qbad));
					 }
					 
					 if(!strzhejbz_Had.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Had));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Had));
	//					 meij+=Double.parseDouble(strzhejbz_Had);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Had));
					 }
					 
					 if(!strzhejbz_Stad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Stad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Stad));
	//					 meij+=Double.parseDouble(strzhejbz_Stad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Stad));
					 }
					 
					 if(!strzhejbz_Mad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mad));
	//					 meij+=Double.parseDouble(strzhejbz_Mad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mad));
					 }
					 
					 if(!strzhejbz_Aar.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aar));
	//					 meij+=Double.parseDouble(strzhejbz_Aar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aar));
					 }
					 
					 if(!strzhejbz_Aad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aad));
	//					 meij+=Double.parseDouble(strzhejbz_Aad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aad));
					 }
					 
					 if(!strzhejbz_Vad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vad));
	//					 meij+=Double.parseDouble(strzhejbz_Vad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vad));
					 }
					 
					 if(!strzhejbz_T2.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_T2));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_T2));
	//					 meij+=Double.parseDouble(strzhejbz_T2);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_T2));
					 }
					 
					 if(!strzhejbz_Yunju.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Yunju));
	//					 meij+=Double.parseDouble(strzhejbz_Yunju);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Yunju));
					 }
					 
					 if(!strzhejbz_Star.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Star));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Star));
	//					 meij+=Double.parseDouble(strzhejbz_Star);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Star));
					 }
					 
					 //��ʽ�ۿ�С�ƣ���������С��λ
					 mkoukxj = CustomMaths.Round_new(mkoukxj, 2);
					 
					 if(stryunsfs.equals(Locale.tiel_yunsfs)){
	//					 ��·
						 bTl_yunsfs=true;
					 }else if(stryunsfs.equals(Locale.gongl_yunsfs)){
						 
						 bGl_yunsfs=true;
					 }
					 
					 
					 String Relkk="";	//�����ۿ�
					 String Huifkk="";	//�ҷֿۿ�
					 String Huiffkk="";	//�ӷ��ֿۿ�
					 String Shuifkk="";	//ˮ�ֿۿ�
					 String liufkk="";	//��ֿۿ�
					 String Qitkk="";	//�����ۿ�
					 
					 Relkk=String.valueOf((-Double.parseDouble(strzhejbz_Qnetar.equals("")?"0":strzhejbz_Qnetar))
							 +(-Double.parseDouble(strzhejbz_Qgrad.equals("")?"0":strzhejbz_Qgrad))
							 +(-Double.parseDouble(strzhejbz_Qbad.equals("")?"0":strzhejbz_Qbad)));
					 
					 Huifkk=String.valueOf((-Double.parseDouble(strzhejbz_Ad.equals("")?"0":strzhejbz_Ad))
							 +(-Double.parseDouble(strzhejbz_Aad.equals("")?"0":strzhejbz_Aad)));
					 
					 Huiffkk=String.valueOf((-Double.parseDouble(strzhejbz_Vdaf.equals("")?"0":strzhejbz_Vdaf))
							 +(-Double.parseDouble(strzhejbz_Vad.equals("")?"0":strzhejbz_Vad)));
					 
					 Shuifkk=String.valueOf((-Double.parseDouble(strzhejbz_Mt.equals("")?"0":strzhejbz_Mt))
							 +(-Double.parseDouble(strzhejbz_Mad.equals("")?"0":strzhejbz_Mad)));
					 
					 liufkk=String.valueOf((-Double.parseDouble(strzhejbz_Std.equals("")?"0":strzhejbz_Std))
							 +(-Double.parseDouble(strzhejbz_Stad.equals("")?"0":strzhejbz_Stad))
							 +(-Double.parseDouble(strzhejbz_Star.equals("")?"0":strzhejbz_Star)) 
					 		);
					 
					 Qitkk=String.valueOf((-Double.parseDouble(strzhejbz_T2.equals("")?"0":strzhejbz_T2))
							 +(-Double.parseDouble(strzhejbz_Yunju.equals("")?"0":strzhejbz_Yunju)));
					 
					 
					 if(Relkk.equals("-0.0")){
						 
						 Relkk=""; 
					 }
					 
					 if(Huifkk.equals("-0.0")){
						 
						 Huifkk="";
					 }
					 
					 if(Huiffkk.equals("-0.0")){
						 
						 Huiffkk="";
					 }
					 
					 if(Shuifkk.equals("-0.0")){
						 
						 Shuifkk="";
					 }
					 
					 if(liufkk.equals("-0.0")){
											 
						 liufkk="";
					 }
					
					 if(Qitkk.equals("-0.0")){
						 
						 Qitkk="";
					 }
					 
					 
	//				 ����liuf���ۿ��ۼ�
					 if((strjiesbz_Std.equals("")||strjiesbz_Std.equals("0"))
							 &&(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0"))){
						 
						 strjiesbz_Std="";
					 }else if(!strjiesbz_Star.equals("")
							 &&!strjiesbz_Star.equals("0")){
						 
						 if(!strjiesbz_Std.equals("")){
							 
							 strjiesbz_Std=String.valueOf(Double.parseDouble(strjiesbz_Star)
							 				+Double.parseDouble(strjiesbz_Std));
						 }else{
							 
							 strjiesbz_Std=strjiesbz_Star;
						 }
					 }
					 
	//				 ����Ľ��㵥Ҫ��ʾ�������ۼ�ָ��֮�������ָ��
	//				 2010-01-19 ww
	//				 ���Aar��Starָ�꣬������㵥����ʾ�յ���ָ��
					 String JieszbArray[] = null;
					 JieszbArray = getJieszbxx(table1,where);
					 
	//				 ֱȡdanpcjsmxb�е���ֵ
	//				 if(strjiesbz_Qnetar.equals("")){
						 
						 strjiesbz_Qnetar = JieszbArray[0];
	//				 }
					 
					 if(strjiesbz_Ad.equals("")){
						 
						 strjiesbz_Ad = JieszbArray[8];
					 }
					 
					 if(strjiesbz_Aar.equals("")) {
						 strjiesbz_Aar = JieszbArray[9];
					 }
					 
					 if(strjiesbz_Vdaf.equals("")){
						 
						 strjiesbz_Vdaf = JieszbArray[4];
					 }
					 
					 if(strjiesbz_Mt.equals("")){
						 
						 strjiesbz_Mt = JieszbArray[5];
					 }
					 
					 if(strjiesbz_Std.equals("")){
						 
						 strjiesbz_Std = JieszbArray[1];
					 }
					 
					 if(strjiesbz_Star.equals("")) {
						 strjiesbz_Star = JieszbArray[3];
					 }
					 if (strjiesbz_Qnetar.equals("")){
						 strjiesbz_Qnetar="0";
					 }
					 if (strjiesbz_Aar.equals("")){
						 strjiesbz_Aar="0";
					 }
					 if (strjiesbz_Vdaf.equals("")){
						 strjiesbz_Vdaf="0";
					 }
					 if (strjiesbz_Mt.equals("")){
						 strjiesbz_Mt="0";
					 }
					 if (strjiesbz_Star.equals("")){
						 strjiesbz_Star="0";
					 }
					 
					 if(intLeix!=Locale.meikjs_feiylbb_id && intLeix!=Locale.liangpjs_feiylbb_id){
//						 �������Ͳ�������Ʊ��ú��
						 
//						 ���˷ѽ��㵥����ʾָ����Ϣ
						 long jiesyfb_id = 0;
						 jiesyfb_id = MainGlobal.getTableId(table2, "bianm", where);
						 
						 if("".equals(strchangfys_Yunju)){
//							 �õ��˾�
							 strchangfys_Yunju = MainGlobal.getTableCol(table2, "nvl(yunj,0)", "id = "+jiesyfb_id);
							 
							 if(Double.parseDouble(strchangfys_Yunju)==0){
								 
								 sql1 = 
									 "SELECT nvl(max(zhi),0) AS lic FROM licb\n" +
									 "       WHERE liclxb_id = (SELECT ID FROM liclxb WHERE mingc='����')\n" + 
									 "             AND licb.faz_id = (SELECT id FROM chezxxb WHERE mingc='"+strfaz+"')";
								 
								 rs3 = cn1.getResultSetList(sql1);
								 if(rs3.next()){
									 
									 strchangfys_Yunju = rs3.getString("lic");
								 }
							 }
						 }
						 
						 sql1 = 
							 "SELECT\n" +
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(qnet_ar,0))/SUM(jingz),2)) AS qnet_ar,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(aar,0))/SUM(jingz),2)) AS aar,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(vdaf,0))/SUM(jingz),2)) AS vdaf,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(mt,0))/SUM(jingz),1)) AS mt,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(Star,0))/SUM(jingz),2)) AS star\n" + 
							 " FROM\n" + 
							 "--����\n" + 
							 "(SELECT fahb_id,SUM(maoz-piz-zongkd) AS jingz FROM chepb WHERE ID IN (\n" + 
							 "  SELECT chepb_id FROM danjcpb d\n" + 
							 "         WHERE d.yunfjsb_id = "+jiesyfb_id+")\n" + 
							 "         GROUP BY fahb_id) sl,\n" + 
							 "\n" + 
							 "--����\n" + 
							 "(SELECT fahb.id AS fahb_id,zhilb.* FROM fahb,zhilb WHERE fahb.zhilb_id = zhilb.id AND fahb.ID IN (\n" + 
							 "       SELECT fahb_id FROM chepb WHERE ID IN (\n" + 
							 "              SELECT chepb_id FROM danjcpb d\n" + 
							 "                     WHERE d.yunfjsb_id = "+jiesyfb_id+"))\n" + 
							 ") zl\n" + 
							 "WHERE sl.fahb_id = zl.fahb_id";
						 
						 rs3 = cn1.getResultSetList(sql1);
						 
						 if(rs3.next()){
							 
							 if(strjiesbz_Qnetar.equals("0")){
								 
								 strjiesbz_Qnetar = rs3.getString("qnet_ar");
							 }
							 if(strjiesbz_Aar.equals("0")){
								 
								 strjiesbz_Aar = rs3.getString("aar");
							 }
							 if(strjiesbz_Vdaf.equals("0")){
								 
								 strjiesbz_Vdaf = rs3.getString("vdaf");
							 }
							 if(strjiesbz_Mt.equals("0")){
								 
								 strjiesbz_Mt = rs3.getString("mt");
							 }
							 if(strjiesbz_Star.equals("0")){
								 
								 strjiesbz_Star = rs3.getString("star");
							 }
						 }
					 }
					 rs3.close();
					 cn1.Close();
					 
	//				 ������Mj/kg
					 strjiesbz_Qnetar=String.valueOf(MainGlobal.kcalkg_to_Mjkg(Double.parseDouble(strjiesbz_Qnetar), 
							 ((Visit)this.getVisit()).getFarldec()));		//��������
					 
					 //����xitxxb�е����õõ����㵥�н��㲿�ŵ�����
					 String BuM = "ȼ�ϲ�";
					 BuM = MainGlobal.getXitxx_item("����", "���㲿������", ""+lgdiancxxb_id, BuM);
					 
					 String ArrHeader[][]=new String[6][19];
					 ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[1]=new String[] {"�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾"};
					 ArrHeader[2]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
					 ArrHeader[3]=new String[] {"ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��"};
					 ArrHeader[4]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[5]=new String[] {"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"���ڣ�"+stryansrq,"���ڣ�"+stryansrq,"���ڣ�"+stryansrq,"���ڣ�"+stryansrq,"��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��ţ�"+strbianh,"��ţ�"+strbianh,"��ţ�"+strbianh,"��ţ�"+strbianh};
	
					 String ArrBody[][]=new String[19][19];
					 ArrBody[0]=new String[] {"���㲿�ţ�" + BuM,"","","","������λ��"+strfahdw,"","","","����������"+strGonghdc,"","","","���䵥λ��"+strYunsdw,"","","","Ʒ�֣�"+strhuowmc,"",""};
					 ArrBody[1]=new String[] {"����","","","","","����","","","","","","","","","","","ú��","","˰��"};
					 ArrBody[2]=new String[] {"Ʊ��","","","","","","�ۿ�","","","","","","","���ۺϼ�","����˰��","","","",""};
					 ArrBody[3]=new String[] {"����","����","ӯ��","����","ʵ��","ú��","��ֵ","�ҷ�","�ӷ���","ˮ��","����","����","С��","","","","","",""};
					 ArrBody[4]=new String[] {""+strches+"",""+strfahsl+"",""+("0.0".equals(strxiancsl_sl)?"":-Double.parseDouble(strxiancsl_sl)+"")+"",""+(Double.parseDouble(strxiancsl_sl)<0?"":""+strxiancsl_sl)+""+"",""+strchangfys_sl+"",""+meij+"",""+Relkk+"",""+Huifkk+"",""+Huiffkk+"",""+Shuifkk+"",""+liufkk+"",""+Qitkk+"",""+mkoukxj+"",""+strzhejbz_sl+"",""+strbuhsdj+"","",""+formatq(strjiakhj)+"","",""+formatq(strshuik_mk)+""};
					 ArrBody[5]=new String[] {"��ֵ","�ҷ�","�ӷ���","ˮ��","����","Ӧ���ۿ�","","Ӧ��˰��","","�����ۿ�","","ʵ�����","","","","","","",""};
					 ArrBody[6]=new String[] {""+ (((Visit)this.getVisit()).getFarldec()==3 ? new DecimalFormat("0.000").format(Double.parseDouble(strjiesbz_Qnetar)) : strjiesbz_Qnetar)+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Aar))+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Vdaf))+"",""+new DecimalFormat("0.0").format(Double.parseDouble(strjiesbz_Mt))+"",""+new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Star))+"",""+strjiakhj+"","",""+formatq(strshuik_mk)+"","","","",""+formatq(strjialhj)+"","","","","","","",""};
					 ArrBody[7]=new String[] {"�˾�(km)","","�˷ѵ�����ϸ","","","","","","","","","","","","","","","","ӡ��˰"};
					 ArrBody[8]=new String[] {"����","����","����","����","����","ר��","����","����","�����˷�","","","","","","���ӷ�(Ԫ/��)","","","",""};
					 ArrBody[9]=new String[] {"","","","","","","","","�總��","��ɳ","��װ","����","����","С��","ȡ�ͳ�","�����","���ۺϼ�","����˰��",""};
					 ArrBody[10]=new String[] {strchangfys_Yunju,"",""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"","",""+CustomMaths.Round_New(Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)+"","","",""+(bGl_yunsfs?(strshijfk.equals("")||strshijfk.equals("0"))?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):strshijfk:"")+"","","","","","",
							 					""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(stryunzshj.equals("")?"0":stryunzshj)/Double.parseDouble(strches)==0?1:Double.parseDouble(strches),2)):"")+"","","",""+strHansdj+"",""+strBuhsdj+"",""};
					 ArrBody[11]=new String[] {"�����˷�","","�����˷�","","�����˷�","","ר���˷�","","��;�˷�","","�����˷�","","�����˷�","","���ӷ�","","�ۿ�","","ʵ���˷ѽ��"};
					 ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","���ַ�","����",""};
					 ArrBody[13]=new String[] {""+(bTl_yunsfs?strtielyf:"")+"","","","",""+strkuangqyf+"","","","","","",""+(bGl_yunsfs?strtielyf:"")+"","","","","","",""+strbukouqzf+"",""+strbukmk+"",""+stryunzshj+""};
					 ArrBody[14]=new String[] {"ע�����ݱ���ҵ�ʽ�����ƶ�Ȩ�����л����������򣬻����ż�������д�������ǩ�������ڡ�","","","","","","","","","","","","","","","","","",""};
					 ArrBody[15]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrBody[16]=new String[] {"","","","","","","","","","���񲿣�","","","ȼ�����䲿��","","","","�����ˣ�","",""};
					 ArrBody[17]=new String[] {"�ܾ���","","","","","�����쵼��","","","","","","","","","","","","",""};
					 ArrBody[18]=new String[] {"","","","","","","","","","�ƻ���Ӫ����","","","������������","","","","�����ˣ�","",""};
					 
					 int ArrWidth[]=new int[] {54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54};
	
	//				 ����ҳTitle
					 rt.setTitle(new Table(ArrHeader,0,0,0));
					 rt.setBody(new Table(ArrBody,0,0,0));
					 rt.body.setWidth(ArrWidth);
					 rt.title.setWidth(ArrWidth);
	//				 �ϲ���Ԫ��
	//				 ��ͷ_Begin
	//				 rt.title.merge(1, 1, 1, 19);
					 rt.title.merge(2, 1, 2, 19);
					 rt.title.merge(3, 1, 3, 19);
					 rt.title.merge(4, 1, 4, 19);
					 rt.title.merge(5, 1, 5, 19);
					 
					 rt.title.merge(6, 1, 6, 4);
					 rt.title.merge(6, 5, 6, 8);
					 rt.title.merge(6, 9, 6, 15);
					 rt.title.merge(6, 16, 6, 19);
					 
					 rt.title.setBorder(0,0,0,0);
					 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
					 
					 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
					 
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
	//				 ����
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTNAME, "����");
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTSIZE, 11);
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTNAME, "Arial Unicode MS");
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTSIZE, 12);
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTNAME, "����");
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTSIZE, 20);
	//				 ����				 
					 
	//				 ͼƬ
					 rt.title.setCellImage(1, 1, 110, 50, "imgs/report/GDBZ.gif");	//����ı�־�����ֳ�Ҫһ�����Ͼ����ˣ�
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 rt.title.setCellImage(5, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
	//				 ͼƬ_End
					 
	//				 ��ͷ_End
	//				 ����_Begin
					 
					 rt.body.mergeCell(1,1,1,4);
					 rt.body.mergeCell(1,5,1,8);
					 rt.body.mergeCell(1,9,1,12);
					 rt.body.mergeCell(1,13,1,16);
					 rt.body.mergeCell(1,17,1,19);
					 rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
					 rt.body.mergeCell(2,1,2,5);
					 rt.body.mergeCell(2,6,2,16);
					 rt.body.mergeCell(2,17,4,18);
					 rt.body.mergeCell(2,19,4,19);
					 rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(3,1,3,2);
					 rt.body.mergeCell(3,7,3,13);
					 rt.body.mergeCell(3,14,4,14);
					 rt.body.mergeCell(3,15,4,16);
					 rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(5,15,5,16);
					 rt.body.mergeCell(5,17,5,18);
					 rt.body.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(6,6,6,7);
					 rt.body.mergeCell(6,8,6,9);
					 rt.body.mergeCell(6,10,6,11);
					 rt.body.mergeCell(6,12,6,14);
					 rt.body.mergeCell(6,15,6,16);
					 rt.body.mergeCell(6,17,6,18);
					 rt.body.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(7,6,7,7);
					 rt.body.mergeCell(7,8,7,9);
					 rt.body.mergeCell(7,10,7,11);
					 rt.body.mergeCell(7,12,7,14);
					 rt.body.mergeCell(7,15,7,16);
					 rt.body.mergeCell(7,17,7,18);
					 rt.body.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(8,1,8,2);
					 rt.body.mergeCell(8,3,8,18);
					 rt.body.mergeCell(8,19,10,19);
					 rt.body.setRowCells(8, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(9,1,10,1);
					 rt.body.mergeCell(9,2,10,2);
					 rt.body.mergeCell(9,3,10,3);
					 rt.body.mergeCell(9,4,10,4);
					 rt.body.mergeCell(9,5,10,5);
					 rt.body.mergeCell(9,6,10,6);
					 rt.body.mergeCell(9,7,10,7);
					 rt.body.mergeCell(9,8,10,8);
					 rt.body.mergeCell(9,9,9,14);
					 rt.body.mergeCell(9,15,9,18);
					 rt.body.setRowCells(9, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(11, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(12,1,13,2);
					 rt.body.mergeCell(12,3,13,4);
					 rt.body.mergeCell(12,5,13,6);
					 rt.body.mergeCell(12,7,13,8);
					 rt.body.mergeCell(12,9,13,10);
					 rt.body.mergeCell(12,11,13,12);
					 rt.body.mergeCell(12,13,13,14);
					 rt.body.mergeCell(12,15,13,16);
					 rt.body.mergeCell(12,17,12,18);
					 rt.body.mergeCell(12,19,13,19);
					 rt.body.setRowCells(12, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(13, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(14,1,14,2);
					 rt.body.mergeCell(14,3,14,4);
					 rt.body.mergeCell(14,5,14,6);
					 rt.body.mergeCell(14,7,14,8);
					 rt.body.mergeCell(14,9,14,10);
					 rt.body.mergeCell(14,11,14,12);
					 rt.body.mergeCell(14,13,14,14);
					 rt.body.mergeCell(14,15,14,16);
					 rt.body.setRowCells(14, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(15,1,15,19);
					 rt.body.setRowCells(15, Table.PER_BORDER_BOTTOM, 2);
					 
	//				 ��ע����(����һ��)
					 rt.body.mergeCell(16,1,16,19);
					 rt.body.setRowHeight(16,8);
					 rt.body.setRowCells(16, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(16, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.setBorder(0, 0, 2, 0);
					 rt.body.setCells(1, 1, 15, 1, Table.PER_BORDER_LEFT, 2);
					 rt.body.setCells(1, 19, 15, 19, Table.PER_BORDER_RIGHT, 2);
					 
					 
					 rt.body.mergeCell(17,1,17,5);
					 rt.body.mergeCell(17,6,17,9);
					 rt.body.mergeCell(17,10,17,12);
					 rt.body.mergeCell(17,13,17,16);
					 rt.body.mergeCell(17,17,17,19);
					 rt.body.setRowHeight(17,5);
					 rt.body.setRowCells(17, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(17, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(18,1,18,4);
					 rt.body.mergeCell(18, 6, 18, 19);
					// rt.body.setRowHeight(18,0);
					 rt.body.setRowCells(18, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(18, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(19,1,19,5);
					 rt.body.mergeCell(19,6,19,9);
					 rt.body.mergeCell(19,10,19,12);
					 rt.body.mergeCell(19,13,19,16);
					 rt.body.mergeCell(19,17,19,19);
					 rt.body.setRowHeight(19,5);
					 rt.body.setRowCells(19, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(19, Table.PER_BORDER_RIGHT, 0);
					
				 }else{
//					 �������
					 
					 String tianbdw=getTianzdw(lgdiancxxb_id);//���Ƶ�λ�����ɸ������������뵥λ��
					 if (strzhejbz_sl.equals("")){
						 strzhejbz_sl="0";
					 }
					 double meij=Double.parseDouble(strzhejbz_sl);//ú��
					 double mkoukxj=0;//�ۿ�С��
					 boolean bTl_yunsfs=false;
					 boolean bGl_yunsfs=false; 
					 
					 if(!strzhejbz_Qnetar.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qnetar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
//						 meij=meij+Double.parseDouble(strzhejbz_Qnetar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qnetar));
					 }
					 
					 if(!strzhejbz_Std.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Std));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Std));
//						 meij+=Double.parseDouble(strzhejbz_Std);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Std));
					 }
					 
					 if(!strzhejbz_Ad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Ad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Ad));
//						 meij+=Double.parseDouble(strzhejbz_Ad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Ad));
					 }
					 
					 if(!strzhejbz_Vdaf.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vdaf));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vdaf));
//						 meij+=Double.parseDouble(strzhejbz_Vdaf);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vdaf));
					 }
					 
					 if(!strzhejbz_Mt.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mt));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mt));
//						 meij+=Double.parseDouble(strzhejbz_Mt);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mt));
					 }
					 
					 if(!strzhejbz_Qgrad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qgrad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qgrad));
//						 meij+=Double.parseDouble(strzhejbz_Qgrad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qgrad));
					 }
					 
					 if(!strzhejbz_Qbad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qbad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qbad));
//						 meij+=Double.parseDouble(strzhejbz_Qbad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qbad));
					 }
					 
					 if(!strzhejbz_Had.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Had));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Had));
//						 meij+=Double.parseDouble(strzhejbz_Had);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Had));
					 }
					 
					 if(!strzhejbz_Stad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Stad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Stad));
//						 meij+=Double.parseDouble(strzhejbz_Stad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Stad));
					 }
					 
					 if(!strzhejbz_Mad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mad));
//						 meij+=Double.parseDouble(strzhejbz_Mad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mad));
					 }
					 
					 if(!strzhejbz_Aar.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aar));
//						 meij+=Double.parseDouble(strzhejbz_Aar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aar));
					 }
					 
					 if(!strzhejbz_Aad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aad));
//						 meij+=Double.parseDouble(strzhejbz_Aad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aad));
					 }
					 
					 if(!strzhejbz_Vad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vad));
//						 meij+=Double.parseDouble(strzhejbz_Vad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vad));
					 }
					 
					 if(!strzhejbz_T2.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_T2));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_T2));
//						 meij+=Double.parseDouble(strzhejbz_T2);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_T2));
					 }
					 
					 if(!strzhejbz_Yunju.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Yunju));
//						 meij+=Double.parseDouble(strzhejbz_Yunju);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Yunju));
					 }
					 
					 if(!strzhejbz_Star.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Star));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Star));
//						 meij+=Double.parseDouble(strzhejbz_Star);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Star));
					 }
					 
					 //��ʽ�ۿ�С�ƣ���������С��λ
					 mkoukxj = CustomMaths.Round_new(mkoukxj, 2);
					 
					 if(stryunsfs.equals(Locale.tiel_yunsfs)){
//						 ��·
						 bTl_yunsfs=true;
					 }else if(stryunsfs.equals(Locale.gongl_yunsfs)){
						 
						 bGl_yunsfs=true;
					 }
					 
					 
					 String Relkk="";	//�����ۿ�
					 String Huifkk="";	//�ҷֿۿ�
					 String Huiffkk="";	//�ӷ��ֿۿ�
					 String Shuifkk="";	//ˮ�ֿۿ�
					 String liufkk="";	//��ֿۿ�
					 String Qitkk="";	//�����ۿ�
					 
					 Relkk=String.valueOf((-Double.parseDouble(strzhejbz_Qnetar.equals("")?"0":strzhejbz_Qnetar))
							 +(-Double.parseDouble(strzhejbz_Qgrad.equals("")?"0":strzhejbz_Qgrad))
							 +(-Double.parseDouble(strzhejbz_Qbad.equals("")?"0":strzhejbz_Qbad)));
					 
					 Huifkk=String.valueOf((-Double.parseDouble(strzhejbz_Ad.equals("")?"0":strzhejbz_Ad))
							 +(-Double.parseDouble(strzhejbz_Aad.equals("")?"0":strzhejbz_Aad)));
					 
					 Huiffkk=String.valueOf((-Double.parseDouble(strzhejbz_Vdaf.equals("")?"0":strzhejbz_Vdaf))
							 +(-Double.parseDouble(strzhejbz_Vad.equals("")?"0":strzhejbz_Vad)));
					 
					 Shuifkk=String.valueOf((-Double.parseDouble(strzhejbz_Mt.equals("")?"0":strzhejbz_Mt))
							 +(-Double.parseDouble(strzhejbz_Mad.equals("")?"0":strzhejbz_Mad)));
					 
					 liufkk=String.valueOf((-Double.parseDouble(strzhejbz_Std.equals("")?"0":strzhejbz_Std))
							 +(-Double.parseDouble(strzhejbz_Stad.equals("")?"0":strzhejbz_Stad))
							 +(-Double.parseDouble(strzhejbz_Star.equals("")?"0":strzhejbz_Star)) 
					 		);
					 
					 Qitkk=String.valueOf((-Double.parseDouble(strzhejbz_T2.equals("")?"0":strzhejbz_T2))
							 +(-Double.parseDouble(strzhejbz_Yunju.equals("")?"0":strzhejbz_Yunju)));
					 
					 
					 if(Relkk.equals("-0.0")){
						 
						 Relkk=""; 
					 }
					 
					 if(Huifkk.equals("-0.0")){
						 
						 Huifkk="";
					 }
					 
					 if(Huiffkk.equals("-0.0")){
						 
						 Huiffkk="";
					 }
					 
					 if(Shuifkk.equals("-0.0")){
						 
						 Shuifkk="";
					 }
					 
					 if(liufkk.equals("-0.0")){
											 
						 liufkk="";
					 }
					
					 if(Qitkk.equals("-0.0")){
						 
						 Qitkk="";
					 }
					 
					 
//					 ����liuf���ۿ��ۼ�
					 if((strjiesbz_Std.equals("")||strjiesbz_Std.equals("0"))
							 &&(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0"))){
						 
						 strjiesbz_Std="";
					 }else if(!strjiesbz_Star.equals("")
							 &&!strjiesbz_Star.equals("0")){
						 
						 if(!strjiesbz_Std.equals("")){
							 
							 strjiesbz_Std=String.valueOf(Double.parseDouble(strjiesbz_Star)
							 				+Double.parseDouble(strjiesbz_Std));
						 }else{
							 
							 strjiesbz_Std=strjiesbz_Star;
						 }
					 }
					 
//					 ����Ľ��㵥Ҫ��ʾ�������ۼ�ָ��֮�������ָ��
//					 2010-01-19 ww
//					 ���Aar��Starָ�꣬������㵥����ʾ�յ���ָ��
					 String JieszbArray[] = null;
					 JieszbArray = getJieszbxx(table1,where);
					 
//					 ֱȡdanpcjsmxb�е���ֵ
//					 if(strjiesbz_Qnetar.equals("")){
						 
						 strjiesbz_Qnetar = JieszbArray[0];
//					 }
					 
					 if(strjiesbz_Ad.equals("")){
						 
						 strjiesbz_Ad = JieszbArray[8];
					 }
					 
					 if(strjiesbz_Aar.equals("")) {
						 strjiesbz_Aar = JieszbArray[9];
					 }
					 
					 if(strjiesbz_Vdaf.equals("")){
						 
						 strjiesbz_Vdaf = JieszbArray[4];
					 }
					 
					 if(strjiesbz_Mt.equals("")){
						 
						 strjiesbz_Mt = JieszbArray[5];
					 }
					 
					 if(strjiesbz_Std.equals("")){
						 
						 strjiesbz_Std = JieszbArray[1];
					 }
					 
					 if(strjiesbz_Star.equals("")) {
						 strjiesbz_Star = JieszbArray[3];
					 }
					 if (strjiesbz_Qnetar.equals("")){
						 strjiesbz_Qnetar="0";
					 }
					 if (strjiesbz_Aar.equals("")){
						 strjiesbz_Aar="0";
					 }
					 if (strjiesbz_Vdaf.equals("")){
						 strjiesbz_Vdaf="0";
					 }
					 if (strjiesbz_Mt.equals("")){
						 strjiesbz_Mt="0";
					 }
					 if (strjiesbz_Star.equals("")){
						 strjiesbz_Star="0";
					 }
					 
					 
//					 ������Mj/kg
					 strjiesbz_Qnetar=String.valueOf(MainGlobal.kcalkg_to_Mjkg(Double.parseDouble(strjiesbz_Qnetar), 
							 ((Visit)this.getVisit()).getFarldec()));		//��������
					 
					 //����xitxxb�е����õõ����㵥�н��㲿�ŵ�����
					 String BuM = "ȼ�ϲ�";
					 BuM = MainGlobal.getXitxx_item("����", "���㲿������", ""+lgdiancxxb_id, BuM);
					 
					 String ArrHeader[][]=new String[6][19];
					 ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[1]=new String[] {"�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾"};
					 ArrHeader[2]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
					 ArrHeader[3]=new String[] {"ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��"};
					 ArrHeader[4]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[5]=new String[] {"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"���ڣ�"+stryansrq,"���ڣ�"+stryansrq,"���ڣ�"+stryansrq,"���ڣ�"+stryansrq,"���ڣ�"+stryansrq,"��ͬ�ţ�"+strHetbh,"��ͬ�ţ�"+strHetbh,"��ͬ�ţ�"+strHetbh,"��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��ţ�"+strbianh,"��ţ�"+strbianh,"��ţ�"+strbianh};

//					 ������ʤǩ������
					 String ZHIBR="";
					 String JIESR="";
					 String SHULSHR="";
					 String ZHILSHR="";
					 String JIHYXB="";
					 String CWSHRY="";
					 String LDQM="";
					 String ZKJSQM="";
					 
						String SQL="SELECT TO_CHAR(RZB.SHIJ, 'yyyy\"��\"mm\"��\"dd\"��\"') SHIJ,\n" +
						"       (SELECT MAX(MINGC) FROM RENYXXB WHERE QUANC = RZB.CAOZY) CAORY\n" + 
						"  FROM (SELECT MAX(RZB.ID) ID\n" + 
						"          FROM JIESB, LIUCGZB RZB\n" + 
						"         WHERE JIESB.LIUCGZID = RZB.LIUCGZID\n" + 
						"           AND JIESB.BIANM = '"+where+"'\n" +
						"			AND rzb.id>=GETLIUCGZBID('"+where+"')\n" + 
						"         GROUP BY QIANQZTMC) RZ,\n" + 
						"       LIUCGZB RZB\n" + 
						" WHERE RZ.ID = RZB.ID\n" + 
						" ORDER BY RZB.ID";
							
						ResultSetList Qm_rsl = cn.getResultSetList(SQL);
						
						String qianm[][]=new String[Qm_rsl.getRows()][2];
						for(int i=0;i<Qm_rsl.getRows();i++){
							Qm_rsl.next();
							qianm[i][0]=Qm_rsl.getString(0);
							qianm[i][1]=Qm_rsl.getString(1);
						}
						
						if(qianm.length>=1){
							ZHIBR="<image src='imgs/dsqm/"+qianm[0][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
							JIESR="<image src='imgs/dsqm/"+qianm[0][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=2){
							SHULSHR="<image src='imgs/dsqm/"+qianm[1][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=3){
							ZHILSHR="<image src='imgs/dsqm/"+qianm[2][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=4){
							JIHYXB="<image src='imgs/dsqm/"+qianm[3][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=5){
							CWSHRY="<image src='imgs/dsqm/"+qianm[4][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=6){
							LDQM="<image src='imgs/dsqm/"+qianm[5][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=7){
							ZKJSQM="<image src='imgs/dsqm/"+qianm[6][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
					 
					 String ArrBody[][]=new String[19][19];
					 ArrBody[0]=new String[] {"���㲿�ţ�" + BuM,"","","","������λ��"+strfahdw,"","","","����������","","","","�ƻ�������"+strJihkj,"","","","Ʒ�֣�"+strhuowmc,"",""};
					 ArrBody[1]=new String[] {"����","","","","","����","","","","","","","","","","","ú��","","˰��"};
					 ArrBody[2]=new String[] {"Ʊ��","","","","","","�ۿ�","","","","","","","���ۺϼ�","����˰��","","","",""};
					 ArrBody[3]=new String[] {"����","����","ӯ��","����","ʵ��","ú��","��ֵ","�ҷ�","�ӷ���","ˮ��","����","����","С��","","","","","",""};
					 ArrBody[4]=new String[] {""+strches+"",""+strfahsl+"",""+("0.0".equals(strxiancsl_sl)?"":-Double.parseDouble(strxiancsl_sl)+"")+"",""+(Double.parseDouble(strxiancsl_sl)<0?"":""+strxiancsl_sl)+""+"",""+strchangfys_sl+"",""+meij+"",""+Relkk+"",""+Huifkk+"",""+Huiffkk+"",""+Shuifkk+"",""+liufkk+"",""+Qitkk+"",""+mkoukxj+"",""+strzhejbz_sl+"",""+strbuhsdj+"","",""+formatq(strjiakhj)+"","",""+formatq(strshuik_mk)+""};
					 ArrBody[5]=new String[] {"��ֵ","�ҷ�","�ӷ���","ˮ��","����","Ӧ���ۿ�","","Ӧ��˰��","","�����ۿ�","","ʵ�����","","","","","","",""};
					 ArrBody[6]=new String[] {""+ (((Visit)this.getVisit()).getFarldec()==3 ? new DecimalFormat("0.000").format(Double.parseDouble(strjiesbz_Qnetar)) : strjiesbz_Qnetar)+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Aar))+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Vdaf))+"",""+new DecimalFormat("0.0").format(Double.parseDouble(strjiesbz_Mt))+"",""+new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Star))+"",""+strjiakhj+"","",""+formatq(strshuik_mk)+"","","","",""+formatq(strjialhj)+"","","","","","","",""};
					 ArrBody[7]=new String[] {"�˾�(km)","","�˷ѵ�����ϸ","","","","","","","","","","","","","","","","ӡ��˰"};
					 ArrBody[8]=new String[] {"����","����","����","����","����","ר��","����","����","�����˷�","","","","","","���ӷ�(Ԫ/��)","","","",""};
					 ArrBody[9]=new String[] {"","","","","","","","","�總��","��ɳ","��װ","����","����","С��","ȡ�ͳ�","�����","����","С��",""};
					 ArrBody[10]=new String[] {"","",""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"","",""+CustomMaths.Round_New(Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)+"","","",""+(bGl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"","","","","","",
							 					""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(stryunzshj.equals("")?"0":stryunzshj)/Double.parseDouble(strches)==0?1:Double.parseDouble(strches),2)):"")+"","","","","",""};
					 ArrBody[11]=new String[] {"�����˷�","","�����˷�","","�����˷�","","ר���˷�","","��;�˷�","","�����˷�","","�����˷�","","���ӷ�","","�ۿ�","","ʵ���˷ѽ��"};
					 ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","�����","����",""};
					 ArrBody[13]=new String[] {""+(bTl_yunsfs?strtielyf:"")+"","","","",""+strkuangqyf+"","","","","","",""+(bGl_yunsfs?strtielyf:"")+"","","","","","",""+strbukouqzf+"","",""+stryunzshj+""};
					 ArrBody[14]=new String[] {"ע�����ݱ���ҵ�ʽ�����ƶ�Ȩ�����л����������򣬻����ż�������д�������ǩ�������ڡ�","","","","","","","","","","","","","","","","","",""};
					 ArrBody[15]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrBody[16]=new String[] {"��˾(��)�ֹ��쵼��","",LDQM,"",BuM+"�����ˣ�","",JIHYXB,"","�ʼ���ˣ�","",ZHILSHR,"","������ˣ�","",SHULSHR,"","�Ʊ�",ZHIBR,""};
					 ArrBody[17]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrBody[18]=new String[] {"�ܻ��ʦ��","",ZKJSQM,"","��ƻ��������ˣ�","",CWSHRY,"","������ˣ�","","","","","���","","","�����ˣ�",JIESR,""};

					 int ArrWidth[]=new int[] {55,65,50,50,65,58,50,50,54,52,52,52,50,50,50,50,55,50,67};

//					 ����ҳTitle
					 rt.setTitle(new Table(ArrHeader,0,0,0));
					 rt.setBody(new Table(ArrBody,0,0,0));
					 rt.body.setWidth(ArrWidth);
					 rt.title.setWidth(ArrWidth);
//					 �ϲ���Ԫ��
//					 ��ͷ_Begin
//					 rt.title.merge(1, 1, 1, 19);
					 rt.title.merge(2, 1, 2, 19);
					 rt.title.merge(3, 1, 3, 19);
					 rt.title.merge(4, 1, 4, 19);
					 rt.title.merge(5, 1, 5, 19);
					 
					 rt.title.merge(6, 1, 6, 4);
					 rt.title.merge(6, 5, 6, 9);
					 rt.title.merge(6, 10, 6, 12);
					 rt.title.merge(6, 13, 6, 16);
					 rt.title.merge(6, 17, 6, 19);
					 
					 rt.title.setBorder(0,0,0,0);
					 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
					 
					 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
					 
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
//					 ����
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTNAME, "����");
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTSIZE, 11);
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTNAME, "Arial Unicode MS");
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTSIZE, 12);
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTNAME, "����");
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTSIZE, 20);
//					 ����				 
					 
//					 ͼƬ
					 rt.title.setCellImage(1, 1, 110, 50, "imgs/report/GDBZ.gif");	//����ı�־�����ֳ�Ҫһ�����Ͼ����ˣ�
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 rt.title.setCellImage(5, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
//					 ͼƬ_End
					 
//					 ��ͷ_End
//					 ����_Begin
					 
					 rt.body.mergeCell(1,1,1,4);
					 rt.body.mergeCell(1,5,1,8);
					 rt.body.mergeCell(1,9,1,12);
					 rt.body.mergeCell(1,13,1,16);
					 rt.body.mergeCell(1,17,1,19);
					 rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
					 rt.body.mergeCell(2,1,2,5);
					 rt.body.mergeCell(2,6,2,16);
					 rt.body.mergeCell(2,17,4,18);
					 rt.body.mergeCell(2,19,4,19);
					 rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(3,1,3,2);
					 rt.body.mergeCell(3,7,3,13);
					 rt.body.mergeCell(3,14,4,14);
					 rt.body.mergeCell(3,15,4,16);
					 rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(5,15,5,16);
					 rt.body.mergeCell(5,17,5,18);
					 rt.body.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(6,6,6,7);
					 rt.body.mergeCell(6,8,6,9);
					 rt.body.mergeCell(6,10,6,11);
					 rt.body.mergeCell(6,12,6,14);
					 rt.body.mergeCell(6,15,6,16);
					 rt.body.mergeCell(6,17,6,18);
					 rt.body.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(7,6,7,7);
					 rt.body.mergeCell(7,8,7,9);
					 rt.body.mergeCell(7,10,7,11);
					 rt.body.mergeCell(7,12,7,14);
					 rt.body.mergeCell(7,15,7,16);
					 rt.body.mergeCell(7,17,7,18);
					 rt.body.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(8,1,8,2);
					 rt.body.mergeCell(8,3,8,18);
					 rt.body.mergeCell(8,19,10,19);
					 rt.body.setRowCells(8, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(9,1,10,1);
					 rt.body.mergeCell(9,2,10,2);
					 rt.body.mergeCell(9,3,10,3);
					 rt.body.mergeCell(9,4,10,4);
					 rt.body.mergeCell(9,5,10,5);
					 rt.body.mergeCell(9,6,10,6);
					 rt.body.mergeCell(9,7,10,7);
					 rt.body.mergeCell(9,8,10,8);
					 rt.body.mergeCell(9,9,9,14);
					 rt.body.mergeCell(9,15,9,18);
					 rt.body.setRowCells(9, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(11, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(12,1,13,2);
					 rt.body.mergeCell(12,3,13,4);
					 rt.body.mergeCell(12,5,13,6);
					 rt.body.mergeCell(12,7,13,8);
					 rt.body.mergeCell(12,9,13,10);
					 rt.body.mergeCell(12,11,13,12);
					 rt.body.mergeCell(12,13,13,14);
					 rt.body.mergeCell(12,15,13,16);
					 rt.body.mergeCell(12,17,12,18);
					 rt.body.mergeCell(12,19,13,19);
					 rt.body.setRowCells(12, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(13, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(14,1,14,2);
					 rt.body.mergeCell(14,3,14,4);
					 rt.body.mergeCell(14,5,14,6);
					 rt.body.mergeCell(14,7,14,8);
					 rt.body.mergeCell(14,9,14,10);
					 rt.body.mergeCell(14,11,14,12);
					 rt.body.mergeCell(14,13,14,14);
					 rt.body.mergeCell(14,15,14,16);
					 rt.body.setRowCells(14, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(15,1,15,19);
					 rt.body.setRowCells(15, Table.PER_BORDER_BOTTOM, 2);
					 
//					 ��ע����(����һ��)
					 rt.body.mergeCell(16,1,16,19);
					 rt.body.setRowHeight(16,8);
					 rt.body.setRowCells(16, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(16, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.setBorder(0, 0, 2, 0);
					 rt.body.setCells(1, 1, 15, 1, Table.PER_BORDER_LEFT, 2);
					 rt.body.setCells(1, 19, 15, 19, Table.PER_BORDER_RIGHT, 2);
					 
					 
					 rt.body.mergeCell(17,1,17,2);
					 rt.body.mergeCell(17,3,17,4);
					 rt.body.mergeCell(17,5,17,6);
					 rt.body.setCellAlign(17, 5, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(17,7,17,8);
					 rt.body.mergeCell(17,9,17,10);
					 rt.body.setCellAlign(17, 9, Table.ALIGN_RIGHT);
					 
					 rt.body.mergeCell(17,11,17,12);
					 rt.body.mergeCell(17,13,17,14);
					 rt.body.mergeCell(17,15,17,16);
					 rt.body.setCellAlign(17, 13, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(17,18,17,19);
					 rt.body.setRowCells(17, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(17, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(18,1,18,19);
					 rt.body.setRowHeight(18,0);
					 rt.body.setRowCells(18, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(18, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(19,1,19,2);
					 rt.body.mergeCell(19,3,19,4);
					 rt.body.mergeCell(19,5,19,6);
					 rt.body.setCellAlign(19, 5, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(19,7,19,8);
					 rt.body.mergeCell(19,9,19,10);
					 rt.body.setCellAlign(19, 9, Table.ALIGN_RIGHT);
					 
					 rt.body.mergeCell(19,11,19,12);
					 rt.body.setCellAlign(19, 14, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(19,18,19,19);
					 rt.body.setRowCells(19, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(19, Table.PER_BORDER_RIGHT, 0);
					 
				 } 
			 }
			 
				// ����ҳ��
				_CurrentPage = 1;
				_AllPages=1;
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
	
	private String getGongfsl(long jiesbid,String tables) {
		// TODO �Զ����ɷ������
		JDBCcon con=new JDBCcon();
		String gongfsl="";
		try{
			
			String sql=" select gongf from jieszbsjb,"+tables+",zhibb "
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
	
	public String[] getJieszbxx(String Table,String Jiesbh){
		
//	�������ܣ�
		
//		�õ�ĳһ�Ž��㵥������ָ��
//	�����߼���
		
//		�ӵ����ν�����ϸ���в鵽�ý��㵥��Ӧ��ֵ
//	�����βΣ�
		
//		���㵥���
		
		String sql = "";
		String Jieszb[] = new String[11];
		long TalbeId = 0;
		
		try {
			TalbeId = MainGlobal.getTableId(Table, "bianm", Jiesbh);
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

		sql = 
			"select\n" +
			"   round_new(sum(decode(jiessl,0,0,jiessl*qnetar))/sum(decode(jiessl,0,1,jiessl))," + ((Visit)this.getVisit()).getFarldec() + ") as qnetar,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*std))/sum(decode(jiessl,0,1,jiessl)),2) as std,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*stad))/sum(decode(jiessl,0,1,jiessl)),2) as stad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*star))/sum(decode(jiessl,0,1,jiessl)),2) as star,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*vdaf))/sum(decode(jiessl,0,1,jiessl)),2) as vdaf,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*mt))/sum(decode(jiessl,0,1,jiessl)),1) as mt,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*mad))/sum(decode(jiessl,0,1,jiessl)),2) as mad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*aad))/sum(decode(jiessl,0,1,jiessl)),2) as aad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*ad))/sum(decode(jiessl,0,1,jiessl)),2) as ad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*aar))/sum(decode(jiessl,0,1,jiessl)),2) as aar,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*vad))/sum(decode(jiessl,0,1,jiessl)),2) as vad\n" + 
			" from\n" + 
			"   (select xuh,\n" + 
			"     max(mx.jiessl) as jiessl,\n" + 
			"     max(mx.qnetar)  as qnetar,\n" + 
			"     max(mx.std)  as std,\n" + 
			"     max(mx.stad)  as stad,\n" + 
			"     max(mx.star)  as star,\n" + 
			"     max(mx.vdaf)  as vdaf,\n" + 
			"     max(mx.mt)  as mt,\n" + 
			"     max(mx.mad) as mad,\n" + 
			"     max(mx.aad) as aad,\n" + 
			"     max(mx.ad) as ad,\n" + 
			"     max(mx.aar) as aar,\n" + 
			"     max(mx.vad) as vad\n" + 
			"   from danpcjsmxb mx\n" + 
			"        where leib=1 and jiesdid="+TalbeId+"\n" + 
			"        group by xuh)";
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			
			for(int i=0;i<rsl.getColumnCount();i++){
				
				Jieszb[i] = rsl.getString(i);
			}
		}
		rsl.close();
		con.Close();
		
		return Jieszb;
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