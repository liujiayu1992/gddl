package com.zhiren.dc.rulgl.rulmzlb;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.dc.rulgl.meihyb.Meihybext;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*2009-11-27
*罗朱平
*描述：注销入炉化验页面氢硫值有默认值。
*/
/**
 * @author 王磊
 * @since 2009-07-29
 * @description 新增河北个性化入炉煤质量功能
 *  insert into itemsort(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
	values(id,itemsortid,1,'RUHYBZ','入炉化验班组',1,'');
	insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
	values(id,itemsortid,1,'YI','一班',1,'');
	insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
	values(id,itemsortid,2,'ER','二班',1,'');
	insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
	values(id,itemsortid,3,'SAN','三班',1,'');
 */

public class Rulmzl_hb extends BasePage implements PageValidateListener {
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO Auto-generated method stub
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		/*   硫元素分析基   */
		String S_sign = Compute.getYuansSign(con, Compute.hyType_RL, Compute.yuans_S, visit.getDiancxxb_id(), "Stad");//提供默认值
		/*   氢元素分析基   */
		String H_sign = Compute.getYuansSign(con, Compute.hyType_RL, Compute.yuans_H, visit.getDiancxxb_id(), "Had");//提供默认值
		String sql = "";
		String rulrq = "";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while(mdrsl.next()){
			String id = mdrsl.getString("id");
			rulrq = DateUtil.FormatOracleDate(mdrsl.getString("rulrq"));
			if("0".equals(id)){
				id = MainGlobal.getNewID(Long.parseLong(getTreeid()));
				sql = "insert into rulmzlzb(id,diancxxb_id,fenxrq,lursj," +
						"rulrq,RULBZB_ID,JIZFZB_ID,huayy,LURY) values(" + id + "," + 
						mdrsl.getString("diancxxb_id") + "," + 
						DateUtil.FormatOracleDate(mdrsl.getString("fenxrq")) +
						",to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')," +
						DateUtil.FormatOracleDate(mdrsl.getString("rulrq")) + "," +
						(getExtGrid().getColumn("rulbzb_id").combo)
						.getBeanId(mdrsl.getString("rulbzb_id")) + "," + 
						(getExtGrid().getColumn("jizfzb_id").combo)
						.getBeanId(mdrsl.getString("jizfzb_id")) + ",'" + 
						mdrsl.getString("huayy") + "','"+visit.getRenymc()+"')"	;
				con.getInsert(sql);
			}
			else{
				String updateSQL="update rulmzlzb set fenxrq="+DateUtil.FormatOracleDate(mdrsl.getString("fenxrq"))+
				",RULBZB_ID="+(getExtGrid().getColumn("rulbzb_id").combo).getBeanId(mdrsl.getString("rulbzb_id"))+
				",JIZFZB_ID="+(getExtGrid().getColumn("jizfzb_id").combo).getBeanId(mdrsl.getString("jizfzb_id"))+
				" where id="+id;
				con.getUpdate(updateSQL);
			}
			sql = "update rulmzlzb set " +Compute.ComputeValue(mdrsl.getDouble("mt"),
					mdrsl.getDouble("mad"), mdrsl.getDouble("aad"), mdrsl.getDouble("vad"),
					mdrsl.getDouble("qbad"), H_sign, mdrsl.getDouble(H_sign),
					S_sign, mdrsl.getDouble(S_sign), 2) + "huayy= '"+mdrsl.getString("huayy") +"' where id =" + id;
			con.getUpdate(sql);
			
		}
		mdrsl.close();
		Compute.UpdateRulmzl(con, rulrq, getTreeid());
		con.Close();
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strRiq = getRiqi();
		/*   oracle入炉日期   */
		String oraRiq = DateUtil.FormatOracleDate(strRiq);
		/*   硫元素分析基   */
		String S_sign = Compute.getYuansSign(con, Compute.hyType_RL, Compute.yuans_S, visit.getDiancxxb_id(), "Stad");//提供默认值
		/*   氢元素分析基   */
		String H_sign = Compute.getYuansSign(con, Compute.hyType_RL, Compute.yuans_H, visit.getDiancxxb_id(), "Had");//提供默认值
		/*   硫元素是否可显示及编辑   */
		boolean S_show = Compute.getYuansisShow(con, Compute.yuans_S, visit.getDiancxxb_id(), true);
		/*   氢元素是否可显示及编辑   */
		boolean H_show = Compute.getYuansisShow(con, Compute.yuans_H, visit.getDiancxxb_id(), true);
		/*	 一内月到货硫值    */
		double dblS = Compute.getYuansValRL(con, S_sign, oraRiq, getTreeid());
		/*	 一内月到货氢值    */
		double dblH = Compute.getYuansValRL(con, H_sign, oraRiq, getTreeid());
		/*   化验氢、硫值是否设置正确    */
		if(dblH == 0.0 && !H_show){
			setMsg("化验氢值设置不正确，请检查！");
		}
		if(dblS == 0.0 && !S_show){
			setMsg("化验硫值设置不正确，请检查！");
		}
		String sql = "select r.id,r.diancxxb_id,r.rulrq,r.fenxrq,rb.mingc as rulbzb_id,\n"
			+"jz.mingc as jizfzb_id,r.mt,r.mad,r.aad,r.aar,r.vad,r.vdaf,r."+S_sign+",r."+H_sign+",r.qbad,round_new(r.qgrad,2) qgrad,round_new(r.qgrad_daf,2) qgrad_daf,r.qnet_ar,\n"
			+ "       r.huayy,r.beiz\n"
			+ "  from rulmzlzb r, jizfzb jz,"
			+ "(select i.id,i.xuh,i.bianm,i.mingc\n" 
			+ "from itemsort it,item i\n"  
			+ "where it.id = i.itemsortid\n"  
			+ "and it.bianm = 'RUHYBZ') rb\n"
			+ " where r.rulbzb_id = rb.id(+)\n"
			+ " and r.jizfzb_id=jz.id \n"
			+ "   and r.rulrq = "+oraRiq+" and r.diancxxb_id = "+getTreeid()+"\n"
//			+ "   and (r.shenhzt = 0 or r.shenhzt = 2)\n"
			+ " order by r.fenxrq,r.rulrq,rb.xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rulmzlb");
		egu.setWidth("bodyWidth");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		/*	设置列名	*/
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("rulrq").setHeader("入炉日期");
		egu.getColumn("fenxrq").setHeader("分析日期");
		egu.getColumn("rulbzb_id").setHeader("入炉班组");
		egu.getColumn("jizfzb_id").setHeader("入炉机组");
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("aad").setHeader("Aad(%)");
		egu.getColumn("aar").setHeader("Aar(%)");
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("vdaf").setHeader("Vdaf(%)");
		egu.getColumn(H_sign).setHeader(H_sign + "(%)");
		egu.getColumn(S_sign).setHeader(S_sign + "(%)");
		egu.getColumn("qbad").setHeader("Qb,ad(MJ/kg)");
		egu.getColumn("qgrad").setHeader("Qgr,ad(MJ/kg)");
		egu.getColumn("qgrad_daf").setHidden(true);
		egu.getColumn("qgrad_daf").setHeader("Qgr,daf(MJ/kg)");
		egu.getColumn("qnet_ar").setHeader("Qnet,ar(MJ/kg)");
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("beiz").setHeader("备注");
		/*	设置列不可编辑	 */
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("qnet_ar").setEditor(null);
		/*  设置列默认值  */
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("rulrq").setDefaultValue(strRiq);
		egu.getColumn("fenxrq").setDefaultValue(DateUtil.FormatDate(
				DateUtil.AddDate(DateUtil.getDate(strRiq), 1, DateUtil.AddType_intDay)));
