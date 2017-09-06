package com.zhiren.dc.jilgl.jicxx;

/*
 * 作者:zl
 * 时间:2009-10-21
 * 修改内容:增加成本查询功能
 */
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
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

public class Chengbcx extends BasePage {

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
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
//	 绑定日期
	public String getBRiq() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setBRiq(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getERiq() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setERiq(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}
	
	public String getYunsfs(){
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setYunsfs(String yunsfs){
		((Visit)this.getPage().getVisit()).setString1(yunsfs);
	}
	
	public String getKuangb(){
		return ((Visit)this.getPage().getVisit()).getString5();
	}
	public void setKuangb(String kuangb){
		((Visit)this.getPage().getVisit()).setString5(kuangb);
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

	private String REPORT_NAME_ZIBCCX = "Chengbcx";// zhillsb中的数据

	/**2008-10-18 huochaoyuan
	*由于一个批次煤生成多个采样编号的情况，导致原先报表显示有问题，故新添加一个样式的报表；
	*报表函数getMeizjyrb_zhilb_1()
**/
	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	// private String leix = "";

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getChengb();
		
	}

	// 化验日报(入厂)zhillsb中的数据
	/*
	 * 将检质数量 maoz-piz修改为 laimsl 并按照修约设定进行修约，将全水及热量的修约改为参数
	 * 修改范围 getMeizjyrb(),getMeizjyrb_zhilb(),String getMeizjyrb_zhilb_1()
	 * 修改日期：2008-12-04
	 * 修改人：王磊 
	 */
	
//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
	private String getChengb() {
		Visit v = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		int meikxxb_id=0;
		String kuangb = "";
		String sql = "select id from meikxxb where mingc='"+getKuangb()+"'";
		ResultSetList rsh = con.getResultSetList(sql);
		if(rsh.next()){
			meikxxb_id=rsh.getInt("id");
			kuangb = " and f.meikxxb_id="+meikxxb_id;
		}
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select zjb.*,decode(qnet_ar,0,0,round_new((meij+yunj+jiaohqzf+zaf+daozzf+qitfy)*29.271/qnet_ar,2)) as bmdj, \n");
		sbsql.append("decode(qnet_ar,0,0,round_new((meij+yunj+jiaohqzf+zaf+daozzf+qitfy-meijs-yunjs)*29.271/qnet_ar,2)) as buhsbmdj   \n");
		sbsql.append("from (select d.mingc changb, \n");
		sbsql.append("nvl(c.fenx,'') fenx, sum(jingz) jingz, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*meij)/sum(jingz)),2) meij, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*meijs)/sum(jingz)),2) meijs, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*yunj)/sum(jingz)),2) yunj, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*yunjs)/sum(jingz)),2) yunjs, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*jiaohqzf)/sum(jingz)),2) jiaohqzf, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*zaf)/sum(jingz)),2) zaf, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*daozzf)/sum(jingz)),2) daozzf, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*qitfy)/sum(jingz)),2) qitfy, \n");
		sbsql.append("round_new(decode(sum(jingz),0,0,sum(jingz*Qnet_ar)/sum(jingz)),"+v.getFarldec()+") Qnet_ar \n");
		sbsql.append("from  \n");
		sbsql.append("(select a.diancxxb_id,'本日' fenx, nvl(b.jingz,0) jingz, \n");
		sbsql.append("nvl(b.meij,0) meij,nvl(b.meijs,0) meijs,nvl(b.yunj,0) yunj, \n");
		sbsql.append("nvl(b.yunjs,0) yunjs,nvl(b.jiaohqzf,0) jiaohqzf, nvl(b.zaf,0) zaf, \n");
		sbsql.append("nvl(b.daozzf,0) daozzf,nvl(b.qitfy,0) qitfy,nvl(b.Qnet_ar,0) Qnet_ar from  \n");
		sbsql.append("(select distinct f.diancxxb_id \n");
		sbsql.append("        from fahb f, ruccb r \n");
		sbsql.append("where r.id = f.ruccbb_id  and f.daohrq >="+DateUtil.FormatOracleDate(getBRiq())+" and f.daohrq < "+DateUtil.FormatOracleDate(getERiq())+"+1").append(kuangb).append(") a, \n");
		sbsql.append("(select f.diancxxb_id,sum(round_new(f.laimsl,1)) jingz, \n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meij)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meij,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meijs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meijs,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunj)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunj,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunjs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunjs,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.jiaohqzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) jiaohqzf,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.zaf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) zaf,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.daozzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) daozzf,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.qitfy)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) qitfy,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.Qnet_ar)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) Qnet_ar\n");
		sbsql.append("      from fahb f, ruccb r \n");
		sbsql.append("where r.id = f.ruccbb_id ").append(kuangb).append(" and f.daohrq ="+DateUtil.FormatOracleDate(getERiq())+"  group by f.diancxxb_id) b \n");
		sbsql.append("where a.diancxxb_id = b.diancxxb_id(+) \n");
		sbsql.append("union \n");
		sbsql.append("select f.diancxxb_id,'累计' fenx,sum(round_new(f.laimsl,1)) jingz, \n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meij)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meij,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meijs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meijs,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunj)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunj,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunjs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunjs,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.jiaohqzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) jiaohqzf,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.zaf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) zaf,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.daozzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) daozzf,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.qitfy)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) qitfy,\n");
		sbsql.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.Qnet_ar)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) Qnet_ar\n");
		sbsql.append("      from fahb f, ruccb r \n");
		sbsql.append("where r.id = f.ruccbb_id  and f.daohrq >="+DateUtil.FormatOracleDate(getBRiq())+" and f.daohrq < "+DateUtil.FormatOracleDate(getERiq())+"+1 \n").append(kuangb);
		sbsql.append("      group by f.diancxxb_id) c,diancxxb d \n");
		sbsql.append("where d.id=c.diancxxb_id \n");
		sbsql.append("group by rollup (d.mingc,c.fenx) \n");
		sbsql.append("having not grouping(fenx)=1 \n");
		sbsql.append("order by d.mingc,c.fenx) zjb  \n");
		
		
		ResultSetList rstmp = con.getResultSetList(sbsql.toString());
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='Zibcxx' order by xuh");
        ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='Meizjyrb'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
//        	-----------------------------  新加的方法 -------------------------------------------------------
        	 ArrHeader = new String[][] {{ Locale.diancxxb_id_fahb, Locale.MRtp_fenx, Locale.laimsl_fahb, Locale.meij_chengbrb, 
    			Locale.meijs_chengbrb, Locale.yunj_chengbrb, Locale.yunjs_chengbrb, Locale.jiaohqzf_chengbrb, 
    			Locale.zaf_chengbrb, Locale.daozzf_chengbrb, Locale.qitfy_chengbrb, Locale.Qnet_ar_chengbrb,
    			Locale.biaomdj_chengbrb, Locale.buhsbmdj_chengbrb
    		}};

    		ArrWidth = new int[] {  100, 50, 70, 50, 50, 50, 50, 80, 50, 70, 50, 50, 65, 80 };

    		rt.setTitle("成本查询", ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 14);
    		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
    				Table.ALIGN_LEFT);
    		rt.setDefaultTitle(6, 3, "报表日期：" + getBRiq(),
    				Table.ALIGN_CENTER);
    		rt.setDefaultTitle(11, 4, "单位：吨、车", Table.ALIGN_RIGHT);

