package com.zhiren.dc.jilgl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.main.Visit;

/**
 *
 * @author Rock
 *
 */
/*
 * 2009-04-17 王磊 采样编号生成的分组条件查询加入电厂ID过滤
 */

/*
 * 2009-02-23 王磊 采样编号手工录入时，增加zhuanmb等的处理
 */
/*
 * 2009-05-19 王磊 增加是否自动生成采样编号的判断
 */
/*
 * 作者：王磊 时间：2009-05-23 描述：增加参数设置处理发货按照质量来分列的问题
 */
/*
 * 作者：王磊 时间：2009-05-27 描述：按质量分列where语句异常,存车皮表的时候增加yuanmz、yuanpz参数
 */
/*
 * 作者：王磊 时间：2009-06-13 11：04 描述：修改车皮临时表(cheplsb)向车皮表(chepb)中写值时车皮表ID等于车皮临时表ID
 */
/*
 * 作者：王磊 时间：2009-06-16 10：39 描述：增加补录数据标识处理 需更新数据库 alter table chepb add bulsj
 * number(1) default 0 null; comment on column chepb.bulsj is '补录数据标识 1：补录
 * 0：非补录'; alter table cheplsb add bulsj number(1) default 0 null; comment on
 * column cheplsb.bulsj is '补录数据标识 1：补录 0：非补录';
 */
/*
 * 作者：王磊 时间：2009-06-16 15：31 描述：修改数据导入时更新chepbtmp状态的判断条件去掉diancxxb_id 的限制
 */
/*
 * 作者：王磊 时间：2009-06-16 17：02 描述：增加按质量更新列ID时对采样挑号（zhilb_id=0）的处理
 */
/*
 * 作者：王磊 时间：2009-06-17 14：15 描述：增加save计量Data的方法 可传入JDBC连接
 */
/*
 * 作者：王磊 时间：2009-07-02 11：50 描述：解决导入数据时单批次运损未自动计算的问题
 */
/*
 * 作者：王磊 时间：2009-07-02 16：17 描述：解决导入数据时未写入过衡表引起检斤单查询不到导入数据的问题
 */
/*
 * 作者：王磊 时间：2009-07-06 19：13 描述：数据保存时对核对标志的更新错误
 */
/*
 * 作者：王磊 时间：2009-08-07 15：20 描述：修改导入数据时只有火车数据存入过衡表
 */
/*
 * 作者：王磊 时间：2009-09-09 16：04 描述：数量数据操作时加入参数控制到货日期是否加入列ID计算 系统信息表加入如下配置： mingc =
 * '按到货日期分列ID' zhi = '是' zhuangt = 1 beiz = '使用'
 */
/*
 * 作者：王磊 时间：2009-09-10 17：27 描述：取得设置的运损率时不判断一厂多制 只读取总厂的设置。
 */

/*
 * 作者：王伟 时间：2009-10-10 描述：在InsChepb方法中添加装车单位ID，需更新数据库 alter table chepb add
 * zhuangcdw_item_id number(15) default 0; alter table cheplsb add
 * zhuangcdw_item_id number(15) default 0;
 * 
 */
/*
 * 作者：王磊 时间：2009-10-28 描述：增加了计量处理中对过衡表是否操作的参数
 */

/*
 * 作者：王伟 时间：2009-11-20 描述： 在InsChepb方法中添加参数piaojh字段可手工录入 insert into xitxxb
 * values ( getnewid(diancxxb_id), --电厂信息表ID 1, diancxxb_id, --电厂信息表ID
 * '票据号手工录入','是','','数量',1,'使用' ) piojh字段手工录入后，要想chepb自动排序号，请更新下面函数
 * 
 * CREATE OR REPLACE FUNCTION getChepxh(lursj in date) Return number as begin
 * declare intXuh number; begin select nvl(max(c.xuh),0)+1 into intXuh from
 * chepxhb c where to_char(c.riq,'yyyy-mm-dd') = to_char(lursj,'yyyy-mm-dd');
 * 
 * if intXuh = 1 then insert into chepxhb values(lursj,intXuh); else update
 * chepxhb set xuh = intXuh where to_char(riq,'yyyy-mm-dd') =
 * to_char(lursj,'yyyy-mm-dd'); end if; return intXuh; end; end;
 */
/*
 * 作者：王磊 时间：2009-12-02 描述：SaveJilgl 支持多个fahbtmp id
 */

/*
 * 作者：张森涛 时间：2010-01-19 描述：增加煤矿是否有合同进煤控制
 */
/*
 * 作者：夏峥
 * 时间：2014-03-04
 * 描述：为邯郸采制化编码特殊设置进行程序调整。
 */

public class Jilcz {

    public static final int SaveMode_BL = 0;

    public static final int SaveMode_DR = 1;

    public static final int SaveMode_JK = 2;

    public static final String strJingz = "maoz-piz";

    public static long getZhilbid(JDBCcon con, String caiybh, Date caiyrq,
                                  long diancxxb_id) {
        ResultSetList rsl = con
                .getResultSetList("select zhilb_id from caiyb where bianm='"
                        + caiybh + "'");
        if (rsl.next()) {
            return rsl.getLong("zhilb_id");
        } else {
            if (caiybh == null)
                return insertCaiyb(con, caiyrq, diancxxb_id);
            else
                return insertCaiyb(con, caiybh, caiyrq, diancxxb_id);
        }
    }

    public static long insertCaiyb(JDBCcon con, Date caiyrq, long diancxxb_id) {
        String zhilb_id = MainGlobal.getNewID(diancxxb_id);
        String sqlriq = DateUtil.FormatDate(caiyrq == null ? new Date()
                : caiyrq);
        StringBuffer sbsql = new StringBuffer();
        sbsql
                .append("insert into caiyb(id,zhilb_id,bianm,caiyrq,xuh) (select getnewid(");
        sbsql.append(diancxxb_id).append("),").append(zhilb_id).append(
                ",getCaiybm1(to_date('");
        sbsql.append(sqlriq);
        sbsql.append("','yyyy-mm-dd'),nvl(max(xuh),0)+1),to_date('").append(
                sqlriq);
        sbsql
                .append("','yyyy-mm-dd'),nvl(max(xuh),0)+1 from caiyb where caiyrq =");
        sbsql.append("to_date('").append(sqlriq).append("','yyyy-mm-dd'))");
        // String sql = "insert into caiyb(id,zhilb_id,bianm,riq,xuh)
        // values(getnewid("
        // + diancxxb_id + ")," + zhilb_id + ",getCaiybm(to_date('"
        // + DateUtil.FormatDate(caiyrq == null ? new Date() : caiyrq)
        // + "','yyyy-mm-dd')))";
        int flag = con.getInsert(sbsql.toString());
        if (flag == -1) {
            return -1;
        }
        return Long.parseLong(zhilb_id);
    }

    public static long insertCaiyb(JDBCcon con, String caiybh, Date caiyrq,
                                   long diancxxb_id) {
        String zhilb_id = MainGlobal.getNewID(diancxxb_id);
        String sqlriq = DateUtil.FormatDate(caiyrq == null ? new Date()
                : caiyrq);
        StringBuffer sbsql = new StringBuffer();
        sbsql
                .append("insert into caiyb(id,zhilb_id,bianm,caiyrq,xuh) (select getnewid(");
        sbsql.append(diancxxb_id).append("),").append(zhilb_id).append(",'");
        sbsql.append(caiybh);
        sbsql.append("',to_date('").append(sqlriq);
        sbsql
                .append("','yyyy-mm-dd'),nvl(max(xuh),0)+1 from caiyb where caiyrq =");
        sbsql.append("to_date('").append(sqlriq).append("','yyyy-mm-dd'))");
        int flag = con.getInsert(sbsql.toString());
        if (flag == -1) {
            return -1;
        }
        return Long.parseLong(zhilb_id);
    }

