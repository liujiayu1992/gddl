package com.zhiren.dc.huaygl;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
/*����:���ܱ�
 *����:2010-7-24 
 *����:��ϵͳ���� '�������������' �������ʱ,���ɵı�����"����+��λ�����",
 *    ����0715159�����鲿�ž�����������е㳤,ÿ����������������̫����,��ȥ��
 *    �·�,ֻ��"��+��λ�����"��Ϊ����,����15159
 * ��������һ�²�������
 *      mingc = '��������������Ƿ��д'
 * 		zhi = '��'
 * 		leib = '����'
 * 		zhuangt = 1
 * 
 * 
 */



/**
 * 2010-4-25
 * ���ܱ�
 * �޸�������������ʱ,���Ӳ�����ת��Ĳ�������
 * select zhi from xitxxb where mingc = '�������������Ƿ�ת��' and leib = '����' and zhi ='��' and zhuangt =1 
 */
/*
 * 2009-02-23
 * ����
 * �޸�Ĭ������Ϊ���ɵ���
 */
/*
 * 2009-05-15
 * ����
 * ����AddCaiybh����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-23
 * �����������ж�����ֶ�¼�������������κ�һ�������ɵ��������벻����ǰ׺
 */
/*
 * �޸ģ�����
 * ʱ�䣺2009-06-05
 * ����������һ��������Ʒ���ű�ķ������ڲ����ж����������Ϣ��һ�ǲ���ʱ�䣬һ�Ǳ�ע��Ϣ
 * ����������InsYangpdhb1
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-10 11��02
 * �������޸�����ת���ķ������ж�����õ糧δȡ��ת���������ã���õ糧IDΪһ���������ӳ�ID��ʹ������ID�����ж�
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-31
 * �����������µĲ�����������ɷ�ʽ ����ϵͳ��Ϣ���м�����Ϣ
 * 		mingc = '�������������'
 * 		zhi = '��'
 * 		leib = '����'
 * 		zhuangt = 1
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-05
 * �����������������ɲ���ʱ���ܽ���ת�������
 */

/*
 * ���ߣ���ΰ
 * ʱ�䣺2010-08-17
 * ��������Ӳ������ý��������Ϊ�������룬�����������������������
 * 		select * from xitxxb where mingc = '�������������������������' and leib = '����' and zhi ='��' and zhuangt =1
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-03-04
 * ������Ϊ�������ƻ������������ý��г��������
 */
public class Caiycl {

