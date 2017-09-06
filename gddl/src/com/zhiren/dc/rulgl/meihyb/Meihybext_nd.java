package com.zhiren.dc.rulgl.meihyb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2012-10-31
 * ��������������bug������ʱ��Ӧ����DIANCXXB_ID
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-07-18
 * ��������������bug
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-04-17
 * ������ʹ�ò����ж��Ƿ���ʾ������ť��Ĭ��ֵΪ��(����ʾ)
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-06-09
 * �����������涯��ʱrulmzlb�е�meilû�д���ֵ��meil���볧��¯��ֵ�������Ȩֵ
 */
/*
 * ���ߣ�liuy
 * ʱ�䣺2010-04-26
 * ����������ʱrulmzlb��shenhzt�ֶ�Ĭ��Ϊ0
 */
/*
 * ���ߣ�liht
 * ʱ�䣺2010-03-31
 * �������޸�������־ʱ����ID����ȷ����ʧ�ܵ����⣻��rulmzlb������zhiyr�������ˣ��ֶ�
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-01-16
 * �������޸ĺ����ȵ��ȡ�����������л���ͣ���������Ϊnull �������㲻��ȷ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-24
 * �������޸���ͷ�糧ȡ�������� ���Ҷ����ݽ���ȡ��
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:���밴ť�����в���zbbh��Ϊ�ж�����
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:���밴ť�з����иı�ȡ��������������
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:���밴ť�з����г�ȥ�жϱ�����������
 */
/*
 * ����:tzf
 * ʱ��:2009-10-26
 * �޸�����:���밴ť����Ϊ���ݿ��ַ�����Ĳ�һ�����ֵ���jhgl_rlsj������ct_zbbhȥ���жϡ�
 */
/*
 * ����:tzf
 * ʱ��:2009-10-20
 * �޸�����:���ӵ��밴ť
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-20
 * �������޸ĺ����ȵ����ݿ�ȡ��ʱδ��rulmzlb.meil�ֶ�����볧��¯��ֵ���ѯ������������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-22
 * ����������ú����ȡ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-30 11��35
 * ������������¯ú���������Զ�����
 * 		insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * 		values(23288902,1,229,'��¯ú�������ܼ���','��','','��¯',1,'')
 */
/*
 * ����
 * 2009-05-13
 * �糧����TreeIdδ��ʼ����BUG
 */
/*
 * ly
 * �޸�ʱ�䣺2009-04-27
 * �޸����ݣ����ӵ糧Tree
 */
/*
 * 2009-05-18
 * ����
 * �޸��Զ������պĴ�ʱ����ʱ������Ĵ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-02 14��13
 * �������޸ĵ糧���޸ĵ糧ʱˢ��ҳ��û�����¼������ѡ��糧���ɹ�������
 */
/*
 * ���ߣ�zsj
 * ʱ�䣺2010-04-02
 * �������޸���ͷ�ĵ��빦�ܣ�
 *				  1.����ʱ��Ϊ�����������9#,��10#��,����������ĺ������ݵ�������.Ŀǰ����Ҫ�����������������ݶ�������
                  2.����ĺ������ݶ�Ҫ����2λС��.
 */

/*
 *���ߣ�ww
 *ʱ�䣺2010-06-03 
 *����: �޸�һ�������·ֳ���ѡ����¯����
 *		
 *	   ��Ӳ������ã��жϱ���ʱ���ú����������Ӧ����¯������Ϣ�򲻸���rulmzlb
 */
/*
 * ���ߣ���衻�
 * ʱ�䣺2010��6��7
 * �������޸�shous()�����жԳ�����Ϊ0�Ĵ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2013��3��14
 * �������޸ı��淽����
 */
/*
 * ���ߣ����
 * ʱ�䣺2013��6��27
 * ������ʹ�ô������"N"�ж��Ƿ�ɱ༭��ɾ��������
 */
