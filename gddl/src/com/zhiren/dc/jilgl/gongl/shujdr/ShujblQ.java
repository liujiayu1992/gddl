package com.zhiren.dc.jilgl.gongl.shujdr;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author Rock
 *
 */
/*
 * 2009-02-18
 * ����
 * �޸ı��淽������ж����ʽ��ѡ��
 */
/*
 * 2009-02-25
 * ����
 * ���ӹ����ID��Ϊ-1 ʹ�����μ�������ʱ���Խ���chepb�ķ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-27
 * �������޸ĳ���ͷ����Ĭ��ֵ��"��ѡ��"��Ϊ""
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-16 10��39
 * ���������Ӳ�¼���ݱ�ʶ���� ��������ݿ� 
 *  alter table chepb add bulsj number(1) default 0 null;
	comment on column chepb.bulsj
	  is '��¼���ݱ�ʶ 1����¼ 0���ǲ�¼'; 
	alter table cheplsb add bulsj number(1) default 0 null;
	comment on column cheplsb.bulsj
	  is '��¼���ݱ�ʶ 1����¼ 0���ǲ�¼'; 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-10 09��59
 * ���������ӷ������ڡ��������� Ĭ��Ϊѡ�еĹ������ڡ�
 * ͬʱ���ɵ�js�޸Ĳ��ƶ��� ShujblQ.thml��
 */
public class ShujblQ extends BasePage implements PageValidateListener {
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

	//������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
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
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujblQ.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		//���복Ƥ��ʱ��
		sb.append("begin\n");
		
		
		/*
		 * �޸ķ�վ����վ��ԭ��վ�������ȡֵ�洢����
		 * �޸�ʱ�䣺2008-12-04
		 * �޸��ˣ�����
		 */
		while (rsl.next()) {
			String diancxxb_id = getExtGrid().getColumn("diancxxb_id")
			.combo.getBeanStrId(rsl.getString("diancxxb_id"));
			sb.append("insert into cheplsb\n");
			sb.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, caiybh, yunsfsb_id, chec,cheph, maoz, piz, biaoz, koud, kous, kouz, sanfsl, jianjfs, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, zhongcjjy, daozch, lury, beiz,qingcsj,zhongcsj,yunsdwb_id,xiecfsb_id,caiyrq,guohb_id,bulsj)\n");
			sb.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id);
			sb.append(",").append(((IDropDownModel) getGongysModel())
					.getBeanId(rsl.getString("gongysb_id")));
			sb.append(",").append(((IDropDownModel) getMeikModel())
					.getBeanId(rsl.getString("meikxxb_id")));
			sb.append(",").append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
			sb.append(",").append(rsl.getString("faz_id"));
			sb.append(",").append(rsl.getString("daoz_id"));
			sb.append(",").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
			sb.append(",").append(DateUtil.FormatOracleDate(rsl.getString("fahrq")));
			sb.append(",").append(DateUtil.FormatOracleDate(rsl.getString("daohrq")));
			sb.append(",'").append(rsl.getString("bianm")).append("',");
			sb.append(SysConstant.YUNSFS_QIY);
			sb.append(",'").append(rsl.getString("chec"));
			
			//�滻�µĳ�ͷ