    public static int InsYangpdhb(JDBCcon con, long diancxxb_id, long zhilb_id, String caiyb_id,
                                  String zhillsb_id, String leibid, String bmid, String zhuanmsz, String leib){
        String sql = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id) "+
                "values(getnewid(" + diancxxb_id + ")," + caiyb_id + "," + zhillsb_id +
                ",'" + zhuanmsz + "','" + leib + "'," + bmid + "," + leibid + ")";
        return con.getInsert(sql);
    }
    public static int InsZhillsb(JDBCcon con, long diancxxb_id, long zhilb_id,
                                 String zhillsb_id, String leibid, String bmid, String leib){
        String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID) "+
                "values(" + zhillsb_id + "," + zhilb_id + ",0,'" + leib + "'," + leibid + "," + bmid + ")";
        return con.getInsert(sql);
    }

    public static int InsZhillsb(JDBCcon con, long diancxxb_id, long zhilb_id,
                                 String zhillsb_id, String leibid, String bmid, String leib,String caiysl){
        String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,BIL) "+
                "values(" + zhillsb_id + "," + zhilb_id + ",0,'" + leib + "'," + leibid + "," + bmid + ","+caiysl+")";
        return con.getInsert(sql);
    }
    public static int InsZhuanmb(JDBCcon con,long zhuanmlb_id, long diancxxb_id,
                                 String zhillsb_id, String bianm){
        String sql = "insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
                "values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
                "'," + zhuanmlb_id + ")";
        return con.getInsert(sql);
    }
    //
    public static int Zhuanmcz(JDBCcon con, long diancxxb_id, String zhillsb_id,
                               String bianm, boolean shifzm, String zhuanmqz){

//		String sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id;
        String sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id + " order by jib";
        ResultSetList rsl =  con.getResultSetList(sql);
//		�ж�����õ糧δȡ��ת���������ã���õ糧IDΪһ���������ӳ�ID��ʹ������ID�����ж�
        if(rsl == null  || rsl.getRows() == 0){
//			sql = "select z.* from zhuanmlb z, diancxxb d where z.diancxxb_id = d.fuid and d.id =" + diancxxb_id;
            sql = "select z.* from zhuanmlb z, diancxxb d where z.diancxxb_id = d.fuid and d.id =" + diancxxb_id + "order by z.jib";
            rsl = con.getResultSetList(sql);
        }
        long zhuanm = MainGlobal.getSequenceNextVal(con,SysConstant.BiascCodeSequenceName);
        String sqlRandom = "select * from xitxxb where mingc = '�������������' and leib = '����' and zhi ='��' and zhuangt =1";
        String sqlCaiybmShort="select * from xitxxb where mingc = '��������������Ƿ��д' and leib = '����' and zhi ='��' and zhuangt =1";
        boolean random = con.getHasIt(sqlRandom);
        boolean RandomCaiybmShort = con.getHasIt(sqlCaiybmShort);
        sqlRandom = "select to_char(caiyrq,'yyyy-mm-dd') riq, to_char(caiyrq,'mmdd') cyrq,to_char(caiyrq,'dd') Short_cyrq from caiyb c,\n" +
                "(select caiyb_id from yangpdhb where zhilblsb_id = "+
                zhillsb_id+") d\n" + "where  c.id = d.caiyb_id";
        ResultSetList rs = con.getResultSetList(sqlRandom);

//		ww 2010-08-17 �������������룬�����ͻ�������������
        String sqlZhiHrandom = "select * from xitxxb where mingc = '�������������������������' and leib = '����' and zhi ='��' and zhuangt =1";
        boolean ZhiHrandom = con.getHasIt(sqlZhiHrandom);

//		
        String sqlhuaymQ = "select * from xitxxb where mingc = '��ָ������ʱ���������ת������' and leib = '����' and zhi ='��' and zhuangt =1";
        boolean huaymQ = con.getHasIt(sqlhuaymQ);
        String bianm_tmp = bianm;

        String rq = "";
        String riq = "";
        if(rs.next()){
            if(RandomCaiybmShort){//���ɱ���ʱ,��Ҫ�·�
                rq = rs.getString("Short_cyrq");
            }else{//���ɱ���ʱ,�����·�
                rq = rs.getString("cyrq");
            }

            riq = rs.getString("riq");
        }
        rs.close();
        int flag;
        while(rsl.next()){
            if(random){
                int i = 0;
                while(true){
                    String endStr = String.valueOf(Math.random());
                    while(endStr.length()<5){
                        endStr = endStr + "0";
                    }
                    endStr = endStr.substring(2, 5);
                    sqlRandom =
                            "select * from caiyb c, yangpdhb y, zhuanmb z\n" +
                                    "where  c.id = y.caiyb_id and c.caiyrq = to_date('"+riq+"','yyyy-mm-dd')\n" +
                                    "and y.zhilblsb_id = z.zhillsb_id and z.bianm = '"+rq + endStr+"'";
                    if(!con.getHasIt(sqlRandom)){
                        bianm = rq + endStr;
                        break;
                    }
                    if(i++ > 100){
                        random = false;
                        break;
                    }

                }
            }else{
                if(shifzm){
                    zhuanm = zhuanm*2 + 729;
                    bianm = zhuanmqz + zhuanm;
                }else{

                    //ww 2010-08-17 �������������룬�����ͻ�������������
                    if (ZhiHrandom) {
                        if (!"��������".equals(rsl.getString("mingc"))) {
                            zhuanm = zhuanm*2 + 729;
                            bianm = zhuanmqz + zhuanm;
                        } else {
                            //  ��2һ�Σ�Ϊ��ʹ��������������ԭ���ı���һ��
                            zhuanm = zhuanm*2 + 729;
                        }
                    } else {
                        bianm = zhuanmqz + bianm;
                        if(huaymQ){
                            if ("�������".equals(rsl.getString("mingc"))) {
                                bianm = bianm_tmp;
                            }
                        }
                    }
                }
            }
            flag = InsZhuanmb(con,rsl.getLong("id"),diancxxb_id,zhillsb_id,bianm);
            if(flag == -1){
                return -1;
            }
        }
        rsl.close();
        return 0;
    }

    public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
                                 long meikxxb_id, long jihkjb_id, long yunsfsb_id, long chezxxb_id,
                                 String bianm) {
        String sql ="";
        ResultSetList rsl = null;
        String caiyb_id = null;
        String caiybm = null;
        int flag ;
//		�жϴ�����ID�Ƿ���� ��������򷵻��½���ųɹ�
        sql = "select * from zhuanmb where zhillsb_id in (select id from zhillsb"+
                " where zhilb_id in(" + zhilb_id+"))";
        rsl = con.getResultSetList(sql);
        if(rsl.getRows()>0){
            rsl.close();
            return 0;
        }
//		��ò�����ID
        sql = "select * from caiyb where zhilb_id = " + zhilb_id;
        rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            caiyb_id = rsl.getString("id");
            caiybm = rsl.getString("bianm");
        }
        rsl.close();
