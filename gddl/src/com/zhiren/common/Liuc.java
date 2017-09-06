package com.zhiren.common;

import java.sql.ResultSet;
import java.util.*;

import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.webservice.InterCom_dt;
import com.zhiren.webservice.InterFac_dt;

public class Liuc {

//		private static String TableName="";//数据表单表
//		private static String TableID="0";//数据表单ｉｄ
//		private static String LiucID="0";
////		private static String wodrwIds="";
////		private static String liuczIds="";
//		private static String renyxxb_id="";
//		private static String leibb_id="";
//		private static String xiaox="";//提交回退前的话写入日志
//		
//		
//		public static String getLiucID() {
//			return LiucID;
//		}
//		public static void setLiucID(String liucID) {
//			LiucID = liucID;
//		}
//		public static String getTableID() {
//			return TableID;
//		}
//		public static void setTableID(String tableID) {
//			TableID = tableID;
//		}
//		public  String getTableName() {
//			return TableName;
//		}
//		public static void setTableName(String tableName) {
//			TableName = tableName;
//		}


    private static String[] getXianjd(String TableName, long TableID, String caoz) {
        String[] yuancxjd = null;//远程衔接点
        //or 语句条件：适用于电厂端的配置。如果数据的电厂属于提交的分公司则有远程衔接点
        //缺陷：因忽略问题的普遍性，造成xianjb结构无法描述“是否存在衔接点条件”应该：电厂、状态（区别不同流程和数据类）、操作。
        //现在因为缺少电厂配置，造成很多问题：必须去查找该数据类的电厂，默认为diangcxxb_id字段。例如hetb要用hetslb。diancxxb_id造成缺乏普遍性
        //解决方案：1，彻底解决：加电厂字段2，为了不影响实施配置。特殊化处理，暂时采用2方案。

        JDBCcon con = new JDBCcon();
        String sql = "";
        if (TableName.equals("hetb")) {
            sql = "select xianjb.diancxxb_id_next,liucztb_id_next,zhuangt\n" +
                    "from xianjb\n" +
                    "where xianjb.diancxxb_id_next||xianjb.liucztb_id||caoz =\n" +
                    "(select max(hs.diancxxb_id) || max(liucztb_id)||'" + caoz + "'\n" +
                    "from hetb h,hetslb hs\n" +
                    "where   h.id=" + TableID + " and hs.hetb_id = h.id group by h.id)" +
                    "or (\n" +
                    "(select  max(hs.diancxxb_id) || max(liucztb_id)||'" + caoz + "'\n" +
                    "from hetb h,hetslb hs " +
                    "where  hs.hetb_id = h.id and hs.hetb_id=" + TableID + ") in(select  id||xianjb.liucztb_id || caoz from diancxxb where fuid= xianjb.diancxxb_id_next))";
        } else if (TableName.equals("hetys")) {
            sql = "select xianjb.diancxxb_id_next,liucztb_id_next,zhuangt\n" +
                    "from xianjb\n" +
                    "where xianjb.diancxxb_id_next||xianjb.liucztb_id||caoz =\n" +
                    "(select max(hs.shouhr_id) || max(liucztb_id)||'" + caoz + "'\n" +
                    "from hetys h,HETYSSHRB hs\n" +
                    "where   h.id=" + TableID + " and hs.hetysb_id = h.id group by h.id)" +
                    "or (\n" +
                    "(select max(shouhr_id) || max(liucztb_id)||'" + caoz + "'\n" +
                    "from hetys h,HETYSSHRB hs " +
                    "where   hs.hetysb_id = h.id and hs.hetysb_id=" + TableID + ") in(select  id||xianjb.liucztb_id || caoz from diancxxb where fuid= xianjb.diancxxb_id_next))";
            ;
        } else {//结算
            sql = "select xianjb.diancxxb_id_next,liucztb_id_next,zhuangt\n" +
                    "from xianjb\n" +
                    "where xianjb.diancxxb_id_next||xianjb.liucztb_id||caoz =(\n" +
                    "select diancxxb_id||liucztb_id||'" + caoz + "'\n" +
                    "from " + TableName + "\n" +
                    "where   " + TableName + ".id=" + TableID + " ) or (\n" +
                    "(select diancxxb_id||liucztb_id||'" + caoz + "'\n" +
                    "from " + TableName + "\n" +
                    "where   " + TableName + ".id=" + TableID +
                    ") in(select  id||xianjb.liucztb_id || caoz from diancxxb \n" +
                    " where (fuid= xianjb.diancxxb_id_next or \n" +
                    "(select fuid from diancxxb d\n" +
                    "                 where d.id = diancxxb.fuid and d.jib="
                    + SysConstant.JIB_DC + ")\n" +
                    "                 =xianjb.diancxxb_id_next)))";
        }
//			/*
//			*huochaoyuan
//			*2009-10-22针对本地修改的函数取数sql，使用于合同跨服务器审核流程时提交和回退
//			*/
//					    String sql = "select xianjb.diancxxb_id_next,liucztb_id_next,zhuangt\nfrom xianjb\nwhere xianjb.diancxxb_id_next||xianjb.liucztb_id||caoz =(\nselect diancxxb_id||liucztb_id||'" +
//					      caoz + "'\n" +
//					      "from " + TableName + "\n" +
//					      "where   " + TableName + ".id=" + TableID + " ) \n" +
//					      "or xianjb.diancxxb_id_next||xianjb.liucztb_id||caoz =\n" +
//					      "(select hs.diancxxb_id||liucztb_id||'" + caoz + "'\n" +
//					      "from hetb h,hetslb hs\n" +
//					      "where   h.id=" + TableID + " and hs.hetb_id=h.id)\n" +
//					      "or xianjb.diancxxb_id_next||xianjb.liucztb_id||caoz =\n" +
//					      "(select max(hs.shouhr_id) || max(liucztb_id)||'" + caoz + "'\n" +
//					      "from hetys h,HETYSSHRB hs\n" +
//					      "where   h.id=" + TableID + " and hs.hetysb_id = h.id group by h.id)\n" +
//					      "or xianjb.diancxxb_id_next||xianjb.liucztb_id||caoz =(\n" +
//					      "select d.fuid||liucztb_id||'" + caoz + "'\n" +
//					      "from " + TableName + ",diancxxb d\n" +
//					      "where   " + TableName + ".id=" + TableID + " and " + TableName + ".diancxxb_id=d.id) \n";
////			end
        ResultSet rs = con.getResultSet(sql);
        try {
            if (rs.next()) {
                yuancxjd = new String[3];
                yuancxjd[0] = rs.getString("diancxxb_id_next");
                yuancxjd[1] = rs.getString("liucztb_id_next");
                yuancxjd[2] = rs.getString("zhuangt");    //本地下一个流程（结算时使用）
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yuancxjd;
    }

    public static void tij(String TableName, long TableID, long renyxxb_id, String xiaox) {//提交
        //先判断是否存在衔接点
        JDBCcon con = new JDBCcon();
        if (TableName.equals("hetb")) {
            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetb_id = String.valueOf(TableID);
                String diancxxb_id = null;
                String[] resul = null;
                String hetbh = "";
                String sql = "select hetb.ID,hetb.FUID,DIANCXXB_ID,HETBH,to_char(QIANDRQ,'yyyy-mm-dd')QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                        "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,decode(gongysb.shangjgsbm,null,gongysb.bianm,gongysb.shangjgsbm) HETGYSBID,\n" +
                        " decode(gs.shangjgsbm,null,gs.bianm,gs.shangjgsbm) GONGYSB_ID,to_char(QISRQ,'yyyy-mm-dd')QISRQ,to_char(GUOQRQ,'yyyy-mm-dd')GUOQRQ,HETB_MB_ID,j.bianm JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,nvl(HETJJFSB_ID,0)HETJJFSB_ID,MEIKMCS\n" +
                        "from hetb,gongysb,gongysb gs,jihkjb j\n" +
                        "where hetb.jihkjb_id=j.id and hetb.HETGYSBID=gongysb.id and hetb.GONGYSB_ID=gs.id and hetb.id=" + hetb_id;
                ResultSetList rs1 = con.getResultSetList(sql);

                sql = "select hetslb.ID,pinzb.mingc PINZB_ID,YUNSFSB_ID,fz.mingc FAZ_ID,dz.mingc DAOZ_ID,DIANCXXB_ID,to_char(RIQ,'yyyy-mm-dd')riq,HETL,HETB_ID,hetslb.ZHUANGT\n" +
                        "from hetslb,pinzb,chezxxb fz,chezxxb dz\n" +
                        "where hetslb.faz_id=fz.id and hetslb.daoz_id=dz.id and hetslb.pinzb_id=pinzb.id and hetslb.hetb_id=" + hetb_id;
                ResultSetList rs2 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID\n" +
                        "from hetzlb\n" +
                        "where hetzlb.hetb_id=" + hetb_id;
                ResultSetList rs3 = con.getResultSetList(sql);

/*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
                        "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX\n" +
//end
                        "from hetjgb\n" +
                        "where hetjgb.hetb_id=" + hetb_id;
                ResultSetList rs4 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                        "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID\n" +
                        "from hetzkkb\n" +
                        "where hetzkkb.hetb_id=" + hetb_id;
                ResultSetList rs5 = con.getResultSetList(sql);

                sql = "select ID,WENZNR,HETB_ID\n" +
                        "from hetwzb\n" +
                        "where hetwzb.hetb_id=" + hetb_id;
                ResultSetList rs6 = con.getResultSetList(sql);

                sql = "select id,hetb_id,bianlmc,value from hetblb where hetb_id=" + hetb_id;
                ResultSetList rs7 = con.getResultSetList(sql);

                int len = rs1.getResultSetlist().size() + rs2.getResultSetlist().size() + rs3.getResultSetlist().size() + rs4.getResultSetlist().size()
                        + rs5.getResultSetlist().size() + rs6.getResultSetlist().size() + rs7.getResultSetlist().size() + 7;//6为五个分表的删除
                String[] sqls = new String[len];
                int j = 0;
                sqls[j] = "delete from hetb where id=" + hetb_id;
                j++;
                while (rs1.next()) {
                    diancxxb_id = rs1.getString("DIANCXXB_ID");
                    hetbh = rs1.getString("HETBH");
                    sqls[j] = "insert into hetb(ID,FUID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                            "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,\n" +
                            "GONGYSB_ID,QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS\n" +
                            ")values(\n" +
                            rs1.getString("ID") + "," +
                            rs1.getString("FUID") + "," +
                            diancxxb_id + ",'" +
                            hetbh + "',to_date('" +
                            rs1.getString("QIANDRQ") + "','yyyy-mm-dd'),'" +
                            rs1.getString("QIANDDD") + "','" +
                            rs1.getString("GONGFDWMC") + "','" +
                            rs1.getString("GONGFDWDZ") + "','" +
                            rs1.getString("GONGFDH") + "','" +
                            rs1.getString("GONGFFDDBR") + "','" +
                            rs1.getString("GONGFWTDLR") + "','" +
                            rs1.getString("GONGFDBGH") + "','" +
                            rs1.getString("GONGFKHYH") + "','" +
                            rs1.getString("GONGFZH") + "','" +
                            rs1.getString("GONGFYZBM") + "','" +
                            rs1.getString("GONGFSH") + "','" +
                            rs1.getString("XUFDWMC") + "','" +
                            rs1.getString("XUFDWDZ") + "','" +
                            rs1.getString("XUFFDDBR") + "','" +
                            rs1.getString("XUFWTDLR") + "','" +
                            rs1.getString("XUFDH") + "','" +
                            rs1.getString("XUFDBGH") + "','" +
                            rs1.getString("XUFKHYH") + "','" +
                            rs1.getString("XUFZH") + "','" +
                            rs1.getString("XUFYZBM") + "','" +
                            rs1.getString("XUFSH") + "',(select id from gongysb where bianm='" +
                            rs1.getString("HETGYSBID") + "'),(select id from gongysb where bianm='" +
                            rs1.getString("GONGYSB_ID") + "'),to_date('" +
                            rs1.getString("QISRQ") + "','yyyy-mm-dd'),to_date('" +
                            rs1.getString("GUOQRQ") + "','yyyy-mm-dd')," +
                            rs1.getString("HETB_MB_ID") + ",(select id from jihkjb where bianm='" +
                            rs1.getString("JIHKJB_ID") + "')," +
                            yuancljd[1] + "," +
                            rs1.getString("LIUCGZID") + "," +
                            rs1.getString("LEIB") + "," +
                            rs1.getString("HETJJFSB_ID") + ",'" +
                            rs1.getString("MEIKMCS") + "'" +
//							 rs1.getString("XIAF")+
                            ")";
                    j++;
                }
                rs1.close();

                sqls[j] = "delete from hetslb where hetb_id=" + hetb_id;
                j++;
                while (rs2.next()) {
                    sqls[j] = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT)values(\n" +
                            rs2.getString("ID") + ",(select id from pinzb where mingc='" +
                            rs2.getString("PINZB_ID") + "')," +
                            rs2.getString("YUNSFSB_ID") + ",(select id from chezxxb where mingc='" +
                            rs2.getString("FAZ_ID") + "'),(select id from chezxxb where mingc='" +
                            rs2.getString("DAOZ_ID") + "')," +
                            rs2.getString("DIANCXXB_ID") + ",to_date('" +
                            rs2.getString("RIQ") + "','yyyy-mm-dd')," +
                            rs2.getString("HETL") + "," +
                            rs2.getString("HETB_ID") + "," +
                            rs2.getString("ZHUANGT") +
                            ")";
                    j++;
                }
                rs2.close();

                sqls[j] = "delete from hetzlb where hetb_id=" + hetb_id;
                j++;
                while (rs3.next()) {
                    sqls[j] = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values(\n" +
                            rs3.getString("ID") + ",'" +
                            rs3.getString("ZHIBB_ID") + "','" +
                            rs3.getString("TIAOJB_ID") + "','" +
                            rs3.getString("SHANGX") + "','" +
                            rs3.getString("XIAX") + "','" +
                            rs3.getString("DANWB_ID") + "'," +
                            rs3.getString("HETB_ID") +
                            ")";
                    j++;
                }
                rs3.close();

                sqls[j] = "delete from hetjgb where hetb_id=" + hetb_id;
                j++;
                while (rs4.next()) {
                    sqls[j] = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
  /*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX)values(\n" +
//end
                            rs4.getString("ID") + ",'" +
                            rs4.getString("ZHIBB_ID") + "','" +
                            rs4.getString("TIAOJB_ID") + "','" +
                            rs4.getString("SHANGX") + "','" +
                            rs4.getString("XIAX") + "','" +
                            rs4.getString("DANWB_ID") + "','" +
                            rs4.getString("JIJ") + "','" +
                            rs4.getString("JIJDWID") + "','" +
                            rs4.getString("HETJSFSB_ID") + "','" +
                            rs4.getString("HETJSXSB_ID") + "','" +
                            rs4.getString("YUNJ") + "','" +
                            rs4.getString("YUNJDW_ID") + "','" +
                            rs4.getString("YINGDKF") + "','" +
                            rs4.getString("YUNSFSB_ID") + "','" +
                            rs4.getString("ZUIGMJ") + "','" +
                            rs4.getString("HETB_ID") + "','" +
/*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            rs4.getString("HETJJFSB_ID") + "','" +
                            rs4.getString("JIJLX") +
//end
                            "')";
                    j++;
                }
                rs4.close();

                sqls[j] = "delete from hetzkkb where hetb_id=" + hetb_id;
                j++;
                while (rs5.next()) {
                    sqls[j] = "insert into hetzkkb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                            "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID)values(\n" +
                            rs5.getString("ID") + ",'" +
                            rs5.getString("ZHIBB_ID") + "','" +
                            rs5.getString("TIAOJB_ID") + "','" +
                            rs5.getString("SHANGX") + "','" +
                            rs5.getString("XIAX") + "','" +
                            rs5.getString("DANWB_ID") + "','" +
                            rs5.getString("JIS") + "','" +
                            rs5.getString("JISDWID") + "','" +
                            rs5.getString("KOUJ") + "','" +
                            rs5.getString("KOUJDW") + "','" +
                            rs5.getString("ZENGFJ") + "','" +
                            rs5.getString("ZENGFJDW") + "','" +
                            rs5.getString("XIAOSCL") + "','" +
                            rs5.getString("JIZZKJ") + "','" +
                            rs5.getString("JIZZB") + "','" +
                            rs5.getString("CANZXM") + "','" +
                            rs5.getString("CANZXMDW") + "','" +
                            rs5.getString("CANZSX") + "','" +
                            rs5.getString("CANZXX") + "','" +
                            rs5.getString("HETJSXSB_ID") + "','" +
                            rs5.getString("YUNSFSB_ID") + "','" +
                            rs5.getString("BEIZ") + "'," +
                            rs5.getString("HETB_ID") +
                            ")";
                    j++;
                }
                rs5.close();

                sqls[j] = "delete from hetwzb where hetb_id=" + hetb_id;
                j++;
                while (rs6.next()) {
                    sqls[j] = "insert into hetwzb(ID,WENZNR,HETB_ID)values(\n" +
                            rs6.getString("ID") + ",'" +
                            rs6.getString("WENZNR") + "'," +
                            rs6.getString("HETB_ID") +
                            ")";
                    j++;
                }
                rs6.close();
//						合同变量
                sqls[j] = "delete from hetblb where hetb_id=" + hetb_id;
                j++;
                while (rs7.next()) {
                    sqls[j] = "insert into hetblb(ID,HETB_ID,BIANLMC,VALUE)values(\n" +
                            rs7.getString("ID") + "," +
                            rs7.getString("HETB_ID") + ",'" +
                            rs7.getString("BIANLMC") + "','" +
                            rs7.getString("VALUE") +
                            "')";
                    j++;
                }
                rs7.close();
//						合同变量

//						String strSuc="";//成功的合同编号
                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update  hetb set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    return;
                }
            }
        }
//			2009-5-18日 新增结算 上传、下发功能
        if (TableName.equals("jiesb")
                || TableName.equals("jiesyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                String sql = "";
                if (TableName.equals("jiesb")) {
                    sql =
                            "select j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukmk,\n" +
                                    "       j.hansmk,\n" +
                                    "       j.buhsmk,\n" +
                                    "       j.meikje,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       h.hetbh as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       j.jiesfrl,\n" +
                                    "       jhkj.bianm as jihkjb_id,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as  meikxxb_id,\n" +
                                    "       j.hetj,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.zhiljq,\n" +
                                    "       j.qiyf,\n" +
                                    "       j.jiesrl,\n" +
                                    "       j.jieslf,\n" +
                                    "       j.jiesrcrl,\n" +
                                    "       j.liucgzbid,\n" +
                                    "       j.ruzry,\n" +
                                    "       j.fuid,\n" +
                                    "       j.fengsjj,\n" +
                                    "       j.jiajqdj,\n" +
                                    "       j.jijlx,\n" +
                                    "		nvl(j.diancjsmkb_id,0) as diancjsmkb_id,\n" +
                                    "		nvl(j.yufkje,0) as yufkje,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.chaokdl,0) as chaokdl,\n" +
                                    "		nvl(j.chaokdlx,'') as chaokdlx, \n" +
                                    "		nvl(j.Yunfhsdj,0) as Yunfhsdj, \n" +
                                    "		nvl(j.hansyf,0) as hansyf, \n" +
                                    "		nvl(j.buhsyf,0) as buhsyf, \n" +
                                    "		nvl(j.yunfjsl,0) as yunfjsl \n" +
                                    "  from " + TableName + " j,gongysb g,hetb h,jihkjb jhkj,meikxxb m \n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.jihkjb_id = jhkj.id\n" +
                                    "        and j.hetb_id = h.id \n" +
                                    "        and j.meikxxb_id = m.id";

                } else if (TableName.equals("jiesyfb")) {
                    sql =
                            "select j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.guotyf,\n" +
                                    "       j.guotzf,\n" +
                                    "       j.kuangqyf,\n" +
                                    "       j.kuangqzf,\n" +
                                    "       j.jiskc,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukyf,\n" +
                                    "       j.hansyf,\n" +
                                    "       j.buhsyf,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       decode(j.jieslx,1,(select hetbh from hetb where id=j.hetb_id),(select hetbh from hetys where id=j.hetb_id)) as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       j.diancjsmkb_id as diancjsmkb_id,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       nvl(j.guotyfjf,0) as guotyfjf, \n" +
                                    "       nvl(j.guotzfjf,0) as guotzfjf,\n" +
                                    "       nvl(j.gongfsl,0) as gongfsl,\n" +
                                    "       nvl(j.yanssl,0) as yanssl,\n" +
                                    "       nvl(j.yingk,0) as yingk,\n" +
                                    "       j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       nvl(j.ditzf,0) as ditzf,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                    "       nvl(j.dityf,0) as dityf,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.ruzry,\n" +
                                    "       nvl(j.fuid,0) as fuid,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.diancjsyfb_id,0) as diancjsyfb_id	\n" +
                                    "  from " + TableName + " j,gongysb g,meikxxb m\n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.meikxxb_id = m.id";

                }
                ResultSetList rsl = con.getResultSetList(sql);

                sql = "select j.id, jiesdid, z.bianm as zhibb_id, hetbz,\n" +
                        "       	gongf, changf, jies, yingk, zhejbz, zhejje,\n" +
                        "       	zhuangt, yansbhb_id\n" +
                        "       from jieszbsjb j, zhibb z\n" +
                        "       where j.zhibb_id = z.id\n" +
                        " 			and j.jiesdid=" + TableID;
                ResultSetList rslzb = con.getResultSetList(sql);

                sql =
                        "select d.id,\n" +
                                "       d.xuh,\n" +
                                "       d.jiesdid,\n" +
                                "       z.bianm as zhibb_id,\n" +
                                "       d.hetbz,\n" +
                                "       d.gongf,\n" +
                                "       d.changf,\n" +
                                "       d.jies,\n" +
                                "       d.yingk,\n" +
                                "       d.zhejbz,\n" +
                                "       d.zhejje,\n" +
                                "       d.gongfsl,\n" +
                                "       d.yanssl,\n" +
                                "       d.jiessl,\n" +
                                "       d.koud,\n" +
                                "       d.kous,\n" +
                                "       d.kouz,\n" +
                                "       d.ches,\n" +
                                "       d.jingz,\n" +
                                "       d.koud_js,\n" +
                                "       d.yuns,\n" +
                                "       d.jiesslcy,\n" +
                                "       d.jiesdj,\n" +
                                "       d.jiakhj,\n" +
                                "       d.jiaksk,\n" +
                                "       d.jiashj,\n" +
                                "       d.biaomdj,\n" +
                                "       d.buhsbmdj,\n" +
                                "       d.leib,\n" +
                                "       d.hetj,\n" +
                                "       d.qnetar,\n" +
                                "       d.std,\n" +
                                "       d.stad,\n" +
                                "       d.star,\n" +
                                "       d.vdaf,\n" +
                                "       d.mt,\n" +
                                "       d.mad,\n" +
                                "       d.aad,\n" +
                                "       d.ad,\n" +
                                "       d.aar,\n" +
                                "       d.vad,\n" +
                                "       d.zongje,\n" +
                                "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                "       c.bianm as faz_id,\n" +
                                "       d.chaokdl,\n" +
                                "       d.jiajqdj\n" +
                                "  from danpcjsmxb d,zhibb z,jiesb j,meikxxb m,chezxxb c\n" +
                                "  where d.zhibb_id = z.id\n" +
                                "        and d.jiesdid = j.id\n" +
                                "        and d.meikxxb_id = m.id\n" +
                                "        and d.faz_id = c.id\n" +
                                "		 and d.jiesdid=" + TableID;
                ResultSetList rsldpc = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows() + rslzb.getRows() + rsldpc.getRows() + 3];    //加三行删除语句
                int j = 0;
                sqls[j] = "delete from " + TableName + " where id=" + TableID;

                while (rsl.next()) {
                    j++;

                    if (TableName.equals("jiesb")) {

                        sqls[j] = "insert into jiesb	\n"
                                + " (id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, \n"
                                + " buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, ranlbmjbr, ranlbmjbrq, beiz, jiesfrl, jihkjb_id, meikxxb_id, hetj, meikdwmc, zhiljq, qiyf, jiesrl, jieslf, jiesrcrl, liucgzbid, ruzry, fuid, fengsjj, jiajqdj, jijlx, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, diancjsmkb_id, yufkje, Yunfhsdj, hansyf, buhsyf, yunfjsl)	\n"
                                + " values	\n"
                                + " (" + rsl.getLong("id") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("bianm")
                                + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), '" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id")
                                + ", '" + rsl.getString("yunj") + "', " + rsl.getDouble("yingd") + ", " + rsl.getDouble("kuid") + ", '" + rsl.getString("faz")
                                + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq")) + "','yyyy-mm-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("meiz")
                                + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq")) + "','yyyy-mm-dd'),to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw")
                                + "', '" + rsl.getString("kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs") + "', '" + rsl.getString("duifdd")
                                + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy")
                                + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukmk") + ", " + rsl.getString("hansmk") + ", " + rsl.getString("buhsmk") + ", " + rsl.getString("meikje") + ", " + rsl.getString("shuik")
                                + ", " + rsl.getString("shuil") + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq")))
                                + "','yyyy-MM-dd'), (select id from hetb where hetbh = '" + rsl.getString("hetb_id") + "'), " + yuancljd[1] + ", " + rsl.getString("liucgzid") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq"))
                                + "','yyyy-MM-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("jiesfrl") + ", (select id from jihkjb where bianm = '" + rsl.getString("jihkjb_id") + "'), (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("hetj") + ", '" + rsl.getString("meikdwmc")
                                + "', '" + rsl.getString("zhiljq") + "', " + rsl.getString("qiyf") + ", " + rsl.getString("jiesrl") + ", " + rsl.getString("jieslf") + ", " + rsl.getString("jiesrcrl") + ", " + rsl.getString("liucgzbid") + ", '" + rsl.getString("ruzry")
                                + "', " + rsl.getLong("fuid") + ", " + rsl.getString("fengsjj") + ", " + rsl.getString("jiajqdj") + ", " + rsl.getString("jijlx") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getString("chaokdl") + ", '"
                                + rsl.getString("chaokdlx") + "'," + rsl.getString("diancjsmkb_id") + "," + rsl.getDouble("yufkje") + "," + rsl.getString("Yunfhsdj") + "," + rsl.getString("hansyf") + "," + rsl.getString("buhsyf") + "," + rsl.getString("yunfjsl") + " )\n";

                    } else if (TableName.equals("jiesyfb")) {

                        sqls[j] = "insert into jiesyfb	\n"
                                + " (gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, \n"
                                + " ruzrq, hetb_id, liucztb_id, liucgzid, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, guotyfjf, guotzfjf, gongfsl, yanssl, yingk, id, diancxxb_id, bianm, gongysb_id, ditzf, meikxxb_id, dityf, meikdwmc, ruzry, fuid, kuidjfyf, kuidjfzf, diancjsyfb_id )	\n"
                                + " values	\n"
                                + " ('" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id") + ", '" + rsl.getString("yunj") + "', " + rsl.getString("yingd") + ", " + rsl.getString("kuid") + ", '" + rsl.getString("faz") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("meiz") + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw") + "', '" + rsl.getString("v_kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs")
                                + "', '" + rsl.getString("duifdd") + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy") + ", " + rsl.getString("guotyf") + ", " + rsl.getString("guotzf")
                                + ", " + rsl.getString("kuangqyf") + ", " + rsl.getString("kuangqzf") + ", " + rsl.getString("jiskc") + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukyf") + ", " + rsl.getString("hansyf") + ", " + rsl.getString("buhsyf") + ", " + rsl.getString("shuik") + ", " + rsl.getString("shuil")
                                + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq"))) + "','yyyy-MM-dd'), " + rsl.getString("hetb_id") + ", " + yuancljd[1] + ", " + rsl.getString("liucgzid")
                                + ", " + rsl.getString("diancjsmkb_id") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq")) + "','yyyy_mm-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("guotyfjf") + ", " + rsl.getString("guotzfjf") + ", " + rsl.getString("gongfsl") + ", " + rsl.getString("yanssl")
                                + ", " + rsl.getString("yingk") + ", " + rsl.getString("id") + ", " + rsl.getString("diancxxb_id") + ", '" + rsl.getString("bianm") + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), " + rsl.getString("ditzf") + ", (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("dityf") + ", '" + rsl.getString("meikdwmc")
                                + "','" + rsl.getString("ruzry") + "', " + rsl.getString("fuid") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getLong("diancjsyfb_id") + ") \n";
                    }
                }
                rsl.close();

                sqls[++j] = "delete from jieszbsjb where jiesdid=" + TableID;
                while (rslzb.next()) {
                    j++;
                    sqls[j] = "insert into jieszbsjb	\n"
                            + " (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)	\n"
                            + " values	\n"
                            + " (" + rslzb.getLong("id") + ", " + rslzb.getLong("jiesdid") + ", (select id from zhibb where bianm='" + rslzb.getString("zhibb_id") + "'), '" + rslzb.getString("hetbz")
                            + "', " + rslzb.getString("gongf") + ", " + rslzb.getString("changf") + ", " + rslzb.getString("jies") + ", " + rslzb.getString("yingk")
                            + ", " + rslzb.getString("zhejbz") + ", " + rslzb.getString("zhejje") + ", " + rslzb.getString("zhuangt") + ", " + rslzb.getString("yansbhb_id") + ") \n";
                }
                rslzb.close();

                sqls[++j] = "delete from danpcjsmxb where jiesdid=" + TableID;
                while (rsldpc.next()) {
                    j++;
                    sqls[j] = "insert into danpcjsmxb	\n"
                            + " (id,xuh,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,gongfsl,yanssl,jiessl,koud,kous,kouz,ches,jingz,koud_js," +
                            "yuns,jiesslcy,jiesdj,jiakhj,jiaksk,jiashj,biaomdj,buhsbmdj,leib,hetj,qnetar,std,stad," +
                            "star,vdaf,mt,mad,aad,ad,aar,vad,zongje,meikxxb_id,faz_id,chaokdl,jiajqdj)	\n"
                            + " values	\n"
                            + " (" + rsldpc.getLong("id") + ", " + rsldpc.getString("xuh") + ", " + rsldpc.getString("jiesdid") + ", (select id from zhibb where bianm ='" + rsldpc.getString("zhibb_id") + "'), '" + rsldpc.getString("hetbz")
                            + "', " + rsldpc.getString("gongf") + ", " + rsldpc.getString("changf") + ", " + rsldpc.getString("jies") + ", " + rsldpc.getString("yingk")
                            + ", " + rsldpc.getString("zhejbz") + ", " + rsldpc.getString("zhejje") + ", " + rsldpc.getString("gongfsl")
                            + ", " + rsldpc.getString("yanssl") + ", " + rsldpc.getString("jiessl") + ", " + rsldpc.getString("koud")
                            + ", " + rsldpc.getString("kous") + ", " + rsldpc.getString("kouz") + ", " + rsldpc.getString("ches")
                            + ", " + rsldpc.getString("jingz") + ", " + rsldpc.getString("koud_js") + ", " + rsldpc.getString("yuns")
                            + ", " + rsldpc.getString("jiesslcy") + ", " + rsldpc.getString("jiesdj") + ", " + rsldpc.getString("jiakhj")
                            + ", " + rsldpc.getString("jiaksk") + ", " + rsldpc.getString("jiashj") + ", " + rsldpc.getString("biaomdj")
                            + ", " + rsldpc.getString("buhsbmdj") + ", " + rsldpc.getString("leib") + ", " + rsldpc.getString("hetj")
                            + ", " + rsldpc.getString("qnetar") + ", " + rsldpc.getString("std") + ", " + rsldpc.getString("stad")
                            + ", " + rsldpc.getString("star") + ", " + rsldpc.getString("vdaf") + ", " + rsldpc.getString("mt")
                            + ", " + rsldpc.getString("mad") + ", " + rsldpc.getString("aad") + ", " + rsldpc.getString("ad")
                            + ", " + rsldpc.getString("aar") + ", " + rsldpc.getString("vad") + ", " + rsldpc.getString("zongje")
                            + ", (select id from meikxxb where bianm ='" + rsldpc.getString("meikxxb_id") + "'), (select id from chezxxb where bianm ='" + rsldpc.getString("faz_id") + "'), " + rsldpc.getString("chaokdl")
                            + ", " + rsldpc.getString("jiajqdj") + ") \n";
                }
                rsldpc.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update " + TableName + " set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("上传结算单失败：" + resul[0]);
                    return;
                }
            }
        }

        if (TableName.equals("kuangfjsmkb")
                || TableName.equals("kuangfjsyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                String sql = "";
                if (TableName.equals("kuangfjsmkb")) {
                    sql =
                            "select j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukmk,\n" +
                                    "       j.hansmk,\n" +
                                    "       j.buhsmk,\n" +
                                    "       j.meikje,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       h.hetbh as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       j.jiesfrl,\n" +
                                    "       jhkj.bianm as jihkjb_id,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as  meikxxb_id,\n" +
                                    "       j.hetj,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.zhiljq,\n" +
                                    "       j.qiyf,\n" +
                                    "       j.jiesrl,\n" +
                                    "       j.jieslf,\n" +
                                    "       j.jiesrcrl,\n" +
                                    "       j.liucgzbid,\n" +
                                    "       j.ruzry,\n" +
                                    "       j.fuid,\n" +
                                    "       j.fengsjj,\n" +
                                    "       j.jiajqdj,\n" +
                                    "       j.jijlx,\n" +
                                    "		nvl(j.diancjsmkb_id,0) as diancjsmkb_id,\n" +
                                    "		nvl(j.yufkje,0) as yufkje,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.chaokdl,0) as chaokdl,\n" +
                                    "		nvl(j.chaokdlx,'') as chaokdlx, \n" +
                                    "		nvl(j.Yunfhsdj,0) as Yunfhsdj, \n" +
                                    "		nvl(j.hansyf,0) as hansyf, \n" +
                                    "		nvl(j.buhsyf,0) as buhsyf, \n" +
                                    "		nvl(j.yunfjsl,0) as yunfjsl, \n" +
                                    "       nvl(j.zhuangt,0) as zhuangt	\n" +
                                    "  from " + TableName + " j,gongysb g,hetb h,jihkjb jhkj,meikxxb m \n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.jihkjb_id = jhkj.id\n" +
                                    "        and j.hetb_id = h.id \n" +
                                    "        and j.meikxxb_id = m.id";

                } else if (TableName.equals("kuangfjsyfb")) {
                    sql =
                            "select j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.guotyf,\n" +
                                    "       j.guotzf,\n" +
                                    "       j.kuangqyf,\n" +
                                    "       j.kuangqzf,\n" +
                                    "       j.jiskc,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukyf,\n" +
                                    "       j.hansyf,\n" +
                                    "       j.buhsyf,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       decode(j.jieslx,1,(select hetbh from hetb where id=j.hetb_id),(select hetbh from hetys where id=j.hetb_id)) as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       nvl(j.kuangfjsmkb_id,0) as kuangfjsmkb_id,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       nvl(j.guotyfjf,0) as guotyfjf, \n" +
                                    "       nvl(j.guotzfjf,0) as guotzfjf,\n" +
                                    "       nvl(j.gongfsl,0) as gongfsl,\n" +
                                    "       nvl(j.yanssl,0) as yanssl,\n" +
                                    "       nvl(j.yingk,0) as yingk,\n" +
                                    "       j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       nvl(j.ditzf,0) as ditzf,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                    "       nvl(j.dityf,0) as dityf,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.ruzry,\n" +
                                    "       nvl(j.fuid,0) as fuid,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.diancjsyfb_id,0) as diancjsyfb_id,	\n" +
                                    "		nvl(j.zhuangt,0) as zhuangt \n" +
                                    "  from " + TableName + " j,gongysb g,meikxxb m\n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.meikxxb_id = m.id";

                }
                ResultSetList rsl = con.getResultSetList(sql);

                sql = "select j.id, jiesdid, z.bianm as zhibb_id, hetbz,\n" +
                        "       	gongf, changf, jies, yingk, zhejbz, zhejje,\n" +
                        "       	zhuangt, yansbhb_id\n" +
                        "       from jieszbsjb j, zhibb z\n" +
                        "       where j.zhibb_id = z.id\n" +
                        " 			and j.jiesdid=" + TableID;
                ResultSetList rslzb = con.getResultSetList(sql);

                sql =
                        "select d.id,\n" +
                                "       d.xuh,\n" +
                                "       d.jiesdid,\n" +
                                "       z.bianm as zhibb_id,\n" +
                                "       d.hetbz,\n" +
                                "       d.gongf,\n" +
                                "       d.changf,\n" +
                                "       d.jies,\n" +
                                "       d.yingk,\n" +
                                "       d.zhejbz,\n" +
                                "       d.zhejje,\n" +
                                "       d.gongfsl,\n" +
                                "       d.yanssl,\n" +
                                "       d.jiessl,\n" +
                                "       d.koud,\n" +
                                "       d.kous,\n" +
                                "       d.kouz,\n" +
                                "       d.ches,\n" +
                                "       d.jingz,\n" +
                                "       d.koud_js,\n" +
                                "       d.yuns,\n" +
                                "       d.jiesslcy,\n" +
                                "       d.jiesdj,\n" +
                                "       d.jiakhj,\n" +
                                "       d.jiaksk,\n" +
                                "       d.jiashj,\n" +
                                "       d.biaomdj,\n" +
                                "       d.buhsbmdj,\n" +
                                "       d.leib,\n" +
                                "       d.hetj,\n" +
                                "       d.qnetar,\n" +
                                "       d.std,\n" +
                                "       d.stad,\n" +
                                "       d.star,\n" +
                                "       d.vdaf,\n" +
                                "       d.mt,\n" +
                                "       d.mad,\n" +
                                "       d.aad,\n" +
                                "       d.ad,\n" +
                                "       d.aar,\n" +
                                "       d.vad,\n" +
                                "       d.zongje,\n" +
                                "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                "       c.bianm as faz_id,\n" +
                                "       d.chaokdl,\n" +
                                "       d.jiajqdj\n" +
                                "  from danpcjsmxb d,zhibb z," + TableName + " j,meikxxb m,chezxxb c\n" +
                                "  where d.zhibb_id = z.id\n" +
                                "        and d.jiesdid = j.id\n" +
                                "        and d.meikxxb_id = m.id\n" +
                                "        and d.faz_id = c.id\n" +
                                "		 and d.jiesdid=" + TableID;
                ResultSetList rsldpc = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows() + rslzb.getRows() + rsldpc.getRows() + 3];    //加三行删除语句
                int j = 0;
                sqls[j] = "delete from " + TableName + " where id=" + TableID;

                while (rsl.next()) {
                    j++;

                    if (TableName.equals("kuangfjsmkb")) {

                        sqls[j] = "insert into kuangfjsmkb	\n"
                                + " (id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, \n"
                                + " buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, ranlbmjbr, ranlbmjbrq, beiz, jiesfrl, jihkjb_id, meikxxb_id, hetj, meikdwmc, zhiljq, qiyf, jiesrl, jieslf, jiesrcrl, liucgzbid, ruzry, fuid, fengsjj, jiajqdj, jijlx, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, diancjsmkb_id, yufkje, Yunfhsdj, hansyf, \n"
                                + " buhsyf, yunfjsl, zhuangt)	\n"
                                + " values	\n"
                                + " (" + rsl.getLong("id") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("bianm")
                                + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), '" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id")
                                + ", '" + rsl.getString("yunj") + "', " + rsl.getDouble("yingd") + ", " + rsl.getDouble("kuid") + ", '" + rsl.getString("faz")
                                + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq")) + "','yyyy-mm-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("meiz")
                                + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq")) + "','yyyy-mm-dd'),to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw")
                                + "', '" + rsl.getString("kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs") + "', '" + rsl.getString("duifdd")
                                + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy")
                                + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukmk") + ", " + rsl.getString("hansmk") + ", " + rsl.getString("buhsmk") + ", " + rsl.getString("meikje") + ", " + rsl.getString("shuik")
                                + ", " + rsl.getString("shuil") + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq")))
                                + "','yyyy-MM-dd'), (select id from hetb where hetbh = '" + rsl.getString("hetb_id") + "'), " + yuancljd[1] + ", " + rsl.getString("liucgzid") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq"))
                                + "','yyyy-MM-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("jiesfrl") + ", (select id from jihkjb where bianm = '" + rsl.getString("jihkjb_id") + "'), (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("hetj") + ", '" + rsl.getString("meikdwmc")
                                + "', '" + rsl.getString("zhiljq") + "', " + rsl.getString("qiyf") + ", " + rsl.getString("jiesrl") + ", " + rsl.getString("jieslf") + ", " + rsl.getString("jiesrcrl") + ", " + rsl.getString("liucgzbid") + ", '" + rsl.getString("ruzry")
                                + "', " + rsl.getLong("fuid") + ", " + rsl.getString("fengsjj") + ", " + rsl.getString("jiajqdj") + ", " + rsl.getString("jijlx") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getString("chaokdl") + ", '"
                                + rsl.getString("chaokdlx") + "'," + rsl.getString("diancjsmkb_id") + "," + rsl.getDouble("yufkje") + "," + rsl.getString("Yunfhsdj") + "," + rsl.getString("hansyf") + "," + rsl.getString("buhsyf") + "," + rsl.getString("yunfjsl") + ",0 )\n";

                    } else if (TableName.equals("kuangfjsyfb")) {

                        sqls[j] = "insert into kuangfjsyfb	\n"
                                + " (gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, \n"
                                + " ruzrq, hetb_id, liucztb_id, liucgzid, kuangfjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, guotyfjf, guotzfjf, gongfsl, yanssl, yingk, id, diancxxb_id, bianm, gongysb_id, ditzf, meikxxb_id, dityf, meikdwmc, ruzry, fuid, kuidjfyf, kuidjfzf, diancjsyfb_id, zhuangt)	\n"
                                + " values	\n"
                                + " ('" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id") + ", '" + rsl.getString("yunj") + "', " + rsl.getString("yingd") + ", " + rsl.getString("kuid") + ", '" + rsl.getString("faz") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("meiz") + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw") + "', '" + rsl.getString("v_kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs")
                                + "', '" + rsl.getString("duifdd") + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy") + ", " + rsl.getString("guotyf") + ", " + rsl.getString("guotzf")
                                + ", " + rsl.getString("kuangqyf") + ", " + rsl.getString("kuangqzf") + ", " + rsl.getString("jiskc") + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukyf") + ", " + rsl.getString("hansyf") + ", " + rsl.getString("buhsyf") + ", " + rsl.getString("shuik") + ", " + rsl.getString("shuil")
                                + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq"))) + "','yyyy-MM-dd'), (select id from hetb where hetbh='" + rsl.getString("hetb_id") + "'), " + yuancljd[1] + ", " + rsl.getString("liucgzid")
                                + ", " + rsl.getString("kuangfjsmkb_id") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq")) + "','yyyy_mm-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("guotyfjf") + ", " + rsl.getString("guotzfjf") + ", " + rsl.getString("gongfsl") + ", " + rsl.getString("yanssl")
                                + ", " + rsl.getString("yingk") + ", " + rsl.getString("id") + ", " + rsl.getString("diancxxb_id") + ", '" + rsl.getString("bianm") + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), " + rsl.getString("ditzf") + ", (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("dityf") + ", '" + rsl.getString("meikdwmc")
                                + "','" + rsl.getString("ruzry") + "', " + rsl.getString("fuid") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getLong("diancjsyfb_id") + ", 0) \n";
                    }
                }
                rsl.close();

                sqls[++j] = "delete from jieszbsjb where jiesdid=" + TableID;
                while (rslzb.next()) {
                    j++;
                    sqls[j] = "insert into jieszbsjb	\n"
                            + " (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)	\n"
                            + " values	\n"
                            + " (" + rslzb.getLong("id") + ", " + rslzb.getLong("jiesdid") + ", (select id from zhibb where bianm='" + rslzb.getString("zhibb_id") + "'), '" + rslzb.getString("hetbz")
                            + "', " + rslzb.getString("gongf") + ", " + rslzb.getString("changf") + ", " + rslzb.getString("jies") + ", " + rslzb.getString("yingk")
                            + ", " + rslzb.getString("zhejbz") + ", " + rslzb.getString("zhejje") + ", " + rslzb.getString("zhuangt") + ", " + rslzb.getString("yansbhb_id") + ") \n";
                }
                rslzb.close();

                sqls[++j] = "delete from danpcjsmxb where jiesdid=" + TableID;
                while (rsldpc.next()) {
                    j++;
                    sqls[j] = "insert into danpcjsmxb	\n"
                            + " (id,xuh,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,gongfsl,yanssl,jiessl,koud,kous,kouz,ches,jingz,koud_js," +
                            "yuns,jiesslcy,jiesdj,jiakhj,jiaksk,jiashj,biaomdj,buhsbmdj,leib,hetj,qnetar,std,stad," +
                            "star,vdaf,mt,mad,aad,ad,aar,vad,zongje,meikxxb_id,faz_id,chaokdl,jiajqdj)	\n"
                            + " values	\n"
                            + " (" + rsldpc.getLong("id") + ", " + rsldpc.getString("xuh") + ", " + rsldpc.getString("jiesdid") + ", (select id from zhibb where bianm ='" + rsldpc.getString("zhibb_id") + "'), '" + rsldpc.getString("hetbz")
                            + "', " + rsldpc.getString("gongf") + ", " + rsldpc.getString("changf") + ", " + rsldpc.getString("jies") + ", " + rsldpc.getString("yingk")
                            + ", " + rsldpc.getString("zhejbz") + ", " + rsldpc.getString("zhejje") + ", " + rsldpc.getString("gongfsl")
                            + ", " + rsldpc.getString("yanssl") + ", " + rsldpc.getString("jiessl") + ", " + rsldpc.getString("koud")
                            + ", " + rsldpc.getString("kous") + ", " + rsldpc.getString("kouz") + ", " + rsldpc.getString("ches")
                            + ", " + rsldpc.getString("jingz") + ", " + rsldpc.getString("koud_js") + ", " + rsldpc.getString("yuns")
                            + ", " + rsldpc.getString("jiesslcy") + ", " + rsldpc.getString("jiesdj") + ", " + rsldpc.getString("jiakhj")
                            + ", " + rsldpc.getString("jiaksk") + ", " + rsldpc.getString("jiashj") + ", " + rsldpc.getString("biaomdj")
                            + ", " + rsldpc.getString("buhsbmdj") + ", " + rsldpc.getString("leib") + ", " + rsldpc.getString("hetj")
                            + ", " + rsldpc.getString("qnetar") + ", " + rsldpc.getString("std") + ", " + rsldpc.getString("stad")
                            + ", " + rsldpc.getString("star") + ", " + rsldpc.getString("vdaf") + ", " + rsldpc.getString("mt")
                            + ", " + rsldpc.getString("mad") + ", " + rsldpc.getString("aad") + ", " + rsldpc.getString("ad")
                            + ", " + rsldpc.getString("aar") + ", " + rsldpc.getString("vad") + ", " + rsldpc.getString("zongje")
                            + ", (select id from meikxxb where bianm ='" + rsldpc.getString("meikxxb_id") + "'), (select id from chezxxb where bianm ='" + rsldpc.getString("faz_id") + "'), " + rsldpc.getString("chaokdl")
                            + ", " + rsldpc.getString("jiajqdj") + ") \n";
                }
                rsldpc.close();

                String[] resul = null;
                InterCom_dt Shangb = new InterCom_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update " + TableName + " set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("上传结算单失败：" + resul[0]);
                    return;
                }
            }
        }

//				注 : 目前 只支持 下发功能
        if (TableName.equals("diancjsmkb")
                || TableName.equals("diancjsyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据

//						yuancxjd[0]=rs.getString("diancxxb_id_next");
//						yuancxjd[1]=rs.getString("liucztb_id_next");
//						yuancxjd[2]=rs.getString("zhuangt");

                long id = 0;
                try {
                    if (TableName.equals("diancjsmkb")) {
                        id = MainGlobal.getTableId("jiesb", "diancjsmkb_id"
                                , TableID + "");
                    } else {
                        id = MainGlobal.getTableId("jiesyfb", "diancjsyfb_id"
                                , TableID + "");
                    }

                } catch (Exception e) {
                    // TODO 自动生成 catch 块
                    e.printStackTrace();
                }

                String sql = "";
                if (TableName.equals("diancjsmkb")) {
                    sql = "select * from jiesb where id=" + id;
                } else {
                    sql = "select * from jiesyfb where id=" + id;
                }

                ResultSetList rsl = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows()];
                int j = -1;

                while (rsl.next()) {
                    j++;
//							更新厂级系统的jiesb/jiesyfb中的liucztb_id
                    if (TableName.equals("diancjsmkb")) {

                        sqls[j] = " update jiesb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");

                    } else if (TableName.equals("diancjsyfb")) {

                        sqls[j] = " update jiesyfb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");
                    }
                }
                rsl.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2
                    //更新本地数据库diancjsmkb/diancjsyfb 的 liucztb_id 为下一个流程动作
                    StringBuffer bf = new StringBuffer();
                    bf.append(" update " + TableName + " set liucztb_id=" + yuancljd[2] + " where id=" + TableID + "\n");
                    con.getUpdate(bf.toString());
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    //		return false;
                }

            }
        }
//				2009-6-17日增加hetys的上传下发功能
        if (TableName.equals("hetys")) {
            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetysb_id = String.valueOf(TableID);
                String[] resul = null;
                ResultSetList rsl = null;
                String[] sqls = new String[1];
                StringBuffer sbExeSql = new StringBuffer("begin	\n");
//							主合同信息
                String sql =
                        "select ht.id, ht.mingc, fuid, ht.diancxxb_id, hetbh, to_char(qiandrq,'yyyy-MM-dd') as qiandrq, qianddd,\n" +
                                "       y.bianm as yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh,\n" +
                                "       gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh,\n" +
                                "       xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, to_char(qisrq,'yyyy-MM-dd') as qisrq,\n" +
                                "       to_char(guoqrq,'yyyy-MM-dd') as guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid\n" +
                                "       from hetys ht,yunsdwb y\n" +
                                "       where ht.yunsdwb_id = y.id\n" +
                                "             and ht.id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetys where id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetys\n" +
                            "  (id, mingc, fuid, diancxxb_id, hetbh, qiandrq, qianddd, yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh, gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh, xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, qisrq, guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid)\n" +
                            "	values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("mingc") + "', " + rsl.getLong("fuid") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("hetbh") + "', to_date('" + rsl.getString("qiandrq") + "','yyyy-MM-dd'), '" + rsl.getString("qianddd") + "', (select id from yunsdwb where bianm='"
                            + rsl.getString("yunsdwb_id") + "'), '" + rsl.getString("gongfdwmc") + "', '" + rsl.getString("gongfdwdz") + "', '" + rsl.getString("gongfdh") + "', '" + rsl.getString("gongffddbr") + "', '" + rsl.getString("gongfwtdlr") + "', '" + rsl.getString("gongfdbgh") + "', '" + rsl.getString("gongfkhyh")
                            + "', '" + rsl.getString("gongfzh") + "', '" + rsl.getString("gongfyzbm") + "', '" + rsl.getString("gongfsh") + "', '" + rsl.getString("xufdwmc") + "', '" + rsl.getString("xufdwdz") + "', '" + rsl.getString("xuffddbr") + "', '" + rsl.getString("xufwtdlr") + "', '" + rsl.getString("xufdh")
                            + "', '" + rsl.getString("xufdbgh") + "', '" + rsl.getString("xufkhyh") + "', '" + rsl.getString("xufzh") + "', '" + rsl.getString("xufyzbm") + "', '" + rsl.getString("xufsh") + "', to_date('" + rsl.getString("qisrq") + "','yyyy-MM-dd'), to_date('" + rsl.getString("guoqrq")
                            + "','yyyy-MM-dd'), " + rsl.getLong("hetys_mb_id") + ", '" + rsl.getString("meikmcs") + "', " + yuancljd[1] + ", " + rsl.getLong("liucgzid") + ");\n"
                    );
                }
//							价格信息
                sql =
                        "select jg.id, hetys_id, m.bianm as meikxxb_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id,\n" +
                                "       shangx, xiax, dw.bianm as danwb_id, yunja, yjdw.bianm as yunjdw_id\n" +
                                "       from hetysjgb jg, meikxxb m, zhibb zb, tiaojb tj, danwb dw, danwb yjdw\n" +
/*
*huochaoyuan
*2009-10-22增加条件的右连接
*/
                                "       where jg.meikxxb_id = m.id(+) and jg.zhibb_id = zb.id(+) and jg.tiaojb_id = tj.id(+)\n" +
                                "             and jg.danwb_id = dw.id(+) and jg.yunjdw_id = yjdw.id(+)\n" +
//
                                "             and jg.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetysjgb where hetys_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetysjgb\n" +
                                    "	(id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id)\n" +
                                    "	values\n" +
                                    "	(" + rsl.getLong("id") + ", " + rsl.getLong("hetys_id") + ", (select id from meikxxb where bianm='"
                                    + rsl.getString("meikxxb_id") + "'), (select id from zhibb where bianm='"
                                    + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='"
                                    + rsl.getString("tiaojb_id") + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax")
/*
*huochaoyuan
*2009-10-22添加条件zhibb_id!=0，区分danwb中有编码相同的项；
*/
                                    + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "' and zhibb_id!=0), " + rsl.getString("yunja")
                                    + ", (select id from danwb where bianm='" + rsl.getString("yunjdw_id") + "' and zhibb_id!=0));\n"
//end
                    );
                }
/*
*huochaoyuan
*2009-10-22运输合同添加收货人，但上传/下发未做处理，故添加如下语句；
*/
                sql = "select s.id,s.hetysb_id,s.shouhr_id from hetysshrb s  where s.hetysb_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);
                sbExeSql.append("	delete from hetysshrb where hetysb_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("insert into hetysshrb\n" +
                            "  (id, hetysb_id, shouhr_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", " + rsl.getLong("hetysb_id") + ", " + rsl.getLong("shouhr_id") + ");\n"
                    );
                }
//							end
//							增扣款
                sql = "select zkk.id, jis, jsdw.bianm as jisdwid, kouj, kjdw.bianm as koujdw, zengfj, zfjdw.bianm as zengfjdw,\n" +
                        "       xiaoscl, jizzkj, jizzb, czxm.bianm as canzxm, czxmdw.bianm as canzxmdw, canzsx, canzxx, jsxs.bianm as hetjsxsb_id,\n" +
                        "       ysfs.mingc as yunsfsb_id, zkk.beiz, hetys_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id, shangx, xiax,\n" +
                        "       dw.bianm as danwb_id\n" +
                        "       from hetyszkkb zkk, danwb jsdw, danwb kjdw, danwb zfjdw, zhibb jzzb, zhibb czxm, danwb czxmdw, hetjsxsb jsxs,\n" +
                        "            yunsfsb ysfs, zhibb zb, tiaojb tj,danwb dw\n" +
                        "       where zkk.jisdwid = jsdw.id(+) and zkk.koujdw = kjdw.id (+) and zkk.zengfjdw = zfjdw.id(+)\n" +
                        "             and zkk.jizzb = jzzb.id(+) and zkk.canzxm = czxm.id(+) and zkk.canzxmdw = czxmdw.id(+)\n" +
                        "             and zkk.hetjsxsb_id = jsxs.id and zkk.yunsfsb_id = ysfs.id and zkk.zhibb_id = zb.id\n" +
                        "             and zkk.tiaojb_id = tj.id(+) and zkk.danwb_id = dw.id (+) and zkk.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyszkkb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyszkkb\n" +
                                    "  (id, jis, jisdwid, kouj, koujdw, zengfj, zengfjdw, xiaoscl, jizzkj, jizzb, canzxm, canzxmdw, canzsx, canzxx, hetjsxsb_id, yunsfsb_id, beiz, hetys_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id)\n" +
                                    "values\n" +
                                    "  (" + rsl.getLong("id") + ", " + rsl.getString("jis") + ", (select id from danwb where bianm='" + rsl.getString("jisdwid") + "'), " + rsl.getString("kouj") + ", (select id from danwb where bianm='"
                                    + rsl.getString("koujdw") + "'), " + rsl.getString("zengfj") + ", (select id from danwb where bianm='" + rsl.getString("zengfjdw") + "'), " + rsl.getString("xiaoscl") + ", " + rsl.getString("jizzkj")
                                    + ", " + rsl.getString("jizzb") + ", (select id from zhibb where bianm='" + rsl.getString("canzxm") + "'), (select id from danwb where bianm='" + rsl.getString("canzxmdw") + "'), " + rsl.getString("canzsx")
                                    + ", " + rsl.getString("canzxx") + ", (select id from hetjsxsb where bianm='" + rsl.getString("hetjsxsb_id") + "'), (select id from yunsfsb where mingc='" + rsl.getString("yunsfsb_i")
                                    + "'), '" + rsl.getString("beiz") + "', " + rsl.getLong("hetys_id") + ", (select id from zhibb where bianm='" + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='" + rsl.getString("tiaojb_id")
/*
*huochaoyuan
*2009-10-22添加条件zhibb_id!=0，区分danwb中有编码相同的项；
*/
                                    + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax") + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "'  and zhibb_id!=0));\n"
//end
                    );
                }
