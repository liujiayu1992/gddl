package com.zhiren.dc.hesgl.changnzf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;

/**
 * @author ������
 * 2009-06-11
 * ������Zafjsxz(�ӷѽ���ѡ��)
 */

public class Zafjsxz extends BasePage {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	����ʼ����
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	�󶨽�������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
//	��Ӧ��������
	public IDropDownBean getGongysValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getGongysModel().getOptionCount()>0) {
				setGongysValue((IDropDownBean)getGongysModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysValue(IDropDownBean gongysValue) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(gongysValue);
	}
	
	public IPropertySelectionModel getGongysModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setGongysModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setGongysModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setGongysModels() {
		String str = "select 0 id, 'ȫ��' mingc from dual union select id, mingc " +
				"from gongysb where leix=1 order by id";
		setGongysModel(new IDropDownModel(str));
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _JiesChick = false;

	public void JiesButton(IRequestCycle cycle) {
		_JiesChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick){
			_Refreshclick=false;
			getSelectData();
		}
		if (_JiesChick) {
			_JiesChick = false;
			GotoJiesd(cycle);
		}
	}
	
	Calendar temp1; // ������ʱ�������ڱȽ���ʼ���÷�Χ�ͽ�ֹ���÷�Χ
	Calendar temp2;
	public void GotoJiesd(IRequestCycle cycle) {
		
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = this.getExtGrid();
		ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
		ResultSetList mdrsl1 = egu.getModifyResultSet(this.getChange());
		Visit visit = (Visit)this.getPage().getVisit();
		
		SimpleDateFormat sdfbm = new SimpleDateFormat("yyyyMMdd");
		String bmsql = "select max(bianm) as bianm from zafjsb where bianm like '"+ sdfbm.format(new Date()).substring(0, 6) +"___'";
		ResultSetList bmrsl = con.getResultSetList(bmsql);
		
		String bianm = "";
		while(bmrsl.next()) {
			if (bmrsl.getString("bianm").equals("")) {
				bianm = sdfbm.format(new Date()).substring(0, 6) + "001";
			} else {
				bianm = (Integer.parseInt(bmrsl.getString("bianm"))+1)+"";
			}
		}
		visit.setString12(bianm);
		
		StringBuffer sbsql = new StringBuffer();
		while(mdrsl1.next()) {
			sbsql.append(mdrsl1.getString("id")).append(",");
		}
		visit.setString11(sbsql.substring(0, sbsql.length()-1));

		String sql = "select distinct gongysb_id from changnzfb where id in(" + sbsql.substring(0, sbsql.length()-1)  + ")";
		ResultSetList rsl = con.getResultSetList(sql);
		con.Close();

		double sumShul = 0.0; // �����ܺ�
		double sumFeiyhj = 0.0; // ���úϼ��ܺ�
		double sumBuhszf = 0.0; // ����˰�ӷ��ܺ�
		int mark = 0; // ��һ��ѭ���Ƚ���ʼ���÷�Χ�ͽ�ֹ���÷�Χ�ı��
		StringBuffer strsb = new StringBuffer();
		
		while(mdrsl.next()) {
			
//			��ȡ��Ӧ�����ƣ����ѡ��ļ�¼�Ĺ�Ӧ����ͬ���ڽ��㵥��ʾ��������ʾ��
			if (rsl.getRows() == 1) {
				visit.setString1(mdrsl.getString("gysmc"));
			} else {
				visit.setString1("");
			}
			
//			��ȡѡ���¼�������ֶε��ܺ�
			sumShul = sumShul + Double.parseDouble(mdrsl.getString("shul"));
			visit.setDouble2(sumShul);
			
//			��ȡѡ���¼�ķ��úϼ��ֶε��ܺ�
			sumFeiyhj = sumFeiyhj + Double.parseDouble(mdrsl.getString("feiyhj"));
			visit.setDouble6(sumFeiyhj);
			
//			��ȡѡ���¼�Ĳ���˰�ӷѵ��ܺ�(����˰�ӷ�=���úϼ�*(1 - ˰��))
			sumBuhszf = sumBuhszf +  Double.parseDouble(mdrsl.getString("feiyhj")) * (1.0 - Double.parseDouble(mdrsl.getString("shuil")));
			visit.setDouble4(Math.round(sumBuhszf * 100)/100.0);
			
//			��ȡ�տλ���Ʒŵ�Visit��
			visit.setString7(mdrsl.getString("quanc"));
			
//			��ȡ�տλID
			visit.setString13(mdrsl.getString("shoukdw_id"));
			
//			��ȡ��ѡ��¼����ʼ���÷�Χ�ͽ�ֹ���÷�Χ�����ѡ�������¼��ô��ȡ��С����ʼ���÷�Χ�����Ľ�ֹ���÷�Χ
			if(mdrsl.getRows() == 1) {
				visit.setString8(mdrsl.getString("qiszyfw"));
				visit.setString9(mdrsl.getString("jiezzyfw"));
			} else {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					c1.setTime(sdf.parse(mdrsl.getString("qiszyfw")));
					c2.setTime(sdf.parse(mdrsl.getString("jiezzyfw")));
					if (mark == 0) { // ��һ��ѭ�����Ƚ���ʼ���÷�Χ�ͽ�ֹ���÷�Χ��ֻ�����Ƿŵ��������Թ��´�ѭ���Ƚ�
						temp1 = c1;
						temp2 = c2;
					} else {
						if (temp1.before(c1)) {
							visit.setString8(sdf.format(temp1.getTime()));
						} else {
							visit.setString8(sdf.format(c1.getTime()));
							temp1 = c1;
						}
						if (temp2.after(c2)) {
							visit.setString9(sdf.format(temp2.getTime()));
						} else {
							visit.setString9(sdf.format(c2.getTime()));
							temp2 = c2;
						}
					}
					mark ++;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
//			��ȡ��ѡ��¼�ı�ע��Ϣ�����ѡ�������¼��ô�ѱ�ע��Ϣ����һ��
			if (mdrsl.getRows() == 1) {
				visit.setString10(mdrsl.getString("beiz"));
			} else {
				strsb.append(mdrsl.getString("beiz")).append(";");
				visit.setString10(strsb.substring(0, strsb.length() - 1));
			}
		}
		
//		��ȡѡ���¼�ĵ��۲�������Visit�У����ѡ����Ƕ�����¼��ô����=���úϼ�/��������
		visit.setDouble3(Math.round(sumFeiyhj/sumShul*100)/100.0);
		
//		��ȡ�ӷ�˰�������Visit�У��ӷ�˰��=���úϼ�-����˰�ӷ�
		visit.setDouble5(Math.round((sumFeiyhj - (Math.round(sumBuhszf * 100)/100.0)) * 100) / 100.0);
		
		cycle.activate("Zafjs");
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String gysName = getGongysValue().getValue();
		String briq = getBRiq();
		String eriq = getERiq();
		if (!gysName.equals("ȫ��")) {
			gysName = " and gys.mingc = " + "'" + gysName + "'";
		} else {
			gysName = "";
		}
		
		String zfjsSql = new String();
		
		zfjsSql = 
			"select to_char(c.id) id,\n" +
			"       to_char(c.riq, 'yyyy-MM-dd') riq,\n" + 
			"       gys.mingc as gysmc,\n" + 
			"       i1.mingc as fymc,\n" + 
			"       i2.mingc as fylb,\n" + 
			"       s.quanc,\n" + 
			"       to_char(s.id) shoukdw_id,\n" + 
			"       to_char(c.qiszyfw, 'yyyy-MM-dd') qiszyfw,\n" + 
			"       to_char(c.jiezzyfw, 'yyyy-MM-dd') jiezzyfw,\n" + 
			"       to_char(c.shuil) shuil,\n" + 
			"       to_char(c.danj) danj,\n" + 
			"       to_char(c.shul) shul,\n" + 
			"       to_char(c.feiyhj) feiyhj,\n" + 
			"       c.beiz\n" + 
			"  from changnzfb c, gongysb gys, item i1, item i2, shoukdw s\n" + 
			" where c.gongysb_id = gys.id\n" + 
			"   and c.feiyxm_item_id = i1.id\n" + 
			"   and c.feiylb_item_id = i2.id\n" + 
			"   and c.shoukdw_id = s.id\n" + gysName +
			"   and c.riq between to_date('"+ briq +"', 'yyyy-MM-dd') and\n" + 
			"       to_date('"+ eriq +"', 'yyyy-MM-dd')\n" + 
			"	and c.diancxxb_id = " + getTreeid() + "\n" +
			"   and c.zafjsb_id = 0\n" + 
			"union\n" + 
			"select '' id,\n" + 
			"       '�ϼ�' riq,\n" + 
			"       '' gysmc,\n" + 
			"       '' fymc,\n" + 
			"       '' fylb,\n" + 
			"       '' quanc,\n" + 
			"       '' shoukdw_id,\n" + 
			"       '' qiszyfw,\n" + 
			"       '' jiezzyfw,\n" + 
			"       '' shuil,\n" + 
			"       '' danj,\n" + 
			"       '' shul,\n" + 
			"       '' feiyhj,\n" + 
			"       '' beiz\n" + 
			"  from dual\n" + 
			" where exists\n" + 
			" (select *\n" + 
			"          from changnzfb c, gongysb gys, item i1, item i2, shoukdw s\n" + 
			"         where c.gongysb_id = gys.id\n" + 
			"           and c.feiyxm_item_id = i1.id\n" + 
			"           and c.feiylb_item_id = i2.id\n" + 
			"           and c.shoukdw_id = s.id\n" + 
			"           and c.riq between to_date('"+ briq +"', 'yyyy-MM-dd') and\n" + 
			"               to_date('"+ eriq +"', 'yyyy-MM-dd')\n" + 
			"			and c.diancxxb_id = " + getTreeid() + "\n" +
			"           and c.zafjsb_id = 0)";
		
		ResultSetList rsl = con.getResultSetList(zfjsSql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("changnzfb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("riq").setHeader("¼������");
		egu.getColumn("gysmc").setHeader("��Ӧ��");
		egu.getColumn("fymc").setHeader("��������");
		egu.getColumn("fylb").setHeader("�������");
		egu.getColumn("quanc").setHeader("�տλ");
		egu.getColumn("quanc").setWidth(200);
		egu.getColumn("shoukdw_id").setHeader("�տλID");
		egu.getColumn("shoukdw_id").setHidden(true);
		egu.getColumn("qiszyfw").setHeader("��ʼ���÷�Χ");
		egu.getColumn("jiezzyfw").setHeader("��ֹ���÷�Χ");
		egu.getColumn("shuil").setHeader("˰��");
		egu.getColumn("danj").setHeader("����");
		egu.getColumn("shul").setHeader("����");
		egu.getColumn("feiyhj").setHeader("���úϼ�");
		egu.getColumn("beiz").setHeader("��ע");
		
//		�ڵ�2�м��븴ѡ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		
		egu.addTbarText("ѡ�����ڣ�");
		DateField bdf = new DateField();
		bdf.setValue(getBRiq());
		bdf.Binding("BRiq", "Form0");
		egu.addToolbarItem(bdf.getScript());
		
		egu.addTbarText("��");
		DateField edf = new DateField();
		edf.setValue(getERiq());
		edf.Binding("ERiq", "Form0");
		egu.addToolbarItem(edf.getScript());
		egu.addTbarText("-"); 
		
		egu.addTbarText("�糧��");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, ((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("��Ӧ�̣�");
		ComboBox comb = new ComboBox();
		comb.setWidth(100);
		comb.setListWidth(130);
		comb.setTransform("Gongys");
		comb.setId("Gongys");
		comb.setLazyRender(true);
		comb.setEditable(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		String rsb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("ˢ��", rsb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel_condition, "JiesButton",
				"var rec = gridDiv_grid.getSelectionModel().getSelections();						\n" +
				"	if (rec.length == 1 && rec[0].get('ID') == '') {								\n" +
				"		Ext.MessageBox.alert('��ʾ��Ϣ','û��ѡ��������Ϣ��');						\n" +
				"		return;																		\n" +
				"	}																				\n" +
				"	for (var i=0; i < rec.length; i++) {											\n" +
				"		if (rec[i].get('ID') == '') {												\n" +
				"			Ext.MessageBox.alert('��ʾ��Ϣ','����Ҫѡ��ϼƣ�');						\n" +
				"			return;																	\n" +
				"		}																			\n" +
				"	}																				\n" +
				"	for (var i=0; i < rec.length; i++){												\n" +
				"		if (i != rec.length - 1 && rec[i].get('QUANC') != rec[i+1].get('QUANC')){	\n" +
				"			Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���տλ��ͬ�ļ�¼��');				\n" +
				"			return;																	\n" +
				"		}																			\n" +
				"	}");
		
		egu.addOtherScript("gridDiv_grid.on('rowclick',function() {				   \n" +
				" var shul=0, feiyhj=0;                                            \n" +
				" var rec = gridDiv_grid.getSelectionModel().getSelections();	   \n" +
				" for(var i=0;i<rec.length;i++){                                   \n" +
				"     if(''!=rec[i].get('ID')){                                    \n" +
				"         shul += eval(rec[i].get('SHUL'));                        \n" +
				"         feiyhj += eval(rec[i].get('FEIYHJ'));                    \n" +
				"     }                                                            \n" +
				" }                                                                \n" +
				" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('SHUL',shul);      \n" +
				" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('FEIYHJ',feiyhj);  \n" +
				" });                                                              \n");
		
//		��ѡ��ȫѡ�����¼���ѡ����ĺϼ�ֵ
		egu.addOtherScript("gridDiv_grid.on('click',function(){		\n"
						+ "		reCountToolbarNum(this);			\n"
						+ "});		                                \n");

		egu.addOtherScript("function reCountToolbarNum(obj){	                            \n" +
				"var rec;	                                                                \n" +
				"var shul=0, feiyhj=0;	                                                    \n" +
				"rec = obj.getSelectionModel().getSelections();	                            \n" +
				"	for(var i=0;i<rec.length;i++){		                                    \n" +
				"		if(''!=rec[i].get('ID')){	                                        \n" +
				"			shul += eval(rec[i].get('SHUL'));	                            \n" +
				"			feiyhj += eval(rec[i].get('FEIYHJ'));	                        \n" +
				"		}	                                                                \n" +
				"	}	                                                                    \n" +
				"	if(gridDiv_ds.getCount()>0){	                                        \n" +
				"		gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('SHUL',shul);         \n" +
				"		gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('FEIYHJ',feiyhj); 	\n" +
				"	}					                                                    \n" +
				"}");
		
		egu.addPaging(0);
		setExtGrid(egu);
		con.Close();
	}
	
//	�糧��_��ʼ
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
//	�糧��_����
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			visit.setString1("");
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}