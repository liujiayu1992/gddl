package com.zhiren.dc.hesgl.jiessz;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author zsj
 * 2009-09-22
 * ������1�����Ӷ�һ�����Ƶ�֧�֣������м������ɾ���Ĺ��ܡ�
 * 		�������ɾ���߼����£�������ϵͳ����һ�����Ƶ����ʱ��
 * 			1����ʹ���ܳ��û���ݵ�¼ʱ��
 * 				(1)����Ӳ�����Ҫ��������ӳ��Ķ�Ӧ��¼
 * 				(2)���޸Ĳ�����Ҫ���������ӳ��Ķ�Ӧ��¼����id����ע��diancxxb_id��Ҫȫ��ƥ�䣩
 * 				(3)��ɾ��������Ҫ����ɾ���ӳ��Ķ�Ӧ��¼����id����ע��diancxxb_id��Ҫȫ��ƥ�䣩
 * 			2����ʹ�÷ֳ��û���ݵ�¼ʱ��
 * 				�����м�������ɾ�Ĳ顱
 * */

public class Jiesszfab extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	private long jsid;
	public long getJsid() {
		return jsid;
	}

	public void setJsid(long jsid) {
		this.jsid = jsid;
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
		Save1(getChange(), visit);
	}
	
	private void Save1(String Change,Visit visit){
//		2009-09-22 zsj�ģ�
		JDBCcon con = new JDBCcon();
		ResultSetList drsl = getExtGrid().getDeleteResultSet(Change);
		String TableName="jiesszfab";
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql_Insert = new StringBuffer("");	//insert
		StringBuffer sql_Update = new StringBuffer("");	//Update
		while (drsl.next()) {
			//ɾ��
			sql.append("delete from " ).append(TableName).append(" where id=")
				.append(drsl.getString("ID")).append(";\n");
			
//			����������ɾ��
//			sql.append(Zicjlcz("D",getTreeid(),TableName,drsl.getString("ID")));
		}
		drsl.close();
//		��ӡ��޸�
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(Change);
		while (mdrsl.next()) {
			
			if ("0".equals(mdrsl.getString("ID"))) {
				
				sql_Insert.append("insert into ").append(TableName)
					.append("(id, gongysb_id, diancxxb_id, miaos, qiysj, shezry, shifsy")
				    .append(") values(").append("getnewid(").append(getTreeid()).append(")")
					.append(",").append(getGongysValue().getId()).append(",")
					.append(getTreeid()).append(",'").append(mdrsl.getString("MIAOS")).append("',")
					.append("to_date('").append(mdrsl.getString("QIYSJ")).append("','yyyy-MM-dd')").append(",'")
					.append(mdrsl.getString("SHEZRY")).append("',")
					.append(getExtGrid().getValueSql(getExtGrid().getColumn("SHIFSY"),mdrsl.getString("SHIFSY")))
					.append(");\n");
				
//				��������������
//				sql_Insert.append(Zicjlcz("I",getTreeid(),TableName,sql_Insert.toString()));
				
			} else {
				
				sql_Update.append(" update jiesszfab ").append("set").append(" miaos ='")
						.append(mdrsl.getString("MIAOS")).append("',")
						.append(" qiysj = to_date('").append(mdrsl.getString("QIYSJ")).append("','yyyy-MM-dd'),")
						.append(" SHEZRY = '").append(mdrsl.getString("SHEZRY")).append("',")
						.append(" shifsy = ").append(getExtGrid().getValueSql(getExtGrid().getColumn("SHIFSY"),mdrsl.getString("SHIFSY")))
						.append(" where id=").append(mdrsl.getString("ID")).append(";\n");
				
				StringBuffer sql2 = new StringBuffer("");
					sql2.append(" update jiesszfab ").append("set").append(" miaos ='")
						.append(mdrsl.getString("MIAOS")).append("',")
						.append(" qiysj = to_date('").append(mdrsl.getString("QIYSJ")).append("','yyyy-MM-dd'),")
						.append(" SHEZRY = '").append(mdrsl.getString("SHEZRY")).append("',")
						.append(" shifsy = ").append(getExtGrid().getValueSql(getExtGrid().getColumn("SHIFSY"),mdrsl.getString("SHIFSY")))
						.append(" where diancxxb_id=").append(getTreeid()).append(" and ")
						.append("gongysb_id=").append(getGongysValue().getId()).append(";\n");
				
//				�����������޸�
//					sql_Update.append(Zicjlcz("U",getTreeid(),TableName,sql2.toString()));
			}
		}
		mdrsl.close();
		sql.append(sql_Insert);
		sql.append(sql_Update);
		sql.append("end;");
		if(sql.length()>13){
			
			if(con.getUpdate(sql.toString())>=0){
				
				setMsg("����ɹ���");
			}else{
				
				setMsg("����ʧ�ܣ�");
			}
		}
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
    
	private boolean _ShezfaChick = false;

	public void ShezfaButton(IRequestCycle cycle) {
		_ShezfaChick = true;
	}
	
	private boolean _HetglChick = false;

	public void HetglButton(IRequestCycle cycle) {
		_HetglChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShezfaChick) {
			_ShezfaChick = false;
			GotoShezfa(cycle);	
		}
		if(_HetglChick) {
			_HetglChick=false;
			GotoHetgl(cycle);
		}
	}
	
	public StringBuffer Zicjlcz(String Type,String Diancxxb_id,String TableName,String IdOrSql){
//		�������ܣ�
//			������ϵͳ��һ������ʱ�ܳ����¼��糧�ļ�����ɾ������
//		�����߼���
//			1���ж��Ƿ�Ϊ����ϵͳ��һ�����������
//				1)��ɾ������id�͡���ע���ֶ��⣬Ҫȫ�ֶ�ƥ��	
//		�����βΣ�
//			Type���ڱ�ʶ������ɾ���ģ�I,D,U����Diancxxb_id���ڱ�ʶ��ǰ�����ĵ糧;
//			TableName������Id��id(or Insert_sql) ��ɾ�������ʱ��id���ڲ����ʱ��Ҫ��Insert_sql
		
		StringBuffer sb = new StringBuffer("");
		Visit visit = (Visit) getPage().getVisit();
		if(visit.isFencb()){
//			�Ƿֳ���
			String sql = "";
			JDBCcon con = new JDBCcon();
			sql = "select id from diancxxb where fuid="+Diancxxb_id;
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.getRows()>0){
//				����õ糧���ܳ�����Ҫ��������
				StringBuffer sb2 = new StringBuffer("");
				while(rsl.next()){
					
					if(Type.equals("D")){
//						ɾ���߼�
						sql="select * from "+TableName+" where id="+IdOrSql;
						ResultSetList rec = con.getResultSetList(sql);
						if(rec.next()){
							sb2.setLength(0);
							for(int i=0;i<rec.getColumnCount();i++){
								
								if(!rec.getColumnNames()[i].equals("ID")
										||!rec.getColumnNames()[i].equals("DIANCXXB_ID")
										||!rec.getColumnNames()[i].equals("BEIZ")
								){
									
									if(sb2.length()==0){
										
										sb2.append(rec.getColumnNames()[i]).append(" = ").append(rec.getString(rec.getColumnNames()[i]));
									}else{
										
										sb2.append(" and ").append(rec.getColumnNames()[i]).append(" = ").append(rec.getString(rec.getColumnNames()[i]));
									}
								}
							}
							sb.append("delete ").append(TableName).append(" where ").append(sb2).append(";\n");
						}
						rec.close();
						break;
					}else if(Type.equals("I")){
//						���	�߼�(����ʱע�⣺IdOrSql�е�diancxxb_id�Ƿ��ı�)
//								�滻�ֶΣ�getnewid(Diancxxb_id)��,Diancxxb_id,
						String strtmp=IdOrSql.replaceAll("getnewid\\("+Diancxxb_id+"\\)", "getnewid("+rsl.getString("id")+")");
							   strtmp=strtmp.replaceAll(","+Diancxxb_id+",", ","+rsl.getString("id")+",");
						sb.append(strtmp);
					}else if(Type.equals("U")){
						
//						�޸� �߼�
//								�滻�ֶΣ�diancxxb_id=Diancxxb_id
						sb.append(IdOrSql.replaceAll("diancxxb_id="+Diancxxb_id, "diancxxb_id="+rsl.getString("id")));
					}
				}
				sb2=null;
			}
			rsl.close();
			con.Close();
		}
		
		return sb;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		//this.setJsid(0);
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select j.id,\n" +
                      "  g.mingc as mingc,\n" + 
                      "  j.gongysb_id as gongysb_id,\n"+
                      "  j.diancxxb_id,\n" + 
                      "  j.qiysj,\n" + 
                      "  j.shezry,\n" + 
                      "  decode(j.shifsy, 1, '��', '��') as shifsy,\n" +
                      "  j.miaos \n"+
                      "  from jiesszfab j,gongysb g\n" + 
                      " where j.diancxxb_id ="+this.getTreeid()+"\n" + 
                      "  and j.gongysb_id=g.id\n"+
                      "  and j.gongysb_id ="+getGongysValue().getId()+"");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("jiesszfab");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("mingc").setDefaultValue(getGongysValue().getValue());
		egu.getColumn("mingc").setReturnId(true); 
		egu.getColumn("mingc").setUpdate(false);
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("��Ӧ��ID");
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("gongysb_id").setDefaultValue(""+getGongysValue().getId());
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setDefaultValue(this.getTreeid());
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("qiysj").setHeader("����ʱ��");
		egu.getColumn("qiysj").setEditor(new DateField());
		egu.getColumn("qiysj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("shezry").setHeader("������Ա");
		egu.getColumn("shezry").setDefaultValue(visit.getRenymc());
		egu.getColumn("shifsy").setHeader("�Ƿ�ʹ��");
		egu.getColumn("miaos").setHeader("����");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		List l = new ArrayList();
		l.add(new IDropDownBean(0, "��"));
		l.add(new IDropDownBean(1, "��"));
		egu.getColumn("shifsy").setEditor(new ComboBox());
		egu.getColumn("shifsy").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("shifsy").setReturnId(true);
		egu.getColumn("shifsy").setDefaultValue(String.valueOf(((IDropDownBean)l.get(1)).getValue()));
		
		// ���������糧����
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		//������λ
		egu.addTbarText("-");
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(true);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Gongys.getRawValue()=='��ѡ��'){alert('��ѡ��Ӧ��'); return;}");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton("����",GridButton.ButtonType_Save, "SaveButton");
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
	        +"  var recmx=gridDiv_Mx_sm.getSelected(); \n"
	        +"  if(recmx!=null){\n"
	        +"  gridDiv_Mx_history = recmx.get('JIESSZFAB_ID')+','+recmx.get('JIESSZBMB_ID');\n"
	        +"  document.getElementById('PARAMETERS').value=gridDiv_Mx_history; \n"
	        +"  }else if(rec!=null){\n"
	        + " if(rec.get('ID') == 0){ \n"
		    + " 	Ext.MessageBox.alert('��ʾ��Ϣ','������֮ǰ���ȱ���!'); \n"
		    + "  	return;"
	        +"}"
	        +"  gridDiv_history = rec.get('ID')+','+rec.get('MINGC');	\n"
	        +"  document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"  }else{\n"
	        +"  Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ŀ!'); \n"
	        +"  return;"
	        +"  }"
	        +" document.getElementById('ShezfaButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("����","function(){"+str2+"}").getScript()+"}");
    	String str1=
			" if(gridDiv_sm.getSelections().length <= 0 " 
		    + "|| gridDiv_sm.getSelections().length > 1){ \n"
		    + " 	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ŀ!'); \n"
		    + " 	return;"
	        +"}"
	        + " var rec = gridDiv_sm.getSelected(); \n"
	        + " if(rec.get('ID') == 0){ \n"
		    + " 	Ext.MessageBox.alert('��ʾ��Ϣ','������֮ǰ���ȱ���!'); \n"
		    + "  	return;"
	        +"}"
	        +" gridDiv_history = rec.get('ID')+','+rec.get('GONGYSB_ID');	\n"
	        +" document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +" document.getElementById('HetglButton').click(); \n";
    	egu.addToolbarItem("{"+new GridButton("��ͬ����","function(){"+str1+"}").getScript()+"}");
        String str3=
        	"if(gridDiv_sm.getSelections().length <= 0"
        	+"|| gridDiv_sm.getSelections().length > 1){\n"
        	+"Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ŀ!');\n"
            +"return;}\n"
        	+"	var rec = gridDiv_sm.getSelected();\n"
        	+"	document.getElementById('JSID').value=rec.get('ID');\n"
        	//+"	document.getElementById('gongys_id').value=rec.get('gongysb_id');\n"
            +" document.forms[0].submit();";

        StringBuffer sb = new StringBuffer();
        sb.append("gridDiv_grid.on('rowdblclick',function(own,row,e){"+str3+"});");
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
		con.Close();
		long jsid=this.getJsid();
		setJsid(0);
		getSelectData_Mx(jsid);
	}
	
	public void getSelectData_Mx(long jsid) {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select j.id, j.jiesszfab_id, bm.mingc as jiesszbmb_id, decode(zb.bianm,null,j.zhi,zb.bianm) as zhi\n" +
                               "  from jiesszb j, jiesszbmb bm, jiesszfab fh, jiesszbmglb gl, jiesszbmzb zb\n" + 
                               " where j.jiesszfab_id =" +jsid+"\n" + 
                               "   and j.jiesszfab_id = fh.id\n" + 
                               "   and j.jiesszbmb_id = bm.id\n" + 
                               "   and j.jiesszbmb_id = gl.jiesszbmb_id\n" + 
                               "   and gl.jiesszbmzb_id = zb.id\n" + 
                               "   and j.zhi = zb.zhi(+)" +
                               "   union\n" +
                               "   select j.id, j.jiesszfab_id, bm.mingc as jiesszbmb_id, j.zhi as zhi\n" + 
                               "   from jiesszb j, jiesszbmb bm, jiesszfab fh, jiesszbmglb gl,jiesszbmzb zb\n" + 
                               "  where j.jiesszfab_id =" +jsid+"\n" + 
                               "   and j.jiesszfab_id = fh.id\n" + 
                               "   and j.jiesszbmb_id = bm.id\n" + 
                               "   and j.jiesszbmb_id = gl.jiesszbmb_id\n" + 
                               "   and gl.jiesszbmzb_id = zb.id\n" + 
                               "   and zb.zhi='�ı���'");
		ExtGridUtil egu = new ExtGridUtil("gridDiv_Mx", rsl);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("jiesszfab_id").setHeader("����");
		egu.getColumn("jiesszfab_id").setEditor(null);
		egu.getColumn("jiesszfab_id").setHidden(true);
		egu.getColumn("jiesszbmb_id").setHeader("����");
		egu.getColumn("jiesszbmb_id").setEditor(null);
		egu.getColumn("jiesszbmb_id").setWidth(150);
		egu.getColumn("zhi").setHeader("ֵ");
		egu.getColumn("zhi").setEditor(null);
		egu.getColumn("zhi").setWidth(150);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		setExtGrid_Mx(egu);
		con.Close();
	}
	
	
