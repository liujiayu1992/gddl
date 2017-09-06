package com.zhiren.dc.diaoygl.xiecwh;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yang
 * 时间：2010-3-19
 * 描述：去掉煤种字段
 */
/**
 * @author yang
 * 时间：2010-3-26
 * 修改描述: 当时间相同时查询不到数据，将查询时间精确到分钟
 */

public class Rucxcwh extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, true);
	}

	protected void initialize() {
		super.initialize();
		setMsg(null);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// 保存
	private void Save() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sql = new StringBuffer(" ");
		// 电厂ID
		long diancID = visit.getDiancxxb_id();

		// 上煤班值表ID
		long shangmbzbID = 0;

		// 煤场表ID，默认0
		String meicID = "0";

		// 如果j=0则查询上煤班值表ID,查询后改变该值，在while循环中只进行一次查询
		int j = 0;

		// 保存标识，如果为true，则在begin end;中存在更新语句，最后执行保存
		boolean okSave = false;
		ResultSetList rsl = this.getExtGrid().getModifyResultSet(
				this.getChange());

		sql.append("begin\n");
		while (rsl.next()) {
			// 卸车方式：直上、外卸
			int zhis = 0, waix = 0;
			// 判断修改记录是否是选中状态，如果是则构建更新语句
			if (rsl.getString("zhuangt").equals("√")) {
				okSave = true;
				if (rsl.getString("XIECFS").equals("外卸")) {
					waix = 1;
				} else {
					zhis = 1;
				}
				if (j == 0) {
					// 卸车开始时间
					String ks = this.getXCKaissj();

					// 卸车截止时间
					String jz = this.getXCJiezsj();

					// 判断开始和截止时间是否正确
					if (DateUtil.getDateTime(ks).compareTo(
							DateUtil.getDateTime(jz)) > 0) {
						this.setMsg("'卸车时间范围错误：结束时间要大于等于开始时间！'");
						return;
					}
					// 根据卸车时间范围获得上煤班值表ID
					String sq = "select t.id from(\n"
							+ "select id,substr(to_char(s.kaissj,'yyyy-mm-dd HH24:mi'),12) k,substr(to_char(s.jiezsj,'yyyy-mm-dd HH24:mi'),12) j from shangmbzb s\n"
							+ ") t\n"
							+ "where to_date(t.k,'HH24:mi:ss')<to_date('"
							+ ks.substring(ks.indexOf(" ") + 1)
							+ "','HH24:mi:ss') and to_date(t.j,'HH24:mi:ss')>to_date('"
							+ jz.substring(jz.indexOf(" ") + 1)
							+ "','HH24:mi:ss')";

					ResultSetList rs = con.getResultSetList(sq);
					if (rs.next()) {
						shangmbzbID = rs.getLong(0);
					}
					rs.close();
					j = 1;
				}

				// String sql1 = "insert into
				// rucxcb(id,chepb_id,shangmbzb_id,waix,zhis,diaody,xieckssj,xiecjzsj,beiz)"
				// + " values(getnewid("
				// + diancID
				// + "),"
				// + rsl.getString("chepb_id")
				// + ","
				// + shangmbzbID
				// + ","
				// + waix
				// + ","
				// + zhis
				// + ",'"
				// + rsl.getString("DIAODY")
				// + "',to_date('"
				// + rsl.getDateTimeString("KAISSJ")
				// + "','yyyy-mm-dd HH24:mi'),to_date('"
				// + rsl.getDateTimeString("JIEZSJ")
				// + "','yyyy-mm-dd HH24:mi'),'"
				// + rsl.getString("BEIZ")
				// + "');\n";
				sql
						.append("insert into rucxcb(id,chepb_id,shangmbzb_id,waix,zhis,diaody,xieckssj,xiecjzsj,beiz)");
				sql.append(" values(getnewid(");
				sql.append(diancID);
				sql.append("),");
				sql.append(rsl.getString("chepb_id"));
				sql.append(",");
				sql.append(shangmbzbID);
				sql.append(",");
				sql.append(waix);
				sql.append(",");
				sql.append(zhis);
				sql.append(",'");
				sql.append(rsl.getString("DIAODY"));
				sql.append("',to_date('");
				sql.append(this.getXCKaissj());
				sql.append("','yyyy-mm-dd HH24:mi:ss'),to_date('");
				sql.append(this.getXCJiezsj());
				sql.append("','yyyy-mm-dd HH24:mi:ss'),'");
				sql.append(rsl.getString("BEIZ"));
				sql.append("');\n");

				// sql.append(sql1);

				// 如果选中外卸和煤场，则更新所对应车皮表chepb煤场表编号字段meicb_id
				if (!"".equals(rsl.getString("meic"))
						&& "外卸".equals(rsl.getString("XIECFS"))) {
					for (int i = 0; i < rsl.getColumnCount(); i++) {
						if ("MEIC".toUpperCase().equals(
								this.getExtGrid().getColumn(i).dataIndex
										.toUpperCase())) {
							meicID = this.getValueSql(this.getExtGrid()
									.getColumn(i), rsl.getString("meic"));
						}
					}
					// String sql2 = "update chepb c set c.meicb_id=" + meicID
					// + " where c.id=" + rsl.getString("chepb_id") + ";";
					sql.append("update chepb c set c.meicb_id=");
					sql.append(meicID);
					sql.append(" where c.id=");
					sql.append(rsl.getString("chepb_id"));
					sql.append(";\n");
				}
				// sql.append(sql2);
			}
		}
		sql.append("end;");
		
		//begin end中包含sql语句
		if (okSave) {
			okSave = false;
			con.getInsert(sql.toString());
		}
		rsl.close();
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
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "0" : value;
		} else {
			return value;
		}
	}

	// 保存
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	// 刷新
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

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		StringBuffer shead = new StringBuffer();
		shead
				.append(" [  {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n");
		shead.append("   {header:'车辆入厂信息', colspan:8},\n");
		shead
				.append(" {header:'卸车方式<br/>(直上/外卸)', align:'center',rowspan:2}, \n");
		shead.append(" {header:'煤场', align:'center',rowspan:2}, \n");
		shead.append(" {header:'调度员', align:'center',rowspan:2}, \n");
		shead.append(" {header:'卸车时间范围<br/>(日/时/分)',colspan:2}, \n");
		shead.append(" {header:'备注', align:'center',rowspan:2}, \n");
		shead.append(" {header:'车皮编号', align:'center',rowspan:2} \n");

		StringBuffer xhead = new StringBuffer();
		xhead.append(" [ {header:'序号', align:'center'},\n  ");
		xhead.append("  {header:'状态', align:'center'},\n ");
		xhead.append("  {header:'发货单位', align:'center'},\n ");
		xhead.append("  {header:'车辆号码', align:'center'},\n ");
		xhead.append("  {header:'发站', align:'center'},\n ");
