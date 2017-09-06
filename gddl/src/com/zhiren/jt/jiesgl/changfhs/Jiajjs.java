package com.zhiren.jt.jiesgl.changfhs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Window;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeButton;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：ly
 * 时间:2009-09-17
 * 内容:用于厂级结算中加价计算
 */
public class Jiajjs extends BasePage implements PageValidateListener {
//	界面用户提示
	
	private final static String xiaosdd_fgs="分公司销售默认合同ID";//分公司销售 系统中mingc字段
	private final static String caigdd_dc="电厂采购默认合同ID";	//电厂采购   系统中mingc字段
	
	private final static String isXiaos_cz="是否计算分公司销售";//分公司销售 系统中mingc字段
	private final static String isCaig_cz="是否计算电厂采购";	 //电厂采购   系统中mingc字段
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
//	销售流程mingc
	private String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}
	
	
	//采购流程mingc
	private String _liucmc1;
	
	public void setLiucmc1(String _value) {
		_liucmc1 = _value;
	}
	
	public String getLiucmc1() {
		if (_liucmc1 == null) {
			_liucmc1 = "";
		}
		return _liucmc1;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		this.setLiucmc("");
		this.setLiucmc1("");
		this.setChange("");
	}
	
	private String Zhi1;

	public String getZhi1() {
		return Zhi1;
	}

	public void setZhi1(String zhi) {
		Zhi1 = zhi;
	}
	
	private String Zhi2;

	public String getZhi2() {
		return Zhi2;
	}

	public void setZhi2(String zhi) {
		Zhi2 = zhi;
	}
