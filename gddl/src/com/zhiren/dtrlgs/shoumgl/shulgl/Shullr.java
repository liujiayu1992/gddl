package com.zhiren.dtrlgs.shoumgl.shulgl;


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

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dtrlgs.faygl.faygs.FaycbBean;
import com.zhiren.dtrlgs.faygl.faygs.FayzgInfo;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumcbBean;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumzgInfo;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shullr extends BasePage implements PageValidateListener {
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

//	�糧��
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
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	// ҳ��ˢ�����ڣ�жú���ڣ�
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
	
//	gridInwidow
	public ExtGridUtil getExtGridInWindow() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGridInWindow(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridInWindowScript() {
		if (getExtGridInWindow() == null) {
			return "";
		}
		return getExtGridInWindow().getGridScript();
	}

	public String getGridInWindowHtml() {
		if (getExtGridInWindow() == null) {
			return "";
		}
		return getExtGridInWindow().getHtml();
	}
	
	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _DetailClick = false;

	public void DetailButton(IRequestCycle cycle) {
		_DetailClick = true;
	}

	private boolean _XiemxxClick = false;

	public void XiemxxButton(IRequestCycle cycle) {
		_XiemxxClick = true;
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
		if (_DetailClick) {
			_DetailClick = false;
			((Visit) getPage().getVisit()).setString10(getChange());
			cycle.activate("Chepghxx");
		}
		if (_XiemxxClick) {
			_XiemxxClick = false;
			((Visit) getPage().getVisit()).setString10(getChange());
			cycle.activate("Duimxx");
		}
	}
   
	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// жú���ڵ�ora�ַ�����ʽ
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		// �糧ID
		long dcid = visit.getDiancxxb_id();
		String sql = "select f.id,\n" 
			    + "      (select mingc from diancxxb where id= f.diancxxb_id ) fahr,\n"
		        +"g.mingc gongys,\n"
		        +"nvl((select mingc from diancxxb where id=fa.keh_diancxxb_id),'') keh_diancxxb_id,\n"
		        +" (select mingc from meikxxb where id=f.meikxxb_id)meikxxb_id,\n"
				+ "       p.mingc pinz,\n" 
				+ "       c.mingc faz,\n"
				+"         (select mingc from vwchez where id=f.daoz_id)daoz,\n"
				+"(select mingc from diancxxb where id=f.YUANSHDWB_ID) shouhr,\n"
				+"(select mingc from jihkjb where id= f.jihkjb_id)jihkjb_id,\n"
				+ "       f.chec,\n" 
				+"       (select mingc from yunsfsb where id=f.yunsfsb_id)yunsfsb_id ,"
				+"        f.chuanm,"
				+ "       f.ches,\n" + "       f.biaoz,\n"
				+ "       f.maoz,\n" + "       f.piz,\n"
				+"        f.jingz,"
				+"        f.yuns,"
				+"        f.yingk,"
				+ "       f.fahrq,\n"
				+ "       f.daohrq jiexrq,\n"
				+ "       to_char(f.daohrq, 'hh24') jiexs,\n"
				+ "       to_char(f.daohrq, 'mi') jiexf,\n"
				+ "       f.guohsj guohrq,\n"
				+ "       to_char(f.guohsj, 'hh24') guohs,\n"
				+ "       to_char(f.guohsj, 'mi') guohf,\n"
				+"        f.fayslb_id fayslb_id,"
				+"       (select mingc from yewlxb where id=f.leix_id) leix_id,\n"
				+"        (select hetbh from hetb where id=f.hetb_id)hetb_id,"
				+"        (select hetbh from hetb where id=f.xiaoshetb_id)XIAOSHETB_ID,"
				+"(select case when count(*)>0  then '�����' else 'δ���' end  from duimxxb where fahb_id=f.fahb_id)as rukzt\n"
				+"        ,f.fahb_id "
				+ "  from fahbtmp f,fayslb fa, vwfahr g, vwpinz p, vwchez c\n"
				+ " where f.gongysb_id = g.id\n" + "   and f.pinzb_id = p.id\n"
				+" and fa.id=f.fayslb_id(+)"
				+ "   and f.faz_id = c.id\n"// + "	and f.diancxxb_id = " + dcid
				+ "   and f.fahrq >= " + strxmrqOra + " and f.fahrq < "
				+ strxmrqOra + "+1\n";
		  sql+=" and not  (f.fayslb_id=-2 or f.fahb_id=-2)  and f.shujly='�ֹ�¼��'\n";//��ҳ��ֻ�����ֹ�¼�������;-2��ʾfahtmp��ɾ����
          sql+=" and  f.YUANSHDWB_ID="+this.getTreeid();
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
		// ����grid�б���
		egu.getColumn("fahr").setHeader("������");
		egu.getColumn("gongys").setHeader("��Ӧ��");
		egu.getColumn("meikxxb_id").setHeader("ú����");
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("faz").setHeader(Local.faz);
		egu.getColumn("daoz").setHeader(Local.daoz);
		egu.getColumn("shouhr").setHeader("�ջ���");
		egu.getColumn("chec").setHeader(Local.chec);
		egu.getColumn("ches").setHeader(Local.ches);
		egu.getColumn("biaoz").setHeader(Local.biaoz);
		egu.getColumn("maoz").setHeader(Local.maoz);
		egu.getColumn("piz").setHeader(Local.piz);
		egu.getColumn("fahrq").setHeader(Local.fahrq);
		egu.getColumn("jiexrq").setHeader(Local.daohrq_id_fahb);
		egu.getColumn("jiexs").setHeader(Local.shi);
		egu.getColumn("jiexf").setHeader(Local.fen);
		egu.getColumn("guohrq").setHeader(Local.guohrq);
		egu.getColumn("guohs").setHeader(Local.shi);
		egu.getColumn("guohf").setHeader(Local.fen);
		egu.getColumn("keh_diancxxb_id").setHeader("�ͻ�����");
		
		 egu.getColumn("hetb_id").setHeader("��ú��ͬ��");
		 egu.getColumn("XIAOSHETB_ID").setHeader("���ۺ�ͬ��");
		 egu.getColumn("jingz").setHeader(Local.jingz);
	      egu.getColumn("yuns").setHeader(Local.yuns);
	       egu.getColumn("yingk").setHeader(Local.yingk);
	       egu.getColumn("leix_id").setHeader("ҵ������");
	       egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
	       egu.getColumn("chuanm").setHeader("����");
	       egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
	       
		// ����grid�п��
		egu.getColumn("fahr").setWidth(100);
		egu.getColumn("gongys").setWidth(80);
		egu.getColumn("meikxxb_id").setWidth(80);
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("daoz").setWidth(80);
		egu.getColumn("shouhr").setWidth(80);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("jiexrq").setWidth(80);
		egu.getColumn("jiexs").setWidth(40);
		egu.getColumn("jiexf").setWidth(40);
		egu.getColumn("guohrq").setWidth(80);
		egu.getColumn("guohs").setWidth(40);
		egu.getColumn("guohf").setWidth(40);
		egu.getColumn("XIAOSHETB_ID").setWidth(150);
		egu.getColumn("keh_diancxxb_id").setWidth(120);

		 egu.getColumn("hetb_id").setWidth(150);
		 egu.getColumn("jingz").setWidth(80);
	      egu.getColumn("yuns").setWidth(80);
	       egu.getColumn("yingk").setWidth(80);
	       egu.getColumn("leix_id").setWidth(80);
	       egu.getColumn("yunsfsb_id").setWidth(80);
	       egu.getColumn("chuanm").setWidth(80);
		// ����Ĭ��ֵ
		egu.getColumn("fahrq").setDefaultValue(getRiq());
		egu.getColumn("jiexrq").setDefaultValue(getRiq());
		egu.getColumn("guohrq").setDefaultValue(getRiq());
		egu.getColumn("jiexs").setDefaultValue("0");
		egu.getColumn("jiexf").setDefaultValue("0");
		egu.getColumn("guohs").setDefaultValue("0");
		egu.getColumn("guohf").setDefaultValue("0");
		JDBCcon dianccon=new JDBCcon();
		ResultSet rs=dianccon.getResultSet("select * from diancxxb where id="+this.getTreeid());
		try {
			if(rs.next()){
			egu.getColumn("keh_diancxxb_id").setDefaultValue(rs.getString("mingc"));
			}
		} catch (SQLException e1) {
			// TODO �Զ����� catch ��
			e1.printStackTrace();
		}
		 egu.getColumn("jingz").setDefaultValue("0");
	     egu.getColumn("yuns").setDefaultValue("0");
	     egu.getColumn("yingk").setDefaultValue("0");
	     egu.getColumn("fayslb_id").setDefaultValue("0");
	     egu.getColumn("fahb_id").setDefaultValue("0");
		 egu.getColumn("rukzt").setDefaultValue("δ���");
		 egu.getColumn("fahr").setDefaultValue("����ȼ�Ϲ�˾");
		 
		 try {
			egu.getColumn("shouhr").setDefaultValue(MainGlobal.getTableCol("diancxxb", "mingc", "id", this.getTreeid()));
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	     //���ò���ʾ
	    egu.getColumn("fahb_id").setHidden(true);
	      egu.getColumn("fayslb_id").setHidden(true);
	     egu.getColumn("rukzt").setHidden(true);
	     egu.getColumn("fahr").setHidden(true);
	     egu.getColumn("shouhr").setHidden(true);
	     //���ò��༭
//	    egu.getColumn("jingz").setEditor(null);
	    egu.getColumn("yingk").setEditor(null);
	    egu.getColumn("rukzt").setEditor(null);
	    
		// ����������������
	    
	    //������
	    ComboBox fahr=new ComboBox();
	    egu.getColumn("fahr").setEditor(fahr);
	     fahr.setEditable(true);
	     String fahrsql="select id,mingc from diancxxb where cangkb_id<>1  and mingc='����ȼ�Ϲ�˾' order by mingc";
	     egu.getColumn("fahr").setComboEditor(egu.gridId, new IDropDownModel(fahrsql));
		// ��Ӧ��
		ComboBox gongys = new ComboBox();
		egu.getColumn("gongys").setEditor(gongys);
		gongys.setEditable(true);
		sql = "select id,mingc from vwfahr order by mingc";
		egu.getColumn("gongys").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
//		�ͻ�����
		ComboBox kehmc=new ComboBox();
		egu.getColumn("keh_diancxxb_id").setEditor(kehmc);
		kehmc.setEditable(true);
		String kehmcsql="select id ,mingc from diancxxb where cangkb_id=1 and jib=3";
		egu.getColumn("keh_diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(kehmcsql));
		
		//ú����Ϣ
		ComboBox meikxx=new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(meikxx);
		meikxx.setEditable(true);
		String meikxxsql="select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(meikxxsql));
		// Ʒ��
		ComboBox pinzcb = new ComboBox();
		egu.getColumn("pinz").setEditor(pinzcb);
		pinzcb.setEditable(true);
		sql = "select id,mingc from pinzb  where leib='ú' order by mingc ";
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		// ��վ
		ComboBox fazcb = new ComboBox();
		egu.getColumn("faz").setEditor(fazcb);
		fazcb.setEditable(true);
		sql = "select id,mingc from vwchez order by mingc";
		egu.getColumn("faz")
				.setComboEditor(egu.gridId, new IDropDownModel(sql));
		//��վ
		ComboBox daoz=new ComboBox();
		egu.getColumn("daoz").setEditor(daoz);
		daoz.setEditable(true);
		String daozsql="select id ,mingc from vwchez order by mingc";
		egu.getColumn("daoz").setComboEditor(egu.gridId, new IDropDownModel(daozsql));
		//�ջ���
		ComboBox shouhr=new ComboBox();
		egu.getColumn("shouhr").setEditor(shouhr);
		shouhr.setEditable(true);
		String shouhrsql="select id,mingc from diancxxb where  jib=3 and cangkb_id=1 order by mingc";
		egu.getColumn("shouhr").setComboEditor(egu.gridId, new IDropDownModel(shouhrsql));
//		�ƻ��ھ�
		ComboBox jihkjb_id_comboBox=new ComboBox();
	   egu.getColumn("jihkjb_id").setEditor(jihkjb_id_comboBox);
	     jihkjb_id_comboBox.setEditable(true);
	     String jihkjb_sql="select id,mingc from jihkjb  order by id";
	     egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjb_sql));
		// ��жʱ
		ComboBox jxscb = new ComboBox();
		egu.getColumn("jiexs").setEditor(jxscb);
		jxscb.setEditable(true);
		List h = new ArrayList();
		for (int i = 0; i < 24; i++)
			h.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("jiexs")
				.setComboEditor(egu.gridId, new IDropDownModel(h));
		// ��ж��
		ComboBox jxfcb = new ComboBox();
		egu.getColumn("jiexf").setEditor(jxfcb);
		jxfcb.setEditable(true);
		List m = new ArrayList();
		for (int i = 0; i < 60; i++)
			m.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("jiexf")
				.setComboEditor(egu.gridId, new IDropDownModel(m));
		// ����ʱ
		ComboBox ghscb = new ComboBox();
		egu.getColumn("guohs").setEditor(ghscb);
		ghscb.setEditable(true);
		List gh = new ArrayList();
		for (int i = 0; i < 24; i++)
			gh.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("guohs").setComboEditor(egu.gridId,
				new IDropDownModel(gh));
		// �����
		ComboBox ghfcb = new ComboBox();
		egu.getColumn("guohf").setEditor(ghfcb);
		ghfcb.setEditable(true);
		List gm = new ArrayList();
		for (int i = 0; i < 60; i++)
			gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("guohf").setComboEditor(egu.gridId,
				new IDropDownModel(gm));
		//��ͬ���
		ComboBox hetbh=new ComboBox();
		hetbh.setEditable(true);
		egu.getColumn("hetb_id").setEditor(hetbh);
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,hetbh from hetb where to_date('"+this.getRiq()+"','yyyy-mm-dd')>=qisrq and guoqrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and leib=2"));
         egu.getColumn("hetb_id").setReturnId(true);
         egu.getColumn("hetb_id").editor.setAllowBlank(true);
//       ���ۺ�ͬ���
 		ComboBox xiaos_hetbh=new ComboBox();
 		xiaos_hetbh.setEditable(true);
 		egu.getColumn("XIAOSHETB_ID").setEditor(xiaos_hetbh);
 		egu.getColumn("XIAOSHETB_ID").setComboEditor(egu.gridId,new IDropDownModel("select id,hetbh from hetb where to_date('"+this.getRiq()+"','yyyy-mm-dd')>=qisrq and guoqrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and leib<>2"));
          egu.getColumn("XIAOSHETB_ID").setReturnId(true);
     	//ҵ������
 		ComboBox yewlx=new ComboBox();
 		yewlx.setEditable(true);
 		yewlx.setId("yewlx_combo");
 		yewlx.setListeners("select:function(combo,record,index) {if(combo.getRawValue()!='ֱ��'){var rec = gridDiv_sm.getSelected();rec.set('HETB_ID','');}if(combo.getRawValue()=='���ؽ���'){caozpd(gridDiv_sm.getSelected());}}");
 		egu.getColumn("leix_id").setEditor(yewlx);
 		egu.getColumn("leix_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from yewlxb where mingc<>'�ɹ�'"));
 		egu.getColumn("leix_id").setReturnId(true);
 		//yunsfsb_id���䷽ʽ
 		ComboBox yunsfs=new ComboBox();
 		yunsfs.setEditable(true);
 		egu.getColumn("yunsfsb_id").setEditor(yunsfs);
 		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from yunsfsb"));
 		egu.getColumn("yunsfsb_id").setReturnId(true);
 		//chuanm����
 		ComboBox chuanm=new ComboBox();
 		chuanm.setEditable(true);
 		egu.getColumn("chuanm").setEditor(chuanm);
 		egu.getColumn("chuanm").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from luncxxb"));
 		 egu.getColumn("chuanm").editor.setAllowBlank(true);
         // ��ж����ѡ��
//		egu.addTbarText(Local.jiexrq);
		egu.addTbarText(Local.fahrq);
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
//		���õ糧��
		egu.addTbarText("-");
		egu.addTbarText("�ջ���λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
	   // System.out.println(etu.getWindowTreeScript());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
	
		// ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
//		 ��Ӱ�ť
		String condition=" if(diancTree_text.value=='����ȼ�Ϲ�˾'||document.getElementById('diancTree_id').value=='199'){Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���ӳ��������');return;} \n";
			GridButton insert = new GridButton(GridButton.ButtonType_Insert_condition,
					"gridDiv", egu.getGridColumns(), "",condition);
			egu.addTbarBtn(insert);
//		// ��Ӱ�ť
//		GridButton insert = new GridButton(GridButton.ButtonType_Insert,
//				"gridDiv", egu.getGridColumns(), "");
//		egu.addTbarBtn(insert);
		// ɾ����ť
		String DelCondition="var rec=gridDiv_sm.getSelected();if(rec.get('RUKZT')=='�����'&&rec.get('LEIX_ID')=='�ɹ�'){alert('��������ݲ���ɾ��');return;}\n";
		IGridButton delete = new IGridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "",DelCondition);
		egu.addTbarBtn(delete);
		// ���水ť
		String insert_condition="\nvar rgs=gridDiv_ds.getRange();for(var i=0 ;i<rgs.length;i++){if(rgs[i].get('FAYSLB_ID')==0&&rgs[i].get('LEIX_ID')=='���ؽ���'){alert('��'+(i+1)+'��δƥ�䲻�ܱ���');return;}if(rgs[i].get('HETB_ID')==''&&rgs[i].get('LEIX_ID')=='ֱ��'){alert('��'+(i+1)+'�вɹ���ͬ����Ϊ��');return;}if(rgs[i].get('KEH_DIANCXXB_ID')==''&&rgs[i].get('LEIX_ID')=='ֱ��'){alert('��'+(i+1)+'�пͻ����Ʋ���Ϊ��');return;}}\n";
		GridButton save = new GridButton(GridButton.ButtonType_Save_condition, "gridDiv",
				egu.getGridColumns(), "SaveButton",insert_condition);
		
		egu.addTbarBtn(save);
		/*
		// ��ϸ���ⰴť
		GridButton xiem = new GridButton("������", "Xiem");
		xiem.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(xiem);
		// ��ϸ���ⰴť
		GridButton Detail = new GridButton("��ϸ����", "Detail");
		Detail.setIcon(SysConstant.Btn_Icon_Copy);
		egu.addTbarBtn(Detail);
         */
		

		egu.addOtherScript("\n  gridDiv_grid.on('afteredit',function(e){var rd=e.record;rd.set('JINGZ',Number(rd.get('MAOZ'))-Number(rd.get('PIZ')));rd.set('YINGK',Number(rd.get('JINGZ'))+Number(rd.get('YUNS'))-Number(rd.get('BIAOZ')));if(e.field=='YUNSFSB_ID'&&e.value!='����'){rd.set('CHUANM','')} });");
		setExtGrid(egu);
		con.Close();

	}
