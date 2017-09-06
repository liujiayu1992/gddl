package com.zhiren.jt.jihgl.ranmcgys;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ranmcgyswh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
		// setTbmsg("");
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

		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) getPage().getVisit();
		long diancxxb_id = Long.parseLong(this.getTreeid());
		//
		ResultSetList rr = null;
		String ss = "";
		String caigl = "0";
		String yuns = "0";
		String daocrz = "0";
		String chukjg = "0";
		String yunf = "0";
		String zaf = "0";
		String qitfy = "0";
		String daoczhjhs = "0";
		String daoczhjbhs = "0";
		String daocbmdjbhs = "0";
		String quanndhl = "0";
		String hetl = "0";
		String hetckjg = "0";
		String hetrz = "0";
		String daohl = "0";
		String meiqf = "�г��ɹ�";

		ResultSetList rsl = getExtGrid().getModifyResultSet(
				getChange().replaceAll("&nbsp;", ""));// ��getChange�е�&nbsp;�滻��"",����ext����ʶ��
		StringBuffer sbb = new StringBuffer();
		sbb.append("begin  \n");
		while (rsl.next()) {
			String sql = "select id from gongysb where mingc='"
					+ rsl.getString("mingc") + "'";
			rr = con.getResultSetList(sql);
			int it=0;
			if (rr.next()) {
				ss = rr.getString("ID");
			}
			if (!"".equals(rsl.getString("caigl"))) {
				caigl = rsl.getString("caigl");
			}
			if (!"".equals(rsl.getString("yuns"))) {
				yuns = rsl.getString("yuns");
			}
			if (!"".equals(rsl.getString("daocrz"))) {
				daocrz = rsl.getString("daocrz");
			}
			if (!"".equals(rsl.getString("chukjg"))) {
				chukjg = rsl.getString("chukjg");
			}
			if (!"".equals(rsl.getString("yunf"))) {
				yunf = rsl.getString("yunf");
			}
			if (!"".equals(rsl.getString("zaf"))) {
				zaf = rsl.getString("zaf");
			}
			if (!"".equals(rsl.getString("qitfy"))) {
				qitfy = rsl.getString("qitfy");
			}
			if (!"".equals(rsl.getString("daoczhjhs"))&&!"NaN".equals(rsl.getString("daoczhjhs"))) {
				daoczhjhs = rsl.getString("daoczhjhs");
			}
			if (!"".equals(rsl.getString("daoczhjbhs"))) {
				daoczhjbhs = rsl.getString("daoczhjbhs");
			}
			if (!"".equals(rsl.getString("daocbmdjbhs"))&&!"NaN".equals(rsl.getString("daocbmdjbhs"))) {
				daocbmdjbhs = rsl.getString("daocbmdjbhs");
			}
			if (!"".equals(rsl.getString("quanndhl"))) {
				quanndhl = rsl.getString("quanndhl");
			}
			if (!"".equals(rsl.getString("hetl"))&&!"NaN".equals(rsl.getString("hetl"))) {
				hetl = rsl.getString("hetl");
			}
			if (!"".equals(rsl.getString("hetckjg"))) {
				hetckjg = rsl.getString("hetckjg");
			}
			if (!"".equals(rsl.getString("hetrz"))) {
				hetrz = rsl.getString("hetrz");
			}
			if (!"".equals(rsl.getString("daohl"))) {
				daohl = rsl.getString("daohl");
			}
			if (!"".equals(rsl.getString("meiqf"))) {
				meiqf = rsl.getString("meiqf");
			}
			String sqll = "select id from jihkjb where mingc='"
					+ meiqf +"'";
			ResultSetList rsll = con.getResultSetList(sqll);
			if(rsll.next()){
				it = Integer.parseInt(rsll.getString("ID"));
			}
			
			

			if ("0".equals(rsl.getString("id"))) {
//				System.out.println(rsl.getString("meiqf"));
//				System.out.println(it);
				// ��Ϊ���������Բ������
//				 if(rsll.getRows()==0){
									
				 sbb.append("insert into ranmcgysb (id,caigl,"+
				 "yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs,diancxxb_id,gongysb_id,nianf,jihkjb_id,quanndhl,hetl,hetckjg,hetrz,daohl,beiz"+
				" ) values("+"getNewId(getDiancId('"+diancxxb_id+"')),"
				 +caigl+","+yuns+","+daocrz+","+chukjg+","+yunf+","+zaf+","+qitfy+","
				 +daoczhjhs+","+daoczhjbhs+","+daocbmdjbhs+","+diancxxb_id+","+ss+","+"to_date('"+getNianfValue()+"','yyyy')"+","+it+","+quanndhl+","+hetl+","+hetckjg+","+hetrz+","+daohl+",'"+rsl.getString("beiz")+"')"
				 +";\n");
//				 }else{
//				 setMsg("");
//				 }
			} else {

				sbb.append("update ranmcgysb set caigl=" + caigl + ", yuns ="
						+ yuns + ",daocrz=" + daocrz + ",chukjg=" + chukjg
						+ ",yunf=" + yunf + ",zaf=" + zaf + ",qitfy=" + qitfy
						+ ",daoczhjhs=" + daoczhjhs + ",daoczhjbhs="
						+ daoczhjbhs + ",daocbmdjbhs=" + daocbmdjbhs
						+ ",diancxxb_id=" + diancxxb_id + ",gongysb_id=" + ss
						+ ",nianf=" + "to_date(" + getNianfValue() + ",'yyyy')"
						+ ",jihkjb_id=" + it + ",quanndhl=" + quanndhl
						+ ",hetl=" + hetl + ",hetckjg=" + hetckjg + ",hetrz="
						+ hetrz + ",daohl=" + daohl + ",beiz='"
						+ rsl.getString("beiz") + "'  where id="
						+ rsl.getString("ID") + ";\n");
			}

		}
		// ɾ������
		rsl = visit.getExtGrid1().getDeleteResultSet(
				getChange().replaceAll("&nbsp;", ""));
		while (rsl.next()) {
			if (!"".equals(rsl.getString("ID"))) {

				sbb.append("delete from ranmcgysb where id="
						+ rsl.getString("ID") + ";\n");
			}
		}
		sbb.append("end;");
		if (sbb.toString().length() > 13) {

			con.getUpdate(sbb.toString());
		}
		con.commit();
		// ---------------------
		con.Close();

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _ShengcButton = false;
//	��Ӧ����ͬ�ڰ�ť
	public void ShengcButton(IRequestCycle cycle) {
		_ShengcButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;

			getSelectData();
		}
		if (_ShengcButton) {
			_ShengcButton = false;
			fuz();
		}

	}
