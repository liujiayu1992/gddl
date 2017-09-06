package com.zhiren.dc.monthReport;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author ����
 * @since 2009-06-03
 * @describe ��Ƿ��ú��(����1��ɸ������δ����ú����ú�����δ����ú�����ϱ�����)
 * @version 1.0
 */
public class Yueqfmk extends BasePage implements PageValidateListener {
	/*
	 * ������ҳ����û���ʾ��
	 * �ں�̨������ʾ��Ϣ,�൱���ں�̨��ǰ̨������һ��java script �ű�
	 */
	private String msg = "";
	public String getMsg() {return msg;}
	public void setMsg(String msg) {this.msg = MainGlobal.getExtMessageBox(msg, false);}
	/*
	 * ҳ���ʼ���ص�ʱ��ִ�еķ���
	 * һЩÿ��ˢ��ҳ����ˢ�µı���  �ĳ�ʼ����������д������
	 * @see org.apache.tapestry.AbstractPage#initialize()
	 */
	protected void initialize() {super.initialize();setMsg("");}

	/*
	 * ��������ҳ��Ķ���һ��textfield���
	 * ���������Ϊһ��xml�ĵ� 
	 * ����ResultSetList �е�getModifyResultSet �� getDeleteResultSet ���н���
	 */
	private String Change;
	public String getChange() {return Change;}
	public void setChange(String change) {Change = change;}
	/*
	 * ҳ�水ť�ļ���Դ��Tapestry�����
	 * ���ڱ����أ�ͨ��Ext����İ�ť��click�¼�����
	 */
	/* ��Ӱ�ť */
	private boolean _Insertclick = false;
	public void InsertButton(IRequestCycle cycle) {_Insertclick = true;}
	/* ˢ�°�ť */
	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {_Refreshclick = true;}
	/* ���ɰ�ť */
	private boolean _CreateClick = false;
	public void CreateButton(IRequestCycle cycle) {_CreateClick = true;}
	/* ɾ����ť */
	private boolean _DelClick = false;
	public void DelButton(IRequestCycle cycle) {_DelClick = true;}
	/* ���水ť */
	private boolean _SaveClick = false;
	public void SaveButton(IRequestCycle cycle) {_SaveClick = true;}
	/* �ϱ���ť */
	private boolean _ShangbClick = false;
	public void ShangbButton(IRequestCycle cycle) {_ShangbClick = true;}
	/* �޸����밴ť */
	private boolean _ShenqxgClick = false;
	public void ShenqxgButton(IRequestCycle cycle) {_ShenqxgClick = true;}
	/*
	 * ����ҳ���form ��submit�¼�
	 * cycle �൱��jsp�е� request
	 */
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		if (_Insertclick) {
			_Insertclick = false;
			Insert();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_ShangbClick) {
			_ShangbClick = false;
			Shangb();
		}
		if (_ShenqxgClick) {
			_ShenqxgClick = false;
			Shenqxg();
		}
		initGrid();
	}
	
	private String getInsertSql(String diancxxb_id, String gongysb_id, 
			String CurrODate, String fenx){
		String sql = "insert into yueqfmkb(id,diancxxb_id,gongysb_id,riq,fenx) values(" +
		"getnewid(" + diancxxb_id + ")," + diancxxb_id + "," + gongysb_id + "," +
		CurrODate + ",'" + fenx + "')";
		return sql;
	}
	/**
	 * @describe �����û�ѡ��Ĺ�Ӧ�̲���Ƿ��ú����Ϣ
	 *
	 */
	private void Insert(){
		String gongysid = "";
		if(getGongysValue() == null || getGongysValue().getId() ==-1){
			setMsg("��ѡ����Ҫ���Ƿ������Ϣ�Ĺ�Ӧ��");
			return ;
		}
		gongysid = getGongysValue().getStrId();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String sql = "select * from yueqfmkb where diancxxb_id =" + diancxxb_id + 
		" and gongysb_id=" + gongysid + " and riq=" + CurrODate;
		if(con.getHasIt(sql)){
			setMsg("�˹�Ӧ�������Ѵ���,�����ٴ���ӣ�");
			return;
		}
		con.getInsert(getInsertSql(diancxxb_id,gongysid,CurrODate,SysConstant.Fenx_Beny));
		con.getInsert(getInsertSql(diancxxb_id,gongysid,CurrODate,SysConstant.Fenx_Leij));
	}
	/**
	 * @describe ������ʷδ�������ݼ������������ɹ�Ӧ���б� �������δ��ú��
	 * 		1��ɾ����������
	 * 		2�����ɹ�Ӧ���б��������ݿ�
	 * 		3���������ݿ��й�Ӧ����Ϣ����δ��ú��
	 * 		
	 */
	private void CreateData() {
		String diancxxb_id = getTreeid();	//�õ���ѡ�糧ID
		JDBCcon con = new JDBCcon();		//�������ݿ�JDBC����
		con.setAutoCommit(false);			//�趨JDBC���Զ��ύ
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
											//���ĵ�����
		Date cd = DateUtil.getDate(getNianf() + "-"
				+ getYuef() + "-01");		//��ǰѡ�����¶�Ӧ��Date����
		String CurrODate = DateUtil.FormatOracleDate(cd);
											//��ǰѡ������Oracle��ʽ����
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
											//����õ��ϸ��µ�Oracle��ʽ����
//		ɾ����������
		String sql = "delete from yueqfmkb where riq = " + CurrODate +
		" and diancxxb_id = " + diancxxb_id;
		con.getDelete(sql);
//		���ɹ�Ӧ���б��������ݿ�
		sql = "select distinct gongysb_id from yueqfmkb where diancxxb_id =" +
		diancxxb_id + " and riq = " + LastODate + " union select distinct g.dqid " +
		"from fahb f,vwgongys g where f.jiesb_id = 0 and f.gongysb_id = g.id" + 
		" and f.diancxxb_id = " + diancxxb_id;
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.getRows()>0){
			while(rsl.next()){
				con.getInsert(getInsertSql(diancxxb_id,rsl.getString("gongysb_id"),CurrODate,SysConstant.Fenx_Beny));
				con.getInsert(getInsertSql(diancxxb_id,rsl.getString("gongysb_id"),CurrODate,SysConstant.Fenx_Leij));
			}
			rsl.close();
		}
		sql = "select * from yueqfmkb where riq = " + CurrODate +
		" and diancxxb_id = " + diancxxb_id;
		rsl = con.getResultSetList(sql);
		ResultSetList rs = null;
		while(rsl.next()){
			String tmp = SysConstant.Fenx_Beny.equals(rsl.getString("fenx"))?
					" and f.daohrq>" + CurrODate + " and f.daohrq < add_months("+
					CurrODate+",1)":"";
			sql = "select sum(f.laimsl) laimsl,sum(f.laimsl * r.meij / 10000) qiank from fahb f,vwgongys g,ruccb r " +
					"where f.gongysb_id = g.id and f.ruccbb_id = r.id and diancxxb_id = " + 
					rsl.getString("diancxxb_id") + " and g.dqid = " +
					rsl.getString("gongysb_id") + " and f.jiesb_id = 0 " + tmp;
			rs = con.getResultSetList(sql);
			if(rs.next()){
				sql = "update yueqfmkb set meil =" + rs.getDouble("laimsl") + 
				",qiank =" + rs.getDouble("qiank") +
				" where id=" + rsl.getString("id");
				con.getUpdate(sql);
			}
			rs.close();
		}
		rsl.close();
		con.commit();
		con.Close();
		setMsg(CurrZnDate + "�����ݳɹ����ɣ�");
	}
	/**
	 *  @describe ɾ����������
	 *
	 */
	public void DelData() {
		String diancxxb_id = getTreeid();	//�õ���ѡ�糧ID
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String sql = "delete from yueqfmkb where riq = " + CurrODate +
		" and diancxxb_id = " + diancxxb_id;
		int flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ sql);
			setMsg("ɾ�������з�������");
		} else {
			setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}
	/**
	 * @describe �����޸ĺ��Ƿ��ú����Ϣ
	 *
	 */
	private void Save() {
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl.getRows()>0){
			String sql = "begin\n";
			while(rsl.next()){
				sql += "update yueqfmkb set meil=" + rsl.getDouble("meil")+
				",qiank = "+ rsl.getDouble("qiank") + " where id =" +
				rsl.getString("id") + ";\n";
			}
			rsl.close();
			sql += "end;\n";
			JDBCcon con = new JDBCcon();
			con.getUpdate(sql);
			con.Close();
		}
		rsl.close();

		setMsg(ErrorMessage.SaveSuccessMessage);
	}
	/**
	 * @describe �������ϱ�
	 *
	 */
	private void Shangb(){
		Date cd = DateUtil.getDate(getNianf() + "-"
				+ getYuef() + "-01");		//��ǰѡ�����¶�Ӧ��Date����
		String CurrODate = DateUtil.FormatOracleDate(cd);
											//��ǰѡ������Oracle��ʽ����
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		MainGlobal.Shujshcz(con,getTreeid(),CurrODate,
				"0","Yueqfmk",v.getRenymc(),"");
		con.Close();
		setMsg("�ϱ����ݳɹ���");
	}
	/**
	 * @describe ������ϱ����ݵ��޸�
	 *
	 */
	private void Shenqxg(){
		Date cd = DateUtil.getDate(getNianf() + "-"
				+ getYuef() + "-01");		//��ǰѡ�����¶�Ӧ��Date����
		String CurrODate = DateUtil.FormatOracleDate(cd);
											//��ǰѡ������Oracle��ʽ����
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//con.getUpdate(getInsRptTableSql());
		MainGlobal.Shujshcz(con,getTreeid(),CurrODate,
				"0","Yueqfmk",v.getRenymc(),getChange());
	}
	/**
	 * @describe ��ʼ��ҳ��
	 *
	 */
	public void initGrid() {
//		�õ���ǰ�糧����ѡ��ĵ糧
		String diancxxb_id = getTreeid();
//		�õ���ǰѡ�����¶�Ӧ��Oracle��ʽ������
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
//		ȡ������SQL
		String sql = "select s.id, g.mingc gongysb_id, s.fenx, s.meil, s.qiank, beiz\n" +
			"from yueqfmkb s,vwdianc d,vwgongys g\n" + 
			"where s.diancxxb_id = d.id and s.gongysb_id = g.id\n" + 
			"and s.riq= " + CurrODate + " and d.id = " + diancxxb_id +
			" order by g.xuh,g.mingc,s.fenx";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\n��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
// 		����grid���
		egu.setWidth("bodyWidth");
//		����Ϊ�༭����ҳ
		egu.addPaging(0);
//		����Ϊ�ɱ༭grid
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		��������
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
		egu.getColumn("meil").setHeader("δ����ú��(��)");
		egu.getColumn("qiank").setHeader("Ƿ��(��Ԫ)");
		egu.getColumn("beiz").setHeader("��ע");
//		�����п�
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("meil").setWidth(120);
		egu.getColumn("qiank").setWidth(100);
		egu.getColumn("beiz").setWidth(100);
//		�����в��ɱ༭
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setRenderer(
				"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer(
				"function(value,metadata){metadata.css='tdTextext'; return value;}");
		
//		�������������
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
//		�����·�������
		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
//		���÷ָ���
		egu.addTbarText("-");
// 		���õ糧ѡ����
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
// 		���÷ָ���
		egu.addTbarText("-");

// 		�ж������Ƿ�����(����״̬�ж�״̬1���ϱ�����0δ�ϱ�����)
		boolean isLocked = isLocked(con);
// 		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox(
				"'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+" +
				"Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",
		true)).append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
