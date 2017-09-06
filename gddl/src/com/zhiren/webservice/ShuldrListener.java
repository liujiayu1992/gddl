package com.zhiren.webservice;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;

import com.zhiren.common.ResultSetList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.ResultSet;
import java.util.*;
/*
 * 时间：2016-8-25
 * 作者：刘志宇
 * 描述：每小时执行此任务一次，直到每日上午10点，任务自动结束
 * */


public class ShuldrListener implements ServletContextListener {
    private static JDBCcon con = null;
    private static final Timer timer = new Timer();

    public void contextDestroyed(ServletContextEvent arg0) {
        timer.cancel();
        System.out.println("析构");
    }

    public void contextInitialized(ServletContextEvent arg0) {
//		long period = 1000 * 60 * 60 *24;
//		long period = 1000 * 60 * 60 *1;
        try{
            con = new JDBCcon();
            String sql="select zhi from xitxxb where diancxxb_id='938' and mingc='数量导入时间间隔' and zhuangt=1";
            ResultSetList rs = con.getResultSetList(sql);
            long zhi=0;
            if(rs.next()){
                zhi=rs.getLong("zhi");
            }else{
                zhi=5;
            }
            long period = 1000 * 60 * zhi;
            con.Close();
//		临时设置为60秒执行一次
//		period=5000;
            System.out.println("--------------------------------数据导入程序启动!---------------------------------------------");
            timer.schedule(new AutoInvTask(), 0, period);//86400000
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class AutoInvTask extends TimerTask {
        public void run() {
            con = new JDBCcon();
            int intResult = daorChepbtmp();//质量表
            con.commit();
            if (intResult > 0) {
                System.out.println("-------------------------数据导入程序,导入更新!" + intResult + "车数据!---------------");
            }
            con.Close();
        }
    }

    //-------------------------------------------------导入数量----------------------------------------------------------
    int daorChepbtmp() {  //
        int intRuesltRow = 0;
        String sql =
                " select\n" +
                        "  XL_CHEPBTMP_ID.nextval id ,\n" +
                        "  id as v_TRUCKENTER_ID,\n" +
                        "  samcode V_TRUCKENTER_SAMCODE,\n" +
                        "  -------------------------------------------------------------\n" +
                        "  938 as diancxxb_id,--电厂信息表Id\n" +
                        "  ywid piaojh ,--运单票据号\n" +
                        "  collieryname meikdwmc ,--煤款单位名称\n" +
                        "  decode(goodsname,'燃煤','原煤',goodsname) pinz ,--燃料品种表\n" +
                        "  nvl(dispatchstation,'汽') faz ,--发站\n" +
                        "  nvl(arrivestation,'汽') daoz ,--到站\n" +
                        "  trunc(decode(eweightime,null,sysdate,to_date(eweightime,'yyyy-mm-dd hh24:mi:ss'))) fahrq ,--发货日期\n" +
                        "  transtype yunsfs ,--运输方式(公路、铁路、船运)\n" +
                        "  samcode chec ,--车次\n" +
                        "  samcode caiybh ,--采样编号\n" +
                        "   samcode beiz,\n"+
                        "   -1 fahbtmp_id,\n"+
                        "  numberplate cheph ,--车皮号\n" +
                        "  fweight maoz ,--毛重\n" +
                        "  eweight piz ,--皮重\n" +
                        "  dedcutton koud ,--扣吨\n" +
                        "  '过衡' jianjfs ,--检斤方式(过衡、检尺)\n" +
                        "  3 chebb_id ,--车别（路车,自备,汽,船）\n" +
                        "  nvl(arrivestation,'汽') yuandz ,--原到站\n" +
                        "  transunitname yunsdwb_id ,--运输单位\n" +
                        "  decode(eweightime,null,sysdate,to_date(eweightime,'yyyy-mm-dd hh24:mi:ss') ) qingcsj ,--轻车时间(汽车衡检皮)\n" +
                        "  trunc(decode(eweightime,null,sysdate,to_date(eweightime,'yyyy-mm-dd hh24:mi:ss') )) daohrq ,--到货日期\n" +
                        "\n" +
                        "  --------------------------------------------------------------\n" +
                        "  hashno qingchh ,--轻车衡号\n" +
                        "  hashuser qingcjjy ,--轻车检斤员\n" +
                        "  to_date(fweightime,'yyyy-mm-dd hh24:mi:ss') zhongcsj,--重车时间(汽车衡检毛)\n" +
                        "  weighno zhongchh ,--重车衡号\n" +
                        "  weightuser zhongcjjy ,--重车检斤员\n" +
                        "  nvl(to_date(tosamtime,'yyyy-mm-dd hh24:mi:ss'),sysdate) caiyrq ,--采样日期\n" +
                        "  '机械' xiecfs ,--卸车方式\n" +
                        "  fweight yuanmz,\n" +
                        "  eweight yuanpz,\n" +
                        "  handlertype caozlx,\n" +
                        "  --------------------------------------------------------------\n" +
                        "  sysdate lursj,--录入时间\n" +
                        "  '中卫发电' YUANSHDW,  --coalfieldname meic ,\n" +
                        "  collieryname yuanmkdw,  --原煤矿单位 ,\n" +
                        "  -- transunitcode chedhm ,\n" +
                        "  -- netweight jingz ,\n" +
                        "  decode(nvl(COALNETWEIGHT,0),0,netweight,COALNETWEIGHT) biaoz ,--票重\n" +
                        "  -- fdeductuser meiczjy ,\n" +
                        "  --sdeductuser koudzjy ,\n" +
                        "  -- receivingunit xianshdw ,\n" +
                        "  -- uploadtime gengxsj,\n" +
                        "  -- COLLIERYCODE meikbm,\n" +
                        "  -- DISPATCHSTATIONCODE fazbm,\n" +
                        "  -- ARRIVESTATIONCODE daozbm,\n" +
                        "  (select nvl(max(g.mingc), collieryname)\n" +
                        "   from gongysb g, gongysmkglb gx,meikxxb mk\n" +
                        "   where g.id = gx.gongysb_id\n" +
                        "         and gx.meikxxb_id = mk.id\n" +
                        "         and mk.mingc = jk_truckenter.collieryname\n" +
                        "         and g.leix = 1 ) gongysmc,--供应商名称\n" +
                        "  (SELECT nvl(MAX(MINGC), '市场采购')\n" +
                        "   FROM JIHKJB\n" +
                        "   WHERE ID = (SELECT MAX(JIHKJB_ID) AS JIHKJB_ID\n" +
                        "               FROM MEIKXXB\n" +
                        "               WHERE mingc = collieryname)) jihkj,--计划口径\n" +
                        "   TRANSUNITNAME yunsdw,--运输单位\n" +
                        "  '系统导入' lury\n" +
                        "from jk_truckenter\n" +
                        "where  DATASTATUS = 0\n" +
                        "       and nvl(eweight, 0) > 0";
//        System.out.println("--------------------sql:-----------------------------\n"+sql);
        ResultSetList rs = con.getResultSetList(sql);//
        System.out.println("--------------------------------共查询总数："+rs.getRows()+"---------------------------------");
        while (rs.next()) {
            try {
                String piaojh = rs.getString("piaojh");
                sql = "select * from chepbtmp where piaojh='" + piaojh + "'";
                ResultSetList r = con.getResultSetList(sql);
                int fahb_id = 0;
                boolean isexist=false;
                if (r.next()) {
                    fahb_id = r.getInt("fahb_id");
                    isexist=true;

                }
                if (fahb_id == 0) {
                    if(isexist&&rs.getInt("caozlx")==0){//如果是插入且数据库中有数据
                        rs.setString("caozlx","1");
                    }else if(!isexist&&rs.getInt("caozlx")==1){//如果是更新且数据库中没有数据
                        rs.setString("caozlx","0");
                    }
                    int result = this.updateData(rs, "chepbtmp", "piaojh");
                    if (result != -1) {
                        intRuesltRow = intRuesltRow + 1;
                        sql = "update jk_truckenter set datastatus=1 ,READTIME=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') where ywid=  '" + piaojh + "'";
                        con.getUpdate(sql);
                    }
                }

                r.close();
                con.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        rs.close();
        return intRuesltRow;
    }

    private int updateData(ResultSetList rs, String tbnm, String clnm) throws Exception {
        int colCnt = rs.getColumnCount();
        String colNms[] = rs.getColumnNames();
        String colTyps[] = rs.getColumnTypes();
        String sql = "";
        String clnmValue = rs.getString(clnm);
        int caozlx = rs.getInt("caozlx");
        if (caozlx == 0) {
            String keys = "id";
            String values = rs.getString(0);
            for (int i = 1; i < colCnt; i++) {
                if (!colNms[i].toUpperCase().equals("CAOZLX")) {
                    keys += "," + colNms[i];
                    if (colTyps[i].equals("DATE")) {
                        Date riq = rs.getDate(i);
                        if(colNms[i].equalsIgnoreCase("ZHONGCSJ")||colNms[i].equalsIgnoreCase("QINGCSJ")
                                ||colNms[i].equalsIgnoreCase("CAIYRQ")||colNms[i].equalsIgnoreCase("LURSJ")){
                            String date = DateUtil.Formatdate("yyyy-MM-dd HH:mm:ss", riq);
                            values += ",to_date('" + date + "','yyyy-MM-dd hh24:mi:ss')";
                        }else{
                            String date = DateUtil.Formatdate("yyyy-MM-dd", riq);
                            values += ",to_date('" + date + "','yyyy-MM-dd')";
                        }

                    } else {
                        if (!rs.getString(i).equals("")) {
                            values += ",'" + rs.getString(i) + "'";
                        } else {
                            values += ",null";
                        }
                    }
                }
            }
            sql = "insert into " + tbnm + " (" + keys + ") values (" + values + ")";
        } else if (caozlx == 1) {
            String data = "";
            for (int i = 0; i < colCnt; i++) {
                if (!colNms[i].toUpperCase().equals("CAOZLX") && !colNms[i].toUpperCase().equals("ID")) {
                    if (colTyps[i].equals("DATE")) {
                        Date riq = rs.getDate(i);
                        if(colNms[i].equalsIgnoreCase("ZHONGCSJ")||colNms[i].equalsIgnoreCase("QINGCSJ")
                                ||colNms[i].equalsIgnoreCase("CAIYRQ")||colNms[i].equalsIgnoreCase("LURSJ")){
                            String date = DateUtil.Formatdate("yyyy-MM-dd HH:mm:ss", riq);
                            data += colNms[i] + "=to_date('" + date + "','yyyy-MM-dd hh24:mi:ss')"+ ((i == colCnt - 1) ? "" : ",\n");
                        }else{
                            String date = DateUtil.Formatdate("yyyy-MM-dd", riq);
                            data += colNms[i] + "=to_date('" + date + "','yyyy-MM-dd')" + ((i == colCnt - 1) ? "" : ",\n");
                        }


                    } else {
                        data += colNms[i] + "='" + rs.getString(i) + "'" + ((i == colCnt - 1) ? "" : ",\n");
                    }
                }
            }
            sql = "update " + tbnm + " set " + data + " where " + clnm + "='" + clnmValue+"'";
        } else if (caozlx == 2) {
            sql = "delete from " + tbnm + " where " + clnm + "='" + clnmValue+"'";
        }
//        System.out.println("sql:"+sql);
        return con.getUpdate(sql);
    }
}
