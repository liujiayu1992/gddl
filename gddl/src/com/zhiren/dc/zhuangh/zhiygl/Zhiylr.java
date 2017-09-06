package com.zhiren.dc.zhuangh.zhiygl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhiylr extends BasePage implements PageValidateListener {
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public static int InsYangpdhb(JDBCcon con, long diancxxb_id, long zhilb_id, String caiyb_id, 
			String zhillsb_id, String leibid, String bmid, String zhuanmsz, String leib){
		String sql = "insert into yangpdhb (id,caiyb_id,zhilblsb_id,bianh,leib,BUMB_ID,leibb_id) "+
		"values(getnewid(" + diancxxb_id + ")," + caiyb_id + "," + zhillsb_id + 
		",'" + zhuanmsz + "','" + leib + "'," + bmid + "," + leibid + ")";
		return con.getInsert(sql);
	}
	
	private void Save() {
		 Visit visit = (Visit) this.getPage().getVisit();
		 Save1(getChange(), visit);
	}
	private void Save1(String change,Visit visit) {
		String tableName = "yangpdhb";
		JDBCcon con = new JDBCcon();

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(change);
		while (delrsl.next()) {
//			StringBuffer sql = new StringBuffer("begin \n");
//			sql.append("delete from ").append(tableName).append(" where id =")
//					.append(delrsl.getString(0)).append(";\n");
//			sql.append("delete from yangpdhb where zhilblsb_id=").append(delrsl.getString(0)).append(";\n");
//			sql.append("delete from zhuanmb where zhillsb_id=").append(delrsl.getString(0)).append(";\n");
//			sql.append("end;");
//			con.getUpdate(sql.toString());
		}
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);
		long Diancxxb_id=0;
		if (visit.isFencb()) {
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			Diancxxb_id = visit.getDiancxxb_id();
		}
		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
				
			} else {
				String ss=" select id from renyxxb where quanc='"+mdrsl.getString("MINGC")+"'";
				ResultSetList r = con.getResultSetList(ss);
				String mingc="";
				String zhuanmlb_id="";
				String sa=" select id from zhuanmlb where jib=2 ";
				while(r.next()){
					mingc=r.getString("id");
					
				}
				r=con.getResultSetList(sa);
				while(r.next()){
					zhuanmlb_id=r.getString("id");
				}
				StringBuffer sql2 = new StringBuffer("begin \n");
				// �ȼ��Ȼ��û�������ٲ���
				
				String ss_ren=" select renyxx from zhiyryglb  where yangpdhb_id="+mdrsl.getString("id")+"";
				ResultSetList r_ren = con.getResultSetList(ss_ren);
				if(r_ren.getRows()>0){
					sql2.append(" delete from zhiyryglb where yangpdhb_id="+mdrsl.getString("id")+"").append(";\n");
				}
				if("".equals(mdrsl.getString("MINGC"))||mdrsl.getString("MINGC")==null){
					setMsg("��ѡ��������Ա");
					return;
					
				}else{
					
					sql2.append(" insert into zhiyryglb (yangpdhb_id,renyxx) values ("+mdrsl.getString("ID")+",'"+mdrsl.getString("MINGC")+"')").append(";\n");
				}
				
				if(visit.getboolean4()){//�ֶ���д
					
//					sql2.append(" insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id) values (getnewid("+visit.getDiancxxb_id()+")," +
//							""+mdrsl.getString("ZHILBLSB_ID")+",'"+mdrsl.getString("BBIANM")+"',"+zhuanmlb_id+")").append(";\n");
				}
				sql2.append("update ").append(tableName).append(" set zhiysj=to_date('").append(mdrsl.getString("ZHIYSJ")).append("','YYYY-MM-DD')");
//				sql2.append(",zhiyy='").append(mdrsl.getString("ZHIYY")).append("',zhiyml=").append(mdrsl.getString("ZHIYML")).append(",zhiylx='").append(mdrsl.getString("ZHIYLX")).append("'").append("\n");
				sql2.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
//				sql2.append("update zhuanmb set bianm='").append(mdrsl.getString("BIANM")).append("' where zhillsb_id=").append(mdrsl.getString("ID")).append(";\n");
				sql2.append("end;");
				int flag = 0;
				if(sql2.length()>13){
					flag = con.getUpdate(sql2.toString());
				}
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
							+ sql2);
					return;
				}
				
		   }
		}
	}
	
	
	public static int CreatBianh(JDBCcon con, long zhilb_id, long diancxxb_id,
			long meikxxb_id, long jihkjb_id, long yunsfsb_id, long chezxxb_id, 
			String bianm,String caiyy,String caiysj,String caiyml) {
		String sql ="";
		ResultSetList rsl = null;
		String caiyb_id = null;
		int flag ;
//		�жϴ�����ID�Ƿ���� ��������򷵻��½���ųɹ�
		sql = "select * from zhuanmb where zhillsb_id in (select id from zhillsb"+
		" where zhilb_id in(" + zhilb_id+") and caiysj is null)";
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
					zhillsb_id, leibid, bumid, leib,caiyy,caiysj,caiyml);
			if(flag == -1){
//				��������򷵻ش���
				return -1;
			}
			if(bianm != null){
//				��� ��¼����==�������б��� ��ʹ��ת������
//				������Զ����ɱ���			
				zhuanmsz = "";
				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, false, zhuanmsz);
				if(flag == -1){
//					��������򷵻ش���
					return -1;
				}
			}else{
//				�ж�������Զ����ɱ���
				flag = Zhuanmcz(con, diancxxb_id, zhillsb_id, bianm, shifzm, zhuanmsz);
				if(flag == -1){
//					��������򷵻ش���
					return -1;
				}
			}
		}
		rsl.close();
		return 0;
	}
	
	public static int InsZhillsb(JDBCcon con, long diancxxb_id, long zhilb_id, 
			String zhillsb_id, String leibid, String bmid, String leib,String caiyy,String caiysj,String caiyml){
		String sql = "insert into zhillsb (id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID,caiyy,caiysj,caiyml) "+
		"values(" + zhillsb_id + "," + zhilb_id + ",0,'" + leib + "'," + leibid + "," + bmid + ",'"+caiyy+"',to_date('"+caiysj+"','YYYY-MM-DD hh24:mi:ss'),"+caiyml+")";
		return con.getInsert(sql);
	}
	
	public static int InsZhuanmb(JDBCcon con,long zhuanmlb_id, long diancxxb_id, 
			String zhillsb_id, String bianm){
		String sql = "insert into zhuanmb(id,zhillsb_id,bianm,zhuanmlb_id) " +
			"values(getnewid(" + diancxxb_id + ")," + zhillsb_id + ",'" + bianm +
			"'," + zhuanmlb_id + ")";
		return con.getInsert(sql);
	}
	
	public static int Zhuanmcz(JDBCcon con, long diancxxb_id, String zhillsb_id,
			String bianm, boolean shifzm, String zhuanmqz){
		
		String sql = "select * from zhuanmlb where diancxxb_id =" + diancxxb_id;
		ResultSetList rsl =  con.getResultSetList(sql);
		long zhuanm = MainGlobal.getSequenceNextVal(con,SysConstant.BiascCodeSequenceName);
		int flag;
		while(rsl.next()){
			if(shifzm){
				zhuanm = zhuanm*2 + 729;
				bianm = zhuanmqz + zhuanm;
			}else{
				bianm = zhuanmqz + bianm;
			}
			flag = InsZhuanmb(con,rsl.getLong("id"),diancxxb_id,zhillsb_id,bianm);
			if(flag == -1){
				return -1;
			}
		}
		rsl.close();
		return 0;
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_RefurbishChick){
		   _RefurbishChick = false;
			getSelectData();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		String shij="select beiz from rulbzb where id= "+this.getFahrqValue();
		String beiz="";
		String beiz1="";
		String beiz2="";
		ResultSetList rslq1 = con.getResultSetList(shij);
		while(rslq1.next()){
			beiz=rslq1.getString("beiz");
		}
		if("".equals(beiz)){
			beiz="00-23"; 
			beiz1=beiz.substring(0,2)+":00:00";
			beiz2=beiz.substring(3,5)+":59:59";
		}else{ 
			if("24".equals(beiz.substring(3,5))){
				beiz1=beiz.substring(0,2)+":00:00";
				beiz2="23:59:59";
			}else{
				beiz1=beiz.substring(0,2)+":00:00";
				beiz2=beiz.substring(3,5)+":00:00";
			}
			
		}

	sb1
				.append("select a.id,a.zhilblsb_id,a.bianm,a.mingc,a.zhiysj,bb.bbianm from (\n"
						+ "select yb.id, yb.zhilblsb_id,zmb.bianm, zlb.renyxx as mingc,yb.zhiysj zhiysj,zmb.bianm as bbianm,zh.jib\n"
						+ "  from zhillsb zb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh, zhiyryglb zlb\n"
						+ " where yb.zhilblsb_id = zb.id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
						+ "   and zh.jib=1\n"
//						+ "  -- and zb.shenhzt=0\n"
//						+ "   --and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and zb.caiysj between to_date('"+this.getRiq1()+" "+"00:00:00"+"','YYYY-MM-DD hh24:mi:ss') " +//beiz1
						"and to_date('"+this.getRiq1()+" "+"23:59:59"+"','YYYY-MM-DD hh24:mi:ss')\n"//beiz2
						+ "    ) a,\n"
						+ "\n"
						+ "\n"
						+ "   ( select yb.id, yb.zhilblsb_id,zmb.bianm as bianm, zlb.renyxx as mingc, yb.zhiysj zhiysj,zmb.bianm as bbianm,zh.jib\n"
						+ "  from zhillsb zb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh, zhiyryglb zlb\n"
						+ " where yb.zhilblsb_id = zb.id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
						+ "   and zh.jib=2\n"
//						+ "  -- and zb.shenhzt=0\n"
//						+ "   --and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and zb.caiysj between to_date('"+this.getRiq1()+" "+"00:00:00"+"','YYYY-MM-DD hh24:mi:ss')-1 " +
						"and to_date('"+this.getRiq1()+" "+"23:59:59"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "    ) bb\n"
						+ "    where a.id=bb.id(+)\n"
						+ "\n"
						+ "\n"
						+ "union\n"
						+ "\n"
						+ "select c.id,c.zhilblsb_id,c.bianm,c.mingc,c.zhiysj,d.bbianm from (\n"
						+ "select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm, zlb.renyxx as mingc ,yb.zhiysj zhiysj,zmb.bianm as bbianm\n"
						+ "  from rulmzlzmxb rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb\n"
						+ " where yb.zhilblsb_id = rb.zhuanmbzllsb_id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "   --and rb.shenhzt=0\n"
						+ "   and  zh.jib=1\n"
//						+ "     --and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and rb.rulrq between to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')-1 " +
						"and to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "   ) c,\n"
						+ "   (\n"
						+ "   select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm, zlb.renyxx as mingc, yb.zhiysj zhiysj,zmb.bianm as bbianm\n"
						+ "  from rulmzlzmxb rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb\n"
						+ " where yb.zhilblsb_id = rb.zhuanmbzllsb_id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "   --and rb.shenhzt=0\n"
						+ "   and  zh.jib=2\n"
//						+ "    -- and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and rb.rulrq between to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')-1 " +
						"and to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "   ) d\n" + "   where c.id=d.id(+)\n" 
						+ "\n"
						