// 		���ɰ�ť
		GridButton gbc = new GridButton("����",
				getBtnHandlerScript("CreateButton"));
		if (isLocked) {
			gbc.setDisabled(true);
		}
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
// 		ɾ����ť
		GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
		if (isLocked) {
			gbd.setDisabled(true);
		}
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
// 		���水ť
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		if (isLocked) {
			gbs.setDisabled(true);
		}
		egu.addTbarBtn(gbs);
//		���÷ָ���
		egu.addTbarText("-");
//		���ù�Ӧ��������
		egu.addTbarText("��Ӧ��");
		ComboBox comb3 = new ComboBox();
		comb3.setWidth(120);
		comb3.setTransform("GongysDropDown");
		comb3.setId("GongysDropDown");// ���Զ�ˢ�°�
		comb3.setLazyRender(true);// ��̬��
		comb3.setEditable(true);
		egu.addToolbarItem(comb3.getScript());
		
//		��Ӱ�ť
		GridButton gbi = new GridButton("���",
				"function (){document.getElementById('InsertButton').click();}");
		egu.addTbarBtn(gbi);
//		���÷ָ���
		egu.addTbarText("-");
		if(isLocked){
			GridButton gbxg = new GridButton("�����޸�", 
			"function(){Rpt_window.show();}");
			gbxg.setIcon(SysConstant.Btn_Icon_Return);
			egu.addTbarBtn(gbxg);
		}else{
			GridButton gbsb = new GridButton("�ϱ�", 
			"function (){document.getElementById('ShangbButton').click();}");
			gbsb.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(gbsb);
		}
		
//		// ��ӡ��ť
//		GridButton gbp = new GridButton("��ӡ", "function (){"
//				+ MainGlobal.getOpenWinScript("MonthReport&lx=yueslb") + "}");
//		gbp.setIcon(SysConstant.Btn_Icon_Print);
//		egu.addTbarBtn(gbp);

		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "��" + getYuef() + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		} else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
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
			setRiq();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			initGrid();
		}
	}

	public boolean isLocked(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		return con.getHasIt("select zhuangt from shujshb where diancxxb_id = "+getTreeid()+"\n" +
				"and mokmc = 'Yueqfmk' and zhuangt = 1 and riq = " + CurrODate);
	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}

	// ���������
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�������
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

// 	��Ӧ��
    public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGongysValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getGongysModels() {
		String sql = "select g.dqid,g.dqmc from vwgongys g order by g.dqxh";
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, "��ѡ��"));
		return;
	}	
	
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

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}