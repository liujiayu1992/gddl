package com.zhiren.shanxdted.meigzxbb;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meizhc extends BasePage implements PageValidateListener {

//	����
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {
		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq)) {
			((Visit) this.getPage().getVisit()).setString1(riq);
		}
	}
	//����
	protected void initialize() {
		msg = "";
	}
	private String msg = "";
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public void Save() {
		String strchange = getChange();
		JDBCcon con = new JDBCcon();
		String sql = new String();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			sql = 
				"update hemshc set\n" +
				"              jinml =" + mdrsl.getString("jinml") + ",\n" + 
				"              panml  =" + mdrsl.getString("panml") + ",\n" + 
				"              haoml  =" + mdrsl.getString("haoml") + ",\n" + 
				"              qimkc  =" + mdrsl.getString("qimkc") + "\n" + 
				" where diancxxb_id="+ getChangbValue().getId() +"\n" + 
				"   and riq=to_date('" + mdrsl.getString("riq") + "','yyyy-mm-dd')\n" + 
				"   and leix='��ú'";
		}
		mdrsl.close();
		if(sql.length()>0){
			if(con.getUpdate(sql) >= 0){
				setMsg("����ɹ���");
			}else {
				setMsg("����ʧ�ܣ�");
			}
		}else {
			setMsg("����ʧ�ܣ�");
		}
		con.Close();
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		} else if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
	}
	
	private void CreateData(){
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = new ResultSetList();
		ResultSetList rsl_tmp = new ResultSetList();
		String sql = new String();
		con.setAutoCommit(false);
		sql = //�����Ƿ���ֵ
			"select jinml,panml,haoml,qimkc from hemshc\n" + 
			"  where diancxxb_id="+ getChangbValue().getId() +" and riq=to_date('"+ getRiq() +"','yyyy-mm-dd') and leix='��ú'";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			sql = 
				"select jinml,panml,haoml,qimkc from hemshc\n" + 
				"  where diancxxb_id="+ getChangbValue().getId() +" and riq=to_date('"+ getRiq() +"','yyyy-mm-dd')-1 and leix='��ú'";
			rsl_tmp = con.getResultSetList(sql);
			if(rsl_tmp.next()){//���������춼��ֵ�����
				sql = 
					"delete hemshc\n" + 
					"  where diancxxb_id="+ getChangbValue().getId() +" and to_char(riq,'yyyy-mm-dd')='"+ getRiq() +"' and leix='��ú'";
				con.getDelete(sql);
				sql = 
					"insert into hemshc values(\n" +
					getChangbValue().getId() +",to_date('"+ getRiq() +"','yyyy-mm-dd'),'��ú'," +
					rsl.getString("jinml")+","+rsl.getString("panml")+","+rsl.getString("haoml")+","+
					rsl.getString("jinml")+"+"+rsl.getString("panml")+"-"+rsl.getString("haoml")+"+"+rsl_tmp.getString("qimkc")+")\n";
				con.getInsert(sql);
			}else{
//				������ֵ��������ֵ���������������
			}
		}else{
			sql = 
				"select jinml,panml,haoml,qimkc from hemshc\n" + 
				"  where diancxxb_id="+ getChangbValue().getId() +" and riq=to_date('"+ getRiq() +"','yyyy-mm-dd')-1 and leix='��ú'";
			rsl_tmp = con.getResultSetList(sql);
			if(rsl_tmp.next()){
				sql = //������ֵ��������ֵȡ�����տ��
					"insert into hemshc values(\n"+getChangbValue().getId() +
					",to_date('"+ getRiq() +"','yyyy-mm-dd')" +	",'��ú',0,0,0," + rsl_tmp.getString("qimkc") + " )\n";
				con.getInsert(sql);
			}else{
				sql = //������ֱֵ�ӳ�ʼ0
					"insert into hemshc values(\n"+getChangbValue().getId() +
					",to_date('"+ getRiq() +"','yyyy-mm-dd')" +	",'��ú',0,0,0,0 )\n";
				con.getInsert(sql);
			}
		}
		con.commit();
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql =
			"select h.riq,h.leix,h.jinml,h.panml,h.haoml,h.qimkc,decode(zh.qimkc,null,0,zh.qimkc) zuorkc from hemshc h,hemshc zh\n" +
			" where h.diancxxb_id="+getChangbValue().getStrId()+" and to_char(h.riq,'yyyy-mm-dd')='"+getRiq()+"' and h.leix='��ú'\n" + 
			"	and h.diancxxb_id=zh.diancxxb_id(+) and h.riq-1=zh.riq(+) and h.leix=zh.leix(+)";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("kuangfzlb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
		//���ö�ѡ��
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// /������ʾ������
		
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("leix").setEditor(null);
//		List ls = new ArrayList();
//		ls.add(new IDropDownBean(1, "��ú"));
//		ls.add(new IDropDownBean(2, "�߿�ú"));
//		ComboBox c7 = new ComboBox();
//		egu.getColumn("leix").setEditor(c7);
//		c7.setEditable(true);
//		egu.getColumn("leix").setComboEditor(egu.gridId,new IDropDownModel(ls));
//		egu.getColumn("leix").setDefaultValue("��ú");
		
		egu.getColumn("jinml").setHeader("��ú��");
		
		egu.getColumn("panml").setHeader("��ú��");
		
		egu.getColumn("haoml").setHeader("��ú��");
		
		egu.getColumn("qimkc").setHeader("���");
		
		egu.getColumn("zuorkc").setHeader("���տ��");
		egu.getColumn("zuorkc").setEditor(null);
		egu.getColumn("zuorkc").setHidden(true);

		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		egu.addTbarText("��λ:");
		ComboBox comb = new ComboBox();
		comb.setTransform("ChangbDropDown");
		comb.setId("Changb");
		comb.setEditable(false);
		comb.setLazyRender(true);// ��̬��
		comb.setWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");

//		����Toolbar��ť	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		egu.addTbarText("-");
		GridButton shengc = new GridButton("����",getBtnHandlerScript("CreateButton"));
		egu.addTbarBtn(shengc);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		sql = 
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='JINML'||e.field=='PANML'||e.field=='HAOML'){\n" + 
				"\n" + 
				"    var kc=0;\n" + 
				"    kc=eval(gridDiv_ds.getAt(0).get('JINML')||0)+eval(gridDiv_ds.getAt(0).get('PANML')||0)-eval(gridDiv_ds.getAt(0).get('HAOML')||0)+eval(gridDiv_ds.getAt(0).get('ZUORKC')||0);\n" + 
				"\n" + 
				"    gridDiv_ds.getAt(0).set('QIMKC',kc);\n" + 
				"  }\n" + 
				
				"});";
		egu.addOtherScript(sql);
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
			init();
		}
		getSelectData();
	}
	
	private void init() {
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setboolean1(false);		//����
		this.setChangbModel(null);		//IPropertySelectionModel1
		this.setChangbValue(null);		//IDropDownBean1
		this.getChangbModels();
	}
	
//	����
	public IDropDownBean getChangbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}
	public void setChangbValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}
	public IPropertySelectionModel getChangbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	public IPropertySelectionModel getChangbModels() {
		String sql ="select id,mingc from diancxxb d where d.fuid=300 order by mingc";
		((Visit) getPage().getVisit())
		.setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	����_End
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate ="";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����")
			.append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
}
