package com.zhiren.dc.huaygl;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
/*作者:王总兵
 *日期:2010-7-24 
 *描述:当系统配置 '采样编码随机数' 这个参数时,生成的编码是"月日+三位随机号",
 *    例如0715159，化验部门觉得这个编码有点长,每次向热量仪输入编号太繁琐,让去掉
 *    月份,只用"日+三位随机码"作为编码,例如15159
 * 所以增加一下参数配置
 *      mingc = '采样编码随机数是否简写'
 * 		zhi = '是'
 * 		leib = '采样'
 * 		zhuangt = 1
 * 
 * 
 */



/**
 * 2010-4-25
 * 王总兵
 * 修改新增采样编码时,增加不进行转码的参数设置
 * select zhi from xitxxb where mingc = '新增采样编码是否转码' and leib = '采样' and zhi ='否' and zhuangt =1 
 */
/*
 * 2009-02-23
 * 王磊
 * 修改默认设置为单采单化
 */
/*
 * 2009-05-15
 * 王磊
 * 新增AddCaiybh方法
 */
/*
 * 作者：王磊
 * 时间：2009-05-23
 * 描述：新增判断如果手动录入编码与进厂批次号一致则生成的其它编码不加入前缀
 */
/*
 * 修改：张琦
 * 时间：2009-06-05
 * 描述：新增一个插入样品单号表的方法，在插入中多加入两条信息，一是采样时间，一是备注信息
 * 新增方法：InsYangpdhb1
 */
/*
 * 作者：王磊
 * 时间：2009-09-10 11：02
 * 描述：修改生成转码表的方法，判断如果该电厂未取得转码类别的设置，则该电厂ID为一厂多制下子厂ID，使用主厂ID进行判断
 */
/*
 * 作者：王磊
 * 时间：2009-10-31
 * 描述：增加新的采样编码的生成方式 须在系统信息表中加入信息
 * 		mingc = '采样编码随机数'
 * 		zhi = '是'
 * 		leib = '采样'
 * 		zhuangt = 1
 */
/*
 * 作者：王磊
 * 时间：2009-11-05
 * 描述：解决随机码生成采样时不能进行转码的问题
 */

/*
 * 作者：王伟
 * 时间：2010-08-17
 * 描述：添加参数配置解决车次作为采样编码，化验编码和制样编码随机生成
 * 		select * from xitxxb where mingc = '车次做采样编制样化验码随机' and leib = '采样' and zhi ='是' and zhuangt =1
 */
