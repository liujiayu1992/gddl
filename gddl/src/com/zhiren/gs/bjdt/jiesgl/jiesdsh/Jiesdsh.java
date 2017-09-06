package com.zhiren.gs.bjdt.jiesgl.jiesdsh;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.webservice.dtgsinterface.DBconn;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Liuc;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;

public class Jiesdsh extends BasePage {
	// ����״̬�����
	private static long WODRW = 1;// �ո��ύδѡ�����̵�����

	private static long LIUCZ = 2;// δ�������

	private static long YISH = 3;// �����

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _SbChick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbChick = true;
	}

	private boolean _PassChick = false;

	public void PassButton(IRequestCycle cycle) {
		_PassChick = true;
	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}

	private boolean _Huitui = false;

	public void Huitui(IRequestCycle cycle) {
		_Huitui = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SbChick) {
			_SbChick = false;
//			System.out.println(this.getLiucfsSelectValue().getValue());
//			if (this.getLiucfsSelectValue().getValue().equals("��˾���")) {
				Tij();
//			} else  {
//				Pass();
//			}
			getSelectData();
		}
		if (shuax) {
			shuax = false;
			// Pass();
			getSelectData();
		}
		if (_RbChick) {
			_RbChick = false;
			Huit();
			getSelectData();
		}
		if (_Huitui) {
			_Huitui = false;
			Huitui();
			getSelectData();
		}
		//���ͨ��
		if(_PassChick){
			_PassChick=false;
			Pass();
			getSelectData();
		}
	}

	private void Huit() {// ���˵糧,������ɾ��
		// TODO �Զ����ɷ������
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();

		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");
			for (int i = 0; i < change.length; i++) {

				if (change[i] == null || "".equals(change[i])) {
					continue;
				}

				String record[] = change[i].split(",");
				if (record.length > 2) {
					// Liuc.huit(record[1], Long.parseLong(record[0]),
					// renyxxb_id, "");
					try {
						ILiuc.getDelete(Long.parseLong(record[0]));
					} catch (NumberFormatException e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					} catch (Exception e) {
						// TODO �Զ����� catch ��
						this.setMsg(e.getMessage());
					}
				}
			}
		}
	}

	public void Tij() {

		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();

		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");

			for (int i = 0; i < change.length; i++) {

				if (change[i] == null || "".equals(change[i])) {

					continue;
				}
				String record[] = change[i].split(",");
				if (record.length > 2) {

					// UpdatejsdZt(Long.parseLong(record[0]));

					try {
						if (getWeizSelectValue().getId() == WODRW) {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,this.getLiucfsSelectValue()
											.getId(),false);
						} else {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,false, "");
						}
					} catch (NumberFormatException e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					} catch (Exception e) {
						//֪ͨ�û��쳣
						this.setMsg(e.getMessage());
					}
				}
			}
		}
	}
//
	public void Pass() {//ֱ�����ͨ��

		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();

		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");

			for (int i = 0; i < change.length; i++) {

				if (change[i] == null || "".equals(change[i])) {

					continue;
				}

				String record[] = change[i].split(",");

				if (record.length > 2) {

					// if(Passjiesd(Long.parseLong(record[0])))
					// {
					try {
						if (getWeizSelectValue().getId() == WODRW) {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,this.getLiucfsSelectValue()
											.getId(),true);
						} else {
							ILiuc.tij(record[1], Long.parseLong(record[0]),
									renyxxb_id,true, "");
						}
						
					} catch (NumberFormatException e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					} catch (Exception e) {
						// TODO �Զ����� catch ��
						this.setMsg(e.getMessage());
					}
					// }
				}

			}

		}
	}

	// ��pass��������
//
//	public void getDelete(long diancjsmkb_id) {
//
//		JDBCcon con = new JDBCcon();
//		con.setAutoCommit(false);
//		//ͬ���糧
//		 String sqli="select bianm,diancxxb_id from diancjsmkb where id="+diancjsmkb_id+"\n";
//		ResultSetList sr =con.getResultSetList(sqli);
//		 
//		 if(!ILiuc.DiancUpdate(sr.getLong("diancxxb_id"),sr.getString("bianm"), 1,-1)){ 
//			 this.setMsg("ͬ���糧ʱ�����ݿ���ִ�������ϵ����Ա");
//			 return;
//		 }
//	    int  result1 =-1;
//		String sql = "call deleteDiancJsd(" + diancjsmkb_id + ")";
//		System.out.println(sql);
//		result1 = con.getUpdate(sql);
//
//		if (result1>=0) {
//			con.commit();
//			System.out.println("���㵥ID=" + diancjsmkb_id + "  �����ɹ���"
//					+ DateUtil.FormatDateTime(new Date()));
//			setMsg("���˵糧�����ɹ���");
//		} else {
//			con.rollBack();
//			System.out.println("���㵥ID=" + diancjsmkb_id + "  ����ʧ�ܣ�"
//					+ DateUtil.FormatDateTime(new Date()));
//			setMsg("���˵糧����ʧ�ܣ�");
//		}
//	
//		con.Close();
//	}

	// private void UpdatejsdZt(long jiesdid){
	//			
	// JDBCcon con = new JDBCcon();
	// con.setAutoCommit(false);
	// String sql = "";
	// int result = -1;
	// try{
	// sql="update diancjsmkb set shenhjb=11,zhuangt=1,zhijzxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',zhijzxjbrq="+OraDate(new Date())+"
	// where id="+jiesdid;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("���㵥ID="+jiesdid+"
	// ״̬����ʧ�ܣ�"+DateUtil.FormatDateTime(new Date()));
	// setMsg("���㵥�ύ�쵼ʧ�ܣ�");
	// return;
	// }
	// result = -1;
	// sql="update diancjsyf set shenhjb=11,shenhzt=1,zhijzxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',zhijzxjbrq="+OraDate(new Date())+"
	// where id="+jiesdid;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("���㵥ID="+jiesdid+"
	// ״̬����ʧ�ܣ�"+DateUtil.FormatDateTime(new Date()));
	// setMsg("�˷ѽ��㵥�ύ�쵼ʧ�ܣ�");
	// return;
	// }
	// con.commit();
	// System.out.println("���㵥ID="+jiesdid+" �����ɹ���"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("�ύ�쵼�����ɹ���");
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// con.Close();
	// }
	// }

	// private boolean Passjiesd(long jiesdid){
	//			
	// JDBCcon con = new JDBCcon();
	// ResultSet rs;
	// boolean bln = false;
	// try{
	// String sql2 = "select js.diancxxb_id,js.bianh,js.fapbh,d.diancssgszt from
	// diancjsmkb js,diancxxb d where js.diancxxb_id=d.id and js.id="+jiesdid;
	// rs = con.getResultSet(sql2);
	// if(rs.next()){
	//					
	// bln = UpdateJsdzt(con, rs, jiesdid);
	//			
	// }else{//�˷ѽ��㵥
	// sql2 = "select js.diancxxb_id,js.bianh,js.fapbh,d.diancssgszt from
	// diancjsyf js,diancxxb d where js.diancxxb_id=d.id and js.id="+jiesdid;
	// rs = con.getResultSet(sql2);
	// if(rs.next()){
	// bln = UpdateJsdzt(con, rs, jiesdid);
	// }
	// }
	//						
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// con.Close();
	// }
	// if(bln){
	// System.out.println("���㵥ID="+jiesdid+" �����ɹ���"+DateUtil.FormatDateTime(new
	// Date()));
	// return true;
	// }else{
	// System.out.println("���㵥ID="+jiesdid+" ����ʧ�ܣ�"+DateUtil.FormatDateTime(new
	// Date()));
	// return false;
	// }
	// }

	// private boolean UpdateJsdzt(JDBCcon con, ResultSet rs,long jiesdID){
	// String sql = "";
	// int result = -1;
