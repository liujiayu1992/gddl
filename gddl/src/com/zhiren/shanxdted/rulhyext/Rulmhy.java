package com.zhiren.shanxdted.rulhyext;

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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rulmhy extends BasePage implements PageValidateListener {
//	客户端的消息框
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
//	页面初始化(每次刷新都执行)
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	// 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	 日期控件
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			JDBCcon con = new JDBCcon();
			int zhi=0;
			String sql = "select zhi from xitxxb where  leib='入炉' and mingc ='煤耗用默认日期' and zhuangt =1 ";
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
			   zhi=rsl.getInt("zhi");	
			}
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),+zhi,DateUtil.AddType_intDay));
			con.Close();
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {

		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);

		StringBuffer sb = new StringBuffer();
		StringBuffer sbdl = new StringBuffer();

		// 删除数据
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		//先检查判断删除入炉煤质量问题
		
		if (delrsl.getRows() != 0) {
			sbdl.append("begin\n");
			while (delrsl.next()) {

				sbdl.append(" delete from ").append("meihyb_zb").append(
						" where id =").append(delrsl.getString("id")).append(
						";	\n");

				sbdl.append(" delete from ").append("meihyb ").append(
						"WHERE diancxxb_id=").append(delrsl.getString("diancxxb_id"))
						.append(" AND  rulrq=to_date('" + delrsl.getString("rulrq") + "','yyyy-mm-dd')") 
						.append(";	\n");
			}
			sbdl.append("end;");
			con.getDelete(sbdl.toString());
			con.commit();
		}

		// 保存数据
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yundlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}

		if (rsl.getRows() == 0) {
			return;
		}
		String riq1 = getRiqi();
		long diancxxb_id = 0;

		sb.append("begin\n");
				
		while (rsl.next()) {
			long rulbzb_id = ((IDropDownModel) getRulbzbModel()).getBeanId(rsl
					.getString("rulbzb_id"));
			long jizfzb_id = ((IDropDownModel) getJizfzbModel()).getBeanId(rsl
					.getString("jizfzb_id"));
			diancxxb_id = Long.parseLong(getTreeid());//visit.getDiancxxb_id();
			String rulmzlb_id = MainGlobal.getNewID(diancxxb_id);// 得到rulmzlb_id

			
			if ("0".equals(rsl.getString("id"))) {
				
				sb.append("insert into meihyb_zb \n");
				sb
						.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
				sb.append("values (getnewid(").append(diancxxb_id).append(
						"),to_date('");
				sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
						.append(diancxxb_id).append(",");
				sb.append(rulmzlb_id).append(",").append(rulbzb_id).append(",")
						.append(jizfzb_id);
				sb.append(",").append(rsl.getString("FADHY")).append(",")
						.append(rsl.getString("GONGRHY"));
				sb.append(",").append(rsl.getString("QITY")).append(",")
						.append(rsl.getString("FEISCY"));
				sb.append(",'").append(rsl.getString("BEIZ")).append("','")
						.append(rsl.getString("LURY"));
				sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
						"','yyyy-mm-dd'),'").append(rsl.getString("zhiyr")).append("',");
				sb.append(rsl.getString("SHENHZT")).append("); \n");
				
				sb.append(" delete from ").append(" meihyb ").append(
				"WHERE diancxxb_id=").append(rsl.getString("diancxxb_id"))
				.append(" AND  rulrq=to_date('" + rsl.getString("rulrq") + "','yyyy-mm-dd')") 
				.append(";	\n");
				
				int rows = 0;	
				int irow = 0;
				double haoml = 0;
				double shengyhml=0;
				double zonghml = rsl.getDouble("FADHY") + rsl.getDouble("GONGRHY") + rsl.getDouble("QITY") + rsl.getDouble("FEISCY");
				ResultSetList rs = con.getResultSetList("SELECT * FROM rulmzlb WHERE rulrq=to_date('" + rsl.getString("RULRQ") + "','yyyy-mm-dd') AND diancxxb_id=" + diancxxb_id);				
				rows = rs.getRows();
				
				if (rows>0) {
					haoml = Math.round(zonghml/rows*100)/100;
					shengyhml = zonghml - haoml*(rows-1);
				} else {
					sb.append("insert into meihyb \n");
					sb
							.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
					sb.append("values (getnewid(").append(diancxxb_id).append(
							"),to_date('");
					sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
							.append(diancxxb_id).append(",");
					sb.append(rulmzlb_id).append(",").append(rulbzb_id).append(",")
							.append(jizfzb_id);
					sb.append(",").append(rsl.getString("FADHY")).append(",")
							.append(rsl.getString("GONGRHY"));
					sb.append(",").append(rsl.getString("QITY")).append(",")
							.append(rsl.getString("FEISCY"));
					sb.append(",'").append(rsl.getString("BEIZ")).append("','")
							.append(rsl.getString("LURY"));
					sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
							"','yyyy-mm-dd'),'").append(rsl.getString("zhiyr")).append("',");
					sb.append(rsl.getString("SHENHZT")).append("); \n");
				}

				while (rs.next()) {
					irow++;
					
					sb.append("insert into meihyb \n");
					sb
							.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
					sb.append("values (getnewid(").append(diancxxb_id).append(
							"),to_date('");
					sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
							.append(diancxxb_id).append(",");
					sb.append(rs.getString("id")).append(",").append(rs.getString("RULBZB_ID")).append(",")
							.append(rs.getString("jizfzb_id"));
					sb.append(",").append(rows==irow?shengyhml:haoml).append(",")
							.append("0");
					sb.append(",").append("0").append(",")
							.append("0");
					sb.append(",'").append(rsl.getString("BEIZ")).append("','")
							.append(rsl.getString("LURY"));
					sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
							"','yyyy-mm-dd'),'").append(rsl.getString("zhiyr")).append("',");
					sb.append(rsl.getString("SHENHZT")).append("); \n");
					
					sb.append("UPDATE rulmzlb SET meil=" + (rows==irow?shengyhml:haoml) + " WHERE ID=" + rs.getString("id") + "; \n");
				}
				rs.close();

			} else {

				sb.append("update meihyb_zb set RULRQ=to_date('").append(
						rsl.getString("RULRQ")).append(
						"','yyyy-mm-dd'),DIANCXXB_ID=");
				sb.append(diancxxb_id).append(",RULMZLB_ID=").append(
						rsl.getString("RULMZLB_ID")).append(",RULBZB_ID=");
				sb.append(rulbzb_id).append(",JIZFZB_ID=").append(jizfzb_id)
						.append(",FADHY=");
				sb.append(rsl.getString("FADHY")).append(",GONGRHY=").append(
						rsl.getString("GONGRHY")).append(",QITY=");
				sb.append(rsl.getString("QITY")).append(",FEISCY=").append(
						rsl.getString("FEISCY")).append(",BEIZ='");
				sb.append(rsl.getString("BEIZ")).append("',LURY='").append(
						rsl.getString("LURY")).append("',LURSJ=to_date('");
				sb.append(rsl.getString("LURSJ")).append(
						"','yyyy-mm-dd'),SHENHZT=").append(
						rsl.getString("SHENHZT"));
				sb.append(" where id=").append(rsl.getString("id")).append(
						";\n");
				
				sb.append(" delete from ").append(" meihyb ").append(
				"WHERE diancxxb_id=").append(rsl.getString("diancxxb_id"))
				.append(" AND  rulrq=to_date('" + rsl.getString("rulrq") + "','yyyy-mm-dd')") 
				.append(";	\n");
				
				int rows = 0;	
				int irow = 0;
				double haoml = 0;
				double shengyhml=0;
				double zonghml = rsl.getDouble("FADHY") + rsl.getDouble("GONGRHY") + rsl.getDouble("QITY") + rsl.getDouble("FEISCY");
				ResultSetList rs = con.getResultSetList("SELECT * FROM rulmzlb WHERE rulrq=to_date('" + rsl.getString("RULRQ") + "','yyyy-mm-dd') AND diancxxb_id=" + diancxxb_id);				
				rows = rs.getRows();
				
				if (rows>0) {
					haoml = Math.round(zonghml/rows*100)/100;
					shengyhml = zonghml - haoml*(rows-1);
				} else {
					sb.append("insert into meihyb \n");
					sb
							.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
					sb.append("values (getnewid(").append(diancxxb_id).append(
							"),to_date('");
					sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
							.append(diancxxb_id).append(",");
					sb.append(rulmzlb_id).append(",").append(rulbzb_id).append(",")
							.append(jizfzb_id);
					sb.append(",").append(rsl.getString("FADHY")).append(",")
							.append(rsl.getString("GONGRHY"));
					sb.append(",").append(rsl.getString("QITY")).append(",")
							.append(rsl.getString("FEISCY"));
					sb.append(",'").append(rsl.getString("BEIZ")).append("','")
							.append(rsl.getString("LURY"));
					sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
							"','yyyy-mm-dd'),'").append(rsl.getString("zhiyr")).append("',");
					sb.append(rsl.getString("SHENHZT")).append("); \n");
				}

				while (rs.next()) {
					irow++;
					
					sb.append("insert into meihyb \n");
					sb
							.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
					sb.append("values (getnewid(").append(diancxxb_id).append(
							"),to_date('");
					sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
							.append(diancxxb_id).append(",");
					sb.append(rs.getString("id")).append(",").append(rs.getString("RULBZB_ID")).append(",")
							.append(rs.getString("jizfzb_id"));
					sb.append(",").append(rows==irow?shengyhml:haoml).append(",")
							.append("0");
					sb.append(",").append("0").append(",")
							.append("0");
					sb.append(",'").append(rsl.getString("BEIZ")).append("','")
							.append(rsl.getString("LURY"));
					sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
							"','yyyy-mm-dd'),'").append(rsl.getString("zhiyr")).append("',");
					sb.append(rsl.getString("SHENHZT")).append("); \n");
					
					sb.append("UPDATE rulmzlb SET meil=" + (rows==irow?shengyhml:haoml) + " WHERE ID=" + rs.getString("id") + "; \n");
				}
				rs.close();
			}
		}
		sb.append("end;");
		con.getInsert(sb.toString());