//***************************************************************************//
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	private String riq1;
	public void setRiq1(String value) {
		riq1 = value;
	}
	public String getRiq1() {
		if ("".equals(riq1) || riq1 == null) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setOriRiq1(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	public String getOriRiq1() {
		return ((Visit) getPage().getVisit()).getString2();
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = "0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}else{
			((Visit) getPage().getVisit()).setboolean3(false);
		}
	}
	
	private String gongysmc="";
	public String getGongysmc(){
		if(this.gongysmc==null){
			return "";
		}
		return this.gongysmc;
	}
	public void setGongysmc(String gongysmc){
		this.gongysmc=gongysmc;
	}
	
	private String checklc="";
	public void setChecklc(String checklc){
		this.checklc=checklc;
	}
	public String getChecklc(){
		
		if(this.checklc==null ||  this.checklc.equals("")){
			return "false";
		}
		return this.checklc;
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
//	设定默认流程
	private boolean _InsertChick=false;
	public void InsertButton(IRequestCycle cycle){
		_InsertChick=true;
	}
	
//	选择销售单
	private boolean _XiaoSChick=false;
	public void XiaoSButton(IRequestCycle cycle){
		_XiaoSChick=true;
	}
	
//	选择销采购单
	private boolean _CaiGChick=false;
	public void CaiGButton(IRequestCycle cycle){
		_CaiGChick=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save(cycle);
			getSelectData();
		} else if(_InsertChick){
			_InsertChick=false;
//			设定默认合同
			shedlc();
			getSelectData();
		} else if(_XiaoSChick){
			_XiaoSChick=false;
//			选择结算单
			xiaosjs();
			getSelectData();
		} else if(_CaiGChick){
			_CaiGChick=false;
//			选择结算单
			caigjs();
			getSelectData();
		}
	}
	
//	设定默认流程
	public void shedlc(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="";
		String het="";
		
//		取到当前页面销售的默认合同的设定值
		String het_id_xs=MainGlobal.getProperId_String(this.getILiucmcModel(), this.getLiucmc());
		
//		设定销售合同
		if(!het_id_xs.equals("-1")){
			
//			用户在页面设置了销售默认合同
			het=MainGlobal.getXitxx_item("结算", xiaosdd_fgs
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(het.equals("")){
				
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
					xiaosdd_fgs+"', "+het_id_xs+", '', '结算', 1, '使用');	\n";
			}else{
				sql+= "update xitxxb set zhi='"+het_id_xs+"' where mingc='"+xiaosdd_fgs+"';	\n";
			}
		} else {
			sql += "delete xitxxb where mingc = '"+xiaosdd_fgs+"';\n";
		}
		
//		取得当前页面采购的默认合同的设定值
		String het_id_cg=MainGlobal.getProperId_String(this.getILiucmcModel1(), this.getLiucmc1());
		
//		设定采购合同
		if(!het_id_cg.equals("-1")){
			
//			用户在页面设置了采购默认合同
			het=MainGlobal.getXitxx_item("结算", caigdd_dc
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(het.equals("")){
				
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
						caigdd_dc+"', "+het_id_cg+", '', '结算', 1, '使用');	\n";
			}else{
				
				sql+= "update xitxxb set zhi='"+het_id_cg+"' where mingc='"+caigdd_dc+"';	\n";
			}
		} else {
			sql += "delete xitxxb where mingc = '"+caigdd_dc+"';\n";
		}
		
		if(sql.length()>0){
			sql=" begin \n"+sql+" end ;";
			int a=con.getUpdate(sql);
			
			if(a>=0){
				this.setMsg("合同默认状态<br>更新成功!");
			}
			con.Close();
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();

			setLiucmcValue1(null);
			setILiucmcModel1(null);
			getILiucmcModels1();
		}
	}
	
//	选择销售单
	public void xiaosjs(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="";
		String isCZ="";
		
//		取到当前页面分公司销售单是否被选中
		String xiaosd=getZhi1();
		
		if(!xiaosd.equals("")){			
//			用户在页面选中了分公司销售单
			isCZ=MainGlobal.getXitxx_item("结算", isXiaos_cz
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(isCZ.equals("")){
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
					isXiaos_cz+"', '是', '', '结算', 1, '使用')\n";
			}else{
				sql+= "update xitxxb set zhi='是' where mingc='"+isXiaos_cz+"'\n";
			}
		} 
		else {
			sql+= "update xitxxb set zhi = '否' where mingc='"+isXiaos_cz+"'	\n";
		}
		
		if(sql.length()>0){
//			sql=" begin \n"+sql+" end ;";
			int a=con.getUpdate(sql);
			
			con.Close();
			
			setZhi1("");
//			setZhi2("");
		}
	}
	
//	选采购单
	public void caigjs(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="";
		String isCZ="";
		
//		取到当前页面电厂采购单是否被选中
		String caigd=getZhi2();
		
		if(!caigd.equals("")){			
//			用户在页面选中了电厂采购单
			isCZ=MainGlobal.getXitxx_item("结算", isCaig_cz
					, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
			
			if(isCZ.equals("")){
				sql+=	"insert into xitxxb\n" +
					"  (id, xuh, diancxxb_id, mingc, zhi, danw, leib, zhuangt, beiz)\n" + 
					"values\n" + 
					"  (getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+"), \n" +
					"  	(select max(xuh)+1 from xitxxb), "+((Visit) getPage().getVisit()).getDiancxxb_id()+", '"+
					isCaig_cz+"', '是', '', '结算', 1, '使用')\n";
			}else{
				sql+= "update xitxxb set zhi='是' where mingc='"+isCaig_cz+"'\n";
			}
		} 
		else {
			sql+= "update xitxxb set zhi = '否' where mingc='"+isCaig_cz+"'\n";
		}
		
		if(sql.length()>0){
//			sql=" begin \n"+sql+" end ;";
			int a=con.getUpdate(sql);
			
			con.Close();
			
//			setZhi1("");
			setZhi2("");
		}
	}
	
	public void UpdateSetKuangfjsmkb(JDBCcon con,String TableName,String TableID){
		StringBuffer bf = new StringBuffer();
		bf.append("update "+TableName+" set zhuangt = 1 where id = "+TableID+"\n");
		con.getUpdate(bf.toString());
	}

	public String getHetid(String xx,int leix){
		String id = "";
		JDBCcon con = new JDBCcon();
		String sql_xs = 
			"select id from( \n" +
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j,\n" + 
			"       hetb h,\n" + 
			"       gongysb g,\n" + 
			"       hetjgb jg,\n" + 
			"       (select d.quanc\n" + 
			"          from diancxxb d\n" + 
			"         where d.id in (108)) dc\n" + //108=select d.fuid from diancxxb d where id = "+((Visit) getPage().getVisit()).getDiancxxb_id()+"
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = "+leix+"\n" + 
			"   and dc.quanc = g.quanc\n" + 
			" group by h.id, h.hetbh, g.mingc)\n" +
			" where xx = '"+xx+"'";
		ResultSet rs = con.getResultSet(sql_xs);
		try {	
			while(rs.next()){
				id = rs.getString("id");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return id;
	}
	
	public void save(IRequestCycle cycle) {
//		setMsg(getZhi1()+getZhi2());
		Visit visit = (Visit) getPage().getVisit();
		String kuangfjsmkbid = "";
		String kuangfjsyfbid = "";
		String xiaosht = "";
		String caight = "";
		
		
		String[] raw1=this.getChange().split(",");//分别获取 kuangfjsmkbid kuangfjsyfbid xiaosht caight
		kuangfjsmkbid=raw1[0];
		kuangfjsyfbid=raw1[1];
		xiaosht = raw1[2];		
		if(raw1.length>=4){
			caight = raw1[3];
		}
		
		if(!xiaosht.equals("")&&xiaosht!=null){
			xiaosht = getHetid(xiaosht,1);
		}else{
			xiaosht = "-1";
		}
		
		if(!caight.equals("")&&caight!=null){
			caight = getHetid(caight,1);
		}else{
			caight = "-1";
		}
		
		visit.setString3(kuangfjsmkbid+","+kuangfjsyfbid+","+xiaosht+","+caight);
		
		cycle.activate("Fengsxsd");
		
	}
	
	private StringBuffer getBaseSql(){
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		String diancxxb_id=this.getTreeid();
		
		String str_js="";	//where条件
		String isOver = ""; //where条件 判断是否已完成审核
		
		if(!diancxxb_id.equals("0")){
			
			str_js+=" and d.id="+diancxxb_id+"\n";
		
		}
		
		if(getWeizSelectValue().getId()!=0){
			isOver = "	where liuczt = '"+getWeizSelectValue().getValue()+"'\n";
		}
		
		String sql = 
			"select distinct *\n" +
			"  from (select js.id jiesbid,\n" + 
			"               jy.id jiesyfbid,\n" + 
			"               j.id jsid,\n" + 
			"               dj.id diancjsid,\n" + 
			"               '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || js.bianm || '\"' || ')>' || js.bianm || '</a>' kfbianh,\n" + 
			"               js.gongysmc gongysmc,\n" + 
			"               d.mingc dianc,\n" + 
			"               ht.hetbh hetbh,\n" + 
			"				decode(lc.leibztb_id,1,'已完成','未完成') as liuczt,\n" +
			"               fl.mingc jieslx,\n" + 
			"               js.hansdj hansdj,\n" + 
			"               js.fengsjj fengsjj,\n" + 
			"               js.hansmk hansmc,\n" + 
			"               jy.hansyf hansyf,\n" + 
			"               GetFengsxsht(js.id,"+((Visit) getPage().getVisit()).getDiancxxb_id()+") as xiaosht,\n" + 
			"               GetDCcght(js.id) as caight,\n" + 
			"                '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || dj.bianm || '\"' || ')>' || dj.bianm || '</a>' dcbianh,\n" + 
			"				dj.bianm as xiaosdbm,\n" + 
			"                 '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || j.bianm || '\"' || ')>' || j.bianm || '</a>' jsbianh,\n" + 
			"				j.bianm as caigdbm\n" +
			"          from kuangfjsmkb js, kuangfjsyfb jy, jiesb j,diancjsmkb dj,diancxxb d, hetb ht, feiylbb fl,liucztb lc\n" + 
			"         where (js.diancxxb_id = d.id)\n" + 
			"           and fl.id(+) = js.jieslx\n" + 
			"           and js.hetb_id = ht.id(+)\n" + 
			"           and jy.diancjsmkb_id(+) = js.id\n" + 
			"           and js.fuid = 0\n" + 
			"           and js.id = j.kuangfjsmkb_id(+)\n" + 
			"           and js.diancjsmkb_id = dj.id(+)\n" + 
			"			and js.liucztb_id = lc.id(+)\n" +
			"           --and nvl(js.diancjsmkb_id, 0) = 0\n" + 
			str_js +
			"  			and js.jiesrq>="+DateUtil.FormatOracleDate(this.getRiq())+" and js.jiesrq<="+DateUtil.FormatOracleDate(this.getRiq1())+"\n" +
			"        union\n" + 
			"        select js.id jiesbid,\n" + 
			"               jy.id jiesyfbid,\n" + 
			"               j.id jsid,\n" + 
			"               dy.id diancjsid,\n" + 
			"               '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || jy.bianm || '\"' || ')>' || jy.bianm || '</a>' kfbianh,\n" + 
			"               jy.gongysmc gongysmc,\n" + 
			"               d.mingc dianc,\n" + 
			"               ht.hetbh hetbh,\n" + 
			"				decode(lc.leibztb_id,1,'已完成','未完成') as liuczt,\n" +
			"               fl.mingc jieslx,\n" + 
			"               js.hansdj hansdj,\n" + 
			"               js.fengsjj fengsjj,\n" + 
			"               js.hansmk hansmc,\n" + 
			"               jy.hansyf hansyf,\n" + 
			"               GetFengsxsht(js.id,"+((Visit) getPage().getVisit()).getDiancxxb_id()+") as xiaosht,\n" + 
			"               GetDCcght(js.id) as caight,\n" + 
			"                '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || dy.bianm || '\"' || ')>' || dy.bianm || '</a>' dcbianh,\n" + 
			"				dy.bianm as xiaosdbm,\n" + 
			"                 '<a style=\"cursor:hand\" onclick=chak(' || '\"' || 'dianc,' || j.bianm || '\"' || ')>' || j.bianm || '</a>' jsbianh,\n" + 
			"				j.bianm as caigdbm\n" +
			"          from kuangfjsmkb js,\n" + 
			"               kuangfjsyfb jy,\n" + 
			"               diancjsyfb dy,\n" + 
			"               jiesyfb j,\n" + 
			"               diancxxb d,\n" + 
			"               (select id, hetbh\n" + 
			"                  from hetb\n" + 
			"                union\n" + 
			"                select id, hetbh from hetys) ht,\n" + 
			"               feiylbb fl,liucztb lc\n" + 
			"         where jy.diancxxb_id = d.id\n" + 
			"           and fl.id(+) = jy.jieslx\n" + 
			"           and jy.hetb_id = ht.id(+)\n" + 
			"           and jy.diancjsmkb_id = js.id(+)\n" + 
			"           and jy.fuid = 0\n" + 
			"           and jy.id = j.kuangfjsyfb_id(+)\n" + 
			"           and jy.diancjsyfb_id = dy.id(+)\n" +
			"			and js.liucztb_id = lc.id(+)\n" +
			"           --and nvl(jy.diancjsyfb_id, 0) = 0\n" + 
			str_js +
			"  			and jy.jiesrq>="+DateUtil.FormatOracleDate(this.getRiq())+" and jy.jiesrq<="+DateUtil.FormatOracleDate(this.getRiq1())+")\n" +
			isOver +
			" order by dcbianh\n";

		bf.append(sql);
		
		return bf;
		
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sSql=this.getBaseSql().toString();
		
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("jiesbid").setHidden(true);
		egu.getColumn("jiesbid").setEditor(null);
		egu.getColumn("jiesyfbid").setHidden(true);
		egu.getColumn("jiesyfbid").setEditor(null);
		egu.getColumn("jsid").setHidden(true);
		egu.getColumn("jsid").setEditor(null);
		egu.getColumn("diancjsid").setHidden(true);
		egu.getColumn("diancjsid").setEditor(null);
		egu.getColumn("kfbianh").setHeader("结算编号");
		egu.getColumn("kfbianh").setEditor(null);
		egu.getColumn("kfbianh").setWidth(120);
		egu.getColumn("gongysmc").setHeader("供应商");
		egu.getColumn("gongysmc").setEditor(null);
		egu.getColumn("dianc").setHeader("电厂");
		egu.getColumn("dianc").setEditor(null);
		egu.getColumn("hetbh").setHeader("合同编号");
		egu.getColumn("hetbh").setEditor(null);
		egu.getColumn("liuczt").setHeader("流程状态");
		egu.getColumn("liuczt").setEditor(null);
		egu.getColumn("jieslx").setHeader("结算类型");
		egu.getColumn("jieslx").setEditor(null);
		egu.getColumn("hansdj").setHeader("煤款含税单价");
		egu.getColumn("hansdj").setEditor(null);
		egu.getColumn("fengsjj").setHeader("煤款分公司加价");
		egu.getColumn("fengsjj").setEditor(null);
		egu.getColumn("hansmc").setHeader("含税煤款");
		egu.getColumn("hansmc").setEditor(null);
		egu.getColumn("hansyf").setHeader("含税运费");
		egu.getColumn("hansyf").setEditor(null);
		egu.getColumn("xiaosht").setHeader("选择分公司销售合同");
		egu.getColumn("xiaosht").setWidth(250);
		egu.getColumn("caight").setHeader("选择电厂采购合同");
		egu.getColumn("caight").setWidth(250);
		egu.getColumn("dcbianh").setHeader("分公司销售结算编号");
		egu.getColumn("dcbianh").setEditor(null);
		egu.getColumn("dcbianh").setWidth(120);
		egu.getColumn("xiaosdbm").setHidden(true);
		egu.getColumn("xiaosdbm").setEditor(null);
		egu.getColumn("jsbianh").setHeader("电厂采购结算编号");
		egu.getColumn("jsbianh").setEditor(null);
		egu.getColumn("jsbianh").setWidth(120);
		egu.getColumn("caigdbm").setHidden(true);
		egu.getColumn("caigdbm").setEditor(null);
//		egu.getColumn("chak").setHeader("查看");
//		egu.getColumn("chak").setEditor(null);
		
		//选择销售合同
		egu.getColumn("xiaosht").setEditor(new ComboBox());
		String sql_xs = 
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j,\n" + 
			"       hetb h,\n" + 
			"       gongysb g,\n" + 
			"       hetjgb jg,\n" + 
			"       (select d.quanc\n" + 
			"          from diancxxb d\n" + 
			"         where d.id in (108)) dc\n" + //108=select d.fuid from diancxxb d where id = "+((Visit) getPage().getVisit()).getDiancxxb_id()+"
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 1\n" + 
			"   and dc.quanc = g.quanc\n" + 
			" group by h.id, h.hetbh, g.mingc\n";
		egu.getColumn("xiaosht").setComboEditor(egu.gridId,new IDropDownModel(sql_xs));	
		egu.getColumn("xiaosht").setReturnId(true);
		
		//选择采购合同
		egu.getColumn("caight").setEditor(new ComboBox());
		String sql_cg = 
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j, hetb h, gongysb g, hetjgb jg\n" + 
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 0\n" + 
			" group by h.id, h.hetbh, g.mingc\n";
		egu.getColumn("caight").setComboEditor(egu.gridId,new IDropDownModel(sql_cg));	
		egu.getColumn("caight").setReturnId(true);
		
//***********************************************工具栏********************************************************************
		egu.addTbarText("结算日期:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","forms[0]");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		
		egu.addTbarText("至");
		
		DateField dEnd = new DateField();
		dEnd.Binding("RIQ1","forms[0]");
		dEnd.setValue(getRiq1());
		egu.addToolbarItem(dEnd.getScript());
		
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc,
//				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		
		ExtTreeUtil etu=new ExtTreeUtil("diancTree");
		etu.defaultSelectid=this.getTreeid();
		//etu.getDefaultTree("diancTree", con.getResultSetList(this.getDTsql().toString()), false);
		this.getDefaultTree(etu, "diancTree", con.getResultSetList(this.getDTsql().toString()), false);
		
		etu.window = new Window("diancTree");
		etu.window.setItems("diancTree"+"_treePanel");
		TreeButton tb=new TreeButton("确定","function(){" +
				" var cks = diancTree_treePanel.getSelectionModel().getSelectedNode();\n" +
				" if(cks==null){diancTree_window.hide();return;}\n" +
				" var obj0 = document.getElementById('diancTree_id');obj0.value = cks.id;diancTree_text.setValue(cks.text);\n" +
//				" if(cks.leaf){ document.all.diancmc.value=cks.parentNode.text;\n"+
//				" document.all.gongysmc.value=cks.text;}\n"+
//				" else{ document.all.diancmc.value=cks.text;\n"+
//				" document.all.gongysmc.value='';}\n"+
				" diancTree_window.hide();\n" +
				"document.forms[0].submit();}");
		//etu.addBbarButton(TreeButton.ButtonType_Window_Ok, "SaveButton");
		etu.addBbarButton(tb);
		etu.setWidth(200);
		etu.setTitle("单位选择");
		
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("流程状态");
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(80);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
		WeizSelect.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		egu.addToolbarItem(WeizSelect.getScript());

		egu.addTbarText("-");
		
//		选择计算下拉复选款
		String isXz_x = "false";//判断页面刷新后分公司销售单是否被选中
		String isXz_c = "false";//判断页面刷新后电厂采购单是否被选中
		
		String xs_cz = MainGlobal.getXitxx_item("结算", isXiaos_cz, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		String cg_cz = MainGlobal.getXitxx_item("结算", isCaig_cz, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		if(xs_cz!=null && !xs_cz.equals("")){
			if(xs_cz.equals("是")){
				isXz_x = "true";
				setZhi1("分公司销售单");
			}
		}
		if(cg_cz!=null && !cg_cz.equals("")){
			if(cg_cz.equals("是")){
				isXz_c = "true";
				setZhi2("电厂采购单");
			}
		}
		
		StringBuffer caozlx = new StringBuffer();
		caozlx.append(
				"{text:'操作类型',menu:new Ext.menu.Menu({\n" +
				"      items:[ {\n" + 
				"      id:'1',\n" + 
				"      text:'分公司销售单',\n" +
				"      checked: "+isXz_x+",\n" +
				"      checkHandler: onItemCheck,\n" + 
				"      minWidth:75\n" + 
				"    }\n" + 
				"    , {\n" + 
				"      id:'2',\n" + 
				"      text:'电厂采购单',\n" + 
				"      checked: "+isXz_c+",\n" +
				"      checkHandler: onItemCheck,\n" + 
				"      minWidth:75\n" + 
				"    }\n" + 
				"    ]})\n" + 
				"}");
		egu.addToolbarItem(caozlx.toString());
		
		String caozfn=
			"function onItemCheck(item, checked){\n" +
			"	if (item.text==\"分公司销售单\"){\n" + 
			"		if (checked){\n" + 
			"			document.all.item('ZHI1').value=item.text;\n" + 
			"		}else{\n" + 
			"	    	document.all.item('ZHI1').value=\"\";\n" + 
			"		}\n" + 
			"		document.all.item('XiaoSButton').click();	\n" +
			"	} else if (item.text==\"电厂采购单\"){\n" + 
			"		if (checked){\n" + 
			"			document.all.item('ZHI2').value=item.text;\n" + 
			"		}else{\n" + 
			"			document.all.item('ZHI2').value=\"\";\n" + 
			"		}\n" + 
			"		document.all.item('CaiGButton').click();	\n" +
			"	};\n" + 
//			"	document.all.item('XuanzButton').click();	\n" +
			"}";
		
		egu.addOtherScript(caozfn);
		
		egu.addTbarText("-");
		
//		设定默认合同
		String xiaos_va="";
		String caig_va="";
		
		String sx = MainGlobal.getXitxx_item("结算", xiaosdd_fgs, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		String sc = MainGlobal.getXitxx_item("结算", caigdd_dc, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "");
		
		if(sx!=null && !sx.equals("")){
			xiaos_va=" Ext.getDom('dayzt1').value='"+sx+"';\n";
		}
		if(sc!=null && !sc.equals("")){
			caig_va=" Ext.getDom('dayzt2').value='"+sc+"';\n";
		}
		egu.addOtherScript(xiaos_va+caig_va);
		
		String gb3_fs="function(){  \n"
//			+	"var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n"
//			+   " if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('提示信息','请先选择记录再进行操作!');return;}\n"
//			+"   document.all.CHANGE.value='';\n"
//			+   " for(i = 0; i< Mrcd.length; i++){\n"
//			+   " var rc=Mrcd[i]; document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID');\n"
//			+   " if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n"
//			+   "}\n"
			
			+ " if(!win){	\n" 
			+ "	\tvar form = new Ext.form.FormPanel({	\n" 
			+ " \tbaseCls: 'x-plain',	\n" 		
			+ " \tlabelAlign:'right',	\n" 
			+ " \tdefaultType: 'textfield',	\n"
			+ " \titems: [{		\n"
			+ " \txtype:'fieldset',	\n"
			+ " \ttitle:'请选择默认合同',	\n"
			+ " \tautoHeight:false,	\n"
			+ " \theight:220,	\n"
			+ " \titems:[	\n"
			
			+ " \tlcmccb=new Ext.form.ComboBox({	\n" 
			+ " \twidth:250,	\n"
			+ " \tid:'lcmccb',	\n"
			+ " \tselectOnFocus:true,	\n"
			+ "	\ttransform:'LiucmcDropDown',	\n"		
			+ " \tlazyRender:true,	\n"	
			+ " \tfieldLabel:'分公司销售<br>默认合同',		\n" 
			+ " \ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"	
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false	\n"					
			+ " \t}),	\n"	
			
			+ " \tlcmccb1=new Ext.form.ComboBox({	\n" 
			+ " \twidth:250,	\n"
			+ " \tid:'lcmccb1',	\n"
			+ " \tselectOnFocus:true,	\n"
			+ "	\ttransform:'LiucmcDropDown1',	\n"		
			+ " \tlazyRender:true,	\n"	
			+ " \tfieldLabel:'电厂采购<br>默认合同',		\n" 
			+ " \ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"	
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false	\n"					
			+ " \t})	\n"
			
			+ " \t]		\n"
			+ " \t}]	\n"		
			+ " \t});	\n"
			
			+ " \twin = new Ext.Window({	\n"
			+ " \tel:'hello-win',	\n"
			+ " \tlayout:'fit',	\n"
			+ " \twidth:500,	\n"	
			+ " \theight:300,	\n"
			+ " \tcloseAction:'hide',	\n"
			+ " \tplain: true,	\n"
			+ " \ttitle:'合同',	\n"
			+ " \titems: [form],	\n"
			+ " \tbuttons: [{	\n"
			+ " \ttext:'确定',	\n"
			+ " \thandler:function(){	\n"  
			+ " \twin.hide();	\n"
			
//			+ " \tif(lcmccb.getRawValue()=='请选择' && lcmccb1.getRawValue()=='请选择'){		\n" 
//			+ "	\t	alert('请选择流程名称！');		\n"
//			+ " \t}else{" 
			+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
			+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE1').value=lcmccb1.getRawValue();	\n"
			+ " \t\t document.all.item('InsertButton').click();	\n"
			+ " Ext.Msg.show({title: 'Please wait',msg: '正在加载数据...',progressText: '数据加载中...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'数据加载中……','正在加载数据');},i*100);"
//			+ " \t}	\n"	
			
			+ " \t}	\n"
			+ " \t},{	\n"
			+ " \ttext: '取消',	\n"
			+ " \thandler: function(){	\n"
			+ " \twin.hide();	\n"
//			+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"
//			+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE1').value='';	\n"
			+ " \t}		\n"
			+ " \t}]	\n"
			+ " \t});}	\n" 
			+ " \twin.show(this);	\n"	

//			+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"	
//			//+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"	
//			+ " \t}	\n"	
			+ " \t}";
		GridButton gbt3 = new GridButton("设定默认合同",gb3_fs);
		gbt3.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt3);
		
		egu.addTbarText("-");
		
		String gb_js="function(){\n" +
		" var Mrcd = gridDiv_grid.getSelectionModel().getSelections(); \n" +
		" if( Mrcd==null || Mrcd.length<=0){Ext.Msg.alert('提示信息','请先选择记录再进行操作!');return;}\n" +
		" document.all.CHANGE.value='';\n" +
		
		"var leix1=document.all.item('ZHI1').value;\n" +//销售单
		"var leix2=document.all.item('ZHI2').value;\n" +//采购单
		"var het1= Ext.getDom('dayzt1').value;\n" +//销售合同//document.getElementById('TEXT_LIUCMCSELECT_VALUE').value;
		"var het2= Ext.getDom('dayzt2').value;\n" +//采购合同//document.getElementById('TEXT_LIUCMCSELECT_VALUE1').value;
		"var xsht = '';\n" +
		"var cght = ''\n" +
		
		" for(i = 0; i< Mrcd.length; i++){\n" +
		" var rc=Mrcd[i]; \n" +
		//判断是否选中销售单,采购单
		"if (leix1=='' && leix2==''){" +
		" Ext.Msg.alert('提示信息','请先选择类型!');return; " +
		"}\n" +
		//判断是否选中销售单
		" if (!leix1==''){\n" +
			"if(rc.get('XIAOSDBM')==null || rc.get('XIAOSDBM')==''){\n" +
				"if(rc.get('XIAOSHT')==null || rc.get('XIAOSHT')==''){\n" +
					//判断默认合同
					" if (het1==-1){\n" +
						" Ext.Msg.alert('提示信息','请先选择销售合同!');return;  \n" +
					"} else {\n" +	
						" xsht = het1;\n" +
					"}\n" +
				"} else {\n" +
					" xsht = rc.get('XIAOSHT');\n" +
				"}\n" +
			"} else {\n" +
				" Ext.Msg.alert('提示信息','分公司销售单不能重复生成!');return;  \n" +
			"}\n" +
		"}" +
		//判断是否选中采购单		
		" if (!leix2==''){" +
			"if(rc.get('CAIGDBM')==null || rc.get('CAIGDBM')==''){\n" +
				"if(rc.get('CAIGHT')==null || rc.get('CAIGHT')==''){\n" +
					//判断默认合同
					" if (het2==-1){\n" +
						" Ext.Msg.alert('提示信息','请先选择采购合同!');return;  \n" +
					"} else {\n" +
						" cght = het2;\n" +
					"}\n" +
				"} else {\n" +
					" cght = rc.get('CAIGHT');\n" +
				"}\n" +
			"} else {\n" +
				" Ext.Msg.alert('提示信息','电厂采购单单不能重复生成!');return;  \n" +
			"}\n" +
		"}" +
		" document.all.CHANGE.value+=rc.get('JIESBID')+','+rc.get('JIESYFBID')+','+xsht+','+cght;\n" +

		" if(i!=Mrcd.length-1){ document.all.CHANGE.value+=';';}\n" +
		" }\n" +
		" document.all.item('SaveButton').click();" +
//		" Ext.Msg.progress('提示信息','请等待','数据加载中……');\n"+
		" Ext.Msg.show({title: 'Please wait',msg: '正在加载数据...',progressText: '数据加载中...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'数据加载中……','正在加载数据');},i*100);"+
		"\n}";
			
		GridButton gbt = new GridButton("计算",gb_js);
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		setExtGrid(egu);
		con.Close();
	}
	
	
//	销售合同
//	合同信息 DropDownBean8  
//  合同信息 ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql=
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j,\n" + 
			"       hetb h,\n" + 
			"       gongysb g,\n" + 
			"       hetjgb jg,\n" + 
			"       (select d.quanc\n" + 
			"          from diancxxb d\n" + 
			"         where d.id in (select d.fuid from diancxxb d where id = "+((Visit) getPage().getVisit()).getDiancxxb_id()+")) dc\n" + 
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 1\n" + 
			"   and dc.quanc = g.quanc\n" + 
			" group by h.id, h.hetbh, g.mingc\n";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   合同信息 end
	
	//采购合同
//	合同信息 DropDownBean9  
//  合同信息 ProSelectionModel9
	public IDropDownBean getLiucmcValue1() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit()).setDropDownBean9((IDropDownBean) getILiucmcModel1().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setLiucmcValue1(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean9()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean9(value);
		}
	}

	public void setILiucmcModel1(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getILiucmcModel1() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			
			getILiucmcModels1();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public IPropertySelectionModel getILiucmcModels1() {
		
		String sql="";
		sql=
			"select distinct h.id, h.hetbh || ' ' || g.mingc || ' ' || min(jg.jij) as xx\n" +
			"  from kuangfjsmkb j, hetb h, gongysb g, hetjgb jg\n" + 
			" where j.jihkjb_id = h.jihkjb_id\n" + 
			"   and h.gongysb_id = g.id\n" + 
			"   and jg.hetb_id = h.id\n" + 
			"   and j.yansksrq >= h.qisrq\n" + 
			"   and j.yansjzrq <= h.guoqrq\n" + 
			"   and h.leib = 0\n" + 
			" group by h.id, h.hetbh, g.mingc\n";
		((Visit) getPage().getVisit()).setProSelectionModel9(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
//   合同信息 end
	
	
	public void getDefaultTree(ExtTreeUtil etu,String treeId,ResultSetList rsl,boolean checkbox) {
		
		etu.treeId=treeId;
		etu.init();
		TreeNode parentNode = null;
		TreeNode RootNode = null;
		int lastjib = 0;
		while(rsl.next()) {
			int curjib = rsl.getInt(2);
			TreeNode node = new TreeNode(rsl.getString(0),rsl.getString(1));
			node.setCheckbox(checkbox);
			if(parentNode==null) {
				RootNode = node;
				node.setCheckbox(false);
				parentNode = node;
				lastjib = curjib+1;
				continue;
			}
			if(lastjib < curjib) {
				parentNode = (TreeNode)parentNode.getLastChild();
			}else if(lastjib > curjib){
				for(int i=0;i<lastjib - curjib;i++)
					parentNode = (TreeNode)parentNode.getParentNode();
			}
			lastjib = curjib;
			parentNode.appendChild(node);
		}
		etu.setRootNode(RootNode);
	}
	
	
	//获得电厂 供应商的电厂树形结构的sql
	private StringBuffer getDTsql(){
		
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select d.id,d.mingc,1 jib from jiesb js,diancxxb d where js.diancxxb_id=d.id\n");
		bf.append(" and nvl(js.diancjsmkb_id,0) not in (select nvl(dm.id,null) from diancjsmkb dm )\n");
		bf.append(" union \n");
		bf.append(" select d.id,d.mingc,1 jib from jiesyfb jf,diancxxb d  where jf.diancxxb_id=d.id \n");
		bf.append(" and nvl(jf.diancjsyfb_id,0) not in (select nvl(dy.id,null) from diancjsyfb dy)\n");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
	
		return bf;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			
			visit.setString14(visit.getActivePageName());
			visit.setActivePageName(getPageName().toString());
//			visit.setboolean2(false);//计算类型
			visit.setboolean3(true);
//			2
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			getWeizSelectModel();
			this.setRiq("");
			this.setRiq1("");
			
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();
			
			setLiucmcValue1(null);
			setILiucmcModel1(null);
			getILiucmcModels1();
			
			setZhi1("");
			setZhi2("");
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
	
//	流程状态
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "全部"));
		list.add(new IDropDownBean(1, "已完成"));
		list.add(new IDropDownBean(2, "未完成"));
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
	}

}