//	 final int dcshenhjb = 4;
//	 final int dczhuangt = 1;
	// try {
	// // if(rs.getInt("diancssgszt")==1){//���ڹ�˾�����糧
	// if(!
//	 DiancUpdate(rs.getLong("diancxxb_id"),rs.getString("bianh"),dcshenhjb,dczhuangt)){
	//					
	// con.rollBack();
	// System.out.println("���㵥"+rs.getString("bianh")+"����ʧ�ܣ�"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("������������ݿ���ϣ����㵥���ʧ�ܣ�");
	// return false;
	// }else{
	// sql="update diancjsmkb set shenhjb=15,zhuangt=1,jieszxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',gongsshrq="+OraDate(new Date())+"
	// where id="+jiesdID;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("���㵥"+rs.getString("bianh")+"״̬����ʧ�ܣ�"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("���㵥״̬����ʧ�ܣ�");
	// return false;
	// }
	// result = -1;
	// sql="update diancjsyf set shenhjb=15,shenhzt=1,jieszxjbr='"+((Visit)
	// getPage().getVisit()).getRenymc()+"',gongsshrq="+OraDate(new Date())+"
	// where id="+jiesdID;
	// result = con.getUpdate(sql);
	// if(result<0){
	// con.rollBack();
	// System.out.println("���㵥"+rs.getString("bianh")+"״̬����ʧ�ܣ�"+DateUtil.FormatDateTime(new
	// Date()));
	// setMsg("���㵥״̬����ʧ�ܣ�");
	// return false;
	// }
	// }
	// // }
	// con.commit();
	// setMsg("���㵥��˳ɹ���");
	// } catch (SQLException e) {
	// // TODO �Զ����� catch ��
	// e.printStackTrace();
	// return false;
	// }finally{
	// }
	// return true;
	// }
