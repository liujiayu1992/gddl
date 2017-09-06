package com.zhiren.dc.jilgl.gongl.suoc;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/* 
* 时间：2009-03-20 
* 作者：ww
* 修改内容：直接操作sql server 库的Card_Plate表
*/ 

/*
 * 时间：2009-11-15
 * 作者:王伟
 * 描述：将sql server 改为oracle连接
 */

/*
 * 时间：2010-05-05
 * 作者:王伟
 * 描述：解决查询车号时没有信息无法再正常查询问题
 */

public class Suoc extends BasePage implements PageValidateListener {
	
	private static int BT_LOCK = 0;
	private static int BT_UNLOCK = 1;
	private static int BT_SAVE = 2;
	
//	界面用户提示
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
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	车头号	
	private String cheth;
	public String getCheth() {
		return cheth;
	}
	public void setCheth(String cheth) {		
		this.cheth = cheth;
	}
	
//	 绑定日期
//	public String getRiqi() {
//		return ((Visit)this.getPage().getVisit()).getString3();
//	}
//
//	public void setRiqi(String riqi) {
//		((Visit)this.getPage().getVisit()).setString3(riqi);
//	}
//
//	public String getRiq2() {
//		return ((Visit)this.getPage().getVisit()).getString4();
//	}
//
//	public void setRiq2(String riq2) {
//		((Visit)this.getPage().getVisit()).setString4(riq2);
//	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save(int Switch) {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		JDBCcon sqlserver =null;
//		if(v.getDiancxxb_id()==257){
//			sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
//					"jdbc:odbc:qchData_m","sa","sa");
//		}else{
			sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
					"jdbc:odbc:rmis","automeasure","automeasure");
//		}
		String Operation = "解锁";
		String islocked = "0";
		String sql = "";
		String cheph = "";
		int flag = -1;
		if(Switch == BT_LOCK){
			Operation = "锁定";
			islocked = "1";
		} else if (Switch == BT_SAVE) {
			Operation = "保存";
		}
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
		}
		ResultSetList rsl=getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Suoc.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}		
		while(rsl.next()){
			String cheh = rsl.getString("cheh");
			cheph += "," + cheh;

			if (Switch == BT_LOCK) {
				if (rsl.getString("LockYY") == null || "".equals(rsl.getString("LockYY"))) {
					setMsg("字段 锁车原因 不能为空");
					return;
				}
				if ("已锁定".equals(rsl.getString("zt"))) {
					setMsg("该车已被锁定,请先解锁!");
					return;
				}
				sql = "update Card_Plate set islocked = "+islocked+",\n" +
					"LockRY='" + v.getRenymc() +  "',\n" +
					"LockYY='" + rsl.getString("LockYY")+ "'" +
					"where Rtrim(Ltrim(Numplate)) = '" + cheh + "'";
				flag = sqlserver.getUpdate(sql);				
				if (flag != -1) {
					String strInsert = "INSERT INTO suocztb (ID,chelxxb_id,suocry,suocyy,zt,beiz) VALUES (\n" +
					"getnewid("+ v.getDiancxxb_id() + "),\n" +
					"-1,\n" +
					"'" + v.getRenymc() + "',\n" +
					"'" + rsl.getString("LockYY")+ "',\n" +
					islocked + ",\n" + 
					"'" + cheh + "'\n" +
					")";
					flag = con.getUpdate(strInsert);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						return;
					}
				}				
			} else if (Switch == BT_UNLOCK) {
				sql = "update Card_Plate set islocked = "+islocked+",\n" +
					"LockRY='',\n" +
					"LockYY=''\n" +
					"where Rtrim(Ltrim(Numplate)) = '" + cheh + "'";
				flag = sqlserver.getUpdate(sql);
				if (flag != -1) {
					String strUpdate = "UPDATE suocztb SET jiessj=sysdate,\n" +
						"jiesry='" + v.getRenymc() + "',\n" +
						"zt=0\n" +
						"where zt=1 and Rtrim(Ltrim(beiz))='" + cheh + "'";
					
					flag = con.getUpdate(strUpdate);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						return;
					}
				}
			} else if (Switch == BT_SAVE) {
				sql = "update Card_Plate set \n" +
					"MaxMZ=" + rsl.getDouble("MaxMZ") + ",\n" +
					"MinMZ=" + rsl.getDouble("MinMZ") + ",\n" +
					"MaxPZ=" + rsl.getDouble("MaxPZ") + ",\n" +
					"MinPZ=" + rsl.getDouble("MinPZ") + "\n" +
					"where Rtrim(Ltrim(Numplate)) = '" + cheh + "'";
				sqlserver.getUpdate(sql);
			}
		}
		rsl.close();
		con.Close();
		sqlserver.Close();
		cheph = cheph.substring(1);
		setMsg("您对 "+ cheph +" 进行了"+Operation+"操作。");
	}
 
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Save2Chick = false;

	public void Save2Button(IRequestCycle cycle) {
		_Save2Chick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save(BT_LOCK);
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Save(BT_UNLOCK);
			getSelectData();
		}
		if (_Save2Chick) {
			_Save2Chick = false;
			Save(BT_SAVE);
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		JDBCcon sqlserver =null;
		
		//2009-11-15 包头二电锁车改为Oracle
//		if(v.getDiancxxb_id()==257){
//			sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
//					"jdbc:odbc:qchData_m","sa","sa");
//		}else{
			sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
					"jdbc:odbc:rmis","automeasure","automeasure");
