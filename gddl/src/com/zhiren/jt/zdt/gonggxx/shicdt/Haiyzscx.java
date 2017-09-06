

	package com.zhiren.jt.zdt.gonggxx.shicdt;
	/* 
	* 时间：2009-07-27
	* 作者： ll
	* 修改内容：1、热值单位转换为Kcal/kg；
	*          2、修改入厂标煤单价查询公式。
	* 		   
	*/ 
	/* 
	* 时间：2009-07-31
	* 作者： ll
	* 修改内容：1、修改入厂热值，判断电厂id为141(芜湖电厂)，
	* 入厂低位热值取yuezlb中的diancrz字段，其它电厂入厂低位热值取yuezlb中的qnet_ar字段
	* 		   
	*/ 
	/* 
	* 时间：2009-08-29
	* 作者： ll
	* 修改内容：1、修改入厂热值，判断芜湖电厂求热值差时入厂低位热值取yuezlb中的diancrz字段计算，
	* 求标煤单价时取入厂低位热值取yuezlb中的qnet_ar字段计算
	* 		   
	*/ 
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

	import com.zhiren.common.DateUtil;
	import com.zhiren.common.IDropDownBean;
	import com.zhiren.common.IDropDownModel;
	import com.zhiren.common.JDBCcon;
	import com.zhiren.main.Visit;
	import com.zhiren.main.validate.Login;
	import com.zhiren.report.Report;
	import com.zhiren.report.Table;
	import com.zhiren.common.ext.ExtTreeUtil;
	import com.zhiren.common.ext.Toolbar;
	import com.zhiren.common.ext.ToolbarButton;
	import com.zhiren.common.ext.ToolbarText;
	import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
	import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;



	public class Haiyzscx   extends BasePage implements PageValidateListener{
		
//		 判断是否是集团用户
		public boolean isJitUserShow() {
			return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

		}

		public boolean isGongsUser() {
			return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
		}

		public boolean isDiancUser() {
			return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
		}
		
		//开始日期
		private Date _BeginriqValue = new Date();
//		private boolean _BeginriqChange=false;
		public Date getBeginriqDate() {
			if (_BeginriqValue==null){
				_BeginriqValue = new Date();
			}
			return _BeginriqValue;
		}
		
		public void setBeginriqDate(Date _value) {
			if (_BeginriqValue.equals(_value)) {
//				_BeginriqChange=false;
			} else {
				_BeginriqValue = _value;
//				_BeginriqChange=true;
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
				Refurbish();
			}
		}
		
		private void Refurbish() {
	        //为 "刷新" 按钮添加处理程序
			isBegin=true;
			getSelectData();
		}

//	******************页面初始设置********************//
		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

			Visit visit = (Visit) getPage().getVisit();
			
			if (!visit.getActivePageName().toString().equals(
					this.getPageName().toString())) {
				// 在此添加，在页面第一次加载时需要置为空的变量或方法
				visit.setActivePageName(getPageName().toString());
				setBaoblxValue(null);
				this.setIBaoblxModel(null);
				this.setRiq1(getZuiwrq());
				visit.setList1(null);
				setNianfValue(null);
				setYuefValue(null);
				getNianfModels();
				getYuefModels();
				this.setTreeid(null);
				this.getTree();
				visit.setDropDownBean4(null);
				visit.setProSelectionModel4(null);
				visit.setDropDownBean11(null);
				visit.setProSelectionModel11(null);
				
				isBegin=true;
				
			}
			if(_Baoblxchange){
				_Baoblxchange=false;
				this.setRiq1(getZuiwrq());
			}
		
			getToolBars() ;
			Refurbish();
		}
		
		private String RT_HET="Fadbmdjqkbreport";//发电标煤单价表
		private String mstrReportName="Fadbmdjqkbreport";
		
		public String getPrintTable(){
			if(!isBegin){
				return "";
			}
			isBegin=false;
			if (mstrReportName.equals(RT_HET)){
				return getSelectData();
			}else{
				return "无此报表";
			}
		}
		
		public int getZhuangt() {
			return 1;
		}

		private int intZhuangt=0;
		public void setZhuangt(int _value) {
			intZhuangt=1;
		}

		private boolean isBegin=false;
		private String getSelectData(){
			String strSQL="";
			_CurrentPage=1;
			_AllPages=1;
			
			Visit visit = (Visit) getPage().getVisit();
			JDBCcon cn = new JDBCcon();
			String zhuangt_sl="";
			String zhuangt_zb="";
			String zhuangt_shcy="";
				if(visit.getRenyjb()==3){
					zhuangt_sl="";
					zhuangt_zb="";
				}else if(visit.getRenyjb()==2){
					zhuangt_sl=" and (sl.zhuangt=1 or sl.zhuangt=2) and (zl.zhuangt=1 or zl.zhuangt=2) and (y.zhuangt=1 or y.zhuangt=2) ";
					zhuangt_zb=" and (zb.zhuangt=1 or zb.zhuangt=2)";
					zhuangt_shcy=" and (shcy.zhuangt=1 or shcy.zhuangt=2)";
				}else if(visit.getRenyjb()==1){
					zhuangt_sl=" and  sl.zhuangt=2 and zl.zhuangt=2 and y.zhuangt=2 ";
					zhuangt_zb=" and zb.zhuangt=2";
					zhuangt_shcy=" and shcy.zhuangt=2";
				}
			long intyear;
			if (getNianfValue() == null){
				intyear=DateUtil.getYear(new Date());
			}else{
				intyear=getNianfValue().getId();
			}
			long intMonth;
			if(getYuefValue() == null){
				intMonth = DateUtil.getMonth(new Date());
			}else{
				intMonth = getYuefValue().getId();
			}

			String strGongsID = "";
			String  notHuiz="";
			int jib=this.getDiancTreeJib();
			if(jib==1){//选集团时刷新出所有的电厂
				strGongsID=" ";
				notHuiz="";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂

				strGongsID = "  and (dc.fuid= "+this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
				notHuiz=" having not grouping(dc.fgsmc)=1 ";//当电厂树是分公司时,去掉集团汇总
			}else if (jib==3){//选电厂只刷新出该电厂
				strGongsID=" and dc.id= " +this.getTreeid();
				notHuiz="having not grouping(dc.mingc)=1";//当电厂树是电厂时,去掉分公司和集团汇总
			}else if (jib==-1){
				strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			 
			//报表表头定义
			Report rt = new Report();
			int ArrWidth[] = null;
			String ArrHeader[][] = null;
			
			int iFixedRows=0;//固定行号
			int iCol=0;//列数
			
			
			String strt_sl="";
			String strt_zb="";
			String strt_shcy="";
			String strw="";
			//报表内容
			String biaot="";
			if(getBaoblxValue().getValue().equals("国际运价指数")){		
				strt_sl="and sl.fenx = '本月' ";
				strt_zb="and zb.fenx='本月' ";
				strt_shcy="and shcy.fenx='本月' ";
				strw="-1";
				biaot="国际运价指数";
				 ArrHeader =new String[1][5];
				 ArrHeader[0]=new String[] {"项目名称","市场运价","本周指数","上周相比涨跌情况(%)"};

				 ArrWidth =new int[] {260,70,70,70};

			}else if(getBaoblxValue().getValue().equals("国内运价指数")){
				strt_sl="and sl.fenx = '累计'";
				strt_zb="and zb.fenx='累计' ";
				strt_shcy="and shcy.fenx='累计' ";
				strw="-12";
				biaot="国内运价指数";
				 ArrHeader =new String[1][5];
				 ArrHeader[0]=new String[] {"项目名称","市场运价","本周指数","上周相比涨跌情况(%)"};

				 ArrWidth =new int[] {260,70,70,70};
			}
					strSQL=
		           "select  i.mingc itemmingc,yun.shicyj,yun.benzzs,yun.shangzxbzd \n"+
                   "from (select * from YUNJZSHQB yund where yund.riq=to_date('"+this.getRiq1()+"','yyyy-mm-dd') ) yun,item i,itemsort it\n"+
                    "  where yun.item_id(+)=i.id\n"+
                     " and it.id=i.itemsortid\n"+
                     " and it.mingc in('"+this.getBaoblxValue().getValue()+"')\n"+
                     " order by it.xuh";

					
//	------------------------------------------------------------------------------------------------
				


					 
				//System.out.println(strSQL);
				ResultSet rs = cn.getResultSet(strSQL);
				 
				// 数据
				
				Table tb = new Table(rs,1, 0, 1);
				rt.setBody(tb);
				
				rt.setTitle(biaot+"表", ArrWidth);
				rt.setDefaultTitle(1, 2, "填报单位(盖章):"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
				rt.setDefaultTitle(2, 4, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
				rt.setDefaultTitle(13, 3, "单位:Kcal/kg、元/吨、吨", Table.ALIGN_RIGHT);

				rt.body.setColAlign(1, Table.ALIGN_LEFT);
				rt.body.setColAlign(2, Table.ALIGN_RIGHT);
				
				rt.body.setWidth(ArrWidth);
				rt.body.setPageRows(18);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.mergeFixedRow();
				rt.body.mergeFixedCols();
				rt.body.ShowZero = false;

//				if(rt.body.getRows()>2){
//					rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
//				}
				
				//页脚 
				 rt.createDefautlFooter(ArrWidth);
				  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
				  rt.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
				  rt.setDefautlFooter(7,2,"制表:",Table.ALIGN_LEFT);
				  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
				 
			
				_CurrentPage=1;
				_AllPages=rt.body.getPages();
				if (_AllPages==0){
					_CurrentPage=0;
				}
				cn.Close();

				return rt.getAllPagesHtml();
		}
		
		//得到系统信息表中配置的报表标题的单位名称
		public String getBiaotmc(){
			String biaotmc="";
			JDBCcon cn = new JDBCcon();
			String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
			ResultSet rs=cn.getResultSet(sql_biaotmc);
			try {
				while(rs.next()){
					 biaotmc=rs.getString("zhi");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				cn.Close();
			}
				
			return biaotmc;
			
		}
//		电厂名称
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
			
			String sql="";
			sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
			_IDiancmcModel = new IDropDownModel(sql);
			

		}
		
//		年份
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
				for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
					Object obj = _NianfModel.getOption(i);
					if (_nianf == ((IDropDownBean) obj).getId()) {
						_NianfValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _NianfValue;
		}
		public boolean nianfchanged = false;
		public void setNianfValue(IDropDownBean Value) {
			if (_NianfValue != Value) {
				nianfchanged = true;
			}
			_NianfValue = Value;
		}

		public IPropertySelectionModel getNianfModels() {
			List listNianf = new ArrayList();
			int i;
			for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
				listNianf.add(new IDropDownBean(i, String.valueOf(i)));
			}
			_NianfModel = new IDropDownModel(listNianf);
			return _NianfModel;
		}

		public void setNianfModel(IPropertySelectionModel _value) {
			_NianfModel = _value;
		}
		
		/**
		 * 月份
		 */
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
				for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
					Object obj = _YuefModel.getOption(i);
					if (_yuef == ((IDropDownBean) obj).getId()) {
						_YuefValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _YuefValue;
		}
		public boolean yuefchanged = false;
		public void setYuefValue(IDropDownBean Value) {
			if (_YuefValue != Value) {
				yuefchanged = true;
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

//		 报表类型
		public boolean _Baoblxchange = false;
		private IDropDownBean _BaoblxValue;

		public IDropDownBean getBaoblxValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
				((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getIBaoblxModels().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean11();
		}

		public void setBaoblxValue(IDropDownBean Value) {
			long id = -2;
			if (((Visit)getPage().getVisit()).getDropDownBean11() != null) {
				id = ((Visit)getPage().getVisit()).getDropDownBean11().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_Baoblxchange = true;
				} else {
					_Baoblxchange = false;
				}
			}
			((Visit)getPage().getVisit()).setDropDownBean11(Value);
		}

		private IPropertySelectionModel _IBaoblxModel;

		public void setIBaoblxModel(IPropertySelectionModel value) {
			((Visit)getPage().getVisit()).setProSelectionModel11(value);
		}

		public IPropertySelectionModel getIBaoblxModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel11() == null) {
				getIBaoblxModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel11();
		}

		public IPropertySelectionModel getIBaoblxModels() {
			
			List List = new ArrayList();
			List.add(new IDropDownBean(0,"国际运价指数"));
			List.add(new IDropDownBean(1,"国内运价指数"));
			
			((Visit)getPage().getVisit()).setProSelectionModel11(new IDropDownModel(List));
			
			return ((Visit)getPage().getVisit()).getProSelectionModel11();
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

		private String FormatDate(Date _date) {
			if (_date == null) {
				return "";
			}
			return DateUtil.Formatdate("yyyy年MM月dd日", _date);
		}
		
//		***************************报表初始设置***************************//
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
		 //	页面判定方法
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

		
		
		
		
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}finally{
				con.Close();
			}

			return jib;
		}

		public void getToolBars() {
			Toolbar tb1 = new Toolbar("tbdiv");
			
			
			
//			tb1.addText(new ToolbarText("年份:"));
//			ComboBox nianf = new ComboBox();
//			nianf.setTransform("NIANF");
//			nianf.setWidth(60);
//			nianf.setListeners("select:function(){document.Form0.submit();}");
//			tb1.addField(nianf);
//			tb1.addText(new ToolbarText("-"));
//			
//			
//
//			tb1.addText(new ToolbarText("月份:"));
//			ComboBox yuef = new ComboBox();
//			yuef.setTransform("YUEF");
//			yuef.setWidth(60);
//			yuef.setListeners("select:function(){document.Form0.submit();}");
//			tb1.addField(yuef);
//			tb1.addText(new ToolbarText("-"));
			
			
			tb1.addText(new ToolbarText("日期:"));
			DateField tba_df=new DateField();
			tba_df.setValue(this.getRiq1());
			tba_df.Binding("riq1","forms[0]");// 与html页中的id绑定,并自动刷新
			tba_df.setWidth(100);    
		    tb1.addField(tba_df);
			
			
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
			
//			tb1.addText(new ToolbarText("单位:"));
//			tb1.addField(tf);
//			tb1.addItem(tb2);
			tb1.addText(new ToolbarText("-"));
			
			tb1.addText(new ToolbarText("类型:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BAOBLX");
			cb.setWidth(120);
			cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			
			tb1.addText(new ToolbarText("-"));
			
			ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
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

		private String treeid;

		/*public String getTreeid() {
			if (treeid == null || "".equals(treeid)) {
				return "-1";
			}
			return treeid;
		}

		public void setTreeid(String treeid) {
			this.treeid = treeid;
		}*/
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

//		日期相关_begin
		private String riq1;
		public String getRiq1() {
			if(riq1==null||riq1.equals("")){
				riq1=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -15, DateUtil.AddType_intDay));
			}
			return riq1;
		}
		public void setRiq1(String riq1) {
			this.riq1 = riq1;
		}
//		日期相关_结束
		
		public String getZuiwrq(){
			JDBCcon con=new JDBCcon();
			String sql="select to_char(yun.riq,'yyyy-mm-dd') maxriq from  YUNJZSHQB  yun,item i,itemsort it \n"+
	                    " where yun.item_id =i.id\n"+
	                     " and it.id=i.itemsortid\n"+
	                     " and it.mingc in('"+this.getBaoblxValue().getValue()+"')\n";
			
			 ResultSet rsl= con.getResultSet(sql);
			 String maxriq="";
			 try {
				if(rsl.next()){
					 maxriq=rsl.getString("maxriq");
				 }else{
					 maxriq=DateUtil.FormatDate(new Date());
				 }
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			  con.Close();
			  return maxriq;
		}
		
		
	}
