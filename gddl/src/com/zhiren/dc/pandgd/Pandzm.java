package com.zhiren.dc.pandgd;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 修改人：ww
 * 修改时间：2010-09-18
 * 修改内容：支持一厂多制
 */
/*
 * 修改人：licj
 * 修改时间：2010-10-26
 * 修改内容：把添加按钮换成生成，各个数据自动生成。
 */
public class Pandzm extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
	private IPropertySelectionModel _pandModel;
	public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		else if(v.isFencb()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pand_gd p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ " order by p.bianm desc";
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
	private IDropDownBean _pandValue;
	public void setPandValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getPandModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		String pandbm = "";
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pand_gd where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "  order by bianm desc";
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
	public void setID(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getID() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	/*private boolean _AddChick = false;
	public void AddButton(IRequestCycle cycle) {
		_AddChick = true;
	}*/
	private boolean _CreateClick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
		} else if(_CreateClick) {
			_CreateClick = false;
			insertData();
		}else if(_DelClick){
			delDate();
		}
		getSelectData();
	}
	private void delDate(){
		JDBCcon con = new JDBCcon();
		String sql="delete from pandzmb where pand_gd_id="+getPandbID();
		int result=con.getDelete(sql);
		if (result==-1) {
			setMsg("删除失败！");
		}
		con.Close();
	}
	private ResultSetList getYueshchj(JDBCcon con,long diancxxb_id,String month){
		ResultSetList rs=null;
		String sql=
			"select riq, fenx, shouml, fady, gongry, qith, sunh, diaocl, qickc, shuifctz,kuc,runxcs,jitcs\n" +
			"  from yueshchjb\n" + 
			" where diancxxb_id = "+diancxxb_id+"\n" + 
			"   and riq = date '"+month+"'";
		rs=con.getResultSetList(sql);
		return rs;
	}
	private ResultSetList getPandgd(JDBCcon con,long pandId){
		ResultSetList rs=null;
		String sql="select diancxxb_id,kaisrq,jiesrq from pand_gd where id="+pandId;
		rs=con.getResultSetList(sql);
		return rs;
	}
	//月平均水分差 [0]入厂平均水分差 [1]入炉平均水分差
	public double[] getShuifc(JDBCcon con, long diancxxb_id, String CurrODate){
		double shuifc[] = new double[2];
		String strSql=
			"select dianc.mingc, rucsf.mt rucsf, rulsf.mt rulsf\n" +
			"  from (select f.diancxxb_id,\n" + 
			"               round_new(decode(sum(f.jingz),0,0,sum(z.mt * f.jingz) / sum(f.jingz)),3) mt\n" + 
			"          from fahb f, zhilb z\n" + 
			"         where f.zhilb_id = z.id\n" + 
			"           and f.daohrq >= date'"+CurrODate+"'\n" + 
			"           and f.daohrq < Add_Months(date'"+CurrODate+"',1)\n" + 
			"         group by f.diancxxb_id) rucsf,\n" + 
			"       (select m.diancxxb_id,\n" + 
			"               round_new(decode(sum(m.fadhy + m.gongrhy + m.qity + m.feiscy),0,0,\n" + 
			"                                sum(r.mt *(m.fadhy + m.gongrhy + m.qity + m.feiscy)) /\n" + 
			"                                sum(m.fadhy + m.gongrhy + m.qity + m.feiscy)),3) mt\n" + 
			"          from meihyb m, rulmzlb r\n" + 
			"         where m.rulmzlb_id = r.id\n" + 
			"           and m.rulrq >= date'"+CurrODate+"'\n" + 
			"           and m.rulrq < Add_Months(date'"+CurrODate+"',1)\n" + 
			"         group by m.diancxxb_id) rulsf,\n" + 
			"       vwdianc dianc\n" + 
			" where dianc.id = rucsf.diancxxb_id\n" + 
			"   and dianc.id = rulsf.diancxxb_id\n" + 
			"   and dianc.id="+diancxxb_id;
		ResultSetList rs=con.getResultSetList(strSql);
		if(!con.getHasIt(strSql)){
			shuifc[0]=0.0;
			shuifc[1]=0.0;
			return shuifc;
		}
		if (rs.next()) {
			shuifc[0]=rs.getDouble("rucsf");
			shuifc[1]=rs.getDouble("rulsf");
		}
		return shuifc;
	}
	//生成数据 
	public void insertData() {
		delDate();
		JDBCcon con = new JDBCcon();
		long diancxxb_id=0;
		Date kaisrq=null;
		Date jiesrq=null;
		ResultSetList pandList=getPandgd(con,getPandbID());
		if (pandList!=null&&pandList.next()) {
			diancxxb_id=pandList.getLong("diancxxb_id");
			kaisrq=pandList.getDate("kaisrq");
			jiesrq=pandList.getDate("jiesrq");
		}
		ResultSetList yueList=getYueshchj(con,diancxxb_id,DateUtil.Formatdate("yyyy-MM-01",jiesrq));
		double sunh=0.0; //运输损耗
		double shuifctz=0.0; //水分差调整
		double laiml=0.0;//来煤量
		double haoml=0.0;  //耗煤量
		double qith=0.0;   //其它耗用
		double diaocl=0.0;  //调出量
		double kuc=0.0;   //计提储耗后库存
		double qickc=0.0;  //期初库存
		double runxcs=0.0;  //允许储损
		double jitcx=0.0 ; //计提储损
		while(yueList.next()){
			if(yueList.getString("fenx").equals("本月")){
				sunh=yueList.getDouble("sunh");
				laiml=yueList.getDouble("shouml");
				haoml=yueList.getDouble("fady")+yueList.getDouble("gongry");
				qith=yueList.getDouble("qith");
				diaocl=yueList.getDouble("diaocl");
				qickc=yueList.getDouble("qickc");
				shuifctz=yueList.getDouble("shuifctz");
				runxcs=yueList.getDouble("runxcs");
				jitcx=yueList.getDouble("jitcx");
				kuc=yueList.getDouble("kuc");
			}
		}
		String CurrODate=DateUtil.Formatdate("yyyy-MM-01", jiesrq);
		double[] shuifc=getShuifc(con,diancxxb_id,CurrODate);//月平均水分差 [0]入厂平均水分差 [1]入炉平均水分差
		double bengqkc=kuc+jitcx; //本期账面库存
		String insertSql=
			"insert into pandzmb\n" +
			"  (id,\n" + 
			"   pand_gd_id,\n" + 
			"   yunxcs,\n" + 
			"   jitcs,\n" + 
			"   yuns,\n" + 
			"   dangyljrcsf,\n" + 
			"   dangyljrlsf,\n" + 
			"   shuifctz,\n" + 
			"   qickc,\n" + 
			"   benylm,\n" + 
			"   benyhm,\n" + 
			"   qithm,\n" + 
			"   diaocl,\n" + 
			"   benqkc,\n" + 
			"   jitshhkc)\n" + 
			"values\n" + 
			"  (getnewid("+diancxxb_id+")," +getPandbID()+
			"," +runxcs+
			"," +sunh+
			"," +sunh+
			"," +shuifc[0]+
			"," +shuifc[1]+
			"," +shuifctz+
			"," +qickc+
			"," +laiml+
			"," +haoml+
			"," +qith+
			"," +diaocl+
			"," +bengqkc+
			"," +kuc+")";
		int re=con.getInsert(insertSql);
		if (re==-1) {
			setMsg("生成失败！");
		}else{
			setMsg("生成成功！");
		}
		con.Close();
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
					+ "pandzmb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//进行删除操作时添加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Renyzz,
					"pandzmb",id+"");
			sSql = "delete from pandzmb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandzmb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sSql = "insert into pandzmb values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ getPandbID() + ",'"
					+ rsl.getString("YUNXCS") + "','"
					+ rsl.getString("JITCS") + "','"
					+ rsl.getString("YUNS") + "','"
					+ rsl.getString("DANGYLJRCSF") + "','"
					+ rsl.getString("DANGYLJRLSF") + "','"
					+ rsl.getString("SHUIFCTZ") + "','"
					+ rsl.getString("QICKC") + "','"
					+ rsl.getString("BENYLM") + "','"
					+ rsl.getString("BENYHM") + "','"
					+ rsl.getString("QITHM") + "','"
					+ rsl.getString("DIAOCL") + "','"
					+ rsl.getString("BENQKC") + "','"
					+ rsl.getString("JITSHHKC") + "'"
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				//进行修改操作时添加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Renyzz,
						"pandzmb",id+"");
				sSql = "update pandzmb set "
					+ " pand_gd_id=" + getPandbID() + ","					
					+ " YUNXCS='" + rsl.getString("YUNXCS") + "',"
					+ " JITCS='" + rsl.getString("JITCS") + "',"
					+ " YUNS='" + rsl.getString("YUNS") + "',"
					+ " DANGYLJRCSF='" + rsl.getString("DANGYLJRCSF") + "',"
					+ " DANGYLJRLSF='" + rsl.getString("DANGYLJRLSF") + "',"
					+ " SHUIFCTZ='" + rsl.getString("SHUIFCTZ") + "',"
					+ " QICKC='" + rsl.getString("QICKC") + "',"
					+ " BENYLM='" + rsl.getString("BENYLM") + "',"
					+ " BENYHM='" + rsl.getString("BENYHM") + "',"
					+ " QITHM='" + rsl.getString("QITHM") + "',"
					+ " DIAOCL='" + rsl.getString("DIAOCL") + "',"
					+ " BENQKC='" + rsl.getString("BENQKC") + "',"
					+ " JITSHHKC='" + rsl.getString("JITSHHKC") + "'"
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
	}
	public void loadData() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sID = new StringBuffer();
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select id from pand_gd where riq<(select riq from pand_gd where bianm='" + getPandbm() + "'" 
					+ " and diancxxb_id=" + visit.getDiancxxb_id() + ")"
					+ " and diancxxb_id=" + visit.getDiancxxb_id() + " order by id desc";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				String id = rs.getString("id");
				sql = "select id from pandzmb where pand_gd_id =" + id;
				ResultSet rs2 = con.getResultSet(sql);
				boolean flag = false;
				while (rs2.next()) {
					if (!flag) flag = true;
					sID.append(rs2.getString("id")).append(",");
				}
				if (flag) {
					sID.deleteCharAt(sID.length()-1);
					setID(sID.toString());
					flag = false;
				}
			} 
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		String sSql = "";
		JDBCcon con = new JDBCcon();
		sSql = "select p.id,YUNXCS,JITCS,YUNS,DANGYLJRCSF,DANGYLJRLSF,SHUIFCTZ,QICKC,\n" +
				" BENYLM,BENYHM,QITHM,DIAOCL,BENQKC,JITSHHKC from pandzmb p,pand_gd where p.pand_gd_id=pand_gd.id and pand_gd.bianm='" + getPandbm() + "'";
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
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("YUNXCS").setHeader("允许储损");
		egu.getColumn("JITCS").setHeader("计提储损");
		egu.getColumn("YUNS").setHeader("运输损耗");
		egu.getColumn("DANGYLJRCSF").setHeader("当月累计入厂水分");
	    egu.getColumn("DANGYLJRLSF").setHeader("当月累计入炉水分");
	    egu.getColumn("SHUIFCTZ").setHeader("水分差调整");
	    egu.getColumn("QICKC").setHeader("期初库存");
	    egu.getColumn("BENYLM").setHeader("本月来煤");
	    egu.getColumn("BENYHM").setHeader("本月耗煤");
	    egu.getColumn("QITHM").setHeader("其它耗煤");
	    egu.getColumn("DIAOCL").setHeader("调出量");
	    egu.getColumn("BENQKC").setHeader("本期库存");
	    egu.getColumn("JITSHHKC").setHeader("计提损耗库存");
//	    egu.getColumn("BENQYK").setHeader("本期盈亏");
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
		//生成按键
		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		//删除
		GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
//		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖")
			.append(getPandbm()).append("的已存数据，是否继续？");
		}else {
			btnsb.append("是否删除").append(getPandbm()).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		}
		init();
	}
	private void init() {
		setID(null);
		loadData();
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

