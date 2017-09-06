package com.zhiren.jt.zdt.yunsjh.yueysjh;

import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.Format;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
//import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yueysjh extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		System.out.println(getChange());
		visit.getExtGrid1().Save(getChange(), visit);
		setMsg("����ɹ�!");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
	}

	// ����ͬ��
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		long intMonth1;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		intMonth1=intMonth;
		// ���·���1ʱ�ϸ�����ʾ12
		if (intMonth - 1 == 0) {
			intMonth = 12;
			intyear = intyear - 1;
		} else {
			intMonth = intMonth - 1;
		}
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		String StrMonth1="";
		if (intMonth < 10) {
			StrMonth1 = "0" + intMonth1;
			StrMonth = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth1;
			StrMonth = "" + intMonth;
		}

		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
//					+ ")";
			str = "  and (d.fuid=  " +this.getTreeid()+" or d.shangjgsid="+this.getTreeid()+")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and d.id = " + getTreeid() + "";

		}

		String copyData = "select y.id as id,y.riq as riq,(select mingc from diancxxb d where d.id = y.diancxxb_id ) as diancxxb_id,\n"
				+ "(select mingc from gongysb g where g.id = y.gongysb_id) as gongysb_id,\n"
				+ "(select mingc from yunsfsb yun where yun.id = y.yunsfsb_id) as yunsfsb_id,\n"
				+ "(select mingc from pinzb p where p.id = y.pinzb_id) as pinzb_id,\n"
				+ "(select mingc from chezxxb c where c.id = y.faz_id) as faz_id,\n"
				+ "(select mingc from chezxxb c where c.id = y.daoz_id) as daoz_id,\n"
				+ "ches,duns,y.beiz\n"
				+ "from yueysjh y,vwdianc d"
				+ " where y.diancxxb_id=d.id and to_char(y.riq,'yyyy-mm')='"
				+ intyear
				+ "-"
				+ StrMonth + "'  \n" + "    " + str + "  " + "   order by y.id";

		// System.out.println("����ͬ�ڵ�����:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData);
		while (rslcopy.next()) {
			String diancxxb_id = rslcopy.getString("diancxxb_id");
			String gongysb_id = rslcopy.getString("gongysb_id");
			String yunsfsb_id = rslcopy.getString("yunsfsb_id");
			Date riq = rslcopy.getDate("riq");
			String faz_id = rslcopy.getString("faz_id");
			String daoz_id = rslcopy.getString("daoz_id");
			String pinzb_id = rslcopy.getString("pinzb_id");
			long ches = rslcopy.getLong("ches");
			long duns = rslcopy.getLong("duns");
			String beiz = rslcopy.getString("beiz");

			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			con.getInsert(

			"insert into yueysjh\n" + "  (id,\n" + "   riq,\n"
					+ "   diancxxb_id,\n" + "   gongysb_id,\n"
					+ "   yunsfsb_id,\n" + "   pinzb_id,\n" + " faz_id,\n"
					+ "   daoz_id,\n" + "   ches,\n" + "   duns,\n"
					+ "   beiz)\n" + "values\n" + "  (  " +

					_id + "," + "to_date('" + intyear +"-"+StrMonth1
					+ "-01','yyyy-mm-dd')"
					+ ",(select id from diancxxb where mingc like '%" + diancxxb_id
					+ "%'),(select id from gongysb where mingc like '%" + gongysb_id
					+ "%'),(select id from yunsfsb where mingc like '%" + yunsfsb_id
					+ "%'),(select id from pinzb where mingc = '" + pinzb_id
					+ "'),(select id from chezxxb where mingc like '%" + faz_id
					+ "%'),(select id from chezxxb where mingc like '%" + daoz_id
					+ "%')," + ches + "," + duns + ",'" + beiz + "')");

		}
		getSelectData(null);
		con.Close();
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		// -----------------------------------
		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
