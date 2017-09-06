package com.zhiren.dc.rulgl.rulqtzb;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.huaybb.feihbgd.Feihbgd;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ����:tzf
 * ʱ��:2009-4-20
 * �޸�����:���� ��������  ��Ʒ��Դ   ���� �ֶ� 
 */
/*
 * ����:tzf
 * ʱ��:2009-4-16
 * ����:��¯����ָ���ά��
 */
public class Rulqtzb extends BasePage implements PageValidateListener {
	
	private final static String item_mingc="FH";    //�ɻҵı���
	private final static String itemsort_bianm="RLHYQTZB";   //��¯��������ָ��  ��itemsort�еı���
	private final static String fhbzitem_bianm="FHBZ";//�ɻҰ�ֵ��Ŀ��Ӧ�ı���
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		int a=this.Save1(this.getChange(),visit);
		if(a==-1){
//			this.setMsg("Ext.Msg.alert('��ʾ��Ϣ','�����ɹ�!');");
			this.setMsg(MainGlobal.getExtMessageBox("����ʧ��!",false ));
		}else{
//			this.setMsg("Ext.Msg.alert('��ʾ��Ϣ','����ʧ��!');");
			this.setMsg(MainGlobal.getExtMessageBox("�����ɹ�!",false ));
		}
	}
	
	
	private int Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		String tableName="rulqtzbb";
		
		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl =  this.getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(!mdrsl.getColumnNames()[i].toUpperCase().equals("MINGC")){
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
					
				}
				
				sql.append(" ,diancxxb_id,riq,item_id,leix"); //����ѡ��ĵ糧,����,��Ŀ ��Ϊ�洢�� ����
				sql2.append(" ,").append(this.getTreeid())
				.append(" ,")
				.append(DateUtil.FormatOracleDate(this.getRiqi()))
				.append(",")
				.append(this.getHuaybhValue().getStrId())
				.append(",")
				.append("0");
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(!mdrsl.getColumnNames()[i].toUpperCase().equals("MINGC")){
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
					
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;"); 
		int flag = con.getUpdate(sql.toString());
		con.Close();
		return flag;
	}
	
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
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
		}else if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		getSelectData();
	}
	
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
    private String getItem_mingc(String id){
    	JDBCcon con = new JDBCcon();
    	
    	String s="";
    	String sql=" select bianm from item where id="+id;
    	ResultSetList rsl = con
		.getResultSetList(sql);
    	
    	if(rsl.next()){
    		s=rsl.getString("bianm");
    	}
    	con.Close();
    	return s;
    }
	private boolean isFeiH(){//��Ŀ�Ƿ��Ƿɻ�
		boolean t=false;
		if(this.getItem_mingc(this.getHuaybhValue().getStrId()).equals(item_mingc)){
			t=true;
		}
		return t;
	}
	private StringBuffer getBaseSql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select r.id, it.mingc  \n");
		
		
		if(this.isFeiH()){  //�Ƿɻ�  ��ʾ��ֵ
			bf.append(",(select it.mingc from item it where it.id=r.banz) banz ");
		}
		bf.append(" ,r.zhi");
		bf.append(",r.caiyrq");
		bf.append(",r.yangply");
		bf.append(" ,j.jizbh jizb_id");
		bf.append(",r.beiz \n");
		bf.append(" from item it,itemsort im ,rulqtzbb r,jizb j  where r.item_id=it.id \n ");
		bf.append(" and it.itemsortid=im.itemsortid \n");
		bf.append(" and it.bianm='"+this.getItem_mingc(this.getHuaybhValue().getStrId())+"' \n");
		bf.append(" and im.bianm='"+itemsort_bianm+"' \n");
		bf.append(" and r.jizb_id=j.id(+) \n");

		
		if(!this.hasDianc(this.getTreeid())){
			bf.append(" and r.diancxxb_id=").append(this.getTreeid()).append(" \n");
		}
		
		bf.append(" and r.riq=").append(DateUtil.FormatOracleDate(this.getRiqi())).append(" \n");
		
		bf.append(" order by id");
		return bf;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
	
		ResultSetList rsl = con
				.getResultSetList(this.getBaseSql().toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		
		
		// //���ñ��������ڱ���
		egu.setTableName("rulqtzbb");
		// /������ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("��Ŀ��");
		egu.getColumn("mingc").setWidth(120);
		egu.getColumn("mingc").setEditor(null);
		
		if(this.isFeiH()){
			egu.getColumn("banz").setHeader("��ֵ");
			egu.getColumn("banz").setWidth(120);
			
			egu.getColumn("banz").setEditor(new ComboBox());
			egu.getColumn("banz").setComboEditor(egu.gridId, new IDropDownModel(" select it.id,it.mingc from item it  where it.itemsortid=(select im.id from itemsort im where im.bianm='"+fhbzitem_bianm+"' )"));
			egu.getColumn("banz").returnId=true;
			egu.getColumn("banz").editor.allowBlank=true;
		}
		
		egu.getColumn("zhi").setHeader("ֵ");
		egu.getColumn("zhi").setWidth(120);
		
		egu.getColumn("caiyrq").setHeader("��������");
		egu.getColumn("caiyrq").setDefaultValue(this.getRiqi());
		egu.getColumn("caiyrq").setWidth(120);
		
		egu.getColumn("yangply").setHeader("��Ʒ��Դ");
		egu.getColumn("yangply").setWidth(120);
		
		egu.getColumn("jizb_id").setHeader("����");
		egu.getColumn("jizb_id").setWidth(120);
		egu.getColumn("jizb_id").setEditor(new ComboBox());
		egu.getColumn("jizb_id").setComboEditor(egu.gridId, new IDropDownModel(" select j.id,j.jizbh  from jizb j"));
		egu.getColumn("jizb_id").returnId=true;
		egu.getColumn("jizb_id").editor.allowBlank=true;
		
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(120);
	
		
		
		// /����������Ĭ��ֵ
	//	egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("�糧:");
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
		
		egu.addTbarText("-");
		
		egu.addTbarText("��Ŀ��");

		ComboBox comb4 = new ComboBox();
		comb4.setTransform("HuaybhDropDown");
		comb4.setId("Huaybh");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());


		
		egu.addTbarText("-");
		// /���ð�ť
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		setExtGrid(egu);
		con.Close();
	}

	
	//��Ŀ���

	public IDropDownBean getHuaybhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getHuaybhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setHuaybhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setHuaybhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getHuaybhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getHuaybhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getHuaybhModels() {
		String sql = " select id,mingc from item where itemsortid in (select itemsortid from itemsort where bianm='"+itemsort_bianm+"' and zhuangt=1) order by id";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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
			

			getHuaybhValue();
			setHuaybhValue(null);
			getHuaybhModels();
			
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		
		getSelectData();
	}
	
	protected void initialize(){
		this.msg="";
	}
}
