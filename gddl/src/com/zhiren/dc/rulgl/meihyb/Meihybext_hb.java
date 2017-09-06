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
import org.apache.tapestry.form.IPropertySelectionModel;
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
//import com.zhiren.dc.diaoygl.AutoCreateShouhcrb;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
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
public class Meihybext_hb extends BasePage implements PageValidateListener {
//		�ͻ��˵���Ϣ��
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
//		ҳ���ʼ��(ÿ��ˢ�¶�ִ��)
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
//		 ���ڿؼ�
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			setRiqi(DateUtil.FormatDate(new Date()));
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);

		StringBuffer sb = new StringBuffer();
		StringBuffer sbdl = new StringBuffer();

		// ɾ������
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		if (delrsl.getRows() != 0) {
			sbdl.append("begin\n");
			while (delrsl.next()) {
				String id = delrsl.getString("id");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,
						"meihyb",id);
				sbdl.append(" delete from ").append("meihyb").append(
						" where id =").append(delrsl.getString("id")).append(
						";	\n");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,
						"rulmzlb",id);
				sbdl.append(" delete from ").append("rulmzlb").append(
						" where id =").append(delrsl.getString("rulmzlb_id"))
						.append(";	\n");
			}
			sbdl.append("end;");
			con.getDelete(sbdl.toString());
			con.commit();
		}

		// ��������
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yundlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}

		if (rsl.getRows() == 0) {
			return;
		}
		String riq1 = getRiqi();
		long diancxxb_id = 0;

		sb.append("begin\n");
		String rulrq = "";
		String strDate = "";
		while (rsl.next()) {
			// if (visit.isFencb()) {
			// diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
			// .getBeanId(rsl.getString("diancxxb_id"));
			// } else {
			// diancxxb_id = visit.getDiancxxb_id();
			// }
			diancxxb_id = Integer.parseInt(getTreeid());
			String rulmzlb_id = MainGlobal.getNewID(visit.getDiancxxb_id());// �õ�rulmzlb_id

			String fenxrq = DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			long rulbzb_id = ((IDropDownModel) getRulbzbModel()).getBeanId(rsl
					.getString("rulbzb_id"));
			long jizfzb_id = ((IDropDownModel) getJizfzbModel()).getBeanId(rsl
					.getString("jizfzb_id"));
			rulrq = DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			strDate = rsl.getString("rulrq");
			if ("0".equals(rsl.getString("id"))) {
				String sql = "select id from rulmzlb where rulrq = to_date('"+rsl.getString("RULRQ")+"','yyyy-mm-dd') and diancxxb_id="+diancxxb_id;
				ResultSetList rs = con.getResultSetList(sql);
				if(rs.getRows() <=0){	
					sb
					.append(
					"insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,rulrq,lursj,shenhzt) values (\n")
					.append(rulmzlb_id).append(",").append(diancxxb_id)
					.append(",").append(fenxrq).append(",").append(
							rulbzb_id).append(",").append(jizfzb_id)
							.append(",").append(rulrq).append(",").append(rulrq)
							.append(",0);\n");
					
					sb.append("insert into meihyb \n");
					sb
					.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,SHENHZT) \n");
					sb.append("values (getnewid(").append(diancxxb_id).append(
					"),to_date('");
					sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
					.append(diancxxb_id).append(",");
					sb.append(rulmzlb_id).append(",").append(rulbzb_id).append(",")
					.append(jizfzb_id);
					sb.append(",").append(rsl.getString("FADHY")).append(",")
					.append(rsl.getString("GONGRHY"));
					sb.append(",").append(rsl.getString("QITY")).append(",")
					.append(rsl.getString("FEISCY"));
					sb.append(",'").append(rsl.getString("BEIZ")).append("','")
					.append(rsl.getString("LURY"));
					sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
					"','yyyy-mm-dd'),");
					sb.append(rsl.getString("SHENHZT")).append("); \n");
				}else{
					while(rs.next()){	
						sb.append("update rulmzlb set rulbzb_id=").append(rulbzb_id).append(",jizfzb_id=").append(jizfzb_id)
						.append(" where id=").append(rs.getString("ID")).append(";");
						sb.append("insert into meihyb \n");
						sb
						.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,SHENHZT) \n");
						sb.append("values (getnewid(").append(diancxxb_id).append(
						"),to_date('");
						sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
						.append(diancxxb_id).append(",");
						sb.append(rs.getString("ID")).append(",").append(rulbzb_id).append(",")
						.append(jizfzb_id);
						sb.append(",").append(rsl.getString("FADHY")).append(",")
						.append(rsl.getString("GONGRHY"));
						sb.append(",").append(rsl.getString("QITY")).append(",")
						.append(rsl.getString("FEISCY"));
						sb.append(",'").append(rsl.getString("BEIZ")).append("','")
						.append(rsl.getString("LURY"));
						sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
						"','yyyy-mm-dd'),");
						sb.append(rsl.getString("SHENHZT")).append("); \n");
					}
				}

			} else {
				String id = rsl.getString("id");
				//�޸�ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"rulmzlb",id);
				sb.append("update rulmzlb set fenxrq=").append(fenxrq).append(
						",rulbzb_id=").append(rulbzb_id);
				sb.append(",jizfzb_id=").append(jizfzb_id).append(",rulrq=")
						.append(rulrq).append(",lursj=").append(rulrq).append(
								",shenhzt=0").append(" where id=");
				sb.append(rsl.getString("rulmzlb_id")).append(";\n");

				//�޸�ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"meihyb",id);
				sb.append("update meihyb set RULRQ=to_date('").append(
						rsl.getString("RULRQ")).append(
						"','yyyy-mm-dd'),DIANCXXB_ID=");
				sb.append(diancxxb_id).append(",RULMZLB_ID=").append(
						rsl.getString("RULMZLB_ID")).append(",RULBZB_ID=");
				sb.append(rulbzb_id).append(",JIZFZB_ID=").append(jizfzb_id)
						.append(",FADHY=");
				sb.append(rsl.getString("FADHY")).append(",GONGRHY=").append(
						rsl.getString("GONGRHY")).append(",QITY=");
				sb.append(rsl.getString("QITY")).append(",FEISCY=").append(
						rsl.getString("FEISCY")).append(",BEIZ='");
				sb.append(rsl.getString("BEIZ")).append("',LURY='").append(
						rsl.getString("LURY")).append("',LURSJ=to_date('");
				sb.append(rsl.getString("LURSJ")).append(
						"','yyyy-mm-dd'),SHENHZT=").append(
						rsl.getString("SHENHZT"));
				sb.append(" where id=").append(rsl.getString("id")).append(
						";\n");

			}
		}
		sb.append("end;");
		con.getInsert(sb.toString());
