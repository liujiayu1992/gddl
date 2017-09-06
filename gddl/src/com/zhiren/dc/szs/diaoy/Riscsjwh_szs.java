package com.zhiren.dc.szs.diaoy;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.Locale;
import com.zhiren.dc.diaoygl.AutoCreateShouhcrb;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
public class Riscsjwh_szs extends BasePage implements PageValidateListener {
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
//		if (getChange() == null || "".equals(getChange())) {
//			setMsg("û����Ҫ����ļ�¼��");
//			return;
//		}
		StringBuffer sb=new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag=0;
		String id = "0";
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Riscsjwh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			con.Close();
			return;
		}
		
		sb.append("begin ");
		String riq = "";
		int shangwdl = 0;
		while(rsl.next()) {
			id=rsl.getString("id");
			if(id.equals("0")){
				riq = getRiqi();
				if(rsl.getRow()==1){
					shangwdl = Integer.parseInt(getShangwdl());
				}else{
					shangwdl = 0;
				}
				sb.append("insert into riscsjb (id,diancxxb_id,riq,jizb_id,fadl,gongdl,shangwdl,gongrl,fadfhl)");
				sb.append("values (getnewid(").append(getTreeid()).append("),").append(getTreeid()).append(",").append(DateUtil.FormatOracleDate(riq)).append(",'");
				sb.append((getExtGrid().getColumn("jizb_id").combo).getBeanId(rsl.getString("jizb_id"))).append("',").append(rsl.getDouble("fadl")).append(",");
				sb.append(rsl.getDouble("gongdl")).append(",").append(shangwdl).append(",").append(rsl.getDouble("gongrl")).append(",").append(rsl.getDouble("fadfhl")).append(");") ;
			}else{
				if(rsl.getRow()==1){
					shangwdl = Integer.parseInt(getShangwdl());
				}else{
					shangwdl = 0;
				}
				sb.append("update riscsjb set jizb_id=")
				  .append((getExtGrid().getColumn("jizb_id").combo).getBeanId(rsl.getString("jizb_id")))
				  .append(",fadl=").append(rsl.getDouble("fadl"))
				  .append(",gongrl=").append(rsl.getDouble("gongrl"))
				  .append(",gongdl=").append(rsl.getDouble("gongdl"))
				  .append(",shangwdl=").append(shangwdl)
				  .append(",fadfhl=").append(rsl.getDouble("fadfhl"))
				  .append(" where id=").append(id).append(";");
			}
		}
		sb.append("end;");
		flag = con.getInsert(sb.toString());
		if(flag==-1){
			con.rollBack();
			con.Close();
		}
		if (flag!=-1){//����ɹ�
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
//		AutoCreateShouhcrb.Create(con,visit.getDiancxxb_id(),DateUtil.getDate(riq));
		con.commit();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon(); 
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(
			"select decode(r.id,null,0,r.id) id,j.diancxxb_id,j.jizbh jizb_id,r.fadl,r.gongdl,r.gongrl,r.fadfhl\n" +
			"  from jizb j, riscsjb r\n" + 
			" where j.diancxxb_id = "+getTreeid()+"\n" + 
			"   and r.riq(+) = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
			"   and r.jizb_id(+)=j.id");
		ResultSetList rsl = con.getResultSetList(sbsql.toString());

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("riscsjb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("diancxxb_id").setWidth(80);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("jizb_id").setHeader("������");
		egu.getColumn("jizb_id").setWidth(100);
		egu.getColumn("fadl").setHeader("������(ǧ��ʱ)");
		egu.getColumn("fadl").setWidth(100);
		egu.getColumn("gongdl").setHeader("������(ǧ��ʱ)");
		egu.getColumn("gongdl").setWidth(100);
		egu.getColumn("gongrl").setHeader("������(����)");
		egu.getColumn("gongrl").setWidth(100);
		egu.getColumn("fadfhl").setHeader("���縺����(%)");
		egu.getColumn("fadfhl").setWidth(100);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
//		 ���û���������
		ComboBox c1 = new ComboBox();
		egu.getColumn("jizb_id").setEditor(c1);
		c1.setEditable(true);
		String Sql = new String();
		//�糧��Ա���������ܳ�������ݣ�ʹ�ô������ݵ����ܳ�ID������ú�պĴ��ձ��޷��Զ�ȡ�÷�����.
		String falg = MainGlobal.getXitxx_item("����", "���������ݵĻ����������ͬ��", String.valueOf(getTreeid()), "��");
		if(falg.equals("��")){
			Sql = "select id, jizbh from jizb where diancxxb_id = "+getTreeid();
			if(String.valueOf(visit.getDiancxxb_id()).equals(getTreeid())){
				setMsg("��ѡ�񳧱�");
			}
		}else{
			Sql = "select id, jizbh from jizb where diancxxb_id in (select id from diancxxb d where (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+"))";
		}
		egu.getColumn("jizb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql));

//		 ������
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		String saveScript = 
			"function(){\n" +
			" var gridDivsave_history = '';var Mrcd = gridDiv_ds.data.items;\n" + 
			"for(i = 0; i< Mrcd.length; i++){\n" + 
			"if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n" + 
			"if(Mrcd[i].get('JIZB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n" + 
			"}gridDivsave_history += '<result>' + '<sign>U</sign>' " +
			"+ '<ID update=\"true\">' + Mrcd[i].get('ID')+ '</ID>'\n" + 
			"+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID')+ '</DIANCXXB_ID>'\n" + 
			"+ '<JIZB_ID update=\"true\">' + Mrcd[i].get('JIZB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JIZB_ID>'\n" + 
			"+ '<FADL update=\"true\">' + Mrcd[i].get('FADL')+ '</FADL>'\n" + 
			"+ '<GONGDL update=\"true\">' + Mrcd[i].get('GONGDL')+ '</GONGDL>'\n" + 
			"+ '<GONGRL update=\"true\">' + Mrcd[i].get('GONGRL')+ '</GONGRL>'\n" + 
			"+ '<FADFHL update=\"true\">' + Mrcd[i].get('FADFHL')+ '</FADFHL>'\n" + 
			" + '</result>' ; }\n" + 
			"if(gridDiv_history=='' && gridDivsave_history=='' && theKey_text.getValue()==''){\n" + 
			"Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');\n" + 
			"}else{\n" + 
			"var Cobj = document.getElementById('CHANGE');\n" + 
			"Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';var objkey = document.getElementById('Shangwdl');objkey.value = theKey_text.getValue();document.getElementById('SaveButton').click();Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
			"}\n" + 
			"}";
		GridButton ss = new GridButton("����",saveScript);
		gbr.setIcon(SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(ss);

		if(!falg.equals("��")){
			String sql =
				"select sum(shangwdl) shangwdl\n" +
				"  from jizb j, riscsjb r\n" + 
				" where j.diancxxb_id = "+getTreeid()+"\n" + 
				"   and r.riq(+) = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"   and r.jizb_id(+)=j.id";
			rsl = con.getResultSetList(sql);
			rsl.next();
			
			egu.addTbarText("����ó�������������");
			NumberField theKey = new NumberField();
			theKey.setWidth(80);
			theKey.setValue(rsl.getString("shangwdl"));
			theKey.setId("theKey_text");
			egu.addToolbarItem(theKey.getScript());
		}
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
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

//	��
	public String getTreeid() {
		Visit visit = (Visit) this.getPage().getVisit();
		if (visit.getString10() == null || visit.getString10().equals("")) {
			visit.setString10(String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id()));
		}
		return visit.getString10();
	}
	public void setTreeid(String treeid) {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.setString10(treeid);
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

	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
	}
	
	public void setShangwdl(String value){
		try{
			((Visit) getPage().getVisit()).setInt1(Integer.parseInt(value));
		}catch(Exception e){
			((Visit) getPage().getVisit()).setInt1(0);
		}
	}
	public String getShangwdl(){
		return String.valueOf(((Visit) getPage().getVisit()).getInt1());
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0);
			setTbmsg(null);
			visit.setString10(String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id()));
		}
		getSelectData();
	}
}