//						+ "union\n"
//						+ "\n"
//						+ "select ca.id,ca.zhilblsb_id,ca.bianm,ca.mingc,ca.zhiysj,da.bbianm from (\n"
//						+ "select yb.id, yb.zhilblsb_id,zmb.bianm as bianm, zlb.renyxx as mingc ,yb.zhiysj zhiysj,zmb.bianm as bbianm\n"
//						+ "  from QITYMXB rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb--,renyxxb r\n"
//						+ " where yb.zhilblsb_id = rb.id\n"
//						+ "\n"
//						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
//						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "   --and rb.shenhzt=0\n"
//						+ "   and  zh.jib=1\n"
//						+ "     --and zlb.renyxxb_id=r.id(+)\n"
//						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
//						+ "   and rb.songysj between to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')-1 " +
//						"and to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
//						+ "   ) ca,\n"
//						+ "   (\n"
//						+ "   select yb.id, yb.zhilblsb_id,zmb.bianm as bianm, zlb.renyxx as mingc, yb.zhiysj zhiysj,zmb.bianm as bbianm\n"
//						+ "  from QITYMXB rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb--,renyxxb r\n"
//						+ " where yb.zhilblsb_id = rb.id\n"
//						+ "\n"
//						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
//						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "   --and rb.shenhzt=0\n"
//						+ "   and  zh.jib=2\n"
//						+ "    -- and zlb.renyxxb_id=r.id(+)\n"
//						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
//						+ "   and rb.songysj between to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')-1 " +
//						"and to_date('"+this.getRiq1()+" "+"13:00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
//						+ "   ) da\n" + "   where ca.id=da.id(+)\n"
						+ "");
		
		// �����û�У���ʾ��һ��
		// �����û�У�������ʾ��
