package com.zhiren.webservice;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysInfo;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ��������  
 * ������	InterFac		����	����
	������1��	��Ӧ����	request	
	������1��	�û���	usr	String
	������2��	����	password	String
	������3��	������	task	String
	����ֵ��	�ɹ�/ʧ��1,ʧ��2		String

	������2��	��Ӧ����	requestall	
	������1��	�û���	usr	String
	������2��	����	password	String
	����ֵ��	�ɹ�/ʧ��1,ʧ��2		String
 *
 */
public class InterFac {
	private static final String error006="-1,006,Զ��webservice�������δ֪���⣬������webservice����ʧ�ܡ���";
	private static final String error101="-1,101,��������ʧ�ܣ���url���ܶ�λ������";
	private static final String error102="-1,102,��Դ��λ��url�����������";
	private static final String error103="-1,103,����������sql������";//
	private static final String error104="-1,104,����web������ʧ�ܣ�";
	private static final String error105="1,105,����δ֪�쳣��";
	//tem1="-1,"+"106,"+parmeter[4].toString();//�ͻ��˴���
	private static final String rizPath="C:/fassj_zr";// ������־·��
	private String user;
	private String password;
	private String endpointAddress;
//	
	public InterFac() {
		super();
		// TODO �Զ����ɹ��캯�����
		SysInfo xitxx=new SysInfo();
		user=MainGlobal.getXitxx_item("�ӿ���","�ӿ��ϴ��û���", "197", " ");
		password=MainGlobal.getXitxx_item("�ӿ���","�ӿ��ϴ�����", "197", " ");
		endpointAddress=MainGlobal.getXitxx_item("�ӿ���","��ڵ�ַ", "197", " ");
	}
	//����һ��sql����ܹ��������Ľ���ַ������ַ�����ʽ 1123 ��20
	//������֤����
	public String getSqlString(String sql){
		String reslutStr="";
		JDBCcon con=new JDBCcon();
		ResultSet rs=con.getResultSet(sql);
		try{
			int Colucount=rs.getMetaData().getColumnCount();
			if(rs.next()){//�涨ֻ����һ������ֵ
				for(int i=1;i<=Colucount;i++){
					String tem=rs.getString(i);
					if(i==1){
						reslutStr+=(tem==null?"":tem);
					}else{
						reslutStr+=","+(tem==null?"":tem);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return reslutStr;
	}
	public void shangc(String diancxxb_id,String riq1,String riq2,String leix){
		//1,д���񣬱����봥�����ظ�
		//��������������ͬ�������ֱ������ֱ��۸�ֱ����ۿ�ֱ����ֱַ�ú��˷ѡ��ɱ��±�����¯ú��������¯ú�������պĴ��ձ�
		//�����ձ�������ָ�����ݱ�������ʱ��
		JDBCcon con=new JDBCcon();
		String changbb_id="0";
		String changbWhere=" and  changbb_id<>-1 ";
		String sql="";
		if(!diancxxb_id.equals("-1")){
			sql=
				"select id\n" +
				"from changbb\n" + 
				"where id_jit="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			try {
				if(rs.next()){
					changbb_id=rs.getString(1);
				}else{
					return;
				}
				changbWhere=" and changbb_id="+changbb_id;
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		}

		//fahb
		if(leix.equals("fahb")||leix.equals("ȫ��")){
			sql=
			"insert into jiekrwb\n" +
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (select xl_jiekrwb_id.nextval id,\n" + 
			"          'fahb' renwmc,\n" + 
			"          id renwbs,\n" + 
			"          0 renllx,\n" + 
			"          0 zhixzt,\n" + 
			"          changbb_id,\n" + 
			"          'xml' mingllx,to_date(guohrq,'yyyy-mm-dd')\n" + 
			"     from(\n" + 
			"     select distinct fahb.id,'fahb'renwmc,0 renllx,to_char(chepb.guohsj,'yyyy-mm-dd')guohrq,changbb_id\n" + 
			"     from fahb, chepb\n" + 
			"    where fahb.id = chepb.fahb_id��\n" + //and fahb.hedbz=1
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') >= '"+riq1+"'\n" + 
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') <= '"+riq2+"'\n" + 
			"     "+changbWhere+" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb))";
	
			con.getInsert(sql);
		}
		//zhilb
		if(leix.equals("zhilb")||leix.equals("ȫ��")){
	    sql=
	    	"insert into jiekrwb\n" +
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (select xl_jiekrwb_id.nextval id,\n" + 
			"          'zhilb' renwmc,\n" + 
			"          zhilb_id renwbs,\n" + 
			"          0 renllx,\n" + 
			"          0 zhixzt,\n" + 
			"          changbb_id,\n" + 
			"          'xml' mingllx,to_date(guohrq,'yyyy-mm-dd')\n" + 
			"     from(\n" + 
			"     select distinct fahb.zhilb_id,'zhilb'renwmc,0 renllx,to_char(chepb.guohsj,'yyyy-mm-dd')guohrq,changbb_id\n" + 
			"     from fahb, chepb,zhilb\n" + 
			"    where fahb.zhilb_id=zhilb.id and  zhilb.shangjshzt=1 and fahb.id = chepb.fahb_id��\n" + // and fahb.hedbz=1
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') >= '"+riq1+"'\n" + 
			"      and to_char(chepb.guohsj, 'YYYY-MM-DD') <= '"+riq2+"'\n" + 
			"     "+changbWhere+"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb ))";

			con.getInsert(sql);
		}
			//zhilbtmp
		if(leix.equals("zhillsb")||leix.equals("ȫ��")){
		    sql=
		    	"insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (select xl_jiekrwb_id.nextval id,\n" + 
				"          'zhillsb' renwmc,\n" + 
				"          id renwbs,\n" + 
				"          0 renllx,\n" + 
				"          0 zhixzt,\n" + 
				"         changbb_id,\n" + 
				"          'xml' mingllx,to_date(guohrq,'yyyy-mm-dd')\n" + 
				"     from(\n" + 
				"     select distinct  zhilbtmp.id,'zhillsb'renwmc,0 renllx,to_char(chepb.guohsj,'yyyy-mm-dd')guohrq,changbb_id\n" + 
				"     from fahb, chepb,zhilbtmp\n" + 
				"    where  fahb.zhilb_id=zhilbtmp.zhilb_id  and zhilbtmp.shangjshzt=1 and fahb.id = chepb.fahb_id��\n" + 
				"      and to_char(chepb.guohsj, 'YYYY-MM-DD') >= '"+riq1+"'\n" + 
				"      and to_char(chepb.guohsj, 'YYYY-MM-DD') <= '"+riq2+"'\n" + 
				"    "+changbWhere+"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb ))";

				con.getInsert(sql);
		}
//				hetb
		if(leix.equals("hetb")||leix.equals("ȫ��")){
			    sql="insert into jiekrwb\n" +
			    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			    "  (\n" + 
			    "  select xl_jiekrwb_id.nextval id,'hetb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,to_date(qiandrq,'yyyy-mm-dd')\n" + 
			    "  from(\n" + 
			    "  select id,'hetb'renwmc,0 renllx,to_char(qiandrq,'yyyy-mm-dd')qiandrq,changbb_id\n" + 
			    "  from hetxxb where " + 
			    "    to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"'\n" + 
			    "  and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"'\n" + 
			    changbWhere+
			    "  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='hetb')\n" + 
			    "  )";
				con.getInsert(sql);
//				hetslb
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				" (\n" + 
				"select xl_jiekrwb_id.nextval id, 'hetslb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				"from(\n" + 
				"select nianjhhtb.id,'hetslb'renwmc,0 renllx,changbb_id\n" + 
				"from nianjhhtb,hetxxb\n" + 
				"where nianjhhtb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetslb')\n" + 
				" )";
				con.getInsert(sql);	
//				hetzlxyb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetzlb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				" select hetzlxyb.id,'hetzlb'renwmc,0 renllx,changbb_id\n" + 
				" from hetzlxyb,hetxxb\n" + 
				" where hetzlxyb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetzlb' ))";
				con.getInsert(sql);	
//				hetjgb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetjgb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				"  select hetjsxyb.id,'hetjgb'renwmc,0 renllx,changbb_id\n" + 
				" from hetjsxyb,hetxxb\n" + 
				" where hetjsxyb.biaoz=0  and hetjsxyb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetjgb'))";
				con.getInsert(sql);	
//				hetzkkb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetzkkb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				"   select hetjsxyb.id,'hetzkkb'renwmc,0 renllx,changbb_id\n" + 
				" from hetjsxyb,hetxxb\n" + 
				" where hetjsxyb.biaoz=1  and hetjsxyb.hetxxb_id =hetxxb.id and  to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"\n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetzkkb' ))";
				con.getInsert(sql);	
//				hetwzb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
				"  (\n" + 
				" select xl_jiekrwb_id.nextval id, 'hetwzb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
				" from(\n" + 
				"  select id,'hetwzb'renwmc,0 renllx,changbb_id\n" + 
				" from hetxxb\n" + 
				"  where to_char(qiandrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(qiandrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='hetwzb'  ))";
				con.getInsert(sql);	
		}
//				jiesb
		if(leix.equals("jiesb")||leix.equals("ȫ��")){
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'jiesb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,to_date(jiesrq,'yyyy-mm-dd')\n" + 
				"  from(\n" + 
				"    select id,'jiesb'renwmc,0 renllx,to_char(jiesrq,'yyyy-mm-dd')jiesrq,changbb_id\n" + 
				"  from jiesb\n" + 
				"  where kuangfjs<>1 and  to_char(jiesb.jiesrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(jiesb.jiesrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='jiesb'  ))";

				con.getInsert(sql);
//				jiesyfb
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'jiesyfb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(jiesrq,'yyyy-mm-dd')\n" + 
				"  from(\n" + 
				"   select id,'jiesyfb'renwmc,0 renllx,to_char(jiesrq,'yyyy-mm-dd')jiesrq,changbb_id\n" + 
				"  from jiesb\n" + 
				"  where kuangfjs<>1 and   to_char(jiesb.jiesrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(jiesb.jiesrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+" \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='jiesyfb' ))";

				con.getInsert(sql);
//				jieszbsjb
				sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'jieszbsjb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,to_date(jiesrq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				"select id,'jieszbsjb'renwmc,0 renllx,to_char(jiesrq,'yyyy-mm-dd')jiesrq,changbb_id\n" + 
				"from jiesb\n" + 
				"where kuangfjs<>1 and   to_char(jiesb.jiesrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(jiesb.jiesrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='jieszbsjb'))";
				con.getInsert(sql);
		}
//				yuercbmdj
		if(leix.equals("yuercbmdj")||leix.equals("ȫ��")){
				sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'yuercbmdj'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(riq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				" select id,'yuercbmdj'renwmc,0 renllx,c.nianf||'-'||to_char(yuef,'00')||'-01' riq,changbb_id\n" + 
				"from chengbmxybb c\n" + 
				"where  to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"' and (dangyjlj='����'or dangyjlj='����')\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='yuercbmdj'))";
				con.getInsert(sql);
////				yuetjkjb
//				sql="insert into jiekrwb\n" +
//				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx)\n" + 
//				" (\n" + 
//				" select xl_jiekrwb_id.nextval id, 'yuetjkjb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx\n" + 
//				" from(\n" + 
//				" select id,'yuetjkjb'renwmc,0 renllx,changbb_id\n" + 
//				" from chengbmxybb c\n" + 
//				" where  to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.nianf||'-'||c.yuef||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"' and (dangyjlj='����'or dangyjlj='����')\n" + changbWhere+ 
//				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,changbb_id from jiekrwb where renwmc='yuetjkjb'))";
//				con.getInsert(sql);
		}
//				rulmzlb
		if(leix.equals("rulmzlb")||leix.equals("ȫ��")){
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'rulmzlb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,rulrq\n" + 
				" from(\n" + 
				"  select id,'rulmzlb'renwmc,0 renllx,rulrq,changbb_id\n" + 
				" from rulmzlb r\n" + 
				" where  r.shenhzt=3 and to_char(rulrq,'YYYY-MM-DD')>='"+riq1+"' and to_char(rulrq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='rulmzlb'))";
				con.getInsert(sql);
		}
//				haoyqkmb
		if(leix.equals("meihyb")||leix.equals("ȫ��")){
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'meihyb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				" from(\n" + 
				" select id,'meihyb'renwmc,0 renllx,riq,changbb_id\n" + 
				" from haoyqkmb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+"  \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='meihyb'))";
				con.getInsert(sql);
		}
//				shouhcrbb
		if(leix.equals("shouhcrbb")||leix.equals("ȫ��")){
				sql="insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				"\n" + 
				" select xl_jiekrwb_id.nextval id, 'shouhcrbb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				" from (\n" + 
				" select id,riq,changbb_id\n" + 
				" from shouhcrbmb where id in(\n" + 
				" select id\n" + 
				" from(\n" + 
				" select id,'shouhcrbb'renwmc,0 renllx\n" + 
				" from shouhcrbmb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' "+changbWhere+" \n" + 
				" minus select  to_number(renwbs)renwbs,renwmc,renllx from jiekrwb where renwmc='shouhcrbb')\n" + 
				")\n" + 
				" order by riq\n" + 
				")\n" + 
				")";
				con.getInsert(sql);
		}
//				riscsjb
		if(leix.equals("riscsjb")||leix.equals("ȫ��")){
				sql="insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'riscsjb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				"  from(\n" + 
				"    select id,'riscsjb'renwmc,0 renllx,riq,changbb_id\n" + 
				"  from dianlrb r\n" + 
				"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' and r.jizh<>'�ϼ�'  "+changbWhere+" \n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='riscsjb'))";
				con.getInsert(sql);
		}
		if(leix.equals("yuecgjhb")||leix.equals("ȫ��")){
			sql=
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'yuecgjhb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(riq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				" select id,'yuecgjhb'renwmc,0 renllx,c.niand||'-'||trim(to_char(yued,'00'))||'-01' riq,changbb_id\n" + 
				"from yuedjhfb c\n" + 
				"where  to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='yuecgjhb'))";
				con.getInsert(sql);
//			yuexqjhh
			sql=
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'yuexqjhh'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,to_date(riq,'yyyy-mm-dd')\n" + 
				"from(\n" + 
				" select id,'yuexqjhh'renwmc,0 renllx,c.niand||'-'||trim(to_char(yued,'00'))||'-01' riq,changbb_id\n" + 
				"from yuedjhzb c\n" + 
				"where  to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'  and to_char(to_date(c.niand||'-'||trim(c.yued)||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<= '"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,to_char(renwsj,'yyyy-mm-dd')riq,changbb_id from jiekrwb where renwmc='yuexqjhh'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("quzpkb")||leix.equals("ȫ��")){
			sql=
				"insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'quzpkb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				" from(\n" + 
				" select id,'quzpkb'renwmc,0 renllx,riq,changbb_id\n" + 
				" from quzpkqkb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='quzpkb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("zhuangcyb")||leix.equals("ȫ��")){
			sql=
				"insert into jiekrwb\n" +
				" (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				" (\n" + 
				" select xl_jiekrwb_id.nextval id, 'zhuangcyb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				" from(\n" + 
				" select id,'zhuangcyb'renwmc,0 renllx,riq,changbb_id\n" + 
				" from kuangfybb r\n" + 
				" where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				" minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='zhuangcyb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("tielyb")||leix.equals("ȫ��")){
			sql=
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'tielyb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'tielyb'renwmc,0 renllx,riq,changbb_id\n" + 
				"from tielybb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='tielyb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("pandb")||leix.equals("ȫ��")){
		   //�̵���ܱ�
			sql=//pandb����
				"insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'pandb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'pandb'renwmc,0 renllx,riq,changbb_id\n" + 
				"from panmyqkbgzb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandb'))";
			con.getInsert(sql);
			//pandzmy,pandzmm����
			sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'pandzmy'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'pandzmy'renwmc,0 renllx,riq,changbb_id\n" + 
				"from panmyqkbgzb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandzmy'))";
			con.getInsert(sql);
			sql="insert into jiekrwb\n" +
				"(id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"(\n" + 
				"select xl_jiekrwb_id.nextval id, 'pandzmm'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
				"from(\n" + 
				"select id,'pandzmm'renwmc,0 renllx,riq,changbb_id\n" + 
				"from panmyqkbgzb r\n" + 
				"where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				"minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandzmm'))"; 
		    con.getInsert(sql);
		    //�ܱ��µķֱ�ͻ������ݣ�û���ܱ�Ĳ��ϴ�
		    sql=
		    	"insert into jiekrwb\n" +
		    	"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    	"  (\n" + 
		    	"  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    	"  from(\n" + 
		    	"          select id,'pandyb'renwmc,0 renllx,riq,changbb_id\n" + 
		    	"          from pandcyb\n" + 
		    	"          where to_char(pandcyb.riq,'yyyymmdd')||pandcyb.changbb_id in\n" + 
		    	"          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    	"          from panmyqkbgzb\n" + 
		    	"          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+" )\n" + 
		    	"          minus\n" + 
		    	"          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandyb'\n" + 
		    	"  )\n" + 
		    	" ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" + 
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandmdb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from meicmd\n" + 
		    "          where to_char(meicmd.riq,'yyyymmdd')||meicmd.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'" + changbWhere+"  )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandmdb'\n" + 
		    "  ) ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" +
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandtjb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from meicpd\n" + 
		    "          where to_char(meicpd.riq,'yyyymmdd')||meicpd.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"  )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandtjb'\n" + 
		    "  ) ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" +
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandwzcmb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from weizcm\n" + 
		    "          where to_char(weizcm.riq,'yyyymmdd')||weizcm.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+" )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandwzcmb'\n" + 
		    "  ) ) ";
		    con.getInsert(sql);
		    sql=
		    "insert into jiekrwb\n" +
		    "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
		    "  (\n" + 
		    "  select xl_jiekrwb_id.nextval id,renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
		    "  from(\n" + 
		    "          select id,'pandbmryzzb'renwmc,0 renllx,riq,changbb_id\n" + 
		    "          from pandbp\n" + 
		    "          where to_char(pandbp.riq,'yyyymmdd')||pandbp.changbb_id in\n" + 
		    "          (select to_char(panmyqkbgzb.riq,'yyyymmdd')||panmyqkbgzb.changbb_id\n" + 
		    "          from panmyqkbgzb\n" + 
		    "          where to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+" )\n" + 
		    "          minus\n" + 
		    "          select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='pandbmryzzb'\n" + 
		    "  )) ";
			con.getInsert(sql);
			//pandtb xml

			 sql="insert into jiekrwb\n" +
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'pandtb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
			"  from(\n" + 
			"    select id,'pandtb'renwmc,0 renllx,riq,changbb_id,'xml'mingllx\n" + 
			"  from pandtb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id,mingllx from jiekrwb where renwmc='pandtb'))\n" ;
			 con.getInsert(sql);
			 sql="   insert into jiekrwb\n" + 
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj,minglcs)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'pandtb'renwmc,pandt.id renwbs,0 renllx,0 zhixzt, pandt.changbb_id,'file'mingllx1,pandt.riq,\n" + 
			"  (select zhi from xitxxb where duixm='�̵�ͼ��Ŀ¼')||'\\'||pan.fuj minglcs\n" + 
			"  from(\n" + 
			"  select id,'pandtb'renwmc,0 renllx,riq,'file'mingllx,changbb_id\n" + 
			"  from pandtb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,mingllx,changbb_id from jiekrwb where renwmc='pandtb')pandt,pandtb pan\n" + 
			"  where pandt.id=pan.id)\n" ;
			 con.getInsert(sql);
			 sql="    insert into jiekrwb\n" + 
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'yuemcmyzb'renwmc,id renwbs,0 renllx,0 zhixzt,changbb_id,'xml'mingllx,riq\n" + 
			"  from(\n" + 
			"    select id,'yuemcmyzb'renwmc,0 renllx,riq,changbb_id,'xml'mingllx\n" + 
			"  from yuemcmyzb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id,mingllx from jiekrwb where renwmc='yuemcmyzb'))\n" ;
			 con.getInsert(sql);
			 sql="    insert into jiekrwb\n" + 
			"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
			"  (\n" + 
			"  select xl_jiekrwb_id.nextval id, 'yuemcmyb'renwmc,id renwbs,0 renllx,0 zhixzt,1 changbb_id,'xml'mingllx,riq\n" + 
			"  from(\n" + 
			"    select id,'yuemcmyb'renwmc,0 renllx,riq,changbb_id,'xml'mingllx\n" + 
			"  from yuemcmyzb p\n" + 
			"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'  " + changbWhere+"\n" + 
			"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id,mingllx from jiekrwb where renwmc='yuemcmyb'))";
			 con.getInsert(sql);
	   }
	   if(leix.equals("shouhcrbyb")||leix.equals("ȫ��")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'shouhcrbyb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'shouhcrbyb'renwmc,0 renllx,riq,changbb_id\n" + 
				   "  from ranlkcqk r\n" + 
				   "  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"   and r.pinz='��'\n" + 
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='shouhcrbyb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("yuezbb")||leix.equals("ȫ��")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'yuezbb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'yuezbb' renwmc,0 renllx, to_date(r.niand||'-'||r.yued||'-'||'01','YYYY-MM-DD')  riq,changbb_id\n" + 
				   "  from rulmydtjb r\n" + 
				   "  where  to_char(to_date(r.niand||'-'||r.yued||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')>='"+riq1+"'\n" + 
				   "   and to_char(to_date(r.niand||'-'||r.yued||'-'||'01','YYYY-MM-DD'),'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+"\n" + 
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='yuezbb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("jizyxqkb")||leix.equals("ȫ��")){
			sql=
				"insert into jiekrwb\n" +
				"  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				"  (\n" + 
				"  select xl_jiekrwb_id.nextval id, 'jizyxqkb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				"  from(\n" + 
				"    select id,'jizyxqkb'renwmc,0 renllx,riq,changbb_id\n" + 
				"  from dianlrb r\n" + 
				"  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"' and to_char(riq,'YYYY-MM-DD')<='"+riq2+"' " + changbWhere+" and jizh <> '�ϼ�' and jizzk is not null\n" + 
				"  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='jizyxqkb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("shebyxqkb")||leix.equals("ȫ��")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'shebyxqkb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'shebyxqkb' renwmc,0 renllx,   riq,changbb_id\n" + 
				   "  from jiexsbb\n" + 
				   "  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"'\n" + 
				   "   and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='shebyxqkb'))";
			con.getInsert(sql);
	   }
	   if(leix.equals("rigjb")||leix.equals("ȫ��")){
			sql=
				 "insert into jiekrwb\n" +
				   "  (id, renwmc, renwbs, renllx, zhixzt, changbb_id, mingllx,renwsj)\n" + 
				   "  (\n" + 
				   "  select xl_jiekrwb_id.nextval id, 'rigjb'renwmc,id renwbs,0 renllx,0 zhixzt, changbb_id,'xml'mingllx,riq\n" + 
				   "  from(\n" + 
				   "    select id,'rigjb' renwmc,0 renllx, riq,changbb_id\n" + 
				   "  from rigjb\n" + 
				   "  where  to_char(riq,'YYYY-MM-DD')>='"+riq1+"'\n" + 
				   "   and to_char(riq,'YYYY-MM-DD')<='"+riq2+"'\n" + changbWhere+
				   "  minus select  to_number(renwbs)renwbs,renwmc,renllx,renwsj,changbb_id from jiekrwb where renwmc='rigjb'))";
			con.getInsert(sql);
	   }
		con.Close();
		//2,���ýӿ�
		if(leix.equals("ȫ��")){
			requestall(diancxxb_id,riq1,riq2);
			requestFile(diancxxb_id,"",riq1,riq2);
		}else{
			request(leix,diancxxb_id,riq1,riq2);
			requestFile(diancxxb_id,leix,riq1,riq2);
		}
	}
	public void request(String task,String diancxxb_id,String riq1,String riq2){////Զ�̵��á����ض�ʱ���� and j.renwmc='"+task+"'order by id0";
		String renwmc="",renwsj="",id_jit="",renllx="",id="",shujjl="",tiaoj="",xml="",id0="",tem1="",tem2="";
		String message="";
		String diancidWhere ="";
		String diancidWhere1 ="";
		String riqWhere=" where  renwsj>='"+riq1+"' and renwsj<='"+riq2+"' ";
		String riqWhere1=" and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'��ʱ',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')>='"+riq1+
		"' and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'��ʱ',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')<='"+riq2+"' ";
	
		//ɾ���鿴�ļ��е������ļ�
		File file=new File(rizPath);
		if(!file.exists()){
			file.mkdir();
		}
		File[] files=file.listFiles();
		for(int j=0;j<files.length;j++){
			files[j].delete();
		}
		JDBCcon con=new JDBCcon();
		JDBCcon con1=null;
		
		if(!diancxxb_id.equals("-1")){
			diancidWhere=" and id_jit="+diancxxb_id;
			diancidWhere1=" and changbb_id =(select id from changbb where id_jit="+diancxxb_id+")";
	    }
		if(riq1==null||riq2==null||riq1.equals("")||riq2.equals("")){
			riqWhere="";
			riqWhere1="";
		}
		String sql=
//������ʱ��
//			"select changbb.id_jit,j.id id0,to_char(j.renwsj,'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" +//||' '||p.renwbs||''''||changbb.id_jit||j.renwbs||''''
//			"from jiekrwb j,jiekfspzb p ,changbb\n" + 
//			"where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.changbb_id=changbb.id and j.mingllx=p.mingllx "+diancidWhere+" and j.renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"') order by id0";

			"select *\n" +
			"from(\n" + 
			"  select changbb.id_jit,j.id id0,to_char(decode(renwmcb.pinl,'��ʱ',j.shengcsj,j.renwsj),'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" + 
			"from jiekrwb j,jiekfspzb p ,changbb,renwmcb\n" + 
			"where j.renwmc=renwmcb.renw and j.mingllx='xml' and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.changbb_id=changbb.id and j.mingllx=p.mingllx  "+diancidWhere+" and j.renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"') order by id0\n" + 
			") "+riqWhere;

	    ResultSet rs=con.getResultSet(sql);
		 sql=//Ҫִ�еĽ��м�����0��-1����ִ�гɹ���ִ��ʧ�ܵĻ��Զ���������δִ�е�Ҫ�˹����н���
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt=2\n" + 
		"where mingllx='xml' and(jiekrwb.zhixzt=0 or jiekrwb.zhixzt=-1) and renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"') "
		+riqWhere1+diancidWhere1;
		con.getUpdate(sql);
		Service service = new Service();
		try{
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renwsj=rs.getString("renwsj");
				renllx=rs.getString("renllx");
				id=rs.getString("id");//����Ψһ�����ʶid
				id0=rs.getString("id0");//����id
				shujjl=rs.getString("shujjl");
				tiaoj=rs.getString("tiaoj");
				id_jit=rs.getString("id_jit");
				shujjl=shujjl.replaceAll("%%", tiaoj);
			    con1=new JDBCcon();
				    if(renllx.equals("2")){//��Ϊ����ǰ��Ҫɾ���������޸���������һ���Ķ�����ɾ��������
				    	renllx="0";
				    }
					xml=CreateXml(renwmc,renwsj,id_jit,renllx,id,	con1.getResultSet(shujjl));
					Call call = (Call) service.createCall();//Զ�̵�����
					java.net.URL url=new java.net.URL(endpointAddress);
					call.setTargetEndpointAddress(url);
					call.setOperationName("incept");
					call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("XMLData", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
					call.setReturnType(XMLType.SOAP_STRING);
					tem1=String.valueOf(call.invoke(new Object[] {user,password,xml.getBytes()}));//д��־
					//��������
//					String[]dat=data.split(",");//dat[0] 
					Xierz(tem1,id0);
//				}
					con1.Close();
			}
			StringBuffer id0s=new StringBuffer();
			List sqls=CreateSql(id0s);
			for (int ii=0;ii<sqls.size();ii++){
				Call call = (Call) service.createCall();//Զ�̵�����
				call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
				call.setOperationName("execute");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				
				String data=String.valueOf(call.invoke(new Object[] {user,password,sqls.get(ii).toString()}));//д��־
//				String in=sqls.get(ii).toString().substring(sqls.get(ii).toString().lastIndexOf("in"));
				Xierzexe(data,id0s.toString());
			}
		}catch(SQLException e){//����жϵĴ���
			jies();
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//����urlʱ����
			jies();
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//Զ��δ֪������������No route to host: connect
			jies();
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// �������
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			jies();
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			jies();
			message=error105;
			e.printStackTrace();
		}finally{
			if(con1!=null){
				con1.Close();
			}
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	public  void requestall(String diancxxb_id,String riq1,String riq2) {////Զ�̵��á����ض�ʱ����
		String renwmc="",renwsj="",id_jit="",renllx="",id="",shujjl="",tiaoj="",xml="",id0="",tem1="",tem2="";
		String message="";
		String diancidWhere ="";
		String diancidWhere1 ="";
		String riqWhere=" where  renwsj>='"+riq1+"' and renwsj<='"+riq2+"' ";
		String riqWhere1=" and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'��ʱ',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')>='"+riq1+
		"' and to_char(decode((select renwmcb.pinl from renwmcb where renwmcb.renw=jiekrwb.renwmc),'��ʱ',jiekrwb.shengcsj,jiekrwb.renwsj),'yyyy-mm-dd')<='"+riq2+"' ";
		//ɾ���鿴�ļ��е������ļ�  
		File file=new File(rizPath);
		if(!file.exists()){
			file.mkdir();
		}
		File[] files=file.listFiles();
		for(int j=0;j<files.length;j++){
			files[j].delete();
		}
		JDBCcon con=new JDBCcon();
		JDBCcon con1=null;
		if(!diancxxb_id.equals("-1")){
			diancidWhere=" and id_jit="+diancxxb_id;
			diancidWhere1=" and changbb_id =(select id from changbb where id_jit="+diancxxb_id+")";
	    }
		if(riq1==null||riq2==null||riq1.equals("")||riq2.equals("")){
			riqWhere="";
			riqWhere1="";
		}
		String sql=
//			������ʱ��
//			"select changbb.id_jit,j.id id0,to_char(j.renwsj,'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" +//||' '||p.renwbs||''''||changbb.id_jit||j.renwbs||''''
//			"from jiekrwb j,jiekfspzb p ,changbb\n" + 
//			"where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.changbb_id=changbb.id and j.mingllx=p.mingllx "+diancidWhere+" order by id0";
		"select *\n" +
		"from(\n" + 
		"  select changbb.id_jit,j.id id0,to_char(decode(renwmcb.pinl,'��ʱ',j.shengcsj,j.renwsj),'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" + 
		"from jiekrwb j,jiekfspzb p ,changbb,renwmcb\n" + 
		"where j.renwmc=renwmcb.renw (+)" +
				"and j.mingllx='xml' " +
				"and (zhixzt=0 or zhixzt=-1) " +
				"and j.renwmc=p.renwmc " +
				"and j.changbb_id=changbb.id(+) " +
				"and j.mingllx=p.mingllx (+) "+diancidWhere+"  order by id0\n" +
		")  "+riqWhere;
		try{
	    ResultSet rs=con.getResultSet(sql);
//	    sql=//Ҫִ�еĽ��м�����0��-1����ִ�гɹ���ִ��ʧ�ܵĻ��Զ���������δִ�е�Ҫ�˹����н���
//			"update jiekrwb\n" +
//			"set jiekrwb.zhixzt=2\n" +
//			"where  mingllx='xml' and(jiekrwb.zhixzt=0 or jiekrwb.zhixzt=-1) "
//			+riqWhere1+diancidWhere1;
//			con.getUpdate(sql);
		
		Service service = new Service();
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renllx=rs.getString("renllx");
				id=rs.getString("id");//����Ψһ�����ʶid
				id0=rs.getString("id0");//����id
				shujjl=rs.getString("shujjl");
				tiaoj=rs.getString("tiaoj");
				renwsj=rs.getString("renwsj");
				id_jit=rs.getString("id_jit");
				shujjl=shujjl.replaceAll("%%", tiaoj);
			    con1=new JDBCcon();
			    if(renllx.equals("2")){//��Ϊ����ǰ��Ҫɾ���������޸���������һ���Ķ�����ɾ��������
			    	renllx="0";
			    }
				xml=CreateXml(renwmc,renwsj,id_jit,renllx,id,	con1.getResultSet(shujjl));
				Call call = (Call) service.createCall();//Զ�̵�����
				java.net.URL url=new java.net.URL(endpointAddress);
				call.setTargetEndpointAddress(url);
				call.setOperationName("incept");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("XMLData", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				tem1=String.valueOf(call.invoke(new Object[] {user,password,xml.getBytes()}));//д��־
				//��������
//					String[]dat=data.split(",");//dat[0] 
				Xierz(tem1,id0);
				con1.Close();
			}
			StringBuffer id0s=new StringBuffer();
			List sqls=CreateSql(id0s);
			for (int ii=0;ii<sqls.size();ii++){
				Call call = (Call) service.createCall();//Զ�̵�����
				call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
				call.setOperationName("execute");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				
				String data=String.valueOf(call.invoke(new Object[] {user,password,sqls.get(ii).toString()}));//д��־
//				String in=sqls.get(ii).toString().substring(sqls.get(ii).toString().lastIndexOf("in"));
				Xierzexe(data,id0s.toString());
			}
		}catch(SQLException e){//����жϵĴ���
			jies();
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//����urlʱ����
			jies();
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//Զ��δ֪������������No route to host: connect
			jies();
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// �������
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			jies();
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			jies();
			message=error105;
			e.printStackTrace();
		}finally{
			if(con1!=null){
				con1.Close();
			}
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	private void jies(){//�� 0��-1�Ľ���
		JDBCcon con=new JDBCcon();
		String sql="update jiekrwb\n" +
		"set jiekrwb.zhixzt=0\n" + 
		"where jiekrwb.zhixzt=2 and jiekrwb.zhixbz is null";
		con.getResultSet(sql);
		sql=
			"update jiekrwb\n" +
			"set jiekrwb.zhixzt=-1\n" + 
			"where jiekrwb.zhixzt=2 and jiekrwb.zhixbz  is not null";
		con.getResultSet(sql);
		con.Close();
	}
	private  String CreateXml(String shujxy,String renwsj,String id_jit,String caoz,String guanlId,ResultSet rs) throws SQLException, IOException{
		String xmlAray="";//û�м�¼�ͻ����쳣����������
		// TODO �Զ����ɷ������
		Element root = new Element("����֡");
		Document document = new Document(root);
		
//		root.setAttribute(new Attribute("vin", "123fhg5869705iop90"));
		root.addContent(new Element("����Э��").addContent(shujxy));
		root.addContent(new Element("����").addContent(caoz));
		root.addContent(new Element("����").addContent(guanlId));
		root.addContent(new Element("��������").addContent(renwsj));
		root.addContent(new Element("�糧id").addContent(id_jit));
		
			while(rs.next()){
				Element elShujjl = new Element("���ݼ�¼");
				root.addContent(elShujjl);
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
					elShujjl.addContent(new Element(rs.getMetaData().getColumnName(i)).addContent(rs.getString(i)));
					
				}
			}
			File file=new File(rizPath) ;
			FileWriter writer = new FileWriter(file.getAbsolutePath()+"/"+shujxy+guanlId+caoz+".xml");
			XMLOutputter outputter = new XMLOutputter();   
			Format format=Format.getPrettyFormat(); 
			format.setEncoding("gb2312"); 
//			format.setOmitDeclaration(true);
			outputter.setFormat(format);
			outputter.output(document, writer);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			outputter.output(document, bo);
			xmlAray=bo.toString();
			//xmlAray=bo.toByteArray();
		return xmlAray;
	}
	private   List CreateSql(StringBuffer id0s) throws SQLException{
		List resultSql=new ArrayList();
		JDBCcon con=new JDBCcon();
		String ids="";
		String sql=
			"select j.id,p.renwsql biaom,p.gengxy,changbb.id_jit||j.minglcs gengxyz,p.renwbs,changbb.id_jit||j.renwbs renwbsz\n" +
			"from jiekrwb j,jiekfspzb p,changbb\n" + 
			"where j.renwmc=p.renwmc and j.mingllx=p.mingllx and j.mingllx<>'xml'and j.mingllx<>'file' and zhixzt=0 and j.changbb_id=changbb.id" +
			" order by biaom,gengxy,gengxyz,renwbs";
		ResultSet rs=con.getResultSet(sql);
		String biaom="",gengxy="",gengxyz="",renwbs="",renwbsz="";
		String biaom_p="",gengxy_p="",gengxyz_p="",renwbs_p="";
		String in="(";
		int kk=0;
		try{
			while(rs.next()){
				ids=ids+rs.getString("id")+",";
				biaom=rs.getString("biaom");
				gengxy=rs.getString("gengxy");
				gengxyz=rs.getString("gengxyz");
				renwbs=rs.getString("renwbs");
				renwbsz=rs.getString("renwbsz");
				if(kk==0||(biaom.equals(biaom_p)&&gengxy.equals(gengxy_p)&&gengxyz.equals(gengxyz_p)&&renwbs.equals(renwbs_p))){//�����ǰֵ��һ����Ȼ��һ��ֵ��˵��Ϊͬһ������
					//׷��in��ģ���������
					biaom_p=biaom;
					gengxy_p=gengxy;
					gengxyz_p=gengxyz;
					renwbs_p=renwbs;
					
					if(kk==0){
						in+=renwbsz;
					}else{
						in+=","+renwbsz;
					}
					kk++;
				}else{ //����������һ���·��顣
					in+=")";
					resultSql.add("update "+biaom_p+" set "+gengxy_p+"'"+gengxyz_p+"' where "+renwbs_p+in);
					//��ʼ����������
					kk=1;
					in="(";
					biaom_p=biaom;
					gengxy_p=gengxy;
					gengxyz_p=gengxyz;
					renwbs_p=renwbs;
					in+=renwbsz;
				}
			}
			//���һ������
			if(kk!=0){
				in+=")";
				resultSql.add("update "+biaom+" set "+gengxy+"'"+gengxyz+"' where "+renwbs+in);
			}
		}finally{
			con.Close();
		}
		if(!ids.equals("")){
			id0s.append(ids.substring(0, ids.lastIndexOf(",")));
		}
		return resultSql;
	}
	public  void requestFile(String diancxxb_id,String task,String riq1,String riq2) {////Զ�̵��á����ض�ʱ����
		String renwmc="",filePath="",id_jit="",renllx="",id0="",tem1="";
		Object[] parmeter=null;
		String message="";
		String diancidWhere ="";
		String taskWhere=" and j.renwmc in (select renw from renwmcb where fuid in(select id from renwmcb where renw='"+task+"' )or renw='"+task+"')";
		String riqWhere=" where  renwsj>='"+riq1+"' and renwsj<='"+riq2+"' ";
		JDBCcon con=new JDBCcon();
		if(!diancxxb_id.equals("-1")){
			diancidWhere=" and id_jit="+diancxxb_id;
	    }
		if(riq1==null||riq2==null||riq1.equals("")||riq2.equals("")){
			riqWhere="";
		}
		if(task==null ||task.endsWith("")){
			taskWhere="";
		}
		String sql=
		"select *\n" +
		"from(\n" + 
		"  select changbb.id_jit,j.id id0,to_char(decode(renwmcb.pinl,'��ʱ',j.shengcsj,j.renwsj),'yyyy-mm-dd')renwsj,j.renwmc,j.renllx, changbb.id_jit||j.renwbs id,j.minglcs\n" + 
		"from jiekrwb j,changbb,renwmcb\n" + 
		"where j.renwmc=renwmcb.renw and j.mingllx='file' and (zhixzt=0 or zhixzt=-1)  and j.changbb_id=changbb.id   "+diancidWhere+taskWhere+" order by id0\n" + 
		") "+riqWhere;
	    ResultSet rs=con.getResultSet(sql);
	   
		
		Service service = new Service();
		try{
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renllx=rs.getString("renllx");
				id0=rs.getString("id0");//����id
				filePath=rs.getString("minglcs");
				id_jit=rs.getString("id_jit");
				//����filePath��renwmc��id_jit,renllx����������ļ��洢���ĸ�����
				parmeter=parmeterFilestroe(filePath,renwmc,id_jit,renllx);//��������0:path1:name2:caoz3��������true;
				if(parmeter[3].equals("true")){//���Ϊ��˵���߱����ϴ�����
					Call call = (Call) service.createCall();//Զ�̵�����
					java.net.URL url=new java.net.URL(endpointAddress);
					call.setTargetEndpointAddress(url);
					call.setOperationName("fileStroe");
					call.addParameter("path", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("name", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("file", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
					call.addParameter("caoz", XMLType.SOAP_STRING,ParameterMode.IN);
					call.setReturnType(XMLType.SOAP_STRING);
					tem1=String.valueOf(call.invoke(new Object[] {parmeter[0],parmeter[1],fileByte,parmeter[2]}));//д��־
				}else{
					tem1="-1,"+"106,"+parmeter[3].toString();//�ͻ��˴���
				}
				Xierz(tem1,id0);////"1,'000','���ճɹ�'";
			}
			
		}catch(SQLException e){//����жϵĴ���
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//����urlʱ����
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//Զ��δ֪������������No route to host: connect
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// �������
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			message=error105;
			e.printStackTrace();
		}finally{
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	private byte[] fileByte=null;
	private Object[] parmeterFilestroe(String filePath,String renwmc,String id_jit,String renllx){
		//filePath:D:\Tomcat 5.0\webapps\ftp\20081210.dmp
		//0:����+�糧��pandtb��108��1:name2:caoz3,err
		Object[] temArry=new String[4];
		InputStream is = null;
		ByteArrayOutputStream byteOut=null;
		//���ļ�ת���ɶ���������byte
		if (filePath != null&& !filePath.equals("")) {
			try {
				File file=new File(filePath);
				//�ϴ����ļ�����������ݶ�Ӧ
				temArry[0]=renwmc+","+id_jit;
				//�ϴ����ļ�����������ݶ�Ӧ
				temArry[1]=filePath.substring(filePath.lastIndexOf("\\")+1);
				is = new FileInputStream(file);
				byteOut=new ByteArrayOutputStream();
				byte[] buff = new byte[1024];
				while (is.read(buff) != -1) {
					byteOut.write(buff);
				}
				fileByte=byteOut.toByteArray();//���ض���������byte
				temArry[2]=renllx;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.print("�ϴ�����");
				temArry[3]=e.getMessage();
				return temArry;
			} finally {
				try {
					byteOut.close();
					is.close();
				} catch (Exception e) {
					
				}
			}
		}else{
			temArry[3]="·��Ϊ�գ�";
			return temArry;
		}
		temArry[3]="true";
		return temArry;
	}
	private void Xierz(String data,String id0){//д��־
		//"1,'000','���ճɹ�'";
		String[]dat=data.split(",");
		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		
		"where id='"+id0+"'";
		con.getUpdate(sql);
		con.Close();
	}
	private void Xierzexe(String data,String in){//ִ����־
		//"1,'000','���ճɹ�'";
		String[]dat=data.split(",");
		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		"where id in("+in+")";
		con.getUpdate(sql);
		con.Close();
	}
	public String[]  sqlExe(String[] sqls,boolean isTransaction) throws Exception{
		//1�������ݿ�
		String ConnStr="",UserName="",UserPassword="";
		String[] arryRes=new String[sqls.length];
		if (ConnStr.equals("")) {
			ConnStr = MainGlobal.getDb_jdbcDriverURL();
		}
		if (UserName.equals("")) {
			UserName = MainGlobal.getDb_username();
		}
		if (UserPassword.equals("")) {
			UserPassword = MainGlobal.getDb_password();
		}

		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection dBConnection = DriverManager.getConnection(ConnStr,UserName, UserPassword);
		Statement st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		//2ѭ��ִ��sqls������¼ִ�н����
		try{
			if(isTransaction){//���������
				dBConnection.setAutoCommit(false);
				for (int i=0;i<sqls.length;i++){
					try {
						st.executeUpdate(sqls[i]);
						arryRes[0]="true";
					} catch (SQLException e) {
						// TODO �Զ����� catch ��
						arryRes[0]=e.getLocalizedMessage();
						e.printStackTrace();
						dBConnection.rollback();
						return arryRes;
					}
				}
				dBConnection.commit();
			}else{// ��������
				for (int i=0;i<sqls.length;i++){
					try {
						st.executeUpdate(sqls[i]);
						arryRes[i]="true";
					} catch (SQLException e) {
						// TODO �Զ����� catch ��
						arryRes[i]=e.getLocalizedMessage();
						e.printStackTrace();
					}
				}
			}
		}
		finally{
			st.close();
			dBConnection.close();
		}
		return arryRes;
	}

}