//			sb.append("','").append(rsl.getString("cheph"));
			String cheph="";
			if(rsl.getString("cheph").matches("^[^0-9]+")){//ԭ�еİ����ı��
				cheph=rsl.getString("cheph").replaceFirst("^[^0-9]+",rsl.getString("qiccht_id"));
			}else{//�����ӵļ�¼
				cheph=rsl.getString("qiccht_id")+rsl.getString("cheph");
			}
			
			sb.append("','").append(cheph);
			
			
			sb.append("',").append(rsl.getDouble("maoz"));
			sb.append(",").append(rsl.getDouble("piz"));
			sb.append(",").append(rsl.getDouble("biaoz"));
			sb.append(",").append(rsl.getDouble("koud"));
			sb.append(",").append(rsl.getDouble("kous"));
			sb.append(",").append(rsl.getDouble("kouz"));
			sb.append(",").append(rsl.getDouble("sanfsl"));
			sb.append(",'").append(rsl.getString("jianjfs"));
			sb.append("',").append(rsl.getString("chebb_id"));
			sb.append(",").append(rsl.getString("yuandz_id"));
			sb.append(",").append((getExtGrid().getColumn("yuanshdwb_id").combo).getBeanId(rsl.getString("yuanshdwb_id")));
			sb.append(",'").append(rsl.getString("yuanmkdw"));
			sb.append("','").append(rsl.getString("zhongcjjy"));
			sb.append("','").append(rsl.getString("daozch"));
			sb.append("','").append(visit.getRenymc());
			sb.append("','").append(rsl.getString("beiz")).append("'");
			sb.append(",").append(DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
			sb.append(",").append(DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
			sb.append(",").append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id")));
			sb.append(",").append((getExtGrid().getColumn("xiecfsb_id").combo).getBeanId(rsl.getString("xiecfsb_id")));
			sb.append(",").append(DateUtil.FormatOracleDate(DateUtil.getDate(rsl.getString("guohsj")))).append(",-1,1);\n");
		}
		sb.append("end;");
		setMsg(Jilcz.SaveJilData(sb.toString(), visit.getDiancxxb_id(), SysConstant.YUNSFS_QIY, 
				SysConstant.HEDBZ_YJJ, null, this.getClass().getName(), Jilcz.SaveMode_BL));
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		getSelectData();
	}
	
	/**
	 * ����:tongzf
	 * ʱ��:2009-4-14
	 * �޸�����:��������ͷ�ֶ�
	 *
	 */
	
	
	private String getDefaultChet(JDBCcon con){
		
		String qiccSql="select id ,mingc  from qiccht where zhuangt=1 order by xuh ";
		
		ResultSetList rsl = con.getResultSetList(qiccSql);
		
		String default_value="";
		 if(rsl.next()){
			 default_value=rsl.getString("mingc");
		 }
		 
		 return default_value;
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
//		sb.append("select c.diancxxb_id,c.cheph,c.maoz,c.piz,c.biaoz,c.koud,c.kous,c.kouz,c.sanfsl,c.id,c.chec,'' as gongysb_id,'' as meikxxb_id,'' as faz_id,\n");
		sb.append("select c.diancxxb_id,(select  REGEXP_SUBSTR(c.cheph,'^[^[:digit:]]+') as qiccht_id  from qiccht) qiccht_id,c.cheph,c.maoz,c.piz,c.biaoz,c.koud,c.kous,c.kouz,c.sanfsl,c.id,c.chec,'' as gongysb_id,'' as meikxxb_id,'' as faz_id,\n");
		sb.append("'' as jihkjb_id,'' as pinzb_id,'' as yunsdwb_id,'' xiecfsb_id,c.fahrq,c.daohrq,c.jianjfs,'' as chebb_id,\n");
		sb.append("'' as guohsj,'' as bianm,c.zhongcjjy,\n");
		sb.append("'' as daoz_id,'' as yuandz_id,'' as yuanshdwb_id,\n");
		sb.append("c.yuanmkdw,c.daozch,c.beiz from cheplsb c ");
		ResultSetList rsl = con.getResultSetList(sb.toString());
/**
 * huochaoyuan 2009-03-04����������������д���
 */		
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������sb:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		ComboBox dc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		dc.setEditable(true);
		String sql = 
			"select id, mingc from (\n" +
			"select id, mingc, level,CONNECT_BY_ISLEAF leaf\n" + 
			"  from diancxxb\n" + 
			" start with id = "+getTreeid()+"\n" + 
			"connect by fuid = prior id)\n" + 
			"where leaf = 1";
		IDropDownModel dcmd = new IDropDownModel(sql);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dcmd);
		if(dcmd.getOptionCount()>1){
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else{
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
			egu.getColumn("diancxxb_id").setDefaultValue(dcmd.getBeanValue(getTreeid()));
		}
		
		egu.getColumn("qiccht_id").setHeader("��ͷ");
		egu.getColumn("qiccht_id").setWidth(65);
		egu.getColumn("qiccht_id").setDefaultValue(this.getDefaultChet(con));
//		���ó�ͷ������
		ComboBox qic=new ComboBox();
		egu.getColumn("qiccht_id").setEditor(qic);
		qic.setEditable(true);
		String qiccSql="select id ,mingc  from qiccht where zhuangt=1 order by xuh ";
		egu.getColumn("qiccht_id").setComboEditor(egu.gridId, new IDropDownModel(qiccSql));
		egu.getColumn("qiccht_id").editor.allowBlank = true;
		
		
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("faz_id").setHidden(true);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("jianjfs").setHeader(Locale.jianjfs_chepb);
		egu.getColumn("jianjfs").setWidth(60);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("bianm").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("guohsj").setHidden(true);
		egu.getColumn("guohsj").editor = null;
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("xiecfsb_id").setHeader(Locale.xiecfs_chepb);
		egu.getColumn("xiecfsb_id").setWidth(80);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
//		egu.getColumn("cheph").editor
//		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 3); }");
		egu.getColumn("cheph").editor
		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
//		egu.getColumn("maoz").editor
//		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("maoz").editor
		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 5); }");
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
//		egu.getColumn("piz").editor
//		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 5); }");
		egu.getColumn("piz").editor
		.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 6); }");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		StringBuffer lins = new StringBuffer();
		lins
				.append("specialkey:function(own,e){ \n")
				.append("if(row+1 == gridDiv_grid.getStore().getCount()){ \n")
				.append("Ext.MessageBox.alert('��ʾ��Ϣ','�ѵ�������ĩβ��');return; \n")
				.append("} \n")
				.append("row = row+1; \n")
				.append(
						"last = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("gridDiv_grid.getSelectionModel().selectRow(row); \n")
				.append(
						"cur = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("copylastrec(last,cur); \n").append(
//					
				"gridDiv_grid.startEditing(row , 3); }");
		egu.getColumn("biaoz").editor.setListeners(lins.toString());
		
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setDefaultValue("0");//����Ĭ��ֵ
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("kous").setHeader(Locale.kous_chepb);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("sanfsl").setWidth(60);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("daoz_id").setHidden(true);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuandz_id").setHidden(true);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		
		egu.addTbarText("����ʱ��:");
		DateField df = new DateField();
		
		//yuss 2012-3-28 ��Ȫ�����β�¼����ʱ����Ϊǰһ��
		ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='�����β�¼�����������ڼ�1' and zhuangt=1");
		if(rs.next()){//�Ǿ�Ȫ
			if(rs.getString("zhi").equals("��")){
				Date Date_riq=DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay);
				String Str_riq=DateUtil.FormatDate(Date_riq);
				df.setValue(Str_riq);
			}
		}else{
			df.setValue(this.getRiq());
		}
		
		//yuss end
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("ʱ:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("HOUR");
		comb1.setId("HOUR");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("��:");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("MIN");
		comb2.setId("MIN");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
//		ж����ʽ��������
		ComboBox c8 = new ComboBox();
		egu.getColumn("xiecfsb_id").setEditor(c8);
		c8.setEditable(true);
		egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_xiecfs));
		/*
		 * ���÷�վ����վ��ԭ��վ������Ĭ��ֵ,ȡ���༭���ܣ�ȡ������ҳ������ʾ����ֹ�û��������
		 * �޸�ʱ�䣺2008-12-04
		 * �޸��ˣ�����
		 */
		//����Ĭ�Ϸ�վ
		egu.getColumn("faz_id").setDefaultValue("1");
		//����Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue("1");
		//����Ĭ��ԭ��վ
		egu.getColumn("yuandz_id").setDefaultValue("1");
		//���ó����Ĭ��ֵ CHEB_QC
		egu.getColumn("chebb_id").setDefaultValue(""+SysConstant.CHEB_QC);
		//����Ʒ��������
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c2);
		c2.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		//���ÿھ�������
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjsb));
		//���ü�﷽ʽ������
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "����"));
		l.add(new IDropDownBean(1, "���"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("jianjfs").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("jianjfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("jianjfs").setDefaultValue("����");
		//����ԭ�ջ���λ������
		ComboBox c7 = new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c7);
		c7.setEditable(true);//���ÿ�����
		sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue(
				"" + ((Visit) getPage().getVisit()).getDiancmc());
		//�������䵥λ������
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(comb);
		comb.setEditable(true);
		String yunsdwsb = "select id,mingc from yunsdwb where diancxxb_id="
				+ getTreeid();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwsb));
		egu.getColumn("yunsdwb_id").setDefaultValue("��ѡ��");
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����ÿҳ��ʾ����
		egu.addPaging(25);
		
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//���õ����Ӱ�ťʱ�����¼�
		egu.addOtherScript("gridDiv_ds.on('add',setDateDefault);\n");