//		sb.append("select z.id,f.zhilb_id,to_char(z.zhiysj,'YYYY-MM-DD hh24:mi:ss') as zhiysj,b.bianm ,z.zhiyy from zhillsb z,fahb f,zhuanmb b, zhuanmlb l\n");
//		sb.append("where z.zhilb_id=f.zhilb_id \n");//
//		sb.append("and b.zhillsb_id=z.id \n");
//		sb.append("and b.zhuanmlb_id=l.id \n");
//		sb.append("and l.jib=2 \n");//jib=2��������
//		sb.append("and f.zhilb_id=").append(getYundxxValue().getId());
//System.out.println(sb.toString());
		ResultSetList rsl = con.getResultSetList(sb1.toString());
		ResultSetList rsl1;
		
//		if(rsl.getRows()>0){//��һ�������ݣ���ô����sb1
//			rsl1 = con.getResultSetList(sb1.toString());
//			if(rsl.getRows()>0){//��2�������ݣ���ô����
//				rsl=rsl1;
//			}
//		}
		
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
        String czh=
        	"select *\n" +
        	"  from xitxxb\n" + 
        	" where mingc = '�ж��Ƿ��Զ���������'\n" + 
        	"   and diancxxb_id = 300\n" + 
        	"   and zhi = '��'\n" + 
        	"   and leib = '���ƻ�'\n" + 
        	"   and zhuangt = 1\n" + 
        	"   and beiz = 'ʹ��'";
        ResultSetList rsl_czh = con.getResultSetList(czh);
        if(rsl_czh.getRows()>0){
        	visit.setboolean4(true);
        }
        
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setTableName("fahb");
		// ����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ�����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// ����ÿҳ��ʾ����
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setWidth(60);
		egu.getColumn("id").editor = null;
		egu.getColumn("zhilblsb_id").setHidden(true);
		egu.getColumn("zhilblsb_id").setWidth(60);
		egu.getColumn("zhilblsb_id").editor = null;
