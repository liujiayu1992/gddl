package com.zhiren.jt.jihgl.ranmcgys;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ranmcgyszbwh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	protected void initialize() {
		// TODO 自动生成方法存根
		// super.initialize();
		setMsg("");
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

	private void Save() {

		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) getPage().getVisit();
		long diancxxb_id = Long.parseLong(this.getTreeid());
		//
		ResultSetList rr = null;
		String ss = "";
		String caigl = "0";
		String yuns = "0";
		String daocrz = "0";
		String chukjg = "0";
		String yunf = "0";
		String zaf = "0";
		String qitfy = "0";
		String daoczhjhs = "0";
		String daoczhjbhs = "0";
		String daocbmdjbhs = "0";

		ResultSetList rsl = getExtGrid().getModifyResultSet(
				getChange().replaceAll("&nbsp;", ""));// 把getChange中的&nbsp;替换成"",否则ext不能识别
		StringBuffer sbb = new StringBuffer();
		sbb.append("begin  \n");
		while (rsl.next()) {
			String sql = "select id from gongysb where mingc='"
					+ rsl.getString("mingc") + "'";
			rr = con.getResultSetList(sql);
			if (rr.next()) {
				ss = rr.getString("ID");
			}
			if (!"".equals(rsl.getString("caigl"))) {
				caigl = rsl.getString("caigl");
			}
			if (!"".equals(rsl.getString("yuns"))) {
				yuns = rsl.getString("yuns");
			}
			if (!"".equals(rsl.getString("daocrz"))) {
				daocrz = rsl.getString("daocrz");
			}
			if (!"".equals(rsl.getString("chukjg"))) {
				chukjg = rsl.getString("chukjg");
			}
			if (!"".equals(rsl.getString("yunf"))) {
				yunf = rsl.getString("yunf");
			}
			if (!"".equals(rsl.getString("zaf"))) {
				zaf = rsl.getString("zaf");
			}
			if (!"".equals(rsl.getString("qitfy"))) {
				qitfy = rsl.getString("qitfy");
			}
			if (!"".equals(rsl.getString("daoczhjhs"))&&!"NaN".equals(rsl.getString("daoczhjhs"))) {
				daoczhjhs = rsl.getString("daoczhjhs");
			}
			if (!"".equals(rsl.getString("daoczhjbhs"))) {
				daoczhjbhs = rsl.getString("daoczhjbhs");
			}
			if (!"".equals(rsl.getString("daocbmdjbhs"))&&!"NaN".equals(rsl.getString("daocbmdjbhs"))) {
				daocbmdjbhs = rsl.getString("daocbmdjbhs");
			}
			// 月份这里存的是季度的值
			if ("108".equals(getTreeid())) {
				setMsg("分子公司不能维护");
			}
			if ("0".equals(rsl.getString("id"))
					&& !"预算".equals(rsl.getString("xiangm"))
					&& !"108".equals(getTreeid())) {
				sbb
						.append("insert into ranmzbsjb (id,caigl, yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs,diancxxb_id,chaxtjb_id ) values("
								+ "getNewId(getDiancId('"
								+ diancxxb_id
								+ "')),"
								+ caigl
								+ ","
								+ yuns
								+ ","
								+ daocrz
								+ ","
								+ chukjg
								+ ","
								+ yunf
								+ ","
								+ zaf
								+ ","
								+ qitfy
								+ ","
								+ daoczhjhs
								+ ","
								+ daoczhjbhs
								+ ","
								+ daocbmdjbhs
								+ ","
								+ diancxxb_id
								+ ","
								+ visit.getString10() + "," + ")" + ";\n");
			} 
			
			if(!"0".equals(rsl.getString("id"))
					&& !"预算".equals(rsl.getString("xiangm"))
					&& !"108".equals(getTreeid())){
				
				sbb.append("update ranmzbsjb set caigl=" + caigl + ", yuns ="
						+ yuns + ",daocrz=" + daocrz + ",chukjg=" + chukjg
						+ ",yunf=" + yunf + ",zaf=" + zaf + ",qitfy=" + qitfy
						+ ",daoczhjhs=" + daoczhjhs + ",daoczhjbhs="
						+ daoczhjbhs + ",daocbmdjbhs=" + daocbmdjbhs
						+ ",diancxxb_id=" + diancxxb_id + ",chaxtjb_id=" + visit.getString10()
//						+ ",chaxtjb_id=" +visit.getString10()+",'yyyymm')"
						+ "  where id=" + rsl.getString("ID") + ";\n");
			}else {
				
				
			}
			}
		// 删除数据
		rsl = visit.getExtGrid1().getDeleteResultSet(
				getChange().replaceAll("&nbsp;", ""));
		while (rsl.next()) {
			if (!"0".equals(rsl.getString("ID"))) {

				sbb.append("delete from ranmzbsjb where id="
						+ rsl.getString("ID") + ";\n");
			}
		}
		sbb.append("end;");
//		System.out.println(sbb.toString());
		if (sbb.toString().length() > 13) {

			con.getUpdate(sbb.toString());
		}
//		System.out.println(sbb.toString());
		con.commit();
		// ---------------------
		con.Close();

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
//	对应于生成功能
	private boolean _ShengcButton = false;

	public void ShengcButton(IRequestCycle cycle) {
		_ShengcButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShengcButton) {
			_ShengcButton = false;
			shengc();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;

			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			cycle.activate("Chaxtj");
		}

	}