//		�鿴��Ӧ�̶�Ӧ�Ĳ�������
        sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhdyb c, fenkcyfsb f, bumb b, leibb l " +
                "where c.caizhfsb_id = f.caizhfsb_id\n" +
                " and c.bum_id = b.id\n" +
                " and c.leibb_id = l.id\n" +
                " and f.diancxxb_id = " + diancxxb_id + "\n" +
                " and f.meikxxb_id = " + meikxxb_id + "\n" +
                " and f.yunsfsb_id = " + yunsfsb_id + "\n" +
                " and f.chezxxb_id = " + chezxxb_id + "\n" +
                " and f.jihkjb_id = " + jihkjb_id + "\n" ;
        rsl = con.getResultSetList(sql);

        if(rsl.getRows() == 0){
//			���ϵͳû������ ��ȡ��ϵͳĬ������
            sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
                    "where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
                    " and c.leibb_id = l.id\n" +
                    " and f.mingc = 'Ĭ��' and f.diancxxb_id =" + diancxxb_id;

            rsl.close();
            rsl = con.getResultSetList(sql);
            if(rsl.getRows() == 0){
//				���Ĭ������δ���� �򷵻ش���
                return -1;
            }
        }
        while(rsl.next()){
//			�½�������ʱID
            String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//			ת���������
            String leib = rsl.getString("leib");
//			ת�����
            String zhuanmsz = rsl.getString("zhuanmsz");
//			�������ID
            String leibid = rsl.getString("lbid");
//			����ID
            String bumid = rsl.getString("bmid");
//			�Ƿ�ת��
            boolean shifzm = rsl.getInt("shifzm") == 1;
//			д����Ʒ���ű�
            flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id,
                    zhillsb_id, leibid, bumid, zhuanmsz, leib);
            if(flag == -1){
//				��������򷵻ش���
                return -1;
            }
//			д��������ʱ��
            InsZhillsb(con, diancxxb_id, zhilb_id,
                    zhillsb_id, leibid, bumid, leib);
            if(flag == -1){
//				��������򷵻ش���
                return -1;
            }
            if(bianm == null){
//			�ж�������Զ����ɱ���
                flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, caiybm, shifzm, zhuanmsz);
                if(flag == -1){
//					��������򷵻ش���
                    return -1;
                }
            }else{
//			��� ��¼����==�������б��� ��ʹ��ת������
                if(bianm.equalsIgnoreCase(caiybm)){
                    zhuanmsz = "";
                }
//			������Զ����ɱ���
                flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, false, zhuanmsz);
                if(flag == -1){
//					��������򷵻ش���
                    return -1;
                }
            }
        }
        rsl.close();
        return 0;
    }
    public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id){
        return CreatBianh(con,zhilb_id,diancxxb_id,0,0,0,0);
    }

    public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
                                 long meikxxb_id, long jihkjb_id, long yunsfsb_id, long chezxxb_id) {
        return CreatBianh(con, zhilb_id, diancxxb_id,
                meikxxb_id, jihkjb_id, yunsfsb_id, chezxxb_id, null);
    }

    public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
                                 String bianm){
        return CreatBianh(con,zhilb_id,diancxxb_id,0,0,0,0,bianm);
    }
    public static int InsYangpdhb1(JDBCcon con, long diancxxb_id, long zhilb_id, String caiyb_id,
                                   String zhillsb_id, String leibid, String bmid, String zhuanmsz, String leib,String caiysj,String beiz){
        String sql = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id,caiysj,beiz) "+
                "values(getnewid(" + diancxxb_id + ")," + caiyb_id + "," + zhillsb_id +
                ",'" + zhuanmsz + "','" + leib + "'," + bmid + "," + leibid +",to_date('"+caiysj+"','yyyy-mm-dd'),'"+beiz +"')";
        return con.getInsert(sql);
    }
    public static int AddBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
                               String caiyb_id,  String leibb_id, String bumb_id, String leib,
                               String zhuanmsz){
        String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//		д����Ʒ���ű�
        int flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id,
                zhillsb_id, leibb_id, bumb_id, zhuanmsz, leib);
        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
