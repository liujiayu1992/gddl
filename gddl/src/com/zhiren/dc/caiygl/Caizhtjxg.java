package com.zhiren.dc.caiygl;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caizhtjxg extends BasePage implements PageValidateListener {
	
	private static String PAR_CAIY = "caiy"; //采样人样资源
	private static String PAR_ZHIY = "zhiy"; //制样人员资源
	private static String PAR_ALL = "all";  //全部
	
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
	}
	
//	绑定日期
	private boolean riqchange=false;
	public String getBeginRiq() {
		if (((Visit) getPage().getVisit()).getString3()==null){
			((Visit) getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		
		return ((Visit) getPage().getVisit()).getString3();
	}
	public void setBeginRiq(String riq) {
		if (riq != null 
				&& !riq.equals(((Visit) getPage().getVisit()).getString3())) {
			((Visit) getPage().getVisit()).setString3(riq);
			riqchange=true;
		}
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	//发货信息下拉框	
	public void setFahxxDownBean(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	public IDropDownBean getFahxxDownBean() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getFahxxModel().getOptionCount()>0) {
				setFahxxDownBean((IDropDownBean)getFahxxModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}

	
	public void setFahxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public IPropertySelectionModel getFahxxModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getFahxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	public void getFahxxModels() {
		String sql =
			"SELECT DISTINCT z.zhilb_id AS ID, getfahxx4zl(c.zhilb_id) as fahxx\n" + 
			"  FROM zhillsb z, caiyb c, yangpdhb d\n" + 
			" WHERE c.id = d.caiyb_id\n" + 
			"   AND d.zhilblsb_id = z.id\n" + 
			"   AND c.caiyrq = to_date('" + this.getBeginRiq()+ "', 'yyyy-mm-dd')\n" + 
			"   AND z.zhilb_id IN\n" + 
			"       (SELECT zhilb_id\n" + 
			"          FROM fahb f, diancxxb d\n" + 
			"         WHERE f.diancxxb_id = d.id\n" + 
			"           AND d.id = 182\n" + 
			"           AND daohrq = to_date('" + this.getBeginRiq()+ "', 'yyyy-mm-dd')\n" + 
			"       )\n" + 
			" ORDER BY id,fahxx";
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql, "请选择"));
	}
	
	
	private boolean hasFenc(JDBCcon con){//有分厂返回  true
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			rsl.close();
			return true;
		}
		rsl.close();
		return false;
	}
	
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ShedsqlChick = false;

	public void ShedsqlButton(IRequestCycle cycle) {
		_ShedsqlChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		
		if (_ShedsqlChick) {
			_ShedsqlChick = false;
			((Visit)getPage().getVisit()).setString12(this.getBeginRiq());
			Update(cycle);
		}
		
		if (riqchange) {
//			this.getFahxxModels();
			riqchange = false;
		}
	}
	
	private void Update(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Caiyangry");
	}
	
	public void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择一行数据进行查看！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Caitjbmxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String dataIndex = "";
		String colvalue = "";
		String zhillsbId = "";
		String yangpdhbId = "";
		String zhuanmbId = "";
		String sqlZ = "select * from zhuanmlb order by jib";
		ResultSetList rs = con.getResultSetList(sqlZ);
		
		String sql = "begin\n";
		while (rsl.next()) {
			if (!"0".equals(rsl.getString("id"))) {
				
				rs.beforefirst();
				while (rs.next()) {
					dataIndex = "BM" + rs.getString("id");
					colvalue = rsl.getString(dataIndex);
					sql += "UPDATE zhuanmb SET bianm='" + colvalue + "'where zhuanmlb_id=" + rs.getString("id") + " and zhillsb_id=" + rsl.getString("zhillsb_id") + "; \n";
				}
				
				sql += "UPDATE yangpdhb SET caiyfs='" + rsl.getString("caiyfs") + "',bianh='" + rsl.getString("BM100661") 
					+ "', meikxxb_id ='" + this.getExtGrid().getColumn("meikmc").combo.getBeanId(rsl.getString("meikmc")) + "'" 
					+ ",beiz='" + rsl.getString("caiyry") + "'\n" 
					+ " WHERE id=" + rsl.getString("id") + "; \n";
				
			} else {
				zhillsbId = MainGlobal.getNewID(visit.getDiancxxb_id());
				yangpdhbId = MainGlobal.getNewID(visit.getDiancxxb_id());
				rs.beforefirst();				
				while (rs.next()) {
					zhuanmbId = MainGlobal.getNewID(visit.getDiancxxb_id());
					dataIndex = "BM" + rs.getString("id");
					colvalue = rsl.getString(dataIndex);
					sql += 
						"INSERT INTO zhuanmb (ID,zhillsb_id,bianm,zhuanmlb_id)\n" +
						"VALUES(" + zhuanmbId + "," + zhillsbId + ",'" + colvalue + "'," + rs.getString("id") + "); \n";

				}
				
				//插入zhillsb				
				sql += 
					"INSERT INTO zhillsb\n" +
					"(id,zhilb_id,shenhzt,huaylb,huaylbb_id,BUMB_ID)\n" + 
					"VALUES(" + zhillsbId + ",0,0,'正常样',257624,257623); \n";

				
				//插入yangpdhb				
				sql += 
					"INSERT INTO yangpdhb\n" +
					"(id,caiyb_id,zhilblsb_id,lurry,caiysj,caiyfs,bianh,leib,leibb_id,BUMB_ID,kaissj,jiessj,meikxxb_id,beiz)\n" + 
					"VALUES(" + yangpdhbId + ",0," + zhillsbId + ",'" + rsl.getString("lurry") + "', Date '" + rsl.getString("caiysj") + 
					"','" + rsl.getString("caiyfs") + "','" + rsl.getString("BM100661") + "','正常样',257624,257623,'" + rsl.getString("kaissj") + "','" + 
					rsl.getString("jiessj") + "'," + this.getExtGrid().getColumn("meikmc").combo.getBeanId(rsl.getString("meikmc")) +
					",'" + rsl.getString("caiyry") + "'" +
					"); \n";			
			}
		}
		
		rsl = getExtGrid().getDeleteResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Caitjbmxg.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		while (rsl.next()) {
			if (!"0".equals(rsl.getString("id"))) {
				sql += 
					"DELETE FROM yangpdhb WHERE ID=" + rsl.getString("id") + "; \n" +
					"DELETE FROM zhillsb WHERE ID=" + rsl.getString("zhillsb_id") + "; \n" + 
					"DELETE FROM zhuanmb WHERE zhillsb_id=" + rsl.getString("zhillsb_id") + "; \n";

			}
		}
		
		sql += "end;";
		if(sql.length()>13){
			con.getUpdate(sql);
//			getSelectData();
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		String sql = "select * from zhuanmlb order by jib";
		ResultSetList rsl = con.getResultSetList(sql);
//		查询不同的编号所需的sqltmp
		String sqltmp = "";	
		String[] columnId = new String [rsl.getRows()];
		while(rsl.next()){
			columnId[rsl.getRow()] = //"BM" + 
				rsl.getString("id");
			sqltmp += ",(SELECT bianm FROM zhuanmb WHERE zhillsb_id=yp.zhilblsb_id AND zhuanmlb_id= "+rsl.getString("id")+") " + rsl.getString("mingc") + "\n";
		}
		rsl.close();
		
		
		sql = 
			"SELECT yp.id, zl.id AS zhillsb_id,yp.caiysj,\n" +
			"nvl(yp.kaissj,to_char(SYSDATE,'hh24:mi:ss')) AS kaissj,\n" + 
			"nvl(yp.jiessj,to_char(SYSDATE,'hh24:mi:ss')) AS jiessj,\n" + 
			" nvl(mk.piny||'---'||mk.mingc,'') as meikmc,yp.caiyfs";

		sql += sqltmp;
		
		sql +=
			",decode(yp.beiz,NULL,GetCaiyry(yp.id),yp.beiz) AS caiyry, \n" +
			"yp.lurry,decode(zl.shenhzt,0,'未化验','已化验') shenhzt\n" +
			"FROM yangpdhb yp, zhillsb zl, meikxxb mk\n" + 
			"WHERE yp.zhilblsb_id=zl.id\n" + 
			"AND yp.meikxxb_id = mk.id\n" + 
			"AND yp.caiysj=to_date('" + this.getBeginRiq() + "','yyyy-mm-dd') \n" +
			"order by 采样编码";		
		
		rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
//		设置grid为可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setDefaultsortable(false);
		egu.setEnableHdMenu(false);
//		设置为grid数据不分页
		egu.addPaging(0);
//		设置grid宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置grid列信息
		egu.getColumn("zhillsb_id").setHidden(true);
		egu.getColumn("caiysj").setHeader("采样日期");
		egu.getColumn("caiysj").setDefaultValue(this.getBeginRiq());
		egu.getColumn("caiysj").setWidth(100);
		egu.getColumn("caiysj").setEditor(null);
		egu.getColumn("kaissj").setHeader("开始时间");
		egu.getColumn("kaissj").setDefaultValue("00:00:00");
		egu.getColumn("kaissj").setWidth(80);
		egu.getColumn("jiessj").setHeader("结束时间");
		egu.getColumn("jiessj").setDefaultValue("23:59:59");
		egu.getColumn("jiessj").setWidth(80);
		egu.getColumn("meikmc").setHeader("煤矿名称");
		egu.getColumn("meikmc").setWidth(150);
		egu.getColumn("caiyfs").setHeader("采样方式");
		egu.getColumn("caiyfs").setWidth(100);
		egu.getColumn("shenhzt").setEditor(null);
		egu.getColumn("caiyry").setHeader("采样人");
//		egu.getColumn("caiyry").setEditor(null);
		egu.getColumn("lurry").setHeader("录入人");
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setHidden(true);
		egu.getColumn("shenhzt").setHeader("化验状态");
		egu.getColumn("shenhzt").setDefaultValue("未化验");
		egu.getColumn("shenhzt").setWidth(80);

		for(int i = 0; i< columnId.length ; i++){
			egu.getColumn(i+8).setDataindex("BM" + columnId[i]);
			egu.getColumn(i+8).setWidth(130);
			egu.getColumn(i+8).setDefaultValue("JC" + getBeginRiq().substring(2,4)+getBeginRiq().substring(5,7) + getBeginRiq().substring(8,10));
			TextField t = new TextField();
			t.setSelectOnFocus(false);
			egu.getColumn(i+8).setEditor(t);
		}
		
		TextField t = new TextField();
		t.setSelectOnFocus(false);
		egu.getColumn("caiyfs").setEditor(t);
		
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.getColumn("meikmc").setEditor(new ComboBox());
		egu
		.getColumn("meikmc")
		.setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id,nvl(piny||'---'||mingc,'') mingc from meikxxb"
				));
		egu.getColumn("meikmc").setReturnId(true);
		((ComboBox)(egu.getColumn("meikmc").editor)).setEditable(true);
		
		