    public static String getCaiybm(JDBCcon con, Date caiyrq) {
        String sql = "select getCaiybm(to_date('"
                + DateUtil.FormatDate(caiyrq == null ? new Date() : caiyrq)
                + "','yyyy-mm-dd')) caiybm from dual";
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            return rsl.getString(0);
        }
        return null;
    }

    /**
     * @param GroupName
     *            取SysConstant固定值
     * @return
     */
    public static List getGroupList(String GroupName, String diancxxb_id) {
        List fahgrouplist = new ArrayList();
        JDBCcon con = new JDBCcon();
        StringBuffer querysql = new StringBuffer(
                "select zhi from xitxxb where leib = '数量' and danw ='"
                        + GroupName + "' and beiz = '使用' and diancxxb_id="
                        + diancxxb_id);
        try {
            ResultSetList rs = con.getResultSetList(querysql.toString());
            while (rs.next()) {
                String StrTmp[] = rs.getString("zhi").split(",");
                fahgrouplist.add(StrTmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fahgrouplist;
    }

    public static String getStringGroup(List GroupList) {
        if (GroupList == null || GroupList.isEmpty()) {
            return null;
        }
        StringBuffer sbGroup = new StringBuffer();
        int size = GroupList.size();
        for (int m = 0; m < size; m++) {
            sbGroup.append(((String[]) GroupList.get(m))[1]).append(",");
        }
        sbGroup.deleteCharAt(sbGroup.length() - 1);
        return sbGroup.toString();
    }

    public static String getGroupWhere(ResultSetList rs, List listTmp) {
        StringBuffer strWhere = new StringBuffer();
        try {
            int size = listTmp.size();
            for (int n = 0; n < size; n++) {
                String strColType = ((String[]) listTmp.get(n))[0]
                        .toUpperCase();
                String strColName = ((String[]) listTmp.get(n))[1];
                strWhere.append(strColName);
                if ("STRING".equals(strColType)) {
                    if (rs.getString(strColName) != null) {
                        strWhere.append("='").append(rs.getString(strColName))
                                .append("'");
                    } else {
                        strWhere.append(" is null");
                    }

                } else if ("LONG".equals(strColType)) {
                    strWhere.append("=").append(rs.getLong(strColName));
                } else if ("DATE".equals(strColType)) {
                    strWhere.append("=to_date('").append(
                            rs.getDateString(strColName)).append(
                            "','yyyy-mm-dd')");
                } else if ("TIMESTAMP".equals(strColType)) {
                    strWhere.append("=to_date('").append(
                            rs.getDateTimeString(strColName)).append(
                            "yyyy-mm-dd hh24:mi:ss')");
                }
                if (n + 1 < size) {
                    strWhere.append(" and ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return strWhere.toString();
    }

    public static double getShul(JDBCcon con, String zid, long zhilb_id) {
        double shul = 0.0;
        String sql = "select nvl(sum(shul),0) shul from  (select nvl(sum("
                + zid + "),0) shul from fahb where zhilb_id=" + zhilb_id
                + " union select nvl(sum(" + zid
                + "),0) shul from cheplsb where zhilb_id =" + zhilb_id + ")";
        ResultSetList rs = con.getResultSetList(sql);
        if (rs.next()) {
            shul = rs.getDouble("shul");
        }
        rs.close();
        return shul;
    }

    public static int getWeekOfDate(Date dt) {
        // String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
        // "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
        // return weekDays[w];
    }

    // 增加合同控制进煤
    public static int Hetkzjm(JDBCcon con, long diancxxb_id, long meikxxb_id,
                              String riq, int yunsfs) {

        boolean isContrl = MainGlobal.getXitxx_item("数量", "通过合同控制进煤",
                String.valueOf(diancxxb_id), "否").equals("是");

        int shifzm = Jilcz.getWeekOfDate(new Date());

        if (yunsfs == SysConstant.YUNSFS_QIY) {
            shifzm = Jilcz.getWeekOfDate(DateUtil.getDate(riq));
        }

        if (isContrl) {
            if (shifzm == 0 || shifzm == 6) {
                return 0;
            } else {
                String sql = "select * from jinmsqpfb where meikxxb_id="
                        + meikxxb_id + " \n" + " and qisrq<="
                        + DateUtil.FormatOracleDate(riq) + " and jiezrq>="
                        + DateUtil.FormatOracleDate(riq) + " \n"
                        + " and zhuangt=1";
                ResultSetList rsl = con.getResultSetList(sql);
                if (rsl.next()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
        return 0;
    }

    // public static int Caiyzhilcl(JDBCcon,ResultSetList rs,)

    public static int updateRijhzl(JDBCcon con, long diancxxb_id, int yunsfs,
                                   String caiybh) {
        String Grouptype = SysConstant.Caiy_GroupType_hc;
//		String ViewName = "VWHUOYCYFZFS";
        if (yunsfs == SysConstant.YUNSFS_QIY) {
            Grouptype = SysConstant.Caiy_GroupType_qc;
//			ViewName = "VWQIYCYFZFS";
        }
        List l = getGroupList(Grouptype, String.valueOf(diancxxb_id));
        // 如果3 日计划生成采样
        if (MainGlobal.getXitxx_item("数量", "日计划生成采样",
                String.valueOf(diancxxb_id), "否").equals("是")) {

            String strWhere = " zhilb_id=0";
            String sql = "select * from qicrjhb where " + strWhere;
            ResultSetList rs = con.getResultSetList(sql);
            long zhilb_id = 0;
            int flag = 0;
            while (rs.next()) {
                // 根据汽车日计划表信息关联采样分组方式视图

                // 判断日计划是否已有zhilb_id con,分组条件,记录集
                zhilb_id = getRijhZlfz(con, l, rs);
                // 新增采样

                if (zhilb_id == 0) {

                    zhilb_id = getZhilbid(con, null, new Date(), diancxxb_id);
                    if (zhilb_id == -1) {
                        // 未取得质量ID 错误代码在此添加
                        return -1;
                    }

                    long jihkjid = SysConstant.JIHKJ_NONE;
                    if (rs.getString("kouj_id") != null) {
                        jihkjid = rs.getLong("kouj_id");
                    }
                    long fazid = SysConstant.Chez_q;
                    if (rs.getString("faz_id") != null) {
                        fazid = rs.getLong("faz_id");
                    }
                    flag = Caiycl.CreatBianh(con, zhilb_id, diancxxb_id, rs
                            .getLong("meikxxb_id"), jihkjid, yunsfs, fazid);
                    if (flag == -1)
                        // 采样编号生成失败 错误代码在此添加
                        return -1;
                }

                StringBuffer sb = new StringBuffer();
                sb.append("update qicrjhb set zhilb_id = ").append(zhilb_id)
                        .append(" where id = ").append(rs.getString("id"));
                flag = con.getUpdate(sb.toString());
                if (flag == -1) {
                    // 车皮临时表未添加成功 错误代码在此添加
                    return -1;
                }
            }
            rs.close();
        }
        return 0;
    }

    public static boolean isAutoCreateCaiy(JDBCcon con, long diancxxb_id) {
        String sql = "select * from xitxxb where mingc = '是否自动生成采样' "
                + "and zhuangt = 1 and leib = '数量' and beiz='使用' "
                + "and diancxxb_id = " + diancxxb_id;
        ResultSetList rs = con.getResultSetList(sql);
        boolean isAutoCreateCaiy = true;
        if (rs.next()) {
            isAutoCreateCaiy = "是".equals(rs.getString("zhi"));
        }
        rs.close();
        return isAutoCreateCaiy;
    }

    public static int Updatezlid(JDBCcon con, long diancxxb_id, int yunsfs,
                                 String caiybh) {
        if (!isAutoCreateCaiy(con, diancxxb_id)) {
            return 0;
        }
        // 如果1 采样编号为NULL
        if (caiybh == null) {
            String Grouptype = SysConstant.Caiy_GroupType_hc;
            String ViewName = "VWHUOYCYFZFS";
            if (yunsfs == SysConstant.YUNSFS_QIY) {
                Grouptype = SysConstant.Caiy_GroupType_qc;
                ViewName = "VWQIYCYFZFS";
            }
            List l = getGroupList(Grouptype, String.valueOf(diancxxb_id));
            String g = getStringGroup(l);
            if (g == null) {
                return -1;
            }
            long zhilb_id = 0;

            String sql = "select * from xitxxb where diancxxb_id = "
                    + diancxxb_id + " and mingc ='采样按数量分组' and leib= '数量'";
            ResultSetList rs = con.getResultSetList(sql);
            double Fenzl = 0.0;
            String Fenzd = "jingz";
            if (rs.next()) {
                Fenzl = rs.getDouble("zhi");
                Fenzd = rs.getString("danw") == null ? Fenzd : rs
                        .getString("danw");
            }
            rs.close();
            // 判断采样编号是否由车次决定
            sql = "select * from xitxxb where diancxxb_id = " + diancxxb_id
                    + " and mingc ='车次为采样编号' and leib= '数量'";
            rs = con.getResultSetList(sql);
            boolean chec = false;
            if (rs.next()) {
                chec = "是".equals(rs.getString("zhi"));
            }
            rs.close();
            // 判断采样编号是否由进厂编号决定
            sql = "select * from xitxxb where diancxxb_id = " + diancxxb_id
                    + " and mingc ='进厂批次号为采样编号' and leib= '数量'";
            rs = con.getResultSetList(sql);
            boolean caiybm = false;
            if (rs.next()) {
                caiybm = "是".equals(rs.getString("zhi"));
            }
            // 判断采制化编码是否由采样机编码决定(青铝)
            String isCaiyjbm = MainGlobal.getXitxx_item(con,"数量", "采样机编码为采制化编码", diancxxb_id+"", "否");
            rs.close();

            // 如果2 采样方式需要按吨处理
            if (Fenzl > 100.0 && yunsfs != SysConstant.YUNSFS_HUOY) {
                // 遍历车皮临时表
                sql = "select * from cheplsb order by id";
                rs = con.getResultSetList(sql);
                while (rs.next()) {
                    // 定义采样日期
                    Date caiyrq = null;
                    if (rs.getString("caiyrq") == null
                            || "".equals(rs.getString("caiyrq"))) {
                        caiyrq = new Date();
                    } else {
                        caiyrq = rs.getDate("caiyrq");
                    }
                    // 根据车皮临时表信息关联采样分组方式视图
                    String strWhere = getGroupWhere(rs, l);
                    String fhSql = "select nvl(max(zhilb_id),0) zhilb_id from "
                            + ViewName + " where " + strWhere;
                    ResultSetList rsl = con.getResultSetList(fhSql);
                    // 如果3 有相应发货
                    if (rsl.next()) {
                        // 判断如果zhilb_id为0则新增质量id
                        if (rsl.getLong("zhilb_id") == 0) {
                            zhilb_id = getZhilbid(con, null, caiyrq,
                                    diancxxb_id);
                        } else {
                            // 根据 发货分组查询出的质量ID
                            // 统计煤量（该煤量的组成在xitxxb中设定，需统计发货表与车皮临时表的合计）
                            double shul = getShul(con, Fenzd, rsl
                                    .getLong("zhilb_id"));
                            // 如果 4煤量>标定煤量(标定煤量由xitxxb中查出)
                            if (shul >= Fenzl) {
                                // 新增采样
                                zhilb_id = getZhilbid(con, null, caiyrq,
                                        diancxxb_id);
                                // 否则 4
                            } else {
                                // 使用原质量ID
                                zhilb_id = rsl.getLong("zhilb_id");
                            }
                        }
                    }
                    rsl.close();
                    if (zhilb_id == -1) {
                        // 未取得质量ID 错误代码在此添加
                        return -1;
                    }
                    long jihkjid = SysConstant.JIHKJ_NONE;
                    if (rs.getString("jihkjb_id") != null) {
                        jihkjid = rs.getLong("jihkjb_id");
                    }
                    long fazid = SysConstant.Chez_q;
                    if (rs.getString("faz_id") != null) {
                        fazid = rs.getLong("faz_id");
                    }
                    int flag = Caiycl.CreatBianh(con, zhilb_id, diancxxb_id, rs
                            .getLong("meikxxb_id"), jihkjid, yunsfs, fazid);
                    if (flag == -1)
                        // 采样编号生成失败 错误代码在此添加
                        return -1;
                    StringBuffer sb = new StringBuffer();
                    sb.append("update cheplsb set zhilb_id = ")
                            .append(zhilb_id).append(" where id = ").append(
                            rs.getString("id"));
                    flag = con.getUpdate(sb.toString());
                    if (flag == -1) {
                        // 车皮临时表未添加成功 错误代码在此添加
                        return -1;
                    }
                }
                rs.close();
                // 否则2
            } else {
                // 正常处理
                StringBuffer sb = new StringBuffer();
                sb.append("select ").append(g).append(
                        " from cheplsb where yunsfsb_id = ").append(yunsfs)
                        .append(" group by ").append(g);
                rs = con.getResultSetList(sb.toString());
                while (rs.next()) {
                    Date caiyrq = null;
                    // 增加判断采样日期是否存在 如果存在则使用传入的采样日期生成质量采样信息
                    if (rs.getString("caiyrq") == null
                            || "".equals(rs.getString("caiyrq"))) {
                        caiyrq = new Date();
                    } else {
                        caiyrq = rs.getDate("caiyrq");
                    }
                    // 定义采样编号
                    String checbm = null;
                    if (rs.getString("chec") == null
                            || "".equals(rs.getString("chec"))) {
                        checbm = "设置错误";
                    } else {
                        checbm = rs.getString("chec");
                    }
                    String strWhere = getGroupWhere(rs, l);
                    String fhSql = "select distinct zhilb_id from " + ViewName
                            + " where " + strWhere;
                    ResultSetList rsl = con.getResultSetList(fhSql);
                    if (rsl.next()) {
                        zhilb_id = rsl.getLong(0);
                    } else {
                        zhilb_id = getZhilbid(con, null, caiyrq, diancxxb_id);
                    }
                    rsl.close();
                    if (zhilb_id == -1) {
                        return -1;
                    }
                    long jihkjid = SysConstant.JIHKJ_NONE;
                    if (rs.getString("jihkjb_id") != null) {
                        jihkjid = rs.getLong("jihkjb_id");
                    }
                    long fazid = SysConstant.Chez_q;
                    if (rs.getString("faz_id") != null) {
                        fazid = rs.getLong("faz_id");
                    }
                    int flag = -1;
                    if (chec) {
                        flag = Caiycl.CreatBianh(con, zhilb_id, diancxxb_id, rs
                                        .getLong("meikxxb_id"), jihkjid, yunsfs, fazid,
                                checbm);
                    } else if (caiybm) {
                        String strcaiybm = "";
                        rsl = con
                                .getResultSetList("select * from caiyb where zhilb_id = "
                                        + zhilb_id);
                        if (rsl.next()) {
                            strcaiybm = rsl.getString("bianm");
                        }
                        rsl.close();
                        flag = Caiycl.CreatBianh(con, zhilb_id, diancxxb_id, rs
                                        .getLong("meikxxb_id"), jihkjid, yunsfs, fazid,
                                strcaiybm);
                    } else if ("是".equals(isCaiyjbm)){//青铝需求：由采样机取出编码，通过算法把编码存入chepbtmp.bei，样式：a,b,c(一批煤有三个子样)
                        flag = Caiycl.CreatBianh(con, zhilb_id, diancxxb_id, (rs.getString("beiz")).split(","));
                    } else {
                        flag = Caiycl.CreatBianh(con, zhilb_id, diancxxb_id, rs
                                .getLong("meikxxb_id"), jihkjid, yunsfs, fazid);
                    }

                    if (flag == -1){
                        // 采样编号生成失败 错误代码在此添加
                        return -1;
                    }else if(flag == -2){
                        return -2;
                    }
                    sb = new StringBuffer();
                    sb.append("update cheplsb set zhilb_id = ")
                            .append(zhilb_id).append(" where ")
                            .append(strWhere);
                    flag = con.getUpdate(sb.toString());
                    if (flag == -1) {
                        return -1;
                    }
                }
                rs.close();
            }
            // 否则 1
        } else {
            StringBuffer sb = new StringBuffer();
            sb
                    .append("select distinct caiybh,caiyrq from cheplsb where caiybh is not null");
            ResultSetList rsl = con.getResultSetList(sb.toString());
            while (rsl.next()) {
                Date caiyrq = null;
                // 增加判断采样日期是否存在 如果存在则使用传入的采样日期生成质量采样信息
                if (rsl.getString("caiyrq") == null
                        || "".equals(rsl.getString("caiyrq"))) {
                    caiyrq = new Date();
                } else {
                    caiyrq = rsl.getDate("caiyrq");
                }
                long zhilb_id = getZhilbid(con, rsl.getString("caiybh"),
                        caiyrq, diancxxb_id);
                int flag = -1;
                flag = Caiycl.CreatBianh(con, zhilb_id, diancxxb_id, rsl
                        .getString("caiybh"));
                if (flag == -1) {
                    return -1;
                }
                StringBuffer sql = new StringBuffer();
                sql.append("update cheplsb set zhilb_id = ").append(zhilb_id)
                        .append(" where caiybh = '").append(
                        rsl.getString("caiybh")).append("'");
                flag = con.getUpdate(sql.toString());
                if (flag == -1) {
                    return -1;
                }
            }
            rsl.close();
        }
        return 0;
    }

    private static long getRijhZlfz(JDBCcon con, List l, ResultSetList rs) {
        // TODO 自动生成方法存根

        StringBuffer where = new StringBuffer();
        String Chile = "";
        long zhilb_id = 0;
        for (int i = 0; i < l.size(); i++) {

            Chile = ((String[]) l.get(i))[1];
            if (Chile.equals("gongysb_id")) {

                where.append(" and gongys_id=" + rs.getLong("gongys_id"));
            } else if (Chile.equals("meikxxb_id")) {

                where.append(" and meikxxb_id=" + rs.getLong("meikxxb_id"));
            } else if (Chile.equals("pinzb_id")) {

                where.append(" and pinz_id=" + rs.getLong("pinz_id"));
            } else if (Chile.equals("jihkjb_id")) {

                where.append(" and kouj_id=" + rs.getLong("kouj_id"));
            } else if (Chile.equals("diancxxb_id")) {

                where.append(" and diancxxb_id=" + rs.getLong("diancxxb_id"));
            } else if (Chile.equals("yunsdwb_id")) {

                where.append(" and yunsdwb_id=" + rs.getLong("yunsdwb_id"));
            } else if (Chile.equals("chec")) {

                where.append(" and chec='" + rs.getString("chec") + "'");
            }
        }

        String sql = "select zhilb_id from qicrjhb where riq=to_date('"
                + Jiesdcz.FormatDate(rs.getDate("riq")) + "','yyyy-MM-dd') "
                + where.toString();
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {

            zhilb_id = rsl.getLong("zhilb_id");
        }
        rsl.close();
        return zhilb_id;
    }

    public static String CopyFahb(JDBCcon con, String fahbid, long diancxxb_id) {
        String newId = MainGlobal.getNewID(diancxxb_id);
        StringBuffer sb = new StringBuffer();
        sb
                .append(
                        "insert into fahb(id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, \n")
                .append(
                        "pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, hetb_id, \n")
                .append(
                        "zhilb_id, jiesb_id, yunsfsb_id, chec, tiaozbz, yansbhb_id, lie_id, \n")
                .append(
                        "yuandz_id, yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz, beiz) \n")
                .append("(select ")
                .append(newId)
                .append(",")
                .append(newId)
                .append(",")
                .append("diancxxb_id, gongysb_id, meikxxb_id, \n")
                .append(
                        "pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, hetb_id, \n")
                .append(
                        "zhilb_id, jiesb_id, yunsfsb_id, chec, tiaozbz, yansbhb_id, lie_id, \n")
                .append(
                        "yuandz_id, yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz, beiz \n")
                .append("from fahb where id=").append(fahbid).append(")");
        int flag = con.getInsert(sb.toString());
        if (flag <= 0) {
            return null;
        }
        return newId;
    }

    public static int INSorUpfahb(JDBCcon con, long diancxxb_id) {
        StringBuffer sb = new StringBuffer();
        sb.append("select nvl(f.id,0) fahid,ls.* from\n");
        sb.append("(select diancxxb_id,gongysb_id,meikxxb_id,pinzb_id,\n");
        sb.append("faz_id,daoz_id,jihkjb_id,fahrq,yuanshdwb_id,yuandz_id,\n");
        sb
                .append("daohrq,hetb_id,zhilb_id,caiybh,jiesb_id,yunsfsb_id,chec,yunsl,\n");
        sb.append("sum(maoz) maoz, sum(piz) piz,sum(" + strJingz
                + ") jingz, sum(biaoz) biaoz,\n");
        sb
                .append("sum(koud) koud, sum(kous) kous, sum(kouz) kouz,sum(koud+kous+kouz) zongkd,\n");
        sb.append("sum(sanfsl) sanfsl,count(id) ches from cheplsb\n");
        sb.append("group by(diancxxb_id,gongysb_id,meikxxb_id,pinzb_id,\n");
        sb.append("faz_id,daoz_id,jihkjb_id,fahrq,daohrq,hetb_id,zhilb_id,\n");
        sb
                .append("caiybh,jiesb_id,yunsfsb_id,chec,yunsl,yuanshdwb_id,yuandz_id) ) ls, fahb f\n");
        sb
                .append("where ls.diancxxb_id = f.diancxxb_id(+) and ls.gongysb_id = f.gongysb_id(+)\n");
        sb
                .append("and ls.meikxxb_id = f.meikxxb_id(+) and ls.pinzb_id = f.pinzb_id(+)\n");
        sb
                .append("and ls.faz_id = f.faz_id(+) and ls.daoz_id = f.daoz_id(+)\n");
        sb
                .append("and ls.jihkjb_id = f.jihkjb_id(+) and ls.fahrq = f.fahrq(+)\n");
        sb.append("and ls.daohrq = f.daohrq(+) \n");
        // sb.append("and ls.hetb_id = f.hetb_id(+) \n");
        sb.append("and ls.zhilb_id = f.zhilb_id(+) \n");
        // sb.append("and ls.jiesb_id = f.jiesb_id(+) \n");
        sb.append("and ls.yunsfsb_id = f.yunsfsb_id(+)\n");
        sb.append("and ls.chec=f.chec(+)\n");
        // sb.append("and ls.yunsl=f.yunsl(+)\n");
        sb.append("and ls.yuanshdwb_id=f.yuanshdwb_id(+)\n");
        sb.append("and ls.yuandz_id=f.yuandz_id(+)\n");

        ResultSetList rl = con.getResultSetList(sb.toString());
        while (rl.next()) {
            String strfahbid = rl.getString("fahid");
            if ("0".equals(strfahbid)) {
                strfahbid = MainGlobal.getNewID(diancxxb_id);
                double yunsl = getYunsl(diancxxb_id, rl.getLong("pinzb_id"), rl
                        .getInt("yunsfsb_id"), rl.getLong("meikxxb_id"));
                // 2008-09-26huochaoyuan
                // 将上边调用函数时的参数原来的固定值1改为rl.getInt("yunsfsb_id")
                sb.delete(0, sb.length());
                sb
                        .append("insert into fahb (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id,\n");
                sb
                        .append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, zhilb_id, yunsfsb_id,\n");
                sb
                        .append("chec, maoz, piz, jingz, biaoz, yunsl, koud, kous, kouz, zongkd, sanfsl, ches,\n");
                sb.append("yuandz_id, yuanshdwb_id) values(\n");
                sb.append(strfahbid).append(",").append(strfahbid).append(",");
                sb.append(rl.getLong("diancxxb_id")).append(",").append(
                        rl.getLong("gongysb_id")).append(",");
                sb.append(rl.getLong("meikxxb_id")).append(",\n").append(
                        rl.getLong("pinzb_id")).append(",");
                sb.append(rl.getLong("faz_id")).append(",").append(
                        rl.getLong("daoz_id")).append(",");
                sb.append(rl.getLong("jihkjb_id")).append(",").append(
                        DateUtil.FormatOracleDate(rl.getDateString("fahrq")))
                        .append(",");
                sb
                        .append(
                                DateUtil.FormatOracleDate(rl
                                        .getDateString("daohrq"))).append(",")
                        .append(rl.getLong("zhilb_id")).append(",");
                sb.append(rl.getLong("yunsfsb_id")).append(",\n'").append(
                        rl.getString("chec")).append("',");
                sb.append(rl.getDouble("maoz")).append(",").append(
                        rl.getDouble("piz")).append(",");
                sb.append(rl.getDouble("jingz")).append(",").append(
                        rl.getDouble("biaoz")).append(",");
                sb.append(yunsl).append(",").append(rl.getDouble("koud"))
                        .append(",").append(rl.getDouble("kous")).append(",");
                sb.append(rl.getDouble("kouz")).append(",").append(
                        rl.getDouble("zongkd")).append(",").append(
                        rl.getDouble("sanfsl")).append(",").append(
                        rl.getInt("ches")).append(",");
                sb.append(rl.getLong("yuandz_id")).append(",").append(
                        rl.getLong("yuanshdwb_id")).append(")");
                int flag = con.getInsert(sb.toString());
                if (flag == -1) {
                    return -1;
                } else {

                    // 判断票重是否减扣吨
                    piaozPz(con, strfahbid, "fahb");
                }
            } else {
                sb.delete(0, sb.length());
                sb.append("update fahb set maoz=maoz+").append(
                        rl.getDouble("maoz"));
                sb.append(",piz=piz+").append(rl.getDouble("piz"));
                sb.append(",jingz=jingz+").append(rl.getDouble("jingz"));
                sb.append(",biaoz=biaoz+").append(rl.getDouble("biaoz"));
                sb.append(",koud=koud+").append(rl.getDouble("koud"));
                sb.append(",kous=kous+").append(rl.getDouble("kous"));
                sb.append(",kouz=kouz+").append(rl.getDouble("kouz"));
                sb.append(",zongkd=zongkd+").append(rl.getDouble("zongkd"));
                sb.append(",sanfsl=sanfsl+").append(rl.getDouble("sanfsl"));
                sb.append(",ches=ches+").append(rl.getDouble("ches"));
                sb.append(" where id=").append(strfahbid);
                int flag = con.getUpdate(sb.toString());
                if (flag == -1) {
                    return -1;
                } else {

                    // 判断票重是否减扣吨
                    piaozPz(con, strfahbid, "fahb");
                }
            }
            StringBuffer Sql = new StringBuffer();
            Sql.append("update cheplsb set fahb_id = ").append(strfahbid)
                    .append(" where ");
            Sql.append(" diancxxb_id = ").append(rl.getLong("diancxxb_id"));
            Sql.append(" and gongysb_id =").append(rl.getLong("gongysb_id"));
            Sql.append(" and meikxxb_id =").append(rl.getLong("meikxxb_id"));
            Sql.append(" and pinzb_id = ").append(rl.getLong("pinzb_id"));
            Sql.append(" and faz_id = ").append(rl.getLong("faz_id"));
            Sql.append(" and daoz_id = ").append(rl.getLong("daoz_id"));
            Sql.append(" and jihkjb_id = ").append(rl.getLong("jihkjb_id"));
            Sql.append(" and zhilb_id = ").append(rl.getLong("zhilb_id"));
            Sql.append(" and to_char(fahrq,'yyyy-mm-dd') ='").append(
                    rl.getDateString("fahrq"));
            Sql.append("' and to_char(daohrq,'yyyy-mm-dd')='").append(
                    rl.getDateString("daohrq"));
            Sql.append("' and chec ='").append(rl.getString("chec"));
            Sql.append("' and yunsfsb_id = ").append(rl.getLong("yunsfsb_id"));
            Sql.append(" and yuanshdwb_id=").append(rl.getLong("yuanshdwb_id"));
            Sql.append(" and yuandz_id=").append(rl.getLong("yuandz_id"));
            int flag = con.getUpdate(Sql.toString());
            if (flag == -1) {
                return -1;
            }
        }
        return 0;
    }

    public static int InsChepb(JDBCcon con, long diancxxb_id, String lursj,
                               int hedbz) {
        String strlursj;
        if (lursj != null) {
            strlursj = lursj;
        } else {
            strlursj = DateUtil.FormatOracleDateTime(new Date());
        }
        String xwsql = MainGlobal.getXitxx_item("数量", "宣威汽车数据导入", String
                .valueOf(diancxxb_id), "否");
        String strPiaojh = MainGlobal.getXitxx_item("数量", "票据号手工录入", String
                .valueOf(diancxxb_id), "否");
        StringBuffer sb = new StringBuffer();
        sb
                .append("insert into chepb(id,xuh,piaojh, cheph, yuanmz, yuanpz, maoz, piz, biaoz, ");
        sb
                .append("koud, kous, kouz, zongkd, sanfsl, ches, jianjfs, guohb_id, ");
        sb
                .append("fahb_id, chebb_id, yuanmkdw, yunsdwb_id, qingcsj, qingchh, qingcjjy, ");
        sb
                .append("zhongcsj, zhongchh, zhongcjjy, meicb_id, daozch, lursj, lury, beiz , hedbz,xiecfsb_id,bulsj,zhuangcdw_item_id,meigy\n");
        sb.append(")");
        sb.append("(");
        sb.append("select id,getChepxh(").append(strlursj).append("), ");
        if (xwsql.equals("是")) {
            sb.append(" piaojh,");
        } else if (strPiaojh.equals("是")) {
            sb.append(" piaojh,");
        } else {
            sb.append("getLiush(").append(strlursj).append("),");
        }

        sb.append(" cheph,");
        sb
                .append(" yuanmz, yuanpz, maoz, piz, biaoz, koud, kous, kouz,koud+kous+kouz,sanfsl, ");
        sb
                .append("ches, jianjfs, guohb_id, fahb_id, chebb_id, yuanmkdw, yunsdwb_id,");
        sb
                .append(
                        " qingcsj, qingchh, qingcjjy, zhongcsj, zhongchh, zhongcjjy, meicb_id, daozch, ")
                .append(strlursj).append(",lury, beiz, ").append(hedbz);
        sb.append(",xiecfsb_id,bulsj,zhuangcdw_item_id,meigy");
        sb.append(" from cheplsb )");
        int flag = con.getInsert(sb.toString());
        if (flag == -1) {
            return -1;
        } else {

            // 判断票重是否减扣吨
            String sql = "select id from cheplsb";
            ResultSetList rsl = con.getResultSetList(sql);
            while (rsl.next()) {
                piaozPz(con, rsl.getString("id"), "chepb");
            }
        }
        return 0;
    }

    public static int InsChepb(JDBCcon con, long diancxxb_id, int yunsfs,
                               int hedbz) {
        String lursj = DateUtil.FormatOracleDateTime(new Date());
        StringBuffer sb = new StringBuffer();
        sb
                .append("insert into chepb(id,xuh,piaojh, cheph, yuanmz, yuanpz, maoz, piz, biaoz, ");
        sb.append("koud, kous, kouz,zongkd, sanfsl, ches, jianjfs, guohb_id, ");
        sb.append("fahb_id, chebb_id, yuanmkdw, yunsdwb_id, ");
        sb
                .append("meicb_id, daozch, lursj, lury, beiz , hedbz, xiecfsb_id,bulsj,zhuangcdw_item_id,meigy\n");
        if (yunsfs == SysConstant.YUNSFS_QIY) {
            sb.append(", zhongcsj, zhongcjjy, zhongchh ");
        }
        sb.append(")");
        sb.append("(");
        sb.append("select id,getChepxh(").append(lursj).append("),getLiush(")
                .append(lursj).append("), cheph,");
        sb
                .append(" yuanmz, yuanpz, maoz, piz, biaoz, koud, kous, kouz,koud+kous+kouz,sanfsl, ");
        sb
                .append("ches, jianjfs, guohb_id, fahb_id, chebb_id, yuanmkdw, yunsdwb_id, ");
        sb.append("meicb_id, daozch, ").append(lursj).append(",lury, beiz, ")
                .append(hedbz);
        sb.append(",xiecfsb_id,bulsj,zhuangcdw_item_id,meigy");
        if (yunsfs == SysConstant.YUNSFS_QIY) {
            sb.append(", zhongcsj, zhongcjjy, zhongchh ");
        }
        sb.append(" from cheplsb )");
        int flag = con.getInsert(sb.toString());
        if (flag == -1) {
            return -1;
        } else {

            // 判断票重是否减扣吨
            String sql = "select id from cheplsb";
            ResultSetList rsl = con.getResultSetList(sql);
            while (rsl.next()) {
                piaozPz(con, rsl.getString("id"), "chepb");
            }
        }
        return 0;
    }

    public static int updateLieid(JDBCcon con, String fahbid) {
        StringBuffer sb = new StringBuffer();
        String sql = "select * from xitxxb where mingc = '按到货日期分列ID' "
                + "and zhuangt = 1 and diancxxb_id = (select diancxxb_id from fahb"
                + " where id =" + fahbid + ") and zhi='是' and beiz='使用'";
        String daohrq = "";
        if (con.getHasIt(sql)) {
            daohrq = "||','||f.daohrq";
        }

        sb
                .append("select nvl(max(lie_id),0) lie_id from fahb f where ")
                .append(
                        "f.diancxxb_id||','||f.gongysb_id ||','||f.meikxxb_id||','||f.faz_id||','||f.fahrq||','||f.chec"
                                + daohrq + " = ")
                .append(
                        "(select f.diancxxb_id||','||f.gongysb_id ||','||f.meikxxb_id||','||f.faz_id||','||f.fahrq||','||f.chec"
                                + daohrq + " ")
                .append("from fahb f where id =").append(fahbid).append(")");
        // 增加参数按质量分列
        sql = "select * from xitxxb where mingc = '按质量分列' "
                + "and zhuangt = 1 and diancxxb_id = (select diancxxb_id from fahb"
                + " where id =" + fahbid + ") and zhi='是' and beiz='使用'";
        if (con.getHasIt(sql)) {
            sb.delete(0, sb.length());
            sb
                    .append(
                            "select nvl(max(f.lie_id),0) lie_id from fahb f where ")
                    .append(" zhilb_id = (")
                    .append(
                            "select decode(f.zhilb_id, 0, -1, f.zhilb_id) from fahb f where id =")
                    .append(fahbid).append(")");
        }

        ResultSetList rs = con.getResultSetList(sb.toString());
        if (rs == null)
            return -1;
        if (rs.next()) {
            sb.delete(0, sb.length());
            String lieid = rs.getString("lie_id");
            if ("0".equals(lieid)) {
                lieid = "getnewid(diancxxb_id)";
            }
            sb.append("update fahb set lie_id = ").append(lieid).append(
                    " where id =").append(fahbid);
        }
        return con.getUpdate(sb.toString());
    }

    public static int updateFahb(JDBCcon con, String fahbid) {
        return updateFahb(con, fahbid, null);
    }

    public static int updateFahb(JDBCcon con, String fahbid, Date Daohrq) {
        int flag = 0;
        StringBuffer sb = new StringBuffer();
        sb
                .append("select ")
                .append("sum(maoz) maoz, sum(piz) piz, sum(")
                .append(strJingz)
                .append(") as jingz,sum(yingd) yingd,\n")
                .append(
                        "sum(yingk) yingk, sum(yuns) yuns, sum(biaoz) biaoz, sum(koud) koud,\n ")
                .append(
                        "sum(kous) kous, sum(kouz) kouz, sum(sanfsl) sanfsl,count(id) ches,min(hedbz) hedbz from chepb \n")
                .append("where fahb_id=").append(fahbid);
        ResultSetList rsl = con.getResultSetList(sb.toString());
        while (rsl.next()) {
            sb.delete(0, sb.length());
            if (rsl.getInt("ches") == 0) {
                sb.append("delete from fahb where id=").append(fahbid);
                flag = con.getDelete(sb.toString());
            } else {
                sb.append("update fahb set maoz=")
                        .append(rsl.getDouble("maoz")).append(",piz=").append(
                        rsl.getDouble("piz")).append(",jingz=").append(
                        rsl.getDouble("jingz")).append(",biaoz=")
                        .append(rsl.getDouble("biaoz")).append(",yingd=")
                        .append(rsl.getDouble("yingd")).append(",yingk=")
                        .append(rsl.getDouble("yingk")).append(",yuns=")
                        .append(rsl.getDouble("yuns")).append(",koud=").append(
                        rsl.getDouble("koud")).append(",kous=").append(
                        rsl.getDouble("kous")).append(",kouz=").append(
                        rsl.getDouble("kouz")).append(",zongkd=")
                        .append(
                                rsl.getDouble("koud") + rsl.getDouble("kous")
                                        + rsl.getDouble("kouz")).append(
                        ",ches=").append(rsl.getDouble("ches")).append(
                        ",sanfsl=").append(rsl.getDouble("sanfsl"))
                        .append(",hedbz=").append(rsl.getInt("hedbz")).append(
                        ",laimsl=").append(rsl.getDouble("jingz")-rsl.getDouble("koud"))
                        .append(",laimzl=").append(rsl.getDouble("jingz"))
                        .append(",laimkc=").append(rsl.getDouble("jingz"));
                if (Daohrq != null) {
                    sb.append(",daohrq=").append(
                            DateUtil.FormatOracleDate(Daohrq));
                }
                sb.append(" where id=").append(fahbid);
                flag = con.getUpdate(sb.toString());
            }
        }
        return flag;
    }

    public static int CountFahbYuns(JDBCcon con, String fahbid) {
		/*
		 * ` 算法二 进行单车分配 具体算法为 假设该批次共有n车 一 首先计算单车运损 1、有 盈吨 时 单车运损[1...n] = 0;
		 * 2、亏吨 > 标准运损 时，单车运损全部计为标准运损，算盈亏 单车运损[1...n] = 单车票重[1...n] * 运损率; 3、亏吨 <
		 * 标准运损 时， 反推运损率 = round(总亏吨/总票重,4); 单车运损[1...(n-1)] =
		 * round(单车票重[1...(n-1)] * 反推运损率,3); 单车运损n = 总亏吨 - sum(单车运损[1...(n-1)]);
		 * 二 计算单车盈亏吨 1、单车盈亏 = 单车毛重 - 单车皮重 - 单车票重 + 单车运损 2、单车盈亏 >= 0 单车盈吨 = 单车盈亏;
		 * 3、单车盈亏 < 0 单车亏吨 = 单车盈亏;
		 */
        double Jingz = 0.0;
        double Biaoz = 0.0;
        double Yunsl = 0.0;
        double Yingd = 0.0;
        double Kuid = 0.0;
        double Yuns = 0.0;
        double yunsl = 0.0;
        double Yingk = 0.0;
        int flag = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("select ").append(strJingz).append(
                " jingz,biaoz,yunsl from fahb where id=").append(fahbid);
        ResultSetList rs = con.getResultSetList(sb.toString());
        if (rs.next()) {
            Biaoz = rs.getDouble("Biaoz");
            Yunsl = rs.getDouble("Yunsl");
            Jingz = rs.getDouble("jingz");
            Yingk = CustomMaths.sub(Jingz, Biaoz);
            Yuns = CustomMaths.mul(Biaoz, Yunsl);
            // 如果盈亏大于零
            if (Yingk >= 0) {
                Yuns = 0;
                yunsl = 0;
                Yingd = Yingk;
                Kuid = 0;
            } else if (Math.abs(Yingk) <= Yuns) {
                Yuns = Math.abs(Yingk);
                Yingd = 0;
                Kuid = 0;
                yunsl = CustomMaths.div(Yuns, Biaoz, 4);
            } else {
                yunsl = Yunsl;
                Yingd = 0;
                Kuid = CustomMaths.sub(Math.abs(Yingk), Yuns);
            }
            sb.delete(0, sb.length());
            sb.append("update chepb set yuns = round_new(biaoz*").append(yunsl)
                    .append(",3) where guohb_id != 0 and fahb_id=").append(
                    fahbid);
            flag = con.getUpdate(sb.toString());
            if (flag == -1) {
                return -1;
            }
            if (Yingk < 0) {
                sb.delete(0, sb.length());
                sb
                        .append(
                                "select max(id) id,sum(yuns) yuns from chepb where guohb_id != 0 and fahb_id=")
                        .append(fahbid);
                rs = con.getResultSetList(sb.toString());
                if (rs.next()) {
                    sb.delete(0, sb.length());
                    sb.append("update chepb set yuns = ").append(Yuns).append(
                            "-").append(rs.getDouble("yuns")).append(
                            "+ yuns where id=").append(rs.getLong("id"));
                    flag = con.getUpdate(sb.toString());
                    if (flag == -1) {
                        return -1;
                    }
                }
            }
            sb.delete(0, sb.length());
            sb.append("update chepb set yingk=").append(strJingz).append(
                    "-biaoz+yuns, ").append("yingd = case when (").append(
                    strJingz).append("-biaoz+yuns)>0 ").append("then (")
                    .append(strJingz).append("-biaoz+yuns) else 0 end").append(
                    " where guohb_id != 0 and fahb_id = ").append(
                    fahbid);
            flag = con.getUpdate(sb.toString());
            if (flag == -1) {
                return -1;
            }
            sb.delete(0, sb.length());
            sb.append("update fahb set yingd=").append(Yingd).append(",Yingk=")
                    .append(Yingd - Kuid).append(",yuns=").append(Yuns).append(
                    " where id=").append(fahbid);
            flag = con.getUpdate(sb.toString());
        }
        return flag;
    }

    public static int CountChepbYuns(JDBCcon con, String chepbid, int hedbz) {
        double Jingz = 0.0;
        double Biaoz = 0.0;
        double Yunsl = 0.0;
        double Yingd = 0.0;
        double Kuid = 0.0;
        double Yuns = 0.0;
        double Yingk = 0.0;
        int returnValue = 0;// 代表
        StringBuffer sb = new StringBuffer();
        sb
                .append(
                        "select yunsl from fahb where id = (select distinct fahb_id from chepb where id = ")
                .append(chepbid).append(")");
        ResultSetList rsl = con.getResultSetList(sb.toString());
        if (rsl.next()) {
            Yunsl = rsl.getDouble("yunsl");
        }
        sb.delete(0, sb.length());
        sb.append("select ").append(strJingz).append(
                " jingz,biaoz from chepb where id=").append(chepbid);
        rsl = con.getResultSetList(sb.toString());
        while (rsl.next()) {
            Biaoz = rsl.getDouble("biaoz");
            Jingz = rsl.getDouble("jingz");
            if (Jingz != 0.0 && Biaoz != 0.0) {
                Yingk = CustomMaths.sub(Jingz, Biaoz);
                Yuns = CustomMaths.mul(Biaoz, Yunsl);
                if (Yingk >= 0) {
                    Yuns = 0;
                    Yingd = Yingk;
                    Kuid = 0;
                } else {
                    if (Math.abs(Yingk) <= Yuns) {
                        Yuns = Math.abs(Yingk);
                        Yingd = 0;
                        Kuid = 0;
                    } else {
                        // Yuns = Yuns;
                        Yingd = 0;
                        Kuid = CustomMaths.sub(Math.abs(Yingk), Yuns);
                    }
                }
                Yingk = Yingd - Kuid;
                sb.delete(0, sb.length());
                String strsqltmp = "hedbz=" + hedbz + ",";
                if (hedbz == -1) {
                    strsqltmp = "";
                }
                sb.append("update chepb set ").append(strsqltmp).append(
                        "yingd=").append(Yingd).append(",yingk=").append(Yuns)
                        .append(",yuns=").append(Yingk).append(" where id=")
                        .append(chepbid);
                returnValue = con.getUpdate(sb.toString());
            }
        }
        return returnValue;
    }

    public static double getYunsl(long diancxxb_id, long pinzb_id,
                                  int yunsfsb_id, long meikxxb_id) {
        JDBCcon con = new JDBCcon();
        String sql = "";
        ResultSetList rsl;
        double yunsl = 0.012;
        String fs = "";
        if (yunsfsb_id == SysConstant.YUNSFS_HUOY) {
            yunsl = 0.012;
            fs = "火车";
        } else if (yunsfsb_id == SysConstant.YUNSFS_QIY) {
            yunsl = 0.01;
            fs = "汽车";
        }
        sql = "select * from xitxxb where mingc ='默认运损率' and danw ='"
                + fs
                + "' and leib = '数量' and beiz ='使用' and zhuangt =1 and diancxxb_id="
                + diancxxb_id;
        rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            yunsl = rsl.getDouble("zhi");
        }
        sql = "select yunsl from yunslb where diancxxb_id=" + diancxxb_id
                + " and pinzb_id=" + pinzb_id + " and yunsfsb_id ="
                + yunsfsb_id + " and meikxxb_id=" + meikxxb_id;
        rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            yunsl = rsl.getDouble("yunsl");
        }
        return yunsl;
    }

    public static boolean SaveChelxx(JDBCcon con, long diancxxb_id,
                                     long yunsdwb_id, long yunsfsb_id, String cheph, double maoz,
                                     double piz) {
        StringBuffer callSql = new StringBuffer();
        callSql.append("{call savechelxx(").append(diancxxb_id).append(",")
                .append(yunsdwb_id).append(",").append(yunsfsb_id).append(",'")
                .append(cheph).append("',").append(maoz).append(",")
                .append(piz).append(")}");
        return con.UpdateCall(callSql.toString());
    }

    public static void addFahid(List list, String fahid) {
        if (list == null) {
            list = new ArrayList();
        }
        int i = 0;
        for (; i < list.size(); i++) {
            if (((String) list.get(i)).equals(fahid)) {
                break;
            }
        }
        if (i == list.size()) {
            list.add(fahid);
        }
    }

    public static String filterDcid(Visit v, String Prefix) {
        Prefix = Prefix == null || "".equals(Prefix) ? "" : Prefix + ".";
        String sqltmp = " and  " + Prefix + "diancxxb_id = "
                + v.getDiancxxb_id();
        if (v.isFencb()) {
            JDBCcon con = new JDBCcon();
            ResultSetList rsl = con
                    .getResultSetList("select id from diancxxb where fuid="
                            + v.getDiancxxb_id());
            sqltmp = "";
            while (rsl.next()) {
                sqltmp += "or " + Prefix + "diancxxb_id = "
                        + rsl.getString("id") + " ";
            }
            sqltmp = " and (" + sqltmp.substring(2) + ") ";
            rsl.close();
            con.Close();
        }
        return sqltmp;
    }

    public static String SaveJilData(JDBCcon con, String sql, long diancxxb_id,
                                     int yunsfs, int hedbz, String lursj, String ClassName,
                                     int SaveMode, String tmpid, boolean writeghb) {

        if (sql == null || "".equals(sql)) {
            WriteLog.writeErrorLog(ErrorMessage.NullSql + "\n\t\t" + ClassName);
            return ErrorMessage.NullSql;
        }
        // JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        String AddDaohrq = "";
        // 判断是否单车计算运损
        boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal
                .getXitxx_item("数量", "运损计算方法", String.valueOf(diancxxb_id),
                        "单车"));
        String paramSql = "";
        // 判断补录是否自动生成采样编号
        String caiybh = null;
        if (SaveMode_BL == SaveMode) {
            String danw = "火车";
            if (yunsfs == SysConstant.YUNSFS_HUOY) {
                danw = "火车";
            } else if (yunsfs == SysConstant.YUNSFS_QIY) {
                danw = "汽车";
            }
            paramSql = "select * from xitxxb where zhi = '否' and leib = '数量' and mingc = '补录自动生成编号' and danw = '"
                    + danw
                    + "' and zhuangt = 1 and diancxxb_id ="
                    + diancxxb_id;
            if (con.getHasIt(paramSql)) {
                caiybh = "";
            }
        }
        // 插入车皮临时表
        int flag = con.getInsert(sql);
        if (flag == -1) {
            con.rollBack();
            con.Close();
            WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
                    + sql + "\n\t\t" + ClassName);
            return ErrorMessage.InesrtCheplsbFailed;
        }
        String strSql = "select distinct yunsfsb_id,zhongcsj,chec,daohrq from cheplsb ";
        if (writeghb) {
            ResultSetList rstmp;
            boolean IsGetNewId = false;
            String moreShuj = "select zhi from xitxxb x where x.mingc='汽车数据导入单行车数是否超过300车'  and zhuangt=1";
            rstmp = con.getResultSetList(moreShuj);
            if (rstmp.next()) {
                String zhi = rstmp.getString("zhi");
                if (zhi.equals("是")) {
                    IsGetNewId = true;
                }
            }

            rstmp = con.getResultSetList(strSql);
            while (rstmp.next()) {
                String upsql = "";
                String guohb_id = "";
                if (IsGetNewId) {
                    // 当汽车数据导入单行数据超过300车时,getNewID方法如果不传递con参数,会导致oracle并发连接数用尽,致使连接失败.
                    guohb_id = MainGlobal.getNewID(con, diancxxb_id);
                } else {
                    guohb_id = MainGlobal.getNewID(con,diancxxb_id);
                }

                String guohsj = "";
                if (rstmp.getInt("yunsfsb_id") == SysConstant.YUNSFS_HUOY) {
                    guohsj = DateUtil.FormatOracleDateTime(rstmp
                            .getDateTimeString("zhongcsj"));
                    upsql = "update cheplsb set guohb_id ="
                            + guohb_id
                            + " where yunsfsb_id="
                            + SysConstant.YUNSFS_HUOY
                            + " and "
                            + " zhongcsj = "
                            + DateUtil.FormatOracleDateTime(rstmp
                            .getDateTimeString("zhongcsj"));
                    con.getUpdate(upsql);
                    upsql = "insert into guohb(id,guohsj,wenjm) values("
                            + guohb_id + "," + guohsj + ",'"
                            + rstmp.getString("chec") + "')";
                    con.getInsert(upsql);
                } else if (rstmp.getInt("yunsfsb_id") == SysConstant.YUNSFS_QIY) {
                    guohsj = DateUtil.FormatOracleDate(rstmp
                            .getDateString("zhongcsj"));
                    upsql = "update cheplsb set guohb_id ="
                            + guohb_id
                            + " where yunsfsb_id="
                            + SysConstant.YUNSFS_QIY
                            + " and "
                            + " zhongcsj = "
                            + DateUtil.FormatOracleDateTime(rstmp
                            .getDateTimeString("zhongcsj"));
                    AddDaohrq = rstmp.getDateString("daohrq");
                    con.getUpdate(upsql);
                }

            }
            rstmp.close();
        }
        String xwsql = MainGlobal.getXitxx_item("数量", "宣威汽车数据导入", String
                .valueOf(diancxxb_id), "否");
        if (xwsql.equals("是")) {

        } else {
            // 更新车皮临时表质量id生成采样编号
            flag = Jilcz.Updatezlid(con, diancxxb_id, yunsfs, caiybh);
            if (flag == -1) {
                con.rollBack();
                con.Close();
                WriteLog.writeErrorLog(ErrorMessage.UpdatezlidFailed + "\n\t\t"
                        + ClassName);
                return ErrorMessage.UpdatezlidFailed;
            }else if(flag ==-2){
                con.rollBack();
                con.Close();
                WriteLog.writeErrorLog(ErrorMessage.CaiyjBmFailed + "\n\t\t"
                        + ClassName);
                return ErrorMessage.CaiyjBmFailed;
            }
        }

        // 根据车皮临时表分组生成发货
        flag = Jilcz.INSorUpfahb(con, diancxxb_id);
        if (flag == -1) {
            con.rollBack();
            con.Close();
            WriteLog.writeErrorLog(ErrorMessage.INSorUpfahbFailed + "\n\t\t"
                    + ClassName);
            return ErrorMessage.INSorUpfahbFailed;
        }
        // 写入车皮表将核对标志设置为9
        flag = Jilcz.InsChepb(con, diancxxb_id, null, hedbz);
        if (flag == -1) {
            con.rollBack();
            con.Close();
            WriteLog.writeErrorLog(ErrorMessage.InsChepbFailed + "\n\t\t"
                    + ClassName);
            return ErrorMessage.InsChepbFailed;
        }
        // 将车皮表核对标志设置为9为后续算fahb_id 做准备
        paramSql = "select distinct fahb_id from cheplsb";
        ResultSetList rsl = con.getResultSetList(paramSql);
        List fhlist = new ArrayList();
        while (rsl.next()) {
            // 如果是单批次计算运损则更新车皮
            if (isDancYuns) {
                strSql = "select id from chepb where fahb_id="
                        + rsl.getString("fahb_id");
                ResultSetList cheprs = con.getResultSetList(strSql);
                while (cheprs.next()) {
                    flag = Jilcz.CountChepbYuns(con, cheprs.getString("id"),
                            hedbz);
                    if (flag == -1) {
                        con.rollBack();
                        con.Close();
                        WriteLog
                                .writeErrorLog(ErrorMessage.CountChepbYunsFailed
                                        + "\n\t\t" + ClassName);
                        return ErrorMessage.CountChepbYunsFailed;
                    }
                }
            }
            // 计算fahbid的list
            Jilcz.addFahid(fhlist, rsl.getString("fahb_id"));
        }
        // 循环此次操作用到的fahb记录
        for (int i = 0; i < fhlist.size(); i++) {
            // 更新发货表
            flag = Jilcz.updateFahb(con, (String) fhlist.get(i));
            if (flag == -1) {
                con.rollBack();
                con.Close();
                WriteLog.writeErrorLog(ErrorMessage.updateFahbFailed + "\n\t\t"
                        + ClassName);
                return ErrorMessage.updateFahbFailed;
            }
            // 更新发货表列ID
            flag = Jilcz.updateLieid(con, (String) fhlist.get(i));
            if (flag == -1) {
                con.rollBack();
                con.Close();
                WriteLog.writeErrorLog(ErrorMessage.updateLieidFailed
                        + "\n\t\t" + ClassName);
                return ErrorMessage.updateLieidFailed;
            }
            // 如果是按批次计算运损则计算并更新到单车中
            if (!isDancYuns) {
                flag = Jilcz.CountFahbYuns(con, (String) fhlist.get(i));
                if (flag == -1) {
                    con.rollBack();
                    con.Close();
                    WriteLog.writeErrorLog(ErrorMessage.CountFahbYunsFailed
                            + "\n\t\t" + ClassName);
                    return ErrorMessage.CountFahbYunsFailed;
                }
            }
        }
        if (SaveMode_DR == SaveMode) {
            String sqltmp = "";
            if (tmpid != null && !"".equals(tmpid)) {

                String tiaojpd = MainGlobal.getXitxx_item("数量",
                        "汽车数据导入更新chepbtmp中fahb_id字段时是否增加到货日期的判断", String
                                .valueOf(diancxxb_id), "否");
                if (tiaojpd.equals("是")) {
                    sqltmp = " and fahbtmp_id in (" + tmpid
                            + ") and daohrq=to_date('" + AddDaohrq
                            + "','yyyy-mm-dd')";
                } else {
                    sqltmp = " and fahbtmp_id in (" + tmpid + ")";
                }
            }
            paramSql = "update chepbtmp set fahb_id = 1 where fahb_id = 0 "// and
                    // diancxxb_id="
                    // +
                    // diancxxb_id
                    + sqltmp;
            flag = con.getUpdate(paramSql);
            if (flag == -1) {
                con.rollBack();
                con.Close();
                WriteLog.writeErrorLog(ErrorMessage.ShujdrBjFailed + "\n\t\t"
                        + ClassName);
                return ErrorMessage.ShujdrBjFailed;
            }
        }

//		增加邯郸编码特殊需求
        if(MainGlobal.getXitxx_item("数量", "邯郸采制化编码特殊设置", "0","否").equals("是")){
            for (int i = 0; i < fhlist.size(); i++) {
                if (yunsfs == SysConstant.YUNSFS_HUOY) {
//				火运更新ZHUANMB的编码
                    String errMsg=UpdZhuanmb(con, (String) fhlist.get(i));
                    if(errMsg.length()>0){
                        con.rollBack();
                        con.Close();
                        WriteLog.writeErrorLog("数据导入时，新增火车采样失败"+ "\n\t\t"
                                + ClassName);
                        return errMsg;
                    }
                } else if (yunsfs == SysConstant.YUNSFS_QIY) {
//				汽运根据接口视图插入相关采样信息
                    flag = Jilcz.SaveZhuanmb(con, (String) fhlist.get(i));
                    if (flag == -1) {
                        con.rollBack();
                        con.Close();
                        WriteLog.writeErrorLog("数据导入时，新增采样失败"+ "\n\t\t"
                                + ClassName);
                        return "数据导入时，新增采样失败";
                    }
                }
            }
        }

        Chengbjs.CountChengb(con, diancxxb_id, fhlist);

        return ErrorMessage.SaveSuccessMessage;
    }

    public static String UpdZhuanmb(JDBCcon con, String fahbid){
        String zhilb_sql=
                "SELECT L.ZHUANMSZ,\n" +
                        "       Z.ID ZHILLSB_ID,\n" +
                        "       F.DIANCXXB_ID\n" +
                        "  FROM CAIYB Y, ZHILLSB Z, FAHB F, YANGPDHB D, LEIBB L\n" +
                        " WHERE Y.ZHILB_ID = Z.ZHILB_ID\n" +
                        "   AND Z.ID = D.ZHILBLSB_ID\n" +
                        "   AND D.LEIBB_ID = L.ID\n" +
                        "   AND Z.ZHILB_ID = F.ZHILB_ID\n" +
                        "   AND F.ID = "+fahbid;
        ResultSetList rsl=con.getResultSetList(zhilb_sql);
        while(rsl.next()){
            String zhuanmsz=rsl.getString("ZHUANMSZ");
            String zhillsb_id=rsl.getString("ZHILLSB_ID");

            String cybm_sql="SELECT  CAIYBM FROM\n" +
                    "(SELECT CAIYBM FROM VW_HCMBM\n" +
                    "MINUS\n" +
                    "SELECT CASE\n" +
                    "         WHEN LENGTH(M.BIANM) > 1 THEN\n" +
                    "          SUBSTR(M.BIANM, 2, LENGTH(M.BIANM))\n" +
                    "         ELSE\n" +
                    "          M.BIANM\n" +
                    "       END BIANM\n" +
                    "  FROM ZHILLSB Z,\n" +
                    "       FAHB F,\n" +
                    "       ZHUANMB M,\n" +
                    "       ZHUANMLB L,\n" +
                    "       (SELECT DAOHRQ FROM FAHB WHERE ID = "+fahbid+") RQ\n" +
                    " WHERE F.ZHILB_ID = Z.ZHILB_ID\n" +
                    "   AND M.ZHUANMLB_ID = L.ID\n" +
                    "   AND M.ZHILLSB_ID = Z.ID\n" +
                    "   AND L.JIB = 1\n" +
                    "   AND F.DAOHRQ BETWEEN RQ.DAOHRQ-1 AND RQ.DAOHRQ\n" +
                    "   AND F.YUNSFSB_ID = 1\n" +
                    "   ORDER BY 1)\n" +
                    "   WHERE ROWNUM=1";
            String CAIYBM="";
            ResultSetList zmb_rsl=con.getResultSetList(cybm_sql);
            while(zmb_rsl.next()){
                CAIYBM=zmb_rsl.getString("CAIYBM");
            }
            if(zmb_rsl == null  || zmb_rsl.getRows() == 0){
                zmb_rsl.close();
                rsl.close();
                return "火车采样编码已用尽，请联系相关维护人员";
            }

//			写入转码表
            String sql =
                    "SELECT Z.ID, Z.BIANM\n" +
                            "  FROM ZHUANMLB L, ZHUANMB Z\n" +
                            " WHERE Z.ZHUANMLB_ID = L.ID\n" +
                            "   AND Z.ZHILLSB_ID = "+zhillsb_id+"\n" +
                            " ORDER BY L.JIB";
            zmb_rsl =  con.getResultSetList(sql);

            int flag=1;
            String bianm="";
            String upd_zmb="begin \n ";
            while(zmb_rsl.next()){
                bianm=bianm+zhuanmsz;
                upd_zmb+=" update (select bianm from ZHUANMB where id ="+zmb_rsl.getString("ID")+") set bianm= '"+bianm+CAIYBM+"';\n";
            }
            upd_zmb+="end;";
            if(upd_zmb.length()>20){
                flag=con.getUpdate(upd_zmb);
            }
            if(flag == -1){
//				如果出错则返回错误
                return "更新火车采样编码时出错，请联系相关维护人员";
            }
        }
        return "";
    }

    public static int SaveZhuanmb(JDBCcon con, String fahbid){
//		通过发货找到ZHILB_ID,CAIYSJ,BEIZ等信息
        String caiyrq="";
        long zhilb_id=0;
        long diancxxb_id=0;
        String beiz="";
        String caiyb_id="";
        String zhuanmsz = "";
        String bumb_id = "";

        String upd_cyml=
                "UPDATE (SELECT BIL\n" +
                        "          FROM ZHILLSB\n" +
                        "         WHERE ZHILB_ID = (SELECT ZHILB_ID FROM FAHB WHERE ID = "+fahbid+"))\n" +
                        "   SET BIL =\n" +
                        "       (SELECT JINGZ FROM FAHB WHERE ID = "+fahbid+")";
        con.getUpdate(upd_cyml);

        String Fhxx_sql="SELECT Y.ID CAIYB_ID,\n" +
                "       TO_CHAR(Y.CAIYRQ, 'yyyy-mm-dd') CAIYSJ,\n" +
                "       D.BUMB_ID,\n" +
                "       L.ZHUANMSZ,\n" +
                "       F.ZHILB_ID,F.DIANCXXB_ID\n" +
                "  FROM CAIYB Y, ZHILLSB Z, FAHB F, YANGPDHB D, LEIBB L\n" +
                " WHERE Y.ZHILB_ID = Z.ZHILB_ID\n" +
                "   AND Z.ID = D.ZHILBLSB_ID\n" +
                "   AND D.LEIBB_ID = L.ID\n" +
                "   AND Z.ZHILB_ID = F.ZHILB_ID\n" +
                "   AND F.ID = "+fahbid;

        ResultSetList rsl=con.getResultSetList(Fhxx_sql);
        while(rsl.next()){
//			得到采样时间
            caiyrq=rsl.getString("CAIYSJ");
//			得到质量ID
            zhilb_id=rsl.getLong("ZHILB_ID");
//			得到电厂ID
            diancxxb_id=rsl.getLong("DIANCXXB_ID");
//			得到采样ID
            caiyb_id=rsl.getString("CAIYB_ID");
//			得到部门
            bumb_id = rsl.getString("bumb_ID");
//			得到转码设置
            zhuanmsz = rsl.getString("ZHUANMSZ");
        }

        String sql=
                "SELECT CY.CAIYFS, NVL(L.ID, -1) LEIB, CAIYBM, JICBM, SHUL, CHES\n" +
                        "  FROM (SELECT CAIYFS, CAIYBM, JICBM, SHUL, CHES\n" +
                        "          FROM VW_CYBMDZ V\n" +
                        "         WHERE V.JICBM = (SELECT CASE\n" +
                        "                                   WHEN LENGTH(M.BIANM) > 3 THEN\n" +
                        "                                    SUBSTR(M.BIANM, 4, LENGTH(M.BIANM))\n" +
                        "                                   ELSE\n" +
                        "                                    M.BIANM\n" +
                        "                                 END BIANM\n" +
                        "                            FROM ZHILLSB Z, FAHB F, ZHUANMB M, ZHUANMLB L\n" +
                        "                           WHERE F.ZHILB_ID = Z.ZHILB_ID\n" +
                        "                             AND M.ZHUANMLB_ID = L.ID\n" +
                        "                             AND M.ZHILLSB_ID = Z.ID\n" +
                        "                             AND L.JIB = 3\n" +
                        "                             AND F.ID = "+fahbid+")) CY,\n" +
                        "       LEIBB L\n" +
                        " WHERE CY.CAIYFS = L.MINGC(+)";

        rsl=con.getResultSetList(sql);
        while (rsl.next()){
            long leibb_id=rsl.getLong("LEIB");
            String leib=rsl.getString("CAIYFS");
//			如果类别未取到则为错误类型，且不予保存。
            if(leibb_id==-1){
                rsl.close();
                return -1;
            }
            String caiybm=rsl.getString("CAIYBM");
            String caiyml=rsl.getString("SHUL");

//			添加编码
            int flag = Caiycl.AddBianhHD(con, zhilb_id, diancxxb_id,
                    caiyb_id, leibb_id+"", bumb_id, leib,zhuanmsz,caiyrq,beiz,caiybm,caiyml);
            if(flag==-1){
//		    	新增采样失败
                rsl.close();
                return -1;
            }
        }
        rsl.close();
        return 1;
    }

    public static String SaveJilData(JDBCcon con, String sql, long diancxxb_id,
                                     int yunsfs, int hedbz, String lursj, String ClassName,
                                     int SaveMode, String tmpid) {
        return SaveJilData(con, sql, diancxxb_id, yunsfs, hedbz, lursj,
                ClassName, SaveMode, tmpid, true);
    }

    public static String SaveJilData(String sql, long diancxxb_id, int yunsfs,
                                     int hedbz, String lursj, String ClassName, int SaveMode,
                                     String tmpid) {
        JDBCcon con = new JDBCcon();
        String revalue = SaveJilData(con, sql, diancxxb_id, yunsfs, hedbz,
                lursj, ClassName, SaveMode, tmpid);
        if (revalue.equals(ErrorMessage.SaveSuccessMessage)) {
            con.commit();
            con.Close();
        }
        return revalue;
    }

    public static String SaveJilData(String sql, long diancxxb_id, int yunsfs,
                                     int hedbz, String lursj, String ClassName, int SaveMode) {
        JDBCcon con = new JDBCcon();
        String revalue = SaveJilData(con, sql, diancxxb_id, yunsfs, hedbz,
                lursj, ClassName, SaveMode, null);
        if (revalue.equals(ErrorMessage.SaveSuccessMessage)) {
            con.commit();
            con.Close();
        }
        return revalue;
    }

    public static String getChecSql(String sql, long diancxxb_id,String time) {
        JDBCcon con = new JDBCcon();
        ResultSetList rs = con.getResultSetList(sql);
        String revalue = "";
        String SQL = "";
        // rs.getRow();
        if (rs.next()) {
            revalue = rs.getString("chec").substring(0, 3);
            ;
        }
        if (revalue.equals(DateUtil.GetDayOfWeektoSting())) {
            SQL = "select rownum as id, t.chec\n"
                    + "  from (Select distinct chec\n"
                    + "          From Fahb f, chepb c\n"
                    + "         Where f.id = c.fahb_id\n"
                    + "           and f.yunsfsb_id = "
                    + SysConstant.YUNSFS_HUOY
                    + "\n"
                    + "           and f.diancxxb_id = "
                    + diancxxb_id
                    + "\n"
                    + "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
                    + "               to_date(to_char("+time+", 'yyyy-mm-dd'), 'yyyy-mm-dd')\n"
                    + "        union\n"
                    + "        Select to_char(sysdate, 'DY') ||\n"
                    + "               to_char(max(nvl(to_number(replace(chec,\n"
                    + "                                                 to_char(sysdate, 'DY'),\n"
                    + "                                                 '')),\n"
                    + "                               0)) + 1) as chec\n"
                    + "          From fahb f, chepb c\n"
                    + "         Where f.id = c.fahb_id\n"
                    + "           and f.yunsfsb_id = "
                    + SysConstant.YUNSFS_HUOY
                    + "\n"
                    + "           and f.diancxxb_id = "
                    + diancxxb_id
                    + "\n"
                    + "           and substr(chec, 1, 3) like to_char(sysdate, 'DY')\n"
                    + "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
                    + "               to_date(to_char("+time+", 'yyyy-mm-dd'), 'yyyy-mm-dd')) t\n"
                    + " order by t.chec desc";

        } else {
            SQL = "Select rownum as id, t.chec\n"
                    + "  From (Select Distinct f.chec\n"
                    + "          From Fahb f, chepb c\n"
                    + "         Where f.id = c.fahb_id\n"
                    + "           and f.yunsfsb_id = "
                    + SysConstant.YUNSFS_HUOY
                    + "\n"
                    + "           and f.diancxxb_id = "
                    + diancxxb_id
                    + "\n"
                    + "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
                    + "               to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')\n"
                    + "        union\n"
                    + "        select to_char(sysdate, 'DY') || '1' as chec from dual) t\n"
                    + " Order By chec desc";
        }
        return SQL;
    }

    // 判断某个矿的票重是否减扣吨
    public static void piaozPz(JDBCcon con, String tableid, String tablename) {
        //		String meikxxb_id = "";
        //		String jihkjb_id = "";
        //		String yunsfsb_id = "";
        //		String pinzb_id = "";
        //
        //		String sql = "select meikxxb_id, jihkjb_id, yunsfsb_id, pinzb_id from fahb where id = " +
        //						"(select fahb_id from chepb where id = " + tableid + ")";
        //
        //		if (tablename.equals("fahb")) {
        //			sql = "select meikxxb_id, jihkjb_id, yunsfsb_id, pinzb_id from fahb where id = " + tableid;
        //		}
        //
        //		ResultSetList rsl = con.getResultSetList(sql);
        //
        //		if (rsl.next()) {
        //			meikxxb_id = rsl.getString("meikxxb_id");
        //			jihkjb_id = rsl.getString("jihkjb_id");
        //			yunsfsb_id = rsl.getString("yunsfsb_id");
        //			pinzb_id = rsl.getString("pinzb_id");
        //		}
        //
        //		sql = "select * from piaozpzb where meikxxb_id = " + meikxxb_id;
        //		rsl = con.getResultSetList(sql);
        //
        //		if (!rsl.next()) {
        //
        //			sql = "update " + tablename + " set biaoz = " + strJingz + " where piz>0 and " +
        //								"id = " + tableid;
        //
        //			con.getUpdate(sql);
        //
        //		} else {
        //
        //			rsl.beforefirst();
        //			while (rsl.next()) {
        //
        //				if (jihkjb_id.equals(rsl.getString("jihkjb_id")) && yunsfsb_id.equals(rsl.getString("yunsfsb_id"))
        //						&& pinzb_id.equals(rsl.getString("pinzb_id"))) {
        //
        //				} else {
        //
        //					sql = "update " + tablename + " set biaoz = " + strJingz + " where  piz>0 and " +
        //										"id = " + tableid;
        //
        //					con.getUpdate(sql);
        //				}
        //			}
        //		}
    }
}