//							文字
                sql = "select id, wenznr, hetys_id from hetyswzb where hetys_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyswzb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyswzb(id, wenznr, hetys_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("wenznr") + "', " + rsl.getLong("hetys_id") + ");\n"
                    );
                }

                if (rsl != null) {

                    rsl.close();
                }

                sbExeSql.append("end;");
                sqls[0] = sbExeSql.toString();

                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update hetys set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//								strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("上传失败：" + resul[0]);
                    return;
                }
            }
        }

        //
        String sql = "";
        long dangqdz = -1; //表单当前状态
        long liuczthjid = 0;
        long liucdzb_id = 0;
        String qianqmc = "";
        String houjmc = "";
        String caoz = "";
        long leibztb_id = -1;
        try {
            sql = "select liucztb_id\n" +//获得当前状态
                    "from " + TableName + "\n" +
                    "where id=" + TableID;
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {
                dangqdz = rs.getLong(1);
            }
            sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n" +
                    "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n" +
                    "where liucdzb.liucztqqid=liucztb1.id\n" +
                    " and liucdzb.liuczthjid=liucztb2.id\n" +
                    " and liucztb1.leibztb_id=leibztb1.id\n" +
                    " and liucztb2.leibztb_id=leibztb2.id\n" +
                    " and liucdzb.liucztqqid=" + dangqdz +
                    "  and liucztb1.xuh<liucztb2.xuh";
            ResultSet rs1 = con.getResultSet(sql);
            if (rs1.next()) {
                liuczthjid = rs1.getLong("liuczthjid");
                liucdzb_id = rs1.getLong("id");
                qianqmc = rs1.getString("qianqmc");
                houjmc = rs1.getString("houjmc");
                caoz = rs1.getString("caoz");
                leibztb_id = rs1.getLong("leibztb_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //日志
//			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij," +
                "miaos)values(xl_xul_id.nextval,"
                + TableID
                + ","
                + liucdzb_id
                + ",'"
                + qianqmc
                + "','"
                + houjmc
                + "','"
                + caoz
                + "',(select quanc from renyxxb where id="
                + renyxxb_id
                + "),sysdate"
                + ",'"
                + xiaox
                + "')";
        con.getInsert(sql);
        if (leibztb_id == 0 || leibztb_id == 1) {//如果后继类别状态为0或１即为回退
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + leibztb_id
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        } else {//如果后继为
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + liuczthjid
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        }

        con.getUpdate(sql);
        con.Close();
    }

    public static void tij(String TableName, long TableID, long renyxxb_id, String xiaox, long liucb_id) {//提交
        JDBCcon con = new JDBCcon();
        String sql = "";
//			long dangqdz=-1; //表单当前状态
        long liuczthjid = 0;
        long liucdzb_id = 0;
        String qianqmc = "";
        String houjmc = "";
        String caoz = "";
        long leibztb_id = -1;
        try {
//			sql="select liucztb_id\n" +//获得当前状态
//			"from "+TableName+"\n" +
//			"where id="+TableID;
//			ResultSet rs=con.getResultSet(sql);
//			if(rs.next()){
//				dangqdz=rs.getLong(1);
//			}
            //为初始提交，这时要要找他的后继则要有流程ｉｄ和leibztb.id=0来作为条件查找动作表
            sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n" +
                    "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n" +
                    "where liucdzb.liucztqqid=liucztb1.id\n" +
                    " and liucdzb.liuczthjid=liucztb2.id\n" +
                    " and liucztb1.leibztb_id=leibztb1.id\n" +
                    " and liucztb2.leibztb_id=leibztb2.id\n" +
                    " and leibztb1.id=0\n" +
                    " and liucztb1.liucb_id=" + liucb_id +
                    "  and liucztb1.xuh<liucztb2.xuh";
            ResultSet rs1 = con.getResultSet(sql);
            if (rs1.next()) {
                liuczthjid = rs1.getLong("liuczthjid");
                liucdzb_id = rs1.getLong("id");
                qianqmc = rs1.getString("qianqmc");
                houjmc = rs1.getString("houjmc");
                caoz = rs1.getString("caoz");
                leibztb_id = rs1.getLong("leibztb_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //日志
//			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij," +
                "miaos)values(xl_xul_id.nextval,"
                + TableID
                + ","
                + liucdzb_id
                + ",'"
                + qianqmc
                + "','"
                + houjmc
                + "','"
                + caoz
                + "',(select quanc from renyxxb where id="
                + renyxxb_id
                + "),sysdate"
                + ",'"
                + xiaox
                + "')";
        con.getInsert(sql);
        if (leibztb_id == 0 || leibztb_id == 1) {//如果后继类别状态为0或１即为回退
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + leibztb_id
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        } else {//如果后继为
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + liuczthjid
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        }
        con.getUpdate(sql);
        con.Close();
    }

    public static boolean huit(String TableName, long TableID, long renyxxb_id, String xiaox) {//回退
//			先判断是否存在衔接点
        JDBCcon con = new JDBCcon();
        if (TableName.equals("hetb")) {
            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetb_id = String.valueOf(TableID);
                String diancxxb_id = null;
                String[] resul = null;
                String hetbh = "";
                String sql = "select hetb.ID,hetb.FUID,DIANCXXB_ID,HETBH,to_char(QIANDRQ,'yyyy-mm-dd')QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                        "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,gongysb.bianm HETGYSBID,\n" +
                        " gs.bianm GONGYSB_ID,to_char(QISRQ,'yyyy-mm-dd')QISRQ,to_char(GUOQRQ,'yyyy-mm-dd')GUOQRQ,HETB_MB_ID,j.bianm JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,nvl(HETJJFSB_ID,0)HETJJFSB_ID,MEIKMCS\n" +
                        "from hetb,gongysb,gongysb gs,jihkjb j\n" +
                        "where hetb.jihkjb_id=j.id and hetb.HETGYSBID=gongysb.id and hetb.GONGYSB_ID=gs.id and hetb.id=" + hetb_id;
                ResultSetList rs1 = con.getResultSetList(sql);

                sql = "select hetslb.ID,pinzb.mingc PINZB_ID,YUNSFSB_ID,fz.mingc FAZ_ID,dz.mingc DAOZ_ID,DIANCXXB_ID,to_char(RIQ,'yyyy-mm-dd')riq,HETL,HETB_ID,hetslb.ZHUANGT\n" +
                        "from hetslb,pinzb,chezxxb fz,chezxxb dz\n" +
                        "where hetslb.faz_id=fz.id and hetslb.daoz_id=dz.id and hetslb.pinzb_id=pinzb.id and hetslb.hetb_id=" + hetb_id;
                ResultSetList rs2 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID\n" +
                        "from hetzlb\n" +
                        "where hetzlb.hetb_id=" + hetb_id;
                ResultSetList rs3 = con.getResultSetList(sql);

 /*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
                        "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX\n" +
//end
                        "from hetjgb\n" +
                        "where hetjgb.hetb_id=" + hetb_id;
                ResultSetList rs4 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                        "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID\n" +
                        "from hetzkkb\n" +
                        "where hetzkkb.hetb_id=" + hetb_id;
                ResultSetList rs5 = con.getResultSetList(sql);

                sql = "select ID,WENZNR,HETB_ID\n" +
                        "from hetwzb\n" +
                        "where hetwzb.hetb_id=" + hetb_id;
                ResultSetList rs6 = con.getResultSetList(sql);
                int len = rs1.getResultSetlist().size() + rs2.getResultSetlist().size() + rs3.getResultSetlist().size() + rs4.getResultSetlist().size()
                        + rs5.getResultSetlist().size() + rs6.getResultSetlist().size() + 6;//6为五个分表的删除
                String[] sqls = new String[len];
                int j = 0;
                sqls[j] = "delete from hetb where id=" + hetb_id;
                j++;
                while (rs1.next()) {
                    diancxxb_id = rs1.getString("DIANCXXB_ID");
                    hetbh = rs1.getString("HETBH");
                    sqls[j] = "insert into hetb(ID,FUID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                            "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,\n" +
                            "GONGYSB_ID,QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS\n" +
                            ")values(\n" +
                            rs1.getString("ID") + "," +
                            rs1.getString("FUID") + "," +
                            diancxxb_id + ",'" +
                            hetbh + "',to_date('" +
                            rs1.getString("QIANDRQ") + "','yyyy-mm-dd'),'" +
                            rs1.getString("QIANDDD") + "','" +
                            rs1.getString("GONGFDWMC") + "','" +
                            rs1.getString("GONGFDWDZ") + "','" +
                            rs1.getString("GONGFDH") + "','" +
                            rs1.getString("GONGFFDDBR") + "','" +
                            rs1.getString("GONGFWTDLR") + "','" +
                            rs1.getString("GONGFDBGH") + "','" +
                            rs1.getString("GONGFKHYH") + "','" +
                            rs1.getString("GONGFZH") + "','" +
                            rs1.getString("GONGFYZBM") + "','" +
                            rs1.getString("GONGFSH") + "','" +
                            rs1.getString("XUFDWMC") + "','" +
                            rs1.getString("XUFDWDZ") + "','" +
                            rs1.getString("XUFFDDBR") + "','" +
                            rs1.getString("XUFWTDLR") + "','" +
                            rs1.getString("XUFDH") + "','" +
                            rs1.getString("XUFDBGH") + "','" +
                            rs1.getString("XUFKHYH") + "','" +
                            rs1.getString("XUFZH") + "','" +
                            rs1.getString("XUFYZBM") + "','" +
                            rs1.getString("XUFSH") + "',(select id from gongysb where bianm='" +
                            rs1.getString("HETGYSBID") + "' or shangjgsbm='" + rs1.getString("HETGYSBID") + "'),(select id from gongysb where bianm='" +
                            rs1.getString("GONGYSB_ID") + "' or shangjgsbm='" + rs1.getString("GONGYSB_ID") + "'),to_date('" +
                            rs1.getString("QISRQ") + "','yyyy-mm-dd'),to_date('" +
                            rs1.getString("GUOQRQ") + "','yyyy-mm-dd')," +
                            rs1.getString("HETB_MB_ID") + ",(select id from jihkjb where bianm='" +
                            rs1.getString("JIHKJB_ID") + "')," +
                            yuancljd[1] + "," +
                            rs1.getString("LIUCGZID") + "," +
                            rs1.getString("LEIB") + "," +
                            rs1.getString("HETJJFSB_ID") + ",'" +
                            rs1.getString("MEIKMCS") + "'" +
//							 rs1.getString("XIAF")+
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetslb where hetb_id=" + hetb_id;
                j++;
                while (rs2.next()) {
                    sqls[j] = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT)values(\n" +
                            rs2.getString("ID") + ",(select id from pinzb where mingc='" +
                            rs2.getString("PINZB_ID") + "')," +
                            rs2.getString("YUNSFSB_ID") + ",(select id from chezxxb where mingc='" +
                            rs2.getString("FAZ_ID") + "'),(select id from chezxxb where mingc='" +
                            rs2.getString("DAOZ_ID") + "')," +
                            rs2.getString("DIANCXXB_ID") + ",to_date('" +
                            rs2.getString("RIQ") + "','yyyy-mm-dd')," +
                            rs2.getString("HETL") + "," +
                            rs2.getString("HETB_ID") + "," +
                            rs2.getString("ZHUANGT") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetzlb where hetb_id=" + hetb_id;
                j++;
                while (rs3.next()) {
                    sqls[j] = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values(\n" +
                            rs3.getString("ID") + ",'" +
                            rs3.getString("ZHIBB_ID") + "','" +
                            rs3.getString("TIAOJB_ID") + "','" +
                            rs3.getString("SHANGX") + "','" +
                            rs3.getString("XIAX") + "','" +
                            rs3.getString("DANWB_ID") + "'," +
                            rs3.getString("HETB_ID") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetjgb where hetb_id=" + hetb_id;
                j++;
                while (rs4.next()) {
                    sqls[j] = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
/*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX)values(\n" +
//	end
                            rs4.getString("ID") + ",'" +
                            rs4.getString("ZHIBB_ID") + "','" +
                            rs4.getString("TIAOJB_ID") + "','" +
                            rs4.getString("SHANGX") + "','" +
                            rs4.getString("XIAX") + "','" +
                            rs4.getString("DANWB_ID") + "','" +
                            rs4.getString("JIJ") + "','" +
                            rs4.getString("JIJDWID") + "','" +
                            rs4.getString("HETJSFSB_ID") + "','" +
                            rs4.getString("HETJSXSB_ID") + "','" +
                            rs4.getString("YUNJ") + "','" +
                            rs4.getString("YUNJDW_ID") + "','" +
                            rs4.getString("YINGDKF") + "','" +
                            rs4.getString("YUNSFSB_ID") + "','" +
                            rs4.getString("ZUIGMJ") + "','" +
                            rs4.getString("HETB_ID") + "','" +
 /*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            rs4.getString("HETJJFSB_ID") + "','" +
                            rs4.getString("JIJLX") +
//	end
                            "')";
                    j++;
                }
                sqls[j] = "delete from hetzkkb where hetb_id=" + hetb_id;
                j++;
                while (rs5.next()) {
                    sqls[j] = "insert into hetzkkb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                            "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID)values(\n" +
                            rs5.getString("ID") + ",'" +
                            rs5.getString("ZHIBB_ID") + "','" +
                            rs5.getString("TIAOJB_ID") + "','" +
                            rs5.getString("SHANGX") + "','" +
                            rs5.getString("XIAX") + "','" +
                            rs5.getString("DANWB_ID") + "','" +
                            rs5.getString("JIS") + "','" +
                            rs5.getString("JISDWID") + "','" +
                            rs5.getString("KOUJ") + "','" +
                            rs5.getString("KOUJDW") + "','" +
                            rs5.getString("ZENGFJ") + "','" +
                            rs5.getString("ZENGFJDW") + "','" +
                            rs5.getString("XIAOSCL") + "','" +
                            rs5.getString("JIZZKJ") + "','" +
                            rs5.getString("JIZZB") + "','" +
                            rs5.getString("CANZXM") + "','" +
                            rs5.getString("CANZXMDW") + "','" +
                            rs5.getString("CANZSX") + "','" +
                            rs5.getString("CANZXX") + "','" +
                            rs5.getString("HETJSXSB_ID") + "','" +
                            rs5.getString("YUNSFSB_ID") + "','" +
                            rs5.getString("BEIZ") + "'," +
                            rs5.getString("HETB_ID") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetwzb where hetb_id=" + hetb_id;
                j++;
                while (rs6.next()) {
                    sqls[j] = "insert into hetwzb(ID,WENZNR,HETB_ID)values(\n" +
                            rs6.getString("ID") + ",'" +
                            rs6.getString("WENZNR") + "'," +
                            rs6.getString("HETB_ID") +
                            ")";
                    j++;
                }
//						String strSuc="";//成功的合同编号
                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update  hetb set liucztb_id=" + yuancljd[2] + "  where id=" + TableID;
                    con.getUpdate(sql1);
                    return true;
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    return false;
                }
            }
        }

        if (TableName.equals("jiesb")
                || TableName.equals("jiesyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据

                String sql = "select * from " + TableName + " where id=" + TableID;
                ResultSetList rsl = con.getResultSetList(sql);

//					sql="select * from jieszbsjb where jiesdid="+TableID;
//					ResultSetList rslzb=con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows()];
                int j = -1;
//					更新厂级的结算单流程状态
                while (rsl.next()) {
                    j++;

                    if (TableName.equals("jiesb")) {

                        sqls[j] = " update jiesb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");

                    } else if (TableName.equals("jiesyfb")) {

                        sqls[j] = " update jiesyfb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");
                    }
                }
                rsl.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    //	String sql1="update "+TableName+" set liucztb_id="+yuancljd[2]+" where id="+TableID;
                    //	con.getUpdate(sql1);
//						操作本层数据
                    StringBuffer bf = new StringBuffer();
                    bf.append(" begin \n");
                    if (TableName.equals("jiesb")) {

                        bf.append(" delete from diancjsmkb where id in (select nvl(diancjsmkb_id,0) as id from jiesb where id=" + TableID + ");\n");
                        bf.append(" delete from diancjsmkb where id in (select nvl(diancjsmkb_id,0) as id from jiesb where fuid=" + TableID + ");\n");
                        bf.append(" delete from danpcjsmxb where jiesdid=" + TableID + ";	\n");
                        bf.append(" delete from danpcjsmxb where jiesdid in (select id from jiesb where fuid=" + TableID + ");	\n");

                    } else {

                        bf.append(" delete from diancjsyfb where id in (select nvl(diancjsyfb_id,0) as id from jiesyfb where id=" + TableID + ");\n");
                        bf.append(" delete from diancjsyfb where id in (select nvl(diancjsyfb_id,0) as id from jiesyfb where fuid=" + TableID + ");\n");
                    }

                    bf.append(" delete from " + TableName + " where id=" + TableID + ";		\n");
                    bf.append(" delete from " + TableName + " where fuid in (" + TableID + ");		\n");
                    bf.append(" delete from jieszbsjb where jiesdid=" + TableID + ";	\n");
                    bf.append(" delete from jieszbsjb where jiesdid in (select id from " + TableName + " where fuid=" + TableID + ");	\n");
                    //------------------------指标表 和哪个表对应

                    bf.append(" end;\n");

                    con.getUpdate(bf.toString());
                    return true;//没有日志
                } else {
//						strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("回退失败：" + resul[0]);
                    return false;
                }

            }
        }
        if (TableName.equals("kuangfjsmkb")
                || TableName.equals("kuangfjsyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据

                String sql = "select * from " + TableName + " where id=" + TableID;
                ResultSetList rsl = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows()];
                int j = -1;
//					更新厂级的结算单流程状态
                while (rsl.next()) {
                    j++;
                    sqls[j] = " update " + TableName + " set liucztb_id=" + yuancljd[1] + " where id=" + TableID;
                }
                rsl.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2
//						如果厂级数据更新成功，则删除分公司层数据
                    StringBuffer bf = new StringBuffer();
                    bf.append(" begin \n");

                    bf.append(" delete from ").append(TableName).append(" where id=").append(TableID).append(";\n");
                    bf.append(" delete from jieszbsjb where jiesdid=").append(TableID).append(";\n");
                    bf.append(" delete from danpcjsmxb where jiesdid=").append(TableID).append(";\n");

                    bf.append(" end;\n");

                    con.getUpdate(bf.toString());
                    return true;//没有日志
                } else {
//						strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("回退失败：" + resul[0]);
                    return false;
                }
            }
        }
//			暂且不支持分公司上传、下发回退
//			if(TableName.equals("diancjsmkb")
//					||TableName.equals("diancjsyfb")){
//
//				String[] yuancljd=getXianjd(TableName,TableID,"回退");//远程连接点
//				if(yuancljd!=null){//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
//
//					long id=0;
//					try {
//						if(TableName.equals("diancjsmkb")){
//							 id=MainGlobal.getTableId("jiesb", "diancjsmkb_id"
//										, TableID+"");
//						}else{
//							 id=MainGlobal.getTableId("jiesyfb", "diancjsyfb_id"
//										, TableID+"");
//						}
//
//					} catch (Exception e) {
//						// TODO 自动生成 catch 块
//						e.printStackTrace();
//					}
//
//					String sql="";
//					if(TableName.equals("diancjsmkb")){
//						 sql="select * from "+" jiesb "+" where id="+id;
//					}else{
//						 sql="select * from "+" jiesyfb "+" where id="+id;
//					}
//
//					ResultSetList rsl=con.getResultSetList(sql);
//
////					sql="select * from jieszbsjb where jiesdid="+TableID;
////					ResultSetList rslzb=con.getResultSetList(sql);
//
//					String[] sqls=new String[rsl.getRows()];
//					int j=-1;
//
//					while(rsl.next()){
//						j++;
//
//						if(TableName.equals("diancjsmkb")){
//
//							sqls[j]=" update jiesb set liucztb_id="+yuancljd[1]+" where id="+rsl.getLong("id");
//
//						}else if (TableName.equals("diancjsyfb")){
//
//							sqls[j]=" update jiesyfb set liucztb_id="+yuancljd[1]+" where id="+rsl.getLong("id");
//
//						}
//					}
//					rsl.close();
//
//					String[] resul=null;
//					InterFac_dt Shangb=new InterFac_dt();	//实例化接口变量
//					resul=Shangb.sqlExe(yuancljd[0], sqls, true);
//
//					if(resul[0].equals("true")){
//						//1置当前数据状态为2
//
//					//	String sql1="update "+TableName+" set liucztb_id="+yuancljd[2]+" where id="+TableID;
//					//	con.getUpdate(sql1);
//
//						StringBuffer bf=new StringBuffer();
//						bf.append(" begin \n");
//						if(TableName.equals("diancjsmkb")){
//							bf.append(" delete from jiesb where id in ( "+id+");\n");
//							bf.append(" delete from jieszbsjb where jiesdid="+id+";\n");
//						}else{
//							bf.append(" delete from jiesyfb where id in ("+id+" );\n");
//						}
//
//						bf.append(" delete from "+TableName+" where id="+TableID+";\n");
//						//------------------------指标表 和哪个表对应
//
//						bf.append(" end;\n");
//
//						con.getUpdate(bf.toString());
//						return true;//没有日志
//					}else{
////						strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
//						System.out.print("下发失败："+resul[0]);
//						return false;
//					}
//
//				}
//			}

        if (TableName.equals("hetys")) {
            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetysb_id = String.valueOf(TableID);
                String[] resul = null;
                ResultSetList rsl = null;
                String[] sqls = new String[1];
                StringBuffer sbExeSql = new StringBuffer("begin	\n");
//						主合同信息
                String sql =
                        "select ht.id, ht.mingc, fuid, ht.diancxxb_id, hetbh, to_char(qiandrq,'yyyy-MM-dd') as qiandrq, qianddd,\n" +
                                "       y.bianm as yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh,\n" +
                                "       gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh,\n" +
                                "       xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, to_char(qisrq,'yyyy-MM-dd') as qisrq,\n" +
                                "       to_char(guoqrq,'yyyy-MM-dd') as guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid\n" +
                                "       from hetys ht,yunsdwb y\n" +
                                "       where ht.yunsdwb_id = y.id\n" +
                                "             and ht.id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetys where id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetys\n" +
                            "  (id, mingc, fuid, diancxxb_id, hetbh, qiandrq, qianddd, yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh, gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh, xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, qisrq, guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid)\n" +
                            "	values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("mingc") + "', " + rsl.getLong("fuid") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("hetbh") + "', to_date('" + rsl.getString("qiandrq") + "','yyyy-MM-dd'), '" + rsl.getString("qianddd") + "', (select id from yunsdwb where bianm='"
                            + rsl.getString("yunsdwb_id") + "'), '" + rsl.getString("gongfdwmc") + "', '" + rsl.getString("gongfdwdz") + "', '" + rsl.getString("gongfdh") + "', '" + rsl.getString("gongffddbr") + "', '" + rsl.getString("gongfwtdlr") + "', '" + rsl.getString("gongfdbgh") + "', '" + rsl.getString("gongfkhyh")
                            + "', '" + rsl.getString("gongfzh") + "', '" + rsl.getString("gongfyzbm") + "', '" + rsl.getString("gongfsh") + "', '" + rsl.getString("xufdwmc") + "', '" + rsl.getString("xufdwdz") + "', '" + rsl.getString("xuffddbr") + "', '" + rsl.getString("xufwtdlr") + "', '" + rsl.getString("xufdh")
                            + "', '" + rsl.getString("xufdbgh") + "', '" + rsl.getString("xufkhyh") + "', '" + rsl.getString("xufzh") + "', '" + rsl.getString("xufyzbm") + "', '" + rsl.getString("xufsh") + "', to_date('" + rsl.getString("qisrq") + "','yyyy-MM-dd'), to_date('" + rsl.getString("guoqrq")
                            + "','yyyy-MM-dd'), " + rsl.getLong("hetys_mb_id") + ", '" + rsl.getString("meikmcs") + "', " + yuancljd[1] + ", " + rsl.getLong("liucgzid") + ");\n"
                    );
                }
//						价格信息
                sql =
                        "select jg.id, hetys_id, m.bianm as meikxxb_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id,\n" +
                                "       shangx, xiax, dw.bianm as danwb_id, yunja, yjdw.bianm as yunjdw_id\n" +
                                "       from hetysjgb jg, meikxxb m, zhibb zb, tiaojb tj, danwb dw, danwb yjdw\n" +
                                "       where jg.meikxxb_id = m.id(+) and jg.zhibb_id = zb.id(+) and jg.tiaojb_id = tj.id(+)\n" +
                                "             and jg.danwb_id = dw.id(+) and jg.yunjdw_id = yjdw.id(+)\n" +
                                "             and jg.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetysjgb where hetys_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetysjgb\n" +
                            "	(id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id)\n" +
                            "	values\n" +
                            "	(" + rsl.getLong("id") + ", " + rsl.getLong("hetys_id") + ", (select id from meikxxb where bianm='"
                            + rsl.getString("meikxxb_id") + "'), (select id from zhibb where bianm='"
                            + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='"
                            + rsl.getString("tiaojb_id") + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax")
                            + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "'and zhibb_id!=0), " + rsl.getString("yunja")
                            + ", (select id from danwb where bianm='" + rsl.getString("yunjdw_id") + "'and zhibb_id!=0));\n"
                    );
                }
/*
*huochaoyuan
*2009-10-22运输合同添加收货人，但上传/下发未做处理，故添加如下语句；
*/
                sql = "select s.id,s.hetysb_id,s.shouhr_id from hetysshrb s  where s.hetysb_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);
                sbExeSql.append("	delete from hetysshrb where hetysb_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("insert into hetysshrb\n" +
                            "  (id, hetysb_id, shouhr_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", " + rsl.getLong("hetysb_id") + ", " + rsl.getLong("shouhr_id") + ");\n"
                    );
                }
//						end
//						增扣款
                sql = "select zkk.id, jis, jsdw.bianm as jisdwid, kouj, kjdw.bianm as koujdw, zengfj, zfjdw.bianm as zengfjdw,\n" +
                        "       xiaoscl, jizzkj, jizzb, czxm.bianm as canzxm, czxmdw.bianm as canzxmdw, canzsx, canzxx, jsxs.bianm as hetjsxsb_id,\n" +
                        "       ysfs.mingc as yunsfsb_id, zkk.beiz, hetys_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id, shangx, xiax,\n" +
                        "       dw.bianm as danwb_id\n" +
                        "       from hetyszkkb zkk, danwb jsdw, danwb kjdw, danwb zfjdw, zhibb jzzb, zhibb czxm, danwb czxmdw, hetjsxsb jsxs,\n" +
                        "            yunsfsb ysfs, zhibb zb, tiaojb tj,danwb dw\n" +
                        "       where zkk.jisdwid = jsdw.id(+) and zkk.koujdw = kjdw.id (+) and zkk.zengfjdw = zfjdw.id(+)\n" +
                        "             and zkk.jizzb = jzzb.id(+) and zkk.canzxm = czxm.id(+) and zkk.canzxmdw = czxmdw.id(+)\n" +
                        "             and zkk.hetjsxsb_id = jsxs.id and zkk.yunsfsb_id = ysfs.id and zkk.zhibb_id = zb.id\n" +
                        "             and zkk.tiaojb_id = tj.id(+) and zkk.danwb_id = dw.id (+) and zkk.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyszkkb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyszkkb\n" +
                            "  (id, jis, jisdwid, kouj, koujdw, zengfj, zengfjdw, xiaoscl, jizzkj, jizzb, canzxm, canzxmdw, canzsx, canzxx, hetjsxsb_id, yunsfsb_id, beiz, hetys_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", " + rsl.getString("jis") + ", (select id from danwb where bianm='" + rsl.getString("jisdwid") + "'), " + rsl.getString("kouj") + ", (select id from danwb where bianm='"
                            + rsl.getString("koujdw") + "'), " + rsl.getString("zengfj") + ", (select id from danwb where bianm='" + rsl.getString("zengfjdw") + "'), " + rsl.getString("xiaoscl") + ", " + rsl.getString("jizzkj")
                            + ", " + rsl.getString("jizzb") + ", (select id from zhibb where bianm='" + rsl.getString("canzxm") + "'), (select id from danwb where bianm='" + rsl.getString("canzxmdw") + "'), " + rsl.getString("canzsx")
                            + ", " + rsl.getString("canzxx") + ", (select id from hetjsxsb where bianm='" + rsl.getString("hetjsxsb_id") + "'), (select id from yunsfsb where mingc='" + rsl.getString("yunsfsb_id")
                            + "'), '" + rsl.getString("beiz") + "', " + rsl.getLong("hetys_id") + ", (select id from zhibb where bianm='" + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='" + rsl.getString("tiaojb_id")
                            + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax") + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "'  and zhibb_id!=0));\n"
                    );
                }
//						文字
                sql = "select id, wenznr, hetys_id from hetyswzb where hetys_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyswzb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyswzb(id, wenznr, hetys_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("wenznr") + "', " + rsl.getLong("hetys_id") + ");\n"
                    );
                }

                sbExeSql.append("end;");
                sqls[0] = sbExeSql.toString();

                if (rsl != null) {

                    rsl.close();
                }

                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update hetys set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return true;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    return false;
                }
            }
        }

        //