//					+ ")";
			str = "  and (d.fuid=  " +this.getTreeid()+" or d.shangjgsid="+this.getTreeid()+")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and d.id = " + getTreeid() + "";

		}
		if (rsl == null) {
			String chaxun = "select y.id as id,y.riq as riq,d.mingc as diancxxb_id,\n"
					+ "(select mingc from gongysb g where g.id = y.gongysb_id) as gongysb_id,\n"
					+ "(select mingc from yunsfsb yun where yun.id = y.yunsfsb_id) as yunsfsb_id,\n"
					+ "(select mingc from pinzb p where p.id = y.pinzb_id) as pinzb_id,\n"
					+ "(select decode(mingc,'','��',mingc) from chezxxb c where c.id = y.faz_id) as faz_id,\n"
					+ "(select decode(mingc,'','��',mingc) from chezxxb c where c.id = y.daoz_id) as daoz_id,\n"
					+ "ches,duns,y.beiz\n"
					+ "from yueysjh y,diancxxb d "
					+ " where y.diancxxb_id=d.id and to_char(y.riq,'yyyy-mm')='"
					+ intyear
					+ "-"
					+ StrMonth
					+ "'  \n"
					+ "    "
					+ str
					+ "  "
					+ "   order by y.id";

			// System.out.println(chaxun);
			rsl = con.getResultSetList(chaxun);
		}
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yueysjh");

		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("gongysb_id").setHeader("������λ");
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("faz_id").setHeader("��վ");
		egu.getColumn("daoz_id").setHeader("��վ");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("duns").setHeader("����");
		egu.getColumn("beiz").setHeader("��ע");

		// ���ò��ɱ༭����ɫ
		// egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext';
		// return value;}");

		// �趨�г�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("gongysb_id").setWidth(180);
		egu.getColumn("diancxxb_id").setWidth(180);
		egu.getColumn("yunsfsb_id").setWidth(120);
		egu.getColumn("faz_id").setWidth(80);
		egu.getColumn("daoz_id").setWidth(80);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("ches").setDefaultValue("0");
		egu.getColumn("duns").setWidth(80);
		egu.getColumn("duns").setDefaultValue("0");
		egu.getColumn("beiz").setWidth(80);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);// ���÷�ҳ
//		egu.setWidth(1020);// ����ҳ��Ŀ��,������������ʱ��ʾ������
		
		// *****************************************����Ĭ��ֵ****************************
		// �糧������
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu
					.getColumn("diancxxb_id")
					.setComboEditor(
							egu.gridId,
							new IDropDownModel(
									"select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where (fuid="
									+ getTreeid() + " or shangjgsid ="+getTreeid()+") order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where id="
									+ getTreeid() + " order by mingc"));
			ResultSetList r = con
					.getResultSetList("select id,mingc from diancxxb where id="
							+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		}

		// ���õ糧Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
		// �������ڵ�Ĭ��ֵ,
		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// *************************������*****************************************88
		// ���ù�Ӧ�̵�������
//		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		/*
		 * //��糧������Ĺ�Ӧ�� String GongysSql = "select g.id,g.mingc from diancxxb
		 * d,gongysdcglb gd,gongysb g\n" + "where gd.diancxxb_id=d.id\n" + "and
		 * gd.gongysb_id=g.id\n" + "and d.id="+visit.getDiancxxb_id();
		 */
		String GongysSql = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		// ���÷�վ������
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		egu.getColumn("faz_id").setDefaultValue("  ");
		// egu.getColumn("faz_id").setEditor(null);
		cb_faz.setEditable(true);
		String fazSql = "select id ,mingc from chezxxb c where c.leib='��վ' order by c.mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		egu.getColumn("faz_id").setReturnId(true);
		// ���õ�վ������
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		egu.getColumn("daoz_id").setDefaultValue("  ");
		cb_daoz.setEditable(true);

		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		egu.getColumn("daoz_id").setReturnId(true);
		// �������䷽ʽ
		ComboBox cb_ys = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(cb_ys);
		cb_ys.setEditable(true);
		String ysSql = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ysSql));
		egu.getColumn("yunsfsb_id").setReturnId(true);
		// ����Ʒ��������
		ComboBox c5 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);

		String pinzSql = "select id,mingc from pinzb order by mingc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		egu.getColumn("pinzb_id").setReturnId(true);
		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("�·�:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// �趨�������������Զ�ˢ��
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		GridButton gb_insert = new GridButton(GridButton.ButtonType_Insert, egu.gridId,
				egu.getGridColumns(), null);
				gb_insert.setId("INSERT");
				egu.addTbarBtn(gb_insert);
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);

		if (showBtn) {
//			egu.addToolbarItem("{"
//				+ new GridButton("����ͬ�ڼƻ�",
//				"function(){document.getElementById('CopyButton').click();}")
//				.getScript() + "}");
			
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("����ǰ������", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
//		//��Ӧ��ú��վ����
//		egu
//		.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
//						+ "row = irow; \n"
//						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
//						+ "gongysTree_window.show();}});\n");
//		// egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){alert();if(e.Field=='MEIKXXB_ID'){e.record.set('YUANMKDW',e.value)}});\n");
//		egu.setDefaultsortable(false);

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		String sb_str=
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"rec=gridDiv_ds.getAt(e.row);\n" + 
			"var falge=0;\n" + 
			"var diancxxb_id=rec.get('DIANCXXB_ID');\n"+
			"var gonghdw=rec.get('GONGYSB_ID');\n" + 
			"var pinzb=rec.get('PINZB_ID');\n" + 
			"var yunsfsb=rec.get('YUNSFSB_ID');\n" + 
			"var faz_id=rec.get('FAZ_ID');\n"+
			"var daoz_id=rec.get('DAOZ_ID');\n"+
			"var rows=gridDiv_ds.getTotalCount();\n" + 
			"for (var i=0;i<rows;i++){\n" + 
			"var rec1=gridDiv_ds.getAt(i);\n" + 
			"var diancxxb_id1=rec1.get('DIANCXXB_ID');\n"+
			"var gonghdw1=rec1.get('GONGYSB_ID');\n" + 
			"var pinzb1=rec1.get('PINZB_ID');\n" + 
			"var yunsfsb1=rec1.get('YUNSFSB_ID');\n" + 
			"var faz_id1=rec1.get('FAZ_ID');\n"+
			"var daoz_id1=rec1.get('DAOZ_ID');\n"+
			"if(i==e.row){\n" + 
			"continue;\n" + 
			"}else if (gonghdw==gonghdw1 && diancxxb_id==diancxxb_id1 && pinzb==pinzb1 && yunsfsb==yunsfsb1 && faz_id==faz_id1 && daoz_id==daoz_id1){\n" + 
			"falge=1;\n Ext.MessageBox.alert('��ʾ��Ϣ',\"��¼����������\"+(i+1)+\"������������ȫ��ͬ�������޸����ݣ�\");\n break;\n" + 
			"}else{\n" + 
			" continue;\n" + 
			"}\n" + 
			"}\n" + 
			"if(falge==1){\n" +
			"Ext.getCmp(\"SAVE\").setDisabled(true) ;\n" + 
			"Ext.getCmp(\"INSERT\").setDisabled(true) ;\n" +
			"}else{\n" + 
			"Ext.getCmp(\"SAVE\").setDisabled(false) ;\n" + 
			"Ext.getCmp(\"INSERT\").setDisabled(false) ;\n" +
			"}"+
			"});";

		StringBuffer sb = new StringBuffer(sb_str);
		egu.addOtherScript(sb.toString());
		// ---------------ҳ��js�������--------------------------
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();

		}
		getSelectData(null);

	}

	// ���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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

	// �·�
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// �õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
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