//	������λ


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
			"where glb.diancxxb_id=dc.id and gys.zhuangt=1 and glb.gongysb_id=gys.id and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" and gys.leix=1 order by gys.mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
	private void GotoShezfa(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		 ((Visit) getPage().getVisit()).setString10(this.getParameters());
		 ((Visit) getPage().getVisit()).setString11(this.getTreeid());
     	cycle.activate("Jiesszzb");
	}

	private void GotoHetgl(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		 ((Visit) getPage().getVisit()).setString10(this.getParameters());
     	cycle.activate("Hetglsz");
	}
	
	private String Parameters;//��¼��ĿID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public ExtGridUtil getExtGrid_Mx() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid_Mx(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript_Mx() {
		return getExtGrid_Mx().getGridScript();
	}

	public String getGridHtml_Mx() {
		return getExtGrid_Mx().getHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
//			visit.setList1(null);
            setGongysModel(null);
			setGongysValue(null);
			getGongysModels();
			visit.setString10("");	//ҳ�洫�ݲ���(��¼��ĿID)
			visit.setString11("");	//��¼�糧��Ϣ��id
			this.setJsid(0);
			getSelectData();
			setExtGrid(null);
			setTree(null);
		}
		getSelectData();
//		getSelectData_Mx();
	}
}
