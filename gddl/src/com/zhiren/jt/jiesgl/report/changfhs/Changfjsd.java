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
 * ���ߣ���ɳɳ
 * ʱ�䣺2012-4-20
 * ��������Ծ�Ȫ�糧�Ľ����������������Ʊ�������͵Ľ��㵥��ʾ���ݵ�ȡ������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-9-01
 * ��������������糧�Ľ���������������㵥������ʾ����
 * 		ʹ�ò��������Ƿ���ʾԤ���㵥��Ϣ��
 * insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * values(getnewid(1),1,0,'�Ƿ��ȡԤ���㵥��Ϣ','��','','����',1,'ʹ��');
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-9-11
 * �������޸Ľ����д��ڵ�BUG
 * 		ʹ�ò������ƽ��㵥��ʵ������Ϣ�Ƿ�������䷽ʽ��ͬ�������֡�
		INSERT INTO xitxxb (id,xuh,diancxxb_id,mingc,zhi,leib,zhuangt,beiz)
		VALUES(getnewid(1),1,476,'ʵ�����Ƿ�������䷽ʽ��ͬ��������','��','�����ѯ',1,'ʹ��')
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-9-12
 * �������޸��˷ѽ���ʱʵ����Ӧȡ��������
 * 		ʹ�ò������ƽ��㵥��ʵ������Ϣ�Ƿ�������䷽ʽ��ͬ�������֡�
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-03-02
 * �������������㵥��ʽ��Ա���һ�����㵥�������趨
 * 		����MainGlobal.getXitxx_item("����", "����������㵥��ʽ", lgdiancxxb_id, "��").equals("��");�������ڿ��ƽ��㵥��ʽ��
 * 		����MainGlobal.getXitxx_item("����", "���㵥��ʾ��������", lgdiancxxb_id, "����").equals("����")�������ڿ��ƽ��㵥��ʾ�������͡�
 * 		����MainGlobal.getXitxx_item("����", "���㵥�Ƿ���ʾʵ������", lgdiancxxb_id, "��").equals("��")�������ڿ����Ƿ���ʾʵ��������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-03-26
 * ������������Ȫ���㵥ú����ʾ���
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-05-16
 * �����������������㵥��ʽ
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-09-22
 * ������Ϊ���ȵ�����ͨ��xitxxb���ý��㵥��ʾ�ҷֻ�׼
 * 		MainGlobal.getXitxx_item("����", "���㵥��ʾ�ҷֻ�׼", lgdiancxxb_id, "AAR");
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-09-26
 * �������޸����ھ�Ȫ�糧����ʷԭ���µ���ʾBUG
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-10-14
 * ���������Ӵ󿪵糧���㵥ҳ����������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-11-07
 * �����������˷ѽ��㵥����ʾ��ݵ�����
 * 		��strjiesbz_Stad���Ϊstrjiesbz_Star
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-11-11
 * �������������������˷ѽ��㵥����ʾ��ݵ�����ʱ���ֵ�BUG��
 * 		����Ҫ����㵥�������Ӧ��ʾΪStad��������Star������轫�˷Ѳ�ѯ����SQL������Ӧ������
 * 		�������㵥�������ʾֵ��strjiesbz_Star���Ϊstrjiesbz_Stad
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-11-11
 * �������������������˷ѽ��㵥�����˵��۵���Ϊ���ۺϼơ�
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-12-06
 * ������������Ȫ������ʾBUG
 */
/*
 * ���ߣ���ҫ��
 * ʱ�䣺2014-1-08
 * �������޸ľ�Ȫ�Ľ�������
 */
/*
 * ���ߣ���ҫ��
 * ʱ�䣺2014-1-09
 * ������ԭ����Ȫstrjiesbz_StadΪ�գ������Ѿ��޸�
 */
/*
 * ���ߣ���ҫ��
 * ʱ�䣺2014-1-14
 * �������Դ󿪵Ľ��㵥��������ۿ��ֵ  2682��
 */
/*
 * ���ߣ���ҫ��
 * ʱ�䣺2014-1-22
 * �������Ѿ�Ȫ���㵥�����ֵ�Դ���ʾ2323��
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-02-10
 * ������������Ȫ������ʾBUG
 */
/*
 * ���ߣ����
 * ʱ�䣺2015-04-02
 * �������������㵥ǩ������
 */
public class Changfjsd extends BasePage{

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

