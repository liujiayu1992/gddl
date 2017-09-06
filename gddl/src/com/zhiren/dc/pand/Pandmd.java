package com.zhiren.dc.pand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-09-10 16：30
 * 描述：修改一厂多制下过滤盘点编码的方法
 */
/**
 * 
 * @author 张琦
 * @version 1.1.2.5
 */
public class Pandmd extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	//设置盘点器皿容积的值
	private double zhi = -1;
	public double getZhi() {
		if(this.zhi == -1){
			setZhi();
		}
			return zhi;
	}
	public void setZhi() {
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select zhi from xitxxb where mingc = '盘点器皿容积' and diancxxb_id ="+v.getDiancxxb_id());
		if(rsl.next()){
			this.zhi=rsl.getDouble("zhi");
		}else{
			setMsg("请先设置盘点器皿容积的值");
		}
		
		rsl.close();
		con.Close();
	}
	
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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
	//盘点编号下拉框
	public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pandb p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ " and p.zhuangt=0"+ " order by p.id desc";
		    v.setProSelectionModel10(new IDropDownModel(sql,"请选择"));
		}
	    return v.getProSelectionModel10();
	}
	public void setPandValue(IDropDownBean value) {
		((Visit)getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getPandModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		String pandbm = "";
		//判断页面是否有盘点码，如果没有的话，从数据库中读取
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pandb where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + " and zhuangt=0 order by id desc";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				pandbm = rsl.getString("bianm");
			}
			return pandbm;
		}
		return getPandValue().getValue();
	}
	public long getPandbID() {
		if (getPandValue() == null) {
			return -1;
		}
		return getPandValue().getId();
	}
	
	
	//设置测量方法下拉框
	private IPropertySelectionModel _celModel;
	public void setCelModel(IPropertySelectionModel  value){
		
		_celModel = value;
	}
	public IPropertySelectionModel getCelModel(){
		if(_celModel == null){
			List list = new ArrayList();
		
			list.add(new IDropDownBean(1,"模拟"));
			list.add(new IDropDownBean(2,"沉桶"));
			list.add(new IDropDownBean(3,"其它"));
			_celModel = new IDropDownModel(list);
		}
		return _celModel;
	}
	
	public void setCelValue(IDropDownBean value) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setDropDownBean6(value);
		
	}
	public IDropDownBean getCelValue() {
		Visit visit = (Visit) getPage().getVisit();
		if(visit.getDropDownBean6()== null){
			setCelValue((IDropDownBean)getCelModel().getOption(0));
		}
		return visit.getDropDownBean6();
	}
	//刷新按钮
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
//	private boolean _AddChick = false;
//	public void AddButton(IRequestCycle cycle) {
//		_AddChick = true;
//	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} 
//		else if (_AddChick) {
//			getSelectData();
//		}
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		String celffid = getCelValue().getValue();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Pandmd.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//进行删除操作是添加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meicmd,
					"pandmdb",id+"");
			sSql = "delete from pandmdb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Pandmd.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if(celffid=="模拟"){
			while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sSql = "insert into pandmdb(id, pandb_id, meicb_id, ced, celff, yangpzl, cedjj, chentmz, chentpz) values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ getPandbID() + ","
					+ getMeicbID(con, rsl.getString("mingc")) + ",'"
					+ rsl.getString("ced") + "','"
					+ rsl.getString("celff") + "',"
					+ rsl.getDouble("yangpzl") + ","
					+ rsl.getDouble("cedjj") + ",0,0"
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					setMsg("保存失败!");
					con.rollBack();
					con.Close();
					return;
				}
			} else {
				//进行修改操作时添加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meicmd,
						"pandmdb",id+"");
				sSql = "update pandmdb set "
					+ " pandb_id=" + getPandbID() + ","					
					+ " meicb_id=" + getMeicbID(con, rsl.getString("mingc")) + ","
					+ " ced='" + rsl.getString("ced") + "',"
					+ " celff='" + rsl.getString("celff") + "',"
					+ " yangpzl=" + rsl.getDouble("yangpzl") + ","
					+ " cedjj=" + rsl.getDouble("cedjj")
					+ "  where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					setMsg("保存失败");
					con.rollBack();
					con.Close();
					return;
					}
				}
			}
		}else if(celffid=="沉桶"){
			while (rsl.next()) {
				id = rsl.getLong("id");
				if (id == 0) {
					sSql = "insert into pandmdb(id, pandb_id, meicb_id, ced, celff, yangpzl, cedjj, chentmz, chentpz) values(getNewId(" + visit.getDiancxxb_id() + "),"
						+ getPandbID() + ","
						+ getMeicbID(con, rsl.getString("mingc")) + ",'"
						+ rsl.getString("ced") + "','"
						+ rsl.getString("celff") + "',"
						+ rsl.getDouble("yangpzl") + ","
						+ rsl.getDouble("cedjj") + ","
						+ rsl.getDouble("chentmz") + ","
						+ rsl.getDouble("chentpz")
						+ " )";
					flag = con.getInsert(sSql);
					if (flag == -1) {
						setMsg("保存失败!");
						con.rollBack();
						con.Close();
						return;
					}
				} else {
				//进行修改操作时添加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meicmd,
						"pandmdb",id+"");
					sSql = "update pandmdb set "
						+ " pandb_id=" + getPandbID() + ","					
						+ " meicb_id=" + getMeicbID(con, rsl.getString("mingc")) + ","
						+ " ced='" + rsl.getString("ced") + "',"
						+ " celff='" + rsl.getString("celff") + "',"
						+ " yangpzl=" + rsl.getDouble("yangpzl") + ","
						+ " cedjj=" + rsl.getDouble("cedjj")+","
						+ " chentmz=" + rsl.getDouble("chentmz") + ","
						+ " chentpz=" + rsl.getDouble("chentpz")
						+ "  where id=" + id;
					flag = con.getUpdate(sSql);
					if (flag == -1) {
						setMsg("保存失败");
						con.rollBack();
						con.Close();
						return;
						}
					}
				}
			
		}else{
			while (rsl.next()) {
				id = rsl.getLong("id");
				if (id == 0) {
					sSql = "insert into pandmdb(id, pandb_id, meicb_id, ced, celff, yangpzl, cedjj, chentmz, chentpz,pandrqrj,mid) values(getNewId(" + visit.getDiancxxb_id() + "),"
						+ getPandbID() + ","
						+ getMeicbID(con, rsl.getString("mingc")) + ",'"
						+ rsl.getString("ced") + "','"
						+ rsl.getString("celff") + "',"
						+ rsl.getDouble("yangpzl") + ","
						+ rsl.getDouble("cedjj") + ","
						+ rsl.getDouble("chentmz") + ","
						+ rsl.getDouble("chentpz")+","
						+ rsl.getDouble("zhi")+","
						+ rsl.getDouble("mid")
						+ " )";
					flag = con.getInsert(sSql);
					if (flag == -1) {
						setMsg("保存失败!");
						con.rollBack();
						con.Close();
						return;
					}
				} else {
				//进行修改操作时添加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meicmd,
						"pandmdb",id+"");
					sSql = "update pandmdb set "
						+ " pandb_id=" + getPandbID() + ","					
						+ " meicb_id=" + getMeicbID(con, rsl.getString("mingc")) + ","
						+ " ced='" + rsl.getString("ced") + "',"
						+ " celff='" + rsl.getString("celff") + "',"
						+ " yangpzl=" + rsl.getDouble("yangpzl") + ","
						+ " cedjj=" + rsl.getDouble("cedjj")+","
						+ " chentmz=" + rsl.getDouble("chentmz") + ","
						+ " chentpz=" + rsl.getDouble("chentpz")+","
						+ " pandrqrj=" +rsl.getDouble("zhi")+","
						+ " mid=" + rsl.getDouble("mid")
						+ "  where id=" + id;
					flag = con.getUpdate(sSql);
					if (flag == -1) {
						setMsg("保存失败");
						con.rollBack();
						con.Close();
						return;
						}
					}
				}
			
		
		}
	}
	public String getMeicbID(JDBCcon con, String meicMC) {
		String yougbID = "";
		String sql = "select id from meicb where mingc='" + meicMC + "' and meicb.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			yougbID = rs.getString("id");
		}
		return yougbID;
	}
	public void getSelectData() {
		String sSql = "";
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String celffid = getCelValue().getValue();//得到页面选择的测量方法，如果是模拟法就执行IF中的语句，如果是沉桶法就执行ELSE中的语句
		boolean cel = false;
//		修正查询时的密度
		String where ="";
		if(visit.isDCUser()){
			where= "and b.diancxxb_id = "+visit.getDiancxxb_id()+" \n";
		}
		if(celffid == "模拟"){
			sSql = "select p.id,\n" +
				"       m.mingc,\n" + 
				"       p.yangpzl,\n" + 
				"       k.zhi,\n" + 
				"       p.celff,\n" + 
				"       p.ced,\n" + 
				"       p.cedjj,\n" +
				"       round((p.yangpzl / k.zhi * decode(p.cedjj,0,1,p.cedjj)) / decode(p.cedjj,0,1,p.cedjj),5) mid\n" + 
				"  from pandmdb p,\n" + 
				"       pandb b,\n" + 
				"       meicb m,\n" + 
				"       (select x.zhi,x.diancxxb_id dc\n" + 
				"          from xitxxb x\n" + 
				"         where x.mingc = '盘点器皿容积' ) k\n" + 
				" where b.id = p.pandb_id\n" + 
				"   and m.id = p.meicb_id\n" + 
				"   and p.celff = '模拟' and k.dc=b.diancxxb_id\n" + 
				"   and b.bianm = '"+getPandbm()+"' \n" +
				where+
				" order by m.xuh,m.mingc,p.ced \n";

			cel = true;

		}else if(celffid == "沉桶"){
			sSql = 	"select p.id,\n" +
				"       m.mingc,\n" + 
				"       p.chentmz,\n" + 
				"       p.chentpz,\n" + 
				"       p.chentmz - p.chentpz yangpzl,\n" + 
				"       k.zhi,\n" + 
				"       p.celff,\n" + 
				"       p.ced,\n" + 
				"       p.cedjj,\n" + 
				"       round((p.chentmz-p.chentpz)/decode(k.zhi,0,1,k.zhi),5) mid\n" + 
				"  from pandmdb p,\n" + 
				"       pandb b,\n" + 
				"       meicb m,\n" + 
				"       (select x.zhi,x.diancxxb_id dc\n" + 
				"          from xitxxb x\n" + 
				"         where x.mingc = '盘点器皿容积'  ) k\n" + 
				" where p.pandb_id = b.id\n" + 
				"   and m.id = p.meicb_id\n" + 
				"   and p.celff = '沉桶' and k.dc=b.diancxxb_id\n" + 
				"   and b.bianm = '"+getPandbm()+"'\n" + 
				where+
				" order by m.xuh,m.mingc,p.ced";
			cel=true;
			
		}else{
			sSql = 
				"select p.id,\n" +
				"\t\t\t\t       m.mingc,\n" + 
				"\t\t\t\t       p.yangpzl,\n" + 
				"\t\t\t\t       p.pandrqrj as zhi,\n" + 
				"\t\t\t\t       p.celff,\n" + 
				"\t\t\t\t       p.ced,\n" + 
				"\t\t\t\t       p.cedjj,\n" + 
				"               p.mid\n" + 
				"\t\t\t\t  from pandmdb p,\n" + 
				"\t\t\t\t       pandb b,\n" + 
				"\t\t\t\t       meicb m\n" + 
				"\t\t\t\t where b.id = p.pandb_id\n" + 
				"\t\t\t\t   and m.id = p.meicb_id\n" + 
				"\t\t\t\t  and p.celff = '其它'\n" + 
				"\t\t\t\t   and b.bianm = '"+getPandbm()+"'\n" + 
				where+
				"\t\t\t\t order by m.xuh,m.mingc,p.ced";
          
		}
		

			
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = getEgu(cel,rsl);
//		 设置煤场下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("mingc").setEditor(c1);
		c1.setEditable(true);
		String Sql = "select id ,mingc from meicb where diancxxb_id="+visit.getDiancxxb_id();
		egu.getColumn("mingc").setComboEditor(egu.gridId,new IDropDownModel(Sql));
//		设置工具栏盘点编码下拉框
		egu.addTbarText("盘点编码：");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
//		设置测量方法下拉框
		egu.addTbarText("测量方法");
		ComboBox cobCel = new ComboBox();
		cobCel.setWidth(100);
		cobCel.setTransform("CelDropDown");
		cobCel.setId("CelDropDown");
		cobCel.setLazyRender(true);
		egu.addToolbarItem(cobCel.getScript());
		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);


		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String fun="";
		if(!cel){ //点击查看密度按钮后，提示的是沉桶算法的密度值
			 fun ="function(){var count = gridDiv_ds.getCount();\n" +
			"if(count<=0){\n" + 
			"Ext.MessageBox.alert('提示信息','请先输入数据');\n" + 
			"return;\n" + 
			"}\n" + 
			"var yangpzl=0;\n" + 
			"var zhi = 0;\n" + 
			"var mid =0;\n" + 
			"var cedjj=0;\n" + 
			"for(var i=0;i<count;i++){\n" + 
			"var record = gridDiv_ds.getAt(i);\n" + 
			"yangpzl=yangpzl+eval(record.get('YANGPZL')||0);\n" + 
			"zhi = record.get('ZHI');\n" + 
			"cedjj = cedjj+1;\n" + 
			"}\n" + 
			"mid =yangpzl/cedjj/zhi;\n" + 
			"mid=mid.toFixed(5);\n" + 
			"Ext.MessageBox.alert('提示信息','沉桶法的密度为:'+mid);}";
		}else{  //点击查看密度按钮后，提示的是模拟算法的密度值
			fun="function(){var count = gridDiv_ds.getCount();\n" +
			"if(count<=0){\n" +
			"Ext.MessageBox.alert('提示信息','请先输入数据');\n" + 
			"return;\n" + 
			"}\n" + 
			"var yangpzl=0;\n" + 
			"var zhi = 0;\n" + 
			"var mid =0;\n" + 
			"var cedjj=0;\n" + 
			"var sum1=0;\n" + 
			"var ced=0;\n" + 
			"for(var i=0;i<count;i++){\n" + 
			"var record = gridDiv_ds.getAt(i);\n" + 
			"yangpzl=eval(record.get('YANGPZL')||0);\n" + 
			"zhi = record.get('ZHI');\n" + 
			"cedjj = eval(record.get('CEDJJ')||0);\n" + 
			"if(cedjj==0){\n" + 
			"Ext.MessageBox.alert('提示信息','第'+(i+1)+'行测点间距不能为0');\n" + 
			"return;\n" + 
			"}\n" + 
			"ced =  ced+cedjj;\n" + 
			"var x=yangpzl/zhi*cedjj;\n" + 
			"sum1 = sum1+x;\n" + 
			"}\n" + 
			"mid =sum1/ced;\n" + 
			"mid=mid.toFixed(5);\n" + 
			"Ext.MessageBox.alert('提示信息','模拟法的密度为:'+mid);}";


		}

		GridButton gbn = new GridButton("查看密度",fun);
		gbn.setIcon(SysConstant.Btn_Icon_Show);
		egu.addTbarBtn(gbn);
		String scriptc = "\n  var celIndex = CelDropDown.getValue();\n"+
					"CelDropDown.on('select',function(o,record,index){ \n" +
					"if(celIndex!=CelDropDown.getValue()){document.forms[0].submit();}}); \n" ;
		egu.addOtherScript(scriptc);
		String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);
		if(cel){   //添加模拟法的密度算法JS
			String moni ="gridDiv_grid.on('afteredit',function(e){\n" +
				"if(e.field=='YANGPZL'||e.field=='CED'){\n" + 
				"var record = gridDiv_ds.getAt(e.row);\n" + 
				"var yangpzl = eval(record.get('YANGPZL')||0);\n" + 
				"var ced = eval(record.get('CED')||0);\n" + 
				"var cedjj = eval(record.get('CEDJJ')||0);\n" + 
				"var zhi = record.get('ZHI');\n" + 
				"var mid = yangpzl/zhi ;\n" + 
				"mid=mid.toFixed(5);\n" + 
				"record.set('MID',mid);\n" + 
				"}\n" + 
				"});";

			egu.addOtherScript(moni);

		}else{  //添加沉桶法的密度算法JS
			String ct =	"gridDiv_grid.on('afteredit',function(e){\n" +
				"if(e.field=='CHENTMZ'||e.field=='CHENTPZ'||e.field=='CED'||e.field=='CEDJJ'){\n" + 
				"var record=gridDiv_ds.getAt(e.row);\n" + 
				"var yangpzl = 0;\n" + 
				"var chentmz =eval(record.get('CHENTMZ')||0);\n" + 
				"var chentpz = eval(record.get('CHENTPZ')||0);\n" + 
				"var ced=eval(record.get('CED')||0);\n" + 
				"var cedjj = eval(record.get('CEDJJ')||0);\n" + 
				"var zhi = record.get('ZHI');\n" + 
				"yangpzl = chentmz-chentpz;\n" + 
				"record.set('YANGPZL',yangpzl);\n" + 
				"var mid=(yangpzl/zhi);\n" + 
				"mid =mid.toFixed(5);\n" + 
				"record.set('MID',mid);\n" + 
				"}});";
			egu.addOtherScript(ct);

		}
		setExtGrid(egu);
		con.Close();
	}
	public ExtGridUtil getEgu(boolean x,ResultSetList rsl){
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		if(x){ 
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.setWidth(Locale.Grid_DefaultWidth);
			egu.setHeight("bodyHeight");
			egu.getColumn("id").setHidden(true);
			egu.getColumn("mingc").setHeader(Locale.meicb_id_chepb);
			egu.getColumn("yangpzl").setHeader(Locale.yangpzl);
			egu.getColumn("zhi").setHeader(Locale.qimrj);
			egu.getColumn("zhi").setEditor(null);
			egu.getColumn("zhi").setDefaultValue(Double.toString(getZhi()));
			egu.getColumn("celff").setHeader(Locale.celff);
			egu.getColumn("celff").setEditor(null);
			egu.getColumn("celff").setDefaultValue("模拟");
			egu.getColumn("ced").setHeader(Locale.ced);
			egu.getColumn("ced").editor.setMaxLength(20);
			egu.getColumn("cedjj").setHeader(Locale.cedjj);
			egu.getColumn("cedjj").editor.setMaxValue("10000");
			egu.getColumn("mid").setHeader(Locale.mid);
			egu.getColumn("mid").setEditor(null);
		}else if(x){
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.setWidth(Locale.Grid_DefaultWidth);
			egu.setHeight("bodyHeight");
			egu.getColumn("id").setHidden(true);
			egu.getColumn("mingc").setHeader(Locale.meicb_id_chepb);
			egu.getColumn("chentmz").setHeader(Locale.chentmz);
			egu.getColumn("chentmz").editor.setMaxValue("99.9999");
			egu.getColumn("chentpz").setHeader(Locale.chentpz);
			egu.getColumn("chentpz").editor.setMaxValue("99.9999");
			egu.getColumn("yangpzl").setHeader(Locale.yangpzl);
			egu.getColumn("yangpzl").setEditor(null);
			egu.getColumn("zhi").setHeader(Locale.qimrj);
			egu.getColumn("zhi").setEditor(null);
			egu.getColumn("zhi").setDefaultValue(Double.toString(getZhi()));
			egu.getColumn("celff").setHeader(Locale.celff);
			egu.getColumn("celff").setEditor(null);
			egu.getColumn("celff").setDefaultValue("沉桶");
			egu.getColumn("ced").setHeader(Locale.ced);
			egu.getColumn("ced").editor.setMaxLength(20);
			egu.getColumn("cedjj").setHeader(Locale.cedjj);
			egu.getColumn("cedjj").editor.setMaxValue("10000");
			egu.getColumn("mid").setHeader(Locale.mid);
			egu.getColumn("mid").setEditor(null);
		}
		else{
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.setWidth(Locale.Grid_DefaultWidth);
			egu.setHeight("bodyHeight");
			egu.getColumn("id").setHidden(true);
			egu.getColumn("mingc").setHeader(Locale.meicb_id_chepb);
			egu.getColumn("yangpzl").setHeader(Locale.yangpzl);
			egu.getColumn("zhi").setHeader(Locale.qimrj);
			egu.getColumn("celff").setHeader(Locale.celff);
			egu.getColumn("celff").setEditor(null);
			egu.getColumn("celff").setDefaultValue("其它");
			egu.getColumn("ced").setHeader(Locale.ced);
			egu.getColumn("ced").editor.setMaxLength(20);
			egu.getColumn("cedjj").setHeader(Locale.cedjj);
			egu.getColumn("cedjj").editor.setMaxValue("10000");
			egu.getColumn("mid").setHeader(Locale.mid);
		}
		return egu;
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		//在进入页面之前先判断，如果在系统信息表中没有该厂的盘点器皿容积参数，提示用户设置
//		JDBCcon con = new JDBCcon();
//		ResultSetList rsl = con.getResultSetList("select * from xitxxb where mingc ='盘点器皿容积' and diancxxb_id ="+visit.getDiancxxb_id());
//		if(!rsl.next()){
//			setMsg("请先输入盘点器皿容积参数");
//			rsl.close();
//			con.Close();
//			//return;
//		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
			setCelModel(null);
			setCelValue(null);
			setZhi();
		}
		init();
//		rsl.close();
//		con.Close();
	}
	private void init() {
		getSelectData();
//		_AddChick = false;
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

}