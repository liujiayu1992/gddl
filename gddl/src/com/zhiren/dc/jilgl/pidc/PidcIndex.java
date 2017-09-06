package com.zhiren.dc.jilgl.pidc;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class PidcIndex extends BasePage implements PageValidateListener {
//	�����û���ʾ
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
	}
//	������
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString1(riq);
	}
	
	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishClick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save(){
		if(getChange() == null || "".equals(getChange())){
			setMsg("û�иĶ������豣�棡");
			return;
		}
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
//		ɾ����������
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		String msg = "";
		
		sql = "begin\n";
		while(rsl.next()){
			if("�����".equals(rsl.getString("zhuangt"))){
				msg = "ɾ�������г��ִ�������˵�����δ��ɾ����";
				continue;
			}else{
				sql += "delete from chepb where fahb_id = "+ rsl.getString("id") + ";\n";
				sql += "delete from fahb where id = "+ rsl.getString("id") + ";\n";
			}
		}
		if(rsl.getRows()>0){
			sql += "end;";
			con.getDelete(sql);
		}
		rsl.close();
		
		sql = "select * from xitxxb where mingc = '����ȷ������' and zhi = '���' and leib = '����' and zhuangt = 1 and diancxxb_id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
		boolean daohsz_sh = con.getHasIt(sql); 
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		List fhlist = new ArrayList();
		sql = "begin\n";
		while(rsl.next()){
			String id = rsl.getString("id");
			long gysid = ((IDropDownModel)getGongysModel()).getBeanId(rsl.getString("gys"));
			long mkid = ((IDropDownModel)getMeikModel()).getBeanId(rsl.getString("mk"));
			long pzid = getExtGrid().getColumn("pz").combo.getBeanId(rsl.getString("pz"));
			long kjid = getExtGrid().getColumn("kj").combo.getBeanId(rsl.getString("kj"));
			String fahrq = rsl.getString("fahrq");
			double maoz = rsl.getDouble("maoz");
			double biaoz = rsl.getDouble("biaoz");
			double kuid = rsl.getDouble("kuid");
			if("0".equals(id)){
				int hedbz = SysConstant.HEDBZ_YJJ;
				id = MainGlobal.getNewID(v.getDiancxxb_id());
				long zhilid = Jilcz.getZhilbid(con, null, DateUtil.getDate(fahrq), v.getDiancxxb_id());
				Caiycl.CreatBianh(con,zhilid,v.getDiancxxb_id(),mkid,kjid,SysConstant.YUNSFS_Pidc,SysConstant.Chez_pdc);
				String strlursj = DateUtil.FormatOracleDateTime(new Date());
/**
 * huochaoyuan 
 * 2009-11-24Ƥ�����ֹ�¼��ʱ����ӱ������������bug��������TRIGGER_IUD_CHEPBӰ�죬��������˳���ϵͳ������
 * ���߼�ҲӦ��������chepb���ݣ����з��������ݣ�
 */				
				sql += "insert into chepb(id,xuh,piaojh, cheph, yuanmz, maoz, piz, yingd, yingk, biaoz, "
					+ "koud, kous, kouz, sanfsl, ches, jianjfs, guohb_id, "
					+ "fahb_id, chebb_id, qingcsj, lursj, lury, hedbz) values(\n"
					+ id + ",getChepxh(" + strlursj + "),getLiush(" + strlursj + "), 'Ƥ����'," + maoz + ","
					+ maoz + ",0," + (0 - kuid) + "," + (0 - kuid) + "," + biaoz + ",0,0,0,0,0,'Ƥ����',0," 
					+ id + ","  + SysConstant.CHEB_PDC + "," + DateUtil.FormatOracleDateTime(fahrq + " 23:59:59")
					+ "," + strlursj + ",'" + v.getRenymc() + "'," + hedbz + ");\n"; 
				sql += "insert into fahb (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id,\n"
					+ "pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, zhilb_id, yunsfsb_id,\n"
					+ "maoz, piz, jingz, yingd, yingk, biaoz, yunsl, koud, kous, kouz, zongkd, sanfsl, ches,\n"
					+ "yuandz_id, yuanshdwb_id,hedbz) values(\n" + id + "," + id + "," + v.getDiancxxb_id()
					+ "," + gysid + "," + mkid + "," + pzid + "," + SysConstant.Chez_pdc + ","
					+ SysConstant.Chez_pdc + "," + kjid + "," + DateUtil.FormatOracleDate(fahrq) 
					+ "," + (daohsz_sh?DateUtil.FormatOracleDate("2050-12-31"):DateUtil.FormatOracleDate(fahrq)) + "," + zhilid + ","
					+ SysConstant.YUNSFS_Pidc + "," + maoz + ",0," + maoz + "," + (0-kuid) + "," 
					+ (0-kuid) + "," + biaoz + ",0,0,0,0,0,0,1,"
					+ SysConstant.Chez_pdc + "," + v.getDiancxxb_id() + "," + hedbz + ");\n";
//				String strlursj = DateUtil.FormatOracleDateTime(new Date());
//				sql += "insert into chepb(id,xuh,piaojh, cheph, yuanmz, maoz, piz, yingd, yingk, biaoz, "
//					+ "koud, kous, kouz, sanfsl, ches, jianjfs, guohb_id, "
//					+ "fahb_id, chebb_id, qingcsj, lursj, lury, hedbz) values(\n"
//					+ id + ",getChepxh(" + strlursj + "),getLiush(" + strlursj + "), 'Ƥ����'," + maoz + ","
//					+ maoz + ",0," + (0 - kuid) + "," + (0 - kuid) + "," + biaoz + ",0,0,0,0,0,'Ƥ����',0," 
//					+ id + ","  + SysConstant.CHEB_PDC + "," + DateUtil.FormatOracleDateTime(fahrq + " 23:59:59")
//					+ "," + strlursj + ",'" + v.getRenymc() + "'," + hedbz + ");\n"; 
			}else{
				
//				����ƽ 2010-07-20 ���������������޸ĻὫchepb������ȫ���޸ģ����fahb_id������
				sql +="update chepb set maoz=" +maoz
				+",yuanmz="+maoz
				+",biaoz="+biaoz
				+",yingk=0 -(" + kuid 
				+") where fahb_id="+rsl.getString("id")+";\n";
				
				sql += "update fahb set gongysb_id = " + gysid 
				+ ", meikxxb_id = " + mkid + ", pinzb_id = " + pzid
				+ ", jihkjb_id = " + kjid + ", maoz = " + maoz
				+ ", biaoz = " + biaoz + ", yingk = 0-(" + kuid + "), jingz = " + maoz
				+ ", fahrq = " + DateUtil.FormatOracleDate(fahrq)
				+ " where id = " + id + ";\n";
			}
			Jilcz.addFahid(fhlist,id);
		}
		if(rsl.getRows() > 0){
			sql += "end;";
			con.getUpdate(sql);
		}
		for(int i=0; i< fhlist.size() ;i++) {
//			���·�������ID
			Jilcz.updateLieid(con,(String)fhlist.get(i));
		}
		rsl.close();
		con.Close();
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			initGrid();
		}
	}
	
	private void initGrid() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select f.id,decode(hedbz,").append(SysConstant.HEDBZ_YSH).append(",'�����','δ���') zhuangt,\n")
		.append("f.fahrq,f.maoz, f.biaoz, f.yingd - f.yingk kuid ,\n")
		.append("g.mingc gys,m.mingc mk,p.mingc pz,j.mingc kj\n")
		.append("from fahb f,gongysb g,meikxxb m,pinzb p,jihkjb j\n")
		.append("where f.gongysb_id = g.id and f.meikxxb_id = m.id\n")
		.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id\n")
		.append("and f.fahrq >=").append(DateUtil.FormatOracleDate(getBeginRiq()))
		.append(" and f.fahrq <").append(DateUtil.FormatOracleDate(getEndRiq()))
		.append(" +1\n and f.yunsfsb_id = ").append(SysConstant.YUNSFS_Pidc)
		.append(" and f.diancxxb_id = ").append(v.getDiancxxb_id())
		.append("\norder by fahrq,g.xuh");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		��ʽ
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		//����ÿҳ��ʾ����
//		egu.addPaging(25);
		egu.setDefaultsortable(false);
		
