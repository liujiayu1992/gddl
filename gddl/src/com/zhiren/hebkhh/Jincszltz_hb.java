package com.zhiren.hebkhh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author Rock
 * @since 2009.09.24
 * @version 1.0
 * @discription 进厂煤数质量查询
 */
/*
 * 配置SQL
insert into itemsort(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(209,209,1,'DCHZBM','电厂汇总编码',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(300,209,1,229,'2291',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(310,209,1,229,'2292',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(320,209,1,232,'2321',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(330,209,1,232,'2321',1,'');
 * 
 */
/*
 * 作者：王磊
 * 时间：2009-09-25
 * 描述：增加净重的显示宽度,增加排序
 */
/*
 * 作者：王磊
 * 时间：2009-10-31
 * 描述：增加了报表类型选项，分为明细和汇总
 */
/*
 * 作者：王磊
 * 时间：2009-11-02
 * 描述：修改化验指标为与月报指标一致。
 */
public class Jincszltz_hb extends BasePage {

	private static final String RptType_mx = "明细";
	private static final String RptType_hz = "汇总";
//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
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
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
//	报表类型下拉框
	public IDropDownBean getRptTypeValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getRptTypeModel().getOptionCount()>0) {
				setRptTypeValue((IDropDownBean)getRptTypeModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setRptTypeValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getRptTypeModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setRptTypeModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setRptTypeModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	public void setRptTypeModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1,RptType_mx));
		list.add(new IDropDownBean(2,RptType_hz));
		setRptTypeModel(new IDropDownModel(list));
	}