    public Changfjsd(){

    }
    /**
     * @param where
     * @param iPageIndex
     * @param tables
     * @return
     */
    public String getChangfjsd(String where,int iPageIndex,String tables){		JDBCcon cn = new JDBCcon();
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
            String strmeikdw = "";
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
            String strbuhsdj = "";//�˷Ѳ���˰����
            String strbuhsdj_m = "";//ú����˰����
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
            String jiesdbz="";
            //
            double dblMeik =0;
            double dblYunf =0;
            String strkuidjfyf="";
            String strkuidjfzf="";
            String strbukmk = "";

            int is_yujs=0;//�Ƿ�Ԥ����
            long chongdjsb_id=0;//��ֽ����id
            String changfysl="";//����������
            int jieslx_dat=0;//��ͬ�糧��������

            //yuss 2012-4-6  �����ڹ����Ȫ��Ʊ����ʱ�Ľ��㵥
            String strzhuanxyf="";//ר���˷�
            String strqicyf="";//�����˷�
            String strzhuangxf="";//װж��
            String strhulf="";//��·��
            String strjiessl="";//������
            String strjiesslcy="";//������������
            String stryunfhsdj="";//�˷Ѻ�˰����
            String strhansyf="";//��˰�˷�
            //yuss 2012-4-6
            String gongysb_id = null;
            boolean flag = false;  //strchangfys_sl��������
            String shisltj = "����"; //ʵ����Ĭ�ϰ�����ͳ��
            sql = "select * from xitxxb where mingc='ʵ����ͳ�ƹ���' and zhuangt=1 and leib = '����'";
            ResultSetList rss = cn.getResultSetList(sql);
            if(rss.next()){
                flag = true;
                shisltj = rss.getString("zhi");
            }


            rss.close();
            sql="select * from "+table1+" where bianm='"+where+"'";
            ResultSet rs = cn.getResultSet(sql);

            int intLeix=3;
            long intDiancjsmkId=0;
            long strkuangfjsmkb_id = -1;
            boolean blnHasMeik =false;		//�Ƿ���ú��



            if(rs.next()){

                strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetb", "hetbh", "id", rs.getString("hetb_id")));
//				 danjc = rs.getDouble("danjc");
                stryunfhsdj=rs.getString("yunfhsdj");
                gongysb_id=rs.getString("gongysb_id");
                strhansyf=rs.getString("hansyf");
                strbukmk = rs.getString("bukmk");
                lgdiancxxb_id=rs.getLong("diancxxb_id");
                strbianh=rs.getString("bianm");
                strjiesrq=FormatDate(rs.getDate("jiesrq"));
                intLeix=rs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
                intDiancjsmkId =rs.getLong("id");//ú��id
                strfahdw=rs.getString("gongysmc");//������λ
                strmeikdw = rs.getString("meikdwmc");//ú��λ
                strfahksrq=rs.getString("fahksrq");//������ʼ����
                strfahjzrq=rs.getString("fahjzrq");//������ֹ����
                strjiessl=rs.getString("jiessl");
                strjiesslcy=rs.getString("jiesslcy");
                jiesdbz=rs.getString("beiz");
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
                strbuhsdj_m=rs.getString("buhsdj");	//����˰����

                if(lgdiancxxb_id==476){
                    strbuhsdj=rs.getString("buhsdj");	//����˰����
                }

                if(MainGlobal.getXitxx_item("����", "�Ƿ��ȡԤ���㵥��Ϣ", "0", "��").equals("��")){
                    is_yujs=rs.getInt("is_yujsd");//�Ƿ�Ԥ���㵥
                    chongdjsb_id=rs.getLong("chongdjsb_id");//��ֽ����id
                    jieslx_dat=rs.getInt("jieslx_dt");//��ͬ�糧��������
                }

                if(rs.getString("yunsfsb_id")!=null){//�ж����䷽ʽ�Ƿ�Ϊ��
                    stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
                }
                strChaokdl=String.valueOf(Math.abs(rs.getDouble("chaokdl")));	//��/������
                strChaokdlx=Jiesdcz.nvlStr(rs.getString("chaokdlx"));	//����������

                if(flag){
                    if(shisltj.equals("����")){
                        strchangfys_sl = rs.getString("guohl");
                    }else if(shisltj.equals("Ʊ��")){
                        strchangfys_sl = rs.getString("guohl-yingd+kuid+yuns");
                    }else if(shisltj.equals("������")){
                        strchangfys_sl = rs.getString("jiessl");
                    }

                    if(lgdiancxxb_id!=323){//��Ȫ�糧������2λС��
                        if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){//��ͬ���������������С��λ����
                            //��ͬ�糧,��������С��λ��
                        }else{
                            if(MainGlobal.getXitxx_item("�����ѯ", "ʵ�����Ƿ�������䷽ʽ��ͬ��������", lgdiancxxb_id, "��").equals("��") ){
                                if(rs.getString("yunsfsb_id").equals("1")){
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,0)+"";
                                }else{
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,1)+"";
                                }
                            }
                        }
                    }
                }
                sql="select jieszbsjb.*,zhibb.bianm as mingc from jieszbsjb,"+table1+",zhibb "
                        + " where jieszbsjb.jiesdid="+table1+".id and zhibb.id=jieszbsjb.zhibb_id"
                        + " and "+table1+".bianm='"+where+"' and jieszbsjb.zhuangt=1 order by jieszbsjb.id";

                ResultSet rs2=cn.getResultSet(sql);
                while(rs2.next()){

                    if(rs2.getString("mingc").equals(Locale.jiessl_zhibb)){

                        strhetbz_sl = rs2.getString("hetbz");		//��ͬ��׼
                        strgongfbz_sl = rs2.getString("gongf");	//��������
                        strfahsl=strgongfbz_sl;
                        if(!flag){
                            strchangfys_sl = rs2.getString("CHANGF");	//��������
                        }
                        strjiesbz_sl = rs2.getString("JIES");		//��������
                        if(lgdiancxxb_id==323){
                            if(intLeix==1&&stryunfhsdj.equals("0")){
                                strxiancsl_sl =strjiesslcy;//��Ȫ��Ʊ����ʱ��jiesb.jiesslcy�϶��ǿ���
                            }else{
                                strxiancsl_sl = String.valueOf((rs2.getDouble("YINGK")>0?0:-rs2.getDouble("YINGK")));//��������
                            }
                        }else  if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){
                            //��ͬ����,��Ϊ��ͬ��һ������,����diancxxb_id�Ƚ϶�,
                            strxiancsl_sl =  String.valueOf(rs2.getDouble("YINGK"));//��ͬ���ж�ӯ��С��0ʱ,��ʾΪ0.
                        }else{
                            strxiancsl_sl = String.valueOf((rs2.getDouble("YINGK")>0?(-rs2.getDouble("YINGK")):0));//��������
                        }

                        strzhejbz_sl = rs2.getString("ZHEJBZ");	//�ۼ۱�׼
                        strzhehje_sl = rs2.getString("ZHEJJE");	//�ۺϽ��
                        changfysl=rs2.getString("changf");//����������,��ͬ��

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


            double chongdjssl=0;//��ֽ�������(��ͬ��)
            if(chongdjsb_id!=0){//���chongdjsb_id������0,֤���ǳ�ֽ��㵥
                String sql_chongdjsb="select jiessl from jiesb where id="+chongdjsb_id;
                ResultSetList rss2=cn.getResultSetList(sql_chongdjsb);
                if(rss2.next()){
                    chongdjssl=rss2.getDouble("jiessl");
                }
                rss2.close();
            }






//			1, ��Ʊ����;
//			2, ú�����
//			3, �����˷�
//			4, �����˷�

            if ((blnHasMeik)&&(intLeix==1)){

                if(lgdiancxxb_id==323&&stryunfhsdj.equals("0")){
                    sql="select * from "+table2+" where bianm='"+where+"'";

                    ResultSet rs3=cn.getResultSet(sql);
                    if (rs3.next()){
                        stryunzshj = rs3.getString("hansyf");//���ӷѺϼ�
                        String jiesyfb_id=rs3.getString("id");//jiesyfb_id
                        String sql_feiy=
                                "select feiymcb.mingc mingc, f.zhi zhi\n" +
                                        "  from (select feiyxmb_id, zhi\n" +
                                        "          from feiyb\n" +
                                        "         where feiyb_id =\n" +
                                        "               (select feiyb_id\n" +
                                        "                  from yunfdjb\n" +
                                        "                 where id = (select distinct yunfdjb_id\n" +
                                        "                               from danjcpb\n" +
                                        "                              where yunfjsb_id = "+jiesyfb_id+"))) f,\n" +
                                        "       feiyxmb,\n" +
                                        "       feiymcb\n" +
                                        " where f.feiyxmb_id = feiyxmb.id\n" +
                                        "   and feiymcb.id = feiyxmb.feiymcb_id";
                        ResultSet rs_feiy=cn.getResultSet(sql_feiy);
                        while(rs_feiy.next()){
                            if(rs_feiy.getString("mingc").equals("�����˷�")){
                                strtielyf=rs_feiy.getString("zhi");
                            }else if(rs_feiy.getString("mingc").equals("ר���˷�")){
                                strzhuanxyf=rs_feiy.getString("zhi");
                            }else if(rs_feiy.getString("mingc").equals("�����˷�")){
                                strqicyf=rs_feiy.getString("zhi");
                            }else if(rs_feiy.getString("mingc").equals("װж��")){
                                strzhuangxf=rs_feiy.getString("zhi");
                            }else{//��·��
                                strhulf=rs_feiy.getString("zhi");
                            }

                        }
                        rs_feiy.close();
                    }

                    rs3.close();
                }else{
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
                        strshijfk = rs3.getString("hansdj");
                        dblYunf=rs3.getDouble("hansyf");
                        strkuidjfyf = rs3.getString("kuidjfyf");
                        strkuidjfzf = rs3.getString("kuidjfzf");
//					 strjiesyfbz_sl = rs3.getString("jiessl"); //�˷ѽ�������
                        if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){
                            //��ͬ����,��Ϊ��ͬ��һ������,����diancxxb_id�Ƚ϶�,
                            //��ͬ�˷ѽ�������ȥjiessl
                            strjiesyfbz_sl = rs3.getString("jiessl");
                        }else{
                            strjiesyfbz_sl = rs3.getString("gongfsl");
                        }

                    }

                    rs3.close();
                }
            }else if(intLeix!=2){

                if( stryuanshr.equals("���������Ȫ�������޹�˾")){//���Ǿ�Ȫ�糧ʱ
                    sql="select * from "+table2+"  where bianm='"+where+"'";
                    rs=cn.getResultSet(sql);
                    if(rs.next()){
//							 strshijfk =rs.getString("hansdj");		
                        lgdiancxxb_id=rs.getLong("diancxxb_id");
                        strbianh=rs.getString("bianm");
                        strjiesrq=FormatDate(rs.getDate("jiesrq"));
                        intLeix=rs.getInt("jieslx");//��������0 Ϊ��Ʊһ����Ӧ��,1Ϊ��Ʊ������Ӧ�̣�2Ϊ�˷ѻ�ú��
                        //					 intDiancjsmkId =rs.getInt("id");//ú��id
                        strfahdw=rs.getString("gongysmc");
                        strmeikdw = rs.getString("meikdwmc");
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
                        strchangfys_sl=rs.getString("guohl");
                        if(!flag){
                            strchangfys_sl=rs.getString("yanssl");
                        }
                        if(flag){
                            if(rs.getString("yunsfsb_id").equals("1")){
                                strchangfys_sl = CustomMaths.round(strchangfys_sl,0)+"";
                            }else{
                                strchangfys_sl = CustomMaths.round(strchangfys_sl,1)+"";
                            }
                        }
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

//							 2009-3-12 zsj���˷���ʾ
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
//							 strjiesyfbz_sl = rs.getString("jiessl"); //�˷ѽ�������
                        strjiesyfbz_sl = rs.getString("gongfsl");

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
//							 strgongfbz_sl = "";		//��������
//							 strchangfys_sl ="";		//��������
//							 strjiesbz_sl = "";			//�����׼
//							 strxiancsl_sl = "";		//�������
                        strzhejbz_sl ="";			//��������
                        strzhehje_sl = "";			//�ۺϽ��



//							 strjiessl = rs.getString("jiessl");//��������
//							 strjiesbz_sl = rs.getString("gongfsl");
//							 strdanj = (double)Math.round(a);//����
							 /*strjine = "0";//���
							 strbukouqjk = "0";//��(��)��ǰ�ۿ�
							 strjiakhj = "0";//�ۿ�ϼ�
							 strshuil_mk = "0";//˰��(ú��)
							 strshuik_mk = "0";//˰��(ú��)
							 strjialhj = "0";//��˰�ϼ�
*/							 strguohzl =rs.getString("GUOHL");//��������
                        strbeiz = nvlStr(rs.getString("beiz"));//��ע
                        dblMeik= Double.parseDouble(strjialhj);
                        blnHasMeik=true;

                        strranlbmjbr=rs.getString("ranlbmjbr");
                        strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));

                        strkuidjfyf = rs.getString("kuidjfyf");
                        strkuidjfzf = rs.getString("kuidjfzf");
                    }

                }else{

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
                        strmeikdw = rs.getString("meikdwmc");
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
                        strchangfys_sl=rs.getString("guohl");
                        if(!flag){
                            strchangfys_sl=rs.getString("yanssl");
                        }
//						 ʹ�ò����жϣ��Ƿ��վ����䷽ʽ��ͬ����ȡ����ʽ������
                        if(flag){
                            if(MainGlobal.getXitxx_item("�����ѯ", "ʵ�����Ƿ�������䷽ʽ��ͬ��������", lgdiancxxb_id, "��").equals("��") ){
                                if(rs.getString("yunsfsb_id").equals("1")){
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,0)+"";
                                }else{
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,1)+"";
                                }
                            }
                        }