//	生成功能
	public void shengc() {

		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList rr = null;
		ResultSetList rr1 = null;
		StringBuffer sbb = new StringBuffer("begin \n");
		String kais = "";
		String jies = "";
		if (visit.getString12().length() == 1) {
			kais = "0" + visit.getString12();
		}
		if (visit.getString13().length() == 1) {
			jies = "0" + visit.getString12();
		}
		String caigl = "0";
		String yuns = "0";
		int daocrz = 0;
		String chukjg = "0";
		String yunf = "0";
		String zaf = "0";
		String qitfy = "0";
		String daoczhjhs = "0";
		String daoczhjbhs = "0";
		String daocbmdjbhs = "0";
		String sql = "select sum(ylb.laimsl) caigl,sum(ylb.yuns) yuns,sum(yzl.qnet_ar) daocrz,sum(yj.meij) chukjg,sum(yj.yunj) yunf,sum(yj.zaf+yj.jiaohqzf+yj.daozzf) zaf,sum(yj.qit) qitfy,sum((round_new((yj.zaf+yj.jiaohqzf+yj.daozzf+yj.qit+yj.yunj+yj.meij) ,2))) daoczhjhs,sum(round_new((yj.zaf+yj.jiaohqzf+yj.daozzf+yj.qit+yj.yunj+yj.meij-yj.meijs-yj.yunjs) ,2)) daoczhjbhs, sum(round_new(((yj.zaf+yj.jiaohqzf+yj.daozzf+yj.qit+yj.yunj+yj.meij-yj.meijs-yj.yunjs)*29.271/yzl.qnet_ar),0)) daocbmdjbhs \n"
				+ "\n"
				+ "\n"
				+ "\t\t\t\tfrom yuercbmdj yj,yuetjkjb yb ,yueslb ylb,yuezlb yzl\n"
				+ "\n"
				+ "         where yj.yuetjkjb_id=yb.id and yb.riq>= to_date('"
				+ visit.getString11()
				+ kais
				+ "01','yyyy-mm-dd') and yb.riq<=to_date('"
				+ visit.getString11()
				+ jies + "01','yyyy-mm-dd') and ylb.yuetjkjb_id=yb.id";

		rr = con.getResultSetList(sql);
		if (rr.getRows() == 0) {

			setMsg("月报基础数据不全");
		}
		while (rr.next()) {
			if (!"".equals(rr.getString("caigl"))) {
				caigl = rr.getString("caigl");
			}
			if (!"".equals(rr.getString("yuns"))) {
				yuns = rr.getString("yuns");
			}
			if (!"".equals(rr.getString("daocrz"))) {
				daocrz = Integer.parseInt(rr.getString("daocrz"))*1000;
			}
			if (!"".equals(rr.getString("chukjg"))) {
				chukjg = rr.getString("chukjg");
			}
			if (!"".equals(rr.getString("yunf"))) {
				yunf = rr.getString("yunf");
			}
			if (!"".equals(rr.getString("zaf"))) {
				zaf = rr.getString("zaf");
			}
			if (!"".equals(rr.getString("qitfy"))) {
				qitfy = rr.getString("qitfy");
			}
			if (!"".equals(rr.getString("daoczhjhs"))&&!"NaN".equals(rr.getString("daoczhjhs"))) {
				daoczhjhs = rr.getString("daoczhjhs");
			}
			if (!"".equals(rr.getString("daoczhjbhs"))) {
				daoczhjbhs = rr.getString("daoczhjbhs");
			}
			if (!"".equals(rr.getString("daocbmdjbhs"))&&!"NaN".equals(rr.getString("daocbmdjbhs"))) {
				daocbmdjbhs = rr.getString("daocbmdjbhs");
			}
		}
		String sfql="select * from ranmzbsjb r where r.chaxtjb_id="+visit.getString10()+"and r.diancxxb_id="+getTreeid();
		rr1 = con.getResultSetList(sfql);
		if (!"108".equals(getTreeid())&&rr1.getRows()==0) {

			sbb
					.append("insert into ranmzbsjb (id,caigl, yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs,diancxxb_id,chaxtjb_id ) values("
							+ "getNewId(getDiancId('"
							+ getTreeid()
							+ "')),"
							+ caigl
							+ ","
							+ yuns
							+ ","
							+ daocrz
							+ ","
							+ chukjg
							+ ","
							+ yunf
							+ ","
							+ zaf
							+ ","
							+ qitfy
							+ ","
							+ daoczhjhs
							+ ","
							+ daoczhjbhs
							+ ","
							+ daocbmdjbhs
							+ ","
							+ getTreeid()
							+ ","
							+ visit.getString10()
							+ ")" + ";\n");
		} else {
			setMsg("分子公司不能维护");
		}
        if(rr1.getRows()>0){
        	setMsg("只能生成一次");
        }
		sbb.append("end;");
		if (sbb.toString().length() > 13) {

			con.getUpdate(sbb.toString());
		}
		con.commit();
		// ---------------------
		con.Close();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框

		// -----------------------------------
		String str = "";

		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "  and (dc.fuid=  " + this.getTreeid() + " or dc.shangjgsid="
					+ this.getTreeid() + ")";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and dc.id = " + getTreeid() + "";

		}
		int nian = 0;
		// ---------------------------------------------------------------------
		if (!"".equals(visit.getString11()) || visit.getString11() != null) {

			nian = Integer.parseInt(visit.getString11());
		}

		String cha = "(select '完成' xiangm,r.id,   r.caigl,r.yuns,r.daocrz,r.chukjg,r.yunf,r.zaf,r.qitfy,r.daoczhjhs,r.daoczhjbhs,r.daocbmdjbhs  from ranmzbsjb r  ,chaxtjb c "
				+ "where   r.chaxtjb_id=c.id and r.diancxxb_id="
				+ getTreeid()
				+ " and  r.chaxtjb_id="
				+ visit.getString10()
				+ ") \n union ("
				+ "select '预算' as xiangm,0 id ,caigl,yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs from ("
				+ "select "
				+ "  nvl(sum( r.caigl),0 )caigl,nvl(sum(r.yuns),0 ) yuns,nvl(sum(r.daocrz),0 ) daocrz,nvl(sum(r.chukjg),0 ) chukjg,nvl(sum(r.yunf),0 ) yunf,nvl(sum(r.zaf),0 ) zaf,"
				+ "nvl(sum(r.qitfy),0 ) qitfy,"
				+ "nvl(sum(r.daoczhjhs),0 ) daoczhjhs,"
				+ "nvl(sum(r.daoczhjbhs),0 ) daoczhjbhs,nvl(sum(r.daocbmdjbhs),0 ) daocbmdjbhs  from ranmcgysb r   where "
				+ "r.diancxxb_id="
				+ getTreeid()
				+ " and to_char(r.nianf,'yyyy')='"
				+ (nian + 1)
				+ "'  "
				+ " ) )";