//	����ͬ��
	public void fuz() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int nian=Integer.parseInt(getNianfValue().getValue());
		Visit visit = (Visit) getPage().getVisit();
		String sql = "select * from ranmcgysb r where to_char(r.nianf,'yyyy')="
				+ (nian-1) + " and r.diancxxb_id=" + getTreeid();
		StringBuffer sbb = new StringBuffer("begin \n");
		ResultSetList rsl = null;
		ResultSetList rslj = null;
		rsl = con.getResultSetList(sql);
		String ssq="select id from ranmcgysb r where to_char(r.nianf,'yyyy')="
				+ getNianfValue() + " and r.diancxxb_id=" + getTreeid();
		rslj = con.getResultSetList(ssq);
		
		while(rslj.next()){
			
			sbb.append("delete from ranmcgysb r where id="+rslj.getString("id")+";\n");
		}
		while (rsl.next()) {

			sbb
					.append("insert into ranmcgysb(id,caigl,diancxxb_id, gongysb_id, yuns,"
							+ " daocrz, chukjg, yunf, zaf, qitfy, daoczhjhs, daoczhjbhs, daocbmdjbhs,"
							+ " quanndhl, hetl, daohl, hetckjg, hetrz, nianf, jihkjb_id, beiz) values("
							+ "getNewId(getDiancId('"
							+ getTreeid()
							+ "')),"
							+ rsl.getString("caigl")+","
							+ rsl.getString("diancxxb_id")+","
							+ rsl.getString("gongysb_id")+","
							+ rsl.getString("yuns")+","
							+ rsl.getString("daocrz")+","
							+ rsl.getString("chukjg")+","
							+ rsl.getString("yunf")+","
							+ rsl.getString("zaf")+","
							+ rsl.getString("qitfy")+","
							+ rsl.getString("daoczhjhs")+","
							+ rsl.getString("daoczhjbhs")+","
							+ rsl.getString("daocbmdjbhs")+","
							+ rsl.getString("quanndhl")+","
							+ rsl.getString("hetl")+","
							+ rsl.getString("daohl")+","
							+ rsl.getString("hetckjg")+","
							+ rsl.getString("hetrz")+","
							+ "to_date('"
							+ nian
							+ "','yyyy')"+","
							+ rsl.getString("jihkjb_id")+",'"
							+ rsl.getString("beiz") + "');\n");
		}
		sbb.append("end;");
		if (sbb.toString().length() > 13) {

			con.getUpdate(sbb.toString());
		}
		con.commit();
		// ---------------------
		con.Close();

	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		boolean flage = visit.getboolean7();

		// -----------------------------------
		String str = "";

		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "  and (dc.fuid=  " + this.getTreeid() + " or dc.shangjgsid="
					+ this.getTreeid() + ")";

		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + "";

		}
		// ---------------------------------------------------------------------

		String cha = "select r.id, g.mingc,j.mingc meiqf ,r.caigl,r.yuns,r.daocrz,r.chukjg,r.yunf,r.zaf,r.qitfy,r.daoczhjhs,r.daoczhjbhs,r.daocbmdjbhs,r.quanndhl,r.hetl,r.hetckjg,r.hetrz,r.daohl,r.beiz"
				+ "  from ranmcgysb r ,gongysb g,jihkjb j "
				+ "where r.gongysb_id=g.id and r.jihkjb_id=j.id and r.nianf= (to_date("
				+ getNianfValue()
				+ ",'yyyy')) and r.diancxxb_id="
				+ getTreeid()+"order by meiqf";
		// System.out.println(cha);
		ResultSetList rsl = con.getResultSetList(cha);

		if (rsl.getRows() >= 1) {// ������

			// ���ư�ť�û�
			flage = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("ranmzbsjb");
		egu.setWidth("bodyWidth");
		egu.getColumn("mingc").setCenterHeader("��Ӧ��");
		egu.getColumn("caigl").setCenterHeader("�ɹ���<br>���");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("yuns").setCenterHeader("����<br>���");

		egu.getColumn("daocrz").setCenterHeader("������ֵ<br>KJ/kg");
		egu.getColumn("chukjg").setCenterHeader("����۸�<br>Ԫ/��");
		egu.getColumn("yunf").setCenterHeader("�˷�<br>Ԫ/��");
		egu.getColumn("zaf").setCenterHeader("�ӷ�<br>Ԫ/��");
		egu.getColumn("qitfy").setCenterHeader("��������<br>Ԫ/��");

		egu.getColumn("daoczhjhs").setCenterHeader("�����ۺϼ۸�<br>(��˰)<br>Ԫ/��");
//		egu.getColumn("daoczhjhs").setCenterHeader("������ú����<br>(��˰)<br>Ԫ/��");
		egu.getColumn("daoczhjbhs").setCenterHeader("�����ۺϼ۸�<br>(����˰)<br>Ԫ/��");
		((NumberField)egu.getColumn("daocbmdjbhs").editor).setDecimalPrecision(3);
		egu.getColumn("daocbmdjbhs").setCenterHeader("������ú����<br>(����˰)<br>Ԫ/��");
		egu.getColumn("quanndhl").setCenterHeader("ȫ�굽����<br>���");
		egu.getColumn("hetl").setCenterHeader("��ͬ��<br>���");
		((NumberField)egu.getColumn("daohl").editor).setDecimalPrecision(3);
		egu.getColumn("daohl").setCenterHeader("������<br>%");
		egu.getColumn("hetckjg").setCenterHeader("��ͬ����۸�<br>���");
		egu.getColumn("hetrz").setCenterHeader("��ͬ��ֵ<br>KJ/kg");
		egu.getColumn("meiqf").setCenterHeader("�ƻ��ھ�");
		egu.getColumn("beiz").setCenterHeader("��ע");

//		egu.setDefaultsortable(false);// �趨ҳ�治�Զ�����

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(30);// ���÷�ҳ
//		�趨�е�С��λ
		((NumberField)egu.getColumn("daocbmdjbhs").editor).setDecimalPrecision(3);

		// *************************������*****************************************88
		// ���ù�Ӧ�̵�������
		ComboBox cb_gongys = new ComboBox();
		egu.getColumn("mingc").setEditor(cb_gongys);
		cb_gongys.setEditable(true);
		String GongysSql = "select id,mingc from gongysb where leix = 0 order by mingc";
		egu.getColumn("mingc").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		// egu.getColumn("mingc").setReturnId(true);

		// ����
		ComboBox cb_mqf = new ComboBox();
		egu.getColumn("meiqf").setEditor(cb_mqf);
		String MeikqfSql = "select id,mingc from jihkjb ";
		cb_gongys.setEditable(true);
		egu.getColumn("meiqf").setComboEditor(egu.gridId,
				new IDropDownModel(MeikqfSql));
		egu.getColumn("meiqf").setReturnId(true);

		// ********************������************************************************

		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

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
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		// �趨�������������Զ�ˢ��
		egu.addTbarText("-");// ���÷ָ���

//		if (!flage) {
			// ���ɰ�ť
			StringBuffer rsb1 = new StringBuffer();
			rsb1.append("function (){").append(
//					+ "var Mrcd = gridDiv_grid.getStore().getModifiedRecords(); \n"
//					+ "		if(Mrcd.length > 0) { \n"
					 "    		Ext.MessageBox.confirm('��ʾ��Ϣ','���������Ԥ�����ݽ���ɾ�����Ƿ������',"
					+ "    		function(btn){if(btn=='yes'){"
//					+ "		}"
					+
					"document.getElementById('ShengcButton').click();}});}");
			GridButton gbr1 = new GridButton("��������", rsb1.toString());

			egu.addTbarBtn(gbr1);
//		}
		
		
		//���
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				MainGlobal.getExtMessageBox(
						"'����ˢ��'+Ext.getDom('NIANF').value+'�������,���Ժ�'", true))
				.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");// ���÷ָ���
		// ɾ����ť
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");// ���÷ָ���

		// ���水ť
		String handler = ""
