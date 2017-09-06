package com.zhiren.dc.diaoygl.fahhy;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Fahhy extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}
	
	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	
	private String Changebz;

	public String getChangebz() {
		return Changebz;
	}
	
	public void setChangebz(String Changebz) {
		this.Changebz = Changebz;
	}
	
	// 页面变化记录
	private String Changed;

	public String getChanged() {
		return Changed;
	}

	public void setChanged(String changed) {
		Changed = changed;
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}
	
	
	// 这个save方法是要处理第一个grid的添加以及修改和删除的信息，
	private void Save1(String change, Visit visit) {		
		String tableName = "meihyb";
		JDBCcon con = new JDBCcon();
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(change);
		StringBuffer sql = new StringBuffer("begin \n");
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id = ")
			   .append(delrsl.getString(0)).append(";\n");
			sql.append("delete from meihybtmp where meihyb_id = " + delrsl.getString(0)).append(";\n");
			sql.append("delete from rulmzlb where id = ").append(delrsl.getLong("rulmzlb_id")).append(";\n");
			
			// 判断入炉煤质量表是否已经存在化验数据，如果存在不能删除
			String sqlstr = "select qnet_ar\n" +
						 	"  from rulmzlb\n" + 
						 	" where id = " + delrsl.getLong("rulmzlb_id");
			ResultSet rs = con.getResultSet(sqlstr);
			try {
				if (rs.next()) {
					double qnet_ar = rs.getDouble("qnet_ar");
					if (qnet_ar != 0) {// 存在化验值
						setMsg("入炉化验值已经存在，不能进行删除操作！");
						// con.rollBack();
						return;
					}
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		sql.append("end;");
		if (sql.length() > 13) {
			con.getDelete(sql.toString());
		}
		sql.setLength(0);
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);
		sql = new StringBuffer("begin \n");
		while (mdrsl.next()) {
			String rulrq = DateUtil.FormatOracleDate(mdrsl.getString("rulrq"));
//			String ruld = mdrsl.getString("shangmjssj");
//			long bin2=Integer.parseInt(rulrq.substring(0, 10).replaceAll("-", ""));
			// Date d=new Date(bin2);
//			if (Integer.parseInt(ruld.substring(11, 13)) >= 13) {
//			}
//			
//			if ("".equals(mdrsl.getString("BIANM")) || mdrsl.getString("BIANM") == null) {
//				
//			} else {
//			}
			
			if ("0".equals(mdrsl.getString("ID"))) {
				String id = MainGlobal.getNewID(visit.getDiancxxb_id());
				String rulmzlb_id = MainGlobal.getNewID(visit.getDiancxxb_id());// 得到rulmzlb_id
				
				sql.append("insert into rulmzlb (id, diancxxb_id, fenxrq, rulbzb_id, jizfzb_id, rulrq, lursj, shenhzt) values (\n")
				   .append(rulmzlb_id).append(", ").append(visit.getDiancxxb_id()).append(", ").append(rulrq).append(", ")
				   .append(getExtGrid().getValueSql(getExtGrid().getColumn("bzmc"), mdrsl.getString("bzmc"))).append(", ")
				   .append(getExtGrid().getValueSql(getExtGrid().getColumn("jzmc"), mdrsl.getString("jzmc"))).append(", ")
				   .append(rulrq).append(", ").append(rulrq).append(", 0);\n");
				
				sql.append("insert into meihyb \n");
				sql.append("(ID, RULRQ, DIANCXXB_ID, RULMZLB_ID, RULBZB_ID, JIZFZB_ID, FADHY, GONGRHY, QITY, FEISCY, BEIZ, LURY, LURSJ, zhiyr, SHENHZT, BIANH)\n");
				sql.append("values (").append(id).append(", ").append(rulrq).append(", ");
				sql.append(visit.getDiancxxb_id()).append(", ").append(rulmzlb_id).append(", ");
				sql.append(getExtGrid().getValueSql(getExtGrid().getColumn("bzmc"), mdrsl.getString("bzmc"))).append(", ");
				sql.append(getExtGrid().getValueSql(getExtGrid().getColumn("jzmc"), mdrsl.getString("jzmc"))).append(", ");
				sql.append(mdrsl.getDouble("fadhy")).append(", ").append(mdrsl.getDouble("gongrhy")).append(", ");
				sql.append(mdrsl.getDouble("qity")).append(", ").append(mdrsl.getDouble("feiscy")).append(", '");
				sql.append(mdrsl.getString("BEIZ")).append("', '").append(mdrsl.getString("LURY"));
				sql.append("', ").append("SYSDATE, '").append(mdrsl.getString("zhiyr")).append("', ");
				sql.append(mdrsl.getString("SHENHZT")).append(", '").append(mdrsl.getString("BIANH")).append("');\n");
			} else {				
				sql.append("update rulmzlb set fenxrq = ").append(rulrq);
				sql.append(", rulbzb_id = ").append(getExtGrid().getValueSql(getExtGrid().getColumn("bzmc"), mdrsl.getString("bzmc")));
				sql.append(", jizfzb_id = ").append(getExtGrid().getValueSql(getExtGrid().getColumn("jzmc"), mdrsl.getString("jzmc")));
				sql.append(", rulrq = ").append(rulrq).append(", lursj = ").append(rulrq).append(", ").append("shenhzt = 0").append(" where id = ");
				sql.append(mdrsl.getString("rulmzlb_id")).append(";\n");
				
				sql.append("update meihyb set RULRQ = to_date('").append(mdrsl.getString("RULRQ")).append("', 'yyyy-mm-dd'), DIANCXXB_ID = ");
				sql.append(visit.getDiancxxb_id()).append(", RULMZLB_ID = ").append(mdrsl.getString("RULMZLB_ID")).append(", RULBZB_ID = ");
				sql.append(getExtGrid().getValueSql(getExtGrid().getColumn("bzmc"), mdrsl.getString("bzmc"))).append(", JIZFZB_ID = ");
				sql.append(getExtGrid().getValueSql(getExtGrid().getColumn("jzmc"), mdrsl.getString("jzmc"))).append(", FADHY = ");
				sql.append(mdrsl.getString("FADHY")).append(", GONGRHY = ").append(mdrsl.getString("GONGRHY")).append(", QITY = ");
				sql.append(mdrsl.getString("QITY")).append(", FEISCY = ").append(mdrsl.getString("FEISCY")).append(", BEIZ = '");
				sql.append(mdrsl.getString("BEIZ")).append("', LURY = '").append(mdrsl.getString("LURY")).append("', LURSJ = to_date('");
				sql.append(mdrsl.getString("LURSJ")).append("', 'yyyy-mm-dd'), SHENHZT = ").append(mdrsl.getString("SHENHZT"));
				sql.append(", BIANH = '").append(mdrsl.getString("BIANH"));
				sql.append("' where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sql.append("end;");
		if (sql.length() > 13) {
			con.getUpdate(sql.toString());
		}
		sql.setLength(0);
		
		con.Close();
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
			return "to_date('" + value + "', 'yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "0" : value;
		} else {
			return value;
		}
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Save1Chick = false;
	
	public void Save1Button(IRequestCycle cycle) {
		_Save1Chick = true;
	}
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _TijChick = false;
	
	public void TijButton(IRequestCycle cycle) {
		_TijChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Save1Chick) {
			_Save1Chick = false;
			Save1();
		}
		if(_RefurbishChick){
			_RefurbishChick = false;
		}
		if(_TijChick){
			_TijChick = false;
			Tij();
		}
		getSelectData();
	}
	
	private void Tij() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sqlstr = 
			"SELECT TMP.*\n" +
			"  FROM MEIHYB HY, MEIHYBTMP TMP\n" + 
			" WHERE HY.ID = TMP.MEIHYB_ID\n";
		ResultSetList rsl = con.getResultSetList(sqlstr);
		if (!rsl.next()) {
			setMsg("请先录入数据！");
			return;
		}
		
		StringBuffer sql = new StringBuffer("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update meihyb set zhuangt = 1 where id = ").append(mdrsl.getLong("id")).append(";");
		}
		sql.append("end;");
		if (sql.length() > 13) {
			con.getUpdate(sql.toString());
		}
		
		rsl.close();
		mdrsl.close();
		con.Close();
	}
	
	// 更新煤场库存信息
	private void updateMeicxx(Visit visit, JDBCcon con) {
		ResultSetList list = null;
		ResultSetList r = null;
		String sql = "";
		sql = "SELECT * FROM MEICDYB WHERE RIQ = TO_DATE('" + getRiq() + "', 'yyyy-mm-dd')";
		list = con.getResultSetList(sql);
		if (!list.next()) {
			sql = "INSERT INTO MEICDYB (ID, RIQ, MEICB_ID, LAIMSL, HAOMSL, CUNML, TIAOZL)\n" +
				  "(SELECT GETNEWID(" + visit.getDiancxxb_id() + "), " +
				  "        TO_DATE('" + getRiq() + "', 'yyyy-mm-dd'), ID, 0, 0, 0, 0 " +
			      "   FROM MEICB)";
			con.getInsert(sql);
		}
			
		sql = 
			"SELECT MC.ID MEICB_ID,\n" +
			"       NVL(LAIMSL, 0) LAIMSL,\n" + 
			"       NVL(HAOMSL, 0) HAOMSL,\n" + 
			"       NVL(CUNML, 0) CUNML,\n" + 
			"       NVL(TIAOZL, 0) TIAOZL\n" + 
			"  FROM (SELECT MEICB_ID, LAIMSL, HAOMSL, CUNML, TIAOZL\n" + 
			"          FROM MEICDYB DY\n" + 
			"         WHERE RIQ = TO_DATE('" + getRiq() + "', 'yyyy-mm-dd') - 1) DY,\n" + 
			"       MEICB MC\n" + 
			" WHERE DY.MEICB_ID(+) = MC.ID";
		list = con.getResultSetList(sql);
		while (list.next()) {
			sql = "SELECT SUM(TMP.FADHY + TMP.GONGRHY + TMP.FEISCY + TMP.QITY) HAOMSL" +
				  "  FROM MEIHYB HY, MEIHYBTMP TMP\n" +
				  " WHERE TMP.MEIHYB_ID = HY.ID " +
				  "   AND TMP.MEICB_ID = " + list.getLong("meicb_id") + 
				  "   AND HY.RULRQ = TO_DATE('" + getRiq() + "', 'yyyy-mm-dd')";
			r = con.getResultSetList(sql);
			double haomsl = 0;
			if (r.next()) {
				haomsl = r.getDouble("haomsl");
			}
			sql = "UPDATE MEICDYB SET HAOMSL = " + haomsl + ",\n" +
				  "CUNML = -" + haomsl + " + LAIMSL + " + list.getDouble("cunml") + " + " + list.getDouble("tiaozl") + "\n" +
				  "WHERE MEICB_ID = " + list.getLong("meicb_id") + " AND RIQ = TO_DATE('" + getRiq() + "', 'yyyy-mm-dd')";
			con.getUpdate(sql);
		}
	}
	
	private void Save1() {
		// 保存第二组信息，会有第一组的id，
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon(); 
        StringBuffer sb = new StringBuffer();
		ResultSetList mdrsl = visit.getExtGrid2().getModifyResultSet(getChange());
		sb.append("begin \n");
		
		while (mdrsl.next()) {
			String zzb = MainGlobal.getNewID(visit.getDiancxxb_id());
			String sql = "select * from fahhybzb where fahb_id="+mdrsl.getString("fahb_id")+" and riq=to_Date('','') and banzm='"+getChangebz()+"'";
			ResultSetList rs = con.getResultSetList(sql);
			if (rs.next()) {
				// 新增加的信息
				sb.append(" update fahhybzb set haoyl+").append(mdrsl.getString("haoyl")).append(" where id=").append(rs.getString("id")).append(";\n");
			} else {
				sb.append("insert into fahhybzb ")
			    .append(" (id, riq,fahb_id,banzm,haoyl)")
			    .append(" values (" + zzb + ",to_date('")
			    .append(getRiq() + "','yyyy-mm-dd'),")
			    .append(mdrsl.getString("fahb_id")).append(",'")
			    .append(getChangebz() + "', ")
			    .append(mdrsl.getString("haoyl")+");\n");
			}
			
			sb.append("update fahhyb set yue = ").append(mdrsl.getString("yue")).append(" where id=").append(mdrsl.getString("id")).append(";\n");
		}
		
		sb.append("end;\n");
		if (sb.length() > 13) {
			con.getUpdate(sb.toString());
		}
		sb.setLength(0);
		
		mdrsl.close();
		con.Close();
	}
	
	private void Create(String riq){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String banzsql = "select mingc from rulbzb where mingc <>'全厂' order by xuh";
		ResultSetList rs = con.getResultSetList(banzsql);
		String sql = "begin \n";
		while(rs.next()){
			sql += "insert into fahhybzb (id,riq,banzm,haoyl) values (getnewid("+visit.getDiancxxb_id()+"),to_date('"+riq+"','yyyy-mm-dd'),'"+rs.getString("mingc")+"',0);\n"; 
		}
		sql += "end;\n";
		int flag = con.getUpdate(sql);
		if(flag==-1){
			this.setMsg("生成失败!");
		}
		getSelectData();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
        String sql= 
        	"SELECT r.mingc as banzm,nvl(h.haoy,0) haoy FROM\n" +
        	"(select banzm,sum(haoyl) haoy from FAHHYBZB f\n" + 
        	"WHERE riq = to_Date('"+getRiq()+"','yyyy-mm-dd')\n" + 
        	"group by banzm) h,(SELECT * FROM rulbzb r WHERE  r.mingc<>'全厂')r\n" + 
        	"WHERE h.banzm(+) = r.mingc ORDER BY r.xuh";

		ResultSetList rsl1 = con.getResultSetList(sql);
//		String id = "";
		if (rsl1.next()) {
			
		}else{
			sql = "SELECT mingc as banzm,0 AS haoy FROM rulbzb WHERE mingc<>'全厂' ORDER BY xuh"; 
		}
		rsl1 = con.getResultSetList(sql);
        // 记录记录集的条数
//		int jil = 0;
//		if (rsl1.getRows() > 0) {
//			jil = rsl1.getRows();
//		}
		// 查出数据后进行记录集重置
//		rsl1.beforefirst();
		
//		if (rsl1 == null) {
//			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL：" + sql);
//			setMsg(ErrorMessage.NullResult);
//			return;
//		}
		        
		String sql1 = 
			"SELECT h.id,h.fahb_id,g.mingc gys,m.mingc mk,f.daohrq,h.jingz,h.yue ye,h.yue,'' haoyl\n" +
			"FROM fahhyb h,fahb f,meikxxb m,gongysb g\n" + 
			"WHERE f.id = h.fahb_id AND f.meikxxb_id = m.id\n" + 
			"AND f.gongysb_id = g.id and h.yue<>0"+
			"ORDER BY f.gongysb_id,f.meikxxb_id,f.daohrq desc";

		// System.out.println(chaxun);
		
		ResultSetList rsl2 = con.getResultSetList(sql1);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl1);
		
//		if (rsl2.getRows() > 0) {
//			egu.setGridType(ExtGridUtil.Gridstyle_Read);
//		} else {
//			// 设置GRID是否可以编辑
//			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDiv1", rsl2);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置每页显示行数
		egu.addPaging(25);
		
		egu.getColumn("banzm").setHeader("班组");//Center
		egu.getColumn("banzm").setWidth(70);
		
		egu.getColumn("haoy").setHeader("耗用量");
		egu.getColumn("haoy").editor = null;
				
		egu.addTbarText("耗用日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("RIQ", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// 设置分隔符
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addTbarText("-");
		
		egu.setDefaultsortable(false);
		
		setExtGrid(egu);
		// 设置GRID是否可以编辑
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu1.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置每页显示行数
		egu1.addPaging(25);
		egu1.setWidth("bodyWidth");
		egu1.getColumn("fahb_id").setHidden(true);
		egu1.getColumn("gys").setHeader("供应商");
		egu1.getColumn("gys").editor = null;
		egu1.getColumn("mk").setHeader("煤矿");
		egu1.getColumn("mk").editor = null;
		egu1.getColumn("daohrq").setHeader("到货日期");
		egu1.getColumn("daohrq").editor = null;
		egu1.getColumn("jingz").setHeader("来煤数量");
		egu1.getColumn("jingz").editor = null;
		egu1.getColumn("ye").setHidden(true);
		egu1.getColumn("ye").editor = null;
		egu1.getColumn("yue").setHeader("余额");
		egu1.getColumn("yue").editor = null;
		egu1.getColumn("haoyl").setHeader("耗用量");
		
		String condition = 
			  "var Mrcdd = gridDiv1_ds.getModifiedRecords(); " +
			  "var rec = gridDiv_sm.getSelected(); "+
			  "if (rec != null) {" +
//			  "    var id = rec.get('ID');" +
			  "	   var rq = rec.get('BANZM');"+
//			+ "    var Cobjid = document.getElementById('CHANGEID');" +
			  "	   var Cobjbz = document.getElementById('CHANGEBZ');" +
//			+ "    Cobjid.value = id; " +
			  "	   Cobjbz.value = rq; " +
//			  "    if (id == 0 || id == '0') {" +
//			  "	   	   Ext.MessageBox.alert('提示信息','请先保存第一组添加的记录'); " +
//			  "        return; " +
//			  "	   }" +
			  "}"+
			  "if (rec == null) {"+
			  "    Ext.MessageBox.alert('提示信息','请选择一条第一组信息记录'); " +
			  "    return ;"+
			  "}";
		egu1.addToolbarButton(GridButton.ButtonType_Save_condition, "Save1Button", condition);
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv1_grid.on('afteredit',function(e){ ");
			//sb.append("e.record.set('DAOHLDY',parseFloat(e.record.get('SHIDLDY')==''?0:e.record.get('SHIDLDY'))/parseFloat(e.record.get('JIHLDY')==''?0:e.record.get('JIHLDY'))*100);");
			sb.append("e.record.set('YUE',eval(e.record.get('YE')||0)-eval(e.record.get('HAOYL')||0));");
			sb.append("if (e.record.get('YUE')<0){Ext.MessageBox.alert('提示信息','耗用不可以比余额大！');}");
		sb.append("});");
		
		egu1.addOtherScript(sb.toString());
		
//		sb.append("gridDiv_grid.on('beforeedit',function(e){");
//		sb.append("if(e.record.get('DIANCXXB_ID')=='合计'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
//		sb.append("});");
		
		egu1.defaultsortable = false;
		setExtGrid1(egu1);
		con.Close();
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
		// 导航栏树的查询SQL
		String sql = 
			"select 0 id, '根' as mingc, 1 jib, -1 fuid, 0 checked from dual\n" +
			"union\n" + 
			"select id, mingc, 2 jib, 0 fuid, 0 checked\n" + 
			"  from meicangb \n" + 
			" where diancxxb_id = " + visit.getDiancxxb_id() + "\n";
			
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}
		
	public ExtGridUtil getExtGrid1() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid1(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}
	
	public String getGridScript1() {
		if (getExtGrid1() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid1().addToolbarItem("'->'");
			getExtGrid1().addToolbarItem("'<marquee width=300 scrollamount=2>" + getTbmsg() + "</marquee>'");
		}
		return getExtGrid1().getGridScript();
	}
	
	public String getGridHtml1() {
		if (getExtGrid1() == null) {
			return "";
		}
		return getExtGrid1().getHtml();
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>" + getTbmsg() + "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	boolean riqichange = false;
	// 绑定日期
	
	private String riq;
	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setRiq(String riq) {
		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqichange = true;
		}
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
			setTbmsg(null);
			visit.setString1(null);
			setRiq(DateUtil.FormatDate(new Date()));
		}
		
		if (visit.getboolean1()) {
			visit.setboolean1(false);
			visit.setList1(null);
			visit.setString1(null);
		}
		
		if (visit.getboolean2()) {
			visit.setboolean2(false);
			visit.setString1(null);
		}
		
		getSelectData();
	}
}