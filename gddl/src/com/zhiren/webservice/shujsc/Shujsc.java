package com.zhiren.webservice.shujsc;

import com.zhiren.common.JDBCcon;
import com.zhiren.webservice.InterFac_dt;

/**
 * Created by liuzhiyu on 2017/4/12.
 */
public class Shujsc extends InterFac_dt {
    public void addjiekrw(String id,String renwmc,String renwlx,String diancxxb_id,String riq ){
        JDBCcon con = new JDBCcon();
        try{
            String sql=" insert into jiekrwb (ID,RENWMC,RENWBS,RENLLX,CHANGBB_ID,MINGLLX,MINGLCS,RENWSJ)\n" +
                    "values(xl_jiekrwb_id.nextval,'"+renwmc+"',"+id+","+renwlx+","+diancxxb_id+",'xml',"+id+",date'"+riq+"')";
            con.getUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
            con.rollBack();
        }finally {
            con.Close();
        }
    }
}