//			+"function (){ \n" 
			+ " var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();\n"
//				+ "var Mrcd = gridDiv_grid.getStore().getModifiedRecords(); \n"
		+"for(var i=0;i<Mrcd.length;i++){ for(var j=i+1;j<Mrcd.length;j++){ " +
				"" +
				"" +
				"if((Mrcd[i].get('MINGC') == Mrcd[j].get('MINGC')) && (Mrcd[i].get('MEIQF') == Mrcd[j].get('MEIQF'))){Ext.MessageBox.alert('��ʾ��Ϣ','��Ӧ�̺ͼƻ��ھ������ظ���');return;}  }  }"
//		"	}\n"
		;

		// ����
		egu.addToolbarButton(GridButton.ButtonType_Save_condition,
				"SaveButton", handler);

		egu.addTbarText("->");

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// �趨ĳһ�в��ܱ༭
		sb.append("if(e.row==0&& e.column==11){e.cancel=true;}");//
		sb.append("if(e.column==13){e.cancel=true;}");//
		sb.append("if(e.column==18){e.cancel=true;}");//

		sb.append("});");

		sb.append("gridDiv_grid.on('afteredit',function(e){");
		// ���㷢��ԭú�� Round((eval(||0)*1000)*/1000/1000,0)
		String ss = ""
				+ "    var daoczhj_lj=0,biaomdj_lj=0,buhsbmdj_lj=0,j=e.row;\n"
				+

				"    daoczhj_lj=eval(gridDiv_ds.getAt(j).get('CHUKJG'))+eval(gridDiv_ds.getAt(j).get('YUNF'))+eval(gridDiv_ds.getAt(j).get('ZAF'))+eval(gridDiv_ds.getAt(j).get('QITFY'));\n"
				+"    biaomdj_lj=Round_new(eval(gridDiv_ds.getAt(j).get('DAOCZHJBHS'))*29271/eval(gridDiv_ds.getAt(j).get('DAOCRZ')),2);\n" +
				"   buhsbmdj_lj=Round_new(eval(gridDiv_ds.getAt(j).get('QUANNDHL'))*100/(0.0000000000000000001+eval(gridDiv_ds.getAt(j).get('HETL'))),2);\n"+
				"   gridDiv_ds.getAt(j).set('DAOCBMDJBHS',biaomdj_lj);\n" +"   gridDiv_ds.getAt(j).set('DAOHL',buhsbmdj_lj);\n" +
				"   gridDiv_ds.getAt(j).set('DAOCZHJHS',daoczhj_lj);\n";

		sb.append(ss);
//		N7/(O7+0.0000000000000000001)*100
		sb.append("});");

		egu.addOtherScript(sb.toString());
		// ---------------ҳ��js�������--------------------------
//		egu.defaultsortable=false;
		egu.setDefaultsortable(false);
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
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setString2("");
			visit.setboolean7(false);
		}

		getSelectData();

	}

	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}

	// ���
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	// �·�

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

	private String treeid;

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