//		xhead.append("  {header:'煤种', align:'center'},\n ");
		xhead.append("  {header:'票重', align:'center'},\n ");
		xhead.append("  {header:'股道号', align:'center'},\n ");
		xhead.append("  {header:'入厂时间<br/>(日/时/分)', align:'center'},\n ");
		xhead.append("  {header:'开始时间', align:'center'},\n ");
		xhead.append("  {header:'结束时间', align:'center'}\n ");
		xhead.append(" ] \n ");

		shead.append(" ],\n");

		// ×表示未选中状态
		//select m.mingc meiz       from meizb m,
		String sql = "select rownum xuh,'×' zhuangt,g.mingc fahdw,c.cheph chelhm,cz.mingc faz,\n"
				+ "       c.biaoz piaoz,c.zhongchh gudh,to_char(c.zhongcsj,'dd-HH24-mi') rucsj,\n"
				+ "       '' xiecfs,'' meic,r.diaody diaody,r.xieckssj kaissj,r.xiecjzsj jiezsj,r.beiz beiz,c.id chepb_id\n"
				+ "from fahb f,chepb c,chezxxb cz,gongysb g,rucxcb r,yunsfsb y\n"
				+ "where f.id=c.fahb_id\n"
				+ "      and f.gongysb_id=g.id\n"
				+ "      and f.faz_id=cz.id\n"
				//+ "      and f.meizb_id=m.id\n"
				+ "		 and f.yunsfsb_id=y.id\n"
				+ "		 and y.id=1\n"
				+ "      and r.chepb_id(+)=c.id\n"
				+ "      and to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi')>="
				+ this.getWanzRiq(this.getRCKSRiq(), this.getRCKSSJValue()
						.getValue())
				+ "\n"
				+ "		 and to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi')<="
				+ this.getWanzRiq(this.getRCJZRiq(), this.getRCJZSJValue()
						.getValue())
				+ "\n "
				+ "		and c.id not in(\n"
				+ "         select r.chepb_id from rucxcb r\n"
				+ "     )\n"
				+ "order by xuh";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("rucxcb");
		egu.addPaging(25);

		// egu.getColumn("zhuangt").setEditor(new Checkbox());
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setEditor(null);
		
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setEditor(null);

		egu.getColumn("fahdw").setHeader("发货单位");
		egu.getColumn("fahdw").setEditor(null);

		egu.getColumn("chelhm").setHeader("车辆号码");
		egu.getColumn("chelhm").setEditor(null);

		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setEditor(null);

