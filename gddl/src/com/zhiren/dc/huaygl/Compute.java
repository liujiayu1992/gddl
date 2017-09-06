package com.zhiren.dc.huaygl;

import bsh.EvalError;
import bsh.Interpreter;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

/*
 * 修改人：ww
 * 修改时间：2009-09-18
 * 修改内容：
 * 		添加getYuansValue()方法的重载方法解决
 * 		一厂多制相同矿在不同厂别下的氢值不同，
 * 		根据该厂别的id取出该厂别下某一矿的氢值
 */
public class Compute {

    /**
     * @param args
     */
    public static final String yuans_H = "氢";
    public static final String yuans_S = "硫";
    public static final String hyType_RC = "入厂";
    public static final String hyType_RL = "入炉";

    public static void UpdateRulmzl(JDBCcon con, String rulriq, String diancxxb_id) {
        String sql = "select id from rulmzlb where rulrq = " + rulriq + " and diancxxb_id=" + diancxxb_id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.getRows() > 0) {
            rsl.close();
            sql = "select diancxxb_id,round_new(avg(Qnet_ar),2) qnet_ar, round_new(avg(aar),2) aar, round_new(avg(ad),2) ad,\n" +
                    "round_new(avg(vdaf),2) vdaf, round_new(avg(mt),1) mt, round_new(avg(stad),2) stad, round_new(avg(aad),2) aad,\n" +
                    "round_new(avg(mad),2) mad, round_new(avg(qbad),2) qbad, round_new(avg(had),2) had, round_new(avg(vad),2) vad,\n" +
                    "round_new(avg(std),2) std, round_new(avg(qgrad),2) qgrad, round_new(avg(hdaf),2) hdaf, round_new(avg(sdaf),2) sdaf,\n" +
                    "round_new(avg(var),2) var, round_new(avg(fcad),2) fcad, round_new(max(huayy),2) huayy, max(beiz) beiz,\n" +
                    "max(lursj) lursj, max(lury) lury, round_new(avg(har),2) har, round_new(avg(qgrd),2) qgrd, round_new(avg(qgrad_daf),2) qgrad_daf\n" +
                    "from rulmzlzb where rulrq =" + rulriq + " and diancxxb_id=" + diancxxb_id + " group by diancxxb_id";
            rsl = con.getResultSetList(sql);
            while (rsl.next()) {
                sql = "update rulmzlb set qnet_ar =" + rsl.getDouble("qnet_ar") +
                        ",aar = " + rsl.getDouble("aar") +
                        ",ad = " + rsl.getDouble("ad") +
                        ",vdaf = " + rsl.getDouble("vdaf") +
                        ",mt = " + rsl.getDouble("mt") +
                        ",stad = " + rsl.getDouble("stad") +
                        ",aad = " + rsl.getDouble("aad") +
                        ",mad = " + rsl.getDouble("mad") +
                        ",qbad = " + rsl.getDouble("qbad") +
                        ",had = " + rsl.getDouble("had") +
                        ",vad = " + rsl.getDouble("vad") +
                        ",std = " + rsl.getDouble("std") +
                        ",qgrad = " + rsl.getDouble("qgrad") +
                        ",hdaf = " + rsl.getDouble("hdaf") +
                        ",sdaf = " + rsl.getDouble("sdaf") +
                        ",var = " + rsl.getDouble("var") +
                        ",fcad = " + rsl.getDouble("fcad") +
                        ",har = " + rsl.getDouble("har") +
                        ",qgrd = " + rsl.getDouble("qgrd") +
                        ",qgrad_daf = " + rsl.getDouble("qgrad_daf") +
                        ",huayy = '" + rsl.getString("huayy") + "'" +
                        ",lury = '" + rsl.getString("lury") + "'" +
                        ",lursj = " + DateUtil.FormatOracleDateTime(rsl.getDateTimeString("lursj")) +
                        ",shenhzt=1" +
                        ",beiz = '" + rsl.getDouble("beiz") + "'" +
                        " where rulrq = " + rulriq + " and diancxxb_id =" + rsl.getString("diancxxb_id");
                con.getUpdate(sql);
            }
            rsl.close();
        } else {
            rsl.close();
            sql = "select diancxxb_id,rulrq,max(fenxrq) as fenxrq,max(rulbzb_id)as rulbzb_id,max(jizfzb_id) as jizfzb_id,round_new(avg(Qnet_ar),2) qnet_ar, round_new(avg(aar),2) aar, round_new(avg(ad),2) ad,\n" +
                    "round_new(avg(vdaf),2) vdaf, round_new(avg(mt),1) mt, round_new(avg(stad),2) stad, round_new(avg(aad),2) aad,\n" +
                    "round_new(avg(mad),2) mad, round_new(avg(qbad),2) qbad, round_new(avg(had),2) had, round_new(avg(vad),2) vad,\n" +
                    "round_new(avg(std),2) std, round_new(avg(qgrad),2) qgrad, round_new(avg(hdaf),2) hdaf, round_new(avg(sdaf),2) sdaf,\n" +
                    "round_new(avg(var),2) var, round_new(avg(fcad),2) fcad, round_new(max(huayy),2) huayy, max(beiz) beiz,\n" +
                    "max(lursj) lursj, max(lury) lury, round_new(avg(har),2) har, round_new(avg(qgrd),2) qgrd, round_new(avg(qgrad_daf),2) qgrad_daf\n" +
                    "from rulmzlzb where rulrq =" + rulriq + " and diancxxb_id=" + diancxxb_id + " group by diancxxb_id,rulrq";
            rsl = con.getResultSetList(sql);
            while (rsl.next()) {
                long diancxxbid = Long.parseLong(diancxxb_id);
                sql = "insert into rulmzlb (id,diancxxb_id,fenxrq,rulrq,rulbzb_id,jizfzb_id,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,std,qgrad,\n" +
                        "hdaf,sdaf,var,fcad,har,qgrd,qgrad_daf,huayy,lury,lursj,shenhzt,beiz) values (" + MainGlobal.getNewID(diancxxbid) +
                        "," + rsl.getLong("diancxxb_id") + "," + DateUtil.FormatOracleDate(rsl.getDateString("fenxrq")) + "," + DateUtil.FormatOracleDate(rsl.getDateString("rulrq")) +
                        "," + rsl.getLong("rulbzb_id") + "," + rsl.getLong("jizfzb_id") + "," + rsl.getDouble("qnet_ar") +
                        "," + rsl.getDouble("aar") +
                        "," + rsl.getDouble("ad") +
                        "," + rsl.getDouble("vdaf") +
                        "," + rsl.getDouble("mt") +
                        "," + rsl.getDouble("stad") +
                        "," + rsl.getDouble("aad") +
                        "," + rsl.getDouble("mad") +
                        "," + rsl.getDouble("qbad") +
                        "," + rsl.getDouble("had") +
                        "," + rsl.getDouble("vad") +
                        "," + rsl.getDouble("std") +
                        "," + rsl.getDouble("qgrad") +
                        "," + rsl.getDouble("hdaf") +
                        "," + rsl.getDouble("sdaf") +
                        "," + rsl.getDouble("var") +
                        "," + rsl.getDouble("fcad") +
                        "," + rsl.getDouble("har") +
                        "," + rsl.getDouble("qgrd") +
                        "," + rsl.getDouble("qgrad_daf") +
                        ",'" + rsl.getString("huayy") + "'" +
                        ",'" + rsl.getString("lury") + "'" +
                        "," + DateUtil.FormatOracleDateTime(rsl.getDateTimeString("lursj")) +
                        ",1" +
                        ",'" + rsl.getDouble("beiz") + "')";
                con.getUpdate(sql);
            }
            rsl.close();
        }

    }

    public static boolean getYuansEditable(JDBCcon con, String Yuans, long diancxxb_id, boolean defedit) {
        boolean editable = defedit;
        String sql = "select zhi from xitxxb where mingc = '入厂化验" + Yuans + "可编辑' and zhuangt = 1 and diancxxb_id ="
                + diancxxb_id + " and leib ='化验' ";
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            if ("是".equals(rsl.getString("zhi"))) {
                editable = true;
            } else {
                editable = false;
            }
        }
        rsl.close();
        return editable;
    }

    public static boolean getYuansisShow(JDBCcon con, String Yuans, long diancxxb_id, boolean defShow) {
        boolean isShow = defShow;
        String sql = "select zhi from xitxxb where mingc = '是否显示入炉化验" + Yuans + "' and zhuangt = 1 and diancxxb_id = "
                + diancxxb_id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            if ("是".equals(rsl.getString("zhi"))) {
                isShow = true;
            } else {
                isShow = false;
            }
        }
        rsl.close();
        return isShow;
    }

    public static String getYuansSign(JDBCcon con, String Yuans, long diancxxb_id, String defaultSign) {
        String sign = defaultSign;
        String sql = "select zhi from xitxxb where mingc = '入厂化验" + Yuans + "'\n"
                + " and zhuangt = 1 and diancxxb_id = " + diancxxb_id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            sign = rsl.getString("zhi");
        }
        rsl.close();
        return sign;
    }

    public static String getYuansSign(JDBCcon con, String hytype, String Yuans, long diancxxb_id, String defaultSign) {
        String sign = defaultSign;
        String sql = "select zhi from xitxxb where mingc = '" + hytype + "化验" + Yuans + "'\n"
                + " and zhuangt = 1 and diancxxb_id = " + diancxxb_id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            sign = rsl.getString("zhi");
        }
        rsl.close();
        return sign;
    }

    public static double getYuansValRL(JDBCcon con, String Sign, String oraRiq, String diancxxb_id) {
        double Val = 0D;
        String sql =
                "select nvl(decode(sum(f.jingz),0,0,round(sum(f.jingz * z." + Sign + ") / sum(f.jingz), 4)),0) val\n" +
                        "from fahb f, zhilb z\n" +
                        "where f.zhilb_id = z.id and f.diancxxb_id = " + diancxxb_id + "\n" +
                        "and f.daohrq >= add_months(" + oraRiq + ",-1)\n" +
                        "and f.daohrq <" + oraRiq;
        ResultSetList rs = con.getResultSetList(sql);
        if (rs.next()) {
            Val = rs.getDouble("val");
        }
        rs.close();
        return Val;
    }

    /*
     * 修改人：梁丽丽
     * 修改时间：2010-07-21
     * 修改内容：修改下边sql在元素分析有多个品种录入元素分析值时候加入品种关联并用参数控制
     */
    public static double getYuansValue(JDBCcon con, long zhillsb_id, long diancxxb_id, String Sign, double Vad) {
        boolean MorePz = false;//多个品种录入元素分析值
        String sl = "";
        String pz = "";
        sl = "select zhi from xitxxb where mingc = '多个品种录入元素分析值' and zhuangt = 1 and leib='化验'and diancxxb_id = "
                + diancxxb_id + "";
        ResultSetList rl = con.getResultSetList(sl);

        while (rl.next()) {
            if (rl.getString("zhi").equals("是")) {
                MorePz = true;
            } else {
                MorePz = false;
            }
        }
        if (MorePz) {
            pz = " and y.meizb_id=f.pinzb_id";
        } else {
            pz = "";
        }
        double value = -1;
        String sql = "select y.zhi from yuansfxb y, yuansxmb m, fahb f, zhillsb l\n" +
                "where y.meikxxb_id = f.meikxxb_id and y.yunsfsb_id = f.yunsfsb_id\n" +
                "and y.yuansxmb_id = m.id and m.mingc = '" + Sign + "' and m.zhuangt = 1\n" +
                "and y.diancxxb_id = f.diancxxb_id\n" + " " + pz + " " +
                "and f.zhilb_id = l.zhilb_id and l.id =" + zhillsb_id + " order by y.riq desc";

/**
 * huochaoyuan 2009-03-01修改上边sql，原因是元素分析未按起用时间正常使用，做简单调整，添加order by y.riq desc
 * 目前结果正常，但发现上边语句也未按品种判断元素分析值；
 */
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            if (isNumeric(rsl.getString("zhi"))) {
                value = Double.parseDouble(rsl.getString("zhi"));
            } else {
                Interpreter bsh = new Interpreter();
                try {
                    bsh.set("氢值", 0);
                    bsh.set("Vad", Vad);
                    bsh.eval(rsl.getString("zhi"));
                    value = CustomMaths.Round_new(Double.parseDouble(bsh.get(
                            "氢值").toString()), 2);
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (EvalError e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        rsl.close();
        return value;
    }

    /**
     * @param con
     * @param zhillsb_id
     * @param diancxxb_id
     * @param Sign
     * @param Vad
     * @param isFencb        是否分厂别
     * @param fc_diancxxb_id 传入厂别id，相同矿不同厂可能氢值不同，根据该厂别的id取出该厂别下某一矿的氢值
     * @return
     * @author ww
     */
    public static double getYuansValue(JDBCcon con, long zhillsb_id, long diancxxb_id,
                                       String Sign, double Vad, boolean isFencb, String fc_diancxxb_id) {

        double value = -1;
        String sql = "";
        if (isFencb) {
            sql = "select y.zhi from yuansfxb y, yuansxmb m, fahb f, zhillsb l\n" +
                    "where y.meikxxb_id = f.meikxxb_id and y.yunsfsb_id = f.yunsfsb_id\n" +
                    "and y.yuansxmb_id = m.id and m.mingc = '" + Sign + "' and m.zhuangt = 0\n" +
                    "and f.diancxxb_id IN (SELECT ID FROM diancxxb WHERE fuid=" + diancxxb_id + ")\n" +
                    "and y.diancxxb_id =" + fc_diancxxb_id + "\n" +
                    "and f.zhilb_id = l.zhilb_id and l.id =" + zhillsb_id + " order by y.riq desc";

        } else {
            sql = "select y.zhi from yuansfxb y, yuansxmb m, fahb f, zhillsb l\n" +
                    "where y.meikxxb_id = f.meikxxb_id and y.yunsfsb_id = f.yunsfsb_id\n" +
                    "and y.yuansxmb_id = m.id and m.mingc = '" + Sign + "' and m.zhuangt = 0\n" +
                    "and y.diancxxb_id = f.diancxxb_id and f.diancxxb_id =" + diancxxb_id + "\n" +
                    "and f.zhilb_id = l.zhilb_id and l.id =" + zhillsb_id + " order by y.riq desc";
        }
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            if (isNumeric(rsl.getString("zhi"))) {
                value = Double.parseDouble(rsl.getString("zhi"));
            } else {
                Interpreter bsh = new Interpreter();
                try {
                    bsh.set("氢值", 0);
                    bsh.set("Vad", Vad);
                    bsh.eval(rsl.getString("zhi"));
                    value = CustomMaths.Round_new(Double.parseDouble(bsh.get(
                            "氢值").toString()), 2);
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (EvalError e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        rsl.close();
        return value;
    }

    public static int getQnet_arScale(JDBCcon con, long diancxxb_id) {
        int Qnet_scale = 3;        //低位发热量小数位
        String sql = "select zhi from xitxxb where mingc ='低位发热量小数位' and leib='化验' and zhuangt=1 and diancxxb_id=" +
                +diancxxb_id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            Qnet_scale = rsl.getInt("zhi");
        }
        rsl.close();
        return Qnet_scale;
    }


    public static String ComputeValue(double mt, double mad, double aad,
                                      double vad, double qbad, String Hsign, double h, String Ssign,
                                      double s, int Qnet_scale) {
        String sql = "";
        double dblHad = 0;
        double dblVad = vad;
        double dblStad = 0;
        double dblAad = aad;
        double dblMt = mt;
        double dblMad = mad;
        double dblA = 0;
        double dblAd = 0;
        double dblAar = 0;
        double dblQgrad = 0;
        double dblQbad = qbad;
        double dblQnetar = 0;
        double dblFcad = 0;
        double dblVdaf = 0;
        double dblStd = 0;
        double dblSdaf = 0;
        double dblHdaf = 0;
        double dblQgrdaf = 0;
        double dblHar = 0;
        double dblQgrd = 0;
        try {
            if (Ssign.equalsIgnoreCase("Stad")) {
                dblStad = s;
                if (dblMad == 100) {
                    dblStd = 0;
                } else {
                    dblStd = CustomMaths.Round_new(dblStad * 100
                            / (100 - dblMad), 2);
                }
                dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
                        2);
            } else if (Ssign.equalsIgnoreCase("Std")) {
                dblStd = s;
                dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
                        2);
                dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
                        2);
            } else if (Ssign.equalsIgnoreCase("Sdaf")) {
                dblSdaf = s;
                if (dblMad == 100) {
                    dblStd = 0;
                } else {
                    dblStd = CustomMaths.Round_new((100 - dblAd) / 100
                            * dblSdaf, 2);
                }
                dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
                        2);
            }
            if (Hsign.equalsIgnoreCase("Had")) {
                dblHad = h;
                if ((100 - dblMad - dblAad) == 0) {
                    dblHdaf = 0;
                } else {
                    dblHdaf = CustomMaths.Round_new(dblHad * 100
                            / (100 - dblMad - dblAad), 2);
                }
            } else if (Hsign.equalsIgnoreCase("Hdaf")) {
                dblHdaf = h;
                dblHad = CustomMaths.Round_new(((100 - dblMad - dblAad)
                        * dblHdaf / 100), 2);
            }
            if (dblMad == 100) {
                dblAd = 0;
            } else {
                dblAd = CustomMaths.Round_new(dblAad * 100 / (100 - dblMad), 2);
            }
            if (dblQbad < 16.72) {
                dblA = 0.001;
            } else {
                if (dblQbad >= 16.72 && dblQbad <= 25.1) {
                    dblA = 0.0012;
                } else {
                    dblA = 0.0016;
                }
            }
            dblQgrad = CustomMaths.Round_new((dblQbad - (0.0941 * dblStad + dblA * dblQbad)), 2);
            if (dblMad == 100) {
                dblQnetar = -23;
                dblAd = 0;
                dblAar = 0;
            } else {
                dblQnetar = CustomMaths.Round_new(((dblQgrad - 0.206 * dblHad) * (100 - dblMt) / (100 - dblMad) - 0.023 * dblMt), Qnet_scale);
                dblAd = CustomMaths.Round_new((dblAad * 100 / (100 - dblMad)), 2);
                dblAar = CustomMaths.Round_new((dblAad * (100 - dblMt) / (100 - dblMad)), 2);
                dblFcad = CustomMaths.Round_new((100 - dblAad - dblVad - dblMad), 2);
            }
            if ((100 - dblMad - dblAad) == 0) {
                dblVdaf = 0;
            } else {
                dblVdaf = CustomMaths.Round_new((dblVad * 100 / (100 - dblMad - dblAad)), 2);
            }
            dblQgrdaf = CustomMaths.Round_new((100 / (100 - dblMad - dblAad) * dblQgrad), 3);
            dblQgrd = CustomMaths.Round_new(dblQgrad * 100 / (100 - dblMad), 3);
            dblHar = CustomMaths.Round_new(dblHad * (100 - dblMt) / (100 - dblMad), 2);
            sql = "qnet_ar = " + dblQnetar + ",\n"
                    + "aar = " + dblAar
                    + ",\n" + "          ad = " + dblAd + ",\n"
                    + "                  vdaf = " + dblVdaf + ",\n"
                    + "                  mt = " + dblMt + ",\n"
                    + "                  stad = " + dblStad + ",\n"
                    + "                  aad = " + dblAad + ",\n"
                    + "                  mad = " + dblMad + ",\n"
                    + "                  qbad = " + dblQbad + ",\n"
                    + "                  had = " + dblHad + ",\n"
                    + "                  vad = " + dblVad + ",\n"
                    + "                  fcad = " + dblFcad + ",\n"
                    + "                  std = " + dblStd + ",\n"
                    + "                  qgrad = " + dblQgrad + ",\n"
                    + "                  hdaf = " + dblHdaf + ",\n"
                    + "                  qgrad_daf = " + dblQgrdaf + ",\n"
                    + "                  sdaf = " + dblSdaf + ",\n"
                    + "                  har = " + dblHar + ",\n"
                    + "                  qgrd = " + dblQgrd + ",\n";
            return sql;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return sql;
    }


    public static void main(String[] args) {
        // TODO 自动生成方法存根
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (str.charAt(i) == '.') {
                } else {
                    return false;
                }
            }
        }
        if (str.substring(str.length() - 1, str.length()).equals(".")) {
            str += "0";
        }
        return true;
    }

    public static void ComputeValue(JDBCcon con, long zhillsb_id,
                                    long diancxxb_id, double[] zhi) {
        Interpreter bsh = new Interpreter();
        StringBuffer SQL = new StringBuffer("");
        double dblHad = 0;
        double dblVad = 0;
        double dblStad = 0;
        double dblAad = 0;
        double dblMt = 0;
        double dblMad = 0;
        double dblA = 0;
        double dblAd = 0;
        double dblAar = 0;
        double dblQgrad = 0;
        double dblQbad = 0;
        double dblQnetar = 0;
        double dblFcad = 0;
        double dblVdaf = 0;
        double dblStd = 0;
        double dblSdaf = 0;
        double dblHdaf = 0;
        double dblQgrdaf = 0;
        double dblHar = 0;
        double dblQgrd = 0;
        ResultSetList rs = null;
        try {
            dblMt = zhi[0];
            dblMad = zhi[1];
            dblAad = zhi[2];
            dblVad = zhi[3];
            dblQbad = zhi[5];
            String str = "Std";
            String sql = "";
            sql = "select zhi\n" + "  from xitxxb\n"
                    + " where mingc = '入厂化验硫'\n" + "   and zhuangt = 1\n"
                    + "   and diancxxb_id = " + diancxxb_id;
            ResultSetList rsl = con.getResultSetList(sql);
            while (rsl.next()) {
                str = rsl.getString("zhi");
            }
            if (zhi[4] == 0) {
                SQL = new StringBuffer("");
                SQL.append("select y.zhi\n"
                        + "  from yuansfxb y, yuansxmb sm\n"
                        + " where (meikxxb_id, yunsfsb_id) in\n"
                        + "       (select meikxxb_id, yunsfsb_id\n"
                        + "          from fahb\n"
                        + "         where zhilb_id in\n"
                        + "               (select zhilb_id\n"
                        + "                  from caiyb\n"
                        + "                 where id = (select caiyb_id\n"
                        + "                               from yangpdhb\n"
                        + "                              where zhilblsb_id = "
                        + zhillsb_id + "))\n" + "           and diancxxb_id = "
                        + diancxxb_id + ")\n"
                        + "   and y.yuansxmb_id = sm.id\n"
                        + "   and sm.mingc = '" + str + "'\n"
                        + "   and y.zhuangt = 1");
                rs = con.getResultSetList(SQL.toString());
                while (rs.next()) {
                    zhi[4] = rs.getDouble("zhi");
                }
                rs.close();
            }
            if (str.equals("Stad")) {
                dblStad = zhi[4];
                if (dblMad == 100) {
                    dblStd = 0;
                } else {
                    dblStd = CustomMaths.Round_new(dblStad * 100
                            / (100 - dblMad), 2);
                }
                dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
                        2);
            } else if (str.equals("Std")) {
                dblStd = zhi[4];
                dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
                        2);
                dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
                        2);
            } else if (str.equals("Sdaf")) {
                dblSdaf = zhi[4];
                if (dblMad == 100) {
                    dblStd = 0;
                } else {
                    dblStd = CustomMaths.Round_new((100 - dblAd) / 100
                            * dblSdaf, 2);
                }
                dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
                        2);
            }

            String H = "Hdaf";
            rs = con
                    .getResultSetList("select zhi from xitxxb where mingc = '入厂化验氢' and zhuangt = 1");
            while (rs.next()) {
                H = rs.getString("zhi");
            }
            SQL = new StringBuffer("");
            SQL.append("select y.zhi\n" + "  from yuansfxb y, yuansxmb sm\n"
                    + " where (meikxxb_id, yunsfsb_id) in\n"
                    + "       (select meikxxb_id, yunsfsb_id\n"
                    + "          from fahb\n" + "         where zhilb_id in\n"
                    + "               (select zhilb_id\n"
                    + "                  from caiyb\n"
                    + "                 where id = (select caiyb_id\n"
                    + "                               from yangpdhb\n"
                    + "                              where zhilblsb_id = "
                    + zhillsb_id + "))\n" + "           and diancxxb_id = "
                    + diancxxb_id + ")\n" + "   and y.yuansxmb_id = sm.id\n"
                    + "   and sm.mingc = '" + H + "'\n"
                    + "   and y.zhuangt = 1");
            rs = con.getResultSetList(SQL.toString());
            double dblH = 0;
            while (rs.next()) {
                String strch = rs.getString("zhi");
                if (isNumeric(strch)) {
                    dblH = Double.parseDouble(strch);
                } else {
                    bsh.set("氢值", 0);
                    bsh.set("Vad", dblVad);
                    bsh.eval(strch);
                    dblH = CustomMaths.Round_new(Double.parseDouble(bsh.get(
                            "氢值").toString()), 2);
                    // dblH = rs.getDouble("zhi");
                }
                if (H.equals("Had")) {

                    dblHad = dblH;
                    if ((100 - dblMad - dblAad) == 0) {
                        dblHdaf = 0;
                    } else {
                        dblHdaf = CustomMaths.Round_new(dblHad * 100
                                / (100 - dblMad - dblAad), 2);
                    }
                } else {
                    dblHdaf = dblH;
                    dblHad = CustomMaths.Round_new(((100 - dblMad - dblAad)
                            * dblHdaf / 100), 2);
                }
            }

            if (dblMad == 100) {
                dblAd = 0;
            } else {
                dblAd = CustomMaths.Round_new(dblAad * 100 / (100 - dblMad), 2);
            }
            if (dblQbad < 16.72) {
                dblA = 0.001;
            } else {
                if (dblQbad >= 16.72 && dblQbad <= 25.1) {
                    dblA = 0.0012;
                } else {
                    dblA = 0.0016;
                }
            }
            dblQgrad = CustomMaths.Round_new(
                    (dblQbad - (0.0941 * dblStad + dblA * dblQbad)), 3);
            if (dblMad == 100) {
                dblQnetar = -23;
                dblAd = 0;
                dblAar = 0;
            } else {
                dblQnetar = CustomMaths.Round_new(((dblQgrad - 0.206 * dblHad)
                        * (100 - dblMt) / (100 - dblMad) - 0.023 * dblMt), 3);
                dblAd = CustomMaths.Round_new((dblAad * 100 / (100 - dblMad)),
                        2);
                dblAar = CustomMaths.Round_new(
                        (dblAad * (100 - dblMt) / (100 - dblMad)), 2);
                dblFcad = CustomMaths.Round_new(
                        (100 - dblAad - dblVad - dblMad), 2);
            }
            if ((100 - dblMad - dblAad) == 0) {
                dblVdaf = 0;
            } else {
                dblVdaf = CustomMaths.Round_new(
                        (dblVad * 100 / (100 - dblMad - dblAad)), 2);
            }
            dblQgrdaf = CustomMaths.Round_new(
                    (100 / (100 - dblMad - dblAad) * dblQgrad), 3);
            dblQgrd = CustomMaths.Round_new(dblQgrad * 100 / (100 - dblMad), 3);
            dblHar = CustomMaths.Round_new(dblHad * (100 - dblMt)
                    / (100 - dblMad), 2);
            SQL = new StringBuffer("");
            SQL.append("update zhillsb\n" + "              set qnet_ar = "
                    + dblQnetar + ",\n" + "                  aar = " + dblAar
                    + ",\n" + "                  ad = " + dblAd + ",\n"
                    + "                  vdaf = " + dblVdaf + ",\n"
                    + "                  mt = " + dblMt + ",\n"
                    + "                  stad = " + dblStad + ",\n"
                    + "                  aad = " + dblAad + ",\n"
                    + "                  mad = " + dblMad + ",\n"
                    + "                  qbad = " + dblQbad + ",\n"
                    + "                  had = " + dblHad + ",\n"
                    + "                  vad = " + dblVad + ",\n"
                    + "                  fcad = " + dblFcad + ",\n"
                    + "                  std = " + dblStd + ",\n"
                    + "                  qgrad = " + dblQgrad + ",\n"
                    + "                  hdaf = " + dblHdaf + ",\n"
                    + "                  qgrad_daf = " + dblQgrdaf + ",\n"
                    + "                  sdaf = " + dblSdaf + ",\n"
                    + "                  har = " + dblHar + ",\n"
                    + "                  qgrd = " + dblQgrd + ",\n"
                    + "                  shenhzt = 3,\n"
                    + "                  banz = ''\n"
                    + "            where id = " + zhillsb_id);
            con.getUpdate(SQL.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void ComputeRULUValue(JDBCcon con, long rulmzlbid,
                                        long diancxxb_id, double[] zhi, int weis) {
        StringBuffer SQL = new StringBuffer("");
        double dblHad = 0;
        double dblVad = 0;
        double dblStad = 0;
        double dblAad = 0;
        double dblMt = 0;
        double dblMad = 0;
        double dblA = 0;
        double dblAd = 0;
        double dblAar = 0;
        double dblQgrad = 0;
        double dblQbad = 0;
        double dblQnetar = 0;
        double dblFcad = 0;
        double dblVdaf = 0;
        double dblStd = 0;
        double dblSdaf = 0;
        double dblHdaf = 0;
        double dblQgrdaf = 0;
        double dblHar = 0;
        double dblQgrd = 0;
        double dblVar = 0;
        try {
            dblMt = zhi[0];
            dblMad = zhi[1];
            dblAad = zhi[2];
            dblVad = zhi[3];
            dblQbad = zhi[5];
            String SQLCtr = "";
            String SQLStr = "select * from xitxxb where leib = '入炉' and mingc = '入炉氢取值'";
            ResultSetList rsl = con.getResultSetList(SQLStr);
            if (rsl.next()) {
                SQLCtr = "select zhi\n" + "  from rulysfxb\n"
                        + " where diancxxb_id = " + diancxxb_id + "\n"
                        + "   and zhuangt = 1\n"
                        + "   and rulysfxxm_id = (select id\n"
                        + "                         from xitxxb\n"
                        + "                        where leib = '入炉'\n"
                        + "                          and mingc like '入炉化验氢'\n"
                        + "                          and zhuangt = 1\n"
                        + "                          and diancxxb_id = "
                        + diancxxb_id + " ) order by riq ";
                ResultSetList rsel = con.getResultSetList(SQLCtr);
                while (rsel.next()) {
                    zhi[6] = rsel.getDouble("zhi");
                }
                rsel.close();
            }
            rsl.close();
            SQLStr = "select * from xitxxb where leib = '入炉' and mingc = '入炉硫取值'";
            rsl = con.getResultSetList(SQLStr);
            if (rsl.next()) {
                SQLCtr = "select zhi\n" + "  from rulysfxb\n"
                        + " where diancxxb_id = " + diancxxb_id + "\n"
                        + "   and zhuangt = 1\n"
                        + "   and rulysfxxm_id = (select id\n"
                        + "                         from xitxxb\n"
                        + "                        where leib = '入炉'\n"
                        + "                          and mingc like '入炉化验硫'\n"
                        + "                          and zhuangt = 1\n"
                        + "                          and diancxxb_id = "
                        + diancxxb_id + " ) order by riq ";
                ResultSetList rsel = con.getResultSetList(SQLCtr);
                while (rsel.next()) {
                    zhi[4] = rsel.getDouble("zhi");
                }
                rsel.close();
            }
            rsl.close();
            String sql = "select zhi\n" + "  from xitxxb\n"
                    + " where mingc = '入炉化验硫'\n" + "   and zhuangt = 1\n"
                    + "   and diancxxb_id = " + diancxxb_id;
            ResultSetList rs = con.getResultSetList(sql);
            String S = "";
            while (rs.next()) {
                S = rs.getString("zhi");
            }
            sql = "select zhi\n" + "  from xitxxb\n"
                    + " where mingc = '入炉化验氢'\n" + "   and zhuangt = 1\n"
                    + "   and diancxxb_id = " + diancxxb_id;
            rs = con.getResultSetList(sql);
            String H = "";
            while (rs.next()) {
                H = rs.getString("zhi");
            }

            //阳城发电入炉氢(Had)值自动计算
            sql = "select zhi\n" + "  from xitxxb\n"
                    + " where mingc = '入炉化验氢值自动计算'\n" + " and leib='入炉'  and zhuangt = 1\n"
                    + "   and diancxxb_id = " + diancxxb_id;
            rs = con.getResultSetList(sql);
            boolean Is_YangcPowe = false;
            while (rs.next()) {
                if (rs.getString("zhi").equals("是")) {
                    Is_YangcPowe = true;
                    ;
                } else {
                    Is_YangcPowe = false;
                    ;
                }
            }


            if (S.equals("Std")) {
                dblStd = zhi[4];
                dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
                        2);
                dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
                        2);
            } else if (S.equals("Stad")) {
                dblStad = zhi[4];
                if (dblMad == 100) {
                    dblStd = 0;
                } else {
                    dblStd = CustomMaths.Round_new(dblStad * 100
                            / (100 - dblMad), 2);
                }
                dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
                        2);
            } else if (S.equals("Sdaf")) {
                dblSdaf = zhi[4];
                if (dblMad == 100) {
                    dblStd = 0;
                } else {
                    dblStd = CustomMaths.Round_new((100 - dblAd) / 100
                            * dblSdaf, 2);
                }
                dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
                        2);
            }
            if (H.equals("Had")) {
                if (Is_YangcPowe) {//阳城发电氢值自动计算

                    if ((100 - dblMad - dblAad) == 0) {
                        dblVdaf = 0;
                        dblHdaf = 0;
                    } else {
                        dblVdaf = CustomMaths.Round_new(
                                (dblVad * 100 / (100 - dblMad - dblAad)), 2);
                    }
                    dblHad = CustomMaths.Round_new((100 - dblMad - dblAad) / 100 * dblVdaf / (0.1462 * dblVdaf + 1.1124), 2);
                    dblHdaf = CustomMaths.Round_new(dblHad * 100
                            / (100 - dblMad - dblAad), 2);

                } else {
                    dblHad = zhi[6];
                    if ((100 - dblMad - dblAad) == 0) {
                        dblHdaf = 0;
                    } else {
                        dblHdaf = CustomMaths.Round_new(dblHad * 100
                                / (100 - dblMad - dblAad), 2);
                    }
                }

            } else if (H.equals("Hdaf")) {
                dblHdaf = zhi[6];
                dblHad = CustomMaths.Round_new(((100 - dblMad - dblAad)
                        * dblHdaf / 100), 2);
            }
            if (dblMad == 100) {
                dblAd = 0;
            } else {
                dblAd = CustomMaths.Round_new(dblAad * 100 / (100 - dblMad), 2);
            }
            if (dblQbad < 16.72) {
                dblA = 0.001;
            } else {
                if (dblQbad >= 16.72 && dblQbad <= 25.1) {
                    dblA = 0.0012;
                } else {
                    dblA = 0.0016;
                }
            }
            dblQgrad = CustomMaths.Round_new(
                    (dblQbad - (0.0941 * dblStad + dblA * dblQbad)), 3);
            if (dblMad == 100) {
                dblQnetar = -23;
                dblAd = 0;
                dblAar = 0;
            } else {
                dblQnetar = CustomMaths.Round_new(((dblQgrad - 0.206 * dblHad)
                        * (100 - dblMt) / (100 - dblMad) - 0.023 * dblMt), weis);
                dblAd = CustomMaths.Round_new((dblAad * 100 / (100 - dblMad)),
                        2);
                dblAar = CustomMaths.Round_new(
                        (dblAad * (100 - dblMt) / (100 - dblMad)), 2);
                dblFcad = CustomMaths.Round_new(
                        (100 - dblAad - dblVad - dblMad), 2);
            }
            if ((100 - dblMad - dblAad) == 0) {
                dblVdaf = 0;
            } else {
                dblVdaf = CustomMaths.Round_new(
                        (dblVad * 100 / (100 - dblMad - dblAad)), 2);
            }
            dblQgrdaf = CustomMaths.Round_new(
                    (100 / (100 - dblMad - dblAad) * dblQgrad), 3);
            dblQgrd = CustomMaths.Round_new(dblQgrad * 100 / (100 - dblMad), 3);
            dblHar = CustomMaths.Round_new(dblHad * (100 - dblMt)
                    / (100 - dblMad), 2);
            dblVar = CustomMaths.Round_new(dblVad * (100 - dblMt) / (100 - dblMad), 2);
            SQL = new StringBuffer("");
            SQL.append("update rulmzlb\n" + "              set qnet_ar = "
                    + dblQnetar + ",\n" + "                  aar = " + dblAar
                    + ",\n" + "                  ad = " + dblAd + ",\n"
                    + "                  vdaf = " + dblVdaf + ",\n"
                    + "                  mt = " + dblMt + ",\n"
                    + "                  stad = " + dblStad + ",\n"
                    + "                  aad = " + dblAad + ",\n"
                    + "                  mad = " + dblMad + ",\n"
                    + "                  qbad = " + dblQbad + ",\n"
                    + "                  had = " + dblHad + ",\n"
                    + "                  vad = " + dblVad + ",\n"
                    + "                  fcad = " + dblFcad + ",\n"
                    + "                  std = " + dblStd + ",\n"
                    + "                  har = " + dblHar + ",\n"
                    + "                  qgrd = " + dblQgrd + ",\n"
                    + "                  qgrad = " + dblQgrad + ",\n"
                    + "                  hdaf = " + dblHdaf + ",\n"
                    + "                  qgrad_daf = " + dblQgrdaf + ",\n"
                    + "                  sdaf = " + dblSdaf + ",\n"
                    + "                  var = " + dblVar + ",\n"
                    + "                  shenhzt = 1\n" + " where id = "
                    + rulmzlbid);
            con.getUpdate(SQL.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}