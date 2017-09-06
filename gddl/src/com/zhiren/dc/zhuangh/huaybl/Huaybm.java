package com.zhiren.dc.zhuangh.huaybl;


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

/*
 * ���ߣ����
 * ʱ�䣺2012-09-01
 * ���������ӡ����۵��顯�У���ȷ�ϱ���������ûҲ����ɱ��༭
 * 		zhillsb������HUIRDSF�ֶβ���������ʽ���棬Ĭ��ֵΪ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-17
 * ���������ӡ����������
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-03-10
 * ����������ׯ���볧��¯�����жϣ�������ʱ��ڵ�����9����������9��Ϊһ��ͳ�����ڡ�
 * 		����������ڵ�BUG���������ע�͡�
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-04-1
 * ����������ׯ���볧��¯�����жϣ�������ʱ��ڵ�����9����������9��Ϊһ��ͳ�����ڡ�
 * 		��2����¯ú��Ϊ1����9����2����9�����¯ú��
 */
public class Huaybm extends BasePage implements PageValidateListener {
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
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}
	public void setChangeid(String changeid) {
		Changeid = changeid;
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
		JDBCcon con = new JDBCcon();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);

		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
				
			} else {
				String ss=" select id from renyxxb where quanc='"+mdrsl.getString("MINGC")+"'";
				ResultSetList r = con.getResultSetList(ss);
//				String mingc="";
				String zhuanmlb_id="";
				String sa=" select id from zhuanmlb where jib=3 ";
//				while(r.next()){
//					mingc=r.getString("id");
//					
//				}
				r=con.getResultSetList(sa);
				while(r.next()){
					zhuanmlb_id=r.getString("id");
				}
				StringBuffer sql2 = new StringBuffer("begin \n");
				
				if(mdrsl.getString("quf").equals("1")){
					sql2.append(" update zhillsb set querry='"+visit.getRenymc()+"', quersj=to_date('")
					.append(mdrsl.getString("quersj")).append("','YYYY-MM-DD hh24:mi:ss'), HUIRDSF='"+mdrsl.getString("HUIRDSF")+"' where id="+getChangeid()).append(";\n") ;
				}else if(mdrsl.getString("quf").equals("2")){
					sql2.append("update rulmzlzmxb set querry='"+visit.getRenymc()+"', quersj=to_date('")
					.append(mdrsl.getString("quersj")).append("','YYYY-MM-DD hh24:mi:ss') where zhuanmbzllsb_id="+mdrsl.getString("zhilblsb_id")).append(";\n");
				}else{
					sql2.append("update QITYMXB set querry='"+visit.getRenymc()+"', quersj=to_date('")
					.append(mdrsl.getString("quersj")).append("','YYYY-MM-DD hh24:mi:ss') where id="+getChangeid()).append(";\n");
				}
				if(visit.getboolean4()){//true ��ô�ֶ���д
					
					sql2.append(" insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id) values (getnewid("+visit.getDiancxxb_id()+")," +
							""+mdrsl.getString("ZHILBLSB_ID")+",'"+mdrsl.getString("BBIANM")+"',"+zhuanmlb_id+")").append(";\n");
				}else{
					//�Զ���ʵҳ��û�б仯��Ҳ���ᷢ�������¼�
				}
				sql2.append("end;");
				if(sql2.length()>13){
					
					con.getUpdate(sql2.toString());
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
//		String beiz1="";
//		String beiz2="";
		ResultSetList rslq1 = con.getResultSetList(shij);
		while(rslq1.next()){
			beiz=rslq1.getString("beiz");
		}
		if("".equals(beiz)){
			beiz="00-23"; 
//			beiz1=beiz.substring(0,2)+":00:00";
//			beiz2=beiz.substring(3,5)+":59:59";
		}else{ 
//			if("24".equals(beiz.substring(3,5))){
////				beiz1=beiz.substring(0,2)+":00:00";
////				beiz2="23:59:59";
//			}else{
////				beiz1=beiz.substring(0,2)+":00:00";
////				beiz2=beiz.substring(3,5)+":00:00";
//			}
			
		}
		String shijd=MainGlobal.getXitxx_item("����", "�볧��¯�ڵ�ʱ��", "0", "0");
		sb1.append("select a.id,a.zhilblsb_id,DECODE(1,1,'�볧')leib,a.bianm,bb.bbianm,bb.querry,decode(bb.quersj,'',to_char(sysdate,'YYYY-MM-DD hh24:mi:ss'),to_char(bb.quersj,'YYYY-MM-DD hh24:mi:ss')) quersj,'1' as quf,a.huirdsf  from (\n"
						+ "select distinct yb.id, yb.zhilblsb_id,zmb.bianm, r.quanc as mingc, to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm,zh.jib,zb.huirdsf\n"
						+ "  from zhillsb zb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh, zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = zb.id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
						+ "   and zh.jib=2\n"
//						+ "  -- and zb.shenhzt=0\n"
						+ "   and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and zb.caiysj between to_date('"+this.getRiq1()+" "+"00:00:00"+"','YYYY-MM-DD hh24:mi:ss') " +
						"and to_date('"+this.getRiq1()+" "+"23:59:59"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "    ) a,\n"
						+ "\n"
						+ "\n"
						+ "   ( select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm,zb.querry,zb.quersj, r.quanc as mingc, to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm,zh.jib,zb.huirdsf\n"
						+ "  from zhillsb zb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh, zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = zb.id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
						+ "   and zh.jib=3\n"
//						+ "  -- and zb.shenhzt=0\n"
						+ "   and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and zb.caiysj between to_date('"+this.getRiq1()+" "+"00:00:00"+"','YYYY-MM-DD hh24:mi:ss') " +
						"and to_date('"+this.getRiq1()+" "+"23:59:59"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "    ) bb\n"
						+ "    where a.id=bb.id(+)\n"
						+ "\n"
						+ "\n"
						+ "union\n"
						+ "\n"
						+ "select  c.id,c.zhilblsb_id,DECODE(1,1,'��¯')leib, c.bianm,d.bbianm,max(d.querry) querry,decode(min(d.quersj),'',to_char(sysdate,'YYYY-MM-DD hh24:mi:ss'),to_char(min(d.quersj),'YYYY-MM-DD hh24:mi:ss')) quersj,'2' as quf,'��' huirdsf  from (\n"
						+ "select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm, r.quanc as mingc , to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm\n"
						+ "  from rulmzlzmxb rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = rb.zhuanmbzllsb_id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "  -- and rb.shenhzt=0\n"
						+ "   and  zh.jib=2\n"
						+ "     and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and rb.rulrq between to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')-1 " +
						"and to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "   ) c,\n"
						+ "   (\n"
						+ "   select distinct yb.id, yb.zhilblsb_id,zmb.bianm as bianm,rb.querry,rb.quersj, r.quanc as mingc, to_char(yb.zhiysj,'YYYY-MM-DD hh24:mi:ss') zhiysj,zmb.bianm as bbianm\n"
						+ "  from rulmzlzmxb rb, yangpdhb yb, zhuanmb zmb,zhuanmlb zh,zhiyryglb zlb,renyxxb r\n"
						+ " where yb.zhilblsb_id = rb.zhuanmbzllsb_id\n"
						+ "\n"
						+ "   and yb.zhilblsb_id=zmb.zhillsb_id\n"
						+ "   and zmb.zhuanmlb_id=zh.id\n"
//						+ "   --and rb.shenhzt=0\n"
						+ "   and  zh.jib=3\n"
						+ "     and zlb.renyxxb_id=r.id(+)\n"
						+ "    and yb.id=zlb.yangpdhb_id(+)\n"
						+ "   and rb.rulrq between to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')-1\n " +
						"and to_date('"+this.getRiq1()+" "+""+shijd+":00:00"+"','YYYY-MM-DD hh24:mi:ss')\n"
						+ "   ) d\n" + "   where c.id=d.id group by (c.id,c.zhilblsb_id,c.bianm,d.bianm)\n" 
						+ "\n"

						+ "");
		
		// �����û�У���ʾ��һ��
		// �����û�У�������ʾ��

		ResultSetList rsl = con.getResultSetList(sb1.toString());
//		ResultSetList rsl1;
		
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
		// ����ҳ����
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
		
		egu.getColumn("leib").setCenterHeader("�������");
		egu.getColumn("leib").setWidth(60);
		egu.getColumn("leib").setEditor(null);
		
		egu.getColumn("bbianm").setCenterHeader("�������");
		egu.getColumn("bbianm").setWidth(120);
		if(visit.getboolean4()){
			//�������true����ô���ֶ�
		}else{
			//�Զ�
			egu.getColumn("bbianm").setEditor(null);
		}
//		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("querry").setCenterHeader("ȷ����Ա");
		egu.getColumn("querry").setWidth(150);
		egu.getColumn("querry").setEditor(null);
		egu.getColumn("quersj").setCenterHeader("ȷ��ʱ��");
		egu.getColumn("quersj").setWidth(150);
		egu.getColumn("quf").setCenterHeader("����");
		egu.getColumn("quf").setWidth(150);
		egu.getColumn("quf").setEditor(null);
		egu.getColumn("quf").setHidden(true);
		
		
		egu.getColumn("bianm").setCenterHeader("��������");
		egu.getColumn("bianm").setWidth(120);
		egu.getColumn("bianm").setEditor(null);
		
		// ���÷������ں͵������ڵ�Ĭ��ֵ
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("quersj").setEditor(datetime);
		egu.getColumn("quersj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		
		egu.getColumn("huirdsf").setHeader("���۵���");//Center
		egu.getColumn("huirdsf").setWidth(90);
		egu.getColumn("huirdsf").setDefaultValue("��");
		ComboBox c1 = new ComboBox();
		egu.getColumn("huirdsf").setEditor(c1);
		c1.setEditable(true);
		String MeicbSql1="select  1 as id, decode(0,0,'��') as mingc from dual union select 2 as id,decode(0,0,'��') as mingc from dual ";
		egu.getColumn("huirdsf").setComboEditor(egu.gridId, new IDropDownModel(MeicbSql1));
		
		
		egu.addTbarText("����ʱ��");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");

		GridButton gb = new GridButton("����ȷ��", "function(){ "
				+
				" var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ZHILBLSB_ID');"//zhilblsb_id
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;  if(id==0||id=='0'){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ����һ����ӵļ�¼'); return ; }}"
				+ " if(rec == null){"
				+ "  Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ϣ��¼'); return ;"
				+ " }"+
				" var gridDivsave_history = '';\n" +
				" var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n" + //gridDiv_ds.getModifiedRecords();
				"for(i = 0; i< Mrcd.length; i++){\n" + 
				"if(typeof(gridDiv_save)=='function'){\n" + 
				" var revalue = gridDiv_save(Mrcd[i]);\n" + 
				"  if(revalue=='return'){return;}else if(revalue=='continue'){continue;}\n" + 
				"  }\n" + 
				"if(Mrcd.length>0 && Mrcd[i].get('QUERSJ') == ''){\n" + 
				"Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� <center>ȷ��ʱ��</center> ����Ϊ��');return;\n" + 
				"}\n" + 
				"gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
				"+ '<ZHILBLSB_ID update=\"true\">' + Mrcd[i].get('ZHILBLSB_ID')+ '</ZHILBLSB_ID>'\n" + 
				"+ '<BIANM update=\"true\">' + Mrcd[i].get('BIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BIANM>'\n" + 
				"+ '<BBIANM update=\"true\">' + Mrcd[i].get('BBIANM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BBIANM>'\n" + 
				"+ '<QUERRY update=\"true\">' + Mrcd[i].get('QUERRY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUERRY>'\n" + 
				"+ '<QUERSJ update=\"true\">' + Mrcd[i].get('QUERSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUERSJ>'\n" + 
				"+ '<HUIRDSF update=\"true\">' + Mrcd[i].get('HUIRDSF').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HUIRDSF>'\n" + 
				"+ '<QUF update=\"true\">' + Mrcd[i].get('QUF').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</QUF>'\n" + 
				" + '</result>' ;\n" + 
				"  }"+

				"    if(gridDiv_history=='' && gridDivsave_history=='' ){\n" + //&& rec == null
				"    Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');\n" + 
				"\t\t\t}else{\n" + 
				"\t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
				"\t\t\t\tCobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" + 
				"\t\t\t\tdocument.getElementById('SaveButton').click();\n" + 
				"\t\t\t\tExt.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"\t\t\t}\n" + 
				"\t}"

				+ " ");
		egu.addTbarBtn(gb);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
//		���ò��ɱ༭������
		String Scrpit="gridDiv_grid.on('beforeedit',function(e){\n"+
		"if(e.field=='HUIRDSF'&& e.record.get('QUERRY').length>0){e.cancel=true;}\n"+
		"if(e.field=='HUIRDSF'&& e.record.get('QUF')!='1'){e.cancel=true;}\n"+
		"});\n";
//		������ɫ
		String changeColor=" var girdcount=0;\n"+
			" gridDiv_ds.each(function(r){\n"+
			"     if(r.get('QUERRY').length>0){\n"+
			"         gridDiv_grid.getView().getCell(girdcount, 8).style.backgroundColor='#E3E3E3';\n"+
			"     }\n"+
			"     girdcount=girdcount+1;\n"+
			" });\n";
		egu.addOtherScript(Scrpit+changeColor);

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
//		Visit v=(Visit)this.getPage().getVisit();	
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
//		String sql_yunsfs = "";
		String sql="";
//		if(HAIY_SH.equals(getLeix())){
			
		sql="select id,mingc from rulbzb where diancxxb_id="+visit.getDiancxxb_id();
 
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			setYundxxModel(null);	
			getYundxxModels();	
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
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			visit.setString1(null);
		}
		if(visit.getboolean2()){
			visit.setboolean2(false);
		}
		if(visit.getboolean3()){
		   visit.setboolean3(false);
		   setYundxxValue(null);		//4
		   setYundxxModel(null);
		   visit.setString1(null);
		}
		visit.setboolean4(false);
		initNavigation();
		getSelectData();
	}
}