//		 System.out.println(cha);
		ResultSetList rsl = con.getResultSetList(cha);
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("ranmzbsjb");
		egu.setWidth("bodyWidth");
		egu.getColumn("xiangm").setCenterHeader("项目");
		egu.getColumn("xiangm").setEditor(null);
		egu.getColumn("caigl").setCenterHeader("采购量<br>万吨");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("yuns").setCenterHeader("运损<br>万吨");

		egu.getColumn("daocrz").setCenterHeader("到厂热值<br>KJ/kg");
		egu.getColumn("chukjg").setCenterHeader("出矿价格<br>元/吨");
		egu.getColumn("yunf").setCenterHeader("运费<br>元/吨");
		egu.getColumn("zaf").setCenterHeader("杂费<br>元/吨");
		egu.getColumn("qitfy").setCenterHeader("其他费用<br>元/吨");

		egu.getColumn("daoczhjhs").setCenterHeader("到厂综合价格<br>(含税)<br>元/吨");
		egu.getColumn("daoczhjbhs").setCenterHeader("到厂综合价格<br>(不含税)<br>元/吨");
		egu.getColumn("daocbmdjbhs").setCenterHeader("到厂标煤单价<br>(不含税)<br>元/吨");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(30);// 设置分页

		// 设定列的小数位
		((NumberField) egu.getColumn("daocbmdjbhs").editor)
				.setDecimalPrecision(3);

		// *************************下拉框*****************************************88

		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");// 设置分隔符
		 egu.addToolbarItem("{"+new GridButton("返回","function(){ document.getElementById('ReturnButton').click();" +
			"}").getScript()+"}");
		 egu.addTbarText("-");// 设置分隔符
		// 生成按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('ShengcButton').click();}");
		GridButton gbr = new GridButton("生成", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbr);
