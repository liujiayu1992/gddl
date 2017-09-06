package com.zhiren.dc.rulgl.meihyb;

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
//import com.zhiren.dc.diaoygl.AutoCreateShouhcrb;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 王磊
 * 2009-05-13
 * 电厂树的TreeId未初始化的BUG
 */
/*
 * ly
 * 修改时间：2009-04-27
 * 修改内容：增加电厂Tree
 */
/*
 * 2009-05-18
 * 王磊
 * 修改自动计算收耗存时传入时间参数的错误
 */
public class Meihybext_hb extends BasePage implements PageValidateListener {
//		客户端的消息框
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
//		页面初始化(每次刷新都执行)
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
//		 日期控件
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			setRiqi(DateUtil.FormatDate(new Date()));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {
		// Visit visit = (Visit) this.getPage().getVisit();
		// visit.getExtGrid1().Save(getChange(), visit);
		// UpdateRulzlID(getRiqi(),visit.getDiancxxb_id());

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
		if (delrsl.getRows() != 0) {
			sbdl.append("begin\n");
			while (delrsl.next()) {
				String id = delrsl.getString("id");
				//删除时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,
						"meihyb",id);
				sbdl.append(" delete from ").append("meihyb").append(
						" where id =").append(delrsl.getString("id")).append(
						";	\n");
				//删除时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,
						"rulmzlb",id);
				sbdl.append(" delete from ").append("rulmzlb").append(
						" where id =").append(delrsl.getString("rulmzlb_id"))
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
		String rulrq = "";
		String strDate = "";
		while (rsl.next()) {
			// if (visit.isFencb()) {
			// diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
			// .getBeanId(rsl.getString("diancxxb_id"));
			// } else {
			// diancxxb_id = visit.getDiancxxb_id();
			// }
			diancxxb_id = Integer.parseInt(getTreeid());
			String rulmzlb_id = MainGlobal.getNewID(visit.getDiancxxb_id());// 得到rulmzlb_id

			String fenxrq = DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			long rulbzb_id = ((IDropDownModel) getRulbzbModel()).getBeanId(rsl
					.getString("rulbzb_id"));
			long jizfzb_id = ((IDropDownModel) getJizfzbModel()).getBeanId(rsl
					.getString("jizfzb_id"));
			rulrq = DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			strDate = rsl.getString("rulrq");
			if ("0".equals(rsl.getString("id"))) {
				String sql = "select id from rulmzlb where rulrq = to_date('"+rsl.getString("RULRQ")+"','yyyy-mm-dd') and diancxxb_id="+diancxxb_id;
				ResultSetList rs = con.getResultSetList(sql);
				if(rs.getRows() <=0){	
					sb
					.append(
					"insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,rulrq,lursj,shenhzt) values (\n")
					.append(rulmzlb_id).append(",").append(diancxxb_id)
					.append(",").append(fenxrq).append(",").append(
							rulbzb_id).append(",").append(jizfzb_id)
							.append(",").append(rulrq).append(",").append(rulrq)
							.append(",0);\n");
					
					sb.append("insert into meihyb \n");
					sb
					.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,SHENHZT) \n");
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
					"','yyyy-mm-dd'),");
					sb.append(rsl.getString("SHENHZT")).append("); \n");
				}else{
					while(rs.next()){	
						sb.append("update rulmzlb set rulbzb_id=").append(rulbzb_id).append(",jizfzb_id=").append(jizfzb_id)
						.append(" where id=").append(rs.getString("ID")).append(";");
						sb.append("insert into meihyb \n");
						sb
						.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,SHENHZT) \n");
						sb.append("values (getnewid(").append(diancxxb_id).append(
						"),to_date('");
						sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),")
						.append(diancxxb_id).append(",");
						sb.append(rs.getString("ID")).append(",").append(rulbzb_id).append(",")
						.append(jizfzb_id);
						sb.append(",").append(rsl.getString("FADHY")).append(",")
						.append(rsl.getString("GONGRHY"));
						sb.append(",").append(rsl.getString("QITY")).append(",")
						.append(rsl.getString("FEISCY"));
						sb.append(",'").append(rsl.getString("BEIZ")).append("','")
						.append(rsl.getString("LURY"));
						sb.append("',to_date('").append(rsl.getString("LURSJ")).append(
						"','yyyy-mm-dd'),");
						sb.append(rsl.getString("SHENHZT")).append("); \n");
					}
				}

			} else {
				String id = rsl.getString("id");
				//修改时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"rulmzlb",id);
				sb.append("update rulmzlb set fenxrq=").append(fenxrq).append(
						",rulbzb_id=").append(rulbzb_id);
				sb.append(",jizfzb_id=").append(jizfzb_id).append(",rulrq=")
						.append(rulrq).append(",lursj=").append(rulrq).append(
								",shenhzt=0").append(" where id=");
				sb.append(rsl.getString("rulmzlb_id")).append(";\n");

				//修改时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"meihyb",id);
				sb.append("update meihyb set RULRQ=to_date('").append(
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
	private boolean shousButton = false;

	public void shousButton(IRequestCycle cycle) {
		shousButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(shousButton){
			shousButton = false;
			shous();
			getSelectData();
		}
	}
	private void shous(){
		//如果入炉耗用存在
			//不能操作，必须先删除
		//如果入炉耗用不存在
			//插入rulmzlb，meihyb

		JDBCcon con=new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql="select *\n" +
		"from meihyb\n" + 
		"where to_char(meihyb.rulrq,'yyyy-mm-dd')='"+getRiqi()+"'";
		ResultSet rs=con.getResultSet(sql);
		String rulmzlb_id="0";
		try{
			if(rs.next()){//如果入炉耗用存在
				setMsg("煤耗用已经存在，请删除后再操作！");
			}else{
				int flag=0;
				con.setAutoCommit(true);
//					插入rulmzlb，meihyb
				rulmzlb_id= MainGlobal.getNewID(visit.getDiancxxb_id());
			   sql="insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,rulrq,lursj,shenhzt) values (\n"+
			   rulmzlb_id+","+visit.getDiancxxb_id()+",to_date('"+getRiqi()+"','yyyy-mm-dd')+1,(select id from rulbzb where mingc='全天'),"+
			   "(select id from JIZFZB where mingc='全厂机组'),"+"to_date('"+getRiqi()+"','yyyy-mm-dd'),sysdate,0)";
			   flag= con.getInsert(sql);
			   if(flag==-1){
				   con.rollBack();
				   return;
			   }
			   sql=
				   "insert into meihyb(id,rulrq,diancxxb_id,rulmzlb_id,rulbzb_id,jizfzb_id,fadhy,gongrhy,shenhzt)(\n" +
				   "select getnewid(257)id,to_date('"+getRiqi()+"','yyyy-mm-dd')riq,"+visit.getDiancxxb_id()+" diancxxb_id,"+rulmzlb_id+" RULMZLB_ID,\n" + 
				   "(select id from rulbzb where mingc='全天')rulbzb_id,\n" + 
				   "(select id from JIZFZB where mingc='全厂机组')JIZFZB_ID,\n" + 
				   "b.fadhy,b.gongrhy,5 shenhzt\n" + 
				   "from (\n" + 
				   "     select nvl(sum(fadhy),0)fadhy,nvl(sum(gongrhy),0)gongrhy\n" + 
				   "    from (\n" + 
				   "    select plan_data fadhy,0 gongrhy\n" + 
				   "    from zhiren@tj t\n" + 
				   "    where t.plan_xh='90041' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')--一正平衡发电耗天然煤量\n" + 
				   "    union\n" + 
				   "    select 0 fadhy,plan_data gongrhy\n" + 
				   "    from zhiren@tj t\n" + 
				   "    where t.plan_xh='90054' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')--一正平衡供热天然煤量\n" + 
				   "    )\n" + 
				   ")b\n" + 
				   " )";
			   flag=con.getInsert(sql);
			   if(flag==-1){
				   con.rollBack();
				   return;
			   }
			   	con.commit();
			}
		}catch(Exception e){
			e.printStackTrace();
			con.rollBack();
		}finally{
			con.Close();
		}
		
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
			diancxxb_id = "and d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + "\n";
		}else if(getDiancTreeJib()==3){
			diancxxb_id = "and d.id = " + this.getTreeid() + "\n";
		}
		
		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,m.beiz,m.lury,m.lursj,m.shenhzt\n"
				+ "  from meihyb m, diancxxb d, rulmzlb r,(select item.id,item.mingc from item,itemsort where item.itemsortid=itemsort.id and itemsort.bianm='RUHYBZ') rb, jizfzb j\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulmzlb_id = r.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+ "   and m.rulrq = "+ rulrq + "\n"
//					+ "   and d.id ="+ visit.getDiancxxb_id()
				+ diancxxb_id
				+ "   and " + Str;;
		// System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");

		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("耗用日期");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("入炉班组");
		egu.getColumn("jizfzb_id").setHeader("入炉机组");
		egu.getColumn("fadhy").setHeader("发电耗用(吨)");
		egu.getColumn("gongrhy").setHeader("供热耗用(吨)");
		egu.getColumn("qity").setHeader("其它用(吨)");
		egu.getColumn("feiscy").setHeader("非生产用(吨)");
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
		egu.setWidth(1000);// 设置页面的宽度,当超过这个宽度时显示滚动条
		// *****************************************设置默认值****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + getTreeid());
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
		String rulbzb_idSql = "select r.id,r.mingc from rulbzb r where r.diancxxb_id="
				+ visit.getDiancxxb_id() + " order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		egu.getColumn("rulbzb_id").setReturnId(true);
		// 设置下拉框入炉机组
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="
				+ visit.getDiancxxb_id() + "";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));
		egu.getColumn("jizfzb_id").setReturnId(true);

		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//			 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
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
		
		if(MainGlobal.getXitxx_item("耗用电量", "耗用电量取数据", "0", "否").equals("是")){
//				egu.addToolbarButton(GridButton.."收取数据", "shouqsj");
			egu.addToolbarItem("{"+new GridButton("收取数据","function(){document.getElementById('shousButton').click();}").getScript()+"}");
		}
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			
		}
		getSelectData();
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
		String sql = "select item.id,item.mingc from item,itemsort where item.itemsortid=itemsort.id and itemsort.bianm='RUHYBZ'";
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
		String sql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="
				+ visit.getDiancxxb_id() + "";
		setJizfzbModel(new IDropDownModel(sql));
	}
	
//		 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