//						 �����糧�˷ѽ���ʱ��ʵ�������ý�������
                        if(lgdiancxxb_id==476){
                            strchangfys_sl=rs.getString("jiessl");
                        }

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
//						 strjiesyfbz_sl = rs.getString("jiessl"); //�˷ѽ�������



                        if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){
                            //��ͬ�糧,ʵ��������ʾ������,
                            //�˷ѽ���������ʾjiessl
                            strchangfys_sl=CustomMaths.round(rs.getString("jiessl"),2)+"";
                            strjiesyfbz_sl = rs.getString("jiessl"); //�˷ѽ�������

                        }else{
                            strjiesyfbz_sl = rs.getString("gongfsl");
                        }

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

            type=MainGlobal.getXitxx_item("����", "���㵥������λ", String.valueOf(lgdiancxxb_id), "GD");
            if(type.equals("GD")){//�������
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
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Qnetar));
                }

                if(!strzhejbz_Std.equals("")){
                    meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Std));
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Std));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Std));
                }

                if(!strzhejbz_Ad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Ad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Ad));
                }

                if(!strzhejbz_Vdaf.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vdaf));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Vdaf));
                }

                if(!strzhejbz_Mt.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mt));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Mt));
                }

                if(!strzhejbz_Qgrad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qgrad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Qgrad));
                }

                if(!strzhejbz_Qbad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qbad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Qbad));
                }

                if(!strzhejbz_Had.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Had));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Had));
                }

                if(!strzhejbz_Stad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Stad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Stad));
                }

                if(!strzhejbz_Mad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Mad));
                }

                if(!strzhejbz_Aar.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aar));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Aar));
                }

                if(!strzhejbz_Aad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Aad));
                }

                if(!strzhejbz_Vad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Vad));
                }

                if(!strzhejbz_T2.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_T2));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_T2));
                }

                if(!strzhejbz_Yunju.equals("")){
                    meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Yunju));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Yunju));
                }

                if(!strzhejbz_Star.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Star));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Star));
                }

                //��ʽ�ۿ�С�ƣ���������С��λ
                mkoukxj = CustomMaths.Round_new(mkoukxj, 2);

                if(stryunsfs.equals(Locale.tiel_yunsfs)){// ��·
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
                        +(-Double.parseDouble(strzhejbz_Aar.equals("")?"0":strzhejbz_Aar))
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

                strjiesbz_Qnetar = JieszbArray[0];

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

                if(strjiesbz_Stad.equals("")) {
                    strjiesbz_Stad = JieszbArray[2];
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
                if (strjiesbz_Std.equals("")){
                    strjiesbz_Std="0";
                }
                if (strjiesbz_Stad.equals("")){
                    strjiesbz_Stad="0";
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
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(ad,0))/SUM(jingz),2)) AS ad,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(vdaf,0))/SUM(jingz),2)) AS vdaf,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(mt,0))/SUM(jingz),1)) AS mt,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(Stad,0))/SUM(jingz),2)) AS stad,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(Stad,0))/SUM(jingz),2)) AS std,\n" +
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
                        if(strjiesbz_Ad.equals("0")){

                            strjiesbz_Ad = rs3.getString("ad");
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
                        if(strjiesbz_Stad.equals("0")){

                            strjiesbz_Stad = rs3.getString("stad");
                        }
                        if(strjiesbz_Std.equals("0")){

                            strjiesbz_Std = rs3.getString("std");
                        }
                    }
                }
                rs3.close();
                cn1.Close();

                String strjiesbz_Shulzj="";
                sql = "select * from danpcjsmxb d,jiesb j where d.zhibb_id=21 and d.jiesdid= j.id and j.bianm='"+where+"'";
                ResultSetList rs4 = cn.getResultSetList(sql);
                if(rs4.next()){
                    strjiesbz_Shulzj = rs4.getString("zhejje");
                }
                rs4.close();
                //				 ������MJ/kg
                strjiesbz_Qnetar=String.valueOf(MainGlobal.kcalkg_to_Mjkg(Double.parseDouble(strjiesbz_Qnetar),
                        ((Visit)this.getVisit()).getFarldec()));		//��������

                //����xitxxb�е����õõ����㵥�н��㲿�ŵ�����
                String BuM = "ȼ�Ϲ���";
                BuM = MainGlobal.getXitxx_item("����", "���㲿������", ""+lgdiancxxb_id, BuM);
                JDBCcon con=new JDBCcon();
                ResultSet frs=con.getResultSet("select f.url from hetb h inner join hetfjb f on h.id=f.hetid where h.hetbh='"+strHetbh+"' and f.url is not null");
                String url="#";
                if(frs.next()){
                    url=frs.getString("url");
                }
                strHetbh="<a href='"+url+"'>"+strHetbh+"</a>";
                String ArrHeader[][]=new String[5][19];
                ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
