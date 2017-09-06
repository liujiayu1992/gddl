package com.zhiren.dc.pandgd;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 修改人：ww
 * 时间：2010-09-17
 * 修改内容：计算密度时用四舍五入修约，不用四舍六入修约
 */
/*
 * 修改人：lip
 * 时间：2010-09-19
 * 修改内容：修正密度、容积保留小数位
 */

/*
 * 修改人：ww
 * 时间：2010-09-20
 * 修改内容：添加平均密度
 */
/*
 * 修改人：ww
 * 时间：2010-09-20
 * 修改内容：修改平均密度为加数求平均
 */
/*
 * 修改人：lip
 * 时间：2010-11-30
 * 修改内容：容积加参数选择，L或m3
 */
/*
 * 修改人：songy
 * 时间：2011-5-31
 * 修改内容：
 *///煤场直接得到当前电厂的id,不通过编码找电厂id,若编码重复有可能得到的就不是当前的电厂id了
   //pingjmd平均密度计算公式为净重加权平均
public class Meicmdcl extends BasePage implements PageValidateListener{
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
		}else if(v.isFencb()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pand_gd p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ "order by p.bianm desc";
			JDBCcon cn=new JDBCcon();
			ResultSetList rs=cn.getResultSetList(sql);
			if(rs.getRows()==0){
				v.setProSelectionModel10(new IDropDownModel(sql,"请添加盘点编码"));
			}else{
		    v.setProSelectionModel10(new IDropDownModel(sql));
			}
			rs.close();
			cn.Close();
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
			String sql = "select id,bianm from pand_gd where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "order by bianm desc";
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
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} 
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "meicdjmdcdb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//进行删除操作是添加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meicmd,
					"meicdjmdcdb",id+"");
			sSql = "delete from meicdjmdcdb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "meicdjmdcdb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
			while (rsl.next()) {
				id = rsl.getLong("id");
				if (id == 0) {
					sSql = "insert into meicdjmdcdb(id, pand_gd_id, meicb_id, tongb, zongz, piz, jingz, rongj, mid) values(getNewId(" + visit.getDiancxxb_id() + "),"
						+ getPandbID() + ","
						+ getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc")) + ",'"
						+ rsl.getString("tongb") + "','"
						+ rsl.getDouble("zongz") + "',"
						+ rsl.getDouble("piz") + ","
						+ rsl.getDouble("jingz") + ","
						+ rsl.getDouble("rongj")/visit.getInt1() + ","
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
						"meicdjmdcdb",id+"");
					sSql = "update meicdjmdcdb set "
						+ " pand_gd_id=" + getPandbID() + ","					
						+ " meicb_id=" + getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc")) + ","
						+ " tongb='" + rsl.getString("tongb") + "',"
						+ " zongz='" + rsl.getDouble("zongz") + "',"
						+ " piz=" + rsl.getDouble("piz") + ","
						+ " jingz=" + rsl.getDouble("jingz")+","
						+ " rongj=" + rsl.getDouble("rongj") + "/" + visit.getInt1() + ","
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
	public String getMeicbID(JDBCcon con, String meicMC) {
		String yougbID = "";
		String sql = "select id from meicb where mingc='" + meicMC + "' and meicb.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			yougbID = rs.getString("id");
		}
		return yougbID;
	}
	
	// 如果有分厂根据编码得电厂id否则直接得
	private long getDiancid(JDBCcon con) {
		Visit visit = (Visit) getPage().getVisit();
		long diancxxb_id = visit.getDiancxxb_id();
		ResultSetList rsl = null;
		String sql = "select diancxxb_id from PAND_GD where bianm = '" + getPandbm() + "'";
		rsl = con.getResultSetList(sql);
		if (visit.isFencb()) {
			if (rsl.next()) {
				diancxxb_id = rsl.getLong("diancxxb_id");
			}
		}
		
		rsl.close();
		return diancxxb_id;
	}
	
	public void getSelectData() {
		Visit visit  = (Visit)getPage().getVisit();
		String sSql = "";
		JDBCcon con = new JDBCcon();
		
//		取得盘煤密度、盘煤容积小数位
		int panmmdxsw = 3;
		int panmrjxsw = 2;
		panmmdxsw = Integer.parseInt(MainGlobal.getXitxx_item("盘点", "盘煤密度小数位", String.valueOf(visit.getDiancxxb_id()), "3"));
		panmrjxsw = Integer.parseInt(MainGlobal.getXitxx_item("盘点", "盘煤容积小数位", String.valueOf(visit.getDiancxxb_id()), "10"));

		sSql = "select p.id, m.mingc, p.tongb, p.zongz, p.piz, p.jingz, p.rongj*"+visit.getInt1()+" rongj, p.mid,\n" +" "+
			"(SELECT  DECODE(NVL(SUM(JINGZ),0), 0, 0, ROUND( SUM(MID*JINGZ) /SUM(JINGZ)," + panmmdxsw + ")) FROM meicdjmdcdb\n" +
			"WHERE meicb_id=m.id AND pand_gd_id=b.id) AS pingjmd \n" +
			"  from meicdjmdcdb p, meicb m, pand_gd b,diancxxb d\n" +" "+ 
			" where p.pand_gd_id = b.id and d.id = b.diancxxb_id\n" + " "+
			"   and m.id = p.meicb_id and b.bianm='"+getPandbm()+"' " +
			"   AND ( B.DIANCXXB_ID = " + getDiancid(con) + " OR d.fuid = " + getDiancid(con) + ")" +
			" order by m.mingc,p.tongb";
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("煤场");
		egu.getColumn("tongb").setHeader("桶别");
		egu.getColumn("zongz").setHeader("总重<br>(kg)");
		egu.getColumn("piz").setHeader("皮重<br>(kg)");
		egu.getColumn("jingz").setHeader("净重<br>(kg)");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("rongj").setHeader(visit.getString15());
        egu.getColumn("rongj").setDefaultValue("0.0322512684159");
		((NumberField)egu.getColumn("rongj").editor).setDecimalPrecision(panmrjxsw);
		egu.getColumn("mid").setHeader("密度<br>(t/m3)");
		((NumberField)egu.getColumn("mid").editor).setDecimalPrecision(panmmdxsw);
		egu.getColumn("pingjmd").setHeader("平均密度<br>(t/m3)");
		egu.getColumn("pingjmd").setEditor(null);
		
//		 设置煤场下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("mingc").setEditor(c1);
		c1.setEditable(true);
		String Sql = "select id, mingc from meicb where diancxxb_id = " +getDiancid(con);//直接得到当前电厂的id,不通过编码找电厂id,若编码重复有可能得到的就不是当前的电厂id了
		egu.getColumn("mingc").setComboEditor(egu.gridId,new IDropDownModel(Sql));
//		设置桶别下拉框
		ComboBox c2=new ComboBox();
		egu.getColumn("tongb").setEditor(c2);
		c2.setEditable(true);//由于itemsort中无厂别,暂时不能只显示限制电厂的桶别
		String sql ="select id, mingc from item where itemsortid=(select itemsortid from itemsort where bianm='TONGB') order by mingc";
		egu.getColumn("tongb").setComboEditor(egu.gridId, new IDropDownModel(sql));
//		设置工具栏盘点编码下拉框
		egu.addTbarText("盘点编码：");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		//设置联动
		String sql_com = "select mingc,beiz from item where itemsortid=(select itemsortid from itemsort where bianm='TONGB')";
		rsl = con.getResultSetList(sql_com);
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(i=0;i<rsl.getRows();i++){
			if(i==0){
				sb.append("new Array()");
			}else{
				sb.append(", new Array()");
			}
		}
		
		String arry = "var arr = new Array("+sb+");";
		i = 0;
		while(rsl.next()){
			arry+="	arr["+i+"][0] = '" + rsl.getString("mingc") + "';\n" +
				"	arr["+i+"][1] = '" + rsl.getString("beiz") + "';\n"; 
			i++;
		}	
		egu.addOtherScript(arry);
		
		String moni ="gridDiv_grid.on('afteredit',function(e){\n" +
					"var record = gridDiv_ds.getAt(e.row);\n" + 
				
					"var zongz = eval(record.get('ZONGZ')||0);\n" +
					"var piz = eval(record.get('PIZ')||0);\n" + 
					"\n" + 
					"var tongb = record.get('TONGB');\n" + 
					"\n" + 
					"if(e.field=='TONGB'){\n" +
					"\n" + 
					"\tfor(var i=0;i<arr.length;i++){\n" + 
					"\t\tif(tongb==arr[i][0]){\n" + 
					"\t\t\trecord.set('RONGJ',arr[i][1]);\n" + 
					"\t\t\tbreak;\n" + 
					"\t\t}\n" + 
					"\t}\n" + 
					"}\n" +
					"\n" + 
					"var rongj = Math.round(eval(record.get('RONGJ')||0)* Math.pow(10," + panmrjxsw + "))/Math.pow(10,"+ panmrjxsw + ");\n" + 
					"if(e.field=='ZONGZ'||e.field=='PIZ'){\n" + 
					"\tvar jingz=(zongz-piz);\n" + 
					"\trecord.set('JINGZ',jingz);\n" + 
					"}\n" + 
					"var jingz = eval(record.get('JINGZ')||0);\n" + 
					"\n" + 
					"if(e.field=='ZONGZ'||e.field=='PIZ'||e.field=='RONGJ'||e.field=='TONGB'){\n" + 
					"var mid = Math.round(((zongz-piz)/(rongj*1000/"+visit.getInt1()+")) * Math.pow(10, " + panmmdxsw + ")) / Math.pow(10,"+ panmmdxsw +");\n" + 
					"mid=mid.toFixed(7);\n" + 
					"record.set('MID',mid);\n" + 
					"}\n" + 
					"});";

	   egu.addOtherScript(moni);
		String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
			setCelModel(null);
			setCelValue(null);
			setZhi();
			
			visit.setInt1(1);
			visit.setString15("容积<br>(m3)");
			String flag = MainGlobal.getXitxx_item("盘点", "密度测量_单位是L", String.valueOf(visit.getDiancxxb_id()), "1");
			if(!flag.equals("1")){
				visit.setInt1(Integer.parseInt(flag));
				visit.setString15("容积<br>(L)");
			}
		}
		init();
	}
	private void init() {
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

}
