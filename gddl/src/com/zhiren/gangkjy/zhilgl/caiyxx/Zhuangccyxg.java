package com.zhiren.gangkjy.zhilgl.caiyxx;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhuangccyxg extends BasePage implements PageValidateListener {
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
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	 ҳ��ˢ�����ڣ�жú���ڣ�
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//	 ҳ��ˢ�����ڣ�жú���ڣ�
	private String riqe;

	public String getRiqe() {
		return riqe;
	}
	public void setRiqe(String riqe) {
		this.riqe = riqe;
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

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			((Visit) getPage().getVisit()).setString7(getRiq());
			((Visit) getPage().getVisit()).setString8(getRiqe());
			cycle.activate(((Visit) getPage().getVisit()).getString9());
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
//		 жú���ڵ�ora�ַ�����ʽ
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + visit.getDiancxxb_id() + " or dc.fuid = "
						+ visit.getDiancxxb_id() + ")";
			} else {
				str = "and dc.id = " + visit.getDiancxxb_id() + "";
			}
		} 
		JDBCcon con = new JDBCcon();
		String sql = 
			"select c.id, z.zhilb_id,l.huaybh,to_char(z.zhuangcjssj,'yyyy-mm-dd') zhuangcsj,\n" +
			"       x.mingc jiehr ,p.mingc pinz ,c.bianm bianm,c.yangplb,c.yangpzl,\n" + 
			"       s.mingc jydw,c.jieyr,b.mingc caiylb ,c.beiz\n" + 
			"       from caiyb c,\n" + 
			"       (select t.id, t.mingc\n" + 
			"          from item t, itemsort s\n" + 
			"         where t.itemsortid = s.id\n" + 
			"           and s.mingc = '�������鵥λ') s,\n" + 
			"       zhuangcb z,\n" + 
			"       zhillsb l,\n" + 
			"       vwpinz p,\n" + 
			"       caiylbb b,\n" + 
			"       vwxuqdw x,\n" + 
			"       diancxxb dc \n" + 
			"       where z.diancxxb_id= dc.id \n" + 
			"             and z.zhilb_id=l.zhilb_id\n" + 
			"             and b.mingc='װ������'\n" + 
			"             and b.id=c.caiylbb_id\n" + 
			"             and c.songjdwb_id=s.id(+)\n" + 
			"             and c.id=l.caiyb_id\n" + 
			str+" \n"+
			"             and z.zhuangcjssj >= "+strxmrqOra+"\n" + 
			"             and z.zhuangcjssj <"+strxmrqeOra+"+ 1\n" + 
			"             and x.id=z.xiaosgysb_id\n" + 
			"             and p.id=z.pinzb_id";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// �½�grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// ����grid�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// �������ݲ���ҳ
		egu.addPaging(0);
		// ����gridΪ��ѡ
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		����grid������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("huaybh").setHidden(true);
		// ����grid�б���
		egu.getColumn("zhuangcsj").setHeader(Local.zhuangcsj_zhuangcb);
		egu.getColumn("jiehr").setHeader(Local.shouhr_jies);
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("bianm").setHeader(Local.cybm);
		egu.getColumn("yangplb").setHeader(Local.yangplb);
		egu.getColumn("yangpzl").setHeader(Local.yangpzl);
		egu.getColumn("jydw").setHeader(Local.songjdw);
		egu.getColumn("jieyr").setHeader(Local.jieyr);
		egu.getColumn("caiylb").setHeader(Local.caiylb);
		egu.getColumn("beiz").setHeader(Local.beiz);
//		�����п��
		egu.getColumn("zhuangcsj").setWidth(80);
		egu.getColumn("jiehr").setWidth(100);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("yangplb").setWidth(60);
		egu.getColumn("yangpzl").setWidth(80);
		egu.getColumn("jydw").setWidth(140);
		egu.getColumn("jieyr").setWidth(60);
		egu.getColumn("caiylb").setWidth(60);
		egu.getColumn("beiz").setWidth(180);
