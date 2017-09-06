package com.zhiren.dc.jilgl.baob;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Chepshlmcx extends BasePage implements PageValidateListener  {
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ_BULMX";// baobpzb中对应的关键字
	// 界面用户提示

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO 自动生成方法存根
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

	// 绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	// 绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public boolean getRaw() {
		return true;
	}

	// 厂别下拉框
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	// 获取供应商
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
		String sql = "select id,mingc from vwgongysmk where diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();

		sql += "  union select 0 id, '全部' mingc from dual ";
		// setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
		setGongysDropDownModel(new IDropDownModel(sql));
		return;
	}

	// 获得选择的树节点的对应的电厂名称
	private String getMingc(String id) {
		JDBCcon con = new JDBCcon();
		String mingc = null;
		String sql = "select mingc from diancxxb where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = rsl.getString("mingc");

		}
		rsl.close();
		return mingc;
	}

	// 判断电厂Tree中所选电厂时候还有子电厂
	private boolean hasDianc(String id) {
		JDBCcon con = new JDBCcon();
		boolean mingc = false;
		String sql = "select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = true;
		}
		rsl.close();
		return mingc;
	}
	// 获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		return "车皮审核来煤查询";
	}

	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,
				"gongysTree", "" + visit.getDiancxxb_id(), "forms[0]",
				getTreeid(), getTreeid());

		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(90);
		tf.setValue(((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(getTreeid() == null
						|| "".equals(getTreeid()) ? "-1" : getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("煤矿单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		// 电厂Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null,
				getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(80);
		tf1.setValue(((IDropDownModel) getDiancmcModel())
				.getBeanValue(Long.parseLong(getTreeid_dc() == null
						|| "".equals(getTreeid_dc()) ? "-1" : getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);

		tb1.addText(new ToolbarText("-"));
		//审核
		tb1.addText(new ToolbarText("化验状态"));
		ComboBox ch=new ComboBox();
		ch.setTransform("HuayDropDown");
		ch.setId("HuayDropDown");
		ch.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
		ch.setEditable(true);
		ch.setLazyRender(true);
		ch.setWidth(60);
		tb1.addField(ch);
		tb1.addText(new ToolbarText("-"));
		

		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox yunsfs = new ComboBox();
		yunsfs.setTransform("YUNSFSSelect");
		yunsfs.setEditable(true);
		yunsfs.setWidth(50);
		yunsfs.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yunsfs);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	//化验状态下拉框
	 public boolean _HuayDropDown=false;
	    public IDropDownBean getHuayValue() {

			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getHuayModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setHuayValue(IDropDownBean Value) {

			if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

				((Visit) getPage().getVisit()).setboolean5(true);

			}
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}

		public IPropertySelectionModel getHuayModel() {

			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

				getHuayModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public void setHuayModel(IPropertySelectionModel value) {

			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getHuayModels() {
			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {
				List.add(new IDropDownBean(0,"全部"));
				List.add(new IDropDownBean(1, "未审核"));
				List.add(new IDropDownBean(2, "已审核"));

			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel5(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}
	// 运输方式下拉框
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) this.getYunsfsModel().getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		String sql = "select 0 id,'全部' mingc from dual union select id,mingc from yunsfsb";// 连接一条假设行，如果ID为0时，运输方式为全部.
		YunsfsModel = new IDropDownModel(sql);
		return YunsfsModel;
	}

	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		String yunsfs="";
		String meikdw="";
		String shenhzt="";
		if(getYunsfsValue().getId()==0){
			yunsfs="";
		}else{
			yunsfs=" and ys.mingc='"+
			getYunsfsValue().getValue()+"'";
		}
		
		if(getTreeid().equals("0")){
			meikdw="";
		}else{
			meikdw="and f.meikxxb_id="+getTreeid();
		}
		if(getHuayValue().getId()==0){
			shenhzt="";
		}else if(getHuayValue().getId()==1){
			shenhzt="and z.shenhzt<7";
		}else{
			shenhzt="and z.shenhzt=7";
		}
		String meik="";
		if(getTreeid().equals("0")){
			meik="全部";
		}else
		{
		String sql1="select id,mingc from meikxxb m where m.id="+getTreeid();
		ResultSetList rs1=cn.getResultSetList(sql1);
		while(rs1.next()){
			meik=rs1.getString("mingc");
		}
		rs1.close();
		}
		String sql=

			"select decode(f.daohrq,null,'总计',to_char(f.daohrq,'yyyy-MM-dd')) as daohrq,m.mingc, ys.mingc,cy.bianm,\n" +
			"            sum(f.ches) as ches,\n" + 
			"            sum(f.maoz-f.piz-f.koud) as jinz,\n" + 
			"            sum(f.maoz-f.piz) as piaoz,\n" + 
			"            sum(f.koud) as koud,\n" + 
			"            gethuaybh4zl(z.zhilb_id) as huaybh,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.mt,1)*f.laimsl)/sum(f.laimsl),1)) as quansf,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.mad,2)*f.laimsl)/sum(f.laimsl),2)) as kongqgzjsf,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.aad,2)*f.laimsl)/sum(f.laimsl),2)) as kongqgzjhf,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.ad,2)*f.laimsl)/sum(f.laimsl),2)) as ganzjhf,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.aar,2)*f.laimsl)/sum(f.laimsl),2)) as shoudjhf,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.vad,2)*f.laimsl)/sum(f.laimsl),2)) as kongqgzjhff,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.vdaf,2)*f.laimsl)/sum(f.laimsl),2)) as huiff,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.qbad, 2)* 1000*f.laimsl)/sum(f.laimsl),2)) as DANTRL,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.qnet_ar, 2)*1000*f.laimsl)/sum(f.laimsl),2))as FARL,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(round_new(z.qnet_ar, 2) * 7000 / 29.271, 0)*f.laimsl)/sum(f.laimsl),2)) as FARL1,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.sdaf , 2)*f.laimsl)/sum(f.laimsl),2)) AS SDAF,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.std, 2)*f.laimsl)/sum(f.laimsl),2)) as GANZJL,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.stad, 2)*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad), 2)*f.laimsl)/sum(f.laimsl),2)) as star,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.hdaf, 2)*f.laimsl)/sum(f.laimsl),2)) as HDAF,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.had, 2)*f.laimsl)/sum(f.laimsl),2)) as had,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.fcad, 2)*f.laimsl)/sum(f.laimsl),2)) as FCAD,\n" + 
			"            decode(sum(f.laimsl),0,0,round_new(sum(round_new(z.qgrd, 2)*f.laimsl)/sum(f.laimsl),2)) as QGRD,\n" + 
			"            decode(f.liucztb_id,1,'审核',0,'未审核',f.liucztb_id) as shulzt,\n" + 
			"            decode(z.shenhzt,7,'审核',0,'未审核',1,'未审核',2,'未审核',3,'未审核',4,'未审核',5,'未审核',6,'未审核',z.shenhzt) as zhilsh\n" + 
			"            from caiyb cy,zhillsb z,zhilb zb,fahb f,yunsfsb ys,meikxxb m\n" + 
			"            where cy.zhilb_id=zb.id\n" + 
			"            and  f.zhilb_id=zb.id\n" + 
			"            and  z.zhilb_id=zb.id\n" + 
			"            and  m.id=f.meikxxb_id\n" + 
			"            and  f.yunsfsb_id=ys.id\n" + 
			"            and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())+"\n" + 
			"            and f.daohrq<="+DateUtil.FormatOracleDate(getERiq())+"\n" + 
			"            "+yunsfs+"\n" +
			"            "+meikdw+"\n" +
			"            "+shenhzt+"" + 
			"            group by rollup(cy.bianm,z.zhilb_id,f.liucztb_id,z.shenhzt,f.daohrq,m.mingc,ys.mingc)\n" + 
			"            having not(grouping(z.zhilb_id)+grouping(cy.bianm)=1 or grouping(f.liucztb_id)+grouping(cy.bianm)=1 or grouping(z.shenhzt)+grouping(cy.bianm)=1 \n"+
            "            or grouping(f.daohrq)+grouping(z.shenhzt)=1 or grouping(m.mingc)+grouping(ys.mingc)+grouping(z.shenhzt)=2 or grouping(m.mingc)+grouping(ys.mingc)=1)\n" + 
			"            order by cy.bianm,z.zhilb_id,f.liucztb_id,z.shenhzt";


		ResultSetList rs = cn.getResultSetList(sql);
		String ArrHeader[][] = new String [1][29];
		Report rt = new Report();
			ArrHeader[0] = new String[] {"到货日期","矿点","运输方式","批次编号","车数","净重","票重", 
        		"扣吨","化验编号","全水分(%)<br>Mt",
    			"空气干燥基水分(%)<br>Mad","空气干燥基灰分(%)<br>Aad",
    			"干燥基灰分(%)<br>Ad","收到基灰分(%)<br>Aar","空气干燥基挥发分(%)<br>Vad","干燥无灰基挥发分(%)<br>Vdaf",
    			"弹筒发热量(J/g)<br>Qb,ad","收到基地位发热量(J/g)<br>Qnet,ar",
    			"收到基地位热值<br>(Kcal/Kg)","干燥无灰基硫<br>Sdaf","干燥基硫(%)<br>St,d","空气干燥基硫(%)<br>St,ad",
    			"收到基硫(%)<br>St,ar","干燥无灰基氢(%)<br>Hdaf","空气干燥基氢(%)<br>Had","固定碳(%)<br>Fcad","干基高位热(%)<br>Qgrd","数量审核状态","质量审核状态"};

		int ArrWidth[] = new int[] {100,100,50,60, 60, 40, 40, 40, 40, 40, 40, 40, 40,
					40, 40, 40, 40,40 ,40,40,40,40,40,40,40,40,40,40,60};

		rt.setTitle(getRptTitle(), ArrWidth);
		rt.setDefaultTitle(1, 6, "到货日期:"+getBRiq()+"至"+getERiq(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 4, "煤矿单位:"+meik, Table.ALIGN_CENTER);
		rt.setDefaultTitle(27, 3, "运输方式:"+getYunsfsValue().getValue(), Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCol(1);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();// ph;
	}

	// 工具栏使用的方法
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			// treeid=String.valueOf(((Visit)
			// this.getPage().getVisit()).getDiancxxb_id());
			treeid = "0";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("gongysTree_text"))
						.setValue(((IDropDownModel) getGongysDropDownModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// -------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
//
	DefaultTree dc;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}

	// -------------------------电厂Tree
	// END-------------------------------------------------------------
//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		return getToolbar().getRenderScript();
	}

	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			getGongysDropDownModels();
			visit.setProSelectionModel5(null);
			visit.setDropDownBean5(null);
			setChangbValue(null);
			setChangbModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");

			this.setYunsfsValue(null);
			this.setYunsfsModel(null);
			getHuayModels();
			setHuayValue(null);
			getHuayModel();
			getSelectData();
		}
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// 页面登陆验证
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
}