//			JDBCcon con=new JDBCcon();
        String sql = "";
        String dangqdz = ""; //表单当前状态
        long liuczthjid = 0;
        long liucdzb_id = 0;
        String qianqmc = "";
        String houjmc = "";
        String caoz = "";
        long leibztb_id = -1;
        try {
            sql = "select liucztb_id\n" +//获得当前状态
                    "from " + TableName + "\n" +
                    "where id=" + TableID;
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {
                dangqdz = rs.getString(1);
            }
            sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n" +
                    "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n" +
                    "where liucdzb.liucztqqid=liucztb1.id\n" +
                    " and liucdzb.liuczthjid=liucztb2.id\n" +
                    " and liucztb1.leibztb_id=leibztb1.id\n" +
                    " and liucztb2.leibztb_id=leibztb2.id\n" +
                    " and liucdzb.liucztqqid=" + dangqdz +
                    "  and liucztb1.xuh>liucztb2.xuh";
            ResultSet rs1 = con.getResultSet(sql);
            if (rs1.next()) {
                liuczthjid = rs1.getLong("liuczthjid");
                liucdzb_id = rs1.getLong("id");
                qianqmc = rs1.getString("qianqmc");
                houjmc = rs1.getString("houjmc");
                caoz = rs1.getString("caoz");
                leibztb_id = rs1.getLong("leibztb_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //日志
//				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij," +
                "miaos)values(xl_xul_id.nextval,"
                + TableID
                + ","
                + liucdzb_id
                + ",'"
                + qianqmc
                + "','"
                + houjmc
                + "','"
                + caoz
                + "',(select quanc from renyxxb where id="
                + renyxxb_id
                + "),sysdate"
                + ",'"
                + xiaox
                + "')";
        con.getInsert(sql);
        if (leibztb_id == 0 || leibztb_id == 1) {//如果后继类别状态为0或１即为回退
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + leibztb_id
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        } else {//如果后继为
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + liuczthjid
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        }
        con.getUpdate(sql);
        con.Close();
        return true;
    }

    /**
     * 我的任务返回数据表的ｉｄ，类别中所有流程中我的数据表ｉｄ除了发起和结束状态的所有状态
     *
     * @return
     */
    public static String getWodrws(String tableName, long renyxxb_id, String leib) {
        String ids = "";
        JDBCcon con = new JDBCcon();
        //
        String sql =
//				"select "+tableName+".id\n" +
//				"from  "+tableName+"\n" +
//				"where  "+tableName+".liucztb_id in(\n" +
//				"select liucdzb.liucztqqid\n" +
//				"from liucdzjsb,liucdzb\n" +
//				"where liucdzjsb.liucdzb_id=liucdzb.id\n" +
//				"and liucdzjsb.liucjsb_id in\n" +
//				"(select liucjsb_id from renyjsb where renyxxb_id="+renyxxb_id+")\n" +
//				")";
                "select " + tableName + ".id\n" +
                        "from  " + tableName + "\n" +
                        "where  " + tableName + ".liucztb_id in(\n" +
                        "select liucdzb.liucztqqid\n" +
                        "from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb\n" +
                        "where liucdzjsb.liucdzb_id=liucdzb.id and liucdzb.liucztqqid=liucztb.id\n" +
                        "and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='" + leib + "'\n" +
                        "and liucdzjsb.liucjsb_id in\n" +
                        "(select liucjsb_id from renyjsb where renyxxb_id=" + renyxxb_id + ")\n" +
                        ")";

        ResultSet rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
                if (!ids.equals("")) {
                    ids += ",";
                }
                ids += rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        if (ids.equals("")) {
            ids = "-1";
        }
        return ids;
    }

    /**
     * 类别中所有流程中不是我的数据表ｉｄ
     *
     * @return
     */
    public static String getLiuczs(String tableName, long renyxxb_id, String leib) {
        String ids = "";
        JDBCcon con = new JDBCcon();
        String sql =
                "select " + tableName + ".id\n" +
                        "from  " + tableName + "\n" +
                        "where   " + tableName + ".liucztb_id in\n" +
                        "(\n" +
                        "select liucztb.id\n" +
                        "from liucztb,leibztb,liuclbb\n" +
                        "where liucztb.leibztb_id=leibztb.id and liuclbb.id=leibztb.liuclbb_id and liuclbb.mingc='" + leib + "'\n" +
                        "minus\n" +
                        "select distinct liucdzb.liucztqqid\n" +
                        "from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb\n" +
                        "where liucdzjsb.liucdzb_id=liucdzb.id and liucdzb.liucztqqid=liucztb.id\n" +
                        "and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='" + leib + "'\n" +
                        "and liucdzjsb.liucjsb_id in\n" +
                        "(select liucjsb_id from renyjsb where renyxxb_id=" + renyxxb_id + ")\n" +
                        ")";//新增远程流程处理中
        ResultSet rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
                if (!ids.equals("")) {
                    ids += ",";
                }
                ids += rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        if (ids.equals("")) {
            ids = "-1";
        }
        return ids;
    }


    //增加对合同模板的处理方法
    public static String getLiuczsMB(String tableName, long renyxxb_id, String leib) {
        String ids = "";
        JDBCcon con = new JDBCcon();
        String sql =
                "select " + tableName + ".id\n" +
                        "from  " + tableName + "\n" +
                        "where   " + tableName + ".liucztb_id in\n" +
                        "(\n" +
                        "select liucztb.id\n" +
                        "from liucztb,leibztb,liuclbb\n" +
                        "where liucztb.leibztb_id=leibztb.id and liuclbb.id=leibztb.liuclbb_id and liuclbb.mingc='" + leib + "'\n" +
                        "minus\n" +
                        "select distinct liucdzb.liucztqqid\n" +
                        "from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb\n" +
                        "where liucdzjsb.liucdzb_id=liucdzb.id and liucdzb.liucztqqid=liucztb.id\n" +
                        "and liucztb.leibztb_id=leibztb.id and leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='" + leib + "'\n" +
                        "and liucdzjsb.liucjsb_id in\n" +
                        "(select liucjsb_id from renyjsb where renyxxb_id=" + renyxxb_id + ")\n" +
                        ")";//新增远程流程处理中
        ResultSet rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
                if (!ids.equals("")) {
                    ids += ",";
                }
                ids += rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        if (ids.equals("")) {
            ids = "-1";
        }
        return ids;
    }

    public static int getShendId(long LiucID, long liucztb_id) {//获得待审定状态ｉｄ
        String sql =
                "select id,rownum as nmber\n" +
                        "from\n" +
                        "(\n" +
                        "select id,rownum as nmber\n" +
                        "from(\n" +
                        "select id\n" +
                        "from liucztb\n" +
                        "where liucb_id=" + LiucID +
                        " order by liucztb.xuh desc\n" +
                        ")a\n" +
                        ")\n" +
                        "where nmber=2";
        JDBCcon con = new JDBCcon();
        ResultSet rs = con.getResultSet(sql);
        long ShendId = 0;
        try {
            if (rs.next()) {
                ShendId = rs.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        con.Close();
        if (liucztb_id == ShendId) {//审定状态
            return 1;
        } else {
            return 0;
        }
    }

    public static List getRiz(long TableID) {//日志bean
//			加载意见
        JDBCcon con = new JDBCcon();
        List list = new ArrayList();
        String sql =
                "select rzb.qianqztmc,to_char(rzb.shij,'YYYY-MM-DD HH24:mi:ss')shij,rzb.liucdzbmc,rzb.houjztmc,rzb.miaos,rzb.caozy\n" +
                        "from liucgzb rzb\n" +
                        "where rzb.liucgzid=" + TableID + " order by shij";
        ResultSet rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
//					String qianqztmc=rs.getString("qianqztmc");
                String caozy = rs.getString("caozy");
                String shij = rs.getString("shij");
                String liucdzbmc = rs.getString("liucdzbmc");
                String houjztmc = rs.getString("houjztmc");
                String miaos = rs.getString("miaos");
                list.add(new Yijbean(caozy + shij + liucdzbmc + houjztmc + ":", miaos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        con.Close();
        return list;
    }

    //根据qxlx权限类型 确定是 我的任务 还是流程中的数据
    public static String getTableLiuczt(String tableName, long renyxxb_id, String leib, long qxlx) {

        if (qxlx == 1) {//我的任务
            return getWodrws(tableName, renyxxb_id, leib);
        } else {
            return getLiuczs(tableName, renyxxb_id, leib);
        }

    }

    public static void liucxjdzcl(JDBCcon con, String TableName, long TableID, long liucjsb_id, String liucztqqid, String liuczthjid, String caoz, String dongz) {//流程衔接动作处理

        if (dongz == null || dongz.equals("")) {
            return;//没有 相应的动作，无需查找配置
        }
        String sql = " select * from liucdzxjb where liucjsb_id=" + liucjsb_id + " and liucztqqmc='" + liucztqqid + "' \n" +
                " and liuczthjmc='" + liuczthjid + "' and caoz='" + caoz + "' and liucdz='" + dongz + "' order by youxj asc ";

        ResultSetList rsl = con.getResultSetList(sql);

        while (rsl.next()) {
            String xianjdz = rsl.getString("xianjdz");
            String xianjl = rsl.getString("xianjl");
            Liucxjcl.active(con, TableName, TableID, xianjdz, xianjl);
        }
    }

    public static void tij(JDBCcon con, String TableName, long TableID, long renyxxb_id, String xiaox) {
//			提交
        //先判断是否存在衔接点

        if (TableName.equals("hetb")) {
            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetb_id = String.valueOf(TableID);
                String diancxxb_id = null;
                String[] resul = null;
                String hetbh = "";
                String sql = "select hetb.ID,hetb.FUID,DIANCXXB_ID,HETBH,to_char(QIANDRQ,'yyyy-mm-dd')QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                        "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,decode(gongysb.shangjgsbm,null,gongysb.bianm,gongysb.shangjgsbm) HETGYSBID,\n" +
                        " decode(gs.shangjgsbm,null,gs.bianm,gs.shangjgsbm) GONGYSB_ID,to_char(QISRQ,'yyyy-mm-dd')QISRQ,to_char(GUOQRQ,'yyyy-mm-dd')GUOQRQ,HETB_MB_ID,j.bianm JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,nvl(HETJJFSB_ID,0)HETJJFSB_ID,MEIKMCS\n" +
                        "from hetb,gongysb,gongysb gs,jihkjb j\n" +
                        "where hetb.jihkjb_id=j.id and hetb.HETGYSBID=gongysb.id and hetb.GONGYSB_ID=gs.id and hetb.id=" + hetb_id;
                ResultSetList rs1 = con.getResultSetList(sql);

                sql = "select hetslb.ID,pinzb.mingc PINZB_ID,YUNSFSB_ID,fz.mingc FAZ_ID,dz.mingc DAOZ_ID,DIANCXXB_ID,to_char(RIQ,'yyyy-mm-dd')riq,HETL,HETB_ID,hetslb.ZHUANGT\n" +
                        "from hetslb,pinzb,chezxxb fz,chezxxb dz\n" +
                        "where hetslb.faz_id=fz.id and hetslb.daoz_id=dz.id and hetslb.pinzb_id=pinzb.id and hetslb.hetb_id=" + hetb_id;
                ResultSetList rs2 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID\n" +
                        "from hetzlb\n" +
                        "where hetzlb.hetb_id=" + hetb_id;
                ResultSetList rs3 = con.getResultSetList(sql);

/*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
                        "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX\n" +
//end
                        "from hetjgb\n" +
                        "where hetjgb.hetb_id=" + hetb_id;
                ResultSetList rs4 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                        "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID\n" +
                        "from hetzkkb\n" +
                        "where hetzkkb.hetb_id=" + hetb_id;
                ResultSetList rs5 = con.getResultSetList(sql);

                sql = "select ID,WENZNR,HETB_ID\n" +
                        "from hetwzb\n" +
                        "where hetwzb.hetb_id=" + hetb_id;
                ResultSetList rs6 = con.getResultSetList(sql);
                int len = rs1.getResultSetlist().size() + rs2.getResultSetlist().size() + rs3.getResultSetlist().size() + rs4.getResultSetlist().size()
                        + rs5.getResultSetlist().size() + rs6.getResultSetlist().size() + 6;//6为五个分表的删除
                String[] sqls = new String[len];
                int j = 0;
                sqls[j] = "delete from hetb where id=" + hetb_id;
                j++;
                while (rs1.next()) {
                    diancxxb_id = rs1.getString("DIANCXXB_ID");
                    hetbh = rs1.getString("HETBH");
                    sqls[j] = "insert into hetb(ID,FUID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                            "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,\n" +
                            "GONGYSB_ID,QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS\n" +
                            ")values(\n" +
                            rs1.getString("ID") + "," +
                            rs1.getString("FUID") + "," +
                            diancxxb_id + ",'" +
                            hetbh + "',to_date('" +
                            rs1.getString("QIANDRQ") + "','yyyy-mm-dd'),'" +
                            rs1.getString("QIANDDD") + "','" +
                            rs1.getString("GONGFDWMC") + "','" +
                            rs1.getString("GONGFDWDZ") + "','" +
                            rs1.getString("GONGFDH") + "','" +
                            rs1.getString("GONGFFDDBR") + "','" +
                            rs1.getString("GONGFWTDLR") + "','" +
                            rs1.getString("GONGFDBGH") + "','" +
                            rs1.getString("GONGFKHYH") + "','" +
                            rs1.getString("GONGFZH") + "','" +
                            rs1.getString("GONGFYZBM") + "','" +
                            rs1.getString("GONGFSH") + "','" +
                            rs1.getString("XUFDWMC") + "','" +
                            rs1.getString("XUFDWDZ") + "','" +
                            rs1.getString("XUFFDDBR") + "','" +
                            rs1.getString("XUFWTDLR") + "','" +
                            rs1.getString("XUFDH") + "','" +
                            rs1.getString("XUFDBGH") + "','" +
                            rs1.getString("XUFKHYH") + "','" +
                            rs1.getString("XUFZH") + "','" +
                            rs1.getString("XUFYZBM") + "','" +
                            rs1.getString("XUFSH") + "',(select id from gongysb where bianm='" +
                            rs1.getString("HETGYSBID") + "'),(select id from gongysb where bianm='" +
                            rs1.getString("GONGYSB_ID") + "'),to_date('" +
                            rs1.getString("QISRQ") + "','yyyy-mm-dd'),to_date('" +
                            rs1.getString("GUOQRQ") + "','yyyy-mm-dd')," +
                            rs1.getString("HETB_MB_ID") + ",(select id from jihkjb where bianm='" +
                            rs1.getString("JIHKJB_ID") + "')," +
                            yuancljd[1] + "," +
                            rs1.getString("LIUCGZID") + "," +
                            rs1.getString("LEIB") + "," +
                            rs1.getString("HETJJFSB_ID") + ",'" +
                            rs1.getString("MEIKMCS") + "'" +
//							 rs1.getString("XIAF")+
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetslb where hetb_id=" + hetb_id;
                j++;
                while (rs2.next()) {
                    sqls[j] = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT)values(\n" +
                            rs2.getString("ID") + ",(select id from pinzb where mingc='" +
                            rs2.getString("PINZB_ID") + "')," +
                            rs2.getString("YUNSFSB_ID") + ",(select id from chezxxb where mingc='" +
                            rs2.getString("FAZ_ID") + "'),(select id from chezxxb where mingc='" +
                            rs2.getString("DAOZ_ID") + "')," +
                            rs2.getString("DIANCXXB_ID") + ",to_date('" +
                            rs2.getString("RIQ") + "','yyyy-mm-dd')," +
                            rs2.getString("HETL") + "," +
                            rs2.getString("HETB_ID") + "," +
                            rs2.getString("ZHUANGT") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetzlb where hetb_id=" + hetb_id;
                j++;
                while (rs3.next()) {
                    sqls[j] = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values(\n" +
                            rs3.getString("ID") + ",'" +
                            rs3.getString("ZHIBB_ID") + "','" +
                            rs3.getString("TIAOJB_ID") + "','" +
                            rs3.getString("SHANGX") + "','" +
                            rs3.getString("XIAX") + "','" +
                            rs3.getString("DANWB_ID") + "'," +
                            rs3.getString("HETB_ID") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetjgb where hetb_id=" + hetb_id;
                j++;
                while (rs4.next()) {
                    sqls[j] = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
  /*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX)values(\n" +
//end
                            rs4.getString("ID") + ",'" +
                            rs4.getString("ZHIBB_ID") + "','" +
                            rs4.getString("TIAOJB_ID") + "','" +
                            rs4.getString("SHANGX") + "','" +
                            rs4.getString("XIAX") + "','" +
                            rs4.getString("DANWB_ID") + "','" +
                            rs4.getString("JIJ") + "','" +
                            rs4.getString("JIJDWID") + "','" +
                            rs4.getString("HETJSFSB_ID") + "','" +
                            rs4.getString("HETJSXSB_ID") + "','" +
                            rs4.getString("YUNJ") + "','" +
                            rs4.getString("YUNJDW_ID") + "','" +
                            rs4.getString("YINGDKF") + "','" +
                            rs4.getString("YUNSFSB_ID") + "','" +
                            rs4.getString("ZUIGMJ") + "','" +
                            rs4.getString("HETB_ID") + "','" +
/*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            rs4.getString("HETJJFSB_ID") + "','" +
                            rs4.getString("JIJLX") +
//end
                            "')";
                    j++;
                }
                sqls[j] = "delete from hetzkkb where hetb_id=" + hetb_id;
                j++;
                while (rs5.next()) {
                    sqls[j] = "insert into hetzkkb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                            "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID)values(\n" +
                            rs5.getString("ID") + ",'" +
                            rs5.getString("ZHIBB_ID") + "','" +
                            rs5.getString("TIAOJB_ID") + "','" +
                            rs5.getString("SHANGX") + "','" +
                            rs5.getString("XIAX") + "','" +
                            rs5.getString("DANWB_ID") + "','" +
                            rs5.getString("JIS") + "','" +
                            rs5.getString("JISDWID") + "','" +
                            rs5.getString("KOUJ") + "','" +
                            rs5.getString("KOUJDW") + "','" +
                            rs5.getString("ZENGFJ") + "','" +
                            rs5.getString("ZENGFJDW") + "','" +
                            rs5.getString("XIAOSCL") + "','" +
                            rs5.getString("JIZZKJ") + "','" +
                            rs5.getString("JIZZB") + "','" +
                            rs5.getString("CANZXM") + "','" +
                            rs5.getString("CANZXMDW") + "','" +
                            rs5.getString("CANZSX") + "','" +
                            rs5.getString("CANZXX") + "','" +
                            rs5.getString("HETJSXSB_ID") + "','" +
                            rs5.getString("YUNSFSB_ID") + "','" +
                            rs5.getString("BEIZ") + "'," +
                            rs5.getString("HETB_ID") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetwzb where hetb_id=" + hetb_id;
                j++;
                while (rs6.next()) {
                    sqls[j] = "insert into hetwzb(ID,WENZNR,HETB_ID)values(\n" +
                            rs6.getString("ID") + ",'" +
                            rs6.getString("WENZNR") + "'," +
                            rs6.getString("HETB_ID") +
                            ")";
                    j++;
                }
//						String strSuc="";//成功的合同编号
                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update  hetb set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    return;
                }
            }
        }
//			2009-5-18日 新增结算 上传、下发功能
        if (TableName.equals("jiesb")
                || TableName.equals("jiesyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                String sql = "";
                if (TableName.equals("jiesb")) {
                    sql =
                            "select j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukmk,\n" +
                                    "       j.hansmk,\n" +
                                    "       j.buhsmk,\n" +
                                    "       j.meikje,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       h.hetbh as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       j.jiesfrl,\n" +
                                    "       jhkj.bianm as jihkjb_id,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as  meikxxb_id,\n" +
                                    "       j.hetj,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.zhiljq,\n" +
                                    "       j.qiyf,\n" +
                                    "       j.jiesrl,\n" +
                                    "       j.jieslf,\n" +
                                    "       j.jiesrcrl,\n" +
                                    "       j.liucgzbid,\n" +
                                    "       j.ruzry,\n" +
                                    "       j.fuid,\n" +
                                    "       j.fengsjj,\n" +
                                    "       j.jiajqdj,\n" +
                                    "       j.jijlx,\n" +
                                    "		nvl(j.diancjsmkb_id,0) as diancjsmkb_id,\n" +
                                    "		nvl(j.yufkje,0) as yufkje,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.chaokdl,0) as chaokdl,\n" +
                                    "		nvl(j.chaokdlx,'') as chaokdlx, \n" +
                                    "		nvl(j.Yunfhsdj,0) as Yunfhsdj, \n" +
                                    "		nvl(j.hansyf,0) as hansyf, \n" +
                                    "		nvl(j.buhsyf,0) as buhsyf, \n" +
                                    "		nvl(j.yunfjsl,0) as yunfjsl \n" +
                                    "  from " + TableName + " j,gongysb g,hetb h,jihkjb jhkj,meikxxb m \n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.jihkjb_id = jhkj.id\n" +
                                    "        and j.hetb_id = h.id \n" +
                                    "        and j.meikxxb_id = m.id";

                } else if (TableName.equals("jiesyfb")) {
                    sql =
                            "select j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.guotyf,\n" +
                                    "       j.guotzf,\n" +
                                    "       j.kuangqyf,\n" +
                                    "       j.kuangqzf,\n" +
                                    "       j.jiskc,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukyf,\n" +
                                    "       j.hansyf,\n" +
                                    "       j.buhsyf,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       decode(j.jieslx,1,(select hetbh from hetb where id=j.hetb_id),(select hetbh from hetys where id=j.hetb_id)) as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       j.diancjsmkb_id as diancjsmkb_id,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       nvl(j.guotyfjf,0) as guotyfjf, \n" +
                                    "       nvl(j.guotzfjf,0) as guotzfjf,\n" +
                                    "       nvl(j.gongfsl,0) as gongfsl,\n" +
                                    "       nvl(j.yanssl,0) as yanssl,\n" +
                                    "       nvl(j.yingk,0) as yingk,\n" +
                                    "       j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       nvl(j.ditzf,0) as ditzf,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                    "       nvl(j.dityf,0) as dityf,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.ruzry,\n" +
                                    "       nvl(j.fuid,0) as fuid,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.diancjsyfb_id,0) as diancjsyfb_id	\n" +
                                    "  from " + TableName + " j,gongysb g,meikxxb m\n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.meikxxb_id = m.id";

                }
                ResultSetList rsl = con.getResultSetList(sql);

                sql = "select j.id, jiesdid, z.bianm as zhibb_id, hetbz,\n" +
                        "       	gongf, changf, jies, yingk, zhejbz, zhejje,\n" +
                        "       	zhuangt, yansbhb_id\n" +
                        "       from jieszbsjb j, zhibb z\n" +
                        "       where j.zhibb_id = z.id\n" +
                        " 			and j.jiesdid=" + TableID;
                ResultSetList rslzb = con.getResultSetList(sql);

                sql =
                        "select d.id,\n" +
                                "       d.xuh,\n" +
                                "       d.jiesdid,\n" +
                                "       z.bianm as zhibb_id,\n" +
                                "       d.hetbz,\n" +
                                "       d.gongf,\n" +
                                "       d.changf,\n" +
                                "       d.jies,\n" +
                                "       d.yingk,\n" +
                                "       d.zhejbz,\n" +
                                "       d.zhejje,\n" +
                                "       d.gongfsl,\n" +
                                "       d.yanssl,\n" +
                                "       d.jiessl,\n" +
                                "       d.koud,\n" +
                                "       d.kous,\n" +
                                "       d.kouz,\n" +
                                "       d.ches,\n" +
                                "       d.jingz,\n" +
                                "       d.koud_js,\n" +
                                "       d.yuns,\n" +
                                "       d.jiesslcy,\n" +
                                "       d.jiesdj,\n" +
                                "       d.jiakhj,\n" +
                                "       d.jiaksk,\n" +
                                "       d.jiashj,\n" +
                                "       d.biaomdj,\n" +
                                "       d.buhsbmdj,\n" +
                                "       d.leib,\n" +
                                "       d.hetj,\n" +
                                "       d.qnetar,\n" +
                                "       d.std,\n" +
                                "       d.stad,\n" +
                                "       d.star,\n" +
                                "       d.vdaf,\n" +
                                "       d.mt,\n" +
                                "       d.mad,\n" +
                                "       d.aad,\n" +
                                "       d.ad,\n" +
                                "       d.aar,\n" +
                                "       d.vad,\n" +
                                "       d.zongje,\n" +
                                "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                "       c.bianm as faz_id,\n" +
                                "       d.chaokdl,\n" +
                                "       d.jiajqdj\n" +
                                "  from danpcjsmxb d,zhibb z,jiesb j,meikxxb m,chezxxb c\n" +
                                "  where d.zhibb_id = z.id\n" +
                                "        and d.jiesdid = j.id\n" +
                                "        and d.meikxxb_id = m.id\n" +
                                "        and d.faz_id = c.id\n" +
                                "		 and d.jiesdid=" + TableID;
                ResultSetList rsldpc = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows() + rslzb.getRows() + rsldpc.getRows() + 3];    //加三行删除语句
                int j = 0;
                sqls[j] = "delete from " + TableName + " where id=" + TableID;

                while (rsl.next()) {
                    j++;

                    if (TableName.equals("jiesb")) {

                        sqls[j] = "insert into jiesb	\n"
                                + " (id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, \n"
                                + " buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, ranlbmjbr, ranlbmjbrq, beiz, jiesfrl, jihkjb_id, meikxxb_id, hetj, meikdwmc, zhiljq, qiyf, jiesrl, jieslf, jiesrcrl, liucgzbid, ruzry, fuid, fengsjj, jiajqdj, jijlx, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, diancjsmkb_id, yufkje, Yunfhsdj, hansyf, buhsyf, yunfjsl)	\n"
                                + " values	\n"
                                + " (" + rsl.getLong("id") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("bianm")
                                + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), '" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id")
                                + ", '" + rsl.getString("yunj") + "', " + rsl.getDouble("yingd") + ", " + rsl.getDouble("kuid") + ", '" + rsl.getString("faz")
                                + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq")) + "','yyyy-mm-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("meiz")
                                + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq")) + "','yyyy-mm-dd'),to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw")
                                + "', '" + rsl.getString("kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs") + "', '" + rsl.getString("duifdd")
                                + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy")
                                + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukmk") + ", " + rsl.getString("hansmk") + ", " + rsl.getString("buhsmk") + ", " + rsl.getString("meikje") + ", " + rsl.getString("shuik")
                                + ", " + rsl.getString("shuil") + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq")))
                                + "','yyyy-MM-dd'), (select id from hetb where hetbh = '" + rsl.getString("hetb_id") + "'), " + yuancljd[1] + ", " + rsl.getString("liucgzid") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq"))
                                + "','yyyy-MM-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("jiesfrl") + ", (select id from jihkjb where bianm = '" + rsl.getString("jihkjb_id") + "'), (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("hetj") + ", '" + rsl.getString("meikdwmc")
                                + "', '" + rsl.getString("zhiljq") + "', " + rsl.getString("qiyf") + ", " + rsl.getString("jiesrl") + ", " + rsl.getString("jieslf") + ", " + rsl.getString("jiesrcrl") + ", " + rsl.getString("liucgzbid") + ", '" + rsl.getString("ruzry")
                                + "', " + rsl.getLong("fuid") + ", " + rsl.getString("fengsjj") + ", " + rsl.getString("jiajqdj") + ", " + rsl.getString("jijlx") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getString("chaokdl") + ", '"
                                + rsl.getString("chaokdlx") + "'," + rsl.getString("diancjsmkb_id") + "," + rsl.getDouble("yufkje") + "," + rsl.getString("Yunfhsdj") + "," + rsl.getString("hansyf") + "," + rsl.getString("buhsyf") + "," + rsl.getString("yunfjsl") + " )\n";

                    } else if (TableName.equals("jiesyfb")) {

                        sqls[j] = "insert into jiesyfb	\n"
                                + " (gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, \n"
                                + " ruzrq, hetb_id, liucztb_id, liucgzid, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, guotyfjf, guotzfjf, gongfsl, yanssl, yingk, id, diancxxb_id, bianm, gongysb_id, ditzf, meikxxb_id, dityf, meikdwmc, ruzry, fuid, kuidjfyf, kuidjfzf, diancjsyfb_id )	\n"
                                + " values	\n"
                                + " ('" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id") + ", '" + rsl.getString("yunj") + "', " + rsl.getString("yingd") + ", " + rsl.getString("kuid") + ", '" + rsl.getString("faz") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("meiz") + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw") + "', '" + rsl.getString("v_kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs")
                                + "', '" + rsl.getString("duifdd") + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy") + ", " + rsl.getString("guotyf") + ", " + rsl.getString("guotzf")
                                + ", " + rsl.getString("kuangqyf") + ", " + rsl.getString("kuangqzf") + ", " + rsl.getString("jiskc") + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukyf") + ", " + rsl.getString("hansyf") + ", " + rsl.getString("buhsyf") + ", " + rsl.getString("shuik") + ", " + rsl.getString("shuil")
                                + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq"))) + "','yyyy-MM-dd'), " + rsl.getString("hetb_id") + ", " + yuancljd[1] + ", " + rsl.getString("liucgzid")
                                + ", " + rsl.getString("diancjsmkb_id") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq")) + "','yyyy_mm-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("guotyfjf") + ", " + rsl.getString("guotzfjf") + ", " + rsl.getString("gongfsl") + ", " + rsl.getString("yanssl")
                                + ", " + rsl.getString("yingk") + ", " + rsl.getString("id") + ", " + rsl.getString("diancxxb_id") + ", '" + rsl.getString("bianm") + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), " + rsl.getString("ditzf") + ", (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("dityf") + ", '" + rsl.getString("meikdwmc")
                                + "','" + rsl.getString("ruzry") + "', " + rsl.getString("fuid") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getLong("diancjsyfb_id") + ") \n";
                    }
                }
                rsl.close();

                sqls[++j] = "delete from jieszbsjb where jiesdid=" + TableID;
                while (rslzb.next()) {
                    j++;
                    sqls[j] = "insert into jieszbsjb	\n"
                            + " (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)	\n"
                            + " values	\n"
                            + " (" + rslzb.getLong("id") + ", " + rslzb.getLong("jiesdid") + ", (select id from zhibb where bianm='" + rslzb.getString("zhibb_id") + "'), '" + rslzb.getString("hetbz")
                            + "', " + rslzb.getString("gongf") + ", " + rslzb.getString("changf") + ", " + rslzb.getString("jies") + ", " + rslzb.getString("yingk")
                            + ", " + rslzb.getString("zhejbz") + ", " + rslzb.getString("zhejje") + ", " + rslzb.getString("zhuangt") + ", " + rslzb.getString("yansbhb_id") + ") \n";
                }
                rslzb.close();

                sqls[++j] = "delete from danpcjsmxb where jiesdid=" + TableID;
                while (rsldpc.next()) {
                    j++;
                    sqls[j] = "insert into danpcjsmxb	\n"
                            + " (id,xuh,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,gongfsl,yanssl,jiessl,koud,kous,kouz,ches,jingz,koud_js," +
                            "yuns,jiesslcy,jiesdj,jiakhj,jiaksk,jiashj,biaomdj,buhsbmdj,leib,hetj,qnetar,std,stad," +
                            "star,vdaf,mt,mad,aad,ad,aar,vad,zongje,meikxxb_id,faz_id,chaokdl,jiajqdj)	\n"
                            + " values	\n"
                            + " (" + rsldpc.getLong("id") + ", " + rsldpc.getString("xuh") + ", " + rsldpc.getString("jiesdid") + ", (select id from zhibb where bianm ='" + rsldpc.getString("zhibb_id") + "'), '" + rsldpc.getString("hetbz")
                            + "', " + rsldpc.getString("gongf") + ", " + rsldpc.getString("changf") + ", " + rsldpc.getString("jies") + ", " + rsldpc.getString("yingk")
                            + ", " + rsldpc.getString("zhejbz") + ", " + rsldpc.getString("zhejje") + ", " + rsldpc.getString("gongfsl")
                            + ", " + rsldpc.getString("yanssl") + ", " + rsldpc.getString("jiessl") + ", " + rsldpc.getString("koud")
                            + ", " + rsldpc.getString("kous") + ", " + rsldpc.getString("kouz") + ", " + rsldpc.getString("ches")
                            + ", " + rsldpc.getString("jingz") + ", " + rsldpc.getString("koud_js") + ", " + rsldpc.getString("yuns")
                            + ", " + rsldpc.getString("jiesslcy") + ", " + rsldpc.getString("jiesdj") + ", " + rsldpc.getString("jiakhj")
                            + ", " + rsldpc.getString("jiaksk") + ", " + rsldpc.getString("jiashj") + ", " + rsldpc.getString("biaomdj")
                            + ", " + rsldpc.getString("buhsbmdj") + ", " + rsldpc.getString("leib") + ", " + rsldpc.getString("hetj")
                            + ", " + rsldpc.getString("qnetar") + ", " + rsldpc.getString("std") + ", " + rsldpc.getString("stad")
                            + ", " + rsldpc.getString("star") + ", " + rsldpc.getString("vdaf") + ", " + rsldpc.getString("mt")
                            + ", " + rsldpc.getString("mad") + ", " + rsldpc.getString("aad") + ", " + rsldpc.getString("ad")
                            + ", " + rsldpc.getString("aar") + ", " + rsldpc.getString("vad") + ", " + rsldpc.getString("zongje")
                            + ", (select id from meikxxb where bianm ='" + rsldpc.getString("meikxxb_id") + "'), (select id from chezxxb where bianm ='" + rsldpc.getString("faz_id") + "'), " + rsldpc.getString("chaokdl")
                            + ", " + rsldpc.getString("jiajqdj") + ") \n";
                }
                rsldpc.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update " + TableName + " set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("上传结算单失败：" + resul[0]);
                    return;
                }
            }
        }

        if (TableName.equals("kuangfjsmkb")
                || TableName.equals("kuangfjsyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                String sql = "";
                if (TableName.equals("kuangfjsmkb")) {
                    sql =
                            "select j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukmk,\n" +
                                    "       j.hansmk,\n" +
                                    "       j.buhsmk,\n" +
                                    "       j.meikje,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       h.hetbh as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       j.jiesfrl,\n" +
                                    "       jhkj.bianm as jihkjb_id,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as  meikxxb_id,\n" +
                                    "       j.hetj,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.zhiljq,\n" +
                                    "       j.qiyf,\n" +
                                    "       j.jiesrl,\n" +
                                    "       j.jieslf,\n" +
                                    "       j.jiesrcrl,\n" +
                                    "       j.liucgzbid,\n" +
                                    "       j.ruzry,\n" +
                                    "       j.fuid,\n" +
                                    "       j.fengsjj,\n" +
                                    "       j.jiajqdj,\n" +
                                    "       j.jijlx,\n" +
                                    "		nvl(j.diancjsmkb_id,0) as diancjsmkb_id,\n" +
                                    "		nvl(j.yufkje,0) as yufkje,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.chaokdl,0) as chaokdl,\n" +
                                    "		nvl(j.chaokdlx,'') as chaokdlx, \n" +
                                    "		nvl(j.Yunfhsdj,0) as Yunfhsdj, \n" +
                                    "		nvl(j.hansyf,0) as hansyf, \n" +
                                    "		nvl(j.buhsyf,0) as buhsyf, \n" +
                                    "		nvl(j.yunfjsl,0) as yunfjsl, \n" +
                                    "       nvl(j.zhuangt,0) as zhuangt	\n" +
                                    "  from " + TableName + " j,gongysb g,hetb h,jihkjb jhkj,meikxxb m \n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.jihkjb_id = jhkj.id\n" +
                                    "        and j.hetb_id = h.id \n" +
                                    "        and j.meikxxb_id = m.id";

                } else if (TableName.equals("kuangfjsyfb")) {
                    sql =
                            "select j.gongysmc,\n" +
                                    "       j.yunsfsb_id,\n" +
                                    "       j.yunj,\n" +
                                    "       j.yingd,\n" +
                                    "       j.kuid,\n" +
                                    "       j.faz,\n" +
                                    "       j.fahksrq,\n" +
                                    "       j.fahjzrq,\n" +
                                    "       j.meiz,\n" +
                                    "       j.daibch,\n" +
                                    "       j.yuanshr,\n" +
                                    "       j.xianshr,\n" +
                                    "       j.yansksrq,\n" +
                                    "       j.yansjzrq,\n" +
                                    "       j.yansbh,\n" +
                                    "       j.shoukdw,\n" +
                                    "       j.kaihyh,\n" +
                                    "       j.zhangh,\n" +
                                    "       j.fapbh,\n" +
                                    "       j.fukfs,\n" +
                                    "       j.duifdd,\n" +
                                    "       j.ches,\n" +
                                    "       j.jiessl,\n" +
                                    "       j.guohl,\n" +
                                    "       j.yuns,\n" +
                                    "       j.koud,\n" +
                                    "       j.jiesslcy,\n" +
                                    "       j.guotyf,\n" +
                                    "       j.guotzf,\n" +
                                    "       j.kuangqyf,\n" +
                                    "       j.kuangqzf,\n" +
                                    "       j.jiskc,\n" +
                                    "       j.hansdj,\n" +
                                    "       j.bukyf,\n" +
                                    "       j.hansyf,\n" +
                                    "       j.buhsyf,\n" +
                                    "       j.shuik,\n" +
                                    "       j.shuil,\n" +
                                    "       j.buhsdj,\n" +
                                    "       j.jieslx,\n" +
                                    "       j.jiesrq,\n" +
                                    "       j.ruzrq,\n" +
                                    "       decode(j.jieslx,1,(select hetbh from hetb where id=j.hetb_id),(select hetbh from hetys where id=j.hetb_id)) as hetb_id,\n" +
                                    "       j.liucztb_id,\n" +
                                    "       j.liucgzid,\n" +
                                    "       nvl(j.kuangfjsmkb_id,0) as kuangfjsmkb_id,\n" +
                                    "       j.ranlbmjbr,\n" +
                                    "       j.ranlbmjbrq,\n" +
                                    "       j.beiz,\n" +
                                    "       nvl(j.guotyfjf,0) as guotyfjf, \n" +
                                    "       nvl(j.guotzfjf,0) as guotzfjf,\n" +
                                    "       nvl(j.gongfsl,0) as gongfsl,\n" +
                                    "       nvl(j.yanssl,0) as yanssl,\n" +
                                    "       nvl(j.yingk,0) as yingk,\n" +
                                    "       j.id,\n" +
                                    "       j.diancxxb_id,\n" +
                                    "       j.bianm,\n" +
                                    "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                                    "       nvl(j.ditzf,0) as ditzf,\n" +
                                    "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                    "       nvl(j.dityf,0) as dityf,\n" +
                                    "       j.meikdwmc,\n" +
                                    "       j.ruzry,\n" +
                                    "       nvl(j.fuid,0) as fuid,\n" +
                                    "		nvl(j.kuidjfyf,0) as kuidjfyf,\n" +
                                    "		nvl(j.kuidjfzf,0) as kuidjfzf,\n" +
                                    "		nvl(j.diancjsyfb_id,0) as diancjsyfb_id,	\n" +
                                    "		nvl(j.zhuangt,0) as zhuangt \n" +
                                    "  from " + TableName + " j,gongysb g,meikxxb m\n" +
                                    "  where j.id = " + TableID + "\n" +
                                    "        and j.gongysb_id = g.id\n" +
                                    "        and j.meikxxb_id = m.id";

                }
                ResultSetList rsl = con.getResultSetList(sql);

                sql = "select j.id, jiesdid, z.bianm as zhibb_id, hetbz,\n" +
                        "       	gongf, changf, jies, yingk, zhejbz, zhejje,\n" +
                        "       	zhuangt, yansbhb_id\n" +
                        "       from jieszbsjb j, zhibb z\n" +
                        "       where j.zhibb_id = z.id\n" +
                        " 			and j.jiesdid=" + TableID;
                ResultSetList rslzb = con.getResultSetList(sql);

                sql =
                        "select d.id,\n" +
                                "       d.xuh,\n" +
                                "       d.jiesdid,\n" +
                                "       z.bianm as zhibb_id,\n" +
                                "       d.hetbz,\n" +
                                "       d.gongf,\n" +
                                "       d.changf,\n" +
                                "       d.jies,\n" +
                                "       d.yingk,\n" +
                                "       d.zhejbz,\n" +
                                "       d.zhejje,\n" +
                                "       d.gongfsl,\n" +
                                "       d.yanssl,\n" +
                                "       d.jiessl,\n" +
                                "       d.koud,\n" +
                                "       d.kous,\n" +
                                "       d.kouz,\n" +
                                "       d.ches,\n" +
                                "       d.jingz,\n" +
                                "       d.koud_js,\n" +
                                "       d.yuns,\n" +
                                "       d.jiesslcy,\n" +
                                "       d.jiesdj,\n" +
                                "       d.jiakhj,\n" +
                                "       d.jiaksk,\n" +
                                "       d.jiashj,\n" +
                                "       d.biaomdj,\n" +
                                "       d.buhsbmdj,\n" +
                                "       d.leib,\n" +
                                "       d.hetj,\n" +
                                "       d.qnetar,\n" +
                                "       d.std,\n" +
                                "       d.stad,\n" +
                                "       d.star,\n" +
                                "       d.vdaf,\n" +
                                "       d.mt,\n" +
                                "       d.mad,\n" +
                                "       d.aad,\n" +
                                "       d.ad,\n" +
                                "       d.aar,\n" +
                                "       d.vad,\n" +
                                "       d.zongje,\n" +
                                "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as meikxxb_id,\n" +
                                "       c.bianm as faz_id,\n" +
                                "       d.chaokdl,\n" +
                                "       d.jiajqdj\n" +
                                "  from danpcjsmxb d,zhibb z," + TableName + " j,meikxxb m,chezxxb c\n" +
                                "  where d.zhibb_id = z.id\n" +
                                "        and d.jiesdid = j.id\n" +
                                "        and d.meikxxb_id = m.id\n" +
                                "        and d.faz_id = c.id\n" +
                                "		 and d.jiesdid=" + TableID;
                ResultSetList rsldpc = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows() + rslzb.getRows() + rsldpc.getRows() + 3];    //加三行删除语句
                int j = 0;
                sqls[j] = "delete from " + TableName + " where id=" + TableID;

                while (rsl.next()) {
                    j++;

                    if (TableName.equals("kuangfjsmkb")) {

                        sqls[j] = "insert into kuangfjsmkb	\n"
                                + " (id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, \n"
                                + " buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, ranlbmjbr, ranlbmjbrq, beiz, jiesfrl, jihkjb_id, meikxxb_id, hetj, meikdwmc, zhiljq, qiyf, jiesrl, jieslf, jiesrcrl, liucgzbid, ruzry, fuid, fengsjj, jiajqdj, jijlx, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, diancjsmkb_id, yufkje, Yunfhsdj, hansyf, \n"
                                + " buhsyf, yunfjsl, zhuangt)	\n"
                                + " values	\n"
                                + " (" + rsl.getLong("id") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("bianm")
                                + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), '" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id")
                                + ", '" + rsl.getString("yunj") + "', " + rsl.getDouble("yingd") + ", " + rsl.getDouble("kuid") + ", '" + rsl.getString("faz")
                                + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq")) + "','yyyy-mm-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("meiz")
                                + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq")) + "','yyyy-mm-dd'),to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-mm-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw")
                                + "', '" + rsl.getString("kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs") + "', '" + rsl.getString("duifdd")
                                + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy")
                                + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukmk") + ", " + rsl.getString("hansmk") + ", " + rsl.getString("buhsmk") + ", " + rsl.getString("meikje") + ", " + rsl.getString("shuik")
                                + ", " + rsl.getString("shuil") + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq")))
                                + "','yyyy-MM-dd'), (select id from hetb where hetbh = '" + rsl.getString("hetb_id") + "'), " + yuancljd[1] + ", " + rsl.getString("liucgzid") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq"))
                                + "','yyyy-MM-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("jiesfrl") + ", (select id from jihkjb where bianm = '" + rsl.getString("jihkjb_id") + "'), (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("hetj") + ", '" + rsl.getString("meikdwmc")
                                + "', '" + rsl.getString("zhiljq") + "', " + rsl.getString("qiyf") + ", " + rsl.getString("jiesrl") + ", " + rsl.getString("jieslf") + ", " + rsl.getString("jiesrcrl") + ", " + rsl.getString("liucgzbid") + ", '" + rsl.getString("ruzry")
                                + "', " + rsl.getLong("fuid") + ", " + rsl.getString("fengsjj") + ", " + rsl.getString("jiajqdj") + ", " + rsl.getString("jijlx") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getString("chaokdl") + ", '"
                                + rsl.getString("chaokdlx") + "'," + rsl.getString("diancjsmkb_id") + "," + rsl.getDouble("yufkje") + "," + rsl.getString("Yunfhsdj") + "," + rsl.getString("hansyf") + "," + rsl.getString("buhsyf") + "," + rsl.getString("yunfjsl") + ",0 )\n";

                    } else if (TableName.equals("kuangfjsyfb")) {

                        sqls[j] = "insert into kuangfjsyfb	\n"
                                + " (gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, \n"
                                + " ruzrq, hetb_id, liucztb_id, liucgzid, kuangfjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, guotyfjf, guotzfjf, gongfsl, yanssl, yingk, id, diancxxb_id, bianm, gongysb_id, ditzf, meikxxb_id, dityf, meikdwmc, ruzry, fuid, kuidjfyf, kuidjfzf, diancjsyfb_id, zhuangt)	\n"
                                + " values	\n"
                                + " ('" + rsl.getString("gongysmc") + "', " + rsl.getLong("yunsfsb_id") + ", '" + rsl.getString("yunj") + "', " + rsl.getString("yingd") + ", " + rsl.getString("kuid") + ", '" + rsl.getString("faz") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("fahksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("fahjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("meiz") + "', '" + rsl.getString("daibch") + "', '" + rsl.getString("yuanshr") + "', '" + rsl.getString("xianshr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("yansksrq"))
                                + "','yyyy-MM-dd'), to_date('" + DateUtil.FormatDate(rsl.getDate("yansjzrq")) + "','yyyy-MM-dd'), '" + rsl.getString("yansbh") + "', '" + rsl.getString("shoukdw") + "', '" + rsl.getString("v_kaihyh") + "', '" + rsl.getString("zhangh") + "', '" + rsl.getString("fapbh") + "', '" + rsl.getString("fukfs")
                                + "', '" + rsl.getString("duifdd") + "', " + rsl.getString("ches") + ", " + rsl.getString("jiessl") + ", " + rsl.getString("guohl") + ", " + rsl.getString("yuns") + ", " + rsl.getString("koud") + ", " + rsl.getString("jiesslcy") + ", " + rsl.getString("guotyf") + ", " + rsl.getString("guotzf")
                                + ", " + rsl.getString("kuangqyf") + ", " + rsl.getString("kuangqzf") + ", " + rsl.getString("jiskc") + ", " + rsl.getString("hansdj") + ", " + rsl.getString("bukyf") + ", " + rsl.getString("hansyf") + ", " + rsl.getString("buhsyf") + ", " + rsl.getString("shuik") + ", " + rsl.getString("shuil")
                                + ", " + rsl.getString("buhsdj") + ", " + rsl.getString("jieslx") + ", to_date('" + DateUtil.FormatDate(rsl.getDate("jiesrq")) + "','yyyy-MM-dd'), to_date('" + (rsl.getString("ruzrq").equals("") ? "" : DateUtil.FormatDate(rsl.getDate("ruzrq"))) + "','yyyy-MM-dd'), (select id from hetb where hetbh='" + rsl.getString("hetb_id") + "'), " + yuancljd[1] + ", " + rsl.getString("liucgzid")
                                + ", " + rsl.getString("kuangfjsmkb_id") + ", '" + rsl.getString("ranlbmjbr") + "', to_date('" + DateUtil.FormatDate(rsl.getDate("ranlbmjbrq")) + "','yyyy_mm-dd'), '" + rsl.getString("beiz") + "', " + rsl.getString("guotyfjf") + ", " + rsl.getString("guotzfjf") + ", " + rsl.getString("gongfsl") + ", " + rsl.getString("yanssl")
                                + ", " + rsl.getString("yingk") + ", " + rsl.getString("id") + ", " + rsl.getString("diancxxb_id") + ", '" + rsl.getString("bianm") + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "'), " + rsl.getString("ditzf") + ", (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "'), " + rsl.getString("dityf") + ", '" + rsl.getString("meikdwmc")
                                + "','" + rsl.getString("ruzry") + "', " + rsl.getString("fuid") + ", " + rsl.getString("kuidjfyf") + ", " + rsl.getString("kuidjfzf") + ", " + rsl.getLong("diancjsyfb_id") + ", 0) \n";
                    }
                }
                rsl.close();

                sqls[++j] = "delete from jieszbsjb where jiesdid=" + TableID;
                while (rslzb.next()) {
                    j++;
                    sqls[j] = "insert into jieszbsjb	\n"
                            + " (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)	\n"
                            + " values	\n"
                            + " (" + rslzb.getLong("id") + ", " + rslzb.getLong("jiesdid") + ", (select id from zhibb where bianm='" + rslzb.getString("zhibb_id") + "'), '" + rslzb.getString("hetbz")
                            + "', " + rslzb.getString("gongf") + ", " + rslzb.getString("changf") + ", " + rslzb.getString("jies") + ", " + rslzb.getString("yingk")
                            + ", " + rslzb.getString("zhejbz") + ", " + rslzb.getString("zhejje") + ", " + rslzb.getString("zhuangt") + ", " + rslzb.getString("yansbhb_id") + ") \n";
                }
                rslzb.close();

                sqls[++j] = "delete from danpcjsmxb where jiesdid=" + TableID;
                while (rsldpc.next()) {
                    j++;
                    sqls[j] = "insert into danpcjsmxb	\n"
                            + " (id,xuh,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,gongfsl,yanssl,jiessl,koud,kous,kouz,ches,jingz,koud_js," +
                            "yuns,jiesslcy,jiesdj,jiakhj,jiaksk,jiashj,biaomdj,buhsbmdj,leib,hetj,qnetar,std,stad," +
                            "star,vdaf,mt,mad,aad,ad,aar,vad,zongje,meikxxb_id,faz_id,chaokdl,jiajqdj)	\n"
                            + " values	\n"
                            + " (" + rsldpc.getLong("id") + ", " + rsldpc.getString("xuh") + ", " + rsldpc.getString("jiesdid") + ", (select id from zhibb where bianm ='" + rsldpc.getString("zhibb_id") + "'), '" + rsldpc.getString("hetbz")
                            + "', " + rsldpc.getString("gongf") + ", " + rsldpc.getString("changf") + ", " + rsldpc.getString("jies") + ", " + rsldpc.getString("yingk")
                            + ", " + rsldpc.getString("zhejbz") + ", " + rsldpc.getString("zhejje") + ", " + rsldpc.getString("gongfsl")
                            + ", " + rsldpc.getString("yanssl") + ", " + rsldpc.getString("jiessl") + ", " + rsldpc.getString("koud")
                            + ", " + rsldpc.getString("kous") + ", " + rsldpc.getString("kouz") + ", " + rsldpc.getString("ches")
                            + ", " + rsldpc.getString("jingz") + ", " + rsldpc.getString("koud_js") + ", " + rsldpc.getString("yuns")
                            + ", " + rsldpc.getString("jiesslcy") + ", " + rsldpc.getString("jiesdj") + ", " + rsldpc.getString("jiakhj")
                            + ", " + rsldpc.getString("jiaksk") + ", " + rsldpc.getString("jiashj") + ", " + rsldpc.getString("biaomdj")
                            + ", " + rsldpc.getString("buhsbmdj") + ", " + rsldpc.getString("leib") + ", " + rsldpc.getString("hetj")
                            + ", " + rsldpc.getString("qnetar") + ", " + rsldpc.getString("std") + ", " + rsldpc.getString("stad")
                            + ", " + rsldpc.getString("star") + ", " + rsldpc.getString("vdaf") + ", " + rsldpc.getString("mt")
                            + ", " + rsldpc.getString("mad") + ", " + rsldpc.getString("aad") + ", " + rsldpc.getString("ad")
                            + ", " + rsldpc.getString("aar") + ", " + rsldpc.getString("vad") + ", " + rsldpc.getString("zongje")
                            + ", (select id from meikxxb where bianm ='" + rsldpc.getString("meikxxb_id") + "'), (select id from chezxxb where bianm ='" + rsldpc.getString("faz_id") + "'), " + rsldpc.getString("chaokdl")
                            + ", " + rsldpc.getString("jiajqdj") + ") \n";
                }
                rsldpc.close();

                String[] resul = null;
                InterCom_dt Shangb = new InterCom_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update " + TableName + " set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("上传结算单失败：" + resul[0]);
                    return;
                }
            }
        }

//				注 : 目前 只支持 下发功能
        if (TableName.equals("diancjsmkb")
                || TableName.equals("diancjsyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据

//						yuancxjd[0]=rs.getString("diancxxb_id_next");
//						yuancxjd[1]=rs.getString("liucztb_id_next");
//						yuancxjd[2]=rs.getString("zhuangt");

                long id = 0;
                try {
                    if (TableName.equals("diancjsmkb")) {
                        id = MainGlobal.getTableId("jiesb", "diancjsmkb_id"
                                , TableID + "");
                    } else {
                        id = MainGlobal.getTableId("jiesyfb", "diancjsyfb_id"
                                , TableID + "");
                    }

                } catch (Exception e) {
                    // TODO 自动生成 catch 块
                    e.printStackTrace();
                }

                String sql = "";
                if (TableName.equals("diancjsmkb")) {
                    sql = "select * from jiesb where id=" + id;
                } else {
                    sql = "select * from jiesyfb where id=" + id;
                }

                ResultSetList rsl = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows()];
                int j = -1;

                while (rsl.next()) {
                    j++;
//							更新厂级系统的jiesb/jiesyfb中的liucztb_id
                    if (TableName.equals("diancjsmkb")) {

                        sqls[j] = " update jiesb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");

                    } else if (TableName.equals("diancjsyfb")) {

                        sqls[j] = " update jiesyfb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");
                    }
                }
                rsl.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2
                    //更新本地数据库diancjsmkb/diancjsyfb 的 liucztb_id 为下一个流程动作
                    StringBuffer bf = new StringBuffer();
                    bf.append(" update " + TableName + " set liucztb_id=" + yuancljd[2] + " where id=" + TableID + "\n");
                    con.getUpdate(bf.toString());
                    return;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    //		return false;
                }

            }
        }
//				2009-6-17日增加hetys的上传下发功能
        if (TableName.equals("hetys")) {
            String[] yuancljd = getXianjd(TableName, TableID, "提交");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetysb_id = String.valueOf(TableID);
                String[] resul = null;
                ResultSetList rsl = null;
                String[] sqls = new String[1];
                StringBuffer sbExeSql = new StringBuffer("begin	\n");
//							主合同信息
                String sql =
                        "select ht.id, ht.mingc, fuid, ht.diancxxb_id, hetbh, to_char(qiandrq,'yyyy-MM-dd') as qiandrq, qianddd,\n" +
                                "       y.bianm as yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh,\n" +
                                "       gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh,\n" +
                                "       xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, to_char(qisrq,'yyyy-MM-dd') as qisrq,\n" +
                                "       to_char(guoqrq,'yyyy-MM-dd') as guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid\n" +
                                "       from hetys ht,yunsdwb y\n" +
                                "       where ht.yunsdwb_id = y.id\n" +
                                "             and ht.id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetys where id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetys\n" +
                            "  (id, mingc, fuid, diancxxb_id, hetbh, qiandrq, qianddd, yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh, gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh, xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, qisrq, guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid)\n" +
                            "	values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("mingc") + "', " + rsl.getLong("fuid") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("hetbh") + "', to_date('" + rsl.getString("qiandrq") + "','yyyy-MM-dd'), '" + rsl.getString("qianddd") + "', (select id from yunsdwb where bianm='"
                            + rsl.getString("yunsdwb_id") + "'), '" + rsl.getString("gongfdwmc") + "', '" + rsl.getString("gongfdwdz") + "', '" + rsl.getString("gongfdh") + "', '" + rsl.getString("gongffddbr") + "', '" + rsl.getString("gongfwtdlr") + "', '" + rsl.getString("gongfdbgh") + "', '" + rsl.getString("gongfkhyh")
                            + "', '" + rsl.getString("gongfzh") + "', '" + rsl.getString("gongfyzbm") + "', '" + rsl.getString("gongfsh") + "', '" + rsl.getString("xufdwmc") + "', '" + rsl.getString("xufdwdz") + "', '" + rsl.getString("xuffddbr") + "', '" + rsl.getString("xufwtdlr") + "', '" + rsl.getString("xufdh")
                            + "', '" + rsl.getString("xufdbgh") + "', '" + rsl.getString("xufkhyh") + "', '" + rsl.getString("xufzh") + "', '" + rsl.getString("xufyzbm") + "', '" + rsl.getString("xufsh") + "', to_date('" + rsl.getString("qisrq") + "','yyyy-MM-dd'), to_date('" + rsl.getString("guoqrq")
                            + "','yyyy-MM-dd'), " + rsl.getLong("hetys_mb_id") + ", '" + rsl.getString("meikmcs") + "', " + yuancljd[1] + ", " + rsl.getLong("liucgzid") + ");\n"
                    );
                }
//							价格信息
                sql =
                        "select jg.id, hetys_id, m.bianm as meikxxb_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id,\n" +
                                "       shangx, xiax, dw.bianm as danwb_id, yunja, yjdw.bianm as yunjdw_id\n" +
                                "       from hetysjgb jg, meikxxb m, zhibb zb, tiaojb tj, danwb dw, danwb yjdw\n" +
/*
*huochaoyuan
*2009-10-22增加条件的右连接
*/
                                "       where jg.meikxxb_id = m.id(+) and jg.zhibb_id = zb.id(+) and jg.tiaojb_id = tj.id(+)\n" +
                                "             and jg.danwb_id = dw.id(+) and jg.yunjdw_id = yjdw.id(+)\n" +
//
                                "             and jg.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetysjgb where hetys_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetysjgb\n" +
                                    "	(id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id)\n" +
                                    "	values\n" +
                                    "	(" + rsl.getLong("id") + ", " + rsl.getLong("hetys_id") + ", (select id from meikxxb where bianm='"
                                    + rsl.getString("meikxxb_id") + "'), (select id from zhibb where bianm='"
                                    + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='"
                                    + rsl.getString("tiaojb_id") + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax")
/*
*huochaoyuan
*2009-10-22添加条件zhibb_id!=0，区分danwb中有编码相同的项；
*/
                                    + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "' and zhibb_id!=0), " + rsl.getString("yunja")
                                    + ", (select id from danwb where bianm='" + rsl.getString("yunjdw_id") + "' and zhibb_id!=0));\n"
//end
                    );
                }
/*
*huochaoyuan
*2009-10-22运输合同添加收货人，但上传/下发未做处理，故添加如下语句；
*/
                sql = "select s.id,s.hetysb_id,s.shouhr_id from hetysshrb s  where s.hetysb_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);
                sbExeSql.append("	delete from hetysshrb where hetysb_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("insert into hetysshrb\n" +
                            "  (id, hetysb_id, shouhr_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", " + rsl.getLong("hetysb_id") + ", " + rsl.getLong("shouhr_id") + ");\n"
                    );
                }
//							end
//							增扣款
                sql = "select zkk.id, jis, jsdw.bianm as jisdwid, kouj, kjdw.bianm as koujdw, zengfj, zfjdw.bianm as zengfjdw,\n" +
                        "       xiaoscl, jizzkj, jizzb, czxm.bianm as canzxm, czxmdw.bianm as canzxmdw, canzsx, canzxx, jsxs.bianm as hetjsxsb_id,\n" +
                        "       ysfs.mingc as yunsfsb_id, zkk.beiz, hetys_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id, shangx, xiax,\n" +
                        "       dw.bianm as danwb_id\n" +
                        "       from hetyszkkb zkk, danwb jsdw, danwb kjdw, danwb zfjdw, zhibb jzzb, zhibb czxm, danwb czxmdw, hetjsxsb jsxs,\n" +
                        "            yunsfsb ysfs, zhibb zb, tiaojb tj,danwb dw\n" +
                        "       where zkk.jisdwid = jsdw.id(+) and zkk.koujdw = kjdw.id (+) and zkk.zengfjdw = zfjdw.id(+)\n" +
                        "             and zkk.jizzb = jzzb.id(+) and zkk.canzxm = czxm.id(+) and zkk.canzxmdw = czxmdw.id(+)\n" +
                        "             and zkk.hetjsxsb_id = jsxs.id and zkk.yunsfsb_id = ysfs.id and zkk.zhibb_id = zb.id\n" +
                        "             and zkk.tiaojb_id = tj.id(+) and zkk.danwb_id = dw.id (+) and zkk.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyszkkb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyszkkb\n" +
                                    "  (id, jis, jisdwid, kouj, koujdw, zengfj, zengfjdw, xiaoscl, jizzkj, jizzb, canzxm, canzxmdw, canzsx, canzxx, hetjsxsb_id, yunsfsb_id, beiz, hetys_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id)\n" +
                                    "values\n" +
                                    "  (" + rsl.getLong("id") + ", " + rsl.getString("jis") + ", (select id from danwb where bianm='" + rsl.getString("jisdwid") + "'), " + rsl.getString("kouj") + ", (select id from danwb where bianm='"
                                    + rsl.getString("koujdw") + "'), " + rsl.getString("zengfj") + ", (select id from danwb where bianm='" + rsl.getString("zengfjdw") + "'), " + rsl.getString("xiaoscl") + ", " + rsl.getString("jizzkj")
                                    + ", " + rsl.getString("jizzb") + ", (select id from zhibb where bianm='" + rsl.getString("canzxm") + "'), (select id from danwb where bianm='" + rsl.getString("canzxmdw") + "'), " + rsl.getString("canzsx")
                                    + ", " + rsl.getString("canzxx") + ", (select id from hetjsxsb where bianm='" + rsl.getString("hetjsxsb_id") + "'), (select id from yunsfsb where mingc='" + rsl.getString("yunsfsb_i")
                                    + "'), '" + rsl.getString("beiz") + "', " + rsl.getLong("hetys_id") + ", (select id from zhibb where bianm='" + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='" + rsl.getString("tiaojb_id")
/*
*huochaoyuan
*2009-10-22添加条件zhibb_id!=0，区分danwb中有编码相同的项；
*/
                                    + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax") + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "'  and zhibb_id!=0));\n"
//end
                    );
                }
//							文字
                sql = "select id, wenznr, hetys_id from hetyswzb where hetys_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyswzb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyswzb(id, wenznr, hetys_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("wenznr") + "', " + rsl.getLong("hetys_id") + ");\n"
                    );
                }

                if (rsl != null) {

                    rsl.close();
                }

                sbExeSql.append("end;");
                sqls[0] = sbExeSql.toString();

                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update hetys set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return;//没有日志
                } else {
//								strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("上传失败：" + resul[0]);
                    return;
                }
            }
        }

        //
        String sql = "";
        long dangqdz = -1; //表单当前状态
        long liuczthjid = 0;
        long liucztqqid = 0;//新增  前驱状态  判断是否衔接  反射
        long liucdzb_id = 0;
        String qianqmc = "";
        String houjmc = "";
        String caoz = "";
        String dongz = "";//新增  动作  判断是否衔接  反射
        long liucjsb_id = 0;//新增  流程角色表  判断是否衔接  反射
        long leibztb_id = -1;
        try {
            sql = "select liucztb_id\n" +//获得当前状态
                    "from " + TableName + "\n" +
                    "where id=" + TableID;
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {
                dangqdz = rs.getLong(1);
            }
            sql = "select liucdzb.id,liucdzb.liuczthjid,liucdzb.liucztqqid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucdzb.dongz,liucztb2.leibztb_id,\n" +
                    "(select liucjsb_id from liucdzjsb where liucdzb_id=liucdzb.id) liucjsb_id \n" +
                    "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n" +
                    "where liucdzb.liucztqqid=liucztb1.id\n" +
                    " and liucdzb.liuczthjid=liucztb2.id\n" +
                    " and liucztb1.leibztb_id=leibztb1.id\n" +
                    " and liucztb2.leibztb_id=leibztb2.id\n" +
                    " and liucdzb.liucztqqid=" + dangqdz +
                    "  and liucztb1.xuh<liucztb2.xuh";
            ResultSet rs1 = con.getResultSet(sql);
            if (rs1.next()) {
                liuczthjid = rs1.getLong("liuczthjid");
                liucztqqid = rs1.getLong("liucztqqid");
                liucjsb_id = rs1.getLong("liucjsb_id");
                liucdzb_id = rs1.getLong("id");
                qianqmc = rs1.getString("qianqmc");
                houjmc = rs1.getString("houjmc");
                caoz = rs1.getString("caoz");
                dongz = rs1.getString("dongz");
                leibztb_id = rs1.getLong("leibztb_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //日志
//			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij," +
                "miaos)values(xl_xul_id.nextval,"
                + TableID
                + ","
                + liucdzb_id
                + ",'"
                + qianqmc
                + "','"
                + houjmc
                + "','"
                + caoz
                + "',(select quanc from renyxxb where id="
                + renyxxb_id
                + "),sysdate"
                + ",'"
                + xiaox
                + "')";
        con.getInsert(sql);
        if (leibztb_id == 0 || leibztb_id == 1) {//如果后继类别状态为0或１即为回退
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + leibztb_id
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        } else {//如果后继为
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + liuczthjid
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        }

        con.getUpdate(sql);

        liucxjdzcl(con, TableName, TableID, liucjsb_id, qianqmc, houjmc, caoz, dongz);

    }

    public static boolean huit(JDBCcon con, String TableName, long TableID, long renyxxb_id, String xiaox) {//回退

//			回退
//			先判断是否存在衔接点

        if (TableName.equals("hetb")) {
            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetb_id = String.valueOf(TableID);
                String diancxxb_id = null;
                String[] resul = null;
                String hetbh = "";
                String sql = "select hetb.ID,hetb.FUID,DIANCXXB_ID,HETBH,to_char(QIANDRQ,'yyyy-mm-dd')QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                        "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,gongysb.bianm HETGYSBID,\n" +
                        " gs.bianm GONGYSB_ID,to_char(QISRQ,'yyyy-mm-dd')QISRQ,to_char(GUOQRQ,'yyyy-mm-dd')GUOQRQ,HETB_MB_ID,j.bianm JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,nvl(HETJJFSB_ID,0)HETJJFSB_ID,MEIKMCS\n" +
                        "from hetb,gongysb,gongysb gs,jihkjb j\n" +
                        "where hetb.jihkjb_id=j.id and hetb.HETGYSBID=gongysb.id and hetb.GONGYSB_ID=gs.id and hetb.id=" + hetb_id;
                ResultSetList rs1 = con.getResultSetList(sql);

                sql = "select hetslb.ID,pinzb.mingc PINZB_ID,YUNSFSB_ID,fz.mingc FAZ_ID,dz.mingc DAOZ_ID,DIANCXXB_ID,to_char(RIQ,'yyyy-mm-dd')riq,HETL,HETB_ID,hetslb.ZHUANGT\n" +
                        "from hetslb,pinzb,chezxxb fz,chezxxb dz\n" +
                        "where hetslb.faz_id=fz.id and hetslb.daoz_id=dz.id and hetslb.pinzb_id=pinzb.id and hetslb.hetb_id=" + hetb_id;
                ResultSetList rs2 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID\n" +
                        "from hetzlb\n" +
                        "where hetzlb.hetb_id=" + hetb_id;
                ResultSetList rs3 = con.getResultSetList(sql);

 /*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
                        "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX\n" +
//end
                        "from hetjgb\n" +
                        "where hetjgb.hetb_id=" + hetb_id;
                ResultSetList rs4 = con.getResultSetList(sql);

                sql = "select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                        "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID\n" +
                        "from hetzkkb\n" +
                        "where hetzkkb.hetb_id=" + hetb_id;
                ResultSetList rs5 = con.getResultSetList(sql);

                sql = "select ID,WENZNR,HETB_ID\n" +
                        "from hetwzb\n" +
                        "where hetwzb.hetb_id=" + hetb_id;
                ResultSetList rs6 = con.getResultSetList(sql);
                int len = rs1.getResultSetlist().size() + rs2.getResultSetlist().size() + rs3.getResultSetlist().size() + rs4.getResultSetlist().size()
                        + rs5.getResultSetlist().size() + rs6.getResultSetlist().size() + 6;//6为五个分表的删除
                String[] sqls = new String[len];
                int j = 0;
                sqls[j] = "delete from hetb where id=" + hetb_id;
                j++;
                while (rs1.next()) {
                    diancxxb_id = rs1.getString("DIANCXXB_ID");
                    hetbh = rs1.getString("HETBH");
                    sqls[j] = "insert into hetb(ID,FUID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,\n" +
                            "GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,\n" +
                            "GONGYSB_ID,QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS\n" +
                            ")values(\n" +
                            rs1.getString("ID") + "," +
                            rs1.getString("FUID") + "," +
                            diancxxb_id + ",'" +
                            hetbh + "',to_date('" +
                            rs1.getString("QIANDRQ") + "','yyyy-mm-dd'),'" +
                            rs1.getString("QIANDDD") + "','" +
                            rs1.getString("GONGFDWMC") + "','" +
                            rs1.getString("GONGFDWDZ") + "','" +
                            rs1.getString("GONGFDH") + "','" +
                            rs1.getString("GONGFFDDBR") + "','" +
                            rs1.getString("GONGFWTDLR") + "','" +
                            rs1.getString("GONGFDBGH") + "','" +
                            rs1.getString("GONGFKHYH") + "','" +
                            rs1.getString("GONGFZH") + "','" +
                            rs1.getString("GONGFYZBM") + "','" +
                            rs1.getString("GONGFSH") + "','" +
                            rs1.getString("XUFDWMC") + "','" +
                            rs1.getString("XUFDWDZ") + "','" +
                            rs1.getString("XUFFDDBR") + "','" +
                            rs1.getString("XUFWTDLR") + "','" +
                            rs1.getString("XUFDH") + "','" +
                            rs1.getString("XUFDBGH") + "','" +
                            rs1.getString("XUFKHYH") + "','" +
                            rs1.getString("XUFZH") + "','" +
                            rs1.getString("XUFYZBM") + "','" +
                            rs1.getString("XUFSH") + "',(select id from gongysb where bianm='" +
                            rs1.getString("HETGYSBID") + "' or shangjgsbm='" + rs1.getString("HETGYSBID") + "'),(select id from gongysb where bianm='" +
                            rs1.getString("GONGYSB_ID") + "' or shangjgsbm='" + rs1.getString("GONGYSB_ID") + "'),to_date('" +
                            rs1.getString("QISRQ") + "','yyyy-mm-dd'),to_date('" +
                            rs1.getString("GUOQRQ") + "','yyyy-mm-dd')," +
                            rs1.getString("HETB_MB_ID") + ",(select id from jihkjb where bianm='" +
                            rs1.getString("JIHKJB_ID") + "')," +
                            yuancljd[1] + "," +
                            rs1.getString("LIUCGZID") + "," +
                            rs1.getString("LEIB") + "," +
                            rs1.getString("HETJJFSB_ID") + ",'" +
                            rs1.getString("MEIKMCS") + "'" +
//							 rs1.getString("XIAF")+
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetslb where hetb_id=" + hetb_id;
                j++;
                while (rs2.next()) {
                    sqls[j] = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT)values(\n" +
                            rs2.getString("ID") + ",(select id from pinzb where mingc='" +
                            rs2.getString("PINZB_ID") + "')," +
                            rs2.getString("YUNSFSB_ID") + ",(select id from chezxxb where mingc='" +
                            rs2.getString("FAZ_ID") + "'),(select id from chezxxb where mingc='" +
                            rs2.getString("DAOZ_ID") + "')," +
                            rs2.getString("DIANCXXB_ID") + ",to_date('" +
                            rs2.getString("RIQ") + "','yyyy-mm-dd')," +
                            rs2.getString("HETL") + "," +
                            rs2.getString("HETB_ID") + "," +
                            rs2.getString("ZHUANGT") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetzlb where hetb_id=" + hetb_id;
                j++;
                while (rs3.next()) {
                    sqls[j] = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values(\n" +
                            rs3.getString("ID") + ",'" +
                            rs3.getString("ZHIBB_ID") + "','" +
                            rs3.getString("TIAOJB_ID") + "','" +
                            rs3.getString("SHANGX") + "','" +
                            rs3.getString("XIAX") + "','" +
                            rs3.getString("DANWB_ID") + "'," +
                            rs3.getString("HETB_ID") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetjgb where hetb_id=" + hetb_id;
                j++;
                while (rs4.next()) {
                    sqls[j] = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
/*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,JIJLX)values(\n" +
//	end
                            rs4.getString("ID") + ",'" +
                            rs4.getString("ZHIBB_ID") + "','" +
                            rs4.getString("TIAOJB_ID") + "','" +
                            rs4.getString("SHANGX") + "','" +
                            rs4.getString("XIAX") + "','" +
                            rs4.getString("DANWB_ID") + "','" +
                            rs4.getString("JIJ") + "','" +
                            rs4.getString("JIJDWID") + "','" +
                            rs4.getString("HETJSFSB_ID") + "','" +
                            rs4.getString("HETJSXSB_ID") + "','" +
                            rs4.getString("YUNJ") + "','" +
                            rs4.getString("YUNJDW_ID") + "','" +
                            rs4.getString("YINGDKF") + "','" +
                            rs4.getString("YUNSFSB_ID") + "','" +
                            rs4.getString("ZUIGMJ") + "','" +
                            rs4.getString("HETB_ID") + "','" +
 /*
*huochaoyuan
*2009-10-22 合同价格表中添加新字段hetjgb.jijlx(计价类型，含税/不含税)上传/回退时未考虑，故添加；
*/
                            rs4.getString("HETJJFSB_ID") + "','" +
                            rs4.getString("JIJLX") +
//	end
                            "')";
                    j++;
                }
                sqls[j] = "delete from hetzkkb where hetb_id=" + hetb_id;
                j++;
                while (rs5.next()) {
                    sqls[j] = "insert into hetzkkb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,\n" +
                            "JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID)values(\n" +
                            rs5.getString("ID") + ",'" +
                            rs5.getString("ZHIBB_ID") + "','" +
                            rs5.getString("TIAOJB_ID") + "','" +
                            rs5.getString("SHANGX") + "','" +
                            rs5.getString("XIAX") + "','" +
                            rs5.getString("DANWB_ID") + "','" +
                            rs5.getString("JIS") + "','" +
                            rs5.getString("JISDWID") + "','" +
                            rs5.getString("KOUJ") + "','" +
                            rs5.getString("KOUJDW") + "','" +
                            rs5.getString("ZENGFJ") + "','" +
                            rs5.getString("ZENGFJDW") + "','" +
                            rs5.getString("XIAOSCL") + "','" +
                            rs5.getString("JIZZKJ") + "','" +
                            rs5.getString("JIZZB") + "','" +
                            rs5.getString("CANZXM") + "','" +
                            rs5.getString("CANZXMDW") + "','" +
                            rs5.getString("CANZSX") + "','" +
                            rs5.getString("CANZXX") + "','" +
                            rs5.getString("HETJSXSB_ID") + "','" +
                            rs5.getString("YUNSFSB_ID") + "','" +
                            rs5.getString("BEIZ") + "'," +
                            rs5.getString("HETB_ID") +
                            ")";
                    j++;
                }
                sqls[j] = "delete from hetwzb where hetb_id=" + hetb_id;
                j++;
                while (rs6.next()) {
                    sqls[j] = "insert into hetwzb(ID,WENZNR,HETB_ID)values(\n" +
                            rs6.getString("ID") + ",'" +
                            rs6.getString("WENZNR") + "'," +
                            rs6.getString("HETB_ID") +
                            ")";
                    j++;
                }
//						String strSuc="";//成功的合同编号
                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update  hetb set liucztb_id=" + yuancljd[2] + "  where id=" + TableID;
                    con.getUpdate(sql1);
                    return true;
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    return false;
                }
            }
        }

        if (TableName.equals("jiesb")
                || TableName.equals("jiesyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据

                String sql = "select * from " + TableName + " where id=" + TableID;
                ResultSetList rsl = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows()];
                int j = -1;
//					更新厂级的结算单流程状态
                while (rsl.next()) {
                    j++;

                    if (TableName.equals("jiesb")) {

                        sqls[j] = " update jiesb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");

                    } else if (TableName.equals("jiesyfb")) {

                        sqls[j] = " update jiesyfb set liucztb_id=" + yuancljd[1] + " where id=" + rsl.getLong("id");
                    }
                }
                rsl.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