//		egu.getColumn("ZHILB_ID").setDefaultValue(""+getYundxxValue().getId());
		egu.getColumn("zhiysj").setHeader("��������");//Center
		egu.getColumn("zhiysj").setWidth(120);
		

		egu.getColumn("bbianm").setCenterHeader("��������");
		egu.getColumn("bbianm").setWidth(90);
		if(visit.getboolean4()){
			//�������true����ô���ֶ�
		}else{
			//�Զ�
			egu.getColumn("bbianm").setEditor(null);
		}
//		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("mingc").setCenterHeader("������Ա");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("bianm").setCenterHeader("��������");
		egu.getColumn("bianm").setWidth(90);
		egu.getColumn("bianm").setEditor(null);
		

		egu.addTbarText("����ʱ��");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
//		egu.addToolbarItem(df.getScript());
		
		egu.addToolbarItem(df.getScript());



//		ComboBox comb2 = new ComboBox();
//		comb2.setTransform("FahrqSelect");
//		comb2.setId("FahrqSelect");
//		comb2.setWidth(100);
//		comb2.setListeners("select:function(own,rec,index){Ext.getDom('FahrqSelect').selectedIndex=index}");
//		egu.addToolbarItem(comb2.getScript());
//
//		egu.addTbarText("-");// ���÷ָ���


		// ���÷������ں͵������ڵ�Ĭ��ֵ
		DateField datetime = new DateField();
		egu.getColumn("zhiysj").setEditor(datetime);
		egu.getColumn("zhiysj").setDefaultValue(DateUtil.FormatDate(new Date()));
		

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
//		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Yundxx.getRawValue()=='��ѡ��'){alert('��ѡ���˵���Ϣ'); return;}");
		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '������Ա',\n" + 
			//"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
			"    \t\t\t}else{\n" + 
			"    \t\t\t\te.cancel = true;\n" + 
			"    \t\t\t}\n" + 
			"    \t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\taddNode(node);\n" + 
			"\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\tsubNode(node);\n" + 
			"\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\tnode.expand();\n" + 
			"\t\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\t\t\taddNode(child);\n" + 
			"\t\t\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\t\t\tsubNode(child);\n" + 
			"\t\t\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t\t}\n" + 
			"\t\t\t\t\t});\n" + 
			"\t\t\t\t}\n" + 
			"    \t},\n" + 
			"\t   \ttbar:[{text:'ȷ��',handler:function(){\n" + 
			"        var cs = navtree.getChecked();\n" + 
			"        rec = gridDiv_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('MINGC',tmp2);\n" + 
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			" win = new Ext.Window({\n" + 
			" title: '������Ա',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		
