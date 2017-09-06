package com.zhiren.jt.dtsx;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import java.sql.ResultSet;
import java.util.Date;


public class Jiltz_fb extends BasePage {
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	// ***************设置消息框******************//
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// 得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}



	public String getPrintTable() {
		setMsg(null);

		return getJiltz_fb();
	
	}

	private String getJiltz_fb() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String Pound="";
		String yunsdw="";
		//以磅号查询卸煤点，模糊查询磅号
		if(getPoundValue().getId()==1){
			Pound="and c.qingchh like '%1期%'\n";
			
		}else if(getPoundValue().getId()==2){
			Pound="and c.qingchh like '%2期%'\n";
		}else{
			Pound="";
		}
		
		if(getYunsdwValue().getId()==-1){
			yunsdw="";
		}else{
			yunsdw=" and c.yunsdwb_id="+
        		            getYunsdwValue().getId();
		}
		
		StringBuffer sbsql = new StringBuffer();
        sbsql.append(
        		"select dianc,\n" +
        		"       fahdw,\n" + 
        		"       meikdw,\n" + 
        		"       faz,\n" + 
        		"       maoz,\n" + 
        		"       piz,\n" + 
        		"       jingz,\n" + 
        		"       yingk,\n" + 
        		"       yuns,\n" + 
        		"       zongkd,\n" + 
        		"       ches\n" + 
        		"  from (select decode(grouping(d.fgsmc) + grouping(d.mingc) +\n" + 
        		"                      grouping(g.mingc) + grouping(m.mingc),\n" + 
        		"                      4,\n" + 
        		"                      '总计',\n" + 
        		"                      3,\n" + 
        		"                      decode(grouping(d.mingc) + grouping(m.mingc) +\n" + 
        		"                             grouping(d.fgsmc),\n" + 
        		"                             3,\n" + 
        		"                             '韩城二电厂',\n" + 
        		"                             d.mingc),\n" + 
        		"                      d.mingc) as dianc,\n" + 
        		"               decode(grouping(g.mingc) + grouping(m.mingc) -\n" + 
        		"                      grouping(d.mingc),\n" + 
        		"                      2,\n" + 
        		"                      '合计',\n" + 
        		"                      g.mingc) as fahdw,\n" + 
        		"               decode(grouping(m.mingc) - grouping(g.mingc) -\n" + 
        		"                      grouping(d.mingc),\n" + 
        		"                      1,\n" + 
        		"                      '小计',\n" + 
        		"                      m.mingc) as meikdw,\n" + 
        		"               z.mingc faz,\n" + 
        		"               p.mingc pinz,\n" + 
        		"               sum(round_new(c.maoz,\n" + 
        		"                             nvl((select r.zhi\n" + 
        		"                                   from xitxxb r\n" + 
        		"                                  where r.diancxxb_id = 303\n" + 
        		"                                    and r.mingc = '汽车日报小数位'\n" + 
        		"                                    and r.zhuangt = 1),\n" + 
        		"                                 2))) maoz,\n" + 
        		"               sum(round_new(c.piz,\n" + 
        		"                             nvl((select r.zhi\n" + 
        		"                                   from xitxxb r\n" + 
        		"                                  where r.diancxxb_id = 303\n" + 
        		"                                    and r.mingc = '汽车日报小数位'\n" + 
        		"                                    and r.zhuangt = 1),\n" + 
        		"                                 2))) piz,\n" + 
        		"               sum(round_new(c.maoz - c.piz-c.koud,\n" + 
        		"                             nvl((select r.zhi\n" + 
        		"                                   from xitxxb r\n" + 
        		"                                  where r.diancxxb_id = 303\n" + 
        		"                                    and r.mingc = '汽车日报小数位'\n" + 
        		"                                    and r.zhuangt = 1),\n" + 
        		"                                 2))) jingz,\n" + 
        		"               sum(round_new(c.yingk,\n" + 
        		"                             nvl((select r.zhi\n" + 
        		"                                   from xitxxb r\n" + 
        		"                                  where r.diancxxb_id = 303\n" + 
        		"                                    and r.mingc = '汽车日报小数位'\n" + 
        		"                                    and r.zhuangt = 1),\n" + 
        		"                                 2))) yingk,\n" + 
        		"               sum(round_new(c.yuns,\n" + 
        		"                             nvl((select r.zhi\n" + 
        		"                                   from xitxxb r\n" + 
        		"                                  where r.diancxxb_id = 303\n" + 
        		"                                    and r.mingc = '汽车日报小数位'\n" + 
        		"                                    and r.zhuangt = 1),\n" + 
        		"                                 2))) yuns,\n" + 
        		"               sum(round_new(c.koud,\n" + 
        		"                             nvl((select r.zhi\n" + 
        		"                                   from xitxxb r\n" + 
        		"                                  where r.diancxxb_id = 303\n" + 
        		"                                    and r.mingc = '汽车日报小数位'\n" + 
        		"                                    and r.zhuangt = 1),\n" + 
        		"                                 2))) zongkd,\n" + 
        		"count(c.cheph) ches"+
        		"          from fahb    f,\n" + 
        		"               vwdianc d,\n" + 
        		"               gongysb g,\n" + 
        		"               meikxxb m,\n" + 
        		"               chezxxb z,\n" + 
        		"               pinzb   p,\n" + 
        		"               chepb   c\n" + 
        		"         where f.yunsfsb_id = 2and\n" + 
        		"         f.daohrq >=to_date('"+getRiqi()+"', 'yyyy-mm-dd') and\n" + 
        		"         f.daohrq <=to_date('"+getRiq2()+"', 'yyyy-mm-dd')\n" +
        		"           and f.gongysb_id = g.id\n" + 
        		"           and f.meikxxb_id = m.id\n" + 
        		"           and f.diancxxb_id = d.id\n" + 
        		"           and d.id = 303\n" + 
        		"           and f.faz_id = z.id\n" + 
        		"           and f.pinzb_id = p.id\n" + 
        		"           and c.fahb_id = f.id\n" + 
        		            yunsdw+
        		            Pound + 
        		"         group by grouping sets('1',(d.mingc),(g.mingc, d.mingc),(d.fgsmc, g.mingc, d.mingc, m.mingc, p.mingc, z.mingc))\n" + 
        		"         order by '1' desc,\n" + 
        		"                  grouping(d.mingc) desc,\n" + 
        		"                  d.mingc,\n" + 
        		"                  g.mingc desc,\n" + 
        		"                  grouping(m.mingc) desc)"
);

