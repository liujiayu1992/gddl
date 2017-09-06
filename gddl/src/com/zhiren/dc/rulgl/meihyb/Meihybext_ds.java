package com.zhiren.dc.rulgl.meihyb;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
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
public class Meihybext_ds extends BasePage implements PageValidateListener {
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
			rsl.close();
			con.Close();
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {
		// Visit visit = (Visit) this.getPage().getVisit();
		// visit.getExtGrid1().Save(getChange(), visit);
		// UpdateRulzlID(getRiqi(),visit.getDiancxxb_id());

		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
//		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sb=new StringBuffer();
		sb.append("begin ");
		while(rsl.next()){
			sb.append("update meihyb set FADHY="+rsl.getString("FADHY")).append(",\n");
			sb.append(" GONGRHY="+rsl.getString("GONGRHY")).append(",\n");
			sb.append(" QITY="+rsl.getString("QITY")).append(",\n");
			sb.append(" TIAOZL="+rsl.getString("TIAOZL")+",\n");
			sb.append(" beiz='"+rsl.getString("BEIZ")+"'\n");
			sb.append(" WHERE ID="+rsl.getString("ID")+";\n");
		}
		sb.append("end ;");
		con.getUpdate(sb.toString());
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	private boolean shancButton;
	public void shancButton(IRequestCycle cycle){
		shancButton=true;
	}
	private boolean shengcButton;
	public void shengcButton(IRequestCycle cycle){
		shengcButton=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		if (shancButton) {
			shancButton = false;
			shanc();
		}
		if (shengcButton) {
			shengcButton = false;
			create();
		}
		getSelectData();
	}
	public void shanc(){
		JDBCcon con = new JDBCcon();
		String sql="delete \n" +
		"from meihyb m\n" + 
		"where m.rulrq="+ DateUtil.FormatOracleDate(this.getRiqi());
		con.getDelete(sql);
		sql="delete \n" +
		"from RULMZLB m\n" + 
		"where m.rulrq="+ DateUtil.FormatOracleDate(this.getRiqi());
		con.getDelete(sql);
		con.Close();
	}
	public void create(){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String riqStr="to_date('"+this.getRiqi()+"','yyyy-mm-dd')";
		if(con.getHasIt("select * from meihyb m where  m.rulrq ="+riqStr)){
			this.setMsg("ɾ�����ݺ��ٽ����������ݣ�");
			return;
		}
		String sql=
			"insert into meihyb(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,\n" +
			"FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,SHENHZT,ZHIYR,KAISSJ,JIESSJ,MEIL1,MEIL2,MEIL3,TIAOZL)\n" + 
			"select getNewId("+visit.getDiancxxb_id()+"),"+riqStr+","+visit.getDiancxxb_id()+",getNewId("+visit.getDiancxxb_id()+"),(select id from rulbzb where mingc=biaot.banz)banz_id,\n" + 
			"(select nvl(max(id),0) from jizfzb where mingc=biaot.jizbh)jizfzb_id,\n" + 
			//"0," +
			"getMeil(decode(Endtime,'00:00:00',"+riqStr+"+1,"+riqStr+"),jizbh,Endtime)-getMeil("+riqStr+",jizbh,Startime)"+
			",0,0,0,'',\n" + 
			"'"+visit.getRenymc()+"',sysdate,5,'',\n" + //getRiqi
			"getTime("+riqStr+",jizbh,Startime) kaissj\n" + //��ʼʱ��
			",getTime(decode(Endtime,'00:00:00',"+riqStr+"+1,"+riqStr+"),jizbh,Endtime)jiessj,\n" + 
			"getMeil("+riqStr+",jizbh,Startime)meil1,\n" + 
			"getMeil(decode(Endtime,'00:00:00',"+riqStr+"+1,"+riqStr+"),jizbh,Endtime)meil2,\n" + 
			"getMeil(decode(Endtime,'00:00:00',"+riqStr+"+1,"+riqStr+"),jizbh,Endtime)-getMeil("+riqStr+",jizbh,Startime) meil3,\n" + 
			"0 tiaozl\n" + 
			"from(\n" + 
			"select ScheTab.banz,jizfzb.mingc jizbh,ScheTab.Startime,ScheTab.Endtime\n" + 
			"from ScheTab,jizfzb)biaot";
		 	con.getInsert(sql);

		 	sql="insert into RULMZLB (ID,RULRQ,FENXRQ,DIANCXXB_ID,RULBZB_ID,JIZFZB_ID, MEIL,Lursj)\n" +
		 	"select rulmzlb_id,rulrq,rulrq,diancxxb_id,rulbzb_id,jizfzb_id,meihyb.fadhy+gongrhy meil,sysdate\n" + 
		 	"from meihyb\n" + 
		 	"where rulrq="+riqStr;
		 	con.getInsert(sql);
		 	con.Close();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rulrq = DateUtil.FormatOracleDate(this.getRiqi());	//��¯���� 
		String DefaultShzt = "5";		//Ĭ�����״̬  �ڲ�����ϵͳ���õ�ʱ��Ĭ��Ϊ�����
		String DefaultShjb = "";		//
		String Str = "m.shenhzt = 5";	//SQL�������  ֻ�г���Ӧ���״̬������
		String SQL = "select mingc from xitxxb where leib = 'ú����' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		if (rs.next()) {
			DefaultShjb = rs.getString("mingc");
		}
		rs.close();
		if (!DefaultShjb.equals("�����")) {
			DefaultShzt = "1";
			Str = "(m.shenhzt = 0 or m.shenhzt = 2)";
		} 
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
		
//		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
//				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,m.zhiyr,m.beiz,m.lury,m.lursj,m.shenhzt\n"
//				+ "  from meihyb m, diancxxb d, rulmzlb r, rulbzb rb, jizb j\n"
//				+ " where m.diancxxb_id = d.id(+)\n"
//				+ "   and m.rulmzlb_id = r.id(+)\n"
//				+ "   and m.rulbzb_id = rb.id(+)\n"
//				+ "   and m.jizfzb_id = j.id(+)\n"
//				+ "   and m.rulrq = "+ rulrq + "\n"
////				+ "   and d.id ="+ visit.getDiancxxb_id()
//				+ diancxxb_id
//				+ "   and " + Str
//		        +"  order by m.rulrq,rb.xuh,j.xuh";

		String chaxun = "select m.id, m.rulrq, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n" +
		" to_char(kaissj,'hh24:mi:ss')kaissj,to_char(jiessj,'hh24:mi:ss')jiessj,meil1,meil2,meil3,tiaozl, m.fadhy,m.gongrhy,m.qity,m.beiz\n" + 
		"  from meihyb m, diancxxb d, rulmzlb r, rulbzb rb, jizfzb j\n" + 
		" where m.diancxxb_id = d.id(+)\n" + 
		"   and m.rulmzlb_id = r.id(+)\n" + 
		"   and m.rulbzb_id = rb.id(+)\n" + 
		"   and m.jizfzb_id = j.id(+)\n"
		+ "   and m.rulrq = "+ rulrq + "\n"
	//	+ "   and d.id ="+ visit.getDiancxxb_id()
		+ diancxxb_id
		+ "   and " + Str
	    +"  order by m.rulrq,rb.xuh,j.xuh";

		// System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");
		egu.setWidth("bodyWidth");
//		egu.getColumn("diancxxb_id").setHeader("��λ");
//		egu.getColumn("diancxxb_id").setHidden(true);
//		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("��������");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulrq").setHidden(true);
//		egu.getColumn("rulmzlb_id").setHidden(true);
//		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("��¯����");
		egu.getColumn("rulbzb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setWidth(100);
		egu.getColumn("jizfzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setEditor(null);
		egu.getColumn("jizfzb_id").setWidth(80);
		egu.getColumn("kaissj").setHeader("��ʼ��");
		egu.getColumn("kaissj").setWidth(100);
		egu.getColumn("kaissj").setEditor(null);
		egu.getColumn("jiessj").setHeader("������");
		egu.getColumn("jiessj").setWidth(100);
		egu.getColumn("jiessj").setEditor(null);
		egu.getColumn("meil1").setHeader("���ú��");
		egu.getColumn("meil1").setEditor(null);
		egu.getColumn("meil1").setWidth(80);
		
		egu.getColumn("meil2").setHeader("ֹ��ú��");
		egu.getColumn("meil2").setEditor(null);
		egu.getColumn("meil2").setWidth(80);
		
		egu.getColumn("meil3").setHeader("��¯ú��");
		egu.getColumn("meil3").setEditor(null);
		egu.getColumn("meil3").setWidth(80);
		
		egu.getColumn("tiaozl").setHeader("������ú��");
		egu.getColumn("tiaozl").setWidth(80);
		
		egu.getColumn("fadhy").setHeader("�������");
		egu.getColumn("fadhy").setWidth(80);
		egu.getColumn("gongrhy").setHeader("���Ⱥ���");
		egu.getColumn("gongrhy").setWidth(80);
		egu.getColumn("qity").setHeader("������");
		egu.getColumn("qity").setWidth(80);
//		egu.getColumn("feiscy").setHeader("��������(��)");
//		egu.getColumn("zhiyr").setHeader("������");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(350);
//		egu.getColumn("lury").setHeader("¼��Ա");
//		egu.getColumn("lury").setHidden(true);
//		egu.getColumn("lury").setEditor(null);
//		egu.getColumn("lursj").setHeader("¼��ʱ��");
//		egu.getColumn("lursj").setHidden(true);
//		egu.getColumn("lursj").setEditor(null);
//		egu.getColumn("shenhzt").setHeader("״̬");
//		egu.getColumn("shenhzt").setHidden(true);
//		egu.getColumn("shenhzt").setEditor(null);
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		// *****************************************����Ĭ��ֵ****************************
//		egu.getColumn("diancxxb_id").setDefaultValue(
//				"" + visit.getDiancxxb_id());
		egu.getColumn("rulrq").setDefaultValue(this.getRiqi());
//		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
//		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
//		egu.getColumn("rulmzlb_id").setDefaultValue("0");
		egu.getColumn("fadhy").setDefaultValue("0");
		egu.getColumn("gongrhy").setDefaultValue("0");
		egu.getColumn("qity").setDefaultValue("0");
//		egu.getColumn("feiscy").setDefaultValue("0");
//		egu.getColumn("shenhzt").setDefaultValue(DefaultShzt);

//		// ������������¯����
//		ComboBox cb_banz = new ComboBox();
//		egu.getColumn("rulbzb_id").setEditor(cb_banz);
//		cb_banz.setEditable(true);
//		String rulbzb_idSql = "select r.id,r.mingc from rulbzb r,diancxxb d  where r.diancxxb_id=d.id(+)"
//			+ "and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by r.xuh";
//		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
//				new IDropDownModel(rulbzb_idSql));
//		egu.getColumn("rulbzb_id").setReturnId(true);
//		//���Ƿ�����¯�Ƚ���,��������¯����,����ר��Ϊ���Ƿ�����¯�������Ĭ��ֵ
//		if(visit.getDiancxxb_id()==264){//264�����Ƿ���ĵ糧id
//			egu.getColumn("rulbzb_id").setDefaultValue("ȫ��");
//		}
//		// ������������¯����
//		ComboBox cb_jiz = new ComboBox();
//		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
//		cb_jiz.setEditable(true);
//		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) " +
//				"and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by j.xuh";
//		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
//				new IDropDownModel(cb_jizSql));
//		egu.getColumn("jizfzb_id").setReturnId(true);
		
        // ����������������
//		ComboBox zhiyr = new ComboBox();
//		egu.getColumn("zhiyr").setEditor(zhiyr);
//		zhiyr.setEditable(true);
//		String cb_zhiyr = "select id, quanc from renyxxb where bum = '����'";
//		egu.getColumn("zhiyr").setComboEditor(egu.gridId,
//				new IDropDownModel(cb_zhiyr));
//		egu.getColumn("zhiyr").editor.setAllowBlank(true);

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
		
//		egu.addToolbarButton(GridButton.ButtonType_Delete, "shanc");
		egu.addToolbarItem("{"+new GridButton("ɾ��","function(){document.getElementById('shanc').click();}").getScript()+"}");
//		egu.addToolbarButton(GridButton.ButtonType_Delete, "shanc");
		egu.addToolbarItem("{"+new GridButton("��ȡ","function(){document.getElementById('shengcButton').click();}").getScript()+"}");
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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

	

	public static void UpdateRulzlID(String riq, long diancxxb_id) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("update meihyb h ")
				.append("set rulmzlb_id = ( \n")
				.append(
						"select nvl(max(id),0) from rulmzlb z where z.rulrq = h.rulrq \n")
				.append(
						"and z.diancxxb_id = h.diancxxb_id and z.rulbzb_id = h.rulbzb_id \n")
				.append("and z.jizfzb_id = h.jizfzb_id ) where h.rulrq = ")
				.append(DateUtil.FormatOracleDate(riq)).append(
						" and h.diancxxb_id=").append(diancxxb_id);
		JDBCcon con = new JDBCcon();
		con.getUpdate(sb.toString());
		con.Close();
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