    		String[] arrFormat = new String[] { "", "", "", "", "", "", "0.00", "0.00",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "0.00","0.00" };
        }
    		rt.setBody(new Table(rs, 1, 0, 4));
    		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
    		rt.body.setColAlign(1, Table.ALIGN_CENTER);
    		rt.body.setColAlign(2, Table.ALIGN_CENTER);
    		rt.body.setColAlign(3, Table.ALIGN_CENTER);
    		rt.body.setColAlign(4, Table.ALIGN_CENTER);
    		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
    		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
    		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
    		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
    		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
    		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
    		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
//    		rt.body.setCellVAlign(i, j, intAlign)
    		rt.body.setWidth(ArrWidth);
    		rt.body.setHeaderData(ArrHeader);
    		rt.body.setPageRows(40);
    		rt.body.mergeFixedCols();
    		rt.body.mergeFixedRow();

//    		rt.createDefautlFooter(ArrWidth);
    		rt.createFooter(1,ArrWidth);
    		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
    				Table.ALIGN_LEFT);
    		rt.setDefautlFooter(6, 3, "审核：", Table.ALIGN_CENTER);
    		rt.setDefautlFooter(12, 3, "制表：", Table.ALIGN_LEFT);
    		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.footer.fontSize=10;
//    		rt.footer.setRowHeight(1, 1);
    		con.Close();
    		if(rt.body.getPages()>0) {
    			setCurrentPage(1);
    			setAllPages(rt.body.getPages());
    		}
    		rt.body.setRowHeight(21);
        return rt.getAllPagesHtml();// ph;
	}
	
	
	
	// 按钮的监听事件
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
//	按钮的监听事件
	private boolean _SearchChick = false;
	public void SearchButton(IRequestCycle cycle) {
		_SearchChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_ReturnChick) {
			_ReturnChick = false;
			getFanh(cycle);
		}
	}
	
	private void getFanh(IRequestCycle cycle){
		cycle.activate("Fahxgsh");
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
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
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
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END----------
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	
	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		 
		
		ToolbarButton rbtn = new ToolbarButton(null,"返回","function(){document.getElementById('ReturnButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Return);
		tb1.addItem(rbtn);
		
		tb1.addFill();
//		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
		
//		ToolbarButton rbtn = new ToolbarButton(null, "查询","function(){document.getElementById('RefurbishButton').click();}");
//		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
//		tb1.addItem(rbtn);
//		tb1.addFill();
//		// tb1.addText(new ToolbarText("<marquee width=300
//		// scrollamount=2></marquee>"));
//		setToolbar(tb1);
	}

	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(visit.getActivePageName().toString().equals("Fahxgsh")){
				
			}else{
				visit.setActivePageName(getPageName().toString());
				setBRiq(DateUtil.FormatDate(new Date()));
				setERiq(DateUtil.FormatDate(new Date()));
				visit.setString1("");
			}
		}
		blnIsBegin = true;
		getSelectData();
	}
}
