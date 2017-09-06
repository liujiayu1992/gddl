package com.zhiren.sms;

import java.sql.ResultSet;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

public class SmsManager {
	public static void SendSms() {
		JDBCcon cn = new JDBCcon();
		ResultSet rs_c = cn.getResultSet("select zhi from xitxxb where mingc = '串口号' and zhuangt=1");
		String com = "COM1";
		try {
			if (rs_c.next()) {
				com = rs_c.getString("zhi");
			}
			rs_c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

//		当用到短信定义时，数据插入duanxdyb的同时也插入到duanxfsb，
//		判断duanxdyb的发送时间是否是当日，如果是跳出
//		如果不是更新duanxdyb、duanxfsb的发送时间，然后让定时器来执行短信发送

		String id = "";
//		String duanxdypzb_id = "";
		String fassj = "";
		String fassj1 = "";
		String shij = "";
//		String fujxx="";
		String sql1 = "select d.id,d.duanxdypzb_id,fujxx,to_char(d.fassj,'yyyy-mm-dd') fassj,to_char(d.fassj,'hh24:mi:ss') fassj1  from duanxdyb d where zhuangt=1";
		ResultSetList rsl1 = cn.getResultSetList(sql1);

		String sql2 = "select to_char(sysdate,'yyyy-mm-dd') shij from dual";
		ResultSetList rsl2 = cn.getResultSetList(sql2);
		if (rsl2.next()) {
			shij = rsl2.getString("shij");
		}
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String fashs="";
		String neir1="";
		String sql_neir="";
		String sql_neir1="";
		while (rsl1.next()) {
			id = rsl1.getString("id");
//			duanxdypzb_id = rsl1.getString("duanxdypzb_id");
			fassj = rsl1.getString("fassj");
			fassj1 = rsl1.getString("fassj1");
//			fujxx = rsl1.getString("fujxx");

			if (fassj.equals(shij)){

			} else {
				
				 sql_neir="select fashs from duanxdypzb where id in (select duanxdypzb_id from duanxdyb where id="+id+")";
				ResultSetList rsl_neir=cn.getResultSetList(sql_neir);
				if(rsl_neir.next()){
					fashs=rsl_neir.getString("fashs");
					sql_neir1="select "+fashs+" neir1 from dual ";
					ResultSetList rsl_neir1=cn.getResultSetList(sql_neir1);
					if(rsl_neir1.next()){
						neir1=rsl_neir1.getString("neir1");
						sql6="update duanxfsb a set a.neir='"+neir1+"' where id="+id;
						cn.getUpdate(sql6);
					}
				}
				sql3 = "update duanxdyb a set a.fassj=to_date('" + shij + " "+ fassj1+ "','yyyy-mm-dd hh24:mi:ss') where id=" + id;
				sql4 = "update duanxfsb a set a.riq=to_date('" + shij + " "+ fassj1+ "','yyyy-mm-dd hh24:mi:ss') where id=" + id;
				sql5 = " update duanxjsrb set zhuangt=0 where duanxfsb_id="+id;
				cn.getUpdate(sql3);
				cn.getUpdate(sql4);
				cn.getUpdate(sql5);
			}
		}

		String sql = "";

		// 查询所有需要处理的短信内容
		ResultSetList rs = cn.getResultSetList("select\n"
				+ "d.id id,d1.id did,d.zhuangt,d1.riq riq,d.jiesr,d1.neir,d1.fujxx\n"
				+ " from duanxjsrb d , duanxfsb d1\n"
				+ "where d.duanxfsb_id=d1.id\n" + "and d.zhuangt=0  " +
						"  and d1.riq<=trunc(sysdate,'mi') " +
						"\n");
		if (rs.getRows() > 0) {
			JinDiSms sms = new JinDiSms();
			System.out.print("串口号是：" + com);
			if (sms.Start(com)) {
				while (rs.next()) {
					sql = "select l.shouj from lianxrb l where l.id in("
							+ rs.getString("jiesr") + ") ";
					ResultSetList rsl = cn.getResultSetList(sql);
					while (rsl.next()) {
						if (sms.SendMessage(rsl.getString("shouj"), rs
								.getString("neir")+rs
								.getString("fujxx") )) {
							cn.getUpdate(" update duanxjsrb set zhuangt=1 where id="+ rs.getString("id"));
						} else {
							cn.getUpdate(" update duanxfsb set ErrorInfo='发送时发生错误' where id="+ rs.getString("did"));
						}
					}
				}
				sms.Stop();
			} else {
				// 设备没有打开
			}
			sms = null;
		} else{
			System.out.print("没有手动待发短信内容");}

		rs.close();
		cn.Close();
	}
}
