package com.zhiren.jt.jihgl.nianxqjhh;

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

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


public class Xinzjznhycx  extends BasePage implements PageValidateListener{

	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	
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
	
	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getEndriqDateSelect() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setEndriqDateSelect(String after) {
		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}


//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.getSelectData();
		}
		isBegin=true;
		getToolBars();
		this.getSelectData();
		
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
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
//		年，月
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		int jib=this.getDiancTreeJib();
		
		StringBuffer strSQL = new StringBuffer();
		
		if (jib==1) {//选集团时刷新出所有的电厂
			strSQL.append("select decode(grouping(vdc.fgsmc)+grouping(vdc.mingc),2,'总计',1,vdc.fgsmc,vdc.mingc) as danwmc,\n");
			strSQL.append("sf.quanc,\n");
			strSQL.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
			strSQL.append("n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
//			strSQL.append("from nianxqjhh n,\n");
			strSQL.append("from (select * from nianxqjhh where nianf = "+intyear+" and shujzt='"+intyear+"' and jizzt=1) n,\n ");
			strSQL.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
			strSQL.append("Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
			strSQL.append("from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
			strSQL.append("and to_char(y.riq,'yyyy')=").append(intyear);
			strSQL.append("group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
			strSQL.append("where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
			strSQL.append("and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
			strSQL.append("and n.nianf=").append(intyear);
			strSQL.append("group by rollup (vdc.fgsmc,vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
			strSQL.append("having not (grouping(vdc.mingc) || grouping(n.beiz)) =1 \n");
			strSQL.append("order by grouping(vdc.fgsmc) desc,vdc.fgsmc,grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
			strSQL.append("grouping(sf.quanc) desc,sf.quanc \n");
			
		}else if(jib==2) {//选分公司的时候刷新出分公司下所有的电厂

			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();

			try{
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//燃料公司
					strSQL.append("select decode(grouping(vdc.rlgsmc)+grouping(vdc.mingc),2,'总计',1,vdc.rlgsmc,vdc.mingc) as danwmc,\n");
					strSQL.append("sf.quanc,\n");
					strSQL.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
					strSQL.append("n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
					//strSQL.append("from nianxqjhh n,\n");
					strSQL.append("from (select * from nianxqjhh where nianf = "+intyear+" and shujzt='"+intyear+"' and jizzt=1) n,\n ");
					strSQL.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
					strSQL.append("Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
					strSQL.append("from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
					strSQL.append("and to_char(y.riq,'yyyy')=").append(intyear);
					strSQL.append("group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
					strSQL.append("where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
					strSQL.append("and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
					strSQL.append("and n.nianf=").append(intyear);
					strSQL.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					strSQL.append("group by rollup (vdc.rlgsmc,vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
					strSQL.append("having not (grouping(vdc.mingc) || grouping(n.beiz)) =1 \n");
					strSQL.append("order by grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
					strSQL.append("grouping(sf.quanc) desc,sf.quanc \n");
				}else{
					strSQL.append("select decode(grouping(vdc.fgsmc)+grouping(vdc.mingc),2,'总计',1,vdc.fgsmc,vdc.mingc) as danwmc,\n");
					strSQL.append("sf.quanc,\n");
					strSQL.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
					strSQL.append("n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
//					strSQL.append("from nianxqjhh n,\n");
					strSQL.append("from (select * from nianxqjhh where nianf = "+intyear+" and shujzt='"+intyear+"' and jizzt=1) n,\n ");
					strSQL.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
					strSQL.append("Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
					strSQL.append("from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
					strSQL.append("and to_char(y.riq,'yyyy')=").append(intyear);
					strSQL.append("group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
					strSQL.append("where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
					strSQL.append("and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
					strSQL.append("and n.nianf=").append(intyear);
					strSQL.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					strSQL.append("group by rollup (vdc.fgsmc,vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
					strSQL.append("having not (grouping(vdc.mingc) || grouping(n.beiz)) =1 \n");
					strSQL.append("order by grouping(vdc.fgsmc) desc,vdc.fgsmc,grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
					strSQL.append("grouping(sf.quanc) desc,sf.quanc \n");
				}
				rl.close();

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}

		}else{//选择电厂
			strSQL.append("select decode(grouping(vdc.mingc),1,'总计',vdc.mingc) as danwmc,\n");
			strSQL.append("sf.quanc,\n");
			strSQL.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
			strSQL.append("n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,sum(n.xuyyml) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
//			strSQL.append("from nianxqjhh n,\n");
			strSQL.append("from (select * from nianxqjhh where nianf = "+intyear+" and shujzt='"+intyear+"' and jizzt=1) n,\n ");
			strSQL.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
			strSQL.append("Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
			strSQL.append("from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
			strSQL.append("and to_char(y.riq,'yyyy')=").append(intyear);
			strSQL.append("group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
			strSQL.append("where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
			strSQL.append("and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
			strSQL.append("and n.nianf=").append(intyear);
			strSQL.append(" and dc.id=").append(this.getTreeid()).append("\n");
			strSQL.append("group by rollup (vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
			strSQL.append("having not (grouping(sf.quanc) || grouping(n.beiz)) =1 \n");
			strSQL.append("order by grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
			strSQL.append("grouping(sf.quanc) desc,sf.quanc \n");
		}
			
		 String ArrHeader[][]=new String[1][11];
		 ArrHeader[0]=new String[] {"电厂名称","所属省份","装机容量<br>（万千瓦）","投产日期","计划电量<br>（亿千瓦时）","设计煤种","主要需求供煤单位及数量<br>(万吨)","需求煤量<br>（万吨）","到站/中转港","运输方式","备注"};

		 int ArrWidth[]=new int[] {150,70,65,120,80,58,150,58,58,58,60};

		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		
		Table tb=new Table(rs, 1, 0, 2);
		rt.setBody(tb);
		
		rt.setTitle("中电投集团公司"+intyear+"年新增机组电煤需求表", ArrWidth);
		rt.body.setRowHeight(1, 40);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(7, Table.ALIGN_LEFT);
		rt.body.setColAlign(9, Table.ALIGN_LEFT);
		rt.body.setColAlign(10, Table.ALIGN_LEFT);
		rt.body.mergeFixedRow();
		
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
	//年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
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
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("至:"));
//		DateField df1 = new DateField();
//		df1.setReadOnly(true);
//		df1.setValue(this.getEndriqDateSelect());
//		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
//		df1.setId("after");
//		tb1.addField(df1);
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.getElementById('RefurbishButton').click();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
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
	
	private String treeid;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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

//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}

}