		ResultSet rs = cn.getResultSet(sbsql.toString());


		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] { "电厂", "发货单位", "煤矿单位", "发站", "毛重", "皮重",
				"净重", "盈亏", "运损", "总扣杂","车数" };

		int ArrWidth[] = new int[] { 100, 100, 100, 50, 60, 60, 60, 60, 60,
				60,60 };

		rt.setTitle("数量台帐", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		
//		rt.body.setColAlign(4, Table.ALIGN_CENTER);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);

		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
	}


//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}



	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())){
			visit.setActivePageName(getPageName().toString());
			
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean4(null);
			getGongysDropDownModels();
			getYunsdwModels() ;
			setYunsdwValue(null) ;
			getYunsdwModel() ;
			getPoundModels();
			setPoundValue(null);
			getPoundModel();
			setRiqi(null);
			setRiq2(null);
			visit.setboolean3(false);
			
	

		}
		
		getSelectData();
	
	}

	// 绑定日期
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

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	// 卸煤点
	public boolean _Poundchange = false;

	public IDropDownBean getPoundValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getPoundModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setPoundValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getPoundModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getPoundModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setPoundModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getPoundModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			List.add(new IDropDownBean(0, "全部"));
			List.add(new IDropDownBean(1, "1期"));
			List.add(new IDropDownBean(2, "2期"));

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
//运输单位
	
	public boolean _Yunsdwchange = false;

	public IDropDownBean getYunsdwValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getYunsdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYunsdwValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean4(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getYunsdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYunsdwModels() {
		JDBCcon con = new JDBCcon();
       String sql="select id,mingc from yunsdwb where diancxxb_id= "+((Visit) getPage().getVisit()).getDiancxxb_id();
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql,"全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
//	获取供应商
	public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
        setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    
	public void getSelectData() {
		
		Visit visit=(Visit)getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("开始日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("结束日期:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("卸煤点:"));
		ComboBox gh = new ComboBox();
		gh.setTransform("PoundDropDown");
		gh.setEditable(true);
		gh.setWidth(60);
		//gh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gh);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("运输单位:"));
		ComboBox ys = new ComboBox();
		ys.setTransform("YunsdwDropDown");
		ys.setEditable(true);
		ys.setWidth(80);
		//ys.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(ys);
		

		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));

		setToolbar(tb1);

	}
	
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
}