//		д��������ʱ��
        InsZhillsb(con, diancxxb_id, zhilb_id,
                zhillsb_id, leibb_id, bumb_id, leib);
        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
//		д��ת���
        flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, "", true, zhuanmsz);
        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
        return flag;
    }
    public static int AddBianhC(JDBCcon con, long zhilb_id, long diancxxb_id,
                                String caiyb_id,  String leibb_id, String bumb_id, String leib,
                                String zhuanmsz,String caiysj,String beiz){
        String sql="";
        ResultSetList rsl;
        String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//		д����Ʒ���ű�


        int flag = InsYangpdhb1(con, diancxxb_id, zhilb_id, caiyb_id,
                zhillsb_id, leibb_id, bumb_id, zhuanmsz, leib,caiysj,beiz);
        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
//		д��������ʱ��
        InsZhillsb(con, diancxxb_id, zhilb_id,
                zhillsb_id, leibb_id, bumb_id, leib);
        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
//		д��ת���
        boolean IsZhuanm=true;
        sql = " select zhi from xitxxb where mingc = '�������������Ƿ�ת��' and leib = '����' and zhi ='��' and zhuangt =1";
        rsl = con.getResultSetList(sql);
        while(rsl.next()){
            IsZhuanm  =false; ;
        }
        if(IsZhuanm){
            flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, "", IsZhuanm, zhuanmsz);
        }else{
            //����:���ܱ�,�������һ��5λ���ı�����Ϊ���������ı���,���Ҳ�����ת��
            String endStr = String.valueOf(Math.random());
            while(endStr.length()<7){
                endStr = endStr + "0";
            }
            endStr = endStr.substring(2, 7);
            flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, endStr, IsZhuanm, zhuanmsz);
        }

        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
        String ypdhbid ="";
        sql = " select y.id from zhillsb z ,yangpdhb y  where  y.zhilblsb_id=z.id and z.id="+zhillsb_id;
        rsl = con.getResultSetList(sql);
        while(rsl.next()){
            ypdhbid = rsl.getString("id");
        }
        sql = "select b.renyxxb_id from zhillsb z ,yangpdhb y , caiyryglb b where b.yangpdhb_id=y.id and  y.zhilblsb_id=z.id and z.zhilb_id ="+zhilb_id;

        rsl = con.getResultSetList(sql);
        while(rsl.next()){
            String temp = rsl.getString("renyxxb_id");

            if(temp != ""){
                String q = "insert into caiyryglb(yangpdhb_id,renyxxb_id) values("+ypdhbid+","+temp+")\n";
                con.getInsert(q);
                con.commit();

            }else{
                break;
            }

        }


        return flag;
    }

    public static int AddBianhHD(JDBCcon con, long zhilb_id, long diancxxb_id,
                                 String caiyb_id,  String leibb_id, String bumb_id, String leib,
                                 String zhuanmsz,String caiysj,String beiz,String caiybm,String caiyml){
        String sql="";
        ResultSetList rsl;
        String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//		д����Ʒ���ű�
        int flag = InsYangpdhb1(con, diancxxb_id, zhilb_id, caiyb_id,
                zhillsb_id, leibb_id, bumb_id, zhuanmsz, leib,caiysj,beiz);
        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
//		д��������ʱ��
        InsZhillsb(con, diancxxb_id, zhilb_id, zhillsb_id, leibb_id, bumb_id, leib,caiyml);
        if(flag == -1){
//			��������򷵻ش���
            return -1;
        }
//		д��ת���
        sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id + " order by jib";
        rsl =  con.getResultSetList(sql);
//		�ж�����õ糧δȡ��ת���������ã���õ糧IDΪһ���������ӳ�ID��ʹ������ID�����ж�
        if(rsl == null  || rsl.getRows() == 0){
            sql = "select z.* from zhuanmlb z, diancxxb d where z.diancxxb_id = d.fuid and d.id =" + diancxxb_id + "order by z.jib";
            rsl = con.getResultSetList(sql);
        }
        String bianm="";
        while(rsl.next()){
            bianm=bianm+zhuanmsz;
            flag = InsZhuanmb(con,rsl.getLong("id"),diancxxb_id,zhillsb_id,bianm+caiybm);
            if(flag == -1){
//				��������򷵻ش���
                return -1;
            }
        }

        String ypdhbid ="";
        sql = " select y.id from zhillsb z ,yangpdhb y  where  y.zhilblsb_id=z.id and z.id="+zhillsb_id;
        rsl = con.getResultSetList(sql);
        while(rsl.next()){
            ypdhbid = rsl.getString("id");
        }
        sql = "select b.renyxxb_id from zhillsb z ,yangpdhb y , caiyryglb b where b.yangpdhb_id=y.id and  y.zhilblsb_id=z.id and z.zhilb_id ="+zhilb_id;

        rsl = con.getResultSetList(sql);
        while(rsl.next()){
            String temp = rsl.getString("renyxxb_id");
            if(temp != ""){
                String q = "insert into caiyryglb(yangpdhb_id,renyxxb_id) values("+ypdhbid+","+temp+")\n";
                con.getInsert(q);
                con.commit();
            }else{
                break;
            }
        }
        return flag;
    }

    public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,String[] bianms) {
        String sql ="";
        ResultSetList rsl = null;
        String caiyb_id = null;
        String caiybm = null;
        int flag ;

//		ת���������
        String leib = null;
//		ת�����
        String zhuanmsz = null;
//		�������ID
        String leibid = null;
//		����ID
        String bumid = null;

//		�жϴ�����ID�Ƿ���� ��������򷵻��½���ųɹ�
        sql = "select * from zhuanmb where zhillsb_id in (select id from zhillsb"+
                " where zhilb_id in(" + zhilb_id+"))";
        rsl = con.getResultSetList(sql);
        if(rsl.getRows()>0){
            rsl.close();
            return 0;
        }
//		��ò�����ID
        sql = "select * from caiyb where zhilb_id = " + zhilb_id;
        rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            caiyb_id = rsl.getString("id");
            caiybm = rsl.getString("bianm");
        }
        rsl.close();