//		if(v.isFencb()) {
//			ComboBox dc= new ComboBox();
//			egu.getColumn("diancxxb_id").setEditor(dc);
//			dc.setEditable(true);
//			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
//			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
//			egu.getColumn("diancxxb_id").setWidth(70);
//		}else {
//			egu.getColumn("id").setHidden(true);
//			egu.getColumn("id").editor = null;
//		}
		egu.getColumn("zhuangt").setHeader(Locale.Pidc_zhuangt);
		egu.getColumn("zhuangt").setWidth(50);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("gys").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gys").setWidth(120);
		egu.getColumn("gys").setEditor(null);
		egu.getColumn("mk").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("mk").setWidth(150);
		egu.getColumn("mk").setEditor(null);
		egu.getColumn("pz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pz").setWidth(70);
		egu.getColumn("fahrq").setHeader(Locale.Pidc_fahrq);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("kj").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("kj").setWidth(70);
		egu.getColumn("maoz").setHeader(Locale.Pidc_maoz);
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("biaoz").setHeader(Locale.Pidc_biaoz);
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("kuid").setHeader(Locale.kuid_chepb);
		egu.getColumn("kuid").setWidth(70);
		egu.getColumn("kuid").setEditor(null);
//		����Ĭ��ֵ
		egu.getColumn("zhuangt").defaultvalue = "δ���";
		egu.getColumn("fahrq").defaultvalue = getBeginRiq();
		egu.getColumn("maoz").defaultvalue = "0";
		egu.getColumn("biaoz").defaultvalue = "0";
		egu.getColumn("kuid").defaultvalue = "0";
		sb.delete(0, sb.length());
		sb.append("select * from (select \n")
		.append("g.mingc gys,m.mingc mk,p.mingc pz,j.mingc kj\n")
		.append("from fahb f,gongysb g,meikxxb m,pinzb p,jihkjb j\n")
		.append("where f.gongysb_id = g.id and f.meikxxb_id = m.id\n")
		.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id\n")
		.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_Pidc)
		.append(" and f.diancxxb_id = ").append(v.getDiancxxb_id())
		.append("\norder by f.id desc) where rownum = 1");
		rsl = con.getResultSetList(sb.toString());
		if(rsl.next()){
			egu.getColumn("gys").defaultvalue = rsl.getString("gys");
			egu.getColumn("mk").defaultvalue = rsl.getString("mk");
			egu.getColumn("pz").defaultvalue = rsl.getString("pz");
			egu.getColumn("kj").defaultvalue = rsl.getString("kj");
		}
		rsl.close();