public class Meihybext_nd extends BasePage implements PageValidateListener {
//	�ͻ��˵���Ϣ��
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
//	ҳ���ʼ��(ÿ��ˢ�¶�ִ��)
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	// ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	 ���ڿؼ�
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			JDBCcon con = new JDBCcon();
			int zhi=0;
			String sql = "select zhi from xitxxb where  leib='��¯' and mingc ='ú����Ĭ������' and zhuangt =1 ";
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
			   zhi=rsl.getInt("zhi");	
			}
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),+zhi,DateUtil.AddType_intDay));
			con.Close();
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {

		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);

		StringBuffer sb = new StringBuffer();
		StringBuffer sbdl = new StringBuffer();

		// ɾ������
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		//�ȼ���ж�ɾ����¯ú��������
		
		if (delrsl.getRows() != 0) {
			sbdl.append("begin\n");
			while (delrsl.next()) {
				String id = delrsl.getString("id");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,"meihyb",id);
				sbdl.append(" delete from ").append("meihyb").append(" where id =").append(delrsl.getString("id")).append(";\n");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,"rulmzlb",delrsl.getString("rulmzlb_id"));
				sbdl.append(" delete from ").append("rulmzlb").append(" where id =").append(delrsl.getString("rulmzlb_id")).append(";\n");
				//�ж���¯ú�������Ƿ��Ѿ����ڻ������ݣ�������ڲ���ɾ��

				String sql="select qnet_ar\n" +
							"from rulmzlb\n" + 
							"where id="+delrsl.getString("rulmzlb_id");
				ResultSet rs=con.getResultSet(sql);
				try {
					if(rs.next()){
						double qnet_ar= rs.getDouble("qnet_ar");
						if(qnet_ar!=0){//���ڻ���ֵ
							setMsg("��¯����ֵ����ֵ�Ѿ����ڣ�����ɾ��ֻ���޸ġ�");
							return;
						}
					}
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
			}
			sbdl.append("end;");
			con.getDelete(sbdl.toString());
			con.commit();
		}

		// ��������
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
							+ "Meihybext.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}

		if (rsl.getRows() == 0) {
			return;
		}
