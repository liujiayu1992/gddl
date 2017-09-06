package com.zhiren.jt.gongys;

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

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：赵胜男
 * 时间：2012-2-10
 * 描述: 修正新煤矿维护中不能保存多条记录
 * 		
 */
/*
 * 作者：夏峥
 * 时间：2012-03-20
 * 描述：使用String2记录当前界面
 */

/*
 * 作者：夏峥
 * 时间：2013-03-2
 * 描述：使用xl_meikxxb_id取得煤矿和供应商对应的ID，避免使用公共方法导致ID过大的问题
 */
/*
 * 作者：夏峥
 * 时间：2013-10-17
 * 描述：增加大同煤矿地区特殊保存设置
 */
/*
 * 作者：夏峥
 * 时间：2013-10-17
 * 描述：增加大同煤矿地区特殊保存设置
 */
/*
 * 作者：王耀霆
 * 时间：2014-03-03
 * 描述：修改无法启用和停用的BUG
 */

public class Meikxx_gd extends BasePage implements PageValidateListener {
//  系统日志表中的状态字段
//	private static final String ZhangTConstant1 = "成功";
//
//	private static final String ZhangTConstant2 = "失败";

	// 系统日志表中的类别字段
//	private static final String leiBConstant = "煤矿维护";
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	private String id;  //记录前台传递过来的id
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	private String SaveMsg;

	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = MainGlobal.getExtMessageBox(saveMsg, false);;
	}

	private boolean tiShi;// 给予提示信息是否显示的
	
	private boolean happenWrong; // 判断保存时是否有错误数据，true为是，false为否

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}
	
	private String Meikmc; // 保存页面上的煤矿名称
	
	public String getMeikmc() {
		return Meikmc;
	}

	public void setMeikmc(String meikmc) {
		Meikmc = meikmc;
	}
	
	private String DataSource;

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}
	
	private String wrongDataSource; // 保存时将有错误的数据拼成可以生成Record的字符串后，保存到该变量中。
	
	public String getWrongDataSource() {
		return wrongDataSource;
	}

	public void setWrongDataSource(String wrongDataSource) {
		this.wrongDataSource = wrongDataSource;
	}

	// 有可能从返回按钮返回本页面，也可能是从添加所选按钮返回本页面，标记作用
	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}
	
//	省份IDropDownModel
	public IPropertySelectionModel getShengfModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setShengfModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setShengfModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setShengfModels() {
		String sql = "select sf.id, sf.quanc from shengfb sf";
		setShengfModel(new IDropDownModel(sql));
	}