//		AutoCreateShouhcrb.Create(con,diancxxb_id,DateUtil.getDate(strDate));

		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);

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
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		getSelectData();
	}
	
	private String getFenc(JDBCcon con){
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		String s="";
		while(rsl.next()){
			s+=rsl.getString("id")+",";
		}
		if(s.equals("")){
			s=this.getTreeid()+",";
		}
		
		return s.substring(0,s.lastIndexOf(","));
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rulrq = DateUtil.FormatOracleDate(this.getRiqi());	//入炉日期 
		String DefaultShzt = "5";		//默认审核状态  在不进行系统设置的时候默认为不审核
		String DefaultShjb = "";		//
		String Str = "m.shenhzt = 5";	//SQL过滤语句  只列出相应审核状态的数据
		String SQL = "select mingc from xitxxb where leib = '煤耗用' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		if (rs.next()) {
			DefaultShjb = rs.getString("mingc");
		}
		rs.close();
		if (!DefaultShjb.equals("不审核")) {
			DefaultShzt = "1";
			Str = "(m.shenhzt = 0 or m.shenhzt = 2)";
		} 
		//电厂Tree刷新条件
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			diancxxb_id = "and (d.id = " + this.getTreeid() + ")\n";
		}
		
		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,m.zhiyr,m.beiz,m.lury,m.lursj,m.shenhzt\n"
				+ "  from meihyb_zb m, diancxxb d, rulbzb rb, jizfzb j\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+ "   and m.rulrq = "+ rulrq + "\n"
				+ diancxxb_id
				+ "   and " + Str
		        +"  order by m.rulrq,rb.xuh,j.xuh";
		
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb_zb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("耗用日期");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("入炉班组");
		egu.getColumn("jizfzb_id").setHeader("入炉机组");
		egu.getColumn("fadhy").setHeader("发电耗用(t)");
		egu.getColumn("gongrhy").setHeader("供热耗用(t)");
		egu.getColumn("qity").setHeader("其它用(t)");
		egu.getColumn("feiscy").setHeader("非生产用(t)");
		egu.getColumn("zhiyr").setHeader("制样人");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("shenhzt").setHeader("状态");
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		// *****************************************设置默认值****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + visit.getDiancxxb_id());
		egu.getColumn("rulrq").setDefaultValue(this.getRiqi());
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("rulmzlb_id").setDefaultValue("0");
		egu.getColumn("fadhy").setDefaultValue("0");
		egu.getColumn("gongrhy").setDefaultValue("0");
		egu.getColumn("qity").setDefaultValue("0");
		egu.getColumn("feiscy").setDefaultValue("0");
		egu.getColumn("shenhzt").setDefaultValue(DefaultShzt);

		// 设置下拉框入炉班组
		ComboBox cb_banz = new ComboBox();
		egu.getColumn("rulbzb_id").setEditor(cb_banz);
		cb_banz.setEditable(true);
		String rulbzb_idSql = "select r.id,r.mingc from rulbzb r,diancxxb d  where r.diancxxb_id=d.id(+)"
			+ "and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		egu.getColumn("rulbzb_id").setReturnId(true);

		// 设置下拉框入炉机组
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) " +
				"and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by j.xuh";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));
		egu.getColumn("jizfzb_id").setReturnId(true);
		
        // 设置下拉框制样人
		ComboBox zhiyr = new ComboBox();
		egu.getColumn("zhiyr").setEditor(zhiyr);
		zhiyr.setEditable(true);
		String cb_zhiyr = "select id, quanc from renyxxb where bum = '制样'";
		egu.getColumn("zhiyr").setComboEditor(egu.gridId,
				new IDropDownModel(cb_zhiyr));
		egu.getColumn("zhiyr").editor.setAllowBlank(true);

		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		