//					 ArrHeader[1]=new String[] {"�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾","�� �� �� �� �� չ �� �� �� �� �� ˾"};
//					 ArrHeader[2]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
                ArrHeader[1]=new String[] {"ȼ  ��  �� ú ̿ ��  ��  ��  ��","","","","","","","","","","","","","","","","","","",};
                ArrHeader[2]=new String[] {"","","","","","","","","","","","","","","","","","",""};
                ArrHeader[3]=new String[] {"��λ��"+tianbdw,"","","","","","�������ڣ�"+stryansrq,"","","","","","","","��λ����,Ԫ/��,MJ/kg,%,Ԫ","","","","","","",};
                ArrHeader[4]=new String[] {"���㵥��ţ�"+strbianh,"","","","","","","","","","","","","","��ͬ��ţ�"+strHetbh,"","","","",""};


                String ArrBody[][]=new String[20][19];
                ArrBody[0]=new String[] {"���㲿�ţ�" + BuM,"","","������λ��"+strfahdw,"","","","","","���䵥λ��"+strYunsdw,"","","","���䵥λ��"+strYunsdw,"","","���ڣ�"+strjiesrq,"",""};//"Ʒ�֣�"+strhuowmc,""};
                ArrBody[1]=new String[] {"����","","","","","����","","","","","","","","","","","ú��","","˰��"};
                ArrBody[2]=new String[] {"����","Ʊ��","ӯ��","����","ʵ��","ú��","�ۿ�","","","","","","","���ۺϼ�","����˰��","","","",""};
                ArrBody[3]=new String[] {"","","","","","","��ֵ","�ҷ�","�ӷ���","ˮ��","���","����","С��","","","","","",""};
                ArrBody[4]=new String[] {""+strches+"",""+strfahsl+"",""+("0.0".equals(strxiancsl_sl)?"":-Double.parseDouble(strxiancsl_sl)+"")+"",""+(Double.parseDouble(strxiancsl_sl)<0?"":""+strxiancsl_sl)+""+"",""+strchangfys_sl+"",""+meij+"",""+
                        ("".equals(Relkk)?"0":-Double.parseDouble(Relkk))+"",""+
                        ("".equals(Huifkk)?"0":-Double.parseDouble(Huifkk))+"",""+("".equals(Huiffkk)?"0":-Double.parseDouble(Huiffkk))+"",""+
                        ("".equals(Shuifkk)?"0":-Double.parseDouble(Shuifkk))+"",""+("".equals(liufkk)?"0":-Double.parseDouble(liufkk))+"",""+
                        ("".equals(Qitkk)?"0":-Double.parseDouble(Qitkk))+"",""+(-mkoukxj)+"",
                        ""+strzhejbz_sl+"",""+strbuhsdj+"","",""+formatq(strjiakhj)+"","",""+formatq(strshuik_mk)+""};
                ArrBody[5]=new String[] {"��ֵ","�ҷ�<br>A,d","�ӷ���<br>Vdaf","ˮ��<br>Mt","���<br>St,d","Ӧ���ۿ�","","Ӧ��˰��","","�����ۿ�","","ʵ�����","","","","","","",""};
                ArrBody[6]=new String[] {""+ (((Visit)this.getVisit()).getFarldec()==3 ? new DecimalFormat("0.000").format(Double.parseDouble(strjiesbz_Qnetar)) : strjiesbz_Qnetar)+"<br>("+new DecimalFormat("0").format(CustomMaths.Round_new(Double.parseDouble(strjiesbz_Qnetar)/0.0041816,0))+")",
                        ""+ ("".equals(strjiesbz_Ad)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Ad)))+"",""+
                        ("".equals(strjiesbz_Vdaf)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Vdaf)))+"",""+
                        ("".equals(strjiesbz_Mt)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Mt)))+"",""+
                        ("".equals(strjiesbz_Std)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Std)))+"",""+strjiakhj+"","",""+formatq(strshuik_mk)+"","",""+strbukmk,"",""+formatq(strjialhj)+"","","","","","","",""};
                ArrBody[7]=new String[] {"�˾�","","�˷ѵ�����ϸ","","","","","","","","","","","","","","","","ӡ��˰"};
                ArrBody[8]=new String[] {"����","����","����","����","����","ר��","����","����","�����˷�","","","","","","���ӷ�","","","",""};
                ArrBody[9]=new String[] {"","","","","","","","","�總��","��ɳ","��װ","����","����","С��","ȡ�ͳ�","�����","���ۺϼ�","����˰��",""};
                ArrBody[10]=new String[] {strchangfys_Yunju,//1
                        "",//2
                        ""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"",
                        "",//4
                        ""+CustomMaths.Round_New(Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)+"",
                        "",//6
                        "",//7
                        ""+strHansdj+"",//8
                        "",//9
                        "",//10
                        "",//11
                        "",//12
                        "",//13
                        ""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(stryunzshj.equals("")?"0":stryunzshj)/Double.parseDouble(strches)==0?1:Double.parseDouble(strches),2)):"")+"",
                        "",//15
                        "",//16
                        ""+strHansdj+"",//17
                        ""+strBuhsdj+"",//18
                        ""};//19
                ArrBody[11]=new String[] {"�����˷�","","�����˷�","","�����˷�","","ר���˷�","","��;�˷�","","�����˷�","","�����˷�","","���ӷ�","","�ۿ�","","ʵ���˷ѽ��"};
                ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","�ۿ�","",""};
                ArrBody[13]=new String[] {""+(bTl_yunsfs?strtielyf:"")+"","","","",""+strkuangqyf+"","","","","","",""+(bGl_yunsfs?strtielyf:"")+"","","","","","",""+strbukouqzf+"","",""+stryunzshj+""};
                ArrBody[14]=new String[] {"��ע��"+(jiesdbz==null?"":jiesdbz),"","","","","","","","","","","","","","","","","",""};
                ArrBody[15]=new String[] {"ע�����ݱ���ҵ�ʽ�����ƶ�Ȩ�����л����������򣬻����ż�������д�������ǩ�������ڡ�","","","","","","","","","","","","","","","","","",""};
                ArrBody[16]=new String[] {"","","","","","","","","","","","","","","","","","",""};
