package com.zhiren.dc.huaygl.huaybb.huaybgd;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author wzb
 * 合山化验报告单
 */
public class Huaybgd_hs  extends BasePage implements PageValidateListener{

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
	
//	private static final String BAOBPZB_GUANJZ = "CAIYLBCX";// baobpzb中对应的关键字
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	private boolean rqchange = false;//判断日期是否改变
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if (this.briq!=null){
			if(!this.briq.equals(briq)){
				rqchange = true;
			}
		}
		
		this.briq = briq;
	}
	
	
	//结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		if (this.eriq!=null){
			if(!this.eriq.equals(eriq)){
				rqchange = true;
			}
		}
		
		this.eriq = eriq;
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
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			
			getBianhModels();
			setBianhValue(null);
			getBianhModel();
			this.getSelectData();
		}else if(rqchange){
			getBianhModels();
			setBianhValue(null);
			getBianhModel();
		}
		rqchange = false;
		isBegin = true;
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
	
	private String getCaiySql(){
		String riq=this.getBRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		
		String	sSql =  "select decode(y.caiyrq,null,' ',TO_CHAR(Y.CAIYRQ, 'YYYY-MM-DD')) AS CAIYRQ,\n"
				+ "       decode(ls.lury,null,' ',ls.lury) as lurry,\n"
				+ "       TO_CHAR(ls.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,\n"
				+ "       round_new(ls.mt,2) as MT,round_new(ls.mad,2) as MAD,\n"
				+ "       round_new(ls.vdaf,2) as VDAF,round_new(ls.stad,2) as STAD,\n"
				+ "       round_new(ls.aad,2) as AAD,\n"
				+ "       round_new(ls.qbad,2) as QBAD,\n"
				+ "       round_new(ls.qgrad,2) as QGRAD,\n"
				+ "       round_new(ls.qnet_ar,2) as QNETAR,\n"
				+ "       decode(ls.huayy,null,' ',ls.huayy) as huayy,ls.shenhry as shenhry,ls.shenhryej as shenhry2,a.kaissj,a.jiessj,\n"
				+ "       '"+this.getBianhValue().getValue()+"' as bianh,a.meikdwmc,a.chez,a.pinz,a.cheph,a.ches,a.meil,ls.beiz,a.heth from zhilb z,caiyb y,zhillsb ls,\n"
				+ "    (select distinct m.mingc as meikdwmc,\n"
				+ "    cz.mingc as chez,\n"
				+ "    p.mingc as pinz,\n"
				+ "     f.zhilb_id as zhilb_id,min(c.zhongcsj) as kaissj,max(c.qingcsj) as jiessj,\n"
				+ "    round_new(sum(c.maoz-c.piz-c.koud), 2) as meil,max(h.hetbh) as heth,\n"
				+ "    count(c.id) AS CHES,\n"
				+ "    GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH\n"
				+ "    from fahb f, zhilb z,meikxxb m,chezxxb cz,pinzb p,hetb h,chepb c,zhillsb ls\n"
				+ "    where z.id = f.zhilb_id\n"
				+ "     and f.pinzb_id=p.id\n"
				+ "    and f.faz_id=cz.id\n"
				+ "     and f.hetb_id=h.id(+)\n"
				+ "    and f.meikxxb_id=m.id\n"
				+ "    and f.id=c.fahb_id\n"
				+ "    and z.id=ls.zhilb_id\n"
				+ "    and ls.id ="+this.getBianhValue().getId()+"\n"
				+ "    group by m.mingc,cz.mingc,p.mingc,f.zhilb_id) a\n"
				+ "where z.id=a.zhilb_id\n"
				+ "and z.caiyb_id=y.id\n"
				+ "and y.zhilb_id=z.id\n"
				+ "and z.id=ls.zhilb_id\n"
				+ "and ls.id = "+this.getBianhValue().getId()+"\n";



		return sSql;
	}
	
	private String getCaizrSql(){
		String riq=this.getBRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		String	sSql =  
			"select distinct r.quanc as caizr,y.beiz as beiz\n" +
			"from fahb f,meikxxb m,hetb h,chepb c,gongysb g,caiyb cy,zhillsb z,zhuanmb zm,yangpdhb y,caiyryglb cgl,renyxxb r\n" + 
			"where f.meikxxb_id = m.id\n" + 
			"      and f.hetb_id = h.id(+)\n" + 
			"      and c.fahb_id = f.id\n" + 
			"      and f.gongysb_id = g.id\n" + 
			"      and f.zhilb_id = z.zhilb_id\n" + 
			"      and zm.zhillsb_id = z.id\n" + 
			"      and zm.zhuanmlb_id =(select id from zhuanmlb lb where lb.mingc='化验编码')\n" + 
			"      and zm.bianm = '" + getBianhValue().getValue() + "'\n" + 
			"      and cy.zhilb_id = f.zhilb_id\n" + 
			"      and y.caiyb_id = cy.id\n" + 
			"      and cgl.yangpdhb_id = y.id\n" + 
			"      and cgl.renyxxb_id = r.id\n";

		return sSql;
	}
	
	

	
	
	

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		String sql = this.getCaiySql();
		ResultSetList rs = con.getResultSetList(sql);
		String caiyrq="";
		String huayrq="";
		String Mt="";
		String Mad="";
		String Vdaf="";
		String Stad="";
		String Aad="";
		String Qbad="";
		String Qgrad="";
		String Qnetar="";
		String huayy="";
		String shenhry="";
		String huaybh="";
		String meik="";
		String faz="";
		String cheph1="";
		String ches="";
		String meil="";
		String heth="";
		String kaissj="";
		String jiessj="";
		String shenhry1="";
		String shenhry2="";
		

		while(rs.next()){
			caiyrq = rs.getString("CAIYRQ");
			huayrq = rs.getString("HUAYRQ");
			Mt =String.valueOf( rs.getDouble("MT"));
			Mad=String.valueOf(rs.getDouble("MAD"));
			Vdaf=String.valueOf(rs.getDouble("VDAF"));
			Stad=String.valueOf(rs.getDouble("STAD"));
			 Aad=String.valueOf(rs.getDouble("AAD"));
			 Qbad=String.valueOf(rs.getDouble("QBAD"));
			 Qgrad=String.valueOf(rs.getDouble("QGRAD"));
			 Qnetar=String.valueOf(rs.getDouble("QNETAR"));
			 huayy=rs.getString("huayy");
			 shenhry=rs.getString("shenhry");;
			 huaybh=rs.getString("bianh");
			 meik=rs.getString("meikdwmc");
			 faz=rs.getString("chez");
			 cheph1=rs.getString("cheph");
			 ches=String.valueOf(rs.getLong("ches"));
			 meil=String.valueOf(rs.getDouble("meil"));
			 heth=rs.getString("heth");
			 kaissj=rs.getDateTimeString("kaissj");
			 jiessj=rs.getDateTimeString("jiessj");
			 shenhry1=rs.getString("shenhry");
			 shenhry2=rs.getString("shenhry2");
			 
		}
		if(cheph1!=null||cheph1.equals("")){
			StringBuffer buffer = new StringBuffer();
			String[] list = cheph1.split(",");				
			for (int i = 1; i <= list.length; i++) {
				if (i % 8 == 0) {
					buffer.append(list[i - 1] + ",<br>");
				} else {
					buffer.append(list[i - 1] + ",");
				}
			}
			cheph1 = buffer.toString().substring(0, buffer.length() - 1);	
			
		}
		if(jiessj.equals("1970-01-01 08:00:00")||jiessj==null){
			jiessj=kaissj;
		}
		
		
		String sql_czr = this.getCaizrSql();
		rs = con.getResultSetList(sql_czr);
		String caizr = "";
		String beiz="";
		while(rs.next()){
			caizr += "、";
			caizr += rs.getString("CAIZR");
			beiz=rs.getString("beiz");
		}
		if(!(caizr.equals(null)||caizr.equals(""))){
			caizr = caizr.substring(1);
		}else{
			caizr = " ";
		}
		
		
		
		
		

		String[][] CAIY=new String[][]{
			{"煤样编号","化验日期","化验日期","采制员","采制员","采制员","化验员","化验员","化验员"},
			{huaybh,huayrq,huayrq,caizr,caizr,caizr,huayy,huayy,huayy,},
			{"发站","发站","发站","采样日期","采样日期","采样日期","车数","煤量","煤量"},
			{faz,faz,faz,caiyrq,caiyrq,caiyrq,ches,meil,meil},
			{"合同编号",heth,heth,"矿别:",meik,meik,meik,meik,meik},
			{"过衡开始",kaissj,kaissj,kaissj,"过衡结束时间","过衡结束时间",jiessj,jiessj,jiessj},
			{"分析项目","水份(%)","水份(%)","挥发(%)<br>Vdaf","灰分(%)<br>Aad","硫分(%)<br>Stad","热值(MJ/Kg)","热值(MJ/Kg)","热值(MJ/Kg)"},
			{"分析项目","Mt","Mad","挥发(%)<br>Vdaf","灰分(%)<br>Aad","硫分(%)<br>Stad","Qb,ad","Qgr,ad","Qnet,ar"},
			{"分析项目",Mt,Mad,Vdaf,Aad,Stad,Qbad,Qgrad,Qnetar},
			{"车号列表",cheph1,cheph1,cheph1,cheph1,cheph1,cheph1,cheph1,cheph1},
			{"备注","","","","","","","",""}
		};
		rs.close();
		
		String[][] ArrHeader = new String[11][9];
		int i=0;
		for(int j=0;j<CAIY.length;j++){
			ArrHeader[i++]=CAIY[j];
		}
		int[] ArrWidth = new int[] { 90,65,65,65,65,65,65,65,65};
		
		Table bt=new Table(11,9);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][9];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		rt.setTitle("燃料煤样化验单", ArrWidth);
		
		
		//合并
		rt.body.merge(1, 2, 1, 3);
		rt.body.merge(1, 4, 1, 6);
		rt.body.merge(1, 7, 1, 9);
		
		rt.body.merge(2, 2, 2, 3);
		rt.body.merge(2, 4, 2, 6);
		rt.body.merge(2, 7, 2, 9);
		
		rt.body.merge(3, 1, 3, 3);
		rt.body.merge(3, 4, 3, 6);
		rt.body.merge(3, 8, 3, 9);
		
		rt.body.merge(4, 1, 4, 3);
		rt.body.merge(4, 4, 4, 6);
		rt.body.merge(4, 8, 4, 9);
		
		rt.body.merge(5, 2, 5, 3);
		rt.body.merge(5, 5, 5, 9);
		
		rt.body.merge(6, 2, 6, 4);
		rt.body.merge(6, 5, 6, 6);
		rt.body.merge(6, 7, 6, 9);
		
		rt.body.merge(7, 2, 7, 3);
		rt.body.merge(7, 7, 7, 9);
		
		rt.body.merge(7, 1, 9, 1);
		rt.body.merge(7, 4, 8, 4);
		rt.body.merge(7, 5, 8, 5);
		rt.body.merge(7, 6, 8, 6);
		
		rt.body.merge(10, 2, 10, 9);
		
		rt.body.merge(11, 2, 11, 9);
		
		rt.body.setRowHeight(10, 75);
		
		
		
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(50);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		

		
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(5, 2, "一审:"+shenhry1, Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 3, "二审:"+shenhry2,Table.ALIGN_LEFT);
		rt.body.setCellAlign(5, 5, Table.ALIGN_LEFT);
		//rt.body.setCellVAlign(10, 2, Table.VALIGN_TOP);
		rt.body.setCells(10, 2, 10, 9, Table.PER_ALIGN, Table.ALIGN_LEFT);
		
		rt.body.setCellAlign(11, 2, Table.ALIGN_LEFT);
		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();

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
		
		tb1.addText(new ToolbarText("化验日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "caiF");// 与html页中的id绑定,并自动刷新
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		
		tb1.addText(new ToolbarText("至"));
		DateField dfb1 = new DateField();
		dfb1.setValue(getERiq());
		dfb1.Binding("ERIQ", "caiF");// 与html页中的id绑定,并自动刷新
		dfb1.setId("endriq");
		tb1.addField(dfb1);
		
		
		
		tb1.addText(new ToolbarText("-")); 
		
		tb1.addText(new ToolbarText("化验编号:"));
		ComboBox bh = new ComboBox();
		bh.setTransform("BianhDropDown");
		bh.setEditable(true);
		bh.setWidth(100);
		bh.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(bh);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){if(document.all.BianhDropDown.value==-1){Ext.Msg.alert('提示信息','请选择一个采样单号!'); return;} document.Form0.submit();}");
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

//	编号

	public IDropDownBean getBianhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getBianhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setBianhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setBianhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getBianhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getBianhModels() {
		
		Visit visit = (Visit) getPage().getVisit();

		String sql =

			"select distinct ls.id,zm.bianm from zhilb z,zhillsb ls,zhuanmb zm,fahb f\n" +
			"where z.id=ls.zhilb_id\n" + 
			"and ls.id=zm.zhillsb_id\n" + 
			"and zm.zhuanmlb_id=(select id from zhuanmlb lb where  lb.mingc='化验编码')\n" + 
			"and ls.shenhzt=7\n" + 
			"and ls.huaysj>= "+ DateUtil.FormatOracleDate(getBRiq()) + "\n"+
			"and ls.huaysj<= "+ DateUtil.FormatOracleDate(getERiq()) + "\n"+
			"and f.zhilb_id=z.id\n" + 
			"and f.diancxxb_id="+visit.getDiancxxb_id()+"\n" + 
			"order by zm.bianm";


		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

}