private void initGridInWindow(){
	JDBCcon con=new JDBCcon();
	ResultSetList rsl=con.getResultSetList("select id,\n" +
			"   fy.chec,\n" +
			"(select mingc from gongysb where id= fy.gongysb_id) gongysb_id,\n"+
			"(select mingc from yunsfsb where id= fy.yunsfsb_id) yunsfsb_id,\n" +
			"(select mingc from luncxxb where id=fy.luncxxb_id)luncxxb_id,\n" +
			"(select mingc from diancxxb where id=shr_diancxxb_id) shouhr,\n"+
			" fy.fahrq,\n" +
			"fy.meil\n" +
			"from fayslb fy where fy.daohrq is null and fy.shr_diancxxb_id="+this.getTreeid()+" and  fy.fahrq < to_date('" + getRiq() + "','yyyy-mm-dd')+1 and fy.fahrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n");
	    con.Close();
	      ExtGridUtil egu=new ExtGridUtil("gridDivInwindow",rsl);
	      egu.setGridSelModel(ExtGridUtil.GridselModel_Row);
	      egu.setHeight(300);
	      egu.setWidth(500);
	      egu.getColumn("id").setHidden(true);
	      egu.getColumn("chec").setHeader("����");
	      egu.getColumn("gongysb_id").setHeader("��Ӧ��");
	      egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
	      egu.getColumn("luncxxb_id").setHeader("����");
	      egu.getColumn("shouhr").setHeader("�ջ���");
	      egu.getColumn("fahrq").setHeader("��������");
	      egu.getColumn("meil").setHeader("ú��");
	      
	      
	     
	      egu.getColumn("chec").setWidth(80);
	      egu.getColumn("gongysb_id").setWidth(80);
	      egu.getColumn("yunsfsb_id").setWidth(60);
	      egu.getColumn("luncxxb_id").setWidth(60);
	      egu.getColumn("shouhr").setWidth(80);
	      egu.getColumn("fahrq").setWidth(70);
	      egu.getColumn("meil").setWidth(80);
	      egu.addOtherScript("	gridDivInwindow_sm.singleSelect=true;\n");
			egu.addOtherScript("function haiypipei(rd){\n");
			egu.addOtherScript("  for(var i=0;i<gridDivInwindow_ds.getCount();i++){\n");
			egu.addOtherScript("     if(gridDivInwindow_ds.getAt(i).get('CHEC')==rd.get('CHEC')&&gridDivInwindow_ds.getAt(i).get('LUNCXXB_ID')==rd.get('CHUANM')&&gridDivInwindow_ds.getAt(i).get('SHOUHR')==rd.get('SHOUHR')&&rd.get('CHEC')!=''&&rd.get('CHUANM')!=''&&rd.get('SHOUHR')!=''){\n");
			egu.addOtherScript("               gridDivInwindow_sm.selectRow(i)  ;\n");
			egu.addOtherScript("              return true; \n");  
			egu.addOtherScript("      } \n");
			egu.addOtherScript("  }\n");
			egu.addOtherScript("  return false;\n");
			egu.addOtherScript("	}\n");
			egu.addOtherScript("function huocpipei(rd){\n");
			egu.addOtherScript(" for(var i=0;i<gridDivInwindow_ds.getCount();i++){\n");
			egu.addOtherScript("     if(gridDivInwindow_ds.getAt(i).get('CHEC')==rd.get('CHEC')&&gridDivInwindow_ds.getAt(i).get('FAHRQ')==rd.get('FAHRQ')&&gridDivInwindow_ds.getAt(i).get('SHOUHR')==rd.get('SHOUHR')&&gridDivInwindow_ds.getAt(i).get('GONGYSB_ID')==rd.get('GONGYS')&&rd.get('CHEC')!=''&&rd.get('FAHRQ')!=''&&rd.get('SHOUHR')!=''&&rd.get('GONGYS')!=''){\n");
			egu.addOtherScript("               gridDivInwindow_sm.selectRow(i)  ;\n");
			egu.addOtherScript("               return true;   \n");
			egu.addOtherScript("       } \n");
			egu.addOtherScript("    }\n");
			egu.addOtherScript("  return false;\n");
			egu.addOtherScript("}\n");
			egu.addOtherScript(" gridDiv_grid.on('beforeedit',function(e){ \n");
			egu.addOtherScript("     rd=e.record;\n");
			egu.addOtherScript("     if(e.field=='LEIX_ID')\n");
			egu.addOtherScript("     if(rd.get('YUNSFSB_ID')==null||rd.get('YUNSFSB_ID')==''){\n");
			egu.addOtherScript("           e.cancel=true;  \n");
			egu.addOtherScript("          alert('����ѡ�����䷽ʽ'); \n");     
			egu.addOtherScript("    }\n");
			egu.addOtherScript("if(e.record.get('FAYSLB_ID')!=0&&e.record.get('LEIX_ID')=='���ؽ���'&&Number(e.record.get('ID'))!=0 ){\n");
			
			egu.addOtherScript("     e.cancel=true;\n");
			egu.addOtherScript("alert('����������ƥ�䱣�治���޸�,ֻ�����,ɾ��');\n");
			egu.addOtherScript(" }else if(e.record.get('FAHB_ID')!=0 && e.record.get('LEIX_ID')!='���ؽ���'){ e.cancel=true;alert('����������ƥ�䱣�治���޸�,ֻ�����,ɾ��');}\n");
			egu.addOtherScript("if(rd.get('YUNSFSB_ID')!='����'&&e.field=='CHUANM'){\n");
			egu.addOtherScript("   e.cancel=true;      \n");
			egu.addOtherScript("  }\n");
//			egu.addOtherScript("if(rd.get('LEIX_ID')!='ֱ��'&&e.field=='XIAOSHETB_ID'){\n");
//			egu.addOtherScript("  e.cancel=true;\n");
//			egu.addOtherScript(" }\n");


			egu.addOtherScript("  });\n");
			egu.addOtherScript(" function caozpd(gridrecord){\n");
			egu.addOtherScript("gridDivInwindow_sm.clearSelections();\n");

//			egu.addOtherScript("	  if(er.value=='���ؽ���'){\n");
			egu.addOtherScript("	      if(gridrecord.get('YUNSFSB_ID')=='����'){\n");
			egu.addOtherScript("        haiypipei(gridrecord);\n");
			egu.addOtherScript("     }\n");
			egu.addOtherScript("     if(gridrecord.get('YUNSFSB_ID')=='��·'){   \n"); 
			egu.addOtherScript("        huocpipei(gridrecord);\n");
			egu.addOtherScript("    }\n");
			egu.addOtherScript("     Jieg_window.show();\n");
//			egu.addOtherScript("	  }\n");
			egu.addOtherScript("	 }\n");
	      this.setExtGridInWindow(egu);
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
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		// ɾ������
	
		
		sql = "begin \n";
		while (rs.next()) {
			
//			sql += "delete from chepb where fahb_id=" + rs.getString("id")
//					+ ";\n";
			if(!rs.getString("leix_id").equals("���ؽ���")){//�����ڳ��ؽ���
				//д����־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,visit.getExtGrid1().getMokmc(),
						"fahb",rs.getLong("id")+"");
				sql += "delete from fahb where id =" + rs.getLong("FAHB_ID") + ";\n";
				sql +="delete from fayslb where id="+rs.getLong("fayslb_id")+";\n";
				
			}else{
				sql+="update fayslb set daohrq=null where id="+rs.getLong("FAYSLB_ID")+";\n";
			}
			//�����ֱ��ĸ���duimxxb,qumxx,duowkcb
			if(rs.getString("leix_id").equals("ֱ��")){
				sql+="delete from duowkcb where duiqm_id=(select id from qumxxb where zhuangcb_id="+rs.getLong("FAYSLB_ID")+");\n";
				sql+="delete from duowkcb where duiqm_id=(select id from duimxxb where fahb_id="+rs.getLong("fahb_id")+");\n";
				sql+="delete from duimxxb where fahb_id="+rs.getLong("fahb_id")+";\n";
				sql+="delete from qumxxb where zhuangcb_id="+rs.getLong("FAYSLB_ID")+";\n";
			}
//			ɾ����Ӧ���������ݸ���Ϊ-2��ʾ������initgrid()����ʾ.
			if(rs.getString("leix_id").equals("���ؽ���")){
				sql +=" update fahbtmp set  FAYSLB_ID=-2 where id="+rs.getLong("id")+";\n";
			}else{
				sql +="update fahbtmp set  fahb_id=-2,FAYSLB_ID=-2 where id="+rs.getLong("id")+";\n";	
			}
			
		}
		sql += "end;\n";
		if (rs.getRows() > 0) {
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
//		 List	fahb_idList=new ArrayList();//������ú�ݹ�
//		 List   fayslb_idList=new ArrayList();
		// �޸�����
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		 List	fahb_idList=new ArrayList();//������ú�ݹ�
		 List   fayslb_idList=new ArrayList();
//		 long[][] fahb_shu=new long[rs.getRows()][2];
//		 long[][]  fayslb_shu=new long[rs.getro]
		while (rs.next()) {
			
			long id = rs.getLong("id");
			long fahr=getExtGrid().getColumn("fahr").combo.getBeanId(rs.getString("fahr"));
			long gongysb_id = getExtGrid().getColumn("gongys").combo.getBeanId(rs
					.getString("gongys"));
			long meikxxb_id=getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rs.getString("meikxxb_id"));
			long pinzb_id = getExtGrid().getColumn("pinz").combo.getBeanId(rs
					.getString("pinz"));
			long faz_id = getExtGrid().getColumn("faz").combo.getBeanId(rs
					.getString("faz"));
			long daoz_id = getExtGrid().getColumn("daoz").combo.getBeanId(rs.getString("daoz"));
			long YUANSHDWB_ID=getExtGrid().getColumn("shouhr").combo.getBeanId(rs.getString("shouhr"));
			String fahrq = DateUtil.FormatOracleDate(rs.getString("fahrq"));

			String strriq = rs.getString("jiexrq") + " "
					+ rs.getString("jiexs") + ":" + rs.getString("jiexf")
					+ ":00";
			String daohrq = DateUtil.FormatOracleDateTime(strriq);
			strriq = rs.getString("guohrq") + " " + rs.getString("guohs") + ":"
					+ rs.getString("guohf") + ":00";
			String guohsj = DateUtil.FormatOracleDateTime(strriq);
			String chec = rs.getString("chec");
			int ches = rs.getInt("ches");
			double maoz = rs.getDouble("maoz");
			double piz = rs.getDouble("piz");
			double biaoz = rs.getDouble("biaoz");
//			double jingz = CustomMaths.sub(maoz, piz);
			double jingz=rs.getDouble("jingz");
			double yunsl = getYunsl(visit.getDiancxxb_id(), pinzb_id,
					SysConstant.YUNSFS_HUOY);
			double yingd = 0.0;
			double kuid = 0.0;
//			double yingk = CustomMaths.sub(jingz, biaoz);
			double yingk=rs.getDouble("yingk");
//			double yuns = CustomMaths.mul(biaoz, yunsl);
			double yuns=rs.getDouble("yuns");
			long FAYSLB_ID=rs.getLong("FAYSLB_ID");//fayslb_id
			long fahb_id=rs.getLong("fahb_id");
			
			long leix_id=getExtGrid().getColumn("leix_id").combo.getBeanId(rs.getString("leix_id"));
			long hetb_id=getExtGrid().getColumn("hetb_id").combo.getBeanId(rs.getString("hetb_id"));
			long yunsfsb_id=getExtGrid().getColumn("yunsfsb_id").combo.getBeanId(rs.getString("yunsfsb_id"));
			String chuanm=rs.getString("chuanm");
			long XIAOSHETB_ID=0;
			XIAOSHETB_ID=getExtGrid().getColumn("XIAOSHETB_ID").combo.getBeanId(rs.getString("XIAOSHETB_ID"));
			long  jihkjb_id=getExtGrid().getColumn("jihkjb_id").combo.getBeanId(rs.getString("jihkjb_id"));
		    long  keh_diancxxb_id=getExtGrid().getColumn("keh_diancxxb_id").combo.getBeanId(rs.getString("keh_diancxxb_id"));
//			shouhr[rs.getRow()][0]=fahr;
//			shouhr[rs.getRow()][1]=YUANSHDWB_ID;
			
			
			// ����ӯ������
			/*
			if (yingk >= 0) {
				yuns = 0;
				yingd = yingk;
				kuid = 0;
			} else {
				if (Math.abs(yingk) <= yuns) {
					yuns = Math.abs(yingk);
					yingd = 0;
					kuid = 0;
				} else {
					// Yuns = Yuns;
					yingd = 0;
					kuid = CustomMaths.sub(Math.abs(yingk), yuns);
				}
			}
			yingk = yingd - kuid;
			*/
	 	 
			if (id == 0) {

				 if(leix_id!=2){//����Ϊ���ؽ���Ĵ�����
//						���û��id�����µ�id
				       try {
							         id =this.getnewId();
						   } catch (Exception e) {
							e.printStackTrace();
							this.setMsg("�������");
							return;
						  }
						   fahb_id=id;
						  
//						 д����־
							MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
									SysConstant.RizOpType_UP,visit.getExtGrid1().getMokmc(),
									"fahb",rs.getLong("id")+"");
				    sql += "insert into fahb(id,gongysb_id,pinzb_id,faz_id,daoz_id,"
						+ "fahrq,daohrq,guohsj,chec,ches,maoz,piz,biaoz,jingz,"
						+ "yingk,yingd,yuns,yunsl,hetb_id,yunsfsb_id,luncxxb_id,diancxxb_id,meikxxb_id,YUANSHDWB_ID,lie_id,yewlxb_id,NEIBCG,jihkjb_id) values("
						+ fahb_id
						+ ","
						+ gongysb_id
						+ ","
						+ pinzb_id
						+ ","
						+ faz_id
						+ ","
						+ daoz_id
						+ ","
						+ fahrq
						+ ","
						+ daohrq
						+ ","
						+ guohsj
						+ ",'"
						+ chec
						+ "',"
						+ ches
						+ ","
						+ maoz
						+ ","
						+ piz
						+ ", "
						+ biaoz
						+ ","
						+ jingz
						+ ","
						+ yingk
						+ ","
						+ yingd
						+ ","
						+ yuns
						+ ","
						+ yunsl
						+","
						+hetb_id
						+","
						+yunsfsb_id
						+",'"
						+getExtGrid().getColumn("chuanm").combo.getBeanId(chuanm)
						+ "'," 
						+fahr
						+","
						+meikxxb_id
						+","
						+YUANSHDWB_ID
						+","
						+"TO_NUMBER('199'||'"+id+"')"
						+","+leix_id
						+	",nvl((select NEIBCG from gongysb gy where gy.id="+gongysb_id+"),1),"+jihkjb_id+");\n";
//			    	�Զ������ݹ�����
				         
			    	  fahb_idList.add(new ShoumcbBean(fahr,fahb_id));
				      if(leix_id==3){//ֱ��ú��

				    	  
				    	  //�Զ����
				    	  long newid;
				    	  long fayslb_newid;
				    	  try{
				    		  newid =this.getnewId();
						   } catch (Exception e) {
							e.printStackTrace();
							this.setMsg("�������");
							return;
						  }
				    	  sql+="insert into duimxxb (id,fahb_id,ruksl,meicb_id,rukkssj,ruksj,caozy,zhuangt)values(\n"
				    		  +newid+",\n"
				    	  	  +fahb_id+",\n"
				    	  	  +jingz+",\n"
				    	  	  +"(select id from meicb where  mingc='ֱ��ú��' and diancxxb_id=199 ),\n"
				    	  	  +daohrq+",\n"
				    	  	  +daohrq+",\n"
				    	  	  +"'"+visit.getRenymc()+"',\n"
				    	  	  +"1"
				    	      + ");\n";
				    	  sql+="insert into duowkcb(id,diancxxb_id,riq,duiqm_id,meicb_id,leix,pinz,shij,shul,kucl,beiz,ruksl,leib,ches,biaoz,PANYK)values(\n" 
				    		  +"getnewid("+visit.getDiancxxb_id()+"),\n" 
							  +fahr+ ",\n"
							  +daohrq+",\n"
							  +newid+",\n"
							  +"(select id from meicb where  mingc='ֱ��ú��' and diancxxb_id=199 ),\n"
							  +"1,"
							  +"'"+rs.getString("pinz")+"',\n"
							  +daohrq+","
							  +jingz+","
							  +jingz+","
							  +"'ֱ��',"
							  +jingz
							  +",3"
							  +","
							  +ches
							  +","
							  +biaoz
							  +","
							  +yingk
				    	  	  +");\n";
				    	  //�Զ�����
				    	  
				    	  try{
				    		  newid =this.getnewId();
				    		  FAYSLB_ID=fayslb_newid=this.getnewId();
				    		  fayslb_idList.add(new FaycbBean(YUANSHDWB_ID,FAYSLB_ID));
				    		  
						   } catch (Exception e) {
							e.printStackTrace();
							this.setMsg("�������");
							return;
						  }
						   double meil=biaoz;
//						 ֱ��cun  ��λ�����  ����,Ʊ��    ӯ��
						   sql+="insert into fayslb(id, diancxxb_id, chec, luncxxb_id, chukkssj, chukjssj, fahrq, faz_id, daoz_id, meil, shr_diancxxb_id, lurry, lursj, beiz, pinzb_id, daohrq, yunsfsb_id, gongysb_id, meikxxb_id, hetb_id,NEIBXS,YEWLXB_ID,JIHKJB_ID,querdhrq,keh_diancxxb_id)\n"+
                                 "values\n"+
                                 "("+fayslb_newid+", "+fahr+",'"+chec+"', "+getExtGrid().getColumn("chuanm").combo.getBeanId(chuanm)+", "+fahrq+", "+fahrq+"," +
                                 fahrq+","+ faz_id+","+daoz_id+","+meil+","+ YUANSHDWB_ID +
                                 ",'"+ visit.getRenymc()+"',sysdate,'',"+ pinzb_id+","+daohrq+","+yunsfsb_id+","
                                 +gongysb_id+","+meikxxb_id+","+ XIAOSHETB_ID+",nvl((select NEIBXS from diancxxb where id="+YUANSHDWB_ID+"),1),3,"+jihkjb_id+","+DateUtil.FormatOracleDate(DateUtil.FormatDate(new Date()))+","+keh_diancxxb_id+");\n";
						   
						   sql+="insert into qumxxb(id,zhuangcb_id,chuksl,meicb_id,chukkssj,chukjssj,caozy,qumyy)values(" 
							   +newid+","
							   +fayslb_newid+","
							   +jingz+","
							   +"(select id from meicb where  mingc='ֱ��ú��' and diancxxb_id=199 ),\n"
							   +daohrq+",\n"
							   +daohrq+",\n"
							   +"'"+visit.getRenymc()+"',\n"
							   +"'ֱ��'"
						   	   +");\n";
						   sql+="insert into duowkcb(id,diancxxb_id,riq,duiqm_id,meicb_id,leix,pinz,shij,shul,kucl,leib,chuksl,ches,biaoz,PANYK)values(\n" 
					    		  +"getnewid("+visit.getDiancxxb_id()+"),\n" 
								  +fahr+ ",\n"
								  +daohrq+",\n"
								  +newid+",\n"
								  +"(select id from meicb where  mingc='ֱ��ú��' and diancxxb_id=199 ),\n"
								  +"-1,"
								  +"'"+rs.getString("pinz")+"',\n"
								  +daohrq+","
								  +jingz+","
								  +"0,"
								  +"3,"
								  +jingz
								  +","
								  +ches
								  +","
								  +biaoz
								  +","
								  +yingk
					    	  	  +");\n";
						   
						   
				      }
				   }else{
					   sql+="update fayslb set daohrq="+daohrq+" where id="+FAYSLB_ID+";\n ";
				   }
				 if(rs.getString("leix_id").equals("���ؽ���")){//���ؽ���ֻ�������ۺ�ͬ
					   if(hetb_id>0){
						    hetb_id=0;
						    this.setMsg("���ؽ���ֻ�������ۺ�ͬ,��ú��ͬ�����");
					   }
						   
					   
				 }
				
				 String neweid=MainGlobal.getNewID(visit.getDiancxxb_id());
					   sql += "insert into fahbtmp(id,gongysb_id,pinzb_id,faz_id,daoz_id,"
							+ "fahrq,daohrq,guohsj,chec,ches,maoz,piz,biaoz,jingz,"
							+ "yingk,yingd,yuns,yunsl,hetb_id,leix_id,fayslb_id,fahb_id,yunsfsb_id,shujly,chuanm,diancxxb_id,meikxxb_id,YUANSHDWB_ID,lie_id,XIAOSHETB_ID,jihkjb_id) values("
							+neweid
						  
							+ ","
							+ gongysb_id
							+ ","
							+ pinzb_id
							+ ","
							+ faz_id
							+ ","
							+ daoz_id
							+ ","
							+ fahrq
							+ ","
							+ daohrq
							+ ","
							+ guohsj
							+ ",'"
							+ chec
							+ "',"
							+ ches
							+ ","
							+ maoz
							+ ","
							+ piz
							+ ", "
							+ biaoz
							+ ","
							+ jingz
							+ ","
							+ yingk
							+ ","
							+ yingd
							+ ","
							+ yuns
							+ ","
							+ yunsl
							+","
							+hetb_id
							+","
							+leix_id
							+","
							+FAYSLB_ID//��Ϊfayslb_id
							+","
							+fahb_id
							+","
							+yunsfsb_id
							+","
							+"'�ֹ�¼��'"//��ҳ��ֻ�����ֹ�¼�������
							+",'"
							+chuanm
							+ "',"
							+fahr
							+","
							+meikxxb_id
							+","
							+YUANSHDWB_ID
							+",TO_NUMBER('199'||'"+neweid+"')"
							+","
							+XIAOSHETB_ID
							+","
							+jihkjb_id
							+ ");\n";
				        
			}