//						操作本层数据
                    StringBuffer bf = new StringBuffer();
                    bf.append(" begin \n");
                    if (TableName.equals("jiesb")) {

                        bf.append(" delete from diancjsmkb where id in (select nvl(diancjsmkb_id,0) as id from jiesb where id=" + TableID + ");\n");
                        bf.append(" delete from diancjsmkb where id in (select nvl(diancjsmkb_id,0) as id from jiesb where fuid=" + TableID + ");\n");
                        bf.append(" delete from danpcjsmxb where jiesdid=" + TableID + ";	\n");
                        bf.append(" delete from danpcjsmxb where jiesdid in (select id from jiesb where fuid=" + TableID + ");	\n");

                    } else {

                        bf.append(" delete from diancjsyfb where id in (select nvl(diancjsyfb_id,0) as id from jiesyfb where id=" + TableID + ");\n");
                        bf.append(" delete from diancjsyfb where id in (select nvl(diancjsyfb_id,0) as id from jiesyfb where fuid=" + TableID + ");\n");
                    }

                    bf.append(" delete from " + TableName + " where id=" + TableID + ";		\n");
                    bf.append(" delete from " + TableName + " where fuid in (" + TableID + ");		\n");
                    bf.append(" delete from jieszbsjb where jiesdid=" + TableID + ";	\n");
                    bf.append(" delete from jieszbsjb where jiesdid in (select id from " + TableName + " where fuid=" + TableID + ");	\n");
                    //------------------------指标表 和哪个表对应

                    bf.append(" end;\n");

                    con.getUpdate(bf.toString());
                    return true;//没有日志
                } else {
//						strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("回退失败：" + resul[0]);
                    return false;
                }

            }
        }
        if (TableName.equals("kuangfjsmkb")
                || TableName.equals("kuangfjsyfb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据

                String sql = "select * from " + TableName + " where id=" + TableID;
                ResultSetList rsl = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows()];
                int j = -1;
//					更新厂级的结算单流程状态
                while (rsl.next()) {
                    j++;
                    sqls[j] = " update " + TableName + " set liucztb_id=" + yuancljd[1] + " where id=" + TableID;
                }
                rsl.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt();    //实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    //1置当前数据状态为2
//						如果厂级数据更新成功，则删除分公司层数据
                    StringBuffer bf = new StringBuffer();
                    bf.append(" begin \n");

                    bf.append(" delete from ").append(TableName).append(" where id=").append(TableID).append(";\n");
                    bf.append(" delete from jieszbsjb where jiesdid=").append(TableID).append(";\n");
                    bf.append(" delete from danpcjsmxb where jiesdid=").append(TableID).append(";\n");

                    bf.append(" end;\n");

                    con.getUpdate(bf.toString());
                    return true;//没有日志
                } else {
//						strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("回退失败：" + resul[0]);
                    return false;
                }
            }
        }

        if (TableName.equals("hetys")) {
            String[] yuancljd = getXianjd(TableName, TableID, "回退");//远程连接点
            if (yuancljd != null) {//如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据
                //2，向远程衔接点发送数据
                String hetysb_id = String.valueOf(TableID);
                String[] resul = null;
                ResultSetList rsl = null;
                String[] sqls = new String[1];
                StringBuffer sbExeSql = new StringBuffer("begin	\n");
//						主合同信息
                String sql =
                        "select ht.id, ht.mingc, fuid, ht.diancxxb_id, hetbh, to_char(qiandrq,'yyyy-MM-dd') as qiandrq, qianddd,\n" +
                                "       y.bianm as yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh,\n" +
                                "       gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh,\n" +
                                "       xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, to_char(qisrq,'yyyy-MM-dd') as qisrq,\n" +
                                "       to_char(guoqrq,'yyyy-MM-dd') as guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid\n" +
                                "       from hetys ht,yunsdwb y\n" +
                                "       where ht.yunsdwb_id = y.id\n" +
                                "             and ht.id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetys where id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetys\n" +
                            "  (id, mingc, fuid, diancxxb_id, hetbh, qiandrq, qianddd, yunsdwb_id, gongfdwmc, gongfdwdz, gongfdh, gongffddbr, gongfwtdlr, gongfdbgh, gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh, xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, qisrq, guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid)\n" +
                            "	values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("mingc") + "', " + rsl.getLong("fuid") + ", " + rsl.getLong("diancxxb_id") + ", '" + rsl.getString("hetbh") + "', to_date('" + rsl.getString("qiandrq") + "','yyyy-MM-dd'), '" + rsl.getString("qianddd") + "', (select id from yunsdwb where bianm='"
                            + rsl.getString("yunsdwb_id") + "'), '" + rsl.getString("gongfdwmc") + "', '" + rsl.getString("gongfdwdz") + "', '" + rsl.getString("gongfdh") + "', '" + rsl.getString("gongffddbr") + "', '" + rsl.getString("gongfwtdlr") + "', '" + rsl.getString("gongfdbgh") + "', '" + rsl.getString("gongfkhyh")
                            + "', '" + rsl.getString("gongfzh") + "', '" + rsl.getString("gongfyzbm") + "', '" + rsl.getString("gongfsh") + "', '" + rsl.getString("xufdwmc") + "', '" + rsl.getString("xufdwdz") + "', '" + rsl.getString("xuffddbr") + "', '" + rsl.getString("xufwtdlr") + "', '" + rsl.getString("xufdh")
                            + "', '" + rsl.getString("xufdbgh") + "', '" + rsl.getString("xufkhyh") + "', '" + rsl.getString("xufzh") + "', '" + rsl.getString("xufyzbm") + "', '" + rsl.getString("xufsh") + "', to_date('" + rsl.getString("qisrq") + "','yyyy-MM-dd'), to_date('" + rsl.getString("guoqrq")
                            + "','yyyy-MM-dd'), " + rsl.getLong("hetys_mb_id") + ", '" + rsl.getString("meikmcs") + "', " + yuancljd[1] + ", " + rsl.getLong("liucgzid") + ");\n"
                    );
                }
