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
 * 描述：1，增加对一厂多制的支持，并带有级联添加删除的功能。
 * 		级联添加删除逻辑如下：当厂级系统出现一厂多制的情况时，
 * 			1、当使用总厂用户身份登录时：
 * 				(1)、添加操作：要级联添加子厂的对应记录
 * 				(2)、修改操作：要级联更新子厂的对应记录（处id、备注、diancxxb_id外要全字匹配）
 * 				(3)、删除操作：要级联删除子厂的对应记录（处id、备注、diancxxb_id外要全字匹配）
 * 			2、当使用分厂用户身份登录时：
 * 				不进行级联“增删改查”
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

	
	// 页面变化记录
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
//		2009-09-22 zsj改，
		JDBCcon con = new JDBCcon();
		ResultSetList drsl = getExtGrid().getDeleteResultSet(Change);
		String TableName="jiesszfab";
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql_Insert = new StringBuffer("");	//insert
		StringBuffer sql_Update = new StringBuffer("");	//Update
		while (drsl.next()) {
			//删除
			sql.append("delete from " ).append(TableName).append(" where id=")
				.append(drsl.getString("ID")).append(";\n");
			
//			级联操作，删除
//			sql.append(Zicjlcz("D",getTreeid(),TableName,drsl.getString("ID")));
		}
		drsl.close();
//		添加、修改
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
				
//				级联操作，插入
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
				