//			else {
//				 if(leix_id!=2){
////					д����־
//						MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
//								SysConstant.RizOpType_UP,visit.getExtGrid1().getMokmc(),
//								"fahb",rs.getLong("id")+"");
//				sql += "update fahb set \n" + " gongysb_id = " + gongysb_id
//						+ ",\n" + " pinzb_id = " + pinzb_id + ",\n"
//						+ " faz_id = " + faz_id + ",\n" + " daoz_id = "
//						+ daoz_id + ",\n" + " fahrq = " + fahrq + ",\n"
//						+ " daohrq = " + daohrq + ",\n" + " guohsj = " + guohsj
//						+ ",\n" + " chec = '" + chec + "',\n" + " ches = "
//						+ ches + ",\n" + " maoz = " + maoz + ",\n" + " piz = "
//						+ piz + ",\n" + " biaoz = " + biaoz + ",\n"
//						+ " jingz = " + jingz + ",\n" + " yingk = " + yingk
//						+ ",\n" + " yingd = " + yingd + ",\n" + " yuns = "
//						+ yuns + ",\n" + " yunsl = " + yunsl + "\n"
//						+",hetb_id="+hetb_id+"\n"
//						
//						+",luncxxb_id='"+getExtGrid().getColumn("chuanm").combo.getBeanId(chuanm)+"'\n"
//						+",diancxxb_id="+fahr+",meikxxb_id="+meikxxb_id
//						+",YUANSHDWB_ID="+YUANSHDWB_ID
//						+",yunsfsb_id="+yunsfsb_id
//						+ " where id=" + fahb_id + ";\n";
//				}
//		  sql+="update fahbtmp set \n" + " gongysb_id = " + gongysb_id
//				+ ",\n" + " pinzb_id = " + pinzb_id + ",\n"
//				+ " faz_id = " + faz_id + ",\n" + " daoz_id = "
//				+ daoz_id + ",\n" + " fahrq = " + fahrq + ",\n"
//				+ " daohrq = " + daohrq + ",\n" + " guohsj = " + guohsj
//				+ ",\n" + " chec = '" + chec + "',\n" + " ches = "
//				+ ches + ",\n" + " maoz = " + maoz + ",\n" + " piz = "
//				+ piz + ",\n" + " biaoz = " + biaoz + ",\n"
//				+ " jingz = " + jingz + ",\n" + " yingk = " + yingk
//				+ ",\n" + " yingd = " + yingd + ",\n" + " yuns = "
//				+ yuns + ",\n" + " yunsl = " + yunsl + "\n"
//				+",leix_id="+leix_id+"\n"
//				+",hetb_id="+hetb_id+"\n"
//				+",chuanm='"+chuanm+"'\n"
//				+",diancxxb_id="+fahr+",meikxxb_id="+meikxxb_id
//			     +",YUANSHDWB_ID="+YUANSHDWB_ID
//			     +",yunsfsb_id="+yunsfsb_id
//			     +",fayslb_id="+FAYSLB_ID
//			     +",fahb_id="+fahb_id
//				+ " where id=" + id + ";\n";
//			}
			

		}
		sql += "end;";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			
			
			if (flag == -1) {
				con.rollBack();
				return;
			}else{
				con.commit();
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				
//				�������ִ�гɹ���,�����ݹ�����(��ֱ��)
				
				ShoumzgInfo.CountChengb(fahb_idList,true);
//				FayzgInfo.  CountChengb(fayslb_idList,true);
			}
				
			
		}
		rs.close();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	public static double getYunsl(long diancxxb_id, long pinzb_id,
			int yunsfsb_id) {
		JDBCcon con = new JDBCcon();
		String sql = "";
		ResultSetList rsl;
		double yunsl = 0.012;
		String fs = "";
		if (yunsfsb_id == SysConstant.YUNSFS_HUOY) {
			yunsl = 0.012;
			fs = "��";
		} else if (yunsfsb_id == SysConstant.YUNSFS_QIY) {
			yunsl = 0.01;
			fs = "����";
		}
		sql = "select * from xitxxb where mingc ='Ĭ��������' and danw ='"
				+ fs
				+ "' and leib = '����' and beiz ='ʹ��' and zhuangt =1 and diancxxb_id="
				+ diancxxb_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			yunsl = rsl.getDouble("zhi");
		}
		sql = "select yunsl from yunslb where diancxxb_id=" + diancxxb_id
				+ " and pinzb_id=" + pinzb_id + " and yunsfsb_id ="
				+ yunsfsb_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			yunsl = rsl.getDouble("yunsl");
		}
		return yunsl;
	}

	private void init() {
		setExtGrid(null);
		setExtGridInWindow(null);
		
		initGridInWindow();
		this.setYewlxModel(null);
		setYewlxValue(null);
		
	}