//	城市IDropDownModel
	public IPropertySelectionModel getChengsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChengsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChengsModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setChengsModels() {
		String sql = "select cs.id, cs.quanc from chengsb cs";
		setChengsModel(new IDropDownModel(sql));
	}

	private void gotochez(IRequestCycle cycle) {
		// 需要传给下个页面取值
		if (getChange() == null || "".equals(getChange())) {
			setMsg("请选中一个人员设置分组!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		visit.setString2(visit.getActivePageName().toString());
		cycle.activate("Chezgl");
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		SaveMsg = "";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next())	{
//			判断编码是否符合标准标准
			String SaveMsgLocal = "";
			ResultSetList weis = con.getResultSetList("select zhi from xitxxb where mingc='煤矿编码位数' and zhuangt=1");
			if(weis.next()){
				if(mdrsl.getString("BIANM").length()<weis.getInt("zhi")){
					SaveMsgLocal="煤矿编码位数小于"+weis.getInt("zhi")+",请按要求重新编码。";
					setSaveMsg(SaveMsgLocal);
					return;
				}
			}
//			初始化并赋值保存时所需变量
			String id="";
			ResultSetList rs = con.getResultSetList("select xl_meikxxb_id.nextval id from dual");
			if(rs.next()) {
				id = rs.getString(0);
			}
			
			String meikxxb_id=visit.getDiancxxb_id()+id;
			String gongysb_id = meikxxb_id;
			
//			String meikxxb_id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
//			String gongysb_id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			
			String xuh=mdrsl.getString("XUH");
			long MEIKDQ_ID = (getExtGrid().getColumn("MEIKDQ_ID").combo).getBeanId(mdrsl.getString("MEIKDQ_ID"));
			String BIANM=mdrsl.getString("BIANM");
			String MINGC=mdrsl.getString("MINGC");
			String QUANC=mdrsl.getString("QUANC");
			String PINY=mdrsl.getString("PINY");
//			如果省份为空，则返回0
			long SHENGFB_ID =0;
			if(mdrsl.getString("SHENGFB_ID") == null|| mdrsl.getString("SHENGFB_ID").equals("")){
				SHENGFB_ID=0;
			}else{
				SHENGFB_ID=((IDropDownModel)getShengfModel()).getBeanId(mdrsl.getString("SHENGFB_ID"));
			}
//			如果城市为空，则返回0
			long CHENGSB_ID=0;
			if(mdrsl.getString("CHENGSB_ID") == null|| mdrsl.getString("CHENGSB_ID").equals("")){
				CHENGSB_ID=0;
			}else{
				CHENGSB_ID =((IDropDownModel)getChengsModel()).getBeanId(mdrsl.getString("CHENGSB_ID")); 
			}
			String LEIB = mdrsl.getString("LEIB");
			String LEIX = mdrsl.getString("LEIX");
			long JIHKJB_ID = (getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(mdrsl.getString("JIHKJB_ID"));
			String beiz=mdrsl.getString("BEIZ");
			String SHANGJGSBM=mdrsl.getString("SHANGJGSBM");
			
			boolean Dtmktssz=MainGlobal.getXitxx_item("基础信息", "大同煤矿地区特殊设置", "0", "否").equals("是");
			
			String sql_check = "select id from meikxxb where (1=0 or bianm='"+BIANM+"' or mingc='"+MINGC+"' OR QUANC='"+QUANC+"')";
			
			if ("0".equals(mdrsl.getString("ID"))) {
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------记录---------<br>--编码:"
						+ BIANM + "<br>--名称:" + MINGC + "<br>--全称:" + QUANC
						+ "<br>的记录有重复,不能保存!";
					continue;
				}
				sql.append("begin \n");
//				插入到供应商表中
				sql.append("INSERT INTO GONGYSB\n" );
				sql.append("  (ID, FUID, XUH, MINGC, QUANC, PINY, BIANM, SHANGJGSBM,SHENGFB_ID, beiz,LEIX, ZHUANGT)\n");
				sql.append("VALUES\n");
				sql.append("  ("+gongysb_id+","+MEIKDQ_ID+",'"+xuh+"','"+MINGC+"','"+QUANC+"','"+PINY+"','"+BIANM+"'," +
						   "	'"+SHANGJGSBM+"',"+SHENGFB_ID+",'"+beiz+"',0,1);\n");
//				插入到煤款信息表中
				sql.append("INSERT INTO MEIKXXB\n" );
				sql.append("  (ID,XUH,BIANM,MINGC,QUANC,PINY,SHENGFB_ID,JIHKJB_ID,CHENGSB_ID,LEIX,LEIB,\n" );
				sql.append("   SHANGJGSBM,MEIKDQ_ID,beiz,SHIYZT)\n" ); 
				sql.append("VALUES\n" );
				sql.append("   ("+meikxxb_id+","+xuh+",'"+BIANM+"','"+MINGC+"','"+QUANC+"','"+PINY+"'" +
						   ","+SHENGFB_ID+","+JIHKJB_ID+","+CHENGSB_ID+",'"+LEIX+"','"+LEIB+"','"+SHANGJGSBM+"',"+gongysb_id+",'"+beiz+"',1);\n");
				if(Dtmktssz){
					sql.append("UPDATE MEIKXXB SET MEIKDQ2_ID="+MEIKDQ_ID+" WHERE ID="+meikxxb_id+"; \n" );
				}
				sql.append("end;" );
			}else{
				sql_check+="and id<>"+mdrsl.getString("ID");
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------记录---------<br>--编码:"
						+ BIANM + "<br>--名称:" + MINGC + "<br>--全称:" + QUANC
						+ "<br>的记录有重复,不能保存!";
					continue;
				}
				sql.append("begin \n");
//				更新供应商表
				sql.append("UPDATE GONGYSB\n" );
				sql.append("   SET FUID       = "+MEIKDQ_ID+",\n" ); 
				sql.append("       XUH        = '"+xuh+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" );
				sql.append("       PINY       = '"+PINY+"',\n" );
				sql.append("       BIANM      = '"+BIANM+"',\n" );
				sql.append("       SHANGJGSBM = '"+SHANGJGSBM+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+",\n" );
				sql.append("       beiz = '"+beiz+"'\n" );
				sql.append(" WHERE ID = (SELECT MEIKDQ_ID FROM meikxxb WHERE ID="+mdrsl.getString("ID")+");\n");
//				更新煤款信息表
				sql.append("UPDATE MEIKXXB\n" );
				sql.append("   SET XUH        = '"+xuh+"',\n" );
				sql.append("       BIANM      = '"+BIANM+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" ); 
				sql.append("       PINY       = '"+PINY+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+",\n" );
				sql.append("       JIHKJB_ID  = "+JIHKJB_ID+",\n" );
				sql.append("       LEIX       = '"+LEIX+"',\n" );
				sql.append("       LEIB       = '"+LEIB+"',\n" );
				sql.append("       beiz = '"+beiz+"',\n" );
				sql.append("       CHENGSB_ID = "+CHENGSB_ID+",\n" );
				sql.append("       SHANGJGSBM = '"+SHANGJGSBM+"'\n" );
				sql.append("    WHERE ID="+mdrsl.getString("ID")+";\n");
				if(Dtmktssz){
					sql.append("UPDATE MEIKXXB SET MEIKDQ2_ID="+MEIKDQ_ID+" WHERE ID="+mdrsl.getString("ID")+"; \n" );
				}
				sql.append("end;" );
			}
			int flag=con.getUpdate(sql.toString());
			sql.delete(0, sql.length());
			if(flag==-1){
				SaveMsg += "----------记录---------<br>--编码:"
					+ BIANM + "<br>--名称:" + MINGC + "<br>--全称:" + QUANC
					+ "<br>保存失败!";
			}
		}
		
		if(SaveMsg.length()>10){
			setSaveMsg(SaveMsg);
		}else{
			setSaveMsg("保存成功");
		}
	}

//	// 判断数据是否在本地库中已经存在(不存在返回0，存在返回行数)
//	private int Shujpd(JDBCcon con, String sql) {
//		return JDBCcon.getRow(con.getResultSet(sql));
//	}
//
//	// 日志记录
//	private String logMsg = "";
//
//	private String zhuangT = "";

//	private void WriteLog(JDBCcon con) {
//
//		Visit visit = (Visit) this.getPage().getVisit();
//
//		if (!logMsg.equals("")) {// 不为空，需要写入日志记录
//
//			Date date = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String date_str = sdf.format(date);
//			String sql = " insert into xitrzb(id,diancxxb_id,yonghm,leib,shij,zhuangt,beiz) values("
//					+ " getnewid("
//					+ visit.getDiancxxb_id()
//					+ "),"
//					+ visit.getDiancxxb_id()
//					+ ",'"
//					+ visit.getRenymc()
//					+ "','"
//					+ leiBConstant
//					+ "',to_date('"
//					+ date_str
//					+ "','YYYY-MM-DD,HH24:mi:ss'),'"
//					+ this.zhuangT
//					+ "','"
//					+ logMsg + "')";
//
//			con.getInsert(sql);
//			logMsg = "";
//		}
//	}

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
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
			// return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	
	private boolean _BeginChick=false;
	
	public void BeginButtonQY(IRequestCycle cycle){
		_BeginChick = true;
	}
	
	private boolean _StopChick = false;
	
	public void StopButtonTY(IRequestCycle cycle){
		_StopChick = true;
	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}
	
//	"关联供应商"按钮
	private boolean _GuanlgysClick = false;
	
	public void GuanlgysButton(IRequestCycle cycle) {
		_GuanlgysClick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	public String getmeikmc() {
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString3();
	}

	public void setmeikmc(String meikmc) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString3(meikmc);
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			ToAddMsg = "";
		}
		if (_InsertChick) {
			_InsertChick = false;
		}
		if(_BeginChick){
			_BeginChick = false;
			Begin();
			getSelectData();
		}
		if(_StopChick){
			_StopChick = false;
			Stop();
			getSelectData();
		}
		if (_RbChick) {
			_RbChick = false;
			gotochez(cycle);
		}
		if (_GuanlgysClick) {
			_GuanlgysClick = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString2(visit.getActivePageName().toString());//使用String2记录当前界面
			visit.setString9(getParameters()); // 将煤矿id传到下个页面
			visit.setString10(getMeikmc()); // 将煤矿名称传到下个页面
			cycle.activate("Meikglgys");
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			ToAddMsg = "";
		}
	}

	public void getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str1 ="";
		if(getShiyztValue().getId() == 1){
			str1="and m.shiyzt = 1 \n";
		}else{
			str1="and m.shiyzt = 0 \n";
		}
		
		String condition="";
		if(getmeikmc()!=null && !getmeikmc().equals("")){
			condition=" and (m.mingc like '%"+getmeikmc()+"%' or m.quanc like '%"+getmeikmc()+"%')\n";
		}
		
		String sql = "SELECT DISTINCT M.ID,\n" +
			"                M.XUH,\n" + 
			"                G.DQMC       MEIKDQ_ID,\n" + 
			"                M.BIANM,\n" + 
			"                M.MINGC,\n" + 
			"                M.QUANC,\n" + 
			"                M.PINY,\n" + 
			"                G.SMC        AS SHENGFB_ID,\n" + 
			"                C.QUANC      AS CHENGSB_ID,\n" + 
			"                M.LEIB,\n" + 
			"                M.LEIX,\n" + 
			"                J.MINGC      AS JIHKJB_ID,\n" + 
			"                M.BEIZ,\n" + 
			"                M.SHANGJGSBM\n" + 
			"  FROM MEIKXXB M, JIHKJB J, CHENGSB C, VWGONGYSDQ G\n" + 
			" WHERE M.ID = G.MK_ID(+)\n" + 
			"   AND M.CHENGSB_ID = C.ID(+)\n" + 
			"   AND M.JIHKJB_ID = J.ID(+)\n" + 
			str1 +
			condition+
			" ORDER BY M.XUH";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("meikxxb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(95);
		egu.getColumn("meikdq_id").setHeader("煤矿地区");
		egu.getColumn("meikdq_id").setWidth(130);
		egu.getColumn("bianm").editor.allowBlank = false;
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(220);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(80);

		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("shengfb_id").setEditor(null);
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(80);
		egu.getColumn("shangjgsbm").setHeader("上级公司编码");
		egu.getColumn("shangjgsbm").setWidth(80);
		egu.getColumn("shangjgsbm").setHidden(true);
		egu.getColumn("chengsb_id").setHeader("城市");
		egu.getColumn("chengsb_id").setWidth(80);
		egu.getColumn("chengsb_id").setEditor(null);
		
		ComboBox combMeikdq = new ComboBox();
		egu.getColumn("meikdq_id").setEditor(combMeikdq);
//		下拉框中的内容可编辑
		combMeikdq.setEditable(true);
//		重写Combobox的过滤方法 ：使之可以模糊匹配输入的字符
		combMeikdq.setListeners("beforequery:function(e){" +
		                "var combo = e.combo;" +
		                "if(!e.forceAll){" +
		                "var value = e.query;" +
		                "combo.store.filterBy(function(record,id){" +
		                "var text = record.get(combo.displayField);" +
		                "" +
		                "return (text.indexOf(value)!=-1);" +
		                "});" +
		                "combo.expand();" +
		                "return false; " +
		                " } " +
		                "}");
		String Meikdqsql = "select id, mingc from meikdqb where zhuangt=1 order by xuh";
		egu.getColumn("meikdq_id").setComboEditor(egu.gridId,new IDropDownModel(Meikdqsql));
		egu.getColumn("meikdq_id").editor.allowBlank = true;
		
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from jihkjb "));
		
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "统配"));
		l.add(new IDropDownBean(2, "地方"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("leib").setReturnId(false);
		egu.getColumn("leib").setDefaultValue("统配");

		List k = new ArrayList();
		k.add(new IDropDownBean(1, "煤"));
		k.add(new IDropDownBean(2, "油"));
		k.add(new IDropDownBean(3, "油"));
		egu.getColumn("leix").setEditor(new ComboBox());
		egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(k));
		egu.getColumn("leix").setReturnId(false);
		egu.getColumn("leix").setDefaultValue("煤");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		String meikhangs="select zhi from xitxxb where mingc='煤矿维护页面单页显示的行数' and zhuangt=1";
		rsl = con.getResultSetList(meikhangs);
		if(rsl.next()){
			String zhi=rsl.getString("zhi");
			egu.addPaging(Integer.parseInt(zhi));
		}else{//默认每页显示25行
			egu.addPaging(25);
		}
		egu.addTbarText("使用状态:");
		ComboBox cb = new ComboBox();
		cb.setTransform("SHIYZT");
		cb.setWidth(80);
		cb.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(cb.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("煤矿名称:");
		TextField tf = new TextField();
		tf.setId("meikmc");
		if(condition.length()>1){
			tf.setValue(getmeikmc());
		}
		egu.addToolbarItem(tf.getScript());
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('meikmc').value=meikmc.getValue(); document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		if(getShiyztValue().getId() == 0){
			egu.addTbarText("-");
			egu.addTbarBtn(new GridButton("启用","function(){\n"+
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('提示信息','请先保存记录');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.BeginButtonQY.click();" +
						"}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('提示信息','请先选择记录');\n"+
						"}}"));
		}else{
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("停用","function(){" +
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('提示信息','请先保存记录');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.StopButtonTY.click();}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('提示信息','请先选择记录');\n"+
						"	}}"));
		}

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		String sPowerHandler = "function(){"
				+ "if(gridDiv_sm.getSelected()== null){"
				+ "	Ext.MessageBox.alert('提示信息','请选中一个煤矿再设置车站');" + "	return;"
				+ "}" + "var grid_rcd = gridDiv_sm.getSelected();"
				+ "if(grid_rcd.get('ID') == '0'){"
				+ "	Ext.MessageBox.alert('提示信息','在设置车站之前请先保存!');" + "	return;"
				+ "}" + "grid_history = grid_rcd.get('ID');"
				+ "var Cobj = document.getElementById('CHANGE');"
				+ "Cobj.value = grid_history;"
				+ "document.getElementById('RbButton').click();" + "}";
		egu.addTbarBtn(new GridButton("设置车站", sPowerHandler));
		
		egu.addTbarText("-");
		String guanlgys_click = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelected()== null){\n" + 
			"        Ext.MessageBox.alert('提示信息','请选中一个煤矿再关联供应商');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var rec = gridDiv_sm.getSelected();\n" + 
			"    if(rec.get('ID') == '0'){\n" + 
			"        Ext.MessageBox.alert('提示信息','在关联供应商之前请先保存!');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var cobjid = document.getElementById('PARAMETERS')\n" + 
			"    cobjid.value = rec.get('ID');\n" +
			"    var meikmc = document.getElementById('Meikmc')\n" + 
			"    meikmc.value = rec.get('MINGC');\n" + 
			"    document.getElementById('GuanlgysButton').click();\n" + 
			"}";
		egu.addTbarBtn(new GridButton("关联供应商", guanlgys_click));
		
		// 从添加所选按钮回来给予的提示信息，
		if (ToAddMsg.equals("toAdd")) {
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("var p=new gridDiv_plant("+ recs[i] +");\n gridDiv_ds.insert("+ i +",p);\n");
				sb.append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
		}
		
//		判断是否有错误数据，如果有显示在页面上
		if (happenWrong) {
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getWrongDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("var p=new gridDiv_plant("+ recs[i] +");\n gridDiv_ds.insert("+ i +",p);\n");
				sb.append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
			happenWrong = false;
		}
		
		String shengf_click = 
			"gridDiv_grid.on('cellclick',\n" +
			"function(own, irow, icol, e){\n" + 
			"    row = irow;\n" + 
			"    if('SHENGFB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){\n" + 
			"        shengfTree_window.show();\n" + 
			"    }\n" + 
			"});";
		egu.addOtherScript(shengf_click);
		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");

		if (tiShi) {
			tiShi = false;
			SaveMsg = "Ext.Msg.alert('提示信息',\"" + SaveMsg + "\");";
		} 
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_sf_cs, "shengfTree", ""+visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		String handler = 
			"function() {\n" +
			"    var cks = shengfTree_treePanel.getSelectionModel().getSelectedNode();\n" + 
			"    if(cks==null){\n" + 
			"        shengfTree_window.hide();\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    rec = gridDiv_grid.getSelectionModel().getSelected();\n" + 
			"    if(cks.getDepth() == 2){\n" + 
			"        rec.set('SHENGFB_ID', cks.parentNode.text);\n" + 
			"        rec.set('CHENGSB_ID', cks.text);\n" + 
			"    }else if(cks.getDepth() == 1){\n" + 
			"        rec.set('SHENGFB_ID', cks.text);\n" +
			"        rec.set('CHENGSB_ID', '');\n" + 
			"    }\n" + 
			"    shengfTree_window.hide();\n" + 
			"    return;\n" + 
			"}";
		
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);

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
	
	public String getTreeScript() {
//		System.out.print(((Visit)this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDefaultTree(null);
			setShengfModel(null);
			setShengfModels();
			setChengsModel(null);
			setChengsModels();
			setShiyztValue(null);
			setShiyztModel(null);

			ToAddMsg = cycle.getRequestContext().getRequest().getParameter("MsgAdd");

			if (ToAddMsg == null) {
				ToAddMsg = "";
			}

			DataSource = visit.getString13();
			getSelectData();
		} else {
			getSelectData();
		}
	}
		
		//使用下拉框
		public IDropDownBean getShiyztValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getShiyztModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setShiyztValue(IDropDownBean value) {

			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}

		public void setShiyztModel(IPropertySelectionModel value) {

			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getShiyztModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getShiyztModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public IPropertySelectionModel getShiyztModels() {
			List shiyzt = new ArrayList();
			shiyzt.add(new IDropDownBean(1, "使用中"));
			shiyzt.add(new IDropDownBean(0, "未使用"));
			((Visit) getPage().getVisit())
					.setProSelectionModel5(new IDropDownModel(shiyzt));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}
		
	//启用的方法	
		public void Begin(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "begin \n update meikxxb set shiyzt = 1 where id ="+getChange()+";\n";
			sql+="UPDATE (SELECT ZHUANGT\n" +
				"          FROM GONGYSB\n" + 
				"         WHERE ID =\n" + 
				"               (SELECT MEIKXXB.MEIKDQ_ID FROM MEIKXXB WHERE ID = "+getChange()+"))\n" + 
				"   SET ZHUANGT = 0; \n end;";
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("启用成功!");
				}
				else{
					setSaveMsg("启用失败!");
				}
		}
	//停用的方法	
		public void Stop(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "begin \n update meikxxb set shiyzt = 0 where id ="+getChange()+";\n";
			sql+="UPDATE (SELECT ZHUANGT\n" +
				"          FROM GONGYSB\n" + 
				"         WHERE ID =\n" + 
				"               (SELECT MEIKXXB.MEIKDQ_ID FROM MEIKXXB WHERE ID = "+getChange()+"))\n" + 
				"   SET ZHUANGT = 0; \n end;";
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("停用成功!");
				}
				else{
					setSaveMsg("停用失败!");
				}
		}
		
	}