//----------------------------------
                sql="select fuid from gongysb where id="+gongysb_id;
                long fuid=0;
                ResultSet grs=con.getResultSet(sql);
                if(grs.next()){
                    fuid=grs.getLong("fuid");
                }

                ArrBody[17]=new String[] {"","","","","","","","","","","","","","","","","","",""};
                if(fuid==9382){
                    ArrBody[18]=new String[] {"����:","","","���ܾ�Ӫ�쵼:","","","","ȼ�Ϲ���:","","","","�������:","","","","����:","","",""};
                }else{
                    ArrBody[18]=new String[] {"����:","","���ܾ�Ӫ�쵼:","","","","ȼ�Ϲ���:","","","","�������:","","","��ú���Ҳ�:","","","����:","",""};

                }
                ArrBody[19]=new String[] {"","","","","","","","","","","","","","","","&nbsp","","",""};

                int ArrWidth[]=new int[] {54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54};
//                int ArrWidthTitle[]=new int[] {220,170,320,160,160};
                //				 ����ҳTitle
                rt.setTitle(new Table(ArrHeader,0,0,0));
                rt.setBody(new Table(ArrBody,0,0,0));
                if(fuid==9382){
                    rt.body.mergeCell(19,4,19,5);
                    rt.body.mergeCell(19,8,19,9);
                    rt.body.mergeCell(19,12,19,13);
                }else {
                    rt.body.mergeCell(19,3,19,4);
                    rt.body.mergeCell(19,7,19,8);
                    rt.body.mergeCell(19,11,19,12);
                    rt.body.mergeCell(19,14,19,15);
                }

                rt.body.setWidth(ArrWidth);
                rt.title.setWidth(ArrWidth);

                //				 �ϲ���Ԫ��
                //				 ��ͷ_Begin
                //rt.title.mergeCell(1, 9, 1, 19);
                rt.title.mergeCell(2, 1, 2, 19);

                rt.title.mergeCell(3, 1, 3, 19);
                rt.title.mergeCell(4, 1, 4, 6);
                rt.title.mergeCell(4, 7, 4, 12);
                rt.title.mergeCell(4, 15, 4, 19);
                rt.title.mergeCell(5, 1, 5, 5);
                rt.title.mergeCell(5, 15, 5, 19);

                rt.title.getCell(5,4).align=Table.ALIGN_RIGHT;
                rt.title.getCell(5,5).align=Table.ALIGN_RIGHT;