//		�趨grid�пɷ�༭
		egu.getColumn("zhuangcsj").setEditor(null);
		egu.getColumn("jiehr").setEditor(null);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("caiylb").setEditor(null);
		
		// ����������������
//		 ��ж����ѡ��
		egu.addTbarText(Local.jiexrq);
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
//		 ��ж����ѡ��
		egu.addTbarText(Local.riqz);
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
		
//		 ������ʽ
		ComboBox caiyfs = new ComboBox();
		egu.getColumn("yangplb").setEditor(caiyfs);
		caiyfs.setEditable(true);
		List h = new ArrayList();
		h.add(new IDropDownBean("�˹�","�˹�"));
		h.add(new IDropDownBean("��е","��е"));
		h.add(new IDropDownBean("���","���"));
		egu.getColumn("yangplb")
				.setComboEditor(egu.gridId, new IDropDownModel(h));
		
		// �ͼ쵥λ
		ComboBox songjdw = new ComboBox();
		egu.getColumn("jydw").setEditor(songjdw);
		songjdw.setEditable(true);
		sql = "select t.id,t.mingc from item t, itemsort s\n" +
		"where t.itemsortid = s.id and s.mingc = '�������鵥λ'";
		egu.getColumn("jydw").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
//		 ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		// ɾ����ť
		GridButton delete = new GridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "");
		egu.addTbarBtn(delete);
		// ���水ť
		GridButton save = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(save);
		
//		 ��ϸ���ⰴť
		GridButton Return = new GridButton("����", "Returnfun");
		Return.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(Return);

		setExtGrid(egu);
		con.Close();

	}

	private void save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û��δ�ύ�Ķ���");
			return;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String sql;
		int flag;
		// ɾ������
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		sql = "begin \n";
		String _msg = "";
		while (rs.next()) {
			String zhilbid = rs.getString("zhilb_id");
			String caiybid = rs.getString("id");
			String huaybh = rs.getString("huaybh");
			String caiybm = rs.getString("bianm");
			if(huaybh != null && !"".equals(huaybh)){
				_msg += "���� " + caiybm + " ɾ��ʧ��;(���л���ֵ)";
				continue;
			}
			sql += "delete from zhillsb where zhilb_id =" + zhilbid + ";\n";
			sql += "update zhuangcb set zhilb_id = 0 where zhilb_id =" + zhilbid + ";\n";
			sql += "delete from zhilb where id =" + zhilbid + ";\n";
			sql += "delete from caiyb where id =" + caiybid + ";\n";
		}
		sql += "end;\n";
		if (rs.getRows() > 0 && sql.length() > 14) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.DeleteDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		// �޸�����
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		while (rs.next()) {
			String caiybid = rs.getString("id");
			String caiybm = rs.getString("bianm");
			String yangplb = getExtGrid().getColumn("yangplb").combo.getBeanStrId(rs
					.getString("yangplb"));
			String songjdw = getExtGrid().getColumn("jydw").combo.getBeanStrId(rs
					.getString("jydw"));
			double yangpzl = rs.getDouble("yangpzl");
			String jieyr = rs.getString("jieyr");
			String beiz = rs.getString("beiz");
			
			sql += "update caiyb set \n" + " yangplb = '" + yangplb
					+ "',\n" + " yangpzl = " + yangpzl + ",\n"
					+ " bianm = '" + caiybm + "',\n" + " songjdwb_id = "
					+ songjdw + ",\n" + " jieyr = '" + jieyr+ "',\n" 
					+ " lury = '" + visit.getRenymc()+ "',\n" 
					+ " beiz = '" + beiz + "'\n"
					+ " where id=" + caiybid + ";\n";
		}
		sql += "end;";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		con.commit();
		con.Close();
		if("".equals(_msg)){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}else{
			setMsg(_msg);
		}
		
	}

	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			if(visit.getActivePageName().toString().equals(
					"Zhuangccy")){
				setRiq(visit.getString7());
				setRiqe(visit.getString8());
			}
			visit.setActivePageName(getPageName().toString());
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			if (getRiqe() == null) {
				setRiqe(DateUtil.FormatDate(new Date()));
			}
			init();
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
}