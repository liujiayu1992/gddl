package com.zhiren.dc.hesgl.tielzf;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * 
 * @author rock
 * @since 2009-12-04
 * @discription 铁路杂费查询功能
 */
/*
 * 作者：王磊
 * 时间：2009-12-17
 * 描述：修改报表刷新项目的时候方式不同
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-20 15：33
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：王耀霆
 * 时间：2014-2-13 10:46
 * 描述：'费用项目'下拉框里的内容显示的不全，修改一下SQL
 */
public class Tielzfcx extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	protected void initialize() {
		this._msg = "";
	}
	
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	public boolean getRaw() {
		return true;
	}
	
//	起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
//	发站名称下拉框
	public IDropDownBean getDaozValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getDaozModel().getOptionCount()>0) {
				setDaozValue((IDropDownBean)getDaozModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setDaozValue(IDropDownBean gongysValue) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(gongysValue);
	}
	
	public IPropertySelectionModel getDaozModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setDaozModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setDaozModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setDaozModels() {
		String str = "select 0 id, '全部' mingc from dual union all select * from " +
			"(select id, mingc from chezxxb where id in (select distinct d.faz_id from " +
			"daozzfb d) order by mingc)";
		setDaozModel(new IDropDownModel(str));
	}
	
//	费用下拉框
	public IDropDownBean getFeiyValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean5()==null) {
			if(getFeiyModel().getOptionCount()>0) {
				setFeiyValue((IDropDownBean)getFeiyModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean5();
	}

	public void setFeiyValue(IDropDownBean gongysValue) {
		((Visit)this.getPage().getVisit()).setDropDownBean5(gongysValue);
	}
	
	public IPropertySelectionModel getFeiyModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel5()==null) {
			setFeiyModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel5();
	}
	
	public void setFeiyModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel5(value);
	}
	
	public void setFeiyModels() {
		String str = "select 0 id, nvl('全部', '') mingc from dual\n" +
		"union\n" + 
		"select i.id, i.mingc from item i where itemsortid=108";
		setFeiyModel(new IDropDownModel(str));
	}
	

