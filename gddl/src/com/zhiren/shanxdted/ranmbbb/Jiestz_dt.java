package com.zhiren.shanxdted.ranmbbb;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public abstract class Jiestz_dt extends BasePage implements PageValidateListener{

	//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	//	 年
	public IDropDownBean getYearValue() {
		int _nianf = DateUtil.getYear(new Date());
		int _yuef = DateUtil.getMonth(new Date());
		if (_yuef == 1) {
			_nianf = _nianf - 1;
		}
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getYearModel().getOptionCount(); i++) {
				Object obj = getYearModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean4((IDropDownBean) getYearModel()
									.getOption(i));
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYearValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public void setYearModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYearModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYearModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getYearModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select yvalue id,ylabel name from nianfb");
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql.toString()));
	}

	// 月
	public IDropDownBean getMonthValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			int _yuef = DateUtil.getMonth(new Date());

			if (1 == _yuef) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getMonthModel()
								.getOption(11));
			} else {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getMonthModel()
								.getOption(_yuef - 2));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setMonthValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}
	}

	public void setMonthModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getMonthModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getMonthModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getMonthModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select mvalue id,mlabel name from yuefb");
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql.toString()));
	}
	
	
//	页面变化记录
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
    
    
	//判断电厂Tree中所选电厂时候还有子电厂   
    private ResultSetList hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		String sql="select id, mingc from diancxxb where fuid = " + id +" union  select id,mingc from diancxxb where id="+id+" order by id asc ";
		ResultSetList rsl=con.getResultSetList(sql);
		con.Close();
		return rsl;
	}
   
    //获取表表标题
	public String getRptTitle(String diancid) {
		String sb="";
		String dcmc="";
		
		try{
			dcmc=MainGlobal.getTableCol("diancxxb", "quanc", "id in ("+diancid+")");
		}catch(Exception e){
			e.printStackTrace();
		}
		sb = dcmc + "公路煤"+getMonthValue().getId()+"月结算台帐";
		return sb;
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox year = new ComboBox();
		year.setTransform("YearSelect");
		year.setEditable(true);
		year.setWidth(80);
		tb1.addField(year);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("月份:"));
		ComboBox month = new ComboBox();
		month.setTransform("MonthSelect");
		month.setEditable(true);
		month.setWidth(60);
		tb1.addField(month);
		tb1.addText(new ToolbarText("-"));

		//电厂Tree
		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
		" and z.mingc='shulzhcxqx' and r.id="+visit.getRenyID();//zuxxb中组的名称
		ResultSetList rsl=con.getResultSetList(toaijsql);
		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		if(rsl.next()){
			diancxxb_id = 300;
		}
