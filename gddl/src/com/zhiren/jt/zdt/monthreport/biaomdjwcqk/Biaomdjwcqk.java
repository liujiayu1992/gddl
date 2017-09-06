package com.zhiren.jt.zdt.monthreport.biaomdjwcqk;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;


public class Biaomdjwcqk  extends BasePage implements PageValidateListener{

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
			//this.getSelectData();
		}
	}
	
	private void Refurbish() {
        //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
		
		}
		
		getToolBars() ;
		this.Refurbish();
	}
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		return getSelectData();
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
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		String strMonth="";
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		if(intMonth<10){
			strMonth="0"+intMonth;
		}else{
			strMonth=intMonth+"";
		}
		int jib11=0,ranlgs11=0;
		String sql="select jib,diancxxb.ranlgs\n" +
		"from diancxxb\n" + 
		"where id="+getTreeid();
		JDBCcon con11=new JDBCcon();
		ResultSet rs11=con11.getResultSet(sql);
		try {
			while(rs11.next()){
				jib11=rs11.getInt("jib") ;
				ranlgs11=rs11.getInt("ranlgs") ;
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		con11.Close();
		String diancWhere="";
		String YearMon=intyear+"-"+strMonth;
		String having="";
		if(jib11==2&&ranlgs11==1){//燃料公司
			return "燃料公司指标无法查看";
		}else if(jib11==2&&ranlgs11!=1){//分公司
			diancWhere=" and d.fuid="+getTreeid()+" ";
			having=" having not (grouping(mingc)=1 and grouping(fgsmc)=1)";
		}else if(jib11==3){//电厂
			diancWhere=" and d.id="+getTreeid()+" ";
			having=" having(grouping(mingc)<>1 and grouping(fgsmc)<>1)";
		}else{//集团
			diancWhere=" ";
			having="";
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		strSQL=
//			"select mingc,round(sum(benybmdj),2)benybmdj,round(sum(leijbmdj),2)leijbmdj,sum(nianzbl)nianzbl,\n" +
//			"round(sum(benybmdj),2)-sum(nianzbl) cha1,\n" + 
//			"round(sum(leijbmdj),2)-sum(nianzbl) cha2\n" + 
//			"from(\n" + 
//			"select fgsmc,decode(grouping(mingc),1,decode(grouping(fgsmc),0,fgsmc,'中电投'),mingc)mingc,decode(sum(benylmsl),0,0,sum(benybmdj*benylmsl)/sum(benylmsl))benybmdj,\n" + 
//			"decode(sum(leijlmsl),0,0,sum(leijbmdj*leijlmsl)/sum(leijlmsl))leijbmdj,0 nianzbl\n" + 
//			"from(\n" + 
//			"select fgsmc,mingc,decode(sum(benylmsl),0,0,round(round(sum(benydczhj*benylmsl)/sum(benylmsl),2)*29.271/(round(sum(benylmzl*benylmsl)/sum(benylmsl),2)),2))benybmdj,\n" + 
//			"decode(sum(leijlmsl),0,0,round(round(sum(leijdczhj*leijlmsl)/sum(leijlmsl),2)*29.271/(round(sum(leijlmzl*leijlmsl)/sum(leijlmsl),2)),2))leijbmdj,\n" + 
//			"sum(benylmsl)benylmsl,sum(leijlmsl)leijlmsl ,0 nianzbl\n" + 
//			"from(\n" + 
//			"select t.riq,d.fgsmc,d.mingc,(y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs) benydczhj,0 leijdczhj,s.laimsl benylmsl,0 leijlmsl,z.qnet_ar benylmzl,0 leijlmzl\n" + 
//			"from yuercbmdj y,yuetjkjb t,vwdianc d,yueslb s,yuezlb z\n" + 
//			"where z.yuetjkjb_id=t.id and z.fenx=y.fenx and t.diancxxb_id=d.id and y.yuetjkjb_id=t.id and y.fenx='本月'and y.yuetjkjb_id=s.yuetjkjb_id and y.fenx=s.fenx and y.zhuangt=2 and s.zhuangt=2\n" + 
//			" and to_char(t.riq,'yyyy-mm')='"+YearMon+"'"+diancWhere+"\n" + 
//			"union\n" + 
//			"select t.riq,d.fgsmc,d.mingc,0 benydczhj,(y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)  leijdczhj,0 benylmsl,s.laimsl leijlmsl,0 benylmzl,z.qnet_ar leijlmzl\n" + 
//			"from yuercbmdj y,yuetjkjb t,vwdianc d,yueslb s,yuezlb z\n" + 
//			"where z.yuetjkjb_id=t.id and z.fenx=y.fenx and t.diancxxb_id=d.id and y.yuetjkjb_id=t.id and y.fenx='累计'and y.yuetjkjb_id=s.yuetjkjb_id and y.fenx=s.fenx and y.zhuangt=2 and s.zhuangt=2\n" + 
//			" and to_char(t.riq,'yyyy-mm')='"+YearMon+"'"+diancWhere+"\n" + 
//			")\n" + 
//			"group by  fgsmc,mingc\n" + 
//			")group by rollup(fgsmc,mingc) --having not (grouping(mingc)=1 and grouping(fgsmc)=1)\n" + 
//			"\n" + 
//			"/* having   (grouping(mingc)<>1 and grouping(fgsmc)<>1)电厂*/\n" + 
//			"union\n" + 
//			"select d.fgsmc,mingc,0 benybmdj,0 leijbmdj,rucbmdjws nianzbl\n" + 
//			"from jjfxnzbwh j,diancxxb d\n" + 
//			"where to_char(j.riq,'yyyy')='"+intyear+"' and j.id="+getTreeid()+"\n" + 
//			")group by fgsmc,mingc\n" + 
//			"order by decode(mingc,'中电投',0,1),fgsmc,decode(fgsmc,mingc,0,1),mingc";

			"select mingc mingc1, benybmdj,leijbmdj,nianzbl,cha1,cha2\n" +
			"from(\n" + 
			"select decode(diancxxb.jib,2,diancxxb.id,diancxxb.fuid)fuid1,a.mingc,benybmdj,leijbmdj,nianzbl,cha1,cha2,diancxxb.xuh,diancxxb.jib\n" + 
			"from(\n" + 
			"select  mingc,round(sum(benybmdj),2)benybmdj,round(sum(leijbmdj),2)leijbmdj,sum(nianzbl)nianzbl,\n" + 
			"round(sum(benybmdj),2)-sum(nianzbl) cha1,\n" + 
			"round(sum(leijbmdj),2)-sum(nianzbl) cha2\n" + 
			"from(\n" + 
			"select decode(grouping(mingc),1,decode(grouping(fgsmc),0,fgsmc,'中电投'),mingc)mingc,decode(sum(benylmsl),0,0,sum(benybmdj*benylmsl)/sum(benylmsl))benybmdj,\n" + 
			"decode(sum(leijlmsl),0,0,sum(leijbmdj*leijlmsl)/sum(leijlmsl))leijbmdj,0 nianzbl\n" + 
			"from(\n" + 
			"select fgsmc,mingc,decode(sum(benylmsl),0,0,round(round(sum(benydczhj*benylmsl)/sum(benylmsl),2)*29.271/(round(sum(benylmzl*benylmsl)/sum(benylmsl),2)),2))benybmdj,\n" + 
			"decode(sum(leijlmsl),0,0,round(round(sum(leijdczhj*leijlmsl)/sum(leijlmsl),2)*29.271/(round(sum(leijlmzl*leijlmsl)/sum(leijlmsl),2)),2))leijbmdj,\n" + 
			"sum(benylmsl)benylmsl,sum(leijlmsl)leijlmsl ,0 nianzbl\n" + 
			"from(\n" + 
			"select t.riq,d.fgsmc,d.mingc,(y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs) benydczhj,0 leijdczhj,s.laimsl benylmsl,0 leijlmsl,z.qnet_ar benylmzl,0 leijlmzl\n" + 
			"from yuercbmdj y,yuetjkjb t,vwdianc d,yueslb s,yuezlb z\n" + 
			"where z.yuetjkjb_id=t.id and z.fenx=y.fenx and t.diancxxb_id=d.id and y.yuetjkjb_id=t.id and y.fenx='本月'and y.yuetjkjb_id=s.yuetjkjb_id and y.fenx=s.fenx and y.zhuangt=2 and s.zhuangt=2\n" + 
			" and to_char(t.riq,'yyyy-mm')='"+YearMon+"'"+diancWhere+"\n" + 
			"union\n" + 
			"select t.riq,d.fgsmc,d.mingc,0 benydczhj,(y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)  leijdczhj,0 benylmsl,s.laimsl leijlmsl,0 benylmzl,z.qnet_ar leijlmzl\n" + 
			"from yuercbmdj y,yuetjkjb t,vwdianc d,yueslb s,yuezlb z\n" + 
			"where z.yuetjkjb_id=t.id and z.fenx=y.fenx and t.diancxxb_id=d.id and y.yuetjkjb_id=t.id and y.fenx='累计'and y.yuetjkjb_id=s.yuetjkjb_id and y.fenx=s.fenx and y.zhuangt=2 and s.zhuangt=2\n" + 
			" and to_char(t.riq,'yyyy-mm')='"+YearMon+"'"+diancWhere+"\n" + 
			")\n" + 
			"group by fgsmc, mingc\n" + 
			")group by rollup(fgsmc,mingc) "+
			having + 
			"\n" + 
			"union\n" + 
			"select d.mingc,0 benybmdj,0 leijbmdj,rucbmdjws nianzbl\n" + 
			"from jjfxnzbwh j,diancxxb d\n" + 
			"where j.diancxxb_id=d.id and to_char(j.riq,'yyyy')='"+intyear+"'and d.id in(select id from diancxxb\n" +
          	"start with id=" + getTreeid()+
          	"connect by prior fuid= id)" + 
			")group by  mingc\n" + 
			")a,diancxxb where a.mingc=diancxxb.mingc)\n" + 
			"order by decode(mingc,'中电投',0,1),fuid1,jib , xuh";


		ArrHeader=new String[2][6];
		ArrHeader[0]=new String[] {"单位","标煤单价","标煤单价","年指标标煤单价","差异","差异"};
		ArrHeader[1]=new String[] {"单位","本月","累计","年指标标煤单价","本月","累计"};
		 
		ArrWidth=new int[] {150,60,60,120,60,60};
//		arrFormat= new String []{"","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00"};
//		 iFixedRows=1;
//		 iCol=10;
			// System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			Table tb = new Table(rs, 2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle( "标煤单价完成情况", ArrWidth);
			rt.setDefaultTitle(1, 2, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			
			rt.setDefaultTitle(3, 2, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
//			if(rt.body.getRows()>4){
//				rt.body.setCellAlign(5, 1, Table.ALIGN_CENTER);
//			}
		
//			rt.body.setColFormat(arrFormat);
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,2,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(3,2,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(5,1,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
//	得到系统信息表中配置的报表标题的单位名称
//	public String getBiaotmc(){
//		String biaotmc="";
//		JDBCcon cn = new JDBCcon();
//		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
//		ResultSet rs=cn.getResultSet(sql_biaotmc);
//		try {
//			while(rs.next()){
//				 biaotmc=rs.getString("zhi");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}finally{
//			cn.Close();
//		}
//			
//		return biaotmc;
//		
//	}
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
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}

//	年份
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
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setWidth(120);
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));
		
		
		
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
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

	
}