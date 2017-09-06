package com.zhiren.dc.monthReport.tb;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuejsdjtb_dk extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString9();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString9(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString10());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString10();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString10(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}

	private void Save() {
		if(getChange() == null || "".equals(getChange())){
			setMsg("没有进行改动无需保存！");
			return ;
		}
		StringBuffer sql = new StringBuffer();
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		while(rs.next()){
			double jiesl = 0;
			double meij = 0;
			double meijs = 0;
			double yunj = 0;
			double yunjs = 0;
			double daozzf = 0;
			double zaf = 0;
			double qit = 0;
			double kuangqyf = 0;
			double qnet_ar = 0;
			double biaomdj = 0;
			double buhsbmdj = 0;
			double _qnet_ar = rs.getDouble("qnet_ar");
			double _biaomdj = 0;
			double _buhsbmdj = 0;
			if(_qnet_ar>0){
				_biaomdj = rs.getDouble("biaomdj");
				_buhsbmdj = rs.getDouble("buhsbmdj");
			}
			
			if("".equals(rs.getString("id")) || "0".equals(rs.getString("id"))){
				sql.append("insert into YUEJSBMDJ_DK(id, fenx, yuetjkjb_id, jiesl, meij, meijs, kuangqyf, " +
					  "                yunj, yunjs, daozzf, zaf, qit, qnet_ar, biaomdj, buhsbmdj)\n" +
					  "values(\n" +
					  "getnewid("+getTreeid()+"),\n" +
					  "'" +rs.getString("fenx")+"',\n" +
					  rs.getString("yuetjkjb_id")+",\n" +
					  rs.getDouble("jiesl")+",\n" +
					  rs.getDouble("meij")+",\n" +
					  rs.getDouble("meijs")+",\n" +
					  rs.getDouble("kuangqyf")+",\n" +
					  rs.getDouble("yunj")+",\n" +
					  rs.getDouble("yunjs")+",\n" +
					  rs.getDouble("daozzf")+",\n" +
					  rs.getDouble("zaf")+",\n" +
					  rs.getDouble("qit")+",\n" +
					  _qnet_ar+",\n" +
					  _biaomdj+",\n" +
					  _buhsbmdj+"\n" +
					  ");\n");
			}else{
				jiesl = rs.getDouble("jiesl");
				meij = rs.getDouble("meij");
				meijs = rs.getDouble("meijs");
				yunj = rs.getDouble("yunj");
				yunjs = rs.getDouble("yunjs");
				daozzf = rs.getDouble("daozzf");
				zaf = rs.getDouble("zaf");
				qit = rs.getDouble("qit");
				kuangqyf = rs.getDouble("kuangqyf");
				qnet_ar = _qnet_ar;
				biaomdj = _biaomdj;
				buhsbmdj = _buhsbmdj;
				sql.append("update YUEJSBMDJ_DK set " +
						"jiesl = " + jiesl +
						",meij = " + meij + 
						",meijs = " + meijs +
						",yunj = " + yunj +
						",yunjs = " + yunjs +
						",daozzf = " + daozzf +
						",zaf = " + zaf +
						",qit = " + qit +
						",qnet_ar = " + qnet_ar +
						",biaomdj = " + biaomdj + 
						",buhsbmdj = " + buhsbmdj +
						",kuangqyf = " + kuangqyf +
						" where yuetjkjb_id =" + rs.getString("yuetjkjb_id") + " and fenx = '" + rs.getString("fenx") + "';");
			}
		}
		if(!"".equals(sql) && sql!=null){
			int flag = con.getUpdate("begin\n" + sql + "\n end;");
			if(flag!=-1){
				rs.beforefirst();
				sql.setLength(0);
				while(rs.next()){
					if("累计".equals(rs.getString("fenx")) && getIsSelectLike()){
						String sq = "select\n" +
							"round_new(sum(jiesl),0) as jiesl,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(meij*jiesl)/sum(jiesl)),2) meij,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(meijs*jiesl)/sum(jiesl)),2) meijs,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(yunj*jiesl)/sum(jiesl)),2) yunj,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(yunjs*jiesl)/sum(jiesl)),2) yunjs,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(daozzf*jiesl)/sum(jiesl)),2) daozzf,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(zaf*jiesl)/sum(jiesl)),2) zaf,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(qit*jiesl)/sum(jiesl)),2) qit,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(qnet_ar*jiesl)/sum(jiesl)),2) qnet_ar,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(biaomdj*jiesl)/sum(jiesl)),2) biaomdj,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(buhsbmdj*jiesl)/sum(jiesl)),2) buhsbmdj,\n" + 
							"round_new(decode(sum(jiesl),0,0,sum(kuangqyf*jiesl)/sum(jiesl)),2) kuangqyf\n" + 
							"  from YUEJSBMDJ_DK y,yuetjkjb yt,(select gongysb_id,jihkjb_iD,pinzb_id,yunsfsb_id from yuetjkjb where id= " + rs.getString("YUETJKJB_ID") + ")yt2\n" + 
							" where y.yuetjkjb_id=yt.id\n" + 
							"	and yt.gongysb_id=yt2.gongysb_id\n" +
							"	and yt.jihkjb_id=yt2.jihkjb_id\n" +
							"	and yt.pinzb_id=yt2.pinzb_id\n" +
							"	and yt.yunsfsb_id=yt2.yunsfsb_id\n" +
							"   and yt.riq>=to_date('"+ getNianf() + "-01-01" + "','yyyy-mm-dd')\n" + 
							"   and yt.riq<=to_date('"+ getNianf() + "-" + getYuef() + "-01" + "','yyyy-mm-dd')\n" + 
							"   and y.fenx='" + SysConstant.Fenx_Beny + "'\n" + 
							"   and yt.diancxxb_id=" + getTreeid();
						ResultSetList rsl = con.getResultSetList(sq);
						rsl.next();
						double daoczhj = rsl.getDouble("meij") + rsl.getDouble("yunj") + rsl.getDouble("daozzf") + rsl.getDouble("zaf") + rsl.getDouble("qit") + rsl.getDouble("kuangqyf");
						double biaomdj = 0;
						double buhsbmdj = 0;
						
						if(rsl.getDouble("qnet_ar")!=0){
							biaomdj = daoczhj*29.271/rsl.getDouble("qnet_ar");
							buhsbmdj = (daoczhj-rsl.getDouble("meijs")-rsl.getDouble("yunjs"))*29.271/rsl.getDouble("qnet_ar");
						}
						
						sql.append("update YUEJSBMDJ_DK set " +
								"jiesl = " + rsl.getDouble("jiesl") +
								",meij = " + rsl.getDouble("meij") + 
								",meijs = " + rsl.getDouble("meijs") +
								",yunj = " + rsl.getDouble("yunj") +
								",yunjs = " + rsl.getDouble("yunjs") +
								",daozzf = " + rsl.getDouble("daozzf") +
								",zaf = " + rsl.getDouble("zaf") +
								",qit = " + rsl.getDouble("qit") +
								",qnet_ar = " + rsl.getDouble("qnet_ar") +
								",biaomdj = round_new("+ biaomdj + ",2)" +
								",buhsbmdj = round_new("+ buhsbmdj + ",2)" +
								",kuangqyf = " + rsl.getDouble("kuangqyf") +
								" where yuetjkjb_id =" + rs.getString("yuetjkjb_id") + " and fenx = '" + rs.getString("fenx") + "';");
					}
				}
				if("".equals(sql) || sql==null){
					setMsg("保存成功!");
				}else{
					flag = con.getUpdate("begin\n"+sql.toString() + "\n end;");
					if(flag!=-1){
						setMsg("保存成功!");
					}else{
						setMsg("保存成功,累计数计算失败!");
					}
				}
			}else{
				setMsg("保存失败!");
			}
			
		}
		
		rs.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
	}
	
	private void DelData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "delete from YUEJSBMDJ_DK where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ diancxxb_id
				+ ")";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}
	
	/**
	 * @param con
	 * @return   true:已上传状态中 不能修改数据 false:未上传状态中 可以修改数据
	 */
	private boolean getZhangt(JDBCcon con){
		String CurrODate =  DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from YUEJSBMDJ_DK s, yuetjkjb k\n" + 
			" where s.yuetjkjb_id = k.id\n" + 
			"   and k.diancxxb_id = "+getTreeid()+"\n" + 
			"   and k.riq = "+CurrODate;
		ResultSetList rs=con.getResultSetList(sql);
		boolean zt=true;
		if(con.getHasIt(sql)){
			while(rs.next()){
				if(rs.getInt("zhuangt")==0||rs.getInt("zhuangt")==2){
					zt=false;
				}
			}
		}else{
			zt=false;
		}
		return zt;
	}
	
	public void getSelectData() {
		String strDate="";
		strDate=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String strSql="";	
		String diancxxb_id=this.getTreeid();
		String rez_lie = "y";//YUEJSBMDJ_DK表
		if("是".equals(MainGlobal.getXitxx_item("月报", "结算标煤单价中的热值是入厂热值", diancxxb_id, "否"))){
			rez_lie = "z";//yuezlb表
		}
		strSql=

			"select y.id,s.gmingc,s.jmingc,s.pmingc,s.ysmingc,s.fenx,s.yuetjkjb_id,\n" +
			"             y.jiesl,y.meij,y.meijs,y.kuangqyf,y.yunj,y.yunjs,\n" + 
			"             y.daozzf,y.zaf,y.qit,\n" + 
			"             "+rez_lie+".qnet_ar,y.biaomdj,y.buhsbmdj from (\n" + 
			"      select x.fenx,y.id yuetjkjb_id,g.mingc gmingc,j.mingc jmingc,p.mingc pmingc,ys.mingc ysmingc\n" + 
			"        from yuetjkjb y,\n" + 
			"             (select decode(0,0,'"+SysConstant.Fenx_Beny+"') fenx\n" + 
			"                from dual\n" + 
			"              union\n" + 
			"              select decode(0,0,'"+SysConstant.Fenx_Leij+"') fenx from dual) x,\n" + 
			"             gongysb g,\n" + 
			"             jihkjb j,\n" + 
			"             pinzb p,\n" + 
			"             yunsfsb ys\n" + 
			"       where y.riq = "+DateUtil.FormatOracleDate(strDate)+"\n" + 
			"         and y.diancxxb_id=" + diancxxb_id +"\n" + 
			"         and y.gongysb_id=g.id\n" + 
			"         and y.jihkjb_id=j.id\n" + 
			"         and y.pinzb_id=p.id\n" + 
			"         and y.yunsfsb_id=ys.id\n" + 
			"      ) s,YUEJSBMDJ_DK y,yuezlb z\n" + 
			"      where y.yuetjkjb_id(+)=s.yuetjkjb_id\n" + 
			"        and y.fenx(+)=s.fenx\n" + 
			"        and z.yuetjkjb_id(+)=s.yuetjkjb_id\n" + 
			"        and z.fenx(+)=s.fenx\n" + 
			"      order by s.yuetjkjb_id,s.fenx";


		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("YUEJSBMDJ_DK");
		// /设置显示列名称
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight-30");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("yuetjkjb_id").setHeader("yuetjkjb_id");
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("yuetjkjb_id").setEditor(null);
		egu.getColumn("gmingc").setHeader("供应商");
		egu.getColumn("gmingc").setWidth(100);
		egu.getColumn("gmingc").setEditor(null);
		
		egu.getColumn("jmingc").setHeader("计划口径");
		egu.getColumn("jmingc").setWidth(80);	
		egu.getColumn("jmingc").setEditor(null);
		
		egu.getColumn("pmingc").setHeader("品种");
		egu.getColumn("pmingc").setWidth(60);
		egu.getColumn("pmingc").setEditor(null);
		
		egu.getColumn("ysmingc").setHeader("运输");
		egu.getColumn("ysmingc").setEditor(null);
		egu.getColumn("ysmingc").setWidth(60);
		
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		
		egu.getColumn("jiesl").setHeader("结算量");
		egu.getColumn("jiesl").setWidth(60);
//		egu.getColumn("jiesl").setEditor(null);
		
		egu.getColumn("meij").setHeader("含税煤价");
		egu.getColumn("meij").setWidth(60);	
//		egu.getColumn("meij").setEditor(null);
		
		egu.getColumn("meijs").setHeader("煤价税");
		egu.getColumn("meijs").setWidth(60);
//		egu.getColumn("meijs").setEditor(null);
		
		egu.getColumn("kuangqyf").setHeader("交货前杂费");
		egu.getColumn("kuangqyf").setWidth(60);
//		egu.getColumn("kuangqyf").setEditor(null);
		
		egu.getColumn("yunj").setHeader("含税运价");
		egu.getColumn("yunj").setWidth(60);
//		egu.getColumn("yunj").setEditor(null);
		
		egu.getColumn("yunjs").setHeader("运价税");
		egu.getColumn("yunjs").setWidth(60);
//		egu.getColumn("yunjs").setEditor(null);
		
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);
		
		egu.getColumn("zaf").setHeader("杂费");
		egu.getColumn("zaf").setWidth(60);