//����ҵ�����������˵�
	public IPropertySelectionModel getYewlxModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null){
			this.setYewlxModel(this.getYewlxModels());
		}
			return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
		
	}
	public void setYewlxModel(IPropertySelectionModel YewlxModel){
		((Visit)this.getPage().getVisit()).setProSelectionModel1(YewlxModel);
	}
	public IDropDownBean getYewlxValue(){
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null){
		this.setYewlxValue( (IDropDownBean)this.getYewlxModel().getOption(0));
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setYewlxValue(IDropDownBean YewlxValue){
		((Visit)this.getPage().getVisit()).setDropDownBean11(YewlxValue);
	}
	public IPropertySelectionModel getYewlxModels(){
		return new IDropDownModel("select id,mingc from yewlxb");
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			visit.setActivePageName(getPageName().toString());
			init();
		}
		initGrid();
		initGridInWindow();
		((Visit)this.getPage().getVisit()).getExtGrid1().setMokmc(SysConstant.RizOpMokm_Rlgs_Shujqr);
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
	private long getnewId()throws Exception{
		   long id=0;
           Visit visit=(Visit)this.getPage().getVisit();		
		   JDBCcon con=new JDBCcon();
		   ResultSet rsid=con.getResultSet("select getnewid("+visit.getDiancxxb_id()+") from dual");
			if(rsid.next()){  
			         id =rsid.getLong(1);
			         rsid.close();
			         con.Close();
			 }else{
				 con.rollBack();
				 throw new Exception();
			 }
		  
			this.setMsg("�������");
			return id;
		  
	}
private	class IGridButton extends GridButton{
		public IGridButton(int btnType,String parentId,List columns,String tapestryBtnId,String condition){
			super( btnType, parentId, columns, tapestryBtnId,condition);
		}
		public String getDeleteScript() {
			StringBuffer record = new StringBuffer();
			record.append("function() {\n");
			record.append( super.condition+"\n");
			record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
			record.append("	record = "+parentId+"_sm.getSelections()[i];\n");
			
			StringBuffer sb = new StringBuffer();
			//sb.append(b);
			sb.append(parentId).append("_history += '<result>' ")
			.append("+ '<sign>D</sign>' ");
			
			for(int c=0;c<columns.size();c++) {
				if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
					GridColumn gc = ((GridColumn)columns.get(c));
					if(gc.update) {
						if("date".equals(gc.datatype)) {
							sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
							.append(gc.dataIndex).append("'))?").append("record.get('")
							.append(gc.dataIndex).append("'):").append("record.get('")
							.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
							.append("+ '</").append(gc.dataIndex).append(">'\n");
						}else {
							sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
							if(!gc.datatype.equals(GridColumn.DataType_Float)) {
								sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
							}
							sb.append("+ '</").append(gc.dataIndex).append(">'\n");
						}
					}
				}
			}
			sb.append(" + '</result>' ;");
			record.append(sb);
			
			record.append("	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);}}");
			return record.toString();
		}
	}
}

 