//		egu.getColumn(H_sign).setDefaultValue(""+dblH);
//		egu.getColumn(S_sign).setDefaultValue(""+dblS);
		/*  设置列显示  */
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn(H_sign).setHidden(!H_show);
		egu.getColumn(S_sign).setHidden(!S_show);
		/*  设置列宽  */
		egu.getColumn("rulrq").setWidth(85);
		egu.getColumn("fenxrq").setWidth(85);
		egu.getColumn("diancxxb_id").setWidth(85);
		egu.getColumn("rulbzb_id").setWidth(85);
		egu.getColumn("jizfzb_id").setWidth(100);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("vad").setWidth(60);
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(80);
		egu.getColumn("huayy").setWidth(60);
		if (H_show) {
			egu.getColumn(H_sign).setWidth(60);
		}else{
			egu.getColumn(H_sign).setWidth(0);
		}
		if (S_show) {
			egu.getColumn(S_sign).setWidth(60);
		}else{
			egu.getColumn(S_sign).setWidth(0);
		}
		/*  设置编辑下拉框  */
		ComboBox cb_banz = new ComboBox();
		egu.getColumn("rulbzb_id").setEditor(cb_banz);
		cb_banz.setEditable(true);
		String rulbzb_idSql = "select i.id,i.mingc\n" +
			"from itemsort it,item i\n" + 
			"where it.id = i.itemsortid\n" + 
			"and it.bianm = 'RUHYBZ' order by i.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));

		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String jizfzb_idSql = "select id,mingc from jizfzb";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jizfzb_idSql));
		/*  设置工具栏  */
		egu.addTbarText("入炉日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//egu.addToolbarButton("提交",GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			visit.setString11("");
			visit.setString12("");
		}

		getSelectData();

	}

	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

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