//                rt.title.getCell(5,1).setBorderNone();
//                rt.title.getCell(5,2).setBorderNone();
//                rt.title.getCell(5,3).setBorderNone();
//                rt.title.getCell(5,4).setBorderNone();
//                rt.title.getCell(5,5).setBorderNone();
                for(int i=0;i<rt.title.getCols();i++){
                    rt.title.getCell(5,i).setBorderNone();
                }
                rt.title.setBorder(0,0,0,0);
                rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
                rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
                rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
                rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);

                rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
                rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
                rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
                rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);

                rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
                rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
                rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.title.setCellAlign(4, 1, Table.ALIGN_LEFT);
                rt.title.setCellAlign(4, 2, Table.ALIGN_LEFT);
                rt.title.setCellAlign(4, 3, Table.ALIGN_CENTER);
                rt.title.setCellAlign(4, 4, Table.ALIGN_LEFT);
                rt.title.setCellAlign(4, 5, Table.ALIGN_RIGHT);

                //				 ����
                rt.title.setCells(2, 1, 2, 5, Table.PER_FONTNAME, "����");
                rt.title.setCells(2, 1, 2, 5, Table.PER_FONTSIZE, 20);
                //				 ����

                //				 ͼƬ
                rt.title.setCellImage(1, 1, 228, 38, "imgs/report/GDTLogo_new.gif");	//����ı�־�����ֳ�Ҫһ�����Ͼ����ˣ�
                rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
                rt.title.setCellImage(3, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
                //				 ͼƬ_End

                //				 ��ͷ_End
                //				 ����_Begin
                rt.body.mergeCell(1,1,1,3);
                rt.body.mergeCell(1,4,1,9);
//                rt.body.mergeCell(1,10,1,13);
                rt.body.mergeCell(1,10,1,16);
                rt.body.mergeCell(1,17,1,19);
                rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);

                rt.body.mergeCell(2,1,2,5);
                rt.body.mergeCell(2,6,2,16);
                rt.body.mergeCell(2,17,4,18);
                rt.body.mergeCell(2,19,4,19);
                rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(3,1,4,1);
                rt.body.mergeCell(3,2,4,2);
                rt.body.mergeCell(3,3,4,3);
                rt.body.mergeCell(3,4,4,4);
                rt.body.mergeCell(3,5,4,5);
                rt.body.mergeCell(3,6,4,6);
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
                rt.body.mergeCell(12,17,13,18);
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
                rt.body.mergeCell(14,17,14,18);
                rt.body.setRowCells(14, Table.PER_ALIGN, Table.ALIGN_CENTER);