//	显示方式下拉框
	public IDropDownBean getXiansfsValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getXiansfsModel().getOptionCount()>0) {
				setXiansfsValue((IDropDownBean)getXiansfsModel().getOption(0));
//				String deffz = "";
//				String sql = "select c.mingc " +
//				"from diancdzb d, chezxxb c " +
//				"where d.chezxxb_id = c.id and d.leib = '车站' " +
//				"and diancxxb_id = " + getTreeid();
//				JDBCcon con = new JDBCcon();
//				ResultSetList rsl = con.getResultSetList(sql);
//				if(rsl.next()){
//					deffz = rsl.getString("mingc");
//				}
//				for(int i= 0; i< getXiansfsModel().getOptionCount() ; i++){
//					IDropDownBean db = (IDropDownBean)getXiansfsModel().getOption(i);
//					if(deffz.equals(db.getValue())){
//						setXiansfsValue(db);
//					}
//				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setXiansfsValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getXiansfsModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setXiansfsModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setXiansfsModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setXiansfsModels() {
		String str = "select 0 id, '全部' mingc from dual union all select * from " +
		"(select id, mingc from chezxxb where id in (select distinct d.faz_id from " +
		"daozzfb d) order by mingc)";
		setXiansfsModel(new IDropDownModel(str));
//		List list = new ArrayList();
//		list.add(new IDropDownBean(1,"汇总"));
//		list.add(new IDropDownBean(2,"明细"));
//		setXiansfsModel(new IDropDownModel(list));
	}
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String sql="";
		String strSQLfz = "";
		if(getXiansfsValue() != null){
			if(!"全部".equals(getXiansfsValue().getValue())){
				strSQLfz = " and faz_id = " + getXiansfsValue().getId();
			}
		}
		String strSQLdz = "";
		if(getDaozValue() != null){
			if(!"全部".equals(getDaozValue().getValue())){
				strSQLdz = " and daoz_id = " + getDaozValue().getId();
			}
		}
		String strSQLfy = "";
		if(getFeiyValue() != null){
			if(getFeiyValue().getId() != 0){
				strSQLfy = " and i.id = " + getFeiyValue().getId();
			}
		}
		String strSQLbrq = DateUtil.FormatOracleDate(new Date());
		if(getBRiq() != null && !"".equals(getBRiq())){
			strSQLbrq = DateUtil.FormatOracleDate(getBRiq());
		}
		String strSQLerq = DateUtil.FormatOracleDate(new Date());
		if(getERiq() != null && !"".equals(getERiq())){
			strSQLerq = DateUtil.FormatOracleDate(getERiq());
		}
		if("".equals(strSQLfy)){
			sql = "select decode(grouping(s.id),1,'合计',to_char(z.riq,'yyyy-mm-dd')) rq,\n" +
			"decode(grouping(s.id),1,'合计',f.mingc) fz,\n" + 
			"decode(grouping(s.id),1,'合计',d.mingc) dz,sum(s.ches) cs,\n" + 
			"sum(s.zibcs) zbcs,sum(s.fancjs) fcjs,\n" + 
			"decode(grouping(s.id),1,nvl(i.mingc,'合计'),nvl(i.mingc,'小计')) xm,\n" + 
			"sum(s.zongje) je from\n" + 
			"(select t.id,t.faz_id,t.daoz_id,t.ches,t.zibcs,\n" + 
			"t.fancjs,0 as feiyxm,t.zongje from tielzf t\n" + 
			"union select m.tielzf_id,-1,-1,0,0,0,m.feiymc_item_id,\n" + 
			"zongje from tielzfmx m) s,\n" + 
			"(select t.id,t.riq from tielzf t, diancxxb c\n" + 
			"where diancxxb_id = c.id\n" + 
			"and riq >= "+strSQLbrq+"\n" + 
			"and riq <= "+strSQLerq+"\n" + 
			"and (c.id = "+ getTreeid() +"  or c.fuid = "+ getTreeid() +" )\n" + 
			strSQLfz + strSQLdz + 
			")z, item i, chezxxb f, chezxxb d\n" + 
			"where s.id = z.id and s.faz_id = f.id(+) and s.daoz_id = d.id(+)\n" + 
			"and s.feiyxm = i.id(+)\n" + strSQLfy +
			" group by rollup(i.mingc,s.id,z.riq,f.mingc,d.mingc)\n" + 
			"having grouping(d.mingc)=0 or (grouping(s.id) = 1 and grouping(i.mingc) = 0)\n" + 
			"order by grouping(i.mingc),grouping(s.id),s.id,i.mingc,f.mingc,d.mingc";
		}else{
			sql = "select decode(grouping(t.riq),1,'总计',to_char(t.riq,'yyyy-mm-dd')) riq,\n" +
				"f.mingc faz_id, d.mingc daoz_id, sum(t.ches) cs, sum(t.zibcs) zbc,\n" + 
				"sum(t.fancjs) fcs, i.mingc, sum(m.zongje) je\n" + 
				"from tielzf t, diancxxb c, item i, chezxxb f, chezxxb d, tielzfmx m\n" + 
				"where t.diancxxb_id = c.id and t.faz_id = f.id(+) and t.daoz_id = d.id(+)\n" + 
				"and m.feiymc_item_id = i.id(+) and t.id = m.tielzf_id\n" + 
				"and t.riq >= "+strSQLbrq+"\n" + 
				"and t.riq <= "+strSQLerq+"\n" + 
				"and (c.id = "+ getTreeid() +"  or c.fuid = "+ getTreeid() +" )\n" + 
				strSQLfz + strSQLdz + strSQLfy +
				"group by rollup(t.riq,f.mingc,d.mingc,i.mingc)\n" + 
				"having grouping(i.mingc) = 0 or grouping(t.riq) = 1\n" + 
				"order by t.riq,f.mingc,d.mingc";
		}

		ResultSetList rslData = con.getResultSetList(sql);
		
		int ArrWidth[] = new int[]{80,70,70,70,70,70,100,70};
				
		String ArrHeader[][] = new String[][]{{"日期","发站","到站","车数","自备车数","翻车机数","费用名称","总金额"}};
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("铁 路 杂 费 查 询", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 3));
		rt.body.setPageRows(rt.PAPER_ROWS);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		//	增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setColCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(7, Table.PER_ALIGN, Table.ALIGN_CENTER);

		rt.setDefaultTitle(1, 2, "制表单位：" + ((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4, "查询日期：" + getBRiq() + " 至 " + getERiq(), Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(5, 2, "制表：", Table.ALIGN_CENTER);
		for(int i=1 ; i <= rt.body.getRows(); i++){
			rt.body.merge(i, 1, i, 3);
		}
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private void getSelectData() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("查询日期："));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");
		tbr.addField(dfb);
		tbr.addText(new ToolbarText("至"));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");
		tbr.addField(dfe);
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tbr.addText(new ToolbarText("-"));
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		
		tbr.addText(new ToolbarText("发站："));
		ComboBox xiansfs = new ComboBox();
		xiansfs.setTransform("XiansfsSelect");
		xiansfs.setWidth(60);
		xiansfs.setListeners("select :function(xiansfs,newValue,oldValue){document.forms[0].submit();}");
		xiansfs.setLazyRender(true);
		xiansfs.setEditable(true);
		tbr.addField(xiansfs);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("-"));
		tbr.addText(new ToolbarText("到站："));
		ComboBox comb = new ComboBox();
		comb.setWidth(60);
		comb.setTransform("Daoz");
		comb.setId("Daoz");
		comb.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
		comb.setLazyRender(true);
		comb.setEditable(true);
		tbr.addField(comb);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("费用项目："));
		ComboBox fy = new ComboBox();
		fy.setTransform("Feiyxm");
		fy.setWidth(120);
		fy.setListeners("select :function(own,newValue,oldValue){document.forms[0].submit();}");
		fy.setLazyRender(true);
		fy.setEditable(true);
		tbr.addField(fy);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		tbr.addFill();
		setToolbar(tbr);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	电厂tree_Begin
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
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

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂tree_End
	
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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			this.setTreeid(""+visit.getDiancxxb_id());
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			getDiancmcModels();
			setXiansfsValue(null);
			setXiansfsModels();
			setFeiyValue(null);
			setFeiyModel(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
		}
		getSelectData();
	}
	
}