//		AutoCreateShouhcrb.Create(con,diancxxb_id,DateUtil.getDate(strDate));
		con.commit();
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
	private boolean shousButton = false;

	public void shousButton(IRequestCycle cycle) {
		shousButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(shousButton){
			shousButton = false;
			shous();
			getSelectData();
		}
	}
	private void shous(){
		//�����¯���ô���
			//���ܲ�����������ɾ��
		//�����¯���ò�����
			//����rulmzlb��meihyb

		JDBCcon con=new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql="select *\n" +
		"from meihyb\n" + 
		"where to_char(meihyb.rulrq,'yyyy-mm-dd')='"+getRiqi()+"'";
		ResultSet rs=con.getResultSet(sql);
		String rulmzlb_id="0";
		try{
			if(rs.next()){//�����¯���ô���
				setMsg("ú�����Ѿ����ڣ���ɾ�����ٲ�����");
			}else{
				int flag=0;
				con.setAutoCommit(true);
//					����rulmzlb��meihyb
				rulmzlb_id= MainGlobal.getNewID(visit.getDiancxxb_id());
			   sql="insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,rulrq,lursj,shenhzt) values (\n"+
			   rulmzlb_id+","+visit.getDiancxxb_id()+",to_date('"+getRiqi()+"','yyyy-mm-dd')+1,(select id from rulbzb where mingc='ȫ��'),"+
			   "(select id from JIZFZB where mingc='ȫ������'),"+"to_date('"+getRiqi()+"','yyyy-mm-dd'),sysdate,0)";
			   flag= con.getInsert(sql);
			   if(flag==-1){
				   con.rollBack();
				   return;
			   }
			   sql=
				   "insert into meihyb(id,rulrq,diancxxb_id,rulmzlb_id,rulbzb_id,jizfzb_id,fadhy,gongrhy,shenhzt)(\n" +
				   "select getnewid(257)id,to_date('"+getRiqi()+"','yyyy-mm-dd')riq,"+visit.getDiancxxb_id()+" diancxxb_id,"+rulmzlb_id+" RULMZLB_ID,\n" + 
				   "(select id from rulbzb where mingc='ȫ��')rulbzb_id,\n" + 
				   "(select id from JIZFZB where mingc='ȫ������')JIZFZB_ID,\n" + 
				   "b.fadhy,b.gongrhy,5 shenhzt\n" + 
				   "from (\n" + 
				   "     select nvl(sum(fadhy),0)fadhy,nvl(sum(gongrhy),0)gongrhy\n" + 
				   "    from (\n" + 
				   "    select plan_data fadhy,0 gongrhy\n" + 
				   "    from zhiren@tj t\n" + 
				   "    where t.plan_xh='90041' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')--һ��ƽ�ⷢ�����Ȼú��\n" + 
				   "    union\n" + 
				   "    select 0 fadhy,plan_data gongrhy\n" + 
				   "    from zhiren@tj t\n" + 
				   "    where t.plan_xh='90054' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')--һ��ƽ�⹩����Ȼú��\n" + 
				   "    )\n" + 
				   ")b\n" + 
				   " )";
			   flag=con.getInsert(sql);
			   if(flag==-1){
				   con.rollBack();
				   return;
			   }
			   	con.commit();
			}
		}catch(Exception e){
			e.printStackTrace();
			con.rollBack();
		}finally{
			con.Close();
		}
		
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
			diancxxb_id = "and d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + "\n";
		}else if(getDiancTreeJib()==3){
			diancxxb_id = "and d.id = " + this.getTreeid() + "\n";
		}
		
		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,m.beiz,m.lury,m.lursj,m.shenhzt\n"
				+ "  from meihyb m, diancxxb d, rulmzlb r,(select item.id,item.mingc from item,itemsort where item.itemsortid=itemsort.id and itemsort.bianm='RUHYBZ') rb, jizfzb j\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulmzlb_id = r.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+ "   and m.rulrq = "+ rulrq + "\n"
