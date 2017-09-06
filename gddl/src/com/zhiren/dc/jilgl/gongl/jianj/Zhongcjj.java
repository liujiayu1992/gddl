package com.zhiren.dc.jilgl.gongl.jianj;

import java.util.Date;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhongcjj extends BasePage implements PageValidateListener {
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
//	�볧��ˮ���б�
	public IDropDownBean getLiushValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getLiushModel().getOptionCount()>0) {
				setLiushValue((IDropDownBean)getLiushModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setLiushValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getLiushModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setLiushModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setLiushModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setLiushModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,piaojh from chepb where piaojh is not null and chebb_id = "+SysConstant.CHEB_QC+" and maoz=0");
		setLiushModel(new IDropDownModel(sb.toString()));
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Zhongcjj.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			String id = rsl.getString("id");
			double maoz = rsl.getDouble("maoz");
			double biaoz = rsl.getDouble("biaoz");
			String fahbid = rsl.getString("fahb_id");
			sb.delete(0, sb.length());
//			���³�Ƥ�� Ƥ�ء�Ʊ�ء���ע �����복Ƥ��
			sb.append("update chepb set maoz = ").append(maoz).append(",biaoz = ").append(biaoz);
			sb.append(",zhongcsj = ").append(DateUtil.FormatOracleDateTime(new Date()));
			sb.append(",zhongcjjy = '").append(rsl.getString("zhongcjjy")).append("',zhongchh = '").append(rsl.getString("zhongchh"));
			sb.append("' where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail+"SQL:"+sb);
				setMsg(ErrorMessage.Zhongcjj001);
				return;
			}
			
//			���ݵ���id ����jilcz ��CountChepbYuns �������㵥��������ӯ��
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ);
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Zhongcjj002);
				setMsg(ErrorMessage.Zhongcjj002);
				return;
			}
//			���ݳ�Ƥ����fahid ����Jilcz �� updateFahb �������·�����
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Zhongcjj003);
				setMsg(ErrorMessage.Zhongcjj003);
				return;
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Zhongcjj004);
				setMsg(ErrorMessage.Zhongcjj004);
				return;
			}
		}
		con.Close();
		setMsg("����ɹ���");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long cid = -1;
		if(getLiushValue() != null) {
			cid = getLiushValue().getId();
		}
		StringBuffer sb = new StringBuffer();
		
		sb.append("select c.id,c.fahb_id,c.biaoz,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') lursj,\n");
		sb.append("c.piaojh,y.bianm,c.cheph,c.maoz,c.piz,nvl('"+visit.getRenymc()+"','') zhongcjjy,c.zhongchh,c.qingcjjy,c.qingchh \n");
		sb.append("from chepb c, fahb f, caiyb y \n");
		sb.append("where c.fahb_id = f.id and f.zhilb_id = y.zhilb_id and c.maoz = 0\n");
		sb.append("and c.chebb_id = ").append(SysConstant.CHEB_QC).append(" and c.id = ").append(cid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		double piz=0;
		if (rsl.next()){
			piz = rsl.getDouble("piz");
		}
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.GridselModel_Row_single);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(453);
		egu.addPaging(16);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").editor=null;
//		�ж�Ƥ���Ƿ�Ϊ0�������0������ʾƤ�ء�
		if(piz==0){
			egu.getColumn("piz").setHidden(true);
			egu.getColumn("piz").editor=null;
		}else{
			egu.getColumn("piz").setHeader(Locale.piz_chepb);
			egu.getColumn("piz").setWidth(70);
			egu.getColumn("piz").setEditor(null);
		}
		egu.getColumn("biaoz").setHidden(true);
		egu.getColumn("biaoz").editor=null;
//		¼��ʱ��
		egu.getColumn("lursj").setHeader(Locale.lursj_chepb);
		egu.getColumn("lursj").setWidth(130);
		egu.getColumn("lursj").setEditor(null);
//		�������
		egu.getColumn("piaojh").setHeader(Locale.piaojh_chepb);
		egu.getColumn("piaojh").setWidth(110);
		egu.getColumn("piaojh").setEditor(null);
//		�������
		egu.getColumn("bianm").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("bianm").setWidth(90);
		egu.getColumn("bianm").setEditor(null);
//		��Ƥ��
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(90);
		egu.getColumn("cheph").setEditor(null);
//		ë��
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("maoz").setEditor(null);
//		�س����Ա
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(90);
		egu.getColumn("zhongcjjy").setEditor(null);
//		�س����
		egu.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("zhongchh").setWidth(80);
		egu.getColumn("zhongchh").setEditor(null);
//		�ᳵ���Ա
		egu.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu.getColumn("qingcjjy").setWidth(90);
		egu.getColumn("qingcjjy").setEditor(null);
//		�ᳵ���
		egu.getColumn("qingchh").setHeader(Locale.qingchh_chepb);
		egu.getColumn("qingchh").setWidth(80);
		egu.getColumn("qingchh").setEditor(null);
//		��ˮ��������		
		ComboBox liush = new ComboBox();
		liush.setTransform("LiushSelect");
		liush.setWidth(130);
		liush.setEditable(true);
		liush.setListeners("select:function(own,rec,index){Ext.getDom('LiushSelect').selectedIndex=index}");
		egu.addToolbarItem(liush.getScript());
//		���ð�ť
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
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
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
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
			init();
		}
	} 
	
	private void init() {
		setLiushValue(null);
		setLiushModel(null);
		setLiushModels();
		setExtGrid(null);
		getSelectData();
	}
}