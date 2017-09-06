package com.zhiren.dc.hesgl.yufkb;
//Ԥ������
import java.sql.PreparedStatement;
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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yufkb extends BasePage implements PageValidateListener {
	private String msg = "";
	private boolean returnId = false;
	private IPropertySelectionModel saveModel=null;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg =  MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	//ҳ�����Ƿ��Ѵ���
	private boolean getChangeBh(String changebh,StringBuffer sql){
		boolean forchange=false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		
		sql.append("and bianh='"+changebh+"'");
		ps=con.getPresultSet(sql.toString());
		
		try {
			rs = ps.executeQuery();			
			while(rs.next()){
				if(rs!=null){
					forchange=true;			
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        
		return forchange;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		String tableName="yufkb";
		returnId=true;
		
		JDBCcon con = new JDBCcon();
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);

		while(mdrsl.next()) {
		
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();//ҳ����û�е��ֶ�
			StringBuffer sql4 = new StringBuffer();//ҳ����û�е��ֶε�����
			StringBuffer sql5 = new StringBuffer();
			StringBuffer sql6 = new StringBuffer();		
			
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if("0".equals(mdrsl.getString("ID"))) {
				
				StringBuffer bsql=new StringBuffer();
				bsql.append("select * from  yufkb where 1=1 ");
				if(getChangeBh(mdrsl.getString("BIANH"),bsql)){
					this.setMsg("���"+mdrsl.getString("BIANH")+"�Ѵ���!");
					return;
				}
				
				sql.append("insert into ").append(tableName).append("(id");
				sql3.append(",diancxxb_id");//�ɱ�
				sql4.append(",").append(getTreeid());
				sql5.append(",yue");//�ɱ�
				sql6.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[5]),mdrsl.getString(5)));				
				for(int i=1;i<mdrsl.getColumnCount();i++) {
			
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
						sql2.append(",").append(getGongysValue().getId());
					}else{
					sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));}

				}
				
				sql.append(sql3).append(sql5).append(") values(").append(sql2).append(sql4).append(sql6).append(");\n");
			}else {
				
				StringBuffer bsql=new StringBuffer();
				bsql.append("select * from  yufkb where 1=1 ");
				bsql.append("and id<>'"+mdrsl.getString("ID")+"' ");
				if(getChangeBh(mdrsl.getString("BIANH"),bsql)){
					this.setMsg("���"+mdrsl.getString("BIANH")+"�Ѵ���!");
					return;
				}
				
				sql.append("update ").append(visit.getExtGrid1().tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
						
						sql.append(getGongysValue().getId()).append(",");
					}else{
					sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
	
	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
	}
		
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}
		if (_shuaxin){
			_shuaxin=false;
//			getSelectData();
		}
	}

	

	public String getGongysxx() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setGongysxx(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select y.id,y.riq,y.bianh,g.mingc as gongysb_id ,s.mingc as shoukdwb_id,y.jine,y.kaihyh,y.zhangh,decode(leib,1,'ú��',2,'�˷�',3,'�ӷ�',4,'����')as leib,y.fapbh as fapbh,y.jingbr as jingbr,y.beiz from yufkb y , gongysb g, shoukdw s where y.riq>=to_date('"+getRiqi()+
		        "','yyyy-mm-dd')and y.riq<=to_date('"+getRiq2()+"','yyyy-mm-dd')and g.id=y.gongysb_id and s.id=y.shoukdwb_id and y.gongysb_id="+getGongysValue().getId() + " and y.diancxxb_id = " + getTreeid());

		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yufkb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("riq").setHeader("����");		
		egu.getColumn("bianh").setHeader("���");
		egu.getColumn("bianh").setWidth(60);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("shoukdwb_id").setHeader("�տλ");
		egu.getColumn("jine").setHeader("���");
		egu.getColumn("jine").setWidth(80);
		egu.getColumn("kaihyh").setHeader("��������");	
		egu.getColumn("kaihyh").setWidth(200);
		egu.getColumn("zhangh").setHeader("�ʺ�");
		egu.getColumn("zhangh").setWidth(130);
		egu.getColumn("leib").setHeader("���");
		egu.getColumn("fapbh").setHeader("��Ʊ���");
		egu.getColumn("jingbr").setHeader("������");
		egu.getColumn("beiz").setHeader("��ע");
		
//		egu.getColumn("gongyb_id").setEditor(new ComboBox());
//		egu.getColumn("gongyb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from gongysb"));
//		egu.getColumn("gongyb_id").setReturnId(true); 
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setDefaultValue(getGongysValue().getValue());
		egu.getColumn("gongysb_id").setReturnId(true); 
		
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "��ѡ��"));
		l.add(new IDropDownBean(1, "ú��"));
		l.add(new IDropDownBean(2, "�˷�"));
		l.add(new IDropDownBean(3, "�ӷ�"));
		l.add(new IDropDownBean(4, "����"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("leib").setDefaultValue("��ѡ��");
		egu.getColumn("leib").setReturnId(true);
		egu.getColumn("leib").setWidth(70);

		egu.getColumn("shoukdwb_id").setEditor(new ComboBox());
		egu.getColumn("shoukdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from shoukdw"));
		egu.getColumn("shoukdwb_id").setDefaultValue("��ѡ��");
		egu.getColumn("shoukdwb_id").setReturnId(true);

		egu.getColumn("jingbr").setEditor(null);
		egu.getColumn("jingbr").setDefaultValue(((Visit) getPage().getVisit()).getRenymc());

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
//		egu.setWidth(1000);
		egu.setWidth("bodyWidth");
	
		
		egu.addTbarText("��ʼ����:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��ֹ����:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		String kaihyh="";
		String zhangh="";
		String gongys="";
		//���ݹ�Ӧ��ˢ�¿������к��˻���Ϣ
		if(getGongysValue().getId()>-1){
				ResultSetList rs = con.getResultSetList("select kaihyh,zhangh from gongysb where id="+getGongysValue().getId());
                while(rs.next()){
                   setGongysxx(rs.getString("kaihyh")+","+rs.getString("zhangh"));
                }
                
                kaihyh=getGongysxx().substring(0,this.getGongysxx().lastIndexOf(','));
                zhangh=getGongysxx().substring(getGongysxx().lastIndexOf(',')+1); 
                gongys=getGongysValue().getValue();
		}
		

		// ������λ
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		// �糧��
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		StringBuffer sb=new StringBuffer();
		
		sb.append("{text:' ���',icon:'imgs/btnicon/insert.gif',cls:'x-btn-text-icon',minWidth:75,handler:function() {	\n")
			.append("if(Gongys.getRawValue()=='��ѡ��'){		\n")
			.append("alert('��ѡ��Ӧ��');	\n")
			.append("return;}	\n")
			
			.append("var plant = new gridDiv_plant({ID: '0',RIQ: '',BIANH: '',GONGYSB_ID: '"+gongys+"',SHOUKDWB_ID: '��ѡ��',JINE: '',KAIHYH: '"+kaihyh+"',ZHANGH: '"+zhangh+"',LEIB: '��ѡ��',FAPBH: '',JINGBR: '"+((Visit) getPage().getVisit()).getRenymc()+"',BEIZ: ''});	\n")
			.append("gridDiv_ds.insert(gridDiv_ds.getCount(),plant);	\n}}");
		
		egu.addTbarText("-");
		egu.addToolbarItem("{text:' ˢ��',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");

			
		egu.addToolbarItem(sb.toString());
//		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Gongys.getRawValue()=='��ѡ��'){alert('��ѡ��Ӧ��'); return;}");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
	    egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton","var Mr = gridDiv_ds.getModifiedRecords();  \n" +
                                                                                "for(i = 0; i< Mr.length; i++){  \n"+
                                                                                "	for(j=i+1; j< Mr.length; j++){  \n"+
	                                                                            "		if(Mr[i].get('BIANH')==Mr[j].get('BIANH')){  \n"+
        	                                                                    "       	Ext.MessageBox.alert('��ʾ��Ϣ','����ظ�!');  \n"+
        	                                                                    "         	return;"+			
  	                                                                            "       }  \n"+        
                                                                                "   }  \n"+  
                                                                                "}");
		setExtGrid(egu);
		
		con.Close();
	}
	
	
//	����
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();

			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "��ѡ��"));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
//������λ

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String  sql ="select gys.id,gys.mingc from gongysdcglb glb,diancxxb dc,gongysb gys\n" +
			"where glb.diancxxb_id=dc.id and gys.leix=1 and gys.zhuangt=1 and glb.gongysb_id=gys.id and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by gys.mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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
			visit.setList1(null);
			visit.isFencb();
			((Visit) getPage().getVisit()).setString1("");
			getFencbValue();
			setFencbValue(null);			
			getFencbModels();
			getGongysValue();
			setGongysValue(null);
			getGongysModels();
			
//			getSelectData();
		}
		getSelectData();
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

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
}