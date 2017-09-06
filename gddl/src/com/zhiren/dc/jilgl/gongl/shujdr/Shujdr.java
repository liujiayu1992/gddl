package com.zhiren.dc.jilgl.gongl.shujdr;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shujdr extends BasePage implements PageValidateListener {
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
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Shujdr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		//插入车皮临时表
		sb.append("begin\n");
		while (rsl.next()) {
			sb.append("insert into cheplsb\n");
			sb.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, caiybh, yunsfsb_id, chec,cheph, maoz, piz, biaoz, koud, kous, kouz, sanfsl, jianjfs, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, zhongcjjy, daozch, lury, beiz,qingcsj,zhongcsj,yunsdwb_id,caiyrq)\n");
			sb.append("values (getnewid(").append(visit.getDiancxxb_id()).append("),").append(visit.getDiancxxb_id());
			sb.append(",").append((visit.getExtGrid1().getColumn("gongysb_id").combo).getBeanId(rsl.getString("gongysb_id")));
			sb.append(",").append((visit.getExtGrid1().getColumn("meikxxb_id").combo).getBeanId(rsl.getString("meikxxb_id")));
			sb.append(",").append((visit.getExtGrid1().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
			sb.append(",").append((visit.getExtGrid1().getColumn("faz_id").combo).getBeanId(rsl.getString("faz_id")));
			sb.append(",").append((visit.getExtGrid1().getColumn("daoz_id").combo)	.getBeanId(rsl.getString("daoz_id")));
			sb.append(",").append((visit.getExtGrid1().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
			sb.append(",").append(DateUtil.FormatOracleDate(rsl.getString("fahrq")));
			sb.append(",").append(DateUtil.FormatOracleDate(rsl.getString("daohrq")));
			sb.append(",'").append(rsl.getString("bianm")).append("',");
			sb.append(SysConstant.YUNSFS_QIY);
			sb.append(",'").append(rsl.getString("chec"));
			sb.append("','").append(rsl.getString("cheph"));
			sb.append("',").append(rsl.getDouble("maoz"));
			sb.append(",").append(rsl.getDouble("piz"));
			sb.append(",").append(rsl.getDouble("biaoz"));
			sb.append(",").append(rsl.getDouble("koud"));
			sb.append(",").append(rsl.getDouble("kous"));
			sb.append(",").append(rsl.getDouble("kouz"));
			sb.append(",").append(rsl.getDouble("sanfsl"));
			sb.append(",'").append(rsl.getString("jianjfs"));
			sb.append("',").append((visit.getExtGrid1().getColumn("chebb_id").combo).getBeanId(rsl.getString("chebb_id")));
			sb.append(",").append((visit.getExtGrid1().getColumn("yuandz_id").combo).getBeanId(rsl.getString("yuandz_id")));
			sb.append(",").append((visit.getExtGrid1().getColumn("yuanshdwb_id").combo).getBeanId(rsl.getString("yuanshdwb_id")));
			sb.append(",'").append(rsl.getString("yuanmkdw"));
			sb.append("','").append(rsl.getString("zhongcjjy"));
			sb.append("','").append(rsl.getString("daozch"));
			sb.append("','").append(visit.getRenymc());
			sb.append("','").append(rsl.getString("beiz")).append("'");
			sb.append(",").append(DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
			sb.append(",").append(DateUtil.FormatOracleDateTime(rsl.getString("guohsj")));
			sb.append(",").append((visit.getExtGrid1().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id")));
			sb.append(",").append(DateUtil.FormatOracleDate(DateUtil.getDate(rsl.getString("guohsj")))).append(");\n");
		}
		sb.append("end;");
		setMsg(Jilcz.SaveJilData(sb.toString(), visit.getDiancxxb_id(), SysConstant.YUNSFS_HUOY, 
				SysConstant.HEDBZ_YJJ, null, this.getClass().getName(), Jilcz.SaveMode_DR));
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
		boolean canEditData=false;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select 0 id, c.diancxxb_id, c.gongysmc gongysb_id, c.meikdwmc meikxxb_id, c.faz faz_id,\n")
		.append("c.pinz pinzb_id, c.jihkj jihkjb_id, c.fahrq, c.daohrq, c.jianjfs, b.mingc as chebb_id,\n")
		.append("c.chec, '' as bianm, c.qingcsj guohsj, c.zhongcjjy, c.cheph, c.maoz, c.piz, c.biaoz,\n")
		.append("c.koud, c.kous, c.kouz, c.sanfsl, c.daoz daoz_id, c.daoz yuandz_id, v.mingc yuanshdwb_id,\n")
		.append("c.meikdwmc yuanmkdw, c.yunsdwb_id, c.daozch, c.beiz from chepbtmp c,vwyuanshdw v,chebb b where c.diancxxb_id = v.id and fahb_id = 0 and c.chebb_id=b.id and diancxxb_id =")
		.append(visit.getDiancxxb_id());
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误sb:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		
			if(visit.isFencb()) {
				ComboBox dc= new ComboBox();
				egu.getColumn("diancxxb_id").setEditor(dc);
				dc.setEditable(true);
				String dcsb="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcsb));
				egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
				egu.getColumn("diancxxb_id").setWidth(70);
			}else {
				egu.getColumn("diancxxb_id").setHidden(true);
				egu.getColumn("diancxxb_id").editor = null;
			}
		
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("jianjfs").setHeader(Locale.jianjfs_chepb);
		egu.getColumn("jianjfs").setWidth(60);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("bianm").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("guohsj").setHidden(true);
		egu.getColumn("guohsj").editor = null;
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setDefaultValue("0");//设置默认值
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("kous").setHeader(Locale.kous_chepb);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("sanfsl").setWidth(60);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		
		//设置供应商下拉框
		ComboBox c8 = new ComboBox();
		
		c8.setEditable(true);
		String gyssb = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new IDropDownModel(gyssb));
		//设置煤矿单位下拉框
		ComboBox c9 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c9);
		c9.setEditable(true);
		c9.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		//设置发站下拉框
		ComboBox c0 = new ComboBox();
		egu.getColumn("faz_id").setEditor(c0);
		c0.setEditable(true);
		String Fazsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(Fazsb));

		//		设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c1);
		c1.setEditable(true);
		String daozsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozsb));
		//设置品种下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c2);
		c2.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		//设置口径下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjsb));
		//设置检斤方式下拉框
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "过衡"));
		l.add(new IDropDownBean(1, "检尺"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("jianjfs").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("jianjfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));

		//设置车别下拉框
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "路车"));
		ls.add(new IDropDownBean(2, "自备车"));
		ls.add(new IDropDownBean(3, "汽车"));
		ComboBox c5 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ls));

		//设置原到站下拉框
		ComboBox c6 = new ComboBox();
		egu.getColumn("yuandz_id").setEditor(c6);
		c6.setEditable(true);
		String Yuandzsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
				new IDropDownModel(Yuandzsb));
		egu.getColumn("yuandz_id").setDefaultValue(visit.getDaoz());
		
		//设置原收货单位下拉框
		ComboBox c7 = new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c7);
		c7.setEditable(true);//设置可输入
		String sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));

		//设置运输单位下拉框
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(comb);
		comb.setEditable(true);
		String yunsdwsb = "select id,mingc from yunsdwb where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwsb));

		//设置GRID是否可以编辑
		if (canEditData){
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		}else{
			egu.setGridType(ExtGridUtil.Gridstyle_Read );
		}
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(0);

		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");

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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
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
			getSelectData();
		}
	}
}
