package com.zhiren.dc.gdxw.caiychxg;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 2009-10-24 11:31:09
 * ���ܱ�
 * �غ���Ա��ú����������Ժ�,��Ƥ��Ա���ֺ�,֪ͨ�غ���Ա�����޸�ú�����ƽ���
 */
public class Zhonghxgmk extends BasePage implements PageValidateListener {
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

		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ���Ŷ��");
			return;
		}
	
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.NullResult + "\n");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if (rsl.next()) {
			
			
			String meikdwmc = rsl.getString("meikdwmc");
			String cheph = rsl.getString("cheph");
			String sql="update chepbtmp set cheph='"+cheph+"', gongysmc='"+meikdwmc+"',meikdwmc='"+meikdwmc+"'"+
					"  where id="+rsl.getLong("id")+"";
			flag = con.getUpdate(sql);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"���³�Ƥ��TMPʧ��!");
				setMsg(this.getClass().getName() + ":���³�Ƥ��TMPʧ��!");
				return;
			}
			
		}
		con.commit();
		con.Close();
		setMsg("�޸ĳɹ�");
	
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		if("".equals(getChehao())){
			sb.append("select c.id,c.cheph,c.meikdwmc,c.maoz,to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,c.zhongcjjy  from chepbtmp c where \n");
			sb.append(" c.zhongcsj is not null and c.qingcsj is null and c.isjus=0 \n");
			sb.append("  order by c.zhongcsj");
		}else{
			sb.append("select c.id,c.cheph,c.meikdwmc ,c.maoz,to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,c.zhongcjjy from chepbtmp c where \n");
			sb.append(" c.zhongcsj is not null and c.qingcsj is null and c.isjus=0 \n");
			sb.append(" AND c.cheph like '%"+this.getChehao()+"%'\n");
			sb.append("  order by c.zhongcsj");
		}
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�����Ƿ���Ա༭
		egu.setTableName("chepbtmp");
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		//egu.getColumn("cheph").setEditor(null);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikdwmc").setWidth(130);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("zhongcsj").setHeader("�س�ʱ��");
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setWidth(120);
		egu.getColumn("zhongcjjy").setHeader("�س����Ա");
		egu.getColumn("zhongcjjy").setEditor(null);
		
		
		//����Grid����
		egu.addPaging(100);
		
//		����ú��������
		ComboBox cmk= new ComboBox();
		egu.getColumn("meikdwmc").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql="select id,piny || '-' ||mingc from meikxxb order by xuh";
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId, new IDropDownModel(mkSql));
	
		
		
	
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("���Ų���:");
		TextField tf=new TextField();
		tf.setWidth(80);
		tf.setValue(getChehao());
		
		tf.setListeners("change:function(own,n,o){document.getElementById('Chehao').value = n}");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
	
	
//	 ˢ�°�ť
	GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
			"gridDiv", egu.getGridColumns(), "RefurbishButton");
	egu.addTbarBtn(refurbish);
	egu.addTbarText("-");

		setExtGrid(egu);
		con.Close();
	}

	
	private String chehao = "";
	public String getChehao(){
		return chehao;
	}
	public void setChehao(String ch){
		chehao = ch;
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
