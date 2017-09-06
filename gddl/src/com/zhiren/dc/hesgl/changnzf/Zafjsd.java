package com.zhiren.dc.hesgl.changnzf;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author ������
 * 2009-06-24
 */

public class Zafjsd extends BasePage{
	
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
	
	public Zafjsd(){
		
	}

	public String getZafjsd(String where,int iPageIndex,String tables){
		
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		
		try{
			 String type="";	// ���㵥���ͣ�"ZGDT"Ϊ�й����ƣ�"GD"Ϊ����
			 long lgdiancxxb_id = 0;
			 String strhej_dx = ""; // ��д�ϼ�
			 String strhej_xx = ""; // Сд�ϼ�
			 String stryansrq = ""; // ��������
			 String stryinhzh = ""; // �����˺�
			 String strkaihyh = ""; // ��������
			 String strbianh = ""; // ���
			 String strjiesrq = ""; // ��������
			 String strgongysmc = ""; // ��Ӧ������
			 String strfahrq = "";
			 String shuil = "";
			 String jiessl = "";
			 String danj = "";
			 String buhsdj = "";
			 String buhszf = "";
			 String zafsk = "";
			 String hej = "";
			 String shoukdw_id = "";
			 String quanc = "";
			 String qiszyfw = "";
			 String jiezzyfw = "";
			 String beiz = "";
			 
			 String sql = "select * from zafjsb where bianm = '" + where + "'";
			 ResultSet rs = cn.getResultSet(sql); 
			 if(rs.next()){
				 
				 lgdiancxxb_id = rs.getLong("diancxxb_id");
				 strbianh = rs.getString("bianm");
				 strjiesrq = FormatDate(rs.getDate("riq"));
				 if ((rs.getString("gongysmc") != null)) {
					 strgongysmc = rs.getString("gongysmc");
				 }
				 jiessl = rs.getString("jiessl");
				 danj = rs.getString("danj");
				 buhszf = rs.getString("buhszf");
				 zafsk = rs.getString("zafsk");
				 hej = rs.getString("hej");
				 shoukdw_id = rs.getString("shoukdw_id");
				 qiszyfw = rs.getString("qiszyfw");
				 jiezzyfw = rs.getString("jiezzyfw");
				 beiz = rs.getString("beiz");
				 
				 if (qiszyfw.equals(jiezzyfw)) {
					 strfahrq = FormatDate(rs.getDate("qiszyfw"));
				 } else {
					 strfahrq = FormatDate(rs.getDate("qiszyfw"))+" �� "+FormatDate(rs.getDate("jiezzyfw"));
				 }

				 if(qiszyfw.equals(jiezzyfw)){
					 stryansrq = FormatDate(rs.getDate("qiszyfw"));
				 }else{
					 stryansrq = FormatDate(rs.getDate("qiszyfw"))+" �� "+FormatDate(rs.getDate("jiezzyfw"));
				 }
				 
				 String shoukdwSql = "select * from shoukdw where id = " + shoukdw_id;
				 ResultSet rsl = cn.getResultSet(shoukdwSql);
				 rsl.next();
				 quanc = rsl.getString("quanc");
				 
				 strkaihyh = rsl.getString("kaihyh");
				 stryinhzh = rsl.getString("zhangh");

//				 ����˰����
				 buhsdj = Math.round(Double.parseDouble(buhszf) / Double.parseDouble(jiessl) * 10000000)/10000000.0+"";
//				 ˰��
				 shuil = Math.round((1 - Double.parseDouble(buhszf) / Double.parseDouble(hej))*100)/100.0+"";

			 }

			 Money money = new Money();
			 
			 strhej_xx = format(Double.parseDouble(hej),"0.00");
			 strhej_dx = money.NumToRMBStr(Double.parseDouble(hej));
			 
			 rs.close();
			 cn.Close();
			 
			 type = MainGlobal.getXitxx_item("����", "���㵥������λ", String.valueOf(lgdiancxxb_id), "ZGDT");
			 if(type.equals("ZGDT")){
				 
				 int ArrWidth[]=new int[] {125,70,65,65,65,65,75,70,85,75,78,65};
				 
				 String ArrHeader[][]=new String[19][12];
				 ArrHeader[0] = new String[] {"������λ:"+strgongysmc,"","","��վ:","","������:","","","�տλ:"+quanc,"","",""};
				 ArrHeader[1] = new String[] {"��������:"+strfahrq,"","","��������:","","ԭ�ջ���:","","","��������:"+strkaihyh,"","",""};
				 ArrHeader[2] = new String[] {"��������:"+stryansrq,"","","��������:","","���ջ���:","","","�����ʺ�:"+stryinhzh,"","",""};
				 ArrHeader[3] = new String[] {"��������(��):"+jiessl,"","����:","���ձ��:","","��Ʊ���:","","","�Ҹ��ص�:","","���ʽ:",""};
				 ArrHeader[4] = new String[] {"��������","��������","��������","��������","��������","��������","��������","��������","��������","","",""};
				 ArrHeader[5] = new String[] {"��˰��:"+danj+"(Ԫ)","��ͬ��׼","������׼","��������","�����׼","�������","�ۼ۱�׼","�ۺϽ��","��������","��������","��������","�ۺϽ��"};
				 ArrHeader[6] = new String[] {""+Locale.Qnetar_zhibb+"("+Locale.qiankmqk_danw+")","","","","","","","","(��)","(��)","(��)","(Ԫ)"};
				 ArrHeader[7] = new String[] {""+Locale.Std_zhibb+"("+Locale.baifb_danw+")","","","","","","","","","","",""};
				 ArrHeader[8] = new String[] {"��������","����","���","","��(��)��ǰ�ۿ�","��(��)��ǰ�ۿ�","�ۿ�ϼ�","","˰��","˰��","��˰�ϼ�","��˰�ϼ�"};
				 ArrHeader[9] = new String[] {jiessl,buhsdj,formatq(buhszf),"","","",formatq(buhszf),"",shuil,zafsk,formatq(hej),""};
				 ArrHeader[10] = new String[] {"ú��ϼ�(��д):","","","","","","","","","","",""};
				 ArrHeader[11] = new String[] {"��·�˷�","��·�ӷ�","�����˷�","�����ӷ�","��(��)��ǰ���ӷ�","��(��)��ǰ���ӷ�","����˰�˷�","","˰��","˰��","���ӷѺϼ�","���ӷѺϼ�"};
				 ArrHeader[12] = new String[] {"","","","","","","","","","","","151546.4"};
				 ArrHeader[13] = new String[] {"���ӷѺϼ�(��д):","","","","","","","","","","",""};
				 ArrHeader[14] = new String[] {"�ϼ�(��д):",strhej_dx,"","","","","","","�ϼ�(Сд):",strhej_xx,"",""};
				 ArrHeader[15] = new String[] {"��ע:",beiz,"","","","","","","��������(��):","",""+Locale.jiesslcy_title+"",""};
				 ArrHeader[16] = new String[] {"�糧ȼ�ϲ���:(����)","","�糧������:(����)","","","�����ල��:(ǩ��)","","�쵼����:(ǩ��)","","�ۺϲ���:(ǩ��)","",""};
				 ArrHeader[17] = new String[] {"������:","","������:","","","������:","","","","������:","",""};
				 ArrHeader[18] = new String[] {"","","","","","","","","","","",""};
				 
//				 ����ҳTitle
				 rt.setTitle(Locale.jiesd_title,ArrWidth);
				 String tianbdw = getTianzdw(lgdiancxxb_id); // ���Ƶ�λ�����ɸ������������뵥λ��
				 rt.setDefaultTitleLeft("���Ƶ�λ��"+tianbdw,3);
				 rt.setDefaultTitle(4,5,"�������ڣ�"+strjiesrq,Table.ALIGN_CENTER);
				 rt.setDefaultTitle(9,4,"���:"+strbianh,Table.ALIGN_RIGHT);
				 rt.setBody(new Table(ArrHeader,0,0,0));
				 rt.body.setWidth(ArrWidth);	
				 
				 rt.body.mergeCell(1,1,1,3);
				 rt.body.mergeCell(1,7,1,8);
				 rt.body.mergeCell(1,9,1,12);
				 rt.body.setCells(1,4,1,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(1,6,1,6,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(2,1,2,3);
				 rt.body.mergeCell(2,7,2,8);
				 rt.body.mergeCell(2,9,2,12);
				 rt.body.setCells(2,4,2,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(2,6,2,6,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(3,1,3,3);
				 rt.body.mergeCell(3,7,3,8);
				 rt.body.mergeCell(3,9,3,12);
				 rt.body.setCells(3,4,3,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(3,6,3,6,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(4,7,4,8);
				 rt.body.setCells(4,4,4,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(5,1,5,8);
				 rt.body.mergeCell(5,9,5,12);
				 
				 rt.body.mergeCell(9,3,9,4);
				 rt.body.mergeCell(9,5,9,6);
				 rt.body.mergeCell(9,7,9,8);
				 rt.body.mergeCell(9,11,9,12);
				 
				 rt.body.mergeCell(10,3,10,4);
				 rt.body.mergeCell(10,5,10,6);
				 rt.body.mergeCell(10,7,10,8);
				 rt.body.mergeCell(10,11,10,12);
				 
				 rt.body.mergeCell(12,5,12,6);
				 rt.body.mergeCell(12,7,12,8);
				 rt.body.mergeCell(12,11,12,12);
			 
				 rt.body.mergeCell(13,5,13,6);
				 rt.body.mergeCell(13,7,13,8);
				 rt.body.mergeCell(13,11,13,12);
				 
				 rt.body.mergeCell(14,2,14,12); // �˷Ѻϼƴ�д
				 rt.body.mergeCell(15,2,15,8);
				 rt.body.mergeCell(15,10,15,12);
				 
				 rt.body.mergeCell(16,2,16,8);
				 rt.body.mergeCell(17,1,17,2);
				 rt.body.mergeCell(17,3,17,5);
				 rt.body.mergeCell(17,6,17,7);
				 rt.body.mergeCell(17,8,17,9);
				 rt.body.mergeCell(17,10,17,12);
				 
				 rt.body.mergeCell(18,1,18,2);
				 rt.body.mergeCell(18,3,18,5);
				 rt.body.mergeCell(18,6,18,7);
				 rt.body.mergeCell(18,8,18,9);
				 rt.body.mergeCell(18,10,18,12);
				 
				 rt.body.mergeCell(19,1,19,2);
				 rt.body.mergeCell(19,3,19,5);
				 rt.body.mergeCell(19,6,19,7);
				 rt.body.mergeCell(19,8,19,9);
				 rt.body.mergeCell(19,10,19,12);
				 
				 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.setCells(5, 1, 12, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.body.setCells(6,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(9,1,9,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(12,1,12,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(13,1,13,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(8,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(17,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(18,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(18,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(19,Table.PER_ALIGN,Table.ALIGN_RIGHT);
				 
				 rt.body.mergeCell(11,2,11,12);	// ú��ϼƴ�д
				 rt.body.setCells(11,12,11,12,Table.PER_BORDER_RIGHT,1);
				 rt.body.setCells(11,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(11,2,11,2,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(10,1,10,1,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 
			 }else if(type.equals("GD")){ // ����
				 
				 String tianbdw = getTianzdw(lgdiancxxb_id); // ���Ƶ�λ�����ɸ������������뵥λ��
				 
				 String ArrHeader[][]=new String[7][19];
				 ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrHeader[1]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrHeader[2]=new String[] {"�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾"};
				 ArrHeader[3]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
				 ArrHeader[4]=new String[] {"ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��","ȼ  ��  �� ú ̿ ��  ��  ��  ��"};
				 ArrHeader[5]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrHeader[6]=new String[] {"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"��λ��"+tianbdw,"���ڣ�"+strjiesrq,"���ڣ�"+strjiesrq,"���ڣ�"+strjiesrq,"��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��λ����,Ԫ/��,MJ/kg,%,Ԫ","��ţ�"+strbianh,"��ţ�"+strbianh,"��ţ�"+strbianh};

				 String ArrBody[][]=new String[19][19];
				 ArrBody[0]=new String[] {"���㲿�ţ�ȼ�ϲ�","","","","������λ��"+strgongysmc,"","","","����������","","","","�ƻ�������","","","","Ʒ�֣�","",""};
				 ArrBody[1]=new String[] {"����","","","","","����","","","","","","","","","","","ú��","","˰��"};
				 ArrBody[2]=new String[] {"Ʊ��","","","","","","�ۿ�","","","","","","","���ۺϼ�","����˰��","","","",""};
				 ArrBody[3]=new String[] {"����","����","ӯ��","����","ʵ��","ú��","��ֵ","�ҷ�","�ӷ���","ˮ��","����","����","С��","","","","","",""};
				 ArrBody[4]=new String[] {"",jiessl,"","","","","","","","","","","",danj,buhsdj,"",""+formatq(buhszf)+"","",""+formatq(zafsk)+""};
				 ArrBody[5]=new String[] {"��ֵ","�ҷ�","�ӷ���","ˮ��","����","Ӧ���ۿ�","","Ӧ��˰��","","�����ۿ�","","ʵ�����","","","","","","",""};
				 ArrBody[6]=new String[] {"","","","","","","",""+formatq(zafsk)+"","","","",""+formatq(hej)+"","","","","","","",""};
				 ArrBody[7]=new String[] {"�˾�(km)","","�˷ѵ�����ϸ","","","","","","","","","","","","","","","","ӡ��˰"};
				 ArrBody[8]=new String[] {"����","����","����","����","����","ר��","����","����","�����˷�","","","","","","���ӷ�(Ԫ/��)","","","",""};
				 ArrBody[9]=new String[] {"","","","","","","","","�總��","��ɳ","��װ","����","����","С��","ȡ�ͳ�","�����","����","С��",""};
				 ArrBody[10]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[11]=new String[] {"�����˷�","","�����˷�","","�����˷�","","ר���˷�","","��;�˷�","","�����˷�","","�����˷�","","���ӷ�","","�ۿ�","","ʵ���˷ѽ��"};
				 ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","�����","����",""};
				 ArrBody[13]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[14]=new String[] {"ע�����ݱ���ҵ�ʽ�����ƶ�Ȩ�����л����������򣬻����ż�������д�������ǩ�������ڡ�","","","","","","","","","","","","","","","","","",""};
				 ArrBody[15]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[16]=new String[] {"��˾(��)�ֹ��쵼��","","","","","ȼ�ϲ������ˣ�","","","","�ʼ���ˣ�","","","������ˣ�","","","","�Ʊ�","",""};
				 ArrBody[17]=new String[] {"","","","","","","","","","","","","","","","","","",""};
				 ArrBody[18]=new String[] {"�ܻ��ʦ��","","","","","��ƻ��������ˣ�","","","","������ˣ�","","","���","","","","�����ˣ�","",""};

				 int ArrWidth[]=new int[] {54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54};
				 
//				 ����ҳTitle
				 rt.setTitle(new Table(ArrHeader,0,0,0));
				 rt.setBody(new Table(ArrBody,0,0,0));
				 rt.body.setWidth(ArrWidth);
				 rt.title.setWidth(ArrWidth);
//				 �ϲ���Ԫ��
//				 ��ͷ_Begin
				 rt.title.merge(2, 1, 2, 19);
				 rt.title.merge(3, 1, 3, 19);
				 rt.title.merge(4, 1, 4, 19);
				 rt.title.merge(5, 1, 5, 19);
				 rt.title.merge(6, 1, 6, 19);
				 
				 rt.title.merge(7, 1, 7, 7);
				 rt.title.merge(7, 8, 7, 10);
				 rt.title.merge(7, 11, 7, 16);
				 rt.title.merge(7, 17, 7, 19);
				 
				 rt.title.setBorder(0,0,0,0);
				 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
				 rt.title.setRowCells(7,Table.PER_BORDER_BOTTOM,0);
				 
				 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
				 rt.title.setRowCells(7,Table.PER_BORDER_RIGHT,0);
				 
				 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.title.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 
//				 ����
				 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTNAME, "����");
				 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTSIZE, 11);
				 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTNAME, "Arial Unicode MS");
				 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTSIZE, 12);
				 rt.title.setCells(5, 1, 5, 19, Table.PER_FONTNAME, "����");
				 rt.title.setCells(5, 1, 5, 19, Table.PER_FONTSIZE, 20);
//				 ����				 
				 
//				 ͼƬ
				 rt.title.setCellImage(2, 1, 120, 60, "imgs/report/GDBZ.gif");	//����ı�־�����ֳ�Ҫһ�����Ͼ����ˣ�
				 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 rt.title.setCellImage(6, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
//				 ͼƬ_End
				 
//				 ��ͷ_End
//				 ����_Begin
				 rt.body.mergeCell(1,1,1,4);
				 rt.body.mergeCell(1,5,1,8);
				 rt.body.mergeCell(1,9,1,12);
				 rt.body.mergeCell(1,13,1,16);
				 rt.body.mergeCell(1,17,1,19);
				 
				 rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 rt.body.setCells(1,17,1,17,Table.PER_BORDER_RIGHT,2);
				 
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
				 rt.body.setCells(15,1,15,1,Table.PER_BORDER_RIGHT,2);
				 
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
				 
				 rt.body.mergeCell(18,1,18,19);
				 rt.body.setRowHeight(18,0);
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
				 
			 }
			 
//			����ҳ��
			_CurrentPage = 1;
			_AllPages = 1;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			return rt.getAllPagesHtml(iPageIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return rt.getAllPagesHtml(iPageIndex);
	}
	
	public String getTianzdw (long diancxxb_id) {
		String Tianzdw = "";
		JDBCcon con = new JDBCcon();
		try{
			String sql = "select quanc from diancxxb where id = " + diancxxb_id;
			ResultSet rs = con.getResultSet(sql);
			if(rs.next()){
				Tianzdw = rs.getString("quanc");
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
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
	}
	
	public String formatq(String strValue){ //��ǧλ�ָ���
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