//		
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//		ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid(),null,true);
//		setDCTree(etu);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		String[] str=getTreeid().split(",");
//		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
//		ToolbarButton toolb2 = new ToolbarButton(null, null,
//				"function(){diancTree_window.show();}");
//		toolb2.setIcon("ext/resources/images/list-items.gif");
//		toolb2.setCls("x-btn-icon");
//		toolb2.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(toolb2);
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
		ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid(),null,true);

		setDCTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		ToolbarButton toolb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		toolb2.setIcon("ext/resources/images/list-items.gif");
		toolb2.setCls("x-btn-icon");
		toolb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(toolb2);
		
		tb1.addText(new ToolbarText("-"));	
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
//		tb1.addFill();
		setToolbar(tb1);
	}
	
	private String getSql(){
		StringBuffer sql=new StringBuffer();
		String strRiq=" and jiesrq>=to_date('"+getYearValue().getId()+"-"+getMonthValue().getId()+"-01','yyyy-mm-dd')" +
				" and jiesrq<=add_months(to_date('"+getYearValue().getId()+"-"+getMonthValue().getId()+"-01','yyyy-mm-dd'),1)";
		
		StringBuffer diancid= new StringBuffer();
		
		String[] arrDc = this.getTreeid().split(","); 
		if (arrDc[0].equals("300")) {
			ResultSetList rsl = this.hasDianc(arrDc[0]);
			StringBuffer strdcid=new StringBuffer();
			while(rsl.next()){
				strdcid.append(String.valueOf(rsl.getLong("id")));
				strdcid.append(",");
			}
			diancid.append(strdcid.substring(1, strdcid.length()-1));
		}
		else {
			diancid.append(getTreeid());
		}		
		sql.append("select\n" );
		sql.append("decode(grouping(a.dcmc),1,'合计',a.dcmc) dcmc,a.dqmc,decode(grouping(dqmc),1,'',max(a.rn)) rn,\n" ); 
		sql.append("a.mkmc,decode(grouping(dqmc),1,'',max(a.gmsj)) gmsj,sum(a.jiessl) meil,round_new(sum(a.jiesdj*a.jiessl)/sum(a.jiessl),2) jiesj,\n" ); 
		sql.append("round_new((sum(a.yunj*a.jiessl)+sum(a.jiesdj*a.jiessl))/sum(a.jiessl),2) meiyj,\n" ); 
		sql.append("round_new(sum(a.hetj*a.jiessl)/sum(a.jiessl),2) hetj,\n" ); 
		sql.append("sum(a.jiesdj*a.jiessl) meik,\n" ); 
		sql.append("round_new(sum(a.yunj*a.jiessl)/sum(a.jiessl),2) yunj,\n" ); 
		sql.append("sum(a.jiessl*a.yunj) yunf,\n" ); 
		sql.append("max(a.hetrz) hetrz,\n" ); 
		sql.append("round_new(sum(a.changfrz*a.jiessl)/sum(a.jiessl),2) changfrz,\n" ); 
		sql.append("round_new(sum(a.jiesrz*a.jiessl)/sum(a.jiessl),2) jiesrz,\n" ); 
		sql.append("case when sum(a.yingk)<0 then abs(sum(a.yingk)) else 0 end kuik,\n" ); 
		sql.append("case when sum(a.yingk)<0 then abs(sum(a.zhejbz)) else 0 end kuikje,\n" ); 
		sql.append("case when sum(a.yingk)<0 then abs(sum(a.zhejje)) else 0 end suopje,\n" ); 
		sql.append("round_new((round_new(sum(a.jiesdj*a.jiessl)/sum(a.jiessl),2)/1.17+round_new(sum(a.yunj*a.jiessl)/sum(a.jiessl),2)*0.93)*7000/round_new(sum(a.changfrz*a.jiessl)/sum(a.jiessl),2),2) biaomdj,\n" ); 
		sql.append("case when sum(a.yingk)>0 then abs(sum(a.zhejbz)) else 0 end jianglje,\n" ); 
		sql.append("case when sum(a.yingk)>0 then abs(sum(a.zhejje)) else 0 end jianglk,decode(grouping(dqmc),1,'',max(a.beiz)) beiz\n" ); 
		sql.append("from (select js.id,max(dc.MINGC) dcmc,decode(max(pz.MINGC),'褐煤',max(mkdq.MINGC) || '褐煤',max(mkdq.MINGC)) dqmc , max(mk.mingc) mkmc,\n" ); 
		sql.append("ROW_NUMBER ()OVER (PARTITION BY max(dc.mingc),max(mkdq.mingc) ORDER BY js.id )rn,\n" ); 
		sql.append("min(to_char(fh.daohrq,'yyyy.yy.dd'))||'-'||max(to_char(fh.daohrq,'dd')) gmsj,\n" ); 
		sql.append("max(js.JIESSL) jiessl,max(js.HANSDJ) jiesdj,max(js.HETJ) hetj,max(js.YUNFHSDJ) yunj,\n" ); 
		sql.append("decode(max(jszb.ZHIBB_ID),2,max(jszb.HETBZ),'') hetrz,decode(max(jszb.ZHIBB_ID),2,max(jszb.changf),'') changfrz,\n" ); 
		sql.append("decode(max(jszb.ZHIBB_ID),2,max(jszb.jies),'') jiesrz,decode(max(jszb.ZHIBB_ID),2,max(jszb.yingk),'') yingk,\n" ); 
		sql.append("decode(max(jszb.ZHIBB_ID),2,max(jszb.zhejbz),'') zhejbz,decode(max(jszb.ZHIBB_ID),2,max(jszb.zhejje),'') zhejje,max(js.BEIZ) beiz\n" ); 
		sql.append("from jiesb js,hetb ht,fahb fh,diancxxb dc,meikxxb mk,meikdqb mkdq,pinzb pz,(select * from jieszbsjb where zhibb_id=2) jszb\n" ); 
		sql.append("where js.id=fh.JIESB_ID and js.HETB_ID=ht.ID and js.DIANCXXB_ID=dc.id and fh.PINZB_ID=pz.ID and js.id=jszb.JIESDID(+)\n" ); 
		sql.append("and js.MEIKXXB_ID=mk.id and mk.meikdq_id=mkdq.id  "+strRiq+" and dc.id in ("+diancid+") group by js.id) a\n" ); 
		sql.append("group by rollup(a.dcmc,a.id,(a.dqmc,a.mkmc))\n" ); 
		sql.append("having (grouping(a.mkmc)=0 or grouping(a.id)=1) order by a.dcmc,grouping(a.id),a.dqmc,a.mkmc");
		return sql.toString();
	}
	
	public String getPrintTable(){
		int PageRows=9999;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		ResultSetList rstmp = con.getResultSetList(getSql());
		int[] ArrWidth=null;
		String[][] ArrHeader=null;
		//表头部分
		ArrHeader = new String[1][22];
		ArrHeader[0] = new String[] { "电厂名称", "采购方式","序号", "煤矿名称", "供煤时间","数量(吨)","结算价<br>(元/吨)","煤运价<br>(元/吨)","合同价<br>(元/吨)","煤款(元)","运价<br>(元/吨)","运费(元)","合同热值<br>(Qnet,ar)(Cal/Kg)"
										,"到厂热值<br>(Qnet,ar)(Cal/Kg)","结算热值<br>(Qnet,ar)(Cal/Kg)","亏卡<br>(MJ/Kg)","亏卡金额<br>(元/吨)","索赔金额(元)","标煤单价<br>(元/吨)","奖励金额<br>(元/吨)","奖励款(元)","备注"};
		
		//设定报表每一列的宽度
		ArrWidth = new int[22];
		ArrWidth = new int[] { 80, 80, 80,80 , 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80};

		rt.createTitle(3, ArrWidth);
		rt.title.setCellValue(1, 1,getRptTitle(getTreeid()) , ArrWidth.length);
		
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 16);
		rt.title.setCellAlign(1, 1, Table.ALIGN_CENTER);
		rt.title.setCells(1, 1, 4, 7, Table.PER_FONTNAME, "宋体");
		rt.title.setCells(1, 1, 2, 1, Table.PER_FONTBOLD, true);
		rt.title.setCells(2, 1, 3, 1, Table.PER_FONTBOLD, true);
		rt.title.setCellValue(2, 1, getYearValue().getValue() + " 年 " + getMonthValue().getValue() + " 月 ",ArrWidth.length);
		rt.title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 10);
		
		rt.setBody(new Table(rstmp, 1, 0, 3));  //填充数据
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.createDefautlFooter(ArrWidth);
		rt.body.merge(1, 1, 1, 22);
		rt.body.mergeFixedCols();
		rt.body.setPageRows(PageRows);
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
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
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
		rt.body.setColAlign(20, Table.ALIGN_RIGHT);
		rt.body.setColAlign(21, Table.ALIGN_RIGHT);
		rt.body.setColAlign(22, Table.ALIGN_RIGHT);
		
		rt.body.ShowZero = ((Visit) getPage().getVisit()).isReportShowZero();  //是否显示值为0的数据
		 