//		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
//		" and z.mingc='shulzhcxqx' and r.id="+visit.getRenyID();//zuxxb中组的名称
//		ResultSetList rsl=con.getResultSetList(toaijsql);
//		if(!rsl.next()){
//			 dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
//					"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null, getTreeid_dc());
//		}else{
//			rsl=con.getResultSetList("select * from diancxxb d where d.id  in (select fuid from diancxxb)");
//			if(rsl.next()){
//				 dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
//							"diancTree", "" + rsl.getLong("id"), "forms[0]", null, getTreeid_dc());
//			}
//		}
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// ************************************************************
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
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
			visit.setList1(null);
			setJizfzbModel(null);
			setJizfzbModels();
			setRulbzbModel(null);
			setRulbzbModels();
			setRiqi(null);
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
	}

	public static void UpdateRulzlID(String riq, long diancxxb_id) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("update meihyb h ")
				.append("set rulmzlb_id = ( \n")
				.append(
						"select nvl(max(id),0) from rulmzlb z where z.rulrq = h.rulrq \n")
				.append(
						"and z.diancxxb_id = h.diancxxb_id and z.rulbzb_id = h.rulbzb_id \n")
				.append("and z.jizfzb_id = h.jizfzb_id ) where h.rulrq = ")
				.append(DateUtil.FormatOracleDate(riq)).append(
						" and h.diancxxb_id=").append(diancxxb_id);
		JDBCcon con = new JDBCcon();
		con.getUpdate(sb.toString());
		con.Close();
	}

	public IPropertySelectionModel getRulbzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setRulbzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setRulbzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setRulbzbModels() {
		String sql = "select r.id,r.mingc from rulbzb r";
		setRulbzbModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getJizfzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setJizfzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setJizfzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setJizfzbModels() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")";
		setJizfzbModel(new IDropDownModel(sql));
	}
	
//	 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}


	boolean treechange = false;

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}

}