//		egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(e.field=='MAOZ' || e.field=='PIZ'){biaoz = eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0);if(biaoz>0){e.record.set('BIAOZ',biaoz);}else{Ext.MessageBox.alert('��ʾ��Ϣ','ë�ص�ֵ�����Ƥ�ش�');return;}}});\n");
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+ "row = irow; \n"
				+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+ "gongysTree_window.show();}});\n");
		
		
		boolean kougFlag=false;
		String kouggs="KOUD";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kougFlag=true;
			kouggs=rsl.getString("zhi");
		}
		
		boolean kousFlag=false;
		String kousgs="KOUS";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='��ˮ�ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kousFlag=true;
			kousgs=rsl.getString("zhi");
		}
		
		boolean kouzFlag=false;
		String kouzgs="KOUZ";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='�����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kouzFlag=true;
			kouzgs=rsl.getString("zhi");
		}
		
		if(kougFlag || kousFlag ||  kouzFlag){
			egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){" +
					"" +
					
					" var rec=e.record;\n" +
					" var MAOZ=eval(rec.get('MAOZ')||0);\n" +
					" var PIZ=eval(rec.get('PIZ')||0);\n"+
					" var KOUD=eval(rec.get('KOUD')||0);\n" +
					" var KOUS=eval(rec.get('KOUS')||0);\n" +
					" var KOUZ=eval(rec.get('KOUZ')||0);"+
					
					"if( e.field=='KOUD' ){\n" +
					" rec.set('KOUD',Round_new("+kouggs+",2) );"+
					"} \n" +
				
					"if( e.field=='KOUS' ){\n" +
					" rec.set('KOUS',Round_new("+kousgs+",2));"+
					"} \n" +
				
					"if( e.field=='KOUZ' ){\n" +
					" rec.set('KOUZ',Round_new("+kouzgs+",2));"+
					"} \n" +
					"" +
					
					" var bs=rec.get('BEIZ');\n" +
					" if(bs==null ||  bs==''){\n" +
					" bs='0,0,0';\n"+
					" }\n"+
					" var bssp=bs.split(',');\n"+//  ��ʽ  koud �� kous �� kouz
					"if(e.field=='KOUD'){bs=KOUD+','+bssp[1]+','+bssp[2];}\n" +
					"if(e.field=='KOUS'){bs=bssp[0]+','+KOUS+','+bssp[2];}\n" +
					"if(e.field=='KOUZ'){bs=bssp[0]+','+bssp[1]+','+KOUZ;}\n" +
				
					"rec.set('BEIZ',bs);\n"+
					
					
					"if(e.field=='MAOZ' || e.field=='PIZ' || e.field=='KOUD'|| e.field=='KOUS' || e.field=='KOUZ' ){biaoz = eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0)-eval(e.record.get('KOUD')||0)-eval(e.record.get('KOUS')||0)-eval(e.record.get('KOUZ')||0);\n" +
					"if(biaoz>0){e.record.set('BIAOZ',biaoz);}else{Ext.MessageBox.alert('��ʾ��Ϣ','ë�ص�ֵ�����Ƥ����۶���Ŀ��');return;}\n" +
					"}\n" +
					"});\n");
			
		}else{
			egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(e.field=='MAOZ' || e.field=='PIZ'){biaoz = eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0);if(biaoz>0){e.record.set('BIAOZ',biaoz);}else{Ext.MessageBox.alert('��ʾ��Ϣ','ë�ص�ֵ�����Ƥ�ش�');return;}}});\n");
		}
		
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree"
				,""+visit.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
		.append("function() { \n")
		.append(
				"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("if(cks.getDepth() == 3){\n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
		.append("rec.set('JIHKJB_ID', cks.text);\n")
		.append("}else if(cks.getDepth() == 2){\n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.text);\n")
		.append("rec.set('MEIKXXB_ID', cks.text);\n")
		.append("}else if(cks.getDepth() == 1){\n")
		.append("rec.set('GONGYSB_ID', cks.text); }\n")
		.append("gongysTree_window.hide();\n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
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
	
	public String getTreeScript1() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	//����Сʱ������
	public IDropDownBean getHourValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHourValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHourModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getHourModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour.add(new IDropDownBean(i, "0" + i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}

	//	 ���÷���������
	public IDropDownBean getMinValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setMinValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			getMinModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMinModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb where leix=1 order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel6() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel6();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel6(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}

	//�糧��
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
			setRiq(DateUtil.FormatDate(new Date()));
			setHourValue(null);
			setHourModel(null);
			setMinValue(null);
			setMinModel(null);
			visit.setDefaultTree(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			getSelectData();
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
	}
}