//						价格信息
                sql =
                        "select jg.id, hetys_id, m.bianm as meikxxb_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id,\n" +
                                "       shangx, xiax, dw.bianm as danwb_id, yunja, yjdw.bianm as yunjdw_id\n" +
                                "       from hetysjgb jg, meikxxb m, zhibb zb, tiaojb tj, danwb dw, danwb yjdw\n" +
                                "       where jg.meikxxb_id = m.id(+) and jg.zhibb_id = zb.id(+) and jg.tiaojb_id = tj.id(+)\n" +
                                "             and jg.danwb_id = dw.id(+) and jg.yunjdw_id = yjdw.id(+)\n" +
                                "             and jg.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetysjgb where hetys_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("	insert into hetysjgb\n" +
                            "	(id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id)\n" +
                            "	values\n" +
                            "	(" + rsl.getLong("id") + ", " + rsl.getLong("hetys_id") + ", (select id from meikxxb where bianm='"
                            + rsl.getString("meikxxb_id") + "'), (select id from zhibb where bianm='"
                            + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='"
                            + rsl.getString("tiaojb_id") + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax")
                            + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "'and zhibb_id!=0), " + rsl.getString("yunja")
                            + ", (select id from danwb where bianm='" + rsl.getString("yunjdw_id") + "'and zhibb_id!=0));\n"
                    );
                }