//		������
		ComboBox pz=new ComboBox();
		egu.getColumn("pz").setEditor(pz);
		pz.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		egu.getColumn("pz").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		//���ÿھ�������
		ComboBox kj=new ComboBox();
		egu.getColumn("kj").setEditor(kj);
		kj.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("kj").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
//		��Ӧ�����ĵ���
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n var record = own.getStore().getAt(irow); var data = record.get('ZHUANGT'); " 
				+"if(data=='�����'){Ext.MessageBox.alert('��ʾ��Ϣ','���ܶ���������ݽ��и��ģ�'); return false;}"
				+"if('GYS' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
//		���ֵ��Զ�����
		egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){ "
				+"if(e.field == 'MAOZ' || e.field == 'BIAOZ'){e.record.set('KUID',eval(eval(e.record.get('BIAOZ')||0) - eval(e.record.get('MAOZ')||0)));}}); ");
//		toolbar
		egu.addTbarText("�������ڣ�");
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("BeginRq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��");
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("EndRq");
		egu.addToolbarItem(dfe.getScript());
//		��Ӱ�ť
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		
//		��Ӧ����
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk,"gongysTree"
				,""+v.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("if(cks.getDepth() < 2){ \n")
		.append("Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���Ӧ��ú��λ��');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GYS', cks.parentNode.text); \n")
		.append("rec.set('MK', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		v.setDefaultTree(dt);
		
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb where leix=1 order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}
	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
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
			setBeginRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			init();
			visit.setActivePageName(getPageName().toString());
			
		}
	} 
	
	private void init() {
		setExtGrid(null);
		initGrid();
	}
}