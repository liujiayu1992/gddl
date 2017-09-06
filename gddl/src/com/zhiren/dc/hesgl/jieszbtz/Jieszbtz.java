package com.zhiren.dc.hesgl.jieszbtz;

import java.io.IOException;
import java.io.StringReader;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jieszbtz extends BasePage implements PageValidateListener{
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	private String Change;

	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _RoolbackChick = false;
    public void RoolbackButton(IRequestCycle cycle) {
        _RoolbackChick = true;
    }
    private void Roolback() {
            
            
    }
    private boolean _SubmitChick = false;
    public void SubmitButton(IRequestCycle cycle) {
        _SubmitChick = true;
    }
    private void Submit() {
            
            
    }
    
	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
    
    private void Save() {
    	
    	
    	if(getChange()==null || "".equals(getChange())) {
//			提示信息
			setMsg("修改为空!");
			return ;
		}
    	Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	
	}
    public void Save1(String strchange,Visit visit){
    	String tableName="jieszbsjb";
    	String strSql;
    	String stt;
    	long ID=0;
    	JDBCcon con = new JDBCcon();
    	try{
    	String id ="select id from yansbhb where bianm='"+visit.getString3()+"'";
    	ResultSet rs=con.getResultSet(id);
    	if(!rs.next()){
    		ID=Long.parseLong(getNewID(visit.getDiancxxb_id()));
    		strSql="insert into yansbhb(id,bianm) values("+ID+",'"+visit.getString3()+"')";
        	con.getInsert(strSql);
    	}
    	else{
    		ID=rs.getLong("id");
    	}
    	
    	rs.close();
  
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		
		while(mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			//if("0".equals(mdrsl.getString("ID"))) {q
			sql.append("insert into ").append(tableName).append("(id");
				for(int i=0;i<mdrsl.getColumnCount();i++) {
					
					if(mdrsl.getColumnNames()[i].equals("ZHIBB_ID")){
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(getProperId(getIZhibModel(),getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)).replace('\'',' ').trim()));
					}else{
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
				}
					
				
				
			    
//				sql.append("update ").append(tableName).append(" set ");
//				for(int i=1;i<mdrsl.getColumnCount();i++) {
//					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
//					sql.append(getValueSql(getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
				
//				sql.deleteCharAt(sql.length()-1);
//				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
		}
				sql.append(",yansbhb_id,jiesdid,hetbz,yingk,zhejbz,zhejje,zhuangt) values(").append(sql2).append(","+ID+",0,0,0,0,0,0);\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
    	}catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
    }
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			//getSelectData();
		}
		if(_SubmitChick){
			_SubmitChick= false;
			
		}
		if(_RoolbackChick){
			_RoolbackChick=false;
		}
	}
	public void getSelectData(){
		JDBCcon con = new JDBCcon();
		String chaxun;
		chaxun ="select '结算数量(吨)' as zhibb_id,"
                 +" sum(f.jingz) as changf,"
                 +" sum(f.biaoz) as gongf,"
                 +" sum(f.jingz) as jies"
                 +" from fahb f,zhilb z,kuangfzlb k"
                 +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '收到基低位热值net,ar(MJ/Kg)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.qnet_ar,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '干燥基高位热值gr,d(MJ/Kg)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.qgrad)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.qgrad,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.qgrad)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '干燥基硫分St,d(%)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.std)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.std,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.std)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '一般分析煤样全硫St,ad(%)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.stad)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.stad,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.stad)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '全水分Mt(%)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.mt)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.mt,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.mt)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '干燥无灰基挥发分Vdaf(%)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.vdaf)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.vdaf,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.vdaf)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '收到基灰分Aar(%)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.aar)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.aar,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.aar)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0) \n"
		         +" union \n"
		         +" select '干燥基灰Ad(%)' as zhibb_id,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.ad)/sum(f.jingz),2)) as changf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*nvl(k.ad,0))/sum(f.jingz),2)) as gongf,"
		         +" decode(sum(f.jingz),0,0,round(sum(f.jingz*z.ad)/sum(f.jingz),2)) as jies"
		         +" from fahb f,zhilb z,kuangfzlb k"
		         +" where f.zhilb_id=z.id and f.kuangfzlb_id=k.id(+) and f.lie_id in (0)";
		ResultSetList rsl = con.getResultSetList(chaxun);
	    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
	    
	    egu.setTableName("jieszbsjb");
	    //egu.getColumn("id").setHidden(true);
	    egu.getColumn("zhibb_id").setHeader("结算指标");
		egu.getColumn("changf").setHeader("厂方");
		egu.getColumn("gongf").setHeader("矿方");
		egu.getColumn("jies").setHeader("结算");
		egu.getColumn("zhibb_id").setEditor(null);
		egu.getColumn("changf").setEditor(null);
		egu.getColumn("gongf").setEditor(null);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
		egu.addPaging(100);//设置分页
		egu.setWidth(1000);//设置页面的宽度,当超过这个宽度时显示滚动条
		if(((Visit) this.getPage().getVisit()).getString5()==null||((Visit) this.getPage().getVisit()).getString5().equals("")){
		    String Ys=MainGlobal.getYansbh();
			egu.addTbarText("验收编号:"+Ys);
			
			((Visit)this.getPage().getVisit()).setString3(Ys);
			}else{
			egu.addTbarText("验收编号:"+(((Visit) this.getPage().getVisit()).getString5()));
			((Visit)this.getPage().getVisit()).setString3((((Visit) this.getPage().getVisit()).getString5()));
			}
		
		egu.addToolbarItem("{"+new GridButton("返回","function(){document.all.item('RoolbackButton').click()}").getScript()+"}");
		egu.addToolbarItem("{"+new GridButton("确认","function(){document.all.item('SubmitButton').click()}").getScript()+"}");
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
		
		egu.addTbarBtn(new GridButton("以矿方为准","function(){CountKuangf(gridDiv_grid);this.setDisabled(false);}") );

		egu.addTbarBtn(new GridButton("计算平均值","function(){CountShc(gridDiv_grid);this.setDisabled(false);}") );
		
//		if (_jieszbsftz.equals("是")) {
			// 如果结算指标取矿方结算指标，那么在输入矿方结算指标时，结算指标自动进行修改
		String _jieszbsftz = "gridDiv_grid.on('afteredit',countJies);\n"
				+ "\n"
				+ "function countJies(e){\n"
				+ "\trec = e.record;\n"
				+ "\tchangeValue = parseFloat(e.value - e.originalValue);\n"
				+ "\tif(e.field=='GONGF'){\n" + "\n"
				+ "\t\trec.set('JIES',eval(rec.get('GONGF')));\n"
				+ "\t}\n" + "}";
		egu.addOtherScript(_jieszbsftz);
//		}
		
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
	
	public IPropertySelectionModel getIZhibModels() {
		
		String sql = "select id,bianm from zhibb ";
		((Visit) this.getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}
	
	public IPropertySelectionModel getIZhibModel() {
		
		if(((Visit) this.getPage().getVisit()).getProSelectionModel1()==null){
			
			getIZhibModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setProSelectionModel1(null);
			getIZhibModels();
		   visit.setString3(null);
		}
		
		getSelectData();
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
			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
	public static String getNewID(long diancxxb_id) {
		JDBCcon con = new JDBCcon();
		String id = "";
		ResultSetList rs = con.getResultSetList("select xl_xul_id.nextval id from dual");
		if(rs.next()) {
			id = rs.getString(0);
		}
		return diancxxb_id + id;
	}
}