/*
*huochaoyuan
*2009-10-22运输合同添加收货人，但上传/下发未做处理，故添加如下语句；
*/
                sql = "select s.id,s.hetysb_id,s.shouhr_id from hetysshrb s  where s.hetysb_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);
                sbExeSql.append("	delete from hetysshrb where hetysb_id=").append(hetysb_id).append(";\n");
                while (rsl.next()) {

                    sbExeSql.append("insert into hetysshrb\n" +
                            "  (id, hetysb_id, shouhr_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", " + rsl.getLong("hetysb_id") + ", " + rsl.getLong("shouhr_id") + ");\n"
                    );
                }
//						end
//						增扣款
                sql = "select zkk.id, jis, jsdw.bianm as jisdwid, kouj, kjdw.bianm as koujdw, zengfj, zfjdw.bianm as zengfjdw,\n" +
                        "       xiaoscl, jizzkj, jizzb, czxm.bianm as canzxm, czxmdw.bianm as canzxmdw, canzsx, canzxx, jsxs.bianm as hetjsxsb_id,\n" +
                        "       ysfs.mingc as yunsfsb_id, zkk.beiz, hetys_id, zb.bianm as zhibb_id, tj.bianm as tiaojb_id, shangx, xiax,\n" +
                        "       dw.bianm as danwb_id\n" +
                        "       from hetyszkkb zkk, danwb jsdw, danwb kjdw, danwb zfjdw, zhibb jzzb, zhibb czxm, danwb czxmdw, hetjsxsb jsxs,\n" +
                        "            yunsfsb ysfs, zhibb zb, tiaojb tj,danwb dw\n" +
                        "       where zkk.jisdwid = jsdw.id(+) and zkk.koujdw = kjdw.id (+) and zkk.zengfjdw = zfjdw.id(+)\n" +
                        "             and zkk.jizzb = jzzb.id(+) and zkk.canzxm = czxm.id(+) and zkk.canzxmdw = czxmdw.id(+)\n" +
                        "             and zkk.hetjsxsb_id = jsxs.id and zkk.yunsfsb_id = ysfs.id and zkk.zhibb_id = zb.id\n" +
                        "             and zkk.tiaojb_id = tj.id(+) and zkk.danwb_id = dw.id (+) and zkk.hetys_id =" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyszkkb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyszkkb\n" +
                            "  (id, jis, jisdwid, kouj, koujdw, zengfj, zengfjdw, xiaoscl, jizzkj, jizzb, canzxm, canzxmdw, canzsx, canzxx, hetjsxsb_id, yunsfsb_id, beiz, hetys_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", " + rsl.getString("jis") + ", (select id from danwb where bianm='" + rsl.getString("jisdwid") + "'), " + rsl.getString("kouj") + ", (select id from danwb where bianm='"
                            + rsl.getString("koujdw") + "'), " + rsl.getString("zengfj") + ", (select id from danwb where bianm='" + rsl.getString("zengfjdw") + "'), " + rsl.getString("xiaoscl") + ", " + rsl.getString("jizzkj")
                            + ", " + rsl.getString("jizzb") + ", (select id from zhibb where bianm='" + rsl.getString("canzxm") + "'), (select id from danwb where bianm='" + rsl.getString("canzxmdw") + "'), " + rsl.getString("canzsx")
                            + ", " + rsl.getString("canzxx") + ", (select id from hetjsxsb where bianm='" + rsl.getString("hetjsxsb_id") + "'), (select id from yunsfsb where mingc='" + rsl.getString("yunsfsb_id")
                            + "'), '" + rsl.getString("beiz") + "', " + rsl.getLong("hetys_id") + ", (select id from zhibb where bianm='" + rsl.getString("zhibb_id") + "'), (select id from tiaojb where bianm='" + rsl.getString("tiaojb_id")
                            + "'), " + rsl.getString("shangx") + ", " + rsl.getString("xiax") + ", (select id from danwb where bianm='" + rsl.getString("danwb_id") + "'  and zhibb_id!=0));\n"
                    );
                }
