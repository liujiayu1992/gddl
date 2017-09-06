package com.zhiren.dtrlgs.faygl.fayjs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.gangkjy.Local;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.dtrlgs.pubclass.BalanceLiuc;

/*
 * 作者:童忠付
 * 时间:2009-3-26
 * 内容:质量管理入场化验统计查询
 */
public class DcBalancesh extends BasePage implements PageValidateListener {
	
	public boolean getRaw() {
		return true;
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

	// ***************设置消息框******************//
	private String _msg;

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value,false);;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		super.initialize();
		_msg = "";
		_pageLink = "";
	}

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}


	private boolean blnIsBegin = false;

	// private String leix = "";


	public String getPrintTable() {
			Changfjsd jsd = new Changfjsd();
			StringBuffer sb = new StringBuffer();
			sb.append(jsd.getChangfjsd(getBianhValue().getValue(), 0,"diancjsmkb,diancjsyfb"));
			setAllPages(jsd.getAllPages());
			_CurrentPage=1;
			return sb.toString();
			
		}
	

	// 审核状态下拉框
	private boolean falg1 = false;

	private IDropDownBean ShenhztValue;

	public IDropDownBean getShenhztValue() {
		if (ShenhztValue == null) {
			ShenhztValue = (IDropDownBean) getShenhztModel().getOption(0);
		}
		return ShenhztValue;
	}

	public void setShenhztValue(IDropDownBean Value) {
		if (!(ShenhztValue == Value)) {
			ShenhztValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel ShenhztModel;

	public void setShenhztModel(IPropertySelectionModel value) {
		ShenhztModel = value;
	}

	public IPropertySelectionModel getShenhztModel() {
		if (ShenhztModel == null) {
			getShenhztModels();
		}
		return ShenhztModel;
	}

	public IPropertySelectionModel getShenhztModels() {
		List listShenhzt = new ArrayList();
		listShenhzt.add(new IDropDownBean(0, "未审核"));
		listShenhzt.add(new IDropDownBean(1, "已审核"));
		
		ShenhztModel = new IDropDownModel(listShenhzt);
		return ShenhztModel;
	}
	
	// ******************编号设置*****************//
	public IDropDownBean getBianhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getBianhModel()
								.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setBianhValue(IDropDownBean Value) {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5()!= Value) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}
	}

	public void setBianhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIBianhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIBianhModels() {
		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		}
		
		String sql ="select js.id, js.bianm\n" +
			"          from diancjsmkb js, diancxxb dc\n" + 
			"          where js.diancxxb_id=dc.id\n" + 
			"          and js.id not in (select k.diancjsmkb_id from kuangfjsmkb k where k.diancjsmkb_id<>0)\n" + 
			"          and js.shujly=0 and js.GONGSQRZT="+ getShenhztValue().getStrId()+ "\n" + 
			str+
			" 			order by bianm";


		setBianhModel(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			setShenhztValue(null);
			setShenhztModel(null);
			setBianhModel(null);
			setBianhValue(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			setTreeid(null);
			visit.setString1(null);
			visit.setString10(null);
			visit.setString3(null);
		}
//		if (cycle.getRequestContext().getParameters("lx") != null) {
//			if (!visit.getString1().equals(
//					cycle.getRequestContext().getParameters("lx")[0])) {
//
//				visit.setProSelectionModel10(null);
//				visit.setDropDownBean10(null);
//				visit.setProSelectionModel5(null);
//				visit.setDropDownBean5(null);
//
//			}
//
//		}
		if(falg1){
			setBianhModel(null);
			setBianhValue(null);
			falg1=false;
		}
		blnIsBegin = true;
		getSelectData();
	}

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	private boolean _YansClick = false;

	public void YansButton(IRequestCycle cycle) {
		_YansClick = true;
	}
	

	private boolean _ShenhClick = false;

	public void ShenhButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}
	
	private boolean _FanhClick = false;

	public void FanhButton(IRequestCycle cycle) {
		_FanhClick = true;
	}
	public void Shenhe(){
		
		boolean zhuangt = false;
		
		if(getBianhValue().getStrId().equals("-1")){
			setMsg("请选择一条记录");
		}else{
			try {
				zhuangt = BalanceLiuc.DiancUpdate(getDiancmcValue().getId(),getBianhValue().getValue(),4,1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(zhuangt){
				JDBCcon con=new JDBCcon();
				con.getUpdate("update diancjsmkb set GONGSQRZT=1 where id="+getBianhValue().getStrId());
				con.Close();
				setMsg("审核成功");
			}else{
				setMsg("审核失败");
			}
		}
	}
	
	public void Fanhdc(){
		JDBCcon con=new JDBCcon();
		String sql="select kuangfjsmkb_id, bianm from diancjsmkb where id="+getBianhValue().getStrId();
		String kfid="0";
		String bm="";
		if(con.getHasIt(sql)){
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
				bm=rsl.getString("bianm");
				kfid=rsl.getString("kuangfjsmkb_id");
			}
		}
			if(kfid.equals("0")){
				boolean zhuangt = false;
				if(getBianhValue().getStrId().equals("-1")){
					setMsg("请选择一条记录");
				}else{
					try {
						BalanceLiuc.getDeleteDcBalance(getBianhValue().getId());
						zhuangt=true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(zhuangt){
						setMsg("返回电厂成功");
					}else{
						setMsg("返回电厂失败");
					}
				}
			}else{
				setMsg("编码为："+bm+" 的记录已有矿方结算单，不能返回电厂。");
			}
		con.Close();
	}
	
	
	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(_ShenhClick){
			Shenhe();
			_ShenhClick=false;
		}
		if(_FanhClick){
			Fanhdc();
			_FanhClick=false;
		}
		if (_RefurbishClick) {
			getSelectData();
			_RefurbishClick = false;
		}
		if(_YansClick){
			if(getBianhValue().getId()!=-1){
			visit.setString1("changf");
			visit.setString3("diancjsmkb,diancjsyfb");
			visit.setString10(getBianhValue().getValue());
			Yansmx();
			
			}else{
				setMsg("请先选择一个编码！");
			}
			_YansClick=false;
		}
		
		
	}

	private void Yansmx(){
		
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
			+ "var end = url.indexOf(';');"
			+ "url = url.substring(0,end);"
			+ "url = url + '?service=page/' + 'BalanceMingx&lx=balancemingx';"
			+ " window.open(url,'BalanceMingx');";
		
		this.setWindowScript(str);
	}
	
//	窗口弹出js_begin
	public String getWindowScript(){
		
		return ((Visit) getPage().getVisit()).getString4();
	}
	
	public void setWindowScript(String value){
		
		((Visit) getPage().getVisit()).setString4(value);
	}
//	窗口弹出js_end
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();
				
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"Form0",null,getTreeid());
		visit.setDefaultTree(dt);
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
		tb1.addText(new ToolbarText("审核状态:"));
		ComboBox shh = new ComboBox();
		shh.setTransform("SHZTSelect");
		shh.setEditable(true);
		shh.setWidth(100);
		shh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(shh);
		
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("结算编号:"));
		ComboBox jsbh = new ComboBox();
		jsbh.setTransform("BianmSelect");
		jsbh.setEditable(true);
		jsbh.setWidth(100);
		jsbh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(jsbh);


		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		
		if(getShenhztValue().getValue().equals("未审核")){
		ToolbarButton sh=new ToolbarButton(null,"审核","function(){document.getElementById(\"ShenhButton\").click();}");
		tb1.addItem(sh);
		}
		
		ToolbarButton fh=new ToolbarButton(null,"返回电厂","function(){document.getElementById(\"FanhButton\").click();}");
		tb1.addItem(fh);
		
		ToolbarButton ysmx=new ToolbarButton(null,"验收明细","function(){document.Form0.YansButton.click();}");
		ysmx.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(ysmx);
		
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

	public String getcontext() {
		return "";
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
				falg1=true;
			}else{
				falg1=false;
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

	// Page方法

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}
	



}