//	刷新衡单列表
	public void getSelectData() {
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
//		报表类型选项
		tb1.addText(new ToolbarText("类型:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("LeixSelect");
		leix.setWidth(80);
		leix.setListeners("select:function(own,rec,index){Ext.getDom('LeixSelect').selectedIndex=index}");
		tb1.addField(leix);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}
	
	private String getJincszltz_mx(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		String sql = 
		"select decode(grouping(f.daohrq),1,'<font color=orange><b><i>合计</b></i></font>'," +
		"to_char(f.daohrq,'yyyy-mm-dd')) dhrq, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>省公司合计</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>保定','<font color=blue>马头')||'小计</font>',\n" +
		"d.mingc) dcmc, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>省公司合计</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>保定','<font color=blue>马头')||'小计</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>小计</b>',y.mingc)) ysfs, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>省公司合计</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>保定','<font color=blue>马头')||'小计</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>小计</b>',g.mingc)) gys, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>省公司合计</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>保定','<font color=blue>马头')||'小计</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>小计</b>',m.mingc)) mkmc,\n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>省公司合计</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>保定','<font color=blue>马头')||'小计</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>小计</b>',p.mingc)) pz,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.jingz),0)||'</font>' jz, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.biaoz),0)||'</font>' bz, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.yuns),0)||'</font>' ys, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.yingk),0)||'</font>' yk, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.zongkd),0)||'</font>' kd,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2)),'0')||'</font>' qner_ar_mj,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/0.0041816,0)),'0')||'</font>' qner_ar_k,\n" +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz),1)),'0')||'</font>' mt,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz),2)),'0')||'</font>' vdaf,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz),2)),'0')||'</font>' std,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz),2)),'0')||'</font>' aar,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz),2)),'0')||'</font>' buhsdj,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz*z.qnet_ar),2)),'0')||'</font>' buhsdj_k,\n" +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj*z.qnet_ar)/sum(f.jingz*29.271),2)),'0')||'</font>' buhsbmdj\n" + 
		
		"from fahb f, item i, itemsort s, diancxxb d, yunsfsb y,\n" + 
		"gongysb g, meikxxb m, pinzb p, zhilb z, jiesb j\n" + 
		"where f.diancxxb_id = d.id and f.yunsfsb_id = y.id and f.gongysb_id = g.id\n" + 
		"and f.meikxxb_id = m.id and f.pinzb_id = p.id\n" + 
		"and f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq()==null||"".equals(getBRiq())?DateUtil.FormatDate(new Date()):getBRiq())+"\n" + 
		"and f.daohrq <= "+DateUtil.FormatOracleDate(getERiq()==null||"".equals(getERiq())?DateUtil.FormatDate(new Date()):getERiq())+"\n" + 
		"and s.bianm = 'DCHZBM' and i.itemsortid = s.id\n" + 
		"and f.diancxxb_id||'' = i.mingc\n" + 
		"and f.zhilb_id = z.id(+) and f.jiesb_id = j.id(+)\n" + 
		"group by rollup(daohrq,i.bianm,d.mingc,g.mingc,y.mingc,m.mingc,p.mingc)\n" + 
		"having grouping(p.mingc) = 0 or grouping(g.mingc) = 1 " +
		"order by f.daohrq,d.mingc,y.mingc,g.mingc,m.mingc,p.mingc" ;
		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
    	ArrHeader = new String[][] {
    		{"日期", "单位", "运输方式", "供应商", "煤矿", 
			"品种", "净重<br>(吨)", "票重<br>(吨)", "运损<br>(吨)", "盈亏<br>(吨)", 
			"扣吨<br>(吨)", "热量", "热量", "水分<br>(mt)<br>(%)", "挥发分<br>(vdaf)<br>(%)", 
			"硫分<br>(std)<br>(%)", "灰分<br>(aar)<br>(%)", "结算单价", "结算单价", "标煤单价<br>元/吨"},
    		{"日期", "单位", "运输方式", "供应商", "煤矿", 
    			"品种", "净重<br>(吨)", "票重<br>(吨)", "运损<br>(吨)", "盈亏<br>(吨)", 
    			"扣吨<br>(吨)", "MJ/kg", "Kcal/kg", "水分<br>(mt)<br>(%)", "挥发分<br>(vdaf)<br>(%)", 
    			"硫分<br>(std)<br>(%)", "灰分<br>(aar)<br>(%)", "元/大卡.吨", "元/吨", "标煤单价<br>元/吨"}
			 };

    	ArrWidth = new int[] {70, 35, 35, 150, 100, 60, 70, 70, 70, 70, 70, 
    			70, 70, 70, 70, 70, 70, 70, 70, 70};

		rt.setTitle("来煤综合查询", ArrWidth);

	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		rt.setBody(new Table(rs, 2, 0, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		for(int i = 3; i< rt.body.getRows() ; i++){
		rt.body.merge(i, 1, i, 6);
		}
		if(rt.body.getRows()>2)
			rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(), 6);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for(int i = 1; i<= 6 ; i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		for(int i = 7; i <=ArrWidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rs.close();
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
     	return rt.getAllPagesHtml();// ph;
	}
	
	private String getJincszltz_hz(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		String sql = 
		"select decode(grouping(i.bianm)+grouping(d.mingc),2,\n" + 
		"'<font color=green><b>省公司合计</b></font>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>保定','<font color=blue>马头')||'小计</font>',\n" + 
		"d.mingc) dcmc,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.jingz)||'</font>' jingz,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.biaoz)||'</font>' biaoz,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.yuns)||'</font>' yuns,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.yingk)||'</font>' yingk,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.zongkd)||'</font>' zongkd,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2)),'0')||'</font>' qner_ar_mj,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/0.0041816,0)),'0')||'</font>' qner_ar_k,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz),1)),'0')||'</font>' mt,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz),2)),'0')||'</font>' vdaf,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz),2)),'0')||'</font>' std,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz),2)),'0')||'</font>' aar,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz),2)),'0')||'</font>' buhsdj,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz*z.qnet_ar),2)),'0')||'</font>' buhsdj_k,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj*z.qnet_ar)/sum(f.jingz*29.271),2)),'0')||'</font>' buhsbmdj\n" + 
		"from fahb f, item i, itemsort s, diancxxb d, zhilb z, jiesb j\n" + 
		"where f.diancxxb_id = d.id\n" + 
		"and f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq()==null||"".equals(getBRiq())?DateUtil.FormatDate(new Date()):getBRiq())+"\n" + 
		"and f.daohrq <= "+DateUtil.FormatOracleDate(getERiq()==null||"".equals(getERiq())?DateUtil.FormatDate(new Date()):getERiq())+"\n" + 
		"and s.bianm = 'DCHZBM' and i.itemsortid = s.id\n" + 
		"and f.diancxxb_id||'' = i.mingc\n" + 
		"and f.zhilb_id = z.id(+) and f.jiesb_id = j.id(+)\n" + 
		"group by rollup(i.bianm,d.mingc)";

		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
    	ArrHeader = new String[][] {
    		{ "单位", "净重<br>(吨)", "票重<br>(吨)", "运损<br>(吨)", "盈亏<br>(吨)", 
			"扣吨<br>(吨)", "热量", "热量", "水分<br>(mt)<br>(%)", "挥发分<br>(vdaf)<br>(%)", 
			"硫分<br>(std)<br>(%)", "灰分<br>(aar)<br>(%)", "结算单价", "结算单价", "标煤单价<br>元/吨"},
    		{"单位", "净重<br>(吨)", "票重<br>(吨)", "运损<br>(吨)", "盈亏<br>(吨)", 
    			"扣吨<br>(吨)", "MJ/kg", "Kcal/kg", "水分<br>(mt)<br>(%)", "挥发分<br>(vdaf)<br>(%)", 
    			"硫分<br>(std)<br>(%)", "灰分<br>(aar)<br>(%)", "元/大卡.吨", "元/吨", "标煤单价<br>元/吨"}
			 };

    	ArrWidth = new int[] {70, 70, 70, 70, 70, 70, 
    			70, 70, 70, 70, 70, 70, 70, 70, 70};

		rt.setTitle("来煤综合查询", ArrWidth);

	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for(int i = 2; i <=ArrWidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rs.close();
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
     	return rt.getAllPagesHtml();// ph;
	}
	
	
	public String getPrintTable(){
		String html = "";
		if(RptType_mx.equalsIgnoreCase(getRptTypeValue().getValue())){
			html = getJincszltz_mx();
		}else{
			html = getJincszltz_hz();
		}
		return html;
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getToolbar() == null){
			return "";
		}
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setRptTypeModels();
			getSelectData();
		}
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