//		���ϵͳû������ ��ȡ��ϵͳĬ������
        sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
                "where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
                " and c.leibb_id = l.id\n" +
                " and f.mingc = 'Ĭ��' and f.diancxxb_id =" + diancxxb_id;

        rsl.close();
        rsl = con.getResultSetList(sql);
        if(rsl.getRows() == 0){
//				���Ĭ������δ���� �򷵻ش���
            return -1;
        }
        if(rsl.next()){
            leib = rsl.getString("leib");
            zhuanmsz = rsl.getString("zhuanmsz");
            leibid = rsl.getString("lbid");
            bumid = rsl.getString("bmid");
        }

        if("�ޱ���".equals(bianms[0])){
            //�����豸���뷢���쳣,ϵͳ���Ƿ��Զ�����һ����������(����)
            String isCaiyjbm = MainGlobal.getXitxx_item(con,"����", "�����豸���뷢���쳣ϵͳ�Ƿ��Զ�����", diancxxb_id + "", "false");
            if("��".equals(isCaiyjbm)){
//				����ʽһ������ǰ�����Զ�����һ����
                bianms = new String[1];
                bianms[0] = MainGlobal.getNewID(con,diancxxb_id).substring(3);
            }else{
//				����ʽ�����׳�����(�����豸���뷢���쳣��)
                return -2;
            }
        }

        for(int i=0;i<bianms.length;i++){
//			�½�������ʱID
            String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//			д����Ʒ���ű�
            flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id,
                    zhillsb_id, leibid, bumid, zhuanmsz, leib);
            if(flag == -1){
//				��������򷵻ش���
                return -1;
            }
//			д��������ʱ��
            InsZhillsb(con, diancxxb_id, zhilb_id,
                    zhillsb_id, leibid, bumid, leib);
            if(flag == -1){
//				��������򷵻ش���
                return -1;
            }
//			��� ��¼����==�������б��� ��ʹ��ת������
            if(bianms[i].equalsIgnoreCase(caiybm)){
                zhuanmsz = "";
            }
//			������Զ����ɱ���
            flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianms[i], false, zhuanmsz);
            if(flag == -1){
//					��������򷵻ش���
                return -1;
            }
        }
        rsl.close();
        return 0;
    }
}
