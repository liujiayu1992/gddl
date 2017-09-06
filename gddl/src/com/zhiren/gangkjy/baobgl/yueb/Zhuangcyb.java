package com.zhiren.gangkjy.baobgl.yueb;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


public class Zhuangcyb  extends BasePage implements PageValidateListener{

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
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
	
//	 年份下拉框
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
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					Object obj = getNianfModel().getOption(i);
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


		public IDropDownBean getYuefValue(){
			if (_YuefValue == null) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					Object obj = getYuefModel().getOption(i);
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
			if (_YuefValue!= null) {
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
			// listYuef.add(new IDropDownBean(-1,"请选择"));
			for (int i = 1; i < 13; i++) {
				listYuef.add(new IDropDownBean(i, String.valueOf(i)));
			}
			_YuefModel = new IDropDownModel(listYuef);
			return _YuefModel;
		}

		public void setYuefModel(IPropertySelectionModel _value) {
			_YuefModel = _value;
		}
	
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
	
	private boolean _FindChick = false;

	public void FindButton(IRequestCycle cycle) {
		_FindChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_FindChick) {
			_FindChick = false;
	    }
	}



//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean5(null);
			this.getSelectData();
		}
		isBegin=true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;

	/**
	 * 发电集团电煤信息日报表
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		StringBuffer strSQL = new StringBuffer();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDianc = "";
		if(this.getDiancTreeJib()==1){//集团
			strDianc = "";
		}else if(this.getDiancTreeJib()==2){
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
		String benyrq = " and z.ligsj>=to_date('"+getNianfValue().getValue()+"-"+YUEF+"-01','yyyy-MM-dd')\n"
						+"and z.ligsj<last_day(to_date('"+getNianfValue().getValue()+"-"+YUEF+"-01','yyyy-MM-dd'))+1";
		String leijrq = " and z.ligsj>=to_date('"+getNianfValue().getValue()+"-01-01','yyyy-MM-dd')\n"
						+"and z.ligsj<last_day(to_date('"+getNianfValue().getValue()+"-"+YUEF+"-01','yyyy-MM-dd'))+1";
		String sql = 
			"select decode(grouping(tb.shouhr),1,'总计',tb.shouhr) as shouhr,\n" +
			"      decode(grouping(tb.shouhr)+grouping(tb.pinz),1,'合计',tb.pinz) as pinz,\n" + 
			"       tb.leix as leix,\n" + 
			"       sum(s.meil) as meil,\n" + 
			"       decode(sum(s.meil),0,0,round_new(sum(s.qnet_ar)/sum(s.meil),2)) as qnet_ar,\n" + 
			"       decode(sum(s.meil),0,0,round_new(sum(s.mt)/sum(s.meil),2)) as mt,\n" + 
			"       decode(sum(s.meil),0,0,round_new(sum(s.mad)/sum(s.meil),2)) as mad,\n" + 
			"       decode(sum(s.meil),0,0,round_new(sum(s.aad)/sum(s.meil),2)) as aad,\n" + 
			"       decode(sum(s.meil),0,0,round_new(sum(s.vdaf)/sum(s.meil),2)) as vdaf,\n" + 
			"       decode(sum(s.meil),0,0,round_new(sum(s.stad)/sum(s.meil),2)) as stad,\n" + 
			"       decode(sum(s.meil),0,0,round_new(sum(s.had)/sum(s.meil),2)) as had\n" + 
			"        from\n" + 
			"(select x.mingc as shouhr,\n" + 
			"       p.mingc as pinz,\n" + 
			"       decode(1,1,'本月','本月') as leix,\n" + 
			"       sum(z.zhuangcl) as meil,\n" + 
			"       sum(ls.qnet_ar*z.zhuangcl) as qnet_ar,\n" + 
			"       sum(ls.mt*z.zhuangcl) as mt,\n" + 
			"       sum(ls.mad*z.zhuangcl)as mad,\n" + 
			"       sum(ls.aad*z.zhuangcl) as aad,\n" + 
			"       sum(ls.vdaf*z.zhuangcl) as vdaf,\n" + 
			"       sum(ls.stad*z.zhuangcl) as stad,\n" + 
			"       sum(ls.had*z.zhuangcl) as had\n" + 
			"from zhuangcb z,vwxuqdw x,vwpinz p,zhillsb ls,zhilb zl,caiyb c,caiylbb lb,diancxxb dc\n" + 
			"where z.xiaosgysb_id = x.id\n" + 
			"      and z.pinzb_id = p.id\n" + 
			"      and z.zhilb_id = zl.id\n" + 
			"      and ls.zhilb_id = zl.id\n" + 
			"      and ls.caiyb_id = c.id\n" + 
			"      and c.caiylbb_id = lb.id\n" + 
			"      and lb.mingc = '装船化验'\n" + 
			"      and z.diancxxb_id=dc.id\n" +strDianc+"\n" + 
			benyrq +" \n" + 
			"  group by (x.mingc,p.mingc,decode(1,1,'小计','小计') )\n" + 
			"union\n" + 
			"select x.mingc as shouhr,\n" + 
			"       p.mingc as pinz,\n" + 
			"       decode(1,1,'累计','累计') as leix,\n" + 
			"       sum(z.zhuangcl) as meil,\n" + 
			"       sum(ls.qnet_ar*z.zhuangcl) as qnet_ar,\n" + 
			"       sum(ls.mt*z.zhuangcl) as mt,\n" + 
			"       sum(ls.mad*z.zhuangcl)as mad,\n" + 
			"       sum(ls.aad*z.zhuangcl) as aad,\n" + 
			"       sum(ls.vdaf*z.zhuangcl) as vdaf,\n" + 
			"       sum(ls.stad*z.zhuangcl) as stad,\n" + 
			"       sum(ls.had*z.zhuangcl) as had\n" + 
			"from zhuangcb z,vwxuqdw x,vwpinz p,zhillsb ls,zhilb zl,caiyb c,caiylbb lb,diancxxb dc\n" + 
			"where z.xiaosgysb_id = x.id\n" + 
			"      and z.pinzb_id = p.id\n" + 
			"      and z.zhilb_id = zl.id\n" + 
			"      and ls.zhilb_id = zl.id\n" + 
			"      and ls.caiyb_id = c.id\n" + 
			"      and c.caiylbb_id = lb.id\n" + 
			"      and lb.mingc = '装船化验'\n" + 
			"      and z.diancxxb_id=dc.id\n" +strDianc+"\n" + 
			leijrq +"\n" + 
			"  group by (x.mingc,p.mingc,decode(1,1,'累计','累计'))) s ,\n" + 
			"\n" + 
			"(select * from\n" + 
			"(select x.mingc as shouhr,\n" + 
			"       p.mingc as pinz\n" + 
			"from zhuangcb z,vwxuqdw x,vwpinz p,zhillsb ls,zhilb zl,caiyb c,caiylbb lb,diancxxb dc\n" + 
			"where z.xiaosgysb_id = x.id\n" + 
			"      and z.pinzb_id = p.id\n" + 
			"      and z.zhilb_id = zl.id\n" + 
			"      and ls.zhilb_id = zl.id\n" + 
			"      and ls.caiyb_id = c.id\n" + 
			"      and c.caiylbb_id = lb.id\n" + 
			"      and z.diancxxb_id=dc.id\n" + 
			"      and lb.mingc = '装船化验'\n" + 
			"      and z.diancxxb_id=dc.id\n" +strDianc+"\n" + 
			leijrq +"\n" + ")t1,\n" + 
			" (select decode(1,1,'本月','本月') as leix from dual union select decode(1,1,'累计','累计') as leix from dual ) t2 ) tb\n" + 
			" where tb.shouhr=s.shouhr(+) and tb.pinz=s.pinz(+) and tb.leix=s.leix(+)\n" + 
			" group by rollup (tb.leix,tb.shouhr,tb.pinz)\n" + 
			" having not grouping(tb.leix)=1\n" + 
			"order by grouping(tb.shouhr) desc ,shouhr,grouping(tb.pinz) desc ,tb.pinz,leix";


		strSQL.append(sql);
		String ArrHeader[][]=new String[1][11];
		ArrHeader[0]=new String[] {Local.shouhr_jies,Local.pinz,Local.dangyue_leij_yueb,Local.meil_yueb,Local.Qnet_ar,Local.Mt,Local.Mad,Local.Aad,Local.Vdaf,Local.Stad,Local.Had};

		int ArrWidth[]=new int[] {120,80,80,80,95,65,65,65,65,65,65};


		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		
		Table tb=new Table(rs, 1, 0, 2);
		rt.setBody(tb);
		
		rt.setTitle("装船月报", ArrWidth);
		rt.setDefaultTitle(1, 8, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 2,"制表时间："+ getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 4, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 3, "制表:", Table.ALIGN_LEFT);
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
	}
//	******************************************************************************
	
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");	
//		 树
				tb1.addText(new ToolbarText("单位名称:"));
				DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+((Visit)getPage().getVisit()).getDiancxxb_id(),"",null,getTreeid());
				((Visit)getPage().getVisit()).setDefaultTree(dt);
				TextField tf = new TextField();
				tf.setId("diancTree_text");
				tf.setWidth(100);
				tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

				ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
				tb2.setIcon("ext/resources/images/list-items.gif");
				tb2.setCls("x-btn-icon");
				tb2.setMinWidth(20);
				tb1.addField(tf);
				tb1.addItem(tb2);
				tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}

	
//	添加电厂树
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
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
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


}