//						文字
                sql = "select id, wenznr, hetys_id from hetyswzb where hetys_id=" + hetysb_id;
                rsl = con.getResultSetList(sql);

                sbExeSql.append("	delete from hetyswzb where hetys_id=").append(hetysb_id).append(";\n");

                while (rsl.next()) {

                    sbExeSql.append("insert into hetyswzb(id, wenznr, hetys_id)\n" +
                            "values\n" +
                            "  (" + rsl.getLong("id") + ", '" + rsl.getString("wenznr") + "', " + rsl.getLong("hetys_id") + ");\n"
                    );
                }

                sbExeSql.append("end;");
                sqls[0] = sbExeSql.toString();

                if (rsl != null) {

                    rsl.close();
                }

                InterFac_dt xiaf = new InterFac_dt();
                resul = xiaf.sqlExe(yuancljd[0], sqls, true);
                if (resul[0].equals("true")) {
                    //1置当前数据状态为2

                    String sql1 = "update hetys set liucztb_id=" + yuancljd[2] + " where id=" + TableID;
                    con.getUpdate(sql1);
                    return true;//没有日志
                } else {
//							strErr+=hetbh+":"+resul[0].replaceAll("\n", "")+";";
                    System.out.print("下发失败：" + resul[0]);
                    return false;
                }
            }
        }

        //