//		String [] strFormat = new String[] {"", "", "", "", "0", "0.00", "0.00", "0.00","0.00","0.00","0.00","0.00","0.00", "0.00", "0.00" ,"0.00", "0.00", "0.00" ,"0.00", "0.00", "" };
//		rt.body.setColFormat(strFormat);
		
//		rt.body.setBorder(0, 0, 2, 2);       //设置上下的粗线框
		// 设置页数
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		con.Close();
//		rt.body.setRowHeight(1,21);   //设置第一行的高度
//		rt.body.setRowHeight(21);   //设置每行的高度
//		rt.setOrientation(Report.PAPER_Landscape);
//		rt.setMargin(0, 0, 0, 0);
		return rt.getAllPagesHtml();
	}
	
	//---------------------------
	
	//工具栏使用的方法
	
	public String getTreeScript() {
		return getDCTree().getWindowTreeScript();
	}
//	-------------------------电厂Tree-----------------------------------------------------------------
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
	
	public ExtTreeUtil getDCTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setDCTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getDCTree().getWindowTreeHtml(this);
	}
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			this.treeid="";
			getDiancmcModels();
			getYearModels();
			getMonthModels();
			setTreeid(visit.getDiancxxb_id() + "");
		}
		getSelectData();
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
			
		}
		getSelectData();
	}
//	页面登陆验证
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
