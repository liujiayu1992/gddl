package com.zhiren.jt.shujsh;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-17
 * �������޸������޸������е糧��δ����ID�����⡣
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-14
 * �������޸��±�����״̬���ô���
 */
/*
 * 2009-05-14
 * ����
 * �޸����������޸�ʱ�Զ����÷ֹ�˾�ӿ��·����糧
 */
public class Shujxgsqpf extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
	}

//	 ������
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getRiq2() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}

    //ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _historyChick = false;
    public void HistoryButton(IRequestCycle cycle) {
    	_historyChick = true;
    }
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(_SaveChick){
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_historyChick){
			_historyChick = false;
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String riqb = DateUtil.FormatOracleDate(getRiqi());
		String riqe = DateUtil.FormatOracleDate(getRiq2());
		String sql = 
			"select s.id, s.shujid, s.riq, s.mokmc, z.shenqsj,\n" +
			"z.caozy, z.miaos, beiz\n" + 
			" from shujshb s,shujshzb z\n" + 
			"where z.shujshb_id = s.id and z.zhuangt =1\n" + 
			"and s.zhuangt = 2\n" + 
			"and s.diancxxb_id = "+getTreeid()+"\n" + 
			"and z.shenqsj >= "+riqb+"\n" + 
			"and z.shenqsj < "+riqe+" +1";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.addPaging(25);
//		���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		egu.getColumn("shujid").setHidden(true);
		
		egu.getColumn("riq").setHeader("��������");
		egu.getColumn("mokmc").setHeader("����ģ��");
		egu.getColumn("shenqsj").setHeader("����ʱ��");
		egu.getColumn("caozy").setHeader("������");
		egu.getColumn("miaos").setHeader("����ԭ��");
		egu.getColumn("beiz").setHeader("��ע");
		
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("mokmc").setWidth(100);
		egu.getColumn("shenqsj").setWidth(110);
		egu.getColumn("caozy").setWidth(100);
		egu.getColumn("miaos").setWidth(150);
		egu.getColumn("beiz").setWidth(100);
		
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("mokmc").setEditor(null);
		egu.getColumn("shenqsj").setEditor(null);
		egu.getColumn("caozy").setEditor(null);
		egu.getColumn("miaos").setEditor(null);
		egu.getColumn("beiz").setEditor(null);
		
//		egu.getColumn("miaos").
//		�������ڲ�ѯ
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		
		GridButton gbt = new GridButton("�鿴","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		GridButton gbs = new GridButton("��׼",GridButton.ButtonType_SubmitSel,egu.gridId,egu.getGridColumns(),"SaveButton");
		gbs.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(gbs);
		
//		GridButton gbh = new GridButton("��ʷ","function(){document.getElementById('HistoryButton').click();}");
//		gbh.setIcon(SysConstant.Btn_Icon_Search);
//		egu.addTbarBtn(gbh);
		
		String script="gridDiv_grid.on('celldblclick',function(own,row,col,e){sqms.setValue(gridDiv_ds.getAt(row).get('MIAOS'));Rpt_window.show();});";
		egu.addOtherScript(script);
		
		setExtGrid(egu);
		con.Close();

	}

	private void Save(){
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			MainGlobal.Shujshcz(con,getTreeid(),DateUtil.FormatOracleDate(rsl.getString("riq")),
					rsl.getString("shujid"),rsl.getString("mokmc"),v.getRenymc(),"");
			InterCom_dt ic = new InterCom_dt();
			String[] msg = ic.sqlExe(getTreeid(), new String[]{"update shujshb set zhuangt=0 where id=" +rsl.getString("id") }, false);
			if("true".equalsIgnoreCase(msg[0])){
				setMsg("�����ɹ�!");
			}else{
				setMsg(msg[0]);
			}
		}
		rsl.close();
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
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if(this.treeid != null && treeid !=null && !this.treeid.equals(treeid)){
			this.treeid = treeid;
			getSelectData();
		}
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
			setRiqi(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			visit.setActivePageName(getPageName().toString());
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			getSelectData();
		}
	}
}
