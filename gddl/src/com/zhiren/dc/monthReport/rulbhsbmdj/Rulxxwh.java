package com.zhiren.dc.monthReport.rulbhsbmdj;

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
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:ly
 * ʱ��:2010-07-27
 * ����:��¯��Ϣά����Ϊ������¯����˰��ú����ά��ϵͳ�еò��������ݡ�
 */

public class Rulxxwh extends BasePage implements PageValidateListener {
	// �����û���ʾ
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}

		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		getSelectData();
	}

	public void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "delete from rulxxwh where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ getTreeid() + "\n";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("ɾ�������з�������");
		} else {
			setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}


	public void CreateData() {
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrZnDate = getNianf() + "��" + getYuef() + "��";
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		int intYuef = Integer.parseInt(getYuef());
		int flag;
		String sql = "delete rulxxwh where riq = " + CurrODate
			+ " and diancxxb_id = " + diancxxb_id + "\n";
		flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ "ɾ����¯��Ϣά����ʱ�����쳣��" + "\nSQL:" + sql);
			setMsg("ɾ����¯��Ϣά����ʱ�����쳣��");
			con.rollBack();
			con.Close();
			return;
		}
		
		String sql_cmdj = "";
		String jizfz = "";
		ResultSetList rsl_dj = null;
		String sql_jz = "select * from jizfzb where diancxxb_id = " + diancxxb_id + "\n";
		ResultSetList rsl = con.getResultSetList(sql_jz);
		String newID = "";
		sql = "begin \n";
		while(rsl.next()){
			jizfz = "";
			if(rsl.getString("mingc").equals("����")){
				jizfz = "'ƶ��ú', '����ú'";
			} else if(rsl.getString("mingc").equals("����")){
				jizfz = "'����ú','��ú'";
			}
			
			newID = MainGlobal.getNewID(Long.parseLong(diancxxb_id));
		
			sql_cmdj = 
				"select decode(nvl(cm2.shangycmsl, 0) + nvl(cf.meil, 0) +\n" +
				"              nvl(bygj.benygjsl, 0) - nvl(sygj.shangygjsl, 0),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new((nvl(cm1.shangycmdj, 0) * nvl(cm2.shangycmsl, 0) +\n" + 
				"                        nvl(cf.chengfje, 0) + nvl(bygj.benygjje, 0) -\n" + 
				"                        nvl(sygj.shangygjje, 0) + nvl(zf.changnzf, 0)) /\n" + 
				"                        (nvl(cm2.shangycmsl, 0) + nvl(cf.meil, 0) +\n" + 
				"                        nvl(bygj.benygjsl, 0) - nvl(sygj.shangygjsl, 0)),\n" + 
				"                        2)) as shangycmdj\n" + 
				"  from --����\n" + 
				"       --���´�ú����\n" + 
				"        (select r.shangycmdj\n" + 
				"           from rulxxwh r\n" + 
				"          where r.diancxxb_id = " + diancxxb_id + "\n" + 
				"            and r.riq = add_months("+CurrODate+", -1)\n" + 
				"            and r.jizfzb_id = "+rsl.getString("id")+") cm1,\n" + 
				"       --���´�ú����\n" + 
				"       (select nvl(sum(y.kuc), 0) as shangycmsl\n" + 
				"          from yuehcb y, yuetjkjb tj\n" + 
				"         where y.yuetjkjb_id = tj.id\n" + 
				"           and y.fenx = '����'\n" + 
				"           and tj.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and tj.riq = add_months("+CurrODate+", -2)\n" + 
				"           and tj.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))) cm2,\n" + 
				"       --���³и�������\n" + 
				"       (select sum((y.meij + y.yunj + y.zaf + y.daozzf + y.qit + y.kuangqyf) *\n" + 
				"                   y.jiesl) as chengfje,\n" + 
				"               sum(y.jiesl) as meil\n" + 
				"          from yuejsbmdj y, yuetjkjb tj\n" + 
				"         where y.yuetjkjb_id = tj.id\n" + 
				"           and y.fenx = '����'\n" + 
				"           and tj.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and tj.riq = add_months("+CurrODate+", -1)\n" + 
				"           and tj.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))) cf,\n" + 
				"       --���¹��۽�����\n" + 
				"       (select nvl(sum(max(g.meij) * f.laimsl), 0) as benygjje,\n" + 
				"               nvl(sum(f.laimsl), 0) as benygjsl\n" + 
				"          from guslsb g, fahb f\n" + 
				"         where g.fahb_id = f.id\n" + 
				"           and g.leix < 4\n" + 
				"           and f.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and last_day(f.daohrq) =\n" + 
				"               last_day(add_months("+CurrODate+", -1))\n" + 
				"           and f.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))\n" + 
				"         group by f.id, f.laimsl) bygj,\n" + 
				"       --���¹��۽�����\n" + 
				"       (select nvl(sum(max(g.meij) * f.laimsl), 0) as shangygjje,\n" + 
				"               nvl(sum(f.laimsl), 0) as shangygjsl\n" + 
				"          from guslsb g, fahb f\n" + 
				"         where g.fahb_id = f.id\n" + 
				"           and g.leix < 4\n" + 
				"           and f.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and last_day(f.daohrq) =\n" + 
				"               last_day(add_months("+CurrODate+", -2))\n" + 
				"           and f.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))\n" + 
				"         group by f.id, f.laimsl) sygj,\n" + 
				"       (select r.changnzf\n" + 
				"          from rulxxwh r, jizfzb j\n" + 
				"         where r.jizfzb_id = j.id\n" + 
				"           and j.mingc = '"+rsl.getString("mingc")+"'\n" + 
				"           and r.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and r.riq = add_months("+CurrODate+", -1)) zf";
			rsl_dj = con.getResultSetList(sql_cmdj);
			if(rsl_dj.next()){
				sql += "insert into rulxxwh values("+newID+","+diancxxb_id+","+CurrODate+","+rsl_dj.getString("shangycmdj")+",0,0,"+rsl.getString("id")+");\n";
			} else {
				sql += "insert into rulxxwh values("+newID+","+diancxxb_id+","+CurrODate+",0,0,0,"+rsl.getString("id")+");\n";
			}
		}
		sql += "end; \n";
		
		flag = con.getInsert(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ "��¯��Ϣά������ʱ�����쳣��"+ "\nSQL:" + sql);
			setMsg("��¯��Ϣά������ʱ�����쳣��");
			con.rollBack();
			con.Close();
			return;
		}

		con.commit();
		con.Close();
		setMsg(CurrZnDate + "�����ݳɹ����ɣ�");
	}

	public void getSelectData() {
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		String sql = "select r.id,r.diancxxb_id,j.mingc as jizfzb_id,r.riq,shangycmdj,youqsdj,changnzf from rulxxwh r,jizfzb j \n"
				+ "	  where r.diancxxb_id = " + diancxxb_id + "\n"
				+ "		and r.jizfzb_id = j.id\n"
				+ "		and r.riq = " + CurrODate + "\n";
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\n��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("rulxxwh");
		// /������ʾ������
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// egu.getColumn("xuh").setHeader("���");
		// egu.getColumn("xuh").setWidth(50);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("jizfzb_id").setHeader("����");
		egu.getColumn("jizfzb_id").setWidth(100);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("shangycmdj").setHeader("���´�ú����");
		egu.getColumn("shangycmdj").setWidth(100);
		egu.getColumn("youqsdj").setHeader("��ȥ˰����");
		egu.getColumn("youqsdj").setWidth(100);
		egu.getColumn("changnzf").setHeader("ȼ�ϳ��ڷ���");
		egu.getColumn("changnzf").setWidth(100);

		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("jizfzb_id").setEditor(new ComboBox());
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select distinct id,mingc from jizfzb order by mingc"));
		egu.getColumn("jizfzb_id").returnId=true;
		egu.getColumn("jizfzb_id").setEditor(null);
		
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());

		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���

		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(MainGlobal.getExtMessageBox(
										"'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		// ���ɰ�ť
		GridButton gbc = new GridButton("����",
				getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		// ɾ����ť
		GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		// ���水ť
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);

		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "��" + getYuef() + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�ɾ��").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
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
			getSelectData();
		}
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}
