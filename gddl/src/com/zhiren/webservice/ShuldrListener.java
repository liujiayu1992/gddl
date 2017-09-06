package com.zhiren.webservice;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;

import com.zhiren.common.ResultSetList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.ResultSet;
import java.util.*;
/*
 * ʱ�䣺2016-8-25
 * ���ߣ���־��
 * ������ÿСʱִ�д�����һ�Σ�ֱ��ÿ������10�㣬�����Զ�����
 * */


public class ShuldrListener implements ServletContextListener {
    private static JDBCcon con = null;
    private static final Timer timer = new Timer();

    public void contextDestroyed(ServletContextEvent arg0) {
        timer.cancel();
        System.out.println("����");
    }

    public void contextInitialized(ServletContextEvent arg0) {
//		long period = 1000 * 60 * 60 *24;
//		long period = 1000 * 60 * 60 *1;
        try{
            con = new JDBCcon();
            String sql="select zhi from xitxxb where diancxxb_id='938' and mingc='��������ʱ����' and zhuangt=1";
            ResultSetList rs = con.getResultSetList(sql);
            long zhi=0;
            if(rs.next()){
                zhi=rs.getLong("zhi");
            }else{
                zhi=5;
            }
            long period = 1000 * 60 * zhi;
            con.Close();
//		��ʱ����Ϊ60��ִ��һ��
//		period=5000;
            System.out.println("--------------------------------���ݵ����������!---------------------------------------------");
            timer.schedule(new AutoInvTask(), 0, period);//86400000
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class AutoInvTask extends TimerTask {
        public void run() {
            con = new JDBCcon();
            int intResult = daorChepbtmp();//������
            con.commit();
            if (intResult > 0) {
                System.out.println("-------------------------���ݵ������,�������!" + intResult + "������!---------------");
            }
            con.Close();
        }
    }

    //-------------------------------------------------��������----------------------------------------------------------
    int daorChepbtmp() {  //
        int intRuesltRow = 0;
        String sql =
                " select\n" +
                        "  XL_CHEPBTMP_ID.nextval id ,\n" +
                        "  id as v_TRUCKENTER_ID,\n" +
                        "  samcode V_TRUCKENTER_SAMCODE,\n" +
                        "  -------------------------------------------------------------\n" +
                        "  938 as diancxxb_id,--�糧��Ϣ��Id\n" +
                        "  ywid piaojh ,--�˵�Ʊ�ݺ�\n" +
                        "  collieryname meikdwmc ,--ú�λ����\n" +
                        "  decode(goodsname,'ȼú','ԭú',goodsname) pinz ,--ȼ��Ʒ�ֱ�\n" +
                        "  nvl(dispatchstation,'��') faz ,--��վ\n" +
                        "  nvl(arrivestation,'��') daoz ,--��վ\n" +
                        "  trunc(decode(eweightime,null,sysdate,to_date(eweightime,'yyyy-mm-dd hh24:mi:ss'))) fahrq ,--��������\n" +
                        "  transtype yunsfs ,--���䷽ʽ(��·����·������)\n" +
                        "  samcode chec ,--����\n" +
                        "  samcode caiybh ,--�������\n" +
                        "   samcode beiz,\n"+
                        "   -1 fahbtmp_id,\n"+
                        "  numberplate cheph ,--��Ƥ��\n" +
                        "  fweight maoz ,--ë��\n" +
                        "  eweight piz ,--Ƥ��\n" +
                        "  dedcutton koud ,--�۶�\n" +
                        "  '����' jianjfs ,--��﷽ʽ(���⡢���)\n" +
                        "  3 chebb_id ,--����·��,�Ա�,��,����\n" +
                        "  nvl(arrivestation,'��') yuandz ,--ԭ��վ\n" +
                        "  transunitname yunsdwb_id ,--���䵥λ\n" +
                        "  decode(eweightime,null,sysdate,to_date(eweightime,'yyyy-mm-dd hh24:mi:ss') ) qingcsj ,--�ᳵʱ��(�������Ƥ)\n" +
                        "  trunc(decode(eweightime,null,sysdate,to_date(eweightime,'yyyy-mm-dd hh24:mi:ss') )) daohrq ,--��������\n" +
                        "\n" +
                        "  --------------------------------------------------------------\n" +
                        "  hashno qingchh ,--�ᳵ���\n" +
                        "  hashuser qingcjjy ,--�ᳵ���Ա\n" +
                        "  to_date(fweightime,'yyyy-mm-dd hh24:mi:ss') zhongcsj,--�س�ʱ��(�������ë)\n" +
                        "  weighno zhongchh ,--�س����\n" +
                        "  weightuser zhongcjjy ,--�س����Ա\n" +
                        "  nvl(to_date(tosamtime,'yyyy-mm-dd hh24:mi:ss'),sysdate) caiyrq ,--��������\n" +
                        "  '��е' xiecfs ,--ж����ʽ\n" +
                        "  fweight yuanmz,\n" +
                        "  eweight yuanpz,\n" +
                        "  handlertype caozlx,\n" +
                        "  --------------------------------------------------------------\n" +
                        "  sysdate lursj,--¼��ʱ��\n" +
                        "  '��������' YUANSHDW,  --coalfieldname meic ,\n" +
                        "  collieryname yuanmkdw,  --ԭú��λ ,\n" +
                        "  -- transunitcode chedhm ,\n" +
                        "  -- netweight jingz ,\n" +
                        "  decode(nvl(COALNETWEIGHT,0),0,netweight,COALNETWEIGHT) biaoz ,--Ʊ��\n" +
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
                        "         and g.leix = 1 ) gongysmc,--��Ӧ������\n" +
                        "  (SELECT nvl(MAX(MINGC), '�г��ɹ�')\n" +
                        "   FROM JIHKJB\n" +
                        "   WHERE ID = (SELECT MAX(JIHKJB_ID) AS JIHKJB_ID\n" +
                        "               FROM MEIKXXB\n" +
                        "               WHERE mingc = collieryname)) jihkj,--�ƻ��ھ�\n" +
                        "   TRANSUNITNAME yunsdw,--���䵥λ\n" +
                        "  'ϵͳ����' lury\n" +
                        "from jk_truckenter\n" +
                        "where  DATASTATUS = 0\n" +
                        "       and nvl(eweight, 0) > 0";
//        System.out.println("--------------------sql:-----------------------------\n"+sql);
        ResultSetList rs = con.getResultSetList(sql);//
        System.out.println("--------------------------------����ѯ������"+rs.getRows()+"---------------------------------");
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
                    if(isexist&&rs.getInt("caozlx")==0){//����ǲ��������ݿ���������
                        rs.setString("caozlx","1");
                    }else if(!isexist&&rs.getInt("caozlx")==1){//����Ǹ��������ݿ���û������
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