//		增加grid中Toolbar显示日期参数
		egu.addTbarText("采样时间:");
		DateField dStart = new DateField();
		dStart.Binding("BeginRq","Form0");
		dStart.setValue(getBeginRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("-");
		
//		设置grid按钮
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		if (PAR_CAIY.equals(visit.getString10())) {
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			String sHander = 
				"function() {\n" +
				"  for(i=0;i<gridDiv_sm.getSelections().length;i++){\n" + 
				"    record = gridDiv_sm.getSelections()[i];\n" + 
				"\n" + 
				"    if(record.get('SHENHZT')=='已化验') {\n" + 
				"      Ext.MessageBox.alert('提示信息','不能删除已化验数据!');\n" + 
				"      return;\n" + 
				"    }\n" + 

				"gridDiv_history += '<result>' + '<sign>D</sign>' + '<ID update=\"true\">' + record.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
				"+ '<ZHILLSB_ID update=\"true\">' + record.get('ZHILLSB_ID')+ '</ZHILLSB_ID>'\n" + 
				"+ '<CAIYSJ update=\"true\">' + ('object' != typeof(record.get('CAIYSJ'))?record.get('CAIYSJ'):record.get('CAIYSJ').dateFormat('Y-m-d'))+ '</CAIYSJ>'\n" + 
				"+ '<KAISSJ update=\"true\">' + record.get('KAISSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</KAISSJ>'\n" + 
				"+ '<JIESSJ update=\"true\">' + record.get('JIESSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JIESSJ>'\n" + 
				"+ '<MEIKMC update=\"true\">' + record.get('MEIKMC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKMC>'\n" + 
				"+ '<CAIYFS update=\"true\">' + record.get('CAIYFS').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYFS>'\n" + 
				"+ '<BM100661 update=\"true\">' + record.get('BM100661').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BM100661>'\n" + 
				"+ '<BM100662 update=\"true\">' + record.get('BM100662').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BM100662>'\n" + 
				"+ '<BM100663 update=\"true\">' + record.get('BM100663').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BM100663>'\n" + 
				"+ '<CAIYRY update=\"true\">' + record.get('CAIYRY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYRY>'\n" + 
				"+ '<LURRY update=\"true\">' + record.get('LURRY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LURRY>'\n" + 
				"+ '<SHENHZT update=\"true\">' + record.get('SHENHZT').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENHZT>'\n" + 
				" + '</result>' ;" +

				"     gridDiv_ds.remove(gridDiv_sm.getSelections()[i--]);\n" + 
				"   }\n" + 
				" }";
	
			GridButton gDelete = new GridButton("删除", sHander, SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gDelete);
//			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

			egu.getColumn("BM100662").setHidden(true);
			egu.getColumn("BM100663").setHidden(true);
			
			
		} else if (PAR_ZHIY.equals(visit.getString10())) {
			egu.getColumn("kaissj").setHidden(true);
			egu.getColumn("jiessj").setHidden(true);
			egu.getColumn("meikmc").setHidden(true);
			egu.getColumn("caiyfs").setHidden(true);
			egu.getColumn("BM100662").setHidden(true);
			egu.getColumn("caiyry").setHidden(true);
			egu.getColumn("BM100661").setEditor(null);
//			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			
		} else {
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			String sHander = 
				"function() {\n" +
				"  for(i=0;i<gridDiv_sm.getSelections().length;i++){\n" + 
				"    record = gridDiv_sm.getSelections()[i];\n" + 
				"\n" + 
				"    if(record.get('SHENHZT')=='已化验') {\n" + 
				"      Ext.MessageBox.alert('提示信息','不能删除已化验数据!');\n" + 
				"      return;\n" + 
				"    }\n" + 

				"gridDiv_history += '<result>' + '<sign>D</sign>' + '<ID update=\"true\">' + record.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
				"+ '<ZHILLSB_ID update=\"true\">' + record.get('ZHILLSB_ID')+ '</ZHILLSB_ID>'\n" + 
				"+ '<CAIYSJ update=\"true\">' + ('object' != typeof(record.get('CAIYSJ'))?record.get('CAIYSJ'):record.get('CAIYSJ').dateFormat('Y-m-d'))+ '</CAIYSJ>'\n" + 
				"+ '<KAISSJ update=\"true\">' + record.get('KAISSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</KAISSJ>'\n" + 
				"+ '<JIESSJ update=\"true\">' + record.get('JIESSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JIESSJ>'\n" + 
				"+ '<MEIKMC update=\"true\">' + record.get('MEIKMC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKMC>'\n" + 
				"+ '<CAIYFS update=\"true\">' + record.get('CAIYFS').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYFS>'\n" + 
				"+ '<BM100661 update=\"true\">' + record.get('BM100661').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BM100661>'\n" + 
				"+ '<BM100662 update=\"true\">' + record.get('BM100662').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BM100662>'\n" + 
				"+ '<BM100663 update=\"true\">' + record.get('BM100663').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BM100663>'\n" + 
				"+ '<CAIYRY update=\"true\">' + record.get('CAIYRY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYRY>'\n" + 
				"+ '<LURRY update=\"true\">' + record.get('LURRY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LURRY>'\n" + 
				"+ '<SHENHZT update=\"true\">' + record.get('SHENHZT').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENHZT>'\n" + 
				" + '</result>' ;" +

				"     gridDiv_ds.remove(gridDiv_sm.getSelections()[i--]);\n" + 
				"   }\n" + 
				" }";
	
			GridButton gDelete = new GridButton("删除", sHander, SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gDelete);		
			
		}
		
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
			StringBuffer saveHander = new StringBuffer();
			if (PAR_CAIY.equals(visit.getString10())) {
				saveHander.append(" var v_field = 'BM100661';\n");
			} else if (PAR_ZHIY.equals(visit.getString10())) {
				saveHander.append(" var v_field = 'BM100663';\n");
			}
			
			saveHander.append(" for (var i=0;i<gridDiv_ds.getCount();i++) {\n");  
			saveHander.append("   for (var j=0;j<gridDiv_ds.getCount();j++) {\n");
			saveHander.append("    if (i != j) {\n");
			saveHander.append("      if (gridDiv_ds.getAt(i).get(v_field)==gridDiv_ds.getAt(j).get(v_field)) {\n");
			if (PAR_CAIY.equals(visit.getString10())) {
				saveHander.append("        Ext.MessageBox.alert('提示信息','第' + (i+1) + '行与第' + (j+1) + '行的采样编码重复');\n");
				saveHander.append("        return;");
			} else if (PAR_ZHIY.equals(visit.getString10())) {
				saveHander.append("        if (gridDiv_ds.getAt(i).get(v_field)!='JC" + getBeginRiq().substring(2,4)+getBeginRiq().substring(5,7)+getBeginRiq().substring(8,10) + "') { \n");
				saveHander.append("        		Ext.MessageBox.alert('提示信息','第' + (i+1) + '行与第' + (j+1) + '行的化验编码重复');\n");
				saveHander.append("             return;\n");
				saveHander.append("        } \n");
			}			
			saveHander.append("      }\n");
			saveHander.append("    }\n"); 
			saveHander.append("  }\n"); 
			saveHander.append("}");

		
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton", saveHander.toString());
		
		if (!PAR_ZHIY.equals(visit.getString10())) {
			GridButton gb = new GridButton(
					"选择采样人员",
					"function(){"
							+ "if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){"
							+ "Ext.MessageBox.alert('提示信息','请选中一条采样记录');"
							+ "return;}"
							+ "grid1_rcd = gridDiv_sm.getSelections()[0];"
							+ "var fun=gridDiv_grid.on('afteredit',function(e){"
							+ "Ext.MessageBox.alert('提示信息','在选择采样人员之前请先保存');"
							+ "return;});" + "if(grid1_rcd.get('ID') == '0'){"
							+ "Ext.MessageBox.alert('提示信息','在选择采样人员之前请先保存!');"
							+ "return;}" + "grid1_history = grid1_rcd.get('ID');"
							+ "var Cobj = document.getElementById('CHANGE');"
							+ "Cobj.value = grid1_history;"
							+ "document.getElementById('ShedsqlButton').click();"
							+ "}");
	
			gb.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(gb);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('SHENHZT')=='已化验'){e.cancel=true;}");//当"已化验"时,这一行不允许编辑
		sb.append("});");
		egu.addOtherScript(sb.toString());
		
		for(int i=0;i<egu.getGridColumns().size();i++){
			((GridColumn)(egu.getGridColumns().get(i))).setRenderer(" function(value){return '<font size=3>'+value+'</font>';} ");
		}
		
		setExtGrid(egu);
		con.Close();
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			this.treeid = treeid;
		}
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			visit.setString2(null);
			
			setBeginRiq(DateUtil.FormatDate(new Date()));
//			setEndRiq(DateUtil.FormatDate(new Date()));
//			visit.setProSelectionModel1(null);
//			visit.setDropDownBean1(null);
			setExtGrid(null);
//			getFahxxModels();
			
			if (getPageName().toString().equals(visit.getString11())) {
				visit.setString10(PAR_CAIY);
				if (visit.getString12()!=null) {
					 String DatePattern = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}";
				     Pattern p = Pattern.compile(DatePattern);
				     Matcher m = p.matcher(visit.getString12());
				     if (m.matches()) {
				    	 setBeginRiq(visit.getString12());
				     }
				}
			} else {
				visit.setString3(null);
				visit.setString10(null);
				visit.setString12(null);
				setBeginRiq(DateUtil.FormatDate(new Date()));
			}
			visit.setString11(getPageName().toString());
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			String czhParameter = cycle.getRequestContext().getParameters("lx")[0];
			visit.setString10(czhParameter);
		} 
		getSelectData();		
	}
}