//				级联操作，修改
//					sql_Update.append(Zicjlcz("U",getTreeid(),TableName,sql2.toString()));
			}
		}
		mdrsl.close();
		sql.append(sql_Insert);
		sql.append(sql_Update);
		sql.append("end;");
		if(sql.length()>13){
			
			if(con.getUpdate(sql.toString())>=0){
				
				setMsg("保存成功！");
			}else{
				
				setMsg("保存失败！");
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
//		函数功能：
//			处理厂级系统中一厂多制时总厂对下级电厂的级联增删改问题
//		函数逻辑：
//			1、判断是否为厂级系统的一厂多制情况，
//				1)、删除：除id和“备注”字段外，要全字段匹配	
//		函数形参：
//			Type用于标识是增、删、改（I,D,U）；Diancxxb_id用于标识当前操作的电厂;
//			TableName表名；Id表id(or Insert_sql) 在删除或更新时用id，在插入的时候要用Insert_sql
		
		StringBuffer sb = new StringBuffer("");
		Visit visit = (Visit) getPage().getVisit();
		if(visit.isFencb()){
//			是分厂别
			String sql = "";
			JDBCcon con = new JDBCcon();
			sql = "select id from diancxxb where fuid="+Diancxxb_id;
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.getRows()>0){
//				如果该电厂是总厂，需要级联操作
				StringBuffer sb2 = new StringBuffer("");
				while(rsl.next()){
					
					if(Type.equals("D")){
//						删除逻辑
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
//						添加	逻辑(调试时注意：IdOrSql中的diancxxb_id是否会改变)
//								替换字段：getnewid(Diancxxb_id)，,Diancxxb_id,
						String strtmp=IdOrSql.replaceAll("getnewid\\("+Diancxxb_id+"\\)", "getnewid("+rsl.getString("id")+")");
							   strtmp=strtmp.replaceAll(","+Diancxxb_id+",", ","+rsl.getString("id")+",");
						sb.append(strtmp);
					}else if(Type.equals("U")){
						
//						修改 逻辑
//								替换字段：diancxxb_id=Diancxxb_id
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
                      "  decode(j.shifsy, 1, '是', '否') as shifsy,\n" +
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
		egu.getColumn("gongysb_id").setHeader("供应商ID");
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("gongysb_id").setDefaultValue(""+getGongysValue().getId());
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setDefaultValue(this.getTreeid());
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("qiysj").setHeader("启用时间");
		egu.getColumn("qiysj").setEditor(new DateField());
		egu.getColumn("qiysj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("shezry").setHeader("设置人员");
		egu.getColumn("shezry").setDefaultValue(visit.getRenymc());
		egu.getColumn("shifsy").setHeader("是否使用");
		egu.getColumn("miaos").setHeader("描述");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		List l = new ArrayList();
		l.add(new IDropDownBean(0, "否"));
		l.add(new IDropDownBean(1, "是"));
		egu.getColumn("shifsy").setEditor(new ComboBox());
		egu.getColumn("shifsy").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("shifsy").setReturnId(true);
		egu.getColumn("shifsy").setDefaultValue(String.valueOf(((IDropDownBean)l.get(1)).getValue()));
		
		// 设置树（电厂数）
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		//供货单位
		egu.addTbarText("-");
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(true);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Gongys.getRawValue()=='请选择'){alert('请选择供应商'); return;}");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton("保存",GridButton.ButtonType_Save, "SaveButton");
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
	        +"  var recmx=gridDiv_Mx_sm.getSelected(); \n"
	        +"  if(recmx!=null){\n"
	        +"  gridDiv_Mx_history = recmx.get('JIESSZFAB_ID')+','+recmx.get('JIESSZBMB_ID');\n"
	        +"  document.getElementById('PARAMETERS').value=gridDiv_Mx_history; \n"
	        +"  }else if(rec!=null){\n"
	        + " if(rec.get('ID') == 0){ \n"
		    + " 	Ext.MessageBox.alert('提示信息','在设置之前请先保存!'); \n"
		    + "  	return;"
	        +"}"
	        +"  gridDiv_history = rec.get('ID')+','+rec.get('MINGC');	\n"
	        +"  document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"  }else{\n"
	        +"  Ext.MessageBox.alert('提示信息','请选中一个项目!'); \n"
	        +"  return;"
	        +"  }"
	        +" document.getElementById('ShezfaButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("设置","function(){"+str2+"}").getScript()+"}");
    	String str1=
			" if(gridDiv_sm.getSelections().length <= 0 " 
		    + "|| gridDiv_sm.getSelections().length > 1){ \n"
		    + " 	Ext.MessageBox.alert('提示信息','请选中一个项目!'); \n"
		    + " 	return;"
	        +"}"
	        + " var rec = gridDiv_sm.getSelected(); \n"
	        + " if(rec.get('ID') == 0){ \n"
		    + " 	Ext.MessageBox.alert('提示信息','在设置之前请先保存!'); \n"
		    + "  	return;"
	        +"}"
	        +" gridDiv_history = rec.get('ID')+','+rec.get('GONGYSB_ID');	\n"
	        +" document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +" document.getElementById('HetglButton').click(); \n";
    	egu.addToolbarItem("{"+new GridButton("合同关联","function(){"+str1+"}").getScript()+"}");
        String str3=
        	"if(gridDiv_sm.getSelections().length <= 0"
        	+"|| gridDiv_sm.getSelections().length > 1){\n"
        	+"Ext.MessageBox.alert('提示信息','请选中一个项目!');\n"
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
                               "   and zb.zhi='文本框'");
		ExtGridUtil egu = new ExtGridUtil("gridDiv_Mx", rsl);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("jiesszfab_id").setHeader("方案");
		egu.getColumn("jiesszfab_id").setEditor(null);
		egu.getColumn("jiesszfab_id").setHidden(true);
		egu.getColumn("jiesszbmb_id").setHeader("编码");
		egu.getColumn("jiesszbmb_id").setEditor(null);
		egu.getColumn("jiesszbmb_id").setWidth(150);
		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("zhi").setEditor(null);
		egu.getColumn("zhi").setWidth(150);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		setExtGrid_Mx(egu);
		con.Close();
	}
	
	
//	供货单位


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
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
	private void GotoShezfa(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		 ((Visit) getPage().getVisit()).setString10(this.getParameters());
		 ((Visit) getPage().getVisit()).setString11(this.getTreeid());
     	cycle.activate("Jiesszzb");
	}

	private void GotoHetgl(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		 ((Visit) getPage().getVisit()).setString10(this.getParameters());
     	cycle.activate("Hetglsz");
	}
	
	private String Parameters;//记录项目ID

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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			visit.setList1(null);
            setGongysModel(null);
			setGongysValue(null);
			getGongysModels();
			visit.setString10("");	//页面传递参数(记录项目ID)
			visit.setString11("");	//记录电厂信息表id
			this.setJsid(0);
			getSelectData();
			setExtGrid(null);
			setTree(null);
		}
		getSelectData();
//		getSelectData_Mx();
	}
}