//		 刷新按钮
//		StringBuffer rsbb = new StringBuffer();
//		rsbb.append("function (){").append(

//				"document.getElementById('RefreshButton').click();}");
//		GridButton gbrb = new GridButton("刷新", rsbb.toString());
//		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
//		egu.addTbarBtn(gbrb);
//		egu.addTbarText("-");// 设置分隔符
		// 删除按钮
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");// 设置分隔符

		// 保存按钮
		String handler = "\n";

		// 保存
		egu.addToolbarButton(GridButton.ButtonType_Save_condition,
				"SaveButton", handler);

		egu.addTbarText("->");
		StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// 设定某一行不能编辑
		sb.append("if( e.column==10||e.column==12){e.cancel=true;}");//

		sb.append("});");

		sb.append("gridDiv_grid.on('afteredit',function(e){");
		String ss = ""
				+ "    var daoczhj_lj=0,biaomdj_lj=0,buhsbmdj_lj=0,j=e.row;\n"
				+

				"    daoczhj_lj=Round_new(eval(gridDiv_ds.getAt(j).get('CHUKJG'))+eval(gridDiv_ds.getAt(j).get('YUNF'))+eval(gridDiv_ds.getAt(j).get('ZAF'))+eval(gridDiv_ds.getAt(j).get('QITFY')),2);\n"
				+ "    biaomdj_lj=Round_new(eval(gridDiv_ds.getAt(j).get('DAOCZHJBHS'))*29.271/eval(gridDiv_ds.getAt(j).get('DAOCRZ')),2);\n"
                +
				// daoczhjbhs daocbmdjbhs

				"   gridDiv_ds.getAt(j).set('DAOCZHJHS',daoczhj_lj);\n"
				+ "   gridDiv_ds.getAt(j).set('DAOCBMDJBHS',biaomdj_lj);\n";

		sb.append(ss);

		sb.append("});");

		egu.addOtherScript(sb.toString());
		// ---------------页面js计算结束--------------------------
		egu.setDefaultsortable(false);

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
			visit.setString2("");
		}

		getSelectData();

	}

	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// 得到电厂的默认到站
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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