/*
 * 作者：夏峥
 * 时间：2014-03-04
 * 描述：为邯郸采制化编码特殊设置进行程序调整。
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
//		判断如果该电厂未取得转码类别的设置，则该电厂ID为一厂多制下子厂ID，使用主厂ID进行判断
        if(rsl == null  || rsl.getRows() == 0){
//			sql = "select z.* from zhuanmlb z, diancxxb d where z.diancxxb_id = d.fuid and d.id =" + diancxxb_id;
            sql = "select z.* from zhuanmlb z, diancxxb d where z.diancxxb_id = d.fuid and d.id =" + diancxxb_id + "order by z.jib";
            rsl = con.getResultSetList(sql);
        }
        long zhuanm = MainGlobal.getSequenceNextVal(con,SysConstant.BiascCodeSequenceName);
        String sqlRandom = "select * from xitxxb where mingc = '采样编码随机数' and leib = '采样' and zhi ='是' and zhuangt =1";
        String sqlCaiybmShort="select * from xitxxb where mingc = '采样编码随机数是否简写' and leib = '采样' and zhi ='是' and zhuangt =1";
        boolean random = con.getHasIt(sqlRandom);
        boolean RandomCaiybmShort = con.getHasIt(sqlCaiybmShort);
        sqlRandom = "select to_char(caiyrq,'yyyy-mm-dd') riq, to_char(caiyrq,'mmdd') cyrq,to_char(caiyrq,'dd') Short_cyrq from caiyb c,\n" +
                "(select caiyb_id from yangpdhb where zhilblsb_id = "+
                zhillsb_id+") d\n" + "where  c.id = d.caiyb_id";
        ResultSetList rs = con.getResultSetList(sqlRandom);

//		ww 2010-08-17 车次做采样编码，制样和化验编码随机生成
        String sqlZhiHrandom = "select * from xitxxb where mingc = '车次做采样编制样化验码随机' and leib = '采样' and zhi ='是' and zhuangt =1";
        boolean ZhiHrandom = con.getHasIt(sqlZhiHrandom);

//		
        String sqlhuaymQ = "select * from xitxxb where mingc = '有指定编码时化验编码无转码设置' and leib = '采样' and zhi ='是' and zhuangt =1";
        boolean huaymQ = con.getHasIt(sqlhuaymQ);
        String bianm_tmp = bianm;

        String rq = "";
        String riq = "";
        if(rs.next()){
            if(RandomCaiybmShort){//生成编码时,不要月份
                rq = rs.getString("Short_cyrq");
            }else{//生成编码时,包含月份
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

                    //ww 2010-08-17 车次做采样编码，制样和化验编码随机生成
                    if (ZhiHrandom) {
                        if (!"采样编码".equals(rsl.getString("mingc"))) {
                            zhuanm = zhuanm*2 + 729;
                            bianm = zhuanmqz + zhuanm;
                        } else {
                            //  ×2一次，为了使制样、化验编码和原来的编码一致
                            zhuanm = zhuanm*2 + 729;
                        }
                    } else {
                        bianm = zhuanmqz + bianm;
                        if(huaymQ){
                            if ("化验编码".equals(rsl.getString("mingc"))) {
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
//		判断此质量ID是否存在 如果存在则返回新建编号成功
        sql = "select * from zhuanmb where zhillsb_id in (select id from zhillsb"+
                " where zhilb_id in(" + zhilb_id+"))";
        rsl = con.getResultSetList(sql);
        if(rsl.getRows()>0){
            rsl.close();
            return 0;
        }
//		获得采样表ID
        sql = "select * from caiyb where zhilb_id = " + zhilb_id;
        rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            caiyb_id = rsl.getString("id");
            caiybm = rsl.getString("bianm");
        }
        rsl.close();
//		查看供应商对应的采样定义
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
//			如果系统没有设置 则取得系统默认设置
            sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
                    "where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
                    " and c.leibb_id = l.id\n" +
                    " and f.mingc = '默认' and f.diancxxb_id =" + diancxxb_id;

            rsl.close();
            rsl = con.getResultSetList(sql);
            if(rsl.getRows() == 0){
//				如果默认设置未设置 则返回错误
                return -1;
            }
        }
        while(rsl.next()){
//			新建质量临时ID
            String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//			转码类别名称
            String leib = rsl.getString("leib");
//			转码编码
            String zhuanmsz = rsl.getString("zhuanmsz");
//			化验类别ID
            String leibid = rsl.getString("lbid");
//			部门ID
            String bumid = rsl.getString("bmid");
//			是否转码
            boolean shifzm = rsl.getInt("shifzm") == 1;
//			写入样品单号表
            flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id,
                    zhillsb_id, leibid, bumid, zhuanmsz, leib);
            if(flag == -1){
//				如果出错则返回错误
                return -1;
            }
//			写入质量临时表
            InsZhillsb(con, diancxxb_id, zhilb_id,
                    zhillsb_id, leibid, bumid, leib);
            if(flag == -1){
//				如果出错则返回错误
                return -1;
            }
            if(bianm == null){
//			判断如果是自动生成编码
                flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, caiybm, shifzm, zhuanmsz);
                if(flag == -1){
//					如果出错则返回错误
                    return -1;
                }
            }else{
//			如果 手录编码==采样表中编码 则不使用转码设置
                if(bianm.equalsIgnoreCase(caiybm)){
                    zhuanmsz = "";
                }
//			如果非自动生成编码
                flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, false, zhuanmsz);
                if(flag == -1){
//					如果出错则返回错误
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
//		写入样品单号表
        int flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id,
                zhillsb_id, leibb_id, bumb_id, zhuanmsz, leib);
        if(flag == -1){
//			如果出错则返回错误
            return -1;
        }
//		写入质量临时表
        InsZhillsb(con, diancxxb_id, zhilb_id,
                zhillsb_id, leibb_id, bumb_id, leib);
        if(flag == -1){
//			如果出错则返回错误
            return -1;
        }
//		写入转码表
        flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, "", true, zhuanmsz);
        if(flag == -1){
//			如果出错则返回错误
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
//		写入样品单号表


        int flag = InsYangpdhb1(con, diancxxb_id, zhilb_id, caiyb_id,
                zhillsb_id, leibb_id, bumb_id, zhuanmsz, leib,caiysj,beiz);
        if(flag == -1){
//			如果出错则返回错误
            return -1;
        }
//		写入质量临时表
        InsZhillsb(con, diancxxb_id, zhilb_id,
                zhillsb_id, leibb_id, bumb_id, leib);
        if(flag == -1){
//			如果出错则返回错误
            return -1;
        }
//		写入转码表
        boolean IsZhuanm=true;
        sql = " select zhi from xitxxb where mingc = '新增采样编码是否转码' and leib = '采样' and zhi ='否' and zhuangt =1";
        rsl = con.getResultSetList(sql);
        while(rsl.next()){
            IsZhuanm  =false; ;
        }
        if(IsZhuanm){
            flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, "", IsZhuanm, zhuanmsz);
        }else{
            //作者:王总兵,随机生成一个5位数的编码做为新增采样的编码,并且不进行转码
            String endStr = String.valueOf(Math.random());
            while(endStr.length()<7){
                endStr = endStr + "0";
            }
            endStr = endStr.substring(2, 7);
            flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, endStr, IsZhuanm, zhuanmsz);
        }

        if(flag == -1){
//			如果出错则返回错误
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
//		写入样品单号表
        int flag = InsYangpdhb1(con, diancxxb_id, zhilb_id, caiyb_id,
                zhillsb_id, leibb_id, bumb_id, zhuanmsz, leib,caiysj,beiz);
        if(flag == -1){
//			如果出错则返回错误
            return -1;
        }
//		写入质量临时表
        InsZhillsb(con, diancxxb_id, zhilb_id, zhillsb_id, leibb_id, bumb_id, leib,caiyml);
        if(flag == -1){
//			如果出错则返回错误
            return -1;
        }
//		写入转码表
        sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id + " order by jib";
        rsl =  con.getResultSetList(sql);
//		判断如果该电厂未取得转码类别的设置，则该电厂ID为一厂多制下子厂ID，使用主厂ID进行判断
        if(rsl == null  || rsl.getRows() == 0){
            sql = "select z.* from zhuanmlb z, diancxxb d where z.diancxxb_id = d.fuid and d.id =" + diancxxb_id + "order by z.jib";
            rsl = con.getResultSetList(sql);
        }
        String bianm="";
        while(rsl.next()){
            bianm=bianm+zhuanmsz;
            flag = InsZhuanmb(con,rsl.getLong("id"),diancxxb_id,zhillsb_id,bianm+caiybm);
            if(flag == -1){
//				如果出错则返回错误
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

//		转码类别名称
        String leib = null;
//		转码编码
        String zhuanmsz = null;
//		化验类别ID
        String leibid = null;
//		部门ID
        String bumid = null;

//		判断此质量ID是否存在 如果存在则返回新建编号成功
        sql = "select * from zhuanmb where zhillsb_id in (select id from zhillsb"+
                " where zhilb_id in(" + zhilb_id+"))";
        rsl = con.getResultSetList(sql);
        if(rsl.getRows()>0){
            rsl.close();
            return 0;
        }
//		获得采样表ID
        sql = "select * from caiyb where zhilb_id = " + zhilb_id;
        rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            caiyb_id = rsl.getString("id");
            caiybm = rsl.getString("bianm");
        }
        rsl.close();

//		如果系统没有设置 则取得系统默认设置
        sql = "select c.shifzm,b.id bmid,l.id lbid,l.zhuanmsz,l.mingc leib from caizhfsb f, caizhdyb c, bumb b, leibb l " +
                "where c.caizhfsb_id = f.id and c.bum_id = b.id\n" +
                " and c.leibb_id = l.id\n" +
                " and f.mingc = '默认' and f.diancxxb_id =" + diancxxb_id;

        rsl.close();
        rsl = con.getResultSetList(sql);
        if(rsl.getRows() == 0){
//				如果默认设置未设置 则返回错误
            return -1;
        }
        if(rsl.next()){
            leib = rsl.getString("leib");
            zhuanmsz = rsl.getString("zhuanmsz");
            leibid = rsl.getString("lbid");
            bumid = rsl.getString("bmid");
        }

        if("无编码".equals(bianms[0])){
            //采样设备编码发生异常,系统内是否自动生成一个样及编码(青铝)
            String isCaiyjbm = MainGlobal.getXitxx_item(con,"数量", "采样设备编码发生异常系统是否自动生成", diancxxb_id + "", "false");
            if("是".equals(isCaiyjbm)){
//				处理方式一，将当前发货自动生成一个样
                bianms = new String[1];
                bianms[0] = MainGlobal.getNewID(con,diancxxb_id).substring(3);
            }else{
//				处理方式二，抛出错误：(采样设备编码发生异常！)
                return -2;
            }
        }

        for(int i=0;i<bianms.length;i++){
//			新建质量临时ID
            String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//			写入样品单号表
            flag = InsYangpdhb(con, diancxxb_id, zhilb_id, caiyb_id,
                    zhillsb_id, leibid, bumid, zhuanmsz, leib);
            if(flag == -1){
//				如果出错则返回错误
                return -1;
            }
//			写入质量临时表
            InsZhillsb(con, diancxxb_id, zhilb_id,
                    zhillsb_id, leibid, bumid, leib);
            if(flag == -1){
//				如果出错则返回错误
                return -1;
            }
//			如果 手录编码==采样表中编码 则不使用转码设置
            if(bianms[i].equalsIgnoreCase(caiybm)){
                zhuanmsz = "";
            }
//			如果非自动生成编码
            flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianms[i], false, zhuanmsz);
            if(flag == -1){
//					如果出错则返回错误
                return -1;
            }
        }
        rsl.close();
        return 0;
    }
}