//		egu.getColumn("meiz").setHeader("煤种");
//		egu.getColumn("meiz").setEditor(null);

		egu.getColumn("piaoz").setHeader("票重");
		egu.getColumn("piaoz").setEditor(null);

		egu.getColumn("gudh").setHeader("股道号");
		egu.getColumn("gudh").setEditor(null);

		egu.getColumn("rucsj").setHeader("入厂时间");
		egu.getColumn("rucsj").setEditor(null);

		egu.getColumn("xiecfs").setHeader("卸车方式");
		egu.getColumn("xiecfs").setEditor(new ComboBox());

		// 卸车方式下拉框
		List xc = new ArrayList();
		xc.add(new IDropDownBean("1", "外卸"));
		xc.add(new IDropDownBean("2", "直上"));
		egu.getColumn("xiecfs").setComboEditor(egu.gridId,
				new IDropDownModel(xc));

		egu.getColumn("meic").setHeader("煤场");
		egu.getColumn("meic").setEditor(new ComboBox());
		egu.getColumn("meic").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from meicb"));

		egu.getColumn("diaody").setHeader("调度员");
		egu.getColumn("diaody").setEditor(new ComboBox());
		egu.getColumn("diaody").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select r.id,r.quanc||'('||r.zhiw||')' from renyxxb r where r.bum='调度'",
						""));

		egu.getColumn("kaissj").setHeader("卸车开始时间");
		egu.getColumn("jiezsj").setHeader("卸车截止时间");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("chepb_id").setHidden(true);
		List list = egu.gridColumns;

		// 设置单元格宽度
		for (int i = 0; i < list.size(); i++) {
			GridColumn gc = (GridColumn) list.get(i);
			if (gc.dataIndex.equalsIgnoreCase("fahdw")) {
				gc.setWidth(200);
			} else if (gc.dataIndex.equalsIgnoreCase("xiecsjfw")) {
				gc.setWidth(200);
			} else if (gc.dataIndex.equalsIgnoreCase("zhuangt")
						||gc.dataIndex.equalsIgnoreCase("xuh")) {
				gc.setWidth(50);
			} else {
				gc.setWidth(120);
			}
		}

		// 开始时间范围选择控件
		DatetimeField ks = new DatetimeField();
		ks.setFormat("Y-m-d H:i");
		ks.setMenu("new DatetimeMenu()");
		egu
				.getColumn("kaissj")
				.setRenderer(
						"function(value){ if(value==null || value==''){return '';}else{if('object' != typeof(value)){return value;}else{ks=value;document.getElementById('Kaissj').value=value.dateFormat('Y-m-d H:i:s');return value.dateFormat('d H/i').replace(' ','/');}}}");
		egu.getColumn("kaissj").setEditor(ks);

		// 结束时间选择
		DatetimeField js = new DatetimeField();
		js.setFormat("Y-m-d H:i");
		js.setMenu("new DatetimeMenu()");
		egu
				.getColumn("jiezsj")
				.setRenderer(
						"function(value){ if(value==null || value==''){return '';}else{if('object' != typeof(value)){return value;}else{jz=value;document.getElementById('Jiezsj').value=value.dateFormat('Y-m-d H:i:s');return value.dateFormat('d H/i').replace(' ','/');}}}");
		egu.getColumn("jiezsj").setEditor(js);

		String Headers = shead.toString() + xhead.toString();
		egu.setHeaders(Headers);
		egu.setPlugins("new Ext.ux.plugins.XGrid()");

		// 入厂开始时间选择
		egu.addTbarText("开始日期：");
		DateField rucksDF = new DateField();
		rucksDF.setValue(this.getRCKSRiq());
		rucksDF
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('RCKSRiq').value = newValue.dateFormat('Y-m-d');}");
		egu.addToolbarItem(rucksDF.getScript());
		egu.addTbarText("-");

		// 入厂截止时间选择
		egu.addTbarText("截止日期：");
		DateField rucjzDF = new DateField();
		rucjzDF.setId("df");
		rucjzDF.setValue(this.getRCJZRiq());
		rucjzDF
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('RCJZRiq').value = newValue.dateFormat('Y-m-d');document.forms[0].submit();}");
		egu.addToolbarItem(rucjzDF.getScript());
		egu
				.addOtherScript("df.on('change', function(o,record,index) {document.forms[0].submit();});");
		egu.addTbarText("-");

		// 开始时间
		egu.addTbarText("开始时间：");
		ComboBox ksComb = new ComboBox();
		ksComb.setWidth(80);
		ksComb.setTransform("RCKSSJDropDown");
		ksComb.setId("KS");// 和自动刷新绑定
		ksComb.setLazyRender(true);// 动态绑定
		ksComb.setEditable(true);
		egu.addToolbarItem(ksComb.getScript());
		egu.addTbarText("-");
		// 开始时间
		egu.addTbarText("截止时间：");
		ComboBox jzComb = new ComboBox();
		jzComb.setWidth(80);
		jzComb.setTransform("RCJZSJDropDown");
		jzComb.setId("JZ");// 和自动刷新绑定
		jzComb.setLazyRender(true);// 动态绑定
		jzComb.setEditable(true);
		egu.addToolbarItem(jzComb.getScript());

		egu.addTbarText("-");
		// 刷新
		GridButton gbrf = new GridButton(
				"刷新",
				"function(){document.getElementById('RefreshButton').click();}",
				SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbrf);

		String handler = "function(){\n"
				+ "if(ks>jz){Ext.MessageBox.alert('提示信息','卸车时间范围 截止时间要大于或等于开始时间！');return;}"
				+ " var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n"
				+ "if(Mrcd[i].get('ZHUANGT')=='√'){"
				+ "if(Mrcd[i].get('XIECFS') == ''){Ext.MessageBox.alert('提示信息','字段 卸车方式 不能为空');return;\n"
				+ "}if(Mrcd[i].get('KAISSJ') == ''){Ext.MessageBox.alert('提示信息','字段 卸车开始时间 不能为空');return;\n"
				+ "}if(Mrcd[i].get('JIEZSJ') == ''){Ext.MessageBox.alert('提示信息','字段 卸车截止时间 不能为空');return;\n"
				+ "}if(Mrcd[i].get('DIAODY') == ''){Ext.MessageBox.alert('提示信息','字段 调度员 不能为空');return;\n"
				+ "}if(Mrcd[i].get('XIECFS')=='外卸'&&Mrcd[i].get('MEIC')==''){Ext.MessageBox.alert('提示信息','外卸需要选择煤场');return;}"
				+ "gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ZHUANGT update=\"true\">' + Mrcd[i].get('ZHUANGT')+ '</ZHUANGT>'\n"
				+ "+ '<XIECFS update=\"true\">' + Mrcd[i].get('XIECFS').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIECFS>'\n"
				+ "+ '<MEIC update=\"true\">' + Mrcd[i].get('MEIC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIC>'\n"
				+ "+ '<DIAODY update=\"true\">' + Mrcd[i].get('DIAODY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIAODY>'\n"
				+ "+ '<KAISSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('KAISSJ'))?Mrcd[i].get('KAISSJ'):Mrcd[i].get('KAISSJ').dateFormat('d/H/i'))+ '</KAISSJ>'\n"
				+ "+ '<JIEZSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('JIEZSJ'))?Mrcd[i].get('JIEZSJ'):Mrcd[i].get('JIEZSJ').dateFormat('d/H/i'))+ '</JIEZSJ>'\n"
				+ "+ '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BEIZ>'\n"
				+ "+ '<CHEPB_ID update=\"true\">' + Mrcd[i].get('CHEPB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHEPB_ID>'\n"
				+ " + '</result>' ; }}\n"
				+ "if(gridDiv_history=='' && gridDivsave_history==''){\n"
				+ "Ext.MessageBox.alert('提示信息','没有进行改动或没有选中要维护的记录，无法保存');\n"
				+ "}else{\n"
				+ "var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';"
				+ "Ext.MessageBox.confirm('提示信息','保存后将无法修改，确认保存？',function(btn){\n"
				+ "	if(btn=='yes'){\n"
				+ "   document.getElementById('SaveButton').click();Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
				+ "	}\n" + "});\n" + "}\n" + "}";

		// 保存
		GridButton gbts = new GridButton("保存", handler,
				SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(gbts);
		setExtGrid(egu);

		rsl.close();
		con.Close();
	}

	// 获得完整日期：to_date(yyyy-mm-dd h:m:s)
	/**
	 * @param riq
	 *            日期字符串 yyyy-mm-dd
	 * @param sj
	 *            时间字符串 h:m:s
	 * @return 完整日期 yyyy-mm-dd h:m:s
	 */
	public String getWanzRiq(String riq, String sj) {
		return "'" + riq + " " + sj + "'";
	}

	// 查询条件：入厂开始日期 yyyy-mm-dd
	private String rcksRiq = "";

	// 初始化查询具体时间标志
	private boolean Flag = false;

	public String getRCKSRiq() {
		return rcksRiq;
	}

	public void setRCKSRiq(String riq) {

		if (!riq.equals(rcksRiq)) {

			Flag = true;
		}
		rcksRiq = riq;
	}

	// 查询条件：入厂结束日期 yyyy-mm-dd
	private String rcjzRiq = "";

	public String getRCJZRiq() {
		return rcjzRiq;
	}

	public void setRCJZRiq(String riq) {

		if (!riq.equals(rcjzRiq)) {

			Flag = true;
		}
		rcjzRiq = riq;
	}

	// 卸车时间范围：kaissj jiezsj
	// 开始时间
	private String kaissj = "";

	public void setXCKaissj(String value) {
		kaissj = value;
	}

	public String getXCKaissj() {
		return kaissj;
	}

	// 卸车时间范围：kaissj jiezsj
	// 结束时间
	private String jiezsj = "";

	public void setXCJiezsj(String value) {
		jiezsj = value;
	}

	public String getXCJiezsj() {
		return jiezsj;
	}

	// 入厂查询开始时间下拉框:时分秒
	private static IPropertySelectionModel _RCKSSJModel;

	public IPropertySelectionModel getRCKSSJModel() {
		if (_RCKSSJModel == null) {
			getRCKSSJModels();
		}
		return _RCKSSJModel;
	}

	private IDropDownBean _RCKSSJValue;

	public IDropDownBean getRCKSSJValue() {
		if (_RCKSSJValue == null) {
			_RCKSSJValue = (IDropDownBean) _RCKSSJModel.getOption(0);
		}
		return _RCKSSJValue;
	}

	public void setRCKSSJValue(IDropDownBean Value) {
		_RCKSSJValue = Value;
	}

	public IPropertySelectionModel getRCKSSJModels() {
		List _list = new ArrayList();
		int i = 0;
		_list.add(new IDropDownBean("-1", "00:00"));
		JDBCcon con = new JDBCcon();

		String sql = "select distinct substr(to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi'),12) mingc\n"
				+ "from chepb c\n"
				+ "where  to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')>=to_date('"
				+ this.getRCKSRiq()
				+ "','yyyy-mm-dd')\n"
				+ "       and to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')<=to_date('"
				+ this.getRCJZRiq()
				+ "','yyyy-mm-dd')\n"
				+ " 	and c.id not in(\n"
				+ "			select r.chepb_id from rucxcb r"
				+ "		)" + "order by mingc asc";

		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			String id = i + "";
			String mc = rs.getString(0);
			_list.add(new IDropDownBean(id, mc));
			i++;
		}
		_RCKSSJModel = new IDropDownModel(_list);

		rs.close();
		con.Close();
		return _RCKSSJModel;
	}

	public void setRCKSSJModel(IPropertySelectionModel _value) {
		_RCKSSJModel = _value;
	}

	// 查询截止时间下拉框
	// 查询开始时间下拉框:时分秒
	private static IPropertySelectionModel _RCJZSJModel;

	public IPropertySelectionModel getRCJZSJModel() {
		if (_RCJZSJModel == null) {
			getRCJZSJModels();
		}
		return _RCJZSJModel;
	}

	private IDropDownBean _RCJZSJValue;

	public IDropDownBean getRCJZSJValue() {	
		if (_RCJZSJValue == null) {
			_RCJZSJValue = (IDropDownBean) _RCJZSJModel.getOption(0);
		}
		return _RCJZSJValue;
	}

	public void setRCJZSJValue(IDropDownBean Value) {
		_RCJZSJValue = Value;
	}

	public IPropertySelectionModel getRCJZSJModels() {
		int i = 0;
		List _list = new ArrayList();
		_list.add(new IDropDownBean("-1", "23:59"));
		JDBCcon con = new JDBCcon();
	
		// 查询时间 hh:mm
		String sql = "select distinct substr(to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi'),12) mingc\n"
				+ "from chepb c\n"
				+ "where  to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')>=to_date('"
				+ this.getRCKSRiq()
				+ "','yyyy-mm-dd')\n"
				+ "       and to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')<=to_date('"
				+ this.getRCJZRiq()
				+ "','yyyy-mm-dd')\n"
				+ " 	and c.id not in(\n"
				+ "			select r.chepb_id from rucxcb r"
				+ "		)" + "order by mingc asc";

		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			String id = i + "";
			String mc = rs.getString(0);
			_list.add(new IDropDownBean(id, mc));
			i++;
		}
		_RCJZSJModel = new IDropDownModel(_list);
		
		rs.close();
		con.Close();
		return _RCJZSJModel;
	}

	public void setRCJZSJModel(IPropertySelectionModel _value) {
		_RCJZSJModel = _value;
	}

	// -----电厂tree
	// private String treeid;
	// private boolean diancFlag=false;
	// public String getTreeid() {
	// return treeid;
	// }
	// public void setTreeid(String treeid) {
	// this.treeid = treeid;
	// }
	// public String getTreeid() {
	// String treeid=((Visit) getPage().getVisit()).getString2();
	// if(treeid==null||treeid.equals("")){
	// ((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit)
	// this.getPage().getVisit()).getDiancxxb_id()));
	// }
	// return ((Visit) getPage().getVisit()).getString2();
	// }
	// public void setTreeid(String treeid) {
	// if(((Visit) getPage().getVisit()).getString2()!=null && !((Visit)
	// getPage().getVisit()).getString2().equals(treeid)){
	// diancFlag=true;
	// }
	// ((Visit) getPage().getVisit()).setString2(treeid);
	// }
	// public ExtTreeUtil getTree() {
	// return ((Visit) this.getPage().getVisit()).getExtTree1();
	// }
	// public void setTree(ExtTreeUtil etu) {
	// ((Visit) this.getPage().getVisit()).setExtTree1(etu);
	// }
	// public String getTreeHtml() {
	// return getTree().getWindowTreeHtml(this);
	// }
	// public String getTreeScript() {
	// return getTree().getWindowTreeScript();
	// }

	// --------------------------------

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
			visit.setActivePageName(getPageName().toString());
			this.getRCJZSJModels();
			this.getRCKSSJModels();
			this.setRCJZRiq(DateUtil.FormatDate(new Date()));
			this.setRCKSRiq(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
		// 修改日期后重新初始化时间下拉框
		if (Flag) {
			Flag = false;
			this.getRCKSSJModels();
			this.getRCJZSJModels();

		}

	}
}