//		egu.getColumn("zaf").setEditor(null);
		
		egu.getColumn("qit").setHeader("其他");
		egu.getColumn("qit").setWidth(60);
		
		egu.getColumn("qnet_ar").setHeader("结算热量");
		egu.getColumn("qnet_ar").setWidth(60);
//		egu.getColumn("qnet_ar").setEditor(null);
		
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("biaomdj").setWidth(60);
		egu.getColumn("biaomdj").setEditor(null);
		
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		egu.getColumn("buhsbmdj").setWidth(100);
		egu.getColumn("buhsbmdj").setEditor(null);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.setDefaultsortable(false);    
		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("月份");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		egu.addToolbarItem(comb2.getScript());
//		egu.addOtherScript("YuefDropDown.on('select',function(){document.forms[0].submit();});");
		
		
		
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addToolbarItem(new ToolbarText("-").getScript());
		egu.addToolbarItem("{"+new GridButton("刷新","function (){document.getElementById('RefreshButton').click()}").getScript()+"}");
//		 生成按钮
//		GridButton gbc = new GridButton("生成",
//				getBtnHandlerScript("CreateButton"));
//		gbc.setIcon(SysConstant.Btn_Icon_Create);
//		egu.addTbarBtn(gbc);
		
//		判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if(getZhangt(con)){
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		}else{
	//		删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
	//		 保存按钮
			egu.addToolbarButton(GridButton.ButtonType_SaveAll,"SaveButton");
		}
		
		Checkbox cb=new Checkbox();
		cb.setId("SelectLike");
		cb.setListeners("check:function(own,checked){if(checked){document.all.SelectLike.value='true'}else{document.all.SelectLike.value='false'}}");
		egu.addToolbarItem(cb.getScript());
		egu.addTbarText("是否自动计算累计值");
		
		String meijs = MainGlobal.getXitxx_item("结算", "煤款税率", diancxxb_id, "0.17");
		String yunjs = MainGlobal.getXitxx_item("结算", "运费税率", diancxxb_id, "0.07");
		
		StringBuffer sb = new StringBuffer();
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n" + 
				"  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjs + ",2);\n" + 
				"    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n" + 
				"\n" + 
				"  if(e.field=='MEIJ'||e.field=='MEIJS'||e.field=='KUANGQYF'||e.field=='YUNJ'||e.field=='YUNJS'||e.field=='DAOZZF'||e.field=='ZAF'||e.field=='QIT'||e.field=='QNET_AR'){\n" + 
				"\n" + 
				"    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ')||0)+eval(gridDiv_ds.getAt(i).get('KUANGQYF')||0)+eval(gridDiv_ds.getAt(i).get('YUNJ')||0)+eval(gridDiv_ds.getAt(i).get('DAOZZF')||0)+eval(gridDiv_ds.getAt(i).get('ZAF')||0)+eval(gridDiv_ds.getAt(i).get('QIT')||0);\n" + 
				" 	if(eval(gridDiv_ds.getAt(i).get('QNET_AR'))!=0){\n" +
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS')||0)-eval(gridDiv_ds.getAt(i).get('YUNJS')||0))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"}\n" + 
				"    gridDiv_ds.getAt(i).set('BIAOMDJ',biaomdj);\n" + 
				"    gridDiv_ds.getAt(i).set('BUHSBMDJ',buhsbmdj);\n" + 
				"  }\n" + 
				
				"});"
		);
		egu.addOtherScript(sb.toString());

		egu.pagsize = 0;
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianfValue() + "年" + getYuefValue() + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的所有数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			visit.setboolean8(false);
			setRiq();
			setTreeid(null);
		}
		getSelectData();
	}
	
	 // 年份下拉框
    private static IPropertySelectionModel _NianfModel;
    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }
    
	private IDropDownBean _NianfValue;
	
    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }
    
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
	
	public boolean getIsSelectLike(){
		return ((Visit) this.getPage().getVisit()).getboolean8();
	}
	public String getSelectLike(){
		return ((Visit) this.getPage().getVisit()).getString8();
	}
	public void setSelectLike(String value){
		boolean flag = false;
		if("true".equals(value)){
			flag = true;
		}
		((Visit) this.getPage().getVisit()).setboolean8(flag);
	}
}