//		}
		ResultSetList rsl = null;
		String sql = "";
		
		sql = 
			"select distinct cheh,id from " +
			"(select q.cheh,nvl(c.id,0) id from qichzcb q,chelxxb c where q.cheh = c.cheph(+)\n" +
			"and to_char(q.maozsj,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd')\n" + 
			") where id = 0 ";
		rsl = con.getResultSetList(sql);
		String isql = "begin\n";
		while(rsl.next()){
			isql += "insert into chelxxb(id,diancxxb_id,yunsdwb_id,yunsfsb_id,cheph,maoz,piz,islocked)\n" +
					" values(getnewid("+v.getDiancxxb_id()+"),"+v.getDiancxxb_id()+
					",-1,"+SysConstant.YUNSFS_QIY+",'"+rsl.getString("cheh")+"',0,0,0);\n";
		}
		if(rsl.getRows()>0){
			isql += "end;";
			con.getInsert(isql);
		}
		rsl.close();
		
		sql = 
			"select distinct Rtrim(Ltrim(NumPlate)) as cheh,\n" +
			"case when IsLocked=1 then '已锁定' else '未锁定' end as zt,\n" +
			"cast(MaxMZ as numeric(8,2)) as MaxMZ,cast(MinMZ as numeric(8,2)) as MinMZ,\n" +
			"cast(MaxPZ as numeric(8,2)) as MaxPZ,cast(MinPZ as numeric(8,2)) as MinPZ,\n" +
			"Rtrim(Ltrim(LockRY)) as LockRY,Rtrim(Ltrim(LockYY)) as LockYY\n" +
			"from Card_Plate \n" ;
		if(getCheth()!=null && !"".equals(getCheth())){
			sql += "where NumPlate like '%" + getCheth() + "%'\n";
//		    sql += "where charindex('" + getCheth() + "',NumPlate)>0\n";
		}
		sql +=	"order by Rtrim(Ltrim(NumPlate))";
		if (!sqlserver.getHasIt(sql)) {
//			this.setMsg("没有该车信息！");
			this.setCheth(null);
			sql = 			
				"select distinct Rtrim(Ltrim(NumPlate)) as cheh,\n" +
				"case when IsLocked=1 then '已锁定' else '未锁定' end as zt,\n" +
				"cast(MaxMZ as numeric(8,2)) as MaxMZ,cast(MinMZ as numeric(8,2)) as MinMZ,\n" +
				"cast(MaxPZ as numeric(8,2)) as MaxPZ,cast(MinPZ as numeric(8,2)) as MinPZ,\n" +
				"Rtrim(Ltrim(LockRY)) as LockRY,Rtrim(Ltrim(LockYY)) as LockYY\n" +
				"from Card_Plate \n" +
				"order by Rtrim(Ltrim(NumPlate))";
		}
		rsl = sqlserver.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("cheplsb");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(25);
		
		egu.getColumn("cheh").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheh").setWidth(100);
		egu.getColumn("cheh").setEditor(null);
		egu.getColumn("zt").setHeader("状态");
		egu.getColumn("zt").setWidth(100);
		egu.getColumn("zt").setRenderer("ztChange");
		egu.getColumn("zt").setEditor(null);
		egu.getColumn("MaxMZ").setHeader("最大毛重");
		egu.getColumn("MaxMZ").setWidth(65);
		egu.getColumn("MinMZ").setHeader("最小毛重");
		egu.getColumn("MinMZ").setWidth(65);
		egu.getColumn("MaxPZ").setHeader("最大皮重");
		egu.getColumn("MaxPZ").setWidth(65);
		egu.getColumn("MinPZ").setHeader("最小皮重");
		egu.getColumn("MinPZ").setWidth(65);
		egu.getColumn("LockRy").setHeader("锁车人");
		egu.getColumn("LockRy").setWidth(65);
		egu.getColumn("LockRy").setEditor(null);
		egu.getColumn("LockYY").setHeader("锁车原因");
		egu.getColumn("LockYY").setWidth(200);
		//锁车原因不允许为空
//		egu.getColumn("LockYY").editor.allowBlank=false;
		
//		egu.addTbarText("检毛日期:");
//		DateField df = new DateField();
//		df.setReadOnly(true);
//		df.setValue(this.getRiqi());
//		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
//		df.setId("riqi");
//		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("车号：");
		TextField tf = new TextField();
		tf.setId("cheth");
		tf.setWidth(100);
		tf.setValue(getCheth());
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('CHETH').value=cheth.getValue(); document.getElementById('RefreshButton').click();}");
//		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton("保存",GridButton.ButtonType_Save, "Save2Button");
		egu.addToolbarButton("锁定",GridButton.ButtonType_SubmitSel, "SaveButton");
		egu.addToolbarButton("解锁",GridButton.ButtonType_SubmitSel, "DeleteButton");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		con.Close();
		sqlserver.Close();
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
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
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
//			setRiqi(DateUtil.FormatDate(new Date()));
//			setRiq2(DateUtil.FormatDate(new Date()));
			setCheth(null);
			setMsg(null);
			setTbmsg(null);
			getSelectData();
		}
	}
}