//		String riq1 = getRiqi();
		long diancxxb_id = 0;

		sb.append("begin\n");
		String rulrq = "";
		String strDate = "";
		
		//���������Ƿ��Զ�����¯���ڴ�1��
		boolean StrFenxrq=false;
		ResultSetList Riqrsl=con.getResultSetList("select zhi from xitxxb x where x.mingc='��¯������������Ƿ����¯���ڴ�1��' and leib='��¯' and x.zhuangt=1");
		if(Riqrsl.next()){
			StrFenxrq=true;
		}else{
			StrFenxrq=false;
		}
		Riqrsl.close();
		
		while (rsl.next()) {
			diancxxb_id = Long.parseLong(getTreeid());//visit.getDiancxxb_id();
			String rulmzlb_id = MainGlobal.getNewID(diancxxb_id);// �õ�rulmzlb_id

			String fenxrq ="";
			if(StrFenxrq){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try{
					Date Date_riq=DateUtil.AddDate(sdf.parse(rsl.getString("rulrq")),1,DateUtil.AddType_intDay);
					String Str_riq=DateUtil.FormatDate(Date_riq);
					fenxrq= DateUtil.FormatOracleDate(Str_riq);
				}catch(Exception  e){
					
				}
			}else{
				fenxrq= DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			}
			
			long rulbzb_id=(getExtGrid().getColumn("rulbzb_id").combo).getBeanId(rsl.getString("rulbzb_id"));
			long jizfzb_id=(getExtGrid().getColumn("jizfzb_id").combo).getBeanId(rsl.getString("jizfzb_id"));
			rulrq = DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			strDate = rsl.getString("rulrq");
			
			//�ж���������Ѿ�¼�벻����rulmzlb
			boolean isHasHy = false;
			String sql = " select * from xitxxb where mingc='��¯ú�����Ƿ���' and leib='��¯' and zhuangt=1 and zhi='��' ";
			ResultSetList rss=con.getResultSetList(sql);
			if(rss.next()){
				isHasHy = con.getHasIt("select * from rulmzlb where rulrq=to_date('" 
						+ rsl.getString("RULRQ") + "','yyyy-mm-dd') and rulbzb_id=" + rulbzb_id + " and jizfzb_id=" + jizfzb_id);
			}

			if ("0".equals(rsl.getString("id"))) {
				
				if (!isHasHy) {
					sb.append("insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,meil,rulrq,lursj,shenhzt) values (\n")
					.append(rulmzlb_id).append(",").append(diancxxb_id)
					.append(",").append(fenxrq).append(",").append(rulbzb_id).append(",").append(jizfzb_id)
					.append(",").append(rsl.getDouble("FADHY")+rsl.getDouble("GONGRHY")+rsl.getDouble("QITY"))
					.append(",").append(rulrq).append(",").append(rulrq)
					.append(",0);\n");
				}
				sb.append("insert into meihyb \n");
				sb.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
				sb.append("values (getnewid(").append(diancxxb_id).append("),to_date('");
				sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),").append(diancxxb_id).append(",");
				sb.append(rulmzlb_id).append(",").append(rulbzb_id).append(",").append(jizfzb_id);
				sb.append(",").append(rsl.getString("FADHY")).append(",").append(rsl.getString("GONGRHY"));
				sb.append(",").append(rsl.getString("QITY")).append(",").append(rsl.getString("FEISCY"));
				sb.append(",'").append(rsl.getString("BEIZ")).append("','").append(rsl.getString("LURY"));
				sb.append("',to_date('").append(rsl.getString("LURSJ"))
				  .append("','yyyy-mm-dd'),'").append(rsl.getString("zhiyr")).append("',");
				sb.append(rsl.getString("SHENHZT")).append("); \n");

			} else {
				String id = rsl.getString("id");
				//�޸�ʱ������־
				MainGlobal.LogOperation(con,diancxxb_id,visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"rulmzlb",rsl.getString("rulmzlb_id"));
				
				if (!isHasHy) {
					sb.append("update rulmzlb set fenxrq=").append(fenxrq).append(",rulbzb_id=").append(rulbzb_id);
					sb.append(",meil="+(rsl.getDouble("FADHY")+rsl.getDouble("GONGRHY")+rsl.getDouble("QITY")) + "\n");
					sb.append(",jizfzb_id=").append(jizfzb_id).append(",rulrq=")
					  .append(rulrq).append(",lursj=")
					  .append(rulrq).append(",")
					  .append("shenhzt=0").append(" where id=");
					sb.append(rsl.getString("rulmzlb_id")).append(";\n");
				}

				//�޸�ʱ������־
				MainGlobal.LogOperation(con,diancxxb_id,visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"meihyb",id);
				sb.append("update meihyb set RULRQ=to_date('"+rsl.getString("RULRQ")+"','yyyy-mm-dd'),\n");
				sb.append(" RULMZLB_ID="+rsl.getString("RULMZLB_ID")).append(",\n");
				sb.append(" RULBZB_ID="+rulbzb_id+",\n");
				sb.append(" JIZFZB_ID="+jizfzb_id+",\n");
				sb.append(" FADHY="+rsl.getString("FADHY")+",\n");
				sb.append(" GONGRHY="+rsl.getString("GONGRHY")+",\n")
				  .append(" QITY="+rsl.getString("QITY")+",\n")
				  .append(" FEISCY="+rsl.getString("FEISCY")+",\n")
				  .append(" BEIZ='"+rsl.getString("BEIZ")+"',\n")
				  .append(" LURY='"+rsl.getString("LURY")+"',\n" )
			      .append(" LURSJ=to_date('"+rsl.getString("LURSJ")+"','yyyy-mm-dd')\n");
				sb.append(" where id=").append(rsl.getString("id")).append(";\n");

			}
		}
		sb.append("end;");
		con.getInsert(sb.toString());
		String sql = "select * from xitxxb where zhi ='��' and leib='��¯' and mingc ='��¯ú�������ܼ���' and zhuangt =1 ";
		if(con.getHasIt(sql)){
			Compute.UpdateRulmzl(con, DateUtil.FormatOracleDate(strDate), String.valueOf(diancxxb_id));
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);

	}
	
	private void Tij(){
		JDBCcon con = new JDBCcon();
		String sql="update meihyb set SHENHZT=5 where id="+getChange();
		int flag=con.getUpdate(sql);
		if(flag<0){
			setMsg("�ύʧ��");
		}else{
			setMsg("�ύ�ɹ�");
		}
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _TijClick = false;
	public void TijButton(IRequestCycle cycle){
		_TijClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		if(_TijClick){
			_TijClick=false;
//			System.out.println(getChange());
			Tij();
		}
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rulrq = DateUtil.FormatOracleDate(this.getRiqi());	//��¯���� 
		String DefaultShzt = "0";		//Ĭ�����״̬  �ڲ�����ϵͳ���õ�ʱ��Ĭ��Ϊ�����
		String Str = "(m.shenhzt = 0 or m.shenhzt = 5)";	//SQL�������  ֻ�г���Ӧ���״̬������
		//�糧Treeˢ������
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			if(visit.isFencb()){
				diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
			}else{
				diancxxb_id = "and (d.id = " + this.getTreeid() + ")\n";
			}
		}
		
		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,m.zhiyr,m.beiz,m.lury,m.lursj,m.shenhzt\n"
				+ "  from meihyb m, diancxxb d, rulmzlb r, rulbzb rb, jizfzb j\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulmzlb_id = r.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+ "   and m.rulrq = "+ rulrq + "\n"
				+ diancxxb_id
				+ "   and " + Str
		        +"  order by m.rulrq,rb.xuh,j.xuh";

		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("��������");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setWidth(110);
		egu.getColumn("fadhy").setHeader("�������(��)");
		egu.getColumn("gongrhy").setHeader("���Ⱥ���(��)");
		egu.getColumn("qity").setHeader("������(��)");
		egu.getColumn("feiscy").setHeader("��������(��)");
		egu.getColumn("zhiyr").setHeader("������");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lursj").setHeader("¼��ʱ��");
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("shenhzt").setHeader("״̬");
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + visit.getDiancxxb_id());
		egu.getColumn("rulrq").setDefaultValue(this.getRiqi());
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("rulmzlb_id").setDefaultValue("0");
		egu.getColumn("fadhy").setDefaultValue("0");
		egu.getColumn("gongrhy").setDefaultValue("0");
		egu.getColumn("qity").setDefaultValue("0");
		egu.getColumn("feiscy").setDefaultValue("0");
		egu.getColumn("shenhzt").setDefaultValue(DefaultShzt);

		// ������������¯����
		ComboBox cb_banz = new ComboBox();
		egu.getColumn("rulbzb_id").setEditor(cb_banz);
		cb_banz.setEditable(true);
		String rulbzb_idSql = "select r.id,r.mingc from rulbzb r,diancxxb d  where r.diancxxb_id=d.id(+)"
			+ "and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		egu.getColumn("rulbzb_id").setReturnId(true);
		// ������������¯����
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) " +
				"and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by j.xuh";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));
		egu.getColumn("jizfzb_id").setReturnId(true);
		
        // ����������������
		ComboBox zhiyr = new ComboBox();
		egu.getColumn("zhiyr").setEditor(zhiyr);
		zhiyr.setEditable(true);
		String cb_zhiyr = "select id, quanc from renyxxb where bum = '����'";
		egu.getColumn("zhiyr").setComboEditor(egu.gridId,
				new IDropDownModel(cb_zhiyr));
		egu.getColumn("zhiyr").editor.setAllowBlank(true);

		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("�糧��");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// ************************************************************
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){"
				+ "if(e.record.get('SHENHZT')=='5'){"
				+ " e.cancel=true;" + "}" + "});");

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		GridButton daor=new GridButton("�ύ","" +
				"function(){ \n" +
				"if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1)\n" +
				"{Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼');return;}\n" +
				"grid1_rcd = gridDiv_sm.getSelections()[0];\n" +
				"grid1_history = grid1_rcd.get('ID');\n" +
				"if(grid1_history=='0'){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ�������ύ');return;} \n" +
				"if(grid1_rcd.get('SHENHZT')=='5'){Ext.MessageBox.alert('��ʾ��Ϣ','���ύ�����ݲ����ظ��ύ');return;} \n" +
				"Ext.MessageBox.confirm('��ʾ��Ϣ','ȷ��Ҫ�ύ�ü�¼��?',\n" +
				"function(btn){if(btn=='yes'){\n" +
				"var Cobj = document.getElementById('CHANGE');\n" +
				"Cobj.value = grid1_history;" +
				"document.all.TijButton.click();" +
				"} });}");
		daor.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(daor);
		
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiqi(null);
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
	}

	
//	 �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}


	boolean treechange = false;

//	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}

}