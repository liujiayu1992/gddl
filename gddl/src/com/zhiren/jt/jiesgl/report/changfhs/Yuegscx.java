package com.zhiren.jt.jiesgl.report.changfhs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：月估收查询
 */

public class Yuegscx extends BasePage implements PageValidateListener {
	
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
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	年份下拉框_开始
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	年份下拉框_结束
	
//	月份下拉框_开始
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	月份下拉框_结束
	
//	供应商下拉框_开始
	public IDropDownBean getGongysValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getGongysModel().getOptionCount() > 0) {
				setGongysValue((IDropDownBean) getGongysModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean LuhValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LuhValue);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getGongysModels() {
		String sql = 
			"select distinct g.id, g.mingc\n" +
			"  from fahb f, gongysb g\n" + 
			" where f.gongysb_id = g.id\n" + 
			"   and g.leix = 1\n" + 
			"   and f.id in\n" + 
			"       (select distinct f.id\n" + 
			"          from fahb f, jiesb j\n" + 
			"         where f.jiesb_id = j.id(+)\n" + 
			"           and j.ruzrq is not null\n" + 
			"           and f.diancxxb_id = "+ getTreeid() +"\n" + 
			"			and f.daohrq < to_date('"+ getFirstDayOfNextMonth(getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01") +"', 'yyyy-MM-dd'))\n" +
			" order by g.mingc";
		setGongysModel(new IDropDownModel(sql, "全部"));
	}
//	供应商拉框_结束
	
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
		String[][] ArrHeader = null;
		int[] ArrWidth = null;
		String sql = "";
		int temp = 0;
		
		if (getGongysValue().getStrId().equals("-1")) { // 没有选择供应商
			sql = 
				"select decode(gys.mingc, '', '合计', gys.mingc) mingc, gus.biaoz, gus.yingk, gus.yuns, gus.gusl, gus.rez, gus.guj, gus.meij, gus.yunj from (\n" +
				"    select decode(mk.meikdq_id, null, -2, mk.meikdq_id) meikdq_id,\n" + 
				"           sum(round_new(fh.biaoz, 0)) biaoz,\n" + 
				"           sum(round_new(fh.jingz, 0) + round_new(fh.yuns, 0) - round_new(fh.biaoz, 0)) yingk,\n" + 
				"           sum(round_new(fh.yuns, 0)) yuns,\n" + 
				"           sum(round_new(fh.jingz, 0)) gusl,\n" + 
				"           round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4id(ls.id,'rez')))\n" + 
				"               / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 0) as rez,\n" + 
				"           round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * (getGusxx4id(ls.id,'meij') + getGusxx4id(ls.id,'yunf'))))\n" + 
				"               / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as guj,\n" + 
				"           round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4id(ls.id,'meij')))\n" + 
				"               / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as meij,\n" + 
				"           round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * (getGusxx4id(ls.id,'yunf')+getGusxx4id(ls.id,'zaf')+getGusxx4id(ls.id,'fazzf')+getGusxx4id(ls.id,'ditf'))))\n" + 
				"               / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as yunj\n" + 
				"        from fahb fh, meikxxb mk, guslsb ls\n" + 
				"       where fh.meikxxb_id = mk.id\n" + 
				"         and ls.fahb_id = fh.id\n" + 
				"         and ls.id in (\n" + 
				"                select max(ls.id) guslsb_id\n" + 
				"                  from fahb fh, jiesb js, guslsb ls\n" + 
				"                 where fh.jiesb_id = js.id\n" + 
				"                   and js.ruzrq is not null\n" + 
				"                   and fh.diancxxb_id = "+ getTreeid() +"\n" + 
				"                   and to_char(fh.daohrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm'), 'yyyy-mm')\n" + 
				"                   and ls.fahb_id = fh.id\n" + 
				"                   and ls.leix != 4\n" + 
				"                   group by ls.fahb_id\n" + 
				"             )\n" + 
				"      group by rollup(mk.meikdq_id)) gus,\n" + 
				"      gongysb gys\n" + 
				"where gus.meikdq_id = gys.id(+)";
			
			ArrHeader = new String[1][9];
			ArrHeader[0] = new String[]{"煤矿地区","票重<br>(吨)","盈亏<br>(吨)","运损<br>(吨)","估收量<br>(吨)","发热量<br>(kcal/kg)",
				"估价<br>(元/吨)","煤价<br>(元/吨)","运价<br>(元/吨)"};
			ArrWidth = new int[] {120, 90, 60, 60, 90, 80, 75, 75, 75};
			temp = 7;
			
		} else {
			sql = 
				"select decode(mk.mingc, null, '总计', mk.mingc) mingc,\n" +
				"       decode(fh.fahrq, null, decode(mk.mingc, null, '', '小计'), to_char(fh.fahrq, 'yyyy-mm-dd')) fahrq,\n" + 
				"       to_char(fh.daohrq, 'yyyy-mm-dd') daohrq,\n" + 
				"       fh.chec,\n" + 
				"       sum(round_new(fh.biaoz, 0)) biaoz,\n" + 
				"       sum(round_new(fh.jingz, 0) + round_new(fh.yuns, 0) - round_new(fh.biaoz, 0)) yingk,\n" + 
				"       sum(round_new(fh.yuns, 0)) yuns,\n" + 
				"       sum(round_new(fh.jingz, 0)) gusl,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4jies(fh.id, 'rez')))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 0) as rez,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * (getGusxx4zang(fh.id, 'meij') + getGusxx4zang(fh.id, 'yunf'))))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as guj,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4zang(fh.id, 'meij')))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as meij,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * (getGusxx4zang(fh.id,'yunf')+getGusxx4zang(fh.id,'zaf')+getGusxx4zang(fh.id,'fazzf')+getGusxx4zang(fh.id,'ditf'))))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as yunj\n" + 
				"    from fahb fh, meikxxb mk, gongysmkglb gmgl\n" + 
				"   where fh.id in (select distinct ls.fahb_id\n" + 
				"                     from fahb fh, jiesb js, guslsb ls\n" + 
				"                    where fh.jiesb_id = js.id\n" + 
				"                      and js.ruzrq is not null\n" + 
				"                      and fh.diancxxb_id = "+ getTreeid() +"\n" + 
				"                      and to_char(fh.daohrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm'), 'yyyy-mm')\n" + 
				"                      and ls.fahb_id = fh.id\n" + 
				"                      and ls.leix = 4)\n" + 
				"     and fh.meikxxb_id = mk.id\n" + 
				"     and mk.id = gmgl.meikxxb_id\n" + 
				"     and gmgl.gongysb_id = "+ getGongysValue().getStrId() +"\n" + 
				"   group by rollup(mk.mingc, fh.fahrq, fh.daohrq, fh.chec)\n" + 
				"   having not (grouping(fh.fahrq) <> 1 and grouping(fh.chec) = 1)\n" + 
				"   order by grouping(mk.mingc) desc, mk.mingc, grouping(fh.fahrq) desc, fh.fahrq";
			
			ArrHeader = new String[1][12];
			ArrHeader[0] = new String[] { "发货单位","发货日期","到货日期","车次","票重<br>(吨)","盈亏<br>(吨)","运损<br>(吨)","估收数量<br>(吨)",
				"热量<br>(kcal/kg)","估价<br>(元/吨)","煤价<br>(元/吨)","运价<br>(元/吨)"};
			ArrWidth = new int[] {120, 80, 80, 70, 70, 54, 54, 70, 54, 54, 54, 54};
			temp = 10;
		}
		
		ResultSetList rslData =  con.getResultSetList(sql);
		rt.setTitle("估收统计表", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 0));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);
		
		rt.setDefaultTitle(1, 4, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(temp, 3, "估收时间："+getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_LEFT);
		
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
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tbr = new Toolbar("tbdiv");
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel())
			.getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null, null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("年份："));
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(60);
		nf_comb.setListWidth(60);
		nf_comb.setTransform("Nianf");
//		nf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf_comb.setLazyRender(true);
		tbr.addField(nf_comb);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("月份："));
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(60);
		yf_comb.setListWidth(60);
		yf_comb.setTransform("Yuef");
//		yf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		yf_comb.setLazyRender(true);
		tbr.addField(yf_comb);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("供应商："));
		ComboBox kouj_comb = new ComboBox();
		kouj_comb.setTransform("Gongys");
		kouj_comb.setWidth(130);
		kouj_comb.setListWidth(150);
//		kouj_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		kouj_comb.setLazyRender(true);
		tbr.addField(kouj_comb);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 * 获取传入日期的下个月份的第一天
	 * @param date
	 * @return
	 */
	public String getFirstDayOfNextMonth(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			Date tempdate = sdf.parse(date);
			int month = DateUtil.getMonth(tempdate);
			int year =DateUtil.getYear(tempdate);
			if(month == 12){
				year += 1;
				month = 1;
			}else{
				month += 1;
			}
			return year+"-"+month+"-1";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return DateUtil.FormatDate(new Date());
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
	
//	电厂树_开始
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
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
//	电厂树_结束

	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // 月份下拉框
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null); // 供应商下拉框
			visit.setDropDownBean4(null); 
		}
		getSelectData();
	}
}