////		StringBuffer cpb = new StringBuffer();
////		cpb.append("function(){ if(!win){"+treepanel+Strtmpfunction+"}").append(
////				"win.show(this);	\n}");
////		GridButton cpr = new GridButton("ѡ�������Ա", cpb.toString());
//		cpr.setIcon(SysConstant.Btn_Icon_Insert);
//		egu.addTbarBtn(cpr);
//		egu.addTbarText("-");
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
				+ "row = irow; \n"
				+ "if('MINGC' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"if(!win){"+treepanel+Strtmpfunction+"}"
				+"win.show(this);}});\n");
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		con.Close();
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		���������Ĳ�ѯSQL
		String sql=
			"select 0 id,'��' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select r.id, r.quanc  as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from diancxxb d,renyxxb r\n" + 
			" where\n" + 
			"r.diancxxb_id=d.id\n" + 
			"and r.bum='����' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

//	����ʲôʱ�������ѡ���
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			JDBCcon con=new JDBCcon();
			ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()+" and zhuangt=1 and mingc='����ú���ڲ�ѯĬ��ֵ'");
			String zhi="";
			while(rsl.next()){
				zhi=rsl.getString("zhi");
			}
			if(zhi.equals("�벴����")){
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(2));
			}else if(zhi.equals("��������")){
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(1));
			}else{
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean3(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		list.add(new IDropDownBean(1, "��������"));
		list.add(new IDropDownBean(2, "��������"));
		list.add(new IDropDownBean(3, "�벴����"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

//	 ������
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}

	}
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

//	 ʱ���
	public IDropDownBean getFahrqValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getFahrqModel().getOptionCount()>0) {
				setFahrqValue((IDropDownBean)getFahrqModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setFahrqValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getFahrqModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setFahrqModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setFahrqModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setFahrqModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql_yunsfs = "";
		String sql="";
//		if(HAIY_SH.equals(getLeix())){
			
			sql=
"select id,mingc from rulbzb where diancxxb_id="+visit.getDiancxxb_id();
 
		setFahrqModel(new IDropDownModel(sql));
	}

	public boolean _Yundxxchange = false;

	public IDropDownBean getYundxxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getYundxxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYundxxValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setYundxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYundxxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYundxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getYundxxModels() {
		
		String sql="";
		
		switch ((int) this.getWeizSelectValue().getId()) {
		case 1:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.kaobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.kaobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// ��������
		case 2:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.daobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// ��������
		case 3:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daohrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.daohrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// �벴����
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ����ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
//			setYundxxValue(null);		//4
//			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
//			setYundxxModel(null);	
//			getYundxxModels();	
			setFahrqValue(null);
			setFahrqModel(null);
			getFahrqModel();
			setTbmsg(null);
			initNavigation();
			visit.setString1(null);
		}
		if(visit.getboolean1()){
			
			visit.setboolean1(false);
//			visit.setString9("");
			visit.setList1(null);
//			setYundxxValue(null);		//4
//			setYundxxModel(null);
			setFahrqValue(null);
			setFahrqModel(null);
			getFahrqModel();
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			visit.setString1(null);
		}
		if(visit.getboolean2()){
			visit.setboolean2(false);
			setFahrqValue(null);
			setFahrqModel(null);
			getFahrqModel();
		}
		if(visit.getboolean3()){
		   visit.setboolean3(false);
//		   setYundxxValue(null);		//4
//		   setYundxxModel(null);
			setFahrqValue(null);
			setFahrqModel(null);
			getFahrqModel();
		   visit.setString1(null);
		}
		
		visit.setboolean4(false);
		initNavigation();
		getSelectData();
	}
}