//					+ "   and d.id ="+ visit.getDiancxxb_id()
				+ diancxxb_id
				+ "   and " + Str;;
		// System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");

		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("��������");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setHeader("��¯����");
		egu.getColumn("fadhy").setHeader("�������(��)");
		egu.getColumn("gongrhy").setHeader("���Ⱥ���(��)");
		egu.getColumn("qity").setHeader("������(��)");
		egu.getColumn("feiscy").setHeader("��������(��)");
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
		egu.setWidth(1000);// ����ҳ��Ŀ��,������������ʱ��ʾ������
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + getTreeid());
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
		String rulbzb_idSql = "select r.id,r.mingc from rulbzb r where r.diancxxb_id="
				+ visit.getDiancxxb_id() + " order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		egu.getColumn("rulbzb_id").setReturnId(true);
		// ������������¯����
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="
				+ visit.getDiancxxb_id() + "";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));
		egu.getColumn("jizfzb_id").setReturnId(true);

		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//			 �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
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
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		if(MainGlobal.getXitxx_item("���õ���", "���õ���ȡ����", "0", "��").equals("��")){
//				egu.addToolbarButton(GridButton.."��ȡ����", "shouqsj");
			egu.addToolbarItem("{"+new GridButton("��ȡ����","function(){document.getElementById('shousButton').click();}").getScript()+"}");
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
			setJizfzbModel(null);
			setJizfzbModels();
			setRulbzbModel(null);
			setRulbzbModels();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			
		}
		getSelectData();
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

	public IPropertySelectionModel getRulbzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setRulbzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setRulbzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setRulbzbModels() {
		String sql = "select item.id,item.mingc from item,itemsort where item.itemsortid=itemsort.id and itemsort.bianm='RUHYBZ'";
		setRulbzbModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getJizfzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setJizfzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setJizfzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setJizfzbModels() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="
				+ visit.getDiancxxb_id() + "";
		setJizfzbModel(new IDropDownModel(sql));
	}
	
//		 �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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

	private String treeid;

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
