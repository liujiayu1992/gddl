package com.zhiren.dc.jilgl.shujdr;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * 2009-03-24
 * ��ɭ��
 * �����һ���ӿ������ϴ���û����ǰ���޸�ú��λ��Ʒ��
 */
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
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shujdrxg extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
	

	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1,
					DateUtil.AddType_intDay));
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}


	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
		
	}
	
	private void Save1(String strchange,Visit visit){
		String tableName="chepbtmp";
		JDBCcon con = new JDBCcon();
		if(strchange==null || "".equals(strchange)) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		con.setAutoCommit(false);
		ResultSetList rsld = getExtGrid().getDeleteResultSet(strchange);
		if(rsld == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Shujdrxg.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
//		ɾ����Ƥ
		while(rsld.next()) {
			String id = rsld.getString("ID");
			String sql = "delete from chepbtmp where id ="+id;
			int flag = con.getDelete(sql);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sql);
				return;
			}else{
				con.commit();
				con.Close();
				setMsg("ɾ���ɹ���");
			}
		}
		ExtGridUtil egu=getExtGrid();
		ResultSetList mdrsl =  visit.getExtGrid1().getModifyResultSet(strchange);
			  StringBuffer sql1 = new StringBuffer();
			  StringBuffer sql2 = new StringBuffer();
			  StringBuffer sql = new StringBuffer("begin \n");
			  while(mdrsl.next()) {
				  sql1.append("update ").append(tableName).append(" set ");
				  sql2.append("update ").append(tableName).append(" set ");
				  for(int i=1;i<mdrsl.getColumnCount();i++) {
					  if(mdrsl.getColumnNames()[i].equals("GONGYSMC")||mdrsl.getColumnNames()[i].equals("MEIKDWMC") ||
							  mdrsl.getColumnNames()[i].equals("YUANMKDW")){
						  sql1.append(mdrsl.getColumnNames()[i]).append(" = ");
						  sql1.append(egu.getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
					  }else if(mdrsl.getColumnNames()[i].equals("PINZ")){
						  sql1.append(mdrsl.getColumnNames()[i]).append(" = ");
						  sql1.append(egu.getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
					  }else if(mdrsl.getColumnNames()[i].equals("PIZ")){
						  sql1.append(" piz= ");
						  sql1.append(egu.getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
						  sql2.append("biaoz=maoz-piz").append(",");
					  }
//				sql.deleteCharAt(sql.length() - 1);
				  }
				  sql1.append(" fahbtmp_id=-1 where id =").append(mdrsl.getString("ID")).append(";\n");
				  sql2.append(" fahbtmp_id=-1 where id =").append(mdrsl.getString("ID")).append(";\n");				
			  }
			  sql.append(sql1).append(sql2);
			  sql.append("end;");
			  con.getUpdate(sql.toString());
			  int flg= con.getUpdate(sql.toString());
			  if(flg == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sql);
					return;
				}else{
					con.Close();
					setMsg("����ɹ���");
				}
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id, c.diancxxb_id, c.gongysmc, c.meikdwmc, c.faz,\n");
		sb.append("c.pinz, c.jihkj, c.fahrq, c.daohrq, c.jianjfs,c.chebb_id,\n");
		sb.append("c.chec,caiybh, c.zhongcjjy, c.cheph, c.maoz, c.piz, c.biaoz,\n");
		sb.append("c.koud, c.kous, c.kouz, c.sanfsl, c.daoz,yuandz,c.yuanshdw,\n");
		sb.append("c.meikdwmc yuanmkdw, c.yunsdw yunsdw, c.daozch, c.beiz  from chepbtmp c where ");
		sb.append(" fahb_id = 0");
		sb.append(" and daohrq = to_date('"+ riqTiaoj+ "','yyyy-mm-dd')") ;
		sb.append(" order by c.gongysmc,c.meikdwmc,c.zhongcsj");
		
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("chepbtmp");
		
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcsb="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcsb));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("gongysmc").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("faz").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz").setWidth(65);
		egu.getColumn("pinz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinz").setWidth(50);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("jihkj").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkj").setWidth(65);
		egu.getColumn("jihkj").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("jianjfs").setHeader(Locale.jianjfs_chepb);
		egu.getColumn("jianjfs").setWidth(60);
		egu.getColumn("jianjfs").setEditor(null);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chebb_id").setEditor(null);
		egu.getColumn("chebb_id").returnId=false;
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("caiybh").setHeader(Locale.caiybm_caiyb);
//		egu.getColumn("qingcsj").setHidden(true);
//		egu.getColumn("qingcsj").editor = null;
//		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		egu.getColumn("biaoz").setEditor(null);
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
		egu.getColumn("daoz").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz").setWidth(65);
		egu.getColumn("yuandz").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz").setWidth(65);
		egu.getColumn("yuanshdw").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		//����Ʒ��������
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinz").setEditor(c2);
		c2.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		egu.getColumn("pinz").returnId=false;
		//���ÿھ�������
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkj").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jihkj").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjsb));
		egu.getColumn("jihkj").returnId=false;
		//���ü�﷽ʽ������
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "����"));
		l.add(new IDropDownBean(1, "���"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("jianjfs").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("jianjfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("jianjfs").returnId=false;

		//���ó���������
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1,"·��"));
		ls.add(new IDropDownBean(2, "�Ա���"));
		ls.add(new IDropDownBean(3, "����"));
		ComboBox c5 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ls));

		//����ԭ��վ������
		ComboBox c6 = new ComboBox();
		egu.getColumn("yuandz").setEditor(c6);
		c6.setEditable(true);
		String Yuandzsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz").setComboEditor(egu.gridId,
				new IDropDownModel(Yuandzsb));
		egu.getColumn("yuandz").setDefaultValue(visit.getDaoz());
		egu.getColumn("yuandz").returnId=false;
		
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����ÿҳ��ʾ����
		egu.addPaging(25);
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

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
			getSelectData();
		}
	}
}
