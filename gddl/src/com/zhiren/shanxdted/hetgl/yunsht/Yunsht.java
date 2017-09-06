package com.zhiren.shanxdted.hetgl.yunsht;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.jt.het.yunsht.yunsht.Hetysxxbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunsht extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO 自动生成方法存根
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

	private void Save() {
		JDBCcon con = new JDBCcon();
		int flag = 0;
		String sql = new String();
		String hetys_id = new String();
		if(getChange()==null || "".equals(getChange())) {
			setMsg("记录未被改动无需保存！");
		}
		
		if(this.getMeikdwValue().getValue().equals("全部")){
			this.setMsg("保存失败,请先选择供应商名称后,然后再修改保存!");
			con.Close();
			return;
			
		}
		
		
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		while(rsl.next()){
			sql = 
				"begin\n" +
				"delete hetys where id=" + rsl.getString("id") + ";\n" + 
				"delete hetysjgb where id=" + rsl.getString("id") + ";\n" + 
				"delete hetysshrb where id=" + rsl.getString("id") + ";\n" + 
				"delete hetyswzb where id=" + rsl.getString("id") + ";\n" +
				"end;";
			con.getDelete(sql);
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			Hetysxxbean bean = new Hetysxxbean();
			bean = getHetysxxbean(bean, "yunsdwb", (getExtGrid().getColumn("ysmingc").combo).getBeanStrId(rsl.getString("ysmingc")));
			bean = getHetysxxbean(bean, "diancxxb", getTreeid());
			if("0".equals(rsl.getString("id"))){
				hetys_id = MainGlobal.getNewID(con, getTreeid());
				
				sql = "insert into hetys(id, mingc, diancxxb_id, hetbh, qiandrq, qianddd, yunsdwb_id, gongfdwmc, gongfdwdz,\n"
					+ " gongfdh, gongffddbr, gongfwtdlr, gongfdbgh, gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz,\n"
					+ " xuffddbr, xufwtdlr, xufdh, xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, qisrq, guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid,yunsjgfab_id)\n"
					+ "values(\n"
					+ hetys_id + "\n"
					+ ",'" + rsl.getString("htmingc") + "'\n"
					+ ", " + getTreeid() + "\n"
					+ ",'" + rsl.getString("htmingc") + "'\n"
					+ ", " + "to_date('" + rsl.getString("qisrq") + "','YYYY-MM-DD')\n" 
					+ ",'" + bean.getQianddd() + "'\n"
					+ ", " + bean.getGongysid() + "\n"
					+ ",'" + bean.getGONGFDWMC() + "'\n"
					+ ",'" + bean.getGONGFDWDZ() + "'\n"
					+ ",'" + bean.getGONGFDH() + "'\n"
					+ ",'" + bean.getGONGFFDDBR() + "'\n"
					+ ",'" + bean.getGONGFWTDLR() + "'\n"
					+ ",'" + bean.getGONGFDBGH() + "'\n"
					+ ",'" + bean.getGONGFKHYH() + "'\n"
					+ ",'" + bean.getGONGFZH() + "'\n"
					+ ",'" + bean.getGONGFYZBM() + "'\n"
					+ ",'" + bean.getGongfsh() + "'\n"
					+ ",'" + bean.getXUFDWMC() + "'\n"
					+ ",'" + bean.getXUFDWDZ() + "'\n"
					+ ",'" + bean.getXUFFDDBR() + "'\n"
					+ ",'" + bean.getXUFWTDLR() + "'\n"
					+ ",'" + bean.getXUFDH() + "'\n"
					+ ",'" + bean.getXUFDBGH() + "'\n"
					+ ",'" + bean.getXUFKHYH() + "'\n"
					+ ",'" + bean.getXUFZH() + "'\n"
					+ ",'" + bean.getXUFYZBM() + "'\n"
					+ ",'" + bean.getXufsh() + "'\n"
					+ ", " + "to_date('" + rsl.getString("qisrq") + "','YYYY-MM-DD')\n" 
					+ ", " + "to_date('" + rsl.getString("guoqrq") + "','YYYY-MM-DD')\n" 
					+ ",0"
					+ ",''"
					+ ",0" // 流程状态表ID
					+ ",0" // 流程跟踪表ID
					+ ",-1);\n";
				
				sql+= "insert into hetysjgb (id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id) \n"
					+ "values (" 
					+ "getnewid(" + getTreeid() + "),\n"
					+ hetys_id + ",\n"
					+ (getExtGrid().getColumn("mkmingc").combo).getBeanId(rsl.getString("mkmingc")) + ",\n"
					+ "11,\n"//运价
					+ "1,\n"
					+ "0,\n"
					+ "0,\n"
					+ "24,\n"
					+ rsl.getString("yunja") + ",\n"
					+ "24\n"
					+ ");\n";
				
				sql+= "insert into hetysshrb values(getnewid("+getTreeid()+")," + hetys_id + "," + getTreeid() + ");\n";
				
				sql+= "insert into hetyswzb values(getnewid("+getTreeid()+"),'" + rsl.getString("wenznr") + "'," + hetys_id + ");\n";
				
			}else{
				sql = "update hetys set " + "\n"
					+ " HETBH='" + rsl.getString("htmingc") + "'\n"
					+ ",mingc='" + rsl.getString("htmingc") + "'\n"
					+ ",QIANDRQ=to_date('"	+ rsl.getString("qisrq") + "','YYYY-MM-DD')" + "\n"
					+ ",QIANDDD='" + bean.getQianddd() + "'\n"
					+ ",YUNSDWB_ID=" + bean.getGongysid() + "\n"
					+ ",GONGFDWMC='" + bean.getGONGFDWMC() + "'\n"
					+ ",GONGFDWDZ='" + bean.getGONGFDWDZ()  + "'\n"
					+ ",GONGFDH='"	+ bean.getGONGFDH()  + "'\n"
					+ ",GONGFFDDBR='" + bean.getGONGFFDDBR()  + "'\n"
					+ ",GONGFWTDLR='" + bean.getGONGFWTDLR()  + "'\n"
					+ ",GONGFDBGH='" + bean.getGONGFDBGH()  + "'\n"
					+ ",GONGFKHYH='" + bean.getGONGFKHYH()  + "'\n"
					+ ",GONGFZH='" + bean.getGONGFZH()  + "'\n"
					+ ",GONGFYZBM='" + bean.getGONGFYZBM()  + "'\n"
					+ ",GONGFSH='"	+ bean.getGongfsh()  + "'\n"
					+ ",XUFDWMC='" + bean.getXUFDWMC() + "'\n"
					+ ",XUFDWDZ='" + bean.getXUFDWDZ()  + "'\n"
					+ ",XUFFDDBR='" + bean.getXUFFDDBR()  + "'\n"
					+ ",XUFWTDLR='" + bean.getXUFWTDLR()  + "'\n"
					+ ",XUFDH='" + bean.getXUFDH() + "'\n"
					+ ",XUFDBGH='" + bean.getXUFDBGH() + "'\n"
					+ ",XUFKHYH='" + bean.getXUFKHYH() + "'\n"
					+ ",XUFZH='" + bean.getXUFZH() + "'\n"
					+ ",XUFYZBM='" + bean.getXUFYZBM() + "'\n"
					+ ",XUFSH='" + bean.getXufsh() + "'\n"
					+ ",QISRQ=to_date('" + rsl.getString("qisrq") + "','YYYY-MM-DD')" + "\n" 
					+ ",GUOQRQ=to_date('" + rsl.getString("guoqrq") + "','YYYY-MM-DD')" + "\n"
					+ " where id=" + rsl.getString("id") + ";\n";
				
				sql+= "update hetysjgb set " + "\n"
					+ "  meikxxb_id=" + (getExtGrid().getColumn("mkmingc").combo).getBeanId(rsl.getString("mkmingc")) + "\n"
					+ " ,yunja=" + rsl.getString("yunja") + "\n"
					+ " where hetys_id=" + rsl.getString("id") + ";\n";
				
				sql+= "update hetyswzb set " + "\n"
					+ "  wenznr='"+rsl.getString("wenznr")+"'" + "\n"
					+ " where hetys_id=" + rsl.getString("id") + ";\n";
				
				sql+= "update hetysshrb set " + "\n"
					+ "  shouhr_id="+getTreeid()+"" + "\n"
					+ " where hetysb_id=" + rsl.getString("id") + ";\n";
			}
		}
		
		if(sql.length()!=0){
			flag = con.getUpdate("begin\n" + sql.toString() + "end;\n");
		}
		
		rsl.close();
		con.Close();
		
		if (flag >= 0) {
			setMsg("保存成功");
		} else {
			setMsg("保存失败");
			return;
		}
	}

	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}

		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = new ExtGridUtil();
		ResultSetList rsl = new ResultSetList();

		String meikdw = this.getMeikdwValue().getStrId();
		if (meikdw.equals("-1")) {
			meikdw = "";
		} else{
			meikdw = "   and jg.meikxxb_id  in (\n"
				+"select mk.id from meikxxb mk ,gongysmkglb gl where mk.id=gl.meikxxb_id\n" +
				"and gl.gongysb_id="+this.getMeikdwValue().getStrId()+")\n";
		}
		
		String sql_dc = "";
		if("300".equals(getTreeid())){
			sql_dc = "and h.diancxxb_id in(300,301,302,303,304)\n";
		}else if("301".equals(getTreeid())){
			sql_dc = "and h.diancxxb_id in (301)\n";
		}else if("302".equals(getTreeid())){
			sql_dc = "and h.diancxxb_id in (302,303,304)\n";
		}
		
		
		
		
		String sql = 
			"select h.id, y.id yid, d.mingc dcmingc, m.mingc mkmingc, y.mingc ysmingc, h.mingc htmingc, h.qisrq, h.guoqrq, jg.yunja, wz.wenznr\n" +
			"  from hetys h, hetysjgb jg, hetyswzb wz, meikxxb m, diancxxb d, yunsdwb y\n" + 
			" where h.id = jg.hetys_id\n" + 
			"   and h.id = wz.hetys_id\n" + 
			"   and jg.meikxxb_id = m.id\n" + 
			"   and h.yunsdwb_id = y.id\n" + 
			"   and h.diancxxb_id = d.id\n" + 
			"	and to_char(h.qisrq,'yyyy')=" + getNianfValue().getValue() + "\n" +
			"	and to_char(h.qisrq,'mm')=" + getYuefValue().getValue() + "\n" +
			"  "+sql_dc+"\n" +
			meikdw;

		rsl = con.getResultSetList(sql.toString());

		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("hetb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("yid").setHeader("YID");
		egu.getColumn("yid").setHidden(true);
		egu.getColumn("yid").setEditor(null);
		
		egu.getColumn("dcmingc").setHeader("厂别");
		egu.getColumn("dcmingc").setEditor(null);
		egu.getColumn("dcmingc").setDefaultValue(getDcMingc(con,getTreeid()));
		
		ComboBox lx = new ComboBox();
		lx.setEditable(true);
		lx.setListeners(
				"beforequery:function(e){" +
				"var combo = e.combo;" +
				"if(!e.forceAll){" +
				"var value = e.query;" +
				"combo.store.filterBy(function(record,id){" +
				"var text = record.get(combo.displayField);" +
				"" +
				"return (text.indexOf(value)!=-1);" +
				"});" +
				"combo.expand();" +
				"return false; " +
				" } " +
				"}");
		egu.getColumn("mkmingc").setEditor(lx);
		
		String gongys_tiaoj="";
		if(this.getMeikdwValue().getId()!=-1){
			gongys_tiaoj="and gl.gongysb_id="+this.getMeikdwValue().getId()+"\n";
		}
		String a1 = "select mk.id,mk.mingc\n" +
			"from meikxxb mk ,gongysmkglb gl\n" + 
			"where mk.id=gl.meikxxb_id\n" + 
			""+gongys_tiaoj+"\n" + 
			"order by mk.mingc";

		egu.getColumn("mkmingc").setComboEditor(egu.gridId,new IDropDownModel(a1));
		egu.getColumn("mkmingc").setHeader("煤矿单位");
		egu.getColumn("mkmingc").setWidth(200);
		
		ComboBox yslx = new ComboBox();
		yslx.setEditable(true);
		yslx.setListeners(
				"beforequery:function(e){" +
				"var combo = e.combo;" +
				"if(!e.forceAll){" +
				"var value = e.query;" +
				"combo.store.filterBy(function(record,id){" +
				"var text = record.get(combo.displayField);" +
				"" +
				"return (text.indexOf(value)!=-1);" +
				"});" +
				"combo.expand();" +
				"return false; " +
				" } " +
				"}");
		egu.getColumn("ysmingc").setEditor(yslx);
		//
		String a2 =
			"select id ,mingc from yunsdwb h where h.id in (\n" +
			"select distinct ys.id\n" + 
			"from yunsdwb ys ,chepb c ,fahb f\n" + 
			"where f.gongysb_id="+this.getMeikdwValue().getId()+"\n" + 
			"and c.fahb_id=f.id\n" + 
			"and c.yunsdwb_id=ys.id\n" + 
			"and f.daohrq>=add_months(to_date('"+this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"-01','yyyy-mm-dd'),-1)\n" + 
			"and f.daohrq<=add_months(to_date('"+this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"-01','yyyy-mm-dd'),1))\n" + 
			""+sql_dc+"  order by mingc ";
		
		egu.getColumn("ysmingc").setComboEditor(egu.gridId,new IDropDownModel(a2));
		egu.getColumn("ysmingc").setHeader("运输单位");
		egu.getColumn("ysmingc").setWidth(180);
		
		egu.getColumn("htmingc").setHeader("合同编号");
		egu.getColumn("htmingc").setWidth(130);
		
		egu.getColumn("qisrq").setHeader("生效日期");
		
		egu.getColumn("guoqrq").setHeader("过期日期");
		
		egu.getColumn("yunja").setHeader("单价");
		egu.getColumn("yunja").setWidth(60);
		
		egu.getColumn("wenznr").setHeader("备注");
		egu.getColumn("wenznr").setWidth(250);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		// ********************工具栏************************************************
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置煤矿单位
		// -------------------------------
		egu.addTbarText("供应商");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MEIKDW");
		comb3.setId("MEIKDW");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(150);
		egu.addToolbarItem(comb3.getScript());
		egu.addOtherScript("MEIKDW.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");

		egu.addToolbarButton(GridButton.ButtonType_Refresh,"RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, "");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "");
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
			setMeikdwModel(null);
			setMeikdwValue(null);
			visit.setString3(null);
		}

		getSelectData();

	}

	// 年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

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

	// 设置煤矿单位
	private IDropDownBean MeikdwValue;

	public IDropDownBean getMeikdwValue() {
		if (MeikdwValue == null) {
			MeikdwValue = (IDropDownBean) getMeikdwModel().getOption(0);
		}
		return MeikdwValue;
	}

	public void setMeikdwValue(IDropDownBean Value) {
		if (!(MeikdwValue == Value)) {
			MeikdwValue = Value;
		}
	}

	private IPropertySelectionModel MeikdwModel;

	public void setMeikdwModel(IPropertySelectionModel value) {
		MeikdwModel = value;
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (MeikdwModel == null) {
			getMeikdwModels();
		}
		return MeikdwModel;
	}

	public IPropertySelectionModel getMeikdwModels() {

		String sql = " select id,mingc from gongysb where leix=1  order by mingc ";
		MeikdwModel = new IDropDownModel(sql, "全部");
		return MeikdwModel;
	}

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
	
	private Hetysxxbean getHetysxxbean(Hetysxxbean bean,String table, String id){
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = new ResultSetList();
		String sql = "";
		String mingc = "";
		String chuanz = "";
		String dianh = "";
		String danwdz = "";
		String faddbr = "";
		String kaihyh = "";
		String shuih = "";
		String weitdlr = "";
		String youzbm = "";
		String zhangh = "";
		try{
			sql = "select * from " + table + " where id=" + id;
			rsl = con.getResultSetList(sql);
			if(rsl.next()){
				mingc = rsl.getString("mingc");
				chuanz = rsl.getString("chuanz");
				dianh = rsl.getString("dianh");
				danwdz = rsl.getString("danwdz");
				faddbr = rsl.getString("faddbr");
				kaihyh = rsl.getString("kaihyh");
				shuih = rsl.getString("shuih");
				weitdlr = rsl.getString("weitdlr");
				youzbm = rsl.getString("youzbm");
				zhangh = rsl.getString("zhangh");
			}
			
			if("yunsdwb".equals(table)){
				bean.setGongysid(Long.parseLong(id));
				bean.setGONGFDWMC(mingc);
				bean.setGONGFDBGH(chuanz);
				bean.setGONGFDH(dianh);
				bean.setGONGFDWDZ(danwdz);
				bean.setGONGFFDDBR(faddbr);
				bean.setGONGFKHYH(kaihyh);
				bean.setGongfsh(shuih);
				bean.setGONGFWTDLR(weitdlr);
				bean.setGONGFYZBM(youzbm);
				bean.setGONGFZH(zhangh);
			}else if("diancxxb".equals(table)){
				bean.setXUFDWMC(mingc);
				bean.setXUFDBGH(chuanz);
				bean.setXUFDH(dianh);
				bean.setXUFDWDZ(danwdz);
				bean.setXUFFDDBR(faddbr);
				bean.setXUFKHYH(kaihyh);
				bean.setXufsh(shuih);
				bean.setXUFWTDLR(weitdlr);
				bean.setXUFYZBM(youzbm);
				bean.setXUFZH(zhangh);
			}
		}catch(Exception e){
			
		}finally{
			rsl.close();
			con.Close();
		}
		return bean;
	}
	private String getDcMingc(JDBCcon con, String id){
		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id=" + id);
		if(rsl.next()){
			return rsl.getString("mingc");
		}else{
			return "";
		}
	}
}