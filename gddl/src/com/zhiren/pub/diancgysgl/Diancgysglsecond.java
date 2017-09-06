package com.zhiren.pub.diancgysgl;

/*
 * 2009-06-29
 * ll
 * ���ݲ�ͬ���û���ҳ�������Ƿ����ӡ�úԴ���á����ܡ�
 */
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Diancgysglsecond extends BasePage implements PageValidateListener {

	private boolean returnMsg = false;

	private boolean hasSaveMsg = false;

	private String _msg;

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value, false);
	}

	protected void initialize() {
		super.initialize();
		_msg = "";
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

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _GobackChick = false;

	public void GobackButton(IRequestCycle cycle) {
		_GobackChick = true;
		((Visit) this.getPage().getVisit()).setString3(this.getTreeid());
		cycle.activate("Diancgysglsz");
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	public void submit(IRequestCycle cycle) {
		returnMsg = false;
		if (_SaveChick) {
			_SaveChick = false;
			Visit visit = (Visit) this.getPage().getVisit();
			Save3(getChange(),visit);
			getSelectData();
		}

		if (_Refreshclick) {
			_Refreshclick = false;

			getSelectData();
		}
		if (_GobackChick) {
			_GobackChick = false;

		}
	}

	// ///////////////////////////////////////
	
	public int Save3(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			String id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append("diancgysmykjb ")
				.append("(ID,DIANCGYSGLB_ID,GONGYSKJB_ID,MEIYXXB_ID,SHIYZT,JITRBDWB_ID,FENL)")
			    .append("values('").append(id).append("','")
			    .append(getExtGrid().getValueSql(getExtGrid().getColumn("DIANCGYSGLB_ID"), mdrsl.getString("DIANCGYSGLB_ID"))).append("','")
			    .append(getExtGrid().getValueSql(getExtGrid().getColumn("GONGYSKJB_ID"),mdrsl.getString("GONGYSKJB_ID"))).append("','")
				.append(getExtGrid().getValueSql(getExtGrid().getColumn("MEIYXXB_ID"),mdrsl.getString("MEIYXXB_ID"))).append("','")
				.append(getExtGrid().getValueSql(getExtGrid().getColumn("SHIYZT"),mdrsl.getString("SHIYZT"))).append("','")
				.append(getExtGrid().getValueSql(getExtGrid().getColumn("JITRBDWB_ID"), mdrsl.getString("JITRBDWB_ID"))).append("','")
				.append(getExtGrid().getValueSql(getExtGrid().getColumn("FENL"),mdrsl.getString("FENL"))).append("');\n");
			} else {
				sql.append("update ").append("diancgysmykjb ").append("set ")
				.append("DIANCGYSGLB_ID=").append(getExtGrid().getValueSql(getExtGrid().getColumn("DIANCGYSGLB_ID"), mdrsl.getString("DIANCGYSGLB_ID"))).append(",")
				.append("GONGYSKJB_ID=").append(getExtGrid().getValueSql(getExtGrid().getColumn("GONGYSKJB_ID"),mdrsl.getString("GONGYSKJB_ID"))).append(",")
				.append("MEIYXXB_ID=").append(getExtGrid().getValueSql(getExtGrid().getColumn("MEIYXXB_ID"),mdrsl.getString("MEIYXXB_ID"))).append(",")
				.append("SHIYZT=").append(getExtGrid().getValueSql(getExtGrid().getColumn("SHIYZT"),mdrsl.getString("SHIYZT"))).append(",")
				.append("JITRBDWB_ID=").append(getExtGrid().getValueSql(getExtGrid().getColumn("JITRBDWB_ID"), mdrsl.getString("JITRBDWB_ID"))).append(",")
				.append("FENL=").append(getExtGrid().getValueSql(getExtGrid().getColumn("FENL"),mdrsl.getString("FENL"))).append("")
				.append(" where id=").append(mdrsl.getLong("ID")).append(";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		con.Close();
		return flag;
	}

	// /////////////////////////////////////////////

	public void getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String chaxun = "";
		// int jib=this.getDiancTreeJib();
		// ��Ӧ���� ��

		String meiy_id = "";
		if (this.isShezhimeiyuan() == false) {
			meiy_id = "";
		} else if (this.isShezhimeiyuan() == true) {
			meiy_id = "(select distinct mingc from meiyxxb where id= kj.meiyxxb_id)  meiyxxb_id,\n";
		}
		chaxun =  

			"select distinct dj.id,\n" +
			"                (select distinct ys1.quanc\n" + 
			"                   from diancgysglb gl1, gongysb ys1\n" + 
			"                  where ys1.id = gl1.gongysb_id\n" + 
			"                    and gl1.id = dg.id) diancgysglb_id,\n" + 
			"                decode(dj.gongyskjb_id,\n" + 
			"                       1,\n" + 
			"                       '�ص�',\n" + 
			"                       2,\n" + 
			"                       '����',\n" + 
			"                       dj.gongyskjb_id) gongyskjb_id,\n" + 
			"                (select distinct mingc\n" + 
			"                   from meiyxxb\n" + 
			"                  where id = dj.meiyxxb_id) meiyxxb_id,\n" + 
			"                decode(dj.shiyzt, 1, '��', 0, '��', dj.shiyzt) shiyzt,\n" + 
			"                (select distinct mingc\n" + 
			"                   from jitrbdwb\n" + 
			"                  where id = dj.jitrbdwb_id) jitrbdwb_id,\n" + 
			"                (select distinct mingc from item where id = dj.fenl) fenl\n" + 
			"           from jitrbdwb      jr,\n" + 
			"                item          i,\n" + 
			"                diancgysmykjb dj,\n" + 
			"                diancgysglb   dg,\n" + 
			"                meiyxxb       my,\n" + 
			"                gongysb       gy,\n" + 
			"                diancxxb      db\n" + 
			"          where dj.diancgysglb_id = dg.id(+)\n" + 
			"            and dg.id = gy.id(+)\n" + 
			"            and dj.meiyxxb_id = my.id(+)\n" + 
			"            and dj.jitrbdwb_id=jr.id"+
			"            and dj.fenl=i.id"+
			"            and dg.diancxxb_id = "+this.getTreeid();
		rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setTableName("diancgysmykjb");// �������sql��ѯ���������ݴ洢��shouhcrbb���ű��С�
		// �糧
		egu.getColumn("id").setHeader("�ھ�id");
		egu.getColumn("id").setWidth(180);
		egu.getColumn("id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("id").setEditor(null);
		// �糧������
		// egu.getColumn("id").setEditor(new ComboBox());
		// egu.getColumn("id").setComboEditor(egu.gridId, new
		// IDropDownModel("select nvl(kj.id,0),dc.mingc from diancxxb
		// dc,diancgysglb gl,diancgysmykjb kj where dc.id=gl.gongysb_id and
		// gl.id=kj.diancgysglb_id(+) and
		// gl.diacxxb_id="+this.getTreeid()+"\n"));
		// egu.getColumn("id").setReturnId(true);
		// ú��
		egu.getColumn("diancgysglb_id").setHeader("ú������");
		// ��Ӧ����Ϣ��������
		ComboBox gongysb = new ComboBox();
		gongysb.setEditable(true);
		gongysb.setWidth(260);
		egu.getColumn("diancgysglb_id").setEditor(gongysb);

		egu.getColumn("diancgysglb_id").setWidth(261);
		String GongysSql = "select gl.id,ys.quanc from diancgysglb gl,gongysb ys where ys.id=gl.gongysb_id and gl.diancxxb_id="
				+ this.getTreeid() + "\n";
		egu.getColumn("diancgysglb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("diancgysglb_id").setReturnId(true);
		// úԴ
		if (this.isShezhimeiyuan() == true) {
			egu.getColumn("meiyxxb_id").setHeader("úԴ");
			ComboBox meiycbox = new ComboBox();
			egu.getColumn("meiyxxb_id").setEditor(meiycbox);
			meiycbox.setEditable(true);
			String meiySql = "select my.id,my.mingc from meiyxxb my order by id ";
			egu.getColumn("meiyxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(meiySql));
			egu.getColumn("meiyxxb_id").setDefaultValue("��ѡ��");
			egu.getColumn("meiyxxb_id").setReturnId(true);
		}
		// ��ú����
		egu.getColumn("gongyskjb_id").setHeader("��ú����");
		
		ComboBox leixcbox = new ComboBox();
		egu.getColumn("gongyskjb_id").setEditor(leixcbox);
		leixcbox.setEditable(true);
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ص�"));
		list.add(new IDropDownBean(2, "����"));
		egu.getColumn("gongyskjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(list));
		egu.getColumn("gongyskjb_id").setReturnId(true);
		// �Ƿ�����
		egu.getColumn("shiyzt").setHeader("�Ƿ���");
		ComboBox Shiyong = new ComboBox();
		egu.getColumn("shiyzt").setEditor(Shiyong);
		List Shiyonglist = new ArrayList();
		Shiyonglist.add(new IDropDownBean(1, "��"));
		Shiyonglist.add(new IDropDownBean(0, "��"));
		egu.getColumn("shiyzt").setComboEditor(egu.gridId,
				new IDropDownModel(Shiyonglist));
		egu.getColumn("shiyzt").setReturnId(true);

		//�����ձ���λ��ID��jitrbdwb_id������Ϊ����
		//egu.getColumn("jitrbdwb_id").setHidden(true);
		egu.getColumn("jitrbdwb_id").setHeader("��������");
		ComboBox rbdwbox = new ComboBox();
		egu.getColumn("jitrbdwb_id").setEditor(rbdwbox);
		rbdwbox.setEditable(true);
		String rbdwSql = "select jt.id,jt.mingc from jitrbdwb jt";
		egu.getColumn("jitrbdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rbdwSql));
		egu.getColumn("jitrbdwb_id").setDefaultValue("��ѡ��");
		egu.getColumn("jitrbdwb_id").setReturnId(true);
		egu.getColumn("jitrbdwb_id").setWidth(260);
		
		// ����
		egu.getColumn("fenl").setHeader("����");
		ComboBox fenlbox = new ComboBox();
		egu.getColumn("fenl").setEditor(fenlbox);
		fenlbox.setEditable(true);
		String fenlsql = "select t.id,t.mingc from item t,itemsort it where t.itemsortid=it.itemsortid and it.bianm='JTRBHTFL'";
		egu.getColumn("fenl").setComboEditor(egu.gridId,new IDropDownModel(fenlsql));
		egu.getColumn("fenl").setDefaultValue("��ѡ��");
		egu.getColumn("fenl").setReturnId(true);
		egu.getColumn("fenl").setWidth(100);

		/*
		 * 
		 * egu.getColumn("leix").setDefaultValue(getGongmLxValue().getValue());
		 * egu.getColumn("leix").setEditor(null);
		 * egu.getColumn("leix").setHidden(true);
		 * 
		 * egu.getColumn("rijjhl").setHeader("�վ��ƻ���");
		 * egu.getColumn("ches").setHeader("����");
		 * egu.getColumn("duns").setHeader("����");
		 * egu.getColumn("shangbzt").setHeader("��˾�ϱ�״̬");
		 * egu.getColumn("shangbzt").setDefaultValue("0");
		 *  // egu.getColumn("diancxxb_id").setUpdate(false);
		 * 
		 * egu.getColumn("riq").setEditor(null);
		 * egu.getColumn("riq").setHidden(true);
		 * 
		 * egu.getColumn("shangbzt").setEditor(null);
		 * egu.getColumn("shangbzt").setHidden(true);
		 * 
		 * 
		 * //�趨�еĳ�ʼ���
		 * 
		 * 
		 * egu.getColumn("gongysb_id").setWidth(320);
		 * egu.getColumn("riq").setWidth(80);
		 * egu.getColumn("leix").setWidth(80);
		 * egu.getColumn("rijjhl").setWidth(70);
		 * egu.getColumn("ches").setWidth(80);
		 * egu.getColumn("duns").setWidth(60);
		 * egu.getColumn("shangbzt").setWidth(70);
		 * 
		 * //�������Ի�û�������ء�
		 * 
		 * //�趨���ɱ༭�е���ɫ
		 * 
		 * egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext';
		 * return value;}"); //
		 * egu.getColumn("shangbzt").setRenderer("function(value,metadata){metadata.css='tdTextext';
		 * return value;}");
		 */
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(20);
		egu.setWidth(2000);
		egu.setDefaultsortable(false);// �趨ҳ�治�Զ�����
		// *****************************************����Ĭ��ֵ****************************

		// *************************������*****************************************88
		// //�糧������
		// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		// egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new
		// IDropDownModel("select id,mingc from diancxxb order by mingc"));
		// egu.getColumn("diancxxb_id").setReturnId(true);

		// ��Ӧ����Ϣ��������
		// ComboBox gongysb= new ComboBox();
		// egu.getColumn("gongysb_id").setEditor(gongysb);
		// gongysb.setEditable(true);
		//	
		// String GongysSql = "select vw.id,vw.gongysmy from vwdcgysmykjgl
		// vw,diancxxb dc where vw.diancxxb_id=dc.id "+strDiancId+" and
		// vw.gongyskjb_id="+getGongmLxValue().getId()+" order by vw.gongysmy ";
		//		
		// egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new
		// IDropDownModel(GongysSql));
		// egu.getColumn("gongysb_id").setReturnId(true);
		//		

		// ��ú����
		// List list = new ArrayList();
		// list.add(new IDropDownBean(1,"�ص�"));
		// list.add(new IDropDownBean(2,"����"));
		// egu.getColumn("leix").setEditor(new ComboBox());
		// egu.getColumn("leix").setComboEditor(egu.gridId, new
		// IDropDownModel(list));
		// egu.getColumn("leix").setReturnId(false);

		// ������,ʱ��ѡ���
		// egu.addTbarText("����:");
		// DateField df = new DateField();
		// df.setValue(this.getRiqi());
		// df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		// df.listeners="change:function(own,newValue,oldValue)
		// {document.getElementById('RIQI').value =
		// newValue.dateFormat('Y-m-d');document.forms[0].submit();"+MainGlobal.getExtMessageShow("���ڴ������Եȡ�����",
		// "������..", 200)+"}";
		// egu.addToolbarItem(df.getScript());

		// �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());

		// TreeButton tbt=new
		// TreeButton(TreeButton.ButtonType_Window_Ok,etu.treeId,null);
		// tbt.handler=tbt.handler.substring(0,
		// tbt.handler.length()-2)+";"+MainGlobal.getExtMessageShow("���ڴ������Եȡ�����",
		// "������..", 200)+"}\n";
		// etu.addBbarButton(tbt);

		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");
		/*
		 * egu.addTbarText("���ͣ�"); ComboBox leix=new ComboBox();
		 * leix.setTransform("GongmLxSelect");
		 * leix.setListeners("select:function(){document.Form0.submit();"+MainGlobal.getExtMessageShow("���ڴ������Եȡ�����",
		 * "������..", 200)+"}"); // egu.addField(leix); leix.setWidth(60);
		 * egu.addToolbarItem(leix.getScript());
		 */

		// //---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		/*
		 * sb.append("gridDiv_grid.on('beforeedit',function(e){"); //
		 * sb.append("if(e.record.get('DIANCXXB_ID')=='�ϼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
		 * sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�糧�в�����༭
		 * sb.append("});");
		 *  // // //�趨�ϼ��в����� // sb.append("function
		 * gridDiv_save(record){if(record.get('diancxxb_id')=='�ϼ�') return
		 * 'continue';}"); // egu.addOtherScript(sb.toString());
		 */// ---------------ҳ��js�������--------------------------
		egu.addTbarText("-");
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				MainGlobal.getExtMessageShow("����ˢ������,���Ժ�", "ˢ���С���", 200))
				.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		// ���
		egu.addToolbarButton(GridButton.ButtonType_Insert, "InsertButton");
		egu.addTbarText("-");
		// ɾ��
		// egu.addToolbarButton(GridButton.ButtonType_Delete, "DeleteButton");
		// egu.addTbarText("-");
		// ����
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton", ""
				+ MainGlobal.getExtMessageShow("����ˢ������,���Ժ�", "ˢ���С���", 200));
		egu.addTbarText("-");
		// ����
		// egu.addToolbarButton(GridButton.ButtonType_Insert, "GobackButton");
		GridButton gb = new GridButton("����",
				"function(){document.getElementById('GobackButton').click();}",
				SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(gb);
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
			if (!visit.getActivePageName().toString().equals("Diancgysglsz")) {
				this.setTreeid(null);
			} else {
				this.setTreeid(visit.getString3());
			}
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			// this.setTreeid(null);
			// this.setRiqi(null);

		}

		getSelectData();
		if (!returnMsg) {
			setMsg("");
		}
		returnMsg = false;

	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ getTreeid();
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// �õ��Ƿ��������ϵͳ���ò���
	// private String getBaohys() {
	// String baohys = "";
	// JDBCcon cn = new JDBCcon();
	// long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
	// String sql= "select zhi from xitxxb where mingc='�Ƿ��������' and
	// diancxxb_id="
	// + diancid;
	// ResultSet rs = cn.getResultSet(sql);
	// try {
	// while (rs.next()) {
	// baohys = rs.getString("zhi");
	// }
	// rs.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// cn.Close();
	// }
	//
	// return baohys;
	//
	// }

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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

	// �õ��糧��Ĭ�ϵ�վ
	// public String getDiancDaoz(){
	// String daoz = "";
	// String treeid=this.getTreeid();
	// if(treeid==null||treeid.equals("")){
	// treeid="1";
	// }
	// JDBCcon con = new JDBCcon();
	// try {
	// StringBuffer sql = new StringBuffer();
	// sql.append("select dc.mingc, cz.mingc as daoz\n");
	// sql.append(" from diancxxb dc, chezxxb cz,diancdzb dd\n");
	// sql.append(" where dd.diancxxb_id=dc.id\n");
	// sql.append(" and dd.chezxxb_id=cz.id\n");
	// sql.append(" and dc.id = "+treeid+"");
	//
	// ResultSet rs = con.getResultSet(sql.toString());
	//			
	// while (rs.next()) {
	// daoz = rs.getString("daoz");
	// }
	// rs.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }finally{
	// con.Close();
	// }
	//		
	// return daoz;
	// }

	boolean treechange = false;

	private String treeid;

	// public String getTreeid() {
	// if(treeid==null||treeid.equals("")){
	// treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
	// }
	// return treeid;
	// }
	// public void setTreeid(String treeid) {
	// this.treeid = treeid;
	// treechange=true;
	// }
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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

	// ���ڿؼ�
	// boolean riqichange=false;
	// private String riqi;
	// public String getRiqi() {
	// if(riqi==null||riqi.equals("")){
	// riqi=DateUtil.FormatDate(DateUtil.AddDate(new
	// Date(),-1,DateUtil.AddType_intDay));
	// }
	// return riqi;
	// }
	// public void setRiqi(String riqi) {
	//		
	// if(this.riqi!=null &&!this.riqi.equals(riqi)){
	// this.riqi = riqi;
	// riqichange=true;
	// }
	//		 
	// }
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

	// ��ѯ�Ƿ�����úԴ
	private boolean isShezhimeiyuan() {
		JDBCcon con = new JDBCcon();
		boolean iss = false;
		ResultSetList rsl = con
				.getResultSetList("select distinct xx.zhi from xitxxb xx where xx.mingc='�Ƿ�����úԴ'\n");

		while (rsl.next()) {
			iss = true;
		}
		con.Close();
		return iss;
	}

	private static IPropertySelectionModel _GongmLxModel;// ��ú����

	public IPropertySelectionModel getGongmLxModel() {
		if (_GongmLxModel == null) {
			getGongmLxModels();
		}
		return _GongmLxModel;
	}

	private IDropDownBean _GongmLxValue;

	public IDropDownBean getGongmLxValue() {
		if (_GongmLxValue == null) {
			setGongmLxValue((IDropDownBean) getGongmLxModel().getOption(0));
		}
		return _GongmLxValue;
	}

	private boolean _GongmLxChange = false;

	public void setGongmLxValue(IDropDownBean Value) {
		if (_GongmLxValue == Value) {
			_GongmLxChange = false;
		} else {
			_GongmLxValue = Value;
			_GongmLxChange = true;
		}
	}

	public IPropertySelectionModel getGongmLxModels() {
		List listGongmLx = new ArrayList();
		listGongmLx.add(new IDropDownBean(1, "�ص�"));
		listGongmLx.add(new IDropDownBean(2, "����"));

		_GongmLxModel = new IDropDownModel(listGongmLx);
		return _GongmLxModel;
	}

	public void setGongmLxModel(IPropertySelectionModel _value) {
		_GongmLxModel = _value;
	}

}