//----------------------------------
                rt.body.mergeCell(15,1,15,19);
                rt.body.setRowCells(15, Table.PER_BORDER_BOTTOM, 2);
                rt.body.setCells(15, 1, 15, 1, Table.PER_BORDER_RIGHT, 2);

                rt.body.mergeCell(16,1,16,19);
                rt.body.setRowCells(16, Table.PER_BORDER_BOTTOM, 2);
                rt.body.setCells(16, 1, 16, 1, Table.PER_BORDER_RIGHT, 2);
                //				 ��ע����(����һ��)
                rt.body.mergeCell(17,1,17,19);
                rt.body.setRowHeight(17,8);
                rt.body.setRowCells(17, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(17, Table.PER_BORDER_RIGHT, 0);

                rt.body.setBorder(0, 0, 2, 0);
                rt.body.setCells(1, 1, 16, 1, Table.PER_BORDER_LEFT, 2);
                rt.body.setCells(1, 19, 16, 19, Table.PER_BORDER_RIGHT, 2);
                rt.body.setCells(1, 18, 1, 18, Table.PER_BORDER_RIGHT, 2);



                rt.body.mergeCell(18,1,18,11);
                rt.body.mergeCell(18,12,18,15);
                rt.body.mergeCell(18,16,18,19);
                rt.body.setRowHeight(18,5);
                rt.body.setRowCells(18, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(18, Table.PER_BORDER_RIGHT, 0);
//--------------------------------------------------

                rt.body.setRowCells(19, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(19, Table.PER_BORDER_RIGHT, 0);

                rt.body.mergeCell(20,12,20,15);
                rt.body.mergeCell(20,16,20,19);
                rt.body.setRowHeight(20,5);
                rt.body.setRowCells(20, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(20, Table.PER_BORDER_RIGHT, 0);

            }

            // ����ҳ��
            _CurrentPage = 1;
            _AllPages=1;
//				_AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }

            //JDBCcon con = new JDBCcon();
            String sqll="select zhi from xitxxb where mingc='���㵥��ҳ��ʾ'";
            ResultSetList rsl=cn.getResultSetList(sqll);
            if(rsl.next()){
                if(rsl.getString("zhi").equals("��")){

                    StringBuffer sb =new StringBuffer();
                    if (rt.title != null) {
                        sb.append(rt.title.getHtml());
                    }

                    if (rt.body != null) {
                        sb.append(rt.body.getHtml());
                    }

                    if (rt.footer != null) {
                        sb.append(rt.footer.getHtml());
                    }
                    return sb.toString();
                }
            }

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

    public String formatq_2(String strValue){
        //��������"-,169,864.60"ת��Ϊ"-169,864.60"
        String strtmp=strValue;
        if(strtmp.lastIndexOf("-,")==0){

            strtmp=strtmp.substring(0,1)+strtmp.substring(2,strtmp.length());

        }
        return strtmp;

    }


}