//���ͨ�� 4,1    ɾ���� 1,-1
//	 private boolean DiancUpdate(long diancID,String Jiesdh,int shenhjb,int zhuangt){
//				
//	 JDBCcon con = new JDBCcon();
//	 ResultSet rs;
//	 String sql = "";
//	 String mhostname = "";
//	 String msid = "";
//	 String mduank = "";
//	 String myonghm = "";
//	 String mmim = "";
//	 String mjiesb = "";
//	 String mjiesbhzd = "";
//	 String mpinysy = "";
//	 String jiesb[]={};
////	���ӣ���Ӫ     ��ɽ
//	 if(diancID==82 || diancID==102){
//		 diancID = 211;
//	 }
//	 sql = "select pz.*,dc.pinysy from diancxtpz pz,diancxxb dc where pz.jitdcid=dc.id and pz.jitdcid="+diancID;
//			
//	 try{
//	 rs = con.getResultSet(sql);
//	 if(rs.next()){
//	 mhostname = rs.getString("ip");
//	 msid = rs.getString("sid");
//	 mduank = rs.getString("duank");
//	 myonghm = rs.getString("yonghm");
//	 mmim = rs.getString("mim");
//	 mjiesb = rs.getString("jiesb");
//	 jiesb = mjiesb.split(",");
//	 mjiesbhzd = rs.getString("jiesbhzd");
//	 mpinysy = rs.getString("pinysy");
//	 }
//	 String bianh = Jiesdh.replaceAll(mpinysy, "");
//	 if(diancID==26){
//	 bianh = bianh.replaceAll("BH", "");
//	 bianh = bianh.replaceAll("LY", "");
//	 }
//	 DBconn dbcn=new DBconn(mhostname,mduank,msid);
//	 dbcn.setAutoCommit(false);
//	 dbcn.setUserName(myonghm);
//	 dbcn.setPassword(mmim);
//	 for(int t=0;t<jiesb.length;t++){
//		 int result=-1;
//		
//	//	 String dcsql1 = "update "+jiesb[t]+" set shenhjb="+shenhjb+",shenhzt="+zhuangt+" where "+mjiesbhzd+"='"+bianh+"'";
//	//	 result = dbcn.getUpdate(dcsql1);
//					    	
//		 if(result<0){
//			 dbcn.rollBack();
//			 dbcn.close();
//			 return false;
//		 }
//	 }
//	 dbcn.commit();
//	 dbcn.close();
//	 }catch(Exception e){
//	 e.printStackTrace();
//	 }finally{
//	 con.Close();
//	 }
//	 return true;
//	 }

	// ��ѯ���ݲ��γɽ���
	public void getSelectData() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		String leix = "��˾����";
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		String strWeizhitj = "";
		String liucztb_id="";
		if (getWeizSelectValue().getId() == WODRW) {// �õ��ոմӵ糧�ϴ��ģ���δѡ�����̵���Ŀ
			strWeizhitj = ILiuc.getWeixz("diancjsmkb", renyxxb_id, leix);
		} else if (getWeizSelectValue().getId() == LIUCZ) {// �õ�δ��˵�����
			strWeizhitj = ILiuc.getWeish("diancjsmkb", renyxxb_id, leix);
		} else {// �õ�����˵�����
			liucztb_id = ILiuc.getYish("diancjsmkb", renyxxb_id, leix);
		}

		sql = "select * \n"
				+ "  from ("
				+ "  select js.id,'diancjsmkb' as tabname,di.id as diancid,di.mingc as dianmc,\n"
				+ "				  getHtmlAlert('"
				+ MainGlobal.getHomeContext(this)
				+ "','Showjsd','jiesdbh',js.bianm,js.bianm) as bianh,"
				+ "               --gy.jianc as gongysmc, \n"
				+ "               js.gongysmc as fahdw,nvl(he.hetbh,js.hetbh) hetbh,jiessl,guohl,nvl(jiesrl,0) jiesrl,nvl(changfrl,0) changfrl,nvl(jieslf,0) jiesliuf,nvl(changflf,0) changflf,\n"
				+ "				  hansmk,meikje,shuik,"
				+ "				  buhsdj danj,js.jiesrq, \n"
				+ "				  nvl(jiesrl,0)-nvl(changfrl,0) as rezc,(nvl(jiessl,0)-nvl(guohl,0))-round((nvl(guohl,0)*0.02)) as shulc,	\n"
				+ "               lz.mingc zhuangt,zt.liucb_id,zt.id as liucztb_id\n"
				+ "          from diancjsmkb js , \n"
				+ "		(\n"
				+ "select nvl(rel.jiesdid,quanl.jiesdid) jiesdid,nvl( changfrl,0) changfrl,nvl(jiesrl,0) jiesrl,nvl(changflf,0) changflf,nvl(jieslf,0) jieslf from\n"
				+ "(select ji.jiesdid  jiesdid, ji.changf changfrl,ji.jies jiesrl from  jieszbsjb ji  left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='�յ�����λ��ֵ') rel\n"
				+ "full outer join\n"
				+ "(select ji.jiesdid jiesdid, ji.changf changflf,ji.jies jieslf from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='�����ȫ��') quanl\n"
				+ "on (rel.jiesdid=quanl.jiesdid)\n"
				+ "      )chengf \n"
				+ "  , gongysb gon \n"
				+ "  , hetb he \n"
				+ "  , liucztb zt \n"
				+ "  , leibztb lz \n"
				+ "  , liuclbb ll \n"
				+ "  , diancxxb di \n";
				if(getWeizSelectValue().getId() == YISH)
				{sql+= "  where js.liucztb_id in(" + liucztb_id + ")  and chengf.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id\n" ;}
				else{
					sql+= "  where js.id in(" + strWeizhitj + ")  and chengf.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id\n" ;
				}
				sql+="and he.id(+)=js.hetb_id and zt.id(+)=js.liucztb_id and lz.id(+)=zt.leibztb_id and ll.id(+)=lz.liuclbb_id\n" +
						"and js.diancxxb_id=di.id(+)\n";
		
		         if(this.getWeizSelectValue().getId()==YISH){
		        	 sql+="and jiesrq>=to_date('"+this.getStartdate()+"','yyyy-mm-dd') and jiesrq<=to_date('"+this.getEnddate()+"','yyyy-mm-dd')\n";
		         }
				
				sql+="  )\n";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);// �趨��¼����Ӧ�ı�

		egu.setGridType(ExtGridUtil.Gridstyle_Read);

		egu.getColumn("id").setHidden(true);
		egu.getColumn("tabname").setHidden(true);
		egu.getColumn("diancid").setHidden(true);
		egu.getColumn("dianmc").setHeader("�糧����");
		egu.getColumn("bianh").setHeader("���㵥����");
		egu.getColumn("jiesrq").setHeader("��������");
		egu.getColumn("fahdw").setHeader("������λ");
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("danj").setHeader("����");
		egu.getColumn("jiessl").setHeader("��������");
		egu.getColumn("guohl").setHeader("��������");
		egu.getColumn("changfrl").setHeader("������ֵ");
		egu.getColumn("jiesrl").setHeader("������ֵ");
		egu.getColumn("changflf").setHeader("�������");
		egu.getColumn("jiesliuf").setHeader("�������");
		egu.getColumn("hansmk").setHeader("��˰���");
		egu.getColumn("meikje").setHeader("�ۿ���");
		egu.getColumn("shuik").setHeader("˰��");
		egu.getColumn("rezc").setHeader("��ֵ��");
		egu.getColumn("shulc").setHeader("������");
		egu.getColumn("zhuangt").setHeader("״̬");

		egu.getColumn("liucztb_id").setHidden(true);
		egu.getColumn("liucb_id").setHidden(true);
		egu.getColumn("rezc").setHidden(true);
		egu.getColumn("shulc").setHidden(true);

		egu.getColumn("id").setWidth(10);
		egu.getColumn("dianmc").setWidth(60);
		egu.getColumn("bianh").setWidth(100);
		// egu.getColumn("jieszb").setWidth(60);
		egu.getColumn("jiesrq").setWidth(70);
		// egu.getColumn("gongysmc").setWidth(90);
		egu.getColumn("fahdw").setWidth(100);
		egu.getColumn("hetbh").setWidth(90);
		egu.getColumn("danj").setWidth(75);
		egu.getColumn("jiessl").setWidth(60);
		egu.getColumn("guohl").setWidth(60);
		egu.getColumn("changfrl").setWidth(60);
		egu.getColumn("jiesrl").setWidth(60);
		egu.getColumn("changflf").setWidth(60);
		egu.getColumn("jiesliuf").setWidth(60);
		egu.getColumn("meikje").setWidth(90);
		egu.getColumn("meikje").setWidth(85);
		egu.getColumn("shuik").setWidth(80);
		egu.getColumn("rezc").setWidth(50);
		egu.getColumn("shulc").setWidth(50);
		egu.getColumn("zhuangt").setWidth(100);

		egu.setWidth(1000);

		egu
				.getColumn("jiessl")
				.setRenderer(
						"function(value,metadata,rec){if(rec.data['SHULC']>0) {metadata.css='tdTextext';} return value;} ");
		egu
				.getColumn("jiesrl")
				.setRenderer(
						"function(value,metadata,rec){if(rec.data['REZC']>200){metadata.css='tdTextext';} return value;}");

		// List tmp= new ArrayList();
		// for(int i=0;i<rsl.getRows();i++){
		// String strtmp="";
		// tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,0)));
		// for(int j=0;j<tmp.size();j++){
		// strtmp+=((Yijbean)tmp.get(j)).getXitts()+"\\n
		// "+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij())+"\\n
		// ";
		// }
		// egu.setDataValue(i, 13, "������ "+egu.getDataValue(i,1)+":\\n
		// "+strtmp);
		// }
		egu.getCheckPlugins();
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(22);

		egu.addTbarText("����״̬");
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(150);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
		egu.addToolbarItem(WeizSelect.getScript());
		egu
				.addOtherScript("Weizx.on('select',function(){Showwait(); document.forms[0].submit();});");
		egu
				.addOtherScript("gridDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");

		
		if(this.getWeizSelectValue().getId()==YISH){
			 DateField startdf=new DateField();
			 startdf.setValue(this.getStartdate());
			 startdf.Binding("startdate", "");
			 egu.addTbarText("���ڣ�");
			 egu.addToolbarItem(startdf.getScript());
			 egu.addTbarText("��");
			 DateField enddf=new DateField();
			 enddf.setValue(this.getEnddate());
			 enddf.Binding("enddate", "");
			 egu.addToolbarItem(enddf.getScript());
			 
				this.setYishzt(true);
	  String condition=MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+"document.getElementById('ShuaxinButton').click();";
		egu.addToolbarItem("{"+new GridButton("ˢ��","function(){"+condition+"}",SysConstant.Btn_Icon_Refurbish).getScript()+"}");				  
////			 GridButton gb=new GridButton("ˢ��","");
//			 gb.setTapestryBtnId("ShuaxinButton");
////			 gb.setButton(GridButton.ButtonType_Refresh);
//					
//			 gb.condition=MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";document.getElementById('ShuaxinButton').click();";
//			 egu.addTbarBtn(gb);
		}
		if (this.getWeizSelectValue().getId() == WODRW) {
			egu.addTbarText("���̷�ʽ��");
			ComboBox liucfs = new ComboBox();
			liucfs.setId("liucfs");
			liucfs.setWidth(80);
			liucfs.setLazyRender(true);
			liucfs.setTransform("liucfs");
			liucfs.setListeners("'change':function(){document.getElementById('liucfs').value=liucfs.getValue();}");
			egu.addToolbarItem(liucfs.getScript());
		}

		egu.addTbarText("-");
		egu.addToolbarItem("{"
				+ new GridButton(
						"���˵糧",
						"function(){ Ext.MessageBox.confirm('����', 'ȷ�������㵥���˵糧��', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
								+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
								+ " 	for(var i=0;i<rec.length;i++){ "
								+ " 		if(i==0){"
								+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
								+ " 		}else{ "
								+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
								+ " document.getElementById('YiSRbButton').click();"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";} else{	alert('��ѡ��һ�Ž��㵥!');}}")
						.getScript() + "})}} ");

		// if(! ((Visit) getPage().getVisit()).getUid().equals("bfg") && !
		// ((Visit) getPage().getVisit()).getUid().equals("zzg")){
		if (this.getWeizSelectValue().getId() != YISH) {
			egu.addTbarText("-");
			String tishi="ȷ���������ύ��";
			if(this.getWeizSelectValue().getId()==WODRW){
				tishi="ȷ�������㰴���� '+liucfs.getRawValue()+' �ύ��";
			}
			if(!ILiuc.isLastLeader(renyxxb_id)){
			egu
					.addToolbarItem("{"
							+ new GridButton(
									"�ύ�쵼",
									"function(){ Ext.MessageBox.confirm('����', '"+tishi+"', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){  "
											+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
											+ " 	for(var i=0;i<rec.length;i++){ "
											+ " 		if(i==0){"
											+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
											+ " 		}else{ "
											+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
											+ " document.getElementById('SbButton').click();"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";}else{	alert('��ѡ��һ�Ž��㵥!');}}")
									.getScript() + "})}}");

			 }

			 egu.addTbarText("-");
			 egu.addToolbarItem("{"
			 + new GridButton(
			 "���ͨ��",
			 "function(){ Ext.MessageBox.confirm('����', '"+tishi+"', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
			 + " rec = gridDiv_grid.getSelectionModel().getSelections(); "
			 + " for(var i=0;i<rec.length;i++){ "
			 + " if(i==0){"
			 + "document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
			 + " }else{ "
			 + "document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
			 + " document.getElementById('PassButton').click();"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";}else{alert('��ѡ��һ�Ž��㵥!');}}").getScript()+"})}}");
							
			egu.addTbarText("-");
		}

		// egu.addToolbarItem("{"
		// + new GridButton("������",
		// "function(){ "
		// + "if(gridDiv_sm.hasSelection()){ "
		// + " if(Weizx.getRawValue()=='������'){ "
		// + " document.getElementById('DivMy_opinion').className =
		// 'x-hidden';}"
		// + " window_panel.show(); "
		// + " rec = gridDiv_grid.getSelectionModel().getSelections(); "
		// + " document.getElementById('My_opinion').value='';"
		// + " document.getElementById('Histry_opinion').value='';"
		// + " var strmyp=''; "
		// + " for(var i=0;i<rec.length;i++){ "
		// + " if(strmyp.substring(rec[i].get('YIJ'))>-1){ "
		// + " if(strmyp==''){ strmyp=rec[i].get('YIJ');}else{
		// strmyp+=','+rec[i].get('YIJ');}}"
		// + " var strtmp=rec[i].get('HISTRYYJ');"
		// + " document.getElementById('Histry_opinion').value+=strtmp+'\\n';}
		// document.getElementById('My_opinion').value=strmyp;"
		// + " }else{ "
		// + " alert('��ѡ��һ�Ÿ��!');} " + "}")
		// .getScript() + "}");
		// egu.addTbarText("-");
		// //��Toolbar��combobox
		// egu.addTbarText("��������:");
		// ComboBox JieslxDropDown = new ComboBox();
		// JieslxDropDown.setId("JieslxDrop");
		// JieslxDropDown.setWidth(100);
		// JieslxDropDown.setLazyRender(true);
		// JieslxDropDown.setTransform("JieslxDropDown");
		// egu.addToolbarItem(JieslxDropDown.getScript());
		// //��
		// egu.addTbarText("-");
		// if(getJieslxValue().getId()!=3){
		// egu.addTbarText("��λ����:");
		// ExtTreeUtil etu = new
		// ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)getPage().getVisit()).getDiancxxbId(),getTreeid());
		//			
		// setTree(etu);
		// egu.addTbarTreeBtn("diancTree");
		// }
		// ��combobox���
		// ����ѡ����� λ�õ�ֵ������ˣ�������ѡ����Щ��ť
		if (this.getWeizSelectValue().getId() == LIUCZ) {
			this.setYishzt(true);
			
			egu
					.addToolbarItem("{"
							+ new GridButton(
									"����",
									"function(){ Ext.MessageBox.confirm('����', 'ȷ�������㵥������', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
											+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
											+ " 	for(var i=0;i<rec.length;i++){ "
											+ " 		if(i==0){"
											+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
											+ " 		}else{ "
											+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('TABNAME')+','+rec[i].get('BIANH')+','+rec[i].get('JIESRQ')+','+rec[i].get('DIANCID')+','+rec[i].get('HETBH')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
											+ " document.getElementById('Huitui').click();"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";} else{	alert('��ѡ��һ�Ž��㵥!');}}")
									.getScript() + "})}} ");

			// egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,
			// "Huitui","", SysConstant.Btn_Icon_Return);
			egu.addTbarText("-");

		}
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	// λ�������˵�--����״̬
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
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
		
		if(ILiuc.isFirstRenY(v.getRenyID())){
		list.add(new IDropDownBean(1, "δѡ�����̵�����"));
		}else{
		list.add(new IDropDownBean(2, "����˵�����"));
		}
		list.add(new IDropDownBean(3, "����˵�����(����������)"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}

	// ѡ�����̷�ʽ
	public IDropDownBean getLiucfsSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLiucfsSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLiucfsSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean6(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public void setLiucfsSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLiucfsSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getLiucfsSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getLiucfsSelectModels() {
	Visit v=(Visit)this.getPage().getVisit();	
	ArrayList al=new ArrayList();
	al=ILiuc.getIcanliuc(v.getRenyID());
	if(al.size()<1){
		JDBCcon jcon=new JDBCcon();
		ResultSetList rsl=jcon.getResultSetList("select * from liucb where mingc='��˾����'");
		ArrayList al2=new ArrayList();
		al2.add(new IDropDownBean(rsl.getLong("id"),rsl.getString("mingc")));
		al=al2;
		jcon.Close();
	}
	v
	.setProSelectionModel3(new IDropDownModel(al));
	}

	public boolean isQuanxkz() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setQuanxkz(boolean value) {
		((Visit) getPage().getVisit()).setboolean4(value);
	}

	public boolean isYishzt() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setYishzt(boolean value) {
		((Visit) getPage().getVisit()).setboolean5(value);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(-1);
			setExtGrid(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			getWeizSelectModel();
			setLiucfsSelectValue(null);
			setLiucfsSelectModel(null);
			getLiucfsSelectModel();
			visit.setboolean4(true);// �ҵ�����������
			visit.setboolean1(false);// λ��
			visit.setboolean2(false);// ��������
			visit.setboolean3(false);// ��λ
			visit.setboolean5(false);// �����
			getSelectData();
		}

		if (((Visit) getPage().getVisit()).getboolean1()
				|| ((Visit) getPage().getVisit()).getboolean2()
				|| ((Visit) getPage().getVisit()).getboolean3()) {// �����ͬλ�øı�
			// 1, λ��2, ��������3, ��λ
			if (((Visit) getPage().getVisit()).getboolean1() == true) {
				if (getWeizSelectValue().getId() == 1) {
					visit.setboolean4(true);
				} else {
					visit.setboolean4(false);
				}
			}
			visit.setboolean1(false);
			visit.setboolean2(false);
			visit.setboolean3(false);
			getSelectData();
		}
	}

	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}

	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}

	public void setEditTableRow(int value) {

		((Visit) this.getPage().getVisit()).setInt1(value);
	}

	// �־û��糧��
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	public String getWunScript() {

		return "for(var i=0;i<rec.length;i++){"

		+ "    rec[i].set('YIJ',document.getElementById('My_opinion').value);"
				+ " }";
	}

	// ��ѡ�������ʱ��
	// ˢ�¶���
	private boolean shuax = false;

	public void ShuaxinButton(IRequestCycle cycle) {
		shuax = true;

	}

	// ���˶���
	public void Huitui() {// ��һ��һ���Ļ���
	// ���˵糧
		// TODO �Զ����ɷ������
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		if (!(this.getChange().equals("") || this.getChange() == null)) {

			String change[] = this.getChange().split(";");
			for (int i = 0; i < change.length; i++) {
				if (change[i] == null || "".equals(change[i])) {
					continue;
				}

				String record[] = change[i].split(",");
				if (record.length > 2) {

					ILiuc.huit(record[1], Long.parseLong(record[0]),
							renyxxb_id, "");
					// getDelete(Long.parseLong(record[0]));
				}
			}
		}

	}

	// ��ʼ������ֵ
	private String startdate;

	public void setStartdate(String startdate) {

		this.startdate = startdate;
	}

	public String getStartdate() {
		if (startdate == null || startdate.equals("")) {
			Date today = new Date();
			today.setDate(today.getDate());
			this.setStartdate(DateUtil.FormatDate(today));
		}
		return this.startdate;
	}

	// ����������ֵ
	private String enddate;

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getEnddate() {
		if (enddate == null || enddate.equals("")) {
			this.setEnddate(DateUtil.FormatDate(new Date()));
		}
		return this.enddate;
	}
	// �õ�����˵�id�� ���� "2332,2323,23234,34334"
	// private String getYish(){
	// String ids="";
	// JDBCcon con=new JDBCcon();
	// String sql="select distinct id from diancjsmkb where liucztb_id=1 and
	// jiesrq>=to_date('"+this.getStartdate()+"','yyyy-mm-dd') and
	// jiesrq<=to_date('"+this.getEnddate()+"','yyyy-mm-dd')";
	//			
	// ResultSet rs=con.getResultSet(sql);
	// try{
	// while(rs.next()){
	// if(!ids.equals("")){
	// ids+=",";
	// }
	// ids+=rs.getString(1);
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// con.Close();
	// }
	// if(ids.equals("")){
	// ids="-1";
	// }
	// return ids;
	// }

}