//			JDBCcon con=new JDBCcon();
        String sql = "";
        String dangqdz = ""; //表单当前状态
        long liuczthjid = 0;
        long liucztqqid = 0;//新增  前驱状态  判断是否衔接  反射
        long liucdzb_id = 0;
        String qianqmc = "";
        String houjmc = "";
        String caoz = "";
        String dongz = "";//新增  动作  判断是否衔接  反射
        long liucjsb_id = 0;//新增  流程角色表  判断是否衔接  反射
        long leibztb_id = -1;
        try {
            sql = "select liucztb_id\n" +//获得当前状态
                    "from " + TableName + "\n" +
                    "where id=" + TableID;
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {
                dangqdz = rs.getString(1);
            }
            sql = "select liucdzb.id,liucdzb.liuczthjid,liucdzb.liucztqqid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucdzb.dongz,liucztb2.leibztb_id,\n" +
                    " (select liucjsb_id from liucdzjsb where liucdzb_id=liucdzb.id) liucjsb_id \n " +
                    "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n" +
                    "where liucdzb.liucztqqid=liucztb1.id\n" +
                    " and liucdzb.liuczthjid=liucztb2.id\n" +
                    " and liucztb1.leibztb_id=leibztb1.id\n" +
                    " and liucztb2.leibztb_id=leibztb2.id\n" +
                    " and liucdzb.liucztqqid=" + dangqdz +
                    "  and liucztb1.xuh>liucztb2.xuh";
            ResultSet rs1 = con.getResultSet(sql);
            if (rs1.next()) {
                liuczthjid = rs1.getLong("liuczthjid");
                liucztqqid = rs1.getLong("liucztqqid");
                liucdzb_id = rs1.getLong("id");
                qianqmc = rs1.getString("qianqmc");
                houjmc = rs1.getString("houjmc");
                caoz = rs1.getString("caoz");
                liucjsb_id = rs1.getLong("liucjsb_id");
                dongz = rs1.getString("dongz");
                leibztb_id = rs1.getLong("leibztb_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //日志
//				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij," +
                "miaos)values(xl_xul_id.nextval,"
                + TableID
                + ","
                + liucdzb_id
                + ",'"
                + qianqmc
                + "','"
                + houjmc
                + "','"
                + caoz
                + "',(select quanc from renyxxb where id="
                + renyxxb_id
                + "),sysdate"
                + ",'"
                + xiaox
                + "')";
        con.getInsert(sql);
        if (leibztb_id == 0 || leibztb_id == 1) {//如果后继类别状态为0或１即为回退
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + leibztb_id
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        } else {//如果后继为
            sql =
                    "update " + TableName + "\n" +
                            "set " + TableName + ".liucztb_id=" + liuczthjid
                            + "," + TableName + ".liucgzid=" + TableID
                            + "where " + TableName + ".id=" + TableID;
        }
        con.getUpdate(sql);
        liucxjdzcl(con, TableName, TableID, liucjsb_id, qianqmc, houjmc, caoz, dongz);

        return true;

    }

    public static void tij(String TableName, long TableID, long renyxxb_id, String xiaox, String a) {
        // 先判断是否存在衔接点
        JDBCcon con = new JDBCcon();

        // 注 : 目前 只支持 下发功能
        if (TableName.equals("diancjsmkb")) {

            String[] yuancljd = getXianjd(TableName, TableID, "提交");// 远程连接点
            if (yuancljd != null) {// 如果存在衔接点，1置当前数据状态为2，向远程衔接点发送数据

                String sql = "select j.id,\n" +
                        "       j.diancxxb_id,\n" +
                        "       j.bianm,\n" +
                        "       decode(g.shangjgsbm,null,g.bianm,g.shangjgsbm) as gongysb_id,\n" +
                        "       j.gongysmc,\n" +
                        "       j.yunsfsb_id,\n" +
                        "       j.yunj,\n" +
                        "       j.yingd,\n" +
                        "       j.kuid,\n" +
                        "       j.faz,\n" +
                        "       j.fahksrq,\n" +
                        "       j.fahjzrq,\n" +
                        "       j.meiz,\n" +
                        "       j.daibch,\n" +
                        "       j.yuanshr,\n" +
                        "       j.xianshr,\n" +
                        "       j.yansksrq,\n" +
                        "       j.yansjzrq,\n" +
                        "       j.yansbh,\n" +
                        "       j.shoukdw,\n" +
                        "       j.kaihyh,\n" +
                        "       j.zhangh,\n" +
                        "       j.fapbh,\n" +
                        "       j.fukfs,\n" +
                        "       j.duifdd,\n" +
                        "       j.ches,\n" +
                        "       j.jiessl,\n" +
                        "       j.guohl,\n" +
                        "       j.yuns,\n" +
                        "       j.koud,\n" +
                        "       j.jiesslcy,\n" +
                        "       j.hansdj,\n" +
                        "       j.bukmk,\n" +
                        "       j.hansmk,\n" +
                        "       j.buhsmk,\n" +
                        "       j.meikje,\n" +
                        "       j.shuik,\n" +
                        "       j.shuil,\n" +
                        "       j.buhsdj,\n" +
                        "       j.jieslx,\n" +
                        "       j.jiesrq,\n" +
                        "       j.ruzrq,\n" +
                        "       h.hetbh as hetb_id,\n" +
                        "       j.liucztb_id,\n" +
                        "       j.liucgzid,\n" +
                        "       j.ranlbmjbr,\n" +
                        "       j.ranlbmjbrq,\n" +
                        "       j.beiz,\n" +
                        "       j.jiesfrl,\n" +
                        "       jhkj.bianm as jihkjb_id,\n" +
                        "       decode(m.shangjgsbm,null,m.bianm,m.shangjgsbm) as  meikxxb_id,\n" +
                        "       j.hetj,\n" +
                        "       j.meikdwmc,\n" +
                        "       j.zhiljq,\n" +
                        "       j.qiyf,\n" +
                        "       j.jiesrl,\n" +
                        "       j.jieslf,\n" +
                        "       j.jiesrcrl,\n" +
                        "       j.liucgzbid,\n" +
                        "       j.ruzry,\n" +
                        "       j.fuid,\n" +
                        "       j.fengsjj,\n" +
                        "       j.jiajqdj,\n" +
                        "       j.jijlx,\n" +
                        "		nvl(j.yufkje, 0) as yufkje,\n" +
                        "		nvl(j.kuidjfyf, 0) as kuidjfyf,\n" +
                        "		nvl(j.kuidjfzf, 0) as kuidjfzf,\n" +
                        "		nvl(j.chaokdl, 0) as chaokdl,\n" +
                        "		nvl(j.chaokdlx, '') as chaokdlx, \n" +
                        "		nvl(j.Yunfhsdj, 0) as Yunfhsdj, \n" +
                        "		nvl(j.hansyf, 0) as hansyf, \n" +
                        "		nvl(j.buhsyf, 0) as buhsyf, \n" +
                        "		nvl(j.yunfjsl, 0) as yunfjsl \n" +
                        "   from " + TableName + " j, gongysb g, hetb h, jihkjb jhkj, meikxxb m \n" +
                        "  where j.id = " + TableID + "\n" +
                        "    and j.gongysb_id = g.id\n" +
                        "    and j.jihkjb_id = jhkj.id\n" +
                        "    and j.hetb_id = h.id \n" +
                        "    and j.meikxxb_id = m.id";

                ResultSetList rsl = con.getResultSetList(sql);

                sql = "select j.id, jiesdid, z.bianm as zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje,\n"
                        + "       zhuangt, yansbhb_id\n"
                        + "  from jieszbsjb j, zhibb z\n"
                        + " where j.zhibb_id = z.id\n"
                        + "   and j.jiesdid = " + TableID;

                ResultSetList rslzb = con.getResultSetList(sql);

                sql =
                        "select d.id,\n" +
                                "       d.xuh,\n" +
                                "       d.jiesdid,\n" +
                                "       z.bianm as zhibb_id,\n" +
                                "       d.hetbz,\n" +
                                "       d.gongf,\n" +
                                "       d.changf,\n" +
                                "       d.jies,\n" +
                                "       d.yingk,\n" +
                                "       d.zhejbz,\n" +
                                "       d.zhejje,\n" +
                                "       d.gongfsl,\n" +
                                "       d.yanssl,\n" +
                                "       d.jiessl,\n" +
                                "       d.koud,\n" +
                                "       d.kous,\n" +
                                "       d.kouz,\n" +
                                "       d.ches,\n" +
                                "       d.jingz,\n" +
                                "       d.koud_js,\n" +
                                "       d.yuns,\n" +
                                "       d.jiesslcy,\n" +
                                "       d.jiesdj,\n" +
                                "       d.jiakhj,\n" +
                                "       d.jiaksk,\n" +
                                "       d.jiashj,\n" +
                                "       d.biaomdj,\n" +
                                "       d.buhsbmdj,\n" +
                                "       d.leib,\n" +
                                "       d.hetj,\n" +
                                "       d.qnetar,\n" +
                                "       d.std,\n" +
                                "       d.stad,\n" +
                                "       d.star,\n" +
                                "       d.vdaf,\n" +
                                "       d.mt,\n" +
                                "       d.mad,\n" +
                                "       d.aad,\n" +
                                "       d.ad,\n" +
                                "       d.aar,\n" +
                                "       d.vad,\n" +
                                "       d.zongje,\n" +
                                "       decode(m.shangjgsbm, null, m.bianm, m.shangjgsbm) as meikxxb_id,\n" +
                                "       c.bianm as faz_id,\n" +
                                "       d.chaokdl,\n" +
                                "       d.jiajqdj\n" +
                                "  from danpcjsmxb d, zhibb z, jiesb j, meikxxb m, chezxxb c\n" +
                                " where d.zhibb_id = z.id\n" +
                                "   and d.jiesdid = j.id\n" +
                                "   and d.meikxxb_id = m.id\n" +
                                "   and d.faz_id = c.id\n" +
                                "   and d.jiesdid = " + TableID;

                ResultSetList rsldpc = con.getResultSetList(sql);

                String[] sqls = new String[rsl.getRows() * 2 + rslzb.getRows() + rsldpc.getRows() + 3];
                int j = 0;

                sqls[j] = "delete from jiesb where id = " + TableID;

                while (rsl.next()) {
                    j++;
                    //
                    if (TableName.equals("diancjsmkb")) {

                        sqls[j] = "insert into jiesb\n"
                                + " (id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, \n"
                                + "yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, \n"
                                + "yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, \n"
                                + "liucztb_id, liucgzid, ranlbmjbr, ranlbmjbrq, beiz, jiesfrl, jihkjb_id, meikxxb_id, hetj, meikdwmc, zhiljq, qiyf, \n"
                                + "jiesrl, jieslf, jiesrcrl, liucgzbid, ruzry, fuid, fengsjj, jiajqdj, jijlx, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, \n"
                                + "diancjsmkb_id, yufkje, Yunfhsdj, hansyf, buhsyf, yunfjsl)\n"
                                + " values	\n"
                                + " ("
                                + rsl.getLong("id")
                                + ", "
                                + rsl.getLong("diancxxb_id")
                                + ", '"
                                + rsl.getString("bianm")
                                + "', (select id from gongysb where bianm = '" + rsl.getString("gongysb_id") + "')"
                                + ", '"
                                + rsl.getString("gongysmc")
                                + "', "
                                + rsl.getLong("yunsfsb_id")
                                + ", '"
                                + rsl.getString("yunj")
                                + "', "
                                + rsl.getDouble("yingd")
                                + ", "
                                + rsl.getDouble("kuid")
                                + ", '"
                                + rsl.getString("faz")
                                + "', to_date('"
                                + DateUtil.FormatDate(rsl.getDate("fahksrq"))
                                + "', 'yyyy-mm-dd'), to_date('"
                                + DateUtil.FormatDate(rsl.getDate("fahjzrq"))
                                + "', 'yyyy-mm-dd'), '"
                                + rsl.getString("meiz")
                                + "', '"
                                + rsl.getString("daibch")
                                + "', '"
                                + rsl.getString("yuanshr")
                                + "', '"
                                + rsl.getString("xianshr")
                                + "', to_date('"
                                + DateUtil.FormatDate(rsl.getDate("yansksrq"))
                                + "', 'yyyy-mm-dd'), to_date('"
                                + DateUtil.FormatDate(rsl.getDate("yansjzrq"))
                                + "', 'yyyy-mm-dd'), '"
                                + rsl.getString("yansbh")
                                + "', '"
                                + rsl.getString("shoukdw")
                                + "', '"
                                + rsl.getString("kaihyh")
                                + "', '"
                                + rsl.getString("zhangh")
                                + "', '"
                                + rsl.getString("fapbh")
                                + "', '"
                                + rsl.getString("fukfs")
                                + "', '"
                                + rsl.getString("duifdd")
                                + "', "
                                + rsl.getString("ches")
                                + ", "
                                + rsl.getString("jiessl")
                                + ", "
                                + rsl.getString("guohl")
                                + ", "
                                + rsl.getString("yuns")
                                + ", "
                                + rsl.getString("koud")
                                + ", "
                                + rsl.getString("jiesslcy")
                                + ", "
                                + rsl.getString("hansdj")
                                + ", "
                                + rsl.getString("bukmk")
                                + ", "
                                + rsl.getString("hansmk")
                                + ", "
                                + rsl.getString("buhsmk")
                                + ", "
                                + rsl.getString("meikje")
                                + ", "
                                + rsl.getString("shuik")
                                + ", "
                                + rsl.getString("shuil")
                                + ", "
                                + rsl.getString("buhsdj")
                                + ", "
                                + rsl.getString("jieslx")
                                + ", to_date('"
                                + DateUtil.FormatDate(rsl.getDate("jiesrq"))
                                + "', 'yyyy-MM-dd'), to_date('"
                                + (rsl.getString("ruzrq").equals("") ? ""
                                : DateUtil.FormatDate(rsl
                                .getDate("ruzrq")))
                                + "', 'yyyy-MM-dd'), (select id from hetb where hetbh = '" + rsl.getString("hetb_id") + "')"
                                + ", "
                                + yuancljd[1]
                                + ", "
                                + rsl.getString("liucgzid")
                                + ", '"
                                + rsl.getString("ranlbmjbr")
                                + "', to_date('"
                                + DateUtil
                                .FormatDate(rsl.getDate("ranlbmjbrq"))
                                + "', 'yyyy-MM-dd'), '" + rsl.getString("beiz")
                                + "', " + rsl.getString("jiesfrl")
                                + ", (select id from jihkjb where bianm = '" + rsl.getString("jihkjb_id") + "')"
                                + ", (select id from meikxxb where bianm = '" + rsl.getString("meikxxb_id") + "')"
                                + ", "
                                + rsl.getString("hetj") + ", '"
                                + rsl.getString("meikdwmc") + "', '"
                                + rsl.getString("zhiljq") + "', "
                                + rsl.getString("qiyf") + ", "
                                + rsl.getString("jiesrl") + ", "
                                + rsl.getString("jieslf") + ", "
                                + rsl.getString("jiesrcrl") + ", "
                                + rsl.getString("liucgzbid") + ", '"
                                + rsl.getString("ruzry") + "', "
                                + rsl.getLong("fuid") + ", "
                                + rsl.getString("fengsjj") + ", "
                                + rsl.getString("jiajqdj") + ", "
                                + rsl.getString("jijlx") + ", "
                                + rsl.getString("kuidjfyf") + ", "
                                + rsl.getString("kuidjfzf") + ", "
                                + rsl.getString("chaokdl") + ", '"
                                + rsl.getString("chaokdlx") + "', "
                                + rsl.getString("id") + ", "
                                + rsl.getDouble("yufkje") + ", "
                                + rsl.getString("Yunfhsdj") + ", "
                                + rsl.getString("hansyf") + ", "
                                + rsl.getString("buhsyf") + ", "
                                + rsl.getString("yunfjsl") + " )\n";

                        sqls[++j] = "update fahb set jiesb_id = "
                                + rsl.getLong("id") + " where id in "
                                + "(select id from fahb where jiesb_id = "
                                + rsl.getLong("id") + ")";
                    }
                }

                rsl.close();

                sqls[++j] = "delete from jieszbsjb where jiesdid = " + TableID;

                while (rslzb.next()) {
                    j++;
                    sqls[j] = "insert into jieszbsjb\n"
                            + " (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)\n"
                            + " values\n" + " (" + rslzb.getLong("id") + ", "
                            + rslzb.getLong("jiesdid")
                            + ", (select id from zhibb where bianm = '" + rslzb.getString("zhibb_id") + "'), '"
                            + rslzb.getString("hetbz") + "', "
                            + rslzb.getString("gongf") + ", "
                            + rslzb.getString("changf") + ", "
                            + rslzb.getString("jies") + ", "
                            + rslzb.getString("yingk") + ", "
                            + rslzb.getString("zhejbz") + ", "
                            + rslzb.getString("zhejje") + ", "
                            + rslzb.getString("zhuangt") + ", "
                            + rslzb.getString("yansbhb_id") + ") \n";
                }

                rslzb.close();

                sqls[++j] = "delete from danpcjsmxb where jiesdid = " + TableID;

                while (rsldpc.next()) {
                    j++;
                    sqls[j] = "insert into danpcjsmxb\n"
                            + " (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, "
                            + "jiessl, koud, kous, kouz, ches, jingz, koud_js,"
                            + "yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib, hetj, qnetar, std, stad,"
                            + "star, vdaf, mt, mad, aad, ad, aar, vad, zongje, meikxxb_id, faz_id, chaokdl, jiajqdj)	\n"
                            + " values	\n" + " (" + rsldpc.getLong("id") + ", "
                            + rsldpc.getString("xuh") + ", "
                            + rsldpc.getString("jiesdid")
                            + ", (select id from zhibb where bianm = '" + rsldpc.getString("zhibb_id") + "'), '"
                            + rsldpc.getString("hetbz") + "', "
                            + rsldpc.getString("gongf") + ", "
                            + rsldpc.getString("changf") + ", "
                            + rsldpc.getString("jies") + ", "
                            + rsldpc.getString("yingk") + ", "
                            + rsldpc.getString("zhejbz") + ", "
                            + rsldpc.getString("zhejje") + ", "
                            + rsldpc.getString("gongfsl") + ", "
                            + rsldpc.getString("yanssl") + ", "
                            + rsldpc.getString("jiessl") + ", "
                            + rsldpc.getString("koud") + ", "
                            + rsldpc.getString("kous") + ", "
                            + rsldpc.getString("kouz") + ", "
                            + rsldpc.getString("ches") + ", "
                            + rsldpc.getString("jingz") + ", "
                            + rsldpc.getString("koud_js") + ", "
                            + rsldpc.getString("yuns") + ", "
                            + rsldpc.getString("jiesslcy") + ", "
                            + rsldpc.getString("jiesdj") + ", "
                            + rsldpc.getString("jiakhj") + ", "
                            + rsldpc.getString("jiaksk") + ", "
                            + rsldpc.getString("jiashj") + ", "
                            + rsldpc.getString("biaomdj") + ", "
                            + rsldpc.getString("buhsbmdj") + ", "
                            + rsldpc.getString("leib") + ", "
                            + rsldpc.getString("hetj") + ", "
                            + rsldpc.getString("qnetar") + ", "
                            + rsldpc.getString("std") + ", "
                            + rsldpc.getString("stad") + ", "
                            + rsldpc.getString("star") + ", "
                            + rsldpc.getString("vdaf") + ", "
                            + rsldpc.getString("mt") + ", "
                            + rsldpc.getString("mad") + ", "
                            + rsldpc.getString("aad") + ", "
                            + rsldpc.getString("ad") + ", "
                            + rsldpc.getString("aar") + ", "
                            + rsldpc.getString("vad") + ", "
                            + rsldpc.getString("zongje")
                            + ", (select id from meikxxb where bianm = '" + rsldpc.getString("meikxxb_id")
                            + "'), (select id from chezxxb where bianm = '" + rsldpc.getString("faz_id") + "'), "
                            + rsldpc.getString("chaokdl") + ", "
                            + rsldpc.getString("jiajqdj") + ")";
                }

                rsldpc.close();

                String[] resul = null;
                InterFac_dt Shangb = new InterFac_dt(); // 实例化接口变量
                resul = Shangb.sqlExe(yuancljd[0], sqls, true);

                if (resul[0].equals("true")) {
                    // 1置当前数据状态为2
                    // 更新本地数据库diancjsmkb/diancjsyfb 的 liucztb_id 为下一个流程动作
                    StringBuffer bf = new StringBuffer();
                    bf.append(" update " + TableName + " set liucztb_id="
                            + yuancljd[2] + " where id=" + TableID + "\n");
                    con.getUpdate(bf.toString());
                    return;// 没有日志
                } else {
                    System.out.print("下发失败：" + resul[0]);
                    return;
                }
            }
        }

        String sql = "";
        long dangqdz = -1; // 表单当前状态
        long liuczthjid = 0;
        long liucdzb_id = 0;
        String qianqmc = "";
        String houjmc = "";
        String caoz = "";
        long leibztb_id = -1;
        try {
            sql = "select liucztb_id\n" + // 获得当前状态
                    "from " + TableName + "\n" + "where id=" + TableID;
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {
                dangqdz = rs.getLong(1);
            }
            sql = "select liucdzb.id,liucdzb.liuczthjid,leibztb1.mingc qianqmc,leibztb2.mingc houjmc,liucdzb.mingc caoz,liucztb2.leibztb_id\n"
                    + "from liucdzb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n"
                    + "where liucdzb.liucztqqid=liucztb1.id\n"
                    + " and liucdzb.liuczthjid=liucztb2.id\n"
                    + " and liucztb1.leibztb_id=leibztb1.id\n"
                    + " and liucztb2.leibztb_id=leibztb2.id\n"
                    + " and liucdzb.liucztqqid="
                    + dangqdz
                    + "  and liucztb1.xuh<liucztb2.xuh";
            ResultSet rs1 = con.getResultSet(sql);
            if (rs1.next()) {
                liuczthjid = rs1.getLong("liuczthjid");
                liucdzb_id = rs1.getLong("id");
                qianqmc = rs1.getString("qianqmc");
                houjmc = rs1.getString("houjmc");
                caoz = rs1.getString("caoz");
                leibztb_id = rs1.getLong("leibztb_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 日志
        // SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql = "insert into liucgzb(id,liucgzid,liucdzb_id,qianqztmc,houjztmc,liucdzbmc,caozy,shij,"
                + "miaos)values(xl_xul_id.nextval,"
                + TableID
                + ","
                + liucdzb_id
                + ",'"
                + qianqmc
                + "','"
                + houjmc
                + "','"
                + caoz
                + "',(select quanc from renyxxb where id="
                + renyxxb_id
                + "),sysdate" + ",'" + xiaox + "')";
        con.getInsert(sql);
        if (leibztb_id == 0 || leibztb_id == 1) {// 如果后继类别状态为0或１即为回退
            sql = "update " + TableName + "\n" + "set " + TableName
                    + ".liucztb_id=" + leibztb_id + "," + TableName
                    + ".liucgzid=" + TableID + "where " + TableName + ".id="
                    + TableID;
        } else {// 如果后继为
            sql = "update " + TableName + "\n" + "set " + TableName
                    + ".liucztb_id=" + liuczthjid + "," + TableName
                    + ".liucgzid=" + TableID + "where " + TableName + ".id="
                    + TableID;
        }

        con.getUpdate(sql);
        con.Close();
    }
}